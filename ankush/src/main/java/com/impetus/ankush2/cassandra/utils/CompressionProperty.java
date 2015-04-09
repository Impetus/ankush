package com.impetus.ankush2.cassandra.utils;

import java.io.Serializable;

public class CompressionProperty implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sstable_compression;

	private Object compressionRatio;

	/**
	 * @return the sstable_compression
	 */
	public String getSstable_compression() {
		return sstable_compression;
	}

	/**
	 * @param sstable_compression the sstable_compression to set
	 */
	public void setSstable_compression(String sstable_compression) {
		this.sstable_compression = sstable_compression;
	}

	/**
	 * @return the compressionRatio
	 */
	public Object getCompressionRatio() {
		return compressionRatio;
	}

	/**
	 * @param compressionRatio the compressionRatio to set
	 */
	public void setCompressionRatio(Object compressionRatio) {
		this.compressionRatio = compressionRatio;
	}

}
