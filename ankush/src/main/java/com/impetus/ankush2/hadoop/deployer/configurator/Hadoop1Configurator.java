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
package com.impetus.ankush2.hadoop.deployer.configurator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddEnvironmentVariables;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.SourceFile;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.deployer.servicemanager.HadoopServiceManager;
import com.impetus.ankush2.hadoop.monitor.commandsmanager.HadoopCommandsManager;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.utils.SSHUtils;

/**
 * @author Akhil
 * 
 */
public class Hadoop1Configurator extends HadoopConfigurator {

	/**
	 * 
	 */
	public Hadoop1Configurator() {
		super();
	}

	/**
	 * @param clusterConfig
	 * @param hadoopConfig
	 * @param classObj
	 */
	public Hadoop1Configurator(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, Hadoop1Configurator.class);
	}

	/** The Constant DEFAULT_PORT_HTTP_JOBTRACKER. */
	public static final String DEFAULT_PORT_HTTP_JOBTRACKER = "50030";

	/** The Constant DEFAULT_PORT_HTTP_TASKTRACKER. */
	public static final String DEFAULT_PORT_HTTP_TASKTRACKER = "50060";

	public static final String DEFAULT_PORT_HTTP_NAMENODE = "50070";

	/** The Constant DEFAULT_PORT_RPC_NAMENODE. */
	public static final String DEFAULT_PORT_RPC_NAMENODE = "9000";

	public static final String DEFAULT_PORT_RPC_JOBTRACKER = "9001";

	public static final String RELPATH_CONF_DIR = "conf/";

	public static final String RELPATH_SCRIPT_DIR = "bin/";

	public static final String RELPATH_BIN_DIR = "bin/";

	public static final String RELPATH_LOGS_DIR = "logs/";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.deployer.HadoopConfigurator#configureNode(
	 * com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	public boolean configureNode(NodeConfig nodeConfig) {
		return configureEnvironmentVariables(nodeConfig, false)
				&& configureSiteXmlFiles(nodeConfig)
				&& configureMastersFile(nodeConfig)
				&& configureSlavesFile(nodeConfig,
						HadoopUtils.getSlaveHosts(this.compConfig))
				&& configureGangliaMetrics(nodeConfig);
	}

	protected boolean configureSiteXmlFiles(NodeConfig nodeConfig) {
		return configureCoreSiteXml(nodeConfig)
				&& configureHdfsSiteXml(nodeConfig)
				&& configureMapredSiteXml(nodeConfig);
	}

	/**
	 * Configure masters file.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected boolean configureMastersFile(NodeConfig nodeConfig) {
		// configuring slaves file
		Result res = null;
		try {
			LOG.info("Updating Hadoop "
					+ HadoopConstants.FileName.ConfigurationFile.MASTERS
					+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());
			String masterHost = HadoopUtils
					.getSecondaryNameNodeHost(this.compConfig);

			String mastersFile = (String) compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_CONF)
					+ HadoopConstants.FileName.ConfigurationFile.MASTERS;// "slaves";
			AnkushTask clearFile = new ClearFile(mastersFile);
			res = nodeConfig.getConnection().exec(clearFile);
			if (!res.isSuccess) {
				HadoopUtils
						.addAndLogError(
								this.LOG,
								this.clusterConfig,
								"Could not clear "
										+ HadoopConstants.FileName.ConfigurationFile.MASTERS
										+ " file contents",
								Constant.Component.Name.HADOOP,
								nodeConfig.getHost());
				return false;
			}

			AnkushTask appendFile = new AppendFileUsingEcho(masterHost,
					mastersFile);
			res = nodeConfig.getConnection().exec(appendFile);
			if (!res.isSuccess) {
				HadoopUtils
						.addAndLogError(
								this.LOG,
								this.clusterConfig,
								"Could not update "
										+ HadoopConstants.FileName.ConfigurationFile.MASTERS
										+ " file",
								Constant.Component.Name.HADOOP,
								nodeConfig.getHost());
				return false;
			}
			ConfigurationManager confManager = new ConfigurationManager();
			// Saving masters file property
			confManager.saveConfiguration(this.clusterConfig.getClusterId(),
					this.clusterConfig.getCreatedBy(),
					HadoopConstants.FileName.ConfigurationFile.MASTERS,
					nodeConfig.getHost(), "Master Host", masterHost);

			return true;
		} catch (Exception e) {
			HadoopUtils
					.addAndLogError(
							this.LOG,
							this.clusterConfig,
							"Could not update "
									+ HadoopConstants.FileName.ConfigurationFile.MASTERS
									+ " file", Constant.Component.Name.HADOOP,
							nodeConfig.getHost(), e);
			return false;
		}
	}

	@Override
	public boolean configureEnvironmentVariables(NodeConfig nodeConfig, boolean isAddNodeOperation) {
		try {
			Result res = null;
			SSHExec connection = nodeConfig.getConnection();
			LOG.info("Setting Hadoop Home in Environment variables",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			LinkedHashMap<String, String> envVariableMap = new LinkedHashMap<String, String>();
			envVariableMap.put(HadoopUtils.KEY_HADOOP_HOME, this
					.getComponentConfig().getHomeDir());
			envVariableMap
					.put(HadoopUtils.KEY_HADOOP_CONF_DIR,
							(String) compConfig
									.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_CONF));

			AnkushTask addEnvVariables = new AddEnvironmentVariables(
					envVariableMap, Constant.LinuxEnvFiles.BASHRC,
					Constant.Component.Name.HADOOP);
			res = connection.exec(addEnvVariables);
			if (!res.isSuccess) {
				String errMsg = "Could not add environment variables for Hadoop in "
						+ Constant.LinuxEnvFiles.BASHRC + " file";

				HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}

			AnkushTask sourceFile = new SourceFile(
					Constant.LinuxEnvFiles.BASHRC);
			res = connection.exec(sourceFile);
			if (!res.isSuccess) {
				String errMsg = "Could not source "
						+ Constant.LinuxEnvFiles.BASHRC + " file";

				HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}

			return true;
		} catch (Exception e) {
			String errMsg = "Could not add environment variables for Hadoop in "
					+ Constant.LinuxEnvFiles.BASHRC + " file";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	@Override
	protected boolean configureHdfsSiteXml(NodeConfig nodeConfig) {

		try {
			// configuring hdfs-site.xml file in $HADOOP_CONF_DIR
			String hdfsSitePath = (String) compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_CONF)
					+ HadoopConstants.FileName.ConfigurationFile.XML_HDFS_SITE;

			LOG.info("Configuring "
					+ HadoopConstants.FileName.ConfigurationFile.XML_HDFS_SITE
					+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());

			String hdfsNameDirValue = (String) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_NAME_DIR);
			String hdfsDataDirValue = (String) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_DATA_DIR);

			Map<String, String> paramList = new HashMap<String, String>();
			paramList
					.put("dfs.replication",
							(String) this.compConfig
									.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_REPLICATION_FACTOR));
			paramList.put("dfs.permissions", "false");
			paramList.put("dfs.name.dir", hdfsNameDirValue);
			paramList.put("dfs.data.dir", hdfsDataDirValue);

			paramList.put("dfs.http.address",
					HadoopUtils.getNameNodeHost(compConfig) + ":"
							+ HadoopConfigurator.DEFAULT_PORT_HTTP_NAMENODE);

			if (HadoopUtils.getSecondaryNameNodeHost(compConfig) != null) {
				String valHttpAddressSecNamenode = HadoopUtils
						.getSecondaryNameNodeHost(compConfig)
						+ ":"
						+ HadoopConfigurator.DEFAULT_PORT_HTTP_SECNAMENODE;
				paramList.put("dfs.secondary.http.address",
						valHttpAddressSecNamenode);
			}
			boolean status;
			for (String propertyName : paramList.keySet()) {
				status = HadoopUtils.addPropertyToXmlFile(this.LOG,
						this.clusterConfig, nodeConfig, hdfsSitePath,
						propertyName, paramList.get(propertyName));
				if (!status) {
					return false;
				}
			}
		} catch (Exception e) {
			String errMsg = "Could not configure "
					+ HadoopConstants.FileName.ConfigurationFile.XML_HDFS_SITE
					+ " file";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}

		return true;
	}

	@Override
	protected boolean configureCoreSiteXml(NodeConfig nodeConfig) {
		try {
			// configuring core-site.xml file in $HADOOP_CONF_DIR
			String coreSitePath = (String) compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_CONF)
					+ HadoopConstants.FileName.ConfigurationFile.XML_CORE_SITE;

			LOG.info("Configuring "
					+ HadoopConstants.FileName.ConfigurationFile.XML_CORE_SITE
					+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());

			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put(
					"fs.default.name",
					HadoopConfigurator.HADOOP_URI_PREFIX
							+ HadoopUtils.getNameNodeHost(compConfig) + ":"
							+ Hadoop1Configurator.DEFAULT_PORT_RPC_NAMENODE);

			paramList
					.put("hadoop.tmp.dir",
							(String) this.compConfig
									.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.TMP_DIR_HADOOP));

			boolean status;
			for (String propertyName : paramList.keySet()) {
				status = HadoopUtils.addPropertyToXmlFile(this.LOG,
						this.clusterConfig, nodeConfig, coreSitePath,
						propertyName, paramList.get(propertyName));
				if (!status) {
					return false;
				}
			}
		} catch (Exception e) {
			String errMsg = "Could not configure "
					+ HadoopConstants.FileName.ConfigurationFile.XML_CORE_SITE
					+ " file";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	@Override
	protected boolean configureMapredSiteXml(NodeConfig nodeConfig) {
		try {
			// configuring core-site.xml file in $HADOOP_CONF_DIR
			String mapredSitePath = (String) compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_CONF)
					+ HadoopConstants.FileName.ConfigurationFile.XML_MAPRED_SITE;

			LOG.info(
					"Configuring "
							+ HadoopConstants.FileName.ConfigurationFile.XML_MAPRED_SITE
							+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());

			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put("mapred.job.tracker",
					HadoopUtils.getJobTrackerHost(compConfig) + ":"
							+ Hadoop1Configurator.DEFAULT_PORT_RPC_JOBTRACKER);

			paramList.put("mapred.jobtracker.restart.recover", "true");

			boolean status;
			for (String propertyName : paramList.keySet()) {
				status = HadoopUtils.addPropertyToXmlFile(this.LOG,
						this.clusterConfig, nodeConfig, mapredSitePath,
						propertyName, paramList.get(propertyName));
				if (!status) {
					return false;
				}
			}
		} catch (Exception e) {
			String errMsg = "Could not configure "
					+ HadoopConstants.FileName.ConfigurationFile.XML_CORE_SITE
					+ " file";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	@Override
	protected boolean configureGangliaMetrics(NodeConfig nodeConfig) {

		return true;
	}

	@Override
	public void setHadoopConfDir() {
		this.getComponentConfig().addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.DIR_CONF,
				this.getComponentConfig().getHomeDir()
						+ Hadoop1Configurator.RELPATH_CONF_DIR);
	}

	@Override
	public void setHadoopScriptDir() {
		this.getComponentConfig().addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.DIR_SCRIPT,
				this.getComponentConfig().getHomeDir()
						+ Hadoop1Configurator.RELPATH_SCRIPT_DIR);
	}

	@Override
	public void setHadoopBinDir() {
		this.getComponentConfig().addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.DIR_BIN,
				this.getComponentConfig().getHomeDir()
						+ Hadoop1Configurator.RELPATH_BIN_DIR);
	}

	@Override
	public void setRpcPorts() {
		this.getComponentConfig().addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.RpcPort.NAMENODE,
				Hadoop1Configurator.DEFAULT_PORT_RPC_NAMENODE);
		this.getComponentConfig().addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.RpcPort.JOBTRACKER,
				Hadoop1Configurator.DEFAULT_PORT_RPC_JOBTRACKER);
	}

	@Override
	public boolean initializeAndStartCluster() {

		try {
			HadoopCommandsManager hadoopCmdManager = HadoopUtils
					.getCommandsManagerInstance(clusterConfig, compConfig);
			// Format NameNode andHadoopConstants
			if(!hadoopCmdManager.formatNameNode()) {
				return false;
			} 
			this.manageComponent(HadoopConstants.Command.Action.START);
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not format Hadoop NameNode",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	@Override
	public boolean manageComponent(String action) {
		boolean status = true;
		try {
			LOG.info("Managing Hadoop services: " + action + " component",
					Constant.Component.Name.HADOOP);

			HadoopServiceManager hadoopServiceManager = HadoopUtils
					.getServiceManagerInstance(clusterConfig, compConfig);
			hadoopServiceManager.getLOG().setCluster(clusterConfig);

			// Manage Master Processes - NameNode and JobTracker
			if (!hadoopServiceManager.manageServiceOnNode(
					HadoopUtils.getNameNodeHost(this.compConfig),
					HadoopConstants.Roles.NAMENODE, action)) {
				status = false;
			}
			
			if(!hadoopServiceManager
					.manageServiceOnNode(
							HadoopUtils.getJobTrackerHost(this.compConfig),
							HadoopConstants.Roles.JOBTRACKER, action)) {
				status = false;
			}

			// Start Slaves Processes on Each Node
			if (!hadoopServiceManager.manageServiceOnNodes(
					HadoopUtils.getSlaveHosts(this.compConfig),
					HadoopConstants.Roles.DATANODE, action)) {
				status = false;
			}
			
			if(!hadoopServiceManager
					.manageServiceOnNodes(
							HadoopUtils.getSlaveHosts(this.compConfig),
							HadoopConstants.Roles.TASKTRACKER, action)) {
				status = false;
			}

			if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
				if (!hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getSecondaryNameNodeHost(this.compConfig),
						HadoopConstants.Roles.SECONDARYNAMENODE, action)) {
					status = false;
				}
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, "Could not "
					+ action + " Hadoop cluster",
					Constant.Component.Name.HADOOP, e);
			status = false;
		}
		return status;
	}

	@Override
	public boolean setupPasswordlessSSH(Set<String> nodes) {
		try {
			LOG.info("Configuring Passwordless SSH from Hadoop NameNode.",
					Constant.Component.Name.HADOOP);

			String namenodeHost = HadoopUtils.getNameNodeHost(this.compConfig);

			if (!this.generateRsaKeysForHadoopNodes(nodes)) {
				return false;
			}

			if (this.clusterConfig.getNodes().containsKey(namenodeHost)) {
				if (!HadoopUtils.setupPasswordlessSSH(LOG, this.clusterConfig,
						namenodeHost, nodes)) {
					return false;
				}
			} else {
				SSHExec connection = SSHUtils.connectToNode(namenodeHost,
						this.clusterConfig.getAuthConf());
				if (!HadoopUtils.setupPasswordlessSSH(LOG, this.clusterConfig,
						namenodeHost, nodes, connection)) {
					return false;
				}
				if (connection != null) {
					connection.disconnect();
				}
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not configure passwordless SSH for Hadoop",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	@Override
	public boolean updateAdvConfSetNodes() {
		try {
			Set<String> slaves = new HashSet<String>();

			for (String host : this.clusterConfig.getNodes().keySet()) {

				Set<String> roles = clusterConfig.getNodes().get(host)
						.getRoles().get(Constant.Component.Name.HADOOP);

				if (roles.isEmpty()) {
					String errorMsg = Constant.Strings.ExceptionsMessage.INVALID_COMPONENT_CONFIG_MSG
							+ ": Roles empty for node - " + host;
					HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
							Constant.Component.Name.HADOOP);
					return false;
				}

				if (roles.contains(HadoopConstants.Roles.NAMENODE)) {

					this.compConfig.addAdvanceConfProperty(
							HadoopConstants.AdvanceConfKeys.NAMENODE, host);
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
			}
			this.compConfig.addAdvanceConfProperty(
					HadoopConstants.AdvanceConfKeys.SLAVES, slaves);
		} catch (Exception e) {
			String errorMsg = Constant.Strings.ExceptionsMessage.INVALID_COMPONENT_CONFIG_MSG
					+ ": Invalid nodes object for Hadoop";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
					Constant.Component.Name.HADOOP, e);
			return false;
		}
		return true;
	}

	@Override
	public String getAgentProperties(NodeConfig nodeConfig)
			throws AnkushException {
		// try {
		// if(nodeConfig.getRoles().get(Constant.Component.Name.HADOOP).contains(HadoopConstants.Roles.JOBTRACKER)
		// ) {
		// int hadoopDataUpdateTime = 30;
		// StringBuffer agentConfProp = new StringBuffer();
		// agentConfProp.append("HADOOP_DATA_UPDATE_TIME=")
		// .append(hadoopDataUpdateTime)
		// .append(Constant.Strings.LINE_SEPERATOR);
		// agentConfProp.append("URL_JOB_STATUS=job").append(
		// Constant.Strings.LINE_SEPERATOR);
		// agentConfProp.append("JOBTRACKER_HOST=")
		// .append(HadoopUtils.getJobTrackerHost(compConfig))
		// .append(Constant.Strings.LINE_SEPERATOR);
		// agentConfProp.append("JOBTRACKER_RPC_PORT=")
		// .append(HadoopUtils.getJobTrackerRpcPort(compConfig))
		// .append(Constant.Strings.LINE_SEPERATOR);
		// return agentConfProp.toString();
		// }
		// return "";
		// } catch (Exception e) {
		// String errorMsg = "Could not get Agent properties for Hadoop";
		// HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
		// Constant.Component.Name.HADOOP, e);
		// throw new AnkushException(errorMsg);
		// }
		// }
		return "";
	}

	@Override
	protected void addNodeRolesToMap(NodeRolesMap nodeRolesMap) {
		nodeRolesMap
				.put(this.compConfig
						.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.NAMENODE),
						HadoopConstants.Roles.NAMENODE);
		nodeRolesMap
				.put(this.compConfig
						.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.JOBTRACKER),
						HadoopConstants.Roles.JOBTRACKER);
		Set<String> slavesList = HadoopUtils.getSlaveHosts(compConfig);
		for (String slave : slavesList) {
			nodeRolesMap.put(slave, HadoopConstants.Roles.DATANODE);
			nodeRolesMap.put(slave, HadoopConstants.Roles.TASKTRACKER);
		}
	}

	@Override
	public Set<String> getHAServicesList() {
		Set<String> serviceList = new HashSet<String>();
		serviceList.add(HadoopConstants.Roles.NAMENODE);
		serviceList.add(HadoopConstants.Roles.DATANODE);
		serviceList.add(HadoopConstants.Roles.JOBTRACKER);
		serviceList.add(HadoopConstants.Roles.TASKTRACKER);
		if (HadoopUtils.getSecondaryNameNodeHost(compConfig) != null) {
			serviceList.add(HadoopConstants.Roles.SECONDARYNAMENODE);
		}
		return serviceList;
	}

//	@Override
//	public boolean canNodesBeDeleted(Collection<String> nodes) throws Exception {
//		for(String node : nodes) {
//			NodeConfig nodeConfig = this.clusterConfig.getNodes().get(node);
//			if(nodeConfig.getRoles().get(key)) {
//				
//			}
//		}
//		return false;
//	}
}
