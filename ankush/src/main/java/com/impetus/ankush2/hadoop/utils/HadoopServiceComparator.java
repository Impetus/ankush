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
package com.impetus.ankush2.hadoop.utils;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopServiceComparator.
 *
 * @author Akhil
 */
public class HadoopServiceComparator implements Comparator<String> {

	/** The desc. */
	private boolean desc;
	
	/**
	 * Instantiates a new hadoop service comparator.
	 *
	 * @param desc the desc
	 */
	public HadoopServiceComparator(boolean desc) {
		this.desc = desc;
	}

	/**
	 * Instantiates a new hadoop service comparator.
	 */
	public HadoopServiceComparator() {
		this.desc = false;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String serviceOne, String serviceTwo) {

		if (serviceOne == null || serviceTwo == null) {
			return 0;
		}

		int serviceOnePriority = HadoopUtils.RolePriorityMap.getRolePriority(serviceOne);
		int serviceTwoPriority = HadoopUtils.RolePriorityMap.getRolePriority(serviceTwo);
		
		if (serviceOnePriority == serviceTwoPriority) {
			return 0;
		} else if (serviceOnePriority > serviceTwoPriority) {
			if (desc) {
				return -1;
			}
			return 1;
		} else if (serviceOnePriority < serviceTwoPriority) {
			if (desc) {
				return 1;
			}
			return -1;
		} else {
			return 0;
		}
	}

}
