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
package com.impetus.ankush2.hadoop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.io.FilenameUtils;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.impetus.ankush.AppStore;
import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.exception.RegisterClusterException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushRestClient;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.ReflectionUtil;
import com.impetus.ankush.common.utils.XmlUtil;
import com.impetus.ankush2.common.scripting.impl.AddConfProperty;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.config.CmpConfigMapping;
import com.impetus.ankush2.hadoop.config.CmpConfigMappingSet;
import com.impetus.ankush2.hadoop.config.ComponentConfigContext;
import com.impetus.ankush2.hadoop.deployer.configurator.Hadoop1Configurator;
import com.impetus.ankush2.hadoop.deployer.configurator.Hadoop2Configurator;
import com.impetus.ankush2.hadoop.deployer.configurator.HadoopConfigurator;
import com.impetus.ankush2.hadoop.deployer.installer.HadoopInstaller;
import com.impetus.ankush2.hadoop.deployer.servicemanager.HadoopServiceManager;
import com.impetus.ankush2.hadoop.monitor.Hadoop2Monitor;
import com.impetus.ankush2.hadoop.monitor.HadoopDFSManager;
import com.impetus.ankush2.hadoop.monitor.HadoopMonitor;
import com.impetus.ankush2.hadoop.monitor.commandsmanager.HadoopCommandsManager;
import com.impetus.ankush2.hadoop.utils.HadoopConstants.ConfigXmlKeys.Attributes;
import com.impetus.ankush2.logger.AnkushLogger;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopUtils.
 * 
 * @author Akhil
 */
public class HadoopUtils {

	/** The log. */
	private static AnkushLogger LOG = new AnkushLogger(HadoopUtils.class);

	/** The conf manager. */
	private final static ConfigurationManager confManager = new ConfigurationManager();

	/** The Constant TOPOLOGY_DATA_FILE. */
	public static final String TOPOLOGY_DATA_FILE = AppStoreWrapper
			.getAnkushConfReader().getStringValue("hadoop.conf.topology.data");

	/** The Constant JMX_BEAN_NAME_JAVA_RUNTIME. */
	public static final String JMX_BEAN_NAME_JAVA_RUNTIME = "java.lang:type=Runtime";

	/** The Constant KEY_HADOOP_HOME. */
	public static final String KEY_HADOOP_HOME = "HADOOP_HOME";

	/** The Constant KEY_HADOOP_PREFIX. */
	public static final String KEY_HADOOP_PREFIX = "HADOOP_PREFIX";

	/** The Constant KEY_HADOOP_OPTS. */
	public static final String KEY_HADOOP_OPTS = "HADOOP_OPTS";

	/** The Constant KEY_HADOOP_CONF_DIR. */
	public static final String KEY_HADOOP_CONF_DIR = "HADOOP_CONF_DIR";

	/** The Constant KEY_PATH_VARIABLE. */
	public static final String KEY_PATH_VARIABLE = "PATH";

	/**
	 * Adds the and log error.
	 * 
	 * @param logger
	 *            the logger
	 * @param clusterConfig
	 *            the cluster config
	 * @param errMsg
	 *            the err msg
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 */
	public static void addAndLogError(AnkushLogger logger,
			ClusterConfig clusterConfig, String errMsg, String componentName,
			String host) {
		logger.error(errMsg, componentName, host);
		clusterConfig.addError(host, componentName, errMsg);
	}

	/**
	 * Adds the and log error.
	 * 
	 * @param logger
	 *            the logger
	 * @param clusterConfig
	 *            the cluster config
	 * @param errMsg
	 *            the err msg
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 * @param t
	 *            the t
	 */
	public static void addAndLogError(AnkushLogger logger,
			ClusterConfig clusterConfig, String errMsg, String componentName,
			String host, Throwable t) {
		logger.error(errMsg, componentName, host, t);
		clusterConfig.addError(host, componentName, errMsg);
	}

	/**
	 * Adds the and log error.
	 * 
	 * @param logger
	 *            the logger
	 * @param clusterConfig
	 *            the cluster config
	 * @param errMsg
	 *            the err msg
	 * @param componentName
	 *            the component name
	 */
	public static void addAndLogError(AnkushLogger logger,
			ClusterConfig clusterConfig, String errMsg, String componentName) {
		logger.error(errMsg, componentName);
		clusterConfig.addError(componentName, errMsg);
	}

	/**
	 * Adds the and log error.
	 * 
	 * @param logger
	 *            the logger
	 * @param clusterConfig
	 *            the cluster config
	 * @param errMsg
	 *            the err msg
	 * @param componentName
	 *            the component name
	 * @param t
	 *            the t
	 */
	public static void addAndLogError(AnkushLogger logger,
			ClusterConfig clusterConfig, String errMsg, String componentName,
			Throwable t) {
		logger.error(errMsg, componentName, t);
		clusterConfig.addError(componentName, errMsg);
	}

	public static void addAndLogWarning(AnkushLogger logger,
			ClusterConfig clusterConfig, String warnMsg, String componentName,
			Throwable t) {
		logger.warn(warnMsg, componentName, t);
		clusterConfig.addError(componentName, warnMsg);
	}

	/** The Constant TOPOLOGY_SCRIPT_FILE. */
	private static final String TOPOLOGY_SCRIPT_FILE = AppStoreWrapper
			.getAnkushConfReader()
			.getStringValue("hadoop.conf.topology.script");

	/** The Constant TOPOLOGY_SCRIPT_FILE_RELPATH_RESOURCES. */
	private static final String TOPOLOGY_SCRIPT_FILE_RELPATH_RESOURCES = AppStoreWrapper
			.getAnkushConfReader().getStringValue(
					"hadoop.conf.topology.script.relpath");

	/** The Constant VERSION_CDH3. */
	public static final String VERSION_CDH3 = "0.20.2-cdh3u1";

	/** The Constant VERSION_CDH4.4.0. */
	public static final String VERSION_CDH4_4_0 = "2.0.0-cdh4.4.0";

	/** The Constant DEFAULT_NODE_IP. */
	public static final String DEFAULT_NODE_IP = "0.0.0.0";


	/**
	 * Adds the property to xml file.
	 * 
	 * @param logger
	 *            the logger
	 * @param clusterConfig
	 *            the cluster config
	 * @param nodeConfig
	 *            the node config
	 * @param filePath
	 *            the file path
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 * @return true, if successful
	 */
	public static boolean addPropertyToXmlFile(AnkushLogger logger,
			ClusterConfig clusterConfig, NodeConfig nodeConfig,
			String filePath, String propertyName, String propertyValue) {
		try {
			// Configuration manager to save the property file change records.
			ConfigurationManager confManager = new ConfigurationManager();
			Result res = null;
			AnkushTask addProperty = new AddConfProperty(propertyName,
					propertyValue, filePath, Constant.File_Extension.XML,
					clusterConfig.getAgentInstallDir());
			res = nodeConfig.getConnection().exec(addProperty);
			if (!res.isSuccess) {
				String errMsg = "Could not add " + propertyName + " to "
						+ filePath;
				HadoopUtils.addAndLogError(logger, clusterConfig, errMsg,
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
			// saving the Add Event for Audit Trail
			confManager.saveConfiguration(clusterConfig.getClusterId(),
					clusterConfig.getCreatedBy(),
					FilenameUtils.getName(filePath), nodeConfig.getHost(),
					propertyName, propertyValue);
		} catch (Exception e) {
			String errMsg = "Could not add " + propertyName + " to " + filePath;
			LOG.error(errMsg, Constant.Component.Name.HADOOP,
					nodeConfig.getHost(), e);
			HadoopUtils.addAndLogError(logger, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	/**
	 * Gets the gmt from time in millis.
	 * 
	 * @param val
	 *            the val
	 * @return the gmt from time in millis
	 */
	public static String getGmtFromTimeInMillis(String val) {
		long time = Long.parseLong(val);
		DateFormat gmtFormat = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm:ss");
		TimeZone gmtTime = TimeZone.getTimeZone("GMT");
		gmtFormat.setTimeZone(gmtTime);
		return (gmtFormat.format(new Date(time)) + " GMT");

	}

	/**
	 * Gets the gmt from time in millis.
	 * 
	 * @param time
	 *            the time
	 * @return the gmt from time in millis
	 */
	public static String getGmtFromTimeInMillis(long time) {
		DateFormat gmtFormat = new SimpleDateFormat("E, dd-MMM-yyyy hh:mm:ss");
		TimeZone gmtTime = TimeZone.getTimeZone("GMT");
		gmtFormat.setTimeZone(gmtTime);
		return (gmtFormat.format(new Date(time)) + " GMT");

	}

	/**
	 * Convert millis to time.
	 * 
	 * @param val
	 *            the val
	 * @return the string
	 */
	public static String convertMillisToTime(String val) {
		long milliseconds = Long.parseLong(val);
		long seconds, minutes, hours;
		seconds = milliseconds / 1000;
		minutes = seconds / 60;
		seconds = seconds % 60;
		hours = minutes / 60;
		minutes = minutes % 60;
		if (hours > 0) {
			return (hours + "hrs, " + minutes + "mins, " + seconds + "secs");
		} else if (minutes > 0) {
			return (minutes + "mins, " + seconds + "secs");
		} else {
			return (seconds + "secs");
		}
	}

	/**
	 * Gets the job id from app id.
	 * 
	 * @param appId
	 *            the app id
	 * @return the job id from app id
	 */
	public static String getJobIdFromAppId(String appId) {
		return appId.replaceAll("application", "job");
	}

	/**
	 * Gets the hadoop config.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @return the hadoop config
	 */
	public static ComponentConfig getHadoopConfig(ClusterConfig clusterConfig) {
		ComponentConfig hadoopConfig = clusterConfig.getComponents().get(
				Constant.Component.Name.HADOOP);
		return hadoopConfig;
	}

	public static boolean isMonitoredByAnkush(ComponentConfig hadoopConfig) {
		if (hadoopConfig.isRegister()) {
			HadoopConstants.RegisterLevel registerLevel = HadoopConstants.RegisterLevel
					.valueOf((String) hadoopConfig.getAdvanceConf().get(
							HadoopConstants.AdvanceConfKeys.REGISTER_LEVEL));
			if (registerLevel != null) {
				if (registerLevel.equals(HadoopConstants.RegisterLevel.LEVEL3)
						|| registerLevel
								.equals(HadoopConstants.RegisterLevel.LEVEL2)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isManagedByAnkush(ComponentConfig hadoopConfig) {
		if (hadoopConfig.isRegister()) {
			HadoopConstants.RegisterLevel registerLevel = HadoopConstants.RegisterLevel
					.valueOf((String) hadoopConfig.getAdvanceConf().get(
							HadoopConstants.AdvanceConfKeys.REGISTER_LEVEL));
			if (registerLevel != null) {
				if (registerLevel.equals(HadoopConstants.RegisterLevel.LEVEL3)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Setup passwordless ssh.
	 * 
	 * @param logger
	 *            the logger
	 * @param clusterConfig
	 *            the cluster config
	 * @param sourceHost
	 *            the source host
	 * @param destinationHosts
	 *            the destination hosts
	 * @return true, if successful
	 */
	public static boolean setupPasswordlessSSH(AnkushLogger logger,
			ClusterConfig clusterConfig, String sourceHost,
			Set<String> destinationHosts) {
		try {
			NodeConfig sourceNodeConfig = clusterConfig.getNodes().get(
					sourceHost);

			String sourceHostPublicKey = com.impetus.ankush2.utils.SSHUtils
					.getFileContents("~/.ssh/id_rsa.pub", sourceNodeConfig);
			if (sourceHostPublicKey == null) {
				HadoopUtils.addAndLogError(logger, clusterConfig,
						"Could not read ~/.ssh/id_rsa.pub file",
						Constant.Component.Name.HADOOP, sourceHost);
				return false;
			}

			// add host entry in know host.
			for (String destinationHost : destinationHosts) {
				String cmdAddToKnowHost = "ssh -o ConnectTimeout=15 -o ConnectionAttempts=5 -o StrictHostKeyChecking=no "
						+ destinationHost + " ls";

				CustomTask task = new ExecCommand(cmdAddToKnowHost);

				sourceNodeConfig.getConnection().exec(task);

				StringBuilder appendKeysCmd = new StringBuilder();
				appendKeysCmd.append("echo \"").append(sourceHostPublicKey)
						.append("\" >> ~/.ssh/authorized_keys");

				String changePermissionCmd = "chmod 0600 ~/.ssh/authorized_keys";
				task = new ExecCommand(appendKeysCmd.toString(),
						changePermissionCmd);

				if (clusterConfig.getNodes().get(destinationHost)
						.getConnection().exec(task).rc != 0) {
					HadoopUtils.addAndLogError(logger, clusterConfig,
							"Could not update ~/.ssh/authorized_keys file for source "
									+ sourceHost,
							Constant.Component.Name.HADOOP, destinationHost);
					return false;
				}
			}

		} catch (Exception e) {
			HadoopUtils.addAndLogError(logger, clusterConfig,
					"Could not configure passwordless SSH " + sourceHost,
					Constant.Component.Name.HADOOP, sourceHost, e);
			return false;
		}
		return true;
	}

	public static boolean setupPasswordlessSSH(AnkushLogger logger,
			ClusterConfig clusterConfig, String sourceHost,
			Set<String> destinationHosts, SSHExec sourceConnection) {
		try {

			String sourceHostPublicKey = com.impetus.ankush2.utils.SSHUtils
					.getFileContents("~/.ssh/id_rsa.pub", sourceConnection);
			if (sourceHostPublicKey == null) {
				HadoopUtils.addAndLogError(logger, clusterConfig,
						"Could not read ~/.ssh/id_rsa.pub file",
						Constant.Component.Name.HADOOP, sourceHost);
				return false;
			}

			// add host entry in know host.
			for (String destinationHost : destinationHosts) {
				String cmdAddToKnowHost = "ssh -o ConnectTimeout=15 -o ConnectionAttempts=5 -o StrictHostKeyChecking=no "
						+ destinationHost + " ls";

				CustomTask task = new ExecCommand(cmdAddToKnowHost);

				sourceConnection.exec(task);

				task = new ExecCommand("echo \"" + sourceHostPublicKey
						+ "\" >> ~/.ssh/authorized_keys",
						"chmod 0600 ~/.ssh/authorized_keys");

				if (clusterConfig.getNodes().get(destinationHost)
						.getConnection().exec(task).rc != 0) {
					HadoopUtils.addAndLogError(logger, clusterConfig,
							"Could not update ~/.ssh/authorized_keys file for source "
									+ sourceHost,
							Constant.Component.Name.HADOOP, destinationHost);
					return false;
				}
			}

		} catch (Exception e) {
			HadoopUtils.addAndLogError(logger, clusterConfig,
					"Could not configure passwordless SSH " + sourceHost,
					Constant.Component.Name.HADOOP, sourceHost, e);
			return false;
		}
		return true;
	}

	public static String getHaNameNodeId1(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_NAMENODEID1);
	}

	public static String getHaNameNodeId2(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_NAMENODEID2);
	}
	
	/**
	 * Gets the name node host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the name node host
	 */
	public static String getNameNodeHost(ComponentConfig hadoopConfig) {
		String namenodeHost = (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.NAMENODE);
		if (HadoopUtils.isHadoop2Config(hadoopConfig)) {
			boolean isHaEnabled = (Boolean) hadoopConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);
			if (isHaEnabled) {
				namenodeHost = (String) hadoopConfig
						.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ACTIVE_NAMENODE);
			}
		}
		return namenodeHost;
	}

	public static String getHdfsUri(ComponentConfig hadoopConfig) {
		StringBuilder hdfsUri = new StringBuilder(
				HadoopConfigurator.HADOOP_URI_PREFIX);
		if (HadoopUtils.isHdfsHaEnabled(hadoopConfig)) {
			hdfsUri.append((String) hadoopConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_NAMESERVICEID));
			return hdfsUri.toString();
		}
		hdfsUri.append(HadoopUtils.getNameNodeHost(hadoopConfig)).append(":")
				.append(HadoopUtils.getNameNodeRpcPort(hadoopConfig));
		return hdfsUri.toString();
	}

	public static boolean isHdfsHaEnabled(ComponentConfig hadoopConfig) {
		if (HadoopUtils.isHadoop2Config(hadoopConfig)) {
			return (Boolean) hadoopConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);
		} else {
			return false;
		}
	}

	/**
	 * Gets the ha name node hosts.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the ha name node hosts
	 */
	public static Set<String> getHaNameNodeHosts(ComponentConfig hadoopConfig) {
		if (hadoopConfig.getAdvanceConf().containsKey(
				HadoopConstants.AdvanceConfKeys.HA_NAMENODE_HOSTS)) {
			return (Set<String>) hadoopConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_NAMENODE_HOSTS);
		} else {
			Set<String> haNameNodeHosts = new HashSet<String>();
			haNameNodeHosts
					.add((String) hadoopConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ACTIVE_NAMENODE));
			haNameNodeHosts
					.add((String) hadoopConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_STANDBY_NAMENODE));
			hadoopConfig.addAdvanceConfProperty(
					HadoopConstants.AdvanceConfKeys.HA_NAMENODE_HOSTS,
					haNameNodeHosts);
			return haNameNodeHosts;
		}
	}

	/**
	 * Gets the slave hosts.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the slave hosts
	 */
	public static Set<String> getSlaveHosts(ComponentConfig hadoopConfig) {
		return (Set<String>) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.SLAVES);
	}

	/**
	 * Gets the journal node hosts.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the journal node hosts
	 */
	public static Set<String> getJournalNodeHosts(ComponentConfig hadoopConfig) {
		return (Set<String>) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_JOURNALNODES);
	}

	/**
	 * Gets the secondary name node host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the secondary name node host
	 */
	public static String getSecondaryNameNodeHost(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.SECONDARY_NAMENODE);
	}

	/**
	 * Gets the job tracker host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the job tracker host
	 */
	public static String getJobTrackerHost(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.JOBTRACKER);
	}

	/**
	 * Gets the resource manager host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the resource manager host
	 */
	public static String getResourceManagerHost(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.RESOURCE_MANAGER);
	}

	/**
	 * Gets the resource manager http port.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the resource manager http port
	 */
	public static String getResourceManagerHttpPort(ComponentConfig hadoopConfig) {
		String rmHttpPort = hadoopConfig
				.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.HttpPort.RESOURCEMANAGER);
		if (rmHttpPort == null) {
			return Hadoop2Configurator.DEFAULT_PORT_HTTP_RESOURCEMANAGER;
		}
		return rmHttpPort;
	}

	/**
	 * Gets the name node http port.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the name node http port
	 */
	public static String getNameNodeHttpPort(ComponentConfig hadoopConfig) {
		String namenodeHttpPort = hadoopConfig
				.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.HttpPort.NAMENODE);
		if (namenodeHttpPort == null) {
			return Hadoop1Configurator.DEFAULT_PORT_HTTP_NAMENODE;
		}
		return namenodeHttpPort;
	}

	/**
	 * Gets the name node rpc port.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the name node rpc port
	 */
	public static String getNameNodeRpcPort(ComponentConfig hadoopConfig) {
		String namenodeHttpPort = hadoopConfig
				.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.RpcPort.NAMENODE);
		if (namenodeHttpPort == null) {
			if (HadoopUtils.isHadoop2Config(hadoopConfig)) {
				return Hadoop2Configurator.DEFAULT_PORT_RPC_NAMENODE;
			} else {
				return Hadoop1Configurator.DEFAULT_PORT_RPC_NAMENODE;
			}
		}
		return namenodeHttpPort;
	}

	/**
	 * Gets the job tracker http port.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the job tracker http port
	 */
	public static String getJobTrackerHttpPort(ComponentConfig hadoopConfig) {
		String jobtrackerHttpPort = hadoopConfig
				.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.HttpPort.JOBTRACKER);
		if (jobtrackerHttpPort == null) {
			return Hadoop1Configurator.DEFAULT_PORT_HTTP_JOBTRACKER;
		}
		return jobtrackerHttpPort;
	}

	/**
	 * Gets the job tracker rpc port.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the job tracker rpc port
	 */
	public static String getJobTrackerRpcPort(ComponentConfig hadoopConfig) {
		String jobtrackerRpcPort = hadoopConfig
				.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.RpcPort.JOBTRACKER);
		if (jobtrackerRpcPort == null) {
			return Hadoop1Configurator.DEFAULT_PORT_RPC_JOBTRACKER;
		}
		return jobtrackerRpcPort;
	}

	/**
	 * Gets the job history server host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the job history server host
	 */
	public static String getJobHistoryServerHost(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.JOB_HISTORY_SERVER);
	}

	/**
	 * Gets the web app proxy server host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the web app proxy server host
	 */
	public static String getWebAppProxyServerHost(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.WEB_APP_PROXY_SERVER);
	}

	/**
	 * Gets the active name node host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the active name node host
	 */
	public static String getActiveNameNodeHost(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ACTIVE_NAMENODE);
	}

	/**
	 * Gets the stand by name node host.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the stand by name node host
	 */
	public static String getStandByNameNodeHost(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_STANDBY_NAMENODE);
	}

	/**
	 * Gets the rpc port name node.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the rpc port name node
	 */
	public static String getRpcPortNameNode(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.RpcPort.NAMENODE);
	}

	/**
	 * Gets the rpc port resource manager.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the rpc port resource manager
	 */
	public static String getRpcPortResourceManager(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.RpcPort.RESOURCEMANAGER);
	}

	/**
	 * Gets the rpc port job tracker.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the rpc port job tracker
	 */
	public static String getRpcPortJobTracker(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.RpcPort.JOBTRACKER);
	}

	/**
	 * Gets the jmx bean url.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @param clientPort
	 *            the client port
	 * @param beanName
	 *            the bean name
	 * @return the jmx bean url
	 */
	public static String getJmxBeanUrl(String nodeIp, String clientPort,
			String beanName) {
		return ("http://" + nodeIp + ":" + clientPort + "/jmx?qry=" + beanName);
	}

	/**
	 * Gets the bean json for jmx data.
	 * 
	 * @param urlDfsJmxBean
	 *            the url dfs jmx bean
	 * @return the bean json for jmx data
	 */
	public JSONObject getBeanJsonForJmxData(String urlDfsJmxBean) {
		String errMsg = "Exception: Unable to get data using url : "
				+ urlDfsJmxBean;
		JSONObject json = null;
		try {
			AnkushRestClient restClient = new AnkushRestClient();
			String data = restClient.getRequest(urlDfsJmxBean);
			if (data == null) {
				LOG.error(errMsg);
			} else {
				json = (JSONObject) new JSONParser().parse(data);
			}
		} catch (Exception e) {
			LOG.error(errMsg);
			LOG.error(e.getMessage());
		}
		LOG.info("json " + json.toJSONString());
		return json;
	}

	/**
	 * Gets the jmx bean using callable.
	 * 
	 * @param host
	 *            the host
	 * @param clientPort
	 *            the client port
	 * @param beanName
	 *            the bean name
	 * @return the jmx bean using callable
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public static Map<String, Object> getJmxBeanUsingCallable(String host,
			String clientPort, String beanName) throws AnkushException {
		String errMsg = "Could not get JMX bean data for host-" + host
				+ ", port-" + clientPort + ".";
		try {
			long waitTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
					"hadoop.jmxmonitoring.wait.time");
			CallableJmxBeanData callableJmxBeanData = new CallableJmxBeanData(
					host, clientPort, beanName);
			FutureTask<Map<String, Object>> futureTaskJmxBeanData = new FutureTask<Map<String, Object>>(
					callableJmxBeanData);

			AppStoreWrapper.getExecutor().execute(futureTaskJmxBeanData);

			Map<String, Object> beanObject = futureTaskJmxBeanData.get(
					waitTime, TimeUnit.MILLISECONDS);
			if (beanObject == null) {
				throw new AnkushException(errMsg);
			}
			return beanObject;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(errMsg, Constant.Component.Name.HADOOP, host, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * Gets the json object using callable.
	 * 
	 * @param url
	 *            the url
	 * @return the json object using callable
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public static JSONObject getJsonObjectUsingCallable(String url)
			throws AnkushException {
		String errMsg = "Could not get JSON object for URL-" + url + ".";
		try {
			long waitTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
					"hadoop.jmxmonitoring.wait.time");
			CallableRestJsonData callableRestJsonData = new CallableRestJsonData(
					url);

			FutureTask<JSONObject> futureTaskJmxBeanData = new FutureTask<JSONObject>(
					callableRestJsonData);

			AppStoreWrapper.getExecutor().execute(futureTaskJmxBeanData);

			JSONObject beanObject = futureTaskJmxBeanData.get(waitTime,
					TimeUnit.MILLISECONDS);
			if (beanObject == null) {
				throw new AnkushException(errMsg);
			}
			return beanObject;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			LOG.error(errMsg, Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * Gets the jmx bean data.
	 * 
	 * @param host
	 *            the node ip
	 * @param port
	 *            the client port
	 * @param beanName
	 *            the bean name
	 * @return the jmx bean data
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public Map<String, Object> getJmxBeanData(String host, String port,
			String beanName) throws AnkushException {

		String urlJmxBean = "";
		if (beanName != null && (!beanName.isEmpty())) {
			urlJmxBean = HadoopUtils.getJmxBeanUrl(host, port, beanName);
		} else {
			throw (new AnkushException(
					"Could not fetch JMX data: Invalid Bean Name"));
		}

		try {
			JSONObject jmxDataJson = this.getBeanJsonForJmxData(urlJmxBean);
			Map<String, Object> beanObject = new HashMap<String, Object>();
			if (jmxDataJson != null) {
				List<JSONObject> beanObjList = (List<JSONObject>) jmxDataJson
						.get(HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_BEANS);
				if (beanObjList.size() == 1) {
					beanObject = (Map<String, Object>) beanObjList.get(0);
				} else {
					throw (new AnkushException(
							"Could not fetch JMX data from URL: " + urlJmxBean));
				}
			}
			return beanObject;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			String errMsg = "Could not fetch JMX data from URL: " + urlJmxBean;
			LOG.error(errMsg, Constant.Component.Name.HADOOP, host, e);
			throw (new AnkushException(errMsg));
		}

	}

	/**
	 * Gets the process start time.
	 * 
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @return the process start time
	 * @throws Exception
	 *             the exception
	 */
	public static String getProcessStartTime(String host, String port)
			throws Exception {
		Map<String, Object> beanJavaRuntime = new HashMap<String, Object>();
		beanJavaRuntime = HadoopUtils.getJmxBeanUsingCallable(host, port,
				HadoopMonitor.JMX_BEAN_NAME_JAVA_RUNTIME);
		long startTime = ((Number) beanJavaRuntime
				.get(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.STARTTIME))
				.longValue();
		return HadoopUtils.getGmtFromTimeInMillis(startTime);
	}

	/**
	 * Gets the process thread count.
	 * 
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @return the process thread count
	 * @throws Exception
	 *             the exception
	 */
	public static String getProcessThreadCount(String host, String port)
			throws Exception {
		Map<String, Object> beanJavaRuntime = new HashMap<String, Object>();
		String beanName = HadoopMonitor.JMX_BEAN_NAME_JAVA_THREADING;
		beanJavaRuntime = HadoopUtils.getJmxBeanUsingCallable(host, port,
				beanName);
		long threadCount = ((Number) beanJavaRuntime
				.get(HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_THREAD_COUNT))
				.longValue();
		return String.valueOf(threadCount);
	}

	/**
	 * Gets the memory used from jmx.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @param clientPort
	 *            the client port
	 * @param memoryType
	 *            the memory type
	 * @return the memory used from jmx
	 * @throws Exception
	 *             the exception
	 */
	public String getMemoryUsedFromJmx(String nodeIp, String clientPort,
			ProcessMemoryType memoryType) throws Exception {
		Map<String, Object> beanJavaMemory = new HashMap<String, Object>();
		String beanName = HadoopMonitor.JMX_BEAN_NAME_JAVA_MEMORY;
		beanJavaMemory = this.getJmxBeanData(nodeIp, clientPort, beanName);
		String memoryKeyName = "";
		switch (memoryType) {
		case HEAP:
			memoryKeyName = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_HEAP_MEMORY_USAGE;
			break;
		case NONHEAP:
			memoryKeyName = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_NON_HEAP_MEMORY_USAGE;
			break;
		}
		JSONObject memoryUsageJson = (JSONObject) beanJavaMemory
				.get(memoryKeyName);

		long heapUsed = ((Number) memoryUsageJson
				.get(HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_MEMORY_USAGE_USED))
				.longValue();

		return HadoopDFSManager.convertBytes(heapUsed);
	}

	/**
	 * Gets the memory info from jmx bean.
	 * 
	 * @param beanJavaMemory
	 *            the bean java memory
	 * @param memoryType
	 *            the memory type
	 * @param usageType
	 *            the usage type
	 * @return the memory info from jmx bean
	 * @throws Exception
	 *             the exception
	 */
	public static String getMemoryInfoFromJmxBean(
			Map<String, Object> beanJavaMemory, ProcessMemoryType memoryType,
			ProcessMemoryUsageType usageType) throws Exception {

		String memoryTypeKey = new String();
		String usageTypeKey = new String();

		switch (memoryType) {
		case HEAP:
			memoryTypeKey = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_HEAP_MEMORY_USAGE;
			break;
		case NONHEAP:
			memoryTypeKey = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_NON_HEAP_MEMORY_USAGE;
			break;
		}

		switch (usageType) {
		case INIT:
			usageTypeKey = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_MEMORY_USAGE_INIT;
			break;
		case USED:
			usageTypeKey = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_MEMORY_USAGE_USED;
			break;
		case MAX:
			usageTypeKey = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_MEMORY_USAGE_MAX;
			break;
		case COMMITTED:
			usageTypeKey = HadoopConstants.Hadoop.Keys.JMX_DATA_KEY_MEMORY_USAGE_COMMITTED;
			break;
		}

		JSONObject memoryUsageJson = (JSONObject) beanJavaMemory
				.get(memoryTypeKey);

		long heapUsed = ((Number) memoryUsageJson.get(usageTypeKey))
				.longValue();

		return HadoopDFSManager.convertBytes(heapUsed);
	}

	/**
	 * Gets the display name for start time.
	 * 
	 * @param role
	 *            the role
	 * @return the display name for start time
	 */
	public static String getDisplayNameForStartTime(String role) {
		return role + " Started";
	}

	/**
	 * Gets the json from rm rest api.
	 * 
	 * @param rmHost
	 *            the rm host
	 * @param rmHttpPort
	 *            the rm http port
	 * @param relativeUrl
	 *            the relative url
	 * @return the json from rm rest api
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public static JSONObject getJsonFromRmRestApi(String rmHost,
			String rmHttpPort, String relativeUrl) throws AnkushException {
		String url = "http://" + rmHost + ":" + rmHttpPort
				+ Hadoop2Monitor.RELATIVE_URL_RM_REST_API + relativeUrl;
		LOG.info("Connecting to RM REST API - " + url,
				Constant.Component.Name.HADOOP);
		try {
			return HadoopUtils.getJsonObjectUsingCallable(url);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			String errMsg = "Could not fetch data from RM REST API - " + url;
			LOG.error(errMsg, Constant.Component.Name.HADOOP, rmHost, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * Gets the json object from app proxy api.
	 * 
	 * @param webAppProxyHost
	 *            the web app proxy host
	 * @param port
	 *            the port
	 * @param relativeUrl
	 *            the relative url
	 * @param appId
	 *            the app id
	 * @return the json object from app proxy api
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public static JSONObject getJsonObjectFromAppProxyApi(
			String webAppProxyHost, String port, String relativeUrl,
			String appId) throws AnkushException {
		String url = "http://"
				+ webAppProxyHost
				+ ":"
				+ port
				+ Hadoop2Monitor.RELATIVE_URL_APPPROXY_REST_API.replace(
						Hadoop2Monitor.APPPROXY_REST_API_TEXT_APPID, appId)
				+ relativeUrl;

		LOG.info("Connecting to Web Application ProxyServer REST API - " + url,
				Constant.Component.Name.HADOOP);
		try {
			return HadoopUtils.getJsonObjectUsingCallable(url);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			String errMsg = "Could not fetch data from Web Application ProxyServer REST API - "
					+ url;
			LOG.error(errMsg, Constant.Component.Name.HADOOP, webAppProxyHost,
					e);
			throw new AnkushException(errMsg);
		}

	}

	/**
	 * Gets the json object from jhs api.
	 * 
	 * @param jhsHost
	 *            the jhs host
	 * @param jhsHttpPort
	 *            the jhs http port
	 * @param relativeUrl
	 *            the relative url
	 * @return the json object from jhs api
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public static JSONObject getJsonObjectFromJhsApi(String jhsHost,
			String jhsHttpPort, String relativeUrl) throws AnkushException {

		String url = "http://" + jhsHost + ":" + jhsHttpPort
				+ Hadoop2Monitor.RELATIVE_URL_JHS_REST_API + relativeUrl;

		LOG.info("Connecting to JobHistoryServer REST API - " + url,
				Constant.Component.Name.HADOOP);
		try {
			return HadoopUtils.getJsonObjectUsingCallable(url);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			String errMsg = "Could not fetch data from JobHistoryServer REST API - "
					+ url;
			LOG.error(errMsg, Constant.Component.Name.HADOOP, jhsHost, e);
			throw new AnkushException(errMsg);
		}
	}

	public static String getHadoopLogsDir(List<String> runtimeArgumentsList)
			throws Exception {
		String propertyName = HadoopConstants.Hadoop.Keys.INPUT_ARG_HADOOP_LOG_DIR;
		return HadoopUtils.getInputArgumentPropertyValue(runtimeArgumentsList,
				propertyName);
	}

	/**
	 * Gets the hadoop home dir from jmx data.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @param clientPort
	 *            the client port
	 * @return the hadoop home dir from jmx data
	 * @throws Exception
	 *             the exception
	 */
	public static String getHadoopHomeDir(List<String> runtimeArgumentsList)
			throws Exception {
		String propertyName = HadoopConstants.Hadoop.Keys.INPUT_ARG_HADOOP_HOME_DIR;
		return HadoopUtils.getInputArgumentPropertyValue(runtimeArgumentsList,
				propertyName);
	}

	/**
	 * Gets the hadoop script dir.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the hadoop script dir
	 */
	public static String getHadoopScriptDir(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_SCRIPT);
	}

	/**
	 * Gets the hadoop conf dir.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the hadoop conf dir
	 */
	public static String getHadoopConfDir(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_CONF);
	}

	/**
	 * Gets the hadoop bin dir.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the hadoop bin dir
	 */
	public static String getHadoopBinDir(ComponentConfig hadoopConfig) {
		return (String) hadoopConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_BIN);
	}

	/**
	 * Adds the property to file.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param nodeConfig
	 *            the node config
	 * @param filePath
	 *            the file path
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 * @return true, if successful
	 */
	public static boolean addPropertyToFile(ClusterConfig clusterConfig,
			NodeConfig nodeConfig, String filePath, String propertyName,
			String propertyValue) {
		try {
			// Configuration manager to save the property file change records.
			ConfigurationManager confManager = new ConfigurationManager();
			Result res = null;
			AnkushTask addProperty = new AddConfProperty(propertyName,
					propertyValue, filePath, Constant.File_Extension.XML,
					clusterConfig.getAgentInstallDir());
			res = nodeConfig.getConnection().exec(addProperty);
			if (!res.isSuccess) {
				LOG.error("Could not add " + propertyName + " to " + filePath,
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
			// saving the Add Event for Audit Trail
			confManager.saveConfiguration(clusterConfig.getClusterId(),
					clusterConfig.getCreatedBy(),
					FilenameUtils.getName(filePath), nodeConfig.getHost(),
					propertyName, propertyValue);
		} catch (Exception e) {
			LOG.error("Could not add " + propertyName + " to " + filePath,
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	/**
	 * Load hadoop config xml parameters.
	 * 
	 * @param filePath
	 *            the file path
	 * @param root
	 *            the root
	 * @param subItems
	 *            the sub items
	 * @return the hadoop config xml set
	 */
	private static CmpConfigMappingSet loadHadoopConfigXMLParameters(
			String filePath, String root, List<String> subItems) {
		// map of item.
		CmpConfigMappingSet hadoopConfigXmlSet = new CmpConfigMappingSet();
		try {
			// creating sax builder obj.
			SAXBuilder builder = new SAXBuilder();
			// getting file object.
			File xml = new File(filePath);
			// input file stream.
			InputStream inputStream = new FileInputStream(xml);
			// jdom document object.
			org.jdom.Document doc = builder.build(inputStream);
			// getting root element.
			Element elements = doc.getRootElement();
			// getting child elements.
			List child = elements.getChildren(root);

			// iterating over the childs.
			for (int index = 0; index < child.size(); index++) {
				// getting element.
				Element e = (Element) child.get(index);

				String installationType = e.getAttribute(
						Attributes.INSTALLATION_TYPE).getValue();
				String vendor = e.getAttribute(Attributes.VENDOR).getValue();
				String version = e.getAttribute(Attributes.VERSION).getValue();

				Map<String, String> classMap = new HashMap<String, String>();

				// iterating over the element properties.
				for (String subItem : subItems) {
					// getting element values.
					String value = XmlUtil.getTagContent(e, subItem);
					// putting element value.
					classMap.put(subItem, value);
				}

				CmpConfigMapping configXmlObject = new CmpConfigMapping(
						installationType, vendor, version, classMap);

				// putting map against the attribute value.
				hadoopConfigXmlSet.add(configXmlObject);
			}
			// closing input stream.
			inputStream.close();
		} catch (Exception e) {
			// printing stack trace.
			e.printStackTrace();
		}
		// returning items.
		return hadoopConfigXmlSet;
	}

	/**
	 * Sets the hadoop config classes.
	 */
	public static void setHadoopConfigClasses() {
		String classType = "hadoop";
		List<String> subItems = new ArrayList<String>();
		subItems.add(HadoopConstants.ConfigXmlKeys.ClassType.INSTALLER);
		subItems.add(HadoopConstants.ConfigXmlKeys.ClassType.CONFIGURATOR);
		subItems.add(HadoopConstants.ConfigXmlKeys.ClassType.SERVICE_MANAGER);
		subItems.add(HadoopConstants.ConfigXmlKeys.ClassType.COMMANDS_MANAGER);
		subItems.add(HadoopConstants.ConfigXmlKeys.ClassType.MONITOR);

		String filePath = AppStoreWrapper.getConfigClassNameFile(classType);
		CmpConfigMappingSet hadoopConfigXmlSet = null;
		if (filePath != null) {
			try {
				hadoopConfigXmlSet = HadoopUtils
						.loadHadoopConfigXMLParameters(filePath,
								HadoopConstants.ConfigXmlKeys.CLASS, subItems);
				AppStore.setObject(
						Constant.AppStore.CompConfigXmlMapping.HADOOP,
						hadoopConfigXmlSet);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Gets the installer instance.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the installer instance
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static HadoopInstaller getInstallerInstance(
			ClusterConfig clusterConfig, ComponentConfig hadoopConfig)
			throws ClassNotFoundException {
		return (HadoopInstaller) HadoopUtils.getHadoopClassInstance(
				clusterConfig, hadoopConfig,
				HadoopConstants.ConfigXmlKeys.ClassType.INSTALLER);
	}

	/**
	 * Gets the commands manager instance.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return the commands manager instance
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static HadoopCommandsManager getCommandsManagerInstance(
			ClusterConfig clusterConfig, ComponentConfig hadoopConfig)
			throws ClassNotFoundException {
		return (HadoopCommandsManager) HadoopUtils.getHadoopClassInstance(
				clusterConfig, hadoopConfig,
				HadoopConstants.ConfigXmlKeys.ClassType.COMMANDS_MANAGER);
	}

	/**
	 * Gets the service manager instance.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param compConfig
	 *            the comp config
	 * @return the service manager instance
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static HadoopServiceManager getServiceManagerInstance(
			ClusterConfig clusterConfig, ComponentConfig compConfig)
			throws ClassNotFoundException {
		return (HadoopServiceManager) HadoopUtils.getHadoopClassInstance(
				clusterConfig, compConfig,
				HadoopConstants.ConfigXmlKeys.ClassType.SERVICE_MANAGER);
	}

	public static HadoopServiceManager getServiceManagerInstance(
			ClusterConfig clusterConfig, ComponentConfig compConfig,
			boolean saveLogsInDb) throws ClassNotFoundException {
		return (HadoopServiceManager) HadoopUtils.getHadoopClassInstance(
				clusterConfig, compConfig,
				HadoopConstants.ConfigXmlKeys.ClassType.SERVICE_MANAGER);
	}

	/**
	 * Gets the configurator instance.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param compConfig
	 *            the comp config
	 * @return the configurator instance
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static HadoopConfigurator getConfiguratorInstance(
			ClusterConfig clusterConfig, ComponentConfig compConfig)
			throws ClassNotFoundException {
		return (HadoopConfigurator) HadoopUtils.getHadoopClassInstance(
				clusterConfig, compConfig,
				HadoopConstants.ConfigXmlKeys.ClassType.CONFIGURATOR);
	}

	/**
	 * Gets the monitor instance.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param compConfig
	 *            the comp config
	 * @return the monitor instance
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static HadoopMonitor getMonitorInstance(ClusterConfig clusterConfig,
			ComponentConfig compConfig) throws ClassNotFoundException {
		return (HadoopMonitor) HadoopUtils.getHadoopClassInstance(
				clusterConfig, compConfig,
				HadoopConstants.ConfigXmlKeys.ClassType.MONITOR, false);
	}

	/**
	 * Gets the hadoop class instance.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param compConfig
	 *            the comp config
	 * @param classType
	 *            the class type
	 * @return the hadoop class instance
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static Object getHadoopClassInstance(ClusterConfig clusterConfig,
			ComponentConfig compConfig, String classType)
			throws ClassNotFoundException {

		CmpConfigMappingSet configXmlSet = AppStoreWrapper
				.getCmpConfigMapping(Constant.Component.Name.HADOOP);

		String className = configXmlSet.get(compConfig, false).getClassName(
				classType);
		ComponentConfigContext hadoopContextObject = (ComponentConfigContext) ReflectionUtil
				.getObject(className);
		hadoopContextObject.setClusterConfig(clusterConfig);
		hadoopContextObject.setComponentConfig(compConfig);
		hadoopContextObject.setLOG(Class.forName(className));
		return hadoopContextObject;
	}

	public static Object getHadoopClassInstance(ClusterConfig clusterConfig,
			ComponentConfig compConfig, String classType, boolean saveLogsInDb)
			throws ClassNotFoundException {

		CmpConfigMappingSet configXmlSet = AppStoreWrapper
				.getCmpConfigMapping(Constant.Component.Name.HADOOP);

		String className = configXmlSet.get(compConfig, false).getClassName(
				classType);
		ComponentConfigContext hadoopContextObject = (ComponentConfigContext) HadoopUtils
				.getHadoopClassInstance(clusterConfig, compConfig, classType);
		if (!saveLogsInDb) {
			AnkushLogger logger = new AnkushLogger(Class.forName(className));
			logger.setClusterName(clusterConfig.getName());
			hadoopContextObject.setLOG(logger);
		}

		return hadoopContextObject;
	}

	public static String convertMbIntoGb(long megaBytes) {
		String convertedVal = "0";
		if (megaBytes == 0) {
			return convertedVal;
		}

		final double HUNDRED = 100.0;
		final long DIVEDEBY = 1024L;

		DecimalFormat df = new DecimalFormat("0.00");

		if (megaBytes / DIVEDEBY > 0) {
			return (df.format(((megaBytes * HUNDRED / DIVEDEBY)) / HUNDRED) + "GB");
		} else {
			return (String.valueOf(megaBytes) + "MB");
		}
	}

	/**
	 * Gets the input argument property value.
	 * 
	 * @param inputArgumentsList
	 *            the input arguments list
	 * @param propertyName
	 *            the property name
	 * @return the input argument property value
	 * @throws Exception
	 *             the exception
	 */
	private static String getInputArgumentPropertyValue(
			List<String> inputArgumentsList, String propertyName)
			throws Exception {

		String propName = "-D" + propertyName + "=";
		for (String inputArgument : inputArgumentsList) {
			if (inputArgument.startsWith(propName)) {
				return (inputArgument.split("="))[1];
			}
		}
		return null;
	}

	/**
	 * Checks if is hadoop2 config.
	 * 
	 * @param hadoopConfig
	 *            the hadoop config
	 * @return true, if is hadoop2 config
	 */
	public static boolean isHadoop2Config(ComponentConfig hadoopConfig) {
		return (hadoopConfig.getVersion().startsWith("2"));
	}

	public static boolean isNameNode(ComponentConfig hadoopConfig, String host) {
		if (HadoopUtils.isHdfsHaEnabled(hadoopConfig)) {
			return HadoopUtils.getHaNameNodeHosts(hadoopConfig).contains(host);
		}
		return HadoopUtils.getNameNodeHost(hadoopConfig).equals(host);
	}

	public static boolean isSecondaryNameNode(ComponentConfig hadoopConfig,
			String host) {
		return HadoopUtils.getSecondaryNameNodeHost(hadoopConfig).equals(host);
	}

	public static boolean isJobTrackerNode(ComponentConfig hadoopConfig,
			String host) {
		return HadoopUtils.getJobTrackerHost(hadoopConfig).equals(host);
	}

	public static boolean isResourceManagerNode(ComponentConfig hadoopConfig,
			String host) {
		return HadoopUtils.getResourceManagerHost(hadoopConfig).equals(host);
	}

	public static boolean isJournalNode(ComponentConfig hadoopConfig,
			String host) {
		return HadoopUtils.getJournalNodeHosts(hadoopConfig).contains(host);
	}

	public static boolean isJobHistoryServerNode(ComponentConfig hadoopConfig,
			String host) {
		return HadoopUtils.getJobHistoryServerHost(hadoopConfig).equals(host);
	}

	public static boolean isWebAppProxyServerNode(ComponentConfig hadoopConfig,
			String host) {
		return HadoopUtils.getWebAppProxyServerHost(hadoopConfig).equals(host);
	}

	public static Set<String> getSlavesListFromJmx(
			Map<String, Object> beanObjNameNodeInfo)
			throws RegisterClusterException {
		try {
			List<String> nodeTypeList = new ArrayList<String>();
			nodeTypeList.add(HadoopConstants.JmxBeanKeys.DfsData.Nodes.LIVE);
			nodeTypeList.add(HadoopConstants.JmxBeanKeys.DfsData.Nodes.DEAD);
			nodeTypeList
					.add(HadoopConstants.JmxBeanKeys.DfsData.Nodes.DECOMMISION);

			Set<String> slavesList = new HashSet<String>();
			for (String nodeType : nodeTypeList) {
				slavesList.addAll(getHostsFromJsonString(
						String.valueOf(beanObjNameNodeInfo.get(nodeType)),
						nodeType));
			}
			return slavesList;
		} catch (RegisterClusterException e) {
			throw e;
		} catch (Exception e) {
			throw new RegisterClusterException(
					"Could not fetch slaves information from NameNodeInfo bean.");
		}
	}

	private static Set<String> getHostsFromJsonString(String nodesInfoJson,
			String nodeType) throws RegisterClusterException {
		JSONObject jsonNodesInfo = JsonMapperUtil.objectFromString(
				nodesInfoJson, JSONObject.class);
		if (jsonNodesInfo != null) {
			return jsonNodesInfo.keySet();
		}
		throw new RegisterClusterException("Could not fetch " + nodeType
				+ " nodes information from NameNodeInfo bean.");
	}

	/**
	 * The Class RolePidFileMap.
	 */
	public static class RolePidFilePathMap {

		/** The dir pid hdfs. */
		static String DIR_PID_HDFS = "/var/run/hadoop-hdfs/";

		/** The prefix pid hdfs. */
		static String PREFIX_PID_HDFS = "hadoop-hdfs-";

		/** The dir pid mapreduce. */
		static String DIR_PID_MAPREDUCE = "/var/run/hadoop-mapreduce/";

		/** The prefix pid mapreduce. */
		static String PREFIX_PID_MAPREDUCE = "mapreduce-mapreduce-";

		/** The DI r_ pi d_ m rv1. */
		static String DIR_PID_MRv1 = "/var/run/hadoop-0.20-mapreduce/";

		/** The prefix pid mrv1. */
		static String PREFIX_PID_MRv1 = "hadoop-hadoop-";

		/** The dir pid yarn. */
		static String DIR_PID_YARN = "/var/run/hadoop-yarn/";

		/** The prefix pid yarn. */
		static String PREFIX_PID_YARN = "yarn-yarn-";

		/** The pid extn. */
		static String PID_EXTN = ".pid";

		/** The process pid file map. */
		static Map<String, String> ROLE_PID_FILE_PATH_MAP = new HashMap<String, String>();

		static {
			Map<String, String> roleFilePathMap = new HashMap<String, String>();

			roleFilePathMap.put(HadoopConstants.Roles.NAMENODE,
					RolePidFilePathMap.DIR_PID_HDFS + PREFIX_PID_HDFS
							+ HadoopConstants.Roles.NAMENODE.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(HadoopConstants.Roles.DATANODE,
					RolePidFilePathMap.DIR_PID_HDFS + PREFIX_PID_HDFS
							+ HadoopConstants.Roles.DATANODE.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(
					HadoopConstants.Roles.SECONDARYNAMENODE,
					RolePidFilePathMap.DIR_PID_HDFS
							+ PREFIX_PID_HDFS
							+ HadoopConstants.Roles.SECONDARYNAMENODE
									.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(
					HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
					RolePidFilePathMap.DIR_PID_HDFS
							+ PREFIX_PID_HDFS
							+ HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER
									.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(
					HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
					RolePidFilePathMap.DIR_PID_HDFS
							+ PREFIX_PID_HDFS
							+ HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER
									.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(HadoopConstants.Roles.JOBTRACKER,
					RolePidFilePathMap.DIR_PID_MRv1 + PREFIX_PID_MRv1
							+ HadoopConstants.Roles.JOBTRACKER.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(HadoopConstants.Roles.TASKTRACKER,
					RolePidFilePathMap.DIR_PID_MRv1 + PREFIX_PID_MRv1
							+ HadoopConstants.Roles.TASKTRACKER.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(HadoopConstants.Roles.JOBHISTORYSERVER,
					RolePidFilePathMap.DIR_PID_MAPREDUCE + PREFIX_PID_MAPREDUCE
							+ "historyserver" + RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(
					HadoopConstants.Roles.RESOURCEMANAGER,
					RolePidFilePathMap.DIR_PID_YARN
							+ PREFIX_PID_YARN
							+ HadoopConstants.Roles.RESOURCEMANAGER
									.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(HadoopConstants.Roles.NODEMANAGER,
					RolePidFilePathMap.DIR_PID_YARN + PREFIX_PID_YARN
							+ HadoopConstants.Roles.NODEMANAGER.toLowerCase()
							+ RolePidFilePathMap.PID_EXTN);

			roleFilePathMap.put(HadoopConstants.Roles.WEBAPPPROXYSERVER,
					RolePidFilePathMap.DIR_PID_YARN + PREFIX_PID_YARN
							+ "proxyserver" + RolePidFilePathMap.PID_EXTN);

			ROLE_PID_FILE_PATH_MAP = roleFilePathMap;
		}

		/**
		 * Gets the process pid file path.
		 * 
		 * @param roleName
		 *            the role name
		 * @return the process pid file path
		 */
		public static String getRolePidFilePath(String roleName) {
			return ROLE_PID_FILE_PATH_MAP.get(roleName);
		}
	}

	/**
	 * The Enum Vendor.
	 */
	public enum Vendor {

		/** The Apache. */
		Apache,
		/** The Cloudera. */
		Cloudera,
		/** The hdp. */
		HDP
	}

	/**
	 * The Enum InstallationType.
	 */
	public enum InstallationType {

		/** The Bundle. */
		Bundle,
		/** The Package. */
		Package
	}

	/**
	 * Implementation to get the Java Process Name for a given role.
	 * 
	 * @author Akhil
	 * 
	 */
	public static class RoleCommandName {

		/** The role command map. */
		static Map<String, String> ROLE_COMMAND_MAP = new HashMap<String, String>();

		static {
			Map<String, String> roleCommandMap = new HashMap<String, String>();

			// Common Roles
			roleCommandMap.put(HadoopConstants.Roles.NAMENODE, "namenode");
			roleCommandMap.put(HadoopConstants.Roles.DATANODE, "datanode");
			roleCommandMap.put(HadoopConstants.Roles.SECONDARYNAMENODE,
					"secondarynamenode");

			// Roles for Hadoop 0.x & 1.x
			roleCommandMap.put(HadoopConstants.Roles.JOBTRACKER, "jobtracker");
			roleCommandMap
					.put(HadoopConstants.Roles.TASKTRACKER, "tasktracker");

			// Roles for Hadoop 2.x
			roleCommandMap.put(HadoopConstants.Roles.RESOURCEMANAGER,
					"resourcemanager");
			roleCommandMap.put(HadoopConstants.Roles.WEBAPPPROXYSERVER,
					"proxyserver");

			roleCommandMap
					.put(HadoopConstants.Roles.NODEMANAGER, "nodemanager");
			roleCommandMap
					.put(HadoopConstants.Roles.JOURNALNODE, "journalnode");
			roleCommandMap.put(HadoopConstants.Roles.JOBHISTORYSERVER,
					"historyserver");
			roleCommandMap.put(HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
					"zkfc");

			ROLE_COMMAND_MAP = roleCommandMap;
		}

		/**
		 * To get the comma separated services for the given role.
		 * 
		 * @param role
		 *            the role
		 * @return comma separated services.
		 */
		public static String getCommandName(String role) {
			return ROLE_COMMAND_MAP.get(role);
		}
	}

	/**
	 * The Class RolePriorityMap.
	 */
	public static class RolePriorityMap {

		/** The Constant rolePriorityMap. */
		static final Map<String, Integer> rolePriorityMap = new HashMap<String, Integer>() {
			{
				put(HadoopConstants.Roles.JOURNALNODE, 1);
				put(HadoopConstants.Roles.NAMENODE, 2);
				put(HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER, 3);
				put(HadoopConstants.Roles.SECONDARYNAMENODE, 4);
				put(HadoopConstants.Roles.DATANODE, 5);
				put(HadoopConstants.Roles.JOBTRACKER, 6);
				put(HadoopConstants.Roles.RESOURCEMANAGER, 6);
				put(HadoopConstants.Roles.TASKTRACKER, 7);
				put(HadoopConstants.Roles.NODEMANAGER, 7);
				put(HadoopConstants.Roles.WEBAPPPROXYSERVER, 8);
				put(HadoopConstants.Roles.JOBHISTORYSERVER, 9);
			}
		};

		/**
		 * Gets the role priority.
		 * 
		 * @param role
		 *            the role
		 * @return the role priority
		 */
		public static int getRolePriority(String role) {
			Integer priority = rolePriorityMap.get(role);
			if (priority != null) {
				return priority.intValue();
			}
			return 0;
		}
	}

	/**
	 * The Class RoleProcessName.
	 */
	public static class RoleProcessName {

		/** The role java process map. */
		static Map<String, String> ROLE_PROCESS_MAP = new HashMap<String, String>();

		static {
			Map<String, String> roleProcessMap = new HashMap<String, String>();
			// Common Roles
			roleProcessMap.put(HadoopConstants.Roles.NAMENODE, "NameNode");
			roleProcessMap.put(HadoopConstants.Roles.DATANODE, "DataNode");
			roleProcessMap.put(HadoopConstants.Roles.SECONDARYNAMENODE,
					"SecondaryNameNode");

			// Roles for Hadoop 0.x & 1.x
			roleProcessMap.put(HadoopConstants.Roles.JOBTRACKER, "JobTracker");
			roleProcessMap
					.put(HadoopConstants.Roles.TASKTRACKER, "TaskTracker");

			// Roles for Hadoop 2.x
			roleProcessMap.put(HadoopConstants.Roles.RESOURCEMANAGER,
					"ResourceManager");
			roleProcessMap.put(HadoopConstants.Roles.WEBAPPPROXYSERVER,
					"WebAppProxyServer");
			roleProcessMap
					.put(HadoopConstants.Roles.NODEMANAGER, "NodeManager");
			roleProcessMap
					.put(HadoopConstants.Roles.JOURNALNODE, "JournalNode");
			roleProcessMap.put(HadoopConstants.Roles.JOBHISTORYSERVER,
					"JobHistoryServer");
			roleProcessMap.put(HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
					"DFSZKFailoverController");

			ROLE_PROCESS_MAP = roleProcessMap;
		}

		/**
		 * To get the comma separated services for the given role.
		 * 
		 * @param role
		 *            the role
		 * @return comma separated services.
		 */
		public static String getProcessName(String role) {
			return ROLE_PROCESS_MAP.get(role);
		}

		/**
		 * Gets the role name.
		 * 
		 * @param javaProcess
		 *            the java process
		 * @return the role name
		 */
		public static String getRoleName(String javaProcess) {
			for (String role : ROLE_PROCESS_MAP.keySet()) {
				String process = ROLE_PROCESS_MAP.get(role);
				if (process.equalsIgnoreCase(javaProcess)) {
					return role;
				}
			}
			return "";
		}
	}
}
