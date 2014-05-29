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
package com.impetus.ankush.hadoop.dfs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * The Class DFSReport.
 *
 * @author bgunjan
 */
public class DFSReport  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The compiled. */
	private String compiled;
	
	/** The version. */
	private String version;
	
	//Disk Status
	/** The configured capacity val. */
	private long configuredCapacityVal;
	
	/** The dfs used val. */
	private long dfsUsedVal;
	
	/** The dfs remaining val. */
	private long dfsRemainingVal;
	
	/** The configured capacity. */
	private String configuredCapacity;

	/** The dfs used. */
	private String dfsUsed;
	
	/** The dfs remaining. */
	private String dfsRemaining = "";
	
	/** The non dfs used. */
	private String nonDFSUsed;

	/** The dfs used percent. */
	private float dfsUsedPercent;
	
	/** The dfs remaining percent. */
	private float dfsRemainingPercent;
	
	// DataNode Info
	/** The live nodes count. */
	private int liveNodesCount;
	
	/** The dead nodes count. */
	private int deadNodesCount;
	
	/** The all nodes count. */
	private int allNodesCount;
	
	/** The under replicated block count. */
	private long underReplicatedBlockCount;

	/** The in safemode. */
	private boolean inSafemode;
	
	/** The default replication. */
	private int defaultReplication;
	
	/** The decommission nodes. */
	private int decommissionNodes;
	
	/** The corrupted block count. */
	private long corruptedBlockCount;
	
	/** The default block size. */
	private long defaultBlockSize;
	
	/** The missing block count. */
	private long missingBlockCount;
	
	/** The total raw used. */
	private String totalRawUsed;
	
	/** The dead node info. */
	private List<DFSDataNodesInfo> deadNodeInfo;
	
	/** The live node info. */
	private List<DFSDataNodesInfo> liveNodeInfo;
	
	/** The all node info. */
	private List<DFSDataNodesInfo> allNodeInfo = new ArrayList<DFSDataNodesInfo>();
	
	/**
	 * Gets the compiled.
	 *
	 * @return the compiled
	 */
	@JsonIgnore
	public String getCompiled() {
		return compiled;
	}
	
	/**
	 * Sets the compiled.
	 *
	 * @param compiled the compiled to set
	 */
	public void setCompiled(String compiled) {
		this.compiled = compiled;
	}
	
	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the version.
	 *
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Gets the configured capacity val.
	 *
	 * @return the configuredCapacityVal
	 */
	@JsonIgnore
	public long getConfiguredCapacityVal() {
		return configuredCapacityVal;
	}
	
	/**
	 * Sets the configured capacity val.
	 *
	 * @param configuredCapacityVal the configuredCapacityVal to set
	 */
	public void setConfiguredCapacityVal(long configuredCapacityVal) {
		this.configuredCapacityVal = configuredCapacityVal;
	}
	
	/**
	 * Gets the dfs used val.
	 *
	 * @return the dfsUsedVal
	 */
	@JsonIgnore
	public long getDfsUsedVal() {
		return dfsUsedVal;
	}
	
	/**
	 * Sets the dfs used val.
	 *
	 * @param dfsUsedVal the dfsUsedVal to set
	 */
	public void setDfsUsedVal(long dfsUsedVal) {
		this.dfsUsedVal = dfsUsedVal;
	}
	
	/**
	 * Gets the dfs remaining val.
	 *
	 * @return the dfsRemainingVal
	 */
	@JsonIgnore
	public long getDfsRemainingVal() {
		return dfsRemainingVal;
	}
	
	/**
	 * Sets the dfs remaining val.
	 *
	 * @param dfsRemainingVal the dfsRemainingVal to set
	 */
	public void setDfsRemainingVal(long dfsRemainingVal) {
		this.dfsRemainingVal = dfsRemainingVal;
	}
	
	/**
	 * Gets the configured capacity.
	 *
	 * @return the configuredCapacity
	 */
	public String getConfiguredCapacity() {
		return configuredCapacity;
	}
	
	/**
	 * Sets the configured capacity.
	 *
	 * @param configuredCapacity the configuredCapacity to set
	 */
	public void setConfiguredCapacity(String configuredCapacity) {
		this.configuredCapacity = configuredCapacity;
	}
	
	/**
	 * Gets the dfs used.
	 *
	 * @return the dfsUsed
	 */
	public String getDfsUsed() {
		return dfsUsed;
	}
	
	/**
	 * Sets the dfs used.
	 *
	 * @param dfsUsed the dfsUsed to set
	 */
	public void setDfsUsed(String dfsUsed) {
		this.dfsUsed = dfsUsed;
	}
	
	/**
	 * Gets the dfs remaining.
	 *
	 * @return the dfsRemaining
	 */
	public String getDfsRemaining() {
		return dfsRemaining;
	}
	
	/**
	 * Sets the dfs remaining.
	 *
	 * @param dfsRemaining the dfsRemaining to set
	 */
	public void setDfsRemaining(String dfsRemaining) {
		this.dfsRemaining = dfsRemaining;
	}
	
	/**
	 * Gets the non dfs used.
	 *
	 * @return the nonDFSUsed
	 */
	public String getNonDFSUsed() {
		return nonDFSUsed;
	}
	
	/**
	 * Sets the non dfs used.
	 *
	 * @param nonDFSUsed the nonDFSUsed to set
	 */
	public void setNonDFSUsed(String nonDFSUsed) {
		this.nonDFSUsed = nonDFSUsed;
	}
	
	/**
	 * Gets the dfs used percent.
	 *
	 * @return the dfsUsedPercent
	 */
	public float getDfsUsedPercent() {
		return dfsUsedPercent;
	}
	
	/**
	 * Sets the dfs used percent.
	 *
	 * @param dfsUsedPercent the dfsUsedPercent to set
	 */
	public void setDfsUsedPercent(float dfsUsedPercent) {
		this.dfsUsedPercent = dfsUsedPercent;
	}
	
	/**
	 * Gets the dfs remaining percent.
	 *
	 * @return the dfsRemainingPercent
	 */
	public float getDfsRemainingPercent() {
		return dfsRemainingPercent;
	}
	
	/**
	 * Sets the dfs remaining percent.
	 *
	 * @param dfsRemainingPercent the dfsRemainingPercent to set
	 */
	public void setDfsRemainingPercent(float dfsRemainingPercent) {
		this.dfsRemainingPercent = dfsRemainingPercent;
	}
	
	/**
	 * Gets the live nodes count.
	 *
	 * @return the liveNodesCount
	 */
	public int getLiveNodesCount() {
		return liveNodesCount;
	}
	
	/**
	 * Sets the live nodes count.
	 *
	 * @param liveNodesCount the liveNodesCount to set
	 */
	public void setLiveNodesCount(int liveNodesCount) {
		this.liveNodesCount = liveNodesCount;
	}
	
	/**
	 * Gets the dead nodes count.
	 *
	 * @return the deadNodesCount
	 */
	public int getDeadNodesCount() {
		return deadNodesCount;
	}
	
	/**
	 * Sets the dead nodes count.
	 *
	 * @param deadNodesCount the deadNodesCount to set
	 */
	public void setDeadNodesCount(int deadNodesCount) {
		this.deadNodesCount = deadNodesCount;
	}
	
	/**
	 * Gets the all nodes count.
	 *
	 * @return the allNodesCount
	 */
	public int getAllNodesCount() {
		return allNodesCount;
	}
	
	/**
	 * Sets the all nodes count.
	 *
	 * @param allNodesCount the allNodesCount to set
	 */
	public void setAllNodesCount(int allNodesCount) {
		this.allNodesCount = allNodesCount;
	}
	
	/**
	 * Gets the under replicated block count.
	 *
	 * @return the underReplicatedBlockCount
	 */
	@JsonIgnore
	public long getUnderReplicatedBlockCount() {
		return underReplicatedBlockCount;
	}
	
	/**
	 * Sets the under replicated block count.
	 *
	 * @param underReplicatedBlockCount the underReplicatedBlockCount to set
	 */
	public void setUnderReplicatedBlockCount(long underReplicatedBlockCount) {
		this.underReplicatedBlockCount = underReplicatedBlockCount;
	}
	
	/**
	 * Checks if is in safemode.
	 *
	 * @return the inSafemode
	 */
	public boolean isInSafemode() {
		return inSafemode;
	}
	
	/**
	 * Sets the in safemode.
	 *
	 * @param inSafemode the inSafemode to set
	 */
	public void setInSafemode(boolean inSafemode) {
		this.inSafemode = inSafemode;
	}
	
	/**
	 * Gets the default replication.
	 *
	 * @return the defaultReplication
	 */
	@JsonIgnore
	public int getDefaultReplication() {
		return defaultReplication;
	}
	
	/**
	 * Sets the default replication.
	 *
	 * @param defaultReplication the defaultReplication to set
	 */
	public void setDefaultReplication(int defaultReplication) {
		this.defaultReplication = defaultReplication;
	}
	
	/**
	 * Gets the corrupted block count.
	 *
	 * @return the corruptedBlockCount
	 */
	@JsonIgnore
	public long getCorruptedBlockCount() {
		return corruptedBlockCount;
	}
	
	/**
	 * Sets the corrupted block count.
	 *
	 * @param corruptedBlockCount the corruptedBlockCount to set
	 */
	public void setCorruptedBlockCount(long corruptedBlockCount) {
		this.corruptedBlockCount = corruptedBlockCount;
	}
	
	/**
	 * Gets the default block size.
	 *
	 * @return the defaultBlockSize
	 */
	@JsonIgnore
	public long getDefaultBlockSize() {
		return defaultBlockSize;
	}
	
	/**
	 * Sets the default block size.
	 *
	 * @param defaultBlockSize the defaultBlockSize to set
	 */
	public void setDefaultBlockSize(long defaultBlockSize) {
		this.defaultBlockSize = defaultBlockSize;
	}
	
	/**
	 * Gets the missing block count.
	 *
	 * @return the missingBlockCount
	 */
	@JsonIgnore
	public long getMissingBlockCount() {
		return missingBlockCount;
	}
	
	/**
	 * Sets the missing block count.
	 *
	 * @param missingBlockCount the missingBlockCount to set
	 */
	public void setMissingBlockCount(long missingBlockCount) {
		this.missingBlockCount = missingBlockCount;
	}
	
	/**
	 * Gets the total raw used.
	 *
	 * @return the totalRawUsed
	 */
	@JsonIgnore
	public String getTotalRawUsed() {
		return totalRawUsed;
	}
	
	/**
	 * Sets the total raw used.
	 *
	 * @param totalRawUsed the totalRawUsed to set
	 */
	public void setTotalRawUsed(String totalRawUsed) {
		this.totalRawUsed = totalRawUsed;
	}
	
	/**
	 * Gets the dead node info.
	 *
	 * @return the deadNodeInfo
	 */
	public List<DFSDataNodesInfo> getDeadNodeInfo() {
		return deadNodeInfo;
	}
	
	/**
	 * Sets the dead node info.
	 *
	 * @param deadNodeInfo the deadNodeInfo to set
	 */
	public void setDeadNodeInfo(List<DFSDataNodesInfo> deadNodeInfo) {
		this.deadNodeInfo = deadNodeInfo;
	}
	
	/**
	 * Gets the live node info.
	 *
	 * @return the liveNodeInfo
	 */
	public List<DFSDataNodesInfo> getLiveNodeInfo() {
		return liveNodeInfo;
	}
	
	/**
	 * Sets the live node info.
	 *
	 * @param liveNodeInfo the liveNodeInfo to set
	 */
	public void setLiveNodeInfo(List<DFSDataNodesInfo> liveNodeInfo) {
		this.liveNodeInfo = liveNodeInfo;
	}
	
	/**
	 * Gets the decommission nodes.
	 *
	 * @return the decommissionNodes
	 */
	public int getDecommissionNodes() {
		return decommissionNodes;
	}
	
	/**
	 * Sets the decommission nodes.
	 *
	 * @param decommissionNodes the decommissionNodes to set
	 */
	public void setDecommissionNodes(int decommissionNodes) {
		this.decommissionNodes = decommissionNodes;
	}
	
	/**
	 * Gets the all node info.
	 *
	 * @return the allNodeInfo
	 */
	public List<DFSDataNodesInfo> getAllNodeInfo() {
		return allNodeInfo;
	}
	
	/**
	 * Sets the all node info.
	 *
	 * @param allNodeInfo the allNodeInfo to set
	 */
	public void setAllNodeInfo(List<DFSDataNodesInfo> allNodeInfo) {
		this.allNodeInfo = allNodeInfo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DFSReport [compiled=" + compiled + ", version=" + version
				+ ", configuredCapacityVal=" + configuredCapacityVal
				+ ", dfsUsedVal=" + dfsUsedVal + ", dfsRemainingVal="
				+ dfsRemainingVal + ", configuredCapacity="
				+ configuredCapacity + ", dfsUsed=" + dfsUsed
				+ ", dfsRemaining=" + dfsRemaining + ", nonDFSUsed="
				+ nonDFSUsed + ", dfsUsedPercent=" + dfsUsedPercent
				+ ", dfsRemainingPercent=" + dfsRemainingPercent
				+ ", liveNodesCount=" + liveNodesCount + ", deadNodesCount="
				+ deadNodesCount + ", allNodesCount=" + allNodesCount
				+ ", underReplicatedBlockCount=" + underReplicatedBlockCount
				+ ", inSafemode=" + inSafemode + ", defaultReplication="
				+ defaultReplication + ", decommissionNodes="
				+ decommissionNodes + ", corruptedBlockCount="
				+ corruptedBlockCount + ", defaultBlockSize="
				+ defaultBlockSize + ", missingBlockCount=" + missingBlockCount
				+ ", totalRawUsed=" + totalRawUsed + ", deadNodeInfo="
				+ deadNodeInfo + ", liveNodeInfo=" + liveNodeInfo
				+ ", allNodeInfo=" + allNodeInfo + "]";
	}
}
