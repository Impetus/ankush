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
package com.impetus.ankush.hadoop.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author hokam
 */
public class JobTask  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String successfulTaskAttemp;
	private Date startTime;
	private Date finishTime;
	private Long progress;
	private String state;
	private String currentStatus;
	private ArrayList<Counter> counters;

	/**
	 * 
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId
	 *            the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 
	 * @return the successfulTaskAttemp
	 */
	public String getSuccessfulTaskAttemp() {
		return successfulTaskAttemp;
	}

	/**
	 * @param successfulTaskAttemp
	 *            the successfulTaskAttemp to set
	 */
	public void setSuccessfulTaskAttemp(String successfulTaskAttemp) {
		this.successfulTaskAttemp = successfulTaskAttemp;
	}

	/**
	 * 
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 
	 * @return the finishTime
	 */
	public Date getFinishTime() {
		return finishTime;
	}

	/**
	 * @param finishTime
	 *            the finishTime to set
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	/**
	 * 
	 * @return the progress
	 */
	public Long getProgress() {
		return progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(Long progress) {
		this.progress = progress;
	}

	/**
	 * 
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 
	 * @return the currentStatus
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *            the currentStatus to set
	 */
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	/**
	 * 
	 * @return the counters
	 */
	public ArrayList<Counter> getCounters() {
		return counters;
	}

	/**
	 * @param counters
	 *            the counters to set
	 */
	public void setCounters(ArrayList<Counter> counters) {
		this.counters = counters;
	}
}
