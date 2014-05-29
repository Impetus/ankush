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
package com.impetus.ankush.elasticsearch;

import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
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
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.NewTileInfo;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileLine;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.AnkushRestClient;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.Parameter;

// TODO: Auto-generated Javadoc
/**
 * The Class ElasticSearchClusterMonitor.
 */
public class ElasticSearchClusterMonitor extends AbstractMonitor {

	/** The logger. */
	AnkushLogger logger = new AnkushLogger(ElasticSearchClusterMonitor.class);

	/** The Constant LOGS. */
	private static final String LOGS = "/logs/";

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);
	/** The list esNodes. */
	private List<NodeConf> esNodes;

	/** The KafkaConf. */
	private ElasticSearchConf esConf;

	/** The no data error string. */
	private String NO_DATA_ERROR_STRING = "Monitoring Data not found.";
	
	final String monitoringNodeString = "monitoringNode";

	// configuration manager for save/delete configuration.
	final ConfigurationManager confManager = new ConfigurationManager();

	private GenericManager<NodeMonitoring, Long> nodeMonitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	/**
	 * Details.
	 */
	private void details() {
		try {
			// cluster conf.
			ClusterConf clusterConf = dbCluster.getClusterConf();
			List<NodeConf> nodeConfs = clusterConf.getNodeConfs();

			// setting errors.
			Map<String, String> errors = clusterConf.getErrors();
			for (NodeConf nodeConf : nodeConfs) {
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
			setNodes(nodeConfs);
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodeConfs
	 *            the new nodes
	 */
	private void setNodes(List<NodeConf> nodeConfs) {
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
			for (NodeConf node : nodeConfs) {
				if (dbCluster.getState()
						.equals(Constant.Cluster.State.DEPLOYED)
						&& node.getStatus()) {
					node.setMessage("Deployment Completed.");
				} else {
					node.setMessage(hostMsgMap.get(node.getPublicIp()));
				}
				node.setType(node.getType());
				LinkedHashMap nodeMap = JsonMapperUtil.objectFromObject(node,
						LinkedHashMap.class);
				parameterMap.put(Constant.Keys.IP, node.getPublicIp());
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
	 * Cluster tiles.
	 */
	private void clustertiles() {
		List<Object> tiles = new ArrayList<Object>();
		// createing tile manager object.
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

		// Get ES specific cluster tiles
		List<Object> technologyTiles = getTechnologyTiles();
		// putting the tiles.
		tiles.addAll(technologyTiles);
		result.put(Constant.Keys.TILES, tiles);
	}

	/**
	 * Method to get nodes tile.
	 * 
	 * @return the nodes tile
	 */
	private List<TileInfo> getNodesTypeTiles() {

		// es conf object.
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);
		Integer es = conf.getNodes().size();

		// create elasticSearch tile
		TileInfo esTile = new TileInfo();
		esTile.setLine1(es.toString());
		esTile.setLine2(Constant.Role.ELASTICSEARCH);
		esTile.setStatus(Constant.Tile.Status.NORMAL);

		// list object.
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		tiles.add(esTile);

		return tiles;
	}

	/**
	 * Gets the adds the node tile.
	 * 
	 * @return the adds the node tile
	 */
	private TileInfo getAddNodeTile() {
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);

		try {
			if (conf != null && conf.getNewNodes() != null
					&& !conf.getNewNodes().isEmpty()) {
				List<NodeConf> nodes = conf.getNewNodes();

				String line1 = nodes.size()
						+ AbstractMonitor.getDisplayNameForNode(nodes.size())
						+ "Addition";
				String line2 = "is in progress";
				String line3 = null;
				String status = Constant.Tile.Status.NORMAL;
				String state = "progress";
				String errorLine = nodes.size() + Constant.STR_SPACE + "Nodes";
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
	 * Gets the removes the node tile.
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
				String line1 = count
						+ AbstractMonitor.getDisplayNameForNode(count)
						+ "Removal";
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

				nodeMap.put(Constant.Keys.SERVICESTATUS, getServiceStatus(node));
				if (dbNode.getPublicIp().equals(
						dbCluster.getClusterConf().getGangliaMaster()
								.getPublicIp())) {
					nodeMap.put("gangliaMaster", true);
				} else {
					nodeMap.put("gangliaMaster", false);
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
	 * Gets the service status.
	 * 
	 * @param node
	 *            the node
	 * @return the service status
	 */
	private boolean getServiceStatus(NodeConf node) {
		// creating monitoring manager object
		MonitoringManager manager = new MonitoringManager();
		// getting node monitoring data
		NodeMonitoring nodeMonitoring = manager.getMonitoringData(node.getId());

		// putting services status in map.
		boolean serviceStatus = true;
		if (nodeMonitoring != null && nodeMonitoring.getServiceStatus() != null) {
			for (Boolean status : nodeMonitoring.getServiceStatus().values()) {
				serviceStatus = serviceStatus && status;
			}
		} else {
			serviceStatus = false;
		}
		return serviceStatus;
	}

	/**
	 * Addingnodes.
	 */
	private void addingnodes() {
		ClusterConf clusterConf = dbCluster.getClusterConf();
		GenericConfiguration conf = clusterConf.getClusterComponents().get(
				Constant.Component.Name.ELASTICSEARCH);

		List<NodeConf> newNodes = conf.getNewNodes();

		if (newNodes == null || newNodes.isEmpty()) {
			return;
		}

		setNodes(newNodes);

		// getting the event state.
		String state = clusterConf.getState();
		for (NodeConf node : newNodes) {
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
	 * Method to get the map of role, ip list.
	 * 
	 * @return The Map of role and ip list.
	 */
	private Map techlogs() {

		// Getting es conf.
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);

		// esnode ip list.
		List<String> es = new ArrayList<String>();

		// filling ElasticSearch nodes.
		for (NodeConf nodeConf : conf.getNodes()) {
			es.add(nodeConf.getPublicIp());
		}

		result.put(Constant.Role.ELASTICSEARCH, es);
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
		String errMsg = "Exception: Unable to process request for log files.";

		// Getting es conf.
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);

		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);
		String type = (String) parameterMap.get(Constant.Keys.TYPE);

		LogViewHandler logHandler = new LogViewHandler(nodeIp,
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey());

		// the log directory of es.
		String logDirectory = conf.getComponentHome() + "/logs";
		// Adding HTML break tag
		// To be changed when Error Div on UI will support \n character
		errMsg +=  "<br>NodeIP-" + nodeIp + "<br>Type-" + type;

		// get the list of all .log files
		List<String> files = logHandler.listLogDirectory(logDirectory);
		if(files.size() == 0 || files == null || files.isEmpty()){
			errors.add(errMsg);
		}

		// puting the files in result Map.
		result.put(Constant.Keys.FILES, files);
	}

	/**
	 * Method to view the content of the file.
	 * 
	 * @return The content of file.
	 */
	private Map view() {
		String errMsg = "Exception: Unable to process request to view log file.";

		// Getting es conf.
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);

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
		
		// Adding HTML break tag
		// To be changed when Error Div on UI will support \n character
		errMsg +=  "<br>NodeIP-" + ip + "<br>Type-" + fileName;
		// logFile path
		String filePath = conf.getComponentHome() + LOGS + fileName;

		// putting the file content with total read characters.
		Map<String, String> content;
		try {
			content = logHandler
					.getFileContent(filePath, readCount, bytesCount);
			result.putAll(content);
		} catch (Exception e) {
			// Adding error
			addAndLogError(errMsg, e);
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
		String errMsg = "Exception: Unable to process request to view log file.";

		// Getting es conf.
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);

		// Getting the ip address from parameter map.
		String ip = (String) parameterMap.get(Constant.Keys.IP);

		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);

		// Create the log View Handler object.
		LogViewHandler logHandler = new LogViewHandler(ip, conf.getUsername(),
				conf.getPassword(), conf.getPrivateKey());
		// Adding HTML break tag
		// To be changed when Error Div on UI will support \n character
		errMsg +=  "<br>NodeIP-" + ip + "<br>Type-" + fileName;
		
		// logFile path
		String filePath = conf.getComponentHome() + LOGS + fileName;

		// setting download path.
		String downloadPath = "";

		try {
			downloadPath = logHandler.downloadFile(conf.getClusterName(),
					filePath);
			result.put(Constant.Keys.DOWNLOADPATH, downloadPath);
		} catch (Exception e) {
			// Adding error
			addAndLogError(errMsg, e);
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

		Map resultInfo = new HashMap();
		Map params = new HashMap();

		// Getting ES conf.
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);
		this.esNodes = conf.getNodes();
		this.esConf = conf;
		/** ES component home path. **/
		String homePath = conf.getComponentHome();
		/** yaml file path **/
		String esYamlFilePath = homePath
				+ ElasticSearchWorker.ELASTICSEARCH_YAML_PATH;

		List<String> confFilesPath = new ArrayList<String>();
		confFilesPath.add(esYamlFilePath);

		for (String filePath : confFilesPath) {
			/** yaml content **/
			String fileContent = SSHUtils.getFileContents(filePath, conf
					.getNodes().get(0).getPublicIp(), conf.getUsername(), conf
					.getClusterConf().isAuthTypePassword() ? conf.getPassword()
					: conf.getPrivateKey(), conf.getClusterConf()
					.isAuthTypePassword());

			List<String> nonEditableParameters = new ArrayList<String>();
			boolean edit;
			Map map = null;
			Map newMap = null;
			/** create yaml object. **/
			Yaml yaml = new Yaml();
			List<String> fileList = new ArrayList<String>(
					Arrays.asList(filePath.split("/")));
			if (filePath.equals(esYamlFilePath)) {

				List<String> nonEditableParamsList = new ArrayList<String>();
				nonEditableParamsList
						.add(ElasticSearchConf.ESConfParams.CLUSTER_NAME);
				nonEditableParamsList
						.add(ElasticSearchConf.ESConfParams.NODE_NAME);
				nonEditableParamsList
						.add(ElasticSearchConf.ESConfParams.NODE_TAG);
				/** create map object by loading from yaml object **/
				map = (Map) yaml.load(fileContent);
				setEditableStatus(map, nonEditableParamsList);
				resultInfo.put(fileList.get(fileList.size() - 1), map);
			}
		}
		params.put("params", resultInfo);
		result.putAll(params);
	}

	/**
	 * Sets the editable status.
	 * 
	 * @param map
	 *            the map
	 * @param nonEditableParams
	 *            the non editable params
	 */
	private void setEditableStatus(Map<String, Object> map,
			List<String> nonEditableParams) {
		boolean edit;
		for (String key : map.keySet()) {
			edit = true;
			List<Object> list = new ArrayList<Object>();
			list.add(map.get(key));
			if ((nonEditableParams != null) && (nonEditableParams.size() != 0)
					&& (nonEditableParams.contains(key))) {
				edit = false;
			}
			list.add(edit);
			map.put(key, list);
		}
	}

	/**
	 * Editparams.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void editparams() throws Exception {
		// check whether Agent is down
		boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
		if (isAgentDown) {
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return;
		}
		// Getting kafka conf.
		ElasticSearchConf conf = (ElasticSearchConf) dbCluster.getClusterConf()
				.getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);

		// confParams Map containg file name as key and list of params as Object
		// to be edited.
		Map<String, Object> confParams = (Map<String, Object>) parameterMap
				.get("params");
		String loggedUser = (String) parameterMap.get("loggedUser");
		this.esNodes = conf.getNodes();
		this.esConf = conf;

		String componentHome = conf.getComponentHome();

		// iterate over confParams Map.
		for (Entry entry : confParams.entrySet()) {

			// get fileName
			String fileName = (String) entry.getKey();

			// get config params list
			List<Map> params = (List<Map>) entry.getValue();

			// iterate on each param
			for (Map param : params) {
				Parameter parameter = JsonMapperUtil.objectFromMap(param,
						Parameter.class);

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
					deleteConfigFileParam(parameter, fileName, loggedUser);
				}
			}
		}
	}

	/**
	 * Adds the config file param.
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
		List<NodeConf> esNodes = this.esNodes;
		try {
			final Semaphore semaphore = new Semaphore(esNodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : esNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String propertyValue = parameter.getValue();

							// get component homepath
							String componentHomePath = esConf
									.getComponentHome();

							// get server.properties file path
							String propertyFilePath = componentHomePath
									+ "/config/" + fileName;

							String username = esConf.getUsername();
							String password = esConf.getPassword();
							String privateKey = esConf.getPrivateKey();

							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask add = new AddConfProperty(
											propertyName, propertyValue,
											propertyFilePath,
											Constant.File_Extension.YAML);
									res = connection.exec(add);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												esConf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, propertyValue);
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
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(esNodes.size());
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
	 * Edits the config file param.
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
		List<NodeConf> esNodes = this.esNodes;
		try {
			final Semaphore semaphore = new Semaphore(esNodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : esNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String propertyValue = parameter.getValue();

							// get component homepath
							String componentHomePath = esConf
									.getComponentHome();

							// get server.properties file path
							String propertyFilePath = componentHomePath
									+ "/config/" + fileName;

							String username = esConf.getUsername();
							String password = esConf.getPassword();
							String privateKey = esConf.getPrivateKey();

							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask add = new EditConfProperty(
											propertyName, propertyValue,
											propertyFilePath,
											Constant.File_Extension.YAML);
									res = connection.exec(add);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												esConf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, propertyValue);
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
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(esNodes.size());
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
	 * Delete config file param.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void deleteConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser) {
		List<NodeConf> esNodes = this.esNodes;
		try {
			final Semaphore semaphore = new Semaphore(esNodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : esNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							String propertyName = parameter.getName();

							// get component homepath
							String componentHomePath = esConf
									.getComponentHome();

							// get server.properties file path
							String propertyFilePath = componentHomePath
									+ "/config/" + fileName;

							String username = esConf.getUsername();
							String password = esConf.getPassword();
							String privateKey = esConf.getPrivateKey();

							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask add = new DeleteConfProperty(
											propertyName, propertyFilePath,
											Constant.File_Extension.YAML);
									res = connection.exec(add);
									// removing records from the configuration
									// table.
									confManager.removeOldConfiguration(
											esConf.getClusterDbId(), hostName,
											fileName, propertyName);
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
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(esNodes.size());
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
	 * Technology data.
	 * 
	 * @return the list
	 */

	// public void technologyTiles() {
	private List<Object> getTechnologyTiles() {

		List<Object> tiles = new ArrayList<Object>();

		// ElasticSearchMonitoringData esMonitoringData = getESMonitoringData();

		NewTileInfo tileInfo = null;
		Map<String, Object> data = new HashMap<String, Object>();
		// if (esMonitoringData == null) {
		// tiles.add(getErrorTile());
		// return tiles;
		// }

		Map map = getESMonitoringData();
		ElasticSearchMonitoringData esMonitoringData = (ElasticSearchMonitoringData) map
				.get(Constant.Keys.OUTPUT);
		if ((map.get(Constant.Keys.ERROR) != null)
				|| (esMonitoringData == null)) {
			String monitoringNodeIp = (String)map.get(monitoringNodeString);
			tiles.add(getErrorTile(monitoringNodeIp));
			return tiles;
		}
		Map clusterHealth = esMonitoringData.getClusterHealth();
		List<TileLine> tileLineList = new ArrayList<TileLine>();
		List<String> lineList = new ArrayList<String>();
		String line;
		// get Master_Node
		line = getMasterNode(esMonitoringData);
		data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.LINE1, line);
		data.put(Constant.Tile.Data.LINE2, "MasterNode");
		tileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(tileInfo);
		// active_primary_shards line
		line = clusterHealth.get(
				Constant.ElasticSearch.Cluster_Health.ACTIVE_PRIMARY_SHARDS)
				.toString();
		lineList.add("active_primary");
		lineList.add(line);
		TileLine tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);
		/* Start of Shard_type line */
		// initializing_ shards line
		line = clusterHealth.get(
				Constant.ElasticSearch.Cluster_Health.INITIALIZING_SHARDS)
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("initializing");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// relocating_shards line
		line = clusterHealth.get(
				Constant.ElasticSearch.Cluster_Health.RELOCATING_SHARDS)
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("relocating");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);
		// unassigned_shards line
		line = clusterHealth.get(
				Constant.ElasticSearch.Cluster_Health.UNASSIGNED_SHARDS)
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("unassigned");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.TILE_LINES, tileLineList);
		data.put(Constant.Tile.Data.Header, "Shards");
		tileInfo = new NewTileInfo(Constant.Tile.Type.BIG_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(tileInfo);
		/* End of Shard_type tile */

		// No of Nodes line
		line = clusterHealth.get(
				Constant.ElasticSearch.Cluster_Health.NUMBER_OF_NODES)
				.toString();
		data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.LINE1, line);
		data.put(Constant.Tile.Data.LINE2, "Node");
		tileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(tileInfo);
		// No of Data_node line
		line = clusterHealth.get(
				Constant.ElasticSearch.Cluster_Health.NUMBER_OF_DATA_NODES)
				.toString();
		data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.LINE1, line);
		data.put(Constant.Tile.Data.LINE2, "DataNode");
		tileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(tileInfo);

		// cluster status tile
		line = clusterHealth.get(Constant.ElasticSearch.Cluster_Health.STATUS)
				.toString();
		data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.LINE1, line);
		data.put(Constant.Tile.Data.LINE2, "Status");
		tileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(tileInfo);
		// cluster timed_out tile
		line = clusterHealth.get(
				Constant.ElasticSearch.Cluster_Health.TIMED_OUT).toString();
		data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.LINE1, line);
		data.put(Constant.Tile.Data.LINE2, "TimedOut");
		tileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(tileInfo);

		// get Stats tiles
		tiles.addAll(getStatsTiles(esMonitoringData));
		// get Cluster_Stats tiles
		tiles.addAll(getClusterStatsTiles(esMonitoringData));

		return tiles;
	}

	public void technologyTiles() {

		List<Object> technologyTiles = getTechnologyTiles();
		List<TileInfo> serviceTiles = getServiceTiles(Constant.Technology.ELASTICSEARCH);
		if (serviceTiles != null && !serviceTiles.isEmpty()) {
			technologyTiles.addAll(serviceTiles);
		}
		result.put("tiles", technologyTiles);
	}

	/**
	 * Gets the error tile.
	 * 
	 * @return the error tile
	 */
//	private TileInfo getErrorTile() {
//		TileInfo tileInfo = new TileInfo("Unable to fetch", "monitoring data",
//				null, null, Constant.Tile.Status.CRITICAL, null);
//		return tileInfo;
//	}
	
	private TileInfo getErrorTile(String monitoringNodeIp) {
		TileInfo tileInfo = new TileInfo(monitoringNodeIp, "ES Monitoring unavailable",
				null, null, Constant.Tile.Status.CRITICAL, null);
		return tileInfo;
	}

	/**
	 * Gets the eS monitoring data.
	 * 
	 * @return the eS monitoring data
	 */
	/*
	 * private ElasticSearchMonitoringData getESMonitoringData() {
	 * MonitoringManager monitoringManeger = new MonitoringManager();
	 * ElasticSearchMonitoringData esMonitoringData = null; try {
	 * esMonitoringData = new ElasticSearchMonitoringData(); esMonitoringData =
	 * (ElasticSearchMonitoringData) monitoringManeger
	 * .getTechnologyData(this.dbCluster.getClusterConf()
	 * .getGangliaMaster().getPublicIp(), Constant.Technology.ELASTICSEARCH); }
	 * catch (Exception e) { logger.error(e.getMessage()); } return
	 * esMonitoringData; }
	 */

	/**
	 * Gets the master node.
	 * 
	 * @param esMonitoringData
	 *            the es monitoring data
	 * @return the master node
	 */
	private String getMasterNode(ElasticSearchMonitoringData esMonitoringData) {
		Map<String, Object> clusterState = (Map<String, Object>) esMonitoringData
				.getClusterState();
		String masterNode = (String) clusterState
				.get(Constant.ElasticSearch.Cluster_State.MASTER_NODE);
		Map<String, Object> nodes = (Map<String, Object>) clusterState
				.get(Constant.ElasticSearch.Cluster_State.NODES);
		for (String key : nodes.keySet()) {
			if (key.equals(masterNode)) {
				Map<String, Object> node = (Map<String, Object>) nodes.get(key);
				masterNode = (String) node.get("name");
			}
		}
		return masterNode;
	}

	/**
	 * Gets the stats tiles.
	 * 
	 * @param esMonitoringData
	 *            the es monitoring data
	 * @return the stats tiles
	 */
	private List<NewTileInfo> getStatsTiles(
			ElasticSearchMonitoringData esMonitoringData) {
		Map<String, Object> stats = (Map<String, Object>) esMonitoringData
				.getStats();
		Map<String, Object> shards = (Map<String, Object>) stats.get("_shards");
		String line = shards.get("total").toString();
		/*
		 * Map<String, Object> store = (Map<String, Object>)
		 * shards.get("store"); String size = store.get("size").toString();
		 */
		List<NewTileInfo> tiles = new ArrayList<NewTileInfo>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.LINE1, line);
		data.put(Constant.Tile.Data.LINE2, "Total Shards");
		NewTileInfo newTileInfo = new NewTileInfo(
				Constant.Tile.Type.SMALL_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(newTileInfo);

		line = shards.get("successful").toString();
		data = new HashMap<String, Object>();
		data.put(Constant.Tile.Data.LINE1, line);
		data.put(Constant.Tile.Data.LINE2, "Successful Shards");
		newTileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
				Constant.Tile.Status.NORMAL, data);
		tiles.add(newTileInfo);

		return tiles;
	}

	/**
	 * Gets the cluster stats tiles.
	 * 
	 * @param esMonitoringData
	 *            the es monitoring data
	 * @return the cluster stats tiles
	 */
	private List<NewTileInfo> getClusterStatsTiles(
			ElasticSearchMonitoringData esMonitoringData) {
		Map<String, Object> clusterStats = (Map<String, Object>) esMonitoringData
				.getClusterStats();
		List<NewTileInfo> tiles = new ArrayList<NewTileInfo>();
		if(clusterStats != null){
			Map<String, Object> indices = (Map<String, Object>) clusterStats
					.get("indices");
			Map<String, Object> store = (Map<String, Object>) indices.get("store");
			String size = store.get("size").toString();
			Map<String, Object> data = new HashMap<String, Object>();
			data = new HashMap<String, Object>();
			data.put(Constant.Tile.Data.LINE1, size);
			data.put(Constant.Tile.Data.LINE2, "Total Size");
			NewTileInfo newTileInfo = new NewTileInfo(
					Constant.Tile.Type.SMALL_TEXT, null,
					Constant.Tile.Status.NORMAL, data);
			tiles.add(newTileInfo);
	
			String indicesCount = indices.get("count").toString();
			data = new HashMap<String, Object>();
			data.put(Constant.Tile.Data.LINE1, indicesCount);
			data.put(Constant.Tile.Data.LINE2, "Indices");
			newTileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
					Constant.Tile.Status.NORMAL, data);
			tiles.add(newTileInfo);
	
			String docsCount = ((Map<String, Object>) indices.get("docs")).get(
					"count").toString();
			data = new HashMap<String, Object>();
			data.put(Constant.Tile.Data.LINE1, docsCount);
			data.put(Constant.Tile.Data.LINE2, "Total Documents");
			newTileInfo = new NewTileInfo(Constant.Tile.Type.SMALL_TEXT, null,
					Constant.Tile.Status.NORMAL, data);
			tiles.add(newTileInfo);
		}
		return tiles;
	}

	/**
	 * Gets the node jvm tile.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the node jvm tile
	 */
	private NewTileInfo getNodeJVMTile(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		List<TileLine> tileLineList = new ArrayList<TileLine>();
		Map<String, Object> data = new HashMap<String, Object>();
		List<String> lineList = new ArrayList<String>();
		TileLine tileLine;
		String line;
		Map<String, Object> nodeStatsJvm = (Map<String, Object>) nodesStats
				.get("jvm");
		Map<String, Object> nodeInfoJvm = (Map<String, Object>) nodesInfo
				.get("jvm");
		// heap_used line
		line = ((Map<String, Object>) nodeStatsJvm.get("mem")).get("heap_used")
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("Heap Used");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// heap_committed line
		line = ((Map<String, Object>) nodeStatsJvm.get("mem")).get(
				"heap_committed").toString();
		lineList = new ArrayList<String>();
		lineList.add("Heap Committed");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// non_heap_used line
		line = ((Map<String, Object>) nodeStatsJvm.get("mem")).get(
				"non_heap_used").toString();
		lineList = new ArrayList<String>();
		lineList.add("Non Heap Used");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// non_heap_committed line
		line = ((Map<String, Object>) nodeStatsJvm.get("mem")).get(
				"non_heap_committed").toString();
		lineList = new ArrayList<String>();
		lineList.add("Non Heap Committed");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// threads count/peak_count line
		line = ((Map<String, Object>) nodeStatsJvm.get("threads")).get("count")
				.toString()
				+ "/"
				+ ((Map<String, Object>) nodeStatsJvm.get("threads")).get(
						"peak_count").toString();
		lineList = new ArrayList<String>();
		lineList.add("Thread Count/Peak");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// gc collection_count
		line = ((Map<String, Object>) nodeStatsJvm.get("gc")).get(
				"collection_count").toString();
		lineList = new ArrayList<String>();
		lineList.add("GC Count");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// java version
		line = nodeInfoJvm.get("version").toString();
		lineList = new ArrayList<String>();
		lineList.add("Java Version");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// jvm vendor
		line = nodeInfoJvm.get("vm_vendor").toString();
		lineList = new ArrayList<String>();
		lineList.add("JVM Vendor");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		// jvm
		line = nodeInfoJvm.get("vm_name").toString();
		lineList = new ArrayList<String>();
		lineList.add("JVM");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);
		data.put(Constant.Tile.Data.TILE_LINES, tileLineList);
		NewTileInfo tileInfo = new NewTileInfo(Constant.Tile.Type.BIG_TEXT,
				null, Constant.Tile.Status.NORMAL, data);
		return tileInfo;
	}

	/**
	 * Gets the node indices tile.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the node indices tile
	 */
	private NewTileInfo getNodeIndicesTile(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		List<TileLine> tileLineList = new ArrayList<TileLine>();
		Map<String, Object> data = new HashMap<String, Object>();
		List<String> lineList = new ArrayList<String>();
		TileLine tileLine;
		String line;
		Map<String, Object> nodeIndices = (Map<String, Object>) nodesStats
				.get("indices");
		// docs count line
		line = ((Map<String, Object>) nodeIndices.get("docs")).get("count")
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("Documents");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("docs")).get("deleted")
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("Documents Deleted");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("store")).get("size")
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("Store Size");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("indexing")).get(
				"index_total").toString();
		lineList = new ArrayList<String>();
		lineList.add("Index Req Total");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("indexing")).get(
				"delete_total").toString();
		lineList = new ArrayList<String>();
		lineList.add("Delete Req Total");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("get")).get("total")
				.toString();
		lineList = new ArrayList<String>();
		lineList.add("Get Req Total");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("get")).get(
				"exists_total").toString();
		lineList = new ArrayList<String>();
		lineList.add("Get(Exists) Total");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("get")).get(
				"missing_total").toString();
		lineList = new ArrayList<String>();
		lineList.add("Get(Missing) Total");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("search")).get(
				"query_total").toString();
		lineList = new ArrayList<String>();
		lineList.add("Query Total");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		line = ((Map<String, Object>) nodeIndices.get("search")).get(
				"fetch_total").toString();
		lineList = new ArrayList<String>();
		lineList.add("Fetch Total");
		lineList.add(line);
		tileLine = new TileLine(lineList, null, null);
		tileLineList.add(tileLine);

		NewTileInfo tileInfo = new NewTileInfo(Constant.Tile.Type.BIG_TEXT,
				null, Constant.Tile.Status.NORMAL, data);
		return tileInfo;
	}

	/**
	 * Gets the node os tile.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the node os tile
	 */
	private void getNodeOsTile(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {

	}

	/**
	 * Gets the node process tile.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the node process tile
	 */
	private void getNodeProcessTile(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {

	}

	/**
	 * Gets the node thread pool tile.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the node thread pool tile
	 */
	private void getNodeThreadPoolTile(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {

	}

	/**
	 * Gets the node network tile.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the node network tile
	 */
	private void getNodeNetworkTile(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {

	}

	/**
	 * Gets the node fs tile.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the node fs tile
	 */
	private void getNodeFsTile(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {

	}

	/**
	 * Monitoring data. This method is just to conform that monitoring data from
	 * Agent is retrived properly and it return that monitoing data.
	 */
	public void monitoringData() {
		MonitoringManager monitoringManeger = new MonitoringManager();
		String publicIp = this.dbCluster.getClusterConf().getGangliaMaster()
				.getPublicIp();
		ElasticSearchMonitoringData esMonitoringData = (ElasticSearchMonitoringData) monitoringManeger
				.getTechnologyData(publicIp, Constant.Technology.ELASTICSEARCH);
		result.put("monitoringData", esMonitoringData);
	}

	/**
	 * Gets the eS monitoring data.
	 *
	 * @return the eS monitoring data
	 */
	private Map getESMonitoringData() {
		MonitoringManager monitoringManeger = new MonitoringManager();
		ElasticSearchMonitoringData esMonitoringData = null;
		NodeConf monitoringNode = this.dbCluster.getClusterConf()
				.getGangliaMaster();
		String serviceName = "";
		// String errorMsg = "Monitoring data cannot be fetched as"
		// + Constant.STR_SPACE + serviceName + "is down on Node:"
		// + Constant.STR_SPACE + monitoringNode.getPublicIp();
		String errorMsg = "Monitoring data cannot be fetched as"
				+ Constant.STR_SPACE;

		esMonitoringData = (ElasticSearchMonitoringData) monitoringManeger
				.getTechnologyData(monitoringNode.getPublicIp(),
						Constant.Technology.ELASTICSEARCH);

		Map resultMap = new HashMap();
		// check whether agent is down
		if (isAgentDown(monitoringNode)) {
			serviceName = Constant.RoleProcessName
					.getProcessName(Constant.Role.AGENT);
			errorMsg += serviceName + Constant.STR_SPACE + "is down on Node:" + Constant.STR_SPACE
					+ monitoringNode.getPublicIp();
			resultMap.put(Constant.Keys.ERROR, errorMsg);
		}
		// check whether node is down
		else if (isNodeDown(monitoringNode.getPublicIp())) {
			// errorMsg = noDataAvailableString +
			// "ElasticSearch is down on node-"
			// + Constant.STR_SPACE + monitoringNode.getPublicIp();
			serviceName = Constant.RoleProcessName
					.getProcessName(Constant.Role.ELASTICSEARCH);
			errorMsg += serviceName + Constant.STR_SPACE + "is down on Node:" + Constant.STR_SPACE
					+ monitoringNode.getPublicIp();
			resultMap.put(Constant.Keys.ERROR, errorMsg);
		}else if(esMonitoringData == null){
			errorMsg = "Unavailable monitoring data";
			resultMap.put(Constant.Keys.ERROR, errorMsg);
		}
		resultMap.put(monitoringNodeString, monitoringNode.getPublicIp());
		resultMap.put(Constant.Keys.OUTPUT, esMonitoringData);
		return resultMap;
	}

	/**
	 * Checks if is agent down.
	 *
	 * @param monitoringNode the monitoring node
	 * @return true, if is agent down
	 */
	private boolean isAgentDown(NodeConf monitoringNode) {
		MonitoringManager monitoringManeger = new MonitoringManager();
		boolean status = false;
		NodeMonitoring nodeMonitoring = monitoringManeger
				.getMonitoringData(monitoringNode.getPublicIp());
		if (nodeMonitoring != null) {
			// check whether agent is down
			status = nodeMonitoring.isAgentDown();
		}
		return status;
	}

	/**
	 * Checks if is node down.
	 *
	 * @param monitoringNodeIp the monitoring node ip
	 * @return true, if is node down
	 */
	private boolean isNodeDown(	String monitoringNodeIp) {
		MonitoringManager monitoringManeger = new MonitoringManager();
		boolean status = false;
		NodeMonitoring nodeMonitoring = monitoringManeger
				.getMonitoringData(monitoringNodeIp);
		if (nodeMonitoring != null) {
			Map<String, Boolean> serviceStatusMap = nodeMonitoring
					.getServiceStatus();
			if (serviceStatusMap != null
					&& (serviceStatusMap.get(Constant.Role.AGENT))) {
				if (!serviceStatusMap.get(Constant.Process.ELASTICSEARCH)) {
					status = true;
				}
			}
		}
		return status;
	}

	/**
	 * Nodeinformation.
	 */
	public void nodeinformation() {
		Map<String, Object> nodeInformationMap = new HashMap<String, Object>();
		String errorMsg = "Monitoring data cannot be fetched as"
				+ Constant.STR_SPACE + "ElasticSearch is down on node:"+Constant.STR_SPACE;		
		try {
			Map map = getESMonitoringData();
			String nodeIP = (String) parameterMap.get(Constant.Keys.IP);
			boolean isNodeDownStatus = isNodeDown(nodeIP); 
			logger.info("isNodeDownStatus for ES on node :"+ nodeIP + " is : "+isNodeDownStatus);
			if(isNodeDownStatus){
				errorMsg+=nodeIP;
				errors.add(errorMsg);
				return;
			}
			if (map.get(Constant.Keys.ERROR) != null) {
				errors.add(map.get(Constant.Keys.ERROR).toString());
			} else {
				ElasticSearchMonitoringData esMonitoringData = (ElasticSearchMonitoringData) map
						.get(Constant.Keys.OUTPUT);
				// }
				// esMonitoringData = getESMonitoringData();
				// if (esMonitoringData == null) {
				// // add Error tile to nodeTiles
				// errors.add("No Information found for the node");
				// } else {
			
				String ankushHostName = HostOperation.getAnkushHostName(nodeIP);
				Map nodesStats = esMonitoringData.getNodesStats();
				Map nodesInfo = esMonitoringData.getNodesInfo();
				Map<String, Object> nodesFromNodesStats = (Map<String, Object>) nodesStats
						.get("nodes");
				Map<String, Object> nodesFromNodesInfo = (Map<String, Object>) nodesInfo
						.get("nodes");
				Map<String, Object> matchingNodeStats = new HashMap<String, Object>();
				Map<String, Object> matchingNodeInfo = new HashMap<String, Object>();
				boolean nodeFound = false;
				String nodeName = null;
				String nodeId = null;
				for (Entry<String, Object> node : nodesFromNodesStats
						.entrySet()) {
					nodeId = node.getKey();
					matchingNodeStats = (Map<String, Object>) node.getValue();
					nodeName = (String) matchingNodeStats.get("name");
					if (nodeName.equals(ankushHostName)) {
						nodeFound = true;
						break;
					}
				}
				for (Entry<String, Object> node : nodesFromNodesInfo.entrySet()) {
					matchingNodeInfo = (Map<String, Object>) node.getValue();
					nodeName = (String) matchingNodeInfo.get("name");
					if (nodeName.equals(ankushHostName)) {
						nodeFound = true;
						break;
					}
				}
				logger.info("Is the "+ nodeIP +"in ES monitoring data provided by Agent found : "+nodeFound);
				if (nodeFound) {
					nodeInformationMap.put("jvm",
							getJvmInfo(matchingNodeStats, matchingNodeInfo));
					nodeInformationMap
							.put("indices",
									getIndicesInfo(matchingNodeStats,
											matchingNodeInfo));
					nodeInformationMap.put("os",
							getOsInfo(matchingNodeStats, matchingNodeInfo));
					nodeInformationMap
							.put("process",
									getProcessInfo(matchingNodeStats,
											matchingNodeInfo));
					nodeInformationMap.put(
							"threadPool",
							getThreadPoolInfo(matchingNodeStats,
									matchingNodeInfo));
					nodeInformationMap
							.put("network",
									getNetworkInfo(matchingNodeStats,
											matchingNodeInfo));
					nodeInformationMap.put("fs",
							getFsInfo(matchingNodeStats, matchingNodeInfo));
				}else{
					errorMsg= "Monitoring data cannot be fetched for node:"
							+ Constant.STR_SPACE + nodeIP + Constant.STR_SPACE + ".Please restart the ElasticSearch service on this node.";		
					errors.add(errorMsg);
				}
			}
		} catch (Exception e) {
			addAndLogError(errorMsg, e);
			return;
		}
		result.put("output", nodeInformationMap);
	}

	/**
	 * Gets the jvm info.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the jvm info
	 */
	private Map<String, Object> getJvmInfo(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		Map<String, Object> jvmInfoMap = new HashMap<String, Object>();
		Object value;
		Map<String, Object> nodeStatsJvm = (Map<String, Object>) nodesStats
				.get("jvm");
		Map<String, Object> nodeInfoJvm = (Map<String, Object>) nodesInfo
				.get("jvm");
		// heap_used line
		value = ((Map<String, Object>) nodeStatsJvm.get("mem"))
				.get("heap_used");
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.HEAP_USED, value);
		// heap_committed line
		value = ((Map<String, Object>) nodeStatsJvm.get("mem"))
				.get("heap_committed");
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.HEAP_COMMITTED, value);
		// non_heap_used line
		value = ((Map<String, Object>) nodeStatsJvm.get("mem"))
				.get("non_heap_used");
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.NON_HEAP_USED, value);
		// non_heap_committed line
		value = ((Map<String, Object>) nodeStatsJvm.get("mem"))
				.get("non_heap_committed");
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.NON_HEAP_COMMITTED,
				value);
		// threads count/peak_count line
		value = ((Map<String, Object>) nodeStatsJvm.get("threads"))
				.get("count")
				+ "/"
				+ ((Map<String, Object>) nodeStatsJvm.get("threads"))
						.get("peak_count");
		jvmInfoMap
				.put(Constant.ElasticSearch.Keys.JVM.THREAD_COUNT_PEAK, value);
		// gc collection_count
		value = ((Map<String, Object>) nodeStatsJvm.get("gc"))
				.get("collection_count");
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.GC_COUNT, value);
		// java version
		value = nodeInfoJvm.get("version").toString();
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.JAVA_VERSION, value);
		// jvm vendor
		value = nodeInfoJvm.get("vm_vendor").toString();
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.JVM_VENDOR, value);
		// jvm
		value = nodeInfoJvm.get("vm_name").toString();
		jvmInfoMap.put(Constant.ElasticSearch.Keys.JVM.JVM, value);
		return jvmInfoMap;
	}

	/**
	 * Gets the indices info.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the indices info
	 */
	private Map<String, Object> getIndicesInfo(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		Map<String, Object> indicesInfoMap = new HashMap<String, Object>();
		Object value;
		Map<String, Object> nodeStatsIndices = (Map<String, Object>) nodesStats
				.get("indices");

		//
		value = ((Map<String, Object>) nodeStatsIndices.get("docs"))
				.get("count");
		indicesInfoMap
				.put(Constant.ElasticSearch.Keys.Indices.DOCUMENTS, value);
		value = ((Map<String, Object>) nodeStatsIndices.get("docs"))
				.get("deleted");
		indicesInfoMap.put(
				Constant.ElasticSearch.Keys.Indices.DOCUMENTS_DELETED, value);
		value = ((Map<String, Object>) nodeStatsIndices.get("store"))
				.get("size");
		indicesInfoMap.put(Constant.ElasticSearch.Keys.Indices.STORE_SIZE,
				value);
		value = ((Map<String, Object>) nodeStatsIndices.get("indexing"))
				.get("index_total");
		indicesInfoMap.put(Constant.ElasticSearch.Keys.Indices.INDEX_REQ_TOTAL,
				value);
		value = ((Map<String, Object>) nodeStatsIndices.get("indexing"))
				.get("delete_total");
		indicesInfoMap.put(
				Constant.ElasticSearch.Keys.Indices.DELETE_REQ_TOTAL, value);
		value = ((Map<String, Object>) nodeStatsIndices.get("get"))
				.get("total");
		indicesInfoMap.put(Constant.ElasticSearch.Keys.Indices.GET_REQ_TOTAL,
				value);
		value = ((Map<String, Object>) nodeStatsIndices.get("get"))
				.get("exists_total");
		indicesInfoMap.put(
				Constant.ElasticSearch.Keys.Indices.GET_EXISTS_TOTAL, value);
		value = ((Map<String, Object>) nodeStatsIndices.get("get"))
				.get("missing_total");
		indicesInfoMap.put(
				Constant.ElasticSearch.Keys.Indices.GET_MISSING_TOTAL, value);
		value = ((Map<String, Object>) nodeStatsIndices.get("search"))
				.get("query_total");
		indicesInfoMap.put(Constant.ElasticSearch.Keys.Indices.QUERT_TOTAL,
				value);
		value = ((Map<String, Object>) nodeStatsIndices.get("search"))
				.get("fetch_total");
		indicesInfoMap.put(Constant.ElasticSearch.Keys.Indices.FETCH_TOTAL,
				value);
		return indicesInfoMap;
	}

	/**
	 * Gets the os info.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the os info
	 */
	private Map<String, Object> getOsInfo(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		Map<String, Object> osInfoMap = new HashMap<String, Object>();
		Object value;
		Map<String, Object> nodeStatsOs = (Map<String, Object>) nodesStats
				.get("os");
		Map<String, Object> nodeInfoOs = (Map<String, Object>) nodesInfo
				.get("os");
		//
		value = ((Map<String, Object>) nodeInfoOs.get("mem")).get("total");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.TOTAL_MEMORY, value);
		value = ((Map<String, Object>) nodeInfoOs.get("swap")).get("total");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.TOTAL_SWAP, value);
		value = ((Map<String, Object>) nodeStatsOs.get("mem")).get("used")
				+ "/"
				+ ((Map<String, Object>) nodeStatsOs.get("mem")).get("free");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.MEMORY_USED_FREE, value);
		value = ((Map<String, Object>) nodeStatsOs.get("swap")).get("used")
				+ "/"
				+ ((Map<String, Object>) nodeStatsOs.get("swap")).get("free");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.SWAP_USED_FREE, value);
		value = ((Map<String, Object>) nodeStatsOs.get("cpu")).get("user")
				+ "/"
				+ ((Map<String, Object>) nodeStatsOs.get("cpu")).get("sys");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.CPU_USER_SYS, value);
		value = ((Map<String, Object>) nodeStatsOs.get("cpu")).get("idle");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.CPU_IDLE, value);
		value = ((Map<String, Object>) nodeInfoOs.get("cpu")).get("vendor");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.CPU_VENDOR, value);
		value = ((Map<String, Object>) nodeInfoOs.get("cpu")).get("model");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.CPU_MODEL, value);
		value = ((Map<String, Object>) nodeInfoOs.get("cpu"))
				.get("total_cores");
		osInfoMap.put(Constant.ElasticSearch.Keys.OS.TOTAL_CORES, value);
		return osInfoMap;
	}

	/**
	 * Gets the process info.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the process info
	 */
	private Map<String, Object> getProcessInfo(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		Map<String, Object> processInfoMap = new HashMap<String, Object>();
		Object value;
		Map<String, Object> nodeStatsProcess = (Map<String, Object>) nodesStats
				.get("process");
		Map<String, Object> nodeInfoProcess = (Map<String, Object>) nodesInfo
				.get("process");
		//
		value = nodeStatsProcess.get("open_file_descriptors");
		processInfoMap.put(
				Constant.ElasticSearch.Keys.Process.OPEN_FILE_DESCRIPTORS,
				value);
		value = ((Map<String, Object>) nodeStatsProcess.get("mem"))
				.get("resident");
		processInfoMap.put(Constant.ElasticSearch.Keys.Process.RESIDENT_MEMORY,
				value);
		value = ((Map<String, Object>) nodeStatsProcess.get("mem"))
				.get("share");
		processInfoMap.put(Constant.ElasticSearch.Keys.Process.SHARED_MEMORY,
				value);
		value = ((Map<String, Object>) nodeStatsProcess.get("mem"))
				.get("total_virtual");
		processInfoMap
				.put(Constant.ElasticSearch.Keys.Process.TOTAL_VIRTUAL_MEMORY,
						value);
		return processInfoMap;
	}

	/**
	 * Gets the thread pool info.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the thread pool info
	 */
	private Map<String, Object> getThreadPoolInfo(
			Map<String, Object> nodesStats, Map<String, Object> nodesInfo) {
		Map<String, Object> threadPoolInfoMap = new HashMap<String, Object>();
		Object value;
		Map<String, Object> nodeStatsThreadPool = (Map<String, Object>) nodesStats
				.get("thread_pool");
		Map<String, Object> nodeInfoThreadPool = (Map<String, Object>) nodesInfo
				.get("thread_pool");
		//
		value = ((Map<String, Object>) nodeStatsThreadPool.get("index"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("index"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("index"))
						.get("active");
		threadPoolInfoMap.put(Constant.ElasticSearch.Keys.Thread_Pool.INDEX,
				value);
		value = ((Map<String, Object>) nodeStatsThreadPool.get("get"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("get"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("get"))
						.get("active");
		threadPoolInfoMap.put(Constant.ElasticSearch.Keys.Thread_Pool.GET,
				value);
		value = ((Map<String, Object>) nodeStatsThreadPool.get("search"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("search"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("search"))
						.get("active");
		threadPoolInfoMap.put(Constant.ElasticSearch.Keys.Thread_Pool.SERCH,
				value);
		value = ((Map<String, Object>) nodeStatsThreadPool.get("bulk"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("bulk"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("bulk"))
						.get("active");
		threadPoolInfoMap.put(Constant.ElasticSearch.Keys.Thread_Pool.BULK,
				value);
		value = ((Map<String, Object>) nodeStatsThreadPool.get("refresh"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("refresh"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("refresh"))
						.get("active");
		threadPoolInfoMap.put(Constant.ElasticSearch.Keys.Thread_Pool.REFRESH,
				value);
		value = ((Map<String, Object>) nodeStatsThreadPool.get("flush"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("flush"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("flush"))
						.get("active");
		threadPoolInfoMap.put(Constant.ElasticSearch.Keys.Thread_Pool.FLUSH,
				value);
		value = ((Map<String, Object>) nodeStatsThreadPool.get("merge"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("merge"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("merge"))
						.get("active");
		threadPoolInfoMap.put(Constant.ElasticSearch.Keys.Thread_Pool.MERGE,
				value);
		value = ((Map<String, Object>) nodeStatsThreadPool.get("management"))
				.get("queue")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("management"))
						.get("largest")
				+ "/"
				+ ((Map<String, Object>) nodeStatsThreadPool.get("management"))
						.get("active");
		threadPoolInfoMap.put(
				Constant.ElasticSearch.Keys.Thread_Pool.MANAGEMENT, value);
		return threadPoolInfoMap;
	}

	/**
	 * Gets the network info.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the network info
	 */
	private Map<String, Object> getNetworkInfo(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		Map<String, Object> networkInfoMap = new HashMap<String, Object>();
		Object value;
		//
		value = nodesInfo.get("http_address");
		networkInfoMap.put(Constant.ElasticSearch.Keys.Network.HTTP_ADDRESS,
				value);
		value = ((Map<String, Object>) nodesInfo.get("http"))
				.get("bound_address");
		networkInfoMap.put(
				Constant.ElasticSearch.Keys.Network.HTTP_BOUND_ADDRESS, value);
		value = ((Map<String, Object>) nodesInfo.get("http"))
				.get("publish_address");
		networkInfoMap
				.put(Constant.ElasticSearch.Keys.Network.HTTP_PUBLISH_ADDRESS,
						value);
		value = nodesInfo.get("transport_address");
		networkInfoMap.put(
				Constant.ElasticSearch.Keys.Network.TRANSPORT_ADDRESS, value);
		value = ((Map<String, Object>) nodesInfo.get("transport"))
				.get("bound_address");
		networkInfoMap.put(
				Constant.ElasticSearch.Keys.Network.TRANSPORT_BOUND_ADDRESS,
				value);
		value = ((Map<String, Object>) nodesInfo.get("transport"))
				.get("publish_address");
		networkInfoMap.put(
				Constant.ElasticSearch.Keys.Network.TRANSPORT_PUBLISH_ADDRESS,
				value);
		return networkInfoMap;
	}

	/**
	 * Gets the fs info.
	 * 
	 * @param nodesStats
	 *            the nodes stats
	 * @param nodesInfo
	 *            the nodes info
	 * @return the fs info
	 */
	private Map<String, Object> getFsInfo(Map<String, Object> nodesStats,
			Map<String, Object> nodesInfo) {
		Map<String, Object> fsInfoMap = new HashMap<String, Object>();
		Object value;
		Map<String, Object> nodeStatsFs = (Map<String, Object>) nodesStats
				.get("fs");
		List fsDataList = (List) nodeStatsFs.get("data");
		Map<String, Object> fsData = (Map<String, Object>) fsDataList.get(0);
		//
		value = fsData.get("path");
		fsInfoMap.put(Constant.ElasticSearch.Keys.File_System.PATH, value);
		value = fsData.get("mount");
		fsInfoMap.put(Constant.ElasticSearch.Keys.File_System.MOUNT, value);
		value = fsData.get("dev");
		fsInfoMap.put(Constant.ElasticSearch.Keys.File_System.DEVICE, value);
		value = fsData.get("total");
		fsInfoMap.put(Constant.ElasticSearch.Keys.File_System.TOTAL_SPACE,
				value);
		value = fsData.get("free");
		fsInfoMap
				.put(Constant.ElasticSearch.Keys.File_System.FREE_SPACE, value);
		value = fsData.get("disk_reads");
		fsInfoMap
				.put(Constant.ElasticSearch.Keys.File_System.DISK_READS, value);
		value = fsData.get("disk_writes");
		fsInfoMap.put(Constant.ElasticSearch.Keys.File_System.DISK_WRITES,
				value);
		value = fsData.get("disk_read_size");
		fsInfoMap.put(Constant.ElasticSearch.Keys.File_System.READ_SIZE, value);
		value = fsData.get("disk_write_size");
		fsInfoMap
				.put(Constant.ElasticSearch.Keys.File_System.WRITE_SIZE, value);
		return fsInfoMap;
	}

	/**
	 * Indices.
	 */
	public void indices() {
		Map map = getESMonitoringData();
		List<Index> indicesList = null;
		if (map.get(Constant.Keys.ERROR) != null) {
			errors.add(map.get(Constant.Keys.ERROR).toString());
		} else {
			ElasticSearchMonitoringData esMonitoringData = (ElasticSearchMonitoringData) map
					.get(Constant.Keys.OUTPUT);
			// List<Index> indicesList = null;
			// if (esMonitoringData == null) {
			// // add error
			// errors.add(NO_DATA_ERROR_STRING);
			// } else {
			indicesList = new ArrayList<Index>();
			Map stats = esMonitoringData.getStats();
			Map clusterState = esMonitoringData.getClusterState();
			Map<String, Object> indicesMapFromMetadata = (Map<String, Object>) ((Map<String, Object>) clusterState
					.get("metadata")).get("indices");
			Map<String, Object> indicesFromStats = (Map<String, Object>) stats
					.get("indices");
			for (String index : indicesFromStats.keySet()) {
				Index indexObj = new Index();
				indexObj.setIndex(index);
				Map<String, Object> dataIndicesStats = (Map<String, Object>) indicesFromStats
						.get(index);
				indexObj.setPrimarySize(((Map<String, Object>) ((Map<String, Object>) dataIndicesStats
						.get("primaries")).get("store")).get("size").toString());
				indexObj.setNoOfDocs(((Map<String, Object>) ((Map<String, Object>) dataIndicesStats
						.get("primaries")).get("docs")).get("count").toString());
				Map<String, Object> dataIndicesMetadata = (Map<String, Object>) indicesMapFromMetadata
						.get(index);
				indexObj.setNoOfShards(((Map<String, Object>) dataIndicesMetadata
						.get("settings")).get("index.number_of_shards")
						.toString());
				indexObj.setNoOfReplicas(((Map<String, Object>) dataIndicesMetadata
						.get("settings")).get("index.number_of_replicas")
						.toString());
				indexObj.setStatus(dataIndicesMetadata.get("state").toString());
				indicesList.add(indexObj);
			}
		}
		result.put("indices", indicesList);
	}

	/*
	 * private void indexTiles() { List<Object> indexTiles = null; String index
	 * = (String) parameterMap.get("index"); ElasticSearchMonitoringData
	 * esMonitoringData = getESMonitoringData(); List<Index> indicesList = new
	 * ArrayList<Index>(); if (esMonitoringData == null) { // add error
	 * getErrorTile(); } else { indexTiles = new ArrayList<Object>();
	 * Map<String, Object> stats = (Map<String, Object>) esMonitoringData
	 * .getStats(); Map<String, Object> indicesStatsMap = ((Map<String, Object>)
	 * stats .get("indices")); if (indicesStatsMap.size() != 0 ||
	 * !indicesStatsMap.isEmpty() || indicesStatsMap != null) { Map<String,
	 * Object> indexStatsMap = (Map<String, Object>) indicesStatsMap
	 * .get(index); Map<String, Object> indexStatsMapTotal = (Map<String,
	 * Object>) indexStatsMap .get("total"); Map<String, Object>
	 * indexStatsMapPrimary = (Map<String, Object>) indexStatsMap
	 * .get("primaries"); } } }
	 */

	/**
	 * Gets the index info.
	 * 
	 * @param index
	 *            the index
	 * @return the index info
	 */
	private Map<String, Object> getindexInfo(String index) {
		Map<String, Object> indexInfo = null;
		// ElasticSearchMonitoringData esMonitoringData = getESMonitoringData();
		List<Index> indicesList = new ArrayList<Index>();
		Map map = getESMonitoringData();
		if (map.get(Constant.Keys.ERROR) != null) {
			errors.add(map.get(Constant.Keys.ERROR).toString());
		} else {
			ElasticSearchMonitoringData esMonitoringData = (ElasticSearchMonitoringData) map
					.get(Constant.Keys.OUTPUT);
			// if (esMonitoringData == null) {
			// // add error
			// errors.add(NO_DATA_ERROR_STRING);
			// } else {
			Map<String, HashMap<String, Object>> indicesDetail = esMonitoringData
					.getIndicesDetail();
			indexInfo = new HashMap<String, Object>();
			if (!indicesDetail.isEmpty() && indicesDetail != null) {
				indexInfo = indicesDetail.get(index);
			}
		}
		return indexInfo;
	}

	/**
	 * Indexdetail.
	 */
	public void indexdetail() {
		Map<String, Object> indexInfo = null;
		String index = (String) parameterMap.get("index");
		// ElasticSearchMonitoringData esMonitoringData = getESMonitoringData();
		List<Index> indicesList = new ArrayList<Index>();
		Map map = getESMonitoringData();
		if (map.get(Constant.Keys.ERROR) != null) {
			errors.add(map.get(Constant.Keys.ERROR).toString());
		} else {
			ElasticSearchMonitoringData esMonitoringData = (ElasticSearchMonitoringData) map
					.get(Constant.Keys.OUTPUT);
			// if (esMonitoringData == null) {
			// // add error
			// errors.add(NO_DATA_ERROR_STRING);
			// } else {
			Map<String, HashMap<String, Object>> indicesDetail = esMonitoringData
					.getIndicesDetail();
			indexInfo = new HashMap<String, Object>();
			indexInfo = indicesDetail.get(index);
			indexInfo.remove(Constant.ElasticSearch.Keys.TILES);
			indexInfo.remove(Constant.ElasticSearch.Keys.SHARDS);
			indexInfo.remove(Constant.ElasticSearch.Keys.ALIASES);
			/*
			 * Map<String,Object> stats =
			 * (Map<String,Object>)esMonitoringData.getStats();
			 * Map<String,Object> status =
			 * (Map<String,Object>)esMonitoringData.getStatus();
			 * Map<String,Object> indicesStatusMap =
			 * ((Map<String,Object>)status.get("indices")); Map<String,Object>
			 * indicesStatsMap = ((Map<String,Object>)stats.get("indices"));
			 * if(indicesStatusMap.size() != 0 || !indicesStatusMap.isEmpty() ||
			 * indicesStatusMap != null){ Map<String,Object> indexStatusMap =
			 * (Map<String,Object>)indicesStatusMap.get(index);
			 * Map<String,Object> indexStatsMap =
			 * (Map<String,Object>)indicesStatsMap.get(index);
			 * Map<String,Object> indexStatsMapTotal =
			 * (Map<String,Object>)indexStatsMap.get("total");
			 * indexInfo.put("documents", getDocumentsTable(indexStatusMap));
			 * indexInfo.put("operations", getOperationsTable(indexStatusMap));
			 * indexInfo.put("mergeActivity",
			 * getMergeActivityTable(indexStatusMap));
			 * indexInfo.put("searchTotal",
			 * getSearchTotalTable(indexStatsMapTotal));
			 * indexInfo.put("getTotal", getGetTotalTable(indexStatsMapTotal));
			 * indexInfo.put("indexingTotal",
			 * getIndexingTotalTable(indexStatsMapTotal)); }
			 */
			result.put("indexInfo", indexInfo);
		}
	}

	/**
	 * Indextiles.
	 */
	public void indextiles() {
		Map map = getESMonitoringData();
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		ElasticSearchMonitoringData esMonitoringData = (ElasticSearchMonitoringData) map
				.get(Constant.Keys.OUTPUT);
		if ((map.get(Constant.Keys.ERROR) != null)
				|| (esMonitoringData == null)) {
			String monitoringNodeIp = (String)map.get(monitoringNodeString);
			tiles.add(getErrorTile(monitoringNodeIp));
			result.put(Constant.Keys.TILES, tiles);
		}else{
			String index = (String) parameterMap.get("index");
			Map<String, Object> indexInfo = getindexInfo(index);
			if ((indexInfo != null) && (!indexInfo.isEmpty())) {
				HashMap<String, Object> tilesMap = (HashMap<String, Object>) indexInfo
						.get(Constant.ElasticSearch.Keys.TILES);
				
				for (String key : tilesMap.keySet()) {
					TileInfo tileInfo = new TileInfo(tilesMap.get(key).toString(),
							key, "", null, Constant.Tile.Status.NORMAL, null);
					tiles.add(tileInfo);
				}
				result.put(Constant.Keys.TILES, tiles);
			}
		}
		/*String index = (String) parameterMap.get("index");
		Map<String, Object> indexInfo = getindexInfo(index);
		if ((indexInfo != null) && (!indexInfo.isEmpty())) {
			HashMap<String, Object> tilesMap = (HashMap<String, Object>) indexInfo
					.get(Constant.ElasticSearch.Keys.TILES);
			
			for (String key : tilesMap.keySet()) {
				TileInfo tileInfo = new TileInfo(tilesMap.get(key).toString(),
						key, "", null, Constant.Tile.Status.NORMAL, null);
				tiles.add(tileInfo);
			}
			result.put(Constant.Keys.TILES, tiles);
		} else {
			result.put(Constant.Keys.TILES, getErrorTile());
		}*/
	}

	/**
	 * Indexshards.
	 */
	public void indexshards() {
		String index = (String) parameterMap.get("index");
		Map<String, Object> indexInfo = getindexInfo(index);
		if ((indexInfo != null) && (!indexInfo.isEmpty())) {
			result.put(Constant.ElasticSearch.Keys.SHARDS,
					indexInfo.get(Constant.ElasticSearch.Keys.SHARDS));
		} else {
			errors.add(NO_DATA_ERROR_STRING);
		}
	}

	/**
	 * Indexaliases.
	 */
	public void indexaliases() {
		String index = (String) parameterMap.get("index");
		Map<String, Object> indexInfo = getindexInfo(index);
		if ((indexInfo != null) && (!indexInfo.isEmpty())) {
			HashMap aliases = (HashMap) indexInfo
					.get(Constant.ElasticSearch.Keys.ALIASES);
			result.put(Constant.ElasticSearch.Keys.ALIASES, aliases);
			/*
			 * if ((aliases != null) && !(aliases.isEmpty())) {
			 * result.put(Constant.ElasticSearch.Keys.ALIASES, aliases); }
			 *//*
				 * else { result.put(Constant.ElasticSearch.Keys.ALIASES,
				 * "No aliases associated with this index."); }
				 */
		} else {
			errors.add(NO_DATA_ERROR_STRING);
		}
	}

	/**
	 * Gets the documents table.
	 * 
	 * @param indexMap
	 *            the index map
	 * @return the documents table
	 */
	private Map<String, Object> getDocumentsTable(Map<String, Object> indexMap) {
		Map<String, Object> documentsMap = new HashMap<String, Object>();
		Object val;
		val = ((Map<String, Object>) indexMap.get("docs")).get("num_docs");
		documentsMap.put(Constant.ElasticSearch.Keys.Documents.DOCS, val);
		val = ((Map<String, Object>) indexMap.get("docs")).get("max_doc");
		documentsMap.put(Constant.ElasticSearch.Keys.Documents.MAX_DOCS, val);
		val = ((Map<String, Object>) indexMap.get("docs")).get("deleted_docs");
		documentsMap.put(Constant.ElasticSearch.Keys.Documents.DELETED_DOCS,
				val);
		val = ((Map<String, Object>) indexMap.get("index")).get("primary_size");
		documentsMap.put(Constant.ElasticSearch.Keys.Documents.PRIMARY_SIZE,
				val);
		val = ((Map<String, Object>) indexMap.get("index")).get("size");
		documentsMap.put(Constant.ElasticSearch.Keys.Documents.TOTAL_SIZE, val);
		return documentsMap;
	}

	/**
	 * Gets the merge activity table.
	 * 
	 * @param indexMap
	 *            the index map
	 * @return the merge activity table
	 */
	private Map<String, Object> getMergeActivityTable(
			Map<String, Object> indexMap) {
		Map<String, Object> mergeActivityMap = new HashMap<String, Object>();
		Object val;
		val = ((Map<String, Object>) indexMap.get("merges")).get("total");
		mergeActivityMap.put(
				Constant.ElasticSearch.Keys.Merge_Activity.MERGE_TOTAL, val);
		val = ((Map<String, Object>) indexMap.get("merges")).get("total_time");
		mergeActivityMap.put(
				Constant.ElasticSearch.Keys.Merge_Activity.MERGE_TOTAL_TIME,
				val);
		val = ((Map<String, Object>) indexMap.get("merges")).get("total_docs");
		mergeActivityMap.put(
				Constant.ElasticSearch.Keys.Merge_Activity.MERGE_TOTAL_DOCS,
				val);
		val = ((Map<String, Object>) indexMap.get("merges")).get("total_size");
		mergeActivityMap.put(
				Constant.ElasticSearch.Keys.Merge_Activity.MERGE_TOTAL_SIZE,
				val);
		return mergeActivityMap;
	}

	/**
	 * Gets the operations table.
	 * 
	 * @param indexMap
	 *            the index map
	 * @return the operations table
	 */
	private Map<String, Object> getOperationsTable(Map<String, Object> indexMap) {
		Map<String, Object> operationsMap = new HashMap<String, Object>();
		Object val;
		val = ((Map<String, Object>) indexMap.get("refresh")).get("total");
		operationsMap.put(Constant.ElasticSearch.Keys.Operations.REFRESH_TOTAL,
				val);
		val = ((Map<String, Object>) indexMap.get("refresh")).get("total_time");
		operationsMap.put(Constant.ElasticSearch.Keys.Operations.REFRESH_TIME,
				val);
		val = ((Map<String, Object>) indexMap.get("flush")).get("total");
		operationsMap.put(Constant.ElasticSearch.Keys.Operations.FLUSH_TOTAL,
				val);
		val = ((Map<String, Object>) indexMap.get("flush")).get("total_time");
		operationsMap.put(Constant.ElasticSearch.Keys.Operations.FLUSH_TIME,
				val);
		return operationsMap;
	}

	/**
	 * Gets the search total table.
	 * 
	 * @param indexMap
	 *            the index map
	 * @return the search total table
	 */
	private Map<String, Object> getSearchTotalTable(Map<String, Object> indexMap) {
		Map<String, Object> searchTotalsMap = new HashMap<String, Object>();
		Object val;
		val = ((Map<String, Object>) indexMap.get("search")).get("query_total");
		searchTotalsMap.put(
				Constant.ElasticSearch.Keys.Search_Total.QUERY_TOTAL, val);
		val = ((Map<String, Object>) indexMap.get("search")).get("query_time");
		searchTotalsMap.put(
				Constant.ElasticSearch.Keys.Search_Total.QUERY_TIME, val);
		val = ((Map<String, Object>) indexMap.get("search")).get("fetch_total");
		searchTotalsMap.put(
				Constant.ElasticSearch.Keys.Search_Total.FETCH_TOTAL, val);
		val = ((Map<String, Object>) indexMap.get("search")).get("fetch_time");
		searchTotalsMap.put(
				Constant.ElasticSearch.Keys.Search_Total.FETCH_TIME, val);
		return searchTotalsMap;
	}

	/**
	 * Gets the gets the total table.
	 * 
	 * @param indexMap
	 *            the index map
	 * @return the gets the total table
	 */
	private Map<String, Object> getGetTotalTable(Map<String, Object> indexMap) {
		Map<String, Object> getTotalsMap = new HashMap<String, Object>();
		Object val;
		val = ((Map<String, Object>) indexMap.get("get")).get("total");
		getTotalsMap.put(Constant.ElasticSearch.Keys.Get_Total.GET_TOTAL, val);
		val = ((Map<String, Object>) indexMap.get("get")).get("get_time");
		getTotalsMap.put(Constant.ElasticSearch.Keys.Get_Total.GET_TIME, val);
		val = ((Map<String, Object>) indexMap.get("get")).get("exists_total");
		getTotalsMap.put(Constant.ElasticSearch.Keys.Get_Total.EXISTS_TOTAL,
				val);
		val = ((Map<String, Object>) indexMap.get("get")).get("exists_time");
		getTotalsMap
				.put(Constant.ElasticSearch.Keys.Get_Total.EXISTS_TIME, val);
		val = ((Map<String, Object>) indexMap.get("get")).get("missing_total");
		getTotalsMap.put(Constant.ElasticSearch.Keys.Get_Total.MISSING_TOTAL,
				val);
		val = ((Map<String, Object>) indexMap.get("get")).get("missing_time");
		getTotalsMap.put(Constant.ElasticSearch.Keys.Get_Total.MISSING_TIME,
				val);
		return getTotalsMap;
	}

	/**
	 * Gets the indexing total table.
	 * 
	 * @param indexMap
	 *            the index map
	 * @return the indexing total table
	 */
	private Map<String, Object> getIndexingTotalTable(
			Map<String, Object> indexMap) {
		Map<String, Object> indexingTotalsMap = new HashMap<String, Object>();
		Object val;
		val = ((Map<String, Object>) indexMap.get("indexing"))
				.get("index_total");
		indexingTotalsMap.put(
				Constant.ElasticSearch.Keys.Indexing_Total.INDEX_TOTAL, val);
		val = ((Map<String, Object>) indexMap.get("indexing"))
				.get("index_time");
		indexingTotalsMap.put(
				Constant.ElasticSearch.Keys.Indexing_Total.INDEX_TIME, val);
		val = ((Map<String, Object>) indexMap.get("indexing"))
				.get("delete_total");
		indexingTotalsMap.put(
				Constant.ElasticSearch.Keys.Indexing_Total.DELETE_TOTAL, val);
		val = ((Map<String, Object>) indexMap.get("indexing"))
				.get("delete_time");
		indexingTotalsMap.put(
				Constant.ElasticSearch.Keys.Indexing_Total.DELETE_TIME, val);
		return indexingTotalsMap;
	}

	/**
	 * Creates the index.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void createindex() throws Exception {
		String errorString = "index cann't be created As ElasticSearch is Down on all the nodes";

		Map indexMap = (Map) parameterMap
				.get(Constant.ElasticSearch.Keys.INDEX);

		NewIndex newIndex = JsonMapperUtil.objectFromMap(indexMap,
				NewIndex.class);
		AnkushRestClient restClient = new AnkushRestClient();

		String urlPath = getRestUrlFixedPart();

		if (urlPath != null) {
			urlPath += newIndex.getIndexId();
		} else {
			return;
		}
		Map postData = new HashMap();
		Map<String, Object> settingsMap = new HashMap<String, Object>();
		settingsMap.put(Constant.ElasticSearch.Keys.NUMBER_OF_SHARDS,
				newIndex.getShards());
		settingsMap.put(Constant.ElasticSearch.Keys.NUMBER_OF_REPLICAS,
				newIndex.getReplicas());
		postData.put(Constant.ElasticSearch.Keys.SETTING, settingsMap);
		sendRequest(urlPath, JsonMapperUtil.jsonFromObject(postData),
				Constant.Method_Type.POST);
	}

	/**
	 * Createalias.
	 */
	public void createalias() {
		Map<String, String> aliasMap = (Map<String, String>) parameterMap
				.get("alias");

		String urlPath = getRestUrlFixedPart();
		if (urlPath != null) {
			urlPath += "_aliases";
			String index = aliasMap.get(Constant.ElasticSearch.Keys.INDEX);
			String alias = aliasMap.get(Constant.ElasticSearch.Keys.ALIAS);
			String searchRouting = aliasMap.get("search_routing");
			String indexRouting = aliasMap.get("index_routing");
			Map<String, Object> postData = new HashMap<String, Object>();
			List<Map<String, Object>> actionList = new ArrayList<Map<String, Object>>();
			Map<String, Object> actionMap = new HashMap<String, Object>();
			actionMap.put("index", index);
			actionMap.put("alias", alias);
			actionMap.put("search_routing", searchRouting);
			actionMap.put("index_routing", indexRouting);
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("add", actionMap);
			actionList.add(newMap);
			postData.put("actions", actionList);
			sendRequest(urlPath, JsonMapperUtil.jsonFromObject(postData),
					Constant.Method_Type.POST);
		} else {
			return;
		}
	}

	/**
	 * Removealias.
	 */
	public void removealias() {

		String index = (String) parameterMap
				.get(Constant.ElasticSearch.Keys.INDEX);
		String alias = (String) parameterMap
				.get(Constant.ElasticSearch.Keys.ALIAS);

		String urlPath = getRestUrlFixedPart();
		if (urlPath != null) {
			urlPath += "_aliases";
			Map<String, Object> postData = new HashMap<String, Object>();
			List<Map<String, Object>> actionList = new ArrayList<Map<String, Object>>();
			Map<String, Object> actionMap = new HashMap<String, Object>();
			actionMap.put(Constant.ElasticSearch.Keys.INDEX, index);
			actionMap.put(Constant.ElasticSearch.Keys.ALIAS, alias);
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("remove", actionMap);
			actionList.add(newMap);
			postData.put("actions", actionList);
			sendRequest(urlPath, JsonMapperUtil.jsonFromObject(postData),
					Constant.Method_Type.POST);
		} else {
			return;
		}
	}

	/**
	 * Administer index.
	 */
	public void administerindex() {
		final String errorString = "Not able to perform this operation";
		String action = (String) parameterMap.get(Constant.Keys.ACTION);
		String index = (String) parameterMap
				.get(Constant.ElasticSearch.Keys.INDEX);
		Map<String, String> actionMap = new HashMap<String, String>();

		actionMap.put(Constant.ElasticSearch.Keys.FLUSH,
				Constant.ElasticSearch.Index_Administration_Keys.FLUSH);
		actionMap.put(Constant.ElasticSearch.Keys.CLEAR_CACHE,
				Constant.ElasticSearch.Index_Administration_Keys.CACHE_CLEAR);
		actionMap.put(Constant.ElasticSearch.Keys.OPTMIZE,
				Constant.ElasticSearch.Index_Administration_Keys.OPTIMIZE);
		actionMap.put(Constant.ElasticSearch.Keys.REFRESH,
				Constant.ElasticSearch.Index_Administration_Keys.REFRESH);
		actionMap.put(Constant.ElasticSearch.Keys.CLOSE,
				Constant.ElasticSearch.Index_Administration_Keys.CLOSE);
		actionMap.put(Constant.ElasticSearch.Keys.OPEN,
				Constant.ElasticSearch.Index_Administration_Keys.OPEN);
		actionMap.put(Constant.ElasticSearch.Keys.DELETE, "");
		String urlPath = getRestUrlFixedPart();
		if (urlPath != null) {
			urlPath += index;
		} else {
			return;
		}

		if (!action.equals(Constant.ElasticSearch.Keys.DELETE)) {
			urlPath += "/" + actionMap.get(action);
		}

		Map dataMap = new HashMap();

		if (action.equals(Constant.ElasticSearch.Keys.DELETE)) {
			sendRequest(urlPath, null, Constant.Method_Type.DELETE);
		} else if (action.equals(Constant.ElasticSearch.Keys.CLOSE)) {
			dataMap.put("indexId", index);
			dataMap.put("cmd", actionMap.get(action));
			sendRequest(urlPath, JsonMapperUtil.jsonFromObject(dataMap),
					Constant.Method_Type.POST);
		} else if (action.equals(Constant.ElasticSearch.Keys.OPEN)) {
			dataMap.put("indexId", index);
			dataMap.put("cmd", actionMap.get(action));
			sendRequest(urlPath, JsonMapperUtil.jsonFromObject(dataMap),
					Constant.Method_Type.POST);
		} else {
			sendRequest(urlPath, null, Constant.Method_Type.GET);
		}
	}

	/**
	 * Gets the rest url fixed part.
	 * 
	 * @return the rest url fixed part
	 */
	private String getRestUrlFixedPart() {
		String url = null;
		ElasticSearchConf esConf = (ElasticSearchConf) this.dbCluster
				.getClusterConf().getClusterComponents()
				.get(Constant.Component.Name.ELASTICSEARCH);
		List<NodeConf> nodes = esConf.getNodes();
		Map<String, Object> outputMap = ElasticSearchUtils
				.geFirsttLiveNode(esConf.getNodes());
		if ((Boolean) outputMap.get(Constant.Keys.STATUS)) {
			NodeConf esLiveNode = (NodeConf) outputMap
					.get(Constant.Keys.OUTPUT);
			if (esLiveNode != null) {
				url = "http://" + esLiveNode.getPublicIp() + ":"
						+ esConf.getHttpPort() + "/";
			}
		} else {
			errors.add((String) outputMap.get(Constant.Keys.ERROR));
		}
		return url;
	}

	/**
	 * Send request.
	 * 
	 * @param urlPath
	 *            the url path
	 * @param input
	 *            the input
	 * @param method
	 *            the method
	 * @return the map
	 */
	private void sendRequest(String urlPath, String input, String method) {
		final String errorString = "Could not complete this operation";
		final String successString = "Operation Successful";
		final String APPLICATION_JSON = "application/json";
		AnkushRestClient restClient = new AnkushRestClient();
		Map outputMap = restClient.sendRequest(urlPath, input, method,
				APPLICATION_JSON, APPLICATION_JSON);
		int responseCode = (Integer) outputMap.get(Constant.Keys.RESPONSE_CODE);
		logger.info("responseCode for urlPath = "+urlPath +"is :" +responseCode);
		String output = (String) outputMap.get(Constant.Keys.OUTPUT);
		if (responseCode != HttpURLConnection.HTTP_OK) {
			if ((output != null) && !(output.isEmpty())) {
				errors.add(output);
			} else {
				errors.add(errorString);
			}
			return;
		} else {
			output = successString;
		}
		result.put(Constant.Keys.OUTPUT, output);
	}
}
