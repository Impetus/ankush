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
package com.impetus.ankush2.cassandra.deployer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.JmxUtil;
import com.impetus.ankush2.cassandra.utils.CassandraConstants;
import com.impetus.ankush2.cassandra.utils.CassandraUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;
import com.impetus.ankush2.utils.HostOperation;
import com.impetus.ankush2.utils.SSHUtils;

public class CassandraRegister {

	private AnkushLogger logger;
	private ClusterConfig clusterConfig;
	private ComponentConfig compConfig;

	/** The Constant ORG_APACHE_CASSANDRA. */
	private static final String ORG_APACHE_CASSANDRA = "org.apache.cassandra.";

	/** The Constant CASSANDRA_JMX_OBJECT_STORAGESERVICE. */
	private static final String CASSANDRA_JMX_OBJECT_STORAGESERVICE = "StorageService";

	/** The Constant CASSANDRA_JMX_OBJECT_ENDPOINTSNITCHINFO. */
	private static final String CASSANDRA_JMX_OBJECT_ENDPOINTSNITCHINFO = "EndpointSnitchInfo";

	/** The Constant CASSANDRA_JMX_OBJECT_GETRACK. */
	private static final String CASSANDRA_JMX_OBJECT_GETRACK = "getRack";

	/** The Constant CASSANDRA_JMX_OBJECT_GETDATACENTER. */
	private static final String CASSANDRA_JMX_OBJECT_GETDATACENTER = "getDatacenter";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES = "LiveNodes";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES = "UnreachableNodes";

	/** The Constant CASSANDRA_FOLDER_CONF. */
	private static final String CASSANDRA_FOLDER_CONF = "conf/";

	public CassandraRegister(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		this.logger = new AnkushLogger(this.getClass(), this.clusterConfig);
		this.compConfig = clusterConfig.getComponents().get(
				Constant.Component.Name.CASSANDRA);
	}

	public boolean createConfig() throws AnkushException {
		JmxUtil jmxUtil = null;
		Map<String, Map<String, Object>> nodes = null;
		Map<String, Object> nodeProperties = null;
		Map<String, Set<String>> nodeRoles = null;

		// Node set containing hostname of nodes in a cluster
		List<String> nodeList = null;
		try {
			// node hostname provided from UI for Cassandra Cluster registration
			final String registeredNodeIp = compConfig.getNodes().keySet()
					.iterator().next();
			// creating an advanceConf Object
			Map<String, Object> advanceConf = compConfig.getAdvanceConf();
			// JMX port to be used for making JMX connection
			Integer jmxPort = (Integer) advanceConf
					.get(CassandraConstants.ClusterProperties.JMX_PORT);

			// Making JMX connection
			jmxUtil = new JmxUtil(registeredNodeIp, jmxPort);
			MBeanServerConnection connection = jmxUtil.connect();

			// error message
			String errorMsg = "Unable to access JMX credentials "
					+ registeredNodeIp + ":" + jmxPort;

			// cassandra.yaml file location on node
			final String cassandraYamlLocation = FileNameUtils
					.convertToValidPath(compConfig.getHomeDir())
					+ CASSANDRA_FOLDER_CONF
					+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML;

			// log4j-server.properties file location on node
			String cassandraLog4jLocation = FileNameUtils
					.convertToValidPath(compConfig.getHomeDir())
					+ CASSANDRA_FOLDER_CONF
					+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_LOG4J_SERVER_PROPERTIES;
			// TODO: Handle version check for logging configuration
			if (compConfig.getVersion().contains("2.1.")) {
				cassandraLog4jLocation = FileNameUtils
						.convertToValidPath(compConfig.getHomeDir())
						+ CASSANDRA_FOLDER_CONF
						+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_LOGBACK_XML;
			}

			Set<String> errMessages = new HashSet<String>();

			if (connection != null) {

				// JMX Object for getting cluster nodes information
				ObjectName mObjNameStorageService = new ObjectName(
						ORG_APACHE_CASSANDRA + "db:type="
								+ CASSANDRA_JMX_OBJECT_STORAGESERVICE);

				// JMX Object for getting Datacenter and Rack information for
				// nodes
				ObjectName mObjNameEndpointSnitchInfo = new ObjectName(
						ORG_APACHE_CASSANDRA + "db:type="
								+ CASSANDRA_JMX_OBJECT_ENDPOINTSNITCHINFO);

				// JMX attribute for getting live nodes in a cluster
				Object attributeLiveNodes = jmxUtil.getAttribute(
						mObjNameStorageService,
						CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES);
				// JMX attribute for getting down nodes in a cluster
				Object attributeUnreachableNodes = jmxUtil.getAttribute(
						mObjNameStorageService,
						CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES);
				// Adding cluster nodes in node list
				nodeList = new ArrayList<String>();
				nodeList.addAll((List<String>) attributeLiveNodes);
				nodeList.addAll((List<String>) attributeUnreachableNodes);

				// Getting file content of cassandra.yaml file
				String cassandraYamlFileContent = SSHUtils.getFileContents(
						cassandraYamlLocation, registeredNodeIp,
						clusterConfig.getAuthConf());

				// Loading the yaml file content in map
				Map map = (Map) (new Yaml()).load(cassandraYamlFileContent);
				// Getting seed node string from map
				String seednodeStr = ((String) ((LinkedHashMap) ((ArrayList) ((LinkedHashMap) ((ArrayList) map
						.get("seed_provider")).get(0)).get("parameters"))
						.get(0)).get("seeds"));
				// Extracting the nodes from seednodeStr and adding them to seed
				// node list
				List<String> seeds = Arrays.asList(seednodeStr
						.split("\\s*,\\s*"));

				// Initializing nodes object
				nodes = new HashMap<String, Map<String, Object>>();
				String opSig[] = { String.class.getName() };
				// Datacenter and Rack name object
				Object datacenter = null;
				Object rack = null;
				boolean isSeedNode;
				String role = CassandraConstants.Node_Type.CASSANDRA_NON_SEED;
				String hostname = new String();
				for (String node : nodeList) {
					hostname = HostOperation.getMachineHostName(node,
							clusterConfig.getAuthConf().getUsername(),
							clusterConfig.getAuthConf().getPassword(),
							clusterConfig.getAuthConf().getPrivateKey());
					// preparing a new node properties for each node
					nodeProperties = new HashMap<String, Object>();
					isSeedNode = false;
					// If seednode list contains the node , then setting its
					// type to seednode
					if (seeds.contains(hostname)) {
						isSeedNode = true;
						role = CassandraConstants.Node_Type.CASSANDRA_SEED;
					}
					// TODO: nodeProperties in seednode to handle
					// TODO: preparing role set and adding it to nodeconfig
					nodeProperties.put(
							CassandraConstants.NodeProperties.CASSANDRA_SEED,
							isSeedNode);
					// TODO: Adding vNodeCount to nodeProperties

					// Adding node with nodeProperties to node map
					nodes.put(hostname, nodeProperties);

					// Parameters to be used for invoking an operation
					Object opParams[] = { hostname };
					// Getting Datacenter name from JMX object
					datacenter = connection
							.invoke(mObjNameEndpointSnitchInfo,
									CASSANDRA_JMX_OBJECT_GETDATACENTER,
									opParams, opSig);
					// Getting Rack name from JMX object
					rack = connection.invoke(mObjNameEndpointSnitchInfo,
							CASSANDRA_JMX_OBJECT_GETRACK, opParams, opSig);
					nodeRoles = new HashMap<String, Set<String>>();
					nodeRoles.put(Constant.Component.Name.CASSANDRA,
							new HashSet<String>(Arrays.asList(role)));
					// Adding nodes to cluster nodes
					// skip Registration in Level1
					if (!AnkushUtils.isMonitoredByAnkush(compConfig)) {
						AnkushUtils.addNodeToComponentConfig(clusterConfig,
								Constant.Component.Name.CASSANDRA, hostname,
								new HashMap());

					} else {
						AnkushUtils.addNodeToClusterAndComponent(clusterConfig,
								hostname,
								new HashSet<String>(Arrays.asList(role)),
								Constant.Component.Name.CASSANDRA);
					}
				}
				// Setting component level properties
				// Getting a truncated home path. The path is truncated to get
				// the child location for setting installation path
				String truncatedCompHome = CassandraUtils
						.getTruncatedPath(compConfig.getHomeDir());

				compConfig.setNodes(nodes);
				advanceConf.put(
						CassandraConstants.ClusterProperties.CLUSTER_NAME,
						(String) map.get("cluster_name"));
				advanceConf.put(
						CassandraConstants.ClusterProperties.PARTITIONER,
						(String) map.get("partitioner"));
				advanceConf.put(CassandraConstants.ClusterProperties.SNITCH,
						(String) map.get("endpoint_snitch"));
				advanceConf.put(
						CassandraConstants.ClusterProperties.SAVED_CACHES_DIR,
						(String) map.get("saved_caches_directory"));
				advanceConf.put(CassandraConstants.ClusterProperties.DATA_DIR,
						String.valueOf(((ArrayList) map
								.get("data_file_directories")).get(0)));
				advanceConf.put(
						CassandraConstants.ClusterProperties.COMMIT_LOG_DIR,
						(String) map.get("commitlog_directory"));
				advanceConf.put(CassandraConstants.ClusterProperties.RPC_PORT,
						String.valueOf(map.get("rpc_port")));
				advanceConf.put(
						CassandraConstants.ClusterProperties.STORAGE_PORT,
						String.valueOf(map.get("storage_port")));
				advanceConf.put(
						CassandraConstants.ClusterProperties.LOG_DIR,
						getCassandraLogDir(cassandraLog4jLocation,
								registeredNodeIp));
				// TODO: Setting install path in component conf. Need to verify
				// whether its needed or not.
				compConfig.setInstallPath(truncatedCompHome.substring(0,
						truncatedCompHome.lastIndexOf("/")));

			} else {
				errMessages.add(errorMsg);
				logger.error(errorMsg, Constant.Component.Name.CASSANDRA);
			}
			if (errMessages.size() > 0) {
				clusterConfig.getErrors().put(
						Constant.Component.Name.CASSANDRA, errMessages);
			}
		} catch (Exception e) {
			throw new AnkushException(
					"Could not create Cassandra configuration for cluster registration");
		} finally {
			jmxUtil.disconnect();
		}

		return true;
	}

	private String getCassandraLogDir(String cassandraLog4jLocation,
			String registeredNodeIp) {
		String logFile = null;
		try {
			// TODO: Handle version check for getting Cassandra Log dir
			if (compConfig.getVersion().contains("2.1.")) {
				logFile = getLogFileName(cassandraLog4jLocation, "appender");
			} else {
				// Getting log4jServer.properties file content for getting log
				// directory location
				String log4jfileContent = SSHUtils.getFileContents(
						cassandraLog4jLocation, registeredNodeIp,
						clusterConfig.getAuthConf());
				// Converting string into Properties.
				Properties properties = new Properties();
				properties.load(new StringReader(log4jfileContent));
				logFile = properties.getProperty("log4j.appender.R.File");
			}
			return logFile.substring(0, logFile.lastIndexOf("/"));
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public static String getLogFileName(String filePath, String root) {
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
			System.out.println("elements: " + elements);
			// getting child elements.
			List child = elements.getChildren(root);
			// iterating over the childs.
			for (int index = 0; index < child.size(); index++) {
				// getting element.
				Element e = (Element) child.get(index);
				System.out.println("e: " + e);
				String appenderName = e.getAttributeValue("name");
				System.out.println("appenderName: " + appenderName);
				if (appenderName.equalsIgnoreCase("FILE")) {
					Element fileChild = (Element) e.getChild("file");
					System.out.println("fileChild: " + fileChild);
					String fileName = fileChild.getValue();
					System.out.println("fileName: " + fileName);
					return fileName;
				}
			}
			// closing input stream.
			inputStream.close();
		} catch (Exception e) {
			// printing stack trace.
			e.printStackTrace();
		}
		// returning items.
		return null;
	}
}
