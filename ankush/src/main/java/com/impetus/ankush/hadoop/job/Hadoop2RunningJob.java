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

import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.job.Hadoop2Job;
/**
 * @author Akhil
 *
 */
public class Hadoop2RunningJob extends Hadoop2Job{

	String elapsedTime;
	
	float mapProgress;
	
	float reduceProgress;
	
	int mapsPending;
	
	int mapsRunning;
	
	int reducesPending;
	
	int reducesRunning;
	
	int newReduceAttempts;
	
	int runningReduceAttempts;
	
	int newMapAttempts;
	
	int runningMapAttempts;

	/**
	 * @return the elapsedTime
	 */
	public String getElapsedTime() {
		return HadoopUtils.convertMillisToTime(elapsedTime);
		//return elapsedTime;
	}

	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the mapProgress
	 */
	public float getMapProgress() {
		return mapProgress;
	}

	/**
	 * @param mapProgress the mapProgress to set
	 */
	public void setMapProgress(float mapProgress) {
		this.mapProgress = mapProgress;
	}

	/**
	 * @return the reduceProgress
	 */
	public float getReduceProgress() {
		return reduceProgress;
	}

	/**
	 * @param reduceProgress the reduceProgress to set
	 */
	public void setReduceProgress(float reduceProgress) {
		this.reduceProgress = reduceProgress;
	}

	/**
	 * @return the mapsPending
	 */
	public int getMapsPending() {
		return mapsPending;
	}

	/**
	 * @param mapsPending the mapsPending to set
	 */
	public void setMapsPending(int mapsPending) {
		this.mapsPending = mapsPending;
	}

	/**
	 * @return the mapsRunning
	 */
	public int getMapsRunning() {
		return mapsRunning;
	}

	/**
	 * @param mapsRunning the mapsRunning to set
	 */
	public void setMapsRunning(int mapsRunning) {
		this.mapsRunning = mapsRunning;
	}

	/**
	 * @return the reducesPending
	 */
	public int getReducesPending() {
		return reducesPending;
	}

	/**
	 * @param reducesPending the reducesPending to set
	 */
	public void setReducesPending(int reducesPending) {
		this.reducesPending = reducesPending;
	}

	/**
	 * @return the reducesRunning
	 */
	public int getReducesRunning() {
		return reducesRunning;
	}

	/**
	 * @param reducesRunning the reducesRunning to set
	 */
	public void setReducesRunning(int reducesRunning) {
		this.reducesRunning = reducesRunning;
	}

	/**
	 * @return the newReduceAttempts
	 */
	public int getNewReduceAttempts() {
		return newReduceAttempts;
	}

	/**
	 * @param newReduceAttempts the newReduceAttempts to set
	 */
	public void setNewReduceAttempts(int newReduceAttempts) {
		this.newReduceAttempts = newReduceAttempts;
	}

	/**
	 * @return the runningReduceAttempts
	 */
	public int getRunningReduceAttempts() {
		return runningReduceAttempts;
	}

	/**
	 * @param runningReduceAttempts the runningReduceAttempts to set
	 */
	public void setRunningReduceAttempts(int runningReduceAttempts) {
		this.runningReduceAttempts = runningReduceAttempts;
	}

	/**
	 * @return the newMapAttempts
	 */
	public int getNewMapAttempts() {
		return newMapAttempts;
	}

	/**
	 * @param newMapAttempts the newMapAttempts to set
	 */
	public void setNewMapAttempts(int newMapAttempts) {
		this.newMapAttempts = newMapAttempts;
	}

	/**
	 * @return the runningMapAttempts
	 */
	public int getRunningMapAttempts() {
		return runningMapAttempts;
	}

	/**
	 * @param runningMapAttempts the runningMapAttempts to set
	 */
	public void setRunningMapAttempts(int runningMapAttempts) {
		this.runningMapAttempts = runningMapAttempts;
	}
}
