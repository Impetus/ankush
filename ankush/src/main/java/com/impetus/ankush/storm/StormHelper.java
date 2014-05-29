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
package com.impetus.ankush.storm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * @author hokam
 * 
 */
public class StormHelper {
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(StormHelper.class);

	private final String relPath_StormYaml = "/conf/storm.yaml";

	/** The conf manager. */
	private ConfigurationManager confManager = new ConfigurationManager();

	/**
	 * Creates the node.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	public boolean createNode(StormConf conf, NodeConf nodeConf,
			boolean addingNodes) {

		SSHExec connection = null;
		Result res = null;

		logger.setLoggerConfig(conf);
		logger.info(nodeConf.getPublicIp(), "Deploying Storm...");

		try {
			logger.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			String stormScriptJZMQPath = getSciprtPath("jzmq");
			String stormScriptZMQPath = getSciprtPath("zmq");

			// if connected
			if (connection != null) {
				logger.info(nodeConf.getPublicIp(), "Creating directory - "
						+ conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					logger.error(nodeConf,
							"Could not create installation directory");
					return false;
				}

				logger.info(nodeConf.getPublicIp(), "Extracting tarball");
				// get and extract tarball
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, conf, "storm");

				// if component exists at destination
				if (isSuccessfull) {
					String componentHome = conf.getInstallationPath()
							+ "storm-" + conf.getComponentVersion();
					// setting componentHome in bean
					conf.setComponentHome(componentHome);

					// install required softwares ...
					connection.uploadSingleDataToServer(stormScriptZMQPath,
							"/tmp/zmq.sh");
					connection.uploadSingleDataToServer(stormScriptJZMQPath,
							"/tmp/jzmq.sh");

					// Execute scripts
					CustomTask execZmqScript = new ExecCommand(
							"sh /tmp/zmq.sh " + conf.getPassword());
					CustomTask execJzmqScript = new ExecCommand(
							"sh /tmp/jzmq.sh " + conf.getPassword());

					logger.info(nodeConf.getPublicIp(), "Installing ZMQ...");
					// executing commands
					String[] resetKeyword = { "error", "fail" };
					execZmqScript.resetErrSysoutKeyword(resetKeyword);
					res = connection.exec(execZmqScript);

					if (!(res.rc == 0)) {
						logger.error(nodeConf, "Could not install ZMQ ");
						return false;
					}

					logger.info(nodeConf.getPublicIp(), "Installing JZMQ...");
					res = connection.exec(execJzmqScript);

					if (!(res.rc == 0)) {
						logger.error(nodeConf, "Could not install JZMQ ");
						return false;
					}

					String yamlContent = null;
					// if adding new nodes.
					Map map = null;
					if (addingNodes) {
						// getting the configuraiton from nimbus node.
						map = getNimbusConfigurationMap(conf);
						Yaml yaml = new Yaml();
						yamlContent = yaml.dumpAsMap(map);
					} else {
						// getting the default configuration.
						yamlContent = conf.getYamlContents(nodeConf);
						map = conf.getConfigurationMap(nodeConf);
					}
					// if yaml content is null.
					if (yamlContent == null) {
						logger.info(
								nodeConf.getPrivateIp(),
								"Unable to get the yaml configuration from the nimbus node. Using default configuration.");
						yamlContent = conf.getYamlContents(nodeConf);
					}
					logger.info(nodeConf.getPublicIp(),
							"Saving Storm configuration...");
					AnkushTask stormYamlConf = new AppendFile(yamlContent,
							componentHome + relPath_StormYaml);

					res = connection.exec(stormYamlConf);
					if (!res.isSuccess) {
						logger.error(nodeConf,
								"Could not create yaml file in storm_home/conf/ folder ");
						return false;
					}

					boolean status = configureJMXMonitoring(conf, nodeConf,
							connection);
					if (!status) {
						return false;
					}
					// adding process list and taskable information in agent
					// conf
					addProcessInfo(nodeConf, conf, connection);

					// save audit trails
					auditConfigurations(conf, nodeConf, map);
					return res.rc == 0;

				} else {
					logger.error(nodeConf, "Counld not extract tarball");
					return false;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			// disconncet to node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	private boolean configureJMXMonitoring(final StormConf conf,
			NodeConf nodeConf, SSHExec connection) {

		logger.debug("Configuring JMX Monitoring for Storm started ... ");
		Map<String, String> processPortMap = new HashMap<String, String>();
		processPortMap.put(
				Constant.Component.ProcessName.SUPERVISOR.toLowerCase(),
				conf.getJmxPort_Supervisor());
		processPortMap.put(Constant.Component.ProcessName.NIMBUS.toLowerCase(),
				conf.getJmxPort_Nimbus());

		boolean status = true;
		Result res = null;
		try {
			String filePath_StormYaml = conf.getComponentHome()
					+ relPath_StormYaml;
			String jmxPortPrefix = AppStoreWrapper.getAnkushConfReader()
					.getStringValue("jmxtrans.json.jmxport.prefix");
			String[] processList = JmxMonitoringUtil
					.getProcessList(Constant.Component.Name.STORM);

			if (processList == null) {
				return false;
			}

			for (String processName : processList) {
				if (processName.toLowerCase().equals(
						Constant.Component.ProcessName.NIMBUS))
					if (!nodeConf.getPrivateIp().equals(
							conf.getNimbus().getPrivateIp()))
						continue;

				String jmxPort = processPortMap.get(processName.toLowerCase());
				String targetText = jmxPortPrefix + processName.toUpperCase();
				AnkushTask updateJmxPort = new ReplaceText(targetText, jmxPort,
						filePath_StormYaml, false, conf.getPassword());
				res = connection.exec(updateJmxPort);
				if (!res.isSuccess) {
					logger.error(nodeConf, "Could not configure Jmx Port for "
							+ processName + ".");
					return false;
				}
				status = JmxMonitoringUtil.copyJmxTransJson(connection,
						conf.getUsername(), conf.getPassword(),
						Constant.Component.Name.STORM, processName, jmxPort,
						nodeConf.getPrivateIp());
				if (!status) {
					logger.error(nodeConf,
							"Could not copy JmxTrans JSON file for "
									+ processName + ".");
					return false;
				}
			}

		} catch (Exception e) {
			logger.error(nodeConf, "Could not update yaml file for Storm.");
			return false;
		}
		logger.debug("Configuring JMX Monitoring for Storm over ... ");
		return true;
	}

	/**
	 * Audit configurations.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 */
	private void auditConfigurations(StormConf conf, NodeConf nodeConf, Map map) {
		confManager.saveConfiguration(conf.getClusterDbId(),
				conf.getCurrentUser(), "storm.yaml", nodeConf.getPublicIp(),
				map);
	}

	/**
	 * Adds the process info.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @param connection
	 *            the connection
	 */
	private void addProcessInfo(NodeConf nodeConf, StormConf conf,
			SSHExec connection) {
		try {
			// add properties in agent configuration.
			Properties props = new Properties();
			props.setProperty("STORM_LIB", conf.getComponentHome() + "/lib");
			props.setProperty("STORM_HOME", conf.getComponentHome());
			AgentUtils.addProperties(connection, props);

			// add the taskable monitors in agent taskable configuration.
			List<String> classes = new ArrayList<String>();
			// classes.add(Constant.TaskableClass.JPS_MONITOR);
			classes.add(Constant.TaskableClass.STORM_MONITOR);
			AgentUtils.addTaskables(connection, classes);
		} catch (Exception e) {
			logger.error("Error in adding agent informations..", e);
		}
	}

	/**
	 * Removes the node.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	public boolean removeNode(StormConf conf, NodeConf nodeConf) {

		SSHExec connection = null;
		try {
			String componentHome = conf.getInstallationPath() + "storm-"
					+ conf.getComponentVersion();

			logger.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// adjusting storm conf dir in case of "root" user
				String stormConfDir = "";
				if (conf.getUsername().equalsIgnoreCase("root")) {
					stormConfDir = "/root/.storm/";
				} else {
					stormConfDir = "/home/" + conf.getUsername() + "/.storm/";
				}
				AnkushTask removeDotStormFolder = new Remove(stormConfDir);
				AnkushTask removeComponentHome = new Remove(componentHome);
				AnkushTask removeStormDataDir = new Remove(conf.getLocalDir());

				logger.info(nodeConf.getPublicIp(), "Removing Storm...");

				connection.exec(removeDotStormFolder);
				connection.exec(removeComponentHome);
				connection.exec(removeStormDataDir);
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

	/**
	 * Gets the sciprt path.
	 * 
	 * @param type
	 *            the type
	 * @return the sciprt path
	 */
	private String getSciprtPath(String type) {
		// getting configuration file
		Resource resource = new ClassPathResource("/scripts/storm/" + type
				+ ".sh");
		try {
			return resource.getFile().getAbsolutePath();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Getting yaml contents of nimbus node yaml file.
	 * 
	 * @param conf
	 * @return
	 */
	private Map getNimbusConfigurationMap(StormConf conf) {

		/** Storm component home path. **/
		String homePath = conf.getComponentHome();

		/** yaml file path **/
		String confPath = homePath + "/conf/storm.yaml";

		/** yaml content **/
		String yamlContent;
		try {
			/** getting yaml contents **/
			yamlContent = SSHUtils.getFileContents(
					confPath,
					conf.getNimbus().getPublicIp(),
					conf.getUsername(),
					conf.getClusterConf().isAuthTypePassword() ? conf
							.getPassword() : conf.getPrivateKey(), conf
							.getClusterConf().isAuthTypePassword());
			/** yaml content **/
		} catch (Exception e) {
			return null;
		}
		/** create yaml object. **/
		Yaml yaml = new Yaml();
		/** create map object by loading from yaml object **/
		Map map = (Map) yaml.load(yamlContent);
		map.remove("nimbus.childopts");
		return map;
	}
}
