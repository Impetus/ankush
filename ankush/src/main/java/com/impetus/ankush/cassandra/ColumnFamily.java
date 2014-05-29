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

public class ColumnFamily implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String columnFamilyName;

	private Object liveSSTableCount;

	private Object writelatency;
	
	private Object readLatency;

	private Object pendingTasks;
	
	/**
	 * @return the columnFamilyName
	 */
	public String getColumnFamilyName() {
		return columnFamilyName;
	}

	/**
	 * @param columnFamilyName the columnFamilyName to set
	 */
	public void setColumnFamilyName(String columnFamilyName) {
		this.columnFamilyName = columnFamilyName;
	}

	/**
	 * @return the liveSSTableCount
	 */
	public Object getLiveSSTableCount() {
		return liveSSTableCount;
	}

	/**
	 * @param liveSSTableCount the liveSSTableCount to set
	 */
	public void setLiveSSTableCount(Object liveSSTableCount) {
		this.liveSSTableCount = liveSSTableCount;
	}

	/**
	 * @return the writelatency
	 */
	public Object getWritelatency() {
		return writelatency;
	}

	/**
	 * @param writelatency the writelatency to set
	 */
	public void setWritelatency(Object writelatency) {
		this.writelatency = writelatency;
	}

	/**
	 * @return the pendingTasks
	 */
	public Object getPendingTasks() {
		return pendingTasks;
	}

	/**
	 * @param pendingTasks the pendingTasks to set
	 */
	public void setPendingTasks(Object pendingTasks) {
		this.pendingTasks = pendingTasks;
	}

	/**
	 * @return the readLatency
	 */
	public Object getReadLatency() {
		return readLatency;
	}

	/**
	 * @param readLatency the readLatency to set
	 */
	public void setReadLatency(Object readLatency) {
		this.readLatency = readLatency;
	}

}
