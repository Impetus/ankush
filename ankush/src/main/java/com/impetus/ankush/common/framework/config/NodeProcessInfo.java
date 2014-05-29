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
package com.impetus.ankush.common.framework.config;

import java.io.Serializable;
import java.util.Date;

/**
 * Class to Store the process informations.
 * @author hokam
 */
public class NodeProcessInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long pid;
	private String pName;
	private Double memUsage;
	private Double cpuUsage;
	private Double residentMemory;
	private Double virtualMemory;
	private Integer threads;
	private Date startSince;
	private String state;
	
	/**
	 * @return the pid
	 */
	public Long getPid() {
		return pid;
	}
	/**
	 * @param pid the pid to set
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}
	/**
	 * @return the pName
	 */
	public String getpName() {
		return pName;
	}
	/**
	 * @param pName the pName to set
	 */
	public void setpName(String pName) {
		this.pName = pName;
	}
	/**
	 * @return the memUsage
	 */
	public Double getMemUsage() {
		return memUsage;
	}
	/**
	 * @param memUsage the memUsage to set
	 */
	public void setMemUsage(Double memUsage) {
		this.memUsage = memUsage;
	}
	/**
	 * @return the cpuUsage
	 */
	public Double getCpuUsage() {
		return cpuUsage;
	}
	/**
	 * @param cpuUsage the cpuUsage to set
	 */
	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	/**
	 * @return the residentMemory
	 */
	public Double getResidentMemory() {
		return residentMemory;
	}
	/**
	 * @param residentMemory the residentMemory to set
	 */
	public void setResidentMemory(Double residentMemory) {
		this.residentMemory = residentMemory;
	}
	/**
	 * @return the virtualMemory
	 */
	public Double getVirtualMemory() {
		return virtualMemory;
	}
	/**
	 * @param virtualMemory the virtualMemory to set
	 */
	public void setVirtualMemory(Double virtualMemory) {
		this.virtualMemory = virtualMemory;
	}
	/**
	 * @return the threads
	 */
	public Integer getThreads() {
		return threads;
	}
	/**
	 * @param threads the threads to set
	 */
	public void setThreads(Integer threads) {
		this.threads = threads;
	}
	/**
	 * @return the startSince
	 */
	public Date getStartSince() {
		return startSince;
	}
	/**
	 * @param startSince the startSince to set
	 */
	public void setStartSince(Date startSince) {
		this.startSince = startSince;
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

}
