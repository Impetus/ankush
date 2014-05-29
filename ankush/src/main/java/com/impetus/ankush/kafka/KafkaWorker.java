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
package com.impetus.ankush.kafka;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang.StringUtils;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ComponentConfigurator;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.ganglia.GangliaConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.PrependBeforeLastLine;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class KafkaWorker.
 * 
 * @author mayur
 */
public class KafkaWorker implements Runnable {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			Constant.Component.Name.KAFKA, KafkaWorker.class);

	/** The kafka-start-script. */
	private static final String relPath_KAFKA_SERVER_START_SCRIPT = "bin/kafka-server-start.sh";

	/** The kafka server-file-path. */
	private static final String KAFKA_SERVER_FILE_PATH = "config/server.properties";

	/** The kafka server-file-path. */
	private static final String KAFKA_LOG4J_FILE_PATH = "config/log4j.properties";

	/** The conf. */
	private KafkaConf conf = null;

	/** The node conf. */
	private NodeConf nodeConf = null;

	/** The semaphore. */
	private Semaphore semaphore = null;

	/** The node id. */
	private int nodeId;

	/** The conf manager. */
	private static ConfigurationManager confManager = new ConfigurationManager();

	/** The broker id ip map. */
	private Map<String, String> brokerIdIPMap = new HashMap<String, String>();

	/** The Constant KAFKA_VERSION. */
	private static final String KAFKA_VERSION = "2.8.0-0.8.0-beta1";

	/** The Constant KAFKA. */
	private static final String KAFKA = "kafka_";
	
	/** The state change logger. */
	private String STATE_CHANGE_LOGGER = "log4j.logger.state.change.logger";

	/** The logger kafka controller. */
	private String CONTROLLER_LOGGER = "log4j.logger.kafka.controller";

	/** The kafka request logger. */
	private String REQUEST_LOGGER = "log4j.logger.kafka.request.logger";

	/** The logger network requestchannel. */
	private String REQUESTCHANNEL_LOGGER = "log4j.logger.kafka.network.RequestChannel$";

	/** The root logger. */
	private String ROOT_LOGGER = "log4j.rootLogger";

	/** The kafka logger. */
	private String KAFKA_LOGGER = "log4j.logger.kafka";

	/**
	 * Instantiates a new kafka worker.
	 * 
	 * @param semaphore
	 *            the semaphore
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @param nodeId
	 *            the node id
	 */
	public KafkaWorker(Semaphore semaphore, KafkaConf conf, NodeConf nodeConf,
			int nodeId) {
		this.semaphore = semaphore;
		this.conf = conf;
		this.nodeConf = nodeConf;
		this.nodeId = nodeId;
	}

	/**
	 * Configure jmx monitoring.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	private static boolean configureJMXMonitoring(SSHExec connection,
			KafkaConf conf, NodeConf nodeConf) {
		logger.debug("Configuring JMX Monitoring for Kafka started ... ");
		boolean status = true;
		String componentHome = conf.getComponentHome();
		String kafkaStartUp_FilePath = componentHome
				+ KafkaWorker.relPath_KAFKA_SERVER_START_SCRIPT;
		status = JmxMonitoringUtil.configureJmxPort(
				Constant.Component.ProcessName.KAFKA, connection,
				kafkaStartUp_FilePath, conf.getJmxPort(), conf.getPassword());
		if (!status) {
			logger.error(nodeConf, "Could not update " + kafkaStartUp_FilePath
					+ " file.");
			return false;
		}

		status = JmxMonitoringUtil.copyJmxTransJson(connection,
				conf.getUsername(), conf.getPassword(),
				Constant.Component.Name.KAFKA,
				Constant.Component.ProcessName.KAFKA, conf.getJmxPort(),
				nodeConf.getPrivateIp());
		if (!status) {
			logger.error(nodeConf,
					"Could not copy JmxTrans JSON file for Kafka.");
			return false;
		}
		logger.debug("Configuring JMX Monitoring for Kafka over ... ");
		return true;
	}

	/**
	 * method called by any thread to Deploy Kafka Cluster.
	 * 
	 */
	@Override
	public void run() {
		logger.setLoggerConfig(this.conf);
		logger.info(nodeConf.getPublicIp(), "Deploying Kafka...");

		SSHExec connection = null;
		Result res = null;
		boolean statusFlag = false;

		try {
			logger.info(nodeConf.getPublicIp(), "Connecting with node"
					+ nodeConf.getPublicIp());
			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					this.conf.getUsername(), this.conf.getPassword(),
					this.conf.getPrivateKey());

			// if connected
			if (connection != null) {
				logger.info(nodeConf.getPublicIp(), "Create directory - "
						+ this.conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						this.conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					logger.error(nodeConf.getPublicIp(),
							"Could not create installation directory at "
									+ this.conf.getInstallationPath());
				} else {
					logger.info(nodeConf.getPublicIp(),
							"Get and extract tarball");
					// get and extract tarball
					boolean isSuccessfull = SSHUtils.getAndExtractComponent(
							connection, this.conf, "kafka");

					// if component exists at destination
					if (isSuccessfull) {
						logger.info(nodeConf.getPublicIp(),
								"Extracting tarball is completed...");

						String componentHome = this.conf.getInstallationPath()
								+ KAFKA + this.conf.getComponentVersion();

						// setting componentHome in bean
						this.conf.setComponentHome(componentHome);

						Map<String, Object> settings = new HashMap<String, Object>();

						// set configuration parameters for kafka
						statusFlag = configureKafka(connection, settings, this.conf);
						if (statusFlag) {
							// update logs location in log4j.properties
							statusFlag = updateLog4j(connection, this.conf, nodeConf);
							// if success in updating log4j.properties
							if (statusFlag) {
								// add kafka ganglia metric reporter
								statusFlag = addGangliaMetricReporter(
										connection, this.conf, nodeConf);
								if (statusFlag) {
									// export jmxPort in kafka-server-start.sh
									// file
									statusFlag = exportJMXPort(connection,
											this.conf, nodeConf);
									if (!statusFlag) {
										logger.error(nodeConf,
												"Could not configure JMX_PORT to kafka-server-start.sh file.");
									}
								} else {
									logger.error(nodeConf,
											"Could not update kafka-ganglia-reporter properties.");
								}
							} else {
								logger.error(nodeConf,
										"Could not update log4j.properties");
							}
						} else {
							logger.error(nodeConf,
									"Could not update server.properties");
						}

						// empty properties object.
						Properties props = new Properties();
						// addign DATA_DIR_LIST property
						props.setProperty("DATA_DIR_LIST", this.conf.getLogDir());
						// add properties in agent configuration.
						AgentUtils.addProperties(connection, props);

						// setting advanced conf with conf's advancedConf.
						this.conf.setAdvancedConf(conf.getAdvancedConf());

					} else {
						logger.error(nodeConf, "Could not extract tarball");
					}
					logger.info(nodeConf.getPublicIp(),
							"Kafka Worker thread execution over...");
				}
			} else {
				logger.error(nodeConf, "Could not connect to node...");
			}
		} catch (Exception e) {
			statusFlag = false;
			logger.error("please view server logs for further information.");
			logger.info(e.getMessage());

		} finally {
			this.nodeConf.setStatus(statusFlag);
			// disconncet to node/machine
			if (connection != null) {
				connection.disconnect();
			}
			if (semaphore != null) {
				semaphore.release();
			}
		}
	}

	/**
	 * Configure kafka.
	 * 
	 * @param connection
	 *            the connection
	 * @param settings
	 *            the settings
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean configureKafka(SSHExec connection,
			Map<String, Object> settings, KafkaConf conf) {

		logger.setLoggerConfig(conf);
		boolean statusFlag = false;

		String kafkaVersion = conf.getComponentVersion();

		// set broker id in configuration
		settings.put(Constant.Kafka_Properties.BROKER_ID, this.nodeId + "");
		//
		// adding brokerId and its corresponding IP in a Map.
		conf.getBrokerIdIPMap().put(this.nodeId + "", nodeConf.getPublicIp());

		// set port in configuration,The port the socket server
		// listens on
		settings.put(Constant.Kafka_Properties.PORT, conf.getPort());

		// set log.dir in configuration
		settings.put(Constant.Kafka_Properties.LOG_DIR, conf.getLogDir());

		// zookeeper node list
		List<String> zookeeperNodeList = (List<String>) conf.getZkNodesPort()
				.get(Constant.Kafka_Keys.ZK_NODES);
		String zkConnectNodes = "";

		// zookeeper nodes are placed in a comma-seperated
		// String for setting zk.connect in configuration
		for (int i = 0; i < zookeeperNodeList.size(); i++) {
			zkConnectNodes = zkConnectNodes + zookeeperNodeList.get(i) + ":"
					+ conf.getZkNodesPort().get(Constant.Kafka_Keys.ZK_PORT);
			if (i != (zookeeperNodeList.size() - 1)) {
				zkConnectNodes += ",";
			}
		}

		// set zk.connect in configuration
		settings.put(Constant.Kafka_Properties.Zookeeper_Connect,
				zkConnectNodes);

		if (conf.getReplicationFactor() > 0) {
			settings.put(Constant.Kafka_Properties.DEFAULT_REPLICATION_FACTOR,
					conf.getReplicationFactor() + "");
		}

		settings.put(Constant.Kafka_Properties.NUM_NETWORK_THREADS,
				conf.getNumOfNetworkThreads() + "");
		settings.put(Constant.Kafka_Properties.NUM_IO_THREADS,
				conf.getNumOfIOThreads() + "");
		settings.put(Constant.Kafka_Properties.QUEUED_MAX_REQUESTS,
				conf.getQueuedMaxRequests() + "");
		settings.put(Constant.Kafka_Properties.NUM_PARTITIONS,
				conf.getNumPartitions() + "");
		settings.put(Constant.Kafka_Properties.LOG_RETENTITION_HOURS,
				conf.getLogRetentionHours() + "");
		settings.put(Constant.Kafka_Properties.LOG_RETENTITION_BYTES,
				conf.getLogRetentitionBytes() + "");
		settings.put(Constant.Kafka_Properties.LOG_CLEANUP_INTERVAL_MINS,
				conf.getLogCleanupIntervalMins() + "");
		settings.put(Constant.Kafka_Properties.LOG_FLUSH_INTERVAL_MESSAGE,
				conf.getLogFlushIntervalMessage() + "");
		settings.put(Constant.Kafka_Properties.LOG_FLUSH_SCHEDULAR_INTERVAL_MS,
				conf.getLogFlushSchedularIntervalMs() + "");
		settings.put(Constant.Kafka_Properties.LOG_FLUSH_INTERVAL_MS,
				conf.getLogFlushIntervalMs() + "");
		settings.put(Constant.Kafka_Properties.CONTROLLED_SHUTDOWN_ENABLE,
				conf.getControlledShutdownEnable() + "");
		settings.put(Constant.Kafka_Properties.CONTROLLED_SHUTDOWN_MAX_RETRIES,
				conf.getControlledShutdownMaxRetries() + "");

		String propertyFilePath = conf.getComponentHome()
				+ KAFKA_SERVER_FILE_PATH;
		logger.info(nodeConf.getPublicIp(),
				"Configuring server.properties file...");
		for (String key : settings.keySet()) {
			statusFlag = editPropertyInFile(connection, nodeConf,
					propertyFilePath, key, settings.get(key).toString(),
					Constant.File_Extension.PROPERTIES);
			if (!statusFlag) {
				break;
			}
		}
		return statusFlag;
	}

	/**
	 * this method updates the log4j.properties file to change the log dir of
	 * Kafka-logs
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 * @throws TaskExecFailException
	 *             the task exec fail exception
	 */
	private boolean updateLog4j(SSHExec connection, KafkaConf conf,
			NodeConf nodeConf) throws TaskExecFailException {
		String componentHome = conf.getComponentHome();
		String log4jFilePath = componentHome + KAFKA_LOG4J_FILE_PATH;
		final String KAFKA_APPENDER = "log4j.appender.kafkaAppender.File";
		final String STATE_CHANGE_APPENDER = "log4j.appender.stateChangeAppender.File";
		final String REQUEST_APPENDER = "log4j.appender.requestAppender.File";
		final String CONTROLLER_APPENDER = "log4j.appender.controllerAppender.File";
		final String SERVER_LOG = "logs/server.log";
		final String STATE_CHANGE_LOG = "logs/state-change.log";
		final String REQUEST_LOG = "logs/kafka-request.log";
		final String CONTROLLER_LOG = "logs/controller.log";
		Map<String, String> settings = new HashMap<String, String>();
		Map<String, Object> advancedConf = conf.getAdvancedConf();
		settings.put(KAFKA_APPENDER, componentHome + SERVER_LOG);
		settings.put(STATE_CHANGE_APPENDER, componentHome + STATE_CHANGE_LOG);
		settings.put(REQUEST_APPENDER, componentHome + REQUEST_LOG);
		settings.put(CONTROLLER_APPENDER, componentHome + CONTROLLER_LOG);
		settings.put(ROOT_LOGGER,
				getNewLoggerValue(conf, log4jFilePath, ROOT_LOGGER));
		settings.put(KAFKA_LOGGER,
				getNewLoggerValue(conf, log4jFilePath, KAFKA_LOGGER));
		settings.put(REQUESTCHANNEL_LOGGER,
				getNewLoggerValue(conf, log4jFilePath, REQUESTCHANNEL_LOGGER));
		settings.put(REQUEST_LOGGER,
				getNewLoggerValue(conf, log4jFilePath, REQUEST_LOGGER));
		settings.put(CONTROLLER_LOGGER,
				getNewLoggerValue(conf, log4jFilePath, CONTROLLER_LOGGER));
		settings.put(STATE_CHANGE_LOGGER,
				getNewLoggerValue(conf, log4jFilePath, STATE_CHANGE_LOGGER));
		Result res;
		boolean statusFlag = false;
		for (String key : settings.keySet()) {
			statusFlag = editPropertyInFile(connection, nodeConf,
					log4jFilePath, key, settings.get(key),
					Constant.File_Extension.PROPERTIES);
			if (!statusFlag) {
				break;
			}
		}
		return statusFlag;
	}
	
	/**
	 * Gets the new logger value.
	 *
	 * @param conf the conf
	 * @param log4jFilePath the log4j file path
	 * @param loggerKey the logger key
	 * @return the new logger value
	 */
	private String getNewLoggerValue(KafkaConf conf, String log4jFilePath,
			String loggerKey) {
		ClusterConf clusterConf = conf.getClusterConf();
		String loggerValue = "";
		try {
			String fileContent = SSHUtils.getFileContents(log4jFilePath,
					nodeConf.getPublicIp(), conf.getUsername(),
					clusterConf.isAuthTypePassword() ? conf.getPassword()
							: conf.getPrivateKey(), clusterConf
							.isAuthTypePassword());
			Properties properties = new Properties();

			// Converting string into Properties.
			properties.load(new StringReader(fileContent));
			loggerValue = (String) properties.get(loggerKey);
			List<String> loggerValueList = Arrays
					.asList(loggerValue.split(","));
			List<String> newLoggerValueList = new ArrayList<String>();
			for (String key : loggerValueList) {
				if (!(getLogLevelList().contains(key.trim()))) {
					newLoggerValueList.add(key);
				}
			}
			loggerValue = conf.getAdvancedConf().get(
					Constant.Kafka_Keys.LOG_LEVEL)
					+ "," + StringUtils.join(newLoggerValueList, ",");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return loggerValue;
	}

	/**
	 * Gets the log level list.
	 *
	 * @return the log level list
	 */
	private List<String> getLogLevelList() {
		List<String> logLevelList = new ArrayList<String>();
		logLevelList.add(Constant.Log.Level.ALL);
		logLevelList.add(Constant.Log.Level.DEBUG);
		logLevelList.add(Constant.Log.Level.ERROR);
		logLevelList.add(Constant.Log.Level.FATAL);
		logLevelList.add(Constant.Log.Level.INFO);
		logLevelList.add(Constant.Log.Level.OFF);
		logLevelList.add(Constant.Log.Level.TRACE);
		logLevelList.add(Constant.Log.Level.WARN);
		return logLevelList;
	}

	/**
	 * Export jmx port.
	 *
	 * @param connection the connection
	 * @param conf the conf
	 * @param nodeConf the node conf
	 * @return true, if successful
	 */
	private boolean exportJMXPort(SSHExec connection, KafkaConf conf,
			NodeConf nodeConf) {
		boolean status = false;
		String jmxPort = conf.getJmxPort();

		String jmxFilePath = conf.getComponentHome()
				+ "bin/kafka-server-start.sh";
		String texttoPrepend = "export JMX_PORT=${JMX_PORT:-" + jmxPort + "}";
		CustomTask prependBeforeLastLine = new PrependBeforeLastLine(
				texttoPrepend, jmxFilePath);
		try {
			Result rs = connection.exec(prependBeforeLastLine);
			if (rs.rc == 0 && rs.isSuccess) {
				status = true;
			}

		} catch (TaskExecFailException e) {
			logger.error(e.getMessage());
			status = false;
		}
		return status;

	}

	/**
	 * Edits the property in file.
	 * 
	 * @param connection
	 *            the connection
	 * @param nodeConf
	 *            the node conf
	 * @param filePath
	 *            the file path
	 * @param propertyKey
	 *            the property key
	 * @param propertyValue
	 *            the property value
	 * @param fileExtension
	 *            the file extension
	 * @return true, if successful
	 */
	private boolean editPropertyInFile(SSHExec connection, NodeConf nodeConf,
			String filePath, String propertyKey, String propertyValue,
			String fileExtension) {
		logger.setLoggerConfig(this.conf);
		boolean statusFlag = false;
		CustomTask editConfProperty = new EditConfProperty(propertyKey,
				propertyValue, filePath, fileExtension);
		logger.debug(nodeConf.getPublicIp() + " ," + propertyKey + " : "
				+ propertyValue);
		try {
			Result res = connection.exec(editConfProperty);
			if (res.isSuccess) {
				confManager.saveConfiguration(this.conf.getClusterDbId(),
						this.conf.getCurrentUser(), getFileName(filePath),
						nodeConf.getPublicIp(), propertyKey, propertyValue);
				statusFlag = true;
			} else {
				logger.error(nodeConf, "Could not update property - "
						+ propertyKey + "=" + propertyValue + "...");
				statusFlag = false;
			}
		} catch (TaskExecFailException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		// }
		return statusFlag;
	}

	/**
	 * Adds the property in file.
	 * 
	 * @param connection
	 *            the connection
	 * @param nodeConf
	 *            the node conf
	 * @param filePath
	 *            the file path
	 * @param propertyKey
	 *            the property key
	 * @param propertyValue
	 *            the property value
	 * @param fileExtension
	 *            the file extension
	 * @return true, if successful
	 */
	private boolean addPropertyInFile(SSHExec connection, NodeConf nodeConf,
			String filePath, String propertyKey, String propertyValue,
			String fileExtension) {
		logger.setLoggerConfig(this.conf);
		boolean statusFlag = false;
		CustomTask addConfProperty = new AddConfProperty(propertyKey,
				propertyValue, filePath, fileExtension);
		logger.debug(nodeConf.getPublicIp() + " ," + propertyKey + " : "
				+ propertyValue);
		try {
			Result res = connection.exec(addConfProperty);
			if (res.isSuccess) {
				confManager.saveConfiguration(this.conf.getClusterDbId(),
						this.conf.getCurrentUser(), getFileName(filePath),
						nodeConf.getPublicIp(), propertyKey, propertyValue);
				statusFlag = true;
			} else {
				logger.error(nodeConf, "Could not add property - "
						+ propertyKey + "=" + propertyValue + "...");
				statusFlag = false;
			}
		} catch (TaskExecFailException e) {
			logger.error(e.getMessage());
		}
		return statusFlag;
	}

	/**
	 * Gets the file name from filePath.
	 * 
	 * @param filePath
	 *            the file path
	 * @return the file name
	 */
	public static String getFileName(String filePath) {
		String[] splittedArray = filePath.split("/");
		List<String> arrayList = Arrays.asList(splittedArray);
		return arrayList.get(arrayList.size() - 1);

	}

	/**
	 * this method upload the required jars and add the kafka-ganglia metrics
	 * reporter properties in server.properties file.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean addGangliaMetricReporter(SSHExec connection,
			KafkaConf conf, NodeConf nodeConf) throws Exception {
		String libsFolderPath = conf.getComponentHome() + "/libs";
		String basePath = AppStoreWrapper.getResourcePath();
		String localKafkaPath = basePath + "Kafka";
		String kafkaGangliaJarPath = localKafkaPath
				+ "/kafka-ganglia-1.0.0.jar";
		String metricsGangliaJarPath = localKafkaPath
				+ "/metrics-ganglia-2.2.0.jar";
		boolean statusFlag = false;
		try {
			connection.uploadSingleDataToServer(kafkaGangliaJarPath,
					libsFolderPath);
			connection.uploadSingleDataToServer(metricsGangliaJarPath,
					libsFolderPath);
		} catch (Exception e) {
			return statusFlag;
		}

		// add ganglia reporter properties in server.properties
		statusFlag = addGangliaReporterProp(connection, conf, nodeConf);
		return statusFlag;
	}

	/**
	 * Adds the ganglia reporter prop.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 * @throws TaskExecFailException
	 *             the task exec fail exception
	 */
	private boolean addGangliaReporterProp(SSHExec connection, KafkaConf conf,
			NodeConf nodeConf) throws TaskExecFailException {
		NodeConf gangliaMasterNode = conf.getClusterConf().getGangliaMaster();
		GangliaConf gangliaConf = ComponentConfigurator.getGangliaConf(conf
				.getClusterConf());
		boolean statusFlag = false;
		if (gangliaConf == null && !gangliaConf.toString().isEmpty()) {
			return statusFlag;
		}
		Map<String, String> settings = new HashMap();
		settings.put("kafka.metrics.reporters",
				"com.criteo.kafka.KafkaGangliaMetricsReporter");
		settings.put("kafka.ganglia.metrics.reporter.enabled", "true");
		settings.put("kafka.ganglia.metrics.host",
				gangliaMasterNode.getPublicIp() + "");
		settings.put("kafka.ganglia.metrics.port", gangliaConf.getPort() + "");
		settings.put("kafka.ganglia.metrics.group", "kafka");
		Result res;
		String serverFilePath = conf.getComponentHome()
				+ KAFKA_SERVER_FILE_PATH;
		for (Entry<String, String> propertyMap : settings.entrySet()) {
			statusFlag = addPropertyInFile(connection, nodeConf,
					serverFilePath, propertyMap.getKey(),
					propertyMap.getValue(), Constant.File_Extension.PROPERTIES);
			if (!statusFlag) {
				break;
			}
		}
		return statusFlag;
	}

	/**
	 * Removes the node. Called from undeployNodes() of KafkaDeployer class.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	public static boolean removeNode(KafkaConf conf, NodeConf nodeConf) {

		logger.setLoggerConfig(conf);

		SSHExec connection = null;
		try {
			String componentHome = conf.getInstallationPath() + KAFKA
					+ conf.getComponentVersion();
			String kafkaLogDir = conf.getLogDir();
			// split the comma seperated log.dir String to Array
			String[] kafkaLogArray = kafkaLogDir.split(",");

			// StringBuilder to store space seperated log.dir String
			StringBuilder sb = new StringBuilder();

			// if kafkaLogDir[].length = 1,then sb =kafkaLogDir[]
			sb.append(kafkaLogDir);

			// if kafkaLogDir[].length > 1;,then iterate kafkaLogDir[]
			if (kafkaLogArray.length > 1) {
				sb = new StringBuilder();
				for (int i = 0; i < kafkaLogArray.length; i++) {
					sb.append(kafkaLogArray[i]);
					sb.append(" ");
				}
			}

			logger.info(nodeConf.getPublicIp(), "Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {

				// remove Kafka bundle
				AnkushTask removeKafka = new Remove(componentHome);

				// remove kafka log.dir
				AnkushTask removeKafkaLogs = new Remove(sb.toString());

				logger.info(nodeConf.getPublicIp(), "Removing Kafka...");
				connection.exec(removeKafka);
				logger.info(nodeConf.getPublicIp(), "Removing Kafka log.Dir");
				connection.exec(removeKafkaLogs);
				return true;
			} else {
				logger.debug("Could not connect to node " + nodeConf);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			// disconncet to node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}
}
