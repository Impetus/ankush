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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.DeleteLineFromFile;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.SSHConnection;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.Version;

/**
 * Manage Oracle NoSQL cluster operations like create, delete, add node.
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLDeployer implements Deployable {

	/** The Constant AGENT_ORACLE_SERVICE. */
	private static final String AGENT_ORACLE_SERVICE = "com.impetus.ankush.agent.oracle.OracleServiceStatusMonitor";

	/** The Constant JAVA_JAR. */
	private static final String JAVA_JAR = "java -jar ";

	/** The Constant PLAN_SUCCESS. */
	private static final String PLAN_SUCCESS = " -wait | grep 'ended successfully'";

	/** The Constant DEPLOY. */
	private static final String DEPLOY = "deploy";

	/** The Constant LINE_SEPERATOR. */
	private static final String LINE_SEPERATOR = "\n";

	/** The Constant DEFAULT_POOL_NAME. */
	private static final String DEFAULT_POOL_NAME = "AllStorageNodes";

	/** The Constant CREATE_NODE. */
	private static final int CREATE_NODE = 0;

	/** The Constant DELETE_NODE. */
	private static final int DELETE_NODE = 1;

	/** The Constant START_NODE. */
	private static final int START_NODE = 2;

	/** The Constant STOP_NODE. */
	private static final int STOP_NODE = 3;

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(OracleNoSQLDeployer.class);

	/** The config. */
	private OracleNoSQLConf config = null;

	/** The admin command. */
	private String adminCommand = null;

	/**
	 * Class constructor.
	 */
	public OracleNoSQLDeployer() {
	}

	/**
	 * Check if any admin node is up or not.
	 * 
	 * @return true, if is admin node deployed
	 */
	private boolean isAdminNodeDeployed() {
		for (NodeConf nodeConf : this.config.getNodes()) {
			if (nodeConf.getStatus()
					&& ((OracleNoSQLNodeConf) nodeConf).isAdmin()) {
				return true;
			}
		}

		logger.error("Getting AdminNode failed.");
		return false;
	}

	/**
	 * Install package and start service on nodes.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @return true, if successful
	 */
	private boolean createNodes(List<NodeConf> nodeConfs) {
		// Get ganglia host ip matching
		logger.info("Creating nodes...");
		if (!asyncOperation(nodeConfs, CREATE_NODE)) {
			logger.info("Could not create nodes.");
			return false;
		}
		return true;
	}

	/**
	 * Perform operations on node.
	 * 
	 * @param operation
	 *            Operation code
	 * @param nodeConf
	 *            Node configuration
	 */
	private void runOperation(final int operation, final NodeConf nodeConf) {
		// Call async operation
		switch (operation) {
		case START_NODE:
			nodeConf.setStatus(startNode((OracleNoSQLNodeConf) nodeConf));
			if (!nodeConf.getStatus()) {
				nodeConf.addError("service", "Could not start node.");
				logger.error(nodeConf.getPublicIp(), "Could not start node.");
			}
			break;
		case STOP_NODE:
			nodeConf.setStatus(stopNode((OracleNoSQLNodeConf) nodeConf));
			if (!nodeConf.getStatus()) {
				nodeConf.addError("service", "Could not stop node.");
				logger.error(nodeConf.getPublicIp(), "Could not stop node.");
			}
			break;
		case DELETE_NODE:
			nodeConf.setStatus(deleteNode((OracleNoSQLNodeConf) nodeConf));
			if (!nodeConf.getStatus()) {
				nodeConf.addError("undeploy", "Could not delete node.");
				logger.error(nodeConf.getPublicIp(), "Could not delete node.");
			}
			break;
		case CREATE_NODE:
			nodeConf.setStatus(createNode((OracleNoSQLNodeConf) nodeConf));
			if (!nodeConf.getStatus()) {
				nodeConf.addError(DEPLOY, "Could not create node.");
				logger.error(nodeConf.getPublicIp(), "Could not create node.");
			} else {
				nodeConf.setNodeState(OracleNoSQLNodeConf.STATE_STARTED);
			}
			break;
		}
	}

	/**
	 * Perform asynchronous operations nodes.
	 * 
	 * @param nodeConfList
	 *            List of node configuration
	 * @param operation
	 *            Operation code
	 * @return true, if successful
	 */
	private boolean asyncOperation(List<NodeConf> nodeConfList,
			final int operation) {

		// Create semaphore to join threads
		final Semaphore semaphore = new Semaphore(nodeConfList.size());
		try {
			for (final NodeConf nodeConf : nodeConfList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						runOperation(operation, nodeConf);

						if (semaphore != null) {
							semaphore.release();
						}
					}

				});
			}
			semaphore.acquire(nodeConfList.size());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}

		// Return true if any of the node is deployed.
		for (NodeConf nodeConf : nodeConfList) {
			if (!nodeConf.getStatus()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Execute command using remote connection.
	 * 
	 * @param connection
	 *            Remote connection
	 * @param command
	 *            Command which should be executed
	 * @return true, if successful
	 */
	private boolean executeCommand(SSHExec connection, String command) {
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
	 * Deploy KV store.
	 * 
	 * @return true, if successful
	 */
	private boolean deployStore() {
		logger.info("Deploying store...");

		// Set admin node and its related fields
		OracleNoSQLNodeConf adminNode = getAdminNode();

		if (adminNode == null) {
			logger.error("Could not get admin node.");
			return false;
		}

		SSHExec connection = adminNode.getConnection();

		if (connection == null) {
			return false;
		}

		if (!configureStore(connection)) {
			return false;
		}

		if (!deployDatacenter(connection)) {
			return false;
		}

		// Deploy Administrator Node
		if (!deployNode(adminNode, connection)) {
			return false;
		}

		// Deploy rest of the storage nodes
		for (NodeConf conf : this.config.getNodes()) {
			if (!deployNode((OracleNoSQLNodeConf) conf, connection)) {
				return false;
			}
		}

		if (!createTopology(connection)) {
			return false;
		}

		return deployTopology(connection);
	}

	/**
	 * Deploy topology.
	 * 
	 * @param connection
	 *            Remote connection
	 * @return true, if successful
	 */
	private boolean deployTopology(SSHExec connection) {
		// Deploy Topology
		logger.info("Deploying topology.");
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan deploy-topology -name \"")
				.append(this.config.getTopologyName())
				.append("\" -wait | grep 'ended successfully'");
		if (!executeCommand(connection, command.toString())) {
			logger.error("Deploying topology failed.");
			return false;
		}

		return true;
	}

	/**
	 * Create topology.
	 * 
	 * @param connection
	 *            Remote connection
	 * @return true, if successful
	 */
	private boolean createTopology(SSHExec connection) {
		// Create Topology
		logger.info("Creating topology.");
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" topology create -name \"")
				.append(this.config.getTopologyName()).append("\" -pool \"")
				.append(DEFAULT_POOL_NAME).append("\" -partitions ")
				.append(this.config.getPartitionCount())
				.append(" | grep 'Created: '");
		if (!executeCommand(connection, command.toString())) {
			logger.error("Creating topology failed.");
			return false;
		}
		return true;
	}

	/**
	 * Deploy datacenter.
	 * 
	 * @param connection
	 *            Remote connection
	 * @return true, if successful
	 */
	private boolean deployDatacenter(SSHExec connection) {
		// Deploy Datacenter
		logger.info("Deploying datacenter(s)...");
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" plan deploy-datacenter -name \"")
				.append(this.config.getDatacenterName()).append("\" -rf ")
				.append(this.config.getReplicationFactor())
				.append(PLAN_SUCCESS);
		if (!executeCommand(connection, command.toString())) {
			logger.error("Deploying datacente failed.");
			return false;
		}

		if (this.config.getDatacenters() != null) {
			for (OracleNoSQLDatacenter dc : this.config.getDatacenters()) {
				command = new StringBuilder(adminCommand)
						.append(" plan deploy-datacenter -name \"")
						.append(dc.getName()).append("\" -rf ")
						.append(dc.getRepFactor()).append(PLAN_SUCCESS);
				if (!executeCommand(connection, command.toString())) {
					logger.error("Deploying datacente failed.");
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Configure the store.
	 * 
	 * @param connection
	 *            Remote connection
	 * @return true, if successful
	 */
	private boolean configureStore(SSHExec connection) {
		// Configure the store
		logger.info("Configuring store");
		StringBuilder command = new StringBuilder(adminCommand)
				.append(" configure -name \"")
				.append(this.config.getClusterName())
				.append("\" | grep 'Store configured: '");
		if (!executeCommand(connection, command.toString())) {
			logger.error("Configuring store failed.");
			return false;
		}
		return true;
	}

	/**
	 * Select admin node.
	 * 
	 * @return the admin node
	 */
	private OracleNoSQLNodeConf getAdminNode() {
		// Get admin node connection object
		logger.info("Getting SSH connection on admin node.");
		for (NodeConf conf : this.config.getNodes()) {
			OracleNoSQLNodeConf nodeConf = (OracleNoSQLNodeConf) conf;
			// Get connection using running admin node.
			if (nodeConf.isAdmin() && nodeConf.getStatus()) {
				nodeConf.connect(this.config.getClusterConf());
				if (nodeConf.getConnection() != null) {
					// Set admin command
					if (adminCommand == null || adminCommand.isEmpty()) {
						adminCommand = new StringBuilder().append(JAVA_JAR)
								.append(this.config.getKvStoreJarPath())
								.append(" runadmin -host ")
								.append(nodeConf.getPrivateIp())
								.append(" -port ")
								.append(nodeConf.getRegistryPort()).toString();
					}
					return nodeConf;
				}
			}
		}
		return null;
	}

	/**
	 * Deploy a single node.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @param connection
	 *            Remote connection
	 * @return true, if successful
	 */
	private boolean deployNode(OracleNoSQLNodeConf nodeConf, SSHExec connection) {

		int snId = 0;
		if (nodeConf.getNodeState().equals(OracleNoSQLNodeConf.STATE_STARTED)) {
			logger.info(nodeConf.getPublicIp(), "Deploying node.");
			// Deploy storage node
			if (!deploySnService(nodeConf, connection)) {
				return false;
			}

			// Get storage node id
			snId = getStorageNodeId(nodeConf);

			// Check storage node id
			if (snId == 0) {
				nodeConf.addError(DEPLOY, "Coud not get storage node id.");
				logger.error(nodeConf.getPublicIp(),
						"Could not get storage node id.");
				nodeConf.setNodeState(OracleNoSQLNodeConf.STATE_ERROR);
				return false;
			}

			if (!deployAdminService(nodeConf, connection, snId)) {
				return false;
			}

			nodeConf.setNodeState(OracleNoSQLNodeConf.STATE_DEPLOYED);
			nodeConf.setSnId(snId);

			// Set Agent configuration
			setAgentConfiguration(nodeConf, snId);

			logger.info(nodeConf.getPublicIp(), "Node deployed.");
		}
		return true;
	}

	/**
	 * Delete agent configuration.
	 * 
	 * @param nodeConf
	 *            the node conf
	 */
	private void deleteAgentConfiguration(OracleNoSQLNodeConf nodeConf) {
		String userHome = CommonUtil.getUserHome(this.config.getUsername());
		String taskableConf = userHome
				+ Constant.Agent.AGENT_TASKABLE_FILE_PATH;
		String agentConf = userHome + Constant.Agent.AGENT_PROPERTY_FILE_PATH;

		// Delete from taskableconf
		AnkushTask task = new DeleteLineFromFile(AGENT_ORACLE_SERVICE,
				taskableConf);
		executeCommand(nodeConf.getConnection(), task.getCommand());

		// Delete from agent conf
		task = new DeleteLineFromFile("SNID=", agentConf);
		executeCommand(nodeConf.getConnection(), task.getCommand());
		task = new DeleteLineFromFile("ADMIN=", agentConf);
		executeCommand(nodeConf.getConnection(), task.getCommand());
		task = new DeleteLineFromFile("REGISTRY_PORT=", agentConf);
		executeCommand(nodeConf.getConnection(), task.getCommand());
		task = new DeleteLineFromFile("HOSTNAME=", agentConf);
		executeCommand(nodeConf.getConnection(), task.getCommand());
		task = new DeleteLineFromFile("KVJAR=", agentConf);
		executeCommand(nodeConf.getConnection(), task.getCommand());
		task = new DeleteLineFromFile("JARPATH=", agentConf);
		executeCommand(nodeConf.getConnection(), task.getCommand());
	}

	/**
	 * Make entries into agent property files.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @param snId
	 *            Storage node id
	 */
	private void setAgentConfiguration(OracleNoSQLNodeConf nodeConf, int snId) {
		AnkushTask task = null;
		logger.info(nodeConf.getPublicIp(), "Adding entry in agent file.");
		String userHome = CommonUtil.getUserHome(this.config.getUsername());
		String taskableConf = userHome
				+ Constant.Agent.AGENT_TASKABLE_FILE_PATH;
		String agentConf = userHome + Constant.Agent.AGENT_PROPERTY_FILE_PATH;

		String serviceStatusClass = LINE_SEPERATOR + AGENT_ORACLE_SERVICE
				+ LINE_SEPERATOR + Constant.TaskableClass.DIR_USAGE_MONITOR;
		StringBuilder confStr = new StringBuilder();
		confStr.append("HOSTNAME=")
				.append(nodeConf.getPrivateIp())
				.append(LINE_SEPERATOR)
				.append("REGISTRY_PORT=")
				.append(nodeConf.getRegistryPort())
				.append(LINE_SEPERATOR)
				.append("SNID=")
				.append(snId)
				.append(LINE_SEPERATOR)
				.append("ADMIN=")
				.append(nodeConf.isAdmin())
				.append(LINE_SEPERATOR)
				.append("KVJAR=")
				.append(this.config.getKvStoreJarPath())
				.append(LINE_SEPERATOR)
				.append("JARPATH=")
				.append(this.config.getKvStoreJarPath())
				.append(",")
				.append(this.config.getKvStoreJarPath().replace("kvstore.jar",
						"je.jar")).append(LINE_SEPERATOR)
				.append("DATA_DIR_LIST=").append(this.config.getDataPath());

		task = new AppendFile(confStr.toString(), agentConf);

		if (!executeCommand(nodeConf.getConnection(), task.getCommand())) {
			logger.error(nodeConf.getPublicIp(),
					"Could not edit agent.properties file.");
		}

		task = new AppendFile(serviceStatusClass, taskableConf);
		if (!executeCommand(nodeConf.getConnection(), task.getCommand())) {
			logger.error(nodeConf.getPublicIp(),
					"Could not edit taskable.conf file.");
		}
	}

	/**
	 * Deploy admin service.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @param connection
	 *            Remote connection
	 * @param snId
	 *            Storage node id
	 * @return true, if successful
	 */
	private boolean deployAdminService(OracleNoSQLNodeConf nodeConf,
			SSHExec connection, int snId) {
		StringBuilder command;
		if (nodeConf.isAdmin()) {
			// Deploy administrative service
			logger.info(nodeConf.getPublicIp(), "Deploying admin service.");
			command = new StringBuilder(adminCommand)
					.append(" plan deploy-admin -sn ").append(snId)
					.append(" -port ").append(nodeConf.getAdminPort())
					.append(PLAN_SUCCESS);

			if (!executeCommand(connection, command.toString())) {
				nodeConf.addError(DEPLOY, "Deploying admin service failed.");
				logger.error(nodeConf.getPublicIp(),
						"Deploying admin service failed.");
				nodeConf.setNodeState(OracleNoSQLNodeConf.STATE_ERROR);
				return false;
			}
		}
		return true;
	}

	/**
	 * Fetch storage node id.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @return the storage node id
	 */
	private int getStorageNodeId(OracleNoSQLNodeConf nodeConf) {
		StringBuilder command;
		SSHConnection conn = null;
		int snId = 0;
		conn = new SSHConnection(nodeConf.getPublicIp(),
				this.config.getUsername(), this.config.getPassword(),
				this.config.getPrivateKey());

		if (conn != null && conn.isConnected()) {
			command = new StringBuilder(adminCommand)
					.append(" show topology | grep \" ")
					.append(nodeConf.getPrivateIp())
					.append(":\" | grep  \"\\[sn[0-9]\\+\\]\" -o | grep \"[0-9]\\+\" -o");
			conn.exec(command.toString());
			if (conn.getExitStatus() == 0 && conn.getOutput() != null) {
				snId = Integer.parseInt(conn.getOutput());
			}
		}
		return snId;
	}

	/**
	 * Deploy storage node service.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @param connection
	 *            Remote connection
	 * @return true, if successful
	 */
	private boolean deploySnService(OracleNoSQLNodeConf nodeConf,
			SSHExec connection) {
		StringBuilder command;
		command = new StringBuilder(adminCommand)
				.append(" plan deploy-sn -dcname \"")
				.append(nodeConf.getDatacenter() == null
						|| nodeConf.getDatacenter().isEmpty() ? this.config
						.getDatacenterName() : nodeConf.getDatacenter())
				.append("\" -host ").append(nodeConf.getPrivateIp())
				.append(" -port ").append(nodeConf.getRegistryPort())
				.append(PLAN_SUCCESS);
		if (!executeCommand(connection, command.toString())) {
			nodeConf.addError(DEPLOY, "Deploying node failed.");
			logger.error(nodeConf.getPublicIp(), "Deploying node failed.");
			nodeConf.setNodeState(OracleNoSQLNodeConf.STATE_ERROR);
			return false;
		}
		return true;
	}

	/**
	 * Set configuration and logger object.
	 * 
	 * @param config
	 *            Cluster configuration
	 */
	private void setConfig(Configuration config) {
		// Set config
		this.config = (OracleNoSQLConf) config;
		// Set logger
		logger.setCluster(this.config.getClusterConf());
	}

	/**
	 * Deploy the cluster.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean deploy(Configuration config) {
		setConfig(config);

		// Check supported version
		if (Version.compare(this.config.getComponentVersion(), "2.0.22") == -1) {
			logger.error(Constant.Technology.ORACLE_NOSQL + " version "
					+ this.config.getComponentVersion() + " is not supported.");
			return false;
		} else if (Version.compare(this.config.getComponentVersion(), "2.1.8") == -1
				&& this.config.getDatacenters() != null) {
			logger.error("Multiple datacenter is not supported by "
					+ Constant.Technology.ORACLE_NOSQL + " version "
					+ this.config.getComponentVersion() + ".");
			return false;
		}

		logger.info("Deploying cluster...");

		// Setup individual nodes.
		if (createNodes(new ArrayList<NodeConf>(this.config.getNodes()))) {
			// Check node status
			if (isAdminNodeDeployed()) {
				// Deploy the store
				if (deployStore()) {
					logger.info("Cluster deployed.");
					// Check technology/topology data
					checkTechnologyData();
					return true;
				} else {
					this.config.addError("Deploy",
							"Could not deploy the store.");
				}
			} else {
				this.config.addError("Deploy", "Could not deploy Admin Node.");
			}
		}

		logger.error("Deploying cluster failed.");
		return false;
	}

	/**
	 * Undeploy the cluster.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean undeploy(Configuration config) {
		setConfig(config);
		return removeNodes(new ArrayList<NodeConf>(this.config.getNodes()),
				config);
	}

	/**
	 * Start the cluster.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean start(Configuration config) {
		setConfig(config);
		return startServices(new ArrayList<NodeConf>(this.config.getNodes()),
				config);
	}

	/**
	 * Stop the cluster.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean stop(Configuration config) {
		setConfig(config);
		return stopServices(new ArrayList<NodeConf>(this.config.getNodes()),
				config);
	}

	/**
	 * Start service.o
	 * 
	 * @param nodeList
	 *            the node list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		if (nodeList == null || nodeList.isEmpty()) {
			return true;
		}
		setConfig(config);
		if (this.config.getClusterConf().getState()
				.equals(Constant.Cluster.State.DEPLOYING)
				|| this.config.getClusterConf().getState()
						.equals(Constant.Cluster.State.ADDING_NODES)) {
			return true;
		}
		logger.info("Starting nodes.");
		if (!asyncOperation(nodeList, START_NODE)) {
			logger.error("Could not start nodes.");
			return false;
		}
		return true;
	}

	/**
	 * Stop selected nodes.
	 * 
	 * @param nodeList
	 *            the node conf list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		if (nodeList == null || nodeList.isEmpty()) {
			return true;
		}
		setConfig(config);
		logger.info("Stopping nodes.");
		if (!asyncOperation(nodeList, STOP_NODE)) {
			logger.error("Could not stop nodes.");
			return false;
		}
		return true;
	}

	/**
	 * Add nodes.
	 * 
	 * @param nodeConfList
	 *            the node conf list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeConfList, Configuration config) {

		setConfig(config);
		logger.info("Adding nodes...");

		try {

			// Setup individual nodes.
			if (createNodes(nodeConfList)) {

				// Set admin node and its related fields
				OracleNoSQLNodeConf adminNode = getAdminNode();

				if (adminNode == null) {
					throw new Exception("Could not get admin node.");
				}

				SSHExec connection = adminNode.getConnection();

				if (connection == null) {
					throw new Exception("Could not get admin node connection.");
				}

				// Deploy storage Nodes
				for (NodeConf nodeConf : nodeConfList) {
					if (!deployNode((OracleNoSQLNodeConf) nodeConf, connection)) {
						// Free connection
						if (connection != null) {
							connection.disconnect();
						}
						return false;
					}
				}

				// Free connection
				if (connection != null) {
					connection.disconnect();
				}
				return true;
			}
			this.config.addError("Add Node", "Adding nodes failed.");
			logger.error("Adding nodes failed.");
		} catch (Exception e) {
			this.config.addError("Add Node", e.getMessage());
			logger.error("Adding nodes failed.", e);
		}
		return false;
	}

	/**
	 * Remove nodes.
	 * 
	 * @param nodeConfList
	 *            the node conf list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeConfList, Configuration config) {
		setConfig(config);
		logger.info("Deleting nodes.");
		if (!asyncOperation(nodeConfList, DELETE_NODE)) {
			logger.error("Could not delete nodes.");
			return false;
		}
		return true;

	}

	/**
	 * Create node.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @return true, if successful
	 */
	private boolean createNode(OracleNoSQLNodeConf nodeConf) {

		logger.info(nodeConf.getPublicIp(), "Copying and installing package...");

		// Create SSH connection
		if (!nodeConf.isConnected()) {
			nodeConf.connect(this.config.getClusterConf());
		}

		// Get list of storage dirs
		String[] storageDirs = null;
		if (nodeConf.getStorageDirs() != null
				&& !nodeConf.getStorageDirs().isEmpty()) {
			storageDirs = nodeConf.getStorageDirs().split(",");
		}

		StringBuilder command;
		if (!createDirectories(nodeConf, storageDirs)) {
			logger.error(nodeConf.getPublicIp(),
					"Could not create directories.");
			return false;
		}

		// Install package
		if (!SSHUtils.getAndExtractComponent(nodeConf.getConnection(),
				this.config, Constant.Component.Name.ORACLE_NOSQL)) {
			return false;
		}

		// Synchronize node
		logger.info(nodeConf.getPublicIp(), "Synchronizing node...");

		command = new StringBuilder().append("echo \"")
				.append(this.config.getClusterConf().getPassword())
				.append("\" | sudo -S /usr/sbin/ntpdate ")
				.append(this.config.getNtpServer());
		if (!executeCommand(nodeConf.getConnection(), command.toString())) {
			return false;
		}

		if (!configureNode(nodeConf, storageDirs)) {
			logger.error(nodeConf.getPublicIp(),
					"Could not configure the node.");
			return false;
		}

		if (!startNode(nodeConf)) {
			return false;
		}

		// Create jmxtrans config file and restart jmxtrans.
		startJmxtrans(nodeConf);
		return checkService(nodeConf);
	}

	/**
	 * Configure node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param storageDirs
	 *            the storage dirs
	 * @return true, if successful
	 */
	private boolean configureNode(OracleNoSQLNodeConf nodeConf,
			String[] storageDirs) {
		StringBuilder command;
		// Configure the node
		logger.info(nodeConf.getPublicIp(), "Configuring node...");

		command = new StringBuilder().append(JAVA_JAR)
				.append(this.config.getKvStoreJarPath())
				.append(" makebootconfig");
		command.append(" -root \"").append(this.config.getDataPath())
				.append("\" -host ").append(nodeConf.getPrivateIp())
				.append(" -port ").append(nodeConf.getRegistryPort())
				.append(" -capacity ").append(nodeConf.getCapacity())
				.append(" -num_cpus ").append(nodeConf.getCpuNum())
				.append(" -memory_mb ").append(nodeConf.getMemoryMb())
				.append(" -harange ").append(nodeConf.getHaPortRangeStart())
				.append(",").append(nodeConf.getHaPortRangeEnd())
				.append(" -mgmt jmx ");
		if (storageDirs != null) {
			for (int count = 0; count < storageDirs.length
					&& count < nodeConf.getCapacity(); count++) {
				command.append(" -storagedir \"").append(storageDirs[count])
						.append("\"");
			}
		}

		if (nodeConf.isAdmin()) {
			command.append(" -admin ").append(nodeConf.getAdminPort());
		}
		return executeCommand(nodeConf.getConnection(), command.toString());
	}

	/**
	 * Creates the directories.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param storageDirs
	 *            the storage dirs
	 * @return true, if successful
	 */
	private boolean createDirectories(OracleNoSQLNodeConf nodeConf,
			String[] storageDirs) {
		logger.info(nodeConf.getPublicIp(), "Creating folders...");
		// Create folders
		StringBuilder command = new StringBuilder().append("[[ ! -d ")
				.append(this.config.getDataPath()).append(" ]] && mkdir -p \"")
				.append(this.config.getInstallationPath())
				.append("\" && mkdir -p \"").append(this.config.getDataPath())
				.append("\"");
		if (storageDirs != null) {
			for (int count = storageDirs.length - 1; count >= 0; count--) {
				command.append(" && mkdir -p \"").append(storageDirs[count])
						.append("\"");
			}
		}

		return executeCommand(nodeConf.getConnection(), command.toString());
	}

	/**
	 * Start jmxtrans.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	private boolean startJmxtrans(OracleNoSQLNodeConf nodeConf) {
		try {
			// Get JSON object
			ConfigurationReader ankushConf = AppStoreWrapper
					.getAnkushConfReader();
			String jsonFile = AppStoreWrapper.getResourcePath()
					+ ankushConf
							.getStringValue("oracle.nosql.jmxtransconfigfile");
			if (jsonFile == null) {
				throw new AnkushException("Could not get JMXtrans config file.");
			}
			JSONObject jmxtransConf = JsonMapperUtil.objectFromJsonFile(
					jsonFile, JSONObject.class);

			// Update default values
			Iterator servers = jmxtransConf.getJSONArray("servers").iterator();
			while (servers.hasNext()) {
				JSONObject server = (JSONObject) servers.next();
				server.put("host", nodeConf.getPrivateIp());
				server.put("port", nodeConf.getRegistryPort());

				Iterator queries = server.getJSONArray("queries").iterator();

				while (queries.hasNext()) {
					JSONObject query = (JSONObject) queries.next();
					JSONArray outputWriters = query
							.getJSONArray("outputWriters");
					JSONObject outputWriter = outputWriters.getJSONObject(0);
					JSONObject settings = outputWriter
							.getJSONObject("settings");
					settings.put("host", this.config.getClusterConf()
							.getGangliaMaster().getPrivateIp());
				}

			}

			// Json file path
			String filePath = ankushConf.getStringValue("agent.dir")
					+ "jmxtrans/kvstore.json";

			// Clear file if exist
			CustomTask task = new ClearFile(filePath);
			nodeConf.getConnection().exec(task);

			// Create jmxtrans config file
			task = new AppendFile(jmxtransConf.toString(4)
					.replace("\"", "\\\""), filePath);

			if (nodeConf.getConnection().exec(task).rc != 0) {
				throw new AnkushException(
						"Could not create jmxtrans config file.");
			}

		} catch (Exception e) {
			logger.error(nodeConf.getPublicIp(), e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Check node service.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @return true, if successful
	 */
	private boolean checkService(OracleNoSQLNodeConf nodeConf) {
		long sleepTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
				"oracle.nosql.servicechecksleeptime");
		long checkTimeout = AppStoreWrapper.getAnkushConfReader().getLongValue(
				"oracle.nosql.servicechecktimeout");
		int totalTries = (int) (checkTimeout / sleepTime);

		StringBuilder command = new StringBuilder().append(JAVA_JAR)
				.append(this.config.getKvStoreJarPath()).append(" ping -host ")
				.append(nodeConf.getPrivateIp()).append(" -port ")
				.append(nodeConf.getRegistryPort())
				.append(" 2>&1 | grep 'SNA at hostname:'");

		logger.info(nodeConf.getPublicIp(), "Checking service.");
		for (int count = 0; count < totalTries; count++) {

			// Check services
			if (executeCommand(nodeConf.getConnection(), command.toString())) {
				return true;
			}

			try {
				logger.debug("Wait for " + sleepTime + " milliseconds.");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.debug(e.getMessage());
			}
		}
		logger.error(nodeConf.getPublicIp(), "Checking service failed (after "
				+ checkTimeout + "s).");
		return false;
	}

	/**
	 * Start node service.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @return true, if successful
	 */
	private boolean startNode(OracleNoSQLNodeConf nodeConf) {
		logger.info(nodeConf.getPublicIp(), "Starting node...");

		// Create SSH connection
		if (!nodeConf.isConnected()) {
			nodeConf.connect(this.config.getClusterConf());
		}

		StringBuilder command = new StringBuilder().append(JAVA_JAR)
				.append(this.config.getKvStoreJarPath())
				.append(" start -root \"").append(this.config.getDataPath())
				.append("\" </dev/null >nohup.out 2>&1 &");
		if (!executeCommand(nodeConf.getConnection(), command.toString())) {
			return false;
		}

		// Set node state
		nodeConf.setNodeState(OracleNoSQLNodeConf.STATE_STARTED);

		// Start agent
		executeCommand(nodeConf.getConnection(), "sh "
				+ Constant.Agent.AGENT_START_SCRIPT);

		// Start ganglia
		if (this.config.getClusterConf().getGangliaMaster().getPublicIp() != null
				&& this.config.getClusterConf().getGangliaMaster()
						.getPublicIp().equals(nodeConf.getPublicIp())) {
			command = new StringBuilder()
					.append("ps -e | grep gmetad || echo \"")
					.append(this.config.getClusterConf().getPassword())
					.append("\" | sudo -S gmetad --conf=.ankush/monitoring/conf/gmetad.conf");
			executeCommand(nodeConf.getConnection(), command.toString());
		}
		command = new StringBuilder()
				.append("ps -e | grep gmond || echo \"")
				.append(this.config.getClusterConf().getPassword())
				.append("\" | sudo -S gmond --conf=.ankush/monitoring/conf/gmond.conf");
		executeCommand(nodeConf.getConnection(), command.toString());

		return true;
	}

	/**
	 * Delete node.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @return true, if successful
	 */
	private boolean deleteNode(OracleNoSQLNodeConf nodeConf) {
		logger.info(nodeConf.getPublicIp(), "Removing node...");

		// Create SSH connection
		if (!nodeConf.isConnected()) {
			nodeConf.connect(this.config.getClusterConf());
		}

		// Remove installation
		StringBuilder command = new StringBuilder().append("echo \"")
				.append(this.config.getPassword())
				.append("\" | sudo -S rm -rf \"")
				.append(this.config.getInstallationPath()).append("\"* \"")
				.append(this.config.getDataPath()).append("\"");

		// Remove storageDirs
		if (nodeConf.getStorageDirs() != null
				&& !nodeConf.getStorageDirs().isEmpty()) {
			String[] storageDirs = nodeConf.getStorageDirs().split(",");
			for (String storageDir : storageDirs) {
				command.append(" \"").append(storageDir).append("\"");
			}
		}

		if (!executeCommand(nodeConf.getConnection(), command.toString())) {
			return false;
		}

		// Remove entries from agent property files
		deleteAgentConfiguration(nodeConf);
		return true;
	}

	/**
	 * Stop node service.
	 * 
	 * @param nodeConf
	 *            Node configuration
	 * @return true, if successful
	 */
	private boolean stopNode(OracleNoSQLNodeConf nodeConf) {
		logger.info(nodeConf.getPublicIp(), "Stopping node...");

		// Create SSH connection
		if (!nodeConf.isConnected()) {
			nodeConf.connect(this.config.getClusterConf());
		}

		// Stop node
		StringBuilder command = new StringBuilder().append(JAVA_JAR)
				.append(this.config.getKvStoreJarPath())
				.append(" stop -root \"").append(this.config.getDataPath())
				.append("\"");
		if (!executeCommand(nodeConf.getConnection(), command.toString())) {
			return false;
		}
		return true;
	}

	/**
	 * Check for availability of technology data.
	 * 
	 * @return true, if successful
	 */
	private boolean checkTechnologyData() {
		long sleepTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
				"oracle.nosql.topologychecksleeptime");
		long checkTimeout = AppStoreWrapper.getAnkushConfReader().getLongValue(
				"oracle.nosql.topologychecktimeout");
		int totalTries = (int) (checkTimeout / sleepTime);
		OracleNoSQLTechnologyData techData = null;

		logger.info("Getting technology data.");
		for (int count = 0; count < totalTries; count++) {
			// Get technology data
			techData = OracleNoSQLTechnologyData.getTechnologyData(this.config);

			if (techData != null && !techData.getRepNodeList().isEmpty()) {
				return true;
			}
			// Wait for few milliseconds
			try {
				logger.debug("Wait for " + sleepTime + " milliseconds.");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.debug(e.getMessage());
			}
		}
		logger.info("Could not get technology data.");
		return false;
	}
}
