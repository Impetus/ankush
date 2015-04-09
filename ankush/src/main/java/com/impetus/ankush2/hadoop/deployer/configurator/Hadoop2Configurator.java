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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddEnvironmentVariables;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.Move;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.scripting.impl.SourceFile;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.constant.Constant.Component;
import com.impetus.ankush2.constant.Constant.Strings;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.deployer.servicemanager.HadoopServiceManager;
import com.impetus.ankush2.hadoop.monitor.commandsmanager.Hadoop2CommandsManager;
import com.impetus.ankush2.hadoop.monitor.commandsmanager.HadoopCommandsManager;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.utils.SSHUtils;
import com.impetus.ankush2.zookeeper.ZookeeperUtils;

/**
 * @author Akhil
 * 
 */
public class Hadoop2Configurator extends HadoopConfigurator {

	public static final String DEFAULT_PORT_HTTP_RESOURCEMANAGER = "8088";

	public static final String DEFAULT_MAPREDUCE_FRAMEWORK_YARN = "yarn";

	public static final String DEFAULT_PORT_HTTP_NAMENODE = "50070";

	public static final String DEFAULT_PORT_HTTP_SECONDARYNAMENODE = "50090";

	public static final String DEFAULT_PORT_RPC_NAMENODE = "8020";

	public static final String DEFAULT_PORT_RPC_JOURNAL_NODE = "8485";

	public static final String DEFAULT_PORT_RPC_RESOURCEMANAGER = "8032";

	public static final String DEFAULT_PORT_RPC_RM_SCHEDULER = "8030";

	public static final String DEFAULT_PORT_RPC_RM_RESOURCETRACKER = "8031";

	public static final String DEFAULT_PORT_RPC_RM_WEBAPPPROXYSERVER = "8035";

	private static final String DEFAULT_PORT_HTTP_JOBHISTORYSERVER = "19888";

	public static final String DEFAULT_PORT_REST_JOBHISTORYSERVER = "10020";

	public static final String DEFAULT_PORT_RPC_RM_ADMIN = "8033";

	public static final String RELPATH_CONF_DIR = "etc/hadoop/";

	public static final String RELPATH_SCRIPT_DIR = "sbin/";

	public static final String RELPATH_BIN_DIR = "bin/";

	public static final String RELPATH_LOGS_DIR = "logs/";

	public Hadoop2Configurator() {
		super();
	}

	public Hadoop2Configurator(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, Hadoop2Configurator.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * configureNode(com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	public boolean configureNode(NodeConfig nodeConfig) {
		return configureEnvironmentVariables(nodeConfig, false)
				&& configureSiteXmlFiles(nodeConfig)
				&& configureSlavesFile(nodeConfig,
						HadoopUtils.getSlaveHosts(this.compConfig))
				&& configureGangliaMetrics(nodeConfig);
	}

	protected boolean configureSiteXmlFiles(NodeConfig nodeConfig) {
		return configureCoreSiteXml(nodeConfig)
				&& configureHdfsSiteXml(nodeConfig)
				&& configureMapredSiteXml(nodeConfig)
				&& configureYarnSiteXml(nodeConfig);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * initializeAndStartCluster()
	 */
	@Override
	public boolean initializeAndStartCluster() {
		if (!initializeAndStartHdfs()) {
			return false;
		}
		this.manageYarnServices(HadoopConstants.Command.Action.START);
		return true;
	}

	private boolean initializeAndStartHdfs() {
		try {
			HadoopCommandsManager hadoopCmdManager = HadoopUtils
					.getCommandsManagerInstance(this.clusterConfig,
							this.compConfig);
			HadoopServiceManager hadoopServiceManager = HadoopUtils
					.getServiceManagerInstance(this.clusterConfig,
							this.compConfig);

			boolean isHaEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

			if (isHaEnabled) {
				if (!hadoopServiceManager.manageServiceOnNodes(
						HadoopUtils.getJournalNodeHosts(this.compConfig),
						HadoopConstants.Roles.JOURNALNODE,
						HadoopConstants.Command.Action.START)) {
					return false;
				}
			}

			// Format NameNode and return false if format command fails
			if (!hadoopCmdManager.formatNameNode()) {
				return false;
			}

			hadoopServiceManager.manageServiceOnNode(
					HadoopUtils.getNameNodeHost(this.compConfig),
					HadoopConstants.Roles.NAMENODE,
					HadoopConstants.Command.Action.START);

			boolean isAutomaticFailoverEnabled = false;
			if (isHaEnabled) {
				Hadoop2CommandsManager hadoop2CmdManager = (Hadoop2CommandsManager) hadoopCmdManager;
				isAutomaticFailoverEnabled = (Boolean) this.compConfig
						.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_AUTOMATIC_FAILOVER_ENABLED);

				hadoop2CmdManager.initializeStandByNameNode();

				hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getStandByNameNodeHost(this.compConfig),
						HadoopConstants.Roles.NAMENODE,
						HadoopConstants.Command.Action.START);

				if (isAutomaticFailoverEnabled) {

					hadoop2CmdManager.initializeHAInZooKeeper();
					hadoopServiceManager.manageServiceOnNode(
							HadoopUtils.getActiveNameNodeHost(this.compConfig),
							HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
							HadoopConstants.Command.Action.START);

					hadoopServiceManager
							.manageServiceOnNode(
									HadoopUtils
											.getStandByNameNodeHost(this.compConfig),
									HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
									HadoopConstants.Command.Action.START);
				}
			} else if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
				hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getSecondaryNameNodeHost(this.compConfig),
						HadoopConstants.Roles.SECONDARYNAMENODE,
						HadoopConstants.Command.Action.START);
			}

			// Start DataNode Process on each Slave Node
			hadoopServiceManager.manageServiceOnNodes(
					HadoopUtils.getSlaveHosts(this.compConfig),
					HadoopConstants.Roles.DATANODE,
					HadoopConstants.Command.Action.START);

			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not start Hadoop HDFS services",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	private boolean manageYarnServices(String action) {
		boolean status = true;
		try {
			HadoopServiceManager hadoopServiceManager = HadoopUtils
					.getServiceManagerInstance(this.clusterConfig,
							this.compConfig);

			// Manage ResourceManager Process
			if (!hadoopServiceManager.manageServiceOnNode(
					HadoopUtils.getResourceManagerHost(this.compConfig),
					HadoopConstants.Roles.RESOURCEMANAGER, action)) {
				status = false;
			}

			// Manage NodeManager Process on each Slave Node
			if (!hadoopServiceManager.manageServiceOnNodes(
					HadoopUtils.getSlaveHosts(this.compConfig),
					HadoopConstants.Roles.NODEMANAGER, action)) {
				status = false;
			}

			boolean webAppProxyServerEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.WEBAPPPROXYSERVER_ENABLED);

			if (webAppProxyServerEnabled) {
				if (!hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getWebAppProxyServerHost(this.compConfig),
						HadoopConstants.Roles.WEBAPPPROXYSERVER, action)) {
					status = false;
				}
			}

			boolean jobHistoryServerEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.JOBHISTORYSERVER_ENABLED);
			if (jobHistoryServerEnabled) {
				if (!hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getJobHistoryServerHost(this.compConfig),
						HadoopConstants.Roles.JOBHISTORYSERVER, action)) {
					status = false;
				}
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, "Could not "
					+ action + " Hadoop YARN services",
					Constant.Component.Name.HADOOP, e);
			status = false;
		}
		return status;
	}

	private boolean manageHdfsServices(String action) {
		boolean status = true;
		try {
			HadoopServiceManager hadoopServiceManager = HadoopUtils
					.getServiceManagerInstance(this.clusterConfig,
							this.compConfig);

			boolean isHaEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

			if (!hadoopServiceManager.manageServiceOnNodes(
					HadoopUtils.getJournalNodeHosts(this.compConfig),
					HadoopConstants.Roles.JOURNALNODE, action)) {
				status = false;
			}

			if (isHaEnabled) {
				boolean isAutomaticFailoverEnabled = (Boolean) this.compConfig
						.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_AUTOMATIC_FAILOVER_ENABLED);

				if (!hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getActiveNameNodeHost(this.compConfig),
						HadoopConstants.Roles.NAMENODE, action)) {
					status = false;
				}

				if (!hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getStandByNameNodeHost(this.compConfig),
						HadoopConstants.Roles.NAMENODE, action)) {
					status = false;
				}

				if (isAutomaticFailoverEnabled) {
					if (!hadoopServiceManager.manageServiceOnNodes(
							HadoopUtils.getHaNameNodeHosts(this.compConfig),
							HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
							action)) {

						status = false;
					}
				}
			} else {
				if (hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getNameNodeHost(this.compConfig),
						HadoopConstants.Roles.NAMENODE, action)) {
					status = false;
				}
			}

			if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
				if (!hadoopServiceManager.manageServiceOnNode(
						HadoopUtils.getSecondaryNameNodeHost(this.compConfig),
						HadoopConstants.Roles.SECONDARYNAMENODE, action)) {
					status = false;
				}
			}

			// Start DataNode Process on each Slave Node
			if (!hadoopServiceManager.manageServiceOnNodes(
					HadoopUtils.getSlaveHosts(this.compConfig),
					HadoopConstants.Roles.DATANODE, action)) {
				status = false;
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not format NameNode and start Hadoop services",
					Constant.Component.Name.HADOOP, e);
			status = false;
		}
		return status;
	}

	@Override
	public boolean manageComponent(String action) {
		boolean status = manageHdfsServices(action);
		return manageYarnServices(action) && status;
	}

	private boolean configureHadoopEnvScripts(NodeConfig nodeConfig) {
		Result res = null;
		try {
			// Set Environment Variables in Hadoop Environment Script Files
			String envVariables = this.getBashrcContents();
			List<String> envFileList = new ArrayList<String>();
			envFileList.add(HadoopUtils.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.ENV_HADOOP);
			envFileList.add(HadoopUtils.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.ENV_YARN);
			envFileList.add(HadoopUtils.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.ENV_MAPRED);

			for (String fileName : envFileList) {
				LOG.info("Configuring variables in " + fileName + " file",
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				AnkushTask updateEnvFile = new AppendFileUsingEcho(
						envVariables, fileName);
				res = nodeConfig.getConnection().exec(updateEnvFile);
				if (res.rc != 0) {
					String errMsg = "Could not add environment variables for Hadoop in "
							+ fileName + " file";
					HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			String errMsg = "Could not update Hadoop environment scripts";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	private boolean configureLinuxEnvironmentFile(NodeConfig nodeConfig) {
		Result res = null;
		LOG.info("Configuring variables in " + Constant.LinuxEnvFiles.BASHRC
				+ " file", Constant.Component.Name.HADOOP, nodeConfig.getHost());
		try {
			String componentHome = this.compConfig.getHomeDir();

			// set environment variables in /etc/environment file
			LinkedHashMap<String, String> envVariableMap = new LinkedHashMap<String, String>();
			envVariableMap.put(HadoopUtils.KEY_HADOOP_HOME, componentHome);
			envVariableMap.put(HadoopUtils.KEY_HADOOP_PREFIX, componentHome);
			envVariableMap.put(HadoopUtils.KEY_HADOOP_CONF_DIR,
					HadoopUtils.getHadoopConfDir(this.compConfig));

			// String pathVariableVal = HadoopUtils
			// .getPathVariableValue(nodeConfig)
			// + ":"
			// + HadoopUtils.getHadoopBinDir(this.compConfig)
			// + ":"
			// + HadoopUtils.getHadoopScriptDir(this.compConfig);
			//
			// envVariableMap.put(HadoopUtils.KEY_PATH_VARIABLE,
			// pathVariableVal);

			// envVariableMap.put(HadoopUtils.KEY_HADOOP_OPTS,
			// "-Djava.net.preferIPv4Stack=true");

			AnkushTask addEnvVariables = new AddEnvironmentVariables(
					envVariableMap, Constant.LinuxEnvFiles.BASHRC,
					Component.Name.HADOOP);
			res = nodeConfig.getConnection().exec(addEnvVariables);
			if (!res.isSuccess) {
				String errMsg = "Could not add environment variables for Hadoop in "
						+ Constant.LinuxEnvFiles.BASHRC + " file";
				HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}

			AnkushTask sourceFile = new SourceFile(
					Constant.LinuxEnvFiles.BASHRC);
			res = nodeConfig.getConnection().exec(sourceFile);

			if (res.rc != 0) {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * configureEnvironmentVariables
	 * (com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	public boolean configureEnvironmentVariables(NodeConfig nodeConfig,
			boolean isAddNodeOperation) {
		boolean status = this.configureLinuxEnvironmentFile(nodeConfig);
		if (!isAddNodeOperation) {
			status = status && this.setJavaInHadoop(nodeConfig)
					&& this.configureHadoopEnvScripts(nodeConfig);
		}
		return status;
	}

	private boolean setJavaInHadoop(NodeConfig nodeConfig) {
		try {
			LOG.info("Configuring Java in Hadoop environment scripts",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			LinkedHashMap<String, String> scriptTargetTextMap = new LinkedHashMap<String, String>();
			scriptTargetTextMap
					.put(HadoopUtils.getHadoopConfDir(this.compConfig)
							+ HadoopConstants.FileName.ConfigurationFile.ENV_HADOOP,
							AppStoreWrapper
									.getAnkushConfReader()
									.getStringValue(
											"hadoop2.javahome.hadoopenv.targettext"));
			scriptTargetTextMap
					.put(HadoopUtils.getHadoopConfDir(this.compConfig)
							+ HadoopConstants.FileName.ConfigurationFile.ENV_YARN,
							AppStoreWrapper
									.getAnkushConfReader()
									.getStringValue(
											"hadoop2.javahome.yarnenv.targettext"));

			scriptTargetTextMap
					.put(HadoopUtils.getHadoopConfDir(this.compConfig)
							+ HadoopConstants.FileName.ConfigurationFile.ENV_MAPRED,
							AppStoreWrapper
									.getAnkushConfReader()
									.getStringValue(
											"hadoop2.javahome.yarnenv.targettext"));

			for (String hadoopEnvScript : scriptTargetTextMap.keySet()) {
				if (!this.setJavaInHadoopEnvScript(nodeConfig, hadoopEnvScript,
						scriptTargetTextMap.get(hadoopEnvScript))) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			String errMsg = "Could not update Java in Hadoop environment scripts";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	private boolean setJavaInHadoopEnvScript(NodeConfig nodeConfig,
			String filePath, String targetText) {
		Result res = null;
		try {
			String javaHome = this.clusterConfig.getJavaConf().getHomeDir();

			String replacementText = AppStoreWrapper.getAnkushConfReader()
					.getStringValue(
							"hadoop2.javahome.hadoopenv.replacementtext");

			replacementText = replacementText.replaceAll(
					AppStoreWrapper.getAnkushConfReader().getStringValue(
							"hadoop2.template.javahome.value"), javaHome);

			AnkushTask updateJavaHome = new ReplaceText(targetText,
					replacementText, filePath, false, this.clusterConfig
							.getAuthConf().getPassword());

			res = nodeConfig.getConnection().exec(updateJavaHome);
			if (!res.isSuccess) {
				if (!res.isSuccess) {
					String errMsg = "Could not update Java in Hadoop environment script "
							+ filePath;
					HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			String errMsg = "Could not add environment variables for Hadoop in "
					+ Constant.LinuxEnvFiles.ETC_ENVIRONMENT + " file";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}

	}

	private String getBashrcContents() {
		String hadoopHome = this.compConfig.getHomeDir();
		StringBuilder sb = new StringBuilder();
		sb.append("export " + HadoopUtils.KEY_HADOOP_HOME + "=")
				.append(hadoopHome).append(Strings.LINE_SEPERATOR);
		sb.append("export HADOOP_PREFIX=").append(hadoopHome)
				.append(Strings.LINE_SEPERATOR);
		sb.append("export HADOOP_MAPRED_HOME=").append(hadoopHome)
				.append(Strings.LINE_SEPERATOR);
		sb.append("export HADOOP_COMMON_HOME=").append(hadoopHome)
				.append(Strings.LINE_SEPERATOR);
		sb.append("export HADOOP_HDFS_HOME=").append(hadoopHome)
				.append(Strings.LINE_SEPERATOR);
		sb.append("export YARN_HOME=").append(hadoopHome)
				.append(Strings.LINE_SEPERATOR);
		sb.append("export HADOOP_CONF_DIR=")
				.append(hadoopHome)
				.append(Hadoop2Configurator.RELPATH_CONF_DIR
						+ Strings.LINE_SEPERATOR);
		sb.append("export YARN_CONF_DIR=")
				.append(hadoopHome)
				.append(Hadoop2Configurator.RELPATH_CONF_DIR
						+ Strings.LINE_SEPERATOR);
		return sb.toString();
	}

	private boolean isCdh4_4_0() {
		if (this.compConfig.getVendor()
				.equals(HadoopConstants.Vendors.CLOUDERA)) {

			if (this.compConfig.getVersion().equals(
					HadoopUtils.VERSION_CDH4_4_0)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Configure yarn site xml.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected boolean configureYarnSiteXml(NodeConfig nodeConfig) {
		boolean status = true;
		try {
			String yarnSiteXmlPath = HadoopUtils
					.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.XML_YARN_SITE;

			LOG.info("Configuring "
					+ HadoopConstants.FileName.ConfigurationFile.XML_YARN_SITE
					+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());

			String valAuxServices = "mapreduce_shuffle";
			String keyMRShuffleClass = "yarn.nodemanager.aux-services.mapreduce_shuffle.class";

			if (this.isCdh4_4_0()) {
				valAuxServices = AppStoreWrapper.getAnkushConfReader()
						.getStringValue(
								"hadoop2.propval.auxservices."
										+ this.compConfig.getVersion());

				keyMRShuffleClass = AppStoreWrapper.getAnkushConfReader()
						.getStringValue(
								"hadoop2.propname.mrshuffleclass."
										+ this.compConfig.getVersion());
			}

			Map<String, String> paramList = new HashMap<String, String>();
			String resourceManagerNode = HadoopUtils
					.getResourceManagerHost(this.compConfig);

			paramList.put("yarn.nodemanager.aux-services", valAuxServices);
			paramList.put(keyMRShuffleClass,
					"org.apache.hadoop.mapred.ShuffleHandler");

			paramList.put("yarn.resourcemanager.hostname", resourceManagerNode);

			paramList.put("yarn.resourcemanager.address", resourceManagerNode
					+ ":"
					+ Hadoop2Configurator.DEFAULT_PORT_RPC_RESOURCEMANAGER);
			paramList
					.put("yarn.resourcemanager.resource-tracker.address",
							resourceManagerNode
									+ ":"
									+ Hadoop2Configurator.DEFAULT_PORT_RPC_RM_RESOURCETRACKER);
			paramList
					.put("yarn.resourcemanager.scheduler.address",
							resourceManagerNode
									+ ":"
									+ Hadoop2Configurator.DEFAULT_PORT_RPC_RM_SCHEDULER);
			paramList
					.put("yarn.resourcemanager.scheduler.class",
							"org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler");

			boolean webAppProxyServerEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.WEBAPPPROXYSERVER_ENABLED);

			if (webAppProxyServerEnabled) {
				paramList
						.put("yarn.web-proxy.address",
								HadoopUtils
										.getWebAppProxyServerHost(this.compConfig)
										+ ":"
										+ Hadoop2Configurator.DEFAULT_PORT_RPC_RM_WEBAPPPROXYSERVER);
			}

			for (String propertyName : paramList.keySet()) {
				status = HadoopUtils.addPropertyToFile(clusterConfig,
						nodeConfig, yarnSiteXmlPath, propertyName,
						paramList.get(propertyName));

				if (!status) {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not add " + propertyName + " property to "
									+ yarnSiteXmlPath + " file",
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					return false;
				}
			}
			return true;

		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not update Hadoop yarn-site.xml  file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * configureHdfsSiteXml(com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	protected boolean configureHdfsSiteXml(NodeConfig nodeConfig) {
		boolean status = true;
		try {
			String hdfsSiteXmlPath = HadoopUtils
					.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.XML_HDFS_SITE;

			LOG.info("Configuring "
					+ HadoopConstants.FileName.ConfigurationFile.XML_HDFS_SITE
					+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());

			Map<String, String> paramList = new HashMap<String, String>();
			paramList
					.put("dfs.replication",
							(String) this.compConfig
									.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_REPLICATION_FACTOR));
			paramList.put("dfs.permissions.enabled", "true");
			paramList
					.put("dfs.namenode.name.dir",
							HadoopConstants.URI_FILE_PREFIX
									+ (String) this.compConfig
											.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_NAME_DIR));
			paramList
					.put("dfs.datanode.data.dir",
							HadoopConstants.URI_FILE_PREFIX
									+ (String) this.compConfig
											.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_DATA_DIR));

			boolean isHaEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

			if (isHaEnabled) {
				String nameserviceId = (String) this.compConfig
						.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_NAMESERVICEID);
				String namenodeId1 = (String) this.compConfig
						.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_NAMENODEID1);
				String namenodeId2 = (String) this.compConfig
						.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_NAMENODEID2);

				String activeNameNodeHost = HadoopUtils
						.getActiveNameNodeHost(this.compConfig);
				String standByNameNodeHost = HadoopUtils
						.getStandByNameNodeHost(this.compConfig);

				paramList.put("dfs.nameservices", nameserviceId);
				paramList.put("dfs.ha.namenodes." + nameserviceId, namenodeId1
						+ "," + namenodeId2);
				paramList.put("dfs.namenode.rpc-address." + nameserviceId + "."
						+ namenodeId1, activeNameNodeHost + ":"
						+ Hadoop2Configurator.DEFAULT_PORT_RPC_NAMENODE);

				paramList.put("dfs.namenode.rpc-address." + nameserviceId + "."
						+ namenodeId2, standByNameNodeHost + ":"
						+ Hadoop2Configurator.DEFAULT_PORT_RPC_NAMENODE);

				paramList.put("dfs.namenode.http-address." + nameserviceId
						+ "." + namenodeId1, activeNameNodeHost + ":"
						+ Hadoop2Configurator.DEFAULT_PORT_HTTP_NAMENODE);

				paramList.put("dfs.namenode.http-address." + nameserviceId
						+ "." + namenodeId2, standByNameNodeHost + ":"
						+ Hadoop2Configurator.DEFAULT_PORT_HTTP_NAMENODE);

				StringBuilder journalNodeList = new StringBuilder("qjournal://");
				for (String journalNodeHost : HadoopUtils
						.getJournalNodeHosts(this.compConfig)) {
					journalNodeList
							.append(journalNodeHost
									+ ":"
									+ Hadoop2Configurator.DEFAULT_PORT_RPC_JOURNAL_NODE);
					journalNodeList.append(";");
				}
				String valJournalNodeProp = journalNodeList.toString()
						.substring(0, journalNodeList.length() - 1)
						+ "/"
						+ nameserviceId;
				paramList.put("dfs.namenode.shared.edits.dir",
						valJournalNodeProp);
				paramList
						.put("dfs.journalnode.edits.dir",
								(String) this.compConfig
										.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_JOURNALNODE_EDITS_DIR));
				paramList
						.put("dfs.client.failover.proxy.provider."
								+ nameserviceId,
								"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
				paramList.put("dfs.ha.fencing.methods", "shell(/bin/true)");

				boolean isAutomaticFailoverEnabled = (Boolean) this.compConfig
						.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_AUTOMATIC_FAILOVER_ENABLED);

				if (isAutomaticFailoverEnabled) {
					paramList.put("dfs.ha.automatic-failover.enabled."
							+ nameserviceId, "true");

					String zkEnsembleId = (String) this.compConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ZK_ENSEMBLEID);

					String zkQuorumValue = ZookeeperUtils
							.getZookeeperConnectionString(this.clusterConfig,
									zkEnsembleId);
					paramList.put("ha.zookeeper.quorum", zkQuorumValue);
				} else {
					paramList.put("dfs.ha.automatic-failover.enabled."
							+ nameserviceId, "false");
				}
			} else if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
				String valAddrSNNIp = HadoopUtils
						.getSecondaryNameNodeHost(this.compConfig);
				paramList
						.put("dfs.namenode.secondary.http-address",
								valAddrSNNIp
										+ ":"
										+ Hadoop2Configurator.DEFAULT_PORT_HTTP_SECONDARYNAMENODE);
			}

			for (String propertyName : paramList.keySet()) {
				status = HadoopUtils.addPropertyToFile(clusterConfig,
						nodeConfig, hdfsSiteXmlPath, propertyName,
						paramList.get(propertyName));

				if (!status) {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not add " + propertyName + " property to "
									+ hdfsSiteXmlPath + " file",
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					return false;
				}
			}
			return true;

		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not update Hadoop yarn-site.xml  file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * configureCoreSiteXml(com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	protected boolean configureCoreSiteXml(NodeConfig nodeConfig) {
		boolean status = true;
		try {
			// configuring core-site.xml file in $HADOOP_CONF_DIR
			String coreSiteXmlPath = HadoopUtils
					.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.XML_CORE_SITE;

			LOG.info("Configuring "
					+ HadoopConstants.FileName.ConfigurationFile.XML_CORE_SITE
					+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());

			String valFsDefaultFS = HadoopUtils.getHdfsUri(compConfig);
			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put("fs.defaultFS", valFsDefaultFS);
			paramList
					.put("hadoop.tmp.dir",
							(String) this.compConfig
									.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.TMP_DIR_HADOOP));
			// Constant.URI_FILE_PREFIX + conf.getHadoopTmpDir());

			for (String propertyName : paramList.keySet()) {
				status = HadoopUtils.addPropertyToFile(clusterConfig,
						nodeConfig, coreSiteXmlPath, propertyName,
						paramList.get(propertyName));

				if (!status) {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not add " + propertyName + " property to "
									+ coreSiteXmlPath + " file",
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not update Hadoop core-site.xml file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * configureMapredSiteXml(com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	protected boolean configureMapredSiteXml(NodeConfig nodeConfig) {
		try {
			if (!createMapredSiteXml(nodeConfig)) {
				return false;
			}
			String mapredSiteXmlPath = HadoopUtils
					.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.XML_MAPRED_SITE;

			LOG.info(
					"Configuring "
							+ HadoopConstants.FileName.ConfigurationFile.XML_MAPRED_SITE
							+ " file", Constant.Component.Name.HADOOP,
					nodeConfig.getHost());

			Map<String, String> paramList = new HashMap<String, String>();
			String mapreduceFramework = (String) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.MAPREDUCE_FRAMEWORK);
			if (mapreduceFramework == null) {
				mapreduceFramework = Hadoop2Configurator.DEFAULT_MAPREDUCE_FRAMEWORK_YARN;
			}

			paramList.put("mapreduce.framework.name", mapreduceFramework);

			paramList
					.put("mapreduce.cluster.temp.dir",
							HadoopConstants.URI_FILE_PREFIX
									+ (String) this.compConfig
											.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.TMP_DIR_MAPRED));
			boolean jobHistoryServerEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.JOBHISTORYSERVER_ENABLED);

			if (jobHistoryServerEnabled) {
				paramList
						.put("mapreduce.jobhistory.address",
								HadoopUtils
										.getJobHistoryServerHost(this.compConfig)
										+ ":"
										+ Hadoop2Configurator.DEFAULT_PORT_REST_JOBHISTORYSERVER);
			}

			for (String propertyName : paramList.keySet()) {
				if (!HadoopUtils.addPropertyToFile(clusterConfig, nodeConfig,
						mapredSiteXmlPath, propertyName,
						paramList.get(propertyName))) {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not add " + propertyName + " property to "
									+ mapredSiteXmlPath + " file",
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not update Hadoop mapred-site.xml  file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	private boolean createMapredSiteXml(NodeConfig nodeConfig) {
		try {
			String mapredsite = HadoopUtils.getHadoopConfDir(this.compConfig)
					+ HadoopConstants.FileName.ConfigurationFile.XML_MAPRED_SITE;

			String mapredsiteTemplate = HadoopUtils
					.getHadoopConfDir(this.compConfig)
					+ "mapred-site.xml.template";

			AnkushTask moveFile = new Move(mapredsiteTemplate, mapredsite);
			Result res = nodeConfig.getConnection().exec(moveFile);
			if (res.rc != 0) {
				HadoopUtils.addAndLogError(LOG, clusterConfig,
						"Could not create Hadoop mapred-site.xml file",
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not create Hadoop mapred-site.xml file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * configureGangliaMetrics(com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	protected boolean configureGangliaMetrics(NodeConfig nodeConfig) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * setHadoopConfDir()
	 */
	@Override
	public void setHadoopConfDir() {
		this.compConfig.addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.DIR_CONF,
				this.compConfig.getHomeDir()
						+ Hadoop2Configurator.RELPATH_CONF_DIR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * setHadoopScriptDir()
	 */
	@Override
	public void setHadoopScriptDir() {
		this.compConfig.addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.DIR_SCRIPT,
				this.compConfig.getHomeDir()
						+ Hadoop2Configurator.RELPATH_SCRIPT_DIR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator#
	 * setHadoopBinDir()
	 */
	@Override
	public void setHadoopBinDir() {
		this.compConfig.addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.DIR_BIN,
				this.compConfig.getHomeDir()
						+ Hadoop2Configurator.RELPATH_BIN_DIR);
	}

	@Override
	public void setRpcPorts() {
		this.compConfig.addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.RpcPort.NAMENODE,
				Hadoop2Configurator.DEFAULT_PORT_RPC_NAMENODE);
		this.compConfig.addAdvanceConfProperty(
				HadoopConstants.AdvanceConfKeys.RpcPort.RESOURCEMANAGER,
				Hadoop2Configurator.DEFAULT_PORT_RPC_RESOURCEMANAGER);
	}

	@Override
	public boolean setupPasswordlessSSH(Set<String> nodes) {
		try {
			LOG.info("Configuring Passwordless SSH for Hadoop 2.x cluster",
					Constant.Component.Name.HADOOP);

			if (!this.generateRsaKeysForHadoopNodes(nodes)) {
				return false;
			}

			Set<String> sourceHosts = new HashSet<String>();
			sourceHosts.add(HadoopUtils.getNameNodeHost(this.compConfig));
			sourceHosts
					.add(HadoopUtils.getResourceManagerHost(this.compConfig));

			boolean isHaEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

			if (isHaEnabled) {
				sourceHosts.add(HadoopUtils
						.getActiveNameNodeHost(this.compConfig));
				sourceHosts.add(HadoopUtils
						.getStandByNameNodeHost(this.compConfig));
			}

			for (String sourceHost : sourceHosts) {
				if (this.clusterConfig.getNodes().containsKey(sourceHost)) {
					if (!HadoopUtils.setupPasswordlessSSH(LOG,
							this.clusterConfig, sourceHost, nodes)) {
						return false;
					}
				} else {
					SSHExec connection = SSHUtils.connectToNode(sourceHost,
							this.clusterConfig.getAuthConf());
					if (!HadoopUtils.setupPasswordlessSSH(LOG,
							this.clusterConfig, sourceHost, nodes, connection)) {
						return false;
					}
					if (connection != null) {
						connection.disconnect();
					}
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

			Set<String> journalNodes = new HashSet<String>();

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
					Constant.Component.Name.HADOOP, e);
			return false;
		}
		return true;
	}

	@Override
	public String getAgentProperties(NodeConfig nodeConfig)
			throws AnkushException {
		try {
			boolean isHaEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

			if (isHaEnabled
					&& nodeConfig.getRoles()
							.get(Constant.Component.Name.HADOOP)
							.contains(HadoopConstants.Roles.NAMENODE)) {

				int hadoopDataUpdateTime = 30;
				StringBuffer agentConfProp = new StringBuffer();
				agentConfProp.append("HADOOP_DATA_UPDATE_TIME=")
						.append(hadoopDataUpdateTime)
						.append(Constant.Strings.LINE_SEPERATOR);
				agentConfProp.append("URL_NAMENODE_ROLE=")
						.append(HadoopConstants.Hadoop.NNROLE_INFO_KEY)
						.append(Constant.Strings.LINE_SEPERATOR);
				agentConfProp.append("NAMENODE_HTTP_PORT=")
						.append(HadoopUtils.getNameNodeHttpPort(compConfig))
						.append(Constant.Strings.LINE_SEPERATOR);
				return agentConfProp.toString();
			}
			return "";
		} catch (Exception e) {
			String errorMsg = "Could not get Agent properties for Hadoop";
			HadoopUtils.addAndLogError(LOG, clusterConfig, errorMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errorMsg);
		}
	}

	@Override
	protected void addNodeRolesToMap(NodeRolesMap nodeRolesMap) {
		nodeRolesMap
				.put(this.compConfig
						.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.NAMENODE),
						HadoopConstants.Roles.NAMENODE);
		nodeRolesMap
				.put(this.compConfig
						.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.RESOURCE_MANAGER),
						HadoopConstants.Roles.RESOURCEMANAGER);
		Set<String> slavesList = HadoopUtils.getSlaveHosts(compConfig);
		for (String slave : slavesList) {
			nodeRolesMap.put(slave, HadoopConstants.Roles.DATANODE);
			nodeRolesMap.put(slave, HadoopConstants.Roles.NODEMANAGER);
		}
	}

	@Override
	public Set<String> getHAServicesList() {
		Set<String> serviceList = new HashSet<String>();
		serviceList.add(HadoopConstants.Roles.NAMENODE);
		serviceList.add(HadoopConstants.Roles.DATANODE);
		serviceList.add(HadoopConstants.Roles.RESOURCEMANAGER);
		serviceList.add(HadoopConstants.Roles.NODEMANAGER);
		if (HadoopUtils.getSecondaryNameNodeHost(compConfig) != null) {
			serviceList.add(HadoopConstants.Roles.SECONDARYNAMENODE);
		}
		Boolean jobHistoryServerEnabled = (Boolean) this.compConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.JOBHISTORYSERVER_ENABLED);

		if (jobHistoryServerEnabled != null && jobHistoryServerEnabled) {
			serviceList.add(HadoopConstants.Roles.JOBHISTORYSERVER);
		}

		Boolean webAppProxyServerEnabled = (Boolean) this.compConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.WEBAPPPROXYSERVER_ENABLED);

		if (webAppProxyServerEnabled != null && webAppProxyServerEnabled) {
			serviceList.add(HadoopConstants.Roles.WEBAPPPROXYSERVER);
		}

		Boolean isHaEnabled = (Boolean) this.compConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

		if (isHaEnabled != null && isHaEnabled) {
			serviceList.add(HadoopConstants.Roles.JOURNALNODE);
			serviceList.add(HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER);
		}

		return serviceList;
	}

//	@Override
//	public boolean canNodesBeDeleted(Collection<String> nodes) throws Exception {
//		// TODO Auto-generated method stub
//		return false;
//	}
}
