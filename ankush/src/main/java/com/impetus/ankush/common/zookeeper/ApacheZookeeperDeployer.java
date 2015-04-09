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
package com.impetus.ankush.common.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

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
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.RemoveText;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.JmxUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.validator.ValidationResult;
import com.impetus.ankush.common.utils.validator.ValidationUtility;
import com.impetus.ankush2.constant.Constant.Component;
import com.impetus.ankush2.constant.Constant.Strings;

// TODO: Auto-generated Javadoc
/**
 * The Class ApacheZookeeperDeployer.
 * 
 * @author mayur
 */
public class ApacheZookeeperDeployer implements Deployable {

	private static final String DOTS = "...";

	/** The log. */
	private final AnkushLogger LOG = new AnkushLogger(
			Component.Name.ZOOKEEPER, ApacheZookeeperDeployer.class);

	/** The start. */
	private final String START = "start";

	/** The stop. */
	private final String STOP = "stop";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Deploying Zookeeper cluster");
		LOG.debug("Deploying Zookeeper cluster with configuration - " + conf);

		List<NodeConf> nodeList = conf.getNodes();
		int nodeId = 0;
		Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			// iterate over node list
			for (NodeConf nodeConf : nodeList) {

				semaphore.acquire();
				++nodeId;
				// create worker node
				ZookeeperWorker worker = new ZookeeperWorker(semaphore, conf,
						nodeConf, nodeId);
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		LOG.log("Zookeeper deployment", status);
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		final ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		LOG.debug("Undeploying Zookeeper cluster with configuration - " + conf);
		LOG.info("Undeploying Zookeeper cluster");

		List<NodeConf> nodeList = conf.getNodes();
		return removeNode(nodeList, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		LOG.debug("Starting Zookeeper cluster with configuration - " + conf);
		LOG.info("Starting Zookeeper cluster");
		boolean status = action(conf, conf.getNodes(), START);
		LOG.log("Starting zookeeper", status);
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		LOG.debug("Stopping Zookeeper cluster with configuration - " + conf);
		LOG.info("Stopping Zookeeper cluster");
		boolean status = action(conf, conf.getNodes(), STOP);
		LOG.log("Stopping zookeeper", status);
		return status;
	}

	/**
	 * Action.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeList
	 *            the node list
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	private boolean action(final ZookeeperConf conf, List<NodeConf> nodeList,
			final String action) {

		boolean statusFlag = false;

		try {
			final Semaphore semaphore = new Semaphore(nodeList.size());
			for (final NodeConf nodeConf : nodeList) {
				nodeConf.setStatus(statusFlag);
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						final String publicIp = nodeConf.getPublicIp();

						SSHExec connection = null;
						try {
							// connect to remote node
							connection = SSHUtils.connectToNode(publicIp,
									conf.getUsername(), conf.getPassword(),
									conf.getPrivateKey());

							// if connected
							if (connection != null) {
								String cmd = FileUtils
										.getSeparatorTerminatedPathEntry(conf
												.getComponentHome())
										+ "bin/zkServer.sh"
										+ Strings.SPACE + action;
								LOG.debug("Executing command - " + cmd
										+ "on node - " + publicIp);
								Result result;
								if (action.equals(STOP)) {
									CustomTask stopZoo = new ExecCommand(cmd);
									result = connection.exec(stopZoo);
								} else {
									AnkushTask runCmd = new RunInBackground(cmd);
									result = connection.exec(runCmd);
								}
								if (result.rc != 0) {
									LOG.error(nodeConf, result.error_msg);
								}
								nodeConf.setStatus(result.isSuccess);
							} else {
								LOG.error(nodeConf, "Authentication failed");
							}
						} catch (Exception e) {
							LOG.error(nodeConf, e.getMessage(), e);
							nodeConf.setStatus(false);
						} finally {
							// disconnect to node/machine
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());
			statusFlag = status(nodeList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			statusFlag = false;
		}
		return statusFlag;
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
		return status(nodeList);
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
		ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		LOG.debug("Stopping Zookeeper cluster with configuration - " + conf);
		LOG.info("Stopping Zookeeper cluster");
		LOG.info("Stopping Zookeeper...");
		boolean status = action(conf, nodeList, "stop");
		LOG.log("Stopping Zookeeper", status);
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
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		return status(nodeList);
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
		return removeNode(nodeList, config);
	}

	/**
	 * Removes the node.
	 * 
	 * @param nodeList
	 *            the node list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	private boolean removeNode(List<NodeConf> nodeList, Configuration config) {
		final ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		final Semaphore semaphore = new Semaphore(nodeList.size());
		boolean statusFlag = false;

		try {
			// iterate over nodeList
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						final String publicIp = nodeConf.getPublicIp();

						SSHExec connection = null;
						try {
							LOG.info("Connecting with node..." + publicIp);
							// connect to remote node
							connection = SSHUtils.connectToNode(publicIp,
									conf.getUsername(), conf.getPassword(),
									conf.getPrivateKey());

							// if connected
							if (connection != null) {
								AnkushTask removeZkDir = new Remove(FileUtils
										.getSeparatorTerminatedPathEntry(conf
												.getComponentHome()));
								AnkushTask removeDataDir = new Remove(FileUtils
										.getSeparatorTerminatedPathEntry(conf
												.getDataDirectory()));
								LOG.info("Removing DataDir...");
								connection.exec(removeDataDir);
								LOG.info("Removing zookeeper");
								nodeConf.setStatus(connection.exec(removeZkDir).isSuccess);
							}
						} catch (Exception e) {
							LOG.error(nodeConf, e.getMessage(), e);
						} finally {
							// disconncet to node/machine
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());
			statusFlag = status(nodeList);
			// if statusFlag=true,update zoo.cfg file on remaining nodes
			if (statusFlag) {
				statusFlag = updateZooConfFile(config, nodeList);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			statusFlag = false;
		}
		return statusFlag;
	}

	/**
	 * Update zoo conf file.
	 * 
	 * @param config
	 *            the config
	 * @param removedNodes
	 *            the node list
	 * @return true, if successful
	 */
	private boolean updateZooConfFile(Configuration config,
			List<NodeConf> removedNodes) {
		ZookeeperConf zkConf = (ZookeeperConf) config;

		// zkNodes List
		List<NodeConf> zkNodes = new ArrayList<NodeConf>();

		// iterate over all zookeeper nodes
		for (NodeConf nc : zkConf.getNodes()) {
			// if removedNodes doesn't contain this zookeeper node,add this
			// zookeeper node to zkNodes List.
			if (!removedNodes.contains(nc)) {
				zkNodes.add(nc);
			}
		}
		List<String> hostNameList = new ArrayList<String>();

		// iterate over all removedNodes to make their hostName List.
		for (NodeConf nc : removedNodes) {

			// get hostName for node
			// String hostName = HostOperation
			// .getAnkushHostName(nc.getPrivateIp());
			String hostName = nc.getPrivateIp();

			// add hostName to hostName List
			hostNameList.add(hostName);
		}
		return updateFile(zkNodes, hostNameList, config);
	}

	/**
	 * Update file.
	 * 
	 * @param zkNodes
	 *            the zk nodes
	 * @param hostNameList
	 *            the host name list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	private boolean updateFile(List<NodeConf> zkNodes,
			final List<String> hostNameList, Configuration config) {

		// if there is no zookeeper node in cluster,return
		if (zkNodes.isEmpty()) {
			return true;
		}
		final ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Going to update zoo.cfg file...");
		final Semaphore semaphore = new Semaphore(zkNodes.size());
		boolean statusFlag = false;

		try {
			for (final NodeConf nodeConf : zkNodes) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						final String publicIp = nodeConf.getPublicIp();

						SSHExec connection = null;
						try {
							LOG.info("Connecting with node..." + publicIp);
							// connect to remote node
							connection = SSHUtils.connectToNode(publicIp,
									conf.getUsername(), conf.getPassword(),
									conf.getPrivateKey());

							// if connected
							if (connection != null) {
								String filePath = conf.getComponentHome()
										+ "/conf/zoo.cfg";
								boolean createBackUpFile = true;
								AnkushTask removeText = new RemoveText(
										hostNameList, filePath, conf
												.getPassword());
								nodeConf.setStatus(connection.exec(removeText).isSuccess);
							}
						} catch (Exception e) {
							LOG.error(nodeConf, e.getMessage(), e);
						} finally {
							// disconncet to node/machine
							if (connection != null) {
								connection.disconnect();
							}
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(zkNodes.size());
			statusFlag = status(zkNodes);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			statusFlag = false;
		}
		return statusFlag;
	}

	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            List<NodeConf>
	 * @return boolean
	 */
	private boolean status(List<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = nodeConf.getStatus() && status;
		}
		return status;
	}

	@Override
	public boolean registerComponent(Configuration config) {
		final ZookeeperConf conf = (ZookeeperConf) config;
		LOG.setLoggerConfig(conf);
		List<NodeConf> nodeList = conf.getNodes();
		final Semaphore semaphore = new Semaphore(nodeList.size());
		try {
			// iterate over node list
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						SSHExec connection = null;
						try {
							// connect to node
							connection = SSHUtils.connectToNode(
									nodeConf.getPublicIp(), conf.getUsername(),
									conf.getPassword(), conf.getPrivateKey());
							// register node
							nodeConf.setStatus(ZookeeperWorker.register(conf,
									connection, nodeConf));
						} catch (Exception e) {
							LOG.error(e.getMessage(), e);
						} finally {

							if (semaphore != null) {
								semaphore.release();
							}
							// disconncet to node/machine
							if (connection != null) {
								connection.disconnect();
							}
						}
					}
				});
			}
			// block all threads.
			semaphore.acquire(nodeList.size());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return status(nodeList);
	}

	@Override
	public boolean unregisterComponent(Configuration config) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validateComponent(String nodeIp, SSHExec connection,
			GenericConfiguration config) {
		ZookeeperConf zooConf = (ZookeeperConf) config;
		System.out.println("Zookeeper config.deployComponentFlag()"
				+ config.isDeployComponentFlag());
		if (config.isDeployComponentFlag()) {
			return validateDeployableComponent(nodeIp, connection, zooConf);
		} else {
			return validateRegistrableComponent(nodeIp, connection, zooConf);
		}
		// return true;
	}

	/**
	 * Validate deployable component.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @param connection
	 *            the connection
	 * @param zooConf
	 *            the zoo conf
	 * @return true, if successful
	 */
	private boolean validateDeployableComponent(String nodeIp,
			SSHExec connection, ZookeeperConf zooConf) {
		Set<NodeConf> compNodes = zooConf.getCompNodes();
		NodeConf node = null;
		for (NodeConf nc : compNodes) {
			if (nc.getPublicIp().equals(nodeIp)) {
				node = nc;
			}
		}
		// validating installation path and its permissions.
		ValidationUtility.validatePathPermissions(connection,
				Component.Name.ZOOKEEPER, zooConf, node);
		// validating component paths for local or tarball bundle

		ValidationUtility.validatingComponentPaths(connection,
				Component.Name.ZOOKEEPER, zooConf, node);
		return node.getStatus();
	}

	/**
	 * Validate registrable component.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @param connection
	 *            the connection
	 * @param zooConf
	 *            the zoo conf
	 * @return true, if successful
	 */
	private boolean validateRegistrableComponent(String nodeIp,
			SSHExec connection, ZookeeperConf zooConf) {
		boolean returnStatusFlag = true;
		LOG.setLoggerConfig(zooConf);
		String message = "Validating Zookeeper home path";
		LOG.info(nodeIp, message + DOTS);
		ValidationResult validationResult = ValidationUtility
				.validatePathExistence(connection, zooConf.getComponentHome());
		if (!validationResult.isStatus()) {
			returnStatusFlag = false;
			LOG.error(nodeIp,
					Component.Name.ZOOKEEPER + Strings.SPACE
							+ "componentHome " + validationResult.getMessage());
		}
		LOG.log(nodeIp, message, returnStatusFlag);
		if (!validateJmxPort(Component.Name.ZOOKEEPER, connection,
				zooConf, nodeIp)) {
			returnStatusFlag = false;
		}
		return returnStatusFlag;
	}

	/**
	 * Validate jmx port.
	 * 
	 * @param componentName
	 *            the component name
	 * @param connection
	 *            the connection
	 * @param zooConf
	 *            the zoo conf
	 * @param nodeIp
	 *            the node ip
	 * @return true, if successful
	 */
	private boolean validateJmxPort(String componentName, SSHExec connection,
			ZookeeperConf zooConf, String nodeIp) {
		String VALIDATING = "Validating ";
		LOG.setLoggerConfig(zooConf);
		LOG.info(nodeIp, VALIDATING + componentName + " jmx port...");
		boolean status = false;
		try {
			LOG.info(nodeIp, "making jmx connection for Zookeeper on port:"
					+ zooConf.getJmxPort());
			JmxUtil jmxUtil = new JmxUtil(nodeIp, Integer.parseInt(zooConf
					.getJmxPort()));
			MBeanServerConnection jmxConnection = jmxUtil.connect();
			if (jmxConnection != null) {
				// get offlinePartitions Value
				Set<ObjectName> objectNameSet = jmxUtil
						.getObjectSetFromPatternString("org.apache.ZooKeeperService:name0=*");
				if (objectNameSet == null && objectNameSet.size() <= 0) {
					LOG.error(nodeIp, VALIDATING + componentName
							+ " jmx port failed...");
					System.out.println("objectNameSet=" + objectNameSet);
				} else {
					LOG.info(nodeIp, VALIDATING + componentName
							+ " jmx port passed.");
					status = true;
				}
			} else {
				LOG.error(nodeIp,
						"Couldn't connect with the JMX_PORT of Zookeeper.");
			}
		} catch (AnkushException e) {
			LOG.error(e.getMessage() != null ? e.getMessage()
					: com.impetus.ankush2.constant.Constant.Keys.GENERAL_EXCEPTION_STRING, e);
		} catch (NumberFormatException e) {
			LOG.error(e.getMessage() != null ? e.getMessage()
					: com.impetus.ankush2.constant.Constant.Keys.GENERAL_EXCEPTION_STRING, e);
		} catch (Exception e) {
			LOG.error(e.getMessage() != null ? e.getMessage()
					: com.impetus.ankush2.constant.Constant.Keys.GENERAL_EXCEPTION_STRING, e);
		}
		return status;
	}

	@Override
	public boolean deployPatch(Configuration config) {
		// TODO Auto-generated method stub
		return false;
	}
}
