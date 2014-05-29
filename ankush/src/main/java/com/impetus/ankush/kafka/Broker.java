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
 * The Class Broker.
 * 
 * This Object is populated with the Broker's detail.Some Information to its
 * member variables(like leaderCount and followerCount) are added from
 * list-topic command of Kafka.
 */
public class Broker {
	
	/** The broker id. */
	String brokerId;
	
	/** The broker ip. */
	String brokerIP;
	
	/** The leader count. */
	int leaderCount;
	
	/** The follower count. */
	int followerCount;

	/**
	 * Gets the broker id.
	 *
	 * @return the brokerId
	 */
	public String getBrokerId() {
		return brokerId;
	}

	/**
	 * Sets the broker id.
	 *
	 * @param brokerId the brokerId to set
	 */
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	/**
	 * Gets the broker ip.
	 *
	 * @return the brokerIP
	 */
	public String getBrokerIP() {
		return brokerIP;
	}

	/**
	 * Sets the broker ip.
	 *
	 * @param brokerIP the brokerIP to set
	 */
	public void setBrokerIP(String brokerIP) {
		this.brokerIP = brokerIP;
	}

	/**
	 * Gets the leader count.
	 *
	 * @return the leaderCount
	 */
	public int getLeaderCount() {
		return leaderCount;
	}

	/**
	 * Sets the leader count.
	 *
	 * @param leaderCount the leaderCount to set
	 */
	public void setLeaderCount(int leaderCount) {
		this.leaderCount = leaderCount;
	}

	/**
	 * Gets the follower count.
	 *
	 * @return the followerCount
	 */
	public int getFollowerCount() {
		return followerCount;
	}

	/**
	 * Sets the follower count.
	 *
	 * @param followerCount the followerCount to set
	 */
	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}
}
