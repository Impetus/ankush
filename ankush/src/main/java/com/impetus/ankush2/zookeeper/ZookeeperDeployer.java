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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.management.ObjectName;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JmxUtil;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

/**
 * The Class ZookeeperDeployer.
 */
public class ZookeeperDeployer extends AbstractDeployer {

	/** The logger. */
	private final AnkushLogger logger = new AnkushLogger(
			ZookeeperDeployer.class);

	/** The cluster config. */
	private ClusterConfig clusterConfig;

	/**
	 * Sets the cluster and logger.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @return true, if successful
	 */
	private boolean setClusterAndLogger(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		if (this.clusterConfig.getComponents().get(this.componentName) == null) {
			logger.error(ZookeeperConstant.Keys.ERROR_Zookeeper_CONF_NOT_FOUND,
					this.componentName);
			this.clusterConfig.addError(this.componentName,
					"Couldn't get Zookeeper configuration from cluster conf.");
			return false;
		}
		logger.setCluster(clusterConfig);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#createConfig(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean createConfig(ClusterConfig clusterConfig) {
		if (!this.setClusterAndLogger(clusterConfig)) {
			return false;
		}
		ComponentConfig compConf = clusterConfig.getComponents().get(
				this.componentName);
		if (!compConf.isRegister()) {
			compConf.setHomeDir(FileUtils
					.getSeparatorTerminatedPathEntry(compConf.getInstallPath())
					+ Constant.Component.Name.ZOOKEEPER.toLowerCase()
					+ "-"
					+ compConf.getVersion());
		} else {
			JmxUtil jmxUtil = null;
			String zookeeperHost = compConf
					.getAdvanceConfStringProperty(ZookeeperConstant.Keys.Advance_Conf_Keys.HOST);
			int jmxPort = compConf
					.getAdvanceConfIntegerProperty(ZookeeperConstant.Keys.JMX_PORT);

			try {
				logger.info(
						"Getting all the zookeeper nodes from the given host-"
								+ zookeeperHost, this.componentName);
				String patternStr = "org.apache.ZooKeeperService:name0=*";
				jmxUtil = new JmxUtil(zookeeperHost, jmxPort);
				jmxUtil.connect();
				Set<ObjectName> objectNameSet;
				String quorumAddress = null;
				if ((objectNameSet = jmxUtil
						.getObjectSetFromPatternString(patternStr)) != null) {
					for (ObjectName objName : objectNameSet) {
						if (objName.toString().contains(
								ZookeeperConstant.Keys.STANDALONESERVER_PORT_1)) {
							quorumAddress = zookeeperHost;
							// skip adding node in cluster for Level1
							// Registration
							addNodeToClusterAndComponent(compConf,
									quorumAddress);

						} else {
							String replicaObjName = objName + ",name1=*";
							objectNameSet = jmxUtil
									.getObjectSetFromPatternString(replicaObjName);
							for (ObjectName objName1 : objectNameSet) {
								String quorumAddressStr = (String) jmxUtil
										.getAttribute(objName1, "QuorumAddress");

								String[] quorumAddressArray = quorumAddressStr
										.split(":");
								if (quorumAddressArray[0].contains("/")) {
									quorumAddress = quorumAddressArray[0]
											.split("/")[0];
								} else {
									quorumAddress = quorumAddressArray[0];
								}
								addNodeToClusterAndComponent(compConf,
										quorumAddress);
							}
						}
					}
				} else {
					throw new AnkushException("Zookeeper JMX port : " + jmxPort
							+ " is not enabled on host : " + zookeeperHost);
				}
			} catch (AnkushException e) {
				logger.error(e.getMessage(), this.componentName, zookeeperHost,
						e);
				clusterConfig.addError(componentName, e.getMessage());
				return false;
			} catch (Exception e) {
				logger.error("Couldn't create configuration",
						this.componentName, zookeeperHost, e);
				return false;
			} finally {
				jmxUtil.disconnect();
			}

		}
		return true;
	}

	/**
	 * Register Zookeeper : Adds the node to cluster and componnet depending
	 * upon the level of Registration for Zookeeper
	 * 
	 * @param compConf
	 *            the comp conf
	 * @param quorumAddress
	 *            the quorum address
	 * @throws Exception
	 *             the exception
	 */
	private void addNodeToClusterAndComponent(ComponentConfig compConf,
			String quorumAddress) throws Exception {
		if (!AnkushUtils.isMonitoredByAnkush(compConf)) {
			AnkushUtils.addNodeToComponentConfig(clusterConfig,
					this.componentName, quorumAddress,
					new HashMap<String, Object>());

		} else {
			AnkushUtils
					.addNodeToClusterAndComponent(
							clusterConfig,
							quorumAddress,
							new HashSet<String>(Arrays
									.asList(Constant.Role.ZOOKEEPER)), this
									.getComponentName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#validate(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean validate(ClusterConfig conf) {

		boolean status = false;
		ComponentConfig compConf = clusterConfig.getComponents().get(
				this.getComponentName());
		// skip all the Validation in Level1 Registration
		if (compConf.isRegister() && !AnkushUtils.isMonitoredByAnkush(compConf)) {
			return true;
		}
		try {
			final Semaphore semaphore = new Semaphore(compConf.getNodes()
					.size());
			for (final String host : compConf.getNodes().keySet()) {
				semaphore.acquire();
				final ZookeeperValidator zooValidator = new ZookeeperValidator(
						clusterConfig, clusterConfig.getNodes().get(host),
						this.getComponentName());

				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						try {
							clusterConfig.getNodes().get(host)
									.setStatus(zooValidator.validate());

						} catch (Exception e) {
							logger.error(
									"Exception in validation of Zookeeper."
											+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
									getComponentName(), e);
							clusterConfig.getNodes().get(host).setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(compConf.getNodes().size());
			status = AnkushUtils.getStatus(clusterConfig, compConf.getNodes()
					.keySet());
		} catch (Exception e) {
			status = false;
			logger.error("Exception in validation of Zookeeper."
					+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
					this.getComponentName(), e);
		}
		if (status) {
			logger.info(this.getComponentName() + " validation is over.",
					this.getComponentName());
		} else {
			logger.error("Validating " + this.getComponentName() + " failed.",
					this.getComponentName());
			clusterConfig.addError(getComponentName(), "Validating "
					+ getComponentName() + " failed.");
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#deploy(com.impetus.ankush2.framework
	 * .config.ClusterConfig)
	 */
	@Override
	public boolean deploy(ClusterConfig clusterConfig) {
		if (!this.setClusterAndLogger(clusterConfig)) {
			return false;
		}
		ComponentConfig compConf = clusterConfig.getComponents().get(
				this.componentName);
		try {
			int nodeId = 0;
			// iterate over node list
			for (String host : compConf.getNodes().keySet()) {
				nodeId++;
				((Map<String, Object>) compConf.getNodes().get(host)).put(
						ZookeeperConstant.Keys.NODE_ID, nodeId);
			}
			compConf.getAdvanceConf().put(ZookeeperConstant.Keys.LAST_NODE_ID,
					nodeId);
			return deployNodes(clusterConfig);
		} catch (Exception e) {
			logger.error(e.getMessage(), this.componentName, e);
		}
		return false;
	}

	/**
	 * Deploy nodes.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @return true, if successful
	 */
	private boolean deployNodes(ClusterConfig clusterConfig) {
		logger.info("Deploying Zookeeper...", this.componentName);
		ComponentConfig compConfig = clusterConfig.getComponents().get(
				this.componentName);
		final Semaphore semaphore = new Semaphore(compConfig.getNodes().size());

		boolean status = false;
		try {
			for (final String host : compConfig.getNodes().keySet()) {
				// acuiring the semaphore
				semaphore.acquire();
				final NodeConfig nodeConfig = clusterConfig.getNodes()
						.get(host);
				Map<String, Object> nodeAdvanceConf = compConfig.getNodes()
						.get(host);

				final ZookeeperWorker zookeeperWorker = new ZookeeperWorker(
						this.clusterConfig, this.componentName, nodeConfig,
						nodeAdvanceConf);
				final ZookeeperServiceMonitor zooServiceMonitor = new ZookeeperServiceMonitor(
						clusterConfig, this.componentName);
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						try {
							nodeConfig.setStatus(zookeeperWorker.createNode());
							if (nodeConfig.getStatus()) {
								if (!zooServiceMonitor.action(
										ZookeeperConstant.Action.START,
										nodeConfig.getHost())) {
									logger.warn("Couldn't "
											+ ZookeeperConstant.Action.START
											+ " Zookeeper.", componentName,
											nodeConfig.getHost());
								}
							} else {
								logger.error("Couldn't deploy Zookeeper.",
										componentName, nodeConfig.getHost());
							}
						} catch (Exception e) {
							nodeConfig.setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(compConfig.getNodes().size());
			status = AnkushUtils.getStatus(clusterConfig, compConfig.getNodes()
					.keySet());
		} catch (Exception e) {
			logger.error(e.getMessage(), this.componentName, e);
			status = false;
		}
		if (status) {
			logger.info("Deploying Zookeeper is over.", componentName);
		} else {
			logger.error("Deploying Zookeeper failed.", componentName);
			clusterConfig.addError(getComponentName(),
					"Deploying Zookeeper failed.");
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#register(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean register(ClusterConfig clusterConfig) {
		ComponentConfig compConf = clusterConfig.getComponents().get(
				this.componentName);
		if (!AnkushUtils.isMonitoredByAnkush(compConf)) {
			logger.info(
					"Skipping Zookeeper service configuration for Level1 Registration",
					this.componentName);
			return true;
		}
		logger.info("Registering Zookeeper...", this.componentName);
		boolean status = false;
		try {
			final Semaphore semaphore = new Semaphore(compConf.getNodes()
					.size());
			for (String host : compConf.getNodes().keySet()) {
				semaphore.acquire();
				final NodeConfig nodeConfig = clusterConfig.getNodes()
						.get(host);
				final ZookeeperWorker zookeeperWorker = new ZookeeperWorker(
						clusterConfig, this.componentName, nodeConfig);
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						try {
							nodeConfig.setStatus(zookeeperWorker.register());
						} catch (Exception e) {
							logger.error(e.getMessage(), componentName, e);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(compConf.getNodes().size());
			status = AnkushUtils.getStatus(clusterConfig, compConf.getNodes()
					.keySet());
		} catch (Exception e) {
			logger.error(e.getMessage(), this.componentName, e);
		}
		if (status) {
			logger.info("Registering Zookeeper is over.", componentName);
		} else {
			logger.error("Registering Zookeeper failed.", componentName);
			clusterConfig.addError(getComponentName(),
					"Registering Zookeeper failed.");
		}
		return status;
	}

	@Override
	public boolean unregister(ClusterConfig clusterConfig) {
		if (!this.setClusterAndLogger(clusterConfig)) {
			return false;
		}
		ComponentConfig compConf = clusterConfig.getComponents().get(
				this.componentName);
		if (compConf.isRegister() && !AnkushUtils.isMonitoredByAnkush(compConf)) {
			return true;
		}
		logger.info("Unregistering Zookeeper...", this.componentName);
		boolean status = false;
		try {
			final Semaphore semaphore = new Semaphore(compConf.getNodes()
					.size());
			for (String host : compConf.getNodes().keySet()) {
				semaphore.acquire();
				final NodeConfig nodeConfig = clusterConfig.getNodes()
						.get(host);
				final ZookeeperWorker zookeeperWorker = new ZookeeperWorker(
						clusterConfig, this.componentName, nodeConfig);
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						try {
							nodeConfig.setStatus(zookeeperWorker.unregister());
						} catch (Exception e) {
							logger.error(e.getMessage(), componentName, e);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(compConf.getNodes().size());
			status = AnkushUtils.getStatus(clusterConfig, compConf.getNodes()
					.keySet());
		} catch (Exception e) {
			logger.error(e.getMessage(), this.componentName, e);
		}
		if (status) {
			logger.info("Unregistering Zookeeper is over.", componentName);
		} else {
			logger.error("Unregistering Zookeeper failed.", componentName);
			clusterConfig.addError(getComponentName(),
					"Registering Zookeeper failed.");
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#undeploy(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean undeploy(ClusterConfig clusterConfig) {
		if (!this.setClusterAndLogger(clusterConfig)) {
			return false;
		}
		return undeployNodes(clusterConfig);
	}

	/**
	 * Undeploy nodes.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @return true, if successful
	 */
	private boolean undeployNodes(ClusterConfig clusterConfig) {
		logger.info("Undeploying Zookeeper...", this.componentName);
		ComponentConfig compConfig = clusterConfig.getComponents().get(
				this.componentName);
		final Semaphore semaphore = new Semaphore(compConfig.getNodes().size());

		boolean status = false;
		try {
			for (final String host : compConfig.getNodes().keySet()) {
				// acuiring the semaphore
				semaphore.acquire();
				final NodeConfig nodeConfig = clusterConfig.getNodes()
						.get(host);
				final ZookeeperWorker zookeeperWorker = new ZookeeperWorker(
						this.clusterConfig, this.componentName, nodeConfig);
				final ZookeeperServiceMonitor zooServiceMonitor = new ZookeeperServiceMonitor(
						clusterConfig, this.componentName);
				// zooServiceMonitor.setCluster(clusterConfig,
				// this.componentName);
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						// logger.info("Stopping Zookeeper...", componentName);
						// zookeeperWorker.action(ZookeeperConstant.Action.STOP);
						zooServiceMonitor.action(ZookeeperConstant.Action.STOP,
								nodeConfig.getHost());
						logger.info("removing Zookeeper...", componentName);
						zookeeperWorker.removeNode();
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			// waiting for all semaphores to finish the installation.
			semaphore.acquire(compConfig.getNodes().size());
			status = AnkushUtils.getStatus(clusterConfig, compConfig.getNodes()
					.keySet());
		} catch (Exception e) {
			logger.error(e.getMessage(), this.componentName, e);
		}
		if (status) {
			logger.info("Undeploying Zookeeper is over.", componentName);
		} else {
			logger.error("Undeploying Zookeeper failed.", componentName);
			clusterConfig.addError(getComponentName(),
					"Undeploying Zookeeper failed.");
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#start(com.impetus.ankush2.framework
	 * .config.ClusterConfig)
	 */
	// @Override
	// public boolean start(ClusterConfig clusterConfig) {
	// if (!this.setClusterAndLogger(clusterConfig)) {
	// return false;
	// }
	// boolean status = false;
	// try {
	// ComponentConfig compConf = clusterConfig.getComponents().get(
	// this.componentName);
	// final Semaphore semaphore = new Semaphore(compConf.getNodes()
	// .size());
	// for (String host : compConf.getNodes().keySet()) {
	// semaphore.acquire();
	// final NodeConfig nodeConfig = clusterConfig.getNodes()
	// .get(host);
	// final ZookeeperWorker zookeeperWorker = new ZookeeperWorker(
	// clusterConfig, this.componentName, nodeConfig);
	// AppStoreWrapper.getExecutor().execute(new Runnable() {
	// @Override
	// public void run() {
	// logger.info("Starting Zookeeper...", componentName);
	// nodeConfig.setStatus(zookeeperWorker.action("start"));
	// if (semaphore != null) {
	// semaphore.release();
	// }
	// }
	// });
	// semaphore.acquire(compConf.getNodes().size());
	// status = AnkushUtils.getStatus(clusterConfig, compConf
	// .getNodes().keySet());
	// }
	// } catch (Exception e) {
	// logger.error(e.getMessage(), this.componentName, e);
	// }
	// if (status) {
	// logger.info("Starting Zookeeper is over.", componentName);
	// } else {
	// logger.error("Starting Zookeeper failed.", componentName);
	// clusterConfig.addError(getComponentName(),
	// "Starting Zookeeper failed.");
	// }
	// return status;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#stop(com.impetus.ankush2.framework
	 * .config.ClusterConfig)
	 */
	// @Override
	// public boolean stop(ClusterConfig clusterConfig) {
	// if (!this.setClusterAndLogger(clusterConfig)) {
	// return false;
	// }
	// boolean status = false;
	// try {
	// ComponentConfig compConf = clusterConfig.getComponents().get(
	// this.componentName);
	// final Semaphore semaphore = new Semaphore(compConf.getNodes()
	// .size());
	// for (String host : compConf.getNodes().keySet()) {
	// semaphore.acquire();
	// final NodeConfig nodeConfig = clusterConfig.getNodes()
	// .get(host);
	// final ZookeeperWorker zookeeperWorker = new ZookeeperWorker(
	// clusterConfig, this.componentName, nodeConfig);
	// AppStoreWrapper.getExecutor().execute(new Runnable() {
	// @Override
	// public void run() {
	// logger.info("Stopping Zookeeper...", componentName);
	// nodeConfig.setStatus(zookeeperWorker.action("stop"));
	// if (semaphore != null) {
	// semaphore.release();
	// }
	// }
	// });
	// semaphore.acquire(compConf.getNodes().size());
	// status = AnkushUtils.getStatus(clusterConfig, compConf
	// .getNodes().keySet());
	// }
	// } catch (Exception e) {
	// logger.error(e.getMessage(), this.componentName, e);
	// }
	// if (status) {
	// logger.info("Stopping Zookeeper is over.", componentName);
	// } else {
	// logger.error("Stopping Zookeeper failed.", componentName);
	// clusterConfig.addError(getComponentName(),
	// "Stopping Zookeeper failed.");
	// }
	// return status;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#addNode(com.impetus.ankush2.
	 * framework.config.ClusterConfig,
	 * com.impetus.ankush2.framework.config.ClusterConfig)
	 */
	@Override
	public boolean addNode(ClusterConfig conf, ClusterConfig newConf) {
		return false;
		// For future use,when Zookeeper supports Add node.
		// To be handled - from all the nodes server.serverId = host:2888:3888
		// to be copied on the newly added node and new node host to be added on
		// all existing nodes.
		/*
		 * if (!this.setClusterAndLogger(clusterConfig)) { return false; }
		 * ComponentConfig compConf = newConf.getComponents().get(
		 * this.componentName); int nodeId = (Integer)
		 * conf.getComponents().get(this.componentName)
		 * .getAdvanceConf().get(ZookeeperConstant.Keys.LAST_NODE_ID); for
		 * (String host : compConf.getNodes().keySet()) { nodeId++;
		 * compConf.getNodes().get(host) .put(ZookeeperConstant.Keys.NODE_ID,
		 * nodeId); }
		 * conf.getComponents().get(this.componentName).getAdvanceConf()
		 * .put(ZookeeperConstant.Keys.LAST_NODE_ID, nodeId); return
		 * deployNodes(newConf);
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#removeNode(com.impetus.ankush2
	 * .framework.config.ClusterConfig, java.util.Collection)
	 */
	@Override
	public boolean removeNode(ClusterConfig clusterConfig,
			Collection<String> nodes) {
		if (!this.setClusterAndLogger(clusterConfig)) {
			return false;
		}
		boolean status = false;
		logger.error("Zookeeper node cann't be deleted.");
		// logger.info("Undeploying Zookeeper...", this.componentName);
		// final Semaphore semaphore = new Semaphore(nodes.size());
		//
		// boolean status = false;
		// try {
		// for (final String host : nodes) {
		// // acuiring the semaphore
		// semaphore.acquire();
		// final NodeConfig nodeConfig = clusterConfig.getNodes()
		// .get(host);
		// final ZookeeperWorker zookeeperWorker = new ZookeeperWorker(
		// this.clusterConfig, this.componentName, nodeConfig);
		// final ZookeeperServiceMonitor zookeeperServiceMonitor = new
		// ZookeeperServiceMonitor(
		// clusterConfig, getComponentName());
		// AppStoreWrapper.getExecutor().execute(new Runnable() {
		// @Override
		// public void run() {
		// try {
		// nodeConfig.setStatus(zookeeperServiceMonitor
		// .action(ZookeeperConstant.Action.STOP, host));
		// zookeeperWorker.removeNode();
		// } catch (Exception e) {
		// logger.error(e.getMessage(), componentName, e);
		// } finally {
		// if (semaphore != null) {
		// semaphore.release();
		// }
		// }
		// }
		// });
		// }
		// // waiting for all semaphores to finish the installation.
		// semaphore.acquire(nodes.size());
		// status = AnkushUtils.getStatus(clusterConfig, nodes);
		// } catch (Exception e) {
		// logger.error(e.getMessage(), this.componentName, e);
		// }
		// if (status) {
		// logger.info("Removing Zookeeper is over.", componentName);
		// } else {
		// logger.error("Removing Zookeeper failed.", componentName);
		// clusterConfig.addError(getComponentName(),
		// "Removing Zookeeper failed.");
		// }
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#removeNode(com.impetus.ankush2
	 * .framework.config.ClusterConfig,
	 * com.impetus.ankush2.framework.config.ClusterConfig)
	 */
	@Override
	public boolean removeNode(ClusterConfig conf, ClusterConfig newConf) {
		// if (!this.setClusterAndLogger(conf)) {
		// return false;
		// }
		return undeployNodes(newConf);
	}

	// @Override
	// public boolean canNodeBeDeleted(ClusterConfig clusterConfig,
	// Collection<String> nodes) {
	// if (this.setClusterAndLogger(clusterConfig)) {
	// return false;
	// }
	// ComponentConfig compConfig = clusterConfig.getComponents().get(
	// getComponentName());
	// // if all zookeeper nodes come for deletion, it cann't be deleted.
	// if (nodes.size() == compConfig.getNodes().size()) {
	// String errMsg =
	// "Couldn't delete all the Zookeeper nodes from a Cluster.";
	// logger.error(errMsg);
	// clusterConfig.addError(componentName, errMsg);
	// return false;
	// }
	// return true;
	// }
}
