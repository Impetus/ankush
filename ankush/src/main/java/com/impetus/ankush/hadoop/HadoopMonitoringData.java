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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringListener;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.dfs.DFSReport;
import com.impetus.ankush.hadoop.dfs.HadoopDFSManager;
import com.impetus.ankush.hadoop.job.HadoopJob;
import com.impetus.ankush.hadoop.job.HadoopMetricsInfo;

/**
 * The Class HadoopMonitoringData.
 * 
 * @author bgunjan
 */
public class HadoopMonitoringData implements TechnologyData {

	private static final long serialVersionUID = 1L;

	/** The hadoop metrics info. */
	private HadoopMetricsInfo hadoopMetricsInfo;

	/** The job infos. */
	private ArrayList<HadoopJob> jobInfos;

	/** The job infos. */
	private ArrayList<HadoopJob> retiredJobs = new ArrayList<HadoopJob>();

	/** The dfs infos. */
	private DFSReport dfsInfos;

	/**
	 * Gets the hadoop metrics info.
	 * 
	 * @return the hadoopMetricsInfo
	 */
	public HadoopMetricsInfo getHadoopMetricsInfo() {
		return hadoopMetricsInfo;
	}

	/**
	 * Sets the hadoop metrics info.
	 * 
	 * @param hadoopMetricsInfo
	 *            the hadoopMetricsInfo to set
	 */
	public void setHadoopMetricsInfo(HadoopMetricsInfo hadoopMetricsInfo) {
		this.hadoopMetricsInfo = hadoopMetricsInfo;
	}

	/**
	 * Gets the job infos.
	 * 
	 * @return the jobInfos
	 */
	public ArrayList<HadoopJob> getJobInfos() {
		return jobInfos;
	}

	/**
	 * Sets the job infos.
	 * 
	 * @param jobInfos
	 *            the jobInfos to set
	 */
	public void setJobInfos(ArrayList<HadoopJob> jobInfos) {
		this.jobInfos = jobInfos;
	}

	/**
	 * @param retiredJobs
	 *            the retiredJobs to set
	 */
	public void setRetiredJobs(ArrayList<HadoopJob> retiredJobs) {
		this.retiredJobs = retiredJobs;
	}

	/**
	 * @return the retiredJobs
	 */
	public ArrayList<HadoopJob> getRetiredJobs() {
		return retiredJobs;
	}

	/**
	 * Gets the dfs infos.
	 * 
	 * @return the dfsInfos
	 */
	public DFSReport getDfsInfos() {
		return dfsInfos;
	}

	/**
	 * Sets the dfs infos.
	 * 
	 * @param dfsInfos
	 *            the dfsInfos to set
	 */
	public void setDfsInfos(DFSReport dfsInfos) {
		this.dfsInfos = dfsInfos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dfsInfos == null) ? 0 : dfsInfos.hashCode());
		result = prime
				* result
				+ ((hadoopMetricsInfo == null) ? 0 : hadoopMetricsInfo
						.hashCode());
		result = prime * result
				+ ((jobInfos == null) ? 0 : jobInfos.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HadoopMonitoringData other = (HadoopMonitoringData) obj;
		if (dfsInfos == null) {
			if (other.dfsInfos != null) {
				return false;
			}
		} else if (!dfsInfos.equals(other.dfsInfos)) {
			return false;
		}
		if (hadoopMetricsInfo == null) {
			if (other.hadoopMetricsInfo != null) {
				return false;
			}
		} else if (!hadoopMetricsInfo.equals(other.hadoopMetricsInfo)) {
			return false;
		}
		if (jobInfos == null) {
			if (other.jobInfos != null) {
				return false;
			}
		} else if (!jobInfos.equals(other.jobInfos)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HadoopMonitoringData [hadoopMetricsInfo=" + hadoopMetricsInfo
				+ ", jobInfos=" + jobInfos + ", dfsInfos=" + dfsInfos + "]";
	}

	@Override
	public String getTechnologyName() {
		return Constant.Technology.HADOOP;
	}

	@Override
	public MonitoringListener getMonitoringListener() {
		return new HadoopMonitoringListener();
	}

	/**
	 * Method to get tiles from the monitoring data.
	 * 
	 * @return List of tiles.
	 */
	public List<TileInfo> getTiles() {
		List<TileInfo> clusterTiles = getJOBTiles();
		//clusterTiles.addAll(HadoopDFSManager.getDFSTiles(dfsInfos));
		return clusterTiles;
	}

	public Map<String, String> getJobsSummary() {
		Map<String, String> mapJobSummary = new HashMap<String, String>();
		mapJobSummary.put("Map Task Available Capacity", hadoopMetricsInfo.getMaxMapTasksCapacity());
		mapJobSummary.put("Reduce Task Available Capacity", hadoopMetricsInfo.getMaxReduceTasksCapacity());
		mapJobSummary.put("Running Jobs", hadoopMetricsInfo.getTotalJobRunning());
		mapJobSummary.put("Completed Jobs", hadoopMetricsInfo.getTotalJobsCompleted());
		return mapJobSummary;
	}
	
	public List<TileInfo> getJOBTiles() {
		// empty array list.
		List<TileInfo> clusterTiles = new ArrayList<TileInfo>();
		// normal status.
		String status = Constant.Tile.Status.NORMAL;

		// map task capacity tile
		TileInfo tileInfo = new TileInfo(
				hadoopMetricsInfo.getMaxMapTasksCapacity(),
				"Map Task Available Capacity", null, null, status, null);
		clusterTiles.add(tileInfo);

		// reduce task capacity tile
		tileInfo = new TileInfo(hadoopMetricsInfo.getMaxReduceTasksCapacity(),
				"Reduce Task Available Capacity", null, null, status, null);
		clusterTiles.add(tileInfo);

		// total job running.
		tileInfo = new TileInfo(hadoopMetricsInfo.getTotalJobRunning(),
				"Running Jobs", null, null, status, "Job Monitoring");
		clusterTiles.add(tileInfo);

		// total job completed.
		tileInfo = new TileInfo(hadoopMetricsInfo.getTotalJobsCompleted(),
				"Completed Jobs", null, null, status, "Job Monitoring");
		clusterTiles.add(tileInfo);
		return clusterTiles;
	}
}
