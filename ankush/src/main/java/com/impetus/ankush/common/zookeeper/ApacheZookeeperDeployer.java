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
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.RemoveText;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.SSHUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ApacheZookeeperDeployer.
 * 
 * @author mayur
 */
public class ApacheZookeeperDeployer implements Deployable {

	/** The log. */
	private final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.ZOOKEEPER, ApacheZookeeperDeployer.class);
	
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
								String cmd = conf.getInstallationPath()
										+ "zookeeper-"
										+ conf.getComponentVersion()
										+ "/bin/zkServer.sh" + Constant.STR_SPACE + action;
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
								nodeConf.setStatus(result.isSuccess);
							} else {
								LOG.error(nodeConf, "Authentication failed");
							}
						} catch (Exception e) {
							LOG.error(nodeConf, e.getMessage(), e);
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
								AnkushTask removeZkDir = new Remove(conf
										.getInstallationPath()
										+ "zookeeper-"
										+ conf.getComponentVersion());
								AnkushTask removeDataDir = new Remove(conf
										.getDataDirectory());
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
			String hostName = HostOperation
					.getAnkushHostName(nc.getPrivateIp());

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
								System.out.println("filePath-" + filePath);
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
}
