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

import java.util.List;
import java.util.ArrayList;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringListener;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Application;
import com.impetus.ankush.hadoop.job.HadoopClusterMetrics;

/**
 * @author Akhil
 *
 */
public class Hadoop2ApplicationData implements TechnologyData {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static AnkushLogger LOG = new AnkushLogger(
			Hadoop2ApplicationData.class);
	
	/** cluster metrics. **/
	private HadoopClusterMetrics clusterMetrics;

	/** The job infos. */
	private ArrayList<Hadoop2Application> applications;
	
	/** The hadoop nodes detail **/
	private List<Hadoop2Node> nodes;

	/**
	 * @param clusterMetrics
	 *         		the clusterMetrics to set
	 */
	public void setClusterMetrics(HadoopClusterMetrics clusterMetrics) {
		this.clusterMetrics = clusterMetrics;
	}

	/**
	 * @return the clusterMetrics
	 */
	public HadoopClusterMetrics getClusterMetrics() {
		return clusterMetrics;
	}
	
	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<Hadoop2Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the nodes
	 */
	public List<Hadoop2Node> getNodes() {
		return nodes;
	}
	
	/**
	 * @return the applications
	 */
	public ArrayList<Hadoop2Application> getApplications() {
		return applications;
	}

	/**
	 * @param applications the applications to set
	 */
	public void setApplications(ArrayList<Hadoop2Application> applications) {
		this.applications = applications;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.TechnologyData#getTechnologyName()
	 */
	@Override
	public String getTechnologyName() {
		return Constant.Technology.HADOOP;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.TechnologyData#getMonitoringListener()
	 */
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
		if (clusterMetrics == null) {
			return tiles;
		}
		tiles.addAll(getClusterMetricTiles());

		return tiles;
	}

	public List<TileInfo> getClusterMetricTiles() {
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		// status
		String status = Constant.Tile.Status.NORMAL;

		try {
			// running app tile.
			TileInfo tile = new TileInfo(clusterMetrics.getAppsRunning() + "",
					"Running Applications", null, null, status,
					"Job Monitoring");
			tiles.add(tile);

			// completed app tile.
			tile = new TileInfo(clusterMetrics.getAppsCompleted() + "",
					"Completed Applications", null, null, status,
					"Job Monitoring");
			tiles.add(tile);

			// submitted app tile.
			tile = new TileInfo(clusterMetrics.getAppsSubmitted() + "",
					"Submitted Applications", null, null, status,
					"Job Monitoring");
			tiles.add(tile);

			// failed/ killed app tile.
			tile = new TileInfo(clusterMetrics.getAppsFailed() + " / "
					+ clusterMetrics.getAppsKilled(),
					"Failed / Killed Applications", null, null, status,
					"Job Monitoring");
			tiles.add(tile);

			// used/ total memory tile.
			tile = new TileInfo(clusterMetrics.getAllocatedMB() + " / "
					+ clusterMetrics.getTotalMB(), "Used / Total Memory(in MB)");
			tiles.add(tile);

			// active/total nodes tile.
			tile = new TileInfo(clusterMetrics.getActiveNodes() + " / "
					+ clusterMetrics.getTotalNodes(), "Active / Total Nodes");
			tiles.add(tile);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return tiles;
	}
}
