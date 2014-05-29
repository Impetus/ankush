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
@RequestMapping("/elasticSearch-cluster")
public class AnkushElasticSearchController extends AbstractController {
	private static final Logger logger = LoggerFactory
	.getLogger(AnkushElasticSearchController.class);
	
	private ArrayList<String> cssFiles;
	private ArrayList<String> jsFiles;
	public AnkushElasticSearchController() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();	
		jsFiles = new ArrayList<String>();
		jsFiles.add("ElasticSearch/elasticSearchClusterCreation");
		jsFiles.add("ElasticSearch/elasticSearchSetupDetail");
		jsFiles.add("ankush.validation");
		jsFiles.add("ankush.common");
		jsFiles.add("ErrorMessage");
	}
	/**
	 * home view to render by returning its name.
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String elasticSearchCreate(ModelMap model) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "elasticSearch/elasticSearchClusterCreation/elasticSearchClusterCreate";
	}
	@RequestMapping(value = "/home/{clusterId}", method = RequestMethod.GET)
	public String elasticSearchCreate(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		return "elasticSearchCreate/elasticSearchCreateClusterCreation/elasticSearchCreateClusterCreate";
	}
	@RequestMapping(value = "/setupDetail/{clusterId}", method = RequestMethod.GET)
	public String oClusterCreate(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside setup deatail view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("clusterId", clusterId);
		return "elasticSearch/elasticSearchClusterCreation/elasticSearchClusterCreate";
	}
	@RequestMapping(value = "/retrievedNodeDetail", method = RequestMethod.GET)
	public String oNodeDetail(ModelMap model) {
		logger.info("Inside node details view");
		model.addAttribute("title", "oNodeDetail");
		return "elasticSearch/elasticSearchClusterCreation/retrievedNodeDetail";
	}
	@RequestMapping(value = "/clusterConf/{clusterId}", method = RequestMethod.GET)
	public String clusterConf(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside  clusterConf view");
		model.addAttribute("title", "clusterConf");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("clusterId", clusterId);
		return "elasticSearch/elasticSearchClusterCreation/elasticSearchClusterCreate";
	}
	/*	@RequestMapping(value = "/deploymentProgress/{clusterId}", method = RequestMethod.GET)
	public String deploymentProgress(ModelMap model,@PathVariable("clusterId") String clusterId,@RequestParam("clusterTechnology") String clusterTechnology) {
		logger.info("Inside deploymentProgress view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("jsFiles", jsFiles);
		return "common/deploymentLogs";
	}
	@RequestMapping(value = "/nodeStatus", method = RequestMethod.GET)
	public String nodeStatus(ModelMap model) {
		logger.info("Inside nodeStatus view");
		model.addAttribute("title", "oNodeDetail");
		return "common/nodeStatus";
	}
	*//**
	 * O node setup detail.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 *//*
	@RequestMapping(value = "/nodeStatusNode/{clusterId}/details", method = RequestMethod.GET)
	public String nodeStatusNode(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside nodeStatusNode view");
		model.addAttribute("title", "nodeStatusNode");
		model.addAttribute("clusterId", clusterId);
		return "common/nodeDeploymentLogs";
	}
	
	@RequestMapping(value = "/clusterConf/{clusterId}", method = RequestMethod.GET)
	public String clusterConf(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside  clusterConf view");
		model.addAttribute("title", "clusterConf");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("clusterId", clusterId);
		return "storm/stormClusterCreation/stormClusterCreate";
	}*/
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
