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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

public class ZookeeperServiceMonitor implements Serviceable {

	final private static AnkushLogger logger = new AnkushLogger(
			ZookeeperServiceMonitor.class);

	private String componentName;

	private ClusterConfig clusterConfig;

	private ComponentConfig compConf;

	private Set<String> errors = new HashSet<String>();

	public ZookeeperServiceMonitor() {

	}

	public ZookeeperServiceMonitor(ClusterConfig clusterConfig,
			String componentName) {
		this.clusterConfig = clusterConfig;
		logger.setCluster(clusterConfig);
		if (!clusterConfig.getComponents().containsKey(componentName)) {
			logger.error(ZookeeperConstant.Keys.ERROR_Zookeeper_CONF_NOT_FOUND,
					componentName);
			errors.add(ZookeeperConstant.Keys.ERROR_Zookeeper_CONF_NOT_FOUND);
			clusterConfig.addError(componentName, errors);
		}
		this.compConf = clusterConfig.getComponents().get(componentName);
	}

	public boolean setCluster(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		logger.setCluster(clusterConfig);
		if (!clusterConfig.getComponents().containsKey(componentName)) {
			logger.error(ZookeeperConstant.Keys.ERROR_Zookeeper_CONF_NOT_FOUND,
					componentName);
			errors.add(ZookeeperConstant.Keys.ERROR_Zookeeper_CONF_NOT_FOUND);
			clusterConfig.addError(componentName, errors);
			return false;
		}
		this.compConf = clusterConfig.getComponents().get(componentName);
		return true;
	}

	// public boolean setCluster(ClusterConfig clusterConfig, String
	// componentName) {
	// this.clusterConfig = clusterConfig;
	// logger.setCluster(clusterConfig);
	// if (!clusterConfig.getComponents().containsKey(componentName)) {
	// logger.error(ZookeeperConstant.Keys.ERROR_Zookeeper_CONF_NOT_FOUND,
	// componentName);
	// errors.add(ZookeeperConstant.Keys.ERROR_Zookeeper_CONF_NOT_FOUND);
	// clusterConfig.addError(componentName, errors);
	// return false;
	// }
	// this.compConf = clusterConfig.getComponents().get(componentName);
	// return true;
	// }

	@Override
	public String getComponentName() {
		return componentName;
	}

	@Override
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public boolean startServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageNodeService(host, Constant.ServiceAction.START);
	}

	@Override
	public boolean stopServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageNodeService(host, Constant.ServiceAction.STOP);
	}

	@Override
	public boolean startNode(ClusterConfig clusterConfig, String host) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageNodeService(host, Constant.ServiceAction.START);
	}

	@Override
	public boolean stopNode(ClusterConfig clusterConfig, String host) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageNodeService(host, Constant.ServiceAction.STOP);
	}

	@Override
	public boolean start(ClusterConfig clusterConfig) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageClusterService(Constant.ServiceAction.START);
	}

	@Override
	public boolean stop(ClusterConfig clusterConfig) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageClusterService(Constant.ServiceAction.STOP);
	}

	private boolean manageNodeService(String host, Constant.ServiceAction action) {
		boolean statusFlag = false;
		try {
			switch (action) {
			case START:
				statusFlag = action(ZookeeperConstant.Action.START, host);
				break;
			case STOP:
				statusFlag = action(ZookeeperConstant.Action.STOP, host);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			errors.add(Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG);
		}
		if (!statusFlag) {
			errors.add("Couldn't " + action + " : zookeeper on node : " + host);
			this.clusterConfig.addError(componentName, errors);
		}
		return statusFlag;
	}

	private boolean manageClusterService(final Constant.ServiceAction action) {
		boolean statusFlag = false;
		try {
			final Semaphore semaphore = new Semaphore(compConf.getNodes()
					.size());
			for (final String host : compConf.getNodes().keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						switch (action) {
						case START:
							clusterConfig
									.getNodes()
									.get(host)
									.setStatus(
											action(ZookeeperConstant.Action.START,
													host));
							break;
						case STOP:
							clusterConfig
									.getNodes()
									.get(host)
									.setStatus(
											action(ZookeeperConstant.Action.STOP,
													host));
						}

						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(compConf.getNodes().size());
			statusFlag = AnkushUtils.getStatus(clusterConfig, compConf
					.getNodes().keySet());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			errors.add(Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG);
		}
		if (!statusFlag) {
			errors.add("Couldn't " + action + " : " + componentName);
			this.clusterConfig.addError(componentName, errors);
		}
		return statusFlag;
	}

	public boolean action(final String action, String host) {
		NodeConfig nodeConfig = this.clusterConfig.getNodes().get(host);
		boolean statusFlag = false;
		try {
			// File commandExecLogsFilePath = new File(
			// AgentUtils.getAgentLogDirectoryPath(clusterConfig)
			// + ZookeeperConstant.Keys.ZOOKEEPER_COMMAND_EXECUTION_LOG_PATH
			// + ".log");
			// String commandExecLogsFilePath =
			SSHExec connection = nodeConfig.getConnection();
			// if connected
			if (connection != null) {
				// String cmd = this.compConf.getHomeDir() + "bin/zkServer.sh"
				// + Strings.SPACE + action;
				// Result result;
				// AnkushTask runCmd = new RunInBackground(cmd,
				// commandExecLogsFilePath.getAbsolutePath());
				// result = connection.exec(runCmd);
				// if (statusFlag = (result.rc != 0)) {
				// throw new AnkushException(result.error_msg);
				// }
				StringBuilder zookeeperCommandString = new StringBuilder(
						compConf.getHomeDir()).append(
						ZookeeperConstant.Keys.relPath_ZkServerScript).append(
						action);
				CustomTask zooActionCommand = new ExecCommand(
						zookeeperCommandString.toString());
				Result result = connection.exec(zooActionCommand);
				if (result.rc != 0) {
					statusFlag = false;
					throw new AnkushException(result.error_msg);
				} else {
					System.out.println("result.sysout : " + result.sysout);
					if (result.sysout
							.contains("Starting zookeeper ... already running")) {
						throw new AnkushException(result.sysout);
					}
					statusFlag = true;
				}
			} else {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
		} catch (AnkushException e) {
			logger.error(e.getMessage(), componentName, nodeConfig.getHost(), e);
			errors.add(e.getMessage());
		} catch (Exception e) {
			logger.error(
					Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG,
					componentName, nodeConfig.getHost(), e);
			errors.add(Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG);
		}
		return statusFlag;
	}

	@Override
	public Set<String> getServiceList(ClusterConfig clusterConfig) {
		return new HashSet<String>(Arrays.asList(Constant.Role.ZOOKEEPER));
	}

	@Override
	public boolean startRole(ClusterConfig clusterConfig, String role) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageClusterService(Constant.ServiceAction.START);
	}

	@Override
	public boolean stopRole(ClusterConfig clusterConfig, String role) {
		if (!this.setCluster(clusterConfig)) {
			return false;
		}
		return manageClusterService(Constant.ServiceAction.STOP);
	}

	@Override
	public String getLogDirPath(ClusterConfig clusterConfig, String host,
			String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogFilesRegex(ClusterConfig clusterConfig,
			String host, String role, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}
}
