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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.JmxUtil;
import com.impetus.ankush2.cassandra.utils.CassandraConstants;
import com.impetus.ankush2.logger.AnkushLogger;

public class CassandraJMX {
	/** The logger. */
	private static AnkushLogger LOG = new AnkushLogger(CassandraJMX.class);
	private static DecimalFormat df = new DecimalFormat("###.##");

	private JmxUtil jmxUtil;
	private MBeanServerConnection connection;

	public CassandraJMX(String hostname, int jmxPort) throws Exception {

		jmxUtil = new JmxUtil(hostname, jmxPort);

		// connect to node
		connection = jmxUtil.connect();
		if (connection == null) {
			throw new AnkushException("Could not create JMX connection.");
		}
	}

	private List<String> getKeyspaces() {

		ObjectName mObjNameStorageService;
		try {
			mObjNameStorageService = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);
			Object attrKeyspace = jmxUtil
					.getAttribute(
							mObjNameStorageService,
							CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_KEYSPACES);

			return (List<String>) attrKeyspace;
		} catch (MalformedObjectNameException e) {
			LOG.error("Could not fetch keyspaces.", e);
		}
		return null;
	}

	private Map<String, String> getOwnershipMap() {
		Map<String, String> ownershipMap = null;

		try {
			ObjectName mObjNameStorageService = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);

			Object attrOwnership = jmxUtil
					.getAttribute(
							mObjNameStorageService,
							CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP);
			LinkedHashMap ownership = (LinkedHashMap) attrOwnership;

			ownershipMap = new HashMap<String, String>();
			for (Object key : ownership.keySet()) {
				ownershipMap
						.put(key.toString().substring(
								key.toString().lastIndexOf("/") + 1),
								String.valueOf(df.format(((Float) ownership
										.get(key)) * 100)) + " %");
			}

		} catch (MalformedObjectNameException e) {
			LOG.error("Could not get ownership details.", e);
		}

		return ownershipMap;
	}

	private Map<String, String> getLoadMap() {
		Map<String, String> loadMap = null;
		ObjectName mObjNameStorageService;
		try {
			mObjNameStorageService = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);
			// Getting load
			Object attrLoadMap = jmxUtil
					.getAttribute(
							mObjNameStorageService,
							CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LOADMAP);
			Map<String, String> load = (HashMap<String, String>) attrLoadMap;
			loadMap = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : load.entrySet()) {
				loadMap.put(entry.getKey().trim(), entry.getValue().trim());
			}
		} catch (MalformedObjectNameException e) {
			LOG.error("Could not get load details.", e);
		}
		return loadMap;
	}

	private Map<String, String> getHostIdMap() {
		Map<String, String> hostIdMap = null;
		try {
			ObjectName mObjNameStorageService = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);
			Object attrHostIdMap = jmxUtil
					.getAttribute(
							mObjNameStorageService,
							CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_HOSTIDMAP);
			Map<String, String> hostId = (HashMap<String, String>) attrHostIdMap;
			hostIdMap = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : hostId.entrySet()) {
				hostIdMap.put(entry.getKey().trim(), entry.getValue().trim());
			}
		} catch (MalformedObjectNameException e) {
			LOG.error("Could not get host Id details.", e);
		}
		return hostIdMap;
	}

	private Map<String, String> getStateMap() {
		Map<String, String> stateMap = null;
		try {
			ObjectName mObjNameFailureDetector = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "net:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_FAILUREDETECTOR);
			// Getting status
			Object attrStatus = jmxUtil
					.getAttribute(
							mObjNameFailureDetector,
							CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_SIMPLESTATES);
			Map<String, String> status = (Map) attrStatus;
			;
			stateMap = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : status.entrySet()) {

				stateMap.put(
						entry.getKey().substring(
								entry.getKey().lastIndexOf("/") + 1),
						entry.getValue());
			}
		} catch (MalformedObjectNameException e) {
			LOG.error("Could not get host Id details.", e);
		}
		return stateMap;
	}

	// TODO: Can be eliminated using keys retrun from above functions.
	private List<String> getNodeList() {
		List<String> nodes = null;

		try {
			ObjectName mObjNameStorageService = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);
			Object attributeLiveNodes = jmxUtil
					.getAttribute(
							mObjNameStorageService,
							CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES);
			Object attributeUnreachableNodes = jmxUtil
					.getAttribute(
							mObjNameStorageService,
							CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES);

			nodes = new ArrayList<String>();
			nodes.addAll((List<String>) attributeLiveNodes);
			nodes.addAll((List<String>) attributeUnreachableNodes);
		} catch (MalformedObjectNameException e) {
			LOG.error("Could not get node list.", e);
		}

		return nodes;
	}

	public List<CassandraNode> getCassandraNodeList() {
		List<CassandraNode> nodeList = null;
		Map<String, String> ownershipMap = getOwnershipMap();
		Map<String, String> loadMap = getLoadMap();
		Map<String, String> hostIdMap = getHostIdMap();
		Map<String, String> stateMap = getStateMap();
		List<String> nodes = getNodeList();

		if (ownershipMap == null || loadMap == null || hostIdMap == null
				|| stateMap == null || nodes == null) {
			LOG.error("Missing details.");
			return null;
		}

		try {

			ObjectName mObjNameStorageService = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_STORAGESERVICE);

			ObjectName mObjNameEndPointSnitchInfo = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "db:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_ENDPOINT_SNITCH_INFO);

			ObjectName mObjNameFailureDetector = new ObjectName(
					CassandraConstants.ORG_APACHE_CASSANDRA
							+ "net:type="
							+ CassandraConstants.Cassandra_JMX_Attributes.CASSANDRA_JMX_OBJECT_FAILUREDETECTOR);

			nodeList = new ArrayList<CassandraNode>();

			// getting topology information

			for (String cNodeConf : nodes) {

				Object opParams[] = { cNodeConf };
				String opSig[] = { String.class.getName() };

				CassandraNode node = new CassandraNode();
				Object result = connection
						.invoke(mObjNameEndPointSnitchInfo,
								CassandraConstants.JMX_Operations.CASSANDRA_JMX_ATTRIBUTE_GETRACK,
								opParams, opSig);
				node.setRack((String) result);

				result = connection
						.invoke(mObjNameEndPointSnitchInfo,
								CassandraConstants.JMX_Operations.CASSANDRA_JMX_ATTRIBUTE_GETDATACENTER,
								opParams, opSig);
				node.setDataCenter((String) result);

				node.setHost(cNodeConf);
				node.setOwnership(ownershipMap.get(cNodeConf));
				node.setLoad((loadMap.get(cNodeConf) != null
						&& !loadMap.get(cNodeConf).isEmpty() ? loadMap
						.get(cNodeConf) : "?"));
				node.setStatus(stateMap.get(cNodeConf));
				node.setHostId(hostIdMap.get(cNodeConf));

				result = connection
						.invoke(mObjNameFailureDetector,
								CassandraConstants.JMX_Operations.CASSANDRA_JMX_ATTRIBUTE_ENDPOINTSTATE,
								opParams, opSig);
				String strResult = (String) result;

				List<String> sysoutList = new ArrayList<String>(
						Arrays.asList(strResult.split("\n")));
				for (String outData : sysoutList) {
					List<String> paramList = new ArrayList<String>(
							Arrays.asList(outData.split(":")));
					String key = paramList.get(0).trim();
					if (key.equals("STATUS")) {
						node.setState((!paramList.get(1).trim().split(",")[0]
								.isEmpty() ? paramList.get(1).trim().split(",")[0]
								: "?"));
						break;
					}
				}

				// Getting tokens
				result = connection
						.invoke(mObjNameStorageService,
								CassandraConstants.JMX_Operations.CASSANDRA_JMX_ATTRIBUTE_TOKENS,
								opParams, opSig);
				List<String> tokens = (List<String>) result;
				node.setTokenCount(String.valueOf(tokens.size()));

				nodeList.add(node);
			}
		} catch (Exception e) {
			LOG.error("Could not get Cassandra node list.", e);
			nodeList = null;
		}

		return nodeList;
	}

	@Override
	protected void finalize() throws Throwable {
		if (jmxUtil != null) {
			jmxUtil.disconnect();
		}
		super.finalize();
	}
}
