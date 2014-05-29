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
package com.impetus.ankush.common.framework;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.security.core.context.SecurityContextHolder;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.Graph.StartTime;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.DeployableConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.framework.config.NodeDiskInfo;
import com.impetus.ankush.common.framework.config.NodeMemoryInfo;
import com.impetus.ankush.common.ganglia.Graph;
import com.impetus.ankush.common.ganglia.LiveGraph;
import com.impetus.ankush.common.ganglia.NodeGraph;
import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.service.impl.AppConfServiceImpl;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.hadoop.GenericRackAwareness;

/**
 * The Class AbstractMonitor.
 */
public abstract class AbstractMonitor {

	/* Logger object */
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(AbstractMonitor.class);

	/** Generic log master. */
	protected static GenericManager<Log, Long> logManager = AppStoreWrapper
			.getManager(Constant.Manager.LOG, Log.class);

	// monitoring manager.
	/** The monitoring manager. */
	private GenericManager<NodeMonitoring, Long> monitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	// cluster manager.
	/** The cluster manager. */
	protected GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** App Conf service **/
	private AppConfService appConfService = AppStoreWrapper.getService(
			Constant.Service.APPCONF, AppConfServiceImpl.class);

	/* Error list */
	/** The errors. */
	protected List<String> errors = new ArrayList<String>();

	/* Store result */
	/** The result. */
	protected Map<String, Object> result = new HashMap<String, Object>();

	// cluster object
	/** The db cluster. */
	protected Cluster dbCluster;

	/** The cluster config. */
	protected ClusterConf clusterConfig;

	// parameter map.
	/** The parameter map. */
	protected Map parameterMap;

	/** The graph. */
	protected Graph graph;

	// formator
	/** The formator. */
	DecimalFormat formator = new DecimalFormat("##");

	/**
	 * Monitor.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param action
	 *            the action
	 * @param parameterMap
	 *            the parameter map
	 * @return the map
	 */
	public Map monitor(Cluster cluster, String action, Map parameterMap) {
		this.dbCluster = cluster;
		this.parameterMap = parameterMap;
		result.clear();
		String errMsg = "Exception: Unable to process request.";
		try {

			// Setting the cluster name to logger.
			logger.setClusterName(cluster.getName());
			this.clusterConfig = dbCluster.getClusterConf();
			Method method = null;
			try {
				// Create method object using the action name.
				method = this.getClass().getDeclaredMethod(action);
			} catch (NoSuchMethodException e) {
				method = this.getClass().getMethod(action);
			} catch (Exception e) {
				throw e;
			}
			// setting accessibility true.
			method.setAccessible(true);
			// invoking the method.
			method.invoke(this);
		} catch (NoSuchMethodException e) {
			addError("No such method found.");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e.getMessage() != null) {
				addError(e.getMessage());
			} else {
				addError(e.getCause().getMessage());
			}
			// Adding and logging error
			addAndLogError(errMsg, e);
		}
		return returnResult();
	}

	public void addAndLogError(String errorMsg) {
		addError(errorMsg);
		logger.error(errorMsg);
	}

	public void addAndLogError(String errorDisplayMsg, String errorLogMsg) {
		addError(errorDisplayMsg);
		logger.error(errorDisplayMsg);
		logger.error(errorLogMsg);
	}

	public void addAndLogError(String errorDisplayMsg, Exception e) {
		addError(errorDisplayMsg);
		logger.error(errorDisplayMsg);
		if (e.getMessage() != null) {
			logger.error(e.getMessage());
		} else {
			logger.error(e.getCause().getMessage());
		}
	}

	/**
	 * Adds the error.
	 * 
	 * @param error
	 *            the error
	 */
	protected void addError(String error) {
		this.errors.add(error);
	}

	/**
	 * Return result.
	 * 
	 * @return the map
	 */
	protected Map returnResult() {
		// cluster state is returning as server crashed then mark it as error.
		if (result.containsKey(Constant.Keys.STATE)) {
			// getting state.
			String state = (String) result.get(Constant.Keys.STATE);
			// if state is server crashed.
			if (state.equals(Constant.Cluster.State.SERVER_CRASHED)) {
				// setting state as error.
				result.put(Constant.Keys.STATE, Constant.Cluster.State.ERROR);
			}
		}
		// if error is empth then set status as true else false
		if (this.errors.isEmpty()) {
			this.result.put(Constant.Keys.STATUS, true);
		} else {
			this.result.put(Constant.Keys.STATUS, false);
			this.result.put(Constant.Keys.ERROR, this.errors);
		}
		return this.result;
	}

	/**
	 * Return node graph.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void nodegraph() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();

		// creating node graph object.
		NodeGraph nodeGraph = new NodeGraph(conf.getGangliaMaster()
				.getPublicIp(), conf.getUsername(), conf.getPassword(),
				conf.getPrivateKey(), dbCluster.getName());

		// checking existance of ip and start time.
		if (!parameterMap.containsKey(Constant.Keys.IP)
				|| !parameterMap.containsKey(Constant.Keys.STARTTIME)) {
			throw new Exception("Either ip or start time is missing.");
		}

		// getting starttime
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(Constant.Keys.STARTTIME)).toLowerCase());

		// getting ip address.
		String ip = (String) parameterMap.get(Constant.Keys.IP);

		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}
		// putting the extracted data into the json.
		result.putAll(nodeGraph.extractRRD(startTime, ip));
	}

	/**
	 * Return cluster graphs.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void clustergraph() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				dbCluster.getName());

		// checking existence of start time.
		if (!parameterMap.containsKey(Constant.Keys.STARTTIME)) {
			throw new Exception("Start time is missing.");
		}

		/** Cluster Graph rrd files. **/
		Map<String, String> graphs = null;
		graphs = new HashMap<String, String>();
		graphs.put("cpu", "cpu_*.rrd");
		graphs.put("memory", "mem_*.rrd");
		graphs.put("network", "bytes_*.rrd");
		graphs.put("load", "load_*.rrd proc_run.rrd");
		// getting starttime
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(Constant.Keys.STARTTIME)).toLowerCase());

		// putting the extracted data into the json.
		for (String pattern : graphs.keySet()) {
			result.put(pattern,
					graph.extractRRD(startTime, graphs.get(pattern)));
		}
	}

	/**
	 * Return graph data.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void graphs() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				dbCluster.getName());

		// checking existence of ip and start time.
		if (!parameterMap.containsKey(Constant.Keys.PATTERN)
				&& !parameterMap.containsKey(Constant.Keys.STARTTIME)) {
			throw new Exception("Either pattern or start time is missing.");
		}

		// getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(Constant.Keys.STARTTIME)).toLowerCase());

		// pattern
		String pattern = (String) parameterMap.get(Constant.Keys.PATTERN);
		// node ip.
		String ip = null;
		if (parameterMap.containsKey(Constant.Keys.IP)) {
			ip = (String) parameterMap.get(Constant.Keys.IP);
		}
		// putting the extracted data into the json.
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}
		if (ip != null) {
			result.putAll(graph.extractRRD(ip, startTime, pattern));
		} else {
			result.putAll(graph.extractRRD(startTime, pattern));
		}
	}

	/**
	 * Allgraphs.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void allgraphs() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				dbCluster.getName());

		// checking existence of ip and start time.
		if (!parameterMap.containsKey(Constant.Keys.PATTERN)
				|| !parameterMap.containsKey(Constant.Keys.STARTTIME)) {
			throw new Exception("Either pattern or start time is missing.");
		}

		// getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(Constant.Keys.STARTTIME)).toLowerCase());

		// pattern
		String pattern = (String) parameterMap.get(Constant.Keys.PATTERN);

		result.putAll(graph.extractAllRRD(startTime, pattern));
	}

	/**
	 * Return graph data.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void sparkline() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();

		// creating node graph object.
		boolean update = Boolean.parseBoolean((String) parameterMap
				.get("update"));

		LiveGraph graph = new LiveGraph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				dbCluster.getName(), update);

		// checking existence of ip and start time.
		if (!parameterMap.containsKey(Constant.Keys.STARTTIME)) {
			throw new Exception("Start time is missing.");
		}

		// getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(Constant.Keys.STARTTIME)).toLowerCase());

		// pattern
		// node ip.
		String ip = null;
		if (parameterMap.containsKey(Constant.Keys.IP)) {
			ip = (String) parameterMap.get(Constant.Keys.IP);
		}
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}
		// putting the extracted data into the json.
		if (ip != null) {
			result.putAll(graph.extractRRD(ip, startTime, dbCluster));
		} else {
			result.putAll(graph.extractRRD(startTime, dbCluster));
		}
	}

	/**
	 * Gets the logs.
	 * 
	 * @return the logs
	 */
	public Map getLogs() {
		int lastLog = 0;
		if (parameterMap.containsKey(Constant.Keys.LASTLOG)) {
			lastLog = ParserUtil.getIntValue(
					(String) parameterMap.get(Constant.Keys.LASTLOG), 0);
		}
		String ip = null;

		if (parameterMap.containsKey(Constant.Keys.IP)) {
			ip = (String) parameterMap.get(Constant.Keys.IP);
		}

		// creating empty map.
		Object clusterId = dbCluster.getId();

		// Getting last operation id.
		List<Log> lastOperationLogs = logManager.getAllByNamedQuery(
				"getLastOperationId",
				Collections.singletonMap(Constant.Keys.CLUSTERID, clusterId));

		if (lastOperationLogs == null || lastOperationLogs.isEmpty()) {
			addError("Unable to get operation id from the logs.");
			return Collections.EMPTY_MAP;
		}

		Long operationId = lastOperationLogs.get(lastOperationLogs.size() - 1)
				.getOperationId();

		// Creating Empty property/value map.
		Map<String, Object> propertyValueMap = new HashMap<String, Object>();

		// putting cluster id.
		propertyValueMap.put(Constant.Keys.CLUSTERID, dbCluster.getId());

		// putting host public ip
		if (ip != null && !ip.isEmpty()) {
			propertyValueMap.put(Constant.Keys.HOST, ip);

		}

		// putting last operation id.
		propertyValueMap.put(Constant.Keys.OPERATIONID, operationId);

		List<Log> logs = logManager.getAllByPropertyValue(propertyValueMap,
				Constant.Keys.ID);

		logs.subList(0, lastLog).clear();

		lastLog = lastLog + logs.size();

		Map map = new HashMap();
		map.put(Constant.Keys.LOGS, logs);
		map.put(Constant.Keys.LASTLOG, lastLog);
		map.put(Constant.Keys.STATE, dbCluster.getState());
		return map;
	}

	/**
	 * Method to get the activity progress logs.
	 * 
	 * @return Map of all progress logs.
	 */
	public void logs() {
		result.putAll(getLogs());
	}

	/**
	 * Memoryheatmap.
	 */
	public void memoryheatmap() {
		// cluster nodes
		List<Node> nodes = dbCluster.getSortedNodesByIp();

		// nodes data
		List<Map> nodesData = new ArrayList<Map>();
		// rack node count map.
		Map rackNodeCountMap = new HashMap();
		// list of state which needs to be included for creating the heat map
		// data.
		List<String> includeStates = new ArrayList<String>();
		// adding deployed state for node
		includeStates.add(Constant.Node.State.DEPLOYED);
		// adding removing state for node
		includeStates.add(Constant.Node.State.REMOVING);
		// iterating over the nodes
		for (Node node : nodes) {

			// If state id adding then no need to send the heat map data.
			if (!includeStates.contains(node.getState())) {
				continue;
			}
			Map<String, Object> nodeData = new HashMap<String, Object>();

			// getting node monitoring data.
			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(Constant.Keys.NODEID,
							node.getId());

			String usageValue = null;
			boolean isAgentDown = true;
			// if node monitoring, its monitoring info and its up time info is
			// not null
			if (nodeMonitoring != null
					&& nodeMonitoring.getMonitoringInfo() != null
					&& nodeMonitoring.getMonitoringInfo().getMemoryInfos() != null) {
				// set agent down status.
				isAgentDown = nodeMonitoring.isAgentDown();
				// get usage value.
				Double usageValueDouble = nodeMonitoring.getMonitoringInfo()
						.getMemoryInfos().get(0).getUsedPercentage();
				// current usage value.
				if (usageValueDouble != null) {
					usageValue = formator.format(usageValueDouble).toString();
				}
			}

			// Getting the status value for the CPU Usage
			EventManager eventManager = new EventManager();
			Event event = eventManager.getEvent(node.getPublicIp(),
					Constant.Alerts.Usage.MEMORY);

			// Getting the severity value.
			String status = Constant.Alerts.Severity.NORMAL;
			if (event != null) {
				status = event.getSeverity();
			}

			// if agent is down making status as unavailable.
			if (isAgentDown) {
				usageValue = "0";
				status = Constant.Alerts.Severity.UNAVAILABLE;
			}

			// Getting rack info for node.
			String rackId = GenericRackAwareness.getRackId(node);
			// setting node and rack information in node data
			nodeData.put(Constant.Keys.ID, node.getId());
			nodeData.put(Constant.Keys.RACKID, rackId);
			nodeData.put(Constant.Keys.NODEIP, node.getPublicIp());
			nodeData.put(Constant.Keys.VALUE, usageValue);
			nodeData.put(Constant.Keys.STATUS, status.toLowerCase());
			// adding node data to nodes data
			nodesData.add(nodeData);
			// Getting node rack count.
			Integer rackNodeCount = (Integer) rackNodeCountMap.get(rackId);
			// if rack count is null
			if (rackNodeCount == null) {
				rackNodeCount = 0;
			}
			// Putting node count.
			rackNodeCountMap.put(rackId, ++rackNodeCount);
		}
		// rack info data
		List<Map> rackInfo = new ArrayList<Map>();

		for (Object rackId : rackNodeCountMap.keySet()) {
			logger.debug("rackId : " + rackId);
			Map<String, Object> rackData = new HashMap<String, Object>();
			rackData.put(Constant.Keys.RACKID, rackId);
			rackData.put(Constant.Keys.TOTALNODE, rackNodeCountMap.get(rackId));
			rackInfo.add(rackData);
		}
		// setting rack info in map.
		result.put(Constant.Keys.RACKINFO, rackInfo);
		// setting node data.
		result.put(Constant.Keys.NODEDATA, nodesData);
		// setting total rack.
		result.put(Constant.Keys.TOTALRACK, rackInfo.size());
	}

	/**
	 * Cpu heat map.
	 */
	public void cpuheatmap() {
		// cluster nodes
		List<Node> nodes = dbCluster.getSortedNodesByIp();
		Map rackNodeCountMap = new HashMap();

		// nodes data
		LinkedList<Map> nodesData = new LinkedList<Map>();
		// list of state which needs to be included for creating the heat map
		// data.
		List<String> includeStates = new ArrayList<String>();
		// adding deployed state for node
		includeStates.add(Constant.Node.State.DEPLOYED);
		// adding removing state for node
		includeStates.add(Constant.Node.State.REMOVING);
		// iterating over the nodes.
		for (Node node : nodes) {

			// if the node state is available in including state list.
			if (!includeStates.contains(node.getState())) {
				continue;
			}
			// node data map
			Map<String, Object> nodeData = new HashMap<String, Object>();

			// node monitoring object.
			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(Constant.Keys.NODEID,
							node.getId());

			// usage value.
			String usageValue = null;
			// flag for agent down status
			boolean isAgentDown = true;
			// if node monitoring, its monitoring info and its up time info is
			// not null
			if (nodeMonitoring != null
					&& nodeMonitoring.getMonitoringInfo() != null
					&& nodeMonitoring.getMonitoringInfo().getUptimeInfos() != null) {
				// set agent down status.
				isAgentDown = nodeMonitoring.isAgentDown();
				// get usage value.
				Double usageValueDouble = nodeMonitoring.getMonitoringInfo()
						.getUptimeInfos().get(0).getCpuUsage();
				// current usage value.
				if (usageValueDouble != null) {
					usageValue = formator.format(usageValueDouble).toString();
				}
			}

			// Getting the status value for the CPU Usage
			EventManager eventManager = new EventManager();
			// Getting the event for the node.
			Event event = eventManager.getEvent(node.getPublicIp(),
					Constant.Alerts.Usage.CPU);

			// Getting the severity value.
			String status = Constant.Alerts.Severity.NORMAL;
			if (event != null) {
				status = event.getSeverity();
			}

			// if agent is down making status as unavailable.
			if (isAgentDown) {
				usageValue = "0";
				status = Constant.Alerts.Severity.UNAVAILABLE;
			}

			// Getting rack info for node.
			String rackId = GenericRackAwareness.getRackId(node);
			// Setting node and rack info in node data.
			nodeData.put(Constant.Keys.ID, node.getId());
			nodeData.put(Constant.Keys.RACKID, rackId);
			nodeData.put(Constant.Keys.NODEIP, node.getPublicIp());
			nodeData.put(Constant.Keys.VALUE, usageValue);
			nodeData.put(Constant.Keys.STATUS, status.toLowerCase());
			// Adding node data in nodes data.
			nodesData.add(nodeData);
			// Getting rack node count
			Integer rackNodeCount = (Integer) rackNodeCountMap.get(rackId);
			if (rackNodeCount == null) {
				rackNodeCount = 0;
			}
			rackNodeCountMap.put(rackId, ++rackNodeCount);
		}
		// rack info data
		List<Map> rackInfo = new ArrayList<Map>();

		// Iterating over the rack node count map.
		for (Object rackId : rackNodeCountMap.keySet()) {
			logger.debug("rackId : " + rackId);
			// Creating rack data object.
			Map<String, Object> rackData = new HashMap<String, Object>();
			// Adding rackid and total node count in map.
			rackData.put(Constant.Keys.RACKID, rackId);
			rackData.put(Constant.Keys.TOTALNODE, rackNodeCountMap.get(rackId));
			// Adding rack data data to main rack info list
			rackInfo.add(rackData);
		}
		// setting rack info in map.
		result.put(Constant.Keys.RACKINFO, rackInfo);
		// setting node data.
		result.put(Constant.Keys.NODEDATA, nodesData);
		// setting total rack.
		result.put(Constant.Keys.TOTALRACK, rackInfo.size());
	}

	/**
	 * Gets the monitoring info.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return the monitoring info
	 */
	public void getMonitoringInfo(Cluster cluster) {
		Map map = new HashMap();
		for (Node node : cluster.getNodes()) {

			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(Constant.Keys.NODEID,
							cluster.getId());
			map.put(node.getPublicIp(), nodeMonitoring);
		}
		result.putAll(map);
	}

	/**
	 * Method to get the cluster evnets.
	 * 
	 * @return the map
	 * @author hokam
	 */
	public Map events() {
		// event manager object.
		EventManager eventManager = new EventManager();
		// Getting events by cluster id.
		List<Event> events = eventManager.getEvents(dbCluster.getId());
		// setting events in result map.
		result.put(Constant.Keys.EVENTS, events);
		// returning events.
		return returnResult();
	}

	/**
	 * Services.
	 */
	public void services() {
		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

		// Get the db node info using public IP
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, nodeIp);
		if (node != null) {
			Long nodeId = node.getId();

			// Get the db node monitoring info
			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(Constant.Keys.NODEID, nodeId);
			if (nodeMonitoring != null) {
				// Getting service status.
				Map<String, Boolean> serviceStatusMap = nodeMonitoring
						.getServiceStatus();
				Map<String, Boolean> roleStatusMap = new HashMap<String, Boolean>();
				for (String serviceName : serviceStatusMap.keySet()) {
					String role = Constant.RoleProcessName
							.getRoleName(serviceName);

					// For Cassandra, display service name as cassandra daemon
					// Needs to be handled during Service Management
					// Architecture modification
					if (serviceName
							.equals(Constant.Component.ProcessName.CASSANDRA)) {
						role = serviceName;
					}

					if ((role != null) && (!role.isEmpty())) {
						roleStatusMap.put(role,
								serviceStatusMap.get(serviceName));
					} else {
						roleStatusMap.put(serviceName,
								serviceStatusMap.get(serviceName));
					}
				}
				result.putAll(roleStatusMap);
			} else {
				// making service status as false for agent.
				result.put(Constant.Keys.AGENT, false);
			}
		} else {
			addError("Node with public " + nodeIp + " does not exists.");
		}
	}

	/**
	 * Categories.
	 * 
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	public Map categories() throws Exception {
		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

		// creating node graph object.
		graph = new Graph(this.clusterConfig.getGangliaMaster().getPublicIp(),
				this.clusterConfig.getUsername(),
				this.clusterConfig.getPassword(),
				this.clusterConfig.getPrivateKey(),
				this.clusterConfig.getClusterName());

		Map<String, String> categories = new HashMap<String, String>();
		categories.put("cpu", "cpu_.*\\.rrd");
		categories.put("disk", "disk_.*\\.rrd");
		categories.put("memory", "mem_.*\\.rrd");
		categories.put("load", "load_.*\\.rrd");
		categories.put("network", "(bytes|pkts)_.*\\.rrd");
		categories.put("process", "proc_.*\\.rrd");
		categories.put("swap", "swap_.*\\.rrd");
		for (String componentName : this.clusterConfig.getClusterComponents()
				.keySet()) {
			String[] processList = JmxMonitoringUtil
					.getProcessList(componentName);
			if (processList != null) {
				for (String processName : processList) {
					categories.put(componentName + "-" + processName,
							"ankush_" + componentName.toLowerCase() + "_"
									+ processName.toLowerCase() + ".*\\.rrd");
				}
			}
		}
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, nodeIp);
		if (node != null) {
			nodeIp = node.getPrivateIp();
		}
		List<String> files = graph.getAllFiles(nodeIp);
		Map cateMap = new HashMap();
		for (String category : categories.keySet()) {
			Map map = new HashMap();
			List<String> matchingFiles = graph.getMatchingFiles(
					categories.get(category), files);
			map.put("count", matchingFiles.size());
			map.put("legends", matchingFiles);
			map.put("pattern", categories.get(category));
			map.put("ip", nodeIp);
			if (matchingFiles.size() != 0) {
				cateMap.put(category, map);
			}

		}
		result.put("categories", cateMap);
		return result;
	}

	/**
	 * Gets the node usage map.
	 * 
	 * @param nodeMonitoring
	 *            the node monitoring
	 * @return the node usage map
	 */
	protected Map getNodeUsageMap(NodeMonitoring nodeMonitoring) {
		Map nodeMap = new HashMap();
		nodeMap.put("cpuUsage", "NA");
		nodeMap.put("usedMemory", "NA");
		nodeMap.put("totalMemory", "NA");
		nodeMap.put("freeMemory", "NA");
		nodeMap.put("totalDisk", "NA");
		nodeMap.put("usedDisk", "NA");
		nodeMap.put("freeDisk", "NA");
		try {
			formator = new DecimalFormat(".##");
			if (nodeMonitoring != null
					&& nodeMonitoring.getMonitoringInfo() != null) {
				// adding cpu usage.
				nodeMap.put("cpuUsage", nodeMonitoring.getMonitoringInfo()
						.getUptimeInfos().get(0).getCpuUsage());

				// adding memory usage.
				NodeMemoryInfo memInfo = nodeMonitoring.getMonitoringInfo()
						.getMemoryInfos().get(0);
				nodeMap.put("usedMemory", formator.format(memInfo
						.getActualUsed().doubleValue() / 1024 / 1024 / 1024));
				nodeMap.put("totalMemory", formator.format(memInfo.getTotal()
						.doubleValue() / 1024 / 1024 / 1024));
				nodeMap.put("freeMemory", formator.format(memInfo
						.getActualFree().doubleValue() / 1024 / 1024 / 1024));

				// calculating the total, used and free disk.
				double totalDisk = 0d;
				double usedDisk = 0d;
				double freeDisk = 0d;
				for (NodeDiskInfo diskInfo : nodeMonitoring.getMonitoringInfo()
						.getDiskInfos()) {
					totalDisk = totalDisk
							+ diskInfo.getTotalMemory().doubleValue();
					usedDisk = usedDisk
							+ diskInfo.getUsedMemory().doubleValue();
					freeDisk = freeDisk
							+ diskInfo.getFreeMemory().doubleValue();
				}
				nodeMap.put("totalDisk",
						formator.format(totalDisk / 1024 / 1024));
				nodeMap.put("usedDisk", formator.format(usedDisk / 1024 / 1024));
				nodeMap.put("freeDisk", formator.format(freeDisk / 1024 / 1024));
			}
		} catch (Exception e) {
			addError("Unable to get the usage values.");
		}
		return nodeMap;
	}

	/**
	 * Graphtree.
	 */
	public void graphtree() {
		// getting the node ip from map.
		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);
		try {
			// creating node graph object.
			graph = new Graph(this.clusterConfig.getGangliaMaster()
					.getPublicIp(), this.clusterConfig.getUsername(),
					this.clusterConfig.getPassword(),
					this.clusterConfig.getPrivateKey(),
					this.clusterConfig.getClusterName());

			// putting the extracted data into the json.
			Node node = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodeIp);
			if (node != null) {
				nodeIp = node.getPrivateIp();
			}
			// getting all files from the node.
			List<String> files = graph.getAllFiles(nodeIp);

			TreeMap treeMap = populateTreeMap(files);
			// putting the treemap against tree key.
			result.put("tree", treeMap);
		} catch (Exception e) {
			addError(e.getMessage());
		}
	}

	private TreeMap populateTreeMap(List<String> files) {
		// empty tree map.
		TreeMap treeMap = new TreeMap();
		for (String path : files) {
			// replacing .rrd to empty string.
			String fileNameWtExtn = path.replace(".rrd", "");
			String[] parsedPath = null;
			// if the filename contains . then separating by DOT .
			if (fileNameWtExtn.contains(".")) {
				parsedPath = fileNameWtExtn.split("\\.", 2);
			} else if (fileNameWtExtn.contains("_")) {
				// if the filename contains "_" then separating by "_"
				parsedPath = fileNameWtExtn.split("_", 2);
			} else {
				parsedPath = new String[] { fileNameWtExtn };
			}
			// if parsetPath length is greater than 1.
			if (parsedPath.length > 1) {
				// populating the tree map.
				treePopulation(treeMap, parsedPath[0], parsedPath[1]);
			}
		}
		// convert null values map to list.
		convertToList(treeMap);
		return treeMap;
	}

	/**
	 * Method to get legends for the given pattern.
	 * 
	 * @throws Exception
	 */
	public void legends() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				dbCluster.getName());

		// checking existence of ip and start time.
		if (!parameterMap.containsKey(Constant.Keys.PATTERN)) {
			throw new Exception("Pattern is missing.");
		}

		// pattern
		String pattern = (String) parameterMap.get(Constant.Keys.PATTERN);
		// node ip.
		String ip = null;
		if (parameterMap.containsKey(Constant.Keys.IP)) {
			ip = (String) parameterMap.get(Constant.Keys.IP);
		}
		// putting the extracted data into the json.
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}

		// list of legends.
		List<String> legends = graph.getLegends(ip, pattern);

		// save graph view data.
		saveGraphViewData(legends, ip, true);
		// getting legends for the given pattern.
		result.put("legends", new TreeSet<String>(legends));
	}

	/**
	 * Method to save graph view data.
	 * 
	 * @param pattern
	 * @param legends
	 * @param publicIp
	 */
	private void saveGraphViewData(List<String> legends, String publicIp,
			boolean add) {
		// get Current user.
		User userDetails = getCurrentUser();
		// username.
		String userName = userDetails.getUsername();
		// monitoring manager objcet.
		MonitoringManager monitoringManager = new MonitoringManager();
		// node monitoring object.
		NodeMonitoring nodeMonitoring = monitoringManager
				.getMonitoringData(publicIp);

		HashMap userGraphViewMap = nodeMonitoring.getGraphViewData();
		// hash set of saved legends.
		HashSet graphViewData = (HashSet) userGraphViewMap.get(userName);
		// if set in null assign new hash set.
		if (graphViewData == null) {
			graphViewData = new HashSet();
		}

		if (add) {
			// adding new legends.
			graphViewData.addAll(legends);
		} else {
			// adding new legends.
			graphViewData.removeAll(legends);
		}

		userGraphViewMap.put(userName, graphViewData);
		// setting graphViewData.
		nodeMonitoring.setGraphViewData(userGraphViewMap);
		// saving in database
		monitoringManager.save(nodeMonitoring);
	}

	/**
	 * Method to set graph views in result map which is available in database.
	 * 
	 * @throws Exception
	 */
	public void graphviews() throws Exception {
		// get Current user.
		User userDetails = getCurrentUser();
		// username.
		String userName = userDetails.getUsername();
		// checking ip existance.
		if (parameterMap.containsKey(Constant.Keys.IP)) {
			// getting ip address.
			String ip = (String) parameterMap.get(Constant.Keys.IP);
			// getting montioring manager.
			MonitoringManager monitoringManager = new MonitoringManager();
			// getting monitoring object.
			NodeMonitoring nodeMonitoring = monitoringManager
					.getMonitoringData(ip);

			HashMap userGraphViewMap = nodeMonitoring.getGraphViewData();
			// hash set of saved legends.
			HashSet graphViewData = (HashSet) userGraphViewMap.get(userName);
			// getting saved set of legends for data.
			// if not null.
			if (graphViewData == null || graphViewData.isEmpty()) {
				graphViewData = new HashSet<String>();

				// Getting default saved node graphs.
				Map appConf = appConfService
						.getMetadata(Constant.Keys.NODEGRAPHS);

				if (appConf != null) {
					// Getting saved map object
					Map nodegraphsMap = (Map) appConf
							.get(Constant.Keys.NODEGRAPHS);
					// Getting the list of saved graphs.
					graphViewData.addAll((List) nodegraphsMap
							.get(Constant.Keys.DEFAULTGRAPHS));
					// put it against the user
					userGraphViewMap.put(userName, graphViewData);
					// set it to node monitoring object.
					nodeMonitoring.setGraphViewData(userGraphViewMap);
					// and save the default metrics in db.
					monitoringManager.save(nodeMonitoring);
				}
			}
			result.put(Constant.Keys.LIST, new TreeSet<String>(graphViewData));
		} else {
			throw new Exception("IP Address is missing.");
		}
	}

	/**
	 * Method to get current user.
	 * 
	 * @return
	 */
	private User getCurrentUser() {
		// Getting principal object from context.
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		// user object.
		User userDetails = null;
		// checking the type of instance.
		if (principal instanceof User) {
			userDetails = (User) principal;
		}
		return userDetails;
	}

	/**
	 * Method to get legends for the given pattern.
	 * 
	 * @throws Exception
	 */
	public void removeviews() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				dbCluster.getName());

		// checking existence of ip and start time.
		if (!parameterMap.containsKey(Constant.Keys.PATTERN)) {
			throw new Exception("Pattern is missing.");
		}

		// pattern
		String pattern = (String) parameterMap.get(Constant.Keys.PATTERN);
		// node ip.
		String ip = null;
		if (parameterMap.containsKey(Constant.Keys.IP)) {
			ip = (String) parameterMap.get(Constant.Keys.IP);
		}
		// putting the extracted data into the json.
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}

		// list of legends.
		List<String> legends = graph.getLegends(ip, pattern);

		// remove the legends from existing saved legends.
		saveGraphViewData(legends, ip, false);
	}

	/**
	 * Method to convert null value maps to list object.
	 * 
	 * @param treeMap
	 *            the tree map
	 * @return the object
	 */
	private static Object convertToList(Map treeMap) {
		// if treemap is null or empty return null.
		if (treeMap == null || treeMap.isEmpty())
			return null;
		boolean isList = false;
		// item set
		Set itemSet = new HashSet();
		// iterating over the treemap.
		for (Object m : treeMap.keySet()) {
			// item key.
			String itemKey = m.toString();
			// if value is null.
			if (treeMap.get(itemKey) == null) {
				isList = true;
				// adding item to item set.
				itemSet.add(itemKey);
			} else {
				// getting the object.
				Object obj = convertToList((HashMap) (treeMap.get(itemKey)));
				// empty map.
				Map map = new HashMap();
				// putting object in map.
				map.put(itemKey, obj);
				// putting object in tree map.
				treeMap.put(itemKey, obj);
				// adding the item in set.
				itemSet.add(map);
			}
		}

		// if it is list then return list else return map.
		if (isList) {
			return itemSet;
		} else {
			return treeMap;
		}
	}

	/**
	 * Iterative Method to split the string and populate hashmap with tree
	 * structure.
	 * 
	 * @param treeMap
	 *            the tree map
	 * @param root
	 *            the root
	 * @param rest
	 *            the rest
	 */
	private static void treePopulation(Map treeMap, String root, String rest) {
		// spliting in two parts.
		String[] tmp = rest.split("\\.", 2);

		// getting the value from tree map.
		HashMap rootValue = (HashMap) treeMap.get(root);

		// if rootvalue is null.
		if (rootValue == null) {
			rootValue = new HashMap();
			treeMap.put(root, rootValue);
		}
		// if length is null.
		if (tmp.length == 1) { // path end
			rootValue.put(tmp[0], null);
		} else {
			// iterative call for rest of the string.
			treePopulation(rootValue, tmp[0], tmp[1]);
		}
	}

	/**
	 * Method to get the node tiles. node tiles.
	 */
	public void nodetiles() {
		// putting the tiles.
		result.put(Constant.Keys.TILES, genericNodeTiles());
	}

	protected List<TileInfo> genericNodeTiles() {

		// empty tiles object.
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// creating tile manager object.
		TileManager tileManager = new TileManager();

		// get node ip
		String ip = (String) parameterMap.get(Constant.Keys.IP);

		// Get the db node info using public IP
		Node node = nodeManager.getByPropertyValueGuarded(
				Constant.Keys.PUBLICIP, ip);

		// getting nodeconf from the node.
		NodeConf nodeConf = node.getNodeConf();
		// get the node type tiles.
		tiles.add(new TileInfo(nodeConf.getType(), "Type", null, null,
				Constant.Tile.Status.NORMAL, null));

		// add the event tiles for the node.
		tiles.addAll(tileManager.getNodeEventTiles(dbCluster.getId(), ip));

		return tiles;

	}

	/**
	 * Servicetiles.
	 */
	public void servicetiles() {

		// get technologyName from parameter Map
		String technology = (String) parameterMap.get(Constant.Keys.TECHNOLOGY);
		result.put("tiles", getServiceTiles(technology));
	}

	protected List<TileInfo> getServiceTiles(String technology) {
		// get clusterId
		Long clusterId = this.dbCluster.getClusterConf().getClusterId();

		List<TileInfo> tiles = new ArrayList<TileInfo>();
		String severity = "Critical";
		String type = "Service";
		EventManager eventManager = new EventManager();
		List<Event> events = new ArrayList<Event>();
		// iterating over the nodes.
		for (Node node : dbCluster.getNodes()) {
			// get all the Critical events(Service type) for this node
			events.addAll(eventManager.getEventsBySeverityAndType(
					dbCluster.getId(), severity, node.getPublicIp(), type));
		}
		// get ServiceNameString for this technology
		String serviceNameString = Constant.ServiceName
				.getServiceNameMap(technology);
		String[] serviceNameArray = serviceNameString.split(",");
		List<String> serviceList = Arrays.asList(serviceNameArray);
		TileInfo tileInfo = null;
		if (events != null && events.size() > 0 && !events.isEmpty()) {
			for (String serviceName : serviceList) {
				int count = 0;
				for (Event event : events) {
					if (event.getSubType().equals(serviceName)) {
						count += 1;
					}
				}
				if (count > 0) {
					tileInfo = new TileInfo();
					String role = Constant.RoleProcessName
							.getRoleName(serviceName);
					if ((role != null) && (!role.isEmpty())) {
						tileInfo.setLine1(role);
					} else {
						tileInfo.setLine1(serviceName);
					}
					tileInfo.setLine2(Constant.Service.State.DOWN);
					tileInfo.setLine3(count
							+ AbstractMonitor.getDisplayNameForNode(count));
					tileInfo.setStatus(Constant.Tile.Status.CRITICAL);
					tiles.add(tileInfo);
				}
			}
		}
		return tiles;
	}
	
	

	/**
	 * Gets the display name for node.
	 * 
	 * @param countOfNodes
	 *            the count of nodes
	 * @return the display name for node
	 */
	public static String getDisplayNameForNode(int countOfNodes) {
		String nodes = Constant.STR_SPACE + "Nodes" + Constant.STR_SPACE;
		if (countOfNodes == 1) {
			nodes = Constant.STR_SPACE + "Node" + Constant.STR_SPACE;
		}
		return nodes;
	}

	/**
	 * Start cluster.
	 * 
	 * @return true, if successful
	 */
	public boolean startCluster() {
		boolean isStarted = false;
		// Getting clusteable object using the technology name.
		cluster = ObjectFactory.getClusterableInstanceById(dbCluster
				.getTechnology());
		try {
			// get Deployable Queue containg components to be deployed for this
			// cluster
			getdeployableQueue();
			// stack for tracking inprogress components
			Stack<DeployableConf> deployedStack = new Stack<DeployableConf>();
			// deployemnt status flag
			// iterate over deployable priority queue
			while (deployableQueue.size() > 0) {
				// pick component object from priority queue
				DeployableConf deployableConf = deployableQueue.remove();
				// push component into inprogress stack
				deployedStack.push(deployableConf);
				Deployable deployer = deployableConf.getDeployer();

				// call start method on component
				isStarted = deployer.start(deployableConf.getConfiguration());
			}
		} catch (Exception e) {
			addError(cluster.getError());
			logger.error("Exception while starting cluster.");
			// this.clusterConfig.setState(Constant.Cluster.State.ERROR);
			return false;
		}
		return isStarted;
	}

	/**
	 * Stop cluster.
	 * 
	 * @return true, if successful
	 */
	public boolean stopCluster() {

		boolean isStopped = false;
		// Getting clusteable object using the technology name.
		cluster = ObjectFactory.getClusterableInstanceById(dbCluster
				.getTechnology());

		try {
			// get Undeployable Queue containg components to be deployed for
			// this
			// cluster
			getUndeployableQueue();

			// iterate over deployable priority queue
			while (unDeployableQueue.size() > 0) {
				// pick component object from unDeployableQueue priority queue
				DeployableConf deployableConf = unDeployableQueue.remove();
				// push component into inprogress stack
				Deployable deployer = deployableConf.getDeployer();
				// call stop method on component
				isStopped = deployer.stop(deployableConf.getConfiguration());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return isStopped;
	}

	/**
	 * Restart cluster.
	 * 
	 * @return true, if successful
	 */
	public boolean restartCluster() {
		boolean status = stopCluster();
		if (status) {
			status = startCluster();
		}
		return status;
	}

	/**
	 * Gets the deployable queue.
	 * 
	 * @return the deployable queue
	 */
	private void getdeployableQueue() {
		if (!this.clusterConfig.getState().equals(Constant.Cluster.State.ERROR)) {

			Map<String, Configuration> componentConfig = cluster
					.createConfigs(this.clusterConfig);

			// setting agent , ganglia and preprocessor confs.
			componentConfig.put(Constant.Component.Name.AGENT,
					ComponentConfigurator.getAgentConf(this.clusterConfig));
			componentConfig.put(Constant.Component.Name.GANGLIA,
					ComponentConfigurator.getGangliaConf(this.clusterConfig));
			componentConfig.put(Constant.Component.Name.PREPROCESSOR,
					ComponentConfigurator
							.getPreprocessorConf(this.clusterConfig));
			componentConfig.put(Constant.Component.Name.POSTPROCESSOR,
					ComponentConfigurator
							.getPostProcessorConf(this.clusterConfig));

			// iterate over component list
			for (String componentId : componentConfig.keySet()) {
				// create Deployer obj
				Deployable deployer = ObjectFactory
						.getInstanceById(componentId);
				// create DeployableConf(Deployer, Conf) bean class object
				DeployableConf deployableConf = new DeployableConf(deployer,
						componentConfig.get(componentId));
				// insert DeployableConf in PriorityQueue
				deployableQueue.add(deployableConf);
			}
		}
	}

	/**
	 * Gets the undeployable queue.
	 * 
	 * @return the undeployable queue
	 */
	private void getUndeployableQueue() {
		// If cluster state is error then no need to undeploy the
		// components.
		if (!this.clusterConfig.getState().equals(Constant.Cluster.State.ERROR)) {

			// Creating undeploy configs.
			Map<String, Configuration> componentConfig = cluster
					.createConfigs(this.clusterConfig);

			// setting agent , ganglia and preprocessor confs.
			componentConfig.put(Constant.Component.Name.AGENT,
					ComponentConfigurator.getAgentConf(clusterConfig));
			componentConfig.put(Constant.Component.Name.GANGLIA,
					ComponentConfigurator.getGangliaConf(clusterConfig));
			componentConfig.put(Constant.Component.Name.PREPROCESSOR,
					ComponentConfigurator.getPreprocessorConf(clusterConfig));
			componentConfig.put(Constant.Component.Name.POSTPROCESSOR,
					ComponentConfigurator.getPostProcessorConf(clusterConfig));

			// iterate over component list
			for (String componentId : componentConfig.keySet()) {
				// create Deployer obj
				Deployable deployer = ObjectFactory
						.getInstanceById(componentId);
				// create DeployableConf(Deployer, Conf) bean class object
				DeployableConf deployableConf = new DeployableConf(deployer,
						componentConfig.get(componentId));
				// insert DeployableConf in PriorityQueue
				unDeployableQueue.add(deployableConf);
			}
		}
	}

	/** Clusterable object. */
	private Clusterable cluster = null;

	/** The deployable queue. */
	private PriorityQueue<DeployableConf> deployableQueue = new PriorityQueue<DeployableConf>(
			10, new ComponentComparator());

	/** The un deployable queue. */
	private PriorityQueue<DeployableConf> unDeployableQueue = new PriorityQueue<DeployableConf>(
			10, new ComponentComparator(true));
}
