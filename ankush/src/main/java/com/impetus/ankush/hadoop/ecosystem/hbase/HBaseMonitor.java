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
package com.impetus.ankush.hadoop.ecosystem.hbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.config.Parameter;
import com.impetus.ankush.hadoop.service.ParameterConfigService;
import com.impetus.ankush.hadoop.service.impl.ParameterConfigServiceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class HBaseMonitor.
 * 
 * @author Akhil
 */
public class HBaseMonitor extends AbstractMonitor {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(HBaseMonitor.class);

	/** The parameter config service. */
	private ParameterConfigService parameterConfigService = AppStoreWrapper
			.getService(Constant.Service.PARAMETER_CONFIG_SERVICE,
					ParameterConfigService.class);

	/** The Constant RELPATH_HBASE_LOG_DIR. */
	private static final String RELPATH_HBASE_LOG_DIR = "/logs";

	/**
	 * Method to get the map of role, ip list.
	 * 
	 * @return The Map of role and ip list.
	 */
	private Map techlogs() {

		/** Getting HBase config. **/
		HBaseConf conf = (HBaseConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.HBASE);

		// RegionServerNodes Ip list.
		List<String> regionServersIp = new ArrayList<String>();
		for (NodeConf nodeConf : conf.getHbaseRegionServerNodes()) {
			regionServersIp.add(nodeConf.getPublicIp());
		}
		result.put(Constant.Role.HBASEMASTER, Collections.singletonList(conf
				.getHbaseMasterNode().getPublicIp()));
		result.put(Constant.Role.HBASEREGIONSERVER, regionServersIp);
		return returnResult();
	}

	/**
	 * Gets the h base log file name text.
	 * 
	 * @param role
	 *            the role
	 * @return the h base log file name text
	 */
	private static String getHBaseLogFileNameText(String role) {
		String logText = "";
		if (role.equals(Constant.Role.HBASEMASTER)) {
			logText = "master";
		} else if (role.equals(Constant.Role.HBASEREGIONSERVER)) {
			logText = "regionserver";
		}
		return logText;
	}

	/**
	 * Method to get the node type files.
	 * 
	 * @return The Map of files and directories against the ip address.
	 * @throws Exception
	 *             the exception
	 */
	private void files() throws Exception {
		try {
			/** Getting HBase config. **/
			HBaseConf conf = (HBaseConf) dbCluster.getClusterConf()
					.getClusterComponents().get(Constant.Component.Name.HBASE);

			String nodeIp = (String) parameterMap.get(Constant.Keys.IP);
			String type = (String) parameterMap.get(Constant.Keys.TYPE);
			String logFileNameText = HBaseMonitor.getHBaseLogFileNameText(type);

			LogViewHandler logHandler = new LogViewHandler(nodeIp,
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// Path for the log directory.
			String logDirectory = conf.getComponentHome()
					+ HBaseMonitor.RELPATH_HBASE_LOG_DIR;

			List<String> files = logHandler.listTypeLogDirectory(logDirectory,
					logFileNameText);

			// puting the files in list.
			result.put(Constant.Keys.FILES, files);
		} catch (Exception e) {
			addAndLogError(
					"Exception: Unable to process request to view logs.", e);
		}
	}

	/**
	 * Method to view the content of the file.
	 * 
	 * @return The content of file.
	 */
	private Map view() {

		try {
			// Getting HBASE conf.
			HBaseConf conf = (HBaseConf) dbCluster.getClusterConf()
					.getClusterComponents().get(Constant.Component.Name.HBASE);

			// Getting the ip address from parameter map.
			String ip = (String) parameterMap.get(Constant.Keys.IP);

			// Getting the filename from parameter map.
			String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);

			// Getting the readCount value
			int readCount = ParserUtil.getIntValue(
					(String) parameterMap.get(Constant.Keys.READCOUNT), 0);

			int bytesCount = ParserUtil.getIntValue(
					(String) parameterMap.get(Constant.Keys.BYTESCOUNT), 0);

			// Create log view handler object.
			LogViewHandler logHandler = new LogViewHandler(ip,
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// logFile path
			String filePath = conf.getComponentHome()
					+ HBaseMonitor.RELPATH_HBASE_LOG_DIR + "/" + fileName;

			// putting the file content with total read characters.
			Map<String, String> content;

			content = logHandler
					.getFileContent(filePath, readCount, bytesCount);
			result.putAll(content);
		} catch (Exception e) {
			addAndLogError(
					"Exception: Unable to process request to view log file.", e);
		}

		return returnResult();
	}

	/**
	 * Method to get download url of the file.
	 * 
	 * @return Map of the download url.
	 */
	private Map download() {
		try {
			// Getting HBASE conf.
			HBaseConf conf = (HBaseConf) dbCluster.getClusterConf()
					.getClusterComponents().get(Constant.Component.Name.HBASE);

			// Getting the ip address from parameter map.
			String ip = (String) parameterMap.get(Constant.Keys.IP);

			// Getting the filename from parameter map.
			String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);

			// Create the log View Handler object.
			LogViewHandler logHandler = new LogViewHandler(ip,
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// logFile path
			String filePath = conf.getComponentHome()
					+ HBaseMonitor.RELPATH_HBASE_LOG_DIR + "/" + fileName;

			// setting download path.
			String downloadPath = "";
			downloadPath = logHandler.downloadFile(conf.getClusterName(),
					filePath);
			result.put(Constant.Keys.DOWNLOADPATH, downloadPath);
		} catch (Exception e) {
			addAndLogError(
					"Exception: Unable to process request to download log file.",
					e);
		}
		return returnResult();
	}

	/**
	 * Params.
	 * 
	 */
	private void params() {
		try {

			// Getting HBASE conf.
			HBaseConf conf = (HBaseConf) dbCluster.getClusterConf()
					.getClusterComponents().get(Constant.Component.Name.HBASE);

			if (!HadoopUtils.getServiceStatusForNode(conf.getHbaseMasterNode()
					.getPublicIp(), Constant.Role.AGENT)) {
				addError(AgentUtils.getAgentDownMessage(conf
						.getHbaseMasterNode().getPublicIp()));
				return;
			}

			/** HBase component home path. **/
			String homePath = conf.getComponentHome();

			/** hbase-site.xml file path. **/
			String hbaseSiteXmlPath = homePath
					+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR
					+ Constant.Hadoop.FileName.XML_HBASE_SITE;

			/** publicIp from HBaseMaster **/
			String publicIp = conf.getHbaseMasterNode().getPublicIp();
			List<Parameter> fileParams = new ArrayList<Parameter>();
			String password = conf.getPassword();
			if (!conf.getClusterConf().isAuthTypePassword()) {
				password = conf.getPrivateKey();
			}

			String content = SSHUtils.getFileContents(hbaseSiteXmlPath,
					publicIp, conf.getUsername(), password, conf
							.getClusterConf().isAuthTypePassword());
			if (content != null && content.length() > 0) {
				fileParams = ParameterConfigServiceImpl
						.loadXMLParameters(content);
			}

			Map<String, List<Object>> map = new HashMap<String, List<Object>>();

			// Converting Properties into Map.
			for (Parameter parameter : fileParams) {
				List<Object> list = new ArrayList<Object>();
				list.add(parameter.getValue());
				list.add(this.isParameterEditable(parameter.getName()));
				map.put(parameter.getName(), list);
			}
			Map resultInfo = new HashMap();
			resultInfo.put(Constant.Hadoop.FileName.XML_HBASE_SITE, map);
			result.put("params", resultInfo);
		} catch (Exception e) {
			addAndLogError(
					"Could not get parameter list from HBaseMaster node.", e);
		}
	}

	/**
	 * Editparams.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void editparams() throws Exception {

		boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
		if (isAgentDown) {
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return;
		}

		// Getting HBASE conf.
		HBaseConf conf = (HBaseConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.HBASE);

		Map<String, Object> confParams = (Map<String, Object>) parameterMap
				.get("params");

		String loggedUser = (String) parameterMap.get("loggedUser");

		// iterate over confParams Map.
		for (Entry entry : confParams.entrySet()) {

			// get fileName
			String fileName = (String) entry.getKey();

			// get config params list
			List<Map> params = (List<Map>) entry.getValue();

			// iterate on each param
			for (Map param : params) {
				Parameter parameter = JsonMapperUtil.objectFromMap(param,
						Parameter.class);

				String status = parameter.getStatus();
				// If no status Set for parameter
				if (status.equals(Constant.Parameter_Status.NONE)) {
					continue;
				}
				if (status.equals(Constant.Parameter_Status.ADD)) {
					addConfigFileParam(conf, parameter, fileName, loggedUser);
				}
				if (status.equals(Constant.Parameter_Status.EDIT)) {
					editConfigFileParam(conf, parameter, fileName, loggedUser);
				}
				if (status.equals(Constant.Parameter_Status.DELETE)) {
					deleteConfigFileParam(conf, parameter, fileName, loggedUser);
				}
			}
		}
	}

	/**
	 * add parameter to configuration file.
	 * 
	 * @param conf
	 *            the conf
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void addConfigFileParam(final GenericConfiguration conf,
			final Parameter parameter, final String fileName,
			final String loggedUser) {
		Set<NodeConf> nodes = conf.getCompNodes();
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String propertyValue = parameter.getValue();

							// get component homepath
							String componentHomePath = conf.getComponentHome();

							/** hbase-site.xml file path. **/
							String hbaseSiteXmlPath = componentHomePath
									+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR
									+ fileName;

							String username = conf.getUsername();
							String password = conf.getPassword();
							String privateKey = conf.getPrivateKey();

							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask add = new AddConfProperty(
											propertyName, propertyValue,
											hbaseSiteXmlPath,
											Constant.File_Extension.XML);
									res = connection.exec(add);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												conf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, propertyValue);
									}
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}
	}

	/**
	 * edit the Configuration parameters.
	 * 
	 * @param conf
	 *            the conf
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void editConfigFileParam(final GenericConfiguration conf,
			final Parameter parameter, final String fileName,
			final String loggedUser) {

		Set<NodeConf> nodes = conf.getCompNodes();

		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String newValue = parameter.getValue();

							// get component homepath
							String componentHomePath = conf.getComponentHome();

							/** hbase-site.xml file path. **/
							String hbaseSiteXmlPath = componentHomePath
									+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR
									+ fileName;

							String username = conf.getUsername();
							String password = conf.getPassword();
							String privateKey = conf.getPrivateKey();
							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask update = new EditConfProperty(
											propertyName, newValue,
											hbaseSiteXmlPath,
											Constant.File_Extension.XML);
									res = connection.exec(update);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												conf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, newValue);
									}
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}

	}

	/**
	 * delete the configuration parameters.
	 * 
	 * @param conf
	 *            the conf
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void deleteConfigFileParam(final GenericConfiguration conf,
			final Parameter parameter, final String fileName,
			final String loggedUser) {
		Set<NodeConf> nodes = conf.getCompNodes();

		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							String propertyName = parameter.getName();

							// get component homepath
							String componentHomePath = conf.getComponentHome();
							ConfigurationManager confManager = new ConfigurationManager();

							/** hbase-site.xml file path. **/
							String hbaseSiteXmlPath = componentHomePath
									+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR
									+ fileName;

							String username = conf.getUsername();
							String password = conf.getPassword();
							String privateKey = conf.getPrivateKey();
							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask update = new DeleteConfProperty(
											propertyName, hbaseSiteXmlPath,
											Constant.File_Extension.XML);
									res = connection.exec(update);
									if (res.isSuccess) {
										confManager.removeOldConfiguration(
												conf.getClusterDbId(),
												hostName, fileName,
												propertyName);
									}

								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
								if (semaphore != null) {
									semaphore.release();
								}
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}

	}

	/**
	 * Checks if is parameter editable.
	 * 
	 * @param parameterName
	 *            the parameter name
	 * @return true, if is parameter editable
	 */
	private boolean isParameterEditable(String parameterName) {
		try {
			boolean result = true;

			String predefinedNonEditableParameters = AppStoreWrapper
					.getAnkushConfReader().getStringValue(
							"hbase.non.editable.parameters");

			if (predefinedNonEditableParameters != null) {
				List<String> nonEditableParametersList = Arrays
						.asList(predefinedNonEditableParameters.split(","));
				if (nonEditableParametersList.contains(parameterName)) {
					return false;
				}
			}
			return result;
		} catch (Exception e) {
			return true;
		}
	}
}