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
 * The Class AnkushHadoopClusterMonitoring.
 */
@Controller
@RequestMapping("/hadoop-cluster-monitoring")
public class AnkushHadoopClusterMonitoring extends AbstractController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushHadoopClusterMonitoring.class);
	
	/** The css files. */
	private ArrayList<String> cssFiles;
	
	/** The js files. */
	private ArrayList<String> jsFiles;
	
	/**
	 * Instantiates a new ankush hadoop cluster monitoring.
	 */
	public AnkushHadoopClusterMonitoring() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();
		jsFiles.add("hadoop/hadoopMonitoring");
		jsFiles.add("ankush.common");
		jsFiles.add("hadoop/heatMap");
	}
	
	/**
	 * Hadoop submit jobs.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/hadoopUtilizationTrend", method = RequestMethod.GET)
	public String hadoopUtilizationtrend(ModelMap model) {
		model.addAttribute("title", "hadoopUtilizationTrend");
		return "hadoop/monitoring_hadoop/hadoopUtilizationTrend";
	}
	
	/**
	 * Hadoop submit jobs.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/{clusterName}/{hybridTechnology}/submitJobs/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String hadoopSubmitJobs(ModelMap model
			,@PathVariable("clusterId") String clusterId
			,@PathVariable("clusterName") String clusterName
			,@PathVariable("clusterTechnology") String clusterTechnology
			,@PathVariable("hybridTechnology") String hybridTechnology) {
		logger.info("Inside Hadoop Submit Jobs view");
		model.addAttribute("clusterId",clusterId);
		model.addAttribute("clusterTechnology",clusterTechnology);
		model.addAttribute("clusterName",clusterName);
		model.addAttribute("hybridTechnology",hybridTechnology);
		model.addAttribute("page","submitJob");
		return "hadoop/monitoring_hadoop/jobSubmission";
	}
	
	/**
	 * Hadoop job details.
	 *
	 * @param model the model
	 * @param jobId the job id
	 * @return the string
	 */
	@RequestMapping(value = "/jobDetails/{jobId}", method = RequestMethod.GET)
	public String hadoopJobDetails(ModelMap model,@PathVariable("jobId") String jobId) {
		logger.info("Inside Hadoop Job Details view");
		model.addAttribute("title", "hadoopJobDetails");
		model.addAttribute("jobId",jobId);
		return "hadoop/monitoring_hadoop/jobDetailsChildPage";
	}
	
	/**
	 * Hadoop job monitoring.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/{clusterName}/{hybridTechnology}/jobMonitoring/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String hadoopJobMonitoring(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("hybridTechnology") String hybridTechnology) {
		logger.info("Inside Hadoop Job Monitoring view");
		model.addAttribute("page", "hadoopJobMonitoring");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("hybridTechnology", hybridTechnology);
		return "hadoop/monitoring_hadoop/jobMonitoring";
	}
	
	/**
	 * Hadoop job monitoring.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/{clusterName}/{hybridTechnology}/applicationMonitoring/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String hadoopAppMonitoring(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("hybridTechnology") String hybridTechnology) {
		logger.info("Inside Hadoop Job Monitoring view");
		model.addAttribute("clusterId",clusterId);
		model.addAttribute("clusterName",clusterName);
		model.addAttribute("clusterTechnology",clusterTechnology);
		model.addAttribute("hybridTechnology",hybridTechnology);
		model.addAttribute("page","applicationMonitoring");
		return "hadoop/monitoring_hadoop/applicationMonitoring";
	}
	 /**
		 * Hadoop job monitoring.
		 *
		 * @param model the model
		 * @param clusterId the cluster id
		 * @return the string
		 */
		@RequestMapping(value = "/appMonitoringDrillDown/{clusterId}/{clusterName}/{clusterTechnology}/{hybridTechnology}/{applicationId}", method = RequestMethod.GET)
		public String hadoopAppMonitoringDrillDown(ModelMap model,
				@PathVariable("clusterId") String clusterId,
				@PathVariable("clusterName") String clusterName,
				@PathVariable("clusterTechnology") String clusterTechnology,
				@PathVariable("hybridTechnology") String hybridTechnology,
				@PathVariable("applicationId") String applicationId){
			logger.info("Inside Hadoop Job Monitoring view");
			model.addAttribute("clusterId",clusterId);
			model.addAttribute("clusterName",clusterName);
			model.addAttribute("clusterTechnology",clusterTechnology);
			model.addAttribute("hybridTechnology",hybridTechnology);
			model.addAttribute("applicationId",applicationId);
			model.addAttribute("page","applicationMonitoringDrillDown");
			return "hadoop/monitoring_hadoop/applicationMonitoringDrillDown";
		}
	
	
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
