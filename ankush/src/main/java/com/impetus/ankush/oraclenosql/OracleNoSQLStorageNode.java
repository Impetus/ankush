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
package com.impetus.ankush.oraclenosql;

import java.io.Serializable;

/**
 * Storage node configuration.
 * 
 * @author nikunj
 */
public class OracleNoSQLStorageNode implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The storage node id. */
	private int snId;

	/** The registry port. */
	private int registryPort;

	/** The admin port. */
	private int adminPort;

	/** The hostname. */
	private String hostname;

	/** The active. */
	private int active;

	/** The capacity. */
	private int capacity;

	/** The replication node count. */
	private int rnCount;
	
	/** The admin id. */
	private int adminId;

	/**
	 * Gets the sn id.
	 * 
	 * @return the snId
	 */
	public int getSnId() {
		return snId;
	}

	/**
	 * Sets the sn id.
	 * 
	 * @param snId
	 *            the snId to set
	 */
	public void setSnId(int snId) {
		this.snId = snId;
	}

	/**
	 * Gets the registry port.
	 * 
	 * @return the registryPort
	 */
	public int getRegistryPort() {
		return registryPort;
	}

	/**
	 * Sets the registry port.
	 * 
	 * @param registryPort
	 *            the registryPort to set
	 */
	public void setRegistryPort(int registryPort) {
		this.registryPort = registryPort;
	}

	/**
	 * Gets the admin port.
	 * 
	 * @return the adminPort
	 */
	public int getAdminPort() {
		return adminPort;
	}

	/**
	 * Sets the admin port.
	 * 
	 * @param adminPort
	 *            the adminPort to set
	 */
	public void setAdminPort(int adminPort) {
		this.adminPort = adminPort;
	}

	/**
	 * Gets the hostname.
	 * 
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets the hostname.
	 * 
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Gets the active.
	 * 
	 * @return the active
	 */
	public int getActive() {
		return active;
	}

	/**
	 * Sets the active.
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(int active) {
		this.active = active;
	}

	/**
	 * Gets the capacity.
	 * 
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Sets the capacity.
	 * 
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Gets the rn count.
	 * 
	 * @return the rnCount
	 */
	public int getRnCount() {
		return rnCount;
	}

	/**
	 * Sets the rn count.
	 * 
	 * @param rnCount
	 *            the rnCount to set
	 */
	public void setRnCount(int rnCount) {
		this.rnCount = rnCount;
	}

	/**
	 * Gets the admin id.
	 *
	 * @return the adminId
	 */
	public int getAdminId() {
		return adminId;
	}

	/**
	 * Sets the admin id.
	 *
	 * @param adminId the adminId to set
	 */
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

}
