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



import java.util.Map;

/**
 * The Class BrokerData.
 * 
 * This Object is populated with the JMX data of the Broker.
 */
public class BrokerData {
	
	/** The all topics msg in. */
	private Map<String, Object> allTopicsMsgIn;
	
	/** The all topics bytes in. */
	private Map<String, Object> allTopicsBytesIn;
	
	/** The all topics bytes out. */
	private Map<String, Object> allTopicsBytesOut;
	
	/** The log flush details. */
	private Map<String, Object> logFlushDetails;
	
	/** The topics data. */
	private Map<String,TopicData> topicsData;

	/**
	 * Gets the all topics msg in.
	 *
	 * @return the allTopicsMsgIn
	 */
	public Map<String, Object> getAllTopicsMsgIn() {
		return allTopicsMsgIn;
	}
	
	/**
	 * Sets the all topics msg in.
	 *
	 * @param allTopicsMsgIn the allTopicsMsgIn to set
	 */
	public void setAllTopicsMsgIn(Map<String, Object> allTopicsMsgIn) {
		this.allTopicsMsgIn = allTopicsMsgIn;
	}
	
	/**
	 * Gets the all topics bytes in.
	 *
	 * @return the allTopicsBytesIn
	 */
	public Map<String, Object> getAllTopicsBytesIn() {
		return allTopicsBytesIn;
	}
	
	/**
	 * Sets the all topics bytes in.
	 *
	 * @param allTopicsBytesIn the allTopicsBytesIn to set
	 */
	public void setAllTopicsBytesIn(Map<String, Object> allTopicsBytesIn) {
		this.allTopicsBytesIn = allTopicsBytesIn;
	}
	
	/**
	 * Gets the all topics bytes out.
	 *
	 * @return the allTopicsBytesOut
	 */
	public Map<String, Object> getAllTopicsBytesOut() {
		return allTopicsBytesOut;
	}
	
	/**
	 * Sets the all topics bytes out.
	 *
	 * @param allTopicsBytesOut the allTopicsBytesOut to set
	 */
	public void setAllTopicsBytesOut(Map<String, Object> allTopicsBytesOut) {
		this.allTopicsBytesOut = allTopicsBytesOut;
	}

	/**
	 * Gets the log flush details.
	 *
	 * @return the logFlushDetails
	 */
	public Map<String, Object> getLogFlushDetails() {
		return logFlushDetails;
	}
	
	/**
	 * Sets the log flush details.
	 *
	 * @param logFlushDetails the logFlushDetails to set
	 */
	public void setLogFlushDetails(Map<String, Object> logFlushDetails) {
		this.logFlushDetails = logFlushDetails;
	}

	/**
	 * Gets the topics data.
	 *
	 * @return the topicsData
	 */
	public Map<String, TopicData> getTopicsData() {
		return topicsData;
	}
	
	/**
	 * Sets the topics data.
	 *
	 * @param topicsData the topicsData to set
	 */
	public void setTopicsData(Map<String, TopicData> topicsData) {
		this.topicsData = topicsData;
	}
	
}
