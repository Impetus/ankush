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
package com.impetus.ankush.agent.storm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mayur
 * 
 */
public class Topology {

	private String topolgoyId;

	private String name;

	private int numberOfTasks;

	private int numberOfWorkers;
	
	private int numberOfExecutors;

	private String status;

	private int uptime;

	private List<Spout> spouts = new ArrayList<Spout>();

	private List<Bolt> bolts = new ArrayList<Bolt>();

	private Map rawTopologyData;
	
	/**
	 * @return the topolgoyId
	 */
	public String getTopolgoyId() {
		return topolgoyId;
	}

	/**
	 * @param topolgoyId
	 *            the topolgoyId to set
	 */
	public void setTopolgoyId(String topolgoyId) {
		this.topolgoyId = topolgoyId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the numberOfTasks
	 */
	public int getNumberOfTasks() {
		return numberOfTasks;
	}

	/**
	 * @param numberOfTasks
	 *            the numberOfTasks to set
	 */
	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	/**
	 * @return the numberOfWorkers
	 */
	public int getNumberOfWorkers() {
		return numberOfWorkers;
	}

	/**
	 * @param numberOfWorkers
	 *            the numberOfWorkers to set
	 */
	public void setNumberOfWorkers(int numberOfWorkers) {
		this.numberOfWorkers = numberOfWorkers;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the uptime
	 */
	public int getUptime() {
		return uptime;
	}

	/**
	 * @param uptime
	 *            the uptime to set
	 */
	public void setUptime(int uptime) {
		this.uptime = uptime;
	}

	/**
	 * @return the spouts
	 */
	public List<Spout> getSpouts() {
		return spouts;
	}

	/**
	 * @param spouts
	 *            the spouts to set
	 */
	public void setSpouts(List<Spout> spouts) {
		this.spouts = spouts;
	}

	/**
	 * @param bolts
	 *            the bolts to set
	 */
	public void addSpout(Spout spout) {
		this.spouts.add(spout);
	}

	/**
	 * @return the bolts
	 */
	public List<Bolt> getBolts() {
		return bolts;
	}

	/**
	 * @param bolts
	 *            the bolts to set
	 */
	public void setBolts(List<Bolt> bolts) {
		this.bolts = bolts;
	}

	/**
	 * @param bolts
	 *            the bolts to set
	 */
	public void addBolt(Bolt bolt) {
		this.bolts.add(bolt);
	}

	/**
	 * @return the rawTopologyData
	 */
	public Map getRawTopologyData() {
		return rawTopologyData;
	}

	/**
	 * @param rawTopologyData
	 *            the rawTopologyData to set
	 */
	public void setRawTopologyData(Map rawTopologyData) {
		this.rawTopologyData = rawTopologyData;
	}

	/**
	 * 
	 * @param numOfExecutors
	 */
	public void setNumberOfExecutors(int numOfExecutors) {
		this.numberOfExecutors = numOfExecutors;
	}
	
	/**
	 * @return the number of executors
	 */
	public int getNumberOfExecutors() {
		return this.numberOfExecutors;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Topology [topolgoyId=" + topolgoyId + ", name=" + name
				+ ", numberOfTasks=" + numberOfTasks + ", numberOfWorkers="
				+ numberOfWorkers + ", status=" + status + ", uptime=" + uptime
				+ ", spouts=" + spouts + ", bolts=" + bolts
				+ ", rawTopologyData=" + rawTopologyData + "]";
	}

}
