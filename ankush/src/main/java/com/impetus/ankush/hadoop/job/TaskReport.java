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
package com.impetus.ankush.hadoop.job;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author hokam
 */
public class TaskReport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int completedTask;
	int pendingTask;
	int killedTask;
	int runningTask;
	int failedTask;
	int failedOrKilledTask;
	Map<String, Object[]> diagInfo;
	List<JobTask> tasks;

	/**
	 * 
	 * @return the completedTask
	 */
	public int getCompletedTask() {
		return completedTask;
	}

	/**
	 * @param completedTask
	 *            the completedTask to set
	 */
	public void setCompletedTask(int completedTask) {
		this.completedTask = completedTask;
	}

	/**
	 * 
	 * @return the pendingTask
	 */
	public int getPendingTask() {
		return pendingTask;
	}

	/**
	 * @param pendingTask
	 *            the pendingTask to set
	 */
	public void setPendingTask(int pendingTask) {
		this.pendingTask = pendingTask;
	}

	/**
	 * 
	 * @return the killedTask
	 */
	public int getKilledTask() {
		return killedTask;
	}

	/**
	 * @param killedTask
	 *            the killedTask to set
	 */
	public void setKilledTask(int killedTask) {
		this.killedTask = killedTask;
	}

	/**
	 * 
	 * @return the runningTask
	 */
	public int getRunningTask() {
		return runningTask;
	}

	/**
	 * @param runningTask
	 *            the runningTask to set
	 */
	public void setRunningTask(int runningTask) {
		this.runningTask = runningTask;
	}

	/**
	 * 
	 * @return the failedTask
	 */
	public int getFailedTask() {
		return failedTask;
	}

	/**
	 * @param failedTask
	 *            the failedTask to set
	 */
	public void setFailedTask(int failedTask) {
		this.failedTask = failedTask;
	}

	/**
	 * 
	 * @return the failedOrKilledTask
	 */
	public int getFailedOrKilledTask() {
		return failedOrKilledTask;
	}

	/**
	 * @param failedOrKilledTask
	 *            the failedOrKilledTask to set
	 */
	public void setFailedOrKilledTask(int failedOrKilledTask) {
		this.failedOrKilledTask = failedOrKilledTask;
	}

	/**
	 * 
	 * @return the diagInfo
	 */
	public Map<String, Object[]> getDiagInfo() {
		return diagInfo;
	}

	/**
	 * @param diagInfo
	 *            the diagInfo to set
	 */
	public void setDiagInfo(Map<String, Object[]> diagInfo) {
		this.diagInfo = diagInfo;
	}

	/**
	 * 
	 * @return the tasks
	 */
	public List<JobTask> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks
	 *            the tasks to set
	 */
	public void setTasks(List<JobTask> tasks) {
		this.tasks = tasks;
	}

}
