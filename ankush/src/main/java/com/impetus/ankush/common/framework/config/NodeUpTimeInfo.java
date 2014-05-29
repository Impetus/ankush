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
package com.impetus.ankush.common.framework.config;

import java.io.Serializable;

/**
 * It contains the information about average load, cpu usage and logged in users
 * list on node.
 * 
 * @author hokam.chauhan
 * 
 */
public class NodeUpTimeInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The up time. */
	private Double upTime;
	
	/** The logged users. */
	private String loggedUsers;
	
	/** The load average1. */
	private Double loadAverage1; // Last 1 min
	
	/** The load average2. */
	private Double loadAverage2; // Last 5 min
	
	/** The load average3. */
	private Double loadAverage3; // Last 15 min
	
	/** The cpu usage. */
	private Double cpuUsage = 0D; // cpu usage in percent

	/**
	 * Gets the up time.
	 *
	 * @return the upTime
	 */
	public Double getUpTime() {
		return upTime;
	}

	/**
	 * Sets the up time.
	 *
	 * @param upTime the upTime to set
	 */
	public void setUpTime(Double upTime) {
		this.upTime = upTime;
	}

	/**
	 * Gets the logged users.
	 *
	 * @return the loggedUsers
	 */
	public String getLoggedUsers() {
		return loggedUsers;
	}

	/**
	 * Sets the logged users.
	 *
	 * @param loggedUsers the loggedUsers to set
	 */
	public void setLoggedUsers(String loggedUsers) {
		this.loggedUsers = loggedUsers;
	}

	/**
	 * Gets the load average1.
	 *
	 * @return the loadAverage1
	 */
	public Double getLoadAverage1() {
		return loadAverage1;
	}

	/**
	 * Sets the load average1.
	 *
	 * @param loadAverage1 the loadAverage1 to set
	 */
	public void setLoadAverage1(Double loadAverage1) {
		this.loadAverage1 = loadAverage1;
	}

	/**
	 * Gets the load average2.
	 *
	 * @return the loadAverage2
	 */
	public Double getLoadAverage2() {
		return loadAverage2;
	}

	/**
	 * Sets the load average2.
	 *
	 * @param loadAverage2 the loadAverage2 to set
	 */
	public void setLoadAverage2(Double loadAverage2) {
		this.loadAverage2 = loadAverage2;
	}

	/**
	 * Gets the load average3.
	 *
	 * @return the loadAverage3
	 */
	public Double getLoadAverage3() {
		return loadAverage3;
	}

	/**
	 * Sets the load average3.
	 *
	 * @param loadAverage3 the loadAverage3 to set
	 */
	public void setLoadAverage3(Double loadAverage3) {
		this.loadAverage3 = loadAverage3;
	}

	/**
	 * Sets the cpu usage.
	 *
	 * @param cpuUsage the cpuUsage to set
	 */
	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	/**
	 * Gets the cpu usage.
	 *
	 * @return the cpuUsage
	 */
	public Double getCpuUsage() {
		return cpuUsage;
	}
}
