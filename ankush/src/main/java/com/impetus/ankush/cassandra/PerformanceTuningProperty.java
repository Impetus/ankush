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
package com.impetus.ankush.cassandra;

import java.io.Serializable;

public class PerformanceTuningProperty implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object bloomFilterFalsePositives;

	private Object bloomFilterDiskSpaceUsed;

	private Object recentReadLatencyMicros;

	private Object recentBloomFilterFalseRatios;

	private Object droppableTombStoneRatio;

	private Object bloomFilterFalseRatios;

	private Object totalWriteLatencyMicros;

	private Object recentWriteLatencyMicros;

	private Object recentBloomFilterFalsePositives;

	private Object totalReadLatencyMicros;

	public Object getBloomFilterFalsePositives() {
		return bloomFilterFalsePositives;
	}

	public void setBloomFilterFalsePositives(Object bloomFilterFalsePositives) {
		this.bloomFilterFalsePositives = bloomFilterFalsePositives;
	}

	public Object getBloomFilterDiskSpaceUsed() {
		return bloomFilterDiskSpaceUsed;
	}

	public void setBloomFilterDiskSpaceUsed(Object bloomFilterDiskSpaceUsed) {
		this.bloomFilterDiskSpaceUsed = bloomFilterDiskSpaceUsed;
	}

	public Object getRecentReadLatencyMicros() {
		return recentReadLatencyMicros;
	}

	public void setRecentReadLatencyMicros(Object recentReadLatencyMicros) {
		this.recentReadLatencyMicros = recentReadLatencyMicros;
	}

	public Object getRecentBloomFilterFalseRatios() {
		return recentBloomFilterFalseRatios;
	}

	public void setRecentBloomFilterFalseRatios(Object recentBloomFilterFalseRatios) {
		this.recentBloomFilterFalseRatios = recentBloomFilterFalseRatios;
	}

	public Object getDroppableTombStoneRatio() {
		return droppableTombStoneRatio;
	}

	public void setDroppableTombStoneRatio(Object droppableTombStoneRatio) {
		this.droppableTombStoneRatio = droppableTombStoneRatio;
	}

	public Object getBloomFilterFalseRatios() {
		return bloomFilterFalseRatios;
	}

	public void setBloomFilterFalseRatios(Object bloomFilterFalseRatios) {
		this.bloomFilterFalseRatios = bloomFilterFalseRatios;
	}

	public Object getTotalWriteLatencyMicros() {
		return totalWriteLatencyMicros;
	}

	public void setTotalWriteLatencyMicros(Object totalWriteLatencyMicros) {
		this.totalWriteLatencyMicros = totalWriteLatencyMicros;
	}

	public Object getRecentWriteLatencyMicros() {
		return recentWriteLatencyMicros;
	}

	public void setRecentWriteLatencyMicros(Object recentWriteLatencyMicros) {
		this.recentWriteLatencyMicros = recentWriteLatencyMicros;
	}

	public Object getRecentBloomFilterFalsePositives() {
		return recentBloomFilterFalsePositives;
	}

	public void setRecentBloomFilterFalsePositives(
			Object recentBloomFilterFalsePositives) {
		this.recentBloomFilterFalsePositives = recentBloomFilterFalsePositives;
	}

	public Object getTotalReadLatencyMicros() {
		return totalReadLatencyMicros;
	}

	public void setTotalReadLatencyMicros(Object totalReadLatencyMicros) {
		this.totalReadLatencyMicros = totalReadLatencyMicros;
	}
	
}
