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
import java.util.List;

/**
 * The Class MonitoringInfo.
 * 
 * @author hokam
 */
public class MonitoringInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cpu infos. */
	private List<NodeCpuInfo> cpuInfos;

	/** The memory infos. */
	private List<NodeMemoryInfo> memoryInfos;

	/** The disk infos. */
	private List<NodeDiskInfo> diskInfos;

	/** The uptime infos. */
	private List<NodeUpTimeInfo> uptimeInfos;

	/** The os infos. */
	private List<NodeOSInfo> osInfos;

	/** The swap infos. */
	private List<NodeSwapInfo> swapInfos;
	
	/** The process infos */
	private List<NodeProcessInfo> processMemory;
	
	/** The process infos */
	private List<NodeProcessInfo> processCPU;

	/**
	 * Gets the cpu infos.
	 * 
	 * @return the cpuInfos
	 */
	public List<NodeCpuInfo> getCpuInfos() {
		return cpuInfos;
	}

	/**
	 * Sets the cpu infos.
	 * 
	 * @param cpuInfos
	 *            the cpuInfos to set
	 */
	public void setCpuInfos(List<NodeCpuInfo> cpuInfos) {
		this.cpuInfos = cpuInfos;
	}

	/**
	 * Gets the memory infos.
	 * 
	 * @return the memoryInfos
	 */
	public List<NodeMemoryInfo> getMemoryInfos() {
		return memoryInfos;
	}

	/**
	 * Sets the memory infos.
	 * 
	 * @param memoryInfos
	 *            the memoryInfos to set
	 */
	public void setMemoryInfos(List<NodeMemoryInfo> memoryInfos) {
		this.memoryInfos = memoryInfos;
	}

	/**
	 * Gets the disk infos.
	 * 
	 * @return the diskInfos
	 */
	public List<NodeDiskInfo> getDiskInfos() {
		return diskInfos;
	}

	/**
	 * Sets the disk infos.
	 * 
	 * @param diskInfos
	 *            the diskInfos to set
	 */
	public void setDiskInfos(List<NodeDiskInfo> diskInfos) {
		this.diskInfos = diskInfos;
	}

	/**
	 * Gets the uptime infos.
	 * 
	 * @return the uptimeInfos
	 */
	public List<NodeUpTimeInfo> getUptimeInfos() {
		return uptimeInfos;
	}

	/**
	 * Sets the uptime infos.
	 * 
	 * @param uptimeInfos
	 *            the uptimeInfos to set
	 */
	public void setUptimeInfos(List<NodeUpTimeInfo> uptimeInfos) {
		this.uptimeInfos = uptimeInfos;
	}

	/**
	 * Gets the os infos.
	 * 
	 * @return the osInfos
	 */
	public List<NodeOSInfo> getOsInfos() {
		return osInfos;
	}

	/**
	 * Sets the os infos.
	 * 
	 * @param osInfos
	 *            the osInfos to set
	 */
	public void setOsInfos(List<NodeOSInfo> osInfos) {
		this.osInfos = osInfos;
	}

	/**
	 * Gets the swap infos.
	 * 
	 * @return the swapInfos
	 */
	public List<NodeSwapInfo> getSwapInfos() {
		return swapInfos;
	}

	/**
	 * Sets the swap infos.
	 * 
	 * @param swapInfos
	 *            the swapInfos to set
	 */
	public void setSwapInfos(List<NodeSwapInfo> swapInfos) {
		this.swapInfos = swapInfos;
	}

	/**
	 * @return the processMemory
	 */
	public List<NodeProcessInfo> getProcessMemory() {
		return processMemory;
	}

	/**
	 * @param processMemory the processMemory to set
	 */
	public void setProcessMemory(List<NodeProcessInfo> processMemory) {
		this.processMemory = processMemory;
	}

	/**
	 * @return the processCPU
	 */
	public List<NodeProcessInfo> getProcessCPU() {
		return processCPU;
	}

	/**
	 * @param processCPU the processCPU to set
	 */
	public void setProcessCPU(List<NodeProcessInfo> processCPU) {
		this.processCPU = processCPU;
	}
}
