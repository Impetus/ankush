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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
@Controller
@RequestMapping("/elasticSearchMonitoring")
public class AnkushElasticSearchMonitoringController extends AbstractController{
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushHadoopClusterMonitoring.class);
	/** Constructor. */
	public AnkushElasticSearchMonitoringController() {
		// TODO Auto-generated constructor stub
	}
	/** this will show topics detail table in topic drill down. */
	@RequestMapping(value = "/indexDrillDown", method = RequestMethod.GET)
	public String topicDrillDown(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside Elastic Search index drill down monitoring view");
		return "elasticSearch/elasticSearchMonitoring/indexDrillDown";
	}
	/** this will show shards table*/
	@RequestMapping(value = "/shards", method = RequestMethod.GET)
	public String shards(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside Elastic Search Shards monitoring view");
		return "elasticSearch/elasticSearchMonitoring/shardsTable";
	}
	/** this will show shards table*/
	@RequestMapping(value = "/createIndex", method = RequestMethod.GET)
	public String createIndex(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside Elastic Search Create index monitoring view");
		return "elasticSearch/elasticSearchMonitoring/createIndex";
	}
	/** this will show shards table*/
	@RequestMapping(value = "/createAlias", method = RequestMethod.GET)
	public String createAlias(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside Elastic Search Create Alias monitoring view");
		return "elasticSearch/elasticSearchMonitoring/createAlias";
	}
	/** this will show shards table*/
	@RequestMapping(value = "/aliasList", method = RequestMethod.GET)
	public String aliasList(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside Elastic Search Alias List view");
		return "elasticSearch/elasticSearchMonitoring/aliasList";
	}
	/** this will show shards table*/
	@RequestMapping(value = "/administration", method = RequestMethod.GET)
	public String administration(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside Elastic Search Alias List view");
		return "elasticSearch/elasticSearchMonitoring/administration";
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
