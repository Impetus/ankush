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
package com.impetus.ankush2.cassandra.monitor;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.JmxUtil;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.SSHConnection;
import com.impetus.ankush2.cassandra.utils.CassandraConstants;
import com.impetus.ankush2.cassandra.utils.CassandraUtils;
import com.impetus.ankush2.cassandra.utils.ColumnFamily;
import com.impetus.ankush2.cassandra.utils.CompactionProperty;
import com.impetus.ankush2.cassandra.utils.CompressionProperty;
import com.impetus.ankush2.cassandra.utils.GeneralProperty;
import com.impetus.ankush2.cassandra.utils.Keyspace;
import com.impetus.ankush2.cassandra.utils.PerformanceTuningProperty;
import com.impetus.ankush2.common.scripting.impl.AddConfProperty;
import com.impetus.ankush2.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush2.common.scripting.impl.EditConfProperty;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBEventManager;
import com.impetus.ankush2.db.DBServiceManager;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.monitor.AbstractMonitor;
import com.impetus.ankush2.hadoop.utils.Parameter;
import com.impetus.ankush2.utils.SSHUtils;

public class CassandraClusterMonitor extends AbstractMonitor {

	private ComponentConfig componentConfig;

	private Map<String, Object> advanceConf;

	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	@Override
	public void monitor(Cluster cluster, String action, Map parameterMap) {

		componentConfig = cluster.getClusterConfig().getComponents()
				.get(Constant.Component.Name.CASSANDRA);
		advanceConf = componentConfig.getAdvanceConf();

		super.monitor(cluster, action, parameterMap);
	}

	private List<Datacenter> createTopologyRing(CassandraJMX jmx)
			throws Exception {

		List<CassandraNode> nodeList = jmx.getCassandraNodeList();

		Map<String, Map<String, List<CassandraNode>>> datacenterMap = new HashMap<String, Map<String, List<CassandraNode>>>();

		for (CassandraNode node : nodeList) {
			if (!datacenterMap.containsKey(node.getDataCenter())) {
				datacenterMap.put(node.getDataCenter(),
						new HashMap<String, List<CassandraNode>>());
			}
			if (!datacenterMap.get(node.getDataCenter()).containsKey(
					node.getRack())) {
				datacenterMap.get(node.getDataCenter()).put(node.getRack(),
						new ArrayList<CassandraNode>());
			}
			datacenterMap.get(node.getDataCenter()).get(node.getRack())
					.add(node);
		}

		List<Datacenter> datacenters = new ArrayList<Datacenter>();

		for (String dc : datacenterMap.keySet()) {
			Datacenter datacenter = new Datacenter();
			datacenter.setDatacenterName(dc);
			List<Rack> racks = new ArrayList<Rack>();
			for (String r : datacenterMap.get(dc).keySet()) {
				Rack rack = new Rack();
				rack.setRackName(r);
				rack.setNodes(datacenterMap.get(dc).get(r));
				racks.add(rack);
			}
			datacenter.setRacks(racks);
			datacenters.add(datacenter);
		}
		return datacenters;

	}

	/**
	 * Get cluster overview.
	 * 
	 * @throws AnkushException
	 *             the ankush exception
	 */
	private void clusteroverview() throws AnkushException {

		Integer jmxPort = (Integer) advanceConf
				.get(CassandraConstants.ClusterProperties.JMX_PORT);

		CassandraJMX jmx = null;

		for (String node : componentConfig.getNodes().keySet()) {
			try {
				jmx = new CassandraJMX(node, jmxPort);
				break;
			} catch (Exception e) {
				// TODO: Continue
			}
		}
		if (jmx == null) {
			addAndLogError("Could not get cluster details.");
			return;
		}

		try {
			List<Datacenter> datacenters = createTopologyRing(jmx);
			result.put(Constant.Keys.DATACENTERS, datacenters);
			clusterSummary(datacenters);
		} catch (Exception e1) {
			addAndLogError("Could not get cluster details.");
		}
	}

	private void clusterSummary(List<Datacenter> list) {
		try {
			Set<String> seedNodeSet = CassandraUtils.getSeedNodeSet(
					clusterConf, componentConfig);
			if (seedNodeSet == null || seedNodeSet.isEmpty()) {
				addAndLogError("Could not get seed nodes details");
				return;
			}
			List<Keyspace> ks = getKeyspaces();
			if (getKeyspaces() == null) {
				addAndLogError("Could not get keyspace details");
				return;
			}
			Integer upNodeCount = CassandraUtils.getUpNodeCount(list);
			String partitioner = ((String) advanceConf
					.get(CassandraConstants.ClusterProperties.PARTITIONER));
			Map<String, Object> clusterSummary = new LinkedHashMap<String, Object>();
			clusterSummary.put("Up Node Count", upNodeCount);
			clusterSummary.put("Down node Count", componentConfig.getNodes()
					.size() - upNodeCount);
			clusterSummary.put("Keyspace Count", ks.size());
			clusterSummary.put(WordUtils.capitalize(
					CassandraConstants.ClusterProperties.SNITCH, null),
					advanceConf
							.get(CassandraConstants.ClusterProperties.SNITCH));
			clusterSummary.put(WordUtils.capitalize(
					CassandraConstants.ClusterProperties.PARTITIONER, null),
					partitioner.substring(partitioner.lastIndexOf(".") + 1));
			clusterSummary
					.put("Seed Nodes", StringUtils.join(seedNodeSet, ","));
			result.put(CassandraConstants.CLUSTER_SUMMARY, clusterSummary);
		} catch (Exception e) {
			addAndLogError("Could not get Cluster Summary");
		}
	}

	private void nodeoverview() throws AnkushException {
		// getting node's hostname for which monitoring details is to be fetched
		String hostName = (String) parameterMap.get(Constant.Keys.HOST);
		// if hostname is null
		if (hostName == null || hostName.isEmpty()) {
			throw new AnkushException("Hostname is missing.");
		}
		JmxUtil jmxUtil = null;
		Map<String, Object> nodeData = new HashMap<String, Object>();
		try {
			NodeMonitoring nodeMonitoring = new MonitoringManager()
					.getMonitoringData(hostName);
			if (nodeMonitoring == null) {
				throw new AnkushException(
						"Could not get monitoring data for node: " + hostName);
			}
			// Cassandra service status
			Map<String, Boolean> serviceStatus = nodeMonitoring
					.getServiceStatus(Constant.Component.Name.CASSANDRA);
			if (serviceStatus == null) {
				throw new AnkushException("Could not get service status for "
						+ Constant.Component.Name.CASSANDRA);
			}
			// getting node role
			String role = CassandraUtils.getNodeRole(clusterConf.getNodes()
					.get(hostName).getRoles());
			if (serviceStatus.get(role) == null
					|| serviceStatus.get(role) == false) {
				throw new AnkushException("Could not get node details as "
						+ Constant.Component.Name.CASSANDRA
						+ "service is down.");
			}
			jmxUtil = getJMXConnection(hostName,
					(Integer) advanceConf
							.get(CassandraConstants.ClusterProperties.JMX_PORT));
			if (jmxUtil == null) {
				throw new AnkushException("Unable to get JMX connection.");
			}

			ObjectName mObjNameStorageService = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);
			ObjectName mObjNameCaches = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_CACHES);
			ObjectName mObjNameFailureDetector = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "net:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_FAILUREDETECTOR);
			if (mObjNameStorageService == null || mObjNameCaches == null
					|| mObjNameFailureDetector == null) {
				throw new AnkushException("Could not get node details.");
			}
			List<String> tokens = getNodeTokens(jmxUtil, hostName,
					mObjNameStorageService);
			nodeData.put("ownership",
					getNodeOwnership(jmxUtil, mObjNameStorageService, hostName));
			nodeData.put("load",
					getNodeLoad(jmxUtil, mObjNameStorageService, hostName));
			nodeData.put("status",
					getNodeStatus(jmxUtil, hostName, mObjNameFailureDetector));
			nodeData.put("tokens", tokens);
			nodeData.put("tokenCount", tokens.size());
			nodeData.put("host", hostName);
			nodeData.put("keyCache",
					getCache(mObjNameCaches, getKeyCacheList(), jmxUtil));
			nodeData.put("rowCache",
					getCache(mObjNameCaches, getRowCacheList(), jmxUtil));
			getNodeTopology(nodeData, hostName, jmxUtil,
					mObjNameFailureDetector);
			result.put(Constant.Keys.NODEDATA, nodeData);
		} catch (AnkushException e) {
			addAndLogError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			addAndLogError("Could not get node details.");
		} finally {
			if (jmxUtil != null) {
				jmxUtil.disconnect();
			}
		}
	}

	private String getNodeOwnership(JmxUtil jmxUtil,
			ObjectName mObjNameStorageService, String hostName)
			throws AnkushException {

		// Getting ownership
		Object attrOwnership = jmxUtil
				.getAttribute(
						mObjNameStorageService,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP);
		System.out.println("attrOwnership: " + attrOwnership);
		if (attrOwnership == null) {
			throw new AnkushException("Could not get node ownership.");
		}
		LinkedHashMap ownership = (LinkedHashMap) attrOwnership;
		for (Object key : ownership.keySet()) {
			if (key.toString().contains(hostName + "/")) {
				DecimalFormat df = new DecimalFormat("###.##");
				return String
						.valueOf(df.format(((Float) ownership.get(key)) * 100))
						+ " %";
			}
		}
		throw new AnkushException("Could not get node ownership.");
	}

	private Object getNodeLoad(JmxUtil jmxUtil,
			ObjectName mObjNameStorageService, String hostName)
			throws AnkushException {
		String message = "Could not get node load.";
		// Getting load
		Object attrLoadStr = jmxUtil
				.getAttribute(
						mObjNameStorageService,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LOADSTRING);
		if (attrLoadStr == null) {
			logger.debug("Could not get jmx attribute for load...");
			throw new AnkushException(message);
		}
		return attrLoadStr;
	}

	private String getNodeStatus(JmxUtil jmxUtil, String hostName,
			ObjectName mObjNameFailureDetector) throws Exception {
		String message = "Could not get node status.";
		// Getting status
		Map<String, String> attrStatus = (Map) jmxUtil
				.getAttribute(
						mObjNameFailureDetector,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_SIMPLESTATES);
		System.out.println("attrStatus: " + attrStatus);
		if (attrStatus == null) {
			throw new AnkushException(message);
		}
		for (Object key : attrStatus.keySet()) {
			if (key.toString().contains(hostName + "/")) {
				return attrStatus.get(key);
			}
		}
		throw new AnkushException(message);
	}

	private List<String> getNodeTokens(JmxUtil jmxUtil, String host,
			ObjectName mObjNameStorageService) throws Exception {

		Object opParams[] = { host };
		String opSig[] = { String.class.getName() };
		List<String> tokens = (List<String>) jmxUtil
				.getResultObjectFromOperation(
						mObjNameStorageService,
						CassandraConstants.JMX_Operations.CASSANDRA_JMX_ATTRIBUTE_TOKENS,
						opParams, opSig);
		if (tokens == null) {
			throw new AnkushException("Could not get token list.");
		}
		return tokens;
	}

	private Map<String, Object> getNodeTopology(Map<String, Object> nodeData,
			String hostName, JmxUtil jmxUtil, ObjectName mObjNameFailureDetector)
			throws Exception {
		String message = "Could not get node topology.";
		Object opParams[] = { hostName };
		String opSig[] = { String.class.getName() };
		String strResult = (String) jmxUtil
				.getResultObjectFromOperation(
						mObjNameFailureDetector,
						CassandraConstants.JMX_Operations.CASSANDRA_JMX_ATTRIBUTE_ENDPOINTSTATE,
						opParams, opSig);
		if (strResult == null) {
			throw new AnkushException(message);
		}
		List<String> sysoutList = new ArrayList<String>(Arrays.asList(strResult
				.split("\n")));
		for (String outData : sysoutList) {
			List<String> paramList = new ArrayList<String>(
					Arrays.asList(outData.split(":")));
			String key = paramList.get(0).trim();
			if (key.equals("DC")) {
				nodeData.put("dataCenter", paramList.get(1).trim());
			} else if (key.equals("RACK")) {
				nodeData.put("rack", paramList.get(1).trim());
			} else if (key.equals("STATUS")) {
				nodeData.put("state", paramList.get(1).trim().split(",")[0]);
			}
		}
		return nodeData;
	}

	private List<String> getKeyCacheList() {
		return Arrays
				.asList(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCC,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCE,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCH,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCRHR,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCR,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCSPIS,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KCS);
	}

	private List<String> getRowCacheList() {
		return Arrays
				.asList(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCC,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCE,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCH,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCRHR,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCR,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCSPIS,
						CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RCS);
	}

	private Map<String, Object> getCache(ObjectName mObjNameCaches,
			List<String> caches, JmxUtil jmxUtil) throws AnkushException {
		Map<String, Object> cacheMap = new HashMap<String, Object>();
		Object attrVal;
		for (String cache : caches) {
			attrVal = jmxUtil.getAttribute(mObjNameCaches, cache);
			if (attrVal == null) {
				throw new AnkushException("Could not get value of attribute "
						+ cache);
			}
			cacheMap.put(cache, attrVal);
		}
		return cacheMap;
	}

	private JmxUtil getJMXConnection(String hostname, int jmxPort) {
		JmxUtil jmxUtil = null;
		try {
			jmxUtil = new JmxUtil(hostname, jmxPort);
			// connect to node
			return jmxUtil.connect() == null ? null : jmxUtil;

		} catch (Exception e) {
			addAndLogError("Could not get JMX connection");
		}
		return null;
	}

	/**
	 * Method to get the map of role, ip list.
	 * 
	 * @return The Map of role and ip list.
	 */
	private Map techlogs() {
		// seedNode ip list.
		List<String> seedNodes = new ArrayList<String>();

		// nonSeedNode ip list.
		List<String> nonSeedNodes = new ArrayList<String>();

		Set<String> seedNodeSet = (Set<String>) advanceConf
				.get(CassandraConstants.ClusterProperties.SEED_NODE_SET);
		// filling seed nodes.
		for (String host : seedNodeSet) {
			seedNodes.add(host);
		}

		// filling nonSeed nodes.
		for (String host : componentConfig.getNodes().keySet()) {
			if (!seedNodeSet.contains(host)) {
				nonSeedNodes.add(host);
			}
		}

		if (seedNodes.size() > 0) {
			result.put(Constant.Role.CASSANDRA_SEED, seedNodes);
		}
		if (nonSeedNodes.size() > 0) {
			result.put(Constant.Role.CASSANDRA_NON_SEED, nonSeedNodes);
		}
		return result;
	}

	/**
	 * Method to get the node type files.
	 * 
	 * @return The Map of files and directories against the ip address.
	 * @throws Exception
	 */
	private void files() throws Exception {
		String host = (String) parameterMap.get(Constant.Keys.HOST);
		if (host == null || host.isEmpty()) {
			addAndLogError("Host is missing.");
			return;
		}

		LogViewHandler logHandler = new LogViewHandler(host,
				clusterConf.getAuthConf());

		// Making the log directory.
		String logDirectory = FileNameUtils
				.convertToValidPath((String) advanceConf
						.get(CassandraConstants.ClusterProperties.LOG_DIR));

		// get the list of all .log files
		Map<String, String> files = logHandler.getLogFilesMap(logDirectory);

		// puting the files in list.
		result.put(Constant.Keys.FILES, files);
	}

	private void params() throws Exception {
		try {
			Map resultInfo = new HashMap();
			// checking Agent status
			if (!getAgentStatus(dbCluster, null)) {
				addAndLogError(Constant.Agent.AGENT_DOWN_MESSAGE);
				return;
			}
			// getting first host from the component node list for getting
			// configuration files content.
			String hostname = componentConfig.getNodes().keySet().iterator()
					.next();
			String confPath = FileNameUtils
					.convertToValidPath((String) advanceConf
							.get(CassandraConstants.ClusterProperties.CONF_DIR));
			if (confPath == null) {
				throw new AnkushException("Could not get configuration path.");
			}
			// Reading content of each configuration file
			for (String file : getCassandraConfFiles(confPath)) {
				// getting file content
				String fileContent = SSHUtils.getFileContents(file, hostname,
						clusterConf.getAuthConf());
				Map map = null;
				if (file.endsWith(Constant.File_Extension.YAML)) {
					// create yaml object.
					Yaml yaml = new Yaml();
					// create map object by loading from yaml object
					map = (Map) yaml.load(fileContent);
					if (map.isEmpty()) {
						throw new AnkushException(
								"Could not read file content: " + file);
					}

					// if(map.containsKey("data_file_directories")){
					// System.out.println("class: "
					// + map.get("data_file_directories").getClass());
					//
					// }

					// removing some properties from map
					removeKeyFromMap("initial_token", map);
					removeKeyFromMap("listen_address", map);
					removeKeyFromMap("rpc_address", map);
					removeKeyFromMap("num_tokens", map);
					removeKeyFromMap("seed_provider", map);
					removeKeyFromMap("server_encryption_options", map);
					removeKeyFromMap("client_encryption_options", map);
					map = prepareMapForYamlProperties(map);
				} else if (file.endsWith(Constant.File_Extension.PROPERTIES)) {
					Properties properties = new Properties();
					// Converting string into Properties.
					properties.load(new StringReader(fileContent));
					map = new HashMap<String, String>();
					// Converting Properties into Map.
					for (final String name : properties.stringPropertyNames()) {
						List<Object> newParameterValue = new ArrayList<Object>();
						newParameterValue.add(properties.getProperty(name));
						newParameterValue.add(true);
						map.put(name, newParameterValue);
					}
				} else if (file.endsWith(Constant.File_Extension.XML)) {
					// TODO: logback.xml file handling
					map = new HashMap<String, String>();
				}
				// List<String> fileList = new ArrayList<String>(
				// Arrays.asList(file.split("/")));
				// resultInfo.put(fileList.get(fileList.size() - 1), map);
				resultInfo.put(file.replace(confPath, ""), map);
			}
			Map params = new HashMap();
			params.put("params", resultInfo);
			result.putAll(params);
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		}
	}

	/**
	 * Gets the agent status.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return the agent status
	 */
	private boolean getAgentStatus(Cluster cluster, String host)
			throws AnkushException {
		if (host != null) {
			return !DBServiceManager.getManager().isAgentDown(host);
		} else {
			return !(new DBEventManager()).isAnyAgentDown(cluster);
		}
	}

	private List<String> getCassandraConfFiles(String confPath)
			throws AnkushException {
		List<String> confFiles = new ArrayList<String>();
		// yaml file path
		String cassandraYamlFile = confPath
				+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML;
		// log4j file path
		String cassandraLogServerFile = confPath
				+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_LOG4J_SERVER_PROPERTIES;
		if (componentConfig.getVersion().contains("2.1.")) {
			cassandraLogServerFile = confPath
					+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_LOGBACK_XML;
		}
		confFiles.add(cassandraYamlFile);
		confFiles.add(cassandraLogServerFile);
		String snitch = ((String) advanceConf
				.get(CassandraConstants.ClusterProperties.SNITCH));
		if (snitch == null) {
			throw new AnkushException("Could not get Snitch value.");
		}
		if (snitch
				.equalsIgnoreCase(CassandraConstants.Configuration_Properties.PROPERTY_FILE_SNITCH)) {
			// topology file path
			String cassandraTopologyFile = confPath
					+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_TOPOLOGY_PROPERTIES;
			confFiles.add(cassandraTopologyFile);
		} else if (snitch
				.equals(CassandraConstants.Configuration_Properties.GOSSIPING_PROPERTY_FILE_SNITCH)) {
			// topology file path
			String cassandraTopologyFile = confPath
					+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_RACK_DC_PROPERTIES;
			confFiles.add(cassandraTopologyFile);
		}
		return confFiles;
	}

	private void removeKeyFromMap(String key, Map map) {
		if (map.containsKey(key)) {
			map.remove(key);
		}
	}

	private Map prepareMapForYamlProperties(Map<String, Object> map) {
		Map<String, Object> editMap = new HashMap<String, Object>();
		List<String> nonEditableParameters = getNonEditableParameters();
		boolean edit;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			edit = true;
			List<Object> value = new ArrayList<Object>();
			value.add(entry.getValue());
			if (nonEditableParameters.contains(entry.getKey())) {
				edit = false;
			}
			value.add(edit);
			editMap.put(entry.getKey(), value);
		}
		return editMap;
	}

	private List<String> getNonEditableParameters() {
		List<String> nonEditableParameters = new ArrayList<String>();
		nonEditableParameters.add("cluster_name");
		nonEditableParameters.add("partitioner");
		nonEditableParameters.add("data_file_directories");
		nonEditableParameters.add("commitlog_directory");
		nonEditableParameters.add("saved_caches_directory");
		nonEditableParameters.add("flush_largest_memtables_at");
		nonEditableParameters.add("reduce_cache_sizes_at");
		nonEditableParameters.add("reduce_cache_capacity_to");
		nonEditableParameters.add("dynamic_snitch_badness_threshold");
		nonEditableParameters.add("endpoint_snitch");
		nonEditableParameters.add("commitlog_sync");
		return nonEditableParameters;
	}

	/**
	 * To get the list of configuration parameters.
	 * 
	 * @throws Exception
	 */
	private void nodeparams() throws Exception {
		try {
			String host = (String) parameterMap.get(Constant.Keys.HOST);
			if (host == null || host.isEmpty()) {
				throw new AnkushException("Hostname is missing.");
			}
			Map resultInfo = new HashMap();
			// checking Agent status
			if (!getAgentStatus(dbCluster, host)) {
				throw new AnkushException(Constant.Agent.AGENT_DOWN_MESSAGE);
			}
			// getting configuration directory location
			String confPath = FileNameUtils
					.convertToValidPath((String) advanceConf
							.get(CassandraConstants.ClusterProperties.CONF_DIR));
			if (confPath == null) {
				throw new AnkushException("Could not get configuration path.");
			}
			// Reading content of each configuration file
			for (String file : getCassandraConfFiles(confPath)) {
				// getting file content
				String fileContent = SSHUtils.getFileContents(file, host,
						clusterConf.getAuthConf());
				boolean edit = true;
				Map map = null;
				if (file.endsWith(Constant.File_Extension.YAML)) {
					// create yaml object.
					Yaml yaml = new Yaml();
					// create map object by loading from yaml object
					map = (Map) yaml.load(fileContent);
					if (map.isEmpty()) {
						throw new AnkushException(
								"Could not read file content: " + file);
					}
					// removing some properties from map
					removeKeyFromMap("seed_provider", map);
					removeKeyFromMap("server_encryption_options", map);
					removeKeyFromMap("client_encryption_options", map);
					map = prepareMapForYamlProperties(map);
				} else if (file.endsWith(Constant.File_Extension.PROPERTIES)) {
					if (file.contains(CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_TOPOLOGY_PROPERTIES)) {
						edit = false;
					}
					Properties properties = new Properties();
					// Converting string into Properties.
					properties.load(new StringReader(fileContent));
					map = new HashMap<String, String>();
					// Converting Properties into Map.
					for (final String name : properties.stringPropertyNames()) {
						List<Object> newParameterValue = new ArrayList<Object>();
						newParameterValue.add(properties.getProperty(name));
						newParameterValue.add(true);
						map.put(name, newParameterValue);
					}
				} else if (file.endsWith(Constant.File_Extension.XML)) {
					// TODO: logback.xml file handling
					map = new HashMap<String, String>();
				}
				resultInfo.put(file.replace(confPath, ""), map);
			}
			Map params = new HashMap();
			params.put("params", resultInfo);
			result.putAll(params);
		} catch (AnkushException e) {
			addAndLogError(e.getMessage());
		}
	}

	private void editparams() throws Exception {
		try {
			// getting parameters map to edit
			Map<String, List<Map>> params = (Map<String, List<Map>>) parameterMap
					.get(Constant.Keys.PARAMS);
			// getting logged user information to save it in configuration table
			String loggedUser = (String) parameterMap.get("loggedUser");
			// to be used during node level parameters edit
			boolean editNodeParameters = false;
			String hostname;
			if (parameterMap.containsKey(Constant.Keys.HOST)) {
				hostname = (String) parameterMap.get(Constant.Keys.HOST);
				if (hostname == null || hostname.isEmpty()) {
					throw new AnkushException("Hostname is missing.");
				}
				editNodeParameters = true;
			}
			for (Map.Entry<String, List<Map>> entry : params.entrySet()) {
				String fileName = entry.getKey();
				List<Map> confParams = entry.getValue();
				// getting type of file , viz. xml,yaml,...
				String fileType = fileName
						.substring(fileName.lastIndexOf(".") + 1);
				for (Map confParam : confParams) {
					Parameter parameter = JsonMapperUtil.objectFromMap(
							confParam, Parameter.class);
					// getting status of parameter, whether to add/edit/delete
					String status = parameter.getStatus();
					// If no status Set for parameter
					if (status.equals(Constant.ParameterActionType.NONE
							.toString())) {
						continue;
					}
					if (status.equals(Constant.ParameterActionType.ADD
							.toString())) {
						addConfigFileParam(parameter, fileName, loggedUser,
								fileType, editNodeParameters);
					} else if (status.equals(Constant.ParameterActionType.EDIT
							.toString())) {
						editConfigFileParam(parameter, fileName, loggedUser,
								fileType, editNodeParameters);
					} else if (status
							.equals(Constant.ParameterActionType.DELETE
									.toString())) {
						deleteConfigFileParam(parameter, fileName, fileType,
								editNodeParameters);
					}
				}
			}
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		}
	}

	private void addConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser,
			final String fileType, boolean editNodeParam) {

		final String propertyName = parameter.getName();
		final String propertyValue = parameter.getValue();
		// get server.properties file path
		final String propertyFilePath = advanceConf
				.get(CassandraConstants.ClusterProperties.CONF_DIR) + fileName;
		if (editNodeParam) {
			String host = (String) parameterMap.get(Constant.Keys.HOST);
			addParameter(host, propertyName, propertyValue, propertyFilePath,
					fileType, loggedUser, fileName);
		} else {
			final Semaphore semaphore = new Semaphore(componentConfig
					.getNodes().size());
			try {
				// iterate over all the nodes.
				for (final String host : componentConfig.getNodes().keySet()) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							addParameter(host, propertyName, propertyValue,
									propertyFilePath, fileType, loggedUser,
									fileName);
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(componentConfig.getNodes().size());
			} catch (Exception e) {
				addAndLogError("Error in updating config file params...");
			}
		}
	}

	private void addParameter(String hostName, String propertyName,
			String propertyValue, String propertyFilePath, String fileType,
			String loggedUser, String fileName) {
		Result res = null;
		ConfigurationManager confManager = new ConfigurationManager();
		SSHExec connection = null;
		try {
			// connect to node/machine
			connection = SSHUtils.connectToNode(hostName,
					clusterConf.getAuthConf());
			// if connection is established.
			if (connection == null) {
				throw new AnkushException("Could not connect to node: "
						+ hostName + ".");
			}
			AnkushTask add = new AddConfProperty(propertyName, propertyValue,
					propertyFilePath, fileType);
			res = connection.exec(add);
			if (res.isSuccess) {
				// Configuration manager to save the
				// property file change records.
				confManager.saveConfiguration(dbCluster.getId(), loggedUser,
						fileName, hostName, propertyName, propertyValue);
			}
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		} finally {
			// Disconnecting the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private void editConfigFileParam(final Parameter parameter,
			final String fileName, final String loggedUser,
			final String fileType, boolean editNodeParam) {
		final String propertyName = parameter.getName();
		final String newValue = parameter.getValue();
		final String propertyFilePath = advanceConf
				.get(CassandraConstants.ClusterProperties.CONF_DIR) + fileName;
		if (editNodeParam) {
			String host = (String) parameterMap.get(Constant.Keys.HOST);
			editParameter(host, propertyName, newValue, propertyFilePath,
					fileType, loggedUser, fileName);
		} else {
			final Semaphore semaphore = new Semaphore(componentConfig
					.getNodes().size());
			try {
				for (final String host : componentConfig.getNodes().keySet()) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							editParameter(host, propertyName, newValue,
									propertyFilePath, fileType, loggedUser,
									fileName);
							if (semaphore != null) {
								semaphore.release();
							}
						}
					});
				}
				semaphore.acquire(componentConfig.getNodes().size());
			} catch (Exception e) {
				addAndLogError("Error in updating config file params...");
			}
		}
	}

	private void editParameter(String host, String propertyName,
			String newValue, String propertyFilePath, String fileType,
			String loggedUser, String fileName) {
		Result res = null;
		ConfigurationManager confManager = new ConfigurationManager();
		SSHExec connection = null;
		try {
			// connect to node/machine
			connection = SSHUtils
					.connectToNode(host, clusterConf.getAuthConf());
			// if connection is established.
			if (connection == null) {
				throw new AnkushException("Could not connect to node: " + host
						+ ".");
			}
			AnkushTask update = new EditConfProperty(propertyName, newValue,
					propertyFilePath, fileType);
			res = connection.exec(update);
			if (res.isSuccess) {
				// Configuration manager to save the
				// property file change records.
				confManager.saveConfiguration(dbCluster.getId(), loggedUser,
						fileName, host, propertyName, newValue);
			}
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		} finally {
			// Disconnecting the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private void deleteConfigFileParam(final Parameter parameter,
			final String fileName, final String fileType, boolean editNodeParam) {
		final String propertyName = parameter.getName();
		final String propertyFilePath = advanceConf
				.get(CassandraConstants.ClusterProperties.CONF_DIR) + fileName;

		if (editNodeParam) {
			String host = (String) parameterMap.get(Constant.Keys.HOST);
			deleteParameter(host, propertyName, propertyFilePath, fileType,
					fileName);
		} else {
			final Semaphore semaphore = new Semaphore(componentConfig
					.getNodes().size());
			try {
				for (final String host : componentConfig.getNodes().keySet()) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							deleteParameter(host, propertyName,
									propertyFilePath, fileType, fileName);
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(componentConfig.getNodes().size());
			} catch (Exception e) {
				addAndLogError("Error in updating config file params...");
			}
		}
	}

	private void deleteParameter(String host, String propertyName,
			String propertyFilePath, String fileType, String fileName) {
		Result res = null;
		ConfigurationManager confManager = new ConfigurationManager();
		SSHExec connection = null;
		try {
			connection = SSHUtils
					.connectToNode(host, clusterConf.getAuthConf());
			if (connection == null) {
				throw new AnkushException("Could not connect to node.");
			}
			AnkushTask update = new DeleteConfProperty(propertyName,
					propertyFilePath, fileType);
			res = connection.exec(update);
			if (res.isSuccess) {
				confManager.removeOldConfiguration(dbCluster.getId(), host,
						fileName, propertyName);
			}
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		} finally {
			// Disconnecting the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private void keyspaceactionlist() {
		List<String> keyspaceActionList = new ArrayList<String>();
		try {
			keyspaceActionList.addAll(commonActions());
			Collections.sort(keyspaceActionList);
			result.put(CassandraConstants.ACTIONS, keyspaceActionList);
		} catch (Exception e) {
			addAndLogError("Could not get Keyspace action list");
		}
	}

	private void columnfamilyactionlist() {
		List<String> columnFamilyActionList = new ArrayList<String>();
		try {
			columnFamilyActionList.addAll(commonActions());
			Collections.sort(columnFamilyActionList);
			result.put(CassandraConstants.ACTIONS, columnFamilyActionList);
		} catch (Exception e) {
			addAndLogError("Could not get Columnfamily action list");
		}
	}

	private void nodeactionlist() {
		List<String> nodeActionList = new ArrayList<String>();
		try {
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_CLEANUP);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_NODE_DECOMMISSION);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_NODE_DISABLEBACKUP);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_DISABLE_NATIVE_TRANSPORT);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_DISABLE_GOSSIP);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_DISABLE_HANDOFF);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_DISABLE_THRIFT);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_NODE_DRAIN);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_NODE_ENABLEBACKUP);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_ENABLE_NATIVE_TRANSPORT);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_ENABLE_HANDOFF);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_ENABLE_GOSSIP);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_ENABLE_THRIFT);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_PAUSE_HANDOFF);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_RESUME_HANDOFF);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_RESET_LOCAL_SCHEMA);
			nodeActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_NODE_STOP_DAEMON);

			Collections.sort(nodeActionList);
			result.put(CassandraConstants.ACTIONS, nodeActionList);
		} catch (Exception e) {
			addAndLogError("Could not get action list");
		}
	}

	private void clusteractionlist() {
		List<String> clusterActionList = new ArrayList<String>();
		try {
			clusterActionList.addAll(commonActions());
			clusterActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_INVALIDATE_KEY_CACHE);
			clusterActionList
					.add(CassandraConstants.Cassandra_Actions.CASSANDRA_INVALIDATE_ROW_CACHE);
			Collections.sort(clusterActionList);
			result.put(CassandraConstants.ACTIONS, clusterActionList);
		} catch (Exception e) {
			addAndLogError("Could not get action list");
		}
	}

	private List<String> commonActions() {
		List<String> commonActionList = new ArrayList<String>();
		commonActionList
				.add(CassandraConstants.Cassandra_Actions.CASSANDRA_COMPACT);
		commonActionList
				.add(CassandraConstants.Cassandra_Actions.CASSANDRA_ENABLE_AUTOCOMPACTION);
		commonActionList
				.add(CassandraConstants.Cassandra_Actions.CASSANDRA_DISABLE_AUTOCOMPACTION);
		commonActionList
				.add(CassandraConstants.Cassandra_Actions.CASSANDRA_FLUSH);
		return commonActionList;
	}

	private void action() {
		SSHExec connection = null;
		try {
			String action = (String) parameterMap.get(Constant.Keys.ACTION);
			if (action == null || action.isEmpty()) {
				throw new AnkushException("Action is missing.");
			}
			// checking for keyspace in parameter map if column family name is
			// specified.
			if (parameterMap
					.containsKey(CassandraConstants.Configuration_Properties.COLUMN_FAMILY)
					&& !parameterMap
							.containsKey(CassandraConstants.Configuration_Properties.KEYSPACE)) {
				throw new AnkushException("Keyspace is missing.");
			}
			// getting hostname
			String host = (String) parameterMap.get(Constant.Keys.HOST);
			// if host is empty , then checking whether the action is cluster
			// level or node level , else checking whether the host specified is
			// up and Cassandra is running on it or not.
			if (host == null || host.isEmpty()) {
				// if action is cluster level, then getting any active node
				// hostname on which to run action, else returning with host
				// missing error.
				if (parameterMap
						.containsKey(CassandraConstants.Configuration_Properties.KEYSPACE)
						|| (parameterMap.containsKey("type") && parameterMap
								.get("type").equals("cluster"))) {
					Set<String> activeNodes = CassandraUtils
							.getCassandraActiveNodes(componentConfig.getNodes()
									.keySet());
					if (activeNodes.isEmpty()) {
						throw new AnkushException(
								"Either Cassandra or Agent is down on all nodes in Cluster.");
					}
					host = activeNodes.iterator().next();
				} else {
					throw new AnkushException("Host is missing.");
				}
			} else if (!CassandraUtils.isActive(host)) {
				throw new AnkushException(host + " is down.");
			}
			Result res = null;
			logger.debug("Connecting with node : " + host);
			// connect to remote node
			connection = SSHUtils
					.connectToNode(host, clusterConf.getAuthConf());
			if (connection == null) {
				throw new AnkushException("Could not connect to node.");
			}
			StringBuffer operation = new StringBuffer();
			operation
					.append(advanceConf
							.get(CassandraConstants.ClusterProperties.BIN_DIR))
					.append(CassandraConstants.Cassandra_executables.NODETOOL)
					.append(" ").append(action);
			if (parameterMap
					.containsKey(CassandraConstants.Configuration_Properties.KEYSPACE)) {
				operation
						.append(" ")
						.append(parameterMap
								.get(CassandraConstants.Configuration_Properties.KEYSPACE));
			}
			if (parameterMap
					.containsKey(CassandraConstants.Configuration_Properties.COLUMN_FAMILY)) {
				operation
						.append(" ")
						.append(parameterMap
								.get(CassandraConstants.Configuration_Properties.COLUMN_FAMILY));
			}
			// running command on each node
			CustomTask task = new ExecCommand(operation.toString());
			res = connection.exec(task);
			if (res.rc != 0) {
				throw new AnkushException("Could not perform " + action
						+ " action.");
			}
			result.put(Constant.Keys.OUTPUT, action + " operation is done");
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private List<Keyspace> getKeyspaces() {
		JmxUtil jmxUtil = null;
		try {
			for (String host : CassandraUtils
					.getCassandraActiveNodes(componentConfig.getNodes()
							.keySet())) {
				jmxUtil = getJMXConnection(
						host,
						(Integer) advanceConf
								.get(CassandraConstants.ClusterProperties.JMX_PORT));
				if (jmxUtil == null) {
					continue;
				}
				List<Keyspace> lstKeyspace = getKeyspaceInfo(jmxUtil, null,
						host);
				return lstKeyspace;
			}
			addAndLogError("Unable to fetch Keyspace Information as all nodes are down.");
		} catch (Exception e) {
			addAndLogError("Could not get keyspace details.");
		} finally {
			if (jmxUtil != null) {
				jmxUtil.disconnect();
			}
		}
		return null;
	}

	private void keyspaces() {
		try {
			List<Keyspace> lstKeyspace = getKeyspaces();
			if (lstKeyspace.isEmpty()) {
				return;
			}
			result.put(
					CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KEYSPACES,
					lstKeyspace);
		} catch (Exception e) {
			addAndLogError("Could not get keyspace details.");
		}
	}

	private List<Keyspace> getKeyspaceInfo(JmxUtil jmxUtil, String where,
			String hostName) {
		List<Keyspace> lstKeyspace = new ArrayList<Keyspace>();
		try {
			String cassandraHome = componentConfig.getHomeDir();
			String fileName = cassandraHome + "script";
			String connectionPort = getRpcPort(cassandraHome, hostName);
			if (componentConfig.getVersion().contains("2.1.")) {
				connectionPort = getNativeTransportPort(cassandraHome, hostName);
			}

			String cmdFileContent2 = "echo \"select * from system.schema_keyspaces "
					+ (where == null ? "" : where)
					+ ";\" > "
					+ fileName
					+ " ; "
					+ cassandraHome
					+ "bin/cqlsh "
					+ hostName
					+ " "
					+ connectionPort + " -f " + fileName;

			SSHConnection sshConnection = new SSHConnection(hostName,
					clusterConf.getAuthConf().getUsername(), clusterConf
							.getAuthConf().isUsingPassword() ? clusterConf
							.getAuthConf().getPassword() : clusterConf
							.getAuthConf().getPrivateKey(), clusterConf
							.getAuthConf().isUsingPassword());
			sshConnection.exec(cmdFileContent2);

			if (sshConnection.getExitStatus() != 0) {
				throw new AnkushException(
						"Unable to fetch Column Family Information");
			}

			// to proceed
			List<String> keyspaceRecords = new ArrayList<String>(
					Arrays.asList(sshConnection.getOutput().split("\n")));
			while (true) {
				String removedString = keyspaceRecords.remove(0);
				if (removedString.contains("----+----")) {
					break;
				}
			}
			for (String str : keyspaceRecords) {
				List<String> listKeyspaceDetails = new ArrayList<String>(
						Arrays.asList(str.split("\\|")));
				if (listKeyspaceDetails.size() != 4) {
					break;
				}
				Keyspace ks = new Keyspace();
				ks.setKeyspaceName(listKeyspaceDetails.get(0).trim());
				ks.setDurableWrites(listKeyspaceDetails.get(1).trim());
				ks.setReplicationStrategy(listKeyspaceDetails.get(2)
						.replaceAll(".*\\.", "").trim());
				ks.setStrategyOptions(listKeyspaceDetails.get(3).trim());
				ks.setCfCount(getColumnFamilyList(ks.getKeyspaceName(),
						jmxUtil, "*").size());

				lstKeyspace.add(ks);
			}
		} catch (Exception e) {
			addAndLogError(e.getMessage());
		}
		return lstKeyspace;
	}

	private void createkeyspace() {
		try {
			String ksName = (String) parameterMap.get("keyspace");
			String strategyClass = (String) parameterMap.get("strategyClass");
			Integer replication_factor = (Integer) parameterMap
					.get("replicationFactor");
			String cassandraHome = componentConfig.getHomeDir();
			String fileName = cassandraHome + "keyspacescript";
			for (String host : CassandraUtils
					.getCassandraActiveNodes(componentConfig.getNodes()
							.keySet())) {
				String connectionPort = getRpcPort(cassandraHome, host);
				if (componentConfig.getVersion().contains("2.1.")) {
					connectionPort = getNativeTransportPort(cassandraHome, host);
				}
				String cmdFileContent2 = "echo \"CREATE KEYSPACE " + ksName
						+ " WITH REPLICATION = { 'class' : '" + strategyClass
						+ "', 'replication_factor' : " + replication_factor
						+ " };\" > " + fileName + " ; " + cassandraHome
						+ "bin/cqlsh " + host + " " + connectionPort + " -f "
						+ fileName;
				SSHConnection sshConnection = new SSHConnection(host,
						clusterConf.getAuthConf().getUsername(), clusterConf
								.getAuthConf().isUsingPassword() ? clusterConf
								.getAuthConf().getPassword() : clusterConf
								.getAuthConf().getPrivateKey(), clusterConf
								.getAuthConf().isUsingPassword());
				sshConnection.exec(cmdFileContent2);

				System.out.println("cmdFileContent2: " + cmdFileContent2);

				if (sshConnection.getExitStatus() != 0) {
					throw new AnkushException("Unable to create keyspace");
				} else {
					logger.info("Keyspace created");
				}
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createcolumnfamily() {
		try {
			String ksName = (String) parameterMap.get("keyspace");
			String cfName = (String) parameterMap.get("columnfamily");
			Map<String, String> fields = (Map<String, String>) parameterMap
					.get("fields");
			List<String> primaryKeys = (List<String>) parameterMap
					.get("primaryKey");
			String cassandraHome = componentConfig.getHomeDir();
			String fileName = cassandraHome + "columnfamilyscript";
			for (String host : CassandraUtils
					.getCassandraActiveNodes(componentConfig.getNodes()
							.keySet())) {
				String connectionPort = getRpcPort(cassandraHome, host);
				if (componentConfig.getVersion().contains("2.1.")) {
					connectionPort = getNativeTransportPort(cassandraHome, host);
				}
				String cmdFileContent2 = "echo \"CREATE TABLE " + ksName + "."
						+ cfName + " (";

				for (Map.Entry<String, String> field : fields.entrySet()) {
					cmdFileContent2 += field.getKey() + " " + field.getValue()
							+ ",";
				}
				cmdFileContent2 = cmdFileContent2.replaceAll(",$", "");
				cmdFileContent2 += ", PRIMARY KEY (";
				for (String pk : primaryKeys) {
					cmdFileContent2 += pk + ",";
				}
				cmdFileContent2 = cmdFileContent2.replaceAll(",$", "");
				cmdFileContent2 += "));\" > " + fileName + " ; "
						+ cassandraHome + "bin/cqlsh " + host + " "
						+ connectionPort + " -f " + fileName;

				System.out.println("cmdFileContent2: " + cmdFileContent2);
				SSHConnection sshConnection = new SSHConnection(host,
						clusterConf.getAuthConf().getUsername(), clusterConf
								.getAuthConf().isUsingPassword() ? clusterConf
								.getAuthConf().getPassword() : clusterConf
								.getAuthConf().getPrivateKey(), clusterConf
								.getAuthConf().isUsingPassword());
				sshConnection.exec(cmdFileContent2);

				if (sshConnection.getExitStatus() != 0) {
					throw new AnkushException("Unable to create Column family");
				} else {
					logger.info("Column family created");
				}
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertdata() {
		try {
			String ksName = (String) parameterMap.get("keyspace");
			String cfName = (String) parameterMap.get("columnfamily");
			Map<String, Object> fields = (Map<String, Object>) parameterMap
					.get("fields");
			String cassandraHome = componentConfig.getHomeDir();
			String fileName = cassandraHome + "insertdatascript";
			for (String host : CassandraUtils
					.getCassandraActiveNodes(componentConfig.getNodes()
							.keySet())) {
				String connectionPort = getRpcPort(cassandraHome, host);
				if (componentConfig.getVersion().contains("2.1.")) {
					connectionPort = getNativeTransportPort(cassandraHome, host);
				}
				String cmdFileContent2 = "echo \"INSERT INTO " + ksName + "."
						+ cfName + " (";

				for (String field : fields.keySet()) {
					cmdFileContent2 += field + ",";
				}
				cmdFileContent2 = cmdFileContent2.replaceAll(",$", "");
				cmdFileContent2 += ") VALUES (";
				for (Object field : fields.values()) {
					if (field.getClass().equals(String.class)) {
						field = "'" + field + "'";
					}
					cmdFileContent2 += field + ",";
				}
				cmdFileContent2 = cmdFileContent2.replaceAll(",$", "");
				cmdFileContent2 += ");\" > " + fileName + " ; " + cassandraHome
						+ "bin/cqlsh " + host + " " + connectionPort + " -f "
						+ fileName;

				System.out.println("cmdFileContent2: " + cmdFileContent2);
				SSHConnection sshConnection = new SSHConnection(host,
						clusterConf.getAuthConf().getUsername(), clusterConf
								.getAuthConf().isUsingPassword() ? clusterConf
								.getAuthConf().getPassword() : clusterConf
								.getAuthConf().getPrivateKey(), clusterConf
								.getAuthConf().isUsingPassword());
				sshConnection.exec(cmdFileContent2);

				if (sshConnection.getExitStatus() != 0) {
					throw new AnkushException("Unable to create Column family");
				} else {
					logger.info("Column family created");
				}
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getRpcPort(String homePath, String hostname)
			throws AnkushException {
		// yaml file path
		String cassandraYamlFile = advanceConf
				.get(CassandraConstants.ClusterProperties.CONF_DIR)
				+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML;

		String rpcPort = null;
		try {
			rpcPort = SSHUtils.getCommandOutput(" grep '^rpc_port:' "
					+ cassandraYamlFile + " | cut -d' ' -f2", hostname,
					clusterConf.getAuthConf().getUsername(), clusterConf
							.getAuthConf().isUsingPassword() ? clusterConf
							.getAuthConf().getPassword() : clusterConf
							.getAuthConf().getPrivateKey(), clusterConf
							.getAuthConf().isUsingPassword());
		} catch (Exception e) {
			throw new AnkushException("Could not get rpc port.");
		}
		return rpcPort;
	}

	private String getNativeTransportPort(String homePath, String hostname)
			throws AnkushException {
		// yaml file path
		String cassandraYamlFile = advanceConf
				.get(CassandraConstants.ClusterProperties.CONF_DIR)
				+ CassandraConstants.Cassandra_Configuration_Files.CASSANDRA_YAML;

		String rpcPort = null;
		try {
			rpcPort = SSHUtils.getCommandOutput(
					" grep '^native_transport_port:' " + cassandraYamlFile
							+ " | cut -d' ' -f2", hostname, clusterConf
							.getAuthConf().getUsername(), clusterConf
							.getAuthConf().isUsingPassword() ? clusterConf
							.getAuthConf().getPassword() : clusterConf
							.getAuthConf().getPrivateKey(), clusterConf
							.getAuthConf().isUsingPassword());
		} catch (Exception e) {
			throw new AnkushException("Could not get rpc port.");
		}
		return rpcPort;
	}

	private Set getColumnFamilyList(String keyspace, JmxUtil jmxUtil,
			String columnfamily) throws AnkushException {
		String patternStr = "org.apache.cassandra.db:type=ColumnFamilies,keyspace="
				+ keyspace + ",columnfamily=" + columnfamily;
		Set columnFamilySet = jmxUtil.getObjectSetFromPatternString(patternStr);
		if (columnFamilySet == null) {
			throw new AnkushException("Could not get columnn family details.");
		}
		return columnFamilySet;
	}

	private void columnfamilies() {
		String keyspace = (String) parameterMap.get("keyspace");
		if (keyspace == null || keyspace.isEmpty()) {
			addAndLogError("Keyspace is missing.");
			return;
		}
		try {
			for (String publicIp : CassandraUtils
					.getCassandraActiveNodes(componentConfig.getNodes()
							.keySet())) {
				JmxUtil jmxUtil = getJMXConnection(
						publicIp,
						(Integer) advanceConf
								.get(CassandraConstants.ClusterProperties.JMX_PORT));
				if (jmxUtil == null) {
					continue;
				}

				Set columnFamilySet = getColumnFamilyList(keyspace, jmxUtil,
						"*");
				List<ColumnFamily> lstColumnFamily = new ArrayList<ColumnFamily>();

				Iterator iter = columnFamilySet.iterator();
				while (iter.hasNext()) {
					String objectName = iter.next().toString();
					List<String> jmxObjectList = new ArrayList<String>(
							Arrays.asList(objectName.split("=")));
					ObjectName mObjCF = new ObjectName(objectName);
					String cfName = jmxObjectList.get(jmxObjectList.size() - 1);
					Object liveSSTableCount = jmxUtil
							.getAttribute(
									mObjCF,
									CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LIVESSTABLECOUNT);
					Object writelatency = jmxUtil
							.getAttribute(
									mObjCF,
									CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_WRITELATENCY);
					Object readlatency = jmxUtil
							.getAttribute(
									mObjCF,
									CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_READLATENCY);
					Object pendingTasks = jmxUtil
							.getAttribute(
									mObjCF,
									CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_PENDINGTASKS);
					if (liveSSTableCount == null || writelatency == null
							|| readlatency == null || pendingTasks == null) {
						throw new AnkushException(
								"Could not get Column family details.");
					}
					ColumnFamily cf = new ColumnFamily();
					cf.setColumnFamilyName(cfName);
					cf.setLiveSSTableCount(liveSSTableCount);
					cf.setWritelatency(writelatency);
					cf.setReadLatency(readlatency);
					cf.setPendingTasks(pendingTasks);
					lstColumnFamily.add(cf);
				}
				result.put("ColumnFamilies", lstColumnFamily);
				return;
			}
			addAndLogError("Unable to fetch Keyspace Details as all nodes are down");
		} catch (MalformedObjectNameException e) {
			addAndLogError("Unable to fetch Keyspace Details");
		} catch (NullPointerException e) {
			addAndLogError("Unable to fetch Keyspace Details");
		} catch (AnkushException e) {
			addAndLogError(e.getMessage());
		}
	}

	private void columnfamilydetails() {
		String keyspace = (String) parameterMap.get("keyspace");
		String columnFamily = (String) parameterMap.get("columnfamily");
		if (keyspace == null || keyspace.isEmpty() || columnFamily == null
				|| columnFamily.isEmpty()) {
			addAndLogError("Either Keyspace or Column Family is missing.");
			return;
		}
		try {
			int jmxPort = (Integer) advanceConf
					.get(CassandraConstants.ClusterProperties.JMX_PORT);
			for (String nodeConf : componentConfig.getNodes().keySet()) {
				NodeMonitoring nodeMonitoring = new MonitoringManager()
						.getMonitoringData(nodeConf);

				// Cassandra Service Status.
				Map<String, Boolean> serviceStatus = nodeMonitoring
						.getServiceStatus(Constant.Component.Name.CASSANDRA);

				// assigning role to seed or non seed on the basis
				// of type.
				String role = CassandraUtils.getNodeRole(clusterConf.getNodes()
						.get(nodeConf).getRoles());

				if (nodeMonitoring != null && serviceStatus.get(role) != null
						&& serviceStatus.get(role) == true) {

					JmxUtil jmxUtil = getJMXConnection(nodeConf, jmxPort);
					if (jmxUtil == null) {
						continue;
					}

					Object cfset = getColumnFamilyList(keyspace, jmxUtil,
							columnFamily);
					Set cfset1 = (HashSet) cfset;

					if (cfset1 == null || cfset1.isEmpty()) {
						throw new AnkushException(
								"Either keyspace or Column Family Name is invalid.");
					}
					ObjectName mObjCF = new ObjectName(cfset1.iterator().next()
							.toString());
					Map<String, Object> attributes = jmxUtil
							.getAttributes(mObjCF);
					if (attributes == null || attributes.isEmpty()) {
						throw new AnkushException(
								"Could not get Columnfamily details.");
					}
					ColumnFamily cf = new ColumnFamily();
					cf.setColumnFamilyName(columnFamily);
					cf.setLiveSSTableCount(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LIVESSTABLECOUNT));
					cf.setWritelatency(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_WRITELATENCY));
					cf.setReadLatency(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_READLATENCY));
					cf.setPendingTasks(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_PENDINGTASKS));

					GeneralProperty generalProperties = setGeneralProprties(attributes);
					CompactionProperty compactionProperties = setCompactionProperties(attributes);
					CompressionProperty compressionProperties = setCompressionProperties(attributes);
					PerformanceTuningProperty performanceTuningProperties = setPerformanceTuningProperties(attributes);

					String columnMetadata = columnmetadata(keyspace,
							columnFamily);
					if (columnMetadata.isEmpty()) {
						throw new AnkushException(
								"Could not get Column metadata details.");

					}
					Map<String, Object> properties = new HashMap<String, Object>();
					properties.put("generalProperties", generalProperties);
					properties
							.put("compactionProperties", compactionProperties);
					properties.put("compressionProperties",
							compressionProperties);
					properties.put("performanceTuningProperties",
							performanceTuningProperties);
					properties.put("columnMetadata", columnMetadata);
					result.put("ColumnFamilyDetails", properties);
					return;
				}
			}
			addAndLogError("Unable to fetch Column Family information as all nodes are down.");
		} catch (AnkushException e) {
			addAndLogError(e.getMessage());
		} catch (Exception e) {
			addAndLogError("Could not get Column family details");
		}
	}

	private GeneralProperty setGeneralProprties(Map<String, Object> attributes) {
		GeneralProperty generalProperties = new GeneralProperty();
		try {
			generalProperties
					.setMinRowSize(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MINROWSIZE));
			generalProperties
					.setLiveDiskSpaceUsed(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LIVEDISKSPACEUSED));
			generalProperties
					.setReadCount(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_READCOUNT));
			generalProperties
					.setMeanRowSize(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEANROWSIZE));
			generalProperties
					.setMemtableSwitchCount(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEMTABLESWITCHCOUNT));
			generalProperties
					.setUnleveledSSTables(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_UNLEVELEDSSTABLES));
			generalProperties
					.setWriteCount(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_WRITECOUNT));
			generalProperties
					.setTotalDiskSpaceUsed(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOTALDISKSPACEUSED));
			generalProperties
					.setMemtableColumnsCount(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEMTABLECOLUMNSCOUNT));
			generalProperties
					.setMaxRowSize(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MAXROWSIZE));
			generalProperties
					.setMemtableDataSize(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MEMTABLEDATASIZE));
		} catch (Exception e) {
			addAndLogError("Could not get Column family details.");
		}
		return generalProperties;
	}

	private CompactionProperty setCompactionProperties(
			Map<String, Object> attributes) {
		CompactionProperty compactionProperties = new CompactionProperty();
		try {
			Object compactionStrategyClassObject = attributes
					.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_COMPACTIONSTRATEGYCLASS);
			String compactionStrategyClass;
			if (compactionStrategyClassObject != null
					&& !((String) compactionStrategyClassObject).isEmpty()) {
				List<String> compactionStrategyClassList = new ArrayList<String>(
						Arrays.asList(compactionStrategyClassObject.toString()
								.split("\\.")));
				compactionStrategyClass = compactionStrategyClassList
						.get(compactionStrategyClassList.size() - 1);
			} else {
				compactionStrategyClass = "";
			}
			compactionProperties
					.setMinCompactionThreshold(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MINCOMPACTIONTHRESHOLD));
			compactionProperties
					.setMaxCompactionThreshold(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_MAXCOMPACTIONTHRESHOLD));
			compactionProperties
					.setCompactionStrategyClass(compactionStrategyClass);
		} catch (Exception e) {
			addAndLogError("Could not get Column family details.");
		}
		return compactionProperties;
	}

	private CompressionProperty setCompressionProperties(
			Map<String, Object> attributes) {
		CompressionProperty compressionProperties = new CompressionProperty();
		try {
			Object compressionParameter = attributes
					.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_COMPRESSIONPARAMETERS);
			Map<String, String> compressionParams = (Map<String, String>) compressionParameter;
			String compressionType;
			if (compressionParams.containsKey("sstable_compression")) {
				String compressionTypeObject = compressionParams
						.get("sstable_compression");
				List<String> compressionTypeList = new ArrayList<String>(
						Arrays.asList(compressionTypeObject.toString().split(
								"\\.")));
				compressionType = compressionTypeList.get(compressionTypeList
						.size() - 1);
			} else {
				compressionType = "";
			}
			compressionProperties
					.setCompressionRatio(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_COMPRESSIONRATIO));
			compressionProperties.setSstable_compression(compressionType);
		} catch (Exception e) {
			addAndLogError("Could not get Column family details.");
		}
		return compressionProperties;
	}

	private PerformanceTuningProperty setPerformanceTuningProperties(
			Map<String, Object> attributes) {
		PerformanceTuningProperty performanceTuningProperties = new PerformanceTuningProperty();
		try {
			performanceTuningProperties
					.setBloomFilterFalsePositives(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERFALSEPOSITIVES));
			performanceTuningProperties
					.setBloomFilterDiskSpaceUsed(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERDISKSPACEUSED));
			performanceTuningProperties
					.setRecentReadLatencyMicros(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTREADLATENCYMICROS));
			performanceTuningProperties
					.setRecentBloomFilterFalseRatios(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTBLOOMFILTERFALSERATIOS));
			performanceTuningProperties
					.setDroppableTombStoneRatio(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_DROPPABLETOMBSTONERATIO));
			performanceTuningProperties
					.setBloomFilterFalseRatios(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERFALSERATIO));
			performanceTuningProperties
					.setTotalWriteLatencyMicros(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOTALWRITELATENCYMICROS));
			performanceTuningProperties
					.setRecentWriteLatencyMicros(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTWRITELATENCYMICROS));
			performanceTuningProperties
					.setRecentBloomFilterFalsePositives(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_RECENTBLOOMFILTERFALSEPOSITIVES));
			performanceTuningProperties
					.setTotalReadLatencyMicros(attributes
							.get(CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_TOTALREADLATENCYMICROS));
		} catch (Exception e) {
			addAndLogError("Could not get Column family details.");
		}
		return performanceTuningProperties;
	}

	private String columnmetadata(String keyspace, String columnfamily) {
		String errMsg = "Unable to fetch Column Family Information";
		try {
			String cassandraHome = componentConfig.getHomeDir();
			String fileName = cassandraHome + "columnmetadata";

			for (String host : CassandraUtils
					.getCassandraActiveNodes(componentConfig.getNodes()
							.keySet())) {
				String connectionPort = getRpcPort(cassandraHome, host);

				if (componentConfig.getVersion().contains("2.1.")) {
					connectionPort = getNativeTransportPort(cassandraHome, host);
				}
				String cmdFileContent2 = "echo \"select column_name , component_index,index_name, index_options, index_type, validator from system.schema_columns where keyspace_name = '"
						+ keyspace
						+ "'  and columnfamily_name='"
						+ columnfamily
						+ "';\" > "
						+ fileName
						+ " ; "
						+ cassandraHome
						+ "bin/cqlsh "
						+ host
						+ " "
						+ connectionPort + " -f " + fileName;

				SSHConnection sshConnection = new SSHConnection(host,
						clusterConf.getAuthConf().getUsername(), clusterConf
								.getAuthConf().isUsingPassword() ? clusterConf
								.getAuthConf().getPassword() : clusterConf
								.getAuthConf().getPrivateKey(), clusterConf
								.getAuthConf().isUsingPassword());
				sshConnection.exec(cmdFileContent2);
				if (sshConnection.getExitStatus() != 0) {
					throw new AnkushException(errMsg);
				} else {
					return sshConnection.getOutput();
				}
			}
			addAndLogError(errMsg);
		} catch (AnkushException e) {
			addAndLogError(e.getMessage());
		} catch (Exception e) {
			addAndLogError(errMsg);
		}
		return "";
	}

	// private void nodelist() {
	// Map<String, Map<String, Object>> hostMap = null;
	// try {
	// hostMap = new HashMap<String, Map<String, Object>>();
	// for (String host : componentConfig.getNodes().keySet()) {
	// Map<String, Object> hostDetails = new HashMap<String, Object>();
	// hostDetails.put("host", host);
	// if (!clusterConf.getNodes().get(host).getRoles()
	// .containsKey(Constant.Component.Name.CASSANDRA)) {
	// throw new AnkushException("Could not get role for node "
	// + host);
	// }
	// hostDetails.put("roles", clusterConf.getNodes().get(host)
	// .getRoles().get(Constant.Component.Name.CASSANDRA));
	// hostMap.put(host, hostDetails);
	// }
	// } catch (AnkushException e) {
	// addAndLogError(e.getMessage());
	// } catch (Exception e) {
	// addAndLogError("Could not get Cassandra node details. Please view server logs for more details.");
	// }
	// result.put("nodes", hostMap);
	// }

	private void nodelist() {
		String errMsg = "Could not get Cassandra node details. Please view server logs for more details.";
		Set<Map<String, Object>> hostSet = null;
		try {
			hostSet = new HashSet<Map<String, Object>>();
			for (String host : componentConfig.getNodes().keySet()) {
				Map<String, Object> hostDetails = new HashMap<String, Object>();
				hostDetails.put("host", host);
				if (!clusterConf.getNodes().get(host).getRoles()
						.containsKey(Constant.Component.Name.CASSANDRA)) {
					throw new AnkushException("Could not get role for node "
							+ host);
				}
				hostDetails.put("roles", clusterConf.getNodes().get(host)
						.getRoles().get(Constant.Component.Name.CASSANDRA));
				hostSet.add(hostDetails);
			}
		} catch (AnkushException e) {
			addAndLogError(e.getMessage());
		} catch (Exception e) {
			addAndLogError(errMsg);
		}
		if (hostSet != null) {
			result.put("nodes", hostSet);
		} else {
			addAndLogError(errMsg);
		}
	}

	@Override
	public boolean canNodesBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes, String componentName) {
		// TODO Auto-generated method stub
		return true;
	}
}
