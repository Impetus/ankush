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
package com.impetus.ankush.hadoop.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.impetus.ankush.common.controller.rest.BaseController;
import com.impetus.ankush.common.exception.ControllerException;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush.hadoop.service.ClusterCommandExecutionService;

/**
 * The Class ClusterCommandsController.
 */
@Controller
@RequestMapping("/cluster")
public class ClusterCommandsController extends BaseController {
	
	/** The command execution service. */
	private ClusterCommandExecutionService commandExecutionService;

	/**
	 * Sets the command execution service.
	 *
	 * @param commandExecutionService the new command execution service
	 */
	@Autowired
	public void setCommandExecutionService(
			ClusterCommandExecutionService commandExecutionService) {
		this.commandExecutionService = commandExecutionService;
	}

	/** The command map. */
	private static Map<String, Map<String, List<String>>> commandMap = new HashMap<String, Map<String, List<String>>>();
	static {
		// Hadoop commands
		HashMap<String, List<String>> hadoopCommands = new HashMap<String, List<String>>();
		hadoopCommands.put("archive", Arrays.asList("archiveName",
				"parentDirectory", "sourceDirectory", "destinationDirectory"));
		hadoopCommands.put("distcp",
				Arrays.asList("sourceURL", "destinatinURL", "options"));
		hadoopCommands.put("balancer", Arrays.asList("threshold"));
		hadoopCommands.put("fsck",
				Arrays.asList("path", "genericOptons", "otherOptions"));
		commandMap.put("hadoop", hadoopCommands);

	}

	/**
	 * Execute handoop command.
	 *
	 * @param clusterId the cluster id
	 * @param command the command
	 * @param parameters the parameters
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{clusterId}/commands/hadoop/{command}")
	public ResponseEntity<ResponseWrapper<Map>> executeHandoopCommand(
			@PathVariable("clusterId") Long clusterId,
			@PathVariable("command") String command,
			@RequestBody Map<String, String> parameters) {

		return wrapResponse(
				executeCommand(clusterId, "hadoop", command, parameters),
				HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.toString(),
				"Command accepted for execution");
	}

	/**
	 * Execute command.
	 *
	 * @param clusterId the cluster id
	 * @param category the category
	 * @param command the command
	 * @param commandParams the command params
	 * @return the map
	 */
	private Map executeCommand(Long clusterId, String category, String command,
			Map<String, String> commandParams) {
		if (!commandMap.get(category).containsKey(command)) {
			throw new ControllerException(HttpStatus.NOT_FOUND, "ER001",
					category + ":'" + command + "' command is not supported");
		}
		// retain only required parameters
		List<String> requiredParams = commandMap.get(category).get(command);
		commandParams.keySet().retainAll(requiredParams);
		if (commandParams.keySet().size() != requiredParams.size()) {
			throw new ControllerException(HttpStatus.BAD_REQUEST, "ER001",
					"Not all parameters sent. Required parameters: "
							+ requiredParams);
		}
		if (category.equals("hadoop")) {
			return commandExecutionService.executeHadoopCommand(clusterId,
					command, commandParams);
		}
		return null;
	}

}
