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
package com.impetus.ankush2.hadoop.config;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;


// TODO: Auto-generated Javadoc
/**
 * The Class Hadoop2Application.
 *
 * @author Akhil
 */
public class Hadoop2Application implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private String id;
	
	/** The user. */
	private String user;
	
	/** The name. */
	private String name;
	
	/** The queue. */
	private String queue;
	
	/** The state. */
	private String state;
	
	/** The final status. */
	private String finalStatus;
	
	private String applicationType;
	/** The progress. */
	private String progress;
	
	/** The tracking ui. */
	private String trackingUI;
	
	/** The tracking url. */
	private String trackingUrl;
	
	/** The diagnostics. */
	private String diagnostics;
	
	/** The cluster id. */
	private String clusterId; 
	
	/** The started time. */
	private String startedTime;
	
	/** The finished time. */
	private String finishedTime;
	
	/** The elapsed time. */
	private String elapsedTime;
	
	/** The am container logs. */
	private String amContainerLogs;
	
	/** The am host http address. */
	private String amHostHttpAddress;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * Sets the queue.
	 *
	 * @param queue the queue to set
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the final status.
	 *
	 * @return the finalStatus
	 */
	public String getFinalStatus() {
		return finalStatus;
	}

	/**
	 * Sets the final status.
	 *
	 * @param finalStatus the finalStatus to set
	 */
	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	/**
	 * @return the applicationType
	 */
	public String getApplicationType() {
		return applicationType;
	}

	/**
	 * @param applicationType the applicationType to set
	 */
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	/**
	 * Gets the progress.
	 *
	 * @return the progress
	 */
	public String getProgress() {
		return progress;
	}

	/**
	 * Sets the progress.
	 *
	 * @param progress the progress to set
	 */
	public void setProgress(String progress) {
		this.progress = progress;
	}

	/**
	 * Gets the tracking ui.
	 *
	 * @return the trackingUI
	 */
	public String getTrackingUI() {
		return trackingUI;
	}

	/**
	 * Sets the tracking ui.
	 *
	 * @param trackingUI the trackingUI to set
	 */
	public void setTrackingUI(String trackingUI) {
		this.trackingUI = trackingUI;
	}

	/**
	 * Gets the tracking url.
	 *
	 * @return the trackingUrl
	 */
	@JsonIgnore
	public String getTrackingUrl() {
		return trackingUrl;
	}

	/**
	 * Sets the tracking url.
	 *
	 * @param trackingUrl the trackingUrl to set
	 */
	
	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	/**
	 * Gets the diagnostics.
	 *
	 * @return the diagnostics
	 */
	public String getDiagnostics() {
		return diagnostics;
	}

	/**
	 * Sets the diagnostics.
	 *
	 * @param diagnostics the diagnostics to set
	 */
	public void setDiagnostics(String diagnostics) {
		this.diagnostics = diagnostics;
	}

	/**
	 * Gets the cluster id.
	 *
	 * @return the clusterId
	 */
	public String getClusterId() {
		return clusterId;
	}

	/**
	 * Sets the cluster id.
	 *
	 * @param clusterId the clusterId to set
	 */
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	/**
	 * Gets the started time.
	 *
	 * @return the startedTime
	 */
	public String getStartedTime() {
		return HadoopUtils.getGmtFromTimeInMillis(startedTime);
	}

	/**
	 * Sets the started time.
	 *
	 * @param startedTime the startedTime to set
	 */
	public void setStartedTime(String startedTime) {
		this.startedTime = startedTime;
	}

	/**
	 * Gets the finished time.
	 *
	 * @return the finishedTime
	 */
	public String getFinishedTime() {
		return HadoopUtils.getGmtFromTimeInMillis(finishedTime);
	}

	/**
	 * Sets the finished time.
	 *
	 * @param finishedTime the finishedTime to set
	 */
	public void setFinishedTime(String finishedTime) {
		this.finishedTime = finishedTime;
	}

	/**
	 * Gets the elapsed time.
	 *
	 * @return the elapsedTime
	 */
	public String getElapsedTime() {
		return HadoopUtils.convertMillisToTime(elapsedTime);
	}

	/**
	 * Sets the elapsed time.
	 *
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * Gets the am container logs.
	 *
	 * @return the amContainerLogs
	 */
	@JsonIgnore
	public String getAmContainerLogs() {
		return amContainerLogs;
	}

	/**
	 * Sets the am container logs.
	 *
	 * @param amContainerLogs the amContainerLogs to set
	 */
	public void setAmContainerLogs(String amContainerLogs) {
		this.amContainerLogs = amContainerLogs;
	}

	/**
	 * Gets the am host http address.
	 *
	 * @return the amHostHttpAddress
	 */
	
	public String getAmHostHttpAddress() {
		return amHostHttpAddress;
	}

	/**
	 * Sets the am host http address.
	 *
	 * @param amHostHttpAddress the amHostHttpAddress to set
	 */
	public void setAmHostHttpAddress(String amHostHttpAddress) {
		this.amHostHttpAddress = amHostHttpAddress;
	}

	@JsonIgnore
	public boolean isMapredType() {
		if(this.applicationType != null) {
			if(this.applicationType.equals(HadoopConstants.YARN.APPTYPE_MAPRED)) {
				return true;		
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Hadoop2Application [id=" + id + ", user=" + user + ", name="
				+ name + ", queue=" + queue + ", state=" + state
				+ ", finalStatus=" + finalStatus + ", applicationType="
				+ applicationType + ", progress=" + progress + ", trackingUI="
				+ trackingUI + ", trackingUrl=" + trackingUrl
				+ ", diagnostics=" + diagnostics + ", clusterId=" + clusterId
				+ ", startedTime=" + startedTime + ", finishedTime="
				+ finishedTime + ", elapsedTime=" + elapsedTime
				+ ", amContainerLogs=" + amContainerLogs
				+ ", amHostHttpAddress=" + amHostHttpAddress + "]";
	}
}
