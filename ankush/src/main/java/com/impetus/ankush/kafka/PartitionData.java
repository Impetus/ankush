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

/**
 * The Class PartitionData.
 * 
 * This Object is populated with the JMX data of each individual topic for that
 * topic's partitions detail.
 */
public class PartitionData {

	/** The log end offset. */
	private Long logEndOffset;

	/** The num log segments. */
	private Integer numLogSegments;

	/**
	 * Gets the log end offset.
	 * 
	 * @return the logEndOffset
	 */
	public Long getLogEndOffset() {
		return logEndOffset;
	}

	/**
	 * Sets the log end offset.
	 * 
	 * @param logEndOffset
	 *            the logEndOffset to set
	 */
	public void setLogEndOffset(Long logEndOffset) {
		this.logEndOffset = logEndOffset;
	}

	/**
	 * Gets the num log segments.
	 * 
	 * @return the numLogSegments
	 */
	public Integer getNumLogSegments() {
		return numLogSegments;
	}

	/**
	 * Sets the num log segments.
	 * 
	 * @param numLogSegments
	 *            the numLogSegments to set
	 */
	public void setNumLogSegments(Integer numLogSegments) {
		this.numLogSegments = numLogSegments;
	}
}
