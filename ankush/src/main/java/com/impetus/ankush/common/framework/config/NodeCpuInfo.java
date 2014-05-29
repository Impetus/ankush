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
 * It contains the information about node CPU.
 * 
 * @author hokam.chauhan
 * 
 */
public class NodeCpuInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The vendor. */
	private String vendor;
	
	/** The clock. */
	private Integer clock;
	
	/** The cores. */
	private Integer cores;
	
	/** The cache size. */
	private Long cacheSize;

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets the clock.
	 *
	 * @return the clock
	 */
	public Integer getClock() {
		return clock;
	}

	/**
	 * Sets the clock.
	 *
	 * @param clock the clock to set
	 */
	public void setClock(Integer clock) {
		this.clock = clock;
	}

	/**
	 * Gets the cores.
	 *
	 * @return the cores
	 */
	public Integer getCores() {
		return cores;
	}

	/**
	 * Sets the cores.
	 *
	 * @param cores the cores to set
	 */
	public void setCores(Integer cores) {
		this.cores = cores;
	}

	/**
	 * Gets the cache size.
	 *
	 * @return the cacheSize
	 */
	public Long getCacheSize() {
		return cacheSize;
	}

	/**
	 * Sets the cache size.
	 *
	 * @param cacheSize the cacheSize to set
	 */
	public void setCacheSize(Long cacheSize) {
		this.cacheSize = cacheSize;
	}
}
