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
package com.impetus.ankush2.framework.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event.Severity;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.config.MonitoringInfo;
import com.impetus.ankush.common.framework.config.NodeMemoryInfo;
import com.impetus.ankush.common.framework.config.NodeUpTimeInfo;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush2.db.DBClusterManager;
import com.impetus.ankush2.db.DBEventManager;
import com.impetus.ankush2.db.DBServiceManager;

public class AnkushMonitor {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(AnkushMonitor.class);

	/** The node monitoring manager. */
	private GenericManager<NodeMonitoring, Long> nodeMonitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	/** Generic cluster master. */
	private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	public List<Map<String, Object>> getClusterOverview() {
		try {
			DBEventManager eventManager = new DBEventManager();

			List<Map<String, Object>> clusterLst = new ArrayList<Map<String, Object>>();
			Map<String, Object> graphDataMap = new HashMap<String, Object>();

			// iterating over the clusters.
			for (Cluster dbCluster : new DBClusterManager().getClusters()) {

				Map<String, Object> clusterTileData = new HashMap<String, Object>();

				com.impetus.ankush2.constant.Constant.Cluster.State state = dbCluster
						.getClusterConfig().getState();

				// Setting alert & warning count

				Map<String, Object> notifications = new HashMap<String, Object>();

				if (state == com.impetus.ankush2.constant.Constant.Cluster.State.DEPLOYED) {
					int alertCount = 0;
					int warnCount = 0;

					Map<Severity, Integer> severityCount = eventManager
							.getEventsCountBySeverity(dbCluster.getId());

					// getting alerts count.
					if (severityCount.containsKey(Severity.CRITICAL)) {
						alertCount = severityCount.get(Severity.CRITICAL);
					}
					// getting warn count.
					if (severityCount.containsKey(Severity.WARNING)) {
						warnCount = severityCount.get(Severity.WARNING);
					}

					if (alertCount > 0) {
						state = com.impetus.ankush2.constant.Constant.Cluster.State.CRITICAL;
					} else if (warnCount > 0) {
						state = com.impetus.ankush2.constant.Constant.Cluster.State.WARNING;
					}

					notifications.put("alerts", alertCount);
					notifications.put("warnings", warnCount);

					// Get graph data
					graphDataMap = getGraphData(dbCluster);
				}

				clusterTileData.put("status", state);
				clusterTileData.put("clusterId", dbCluster.getId());
				clusterTileData.put("name", dbCluster.getName());
				clusterTileData.put("technology", dbCluster.getTechnology());
				clusterTileData.put("notifications", notifications);
				clusterTileData.put("environment", dbCluster.getEnvironment());
				clusterTileData.put("data", graphDataMap);

				clusterLst.add(clusterTileData);
			}
			return clusterLst;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param dbCluster
	 * @param graphDataMap
	 * @param state
	 */
	private Map<String, Object> getGraphData(Cluster dbCluster) {
		Map<String, Object> graphDataMap = new HashMap<String, Object>();
		double avgCPU = 0;
		double avgMem = 0;
		int nodeCPUReadingCount = 0;
		int nodeMemReadingCount = 0;

		for (Node node : dbCluster.getNodes()) {

			try {
				NodeMonitoring nodeMonitoringInfo = nodeMonitoringManager
						.getByPropertyValue(
								com.impetus.ankush2.constant.Constant.Keys.NODEID,
								node.getId());
				boolean agentDown = DBServiceManager.getManager().isAgentDown(
						node.getPublicIp());

				if (nodeMonitoringInfo != null) {
					MonitoringInfo monitoringInfo = nodeMonitoringInfo
							.getMonitoringInfo();
					if (monitoringInfo != null) {
						List<NodeUpTimeInfo> nodeUptimeInfoLst = monitoringInfo
								.getUptimeInfos();
						if ((nodeUptimeInfoLst != null)
								&& nodeUptimeInfoLst.size() > 0) {
							NodeUpTimeInfo nodeUptimeInfo = nodeUptimeInfoLst
									.get(0);
							Double cpuLoadValue = nodeUptimeInfo.getCpuUsage();
							if ((cpuLoadValue != null)
									&& (!Double.isNaN(cpuLoadValue))) {
								if (agentDown) {
									cpuLoadValue = 0D;
								}
								avgCPU += cpuLoadValue;
								++nodeCPUReadingCount;
							}
						}

						List<NodeMemoryInfo> nodeMemoryInfoLst = monitoringInfo
								.getMemoryInfos();
						if ((nodeMemoryInfoLst != null)
								&& nodeMemoryInfoLst.size() > 0) {
							NodeMemoryInfo nodeMemoryInfo = nodeMemoryInfoLst
									.get(0);
							Double memUsed = nodeMemoryInfo.getUsedPercentage();
							if ((memUsed != null) && (!Double.isNaN(memUsed))) {
								if (agentDown) {
									memUsed = 0D;
								}
								avgMem += memUsed;
								++nodeMemReadingCount;
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		if (nodeCPUReadingCount > 0) {
			avgCPU /= nodeCPUReadingCount;
		}

		if (nodeMemReadingCount > 0) {
			avgMem /= nodeMemReadingCount;
		}

		avgCPU = round(avgCPU, 2);
		avgMem = round(avgMem, 2);

		graphDataMap.put("cpu", avgCPU);
		graphDataMap.put("memory", avgMem);
		return graphDataMap;
	}

	/**
	 * Round.
	 * 
	 * @param num
	 *            the num
	 * @param digits
	 *            the digits
	 * @return the double
	 */
	private double round(double num, int digits) {
		double fact = Math.pow(10, digits);
		num = Math.round(num * fact) / fact;
		return num;
	}

	/**
	 * Gets the systerm overview.
	 * 
	 * @return the systerm overview
	 */
	public List<TileInfo> getSystermOverview() {
		// list of tiles.
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		// add all cluster tiles.
		tiles.addAll(getClusterTypeTiles());

		return tiles;
	}

	/**
	 * Gets the cluster type tiles.
	 * 
	 * @return the cluster type tiles
	 */
	private List<TileInfo> getClusterTypeTiles() {
		List<TileInfo> tileInfos = new ArrayList<TileInfo>();

		// iterating over the technology.
		for (String technology : Arrays.asList(
				com.impetus.ankush2.constant.Constant.Component.Name.HADOOP,
				com.impetus.ankush2.constant.Constant.Component.Name.CASSANDRA)) {
			// iterating over the environment.
			for (String environment : Arrays
					.asList(Constant.Cluster.Environment.IN_PREMISE)) {

				for (com.impetus.ankush2.constant.Constant.Cluster.State state : Arrays
						.asList(com.impetus.ankush2.constant.Constant.Cluster.State.DEPLOYING,
								com.impetus.ankush2.constant.Constant.Cluster.State.ERROR,
								com.impetus.ankush2.constant.Constant.Cluster.State.REMOVING)) {

					// line 2 message.
					String message = technology + "/" + environment;

					if (state == com.impetus.ankush2.constant.Constant.Cluster.State.ERROR) {
						message = message + " " + state.toString();
					}

					Map<String, Object> propsMap = new HashMap<String, Object>();
					propsMap.put("technology", technology);
					propsMap.put("environment", environment);
					propsMap.put("state", state.toString());

					// Method to get given technology type cluster count.
					Integer count = clusterManager
							.getAllByPropertyValueCount(propsMap);

					TileInfo tileInfo = null;
					if (count != 0) {
						// Create tile info object.
						tileInfo = new TileInfo();
						tileInfo.setLine1(count.toString());
						tileInfo.setLine2(message);
						tileInfo.setStatus(com.impetus.ankush2.constant.Constant.Tile.Status.NORMAL
								.toString());

						if (state == com.impetus.ankush2.constant.Constant.Cluster.State.ERROR) {
							tileInfo.setLine3("In " + state.toString());
							tileInfo.setStatus(com.impetus.ankush2.constant.Constant.Tile.Status.CRITICAL
									.toString());
						}

						tileInfo.setUrl(null);
						tileInfo.setData(null);
						tileInfos.add(tileInfo);
					}
				}
				// creating disjunc map.
				List<Map<String, Object>> disMap = new ArrayList<Map<String, Object>>();
				// iterating over the running cluster states.
				for (com.impetus.ankush2.constant.Constant.Cluster.State state : Arrays
						.asList(com.impetus.ankush2.constant.Constant.Cluster.State.DEPLOYED,
								com.impetus.ankush2.constant.Constant.Cluster.State.ADD_NODE,
								com.impetus.ankush2.constant.Constant.Cluster.State.REMOVE_NODE,
								com.impetus.ankush2.constant.Constant.Cluster.State.REBALANCE,
								com.impetus.ankush2.constant.Constant.Cluster.State.REDISTRIBUTE,
								com.impetus.ankush2.constant.Constant.Cluster.State.CHANGE_REP_FACTOR)) {

					// conjunction map
					Map<String, Object> propsMap = new HashMap<String, Object>();
					propsMap.put("technology", technology);
					propsMap.put("environment", environment);
					propsMap.put("state", state.toString());
					disMap.add(propsMap);
				}

				// Method to get given technology type cluster count.
				Integer count = clusterManager
						.getAllByDisjunctionveNormalQueryCount(disMap);

				// line2 message.
				String message = technology + "/" + environment + " running";
				TileInfo tileInfo = null;
				if (count != 0) {
					// Create tile info object.
					tileInfo = new TileInfo();
					tileInfo.setLine1(count.toString());
					tileInfo.setLine2(message);
					tileInfo.setStatus(com.impetus.ankush2.constant.Constant.Tile.Status.NORMAL
							.toString());
					tileInfo.setUrl(null);
					tileInfo.setData(null);
					tileInfos.add(tileInfo);
				}
			}
		}
		TileManager manager = new TileManager();

		// add Service/CPU/Memory/Agent Down tiles.
		tileInfos.addAll((manager.getSystemEventTiles()));

		return tileInfos;
	}
}
