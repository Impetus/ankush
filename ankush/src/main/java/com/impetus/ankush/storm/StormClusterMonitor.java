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
package com.impetus.ankush.storm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.Tile;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.Parameter;

/**
 * The Class StormClusterMonitor.
 * 
 * @author hokam
 */
public class StormClusterMonitor extends AbstractMonitor {

	private static final String STORM_YAML = "storm.yaml";

	/** The Constant NO_TOPOLOGY_INFORMATION_MESSAGE. */
	private static final String NO_TOPOLOGY_INFORMATION_MESSAGE = "Unable to get the topology information for node.";

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(StormClusterMonitor.class);

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The Set StormNodes. */
	private Set<NodeConf> nodes;

	/** The StormConf. */
	private StormConf stormConf;

	/** The Constant STR_SPACE. */
	private static final String STR_SPACE = " ";
	
	// configuration manager for save/delete configuration.
	private ConfigurationManager confManager = new ConfigurationManager();

	/**
	 * Details.
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
	 * Sets the nodes log msg.
	 * 
	 * @param nodes
	 *            the new nodes
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
	 * Method to get the activity progress logs.
	 * 
	 * @return Map of all progress logs.
	 */
	private void nodelist() {
		setNodes(dbCluster.getClusterConf().getNodeConfs());
	}

	/**
	 * Method to get the activity progress logs.
	 * 
	 * @return Map of all progress logs.
	 */
	private void addingnodes() {
		/** Getting storm cluster config. **/
		ClusterConf clusterConf = dbCluster.getClusterConf();
		GenericConfiguration conf = clusterConf.getClusterComponents().get(
				Constant.Technology.STORM);

		List<NodeConf> nodes = conf.getNewNodes();

		if (nodes == null || nodes.isEmpty()) {
			return;
		}

		setNodes(conf.getNewNodes());

		// getting the event state.
		String state = clusterConf.getState();
		for (NodeConf node : conf.getNewNodes()) {
			if (node.getNodeState().equals(Constant.Keys.ERROR)) {
				state = node.getNodeState();
				break;
			}
		}

		// putting event state in map.
		result.put(Constant.Keys.STATE, state);

		if (!state.equals(Constant.Cluster.State.ADDING_NODES)) {
			conf.setNewNodes(null);
			dbCluster.setClusterConf(clusterConf);
			clusterManager.save(dbCluster);
		}
	}

	/**
	 * Nodes.
	 */
	private void nodes() {
		try {
			Map<String, Object> propMap = new HashMap<String, Object>();
			propMap.put("clusterId", dbCluster.getId());
			List<Node> nodes = nodeManager.getAllByPropertyValue(propMap);

			List<Map> nodesMap = new ArrayList<Map>();
			// iterating over the nodes
			for (Node dbNode : nodes) {
				NodeConf node = dbNode.getNodeConf();
				if (node.getNodeState().equals(Constant.Node.State.ADDING)) {
					continue;
				}
				LinkedHashMap nodeMap = JsonMapperUtil.objectFromObject(node,
						LinkedHashMap.class);
				// creating monitoring manager object
				MonitoringManager manager = new MonitoringManager();
				// getting node monitoring data
				NodeMonitoring nodeMonitoring = manager.getMonitoringData(node
						.getId());

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
			result.put(Constant.Keys.TILES, getNodesTypeTiles());
			result.putAll(Collections.singletonMap(Constant.Keys.NODES,
					nodesMap));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
	}

	/**
	 * Sets the add node tile.
	 * 
	 * @return the adds the node tile
	 */
	private TileInfo getAddNodeTile() {
		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);
		try {
			if (conf != null && conf.getNewNodes() != null
					&& !conf.getNewNodes().isEmpty()) {
				List<NodeConf> nodes = conf.getNewNodes();
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
	 * @return the removes the node tile
	 */
	private TileInfo getRemoveNodeTile() {
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
	 * Method to get nodes tile.
	 * 
	 * @return the nodes tile
	 */
	private List<TileInfo> getNodesTypeTiles() {

		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		Integer supervisors = conf.getSupervisors().size();
		Integer zookeepers = conf.getZkNodes().size();
		String nimbus = conf.getNimbus().getPublicIp();

		// create namenode tile
		TileInfo nimbusTile = new TileInfo(nimbus, Constant.Role.NIMBUS, null,
				null, Constant.Tile.Status.NORMAL, Constant.Tile.Url.NODE_LIST);

		// create datanode tile
		TileInfo supervisorTile = new TileInfo(supervisors.toString(),
				Constant.Role.SUPERVISOR, null, null,
				Constant.Tile.Status.NORMAL, Constant.Tile.Url.NODE_LIST);

		// create secondary name node tile
		TileInfo zookeeperTile = new TileInfo(zookeepers.toString(),
				Constant.Role.ZOOKEEPER, null, null,
				Constant.Tile.Status.NORMAL, Constant.Tile.Url.NODE_LIST);

		// list object.
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		tiles.add(nimbusTile);
		tiles.add(supervisorTile);
		tiles.add(zookeeperTile);

		return tiles;
	}

	/**
	 * Cluster tiles.
	 */
	private void clustertiles() {
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// creating tile manager object.
		TileManager tileManager = new TileManager();

		// add node type tiles.
		tiles.addAll(getNodesTypeTiles());

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

		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		/** Node monitoring object **/
		NodeMonitoring nodeMonitoring = new MonitoringManager()
				.getMonitoringData(conf.getNimbus().getId());

		if (nodeMonitoring.getTechnologyData() != null) {
			/** storm monitoring object */
			StormMonitoringData data = (StormMonitoringData) nodeMonitoring
					.getTechnologyData().get(Constant.Technology.STORM);

			if (data != null) {
				tiles.add(new TileInfo(data.getUsedSlots() + "/"
						+ data.getTotalSlots(), "Used/Total Slots", null, null,
						Tile.Status.NORMAL, null));

				tiles.add(new TileInfo(data.getExecutors() + "", "Executers",
						null, null, Tile.Status.NORMAL, null));

				tiles.add(new TileInfo(data.getTasks() + "", "Tasks", null,
						null, Tile.Status.NORMAL, null));

				Long startTime = new Long(data.getNimbusUpTime());
				long days = TimeUnit.SECONDS.toDays(startTime);
				long hours = TimeUnit.SECONDS.toHours(startTime) - (days * 24);
				long minutes = TimeUnit.SECONDS.toMinutes(startTime)
						- (hours * 60) - (days * 24 * 60);

				StringBuilder time = new StringBuilder();
				if (days > 0) {
					time.append(days).append("d").append(" ");
				}
				if (hours > 0) {
					time.append(hours).append("h").append(" ");
				}
				if (minutes > 0) {
					time.append(minutes).append("m").append(" ");
				}
				if (time.length() != 0) {
					tiles.add(new TileInfo(time.toString(), "Nimbus UpTime",
							null, null, Tile.Status.NORMAL, null));
				}
			}
		}

		// putting the tiles.
		result.put(Constant.Keys.TILES, tiles);
	}

	/**
	 * Method to get the map of role, ip list.
	 * 
	 * @return The Map of role and ip list.
	 */
	private Map techlogs() {

		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		// nimbus.
		List<String> nimbus = Collections.singletonList(conf.getNimbus()
				.getPublicIp());

		// supervisor node ip list.
		List<String> supervisors = new ArrayList<String>();

		// filling supervisor nodes.
		for (NodeConf nodeConf : conf.getSupervisors()) {
			supervisors.add(nodeConf.getPublicIp());
		}

		result.put(Constant.Role.NIMBUS, nimbus);
		result.put(Constant.Role.SUPERVISOR, supervisors);
		return returnResult();
	}

	/**
	 * Method to get the node type files.
	 * 
	 * @return The Map of files and directories against the ip address.
	 * @throws Exception
	 *             the exception
	 */
	private void files() throws Exception {

		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);
		String type = (String) parameterMap.get(Constant.Keys.TYPE);

		LogViewHandler logHandler = new LogViewHandler(nodeIp,
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey());

		// Making the log directory.
		String logDirectory = conf.getComponentHome() + "/logs";

		// Name Node ip list.
		List<String> files = logHandler.listLogDirectory(logDirectory);

		// include types.
		List<String> includeTypes = new ArrayList<String>();

		// adding given types to list.
		includeTypes.add(type.toLowerCase());
		if (type.equalsIgnoreCase(Constant.Role.SUPERVISOR)) {
			includeTypes.add(Constant.Keys.WORKER);
		} else if (type.equalsIgnoreCase(Constant.Role.NIMBUS)) {
			includeTypes.add(Constant.Keys.UI);
		}

		// list of type files.
		List<String> typeFiles = new ArrayList<String>();

		String fileNamePart = null;
		// iterate over the file names.
		for (String fileName : files) {
			// split the string using - character.

			String namePartArray[] = fileName.split("\\.");

			if (namePartArray.length > 0) {
				fileNamePart = namePartArray[0];
			} else {
				fileNamePart = fileName;
			}

			if (includeTypes.contains(fileNamePart)) {
				typeFiles.add(fileName);
			}
		}

		if (typeFiles.isEmpty()) {
			addError("No " + type + " log file available on node.");
		}

		// puting the files in list.
		result.put(Constant.Keys.FILES, typeFiles);
	}

	/**
	 * Method to view the content of the file.
	 * 
	 * @return The content of file.
	 */
	private Map view() {
		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

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

		String filePath = conf.getComponentHome() + "/logs/" + fileName;

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
	 * Method to get download url of the file.
	 * 
	 * @return Map of the download url.
	 */
	private Map download() {

		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		// Getting the ip address from parameter map.
		String ip = (String) parameterMap.get(Constant.Keys.IP);
		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);

		// Create the lod View Handler object.
		LogViewHandler logHandler = new LogViewHandler(ip, conf.getUsername(),
				conf.getPassword(), conf.getPrivateKey());

		String filePath = conf.getComponentHome() + "/logs/" + fileName;

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
	 * Params.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void params() throws Exception {

		boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
		if (isAgentDown) {
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return;
		}

		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		Map<String, Object> paramMap = null;
		try {
			/** Node monitoring object **/
			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(conf.getNimbus().getId());
			if (nodeMonitoring == null) {
				this.errors.add("Monitoring data is null.");
				return;
			}

			/** storm monitoring object */
			StormMonitoringData data = (StormMonitoringData) nodeMonitoring
					.getTechnologyData().get(Constant.Technology.STORM);

			paramMap = JsonMapperUtil.objectFromString(data.getNimbusConf(),
					HashMap.class);
			Map<String, List<Object>> finalMap = new HashMap<String, List<Object>>();
			for (String key : paramMap.keySet()) {
				List<Object> paramValueAndStatusList = new ArrayList<Object>();
				paramValueAndStatusList.add(paramMap.get(key));
				paramValueAndStatusList.add(true);
				finalMap.put(key, paramValueAndStatusList);
			}
			setParamEditableStatus(finalMap);
			String fileName = STORM_YAML;
			Map<String, Map> finalMapToReturn = new HashMap<String, Map>();
			finalMapToReturn.put(fileName, finalMap);

			result.put("params", finalMapToReturn);
		} catch (NullPointerException e) {
			this.errors
					.add("Parameters cann't be obtained as Nimbus Node is down on : "
							+ conf.getNimbus().getPublicIp());
		} catch (Exception e) {
			this.errors.add(e.getMessage());
		}

	}

	/**
	 * Sets the param editable status.
	 * 
	 * @param map
	 *            the map
	 */
	private void setParamEditableStatus(Map<String, List<Object>> map) {
		List<String> paramlist = new ArrayList<String>();
		paramlist.add("java.library.path");
		paramlist.add("storm.local.dir");
		paramlist.add("storm.zookeeper.servers");
		paramlist.add("storm.zookeeper.port");
		paramlist.add("storm.zookeeper.root");
		paramlist.add("storm.cluster.mode");
		paramlist.add("storm.local.mode.zmq");
		paramlist.add("storm.thrift.transport");
		paramlist.add("storm.messaging.transport");
		paramlist.add("nimbus.host");
		paramlist.add("nimbus.thrift.port");
		for (String param : paramlist) {
			if (map.containsKey(param)) {
				List<Object> paramValueAndStatusList = (List<Object>) map
						.get(param);
				paramValueAndStatusList.set(1, false);
			}
		}
	}

	/**
	 * To edit the configuration parameters.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void editparams() throws Exception {
		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		Map<String, Object> confParamsMap = (Map<String, Object>) parameterMap
				.get("params");
		String loggedUser = (String) parameterMap.get("loggedUser");
		this.nodes = conf.getCompNodes();
		this.stormConf = conf;
		for (String key : confParamsMap.keySet()) {
			String fileName = key;
			List<Map> confParams = (List<Map>) confParamsMap.get(key);
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
						addConfigFileParam(parameter, fileName, loggedUser);
					}
					if (status.equals(Constant.Parameter_Status.EDIT)) {
						editConfigFileParam(parameter, fileName, loggedUser);
					}
					if (status.equals(Constant.Parameter_Status.DELETE)) {
						deleteConfigFileParam(parameter, fileName);
					}

				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}

	}

	/**
	 * Edit the configuration parameters.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void editConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser) {
		Set<NodeConf> nodes = this.nodes;
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String newValue = parameter.getValue();
							// get component homepath
							String componentHomePath = stormConf
									.getComponentHome();
							// get storm.yaml file path
							String yamlFilePath = componentHomePath
									+ "/conf/storm.yaml";
							String username = stormConf.getUsername();
							String password = stormConf.getPassword();
							String privateKey = stormConf.getPrivateKey();
							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask update = new EditConfProperty(
											propertyName, newValue,
											yamlFilePath,
											Constant.File_Extension.YAML);
									res = connection.exec(update);
									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												stormConf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, newValue);
									}
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
								if (semaphore != null) {
									semaphore.release();
								}
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}

	}

	/**
	 * Delete the configuration parameters.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 */
	private void deleteConfigFileParam(final Parameter parameter,
			String fileName) {
		Set<NodeConf> nodes = this.nodes;
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.editConfigFileParam
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							String propertyName = parameter.getName();
							// get component homepath
							String componentHomePath = stormConf
									.getComponentHome();
							// get storm.yaml file path
							String yamlFilePath = componentHomePath
									+ "/conf/storm.yaml";

							String username = stormConf.getUsername();
							String password = stormConf.getPassword();
							String privateKey = stormConf.getPrivateKey();
							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask delete = new DeleteConfProperty(
											propertyName, yamlFilePath,
											Constant.File_Extension.YAML);
									connection.exec(delete);
									// removing records from the configuration
									// table.
									confManager.removeOldConfiguration(
											stormConf.getClusterDbId(),
											hostName, STORM_YAML, propertyName);
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
								if (semaphore != null) {
									semaphore.release();
								}
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}
	}

	/**
	 * Adds new param to the Configuration file.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void addConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser) {
		Set<NodeConf> nodes = this.nodes;
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String paramValue = parameter.getValue();
							// get component homepath
							String componentHomePath = stormConf
									.getComponentHome();
							// get storm.yaml file path
							String yamlFilePath = componentHomePath
									+ "/conf/storm.yaml";
							String username = stormConf.getUsername();
							String password = stormConf.getPassword();
							String privateKey = stormConf.getPrivateKey();

							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);

								if (connection != null) {
									AnkushTask add = new AddConfProperty(
											propertyName, paramValue,
											yamlFilePath,
											Constant.File_Extension.YAML);
									res = connection.exec(add);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												stormConf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, paramValue);
									}
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
								if (semaphore != null) {
									semaphore.release();
								}
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}
	}

	/**
	 * Topology.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void topology() throws Exception {

		/** Getting storm cluster config. **/
		StormConf conf = (StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM);

		/** Node monitoring object **/
		NodeMonitoring nodeMonitoring = new MonitoringManager()
				.getMonitoringData(conf.getNimbus().getId());

		if (nodeMonitoring != null) {
			if (nodeMonitoring.getTechnologyData() != null) {
				/** storm monitoring object */
				StormMonitoringData data = (StormMonitoringData) nodeMonitoring
						.getTechnologyData().get(Constant.Technology.STORM);

				if (data != null) {
					/** getting topology from object */
					Map topology = JsonMapperUtil.mapFromObject(data);
					/** putting storm topology data in map. **/
					result.put("topology", topology);
				} else {
					addError(NO_TOPOLOGY_INFORMATION_MESSAGE);
				}
			} else {
				addError(NO_TOPOLOGY_INFORMATION_MESSAGE);
			}
		} else {
			addError(NO_TOPOLOGY_INFORMATION_MESSAGE);
		}
	}

	/**
	 * SubmitTopology.
	 * 
	 * @return the map
	 */
	private Map submitTopology() {
		final Map map = new HashMap();
		// get stormConf
		StormConf stormConf = ((StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM));

		String stormBinPath = FileUtils
				.getSeparatorTerminatedPathEntry(stormConf.getComponentHome())
				+ "bin/";

		List<String> topologyArguments = (List<String>) parameterMap
				.get(Constant.Keys.TOPOLOGYARGS);

		String uploadedJarPath = (String) parameterMap
				.get(Constant.Keys.JARPATH);

		String topologyName = (String) parameterMap.get(Constant.Keys.TOPOLOGY);
		String jarPath = null;

		String hostname = stormConf.getNimbus().getPublicIp();
		String username = stormConf.getUsername();
		String password = stormConf.getPassword();
		String privateKey = stormConf.getPrivateKey();

		String userHome = CommonUtil.getUserHome(stormConf.getUsername());

		String destTopologyFilePath = FileUtils
				.getSeparatorTerminatedPathEntry(userHome)
				+ ".ankush/monitoring/storm/";

		SSHExec connection = null;
		boolean isSuccessful = false;
		try {
			// get uploaded file from jarPath
			File f = new File(uploadedJarPath);
			if (!f.exists()) {
				addError("Could not find jar file.. !");
			} else {
				// get jarName
				String fileName = FileUtils.getNamePart(uploadedJarPath);

				jarPath = destTopologyFilePath + fileName;

				// connect to node/machine
				connection = SSHUtils.connectToNode(hostname, username,
						password, privateKey);
				// if connected
				if (connection != null) {
					// Create Directory on master node
					AnkushTask createDir = new MakeDirectory(
							destTopologyFilePath);
					Result res = connection.exec(createDir);
					if (!res.isSuccess) {
						addError("Could not create topology directory on node..! ");
					} else {
						// Uploading topology file to master node
						try {
							connection.uploadSingleDataToServer(
									uploadedJarPath, jarPath);
							isSuccessful = true;
						} catch (Exception e1) {
							addError("Could not upload topology file to node.. !");
						}
					}
				} else {
					addError("Could not establish connection to node.. !");
				}
			}
		} catch (Exception e) {
			addError("Topology Submission failed " + e.getMessage());
			logger.error("", e);
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}

		if (isSuccessful) {
			// topology Submission Logic
			try {
				// topology Submission command
				StringBuffer topologySubmitCommand = new StringBuffer();
				topologySubmitCommand.append(stormBinPath).append("storm")
						.append(STR_SPACE).append("jar").append(STR_SPACE);
				topologySubmitCommand.append(jarPath).append(STR_SPACE)
						.append(topologyName).append(STR_SPACE);

				if (topologyArguments != null) {
					for (String args : topologyArguments) {
						topologySubmitCommand.append(args).append(STR_SPACE);
					}
				}
				final String commad = topologySubmitCommand.toString();

				// submit the topologySubmission command
				final Map output = submitTopology(dbCluster, commad);

				String message = (String) output.get("output");
				// if topology is not submitted.
				if (!(Boolean) output.get("status")) {
					// add error to the error-list
					addError(message);
				} else {
					map.put("message", message);
				}
				// connect to node/machine
				connection = SSHUtils.connectToNode(hostname, username,
						password, privateKey);
				// if connected
				if (connection != null) {
					// delete topology jar from storm folder
					AnkushTask removeJar = new Remove(jarPath);
					Result res = connection.exec(removeJar);
					if (!res.isSuccess) {
						addError("Couldn't remove storm topology jar from "
								+ jarPath);
					}
				} else {
					addError("Could not establish connection to node.. !");
				}
			} catch (Exception e) {
				addError("Topology Submission failed " + e.getMessage());
				logger.error("", e);
				addError(e.getMessage());
			} finally {
				if(connection != null) {
					connection.disconnect();
				}
			}
		}
		result.putAll(map);
		return map;
	}

	/**
	 * Submit topology.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param command
	 *            the command
	 * @return the map
	 */
	private Map submitTopology(Cluster cluster, String command) {
		SSHExec connection = null;
		Map output = null;
		// get stormConf
		StormConf stormConf = ((StormConf) cluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM));
		ClusterConf clusterConf = cluster.getClusterConf();
		String hostname = stormConf.getNimbus().getPublicIp();
		String username = stormConf.getUsername();
		String password = stormConf.getPassword();
		String privateKey = stormConf.getPrivateKey();
		try {
			// Execute topology submission command and get output/error
			output = SSHUtils.getCommandOutputAndError(command, hostname,
					username, clusterConf.isAuthTypePassword() ? password
							: privateKey, clusterConf.isAuthTypePassword());
		} catch (Exception e) {
			logger.error("", e);
			addError(e.getMessage());
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		return output;
	}

	/**
	 * Perform Actions to manage topology like
	 * Activate/Deactivate/Kill/Rebalance.
	 */
	private void manageTopology() {
		// get stormConf
		StormConf stormConf = ((StormConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM));
		// stormBin path
		String stormBinPath = FileUtils
				.getSeparatorTerminatedPathEntry(stormConf.getComponentHome())
				+ "bin/";
		List<String> topologyNameList = (List<String>) this.parameterMap
				.get("topologyNames");
		String action = (String) this.parameterMap.get("action");
		for (String topoName : topologyNameList) {
			StringBuffer command = new StringBuffer();
			command.append(stormBinPath).append("storm").append(STR_SPACE)
					.append(action).append(STR_SPACE).append(topoName);
			manageTopology(dbCluster, command.toString());
		}

	}

	/**
	 * execute the command for manageTopology.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param command
	 *            the command
	 */
	private void manageTopology(Cluster cluster, String command) {
		// get stormConf
		StormConf stormConf = ((StormConf) cluster.getClusterConf()
				.getClusterComponents().get(Constant.Technology.STORM));
		String hostname = stormConf.getNimbus().getPublicIp();
		String username = stormConf.getUsername();
		String password = stormConf.getPassword();
		String privateKey = stormConf.getPrivateKey();
		SSHExec connection = null;
		try {
			// connect to nimbus node
			connection = SSHUtils.connectToNode(hostname, username, password,
					privateKey);
			// if connected
			if (connection != null) {
				boolean status = SSHUtils.action(connection, command);
			} else {
				addError("Could not connect to nimbus node..." + hostname);
			}
		} catch (Exception e) {
			e.printStackTrace();
			addError(e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
