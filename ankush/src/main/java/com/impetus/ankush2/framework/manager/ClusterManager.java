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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.agent.PostProcessorDeployer;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBClusterManager;
import com.impetus.ankush2.db.DBServiceManager;
import com.impetus.ankush2.framework.Deployable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.framework.config.ProgressConfig;
import com.impetus.ankush2.framework.monitor.ClusterMonitor;
import com.impetus.ankush2.framework.utils.DatabaseUtils;
import com.impetus.ankush2.framework.utils.DeployerComparator;
import com.impetus.ankush2.framework.utils.ObjectFactory;
import com.impetus.ankush2.ganglia.GangliaConstants;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.preprocessor.DependencyDeployer;
import com.impetus.ankush2.preprocessor.PreprocessorDeployer;
import com.impetus.ankush2.utils.AnkushUtils;

public class ClusterManager {

	private String loggedInUser;

	public ClusterManager(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public ClusterManager() {
	}

	private enum ClusterOperation {
		UNDEPLOY, UNREGISTER, CUSTOM_UNINSTALL
	}

	private Map<String, Object> result = new HashMap<String, Object>();
	private ClusterConfig clusterConf;
	private Set<String> errors = new LinkedHashSet<String>();
	private AnkushLogger logger = new AnkushLogger(getClass());

	private boolean initiateRollback = false;

	// Priority Queue for components
	/** The deployable queue. */
	private PriorityQueue<Deployable> deployQueue;

	// Priority Queue for components
	/** The un deployable queue. */
	private PriorityQueue<Deployable> undeployQueue;

	private Map<String, Object> result() {
		if (errors.isEmpty()) {
			result.put(Constant.Response.Header.STATUS, true);
		} else {
			result.put(Constant.Response.Header.STATUS, false);
			result.put(Constant.Response.Header.ERROR, errors);
		}
		return result;
	}

	// Validate component inputs
	// Create configuration in case of registration
	private boolean createConfiguration() {
		logger.info("Creating component configuration...");
		Iterator<Deployable> iterator = deployQueue.iterator();

		// Create configuration
		while (iterator.hasNext()) {
			iterator.next().createConfig(clusterConf);
		}

		// TODO: progress update is for example only. Need to be removed
		iterator = deployQueue.iterator();
		while (iterator.hasNext()) {
			clusterConf.getProgress().setProgress(
					iterator.next().getComponentName(), 10);
		}
		// Return error status
		return clusterConf.getErrors().isEmpty();

	}

	/**
	 * Used to validate whether a component can delete a node
	 * 
	 * @param componentMap
	 * @return
	 */
	private boolean validateNodeDeletion(Map<String, Set<String>> componentMap) {
		try {
			logger.info("Validating nodes for deletion...");
			Iterator<Deployable> iterator = undeployQueue.iterator();

			boolean canNodeBeDeleted = true;
			// Create configuration
			while (iterator.hasNext()) {
				Deployable dep = iterator.next();
				canNodeBeDeleted = canNodeBeDeleted
						&& dep.canNodeBeDeleted(clusterConf,
								componentMap.get(dep.getComponentName()));
			}
			// Return error status
			return canNodeBeDeleted;
		} catch (Exception e) {
			logger.error("Exception while validating nodes for deletion.", e);
			return false;
		}
	}

	// Validate cluster Json
	// private boolean validateClusterJsonForNodes() {
	// logger.info("Validating cluster Json...");
	// new AgentDeployer().validateClusterJsonForNodes(clusterConf);
	// // Return error status
	// return clusterConf.getErrors().isEmpty();
	// }

	// Validate component inputs
	private boolean validateComponents(ClusterConfig newClusterConf) {
		logger.info("Validating components...");
		Iterator<Deployable> iterator = deployQueue.iterator();

		// Validate configuration
		while (iterator.hasNext()) {
			if (newClusterConf == null) {
				iterator.next().validate(clusterConf);
			} else {
				iterator.next().validateNodes(clusterConf, newClusterConf);
			}
		}

		// Return error status
		if (newClusterConf == null) {
			return clusterConf.getErrors().isEmpty();
		} else {
			return newClusterConf.getErrors().isEmpty();
		}
	}

	private void postProcess() {
		// Disconnect ssh connections
		AnkushUtils.disconnectNodes(clusterConf, clusterConf.getNodes()
				.values());
		// Set NodeConfig state to ERROR/DEPLOYED depending upon errors object
		// of NodeCOnfig.
		for (NodeConfig nodeConf : clusterConf.getNodes().values()) {
			if (nodeConf.getErrors().size() > 0) {
				nodeConf.setState(Constant.Node.State.ERROR);
			} else {
				nodeConf.setState(Constant.Node.State.DEPLOYED);
			}
		}

		// handle error condition
		new DatabaseUtils().updateClusterOperation(clusterConf);
		// update database
		new DatabaseUtils().saveCluster(clusterConf);
	}

	private boolean deploy(Deployable deployer) {

		if (clusterConf.getComponents()
				.containsKey(deployer.getComponentName())
				&& clusterConf.getComponents().get(deployer.getComponentName())
						.isRegister()) {
			logger.info(deployer.getComponentName() + " registration started.");
			return deployer.register(clusterConf);
		} else {

			if (deployer.getComponentName().equals(
					Constant.Component.Name.AGENT)
					&& (clusterConf.getNodes() == null || clusterConf
							.getNodes().size() <= 0)) {
				return true;
			} else {
				logger.info(deployer.getComponentName()
						+ " deployment started.");
				return deployer.deploy(clusterConf);
			}
		}
	}

	private boolean undeploy(Deployable deployer) {
		try {
			if (clusterConf.getComponents().containsKey(
					deployer.getComponentName())
					&& clusterConf.getComponents()
							.get(deployer.getComponentName()).isRegister()) {
				return deployer.unregister(clusterConf);
			} else {
				return deployer.undeploy(clusterConf);
			}
		} catch (Exception e) {
			return false;
		}
	}

	private void deploy() {

		try {
			// Create priority queue
			if (!createQueue(clusterConf)) {
				clusterConf.setState(Constant.Cluster.State.ERROR);
				return;
			}

			// create configuration from registering details
			if (!createConfiguration()) {
				logger.error("Creating component configuration failed.");
				clusterConf.setState(Constant.Cluster.State.ERROR);
				return;
			}
			AnkushUtils.disconnectNodes(clusterConf, clusterConf.getNodes()
					.values());
			// validate cluster Json
			// if (!validateClusterJsonForNodes()) {
			// logger.error("Cluster create Json is invalid.");
			// clusterConf.setState(Constant.Cluster.State.ERROR);
			// return;
			// }

			// TODO:License validation
			// database validation
			errors = new DatabaseUtils().validateCluster(clusterConf, true);

			if (!errors.isEmpty()) {
				clusterConf.setState(Constant.Cluster.State.ERROR);
				clusterConf.addError("validation", errors);
				return;
			}

			// Save cluster details
			String error = new DatabaseUtils().saveCluster(clusterConf);

			if (error != null) {
				errors.add(error);
				return;
			}

			// Add services into database
			DBServiceManager.getManager().addClusterServices(clusterConf,
					clusterConf.getNodes().values());

			// Create ssh connections for newly added nodes
			AnkushUtils.connectNodes(clusterConf, clusterConf.getNodes()
					.values());

			// validate components
			if (!validateComponents(null)) {
				clusterConf.setState(Constant.Cluster.State.ERROR);
				return;
			}
		} catch (Exception e) {
			logger.error(
					"Could not deploy cluster, please view server logs for more details.",
					e);
			clusterConf.setState(Constant.Cluster.State.ERROR);
			AnkushUtils.disconnectNodes(clusterConf, clusterConf.getNodes()
					.values());

			return;
		}

		// Deploy components
		while (!deployQueue.isEmpty()) {
			Deployable deployer = deployQueue.remove();
			// Add deployer into undeploy queue
			undeployQueue.add(deployer);
			logger.info(deployer.getComponentName() + " deployment started.");

			boolean deployStatus = true;

			try {
				deployStatus = deploy(deployer);
				if (deployStatus) {
					clusterConf.getProgress().setProgress(
							deployer.getComponentName(), 100);
					logger.info(deployer.getComponentName()
							+ " deployment completed.");
				}
			} catch (Exception e) {
				logger.error("Initiating rollback, exception while deploying "
						+ deployer.getComponentName(),
						deployer.getComponentName(), e);
				deployStatus = false;
			}
			if (!deployStatus) {
				logger.error(deployer.getComponentName()
						+ " deployment failed.");
				clusterConf.setState(Constant.Cluster.State.ERROR);
				clusterConf.getProgress().setProgress(
						deployer.getComponentName(), 100, true);
				new DatabaseUtils().updateOperationProgress(clusterConf);
				break;
			}
		}

		// On error undeploy components
		if (clusterConf.getState() == Constant.Cluster.State.ERROR) {
			while (!undeployQueue.isEmpty()) {
				undeploy(undeployQueue.remove());
			}
		} else {
			clusterConf.setState(Constant.Cluster.State.DEPLOYED);
		}
	}

	private void addAgentComponent(ClusterConfig newClusterConf) {
		try {
			ComponentConfig agentConf = new ComponentConfig();
			newClusterConf.getComponents().put(Constant.Component.Name.AGENT,
					agentConf);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void addNodes(ClusterConfig newClusterConf) throws AnkushException {

		// adding Agent to cluster components components
		addAgentComponent(newClusterConf);

		// Add Ganglia as component to newClusterconf with a defined nodeset
		// Also, adding gmond role to all nodes role
		if (clusterConf.getComponents().containsKey(
				Constant.Component.Name.GANGLIA)) {
			addGangliaComponent(newClusterConf);
		}

		// Create queue and validate components
		if (!createQueue(newClusterConf) || !validateComponents(newClusterConf)) {
			initiateRollback = true;
			// clusterConf.setState(Constant.Cluster.State.ERROR);
			return;
		}

		// Add services into database
		DBServiceManager.getManager().addClusterServices(clusterConf,
				newClusterConf.getNodes().values());

		// Deploy components
		while (!deployQueue.isEmpty()) {
			Deployable deployer = deployQueue.remove();
			// Add deployer into undeploy queue
			undeployQueue.add(deployer);
			// Call deploy method
			if (!deployer.addNode(clusterConf, newClusterConf)) {
				initiateRollback = true;
				// clusterConf.setState(Constant.Cluster.State.ERROR);
				break;
			}
		}

		// On error undeploy components
		if (initiateRollback) {
			while (!undeployQueue.isEmpty()) {
				Deployable deployer = undeployQueue.remove();
				deployer.removeNode(clusterConf, newClusterConf);
			}
		}
	}

	private void addGangliaComponent(ClusterConfig newClusterConf)
			throws AnkushException {
		try {
			Map<String, Map<String, Object>> gangliaNodes = new HashMap<String, Map<String, Object>>();
			Map<String, Set<String>> roles;
			for (Map.Entry<String, NodeConfig> node : newClusterConf.getNodes()
					.entrySet()) {
				// adding node to ganglia nodes
				gangliaNodes.put(node.getKey(), new HashMap<String, Object>());

				// Adding gmond role to nodeConfig's roles Object
				roles = node.getValue().getRoles();
				if (!roles.containsKey(Constant.Component.Name.GANGLIA)) {
					roles.put(Constant.Component.Name.GANGLIA,
							new HashSet<String>());
				}
				roles.get(Constant.Component.Name.GANGLIA).addAll(
						new HashSet<String>(Arrays
								.asList(GangliaConstants.Ganglia_Services.gmond
										.toString())));
			}
			// adding Ganglia component to newClusterConf
			ComponentConfig gangliaConf = new ComponentConfig();
			gangliaConf.setNodes(gangliaNodes);
			newClusterConf.getComponents().put(Constant.Component.Name.GANGLIA,
					gangliaConf);
		} catch (Exception e) {
			throw new AnkushException(
					"Could not add Ganglia Component to cluster.");
		}
	}

	// method to get nodes in their corresponding component for removal of nodes
	private Map<String, Set<String>> getComponentNodesDeletionMap(
			Set<String> nodes) {
		Map<String, Set<String>> componentMap = new HashMap<String, Set<String>>();
		try {
			// iterate over all the incoming nodes to create
			// <Component,Collection<NodeHost>> map
			for (String host : nodes) {
				// if node came for removal doesn't exist in the cluser,add
				// Error
				// and return
				if (clusterConf.getNodes().get(host) == null) {
					errors.add("Host - " + host
							+ " doesn't exist in the cluster.");
					continue;
				}
				if (errors.size() > 0) {
					return null;
				}
				// create componentMap containg all the nodes to be removed for
				// that component
				Set<String> components = new HashSet<String>(clusterConf
						.getNodes().get(host).getRoles().keySet());
				if (!components.isEmpty()) {
					// Adding Agent component
					components.add(Constant.Component.Name.AGENT.toString());
					for (String comp : components) {
						if (!componentMap.containsKey(comp)) {
							componentMap.put(comp, new HashSet<String>(
									Collections.singleton(host)));
						} else {
							componentMap.get(comp).add(host);
						}
					}
				} else {
					errors.add("No component exist on this host : " + host);
				}
			}
		} catch (Exception e) {
			errors.add("Couldn't get the components for nodes : " + nodes);
			logger.error(
					"Couldn't get the components for nodes : " + e.getMessage(),
					e);
		}
		return componentMap;

	}

	private void removeNodes(Map<String, Set<String>> componentMap) {

		// Create queue
		undeployQueue = new PriorityQueue<Deployable>(10,
				new DeployerComparator(true));
		// Add deployer into queue
		for (String componentName : componentMap.keySet()) {
			try {
				undeployQueue.add(ObjectFactory
						.getDeployerObject(componentName));
			} catch (Exception e) {
				initiateRollback = true;
				// clusterConf.setState(Constant.Cluster.State.ERROR);
				// newClusterConf.addError("configuration",
				// "could not found "
				// + componentName + " component.");
				logger.error("configuration", "could not found "
						+ componentName + " component." + e.getMessage(), e);
				return;
			}
		}

		// checking if node can be deleted, on the basis of status provided by
		// component
		List<String> nodeDeletionErrors = new ClusterMonitor()
				.canNodesBeDeleted(clusterConf, componentMap);
		if (!nodeDeletionErrors.isEmpty()) {
			addErrorsToLogger(nodeDeletionErrors);
			logger.error("Issue while validating nodes for deletion.");
			initiateRollback = true;
			return;
		}

		while (!undeployQueue.isEmpty()) {
			Deployable deployer = undeployQueue.remove();
			// Fetch component configuration
			ComponentConfig compConf = clusterConf.getComponents().get(
					deployer.getComponentName());
			// Remove nodes
			if (!deployer.removeNode(clusterConf,
					componentMap.get(deployer.getComponentName()))) {
				initiateRollback = true;
				// clusterConf.setState(Constant.Cluster.State.ERROR);
				// } else {
				// Postprocessor and Agent will have its compConf as null
			}
			if (compConf != null
					&& !deployer.getComponentName().equals(
							Constant.Component.Name.AGENT)) {
				System.out.println("Removing node from compNodes : "
						+ componentMap.get(deployer.getComponentName()));
				// Remove nodes from component
				compConf.getNodes()
						.keySet()
						.removeAll(
								componentMap.get(deployer.getComponentName()));
			}
		}
	}

	private void addErrorsToLogger(List<String> errors) {
		for (String error : errors) {
			logger.error(error);
		}
	}

	/**
	 * @param newClusterConf
	 */
	private boolean createQueue(ClusterConfig newClusterConf) {
		// Create ssh connections
		AnkushUtils.connectNodes(clusterConf, newClusterConf.getNodes()
				.values());

		// Create queue
		deployQueue = new PriorityQueue<Deployable>(10,
				new DeployerComparator());

		undeployQueue = new PriorityQueue<Deployable>(10,
				new DeployerComparator(true));
		deployQueue.add(new PreprocessorDeployer());
		deployQueue.add(new PostProcessorDeployer());
		deployQueue.add(new DependencyDeployer());
		// Add deployer into queue
		for (String componentName : newClusterConf.getComponents().keySet()) {
			try {
				deployQueue.add(ObjectFactory.getDeployerObject(componentName));
			} catch (Exception e) {
				newClusterConf.addError("configuration", "could not found "
						+ componentName + " component.");
				logger.error(e.getMessage(), e);
				return false;
			}
		}
		return true;
	}

	private void postAddNodeProcess(ClusterConfig newClusterConf) {

		// Disconnect ssh connections
		AnkushUtils.disconnectNodes(clusterConf, newClusterConf.getNodes()
				.values());

		// Update new cluster configuration
		new DatabaseUtils().updateOperationData(clusterConf, "input",
				newClusterConf);

		// handle error condition
		new DatabaseUtils().updateClusterOperation(clusterConf);

		// Update node list
		if (!initiateRollback) {
			// updating new nodes state
			for (NodeConfig nodeConf : newClusterConf.getNodes().values()) {
				if (nodeConf.getErrors().size() > 0) {
					nodeConf.setState(Constant.Node.State.ERROR);
				} else {
					nodeConf.setState(Constant.Node.State.DEPLOYED);
				}
			}
			AnkushUtils.addNodes(clusterConf, newClusterConf);
		} else {
			// Remove nodes from database
			new DatabaseUtils().removeNodes(newClusterConf.getNodes().keySet());
		}
		// update database
		// clusterConf.setState(Constant.Cluster.State.DEPLOYED);
		new DatabaseUtils().saveCluster(clusterConf);
	}

	private void postRemoveNodeProcess(List<String> nodes) {

		// Disconnect ssh connections
		AnkushUtils.disconnectCompNodes(clusterConf, nodes);

		// Update new cluster configuration
		new DatabaseUtils().updateOperationData(clusterConf, "input", nodes);

		// handle error condition
		new DatabaseUtils().updateClusterOperation(clusterConf);

		if (!initiateRollback) {
			// Update node list
			AnkushUtils.removeNodes(clusterConf, nodes);
		}

		// update database
		// clusterConf.setState(Constant.Cluster.State.DEPLOYED);
		new DatabaseUtils().saveCluster(clusterConf);
	}

	ClusterConfig roleMigrationRequiredClusterConfig;

	// Deploy or register cluster
	public Map<String, Object> deploy(ClusterConfig conf) {
		clusterConf = conf;

		clusterConf.setState(Constant.Cluster.State.DEPLOYING);
		// Add cluster name with logs
		logger.setClusterName(clusterConf.getName());

		// database validaion
		errors.addAll(new DatabaseUtils().validateCluster(clusterConf, false));

		if (!errors.isEmpty()) {
			return result();
		}

		// Save cluster details
		String error = new DatabaseUtils().saveCluster(clusterConf);
		if (error != null) {
			errors.add(error);
			return result();
		}

		// Create operation
		if (!preOperation(clusterConf, Constant.Cluster.Operation.DEPLOY,
				clusterConf.getComponents().keySet())) {
			return result();
		}

		AppStoreWrapper.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				// deploy the cluster
				try {
					deploy();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					// postProcess must be done in every case
					try {
						postProcess();
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		});

		return result();
	}

	private boolean createUndeployQueue() {
		// Create priority queue
		undeployQueue = new PriorityQueue<Deployable>(10,
				new DeployerComparator(true));
		// Add deployer into queue
		for (String componentId : clusterConf.getComponents().keySet()) {
			try {
				undeployQueue.add(ObjectFactory.getDeployerObject(componentId));
			} catch (Exception e) {
				this.errors
						.add("Could not find " + componentId + " component.");
				return false;
			}
		}
		undeployQueue.add(new PreprocessorDeployer());
		return true;
	}

	private void delete(final Long clusterId, final ClusterOperation operation) {
		// Get cluster details
		Cluster cluster = new DBClusterManager().getCluster(clusterId);
		if (cluster == null) {
			this.errors.add("Could not find cluster details.");
			return;
		}
		this.clusterConf = cluster.getClusterConfig();

		this.clusterConf.setState(Constant.Cluster.State.REMOVING);
		if (!createUndeployQueue()) {
			return;
		}

		// Create operation
		preOperation(clusterConf, Constant.Cluster.Operation.REMOVE,
				clusterConf.getComponents().keySet());

		// Save cluster details
		new DatabaseUtils().saveCluster(clusterConf);

		AppStoreWrapper.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				// Undeploy the cluster
				if (!clusterConf.getState()
						.equals(Constant.Cluster.State.ERROR)) {

					// Create ssh connections
					AnkushUtils.connectNodes(clusterConf, clusterConf
							.getNodes().values());

					// process components
					while (!undeployQueue.isEmpty()) {
						switch (operation) {
						case UNDEPLOY:
							undeployQueue.remove().undeploy(clusterConf);
							break;

						case UNREGISTER:
							undeployQueue.remove().unregister(clusterConf);
							break;
						case CUSTOM_UNINSTALL:
							undeploy(undeployQueue.remove());
							break;
						}
					}
					// Disconnect ssh connections
					AnkushUtils.disconnectNodes(clusterConf, clusterConf
							.getNodes().values());
				}
				// Remove cluster from database.
				new DBClusterManager().remove(clusterId);
			}
		});
	}

	// Undeploy or Unregister cluster
	public Map<String, Object> undeploy(final Long clusterId) {
		delete(clusterId, ClusterOperation.UNDEPLOY);
		return result();
	}

	// Undeploy all components
	public Map<String, Object> customUninstall(final Long clusterId) {
		delete(clusterId, ClusterOperation.CUSTOM_UNINSTALL);
		return result();
	}

	// Unregister all components
	public Map<String, Object> unregister(final Long clusterId) {
		delete(clusterId, ClusterOperation.UNREGISTER);
		return result();
	}

	public Map<String, Object> addNodes(final long clusterId,
			final ClusterConfig newClusterConf) {
		// Get cluster details
		Cluster cluster = new DBClusterManager().getCluster(clusterId);
		if (cluster == null) {
			this.errors.add("Could not find cluster details.");
			return result();
		}

		this.clusterConf = cluster.getClusterConfig();

		// TODO:License validation

		// database validaion
		errors.addAll(new DatabaseUtils().validateCluster(this.clusterConf,
				newClusterConf, true));

		if (!errors.isEmpty()) {
			return result();
		}

		// Create operation
		if (!preOperation(newClusterConf, Constant.Cluster.Operation.ADD_NODE,
				newClusterConf.getComponents().keySet())) {
			return result();
		}

		// Save nodes
		String error = new DatabaseUtils().saveNodes(clusterConf,
				newClusterConf.getNodes().values());
		if (error != null) {
			errors.add(error);
			return result();
		}

		// Save cluster details
		error = new DatabaseUtils().saveCluster(clusterConf);
		if (error != null) {
			errors.add(error);
			return result();
		}

		AppStoreWrapper.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				// deploy the cluster
				try {
					addNodes(newClusterConf);
					postAddNodeProcess(newClusterConf);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});

		return result();
	}

	// TODO: newClusterConf must be created carefully.
	public Map<String, Object> removeNodes(final long clusterId,
			final List<String> nodes) {
		// Get cluster details
		Cluster cluster = new DBClusterManager().getCluster(clusterId);
		if (cluster == null) {
			this.errors.add("Could not find cluster details.");
			return result();
		}

		this.clusterConf = cluster.getClusterConfig();

		// Save cluster details
		String error = new DatabaseUtils().saveCluster(clusterConf);
		if (error != null) {
			errors.add(error);
			return result();
		}
		// map containing <Component , Set<HostString>> for each component
		// Also added Agent and PostProcessor for all the nodes
		final Map<String, Set<String>> componentMap = getComponentNodesDeletionMap(new HashSet<String>(
				nodes));

		if (!preOperation(nodes, Constant.Cluster.Operation.REMOVE_NODE,
				componentMap.keySet())) {
			return result;
		}

		// removeNode only if all the nodes are mapped to their components
		if (errors == null || errors.size() == 0) {
			// Create ssh connections
			AnkushUtils.connectNodesString(clusterConf, nodes);
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					// deploy the cluster
					try {
						removeNodes(componentMap);
						postRemoveNodeProcess(nodes);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			});
		}
		return result();
	}

	public Map<String, Object> redeploy(final Long clusterId,
			final ClusterConfig clusterConfig) {
		try {
			final DBClusterManager clusterManager = new DBClusterManager();
			// Get cluster details
			Cluster cluster = clusterManager.getCluster(clusterId);
			if (cluster == null) {
				this.errors.add("Could not find cluster details.");
				return result();
			}
			this.clusterConf = cluster.getClusterConfig();
			final String state = cluster.getState();
			if (!state.equals(Constant.Cluster.State.ERROR.toString())
					&& !state.equals(Constant.Cluster.State.SERVER_CRASHED
							.toString())) {
				errors.add("Cluster is in " + state
						+ " state, could not redeploy cluster.");
				return result();
			}

			this.clusterConf.setState(Constant.Cluster.State.REMOVING);
			this.clusterConf.incrementOperation();
			cluster = clusterManager.saveCluster(cluster);

			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					// if cluster state is SERVER_CRASHED ,then custom_uninstall
					if (state.equals(Constant.Cluster.State.SERVER_CRASHED
							.toString())) {
						customUninstall(clusterId);
					}
					// remove cluster record.
					clusterManager.remove(clusterId);
					// create cluster.
					deploy(clusterConfig);
				}
			});

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result();
	}

	private synchronized boolean preOperation(final Object input,
			Constant.Cluster.Operation operation, Set<String> components) {

		try {
			clusterConf.incrementOperation();
			// Enable database logs
			logger.setCluster(this.clusterConf);
			clusterConf.setProgress(new ProgressConfig(components));
			// Save operation details
			DatabaseUtils databaseUtils = new DatabaseUtils();
			databaseUtils.addClusterOperation(clusterConf, operation,
					this.loggedInUser);
			// Add operation input data
			databaseUtils.updateOperationData(clusterConf, "input", input);
			return true;
		} catch (AnkushException e) {
			errors.add(e.getMessage());
		} catch (Exception e) {
			errors.add("Exception while incrementing operation id and adding operation to operations table.");
		}
		return false;
	}

}
