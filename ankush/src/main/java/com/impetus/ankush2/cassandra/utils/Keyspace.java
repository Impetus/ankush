package com.impetus.ankush2.cassandra.utils;

import java.io.Serializable;

public class Keyspace implements Serializable {

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
