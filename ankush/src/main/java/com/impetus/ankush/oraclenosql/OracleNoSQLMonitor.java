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

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.LogRecord;

import oracle.kv.impl.admin.CommandServiceAPI;
import oracle.kv.impl.admin.VerifyConfiguration;
import oracle.kv.impl.admin.VerifyConfiguration.Problem;
import oracle.kv.impl.admin.VerifyResults;
import oracle.kv.impl.admin.criticalevent.CriticalEvent;
import oracle.kv.impl.admin.criticalevent.CriticalEvent.EventType;
import oracle.kv.impl.admin.param.Parameters;
import oracle.kv.impl.admin.param.RepNodeParams;
import oracle.kv.impl.admin.param.StorageNodeParams;
import oracle.kv.impl.admin.plan.ChangeAdminParamsPlan;
import oracle.kv.impl.admin.plan.ChangeAllParamsPlan;
import oracle.kv.impl.admin.plan.ChangeParamsPlan;
import oracle.kv.impl.admin.plan.ChangeSNParamsPlan;
import oracle.kv.impl.admin.plan.DeployAdminPlan;
import oracle.kv.impl.admin.plan.DeployDatacenterPlan;
import oracle.kv.impl.admin.plan.DeploySNPlan;
import oracle.kv.impl.admin.plan.DeployStorePlan;
import oracle.kv.impl.admin.plan.DeployTopoPlan;
import oracle.kv.impl.admin.plan.MigrateSNPlan;
import oracle.kv.impl.admin.plan.Plan;
import oracle.kv.impl.admin.plan.PlanRun;
import oracle.kv.impl.admin.plan.StartRepNodesPlan;
import oracle.kv.impl.admin.plan.StopRepNodesPlan;
import oracle.kv.impl.admin.plan.task.DeployAdmin;
import oracle.kv.impl.monitor.Tracker.RetrievedEvents;
import oracle.kv.impl.param.ParameterMap;
import oracle.kv.impl.topo.AdminId;
import oracle.kv.impl.topo.RepGroup;
import oracle.kv.impl.topo.RepNode;
import oracle.kv.impl.topo.RepNodeId;
import oracle.kv.impl.topo.StorageNodeId;
import oracle.kv.impl.topo.Topology;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.ganglia.Graph;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;

/**
 * It provides cluster level monitoring.
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLMonitor extends AbstractMonitor {

	/** The Constant LASTTIMESTAMP. */
	private static final String LASTTIMESTAMP = "lasttimestamp";

	/** The Constant STATE. */
	private static final String STATE = "state";

	/** The Constant MIN_REP_FACTOR. */
	private static final int MIN_REP_FACTOR = 3;

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			OracleNoSQLMonitor.class);

	/** The log manager to interact with log table. */
	private static GenericManager<Log, Long> logManager = AppStoreWrapper
			.getManager("logManager", Log.class);

	/** The conf. */
	private OracleNoSQLConf conf = null;

	/**
	 * Class constructor.
	 */
	public OracleNoSQLMonitor() {
		logger.removeAppender();
	}

	/**
	 * Instantiates a new oracle no sql monitor.
	 * 
	 * @param conf
	 *            the conf
	 */
	public OracleNoSQLMonitor(OracleNoSQLConf conf) {
		this();
		this.conf = conf;
	}

	/**
	 * Get cluster overview.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void clusteroverview() throws AnkushException {
		// Get Topology
		OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
				.getTechnologyData(this.conf);
		List<NodeConf> nodeConfs = null;
		if (this.clusterConfig != null) {
			nodeConfs = this.conf.getNewNodes();
		}
		if (techData != null) {
			result.putAll(techData.getClusterOverview(this.conf, nodeConfs,
					dbCluster));
		} else {
			throw new AnkushException("Could not get topology.");
		}
	}

	/**
	 * Get Store Verification.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void verify() throws AnkushException {
		CommandServiceAPI cs = OracleNoSQLManager.getAdminClient(this.conf);

		StringBuilder sb = new StringBuilder();
		VerifyResults vr = null;
		// Display topology
		try {
			sb.append(VerifyConfiguration.displayTopology(cs.getTopology()))
					.append("\n");
			vr = cs.verifyConfiguration(true, true);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get verify result.");
		}

		// Topology problems
		List<Problem> problems = vr.getViolations();
		// Topology warnings
		List<Problem> warnings = vr.getWarnings();

		if (warnings != null && !warnings.isEmpty()) {
			sb.append("Verification complete, ")
					.append(problems == null ? 0 : problems.size())
					.append(" violations, ")
					.append(warnings == null ? 0 : warnings.size())
					.append(" note found.").append("\n");
		} else if (problems != null && !problems.isEmpty()) {
			sb.append("Verification complete, ")
					.append(problems == null ? 0 : problems.size())
					.append(" violations.").append("\n");
		} else {
			sb.append("Verification complete, no violations.").append("\n");
		}
		// Add problems and warnings
		for (Problem problem : problems) {
			sb.append(problem.toString()).append("\n");
		}
		for (Problem problem : warnings) {
			sb.append(problem.toString()).append("\n");
		}

		result.put("verify", sb.toString());
	}

	/**
	 * Get Admin parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void adminparams() throws AnkushException {
		ParameterMap params = null;
		List<String> keys = null;

		if (!parameterMap.containsKey("all")) {
			// List of Admin Params
			keys = Constant.OracleNoSQL.Admin.EDITABLE_PARAMETERS;
		}
		try {
			// Fetch admin params using adminId
			if (parameterMap.containsKey("id")) {
				int adminId = Integer.parseInt((String) parameterMap.get("id"));
				params = OracleNoSQLManager.getAdminClient(this.conf)
						.getParameters().get(new AdminId(adminId)).getMap();
			} else {
				params = OracleNoSQLManager.getAdminClient(this.conf)
						.getAdmins().get(0);
			}
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get admin parameters.");
		}
		if (!setParameters(keys, params)) {
			addError("Could not get admin parameters.");
		}
	}

	/**
	 * Set parameters into result.
	 * 
	 * @param keys
	 *            Parameter keys
	 * @param params
	 *            Parameter map
	 * @return true, if successful
	 */
	private boolean setParameters(List<String> keys, ParameterMap params) {
		if (params != null) {
			if (keys == null) {
				keys = new ArrayList<String>(params.keys());
			}
			for (String k : keys) {
				result.put(k, params.get(k).toString() != null ? params.get(k)
						.toString() : "");
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * All Replication Node prameters.
	 */
	private void allrepnodeparams() {
		// List of All Replication Node params
		List<String> keys = Constant.OracleNoSQL.AllRepNodes.EDITABLE_PARAMETERS;

		for (String k : keys) {
			result.put(k, "");
		}
	}

	/**
	 * Get Policy Parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void policyparams() throws AnkushException {
		List<String> keys = null;
		if (!parameterMap.containsKey("all")) {
			// List of policy parameters
			keys = Constant.OracleNoSQL.Policy.EDITABLE_PARAMETERS;
		}

		ParameterMap params = null;
		try {
			params = OracleNoSQLManager.getAdminClient(this.conf)
					.getPolicyParameters();
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get policy parameters.");
		}
		if (!setParameters(keys, params)) {
			addError("Unable to get policy parameters.");
		}
	}

	/**
	 * Get Replication Node parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void repnodeparams() throws AnkushException {
		if (!parameterMap.containsKey("rgid")
				|| !parameterMap.containsKey("rnid")) {
			throw new AnkushException("Either rgid or rnid is missing");
		}

		int groupId = Integer.parseInt((String) parameterMap.get("rgid"));
		int nodeNum = Integer.parseInt((String) parameterMap.get("rnid"));
		List<String> keys = null;
		if (!parameterMap.containsKey("all")) {
			// List of Replication Node params
			keys = Constant.OracleNoSQL.RepNode.EDITABLE_PARAMETERS;
		}

		RepNodeParams rnp = null;
		try {
			rnp = OracleNoSQLManager.getAdminClient(this.conf).getParameters()
					.get(new RepNodeId(groupId, nodeNum));
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException(
					"Could not get replication parameters for RG" + groupId
							+ "-RN" + nodeNum + ".");
		}

		if (rnp == null || !setParameters(keys, rnp.getMap())) {
			addError("Could not get replication parameters for RG" + groupId
					+ "-RN" + nodeNum + ".");
		}
	}

	/**
	 * Get StorageNode parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void storagenodeparams() throws AnkushException {
		if (!parameterMap.containsKey("id")) {
			throw new AnkushException("Id is missing.");
		}

		int snId = Integer.parseInt((String) parameterMap.get("id"));
		List<String> keys = null;
		if (!parameterMap.containsKey("all")) {
			// List of Storage Node params
			keys = Constant.OracleNoSQL.StorageNode.EDITABLE_PARAMETERS;
		}

		StorageNodeParams snp;
		try {
			snp = OracleNoSQLManager.getAdminClient(this.conf).getParameters()
					.get(new StorageNodeId(snId));
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException(
					"Could not get storage node parameters for SN" + snId + ".");
		}
		if (snp == null || !setParameters(keys, snp.getMap())) {
			addError("Could not get storage node parameters for SN" + snId
					+ ".");
		}
	}

	/**
	 * Get plans overview.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void planhistory() throws AnkushException {
		ArrayList<ArrayList<String>> planHistory = new ArrayList<ArrayList<String>>();

		try {
			Map<Integer, Plan> plans = OracleNoSQLManager.getAdminClient(
					this.conf).getPlans();
			// Sort plans
			SortedSet<Integer> sortedKeys = new TreeSet<Integer>(plans.keySet());
			for (Integer key : sortedKeys) {
				Plan plan = plans.get(key);
				// Set Plan parameters
				ArrayList<String> array = new ArrayList<String>();
				array.add(Integer.toString(plan.getId()));
				array.add(plan.getName());
				array.add(plan.getClass().getSimpleName());
				array.add(plan.getState().toString());
				array.add(Integer.valueOf(
						plan.getExecutionState().getHistory().size())
						.toString());
				array.add(plan.getStartTime() == null ? "" : plan
						.getStartTime().toString());
				array.add(plan.getEndTime() == null ? "" : plan.getEndTime()
						.toString());
				planHistory.add(array);
			}
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get plans.");
		}

		result.put("planHistory", planHistory);
	}

	/**
	 * Get Store logs.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void storelogs() throws AnkushException {
		// Set default start and end time
		long startTime = 0;

		// Set start time
		if (parameterMap.containsKey(LASTTIMESTAMP)) {
			startTime = Long
					.parseLong((String) parameterMap.get(LASTTIMESTAMP)) + 1;
		}

		ArrayList<ArrayList<String>> array = null;
		// Get logs
		RetrievedEvents<LogRecord> events = null;
		try {
			events = OracleNoSQLManager.getAdminClient(this.conf).getLogSince(
					startTime);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get store logs.");
		}
		List<LogRecord> logs = events.getEvents();

		if (logs != null) {
			// Sort logs
			Collections.sort(logs, new LogComparator());
			array = new ArrayList<ArrayList<String>>();
			for (LogRecord lr : logs) {
				ArrayList<String> a = new ArrayList<String>();
				a.add(formatLogRecord(lr));
				array.add(a);
			}

			// Set last log message time
			if (logs.size() > 0) {
				startTime = logs.get(logs.size() - 1).getMillis() + 1;
			}

			result.put(STATE, dbCluster.getState());
			result.put("logs", array);
			result.put(LASTTIMESTAMP, events.getLastSyntheticTimestamp());
		}
	}

	/**
	 * Get individual plan.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void plan() throws AnkushException {
		Plan plan = getPlan();
		addPlanHistory(plan);
		// Set plan parameters
		Map params = new HashMap();

		if (plan.getClass() == DeployDatacenterPlan.class) {
			// Set DeployDatacenterPlan parameters
			params.put("Datacenter Name",
					((DeployDatacenterPlan) plan).getDatacenterName());
			params.put("Replication Factor",
					((DeployDatacenterPlan) plan).getRepFactor());
		} else if (plan.getClass() == DeploySNPlan.class) {
			// Set DeploySNPlan parameters
			DeploySNPlan dsnp = (DeploySNPlan) plan;
			StorageNodeParams snp = dsnp.getInputStorageNodeParams();
			params.put("Datacenter Id",
					dsnp.getTopology().get(dsnp.getStorageNodeId())
							.getDatacenterId().getDatacenterId());
			params.put("Host", snp.getHostname());
			params.put("Registry Port", snp.getRegistryPort());
			params.put("Comment", snp.getComment());
		} else if (plan.getClass() == DeployAdminPlan.class) {
			// Set DeployAdminPlan parameters
			List tl = ((DeployAdminPlan) plan).getTaskList().getTasks();
			DeployAdmin t = (DeployAdmin) tl.iterator().next();
			params.put("Storage Node", t.getSnId().getStorageNodeId());
			params.put("Admin Port", ((DeployAdminPlan) plan).getHttpPort());
		} else if (plan.getClass() == DeployStorePlan.class) {
			// Set DeployStorePlan parameters
			DeployStorePlan dsp = (DeployStorePlan) plan;
			params.put("Pool Name", dsp.getTargetPool().getName());
			params.put("Replication Factor", dsp.getRepFactor());
			params.put("Partition #", dsp.getNumPartitions());
		} else if (plan.getClass() == StopRepNodesPlan.class) {
			// Set StopRepNodesPlan parameters
			StopRepNodesPlan stoprnp = (StopRepNodesPlan) plan;
			params.put("RepNodes", repNodeIdsToString(stoprnp.getRepNodeIds()));
		} else if (plan.getClass() == StartRepNodesPlan.class) {
			// Set StartRepNodesPlan parameters
			StartRepNodesPlan startrnp = (StartRepNodesPlan) plan;
			params.put("RepNodes", repNodeIdsToString(startrnp.getRepNodeIds()));
		} else if (plan.getClass() == ChangeSNParamsPlan.class) {
			// Set ChangeSNParamsPlan parameters
			ChangeSNParamsPlan csnpp = (ChangeSNParamsPlan) plan;
			for (Entry e : csnpp.getNewParams().getMap().entrySet()) {
				params.put(e.getKey(), e.getValue().toString());
			}
		} else if (plan.getClass() == ChangeParamsPlan.class) {
			// Set ChangeParamsPlan parameters
			ChangeParamsPlan cpp = (ChangeParamsPlan) plan;
			for (Entry e : cpp.getNewParams().getMap().entrySet()) {
				params.put(e.getKey(), e.getValue().toString());
			}
		} else if (plan.getClass() == ChangeAllParamsPlan.class) {
			// Set ChangeAllParamsPlan parameters
			ChangeAllParamsPlan capp = (ChangeAllParamsPlan) plan;
			for (Entry e : capp.getNewParams().getMap().entrySet()) {
				params.put(e.getKey(), e.getValue().toString());
			}
		} else if (plan.getClass() == ChangeAdminParamsPlan.class) {
			// Set ChangeAdminParamsPlan parameters
			ChangeAdminParamsPlan capp = (ChangeAdminParamsPlan) plan;
			for (Entry e : capp.getNewParams().getMap().entrySet()) {
				params.put(e.getKey(), e.getValue().toString());
			}
		} else if (plan.getClass() == MigrateSNPlan.class) {
			// Set MigrateSNPlan parameters
			MigrateSNPlan msnp = (MigrateSNPlan) plan;
			params.put("Old StorageNode", msnp.getOldNode().toString());
			params.put("New StorageNode", msnp.getNewNode().toString());
			params.put("New http Port", msnp.getHttpPort());
		} else if (plan.getClass() == DeployTopoPlan.class) {
			// Set DeployTopoPlan parameters
			DeployTopoPlan dtp = (DeployTopoPlan) plan;
			params.put("Candidate Name", dtp.getCandidateName());
		}

		if (params != null && !params.isEmpty()) {
			result.put("PlanParameters", params);
		}
	}

	/**
	 * Add plan history into result.
	 * 
	 * @param plan
	 *            the plan
	 */
	private void addPlanHistory(Plan plan) {
		ArrayList<Map> history = new ArrayList<Map>();
		// Set execution history
		for (PlanRun pr : plan.getExecutionState().getHistory()) {
			Map h = new HashMap();
			String prStr = pr.toString();
			int startindex = prStr.indexOf('[');
			int endIndex = prStr.indexOf(']');
			h.put("AttemptNumber", pr.getAttemptNumber());
			if (startindex > 0 && endIndex > 0 && endIndex > startindex) {
				h.put("State", prStr.substring(startindex + 1, endIndex));
			}
			h.put("StartTime", new Date(pr.getStartTime()).toString());
			h.put("EndTime", new Date(pr.getEndTime()).toString());
			if (pr.getFailureDescription(true) != null) {
				h.put("FailureDescription", pr.getFailureDescription(true));
			}

			history.add(h);
		}
		result.put("ExecutionHistory", history);
	}

	/**
	 * Get plan and add its details into result.
	 * 
	 * @return the plan
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private Plan getPlan() throws AnkushException {
		if (!parameterMap.containsKey("id")) {
			throw new AnkushException("Plan id is missing.");
		}
		int planId = Integer.parseInt((String) parameterMap.get("id"));

		// Get Plan
		Plan plan = null;
		try {
			plan = OracleNoSQLManager.getAdminClient(this.conf).getPlanById(
					planId);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get plan" + planId + ".");
		}
		result.put("Id", plan.getId());
		result.put("Type", plan.getClass().getSimpleName());
		result.put("Name", plan.getName());
		result.put("State", plan.getState().toString());
		result.put("CreateTime", plan.getCreateTime().toString());
		return plan;
	}

	/**
	 * Get sore events.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void storeevents() throws AnkushException {
		// Set default start and end time
		long startTime = 0;
		long endTime = System.currentTimeMillis();

		// Set start and end time from parameter map
		if (parameterMap.containsKey("starttime")) {
			startTime = Long.parseLong((String) parameterMap.get("starttime"));
		}

		if (parameterMap.containsKey("endtime")) {
			endTime = Long.parseLong((String) parameterMap.get("endtime"));
		}

		ArrayList<ArrayList<String>> array = null;
		// Get Event list
		List<CriticalEvent> list;
		try {
			list = OracleNoSQLManager.getAdminClient(this.conf).getEvents(
					startTime, endTime, EventType.ALL);
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get store events.");
		}
		if (list != null) {
			array = new ArrayList<ArrayList<String>>();
			String event = null;
			int index = 0;
			ArrayList<String> a = null;

			for (CriticalEvent e : list) {
				event = e.toString();
				index = event.indexOf(' ');
				a = new ArrayList<String>();
				if (index > 0) {
					a.add(event.substring(0, index));
					a.add(event.substring(index));
				} else {
					a.add(event);
				}
				array.add(a);
			}
			if (list.size() > 0) {
				endTime = list.get(list.size() - 1).getSyntheticTimestamp();
				endTime++;
			}

			result.put("events", array);
			result.put(LASTTIMESTAMP, endTime);
		}
	}

	/**
	 * Get Replication Factor Change Parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void repfactorparams() throws AnkushException {
		CommandServiceAPI cs = OracleNoSQLManager.getAdminClient(this.conf);
		Topology tp = null;
		Parameters params = null;
		try {
			tp = cs.getTopology();
			params = cs.getParameters();
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get topology.");
		}
		List<StorageNodeId> snIds = tp.getStorageNodeIds();
		int totalNodes = snIds.size();
		int capacityCount = 0;
		int shardsCount = tp.getRepGroupIds().size();
		int repFactor = 0;
		int availableNodes = 0;

		// Calculate Total StorageNode capacity
		for (StorageNodeId snid : snIds) {
			capacityCount += params.get(snid).getCapacity();
		}

		// Fetch Replication Factor
		if (capacityCount > 0) {
			repFactor = tp.getDatacenter(tp.getStorageNodeIds().get(0))
					.getRepFactor();
		}

		// Calculate Available StorageNode capacity
		for (RepGroup rg : tp.getRepGroupMap().getAll()) {
			capacityCount -= rg.getRepNodes().size();
			for (RepNode rn : rg.getRepNodes()) {
				snIds.remove(rn.getStorageNodeId());
			}
		}

		availableNodes = snIds.size();
		if (availableNodes == 0) {
			throw new AnkushException("Could not find available storage node.");
		}

		result.put("totalStorageNodes", totalNodes);
		result.put("availableStorageNodes", availableNodes);
		result.put("availableCapacity", capacityCount);
		result.put("shardCount", shardsCount);
		result.put("repFactor", repFactor);
	}

	/**
	 * Method to get the list of files and directories.
	 * 
	 * @return The Map of files and directories against the ip address.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void logparams() throws AnkushException {

		OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
				.getTechnologyData(this.conf);

		// Admin Node ip list.
		List<String> adminNodes = new ArrayList<String>();

		// Storaget node ip node ip list.
		List<String> storageNodes = new ArrayList<String>();

		// secondary node ip list.
		List<String> repNodes = new ArrayList<String>();

		if (techData != null) {
			for (OracleNoSQLDatacenter dc : techData.getDatacenterList()) {
				for (OracleNoSQLStorageNode sn : dc.getStorageNodeList()) {
					storageNodes.add(sn.getHostname());
					if (sn.getRnCount() > 0) {
						repNodes.add(sn.getHostname());
					}
					if (sn.getAdminPort() > 0) {
						adminNodes.add(sn.getHostname());
					}
				}
			}
		}

		if (!adminNodes.isEmpty()) {
			result.put(Constant.Role.ADMINNODE, adminNodes);
		}
		if (!storageNodes.isEmpty()) {
			result.put(Constant.Role.STORAGENODE, storageNodes);
		}
		if (!repNodes.isEmpty()) {
			result.put(Constant.Role.REPNODE, repNodes);
		}
	}

	/**
	 * Gets the log dir path.
	 * 
	 * @return the log dir path
	 */
	private String getLogDirPath() {
		return new StringBuilder(FileNameUtils.convertToValidPath(conf
				.getDataPath()))
				.append(FileNameUtils.convertToValidPath(conf.getClusterName()))
				.append(FileNameUtils.convertToValidPath("log")).toString();
	}

	/**
	 * Method to get the node type files.
	 * 
	 * @return The Map of files and directories against the ip address.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void logfiles() throws AnkushException {

		if (!parameterMap.containsKey(Constant.Keys.IP)
				|| !parameterMap.containsKey(Constant.Keys.TYPE)) {
			throw new AnkushException("Either ip or type is missing.");
		}
		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);
		String type = (String) parameterMap.get(Constant.Keys.TYPE);

		LogViewHandler logHandler = new LogViewHandler(nodeIp,
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey());

		// puting the files in list.
		try {
			result.put(
					"files",
					filterFile(logHandler.listLogDirectory(getLogDirPath()),
							type));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get log files.");
		}
	}

	/**
	 * Method to view the content of the file.
	 * 
	 * @return The content of file.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void view() throws AnkushException {

		if (!parameterMap.containsKey(Constant.Keys.IP)
				|| !parameterMap.containsKey(Constant.Keys.FILENAME)) {
			throw new AnkushException("Either ip or filename is missing.");
		}

		// Getting the ip address from parameter map.
		String host = (String) parameterMap.get(Constant.Keys.IP);
		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);
		// Getting the readCount value
		int readCount = ParserUtil.getIntValue(
				(String) parameterMap.get(Constant.Keys.READCOUNT), 0);

		int bytesCount = ParserUtil.getIntValue(
				(String) parameterMap.get(Constant.Keys.BYTESCOUNT), 0);

		LogViewHandler logHandler = new LogViewHandler(host,
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey());

		// putting the file content with total read characters.
		try {
			result.putAll(logHandler.getFileContent(getLogDirPath() + fileName,
					readCount, bytesCount));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get file content.");
		}
	}

	/**
	 * Method to get download url of the file.
	 * 
	 * @return Map of the download url.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void downloadfile() throws AnkushException {

		if (!parameterMap.containsKey(Constant.Keys.IP)
				|| !parameterMap.containsKey(Constant.Keys.FILENAME)) {
			throw new AnkushException("Either ip or filename is missing.");
		}

		// Getting the ip address from parameter map.
		String host = (String) parameterMap.get(Constant.Keys.IP);
		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);

		LogViewHandler logHandler = new LogViewHandler(host,
				conf.getUsername(), conf.getPassword(), conf.getPrivateKey());

		// putting the download path.
		try {
			result.put(Constant.Keys.DOWNLOADPATH, logHandler.downloadFile(
					conf.getClusterName(), getLogDirPath() + fileName));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not download file.");
		}
	}

	/**
	 * Filter log files.
	 * 
	 * @param files
	 *            List of files
	 * @param type
	 *            Log type
	 * @return the list
	 */
	private List<String> filterFile(List<String> files, String type) {
		List<String> result = new ArrayList<String>();

		for (String file : files) {
			// Remove files ended with .lck
			if (file.endsWith(".lck")) {
				continue;
			}
			if (type.equals(Constant.Role.ADMINNODE)
					&& file.contains(Constant.Role.Regex.ADMINNODE)) {
				result.add(file);
			} else if (type.equals(Constant.Role.STORAGENODE)
					&& file.contains(Constant.Role.Regex.STORAGENODE)) {
				result.add(file);
			} else if (type.equals(Constant.Role.REPNODE)
					&& file.contains(Constant.Role.Regex.REPNODE)) {
				result.add(file);
			}
		}
		return result;
	}

	/**
	 * Get Migrating Node Parameters.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void migratenodeparams() throws AnkushException {
		CommandServiceAPI cs = OracleNoSQLManager.getAdminClient(this.conf);

		Topology tp;
		try {
			tp = cs.getTopology();
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get topology.");
		}
		List<StorageNodeId> oldSnIds = tp.getStorageNodeIds();
		List<StorageNodeId> newSnIds = tp.getStorageNodeIds();

		// Send error message if Replication factor is less than 3
		if (oldSnIds.size() > 0) {
			int repFactor = tp.getDatacenter(oldSnIds.get(0)).getRepFactor();
			if (repFactor < MIN_REP_FACTOR) {
				throw new AnkushException(
						"Current replication factor is "
								+ repFactor
								+ ". Node migration is possible only if replication factor is 3 or more.");
			}
		}

		for (RepGroup rg : tp.getRepGroupMap().getAll()) {
			for (RepNode rn : rg.getRepNodes()) {
				// Remove existing Storage Node from the list
				newSnIds.remove(rn.getStorageNodeId());
			}
		}

		// Remove new nodes
		oldSnIds.removeAll(newSnIds);

		addOldNodeIds(tp, oldSnIds);

		addNewNodeIds(tp, newSnIds);

	}

	/**
	 * Create old node list which is used by node migration.
	 * 
	 * @param tp
	 *            Store topology
	 * @param oldSnIds
	 *            List of old storage node ids
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void addOldNodeIds(Topology tp, List<StorageNodeId> oldSnIds)
			throws AnkushException {
		Map<Integer, String> oldNodes = new HashMap<Integer, String>();

		// Create list of old Nodes
		for (StorageNodeId snId : oldSnIds) {
			oldNodes.put(snId.getStorageNodeId(), snId.getComponent(tp)
					.getHostname());
		}

		if (oldNodes.isEmpty()) {
			throw new AnkushException("Could not find old storage node.");
		}

		result.put("oldStorageNodes", oldNodes);
	}

	/**
	 * Create new node list used by node migration.
	 * 
	 * @param tp
	 *            Store topology
	 * @param newSnIds
	 *            List of new storage node ids.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void addNewNodeIds(Topology tp, List<StorageNodeId> newSnIds)
			throws AnkushException {
		// Get topology data
		OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
				.getTechnologyData(this.conf);

		Map<Integer, String> newNodes = new HashMap<Integer, String>();

		if (techData != null) {
			Map<Integer, Boolean> statusMap = techData.getStorageNodesStatus();

			// Create list of new Nodes
			for (StorageNodeId snId : newSnIds) {
				if (statusMap.get(snId.getStorageNodeId())) {
					// Add New Node only if its running
					newNodes.put(snId.getStorageNodeId(), snId.getComponent(tp)
							.getHostname());
				}
			}
		}

		if (newNodes.isEmpty()) {
			throw new AnkushException("Could not find available storage node.");
		}
		result.put("newStorageNodes", newNodes);
	}

	/**
	 * Get last log sequence.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void lastlogtimestamp() throws AnkushException {
		try {
			result.put(LASTTIMESTAMP,
					OracleNoSQLManager.getAdminClient(this.conf).getLogSince(0)
							.getLastSyntheticTimestamp());
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get store logs.");
		}
	}

	/**
	 * Comparator for log records.
	 * 
	 */
	class LogComparator implements Comparator<LogRecord> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(LogRecord o1, LogRecord o2) {
			if (o1.getMillis() > o2.getMillis()) {
				return 1;
			} else if (o1.getMillis() < o2.getMillis()) {
				return -1;
			} else {
				// equal
				return 0;
			}
		}
	}

	/**
	 * Format log records.
	 * 
	 * @param lr
	 *            the lr
	 * @return the string
	 */
	private String formatLogRecord(LogRecord lr) {
		String format = lr.getMessage();
		Object lrparams[] = lr.getParameters();
		String formattedMessage = format;

		// Format log message
		if (lrparams != null && lrparams.length != 0) {
			try {
				formattedMessage = MessageFormat.format(format, lrparams);
			} catch (Exception e) {
			}
		}

		return new Date(lr.getMillis()).toString() + " "
				+ lr.getLevel().toString() + " " + formattedMessage;
	}

	/**
	 * Convert RepNode Id to String.
	 * 
	 * @param rnids
	 *            the rnids
	 * @return the string
	 */
	private String repNodeIdsToString(Set<RepNodeId> rnids) {
		StringBuilder sb = new StringBuilder("[");
		for (RepNodeId rnid : rnids) {
			if (sb.length() > 1) {
				sb.append(",");
			}
			sb.append(rnid.toString());
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Database level function start.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */

	/**
	 * Get cluster configuration key value pairs.
	 * 
	 * @throws AnkushException
	 */
	private void details() throws AnkushException {
		storeconfiguration();
		nodelist();
	}

	/**
	 * Get last log message for each node.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the db node messages
	 */
	public Map<String, String> getDbNodeMessages(Long clusterId) {
		Map<String, String> nodeMessages = null;
		try {
			long operationId = logger.getNewOperationId(clusterId) - 1;
			List<Log> logs = logManager.getAllByNativeQuery(Log
					.getNodeLastLogQuery(clusterId, operationId));

			nodeMessages = new HashMap<String, String>();
			for (Log log : logs) {
				nodeMessages.put(log.getHost(), log.getMessage());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
		return nodeMessages;
	}

	/**
	 * Get Store Configuration.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void storeconfiguration() throws AnkushException {
		try {

			// setting errors.
			Map<String, String> errors = this.conf.getClusterConf().getErrors();
			for (NodeConf nodeConf : this.conf.getNodes()) {
				if (nodeConf.getErrors().size() > 0) {
					errors.put(
							nodeConf.getPublicIp(),
							"Deployment failed on node : "
									+ nodeConf.getPublicIp());
				}
			}
			this.conf.getClusterConf().setErrors(errors);
			// putting cluster setup details in map.

			result.putAll(JsonMapperUtil.mapFromObject(this.conf
					.getClusterConf()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new AnkushException("Could not get cluster detail.");
		}
	}

	/**
	 * Set installation message for nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void nodelist() throws AnkushException {
		addNodes(new ArrayList<NodeConf>(this.conf.getNodes()));
		result.put(Constant.Keys.STATE, this.conf.getClusterConf().getState());
	}

	/**
	 * Get Add node details.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void addnodes() throws AnkushException {

		List<NodeConf> nodes = this.conf.getNewNodes();

		if (nodes == null || nodes.isEmpty()) {
			return;
		}

		// getting the event state.

		String state = this.conf.getClusterConf().getState();
		for (NodeConf node : nodes) {
			if (node.getNodeState().equals(Constant.Keys.ERROR)) {
				state = node.getNodeState();
				break;
			}
		}

		addNodes(nodes);

		// putting event state in map.
		result.put(Constant.Keys.STATE, state);

		if (!state.equals(Constant.Cluster.State.ADDING_NODES)) {
			// Remove new nodes
			this.conf.setNewNodes(null);
			// Remove new nodes from Oracle cluster conf
			if (this.clusterConfig instanceof OracleNoSQLClusterConf) {
				((OracleNoSQLClusterConf) this.clusterConfig)
						.setOracleNoSQLConf(this.conf);
			}
			dbCluster.setClusterConf(this.clusterConfig);
			try {
				clusterManager.save(dbCluster);
			} catch (Exception e) {
				logger.error("Could not save cluster object.");
			}
		}

	}

	/**
	 * Set node configuration.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void addNodes(List<NodeConf> nodeConfs) throws AnkushException {

		// Get last message of each nodes
		Map<String, String> nodeMessages = getDbNodeMessages(dbCluster.getId());

		List<Map> nodeList = new ArrayList<Map>();

		for (NodeConf nodeConf : nodeConfs) {
			LinkedHashMap node = null;
			try {
				if (dbCluster.getState()
						.equals(Constant.Cluster.State.DEPLOYED)
						&& nodeConf.getStatus()) {
					nodeConf.setMessage("Deployment Completed.");
				} else {
					if (nodeMessages != null
							&& nodeMessages.get(nodeConf.getPrivateIp()) != null) {
						nodeConf.setMessage(nodeMessages.get(nodeConf
								.getPublicIp()));
					}
				}
				node = JsonMapperUtil.objectFromObject(nodeConf,
						LinkedHashMap.class);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new AnkushException("Could not get node configuration.");
			}
			// Remove extra values
			node.remove("snId");

			nodeList.add(node);
		}

		result.put(Constant.Keys.NODES, nodeList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.AbstractMonitor#monitor(com.impetus
	 * .ankush.common.domain.Cluster, java.lang.String, java.util.Map)
	 */
	@Override
	public Map monitor(Cluster cluster, String action, Map parameterMap) {
		this.dbCluster = cluster;
		try {

			// Setting the cluster name to logger.
			this.clusterConfig = dbCluster.getClusterConf();
			this.conf = (OracleNoSQLConf) dbCluster.getClusterConf()
					.getClusterComponents()
					.get(Constant.Technology.ORACLE_NOSQL);
			this.parameterMap = parameterMap;
			return super.monitor(dbCluster, action, parameterMap);
		} catch (SecurityException e) {
			// Adding error
			addError(e.getMessage());
			// Logging error
			logger.error(e.getMessage());
		} catch (Exception e) {
			// Adding error
			if (e.getMessage() != null) {
				addError(e.getMessage());
			} else {
				addError(e.getCause().getMessage());
			}
			// Logging error
			logger.error(e.getMessage());
		}
		return returnResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.framework.AbstractMonitor#categories()
	 */
	@Override
	public Map categories() throws Exception {
		Map categoryMap = (Map) super.categories().get("categories");
		String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

		OracleNoSQLNodeConf nodeConf = null;

		Map<String, String> categories = new HashMap<String, String>();

		// Add admin node
		for (OracleNoSQLNodeConf nc : this.conf.getNodes()) {
			if (nodeIp.equals(nc.getPublicIp())) {
				nodeConf = nc;
				break;
			}
		}

		if (nodeConf == null) {
			throw new AnkushException("Could not get nodeConf.");
		}

		// Add replication nodes
		try {
			for (String service : new MonitoringManager()
					.getMonitoringData(nodeConf.getPublicIp())
					.getServiceStatus().keySet()) {
				// Check repnode service
				if (service.startsWith("rg")) {
					categories.put(service, ".*" + service + ".*\\.rrd");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		categoryMap.putAll(getCategoryMap(categories, nodeConf));

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
			OracleNoSQLNodeConf nodeConf) {
		Map<String, Object> categoryMap = new HashMap<String, Object>();
		try {
			// creating node graph object.
			if (graph == null) {
				graph = new Graph(this.clusterConfig.getGangliaMaster()
						.getPublicIp(), this.clusterConfig.getUsername(),
						this.clusterConfig.getPassword(),
						this.clusterConfig.getPrivateKey(),
						this.clusterConfig.getClusterName());
			}

			List<String> files = graph.getAllFiles(nodeConf.getPrivateIp());

			for (String category : categories.keySet()) {
				Map map = new HashMap();
				List<String> matchingFiles = graph.getMatchingFiles(
						categories.get(category), files);

				map.put("count", matchingFiles.size());
				map.put("legends", matchingFiles);
				map.put("pattern", categories.get(category));
				map.put(Constant.Keys.IP, nodeConf.getPrivateIp());
				categoryMap.put(category, map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return categoryMap;
	}
}
