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
package com.impetus.ankush2.hadoop.deployer.servicemanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.Semaphore;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.config.ComponentConfigContext;
import com.impetus.ankush2.hadoop.deployer.configurator.Hadoop1Configurator;
import com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopServiceComparator;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.logger.AnkushLogger;

// TODO: Auto-generated Javadoc
// Need to add another class to implement functions related Hadoop Service Management as specified in ankush-component-config.xml
/**
 * The Class HadoopServiceManager.
 * 
 * @author Akhil
 */
public class HadoopServiceManager extends ComponentConfigContext implements Serviceable {

	/**
	 * Instantiates a new hadoop service manager.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param hadoopConfig
	 *            the hadoop config
	 * @param classObj
	 *            the class obj
	 */
	public HadoopServiceManager(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig, Class classObj) {
		super(clusterConfig, hadoopConfig, classObj);
	}

	/** The manager service status. */
	private boolean manageServiceRequestStatus = true;

	/**
	 * Instantiates a new hadoop service manager.
	 */
	public HadoopServiceManager() {

	}

	@Override
	public Set<String> getServiceList(ClusterConfig clusterConfig) {

		try {
			this.intitializeDataMembers(clusterConfig);
			HadoopConfigurator hadoopConfigurator = HadoopUtils
					.getConfiguratorInstance(clusterConfig, compConfig);
			return hadoopConfigurator.getHAServicesList();
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, clusterConfig,
					"Unable to get HA service list.",
					Constant.Component.Name.HADOOP, e);
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.manager.ServiceMonitorable#startServices
	 * (com.impetus.ankush2.framework.config.ClusterConfig, java.lang.String,
	 * java.util.Set)
	 */
	@Override
	public boolean startServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		this.intitializeDataMembers(clusterConfig);
		LOG.info("Starting Hadoop services on - " + host,
				Constant.Component.Name.HADOOP, host);
		return manageServices(clusterConfig, host, services,
				HadoopConstants.Command.Action.START);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.manager.ServiceMonitorable#stopServices
	 * (com.impetus.ankush2.framework.config.ClusterConfig, java.lang.String,
	 * java.util.Set)
	 */
	@Override
	public boolean stopServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		this.intitializeDataMembers(clusterConfig);
		LOG.info("Stopping Hadoop services on - " + host,
				Constant.Component.Name.HADOOP, host);
		return manageServices(clusterConfig, host, services,
				HadoopConstants.Command.Action.STOP);
	}

	/**
	 * Manage services.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param host
	 *            the host
	 * @param services
	 *            the services
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	public boolean manageServices(ClusterConfig clusterConfig, String host,
			Set<String> services, String action) {
		boolean status = true;
		if (!HadoopUtils.isManagedByAnkush(this.compConfig)) {
			HadoopUtils.addAndLogError(this.LOG, clusterConfig, "Could not "
					+ action + " Hadoop services: "
					+ Constant.Registration.ErrorMsg.NOT_MANAGED_MODE,
					Constant.Component.Name.HADOOP, host);
			return false;
		}
		try {
			PriorityQueue<String> serviceQueue = new PriorityQueue<String>(
					services.size(), new HadoopServiceComparator(true));
			serviceQueue.addAll(services);
			while (!serviceQueue.isEmpty()) {
				String serviceName = serviceQueue.remove();
				try {
					HadoopServiceManager manageService = HadoopUtils
							.getServiceManagerInstance(clusterConfig,
									compConfig);
					status = manageService.manageServiceOnNode(host,
							serviceName, action) && status;
				} catch (Exception e) {
					HadoopUtils.addAndLogError(this.LOG, clusterConfig,
							"Could not " + action + " " + serviceName
									+ " service.",
							Constant.Component.Name.HADOOP, host);
					status = false;
				}
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, clusterConfig,
					"Could not stop Hadoop services.",
					Constant.Component.Name.HADOOP, host, e);
			status = false;
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.manager.ServiceMonitorable#start(com.impetus
	 * .ankush2.framework.config.ClusterConfig)
	 */
	@Override
	public boolean start(ClusterConfig clusterConfig) {
		return manageComponent(clusterConfig,
				HadoopConstants.Command.Action.START);
	}

	/**
	 * Manage component.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	private boolean manageComponent(ClusterConfig clusterConfig, String action) {
		try {
			this.intitializeDataMembers(clusterConfig);
			if (!HadoopUtils.isManagedByAnkush(this.compConfig)) {
				HadoopUtils
						.addAndLogError(
								this.LOG,
								clusterConfig,
								"Could not "
										+ action
										+ " Hadoop component: "
										+ Constant.Registration.ErrorMsg.NOT_MANAGED_MODE,
								Constant.Component.Name.HADOOP);
				return false;
			}

			LOG.info("Performing " + action + " Hadoop operation.",
					Constant.Component.Name.HADOOP);
			HadoopConfigurator hadoopConfigurator = HadoopUtils
					.getConfiguratorInstance(this.clusterConfig, compConfig);
			return hadoopConfigurator.manageComponent(action);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not " + action + " Hadoop component",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.manager.ServiceMonitorable#stop(com.impetus
	 * .ankush2.framework.config.ClusterConfig)
	 */
	@Override
	public boolean stop(ClusterConfig clusterConfig) {
		return manageComponent(clusterConfig,
				HadoopConstants.Command.Action.STOP);
	}

	/**
	 * Unregister services.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	public boolean unregisterServices(NodeConfig nodeConfig) {
		LOG.info("Removing service xml configuration file",
				Constant.Component.Name.HADOOP, nodeConfig.getHost());
		return com.impetus.ankush2.agent.AgentUtils.removeServiceXml(
				nodeConfig.getConnection(), Constant.Component.Name.HADOOP);
	}

	/**
	 * Intitialize data members.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @return true, if successful
	 */
	protected boolean intitializeDataMembers(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		try {
			if (this.LOG == null) {
				this.setLOG(new AnkushLogger(HadoopServiceManager.class,
						clusterConfig));
			} else {
				this.setLOG(HadoopServiceManager.class);
			}
			this.compConfig = HadoopUtils.getHadoopConfig(clusterConfig);
			if (this.compConfig == null) {

				String errMsg = Constant.Strings.ExceptionsMessage.INVALID_CLUSTER_CONFIG_MSG
						+ ": Hadoop component missing.";

				HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
						this.getComponentName());
				return false;
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not initialize ", this.getComponentName(), e);
			return false;
		}
	}

	/**
	 * Manage service on nodes.
	 * 
	 * @param nodes
	 *            the nodes
	 * @param role
	 *            the role
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	public final boolean manageServiceOnNodes(Set<String> nodes,
			final String role, final String action) {
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			for (final String host : nodes) {
				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						if (!manageServiceOnNode(
								clusterConfig.getNodes().get(host), role,
								action)) {
							// HadoopUtils.addAndLogError(LOG, clusterConfig,
							// "Could not " + action + " " + role + " service.",
							// Constant.Component.Name.HADOOP, host);
							manageServiceRequestStatus = false;
						}
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodes.size());
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, "Could not "
					+ action + " " + role + " service.",
					Constant.Component.Name.HADOOP);
			return false;
		}
		return manageServiceRequestStatus;
	}

	/**
	 * Manage service on node.
	 * 
	 * @param host
	 *            the host
	 * @param role
	 *            the role
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	public final boolean manageServiceOnNode(String host, String role,
			String action) {
		return manageServiceOnNode(this.clusterConfig.getNodes().get(host),
				role, action);
	}

	/**
	 * Adds the taskable classes.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected final boolean addTaskableClasses(NodeConfig nodeConfig) {
		try {

			LOG.info("Adding Hadoop Taskable classes to agent",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			List<String> classes = new ArrayList<String>();
			if (nodeConfig.getRoles().get(Constant.Component.Name.HADOOP)
					.contains(HadoopConstants.Roles.JOBTRACKER)) {
				// classes.add(HadoopConstants.TaskableClass.JOB_STATUS_MONITOR);
			}
			boolean isHadoop2 = this.compConfig
					.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.IS_HADOOP2);
			if (isHadoop2) {
				boolean isHaEnabled = this.compConfig
						.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);
				if (isHaEnabled
						&& nodeConfig.getRoles()
								.get(Constant.Component.Name.HADOOP)
								.contains(HadoopConstants.Roles.NAMENODE)) {
					classes.add(HadoopConstants.TaskableClass.HA_UPDATE_NAMENODE_ROLE);
				}
			}
			if (classes.size() > 0) {
				if (!AgentUtils.addTaskables(nodeConfig.getConnection(),
						classes, this.clusterConfig.getAgentHomeDir())) {
					HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
							"Could not update taskable.conf in Agent",
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					return false;
				}
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not configure Hadoop service xml",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	/**
	 * Register services.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	public boolean registerServices(NodeConfig nodeConfig,
			boolean isAddNodeOperation) {
		// Default implementation
		return true;
	}

	/**
	 * Configure agent service xml.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	public boolean configureAgentServiceXml(NodeConfig nodeConfig) {
		// Default implementation
		return false;
	}

	/**
	 * Manage service on node.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @param role
	 *            the role
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	protected boolean manageServiceOnNode(NodeConfig nodeConfig, String role,
			String action) {
		// Default implementation: Needs to be defined in Child Classes
		// Will change the methods defined in the classes later
		return true;
	}

	/**
	 * Manage node.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param host
	 *            the host
	 * @param action
	 *            the action
	 * @return true, if successful
	 */
	public boolean manageNode(ClusterConfig clusterConfig, String host,
			String action) {
		// Default implementation
		return false;
	}

	@Override
	public String getComponentName() {
		return Constant.Component.Name.HADOOP;
	}

	@Override
	public boolean startNode(ClusterConfig clusterConfig, String host) {
		try {
			if (!HadoopUtils.isManagedByAnkush(this.compConfig)) {
				HadoopUtils
						.addAndLogError(
								this.LOG,
								clusterConfig,
								"Could not start Hadoop node: "
										+ Constant.Registration.ErrorMsg.NOT_MANAGED_MODE,
								Constant.Component.Name.HADOOP, host);
				return false;
			}
			HadoopServiceManager manageService = HadoopUtils
					.getServiceManagerInstance(clusterConfig, compConfig);
			manageService.manageNode(clusterConfig, host,
					HadoopConstants.Command.Action.START);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, clusterConfig,
					"Could not start Hadoop node.",
					Constant.Component.Name.HADOOP, host, e);
			return false;
		}
		return true;
	}

	@Override
	public boolean stopNode(ClusterConfig clusterConfig, String host) {
		try {
			if (!HadoopUtils.isManagedByAnkush(this.compConfig)) {
				HadoopUtils
						.addAndLogError(
								this.LOG,
								clusterConfig,
								"Could not stop Hadoop node: "
										+ Constant.Registration.ErrorMsg.NOT_MANAGED_MODE,
								Constant.Component.Name.HADOOP, host);
				return false;
			}
			HadoopServiceManager manageService = HadoopUtils
					.getServiceManagerInstance(clusterConfig, compConfig);
			manageService.manageNode(clusterConfig, host,
					HadoopConstants.Command.Action.STOP);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, clusterConfig,
					"Could not stop Hadoop node.",
					Constant.Component.Name.HADOOP, host, e);
			return false;
		}
		return true;
	}

	@Override
	public void setComponentName(String componentName) {
		// TODO Auto-generated method stub

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
		return (clusterConfig.getComponents()
				.get(Constant.Component.Name.HADOOP).getHomeDir() + Hadoop1Configurator.RELPATH_LOGS_DIR);
	}

	@Override
	public String getLogFilesRegex(ClusterConfig clusterConfig, String host,
			String role, Map<String, Object> parameters) {
		return "*" + role.toLowerCase() + "*.log";
	}
}
