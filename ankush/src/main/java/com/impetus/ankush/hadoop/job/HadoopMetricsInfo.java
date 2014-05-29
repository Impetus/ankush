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

/**
 * The Class HadoopMetricsInfo.
 * 
 * @author bgunjan
 */
public class HadoopMetricsInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The system directory name. */
	private String systemDirectoryName = "";

	/** The default maps. */
	private String defaultMaps = "";

	/** The default reduces. */
	private String defaultReduces = "";

	/** The job tracker state. */
	private String jobTrackerState = "";

	/** The map tasks. */
	private String mapTasks = "";

	/** The reduce tasks. */
	private String reduceTasks = "";

	/** The max map tasks capacity. */
	private String maxMapTasksCapacity = "";

	/** The max reduce tasks capacity. */
	private String maxReduceTasksCapacity = "";

	/** The task trackers. */
	private String taskTrackers = "";

	/** The black listed trackers. */
	private String blackListedTrackers = "";

	/** The max memory. (In MB) */
	private String maxMemory = "";

	/** The used memory. (In MB) */
	private String usedMemory = "";

	/** The task tracker expiry interval. */
	private String taskTrackerExpiryInterval = "";

	/** The queue info. */
	private List<String[]> queueInfo;

	/** The total job submission. */
	private String totalJobSubmission = "";

	/** The total job running. */
	private String totalJobRunning = "";

	/** The total jobs completed. */
	private String totalJobsCompleted = "";

	/** The queued job count. */
	private String queuedJobCount = "";

	/** The avarage task per node. */
	private String avarageTaskPerNode = "";

	/** The scheduler type. */
	private String schedulerType = "";

	/**
	 * Gets the system directory name.
	 * 
	 * @return the systemDirectoryName
	 */
	public String getSystemDirectoryName() {
		return systemDirectoryName;
	}

	/**
	 * Sets the system directory name.
	 * 
	 * @param systemDirectoryName
	 *            the systemDirectoryName to set
	 */
	public void setSystemDirectoryName(String systemDirectoryName) {
		this.systemDirectoryName = systemDirectoryName;
	}

	/**
	 * Gets the default maps.
	 * 
	 * @return the defaultMaps
	 */
	public String getDefaultMaps() {
		return defaultMaps;
	}

	/**
	 * Sets the default maps.
	 * 
	 * @param defaultMaps
	 *            the defaultMaps to set
	 */
	public void setDefaultMaps(String defaultMaps) {
		this.defaultMaps = defaultMaps;
	}

	/**
	 * Gets the default reduces.
	 * 
	 * @return the defaultReduces
	 */
	public String getDefaultReduces() {
		return defaultReduces;
	}

	/**
	 * Sets the default reduces.
	 * 
	 * @param defaultReduces
	 *            the defaultReduces to set
	 */
	public void setDefaultReduces(String defaultReduces) {
		this.defaultReduces = defaultReduces;
	}

	/**
	 * Gets the job tracker state.
	 * 
	 * @return the jobTrackerState
	 */
	public String getJobTrackerState() {
		return jobTrackerState;
	}

	/**
	 * Sets the job tracker state.
	 * 
	 * @param jobTrackerState
	 *            the jobTrackerState to set
	 */
	public void setJobTrackerState(String jobTrackerState) {
		this.jobTrackerState = jobTrackerState;
	}

	/**
	 * Gets the map tasks.
	 * 
	 * @return the mapTasks
	 */
	public String getMapTasks() {
		return mapTasks;
	}

	/**
	 * Sets the map tasks.
	 * 
	 * @param mapTasks
	 *            the mapTasks to set
	 */
	public void setMapTasks(String mapTasks) {
		this.mapTasks = mapTasks;
	}

	/**
	 * Gets the reduce tasks.
	 * 
	 * @return the reduceTasks
	 */
	public String getReduceTasks() {
		return reduceTasks;
	}

	/**
	 * Sets the reduce tasks.
	 * 
	 * @param reduceTasks
	 *            the reduceTasks to set
	 */
	public void setReduceTasks(String reduceTasks) {
		this.reduceTasks = reduceTasks;
	}

	/**
	 * Gets the max map tasks capacity.
	 * 
	 * @return the maxMapTasksCapacity
	 */
	public String getMaxMapTasksCapacity() {
		return maxMapTasksCapacity;
	}

	/**
	 * Sets the max map tasks capacity.
	 * 
	 * @param maxMapTasksCapacity
	 *            the maxMapTasksCapacity to set
	 */
	public void setMaxMapTasksCapacity(String maxMapTasksCapacity) {
		this.maxMapTasksCapacity = maxMapTasksCapacity;
	}

	/**
	 * Gets the max reduce tasks capacity.
	 * 
	 * @return the maxReduceTasksCapacity
	 */
	public String getMaxReduceTasksCapacity() {
		return maxReduceTasksCapacity;
	}

	/**
	 * Sets the max reduce tasks capacity.
	 * 
	 * @param maxReduceTasksCapacity
	 *            the maxReduceTasksCapacity to set
	 */
	public void setMaxReduceTasksCapacity(String maxReduceTasksCapacity) {
		this.maxReduceTasksCapacity = maxReduceTasksCapacity;
	}

	/**
	 * Gets the task trackers.
	 * 
	 * @return the taskTrackers
	 */
	public String getTaskTrackers() {
		return taskTrackers;
	}

	/**
	 * Sets the task trackers.
	 * 
	 * @param taskTrackers
	 *            the taskTrackers to set
	 */
	public void setTaskTrackers(String taskTrackers) {
		this.taskTrackers = taskTrackers;
	}

	/**
	 * Gets the black listed trackers.
	 * 
	 * @return the blackListedTrackers
	 */
	public String getBlackListedTrackers() {
		return blackListedTrackers;
	}

	/**
	 * Sets the black listed trackers.
	 * 
	 * @param blackListedTrackers
	 *            the blackListedTrackers to set
	 */
	public void setBlackListedTrackers(String blackListedTrackers) {
		this.blackListedTrackers = blackListedTrackers;
	}

	/**
	 * Gets the max memory.
	 * 
	 * @return the maxMemory
	 */
	public String getMaxMemory() {
		return maxMemory;
	}

	/**
	 * Sets the max memory.
	 * 
	 * @param maxMemory
	 *            the maxMemory to set
	 */
	public void setMaxMemory(String maxMemory) {
		this.maxMemory = maxMemory;
	}

	/**
	 * Gets the used memory.
	 * 
	 * @return the usedMemory
	 */
	public String getUsedMemory() {
		return usedMemory;
	}

	/**
	 * Sets the used memory.
	 * 
	 * @param usedMemory
	 *            the usedMemory to set
	 */
	public void setUsedMemory(String usedMemory) {
		this.usedMemory = usedMemory;
	}

	/**
	 * Gets the task tracker expiry interval.
	 * 
	 * @return the taskTrackerExpiryInterval
	 */
	public String getTaskTrackerExpiryInterval() {
		return taskTrackerExpiryInterval;
	}

	/**
	 * Sets the task tracker expiry interval.
	 * 
	 * @param taskTrackerExpiryInterval
	 *            the taskTrackerExpiryInterval to set
	 */
	public void setTaskTrackerExpiryInterval(String taskTrackerExpiryInterval) {
		this.taskTrackerExpiryInterval = taskTrackerExpiryInterval;
	}

	/**
	 * Gets the queue info.
	 * 
	 * @return the queueInfo
	 */
	public List<String[]> getQueueInfo() {
		return queueInfo;
	}

	/**
	 * Sets the queue info.
	 * 
	 * @param queueInfo
	 *            the queueInfo to set
	 */
	public void setQueueInfo(List<String[]> queueInfo) {
		this.queueInfo = queueInfo;
	}

	/**
	 * Gets the total job submission.
	 * 
	 * @return the totalJobSubmission
	 */
	public String getTotalJobSubmission() {
		return totalJobSubmission;
	}

	/**
	 * Sets the total job submission.
	 * 
	 * @param totalJobSubmission
	 *            the totalJobSubmission to set
	 */
	public void setTotalJobSubmission(String totalJobSubmission) {
		this.totalJobSubmission = totalJobSubmission;
	}

	/**
	 * Gets the total job running.
	 * 
	 * @return the totalJobRunning
	 */
	public String getTotalJobRunning() {
		return totalJobRunning;
	}

	/**
	 * Sets the total job running.
	 * 
	 * @param totalJobRunning
	 *            the totalJobRunning to set
	 */
	public void setTotalJobRunning(String totalJobRunning) {
		this.totalJobRunning = totalJobRunning;
	}

	/**
	 * Gets the total jobs completed.
	 * 
	 * @return the totalJobsCompleted
	 */
	public String getTotalJobsCompleted() {
		return totalJobsCompleted;
	}

	/**
	 * Sets the total jobs completed.
	 * 
	 * @param totalJobsCompleted
	 *            the totalJobsCompleted to set
	 */
	public void setTotalJobsCompleted(String totalJobsCompleted) {
		this.totalJobsCompleted = totalJobsCompleted;
	}

	/**
	 * Gets the queued job count.
	 * 
	 * @return the queuedJobCount
	 */
	public String getQueuedJobCount() {
		return queuedJobCount;
	}

	/**
	 * Sets the queued job count.
	 * 
	 * @param queuedJobCount
	 *            the queuedJobCount to set
	 */
	public void setQueuedJobCount(String queuedJobCount) {
		this.queuedJobCount = queuedJobCount;
	}

	/**
	 * Gets the avarage task per node.
	 * 
	 * @return the avarageTaskPerNode
	 */
	public String getAvarageTaskPerNode() {
		return avarageTaskPerNode;
	}

	/**
	 * Sets the avarage task per node.
	 * 
	 * @param avarageTaskPerNode
	 *            the avarageTaskPerNode to set
	 */
	public void setAvarageTaskPerNode(String avarageTaskPerNode) {
		this.avarageTaskPerNode = avarageTaskPerNode;
	}

	/**
	 * Gets the scheduler type.
	 * 
	 * @return the schedulerType
	 */
	public String getSchedulerType() {
		return schedulerType;
	}

	/**
	 * Sets the scheduler type.
	 * 
	 * @param schedulerType
	 *            the schedulerType to set
	 */
	public void setSchedulerType(String schedulerType) {
		this.schedulerType = schedulerType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HadoopMetricsInfo [systemDirectoryName=" + systemDirectoryName
				+ ", defaultMaps=" + defaultMaps + ", defaultReduces="
				+ defaultReduces + ", jobTrackerState=" + jobTrackerState
				+ ", mapTasks=" + mapTasks + ", reduceTasks=" + reduceTasks
				+ ", maxMapTasksCapacity=" + maxMapTasksCapacity
				+ ", maxReduceTasksCapacity=" + maxReduceTasksCapacity
				+ ", taskTrackers=" + taskTrackers + ", blackListedTrackers="
				+ blackListedTrackers + ", maxMemory=" + maxMemory
				+ ", usedMemory=" + usedMemory + ", taskTrackerExpiryInterval="
				+ taskTrackerExpiryInterval + ", queueInfo=" + queueInfo
				+ ", totalJobSubmission=" + totalJobSubmission
				+ ", totalJobRunning=" + totalJobRunning
				+ ", totalJobsCompleted=" + totalJobsCompleted
				+ ", queuedJobCount=" + queuedJobCount
				+ ", avarageTaskPerNode=" + avarageTaskPerNode
				+ ", schedulerType=" + schedulerType + "]";
	}
}
