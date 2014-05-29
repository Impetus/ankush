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

/**
 * 
 * @author mayur
 *
 */
public class Supervisor {

	private String host;
	
	private int numberOfWorkers;
	
	private int uptime;

	private int numberOfUsedWorkers;
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the numberOfWorkers
	 */
	public int getNumberOfWorkers() {
		return numberOfWorkers;
	}

	/**
	 * @param numberOfWorkers the numberOfWorkers to set
	 */
	public void setNumberOfWorkers(int numberOfWorkers) {
		this.numberOfWorkers = numberOfWorkers;
	}

	/**
	 * @return the uptime
	 */
	public int getUptime() {
		return uptime;
	}

	/**
	 * @param uptime the uptime to set
	 */
	public void setUptime(int uptime) {
		this.uptime = uptime;
	}

	/**
	 * @return the numberOfUsedWorkers
	 */
	public int getNumberOfUsedWorkers() {
		return numberOfUsedWorkers;
	}

	/**
	 * @param numberOfUsedWorkers the numberOfUsedWorkers to set
	 */
	public void setNumberOfUsedWorkers(int numberOfUsedWorkers) {
		this.numberOfUsedWorkers = numberOfUsedWorkers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Supervisor [host=" + host + ", numberOfWorkers="
				+ numberOfWorkers + ", uptime=" + uptime
				+ ", numberOfUsedWorkers=" + numberOfUsedWorkers + "]";
	}

}
