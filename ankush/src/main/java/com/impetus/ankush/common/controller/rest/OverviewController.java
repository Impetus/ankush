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

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush2.framework.monitor.AnkushMonitor;

/**
 * The Class OverviewController.
 */
@Controller
@RequestMapping("/")
public class OverviewController extends BaseController {

	/**
	 * Gets the cluster overview.
	 * 
	 * @return the cluster overview
	 */
	@RequestMapping(method = RequestMethod.GET, value = "clusteroverview")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<List<Map<String, Object>>>> getClusterOverview() {
		return wrapResponse(new AnkushMonitor().getClusterOverview(),
				HttpStatus.OK, HttpStatus.OK.toString(),
				"Cluster overview dashboard");
	}

	/**
	 * Gets the system overview.
	 * 
	 * @return the system overview
	 */
	@RequestMapping(method = RequestMethod.GET, value = "systemoverview")
	@ResponseBody
	public ResponseEntity<ResponseWrapper<List<TileInfo>>> getSystemOverview() {

		return wrapResponse(new AnkushMonitor().getSystermOverview(),
				HttpStatus.OK, HttpStatus.OK.toString(),
				"system overview details.");
	}
}
