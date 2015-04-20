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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.Copy;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.ganglia.GangliaConstants;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

public class AgentServiceManager implements Serviceable {

	private String errMessage(String action) {
		return "Could not " + action + " " + getComponentName()
				+ " . Please view server logs for more details.";
	}

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	/** The compConfig */
	private ComponentConfig componentConfig;

	private static AnkushLogger logger = new AnkushLogger(
			AgentServiceManager.class);

	String componentName;

	public AgentServiceManager() {

	}

	public AgentServiceManager(ClusterConfig clusterConfig) {
		logger.setCluster(clusterConfig);
	}

	@Override
	public String getComponentName() {
		return Constant.Component.Name.AGENT;
	}

	@Override
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public boolean startServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		try {
			return manageService(clusterConfig, host, services,
					Constant.ServiceAction.START);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, host, e);
		} catch (Exception e) {
			logger.error(errMessage("start"), componentName, host, e);
		}
		return false;
	}

	@Override
	public boolean stopServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		try {
			return manageService(clusterConfig, host, services,
					Constant.ServiceAction.STOP);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, host, e);
		} catch (Exception e) {
			logger.error(errMessage("start"), componentName, host, e);
		}
		return false;
	}

	public boolean manageService(ClusterConfig clusterConfig, String host,
			Set<String> services, Constant.ServiceAction action)
			throws AnkushException, Exception {
		try {
			// setting logger
			logger.setCluster(clusterConfig);
			SSHExec connection = clusterConfig.getNodes().get(host)
					.getConnection();
			if (connection == null) {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}

			if (!clusterConfig.getNodes().containsKey(host)) {
				throw new AnkushException("Could not find " + host
						+ " in cluster nodes. Please provide valid host.");
			}
			NodeConfig node = clusterConfig.getNodes().get(host);
			if (action.equals(Constant.ServiceAction.START)) {
				return startAgent(clusterConfig, node);
			} else if (action.equals(Constant.ServiceAction.STOP)) {
				return stopAgent(clusterConfig, node);
			} else {
				throw new AnkushException(action.toString().toLowerCase()
						+ " operation not supported.");
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean startAgent(ClusterConfig clusterConfig, NodeConfig nodeConfig)
			throws AnkushException {
		boolean status = false;
		try {
			System.out.println("clusterConfig.getInstallationType() : "
					+ clusterConfig.getInstallationType());
			// Add Agent Cron entry foe Sudo only
			if (clusterConfig.getInstallationType() == Constant.Cluster.InstallationType.SUDO) {
				// Copying cron file
				String filePathCronConf = clusterConfig.getAgentHomeDir()
						+ AgentConstant.Relative_Path.CRON_CONF_FILE;
				String destinationCronConfPath = AgentConstant.Strings.ETC_PATH_AGENT_CRON_CONF;

				AnkushTask copyCronConf = new Copy(filePathCronConf,
						destinationCronConfPath);
				AnkushTask execSudo = new ExecSudoCommand(clusterConfig
						.getAuthConf().getPassword(), copyCronConf.getCommand());
				if (!(status = nodeConfig.getConnection().exec(execSudo).rc == 0)) {
					logger.warn(
							"Could not copy agent cron configuration file from "
									+ filePathCronConf + " to "
									+ destinationCronConfPath,
							Constant.Component.Name.AGENT, nodeConfig.getHost());
				}
			}

			// Agent start script command
			String startAgent = "sh " + clusterConfig.getAgentHomeDir()
					+ AgentConstant.Relative_Path.START_SCRIPT;
			CustomTask task = new ExecCommand(startAgent);
			if (!(status = nodeConfig.getConnection().exec(task).rc == 0)) {
				logger.warn("Could not start Agent",
						Constant.Component.Name.AGENT, nodeConfig.getHost());
			}

			// Starting jmxtrans
			return startJmxTrans(clusterConfig, nodeConfig);
		} catch (AnkushException e) {
			throw e;
		} catch (TaskExecFailException e) {
			throw new AnkushException(
					"Either copying Agent cron file or starting Agent fail.", e);
		} catch (Exception e) {
			throw new AnkushException(errMessage("start"), e);
		}
	}

	public boolean stopAgent(ClusterConfig clusterConfig, NodeConfig nodeConfig)
			throws AnkushException {
		String etcCronConfPath = AgentConstant.Strings.ETC_PATH_AGENT_CRON_CONF;
		try {
			// Remove Agent Cron entry foe Sudo only
			if (clusterConfig.getInstallationType() == Constant.Cluster.InstallationType.SUDO) {
				AnkushTask removeCronFile = new Remove(etcCronConfPath);
				AnkushTask execSudo = new ExecSudoCommand(clusterConfig
						.getAuthConf().getPassword(),
						removeCronFile.getCommand());
				if (nodeConfig.getConnection().exec(execSudo).rc != 0) {
					logger.warn("Could not delete agent cron configuration file from "
							+ etcCronConfPath + ". Please remove it manually.");
				}
			}
			// stopping jmxTrans
			stopJmxTrans(clusterConfig, nodeConfig.getConnection(),
					nodeConfig.getHost());
			// Agent stop script command
			String stopAgent = "sh " + clusterConfig.getAgentHomeDir()
					+ AgentConstant.Relative_Path.STOP_SCRIPT;
			CustomTask task = new ExecCommand(stopAgent);
			if (nodeConfig.getConnection().exec(task).rc != 0) {
				logger.warn("Could not stop Agent. Please stop it manually.",
						Constant.Component.Name.AGENT, nodeConfig.getHost());
			}
		} catch (Exception e) {
			logger.error(errMessage("stop"), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
		}
		return true;
	}

	public boolean startJmxTrans(ClusterConfig clusterConfig,
			NodeConfig nodeConfig) throws AnkushException {
		boolean status = false;
		try {
			// Validating jmxtrans jar
			String validateJmxtransCmd = "jps | grep ankush-jmxtrans-all.jar";
			CustomTask task = new ExecCommand(validateJmxtransCmd);
			if (nodeConfig.getConnection().exec(task).rc == 0) {
				// stopping jmxTrans
				status = stopJmxTrans(clusterConfig,
						nodeConfig.getConnection(), nodeConfig.getHost());
			}
			// starting jmxTrans
			status = startJmxTrans(clusterConfig, nodeConfig.getConnection(),
					nodeConfig.getHost());
		} catch (AnkushException e) {
			logger.error(e.getMessage(), Constant.Component.Name.AGENT,
					nodeConfig.getHost(), e);
		} catch (Exception e) {
			logger.error("Could not start jmxtrans.",
					Constant.Component.Name.AGENT, nodeConfig.getHost(), e);
			status = false;
		}
		return status;
	}

	public boolean stopJmxTrans(ClusterConfig clusterConfig,
			NodeConfig nodeConfig) throws AnkushException {
		try {
			// stopping jmxTrans
			return stopJmxTrans(clusterConfig, nodeConfig.getConnection(),
					nodeConfig.getHost());
		} catch (Exception e) {
			throw new AnkushException("Could not stop jmxtrans");
		}
	}

	private void validateNode(ClusterConfig clusterConfig, String host,
			String action) throws AnkushException {
		try {
			if (clusterConfig == null || host == null || host.isEmpty()) {
				throw new AnkushException(
						"Either clusterConfig or host is empty or undefined.");
			}
			logger.setCluster(clusterConfig);
			logger.info("Validating connection...", getComponentName(), host);
			// get connection object
			SSHExec connection = clusterConfig.getNodes().get(host)
					.getConnection();
			if (connection == null) {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(errMessage(action));
		}
	}

	@Override
	public boolean startNode(ClusterConfig clusterConfig, String host) {
		try {
			validateNode(clusterConfig, host, "start");
			logger.info("Starting " + getComponentName() + "...",
					getComponentName(), host);
			// getting services list
			logger.info(
					"Getting " + getComponentName() + " services on nodes.",
					getComponentName(), host);
			Set<String> services = clusterConfig.getNodes().get(host)
					.getRoles().get(Constant.Component.Name.AGENT);

			return manageService(clusterConfig, host, services,
					Constant.ServiceAction.START);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), getComponentName(), host, e);
		} catch (Exception e) {
			logger.error(errMessage("start"), componentName, host, e);
		}
		return false;
	}

	@Override
	public boolean stopNode(ClusterConfig clusterConfig, String host) {
		try {
			validateNode(clusterConfig, host, "stop");
			logger.info("Stopping " + getComponentName() + "...",
					getComponentName(), host);
			// getting services list
			logger.info(
					"Getting " + getComponentName() + " services on nodes.",
					getComponentName(), host);
			Set<String> services = clusterConfig.getNodes().get(host)
					.getRoles().get(Constant.Component.Name.AGENT);

			return manageService(clusterConfig, host, services,
					Constant.ServiceAction.STOP);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), getComponentName(), host, e);
		} catch (Exception e) {
			logger.error(errMessage("start"), componentName, host, e);
		}
		return false;
	}

	@Override
	public boolean start(ClusterConfig clusterConfig) {
		try {
			return manageClusterServices(clusterConfig,
					Constant.ServiceAction.START);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, e);
		} catch (Exception e) {
			logger.error(errMessage("start"), componentName, e);
		}
		return false;
	}

	@Override
	public boolean stop(ClusterConfig clusterConfig) {
		try {
			return manageClusterServices(clusterConfig,
					Constant.ServiceAction.STOP);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, e);
		} catch (Exception e) {
			logger.error(errMessage("stop"), componentName, e);
		}
		return false;
	}

	private boolean manageClusterServices(final ClusterConfig clusterConfig,
			final Constant.ServiceAction action) throws AnkushException {
		try {
			if (clusterConfig == null) {
				throw new AnkushException("Could not get clusterConfig for "
						+ getComponentName());
			}
			logger.setCluster(clusterConfig);
			// Creating semaphores
			final Semaphore semaphore = new Semaphore(clusterConfig.getNodes()
					.size());
			// starting service on each node in cluster
			for (final String host : clusterConfig.getNodes().keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {

						switch (action) {
						case START:
							clusterConfig.getNodes().get(host)
									.setStatus(startNode(clusterConfig, host));
							break;
						case STOP:
							clusterConfig.getNodes().get(host)
									.setStatus(stopNode(clusterConfig, host));
							break;
						}
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(clusterConfig.getNodes().size());
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(errMessage(action.toString()
					.toLowerCase()));
		}
		return AnkushUtils.getStatus(clusterConfig.getNodes());
	}

	public boolean startJmxTrans(ClusterConfig clusterConfig,
			SSHExec connection, String host) throws AnkushException {
		// Skipping to start JMXTrans,if Cluster doesn't contain Ganglia
		if (!clusterConfig.getComponents().containsKey(
				Constant.Component.Name.GANGLIA)) {
			return true;
		}
		String message = "Starting JmxTrans";
		try {
			logger.info(message, getComponentName(), host);
			final String targetText_JmxOpts = this.ankushConf
					.getStringValue("jmx.opts.targetText");
			String replacementText_JmxOpts = this.ankushConf
					.getStringValue("jmx.opts.replacementText");
			final String jmxTransScriptFilePath = getJmxTransScriptFilePath(clusterConfig);

			ComponentConfig gangliaConfig = getGangliaConfig(clusterConfig);

			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(
							this.ankushConf
									.getStringValue("jmxtrans.script.template.gangliamasterip"),
							gangliaConfig
									.getAdvanceConfStringProperty(GangliaConstants.ClusterProperties.GMETAD_HOST));

			int gangliaPort;
			try {
				gangliaPort = gangliaConfig
						.getAdvanceConfIntegerProperty(GangliaConstants.ClusterProperties.GANGLIA_PORT);
			} catch (Exception e) {
				gangliaPort = AppStoreWrapper.getAnkushConfReader()
						.getIntValue("ganglia.port");
			}

			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(
							this.ankushConf
									.getStringValue("jmxtrans.script.template.gangliaport"),
							String.valueOf(gangliaPort));

			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(
							this.ankushConf
									.getStringValue("jmxtrans.script.template.privateip"),
							host);

			replacementText_JmxOpts = replacementText_JmxOpts
					.replaceAll(
							this.ankushConf
									.getStringValue("jmxtrans.script.template.serveralias"),
							host);

			final AnkushTask jmxTask = new ReplaceText(targetText_JmxOpts,
					replacementText_JmxOpts, jmxTransScriptFilePath, false,
					null);
			if (!connection.exec(jmxTask).isSuccess) {
				logger.warn("Unable to update JMXTRANS_OPTS in JmxTrans script file");
			}

			// Password is set to NULL to run the script command without sudo
			// option
			final String command = JmxMonitoringUtil.getJmxTransCommand(
					jmxTransScriptFilePath, null,
					Constant.JmxTransServiceAction.START);
			final AnkushTask task = new RunInBackground(command);
			if (!connection.exec(task).isSuccess) {
				throw new AnkushException(
						"Could not start jmxtrans service for JMX monitoring.");
			}
		} catch (AnkushException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error("Could not start Jmxtrans.", e);
		}
		return true;
	}

	public boolean stopJmxTrans(ClusterConfig clusterConfig,
			SSHExec connection, String host) throws AnkushException {
		try {
			final String command = JmxMonitoringUtil.getJmxTransCommand(
					getJmxTransScriptFilePath(clusterConfig), null,
					Constant.JmxTransServiceAction.STOP);

			final AnkushTask task = new RunInBackground(command);
			if (!connection.exec(task).isSuccess) {
				logger.warn(
						"Could not stop jmxtrans service for JMX monitoring.",
						Constant.Component.Name.AGENT, host);
			}
		} catch (Exception e) {
			logger.error("Could not stop Jmxtrans",
					Constant.Component.Name.AGENT, host, e);
		}
		return true;
	}

	private String getJmxTransScriptFilePath(ClusterConfig clusterConfig)
			throws AnkushException {
		try {
			String jmxInstallPath = clusterConfig.getAgentHomeDir()
					+ ankushConf
							.getStringValue("jmxtrans.installation.relative.path");
			return (jmxInstallPath + ankushConf
					.getStringValue("jmx.script.file.name"));
		} catch (Exception e) {
			throw new AnkushException(
					"Could not get Jmxtrans script file path.");
		}

	}

	private ComponentConfig getGangliaConfig(ClusterConfig clusterConfig)
			throws AnkushException {
		String errMsg = "Could not get " + Constant.Component.Name.GANGLIA
				+ " details for starting JmxTrans.";
		try {
			if (!clusterConfig.getComponents().containsKey(
					Constant.Component.Name.GANGLIA)) {
				throw new AnkushException(errMsg);
			}
			return clusterConfig.getComponents().get(
					Constant.Component.Name.GANGLIA);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(errMsg);
		}
	}

	@Override
	public Set<String> getServiceList(ClusterConfig clusterConfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean startRole(ClusterConfig clusterConfig, String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopRole(ClusterConfig clusterConfig, String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLogDirPath(ClusterConfig clusterConfig, String host,
			String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogFilesRegex(ClusterConfig clusterConfig, String host,
			String role, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}
}
