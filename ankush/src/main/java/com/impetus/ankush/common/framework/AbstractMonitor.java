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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;

import org.springframework.security.core.context.SecurityContextHolder;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.AlertsConf;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.Graph.StartTime;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event;
import com.impetus.ankush.common.domain.HAService;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.DeployableConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.framework.config.NodeDiskInfo;
import com.impetus.ankush.common.framework.config.NodeMemoryInfo;
import com.impetus.ankush.common.ganglia.GangliaConf;
import com.impetus.ankush.common.ganglia.Graph;
import com.impetus.ankush.common.ganglia.LiveGraph;
import com.impetus.ankush.common.ganglia.NodeGraph;
import com.impetus.ankush.common.mail.MailManager;
import com.impetus.ankush.common.mail.MailMsg;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AgentAction;
import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.service.impl.AppConfServiceImpl;
import com.impetus.ankush.common.tiles.NewTileInfo;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush2.constant.Constant.Component;
import com.impetus.ankush2.constant.Constant.Strings;
import com.impetus.ankush2.db.DBEventManager;

/**
 * The Class AbstractMonitor.
 * 
 * @author hokam
 */
public abstract class AbstractMonitor {

	private static final String CORES = "Cores";

	private static final String FREE_DISK = "Free Disk";

	private static final String USED_DISK = "Used Disk";

	private static final String TOTAL_DISK = "Total Disk";

	private static final String FREE_MEMORY = "Free Memory";

	private static final String TOTAL_MEMORY = "Total Memory";

	private static final String USED_MEMORY = "Used Memory";

	private static final String CPU_USAGE = "Cpu Usage";

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(AbstractMonitor.class);

	/** Generic log master. */
	protected static GenericManager<Log, Long> logManager = AppStoreWrapper
			.getManager(Constant.Manager.LOG, Log.class);

	/** The monitoring manager. */
	private GenericManager<NodeMonitoring, Long> monitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	/** The cluster manager. */
	protected GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The role manager. */
	private GenericManager<Role, Long> roleManager = AppStoreWrapper
			.getManager(Constant.Manager.ROLE, Role.class);

	/** The user manager. */
	private UserManager userManager = AppStoreWrapper.getService("userManager",
			UserManager.class);

	/** App Conf service *. */
	private AppConfService appConfService = AppStoreWrapper.getService(
			Constant.Service.APPCONF, AppConfServiceImpl.class);

	/** HA Service Database Manager *. */
	private GenericManager<HAService, String> hADBManager = AppStoreWrapper
			.getManager(Constant.Manager.HAService, HAService.class,
					String.class);

	/** The operation manager. */
	private GenericManager<Operation, Long> operationManager = AppStoreWrapper
			.getManager(Constant.Manager.OPERATION, Operation.class);

	/** The errors. */
	protected List<String> errors = new ArrayList<String>();

	/** The result. */
	protected Map<String, Object> result = new HashMap<String, Object>();

	/** The db cluster. */
	protected Cluster dbCluster;

	/** The cluster config. */
	protected ClusterConf clusterConfig;

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

	/**
	 * Adds the and log error.
	 * 
	 * @param errorMsg
	 *            the error msg
	 */
	public void addAndLogError(String errorMsg) {
		addError(errorMsg);
		logger.error(errorMsg);
	}

	/**
	 * Adds the and log error.
	 * 
	 * @param errorDisplayMsg
	 *            the error display msg
	 * @param errorLogMsg
	 *            the error log msg
	 */
	public void addAndLogError(String errorDisplayMsg, String errorLogMsg) {
		addError(errorDisplayMsg);
		logger.error(errorDisplayMsg);
		logger.error(errorLogMsg);
	}

	/**
	 * Adds the and log error.
	 * 
	 * @param errorDisplayMsg
	 *            the error display msg
	 * @param e
	 *            the e
	 */
	public void addAndLogError(String errorDisplayMsg, Exception e) {
		addError(errorDisplayMsg);
		logger.error(errorDisplayMsg, e);
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
		if (result
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.STATE)) {
			// getting state.
			String state = (String) result
					.get(com.impetus.ankush2.constant.Constant.Keys.STATE);
			// if state is server crashed.
			if (state.equals(Constant.Cluster.State.SERVER_CRASHED)) {
				// setting state as error.
				result.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
						Constant.Cluster.State.ERROR);
			}
		}
		// if error is empth then set status as true else false
		if (this.errors.isEmpty()) {
			this.result.put(com.impetus.ankush2.constant.Constant.Keys.STATUS,
					true);
		} else {
			this.result.put(com.impetus.ankush2.constant.Constant.Keys.STATUS,
					false);
			this.result.put(com.impetus.ankush2.constant.Constant.Keys.ERROR,
					this.errors);
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

		String clusterName = getGangliaClusterName(conf);

		// creating node graph object.
		NodeGraph nodeGraph = new NodeGraph(conf.getGangliaMaster()
				.getPublicIp(), conf.getUsername(), conf.getPassword(),
				conf.getPrivateKey(), clusterName);

		// checking existance of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)
				|| !parameterMap
						.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)) {
			throw new Exception("Either ip or start time is missing.");
		}

		// getting starttime
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// getting ip address.
		String ip = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);

		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}

		try {
			// putting the extracted data into the json.
			result.putAll(nodeGraph.extractRRD(startTime, ip));
		} catch (Exception e) {
			addError(e.getMessage());
		}
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

		String clusterName = getGangliaClusterName(conf);

		// creating node graph object.
		LiveGraph graph = new LiveGraph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				clusterName);

		// checking existence of start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)) {
			throw new Exception("Start time is missing.");
		}

		// Getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// Getting type
		String type = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.TYPE);

		try {
			// putting the extracted data into the json.
			result.putAll(graph.fetchGraphJson(startTime, type));
		} catch (Exception e) {
			addError(e.getMessage());
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
		// cluster name
		String clusterName = getGangliaClusterName(conf);
		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				clusterName);

		// checking existence of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.PATTERN)
				&& !parameterMap
						.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)) {
			throw new Exception("Either pattern or start time is missing.");
		}

		// getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// pattern
		String pattern = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.PATTERN);
		// node ip.
		String ip = null;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			ip = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		}
		// putting the extracted data into the json.
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}

		try {
			// putting the extracted data into the json.
			if (ip != null) {
				result.putAll(graph.extractRRD(ip, startTime, pattern));
			} else {
				result.putAll(graph.extractRRD(startTime, pattern));
			}
		} catch (Exception e) {
			addError(e.getMessage());
		}

	}

	private String getGangliaClusterName(ClusterConf conf) {
		// cluster name
		String clusterName = dbCluster.getName();

		// if it registerable cluster then take the cluster name from ganglia
		// conf.
		if (conf.isRegisterableCluster()) {
			// Ganglia conf
			GangliaConf gConf = (GangliaConf) conf.getClusterComponents().get(
					Component.Name.GANGLIA);
			// get ganglia cluster name.
			clusterName = gConf.getGangliaClusterName();
		}
		return clusterName;
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
		// cluster name
		String clusterName = getGangliaClusterName(conf);

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				clusterName);

		// checking existence of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.PATTERN)
				|| !parameterMap
						.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)) {
			throw new Exception("Either pattern or start time is missing.");
		}

		// getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// pattern
		String pattern = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.PATTERN);

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
		// cluster name
		String clusterName = getGangliaClusterName(conf);

		LiveGraph graph = new LiveGraph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				clusterName, update);

		// checking existence of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)) {
			throw new Exception("Start time is missing.");
		}

		// getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// pattern
		// node ip.
		String ip = null;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			ip = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		}
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);
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
	 * Method to get the activity progress logs.
	 * 
	 * @return Map of all progress logs.
	 */
	public void mlogs() {
		result.putAll(getMaintenanceLogs());
	}

	/**
	 * Method to set the cluster as agent upgraded.
	 */
	public void agentupgraded() {
		// Get Agent build version
		String agentBuildVersion = AppStoreWrapper.getAgentBuildVersion();
		// Iterate over the nodes
		for (Node node : dbCluster.getNodes()) {
			// Node conf object
			NodeConf nodeConf = node.getNodeConf();
			// Set status as true
			nodeConf.setStatus(true);
			// set errors as empty
			nodeConf.setErrors(new HashMap<String, String>());
			// Set the new agent version
			node.setAgentVersion(agentBuildVersion);
			// Set node conf
			node.setNodeConf(nodeConf);
			// Save node
			nodeManager.save(node);
		}
		// Set new agent version
		dbCluster.setAgentVersion(agentBuildVersion);
		// set state as deployed
		dbCluster.setState(Constant.Cluster.State.DEPLOYED);
		// save cluster.
		clusterManager.save(dbCluster);
	}

	/**
	 * Gets the logs.
	 * 
	 * @return the logs
	 */
	public Map getMaintenanceLogs() {
		int lastLog = 0;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.LASTLOG)) {
			lastLog = ParserUtil
					.getIntValue(
							(String) parameterMap
									.get(com.impetus.ankush2.constant.Constant.Keys.LASTLOG),
							0);
		}
		// ip
		String ip = null;
		// node
		Node node = null;

		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			ip = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);

			// Getting node
			node = nodeManager.getByPropertyValueGuarded(
					com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);
		}

		// result map.
		Map map = new HashMap();

		// creating empty map.
		Object clusterId = dbCluster.getId();

		// Getting last operation id.
		List<Log> lastOperationLogs = logManager.getAllByNamedQuery(
				"getLastOperationId", Collections.singletonMap(
						com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
						clusterId));

		if (lastOperationLogs == null || lastOperationLogs.isEmpty()) {
			addError("Unable to get operation id from the logs.");
			return Collections.EMPTY_MAP;
		}

		Long operationId = lastOperationLogs.get(lastOperationLogs.size() - 1)
				.getOperationId();

		// Creating Empty property/value map.
		Map<String, Object> propertyValueMap = new HashMap<String, Object>();

		// putting cluster id.
		propertyValueMap.put(
				com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
				dbCluster.getId());

		// putting host public ip
		if (ip != null && !ip.isEmpty()) {
			propertyValueMap.put(
					com.impetus.ankush2.constant.Constant.Keys.HOST, ip);

		}

		// putting last operation id.
		propertyValueMap.put(
				com.impetus.ankush2.constant.Constant.Keys.OPERATIONID,
				operationId);
		List<Log> logs = logManager.getAllByPropertyValue(propertyValueMap,
				com.impetus.ankush2.constant.Constant.Keys.ID);

		logs.subList(0, lastLog).clear();

		lastLog = lastLog + logs.size();

		map.put(com.impetus.ankush2.constant.Constant.Keys.ERRORS, node
				.getNodeConf().getErrors());
		map.put(com.impetus.ankush2.constant.Constant.Keys.LOGS, logs);
		map.put(com.impetus.ankush2.constant.Constant.Keys.LASTLOG, lastLog);
		map.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
				dbCluster.getState());
		map.put(com.impetus.ankush2.constant.Constant.Keys.RegisterCluster.REGISTERABLE_CLUSTER,
				dbCluster.getClusterConf().isRegisterableCluster());

		if (node != null) {
			// Agent build version
			String buildAgentVersion = AppStoreWrapper.getAgentBuildVersion();
			// node agent version
			String nodeAgentVersion = node.getAgentVersion();
			// node status
			String nodeStatus = com.impetus.ankush2.constant.Constant.Keys.INPROGRESS;
			// status
			boolean status = node.getNodeConf().getStatus();
			// Setting status.
			if (status) {
				// if bot version are equals.
				if (nodeAgentVersion.equals(buildAgentVersion)) {
					// set status as done.
					nodeStatus = com.impetus.ankush2.constant.Constant.Keys.DONE;
				} else {
					// set status as done.
					nodeStatus = com.impetus.ankush2.constant.Constant.Keys.INPROGRESS;
				}
			} else {
				// if not equals.
				if (!nodeAgentVersion.equals(buildAgentVersion)) {
					// set status as failed.
					// set status as done.
					nodeStatus = com.impetus.ankush2.constant.Constant.Keys.FAILED;
				}
			}
			map.put(com.impetus.ankush2.constant.Constant.Keys.NODESTATUS,
					nodeStatus);
		}
		return map;
	}

	/**
	 * Gets the logs.
	 * 
	 * @return the logs
	 */
	public Map getLogs() {
		int lastLog = 0;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.LASTLOG)) {
			lastLog = ParserUtil
					.getIntValue(
							(String) parameterMap
									.get(com.impetus.ankush2.constant.Constant.Keys.LASTLOG),
							0);
		}
		String ip = null;

		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			ip = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		}

		// creating empty map.
		Object clusterId = dbCluster.getId();

		// Getting last operation id.
		List<Log> lastOperationLogs = logManager.getAllByNamedQuery(
				"getLastOperationId", Collections.singletonMap(
						com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
						clusterId));

		if (lastOperationLogs == null || lastOperationLogs.isEmpty()) {
			addError("Unable to get operation id from the logs.");
			return Collections.EMPTY_MAP;
		}

		Long operationId = lastOperationLogs.get(lastOperationLogs.size() - 1)
				.getOperationId();

		// Creating Empty property/value map.
		Map<String, Object> propertyValueMap = new HashMap<String, Object>();

		// putting cluster id.
		propertyValueMap.put(
				com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
				dbCluster.getId());

		// putting host public ip
		if (ip != null && !ip.isEmpty()) {
			propertyValueMap.put(
					com.impetus.ankush2.constant.Constant.Keys.HOST, ip);

		}

		// putting last operation id.
		propertyValueMap.put(
				com.impetus.ankush2.constant.Constant.Keys.OPERATIONID,
				operationId);
		List<Log> logs = logManager.getAllByPropertyValue(propertyValueMap,
				com.impetus.ankush2.constant.Constant.Keys.ID);

		logs.subList(0, lastLog).clear();

		lastLog = lastLog + logs.size();

		Map map = new HashMap();
		map.put(com.impetus.ankush2.constant.Constant.Keys.LOGS, logs);
		map.put(com.impetus.ankush2.constant.Constant.Keys.LASTLOG, lastLog);
		map.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
				dbCluster.getState());
		map.put(com.impetus.ankush2.constant.Constant.Keys.RegisterCluster.REGISTERABLE_CLUSTER,
				dbCluster.getClusterConf().isRegisterableCluster());
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
		Set<Node> nodes = dbCluster.getNodes(); // dbCluster.getSortedNodesByIp();
		// list of state which needs to be included for creating the heat map
		// data.
		List<String> includeStates = new ArrayList<String>();
		// adding deployed state for node
		includeStates.add(Constant.Node.State.DEPLOYED);
		// adding removing state for node
		includeStates.add(Constant.Node.State.REMOVING);
		// heat map data object.
		TreeMap heatMapData = new TreeMap();
		// iterating over the nodes
		for (Node node : nodes) {

			// If state id adding then no need to send the heat map data.
			if (!includeStates.contains(node.getState())) {
				continue;
			}

			// getting node monitoring data.
			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(
							com.impetus.ankush2.constant.Constant.Keys.NODEID,
							node.getId());

			String usageValue = null;
			boolean isAgentDown = true;
			// if node monitoring, its monitoring info and its up time info is
			// not null
			// if (nodeMonitoring != null
			// && nodeMonitoring.getMonitoringInfo() != null
			// && nodeMonitoring.getMonitoringInfo().getMemoryInfos() != null) {
			// // set agent down status.
			// isAgentDown = nodeMonitoring.isAgentDown();
			// // get usage value.
			// Double usageValueDouble = nodeMonitoring.getMonitoringInfo()
			// .getMemoryInfos().get(0).getUsedPercentage();
			// // current usage value.
			// if (usageValueDouble != null) {
			// usageValue = formator.format(usageValueDouble).toString();
			// }
			// }

			// // Getting the status value for the CPU Usage
			// DBEventManager eventManager = new DBEventManager();
			// Event event = eventManager
			// .getEvent(
			// node.getPublicIp(),
			// com.impetus.ankush2.constant.Constant.Component.Name.ANKUSH,
			// Constant.Alerts.Usage.MEMORY);
			//
			// // Getting the severity value.
			// String status = Constant.Alerts.Severity.NORMAL;
			// if (event != null) {
			// status = event.getSeverity();
			// }
			//
			// // if agent is down making status as unavailable.
			// if (isAgentDown) {
			// usageValue = "0";
			// status = Constant.Alerts.Severity.UNAVAILABLE;
			// }

			// Getting rack info for node.
			// String rackId = GenericRackAwareness.getRackId(node);
			// // update the rack heat map data and put it in main heat map
			// data.
			// heatMapData.put(
			// rackId,
			// updateRackHeatMapData(rackId, node, usageValue, status,
			// heatMapData));
		}
		// setting rack info in map.
		result.put(com.impetus.ankush2.constant.Constant.Keys.RACKINFO,
				heatMapData.values());
		// setting total rack.
		result.put(com.impetus.ankush2.constant.Constant.Keys.TOTALRACKS,
				heatMapData.size());
	}

	/**
	 * Cpu heat map.
	 */
	public void cpuheatmap() {
		// cluster nodes
		Set<Node> nodes = dbCluster.getNodes();// getSortedNodesByIp();
		// list of state which needs to be included for creating the heat map
		// data.
		List<String> includeStates = new ArrayList<String>();
		// adding deployed state for node
		includeStates.add(Constant.Node.State.DEPLOYED);
		// adding removing state for node
		includeStates.add(Constant.Node.State.REMOVING);
		// heat map data object.
		TreeMap heatMapData = new TreeMap();
		// iterating over the nodes.
		for (Node node : nodes) {

			// if the node state is available in including state list.
			if (!includeStates.contains(node.getState())) {
				continue;
			}
			// node monitoring object.
			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(
							com.impetus.ankush2.constant.Constant.Keys.NODEID,
							node.getId());

			// usage value.
			String usageValue = null;
			// flag for agent down status
			boolean isAgentDown = true;
			// if node monitoring, its monitoring info and its up time info is
			// not null
			// if (nodeMonitoring != null
			// && nodeMonitoring.getMonitoringInfo() != null
			// && nodeMonitoring.getMonitoringInfo().getUptimeInfos() != null) {
			// // set agent down status.
			// isAgentDown = nodeMonitoring.isAgentDown();
			// // get usage value.
			// Double usageValueDouble = nodeMonitoring.getMonitoringInfo()
			// .getUptimeInfos().get(0).getCpuUsage();
			// // current usage value.
			// if (usageValueDouble != null) {
			// usageValue = formator.format(usageValueDouble).toString();
			// }
			// }

			// Getting the status value for the CPU Usage
			// DBEventManager eventManager = new DBEventManager();
			// // Getting the event for the node.
			// Event event = eventManager
			// .getEvent(
			// node.getPublicIp(),
			// com.impetus.ankush2.constant.Constant.Component.Name.ANKUSH,
			// Constant.Alerts.Usage.CPU);
			//
			// // Getting the severity value.
			// String status = Constant.Alerts.Severity.NORMAL;
			// if (event != null) {
			// status = event.getSeverity();
			// }
			//
			// // if agent is down making status as unavailable.
			// if (isAgentDown) {
			// usageValue = "0";
			// status = Constant.Alerts.Severity.UNAVAILABLE;
			// }
			// // Getting rack info for node.
			// String rackId = GenericRackAwareness.getRackId(node);
			// // update the rack heat map data and put it in main heat map
			// data.
			// heatMapData.put(
			// rackId,
			// updateRackHeatMapData(rackId, node, usageValue, status,
			// heatMapData));
		}
		// setting rack info in map.
		result.put(com.impetus.ankush2.constant.Constant.Keys.RACKINFO,
				heatMapData.values());
		// setting total rack.
		result.put(com.impetus.ankush2.constant.Constant.Keys.TOTALRACKS,
				heatMapData.size());
	}

	/**
	 * Update rack heat map data.
	 * 
	 * @param rackId
	 *            the rack id
	 * @param node
	 *            the node
	 * @param usageValue
	 *            the usage value
	 * @param status
	 *            the status
	 * @param heatMapData
	 *            the heat map data
	 * @return the tree map
	 */
	public TreeMap updateRackHeatMapData(String rackId, Node node,
			String usageValue, String status, TreeMap heatMapData) {

		// node data map
		TreeMap<String, Object> nodeData = new TreeMap<String, Object>();
		// Setting node and rack info in node data.
		nodeData.put(com.impetus.ankush2.constant.Constant.Keys.ID,
				node.getId());
		nodeData.put(com.impetus.ankush2.constant.Constant.Keys.NODEIP,
				node.getPublicIp());
		nodeData.put(com.impetus.ankush2.constant.Constant.Keys.VALUE,
				usageValue);
		nodeData.put(com.impetus.ankush2.constant.Constant.Keys.STATUS,
				status.toLowerCase());

		// Getting heat map data for rack
		TreeMap rackHeatMapData = (TreeMap) heatMapData.get(rackId);

		// if its null setting it as empty map.
		if (rackHeatMapData == null) {
			// map assignment.
			rackHeatMapData = new TreeMap();
		}
		// Setting rack id in rack heat map data.
		rackHeatMapData.put(com.impetus.ankush2.constant.Constant.Keys.RACKID,
				rackId);
		// Getting node data.
		List<TreeMap> rackNodesData = (ArrayList<TreeMap>) rackHeatMapData
				.get(com.impetus.ankush2.constant.Constant.Keys.NODEDATA);
		// If rack node data is null assign an empty list of map.
		if (rackNodesData == null) {
			// assigning nodes data.
			rackNodesData = new ArrayList<TreeMap>();
		}
		// Adding current node data to rack nodes data.
		rackNodesData.add(nodeData);
		// setting node data in rack heat map data.
		rackHeatMapData.put(
				com.impetus.ankush2.constant.Constant.Keys.NODEDATA,
				rackNodesData);
		// setting total nodes count.
		rackHeatMapData.put(
				com.impetus.ankush2.constant.Constant.Keys.TOTALNODES,
				rackNodesData.size());
		// rack heat map data in main heat map data.
		return rackHeatMapData;
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
					.getByPropertyValueGuarded(
							com.impetus.ankush2.constant.Constant.Keys.NODEID,
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
	// public Map events() {
	// // event manager object.
	// DBEventManager eventManager = new DBEventManager();
	// // Getting events by cluster id.
	// List<Event> events = eventManager.getClusterEvents(dbCluster.getId());
	// // setting events in result map.
	// result.put(com.impetus.ankush2.constant.Constant.Keys.EVENTS, events);
	// // returning events.
	// return returnResult();
	// }

	/**
	 * Method to get cluster services.
	 */
	public void haservices() {
		// Getting List of HA services already available
		List<HAService> configuredServices = hADBManager.getAllByPropertyValue(
				com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
				dbCluster.getId());

		// create ha services.
		Set<HAService> haServices = new HashSet<HAService>(configuredServices);

		// Getting all cluster services.
		Set<String> services = getClusterServices();

		// Iterating over the services.
		for (String service : services) {
			// adding services
			haServices.add(new HAService(service));
		}
		// setting services.
		result.put(com.impetus.ankush2.constant.Constant.Keys.SERVICES,
				haServices);
	}

	/**
	 * Method to get Node HA services.
	 */
	public void nodehaservices() {
		// public ip
		String publicIp = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);

		// setting services.
		result.put("services", getNodeHAServices(publicIp));

	}

	/**
	 * Method to get Node HA configured services.
	 * 
	 * @param publicIp
	 * @return
	 */
	private Set<HAService> getNodeHAServices(String publicIp) {
		// Getting List of HA services already available
		List<HAService> configuredServices = hADBManager.getAllByPropertyValue(
				com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
				dbCluster.getId());

		// node ha services.
		Set<HAService> nodeHAServices = new HashSet<HAService>();

		// if not null
		if (publicIp != null) {
			// Getting node
			Node node = nodeManager.getByPropertyValueGuarded(
					com.impetus.ankush2.constant.Constant.Keys.PUBLICIP,
					publicIp);
			// Getting all node services.
			if (node != null) {
				Set<String> services = getNodeRoles(node);

				// configured HA Services.
				Set<HAService> hAServices = new HashSet<HAService>(
						configuredServices);

				// iterating over all services.
				for (HAService haService : hAServices) {
					// if node services contains the ha services then
					if (services.contains(haService.getService())) {
						// add it in node ha services.
						nodeHAServices.add(haService);
					}
				}
			}
		}
		return nodeHAServices;
	}

	/**
	 * Method to Enable HA on cluster.
	 */
	private void enableClusterHA() {
		updateClusterHA(false);
	}

	/**
	 * Method to disable HA on cluster.
	 */
	private void disableClusterHA() {
		updateClusterHA(true);
	}

	/**
	 * Method to HA configruation force stop flag.
	 * 
	 * @param forceStop
	 */
	private void updateClusterHA(final boolean forceStop) {
		try {
			// Cluster conf
			final ClusterConf clusterConf = dbCluster.getClusterConf();
			// nodes
			Set<Node> nodes = dbCluster.getNodes();
			// semaphore variable.
			final Semaphore semaphore = new Semaphore(nodes.size());
			// iterate over the nodes to set the force stop flag as false.
			for (final Node node : nodes) {
				// acquiring node.
				semaphore.acquire();
				// executing operations.
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						try {
							// Get Node HA services
							Set<HAService> services = getNodeHAServices(node
									.getPublicIp());
							// SSH Connection;
							SSHExec connection = SSHUtils.connectToNode(
									node.getPublicIp(),
									clusterConf.getUsername(),
									clusterConf.getPassword(),
									clusterConf.getPrivateKey());

							// if connection success
							if (connection != null) {
								// Iterate over the services to set for stop
								// flag as given value.
								for (HAService service : services) {
									try {
										// update forceStop flag when user
										// manually
										// start/stop the service.
										AgentAction setForceStopFlag = new AgentAction(
												Constant.Agent.Action.Handler.HACONFIG,
												Constant.Agent.Action.ADDFORCESTOP,
												service.getService(),
												Constant.Agent.AGENT_HASERVICE_CONF_PATH,
												forceStop + "");

										// setting force stop.
										connection.exec(setForceStopFlag);
									} catch (Exception e) {
										logger.error(e.getMessage(), e);
									}
								}
								// disconnect connection.
								connection.disconnect();
							}
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			// block all threads.
			semaphore.acquire(nodes.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Method save to HA Rules for the services.
	 */
	public void saveharules() {
		try {
			// if ha services is null
			if (!parameterMap
					.containsKey(com.impetus.ankush2.constant.Constant.Keys.SERVICES)) {
				addError("HA Service Configuration is missing. Please specify the parameters.");
				return;
			}

			// Getting List of HA services from
			List<HAService> haServices = JsonMapperUtil
					.getListObject(
							(List<Map>) parameterMap
									.get(com.impetus.ankush2.constant.Constant.Keys.SERVICES),
							HAService.class);

			// deleting all old saved information.
			List<HAService> oldHAServices = hADBManager.getAllByPropertyValue(
					com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
					dbCluster.getId());

			// iterating over the UI ha services
			for (HAService haService : haServices) {
				// Removing haService from old HA services object.
				oldHAServices.remove(haService);
				// setting cluster id in it
				haService.setClusterId(dbCluster.getId());
				// Saving HA service
				hADBManager.save(haService);
			}

			// iterating over the UI ha services
			for (HAService haService : oldHAServices) {
				// removing old ha services
				hADBManager.remove(haService.getService());
			}

			// Getting db Nodes.
			Set<Node> nodes = dbCluster.getNodes();

			// cluster conf
			ClusterConf clusterConf = dbCluster.getClusterConf();

			// iterating over the nodes.
			for (Node node : nodes) {
				// create ha services.
				List<AnkushTask> tasks = new ArrayList<AnkushTask>();
				// Getting all node services.
				Set<String> services = getNodeRoles(node);

				// iterating over the ha services.
				for (HAService haService : haServices) {
					// if current ha service in node services adding ha services
					// object.
					if (services.contains(haService.getService())) {
						// adding ha service
						tasks.add(new AgentAction(
								Constant.Agent.Action.Handler.HACONFIG,
								Constant.Agent.Action.ADD, haService
										.getService(), haService
										.getDelayInterval() + "", haService
										.getTryCount() + "",
								Constant.Agent.AGENT_HASERVICE_CONF_PATH));
					}
				}

				// iterating over the ha services.
				for (HAService haService : oldHAServices) {
					// if current ha service in node services adding ha services
					// object.
					if (services.contains(haService.getService())) {
						// adding ha service
						tasks.add(new AgentAction(
								Constant.Agent.Action.Handler.HACONFIG,
								Constant.Agent.Action.DELETE, haService
										.getService(),
								Constant.Agent.AGENT_HASERVICE_CONF_PATH));
					}
				}

				// if node ha services is not empty
				if (!tasks.isEmpty()) {

					// connect to node
					SSHExec connection = SSHUtils.connectToNode(
							node.getPublicIp(), clusterConf.getUsername(),
							clusterConf.getPassword(),
							clusterConf.getPrivateKey());

					// if connected
					if (connection != null) {
						// execute task
						boolean status = SSHUtils.executeTasks(tasks,
								connection).rc == 0;

						if (!status) {
							// adding error
							addError("Could not configure HA Service rules.");
						}
						// Disconnect the node
						connection.disconnect();
					}
				}
			}
		} catch (Exception e) {
			// log exception
			logger.error(e.getMessage(), e);
			// adding error
			addError("Could not configure HA Service rules.");
		}
	}

	/**
	 * Method to send a email alert to user when HA monitor fails to start the
	 * service after trying the configured try count.
	 */
	public void reporthafailure() {
		// node ip
		String nodeIp = parameterMap.get(
				com.impetus.ankush2.constant.Constant.Keys.IP).toString();

		// Service name
		String serviceName = parameterMap.get(
				com.impetus.ankush2.constant.Constant.Keys.SERVICE).toString();

		// node ip is null or service name is null
		if (nodeIp == null || serviceName == null) {
			addError("Node Ip and Service name is missing, Please provide");
			return;
		}

		// getting node from db.
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, nodeIp);

		// if node is null
		if (node == null) {
			addError("Invalid request, no node exists in ankush of given node ip");
			return;
		}

		if (!node.getState().equals(Constant.Node.State.DEPLOYED)
				|| dbCluster.getState().equals(Constant.Cluster.State.REMOVING)) {
			return;
		}

		// Getting alerts conf.
		AlertsConf alertsConf = dbCluster.getAlertsConf();

		String emailList = "";
		if (alertsConf == null) {
			addError("Alerts configuration of the cluster is missing. Please configure it.");
			return;
		}
		// getting administrator mailing list
		if (alertsConf.isInformAllAdmins()) {
			Role role = roleManager.getByPropertyValue(
					com.impetus.ankush2.constant.Constant.Keys.NAME,
					Constant.User.Role.ROLE_SUPER_USER);

			List<User> users = userManager.getUsersByRole(role);
			for (User user : users) {
				if (user.isEnabled()) {
					emailList += user.getEmail() + ";";
				}
			}
		}
		// getting configured mailing list
		if (alertsConf.getMailingList() != null) {
			emailList += alertsConf.getMailingList();
		}
		// total number of tries completed for starting the service
		Integer tries = Integer.parseInt(parameterMap.get("tries").toString());

		// subject of a message to be send.
		String subject = "AnkushHA Critical : " + nodeIp + " " + serviceName
				+ " Service";

		// body of message
		StringBuffer sb = new StringBuffer();
		sb.append("Cluster Name").append(Constant.COLON)
				.append(dbCluster.getName()).append(Constant.SLASH_N);
		sb.append(serviceName).append(" Service").append(Constant.COLON)
				.append("Down").append(Constant.SLASH_N);
		sb.append("Host").append(Constant.COLON).append(nodeIp)
				.append(Constant.SLASH_N);
		sb.append("\nCould not start the " + serviceName + " service after "
				+ tries + " tries.");

		// creating mail message object.
		MailMsg message = new MailMsg();
		message.setTo(emailList);
		message.setSubject(subject);
		message.setMessage(sb.toString());
		message.setContentType("text/plain");

		// Getting mail manager.
		MailManager mm = AppStoreWrapper.getMailManager();
		if (mm != null) {
			// Sending mail.
			mm.sendSystemMail(message);
		}
	}

	/**
	 * Node Roles REST API.
	 */
	public void noderoles() {
		// node ip
		String nodeIp = parameterMap.get(
				com.impetus.ankush2.constant.Constant.Keys.IP).toString();

		// Getting node from db.
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, nodeIp);
		// set node roles.
		result.put(
				com.impetus.ankush2.constant.Constant.Keys.ComponentNodeProperties.ROLES,
				getNodeRoles(node));
	}

	/**
	 * Method to get node roles.
	 * 
	 * @param node
	 * @return
	 */
	private Set<String> getNodeRoles(Node node) {
		// Empty role set.
		Set<String> roles = new HashSet<String>();

		// Node type
		String type = node.getNodeConf().getType();
		// if node role is not null
		if (type != null) {
			roles.addAll(Arrays.asList(type.split("/")));
		}

		// Adding gmond role.
		roles.add(Constant.Role.GMOND);
		// Adding gmetad role.
		if (node.getNodeConf().equals(
				dbCluster.getClusterConf().getGangliaMaster())) {
			roles.add(Constant.Role.GMETAD);
		}
		// return roles.
		return roles;
	}

	/**
	 * Method to get cluster services.
	 * 
	 * @return The list of cluster services.
	 */
	private Set<String> getClusterServices() {
		// Getting db Nodes.
		Set<Node> nodes = dbCluster.getNodes();

		// Service set
		Set<String> services = new HashSet<String>();

		// iterating over the nodes.
		for (Node node : nodes) {
			// Getting and Adding the service set object.
			services.addAll(getNodeRoles(node));
		}

		// Remove Agent
		services.remove(Constant.Component.ProcessName.AGENT);
		// returning services.
		return services;
	}

	/**
	 * Services.
	 */
	public void services() {
		String nodeIp = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);

		result.putAll(getServices(nodeIp));
	}

	/**
	 * Gets the services.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @return the services
	 */
	private Map<String, Boolean> getServices(NodeMonitoring nodeMonitoring,
			boolean agentDependent) {
		// service map.
		Map<String, Boolean> services = new HashMap<String, Boolean>();

		// Iterating over the technology service status.
		Map<String, Map<String, Boolean>> techStatus = nodeMonitoring
				.getTechnologyServiceStatus();

		// Iterating over the technology services map.
		for (String technology : techStatus.keySet()) {

			// Getting technology services.
			services.putAll(techStatus.get(technology));
		}
		// returning services
		return services;
	}

	/**
	 * Gets the services.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @return the services
	 */
	private Map<String, Boolean> getServices(String nodeIp) {
		// service map.
		Map<String, Boolean> services = new HashMap<String, Boolean>();

		// Get the DB node monitoring info
		NodeMonitoring nodeMonitoring = (new MonitoringManager())
				.getMonitoringData(nodeIp);
		// if node monitoring is not null
		if (nodeMonitoring != null) {
			services.putAll(getServices(nodeMonitoring, true));
		} else {
			// making service status as false for agent.
			services.put(com.impetus.ankush2.constant.Constant.Role.AGENT,
					false);
		}
		// returning services
		return services;
	}

	/**
	 * Method to get down services.
	 * 
	 * @param nodeIp
	 * @return
	 */
	public Set<String> getDownServices(NodeMonitoring nodeMonitoring) {
		// Set of down services.
		Set<String> downServices = new HashSet<String>();
		// All services with status.
		Map<String, Boolean> allServices = getServices(nodeMonitoring, true);
		// Iterate over all services
		for (String service : allServices.keySet()) {
			// if service is down add it in down services.
			if (!allServices.get(service)) {
				// add service.
				downServices.add(service);
			}
		}
		// Return down services.
		return downServices;
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
		nodeMap.put("cores", "NA");
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

				// Getting cores.
				nodeMap.put("cores", nodeMonitoring.getMonitoringInfo()
						.getCpuInfos().get(0).getCores());
			}
		} catch (Exception e) {
			addError("Unable to get the usage values.");
		}
		return nodeMap;
	}

	/**
	 * Method to get cluster usage.
	 * 
	 * @return
	 */
	public void clusterusage() {
		// Nodes
		Set<Node> nodes = dbCluster.getNodes();
		// iterate over the nodes.

		double cpuUsage = 0d;
		double usedMemory = 0d;
		double totalMemory = 0d;
		double freeMemory = 0d;
		double totalDisk = 0d;
		double usedDisk = 0d;
		double freeDisk = 0d;
		int cores = 0;

		formator = new DecimalFormat(".##");

		Map clusterUsage = new HashMap();
		for (Node node : nodes) {
			// Node monitoring object.
			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(node.getId());
			if (nodeMonitoring != null
					&& nodeMonitoring.getMonitoringInfo() != null) {
				// adding cpu usage.
				cpuUsage = cpuUsage
						+ nodeMonitoring.getMonitoringInfo().getUptimeInfos()
								.get(0).getCpuUsage();

				// adding memory usage.
				NodeMemoryInfo memInfo = nodeMonitoring.getMonitoringInfo()
						.getMemoryInfos().get(0);
				// used memory
				usedMemory = usedMemory
						+ (memInfo.getActualUsed().doubleValue());
				// total memory
				totalMemory = totalMemory + (memInfo.getTotal().doubleValue());
				// free memory
				freeMemory = freeMemory
						+ (memInfo.getActualFree().doubleValue());

				// calculating the total, used and free disk by iterating over
				// the disk.
				for (NodeDiskInfo diskInfo : nodeMonitoring.getMonitoringInfo()
						.getDiskInfos()) {
					totalDisk = totalDisk
							+ diskInfo.getTotalMemory().doubleValue();
					usedDisk = usedDisk
							+ diskInfo.getUsedMemory().doubleValue();
					freeDisk = freeDisk
							+ diskInfo.getFreeMemory().doubleValue();
				}

				// Getting cores.
				cores = cores
						+ nodeMonitoring.getMonitoringInfo().getCpuInfos()
								.get(0).getCores();
			}
		}
		int nodeSize = dbCluster.getNodes().size();
		// cluster summary.
		clusterUsage.put(CPU_USAGE, formator.format((cpuUsage / nodeSize)));
		// converting memory values in GB grom bytes.
		clusterUsage.put(USED_MEMORY,
				formator.format((usedMemory / 1024 / 1024 / 1024)));
		clusterUsage.put(TOTAL_MEMORY,
				formator.format((totalMemory / 1024 / 1024 / 1024)));
		clusterUsage.put(FREE_MEMORY,
				formator.format((freeMemory / 1024 / 1024 / 1024)));
		// Converting in GB from KB
		clusterUsage
				.put(TOTAL_DISK, formator.format((totalDisk / 1024 / 1024)));
		clusterUsage.put(USED_DISK, formator.format((usedDisk / 1024 / 1024)));
		clusterUsage.put(FREE_DISK, formator.format((freeDisk / 1024 / 1024)));
		clusterUsage.put(CORES, cores);
		clusterUsage.put("Nodes", dbCluster.getNodes().size());
		// set cluster summary.
		result.put("clusterUsage", clusterUsage);
	}

	/**
	 * Method to get node usage.
	 * 
	 * @param node
	 * @return
	 */
	private Map getNodeUsage(Node node) {
		// ouput node usage map.
		Map nodeUsage = new HashMap();

		// Node monitoring object.
		NodeMonitoring nodeMonitoring = new MonitoringManager()
				.getMonitoringData(node.getId());

		// Node usage map with different keys.
		Map nodeUsageMap = getNodeUsageMap(nodeMonitoring);
		// iterate over the node usage map.
		for (Object key : nodeUsageMap.keySet()) {
			// Get value from map using key.
			Object value = nodeUsageMap.get(key);
			if (value.equals("NA")) {
				// put 0 in value if
				nodeUsageMap.put(key, 0);
			}
		}
		// set usage values in map.
		nodeUsage.put(CPU_USAGE, nodeUsageMap.get("cpuUsage"));
		nodeUsage.put(USED_MEMORY, nodeUsageMap.get("usedMemory"));
		nodeUsage.put(TOTAL_MEMORY, nodeUsageMap.get("totalMemory"));
		nodeUsage.put(FREE_MEMORY, nodeUsageMap.get("freeMemory"));
		nodeUsage.put(TOTAL_DISK, nodeUsageMap.get("totalDisk"));
		nodeUsage.put(USED_DISK, nodeUsageMap.get("usedDisk"));
		nodeUsage.put(FREE_DISK, nodeUsageMap.get("cpuUsage"));
		nodeUsage.put(CORES, nodeUsageMap.get("cores"));
		// return node usage.
		return nodeUsage;
	}

	/**
	 * Method to get node usage.
	 */
	public void nodeusage() {
		try {
			// if parameter contain node ip
			if (!parameterMap
					.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
				addError("Node Ip is missing, Please provide the ip in parameter.");
				return;
			}
			// node ip
			String nodeIp = parameterMap.get(
					com.impetus.ankush2.constant.Constant.Keys.IP).toString();

			// Node Object
			Node node = nodeManager.getByPropertyValue("publicIp", nodeIp);

			// get node Usage.
			Map nodeUsage = getNodeUsage(node);

			// set node usage.
			result.put("nodeUsage", nodeUsage);
		} catch (Exception e) {
			// add error
			addError(e.getMessage());
		}
	}

	/**
	 * Graphtree.
	 */
	public void graphtree() {
		// getting the node ip from map.
		String nodeIp = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		try {
			// conf
			ClusterConf conf = dbCluster.getClusterConf();
			// cluster name
			String clusterName = getGangliaClusterName(conf);
			// creating node graph object.
			graph = new Graph(this.clusterConfig.getGangliaMaster()
					.getPublicIp(), this.clusterConfig.getUsername(),
					this.clusterConfig.getPassword(),
					this.clusterConfig.getPrivateKey(), clusterName);

			// putting the extracted data into the json.
			Node node = nodeManager
					.getByPropertyValueGuarded(
							com.impetus.ankush2.constant.Constant.Keys.PUBLICIP,
							nodeIp);
			if (node != null) {
				nodeIp = node.getPrivateIp();
			}
			// getting all files from the node.
			List<String> files = graph.getAllFiles(nodeIp);
			SortedSet<String> sortedFiles = new TreeSet<String>(files);

			TreeMap treeMap = populateTreeMap(sortedFiles);
			// putting the treemap against tree key.
			result.put("tree", treeMap);
		} catch (Exception e) {
			addError(e.getMessage());
		}
	}

	/**
	 * Populate tree map.
	 * 
	 * @param files
	 *            the files
	 * @return the tree map
	 */
	private TreeMap populateTreeMap(SortedSet<String> files) {
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
	 *             the exception
	 */
	public void legends() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();
		// cluster name
		String clusterName = getGangliaClusterName(conf);

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				clusterName);

		// checking existence of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.PATTERN)) {
			throw new Exception("Pattern is missing.");
		}

		// pattern
		String pattern = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.PATTERN);
		// node ip.
		String ip = null;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			ip = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		}
		// putting the extracted data into the json.
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);
		if (node != null) {
			ip = node.getPrivateIp();
		}

		// list of legends.
		List<String> legends = graph.getLegends(ip, pattern);

		// save graph view data.
		saveGraphViewData(legends, ip, true);
		// getting legends for the given pattern.
		result.put("legends", legends);
	}

	/**
	 * Method to save graph view data.
	 * 
	 * @param legends
	 *            the legends
	 * @param publicIp
	 *            the public ip
	 * @param add
	 *            the add
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
	 *             the exception
	 */
	public void graphviews() throws Exception {
		// get Current user.
		User userDetails = getCurrentUser();
		// username.
		String userName = userDetails.getUsername();
		// checking ip existance.
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			// getting ip address.
			String ip = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
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
						.getMetadata(com.impetus.ankush2.constant.Constant.Keys.NODEGRAPHS);

				if (appConf != null) {
					// Getting saved map object
					Map nodegraphsMap = (Map) appConf
							.get(com.impetus.ankush2.constant.Constant.Keys.NODEGRAPHS);
					// Getting the list of saved graphs.
					graphViewData
							.addAll((List) nodegraphsMap
									.get(com.impetus.ankush2.constant.Constant.Keys.DEFAULTGRAPHS));
					// put it against the user
					userGraphViewMap.put(userName, graphViewData);
					// set it to node monitoring object.
					nodeMonitoring.setGraphViewData(userGraphViewMap);
					// and save the default metrics in db.
					monitoringManager.save(nodeMonitoring);
				}
			}
			result.put(com.impetus.ankush2.constant.Constant.Keys.LIST,
					new TreeSet(graphViewData));
		} else {
			throw new Exception("IP Address is missing.");
		}
	}

	/**
	 * Method to get current user.
	 * 
	 * @return the current user
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
	 *             the exception
	 */
	public void removeviews() throws Exception {

		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();
		// cluster name
		String clusterName = getGangliaClusterName(conf);

		// creating node graph object.
		graph = new Graph(conf.getGangliaMaster().getPublicIp(),
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey(),
				clusterName);

		// checking existence of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.PATTERN)) {
			throw new Exception("Pattern is missing.");
		}

		// pattern
		String pattern = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.PATTERN);
		// node ip.
		String ip = null;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			ip = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		}
		// putting the extracted data into the json.
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);
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
	private static Object convertToList(TreeMap treeMap) {
		// if treemap is null or empty return null.
		if (treeMap == null || treeMap.isEmpty())
			return null;
		boolean isList = false;
		// item set
		List itemSet = new ArrayList();
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
				Object obj = convertToList((TreeMap) (treeMap.get(itemKey)));
				// empty map.
				TreeMap map = new TreeMap();
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
	private static void treePopulation(TreeMap treeMap, String root, String rest) {
		// spliting in two parts.
		String[] tmp = rest.split("\\.", 2);

		// getting the value from tree map.
		TreeMap rootValue = (TreeMap) treeMap.get(root);

		// if rootvalue is null.
		if (rootValue == null) {
			rootValue = new TreeMap();
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
	public final void nodetiles() {
		List<TileInfo> tiles = genericNodeTiles();
		// get node ip
		String ip = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		// Get the db node info using public IP
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);

		// getting nodeconf from the node.
		NodeConf nodeConf = node.getNodeConf();

		// Getting components.
		Set<String> componentsSet = getComponents();

		try {
			for (String technology : componentsSet) {
				// Getting monitorable object using the technology name.
				AbstractMonitor monitor = ObjectFactory
						.getMonitorableInstanceById(technology);
				// if monitor in not null.
				if (monitor != null) {
					// Getting comonent node tiles.
					List<TileInfo> compNodeTiles = monitor.compNodeTiles(
							this.dbCluster, this.parameterMap, ip);
					// if it is not null and not empty.
					if (compNodeTiles != null && compNodeTiles.size() > 0) {
						tiles.addAll(compNodeTiles);
					}
				}
			}
		} catch (Exception e) {
			addAndLogError(
					"Exception in getting node tiles : " + e.getMessage(), e);
		}
		// putting the tiles.
		result.put(com.impetus.ankush2.constant.Constant.Keys.TILES, tiles);
	}

	/**
	 * Comp node tiles.
	 * 
	 * @param dbCluster
	 *            the db cluster
	 * @param parameterMap
	 *            the parameter map
	 * @param nodeIP
	 *            the node ip
	 * @return the list
	 */
	public abstract List<TileInfo> compNodeTiles(Cluster dbCluster,
			Map parameterMap, String nodeIP);

	/**
	 * Component summarized tiles.
	 * 
	 * @param dbCluster
	 *            the db cluster
	 * @return the list
	 */
	public abstract List<NewTileInfo> componentSummarizedTiles(Cluster dbCluster);

	/**
	 * Components.
	 */
	public void components() {
		// putting the componnets
		result.put(com.impetus.ankush2.constant.Constant.Keys.COMPONENTS,
				getComponents());
	}

	/**
	 * Gets the components.
	 * 
	 * @return the components
	 */
	private Set<String> getComponents() {
		// Get Node IP
		String ip = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		// Get the db node info using public IP
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);
		// node Components.
		Set<String> components = getComponents(node);
		// return components.
		return components;
	}

	/**
	 * Get Component of node
	 * 
	 * @param node
	 * @return
	 */
	private Set<String> getComponents(Node node) {
		// Getting nodeConf from the node.
		NodeConf nodeConf = node.getNodeConf();
		// empty components.
		Set<String> components = new HashSet<String>();
		// iterate over the components.
		for (String componentName : this.dbCluster.getClusterConf()
				.getClusterComponents().keySet()) {
			// Get Component configuration.
			GenericConfiguration compConf = dbCluster.getClusterConf()
					.getClusterComponents().get(componentName);
			// if node is part of nodes.
			if (compConf.getCompNodes().contains(nodeConf)) {
				// add component.
				components.add(componentName);
			}
		}
		// return components.
		return components;
	}

	/**
	 * Nodes.
	 */
	public void nodes() {
		try {
			result.put("alertsConf", dbCluster.getAlertsConf()
					.getThresholdsMap());
			// set nodes.
			result.put(com.impetus.ankush2.constant.Constant.Keys.NODES,
					getNodeMapList());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
	}

	/**
	 * Generic node tiles.
	 * 
	 * @return the list
	 */
	protected List<TileInfo> genericNodeTiles() {

		// empty tiles object.
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// creating tile manager object.
		TileManager tileManager = new TileManager();

		// get node ip
		String ip = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);

		// Get the db node info using public IP
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, ip);

		// getting nodeconf from the node.
		NodeConf nodeConf = node.getNodeConf();
		List<String> nodeTypesList = Arrays.asList(nodeConf.getType()
				.split("/"));
		for (String nodeType : nodeTypesList) {
			// get the node type tiles.
			tiles.add(new TileInfo(nodeType, "Type", null, null,
					Constant.Tile.Status.NORMAL, null));
		}

		// add the event tiles for the node.
		tiles.addAll(tileManager.getNodeEventTiles(dbCluster.getId(), ip));

		return tiles;

	}

	/**
	 * Servicetiles.
	 */
	public void servicetiles() {

		// get technologyName from parameter Map
		String technology = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.TECHNOLOGY);
		result.put(com.impetus.ankush2.constant.Constant.Keys.TILES,
				getServiceTiles(technology));
	}

	/**
	 * Gets the service tiles.
	 * 
	 * @param technology
	 *            the technology
	 * @return the service tiles
	 */
	protected List<TileInfo> getServiceTiles(String technology) {
		// list of tile
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// event manager object.
		DBEventManager eventManager = new DBEventManager();
		// event list.
		List<Event> events = eventManager.getAlerts(dbCluster.getId(), null,
				null, technology, null);

		// roles string
		String rolesString = Constant.ComponentName
				.getComponentRoleMap(technology);
		// split roles string by comma
		String[] serviceNameArray = rolesString.split(",");
		// converting array as string list.
		List<String> roles = Arrays.asList(serviceNameArray);
		// tile info
		TileInfo tileInfo = null;
		// if events is not null and not empty
		if (events != null && events.size() > 0 && !events.isEmpty()) {
			// iterating over the roles.
			for (String role : roles) {
				int count = 0;

				// iterating over the events
				for (Event event : events) {
					// if event sub type is same as role
					if (event.getCategory().equals(role)) {
						count += 1;
					}
				}
				// if count is > 0
				if (count > 0) {
					// tile object.
					tileInfo = new TileInfo();
					// set line1
					tileInfo.setLine1(role);
					// set line2
					tileInfo.setLine2(Constant.Service.State.DOWN);
					// set line3
					tileInfo.setLine3(count
							+ AbstractMonitor.getDisplayNameForNode(count));
					// set status
					tileInfo.setStatus(Constant.Tile.Status.CRITICAL);
					// adding tiles.
					tiles.add(tileInfo);
				}
			}
		}
		// return tiles.
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
		String nodes = Strings.SPACE + "Nodes" + Strings.SPACE;
		if (countOfNodes == 1) {
			nodes = Strings.SPACE + "Node" + Strings.SPACE;
		}
		return nodes;
	}

	// /**
	// * Method to get Events Summary.
	// */
	// public void eventsummary() {
	// try {
	// // Maximum count value.
	// int maxCount = MAX_EVENT_SUMMARY_COUNT;
	// // parameter map contains it.
	// if (parameterMap.containsKey("maxCount")) {
	// // Get max count value.
	// maxCount = Integer.parseInt(parameterMap.get("maxCount")
	// .toString());
	// }
	// // list of summary.
	// List summaryList = new ArrayList();
	// // event manager object.
	// DBEventManager eventManager = new DBEventManager();
	// // Getting events by cluster id.
	// List<Event> events = eventManager.getEventsSummary(
	// dbCluster.getId(), 0, maxCount);
	// // iterating over the events
	// for (Event event : events) {
	// // event summary object.
	// Map eventSummary = JsonMapperUtil.mapFromObject(event);
	// eventSummary.remove("description");
	// summaryList.add(eventSummary);
	// }
	// // setting events in result map.
	// result.put(com.impetus.ankush2.constant.Constant.Keys.EVENTS,
	// summaryList);
	// } catch (Exception e) {
	// addAndLogError(e.getMessage());
	// }
	// }

	/**
	 * Method to perform start/stop cluster operation.
	 */
	public void operate() {
		final String opName = (String) parameterMap.get("opName");
		if (!dbCluster.getState().equals(Constant.Cluster.State.DEPLOYED)) {
			addError("This operation cann't be performed as Cluster is not in deployed state.");
			return;
		}
		Map operationStatusResult = getOperationStatus();
		if (operationStatusResult != null) {
			if (operationStatusResult.get(Constant.Operation.Keys.STATUS)
					.equals(Constant.Operation.Status.IN_PROGRESS)) {
				addError(Constant.Operation.OperationName
						.getOperationDisplayName((String) operationStatusResult
								.get(Constant.Operation.Keys.NAME))
						+ " is in progress.Can not perform any other operation.");
				return;
			}
		}
		try {
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					boolean status = false;
					Operation operation = new Operation();
					operation.setOpName(opName);
					operation.setStartedAt(new Date());
					operation.setClusterId(dbCluster.getId());
					operation.setStatus(Constant.Operation.Status.IN_PROGRESS);
					operation = operationManager.save(operation);
					if (opName.equals(Constant.Operation.Name.START_CLUSTER)) {
						status = startAllServices();
					} else if (opName
							.equals(Constant.Operation.Name.STOP_CLUSTER)) {
						status = stopAllServices();
					} else if (opName
							.equals(Constant.Operation.Name.RESTART_CLUSTER)) {
						status = restartAllServices();
					}
					if (status) {
						operation
								.setStatus(Constant.Operation.Status.COMPLETED);
					} else {
						operation.setStatus(Constant.Operation.Status.FAILED);
					}
					operation.setCompletedAt(new Date());
					operationManager.save(operation);
				}

			});
			result.put(Constant.Operation.Keys.OPERATION_STATUS,
					Constant.Operation.Status.IN_PROGRESS);
		} catch (Exception e) {
			addAndLogError(
					com.impetus.ankush2.constant.Constant.Keys.GENERAL_EXCEPTION_STRING,
					e);
		}
	}

	/**
	 * Gets the operation tile.
	 * 
	 * @return the operation tile
	 */
	public TileInfo getOperationTile() {
		List<Operation> operationList = getOperationList();
		if (operationList != null && operationList.size() != 0) {
			String line1 = operationList.get(0).getOpName();
			String line2 = operationList.get(0).getStatus();
			if (line2.equals(Constant.Operation.Status.COMPLETED)
					|| line2.equals(Constant.Operation.Status.FAILED)) {
				return null;
			}
			String line3 = null;
			String status = Constant.Tile.Status.NORMAL;
			TileInfo tile = new TileInfo(
					Constant.Operation.OperationName
							.getOperationDisplayName(line1),
					"In Progress", line3, null, status, null);
			return tile;
		}
		return null;
	}

	/**
	 * Gets the operation map.
	 * 
	 * @return the operation map
	 */
	private List<Operation> getOperationList() {
		List<Operation> operationList = operationManager.getAllByPropertyValue(
				"clusterId", dbCluster.getId(), 0, 1, "-startedAt");
		return operationList;
	}

	/**
	 * Operation status.
	 */
	public void operationStatus() {
		Map<String, String> operationMap = getOperationStatus();
		if (operationMap == null) {
			operationMap = new HashMap<String, String>();
			operationMap.put(Constant.Operation.Keys.STATUS, "true");
		}
		result.put("operationMap", operationMap);
	}

	/**
	 * Gets the operation status.
	 * 
	 * @return the operation status
	 */
	private Map<String, String> getOperationStatus() {
		List<Operation> operationList = getOperationList();
		Map<String, String> operationMap = null;
		if (operationList != null && operationList.size() != 0) {
			operationMap = new HashMap<String, String>();
			operationMap.put(Constant.Operation.Keys.NAME, operationList.get(0)
					.getOpName());
			operationMap.put(Constant.Operation.Keys.STATUS,
					operationList.get(0).getStatus());
			if (operationList.get(0).getStatus()
					.equals(Constant.Operation.Status.IN_PROGRESS)) {
				addError(Constant.Operation.OperationName
						.getOperationDisplayName(operationList.get(0)
								.getOpName())
						+ " is in progress.Can not perform any other operation.");
			}
		}
		return operationMap;
	}

	/**
	 * Start all services.
	 * 
	 * @return true, if successful
	 */
	private boolean startAllServices() {
		boolean isStarted;
		Map<String, Boolean> componentStatus = new HashMap<String, Boolean>();
		// Getting clusterable object using the technology name.
		Clusterable cluster = ObjectFactory
				.getClusterableInstanceById(dbCluster.getTechnology());
		logger.setCluster(cluster.getClusterConf(dbCluster));
		try {
			// Get deployable queue
			PriorityQueue<DeployableConf> deployableQueue = getComponentQueue(
					cluster, false);
			// stack for tracking in progress components
			Stack<DeployableConf> deployedStack = new Stack<DeployableConf>();
			// deployment status flag
			// iterate over deployable priority queue
			while (deployableQueue.size() > 0) {
				// pick component object from priority queue
				DeployableConf deployableConf = deployableQueue.remove();
				// push component into in progress stack
				deployedStack.push(deployableConf);
				Deployable deployer = deployableConf.getDeployer();

				// call start method on component
				isStarted = deployer.start(deployableConf.getConfiguration());
				componentStatus.put(deployableConf.getComponentName(),
						isStarted);
			}
		} catch (Exception e) {
			addAndLogError(
					"Exception:Unable to start the cluster "
							+ cluster.getError(), e);
		}
		// enable HA on cluster nodes.
		enableClusterHA();
		logger.info("startCluster componentStatus : " + componentStatus);
		isStarted = true;
		for (String key : componentStatus.keySet()) {
			isStarted = isStarted && componentStatus.get(key);
		}
		return isStarted;
	}

	/**
	 * Stop all services.
	 * 
	 * @return true, if successful
	 */
	private boolean stopAllServices() {
		boolean isStopped;
		Map<String, Boolean> componentStatus = new HashMap<String, Boolean>();
		// Getting clusterable object using the technology name.
		Clusterable cluster = ObjectFactory
				.getClusterableInstanceById(dbCluster.getTechnology());
		logger.setCluster(cluster.getClusterConf(dbCluster));
		logger.info("Stopping cluster...");

		try {
			// disable HA monitoring on all nodes of cluster.
			disableClusterHA();
			// Get deployable queue
			PriorityQueue<DeployableConf> unDeployableQueue = getComponentQueue(
					cluster, true);

			// iterate over deployable priority queue
			while (unDeployableQueue.size() > 0) {
				// pick component object from unDeployableQueue priority queue
				DeployableConf deployableConf = unDeployableQueue.remove();
				// push component into in progress stack
				Deployable deployer = deployableConf.getDeployer();
				// call stop method on component
				isStopped = deployer.stop(deployableConf.getConfiguration());
				componentStatus.put(deployableConf.getComponentName(),
						isStopped);
			}
			isStopped = true;
		} catch (Exception e) {
			addAndLogError(
					"Exception:Unable to stop the cluster "
							+ cluster.getError(), e);
			isStopped = false;
		}
		logger.info("stopCluster componentStatus : " + componentStatus);
		for (String key : componentStatus.keySet()) {
			isStopped = isStopped && componentStatus.get(key);
		}
		if (!isStopped) {
			enableClusterHA();
		}
		logger.log("Stopping cluster ", isStopped);
		return isStopped;
	}

	/**
	 * Restart all services.
	 * 
	 * @return true, if successful
	 */
	private boolean restartAllServices() {
		logger.info("Restarting cluster...");
		boolean status = false;
		status = stopAllServices();
		status = startAllServices();
		logger.log("Restarting cluster ", status);
		return status;
	}

	/**
	 * Gets the Component Queue.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param desc
	 *            the desc
	 * @return the undeployable queue
	 */
	private PriorityQueue<DeployableConf> getComponentQueue(
			Clusterable cluster, boolean desc) {
		// Creating priority queue based on provided order flag.
		PriorityQueue<DeployableConf> queue = new PriorityQueue<DeployableConf>(
				10, new ComponentComparator(desc));
		// If cluster state is error then no need to undeploy the
		// components.
		if (!this.clusterConfig.getState().equals(Constant.Cluster.State.ERROR)) {

			// Creating component configuration.
			Map<String, Configuration> componentConfig = cluster
					.createConfigs(this.clusterConfig);

			// setting Agent, Ganglia and PreProcessor and PostProcessor
			// configs.
			componentConfig.putAll(ClusterManagerUtils
					.getCommonComponents(this.clusterConfig));

			// iterate over component list
			for (String componentId : componentConfig.keySet()) {
				// create Deployer obj
				Deployable deployer = ObjectFactory
						.getInstanceById(componentId);
				// create DeployableConf(Deployer, Conf) bean class object
				DeployableConf deployableConf = new DeployableConf(deployer,
						componentConfig.get(componentId), componentId);
				// insert DeployableConf in PriorityQueue
				queue.add(deployableConf);
			}
		}
		return queue;
	}

	/**
	 * Gets the service status.
	 * 
	 * @param node
	 *            the node
	 * @return the service status
	 */
	public boolean getServiceStatus(NodeConf node,
			Map<String, Map<String, Boolean>> technologyServiceStatus) {
		// putting services status in map.
		boolean serviceStatus = false;

		// technology service status.
		// if tech service status is not null
		if (technologyServiceStatus != null) {
			serviceStatus = true;
			// Iterating over the technology service status.
			for (String technology : technologyServiceStatus.keySet()) {
				// Iterate over the technology service status.
				serviceStatus = serviceStatus
						&& !technologyServiceStatus.get(technology).values()
								.contains(false);
			}
		}
		// return service status.
		return serviceStatus;
	}

	/**
	 * Isregisterable.
	 */
	public void isregisterable() {
		// Get cluster configuration
		ClusterConf conf = dbCluster.getClusterConf();
		result.put(
				com.impetus.ankush2.constant.Constant.Keys.RegisterCluster.REGISTERABLE_CLUSTER,
				conf.isRegisterableCluster());
	}

	/**
	 * Method to get list of node map.
	 * 
	 * @return
	 */
	public List<Map> getNodeMapList() {
		// list of node map.
		List<Map> nodeList = new ArrayList<Map>();
		try {
			Set<Node> nodes = dbCluster.getNodes();

			// iterating over the nodes
			for (Node dbNode : nodes) {
				NodeConf node = dbNode.getNodeConf();
				if (node.getNodeState().equals(Constant.Node.State.ADDING)
						|| node.getNodeState().equals(
								Constant.Node.State.DEPLOYING)) {
					continue;
				}
				// is ganglia master
				boolean isGangliaMaster = false;
				// set Ganglia Master in type
				if (node.equals(dbCluster.getClusterConf().getGangliaMaster())) {
					node.setType(node.getType() + Strings.FORWARD_SLASH
							+ Constant.Role.GMETAD + Strings.FORWARD_SLASH
							+ Constant.Role.GMOND);
					isGangliaMaster = true;
				} else {
					node.setType(node.getType() + Strings.FORWARD_SLASH
							+ Constant.Role.GMOND);
				}

				node.setType(node.getType() + Strings.FORWARD_SLASH
						+ Constant.Role.AGENT);

				LinkedHashMap nodeMap = JsonMapperUtil.objectFromObject(node,
						LinkedHashMap.class);
				// creating monitoring manager object
				MonitoringManager manager = new MonitoringManager();
				// getting node monitoring data
				NodeMonitoring nodeMonitoring = manager.getMonitoringData(node
						.getId());

				// putting services status in map.
				if (nodeMonitoring != null
						&& nodeMonitoring.getTechnologyServiceStatus() != null) {
					nodeMap.put(
							com.impetus.ankush2.constant.Constant.Keys.SERVICESTATUS,
							getServiceStatus(node,
									nodeMonitoring.getTechnologyServiceStatus()));
					nodeMap.put(
							com.impetus.ankush2.constant.Constant.Keys.DOWN_SERVICES,
							getDownServices(nodeMonitoring));
				} else {
					nodeMap.put(
							com.impetus.ankush2.constant.Constant.Keys.SERVICESTATUS,
							false);
					nodeMap.put(
							com.impetus.ankush2.constant.Constant.Keys.DOWN_SERVICES,
							new HashSet());
				}

				nodeMap.putAll(getNodeUsageMap(nodeMonitoring));
				nodeMap.put(com.impetus.ankush2.constant.Constant.Keys.ID,
						node.getId());
				nodeMap.put(
						com.impetus.ankush2.constant.Constant.Keys.GANGLIA_MASTER,
						isGangliaMaster);
				nodeMap.remove(com.impetus.ankush2.constant.Constant.Keys.MESSAGE);
				nodeMap.remove(com.impetus.ankush2.constant.Constant.Keys.ERRORS);
				nodeList.add(nodeMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
		// return list of node map.
		return nodeList;
	}

}
