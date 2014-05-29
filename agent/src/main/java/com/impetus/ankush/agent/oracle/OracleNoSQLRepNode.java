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
package com.impetus.ankush.agent.oracle;

import java.io.Serializable;

import oracle.kv.impl.monitor.views.PerfEvent;

/* Represents Topology RepNode */
/**
 * The Class OracleNoSQLRepNode.
 */
public class OracleNoSQLRepNode implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The rg id. */
	private int rgId;

	/** The hostname. */
	private String hostname;

	/** The node num. */
	private int nodeNum;

	/** The sn id. */
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
	 * Instantiates a new oracle no sql rep node.
	 */
	public OracleNoSQLRepNode() {

	}

	/**
	 * Instantiates a new oracle no sql rep node.
	 * 
	 * @param rgId
	 *            the rg id
	 * @param snId
	 *            the sn id
	 * @param hostname
	 *            the hostname
	 * @param nodeNum
	 *            the node num
	 * @param port
	 *            the port
	 * @param master
	 *            the master
	 * @param active
	 *            the active
	 * @param perfEvent
	 *            the perf event
	 */
	public OracleNoSQLRepNode(int rgId, int snId, String hostname, int nodeNum,
			int port, boolean master, int active, PerfEvent perfEvent) {
		this.rgId = rgId;
		this.snId = snId;
		this.hostname = hostname;
		this.nodeNum = nodeNum;
		this.port = port;
		this.master = master;
		this.active = active;
		if (perfEvent != null) {
			this.singleInt = new Latency(perfEvent.getSingleInt());
			this.singleCum = new Latency(perfEvent.getSingleCum());
			this.multiInt = new Latency(perfEvent.getMultiInt());
			this.multiCum = new Latency(perfEvent.getMultiCum());
		} else {
			this.singleInt = new Latency();
			this.singleCum = new Latency();
			this.multiInt = new Latency();
			this.multiCum = new Latency();
		}
	}

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
	 * @return the singleInt
	 */
	public Latency getSingleInt() {
		return singleInt;
	}

	/**
	 * @param singleInt
	 *            the singleInt to set
	 */
	public void setSingleInt(Latency singleInt) {
		this.singleInt = singleInt;
	}

	/**
	 * @return the singleCum
	 */
	public Latency getSingleCum() {
		return singleCum;
	}

	/**
	 * @param singleCum
	 *            the singleCum to set
	 */
	public void setSingleCum(Latency singleCum) {
		this.singleCum = singleCum;
	}

	/**
	 * @return the multiInt
	 */
	public Latency getMultiInt() {
		return multiInt;
	}

	/**
	 * @param multiInt
	 *            the multiInt to set
	 */
	public void setMultiInt(Latency multiInt) {
		this.multiInt = multiInt;
	}

	/**
	 * @return the multiCum
	 */
	public Latency getMultiCum() {
		return multiCum;
	}

	/**
	 * @param multiCum
	 *            the multiCum to set
	 */
	public void setMultiCum(Latency multiCum) {
		this.multiCum = multiCum;
	}

	public long throughput() {
		return singleInt.getThroughput() + multiInt.getThroughput();
	}
}
