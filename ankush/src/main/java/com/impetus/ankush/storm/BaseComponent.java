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
package com.impetus.ankush.storm;

import java.io.Serializable;

public class BaseComponent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private int numberOfExecutors;

	private int numberOfTasks;

	private long emitted;

	private long transferred;

	private long acked;

	private long failed;

	private String lastError;
	
	private int parallelism;
	
	private String type;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the numberOfExecutors
	 */
	public int getNumberOfExecutors() {
		return numberOfExecutors;
	}

	/**
	 * @param numberOfExecutors the numberOfExecutors to set
	 */
	public void setNumberOfExecutors(int numberOfExecutors) {
		this.numberOfExecutors = numberOfExecutors;
	}

	/**
	 * @return the numberOfTasks
	 */
	public int getNumberOfTasks() {
		return numberOfTasks;
	}

	/**
	 * @param numberOfTasks the numberOfTasks to set
	 */
	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	/**
	 * @return the emitted
	 */
	public long getEmitted() {
		return emitted;
	}

	/**
	 * @param emitted the emitted to set
	 */
	public void setEmitted(long emitted) {
		this.emitted = emitted;
	}

	/**
	 * @return the transferred
	 */
	public long getTransferred() {
		return transferred;
	}

	/**
	 * @param transferred the transferred to set
	 */
	public void setTransferred(long transferred) {
		this.transferred = transferred;
	}

	/**
	 * @return the acked
	 */
	public long getAcked() {
		return acked;
	}

	/**
	 * @param acked the acked to set
	 */
	public void setAcked(long acked) {
		this.acked = acked;
	}

	/**
	 * @return the failed
	 */
	public long getFailed() {
		return failed;
	}

	/**
	 * @param failed the failed to set
	 */
	public void setFailed(long failed) {
		this.failed = failed;
	}

	/**
	 * @return the lastError
	 */
	public String getLastError() {
		return lastError;
	}

	/**
	 * @param lastError the lastError to set
	 */
	public void setLastError(String lastError) {
		this.lastError = lastError;
	}

	/**
	 * @return the parallelism
	 */
	public int getParallelism() {
		return parallelism;
	}

	/**
	 * @param parallelism the parallelism to set
	 */
	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	
}
