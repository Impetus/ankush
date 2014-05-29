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
/**
 * 
 */
package com.impetus.ankush.hadoop.job;

/**
 * @author Akhil
 *
 */
public class Hadoop2HistoryJob extends Hadoop2Job {

	String queue;
	
	String avgMapTime;
	
	String avgReduceTime;
	
	String avgShuffleTime;
	
	String avgMergeTime;

	/**
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * @param queue the queue to set
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * @return the avgMapTime
	 */
	public String getAvgMapTime() {
		return avgMapTime;
	}

	/**
	 * @param avgMapTime the avgMapTime to set
	 */
	public void setAvgMapTime(String avgMapTime) {
		this.avgMapTime = avgMapTime;
	}

	/**
	 * @return the avgReduceTime
	 */
	public String getAvgReduceTime() {
		return avgReduceTime;
	}

	/**
	 * @param avgReduceTime the avgReduceTime to set
	 */
	public void setAvgReduceTime(String avgReduceTime) {
		this.avgReduceTime = avgReduceTime;
	}

	/**
	 * @return the avgShuffleTime
	 */
	public String getAvgShuffleTime() {
		return avgShuffleTime;
	}

	/**
	 * @param avgShuffleTime the avgShuffleTime to set
	 */
	public void setAvgShuffleTime(String avgShuffleTime) {
		this.avgShuffleTime = avgShuffleTime;
	}

	/**
	 * @return the avgMergeTime
	 */
	public String getAvgMergeTime() {
		return avgMergeTime;
	}

	/**
	 * @param avgMergeTime the avgMergeTime to set
	 */
	public void setAvgMergeTime(String avgMergeTime) {
		this.avgMergeTime = avgMergeTime;
	}	
}
