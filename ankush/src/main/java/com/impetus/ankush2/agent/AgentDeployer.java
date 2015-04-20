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
package com.impetus.ankush2.agent;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.domain.AppConf;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Move;
import com.impetus.ankush.common.scripting.impl.PrependFile;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.scripting.impl.Untar;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.constant.Constant.Strings.ExceptionsMessage;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

// Responsible for Agent and preprocessor deployment.
public class AgentDeployer extends AbstractDeployer {

	/** Method to add process name in the agent.properties file. */
	public static final String AGENT_PROPERTY_FILE_PATH = "$HOME/.ankush/agent/conf/agent.properties";

	public static final String ANKUSH_SERVER_PROCSS_NAME = "ankush.jar";

	public static final String AGENT_BUNDLE_NAME = "agent.tar.gz";

	public static final String AGENT_VERSION_FILENAME = "VERSION.txt";

	private static final String CRON_TARGET_TEXT_JAVA = "JAVA_HOME";

	private static final String CRON_TARGET_TEXT_USERNAME = "USER_NAME";

	/** The log. */
	private AnkushLogger logger = new AnkushLogger(AgentDeployer.class);

	private String agentHomeDir;

	final static ConfigurationReader ankushConf = AppStoreWrapper
			.getAnkushConfReader();

	// Creating agent bundle path.
	private final static String agentBundlePath = AppStoreWrapper
			.getResourcePath() + AnkushConstant.Agent.Keys.AGENT_TAR;

	private final static String AGENT_DAEMON_CLASS = ankushConf
			.getStringValue(AnkushConstant.Agent.Keys.AGENT_DAEMON_CLASS);

	private ClusterConfig clusterConfig;

	private void setClusterAndLogger(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		this.agentHomeDir = clusterConfig.getComponents()
				.get(Constant.Component.Name.AGENT).getInstallPath()
				+ AgentConstant.Relative_Path.AGENT_HOME_DIR;
		// setting logger config.
		logger.setCluster(clusterConfig);
	}

	@Override
	public boolean createConfig(ClusterConfig clusterConfig) {
		this.setClusterAndLogger(clusterConfig);
		return true;
	}

	@Override
	public boolean validate(ClusterConfig clusterConfig) {
		this.setClusterAndLogger(clusterConfig);

		// validate java on all nodes
		final Semaphore semaphore = new Semaphore(clusterConfig.getNodes()
				.size());
		final ComponentConfig javaConf = clusterConfig.getJavaConf();

		boolean status = false;
		try {
			// validate java on all nodes
			for (final NodeConfig nodeConfig : clusterConfig.getNodes()
					.values()) {
				// acuiring the semaphore
				semaphore.acquire();
				// starting a thread to start pre-processing on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							validateJava(javaConf, nodeConfig);
							if (nodeConfig.getStatus()) {
								logger.info("Validating java done.",
										getComponentName(),
										nodeConfig.getHost());
							} else {
								logger.info("Validating java failed.",
										getComponentName(),
										nodeConfig.getHost());
							}
						} catch (Exception e) {
							logger.error(
									"Exception in validating Java."
											+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
									Constant.Component.Name.AGENT,
									nodeConfig.getHost(), e);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(clusterConfig.getNodes().size());
			// getting the status on behalf of if status is false on a single
			// node, final status will be false as JAVA needs to be same on all
			// the nodes
			status = getStatus(clusterConfig.getNodes());
		} catch (Exception e) {
			logger.error(e.getMessage(), this.getComponentName());
		}
		if (!status) {
			logger.error("Validating Java failed.", this.getComponentName());
			this.clusterConfig.addError(this.getComponentName(),
					"Validating Java failed.");
		}
		return status;
	}

	private void validateJava(ComponentConfig javaConf, NodeConfig nodeConfig) {
		boolean status = true;
		logger.info("Validating java...", this.getComponentName(),
				nodeConfig.getHost());
		try {
			// if java is already installed
			if (javaConf.isRegister()) {
				//check for java/bin existence at the user given Java Path and run jav --version command
//				JavaValidator javaValidator = new JavaValidator(
//						nodeConfig.getConnection());
//				// validate java home
//				if (!javaValidator.validate(clusterConfig.getJavaConf()
//						.getHomeDir())) {
//					clusterConfig.addError(nodeConfig.getHost(),
//							this.getComponentName(), javaValidator.getErrMsg());
//					logger.error(javaValidator.getErrMsg(),
//							this.getComponentName(), nodeConfig.getHost());
//					status = false;
//				}
			} else {
				// validate java bundle existence in Server Repo
				File f = new File(AppStoreWrapper.getServerRepoPath()
						+ javaConf.getSource());
				if (!f.exists()) {
					String errMsg = "Given java bundle - "
							+ javaConf.getSource()
							+ " doesn't exist in Ankush repo : "
							+ AppStoreWrapper.getServerRepoPath() + ".";
					clusterConfig.addError(nodeConfig.getHost(),
							this.getComponentName(), errMsg);
					logger.error(errMsg, this.getComponentName(),
							nodeConfig.getHost());
					status = false;
				}

			}
		} catch (Exception e) {
			logger.error("Exception in validating java."
					+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
					this.getComponentName(), nodeConfig.getHost(), e);
			status = false;
		}
		nodeConfig.setStatus(status);
	}

	@Override
	public boolean deploy(ClusterConfig config) {
		// set cluster
		this.setClusterAndLogger(config);
		// deploy nodes
		return deployNodes(this.clusterConfig);
	}

	private boolean deployNodes(final ClusterConfig clusterConf) {
		final Semaphore semaphore = new Semaphore(clusterConf.getNodes().size());

		final String deployFailureMsg = "Could not deploy Agent on node";
		try {
			for (final NodeConfig nodeConfig : clusterConf.getNodes().values()) {
				// acuiring the semaphore
				semaphore.acquire();
				final AgentServiceManager agentServiceManager = new AgentServiceManager(
						clusterConf);
				// starting a thread to start pre-processing on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							if (createAgentNode(nodeConfig)) {
								agentServiceManager.startAgent(clusterConfig,
										nodeConfig);
							} else {
								throw new AnkushException(deployFailureMsg);
							}
						} catch (AnkushException e) {
							logger.error(e.getMessage(),
									Constant.Component.Name.AGENT,
									nodeConfig.getHost());
							clusterConf.addError(Constant.Component.Name.AGENT,
									e.getMessage());
						} catch (Exception e) {
							logger.error(deployFailureMsg,
									Constant.Component.Name.AGENT,
									nodeConfig.getHost());
							clusterConf.addError(Constant.Component.Name.AGENT,
									deployFailureMsg);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}

					}
				});
			}
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(clusterConf.getNodes().size());
			return AnkushUtils.getStatus(clusterConf.getNodes());
		} catch (Exception e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT, e);
			return false;
		}
	}

	private boolean createAgentNode(NodeConfig nodeConfig) {
		String agentInstallDir = clusterConfig.getComponents()
				.get(Constant.Component.Name.AGENT).getInstallPath();

		SSHExec connection = null;
		boolean status = false;

		// Log message.
		String message = "Installing Ankush agent";
		logger.info(message + "...", Constant.Component.Name.AGENT,
				nodeConfig.getHost());

		try {
			connection = nodeConfig.getConnection();

			if (connection == null) {
				throw new AnkushException(
						ExceptionsMessage.CONNECTION_NULL_STRING);
			}
			/* Creating the Kill Process Command for AnkushAgent */
			CustomTask killProcess = new ExecCommand("sh " + this.agentHomeDir
					+ AgentConstant.Relative_Path.STOP_SCRIPT);

			// stopping already running agent
			connection.exec(killProcess);
			// backup old existing agent folder.
			Move backUpAgent = new Move(agentHomeDir, agentHomeDir
					+ "/../agentBackup");

			// execute task.
			connection.exec(backUpAgent);

			logger.debug("Create directory - " + agentHomeDir,
					nodeConfig.getHost());
			/* make installation directory if not exists */
			AnkushTask mkInstallationPath = new MakeDirectory(agentHomeDir);

			status = connection.exec(mkInstallationPath).isSuccess;

			if (!status) {
				/*
				 * Throwing Progress message exception for directory creation
				 * failure.
				 */
				throw new AnkushException(
						"Could not create installation and Conf directory.");
			}
			// upload agent bundle from the server and Extract it
			logger.info("Uploading Agent...", Constant.Component.Name.AGENT,
					nodeConfig.getHost());
			status = uploadAndExtractAgent(nodeConfig, connection);

			if (!status) {
				throw new AnkushException(
						"Failed to upload and Extract Agent bundle.");
			}
			// configure agent.properties file.
			logger.info("Configuring Agent...", Constant.Component.Name.AGENT,
					nodeConfig.getHost());

			/* Updating agent.properties file */
			status = updateAgentPropertyFile(connection, nodeConfig);

			if (!status) {
				throw new AnkushException("Couldn't configure Agent.");
			}
			// prepend agent start-script, action-script and
			// log4j.properties file with Agent Install Dir
			logger.info(
					"Updating Agent Script files with Agent install dir...",
					Constant.Component.Name.AGENT, nodeConfig.getHost());
			status = updateAgentScripts(agentInstallDir, connection, status);
			if (!status) {
				throw new AnkushException(
						"Couldn't update Agent Scripts with Agent install dir.");
			}
			logger.info("Creating service dir in Agent...",
					Constant.Component.Name.AGENT, nodeConfig.getHost());
			// Creating make service dir command.
			CustomTask mkDir = new MakeDirectory(this.agentHomeDir
					+ AgentConstant.Relative_Path.SERVICE_CONF_DIR);
			// Execute command.
			status = connection.exec(mkDir).isSuccess;
			if (!status) {
				throw new AnkushException(
						"Couldn't create service dir in Agent install dir.");
			}
			status = configureJmxTrans(nodeConfig, connection, status);
			if (!status) {
				throw new AnkushException("Couldn't configure JMXTrans.");
			}
			// update agent cron file
			status = updateAgentCronFile(nodeConfig, agentInstallDir,
					connection, status);
			if (!status) {
				throw new AnkushException("Could not update agent cron file");
			}
			/* Setting status of the execution to the node status. */
			nodeConfig.setStatus(status);

		} catch (AnkushException e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
			nodeConfig.setStatus(false);
		} catch (Exception e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
			nodeConfig.setStatus(false);
		}
		return nodeConfig.getStatus();
	}

	private boolean configureJmxTrans(NodeConfig nodeConfig,
			SSHExec connection, boolean status) {
		// configure JMXTrans only if Ganglia is availbale
		if (clusterConfig.getComponents().containsKey(
				Constant.Component.Name.GANGLIA)) {
			String jmxTransInstallRelPath = ankushConf
					.getStringValue("jmxtrans.installation.relative.path");
			String jmxtransPath = agentHomeDir + jmxTransInstallRelPath;
			// configure jmxtrans.
			status = this.configureJmxTrans(connection, nodeConfig,
					jmxtransPath, this.clusterConfig.getAuthConf()
							.getPassword());
		}
		return status;
	}

	private boolean uploadAndExtractAgent(NodeConfig nodeConfig,
			SSHExec connection) throws Exception, TaskExecFailException {
		boolean status;
		/* if directories exists at destination */
		String copyJarMsg = "Copying agent bundle";
		logger.info(copyJarMsg + "...", Constant.Component.Name.AGENT,
				nodeConfig.getHost());

		/* Uploading the jar files to node */
		connection.uploadSingleDataToServer(agentBundlePath, agentHomeDir);

		// untar and remove task creation.
		String tarFilePath = agentHomeDir + AgentDeployer.AGENT_BUNDLE_NAME;

		AnkushTask unTarTask = new Untar(tarFilePath, agentHomeDir, false);
		AnkushTask removeTask = new Remove(tarFilePath);

		// executing untar and task creation task.
		status = connection.exec(unTarTask).isSuccess;
		connection.exec(removeTask);
		if (!status) {
			logger.error("Could not extract agent bunble.",
					Constant.Component.Name.AGENT, nodeConfig.getHost());
			status = false;
		}
		return status;
	}

	private boolean updateAgentScripts(String agentInstallDir,
			SSHExec connection, boolean status) throws TaskExecFailException {
		// map containing filePaths and agentInstallDir line
		Map<String, String> filePathAndPrependingLineMap = new HashMap<String, String>();

		StringBuilder agentInstallDirVariableInLog4j = new StringBuilder(
				AgentConstant.Key.JAVA_PROPERTY_AGENT_INSTALL_DIR).append("=")
				.append(agentInstallDir);
		StringBuilder agentInstallDirVariable = new StringBuilder(
				AgentConstant.Key.INSTALL_DIR_ENV_VARIABLE_KEY).append("=")
				.append(agentInstallDir);
		filePathAndPrependingLineMap.put(agentHomeDir
				+ AgentConstant.Relative_Path.LOG4J_PROPERTIES_FILE,
				agentInstallDirVariableInLog4j.toString());
		filePathAndPrependingLineMap.put(agentHomeDir
				+ AgentConstant.Relative_Path.START_SCRIPT,
				agentInstallDirVariable.toString());
		filePathAndPrependingLineMap.put(agentHomeDir
				+ AgentConstant.Relative_Path.ACTION_SCRIPT,
				agentInstallDirVariable.toString());
		for (String filePath : filePathAndPrependingLineMap.keySet()) {
			AnkushTask ankushTask = new PrependFile(
					filePathAndPrependingLineMap.get(filePath), filePath);
			status = connection.exec(ankushTask).rc == 0;
			if (!status) {
				logger.error("Could not update " + filePath + " for Agent.");
			}
		}
		return status;
	}

	private boolean updateAgentCronFile(NodeConfig nodeConfig,
			String agentInstallDir, SSHExec connection, boolean status)
			throws TaskExecFailException {
		Map<String, String> updateCronConfMap = new LinkedHashMap<String, String>();
		String javaBinDir = FileUtils
				.getSeparatorTerminatedPathEntry(this.clusterConfig
						.getJavaConf().getHomeDir())
				+ "bin";
		updateCronConfMap.put(AgentDeployer.CRON_TARGET_TEXT_JAVA, javaBinDir);

		updateCronConfMap.put(AgentDeployer.CRON_TARGET_TEXT_USERNAME,
				this.clusterConfig.getAuthConf().getUsername());
		updateCronConfMap.put(AgentConstant.Key.INSTALL_DIR_ENV_VARIABLE_KEY,
				agentInstallDir);

		String filePathCronConf = agentHomeDir
				+ AgentConstant.Relative_Path.CRON_CONF_FILE;

		for (String targetText : updateCronConfMap.keySet()) {
			AnkushTask updateCronConf = new ReplaceText(targetText,
					updateCronConfMap.get(targetText), filePathCronConf, false);
			if (connection.exec(updateCronConf).rc != 0) {
				logger.error("Could not update " + targetText
						+ " in agent cron configuration file.",
						Constant.Component.Name.AGENT, nodeConfig.getHost());
				status = false;
			}
		}
		return status;
	}

	private boolean updateAgentPropertyFile(SSHExec connection,
			NodeConfig nodeConfig) {
		boolean status = false;

		String agentServiceConfPath = this.agentHomeDir
				+ AgentConstant.Relative_Path.SERVICE_CONF_DIR;
		String agentConfFile = this.agentHomeDir
				+ AgentConstant.Relative_Path.AGENT_CONF_FILE;

		try {
			GenericManager<AppConf, Long> appConfManager = AppStoreWrapper
					.getManager("appConfManager", AppConf.class);

			Properties agentProps = new Properties();
			agentProps.setProperty(Constant.Properties.Agent.NODE_ID,
					nodeConfig.getId().toString());
			// set the cluster id
			agentProps.setProperty(Constant.Properties.Agent.CLUSTER_ID,
					clusterConfig.getClusterId().toString());

			// set the service conf directory.
			agentProps.setProperty(Constant.Properties.Agent.SERVICE_CONF_DIR,
					agentServiceConfPath);

			String publicIp = null;
			String publicPort = null;

			// if app conf manager is not null.
			if (appConfManager != null) {
				// Getting app conf object.
				AppConf appConf = appConfManager.getByPropertyValueGuarded(
						Constant.Keys.CONFKEY, Constant.Keys.SERVERIP);
				// if app conf is null.
				if (appConf == null) {
					logger.error(
							"Unable to configure the public ip/port in agent",
							Constant.Component.Name.AGENT, nodeConfig.getHost());
					return false;
				}
				// getting app conf object as map.
				Map map = JsonMapperUtil.mapFromObject(appConf.getObject());
				// if it contains the public ip.
				if (map.containsKey(Constant.Keys.PUBLICIP)) {
					publicIp = (String) map.get(Constant.Keys.PUBLICIP);
				}
				// if it contains the port.
				if (map.containsKey(Constant.Keys.PORT)) {
					publicPort = (String) map.get(Constant.Keys.PORT);
				}
			}

			if (publicIp != null && !publicIp.isEmpty()) {
				agentProps.setProperty(Constant.Properties.Agent.SERVER_IP,
						publicIp);
			}
			if (publicPort != null && !publicPort.isEmpty()) {
				agentProps.setProperty(Constant.Properties.Agent.PORT,
						publicPort);
			}

			// Setting host ip
			agentProps.setProperty("HOST_IP", nodeConfig.getHost());
			// setting host public ip
			agentProps.setProperty(Constant.Properties.Agent.HOST_PUBLIC_IP,
					nodeConfig.getHost());
			// setting host private ip
			agentProps.setProperty(Constant.Properties.Agent.HOST_PRIVATE_IP,
					nodeConfig.getHost());

			// props file.
			StringBuilder fileContent = new StringBuilder(
					com.impetus.ankush2.constant.Constant.Strings.LINE_SEPERATOR);

			// iterating over the properties.
			for (String key : agentProps.stringPropertyNames()) {
				fileContent
						.append(key)
						.append("=")
						.append(agentProps.getProperty(key))
						.append(com.impetus.ankush2.constant.Constant.Strings.LINE_SEPERATOR);
			}

			// Writing the agent.properties file on node
			CustomTask appendTask = new AppendFileUsingEcho(
					fileContent.toString(), agentConfFile);
			status = (connection.exec(appendTask).rc == 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
		}
		return status;
	}

	private boolean configureJmxTrans(SSHExec connection,
			NodeConfig nodeConfig, String jmxtransInstallPath, String password) {
		try {
			String targetText_DirLog = ankushConf
					.getStringValue("jmx.dir.targetText.log");
			String targetText_DirJson = ankushConf
					.getStringValue("jmx.dir.targetText.json");
			String targetText_JarFile = ankushConf
					.getStringValue("jmx.dir.targetText.jarfile");
			String filePath = jmxtransInstallPath
					+ ankushConf.getStringValue("jmx.script.file.name");

			String replacementText = "\"" + jmxtransInstallPath + "\"";

			AnkushTask jmxTask = new ReplaceText(targetText_DirLog,
					replacementText, filePath, false, password);
			Result res = connection.exec(jmxTask);
			if (!res.isSuccess) {
				logger.error("Unable to update JmxTrans script file",
						Constant.Component.Name.AGENT, nodeConfig.getHost());
				return false;
			}
			jmxTask = new ReplaceText(targetText_DirJson, replacementText,
					filePath, false, password);
			res = connection.exec(jmxTask);
			if (!res.isSuccess) {
				logger.error("Unable to update JmxTrans script file",
						Constant.Component.Name.AGENT, nodeConfig.getHost());
				return false;
			}
			replacementText = "\"" + jmxtransInstallPath
					+ ankushConf.getStringValue("jmx.dir.jarfile.name") + "\"";
			jmxTask = new ReplaceText(targetText_JarFile, replacementText,
					filePath, false, password);
			res = connection.exec(jmxTask);
			if (!res.isSuccess) {
				logger.error("Unable to update JmxTrans script file",
						Constant.Component.Name.AGENT, nodeConfig.getHost());
				return false;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
			return false;
		}

		return true;
	}

	private boolean startNode(NodeConfig nodeConfig) {

		String message = "Starting Agent";

		CustomTask task = new ExecCommand("sh " + this.agentHomeDir
				+ AgentConstant.Relative_Path.START_SCRIPT);

		// Setting the start progress message for activity startup.
		logger.info(message + "...", Constant.Component.Name.AGENT,
				nodeConfig.getHost());

		SSHExec connection = null;
		try {
			// Connection with node via SSH connection
			connection = nodeConfig.getConnection();

			// if connected
			if (connection != null) {
				Result result = connection.exec(task);
				// nodeConfig.setStatus(result.isSuccess);
			} else {
				// nodeConfig.setStatus(false);
				logger.error(ExceptionsMessage.CONNECTION_NULL_STRING,
						Constant.Component.Name.AGENT, nodeConfig.getHost());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
			// nodeConfig.setStatus(false);
		}
		// return nodeConfig.getStatus();
		return true;
	}

	@Override
	public boolean undeploy(ClusterConfig conf) {
		this.setClusterAndLogger(conf);
		logger.info("Undeploying Agent...", Constant.Component.Name.AGENT);
		return undeployNodes(conf);
	}

	private boolean undeployNodes(ClusterConfig conf) {
		try {
			final Semaphore semaphore = new Semaphore(conf.getNodes().size());
			for (final NodeConfig nc : conf.getNodes().values()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							removeNode(nc);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							nc.setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(conf.getNodes().size());
			return AnkushUtils.getStatus(conf.getNodes());
		} catch (InterruptedException e) {
			logger.info(e.getMessage(), Constant.Component.Name.AGENT);
			return false;
		} catch (Exception e) {
			logger.info(e.getMessage(), Constant.Component.Name.AGENT);
			return false;
		}
	}

	private void removeNode(NodeConfig nodeConfig) {
		SSHExec connection = null;
		boolean statusFlag = true;

		// Setting the start progress message for activity startup.
		try {
			// Connection with node via SSH connection
			connection = nodeConfig.getConnection();
			if (connection != null) {
				AgentServiceManager agentServiceManager = new AgentServiceManager(
						clusterConfig);
				logger.info("Stopping Agent...", Constant.Component.Name.AGENT,
						nodeConfig.getHost());
				// stopping Agent
				agentServiceManager.stopAgent(clusterConfig, nodeConfig);
				logger.info(
						"Removing Agent home dir - "
								+ clusterConfig.getAgentHomeDir(),
						Constant.Component.Name.AGENT, nodeConfig.getHost());
				// Removing agent directory
				AnkushTask removeAgentDir = new Remove(
						clusterConfig.getAgentHomeDir());
				if (connection.exec(removeAgentDir).rc != 0) {
					logger.error(
							"Couldn't remove Agent : "
									+ clusterConfig.getAgentHomeDir(),
							Constant.Component.Name.AGENT,
							nodeConfig.getPublicHost());
				}
			} else {
				logger.error(ExceptionsMessage.CONNECTION_NULL_STRING,
						Constant.Component.Name.AGENT,
						nodeConfig.getPublicHost());
			}
		} catch (AnkushException e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getPublicHost(), e);
			statusFlag = false;
		} catch (Exception e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getPublicHost(), e);
			statusFlag = false;
		}
		nodeConfig.setStatus(statusFlag);
	}

	private boolean stopNode(NodeConfig nodeConfig) {
		// Log message.
		String message = "Stopping agent";

		/* Creating the Kill Process Command for AnkushAgent */
		CustomTask killProcess = new ExecCommand("sh " + this.agentHomeDir
				+ AgentConstant.Relative_Path.STOP_SCRIPT);

		/* Setting the start progress message for activity startup. */
		logger.info(message + "...", Constant.Component.Name.AGENT,
				nodeConfig.getPublicHost());
		boolean statusFlag = false;

		SSHExec connection = null;
		try {
			/* Connection with node via SSH connection */
			connection = nodeConfig.getConnection();

			/* if connected */
			if (connection != null) {
				/* Executing the Kill Process Task for AnkushAgent. */
				Result result = connection.exec(killProcess);
				statusFlag = result.isSuccess;
			} else {
				logger.error(ExceptionsMessage.CONNECTION_NULL_STRING,
						Constant.Component.Name.AGENT,
						nodeConfig.getPublicHost());
			}
		} catch (Exception e) {
			statusFlag = false;
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
		}
		nodeConfig.setStatus(statusFlag);
		return statusFlag;
	}

	@Override
	public boolean unregister(ClusterConfig conf) {
		return undeploy(conf);
	}

	@Override
	public boolean start(ClusterConfig conf) {
		// set clusterConfig
		this.setClusterAndLogger(conf);

		final Semaphore semaphore = new Semaphore(clusterConfig.getNodes()
				.size());

		try {
			for (final NodeConfig nodeConfig : clusterConfig.getNodes()
					.values()) {
				// acuiring the semaphore
				semaphore.acquire();
				// starting a thread to start pre-processing on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							if (!startNode(nodeConfig)) {
								logger.error("Could not start Agent on node",
										Constant.Component.Name.AGENT,
										nodeConfig.getHost());
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
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(clusterConfig.getNodes().size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return AnkushUtils.getStatus(clusterConfig.getNodes());
	}

	@Override
	public boolean stop(ClusterConfig conf) {
		// set clusterConfig and logger
		this.setClusterAndLogger(conf);

		final Semaphore semaphore = new Semaphore(clusterConfig.getNodes()
				.size());

		try {
			for (final NodeConfig nodeConfig : clusterConfig.getNodes()
					.values()) {
				// acuiring the semaphore
				semaphore.acquire();
				// starting a thread to start pre-processing on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							if (!stopNode(nodeConfig)) {
								logger.error("Could not stop Agent on node",
										Constant.Component.Name.AGENT,
										nodeConfig.getHost());
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
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(clusterConfig.getNodes().size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return AnkushUtils.getStatus(clusterConfig.getNodes());
	}

	@Override
	public boolean addNode(ClusterConfig conf, ClusterConfig newConf) {
		this.setClusterAndLogger(conf);
		return deployNodes(newConf);
	}

	@Override
	public boolean removeNode(ClusterConfig conf, Collection<String> nodes) {
		this.setClusterAndLogger(conf);
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			for (final String host : nodes) {
				semaphore.acquire();
				final NodeConfig nc = conf.getNodes().get(host);
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							removeNode(nc);
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
			semaphore.acquire(nodes.size());
			return AnkushUtils.getStatus(conf, nodes);
		} catch (InterruptedException e) {
			logger.info(e.getMessage(), Constant.Component.Name.AGENT);
			return false;
		} catch (Exception e) {
			logger.info(e.getMessage(), Constant.Component.Name.AGENT);
			return false;
		}
	}

	@Override
	public boolean removeNode(ClusterConfig conf, ClusterConfig newConf) {
		this.setClusterAndLogger(conf);
		return undeployNodes(newConf);
	}

	@Override
	public boolean register(ClusterConfig conf) {
		return deploy(clusterConfig);
	}

	public boolean getStatus(Map<String, NodeConfig> map) {
		boolean status = true;
		for (NodeConfig nodeConf : map.values()) {
			if (!nodeConf.getStatus()) {
				return false;
			}
		}
		return status;
	}
}
