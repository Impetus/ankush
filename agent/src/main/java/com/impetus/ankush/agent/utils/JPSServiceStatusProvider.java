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
package com.impetus.ankush.agent.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Class JPSServiceStatusProvider.
 * 
 * @author Hokam Chauhan
 */
public class JPSServiceStatusProvider extends ServiceStatusProvider {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(JPSServiceStatusProvider.class);
	/**
	 * Method getServiceStatus.
	 * 
	 * @param services
	 *            List<String>
	 * @return Map<Object,Object> The Map for The Given Services with Status
	 *         Value.
	 */
	public Map<String, Boolean> getServiceStatus(List<String> services) {
		// making result object
		Result rs = new Result();
		try {
			// executing jps command.
			rs = CommandExecutor.executeCommand("jps");
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		// spliting ouput using \n
		String[] outlines = rs.getOutput().split("\n");

		// making hash set of processes.
		Set<String> processes = new HashSet<String>();
		// iterating over the ouput lines.
		for (String line : outlines) {
			String pIdName[] = line.split(" ");
			if (pIdName.length == 2) {
				// added service to list.
				processes.add(pIdName[1].toLowerCase());
			}
		}

		Map<String, Boolean> serviceStatus = new HashMap<String, Boolean>();
		// iterating over the services.
		for (String process : services) {
			// putting service status.
			serviceStatus.put(process,
					processes.contains(process.toString().toLowerCase()));
		}
		LOGGER.info("Service Status " + serviceStatus);
		// returning service status.
		return serviceStatus;
	}
}
