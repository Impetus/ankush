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

import java.util.Date;

import com.impetus.ankush.hadoop.HadoopUtils;

/**
 * @author Akhil
 * 
 */
public class Hadoop2Job {
	
	String id;
	
	String name;
	
	String startTime;
	
	String finishTime;
		
	String user;
	
	String state;
	
	int mapsTotal;
	
	int mapsCompleted;
	
	int reducesTotal;
	
	int reducesCompleted;
	
	boolean uberized;
	
	String diagnostics;
	
	int failedReduceAttempts;
	
	int killedReduceAttempts;
	
	int successfulReduceAttempts;
	
	int failedMapAttempts;
	
	int killedMapAttempts;
	
	int successfulMapAttempts;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return HadoopUtils.getGmtFromTimeInMillis(startTime);
		//return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the finishTime
	 */
	public String getFinishTime() {
		return HadoopUtils.getGmtFromTimeInMillis(finishTime);
		//return finishTime;
	}

	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the mapsTotal
	 */
	public int getMapsTotal() {
		return mapsTotal;
	}

	/**
	 * @param mapsTotal the mapsTotal to set
	 */
	public void setMapsTotal(int mapsTotal) {
		this.mapsTotal = mapsTotal;
	}

	/**
	 * @return the mapsCompleted
	 */
	public int getMapsCompleted() {
		return mapsCompleted;
	}

	/**
	 * @param mapsCompleted the mapsCompleted to set
	 */
	public void setMapsCompleted(int mapsCompleted) {
		this.mapsCompleted = mapsCompleted;
	}

	/**
	 * @return the reducesTotal
	 */
	public int getReducesTotal() {
		return reducesTotal;
	}

	/**
	 * @param reducesTotal the reducesTotal to set
	 */
	public void setReducesTotal(int reducesTotal) {
		this.reducesTotal = reducesTotal;
	}

	/**
	 * @return the reducesCompleted
	 */
	public int getReducesCompleted() {
		return reducesCompleted;
	}

	/**
	 * @param reducesCompleted the reducesCompleted to set
	 */
	public void setReducesCompleted(int reducesCompleted) {
		this.reducesCompleted = reducesCompleted;
	}

	/**
	 * @return the uberized
	 */
	public boolean isUberized() {
		return uberized;
	}

	/**
	 * @param uberized the uberized to set
	 */
	public void setUberized(boolean uberized) {
		this.uberized = uberized;
	}

	/**
	 * @return the diagnostics
	 */
	public String getDiagnostics() {
		return diagnostics;
	}

	/**
	 * @param diagnostics the diagnostics to set
	 */
	public void setDiagnostics(String diagnostics) {
		this.diagnostics = diagnostics;
	}

	/**
	 * @return the failedReduceAttempts
	 */
	public int getFailedReduceAttempts() {
		return failedReduceAttempts;
	}

	/**
	 * @param failedReduceAttempts the failedReduceAttempts to set
	 */
	public void setFailedReduceAttempts(int failedReduceAttempts) {
		this.failedReduceAttempts = failedReduceAttempts;
	}

	/**
	 * @return the killedReduceAttempts
	 */
	public int getKilledReduceAttempts() {
		return killedReduceAttempts;
	}

	/**
	 * @param killedReduceAttempts the killedReduceAttempts to set
	 */
	public void setKilledReduceAttempts(int killedReduceAttempts) {
		this.killedReduceAttempts = killedReduceAttempts;
	}

	/**
	 * @return the successfulReduceAttempts
	 */
	public int getSuccessfulReduceAttempts() {
		return successfulReduceAttempts;
	}

	/**
	 * @param successfulReduceAttempts the successfulReduceAttempts to set
	 */
	public void setSuccessfulReduceAttempts(int successfulReduceAttempts) {
		this.successfulReduceAttempts = successfulReduceAttempts;
	}

	/**
	 * @return the failedMapAttempts
	 */
	public int getFailedMapAttempts() {
		return failedMapAttempts;
	}

	/**
	 * @param failedMapAttempts the failedMapAttempts to set
	 */
	public void setFailedMapAttempts(int failedMapAttempts) {
		this.failedMapAttempts = failedMapAttempts;
	}

	/**
	 * @return the killedMapAttempts
	 */
	public int getKilledMapAttempts() {
		return killedMapAttempts;
	}

	/**
	 * @param killedMapAttempts the killedMapAttempts to set
	 */
	public void setKilledMapAttempts(int killedMapAttempts) {
		this.killedMapAttempts = killedMapAttempts;
	}

	/**
	 * @return the successfulMapAttempts
	 */
	public int getSuccessfulMapAttempts() {
		return successfulMapAttempts;
	}

	/**
	 * @param successfulMapAttempts the successfulMapAttempts to set
	 */
	public void setSuccessfulMapAttempts(int successfulMapAttempts) {
		this.successfulMapAttempts = successfulMapAttempts;
	}
}
