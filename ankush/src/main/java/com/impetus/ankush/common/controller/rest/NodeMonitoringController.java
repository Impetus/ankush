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

import java.util.HashMap;
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

import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.ResponseWrapper;

/**
 * The Class NodeMonitoringController.
 * 
 * @author hokam
 */
@Controller
@RequestMapping("/monitor")
public class NodeMonitoringController extends BaseController {

	/** The log. */
	private static final AnkushLogger LOG = new AnkushLogger(
			NodeMonitoringController.class);

	/**
	 * Save monitoring info.
	 * 
	 * @param id
	 *            the id
	 * @param infoMap
	 *            the info map
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/node/{id}/info", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<HttpStatus> saveMonitoringInfo(@PathVariable Long id,
			@RequestBody Map infoMap, HttpServletRequest rq) throws Exception {
		try {
			MonitoringManager monitoringManager = new MonitoringManager();
			if (monitoringManager.saveNodeMonitoringInfo(id, infoMap)) {
				return new ResponseEntity<HttpStatus>(HttpStatus.OK);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Save service status.
	 * 
	 * @param id
	 *            the id
	 * @param infoMap
	 *            the info map
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/node/{id}/status", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity saveServiceStatus(@PathVariable Long id,
			@RequestBody HashMap<String, Map<String, Boolean>> infoMap,
			HttpServletRequest rq) throws Exception {
		try {
			MonitoringManager monitoringManager = new MonitoringManager();
			if (monitoringManager.saveTecnologyServiceStatus(id, infoMap)) {
				return new ResponseEntity<HttpStatus>(HttpStatus.OK);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Save jobs info.
	 * 
	 * @param id
	 *            the id
	 * @param hadoopMonitoringData
	 *            the hadoop monitoring data
	 * @return the response entity
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/node/{id}/monitoring", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<HttpStatus> saveMonitoringData(@PathVariable Long id,
			@RequestBody TechnologyData technologyData, HttpServletRequest rq)
			throws Exception {
		try {
			MonitoringManager monitoringManager = new MonitoringManager();
			monitoringManager.saveMonitoringData(id, technologyData);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/node/{id}/nnrole", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<HttpStatus> updateNameNodeRole(@PathVariable Long id,
			@RequestBody HashMap<String, String> roleInfoMap) throws Exception {
		try {
//			HadoopCluster hadoopCluster = new HadoopCluster();
//			hadoopCluster.updateNameNodeRole(id, roleInfoMap);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Gets the monitoring info.
	 * 
	 * @param nodeId
	 *            the node id
	 * @return the monitoring info
	 * @throws Exception
	 *             the exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/node/{nodeId}/info")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<NodeMonitoring>> getMonitoringInfo(
			@PathVariable Long nodeId) throws Exception {

		try {
			MonitoringManager manager = new MonitoringManager();
			return wrapResponse(manager.getMonitoringData(nodeId),
					HttpStatus.OK, HttpStatus.OK.toString(),
					"system overview details.");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return wrapResponse(null, HttpStatus.OK, HttpStatus.OK.toString(),
				"system overview details.");
	}
}
