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

/**
 * @author mayur
 * 
 */
public class Bolt extends BaseComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private double executeLatency;

	private double processLatency;
	
	private double executed;
	
	private double capacity;

	/**
	 * @return the executed
	 */
	public double getExecuted() {
		return executed;
	}

	/**
	 * @param executed the executed to set
	 */
	public void setExecuted(double executed) {
		this.executed = executed;
	}

	/**
	 * @return the capacity
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the executeLatency
	 */
	public double getExecuteLatency() {
		return executeLatency;
	}

	/**
	 * @param executeLatency
	 *            the executeLatency to set
	 */
	public void setExecuteLatency(double executeLatency) {
		this.executeLatency = executeLatency;
	}

	/**
	 * @return the processLatency
	 */
	public double getProcessLatency() {
		return processLatency;
	}

	/**
	 * @param processLatency
	 *            the processLatency to set
	 */
	public void setProcessLatency(double processLatency) {
		this.processLatency = processLatency;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Bolt [executeLatency=" + executeLatency + ", processLatency="
				+ processLatency + ", executed=" + executed + ", capacity="
				+ capacity + "]";
	}
}
