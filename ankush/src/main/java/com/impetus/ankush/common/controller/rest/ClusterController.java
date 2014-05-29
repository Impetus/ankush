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
package com.impetus.ankush.common.controller.rest;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.impetus.ankush.common.framework.ClusterManager;
import com.impetus.ankush.common.framework.ClusterPreValidator;
import com.impetus.ankush.common.framework.Clusterable;
import com.impetus.ankush.common.framework.ObjectFactory;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.ResponseWrapper;

/**
 * The Class ClusterController.
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController extends BaseController {

	/** The host operation. */
	private HostOperation hostOperation = new HostOperation();
	
	/**
	 * Creates the cluster.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param technology
	 *            the technology
	 * @param principal
	 *            the principal
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/create/{technology}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> createCluster(
			@RequestBody ClusterConf clusterConf,
			@PathVariable String technology, Principal principal)
			throws Exception {

		logger.debug("Cluster creation request " + clusterConf.getClusterName()
				+ " User " + principal.getName());

		// Set the current user from the principal to clusterconf.
		clusterConf.setCurrentUser(principal.getName());
		// Create Cluster

		ClusterManager manager = new ClusterManager();
		Clusterable cluster = ObjectFactory
				.getClusterableInstanceById(technology);
		Object objectret = manager.create(cluster, clusterConf);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster creation activity in progress");
	}

	/**
	 * Creates the cluster.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param technology
	 *            the technology
	 * @param principal
	 *            the principal
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/redeploy/{clusterId}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> redeployCluster(
			@RequestBody ClusterConf clusterConf, @PathVariable Long clusterId,
			Principal principal) throws Exception {

		logger.debug("Cluster creation request " + clusterConf.getClusterName()
				+ " User " + principal.getName());

		// Set the current user from the principal to clusterconf.
		clusterConf.setCurrentUser(principal.getName());
		// Create Cluster

		ClusterManager manager = new ClusterManager();
		Object objectret = manager.redeploy(clusterId, clusterConf);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster creation activity in progress");
	}

	/**
	 * Adds the nodes.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param clusterConf
	 *            the cluster conf
	 * @param principal
	 *            the principal
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/add")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> addNodes(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody ClusterConf clusterConf, Principal principal)
			throws Exception {

		// Set the current user from the principal to clusterconf.
		clusterConf.setCurrentUser(principal.getName());

		// Add Nodes to Cluster
		ClusterManager manager = new ClusterManager();
		Object objectret = manager.addNodes(clusterId, clusterConf);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Node addition activity in progress");
	}

	/**
	 * Removes the nodes.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param clusterConf
	 *            the cluster conf
	 * @param principal
	 *            the principal
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/remove")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> removeNodes(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody ClusterConf clusterConf, Principal principal)
			throws Exception {

		// Set the current user from the principal to clusterconf.
		clusterConf.setCurrentUser(principal.getName());

		// Remove Nodes from Cluster
		ClusterManager manager = new ClusterManager();
		Object objectret = manager.removeNodes(clusterId, clusterConf);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Node removal activity in progress");
	}

	/**
	 * Removes the.
	 * 
	 * @param id
	 *            the id
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/remove/{id}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> remove(@PathVariable Long id)
			throws Exception {

		// Remove Cluster
		ClusterManager manager = new ClusterManager();
		Object object = manager.delete(id);
		return wrapResponse(object, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster removal is inprogress");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/validate", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> validate(
			@RequestBody LinkedHashMap params) {

		ClusterPreValidator validator = new ClusterPreValidator();
		return wrapResponse(validator.validate(params), HttpStatus.OK,
				HttpStatus.OK.toString(), "Cluster Validations");
	}

	/**
	 * Detect node params.
	 * 
	 * @param parameters
	 *            the parameters
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/detectNodes", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map<String, Object>>> detectNodeParams(
			@RequestBody Map<String, Object> parameters) {

		Map<String, Object> result = hostOperation.detectNodes(parameters);
		return wrapResponse(result, HttpStatus.OK, HttpStatus.OK.toString(),
				"DETECT NODES");
	}
}
