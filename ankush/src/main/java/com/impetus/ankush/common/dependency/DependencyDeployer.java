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
package com.impetus.ankush.common.dependency;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddToPathVariable;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.SetEnvVariable;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.FileNameUtils.ONSFileType;

/**
 * It is used the deploy the dependencies on nodes.
 * 
 * @author hokam chauhan
 * 
 */
public class DependencyDeployer implements Deployable {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			Constant.Component.Name.DEPENDENCY, DependencyDeployer.class);

	/** The repo path. */
	private static final String repoPath = AppStoreWrapper.getServerRepoPath();

	/** The ankush common. */
	private static final String ankushCommon = ".ankush/common/";

	private Map<String, Boolean> statusMap = new HashMap<String, Boolean>();

	/**
	 * Deploy.
	 * 
	 * @param config
	 *            Dependency Configuration object
	 * @return The status of dependency installation on all nodes.
	 * @author hokam chauhan
	 */
	@Override
	public boolean deploy(Configuration config) {
		final DependencyConf conf = (DependencyConf) config;
		logger.setLoggerConfig(conf);

		boolean status = true;
		try {
			for (String component : conf.getComponents().keySet()) {
				GenericConfiguration compConf = (GenericConfiguration) conf
						.getComponents().get(component);
				status = status
						&& installDependencyOnNodes(conf, component,
								compConf.getCompNodes());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return status;
	}

	private boolean installDependencyOnNodes(final DependencyConf conf,
			final String component, Collection<NodeConf> nodes)
			throws InterruptedException {

		final Semaphore nodeSemaphore = new Semaphore(nodes.size());
		for (final NodeConf nodeConf : nodes) {

			nodeSemaphore.acquire();

			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					boolean isInstalled = installDependency(nodeConf, conf,
							component);
					nodeConf.setStatus(isInstalled);
					if (nodeSemaphore != null) {
						nodeSemaphore.release();
					}
				}
			});
		}
		nodeSemaphore.acquire(nodes.size());
		return status(nodes);
	}

	/**
	 * Status.
	 * 
	 * @param keySet
	 *            the key set
	 * @return true, if successful
	 */
	private boolean status(Collection<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = status && nodeConf.getStatus();
		}
		return status;
	}

	/**
	 * Install dependency.
	 * 
	 * @param nodeConf
	 *            Node on which you want to install dependencies
	 * @param conf
	 *            Config object which contains username and password for ssh
	 *            connection
	 * @param componentName
	 *            Name of the component for which you want to install
	 *            dependencies
	 * @return Overall status about dependency installation
	 * @author hokam chauhan
	 */
	private boolean installDependency(NodeConf nodeConf, DependencyConf conf,
			String componentName) {

		SSHExec connection = null;
		logger.info(nodeConf.getPublicIp(), "Installing " + componentName
				+ " dependencies...");

		// Setting the start progress message for activity startup.
		try {
			/* Get OS name */
			String osName = null;

			String password = conf.getPassword();
			if (password == null || password.equals("")) {
				osName = SSHUtils.getOS(nodeConf.getPublicIp(),
						conf.getUsername(), conf.getPrivateKey(), false);
			} else {
				osName = SSHUtils.getOS(nodeConf.getPublicIp(),
						conf.getUsername(), password, true);
			}

			if (osName == null) {
				osName = "Ubuntu";
				// Currently handled by returning // ubuntu if not possible to
				// detect // os name.
			}
			logger.debug("OS detected by SSHUtils :" + osName);

			// setting os in node conf.
			nodeConf.setOs(osName);

			// Connection with node via SSH connection
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), password, conf.getPrivateKey());

			if (connection != null) {
				// install java
				boolean status = true;
				boolean installJava = conf.getInstallJava();
				if (installJava
						&& !statusMap.containsKey(nodeConf.getPublicIp())) {

					// configure java installation
					String binFile = conf.getJavaBinFileName();

					// Location of java binary at Ankush Server
					final String javabinFilePath = repoPath + binFile;
					String destinationFolderName = FileNameUtils
							.getExtractedDirectoryName(javabinFilePath);

					// destination JAVA_HOME path
					final String javaInstallationPath = CommonUtil
							.getUserHome(conf.getUsername()) + ankushCommon;

					// destination file
					final String jdkbin = javaInstallationPath + binFile;

					// JAVA_HOME
					final String newJavaHomePath = javaInstallationPath
							+ destinationFolderName;
					// $JAVA_HOME/bin in $PATH
					final String newJavaBinInPath = newJavaHomePath + "bin";

					// install java : extract bin & set env vars
					status = this.installJava(connection, password,
							javaInstallationPath, javabinFilePath, jdkbin,
							newJavaHomePath, newJavaBinInPath, nodeConf);

					// putting it in status installed java.
					statusMap.put(nodeConf.getPublicIp(), status);

				}

				if (status) {
					// Getting the dependency file name for the fetched os
					// name
					String dependenctFileName = FileNameUtils
							.getDependencyFileName(osName, componentName);

					File file = new File(dependenctFileName);

					if (file.exists()) {
						// Getting list of dependency installation commands
						// from dependency file.
						List<String> depListCmds = FileUtils
								.loadLines(dependenctFileName);

						for (String installCmd : depListCmds) {
							if (!installCmd.isEmpty()) {
								// Executing the dependency installation
								// command.
								CustomTask execTask = new ExecSudoCommand(
										password, installCmd);

								Result rs = connection.exec(execTask);

								if (rs.rc != 0) {
									throw new AnkushException(installCmd
											+ " failed.");
								}
							}
						}
					}
				}
				// Setting the dependency installation status to node status
				nodeConf.setStatus(status);
			} else {
				throw new AnkushException("Authentication failed.");
			}
		} catch (Exception e) {
			logger.error(nodeConf, e.getMessage());
			nodeConf.setStatus(false);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		// Setting the end progress status for execution status
		logger.log(nodeConf.getPublicIp(), "Installing " + componentName
				+ " dependencies", nodeConf.getStatus());
		return nodeConf.getStatus();
	}

	/**
	 * Install java.
	 * 
	 * @param connection
	 *            the connection
	 * @param password
	 *            the password
	 * @param installationPath
	 *            the installation path
	 * @param javabinFilePath
	 *            the javabin file path
	 * @param jdkbin
	 *            the jdkbin
	 * @param newJavaHomePath
	 *            the new java home path
	 * @param newJavaBinInPath
	 *            the new java bin in path
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	private boolean installJava(SSHExec connection, final String password,
			final String installationPath, final String javabinFilePath,
			final String jdkbin, final String newJavaHomePath,
			final String newJavaBinInPath, NodeConf nodeConf) {
		
		
		ONSFileType fileType = FileNameUtils.getFileType(javabinFilePath);
		
		boolean status = true;
		String msg = "Installing java";
		try {
			logger.info(nodeConf.getPublicIp(), msg + "...");

			Result res = null;
			// if connected
			if (connection != null) {
				logger.debug("Create directory - " + installationPath);

				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						installationPath);
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					logger.error(nodeConf,
							"Could not create installation directory "
									+ installationPath);
				}

				// upload java binary
				connection.uploadSingleDataToServer(javabinFilePath, jdkbin);

				// execute command
				if(fileType == ONSFileType.BIN){
					CustomTask cmd = new ExecCommand("cd " + installationPath,
							"echo /r/n/r/n | sh " + jdkbin);
					res = connection.exec(cmd);
				}else if(fileType == ONSFileType.TAR_GZ||fileType == ONSFileType.GZ){
					CustomTask cmd = new ExecCommand("cd " + installationPath,
							"tar -xzvf " + jdkbin);
					res = connection.exec(cmd);
				}else if(fileType == ONSFileType.ZIP){
					CustomTask cmd = new ExecCommand("cd " + installationPath,
							"unzip " + jdkbin);
					res = connection.exec(cmd);
				}
				
				// if successfully extracted, set env vars
				if (res.rc == 0) {
					// set JAVA_HOME
					AnkushTask setJavaHome = new SetEnvVariable(password,
							"JAVA_HOME", newJavaHomePath, "/etc/environment");

					// Add java to path
					AnkushTask addToPath = new AddToPathVariable(
							newJavaBinInPath, "/etc/environment", password);

					res = connection.exec(setJavaHome);
					if (res.rc == 0) {
						res = connection.exec(addToPath);
						if (res.rc == 0) {
							status = true;
						} else {
							msg = "Could not add $JAVA_HOME/bin in $PATH";
							status = false;
						}
					} else {
						msg = "Could not set $JAVA_HOME";
						status = false;
					}

				} else {
					msg = "Could not extract/unpack java bin";
					status = false;
				}
			}
		} catch (Exception e) {
			logger.error(nodeConf, e.getMessage());
			status = false;
		}
		nodeConf.setStatus(status);
		logger.log(nodeConf, msg);
		return status;
	}

	/**
	 * Undeploy.
	 * 
	 * @param config
	 *            the config
	 * @return undeploy status
	 * @see @see
	 *      com.impetus.ankush.common.deployment.Deployable#undeploy(Configuration
	 *      )
	 */
	@Override
	public boolean undeploy(Configuration config) {
		return true;
	}

	/**
	 * Start.
	 * 
	 * @param config
	 *            the config
	 * @return start status
	 * @see @see
	 *      com.impetus.ankush.common.deployment.Deployable#start(Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		return true;
	}

	/**
	 * Stop.
	 * 
	 * @param config
	 *            the config
	 * @return stop status
	 * @see @see
	 *      com.impetus.ankush.common.deployment.Deployable#stop(Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
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
	public boolean addNodes(List<NodeConf> nodes, Configuration config) {

		DependencyConf conf = (DependencyConf) config;
		logger.setLoggerConfig(conf);

		boolean status = true;
		try {
			for (String component : conf.getComponents().keySet()) {
				GenericConfiguration compConf = (GenericConfiguration) conf
						.getComponents().get(component);
				status = status
						&& installDependencyOnNodes(conf, component,
								compConf.getNewNodes());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
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
		// TODO Auto-generated method stub
		DependencyConf conf = (DependencyConf) config;
		logger.setLoggerConfig(conf);
		return true;
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
		// TODO Auto-generated method stub
		DependencyConf conf = (DependencyConf) config;
		logger.setLoggerConfig(conf);
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
		// TODO Auto-generated method stub
		DependencyConf conf = (DependencyConf) config;
		logger.setLoggerConfig(conf);
		return true;
	}
}
