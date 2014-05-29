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
package com.impetus.ankush.agent.action;

/**
 * @author monika
 *
 */
public interface Manipulatable {
	/**
	 * @param file
	 * @param propertyName
	 * @return true if successful
	 */
	public boolean deleteConfValue(String file, String propertyName);

	/**
	 * 
	 * @param file
	 * @param propertyName
	 * @param newPropertyValue
	 * @return true if successful
	 */
	public boolean editConfValue(String file, String propertyName,
			String newPropertyValue);

	/**
	 * 
	 * @param file
	 * @param propertyName
	 * @return value of given property
	 */
	public String readConfValue(String file, String propertyName);

	/**
	 * 
	 * @param file
	 * @param propertyName
	 * @param propertyValue
	 * @return true if successful
	 */
	public boolean writeConfValue(String file, String propertyName,
			String propertyValue);
}
