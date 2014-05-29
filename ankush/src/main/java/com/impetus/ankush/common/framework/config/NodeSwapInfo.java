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
 * It contains the information about the node swap area.
 * 
 * @author hokam.chauhan
 * 
 */
public class NodeSwapInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The free system swap. */
	private long freeSystemSwap; // in Bytes
	
	/** The page in. */
	private long pageIn; // Page in Count
	
	/** The page out. */
	private long pageOut; // Page out Count
	
	/** The total system swap. */
	private long totalSystemSwap; // in Bytes
	
	/** The used system swap. */
	private long usedSystemSwap; // in Bytes

	/**
	 * Gets the free system swap.
	 *
	 * @return the freeSystemSwap
	 */
	public long getFreeSystemSwap() {
		return freeSystemSwap;
	}

	/**
	 * Sets the free system swap.
	 *
	 * @param freeSystemSwap the freeSystemSwap to set
	 */
	public void setFreeSystemSwap(long freeSystemSwap) {
		this.freeSystemSwap = freeSystemSwap;
	}

	/**
	 * Gets the page in.
	 *
	 * @return the pageIn
	 */
	public long getPageIn() {
		return pageIn;
	}

	/**
	 * Sets the page in.
	 *
	 * @param pageIn the pageIn to set
	 */
	public void setPageIn(long pageIn) {
		this.pageIn = pageIn;
	}

	/**
	 * Gets the page out.
	 *
	 * @return the pageOut
	 */
	public long getPageOut() {
		return pageOut;
	}

	/**
	 * Sets the page out.
	 *
	 * @param pageOut the pageOut to set
	 */
	public void setPageOut(long pageOut) {
		this.pageOut = pageOut;
	}

	/**
	 * Gets the total system swap.
	 *
	 * @return the totalSystemSwap
	 */
	public long getTotalSystemSwap() {
		return totalSystemSwap;
	}

	/**
	 * Sets the total system swap.
	 *
	 * @param totalSystemSwap the totalSystemSwap to set
	 */
	public void setTotalSystemSwap(long totalSystemSwap) {
		this.totalSystemSwap = totalSystemSwap;
	}

	/**
	 * Gets the used system swap.
	 *
	 * @return the usedSystemSwap
	 */
	public long getUsedSystemSwap() {
		return usedSystemSwap;
	}

	/**
	 * Sets the used system swap.
	 *
	 * @param usedSystemSwap the usedSystemSwap to set
	 */
	public void setUsedSystemSwap(long usedSystemSwap) {
		this.usedSystemSwap = usedSystemSwap;
	}
}
