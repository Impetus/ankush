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
package com.impetus.ankush.common.ganglia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.aspectj.util.FileUtil;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.Symbols;

/**
 * It is deployer class for deployment of ganglia on nodes.
 * 
 * @author Hokam Chauhan
 * 
 */
public class Ganglia2Deployer implements Deployable {

	/** The Constant DOTS. */
	private static final String DOTS = "...";

	/** The Constant GMETAD_STOP_COMMAND. */
	private static final String GMETAD_STOP_COMMAND = "killall -9 gmetad";

	/** The Constant GMOND_STOP_COMMAND. */
	private static final String GMOND_STOP_COMMAND = "killall -9 gmond";

	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.GANGLIA, Ganglia2Deployer.class);

	/**
	 * Method deploy.
	 * 
	 * @param config
	 *            Configuration
	 * @return boolean
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		GangliaConf conf = (GangliaConf) config;
		// setting logger config.
		LOG.setLoggerConfig(conf);
		// logging startup message.
		String message = "Deploying ganglia";
		LOG.info(message + DOTS);
		// deploying ganglia on nodes.
		boolean status = deployNodes(conf, conf.getGmondNodes());
		// logging end message.
		LOG.log(message, status);
		return status;
	}

	/**
	 * Deploy nodes.
	 * 
	 * @param conf
	 *            the conf
	 * @param gmondNodes
	 *            the gmond nodes
	 * @return true, if successful
	 */
	private boolean deployNodes(final GangliaConf conf, Set<NodeConf> gmondNodes) {
		final Semaphore semaphore = new Semaphore(gmondNodes.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : gmondNodes) {
				// acuiring the semaphore
				semaphore.acquire();
				// starting a thread for installing ganglia on node.
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
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(gmondNodes.size());
			status = status(gmondNodes);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            Set<NodeConf>
	 * @return boolean
	 */
	private boolean status(Set<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = status && nodeConf.getStatus();
		}
		return status;
	}

	/**
	 * Method undeploy.
	 * 
	 * @param config
	 *            Configuration
	 * @return boolean
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		final GangliaConf conf = (GangliaConf) config;
		LOG.setLoggerConfig(conf);

		final Semaphore semaphore = new Semaphore(conf.getGmondNodes().size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : conf.getGmondNodes()) {
				// acuiring the semaphore.
				semaphore.acquire();
				// starting a thread for ganglia removal on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = removeNode(nodeConf, conf);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(conf.getGmondNodes().size());
			status = status(conf.getGmondNodes());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Method start.
	 * 
	 * @param config
	 *            Configuration
	 * @return boolean
	 * @see com.impetus.ankush.common.framework.Deployable#start(Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		GangliaConf conf = (GangliaConf) config;
		LOG.setLoggerConfig(conf);

		// starting gmetad nodes.
		for (NodeConf nodeConf : conf.getGmondNodes()) {
			if (conf.isGmetadNode(nodeConf)) {
				nodeConf.setStatus(startNode(nodeConf, conf));
				break;
			}
		}

		// starting gmond nodes.
		for (NodeConf nodeConf : conf.getGmondNodes()) {
			if (!conf.isGmetadNode(nodeConf)) {
				nodeConf.setStatus(startNode(nodeConf, conf));
			}
		}

		return status(conf.getGmondNodes());
	}

	/**
	 * Method stop.
	 * 
	 * @param config
	 *            Configuration
	 * @return boolean
	 * @see com.impetus.ankush.common.framework.Deployable#stop(Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		final GangliaConf conf = (GangliaConf) config;
		LOG.setLoggerConfig(conf);
		// creating semaphore object.
		final Semaphore semaphore = new Semaphore(conf.getGmondNodes().size());
		boolean status = false;
		try {
			// iterating over all the nodes.
			for (final NodeConf nodeConf : conf.getGmondNodes()) {

				// acuiring the semaphore for stopping one node.
				semaphore.acquire();
				// starting a thread for stopping ganglia on node.
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = stopNode(nodeConf, conf);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(conf.getGmondNodes().size());
			status = status(conf.getGmondNodes());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Method startNode.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            GangliaConf
	 * @return boolean
	 */
	private boolean startNode(NodeConf nodeConf, GangliaConf conf) {

		String message = "Starting ganglia";
		// Setting the start progress message for activity startup.
		LOG.info(nodeConf.getPublicIp(), message + DOTS);

		String startGmondCmd = "gmond --conf=" + conf.getInstallationPath()
				+ "conf/gmond.conf";
		String startGmetadCmd = "gmetad --conf=" + conf.getInstallationPath()
				+ "conf/gmetad.conf";

		SSHExec connection = null;
		try {
			// Connection with node via SSH connection
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			nodeConf.setStatus(false);
			if (connection != null) {
				if (conf.isGmetadNode(nodeConf)) {
					// Starting the gmetad process on node
					nodeConf.setStatus(SSHUtils.action(conf.getPassword(),
							connection, startGmetadCmd));
				}
				// Starting the gmond process on node
				nodeConf.setStatus(SSHUtils.action(conf.getPassword(),
						connection, startGmondCmd));
				// Starting the gmond process on node
				nodeConf.setStatus(SSHUtils.action(conf.getPassword(),
						connection, GMOND_STOP_COMMAND));
				// Starting the gmond process on node
				nodeConf.setStatus(SSHUtils.action(conf.getPassword(),
						connection, startGmondCmd));
			} else {
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			LOG.error(nodeConf, e.getMessage(), e);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}

		}

		// Setting the end progress status for execution status
		LOG.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());

		return nodeConf.getStatus();
	}

	/**
	 * Method stopNode.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            GangliaConf
	 * @return boolean
	 */
	private boolean stopNode(NodeConf nodeConf, GangliaConf conf) {

		String message = "Stopping ganglia";
		// // Setting the start progress message for activity startup.
		LOG.info(nodeConf.getPublicIp(), message + DOTS);

		SSHExec connection = null;
		try {
			// Connection with node via SSH connection
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			nodeConf.setStatus(false);
			if (connection != null) {
				// Stopping the gmond process on node
				nodeConf.setStatus(SSHUtils.action(conf.getPassword(),
						connection, GMOND_STOP_COMMAND));

				if (conf.isGmetadNode(nodeConf)) {
					// Stopping the gmetad process on node
					nodeConf.setStatus(SSHUtils.action(conf.getPassword(),
							connection, GMETAD_STOP_COMMAND));
				}
			} else {
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			LOG.error(nodeConf, e.getMessage(), e);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}

		// Setting the end progress status for execution status
		LOG.log(nodeConf.getPublicIp(), message, true);

		return true;
	}

	/**
	 * Method createNode.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            GangliaConf
	 * @return boolean
	 */
	private boolean createNode(NodeConf nodeConf, GangliaConf conf) {
		SSHExec connection = null;
		boolean status = false;

		// Setting the start progress message for activity startup.
		String message = "Deploying ganglia";
		LOG.info(nodeConf.getPublicIp(), message + DOTS);

		try {
			// Connection with node via SSH connection
			String password = conf.getPassword();
			String publicIp = nodeConf.getPublicIp();
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					password, conf.getPrivateKey());

			if (connection != null) {
				LOG.debug("Create directory - " + conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());

				// make conf directory on node
				AnkushTask mkCompConfPath = new MakeDirectory(
						conf.getInstallationPath() + "conf/");

				status = connection.exec(mkInstallationPath).isSuccess
						&& connection.exec(mkCompConfPath).isSuccess;

				if (!status) {
					LOG.error(
							nodeConf,
							"Could not create directory - "
									+ conf.getInstallationPath());
				}

				// if Directory created on nodes.
				if (status) {
					status = installGanglia(nodeConf, conf, connection);

					if (status) {
						// setting installed ganglia version in ganglia
						// conf.
						setInstalledGangliaVersion(nodeConf, conf);

						// creging gmond configuration on node.
						status = createGmondConfigurationFile(nodeConf, conf,
								connection);

						// if gmetad node.
						if (status && conf.isGmetadNode(nodeConf)) {
							// creating gmetad configuration file.
							status = createGmetadConfigurationFile(nodeConf,
									conf, connection);
							if (status) {
								// creting rrd directories.
								status = createRRDDirectories(nodeConf, conf,
										connection);
							}
						}
						nodeConf.setStatus(status);
						if (status) {
							addProcessName(nodeConf, conf, connection);
						}
					}
					nodeConf.setStatus(status);
				}
			} else {
				LOG.error(nodeConf, "Authentication failed");
				nodeConf.setStatus(status);
			}
		} catch (Exception e) {
			status = false;
			String error = e.getMessage();
			if (error == null) {
				if (e.getCause() != null) {
					error = e.getCause().getMessage();
				} else {
					error = "Configuring ganglia failed.";
				}
			}
			LOG.error(nodeConf, error, e);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}
		nodeConf.setStatus(status);

		// Setting the end progress status for execution status
		LOG.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());

		return nodeConf.getStatus();
	}

	/**
	 * Method to add process name in the agent.properties file.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 */
	private void addProcessName(NodeConf nodeConf, GangliaConf conf,
			SSHExec connection) {

		String userHome = CommonUtil.getUserHome(conf.getUsername());
		String agentFile = userHome + "/.ankush/agent/conf/agent.properties";
		String processList = "gmond";

		if (conf.isGmetadNode(nodeConf)) {
			processList = processList + ",gmetad";
		}
		String appendStr = "GANGLIA_PROCESS_LIST=" + processList;
		CustomTask task = new AppendFile(appendStr, agentFile);
		try {
			connection.exec(task);
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf, error, e);
		}
	}

	/**
	 * Install ganglia.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws TaskExecFailException
	 *             the task exec fail exception
	 */
	private boolean installGanglia(NodeConf nodeConf, GangliaConf conf,
			SSHExec connection) throws IOException, TaskExecFailException {

		String authInfo = null;
		String password = conf.getPassword();
		boolean authUsingPassword = (password != null && !password.isEmpty());
		if (authUsingPassword) {
			authInfo = password;
		} else {
			authInfo = conf.getPrivateKey();
		}
		boolean status = true;

		// Getting the Os Name of the Node.
		String osName = SSHUtils.getOS(nodeConf.getPublicIp(),
				conf.getUsername(), authInfo, authUsingPassword);

		String comoponentName = "gmond";
		if (conf.isGmetadNode(nodeConf)) {
			comoponentName = "gmetad";
		}
		String message = "Installing ganglia";
		LOG.info(nodeConf.getPublicIp(), message + DOTS);
		/* Getting the installer file name of the component. */
		String installerFilePath = FileNameUtils.getInstallerFilePath(osName,
				comoponentName);

		/* Getting list of installation commands from the file. */
		List<String> depListCmds = FileUtils.readLines(new File(
				installerFilePath));

		/* Iterating over the command lists. */
		for (String installCmd : depListCmds) {
			if (!installCmd.isEmpty()) {
				/* Executing the installation command. */
				CustomTask execTask = new ExecSudoCommand(conf.getPassword(),
						installCmd);

				Result rs = connection.exec(execTask);

				if (rs.rc != 0) {
					LOG.error(nodeConf, installCmd + " failed.");
					status = false;
					break;
				}
			}
		}
		nodeConf.setStatus(status);
		LOG.log(nodeConf.getPublicIp(), message, status);
		return status;
	}

	/**
	 * Set the Installed Ganglia Version to the Config value.
	 * 
	 * @param nodeConf
	 *            The NodeConf Object.
	 * @param conf
	 *            the conf
	 * @throws Exception
	 *             the exception
	 */
	private void setInstalledGangliaVersion(NodeConf nodeConf, GangliaConf conf)
			throws Exception {

		// Command to show the installed version of gmond
		String command = "gmond -V";

		if (conf.isGmetadNode(nodeConf)) {
			// Command to show the installed version of gmetad
			command = "gmetad -V";
		}

		String authInfo = conf.getPassword();

		boolean authUsingPassword = (authInfo != null && !authInfo.isEmpty());
		if (!authUsingPassword) {
			authInfo = conf.getPrivateKey();
		}

		try {
			/* Executing the command to get the output. */
			String output = SSHUtils.getCommandOutput(command,
					nodeConf.getPublicIp(), conf.getUsername(), authInfo,
					authUsingPassword);
			if (output != null) {
				String vesrion = output.split(" ")[1];
				conf.setComponentVersion(vesrion);
			}
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf, error, e);
		}

	}

	/**
	 * Creates the rrd directories.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 * @return true, if successful
	 */
	private boolean createRRDDirectories(NodeConf nodeConf, GangliaConf conf,
			SSHExec connection) {
		boolean status;
		// Creating ganglia directories on node
		String mkRRDFileDir = "mkdir -p " + conf.getRrdFilePath();
		String mkDwooFileDir = "mkdir -p " + conf.getDwooFilePath();
		String chmodDwooTo777 = "chmod 777 /var/lib/ganglia/dwoo/";
		String chownGanglilaToNobody = "chown -R nobody /var/lib/ganglia/";

		// logging startup message.
		String message = "Creating ganglia directories";
		LOG.info(nodeConf.getPublicIp(), message + DOTS);

		// executing the directory creation commands.
		status = SSHUtils.action(conf.getPassword(), connection, mkRRDFileDir)
				&& SSHUtils.action(conf.getPassword(), connection,
						mkDwooFileDir)
				&& SSHUtils.action(conf.getPassword(), connection,
						chmodDwooTo777)
				&& SSHUtils.action(conf.getPassword(), connection,
						chownGanglilaToNobody);

		// setting status.
		nodeConf.setStatus(status);
		// settting log.
		LOG.log(nodeConf, message);
		return status;
	}

	/**
	 * Creates the gmetad configuration file.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 * @return true, if successful
	 * @throws Exception
	 *             the task exec fail exception
	 */
	private boolean createGmetadConfigurationFile(NodeConf nodeConf,
			GangliaConf conf, SSHExec connection) throws Exception {

		// gmetad conf path
		String gmetadConfPath = conf.getInstallationPath() + "conf/gmetad.conf";
		boolean status = false;

		// logging startup message.
		String message = "Configuring gmetad file";
		LOG.info(nodeConf.getPublicIp(), message + DOTS);

		try {
			// creating configuration write commands.
			CustomTask clearFile = new ClearFile(gmetadConfPath);
			String fileContent = getGmetadConfigurationContent(conf)
					.replaceAll("\\\"", "\\\\\"");
			CustomTask appendFile = new AppendFile(fileContent, gmetadConfPath);

			// executing configuration write command.
			status = ((connection.exec(clearFile).rc == 0) && (connection
					.exec(appendFile).rc == 0));
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf, error, e);
			status = false;
		}

		// setting status.
		nodeConf.setStatus(status);
		// logging end message
		LOG.log(nodeConf, message);
		return status;
	}

	/**
	 * Creates the gmond configuration file.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 * @return true, if successful
	 * @throws TaskExecFailException
	 *             the task exec fail exception
	 */
	private boolean createGmondConfigurationFile(NodeConf nodeConf,
			GangliaConf conf, SSHExec connection) throws TaskExecFailException {

		String gmondConfPath = conf.getInstallationPath() + "conf/gmond.conf";
		boolean status = false;
		// Create the gmond configuration on node

		String message = "Configuring gmond file";
		LOG.info(nodeConf.getPublicIp(), message + DOTS);

		try {
			CustomTask clearFile = new ClearFile(gmondConfPath);
			String fileContent = getGmondConfigurationContent(nodeConf, conf)
					.replaceAll("\\\"", "\\\\\"");
			CustomTask appendFile = new AppendFile(fileContent, gmondConfPath);

			status = ((connection.exec(clearFile).rc == 0) && (connection
					.exec(appendFile).rc == 0));
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf, error, e);
			status = false;
		}

		nodeConf.setStatus(status);
		LOG.log(nodeConf, message);
		return status;
	}

	/**
	 * Method removeNode.
	 * 
	 * @param nodeConf
	 *            NodeConf
	 * @param conf
	 *            GangliaConf
	 * @return boolean
	 */
	private boolean removeNode(NodeConf nodeConf, GangliaConf conf) {

		String message = "Removing ganglia";
		// Setting the start progress message for activity remove.
		LOG.info(nodeConf.getPublicIp(), message + DOTS);

		List<String> rmCmds = new ArrayList<String>();
		rmCmds.add("rm " + conf.getInstallationPath() + " -fr");
		rmCmds.add("rm " + conf.getRrdFilePath() + conf.getClusterName()
				+ " -fr");

		SSHExec connection = null;
		try {
			// Connection with node via SSH connection
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			if (connection != null) {
				// RemoveText prepFile = new RemoveText(getEtcHostsEntries(
				// nodeConf, conf), "/etc/hosts", conf.getPassword());
				// connection.exec(prepFile);
				// Remove the ganglia specific directories and binary files.
				CustomTask execTask = new ExecSudoCommand(conf.getPassword(),
						rmCmds.toArray(new String[rmCmds.size()]));

				// Setting status of the execution to the node status.
				nodeConf.setStatus(connection.exec(execTask).rc == 0);
			}
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null) {
				error = e.getCause().getMessage();
			}
			LOG.error(nodeConf.getPublicIp(), error, e);
		} finally {
			// Disconnecting the ssh connection
			if (connection != null) {
				connection.disconnect();
			}
		}

		// Setting the end progress status for execution status
		LOG.log(nodeConf.getPublicIp(), message, nodeConf.getStatus());

		return nodeConf.getStatus();
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
		// Create HashSet with List.
		Set<NodeConf> nodes = new HashSet<NodeConf>(nodeList);
		// Create Ganglia conf.
		GangliaConf conf = (GangliaConf) config;
		// Set LOG configs.
		LOG.setLoggerConfig(conf);

		String message = "Adding ganglia";
		// log message.
		LOG.info(message + DOTS);

		// create ganglia nodes.
		boolean status = deployNodes(conf, nodes);

		// log end message
		LOG.log(message, status);
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
		// Create HashSet with List.
		Set<NodeConf> nodes = new HashSet<NodeConf>(nodeList);
		// Create Ganglia conf.
		GangliaConf conf = (GangliaConf) config;
		// Set LOG configs.
		LOG.setLoggerConfig(conf);

		String message = "Removing ganglia";
		// log message.
		LOG.info(message + DOTS);

		// removing nodes.
		for (NodeConf nodeConf : nodes) {
			nodeConf.setStatus(removeNode(nodeConf, conf));
		}
		boolean status = status(nodes);
		// log end message
		LOG.log(message, status);
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
		// Create HashSet with List.
		Set<NodeConf> nodes = new HashSet<NodeConf>(nodeList);
		// Create Ganglia conf.
		GangliaConf conf = (GangliaConf) config;
		// Set LOG configs.
		LOG.setLoggerConfig(conf);
		String message = "Starting ganglia";
		// log message.
		LOG.info(message + DOTS);

		// starting nodes.
		for (NodeConf nodeConf : nodes) {
			nodeConf.setStatus(startNode(nodeConf, conf));
		}
		boolean status = status(nodes);
		// log end message
		LOG.log(message, status);
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
		// Create HashSet with List.
		Set<NodeConf> nodes = new HashSet<NodeConf>(nodeList);
		// Create Ganglia conf.
		GangliaConf conf = (GangliaConf) config;
		// Set LOG configs.
		LOG.setLoggerConfig(conf);

		String message = "Stopping ganglia";
		// log message.
		LOG.info(message + DOTS);

		// stopping nodes.
		for (NodeConf nodeConf : nodes) {
			nodeConf.setStatus(stopNode(nodeConf, conf));
		}
		boolean status = status(nodes);
		// log end message
		LOG.log(message, status);
		return status;
	}

	/**
	 * Generate The Content Of The Gmetad Configuration File.
	 * 
	 * @return The Gmetad Configuration File Content
	 * @throws Exception
	 */
	public String getGmetadConfigurationContent(GangliaConf conf)
			throws Exception {

		String fileContent = null;
		Map<String, Object> configValues = getConfigValueMap(conf);
		String gmetadFileName = conf.getServerConfFolder() + "gmetad-all.conf";
		StringBuffer nodeIpPorts = new StringBuffer();

		// Preparing a String of nodeIp:port of gmetad node.
		nodeIpPorts.append(conf.getGmetadNode().getPrivateIp()).append(
				Symbols.STR_COLON);
		nodeIpPorts.append(conf.getPort());

		// Putting the nodeIpsPorts string in map
		configValues.put("nodeIpsPorts", nodeIpPorts.toString());

		// Reading the content of the template file
		fileContent = FileUtil.readAsString(new File(gmetadFileName));

		// Creating a string substitutor using config values map
		StrSubstitutor sub = new StrSubstitutor(configValues);

		// Replacing the config values key found in the file content with
		// respected values.
		fileContent = sub.replace(fileContent);
		return fileContent;
	}

	/**
	 * Generate The Content Of The Gmetad Configuration File.
	 * 
	 * @param nodeConf
	 *            The NodeConf for which we
	 * @return The Gmetad Configuration File Content
	 * @throws Exception
	 */
	public String getGmondConfigurationContent(NodeConf nodeConf,
			GangliaConf conf) throws Exception {
		String fileContent = null;
		String gmondFileName = null;

		Map<String, Object> configValues = getConfigValueMap(conf);

		// Getting the gmond template file for unicast mode
		gmondFileName = (String) configValues.get("gmondUCastTemplate");

		// Putting the node ip as gmetad node private ip
		configValues.put("nodeIP", conf.getGmetadNode().getPrivateIp());

		String udpRecvChannel = "udp_recv_channel {\n port = "
				+ configValues.get("port") + " \n } ";

		if (conf.isGmetadNode(nodeConf)) {
			// On Gmetad node using udp_recv_channel block
			configValues.put("udp_recv_channel", udpRecvChannel);
		} else {
			// On gmond nodes other than Gmetad node commenting
			// udp_recv_channel block
			configValues.put("udp_recv_channel", "/*" + udpRecvChannel + "*/");
		}

		// Reading the content of the template file
		fileContent = FileUtil.readAsString(new File(gmondFileName));

		// Creating a string substitutor using config values map
		StrSubstitutor sub = new StrSubstitutor(configValues);

		// Replacing the config values key found in the file content with
		// respected values.
		return sub.replace(fileContent);
	}

	/**
	 * Create a map for configuration generation.
	 * 
	 * @return The map of configuration values
	 */
	private Map<String, Object> getConfigValueMap(GangliaConf conf) {

		Map<String, Object> configValues = new HashMap<String, Object>();
		configValues.put("nodeIP", conf.getGmetadNode().getPrivateIp());
		configValues.put("clusterName", conf.getClusterName());
		configValues.put("clusterOwner", conf.getUsername());
		configValues.put("port", conf.getPort());
		configValues.put("pollingInterval", conf.getPollingInterval());
		configValues.put("gridName", conf.getGridName());

		String confTemplateFolder = conf.getServerConfFolder();
		String gmondUCastTemplate = confTemplateFolder + "gmond-unicast-"
				+ conf.getComponentVersion() + ".conf";

		configValues.put("gmondUCastTemplate", gmondUCastTemplate);

		return configValues;
	}
}
