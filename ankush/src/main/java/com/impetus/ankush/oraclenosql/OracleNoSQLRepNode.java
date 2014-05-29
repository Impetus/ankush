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
 * Replication node configuration.
 * 
 * @author nikunj
 */
public class OracleNoSQLRepNode implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The replication group id. */
	private int rgId;

	/** The hostname. */
	private String hostname;

	/** The replication node num. */
	private int nodeNum;

	/** The storage node id. */
	private int snId;

	/** The port. */
	private int port;

	/** The active. */
	private int active;

	/** The master. */
	private boolean master;

	/** The single int. */
	private Latency singleInt;

	/** The single cum. */
	private Latency singleCum;

	/** The multi int. */
	private Latency multiInt;

	/** The multi cum. */
	private Latency multiCum;

	/**
	 * Gets the rg id.
	 * 
	 * @return the rgId
	 */
	public int getRgId() {
		return rgId;
	}

	/**
	 * Sets the rg id.
	 * 
	 * @param rgId
	 *            the rgId to set
	 */
	public void setRgId(int rgId) {
		this.rgId = rgId;
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
	 * Gets the node num.
	 * 
	 * @return the nodeNum
	 */
	public int getNodeNum() {
		return nodeNum;
	}

	/**
	 * Sets the node num.
	 * 
	 * @param nodeNum
	 *            the nodeNum to set
	 */
	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}

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
	 * Gets the port.
	 * 
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 * 
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
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
	 * Checks if is master.
	 * 
	 * @return the master
	 */
	public boolean isMaster() {
		return master;
	}

	/**
	 * Sets the master.
	 * 
	 * @param master
	 *            the master to set
	 */
	public void setMaster(boolean master) {
		this.master = master;
	}

	/**
	 * Gets the single int.
	 *
	 * @return the singleInt
	 */
	public Latency getSingleInt() {
		return singleInt;
	}

	/**
	 * Sets the single int.
	 *
	 * @param singleInt the singleInt to set
	 */
	public void setSingleInt(Latency singleInt) {
		this.singleInt = singleInt;
	}

	/**
	 * Gets the single cum.
	 *
	 * @return the singleCum
	 */
	public Latency getSingleCum() {
		return singleCum;
	}

	/**
	 * Sets the single cum.
	 *
	 * @param singleCum the singleCum to set
	 */
	public void setSingleCum(Latency singleCum) {
		this.singleCum = singleCum;
	}

	/**
	 * Gets the multi int.
	 *
	 * @return the multiInt
	 */
	public Latency getMultiInt() {
		return multiInt;
	}

	/**
	 * Sets the multi int.
	 *
	 * @param multiInt the multiInt to set
	 */
	public void setMultiInt(Latency multiInt) {
		this.multiInt = multiInt;
	}

	/**
	 * Gets the multi cum.
	 *
	 * @return the multiCum
	 */
	public Latency getMultiCum() {
		return multiCum;
	}

	/**
	 * Sets the multi cum.
	 *
	 * @param multiCum the multiCum to set
	 */
	public void setMultiCum(Latency multiCum) {
		this.multiCum = multiCum;
	}
}
