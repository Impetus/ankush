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
package com.impetus.ankush.cassandra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.lang3.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class CassandraDeployer.
 */
public class CassandraDeployer implements Deployable {

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	/** The Constant CASSANDRA_CONF_ENV. */
	private static final String CASSANDRA_CONF_ENV = "cassandra-env.sh";

	/** The Constant CASSANDRA-TOPOLOGY_PROPERTIES. */
	private static final String CASSANDRA_TOPOLOGY_PROPERTIES = "cassandra-topology.properties";

	/** The Constant CASSANDRA-CASSANDRA_RACK_DC_PROPERTIES. */
	private static final String CASSANDRA_RACK_DC_PROPERTIES = "cassandra-rackdc.properties";

	/** The Constant CASSANDRA_CONF_YAML. */
	private static final String CASSANDRA_CONF_YAML = "cassandra.yaml";

	/** The Constant CASSANDRA_CONF_LOG_FILE. */
	private static final String CASSANDRA_CONF_LOG_FILE = "log4j-server.properties";

	/** The Constant CASSANDRA_FOLDER_BIN. */
	private static final String CASSANDRA_FOLDER_BIN = "bin/";

	/** The Constant CASSANDRA_FOLDER_CONF. */
	private static final String CASSANDRA_FOLDER_CONF = "conf/";

	/** The Constant CASSANDRA_SERVICE_START. */
	private static final String CASSANDRA_SERVICE_START = "cassandra";

	/** The Constant CASSANDRA_NODETOOL. */
	private static final String CASSANDRA_NODETOOL = "nodetool ";

	/** The Constant CASSANDRA_CLEANUP. */
	private static final String CASSANDRA_CLEANUP = "cleanup";

	/** The Constant CASSANDRA_VENDOR_DSC. */
	private static final String CASSANDRA_VENDOR_DSC = "dsc";

	/** The Constant CASSANDRA_VENDOR_APACHE. */
	private static final String CASSANDRA_VENDOR_APACHE = "apache";

	/** The Constant COMPONENT_CASSANDRA. */
	private static final String COMPONENT_CASSANDRA = "Cassandra";

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			Constant.Technology.CASSANDRA, CassandraDeployer.class);

	/** The Constant LINE_SEPERATOR. */
	private static final String LINE_SEPERATOR = System
			.getProperty("line.separator");

	private CassandraConf cassandraConf;

	/** The conf manager. */
	private static ConfigurationManager confManager = new ConfigurationManager();

	public CassandraDeployer(CassandraConf cassandraConf) {
		this.cassandraConf = cassandraConf;
	}

	public CassandraDeployer() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {

		// Casting Configuration to CassandraConf
		cassandraConf = (CassandraConf) config;

		// Set component home
		String vendor;

		if (cassandraConf.getComponentVendor().equalsIgnoreCase("Datastax")) {
			vendor = CASSANDRA_VENDOR_DSC;
		} else {
			vendor = CASSANDRA_VENDOR_APACHE;
		}

		String component_home = cassandraConf.getInstallationPath() + vendor
				+ "-cassandra-" + cassandraConf.getComponentVersion();

		cassandraConf.setComponentHome(component_home);

		return addNodes(new ArrayList<NodeConf>(cassandraConf.getNodes()),
				config);
	}

	private boolean checkTechnologyData() {
		long sleepTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
				"cassandra.topologychecksleeptime");
		long checkTimeout = AppStoreWrapper.getAnkushConfReader().getLongValue(
				"cassandra.topologychecktimeout");
		int totalTries = (int) (checkTimeout / sleepTime);
		boolean isTechDataAvailable = false;
		CassandraJMXData techData = null;

		logger.info("Fetching Cassandra Technology Data.");
		for (int count = 0; count < totalTries && !isTechDataAvailable; count++) {

			techData = CassandraJMXData.getTechnologyData(cassandraConf, true);
			if (techData != null && techData.getDatacenters() != null
					&& !techData.getDatacenters().isEmpty()) {
				break;
			}

			// Wait for few milliseconds
			try {
				logger.debug("Wait for " + sleepTime + " milliseconds.");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.debug(e.getMessage());
			}
		}
		logger.info("Could not get Cassandra technology data.");
		return false;
	}

	private String getSeedNodeString() {
		List<String> seedNodes = new ArrayList<String>();
		for (CassandraNodeConf nodeConf : cassandraConf.getNodes()) {
			if (nodeConf.isSeedNode()) {
				seedNodes.add(nodeConf.getPublicIp());
			}
		}

		if (cassandraConf.getNewNodes() != null) {
			for (NodeConf node : cassandraConf.getNewNodes()) {
				CassandraNodeConf nodeConf = (CassandraNodeConf) node;
				if (nodeConf.isSeedNode()) {
					seedNodes.add(nodeConf.getPublicIp());
				}
			}
		}

		return StringUtils.join(seedNodes, ",");
	}

	private String getJavaHome() {
		return ((CassandraClusterConf) cassandraConf.getClusterConf())
				.getJavaConf().getJavaHomePath();
	}

	private boolean installCassandra(CassandraNodeConf nodeConf,
			SSHExec connection) {
		logger.info(nodeConf.getPublicIp(), "Installing Cassandra ...");

		// Check java 7 for version 2.x.x
		if (cassandraConf.getComponentVendor().startsWith("2")) {
			if (!execCustomtask(getJavaHome()
					+ "bin/java -version | grep version | grep 1.7",
					connection, nodeConf.getPublicIp(), "Invalid java version.")) {
				return false;
			}
		}

		// Create directories
		if (!execCustomtask("mkdir -p " + cassandraConf.getInstallationPath()
				+ " " + cassandraConf.getLogDir(), connection,
				nodeConf.getPublicIp(), "Could not create directories.")) {
			return false;
		}

		// Extract package
		if (!SSHUtils.getAndExtractComponent(connection, cassandraConf,
				COMPONENT_CASSANDRA)) {
			logger.error(nodeConf.getPublicIp(),
					"Could not get/extract tarball.");
			return false;
		}

		return true;
	}

	/**
	 * Creates the node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param nodeCount
	 *            the node count
	 * @param seedNodes
	 *            the seed nodes
	 * @return true, if successful
	 */
	private boolean createNode(CassandraNodeConf nodeConf) {

		logger.setLoggerConfig(cassandraConf);
		SSHExec connection = null;

		try {
			logger.debug("Connecting with node : " + nodeConf.getPublicIp());

			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					cassandraConf.getUsername(), cassandraConf.getPassword(),
					cassandraConf.getPrivateKey());

			if (connection == null) {
				logger.log(nodeConf.getPublicIp(), "Connection", false);
				return false;
			}

			// if connected

			logger.info(nodeConf.getPublicIp(), "Deploying Cassandra ...");
			if (!installCassandra(nodeConf, connection)
					|| !configureCassandra(nodeConf, connection)
					|| !configureJMXMonitoring(connection, nodeConf)) {
				return false;
			}

			// adding process list and taskable information in agent conf
			addProcessInfo(nodeConf, connection);
			// save audit trails
			confManager.saveConfiguration(cassandraConf.getClusterDbId(),
					cassandraConf.getCurrentUser(), CASSANDRA_CONF_YAML,
					nodeConf.getPublicIp(),
					cassandraConf.getConfigurationMap(nodeConf));
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	private boolean editCassandraConfigFile(CassandraNodeConf nodeConf,
			SSHExec connection) {
		// Set auto_bootstap to true on adding new nodes.
		boolean autoBootStrap = cassandraConf.getClusterConf().getState()
				.equals(Constant.Cluster.State.ADDING_NODES);

		// Set cassandra.yaml file
		String cassandraYaml = cassandraConf.getComponentHome()
				+ CASSANDRA_FOLDER_CONF + CASSANDRA_CONF_YAML;
		StringBuilder command = new StringBuilder();

		command.append("sed -i -e \"/^cluster_name:/c\\cluster_name: '")
				.append(cassandraConf.getClusterName()).append("'\" ");
		command.append("-e '/^initial_token:/c#initial_token:' ");
		command.append("-e '/num_tokens:/cnum_tokens: ")
				.append(nodeConf.getvNodeCount()).append("' ");
		command.append("-e '/^partitioner:/c\\partitioner: ")
				.append(cassandraConf.getPartitioner()).append("' ");
		command.append("-e '/^rpc_port:/c\\rpc_port: ")
				.append(cassandraConf.getRpcPort()).append("' ");
		command.append("-e '/^storage_port:/c\\storage_port: ")
				.append(cassandraConf.getStoragePort()).append("' ");
		command.append("-e '/^listen_address:/clisten_address: ")
				.append(nodeConf.getPublicIp()).append("' ");
		command.append("-e '/^rpc_address:/c\\rpc_address: ")
				.append(nodeConf.getPublicIp()).append("' ");
		command.append("-e 's_/var/lib/cassandra/data_")
				.append(cassandraConf.getDataDir()).append("_g' ");
		command.append(
				"-e '/^saved_caches_directory:/c\\saved_caches_directory: ")
				.append(cassandraConf.getSavedCachesDir()).append("' ");
		command.append("-e '/^commitlog_directory:/c\\commitlog_directory: ")
				.append(cassandraConf.getCommitlogDir()).append("' ");
		command.append("-e 's/- seeds: \"127.0.0.1\"/- seeds: \"")
				.append(getSeedNodeString()).append("\"/' ");
		command.append("-e '/^endpoint_snitch:/cendpoint_snitch: ")
				.append(cassandraConf.getSnitch()).append("' ");
		command.append("-e '$ a\\auto_bootstrap: ").append(autoBootStrap)
				.append("' ").append(cassandraYaml);
		if (!execCustomtask(command.toString(), connection,
				nodeConf.getPublicIp(), "Unable to edit " + CASSANDRA_CONF_YAML
						+ ".")) {
			return false;
		}
		return true;
	}

	private String getTopologyString() {
		StringBuilder topology = new StringBuilder();
		topology.append("default=DC1:RAC1").append("\n");
		for (CassandraNodeConf nodeConf : cassandraConf.getNodes()) {
			if (nodeConf.getDatacenter() != null && nodeConf.getRack() != null
					|| !nodeConf.getDatacenter().isEmpty()
					|| !nodeConf.getRack().isEmpty()) {
				topology.append(nodeConf.getPublicIp()).append("=")
						.append(nodeConf.getDatacenter()).append(":")
						.append(nodeConf.getRack()).append("\n");
			}
		}
		return topology.toString();
	}

	private String getDCRackFileString(CassandraNodeConf nodeConf) {
		StringBuilder dcRack = new StringBuilder();

		if (nodeConf.getDatacenter() != null
				&& !nodeConf.getDatacenter().isEmpty()) {
			dcRack.append("dc=").append(nodeConf.getDatacenter()).append("\n");
		} else {
			dcRack.append("dc=DC1\n");
		}

		if (nodeConf.getRack() != null && !nodeConf.getRack().isEmpty()) {
			dcRack.append("rack=").append(nodeConf.getRack()).append("\n");
		} else {
			dcRack.append("rack=RAC1\n");
		}
		return dcRack.toString();
	}

	private boolean editTopologyFile(SSHExec connection,
			CassandraNodeConf nodeConf) {
		if (cassandraConf.getSnitch().equalsIgnoreCase("PropertyFileSnitch")) {
			// set rack info in cassandra-topology.properties
			String topologyFile = cassandraConf.getComponentHome()
					+ CASSANDRA_FOLDER_CONF + CASSANDRA_TOPOLOGY_PROPERTIES;
			if (!execCustomtask("echo \"" + getTopologyString() + "\" > "
					+ topologyFile, connection, nodeConf.getPublicIp(),
					"Unable to edit " + CASSANDRA_TOPOLOGY_PROPERTIES + ".")) {
				return false;
			}
		} else if (cassandraConf.getSnitch().equalsIgnoreCase(
				"GossipingPropertyFileSnitch")) {
			String topologyFile = cassandraConf.getComponentHome()
					+ CASSANDRA_FOLDER_CONF + CASSANDRA_RACK_DC_PROPERTIES;
			if (!execCustomtask("echo \"" + getDCRackFileString(nodeConf)
					+ "\" > " + topologyFile, connection,
					nodeConf.getPublicIp(), "Unable to edit "
							+ CASSANDRA_RACK_DC_PROPERTIES + ".")) {
				return false;
			}
		}
		return true;
	}

	private boolean updatePaths(CassandraNodeConf nodeConf, SSHExec connection) {
		// setting cassandra environment and configuration file
		// and JAVA_HOME in cassandra-env.sh
		String cassandraEnvFile = cassandraConf.getComponentHome()
				+ CASSANDRA_FOLDER_CONF + CASSANDRA_CONF_ENV;
		if (execCustomtask("echo \"" + getBashrcContents() + "\" >> .bashrc",
				connection, nodeConf.getPublicIp(), "Could not edit .bashrc.")
				&& execCustomtask("echo \"export JAVA_HOME=" + getJavaHome()
						+ "\" >> " + cassandraEnvFile, connection,
						nodeConf.getPublicIp(), "Could not edit "
								+ CASSANDRA_CONF_ENV + ".")) {
			return true;
		}
		return false;
	}

	private boolean configureCassandra(CassandraNodeConf nodeConf,
			SSHExec connection) {
		try {
			logger.info(nodeConf.getPublicIp(), "Configuring Cassandra ...");

			if (!updatePaths(nodeConf, connection)
					|| !editCassandraConfigFile(nodeConf, connection)
					|| !editTopologyFile(connection, nodeConf)) {
				return false;
			}

			StringBuilder command = new StringBuilder();

			// set log directory in log4j-server.properties file
			String cassandralogFile = cassandraConf.getComponentHome()
					+ CASSANDRA_FOLDER_CONF + CASSANDRA_CONF_LOG_FILE;
			command = new StringBuilder();
			command.append(
					"sed -i '/log4j.appender.R.File/c\\log4j.appender.R.File=")
					.append(cassandraConf.getLogDir()).append("system.log")
					.append("' ").append(cassandralogFile);
			if (execCustomtask(command.toString(), connection,
					nodeConf.getPublicIp(), "Unable to set log directory  in "
							+ cassandralogFile + " file")) {
				return true;
			}
		} catch (Exception e) {
			logger.error(nodeConf, e.getMessage());
		}
		return false;
	}

	private boolean execCustomtask(String command, SSHExec connection,
			String publicIp, String errMsg) {
		// set clusterName in cassandra.yaml
		try {
			CustomTask task = new ExecCommand(command);
			if (connection.exec(task).rc == 0) {
				return true;
			}
			logger.error(publicIp, errMsg);
		} catch (TaskExecFailException e) {
			logger.error(publicIp, errMsg, e);
		}
		return false;
	}

	private boolean configureJMXMonitoring(SSHExec connection,
			CassandraNodeConf nodeConf) {
		logger.debug("Configuring JMX Monitoring for Cassandra started ... ");
		boolean status = JmxMonitoringUtil.copyJmxTransJson(connection,
				cassandraConf.getUsername(), cassandraConf.getPassword(),
				Constant.Component.Name.CASSANDRA,
				Constant.Component.ProcessName.CASSANDRA,
				cassandraConf.getJmxPort(), nodeConf.getPrivateIp());
		if (!status) {
			logger.error(nodeConf,
					"Could not copy JmxTrans JSON file for Cassandra.");
			return false;
		}
		logger.debug("Configuring JMX Monitoring for Cassandra over ... ");
		return true;
	}

	/**
	 * Adds the process info.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param cassandraConf
	 *            the conf
	 * @param connection
	 *            the connection
	 */
	private void addProcessInfo(CassandraNodeConf nodeConf, SSHExec connection) {
		String userHome = CommonUtil.getUserHome(cassandraConf.getUsername());
		String agentFile = userHome + Constant.Agent.AGENT_PROPERTY_FILE_PATH;
		String taskFile = userHome + Constant.Agent.AGENT_TASKABLE_FILE_PATH;

		String lineSeperator = LINE_SEPERATOR;
		try {

			// jps service status monitor class name.
			StringBuffer monitorConfProp = new StringBuffer();
			String cassandraMonitorClassName = "com.impetus.ankush.agent.cassandra.CassandraServiceStatusMonitor";
			monitorConfProp.append(lineSeperator);
			monitorConfProp.append(cassandraMonitorClassName).append(
					lineSeperator);

			StringBuffer agentConfProp = new StringBuffer();
			agentConfProp.append("JMX_PORT=")
					.append(cassandraConf.getJmxPort()).append(lineSeperator);
			agentConfProp.append("SEEDNODE=").append(nodeConf.isSeedNode())
					.append(lineSeperator);

			CustomTask task = new AppendFile(agentConfProp.toString(),
					agentFile);
			connection.exec(task);

			// add task information in taskable conf.
			task = new AppendFile(monitorConfProp.toString(), taskFile);
			connection.exec(task);
		} catch (TaskExecFailException e) {
			logger.error("Error in adding Cassandra agent informations..");
		}
	}

	/**
	 * Gets the bashrc contents.
	 * 
	 * @param cassandraHome
	 *            the cassandra home
	 * @param javaHome
	 *            the java home
	 * @return the bashrc contents
	 */
	private String getBashrcContents() {
		final StringBuilder sb = new StringBuilder();
		sb.append("export CASSANDRA_HOME=")
				.append(cassandraConf.getComponentHome()).append("\n");
		sb.append("export JAVA_HOME=").append(getJavaHome()).append("\n");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		// casting Configuration to CassandraClusterConf
		cassandraConf = (CassandraConf) config;
		logger.setLoggerConfig(cassandraConf);

		final Semaphore semaphore = new Semaphore(cassandraConf.getNodes()
				.size());
		try {
			// undeploying package from each node
			for (final NodeConf nodeConf : cassandraConf.getNodes()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						nodeConf.setStatus(removeNode(nodeConf));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(cassandraConf.getNodes().size());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		// Return true in all cases.
		return true;
	}

	/**
	 * Removes the node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param cassandraConf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean removeNode(NodeConf nodeConf) {
		logger.setLoggerConfig(cassandraConf);
		SSHExec connection = null;

		logger.info(nodeConf.getPublicIp(), "Removing Cassandra...");
		try {
			StringBuilder command = new StringBuilder();

			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					cassandraConf.getUsername(), cassandraConf.getPassword(),
					cassandraConf.getPrivateKey());

			if (connection == null) {
				logger.info(nodeConf.getPublicIp(), "Could not get connection.");
				return false;
			}

			// Remove directories
			command.append("rm -rf ").append(cassandraConf.getDataDir())
					.append(" ").append(cassandraConf.getLogDir()).append(" ")
					.append(cassandraConf.getSavedCachesDir()).append(" ")
					.append(cassandraConf.getCommitlogDir()).append(" ")
					.append(cassandraConf.getInstallationPath());

			execCustomtask(command.toString(), connection,
					nodeConf.getPublicIp(), "Could not remove directories.");

			// Remove path variables
			command = new StringBuilder("sed -i ");
			command.append("-e '/export CASSANDRA_HOME=/d' ")
					.append(" -e'/export JAVA_HOME=/d' ").append(".bashrc");

			execCustomtask(command.toString(), connection,
					nodeConf.getPublicIp(),
					"Could not remove path variables from .bashrc.");
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			// disconncet from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {

		// Casting Configuration to CassandraClusterConf
		cassandraConf = (CassandraConf) config;

		if (startServices(new ArrayList<NodeConf>(cassandraConf.getNodes()),
				config)) {
			checkTechnologyData();
			return true;
		}

		return false;
	}

	/**
	 * Start cluster.
	 * 
	 * @param cassandraConf
	 *            the conf
	 * @param host
	 *            the node conf
	 * @return true, if successful
	 */
	public boolean startNode(String host) {
		logger.setLoggerConfig(cassandraConf);
		// command for starting cassandra
		String startCassandraNode = cassandraConf.getComponentHome()
				+ CASSANDRA_FOLDER_BIN + CASSANDRA_SERVICE_START;

		SSHExec connection = null;

		// connect to node
		try {
			logger.info(host, "Starting cassandra...");
			// connect to remote node
			connection = SSHUtils.connectToNode(host,
					cassandraConf.getUsername(), cassandraConf.getPassword(),
					cassandraConf.getPrivateKey());

			// if connected
			if (connection != null) {
				// running command on each node
				AnkushTask startCassandra = new RunInBackground(
						startCassandraNode);

				if (connection.exec(startCassandra).rc == 0) {
					logger.info(host, "Cassandra Daemon started");
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(host, e.getMessage(), e);
		} finally {
			// disconncet from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		logger.error(host, "Cassandra Daemon startup failed");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		// Casting Configuration to CassandraClusterConf
		cassandraConf = (CassandraConf) config;
		stopServices(new ArrayList<NodeConf>(cassandraConf.getNodes()), config);

		// Return true in all cases
		return true;
	}

	/**
	 * Stop cluster.
	 * 
	 * @param host
	 *            the node conf
	 * @return true, if successful
	 */
	public boolean stopNode(String host) {
		logger.setLoggerConfig(cassandraConf);

		SSHExec connection = null;
		logger.info(host, "Stopping Cassandra...");

		try {
			// connect to remote node
			connection = SSHUtils.connectToNode(host,
					cassandraConf.getUsername(), cassandraConf.getPassword(),
					cassandraConf.getPrivateKey());

			// if connected
			if (connection != null) {
				// Stopping the cassandra process on node
				AnkushTask stopCassandra = new KillJPSProcessByName(
						Constant.Component.ProcessName.CASSANDRA);
				return connection.exec(stopCassandra).rc == 0;
			} else {
				logger.error(host, "Authentication failed");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			// disconncet from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#startServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {

		cassandraConf = (CassandraConf) config;
		logger.setLoggerConfig(cassandraConf);

		if (cassandraConf.getClusterConf().getState()
				.equals(Constant.Cluster.State.ADDING_NODES)) {
			// starting service in each cluster node
			for (final NodeConf nodeConf : nodeList) {
				nodeConf.setStatus(startNode(nodeConf.getPublicIp()));
				// Wait for two minutes
				try {
					logger.info(nodeConf.getPublicIp(),
							"Waiting for two minutes...");
					logger.debug("Wait for two minutes.");
					Thread.sleep(120000);
				} catch (InterruptedException e) {
					logger.debug(e.getMessage());
				}
			}
		} else {
			final Semaphore semaphore = new Semaphore(nodeList.size());
			try {

				// starting service in each cluster node
				for (final NodeConf nodeConf : nodeList) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							nodeConf.setStatus(startNode(nodeConf.getPublicIp()));
							if (semaphore != null) {
								semaphore.release();
							}
						}
					});
				}

				semaphore.acquire(nodeList.size());

			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				return false;
			}
		}

		// Return false if any of the node is not started.
		for (NodeConf nodeConf : nodeList) {
			if (!nodeConf.getStatus()) {
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stopServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {

		// Casting Configuration to CassandraClusterConf
		cassandraConf = (CassandraConf) config;
		logger.setLoggerConfig(cassandraConf);

		// Stop services only if cluster is not in REMOVING_NODES
		if (!cassandraConf.getClusterConf().getState()
				.equals(Constant.Cluster.State.REMOVING_NODES)) {
			final Semaphore semaphore = new Semaphore(nodeList.size());
			try {
				// stopping service on each of the cluster nodes
				for (final NodeConf nodeConf : nodeList) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							nodeConf.setStatus(stopNode(nodeConf.getPublicIp()));
							if (semaphore != null) {
								semaphore.release();
							}
						}
					});
				}
				semaphore.acquire(nodeList.size());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List,
	 * com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		cassandraConf = (CassandraConf) config;
		logger.setLoggerConfig(cassandraConf);

		// Node Deployment process ...
		final Semaphore semaphore = new Semaphore(nodeList.size());
		try {
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						nodeConf.setStatus(createNode((CassandraNodeConf) nodeConf));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());

		} catch (Exception e) {
			logger.error("Error in setting up nodes..", e);
			return false;
		}

		// Return false if any of the node is not deployed.
		for (NodeConf nodeConf : nodeList) {
			if (!nodeConf.getStatus()) {
				return false;
			}
		}

		// update seeds value if seednode is added.
		if (cassandraConf.getClusterConf().getState()
				.equals(Constant.Cluster.State.ADDING_NODES)) {
			for (NodeConf nodeConf : nodeList) {
				CassandraNodeConf cNodeConf = (CassandraNodeConf) nodeConf;
				if (cNodeConf.isSeedNode()) {
					updateSeedNodeValue();
					break;
				}
			}
		}

		return true;
	}

	private void updateSeedNodeValue() {
		// Seeds update command
		String cassandraYaml = cassandraConf.getComponentHome()
				+ CASSANDRA_FOLDER_CONF + CASSANDRA_CONF_YAML;
		final String command = "sed -i -e 's/- seeds: \".*$/- seeds: \""
				+ getSeedNodeString() + "\"/' " + cassandraYaml;
		final Semaphore semaphore = new Semaphore(cassandraConf.getNodes()
				.size());
		try {
			for (final CassandraNodeConf nodeConf : cassandraConf.getNodes()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {

						logger.setLoggerConfig(cassandraConf);
						SSHExec connection = null;

						try {
							// connect to remote node
							connection = SSHUtils.connectToNode(
									nodeConf.getPublicIp(),
									cassandraConf.getUsername(),
									cassandraConf.getPassword(),
									cassandraConf.getPrivateKey());

							if (connection != null) {
								execCustomtask(command, connection,
										nodeConf.getPublicIp(),
										"Could not update seeds value");
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						} finally {
							// Disconnect from node/machine
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
			semaphore.acquire(cassandraConf.getNodes().size());

		} catch (Exception e) {
			logger.error("Error in editing seeds values.", e);
		}
	}

	// Sequencially cleanup all nodes.
	public boolean cleanUp() {
		// command for setting initial token cassandra
		String nodeCleanUp = cassandraConf.getComponentHome()
				+ CASSANDRA_FOLDER_BIN + CASSANDRA_NODETOOL + " "
				+ CASSANDRA_CLEANUP;

		SSHExec connection = null;
		for (CassandraNodeConf nodeConf : cassandraConf.getNodes()) {
			try {
				connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
						cassandraConf.getUsername(),
						cassandraConf.getPassword(),
						cassandraConf.getPrivateKey());
				if (connection == null) {
					logger.error(nodeConf.getPublicIp(),
							"Could not connect the node.");
					nodeConf.setStatus(false);
				} else {
					nodeConf.setStatus(execCustomtask(nodeCleanUp, connection,
							nodeConf.getPublicIp(),
							"Could not cleanup the node."));
				}
			} catch (Exception e) {
				nodeConf.setStatus(false);
				logger.error(nodeConf.getPublicIp(), e.getMessage(), e);
			} finally {
				// Disconnect from node/machine
				if (connection != null) {
					connection.disconnect();
				}
			}
		}

		// Return false if any of the node is not started.
		for (NodeConf nodeConf : cassandraConf.getNodes()) {
			if (!nodeConf.getStatus()) {
				return false;
			}
		}
		return true;
	}

	private Set<String> getActiveNodes() {
		Set<String> activeNodes = new HashSet<String>();
		for (CassandraNodeConf nodeConf : cassandraConf.getNodes()) {
			NodeMonitoring mNode = new MonitoringManager()
					.getMonitoringData(nodeConf.getPublicIp());
			if (mNode != null) {
				Map<String, Boolean> serviceMap = mNode.getServiceStatus();
				if (serviceMap
						.containsKey(Constant.Component.ProcessName.CASSANDRA)
						&& serviceMap
								.get(Constant.Component.ProcessName.CASSANDRA)) {
					activeNodes.add(nodeConf.getPublicIp());
				}
			}
		}
		return activeNodes;
	}

	private String getHostId(String host) {

		CassandraJMXData techData = CassandraJMXData.getTechnologyData(
				cassandraConf, true);
		for (Datacenter dc : techData.getDatacenters()) {
			for (Rack rack : dc.getRacks()) {
				for (com.impetus.ankush.cassandra.Node node : rack.getNodes()) {
					if (node.getHost().equals(host)) {
						return node.getHostId();
					}
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.
	 * List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		cassandraConf = (CassandraConf) config;
		logger.setLoggerConfig(cassandraConf);
		SSHExec connection = null;
		String nodeToolCommand = cassandraConf.getComponentHome()
				+ CASSANDRA_FOLDER_BIN + CASSANDRA_NODETOOL;

		Set<String> activeNodes = getActiveNodes();
		Set<String> activeNodesCopy = new HashSet<String>(activeNodes);

		for (NodeConf nodeConf : nodeList) {
			activeNodes.remove(nodeConf.getPublicIp());
		}

		for (String host : activeNodes) {
			// connect to remote node
			connection = SSHUtils.connectToNode(host,
					cassandraConf.getUsername(), cassandraConf.getPassword(),
					cassandraConf.getPrivateKey());
			if (connection != null) {
				break;
			}
		}

		for (NodeConf nodeConf : nodeList) {
			if (activeNodesCopy.contains(nodeConf.getPublicIp())) {
				// Decommission node
				nodeConf.setStatus(execCustomtask(nodeToolCommand + " -h "
						+ nodeConf.getPublicIp() + " decommission", connection,
						nodeConf.getPublicIp(),
						"Could not decommission the node."));
			} else {
				// Get host ID
				String hostId = getHostId(nodeConf.getPublicIp());
				if (hostId == null) {
					logger.error(nodeConf, "Could not get Host Id.");
					nodeConf.setStatus(false);
					continue;
				}
				// Remove node
				nodeConf.setStatus(execCustomtask(nodeToolCommand
						+ " removenode " + hostId, connection,
						nodeConf.getPublicIp(), "Could not remove the node."));
			}
		}

		final Semaphore semaphore = new Semaphore(nodeList.size());
		try {
			// undeploying package from each node
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						stopNode(nodeConf.getPublicIp());
						nodeConf.setStatus(removeNode(nodeConf));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		// Return true in all cases
		return true;
	}

	private boolean nodeDecommission(String host) {
		SSHExec connection = null;

		// connect to node
		try {
			logger.info(host, "Decommissioning node...");
			// connect to remote node
			connection = SSHUtils.connectToNode(host,
					cassandraConf.getUsername(), cassandraConf.getPassword(),
					cassandraConf.getPrivateKey());

			// if connected
			if (connection != null) {
				StringBuilder command = new StringBuilder();
				command.append(cassandraConf.getComponentHome())
						.append(CASSANDRA_FOLDER_BIN)
						.append(CASSANDRA_NODETOOL).append(" -h ").append(host)
						.append(" decommission");
				return execCustomtask(command.toString(), connection, host,
						"Could not decommission the node.");

			}
		} catch (Exception e) {
			logger.error(host, e.getMessage(), e);
		} finally {
			// disconncet from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

}
