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
package com.impetus.ankush2.ganglia;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.aspectj.util.FileUtil;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.ComponentService;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.Symbols;
import com.impetus.ankush2.agent.AgentConstant;
import com.impetus.ankush2.agent.AgentUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;
import com.impetus.ankush2.utils.SSHUtils;

public class GangliaDeployer extends AbstractDeployer {

	private ClusterConfig clusterConf;
	private ComponentConfig compConfig;
	private Map<String, Object> advanceConf;
	/** The newClusterConf */
	private ClusterConfig newClusterConf = null;

	private String gmetadHost;

	/** GangliaDeployer logger */
	private AnkushLogger logger = new AnkushLogger(GangliaDeployer.class);

	/**
	 * Setting clusterConf, componentConf and logger
	 * 
	 * @param clusterConf
	 *            {@link ClusterConfig}
	 */
	private boolean initializeDataMembers(ClusterConfig clusterConf)
			throws AnkushException {
		try {
			this.clusterConf = clusterConf;
			this.logger.setCluster(clusterConf);
			if (!clusterConf.getComponents().containsKey(getComponentName())) {
				throw new AnkushException("Could not find "
						+ getComponentName()
						+ " in cluster configuration object.");
			}
			this.compConfig = clusterConf.getComponents().get(
					getComponentName());

			// Initializing advancedConf to use it to for getting and setting
			// cluster level properties
			if (this.compConfig == null
					|| this.compConfig.getAdvanceConf() == null) {
				throw new AnkushException(
						"Could not get configuration details for "
								+ getComponentName());
			}
			this.advanceConf = this.compConfig.getAdvanceConf();
			return true;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not initialize data members for "
					+ getComponentName(), e);
		}
	}

	private boolean addClusterError(String error, String host) {
		return addClusterError(error, host, null);
	}

	private boolean addClusterError(String error, Throwable t) {
		return addClusterError(error, null, t);
	}

	private boolean addClusterError(String error, String host, Throwable t) {

		if (host != null) {
			this.clusterConf.addError(host, getComponentName(), error);
			logger.error(error, getComponentName(), host, t);
		} else {
			this.clusterConf.addError(getComponentName(), error);
			logger.error(error, getComponentName(), t);
		}
		return false;
	}

	@Override
	public boolean createConfig(ClusterConfig conf) {
		try {
			// Initializing data members
			if (!initializeDataMembers(conf)) {
				return false;
			}
			if (compConfig.isRegister()) {
				if (!(new GangliaRegister(clusterConf).createConfig())) {
					return false;
				}
				gmetadHost = (String) this.advanceConf
						.get(GangliaConstants.ClusterProperties.GMETAD_HOST);
				return true;
			} else {
				// set server gmond and gmetad template configuration folder.
				// Getting resource path.
				String resourceBasePath = AppStoreWrapper.getResourcePath();
				// Setting server conf folder.
				advanceConf
						.put(GangliaConstants.ClusterProperties.SERVER_CONF_FOLDER,
								FileNameUtils
										.convertToValidPath(resourceBasePath)
										+ GangliaConstants.CONFIG_GANGLIA_RELATIVE_PATH);
				return addGmondNodes();
			}
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError(
					"There is some exception while creating configuration for deploying "
							+ getComponentName() + ". "
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	/**
	 * Adding all cluster nodes to Ganglia component nodes
	 * 
	 * @return true , if successful
	 */
	private boolean addGmondNodes() throws AnkushException {
		try {
			// Preparing Ganglia component nodes. Adding all cluster nodes to
			// Ganglia nodes
			Map<String, Map<String, Object>> compNodes = new HashMap<String, Map<String, Object>>();
			Map<String, Set<String>> roles;
			Set<String> gmondNodes = new HashSet<String>();
			for (Map.Entry<String, NodeConfig> node : clusterConf.getNodes()
					.entrySet()) {
				compNodes.put(node.getKey(), new HashMap<String, Object>());
				roles = node.getValue().getRoles();
				if (!roles.containsKey(getComponentName())) {
					logger.warn(
							"Could not find any roles for "
									+ getComponentName()
									+ " in node's roles map, so adding an empty roles set.",
							getComponentName(), node.getKey());
					roles.put(getComponentName(), new HashSet<String>());
				}
				if (roles.get(getComponentName())
						.contains(Constant.Role.GMETAD)) {
					// assuming that there is single Ganglia master
					advanceConf.put(
							GangliaConstants.ClusterProperties.GMETAD_HOST,
							node.getKey());
					gmetadHost = node.getKey();
				}
				gmondNodes.add(node.getKey());
			}

			if (gmetadHost == null || gmetadHost.isEmpty()) {
				throw new AnkushException(
						"Could not find any node with role as "
								+ GangliaConstants.Ganglia_Services.GangliaMaster
								+ ", so could not deploy " + getComponentName()
								+ ".");
			}
			advanceConf.put(GangliaConstants.ClusterProperties.GMOND_SET,
					gmondNodes);
			compConfig.setNodes(compNodes);
			return true;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while adding cluster nodes to Ganglia component nodes object. + "
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	@Override
	public boolean validate(ClusterConfig conf) {
		try {
			// if (!this.initializeDataMembers(conf)) {
			// return false;
			// }
			// TODO: Ganglia cluster registration validation
			if (compConfig.isRegister()) {
				return true;
			}
			return validate(compConfig.getNodes().keySet());
		} catch (AnkushException e) {
			return addClusterError(e.getMessage(), e);
		} catch (Exception e) {
			return addClusterError(
					"There is some exception while validating nodes for "
							+ getComponentName() + " deployment."
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	@Override
	public boolean deploy(ClusterConfig conf) {
		// deploying and starting ganglia on all nodes
		return (deployNodes(conf, compConfig.getNodes()) && start(conf,
				compConfig.getNodes().keySet()));
	}

	@Override
	public boolean register(final ClusterConfig conf) {

		try {
			if (String.valueOf(advanceConf.get(Constant.Keys.REGISTER_LEVEL))
					.equalsIgnoreCase(Constant.RegisterLevel.LEVEL1.toString())) {
				return true;
			}
			final String infoMsg = "Registering " + getComponentName() + "...";
			logger.info(infoMsg);
			// Getting node map for cluster deployment
			Map<String, Map<String, Object>> nodeMap = new HashMap<String, Map<String, Object>>(
					compConfig.getNodes());

			// Node Registration process ...
			final Semaphore semaphore = new Semaphore(nodeMap.size());
			for (final String host : nodeMap.keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						conf.getNodes().get(host)
								.setStatus(configureServiceMonitoring(host));
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
			addClusterError("Could not register " + getComponentName(), e);
			return false;
		}
	}

	@Override
	public boolean undeploy(ClusterConfig conf) {
		try {
			// setting clusterconf, componentconf and logger
			if (!initializeDataMembers(conf)) {
				return false;
			}
			if (clusterConf.getNodes().keySet() != null
					|| !clusterConf.getNodes().isEmpty()) {
				return removeNode(conf, clusterConf.getNodes().keySet());
			}
		} catch (Exception e) {
			addClusterError(
					"Could not remove Ganglia. Please view server logs for more details.",
					e);
		}
		return false;
	}

	@Override
	public boolean unregister(final ClusterConfig conf) {

		try {
			if (String.valueOf(advanceConf.get(Constant.Keys.REGISTER_LEVEL))
					.equalsIgnoreCase(Constant.RegisterLevel.LEVEL1.toString())) {
				return true;
			}

			if (!initializeDataMembers(conf)) {
				return false;
			}
			final String infoMsg = "Unregistering " + getComponentName()
					+ "...";
			logger.info(infoMsg, getComponentName());
			// Getting node map for cluster deployment
			Map<String, Map<String, Object>> nodeMap = new HashMap<String, Map<String, Object>>(
					compConfig.getNodes());

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
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
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
				addClusterError("Could not get connection.", host);
				return false;
			}
			// Removing Ganglia service.xml
			String command = "rm -rf " + clusterConf.getAgentHomeDir()
					+ AgentConstant.Relative_Path.SERVICE_CONF_DIR
					+ getComponentName() + "." + Constant.File_Extension.XML;

			execCustomtask(command, connection, host, "Could not remove "
					+ getComponentName() + ".xml" + " file.");
			return true;
		} catch (Exception e) {
			addClusterError("Could not register node.", host, e);
			return false;
		}
	}

	@Override
	public boolean start(ClusterConfig conf) {
		return start(conf, compConfig.getNodes().keySet());
	}

	@Override
	public boolean start(final ClusterConfig conf, Collection<String> nodes) {
		final Semaphore semaphore = new Semaphore(nodes.size());
		String gangliaMaster = (String) advanceConf
				.get(GangliaConstants.ClusterProperties.GMETAD_HOST);
		try {
			// starting Ganglia master during deployment case
			if (newClusterConf == null) {
				// starting ganglia master first and then applying a 1 minute
				// sleep
				// before starting gmond
				if (!startGangliaMaster(gangliaMaster)) {
					return false;
				}
			}

			// starting service in each cluster node
			for (final String host : nodes) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						conf.getNodes().get(host).setStatus(startGmond(host));
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodes.size());
		} catch (Exception e) {
			logger.warn("There is some exception while stating "
					+ getComponentName() + " services.", getComponentName(), e);
		}
		// Return false if any of the node service is not started.
		// return AnkushUtils.getStatus(conf.getNodes());
		return true;
	}

	@Override
	public boolean addNode(ClusterConfig conf, ClusterConfig newConf) {

		try {
			// Set logger
			if (!initializeDataMembers(conf)) {
				return false;
			}
			this.newClusterConf = newConf;

			Map<String, Map<String, Object>> nodeMap = newConf.getComponents()
					.get(getComponentName()).getNodes();
			Set<String> gmondNodeSet = (Set<String>) compConfig
					.getAdvanceConf().get(
							GangliaConstants.ClusterProperties.GMOND_SET);
			for (String newNode : nodeMap.keySet()) {
				gmondNodeSet.add(newNode);
			}
			if (!deployNodes(newConf, nodeMap)) {
				return false;
			}
			// TODO: Updating 'gmondSet' in component advanceConf
			return start(newConf, nodeMap.keySet());
		} catch (AnkushException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private void updateGmondSet() {

	}

	@Override
	public boolean removeNode(final ClusterConfig conf, Collection<String> nodes) {
		try {
			if (newClusterConf == null) {
				// setting clusterconf, componentconf and logger
				if (!initializeDataMembers(conf)) {
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
		} catch (Exception e) {
			addClusterError("Could not remove " + getComponentName(), e);
			return false;
		}
		return AnkushUtils.getStatus(conf.getNodes());
	}

	public boolean removeNode(ClusterConfig conf, ClusterConfig newConf) {
		this.newClusterConf = newConf;
		return removeNode(newConf,
				newConf.getComponents().get(getComponentName()).getNodes()
						.keySet());
	}

	/**
	 * Stop cluster.
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	public boolean stopNode(String host) {
		String message = "Stopping ganglia...";
		// // Setting the start progress message for activity startup.
		logger.info(message, getComponentName(), host);
		String stopGmondCmd = "killall -9 "
				+ GangliaConstants.GangliaExecutables.GMOND;
		String stopGmetadCmd = "killall -9 "
				+ GangliaConstants.GangliaExecutables.GMETAD;
		try {
			// Connection with node via SSH connection
			SSHExec connection = getConnection(host);
			if (connection == null) {
				addClusterError("Could not get connection.", host);
				return false;
			}

			// stopping gmond
			execCustomtask(stopGmondCmd, connection, host, "Could not stop "
					+ Constant.Role.GMOND);
			if (((String) advanceConf
					.get(GangliaConstants.ClusterProperties.GMETAD_HOST))
					.equals(host)) {
				// stopping gmetad
				execCustomtask(stopGmetadCmd, connection, host,
						"Could not stop " + Constant.Role.GMETAD);
			}
		} catch (Exception e) {
			addClusterError("Could not stop " + getComponentName(), host, e);
			return false;
		}
		return true;
	}

	/**
	 * Removes Ganglia cluster related directories and environment variables
	 * from node
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code>, if successful
	 */
	private boolean removeNode(String host) {
		// Setting the start progress message for activity remove.
		String message = "Removing ganglia";
		logger.info(message, getComponentName(), host);

		List<String> rmCmds = new ArrayList<String>();
		rmCmds.add("rm -rf "
				+ advanceConf
						.get(GangliaConstants.ClusterProperties.GMOND_CONF_PATH));
		// removing gmetad.conf if node is ganglia master
		if (((String) advanceConf
				.get(GangliaConstants.ClusterProperties.GMETAD_HOST))
				.equals(host)) {
			rmCmds.add("rm -rf  "
					+ advanceConf
							.get(GangliaConstants.ClusterProperties.GMETAD_CONF_PATH));
		}
		rmCmds.add("rm -rf "
				+ (String) advanceConf
						.get(GangliaConstants.ClusterProperties.RRD_FILE_PATH));
		try {
			SSHExec connection = getConnection(host);
			if (connection == null) {
				addClusterError("Could not get connection.", host);
				return false;
			}
			// Removing directories
			CustomTask execTask = new ExecSudoCommand(
					GangliaUtils.getAuthentication(clusterConf),
					rmCmds.toArray(new String[rmCmds.size()]));
			return (connection.exec(execTask).rc == 0);
		} catch (Exception e) {
			addClusterError("Could not remove " + getComponentName()
					+ " directories.", host, e);
			return false;
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
				final NodeConfig nodeConf = clusterConf.getNodes().get(host);
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							nodeConf.setStatus(new GangliaValidator(
									clusterConf, nodeConf).validate());
						} catch (AnkushException e) {
							addClusterError(e.getMessage(), host, e);
						} catch (Exception e) {
							addClusterError(
									"There is some exception while validating "
											+ host + " for "
											+ getComponentName()
											+ " deployment. "
											+ GangliaConstants.EXCEPTION_STRING,
									host, e);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());

		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while validating nodes for "
							+ getComponentName() + " deployment."
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
		return AnkushUtils.getStatus(clusterConf, nodeList);
	}

	private boolean deployNodes(final ClusterConfig conf,
			Map<String, Map<String, Object>> nodeMap) {
		try {
			// Node Deployment process ...
			final Semaphore semaphore = new Semaphore(nodeMap.size());
			for (final String host : nodeMap.keySet()) {
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
			return addClusterError("There is some exception while deploying "
					+ getComponentName() + " on all nodes. "
					+ GangliaConstants.EXCEPTION_STRING, e);
		}
		if (newClusterConf == null) {
			if (!clusterConf.getNodes().get(gmetadHost).getStatus()) {
				logger.error("Could not deploy " + getComponentName() + " on "
						+ GangliaConstants.Ganglia_Services.GangliaMaster
						+ " node , so initializing rollback.",
						getComponentName());
			}
			return clusterConf.getNodes().get(gmetadHost).getStatus();
		} else {
			return AnkushUtils.getStatus(conf.getNodes());
		}
	}

	/**
	 * installing and configuring Ganglia
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code> , if successful
	 */
	private boolean createNode(String host) {
		boolean isGmetad = host.equals(gmetadHost);
		try {
			// Setting the start progress message for activity startup.
			String message = "Deploying " + getComponentName() + " on host : "
					+ host;
			logger.info(message, getComponentName(), host);
			// getting connection
			SSHExec connection = getConnection(host);
			if (connection == null) {
				throw new AnkushException("Issue while connecting to node : "
						+ host);
			}
			// Gmond conf folder directory
			String gmondConfFolderPath = FilenameUtils
					.getFullPath((String) advanceConf
							.get(GangliaConstants.ClusterProperties.GMOND_CONF_PATH));
			// Gmetad conf folder directory
			String gmetadConfFolderPath = FilenameUtils
					.getFullPath((String) advanceConf
							.get(GangliaConstants.ClusterProperties.GMETAD_CONF_PATH));

			if (gmondConfFolderPath == null || gmondConfFolderPath.isEmpty()
					|| gmetadConfFolderPath == null
					|| gmetadConfFolderPath.isEmpty()) {

				return addClusterError(
						getComponentName()
								+ " configuration path provided from UI is either null or empty.",
						host);
			}

			// Make gmond conf directory if not exists
			AnkushTask mkGmondConfFolderPath = new MakeDirectory(
					gmondConfFolderPath);
			if (connection.exec(mkGmondConfFolderPath).rc != 0) {
				throw new AnkushException("Could not create "
						+ gmondConfFolderPath + " directory on $TYPE node "
						+ host + ".");
			}
			// make gmetad conf directory on node
			AnkushTask mkGmetadConfFolderPath = new MakeDirectory(
					gmetadConfFolderPath);
			// if gmetad parent directory is different than the gmond parent
			// directory
			if (!mkGmondConfFolderPath.equals(mkGmetadConfFolderPath)) {
				if (connection.exec(mkGmetadConfFolderPath).rc != 0) {
					throw new AnkushException("Could not create "
							+ gmetadConfFolderPath
							+ " directory on $TYPE node " + host + ".");
				}
			}
			// TODO: gmetad conf path creation
			if (!installGanglia(host, connection, isGmetad)
					|| !createConfigurationFiles(host, connection, isGmetad)
					|| !createRRDDirectories(host, connection, isGmetad)
					|| !configureServiceMonitoring(host)) {
				return false;
			}
			// setting installed ganglia version in ganglia
			// conf.
			setInstalledGangliaVersion(host, isGmetad);
			return true;
		} catch (AnkushException e) {
			logger.warn(e.getMessage(), getComponentName(), host, e);
			return false;
		} catch (Exception e) {
			return addClusterError("There is some exception while installing "
					+ getComponentName() + " on node: " + host + ". "
					+ GangliaConstants.EXCEPTION_STRING, host, e);
		}
	}

	/**
	 * 
	 * @param isGmetad
	 * @param message
	 * @param host
	 * @param e
	 * @throws AnkushException
	 */
	private void addErrorOrWarning(boolean isGmetad, String message,
			String host, Exception e) throws AnkushException {
		if (isGmetad) {
			throw new AnkushException(
					message.replace("$TYPE ",
							GangliaConstants.Ganglia_Services.GangliaMaster
									.toString())
							+ " So rolling back "
							+ getComponentName()
							+ " deployment.", e);
		} else {
			logger.warn(message.replace("$TYPE ", ""), getComponentName(),
					host, e);
		}
	}

	/**
	 * 
	 * @param isGmetad
	 * @param message
	 * @param host
	 * @throws AnkushException
	 */
	private void addErrorOrWarning(boolean isGmetad, String message, String host)
			throws AnkushException {
		if (isGmetad) {
			throw new AnkushException(message.replace("$TYPE ",
					GangliaConstants.Ganglia_Services.GangliaMaster.toString())
					+ " So rolling back " + getComponentName() + " deployment.");
		} else {
			logger.warn(message.replace("$TYPE ", ""), getComponentName(), host);
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
	private boolean installGanglia(String host, SSHExec connection,
			boolean isGmetad) throws AnkushException {

		try {
			// Getting the OS Name of the Node.
			String osName = SSHUtils.getOS(connection, host);
			if (osName == null || osName.isEmpty()) {
				throw new AnkushException(
						"Could not identify OS details on node : " + host + ".");
			}

			String componentName = GangliaConstants.ComponentType.GMOND;
			if (isGmetad) {
				componentName = GangliaConstants.ComponentType.GMETAD;
			}
			String message = "Executing " + getComponentName()
					+ " deployment scripts";
			logger.info(message, getComponentName(), host);
			/* Getting the installer file name of the component. */
			String installerFilePath = FileNameUtils.getInstallerFilePath(
					osName, componentName);
			if (installerFilePath == null) {
				throw new AnkushException(
						"Could not get script file to install " + componentName
								+ " on node : " + host + ".");
			}
			// Getting list of installation commands from the file.
			List<String> depListCmds = FileUtils.readLines(new File(
					installerFilePath));
			// Iterating over the command lists.
			for (String installCmd : depListCmds) {
				if (!installCmd.isEmpty()) {
					// Executing the installation command.
					CustomTask execTask = new ExecSudoCommand(
							GangliaUtils.getAuthentication(clusterConf),
							installCmd);
					if (connection.exec(execTask).rc != 0) {
						throw new AnkushException("Executing command '"
								+ installCmd + "' on node " + host + " failed.");
					}
				}
			}
			return true;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while installing "
							+ getComponentName() + " on node : " + host + ". "
							+ GangliaConstants.EXCEPTION_STRING);
		}
	}

	private void setInstalledGangliaVersion(String host, boolean isGmetad)
			throws Exception {

		// Command to show the installed version of gmond
		String command = GangliaConstants.GangliaExecutables.GMOND;

		if (isGmetad) {
			// Command to show the installed version of gmetad
			command = GangliaConstants.GangliaExecutables.GMETAD;
		}
		try {
			SSHExec connection = getConnection(host);
			if (connection == null) {
				logger.warn(
						"There is some connection issue with node while running '"
								+ command + "' --v command for getting "
								+ getComponentName() + " version.",
						getComponentName(), host);
			}
			/* Executing the command to get the output. */
			CustomTask task = new ExecCommand(command + " --v");
			/* Executing the command. */
			Result rs = connection.exec(task);

			if (rs.sysout != null) {
				String version = rs.sysout.split(" ")[1];
				compConfig.setVersion(version);
			}
		} catch (Exception e) {
			logger.warn("There is some exception while getting "
					+ getComponentName() + " version on node: " + host + ". "
					+ GangliaConstants.EXCEPTION_STRING, getComponentName(),
					host, e);
		}

	}

	/**
	 * Used to create gmond.conf and gmetad.conf file
	 */
	private boolean createConfigurationFiles(String host, SSHExec connection,
			boolean isGmetad) throws AnkushException {

		try {
			// getting gmond.conf path on node
			String confPath = (String) advanceConf
					.get(GangliaConstants.ClusterProperties.GMOND_CONF_PATH);
			if (confPath == null || confPath.isEmpty()) {
				return addClusterError(
						"Gmond configuration path provided from UI is either null or empty.",
						host);
			}
			// getting gmond.conf from server
			String confFileName = (String) advanceConf
					.get(GangliaConstants.ClusterProperties.SERVER_CONF_FOLDER)
					+ GangliaConstants.ConfigurationFiles.GMOND_CONF;
			// Create the gmond configuration on node
			String message = "Configuring gmond file on node : " + host;
			logger.info(message, getComponentName(), host);
			CustomTask clearFile = new ClearFile(confPath);
			String fileContent = getConfigurationContent(host, confFileName)
					.replaceAll("\\\"", "\\\\\"");
			CustomTask appendFile = new AppendFileUsingEcho(fileContent,
					confPath);

			if ((connection.exec(clearFile).rc != 0)
					|| (connection.exec(appendFile).rc != 0)) {
				throw new AnkushException(
						"Could not create Gmond configuration file " + confPath
								+ " on node : " + host);
			}
			if (isGmetad) {
				confPath = (String) advanceConf
						.get(GangliaConstants.ClusterProperties.GMETAD_CONF_PATH);
				if (confPath == null || confPath.isEmpty()) {
					return addClusterError(
							"Gmetad configuration path provided from UI is either null or empty.",
							host);
				}
				confFileName = (String) advanceConf
						.get(GangliaConstants.ClusterProperties.SERVER_CONF_FOLDER)
						+ GangliaConstants.ConfigurationFiles.GMETAD_CONF;
				clearFile = new ClearFile(confPath);
				fileContent = getConfigurationContent(host, confFileName)
						.replaceAll("\\\"", "\\\\\"");
				appendFile = new AppendFileUsingEcho(fileContent, confPath);
				if (((connection.exec(clearFile).rc != 0) || (connection
						.exec(appendFile).rc != 0))) {
					throw new AnkushException(
							"Could not create Gmetad configuration file "
									+ confPath + " on $TYPE node : " + host);
				}
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while configuring "
							+ getComponentName() + " on $TYPE node " + host
							+ ". " + GangliaConstants.EXCEPTION_STRING);
		}
		return true;
	}

	/**
	 * Used to create RRD directory
	 */
	private boolean createRRDDirectories(String host, SSHExec connection,
			boolean isGmetad) throws AnkushException {
		try {
			logger.info("Creating rrd directory.", getComponentName(), host);

			// logging startup message.
			String message = "Creating ganglia directories";
			logger.info(message, getComponentName(), host);

			CustomTask mkdirRRd = new MakeDirectory(
					(String) advanceConf
							.get(GangliaConstants.ClusterProperties.RRD_FILE_PATH));
			return (connection.exec(mkdirRRd).rc == 0);
		} catch (TaskExecFailException e) {
			throw new AnkushException(
					"Could not create RRD directory on $TYPE node: " + host);
		}
	}

	/**
	 * function to execute Custom Tasks and setting error if it fails
	 * 
	 * @return <code>true</code>, if successful
	 */
	private boolean execCustomtask(String command, SSHExec connection,
			String publicIp, String errMsg) {
		// set clusterName in cassandra.yaml
		try {
			CustomTask task = new ExecCommand(command);
			if (connection.exec(task).rc == 0) {
				return true;
			}
			logger.warn(errMsg, getComponentName(), publicIp);
			// addClusterError(errMsg, publicIp);
		} catch (TaskExecFailException e) {
			logger.warn(errMsg, getComponentName(), publicIp, e);
			// addClusterError(errMsg, publicIp, e);
		}
		return false;
	}

	public String getConfigurationContent(String host, String confFileName)
			throws Exception {
		String fileContent = null;
		Map<String, Object> configValues = getConfigValueMap();

		String udpRecvChannel = "udp_recv_channel {\n port = "
				+ configValues.get("port") + " \n } ";
		// 'udp_recv_channel' value for gmond.conf
		configValues.put("udp_recv_channel", "/*" + udpRecvChannel + "*/");

		if (((String) advanceConf
				.get(GangliaConstants.ClusterProperties.GMETAD_HOST))
				.equals(host)) {
			StringBuffer nodeIpPorts = new StringBuffer();
			// Preparing a String of nodeIp:port of gmetad node used in
			// data_source in gmetad.conf.
			nodeIpPorts
					.append(advanceConf
							.get(GangliaConstants.ClusterProperties.GMETAD_HOST))
					.append(Symbols.STR_COLON);
			nodeIpPorts.append(advanceConf
					.get(GangliaConstants.ClusterProperties.GANGLIA_PORT));
			// Putting the nodeIpsPorts string in map
			configValues.put("nodeIpsPorts", nodeIpPorts.toString());
			// On gmond nodes other than Gmetad node commenting
			// udp_recv_channel block
			configValues.put("udp_recv_channel", udpRecvChannel);
		}
		// Reading the content of the template file
		fileContent = FileUtil.readAsString(new File(confFileName));

		// Creating a string substitutor using config values map
		StrSubstitutor sub = new StrSubstitutor(configValues);

		// Replacing the config values key found in the file content with
		// respected values.
		return sub.replace(fileContent);
	}

	public String getGmetadConfigurationContent(String host) throws Exception {
		String fileContent = null;
		String confFileName = (String) advanceConf
				.get(GangliaConstants.ClusterProperties.SERVER_CONF_FOLDER)
				+ GangliaConstants.ConfigurationFiles.GMOND_CONF;

		Map<String, Object> configValues = getConfigValueMap();

		String udpRecvChannel = "udp_recv_channel {\n port = "
				+ configValues.get("port") + " \n } ";
		configValues.put("udp_recv_channel", "/*" + udpRecvChannel + "*/");

		if (((String) advanceConf
				.get(GangliaConstants.ClusterProperties.GMETAD_HOST))
				.equals(host)) {
			confFileName = (String) advanceConf
					.get(GangliaConstants.ClusterProperties.SERVER_CONF_FOLDER)
					+ GangliaConstants.ConfigurationFiles.GMETAD_CONF;
			StringBuffer nodeIpPorts = new StringBuffer();
			// Preparing a String of nodeIp:port of gmetad node.
			nodeIpPorts
					.append(advanceConf
							.get(GangliaConstants.ClusterProperties.GMETAD_HOST))
					.append(Symbols.STR_COLON);
			nodeIpPorts.append(advanceConf
					.get(GangliaConstants.ClusterProperties.GANGLIA_PORT));
			// Putting the nodeIpsPorts string in map
			configValues.put("nodeIpsPorts", nodeIpPorts.toString());
			// On gmond nodes other than Gmetad node commenting
			// udp_recv_channel block
			configValues.put("udp_recv_channel", udpRecvChannel);
		}
		// Reading the content of the template file
		fileContent = FileUtil.readAsString(new File(confFileName));

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
	private Map<String, Object> getConfigValueMap() {

		Map<String, Object> configValues = new HashMap<String, Object>();
		configValues
				.put("nodeIP", advanceConf
						.get(GangliaConstants.ClusterProperties.GMETAD_HOST));
		configValues.put("clusterName", advanceConf
				.get(GangliaConstants.ClusterProperties.GANGLIA_CLUSTER_NAME));
		configValues.put("clusterOwner", clusterConf.getAuthConf()
				.getUsername());
		configValues.put("port", advanceConf
				.get(GangliaConstants.ClusterProperties.GANGLIA_PORT));
		configValues.put("pollingInterval", advanceConf
				.get(GangliaConstants.ClusterProperties.POLLING_INTERVAL));
		configValues.put("gridName",
				advanceConf.get(GangliaConstants.ClusterProperties.GRID_NAME));
		configValues.put("rrdFilePath", advanceConf
				.get(GangliaConstants.ClusterProperties.RRD_FILE_PATH));
		return configValues;
	}

	private boolean configureServiceMonitoring(String host) {
		try {
			// getting connection
			SSHExec connection = getConnection(host);
			if (connection == null) {
				logger.warn("Issue while connecting to node: " + host,
						getComponentName(), host);
			}
			// List of service object
			List<ComponentService> services = new ArrayList<ComponentService>();
			// Considering Ganglia registration case when separate node is used
			// as
			// Ganglia Master and no gmond is installed on it
			// if (((Set<String>) advanceConf
			// .get(GangliaConstants.ClusterProperties.GMOND_SET))
			// .contains(host)) {
			services.add(new ComponentService(Constant.Process.GMOND,
					Constant.Role.GMOND, Constant.ServiceType.PS));
			// }

			// Gmetad component service.
			if (host.equals(gmetadHost)) {
				// gmetad service object.
				services.add(new ComponentService(Constant.Process.GMETAD,
						Constant.Role.GMETAD, Constant.ServiceType.PS));
			}
			// Create Ganglia service xml.
			AgentUtils.createServiceXML(connection, services,
					getComponentName(), clusterConf.getAgentHomeDir());
		} catch (Exception e) {
			logger.warn(
					"There is some exception while creating service xml files for monitoring "
							+ getComponentName() + " services.",
					getComponentName(), host, e);
		}
		return true;
	}

	public boolean startGangliaMaster(String host) {
		try {
			// Connection with node via SSH connection
			SSHExec connection = getConnection(host);
			if (connection == null) {
				logger.warn("Issue while creating connection with "
						+ GangliaConstants.Ganglia_Services.GangliaMaster
						+ " node while starting 'gmetad' process",
						getComponentName(), host);
			}
			logger.info("Starting " + Constant.Role.GMETAD + "...",
					getComponentName(), host);
			String startGmetadCmd = GangliaConstants.GangliaExecutables.GMETAD
					+ " --conf="
					+ advanceConf
							.get(GangliaConstants.ClusterProperties.GMETAD_CONF_PATH);
			execCustomtask(startGmetadCmd, connection, host, "Could not start "
					+ GangliaConstants.Ganglia_Services.GangliaMaster
					+ " on node " + host
					+ " . Please start it manually after cluster deployment.");
		} catch (Exception e) {
			logger.warn("There is some exception while starting "
					+ GangliaConstants.Ganglia_Services.GangliaMaster
					+ " on node: " + host + ". "
					+ GangliaConstants.EXCEPTION_STRING, getComponentName(),
					host, e);
		}
		return true;
	}

	public boolean startGmond(String host) {
		String message = "Starting " + Constant.Role.GMOND + "...";
		// Setting the start progress message for activity startup.
		logger.info(message, getComponentName(), host);
		String startGmondCmd = GangliaConstants.GangliaExecutables.GMOND
				+ " --conf="
				+ advanceConf
						.get(GangliaConstants.ClusterProperties.GMOND_CONF_PATH);
		try {
			// Connection with node via SSH connection
			SSHExec connection = getConnection(host);
			if (connection == null) {
				logger.warn("Issue while creating connection with node : "
						+ host, getComponentName(), host);
			}
			// Starting the gmond process on node
			execCustomtask(startGmondCmd, connection, host, "Could not start "
					+ Constant.Role.GMOND + " on node " + host
					+ " . Please start it manually after cluster deployment.");
		} catch (Exception e) {
			logger.warn(
					"There is some exception while starting gmond process on node: "
							+ host, getComponentName(), host, e);
		}
		return true;
	}

	/**
	 * Provides node's connection object
	 * 
	 * @param host
	 *            {@link String}
	 * @return {@link SSHExec}
	 */
	private SSHExec getConnection(String host) {
		try {
			if (clusterConf.getNodes().containsKey(host)) {
				return clusterConf.getNodes().get(host).getConnection();
			}
			return newClusterConf.getNodes().get(host).getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Checks whether a node can be deleted
	 */
	@Override
	public boolean canNodeBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes) {
		logger.setCluster(clusterConfig);
		if (nodes.contains(clusterConfig
				.getComponents()
				.get(getComponentName())
				.getAdvanceConfStringProperty(
						GangliaConstants.ClusterProperties.GMETAD_HOST))) {
			logger.error(
					"Could not delete nodes as they contains GangliaMaster which cannot be deleted.",
					getComponentName());
			return false;
		}
		return true;
	}
}
