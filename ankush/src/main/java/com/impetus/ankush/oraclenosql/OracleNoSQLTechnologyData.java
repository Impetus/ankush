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
package com.impetus.ankush.oraclenosql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.Tile;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.MonitoringListener;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;

/**
 * Technology specific data to show cluster overview.
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLTechnologyData implements TechnologyData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant STATE_RUNNING. */
	public static final int STATE_RUNNING = 0;

	/** The Constant STATE_DOWN. */
	public static final int STATE_DOWN = 1;

	/** The Constant STATE_WARNING. */
	public static final int STATE_WARNING = 2;

	/** The datacenter list. */
	private List<OracleNoSQLDatacenter> datacenterList = new ArrayList<OracleNoSQLDatacenter>();

	/** The replication node list. */
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
	 * Constructor.
	 */
	public OracleNoSQLTechnologyData() {
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
	 * Sets the add node tile.
	 * 
	 * @param newNodes
	 *            the new nodes
	 * @return the adds the node tile
	 */
	private TileInfo getAddNodeTile(List<NodeConf> newNodes) {
		try {
			if (newNodes != null && !newNodes.isEmpty()) {
				TileInfo tile = new TileInfo();
				tile.setLine1("Node Addition");
				tile.setLine2("is in progress");
				tile.setStatus(Constant.Tile.Status.NORMAL);
				String state = "progress";
				for (NodeConf node : newNodes) {
					String nodeState = node.getNodeState();
					if (nodeState != null) {
						if (nodeState.equals(Constant.Node.State.ERROR)) {
							state = "error";
							tile.setLine2("Failed");
							tile.setLine3(newNodes.size() + " Nodes");
							tile.setStatus(Constant.Tile.Status.CRITICAL);
							break;
						} else if (nodeState
								.equals(Constant.Node.State.DEPLOYED)) {
							tile.setLine2("Completed");
							state = "success";
						}
					}
				}
				tile.setUrl(Constant.Cluster.State.ADDING_NODES + "|" + state);
				return tile;
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * Get cluster overview.
	 * 
	 * @param conf
	 *            the conf
	 * @param newNodes
	 *            the new nodes
	 * @param dbCluster
	 *            the db cluster
	 * @return the cluster overview
	 */
	public Map getClusterOverview(OracleNoSQLConf conf,
			List<NodeConf> newNodes, Cluster dbCluster) {
		Map map = new HashMap();

		map.put("shards", repNodeList);

		map.put("datacenters", datacenterList);

		// Add topology tree
		map.put("tree", topologyTree);

		// Add tiles
		List t = new ArrayList<TileInfo>();
		t.addAll(tiles);
		TileManager tileManager = new TileManager();
		t.addAll(tileManager.getClusterEventTypeTiles(dbCluster,
				Constant.Alerts.Type.USAGE));
		TileInfo tileinfo = getAddNodeTile(newNodes);
		if (tileinfo != null) {
			t.add(tileinfo);
		}
		List<Tile> tilesRecords = tileManager.readClusterTile(conf
				.getClusterDbId());
		if (tilesRecords != null) {
			for (Tile tr : tilesRecords) {
				t.add(tr.getTileInfoObj());
			}
		}

		map.put("tiles", t);
		map.put("storename", conf.getClusterName());

		return map;
	}

	/**
	 * Get cluster specific technology data.
	 * 
	 * @param conf
	 *            the conf
	 * @return the technology data
	 */
	public static OracleNoSQLTechnologyData getTechnologyData(
			OracleNoSQLConf conf) {
		OracleNoSQLTechnologyData techData = null;

		if (conf == null) {
			return techData;
		}

		NodeMonitoring nodeMonitoring;
		for (OracleNoSQLNodeConf nodeConf : conf.getNodes()) {
			if (nodeConf.isAdmin()) {
				nodeMonitoring = new MonitoringManager()
						.getMonitoringData(nodeConf.getPublicIp());

				if (nodeMonitoring != null
						&& nodeMonitoring.getTechnologyData() != null) {
					techData = (OracleNoSQLTechnologyData) nodeMonitoring
							.getTechnologyData().get(
									Constant.Technology.ORACLE_NOSQL);
					if (techData != null
							&& !techData.getDatacenterList().isEmpty()) {
						break;
					}
				}
			}
		}

		setDefaultStorageNodeList(conf, techData);
		return techData;
	}

	/**
	 * If admin node is down, set storage node list using nodes from database.
	 * 
	 * @param conf
	 *            the conf
	 * @param techData
	 *            the tech data
	 */
	private static void setDefaultStorageNodeList(OracleNoSQLConf conf,
			OracleNoSQLTechnologyData techData) {
		if (techData != null && techData.getDatacenterList().isEmpty()) {
			List<OracleNoSQLStorageNode> storageNodeList = new ArrayList<OracleNoSQLStorageNode>();
			OracleNoSQLDatacenter dc = new OracleNoSQLDatacenter();
			dc.setName(conf.getDatacenterName());
			for (OracleNoSQLNodeConf nodeConf : conf.getNodes()) {
				OracleNoSQLStorageNode sn = new OracleNoSQLStorageNode();
				sn.setActive(STATE_DOWN);
				if (nodeConf.getAdminPort() != null) {
					sn.setAdminPort(nodeConf.getAdminPort());
				}
				sn.setCapacity(nodeConf.getCapacity());
				sn.setHostname(nodeConf.getPublicIp());
				sn.setRegistryPort(nodeConf.getRegistryPort());
				sn.setRnCount(0);
				sn.setSnId(nodeConf.getSnId());
				storageNodeList.add(sn);
			}
			dc.setStorageNodeList(storageNodeList);
			List<OracleNoSQLDatacenter> datacenterList = new ArrayList<OracleNoSQLDatacenter>();
			datacenterList.add(dc);
			techData.setDatacenterList(datacenterList);
		}
	}

	/**
	 * Gets the storage nodes status.
	 * 
	 * @return the storage nodes status
	 */
	public Map<Integer, Boolean> getStorageNodesStatus() {
		Map<Integer, Boolean> statusMap = new HashMap<Integer, Boolean>();
		for (OracleNoSQLDatacenter dc : datacenterList) {
			for (OracleNoSQLStorageNode sn : dc.getStorageNodeList()) {
				statusMap.put(sn.getSnId(), sn.getActive() == STATE_RUNNING);
			}
		}
		return statusMap;
	}

	/**
	 * Gets the available storage node count.
	 * 
	 * @return the available storage node count
	 */
	public int getAvailableStorageNodeCount() {
		int availableSNCount = 0;
		for (OracleNoSQLDatacenter dc : datacenterList) {
			for (OracleNoSQLStorageNode sn : dc.getStorageNodeList()) {
				if (sn.getRnCount() == 0) {
					availableSNCount++;
				}
			}
		}
		return availableSNCount;
	}

	/**
	 * Gets the replication factor.
	 * 
	 * @return the replication factor
	 */
	public int getReplicationFactor() {
		int replicationFactor = 0;
		for (OracleNoSQLDatacenter dc : datacenterList) {
			replicationFactor += dc.getRepFactor();
		}
		return replicationFactor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.TechnologyData#getTechnologyName()
	 */
	@Override
	public String getTechnologyName() {
		return Constant.Technology.ORACLE_NOSQL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.TechnologyData#getMonitoringListener
	 * ()
	 */
	@Override
	public MonitoringListener getMonitoringListener() {
		// TODO Auto-generated method stub
		return null;
	}
}
