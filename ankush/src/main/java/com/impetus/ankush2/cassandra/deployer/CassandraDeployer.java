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
package com.impetus.ankush2.cassandra.deployer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.DeleteLineFromFile;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush2.agent.AgentConstant;
import com.impetus.ankush2.agent.AgentUtils;
import com.impetus.ankush2.agent.ComponentService;
import com.impetus.ankush2.cassandra.monitor.CassandraJMX;
import com.impetus.ankush2.cassandra.utils.CassandraConstants;
import com.impetus.ankush2.cassandra.utils.CassandraUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;
import com.impetus.ankush2.utils.SSHUtils;

public class CassandraDeployer extends AbstractDeployer {

	/** The clusterConf */
	private ClusterConfig clusterConfig;
	/** The newClusterConf */
	private ClusterConfig newClusterConf = null;
	/** The compConfig */
	private ComponentConfig componentConfig;
	/** The newCompConfig */
	private ComponentConfig newCompConfig = null;
	/** The advanceConf */
	private Map<String, Object> advanceConf;

	private static final String COULD_NOT_GET_CONNECTION = "Could not get connection";

	/** The Constant CASSANDRA_FOLDER_CONF. */
	private static final String CASSANDRA_FOLDER_CONF = "conf/";
	/** The Constant CASSANDRA_FOLDER_BIN. */
	private static final String CASSANDRA_FOLDER_BIN = "bin/";
	/** The Constant LINE_SEPERATOR. */
	private static final String LINE_SEPERATOR = System
			.getProperty("line.separator");
	/** The Cassandra logger */
	private AnkushLogger logger = new AnkushLogger(CassandraDeployer.class);
	/** The conf manager. */
	private static ConfigurationManager confManager = new ConfigurationManager();

	public CassandraDeployer(ClusterConfig clusterConf) {
		this.clusterConfig = clusterConf;
		this.logger = new AnkushLogger(CassandraDeployer.class);
		this.componentConfig = clusterConf.getComponents().get(
				getComponentName());
	}

	public CassandraDeployer() {
		super();
	}

	/**
	 * it initializes clusterConf , compConfig and advanceConf
	 * 
	 * @param clusterConf
	 *            {@link ClusterConfig}
	 * @return <code>true</code>, if successful
	 */
	private boolean setClassVariables(ClusterConfig clusterConf)
			throws AnkushException {
		try {
			if (clusterConf == null
					|| !clusterConf.getComponents().containsKey(
							getComponentName())) {
				throw new AnkushException(
						"Either cluster configuration is not defined or cluster does not contains "
								+ getComponentName() + ".");
			}

			this.clusterConfig = clusterConf;
			this.logger.setCluster(clusterConf);
			this.componentConfig = clusterConf.getComponents().get(
					getComponentName());
			if (this.componentConfig == null
					|| this.componentConfig.getNodes() == null
					|| this.componentConfig.getNodes().isEmpty()) {
				throw new AnkushException(
						"Could not get component configuration.");
			}
			// Initializing advancedConf to use it to for getting and setting
			// cluster level properties
			this.advanceConf = this.componentConfig.getAdvanceConf();
			return true;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not get " + getComponentName()
					+ " configuration details.", e);
		}
	}

	/**
	 * Adds cluster level error to clusterConf's error object and logger
	 * 
	 * @param error
	 *            {@link String}
	 */
	private boolean addClusterError(String error) {
		return addClusterError(error, null, null);
	}

	/**
	 * Adds node level error to clusterConf's error object and logger
	 * 
	 * @param error
	 *            {@link String}
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>
	 */
	private boolean addClusterError(String error, String host) {
		return addClusterError(error, host, null);
	}

	/**
	 * Adds cluster level error and exception to clusterConf's error object and
	 * logger
	 * 
	 * @param error
	 *            {@link String}
	 * @param t
	 *            {@link Throwable}
	 * @return <code>true</code>
	 */
	private boolean addClusterError(String error, Throwable t) {
		return addClusterError(error, null, t);
	}

	/**
	 * Adds errors
	 * 
	 * @param error
	 *            {@link String}
	 * @param host
	 *            {@link String}
	 * @param t
	 *            {@link Throwable}
	 * @return <code>true</code>
	 */
	private boolean addClusterError(String error, String host, Throwable t) {
		if (host != null) {
			this.clusterConfig.addError(host, getComponentName(), error);
			logger.error(error, getComponentName(), host, t);
		} else {
			this.clusterConfig.addError(getComponentName(), error);
			logger.error(error, getComponentName(), t);
		}
		return false;
	}

	@Override
	public boolean validate(ClusterConfig conf) {
		try {
			// TODO: Registration validation pending
			if (componentConfig.isRegister()) {
				return true;
			}
			if (!validate(componentConfig.getNodes().keySet())) {
				throw new AnkushException(getComponentName()
						+ " validation failed.");
			}
			return true;
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError("Could not validate " + getComponentName()
					+ ".", e);
		}
	}

	/**
	 * Perform asynchronous operations nodes.
	 * 
	 * @param nodeList
	 *            {@link Collection}
	 * @return <code>true</code>, if successful
	 */
	private boolean validate(Collection<String> nodeList)
			throws AnkushException {
		try {
			// Create semaphore to join threads
			final Semaphore semaphore = new Semaphore(nodeList.size());
			for (final String host : nodeList) {
				final NodeConfig nodeConfig = clusterConfig.getNodes()
						.get(host);
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						nodeConfig.setStatus(new CassandraValidator(
								clusterConfig, nodeConfig).validate());

						if (semaphore != null) {
							semaphore.release();
						}
					}

				});
			}
			semaphore.acquire(nodeList.size());
		} catch (Exception e) {
			throw new AnkushException(getComponentName()
					+ " validation failed.");
		}
		return AnkushUtils.getStatus(clusterConfig, nodeList);
	}

	@Override
	public boolean deploy(ClusterConfig conf) {
		try {
			// creating a vendor string literal
			String vendor;
			if (componentConfig.getVendor().equalsIgnoreCase("Datastax")) {
				vendor = CassandraConstants.Cassandra_vendors.CASSANDRA_VENDOR_DSC;
			} else if (componentConfig.getVendor().equalsIgnoreCase("Apache")) {
				vendor = CassandraConstants.Cassandra_vendors.CASSANDRA_VENDOR_APACHE;
			} else {
				throw new AnkushException(componentConfig.getVendor()
						+ " vendor not supported for " + getComponentName());
			}
			// Setting component home
			String component_home = componentConfig.getInstallPath() + vendor
					+ "-cassandra-" + componentConfig.getVersion() + "/";
			componentConfig.setHomeDir(component_home);
			advanceConf.put(CassandraConstants.ClusterProperties.CONF_DIR,
					component_home + CASSANDRA_FOLDER_CONF);
			advanceConf.put(CassandraConstants.ClusterProperties.BIN_DIR,
					component_home + CASSANDRA_FOLDER_BIN);
			return (deployNodes(conf, componentConfig.getNodes().keySet()) && start(conf));
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError("Could not deploy " + getComponentName(), e);
		}
	}

	/**
	 * method to deploy Cassandra packages on all nodes
	 * 
	 * @param conf
	 *            {@link ClusterConfig}
	 * @param nodeMap
	 *            {@link Map}
	 * 
	 * @return <code>true</code>, if successful
	 * 
	 */
	private boolean deployNodes(final ClusterConfig conf, Set<String> nodeMap)
			throws AnkushException {

		try {
			// Node Deployment process ...
			final Semaphore semaphore = new Semaphore(nodeMap.size());
			for (final String host : nodeMap) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						conf.getNodes().get(host).setStatus(createNode(host));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeMap.size());
		} catch (Exception e) {
			throw new AnkushException("Could not deploy " + getComponentName(),
					e);
		}
		return AnkushUtils.getStatus(conf.getNodes());
	}

	@Override
	public boolean undeploy(final ClusterConfig conf) {
		try {
			// setting clusterconf, componentconf and logger
			if (!setClassVariables(conf)) {
				return false;
			}
			return removeNode(conf, componentConfig.getNodes().keySet());
		} catch (Exception e) {
			return addClusterError("Could not undeploy " + getComponentName(),
					e);
		}
	}

	@Override
	public boolean unregister(final ClusterConfig conf) {
		try {
			ComponentConfig compConfig = clusterConfig.getComponents().get(
					this.componentName);
			if (!AnkushUtils.isMonitoredByAnkush(compConfig)) {
				logger.info("Skipping " + getComponentName()
						+ " unregistration for Level1", this.componentName);
				return true;
			}

			if (!setClassVariables(conf)) {
				return false;
			}
			final String infoMsg = "Unregistering Cassandra";
			logger.info(infoMsg, getComponentName());
			// Getting node map for cluster deployment
			Map<String, Map<String, Object>> nodeMap = new HashMap<String, Map<String, Object>>(
					componentConfig.getNodes());

			// Node Registration process ...
			final Semaphore semaphore = new Semaphore(nodeMap.size());
			for (final String host : nodeMap.keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						conf.getNodes().get(host)
								.setStatus(unregisterNode(host));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeMap.size());

			// Return false if any of the node is not deployed.
			return AnkushUtils.getStatus(conf.getNodes());
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError(
					"Could not unregister " + getComponentName(), e);
		}
	}

	/**
	 * Removes Cassandra cluster related directories and environment variables
	 * from node
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	private boolean removeNode(String host) {
		try {
			SSHExec connection = getConnection(host);
			if (connection == null) {
				throw new AnkushException(COULD_NOT_GET_CONNECTION
						+ " on node: " + host);
			}
			logger.info("Removing Cassandra...", getComponentName(), host);
			StringBuilder command = new StringBuilder();
			// Remove directories
			command.append("rm -rf ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.DATA_DIR))
					.append(" ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.LOG_DIR))
					.append(" ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.SAVED_CACHES_DIR))
					.append(" ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.COMMIT_LOG_DIR))
					.append(" ").append(componentConfig.getInstallPath());

			if (!execCustomtask(command.toString(), connection, host,
					"Could not remove directories.")) {
				return false;
			}

			// Remove path variables
			command = new StringBuilder("sed -i ");
			command.append("-e '/export CASSANDRA_HOME=/d' ")
					.append(" -e'/export JAVA_HOME=/d' ").append(".bashrc");

			return execCustomtask(command.toString(), connection, host,
					"Could not remove path variables from .bashrc.");
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), host, e);
		} catch (Exception e) {
			return addClusterError(
					"Could not remove Cassandra directories from node.", host,
					e);
		}
	}

	@Override
	public boolean start(ClusterConfig conf) {
		try {
			if (!start(conf, componentConfig.getNodes().keySet())) {
				return false;
			}
			// getting ring topology information during cluster deployment case
			if (conf.getState().equals(Constant.Cluster.State.DEPLOYING)) {
				checkTechnologyData();
			}
			return true;
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError("Could not start " + getComponentName()
					+ " services.", e);
		}
	}

	// @Override
	// public boolean stop(ClusterConfig conf) {
	// if (!setClassVariables(conf)) {
	// return false;
	// }
	// stop(conf, componentConfig.getNodes().keySet());
	// return true;
	// }

	/**
	 * Start cluster.
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	public boolean startNode(String host) {
		try {
			// command for starting cassandra
			String startCassandraNode = advanceConf
					.get(CassandraConstants.ClusterProperties.BIN_DIR)
					+ CassandraConstants.Cassandra_executables.CASSANDRA_DAEMON_START;
			logger.info("Starting cassandra...", getComponentName(), host);
			SSHExec connection = getConnection(host);
			if (connection == null) {
				throw new AnkushException(COULD_NOT_GET_CONNECTION
						+ " on node: " + host);
			}
			// running command on each node
			AnkushTask startCassandra = new RunInBackground(startCassandraNode);

			if (connection.exec(startCassandra).rc != 0) {
				throw new AnkushException("Could not start "
						+ getComponentName() + " on node: " + host);
			}
			logger.info("Cassandra Daemon started", getComponentName(), host);
			return true;
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), host, e);
		} catch (Exception e) {
			return addClusterError("Could not start " + getComponentName()
					+ " on node: " + host, host, e);
		}
	}

	/**
	 * Stop cluster.
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	public boolean stopNode(String host) {
		try {
			SSHExec connection = getConnection(host);
			if (connection == null) {
				throw new AnkushException(COULD_NOT_GET_CONNECTION
						+ " on node: " + host);
			}
			logger.info("Stopping Cassandra...", getComponentName(), host);
			// Stopping the cassandra process on node
			AnkushTask stopCassandra = new KillJPSProcessByName(
					CassandraConstants.CASSANDRA_DAEMON);
			return connection.exec(stopCassandra).rc == 0;
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), host, e);
		} catch (Exception e) {
			return addClusterError("Could not stop Cassandra on " + host, host,
					e);
		}
	}

	/**
	 * Method to get Cassandra technology data from agent
	 * 
	 * @return <code>true</code>, if successful
	 */
	private boolean checkTechnologyData() throws AnkushException {
		try {
			// time interval to check for technology data
			long sleepTime = AppStoreWrapper.getAnkushConfReader()
					.getLongValue("cassandra.topologychecksleeptime");
			// maximum time for getting technology data
			long checkTimeout = AppStoreWrapper.getAnkushConfReader()
					.getLongValue("cassandra.topologychecktimeout");
			int totalTries = (int) (checkTimeout / sleepTime);

			logger.info("Getting Cassandra JMX connection.");
			Integer jmxPort = (Integer) advanceConf
					.get(CassandraConstants.ClusterProperties.JMX_PORT);
			for (int count = 0; count < totalTries; count++) {

				CassandraJMX jmx = null;

				for (String node : componentConfig.getNodes().keySet()) {
					try {
						jmx = new CassandraJMX(node, jmxPort);
						break;
					} catch (Exception e) {
						// TODO: Continue
					}
				}
				if (jmx != null) {
					logger.debug("Created JMX connection.");
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
			logger.info("Could not get Cassandra JMX connection.");
			return false;
		} catch (Exception e) {
			throw new AnkushException("Could not get " + getComponentName()
					+ " technology data.");
		}
	}

	@Override
	public boolean addNode(final ClusterConfig conf, ClusterConfig newConf) {
		try {
			// Set logger
			if (!setClassVariables(conf)) {
				return false;
			}
			this.newClusterConf = newConf;
			this.newCompConfig = newConf.getComponents()
					.get(getComponentName());

			Map<String, Map<String, Object>> nodeMap = this.newCompConfig
					.getNodes();
			if (!deployNodes(newConf, nodeMap.keySet())) {
				return false;
			}
			for (String host : nodeMap.keySet()) {
				if (CassandraUtils.isSeedNode(newClusterConf.getNodes()
						.get(host).getRoles())) {
					updateSeedNodeValue();
					break;
				}
			}
			if (((String) advanceConf
					.get(CassandraConstants.ClusterProperties.SNITCH))
					.equalsIgnoreCase(CassandraConstants.Configuration_Properties.PROPERTY_FILE_SNITCH)) {
				updateTopologyFile();
			}
			return start(newConf, newCompConfig.getNodes().keySet());
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError("Could not add node to "
					+ getComponentName() + " cluster.", e);
		}
	}

	/**
	 * Creates the node.
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	private boolean createNode(String host) {
		try {
			// getting connection object
			SSHExec connection = getConnection(host);
			// If connection is not created , then exiting.
			if (connection == null) {
				throw new AnkushException(COULD_NOT_GET_CONNECTION);
			}

			logger.info("Deploying Cassandra ...", getComponentName(), host);
			// checking Java version as Cassandra 2.x.x needs Java 7
			// support.This validation during deployment is applied for handling
			// HybridCluster case.
			if (!validateJavaVersion(host)) {
				return false;
			}
			// Extracting Cassandra bundle and modifying required configuration
			// files
			if (!installCassandra(host, connection)
					|| !configureCassandra(host, connection)) {
				return false;
			}
			// Adding JMXMonitoring, cassandra-specific details in agent and
			// Cassandra service xml file on host
			return registerNode(host);
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), host, e);
		} catch (Exception e) {
			return addClusterError("Could not create Cassandra node.", host, e);
		}
	}

	/**
	 * Registers the node.
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	private boolean registerNode(String host) {
		try {
			// getting connection
			SSHExec connection = getConnection(host);
			if (connection == null) {
				throw new AnkushException(COULD_NOT_GET_CONNECTION);
			}

			// configuring JMX monitoring for Cassandra
			if (!configureJMXMonitoring(connection, host)) {
				return false;
			}

			// initializing nodes map as per node is prepared during deployment
			// or during addnode
			Map<String, Set<String>> roles;
			if (newClusterConf == null) {
				roles = clusterConfig.getNodes().get(host).getRoles();
			} else {
				roles = newClusterConf.getNodes().get(host).getRoles();
			}

			// service list.
			List<ComponentService> services = new ArrayList<ComponentService>();
			// Add CASSANDRA service in it.
			services.add(new ComponentService(
					CassandraConstants.Cassandra_Services.CassandraDaemon
							.toString(), CassandraUtils.getNodeRole(roles),
					Constant.ServiceType.JPS));

			// Creating cassandra service xml using agent on node.
			if (!AgentUtils.createServiceXML(connection, services,
					getComponentName(), clusterConfig.getAgentHomeDir())) {
				throw new AnkushException(
						"Could not create cassandra service configuration in agent.");
			}

			// adding process list and taskable information in agent conf
			addProcessInfo(host, connection);
			// save audit trails
			confManager
					.saveConfiguration(
							clusterConfig.getClusterId(),
							clusterConfig.getCreatedBy(),
							CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML,
							host, getConfigurationMap(host));
			return true;
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), host, e);
		} catch (Exception e) {
			return addClusterError("Could not register " + getComponentName()
					+ " node.", host, e);
		}
	}

	/**
	 * unregister node
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	private boolean unregisterNode(String host) {
		try {
			// getting connection object
			SSHExec connection = getConnection(host);
			if (connection == null) {
				throw new AnkushException("Unable to get connection on " + host);
			}
			// Removing Cassandra JMX json used for showing node utilization
			// graphs and Cassandra service.xml
			StringBuilder command = new StringBuilder();
			// Remove directories
			command.append("rm -rf ")
					.append(clusterConfig.getAgentHomeDir()
							+ AgentConstant.Relative_Path.SERVICE_CONF_DIR
							+ getComponentName() + "."
							+ Constant.File_Extension.XML)
					.append(" ")
					.append(clusterConfig.getAgentHomeDir()
							+ AgentConstant.Relative_Path.JMXTRANS + "jmxJson_"
							+ Constant.Process.CASSANDRA.toLowerCase()
							+ ".json");

			execCustomtask(command.toString(), connection, host,
					"Could not remove Cassandra.xml and " + "jmxJson_"
							+ Constant.Process.CASSANDRA.toLowerCase()
							+ ".json file.");

			// Delete from taskableconf
			AnkushTask task = new DeleteLineFromFile(
					CassandraConstants.CASSANDRA_MONITORABLE_CLASS_NAME,
					clusterConfig.getAgentHomeDir()
							+ AgentConstant.Relative_Path.TASKABLE_FILE);
			execCustomtask(
					task.getCommand(),
					connection,
					host,
					"Could not remove "
							+ CassandraConstants.CASSANDRA_MONITORABLE_CLASS_NAME
							+ "from" + Constant.Agent.AGENT_TASKABLE_FILE_PATH
							+ "file.");

			task = new DeleteLineFromFile("JMX_PORT=",
					clusterConfig.getAgentHomeDir()
							+ AgentConstant.Relative_Path.AGENT_CONF_FILE);
			execCustomtask(task.getCommand(), connection, host,
					"Could not remove JMX_PORT from"
							+ Constant.Agent.AGENT_PROPERTY_FILE_PATH + "file.");

			task = new DeleteLineFromFile("SEEDNODE=",
					Constant.Agent.AGENT_PROPERTY_FILE_PATH);
			execCustomtask(task.getCommand(), connection, host,
					"Could not remove SEEDNODE from"
							+ Constant.Agent.AGENT_PROPERTY_FILE_PATH + "file.");
			return true;
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), host, e);
		} catch (Exception e) {
			return addClusterError("Could not unregister " + getComponentName()
					+ " node.", host, e);
		}
	}

	/**
	 * Prepares map of configuration object
	 * 
	 * @param host
	 *            {@link String}
	 * @return {@link Map}
	 */
	public Map<String, Object> getConfigurationMap(String host) {
		Map<String, Object> yamlContents = new HashMap<String, Object>();
		yamlContents.put("cluster_name", clusterConfig.getName());
		yamlContents.put("partitioner", advanceConf
				.get(CassandraConstants.ClusterProperties.PARTITIONER));
		yamlContents.put("rpc_port",
				advanceConf.get(CassandraConstants.ClusterProperties.RPC_PORT));
		yamlContents.put("storage_port", advanceConf
				.get(CassandraConstants.ClusterProperties.STORAGE_PORT));
		yamlContents.put("listen_address", host);
		yamlContents.put("rpc_address", host);
		yamlContents.put("endpoint_snitch",
				advanceConf.get(CassandraConstants.ClusterProperties.SNITCH));
		yamlContents.put("saved_caches_directory", advanceConf
				.get(CassandraConstants.ClusterProperties.SAVED_CACHES_DIR));
		yamlContents.put("commitlog_directory", advanceConf
				.get(CassandraConstants.ClusterProperties.COMMIT_LOG_DIR));
		return yamlContents;
	}

	/**
	 * Adds Cassandra jmx monitoring json to node
	 * 
	 * @param connection
	 *            {@link SSHExec}
	 * @param host
	 *            {@link String}
	 */
	private boolean configureJMXMonitoring(SSHExec connection, String host)
			throws AnkushException {
		try {
			logger.info(
					"Configuring JMX Monitoring for Cassandra started ... ",
					getComponentName(), host);
			// if copying of cassandra jmx trans json to node fails, then
			// exiting with return status false
			if (!JmxMonitoringUtil
					.copyJmxTransJson(
							connection,
							clusterConfig.getAuthConf(),
							host,
							Constant.Process.CASSANDRA,
							(Integer) advanceConf
									.get(CassandraConstants.ClusterProperties.JMX_PORT),
							clusterConfig.getAgentHomeDir())) {
				throw new AnkushException(
						"Could not copy JmxTrans JSON file for Cassandra.");
			}
			logger.info("Configuring JMX Monitoring for Cassandra over ... ",
					getComponentName(), host);
			return true;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not configure JMX monitoring ");
		}
	}

	/**
	 * Adds the process info.
	 * 
	 * @param host
	 *            {@link String}
	 * @param connection
	 *            {@link SSHExec}
	 */
	private void addProcessInfo(String host, SSHExec connection)
			throws AnkushException {
		try {
			Map<String, Set<String>> roles;
			if (newClusterConf == null) {
				roles = clusterConfig.getNodes().get(host).getRoles();
			} else {
				roles = newClusterConf.getNodes().get(host).getRoles();
			}
			if (CassandraUtils.isSeedNode(roles)) {
				String userHome = CommonUtil.getUserHome(clusterConfig
						.getAuthConf().getUsername());
				String agentFile = clusterConfig.getAgentHomeDir()
						+ AgentConstant.Relative_Path.AGENT_CONF_FILE;
				String taskFile = clusterConfig.getAgentHomeDir()
						+ AgentConstant.Relative_Path.TASKABLE_FILE;

				String lineSeperator = LINE_SEPERATOR;
				// jps service status monitor class name.
				StringBuffer monitorConfProp = new StringBuffer();
				String cassandraMonitorClassName = CassandraConstants.CASSANDRA_MONITORABLE_CLASS_NAME;
				monitorConfProp.append(lineSeperator);
				monitorConfProp.append(cassandraMonitorClassName).append(
						lineSeperator);

				StringBuffer agentConfProp = new StringBuffer();
				// TODO: Either agent-based monitoring is to be removed , or
				// JMX_PORT is to be replaced by CASSANDRA_JMX_PORT
				agentConfProp
						.append("JMX_PORT=")
						.append(advanceConf
								.get(CassandraConstants.ClusterProperties.JMX_PORT))
						.append(lineSeperator);
				agentConfProp.append("SEEDNODE=true").append(lineSeperator);

				CustomTask task = new AppendFileUsingEcho(
						agentConfProp.toString(), agentFile);
				connection.exec(task);

				// add task information in taskable conf.
				task = new AppendFileUsingEcho(monitorConfProp.toString(),
						taskFile);
				connection.exec(task);
			}
		} catch (TaskExecFailException e) {
			throw new AnkushException("Could not execute task for adding "
					+ getComponentName() + " related information to Agent.");
		} catch (Exception e) {
			throw new AnkushException("Execption occurs while adding "
					+ getComponentName() + " process info to Agent.");
		}
	}

	/**
	 * method to validate java version compatibility with Cassandra version
	 * 
	 * @param host
	 *            {@link String}
	 * 
	 * @return true , if successful
	 */
	private boolean validateJavaVersion(String host) throws AnkushException {
		try {
			// Checking whether the major revision number of the Cassandra
			// package version is 2
			if (componentConfig.getVersion().split("\\.")[0].equals("2")) {
				logger.info("Validating Java version for Cassandra 2.x.",
						getComponentName(), host);
				// If Java is already installed, then checking its version
				if (clusterConfig.getJavaConf().isRegister()) {
					String username = clusterConfig.getAuthConf().getUsername();
					String password = clusterConfig.getAuthConf()
							.isUsingPassword() ? clusterConfig.getAuthConf()
							.getPassword() : clusterConfig.getAuthConf()
							.getPrivateKey();
					// Getting Java version
					String javaVersion = SSHUtils.getJavaVersion(host,
							username, password, clusterConfig.getAuthConf()
									.isUsingPassword());
					// Checking whether the minor revision number of Java
					// version is 7 as Cassandra 2.x.x requires Java 1.7.x
					if (!javaVersion.split("\\.")[1].equals("7")) {
						throw new AnkushException(
								"Java version should be 1.7.x for Cassandra "
										+ componentConfig.getVersion() + " ...");
					}
				}
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"Exception while validating Java version", e);
		}
		return true;
	}

	/**
	 * method to create installation directory and extarcting Cassandra bundle
	 * 
	 * @param host
	 *            {@link String}
	 * @param connection
	 *            {@link SSHExec}
	 * @return <code>true</code> , if successful
	 */
	private boolean installCassandra(String host, SSHExec connection)
			throws AnkushException {
		logger.info("Creating log directory ...", getComponentName(), host);
		try {
			// Create installation and log directory
			if (!execCustomtask(
					"mkdir -p "
							+ componentConfig.getInstallPath()
							+ " "
							+ advanceConf
									.get(CassandraConstants.ClusterProperties.LOG_DIR),
					connection, host,
					"Could not create " + componentConfig.getInstallPath()
							+ "directory.")) {
				return false;
			}
			logger.info("Extracting bundle", getComponentName(), host);
			// Extracting Cassandra Bundle
			if (!SSHUtils.getAndExtractComponent(connection, componentConfig,
					this.getComponentName())) {
				throw new AnkushException("Could not get/extract tarball.");
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not install Cassandra.", e);
		}
		return true;
	}

	/**
	 * method to update environment variables, Cassandra yaml , topology and
	 * logger configuration
	 * 
	 * @param host
	 *            {@link String}
	 * @param connection
	 *            {@link SSHExec}
	 * @return <code>true</code> , if successful
	 */
	private boolean configureCassandra(String host, SSHExec connection)
			throws AnkushException {
		try {
			logger.info("Configuring Cassandra ...", getComponentName(), host);
			// Updating environment variables , Cassandra configuration files
			// and topology file
			if (!updatePaths(host, connection)
					|| !editCassandraConfigFile(host, connection)
					|| !editTopologyFile(connection, host)) {
				return false;
			}

			StringBuilder command = new StringBuilder();
			String cassandralogFile = null;

			// Applying version check to handle logging details
			// TODO: Proper version check
			if (componentConfig.getVersion().contains("2.1.")) {
				// set log directory in log4j-server.properties file
				cassandralogFile = advanceConf
						.get(CassandraConstants.ClusterProperties.CONF_DIR)
						+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_LOGBACK_XML;
				command.append("sed -i 's/${cassandra.logdir}/")
						.append(FileNameUtils
								.convertToValidPath(
										(String) advanceConf
												.get(CassandraConstants.ClusterProperties.LOG_DIR))
								.replace("/", "\\/")).append("/g' ")
						.append(cassandralogFile);
			} else {
				// set log directory in log4j-server.properties file
				cassandralogFile = advanceConf
						.get(CassandraConstants.ClusterProperties.CONF_DIR)
						+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_LOG4J_SERVER_PROPERTIES;
				command.append(
						"sed -i '/log4j.appender.R.File/c\\log4j.appender.R.File=")
						.append(FileNameUtils.convertToValidPath((String) advanceConf
								.get(CassandraConstants.ClusterProperties.LOG_DIR)))
						.append("system.log").append("' ")
						.append(cassandralogFile);
			}
			logger.info("Updating " + cassandralogFile + " file.",
					getComponentName(), host);
			if (execCustomtask(command.toString(), connection, host,
					"Unable to set log directory  in " + cassandralogFile
							+ " file")) {
				return true;
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not configure "
					+ getComponentName() + ".", e);
		}
		return false;
	}

	/**
	 * function to add java home and cassandra home to environment variables
	 * 
	 * @param host
	 *            {@link String}
	 * @param connection
	 *            {@link SSHExec}
	 * @return <code>true</code> , if successful
	 */
	private boolean updatePaths(String host, SSHExec connection)
			throws AnkushException {
		try {
			// Adding CASSANDRA_HOME and JAVA_HOME in $HOME/.bashrc file and
			// JAVA_HOME in cassandra-env.sh file
			String cassandraEnvFile = advanceConf
					.get(CassandraConstants.ClusterProperties.CONF_DIR)
					+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_ENV_SH;

			// editing $HOME/.bashrc and cassandra environment file
			return (execCustomtask("echo \"" + getBashrcContents()
					+ "\" >> .bashrc", connection, host,
					"Could not edit .bashrc.") && execCustomtask(
					"echo \"export JAVA_HOME=" + getJavaHome() + "\" >> "
							+ cassandraEnvFile,
					connection,
					host,
					"Could not edit "
							+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_ENV_SH
							+ "."));
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"Could not update environment variables path for "
							+ getComponentName());
		}
	}

	/**
	 * Gets the bashrc contents.
	 * 
	 * @return {@link String}
	 */
	private String getBashrcContents() {
		// Preparing contents to be added in $HOME/.bashrc file
		final StringBuilder sb = new StringBuilder();
		sb.append("export CASSANDRA_HOME=")
				.append(componentConfig.getHomeDir()).append("\n");
		sb.append("export JAVA_HOME=").append(getJavaHome()).append("\n");
		return sb.toString();
	}

	/**
	 * Method to get java home path from cluster conf.
	 * 
	 * @return {@link String}
	 */
	private String getJavaHome() {
		return clusterConfig.getJavaConf().getHomeDir();
	}

	/**
	 * Editing cassandra yaml configuration file
	 * 
	 * @param host
	 *            {@link String}
	 * @param connection
	 *            {@link SSHExec}
	 * @return <code>true</code> , if successful
	 */
	private boolean editCassandraConfigFile(String host, SSHExec connection)
			throws AnkushException {
		try {
			// Set auto_bootstap to true on adding new nodes.
			boolean autoBootStrap = clusterConfig.getState().equals(
					Constant.Cluster.State.ADD_NODE);

			// Getting cassandra.yaml file location
			String cassandraYaml = advanceConf
					.get(CassandraConstants.ClusterProperties.CONF_DIR)
					+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML;
			StringBuilder command = new StringBuilder();

			int vNodeCount;
			if (newCompConfig != null) {
				vNodeCount = (Integer) newCompConfig.getNodes().get(host)
						.get(CassandraConstants.NodeProperties.V_NODE_COUNT);
			} else {
				vNodeCount = (Integer) componentConfig.getNodes().get(host)
						.get(CassandraConstants.NodeProperties.V_NODE_COUNT);
			}
			// Configuring cluster_name, num_tokens, partitioner, rpc_port,
			// listen_address, storage_port, rpc_address, data_directory,
			// saved_cache directory, commilog_dir, seeds info, endpoint_snitch
			// and
			// auto_bootstrap property in cassandra.yaml file
			command.append("sed -i -e \"/^cluster_name:/c\\cluster_name: '")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.CLUSTER_NAME))
					.append("'\" ");
			command.append("-e '/^initial_token:/c#initial_token:' ");
			command.append("-e '/num_tokens:/cnum_tokens: ").append(vNodeCount)
					.append("' ");
			command.append("-e '/^partitioner:/c\\partitioner: ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.PARTITIONER))
					.append("' ");
			command.append("-e '/^rpc_port:/c\\rpc_port: ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.RPC_PORT))
					.append("' ");
			command.append("-e '/^storage_port:/c\\storage_port: ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.STORAGE_PORT))
					.append("' ");
			command.append("-e '/^listen_address:/clisten_address: ")
					.append(host).append("' ");
			command.append("-e '/^rpc_address:/c\\rpc_address: ").append(host)
					.append("' ");
			command.append("-e 's_/var/lib/cassandra/data_")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.DATA_DIR))
					.append("_g' ");
			command.append(
					"-e '/saved_caches_directory:/c\\saved_caches_directory: ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.SAVED_CACHES_DIR))
					.append("' ");
			command.append("-e '/commitlog_directory:/c\\commitlog_directory: ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.COMMIT_LOG_DIR))
					.append("' ");
			command.append("-e 's/- seeds: \"127.0.0.1\"/- seeds: \"")
					.append(getSeedNodeString()).append("\"/' ");
			command.append("-e '/^endpoint_snitch:/cendpoint_snitch: ")
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.SNITCH))
					.append("' ");
			command.append("-e '$ a\\auto_bootstrap: ").append(autoBootStrap)
					.append("' ").append(cassandraYaml);
			if (!execCustomtask(
					command.toString(),
					connection,
					host,
					"Unable to edit "
							+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML
							+ ".")) {
				return false;
			}
			return true;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not update " + getComponentName()
					+ " configuration files.");
		}
	}

	/**
	 * adding Cassandra nodes topology information
	 * 
	 * @param connection
	 *            {@link SSHExec}
	 * @param host
	 *            {@link String}
	 * @return <code>true</code> , if successful
	 */
	private boolean editTopologyFile(SSHExec connection, String host)
			throws AnkushException {
		try {
			// Getting snitch name
			String snitch = (String) advanceConf
					.get(CassandraConstants.ClusterProperties.SNITCH);

			// if snitch is SimpleSnitch or RackInferringSnitch, exiting
			// successfully with true return value
			if (snitch
					.equalsIgnoreCase(CassandraConstants.Configuration_Properties.SIMPLE_SNITCH)
					|| snitch
							.equalsIgnoreCase(CassandraConstants.Configuration_Properties.RACK_INFERRING_SNITCH)) {
				return true;
			}

			// Topology file to update
			String topologyFile = new String();
			// File content to place in topology file
			String fileContent = new String();
			// Checking whether snitch is PropertyFileSnitch or
			// GossipingPropertyFile Snitch and updating the corresponding
			// configuration file
			if (snitch
					.equalsIgnoreCase(CassandraConstants.Configuration_Properties.PROPERTY_FILE_SNITCH)) {
				// Getting Topology file name
				topologyFile = advanceConf
						.get(CassandraConstants.ClusterProperties.CONF_DIR)
						+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_TOPOLOGY_PROPERTIES;
				// Getting file content
				fileContent = getTopologyString();
			} else if (snitch
					.equalsIgnoreCase(CassandraConstants.Configuration_Properties.GOSSIPING_PROPERTY_FILE_SNITCH)) {
				// Getting Topology file name
				topologyFile = advanceConf
						.get(CassandraConstants.ClusterProperties.CONF_DIR)
						+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_RACK_DC_PROPERTIES;
				// Getting Topology file name
				fileContent = getDCRackFileString(host);
			}
			logger.info("Update topology file " + topologyFile,
					getComponentName(), host);
			return execCustomtask("echo \"" + fileContent + "\" > "
					+ topologyFile, connection, host,
					"Unable to update topology file.");
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not edit " + getComponentName()
					+ " topology configuration file.");
		}
	}

	/**
	 * Prepares Topology information of nodes in cluster when snitch used is
	 * PropertyFileSnitch
	 * 
	 * @return {@link String}
	 */
	private String getTopologyString() throws AnkushException {
		try {
			StringBuilder topology = new StringBuilder();
			// topology default Datacenter & Rack configuration
			topology.append("default=DC1:RAC1").append("\n");

			for (Map.Entry<String, NodeConfig> nodes : clusterConfig.getNodes()
					.entrySet()) {
				if (returnNodes().keySet().contains(nodes.getKey())) {
					if (nodes.getValue().getDatacenter() != null
							&& nodes.getValue().getRack() != null
							|| !nodes.getValue().getDatacenter().isEmpty()
							|| !nodes.getValue().getRack().isEmpty()) {
						topology.append(nodes.getKey()).append("=")
								.append(nodes.getValue().getDatacenter())
								.append(":").append(nodes.getValue().getRack())
								.append("\n");
					}

				}
			}
			return topology.toString();
		} catch (Exception e) {
			throw new AnkushException("Could not get topology string.");
		}
	}

	/**
	 * Prepares node wise toplogy string when snitch used is
	 * GossipingPropertyFileSnitch
	 * 
	 * @param host
	 *            {@link String}
	 * @return {@link String}
	 * 
	 */
	private String getDCRackFileString(String host) throws AnkushException {
		try {
			StringBuilder dcRack = new StringBuilder();

			NodeConfig nc = clusterConfig.getNodes().get(host);

			if (nc.getDatacenter() != null && !nc.getDatacenter().isEmpty()) {
				dcRack.append("dc=").append(nc.getDatacenter()).append("\n");
			} else {
				dcRack.append("dc=DC1\n");
			}

			if (nc.getRack() != null && !nc.getRack().isEmpty()) {
				dcRack.append("rack=").append(nc.getRack()).append("\n");
			} else {
				dcRack.append("rack=RAC1\n");
			}
			return dcRack.toString();
		} catch (Exception e) {
			throw new AnkushException("Could not get topology string.");
		}
	}

	/**
	 * Function to update seed node information after add or remove nodes on all
	 * nodes in a cluster
	 */
	private void updateSeedNodeValue() {
		// Seeds update command
		String cassandraYaml = advanceConf
				.get(CassandraConstants.ClusterProperties.CONF_DIR)
				+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML;
		final String command = "sed -i -e 's/- seeds: \".*$/- seeds: \""
				+ getSeedNodeString() + "\"/' " + cassandraYaml;

		Map<String, Map<String, Object>> nodes = new HashMap<String, Map<String, Object>>(
				componentConfig.getNodes());
		if (clusterConfig.getState().equals(Constant.Cluster.State.REMOVE_NODE)) {
			nodes = new HashMap<String, Map<String, Object>>(returnNodes());
		}

		final Semaphore semaphore = new Semaphore(nodes.size());
		try {
			for (final String host : nodes.keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							SSHExec connection = getConnection(host);
							if (connection != null) {
								execCustomtask(command, connection, host,
										"Could not update seeds value");
							}
						} catch (AnkushException e) {
							addClusterError(e.getMessage(), host, e);
						} catch (Exception e) {
							addClusterError(
									"Could not update seed node value in cassandra.yaml file.",
									host, e);
						}
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodes.size());

		} catch (Exception e) {
			logger.error("Error in editing seeds values.", e);
		}
	}

	/**
	 * Function to update topology information on all nodes in cluster after add
	 * or remove nodes
	 */
	private void updateTopologyFile() {
		Map<String, Map<String, Object>> nodes = new HashMap<String, Map<String, Object>>(
				componentConfig.getNodes());
		if (clusterConfig.getState().equals(Constant.Cluster.State.REMOVE_NODE)) {
			nodes = new HashMap<String, Map<String, Object>>(returnNodes());
		}

		final Semaphore semaphore = new Semaphore(nodes.size());
		try {
			for (final String host : nodes.keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							SSHExec connection = getConnection(host);
							if (connection != null) {
								editTopologyFile(connection, host);
							}
						} catch (AnkushException e) {
							addClusterError(e.getMessage(), host, e);
						} catch (Exception e) {
							addClusterError(
									"Could not update topology details.", host,
									e);
						}

						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodes.size());

		} catch (Exception e) {
			logger.error("Error in updating topology file.", e);
		}
	}

	/**
	 * Prepares seednodes String to be updated in cassandra.yaml configuration
	 * file
	 * 
	 * @return {@link String}
	 */
	private String getSeedNodeString() {
		Set<String> seedNodes = new HashSet<String>();
		Map<String, Set<String>> roles = new HashMap<String, Set<String>>();
		for (Map.Entry<String, Map<String, Object>> node : returnNodes()
				.entrySet()) {

			if (clusterConfig.getNodes().containsKey(node.getKey())) {
				roles = clusterConfig.getNodes().get(node.getKey()).getRoles();
			} else {
				roles = newClusterConf.getNodes().get(node.getKey()).getRoles();
			}

			if (roles.containsKey(Constant.Component.Name.CASSANDRA)
					&& roles.get(Constant.Component.Name.CASSANDRA).contains(
							CassandraConstants.Node_Type.CASSANDRA_SEED)) {

				seedNodes.add(node.getKey());
			}
		}
		return StringUtils.join(seedNodes, ",");
	}

	/**
	 * This function handles the node map to use to prepare seed node string and
	 * topology file content
	 * 
	 * @return {@link Map}
	 **/
	private Map<String, Map<String, Object>> returnNodes() {
		// Getting all component nodes during deployment case
		Map<String, Map<String, Object>> nodes = new HashMap<String, Map<String, Object>>(
				componentConfig.getNodes());
		// Modifying node map during add or remove nodes case
		if (clusterConfig.getState().equals(Constant.Cluster.State.ADD_NODE)) {
			// Adding nodes to be added to node map
			nodes.putAll(newCompConfig.getNodes());
		} else if (clusterConfig.getState().equals(
				Constant.Cluster.State.REMOVE_NODE)) {
			// Removing nodes to be deleted from node map
			for (String nc : newCompConfig.getNodes().keySet()) {
				nodes.remove(nc);
			}
		}
		return nodes;
	}

	/**
	 * function to execute Custom Tasks and setting error if it fails
	 * 
	 * @return <code>true</code>, if successful
	 */
	private boolean execCustomtask(String command, SSHExec connection,
			String publicIp, String errMsg) throws AnkushException {
		try {
			CustomTask task = new ExecCommand(command);
			if (connection.exec(task).rc == 0) {
				return true;
			}
			throw new AnkushException(errMsg);
		} catch (AnkushException e) {
			throw e;
		} catch (TaskExecFailException e) {
			throw new AnkushException("Could not execute task : " + command, e);
		} catch (Exception e) {
			throw new AnkushException("Exception while executing command '"
					+ command + "' on " + publicIp, e);
		}
	}

	@Override
	public boolean createConfig(ClusterConfig conf) {
		try {
			// Setting logger and cluster conf
			setClassVariables(conf);

			// Need to verify registration case code to consider node roles in
			// NodeConfig object
			if (componentConfig.isRegister()) {
				if (!(new CassandraRegister(clusterConfig).createConfig())) {
					throw new AnkushException("Could not create "
							+ getComponentName()
							+ " configuration for cluster registration");
				}
			} else {
				// Adding clusterName key to advanceConf map to use it in
				// configuring cluster_name in cassandra.yaml
				advanceConf.put(
						CassandraConstants.ClusterProperties.CLUSTER_NAME,
						clusterConfig.getName());
				// Adding a set of seed nodes to Cassandra advanceConf
				advanceConf.put(
						CassandraConstants.ClusterProperties.SEED_NODE_SET,
						CassandraUtils.getSeedNodeSet(clusterConfig,
								componentConfig));
			}
			return true;
		} catch (AnkushException e) {
			addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			addClusterError("Could not create " + getComponentName()
					+ " configuration.", e);
		}
		return false;
	}

	// /**
	// * Setting roles in clustercConf node's roles set. This is to be used
	// during
	// * registration case.
	// */
	// private boolean setRole() {
	// Map<String, Set<String>> roles;
	// for (String compNode : compConfig.getNodes().keySet()) {
	// roles = clusterConf.getNodes().get(compNode).getRoles();
	// if (!roles.containsKey(Constant.Component.Name.CASSANDRA)) {
	// roles.put(Constant.Component.Name.CASSANDRA,
	// new LinkedHashSet<String>());
	// }
	// roles.get(Constant.Component.Name.CASSANDRA).add(
	// CassandraUtils.getNodeRole(roles));
	// }
	// return true;
	// }

	@Override
	public boolean register(final ClusterConfig conf) {

		try {
			ComponentConfig compConfig = clusterConfig.getComponents().get(
					this.componentName);
			if (!AnkushUtils.isMonitoredByAnkush(compConfig)) {
				logger.info("Skipping " + getComponentName()
						+ " registration for Level1", this.componentName);
				return true;
			}
			final String infoMsg = "Registering Cassandra";
			logger.info(infoMsg);

			// Getting node map for cluster deployment
			Map<String, Map<String, Object>> nodeMap = new HashMap<String, Map<String, Object>>(
					componentConfig.getNodes());

			// Node Registration process ...
			final Semaphore semaphore = new Semaphore(nodeMap.size());
			for (final String host : nodeMap.keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						conf.getNodes().get(host).setStatus(registerNode(host));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeMap.size());
			// Return false if any of the node is not deployed.
			return AnkushUtils.getStatus(conf.getNodes());
		} catch (Exception e) {
			return addClusterError("Could not register " + getComponentName(),
					e);
		}
	}

	@Override
	public boolean start(final ClusterConfig conf, Collection<String> nodes) {

		try {
			// Causing the thread to sleep for two minutes during add nodes case
			// to
			// update ring topology details
			if (this.clusterConfig.getState().equals(
					Constant.Cluster.State.ADD_NODE)) {
				// starting service on all newly added nodes
				for (final String host : nodes) {
					// setting cluster conf nodes status
					conf.getNodes().get(host).setStatus(startNode(host));
					// Wait for two minutes
					try {
						logger.info("Waiting for two minutes...",
								getComponentName(), host);
						logger.debug("Wait for two minutes.", host);
						Thread.sleep(120000);
					} catch (InterruptedException e) {
						logger.debug(e.getMessage());
					}
				}
			} else {
				final Semaphore semaphore = new Semaphore(nodes.size());
				// starting service on each node in cluster
				for (final String host : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							// setting cluster conf nodes status
							conf.getNodes().get(host)
									.setStatus(startNode(host));
							if (semaphore != null) {
								semaphore.release();
							}
						}
					});
				}

				semaphore.acquire(nodes.size());

			}
			// Return false if any of the node is not deployed.
			return AnkushUtils.getStatus(conf.getNodes());
		} catch (Exception e) {
			return addClusterError("Could not start " + getComponentName()
					+ " services.", e);
		}
	}

	@Override
	public boolean stop(final ClusterConfig conf, Collection<String> nodes) {
		// Stop services only if cluster is not in REMOVING_NODES
		if (!conf.getState().equals(Constant.Cluster.State.REMOVE_NODE)) {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// stopping service on each of the cluster nodes
				for (final String host : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							conf.getNodes().get(host).setStatus(stopNode(host));
							if (semaphore != null) {
								semaphore.release();
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return true;
	}

	@Override
	public boolean removeNode(final ClusterConfig conf, Collection<String> nodes) {
		logger.info("Deleting Cassandra packages...", getComponentName());
		try {
			if (newClusterConf == null) {
				// setting clusterconf, componentconf and logger
				if (!setClassVariables(conf)) {
					return false;
				}
			}
			final Semaphore semaphore = new Semaphore(nodes.size());
			// undeploying package from each node
			for (final String host : nodes) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						// setting nodestatus default value to false
						boolean nodestatus = false;
						// if service stopped successfully, then removing
						// component from node
						if (stopNode(host)) {
							nodestatus = removeNode(host);
						}
						conf.getNodes().get(host).setStatus(nodestatus);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodes.size());
			return AnkushUtils.getStatus(conf.getNodes());
		} catch (Exception e) {
			return addClusterError("Could not remove " + getComponentName(), e);
		}
	}

	public boolean removeNode(ClusterConfig conf, ClusterConfig newConf) {
		try {
			// setting clusterconf, componentconf and logger
			if (!setClassVariables(conf)) {
				return false;
			}
			this.newClusterConf = newConf;
			this.newCompConfig = newConf.getComponents()
					.get(getComponentName());
			return removeNode(newConf, newCompConfig.getNodes().keySet());
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError("Could not undeploy " + getComponentName(),
					e);
		}
	}

	/**
	 * Provides node's connection object
	 * 
	 * @param host
	 *            {@link String}
	 * @return {@link SSHExec}
	 */
	private SSHExec getConnection(String host) throws AnkushException {
		try {
			if (clusterConfig.getNodes().containsKey(host)) {
				return clusterConfig.getNodes().get(host).getConnection();
			}
			return newClusterConf.getNodes().get(host).getConnection();
		} catch (Exception e) {
			throw new AnkushException(COULD_NOT_GET_CONNECTION, e);
		}
	}

	@Override
	public boolean canNodeBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes) {
		return true;
	}

}
