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
package com.impetus.ankush2.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush2.agent.AgentUtils;
import com.impetus.ankush2.agent.ComponentService;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.constant.Constant.Component;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.SSHUtils;

/**
 * The Class ZookeeperWorker.
 */
public class ZookeeperWorker {

	/** The logger. */
	private final AnkushLogger logger = new AnkushLogger(ZookeeperWorker.class);

	/** The cluster config. */
	private ClusterConfig clusterConfig;

	/** The node config. */
	private NodeConfig nodeConfig;

	/** The conf. */
	private ComponentConfig conf;

	/** The node advance conf. */
	private Map<String, Object> nodeAdvanceConf;

	/** The component name. */
	private String componentName;

	/**
	 * Sets the cluster and logger.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param componentName
	 *            the component name
	 * @param nodeConfig
	 *            the node config
	 */
	private void setClusterAndLogger(ClusterConfig clusterConfig,
			String componentName, NodeConfig nodeConfig) {
		this.clusterConfig = clusterConfig;
		this.conf = clusterConfig.getComponents().get(componentName);
		this.componentName = componentName;
		this.nodeConfig = nodeConfig;
		logger.setCluster(clusterConfig);
	}

	/**
	 * Instantiates a new zookeeper worker.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param componentName
	 *            the component name
	 * @param nodeConfig
	 *            the node config
	 * @param nodeAdvanceConf
	 *            the node advance conf
	 */
	public ZookeeperWorker(ClusterConfig clusterConf, String componentName,
			NodeConfig nodeConfig, Map<String, Object> nodeAdvanceConf) {
		this.setClusterAndLogger(clusterConf, componentName, nodeConfig);
		this.nodeAdvanceConf = nodeAdvanceConf;
	}

	/**
	 * Instantiates a new zookeeper worker.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param componentName
	 *            the component name
	 * @param nodeConfig
	 *            the node config
	 */
	public ZookeeperWorker(ClusterConfig clusterConf, String componentName,
			NodeConfig nodeConfig) {
		this.setClusterAndLogger(clusterConf, componentName, nodeConfig);
	}

	/**
	 * Creates the node.
	 * 
	 * @return true, if successful
	 */
	public boolean createNode() {
		logger.info("Starting Zookeeper Worker thread..", nodeConfig.getHost(),
				this.componentName);

		SSHExec connection = null;
		Result res = null;
		try {
			// connect to remote node
			connection = this.nodeConfig.getConnection();

			// if connected
			if (connection != null) {
				logger.info("Create directory - " + this.conf.getInstallPath(),
						this.componentName, this.nodeConfig.getPublicHost());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						this.conf.getInstallPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					// logger.error("Could not create installation directory",
					// this.componentName, this.nodeConfig.getPublicHost());
					// return false;
					throw new AnkushException(
							"Could not create installation directory");
				}
				logger.info("Get and extract tarball", this.componentName,
						this.nodeConfig.getPublicHost());
				// get and extract tarball
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, this.conf,
						Constant.Component.Name.ZOOKEEPER);
				if (!isSuccessfull) {
					throw new AnkushException(
							"Could not extract bundle for Zookeeper");
				}
				// make data directory
				AnkushTask makeDataDir = new MakeDirectory((String) this.conf
						.getAdvanceConf().get(
								ZookeeperConstant.Keys.DATA_DIRECTORY));

				// create zoo.cfg file
				AnkushTask createZooCfg = new AppendFileUsingEcho(
						getZooConfContents(), this.conf.getHomeDir()
								+ "/conf/zoo.cfg");
				// create myid file
				AnkushTask createMyId = new AppendFileUsingEcho(
						String.valueOf(this.nodeAdvanceConf
								.get(ZookeeperConstant.Keys.NODE_ID)),
						this.conf.getAdvanceConf().get(
								ZookeeperConstant.Keys.DATA_DIRECTORY)
								+ "myid");

				logger.info("Creating zookeeper's data directory...",
						this.componentName, this.nodeConfig.getPublicHost());
				if (!connection.exec(makeDataDir).isSuccess) {
					throw new AnkushException(
							"Couldn't create zookeeper's data directory");
				}
				logger.info("Creating zoo.cfg file...", this.componentName,
						this.nodeConfig.getPublicHost());
				if (!connection.exec(createZooCfg).isSuccess) {
					throw new AnkushException("Couldn't create zoo.cfg file");
				}
				logger.info("Creating myid file...", this.componentName,
						this.nodeConfig.getPublicHost());
				if (!connection.exec(createMyId).isSuccess) {
					throw new AnkushException("Couldn't create myid file");
				}
				logger.info("Configuring JMX monitoring for Zookeeper... ",
						this.componentName, this.nodeConfig.getPublicHost());
				if (!configureJMXMonitoring()) {
					throw new AnkushException(
							"Couldn't configure JMX monitoring for Zookeeper.");
				}
				if (!configure()) {
					throw new AnkushException("Couldn't configure Zookeeper.");
				}
				logger.info("Zookeeper worker thread execution over ... ",
						this.componentName, this.nodeConfig.getPublicHost());
				return true;
			} else {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
		}
		return false;
	}

	/**
	 * Gets the zoo conf contents.
	 * 
	 * @return the zoo conf contents
	 */
	public String getZooConfContents() {

		// Building first part of zoo.cfg file
		StringBuilder partOneBuilder = new StringBuilder();
		String dataDir = (String) this.conf.getAdvanceConf().get(
				ZookeeperConstant.Keys.DATA_DIRECTORY);
		if ((dataDir != null) && (dataDir.endsWith("/"))) {
			dataDir = dataDir.substring(0, dataDir.length() - 1);
		}
		partOneBuilder
				.append("dataDir=")
				.append(dataDir)
				.append("\n")
				.append("clientPort=")
				.append(""
						+ this.conf.getAdvanceConf().get(
								ZookeeperConstant.Keys.CLIENT_PORT))
				.append("\n")
				.append("tickTime=")
				.append(""
						+ this.conf.getAdvanceConf().get(
								ZookeeperConstant.Keys.TICK_TIME))
				.append("\n")
				.append("initLimit=")
				.append(""
						+ this.conf.getAdvanceConf().get(
								ZookeeperConstant.Keys.INIT_LIMIT))
				.append("\n")
				.append("syncLimit=")
				.append(""
						+ this.conf.getAdvanceConf().get(
								ZookeeperConstant.Keys.SYNC_LIMIT))
				.append("\n");

		// Building second part of zoo.cfg file
		StringBuilder partTwoBuilder = new StringBuilder();
		for (String host : this.conf.getNodes().keySet()) {
			partTwoBuilder
					.append("server.")
					.append(this.conf.getNodes().get(host)
							.get(ZookeeperConstant.Keys.NODE_ID)).append("=")
					.append(host).append(":2888:3888").append("\n");
		}
		// merging both buffers
		StringBuilder zooConfContent = partOneBuilder.append(partTwoBuilder);

		return zooConfContent.toString();
	}

	/**
	 * Configure jmx monitoring.
	 * 
	 * @return true, if successful
	 */
	private boolean configureJMXMonitoring() {

		logger.info("Configuring JMX Monitoring for Zookeeper started ... ",
				this.componentName, this.nodeConfig.getPublicHost());
		boolean status = true;
		String componentHome = conf.getHomeDir();
		String zkServer_FilePath = componentHome
				+ ZookeeperConstant.Keys.relPath_ZkServerScript;
		status = JmxMonitoringUtil
				.configureJmxPort(
						Constant.Process.QUORUMPEERMAIN,
						this.nodeConfig.getConnection(),
						zkServer_FilePath,
						this.conf
								.getAdvanceConfIntegerProperty(ZookeeperConstant.Keys.JMX_PORT),
						this.clusterConfig.getAuthConf().getPassword());
		if (!status) {
			logger.error("Could not update " + zkServer_FilePath + " file.",
					this.componentName, this.nodeConfig.getPublicHost());
			return false;
		}
		return true;
	}

	/**
	 * Configure.
	 * 
	 * @return true, if successful
	 */
	private boolean configure() {
		boolean statusFlag = false;
		logger.info("Creating zookeeper service configuration in agent...",
				this.componentName, this.nodeConfig.getPublicHost());
		// Create ZOOKEEPER Service XML configuration in agent.
		// Component service list
		List<ComponentService> services = new ArrayList<ComponentService>();
		// adding ZOOKEEPER service entry.
		services.add(new ComponentService(Constant.Process.QUORUMPEERMAIN,
				Constant.Role.ZOOKEEPER, Constant.ServiceType.JPS));
		// Creating ZOOKEEPER service XML.
		statusFlag = AgentUtils.createServiceXML(
				this.nodeConfig.getConnection(), services, this.componentName,
				this.clusterConfig.getAgentHomeDir());
		// add warning if failed
		if (!statusFlag) {
			logger.warn(
					"Could not create zookeeper service configuration in agent.",
					this.componentName, this.nodeConfig.getPublicHost());
		}
		logger.info("Copying JmxTrans JSON file to Zookeeper...",
				this.componentName, this.nodeConfig.getPublicHost());
		// Configuring JMX Monitoring using JMX-Trans
		statusFlag = JmxMonitoringUtil
				.copyJmxTransJson(
						this.nodeConfig.getConnection(),
						this.clusterConfig.getAuthConf(),
						Component.Name.ZOOKEEPER,
						Constant.Process.QUORUMPEERMAIN,
						conf.getAdvanceConfIntegerProperty(ZookeeperConstant.Keys.JMX_PORT),
						clusterConfig.getAgentHomeDir());
		if (!statusFlag) {
			logger.warn("Could not copy JmxTrans JSON file for Zookeeper.",
					this.componentName, this.nodeConfig.getPublicHost());
		}
		return statusFlag;
	}

	/**
	 * Action.
	 * 
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
//	public boolean action(final String action) {
//
//		SSHExec connection = null;
//		try {
//			File commandExecLogsFilePath = new File(
//					AgentUtils.getAgentLogDirectoryPath(clusterConfig)
//							+ ZookeeperConstant.Keys.ZOOKEEPER_COMMAND_EXECUTION_LOG_PATH
//							+ ".log");
//			connection = this.nodeConfig.getConnection();
//			// if connected
//			if (connection != null) {
//				String cmd = this.conf.getHomeDir() + "bin/zkServer.sh"
//						+ Strings.SPACE + action;
//				Result result;
//				AnkushTask zooActionCommand = new AnkushTask() {
//
//					@Override
//					public String getCommand() {
//						return conf.getHomeDir() + "bin/zkServer.sh"
//								+ Strings.SPACE + action;
//					}
//				};
//				// new RunInBackground(cmd,
//				// commandExecLogsFilePath.getAbsolutePath());
//				result = connection.exec(zooActionCommand);
//				if (result.rc != 0) {
//					throw new AnkushException(result.error_msg);
//				} else {
//					System.out.println("result.sysout : " + result.sysout);
//					if (result.sysout
//							.contains("Starting zookeeper ... already running")) {
//						throw new AnkushException(result.sysout);
//					}
//					return true;
//				}
//			} else {
//				throw new AnkushException(
//						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
//			}
//		} catch (AnkushException e) {
//			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
//			this.clusterConfig.addError(this.nodeConfig.getHost(),
//					componentName, e.getMessage());
//		} catch (Exception e) {
//			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
//			this.clusterConfig.addError(this.nodeConfig.getHost(),
//					componentName, e.getMessage());
//		}
//		return false;
//	}

	/**
	 * Removes the node.
	 * 
	 * @return true, if successful
	 */
	public boolean removeNode() {

		SSHExec connection = null;
		try {
			connection = this.nodeConfig.getConnection();
			if (connection != null) {
				AnkushTask removeZkDir = new Remove((String) this.conf
						.getAdvanceConf().get(
								ZookeeperConstant.Keys.DATA_DIRECTORY));
				AnkushTask removeZookeeper = new Remove(this.conf.getHomeDir());
				AnkushTask removeZooCommandExecutionLog = new Remove(
						AgentUtils.getAgentLogDirectoryPath(clusterConfig)
								+ ZookeeperConstant.Keys.ZOOKEEPER_COMMAND_EXECUTION_LOG_PATH);
				connection.exec(removeZkDir);
				connection.exec(removeZookeeper);
				connection.exec(removeZooCommandExecutionLog);
				return true;
			} else {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
		}
		return false;
	}

	/**
	 * Register.
	 * 
	 * @return true, if successful
	 */
	public boolean register() {
		SSHExec connection = null;
		try {
			connection = this.nodeConfig.getConnection();
			if (connection == null) {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
			return configure();
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
		}
		return false;
	}

	public boolean unregister() {
		SSHExec connection = null;
		boolean status = false;
		try {
			connection = this.nodeConfig.getConnection();
			if (connection == null) {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
			logger.info("Removing " + componentName + ".xml file from Agent");
			status = com.impetus.ankush2.agent.AgentUtils.removeServiceXml(
					nodeConfig.getConnection(), componentName);
			if (!status) {
				logger.warn("Couldn't remove " + componentName
						+ ".xml file from Agent", componentName,
						this.nodeConfig.getHost());
			}
			logger.info("Removing Zookeeper JMXTrans JSON from Agent");
			status = com.impetus.ankush2.agent.AgentUtils
					.removeProcessJmxtransJSON(nodeConfig.getConnection(),
							Constant.Process.QUORUMPEERMAIN,
							clusterConfig.getAgentHomeDir());
			if (!status) {
				logger.warn(
						"Couldn't remove Zookeeper JMXTrans JSON from Agent",
						componentName, this.nodeConfig.getHost());
			}
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
			status = false;
		} catch (Exception e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			this.clusterConfig.addError(this.nodeConfig.getHost(),
					componentName, e.getMessage());
			status = false;
		}
		return status;
	}
}
