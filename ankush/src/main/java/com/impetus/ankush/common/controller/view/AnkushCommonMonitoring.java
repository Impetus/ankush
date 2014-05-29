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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;



@Controller
@RequestMapping("/commonMonitoring")
public class AnkushCommonMonitoring extends AbstractController {
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushHadoopClusterMonitoring.class);
	/** Constructor. */
	public AnkushCommonMonitoring() {
		// TODO Auto-generated constructor stub
	}
	/** Main monitoring page. */
	@RequestMapping(value = "/home", method = RequestMethod.POST)
	public String common(ModelMap model, @RequestParam("clusterTechnology") String clusterTechnology,@RequestParam("clusterName") String clusterName,@RequestParam("clusterId") Long clusterId,@RequestParam("clusterEnvironment") String clusterEnv) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside common monitoring view");
		jsFiles.removeAll(jsFiles);
		if(clusterTechnology.equalsIgnoreCase("storm")){
			jsFiles.add("storm/stormSetupDetail");
		}else if(clusterTechnology.equalsIgnoreCase("cassandra")){
			jsFiles.add("cassandra/cassandraSetupDetail");
		}else if(clusterTechnology.equalsIgnoreCase("kafka")){
			jsFiles.add("kafka/kafkaSetupDetail");
		} else if(clusterTechnology.equalsIgnoreCase("hybrid")){
			jsFiles.add("hybrid/hybridSetupDetail");
		}else if(clusterTechnology.equalsIgnoreCase("elasticSearch")){
			jsFiles.add("ElasticSearch/elasticSearchSetupDetail");
		}
		jsFiles.add("commonMonitoring/auditTrails");
		jsFiles.add("commonMonitoring/nodes");
		jsFiles.add("commonMonitoring/events");
		jsFiles.add("commonMonitoring/nodeDrillDown");
		jsFiles.add("hadoop/hadoopJobs");
		jsFiles.add("ankush.common");
		jsFiles.add("ankush.validation");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("clusterEnv", clusterEnv);
		return "commonMonitoringPages/monitoringPage";
	}
	/** Audit trail page. */
	@RequestMapping(value = "/auditTrails", method = RequestMethod.GET)
	public String auditTrail(ModelMap model) {
		logger.info("Inside Audit Trail view");
		ArrayList<String>jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/auditTrailDetail");
		model.addAttribute("jsFiles", jsFiles);
		return "commonMonitoringPages/auditTrails";
	}	
	/** Audit trail child page. */
	@RequestMapping(value = "/auditTrailDetails", method = RequestMethod.GET)
	public String auditTrailDetail(ModelMap model) {
		logger.info("Inside Audit Trails Details view");
		return "commonMonitoringPages/auditTrailDetails";
	}
	/** Events page. */
	@RequestMapping(value = "/events", method = RequestMethod.GET)
	public String events(ModelMap model) {
		logger.info("Inside Events view");
		return "commonMonitoringPages/events";
	}
	/** Logs page. */
	@RequestMapping(value = "/logs", method = RequestMethod.GET)
	public String logs(ModelMap model) {
		logger.info("Inside Logs page view");
		return "commonMonitoringPages/logs";
	}
	/** Nodes page. */
	@RequestMapping(value = "/nodes", method = RequestMethod.GET)
	public String nodes(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside Nodes view");
		return "commonMonitoringPages/nodes";
	}
	/** Nodes child page or heatmap child page. */
	@RequestMapping(value = "/nodeDrillDown", method = RequestMethod.GET)
	public String nodeDrillDown(ModelMap model,@RequestParam("nodeIndex") int nodeIndex,@RequestParam("clusterTechnology") String clusterTechnology,@RequestParam("hybridTechnology") String hybridTechnology) {
		logger.info("Inside nodeDrillDown view");
		model.addAttribute("nodeIndex", nodeIndex);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("hybridTechnology", hybridTechnology);
		return "commonMonitoringPages/nodeDrillDown";
	}
	/** Configuration page. */
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	public String configuration(ModelMap model) {
		logger.info("Inside configuration view");
		return "commonMonitoringPages/configuration";
	}
	/** Alert page. */
	@RequestMapping(value = "/alert", method = RequestMethod.GET)
	public String alert(ModelMap model) {
		logger.info("Inside alert view");
		return "commonMonitoringPages/alert";
	}
	/** Utilization Trend graphs details. */
	@RequestMapping(value = "/utilizationTrend", method = RequestMethod.GET)
	public String utilizationTrend(ModelMap model) {
		logger.info("Inside Utilization Trend Detailed Graph view");
		return "commonMonitoringPages/utilizationTrend";
	}
	/**Node Utilization Trend graphs details. */
	@RequestMapping(value = "/nodeUtilizationTrend", method = RequestMethod.GET)
	public String nodeUtilizationTrend(ModelMap model,@RequestParam("clusterTechnology") String clusterTechnology) {
		logger.info("Inside Node Utilization Trend Detailed Graph view");
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "commonMonitoringPages/nodeUtilizationTrends";
	}
	/** Cluster configuration page. */
	@RequestMapping(value = "/clusterConf", method = RequestMethod.GET)
	public String clusterConf(ModelMap model) {
		logger.info("Inside Cluster Configuration Page view");
		return "commonMonitoringPages/clusterConf";
	}
	/** Cluster configuration page. */
	@RequestMapping(value = "/addNodes", method = RequestMethod.GET)
	public String addNodes(ModelMap model) {
		logger.info("Inside Add Node Page view");
		return "commonMonitoringPages/addNodes";
	}
	@RequestMapping(value = "/commonNodeDetails", method = RequestMethod.GET)
	public String commonNodeDetails(ModelMap model) {
		logger.info("Inside Common Node Details Page view");
		return "commonMonitoringPages/commonNodeDetails";
	}
	@RequestMapping(value = "/addNodeProgress", method = RequestMethod.GET)
	public String addNodeProgress(ModelMap model) {
		logger.info("Inside Common Node Details Page view");
		return "commonMonitoringPages/addNodeProgress";
	}
	@RequestMapping(value = "/addNodeProgressLogs", method = RequestMethod.GET)
	public String addNodeProgressLogs(ModelMap model) {
		logger.info("Inside Common Node Details Page view");
		return "common/nodeDeploymentLogs";
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
