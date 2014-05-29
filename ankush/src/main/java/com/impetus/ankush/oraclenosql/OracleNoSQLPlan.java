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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHConnection;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The class is responsible for creating and executing plans.
 * 
 * The code is compatible with Oracle NoSQL DB R2 (2.0.26).
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLPlan {

	/** The Constant PLAN_SUCCESS. */
	private static final String PLAN_SUCCESS = " -wait | grep 'ended successfully'";

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(OracleNoSQLPlan.class);

	/** The connection. */
	private SSHExec connection = null;

	/** The cluster conf. */
	private OracleNoSQLConf conf = null;

	/** The admin command. */
	private String adminCommand = null;

	/** The admin public ip. */
	private String adminPublicIp = null;

	/**
	 * Instantiates a new oracle no sql plan.
	 * 
	 * @param conf
	 *            the conf
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public OracleNoSQLPlan(OracleNoSQLConf conf) throws AnkushException {
		this.conf = conf;

		OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
				.getTechnologyData(this.conf);

		logger.setCluster(this.conf.getClusterConf());
		if (techData == null || techData.getDatacenterList().isEmpty()) {
			throw new AnkushException("Could not get active admin node.");
		}

		Map<String, String> addresses = new HashMap<String, String>();
		for (OracleNoSQLNodeConf nodeConf : this.conf.getNodes()) {
			addresses.put(nodeConf.getPrivateIp(), nodeConf.getPublicIp());
		}

		for (OracleNoSQLDatacenter dc : techData.getDatacenterList()) {
			for (OracleNoSQLStorageNode sn : dc.getStorageNodeList()) {
				if (sn.getActive() == OracleNoSQLTechnologyData.STATE_RUNNING
						&& sn.getAdminPort() > 0) {

					// create remote connection
					connection = SSHUtils.connectToNode(
							addresses.get(sn.getHostname()),
							this.conf.getUsername(), this.conf.getPassword(),
							this.conf.getPrivateKey());

					StringBuilder command = new StringBuilder()
							.append("java -jar ")
							.append(conf.getKvStoreJarPath())
							.append(" ping -host ").append(sn.getHostname())
							.append(" -port ").append(sn.getRegistryPort())
							.append(" 2>&1 | grep 'Pinging component'");

					if (!executeCommand(command.toString())) {
						connection.disconnect();
						connection = null;
						continue;
					}

					// set admin command
					adminCommand = new StringBuilder().append("java -jar ")
							.append(this.conf.getKvStoreJarPath())
							.append(" runadmin -host ")
							.append(sn.getHostname()).append(" -port ")
							.append(sn.getRegistryPort()).toString();
					// Set admin public ip
					this.adminPublicIp = addresses.get(sn.getHostname());
					break;
				}
			}
		}
		if (connection == null) {
			throw new AnkushException("Could not get active admin node.");
		}
	}

	/**
	 * @return the adminPublicIp
	 */
	public String getAdminPublicIp() {
		return adminPublicIp;
	}

	/**
	 * Destructor.
	 * 
	 * @throws Throwable
	 *             the throwable
	 */
	@Override
	protected void finalize() throws Throwable {
		if (connection != null) {
			connection.disconnect();
		}
		super.finalize();
	}

	/**
	 * Check if parameter value is null or not.
	 * 
	 * @param value
	 *            Parameter value
	 * @return true, if is parameter value empty
	 */
	private boolean isParameterValueEmpty(String value) {
		return (value == null || value.equals("null") || value.equals(""));
	}

	/**
	 * Execute command using remote connection.
	 * 
	 * @param command
	 *            Command which should be executed
	 * @return true, if successful
	 */
	private boolean executeCommand(String command) {
		if (connection == null) {
			return false;
		}
		Result result = null;
		try {
			// Execute command
			result = connection.exec(new ExecCommand(command));
		} catch (TaskExecFailException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		// Validate result
		if (result == null) {
			return false;
		}
		if (result.rc != 0) {
			if (result.sysout != null && !result.sysout.isEmpty()) {
				logger.error(result.sysout);
			}
			if (result.error_msg != null && !result.error_msg.isEmpty()) {
				logger.error(result.error_msg);
			}
			return false;
		}
		return true;
	}

	/**
	 * Change Storage Node parameters.
	 * 
	 * @param snId
	 *            Storage Node Id
	 * @param params
	 *            Parameter Map
	 * @throws AnkushException
	 *             the ankush exception
	 */

	public void changeStorageNodeParams(int snId, Map<String, String> params)
			throws AnkushException {
		logger.info("Changing StorageNode parameter.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan change-parameters -service ").append(snId)
				.append(" -wait -params");

		// Validate and set Storage Node parameter.
		for (String key : params.keySet()) {
			if (!isParameterValueEmpty(params.get(key))) {
				command.append(" ").append(key).append("=\"")
						.append(params.get(key)).append("\"");
			}
		}
		command.append(" | grep ' ended successfully'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException(
					"Could not change storage node parameters.");
		}

	}

	/**
	 * Change All Admin Parameters.
	 * 
	 * @param params
	 *            Parameter Map
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void changeAllAdminParams(Map<String, String> params)
			throws AnkushException {
		logger.info("Changing All Admin parameters.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan change-parameters -all-admins -wait -params");

		// Validate and set Storage Node parameter.
		for (String key : params.keySet()) {
			if (!isParameterValueEmpty(params.get(key))) {
				command.append(" ").append(key).append("=\"")
						.append(params.get(key)).append("\"");
			}
		}
		command.append(" | grep ' ended successfully'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not change all admin parameters.");
		}
	}

	/**
	 * Change all Replication Nodes parameters.
	 * 
	 * @param params
	 *            Parameter Map.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void changeAllRepNodeParams(Map<String, String> params)
			throws AnkushException {
		logger.info("Changing All RepNode parameters.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan change-parameters -all-rns -wait -params");

		// Validate and set Storage Node parameter.
		for (String key : params.keySet()) {
			if (!isParameterValueEmpty(params.get(key))) {
				command.append(" ").append(key).append("=\"")
						.append(params.get(key)).append("\"");
			}
		}
		command.append(" | grep ' ended successfully'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException(
					"Could not change all replication node parameters.");
		}
	}

	/**
	 * Change Replication Node parameters.
	 * 
	 * @param groupId
	 *            Replication group Id.
	 * @param nodeNum
	 *            Replication Node number.
	 * @param params
	 *            Parameter map.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void changeRepNodeParams(int groupId, int nodeNum,
			Map<String, String> params) throws AnkushException {
		logger.info("Changing RepNode parameters.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan change-parameters -service rg").append(groupId)
				.append("-rn").append(nodeNum).append(" -wait -params");

		// Validate and set Storage Node parameter.
		for (String key : params.keySet()) {
			if (!isParameterValueEmpty(params.get(key))) {
				command.append(" ").append(key).append("=\"")
						.append(params.get(key)).append("\"");
			}
		}
		command.append(" | grep ' ended successfully'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException(
					"Could not change replication node parameters.");
		}

	}

	/**
	 * Change Policy Parameters.
	 * 
	 * @param params
	 *            Parameter map.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void changePolicyParams(Map<String, String> params)
			throws AnkushException {
		logger.info("Changing Policy paramters.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" change-policy -params");

		// Validate and set Storage Node parameter.
		for (String key : params.keySet()) {
			if (!isParameterValueEmpty(params.get(key))) {
				command.append(" ").append(key).append("=\"")
						.append(params.get(key)).append("\"");
			}
		}
		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not change policy parameters.");
		}
	}

	/**
	 * Start all Replication Nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void startAllRepNode() throws AnkushException {
		logger.info("Starting All RepNodes.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan start-service -all-rns -wait").append(" | ")
				.append("grep ' ended successfully'");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not start all replication nodes.");
		}
	}

	/**
	 * Start Replication Nodes.
	 * 
	 * @param list
	 *            List of replication Nodes.
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void startRepNode(List<List<String>> list) throws AnkushException {
		logger.info("Starting RepNode(s).");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan start-service ");

		for (List<String> l : list) {
			command.append(" -service rg").append(l.get(0)).append("-rn")
					.append(l.get(1));
		}
		command.append(" -wait | grep ' ended successfully'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not start replication nodes.");
		}
	}

	/**
	 * Stop All Replication Nodes.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void stopAllRepNode() throws AnkushException {
		logger.info("Stopping All RepNodes.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan stop-service -all-rns -wait").append(" | ")
				.append("grep ' ended successfully'");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not stop all replication nodes.");
		}
	}

	/**
	 * Stop Replication Nodes.
	 * 
	 * @param list
	 *            List of replication nodes
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void stopRepNode(List<List<String>> list) throws AnkushException {
		logger.info("Stopping RepNode(s).");
		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan stop-service ");

		for (List<String> l : list) {
			command.append(" -service rg").append(l.get(0)).append("-rn")
					.append(l.get(1));
		}
		command.append(" -wait | grep ' ended successfully'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not stop replication nodes.");
		}
	}

	/**
	 * Migrate old Storage Node with new Storage Node.
	 * 
	 * @param oldSn
	 *            the old sn
	 * @param newSn
	 *            the new sn
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void migrateNode(OracleNoSQLNodeConf oldSn,
			OracleNoSQLStorageNode newSn) throws AnkushException {
		StringBuilder command = null;
		logger.info("Migrating StorageNode.");

		// Migrate storage node
		command = new StringBuilder(adminCommand)
				.append(" plan migrate-sn -from sn").append(oldSn.getSnId())
				.append(" -to sn").append(newSn.getSnId());

		if (oldSn.isAdmin() && newSn.getAdminPort() > 0) {
			command.append(" -admin-port ").append(newSn.getAdminPort());
		}
		command.append(PLAN_SUCCESS);

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not migrate storage node.");
		}

		// Remove storage node
		command = new StringBuilder(adminCommand)
				.append(" plan remove-sn -sn sn").append(oldSn.getSnId())
				.append(PLAN_SUCCESS);
		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not remove storage node.");
		}
	}

	/**
	 * Removes the admin.
	 * 
	 * @param storageNode
	 *            the storage node
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void removeAdmin(OracleNoSQLStorageNode storageNode)
			throws AnkushException {
		StringBuilder command = null;
		logger.info("Remove admin service.");

		// Remove admin service
		if (storageNode.getAdminPort() > 0 && storageNode.getAdminId() > 0) {
			command = new StringBuilder(adminCommand)
					.append(" plan remove-admin -admin ")
					.append(storageNode.getAdminId()).append(PLAN_SUCCESS);
			if (!executeCommand(command.toString())) {
				throw new AnkushException("Could not remove admin service.");
			}
		}
	}

	/**
	 * Clone current topology.
	 * 
	 * @return the string
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private String cloneTopology() throws AnkushException {
		String topologyName = null;
		StringBuilder command = null;

		// Get new topology name
		SSHConnection conn = new SSHConnection(this.adminPublicIp,
				this.conf.getUsername(), this.conf.getPassword(),
				this.conf.getPrivateKey());
		if (conn != null && conn.isConnected()) {
			command = new StringBuilder(adminCommand)
					.append(" topology list | wc -l");
			conn.exec(command.toString());
			if (conn.getExitStatus() == 0 && conn.getOutput() != null) {
				topologyName = this.conf.getTopologyName()
						+ Integer.parseInt(conn.getOutput());
			}
		}

		if (topologyName == null) {
			throw new AnkushException("Could not get topology name.");
		}

		// Create command
		command = new StringBuilder(adminCommand)
				.append(" topology  clone -current -name \"")
				.append(topologyName).append("\"");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not clone topology.");
		}
		return topologyName;
	}

	/**
	 * Deploy topology.
	 * 
	 * @param topologyName
	 *            the topology name
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void deployTopology(String topologyName) throws AnkushException {
		// Deploy Topology
		logger.info("Deploying topology.");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan deploy-topology -name \"").append(topologyName)
				.append("\" -wait | grep 'ended successfully'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not deploy topology.");
		}
	}

	/**
	 * Redistribute Topology.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void redistribute() throws AnkushException {
		logger.info("Redistributing Topology.");
		String topologyName = cloneTopology();

		// Fetch StorageNode pool name.
		String snPoolName = AppStoreWrapper.getAnkushConfReader()
				.getStringValue("oracle.nosql.snpoolname");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" topology redistribute -name \"").append(topologyName)
				.append("\" -pool \"").append(snPoolName).append("\"");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not redistribute topology.");
		}

		deployTopology(topologyName);
	}

	/**
	 * Change Replication Factor.
	 * 
	 * @param repFactor
	 *            Replication factor
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void changeRepFactor(int repFactor) throws AnkushException {
		logger.info("Changing replication factor.");
		// Fetch StorageNode pool name.
		String snPoolName = AppStoreWrapper.getAnkushConfReader()
				.getStringValue("oracle.nosql.snpoolname");

		String topologyName = cloneTopology();

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" topology change-repfactor -name \"")
				.append(topologyName).append("\" -pool \"").append(snPoolName)
				.append("\"").append(" -dcname \"")
				.append(this.conf.getDatacenterName()).append("\" -rf ")
				.append(repFactor);

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not change replication factor.");
		}

		deployTopology(topologyName);
	}

	/**
	 * Re-balance the Topology candidate.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void rebalanceTopology() throws AnkushException {
		logger.info("Rebalancing topology.");
		// Fetch StorageNode pool name.
		String snPoolName = AppStoreWrapper.getAnkushConfReader()
				.getStringValue("oracle.nosql.snpoolname");

		String topologyName = cloneTopology();

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" topology rebalance -name \"").append(topologyName)
				.append("\" -pool \"").append(snPoolName).append("\"");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not rebalance topology.");
		}

		deployTopology(topologyName);
	}

	/**
	 * Adds the schema.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param schema
	 *            the schema
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void addSchema(NodeConf nodeConf, String schema)
			throws AnkushException {
		String schemaName = schema.replaceFirst(".*/", "");

		// Upload schema
		try {
			SSHExec connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					this.conf.getClusterConf().getUsername(), this.conf
							.getClusterConf().getPassword(), this.conf
							.getClusterConf().getPrivateKey());
			connection.downloadFile(schema, "/tmp/");
			this.connection
					.uploadSingleDataToServer("/tmp/" + schemaName, "./");
		} catch (Exception e) {
			throw new AnkushException("Could not upload schema.");
		}

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" ddl add-schema -file \"").append(schemaName)
				.append("\" | grep 'Added schema:'");
		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not add schema.");
		}
	}

	/**
	 * Interrupt plan.
	 * 
	 * @param planId
	 *            the plan id
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void interruptPlan(int planId) throws AnkushException {
		logger.info("Interrupting plan...");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan interrupt -id ").append(planId)
				.append(" | grep 'was interrupted'");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not interrupt plan(" + planId
					+ ").");
		}
	}

	/**
	 * Cancel plan.
	 * 
	 * @param planId
	 *            the plan id
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void cancelPlan(int planId) throws AnkushException {
		logger.info("Canceling plan...");

		// Create command cancelling
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan cancel -id ").append(planId)
				.append(" | grep 'was canceled'");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not cancel plan(" + planId + ").");
		}
	}

	/**
	 * Wait plan.
	 * 
	 * @param planId
	 *            the plan id
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void waitPlan(int planId) throws AnkushException {
		logger.info("Waiting for a plan...");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan wait -id ").append(planId)
				.append(" | grep 'ended successfully'");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not wait for a plan(" + planId
					+ ").");
		}
	}

	/**
	 * Execute plan.
	 * 
	 * @param planId
	 *            the plan id
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void executePlan(int planId) throws AnkushException {
		logger.info("Executing a plan...");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan execute -id ").append(planId)
				.append(" | grep 'Started plan'");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not execute a plan(" + planId
					+ ").");
		}
	}

	/**
	 * Execute and wait plan.
	 * 
	 * @param planId
	 *            the plan id
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public void executeAndWaitPlan(int planId) throws AnkushException {
		logger.info("Executing and waiting for a plan...");

		// Create command
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan execute -wait -id ").append(planId)
				.append(" | grep 'ended successfully'");

		if (!executeCommand(command.toString())) {
			throw new AnkushException("Could not execute and wait for plan("
					+ planId + ").");
		}
	}
}
