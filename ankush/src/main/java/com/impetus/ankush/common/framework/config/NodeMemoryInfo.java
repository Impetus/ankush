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
 * It contains the information about node memory.
 * 
 * @author hokam.chauhan
 * 
 */
public class NodeMemoryInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The total. */
	private Long total;
	
	/** The used. */
	private Long used;
	
	/** The free. */
	private Long free;
	
	/** The Actual free. */
	private Long actualFree;
	
	/** The Actual Used. */
	private Long actualUsed;
	
	/** The used percentage. */
	private Double usedPercentage;
	
	/** The free percentage. */
	private Double freePercentage;

	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * Sets the total.
	 *
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}

	/**
	 * Gets the used.
	 *
	 * @return the used
	 */
	public Long getUsed() {
		return used;
	}

	/**
	 * Sets the used.
	 *
	 * @param used the used to set
	 */
	public void setUsed(Long used) {
		this.used = used;
	}

	/**
	 * Gets the free.
	 *
	 * @return the free
	 */
	public Long getFree() {
		return free;
	}

	/**
	 * Sets the free.
	 *
	 * @param free the free to set
	 */
	public void setFree(Long free) {
		this.free = free;
	}

	/**
	 * @return the actualFree
	 */
	public Long getActualFree() {
		return actualFree;
	}

	/**
	 * @param actualFree the actualFree to set
	 */
	public void setActualFree(Long actualFree) {
		this.actualFree = actualFree;
	}

	/**
	 * @return the actualUsed
	 */
	public Long getActualUsed() {
		return actualUsed;
	}

	/**
	 * @param actualUsed the actualUsed to set
	 */
	public void setActualUsed(Long actualUsed) {
		this.actualUsed = actualUsed;
	}

	/**
	 * Gets the used percentage.
	 *
	 * @return the usedPercentage
	 */
	public Double getUsedPercentage() {
		return usedPercentage;
	}

	/**
	 * Sets the used percentage.
	 *
	 * @param usedPercentage the usedPercentage to set
	 */
	public void setUsedPercentage(Double usedPercentage) {
		this.usedPercentage = usedPercentage;
	}

	/**
	 * Gets the free percentage.
	 *
	 * @return the freePercentage
	 */
	public Double getFreePercentage() {
		return freePercentage;
	}

	/**
	 * Sets the free percentage.
	 *
	 * @param freePercentage the freePercentage to set
	 */
	public void setFreePercentage(Double freePercentage) {
		this.freePercentage = freePercentage;
	}
}
