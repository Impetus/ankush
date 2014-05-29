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
import org.springframework.web.bind.annotation.ResponseBody;

import com.impetus.ankush.common.controller.rest.BaseController;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush.hadoop.scheduler.SchedulerConfig;
import com.impetus.ankush.hadoop.service.SchedulerConfigService;

/**
 * The Class SchedulerConfController.
 *
 * @author mayur
 */
@Controller
@RequestMapping("/job")
public class SchedulerConfController extends BaseController {

	/** The service. */
	private SchedulerConfigService service;

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			SchedulerConfController.class);

	/**
	 * Sets the service.
	 *
	 * @param service the new service
	 */
	@Autowired
	public void setService(SchedulerConfigService service) {
		this.service = service;
	}

	/**
	 * Save.
	 *
	 * @param clusterId the cluster id
	 * @param schedulerConf the scheduler conf
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = { "/{clusterId}/scheduler" })
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> save(
			@PathVariable Long clusterId,
			@RequestBody SchedulerConfig schedulerConf,
			HttpServletRequest request) throws Exception {
		Map map = service.saveConfig(clusterId, schedulerConf);
		if (schedulerConf != null) {
			logger.debug(schedulerConf.toXML());
			logger.debug(SchedulerConfig.parametersToString(schedulerConf
					.toParameters()));
		}
		return wrapResponse(map, HttpStatus.OK, HttpStatus.OK.toString(),
				"config info");
	}

	/**
	 * Gets the config.
	 *
	 * @param clusterId the cluster id
	 * @return the config
	 * @throws Exception the exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{clusterId}/scheduler")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> getConfig(
			@PathVariable Long clusterId) throws Exception {
		return wrapResponse(service.getConfig(clusterId), HttpStatus.OK,
				HttpStatus.OK.toString(), "config info");
	}
}
