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
package com.impetus.ankush.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.impetus.ankush.agent.utils.AgentLogger;

/**
 * The Class AgentConf.
 * 
 * @author Hokam Chauhan
 */
public class AgentConf {

	/** The log. */
	private static final AgentLogger LOGGER = new AgentLogger(AgentConf.class);

	/** The properties. */
	private Properties properties = new Properties();

	/** The file name. */
	private String fileName = "";

	/**
	 * Gets the string value.
	 * 
	 * @param key
	 *            the key
	 * @return String value for key
	 */
	public String getStringValue(String key) {
		return this.properties.getProperty(key);
	}

	/**
	 * Gets the boolean value.
	 * 
	 * @param key
	 *            the key
	 * @return Boolean value for key
	 */
	public boolean getBooleanValue(String key) {
		return Boolean.parseBoolean(this.properties.getProperty(key));
	}

	/**
	 * Gets the int value.
	 * 
	 * @param key
	 *            the key
	 * @return Integer value for key
	 */
	public int getIntValue(String key) {
		if (this.properties.getProperty(key) == null) {
			return 0;
		}
		return Integer.parseInt(this.properties.getProperty(key));
	}

	/**
	 * Instantiates a new agent conf.
	 */
	public AgentConf() {
		try {
			this.fileName = System.getProperty(Constant.AGENT_INSTALL_DIR)
					+ "/.ankush/agent/conf/agent.properties";
			load();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Gets the log upload file url.
	 * 
	 * @return the log upload file url
	 */
	public String getLogUploadFileUrl() {
		StringBuilder url = new StringBuilder("http://");
		url.append(properties.getProperty(Constant.PROP_SERVER_IP)).append(":");
		url.append(properties.getProperty(Constant.PROP_NAME_PORT));
		url.append(properties.getProperty(Constant.PROP_NAME_UPLOAD_FILE_URL));
		url.append("?category=log");
		return url.toString();
	}

	/**
	 * Gets the jar path.
	 * 
	 * @return the jar path
	 */
	public List<String> getJarPath() {
		List<String> jars = new ArrayList<String>();
		String jarString = properties.getProperty(Constant.PROP_JARPATH);

		if (jarString != null) {
			jars.addAll(Arrays.asList(jarString.split(",")));
		}
		return jars;
	}

	/**
	 * Load the Properties from the given file.
	 * 
	 * @param fileName
	 *            the file name
	 * @return Properties
	 * @throws IOException
	 *             * @throws FileNotFoundException
	 */
	public Properties load(String fileName) throws IOException {
		Properties properties = new Properties();
		InputStream inStream = new FileInputStream(fileName);
		try {
			properties.load(inStream);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			inStream.close();
		}
		return properties;
	}

	/**
	 * Load the properties from the default file.
	 * 
	 * @throws IOException
	 *             * @throws FileNotFoundException
	 */
	public void load() throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			String confPath = FilenameUtils.getFullPathNoEndSeparator(fileName);
			FileUtils.forceMkdir(new File(confPath));
			FileUtils.touch(file);
		}
		this.properties = load(fileName);
	}

	/**
	 * Save the specified properties values in given file.
	 * 
	 * @param properties
	 *            Properties
	 * @param fileName
	 *            String
	 * @throws IOException
	 *             * @throws FileNotFoundException
	 */
	public void save(Properties properties, String fileName) throws IOException {
		OutputStream outputStream = new FileOutputStream(fileName);
		properties.store(outputStream,
				"Updated at" + System.currentTimeMillis());
		outputStream.close();
	}

	/**
	 * Method save.
	 * 
	 * @param fileName
	 *            String
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void save(String fileName) throws IOException {
		this.fileName = fileName;
		save(this.properties, fileName);
	}

	/**
	 * Save the own properties values.
	 * 
	 * 
	 * @throws IOException
	 *             * @throws FileNotFoundException
	 */
	public void save() throws IOException {
		save(this.properties, this.fileName);
	}

	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Sets the properties.
	 * 
	 * @param props
	 *            the new properties
	 */
	public void setProperties(Properties props) {
		this.properties = props;
	}

	/**
	 * Gets the node info send URL.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return The URL to update the Node information
	 */
	public String getURL(String propertyName) {
		StringBuilder url = new StringBuilder("http://");
		url.append(properties.getProperty(Constant.PROP_SERVER_IP)).append(":");
		url.append(properties.getProperty(Constant.PROP_NAME_PORT));
		url.append(properties.getProperty(Constant.PROP_NAME_URL_PART));
		url.append(properties.getProperty(Constant.PROP_NAME_NODE_ID)).append("/");
		url.append(properties.getProperty(propertyName));
		return url.toString();
	}

	/**
	 * Gets the node info send URL.
	 * 
	 * @param action
	 *            the property name
	 * @return The URL to update the Node information
	 */
	public String getClusterMonitoringURL(String action) {
		StringBuilder url = new StringBuilder("http://");
		url.append(properties.getProperty(Constant.PROP_SERVER_IP)).append(":");
		url.append(properties.getProperty(Constant.PROP_NAME_PORT));
		url.append(properties
				.getProperty(Constant.PROP_NAME_CLUSTER_MONITORING_URL));
		url.append(properties.getProperty(Constant.PROP_NAME_CLUSTER_ID))
				.append("/").append(action);
		return url.toString();
	}

	/**
	 * Gets the jPS services.
	 * 
	 * @return the jPS services
	 */
	public List<String> getJPSServices() {
		return getDataList(Constant.PROP_NAME_JPS_PROCESS_LIST, ",");
	}

	/**
	 * Gets the ganglia services.
	 * 
	 * @return the ganglia services
	 */
	public List<String> getGangliaServices() {
		return getDataList(Constant.PROP_NAME_GANGLIA_PROCESS_LIST, ",");
	}

	/**
	 * Gets the Process Port List.
	 * 
	 * @return the Process Port List
	 */
	public List<String> getProcessPortList() {
		return getDataList(Constant.PROP_NAME_PROCESS_PORT_MAP, ",");
	}

	/**
	 * Gets the jPS services.
	 * 
	 * @return the jPS services
	 */
	public List<String> getDataList(String propertyName, String spliter) {

		List<String> procs = new ArrayList<String>();
		try {
			load();
			String processStr = properties.getProperty(propertyName);
			if (processStr != null) {
				LOGGER.info(" Process String " + processStr);
				return Arrays.asList(processStr.split(spliter));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return procs;
	}

	public String getServiceManageURL() {
		StringBuilder url = new StringBuilder("http://");
		url.append(properties.getProperty(Constant.PROP_SERVER_IP)).append(":");
		url.append(properties.getProperty(Constant.PROP_NAME_PORT));
		url.append(properties
				.getProperty(Constant.PROP_NAME_URL_SERVICE_PART_1));
		url.append(properties.getProperty(Constant.PROP_NAME_CLUSTER_ID))
				.append("/");
		url.append(properties
				.getProperty(Constant.PROP_NAME_URL_SERVICE_PART_2));
		return url.toString();
	}
}
