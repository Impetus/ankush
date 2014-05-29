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
package com.impetus.ankush.cassandra;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.GenericServiceMonitor;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.ganglia.Graph;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.JmxUtil;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHConnection;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.Parameter;

/**
 * The Class CassandraClusterMonitor.
 */
public class CassandraClusterMonitor extends AbstractMonitor {

	/** The logger. */
	private final AnkushLogger logger = new AnkushLogger(
			CassandraClusterMonitor.class);

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The monitoring manager. */
	private GenericManager<NodeMonitoring, Long> monitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	private ConfigurationManager configManager = new ConfigurationManager();

	/** The list CassandraNodes. */
	private List<NodeConf> cassandraNodes;

	/** The CassandraClusterConf. */
	// private CassandraClusterConf clusterConf;

	private CassandraConf cassandraConf;

	/** The Constant CASSANDRA_FOLDER_BIN. */
	private static final String CASSANDRA_FOLDER_BIN = "/bin/";

	/** The Constant CASSANDRA_FOLDER_CONF. */
	private static final String CASSANDRA_FOLDER_CONF = "conf/";

	/** The Constant EXTENSION_YAML. */
	private static final String EXTENSION_YAML = ".yaml";

	/** The Constant EXTENSION_PROPERTIES. */
	private static final String EXTENSION_PROPERTIES = ".properties";

	/** The Constant ORG_APACHE_CASSANDRA. */
	private static final String ORG_APACHE_CASSANDRA = "org.apache.cassandra.";

	/**
	 * Details.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param parameterMap
	 *            the parameter map
	 * @return the map
	 */
	private void details() {
		try {
			// cluster conf.
			ClusterConf clusterConf = dbCluster.getClusterConf();

			// setting errors.
			Map<String, String> errors = clusterConf.getErrors();
			for (NodeConf nodeConf : clusterConf.getNodeConfs()) {
				if (nodeConf.getErrors().size() > 0) {
					errors.put(
							nodeConf.getPublicIp(),
							"Deployment failed on node : "
									+ nodeConf.getPublicIp());
				}
			}
			clusterConf.setErrors(errors);
			// putting cluster setup details in map.
			result.putAll(JsonMapperUtil.mapFromObject(clusterConf));

			// set nodes
			setNodes(clusterConf.getNodeConfs());
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Method to get the node list.
	 * 
	 */
	private void nodelist() {
		setNodesLogMsg();
	}

	/**
	 * Sets the nodes log msg.
	 * 
	 */
	private void setNodesLogMsg() {
		try {
			long operationId = logger.getNewOperationId(dbCluster.getId()) - 1;
			List<Log> logs = logManager.getAllByNativeQuery(Log
					.getNodeLastLogQuery(dbCluster.getId(), operationId));

			Map<String, String> hostMsgMap = new HashMap<String, String>();
			for (Log log : logs) {
				hostMsgMap.put(log.getHost(), log.getMessage());
			}

			List<Map> nodesMap = new ArrayList<Map>();
			CassandraClusterConf conf = (CassandraClusterConf) dbCluster
					.getClusterConf();
			for (NodeConf node : conf.getNodeConfs()) {
				CassandraNodeConf cassandraNode = (CassandraNodeConf) node;
				node.setMessage(hostMsgMap.get(node.getPublicIp()));

				if (cassandraNode.isSeedNode()) {
					node.setType(Constant.Role.CASSANDRA_SEED);
				} else {
					node.setType(Constant.Role.CASSANDRA_NON_SEED);
				}

				LinkedHashMap nodeMap = JsonMapperUtil.objectFromObject(node,
						LinkedHashMap.class);
				parameterMap.put(Constant.Keys.IP, node.getPublicIp());
				nodeMap.remove(Constant.Keys.NODESTATE);
				nodeMap.remove(Constant.Keys.TOKENVALUE);
				nodeMap.remove(Constant.Keys.DATACENTERNAME);
				nodeMap.remove("sysHostName");
				nodeMap.remove(Constant.Keys.NODESTATUS);
				nodeMap.remove(Constant.Keys.RACKNAME);
				nodeMap.remove(Constant.Keys.CLUSTEROWNERSHIP);
				nodeMap.remove(Constant.Keys.NODELOAD);
				nodeMap.remove(Constant.Keys.TOKENS);
				nodeMap.remove(Constant.Keys.V_NODE_COUNT);
				nodeMap.remove(Constant.Keys.RACKINFO);
				nodesMap.add(nodeMap);
			}
			result.putAll(Collections.singletonMap(Constant.Keys.NODES,
					nodesMap));
			result.put(Constant.Keys.STATE, dbCluster.getState());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
	}

	private void nodes() {
		try {

			ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();
			CassandraConf cassandraConf = (CassandraConf) conf
					.getClusterComponents().get(
							Constant.Component.Name.CASSANDRA);
			Set<String> nodesList = new HashSet<String>();

			for (NodeConf nc : cassandraConf.getNodes()) {
				nodesList.add(nc.getPublicIp());
			}

			Map<String, Object> propMap = new HashMap<String, Object>();
			propMap.put("clusterId", dbCluster.getId());
			List<Node> nodes = nodeManager.getAllByPropertyValue(propMap);

			List<Map> nodesMap = new ArrayList<Map>();
			// iterating over the nodes
			for (Node dbNode : nodes) {

				if (nodesList.contains(dbNode.getPublicIp())) {
					NodeConf node = dbNode.getNodeConf();
					if (node.getNodeState().equals(Constant.Node.State.ADDING)) {
						continue;
					}
					LinkedHashMap nodeMap = JsonMapperUtil.objectFromObject(
							node, LinkedHashMap.class);
					// creating monitoring manager object
					MonitoringManager manager = new MonitoringManager();
					// getting node monitoring data
					NodeMonitoring nodeMonitoring = manager
							.getMonitoringData(node.getId());

					// putting services status in map.
					boolean serviceStatus = true;
					if (nodeMonitoring != null
							&& nodeMonitoring.getServiceStatus() != null) {
						for (Boolean status : nodeMonitoring.getServiceStatus()
								.values()) {
							serviceStatus = serviceStatus && status;
						}
						nodeMap.put(Constant.Keys.SERVICESTATUS, serviceStatus);
					} else {
						nodeMap.put(Constant.Keys.SERVICESTATUS, false);
					}

					nodeMap.putAll(getNodeUsageMap(nodeMonitoring));
					nodeMap.put(Constant.Keys.ID, node.getId());
					nodeMap.remove(Constant.Keys.MESSAGE);
					nodeMap.remove(Constant.Keys.ERRORS);
					nodesMap.add(nodeMap);
				}

			}
			result.put(Constant.Keys.TILES, getNodesTile());
			result.putAll(Collections.singletonMap(Constant.Keys.NODES,
					nodesMap));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
	}

	/**
	 * Method to get nodes tile.
	 * 
	 * @param conf
	 *            the conf
	 * @return the nodes tile
	 */
	private List<TileInfo> getNodesTile() {

		ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();
		CassandraConf cassandraConf = (CassandraConf) conf
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		Integer seedNodes = cassandraConf.getSeedNodes().size();
		Integer totalNodes = conf.getNodeCount();
		String nodeString = seedNodes.toString() + "/" + totalNodes.toString();

		// create seedNode tile
		TileInfo nodeTile = new TileInfo();
		nodeTile.setLine1(nodeString);
		nodeTile.setLine2(Constant.Keys.SEEDNODES);
		nodeTile.setUrl(Constant.Tile.Url.NODE_LIST);
		nodeTile.setStatus(Constant.Tile.Status.NORMAL);

		// // list object.
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		tiles.add(nodeTile);

		return tiles;
	}

	/**
	 * Cluster tiles.
	 */
	private void clustertiles() {
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// createing tile manager object.
		TileManager tileManager = new TileManager();

		// add node type tiles.
		tiles.addAll(getNodesTile());

		// adding event tiles.
		tiles.addAll(tileManager.getGroupedClusterTiles(dbCluster));

		// add add nodes tile.
		TileInfo addNodeTile = getAddNodeTile();
		if (addNodeTile != null) {
			tiles.add(addNodeTile);
		}

		// add remove nodes tile.
		TileInfo removeNodeTile = getRemoveNodeTile();
		if (removeNodeTile != null) {
			tiles.add(removeNodeTile);
		}

		CassandraJMXData techData = CassandraJMXData
				.getTechnologyData(cassandraConf);
		if (techData != null) {
			tiles.addAll(techData.getTiles());
		}

		// putting the tiles.
		result.put(Constant.Keys.TILES, tiles);
	}

	private Set<String> getActiveNodes() {
		Set<String> activeNodes = new HashSet<String>();
		for (Node dbNode : dbCluster.getNodes()) {
			NodeMonitoring mNode = new MonitoringManager()
					.getMonitoringData(dbNode.getId());
			if (mNode != null) {
				Map<String, Boolean> serviceMap = mNode.getServiceStatus();
				if (serviceMap
						.containsKey(Constant.Component.ProcessName.CASSANDRA)
						&& serviceMap
								.get(Constant.Component.ProcessName.CASSANDRA)) {
					activeNodes.add(dbNode.getPublicIp());
				}
			}
		}
		return activeNodes;
	}

	private List<TileInfo> getNodeDetailTile(String nodeIp) {
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		JmxUtil jmxUtil = null;
		try {

			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(nodeIp);

			if (nodeMonitoring != null
					&& nodeMonitoring.getServiceStatus().get(
							Constant.Component.ProcessName.CASSANDRA) != null
					&& nodeMonitoring.getServiceStatus().get(
							Constant.Component.ProcessName.CASSANDRA) == true) {
				jmxUtil = getJMXConnection(nodeIp, Constant.Cassandra.JMX_PORT);
				if (jmxUtil != null) {
					ObjectName mObjNameStorageService = new ObjectName(
							ORG_APACHE_CASSANDRA
									+ "db:type="
									+ Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);
					// Getting ownership
					Object attrOwnership = jmxUtil
							.getAttribute(
									mObjNameStorageService,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP);
					LinkedHashMap ownership = (LinkedHashMap) attrOwnership;

					for (Object key : ownership.keySet()) {
						if (key.toString().trim().substring(1)
								.equalsIgnoreCase(nodeIp)) {
							// create ownership tile
							TileInfo ownershipTile = new TileInfo();
							ownershipTile
									.setLine1(String.valueOf(((Float) ownership
											.get(key)) * 100) + " %");
							ownershipTile.setLine2(Constant.Keys.OWNERSHIP);
							ownershipTile
									.setStatus(Constant.Tile.Status.NORMAL);
							tiles.add(ownershipTile);
							break;
						}
					}
					// Getting load
					Object attrLoadMap = jmxUtil
							.getAttribute(
									mObjNameStorageService,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LOADMAP);
					Map<String, String> load = (HashMap<String, String>) attrLoadMap;
					String value = load.get(nodeIp);
					if (value != null) {
						// create load tile
						TileInfo loadTile = new TileInfo();
						loadTile.setLine1(value);
						loadTile.setLine2(Constant.Keys.LOAD);
						loadTile.setStatus(Constant.Tile.Status.NORMAL);
						tiles.add(loadTile);
					}

					// Getting tokens
					Object opParams[] = { nodeIp };
					String opSig[] = { String.class.getName() };
					Object resultSet = jmxUtil
							.getResultObjectFromOperation(
									mObjNameStorageService,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOKENS,
									opParams, opSig);
					List<String> tokens = (List<String>) resultSet;
					// create tokenCount tile
					TileInfo tokenCountTile = new TileInfo();
					tokenCountTile.setLine1(String.valueOf(tokens.size()));
					tokenCountTile.setLine2(Constant.Keys.TOKENS);
					tokenCountTile.setStatus(Constant.Tile.Status.NORMAL);
					tiles.add(tokenCountTile);
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (jmxUtil != null) {
				jmxUtil.disconnect();
			}
		}
		return tiles;
	}

	/**
	 * Method to get the activity progress logs.
	 * 
	 * @return Map of all progress logs.
	 */
	private void addingnodes() {
		CassandraClusterConf conf = (CassandraClusterConf) dbCluster
				.getClusterConf();
		List<NodeConf> nodes = conf.getCassandra().getNewNodes();

		if (nodes == null || nodes.isEmpty()) {
			return;
		}

		setNodes(nodes);

		// getting the event state.
		String state = conf.getState();
		for (NodeConf node : conf.getCassandra().getNewNodes()) {
			if (node.getNodeState().equals(Constant.Keys.ERROR)) {
				state = node.getNodeState();
				break;
			}
		}

		// putting event state in map.
		result.put(Constant.Keys.STATE, state);

		if (!state.equals(Constant.Cluster.State.ADDING_NODES)) {
			conf.getCassandra().setNewNodes(null);
			dbCluster.setClusterConf(conf);
			clusterManager.save(dbCluster);
		}
	}

	/**
	 * Sets the nodes log msg.
	 * 
	 */
	private void setNodes(List<NodeConf> nodes) {
		try {
			long operationId = logger.getNewOperationId(dbCluster.getId()) - 1;
			List<Log> logs = logManager.getAllByNativeQuery(Log
					.getNodeLastLogQuery(dbCluster.getId(), operationId));

			Map<String, String> hostMsgMap = new HashMap<String, String>();
			for (Log log : logs) {
				hostMsgMap.put(log.getHost(), log.getMessage());
			}

			List<Map> nodesMap = new ArrayList<Map>();
			// iterating over the nodes for node type calculation.
			for (NodeConf node : nodes) {
				if (dbCluster.getState()
						.equals(Constant.Cluster.State.DEPLOYED)
						&& node.getStatus()) {
					node.setMessage("Deployment Completed.");
				} else {
					node.setMessage(hostMsgMap.get(node.getPublicIp()));
				}
				LinkedHashMap nodeMap = JsonMapperUtil.objectFromObject(node,
						LinkedHashMap.class);
				nodeMap.remove(Constant.Keys.NODESTATE);
				nodeMap.remove(Constant.Keys.TOKENVALUE);
				nodeMap.remove(Constant.Keys.DATACENTERNAME);
				nodeMap.remove("sysHostName");
				nodeMap.remove(Constant.Keys.NODESTATUS);
				nodeMap.remove(Constant.Keys.RACKNAME);
				nodeMap.remove(Constant.Keys.CLUSTEROWNERSHIP);
				nodeMap.remove(Constant.Keys.NODELOAD);
				nodeMap.remove(Constant.Keys.TOKENS);
				nodeMap.remove(Constant.Keys.RACKINFO);
				nodesMap.add(nodeMap);
			}
			result.putAll(Collections.singletonMap(Constant.Keys.NODES,
					nodesMap));
			result.put(Constant.Keys.STATE, dbCluster.getState());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
	}

	/**
	 * Sets the add node tile.
	 * 
	 * @param conf
	 *            the conf
	 * @param clusterTiles
	 *            the cluster tiles
	 */
	private TileInfo getAddNodeTile() {
		ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();
		CassandraConf cassandraConf = (CassandraConf) conf
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		try {
			if (conf != null && cassandraConf.getNewNodes() != null
					&& !cassandraConf.getNewNodes().isEmpty()) {
				List<NodeConf> nodes = cassandraConf.getNewNodes();
				String line1 = "Node Addition";
				String line2 = "is in progress";
				String line3 = null;
				String status = Constant.Tile.Status.NORMAL;
				String state = "progress";
				String errorLine = nodes.size() + " Nodes";
				for (NodeConf node : nodes) {
					String nodeState = node.getNodeState();
					if (nodeState != null) {
						if (nodeState.equals(Constant.Node.State.ERROR)) {
							line2 = "Failed";
							line3 = errorLine;
							status = Constant.Tile.Status.CRITICAL;
							state = "error";
							break;
						} else if (nodeState
								.equals(Constant.Node.State.DEPLOYED)) {
							line2 = "Completed";
							state = "success";
						}
					}
				}
				return (new TileInfo(line1, line2, line3, null, status,
						Constant.Cluster.State.ADDING_NODES + "|" + state));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Sets the remove node tile.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param clusterTiles
	 *            the cluster tiles
	 */
	private TileInfo getRemoveNodeTile() {
		ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();
		CassandraConf cassandraConf = (CassandraConf) conf
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("clusterId", dbCluster.getId());
			map.put("state", Constant.Node.State.REMOVING);

			int count = nodeManager.getAllByPropertyValueCount(map);
			if (count > 0) {
				String line1 = "Node Removal";
				String line2 = "is in progress";
				String line3 = null;
				String status = Constant.Tile.Status.NORMAL;
				return (new TileInfo(line1, line2, line3, null, status,
						Constant.Tile.Url.NODE_LIST));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * node tiles.
	 */
	public void nodetiles() {
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// createing tile manager object.
		TileManager tileManager = new TileManager();

		// get node ip
		String ip = (String) parameterMap.get(Constant.Keys.IP);
		if (ip == null || ip.isEmpty()) {
			this.errors.add("IP is missing.");
			return;
		}

		// Get the db node info using public IP
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, ip);

		// getting nodeconf from the node.
		NodeConf nodeConf = node.getNodeConf();
		// get the node type tiles.
		tiles.add(getNodeTypeTile(nodeConf));

		// add the event tiles for the node.
		tiles.addAll(tileManager.getNodeEventTiles(dbCluster.getId(), ip));

		// add the event tiles for the node.
		tiles.addAll(getNodeDetailTile(ip));

		// putting the tiles.
		result.put(Constant.Keys.TILES, tiles);
	}

	/**
	 * Method to get the map of role, ip list.
	 * 
	 * @return The Map of role and ip list.
	 */
	private Map techlogs() {
		ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();

		CassandraConf cassandraConf = (CassandraConf) conf
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		// seedNode ip list.
		List<String> seedNodes = new ArrayList<String>();

		// nonSeedNode ip list.
		List<String> nonSeedNodes = new ArrayList<String>();

		// filling seed nodes.
		for (NodeConf nodeConf : cassandraConf.getSeedNodes()) {
			seedNodes.add(nodeConf.getPublicIp());
		}

		// filling nonSeed nodes.
		for (NodeConf nodeConf : cassandraConf.getNonSeedNodes()) {
			nonSeedNodes.add(nodeConf.getPublicIp());
		}

		if (seedNodes.size() > 0) {
			result.put(Constant.Role.CASSANDRA_SEED, seedNodes);
		}
		if (nonSeedNodes.size() > 0) {
			result.put(Constant.Role.CASSANDRA_NON_SEED, nonSeedNodes);
		}
		return returnResult();
	}

	/**
	 * Gets the node role tiles.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param nodeTiles
	 *            the node tiles
	 * @return the node role tiles
	 */
	private TileInfo getNodeTypeTile(NodeConf node) {

		try {
			CassandraNodeConf cassandraNode = (CassandraNodeConf) node;
			String role = Constant.Role.CASSANDRA_SEED;
			if (!cassandraNode.isSeedNode()) {
				role = Constant.Role.CASSANDRA_NON_SEED;
			}
			TileInfo tileInfo = new TileInfo(role, "Type", null, null,
					Constant.Tile.Status.NORMAL, null);
			return tileInfo;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to get download url of the file.
	 * 
	 * @return Map of the download url.
	 */
	private Map download() {
		ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();

		CassandraConf cassandraConf = (CassandraConf) conf
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		// Getting the ip address from parameter map.
		String ip = (String) parameterMap.get(Constant.Keys.IP);
		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);

		// Create the log View Handler object.
		LogViewHandler logHandler = new LogViewHandler(ip, conf.getUsername(),
				conf.getPassword(), conf.getPrivateKey());

		String filePath = cassandraConf.getLogDir() + fileName;

		// setting download path.
		String downloadPath;
		try {
			downloadPath = logHandler.downloadFile(conf.getClusterName(),
					filePath);
			result.put(Constant.Keys.DOWNLOADPATH, downloadPath);
		} catch (Exception e) {
			// Adding error
			addError(e.getMessage());
			// Logging error
			logger.error(e.getMessage(), e);
		}

		return returnResult();
	}

	/**
	 * Method to view the content of the file.
	 * 
	 * @return The content of file.
	 */
	private Map view() {
		// Getting cassandra cluster conf.
		ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();

		CassandraConf cassandraConf = (CassandraConf) conf
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		// Getting the ip address from parameter map.
		String ip = (String) parameterMap.get(Constant.Keys.IP);
		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);
		// Getting the readCount value
		int readCount = ParserUtil.getIntValue(
				(String) parameterMap.get(Constant.Keys.READCOUNT), 0);

		int bytesCount = ParserUtil.getIntValue(
				(String) parameterMap.get(Constant.Keys.BYTESCOUNT), 0);

		// Create log view handler object.
		LogViewHandler logHandler = new LogViewHandler(ip, conf.getUsername(),
				conf.getPassword(), conf.getPrivateKey());

		String filePath = cassandraConf.getLogDir() + fileName;

		// putting the file content with total read characters.
		Map<String, String> content;
		try {
			content = logHandler
					.getFileContent(filePath, readCount, bytesCount);
			result.putAll(content);
		} catch (Exception e) {
			// Adding error
			addError(e.getMessage());
			// Logging error
			logger.error(e.getMessage(), e);
		}

		return returnResult();
	}

	/**
	 * Method to get the node type files.
	 * 
	 * @return The Map of files and directories against the ip address.
	 * @throws Exception
	 */
	private void files() throws Exception {
		ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();

		CassandraConf cassandraConf = (CassandraConf) conf
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

		LogViewHandler logHandler = new LogViewHandler(nodeIp,
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey());

		// Making the log directory.
		String logDirectory = cassandraConf.getLogDir();

		// Name Node ip list.
		List<String> files = logHandler.listLogDirectory(logDirectory);

		// puting the files in list.
		result.put(Constant.Keys.FILES, files);
	}

	private boolean getStatus(String nodeIp) {
		boolean agentStatus = true;
		try {
			// Get the db node monitoring info
			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(nodeIp);
			if (nodeMonitoring != null) {
				// Fetching HadoopNodeConf for node roles
				Map<String, Boolean> serviceStatusMap = nodeMonitoring
						.getServiceStatus();
				if (!serviceStatusMap.get(Constant.Component.Name.AGENT)) {
					agentStatus = false;
					addError("Unable to fetch data as Ankush Agent is down on "
							+ nodeIp + ".");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return agentStatus;
	}

	/**
	 * Gets the agent status.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return the agent status
	 */
	private boolean getAgentStatus(Cluster cluster, String nodeIp) {
		boolean agentStatus = true;
		if (cluster.getTechnology().equals(Constant.Technology.CASSANDRA)) {
			try {

				if (nodeIp != null) {
					return getStatus(nodeIp);
				}
				ClusterConf clusterConf = (ClusterConf) cluster
						.getClusterConf();
				String gangliaMaster = clusterConf.getGangliaMaster()
						.getPublicIp();
				agentStatus = getStatus(gangliaMaster);

				Long nodeId;
				NodeMonitoring nodeMonitoring = null;
				if (agentStatus) {
					Set<Node> nodes = cluster.getNodes();
					for (Node clusterNode : nodes) {
						nodeId = clusterNode.getId();

						// Get the db node monitoring info
						nodeMonitoring = monitoringManager
								.getByPropertyValueGuarded(
										Constant.Keys.NODEID, nodeId);

						Map<String, Boolean> serviceStatusMap = nodeMonitoring
								.getServiceStatus();

						if (!serviceStatusMap
								.get(Constant.Component.Name.AGENT)) {
							addError(Constant.Agent.AGENT_DOWN_MESSAGE);
							break;
						}
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return agentStatus;
	}

	private void params() throws Exception {
		try {
			Map resultInfo = new HashMap();
			Map params = new HashMap();
			if (getAgentStatus(dbCluster, null)) {

				/** Getting cassandra cluster config. **/
				ClusterConf conf = (ClusterConf) dbCluster.getClusterConf();

				/** Cassandra component home path. **/
				CassandraConf cassandraConf = (CassandraConf) conf
						.getClusterComponents().get(
								Constant.Component.Name.CASSANDRA);

				List<NodeConf> nc = new ArrayList<NodeConf>();
				nc.addAll(cassandraConf.getSeedNodes());
				String hostname = conf.getGangliaMaster().getPublicIp();

				String homePath = FileNameUtils
						.convertToValidPath(cassandraConf.getComponentHome());

				/** yaml file path **/
				String cassandraYamlFile = homePath
						+ CASSANDRA_FOLDER_CONF
						+ Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_YAML;
				/** log4j file path **/
				String cassandraLogServerFile = homePath
						+ CASSANDRA_FOLDER_CONF
						+ Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_LOG4JSERVER;
				/** topology file path **/
				String cassandraTopologyFile = homePath
						+ CASSANDRA_FOLDER_CONF
						+ Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_TOPOLOGY;

				List<String> confFiles = new ArrayList<String>();
				confFiles.add(cassandraYamlFile);
				confFiles.add(cassandraLogServerFile);
				if (cassandraConf.getSnitch().equalsIgnoreCase(
						"PropertyFileSnitch")) {
					confFiles.add(cassandraTopologyFile);
				}

				for (String file : confFiles) {

					/** yaml content **/
					String fileContent = SSHUtils.getFileContents(file,
							hostname, conf.getUsername(), conf
									.isAuthTypePassword() ? conf.getPassword()
									: conf.getPrivateKey(), conf
									.isAuthTypePassword());

					Map map = null;
					Map newMap = null;
					if (file.endsWith(EXTENSION_YAML)) {

						/** create yaml object. **/
						Yaml yaml = new Yaml();
						/** create map object by loading from yaml object **/
						map = (Map) yaml.load(fileContent);

						removeKeyFromMap("initial_token", map);
						removeKeyFromMap("listen_address", map);
						removeKeyFromMap("rpc_address", map);
						removeKeyFromMap("num_tokens", map);
						removeKeyFromMap("seed_provider", map);
						removeKeyFromMap("server_encryption_options", map);
						removeKeyFromMap("client_encryption_options", map);
						newMap = prepareMapForYamlProperties(map);

					} else if (file.endsWith(EXTENSION_PROPERTIES)) {
						Properties properties = new Properties();
						// Converting string into Properties.
						properties.load(new StringReader(fileContent));
						map = new HashMap<String, String>();
						// Converting Properties into Map.
						for (final String name : properties
								.stringPropertyNames()) {
							List<Object> newParameterValue = new ArrayList<Object>();
							newParameterValue.add(properties.getProperty(name));
							newParameterValue.add(true);
							map.put(name, newParameterValue);
						}
						newMap = map;
					}
					List<String> fileList = new ArrayList<String>(
							Arrays.asList(file.split("/")));
					resultInfo.put(fileList.get(fileList.size() - 1), map);
					resultInfo.put(fileList.get(fileList.size() - 1), newMap);
				}
				params.put("params", resultInfo);
			}
			result.putAll(params);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	private void removeKeyFromMap(String key, Map map) {
		if (map.containsKey(key)) {
			map.remove(key);
		}
	}

	/**
	 * To get the list of configuration parameters.
	 * 
	 * @throws Exception
	 */
	private void nodeparams() throws Exception {

		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

		if (nodeIp == null || nodeIp.isEmpty()) {
			this.errors.add("Node IP is missing.");
			return;
		}

		Map resultInfo = new HashMap();
		Map params = new HashMap();
		if (getAgentStatus(dbCluster, nodeIp)) {
			/** Getting cassandra cluster config. **/
			CassandraClusterConf conf = (CassandraClusterConf) dbCluster
					.getClusterConf();
			/** Cassandra component home path. **/
			String homePath = FileNameUtils.convertToValidPath(conf
					.getCassandra().getComponentHome());

			/** yaml file path **/
			String cassandraYamlFile = homePath
					+ CASSANDRA_FOLDER_CONF
					+ Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_YAML;
			/** log4j file path **/
			String cassandraLogServerFile = homePath
					+ CASSANDRA_FOLDER_CONF
					+ Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_LOG4JSERVER;
			/** topology file path **/
			String cassandraTopologyFile = homePath
					+ CASSANDRA_FOLDER_CONF
					+ Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_TOPOLOGY;

			List<String> confFiles = new ArrayList<String>();
			confFiles.add(cassandraYamlFile);
			confFiles.add(cassandraLogServerFile);
			if (conf.isRackEnabled()) {
				confFiles.add(cassandraTopologyFile);
			}
			for (String file : confFiles) {
				/** yaml content **/
				String fileContent = SSHUtils.getFileContents(
						file,
						nodeIp,
						conf.getUsername(),
						conf.isAuthTypePassword() ? conf.getPassword() : conf
								.getPrivateKey(), conf.isAuthTypePassword());
				boolean edit = true;
				Map map = null;
				Map newMap = null;
				if (file.endsWith(EXTENSION_YAML)) {
					/** create yaml object. **/
					Yaml yaml = new Yaml();
					/** create map object by loading from yaml object **/
					map = (Map) yaml.load(fileContent);
					removeKeyFromMap("seed_provider", map);
					removeKeyFromMap("server_encryption_options", map);
					removeKeyFromMap("client_encryption_options", map);
					newMap = prepareMapForYamlProperties(map);
				} else if (file.endsWith(EXTENSION_PROPERTIES)) {
					if (file.contains(Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_TOPOLOGY)) {
						edit = false;
					}

					Properties properties = new Properties();
					// Converting string into Properties.
					properties.load(new StringReader(fileContent));
					map = new HashMap<String, String>();
					// Converting Properties into Map.
					for (final String name : properties.stringPropertyNames()) {
						List<Object> newParameterValue = new ArrayList<Object>();
						newParameterValue.add(properties.getProperty(name));
						newParameterValue.add(edit);
						// map.put(name, properties.getProperty(name));
						map.put(name, newParameterValue);
					}
					newMap = map;
				}
				List<String> fileList = new ArrayList<String>(
						Arrays.asList(file.split("/")));
				resultInfo.put(fileList.get(fileList.size() - 1), newMap);
			}
			params.put("params", resultInfo);
		}
		result.putAll(params);
	}

	private Map prepareMapForYamlProperties(Map<String, Object> map) {
		Map<String, Object> editMap = new HashMap<String, Object>();
		try {
			List<String> nonEditableParameters = new ArrayList<String>();
			nonEditableParameters = getNonEditableParameters();
			boolean edit;
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				edit = true;
				List<Object> value = new ArrayList<Object>();
				value.add(entry.getValue());
				if (nonEditableParameters.contains(entry.getKey())) {
					edit = false;
				}
				value.add(edit);
				editMap.put(entry.getKey(), value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return editMap;
	}

	private List<String> getNonEditableParameters() {
		List<String> nonEditableParameters = new ArrayList<String>();
		nonEditableParameters.add("cluster_name");
		nonEditableParameters.add("partitioner");
		nonEditableParameters.add("data_file_directories");
		nonEditableParameters.add("commitlog_directory");
		nonEditableParameters.add("saved_caches_directory");
		nonEditableParameters.add("flush_largest_memtables_at");
		nonEditableParameters.add("reduce_cache_sizes_at");
		nonEditableParameters.add("reduce_cache_capacity_to");
		nonEditableParameters.add("dynamic_snitch_badness_threshold");
		nonEditableParameters.add("endpoint_snitch");
		nonEditableParameters.add("commitlog_sync");
		return nonEditableParameters;
	}

	private void editparams() throws Exception {
		/** Getting cassandra cluster config. **/
		CassandraClusterConf conf = (CassandraClusterConf) dbCluster
				.getClusterConf();
		Map<String, List<Map>> params = (Map<String, List<Map>>) parameterMap
				.get("params");
		String loggedUser = (String) parameterMap.get("loggedUser");
		String nodeIp = null;
		boolean editNodeParams = false;
		if (parameterMap.containsKey("ip")) {
			nodeIp = (String) parameterMap.get("ip");
			editNodeParams = true;
		}
		this.cassandraNodes = conf.getNodeConfs();

		for (Map.Entry<String, List<Map>> entry : params.entrySet()) {
			String fileName = entry.getKey();
			List<Map> confParams = entry.getValue();
			List<String> filetypeList = new ArrayList<String>(
					Arrays.asList(fileName.split("\\.")));
			String fileType = filetypeList.get(filetypeList.size() - 1);
			for (Map confParam : confParams) {
				try {
					Parameter parameter = JsonMapperUtil.objectFromMap(
							confParam, Parameter.class);

					String status = parameter.getStatus();
					// If no status Set for parameter
					if (status.equals(Constant.Parameter_Status.NONE)) {
						continue;
					}
					if (status.equals(Constant.Parameter_Status.ADD)) {
						addConfigFileParam(parameter, fileName, loggedUser,
								fileType, editNodeParams, nodeIp);
					}
					if (status.equals(Constant.Parameter_Status.EDIT)) {
						editConfigFileParam(parameter, fileName, loggedUser,
								fileType, editNodeParams, nodeIp);
					}
					if (status.equals(Constant.Parameter_Status.DELETE)) {
						deleteConfigFileParam(parameter, fileName, fileType,
								editNodeParams, nodeIp);
					}

				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	private void editConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser,
			final String fileType, boolean editNodeParam, String nodeIp) {

		try {
			final String propertyName = parameter.getName();
			final String newValue = parameter.getValue();
			String componentHomePath = FileNameUtils
					.convertToValidPath(cassandraConf.getComponentHome());
			final String propertyFilePath = componentHomePath
					+ CASSANDRA_FOLDER_CONF + fileName;

			final String username = cassandraConf.getClusterConf()
					.getUsername();
			final String password = cassandraConf.getClusterConf()
					.getPassword();
			final String privateKey = cassandraConf.getClusterConf()
					.getPrivateKey();
			if (editNodeParam) {
				String hostName = nodeIp;
				editParameter(hostName, username, password, privateKey,
						propertyName, newValue, propertyFilePath, fileType,
						loggedUser, fileName);
			} else {
				List<NodeConf> cassandraNodes = this.cassandraNodes;
				final Semaphore semaphore = new Semaphore(cassandraNodes.size());
				try {
					for (final NodeConf node : cassandraNodes) {
						semaphore.acquire();
						AppStoreWrapper.getExecutor().execute(new Runnable() {
							@Override
							public void run() {
								String hostName = node.getPublicIp();
								editParameter(hostName, username, password,
										privateKey, propertyName, newValue,
										propertyFilePath, fileType, loggedUser,
										fileName);
								if (semaphore != null) {
									semaphore.release();
								}
							}
						});
					}
					semaphore.acquire(cassandraNodes.size());
				} catch (Exception e) {
					logger.error("Error in updating config file params..."
							+ e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}
	}

	private void editParameter(String hostName, String username,
			String password, String privateKey, String propertyName,
			String newValue, String propertyFilePath, String fileType,
			String loggedUser, String fileName) {
		try {
			Result res = null;
			ConfigurationManager confManager = new ConfigurationManager();
			SSHExec connection = null;
			try {
				connection = SSHUtils.connectToNode(hostName, username,
						password, privateKey);
				if (connection != null) {
					AnkushTask update = new EditConfProperty(propertyName,
							newValue, propertyFilePath, fileType);
					res = connection.exec(update);
					if (res.isSuccess) {
						// Configuration manager to save the
						// property file change records.
						confManager.saveConfiguration(dbCluster.getId(),
								loggedUser, fileName, hostName, propertyName,
								newValue);
					}

					if (propertyName.equalsIgnoreCase("rpc_port")) {
						GenericServiceMonitor gsm = new GenericServiceMonitor();
						gsm.stopagent(connection, null);
						gsm.startagent(connection, null);

						CassandraDeployer cd = new CassandraDeployer(
								cassandraConf);
						cd.stopNode(hostName);
						cd.startNode(hostName);
					}

				} else {
					logger.error("Could not connect to node..." + hostName);
				}
			} catch (Exception e) {
				logger.error("error:" + e.getMessage());
			} finally {
				// Disconnecting the connection
				if (connection != null) {
					connection.disconnect();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser,
			final String fileType, boolean editNodeParam, String nodeIp) {

		try {
			final String propertyName = parameter.getName();
			final String propertyValue = parameter.getValue();
			// get component homepath
			String componentHomePath = FileNameUtils
					.convertToValidPath(cassandraConf.getComponentHome());
			// get server.properties file path
			final String propertyFilePath = componentHomePath
					+ CASSANDRA_FOLDER_CONF + fileName;
			final String username = cassandraConf.getClusterConf()
					.getUsername();
			final String password = cassandraConf.getClusterConf()
					.getPassword();
			final String privateKey = cassandraConf.getClusterConf()
					.getPrivateKey();
			if (editNodeParam) {
				String hostName = nodeIp;
				addParameter(hostName, username, password, privateKey,
						propertyName, propertyValue, propertyFilePath,
						fileType, loggedUser, fileName);
			} else {
				List<NodeConf> cassandraNodes = this.cassandraNodes;

				final Semaphore semaphore = new Semaphore(cassandraNodes.size());
				try {
					// iterate over all the nodes.
					for (final NodeConf node : cassandraNodes) {
						semaphore.acquire();
						AppStoreWrapper.getExecutor().execute(new Runnable() {
							@Override
							public void run() {
								String hostName = node.getPublicIp();
								addParameter(hostName, username, password,
										privateKey, propertyName,
										propertyValue, propertyFilePath,
										fileType, loggedUser, fileName);
								if (semaphore != null) {
									semaphore.release();
								}

							}
						});
					}
					semaphore.acquire(cassandraNodes.size());
				} catch (Exception e) {
					logger.error("Error in updating config file params..."
							+ e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}
	}

	private void addParameter(String hostName, String username,
			String password, String privateKey, String propertyName,
			String propertyValue, String propertyFilePath, String fileType,
			String loggedUser, String fileName) {
		try {
			Result res = null;
			ConfigurationManager confManager = new ConfigurationManager();
			SSHExec connection = null;
			try {
				// connect to node/machine
				connection = SSHUtils.connectToNode(hostName, username,
						password, privateKey);
				// if connection is established.
				if (connection != null) {
					AnkushTask add = new AddConfProperty(propertyName,
							propertyValue, propertyFilePath, fileType);
					res = connection.exec(add);

					if (res.isSuccess) {
						// Configuration manager to save the
						// property file change records.
						confManager.saveConfiguration(dbCluster.getId(),
								loggedUser, fileName, hostName, propertyName,
								propertyValue);
					}
				} else {
					logger.error("Could not connect to node..." + hostName);
				}
			} catch (Exception e) {
				logger.error("error:" + e.getMessage());
			} finally {
				// Disconnecting the connection
				if (connection != null) {
					connection.disconnect();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteConfigFileParam(final Parameter parameter,
			final String fileName, final String fileType,
			boolean editNodeParam, String nodeIp) {

		try {
			final String propertyName = parameter.getName();
			String componentHomePath = FileNameUtils
					.convertToValidPath(cassandraConf.getComponentHome());
			final String propertyFilePath = componentHomePath
					+ CASSANDRA_FOLDER_CONF + fileName;

			final String username = cassandraConf.getClusterConf()
					.getUsername();
			final String password = cassandraConf.getClusterConf()
					.getPassword();
			final String privateKey = cassandraConf.getClusterConf()
					.getPrivateKey();
			if (editNodeParam) {
				String hostName = nodeIp;
				deleteParameter(hostName, username, password, privateKey,
						propertyName, propertyFilePath, fileType);
			} else {
				List<NodeConf> cassandraNodes = this.cassandraNodes;
				final Semaphore semaphore = new Semaphore(cassandraNodes.size());
				try {
					for (final NodeConf node : cassandraNodes) {
						semaphore.acquire();
						AppStoreWrapper.getExecutor().execute(new Runnable() {
							@Override
							public void run() {
								String hostName = node.getPublicIp();
								deleteParameter(hostName, username, password,
										privateKey, propertyName,
										propertyFilePath, fileType);
								configManager.removeOldConfiguration(
										dbCluster.getId(), hostName, fileName,
										propertyName);
								if (semaphore != null) {
									semaphore.release();
								}

							}
						});
					}
					semaphore.acquire(cassandraNodes.size());
				} catch (Exception e) {
					logger.error("Error in updating config file params..."
							+ e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}
	}

	private void deleteParameter(String hostName, String username,
			String password, String privateKey, String propertyName,
			String propertyFilePath, String fileType) {
		try {
			Result res = null;
			SSHExec connection = null;
			try {
				connection = SSHUtils.connectToNode(hostName, username,
						password, privateKey);
				if (connection != null) {
					AnkushTask update = new DeleteConfProperty(propertyName,
							propertyFilePath, fileType);
					res = connection.exec(update);
				} else {
					logger.error("Could not connect to node..." + hostName);
				}
			} catch (Exception e) {
				logger.error("error:" + e.getMessage());
			} finally {
				// Disconnecting the connection
				if (connection != null) {
					connection.disconnect();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> commonActions() {
		List<String> commonActionList = new ArrayList<String>();
		try {
			commonActionList.add(Constant.Cassandra_Actions.CASSANDRA_CLEANUP);
			commonActionList.add(Constant.Cassandra_Actions.CASSANDRA_COMPACT);
			commonActionList.add(Constant.Cassandra_Actions.CASSANDRA_FLUSH);
			commonActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_INVALIDATE_KEY_CACHE);
			commonActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_INVALIDATE_ROW_CACHE);
			commonActionList.add(Constant.Cassandra_Actions.CASSANDRA_SCRUB);
			return commonActionList;
		} catch (Exception e) {
			this.errors.add("Unable to prepare action list: " + e.getMessage());
			logger.error(e.getMessage());
		}
		return null;
	}

	private void nodeactionlist() {
		List<String> nodeActionList = new ArrayList<String>();
		try {
			nodeActionList.addAll(commonActions());
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_RESET_LOCAL_SCHEMA);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_PAUSE_HANDOFF);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_RESUME_HANDOFF);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_DISABLE_NATIVE_TRANSPORT);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_ENABLE_NATIVE_TRANSPORT);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_DISABLE_HANDOFF);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_ENABLE_HANDOFF);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_DISABLE_GOSSIP);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_ENABLE_GOSSIP);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_DISABLE_THRIFT);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_ENABLE_THRIFT);
			nodeActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_NODE_DECOMMISSION);
			nodeActionList.add(Constant.Cassandra_Actions.CASSANDRA_NODE_DRAIN);
			Collections.sort(nodeActionList);
			result.put("actions", nodeActionList);
		} catch (Exception e) {
			this.errors.add("Unable to prepare action list: " + e.getMessage());
			logger.error(e.getMessage());
		}
	}

	// private void action() {
	// String action = (String) parameterMap.get(Constant.Keys.ACTION);
	// nodeAction(action);
	// }

	private boolean validateAction(String ip, String action) {
		NodeMonitoring monitoring = new MonitoringManager()
				.getMonitoringData(ip);
		if (monitoring == null) {
			addError("Could not get Cassandra service status.");
			return false;
		}
		Map<String, Boolean> serviceMap = monitoring.getServiceStatus();
		if (monitoring == null) {
			addError("Could not get Cassandra service status.");
			return false;
		}

		if (!serviceMap.containsKey(Constant.Component.ProcessName.CASSANDRA)
				|| !serviceMap.get(Constant.Component.ProcessName.CASSANDRA)) {
			addError("Either Cassandra or Agent is down on the node.");
			return false;
		}

		return true;
	}

	private void action() {
		String action = (String) parameterMap.get(Constant.Keys.ACTION);
		if (action == null || action.isEmpty()) {
			this.errors.add("Action is missing.");
			return;
		}

		String hostName = (String) parameterMap.get(Constant.Keys.IP);
		if (hostName == null || hostName.isEmpty()) {
			this.errors.add("IP is missing.");
			return;
		}

		if (!validateAction(hostName, action)) {
			return;
		}

		String componentHome = cassandraConf.getComponentHome();

		SSHExec connection = null;
		try {

			StringBuffer operation = new StringBuffer();
			operation.append(componentHome).append(CASSANDRA_FOLDER_BIN)
					.append(Constant.Cassandra_Actions.CASSANDRA_NODETOOL)
					.append(" ").append(action);

			if (parameterMap.containsKey("keyspace")) {
				operation.append(" ").append(parameterMap.get("keyspace"));
			}
			if (parameterMap.containsKey("columnfamily")) {
				operation.append(" ").append(parameterMap.get("columnfamily"));
			}

			net.neoremind.sshxcute.core.Result res = null;
			logger.debug("Connecting with node : " + hostName);
			// connect to remote node
			connection = SSHUtils.connectToNode(hostName, cassandraConf
					.getClusterConf().getUsername(), cassandraConf
					.getClusterConf().getPassword(), cassandraConf
					.getClusterConf().getPrivateKey());

			// if connected
			if (connection != null) {
				// running command on each node
				CustomTask keyspaceOperation = new ExecCommand(
						operation.toString());
				res = connection.exec(keyspaceOperation);
				if (res.rc == 0) {
					result.put("output", action + " operation is done");
					logger.info(action + " is done");
				} else {
					this.errors.add(res.error_msg);
					logger.error(res.error_msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (!e.getMessage().isEmpty()) {
				logger.error(e.getMessage());
			} else {
				logger.error(e.getLocalizedMessage());
			}
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * this method added the required categories in Utilization Trends for Kafka
	 */
	@Override
	public Map categories() throws Exception {
		Map categoryMap = (Map) super.categories().get("categories");
		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

		// get kafkaConf
		CassandraConf cassandraConf = (CassandraConf) dbCluster
				.getClusterConf().getClusterComponents()
				.get(Constant.Component.Name.CASSANDRA);

		// map to contains different groups of kafka.
		Map<String, String> categories = new HashMap<String, String>();

		List<String> objTypesCassandraInternal = new ArrayList<String>();
		objTypesCassandraInternal.add("AntiEntropyStage");
		objTypesCassandraInternal.add("commitlog_archiver");
		objTypesCassandraInternal.add("FlushWriter");
		objTypesCassandraInternal.add("GossipStage");
		objTypesCassandraInternal.add("HintedHandoff");
		objTypesCassandraInternal.add("InternalResponseStage");
		objTypesCassandraInternal.add("MemtablePostFlusher");
		objTypesCassandraInternal.add("MigrationStage");
		objTypesCassandraInternal.add("MiscStage");

		List<String> objTypesCassandraRequest = new ArrayList<String>();
		objTypesCassandraRequest.add("MutationStage");
		objTypesCassandraRequest.add("ReadRepairStage");
		objTypesCassandraRequest.add("ReadStage");
		objTypesCassandraRequest.add("ReplicateOnWriteStage");
		objTypesCassandraRequest.add("RequestResponseStage");

		List<String> objTypesCassandraDB = new ArrayList<String>();
		objTypesCassandraDB.add("StorageProxy");
		objTypesCassandraDB.add("StorageService");
		objTypesCassandraDB.add("CompactionManager");
		objTypesCassandraDB.add("Caches");

		for (String objType : objTypesCassandraInternal) {
			categories.put("org.apache.cassandra.internal." + objType,
					"org.apache.cassandra.internal." + objType + ".*.rrd");
		}

		for (String objType : objTypesCassandraRequest) {
			categories.put("org.apache.cassandra.request." + objType,
					"org.apache.cassandra.request." + objType + ".*.rrd");
		}

		for (String objType : objTypesCassandraDB) {
			categories.put("org.apache.cassandra.db." + objType,
					"org.apache.cassandra.db." + objType + ".*.rrd");
		}

		try {
			// creating node graph object.
			Graph graph = new Graph(this.clusterConfig.getGangliaMaster()
					.getPublicIp(), this.clusterConfig.getUsername(),
					this.clusterConfig.getPassword(),
					this.clusterConfig.getPrivateKey(),
					this.clusterConfig.getClusterName());

			// get all the .rrd files for this nodeIp
			List<String> files = graph.getAllFiles(nodeIp);

			// iterate over categories Map
			for (String category : categories.keySet()) {
				Map map = new HashMap();

				// get the .rrd files of a particular category of kafka.
				List<String> matchingFiles = graph.getMatchingFiles(
						categories.get(category), files);

				map.put("count", matchingFiles.size());
				map.put("legends", matchingFiles);
				map.put("pattern", categories.get(category));
				map.put("ip", nodeIp);
				if (matchingFiles != null && !matchingFiles.isEmpty()) {
					categoryMap.put(category, map);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		result.put("categories", categoryMap);
		return result;
	}

	@Override
	public Map monitor(Cluster cluster, String action, Map parameterMap) {

		dbCluster = cluster;
		cassandraConf = (CassandraConf) cluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.CASSANDRA);

		return super.monitor(cluster, action, parameterMap);
	}

	/**
	 * Get cluster overview.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void clusteroverview() throws AnkushException {

		CassandraJMXData techData = CassandraJMXData
				.getTechnologyData(cassandraConf);

		if (techData != null) {
			result.put(Constant.Keys.DATACENTERS, techData.getDatacenters());
		} else {
			this.errors
					.add("Unable to show Ring Topology as all seed nodes are down.");
		}
	}

	private void nodeoverview() throws AnkushException {
		String hostName = (String) parameterMap.get(Constant.Keys.IP);
		if (hostName == null || hostName.isEmpty()) {
			this.errors.add("IP is missing.");
			return;
		}

		JmxUtil jmxUtil = null;
		Map<String, Object> nodeData = new HashMap<String, Object>();

		try {

			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(hostName);

			if (nodeMonitoring != null
					&& nodeMonitoring.getServiceStatus().get(
							Constant.Component.ProcessName.CASSANDRA) != null
					&& nodeMonitoring.getServiceStatus().get(
							Constant.Component.ProcessName.CASSANDRA) == true) {
				jmxUtil = getJMXConnection(hostName,
						Constant.Cassandra.JMX_PORT);
				if (jmxUtil == null) {
					this.errors
							.add("Unable to fetch node information as all the nodes are down");
				} else {
					ObjectName mObjNameCaches = new ObjectName(
							ORG_APACHE_CASSANDRA
									+ "db:type="
									+ Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_CACHES);

					List<String> keyCacheList = Arrays
							.asList(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCC,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCE,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCH,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCRHR,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCR,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCSPIS,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCS);

					List<String> rowCacheList = Arrays
							.asList(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCC,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCE,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCH,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCRHR,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCR,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCSPIS,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCS);

					ObjectName mObjNameStorageService = new ObjectName(
							ORG_APACHE_CASSANDRA
									+ "db:type="
									+ Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);

					ObjectName mObjNameFailureDetector = new ObjectName(
							ORG_APACHE_CASSANDRA
									+ "net:type="
									+ Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_FAILUREDETECTOR);

					// Getting ownership
					Object attrOwnership = jmxUtil
							.getAttribute(
									mObjNameStorageService,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP);
					LinkedHashMap ownership = (LinkedHashMap) attrOwnership;

					for (Object key : ownership.keySet()) {
						if (key.toString().trim().substring(1)
								.equalsIgnoreCase(hostName)) {
							nodeData.put(
									"ownership",
									String.valueOf(((Float) ownership.get(key)) * 100)
											+ " %");
							break;
						}
					}
					// Getting load
					Object attrLoadMap = jmxUtil
							.getAttribute(
									mObjNameStorageService,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LOADMAP);
					Map<String, String> load = (HashMap<String, String>) attrLoadMap;
					nodeData.put("load", load.get(hostName));
					// Getting status
					Object attrStatus = jmxUtil
							.getAttribute(
									mObjNameFailureDetector,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_SIMPLESTATES);
					Map<String, String> status = (Map) attrStatus;
					nodeData.put("status", status.get("/" + hostName));
					Object opParams[] = { hostName };
					String opSig[] = { String.class.getName() };
					Object resultSet = jmxUtil
							.getResultObjectFromOperation(
									mObjNameFailureDetector,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_ENDPOINTSTATE,
									opParams, opSig);
					String strResult = (String) resultSet;
					List<String> sysoutList = new ArrayList<String>(
							Arrays.asList(strResult.split("\n")));
					for (String outData : sysoutList) {

						List<String> paramList = new ArrayList<String>(
								Arrays.asList(outData.split(":")));
						String key = paramList.get(0).trim();

						if (key.equals("DC")) {
							nodeData.put("dataCenter", paramList.get(1).trim());
						} else if (key.equals("RACK")) {
							nodeData.put("rack", paramList.get(1).trim());
						} else if (key.equals("STATUS")) {
							nodeData.put("state", paramList.get(1).trim()
									.split(",")[0]);
						}
					}

					// Getting tokens
					resultSet = jmxUtil
							.getResultObjectFromOperation(
									mObjNameStorageService,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOKENS,
									opParams, opSig);
					List<String> tokens = (List<String>) resultSet;
					nodeData.put("tokens", tokens);
					nodeData.put("tokenCount", String.valueOf(tokens.size()));

					nodeData.put("host", hostName);
					nodeData.put("keyCache",
							getCache(mObjNameCaches, keyCacheList, jmxUtil));
					nodeData.put("rowCache",
							getCache(mObjNameCaches, rowCacheList, jmxUtil));
					result.put(Constant.Keys.NODEDATA, nodeData);
					return;
				}
			}
			this.errors
					.add("Unable to fetch node information as the node is down");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jmxUtil.disconnect();
		}
	}

	private Map<String, Object> getCache(ObjectName mObjNameCaches,
			List<String> caches, JmxUtil jmxUtil) {
		Map<String, Object> cacheMap = new HashMap<String, Object>();
		for (String cache : caches) {
			cacheMap.put(cache, jmxUtil.getAttribute(mObjNameCaches, cache));
		}
		return cacheMap;
	}

	private JmxUtil getJMXConnection(String hostname, int jmxPort) {
		JmxUtil jmxUtil = null;
		try {
			jmxUtil = new JmxUtil(hostname, jmxPort);
			// connect to node
			return jmxUtil.connect() == null ? null : jmxUtil;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void keyspaces() {
		JmxUtil jmxUtil = null;
		try {
			for (String publicIp : getActiveNodes()) {
				jmxUtil = getJMXConnection(publicIp,
						Constant.Cassandra.JMX_PORT);
				if (jmxUtil == null) {
					continue;
				}
				List<Keyspace> lstKeyspace;
				lstKeyspace = getKeyspaceInfo(jmxUtil, null, publicIp);
				result.put(Constant.Keys.TILES,
						getKeyspaceTile(lstKeyspace.size()));
				result.put(
						Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KEYSPACES,
						lstKeyspace);
				return;
			}
			this.errors
					.add("Unable to fetch Keyspace Information as all nodes are down.");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jmxUtil.disconnect();
		}
	}

	private List<TileInfo> getKeyspaceTile(int keyspaceCount) {

		List<TileInfo> tiles = new ArrayList<TileInfo>();
		TileInfo ksTile = null;
		try {
			String keyspace = "Keyspace";
			if (keyspaceCount > 1) {
				keyspace = Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KEYSPACES;
			}
			ksTile = new TileInfo();
			ksTile.setLine1(String.valueOf(keyspaceCount));
			ksTile.setLine2(keyspace);
			ksTile.setStatus(Constant.Tile.Status.NORMAL);
			ksTile.setUrl("Keyspace");
			tiles.add(ksTile);
			// putting the tiles.
			// result.put(Constant.Keys.TILES, tiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tiles;
	}

	private List<Keyspace> getKeyspaceInfo(JmxUtil jmxUtil, String where,
			String hostName) {
		List<Keyspace> lstKeyspace = new ArrayList<Keyspace>();
		try {
			String cassandraHome = cassandraConf.getComponentHome();
			String fileName = cassandraHome + "script";
			Integer rpcPort = getRpcPort(cassandraHome, hostName,
					cassandraConf.getClusterConf());

			String cmdFileContent2 = "echo \"select * from system.schema_keyspaces "
					+ (where == null ? "" : where)
					+ ";\" > "
					+ fileName
					+ " ; "
					+ cassandraHome
					+ "bin/cqlsh "
					+ hostName
					+ " "
					+ String.valueOf(rpcPort) + " -f " + fileName;

			SSHConnection sshConnection = new SSHConnection(hostName,
					cassandraConf.getClusterConf().getUsername(), cassandraConf
							.getClusterConf().getPassword(), cassandraConf
							.getClusterConf().getPrivateKey());
			sshConnection.exec(cmdFileContent2);

			if (sshConnection.getExitStatus() != 0) {
				this.errors.add("Unable to fetch Column Family Information");
			} else {
				List<String> keyspaceRecords = new ArrayList<String>(
						Arrays.asList(sshConnection.getOutput().split("\n")));
				while (true) {
					String removedString = keyspaceRecords.remove(0);
					if (removedString.contains("----+----")) {
						break;
					}
				}
				for (String str : keyspaceRecords) {
					List<String> listKeyspaceDetails = new ArrayList<String>(
							Arrays.asList(str.split("\\|")));
					if (listKeyspaceDetails.size() != 4) {
						break;
					}
					Keyspace ks = new Keyspace();
					ks.setKeyspaceName(listKeyspaceDetails.get(0).trim());
					ks.setDurableWrites(listKeyspaceDetails.get(1).trim());
					ks.setReplicationStrategy(listKeyspaceDetails.get(2)
							.replaceAll(".*\\.", "").trim());
					ks.setStrategyOptions(listKeyspaceDetails.get(3).trim());
					ks.setCfCount(getColumnFamilyList(ks.getKeyspaceName(),
							jmxUtil, "*").size());

					lstKeyspace.add(ks);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lstKeyspace;
	}

	private Set getColumnFamilyList(String keyspace, JmxUtil jmxUtil,
			String columnfamily) {
		try {
			String patternStr = "org.apache.cassandra.db:type=ColumnFamilies,keyspace="
					+ keyspace + ",columnfamily=" + columnfamily;
			Set columnFamilySet = jmxUtil
					.getObjectSetFromPatternString(patternStr);

			return columnFamilySet;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	private String getReplicationStrategy(String strategy_class) {
		String replicationStrategy = null;
		try {
			List<String> listStrategy = new ArrayList<String>(
					Arrays.asList(strategy_class.split("\\.")));
			replicationStrategy = listStrategy.get((listStrategy.size() - 1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return replicationStrategy;
	}

	private void keyspaceactionlist() {
		List<String> keyspaceActionList = new ArrayList<String>();
		try {
			keyspaceActionList.addAll(commonActions());
			keyspaceActionList.add(Constant.Cassandra_Actions.CASSANDRA_REPAIR);
			Collections.sort(keyspaceActionList);
			result.put("actions", keyspaceActionList);
		} catch (Exception e) {
			this.errors.add("Unable to prepare action list: " + e.getMessage());
			logger.error(e.getMessage());
		}
	}

	private void columnfamilies() {
		String keyspace = (String) parameterMap.get("keyspace");
		if (keyspace == null || keyspace.isEmpty()) {
			this.errors.add("Keyspace is missing.");
			return;
		}
		try {
			for (String publicIp : getActiveNodes()) {
				JmxUtil jmxUtil = getJMXConnection(publicIp,
						Constant.Cassandra.JMX_PORT);
				if (jmxUtil == null) {
					continue;
				}

				List<TileInfo> tiles = getColumnFamilyTiles(jmxUtil, keyspace,
						publicIp);

				if (tiles.isEmpty()) {
					this.errors.add("Invalid Keyspace Name.");
					return;
				}

				result.put(Constant.Keys.TILES, tiles);

				Set columnFamilySet = getColumnFamilyList(keyspace, jmxUtil,
						"*");
				List<ColumnFamily> lstColumnFamily = new ArrayList<ColumnFamily>();

				Iterator iter = columnFamilySet.iterator();
				while (iter.hasNext()) {
					String objectName = iter.next().toString();
					List<String> jmxObjectList = new ArrayList<String>(
							Arrays.asList(objectName.split("=")));
					ObjectName mObjCF = new ObjectName(objectName);
					String cfName = jmxObjectList.get(jmxObjectList.size() - 1);
					Object liveSSTableCount = jmxUtil
							.getAttribute(
									mObjCF,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LIVESSTABLECOUNT);
					Object writelatency = jmxUtil
							.getAttribute(
									mObjCF,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_WRITELATENCY);
					Object readlatency = jmxUtil
							.getAttribute(
									mObjCF,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_READLATENCY);
					Object pendingTasks = jmxUtil
							.getAttribute(
									mObjCF,
									Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_PENDINGTASKS);

					ColumnFamily cf = new ColumnFamily();
					cf.setColumnFamilyName(cfName);
					cf.setLiveSSTableCount(liveSSTableCount);
					cf.setWritelatency(writelatency);
					cf.setReadLatency(readlatency);
					cf.setPendingTasks(pendingTasks);

					lstColumnFamily.add(cf);
				}
				result.put("ColumnFamilies", lstColumnFamily);
				return;
			}
			this.errors
					.add("Unable to fetch Keyspace Details as all nodes are down");
		} catch (MalformedObjectNameException e) {
			logger.error(e.getMessage());
			this.errors.add("Unable to fetch Keyspace Details");
		} catch (NullPointerException e) {
			logger.error(e.getMessage());
			this.errors.add("Unable to fetch Keyspace Details");
		}
	}

	private List<TileInfo> getColumnFamilyTiles(JmxUtil jmxUtil,
			String keyspace, String hostname) {
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		try {
			List<Keyspace> lstKeyspace = getKeyspaceInfo(jmxUtil,
					"where keyspace_name='" + keyspace + "'", hostname);

			if (!lstKeyspace.isEmpty()
					&& lstKeyspace.get(0).getKeyspaceName()
							.equalsIgnoreCase(keyspace)) {
				Integer columnFamilyCount = lstKeyspace.get(0).getCfCount();
				String columnFamily = "Column Family";
				if (columnFamilyCount > 1) {
					columnFamily = "Column Families";
				}
				tiles.add(getColumnFamilyTile(columnFamilyCount.toString(),
						columnFamily));
				tiles.add(getColumnFamilyTile(lstKeyspace.get(0)
						.getDurableWrites(), "Durable writes"));
				tiles.add(getColumnFamilyTile(lstKeyspace.get(0)
						.getReplicationStrategy(), "Strategy Class"));
				return tiles;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tiles;
	}

	private GeneralProperty setGeneralProprties(Map<String, Object> attributes) {
		GeneralProperty generalProperties = new GeneralProperty();
		try {
			generalProperties
					.setMinRowSize(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MINROWSIZE));
			generalProperties
					.setLiveDiskSpaceUsed(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LIVEDISKSPACEUSED));
			generalProperties
					.setReadCount(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_READCOUNT));
			generalProperties
					.setMeanRowSize(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEANROWSIZE));
			generalProperties
					.setMemtableSwitchCount(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEMTABLESWITCHCOUNT));
			generalProperties
					.setUnleveledSSTables(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_UNLEVELEDSSTABLES));
			generalProperties
					.setWriteCount(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_WRITECOUNT));
			generalProperties
					.setTotalDiskSpaceUsed(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOTALDISKSPACEUSED));
			generalProperties
					.setMemtableColumnsCount(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEMTABLECOLUMNSCOUNT));
			generalProperties
					.setMaxRowSize(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MAXROWSIZE));
			generalProperties
					.setMemtableDataSize(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEMTABLEDATASIZE));
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return generalProperties;
	}

	private CompactionProperty setCompactionProperties(
			Map<String, Object> attributes) {
		CompactionProperty compactionProperties = new CompactionProperty();
		try {
			Object compactionStrategyClassObject = attributes
					.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_COMPACTIONSTRATEGYCLASS);
			String compactionStrategyClass;
			if (compactionStrategyClassObject != null
					|| !compactionStrategyClassObject.toString().isEmpty()) {
				List<String> compactionStrategyClassList = new ArrayList<String>(
						Arrays.asList(compactionStrategyClassObject.toString()
								.split("\\.")));
				compactionStrategyClass = compactionStrategyClassList
						.get(compactionStrategyClassList.size() - 1);
			} else {
				compactionStrategyClass = "";
			}
			compactionProperties
					.setMinCompactionThreshold(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MINCOMPACTIONTHRESHOLD));
			compactionProperties
					.setMaxCompactionThreshold(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MAXCOMPACTIONTHRESHOLD));
			compactionProperties
					.setCompactionStrategyClass(compactionStrategyClass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return compactionProperties;
	}

	private CompressionProperty setCompressionProperties(
			Map<String, Object> attributes) {
		CompressionProperty compressionProperties = new CompressionProperty();
		try {
			Object compressionParameter = attributes
					.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_COMPRESSIONPARAMETERS);
			Map<String, String> compressionParams = (Map<String, String>) compressionParameter;
			String compressionType;
			if (compressionParams.containsKey("sstable_compression")) {
				String compressionTypeObject = compressionParams
						.get("sstable_compression");
				List<String> compressionTypeList = new ArrayList<String>(
						Arrays.asList(compressionTypeObject.toString().split(
								"\\.")));
				compressionType = compressionTypeList.get(compressionTypeList
						.size() - 1);
			} else {
				compressionType = "";
			}
			compressionProperties
					.setCompressionRatio(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_COMPRESSIONRATIO));
			compressionProperties.setSstable_compression(compressionType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return compressionProperties;
	}

	private PerformanceTuningProperty setPerformanceTuningProperties(
			Map<String, Object> attributes) {
		PerformanceTuningProperty performanceTuningProperties = new PerformanceTuningProperty();
		try {
			performanceTuningProperties
					.setBloomFilterFalsePositives(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERFALSEPOSITIVES));
			performanceTuningProperties
					.setBloomFilterDiskSpaceUsed(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERDISKSPACEUSED));
			performanceTuningProperties
					.setRecentReadLatencyMicros(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTREADLATENCYMICROS));
			performanceTuningProperties
					.setRecentBloomFilterFalseRatios(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTBLOOMFILTERFALSERATIOS));
			performanceTuningProperties
					.setDroppableTombStoneRatio(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_DROPPABLETOMBSTONERATIO));
			performanceTuningProperties
					.setBloomFilterFalseRatios(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERFALSERATIO));
			performanceTuningProperties
					.setTotalWriteLatencyMicros(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOTALWRITELATENCYMICROS));
			performanceTuningProperties
					.setRecentWriteLatencyMicros(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTWRITELATENCYMICROS));
			performanceTuningProperties
					.setRecentBloomFilterFalsePositives(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTBLOOMFILTERFALSEPOSITIVES));
			performanceTuningProperties
					.setTotalReadLatencyMicros(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOTALREADLATENCYMICROS));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return performanceTuningProperties;
	}

	private void columnfamilydetails() {
		String keyspace = (String) parameterMap.get("keyspace");
		String columnFamily = (String) parameterMap.get("columnfamily");
		if (keyspace == null || keyspace.isEmpty() || columnFamily == null
				|| columnFamily.isEmpty()) {
			this.errors.add("Either Keyspace or Column Family is missing.");
			return;
		}
		try {
			int jmxPort = Constant.Cassandra.JMX_PORT;
			for (CassandraNodeConf nodeConf : cassandraConf.getNodes()) {
				NodeMonitoring nodeMonitoring = new MonitoringManager()
						.getMonitoringData(nodeConf.getId());

				if (nodeMonitoring != null
						&& nodeMonitoring.getServiceStatus().get(
								Constant.Component.ProcessName.CASSANDRA) != null
						&& nodeMonitoring.getServiceStatus().get(
								Constant.Component.ProcessName.CASSANDRA) == true) {

					JmxUtil jmxUtil = getJMXConnection(nodeConf.getPublicIp(),
							jmxPort);
					if (jmxUtil == null) {
						continue;
					}

					Object cfset = getColumnFamilyList(keyspace, jmxUtil,
							columnFamily);
					Set cfset1 = (HashSet) cfset;

					if (cfset1 == null || cfset1.isEmpty()) {
						this.errors
								.add("Either keyspace or Column Family Name is invalid.");
						return;
					}
					ObjectName mObjCF = new ObjectName(cfset1.iterator().next()
							.toString());
					Map<String, Object> attributes = jmxUtil
							.getAttributes(mObjCF);

					ColumnFamily cf = new ColumnFamily();
					cf.setColumnFamilyName(columnFamily);
					cf.setLiveSSTableCount(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LIVESSTABLECOUNT));
					cf.setWritelatency(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_WRITELATENCY));
					cf.setReadLatency(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_READLATENCY));
					cf.setPendingTasks(attributes
							.get(Constant.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_PENDINGTASKS));

					GeneralProperty generalProperties = setGeneralProprties(attributes);
					CompactionProperty compactionProperties = setCompactionProperties(attributes);
					CompressionProperty compressionProperties = setCompressionProperties(attributes);
					PerformanceTuningProperty performanceTuningProperties = setPerformanceTuningProperties(attributes);

					Map<String, Object> columnMetadata = columnmetadata(
							keyspace, columnFamily);

					Map<String, Object> properties = new HashMap<String, Object>();
					properties.put("generalProperties", generalProperties);
					properties
							.put("compactionProperties", compactionProperties);
					properties.put("compressionProperties",
							compressionProperties);
					properties.put("performanceTuningProperties",
							performanceTuningProperties);
					properties.put("columnMetadata", columnMetadata);
					result.put("ColumnFamilyDetails", properties);
					return;
				}
			}
			this.errors
					.add("Unable to fetch Column Family information as all nodes are down.");

		} catch (Exception e) {
			logger.error(e.getMessage());
			this.errors.add("Unable to get Column Family information");
		}
	}

	private Map<String, Object> columnmetadata(String keyspace,
			String columnfamily) {
		try {
			String columnMetadata = null;
			Map<String, Object> metadata = new HashMap<String, Object>();

			String cassandraHome = cassandraConf.getComponentHome();
			String fileName = cassandraHome + "columnmetadata";

			NodeMonitoring nodeMonitoring;

			for (String publicIp : getActiveNodes()) {

				Integer rpcPort = getRpcPort(cassandraHome, publicIp,
						cassandraConf.getClusterConf());
				String cmdFileContent2 = "echo \"use " + keyspace
						+ ";\n describe " + columnfamily + ";\" > " + fileName
						+ " ; " + cassandraHome + "bin/cassandra-cli -h "
						+ publicIp + " -p " + String.valueOf(rpcPort) + " -f "
						+ fileName;

				SSHConnection sshConnection = new SSHConnection(publicIp,
						cassandraConf.getClusterConf().getUsername(),
						cassandraConf.getClusterConf().getPassword(),
						cassandraConf.getClusterConf().getPrivateKey());
				sshConnection.exec(cmdFileContent2);

				if (sshConnection.getExitStatus() != 0) {
					this.errors.add("Unable to get Column Family information");
				} else {
					if (sshConnection.getOutput().contains("Column Metadata:")) {
						int beginIndex = sshConnection.getOutput().indexOf(
								"Column Name:");
						int endIndex = sshConnection.getOutput().indexOf(
								"Compaction Strategy:");
						columnMetadata = new String();
						columnMetadata = sshConnection.getOutput().substring(
								beginIndex, endIndex);
						List<String> listColumnns = new ArrayList<String>(
								Arrays.asList(columnMetadata
										.split("Column Name:")));
						listColumnns.remove(0);
						for (String column : listColumnns) {
							List<String> listColumnn = new ArrayList<String>(
									Arrays.asList(column.split("\n")));
							listColumnn.remove(listColumnn.size() - 1);
							Map<String, String> mapColumn = new HashMap<String, String>();
							String columnName = new String();
							columnName = listColumnn.get(0).trim();
							listColumnn.remove(0);
							for (String detail : listColumnn) {
								List<String> columnDetail = new ArrayList<String>(
										Arrays.asList(detail.split(":")));
								mapColumn.put(columnDetail.get(0).trim(),
										columnDetail.get(1).trim());
							}
							metadata.put(columnName, mapColumn);
						}
					}
					return metadata;
				}
			}
			this.errors.add("Unable to fetch Column Family Information");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void columnfamilyactionlist() {
		List<String> columnFamilyActionList = new ArrayList<String>();
		try {
			columnFamilyActionList.addAll(commonActions());
			columnFamilyActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_REFRESH);
			columnFamilyActionList
					.add(Constant.Cassandra_Actions.CASSANDRA_REPAIR);
			Collections.sort(columnFamilyActionList);
			result.put("actions", columnFamilyActionList);
		} catch (Exception e) {
			this.errors.add("Unable to prepare action list: " + e.getMessage());
			logger.error(e.getMessage());
		}
	}

	private Integer getRpcPort(String homePath, String hostname,
			ClusterConf conf) {
		Integer rpcPort = 0;
		try {
			/** yaml file path **/
			String cassandraYamlFile = homePath
					+ CASSANDRA_FOLDER_CONF
					+ Constant.Cassandra_Configuration_Files.CASSANDRA_FILE_YAML;

			/** yaml content **/
			String fileContent = SSHUtils.getFileContents(
					cassandraYamlFile,
					hostname,
					conf.getUsername(),
					conf.isAuthTypePassword() ? conf.getPassword() : conf
							.getPrivateKey(), conf.isAuthTypePassword());

			/** create yaml object. **/
			Yaml yaml = new Yaml();
			/** create map object by loading from yaml object **/
			Map map = (Map) yaml.load(fileContent);

			rpcPort = (Integer) map.get("rpc_port");

			return rpcPort;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rpcPort;
	}

	private TileInfo getColumnFamilyTile(String line1, String line2) {
		// create tile
		TileInfo keyspaceCountTile = null;
		try {
			keyspaceCountTile = new TileInfo();
			keyspaceCountTile.setLine1(line1);
			keyspaceCountTile.setLine2(line2);
			keyspaceCountTile.setStatus(Constant.Tile.Status.NORMAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyspaceCountTile;
	}

}
