package com.impetus.ankush2.cassandra.utils;

import java.io.Serializable;

public class ColumnFamily implements Serializable {

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
