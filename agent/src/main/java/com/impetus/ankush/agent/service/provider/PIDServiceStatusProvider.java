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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.impetus.ankush.agent.service.ComponentService;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.CommandExecutor;
import com.impetus.ankush.agent.utils.Result;

/**
 * @author hokam
 * 
 */
public class PIDServiceStatusProvider extends ServiceProvider {

	// command for getting pid status.
	private static final String COMMAND_PS_P = "ps -p ";
	// key for getting pid file path.
	private static final String KEY_PID_FILE = "pidFile";
	/** The log. */
	private static final AgentLogger LOGGER = new AgentLogger(
			PIDServiceStatusProvider.class);

	/**
	 * Method to get service status using the process id.
	 */
	@Override
	public Map<String, Boolean> getServiceStatus(List<ComponentService> services) {
		// Service status map
		Map<String, Boolean> status = new HashMap<String, Boolean>();

		// iterate over the services for getting service status.
		for (ComponentService service : services) {
			try {
				// Get pid file path
				String pidFile = service.getParameters().get(KEY_PID_FILE);
				// create file object
				File file = new File(pidFile);
				// check file exists or not
				if (file.exists()) {
					// Get file string

					List<String> lines = FileUtils.readLines(file);
					// if content is empty
					if (lines == null || lines.isEmpty()) {
						status.put(service.getRole(), false);
					} else {
						// if pid exists
						Result result = CommandExecutor
								.executeCommand(COMMAND_PS_P + lines.get(0));
						// setting service status.
						status.put(service.getRole(), result.getExitVal() == 0);
					}
				} else {
					status.put(service.getRole(), false);
				}
			} catch (Exception e) {
				// logging error in agent log
				LOGGER.error(e.getMessage(), e);
				// setting service status as false.
				status.put(service.getRole(), false);
			}
		}
		// return service status.
		return status;
	}

}
