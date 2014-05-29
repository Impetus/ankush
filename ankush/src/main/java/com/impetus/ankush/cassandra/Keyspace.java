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

public class Keyspace implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyspaceName;

	private String strategyOptions;

	private String replicationStrategy;

	private Integer cfCount;
	
	private String durableWrites;

	/**
	 * @return the keyspaceName
	 */
	public String getKeyspaceName() {
		return keyspaceName;
	}

	/**
	 * @param keyspaceName the keyspaceName to set
	 */
	public void setKeyspaceName(String keyspaceName) {
		this.keyspaceName = keyspaceName;
	}

	/**
	 * @return the replicationStrategy
	 */
	public String getReplicationStrategy() {
		return replicationStrategy;
	}

	/**
	 * @param replicationStrategy the replicationStrategy to set
	 */
	public void setReplicationStrategy(String replicationStrategy) {
		this.replicationStrategy = replicationStrategy;
	}

	/**
	 * @return the cfCount
	 */
	public Integer getCfCount() {
		return cfCount;
	}

	/**
	 * @param cfCount the cfCount to set
	 */
	public void setCfCount(Integer cfCount) {
		this.cfCount = cfCount;
	}

	
	/**
	 * @return the durableWrites
	 */
	public String getDurableWrites() {
		return durableWrites;
	}

	/**
	 * @param durableWrites the durableWrites to set
	 */
	public void setDurableWrites(String durableWrites) {
		this.durableWrites = durableWrites;
	}

	/**
	 * @return the strategyOptions
	 */
	public String getStrategyOptions() {
		return strategyOptions;
	}

	/**
	 * @param strategyOptions the strategyOptions to set
	 */
	public void setStrategyOptions(String strategyOptions) {
		this.strategyOptions = strategyOptions;
	}

}
