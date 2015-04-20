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
package com.impetus.ankush.common.controller.rest;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush2.db.DBHAServiceManager;
import com.impetus.ankush2.framework.manager.ServiceManager;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * Manage deployed cluster.
 * 
 * @author nikunj
 * 
 */

@Controller
@RequestMapping("/service")
public class ServiceController extends BaseController {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			ServiceController.class);

	/** The Constant CLUSTER_SERVICE. */
	private static final String CLUSTER_SERVICE = "Cluster service";

	/** The Constant COMPONENT_SERVICE. */
	private static final String COMPONENT_SERVICE = "Component service";

	/** The Constant CLUSTER_SERVICE. */
	private static final String NODE_SERVICES = "Node services";

	/** The Constant CLUSTER_ID. */
	private static final String CLUSTER_ID = "clusterId";

	/** The Constant NODE */
	private static final String NODE = "node";

	/** The Constant COMPONENT */
	private static final String COMPONENT = "component";

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/start/{node:.+}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> start(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(NODE) String node,
			@RequestBody Map<String, Set<String>> services, Principal principal) {
		Map map = null;
		try {
			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			serviceManager.validateNode(node);
			map = serviceManager.startServices(node, services);
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("status", false);
			map.put("error", new HashSet<String>(Arrays.asList(e.getMessage())));
			map.put("message", "Operation submission failed.");
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				NODE_SERVICES);

	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/stop/{node:.+}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> stop(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(NODE) String node,
			@RequestBody Map<String, Set<String>> services, Principal principal) {
		Map map = null;
		try {
			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			serviceManager.validateNode(node);
			map = serviceManager.stopServices(node, services);
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("status", false);
			map.put("error", new HashSet<String>(Arrays.asList(e.getMessage())));
			map.put("message", "Operation submission failed.");
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				NODE_SERVICES);

	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/{node:.+}/start")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> startNode(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(NODE) String node, Principal principal) {
		Map map = null;
		try {
			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			serviceManager.validateNode(node);
			map = serviceManager.startNode(node);
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("status", false);
			map.put("error", new HashSet<String>(Arrays.asList(e.getMessage())));
			map.put("message", "Operation submission failed.");
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				NODE_SERVICES);

	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/{node:.+}/stop")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> stopNode(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(NODE) String node, Principal principal) {
		Map map = null;
		try {
			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			serviceManager.validateNode(node);
			map = serviceManager.stopNode(node);
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("status", false);
			map.put("error", new HashSet<String>(Arrays.asList(e.getMessage())));
			map.put("message", "Operation submission failed.");
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				NODE_SERVICES);

	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/component/{component}/start")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> startComponent(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(COMPONENT) String component,
			@RequestParam(value = "role", required = false) String role,
			Principal principal) {
		Map map = null;
		try {
			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			serviceManager.validateComponent(component);
			map = serviceManager.startComponent(component, role);
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("status", false);
			map.put("error", new HashSet<String>(Arrays.asList(e.getMessage())));
			map.put("message", "Operation submission failed.");
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				COMPONENT_SERVICE);
	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/component/{component}/stop")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> stopComponent(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(COMPONENT) String component,
			@RequestParam(value = "role", required = false) String role,
			Principal principal) {
		Map map = null;
		try {
			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			serviceManager.validateComponent(component);
			map = serviceManager.stopComponent(component, role);
		} catch (Exception e) {
			map = new HashMap<String, Object>();
			map.put("status", false);
			map.put("error", new HashSet<String>(Arrays.asList(e.getMessage())));
			map.put("message", "Operation submission failed.");
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				COMPONENT_SERVICE);
	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/start")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> startCluster(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@RequestBody Map<String, Object> parameterMap, Principal principal) {
		Map map = null;
		try {
			String userName = principal.getName();
			if (parameterMap.get("password") == null
					|| ((String) parameterMap.get("password")).isEmpty()) {
				throw new AnkushException(
						"Please provide a valid password to delete this cluster.");
			}
			if (!userManager.doesPasswordMatch(userName,
					parameterMap.get("password").toString())) {
				throw new AnkushException(
						"Given password doesn't match.You are not authorized to delete cluster.");
			}
			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			map = serviceManager.startCluster();

		} catch (Exception e) {
			map = new HashMap<String, Object>(returnErrorMap(e.getMessage()));
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				CLUSTER_SERVICE);
	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/stop")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> stopCluster(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@RequestBody Map<String, Object> parameterMap, Principal principal) {
		Map map = null;
		try {
			String userName = principal.getName();
			if (parameterMap.get("password") == null
					|| ((String) parameterMap.get("password")).isEmpty()) {
				throw new AnkushException(
						"Please provide a valid password to delete this cluster.");
			}
			if (!userManager.doesPasswordMatch(userName,
					parameterMap.get("password").toString())) {
				throw new AnkushException(
						"Given password doesn't match.You are not authorized to delete cluster.");
			}

			// Create service manager
			ServiceManager serviceManager = new ServiceManager(clusterId,
					principal.getName());
			map = serviceManager.stopCluster();
		} catch (Exception e) {
			map = new HashMap<String, Object>(returnErrorMap(e.getMessage()));
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				CLUSTER_SERVICE);
	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/haservices")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> manageServices(
			@PathVariable(CLUSTER_ID) Long clusterId, @RequestBody List services) {
		Map map = null;
		try {
			map = new DBHAServiceManager().updateServices(clusterId, services);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				CLUSTER_SERVICE);
	}

	private Map<String, Object> returnErrorMap(String errMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", false);
		map.put("error", new HashSet<String>(Arrays.asList(errMsg)));
		map.put("message", "Operation submission failed.");
		return map;
	}
}
