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
package com.impetus.ankush.elasticsearch;

/**
 * The Class NewIndex.
 * 
 * This class is used to map the parameters of new Index creation.
 */
public class NewIndex {
	
	/** The index id. */
	String indexId;
	
	/** The shards. */
	int shards;
	
	/** The replicas. */
	int replicas;
	
	/**
	 * Gets the index id.
	 *
	 * @return the indexId
	 */
	public String getIndexId() {
		return indexId;
	}
	
	/**
	 * Sets the index id.
	 *
	 * @param indexId the indexId to set
	 */
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	
	/**
	 * Gets the shards.
	 *
	 * @return the shards
	 */
	public int getShards() {
		return shards;
	}
	
	/**
	 * Sets the shards.
	 *
	 * @param shards the shards to set
	 */
	public void setShards(int shards) {
		this.shards = shards;
	}
	
	/**
	 * Gets the replicas.
	 *
	 * @return the replicas
	 */
	public int getReplicas() {
		return replicas;
	}
	
	/**
	 * Sets the replicas.
	 *
	 * @param replicas the replicas to set
	 */
	public void setReplicas(int replicas) {
		this.replicas = replicas;
	}
	
	

}
