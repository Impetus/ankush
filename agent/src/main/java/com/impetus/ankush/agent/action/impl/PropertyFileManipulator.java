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
import java.io.IOException;
import java.util.Properties;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.action.Manipulatable;
import com.impetus.ankush.agent.sigar.SigarNodeInfoProvider;
import com.impetus.ankush.agent.utils.AgentLogger;

public class PropertyFileManipulator implements Manipulatable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(SigarNodeInfoProvider.class);

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
				System.err.println("File does not exist.");
				status = false;
			}
			AgentConf agentConf = new AgentConf();
			Properties configuration = agentConf.load(file);
			configuration.put(propertyName, newPropertyValue);
			agentConf.save(configuration, file);
			status = true;
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
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
				System.err.println("File does not exist.");
				status = false;
			}
			AgentConf agentConf = new AgentConf();
			Properties configuration = agentConf.load(file);
			if (configuration.remove(propertyName) != null) {
				configuration.remove(propertyName);
				agentConf.save(configuration, file);
				status = true;
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
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
				System.err.println("File does not exist.");
				return confValue;
			}
			AgentConf agentConf = new AgentConf();
			Properties configuration = agentConf.load(file);
			confValue = configuration.getProperty(propertyName);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return confValue;
	}

}
