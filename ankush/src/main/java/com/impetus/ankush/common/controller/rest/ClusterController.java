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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impetus.ankush.common.domain.Template;
import com.impetus.ankush.common.framework.ClusterManager;
import com.impetus.ankush.common.framework.ClusterPreValidator;
import com.impetus.ankush.common.framework.TemplateManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush2.framework.config.ClusterConfig;

/**
 * The Class ClusterController.
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController extends BaseController {

	/** The user manager. */
	private UserManager userManager;

	/**
	 * Sets the user manager.
	 * 
	 * @param userManager
	 *            the new user manager
	 */
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
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
			@RequestBody ClusterConfig clusterConfig,
			@PathVariable Long clusterId, Principal principal) throws Exception {

		logger.debug("Cluster redeploy request " + clusterConfig.getName()
				+ " User " + principal.getName());

		// Set the current user from the principal to clusterconf.
		clusterConfig.setCreatedBy(principal.getName());
		// Create Cluster

		Object objectret = new com.impetus.ankush2.framework.manager.ClusterManager(
				principal.getName()).redeploy(clusterId, clusterConfig);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster creation activity in progress");
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
	 * Template REST API to save/update the template.
	 * 
	 * @param template
	 * @param update
	 * @param principle
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/template", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> saveTemplate(
			@RequestBody Template template,
			@RequestParam(required = false, defaultValue = "false") boolean update,
			Principal principle) {
		TemplateManager templateManager = new TemplateManager();
		template.setUser(principle.getName());
		Map map = templateManager.saveTemplate(template, update);
		return wrapResponse((Object) map, HttpStatus.OK,
				HttpStatus.OK.toString(), "Cluster Templates");
	}

	/**
	 * Template REST API to template by name.
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/template", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> getTemplate(
			@RequestParam String name) {
		TemplateManager templateManager = new TemplateManager();
		Map map = templateManager.getTemplateMap(name);
		return wrapResponse((Object) map, HttpStatus.OK,
				HttpStatus.OK.toString(), "Cluster Templates");
	}

	/**
	 * Template REST API to get templates by technology and user name.
	 * 
	 * @param technology
	 * @param principal
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/templates")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<List<Template>>> getTemplates(
			@RequestParam(required = false, defaultValue = com.impetus.ankush2.constant.Constant.Keys.ALL) String technology) {

		TemplateManager templateManager = new TemplateManager();
		List<Template> templates;
		if (technology.equals(com.impetus.ankush2.constant.Constant.Keys.ALL)) {
			templates = templateManager.getAll();
		} else {
			templates = templateManager.getAll(technology);
		}
		return wrapResponse(templates, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster Templates");
	}

	/**
	 * Template REST API to delete the template by name.
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/template")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<HttpStatus>> deleteTemplate(
			@RequestParam String name) {

		TemplateManager templateManager = new TemplateManager();
		templateManager.delete(name);
		return wrapResponse(HttpStatus.OK, HttpStatus.OK,
				HttpStatus.OK.toString(), "Cluster Templates");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/create")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> createNewCluster(
			@RequestBody ClusterConfig clusterConfig, Principal principal)
			throws Exception {

		logger.debug("Cluster creation request " + clusterConfig.getName()
				+ " User " + principal.getName());

		// Set the current user from the principal to clusterconf.
		clusterConfig.setCreatedBy(principal.getName());
		// Create Cluster

		com.impetus.ankush2.framework.manager.ClusterManager manager = new com.impetus.ankush2.framework.manager.ClusterManager(
				principal.getName());
		Object objectret = manager.deploy(clusterConfig);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster creation activity in progress");
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/removecluster/{id}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map<String, Object>>> removeNewCluster(
			@PathVariable Long id, Principal principal) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// Remove Cluster
			com.impetus.ankush2.framework.manager.ClusterManager manager = new com.impetus.ankush2.framework.manager.ClusterManager(
					principal.getName());
			returnMap = manager.undeploy(id);
			return wrapResponse(returnMap, HttpStatus.OK,
					HttpStatus.OK.toString(), "Cluster removal is inprogress");
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnMap.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, false);
		return wrapResponse(returnMap, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster removal failed.");
	}/**
	 * Deploys technology patch.
	 * 
	 * @param serverPatchTarballLocation
	 *            the patch bundle location on server
	 * @param clusterId
	 *            the cluster id
	 * @param componentId
	 *            the componentId for which patch will be deployed
	 * @param principal
	 *            the principal
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/patchDeploy/{clusterId}/{componentId}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> deployPatch(
			@RequestBody Map map, @PathVariable long clusterId,
			@PathVariable String componentId, HttpServletRequest request)
			throws Exception {
		ClusterManager manager = new ClusterManager();
		Object objectret = manager.deployPatch(clusterId, componentId, map);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Patch deployment activity in progress");
	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/addnode")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> addNodes(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody ClusterConfig newclusterConfig, Principal principal)
			throws Exception {

		// Set the current user from the principal to clusterconf.
		newclusterConfig.setCreatedBy(principal.getName());

		// Add Nodes to Cluster
		com.impetus.ankush2.framework.manager.ClusterManager manager = new com.impetus.ankush2.framework.manager.ClusterManager(principal.getName());
		Object objectret = manager.addNodes(clusterId, newclusterConfig);
		return wrapResponse(objectret, HttpStatus.OK, HttpStatus.OK.toString(),
				"Node addition activity in progress");
	}

	@RequestMapping(method = RequestMethod.POST, value = "{clusterId}/removenode")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> removeNodes(
			@PathVariable("clusterId") Long clusterId,
			@RequestBody Map<String, Object> parameterMap, Principal principal)
			throws Exception {
		String userName = principal.getName();
		String error = "Given password doesn't match.You are not authorized to remove nodes.";
		try {
			if (parameterMap.get("password") == null
					|| ((String) parameterMap.get("password")).isEmpty()) {
				error = "Please provide a valid password to delete these nodes.";
			} else {
				if (userManager.doesPasswordMatch(userName,
						parameterMap.get("password").toString())) {
					// Remove Nodes from Cluster
					com.impetus.ankush2.framework.manager.ClusterManager manager = new com.impetus.ankush2.framework.manager.ClusterManager(principal.getName());
					Object objectret = manager.removeNodes(clusterId,
							(List<String>) parameterMap.get("nodes"));
					// Object objectret = null;
					return wrapResponse(objectret, HttpStatus.OK,
							HttpStatus.OK.toString(),
							"Node removal activity in progress");
				}
			}
		} catch (Exception e) {
			error = e.getMessage() != null ? e.getMessage()
					: "Couldn't remove node. ";
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, false);
		returnMap.put(com.impetus.ankush2.constant.Constant.Keys.ERROR, error);
		return wrapResponse((Object) returnMap, HttpStatus.OK,
				HttpStatus.OK.toString(), "Node deletion failed.");
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/unregisterCluster/{id}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map<String, Object>>> unregisterNewCluster(
			@PathVariable Long id, Principal principal) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			// Remove Cluster
			com.impetus.ankush2.framework.manager.ClusterManager manager = new com.impetus.ankush2.framework.manager.ClusterManager(principal.getName());
			returnMap = manager.unregister(id);
			return wrapResponse(returnMap, HttpStatus.OK,
					HttpStatus.OK.toString(),
					"Cluster unregister is inprogress");
		} catch (Exception e) {
			
		}
		returnMap.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, false);
		return wrapResponse(returnMap, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster unregister failed.");
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/removemixcluster/{id}")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map<String, Object>>> removeCustomCluster(
			@PathVariable Long id,
			@RequestBody Map<String, Object> parameterMap, Principal principal)
			throws Exception {

		String userName = principal.getName();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String error = "Given password doesn't match.You are not authorized to delete cluster.";
		try {
			if (parameterMap.get("password") == null
					|| ((String) parameterMap.get("password")).isEmpty()) {
				error = "Please provide a valid password to delete this cluster.";
			} else {
				if (userManager.doesPasswordMatch(userName,
						parameterMap.get("password").toString())) {

					// Remove Cluster
					com.impetus.ankush2.framework.manager.ClusterManager manager = new com.impetus.ankush2.framework.manager.ClusterManager(principal.getName());
					returnMap = manager.customUninstall(id);
					return wrapResponse(returnMap, HttpStatus.OK,
							HttpStatus.OK.toString(),
							"Cluster removal is inprogress");
				}

			}
		} catch (Exception e) {
			error = e.getMessage() != null ? e.getMessage()
					: "Couldn't delete cluster. ";
		}
		returnMap.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, false);
		returnMap.put(com.impetus.ankush2.constant.Constant.Keys.ERROR, error);
		return wrapResponse(returnMap, HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster removal failed.");
	}
}
