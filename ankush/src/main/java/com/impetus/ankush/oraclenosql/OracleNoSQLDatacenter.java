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
package com.impetus.ankush.oraclenosql;

import java.util.List;

import com.impetus.ankush.common.framework.config.Configuration;

/**
 * The Class OracleNoSQLDatacenter.
 * 
 * @author nikunj
 */
public class OracleNoSQLDatacenter implements Configuration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name. */
	private String name;

	/** The rep factor. */
	private int repFactor;

	/** The datacenter id. */
	private int datacenterId;

	/** The storage node list. */
	private List<OracleNoSQLStorageNode> storageNodeList;

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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the rep factor.
	 * 
	 * @return the repFactor
	 */
	public int getRepFactor() {
		return repFactor;
	}

	/**
	 * Sets the rep factor.
	 * 
	 * @param repFactor
	 *            the repFactor to set
	 */
	public void setRepFactor(int repFactor) {
		this.repFactor = repFactor;
	}

	/**
	 * Gets the datacenter id.
	 * 
	 * @return the datacenterId
	 */
	public int getDatacenterId() {
		return datacenterId;
	}

	/**
	 * Sets the datacenter id.
	 * 
	 * @param datacenterId
	 *            the datacenterId to set
	 */
	public void setDatacenterId(int datacenterId) {
		this.datacenterId = datacenterId;
	}

	/**
	 * Gets the storage node list.
	 * 
	 * @return the storageNodeList
	 */
	public List<OracleNoSQLStorageNode> getStorageNodeList() {
		return storageNodeList;
	}

	/**
	 * Sets the storage node list.
	 * 
	 * @param storageNodeList
	 *            the storageNodeList to set
	 */
	public void setStorageNodeList(List<OracleNoSQLStorageNode> storageNodeList) {
		this.storageNodeList = storageNodeList;
	}

}
