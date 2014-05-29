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
 * It contains the information about the node disk.
 * 
 * @author hokam.chauhan
 * 
 */
public class NodeDiskInfo implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The file system type. */
	private String fileSystemType;
	
	/** The device name. */
	private String deviceName;
	
	/** The dir name. */
	private String dirName;
	
	/** The available memory. */
	private Long availableMemory;
	
	/** The free memory. */
	private Long freeMemory;
	
	/** The total memory. */
	private Long totalMemory;
	
	/** The used memory. */
	private Long usedMemory;

	/**
	 * Gets the file system type.
	 *
	 * @return the fileSystemType
	 */
	public String getFileSystemType() {
		return fileSystemType;
	}

	/**
	 * Sets the file system type.
	 *
	 * @param fileSystemType the fileSystemType to set
	 */
	public void setFileSystemType(String fileSystemType) {
		this.fileSystemType = fileSystemType;
	}

	/**
	 * Gets the device name.
	 *
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Sets the device name.
	 *
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * Gets the dir name.
	 *
	 * @return the dirName
	 */
	public String getDirName() {
		return dirName;
	}

	/**
	 * Sets the dir name.
	 *
	 * @param dirName the dirName to set
	 */
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	/**
	 * Gets the available memory.
	 *
	 * @return the availableMemory
	 */
	public Long getAvailableMemory() {
		return availableMemory;
	}

	/**
	 * Sets the available memory.
	 *
	 * @param availableMemory the availableMemory to set
	 */
	public void setAvailableMemory(Long availableMemory) {
		this.availableMemory = availableMemory;
	}

	/**
	 * Gets the free memory.
	 *
	 * @return the freeMemory
	 */
	public Long getFreeMemory() {
		return freeMemory;
	}

	/**
	 * Sets the free memory.
	 *
	 * @param freeMemory the freeMemory to set
	 */
	public void setFreeMemory(Long freeMemory) {
		this.freeMemory = freeMemory;
	}

	/**
	 * Gets the total memory.
	 *
	 * @return the totalMemory
	 */
	public Long getTotalMemory() {
		return totalMemory;
	}

	/**
	 * Sets the total memory.
	 *
	 * @param totalMemory the totalMemory to set
	 */
	public void setTotalMemory(Long totalMemory) {
		this.totalMemory = totalMemory;
	}

	/**
	 * Gets the used memory.
	 *
	 * @return the usedMemory
	 */
	public Long getUsedMemory() {
		return usedMemory;
	}

	/**
	 * Sets the used memory.
	 *
	 * @param usedMemory the usedMemory to set
	 */
	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}
}
