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
/**
 * 
 */
package com.impetus.ankush.oraclenosql;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.Tile;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.ClusterManager;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * Manage cluster configuration and services.
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLService implements ServiceMonitorable {

	/** The Constant STATUS. */
	private static final String STATUS = "status";

	/** The Constant ID. */
	private static final String ID = "id";

	/** The Constant ID_ERROR_MESSAGE. */
	private static final String ID_ERROR_MESSAGE = "Id is missing.";

	/** The Constant LASTLOGTIMESTAMP. */
	private static final String LASTLOGTIMESTAMP = "lastlogtimestamp";

	/** The Constant USER. */
	private static final String USER = "user";

	/** The Constant MIN_REP_FACTOR. */
	private static final int MIN_REP_FACTOR = 3;

	/** The Constant TOPOLOGY_OPERATION. */
	private static final String TOPOLOGY_OPERATION = "topologyOperation";

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(OracleNoSQLService.class);

	/** The cluster manager. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager("clusterManager", Cluster.class);

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The cluster config. */
	private OracleNoSQLConf conf;

	/** The errors. */
	private List<String> errors = new ArrayList<String>();

	/** The result. */
	private Map<String, Object> result = new HashMap<String, Object>();

	/** The database cluster object. */
	private Cluster dbCluster;

	/** The parameter map. */
	private Map<String, String> parameterMap;

	/** The post data. */
	private Object postData;

	/**
	 * Start replication nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void startrepnode() throws AnkushException {
		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			plan.startRepNode((List<List<String>>) postData);
		} catch (Exception e) {
			throw new AnkushException(
					"Could not start selected replication nodes.");
		}
	}

	/**
	 * Stop replication nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void stoprepnode() throws AnkushException {
		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			plan.stopRepNode((List<List<String>>) postData);
		} catch (Exception e) {
			throw new AnkushException(
					"Could not stop selected replication nodes.");
		}
	}

	/**
	 * Start all replication nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void startallrepnodes() throws AnkushException {
		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			plan.startAllRepNode();
		} catch (Exception e) {
			throw new AnkushException("Could not start all replication nodes.");
		}
	}

	/**
	 * Stop all replication nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void stopallrepnodes() throws AnkushException {
		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			plan.stopAllRepNode();
		} catch (Exception e) {
			throw new AnkushException("Could not stop all replication nodes.");
		}
	}

	/**
	 * Start all storage nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void startallstoragenodes() throws AnkushException {
		if (!new OracleNoSQLDeployer().startServices(setStorageNodes(null),
				this.conf)) {
			throw new AnkushException("Could not start all storage nodes.");
		}
	}

	/**
	 * Stop all storage nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void stopallstoragenodes() throws AnkushException {
		if (!new OracleNoSQLDeployer().stopServices(setStorageNodes(null),
				this.conf)) {
			throw new AnkushException("Could not stop all storage nodes.");
		}
	}

	/**
	 * Start storage nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void startstoragenode() throws AnkushException {
		if (!new OracleNoSQLDeployer().startServices(
				setStorageNodes((List<String>) postData), conf)) {
			throw new AnkushException("Could not start selected storage nodes.");
		}
	}

	/**
	 * Stop storage nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void stopstoragenode() throws AnkushException {
		if (!new OracleNoSQLDeployer().stopServices(
				setStorageNodes((List<String>) postData), this.conf)) {
			throw new AnkushException("Could not stop selected storage nodes.");
		}
	}

	/**
	 * Remove unchanged entries.
	 * 
	 * @param oldMap
	 *            the old map
	 * @param newMap
	 *            the new map
	 * @return the map
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private Map<String, String> cleanUpParameterMap(Map<String, String> oldMap,
			Map<String, String> newMap) throws AnkushException {
		Map<String, String> params = new HashMap<String, String>();
		for (String key : newMap.keySet()) {
			if (!newMap.get(key).equals(oldMap.get(key))) {
				params.put(key, newMap.get(key));
			}
		}
		if (params.isEmpty()) {
			throw new AnkushException("Parameters are unchanged.");
		}
		return params;
	}

	/**
	 * Change Admin Parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void adminparams() throws AnkushException {
		Map<String, String> params = (Map<String, String>) postData;
		String user = params.remove(USER);
		params = cleanUpParameterMap(new OracleNoSQLMonitor().monitor(
				dbCluster, "adminparams", null), params);

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.changeAllAdminParams(params);
		new ConfigurationManager().saveConfiguration(conf.getClusterDbId(),
				user, "Admin Parameters", "Admin", params);
	}

	/**
	 * Change Policy Parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void policyparams() throws AnkushException {
		Map<String, String> params = (Map<String, String>) postData;
		String user = params.remove(USER);

		params = cleanUpParameterMap(new OracleNoSQLMonitor().monitor(
				dbCluster, "policyparams", null), params);

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.changePolicyParams(params);
		new ConfigurationManager().saveConfiguration(conf.getClusterDbId(),
				user, "Policy Parameters", "Policy", params);
	}

	/**
	 * Change All Replication Nodes Parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void allrepnodeparams() throws AnkushException {
		Map<String, String> params = (Map<String, String>) postData;
		String user = params.remove(USER);

		Map<String, String> newParams = new HashMap<String, String>();

		for (String key : params.keySet()) {
			if (!params.get(key).trim().isEmpty()) {
				newParams.put(key, params.get(key));
			}
		}

		if (newParams.isEmpty()) {
			throw new AnkushException("Parameters are unchanged.");
		}

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.changeAllRepNodeParams(newParams);
		new ConfigurationManager().saveConfiguration(conf.getClusterDbId(),
				user, "All RepNode Parameters", "RepNode", newParams);
	}

	/**
	 * Change Replication Node Parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void repnodeparams() throws AnkushException {
		Map<String, String> params = (Map<String, String>) postData;
		String user = params.remove(USER);

		if (!parameterMap.containsKey("rgid")
				|| !parameterMap.containsKey("rnid")) {
			throw new AnkushException("Either rnid or rgid is missing.");
		}

		params = cleanUpParameterMap(new OracleNoSQLMonitor().monitor(
				dbCluster, "repnodeparams", parameterMap), params);

		int groupId = Integer.parseInt(parameterMap.get("rgid"));
		int nodeNum = Integer.parseInt(parameterMap.get("rnid"));

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.changeRepNodeParams(groupId, nodeNum, params);
		new ConfigurationManager().saveConfiguration(conf.getClusterDbId(),
				user, "RepNode Parameters", "rg" + groupId + "-rn" + nodeNum,
				params);
	}

	/**
	 * Change Storage Node parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void storagenodeparams() throws AnkushException {
		Map<String, String> params = (Map<String, String>) postData;
		String user = params.remove(USER);

		if (!parameterMap.containsKey(ID)) {
			throw new AnkushException(ID_ERROR_MESSAGE);
		}

		params = cleanUpParameterMap(new OracleNoSQLMonitor().monitor(
				dbCluster, "storagenodeparams", parameterMap), params);

		int snId = Integer.parseInt(parameterMap.get(ID));

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.changeStorageNodeParams(snId, params);

		new ConfigurationManager().saveConfiguration(conf.getClusterDbId(),
				user, "StorageNode Parameters", "sn" + snId, params);
	}

	/**
	 * Interrupt a plan.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void interruptplan() throws AnkushException {
		if (!parameterMap.containsKey(ID)) {
			throw new AnkushException(ID_ERROR_MESSAGE);
		}

		int planId = Integer.parseInt(parameterMap.get(ID));

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.interruptPlan(planId);
	}

	/**
	 * Cancel an interrupted plan.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void cancelplan() throws AnkushException {
		if (!parameterMap.containsKey(ID)) {
			throw new AnkushException(ID_ERROR_MESSAGE);
		}

		int planId = Integer.parseInt(parameterMap.get(ID));

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.cancelPlan(planId);
	}

	/**
	 * Wait for a running plan.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void waitplan() throws AnkushException {
		if (!parameterMap.containsKey(ID)) {
			throw new AnkushException(ID_ERROR_MESSAGE);
		}

		int planId = Integer.parseInt(parameterMap.get(ID));

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.waitPlan(planId);
	}

	/**
	 * Executeplan.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void executeplan() throws AnkushException {
		if (!parameterMap.containsKey(ID)) {
			throw new AnkushException(ID_ERROR_MESSAGE);
		}

		int planId = Integer.parseInt(parameterMap.get(ID));

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.executePlan(planId);
	}

	/**
	 * Executeandwaitplan.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void executeandwaitplan() throws AnkushException {
		if (!parameterMap.containsKey(ID)) {
			throw new AnkushException(ID_ERROR_MESSAGE);
		}

		int planId = Integer.parseInt(parameterMap.get(ID));

		OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
		plan.executeAndWaitPlan(planId);
	}

	/**
	 * Change Replication Factor.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void changerepfactor() throws AnkushException {
		final int repFactor = Integer.parseInt(parameterMap.get("repfactor"));
		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			plan.changeRepFactor(repFactor);
		} catch (AnkushException e) {
			logger.error(e.getMessage());
			conf.getClusterConf().setState(Constant.Cluster.State.ERROR);
		}
		postOperation();
	}

	/**
	 * Rebalance topology.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void rebalance() throws AnkushException {
		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			plan.rebalanceTopology();
		} catch (AnkushException e) {
			logger.error(e.getMessage());
			conf.getClusterConf().setState(Constant.Cluster.State.ERROR);
		}
		postOperation();
	}

	/**
	 * Re-distribute topology.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void redistribute() throws AnkushException {
		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			plan.redistribute();
		} catch (AnkushException e) {
			logger.error(e.getMessage());
			conf.getClusterConf().setState(Constant.Cluster.State.ERROR);
		}
		postOperation();
	}

	/**
	 * Migrate StorageNode.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void migratenode() throws AnkushException {

		int oldSnId = Integer.parseInt(parameterMap.get("oldsnid"));
		int newSnId = Integer.parseInt(parameterMap.get("newsnid"));

		// Fetch old node configuration to stop storage node
		OracleNoSQLNodeConf oldNodeConf = null;

		for (OracleNoSQLNodeConf nodeConf : conf.getNodes()) {
			if (nodeConf.getSnId().intValue() == oldSnId) {
				oldNodeConf = nodeConf;
				break;
			}
		}

		// New storage node from technology data
		OracleNoSQLStorageNode newStorageNode = null;
		OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
				.getTechnologyData(this.conf);
		for (OracleNoSQLDatacenter datacenter : techData.getDatacenterList()) {
			for (OracleNoSQLStorageNode storageNode : datacenter
					.getStorageNodeList()) {
				if (storageNode.getSnId() == newSnId) {
					newStorageNode = storageNode;
					break;
				}
			}
		}

		migrateNodeCommand(oldNodeConf, newStorageNode);
	}

	/**
	 * Validate migrate node parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void validateMigrateNode() throws AnkushException {
		OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
				.getTechnologyData(this.conf);

		validateOperation(techData);

		// Validate replication factor
		if (techData.getReplicationFactor() < MIN_REP_FACTOR) {
			throw new AnkushException(
					"Current replication factor is "
							+ techData.getReplicationFactor()
							+ ". Migrate node is possible only if replication factor is 3 or more.");
		}

		if (!parameterMap.containsKey("oldsnid")
				|| !parameterMap.containsKey("newsnid")) {
			throw new AnkushException("Either oldsnid or newsnid is missing.");
		}

		final int oldSnId = Integer.parseInt(parameterMap.get("oldsnid"));
		final int newSnId = Integer.parseInt(parameterMap.get("newsnid"));

		// Fetch old node configuration to stop storage node
		OracleNoSQLNodeConf oldNodeConf = null;

		for (OracleNoSQLNodeConf nodeConf : conf.getNodes()) {
			if (nodeConf.getSnId().intValue() == oldSnId) {
				oldNodeConf = nodeConf;
				break;
			}
		}

		// New storage node from technology data
		OracleNoSQLStorageNode newStorageNode = null;

		for (OracleNoSQLDatacenter datacenter : techData.getDatacenterList()) {
			for (OracleNoSQLStorageNode storageNode : datacenter
					.getStorageNodeList()) {
				if (storageNode.getSnId() == newSnId) {
					newStorageNode = storageNode;
					break;
				}
			}
		}

		if (oldNodeConf == null || newStorageNode == null) {
			throw new AnkushException(
					"Could not get StorageNode configuration.");
		}

		// Check for admin node
		if (oldNodeConf.isAdmin()) {
			if (newStorageNode.getAdminPort() <= 0) {
				throw new AnkushException(
						"New storage node is not an admin node.");
			}

			// Check for deployed admin nodes
			int adminCount = 0;

			for (OracleNoSQLDatacenter datacenter : techData
					.getDatacenterList()) {
				for (OracleNoSQLStorageNode storageNode : datacenter
						.getStorageNodeList()) {
					if (storageNode.getAdminPort() > 0
							&& storageNode.getRnCount() > 0) {
						adminCount++;
					}
					if (adminCount >= 3) {
						break;
					}
				}
				if (adminCount >= 3) {
					break;
				}
			}

			if (adminCount < 3) {
				throw new AnkushException(
						"Admin node could migrate only if at least three admin nodes are deployed.");
			}
		}

	}

	/**
	 * Migrate storage node operation.
	 * 
	 * @param oldSnId
	 *            Old storage node id
	 * @param newSnId
	 *            New storage node id
	 * @param newRegistryPort
	 *            New registry port
	 */
	private void migrateNodeCommand(OracleNoSQLNodeConf oldSn,
			OracleNoSQLStorageNode newSn) {

		try {
			OracleNoSQLPlan plan = new OracleNoSQLPlan(conf);
			// Remove admin service
			plan.removeAdmin(newSn);

			// Stop old storage node
			new OracleNoSQLDeployer().stopServices(
					Arrays.asList((NodeConf) oldSn), this.conf);
			if (plan.getAdminPublicIp().equalsIgnoreCase(oldSn.getPublicIp())) {
				plan = new OracleNoSQLPlan(conf);
			}

			// Migrate the node
			plan.migrateNode(oldSn, newSn);

			dbCluster.setState(Constant.Cluster.State.DEPLOYED);
			try {
				clusterManager.save(dbCluster);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			// Remove node			
			deleteNodeFromDB(oldSn);			

		} catch (Exception e) {
			logger.error(e.getMessage());
			conf.getClusterConf().setState(Constant.Cluster.State.ERROR);
		} finally {
			postOperation();
		}
	}

	/**
	 * Delete node from oracle cluster.
	 * 
	 * @param oNodeConf
	 *            the o node conf
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void deleteNodeFromDB(OracleNoSQLNodeConf oNodeConf)
			throws AnkushException {
		// If node is used as a ganglia master apply soft delete
		if (oNodeConf.getPublicIp().equals(
				this.conf.getClusterConf().getGangliaMaster().getPublicIp())) {

			removeNode(oNodeConf);

			return;
			// throw new AnkushException(
			// "Could not remove old storage node as it is used as ganglia master.");
		}

		// Remove node
		((OracleNoSQLClusterConf) this.conf.getClusterConf())
				.setNewNodes(Collections.singletonList(oNodeConf));

		Map<String, Object> removeResult = new ClusterManager().removeNodes(
				conf.getClusterDbId(), this.conf.getClusterConf());
		if (removeResult.get(STATUS) != null
				&& !(Boolean) removeResult.get(STATUS)) {
			throw new AnkushException("Could not remove old storage node.");
		}
	}

	/**
	 * @param oNodeConf
	 */
	private void removeNode(OracleNoSQLNodeConf oNodeConf) {

		// Remove Oracle NoSQL
		new OracleNoSQLDeployer().removeNodes(
				Collections.singletonList((NodeConf) oNodeConf), this.conf);

		// Remove monitoring data
		NodeMonitoring nodeMonitoring = new MonitoringManager()
				.getMonitoringData(oNodeConf.getPublicIp());
		Map<String, TechnologyData> techData = nodeMonitoring
				.getTechnologyData();
		techData.remove(Constant.Technology.ORACLE_NOSQL);
		nodeMonitoring
				.setTechnologyData((HashMap<String, TechnologyData>) techData);
		Map<String, Boolean> services = nodeMonitoring.getServiceStatus();
		Map<String, Boolean> newServices = new HashMap<String, Boolean>();
		for (String service : services.keySet()) {
			if (!service.startsWith("sn") && !service.startsWith("rg")) {
				newServices.put(service, services.get(service));
			}
		}
		nodeMonitoring.setServiceStatus((HashMap<String, Boolean>) newServices);
		new MonitoringManager().save(nodeMonitoring);

		// Remove events
		List<String> subTypes = new ArrayList<String>();
		subTypes.add("Storage Node");
		subTypes.add("Rep Node");

		new EventManager().deleteEvents(oNodeConf.getPublicIp(), subTypes);
	}

	/**
	 * Validate cluster state before performing operation.
	 * 
	 * @param techData
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void validateOperation(OracleNoSQLTechnologyData techData)
			throws AnkushException {

		if (techData == null) {
			throw new AnkushException("Could not get technology data.");
		}

		if (!dbCluster.getState().equals(Constant.Cluster.State.DEPLOYED)) {
			throw new AnkushException("Cluster is in " + dbCluster.getState()
					+ " state.");
		}

		if (techData.getAvailableStorageNodeCount() <= 0) {
			throw new AnkushException("Could not find available StorageNode");
		}
	}

	/**
	 * Pre operation activity.
	 * 
	 * @param state
	 *            Cluster state
	 * @param operation
	 *            Operation
	 */
	private void preOperation(String state, String operation) {
		// Change cluster state

		dbCluster.setState(state);
		try {
			dbCluster = clusterManager.save(dbCluster);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// Create operation tile
		TileInfo tileInfo = new TileInfo();
		tileInfo.setLine1(operation);
		tileInfo.setLine2("Is in progress");
		tileInfo.setUrl(TOPOLOGY_OPERATION);
		tileInfo.setStatus(Constant.Tile.Status.NORMAL);
		tileInfo.setData(result);
		Tile tile = new TileManager().saveTile(tileInfo, conf.getClusterDbId());
		conf.setTileId(tile.getId());
	}

	/**
	 * Post operation activity.
	 */
	private void postOperation() {

		// Fetch operation tile
		TileManager tileManager = new TileManager();
		Tile tile = tileManager.getTile(conf.getTileId());
		if (tile != null) {
			TileInfo tileInfo = tile.getTileInfoObj();
			if (conf.getClusterConf().getState()
					.equals(Constant.Cluster.State.ERROR)) {
				// Set Error state
				tileInfo.setLine2("Failed");
				tileInfo.setStatus(Constant.Tile.Status.ERROR);
			} else {
				// Set success state
				tileInfo.setLine2("Completed");
				tileInfo.setStatus(Constant.Tile.Status.NORMAL);
			}

			// Save tile
			tileInfo.setUrl(null);
			tile.setTileInfoObj(tileInfo);
			tile.setDestroy(true);
			tileManager.saveTile(tile);
		}

		// Update cluster state
		try {
			dbCluster = clusterManager.get(conf.getClusterDbId());
			dbCluster.setState(Constant.Cluster.State.DEPLOYED);
			clusterManager.save(dbCluster);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Process cluster service request.
	 * 
	 * @param clusterId
	 *            Cluster Id
	 * @param action
	 *            Cluster action
	 * @param parameterMap
	 *            Paramter Map
	 * @param postData
	 *            Post data
	 * @return the map
	 */
	public Map service(long clusterId, String action, Map parameterMap,
			Object postData) {
		try {
			// Getting cluster object from database.
			dbCluster = clusterManager.get(clusterId);
			if (dbCluster == null) {
				throw new AnkushException("Could not find cluster.");
			}

			this.conf = (OracleNoSQLConf) dbCluster.getClusterConf()
					.getClusterComponents()
					.get(Constant.Technology.ORACLE_NOSQL);
			if (this.conf == null) {
				throw new AnkushException("Not an OracleNoSQL cluster.");
			}

			return service(this.conf, action, parameterMap, postData);

		} catch (AnkushException e) {
			logger.error(e.getMessage());
			addError(e.getMessage());
		} catch (Exception e) {
			// Adding error
			if (e.getMessage() != null) {
				addError(e.getMessage());
			} else {
				addError(e.getCause().getMessage());
			}
			// Logging error
			logger.error(e.getMessage(), e);
		}

		return returnResult();
	}

	/**
	 * Service.
	 * 
	 * @param conf
	 *            the conf
	 * @param action
	 *            the action
	 * @param parameterMap
	 *            the parameter map
	 * @param postData
	 *            the post data
	 * @return the map
	 */
	public Map service(OracleNoSQLConf conf, String action, Map parameterMap,
			Object postData) {
		try {
			this.conf = conf;
			this.postData = postData;
			this.dbCluster = clusterManager.get(conf.getClusterDbId());
			this.parameterMap = getParameterMap(parameterMap);

			// Getting last operation id.
			conf.getClusterConf().setClusterId(conf.getClusterDbId());
			conf.setOperationId(logger.getNewOperationId(dbCluster.getId()));
			logger.setCluster(conf.getClusterConf());

			// Create method object using the action name.
			final Method method = this.getClass().getDeclaredMethod(
					action.toLowerCase());
			method.setAccessible(true);
			final OracleNoSQLService service = this;

			validateParameter(action);

			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					// invoking the method.
					try {
						method.invoke(service);
					} catch (Exception e) {
						// Adding error
						if (e.getMessage() != null) {
							addError(e.getMessage());
						} else {
							addError(e.getCause().getMessage());
						}
						// Logging error
						logger.error(e.getMessage(), e);
					}
				}
			});

		} catch (SecurityException e) {
			// Adding error
			addError(e.getMessage());
			// Logging error
			logger.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			// Adding error
			addError(e.getMessage());
			// Logging error
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			// Adding error
			if (e.getMessage() != null) {
				addError(e.getMessage());
			} else {
				addError(e.getCause().getMessage());
			}
			// Logging error
			logger.error(e.getMessage(), e);
		}

		return returnResult();
	}

	/**
	 * Validate parameter.
	 * 
	 * @param action
	 *            the action
	 * @throws AnkushException
	 */
	private void validateParameter(String action) throws AnkushException {
		if (action.equals("repnodeparams")) {
			if (!parameterMap.containsKey("rgid")
					|| !parameterMap.containsKey("rnid")) {
				throw new AnkushException("Either rnid or rgid is missing.");
			}
		} else if (action.equals("storagenodeparams")
				|| action.equals("interruptplan")
				|| action.equals("cancelplan") || action.equals("waitplan")
				|| action.equals("executeplan")
				|| action.equals("executeandwaitplan")) {
			if (!parameterMap.containsKey(ID)) {
				throw new AnkushException(ID_ERROR_MESSAGE);
			}
		} else if (action.equals("changerepfactor")) {
			OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
					.getTechnologyData(this.conf);

			validateOperation(techData);
			if (!parameterMap.containsKey("repfactor")) {
				throw new AnkushException("repfactor is missing.");
			}
			result.putAll(new OracleNoSQLMonitor().monitor(dbCluster,
					LASTLOGTIMESTAMP, null));
			preOperation(Constant.Cluster.State.CHANGE_REP_FACTOR,
					"Inc. Rep Factor");
		} else if (action.equals("rebalance")) {
			OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
					.getTechnologyData(this.conf);

			validateOperation(techData);
			if (techData.getReplicationFactor() < MIN_REP_FACTOR) {
				throw new AnkushException(
						"Current replication factor is "
								+ techData.getReplicationFactor()
								+ ". Topology rebalance is possible only if replication factor is 3 or more.");
			}

			result.putAll(new OracleNoSQLMonitor().monitor(dbCluster,
					LASTLOGTIMESTAMP, null));

			preOperation(Constant.Cluster.State.REBALANCE, "Rebalance");
		} else if (action.equals("redistribute")) {
			OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
					.getTechnologyData(this.conf);
			validateOperation(techData);

			result.putAll(new OracleNoSQLMonitor().monitor(dbCluster,
					LASTLOGTIMESTAMP, null));
			preOperation(Constant.Cluster.State.REDISTRIBUTE, "Redistribute");
		} else if (action.equals("migratenode")) {
			validateMigrateNode();
			result.putAll(new OracleNoSQLMonitor().monitor(dbCluster,
					LASTLOGTIMESTAMP, null));

			preOperation(Constant.Cluster.State.MIGRATE_NODE, "Migrate");
		}
	}

	/**
	 * Add error message.
	 * 
	 * @param error
	 *            Error message
	 */
	private void addError(String error) {
		this.errors.add(error);
	}

	/**
	 * Return result.
	 * 
	 * @return the map
	 */
	private Map returnResult() {
		if (this.errors.isEmpty()) {
			this.result.put(STATUS, true);
		} else {
			this.result.put(STATUS, false);
			this.result.put("error", this.errors);
		}
		return this.result;
	}

	/**
	 * Set storage node configuration to start/stop storage nodes.
	 * 
	 * @param snIds
	 *            List of storage node ids
	 * @return the list
	 */
	private List<NodeConf> setStorageNodes(List<String> snIds) {
		Map<Integer, OracleNoSQLNodeConf> snMap = new HashMap<Integer, OracleNoSQLNodeConf>();
		List<NodeConf> nodes = new ArrayList<NodeConf>();

		// Generate StorageNode map
		for (OracleNoSQLNodeConf nodeConf : this.conf.getNodes()) {
			snMap.put(nodeConf.getSnId(), nodeConf);
		}

		// Add all StorageNodes
		if (snIds == null) {
			nodes.addAll(snMap.values());
		} else {
			// Add selected StorageNodes
			for (String snId : snIds) {
				OracleNoSQLNodeConf nodeConf = snMap
						.get(Integer.parseInt(snId));
				if (nodeConf != null) {
					nodes.add(nodeConf);
				}
			}
		}
		return nodes;
	}

	/**
	 * Method to fetch parameters from HTTPRequestParameterMap.
	 * 
	 * @param parameterMap
	 *            Parameter map
	 * @param name
	 *            Parameter key
	 * @return the parameter
	 */
	private String getParameter(Map parameterMap, final String name) {
		String[] strings = (String[]) parameterMap.get(name);
		if (strings != null) {
			return strings[0];
		}
		return null;
	}

	/**
	 * Retrieve Key:Value map.
	 * 
	 * @param parameterMap
	 *            Parameter map
	 * @return the parameter map
	 */
	private Map<String, String> getParameterMap(Map parameterMap) {
		if (parameterMap == null) {
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		Set<String> keys = new HashSet<String>(parameterMap.keySet());
		for (String key : keys) {
			params.put(key, getParameter(parameterMap, key));
		}
		return params;
	}

	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {
		this.conf = (OracleNoSQLConf) conf.getClusterComponents().get(
				Constant.Technology.ORACLE_NOSQL);
		if (processName.startsWith("sn")) {
			// Fetch storage node id
			List<String> snList = new ArrayList<String>();
			snList.add(processName.substring(2));
			this.postData = snList;
			if (action.equalsIgnoreCase("start")) {
				try {
					startstoragenode();
					return true;
				} catch (AnkushException e) {
					logger.error("Unable to start service.", e);
					return false;
				}
			} else if (action.equalsIgnoreCase("stop")) {
				try {
					stopstoragenode();
					return true;
				} catch (AnkushException e) {
					logger.error("Unable to stop service.", e);
					return false;
				}
			} else {
				return false;
			}
		} else if (processName.startsWith("rg")) {
			// Fetch replication node id.
			List<List<String>> rnList = new ArrayList<List<String>>();
			List<String> rn = new ArrayList<String>();
			String[] token = processName.split("-");
			if (token.length != 2) {
				return false;
			}
			rn.add(token[0].substring(2));
			rn.add(token[1].substring(2));

			rnList.add(rn);
			this.postData = rnList;
			if (action.equalsIgnoreCase("start")) {
				try {
					startrepnode();
					return true;
				} catch (AnkushException e) {
					logger.error("Unable to start service.", e);
					return false;
				}
			} else if (action.equalsIgnoreCase("stop")) {
				try {
					stoprepnode();
					return true;
				} catch (AnkushException e) {
					logger.error("Unable to stop service.", e);
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}
}
