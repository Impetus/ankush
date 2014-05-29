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
