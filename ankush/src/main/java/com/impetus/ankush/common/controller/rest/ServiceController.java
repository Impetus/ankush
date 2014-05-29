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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impetus.ankush.common.framework.ServiceManager;
import com.impetus.ankush.common.framework.config.ServiceConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush.oraclenosql.OracleNoSQLService;

/**
 * Manage deployed cluster.
 * 
 * @author nikunj
 * 
 */

@Controller
@RequestMapping("/manage")
public class ServiceController extends BaseController {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			ServiceController.class);

	/** The Constant USER. */
	private static final String USER = "user";

	/** The Constant CLUSTER_SERVICE. */
	private static final String CLUSTER_SERVICE = "Cluster service";

	/** The Constant ACTION. */
	private static final String ACTION = "action";

	/** The Constant CLUSTER_ID. */
	private static final String CLUSTER_ID = "clusterId";

	/**
	 * Manage cluster services.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param action
	 *            the action
	 * @param request
	 *            the request
	 * @param list
	 *            the list
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/service/{action}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> service(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(ACTION) String action, HttpServletRequest request,
			@RequestBody List list) {
		return wrapResponse(
				new OracleNoSQLService().service(clusterId, action,
						request.getParameterMap(), list), HttpStatus.OK,
				HttpStatus.OK.toString(), CLUSTER_SERVICE);
	}

	/**
	 * Manage cluster configuration.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param action
	 *            the action
	 * @param request
	 *            the request
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/{action}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> manage(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(ACTION) String action, HttpServletRequest request) {
		return wrapResponse(
				new OracleNoSQLService().service(clusterId, action,
						request.getParameterMap(), null), HttpStatus.OK,
				HttpStatus.OK.toString(), CLUSTER_SERVICE);
	}

	/**
	 * Manage cluster level operations.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param action
	 *            the action
	 * @param request
	 *            the request
	 * @param map
	 *            the map
	 * @param p
	 *            the p
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/config/{action}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> configuration(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@PathVariable(ACTION) String action, HttpServletRequest request,
			@RequestBody Map map, Principal p) {
		map.put(USER, p.getName());
		return wrapResponse(
				new OracleNoSQLService().service(clusterId, action,
						request.getParameterMap(), map), HttpStatus.OK,
				HttpStatus.OK.toString(), CLUSTER_SERVICE);
	}

	/**
	 * Manage services.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param map
	 *            the map
	 * @param p
	 *            the p
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/manage")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> manageServices(
			@PathVariable(CLUSTER_ID) Long clusterId,
			@RequestBody ServiceConf conf) {
		Map map = null;
		try {
			ServiceManager manager = new ServiceManager();
			map = manager.manage(clusterId, conf);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				CLUSTER_SERVICE);
	}
}
