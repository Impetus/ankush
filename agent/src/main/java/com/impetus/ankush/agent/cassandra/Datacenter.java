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
package com.impetus.ankush.agent.cassandra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Datacenter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String datacenterName = new String();

	private List<Rack> racks = new ArrayList<Rack>();

	/**
	 * @return the datacenterName
	 */
	public String getDatacenterName() {
		return datacenterName;
	}

	/**
	 * @param datacenterName the datacenterName to set
	 */
	public void setDatacenterName(String datacenterName) {
		this.datacenterName = datacenterName;
	}

	/**
	 * @return the racks
	 */
	public List<Rack> getRacks() {
		return racks;
	}

	/**
	 * @param racks the racks to set
	 */
	public void setRacks(List<Rack> racks) {
		this.racks = racks;
	}
	
}
