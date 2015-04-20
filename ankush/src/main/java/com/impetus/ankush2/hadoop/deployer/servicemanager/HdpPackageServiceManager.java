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
package com.impetus.ankush2.hadoop.deployer.servicemanager;

import java.util.ArrayList;
import java.util.List;

import com.impetus.ankush2.agent.AgentUtils;
import com.impetus.ankush2.agent.ComponentService;
import com.impetus.ankush2.common.Parameter;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.constant.Constant.Component;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;

/**
 * @author Akhil
 * 
 */
public class HdpPackageServiceManager extends HadoopServiceManager {

	/**
	 * @param clusterConfig
	 * @param hadoopConfig
	 * @param classObj
	 */
	public HdpPackageServiceManager(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, HadoopBundleServiceManager.class);
	}

	public HdpPackageServiceManager() {
		super();
	}

	@Override
	public boolean configureAgentServiceXml(NodeConfig nodeConfig) {
		try {

			LOG.info("Configuring Hadoop service xml file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			// List of service object
			List<ComponentService> componentServices = new ArrayList<ComponentService>();

			for (String role : nodeConfig.getRoles().get(
					Constant.Component.Name.HADOOP)) {
				String process = HadoopUtils.RoleProcessName
						.getProcessName(role);

				List<Parameter> serviceParams = new ArrayList<Parameter>();

				// Get the PID path for Hadoop Daemon
				String pidFilePath = HdpRolePidFilePath
						.getRolePidFilePath(role);
				serviceParams.add(new com.impetus.ankush2.common.Parameter(
						Constant.Agent.ServiceParams.PIDFILE, pidFilePath));

				// Adding component services as PID process with PID
				// file path as parameter.
				componentServices.add(new ComponentService(process, role,
						Constant.Agent.ServiceType.PID, serviceParams));

			}
			return AgentUtils.createServiceXML(nodeConfig.getConnection(),
					componentServices, Component.Name.HADOOP,
					clusterConfig.getAgentHomeDir());
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not configure Hadoop service xml",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	@Override
	protected boolean manageServiceOnNode(NodeConfig nodeConfig, String role,
			String action) {
		try {

			if (HadoopUtils.isManagedByAnkush(this.compConfig)) {
				// LOG.info("Performing " + action + " " + role + " operation",
				// Constant.Component.Name.HADOOP, nodeConfig.getHost());
				// String hadoopScriptDirPath = HadoopUtils
				// .getHadoopScriptDir(this.compConfig);
				// String manageCommand = hadoopScriptDirPath
				// + HadoopConstants.JavaProcessScriptFile
				// .getProcessDaemonFile(role) + " " + action + " "
				// + HadoopUtils.RoleCommandName.getCommandName(role);
				//
				// CustomTask manageServices = new ExecCommand(manageCommand);
				// if (nodeConfig.getConnection().exec(manageServices).rc != 0)
				// {
				// HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
				// "Could not " + action + " " + role,
				// Constant.Component.Name.HADOOP, nodeConfig.getHost());
				// return false;
				// }
			} else {
				HadoopUtils
						.addAndLogError(
								this.LOG,
								this.clusterConfig,
								"Could not "
										+ action
										+ " "
										+ role
										+ ": "
										+ Constant.Registration.ErrorMsg.MONITOR_ONLY_MODE,
								Constant.Component.Name.HADOOP,
								nodeConfig.getHost());
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
}
