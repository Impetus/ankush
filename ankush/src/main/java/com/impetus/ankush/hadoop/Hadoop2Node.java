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

import java.io.Serializable;
import java.util.Date;

/**
 * Bean used to store the hadoop 2 nodes information.
 * @author hokam
 *
 */
public class Hadoop2Node implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String rack;
	private String state;
	private String id;
	private String nodeHostName;
	private String nodeHTTPAddress;
	private String healthStatus;
	private Date lastHealthUpdate;
	private String healthReport;
	private Integer numContainers;
	private Long usedMemoryMB;
	private Long availMemoryMB;
	/**
	 * @return the rack
	 */
	public String getRack() {
		return rack;
	}
	/**
	 * @param rack the rack to set
	 */
	public void setRack(String rack) {
		this.rack = rack;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the nodeHostName
	 */
	public String getNodeHostName() {
		return nodeHostName;
	}
	/**
	 * @param nodeHostName the nodeHostName to set
	 */
	public void setNodeHostName(String nodeHostName) {
		this.nodeHostName = nodeHostName;
	}
	/**
	 * @return the nodeHTTPAddress
	 */
	public String getNodeHTTPAddress() {
		return nodeHTTPAddress;
	}
	/**
	 * @param nodeHTTPAddress the nodeHTTPAddress to set
	 */
	public void setNodeHTTPAddress(String nodeHTTPAddress) {
		this.nodeHTTPAddress = nodeHTTPAddress;
	}
	/**
	 * @return the healthStatus
	 */
	public String getHealthStatus() {
		return healthStatus;
	}
	/**
	 * @param healthStatus the healthStatus to set
	 */
	public void setHealthStatus(String healthStatus) {
		this.healthStatus = healthStatus;
	}
	/**
	 * @return the lastHealthUpdate
	 */
	public Date getLastHealthUpdate() {
		return lastHealthUpdate;
	}
	/**
	 * @param lastHealthUpdate the lastHealthUpdate to set
	 */
	public void setLastHealthUpdate(Date lastHealthUpdate) {
		this.lastHealthUpdate = lastHealthUpdate;
	}
	/**
	 * @return the healthReport
	 */
	public String getHealthReport() {
		return healthReport;
	}
	/**
	 * @param healthReport the healthReport to set
	 */
	public void setHealthReport(String healthReport) {
		this.healthReport = healthReport;
	}
	/**
	 * @return the numContainers
	 */
	public Integer getNumContainers() {
		return numContainers;
	}
	/**
	 * @param numContainers the numContainers to set
	 */
	public void setNumContainers(Integer numContainers) {
		this.numContainers = numContainers;
	}
	/**
	 * @return the usedMemoryMB
	 */
	public Long getUsedMemoryMB() {
		return usedMemoryMB;
	}
	/**
	 * @param usedMemoryMB the usedMemoryMB to set
	 */
	public void setUsedMemoryMB(Long usedMemoryMB) {
		this.usedMemoryMB = usedMemoryMB;
	}
	/**
	 * @return the availMemoryMB
	 */
	public Long getAvailMemoryMB() {
		return availMemoryMB;
	}
	/**
	 * @param availMemoryMB the availMemoryMB to set
	 */
	public void setAvailMemoryMB(Long availMemoryMB) {
		this.availMemoryMB = availMemoryMB;
	}
}
