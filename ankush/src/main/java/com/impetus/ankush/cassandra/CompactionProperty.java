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
package com.impetus.ankush.cassandra;

import java.io.Serializable;

public class CompactionProperty implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object maxCompactionThreshold;
	
	private Object minCompactionThreshold;
	
	private String compactionStrategyClass;

	/**
	 * @return the compactionStrategyClass
	 */
	public String getCompactionStrategyClass() {
		return compactionStrategyClass;
	}

	/**
	 * @param compactionStrategyClass the compactionStrategyClass to set
	 */
	public void setCompactionStrategyClass(String compactionStrategyClass) {
		this.compactionStrategyClass = compactionStrategyClass;
	}

	/**
	 * @return the maxCompactionThreshold
	 */
	public Object getMaxCompactionThreshold() {
		return maxCompactionThreshold;
	}

	/**
	 * @param maxCompactionThreshold the maxCompactionThreshold to set
	 */
	public void setMaxCompactionThreshold(Object maxCompactionThreshold) {
		this.maxCompactionThreshold = maxCompactionThreshold;
	}

	/**
	 * @return the minCompactionThreshold
	 */
	public Object getMinCompactionThreshold() {
		return minCompactionThreshold;
	}

	/**
	 * @param minCompactionThreshold the minCompactionThreshold to set
	 */
	public void setMinCompactionThreshold(Object minCompactionThreshold) {
		this.minCompactionThreshold = minCompactionThreshold;
	}
	
}
