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
 * The Class HadoopJob.
 *
 * @author hokam
 */
public class HadoopJob implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The job state. */
	private String failureInfo;
	
	/** The job state. */
	private String jobState;
	
	/** The job id. */
	private String jobId;
	
	/** The job name. */
	private String jobName;
	
	/** The user name. */
	private String userName;
	
	/** The job priority. */
	private String jobPriority;
	
	/** The scheduling info. */
	private String schedulingInfo;
	
	/** The job start time. */
	private Date jobStartTime;
	
	/** The job complete. */
	private boolean jobComplete;
	
	/** The map progress. */
	private Float mapProgress;
	
	/** The map total. */
	private int mapTotal;
	
	/** The map completed. */
	private int mapCompleted;
	
	/** The reduce progress. */
	private Float reduceProgress;
	
	/** The reduce total. */
	private int reduceTotal;
	
	/** The reduce completed. */
	private int reduceCompleted;
	
	/** The setup progress. */
	private Float setupProgress;

	/** The cleanup progress. */
	private Float cleanupProgress;
	
	/** The tracking URL. */
	private String trackingURL;
	
	/** The Job File. */
	private String jobFile;
	
	/** The Counters. */
	private ArrayList<Counter> counters;
	
	/** The map report. */
	private TaskReport mapReport;

	/** The reduce report. */
	private TaskReport reduceReport;
	
	/** The setup report. */
	private TaskReport setupReport;
	
	/** The cleanup report. */
	private TaskReport cleanupReport;
	
	/**
	 * Gets the job state.
	 *
	 * @return the jobState
	 */
	public String getJobState() {
		return jobState;
	}

	/**
	 * Sets the job state.
	 *
	 * @param jobState the jobState to set
	 */
	public void setJobState(String jobState) {
		this.jobState = jobState;
	}

	/**
	 * Gets the job id.
	 *
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * Sets the job id.
	 *
	 * @param jobId the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * Gets the job name.
	 *
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * Sets the job name.
	 *
	 * @param jobName the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the job priority.
	 *
	 * @return the jobPriority
	 */
	public String getJobPriority() {
		return jobPriority;
	}

	/**
	 * Sets the job priority.
	 *
	 * @param jobPriority the jobPriority to set
	 */
	public void setJobPriority(String jobPriority) {
		this.jobPriority = jobPriority;
	}

	/**
	 * Gets the scheduling info.
	 *
	 * @return the schedulingInfo
	 */
	public String getSchedulingInfo() {
		return schedulingInfo;
	}

	/**
	 * Sets the scheduling info.
	 *
	 * @param schedulingInfo the schedulingInfo to set
	 */
	public void setSchedulingInfo(String schedulingInfo) {
		this.schedulingInfo = schedulingInfo;
	}

	/**
	 * Gets the job start time.
	 *
	 * @return the jobStartTime
	 */
	public Date getJobStartTime() {
		return jobStartTime;
	}

	/**
	 * Sets the job start time.
	 *
	 * @param jobStartTime the jobStartTime to set
	 */
	public void setJobStartTime(Date jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	/**
	 * Checks if is job complete.
	 *
	 * @return the jobComplete
	 */
	public boolean isJobComplete() {
		return jobComplete;
	}

	/**
	 * Sets the job complete.
	 *
	 * @param jobComplete the jobComplete to set
	 */
	public void setJobComplete(boolean jobComplete) {
		this.jobComplete = jobComplete;
	}

	/**
	 * Gets the map progress.
	 *
	 * @return the mapProgress
	 */
	public Float getMapProgress() {
		return mapProgress;
	}

	/**
	 * Sets the map progress.
	 *
	 * @param mapProgress the mapProgress to set
	 */
	public void setMapProgress(Float mapProgress) {
		this.mapProgress = mapProgress;
	}

	/**
	 * Gets the map total.
	 *
	 * @return the mapTotal
	 */
	public int getMapTotal() {
		return mapTotal;
	}

	/**
	 * Sets the map total.
	 *
	 * @param mapTotal the mapTotal to set
	 */
	public void setMapTotal(int mapTotal) {
		this.mapTotal = mapTotal;
	}

	/**
	 * Gets the map completed.
	 *
	 * @return the mapCompleted
	 */
	public int getMapCompleted() {
		return mapCompleted;
	}

	/**
	 * Sets the map completed.
	 *
	 * @param mapCompleted the mapCompleted to set
	 */
	public void setMapCompleted(int mapCompleted) {
		this.mapCompleted = mapCompleted;
	}

	/**
	 * Gets the reduce progress.
	 *
	 * @return the reduceProgress
	 */
	public Float getReduceProgress() {
		return reduceProgress;
	}

	/**
	 * Sets the reduce progress.
	 *
	 * @param reduceProgress the reduceProgress to set
	 */
	public void setReduceProgress(Float reduceProgress) {
		this.reduceProgress = reduceProgress;
	}

	/**
	 * Gets the reduce total.
	 *
	 * @return the reduceTotal
	 */
	public int getReduceTotal() {
		return reduceTotal;
	}

	/**
	 * Sets the reduce total.
	 *
	 * @param reduceTotal the reduceTotal to set
	 */
	public void setReduceTotal(int reduceTotal) {
		this.reduceTotal = reduceTotal;
	}

	/**
	 * Gets the reduce completed.
	 *
	 * @return the reduceCompleted
	 */
	public int getReduceCompleted() {
		return reduceCompleted;
	}

	/**
	 * Sets the reduce completed.
	 *
	 * @param reduceCompleted the reduceCompleted to set
	 */
	public void setReduceCompleted(int reduceCompleted) {
		this.reduceCompleted = reduceCompleted;
	}

	/**
	 * Gets the setup progress.
	 *
	 * @return the setupProgress
	 */
	public Float getSetupProgress() {
		return setupProgress;
	}

	/**
	 * Sets the setup progress.
	 *
	 * @param setupProgress the setupProgress to set
	 */
	public void setSetupProgress(Float setupProgress) {
		this.setupProgress = setupProgress;
	}

	/**
	 * @return the failureInfo
	 */
	public String getFailureInfo() {
		return failureInfo;
	}

	/**
	 * @param failureInfo the failureInfo to set
	 */
	public void setFailureInfo(String failureInfo) {
		this.failureInfo = failureInfo;
	}

	/**
	 * @return the cleanupProgress
	 */
	public Float getCleanupProgress() {
		return cleanupProgress;
	}

	/**
	 * @param cleanupProgress the cleanupProgress to set
	 */
	public void setCleanupProgress(Float cleanupProgress) {
		this.cleanupProgress = cleanupProgress;
	}

	/**
	
	 * @return the trakingURL */
	public String getTrackingURL() {
		return trackingURL;
	}

	/**
	 * @param trakingURL the trakingURL to set
	 */
	public void setTrackingURL(String trackingURL) {
		this.trackingURL = trackingURL;
	}

	/**
	
	 * @return the jobFile */
	public String getJobFile() {
		return jobFile;
	}

	/**
	 * @param jobFile the jobFile to set
	 */
	public void setJobFile(String jobFile) {
		this.jobFile = jobFile;
	}

	/**
	
	 * @return the counters */
	public ArrayList<Counter> getCounters() {
		return counters;
	}

	/**
	 * @param counters the counters to set
	 */
	public void setCounters(ArrayList<Counter> counters) {
		this.counters = counters;
	}

	/**
	
	 * @return the map */
	public TaskReport getMapReport() {
		return mapReport;
	}

	/**
	 * @param map the map to set
	 */
	public void setMapReport(TaskReport mapReport) {
		this.mapReport = mapReport;
	}

	/**
	
	 * @return the reduce */
	public TaskReport getReduceReport() {
		return reduceReport;
	}

	/**
	 * @param reduce the reduce to set
	 */
	public void setReduceReport(TaskReport reduceReport) {
		this.reduceReport = reduceReport;
	}

	/**
	
	 * @return the setup */
	public TaskReport getSetupReport() {
		return setupReport;
	}

	/**
	 * @param setup the setup to set
	 */
	public void setSetupReport(TaskReport setupReport) {
		this.setupReport = setupReport;
	}

	/**
	
	 * @return the cleanup */
	public TaskReport getCleanupReport() {
		return cleanupReport;
	}

	/**
	 * @param cleanup the cleanup to set
	 */
	public void setCleanupReport(TaskReport cleanupReport) {
		this.cleanupReport = cleanupReport;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof HadoopJob))
			return false;
		HadoopJob other = (HadoopJob) obj;
		if (jobId == null) {
			if (other.jobId != null)
				return false;
		} else if (!jobId.equals(other.jobId))
			return false;
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		return true;
	}
}
