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
package com.impetus.ankush.hadoop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.util.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.cassandra.CassandraJMXData;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.tiles.TileViewHandler;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.dfs.DFSDataNodesInfo;
import com.impetus.ankush.hadoop.dfs.DFSReport;
import com.impetus.ankush.hadoop.dfs.HadoopDFSManager;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.job.HadoopJobsManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;


// TODO: Auto-generated Javadoc
/**
 * The Class HadoopTileViewHandler.
 * 
 * @author bgunjan
 */
public class HadoopTileViewHandler extends TileViewHandler {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			HadoopTileViewHandler.class);

	public static final String STRING_EMPTY_VALUE = "--";
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.core.tiles.TileViewHandler#getClusterDetailsTiles(
	 * com.impetus.ankush.common.domain.Cluster, java.util.Map)
	 */
	@Override
	public List<TileInfo> getClusterDetailsTiles(Cluster cluster, Map parameter) {
		List<TileInfo> clusterTiles = new ArrayList<TileInfo>();

		// hadoop cluster conf object.
		HadoopClusterConf conf = ((HadoopClusterConf) cluster.getClusterConf());
		logger.info("inside getClusterDetailsTiles");
		// Hadoop Cluster Data
		getHadoopClusterData(cluster, clusterTiles);

		// Hadoop Monitoring Data
		//clusterTiles.addAll(getHadoopMonitoringTiles(cluster));
		
		// add node tile
		setAddNodeTile(conf, clusterTiles);

		// remove node tile
		setRemoveNodeTile(cluster, clusterTiles);

		Collections.reverse(clusterTiles);
		return clusterTiles;
	}

	/**
	 * Sets the add node tile.
	 * 
	 * @param conf
	 *            the conf
	 * @param clusterTiles
	 *            the cluster tiles
	 */
	private void setAddNodeTile(HadoopClusterConf conf,
			List<TileInfo> clusterTiles) {
		TileInfo tileInfo = new TileInfo();
		try {
			
			if (conf != null && conf.getNewNodes() != null) {
				List<HadoopNodeConf> nodes = conf.getNewNodes();
				String line1 = "Node Addition";
				String line2 = "is in progress";
				String line3 = null;
				String status = Constant.Tile.Status.NORMAL;
				String state = "progress";
				String errorLine = nodes.size() + " Nodes";
				for (NodeConf node : nodes) {
					node = (HadoopNodeConf) node;
					String nodeState = node.getNodeState();
					if (nodeState != null) {
						if (nodeState.equals(Constant.Node.State.ERROR)) {
							line2 = "Failed";
							line3 = errorLine;
							status = Constant.Tile.Status.CRITICAL;
							state = "error";
							break;
						} else if (nodeState
								.equals(Constant.Node.State.DEPLOYED)) {
							line2 = "Completed";
							state = "success";
						}
					}
				}
				tileInfo.setLine1(line1);
				tileInfo.setLine2(line2);
				tileInfo.setLine3(line3);
				tileInfo.setUrl(Constant.Cluster.State.ADDING_NODES + "|"
						+ state);
				tileInfo.setData(null);
				tileInfo.setStatus(status);
				clusterTiles.add(tileInfo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Sets the remove node tile.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param clusterTiles
	 *            the cluster tiles
	 */
	private void setRemoveNodeTile(Cluster cluster, List<TileInfo> clusterTiles) {
		try {
			HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) cluster
					.getClusterConf();
			int nodeRemoving = 0;
			if (hadoopClusterConf != null) {
				List<HadoopNodeConf> nodes = hadoopClusterConf.getNodes();
				for (HadoopNodeConf hnc : nodes) {
					if (hnc.getNodeState().equals(Constant.Node.State.REMOVING)) {
						nodeRemoving++;
					}
				}
				if (nodeRemoving > 0) {
					String line1 = nodeRemoving + " Node Remove";
					String line2 = "is in progress";
					String line3 = null;
					String status = Constant.Tile.Status.NORMAL;

					TileInfo tileInfo = new TileInfo();
					tileInfo.setLine1(line1);
					tileInfo.setLine2(line2);
					tileInfo.setLine3(line3);
					tileInfo.setUrl(Constant.Tile.Url.NODE_LIST);
					tileInfo.setData(null);
					tileInfo.setStatus(status);
					clusterTiles.add(tileInfo);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Gets the node dfs data.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param nodeIp
	 *            the node ip
	 * @param nodeTiles
	 *            the node tiles
	 * @return the node dfs data
	 */
	private void getNodeDFSData(Cluster cluster, String nodeIp,
			List<TileInfo> nodeTiles) {
		String tileStatus = Constant.Tile.Status.NORMAL;
		TileInfo tileInfo = new TileInfo();
		boolean serviceStatus = true;
		if (!serviceStatus) {
			tileStatus = Constant.Tile.Status.ERROR;
			tileInfo = getTileInfo("Down", "NameNode",
					"Could not get dfs data", null, tileStatus, null);
			nodeTiles.add(tileInfo);
			return;
		} else {
			HadoopMonitoringData hadoopMonitoringData = getHadoopMonitoringData(cluster);
			DFSReport dfsInfos = hadoopMonitoringData.getDfsInfos();
			
			try {
				if (dfsInfos != null) {
					String dfsRemaining = "0";
					List<DFSDataNodesInfo> allNodesInfo = dfsInfos
							.getAllNodeInfo();
					for (DFSDataNodesInfo dfsDN : allNodesInfo) {
						
						String nodeHost = dfsDN.getDatanodeName();
						if (nodeHost != null && nodeHost.contains(":")) {
							String dnIp = nodeHost.split(":")[0];
							if (dnIp.equals(nodeIp)) {
								String configureddfs = dfsDN
										.getConfiguredCapacity();
								tileInfo = getTileInfo(configureddfs,
										"Configured DFS", null, null,
										tileStatus, null);
								nodeTiles.add(tileInfo);
								
								dfsRemaining = dfsDN.getDfsRemaining();
								tileInfo = getTileInfo(dfsRemaining,
										"HDFS is Free", null, null, tileStatus,
										null);
								nodeTiles.add(tileInfo);

								break;
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.core.tiles.TileViewHandler#getNodeDetailsTiles(com
	 * .impetus.ankush.domain.NewCluster, java.util.Map)
	 */
	@Override
	public List<TileInfo> getNodeDetailsTiles(Cluster cluster, Map parameter) {
		List<TileInfo> nodeTiles = new ArrayList<TileInfo>();
		String nodeIp = (String) parameter.get(Constant.Keys.IP);

		try {
			Node hadoopNode = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodeIp);
			Long nodeId = hadoopNode.getId();

			if (nodeId != null) {
				HadoopNodeConf hadoopNodeConf = (HadoopNodeConf) hadoopNode
						.getNodeConf();

				// Hadoop node type tiles for specified node
				addNodeRoleTile(hadoopNodeConf, nodeTiles);

				// Get the db node monitoring info
				NodeMonitoring nodeMonitoring = monitoringManager
						.getByPropertyValueGuarded(Constant.Keys.NODEID, nodeId);

				if (!nodeMonitoring.isAgentDown()) {
					
					// DFS data tiles for specified node
					getNodeDFSData(cluster, nodeIp, nodeTiles);
					logger.info("nodeTiles :" + nodeTiles);
					// Event data for nodes
					nodeTiles.addAll(new TileManager().getNodeEventTiles(
							cluster.getId(), hadoopNodeConf.getPublicIp()));
				} else {
					TileInfo errorTile = new TileInfo(Constant.Keys.DOWN,
							Constant.Role.AGENT,
							"Could not fetch node data.", null,
							Constant.Tile.Status.ERROR, null);
					nodeTiles.add(errorTile);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		Collections.reverse(nodeTiles);
		return nodeTiles;
	}

	/**
	 * Gets the node role tiles.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param nodeTiles
	 *            the node tiles
	 * @return the node role tiles
	 */
	private void addNodeRoleTile(HadoopNodeConf nodeConf,
			List<TileInfo> nodeTiles) {
		String tileStatus = Constant.Tile.Status.NORMAL;

		try {
			Set<String> nodeTypes = new HashSet<String>();
			String nodeType = "";
			if (nodeConf.getNameNode()) {
				nodeTypes.add("NN");
			}
			if (nodeConf.getSecondaryNameNode()) {
				nodeTypes.add("SNN");
			}
			if (nodeConf.getDataNode()) {
				nodeTypes.add("DN");
			}
			if (nodeConf.getStandByNameNode()) {
				nodeTypes.add("NN");
			}
			nodeType = StringUtils.collectionToDelimitedString(nodeTypes, "/");

			TileInfo tileInfo = getTileInfo(nodeType, "Type", null, null,
					tileStatus, null);
			nodeTiles.add(tileInfo);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Gets the hadoop monitoring data.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return the hadoop monitoring data
	 */
	private HadoopMonitoringData getHadoopMonitoringData(Cluster cluster) {
		HadoopMonitoringData hadoopmonitoringData = new HadoopMonitoringData();
		try {
			String nodeIp = "";
			HadoopClusterConf hadoopClusterConf = ((HadoopClusterConf) cluster
					.getClusterConf());
			nodeIp = hadoopClusterConf.getNameNode().getPublicIp();

			// Get the db node info using public IP
			Node hadoopNode = nodeManager.getByPropertyValueGuarded("publicIp",
					nodeIp);
			if (hadoopNode != null) {
				Long nodeId = hadoopNode.getId();

				// Get the db node monitoring info
				NodeMonitoring nodeMonitoring = monitoringManager
						.getByPropertyValueGuarded("nodeId", nodeId);
				if (nodeMonitoring != null) {
					hadoopmonitoringData = (HadoopMonitoringData) nodeMonitoring
							.getTechnologyData()
							.get(Constant.Technology.HADOOP);
				}
			}
		} catch (Exception e) {

		}
		return hadoopmonitoringData;
	}

	/**
	 * Gets the hadoop cluster data.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param clusterTiles
	 *            the cluster tiles
	 * @return the hadoop cluster data
	 */
	private void getHadoopClusterData(Cluster cluster,
			List<TileInfo> clusterTiles) {
		try {
			HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) cluster
					.getClusterConf();
			
			Map<String, Object> data = null;
			int hesCount = 0;
			int dnCount = 0;
			String status = Constant.Tile.Status.NORMAL;

			// Hadoop Ecosystem Component Size
			LinkedHashMap<String, GenericConfiguration> components = hadoopClusterConf
					.getComponents();

			List<HadoopNodeConf> nodes = hadoopClusterConf.getNodes();
			for (HadoopNodeConf hadoopNodeConf : nodes) {
				if (hadoopNodeConf.getDataNode()) {
					dnCount++;
				}
			}

			TileInfo tileInfo = null;

			tileInfo = new TileInfo(dnCount + "", "Data Nodes", null, data,
					status, Constant.Tile.Url.NODE_LIST);
			clusterTiles.add(tileInfo);

			hesCount = components.size() - 1;
			tileInfo = new TileInfo(hesCount + "", "Deployed Ecosystems", null,
					data, status, "Ecosystem");
			clusterTiles.add(tileInfo);

			if (components.containsKey(Constant.Component.Name.HADOOP2)) {
				Hadoop2Conf hConf2 = (Hadoop2Conf) components
						.get(Constant.Component.Name.HADOOP2);

				if(hConf2.isStartJobHistoryServer()) {
					tileInfo = new TileInfo(hConf2.getJobHistoryServerNode()
							.getPublicIp(), "JobHistoryServer", null, null, status,
							Constant.Tile.Url.NODE_LIST);
					clusterTiles.add(tileInfo);
				}
				
				if(hConf2.isWebProxyEnabled()) {
					tileInfo = new TileInfo(hConf2.getWebAppProxyNode()
							.getPublicIp(), "WebAppProxyServer", null, null, status,
							Constant.Tile.Url.NODE_LIST);
					clusterTiles.add(tileInfo);
				}
				
				if (hConf2.isHaEnabled()) {
					// Tile for HA Enabled
					tileInfo = new TileInfo("Enabled", "HA", null, null,
							Constant.Tile.Status.NORMAL, "");
					clusterTiles.add(tileInfo);
				}
			} 
			logger.info("clusterTiles : " + clusterTiles);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * Gets the hadoop non ha tiles.
	 *
	 * @param hConf the h conf
	 * @return the hadoop non ha tiles
	 */
	private List<TileInfo> getHadoopNonHATiles(HadoopConf hConf) {
		List<TileInfo> hadoopNonHATiles = new ArrayList<TileInfo>();
		
		
		TileInfo tileInfo = null;
		
		if(hConf.getSecondaryNamenode() != null) {
			tileInfo = new TileInfo(hConf.getSecondaryNamenode().getPublicIp(), "Secondary NameNode", 
									null, null, Constant.Tile.Status.NORMAL, Constant.Tile.Url.NODE_LIST);
			hadoopNonHATiles.add(tileInfo);
		}
		
		tileInfo = new TileInfo(hConf.getNamenode().getPublicIp(), "NameNode", 
								null, null, Constant.Tile.Status.NORMAL, Constant.Tile.Url.NODE_LIST);
		hadoopNonHATiles.add(tileInfo);
		
		return hadoopNonHATiles;
	}

	public static Map<String, String> getHadoopNodesSummary(HadoopConf hConf, boolean isHadoop2) {
		Map<String, String> nodesSummary = null;
		if(isHadoop2) {
			Hadoop2Conf h2Conf = (Hadoop2Conf) hConf;
			nodesSummary = HadoopTileViewHandler.getHadoop2NodesSummary(h2Conf);
		} else {
			nodesSummary = HadoopTileViewHandler.getHadoop1NodesSummary(hConf);
		}
		return nodesSummary;
	}
	
	private static Map<String, String> getHadoop1NodesSummary(HadoopConf hConf) {
		Map<String, String> hadoop1NodesSummary = new LinkedHashMap<String, String>();
		hadoop1NodesSummary.put(Constant.Role.JOBTRACKER, hConf.getNamenode().getPublicIp());
		hadoop1NodesSummary.putAll(HadoopTileViewHandler.getHadoopNonHANodesSummary(hConf));
		
		hadoop1NodesSummary.put(Constant.Hadoop.Keys.DISPLAY_NAME_LIVE_DATANODES_COUNT, 
								HadoopDFSManager.getLiveDataNodesCount(hConf));
		hadoop1NodesSummary.put(Constant.Hadoop.Keys.DISPLAY_NAME_ACTIVE_TASKTRACKERS_COUNT, 
								HadoopJobsManager.getActiveTaskTrackersCount(hConf));
		return hadoop1NodesSummary;
	}
	
	private static Map<String, String> getHadoopNonHANodesSummary(HadoopConf hConf) {
		Map<String, String> nonHANodeSummary = new LinkedHashMap<String, String>();
		nonHANodeSummary.put(Constant.Role.NAMENODE, hConf.getNamenode().getPublicIp());
		if(hConf.getSecondaryNamenode() != null) {
			nonHANodeSummary.put(Constant.Role.SECONDARYNAMENODE, hConf.getSecondaryNamenode().getPublicIp());
		} else {
			nonHANodeSummary.put(Constant.Role.SECONDARYNAMENODE, HadoopTileViewHandler.STRING_EMPTY_VALUE);
		}
		return nonHANodeSummary;
	}
	
	private static Map<String, String> getHadoop2NodesSummary(Hadoop2Conf h2Conf) {
		Map<String, String> hadoop2NodesSummary = new LinkedHashMap<String, String>();
		hadoop2NodesSummary.put(Constant.Role.RESOURCEMANAGER, h2Conf.getResourceManagerNode().getPublicIp());
		if(h2Conf.isHaEnabled()) {
			hadoop2NodesSummary.putAll(HadoopTileViewHandler.getHadoopHANodesSummary(h2Conf));
		} else {
			hadoop2NodesSummary.putAll(HadoopTileViewHandler.getHadoopNonHANodesSummary(h2Conf));
		}
		
		
		hadoop2NodesSummary.put(Constant.Hadoop.Keys.DISPLAY_NAME_LIVE_DATANODES_COUNT, 
				HadoopDFSManager.getLiveDataNodesCount(h2Conf));
		hadoop2NodesSummary.put(Constant.Hadoop.Keys.DISPLAY_NAME_ACTIVE_NODEMANAGERS_COUNT, 
				HadoopJobsManager.getActiveNodeManagersCount(h2Conf));
		return hadoop2NodesSummary;
	}
	
	private static Map<String, String> getHadoopHANodesSummary(Hadoop2Conf hConf2) {
		Map<String, String> haNodesSummary = new LinkedHashMap<String, String>();
		
		if(hConf2.getActiveNamenode() != null) {
			haNodesSummary.put("Active " + Constant.Role.NAMENODE, hConf2.getActiveNamenode().getPublicIp());
		} else {
			haNodesSummary.put("Active " + Constant.Role.NAMENODE, HadoopTileViewHandler.STRING_EMPTY_VALUE);
		}
		
		if(hConf2.getStandByNamenode() != null) {
			haNodesSummary.put("StandBy " + Constant.Role.NAMENODE, hConf2.getStandByNamenode().getPublicIp());
		} else {
			haNodesSummary.put("StandBy " + Constant.Role.NAMENODE, HadoopTileViewHandler.STRING_EMPTY_VALUE);
		}
		return haNodesSummary;
	}
	
	/**
	 * Gets the hadoop2 ha tiles.
	 *
	 * @param hConf2 the h conf2
	 * @return the hadoop2 ha tiles
	 */
	private List<TileInfo> getHadoop2HATiles(Hadoop2Conf hConf2) {
		List<TileInfo> hadoop2HATiles = new ArrayList<TileInfo>();
		
		TileInfo tileInfo = null; 
				
		// Tile for StandBy NameNode
		if(hConf2.getStandByNamenode() != null) {
			tileInfo = new TileInfo(hConf2.getStandByNamenode()
					.getPublicIp(), "StandBy NameNode", null, null,
					Constant.Tile.Status.NORMAL, Constant.Tile.Url.NODE_LIST);
			hadoop2HATiles.add(tileInfo);	
		}

		// Tile for Active NameNode
		if(hConf2.getActiveNamenode() != null) {
			tileInfo = new TileInfo(hConf2.getActiveNamenode()
					.getPublicIp(), "Active NameNode", null, null,
					Constant.Tile.Status.NORMAL, Constant.Tile.Url.NODE_LIST);
			hadoop2HATiles.add(tileInfo);	
		}
		
		// Tile for HA Enabled
		tileInfo = new TileInfo("HA", "Enabled", null, null,
				Constant.Tile.Status.NORMAL, "");
		hadoop2HATiles.add(tileInfo);
		
		return hadoop2HATiles;
	}
	
	/**
	 * Gets the hadoop job data.
	 *
	 * @param cluster the cluster
	 * @return the hadoop job data
	 */
	private List<TileInfo> getHadoopMonitoringTiles(Cluster cluster) {
		try {
			List<TileInfo> monitoringTiles = new ArrayList<TileInfo>();
			boolean isHadoop2 = false;
			HadoopConf hConf = (HadoopConf) cluster.getClusterConf().getClusterComponents().get(Constant.Component.Name.HADOOP);
			if(hConf == null) {
				hConf = (Hadoop2Conf) cluster.getClusterConf().getClusterComponents().get(Constant.Component.Name.HADOOP2);
				isHadoop2 = true;
			}
			monitoringTiles.addAll(this.getDfsMonitoringTiles(hConf, isHadoop2));
			monitoringTiles.addAll(this.getMapRedMonitoringTiles(hConf, isHadoop2));
			return monitoringTiles;	
		} catch (Exception e) {
			logger.error(e.getMessage());
			return Collections.singletonList(getTileInfo("Error", "Hadoop Monitoring", "Could not get Hadoop data", null, Constant.Tile.Status.ERROR, Constant.Tile.Url.NODE_LIST));
		}
	}
	
	/**
	 * Gets the map red monitoring tiles.
	 *
	 * @param hConf the h conf
	 * @param idHadoop2 the id hadoop2
	 * @return the map red monitoring tiles
	 */
	private List<TileInfo> getMapRedMonitoringTiles(HadoopConf hConf, boolean idHadoop2) {
		List<TileInfo> mapRedMonitoringTiles = new ArrayList<TileInfo>();
		TileInfo errorTile = new TileInfo();
		String tileErrMsg = "Could not get Job data";
		
		try {
			String nodeIp = hConf.getNamenode().getPublicIp();
			String role = Constant.Role.JOBTRACKER;
			if(idHadoop2) {
				nodeIp = ((Hadoop2Conf) hConf).getResourceManagerNode().getPublicIp();
				role = Constant.Role.RESOURCEMANAGER;
				tileErrMsg = "Could not get Application data";
			}
			if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.AGENT)) {
				// Return error tile.. Agent Down
				errorTile = getTileInfo("Agent Down", nodeIp, tileErrMsg, null, Constant.Tile.Status.ERROR, Constant.Tile.Url.NODE_LIST);
				return Collections.singletonList(errorTile);
			}
			
			if(!HadoopUtils.getServiceStatusForNode(nodeIp, role)) {
				// Return error tile.. Node Down
				//errorTile = getTileInfo(role + " Down", nodeIp, tileErrMsg, null, Constant.Tile.Status.ERROR, null);
				errorTile = getTileInfo("Down", role, tileErrMsg, null, Constant.Tile.Status.ERROR, Constant.Tile.Url.NODE_LIST);
				return Collections.singletonList(errorTile);
			}
			if(idHadoop2) {
				// To be implemented
			} else {
				HadoopMonitoringData hadoopmonitoringData = (HadoopMonitoringData) new MonitoringManager()
												.getTechnologyData(nodeIp, Constant.Technology.HADOOP);
				mapRedMonitoringTiles.addAll(hadoopmonitoringData.getJOBTiles());
			}
			return mapRedMonitoringTiles;
		} catch (Exception e) {
			logger.error(e.getMessage());
			errorTile = getTileInfo("Error", "MapReduce Monitoring", tileErrMsg, null, Constant.Tile.Status.ERROR, Constant.Tile.Url.NODE_LIST);
			return Collections.singletonList(errorTile);
		}
	}
	
	/**
	 * Gets the dfs monitoring tiles.
	 *
	 * @param hConf the h conf
	 * @param isHadoop2 the is hadoop2
	 * @return the dfs monitoring tiles
	 */
	private List<TileInfo> getDfsMonitoringTiles(HadoopConf hConf, boolean isHadoop2) {
		List<TileInfo> dfsMonitoringTiles = new ArrayList<TileInfo>();
		TileInfo errorTile = new TileInfo();
		
		String tileErrMsg = "Could not get dfs data";
		try {
			String nameNodeIp = hConf.getNamenode().getPublicIp();
			String role = Constant.Role.NAMENODE;
			int clientPort = Integer.parseInt(Hadoop1Deployer.DEFAULT_PORT_HTTP_NAMENODE);
			
			if(isHadoop2) {
				if(((Hadoop2Conf) hConf).isHaEnabled()) {
					if(((Hadoop2Conf) hConf).getActiveNamenode() != null) {
						nameNodeIp = ((Hadoop2Conf) hConf).getActiveNamenode().getPublicIp();
					}
				}
			}
			
			if(!HadoopUtils.getServiceStatusForNode(nameNodeIp, Constant.Role.AGENT)) {
				// Return error tile.. Agent Down
				errorTile = getTileInfo("Agent Down", nameNodeIp, tileErrMsg, null, Constant.Tile.Status.ERROR, Constant.Tile.Url.NODE_LIST);
				return Collections.singletonList(errorTile);
			}
			
			if(!HadoopUtils.getServiceStatusForNode(nameNodeIp, role)) {
				// Return error tile.. NameNode Down
				errorTile = getTileInfo(role + " Down", nameNodeIp, tileErrMsg, null, Constant.Tile.Status.ERROR, Constant.Tile.Url.NODE_LIST);
				return Collections.singletonList(errorTile);
			}
			logger.info("inside getDfsMonitoringTiles");
			//dfsMonitoringTiles.addAll(this.getDfsMonitoringTiles(nameNodeIp, clientPort, isHadoop2, hConf.isCdh3()));
			logger.info("dfsMonitoringTiles : " + dfsMonitoringTiles);
			return dfsMonitoringTiles;
		} catch (Exception e) {
			logger.error(e.getMessage());
			errorTile = getTileInfo("Error", "DFS Monitoring", tileErrMsg, null, Constant.Tile.Status.ERROR, Constant.Tile.Url.NODE_LIST);
			return Collections.singletonList(errorTile);
		}
	}
	
	
	// For Cluster Level & Node Level Tiles
	/**
	 * The Enum DfsDataType.
	 */
	private enum MonitoringDataType {
		
		/** The cluster. */
		CLUSTER,
		
		/** The node. */
		NODE
	}
	
	
	private class CallableDfsMonitoringData implements Callable<Map<String, String>>
	{
		
		/** The node ip. */
		String nodeIp;
		
		/** The client port. */
		int clientPort;
		
		/** The is hadoop2. */
		boolean isHadoop2;
		
		/** The is cdh3. */
		boolean isCdh3;
		
		/** The dfs data type. */
		MonitoringDataType dfsDataType;
	
		/**
		 * Instantiates a new thread dfs monitoring data.
		 *
		 * @param nodeIp the node ip
		 * @param clientPort the client port
		 * @param isHadoop2 the is hadoop2
		 * @param isCdh3 the is cdh3
		 * @param dfsDataType the dfs data type
		 */
		CallableDfsMonitoringData(String nodeIp, int clientPort, boolean isHadoop2, boolean isCdh3, MonitoringDataType dfsDataType) {
			this.nodeIp = nodeIp;
			this.clientPort = clientPort;
			this.isHadoop2 = isHadoop2;
			this.isCdh3 = isCdh3;
			this.dfsDataType = dfsDataType;
		}

		@Override
		public Map<String, String> call() throws Exception {
			Map<String, String> dfsDataMap = null;
			// Based on the Dfs Data Type, function for Cluster / Node data is called
			if(dfsDataType.equals(MonitoringDataType.CLUSTER)) {
					dfsDataMap = HadoopDFSManager.getDfsClusterDataMap(nodeIp, clientPort, isHadoop2, isCdh3);	
			}
			return dfsDataMap;
		}	
	}
	
	private class CallableMapredJobsData implements Callable<Map<String, String>>
	{
		
		/** The node ip. */
		String nodeIp;
		
		/** The client port. */
		int clientPort;
		
		/** The is hadoop2. */
		boolean isHadoop2;
		
		/** The dfs data type. */
		MonitoringDataType mapredDataType;
		
		CallableMapredJobsData(String nodeIp, int clientPort, boolean isHadoop2, MonitoringDataType mapredDataType) {
			this.nodeIp = nodeIp;
			this.clientPort = clientPort;
			this.isHadoop2 = isHadoop2;
			this.mapredDataType = mapredDataType;
		}

		@Override
		public Map<String, String> call() throws Exception {
			Map<String, String> mapredJobsDataMap = null;
			if(mapredDataType.equals(MonitoringDataType.CLUSTER)) {
				if(isHadoop2) {
					mapredJobsDataMap = HadoopJobsManager.getYarnClusterData(nodeIp, clientPort);
				} else {
					mapredJobsDataMap = HadoopJobsManager.getMapRedClusterData(nodeIp, clientPort);
				}	
			}
			return mapredJobsDataMap;
		}	
	}

	
	public Map<String, String> getHdfsClusterUsageData(String nodeIp, int clientPort, boolean isHadoop2, boolean isCdh3) {
		
		String errMsg = "Error: Unable to fetch DFS data";
		Map<String, String> dfsDataMap = null;
		
		try {
			long waitTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
					"hadoop.jmxmonitoring.wait.time");
			
			CallableDfsMonitoringData callableDfsMonitoringData = new CallableDfsMonitoringData(nodeIp, clientPort, isHadoop2, isCdh3, MonitoringDataType.CLUSTER);
			FutureTask<Map<String, String>> futureTaskDfsMonitoringData = new FutureTask<Map<String, String>>(callableDfsMonitoringData);
			
			AppStoreWrapper.getExecutor().execute(futureTaskDfsMonitoringData);
			
		    dfsDataMap = futureTaskDfsMonitoringData.get(waitTime, TimeUnit.MILLISECONDS);
		    if(dfsDataMap == null) {
		    	logger.error(errMsg);
				return null;
		    } 
		} catch (Exception e) {
			logger.error(errMsg);
			return null;
		}
		return dfsDataMap;
	}
	
	public Map<String, String> getMapredJobsData(String nodeIp, int clientPort, boolean isHadoop2) {
		
		String errMsg = "Error: Unable to fetch Mapred data";
		Map<String, String> mapredDataMap = null;
		
		try {
			long waitTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
					"hadoop.jmxmonitoring.wait.time");
			
			CallableMapredJobsData callableMapredMonitoringData = new CallableMapredJobsData(nodeIp, clientPort, isHadoop2, MonitoringDataType.CLUSTER);
			FutureTask<Map<String, String>> futureTaskMapredMonitoringData = new FutureTask<Map<String, String>>(callableMapredMonitoringData);

			AppStoreWrapper.getExecutor().execute(futureTaskMapredMonitoringData);
			
		    mapredDataMap = futureTaskMapredMonitoringData.get(waitTime, TimeUnit.MILLISECONDS);
		    if(mapredDataMap == null) {
		    	logger.error(errMsg);
				return null;
		    } 
		} catch (Exception e) {
			logger.error(errMsg);
			return null;
		}
		return mapredDataMap;
	}
}
