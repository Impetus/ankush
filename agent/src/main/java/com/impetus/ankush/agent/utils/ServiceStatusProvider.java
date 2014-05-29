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
import java.util.List;
import java.util.Map;

/**
 * The Class ServiceStatusProvider.
 * 
 * @author hokam
 */
public class ServiceStatusProvider {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(ServiceStatusProvider.class);

	/**
	 * Method to get Process / Port service status.
	 * 
	 * @param services
	 *            the services
	 * @return the Port service status
	 */
	public Map<String, Boolean> getProcessPortStatus(List<String> processPortMap) {
		// key value hash map.
		Map<String, Boolean> processStatus = new HashMap<String, Boolean>();

		// iterating over the services.
		for (String processPortInfo : processPortMap) {

			String[] tmp = processPortInfo.split(":");
			if (tmp.length != 2) {
				LOGGER.error("ERROR: Invalid Process Port Info - "
						+ processPortInfo);
				continue;
			}

			String processName = tmp[0];
			String port = tmp[1];

			Result rs = new Result();
			try {
				// getting the service status using command.
				rs = CommandExecutor.executeCommand("fuser -n tcp " + port);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				processStatus.put(processName, false);
			}
			// initially making service status false.
			processStatus.put(processName, false);
			if (rs.getExitVal() == 0) {
				// making service status true.
				processStatus.put(processName, true);
			}
			LOGGER.info(processName + ":" + port + " status - " + processStatus);
		}
		// returning service status.
		return processStatus;
	}

	/**
	 * Method to get ganglia service status.
	 * 
	 * @param services
	 *            the services
	 * @return the ganglia service status
	 */
	public Map<String, Boolean> getGangliaServiceStatus(List<String> services) {
		// key value hash map.
		Map<String, Boolean> serviceStatus = new HashMap<String, Boolean>();

		// iterating over the services.
		for (String process : services) {
			Result rs = new Result();
			try {
				// getting the service status using command.
				rs = CommandExecutor.executeCommand("pgrep -l " + process);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			// initially making service status false.
			serviceStatus.put(process.toString(), false);
			if (rs.getExitVal() == 0) {
				// making service status true.
				serviceStatus.put(process.toString(), true);
			}
			LOGGER.info("Ganglia Service Status " + serviceStatus);
		}
		// returning service status.
		return serviceStatus;
	}
}
