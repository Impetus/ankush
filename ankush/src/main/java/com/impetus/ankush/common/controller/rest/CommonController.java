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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impetus.ankush.common.service.ClusterService;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.ResponseWrapper;

/**
 * The Class CommonController.
 */
@Controller
@RequestMapping("/cluster")
public class CommonController extends BaseController {

	/** The cluster service. */
	private ClusterService clusterService;
	
	/** The host operation. */
	private HostOperation hostOperation = new HostOperation();

	/**
	 * Sets the cluster service.
	 *
	 * @param clusterManager the new cluster service
	 */
	@Autowired
	public void setClusterService(
			@Qualifier("clusterService") ClusterService clusterManager) {
		this.clusterService = clusterManager;
	}

	/**
	 * Detect node params.
	 *
	 * @param parameters the parameters
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
