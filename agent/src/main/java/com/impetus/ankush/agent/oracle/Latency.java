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
package com.impetus.ankush.agent.oracle;

import java.io.Serializable;

import oracle.kv.impl.measurement.LatencyInfo;

/**
 * The Class Latency.
 * 
 * @author impadmin
 */
public class Latency implements Serializable {

	/** The throughput. */
	private long throughput;

	/** The avg. */
	private float avg;

	/** The total ops. */
	private int totalOps;

	/** The percent95. */
	private int percent95;

	/** The percent99. */
	private int percent99;

	/**
	 * Instantiates a new latency.
	 * 
	 * @param latencyInfo
	 *            the latency info
	 */
	public Latency(LatencyInfo latencyInfo) {
		if (latencyInfo != null) {
			throughput = latencyInfo.getThroughputPerSec();
			avg = latencyInfo.getLatency().getAvg();
			totalOps = latencyInfo.getLatency().getTotalOps();
			percent95 = latencyInfo.getLatency().get95thPercent();
			percent99 = latencyInfo.getLatency().get99thPercent();
		}
	}

	/**
	 * Instantiates a new latency.
	 */
	public Latency() {
		throughput = 0;
		avg = 0;
		totalOps = 0;
		percent95 = 0;
		percent99 = 0;
	}

	/**
	 * Gets the throughput.
	 * 
	 * @return the throughput
	 */
	public long getThroughput() {
		return throughput;
	}

	/**
	 * Sets the throughput.
	 * 
	 * @param throughput
	 *            the throughput to set
	 */
	public void setThroughput(long throughput) {
		this.throughput = throughput;
	}

	/**
	 * Gets the avg.
	 * 
	 * @return the avg
	 */
	public float getAvg() {
		return avg;
	}

	/**
	 * Sets the avg.
	 * 
	 * @param avg
	 *            the avg to set
	 */
	public void setAvg(float avg) {
		this.avg = avg;
	}

	/**
	 * Gets the total ops.
	 * 
	 * @return the totalOps
	 */
	public int getTotalOps() {
		return totalOps;
	}

	/**
	 * Sets the total ops.
	 * 
	 * @param totalOps
	 *            the totalOps to set
	 */
	public void setTotalOps(int totalOps) {
		this.totalOps = totalOps;
	}

	/**
	 * Gets the percent95.
	 * 
	 * @return the percent95
	 */
	public int getPercent95() {
		return percent95;
	}

	/**
	 * Sets the percent95.
	 * 
	 * @param percent95
	 *            the percent95 to set
	 */
	public void setPercent95(int percent95) {
		this.percent95 = percent95;
	}

	/**
	 * Gets the percent99.
	 * 
	 * @return the percent99
	 */
	public int getPercent99() {
		return percent99;
	}

	/**
	 * Sets the percent99.
	 * 
	 * @param percent99
	 *            the percent99 to set
	 */
	public void setPercent99(int percent99) {
		this.percent99 = percent99;
	}

}
