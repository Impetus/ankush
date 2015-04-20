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
import java.util.List;

import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush2.agent.AgentConstant;
import com.impetus.ankush2.agent.AgentUtils;
import com.impetus.ankush2.agent.ComponentService;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;

/**
 * @author Akhil
 * 
 */
public class HadoopBundleServiceManager extends HadoopServiceManager {

	private String componentName;

	/**
	 * @param clusterConfig
	 * @param hadoopConfig
	 * @param classObj
	 */
	public HadoopBundleServiceManager(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, HadoopBundleServiceManager.class);
	}

	public HadoopBundleServiceManager() {
		super();
	}

	@Override
	public boolean configureAgentServiceXml(NodeConfig nodeConfig) {
		try {
			LOG.info("Configuring Hadoop service xml file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			// List of service object
			List<ComponentService> services = new ArrayList<ComponentService>();

			for (String role : nodeConfig.getRoles().get(
					Constant.Component.Name.HADOOP)) {
				String process = HadoopUtils.RoleProcessName
						.getProcessName(role);
				services.add(new ComponentService(process, role,
						Constant.ServiceType.JPS));
			}
			if (!AgentUtils.createServiceXML(nodeConfig.getConnection(),
					services, Constant.Component.Name.HADOOP,
					clusterConfig.getAgentHomeDir())) {
				HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
						"Could not configure Hadoop service xml",
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not configure Hadoop service xml",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	@Override
	public boolean registerServices(NodeConfig nodeConfig,
			boolean isAddNodeOperation) {
		return configureAgentServiceXml(nodeConfig);
	}

	protected boolean configureAgentProperties(NodeConfig nodeConfig) {
		try {
			LOG.info("Configuring agent.properties file for Hadoop.",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			HadoopConfigurator hadoopConfigurator = HadoopUtils
					.getConfiguratorInstance(clusterConfig, compConfig);
			String agentProperties = hadoopConfigurator
					.getAgentProperties(nodeConfig);

			if (!agentProperties.isEmpty()) {
				String agentPropertiesFilePath = clusterConfig
						.getAgentHomeDir()
						+ AgentConstant.Relative_Path.AGENT_CONF_FILE;
				CustomTask task = new AppendFileUsingEcho(agentProperties,
						agentPropertiesFilePath);
				if (nodeConfig.getConnection().exec(task).rc != 0) {
					HadoopUtils
							.addAndLogError(
									this.LOG,
									this.clusterConfig,
									"Could not configure agent.properties file for Hadoop",
									Constant.Component.Name.HADOOP,
									nodeConfig.getHost());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not configure agent.properties file for Hadoop",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	// /**
	// * Add Hadoop Service XML in Agent.
	// *
	// * @param conf
	// * the conf
	// * @param node
	// * the node
	// * @param connection
	// * the connection
	// * @param isHadoop2
	// * the is hadoop2
	// * @return true, if successful
	// */
	// private boolean addHadoopServiceXML(NodeConfig nodeConfig) {
	// try {
	//
	// LOG.info("Configuring Hadoop service xml file",
	// Constant.Component.Name.HADOOP, nodeConfig.getHost());
	//
	// // List of service object
	// List<ComponentService> services = new ArrayList<ComponentService>();
	//
	// for (String role : nodeConfig.getRoles().get(
	// Constant.Component.Name.HADOOP)) {
	// String process = HadoopUtils.RoleProcessName
	// .getProcessName(role);
	// services.add(new ComponentService(process, role,
	// Constant.ServiceType.JPS));
	// }
	// return AgentUtils.createServiceXML(nodeConfig.getConnection(),
	// services, Component.Name.HADOOP);
	//
	// // Set<String> nodeRoles = (Set<String>)
	// // (this.hadoopConfig.getNodes()
	// // .get(nodeConfig.getHost()))
	// // .get(Constant.Node.ComponentConfigKeys.ROLES);
	// //
	// // String componentName = Constant.Component.Name.HADOOP;
	// //
	// // // create list of jps services.
	// // List<ComponentService> componentServices = new
	// // ArrayList<ComponentService>();
	// // // Iterating over the roles set.
	// // for (String role : nodeRoles) {
	// // // Getting service name by role.
	// // String process = HadoopUtils.RoleProcessName
	// // .getProcessName(role);
	// //
	// // // if service is not null.
	// // if (process != null) {
	// //
	// // // if (hConf.getDeploymentType().equals(
	// // // DeploymentType.PackageManager)) {
	// // //
	// // // List<com.impetus.ankush.common.agent.Parameter>
	// // // serviceParams = new
	// // // ArrayList<com.impetus.ankush.common.agent.Parameter>();
	// // //
	// // // // Get the PID path for Hadoop Daemon
	// // // String pidFilePath = RolePidFilePathMap
	// // // .getRolePidFilePath(role);
	// // // serviceParams
	// // // .add(new com.impetus.ankush.common.agent.Parameter(
	// // // Constant.Agent.ServiceParams.PIDFILE,
	// // // pidFilePath));
	// // //
	// // // // Adding component services as PID process with PID
	// // // // file path as parameter.
	// // // componentServices.add(new ComponentService(process,
	// // // role, Constant.Agent.ServiceType.PID,
	// // // serviceParams));
	// // // } else {
	// // // Adding component services as JPS process.
	// // componentServices.add(new ComponentService(process, role,
	// // Constant.Agent.ServiceType.JPS));
	// // // }
	// // }
	// // }
	// // if (!AgentUtils.createServiceXML(nodeConfig.getConnection(),
	// // componentServices, componentName)) {
	// // HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
	// // "Could not configure Hadoop service xml",
	// // Constant.Component.Name.HADOOP, nodeConfig.getHost());
	// // return false;
	// // }
	// } catch (Exception e) {
	// HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
	// "Could not configure Hadoop service xml",
	// Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
	// return false;
	// }
	// }

	@Override
	protected boolean manageServiceOnNode(NodeConfig nodeConfig, String role,
			String action) {
		try {
			LOG.info("Performing " + action + " " + role + " operation",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());
			String hadoopScriptDirPath = HadoopUtils
					.getHadoopScriptDir(this.compConfig);
			String manageCommand = hadoopScriptDirPath
					+ HadoopConstants.JavaProcessScriptFile
							.getProcessDaemonFile(role) + " " + action + " "
					+ HadoopUtils.RoleCommandName.getCommandName(role);

			CustomTask manageServices = new ExecCommand(manageCommand);
			if (nodeConfig.getConnection().exec(manageServices).rc != 0) {
				HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
						"Could not " + action + " " + role,
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not " + action + " " + role,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	@Override
	public String getComponentName() {
		return this.componentName;
	}

	@Override
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public boolean startNode(ClusterConfig clusterConfig, String host) {
		this.intitializeDataMembers(clusterConfig);
		LOG.info("Starting Hadoop services on - " + host,
				Constant.Component.Name.HADOOP, host);
		return manageNode(clusterConfig, host,
				HadoopConstants.Command.Action.START);
	}

	@Override
	public boolean stopNode(ClusterConfig clusterConfig, String host) {
		this.intitializeDataMembers(clusterConfig);
		LOG.info("Stopping Hadoop services on - " + host,
				Constant.Component.Name.HADOOP, host);
		return manageNode(clusterConfig, host,
				HadoopConstants.Command.Action.STOP);
	}

	public boolean manageNode(ClusterConfig clusterConfig, String host,
			String action) {
		return manageServices(clusterConfig, host, clusterConfig.getNodes()
				.get(host).getRoles().get(Constant.Component.Name.HADOOP),
				action);
	}

}
