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
package com.impetus.ankush.common.agent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.springframework.util.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.AppConf;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.scripting.impl.Unzip;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.ecosystem.oozie.OozieConf;
import com.impetus.ankush.hadoop.ecosystem.solr.SolrConf;

/**
 * It is deployable class for deployment of Agent on nodes.
 * 
 * @author hokam chauhan
 * 
 */
public class AgentDeployer implements Deployable {

	/** Method to add process name in the agent.properties file. */
	public static final String AGENT_PROPERTY_FILE_PATH = "$HOME/.ankush/agent/conf/agent.properties";

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	// Ankush Logger object.
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			Constant.Component.Name.AGENT, AgentDeployer.class);

	/**
	 * Deploy.
	 * 
	 * @param config
	 *            Configuration
	 * @return boolean
	 * @author hokam chauhan Method deploy.
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		String message = "Deploying agent";
		AgentConf conf = (AgentConf) config;
		logger.setLoggerConfig(conf);
		logger.info(message + "...");
		HashSet<NodeConf> nodeConfs = new HashSet<NodeConf>();
		nodeConfs.addAll(conf.getNodes());
		boolean status = createNodes(nodeConfs, conf);
		// returning the overall status of the action
		logger.log(message, status);
		return status;
	}

	/**
	 * Creates the nodes.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param conf
	 *            the conf
	 * @return status
	 */
	private boolean createNodes(HashSet<NodeConf> nodeConfs,
			final AgentConf conf) {
		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeConfs) {

				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = createNode(nodeConf, conf);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeConfs.size());
			status = status(nodeConfs);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Status.
	 * 
	 * @param keySet
	 *            Set<NodeConf>
	 * @return boolean
	 * @author hokam chauhan Method status.
	 */
	private boolean status(Set<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = status && nodeConf.getStatus();
		}
		return status;
	}

	/**
	 * Status.
	 * 
	 * @param nodes
	 *            the nodes
	 * @return boolean
	 * @author hokam chauhan Method status.
	 */
	private boolean status(List<NodeConf> nodes) {
		return status(new HashSet<NodeConf>(nodes));
	}

	/**
	 * Undeploy.
	 * 
	 * @param config
	 *            Configuration
	 * @return boolean
	 * @author hokam chauhan Method undeploy.
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		AgentConf conf = (AgentConf) config;
		removeNodes(conf.getNodes(), conf);
		return status(conf.getNodes());
	}

	/**
	 * Start.
	 * 
	 * @param config
	 *            Config is the bean of username, password and installation
	 *            path.
	 * @return Status of start action
	 * @author hokam chauhan Method start.
	 * @see com.impetus.ankush.common.framework.Deployable#start(Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		AgentConf conf = (AgentConf) config;
		startServices(conf.getNodes(), conf);
		return status(conf.getNodes());
	}

	/**
	 * Start node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 */
	private void startNode(NodeConf nodeConf, AgentConf conf) {

		String message = "Starting Agent";

		CustomTask task = new ExecCommand("sh " + Constant.Agent.AGENT_START_SCRIPT);

		// Setting the start progress message for activity startup.
		logger.info(nodeConf.getPublicIp(), message + "...");

		SSHExec connection = null;
		try {
			// Connection with node via SSH connection
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				Result result = connection.exec(task);

				nodeConf.setStatus(result.isSuccess);
			} else {
				nodeConf.setStatus(false);
				logger.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			logger.error(nodeConf, error, e);
			nodeConf.setStatus(false);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		logger.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());
	}

	/**
	 * Stop.
	 * 
	 * @param config
	 *            Config is the bean of username, password and installation
	 *            path.
	 * @return Status of start action
	 * @author hokam chauhan Method stop
	 * @see com.impetus.ankush.common.framework.Deployable#stop(Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		AgentConf conf = (AgentConf) config;
		stopServices(conf.getNodes(), conf);
		return status(conf.getNodes());
		/* Returning the overall status of the action */
	}

	/**
	 * Stop node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 */
	private void stopNode(NodeConf nodeConf, AgentConf conf) {

		// Log message.
		String message = "Stopping agent";

		/* Creating the Kill Process Command for AnkushAgent */
		CustomTask killProcess = new ExecCommand("sh " + Constant.Agent.AGENT_STOP_SCRIPT);

		/* Setting the start progress message for activity startup. */
		logger.info(nodeConf.getPublicIp(), message + "...");

		SSHExec connection = null;
		try {
			/* Connection with node via SSH connection */
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			/* if connected */
			if (connection != null) {
				/* Executing the Kill Process Task for AnkushAgent. */
				Result result = connection.exec(killProcess);
				nodeConf.setStatus(result.isSuccess);

				/* Stop jmxtrans */
				CustomTask runJmxtrans = new ExecCommand("cd \""
						+ conf.getInstallationPath() + "jmxtrans\" ; sh "
						+ ankushConf.getStringValue("jmx.script.file.name")
						+ " stop");
				connection.exec(runJmxtrans);

			} else {
				nodeConf.setStatus(false);
				logger.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			/* Logging Error for Exception Received */
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			logger.error(nodeConf, error, e);
			nodeConf.setStatus(false);
		} finally {
			/* Disconnecting the ssh connection */
			if (connection != null) {
				connection.disconnect();
			}
		}

		nodeConf.setStatus(true);
		logger.log(nodeConf.getPublicIp(), message, true);
	}

	private boolean configureJmxTrans(SSHExec connection, NodeConf nodeConf,
			String jmxtransInstallPath, String password) {
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
				logger.error(nodeConf, "Unable to update JmxTrans script file");
				return false;
			}
			jmxTask = new ReplaceText(targetText_DirJson, replacementText,
					filePath, false, password);
			res = connection.exec(jmxTask);
			if (!res.isSuccess) {
				logger.error(nodeConf, "Unable to update JmxTrans script file");
				return false;
			}
			replacementText = "\"" + jmxtransInstallPath
					+ ankushConf.getStringValue("jmx.dir.jarfile.name") + "\"";
			jmxTask = new ReplaceText(targetText_JarFile, replacementText,
					filePath, false, password);
			res = connection.exec(jmxTask);
			if (!res.isSuccess) {
				logger.error(nodeConf, "Unable to update JmxTrans script file");
				return false;
			}

		} catch (Exception e) {
			/* Logging Error for Exception Received */
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			logger.error(nodeConf, error, e);
			return false;
		}

		return true;
	}

	/**
	 * Creates the node.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            AgentConf
	 * @return boolean
	 * @author hokam chauhan Method createNode.
	 */
	private boolean createNode(NodeConf nodeConf, AgentConf conf) {

		SSHExec connection = null;
		boolean status = false;

		// Log message.
		String message = "Installing Ankush agent";
		/* Setting the start progress message for activity startup. */
		String publicIp = nodeConf.getPublicIp();
		logger.info(publicIp, message + "...");

		try {
			/* Connection with node via SSH connection */
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			if (connection != null) {
				logger.debug("Create directory - " + conf.getInstallationPath());
				/* make installation directory if not exists */
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());

				String jmxTransInstallRelPath = ankushConf
						.getStringValue("jmxtrans.installation.relative.path");
				String jmxtransPath = conf.getInstallationPath()
						+ jmxTransInstallRelPath;

				status = connection.exec(mkInstallationPath).isSuccess;

				if (!status) {
					/* Saving Progress message for directory creation failure. */
					logger.error(nodeConf,
							"Could not create installation and Conf directory.");
				} else {
					/* if directories exists at destination */
					String copyJarMsg = "Copying agent bundle";
					logger.info(publicIp, copyJarMsg + "...");

					/* Uploading the jar files to node */
					connection
							.uploadSingleDataToServer(conf.getLocalJarsPath(),
									conf.getInstallationPath());

					// unzip and remove task creation.
					String zipFilePath = conf.getInstallationPath()
							+ "agent.zip";
					AnkushTask unzipTask = new Unzip(zipFilePath,
							conf.getInstallationPath());
					AnkushTask removeTask = new Remove(zipFilePath);

					// executing unzip and task creation task.
					status = connection.exec(unzipTask).isSuccess
							| connection.exec(removeTask).isSuccess;

					logger.log(publicIp, copyJarMsg, status);

					// configure agent.properties file.
					String configuringMsg = "Configuring Agent";
					logger.info(publicIp, configuringMsg + "...");

					/* Updating agent.properties file */
					status = updateAgentPropertyFile(connection, nodeConf, conf);

					logger.log(publicIp, configuringMsg, status);

					if (status) {
						// configure jmxtrans.
						status = this.configureJmxTrans(connection, nodeConf,
								jmxtransPath, conf.getPassword());
					}
					/* Setting status of the execution to the node status. */
					nodeConf.setStatus(status);
				}
			} else {
				logger.error(nodeConf, "Authentication failed");
			}

		} catch (Exception e) {
			/* Logging Error for Exception Received */
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			nodeConf.setStatus(false);
			logger.error(nodeConf, error, e);
		} finally {
			/* Disconnecting the ssh connection */
			if (connection != null) {
				connection.disconnect();
			}
		}

		// Setting the end progress status for execution status
		logger.log(publicIp, message, nodeConf.getStatus());

		return nodeConf.getStatus();
	}

	/**
	 * Removes the node.
	 * 
	 * @param nodeConf
	 *            Nodeconf is the bean of public ip, private ip and status of
	 *            the node.
	 * @param conf
	 *            Conf is the object of AgentConf
	 * @return Status of the node removal
	 * @author hokam chauhan Method removeNode.
	 */
	protected boolean removeNode(NodeConf nodeConf, AgentConf conf) {
		SSHExec connection = null;
		boolean statusFlag = false;

		String message = "Removing agent";
		// Setting the start progress message for activity startup.
		logger.info(nodeConf.getPublicIp(), message + "...");

		try {
			// Connection with node via SSH connection
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			if (connection != null) {
				// Remoing agent directory
				AnkushTask removeAgentDir = new Remove(
						conf.getInstallationPath());
				statusFlag = connection.exec(removeAgentDir).isSuccess;
				nodeConf.setStatus(statusFlag);
			} else {
				logger.error(nodeConf.getPublicIp(), "Authentication failed");
			}
		} catch (Exception e) {
			logger.error(nodeConf.getPublicIp(), e.getMessage(), e);
			statusFlag = false;
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}

		// Setting the end progress status for execution status
		logger.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());

		return statusFlag;
	}

	private static String getProcessPortInfo(String processName, int port) {
		if (processName.isEmpty() || port == 0) {
			return "";
		}
		return (processName + ":" + port);
	}

	/**
	 * Update agent property file.
	 * 
	 * @param connection
	 *            the connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean updateAgentPropertyFile(SSHExec connection,
			NodeConf nodeConf, AgentConf conf) {
		boolean status = false;

		String agetConfFile = conf.getInstallationPath()
				+ "conf/agent.properties";

		try {
			GenericManager<AppConf, Long> appConfManager = AppStoreWrapper
					.getManager("appConfManager", AppConf.class);

			Properties agentProps = new Properties();
			agentProps.setProperty(Constant.Properties.Agent.NODE_ID, nodeConf
					.getId().toString());
			// set the cluster technology name
			agentProps.setProperty(
					Constant.Properties.Agent.CLUSTER_TECHNOLOGY_NAME,
					conf.getTechnologyName());
			// set the cluster id
			agentProps.setProperty(Constant.Properties.Agent.CLUSTER_ID, conf
					.getClusterDbId().toString());

			String publicIp = null;
			String publicPort = null;

			if (appConfManager != null) {
				AppConf appConf = appConfManager.getByPropertyValueGuarded(
						"confKey", "serverIP");
				Map map = JsonMapperUtil.mapFromObject(appConf.getObject());
				if (map.containsKey("publicIp")) {
					publicIp = (String) map.get("publicIp");
				}
				if (map.containsKey("port")) {
					publicPort = (String) map.get("port");
				}
			}

			if (publicIp != null && !publicIp.isEmpty()) {
				agentProps
						.setProperty(Constant.Properties.Agent.HOST, publicIp);
			}
			if (publicPort != null && !publicPort.isEmpty()) {
				agentProps.setProperty(Constant.Properties.Agent.PORT,
						publicPort);
			}

			// if type is not null then setting the jps process list in conf.
			if (nodeConf.getType() != null) {
				// node roles.
				Set<String> roles = new HashSet<String>(Arrays.asList(nodeConf
						.getType().split("/")));

				Set<String> services = new HashSet<String>();

				Set<String> processForPortChecking = new HashSet<String>();

				for (String role : roles) {

					if (role.equals(Constant.Role.GMETAD)) {
						continue;
					} else if (role.equals(Constant.Role.OOZIESERVER)) {
						OozieConf oozieConf = (OozieConf) conf.getClusterConf()
								.getClusterComponents()
								.get(Constant.Component.Name.OOZIE);
						String processPortInfo = AgentDeployer
								.getProcessPortInfo(Constant.RoleProcessName
										.getProcessName(role), oozieConf
										.getServerPort());
						processForPortChecking.add(processPortInfo);
					} else if (role.equals(Constant.Role.SOLRSERVICE)) {
						SolrConf solrConf = (SolrConf) conf.getClusterConf()
								.getClusterComponents()
								.get(Constant.Component.Name.SOLR);
						String processPortInfo = AgentDeployer
								.getProcessPortInfo(Constant.RoleProcessName
										.getProcessName(role), solrConf
										.getServicePort());
						processForPortChecking.add(processPortInfo);
					} else {
						String service = Constant.RoleProcessName
								.getProcessName(role);
						if (service != null) {
							services.add(service);
						}
					}
				}

				if (processForPortChecking.size() > 0) {
					agentProps.setProperty(Constant.Keys.PROCESS_PORT_MAP,
							StringUtils.collectionToDelimitedString(
									processForPortChecking, ","));
				}

				agentProps.setProperty(Constant.Keys.JPS_PROCESS_LIST,
						StringUtils.collectionToDelimitedString(services, ","));

				agentProps.setProperty("HOST_IP", nodeConf.getPrivateIp());
			}

			StringBuilder fileContent = new StringBuilder(Constant.LINE_SEPERATOR);
			for (String key : agentProps.stringPropertyNames()) {
				fileContent.append(key).append("=")
						.append(agentProps.getProperty(key))
						.append(Constant.LINE_SEPERATOR);
			}

			// Writing the agent.properties file on node
			CustomTask appendTask = new AppendFile(fileContent.toString(),
					agetConfFile);
			status = (connection.exec(appendTask).rc == 0);
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			logger.error(nodeConf, error, e);
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List,
	 * com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeConfs, Configuration config) {
		// Create agent conf set
		AgentConf conf = (AgentConf) config;
		logger.setLoggerConfig(conf);

		String message = "Adding agent";
		logger.info(message + "...");

		// Create node conf set.
		HashSet<NodeConf> nodeConfSet = new HashSet<NodeConf>(nodeConfs);

		// Adding agent on nodes.
		boolean status = createNodes(nodeConfSet, conf);

		// logging message.
		logger.log(message, status);

		// returning the overall status of the action
		return status;
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
		// Create agent conf set
		final AgentConf conf = (AgentConf) config;
		logger.setLoggerConfig(conf);

		String message = "Removing agent";
		logger.info(message + "...");

		// Create node conf set.
		HashSet<NodeConf> nodeConfSet = new HashSet<NodeConf>(nodeList);
		final Semaphore semaphore = new Semaphore(nodeConfSet.size());
		boolean status = false;
		// Removing nodes.
		try {
			for (final NodeConf nodeConf : nodeConfSet) {

				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						nodeConf.setStatus(removeNode(nodeConf, conf));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			// watiting for all thread termination.
			semaphore.acquire(nodeConfSet.size());
			// Getting status.
			status = status(nodeConfSet);
		} catch (Exception e) {
			logger.error(e.getMessage());
			status = false;
		}

		// logging message.
		logger.log(message, status);
		return status;
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
		// Create agent conf set
		final AgentConf conf = (AgentConf) config;
		logger.setLoggerConfig(conf);

		String message = "Starting agent";
		logger.info(message + "...");

		// Create node conf set.
		HashSet<NodeConf> nodeConfSet = new HashSet<NodeConf>(nodeList);
		final Semaphore semaphore = new Semaphore(nodeConfSet.size());
		boolean status = false;
		// Removing nodes.
		try {
			for (final NodeConf nodeConf : nodeConfSet) {

				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						startNode(nodeConf, conf);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			// watiting for all thread termination.
			semaphore.acquire(nodeConfSet.size());
			// Getting status.
			status = status(nodeConfSet);
		} catch (Exception e) {
			logger.error(e.getMessage());
			status = false;
		}

		// logging message.
		logger.log(message, status);
		return status;
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
		// Create agent conf set
		final AgentConf conf = (AgentConf) config;
		logger.setLoggerConfig(conf);

		String message = "Stopping agent";
		logger.info(message + "...");

		// Create node conf set.
		HashSet<NodeConf> nodeConfSet = new HashSet<NodeConf>(nodeList);
		final Semaphore semaphore = new Semaphore(nodeConfSet.size());
		boolean status = false;
		// Removing nodes.
		try {
			for (final NodeConf nodeConf : nodeConfSet) {

				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						stopNode(nodeConf, conf);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			// waiting for all thread termination.
			semaphore.acquire(nodeConfSet.size());
			// Getting status.
			status = status(nodeConfSet);
		} catch (Exception e) {
			logger.error(e.getMessage());
			status = false;
		}

		// logging message.
		logger.log(message, status);
		return status;
	}
}