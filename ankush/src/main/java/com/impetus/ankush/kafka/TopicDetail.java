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
 * The Class TopicDetail.
 * 
 * This Object is populated from the output of list-topic command of Kafka.It
 * contains all the partitions details for the given topic.
 */
public class TopicDetail {

	/** The partition. */
	String partition;

	/** The leader. */
	String leader;

	/** The replicas. */
	String replicas;

	/** The isr. */
	String isr;

	/**
	 * Gets the partition.
	 * 
	 * @return the partition
	 */
	public String getPartition() {
		return partition;
	}

	/**
	 * Sets the partition.
	 * 
	 * @param partition
	 *            the partition to set
	 */
	public void setPartition(String partition) {
		this.partition = partition;
	}

	/**
	 * Gets the leader.
	 * 
	 * @return the leader
	 */
	public String getLeader() {
		return leader;
	}

	/**
	 * Sets the leader.
	 * 
	 * @param leader
	 *            the leader to set
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}

	/**
	 * Gets the replicas.
	 * 
	 * @return the replicas
	 */
	public String getReplicas() {
		return replicas;
	}

	/**
	 * Sets the replicas.
	 * 
	 * @param replicas
	 *            the replicas to set
	 */
	public void setReplicas(String replicas) {
		this.replicas = replicas;
	}

	/**
	 * Gets the isr.
	 * 
	 * @return the isr
	 */
	public String getIsr() {
		return isr;
	}

	/**
	 * Sets the isr.
	 * 
	 * @param isr
	 *            the isr to set
	 */
	public void setIsr(String isr) {
		this.isr = isr;
	}

}
