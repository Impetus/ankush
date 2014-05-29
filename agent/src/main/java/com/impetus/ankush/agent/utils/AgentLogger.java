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
package com.impetus.ankush.agent.utils;

import org.apache.log4j.Logger;

/**
 * 
 * @author hokam
 * 
 */
public class AgentLogger {
	// log4j logger object
	private org.apache.log4j.Logger log;

	/**
	 * Constructor.
	 * 
	 * @param classObj
	 */
	public AgentLogger(Class classObj) {
		log = Logger.getLogger(classObj);
	}

	/**
	 * Method to log informative messages.
	 * 
	 * @param message
	 */
	public void info(String message) {
		log.info(message);
	}

	/**
	 * Method to log error messages.
	 * 
	 * @param message
	 */
	public void error(String message) {
		log.error(message);
	}

	/**
	 * Method to log error messages with generate exceptions.
	 * 
	 * @param message
	 * @param e
	 */
	public void error(String message, Exception e) {
		log.error(message, e);
	}

	/**
	 * Method to log debug messages.
	 * 
	 * @param message
	 */
	public void debug(String message) {
		log.debug(message);
	}
}
