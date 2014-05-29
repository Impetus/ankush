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
package com.impetus.ankush.common.framework.config;

import java.io.Serializable;

/**
 * It contains the information about node operating system.
 * 
 * @author hokam.chauhan
 * 
 */
public class NodeOSInfo implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The data model. */
	private String dataModel;
	
	/** The arch. */
	private String arch;
	
	/** The description. */
	private String description;
	
	/** The machine name. */
	private String machineName;
	
	/** The vendor. */
	private String vendor;

	/**
	 * Gets the data model.
	 *
	 * @return the dataModel
	 */
	public String getDataModel() {
		return dataModel;
	}

	/**
	 * Sets the data model.
	 *
	 * @param dataModel the dataModel to set
	 */
	public void setDataModel(String dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * Gets the arch.
	 *
	 * @return the arch
	 */
	public String getArch() {
		return arch;
	}

	/**
	 * Sets the arch.
	 *
	 * @param arch the arch to set
	 */
	public void setArch(String arch) {
		this.arch = arch;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the machine name.
	 *
	 * @return the machineName
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * Sets the machine name.
	 *
	 * @param machineName the machineName to set
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
}
