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
package com.impetus.ankush.kafka;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import org.apache.commons.lang.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
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
import com.impetus.ankush.common.utils.JmxUtil;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.zookeeper.ZookeeperConf;
import com.impetus.ankush.elasticsearch.ElasticSearchClusterMonitor;
import com.impetus.ankush.hadoop.config.Parameter;

// TODO: Auto-generated Javadoc
/**
 * The Class KafkaClusterMonitor.
 */
public class KafkaClusterMonitor extends AbstractMonitor {

	/** The active controller string. */
	private static String activeControllerString = "\"kafka.controller\":type=\"KafkaController\",name=\"ActiveControllerCount\"";

	/** The all msg string. */
	private static String allMsgString = "\"kafka.server\":type=\"BrokerTopicMetrics\",name=\"AllTopicsMessagesInPerSec\"";

	/** The all bytes in string. */
	private static String allBytesInString = "\"kafka.server\":type=\"BrokerTopicMetrics\",name=\"AllTopicsBytesInPerSec\"";

	/** The all bytes out string. */
	private static String allBytesOutString = "\"kafka.server\":type=\"BrokerTopicMetrics\",name=\"AllTopicsBytesOutPerSec\"";

	/** The leader count string. */
	private static String leaderCountString = "\"kafka.server\":type=\"ReplicaManager\",name=\"LeaderCount\"";

	/** The log flush rate and time string. */
	private static String logFlushRateAndTimeString = "\"kafka.log\":type=\"LogFlushStats\",name=\"LogFlushRateAndTimeMs\"";

	/** The partition under replicated string. */
	private static String partitionUnderReplicatedString = "\"kafka.cluster\":type=\"Partition\",name=\"*-UnderReplicated\"";

	/** The logger. */
	AnkushLogger logger = new AnkushLogger(KafkaClusterMonitor.class);

	/** The nodeManager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);
	
	// configuration manager for save/delete configuration.
	final ConfigurationManager confManager = new ConfigurationManager();

	/** The list KafkaNodes. */
	private List<NodeConf> kafkaNodes;

	/** The KafkaConf. */
	private KafkaConf kafkaConf;

	/** The String SPACE. */
	private static final String STR_SPACE = " ";

	/** The String COLON. */
	private static final String STR_COLON = ":";

	/** The String BROKERS. */
	private static final String BROKERS = "brokers";

	/** The String TOPICS. */
	private static final String TOPICS = "topics";

	/** The String MSG. */
	private static final String MESSAGE = "message";

	/** The Constant LOGS. */
	private static final String LOGS = "/logs/";

	/** The String TOPIC_DETAIL. */
	private static final String TOPIC_DETAIL = "topicDetail";

	/** The listTopicCommand String. */
	private static final String listTopicCommand = "bin/kafka-list-topic.sh"
			+ STR_SPACE + "--zookeeper" + STR_SPACE;

	/** The Constant createTopicString. */
	private static final String createTopicString = "bin/kafka-create-topic.sh"
			+ STR_SPACE;

	/** The Constant partition. */
	private static final String partition = "--partition" + STR_SPACE;

	/** The Constant replica. */
	private static final String replica = "--replica" + STR_SPACE;

	/** The Constant topic. */
	private static final String topic = "--topic" + STR_SPACE;

	/** The Constant zookeeper. */
	private static final String zookeeper = "--zookeeper" + STR_SPACE;

	/** The listTopicFailureString. */
	private String listTopicFailureString = "list topic failed because of ";

	/** The listTopicFailZookeeperError */
	private String listTopicFailZookeeperError = "Could not list topic.Zookeeper connection refused.";

	/** The failureString. */
	private String failureString = "Unable to fetch the details.";

	/** The noTopicExistString. */
	private String noTopicExistString = "no topics exist!";

	/** The Constant createTopicSuccessString. */
	private static final String createTopicSuccessString = "creation succeeded!";

	/** The Constant zookeeperDownErrorString. */
	private static final String zookeeperDownErrorString = "org.apache.zookeeper.ClientCnxn";

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
			((HashMap) result.get("kafka")).remove("nodes");

			// set nodes
			setNodes(nodeConfs);
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Sets the nodes log msg.
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
		List<TileInfo> tiles = new ArrayList<TileInfo>();
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

		// putting the tiles.
		result.put(Constant.Keys.TILES, tiles);
	}

	/**
	 * Method to get nodes tile.
	 * 
	 * @return the nodes tile
	 */
//	private void getNodesTypeTiles() {
	private List<TileInfo> getNodesTypeTiles() {

		// kafka conf object.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);
		Integer kafkas = conf.getNodes().size();
		Integer zookeepers = ((List<String>) conf.getZkNodesPort().get(
				Constant.Kafka_Keys.ZK_NODES)).size();

		// create kafka tile
		TileInfo kafkaTile = new TileInfo();
		kafkaTile.setLine1(kafkas.toString());
		kafkaTile.setLine2(Constant.Role.KAFKA);
		kafkaTile.setStatus(Constant.Tile.Status.NORMAL);

		// create zookeeper tile
		TileInfo zookeeperTile = new TileInfo();
		zookeeperTile.setLine1(zookeepers.toString());
		zookeeperTile.setLine2(Constant.Role.ZOOKEEPER);
		zookeeperTile.setStatus(Constant.Tile.Status.NORMAL);

		// list object.
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		tiles.add(kafkaTile);

		tiles.add(zookeeperTile);

		return tiles;
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
	 * This methodchecks all the services status on a node and returns true if
	 * all the services are running.
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
	 * Sets the add node tile.
	 * 
	 * @return the adds the node tile
	 */
	private TileInfo getAddNodeTile() {
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);

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
	 * Method to get the activity progress logs.
	 * 
	 * @return Map of all progress logs.
	 */
	private void addingnodes() {
		ClusterConf clusterConf = dbCluster.getClusterConf();
		GenericConfiguration conf = clusterConf.getClusterComponents().get(
				Constant.Component.Name.KAFKA);

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

		// Getting kafka conf.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);

		// supervisor node ip list.
		List<String> kafkas = new ArrayList<String>();

		// filling kafka nodes.
		for (NodeConf nodeConf : conf.getNodes()) {
			kafkas.add(nodeConf.getPublicIp());
		}

		result.put(Constant.Role.KAFKA, kafkas);
		return returnResult();
	}

	/**
	 * Method to get the node type files.
	 * @return 
	 * 
	 * @return The Map of files and directories against the ip address.
	 * @throws Exception
	 *             the exception
	 */
	private void files() throws Exception {
		String errMsg = "Exception: Unable to process request for log files.";

		// Getting kafka conf.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);

		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);
		String type = (String) parameterMap.get(Constant.Keys.TYPE);

		LogViewHandler logHandler = new LogViewHandler(nodeIp,
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey());

		// the log directory of kafka.
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

		// Getting kafka conf.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);

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
		
		String errMsg = "Exception: Unable to process request to download log file.";

		// Getting kafka conf.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);

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

	/*
	 * To get the list of configuration parameters.
	 * 
	 * @throws Exception
	 */
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

		// Getting kafka conf.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);

		/** Kafka component home path. **/
		String homePath = conf.getComponentHome();

		/** server.properties file path **/
		String confPath = homePath + "/config/server.properties";

		/** log4j.properties file path. **/
		String log4jFilePath = homePath + "/config/log4j.properties";

		/** publicIp from first node **/
		String publicIp = conf.getNodes().get(0).getPublicIp();
		List<String> confFilesList = new ArrayList<String>();
		confFilesList.add(confPath);
		confFilesList.add(log4jFilePath);

		Map resultInfo = new HashMap();
		for (String file : confFilesList) {
			/** file content **/
			String fileContent = SSHUtils.getFileContents(
					file,
					publicIp,
					conf.getUsername(),
					conf.getClusterConf().isAuthTypePassword() ? conf
							.getPassword() : conf.getPrivateKey(), conf
							.getClusterConf().isAuthTypePassword());

			Properties properties = new Properties();

			// Converting string into Properties.
			properties.load(new StringReader(fileContent));

			Map<String, List<Object>> map = new HashMap<String, List<Object>>();

			// Converting Properties into Map.
			for (final String name : properties.stringPropertyNames()) {
				List<Object> list = new ArrayList<Object>();
				list.add(properties.getProperty(name));
				list.add(true);
				map.put(name, list);
			}
			if (file.equals(confPath)) {
				setParamEditableStatus(map);
			}

			String[] f = file.split("/");
			// Convert splitted Array to list
			List splittedList = Arrays.asList(f);

			resultInfo.put(splittedList.get(splittedList.size() - 1), map);
		}
		result.put("params", resultInfo);
	}

	/**
	 * Sets the param editable status.
	 * 
	 * @param map
	 *            the map
	 */
	private void setParamEditableStatus(Map<String, List<Object>> map) {
		// list of params which are not editable.
		List<String> list = new ArrayList<String>();
		list.add("broker.id");
		list.add("zookeeper.connect");
		list.add("kafka.ganglia.metrics.host");
		// iterate over parameter map to set list params status as false as
		// those are not editable.
		for (Entry<String, List<Object>> entry : map.entrySet()) {
			if (list.contains(entry.getKey())) {
				List<Object> valueList = (List<Object>) entry.getValue();
				// set status as false in the list element.
				valueList.set(1, false);
			}
		}
	}

	/**
	 * Editparams.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void editparams() throws Exception {
		//check whether Agent is down
		boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
		if (isAgentDown) {
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return;
		}

		// Getting kafka conf.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);

		// confParams Map containg file name as key and list of params as Object
		// to be edited.
		Map<String, Object> confParams = (Map<String, Object>) parameterMap
				.get("params");
		String loggedUser = (String) parameterMap.get("loggedUser");
		this.kafkaNodes = conf.getNodes();
		this.kafkaConf = conf;

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
					deleteConfigFileParam(parameter, fileName);
				}
			}
		}
	}

	/**
	 * add parameter to configuration file.
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
		List<NodeConf> kafkaNodes = this.kafkaNodes;
		try {
			final Semaphore semaphore = new Semaphore(kafkaNodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : kafkaNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String propertyValue = parameter.getValue();

							// get component homepath
							String componentHomePath = kafkaConf
									.getComponentHome();

							// get server.properties file path
							String propertyFilePath = componentHomePath
									+ "/config/" + fileName;

							String username = kafkaConf.getUsername();
							String password = kafkaConf.getPassword();
							String privateKey = kafkaConf.getPrivateKey();

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
											Constant.File_Extension.PROPERTIES);
									res = connection.exec(add);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												kafkaConf.getClusterDbId(),
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
				semaphore.acquire(kafkaNodes.size());
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
	 * edit the Configuration parameters.
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

		List<NodeConf> kafkaNodes = this.kafkaNodes;
		try {
			final Semaphore semaphore = new Semaphore(kafkaNodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : kafkaNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String newValue = parameter.getValue();

							// get component homepath
							String componentHomePath = kafkaConf
									.getComponentHome();

							// get server.properties file path
							String propertyFilePath = componentHomePath
									+ "/config/" + fileName;

							String username = kafkaConf.getUsername();
							String password = kafkaConf.getPassword();
							String privateKey = kafkaConf.getPrivateKey();
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
											propertyFilePath,
											Constant.File_Extension.PROPERTIES);
									res = connection.exec(update);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												kafkaConf.getClusterDbId(),
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
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(kafkaNodes.size());
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
	 * delete the configuration parameters.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 */
	private void deleteConfigFileParam(final Parameter parameter,
			final String fileName) {
		List<NodeConf> kafkaNodes = this.kafkaNodes;
		try {
			final Semaphore semaphore = new Semaphore(kafkaNodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : kafkaNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							String propertyName = parameter.getName();

							// get component homepath
							String componentHomePath = kafkaConf
									.getComponentHome();

							// get server.properties file path
							String propertyFilePath = componentHomePath
									+ "/config/" + fileName;

							String username = kafkaConf.getUsername();
							String password = kafkaConf.getPassword();
							String privateKey = kafkaConf.getPrivateKey();
							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask update = new DeleteConfProperty(
											propertyName, propertyFilePath,
											Constant.File_Extension.PROPERTIES);
									res = connection.exec(update);
									if (res.isSuccess) {
										// removing records from the configuration table.
										confManager.removeOldConfiguration(
												kafkaConf.getClusterDbId(),
												hostName, fileName, propertyName);
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
				semaphore.acquire(kafkaNodes.size());
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
	 * This method returns brokers and topics details.
	 */

	private void brokers() {
		// Getting kafka conf.
		KafkaConf conf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);
		List<Broker> brokerList = null;

		Map<String, String> brokerIdIPMap = conf.getBrokerIdIPMap();

		// call to executeListTopicCommand() to get the topic-details
		Map output = executeListTopicCommand();

		// output is null only if listTopicCommand fails.
		if ((output != null) && (!output.containsKey(Constant.Keys.ERROR))) {
			if ((Boolean) output.get(Constant.SSHUtils_Keys.STATUS)) {

				// brokerList.
				brokerList = new ArrayList<Broker>();

				// get broker-details
				for (String brokerId : brokerIdIPMap.keySet()) {
					Broker broker = new Broker();
					broker.setBrokerId(brokerId);
					broker.setBrokerIP(brokerIdIPMap.get(brokerId));
					broker.setLeaderCount(getLeaderCount(brokerId, output));
					broker.setFollowerCount(getFollowerCount(brokerId, output));

					// add broker to brokerList.
					brokerList.add(broker);
				}
				result.put(BROKERS, brokerList);

				// get topicsMap.
				Map topicsMap = topics(output);

				// if tpicsMap is not null
				if (topicsMap.get(TOPICS) == null) {
					result.put(MESSAGE, topicsMap.get(MESSAGE));
				}
				result.put(TOPICS, topicsMap.get(TOPICS));
			} else {
				addError((String) output.get(Constant.SSHUtils_Keys.OUTPUT));
			}
		} else {
			addError((String) output.get(Constant.Keys.ERROR));
		}
	}

	/**
	 * Gets the leader count.
	 * 
	 * @param brokerId
	 *            -brokerId,whose leaderCount is needed.
	 * @param commandOutput
	 *            - listTopicCommand output
	 * @return returns the leader count of the broker with given brokerId
	 */
	private int getLeaderCount(String brokerId, Map commandOutput) {
		String output = (String) commandOutput
				.get(Constant.SSHUtils_Keys.OUTPUT);
		int leaderCount = 0;
		if (!output.equals(noTopicExistString)) {
			Map<String, List> tokenizedTopics = tokenizeString((String) commandOutput
					.get(Constant.SSHUtils_Keys.OUTPUT));
			for (String key : tokenizedTopics.keySet()) {
				List partitionsList = tokenizedTopics.get(key);
				for (int i = 0; i < partitionsList.size(); i++) {
					Map partitionMap = (Map<String, String>) partitionsList
							.get(i);
					if (partitionMap.get(Constant.Kafka_Keys.LEADER_ID).equals(
							brokerId)) {
						leaderCount++;
					}
				}
			}
		}
		return leaderCount;
	}

	/**
	 * Gets the follower count.
	 * 
	 * @param brokerId
	 *            -brokerId,whose followerCount is needed.
	 * @param commandOutput
	 *            - listTopicCommand output
	 * @return returns the follower count of the broker with given brokerId
	 */
	private int getFollowerCount(String brokerId, Map commandOutput) {
		String output = (String) commandOutput
				.get(Constant.SSHUtils_Keys.OUTPUT);
		int followerCount = 0;
		if (!output.equals(noTopicExistString)) {
			Map<String, List> tokenizedTopics = tokenizeString((String) commandOutput
					.get(Constant.SSHUtils_Keys.OUTPUT));
			for (String key : tokenizedTopics.keySet()) {
				List partitionsList = tokenizedTopics.get(key);
				for (int i = 0; i < partitionsList.size(); i++) {
					Map partitionMap = (Map<String, String>) partitionsList
							.get(i);
					String isrString = (String) partitionMap
							.get(Constant.Kafka_Keys.ISRID);
					List<String> isrList = Arrays.asList(isrString
							.split("\\s*,\\s*"));
					if (isrList.contains(brokerId)) {
						followerCount++;
					}
				}
			}
		}
		return followerCount;
	}

	/**
	 * method executes the listTopicCommand.
	 * 
	 * @return returns the command output and status in a Map.
	 */
	private Map executeListTopicCommand() {
		SSHExec connection = null;
		Map outputMap = null;

		// get kafkaConf
		KafkaConf kafkaConf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);
		List<NodeConf> kafkaNodes = kafkaConf.getNodes();

		// get zookeeperConf
		ZookeeperConf zookeeperConf = (ZookeeperConf) dbCluster
				.getClusterConf().getClusterComponents()
				.get(Constant.Technology.ZOOKEEPER);
		// get clusterConf
		ClusterConf clusterConf = dbCluster.getClusterConf();
		zookeeperConf.getNodes();

		// taking fist kafka node as the hostName as it is gangliaMaster node
		String hostname = kafkaNodes.get(0).getPublicIp();

		String username = kafkaConf.getUsername();
		String password = kafkaConf.getPassword();
		String privateKey = kafkaConf.getPrivateKey();

		// zookeeper clientPort
		int clientPort = zookeeperConf.getClientPort();

		List<NodeConf> zookeeperNodes = zookeeperConf.getNodes();

		// list to add zookeeperNode:clientPort
		List<String> zkNodes = new ArrayList<String>();

		// add clientPort to each zookeeperNode seperated by Colon
		for (NodeConf node : zookeeperNodes) {
			zkNodes.add(node.getPublicIp() + STR_COLON + clientPort);
		}

		// convert zookeeperNodeList to zookeeperNodesString
		String zookeeperNodesString = StringUtils.join(zkNodes, ",");

		// kafka-list-topic command
		String command = kafkaConf.getComponentHome() + listTopicCommand
				+ zookeeperNodesString;
		logger.info("Going to execute Command..." + command);
		try {
			outputMap = new HashMap();
			// Execute list-topic command and get output/error
			outputMap = SSHUtils.getCommandOutputAndError(command, hostname,
					username, clusterConf.isAuthTypePassword() ? password
							: privateKey, clusterConf.isAuthTypePassword());
			logger.info("Command executed successfully..." + command);
			String commandOutput = (String) outputMap
					.get(Constant.SSHUtils_Keys.OUTPUT);
			logger.info("CommandOutput" + commandOutput);
			if (commandOutput.startsWith(listTopicFailureString)) {
				logger.error(commandOutput);
				outputMap.put(Constant.Keys.ERROR, failureString);
				// outputMap = null;
			} else if (commandOutput.contains(zookeeperDownErrorString)) {
				logger.error(commandOutput);
				outputMap.put(Constant.Keys.ERROR, listTopicFailZookeeperError);
				// outputMap = null;
			}
		} catch (Exception e) {
			logger.error("Error in executing list-topic command...", e);
			outputMap.put(Constant.Keys.ERROR, failureString);
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		return outputMap;
	}

	/**
	 * Topics.
	 * 
	 * @param output
	 *            - listTopicCommand output is given.
	 * @return returns topicMap{Topics,message}
	 */
	private Map topics(Map output) {
		List<Topic> topics = null;
		Map topicsMap = new HashMap();
		String message = "";
		if (output != null) {
			if ((Boolean) output.get(Constant.SSHUtils_Keys.STATUS)) {
				String cmdOutput = (String) output
						.get(Constant.SSHUtils_Keys.OUTPUT);
				if (cmdOutput.equalsIgnoreCase(noTopicExistString)) {
					message = noTopicExistString;
				} else {
					Map<String, List> tokenizedTopics = tokenizeString(cmdOutput);
					topics = new ArrayList<Topic>();
					topics = getTopics(tokenizedTopics);
				}
			}
		} else {
			addError((String) output.get(Constant.SSHUtils_Keys.OUTPUT));
		}
		topicsMap.put(TOPICS, topics);
		topicsMap.put(MESSAGE, message);

		return topicsMap;
	}

	/**
	 * gets the topicDetail of the topic passed as a POST paramater.
	 */
	public void topicDetail() {
		String topicName = (String) parameterMap
				.get(Constant.Kafka_Keys.TOPIC_NAME);
		Map output = executeListTopicCommand();
		if (output != null && !output.containsKey(Constant.Keys.ERROR)) {
			if ((Boolean) output.get(Constant.SSHUtils_Keys.STATUS)) {
				String cmdOutput = (String) output
						.get(Constant.SSHUtils_Keys.OUTPUT);
				Map<String, List> tokenizedTopics = tokenizeString(cmdOutput);
				List<TopicDetail> topicDetailList = getTopicDetail(
						topicName.trim(), tokenizedTopics);
				result.put(TOPIC_DETAIL, topicDetailList);
			} else {
				addError((String) output.get("output"));
			}
		} else {
			addError((String) output.get(Constant.Keys.ERROR));
		}

	}

	/**
	 * Gets the topic detail.
	 * 
	 * @param topicName
	 *            - topicName
	 * @param tokenizedTopics
	 *            - Tokenized topics from the output of listTopicCommand is
	 *            given.
	 * @return the topic detail
	 * @return- returns the detail of given topicName.
	 */
	private List<TopicDetail> getTopicDetail(String topicName,
			Map<String, List> tokenizedTopics) {
		List partitionList = tokenizedTopics.get(topicName);
		List<TopicDetail> partitionListToReturn = new ArrayList<TopicDetail>();
		for (int i = 0; i < partitionList.size(); i++) {
			TopicDetail topicDetail = new TopicDetail();
			Map partitionMap = (Map) partitionList.get(i);
			String partitionId = (String) partitionMap
					.get(Constant.Kafka_Keys.PARTITION_ID);
			String leaderId = (String) partitionMap
					.get(Constant.Kafka_Keys.LEADER_ID);
			String replicasId = (String) partitionMap
					.get(Constant.Kafka_Keys.REPLICASID);
			String isrId = (String) partitionMap.get(Constant.Kafka_Keys.ISRID);

			topicDetail.setPartition(partitionId);
			topicDetail.setLeader(leaderId);
			topicDetail.setReplicas(replicasId);
			topicDetail.setIsr(isrId);
			partitionListToReturn.add(topicDetail);

		}
		return partitionListToReturn;

	}

	/**
	 * Tokenize string.
	 * 
	 * @param output
	 *            -listTopicCommand output is given.
	 * @return - returns the tokenzied output string.
	 */
	private Map<String, List> tokenizeString(String output) {
		Map<String, List> topics = null;
		if (output.startsWith(listTopicFailureString)) {
			addError(failureString);
		} else {
			topics = new HashMap<String, List>();
			StringTokenizer tokenizer = new StringTokenizer(output, "\n");
			while (tokenizer.hasMoreTokens()) {

				String[] token = tokenizer.nextToken().split("\t");
				String topicName = token[0].replace(Constant.Kafka_Keys.TOPIC,
						"").trim();
				String partitionId = token[1].replace(
						Constant.Kafka_Keys.PARTITION, "").trim();
				String leaderId = token[2].replace(Constant.Kafka_Keys.LEADER,
						"").trim();
				String replicasId = token[3].replace(
						Constant.Kafka_Keys.REPLICAS, "").trim();

				String isrId = token[4].replace(Constant.Kafka_Keys.ISR, "")
						.trim();
				List partitionsList = topics.get(topicName);
				Map<String, String> partition = new HashMap<String, String>();
				partition.put(Constant.Kafka_Keys.PARTITION_ID, partitionId);
				partition.put(Constant.Kafka_Keys.LEADER_ID, leaderId);
				partition.put(Constant.Kafka_Keys.REPLICASID, replicasId);
				partition.put(Constant.Kafka_Keys.ISRID, isrId);
				if (partitionsList == null) {
					partitionsList = new ArrayList();
				}

				partitionsList.add(partition);
				topics.put(topicName, partitionsList);

			}
		}
		return topics;
	}

	/**
	 * Gets the topics.
	 * 
	 * @param tokenizedTopics
	 *            - tokenizedTopics is given.
	 * @return - returns a list of Topic.
	 */
	private List<Topic> getTopics(Map<String, List> tokenizedTopics) {

		List<Topic> topicList = new ArrayList<Topic>();

		for (String key : tokenizedTopics.keySet()) {
			Topic topic = new Topic();
			// set topicName in topic
			topic.setTopicName(key);

			// set partitionCount in topic
			List partitionsList = tokenizedTopics.get(key);
			topic.setPartitionCount(partitionsList.size());

			// set replicationFactor in topic
			Map partition = (Map) partitionsList.get(0);
			List replicasList = Arrays.asList(((String) partition
					.get(Constant.Kafka_Keys.REPLICASID)).split(("\\s*,\\s*")));
			topic.setReplicas(replicasList.size());

			// add topic to topicList
			topicList.add(topic);
		}
		return topicList;

	}

	/**
	 * this method added the required categories in Utilization Trends for
	 * Kafka.
	 * 
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public Map categories() throws Exception {
		Map categoryMap = (Map) super.categories().get("categories");
		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

		// get kafkaConf
		KafkaConf kafkaConf = (KafkaConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);
		List<NodeConf> kafkaNodes = kafkaConf.getNodes();

		NodeConf nodeConf = null;

		// map to contains different groups of kafka.
		Map<String, String> categories = new HashMap<String, String>();
		categories.put("kafka.controller", "kafka.controller.*.rrd");
		categories.put("kafka.network", "kafka.network.*.rrd");
		categories.put("kafka.server.BrokerTopicMetrics",
				"kafka.server.BrokerTopicMetrics.*.rrd");
		categories.put("kafka.server.DelayedFetchRequestMetrics",
				"kafka.server.DelayedFetchRequestMetrics.*.rrd");
		categories.put("kafka.server.DelayedProducerRequestMetrics",
				"kafka.server.DelayedProducerRequestMetrics.*.rrd");
		categories.put("kafka.server.FetchRequestPurgatory",
				"kafka.server.FetchRequestPurgatory.*.rrd");
		categories.put("kafka.server.ProducerRequestPurgatory",
				"kafka.server.ProducerRequestPurgatory.*.rrd");
		categories.put("kafka.server.ReplicaFetcherManager",
				"kafka.server.ReplicaFetcherManager.*.rrd");
		categories.put("kafka.server.ReplicaManager",
				"kafka.server.ReplicaManager.*.rrd");
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

	/**
	 * Gets the category map.
	 * 
	 * @param categories
	 *            the categories
	 * @param nodeConf
	 *            the node conf
	 * @return the category map
	 */
	private Map<String, Object> getCategoryMap(Map<String, String> categories,
			NodeConf nodeConf) {
		Map<String, Object> categoryMap = new HashMap<String, Object>();
		try {
			// creating node graph object.
			Graph graph = new Graph(this.clusterConfig.getGangliaMaster()
					.getPublicIp(), this.clusterConfig.getUsername(),
					this.clusterConfig.getPassword(),
					this.clusterConfig.getPrivateKey(),
					this.clusterConfig.getClusterName());

			List<String> files = graph.getAllFiles(nodeConf.getPrivateIp());

			for (String category : categories.keySet()) {
				Map map = new HashMap();
				List<String> matchingFiles = graph.getMatchingFiles(
						categories.get(category), files);

				map.put("count", matchingFiles.size());
				map.put("legends", matchingFiles);
				map.put("pattern", categories.get(category));
				map.put("ip", nodeConf.getPrivateIp());
				categoryMap.put(category, map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return categoryMap;
	}

	/**
	 * Active controller.
	 */
	public void activeController() {
		KafkaConf conf = (KafkaConf) this.dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);
		List<NodeConf> nodes = conf.getNodes();
		System.out.println(nodes);
		String node = "";
		JmxUtil jmxUtil = null;
		// iterate on nodes and get JMX value of ActiveController for each node
		try {

			ObjectName mObjName = new ObjectName(activeControllerString);
			String attr = "Value";
			int jmxPort = Integer.parseInt((conf.getJmxPort()));
			// iterating on nodes
			for (int i = 0; i < nodes.size(); i++) {
				// create jmxUtil object
				jmxUtil = new JmxUtil(nodes.get(i).getPublicIp(), jmxPort);
				// connect to node
				MBeanServerConnection connection = jmxUtil.connect();
				// if connection is not null ie Kafka service is not down on
				// node
				if (connection != null) {
					Integer attributeValue = (Integer) jmxUtil.getAttribute(
							mObjName, attr);
					if (attributeValue != null && attributeValue == 1) {
						node = nodes.get(i).getPublicIp();
						break;
					}
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// disconnect to node
			jmxUtil.disconnect();
		}
		if (!node.equals("")) {
			result.put("activeController", node);
		} else {
			addError("Unable to get ActiveController");
		}

	}

	/**
	 * Gets the broker data.
	 * 
	 * @return the broker data
	 */
	private void brokerData() {
		// get node ip
		String brokerIP = (String) parameterMap.get(Constant.Keys.IP);
		BrokerData brokerData = new BrokerData();
		Map<String, Object> map = new HashMap<String, Object>();
		JmxUtil jmxUtil = null;
		KafkaConf conf = (KafkaConf) this.dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);
		try {
			// create jmxUtil object.
			jmxUtil = new JmxUtil(brokerIP, (Integer.parseInt(conf.getJmxPort())));

			// connect to node and if connection not null
			if (null != jmxUtil.connect()) {
				brokerData.setAllTopicsMsgIn(jmxUtil
						.getDoubleAttributes(new ObjectName(allMsgString)));
				brokerData.setAllTopicsBytesIn(jmxUtil
						.getDoubleAttributes(new ObjectName(allBytesInString)));
				brokerData.setAllTopicsBytesOut(jmxUtil
						.getDoubleAttributes(new ObjectName(allBytesOutString)));
				// brokerData.setLeaderCount(jmxUtil.getAttributes(new
				// ObjectName(
				// leaderCountString)));
				brokerData
						.setLogFlushDetails(jmxUtil
								.getDoubleAttributes(new ObjectName(
										logFlushRateAndTimeString)));
				// brokerData.setTopicNameList(getTopicsNameList(jmxUtil));
				brokerData.setTopicsData(getTopicsData(jmxUtil));
				// brokerData.setTiles(getBrokerTiles(jmxUtil));
				// map = jmxUtil.getAttributes(new ObjectName(allMsgString));
				// result.put("brokerData", brokerData);
				result.put("brokerData", brokerData);
			} else {
				addError("Mbean Server Connection is not available at Node..."
						+ brokerIP);
			}
		} catch (Exception e) {
			addError(e.getMessage());
			e.printStackTrace();
		} finally {
			// disconnect to node
			jmxUtil.disconnect();
		}
	}

	/**
	 * Gets the topics data.
	 * 
	 * @param serverConnection
	 *            the server connection
	 * @return the topics data
	 */
	private Map<String, TopicData> getTopicsData(JmxUtil jmxUtil) {

		// topicsDataMap to return
		Map<String, TopicData> topicsDataMap = null;

		// get topic and its partion in a map like {topic1-0,1,2}
		Map<String, Set> topicPartitionMap = getTopicPartitionMap(jmxUtil);

		if (topicPartitionMap != null) {

			// initialize topicsDataMap
			topicsDataMap = new HashMap<String, TopicData>();

			for (Entry entrySet : topicPartitionMap.entrySet()) {
				String topicName = entrySet.getKey().toString();
				TopicData topicData = new TopicData();
				// Mbean name for BytesInPerSec to topic
				String topicBytesInObjName = "\"kafka.server\":type=\"BrokerTopicMetrics\",name=\""
						+ topicName + "-BytesInPerSec\"";
				// Mbean name for BytesOutPerSec from topic
				String topicBytesOutObjName = "\"kafka.server\":type=\"BrokerTopicMetrics\",name=\""
						+ topicName + "-BytesOutPerSec\"";
				// Mbean name for MessageInPerSec to topic
				String topicMsgInObjName = "\"kafka.server\":type=\"BrokerTopicMetrics\",name=\""
						+ topicName + "-MessagesInPerSec\"";
				// set BytesIn to topicData
				topicData.setBytesIn(topicDataFromTopic(topicBytesInObjName,
						jmxUtil));
				// set BytesOut to topicData
				topicData.setBytesOut(topicDataFromTopic(topicBytesOutObjName,
						jmxUtil));
				// set MsgIn to topicData
				topicData.setMsgIn(topicDataFromTopic(topicMsgInObjName,
						jmxUtil));
				// get PartiitonMap for the topic
				Map<String, PartitionData> partitionMap = getPartitionDetail(
						topicName, jmxUtil);
				topicData.setPartitionData(partitionMap);
				topicsDataMap.put(topicName, topicData);

			}
		}
		return topicsDataMap;
	}

	/**
	 * Topic data.
	 * 
	 * @param str
	 *            the str
	 * @param jmxUtil
	 *            the jmx util
	 * @return the map
	 */
	private static Map<String, Object> topicDataFromTopic(String str,
			JmxUtil jmxUtil) {
		Map<String, Object> mapToReturn = null;
		try {
			// get object set matching with the given string from jmxUtil
			Set<?> objectSet = jmxUtil.getObjectSetFromPatternString(str);
			if (objectSet.size() != 0) {
				ObjectName objName = (ObjectName) objectSet.toArray()[0];
				// get Attributes(name,value pair) of each object of the
				// objectSet.
				mapToReturn = jmxUtil.getDoubleAttributes(objName);
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapToReturn;
	}

	/**
	 * Gets the topic name from obj name.
	 * 
	 * @param objName
	 *            the obj name
	 * @return the topic name from obj name
	 */
	private static String getPartitionNameFromObjName(String objName) {
		String partitionName = null;
		// get index of first comma from objName
		int delimIdx = objName.indexOf(',');
		// get substring from delimIdx to objName's length (name part of the
		// objName)
		objName = objName.substring(delimIdx + 1, objName.length());
		// get index of first equal(=) from objName
		delimIdx = objName.indexOf('=');
		// get lastIndexOf '-' from objName
		int secondDelimIdx = objName.lastIndexOf('-');

		// get the partitionName (e.g. ...,name="topic1-0-UnderReplicated") will
		// return topic1-0
		partitionName = objName.substring(delimIdx + 2, secondDelimIdx);
		return partitionName;
	}

	/**
	 * Gets the partition detail.
	 * 
	 * @param topicName
	 *            the topic name
	 * @param jmxUtil
	 *            the jmx util
	 * @return the partition detail
	 */
	private Map<String, PartitionData> getPartitionDetail(String topicName,
			JmxUtil jmxUtil) {
		// LogEndOffset Mbean for the partition
		String logEndOffSetPartitionString = "\"kafka.log\":type=\"Log\",name=\""
				+ topicName + "*-LogEndOffset\"";

		// numLogSegments Mbean for the partition
		String numLogSegmentsString = "\"kafka.log\":type=\"Log\",name=\""
				+ topicName + "*-NumLogSegments\"";

		// pattern string made as per topic name
		String patternString = topicName + "\\-[^\\-]*-LogEndOffset\"";

		Map<String, PartitionData> partitionMap = new HashMap<String, PartitionData>();
		PartitionData partitionData = new PartitionData();
		try {
			// compile the patternString
			Pattern pattern = Pattern.compile(patternString,
					Pattern.CASE_INSENSITIVE);

			// get objectSet from jmxUtil for the given string of logEndOffset
			Set logEndOffsetString = jmxUtil
					.getObjectSetFromPatternString(logEndOffSetPartitionString);
			// get objectSet from jmxUtil for the given string of numLogSegments
			Set numLogSegmentsSet = jmxUtil
					.getObjectSetFromPatternString(numLogSegmentsString);
			// assign logEndOffset Set to an iterator logEndOffsetIterator
			Iterator logEndOffsetIterator = logEndOffsetString.iterator();
			// assign numLogSegments Set to an iterator numLogSegIterator
			Iterator numLogSegIterator = numLogSegmentsSet.iterator();
			while (logEndOffsetIterator.hasNext()) {
				// get logEndOffsetObj(objectName) from logEndOffsetIterator
				ObjectName logEndOffsetObj = (ObjectName) logEndOffsetIterator
						.next();
				ObjectName numLogSegObj = (ObjectName) numLogSegIterator.next();

				// match the logEndOffsetObj(objectName) with the patternString
				Matcher matcher = pattern.matcher(logEndOffsetObj.toString());
				// if matches
				if (matcher.find()) {
					// get "Value" attribute of logEndOffsetObj
					Long logEndOffsetInteger = (Long) jmxUtil.getAttribute(
							logEndOffsetObj, "Value");
					// get "Value" attribute of numLogSegObj
					Integer numLogSegmentInteger = (Integer) jmxUtil
							.getAttribute(numLogSegObj, "Value");
					partitionData.setLogEndOffset(logEndOffsetInteger);
					partitionData.setNumLogSegments(numLogSegmentInteger);
					partitionMap.put(
							getPartitionNameFromObjName(logEndOffsetObj
									.toString()), partitionData);
				} else {
					continue;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return partitionMap;
	}

	/**
	 * Gets the topic partition map.
	 * 
	 * @param jmxUtil
	 *            the jmx util
	 * @return the topic partition map
	 */
	private static Map<String, Set> getTopicPartitionMap(JmxUtil jmxUtil) {
		// topicPartitionMap to return
		Map<String, Set> topicPartitionMap = null;
		try {
			// get objectSet from jmxUtil for the matching string of
			// partitionUnderReplicatedString
			Set objectSet = jmxUtil
					.getObjectSetFromPatternString(partitionUnderReplicatedString);

			if (objectSet.size() != 0) {
				topicPartitionMap = new HashMap<String, Set>();
			}

			// create an iterator
			Iterator iterator = objectSet.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				// convert obj to String
				// Lets take e.g. objName =
				// "kafka.cluster":type="Partition",name="topic1-0-UnderReplicated"
				String objName = obj.toString();
				// get index of first comma
				int delimIdx = objName.indexOf(',');
				// get substring ,we get, name="topic1-0-UnderReplicated"
				objName = objName.substring(delimIdx, objName.length());
				// get index of '='
				delimIdx = objName.indexOf('=');
				// get index pf '-'
				int secondDelimIdx = objName.lastIndexOf('-');
				// we get, topic1-0
				objName = objName.substring(delimIdx + 2, secondDelimIdx);
				// get index pf '-'
				delimIdx = objName.lastIndexOf('-');
				// we get, topic1
				String topicName = objName.substring(0, delimIdx);
				// we get, 0
				String partitionNo = objName.substring(delimIdx + 1);

				// partitionSet contains
				// (topicName->partitionNo1,partitionNo2,partitionNo3) etc.
				Set partitionSet = (Set) topicPartitionMap.get(topicName);
				// initialize partitionSet only once
				if (partitionSet == null) {
					partitionSet = new HashSet();
				}
				partitionSet.add(partitionNo);
				topicPartitionMap.put(topicName, partitionSet);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topicPartitionMap;
	}

	/**
	 * nodetiles
	 */
	public void nodetiles() {

		// list of generic nodeTiles
		List<TileInfo> nodeTiles = super.genericNodeTiles();
		// get nodeIP from parameterMap.
		String nodeIP = (String) parameterMap.get(Constant.Keys.IP);
		KafkaConf conf = (KafkaConf) this.dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.KAFKA);
		String offlinePartitionsString = "\"kafka.controller\":type=\"KafkaController\",name=\"OfflinePartitionsCount\"";
		String activeControllerCountString = "\"kafka.controller\":type=\"KafkaController\",name=\"ActiveControllerCount\"";
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		TileInfo tile = new TileInfo();
		try {
			JmxUtil jmxUtil = new JmxUtil(nodeIP, (Integer.parseInt(conf
					.getJmxPort())));
			MBeanServerConnection connection = jmxUtil.connect();
			if (connection != null) {
				// get offlinePartitions Value
				Integer line1 = (Integer) jmxUtil.getAttribute(new ObjectName(
						offlinePartitionsString), "Value");
				// offlinePartitions Tile
				tile = new TileInfo(line1.toString(),
						"Offline Partitions Count", null, null,
						Constant.Status.NORMAL, null);
				// add to tiles
				tiles.add(tile);
				// get activeControllerCount Value
				line1 = (Integer) jmxUtil.getAttribute(new ObjectName(
						activeControllerCountString), "Value");
				// activeControllerCount Tile
				tile = new TileInfo(line1.toString(),
						"Active Controller Count", null, null,
						Constant.Status.NORMAL, null);
				// add to tiles
				tiles.add(tile);
				// get leaderCount Value
				line1 = (Integer) jmxUtil.getAttribute(new ObjectName(
						leaderCountString), "Value");
				// leaderCount Tile
				tile = new TileInfo(line1.toString(), "Leader Count", null,
						null, Constant.Status.NORMAL, null);
				// add to tiles
				tiles.add(tile);
				// get topicPartitionCount Map
				Map<String, Integer> topicPartitionCountMap = getTopicAndPartitionCountMap(jmxUtil);
				// Topics COUNT tile
				tile = new TileInfo(topicPartitionCountMap.get("topicCount")
						.toString(), "Topics", null, null,
						Constant.Status.NORMAL, null);
				// add to tiles
				tiles.add(tile);
				// partition COUNT tile
				tile = new TileInfo(topicPartitionCountMap
						.get("partitionCount").toString(), "Partitions", null,
						null, Constant.Status.NORMAL, null);
				// add to tiles
				tiles.add(tile);
			} else {
				//add error tile
				tile = new TileInfo("Unavailable", "JMX Connection", nodeIP,
						null, Constant.Tile.Status.ERROR, null);
				tiles.add(tile);
			}
			result.put("tiles", tiles);
		} catch (MalformedObjectNameException e) {
			logger.debug(e.getMessage());
		} catch (NullPointerException e) {
			tiles.add(getJmxErrorTile(nodeIP));
			logger.debug(e.getMessage());
		} catch (NumberFormatException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			tiles.add(getJmxErrorTile(nodeIP));
			logger.debug(e.getMessage());
		}
		// add nodeTiles to tiles.
		tiles.addAll(nodeTiles);
		result.put("tiles", tiles);
	}
	
	/**
	 * Gets the jmx error tile.
	 *
	 * @param nodeIP the node ip
	 * @return the jmx error tile
	 */
	private TileInfo getJmxErrorTile(String nodeIP){
		TileInfo tileInfo = new TileInfo("Unavailable", "Monitoring Data", nodeIP,
				null, Constant.Tile.Status.ERROR, null);
		return tileInfo;
	}
	/**
	 * Gets the topic and partition count map.
	 * 
	 * @param jmxUtil
	 *            the jmx util
	 * @return the topic and partition count map
	 */
	private Map<String, Integer> getTopicAndPartitionCountMap(JmxUtil jmxUtil) {
		// get topicPartiitonMap
		Map<String, Set> topicPartitionMap = getTopicPartitionMap(jmxUtil);
		Map<String, Integer> mapToReturn = new HashMap<String, Integer>();
		int topicCount = 0;
		int partiionCount = 0;
		// if topicPartitionMap is not null
		if (topicPartitionMap != null) {
			// iterate over topicPartitionMap
			for (String key : topicPartitionMap.keySet()) {
				// increase topicCount
				topicCount++;
				// add No of partitions for each topic(key) to current
				// partitionCount value
				partiionCount += topicPartitionMap.get(key).size();
			}
		}
		mapToReturn.put("topicCount", topicCount);
		mapToReturn.put("partitionCount", partiionCount);
		return mapToReturn;
	}

	/**
	 * Creates the topic.
	 */
	private void createTopic() {
		String topicCreationFailureString = "creation failed";

		// get topicMetadata from parameterMap from the post json
		Map<String, Object> parameterMap = (Map<String, Object>) this.parameterMap
				.get("topicMetadata");
		TopicMetadata topicMetadata = new TopicMetadata();

		try {
			// map parameterMap to TopicMetadata
			topicMetadata = JsonMapperUtil.objectFromMap(parameterMap,
					TopicMetadata.class);
			// get zConf
			ZookeeperConf zConf = (ZookeeperConf) this.clusterConfig
					.getClusterComponents().get(
							Constant.Component.Name.ZOOKEEPER);
			// get kafkaConf
			KafkaConf kafkaConf = (KafkaConf) this.clusterConfig
					.getClusterComponents().get(Constant.Component.Name.KAFKA);

			// taking fist kafka node as the hostName as it is gangliaMaster
			// node
			String hostname = kafkaConf.getNodes().get(0).getPublicIp();

			String username = kafkaConf.getUsername();
			String password = kafkaConf.getPassword();
			String privateKey = kafkaConf.getPrivateKey();

			// zookeeper clientPort
			int clientPort = zConf.getClientPort();

			List<NodeConf> zookeeperNodes = zConf.getNodes();

			// list to add zookeeperNode:clientPort
			List<String> zkNodes = new ArrayList<String>();

			// add clientPort to each zookeeperNode seperated by Colon
			for (NodeConf node : zookeeperNodes) {
				zkNodes.add(node.getPublicIp() + STR_COLON + clientPort);
			}

			// convert zookeeperNodeList to zookeeperNodesString
			String zookeeperNodesString = StringUtils.join(zkNodes, ",");

			// create Topic Command
			String createTopicCommand = kafkaConf.getComponentHome()
					+ createTopicString + partition
					+ topicMetadata.getPartitionCount() + STR_SPACE + replica
					+ topicMetadata.getReplicas() + STR_SPACE + topic
					+ topicMetadata.getTopicName() + STR_SPACE + zookeeper
					+ zookeeperNodesString;
			logger.info(hostname, "going to execute createTopicCommand..."
					+ createTopicCommand);

			// execute createTopicCommand
			Map outputMap = SSHUtils.getCommandOutputAndError(
					createTopicCommand, hostname, kafkaConf.getUsername(),
					clusterConfig.isAuthTypePassword() ? password : privateKey,
					clusterConfig.isAuthTypePassword());
			// get command output
			String output = (String) outputMap.get(Constant.Keys.OUTPUT);
			// get command status
			Boolean status = (Boolean) outputMap.get(Constant.Keys.STATUS);
			// if command executed successfully
			if (status) {
				// if topic is successfully created
				if (output.equals(KafkaClusterMonitor.createTopicSuccessString)) {
					result.put("output",
							KafkaClusterMonitor.createTopicSuccessString);
				} else if (output.contains(topicCreationFailureString)) {
					errors.add(output);
				}
			} else {
				addError(output);
			}
		} catch (IllegalArgumentException e1) {
			// add topicCreationFailureString in case of any Exception
			errors.add(topicCreationFailureString);
		} catch (IllegalAccessException e1) {
			errors.add(topicCreationFailureString);
		} catch (InstantiationException e1) {
			errors.add(topicCreationFailureString);
		} catch (InvocationTargetException e1) {
			errors.add(topicCreationFailureString);
		} catch (Exception e) {
			errors.add(topicCreationFailureString);
		}
	}
}
