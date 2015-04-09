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
package com.impetus.ankush2.preprocessor;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.utils.ObjectFactory;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;
import com.impetus.ankush2.utils.SSHUtils;

public class DependencyDeployer extends AbstractDeployer {

	private ClusterConfig clusterConfig;

	/** DependencyDeployer logger */
	private AnkushLogger logger = new AnkushLogger(DependencyDeployer.class);

	private boolean addClusterError(String error, Throwable t) {
		return addClusterError(error, null, t);
	}

	@Override
	public String getComponentName() {
		return Constant.Component.Name.DEPENDENCY;
	}

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
	public boolean deploy(ClusterConfig conf) {
		logger.info("Installing dependencies for cluster deployment.",
				getComponentName());
		try {
			return installDependencies(conf);
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError("Exception while deploying dependencies", e);
		}
	}

	private boolean installDependencies(ClusterConfig clusterConfig)
			throws AnkushException {
		boolean status = true;
		try {
			for (String component : clusterConfig.getComponents().keySet()) {
				if (!component.equalsIgnoreCase(Constant.Component.Name.AGENT)) {
					Set<String> nodesForDependenciesDeployment = ObjectFactory
							.getDeployerObject(component)
							.getNodesForDependenciesDeployment(clusterConfig);
					if (nodesForDependenciesDeployment == null) {
						continue;
					}
					status = installDependencyOnNodes(component,
							nodesForDependenciesDeployment, clusterConfig)
							&& status;
				}
			}
			return status;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"Exception while installing dependencies on nodes"
							+ getExceptionString(e), e);
		}
	}

	@Override
	public boolean addNode(ClusterConfig conf, ClusterConfig newConf) {
		try {
			this.clusterConfig = conf;
			this.logger.setCluster(clusterConfig);
			return installDependencies(newConf);
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError("Could not add node to "
					+ getComponentName() + " cluster" + getExceptionString(e),
					e);
		}
	}

	private boolean installDependencyOnNodes(final String component,
			Set<String> nodes, final ClusterConfig clusterConfig)
			throws AnkushException {
		try {
			logger.info("Installing " + component + " dependencies",
					getComponentName());
			final Semaphore nodeSemaphore = new Semaphore(nodes.size());
			for (final String node : nodes) {
				nodeSemaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						clusterConfig
								.getNodes()
								.get(node)
								.setStatus(
										installDependency(node, component,
												clusterConfig));
						if (nodeSemaphore != null) {
							nodeSemaphore.release();
						}
					}
				});
			}
			nodeSemaphore.acquire(nodes.size());
			logger.info("Installing " + component + " dependencies done.",
					getComponentName());
			return AnkushUtils.getStatus(clusterConfig.getNodes());
		} catch (Exception e) {
			throw new AnkushException(
					"Exception while creating threads for installing dependencies"
							+ getExceptionString(e), e);
		}
	}

	private boolean installDependency(String node, String componentName,
			ClusterConfig clusterConfig) {
		try {
			logger.info("Installing " + componentName + " dependencies on "
					+ node, getComponentName(), node);
			SSHExec connection = getConnection(node, clusterConfig);
			String password = getPassword();
			// Getting the OS Name of the Node.
			String osName = SSHUtils.getOS(connection, node);
			if (osName == null || osName.isEmpty()) {
				throw new AnkushException("Could not identify OS details for "
						+ node + ".");
			}
			// Getting the dependency file name for the fetched os
			// name
			String dependencyFileName = FileNameUtils.getDependencyFileName(
					osName, componentName);
			File file = new File(dependencyFileName);
			if (file.exists()) {
				// Getting list of dependency installation commands
				// from dependency file.
				List<String> depListCmds = FileUtils
						.loadLines(dependencyFileName);
				for (String installCmd : depListCmds) {
					if (!installCmd.isEmpty()) {
						// Executing the dependency installation
						// command.
						CustomTask execTask = new ExecSudoCommand(password,
								installCmd);
						Result rs = connection.exec(execTask);
						if (rs.rc != 0) {
							throw new AnkushException(installCmd + " failed.");
						}
					}
				}
			}
			logger.info("Installing " + componentName + " dependencies on "
					+ node + " done.", getComponentName(), node);
			return true;
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), node, e);
		} catch (Exception e) {
			return addClusterError(
					"Exception while installing dependencies on node"
							+ getExceptionString(e), node, e);
		}
	}

	/**
	 * Provides node's connection object
	 * 
	 * @param host
	 *            {@link String}
	 * @return {@link SSHExec}
	 */
	private SSHExec getConnection(String host, ClusterConfig clusterConfig)
			throws AnkushException {
		try {
			return clusterConfig.getNodes().get(host).getConnection();
		} catch (Exception e) {
			throw new AnkushException(
					"Exception while getting node's connection object.");
		}
	}

	private String getPassword() {
		return clusterConfig.getAuthConf().getPassword() != null ? clusterConfig
				.getAuthConf().getPassword() : null;
	}

	private String getExceptionString(Exception e) {
		return e.getMessage() != null ? ": " + e.getMessage() : "";
	}

	@Override
	public boolean createConfig(ClusterConfig conf) {

		try {
			this.clusterConfig = conf;
			this.logger.setCluster(clusterConfig);
			return true;
		} catch (Exception e) {
			logger.error(
					"Exception while creating configuration for dependencies deployment",
					getComponentName());
			return false;
		}
	}

}
