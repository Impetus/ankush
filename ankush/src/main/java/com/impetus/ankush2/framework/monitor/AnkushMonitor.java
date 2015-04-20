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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event.Severity;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.config.MonitoringInfo;
import com.impetus.ankush.common.framework.config.NodeMemoryInfo;
import com.impetus.ankush.common.framework.config.NodeUpTimeInfo;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBClusterManager;
import com.impetus.ankush2.db.DBEventManager;
import com.impetus.ankush2.db.DBServiceManager;
import com.impetus.ankush2.logger.AnkushLogger;

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
}
