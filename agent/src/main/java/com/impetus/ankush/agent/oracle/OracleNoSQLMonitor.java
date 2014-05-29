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
package com.impetus.ankush.agent.oracle;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.LogRecord;

import oracle.kv.impl.admin.CommandServiceAPI;
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
import oracle.kv.impl.topo.Datacenter;
import oracle.kv.impl.topo.RepGroup;
import oracle.kv.impl.topo.RepNode;
import oracle.kv.impl.topo.RepNodeId;
import oracle.kv.impl.topo.ResourceId;
import oracle.kv.impl.topo.StorageNodeId;
import oracle.kv.impl.topo.Topology;
import oracle.kv.impl.util.ConfigurableService.ServiceStatus;
import oracle.kv.impl.util.registry.RegistryUtils;
import oracle.kv.util.Ping;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.codehaus.jackson.map.ObjectMapper;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.AnkushException;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.utils.ClassPathHack;
import com.impetus.ankush.agent.utils.CommandExecutor;
import com.impetus.ankush.agent.utils.Result;

/**
 * The Class OracleNoSQLMonitor.
 */
public class OracleNoSQLMonitor {

	/** The Constant LASTTIMESTAMP. */
	private static final String LASTTIMESTAMP = "lasttimestamp";

	/** The cs. */
	private CommandServiceAPI cs;
	
	/** The command. */
	private CommandLine command;
	
	/** The result. */
	private Map result;

	/**
	 * Instantiates a new oracle no sql monitor.
	 *
	 * @param args the args
	 * @throws ParseException the parse exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws NotBoundException the not bound exception
	 */
	public OracleNoSQLMonitor(String[] args) throws ParseException,
			IOException, NotBoundException {

		AgentConf conf = new AgentConf();
		String host = conf.getStringValue(Constant.PROP_HOSTNAME);
		Integer port = conf.getIntValue(Constant.PROP_REGISTRY_PORT);

		// Parse command line arguments
		command = parseCommandLineArgs(args);
		result = new HashMap();

		// Load jar files
		for (String jar : conf.getJarPath()) {
			ClassPathHack.addFile(jar);
		}

		/* Get Administrative client using cluster admin node. */
		cs = RegistryUtils.getAdmin(host, port);

	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public Map getResult() {
		return result;
	}

	/**
	 * Gets the admin command.
	 *
	 * @return the admin command
	 */
	private String getAdminCommand() {
		// Create agent conf
		AgentConf conf = new AgentConf();
		// Get variables
		String host = conf.getStringValue(Constant.PROP_HOSTNAME);
		Integer port = conf.getIntValue(Constant.PROP_REGISTRY_PORT);
		String kvJar = conf.getStringValue(Constant.KV_JAR_PATH);
		return new StringBuilder().append("java -jar ").append(kvJar)
				.append(" runadmin -host ").append(host).append(" -port ")
				.append(port).toString();
	}

	/**
	 * Parses the command line args.
	 *
	 * @param args the args
	 * @return the command line
	 * @throws ParseException the parse exception
	 */
	private CommandLine parseCommandLineArgs(String[] args)
			throws ParseException {
		Options options = new Options();
		options.addOption("all", false, "All parameters");
		options.addOption("id", true, "Service id");
		options.addOption("rnid", true, "Replication node id");
		options.addOption("rgid", true, "Replication group id");
		options.addOption("lasttimestamp", true, "Last time stamp");
		options.addOption("starttime", true, "Start time stamp");
		options.addOption("endtime", true, "End time stamp");

		CommandLineParser parser = new BasicParser();
		return parser.parse(options, args);
	}

	/**
	 * Verify.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void verify() throws AnkushException {
		try {
			Result cmdResult = CommandExecutor.executeCommand(getAdminCommand()
					+ " verify");
			if (cmdResult.getExitVal() != 0) {
				throw new AnkushException("Could not get verify result.");
			}
			result.put("verify", cmdResult.getOutput());
		} catch (IOException e1) {
			throw new AnkushException("Could not get verify result.");
		} catch (InterruptedException e1) {
			throw new AnkushException("Could not get verify result.");
		}
	}

	/**
	 * Planhistory.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void planhistory() throws AnkushException {
		List planHistory = new ArrayList();

		try {
			for (Plan plan : cs.getPlans().values()) {
				// Set Plan parameters
				Map p = new HashMap();
				p.put("id", plan.getId());

				p.put("plan", plan.getName());
				p.put("type", plan.getClass().getSimpleName());
				p.put("state", plan.getState().toString());
				p.put("attempts", plan.getExecutionState().getHistory().size());
				p.put("startTime", plan.getStartTime() == null ? "" : plan
						.getStartTime().toString());
				p.put("endTime", plan.getEndTime() == null ? "" : plan
						.getEndTime().toString());

				planHistory.add(p);
			}
			result.put("planHistory", planHistory);
		} catch (Exception e) {
			throw new AnkushException("Could not get plans.");
		}
	}

	/**
	 * Plan.
	 *
	 * @throws AnkushException the ankush exception
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
	 * Gets the plan.
	 *
	 * @return the plan
	 * @throws AnkushException the ankush exception
	 */
	private Plan getPlan() throws AnkushException {
		if (!command.hasOption("id")) {
			throw new AnkushException("Plan id is missing.");
		}
		int planId = Integer.parseInt(command.getOptionValue("id"));

		// Get Plan
		Plan plan = null;
		try {
			plan = cs.getPlanById(planId);
		} catch (RemoteException e) {
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
	 * Adds the plan history.
	 *
	 * @param plan the plan
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
	 * Rep node ids to string.
	 *
	 * @param rnids the rnids
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
	 * Sets the parameters.
	 *
	 * @param keys the keys
	 * @param params the params
	 */
	private void setParameters(List<String> keys, ParameterMap params) {
		if (params != null) {
			if (keys == null) {
				keys = new ArrayList<String>(params.keys());
			}
			for (String k : keys) {
				result.put(k, params.get(k).toString() != null ? params.get(k)
						.toString() : "");
			}
		}
	}

	/**
	 * Adminparams.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void adminparams() throws AnkushException {
		ParameterMap params = null;
		List<String> keys = null;

		if (!command.hasOption("all")) {
			// List of Admin Params
			keys = Constant.OracleNoSQL.Admin.EDITABLE_PARAMETERS;
		}
		try {
			// Fetch admin params using adminId
			if (command.hasOption("id")) {
				int adminId = Integer.parseInt(command.getOptionValue("id"));
				params = cs.getParameters().get(new AdminId(adminId)).getMap();
			} else {
				params = cs.getAdmins().get(0);
			}
		} catch (RemoteException e) {
			throw new AnkushException("Could not get admin parameters.");
		}
		setParameters(keys, params);
	}

	/**
	 * Policyparams.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void policyparams() throws AnkushException {
		List<String> keys = null;
		if (!command.hasOption("all")) {
			// List of policy parameters
			keys = Constant.OracleNoSQL.Policy.EDITABLE_PARAMETERS;
		}

		ParameterMap params = null;
		try {
			params = cs.getPolicyParameters();
		} catch (RemoteException e) {
			throw new AnkushException("Could not get policy parameters.");
		}
		setParameters(keys, params);
	}

	/**
	 * Repnodeparams.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void repnodeparams() throws AnkushException {
		if (!command.hasOption("rgid")) {
			throw new AnkushException("rgid is missing");
		}
		if (!command.hasOption("rnid")) {
			throw new AnkushException("rnid is missing");
		}

		int groupId = Integer.parseInt(command.getOptionValue("rgid"));
		int nodeNum = Integer.parseInt(command.getOptionValue("rnid"));
		List<String> keys = null;
		if (!command.hasOption("all")) {
			// List of Replication Node params
			keys = Constant.OracleNoSQL.RepNode.EDITABLE_PARAMETERS;
		}

		RepNodeParams rnp = null;
		try {
			rnp = cs.getParameters().get(new RepNodeId(groupId, nodeNum));
		} catch (RemoteException e) {
			throw new AnkushException(
					"Could not get replication parameters for RG" + groupId
							+ "-RN" + nodeNum + ".");
		}

		setParameters(keys, rnp.getMap());
	}

	/**
	 * Storagenodeparams.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void storagenodeparams() throws AnkushException {
		if (!command.hasOption("id")) {
			throw new AnkushException("Id is missing.");
		}

		int snId = Integer.parseInt(command.getOptionValue("id"));
		List<String> keys = null;
		if (!command.hasOption("all")) {
			// List of Storage Node params
			keys = Constant.OracleNoSQL.StorageNode.EDITABLE_PARAMETERS;
		}

		StorageNodeParams snp;
		try {
			snp = cs.getParameters().get(new StorageNodeId(snId));
		} catch (RemoteException e) {
			throw new AnkushException(
					"Could not get storage node parameters for SN" + snId + ".");
		}
		setParameters(keys, snp.getMap());
	}

	/**
	 * Repfactorparams.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void repfactorparams() throws AnkushException {
		Topology tp = null;
		Parameters params = null;
		try {
			tp = cs.getTopology();
			params = cs.getParameters();
		} catch (RemoteException e) {
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
			repFactor = getRepFactor();
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
	 * Migratenodeparams.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void migratenodeparams() throws AnkushException {
		Topology tp;
		try {
			tp = cs.getTopology();
		} catch (RemoteException e) {
			throw new AnkushException("Could not get topology.");
		}

		List<StorageNodeId> oldSnIds = tp.getStorageNodeIds();
		List<StorageNodeId> newSnIds = tp.getStorageNodeIds();

		for (RepGroup rg : tp.getRepGroupMap().getAll()) {
			for (RepNode rn : rg.getRepNodes()) {
				// Remove existing Storage Node from the list
				newSnIds.remove(rn.getStorageNodeId());
			}
		}

		// Remove new nodes
		oldSnIds.removeAll(newSnIds);

		result.put("oldStorageNodes", addOldNodeIds(tp, oldSnIds));
		result.put("newStorageNodes", addNewNodeIds(tp, newSnIds));
		result.put("repFactor", getRepFactor());
	}

	/**
	 * Adds the old node ids.
	 *
	 * @param tp the tp
	 * @param oldSnIds the old sn ids
	 * @return the map
	 * @throws AnkushException the ankush exception
	 */
	private Map<Integer, String> addOldNodeIds(Topology tp,
			List<StorageNodeId> oldSnIds) throws AnkushException {
		Map<Integer, String> oldNodes = new HashMap<Integer, String>();

		// Create list of old Nodes
		for (StorageNodeId snId : oldSnIds) {
			oldNodes.put(snId.getStorageNodeId(), snId.getComponent(tp)
					.getHostname());
		}

		if (oldNodes.isEmpty()) {
			throw new AnkushException("Could not find old storage node.");
		}

		return oldNodes;
	}

	/**
	 * Adds the new node ids.
	 *
	 * @param tp the tp
	 * @param newSnIds the new sn ids
	 * @return the map
	 * @throws AnkushException the ankush exception
	 */
	private Map<Integer, String> addNewNodeIds(Topology tp,
			List<StorageNodeId> newSnIds) throws AnkushException {
		Map<Integer, String> newNodes = new HashMap<Integer, String>();
		// Create list of new Nodes
		for (StorageNodeId snId : newSnIds) {
			// Get service status
			Map<ResourceId, ServiceStatus> statusMap = Ping
					.getTopologyStatus(tp);
			if (statusMap != null) {
				ServiceStatus se = statusMap.get(snId);
				if (se != null && se.isAlive()) {
					// Add New Node only if its running
					newNodes.put(snId.getStorageNodeId(), snId.getComponent(tp)
							.getHostname());
				}
			}
		}

		if (newNodes.isEmpty()) {
			throw new AnkushException("Could not find available storage node.");
		}
		return newNodes;
	}

	/**
	 * Repfactor.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void repfactor() throws AnkushException {
		result.put("repFactor", getRepFactor());
	}

	/**
	 * Gets the rep factor.
	 *
	 * @return the rep factor
	 * @throws AnkushException the ankush exception
	 */
	private int getRepFactor() throws AnkushException {
		int repFactor = 0;
		try {
			// Add replication factor of all datacenters
			for (Datacenter dc : cs.getTopology().getDatacenterMap().getAll()) {
				repFactor += dc.getRepFactor();
			}
		} catch (RemoteException e) {
			throw new AnkushException("Could not get topology.");
		}
		return repFactor;
	}

	/**
	 * Lastlogtimestamp.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void lastlogtimestamp() throws AnkushException {
		try {
			result.put(LASTTIMESTAMP, cs.getLogSince(0)
					.getLastSyntheticTimestamp());
		} catch (RemoteException e) {
			throw new AnkushException("Could not get store logs.");
		}
	}

	/**
	 * Storeevents.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void storeevents() throws AnkushException {
		// Set default start and end time
		long startTime = 0;
		long endTime = System.currentTimeMillis();

		// Set start and end time from parameter map
		if (command.hasOption("starttime")) {
			startTime = Long.parseLong(command.getOptionValue("starttime"));
		}

		if (command.hasOption("endtime")) {
			endTime = Long.parseLong(command.getOptionValue("endtime"));
		}

		ArrayList<ArrayList<String>> array = null;
		// Get Event list
		List<CriticalEvent> list;
		try {
			list = cs.getEvents(startTime, endTime, EventType.ALL);
		} catch (RemoteException e) {
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
	 * Storelogs.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void storelogs() throws AnkushException {
		// Set default start and end time
		long startTime = 0;

		// Set start time
		if (command.hasOption(LASTTIMESTAMP)) {
			startTime = Long.parseLong(command.getOptionValue(LASTTIMESTAMP)) + 1;
		}

		ArrayList<ArrayList<String>> array = null;
		// Get logs
		RetrievedEvents<LogRecord> events = null;
		try {
			events = cs.getLogSince(startTime);
		} catch (RemoteException e) {
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

			result.put("logs", array);
			result.put(LASTTIMESTAMP, events.getLastSyntheticTimestamp());
		}
	}

	/**
	 * Format log record.
	 *
	 * @param lr the lr
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
	 * Availablestoragenodes.
	 *
	 * @throws AnkushException the ankush exception
	 */
	private void availablestoragenodes() throws AnkushException {
		Topology tp = null;

		try {
			// Get Topology
			tp = cs.getTopology();

		} catch (RemoteException e) {
			throw new AnkushException("Could not get topology.");
		}
		// Get total node
		List<StorageNodeId> snIds = tp.getStorageNodeIds();
		for (RepGroup rg : tp.getRepGroupMap().getAll()) {
			for (RepNode rn : rg.getRepNodes()) {
				/* Remove existing Storage Node from the list */
				snIds.remove(rn.getStorageNodeId());
			}
		}

		result.put("availableStorageNodes", snIds.size());
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
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Map output = new HashMap();
		boolean status = true;
		OracleNoSQLMonitor monitor = null;

		try {
			// Create monitor object
			monitor = new OracleNoSQLMonitor(args);

			Method method = null;
			try {
				// Create method object using the action name.
				method = monitor.getClass().getDeclaredMethod(
						monitor.command.getArgs()[0]);
			} catch (NoSuchMethodException e) {
				throw new AnkushException("Invalid operation.");
			} catch (Exception e) {
				throw e;
			}
			// setting accessibility true.
			method.setAccessible(true);

			// invoking the method.
			method.invoke(monitor);

			output.put("result", monitor.getResult());
		} catch (Exception e) {
			// Adding error
			if (e.getMessage() != null) {
				output.put("error", e.getMessage());
			} else {
				output.put("error", e.getCause().getMessage());
			}
			status = false;
		}
		// Set output data
		output.put("status", status);

		// convert into Json string format
		try {
			System.out.println(new ObjectMapper().writeValueAsString(output));
		} catch (Exception e) {
			status = false;
		}

		// Exit with exit code
		if (!status) {
			System.exit(1);
		}
	}
}
