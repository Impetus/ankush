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
 * The Class TopicData.
 * 
 * This Object is populated with the JMX data of individual topic.
 */
public class TopicData {
	
	/** The msg in. */
	private Map<String, Object> msgIn;
	
	/** The bytes in. */
	private Map<String, Object> bytesIn;
	
	/** The bytes out. */
	private Map<String, Object> bytesOut;
	
	/** The partition data. */
	private Map<String,PartitionData> partitionData;

	/**
	 * Gets the msg in.
	 *
	 * @return the msgIn
	 */
	public Map<String, Object> getMsgIn() {
		return msgIn;
	}
	
	/**
	 * Sets the msg in.
	 *
	 * @param msgIn the msgIn to set
	 */
	public void setMsgIn(Map<String, Object> msgIn) {
		this.msgIn = msgIn;
	}
	
	/**
	 * Gets the bytes in.
	 *
	 * @return the bytesIn
	 */
	public Map<String, Object> getBytesIn() {
		return bytesIn;
	}
	
	/**
	 * Sets the bytes in.
	 *
	 * @param bytesIn the bytesIn to set
	 */
	public void setBytesIn(Map<String, Object> bytesIn) {
		this.bytesIn = bytesIn;
	}
	
	/**
	 * Gets the bytes out.
	 *
	 * @return the bytesOut
	 */
	public Map<String, Object> getBytesOut() {
		return bytesOut;
	}
	
	/**
	 * Sets the bytes out.
	 *
	 * @param bytesOut the bytesOut to set
	 */
	public void setBytesOut(Map<String, Object> bytesOut) {
		this.bytesOut = bytesOut;
	}
	 
	/**
	 * Gets the partition data.
	 *
	 * @return the partitionData
	 */
	public Map<String,PartitionData> getPartitionData() {
		return partitionData;
	}
	
	/**
	 * Sets the partition data.
	 *
	 * @param partitionData the partitionData to set
	 */
	public void setPartitionData(Map<String,PartitionData> partitionData) {
		this.partitionData = partitionData;
	}
	 
	
}
