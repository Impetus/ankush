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
package com.impetus.ankush.common.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.config.MonitoringInfo;
import com.impetus.ankush.common.framework.config.NodeMemoryInfo;
import com.impetus.ankush.common.framework.config.NodeUpTimeInfo;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.oraclenosql.OracleNoSQLClusterConf;
import com.impetus.ankush.oraclenosql.OracleNoSQLTechnologyData;

/**
 * The Class ClusterOverviewMonitor.
 */
public class ClusterOverviewMonitor {

	/** The Constant STATUS. */
	private static final String STATUS = "status";

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			ClusterOverviewMonitor.class);

	/** The Constant ROUNDING_PLACES. */
	private final static int ROUNDING_PLACES = 2;

	/** Generic cluster manager. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** The node monitoring manager. */
	private static GenericManager<NodeMonitoring, Long> nodeMonitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);
	// Node manager
	/** The node manager. */
	static private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/**
	 * Gets the cluster overview.
	 * 
	 * @return the cluster overview
	 */
	public List<Map<String, Object>> getClusterOverview() {
		EventManager eventManager = new EventManager();
		List<Map<String, Object>> clusterLst = new ArrayList<Map<String, Object>>();
		// getting all clusters.
		List<Cluster> clusters = clusterManager.getAll();
		// iterating over the clusters.
		for (Cluster nc : clusters) {
			// cluster tile data map.
			Map<String, Object> clusterTileData = new HashMap<String, Object>();
			// putting cluster id.
			clusterTileData.put("clusterId", nc.getId());
			// setting cluster name.
			clusterTileData.put("name", nc.getName());
			String state = nc.getState();
			// if state is server crashed setting it as error.
			if (state.equals(Constant.Cluster.State.SERVER_CRASHED)) {
				// set state as error.
				state = Constant.Cluster.State.ERROR;
			}
			// put state in map at status key.
			clusterTileData.put(STATUS, state);

			// Setting alert & warning count
			int alertCount = 0;
			int warnCount = 0;
			boolean isClusterDown = false;
			if (nc.getState().equals(Constant.Cluster.State.DEPLOYED)) {
				// getting alerts count.
				alertCount = eventManager.getEventsCountBySeverity(nc,
						Constant.Alerts.Severity.CRITICAL);
				// getting warn count.
				warnCount = eventManager.getEventsCountBySeverity(nc,
						Constant.Alerts.Severity.WARNING);
			}

			String clusterTechnology = nc.getTechnology();

			// Setting Cluster Status
			if (nc.getTechnology().equals(Constant.Technology.ORACLE_NOSQL)) {
				if (nc.getState().equals(Constant.Cluster.State.DEPLOYED)) {
					int active = OracleNoSQLTechnologyData.STATE_DOWN;
					try {
						OracleNoSQLTechnologyData techData = OracleNoSQLTechnologyData
								.getTechnologyData(((OracleNoSQLClusterConf) nc
										.getClusterConf()).getOracleNoSQLConf());
						if (techData != null) {
							Map topologyTree = techData.getTopologyTree();
							if (topologyTree != null
									&& topologyTree.containsKey("active")) {
								active = (Integer) topologyTree.get("active");
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					if (active == OracleNoSQLTechnologyData.STATE_DOWN) {
						isClusterDown = true;
						alertCount++;
					} else if (active == OracleNoSQLTechnologyData.STATE_WARNING) {
						warnCount++;
					}
				}
			} else if (nc.getTechnology().equals(Constant.Technology.HADOOP)) {
				boolean isHadoop2 = false;
				boolean namenodeStatus = true;
				HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) nc
						.getClusterConf();

				if (hadoopClusterConf.getClusterComponents().get(
						Constant.Component.Name.HADOOP2) != null) {
					isHadoop2 = true;
				}
				if (nc.getState().equals(Constant.Cluster.State.DEPLOYED)) {
					try {
						String nodeIp = hadoopClusterConf.getNameNode()
								.getPublicIp();

						if (isHadoop2) {
							Hadoop2Conf hConf2 = (Hadoop2Conf) hadoopClusterConf
									.getClusterComponents().get(
											Constant.Component.Name.HADOOP2);
							if (hConf2.isHaEnabled()) {
								if (hConf2.getActiveNamenode() != null) {
									nodeIp = hConf2.getActiveNamenode()
											.getPublicIp();
								} else {
									namenodeStatus = false;
									isClusterDown = true;
								}
							} else {
								nodeIp = hConf2.getNamenode().getPublicIp();
							}
						}

						if (namenodeStatus) {
							namenodeStatus = this
									.getHadoopNameNodeStatus(nodeIp);
							if (!namenodeStatus) {
								isClusterDown = true;
							}
						}

					} catch (Exception e) {
						alertCount++;
						logger.error(e.getMessage(), e);
					}
				}
				if (isHadoop2) {
					clusterTechnology = Constant.Component.Name.HADOOP2;
				}
			}

			if (nc.getState().equals(Constant.Cluster.State.DEPLOYED)) {
				if (isClusterDown) {
					clusterTileData.put(STATUS, Constant.Cluster.State.DOWN);
				} else if (alertCount > 0) {
					clusterTileData
							.put(STATUS, Constant.Cluster.State.CRITICAL);
				} else if (warnCount > 0) {
					clusterTileData.put(STATUS, Constant.Cluster.State.WARNING);
				}
			}

			clusterTileData.put("technology", clusterTechnology);
			Map<String, Object> awMap = new HashMap<String, Object>();
			awMap.put("alerts", alertCount);
			awMap.put("warnings", warnCount);
			clusterTileData.put("notifications", awMap);

			Map<String, Object> graphDataMap = null;
			double avgCPU = 0;
			double avgMem = 0;
			int nodeCPUReadingCount = 0;
			int nodeMemReadingCount = 0;

			if (nc.getState().equals(Constant.Cluster.State.DEPLOYED)) {
				List<NodeMonitoring> nodeMonitoringInfos = nodeMonitoringManager
						.getAllByNamedQuery("getClusterMonitoring", Collections
								.singletonMap("clusterId", (Object) nc.getId()));
				for (NodeMonitoring nodeMonitoringInfo : nodeMonitoringInfos) {
					try {
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
									Double cpuLoadValue = nodeUptimeInfo
											.getCpuUsage();
									if ((cpuLoadValue != null)
											&& (!Double.isNaN(cpuLoadValue))) {
										if (nodeMonitoringInfo.isAgentDown()) {
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
									Double memUsed = nodeMemoryInfo
											.getUsedPercentage();
									if ((memUsed != null)
											&& (!Double.isNaN(memUsed))) {
										if (nodeMonitoringInfo.isAgentDown()) {
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

				avgCPU = round(avgCPU, ROUNDING_PLACES);
				avgMem = round(avgMem, ROUNDING_PLACES);

				graphDataMap = new HashMap<String, Object>();
				graphDataMap.put("cpu", avgCPU);
				graphDataMap.put("memory", avgMem);
			}

			clusterTileData.put("data", graphDataMap);

			clusterTileData.put("environment", nc.getEnvironment());

			clusterLst.add(clusterTileData);
		}
		return clusterLst;
	}

	private boolean getHadoopNameNodeStatus(String nameNodeIp) {

		boolean namenodeStatus = false;
		try {
			// Get the db node info using public IP
			Node hadoopNode = nodeManager.getByPropertyValueGuarded("publicIp",
					nameNodeIp);
			if (hadoopNode != null) {
				Long nodeId = hadoopNode.getId();
				NodeMonitoring nodeMonitoring = nodeMonitoringManager
						.getByPropertyValueGuarded("nodeId", nodeId);
				if (nodeMonitoring != null) {
					// Fetching HadoopNodeConf for node roles
					Map<String, Boolean> serviceStatusMap = nodeMonitoring
							.getServiceStatus();
					namenodeStatus = false;
					if (serviceStatusMap != null) {
						Boolean serviceStatusVal = serviceStatusMap
								.get(Constant.Role.NAMENODE);
						if (serviceStatusVal != null) {
							namenodeStatus = serviceStatusVal;
						}
					}
				}
			}
		} catch (Exception e) {

		}
		return namenodeStatus;
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
