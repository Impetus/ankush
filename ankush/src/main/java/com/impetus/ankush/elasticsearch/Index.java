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
 * The Class Index.
 * 
 * This Object is populated with the Index data for all the indices of cluster.
 */
public class Index {
	
	/** The index. */
	String index;
	
	/** The no of docs. */
	String noOfDocs;
	
	/** The primary size. */
	String primarySize;
	
	/** The no of shards. */
	String noOfShards;
	
	/** The no of replicas. */
	String noOfReplicas;
	
	/** The status. */
	String status;
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	
	/**
	 * Sets the index.
	 *
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	
	/**
	 * Gets the no of docs.
	 *
	 * @return the noOfDocs
	 */
	public String getNoOfDocs() {
		return noOfDocs;
	}
	
	/**
	 * Sets the no of docs.
	 *
	 * @param noOfDocs the noOfDocs to set
	 */
	public void setNoOfDocs(String noOfDocs) {
		this.noOfDocs = noOfDocs;
	}
	
	/**
	 * Gets the primary size.
	 *
	 * @return the primarySize
	 */
	public String getPrimarySize() {
		return primarySize;
	}
	
	/**
	 * Sets the primary size.
	 *
	 * @param primarySize the primarySize to set
	 */
	public void setPrimarySize(String primarySize) {
		this.primarySize = primarySize;
	}
	
	/**
	 * Gets the no of shards.
	 *
	 * @return the noOfShards
	 */
	public String getNoOfShards() {
		return noOfShards;
	}
	
	/**
	 * Sets the no of shards.
	 *
	 * @param noOfShards the noOfShards to set
	 */
	public void setNoOfShards(String noOfShards) {
		this.noOfShards = noOfShards;
	}
	
	/**
	 * Gets the no of replicas.
	 *
	 * @return the noOfReplicas
	 */
	public String getNoOfReplicas() {
		return noOfReplicas;
	}
	
	/**
	 * Sets the no of replicas.
	 *
	 * @param noOfReplicas the noOfReplicas to set
	 */
	public void setNoOfReplicas(String noOfReplicas) {
		this.noOfReplicas = noOfReplicas;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
}
