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
package com.impetus.ankush.kafka;

/**
 * The Class TopicMetadata.
 * 
 * This class maps the fields which are required for topic creation.
 */
public class TopicMetadata {

	/** The topic name. */
	String topicName;

	/** The partition count. */
	int partitionCount = 1;

	/** The replicas. */
	int replicas = 1;

	/**
	 * Gets the topic name.
	 * 
	 * @return the topicName
	 */
	public String getTopicName() {
		return topicName;
	}

	/**
	 * Sets the topic name.
	 * 
	 * @param topicName
	 *            the topicName to set
	 */
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	/**
	 * Gets the partition count.
	 * 
	 * @return the partitionCount
	 */
	public int getPartitionCount() {
		return partitionCount;
	}

	/**
	 * Sets the partition count.
	 * 
	 * @param partitionCount
	 *            the partitionCount to set
	 */
	public void setPartitionCount(int partitionCount) {
		this.partitionCount = partitionCount;
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
	 * @param replicas
	 *            the replicas to set
	 */
	public void setReplicas(int replicas) {
		this.replicas = replicas;
	}
}
