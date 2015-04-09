package com.impetus.ankush2.cassandra.utils;

import java.io.Serializable;

public class CompactionProperty implements Serializable {


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
