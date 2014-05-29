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
package com.impetus.ankush.hadoop.job;

import java.io.Serializable;

/**
 * 
 * @author hokam
 *
 */
public class HadoopClusterMetrics implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int appsSubmitted;
	private int appsRunning;
	private int appsPending;
	private int appsCompleted;
	private int appsFailed;
	private int appsKilled;
	private int reservedMB;
    private int availableMB;
    private int allocatedMB;
    private int totalMB;
	private int containersRunning;
	private int containersPending;
	private int containersReserved;
	private int containersAllocated;
	private int totalNodes;
	private int activeNodes;
	private int decommissionedNodes;
	private int lostNodes;
	private int unhealthyNodes;
	private int rebootedNodes;
	/**
	 * @return the appsSubmitted
	 */
	public int getAppsSubmitted() {
		return appsSubmitted;
	}
	/**
	 * @param appsSubmitted the appsSubmitted to set
	 */
	public void setAppsSubmitted(int appsSubmitted) {
		this.appsSubmitted = appsSubmitted;
	}
	/**
	 * @return the appsRunning
	 */
	public int getAppsRunning() {
		return appsRunning;
	}
	/**
	 * @param appsRunning the appsRunning to set
	 */
	public void setAppsRunning(int appsRunning) {
		this.appsRunning = appsRunning;
	}
	/**
	 * @return the appsPending
	 */
	public int getAppsPending() {
		return appsPending;
	}
	/**
	 * @param appsPending the appsPending to set
	 */
	public void setAppsPending(int appsPending) {
		this.appsPending = appsPending;
	}
	/**
	 * @return the appsCompleted
	 */
	public int getAppsCompleted() {
		return appsCompleted;
	}
	/**
	 * @param appsCompleted the appsCompleted to set
	 */
	public void setAppsCompleted(int appsCompleted) {
		this.appsCompleted = appsCompleted;
	}
	/**
	 * @return the appsFailed
	 */
	public int getAppsFailed() {
		return appsFailed;
	}
	/**
	 * @param appsFailed the appsFailed to set
	 */
	public void setAppsFailed(int appsFailed) {
		this.appsFailed = appsFailed;
	}
	/**
	 * @return the appsKilled
	 */
	public int getAppsKilled() {
		return appsKilled;
	}
	/**
	 * @param appsKilled the appsKilled to set
	 */
	public void setAppsKilled(int appsKilled) {
		this.appsKilled = appsKilled;
	}
	/**
	 * @return the reservedMB
	 */
	public int getReservedMB() {
		return reservedMB;
	}
	/**
	 * @param reservedMB the reservedMB to set
	 */
	public void setReservedMB(int reservedMB) {
		this.reservedMB = reservedMB;
	}
	/**
	 * @return the availableMB
	 */
	public int getAvailableMB() {
		return availableMB;
	}
	/**
	 * @param availableMB the availableMB to set
	 */
	public void setAvailableMB(int availableMB) {
		this.availableMB = availableMB;
	}
	/**
	 * @return the allocatedMB
	 */
	public int getAllocatedMB() {
		return allocatedMB;
	}
	/**
	 * @param allocatedMB the allocatedMB to set
	 */
	public void setAllocatedMB(int allocatedMB) {
		this.allocatedMB = allocatedMB;
	}
	/**
	 * @return the totalMB
	 */
	public int getTotalMB() {
		return totalMB;
	}
	/**
	 * @param totalMB the totalMB to set
	 */
	public void setTotalMB(int totalMB) {
		this.totalMB = totalMB;
	}
	/**
	 * @return the containersRunning
	 */
	public int getContainersRunning() {
		return containersRunning;
	}
	/**
	 * @param containersRunning the containersRunning to set
	 */
	public void setContainersRunning(int containersRunning) {
		this.containersRunning = containersRunning;
	}
	/**
	 * @return the containersPending
	 */
	public int getContainersPending() {
		return containersPending;
	}
	/**
	 * @param containersPending the containersPending to set
	 */
	public void setContainersPending(int containersPending) {
		this.containersPending = containersPending;
	}
	/**
	 * @return the containersReserved
	 */
	public int getContainersReserved() {
		return containersReserved;
	}
	/**
	 * @param containersReserved the containersReserved to set
	 */
	public void setContainersReserved(int containersReserved) {
		this.containersReserved = containersReserved;
	}
	/**
	 * @return the containersAllocated
	 */
	public int getContainersAllocated() {
		return containersAllocated;
	}
	/**
	 * @param containersAllocated the containersAllocated to set
	 */
	public void setContainersAllocated(int containersAllocated) {
		this.containersAllocated = containersAllocated;
	}
	/**
	 * @return the totalNodes
	 */
	public int getTotalNodes() {
		return totalNodes;
	}
	/**
	 * @param totalNodes the totalNodes to set
	 */
	public void setTotalNodes(int totalNodes) {
		this.totalNodes = totalNodes;
	}
	/**
	 * @return the activeNodes
	 */
	public int getActiveNodes() {
		return activeNodes;
	}
	/**
	 * @param activeNodes the activeNodes to set
	 */
	public void setActiveNodes(int activeNodes) {
		this.activeNodes = activeNodes;
	}
	/**
	 * @return the decommissionedNodes
	 */
	public int getDecommissionedNodes() {
		return decommissionedNodes;
	}
	/**
	 * @param decommissionedNodes the decommissionedNodes to set
	 */
	public void setDecommissionedNodes(int decommissionedNodes) {
		this.decommissionedNodes = decommissionedNodes;
	}
	/**
	 * @return the lostNodes
	 */
	public int getLostNodes() {
		return lostNodes;
	}
	/**
	 * @param lostNodes the lostNodes to set
	 */
	public void setLostNodes(int lostNodes) {
		this.lostNodes = lostNodes;
	}
	/**
	 * @return the unhealthyNodes
	 */
	public int getUnhealthyNodes() {
		return unhealthyNodes;
	}
	/**
	 * @param unhealthyNodes the unhealthyNodes to set
	 */
	public void setUnhealthyNodes(int unhealthyNodes) {
		this.unhealthyNodes = unhealthyNodes;
	}
	/**
	 * @return the rebootedNodes
	 */
	public int getRebootedNodes() {
		return rebootedNodes;
	}
	/**
	 * @param rebootedNodes the rebootedNodes to set
	 */
	public void setRebootedNodes(int rebootedNodes) {
		this.rebootedNodes = rebootedNodes;
	}
}
