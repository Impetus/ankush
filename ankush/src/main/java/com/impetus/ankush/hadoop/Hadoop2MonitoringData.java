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
import java.util.List;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringListener;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.dfs.DFSReport;

/**
 * @author hokam
 * 
 */
public class Hadoop2MonitoringData implements TechnologyData {

	/** The logger. */
	private static AnkushLogger LOG = new AnkushLogger(
			Hadoop2MonitoringData.class);

	private static final long serialVersionUID = 1L;

//	/** cluster metrics. **/
//	private HadoopClusterMetrics clusterMetrics;
//
//	/** The job infos. */
//	private ArrayList<Hadoop2Job> jobs;
//
//	/** The hadoop nodes detail **/
//	private List<Hadoop2Node> nodes;

	private DFSReport dfsReport;

	/**
	 * @param clusterMetrics
	 *            the clusterMetrics to set
	 */
//	public void setClusterMetrics(HadoopClusterMetrics clusterMetrics) {
//		this.clusterMetrics = clusterMetrics;
//	}
//
//	/**
//	 * @return the clusterMetrics
//	 */
//	public HadoopClusterMetrics getClusterMetrics() {
//		return clusterMetrics;
//	}

//	/**
//	 * @return the jobInfos
//	 */
//	public ArrayList<Hadoop2Job> getJobs() {
//		return jobs;
//	}
//
//	/**
//	 * @param jobInfos
//	 *            the jobInfos to set
//	 */
//	public void setJobs(ArrayList<Hadoop2Job> jobs) {
//		this.jobs = jobs;
//	}
//
//	/**
//	 * @param nodes
//	 *            the nodes to set
//	 */
//	public void setNodes(List<Hadoop2Node> nodes) {
//		this.nodes = nodes;
//	}
//
//	/**
//	 * @return the nodes
//	 */
//	public List<Hadoop2Node> getNodes() {
//		return nodes;
//	}

	/**
	 * @param dfsReport
	 *            the dfsReport to set
	 */
	public void setDfsReport(DFSReport dfsReport) {
		this.dfsReport = dfsReport;
	}

	/**
	 * @return the dfsReport
	 */
	public DFSReport getDfsReport() {
		return dfsReport;
	}

	@Override
	public String getTechnologyName() {
		return Constant.Technology.HADOOP;
	}

	@Override
	public MonitoringListener getMonitoringListener() {
		return null;
	}

	/**
	 * Method to get tiles from the monitoring data.
	 * 
	 * @return List of tiles.
	 */
	public List<TileInfo> getTiles() {
		// empty tiles.
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// normal status.
//		if (clusterMetrics == null) {
//			return tiles;
//		}
//      tiles.addAll(getClusterMetricTiles());

		tiles.addAll(getDFSTiles());

		return tiles;
	}

//	public List<TileInfo> getClusterMetricTiles() {
//		List<TileInfo> tiles = new ArrayList<TileInfo>();
//		// status
//		String status = Constant.Tile.Status.NORMAL;
//
//		try {
//			// running app tile.
//			TileInfo tile = new TileInfo(clusterMetrics.getAppsRunning() + "",
//					"Running Applications", null, null, status,
//					"Job Monitoring");
//			tiles.add(tile);
//
//			// completed app tile.
//			tile = new TileInfo(clusterMetrics.getAppsCompleted() + "",
//					"Completed Applications", null, null, status,
//					"Job Monitoring");
//			tiles.add(tile);
//
//			// submitted app tile.
//			tile = new TileInfo(clusterMetrics.getAppsSubmitted() + "",
//					"Submitted Applications", null, null, status,
//					"Job Monitoring");
//			tiles.add(tile);
//
//			// failed/ killed app tile.
//			tile = new TileInfo(clusterMetrics.getAppsFailed() + " / "
//					+ clusterMetrics.getAppsKilled(),
//					"Failed / Killed Applications", null, null, status,
//					"Job Monitoring");
//			tiles.add(tile);
//
//			// used/ total memory tile.
//			tile = new TileInfo(clusterMetrics.getAllocatedMB() + " / "
//					+ clusterMetrics.getTotalMB(), "Used / Total Memory(in MB)");
//			tiles.add(tile);
//
//			// active/total nodes tile.
//			tile = new TileInfo(clusterMetrics.getActiveNodes() + " / "
//					+ clusterMetrics.getTotalNodes(), "Active / Total Nodes");
//			tiles.add(tile);
//		} catch (Exception e) {
//			LOG.error(e.getMessage());
//		}
//		return tiles;
//	}

	public List<TileInfo> getDFSTiles() {
		// empty tiles.
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		TileInfo tile;
		try {
			if (dfsReport.getNonDFSUsed() != null
					&& !dfsReport.getNonDFSUsed().isEmpty()) {
				tile = new TileInfo(dfsReport.getNonDFSUsed(), "Non DFS Used");
				tiles.add(tile);
			}

			if (dfsReport.getDfsRemaining() != null
					&& !dfsReport.getDfsRemaining().isEmpty()) {
				tile = new TileInfo(dfsReport.getDfsRemaining(),
						"DFS Remaining");
				tiles.add(tile);
			}

			if (dfsReport.getDfsUsed() != null
					&& !dfsReport.getDfsUsed().isEmpty()) {
				tile = new TileInfo(dfsReport.getDfsUsed(), "DFS Used");
				tiles.add(tile);
			}

			if (dfsReport.getConfiguredCapacity() != null
					&& !dfsReport.getConfiguredCapacity().isEmpty()) {
				tile = new TileInfo(dfsReport.getConfiguredCapacity(),
						"DFS Capacity");
				tiles.add(tile);
			}

			boolean inSafemode = dfsReport.isInSafemode();
			if (inSafemode) {
				tile = new TileInfo("Safemode", "NameNode is in Safemode",
						null, null, Constant.Tile.Status.WARNING, null);
				tiles.add(tile);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		return tiles;
	}

}
