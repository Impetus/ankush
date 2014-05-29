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
package com.impetus.ankush.hadoop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;

import org.apache.commons.lang.StringUtils;
import org.aspectj.util.FileUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentDeployer;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.SyncFolder;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.AnkushRestClient;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.XmlUtil;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.config.Parameter;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Deployer;


// TODO: Auto-generated Javadoc
/**
 * The Class HadoopUtils.
 * 
 * @author Akhil
 */
public class HadoopUtils {

	/** The log. */
	private static AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.HADOOP, HadoopUtils.class);
	
	/** The monitoring manager. */
	private static GenericManager<NodeMonitoring, Long> monitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);
	
	/** The node manager. */
	private static GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);
	
	/** The conf manager. */
	private final static ConfigurationManager confManager = new ConfigurationManager();

	/** The Constant TOPOLOGY_DATA_FILE. */
	public static final String TOPOLOGY_DATA_FILE = AppStoreWrapper
			.getAnkushConfReader().getStringValue("hadoop.conf.topology.data");

	/** The Constant KEY_HADOOP_HOME. */
	public static final String KEY_HADOOP_HOME = "HADOOP_HOME";
	
	/** The Constant KEY_HADOOP_CONF_DIR. */
	public static final String KEY_HADOOP_CONF_DIR = "HADOOP_CONF_DIR";
	
	/** The Constant TOPOLOGY_SCRIPT_FILE. */
	private static final String TOPOLOGY_SCRIPT_FILE = AppStoreWrapper
			.getAnkushConfReader()
			.getStringValue("hadoop.conf.topology.script");

	/** The Constant TOPOLOGY_SCRIPT_FILE_RELPATH_RESOURCES. */
	private static final String TOPOLOGY_SCRIPT_FILE_RELPATH_RESOURCES = AppStoreWrapper
			.getAnkushConfReader().getStringValue(
					"hadoop.conf.topology.script.relpath");

	/** The Constant HADOOP_JAR_PATH_LOCATION. */
	private static final String HADOOP_JAR_PATH_LOCATION = ".ankush/agent/libs/compdep/";

	/** The Constant MAPREDPORT. */
	private static final String MAPREDPORT = "9001";
	
	/** The Constant HADOOP_URI_PREFIX. */
	public static final String HADOOP_URI_PREFIX = "hdfs://";
	
	/** The Constant VERSION_CDH3. */
	public static final String VERSION_CDH3 = "0.20.2-cdh3u1";

	/** The Constant DEFAULT_NODE_IP. */
	public static final String DEFAULT_NODE_IP = "0.0.0.0";
	
	/** The Constant JMX_BEAN_NAME_JAVA_RUNTIME. */
	public static final String JMX_BEAN_NAME_JAVA_RUNTIME = "java.lang:type=Runtime";

	/**
	 * Sync hadoop conf folder.
	 *
	 * @param nodeConf the node conf
	 * @param conf the conf
	 * @return true, if successful
	 */
	public static boolean syncHadoopConfFolder(NodeConf nodeConf, HadoopConf conf) {
		SSHExec connection = null;
		try {
			LOG.info(nodeConf.getPublicIp(), "Sync hadoop configuration folder.");
			
			String nodePublicIp = conf.getNamenode().getPublicIp();
			if(conf instanceof Hadoop2Conf) {
				Hadoop2Conf hadoop2Conf = (Hadoop2Conf) conf;
				if(hadoop2Conf.isHaEnabled()) {
					if(hadoop2Conf.getActiveNamenode() != null) {
						nodePublicIp = hadoop2Conf.getActiveNamenode().getPublicIp();
					} else if(hadoop2Conf.getStandByNamenode() != null) {
						nodePublicIp = hadoop2Conf.getStandByNamenode().getPublicIp();
					} else {
						LOG.error(nodeConf, "Could not sync hadoop configuration directory.");
						LOG.debug("Both Active & StandBy NameNodes are down.");
						return false;		
					}
				} else {
					nodePublicIp = hadoop2Conf.getNamenode().getPublicIp();
				}
			}
			// connect to cluster NameNode using Public IP
			connection = SSHUtils.connectToNode(nodePublicIp, conf.getUsername(), 
							conf.getPassword(), conf.getPrivateKey());

			// Use Private IP to sync folder
			AnkushTask syncConfFolder = new SyncFolder(nodeConf.getPrivateIp(), conf.getHadoopConfDir());
			Result res = connection.exec(syncConfFolder);
			if(res.rc != 0) {
				LOG.error(nodeConf, "Could not sync hadoop configuration directory.");
				return false;
			}
			
		} catch (Exception e) {
			LOG.error(nodeConf, "Could not sync hadoop configuration directory.");
			LOG.debug(e.getMessage());
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}
	
	/**
	 * Sets the hadoop metrics.
	 * 
	 * @param connection
	 *            the connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 */
	public static void setHadoopMetrics(SSHExec connection, NodeConf nodeConf,
			HadoopConf conf) {
		String nodeIp = nodeConf.getPublicIp();
		String hadoopVersion = conf.getComponentVersion();
		String hadoopConfPath = conf.getHadoopConfDir();

		try {
			LOG.info(nodeConf.getPublicIp(),
					"Setting hadoop metrics properties...");
			String metricsFilePath = null;
			String metricsFileName = Constant.Hadoop.FileName.METRICS_PROPERTIES_FILE;
			String contextClass = "org.apache.hadoop.metrics.ganglia.GangliaContext31";
			String classProperty = "class";
			String periodProperty = "period";
			String serversProperty = "servers";

			List<String> metricLists = new ArrayList<String>();
			metricLists.add("dfs");
			metricLists.add("mapred");
			metricLists.add("jvm");
			metricLists.add("rpc");
			metricLists.add("ugi");
			metricLists.add("hbase");

			/*
			 * List<String> metricLists = new ArrayList<String>( (List<String>)
			 * configValues.get("hadoopMetrics"));
			 */
			if (hadoopVersion.substring(0, 1).equals("1")) {
				metricsFileName = Constant.Hadoop.FileName.METRICS2_PROPERTIES_FILE;
				contextClass = "org.apache.hadoop.metrics2.sink.ganglia.GangliaSink31";
				classProperty = "sink.ganglia.class";
				periodProperty = "sink.ganglia.period";
				serversProperty = "sink.ganglia.servers";
				metricLists = new ArrayList<String>();
				metricLists.add("namenode");
				metricLists.add("datanode");
				metricLists.add("jobtracker");
				metricLists.add("tasktracker");
				metricLists.add("maptask");
				metricLists.add("reducetask");
			} else if (hadoopVersion.substring(0, 1).equals("2")) {
				metricsFileName = Constant.Hadoop.FileName.METRICS2_PROPERTIES_FILE;
				contextClass = "org.apache.hadoop.metrics2.sink.ganglia.GangliaSink31";
				classProperty = "sink.ganglia.class";
				periodProperty = "sink.ganglia.period";
				serversProperty = "sink.ganglia.servers";
				metricLists = new ArrayList<String>();
				metricLists.add("namenode");
				metricLists.add("datanode");
				metricLists.add("resourcemanager");
				metricLists.add("nodemanager");
			}

			LOG.info(nodeConf.getPublicIp(), "Configuring Hadoop metrics...");

			metricsFilePath = hadoopConfPath + metricsFileName;

			String tmpMetricsFileName = "/tmp/" + FileUtils.getRandomFileName()
					+ ".properties";

			Properties props;

			try {
				props = new Properties();
				String serversAddress = conf.getClusterConf().getGangliaMaster().getPrivateIp()
						+ ":"
						+ AppStoreWrapper.getAnkushConfReader().getIntValue(
								"ganglia.port");

				// TODO Compitibility checking needed to use GangliaContext31
				// class from version > 3.0.7
				// if(VersionChecking) // contextClass =
				// "org.apache.hadoop.metrics.ganglia.GangliaContext31";
				if (!metricLists.isEmpty()) {
					for (String metric : metricLists) {
						props.setProperty(metric + "." + classProperty,
								contextClass);

						// Saving class property.
						confManager.saveConfiguration(conf.getClusterDbId(),
								conf.getCurrentUser(), metricsFileName,
								nodeConf.getPublicIp(), metric + "."
										+ classProperty, contextClass);

						props.setProperty(metric + "." + periodProperty, "10");

						// Saving periodProperty property.
						confManager.saveConfiguration(conf.getClusterDbId(),
								conf.getCurrentUser(), metricsFileName,
								nodeConf.getPublicIp(), metric + "."
										+ periodProperty, "10");

						props.setProperty(metric + "." + serversProperty,
								serversAddress);

						// Saving serversProperty property.
						confManager.saveConfiguration(conf.getClusterDbId(),
								conf.getCurrentUser(), metricsFileName,
								nodeConf.getPublicIp(), metric + "."
										+ serversProperty, serversAddress);
					}
				}

				CommonUtil.storeProperties(tmpMetricsFileName, props);
			} catch (FileNotFoundException e) {
				LOG.error(nodeIp, "Configuring Hadoop metrics failed...", e);
			} catch (IOException e) {
				LOG.error(nodeIp, "Configuring Hadoop metrics failed...", e);
			}
			try {
				String str = FileUtil
						.readAsString(new File(tmpMetricsFileName)).replace(
								"\\:", ":");
				if (connection != null) {
					String command = "echo \"" + str + "\" > "
							+ metricsFilePath;
					LOG.debug("Hadoop Metrics command is : " + command);
					boolean res = SSHUtils.action(connection, command);
					if (!res) {
						LOG.error(nodeIp,
								"Failed to set hadoop ganglia metrics");
					}
				}
			} catch (IOException e) {
				System.out.println(e);
			}

		} catch (Exception e) {
		}
		LOG.info(nodeConf.getPublicIp(),
				"Configuring Hadoop metrics completed.");
	}

	/**
	 * Validate node addition to set.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param node
	 *            the node
	 * @return true, if successful
	 */
	public static boolean validateNodeAdditionToSet(Set<NodeConf> nodeConfs,
			NodeConf node) {
		if (node == null) {
			return false;
		}
		for (NodeConf tmpNode : nodeConfs) {
			if (tmpNode.getPublicIp().equals(node.getPublicIp())) {
				return false;
			} 
			if (tmpNode.getPrivateIp().equals(node.getPrivateIp())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds the hadoop agent info.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 * @return true, if successful
	 */
	public static boolean addHadoopAgentInfo(NodeConf nodeConf,
			HadoopConf conf, SSHExec connection) {
		try {
			String userHome = CommonUtil.getUserHome(conf.getUsername());
			String agentFile = AgentDeployer.AGENT_PROPERTY_FILE_PATH;
			String dfsPort = Hadoop1Deployer.DEFAULT_PORT_RPC_NAMENODE;

			String lineSeperator = Constant.LINE_SEPERATOR;
			int hadoopDataUpdateTime = 30;
			
			StringBuffer agentConfProp = new StringBuffer();
			agentConfProp.append("HADOOP_DATA_UPDATE_TIME=")
					.append(hadoopDataUpdateTime).append(lineSeperator);
			agentConfProp.append("URL_JOB_STATUS=job").append(lineSeperator);
			agentConfProp.append("NAMENODE_IP=")
					.append(conf.getNamenode().getPrivateIp()).append(lineSeperator);
			agentConfProp.append("NAMENODE_PORT=").append(MAPREDPORT)
					.append(lineSeperator);
			agentConfProp.append("NAMENODE_HTTP_PORT=").append(Hadoop1Deployer.DEFAULT_PORT_HTTP_NAMENODE)
					.append(lineSeperator);
			
			// if instance of hadoop2 conf then change dfs port and add
			// resourcemanager host & port.
			if (conf instanceof Hadoop2Conf) {
				dfsPort = Hadoop2Deployer.DEFAULT_PORT_RPC_NAMENODE;
				agentConfProp.append("RESOURCEMANAGER_IP=")
					.append(((Hadoop2Conf) conf).getResourceManagerNode().getPrivateIp())
					.append(lineSeperator);
				agentConfProp.append("RESOURCEMANAGER_HTTP_PORT=")
					.append(Hadoop2Deployer.DEFAULT_PORT_HTTP_RESOURCEMANAGER).append(lineSeperator);
				if(((Hadoop2Conf) conf).isHaEnabled()) {
					agentConfProp.append("URL_NAMENODE_ROLE=")
					.append(Constant.Hadoop.NNROLE_INFO_KEY).append(lineSeperator);
				}
			}
			agentConfProp.append("DFS_PORT=").append(dfsPort)
					.append(lineSeperator);
			agentConfProp.append(HadoopUtils.KEY_HADOOP_HOME + "=")
					.append(conf.getComponentHome()).append(lineSeperator);
			agentConfProp.append("HADOOP_VENDOR=")
					.append(conf.getComponentVendor()).append(lineSeperator);
			agentConfProp.append("DATA_DIR_LIST=").append(conf.getDfsDataDir())
					.append(",").append(conf.getDfsNameDir())
					.append(lineSeperator);
			String hadoopJarPath = FileUtils
					.getSeparatorTerminatedPathEntry(userHome)
					+ HADOOP_JAR_PATH_LOCATION;
			agentConfProp.append("HADOOP_JAR_PATH=").append(hadoopJarPath)
					.append(lineSeperator);

			CustomTask task = new AppendFile(agentConfProp.toString(),
					agentFile);
			connection.exec(task);
			return true;
		} catch (TaskExecFailException e) {
			LOG.error(nodeConf, "Could not add Hadoop information in agent file.");
			return false;
		}
	}

	/**
	 * Adds the class entry.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @param className
	 *            the class name
	 * @return true, if successful
	 */
	public static boolean addClassEntry(SSHExec connection, HadoopConf conf,
			String className) {
		String userName = conf.getUsername();

		String userHome = CommonUtil.getUserHome(userName);
		String destination = userHome + Constant.Agent.AGENT_TASKABLE_FILE_PATH;
		className = Constant.LINE_SEPERATOR + className;
		Result res = null;
		try {
			// Connection established
			if (connection != null) {
				AnkushTask addEntry = new AppendFile(className, destination);
				res = connection.exec(addEntry);
				if (!res.isSuccess) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Setup rack awareness.
	 * 
	 * @param connection
	 *            the connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public static boolean setupRackAwareness(SSHExec connection,
			NodeConf nodeConf, HadoopConf conf) throws Exception {
		Result res = null;

		// Hadoop RackAwareness Script file name
		String rackAwarenessScript = conf.getHadoopConfDir()
				+ HadoopUtils.TOPOLOGY_SCRIPT_FILE;
		// Path for Hadoop core-site.xml file
		String coresite = conf.getHadoopConfDir()
				+ Constant.Hadoop.FileName.XML_CORE_SITE;

		AnkushTask rackAwarenessScriptFile = new AddConfProperty(
				conf.getPropNameTopologyScriptFile(), rackAwarenessScript,
				coresite, Constant.File_Extension.XML);
		res = connection.exec(rackAwarenessScriptFile);
		if (!res.isSuccess) {
			String errorMsg = "Could not add "
					+ conf.getPropNameTopologyScriptFile() + " to "
					+ Constant.Hadoop.FileName.XML_CORE_SITE + " file.";
			// LOG.error(nodeConf, errorMsg);
			return false;
		}
		// Saving topology.script.file.name property.
		confManager.saveConfiguration(conf.getClusterDbId(),
				conf.getCurrentUser(), Constant.Hadoop.FileName.XML_CORE_SITE,
				nodeConf.getPublicIp(), conf.getPropNameTopologyScriptFile(),
				rackAwarenessScript);

		String resourceBasePath = AppStoreWrapper.getResourcePath();
		connection.uploadSingleDataToServer(resourceBasePath
				+ HadoopUtils.TOPOLOGY_SCRIPT_FILE_RELPATH_RESOURCES,
				rackAwarenessScript);

		String topologyDataFilePath = conf.getHadoopConfDir()
				+ HadoopUtils.TOPOLOGY_DATA_FILE;

		AnkushTask createTopologyDataFile = new AppendFile(
				conf.getRackFileContent(), topologyDataFilePath);
		res = connection.exec(createTopologyDataFile);
		if (!res.isSuccess) {
			// LOG.error(nodeConf, "Could not create topology data file");
			return false;
		}
		String strCmdChangeScriptMode = "chmod a+x " + rackAwarenessScript;
		AnkushTask changeScriptPermission = new ExecSudoCommand(
				conf.getPassword(), strCmdChangeScriptMode);
		res = connection.exec(changeScriptPermission);
		if (!res.isSuccess) {
			 LOG.error(nodeConf, "Could not change permisssion of "
			 + rackAwarenessScript);
			return false;
		}

		return true;
	}

	/**
	 * Method to get name node parameters.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param hConf
	 *            the h conf
	 * @return the name node parameter confs
	 */
	public static Map<String, List<Parameter>> getNameNodeParameterConfs(
			NodeConf nodeConf, HadoopConf hConf) {
		Map<String, List<Parameter>> fileConfs = null;

		List<String> files = new ArrayList<String>();
		files.add(Constant.Hadoop.FileName.XML_CORE_SITE);
		files.add(Constant.Hadoop.FileName.XML_MAPRED_SITE);
		files.add(Constant.Hadoop.FileName.XML_HDFS_SITE);
		files.add(Constant.Hadoop.FileName.XML_HADOOP_POLICY);
		files.add(Constant.Hadoop.FileName.XML_CAPACITY_SCHEDULER);

		fileConfs = new HashMap<String, List<Parameter>>();
		for (String file : files) {
			try {
				// getting file parameters.
				List<Parameter> params = getFileParams(hConf, file);
				// if not null and not empty.
				if (params != null && !params.isEmpty()) {
					fileConfs.put(file, params);
				} else {
					LOG.error(nodeConf, "Unable to get the " + file
							+ " parameters.");
					return null;
				}
			} catch (Exception e) {
				LOG.error(nodeConf,
						"Unable to get configuration parameters from node.");
			}
		}

		return fileConfs;
	}

	/**
	 * Method to get the default parameters of hadoop.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return the hadoop parameter confs
	 */
	public static Map<String, List<Parameter>> getHadoopParameterConfs(
			NodeConf nodeConf, HadoopConf conf) {
		Map<String, List<Parameter>> fileConfs = null;
		try {
			fileConfs = new HashMap<String, List<Parameter>>();
			String dfsPort = Hadoop1Deployer.DEFAULT_PORT_RPC_NAMENODE;
			if (conf instanceof Hadoop2Conf) {
				dfsPort = Hadoop2Deployer.DEFAULT_PORT_RPC_NAMENODE;
			}
			// adding core site file parameters.
			List<Parameter> coresiteParams = new ArrayList<Parameter>();
			coresiteParams.add(new Parameter("fs.default.name", HadoopUtils.HADOOP_URI_PREFIX
					+ conf.getNamenode().getPrivateIp() + ":" + dfsPort));
			coresiteParams.add(new Parameter("hadoop.tmp.dir", conf
					.getHadoopTmpDir()));

			boolean includes3 = conf.isIncludes3();
			boolean includes3n = conf.isIncludes3n();

			if (includes3) {
				String s3AccessKey = conf.getS3AccessKey();
				String s3SecretKey = conf.getS3SecretKey();
				coresiteParams.add(new Parameter("fs.s3.awsAccessKeyId",
						s3AccessKey));
				coresiteParams.add(new Parameter("fs.s3.awsSecretAccessKey",
						s3SecretKey));
			}

			if (includes3n) {
				String s3nAccessKey = conf.getS3nAccessKey();
				String s3nSecretKey = conf.getS3nSecretKey();
				coresiteParams.add(new Parameter("fs.s3n.awsAccessKeyId",
						s3nAccessKey));
				coresiteParams.add(new Parameter("fs.s3n.awsSecretAccessKey",
						s3nSecretKey));
			}

			// adding hdfs site file parameters.
			List<Parameter> hdfssiteParams = new ArrayList<Parameter>();
			hdfssiteParams.add(new Parameter("dfs.permissions", "false"));
			hdfssiteParams.add(new Parameter("dfs.name.dir", conf
					.getDfsNameDir()));
			hdfssiteParams.add(new Parameter("dfs.data.dir", conf
					.getDfsDataDir()));
			hdfssiteParams.add(new Parameter("dfs.replication", conf
					.getDfsReplicationFactor() + ""));
			String valHttpAddressNamenode = conf.getNamenode().getPrivateIp()
					+ ":" + Hadoop1Deployer.DEFAULT_PORT_HTTP_NAMENODE;
			hdfssiteParams.add(new Parameter("dfs.http.address",
					valHttpAddressNamenode));
			String valAddrSNNIp = HadoopUtils.DEFAULT_NODE_IP;
			if (conf.getSecondaryNamenode() != null) {
				valAddrSNNIp = conf.getSecondaryNamenode().getPrivateIp();

			}
			String valHttpAddressSecNamenode = valAddrSNNIp + ":"
					+ Hadoop1Deployer.DEFAULT_PORT_HTTP_SECNAMENODE;

			hdfssiteParams.add(new Parameter("dfs.secondary.http.address",
					valHttpAddressSecNamenode));

			// adding hdfs site file parameters.
			List<Parameter> mapredsiteParams = new ArrayList<Parameter>();
			mapredsiteParams.add(new Parameter("mapred.job.tracker", conf
					.getNamenode().getPrivateIp() + ":" + MAPREDPORT));
			boolean jobTrackerRecovery = conf.isJobTrackerRecovery();
			if (jobTrackerRecovery) {
				mapredsiteParams.add(new Parameter(
						"mapred.jobtracker.restart.recover", "true"));
			}

			// adding params with file.
			fileConfs.put(Constant.Hadoop.FileName.XML_CORE_SITE,
					coresiteParams);
			fileConfs.put(Constant.Hadoop.FileName.XML_HDFS_SITE,
					hdfssiteParams);
			fileConfs.put(Constant.Hadoop.FileName.XML_MAPRED_SITE,
					mapredsiteParams);
		} catch (Exception e) {
			LOG.error(nodeConf,
					"Unable to get configuration parameters from node.");
		}
		return fileConfs;
	}

	/**
	 * Method to add configuration parameters.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param connection
	 *            the connection
	 * @param hConf
	 *            the h conf
	 * @param fileConfs
	 *            the file confs
	 * @param type
	 *            the type
	 * @return true, if successful
	 */
	public static boolean addConfParameters(NodeConf nodeConf,
			SSHExec connection, HadoopConf hConf,
			Map<String, List<Parameter>> fileConfs, String type) {
		try {
			// iterating over the file configurations.
			for (String file : fileConfs.keySet()) {
				String errorMessage = "Could not configure " + file + " file.";
				// iterating over the parameters.
				for (Parameter parameter : fileConfs.get(file)) {
					AnkushTask addPropTask = new AddConfProperty(
							parameter.getName(), parameter.getValue(),
							hConf.getHadoopConfDir() + file, type);

					// executing the tasks.
					Result res = connection.exec(addPropTask);

					// saving configuration in file.
					confManager.saveConfiguration(hConf.getClusterDbId(),
							hConf.getCurrentUser(), file,
							nodeConf.getPublicIp(), parameter.getName(),
							parameter.getValue());
					// if executed successfully.
					if (!res.isSuccess) {
						LOG.error(nodeConf, errorMessage);
						return false;
					}
				}
			}
			return true;
		} catch (TaskExecFailException e) {
			LOG.error(nodeConf, "Could not configure the hadoop files.");
			return false;
		}
	}

	/**
	 * Gets the file params.
	 * 
	 * @param hConf
	 *            the h conf
	 * @param fileName
	 *            the file name
	 * @return the file params
	 * @throws Exception
	 *             the exception
	 */
	public static List<Parameter> getFileParams(HadoopConf hConf,
			String fileName) throws Exception {

		String hostname = hConf.getNamenode().getPublicIp();
		String username = hConf.getUsername();
		String password = hConf.getPassword();
		boolean authUsingPassword = hConf.getClusterConf().isAuthTypePassword();
		if(!authUsingPassword) {
			password = hConf.getPrivateKey();
		}
		String filePath = FileUtils.getSeparatorTerminatedPathEntry(hConf
				.getHadoopConfDir()) + fileName;

		List<Parameter> fileParams = new ArrayList<Parameter>();
		String content = SSHUtils.getFileContents(filePath, hostname, username,
				password, authUsingPassword);
		if (content != null && content.length() > 0) {
			fileParams = XmlUtil.loadXMLParameters(content);
		}
		return fileParams;
	}
	
	/**
	 * Gets the gmt from time in millis.
	 *
	 * @param val the val
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
	 * @param time the time
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
	 * @param val the val
	 * @return the string
	 */
	public static String convertMillisToTime(String val){
		long milliseconds = Long.parseLong(val); 
    	long seconds, minutes, hours;
    	seconds = milliseconds / 1000;
    	minutes = seconds / 60;
    	seconds = seconds % 60;
    	hours = minutes / 60;
    	minutes = minutes % 60;
    	if(hours > 0) {
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
	 * @param appId the app id
	 * @return the job id from app id
	 */
	public static String getJobIdFromAppId(String appId) {
		return appId.replaceAll("application", "job");
	}
	
	/**
	 * Setup passwordless ssh.
	 * 
	 * @param conf
	 *            the Hadoop conf
	 * @param sourceNode
	 *            the sourceNode
	 * @param destinationList
	 *            the destination List
	 * @return true, if successful
	 */
	public static boolean setupPasswordlessSSH(GenericConfiguration conf, NodeConf sourceNode,
			Set<NodeConf> destinationList) {
		boolean passwordlessStatus = false;
		try {
			passwordlessStatus = SSHUtils.setupPaswwordlessSSH(sourceNode,
					destinationList, conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return passwordlessStatus;
	}
	
	// Used during Deployment of HBase -- To check if the passwordless ssh is already configured from the node specified
	/**
	 * Checks if is passwordless ssh already configured.
	 *
	 * @param hConf the h conf
	 * @param nodeConf the node conf
	 * @return true, if is passwordless ssh already configured
	 */
	public static boolean isPasswordlessSshAlreadyConfigured(HadoopConf hConf, NodeConf nodeConf) {
		if(nodeConf.equals(hConf.getNamenode())) {
			return true;
		}
		if(hConf instanceof Hadoop2Conf) {
			Hadoop2Conf h2Conf = (Hadoop2Conf) hConf;  
			if(nodeConf.equals(h2Conf.getResourceManagerNode())) {
				return true;	
			}
			if(h2Conf.isHaEnabled()) {
				if(nodeConf.equals(h2Conf.getStandByNamenode())) {
					return true;	
				}	
			}
		}
		return false;
	}
	
	/**
	 * Gets the service status for node.
	 *
	 * @param nodePublicIp the node public ip
	 * @param serviceToCheck the service to check
	 * @return the service status for node
	 */
	public static boolean getServiceStatusForNode(String nodePublicIp, String serviceToCheck) {
		try {
			// Get the db node info using public IP
			Node hadoopNode = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodePublicIp);
			if (hadoopNode != null) {
				Long nodeId = hadoopNode.getId();
				// Get the db node monitoring info
				NodeMonitoring nodeMonitoring = monitoringManager
						.getByPropertyValueGuarded(Constant.Keys.NODEID, nodeId);
				if (nodeMonitoring != null) {
					Map<String, Boolean> serviceStatusMap = nodeMonitoring
							.getServiceStatus();

					String processName = Constant.RoleProcessName.getProcessName(serviceToCheck);
					if(processName == null) {
						processName = serviceToCheck;	
					}
					boolean status = serviceStatusMap.get(processName);
					
					if(!status) {
						LOG.info("Service Status for " + serviceToCheck + ", on node " + nodePublicIp + ": Down." );
					}
					return status;
				}
			}
		} catch (Exception e) {
			LOG.error("Exception: Unalble to get Service Status for " + serviceToCheck);
			LOG.error(e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * Gets the jmx bean url.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @param beanName the bean name
	 * @return the jmx bean url
	 */
	public static String getJmxBeanUrl(String nodeIp, int clientPort, String beanName) {
		return ("http://" + nodeIp + ":" + clientPort + "/jmx?qry=" + beanName);	
	}

	/**
	 * Gets the bean json for jmx data.
	 *
	 * @param urlDfsJmxBean the url dfs jmx bean
	 * @return the bean json for jmx data
	 */
	public static JSONObject getBeanJsonForJmxData(String urlDfsJmxBean) {
		String errMsg = "Exception: Unalble to get data using url : " + urlDfsJmxBean;
		JSONObject json = null;
		try {
			AnkushRestClient restClient = new AnkushRestClient();
			String data = restClient.getRequest(urlDfsJmxBean);
			if(data == null) {
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
	 * Gets the jmx bean data.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @param beanName the bean name
	 * @return the jmx bean data
	 * @throws Exception the exception
	 */
	public static Map<String, Object> getJmxBeanData(String nodeIp, int clientPort, String beanName) throws Exception {
		Map<String, Object> beanObject = new HashMap<String, Object>();
		String urlJmxBean = "";
		if(beanName != null && (!beanName.isEmpty())) {
			urlJmxBean = HadoopUtils.getJmxBeanUrl(nodeIp, clientPort, beanName);
		} else {
			throw (new Exception("Unable to fetch jmx information using url: " + urlJmxBean));
		}
		
		JSONObject jmxDataJson =  HadoopUtils.getBeanJsonForJmxData(urlJmxBean);
		if(jmxDataJson != null) {
			List<JSONObject> beanObjList = (List<JSONObject>) jmxDataJson.get(Constant.Hadoop.Keys.JMX_DATA_KEY_BEANS);
			if(beanObjList.size() == 1) {
				beanObject = (Map<String, Object>) beanObjList.get(0);
			} else {
				throw (new Exception("Unable to fetch jmx information using url: " + urlJmxBean));
			}
			
		}
		return beanObject;
	}
	
	/**
	 * Gets the process start time.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @return the process start time
	 * @throws Exception the exception
	 */
	public static String getProcessStartTime(String nodeIp, int clientPort) throws Exception {
		Map<String, Object> beanJavaRuntime = new HashMap<String, Object>();
		String beanName =  HadoopUtils.JMX_BEAN_NAME_JAVA_RUNTIME;
		beanJavaRuntime = HadoopUtils.getJmxBeanData(nodeIp, clientPort, beanName);
		long startTime = ((Number)beanJavaRuntime.get(Constant.Hadoop.Keys.NameNodeJmxInfo.STARTTIME)).longValue();
		return HadoopUtils.getGmtFromTimeInMillis(startTime);
	}
	
	/**
	 * Gets the display name for start time.
	 *
	 * @param role the role
	 * @return the display name for start time
	 */
	public static String getDisplayNameForStartTime(String role) {
		return role + " Started";
	}
	
	/**
	 * Are nodes equal.
	 *
	 * @param node1 the node1
	 * @param node2 the node2
	 * @return true, if successful
	 */
	public static boolean areNodesEqual(NodeConf node1, NodeConf node2) {
		if(node1 == null || node2 == null) {
			return true;
		}
		if(node1.getPublicIp().equals(node2.getPublicIp())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Starts the update process for Hadoop Configuration to Existing Nodes
	 * during Add / Remove Nodes operation.
	 * 
	 * @param hadoopConf
	 *            the hadoop Conf
	 * @param action
	 *            the Constant.SyncConfigurationAction (ADD / REMOVE)
	 * @return true, if successful
	 */
	public static boolean updateConfToNodes(final HadoopConf hadoopConf,
			final Constant.SyncConfigurationAction action) {
		Set<NodeConf> nodeConfs = hadoopConf.getExpectedNodesAfterAddOrRemove();
		
		final Semaphore semaphorePostOperation = new Semaphore(
				nodeConfs.size());
		boolean status = true;
		try {
			for (final NodeConf nodeConf : nodeConfs) {
				semaphorePostOperation.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean nodeStatus = HadoopUtils.updateConfToNode(
								nodeConf, hadoopConf);
						nodeConf.setStatus(nodeStatus);
						if (semaphorePostOperation != null) {
							semaphorePostOperation.release();
						}
					}
				});
			}
			semaphorePostOperation.acquire(nodeConfs.size());
			status = HadoopUtils.status(nodeConfs);
		//	LOG.info("Could not update configuration to nodes during " + action.toString() + " nodes operation.");
		} catch (Exception e) {
			LOG.error("Could not update configuration to nodes during " + action.toString() + " nodes operation.", e);
			return false;
		}
		return status;
	}
	
	/**
	 * Status.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @return true, if successful
	 */
	public static boolean status(Set<NodeConf> nodeConfs) {
		boolean status = true;
		for (NodeConf nodeConf : nodeConfs) {
			status = nodeConf.getStatus() && status;
		}
		return status;
	}
	
	/**
	 * Updates Hadoop Configuration To Existing Nodes during Add / Remove Nodes
	 * operation.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private static boolean updateConfToNode(NodeConf nodeConf,
			HadoopConf conf) {
		boolean status = false;
		SSHExec connection = null;
		String publicIp = nodeConf.getPublicIp();

		try {
			// connect to remote node
			LOG.debug("Connecting with node : " + publicIp);
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {
				status = HadoopUtils.updateSlavesFile(connection, nodeConf, conf);
				if (!status) {
					nodeConf.setStatus(status);
					return false;
				}

				if (conf.isRackEnabled()) {
					String topologyDataFilePath = conf.getHadoopConfDir()
							+ HadoopUtils.TOPOLOGY_DATA_FILE;
					Result res = null;
					AnkushTask clearFile = new ClearFile(topologyDataFilePath);
					res = connection.exec(clearFile);
					if (!res.isSuccess) {
						LOG.error(nodeConf,
								"Could not clear topology data contents");
						return false;
					}
					AnkushTask createTopologyDataFile = new AppendFile(
							conf.getRackFileContent(), topologyDataFilePath);
					res = connection.exec(createTopologyDataFile);
					if (!res.isSuccess) {
						nodeConf.setStatus(false);
						LOG.error(nodeConf,
								"Could not update topology data file");
						return false;
					}
				}
			}
		} catch (Exception e) {
			nodeConf.setStatus(false);
			LOG.error(nodeConf, "Could not update Hadoop Configuration files.");
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		nodeConf.setStatus(status);
		return status;
	}
	
	/**
	 * Updates Slaves files to Existing Nodes during Add / Remove Nodes
	 * operation.
	 * 
	 * @param connection
	 *            the node connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	public static boolean updateSlavesFile(SSHExec connection, NodeConf nodeConf,
			HadoopConf conf) {
		LOG.debug("Updating Hadoop slaves file");
		// configuring slaves file
		Result res = null;
		try {
			List<String> slaveList = new ArrayList<String>();
			for (NodeConf sNode : conf.getExpectedNodesAfterAddOrRemove()) {
				HadoopNodeConf hNode = (HadoopNodeConf) sNode;
				if (hNode.getDataNode()) {
					slaveList.add(sNode.getPrivateIp());
				}
			}

			String slavesFile = conf.getHadoopConfDir()
					+ Constant.Hadoop.FileName.SLAVES;// "slaves";
			AnkushTask clearFile = new ClearFile(slavesFile);
			res = connection.exec(clearFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not clear slaves file contents");
				return false;
			}
			String slavesFileContent = StringUtils.join(slaveList,
					Constant.LINE_SEPERATOR);
			AnkushTask appendSlaveFile = new AppendFile(slavesFileContent,
					slavesFile);
			res = connection.exec(appendSlaveFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not add nodes information to slaves file");
				return false;
			}

			// Saving slaveFile property.
//			confManager.saveConfiguration(conf.getClusterDbId(),
//					conf.getCurrentUser(), Constant.Hadoop.FileName.SLAVES,
//					nodeConf.getPublicIp(), "IpValues", slavesFileContent);

			return true;
		} catch (Exception e) {
			LOG.error(nodeConf.getPublicIp(), "Could not update slaves file.");
			return false;
		}
	}

	/**
	 * Update conf after add remove nodes.
	 * 
	 * @param conf
	 *            the conf
	 */
	public static void updateConfObjectAfterAddRemoveNodes(HadoopConf conf) {
		conf.getSlaves().clear();
		conf.setSecondaryNamenode(null);

		for (NodeConf objNodeConf : conf.getExpectedNodesAfterAddOrRemove()) {
			HadoopNodeConf hnc = (HadoopNodeConf) objNodeConf;
			if (hnc.getDataNode()) {
				conf.getSlaves().add(hnc);
			}
			if (hnc.getSecondaryNameNode()) {
				conf.setSecondaryNamenode(hnc);
			}
		}
	}
	
}