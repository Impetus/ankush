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
package com.impetus.ankush.hadoop.service.impl;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.xml.XMLSerializer;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.Parameter;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.service.ParameterConfigService;

/**
 * The Class ParameterConfigServiceImpl.
 * 
 * @author bgunjan
 */
public class ParameterConfigServiceImpl implements ParameterConfigService {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(ParameterConfigServiceImpl.class);

	/** The Constant CONF_PROPERTY_COMPONENT_NAME. */
	private static final String CONF_PROPERTY_COMPONENT_NAME = "compName";

	/** The Constant CONF_PROPERTY_FILE_NAME. */
	private static final String CONF_PROPERTY_FILE_NAME = "fileName";

	/** The Constant CONF_PROPERTY_PARAMS. */
	private static final String CONF_PROPERTY_PARAMS = "params";

	/** The Constant EXTENSION_XML. */
	private static final String EXTENSION_XML = ".xml";

	/** The Constant PARAMETER_STATUS_NONE. */
	private static final String PARAMETER_STATUS_NONE = "none";

	/** The Constant PARAMETER_STATUS_ADD. */
	private static final String PARAMETER_STATUS_ADD = "add";

	/** The Constant PARAMETER_STATUS_EDIT. */
	private static final String PARAMETER_STATUS_EDIT = "edit";

	/** The Constant PARAMETER_STATUS_DELETE. */
	private static final String PARAMETER_STATUS_DELETE = "delete";

	/** The cluster conf. */
	private ClusterConf clusterConf;

	/** The hadoop nodes. */
	private List<NodeConf> hadoopNodes;

	/** The hConf. */
	private HadoopConf hConf;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.service.ParameterConfigService#
	 * getComponentConfigFiles(com.impetus.ankush.common.domain.Cluster,
	 * java.util.Map)
	 */
	@Override
	public Map<String, Object> getComponentConfigFiles(Cluster cluster,
			Map<String, String> parameterMap) {
		final Map<String, Object> componentConfigFiles = new HashMap<String, Object>();
		try {
			this.clusterConf = cluster.getClusterConf();
//			logger.setCluster(clusterConf);
//			logger.removeAppender();
			hConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP);
			if(hConf == null) {
				hConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
			}
			
			final String hostname = hConf.getNamenode().getPublicIp();
			final String username = clusterConf.getUsername();
			String password = clusterConf.getPassword();
			boolean authUsingPassword = clusterConf.isAuthTypePassword();
			if(!authUsingPassword) {
				password = clusterConf.getPrivateKey();
			}
			
			final String componentConfPath = hConf.getHadoopConfDir();

			List<String> confFiles = SSHUtils.listDirectory(hostname, username, password, authUsingPassword, componentConfPath);
			
			DateFormat df = new SimpleDateFormat("HH 'hours', mm 'mins,' ss 'seconds'");
			df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			
			final Semaphore semaphore = new Semaphore(confFiles.size());
			final boolean authUsingPasswordFinal = authUsingPassword; 
			final String passwordFinal = password;
			logger.info("message");
			for (final String fileName : confFiles) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
							if (fileName.endsWith(EXTENSION_XML)
								&& (!fileName.equals("fair-scheduler.xml"))) {
							String filePath = FileUtils
									.getSeparatorTerminatedPathEntry(componentConfPath)
									+ fileName;
							
							JSON fileContent = getXmlFileAsJSON(filePath, hostname,
									username, passwordFinal, authUsingPasswordFinal);
							
							if(fileContent != null) {
								if(fileContent.toString().equals("null")) {
									fileContent = new JSONArray();  
								}
								componentConfigFiles.put(fileName, fileContent);
							}
						}
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(confFiles.size());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			logger.error("Exception: Unable to get configuration parameters.");
			logger.error(e.getMessage());
		}
		logger.info("componentConfigFiles: " + componentConfigFiles);
		
		return componentConfigFiles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.service.ParameterConfigService#
	 * getCompConfFileParams(com.impetus.ankush.common.domain.Cluster,
	 * java.util.Map)
	 */
	@Override
	public Map<String, List<Parameter>> getCompConfFileParams(Cluster cluster,
			Map<String, String> parameterMap) {

		clusterConf = cluster.getClusterConf();

		hConf = (HadoopConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.HADOOP);
		if (hConf == null) {
			hConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
		}

		this.hadoopNodes = new ArrayList<NodeConf>(hConf.getCompNodes());

		Map<String, List<Parameter>> paramsInfo = new HashMap<String, List<Parameter>>();

		String componentName = parameterMap.get(CONF_PROPERTY_COMPONENT_NAME);
		String fileName = parameterMap.get(CONF_PROPERTY_FILE_NAME);

		try {

			String componentConfPath = hConf.getHadoopConfDir();
			List<Parameter> fileParams = getFileParams(componentConfPath,
					fileName);
			paramsInfo.put(fileName, fileParams);
		} catch (Exception e) {
			logger.error("Error occurred in getting config parameter, Reason : "
					+ e.getMessage());
		}
		return paramsInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.service.ParameterConfigService#
	 * updateConfigFileParam(com.impetus.ankush.common.domain.Cluster,
	 * java.util.Map)
	 */
	@Override
	public void updateConfigFileParam(final Cluster cluster,
			final Map<String, Object> parameterMap) {

		AppStoreWrapper.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				updateParam(cluster, parameterMap);
			}
		});
	}

	/**
	 * Update param.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param parameterMap
	 *            the parameter map
	 */
	private void updateParam(Cluster cluster, Map<String, Object> parameterMap) {
		Map<String, List<Map>> params = (Map<String, List<Map>>) parameterMap
				.get(CONF_PROPERTY_PARAMS);
		String loggedUser = (String) parameterMap.get("loggedUser");

		clusterConf = cluster.getClusterConf();

		hConf = (HadoopConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.HADOOP);
		if (hConf == null) {
			hConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
		}

		this.hadoopNodes = new ArrayList<NodeConf>(hConf.getCompNodes());

		Iterator iter = params.keySet().iterator();
		while (iter.hasNext()) {
			String fileName = (String) iter.next();
			List<Map> paramInfo = params.get(fileName);
			for (Map confParam : paramInfo) {
				try {
					Parameter parameter = JsonMapperUtil.objectFromMap(
							confParam, Parameter.class);

					String status = parameter.getStatus();
					// If no status Set for parameter
					if (status.equals(PARAMETER_STATUS_NONE)) {
						continue;
					}
					if (status.equals(PARAMETER_STATUS_ADD)) {
						addConfigFileParam(parameter, fileName, loggedUser);
					}
					if (status.equals(PARAMETER_STATUS_EDIT)) {
						editConfigFileParam(parameter, fileName, loggedUser);
					}
					if (status.equals(PARAMETER_STATUS_DELETE)) {
						deleteConfigFileParam(parameter, fileName);
					}

				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * Adds the config file param.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void addConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser) {

		try {
			final Semaphore semaphore = new Semaphore(hadoopNodes.size());
			try {
				for (final NodeConf hn : hadoopNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String paramValue = parameter.getValue();
							String componentConfPath = hConf.getHadoopConfDir();
							String xmlPath = FileUtils
									.getSeparatorTerminatedPathEntry(componentConfPath)
									+ fileName;

							String hostname = hn.getPublicIp();
							;
							String username = clusterConf.getUsername();
							String password = clusterConf.getPassword();
							String privateKey = clusterConf.getPrivateKey();

							SSHExec connection = null;
							try {
								connection = SSHUtils.connectToNode(hostname,
										username, password, privateKey);
								if (connection != null) {
									AnkushTask add = new AddConfProperty(
											propertyName, paramValue, xmlPath,
											Constant.File_Extension.XML);
									res = connection.exec(add);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												hConf.getClusterDbId(),
												loggedUser, fileName, hostname,
												propertyName, paramValue);
									}
								}
							} catch (Exception e) {
								logger.error(
										"Error occurred in updating config parameter, Reason : "
												+ e.getMessage(), e);
							} finally {
								// Disconnecting established connection
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
				semaphore.acquire(hadoopNodes.size());
			} catch (Exception e) {
				logger.error("Error occurred in getting config parameter, Reason : "
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not add conf file parameters.. "
					+ e.getMessage());
		}
	}

	/**
	 * Edits the config file param.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 * @param loggedUser
	 *            the logged user
	 */
	private void editConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser) {

		try {
			final Semaphore semaphore = new Semaphore(hadoopNodes.size());
			try {
				for (final NodeConf hn : hadoopNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String newValue = parameter.getValue();
							String componentConfPath = hConf.getHadoopConfDir();
							String xmlPath = FileUtils
									.getSeparatorTerminatedPathEntry(componentConfPath)
									+ fileName;

							String hostname = hn.getPublicIp();
							String username = clusterConf.getUsername();
							String password = clusterConf.getPassword();
							String privateKey = clusterConf.getPrivateKey();

							SSHExec connection = null;
							try {
								connection = SSHUtils.connectToNode(hostname,
										username, password, privateKey);
								if (connection != null) {
									AnkushTask update = new EditConfProperty(
											propertyName, newValue, xmlPath,
											Constant.File_Extension.XML);
									res = connection.exec(update);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												hConf.getClusterDbId(),
												loggedUser, fileName, hostname,
												propertyName, newValue);
									}
								}
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
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
				semaphore.acquire(hadoopNodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..");
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters.. "
					+ e.getMessage());
		}
	}

	/**
	 * Delete config file param.
	 * 
	 * @param parameter
	 *            the parameter
	 * @param fileName
	 *            the file name
	 */
	private void deleteConfigFileParam(final Parameter parameter,
			final String fileName) {

		try {
			final ConfigurationManager confManager = new ConfigurationManager();
			final Semaphore semaphore = new Semaphore(hadoopNodes.size());
			try {
				for (final NodeConf hn : hadoopNodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							String propertyName = parameter.getName();
							String componentConfPath = hConf.getHadoopConfDir();
							String xmlPath = FileUtils
									.getSeparatorTerminatedPathEntry(componentConfPath)
									+ fileName;

							String hostname = hn.getPublicIp();
							String username = clusterConf.getUsername();
							String password = clusterConf.getPassword();
							String privateKey = clusterConf.getPrivateKey();

							SSHExec connection = null;
							try {
								connection = SSHUtils.connectToNode(hostname,
										username, password, privateKey);
								if (connection != null) {
									AnkushTask deleteProp = new DeleteConfProperty(
											propertyName, xmlPath,
											Constant.File_Extension.XML);
									connection.exec(deleteProp);
									confManager.removeOldConfiguration(
											clusterConf.getClusterId(),
											hostname, fileName, propertyName);
								}
							} catch (Exception e) {
								logger.error(e.getMessage(), e);
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
				semaphore.acquire(hadoopNodes.size());
			} catch (Exception e) {
				logger.error("Could not delete conf file parameters.. ");
			}
		} catch (Exception e) {
			logger.error("Could not delete conf file parameters.. "
					+ e.getMessage());
		}
	}

	/**
	 * Gets the json.
	 * 
	 * @param filePath
	 *            the file path
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            the auth info
	 * @param authUsingPassword
	 *            the auth using password
	 * @return the json
	 */
	public static JSON getXmlFileAsJSON(String filePath, String hostname,
			String username, String authInfo, boolean authUsingPassword) {

		JSON json = null;
		try {
			String output = SSHUtils.getFileContents(filePath, hostname,
					username, authInfo, authUsingPassword);
			if (output != null) {
				String xml = output;
				XMLSerializer xmlSerializer = new XMLSerializer();
				json = xmlSerializer.read(xml);
			}
		} catch (Exception e) {
			logger.error("Error in getting file parameter info, Reason : "
					+ e.getMessage());
		}
		return json;
	}

	/**
	 * Gets the file params.
	 * 
	 * @param conf
	 *            the conf
	 * @param componentConfPath
	 *            the component conf path
	 * @param fileName
	 *            the file name
	 * @return the file params
	 */
	private List<Parameter> getFileParams(String componentConfPath,
			String fileName) {

		String hostname = hConf.getNamenode().getPublicIp();
		String username = clusterConf.getUsername();
		String password = clusterConf.getPassword();

		boolean authUsingPassword = clusterConf.isAuthTypePassword();
		if (!authUsingPassword) {
			password = clusterConf.getPrivateKey();
		}

		String filePath = FileUtils
				.getSeparatorTerminatedPathEntry(componentConfPath) + fileName;

		List<Parameter> fileParams = new ArrayList<Parameter>();
		try {
			String content = SSHUtils.getFileContents(filePath, hostname,
					username, password, authUsingPassword);
			if (content != null && content.length() > 0) {
				fileParams = loadXMLParameters(content);
			}
		} catch (Exception e) {
			logger.error("Error in getting file parameter info, Reason : "
					+ e.getMessage());
		}
		return fileParams;
	}

	/**
	 * Load xml parameters.
	 * 
	 * @param str
	 *            the str
	 * @return the list
	 */
	public static List<Parameter> loadXMLParameters(String str) {
		// str = str.trim().replaceFirst("^([\\W]+)<", "<");
		List<Parameter> parameters = new ArrayList<Parameter>();
		try {
			SAXBuilder builder = new SAXBuilder();
			// Document doc = builder.build(new InputSource(
			// new ByteArrayInputStream( ("<" + str).getBytes("utf-8"))));

			Document doc = builder.build(new InputSource(
					new ByteArrayInputStream(str.getBytes("utf-8"))));

			Element elements = doc.getRootElement();
			List child = elements.getChildren("property");
			for (int index = 0; index < child.size(); index++) {
				Element e = (Element) child.get(index);
				String name = getTagContent(e, "name");
				String value = getTagContent(e, "value");
				// String description = getTagContent(e, "description");
				// String finalVal = getTagContent(e, "final");
				// Boolean isfinal = null;
				// if (!finalVal.isEmpty()) {
				// isfinal = Boolean.parseBoolean(finalVal);
				// }
				parameters.add(new Parameter(name, value, "", "", false));
			}
		} catch (Exception e) {
			logger.error("ConfigLoader#loadXMLParameters " + e);
		}
		logger.debug("Loading XML Config file Parameters Done..!");
		return parameters;
	}

	/**
	 * Gets the tag content.
	 * 
	 * @param element
	 *            the element
	 * @param tagName
	 *            the tag name
	 * @return the tag content
	 */
	private static String getTagContent(Element element, String tagName) {
		String content = "";
		Element e = element.getChild(tagName);
		if (e != null) {
			content = e.getValue();
		}
		return content;
	}
}
