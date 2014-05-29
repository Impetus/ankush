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
package com.impetus.ankush.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class GangliaUtils.
 */
public class GangliaUtils {
	
	/**
	 * Gets the ganglia host names.
	 *
	 * @param hostname the hostname
	 * @param username the username
	 * @param password the password
	 * @param privateKey the private key
	 * @return the ganglia host names
	 */
	public static Map<String, String> getGangliaHostNames(String hostname,
			String username, String password, String privateKey) {
		Map<String, String> ipHostName = new HashMap<String, String>();
		String ipArray[] = null;
		String hostNameArray[] = null;

		/* Fetch list of IP addresses */
		SSHConnection connection = new SSHConnection(hostname, username,
				password, privateKey);

		if (!connection.exec("gstat -1lan | awk '{ print $1}'")) {
			return null;
		}
		ipArray = connection.getOutput().split("\n");

		/* Fetch list of hostnames */
		connection = new SSHConnection(hostname, username, password, privateKey);

		if (!connection.exec("gstat -1la | awk '{ print $1}'")) {
			return null;
		}
		hostNameArray = connection.getOutput().split("\n");

		// polulate values and fill map
		for (int i = ipArray.length - 1; i >= 0; i--) {
			ipHostName.put(ipArray[i], hostNameArray[i]);
		}
		return ipHostName;
	}
}
