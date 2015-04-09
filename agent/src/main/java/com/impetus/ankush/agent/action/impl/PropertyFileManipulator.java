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
package com.impetus.ankush.agent.action.impl;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.impetus.ankush.agent.action.Manipulatable;

public class PropertyFileManipulator implements Manipulatable {

	/**
	 * Edits the conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @param newPropertyValue
	 *            the new property value
	 * @return true, if successful
	 */
	@Override
	public boolean editConfValue(String file, String propertyName,
			String newPropertyValue) {
		boolean status = false;
		try {
			// read conf file
			File confFile = new File(file);

			if (!confFile.exists()) {
				System.err.println("File " + file + " does not exists.");
				status = false;
			}
			PropertiesConfiguration props = new PropertiesConfiguration(file);
			props.setProperty(propertyName, newPropertyValue);
			props.getLayout().setSeparator(propertyName, "=");
			props.save();
			status = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return status;
	}

	/**
	 * Delete conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @return true, if successful
	 */
	@Override
	public boolean deleteConfValue(String file, String propertyName) {
		boolean status = false;
		try {
			// read conf file
			File confFile = new File(file);

			if (!confFile.exists()) {
				System.err.println("File " + file + " does not exists.");
				status = false;
			}
			PropertiesConfiguration props = new PropertiesConfiguration(file);
			props.getLayout().setSeparator(propertyName, "=");
			if (props.getProperty(propertyName) != null) {
				props.clearProperty(propertyName);
				props.save();
				status = true;
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return status;

	}

	/**
	 * Write conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 * @return true, if successful
	 */
	@Override
	public boolean writeConfValue(String filePath, String propertyName,
			String propertyValue) {
		return editConfValue(filePath, propertyName, propertyValue);
	}

	/**
	 * Read conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @return the string
	 */
	@Override
	public String readConfValue(String file, String propertyName) {
		String confValue = null;
		try {
			// read conf file
			File confFile = new File(file);

			if (!confFile.exists()) {
				System.err.println("File " + file + " does not exists.");
				return confValue;
			}
			PropertiesConfiguration props = new PropertiesConfiguration(file);
			confValue = props.getProperty(propertyName).toString();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return confValue;
	}

}
