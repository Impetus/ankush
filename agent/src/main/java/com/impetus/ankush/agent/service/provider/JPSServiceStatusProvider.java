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
package com.impetus.ankush.agent.service.provider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.agent.service.ComponentService;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.CommandExecutor;
import com.impetus.ankush.agent.utils.Result;

/**
 * @author hokam
 * 
 */
public class JPSServiceStatusProvider extends ServiceProvider {

	private static final String LINE_SEPERATOR = "\n";
	private static final String COMMAND_JPS = "jps";
	/** The log. */
	private static final AgentLogger LOGGER = new AgentLogger(
			JPSServiceStatusProvider.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.agent.service.ServiceProvider#getServiceStatus(java
	 * .util.List)
	 */
	@Override
	public Map<String, Boolean> getServiceStatus(List<ComponentService> services) {
		// making result object
		Result rs = new Result();
		try {
			// executing JPS command.
			rs = CommandExecutor.executeCommand(COMMAND_JPS);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		// splitting output using \n
		String[] outlines = rs.getOutput().split(LINE_SEPERATOR);

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
		// Service Status
		Map<String, Boolean> serviceStatus = new HashMap<String, Boolean>();
		// iterating over the services.
		for (ComponentService service : services) {
			// putting service status.
			serviceStatus.put(
					service.getRole(),
					processes.contains(service.getName().toString()
							.toLowerCase()));
		}
		LOGGER.info("Service Status " + serviceStatus);
		// returning service status.
		return serviceStatus;
	}
}
