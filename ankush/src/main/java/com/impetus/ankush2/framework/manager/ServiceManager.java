/*******************************************************************************
 * ===========================================================
 * Ankush : Big Data Cluster Management Solution
 * ===========================================================
 * 
 * (C) Copyright 2014, by Impetus Technologies
 * 
 * This is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation;
 * 
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/
package com.impetus.ankush2.framework.manager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.domain.Tile;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBClusterManager;
import com.impetus.ankush2.db.DBOperationManager;
import com.impetus.ankush2.db.DBServiceManager;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.ProgressConfig;
import com.impetus.ankush2.framework.config.ServiceConf;
import com.impetus.ankush2.framework.utils.DatabaseUtils;
import com.impetus.ankush2.framework.utils.ObjectFactory;
import com.impetus.ankush2.framework.utils.ServiceComparator;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

public class ServiceManager {

	private AnkushLogger logger = new AnkushLogger(getClass());
	private Map<String, Object> result = new HashMap<String, Object>();
	private Set<String> errors = new LinkedHashSet<String>();

	private ClusterConfig clusterConfig;
	private Cluster dbCluster;

	// Priority Queue for components
	/** The service queue. */
	private PriorityQueue<Serviceable> componentQueue;

	private static final String CLUSTER_IN_MAINTENANCE_STATE = "Could not perform operation as Cluster is not in deployed state.";
	private static final String COULD_NOT_FIND_CLUSTER = "Could not find cluster. Please provide valid cluster id.";
	private static final String COULD_NOT_FIND_CLUSTER_CONFIG = "Could not find cluster configuration details.";
	private static final String SERVICE_MANAGE_OPERATION = "serviceOperation";

	private Long tileId;

	Map<String, Object> input;
	
	private String loggedInUser;

	private String errMessage(String action, String type) {
		return "Could not " + action + " " + type
				+ ". Please view server logs for more details.";
	}

	public ServiceManager(Long clusterId, String loggedInUser) throws AnkushException {

		// Getting cluster object from db
		dbCluster = new DBClusterManager().getCluster(clusterId);
		if (dbCluster == null) {
			throw new AnkushException(COULD_NOT_FIND_CLUSTER);
		}
		if (!dbCluster.getState().equals(
				Constant.Cluster.State.DEPLOYED.toString())) {
			throw new AnkushException(CLUSTER_IN_MAINTENANCE_STATE);
		}
		clusterConfig = dbCluster.getClusterConfig();
		if (clusterConfig == null) {
			throw new AnkushException(COULD_NOT_FIND_CLUSTER_CONFIG);
		}

		clusterConfig.incrementOperation();
		this.loggedInUser = loggedInUser;

		logger.setCluster(clusterConfig);
		// preparing input data to save in operation confbytes
		input = new HashMap<String, Object>();
		input.put("clusterId", clusterConfig.getClusterId());
	}

	/**
	 * Return result.
	 * 
	 * @return The result object.
	 */
	private Map returnResult() {
		if (this.errors.isEmpty()) {
			this.result.put("status", true);
			this.result.put("message", "Operation submitted successfully.");
		} else {
			this.result.put("status", false);
			this.result.put("error", this.errors);
			this.result.put("message", "Operation submition failed.");
		}
		return this.result;
	}

	public void validateNode(String host) throws AnkushException {
		// checking node existence in cluster nodes
		if (!clusterConfig.getNodes().containsKey(host)) {
			throw new AnkushException("Could not find " + host + " node.");
		}
	}

	public void validateComponent(String component) throws AnkushException {
		// checking node existence in cluster nodes
		if (component == null
				|| !clusterConfig.getComponents().containsKey(component)) {
			throw new AnkushException("Could not find " + component
					+ " component.");
		}
	}

	private boolean preNodeService(String host, boolean isStop,
			Map<String, Set<String>> componentRolesMap) {
		// creating component queue
		if (!createComponentQueue(componentRolesMap.keySet(), isStop)) {
			return false;
		}
		// Connect to node
		AnkushUtils.connectNodesString(clusterConfig, Arrays.asList(host));
		return true;
	}

	private void postNodeService(String host) {
		// Disconnect node
		SSHExec connection = clusterConfig.getNodes().get(host).getConnection();
		if (connection != null) {
			connection.disconnect();
			clusterConfig.getNodes().get(host).setConnection(null);
		}
		// updating operation completion time and status
		updateOperationProgress();
	}

	private boolean preClusterService(boolean order) throws AnkushException,
			Exception {

		// Adding Agent to cluster component set
		Set<String> componentSet = new HashSet<String>(clusterConfig
				.getComponents().keySet());
		componentSet.add(Constant.Component.Name.AGENT);

		// create component queue
		if (!createComponentQueue(componentSet, order)) {
			return false;
		}

		// Creating node connection for component nodes
		AnkushUtils.connectNodes(clusterConfig, clusterConfig.getNodes()
				.values());

		return true;
	}

	/**
	 * Method to perform the start action on services
	 * 
	 * @param clusterId
	 *            {@link Long}
	 * @param serviceConf
	 *            {@link ServiceConf}
	 * @return {@link Map}
	 * @throws Exception
	 */
	public Map startServices(final String host,
			final Map<String, Set<String>> services) throws Exception {

		// creating component queue and updating HAConfig.xml
		if (preNodeService(host, false, services)) {

			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// preparing input to save in operation confbytes
					prepareInputData(host, Constant.ServiceAction.START, null,
							services);
					// Save operation details
					addOperation(Constant.Cluster.Operation.START_SERVICES,
							clusterConfig.getNodes().get(host).getRoles()
									.keySet());

					boolean status = true;

					// Update force stop flag
					DBServiceManager.getManager()
							.setServicesForceStop(clusterConfig.getClusterId(),
									host, services, false);

					while (!componentQueue.isEmpty()) {
						Serviceable serviceable = componentQueue.remove();
						status = status
								&& serviceable.startServices(
										clusterConfig,
										host,
										new HashSet<String>(services
												.get(serviceable
														.getComponentName())));

						// setting component wise progress
						clusterConfig.getProgress().setProgress(
								serviceable.getComponentName(), 100);
					}

					postNodeService(host);
				}
			});
		}

		return returnResult();
	}

	public Map stopServices(final String host,
			final Map<String, Set<String>> services) throws Exception {

		// creating component queue and updating HAConfig.xml
		if (preNodeService(host, true, services)) {

			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {

					// preparing input to save in operation confbytes
					prepareInputData(host, Constant.ServiceAction.STOP, null,
							services);
					// Save operation details
					addOperation(Constant.Cluster.Operation.STOP_SERVICES,
							clusterConfig.getNodes().get(host).getRoles()
									.keySet());

					boolean status = true;

					// Update force stop flag
					DBServiceManager.getManager().setServicesForceStop(
							clusterConfig.getClusterId(), host, services, true);

					while (!componentQueue.isEmpty()) {
						Serviceable service = componentQueue.remove();
						status = status
								&& service.stopServices(
										clusterConfig,
										host,
										new HashSet<String>(
												services.get(service
														.getComponentName())));
						// setting component wise progress
						clusterConfig.getProgress().setProgress(
								service.getComponentName(), 100);
					}

					postNodeService(host);
				}
			});
		}

		return returnResult();
	}

	private Map<String, Set<String>> getComponentRoleMap(String host)
			throws AnkushException, Exception {
		Map<String, Set<String>> componentRolesMap = clusterConfig.getNodes()
				.get(host).getRoles();
		if (componentRolesMap == null) {
			throw new AnkushException("Could not get Component Role map.");
		}
		// Adding Agent component in roles
		componentRolesMap.put(Constant.Component.Name.AGENT,
				new HashSet<String>(Arrays.asList(Constant.Role.AGENT)));
		return componentRolesMap;
	}

	public Map startNode(final String host) throws Exception {
		try {
			// creating component queue and updating HAConfig.xml
			if (preNodeService(host, false, getComponentRoleMap(host))) {
				// Starting services
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						// preparing input to save in operation confbytes
						prepareInputData(host, Constant.ServiceAction.START,
								null, null);
						// Save operation details
						addOperation(Constant.Cluster.Operation.START_NODE,
								clusterConfig.getNodes().get(host).getRoles()
										.keySet());

						boolean status = true;

						// Update force stop flag
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), host, null, null,
								null, null, false);

						while (!componentQueue.isEmpty()) {
							Serviceable serviceable = componentQueue.remove();
							status = status
									&& serviceable.startNode(clusterConfig,
											host);
							// setting component wise progress
							clusterConfig.getProgress().setProgress(
									serviceable.getComponentName(), 100);
						}
						postNodeService(host);
					}
				});
			}

		} catch (AnkushException e) {
			errors.add(e.getMessage());
		} catch (Exception e) {
			errors.add(errMessage("start", "node"));
		}
		return returnResult();
	}

	public Map stopNode(final String host) throws Exception {
		try {
			// creating component queue and updating HAConfig.xml
			if (preNodeService(host, true, getComponentRoleMap(host))) {
				// Starting services
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {

						// preparing input to save in operation confbytes
						prepareInputData(host, Constant.ServiceAction.STOP,
								null, null);
						// Save operation details
						addOperation(Constant.Cluster.Operation.STOP_NODE,
								clusterConfig.getNodes().get(host).getRoles()
										.keySet());
						boolean status = true;

						// Update force stop flag
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), host, null, null,
								null, null, true);

						while (!componentQueue.isEmpty()) {
							Serviceable serviceable = componentQueue.remove();
							status = status
									&& serviceable
											.stopNode(clusterConfig, host);
							// setting component wise progress
							clusterConfig.getProgress().setProgress(
									serviceable.getComponentName(), 100);
						}
						// node disconnect
						postNodeService(host);
					}
				});
			}

		} catch (AnkushException e) {
			errors.add(e.getMessage());
		} catch (Exception e) {
			errors.add(errMessage("stop", "node"));
		}
		return returnResult();
	}

	/**
	 * Used to start component
	 * 
	 * @param clusterId
	 *            {@link Long}
	 * @param componentName
	 *            {@link String}
	 * 
	 * @return {@link Map}
	 */
	public Map startComponent(final String componentName, final String role)
			throws Exception {
		try {
			final ComponentConfig componentConfig = clusterConfig
					.getComponents().get(componentName);
			final Serviceable componentServiceManager = ObjectFactory
					.getServiceObject(componentName);
			if (componentServiceManager == null) {
				throw new AnkushException("Could not find service manager for "
						+ componentName + "component.");
			}
			// Initializing nodes on which to create connection and operation
			// name depending whether need to start role or component
			final Set<String> nodeSet;
			final Constant.Cluster.Operation opName;
			if (role == null || role.isEmpty()) {
				nodeSet = componentConfig.getNodes().keySet();
				opName = Constant.Cluster.Operation.START_COMPONENT;
			} else {
				nodeSet = getRoleNodeSet(componentName, componentConfig
						.getNodes().keySet(), role);
				opName = Constant.Cluster.Operation.START_ROLE;
				input.put("role", role);
			}
			// Creating node connection for component nodes
			AnkushUtils.connectNodesString(clusterConfig, nodeSet);
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					// preparing input to save in operation confbytes
					prepareInputData(null, Constant.ServiceAction.START,
							componentName, null);
					input.put("component", componentName);
					input.put("action", Constant.ServiceAction.START);
					addOperation(opName,
							new HashSet<String>(Arrays.asList(componentName)));
					// Update force stop flag
					if (role == null || role.isEmpty()) {
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), null,
								componentName, null, null, null, false);
						componentServiceManager.start(clusterConfig);
					} else {
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), null, null, role,
								null, null, false);
						componentServiceManager.startRole(clusterConfig, role);
					}
					// setting component wise progress
					clusterConfig.getProgress().setProgress(componentName, 100);
					// Disconnect node connection for component nodes
					AnkushUtils.disconnectCompNodes(clusterConfig, nodeSet);
					updateOperationProgress();
				}
			});

		} catch (AnkushException e) {
			errors.add(e.getMessage());
		} catch (Exception e) {
			errors.add(errMessage("start", "component"));
		}
		return returnResult();
	}

	public Map stopComponent(final String componentName, final String role)
			throws Exception {
		try {
			final ComponentConfig componentConfig = clusterConfig
					.getComponents().get(componentName);

			final Serviceable componentServiceManager = ObjectFactory
					.getServiceObject(componentName);

			if (componentServiceManager == null) {
				throw new AnkushException("Could not find service manager for "
						+ componentName + "component.");
			}
			// Initializing nodes on which to create connection and operation
			// name depending whether need to start role or component
			final Set<String> nodeSet;
			final Constant.Cluster.Operation opName;
			if (role == null || role.isEmpty()) {
				nodeSet = componentConfig.getNodes().keySet();
				opName = Constant.Cluster.Operation.STOP_COMPONENT;
			} else {
				nodeSet = getRoleNodeSet(componentName, componentConfig
						.getNodes().keySet(), role);
				opName = Constant.Cluster.Operation.STOP_ROLE;
				input.put("role", role);
			}

			// Creating node connection for component nodes
			AnkushUtils.connectNodesString(clusterConfig, nodeSet);
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					// preparing input to save in operation confbytes
					prepareInputData(null, Constant.ServiceAction.STOP,
							componentName, null);
					addOperation(opName,
							new HashSet<String>(Arrays.asList(componentName)));

					// Update force stop flag
					if (role == null || role.isEmpty()) {
						// Update force stop flag
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), null,
								componentName, null, null, null, true);
						componentServiceManager.stop(clusterConfig);
					} else {
						// Update force stop flag
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), null, null, role,
								null, null, true);
						componentServiceManager.stopRole(clusterConfig, role);
					}
					// setting component wise progress
					clusterConfig.getProgress().setProgress(componentName, 100);
					// Disconnect node connection for component nodes
					AnkushUtils.disconnectCompNodes(clusterConfig, nodeSet);
					updateOperationProgress();
				}
			});

		} catch (AnkushException e) {
			errors.add(e.getMessage());
		} catch (Exception e) {
			errors.add(errMessage("stop", "component"));
		}
		return returnResult();
	}

	/**
	 * @param componentName
	 *            {@link String}
	 * @param componentNodes
	 *            {@link Set}
	 * @param role
	 *            {@link String}
	 * @return Collection of nodes on which the specified role exists
	 */
	private Set<String> getRoleNodeSet(String componentName,
			Set<String> componentNodes, String role) throws AnkushException {
		Set<String> roleNodes = new HashSet<String>();
		try {
			Map<String, Set<String>> roles;
			for (String node : componentNodes) {
				if (!clusterConfig.getNodes().containsKey(node)) {
					throw new AnkushException("Could not found component node "
							+ node + " in cluster nodes");
				}
				roles = clusterConfig.getNodes().get(node).getRoles();
				if (roles.containsKey(componentName)
						&& roles.get(componentName).contains(role)) {
					roleNodes.add(node);
				}
			}
			if (roleNodes.isEmpty()) {
				throw new AnkushException("Could not find any node on which "
						+ role + " is installed.");
			}
			return roleNodes;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while getting nodes on which "
							+ role + " exists.", e);
		}
	}

	/**
	 * Used to create operation tile
	 */
	private void createOperationTile(String operation) {

		Map<String, Object> data = new HashMap<String, Object>();
		Long operationDbId = new DBOperationManager()
				.getOperationDbId(clusterConfig);
		if (operationDbId == null) {
			logger.error("Could not get operation id for operation "
					+ operation);
			return;
		}
		data.put("operationDbId", operationDbId);
		data.put("clusterId", clusterConfig.getClusterId());
		data.put("operationId", clusterConfig.getOperationId());

		// Create operation tile
		TileInfo tileInfo = new TileInfo();
		tileInfo.setLine1(operation);
		tileInfo.setLine2("Is in progress");
		tileInfo.setUrl(SERVICE_MANAGE_OPERATION);
		tileInfo.setStatus(Constant.Tile.Status.NORMAL);
		tileInfo.setData(data);
		Tile tile = new TileManager().saveTile(tileInfo, dbCluster.getId());
		this.tileId = tile.getId();
	}

	/**
	 * Used to destroy operation tile
	 */
	private void removeOperationTile() {
		// Fetch operation tile
		TileManager tileManager = new TileManager();
		Tile tile = tileManager.getTile(tileId);

		if (tile != null) {
			TileInfo tileInfo = tile.getTileInfoObj();
			// TODO: Deciding operation fail or completed using clusterConfig
			// data
			// Save tile
			tileInfo.setUrl(null);
			tile.setTileInfoObj(tileInfo);
			tile.setDestroy(true);
			tileManager.saveTile(tile);
		}
	}

	public Map startCluster() throws Exception {
		try {
			// TODO: check if there is any other operation is in progress
			// creating component queue and nodes connection
			if (preClusterService(false)) {
				// managing cluster services
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {

						// preparing input to save in operation confbytes
						prepareInputData(null, Constant.ServiceAction.START,
								null, null);
						// Save operation details
						addOperation(Constant.Cluster.Operation.START_CLUSTER,
								clusterConfig.getComponents().keySet());

						boolean status = true;

						// Update force stop flag
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), null, null, null,
								null, null, false);

						while (!componentQueue.isEmpty()) {
							Serviceable serviceable = componentQueue.remove();
							status = status && serviceable.start(clusterConfig);
							// setting component wise progress
							clusterConfig.getProgress().setProgress(
									serviceable.getComponentName(), 100);
						}

						AnkushUtils.disconnectNodes(clusterConfig,
								clusterConfig.getNodes().values());

						// updating operation completion time , progress and
						// status
						updateOperationProgress();
					}
				});
			}

		} catch (AnkushException e) {
			errors.add(e.getMessage());
		} catch (Exception e) {
			errors.add(errMessage("start", "cluster"));
		}

		return returnResult();
	}

	public Map stopCluster() throws Exception {
		try {
			// creating component queue and nodes connection
			if (preClusterService(true)) {
				// managing cluster services
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {

						// preparing input to save in operation confbytes
						prepareInputData(null, Constant.ServiceAction.STOP,
								null, null);
						// Save operation details
						addOperation(Constant.Cluster.Operation.STOP_CLUSTER,
								clusterConfig.getComponents().keySet());
						boolean status = true;

						// Update force stop flag
						DBServiceManager.getManager().setForceStop(
								clusterConfig.getClusterId(), null, null, null,
								null, null, true);

						while (!componentQueue.isEmpty()) {
							Serviceable serviceable = componentQueue.remove();
							status = status && serviceable.stop(clusterConfig);
							// setting component wise progress
							clusterConfig.getProgress().setProgress(
									serviceable.getComponentName(), 100);
						}
						AnkushUtils.disconnectNodes(clusterConfig,
								clusterConfig.getNodes().values());

						// updating operation completion time and status
						updateOperationProgress();
					}
				});
			}

		} catch (AnkushException e) {
			errors.add(e.getMessage());
		} catch (Exception e) {
			errors.add(errMessage("stop", "cluster"));
		}
		return returnResult();
	}

	private boolean createComponentQueue(Set<String> componentSet,
			boolean serviceOrder) {
		// create component queue
		componentQueue = new PriorityQueue<Serviceable>(10,
				new ServiceComparator(serviceOrder));

		for (String componentName : componentSet) {
			try {
				componentQueue.add(ObjectFactory
						.getServiceObject(componentName));
			} catch (AnkushException e) {
				errors.add(e.getMessage());
				return false;
			} catch (Exception e) {
				errors.add("Unable to find " + componentName
						+ " component, so could not create component queue.");
				return false;
			}
		}
		return true;
	}

	private void addOperation(Constant.Cluster.Operation opName,
			Collection<String> components) {
		// Save operation details
		clusterConfig.setProgress(new ProgressConfig(components));
		DatabaseUtils databaseUtils = new DatabaseUtils();
		databaseUtils.addClusterOperation(clusterConfig, opName,
				this.loggedInUser != null ? this.loggedInUser : "");
		// Add operation input data
		databaseUtils.updateOperationData(clusterConfig, "input", input);
	}

	private void updateOperationProgress() {
		try {
			Operation operation = new DBOperationManager()
					.getOperation(clusterConfig);
			if (operation == null) {
				logger.error("Could not find operation.");
				return;
			}
			operation.setCompletedAt(new Date());
			if (clusterConfig.getErrors().isEmpty()) {
				operation.setStatus(Constant.Operation.Status.COMPLETED
						.toString());
			} else {
				operation.setStatus(Constant.Operation.Status.ERROR.toString());
			}
			// setting operation progress
			DatabaseUtils databaseUtils = new DatabaseUtils();
			databaseUtils.updateOperationProgress(clusterConfig);
			// Add operation output data
			databaseUtils.updateOperationData(clusterConfig, "output",
					clusterConfig);
			new DBOperationManager().saveOperation(operation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void prepareInputData(String host, Constant.ServiceAction action,
			String component, Map<String, Set<String>> services) {
		input.put("action", action);
		if (services != null) {
			input.put("services", services);
		}
		if (host != null) {
			input.put("host", host);
		}
		if (component != null) {
			input.put("component", host);
		}

	}

}
