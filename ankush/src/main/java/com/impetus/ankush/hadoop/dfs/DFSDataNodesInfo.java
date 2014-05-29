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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The Class DFSDataNodesInfo.
 *
 * @author bgunjan
 */
public class DFSDataNodesInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The configured capacity. */
	private String configuredCapacity;
	
	/** The dfs used. */
	private String dfsUsed;
	
	/** The non dfs used. */
	private String nonDfsUsed;
	
	/** The dfs remaining. */
	private String dfsRemaining;
	
	/** The used percent. */
	private float usedPercent;
	
	/** The remaining percent. */
	private float remainingPercent;
	
	/** The host. */
	private String host;
	
	/** The datanode name. */
	private String datanodeName;
	
	/** The admin state. */
	private String adminState;
	
	/** The last contact. */
	private String lastContact;
	
	/** The storage id. */
	private String storageID;
	
	/** The info port. */
	private int infoPort;
	
	/** The port. */
	private int port;

	/** The capacity val. */
	private long capacityVal;
	
	/** The dfs used val. */
	private long dfsUsedVal;
	
	/** The non dfs used val. */
	private long nonDFSUsedVal;
	
	/** The remaining dfs val. */
	private long remainingDFSVal;

	/** The data node report. */
	private String dataNodeReport;
	
	/** The decommissioned. */
	private boolean decommissioned;
	
	/** The decommission in progress. */
	private boolean decommissionInProgress;
	
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
	 * Gets the non dfs used.
	 *
	 * @return the nonDfsUsed
	 */
	public String getNonDfsUsed() {
		return nonDfsUsed;
	}

	/**
	 * Sets the non dfs used.
	 *
	 * @param nonDfsUsed the nonDfsUsed to set
	 */
	public void setNonDfsUsed(String nonDfsUsed) {
		this.nonDfsUsed = nonDfsUsed;
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
	 * Gets the used percent.
	 *
	 * @return the usedPercent
	 */
	public float getUsedPercent() {
		return usedPercent;
	}

	/**
	 * Sets the used percent.
	 *
	 * @param usedPercent the usedPercent to set
	 */
	public void setUsedPercent(float usedPercent) {
		this.usedPercent = usedPercent;
	}

	/**
	 * Gets the remaining percent.
	 *
	 * @return the remainingPercent
	 */
	public float getRemainingPercent() {
		return remainingPercent;
	}

	/**
	 * Sets the remaining percent.
	 *
	 * @param remainingPercent the remainingPercent to set
	 */
	public void setRemainingPercent(float remainingPercent) {
		this.remainingPercent = remainingPercent;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host.
	 *
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Gets the datanode name.
	 *
	 * @return the datanodeName
	 */
	public String getDatanodeName() {
		return datanodeName;
	}

	/**
	 * Sets the datanode name.
	 *
	 * @param datanodeName the datanodeName to set
	 */
	public void setDatanodeName(String datanodeName) {
		this.datanodeName = datanodeName;
	}

	/**
	 * Gets the admin state.
	 *
	 * @return the adminState
	 */
	public String getAdminState() {
		return adminState;
	}

	/**
	 * Sets the admin state.
	 *
	 * @param adminState the adminState to set
	 */
	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}

	/**
	 * Gets the last contact.
	 *
	 * @return the lastContact
	 */
	public String getLastContact() {
		return lastContact;
	}

	/**
	 * Sets the last contact.
	 *
	 * @param lastContact the lastContact to set
	 */
	public void setLastContact(String lastContact) {
		this.lastContact = lastContact;
	}

	/**
	 * Gets the storage id.
	 *
	 * @return the storageID
	 */
	@JsonIgnore
	public String getStorageID() {
		return storageID;
	}

	/**
	 * Sets the storage id.
	 *
	 * @param storageID the storageID to set
	 */
	public void setStorageID(String storageID) {
		this.storageID = storageID;
	}

	/**
	 * Gets the info port.
	 *
	 * @return the infoPort
	 */
	@JsonIgnore
	public int getInfoPort() {
		return infoPort;
	}

	/**
	 * Sets the info port.
	 *
	 * @param infoPort the infoPort to set
	 */
	public void setInfoPort(int infoPort) {
		this.infoPort = infoPort;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	@JsonIgnore
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets the capacity val.
	 *
	 * @return the capacityVal
	 */
	@JsonIgnore
	public long getCapacityVal() {
		return capacityVal;
	}

	/**
	 * Sets the capacity val.
	 *
	 * @param capacityVal the capacityVal to set
	 */
	public void setCapacityVal(long capacityVal) {
		this.capacityVal = capacityVal;
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
	 * Gets the non dfs used val.
	 *
	 * @return the nonDFSUsedVal
	 */
	@JsonIgnore
	public long getNonDFSUsedVal() {
		return nonDFSUsedVal;
	}

	/**
	 * Sets the non dfs used val.
	 *
	 * @param nonDFSUsedVal the nonDFSUsedVal to set
	 */
	public void setNonDFSUsedVal(long nonDFSUsedVal) {
		this.nonDFSUsedVal = nonDFSUsedVal;
	}

	/**
	 * Gets the remaining dfs val.
	 *
	 * @return the remainingDFSVal
	 */
	@JsonIgnore
	public long getRemainingDFSVal() {
		return remainingDFSVal;
	}

	/**
	 * Sets the remaining dfs val.
	 *
	 * @param remainingDFSVal the remainingDFSVal to set
	 */
	public void setRemainingDFSVal(long remainingDFSVal) {
		this.remainingDFSVal = remainingDFSVal;
	}

	/**
	 * Gets the data node report.
	 *
	 * @return the dataNodeReport
	 */
	@JsonIgnore
	public String getDataNodeReport() {
		return dataNodeReport;
	}

	/**
	 * Sets the data node report.
	 *
	 * @param dataNodeReport the dataNodeReport to set
	 */
	public void setDataNodeReport(String dataNodeReport) {
		this.dataNodeReport = dataNodeReport;
	}

	/**
	 * Checks if is decommissioned.
	 *
	 * @return the decommissioned
	 */
	@JsonIgnore
	public boolean isDecommissioned() {
		return decommissioned;
	}

	/**
	 * Sets the decommissioned.
	 *
	 * @param decommissioned the decommissioned to set
	 */
	public void setDecommissioned(boolean decommissioned) {
		this.decommissioned = decommissioned;
	}

	/**
	 * Checks if is decommission in progress.
	 *
	 * @return the decommissionInProgress
	 */
	@JsonIgnore
	public boolean isDecommissionInProgress() {
		return decommissionInProgress;
	}

	/**
	 * Sets the decommission in progress.
	 *
	 * @param decommissionInProgress the decommissionInProgress to set
	 */
	public void setDecommissionInProgress(boolean decommissionInProgress) {
		this.decommissionInProgress = decommissionInProgress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DFSDataNodesInfo [configuredCapacity=" + configuredCapacity
				+ ", dfsUsed=" + dfsUsed + ", nonDfsUsed=" + nonDfsUsed
				+ ", dfsRemaining=" + dfsRemaining + ", usedPercent="
				+ usedPercent + ", remainingPercent=" + remainingPercent
				+ ", host=" + host + ", datanodeName=" + datanodeName
				+ ", adminState=" + adminState + ", lastContact=" + lastContact
				+ ", storageID=" + storageID + ", infoPort=" + infoPort
				+ ", port=" + port + ", capacityVal=" + capacityVal
				+ ", dfsUsedVal=" + dfsUsedVal + ", nonDFSUsedVal="
				+ nonDFSUsedVal + ", remainingDFSVal=" + remainingDFSVal
				+ ", dataNodeReport=" + dataNodeReport + ", decommissioned="
				+ decommissioned + ", decommissionInProgress="
				+ decommissionInProgress + "]";
	}
	
	
}
