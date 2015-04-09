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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * The Class AnkushHadoopClusterCreation.
 */
@Controller
@RequestMapping("/hadoop-cluster")
public class AnkushHadoopClusterCreation extends AbstractController {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
	.getLogger(AnkushHadoopClusterCreation.class);
	
	/** The css files. */
	private ArrayList<String> cssFiles;
	
	/** The js files. */
	private ArrayList<String> jsFiles;
	
	/**
	 * Instantiates a new ankush hadoop cluster creation.
	 */
	public AnkushHadoopClusterCreation() {
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();
		jsFiles.add("hadoop/ankush.hadoop-cluster");
		jsFiles.add("ankush.validation");
		jsFiles.add("ankush.common");
	}
	
	/**
	 * home view to render by returning its name.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(ModelMap model) {
		model.addAttribute("title", "dashboard");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "hadoop/hadoop-home";
	}
	
	/**
	 * Home error.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/home/{clusterId}", method = RequestMethod.GET)
	public String homeError(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside Error Hadoop Cluster  view");
		model.addAttribute("title", "dashboard");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		return "hadoop/hadoop-home";
	}
	
	/**
	 * Hadoop setup details.
	 *
	 * @param model the model
	 * @return the string
	 */
//	@RequestMapping(value = "/hSetupDetails", method = RequestMethod.GET)
//	public String hadoopSetupDetails(ModelMap model) {
//		logger.info("Inside setupdetails hadoop view");
//		model.addAttribute("title", "HadoopClusterSetupDetails");
//		return "hadoop/deployment_hadoop/hSetupDetails";
//	}
//	
	@RequestMapping(value = "/setupDetail/{clusterId}", method = RequestMethod.GET)
	public String clusterCreate(ModelMap model,@PathVariable("clusterId") String clusterId) {
	logger.info("Inside cluster create view");
	model.addAttribute("title", "setupDetail");
	model.addAttribute("cssFiles", cssFiles);
	model.addAttribute("clusterId", clusterId);
	return "hadoop/hadoop-home";
	}
	
	/**
	 * Hadoop node details_ creation.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/hadoopNodeDetails", method = RequestMethod.GET)
	public String hadoopNodeDetailsCreation(ModelMap model) {
		logger.info("Inside Cluster Deployment Node Status view");
		model.addAttribute("title", "setupdetails");
		return "hadoop/hadoopNodeDetails";
	}
	
	/**
	 * Hadoop error node details_ creation.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/hadoopErrorNodeDetails", method = RequestMethod.GET)
	public String hadoopErrorNodeDetailsCreation(ModelMap model) {
		logger.info("Inside Cluster Error Node Status view");
		model.addAttribute("title", "ErrorNodeDetails");
		return "hadoop/hNodeStatusError";
	}
	
	/**
	 * Deployment hadoop.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/deploymenthadoop/{clusterId}", method = RequestMethod.GET)
	public String deploymentHadoop(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside Cluste deployment hadoop view");
		
		model.addAttribute("title", "dashboard");
	
		model.addAttribute("clusterId", clusterId);
		return "hadoop/deployment_hadoop/deploymenthadoop";
	}

	/**
	 * Hadoop node details.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/hNodeStatus", method = RequestMethod.GET)
	public String hadoopNodeDetails(ModelMap model) {
		logger.info("Inside Hadoop Cluster Deployment Progress Node Details view");
		model.addAttribute("title", "dashboard");
		return "hadoop/deployment_hadoop/hNodeStatus";
	}
	
	/**
	 * Hadoop component details.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/HadoopDetails",method = RequestMethod.GET)
	public String hadoopComponentDetails(ModelMap model) {
		logger.info("Inside Hadoop Advance Fields view");
		model.addAttribute("title", "dashboard");
		return "hadoop/hadoopChildPage";
	}
	
	/**
	 * Hadoop configurations hadoop ecosystem advanced settings.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/hAdvDetailsHadoop", method = RequestMethod.GET)
	public String hadoopConfigurationsHadoopEcosystemAdvancedSettings(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "HadoopAdvancedSettings");
		return "hadoop/monitoring_hadoop/configurations_hadoop_details_advancedSettings";
	}
	
	/**
	 * Deployment prg node setup detail.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/hNodeSetupDetail", method = RequestMethod.GET)
	public String deploymentPrgNodeSetupDetail(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "HadoopAdvancedSettings");
		return "hadoop/deployment_hadoop/hNodeSetupDetail";
	}
	
	/**
	 * 
	 * @param model the model
	 * @param componentName the component name
	 * @return the string
	 */
	@RequestMapping(value = "/componentDetails/{componentName}", method = RequestMethod.GET)
	public String hiveComponentDetails(ModelMap model,@PathVariable("componentName") String componentName) {
		logger.info("Inside dashboard home view");
		model.addAttribute("title", "dashboard");
		model.addAttribute("componentName",componentName);
		return "hadoop/componentDetails";
	}
	
	/**
	 * Deployment prg component details.
	 *
	 * @param model the model
	 * @param componentName the component name
	 * @return the string
	 */
	@RequestMapping(value = "/hComponentDetails/{componentName}", method = RequestMethod.GET)
	public String deploymentPrgComponentDetails(ModelMap model,@PathVariable("componentName") String componentName) {
		logger.info("Inside dashboard home view");
		model.addAttribute("title", "dashboard");
		model.addAttribute("componentName",componentName);
		return "hadoop/monitoring_hadoop/componentDetailsView";
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
}
