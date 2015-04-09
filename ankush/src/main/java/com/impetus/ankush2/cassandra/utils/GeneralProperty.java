package com.impetus.ankush2.cassandra.utils;

import java.io.Serializable;

public class GeneralProperty implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The min row size. */
	private Object minRowSize;

	private Object liveDiskSpaceUsed;
	
	private Object readCount;
	
	private Object meanRowSize;
	
	private Object memtableSwitchCount;
	
	private Object unleveledSSTables;
	
	private Object writeCount;
	
	private Object pendingTasks;
	
	private Object totalDiskSpaceUsed;
	
	private Object memtableColumnsCount;
	
	private Object maxRowSize;
	
	private Object liveSSTableCount;
	
	private Object memtableDataSize;
	
	public Object getMinRowSize() {
		return minRowSize;
	}

	public void setMinRowSize(Object minRowSize) {
		this.minRowSize = minRowSize;
	}

	public Object getLiveDiskSpaceUsed() {
		return liveDiskSpaceUsed;
	}

	public void setLiveDiskSpaceUsed(Object liveDiskSpaceUsed) {
		this.liveDiskSpaceUsed = liveDiskSpaceUsed;
	}

	public Object getReadCount() {
		return readCount;
	}

	public void setReadCount(Object readCount) {
		this.readCount = readCount;
	}

	public Object getMeanRowSize() {
		return meanRowSize;
	}

	public void setMeanRowSize(Object meanRowSize) {
		this.meanRowSize = meanRowSize;
	}

	public Object getMemtableSwitchCount() {
		return memtableSwitchCount;
	}

	public void setMemtableSwitchCount(Object memtableSwitchCount) {
		this.memtableSwitchCount = memtableSwitchCount;
	}

	public Object getUnleveledSSTables() {
		return unleveledSSTables;
	}

	public void setUnleveledSSTables(Object unleveledSSTables) {
		this.unleveledSSTables = unleveledSSTables;
	}

	public Object getWriteCount() {
		return writeCount;
	}

	public void setWriteCount(Object writeCount) {
		this.writeCount = writeCount;
	}

	public Object getPendingTasks() {
		return pendingTasks;
	}

	public void setPendingTasks(Object pendingTasks) {
		this.pendingTasks = pendingTasks;
	}

	public Object getTotalDiskSpaceUsed() {
		return totalDiskSpaceUsed;
	}

	public void setTotalDiskSpaceUsed(Object totalDiskSpaceUsed) {
		this.totalDiskSpaceUsed = totalDiskSpaceUsed;
	}

	public Object getMemtableColumnsCount() {
		return memtableColumnsCount;
	}

	public void setMemtableColumnsCount(Object memtableColumnsCount) {
		this.memtableColumnsCount = memtableColumnsCount;
	}

	public Object getMaxRowSize() {
		return maxRowSize;
	}

	public void setMaxRowSize(Object maxRowSize) {
		this.maxRowSize = maxRowSize;
	}

	public Object getLiveSSTableCount() {
		return liveSSTableCount;
	}

	public void setLiveSSTableCount(Object liveSSTableCount) {
		this.liveSSTableCount = liveSSTableCount;
	}

	public Object getMemtableDataSize() {
		return memtableDataSize;
	}

	public void setMemtableDataSize(Object memtableDataSize) {
		this.memtableDataSize = memtableDataSize;
	}
	
}
