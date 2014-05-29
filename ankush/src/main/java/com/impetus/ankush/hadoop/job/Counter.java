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
package com.impetus.ankush.hadoop.job;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author hokam
 */
public class Counter  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<SubCounter> subCounters = new ArrayList<SubCounter>();

	/**
	 * Method getName.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method setName.
	 * 
	 * @param name
	 *            String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Method getSubCounters.
	 * 
	 * @return List<JobCounter>
	 */
	public ArrayList<SubCounter> getSubCounters() {
		return subCounters;
	}

	/**
	 * Method setSubCounters.
	 * 
	 * @param jobCounters
	 *            List<JobCounter>
	 */
	public void setSubCounters(ArrayList<SubCounter> subCounters) {
		this.subCounters = subCounters;
	}
}
