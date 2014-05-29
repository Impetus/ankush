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
package com.impetus.ankush.common.controller.view;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

@Controller
@RequestMapping("/cassandraMonitoring")
public class AnkushCassandraMonitoringController extends AbstractController {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushCassandraMonitoringController.class);
	
	/** The css files. */
	private ArrayList<String> cssFiles;
	
	/** The js files. */
	private ArrayList<String> jsFiles;
	
	/**
	 * Instantiates a new ankush cassandra cluster monitoring.
	 */
	public AnkushCassandraMonitoringController() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();
		jsFiles.add("cassandra/cassandraMonitoring");
		jsFiles.add("ankush.common");
	}
	//this will load add node page for cassandra monitoring
	@RequestMapping(value = "/addNodes/{clusterId}", method = RequestMethod.GET)
	public String cassandraAddNodes(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside cassandraAddNodes view");
		model.addAttribute("title", "addNodes");
		model.addAttribute("clusterId", clusterId);
		return "cassandra/cassandraClusterMonitoring/cassandraAddNodes";
	}
	//this will load keyspaces page for cassandra monitoring
	@RequestMapping(value = "/keyspaces", method = RequestMethod.GET)
	public String keySpaces(ModelMap model) {
		logger.info("Inside keyspaces view");
		return "cassandra/cassandraClusterMonitoring/keyspaces";
	}
	//this will load keyspacesdrilldown page for cassandra monitoring
	@RequestMapping(value = "/keyspaceDrillDown", method = RequestMethod.GET)
	public String keyspaceDrillDown(ModelMap model,@RequestParam("keyspace") String keyspace,@RequestParam("index") String index) {
		logger.info("Inside keyspaces view");
		model.addAttribute("keyspace", keyspace);
		model.addAttribute("index", index);
		return "cassandra/cassandraClusterMonitoring/keyspaceDrillDown";
	}
	//this will load colomnfamilydrilldown page for cassandra monitoring
	@RequestMapping(value = "/columnFamilyDrillDown", method = RequestMethod.GET)
	public String columnFamilyDrillDown(ModelMap model,@RequestParam("keyspace") String keyspace,@RequestParam("columnfamily") String columnfamily) {
		logger.info("Inside keyspaces view");
		model.addAttribute("keyspace", keyspace);
		model.addAttribute("columnfamily", columnfamily);
		return "cassandra/cassandraClusterMonitoring/columnFamilyDrillDown";
	}
	//this will load parameters page for cassandra monitoring
	@RequestMapping(value = "/parameters", method = RequestMethod.GET)
	public String parameters(ModelMap model) {
		logger.info("Inside keyspaces view");
		return "cassandra/cassandraClusterMonitoring/parameters";
	}
	//this will load parameters page for cassandra monitoring
	@RequestMapping(value = "/nodeParameters", method = RequestMethod.GET)
	public String nodeParameters(ModelMap model) {
		logger.info("Inside keyspaces view");
		return "cassandra/cassandraClusterMonitoring/nodeParams";
	}
	//this will load parameters page for cassandra monitoring
		@RequestMapping(value = "/tokenList", method = RequestMethod.GET)
		public String tokenList(ModelMap model) {
			logger.info("Inside Token List view");
			return "cassandra/cassandraClusterMonitoring/tokenList";
		}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
