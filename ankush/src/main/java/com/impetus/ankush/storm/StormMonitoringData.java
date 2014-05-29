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
package com.impetus.ankush.storm;

import java.util.List;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringListener;

/**
 * @author mayur
 * 
 */
public class StormMonitoringData implements TechnologyData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nimbusConf;

	private int nimbusUpTime;

	private int supervisorSize;

	private int topologySize;

	private List<Supervisor> supervisors;

	private List<Topology> topologies;
	
	private int usedSlots = 0;
	
	private int freeSlots = 0;

	private int totalSlots = 0;

	private int executors = 0;

	private int tasks = 0;

	/**
	 * @return the usedSlots
	 */
	public int getUsedSlots() {
		return usedSlots;
	}

	/**
	 * @param usedSlots the usedSlots to set
	 */
	public void setUsedSlots(int usedSlots) {
		this.usedSlots = usedSlots;
	}

	/**
	 * @return the freeSlots
	 */
	public int getFreeSlots() {
		return freeSlots;
	}

	/**
	 * @param freeSlots the freeSlots to set
	 */
	public void setFreeSlots(int freeSlots) {
		this.freeSlots = freeSlots;
	}

	/**
	 * @return the totalSlots
	 */
	public int getTotalSlots() {
		return totalSlots;
	}

	/**
	 * @param totalSlots the totalSlots to set
	 */
	public void setTotalSlots(int totalSlots) {
		this.totalSlots = totalSlots;
	}

	/**
	 * @return the executors
	 */
	public int getExecutors() {
		return executors;
	}

	/**
	 * @param executors the executors to set
	 */
	public void setExecutors(int executors) {
		this.executors = executors;
	}

	/**
	 * @return the tasks
	 */
	public int getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(int tasks) {
		this.tasks = tasks;
	}

	/**
	 * @return the nimbusConf
	 */
	public String getNimbusConf() {
		return nimbusConf;
	}

	/**
	 * @param nimbusConf
	 *            the nimbusConf to set
	 */
	public void setNimbusConf(String nimbusConf) {
		this.nimbusConf = nimbusConf;
	}

	/**
	 * @return the nimbusUpTime
	 */
	public int getNimbusUpTime() {
		return nimbusUpTime;
	}

	/**
	 * @param nimbusUpTime
	 *            the nimbusUpTime to set
	 */
	public void setNimbusUpTime(int nimbusUpTime) {
		this.nimbusUpTime = nimbusUpTime;
	}

	/**
	 * @return the supervisorSize
	 */
	public int getSupervisorSize() {
		return supervisorSize;
	}

	/**
	 * @param supervisorSize
	 *            the supervisorSize to set
	 */
	public void setSupervisorSize(int supervisorSize) {
		this.supervisorSize = supervisorSize;
	}

	/**
	 * @return the topologySize
	 */
	public int getTopologySize() {
		return topologySize;
	}

	/**
	 * @param topologySize
	 *            the topologySize to set
	 */
	public void setTopologySize(int topologySize) {
		this.topologySize = topologySize;
	}

	/**
	 * @return the supervisors
	 */
	public List<Supervisor> getSupervisors() {
		return supervisors;
	}

	/**
	 * @param supervisors
	 *            the supervisors to set
	 */
	public void setSupervisors(List<Supervisor> supervisors) {
		this.supervisors = supervisors;
	}

	/**
	 * @return the topologies
	 */
	public List<Topology> getTopologies() {
		return topologies;
	}

	/**
	 * @param topologies
	 *            the topologies to set
	 */
	public void setTopologies(List<Topology> topologies) {
		this.topologies = topologies;
	}

	@Override
	public String getTechnologyName() {
		return Constant.Technology.STORM;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StormMonitoringData [nimbusConf=");
		builder.append(nimbusConf);
		builder.append(", nimbusUpTime=");
		builder.append(nimbusUpTime);
		builder.append(", supervisorSize=");
		builder.append(supervisorSize);
		builder.append(", topologySize=");
		builder.append(topologySize);
		builder.append(", supervisors=");
		builder.append(supervisors);
		builder.append(", topologies=");
		builder.append(topologies);
		builder.append(", usedSlots=");
		builder.append(usedSlots);
		builder.append(", freeSlots=");
		builder.append(freeSlots);
		builder.append(", totalSlots=");
		builder.append(totalSlots);
		builder.append(", executors=");
		builder.append(executors);
		builder.append(", tasks=");
		builder.append(tasks);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public MonitoringListener getMonitoringListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
