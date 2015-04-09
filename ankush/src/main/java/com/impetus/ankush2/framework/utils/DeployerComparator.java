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
package com.impetus.ankush2.framework.utils;

import java.util.Comparator;

import com.impetus.ankush2.framework.Deployable;

/**
 * The Class ComponentComparator.
 * 
 * @author mayur
 */
public class DeployerComparator implements Comparator<Deployable> {

	/** The desc. */
	private boolean desc;

	/**
	 * Instantiates a new component comparator.
	 * 
	 * @param desc
	 *            the desc
	 */
	public DeployerComparator(boolean desc) {
		this.desc = desc;
	}

	/**
	 * Instantiates a new component comparator.
	 */
	public DeployerComparator() {
		this.desc = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Deployable objectOne, Deployable objectTwo) {
		if (objectOne == null || objectTwo == null) {
			return 0;
		}

		int objectOnePriority = ObjectFactory.getComponentPriority(objectOne
				.getComponentName());
		int objectTwoPriority = ObjectFactory.getComponentPriority(objectTwo
				.getComponentName());

		if (objectOnePriority == objectTwoPriority) {
			return 0;
		} else if (objectOnePriority > objectTwoPriority) {
			if (desc) {
				return -1;
			}
			return 1;
		} else if (objectOnePriority < objectTwoPriority) {
			if (desc) {
				return 1;
			}
			return -1;
		} else {
			return 0;
		}
	}

}
