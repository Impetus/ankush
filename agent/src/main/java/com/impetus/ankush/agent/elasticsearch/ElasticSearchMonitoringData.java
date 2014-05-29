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
package com.impetus.ankush.agent.elasticsearch;

import java.util.HashMap;
import java.util.Map;


/**
 * The Class ElasticSearchMonitoringData.
 */
public class ElasticSearchMonitoringData{


	/** The stats. */
	Map<String,Object> stats;

	/** The cluster health. */
	Map<String,Object> clusterHealth;

	/** The cluster state. */
	Map<String,Object> clusterState;

	/** The cluster stats. */
	Map<String,Object> clusterStats;
	
	/** The pending cluster tasks. */
	Map<String,Object> pendingClusterTasks;
	
	/** The status. */
	Map<String,Object> status;
	
	/** The aliases. */
	Map<String,Object> aliases;

	/** The node stats. */
	Map<String,Object> nodesStats;

	/** The nodes info. */
	Map<String,Object> nodesInfo;
	
	/** The indices detail. */
	Map<String, HashMap<String, Object>> indicesDetail;
	
	/**
	 * @return the stats
	 */
	public Map<String, Object> getStats() {
		return stats;
	}

	/**
	 * @param stats the stats to set
	 */
	public void setStats(Map<String, Object> stats) {
		this.stats = stats;
	}

	/**
	 * @return the clusterHealth
	 */
	public Map<String, Object> getClusterHealth() {
		return clusterHealth;
	}

	/**
	 * @param clusterHealth the clusterHealth to set
	 */
	public void setClusterHealth(Map<String, Object> clusterHealth) {
		this.clusterHealth = clusterHealth;
	}

	/**
	 * @return the clusterState
	 */
	public Map<String, Object> getClusterState() {
		return clusterState;
	}

	/**
	 * @param clusterState the clusterState to set
	 */
	public void setClusterState(Map<String, Object> clusterState) {
		this.clusterState = clusterState;
	}

	/**
	 * @return the clusterStats
	 */
	public Map<String, Object> getClusterStats() {
		return clusterStats;
	}

	/**
	 * @param clusterStats the clusterStats to set
	 */
	public void setClusterStats(Map<String, Object> clusterStats) {
		this.clusterStats = clusterStats;
	}

	/**
	 * @return the pendingClusterTasks
	 */
	public Map<String, Object> getPendingClusterTasks() {
		return pendingClusterTasks;
	}

	/**
	 * @param pendingClusterTasks the pendingClusterTasks to set
	 */
	public void setPendingClusterTasks(Map<String, Object> pendingClusterTasks) {
		this.pendingClusterTasks = pendingClusterTasks;
	}

	/**
	 * @return the status
	 */
	public Map<String, Object> getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Map<String, Object> status) {
		this.status = status;
	}

	/**
	 * @return the aliases
	 */
	public Map<String, Object> getAliases() {
		return aliases;
	}

	/**
	 * @param aliases the aliases to set
	 */
	public void setAliases(Map<String, Object> aliases) {
		this.aliases = aliases;
	}

	/**
	 * @return the nodesStats
	 */
	public Map<String, Object> getNodesStats() {
		return nodesStats;
	}

	/**
	 * @param nodesStats the nodesStats to set
	 */
	public void setNodesStats(Map<String, Object> nodesStats) {
		this.nodesStats = nodesStats;
	}

	/**
	 * @return the nodesInfo
	 */
	public Map<String, Object> getNodesInfo() {
		return nodesInfo;
	}

	/**
	 * @param nodesInfo the nodesInfo to set
	 */
	public void setNodesInfo(Map<String, Object> nodesInfo) {
		this.nodesInfo = nodesInfo;
	}
	
	/**
	 * @return the indicesDetail
	 */
	public Map<String, HashMap<String, Object>> getIndicesDetail() {
		return indicesDetail;
	}

	/**
	 * @param indicesDetail the indicesDetail to set
	 */
	public void setIndicesDetail(Map<String, HashMap<String, Object>> indicesDetail) {
		this.indicesDetail = indicesDetail;
	}
}
