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
package com.impetus.ankush.agent.oracle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.kv.impl.admin.CommandServiceAPI;
import oracle.kv.impl.admin.param.AdminParams;
import oracle.kv.impl.admin.param.Parameters;
import oracle.kv.impl.admin.param.RepNodeParams;
import oracle.kv.impl.monitor.views.PerfEvent;
import oracle.kv.impl.topo.AdminId;
import oracle.kv.impl.topo.Datacenter;
import oracle.kv.impl.topo.RepGroup;
import oracle.kv.impl.topo.RepNode;
import oracle.kv.impl.topo.RepNodeId;
import oracle.kv.impl.topo.ResourceId;
import oracle.kv.impl.topo.StorageNode;
import oracle.kv.impl.util.ConfigurableService.ServiceStatus;
import oracle.kv.impl.util.registry.RegistryUtils;
import oracle.kv.util.Ping;

import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.utils.TileInfo;

/**
 * The Class OracleNoSQLTechnologyData.
 * 
 * @author nikunj
 */
public class OracleNoSQLTechnologyData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant STATE_RUNNING. */
	private static final int STATE_RUNNING = 0;

	/** The Constant STATE_DOWN. */
	private static final int STATE_DOWN = 1;

	/** The Constant STATE_WARNING. */
	private static final int STATE_WARNING = 2;

	/** The datacenter list. */
	private List<OracleNoSQLDatacenter> datacenterList = new ArrayList<OracleNoSQLDatacenter>();

	/** The rep node list. */
	private List<OracleNoSQLRepNode> repNodeList = new ArrayList<OracleNoSQLRepNode>();

	/** The topology tree. */
	private Map topologyTree = new HashMap();

	/** The admin up. */
	private boolean adminUp = false;

	/** The active. */
	private int active = 0;

	/** The tiles. */
	private List<TileInfo> tiles = new ArrayList<TileInfo>();

	/**
	 * Instantiates a new oracle no sql technology data.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param registryPort
	 *            the registry port
	 */
	public OracleNoSQLTechnologyData(String hostname, int registryPort) {
		try {
			/* Get Administrative client using cluster admin node. */
			CommandServiceAPI cs = RegistryUtils.getAdmin(hostname,
					registryPort);
			adminUp = true;
			Map<Integer, OracleNoSQLStorageNode> snMap = addStorageNodes(cs);
			Map<Integer, List<OracleNoSQLRepNode>> rgMap = addRepGroups(cs,
					snMap);
			createTopologyTree(cs, rgMap);
			setTiles();
		} catch (Exception e) {
			// Create Admin node down tile
			TileInfo tileInfo = new TileInfo();
			tileInfo.setLine1("Admin Node");
			tileInfo.setLine2("Down");
			tileInfo.setLine3(hostname);
			tileInfo.setStatus(Constant.Tile.Status.CRITICAL);
			tiles.add(tileInfo);
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new oracle no sql technology data.
	 */
	public OracleNoSQLTechnologyData() {
		// empty technology constructor.
	}

	/**
	 * Gets the datacenter list.
	 * 
	 * @return the datacenterList
	 */
	public List<OracleNoSQLDatacenter> getDatacenterList() {
		return datacenterList;
	}

	/**
	 * Sets the datacenter list.
	 * 
	 * @param datacenterList
	 *            the datacenterList to set
	 */
	public void setDatacenterList(List<OracleNoSQLDatacenter> datacenterList) {
		this.datacenterList = datacenterList;
	}

	/**
	 * Gets the rep node list.
	 * 
	 * @return the repNodeList
	 */
	public List<OracleNoSQLRepNode> getRepNodeList() {
		return repNodeList;
	}

	/**
	 * Sets the rep node list.
	 * 
	 * @param repNodeList
	 *            the repNodeList to set
	 */
	public void setRepNodeList(List<OracleNoSQLRepNode> repNodeList) {
		this.repNodeList = repNodeList;
	}

	/**
	 * Gets the topology tree.
	 * 
	 * @return the topologyTree
	 */
	public Map getTopologyTree() {
		return topologyTree;
	}

	/**
	 * Sets the topology tree.
	 * 
	 * @param topologyTree
	 *            the topologyTree to set
	 */
	public void setTopologyTree(Map topologyTree) {
		this.topologyTree = topologyTree;
	}

	/**
	 * Checks if is admin up.
	 * 
	 * @return the adminUp
	 */
	public boolean isAdminUp() {
		return adminUp;
	}

	/**
	 * Sets the admin up.
	 * 
	 * @param adminUp
	 *            the adminUp to set
	 */
	public void setAdminUp(boolean adminUp) {
		this.adminUp = adminUp;
	}

	/**
	 * Gets the active.
	 * 
	 * @return the active
	 */
	public int getActive() {
		return active;
	}

	/**
	 * Sets the active.
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(int active) {
		this.active = active;
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
	 * Gets the status.
	 * 
	 * @param id
	 *            the id
	 * @param statusMap
	 *            the status map
	 * @return the status
	 */
	private int getStatus(ResourceId id,
			Map<ResourceId, ServiceStatus> statusMap) {
		ServiceStatus serviceStatus = statusMap.get(id);
		if (serviceStatus != null && serviceStatus.isAlive()) {
			return STATE_RUNNING;
		}
		return STATE_DOWN;
	}

	/**
	 * Adds the storage nodes.
	 * 
	 * @param cs
	 *            the cs
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	private Map<Integer, OracleNoSQLStorageNode> addStorageNodes(
			CommandServiceAPI cs) throws Exception {
		int id;
		OracleNoSQLStorageNode mySn = null;
		Map<Integer, OracleNoSQLStorageNode> snMap = new HashMap<Integer, OracleNoSQLStorageNode>();
		Map<Integer, OracleNoSQLDatacenter> dcMap = new HashMap<Integer, OracleNoSQLDatacenter>();
		Parameters params = cs.getParameters();

		// Set Datacenters
		for (Datacenter dc : cs.getTopology().getDatacenterMap().getAll()) {
			OracleNoSQLDatacenter myDc = new OracleNoSQLDatacenter(dc
					.getResourceId().getDatacenterId(), dc.getName(),
					dc.getRepFactor());
			datacenterList.add(myDc);
			dcMap.put(myDc.getDatacenterId(), myDc);
		}

		// Fetch status map
		Map<ResourceId, ServiceStatus> statusMap = Ping.getTopologyStatus(cs
				.getTopology());

		for (StorageNode sn : cs.getTopology().getSortedStorageNodes()) {
			// Fetch storage node parameters
			id = sn.getStorageNodeId().getStorageNodeId();

			// Create storage node and insert into map
			mySn = new OracleNoSQLStorageNode(id, sn.getRegistryPort(),
					sn.getHostname(), params.get(sn.getStorageNodeId())
							.getCapacity(), getStatus(sn.getStorageNodeId(),
							statusMap));
			dcMap.get(sn.getDatacenterId().getDatacenterId()).addStorageNode(
					mySn);
			snMap.put(id, mySn);
		}

		// Set admin ports
		Set<AdminId> aids = cs.getParameters().getAdminIds();
		for (AdminId aid : aids) {
			AdminParams ap = cs.getParameters().get(aid);
			mySn = snMap.get(ap.getStorageNodeId().getStorageNodeId());
			mySn.setAdminPort(ap.getHttpPort());
			mySn.setAdminId(aid.getAdminInstanceId());
		}

		return snMap;
	}

	/**
	 * Adds the rep groups.
	 * 
	 * @param cs
	 *            the cs
	 * @param snMap
	 *            the sn map
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	private Map<Integer, List<OracleNoSQLRepNode>> addRepGroups(
			CommandServiceAPI cs, Map<Integer, OracleNoSQLStorageNode> snMap)
			throws Exception {
		OracleNoSQLRepNode myRn;
		int rgId;
		int nodeNum;
		int port;
		String host;
		boolean master;
		long throughput = 0;

		Map<Integer, List<OracleNoSQLRepNode>> rgMap = new HashMap<Integer, List<OracleNoSQLRepNode>>();

		// Fetch status map
		Map<ResourceId, ServiceStatus> statusMap = Ping.getTopologyStatus(cs
				.getTopology());
		Map<ResourceId, PerfEvent> perfMap = cs.getPerfMap();

		/* Iterate Replication Groups */
		for (RepGroup rg : cs.getTopology().getRepGroupMap().getAll()) {
			rgId = rg.getResourceId().getGroupId();
			rgMap.put(rgId, new ArrayList<OracleNoSQLRepNode>());

			// Fetch master node
			RepNode masterRn = Ping.getMaster(cs.getTopology(),
					rg.getResourceId());

			/* Iterate Replication Nodes */
			for (RepNode rn : rg.getRepNodes()) {
				// Increase replication node count into storage node
				snMap.get(rn.getStorageNodeId().getStorageNodeId()).setRnCount(
						snMap.get(rn.getStorageNodeId().getStorageNodeId())
								.getRnCount() + 1);

				RepNodeId rnid = rn.getResourceId();
				RepNodeParams rnp = cs.getParameters().get(rnid);

				master = false;
				nodeNum = rnid.getNodeNum();
				port = Integer.parseInt(rnp.getNodeHostPort().split(":")[1]);
				host = rnp.getNodeHostPort().split(":")[0];

				if (masterRn != null
						&& masterRn.getResourceId().getNodeNum() == nodeNum) {
					master = true;
				}

				// Create local replication node and add into replication group
				// and map.
				myRn = new OracleNoSQLRepNode(rgId, rn.getStorageNodeId()
						.getStorageNodeId(), host, nodeNum, port, master,
						getStatus(rnid, statusMap), perfMap.get(rnid));

				// Add throughput
				throughput += myRn.throughput();

				rgMap.get(rgId).add(myRn);
				repNodeList.add(myRn);
			}
		}

		// Create throughput node tile
		TileInfo tileInfo = new TileInfo();
		tileInfo.setLine1(Long.valueOf(throughput).toString());
		tileInfo.setLine2("Throughput");
		tileInfo.setStatus(Constant.Tile.Status.NORMAL);
		tiles.add(tileInfo);

		return rgMap;
	}

	/**
	 * Generate topology graph based on service state.
	 * 
	 * @param cs
	 *            the cs
	 * @param rgMap
	 *            the rg map
	 * @throws Exception
	 *             the exception
	 */
	private void createTopologyTree(CommandServiceAPI cs,
			Map<Integer, List<OracleNoSQLRepNode>> rgMap) throws Exception {
		// Set replication group status
		int repFacotr = 0;
		int localRnUp;
		boolean masterUp;
		int shardDownCount = 0;
		int shardWarningCount = 0;
		int repNodeDownCount = 0;

		/* Fetch Replication Factor */
		if (cs.getTopology().getSortedStorageNodes() != null) {
			for (OracleNoSQLDatacenter dc : datacenterList) {
				repFacotr += dc.getRepFactor();
			}
		}

		List child = new ArrayList();
		/* Set RepGroup warn and up and update repnode */
		for (int rgId : rgMap.keySet()) {
			List shardChild = new ArrayList();
			masterUp = false;
			localRnUp = 0;
			for (OracleNoSQLRepNode myRn : rgMap.get(rgId)) {
				// Add repnode
				Map repNode = new HashMap();
				repNode.put("name", "RN" + myRn.getNodeNum());
				repNode.put("rn", myRn.getNodeNum());
				repNode.put("sn", myRn.getSnId());
				repNode.put("active", myRn.getActive());
				shardChild.add(repNode);
				if (myRn.getActive() == STATE_RUNNING) {
					localRnUp++;
					if (myRn.isMaster()) {
						masterUp = true;
					}
				} else {
					repNodeDownCount++;
				}
			}
			// Add Shard
			Map shard = new HashMap();
			shard.put("name", "Shard" + rgId);
			shard.put("shard", rgId);
			shard.put("children", shardChild);

			// Get shard status
			if (localRnUp == 0 || !masterUp) {
				shardDownCount++;
				shard.put("active", STATE_DOWN);
				active = STATE_DOWN;
			} else if (localRnUp != repFacotr) {
				shardWarningCount++;
				shard.put("active", STATE_WARNING);
				if (active != STATE_DOWN) {
					active = STATE_WARNING;
				}
			} else {
				shard.put("active", STATE_RUNNING);
			}

			child.add(shard);

		}

		/** Set topology name */
		List<String> topologies = cs.listTopologies();
		if (topologies != null && !topologies.isEmpty()) {
			topologyTree.put("name", topologies.get(topologies.size() - 1));
			topologyTree.put("children", child);
			topologyTree.put("active", active);
			topologyTree.put("childCount", repNodeList.size());
		}

		createTiles(rgMap.size(), shardDownCount, shardWarningCount,
				repNodeDownCount);
	}

	/**
	 * Creates the tiles.
	 * 
	 * @param totalShardCount
	 *            the total shard count
	 * @param shardDownCount
	 *            the shard down count
	 * @param shardWarningCount
	 *            the shard warning count
	 * @param repNodeDownCount
	 *            the rep node down count
	 */
	private void createTiles(int totalShardCount, int shardDownCount,
			int shardWarningCount, int repNodeDownCount) {
		// Create tiles
		TileInfo tileInfo = null;
		// Create RepNode down tile
		if (repNodeDownCount > 0) {
			tileInfo = new TileInfo();
			tileInfo.setLine1("Rep Node");
			tileInfo.setLine2("Down");
			tileInfo.setLine3(repNodeDownCount + " Nodes");
			tileInfo.setUrl("shardNodeTable");
			tileInfo.setStatus(Constant.Tile.Status.CRITICAL);
			tiles.add(tileInfo);
		}

		// Create shard down tile
		if (shardDownCount > 0) {
			tileInfo = new TileInfo();
			tileInfo.setLine1(Integer.valueOf(shardDownCount).toString());
			tileInfo.setLine2("Shards down");
			tileInfo.setLine3("Total " + totalShardCount + " shards");
			tileInfo.setUrl("shardNodeTable");
			tileInfo.setStatus(Constant.Tile.Status.ERROR);
			tiles.add(tileInfo);
		}

		// Create Shard warning tile
		if (shardWarningCount > 0) {
			tileInfo = new TileInfo();
			tileInfo.setLine1(Integer.valueOf(shardWarningCount).toString());
			tileInfo.setLine2("Shards down");
			tileInfo.setLine3("Total " + totalShardCount + " shards");
			tileInfo.setUrl("shardNodeTable");
			tileInfo.setStatus(Constant.Tile.Status.WARNING);
			tiles.add(tileInfo);
		}

		// Create shard count tile
		tileInfo = new TileInfo();
		tileInfo.setLine1(Integer.valueOf(totalShardCount).toString());
		tileInfo.setLine2("Shards");
		tileInfo.setUrl("shardNodeTable");
		tileInfo.setStatus(Constant.Tile.Status.NORMAL);
		tiles.add(tileInfo);
	}

	/**
	 * Sets the tiles.
	 */
	private void setTiles() {
		TileInfo tileInfo = null;

		int availableSNCount = 0;
		int downSNCount = 0;
		for (OracleNoSQLDatacenter dc : datacenterList) {
			for (OracleNoSQLStorageNode sn : dc.getStorageNodeList()) {
				if (sn.getActive() == STATE_DOWN) {
					downSNCount++;
				}
				if (sn.getRnCount() == 0) {
					availableSNCount++;
				}
			}
		}

		// Create storage node down tile
		if (downSNCount > 0) {
			tileInfo = new TileInfo();
			tileInfo.setLine1("Storage Node");
			tileInfo.setLine2("Down");
			tileInfo.setLine3(downSNCount + " Nodes");
			tileInfo.setUrl("storageNodeTable");
			tileInfo.setStatus(Constant.Tile.Status.CRITICAL);
			tiles.add(tileInfo);
		}

		// Create available storage node tile
		tileInfo = new TileInfo();
		tileInfo.setLine1(Integer.valueOf(availableSNCount).toString());
		tileInfo.setLine2("Storage Node Available");
		tileInfo.setUrl("storageNodeTable");
		tileInfo.setStatus(Constant.Tile.Status.NORMAL);
		tiles.add(tileInfo);
	}
}
