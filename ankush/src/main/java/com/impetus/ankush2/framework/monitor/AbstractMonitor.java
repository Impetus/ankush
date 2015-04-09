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
package com.impetus.ankush2.framework.monitor;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zeroturnaround.zip.ZipUtil;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.domain.Service;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.CreateTarArchive;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.UnTarArchive;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush2.common.scripting.impl.AddConfProperty;
import com.impetus.ankush2.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush2.common.scripting.impl.EditConfProperty;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBEventManager;
import com.impetus.ankush2.db.DBHAServiceManager;
import com.impetus.ankush2.db.DBNodeManager;
import com.impetus.ankush2.db.DBOperationManager;
import com.impetus.ankush2.db.DBServiceManager;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.framework.monitor.Graph.StartTime;
import com.impetus.ankush2.framework.monitor.Graph.Type;
import com.impetus.ankush2.framework.utils.ObjectFactory;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;
import com.impetus.ankush2.utils.SSHUtils;

public abstract class AbstractMonitor {
	private static final String AUTH_FAIL_ERR = "Provided password doesn't match. You are not authorized to perform this operation.";

	/** The user manager. */
	private UserManager userManager;

	/**
	 * Sets the user manager.
	 * 
	 * @param userManager
	 *            the new user manager
	 */
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/** Generic log master. */
	protected static GenericManager<Log, Long> logManager = AppStoreWrapper
			.getManager(Constant.Manager.LOG, Log.class);

	/** The operation manager. */
	private GenericManager<Operation, Long> operationManager = AppStoreWrapper
			.getManager(Constant.Manager.OPERATION, Operation.class);

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The logger. */
	protected AnkushLogger logger = new AnkushLogger(getClass());

	/** The errors. */
	protected List<String> errors = new ArrayList<String>();

	/** The result. */
	protected Map<String, Object> result = new HashMap<String, Object>();

	/** The db cluster. */
	protected Cluster dbCluster;

	protected ClusterConfig clusterConf;

	protected Map parameterMap;

	/** The formator. */
	protected static DecimalFormat formator = new DecimalFormat("##");

	protected Map returnResult() {
		if (this.errors.isEmpty()) {
			this.result.put("status", true);
		} else {
			this.result.put("status", false);
			this.result.put("error", this.errors);
		}
		return this.result;
	}

	public void monitor(Cluster cluster, String action, Map parameterMap) {
		this.dbCluster = cluster;
		this.parameterMap = parameterMap;
		result.clear();
		try {
			this.clusterConf = dbCluster.getClusterConfig();
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
				addAndLogError(e.getMessage());
			} else {
				addAndLogError(e.getCause().getMessage());
			}
			// Adding and logging error
			addAndLogError("Unable to process request");
		}
	}

	/**
	 * Adds the and log error.
	 * 
	 * @param errorMsg
	 *            the error msg
	 */
	public void addAndLogError(String errorMsg) {
		this.errors.add(errorMsg);
		logger.error(errorMsg);
	}

	public void addErrorAndLogException(String displayMsg, Exception e) {
		this.errors.add(displayMsg);
		logger.error(displayMsg, e);
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
		String host = null;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.HOST)) {
			host = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.HOST);
		}
		// Getting last operation id.
		Long operationId = getOperationId();
		if (operationId == null) {
			addAndLogError("Could not get logs.");
			return Collections.EMPTY_MAP;
		}
		// Creating Empty property/value map.
		Map<String, Object> propertyValueMap = new HashMap<String, Object>();

		// putting cluster id.
		propertyValueMap.put(
				com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
				dbCluster.getId());

		// putting host public ip
		if (host != null && !host.isEmpty()) {
			propertyValueMap.put(
					com.impetus.ankush2.constant.Constant.Keys.HOST, host);

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
		map.put(com.impetus.ankush2.constant.Constant.Keys.OPERATION_STATE,
				new DBOperationManager().getOperationStatus(dbCluster.getId(),
						operationId));
		return map;
	}

	/**
	 *	 
	 */
	private Long getOperationId() {
		Long operationId = null;
		try {
			Object clusterId = dbCluster.getId();
			// Getting last operation id.
			List<Log> lastOperationLogs = logManager
					.getAllByNamedQuery(
							"getLastOperationId",
							Collections
									.singletonMap(
											com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
											clusterId));

			if (lastOperationLogs == null || lastOperationLogs.isEmpty()) {
				throw new AnkushException(
						"Could not get logs from which to obtain latest operationId.");
			}
			operationId = lastOperationLogs.get(lastOperationLogs.size() - 1)
					.getOperationId();
			if (parameterMap
					.containsKey(com.impetus.ankush2.constant.Constant.Keys.OPERATIONID)) {
				operationId = Long.valueOf(parameterMap.get(
						com.impetus.ankush2.constant.Constant.Keys.OPERATIONID)
						.toString());
			}
		} catch (AnkushException e) {
			addAndLogError(e.getMessage());
		} catch (Exception e) {
			addAndLogError("Could not get operationId.");
		}
		return operationId;
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
	 * Method to get the cluster evnets.
	 * 
	 * @return the map
	 * @author hokam
	 */
	public void events() {
		String node = null;
		Event.Type type = null;
		String category = null;
		String name = null;
		Event.Severity severity = null;

		// set node
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.HOST)) {
			node = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.HOST);
		}

		// set type
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.TYPE)) {
			type = Event.Type.valueOf((String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.TYPE));
		}

		// set category
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.CATEGORY)) {
			category = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.CATEGORY);
		}
		// set name
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.NAME)) {
			name = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.NAME);
		}
		// set severity
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.SEVERITY)) {
			severity = Event.Severity.valueOf((String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.SEVERITY));
		}

		List<Event> events = new DBEventManager().getEvents(dbCluster.getId(),
				node, type, category, name, severity);

		// setting events in result map.
		result.put(com.impetus.ankush2.constant.Constant.Keys.EVENTS, events);
	}

	/**
	 * Method to get the cluster operations.
	 * 
	 * @return the map
	 */
	public void operations() {
		try {
			String opName = null;
			Long operationId = null;
			String status = null;
			// set node
			if (parameterMap
					.containsKey(com.impetus.ankush2.constant.Constant.Keys.OPERATION_NAME)) {
				opName = (String) parameterMap
						.get(com.impetus.ankush2.constant.Constant.Keys.OPERATION_NAME);
			}
			// set type
			if (parameterMap
					.containsKey(com.impetus.ankush2.constant.Constant.Keys.OPERATIONID)) {
				operationId = Long.valueOf(parameterMap.get(
						com.impetus.ankush2.constant.Constant.Keys.OPERATIONID)
						.toString());
			}
			// set category
			if (parameterMap
					.containsKey(com.impetus.ankush2.constant.Constant.Keys.STATUS)) {
				status = (String) parameterMap
						.get(com.impetus.ankush2.constant.Constant.Keys.STATUS);
			}
			List<Operation> operations = new DBOperationManager()
					.getOperations(dbCluster.getId(), opName, operationId,
							status);

			// setting events in result map.
			result.put(com.impetus.ankush2.constant.Constant.Keys.OPERATIONS,
					operations);
		} catch (Exception e) {
			addErrorAndLogException(
					"Could not get operation list. Please view server logs for more details.",
					e);
		}
	}

	public void alerts() {
		String node = null;
		Event.Type type = null;
		String category = null;
		String name = null;
		Event.Severity severity = null;

		// set node
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.HOST)) {
			node = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.HOST);
		}

		// set type
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.TYPE)) {
			type = Event.Type.valueOf((String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.TYPE));
		}

		// set category
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.CATEGORY)) {
			category = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.CATEGORY);
		}
		// set name
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.NAME)) {
			name = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.NAME);
		}

		List<Event> events = new DBEventManager().getAlerts(dbCluster.getId(),
				node, type, category, name);

		// setting events in result map.
		result.put(com.impetus.ankush2.constant.Constant.Keys.EVENTS, events);
	}

	public void groupevents() {
		String node = null;
		Event.Type type = null;
		String category = null;
		String name = null;
		Event.Severity severity = null;

		// set node
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.HOST)) {
			node = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.HOST);
		}

		// set type
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.TYPE)) {
			type = Event.Type.valueOf((String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.TYPE));
		}

		// set category
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.CATEGORY)) {
			category = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.CATEGORY);
		}
		// set name
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.NAME)) {
			name = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.NAME);
		}
		// set severity
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.SEVERITY)) {
			severity = Event.Severity.valueOf((String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.SEVERITY));
		}

		List<Event> events = new DBEventManager().getGroupbyCount(
				dbCluster.getId(), node, type, category, name, severity);

		// setting events in result map.
		result.put(com.impetus.ankush2.constant.Constant.Keys.EVENTS, events);
	}

	/**
	 * Return node level CPU and Memory graph
	 * 
	 * @throws Exserception
	 */
	public void nodegraph() throws Exception {

		// checking existance of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.HOST)
				|| !parameterMap
						.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)) {
			throw new Exception("Either host or start time is missing.");
		}

		// getting starttime
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// getting ip address.
		String host = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.HOST);

		try {
			Graph graph = new Graph(clusterConf);
			// putting the extracted data into the json.
			result.putAll(graph.nodeGraph(startTime, host));
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		}
	}

	/**
	 * Return cluster level summarized graph.
	 * 
	 * @throws Exception
	 */
	public void clustergraph() throws Exception {

		// checking existence of start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)
				|| !parameterMap
						.containsKey(com.impetus.ankush2.constant.Constant.Keys.TYPE)) {
			throw new Exception("Either start time or type is missing.");
		}

		// Getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// Getting type
		Type type = Type.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.TYPE))
				.toLowerCase());

		try {
			Graph graph = new Graph(clusterConf);
			// putting the extracted data into the json.
			result.putAll(graph.clusterGraph(startTime, type));
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		}
	}

	/**
	 * Return pattern based graphs.
	 * 
	 * @throws Exception
	 */
	public void graphs() throws Exception {

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
		String host = null;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			host = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		}

		try {
			Graph graph = new Graph(clusterConf);
			// putting the extracted data into the json.
			result.putAll(graph.getGraph(startTime, pattern, host));
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		}

	}

	/**
	 * Return live graphs.
	 * 
	 * @throws Exception
	 */
	public void sparkline() throws Exception {

		// checking existence of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.STARTTIME)) {
			throw new Exception("Start time is missing.");
		}

		// Update update flag.
		boolean update = false;
		if (parameterMap.containsKey("update")) {
			update = Boolean.parseBoolean((String) parameterMap.get("update"));
		}

		// getting start time
		StartTime startTime = StartTime.valueOf(((String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.STARTTIME))
				.toLowerCase());

		// pattern
		// node ip.
		String host = null;
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.IP)) {
			host = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		}

		try {
			Graph graph = new Graph(clusterConf);
			// putting the extracted data into the json.
			result.putAll(graph.liveGraph(update, host));
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		}
	}

	/**
	 * Method to get legends for the given pattern.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void removeviews() throws Exception {
		// pattern
		String pattern = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.PATTERN);

		// node ip.
		String ip = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);

		// checking existence of ip and start time.
		if (pattern == null || ip == null || pattern.isEmpty() || ip.isEmpty()) {
			throw new Exception("Either ip or pattern is missing.");
		}

		// creating node graph object.
		Graph graph = new Graph(clusterConf);

		// checking existence of ip and start time.
		if (!parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.PATTERN)) {
			throw new Exception("Pattern is missing.");
		}

		Node node = new DBNodeManager().getNode(ip);
		if (node == null) {
			throw new AnkushException("Can not find node.");
		}
		ip = node.getPrivateIp();

		// list of legends.
		List<String> legends = graph.getLegends(ip, pattern);

		// remove the legends from existing saved legends.
		saveGraphViewData(legends, ip, false);
	}

	/**
	 * Method to get legends for the given pattern.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public void legends() throws Exception {
		// pattern
		String pattern = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.PATTERN);

		// node ip.
		String ip = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);

		// checking existence of ip and start time.
		if (pattern == null || ip == null || pattern.isEmpty() || ip.isEmpty()) {
			throw new Exception("Either ip or pattern is missing.");
		}

		// creating node graph object.
		Graph graph = new Graph(clusterConf);

		Node node = new DBNodeManager().getNode(ip);
		if (node == null) {
			throw new AnkushException("Can not find node.");
		}
		ip = node.getPrivateIp();

		// list of legends.
		List<String> legends = graph.getLegends(ip, pattern);

		// save graph view data.
		saveGraphViewData(legends, ip, true);
		// getting legends for the given pattern.
		result.put("legends", legends);
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
	 * Graphtree.
	 */
	public void graphtree() throws Exception {

		// getting the node ip from map.
		String nodeIp = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.IP);
		if (nodeIp == null || nodeIp.isEmpty()) {
			throw new AnkushException("Host is missing.");
		}

		Node node = new DBNodeManager().getNode(nodeIp);
		if (node == null) {
			throw new AnkushException("Can not find node.");
		}
		nodeIp = node.getPrivateIp();

		try {
			Graph graph = new Graph(clusterConf);

			// getting all files from the node.
			List<String> files = graph.getAllFiles(nodeIp);
			SortedSet<String> sortedFiles = new TreeSet<String>(files);

			TreeMap treeMap = populateTreeMap(sortedFiles);
			// putting the treemap against tree key.
			result.put("tree", treeMap);
		} catch (Exception e) {
			addAndLogError(e.getMessage());
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

	public void deployednodes() {
		try {
			// cluster conf.
			for (NodeConfig nodeConf : clusterConf.getNodes().values()) {
				nodeConf.setParams();
			}

			// putting cluster setup details in map.
			result.put("alertsConf", dbCluster.getAlertsConf()
					.getThresholdsMap());
			result.put("nodelist", clusterConf.getNodes().values());
		} catch (Exception e) {
			addAndLogError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void deployingnodes() {
		try {
			// cluster conf.
			Long operationId = clusterConf.getOperationId();
			List<Log> logs = logManager.getAllByNativeQuery(Log
					.getNodeLastLogQuery(dbCluster.getId(), operationId));

			Map<String, String> hostMsgMap = new HashMap<String, String>();
			for (Log log : logs) {
				hostMsgMap.put(log.getHost(), log.getMessage());
			}

			for (NodeConfig nodeConf : clusterConf.getNodes().values()) {
				if (dbCluster
						.getState()
						.equals(com.impetus.ankush2.constant.Constant.Cluster.State.DEPLOYED
								.toString())
						&& nodeConf.getStatus()) {
					nodeConf.setMessage("Deployment Completed.");
				} else {
					nodeConf.setMessage(hostMsgMap.get(nodeConf.getHost()));
				}
			}

			// putting cluster setup details in map.
			result.put("nodelist", clusterConf.getNodes().values());
			result.put("state", clusterConf.getState().toString());
		} catch (Exception e) {
			addAndLogError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void deployingnode() throws AnkushException {
		try {

			if (!parameterMap
					.containsKey(com.impetus.ankush2.constant.Constant.Keys.NODE)) {
				throw new AnkushException("Node is missing.");
			}
			String node = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.NODE);

			NodeConfig nodeConfig = clusterConf.getNodes().get(node);
			if (nodeConfig == null) {
				throw new AnkushException("Invalid node.");
			}

			// putting cluster setup details in map.
			result.put("node", nodeConfig);
		} catch (Exception e) {
			addAndLogError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	public void details() {
		try {
			result.putAll(JsonMapperUtil.mapFromObject(clusterConf));
		} catch (Exception e) {
			addAndLogError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Gets the service tiles.
	 * 
	 * @param technology
	 *            the technology
	 * @return the service tiles
	 */
	protected List<TileInfo> getServiceTiles(String technology) {

		try {
			// list of tile
			List<TileInfo> tiles = new ArrayList<TileInfo>();
			// event manager object.
			DBEventManager eventManager = new DBEventManager();
			// event list.
			List<Event> events = eventManager.getAlerts(dbCluster.getId(),
					null, null, technology, null);

			// roles string
			// String rolesString = Constant.ComponentName
			// .getComponentRoleMap(technology);
			// // split roles string by comma
			// String[] serviceNameArray = rolesString.split(",");
			// // converting array as string list.
			// List<String> roles = Arrays.asList(serviceNameArray);

			Set<String> rolesList = ObjectFactory.getServiceObject(technology)
					.getServiceList(this.clusterConf);

			// tile info
			TileInfo tileInfo = null;
			// if events is not null and not empty
			if (events != null && events.size() > 0 && !events.isEmpty()) {
				// iterating over the roles.
				for (String role : rolesList) {
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
		} catch (Exception e) {
			// TODO: handle exception
			this.addAndLogError("Could not process request to fetch service tiles.");
			return null;
		}
	}

	/**
	 * Gets the display name for node.
	 * 
	 * @param countOfNodes
	 *            the count of nodes
	 * @return the display name for node
	 */
	public static String getDisplayNameForNode(int countOfNodes) {
		String nodes = Constant.Strings.SPACE + "Nodes"
				+ Constant.Strings.SPACE;
		if (countOfNodes == 1) {
			nodes = Constant.Strings.SPACE + "Node" + Constant.Strings.SPACE;
		}
		return nodes;
	}

	public void clustercomponents() {
		try {
			Map<String, Map<String, Object>> componentMap = new HashMap<String, Map<String, Object>>();
			Map<String, ComponentConfig> components = clusterConf
					.getComponents();
			Map<String, Map<String, Integer>> compRoleMap = getCompRoleCountMap();
			for (String component : components.keySet()) {
				ComponentConfig compConf = components.get(component);
				Map<String, Object> internalMap = new HashMap<String, Object>();
				internalMap.put("vendor", compConf.getVendor());
				internalMap.put("version", compConf.getVersion());
				internalMap.put("isRegister", compConf.isRegister());
				if (component
						.equals(com.impetus.ankush2.constant.Constant.Component.Name.AGENT)) {
					continue;
				}
				// A tmp code for fetching Hadoop data -- Needs To be changed
				if (component
						.equals(com.impetus.ankush2.constant.Constant.Component.Name.HADOOP)
						&& compConf.isRegister()
						&& (!HadoopUtils.isMonitoredByAnkush(compConf))) {

					try {
						Set<String> hadoopNodes = new HashSet<String>();
						hadoopNodes.add(HadoopUtils.getNameNodeHost(compConf));
						Set<String> hadoopSlaves = HadoopUtils
								.getSlaveHosts(compConf);
						hadoopNodes.addAll(hadoopSlaves);

						Map<String, Integer> hadoopNodesRoleCount = new LinkedHashMap<String, Integer>();
						hadoopNodesRoleCount.put(
								HadoopConstants.Roles.NAMENODE, 1);
						hadoopNodesRoleCount.put(
								HadoopConstants.Roles.DATANODE,
								hadoopSlaves.size());

						if (HadoopUtils.isHadoop2Config(compConf)) {
							hadoopNodes.add(HadoopUtils
									.getResourceManagerHost(compConf));
							hadoopNodesRoleCount.put(
									HadoopConstants.Roles.RESOURCEMANAGER, 1);
							hadoopNodesRoleCount.put(
									HadoopConstants.Roles.NODEMANAGER,
									hadoopSlaves.size());
						} else {
							hadoopNodes.add(HadoopUtils
									.getJobTrackerHost(compConf));
							hadoopNodesRoleCount.put(
									HadoopConstants.Roles.JOBTRACKER, 1);
							hadoopNodesRoleCount.put(
									HadoopConstants.Roles.TASKTRACKER,
									hadoopSlaves.size());
						}
						internalMap.put("nodes", hadoopNodes.size());
						internalMap.put("roles", hadoopNodesRoleCount);
					} catch (Exception e) {
						internalMap.put("nodes", "--");
						internalMap
								.put("roles", new HashMap<String, Integer>());
					}
				} else {
					internalMap.put("nodes", compConf.getNodes().size());
					internalMap.put("roles", compRoleMap.get(component));
				}
				componentMap.put(component, internalMap);
			}
			result.put("components", componentMap);
		} catch (Exception e) {
			addErrorAndLogException("Error: Could not get component list.", e);
		}

	}

	/**
	 * Gets the comp role count map.
	 * 
	 * @return the comp role count map
	 */
	private Map<String, Map<String, Integer>> getCompRoleCountMap() {
		Map<String, Map<String, Integer>> compRoleCountMap = new HashMap<String, Map<String, Integer>>();
		for (NodeConfig nodeConf : this.clusterConf.getNodes().values()) {
			for (String component : nodeConf.getRoles().keySet()) {
				Map<String, Integer> roleCountMap = new HashMap<String, Integer>();
				Set<String> compRole = nodeConf.getRoles().get(component);
				if (compRoleCountMap.get(component) == null) {
					for (String role : compRole) {
						roleCountMap.put(role, 1);
					}
				} else {
					roleCountMap = compRoleCountMap.get(component);
					for (String role : compRole) {
						if (roleCountMap.get(role) == null) {
							roleCountMap.put(role, 1);
						} else {
							int count = roleCountMap.get(role);
							count++;
							roleCountMap.put(role, count);
						}
					}
				}
				compRoleCountMap.put(component, roleCountMap);
			}
		}
		return compRoleCountMap;
	}

	private Map<String, Set<String>> getRoleNodesMap(String component) {
		Map<String, Set<String>> roleNodesMap = new HashMap<String, Set<String>>();
		for (NodeConfig nodeConf : this.clusterConf.getNodes().values()) {

			for (String role : nodeConf.getRoles().get(component)) {
				if (roleNodesMap.containsKey(role)) {
					roleNodesMap.get(role).add(nodeConf.getHost());
				} else {
					Set<String> hostList = new HashSet<String>();
					hostList.add(nodeConf.getHost());
					roleNodesMap.put(role, hostList);
				}
			}
		}
		return roleNodesMap;
	}

	private Map<String, Set<String>> getNodeRolesMap(String component) {
		Map<String, Set<String>> nodeRolesMap = new HashMap<String, Set<String>>();
		for (NodeConfig nodeConf : this.clusterConf.getNodes().values()) {
			nodeRolesMap.put(nodeConf.getHost(),
					nodeConf.getRoles().get(component));
		}
		return nodeRolesMap;
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
		includeStates
				.add(com.impetus.ankush2.constant.Constant.Node.State.DEPLOYED
						.toString());
		// adding removing state for node
		includeStates
				.add(com.impetus.ankush2.constant.Constant.Node.State.REMOVING
						.toString()); // heat map data object.
		TreeMap heatMapData = new TreeMap();
		// iterating over the nodes
		for (Node node : nodes) {

			// If state id adding then no need to send the heat map data.
			if (!includeStates.contains(node.getState())) {
				continue;
			}

			// getting node monitoring data.
			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(node.getId());

			String usageValue = null;
			// if node monitoring, its monitoring info and its up time info is
			// not null
			if (nodeMonitoring != null
					&& nodeMonitoring.getMonitoringInfo() != null
					&& nodeMonitoring.getMonitoringInfo().getMemoryInfos() != null) {
				// get usage value.
				Double usageValueDouble = nodeMonitoring.getMonitoringInfo()
						.getMemoryInfos().get(0).getUsedPercentage();
				// current usage value.
				if (usageValueDouble != null) {
					usageValue = formator.format(usageValueDouble).toString();
				}
			}

			// Getting the status value for the CPU Usage
			DBEventManager eventManager = new DBEventManager();
			Event event = eventManager.getEvent(null, node.getPublicIp(), null,
					com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
					Constant.Alerts.Metric.MEMORY, null);

			// Getting the severity value.
			String status = Event.Severity.NORMAL.toString();
			if (event != null) {
				status = event.getSeverity().toString();
			}

			// if agent is down making status as unavailable.
			if (DBServiceManager.getManager().isAgentDown(node.getPublicIp())) {
				usageValue = "0";
				status = Constant.Alerts.Severity.UNAVAILABLE;
			}

			// Getting rack info for node.
			String rackId = getRackId(node);
			// update the rack heat map data and put it in main heat map data.
			heatMapData.put(
					rackId,
					updateRackHeatMapData(rackId, node, usageValue, status,
							heatMapData));
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
		includeStates
				.add(com.impetus.ankush2.constant.Constant.Node.State.DEPLOYED
						.toString());
		// adding removing state for node
		includeStates
				.add(com.impetus.ankush2.constant.Constant.Node.State.REMOVING
						.toString());
		// heat map data object.
		TreeMap heatMapData = new TreeMap();
		// iterating over the nodes.
		for (Node node : nodes) {

			// if the node state is available in including state list.
			if (!includeStates.contains(node.getState())) {
				continue;
			}
			// node monitoring object.
			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(node.getId());

			// usage value.
			String usageValue = null;
			// if node monitoring, its monitoring info and its up time info is
			// not null
			if (nodeMonitoring != null
					&& nodeMonitoring.getMonitoringInfo() != null
					&& nodeMonitoring.getMonitoringInfo().getUptimeInfos() != null) {
				// get usage value.
				Double usageValueDouble = nodeMonitoring.getMonitoringInfo()
						.getUptimeInfos().get(0).getCpuUsage();
				// current usage value.
				if (usageValueDouble != null) {
					usageValue = formator.format(usageValueDouble).toString();
				}
			}

			// Getting the status value for the CPU Usage
			DBEventManager eventManager = new DBEventManager();
			// Getting the event for the node.
			Event event = eventManager.getEvent(null, node.getPublicIp(), null,
					com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
					Constant.Alerts.Metric.CPU, null);

			// Getting the severity value.
			String status = Event.Severity.NORMAL.toString();
			if (event != null) {
				status = event.getSeverity().toString();
			}

			// if agent is down making status as unavailable.
			if (DBServiceManager.getManager().isAgentDown(node.getPublicIp())) {
				usageValue = "0";
				status = Constant.Alerts.Severity.UNAVAILABLE;
			}
			// Getting rack info for node.
			String rackId = getRackId(node);
			// update the rack heat map data and put it in main heat map data.
			heatMapData.put(
					rackId,
					updateRackHeatMapData(rackId, node, usageValue, status,
							heatMapData));
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
	 * Gets the rack id.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @return the rack id
	 */
	public static String getRackId(Node node) {
		String dc = node.getNodeConfig().getDatacenter();
		String rackId = node.getNodeConfig().getRack();
		String rackInfo = new String();

		if (dc != null && !dc.isEmpty()) {
			if (!dc.contains("/")) {
				dc = "/" + dc;
			}
		} else {
			dc = "";
		}

		if (rackId != null && !rackId.isEmpty()) {
			if (!rackId.contains("/")) {
				rackId = "/" + rackId;
			}
		} else {
			rackId = "";
		}

		rackInfo = dc + rackId;
		return rackInfo;
	}

	/**
	 * Method to get cluster services.
	 */
	public void haservices() {
		try {
			// setting services.
			result.put(com.impetus.ankush2.constant.Constant.Keys.SERVICES,
					new DBHAServiceManager().getHAServices(dbCluster));
		} catch (Exception e) {
			addErrorAndLogException("Couldn't find HA services", e);
		}
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
		// returning services.
		return services;
	}

	/**
	 * Method to get node roles.
	 * 
	 * @param node
	 * @return
	 */
	private Set<String> getNodeRoles(Node node) {
		Set<String> roles = new HashSet<String>();
		// Empty role set.
		Map<String, Set<String>> map = node.getNodeConfig().getRoles();
		for (Set<String> role : map.values()) {
			roles.addAll(role);
		}		
		// return roles.
		return roles;
	}

	/**
	 * Rest API to view the logs View.
	 */
	public void view() {

		String errMsg = "Exception: Unable to process request to view log file.";

		// Getting the ip address from parameter map.
		String host = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.HOST);

		// Getting the filename from parameter map.
		String logfilePath = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.LOG_FILE_PATH);

		if (host == null || logfilePath == null) {
			errors.add("Please give proper Host and Filepath to view logs");
			return;
		}
		try {

			// Getting the readCount value
			int readCount = ParserUtil.getIntValue((String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.READCOUNT),
					0);

			int bytesCount = ParserUtil
					.getIntValue(
							(String) parameterMap
									.get(com.impetus.ankush2.constant.Constant.Keys.BYTESCOUNT),
							0);

			// Create log view handler object.
			LogViewHandler logHandler = new LogViewHandler(host,
					this.clusterConf.getAuthConf());
			// Adding HTML break tag
			// To be changed when Error Div on UI will support \n character
			errMsg += "<br>NodeIP-" + host + "<br>Type-" + logfilePath;

			// putting the file content with total read characters.
			Map<String, String> content;
			content = logHandler.getFileContent(logfilePath, readCount,
					bytesCount, clusterConf.getAgentInstallDir());
			result.putAll(content);
		} catch (AnkushException e) {
			// Adding error and logging exception
			addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			// Adding error and logging exception
			addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Rest API to download the logs Download.
	 */
	public void download() {

		String errMsg = "Exception: Unable to process request to download log file.";

		// Getting the ip address from parameter map.
		String host = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.HOST);

		// Getting the filename from parameter map.
		String logFilePath = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.LOG_FILE_PATH);

		if (host == null || logFilePath == null) {
			errors.add("Please give proper Host and Filepath to view logs");
			return;
		}

		try {

			// Create the log View Handler object.
			// Create log view handler object.
			LogViewHandler logHandler = new LogViewHandler(host,
					this.clusterConf.getAuthConf());

			// Adding HTML break tag
			// To be changed when Error Div on UI will support \n character
			errMsg += "<br>NodeIP-" + host + "<br>Type-" + logFilePath;

			// setting download path.
			String downloadPath = "";

			downloadPath = logHandler.downloadFile(this.clusterConf.getName(),
					logFilePath, clusterConf.getAgentInstallDir());
			result.put(com.impetus.ankush2.constant.Constant.Keys.DOWNLOADPATH,
					downloadPath);

		} catch (AnkushException e) {
			// Adding error and logging exception
			addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			// Adding error and logging exception
			addErrorAndLogException(errMsg, e);
		}
	}

	protected boolean addParam(NodeConfig nodeConfig, String componentName,
			String paramName, String paramValue, String propertyFilePath,
			String fileExtension) {
		try {
			logger.info("Adding " + paramName + " in " + propertyFilePath
					+ " file.", componentName, nodeConfig.getHost());

			AnkushTask addParam = new AddConfProperty(paramName, paramValue,
					propertyFilePath, fileExtension,
					clusterConf.getAgentInstallDir());
			Result res = nodeConfig.getConnection().exec(addParam);
			if (res.rc == 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Unable to add " + paramName + " in "
					+ propertyFilePath + " file.", componentName, e);
		}
		this.errors.add("Unable to add " + paramName + " in "
				+ propertyFilePath + " file.");
		return false;
	}

	protected boolean editParam(NodeConfig nodeConfig, String componentName,
			String paramName, String paramValue, String propertyFilePath,
			String fileExtension) {
		try {
			logger.info("Updating " + paramName + " in " + propertyFilePath
					+ " file.", componentName, nodeConfig.getHost());
			AnkushTask editParam = new EditConfProperty(paramName, paramValue,
					propertyFilePath, fileExtension,
					clusterConf.getAgentInstallDir());
			Result res = nodeConfig.getConnection().exec(editParam);
			if (res.rc == 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Unable to edit " + paramName + " in "
					+ propertyFilePath + " file.", componentName, e);
		}
		this.errors.add("Unable to edit " + paramName + " in "
				+ propertyFilePath + " file.");
		return false;
	}

	protected boolean deleteParam(NodeConfig nodeConfig, String componentName,
			String paramName, String propertyFilePath, String fileExtension) {
		try {
			logger.info("Deleting " + paramName + " from " + propertyFilePath
					+ " file.", componentName, nodeConfig.getHost());
			AnkushTask deleteParam = new DeleteConfProperty(paramName,
					propertyFilePath, fileExtension,
					clusterConf.getAgentInstallDir());
			Result res = nodeConfig.getConnection().exec(deleteParam);
			if (res.rc == 0) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Unable to delete " + paramName + " from "
					+ propertyFilePath + " file.", componentName, e);
		}
		this.errors.add("Unable to delete " + paramName + " from "
				+ propertyFilePath + " file.");
		return false;
	}

	public void services() {
		String host = null;
		String category = null;
		String service = null;
		Boolean ha = null;
		Boolean stop = null;
		Boolean status = null;

		// set node
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.HOST)) {
			host = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.HOST);
		}

		// set component
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.CATEGORY)) {
			category = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.CATEGORY);
		}

		// set service
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.SERVICE)) {
			service = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.SERVICE);
		}

		// set ha
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.HA)) {
			ha = (Boolean) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.HA);
		}

		// set stop
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.STOP)) {
			stop = (Boolean) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.STOP);
		}

		// set status
		if (parameterMap
				.containsKey(com.impetus.ankush2.constant.Constant.Keys.STATUS)) {
			status = (Boolean) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.STATUS);
		}

		List<Service> services = DBServiceManager.getManager().getServices(
				dbCluster.getId(), host, category, service, ha, stop, status);

		// checking Agent status and if Agent is down, removing all other
		// services from list
		for (Service serv : services) {
			if (serv.getComponent().equals(
					com.impetus.ankush2.constant.Constant.Component.Name.AGENT)
					&& serv.getService()
							.equals(com.impetus.ankush2.constant.Constant.Component.Name.AGENT)
					&& !serv.getStatus()) {
				// setting events in result map.
				result.put(com.impetus.ankush2.constant.Constant.Keys.SERVICES,
						new ArrayList<Service>(Arrays.asList(serv)));
				return;
			}
		}

		// setting events in result map.
		result.put(com.impetus.ankush2.constant.Constant.Keys.SERVICES,
				services);
	}

	protected boolean checkPaswordCorrect() {
		boolean passwordOk = false;
		String userName = (String) parameterMap.get("loggedUser");
		String password = (String) parameterMap.get("password");
		System.out.println(" user : " + userName + "  Pwd :" + password);
		try {
			if (userManager == null) {
				ApplicationContext applicationContext = AppStoreWrapper
						.getApplicationContext();
				userManager = (UserManager) applicationContext
						.getBean("userManager");
			}
			passwordOk = userManager.doesPasswordMatch(userName, password);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		if (!passwordOk)
			addPasswordMismatchError();
		return passwordOk;
	}

	protected void addPasswordMismatchError() {
		addAndLogError(AUTH_FAIL_ERR);
	}

	/**
	 * Components.
	 */
	public void components() {
		// putting the components
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
		String host = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.HOST);
		// Get the db node info using public IP
		Node node = nodeManager.getByPropertyValueGuarded(
				com.impetus.ankush2.constant.Constant.Keys.PUBLICIP, host);
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
		NodeConfig nodeConfig = node.getNodeConfig();
		// empty components.
		Set<String> components = new HashSet<String>();
		// iterate over the components.
		for (String componentName : this.dbCluster.getClusterConfig()
				.getComponents().keySet()) {
			// Get Component configuration.
			ComponentConfig compConfig = dbCluster.getClusterConfig()
					.getComponents().get(componentName);
			// if node is part of nodes.
			if (compConfig.getNodes().keySet().contains(nodeConfig.getHost())) {
				// add component.
				components.add(componentName);
			}
		}
		// return components.
		return components;
	}

	public void logfilters() {
		String component = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.COMPONENT);
		if (component == null || component.isEmpty()) {
			this.addAndLogError("Invalid Log filters request: Please specify a component.");
			return;
		}
		try {
			result.put(Constant.JsonKeys.Logs.ROLES,
					this.getRoleNodesMap(component));
			result.put(Constant.JsonKeys.Logs.NODES,
					this.getNodeRolesMap(component));
		} catch (Exception e) {
			this.addErrorAndLogException("Could not get Log filters for "
					+ component + ".", e);
		}
	}

	public void downloadlogs() {
		final String component = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.COMPONENT);
		if (component == null || component.isEmpty()) {
			this.addAndLogError("Invalid Log request: Please specify a component.");
			return;
		}
		try {
			ArrayList<String> nodes = (ArrayList) parameterMap
					.get(Constant.JsonKeys.Logs.NODES);
			if (nodes == null || nodes.isEmpty()) {
				nodes = new ArrayList<String>(this.clusterConf.getComponents()
						.get(component).getNodes().keySet());
			}

			ArrayList<String> roles = (ArrayList) parameterMap
					.get(Constant.JsonKeys.Logs.ROLES);

			Serviceable serviceableObj = ObjectFactory
					.getServiceObject(component);

			if (roles == null || roles.isEmpty()) {
				roles = new ArrayList<String>(
						serviceableObj.getServiceList(this.clusterConf));
			}

			String clusterResourcesLogsDir = AppStoreWrapper
					.getClusterResourcesPath() + "logs/";

			String clusterLogsDirName = "Logs_" + this.clusterConf.getName()
					+ "_" + System.currentTimeMillis();

			String clusterLogsArchiveName = clusterLogsDirName + ".zip";

			final String cmpLogsDirPathOnServer = clusterResourcesLogsDir
					+ clusterLogsDirName + "/" + component + "/";

			if (!FileUtils.ensureFolder(cmpLogsDirPathOnServer)) {
				this.addAndLogError("Could not create log directory for "
						+ component + " on server.");
				return;
			}

			final Semaphore semaphore = new Semaphore(nodes.size());
			final ArrayList<String> rolesObj = new ArrayList<String>(roles);
			try {
				for (final String host : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							NodeConfig nodeConfig = clusterConf.getNodes().get(
									host);

							SSHExec connection = SSHUtils.connectToNode(host,
									clusterConf.getAuthConf());
							if (connection == null) {
								// TODO: handle Error
								logger.error(
										"Could not fetch log files - Connection not initialized",
										component, host);
							}
							Serviceable serviceableObj = null;
							try {
								serviceableObj = ObjectFactory
										.getServiceObject(component);

								for (String role : rolesObj) {
									if (nodeConfig.getRoles().get(component)
											.contains(role)) {

										String tmpLogsDirOnServer = cmpLogsDirPathOnServer
												+ "/" + role + "/" + host + "/";
										if (!FileUtils
												.ensureFolder(tmpLogsDirOnServer)) {
											// TODO: handle Error
											// Log error in operation table and
											// skip
											// this role
											continue;
										}

										String nodeLogsDirPath = FileUtils
												.getSeparatorTerminatedPathEntry(serviceableObj
														.getLogDirPath(
																clusterConf,
																host, role));
										String logFilesRegex = serviceableObj
												.getLogFilesRegex(clusterConf,
														host, role, null);
										String outputTarArchiveName = role
												+ "_"
												+ +System.currentTimeMillis()
												+ ".tar.gz";
										try {
											List<String> logsFilesList = AnkushUtils
													.listFilesInDir(connection,
															host,
															nodeLogsDirPath,
															logFilesRegex);
											AnkushTask ankushTask = new CreateTarArchive(
													nodeLogsDirPath,
													nodeLogsDirPath
															+ outputTarArchiveName,
													logsFilesList);
											if (connection.exec(ankushTask).rc != 0) {
												// TODO: handle Error
												// Log error in operation table
												// and
												// skip this
												// role
												continue;
											}
											connection
													.downloadFile(
															nodeLogsDirPath
																	+ outputTarArchiveName,
															tmpLogsDirOnServer
																	+ outputTarArchiveName);
											ankushTask = new Remove(
													nodeLogsDirPath
															+ outputTarArchiveName);
											connection.exec(ankushTask);

											ankushTask = new UnTarArchive(
													tmpLogsDirOnServer
															+ outputTarArchiveName,
													tmpLogsDirOnServer);

											Runtime.getRuntime()
													.exec(ankushTask
															.getCommand())
													.waitFor();
											ankushTask = new Remove(
													tmpLogsDirOnServer
															+ outputTarArchiveName);
											Runtime.getRuntime()
													.exec(ankushTask
															.getCommand())
													.waitFor();
										} catch (Exception e) {
											e.printStackTrace();
											// TODO: handle exception
											// Log error in operation table and
											// skip
											// this role
											continue;
										}
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								return;
							} finally {
								if (semaphore != null) {
									semaphore.release();
								}
								if (connection != null) {
									connection.disconnect();
								}
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {

			}

			ZipUtil.pack(
					new File(clusterResourcesLogsDir + clusterLogsDirName),
					new File(clusterResourcesLogsDir + clusterLogsArchiveName),
					true);

			org.apache.commons.io.FileUtils.deleteDirectory(new File(
					clusterResourcesLogsDir + clusterLogsDirName));

			result.put(com.impetus.ankush2.constant.Constant.Keys.DOWNLOADPATH,
					clusterResourcesLogsDir + clusterLogsArchiveName);
		} catch (Exception e) {
			this.addAndLogError("Could not download logs for " + component
					+ ".");
			logger.error(e.getMessage(), component, e);
		}
	}

	public void downloadlogfiles() {

		String component = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.COMPONENT);

		if (component == null || component.isEmpty()) {
			this.addAndLogError("Invalid Log request: Please specify a component.");
			return;
		}

		try {

		} catch (Exception e) {
			this.addAndLogError("Could not download log files for " + component
					+ ".");
			logger.error(e.getMessage(), component, e);
		}
	}

	public void getlogfiles() {
		String component = (String) parameterMap
				.get(com.impetus.ankush2.constant.Constant.Keys.COMPONENT);

		Set<String> nodes = (Set) parameterMap
				.get(Constant.JsonKeys.Logs.NODES);

		Set<String> roles = (Set) parameterMap
				.get(Constant.JsonKeys.Logs.ROLES);

		if (component == null || component.isEmpty()) {
			this.addAndLogError("Invalid Log request: Please specify a component.");
			return;
		}

		try {

		} catch (Exception e) {
			this.addAndLogError("Could not get Log files for " + component
					+ ".");
		}
	}

	public void clusterInstallationtype() {
		// To be changed -- Key definition in Constant file
		try {
			this.result.put("installationType",
					this.clusterConf.getInstallationType());
		} catch (Exception e) {
			addErrorAndLogException(
					"Exception in getting installation type of this cluster.",
					e);
		}
	}

	public void canNodesBeDeleted() {
		try {
			List<String> nodes = (List) parameterMap
					.get(Constant.JsonKeys.Logs.NODES);
			Map<String, List<String>> compNodeMap = getComponentNodesDeletionMap(nodes);
			boolean deletionPossible = true;
			for (String component : compNodeMap.keySet()) {
				AbstractMonitor monitor = ObjectFactory
						.getMonitorObject(component);
				if (monitor != null) {
					deletionPossible = monitor.canNodesBeDeleted(
							this.clusterConf, compNodeMap.get(component), component)
							&& deletionPossible;
					this.errors.addAll(monitor.errors);
				}
			}
			if (!deletionPossible && this.errors.isEmpty()) {
				addAndLogError("Could not delete nodes.");
			}
		} catch (Exception e) {
			addAndLogError("Exception while getting status for nodes deletion");
		}
	}

	private Map<String, List<String>> getComponentNodesDeletionMap(
			List<String> nodes) {
		Map<String, List<String>> componentMap = new HashMap<String, List<String>>();
		try {
			// iterate over all the incoming nodes to create
			// <Component,Collection<NodeHost>> map
			for (String host : nodes) {
				// if node came for removal doesn't exist in the cluser,add
				// Error
				// and return
				if (!clusterConf.getNodes().containsKey(host)) {
					addAndLogError("Host - " + host
							+ " doesn't exist in the cluster.");
					continue;
				}
				if (this.errors.size() > 0) {
					return null;
				}
				// create componentMap containg all the nodes to be removed for
				// that component
				Set<String> components = new HashSet<String>(clusterConf
						.getNodes().get(host).getRoles().keySet());
				if (!components.isEmpty()) {
					for (String comp : components) {
						if (!componentMap.containsKey(comp)) {
							componentMap.put(comp,
									new ArrayList<String>(Arrays.asList(host)));
						} else {
							componentMap.get(comp).add(host);
						}
					}
				} else {
					addAndLogError("No component exist on this host : " + host);
				}
			}
		} catch (Exception e) {
			addAndLogError("Couldn't get the components for nodes : " + nodes);
		}
		return componentMap;

	}

	public abstract boolean canNodesBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes, String componentName);
}
