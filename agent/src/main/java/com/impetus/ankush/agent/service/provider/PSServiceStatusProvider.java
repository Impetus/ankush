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
import java.util.List;
import java.util.Map;

import com.impetus.ankush.agent.service.ComponentService;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.CommandExecutor;
import com.impetus.ankush.agent.utils.Result;

/**
 * @author hokam
 * 
 */
public class PSServiceStatusProvider extends ServiceProvider {

	/** The log. */
	private static final AgentLogger LOGGER = new AgentLogger(
			PSServiceStatusProvider.class);

	/**
	 * Method to get service status.
	 * 
	 * @param services
	 *            the services
	 * @return the service status
	 */
	public Map<String, Boolean> getServiceStatus(List<ComponentService> services) {
		// key value hash map.
		Map<String, Boolean> serviceStatus = new HashMap<String, Boolean>();

		// iterating over the services.
		for (ComponentService service : services) {
			Result rs = new Result();
			try {
				// getting the service status using command.
				rs = CommandExecutor.executeCommand("pgrep -l "
						+ service.getName());
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
			// initially making service status false.
			serviceStatus.put(service.getRole(), false);
			if (rs.getExitVal() == 0) {
				// making service status true.
				serviceStatus.put(service.getRole(), true);
			}
			LOGGER.info("Service Status " + serviceStatus);
		}
		// returning service status.
		return serviceStatus;
	}
}
