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
package com.impetus.ankush.agent.cassandra;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.JmxUtil;
import com.impetus.ankush.agent.utils.TileInfo;

public class CassandraJMXData implements Serializable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(CassandraJMXData.class);
	/** The tiles. */
	private List<TileInfo> tiles = new ArrayList<TileInfo>();

	/** The datacenters. */
	private List<Datacenter> datacenters = new ArrayList<Datacenter>();

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant ORG_APACHE_CASSANDRA. */
	private static final String ORG_APACHE_CASSANDRA = "org.apache.cassandra.";

	/** The Constant CASSANDRA_JMX_OBJECT_FAILUREDETECTOR. */
	private static final String CASSANDRA_JMX_OBJECT_FAILUREDETECTOR = "FailureDetector";

	/** The Constant CASSANDRA_JMX_SIMPLESTATES. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_SIMPLESTATES = "SimpleStates";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP = "Ownership";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_GETRACK. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_GETRACK = "getRack";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_GETDATACENTER. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_GETDATACENTER = "getDatacenter";

	/** The Constant CASSANDRA_JMX_OBJECT_STORAGESERVICE. */
	private static final String CASSANDRA_JMX_OBJECT_STORAGESERVICE = "StorageService";

	/** The Constant CASSANDRA_JMX_OBJECT_ENDPOINT_SNITCH_INFO. */
	private static final String CASSANDRA_JMX_OBJECT_ENDPOINT_SNITCH_INFO = "EndpointSnitchInfo";

	/** The Constant CASSANDRA_JMX_OBJECT_DYNAMICENDPOINTSNITCH. */
	private static final String CASSANDRA_JMX_OBJECT_DYNAMICENDPOINTSNITCH = "DynamicEndpointSnitch";

	/** The Constant CASSANDRA_JMX_ENDPOINTSTATE. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_ENDPOINTSTATE = "getEndpointState";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_LOADMAP. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_LOADMAP = "LoadMap";

	private static final String CASSANDRA_JMX_ATTRIBUTE_HOSTIDMAP = "HostIdMap";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_TOKENS. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_TOKENS = "getTokens";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES = "LiveNodes";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES = "UnreachableNodes";

	/** The Constant CASSANDRA_JMX_ATTRIBUTE_KEYSPACES. */
	private static final String CASSANDRA_JMX_ATTRIBUTE_KEYSPACES = "Keyspaces";

	public CassandraJMXData(String hostname, Integer jmxPort) {

		try {
			createTopologyRing(hostname, jmxPort);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Instantiates a new cassandra JMX data.
	 */
	public CassandraJMXData() {
		// empty technology constructor.
	}

	private void createTopologyRing(String hostname, int jmxPort) {

		JmxUtil jmxUtil = null;

		try {

			jmxUtil = new JmxUtil(hostname, jmxPort);

			// connect to node
			MBeanServerConnection connection = jmxUtil.connect();

			if (connection != null) {

				clusterTiles(jmxUtil);

				Map<String, String> ownershipMap = new HashMap<String, String>();
				Map<String, String> loadMap = new HashMap<String, String>();
				Map<String, String> hostIdMap = new HashMap<String, String>();
				Map<String, String> stateMap = new HashMap<String, String>();

				// getting nodes
				List<String> nodes = new ArrayList<String>();
				ObjectName mObjNameStorageService = new ObjectName(
						ORG_APACHE_CASSANDRA + "db:type="
								+ CASSANDRA_JMX_OBJECT_STORAGESERVICE);

				ObjectName mObjNameEndPointSnitchInfo = new ObjectName(
						ORG_APACHE_CASSANDRA + "db:type="
								+ CASSANDRA_JMX_OBJECT_ENDPOINT_SNITCH_INFO);

				Object attributeLiveNodes = jmxUtil.getAttribute(
						mObjNameStorageService,
						CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES);
				Object attributeUnreachableNodes = jmxUtil.getAttribute(
						mObjNameStorageService,
						CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES);

				ObjectName mObjNameFailureDetector = new ObjectName(
						ORG_APACHE_CASSANDRA + "net:type="
								+ CASSANDRA_JMX_OBJECT_FAILUREDETECTOR);

				// Getting ownership
				Object attrOwnership = jmxUtil.getAttribute(
						mObjNameStorageService,
						CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP);
				LinkedHashMap ownership = (LinkedHashMap) attrOwnership;
				DecimalFormat df = new DecimalFormat("###.##");
				for (Object key : ownership.keySet()) {
					ownershipMap.put(
							key.toString().substring(
									key.toString().lastIndexOf("/")),
							String.valueOf(df.format(((Float) ownership
									.get(key)) * 100)) + " %");
				}

				// Getting load
				Object attrLoadMap = jmxUtil
						.getAttribute(mObjNameStorageService,
								CASSANDRA_JMX_ATTRIBUTE_LOADMAP);
				Map<String, String> load = (HashMap<String, String>) attrLoadMap;
				for (Map.Entry<String, String> entry : load.entrySet()) {
					loadMap.put(entry.getKey().trim(), entry.getValue().trim());
				}

				// Getting hostID map
				Object attrHostIdMap = jmxUtil.getAttribute(
						mObjNameStorageService,
						CASSANDRA_JMX_ATTRIBUTE_HOSTIDMAP);
				Map<String, String> hostId = (HashMap<String, String>) attrHostIdMap;
				for (Map.Entry<String, String> entry : hostId.entrySet()) {
					hostIdMap.put(entry.getKey().trim(), entry.getValue()
							.trim());
				}

				// Getting status
				Object attrStatus = jmxUtil.getAttribute(
						mObjNameFailureDetector,
						CASSANDRA_JMX_ATTRIBUTE_SIMPLESTATES);
				Map<String, String> status = (Map) attrStatus;
				String nodeOwnership = new String();
				for (Map.Entry<String, String> entry : status.entrySet()) {

					stateMap.put(
							entry.getKey().substring(
									entry.getKey().lastIndexOf("/")),
							entry.getValue());
				}
				nodes.addAll((List<String>) attributeLiveNodes);
				nodes.addAll((List<String>) attributeUnreachableNodes);

				List<Node> nodeList = new ArrayList<Node>();

				// getting topology information

				for (String cNodeConf : nodes) {

					Object opParams[] = { cNodeConf };
					String opSig[] = { String.class.getName() };

					Node node = new Node();
					Object result = connection.invoke(
							mObjNameEndPointSnitchInfo,
							CASSANDRA_JMX_ATTRIBUTE_GETRACK, opParams, opSig);
					node.setRack((String) result);

					result = connection.invoke(mObjNameEndPointSnitchInfo,
							CASSANDRA_JMX_ATTRIBUTE_GETDATACENTER, opParams,
							opSig);
					node.setDataCenter((String) result);

					node.setHost(cNodeConf);
					node.setOwnership(ownershipMap.get(cNodeConf));
					node.setLoad((loadMap.get(cNodeConf) != null
							&& !loadMap.get(cNodeConf).isEmpty() ? loadMap
							.get(cNodeConf) : "?"));
					node.setStatus(stateMap.get(cNodeConf));
					node.setHostId(hostIdMap.get(cNodeConf));

					result = connection.invoke(mObjNameFailureDetector,
							CASSANDRA_JMX_ATTRIBUTE_ENDPOINTSTATE, opParams,
							opSig);
					String strResult = (String) result;

					List<String> sysoutList = new ArrayList<String>(
							Arrays.asList(strResult.split("\n")));
					for (String outData : sysoutList) {
						List<String> paramList = new ArrayList<String>(
								Arrays.asList(outData.split(":")));
						String key = paramList.get(0).trim();
						if (key.equals("STATUS")) {
							node.setState((!paramList.get(1).trim().split(",")[0]
									.isEmpty() ? paramList.get(1).trim()
									.split(",")[0] : "?"));
							break;
						}
					}

					// Getting tokens
					result = connection.invoke(mObjNameStorageService,
							CASSANDRA_JMX_ATTRIBUTE_TOKENS, opParams, opSig);
					List<String> tokens = (List<String>) result;
					node.setTokenCount(String.valueOf(tokens.size()));

					nodeList.add(node);
				}

				Map<String, Map<String, List<Node>>> datacenterMap = new HashMap<String, Map<String, List<Node>>>();

				for (Node node : nodeList) {
					if (!datacenterMap.containsKey(node.getDataCenter())) {
						datacenterMap.put(node.getDataCenter(),
								new HashMap<String, List<Node>>());
					}
					if (!datacenterMap.get(node.getDataCenter()).containsKey(
							node.getRack())) {
						datacenterMap.get(node.getDataCenter()).put(
								node.getRack(), new ArrayList<Node>());
					}
					datacenterMap.get(node.getDataCenter()).get(node.getRack())
							.add(node);
				}

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
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			jmxUtil.disconnect();
		}
	}

	private void clusterTiles(JmxUtil jmxUtil) {
		try {

			ObjectName mObjNameStorageService = new ObjectName(
					ORG_APACHE_CASSANDRA + "db:type="
							+ CASSANDRA_JMX_OBJECT_STORAGESERVICE);

			ObjectName mObjNameDynamicEndpointSnitch = new ObjectName(
					ORG_APACHE_CASSANDRA + "db:type="
							+ CASSANDRA_JMX_OBJECT_DYNAMICENDPOINTSNITCH);

			// Get keyspace count
			Object attrKeyspace = jmxUtil.getAttribute(mObjNameStorageService,
					CASSANDRA_JMX_ATTRIBUTE_KEYSPACES);
			int keyspaceCount = ((List<String>) attrKeyspace).size();

			String keyspace = "Keyspace";
			if (keyspaceCount > 1) {
				keyspace = CASSANDRA_JMX_ATTRIBUTE_KEYSPACES;
			}

			// Create tiles
			if (keyspaceCount > 0) {
				tiles.add(getTile(String.valueOf(keyspaceCount), keyspace,
						"Keyspace"));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	private TileInfo getTile(String line1, String line2, String url) {
		TileInfo tileInfo = null;
		try {
			tileInfo = new TileInfo();
			tileInfo.setLine1(line1);
			tileInfo.setLine2(line2);
			tileInfo.setStatus(Constant.Tile.Status.NORMAL);
			tileInfo.setUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tileInfo;
	}

	/**
	 * Gets the tiles.
	 * 
	 * @return the tiles
	 */
	public List<TileInfo> getTiles() {
		return tiles;
	}

	/**
	 * Sets the tiles.
	 * 
	 * @param tiles
	 *            the tiles to set
	 */
	public void setTiles(List<TileInfo> tiles) {
		this.tiles = tiles;
	}

	/**
	 * Gets the datacenters.
	 * 
	 * @return the datacenters
	 */
	public List<Datacenter> getDatacenters() {
		return datacenters;
	}

	/**
	 * Sets the datacenters.
	 * 
	 * @param datacenters
	 *            the datacenters to set
	 */
	public void setDatacenters(List<Datacenter> datacenters) {
		this.datacenters = datacenters;
	}
}
