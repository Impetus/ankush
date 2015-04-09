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
package com.impetus.ankush2.hadoop.deployer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.RegisterClusterException;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator;
import com.impetus.ankush2.hadoop.deployer.installer.HadoopInstaller;
import com.impetus.ankush2.hadoop.deployer.servicemanager.HadoopServiceManager;
import com.impetus.ankush2.hadoop.monitor.HadoopMonitor;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopDeployer.
 * 
 * @author Akhil
 */
public class HadoopDeployer extends AbstractDeployer {

	/** The ankushConf Reader. */
	// private ConfigurationReader ankushConf =
	// AppStoreWrapper.getAnkushConfReader();

	private ClusterConfig clusterConfig;

	/** The comp config. */
	private ComponentConfig compConfig;

	/** The hadoop advance conf. */
	private Map<String, Object> hadoopAdvanceConf;

	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(HadoopDeployer.class);

	/**
	 * Intitialize data members.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @return true, if successful
	 */
	private boolean intitializeDataMembers(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		this.LOG.setCluster(clusterConfig);
		try {
			this.compConfig = HadoopUtils.getHadoopConfig(clusterConfig);
			if (this.compConfig == null) {

				String errMsg = Constant.Strings.ExceptionsMessage.INVALID_CLUSTER_CONFIG_MSG
						+ ": Hadoop component missing.";

				HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
						this.getComponentName());
				return false;
			}

			this.hadoopAdvanceConf = this.compConfig.getAdvanceConf();

			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Error while initializing Hadoop deployer",
					this.getComponentName(), e);
			return false;
		}
	}

	/**
	 * Update adv conf set nodes.
	 * 
	 * @return true, if successful
	 */
	private boolean updateAdvConfSetNodes() {
		try {
			Set<String> slaves = new HashSet<String>();

			Set<String> journalNodes = new HashSet<String>();

			for (String host : this.clusterConfig.getNodes().keySet()) {

				Set<String> roles = clusterConfig.getNodes().get(host)
						.getRoles().get(Constant.Component.Name.HADOOP);

				if (roles.isEmpty()) {
					String errorMsg = Constant.Strings.ExceptionsMessage.INVALID_COMPONENT_CONFIG_MSG
							+ ": Roles empty for node - " + host;
					HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
							this.getComponentName());
					return false;
				}
				if (roles.contains(HadoopConstants.Roles.NAMENODE)) {
					this.compConfig.addAdvanceConfProperty(
							HadoopConstants.AdvanceConfKeys.NAMENODE, host);

					if (HadoopUtils.isHadoop2Config(compConfig)) {
						boolean isHaEnabled = (Boolean) this.compConfig
								.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);
						if (isHaEnabled) {
							this.compConfig
									.addAdvanceConfProperty(
											HadoopConstants.AdvanceConfKeys.NAMENODE,
											this.compConfig
													.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ACTIVE_NAMENODE));
						}
					}
				}
				if (roles.contains(HadoopConstants.Roles.RESOURCEMANAGER)) {
					this.compConfig.addAdvanceConfProperty(
							HadoopConstants.AdvanceConfKeys.RESOURCE_MANAGER,
							host);
				}
				if (roles.contains(HadoopConstants.Roles.WEBAPPPROXYSERVER)) {
					this.compConfig
							.addAdvanceConfProperty(
									HadoopConstants.AdvanceConfKeys.WEB_APP_PROXY_SERVER,
									host);
				}
				if (roles.contains(HadoopConstants.Roles.JOBHISTORYSERVER)) {
					this.compConfig.addAdvanceConfProperty(
							HadoopConstants.AdvanceConfKeys.JOB_HISTORY_SERVER,
							host);
				}
				if (roles.contains(HadoopConstants.Roles.JOBTRACKER)) {
					this.compConfig.addAdvanceConfProperty(
							HadoopConstants.AdvanceConfKeys.JOBTRACKER, host);
				}
				if (roles.contains(HadoopConstants.Roles.SECONDARYNAMENODE)) {
					this.compConfig.addAdvanceConfProperty(
							HadoopConstants.AdvanceConfKeys.SECONDARY_NAMENODE,
							host);
				}

				if (roles.contains(HadoopConstants.Roles.DATANODE)) {
					slaves.add(host);
				}
				if (roles.contains(HadoopConstants.Roles.JOURNALNODE)) {
					journalNodes.add(host);
				}
			}
			this.compConfig.addAdvanceConfProperty(
					HadoopConstants.AdvanceConfKeys.SLAVES, slaves);
			this.compConfig.addAdvanceConfProperty(
					HadoopConstants.AdvanceConfKeys.HA_JOURNALNODES,
					journalNodes);

		} catch (Exception e) {
			String errorMsg = Constant.Strings.ExceptionsMessage.INVALID_COMPONENT_CONFIG_MSG
					+ ": Invalid nodes object for Hadoop";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
					this.getComponentName(), e);
			return false;
		}
		return true;
	}

	private String fetchHadoopHomeDirFromJmx(String nameNodeHost,
			String httpPortNameNode) throws RegisterClusterException {
		String errMsg = "Could not get NameNode java runtime data from host-"
				+ nameNodeHost + ", port-" + httpPortNameNode + ".";
		try {
			LOG.info("Connecting to " + nameNodeHost + ":" + httpPortNameNode
					+ " to get NameNode java runtime data.",
					Constant.Component.Name.HADOOP, nameNodeHost);
			String beanName = HadoopMonitor.JMX_BEAN_NAME_JAVA_RUNTIME;
			Map<String, Object> beanObject = HadoopUtils
					.getJmxBeanUsingCallable(nameNodeHost, httpPortNameNode,
							beanName);
			if (beanObject != null) {
				List<String> runtimeArgsList = (List<String>) beanObject
						.get(HadoopConstants.Hadoop.Keys.JMX_JAVA_RUNTIME_INPUT_ARGUMENTS_KEY);
				return HadoopUtils.getHadoopHomeDir(runtimeArgsList);
			} else {
				HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
						Constant.Component.Name.HADOOP);
				throw new RegisterClusterException(errMsg);
			}
		} catch (RegisterClusterException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new RegisterClusterException(errMsg);
		}
	}

	private boolean saveJmxInformation() throws RegisterClusterException {
		StringBuilder errMsg = new StringBuilder(
				"Could not get NameNode information");
		try {

			String nameNodeHost = (String) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.NAMENODE);
			String httpPortNameNode = String
					.valueOf(this.compConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HttpPort.NAMENODE));

			errMsg.append(" using host-").append(nameNodeHost)
					.append(" and Http Port-").append(httpPortNameNode)
					.append(".");

			this.compConfig.setHomeDir(this.fetchHadoopHomeDirFromJmx(
					nameNodeHost, httpPortNameNode));
			this.compConfig
					.setNodes(new HashMap<String, Map<String, Object>>());

			LOG.info("Connecting to " + nameNodeHost + ":" + httpPortNameNode
					+ " to get NameNode information.",
					Constant.Component.Name.HADOOP, nameNodeHost);
			String beanName = HadoopMonitor.JMX_BEAN_NAME_NAMENODE_INFO;
			Map<String, Object> beanObject = HadoopUtils
					.getJmxBeanUsingCallable(nameNodeHost, httpPortNameNode,
							beanName);
			if (beanObject != null) {
				this.compConfig
						.setVersion(String
								.valueOf(
										beanObject
												.get(HadoopConstants.JmxBeanKeys.DfsData.VERSION))
								.split(",")[0]);

				this.compConfig.addAdvanceConfProperty(
						HadoopConstants.AdvanceConfKeys.HA_ENABLED, false);
				this.compConfig
						.addAdvanceConfProperty(
								HadoopConstants.AdvanceConfKeys.JOBHISTORYSERVER_ENABLED,
								false);
				this.compConfig
						.addAdvanceConfProperty(
								HadoopConstants.AdvanceConfKeys.WEBAPPPROXYSERVER_ENABLED,
								false);

				HadoopConfigurator hadoopConfigurator = HadoopUtils
						.getConfiguratorInstance(this.clusterConfig,
								this.compConfig);
				Set<String> slavesList = HadoopUtils
						.getSlavesListFromJmx(beanObject);
				this.compConfig.addAdvanceConfProperty(
						HadoopConstants.AdvanceConfKeys.SLAVES, slavesList);

				if (HadoopUtils.isMonitoredByAnkush(this.compConfig)) {
					if (!hadoopConfigurator.saveNodesWhileRegitration()) {
						HadoopUtils
								.addAndLogError(
										LOG,
										clusterConfig,
										"Could not fetch Hadoop nodes information from JMX data.",
										Constant.Component.Name.HADOOP);
						throw new RegisterClusterException(errMsg.toString());
					}

				}
			} else {
				HadoopUtils.addAndLogError(LOG, clusterConfig,
						errMsg.toString(), Constant.Component.Name.HADOOP);
				throw new RegisterClusterException(errMsg.toString());
			}
			return true;
		} catch (RegisterClusterException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg.toString(),
					Constant.Component.Name.HADOOP, e);
			throw new RegisterClusterException(errMsg.toString());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#createConfig(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean createConfig(ClusterConfig conf) {
		try {
			LOG.setCluster(conf);
			LOG.info("Creating deployer configuration for HadoopDeployer",
					this.getComponentName());

			if (!intitializeDataMembers(conf)) {
				return false;
			}

			boolean deployComponentFlag = !this.compConfig.isRegister();

			if (deployComponentFlag) {
				String installPath = FileUtils
						.getSeparatorTerminatedPathEntry(this.compConfig
								.getInstallPath());
				this.compConfig.setHomeDir(installPath
						+ Constant.Component.Name.HADOOP.toLowerCase() + "-"
						+ this.compConfig.getVersion() + "/");
				if (!this.updateAdvConfSetNodes()) {
					String errmsg = Constant.Strings.ExceptionsMessage.INVALID_COMPONENT_CONFIG_MSG
							+ ": Unable to identify Hadoop nodes.";
					LOG.error(errmsg);
					clusterConfig.addError(this.getComponentName(), errmsg);
					return false;
				}
				HadoopConfigurator hadoopConfigurator = HadoopUtils
						.getConfiguratorInstance(clusterConfig, compConfig);

				hadoopConfigurator.setHadoopConfDir();
				hadoopConfigurator.setHadoopScriptDir();
				hadoopConfigurator.setHadoopBinDir();
				hadoopConfigurator.setRpcPorts();
			} else {
				this.saveJmxInformation();
				this.compConfig.addAdvanceConfProperty(
						HadoopConstants.AdvanceConfKeys.HA_ENABLED, false);
				HadoopConfigurator hadoopConfigurator = HadoopUtils
						.getConfiguratorInstance(clusterConfig, compConfig);
				hadoopConfigurator.setHadoopConfDir();
			}

			this.compConfig.addAdvanceConfProperty(
					HadoopConstants.AdvanceConfKeys.IS_HADOOP2,
					HadoopUtils.isHadoop2Config(this.compConfig));

			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not create configuration for Hadoop",
					Constant.Component.Name.HADOOP, e);
			return false;
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
		if (!this.intitializeDataMembers(conf)) {
			return false;
		}
		boolean status = false;
		ComponentConfig compConf = clusterConfig.getComponents().get(
				Constant.Component.Name.HADOOP);
		try {
			if (compConf.isRegister()) {
				status = true;
			} else {
				final Semaphore semaphore = new Semaphore(compConf.getNodes()
						.size());
				for (String host : compConf.getNodes().keySet()) {
					semaphore.acquire();
					final HadoopValidator hadoopValidator = new HadoopValidator(
							clusterConfig, clusterConfig.getNodes().get(host));
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							hadoopValidator.validate();
							if (semaphore != null) {
								semaphore.release();
							}
						}
					});
				}
				// waiting for all semaphores to finish the installation.
				semaphore.acquire(compConf.getNodes().size());
				status = AnkushUtils.getStatus(clusterConfig, compConf
						.getNodes().keySet());
			}
		} catch (Exception e) {
			String errorMsg = Constant.Strings.ExceptionsMessage.INVALID_COMPONENT_CONFIG_MSG
					+ ": Validation failded for Hadoop";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
					this.getComponentName(), e);
			return false;
		}
		if (status) {
			LOG.info("Validating Hadoop is successful", this.getComponentName());
		} else {
			String errorMsg = Constant.Strings.ExceptionsMessage.INVALID_COMPONENT_CONFIG_MSG
					+ ": Validation failded for Hadoop";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
					this.getComponentName());
			return false;
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
	public boolean deploy(ClusterConfig conf) {
		try {
			if (!intitializeDataMembers(conf)) {
				return false;
			}
			LOG.setCluster(conf);
			LOG.info("Deploying Hadoop", Constant.Component.Name.HADOOP);

			HadoopConfigurator hadoopConfigurator = HadoopUtils
					.getConfiguratorInstance(clusterConfig, compConfig);

			if (!hadoopConfigurator.setupPasswordlessSSH(this.compConfig
					.getNodes().keySet())) {
				// Do not return if PasswordLess SSH setup fails
				// return false;
			}
			if (!(installAndConfigureNodes() && initializeAndStartCluster())) {
				return false;
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not deploy Hadoop", Constant.Component.Name.HADOOP,
					e);
			return false;
		}

		return true;
	}

	/**
	 * Removes the component.
	 * 
	 * @return true, if successful
	 */
	private boolean removeComponent() {
		LOG.info("Removing Hadoop component", this.getComponentName());
		return this.removeNodeOperation(clusterConfig, this.compConfig
				.getNodes().keySet());
	}

	/**
	 * Install and configure nodes.
	 * 
	 * @return true, if successful
	 */
	private boolean installAndConfigureNodes() {
		final Semaphore semaphore = new Semaphore(this.compConfig.getNodes()
				.size());
		try {
			for (final String host : this.compConfig.getNodes().keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {

						try {

							HadoopInstaller hadoopInstaller = HadoopUtils
									.getInstallerInstance(clusterConfig,
											compConfig);

							HadoopConfigurator hadoopConfigurator = HadoopUtils
									.getConfiguratorInstance(clusterConfig,
											compConfig);

							HadoopServiceManager hadoopServiceManager = HadoopUtils
									.getServiceManagerInstance(clusterConfig,
											compConfig);

							boolean status = hadoopInstaller
									.createNode(clusterConfig.getNodes().get(
											host))
									&& hadoopConfigurator
											.configureNode(clusterConfig
													.getNodes().get(host))
									&& hadoopServiceManager.registerServices(
											clusterConfig.getNodes().get(host),
											false);
							clusterConfig.getNodes().get(host)
									.setStatus(status);
						} catch (Exception e) {
							HadoopUtils.addAndLogError(LOG, clusterConfig,
									"Could not install and configure Hadoop",
									Constant.Component.Name.HADOOP, host, e);
							clusterConfig.getNodes().get(host).setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}

			semaphore.acquire(this.compConfig.getNodes().size());
			boolean namenodeStatus = clusterConfig.getNodes()
					.get(HadoopUtils.getNameNodeHost(compConfig)).getStatus();
			if (!namenodeStatus) {
				HadoopUtils
						.addAndLogError(
								LOG,
								clusterConfig,
								"Could not install and configure Hadoop NameNode - Initiating rollback",
								Constant.Component.Name.HADOOP);
			}
			return namenodeStatus;
			// return AnkushUtils.getStatus(clusterConfig, compConfig.getNodes()
			// .keySet());
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not install and configure Hadoop",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/**
	 * Initialize and start cluster.
	 * 
	 * @return true, if successful
	 */
	private boolean initializeAndStartCluster() {
		try {
			HadoopConfigurator hadoopConfigurator = HadoopUtils
					.getConfiguratorInstance(clusterConfig, compConfig);
			return hadoopConfigurator.initializeAndStartCluster();
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not initialize and start Hadoop cluster",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.Deployable#undeploy(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean undeploy(ClusterConfig conf) {
		boolean status = true;
		try {
			LOG.setCluster(conf);
			if (!intitializeDataMembers(conf)) {
				return false;
			}

			LOG.info("Undeploying Hadoop", Constant.Component.Name.HADOOP);

			HadoopConfigurator hadoopConfigurator = HadoopUtils
					.getConfiguratorInstance(clusterConfig, compConfig);
			if (!compConfig.isRegister()) {
				status = hadoopConfigurator
						.manageComponent(HadoopConstants.Command.Action.STOP);
				status = removeComponent() && status;
			} else {
				LOG.warn(
						"Skipping stop services and remove node operations for registered component",
						Constant.Component.Name.HADOOP);
			}

		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not undeploy Hadoop",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.AbstractDeployer#addNode(com.impetus.ankush2
	 * .framework.config.ClusterConfig,
	 * com.impetus.ankush2.framework.config.ClusterConfig)
	 */
	@Override
	public boolean addNode(ClusterConfig clusterConfig,
			ClusterConfig newClusterConfig) {
		try {
			LOG.setCluster(clusterConfig);
			LOG.info("Initializing add nodes operation for Hadoop.",
					this.getComponentName());

			if (!intitializeDataMembers(clusterConfig)) {
				return false;
			}
			newClusterConfig.setAuthConf(clusterConfig.getAuthConf());
			newClusterConfig.setJavaConf(clusterConfig.getJavaConf());
			newClusterConfig.getComponents().put(
					Constant.Component.Name.AGENT,
					clusterConfig.getComponents().get(
							Constant.Component.Name.AGENT));

			HadoopConfigurator hadoopConfigurator = HadoopUtils
					.getConfiguratorInstance(newClusterConfig, this.compConfig);
			hadoopConfigurator.getLOG().setCluster(clusterConfig);

			if (!hadoopConfigurator.setupPasswordlessSSH(newClusterConfig
					.getComponents().get(Constant.Component.Name.HADOOP)
					.getNodes().keySet())) {
				return false;
			}

			if (!(addAndConfigureNodes(newClusterConfig) && startSlaveNodes(newClusterConfig))) {
				return false;
			}

			if (!postAddOperation(newClusterConfig)) {
				return false;
			}

		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, newClusterConfig,
					"Could not add node for Hadoop.",
					Constant.Component.Name.HADOOP, e);
			return false;
		} finally {
			newClusterConfig.setAuthConf(null);
			newClusterConfig.setJavaConf(null);
			newClusterConfig.getComponents().remove(
					clusterConfig.getComponents().get(
							Constant.Component.Name.AGENT));
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.AbstractDeployer#removeNode(com.impetus
	 * .ankush2.framework.config.ClusterConfig, java.util.Collection)
	 */
	@Override
	public boolean removeNode(ClusterConfig conf, Collection<String> nodes) {
		try {
			this.intitializeDataMembers(conf);
			removeNodeOperation(conf, nodes);
			// postRemoveOperation(nodes);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.AbstractDeployer#removeNode(com.impetus
	 * .ankush2.framework.config.ClusterConfig,
	 * com.impetus.ankush2.framework.config.ClusterConfig)
	 */
	@Override
	public boolean removeNode(ClusterConfig conf, ClusterConfig newClusterConfig) {
		this.intitializeDataMembers(conf);
		Set<String> nodes = newClusterConfig.getComponents()
				.get(Constant.Component.Name.HADOOP).getNodes().keySet();
		removeNodeOperation(conf, nodes);
		// postRemoveOperation(nodes);
		return true;
	}

	/**
	 * Removes the node operation.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodes
	 *            the nodes
	 * @return true, if successful
	 */
	private boolean removeNodeOperation(ClusterConfig conf,
			Collection<String> nodes) {
		final Semaphore semaphore = new Semaphore(nodes.size());
		try {
			for (final String host : nodes) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							HadoopInstaller hadoopInstaller = HadoopUtils
									.getInstallerInstance(clusterConfig,
											compConfig);

							HadoopServiceManager hadoopServiceManager = HadoopUtils
									.getServiceManagerInstance(clusterConfig,
											compConfig);

							Set<String> roles = clusterConfig.getNodes()
									.get(host).getRoles()
									.get(Constant.Component.Name.HADOOP);

							boolean status = hadoopServiceManager
									.unregisterServices(clusterConfig
											.getNodes().get(host));

							if (!compConfig.isRegister()) {

								if (clusterConfig.getState().equals(
										Constant.Cluster.State.DEPLOYED)) {
									status = hadoopServiceManager
											.manageServices(
													clusterConfig,
													host,
													roles,
													HadoopConstants.Command.Action.STOP)
											&& status;
								}

								status = hadoopInstaller
										.removeNode(clusterConfig.getNodes()
												.get(host))
										&& status;

							} else {
								LOG.warn(
										"Skipping stop services and remove node operations for registered component",
										Constant.Component.Name.HADOOP, host);
							}
							clusterConfig.getNodes().get(host)
									.setStatus(status);
						} catch (Exception e) {
							HadoopUtils
									.addAndLogError(
											LOG,
											clusterConfig,
											"Could not remove and unregister Hadoop from node",
											Constant.Component.Name.HADOOP,
											host, e);
							clusterConfig.getNodes().get(host).setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(nodes.size());
			return postRemoveOperation(nodes)
					&& AnkushUtils.getStatus(clusterConfig, nodes);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not remove and unregister Hadoop",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/**
	 * Post add operation.
	 * 
	 * @param newClusterConfig
	 *            the new cluster config
	 * @return true, if successful
	 */
	private boolean postAddOperation(ClusterConfig newClusterConfig) {
		Set<String> slaveNodesList = new HashSet<String>();
		slaveNodesList.addAll(HadoopUtils.getSlaveHosts(this.compConfig));
		slaveNodesList.addAll(newClusterConfig.getComponents()
				.get(Constant.Component.Name.HADOOP).getNodes().keySet());

		if (!updateSlavesFileOnNodes(newClusterConfig, this.compConfig
				.getNodes().keySet(), slaveNodesList)) {
			String errMsg = "Could not update slaves file on remaining nodes, please update it manually.";
			HadoopUtils.addAndLogError(LOG, newClusterConfig, errMsg,
					Constant.Component.Name.HADOOP);
		}
		updateSlavesInAdvConf(slaveNodesList);
		return true;
	}

	/**
	 * Update slaves file on nodes.
	 * 
	 * @param newClusterConfig
	 *            the new cluster config
	 * @param nodeList
	 *            the node list
	 * @param newSlaveNodesList
	 *            the new slave nodes list
	 * @return true, if successful
	 */
	private boolean updateSlavesFileOnNodes(
			final ClusterConfig newClusterConfig, final Set<String> nodeList,
			final Set<String> newSlaveNodesList) {
		try {
			AnkushUtils.connectNodesString(clusterConfig, nodeList);

			final Semaphore semaphore = new Semaphore(nodeList.size());

			for (final String host : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						String errMsg = "Could not update slaves file on node - "
								+ host + ", please update it manually.";
						try {
							HadoopConfigurator hadoopConfigurator = HadoopUtils
									.getConfiguratorInstance(clusterConfig,
											compConfig);
							hadoopConfigurator.getLOG().setCluster(
									newClusterConfig);
							if (!hadoopConfigurator.configureSlavesFile(
									clusterConfig.getNodes().get(host),
									newSlaveNodesList)) {
								HadoopUtils.addAndLogError(LOG,
										newClusterConfig, errMsg,
										Constant.Component.Name.HADOOP, host);
							}
						} catch (Exception e) {
							HadoopUtils.addAndLogError(LOG, newClusterConfig,
									errMsg, Constant.Component.Name.HADOOP,
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
			String errMsg = "Could not update slaves file on remaining nodes, please update it manually.";
			HadoopUtils.addAndLogError(LOG, newClusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			return true;
		} finally {
			AnkushUtils.disconnectNodesString(clusterConfig, nodeList);
		}
		return true;
	}

	/**
	 * Update slaves in adv conf.
	 * 
	 * @param newSlaveNodesList
	 *            the new slave nodes list
	 */
	private void updateSlavesInAdvConf(Set<String> newSlaveNodesList) {
		this.compConfig.addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.SLAVES, newSlaveNodesList);
	}

	/**
	 * Post remove operation.
	 * 
	 * @param nodes
	 *            the nodes
	 * @return true, if successful
	 */
	private boolean postRemoveOperation(Collection<String> nodes) {
		Set<String> slaveNodesList = new HashSet<String>();
		slaveNodesList.addAll(HadoopUtils.getSlaveHosts(this.compConfig));
		slaveNodesList.removeAll(nodes);
		LOG.info("Updating slaves list on remaining nodes",
				Constant.Component.Name.HADOOP);
		Set<String> hadoopNodes = new HashSet<String>();
		hadoopNodes.addAll(this.compConfig.getNodes().keySet());
		hadoopNodes.removeAll(nodes);
		
		if (!updateSlavesFileOnNodes(clusterConfig, hadoopNodes, slaveNodesList)) {
			String errMsg = "Could not update slaves file on remaining nodes, please update it manually.";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP);
		}
		updateSlavesInAdvConf(slaveNodesList);
		return true;
	}

	/**
	 * Start slave nodes.
	 * 
	 * @param newClusterConfig
	 *            the new cluster config
	 * @return true, if successful
	 */
	private boolean startSlaveNodes(final ClusterConfig newClusterConfig) {
		Set<String> nodes = newClusterConfig.getComponents()
				.get(Constant.Component.Name.HADOOP).getNodes().keySet();

		final Semaphore semaphore = new Semaphore(nodes.size());
		try {
			for (final String host : nodes) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {

						try {
							HadoopServiceManager hadoopServiceManager = HadoopUtils
									.getServiceManagerInstance(
											newClusterConfig, compConfig);

							boolean status = hadoopServiceManager.manageNode(
									newClusterConfig, host,
									HadoopConstants.Command.Action.START);

							newClusterConfig.getNodes().get(host)
									.setStatus(status);
						} catch (Exception e) {
							HadoopUtils.addAndLogError(LOG, newClusterConfig,
									"Could not start Hadoop services on node - "
											+ host + ".",
									Constant.Component.Name.HADOOP, host, e);
							newClusterConfig.getNodes().get(host)
									.setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(nodes.size());
			return AnkushUtils.getStatus(newClusterConfig, nodes);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not start Hadoop services on nodes.",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/**
	 * Adds the and configure nodes.
	 * 
	 * @param newClusterConfig
	 *            the new cluster config
	 * @return true, if successful
	 */
	private boolean addAndConfigureNodes(final ClusterConfig newClusterConfig) {

		Set<String> nodes = newClusterConfig.getComponents()
				.get(Constant.Component.Name.HADOOP).getNodes().keySet();

		final Semaphore semaphore = new Semaphore(nodes.size());
		try {
			for (final String host : nodes) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {

							HadoopInstaller hadoopInstaller = HadoopUtils
									.getInstallerInstance(newClusterConfig,
											compConfig);
							hadoopInstaller.getLOG().setCluster(clusterConfig);

							HadoopConfigurator hadoopConfigurator = HadoopUtils
									.getConfiguratorInstance(newClusterConfig,
											compConfig);
							hadoopConfigurator.getLOG().setCluster(
									clusterConfig);

							HadoopServiceManager hadoopServiceManager = HadoopUtils
									.getServiceManagerInstance(
											newClusterConfig, compConfig);
							hadoopServiceManager.getLOG().setCluster(
									clusterConfig);

							Set<String> slaveNodesList = new HashSet<String>();
							slaveNodesList.addAll(HadoopUtils
									.getSlaveHosts(compConfig));
							slaveNodesList.addAll(newClusterConfig
									.getComponents()
									.get(Constant.Component.Name.HADOOP)
									.getNodes().keySet());

							NodeConfig nodeConfig = newClusterConfig.getNodes()
									.get(host);
							boolean status = hadoopInstaller
									.addNode(nodeConfig)
									&& hadoopConfigurator
											.configureEnvironmentVariables(
													nodeConfig, true)
									&& hadoopConfigurator.configureSlavesFile(
											nodeConfig, slaveNodesList)
									&& hadoopServiceManager.registerServices(
											nodeConfig, true);

							newClusterConfig.getNodes().get(host)
									.setStatus(status);
						} catch (Exception e) {
							HadoopUtils.addAndLogError(LOG, newClusterConfig,
									"Could not install and configure Hadoop",
									Constant.Component.Name.HADOOP, host, e);
							newClusterConfig.getNodes().get(host)
									.setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(nodes.size());
			return AnkushUtils.getStatus(newClusterConfig, nodes);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not add and configure Hadoop nodes.",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.AbstractDeployer#register(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean register(ClusterConfig conf) {
		try {
			if (!intitializeDataMembers(conf)) {
				return false;
			}
			if (!HadoopUtils.isMonitoredByAnkush(this.compConfig)) {
				LOG.info("Skipping Hadoop registration",
						Constant.Component.Name.HADOOP);
				return true;
			}
			LOG.setCluster(conf);
			LOG.info("Registering Hadoop", Constant.Component.Name.HADOOP);

			// boolean isHadoop2 = HadoopUtils.isHadoop2Config(this.compConfig);
			// if (isHadoop2) {
			// boolean isWebAppProxyServerEnabled = this.compConfig
			// .getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.WEBAPPPROXYSERVER_ENABLED);
			//
			// if (isWebAppProxyServerEnabled) {
			// String webAppProxyServer = HadoopUtils
			// .getWebAppProxyServerHost(this.compConfig);
			// }
			// }

			final Semaphore semaphore = new Semaphore(this.compConfig
					.getNodes().size());

			for (final String host : this.compConfig.getNodes().keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							HadoopServiceManager hadoopServiceManager = HadoopUtils
									.getServiceManagerInstance(clusterConfig,
											compConfig);

							boolean status = hadoopServiceManager
									.registerServices(clusterConfig.getNodes()
											.get(host), false);
							clusterConfig.getNodes().get(host)
									.setStatus(status);
						} catch (Exception e) {
							HadoopUtils.addAndLogError(LOG, clusterConfig,
									"Could not register Hadoop node - " + host,
									Constant.Component.Name.HADOOP, host, e);
							clusterConfig.getNodes().get(host).setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(this.compConfig.getNodes().size());
			return AnkushUtils.getStatus(clusterConfig, compConfig.getNodes()
					.keySet());
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not register Hadoop",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.AbstractDeployer#unregister(com.impetus
	 * .ankush2.framework.config.ClusterConfig)
	 */
	@Override
	public boolean unregister(ClusterConfig conf) {
		try {
			if (!intitializeDataMembers(conf)) {
				return false;
			}
			LOG.setCluster(conf);
			if (!HadoopUtils.isMonitoredByAnkush(this.compConfig)) {
				LOG.info("Skipping Hadoop unregistration",
						Constant.Component.Name.HADOOP);
				return true;
			}
			LOG.info("Unregistering Hadoop", Constant.Component.Name.HADOOP);

			final Semaphore semaphore = new Semaphore(this.compConfig
					.getNodes().size());

			for (final String host : this.compConfig.getNodes().keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							HadoopServiceManager hadoopServiceManager = HadoopUtils
									.getServiceManagerInstance(clusterConfig,
											compConfig);

							boolean status = hadoopServiceManager
									.unregisterServices(clusterConfig
											.getNodes().get(host));
							clusterConfig.getNodes().get(host)
									.setStatus(status);
						} catch (Exception e) {
							HadoopUtils.addAndLogError(LOG, clusterConfig,
									"Could not unregister Hadoop node - "
											+ host,
									Constant.Component.Name.HADOOP, host, e);
							clusterConfig.getNodes().get(host).setStatus(false);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(this.compConfig.getNodes().size());
			return AnkushUtils.getStatus(clusterConfig, compConfig.getNodes()
					.keySet());

		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not unregister Hadoop",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	@Override
	public Set<String> getNodesForDependenciesDeployment(
			ClusterConfig clusterConfig) {
		try {
			if (!intitializeDataMembers(clusterConfig)) {
				return null;
			}
			if (clusterConfig.getState()
					.equals(Constant.Cluster.State.DEPLOYED)) {
				return null;
			} else {
				if (HadoopUtils.isHdfsHaEnabled(compConfig)) {
					return HadoopUtils.getHaNameNodeHosts(compConfig);
				} else {
					return Collections.singleton(HadoopUtils
							.getNameNodeHost(compConfig));
				}
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not get nodes list for Dependency deployer",
					Constant.Component.Name.HADOOP, e);
			return null;
		}
	}

	// @Override
	// public Map<String, Map<String, Object>> canNodeBeDeleted(ClusterConfig
	// clusterConfig,
	// Collection<String> nodes) {
	// this.intitializeDataMembers(clusterConfig);
	// try {
	// HadoopConfigurator hadoopConfig =
	// HadoopUtils.getConfiguratorInstance(this.clusterConfig, compConfig);
	// return hadoopConfig.canNodesBeDeleted(nodes);
	// } catch (Exception e) {
	// // TODO: handle exception
	// return false;
	// }
	// return true;
	// }

}
