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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

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

import com.impetus.ankush.common.utils.ParserUtil;

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
	@RequestMapping(value = "/submitJobs/{clusterId}", method = RequestMethod.GET)
	public String hadoopSubmitJobs(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside Hadoop Submit Jobs view");
		model.addAttribute("title", "hadoopSubmitJobs");
		model.addAttribute("clusterId",clusterId);
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
	@RequestMapping(value = "/jobMonitoring/{clusterId}", method = RequestMethod.GET)
	public String hadoopJobMonitoring(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside Hadoop Job Monitoring view");
		model.addAttribute("title", "hadoopJobMonitoring");
		model.addAttribute("clusterId",clusterId);
		return "hadoop/monitoring_hadoop/jobMonitoring";
	}
	
	/**
	 * Hadoop job monitoring.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/appMonitoring/{clusterId}", method = RequestMethod.GET)
	public String hadoopAppMonitoring(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside Hadoop Job Monitoring view");
		model.addAttribute("clusterId",clusterId);
		return "hadoop/monitoring_hadoop/applicationMonitoring";
	}
	 /**
		 * Hadoop job monitoring.
		 *
		 * @param model the model
		 * @param clusterId the cluster id
		 * @return the string
		 */
		@RequestMapping(value = "/appMonitoringDrillDown", method = RequestMethod.GET)
		public String hadoopAppMonitoringDrillDown(ModelMap model) {
			logger.info("Inside Hadoop Job Monitoring view");
			return "hadoop/monitoring_hadoop/applicationMonitoringDrillDown";
		}
	/**
	 * Hadoop commands.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/commands", method = RequestMethod.GET)
	public String hadoopCommands(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "hadoopComands");
		return "hadoop/monitoring_hadoop/hadoop-commands";
	}
	
	/**
	 * Hadoop command archive.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/commands/archive", method = RequestMethod.GET)
	public String hadoopCommandArchive(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "archive");
		return "hadoop/monitoring_hadoop/hadoopCommandArchive";
	}
	
	/**
	 * Hadoop command distcp.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/commands/distcp", method = RequestMethod.GET)
	public String hadoopCommandDistcp(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "distcp");
		return "hadoop/monitoring_hadoop/hadoopCommandDistcp";
	}
	
	/**
	 * Hadoop command balancer.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/commands/balancer", method = RequestMethod.GET)
	public String hadoopCommandBalancer(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "balancer");
		return "hadoop/monitoring_hadoop/hadoopCommandBalancer";
	}
	
	/**
	 * Hadoop command fsck.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/commands/fsck", method = RequestMethod.GET)
	public String hadoopCommandFsck(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "fsck");
		return "hadoop/monitoring_hadoop/hadoopCommandFsck";
	}
	
	/**
	 * Hadoop monitoring details.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/hadoopMonitoringDetails/{clusterId}", method = RequestMethod.GET)
	public String hadoopMonitoringDetails(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside monitoring details view");
		model.addAttribute("title", "hadoopMonitoringDetails");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId",clusterId);
		return "hadoop/monitoring_hadoop/hadoopMonitoringDetails";
	}
	
	/**
	 * Hadoop configurations.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/configurations/{clusterId}", method = RequestMethod.GET)
	public String hadoopConfigurations(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "configurations");
		model.addAttribute("clusterId",clusterId);
		return "hadoop/monitoring_hadoop/Configurations";
	}
	
	/**
	 * Hadoop configurations cluster.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/configurations/cluster", method = RequestMethod.GET)
	public String hadoopConfigurationsCluster(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "configurationsCluster");
		return "hadoop/monitoring_hadoop/configurations_Cluster";
	}
	
	/**
	 * Hadoop configurations hadoop ecosystem.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/configurations/hadoopEcosystem", method = RequestMethod.GET)
	public String hadoopConfigurationsHadoopEcosystem(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "configurationsHadoopEcosystem");
		return "hadoop/monitoring_hadoop/configurations_hadoop_ecosystem";
	}
	
	/**
	 * Hadoop configurations hadoop ecosystem advanced settings.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/configurations/hadoopEcosystemAdvancedSettings", method = RequestMethod.GET)
	public String hadoopConfigurationsHadoopEcosystemAdvancedSettings(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "configurationsHadoopEcosystemAdvancedSettings");
		return "hadoop/monitoring_hadoop/configurations_hadoop_details_advancedSettings";
	}
	
	/**
	 * Node os details.
	 *
	 * @param model the model
	 * @param nodeId the node id
	 * @return the string
	 */
	@RequestMapping(value = "/nodeOsDetails/{nodeId}", method = RequestMethod.GET)
	public String nodeOsDetails(ModelMap model, @PathVariable String nodeId) {
		logger.info("Inside Node Os Details ");
		model.addAttribute("title", "Node Os Details");
		model.addAttribute("nodeId", nodeId);
		return "hadoop/monitoring_hadoop/nodeOsDetails";
	}
	
	/**
	 * Node swap details.
	 *
	 * @param model the model
	 * @param nodeId the node id
	 * @return the string
	 */
	@RequestMapping(value = "/nodeSwapDetails/{nodeId}", method = RequestMethod.GET)
	public String nodeSwapDetails(ModelMap model, @PathVariable String nodeId) {
		logger.info("Inside Node Swap Details ");
		model.addAttribute("title", "Node Swap Details");
		model.addAttribute("nodeId", nodeId);
		return "hadoop/monitoring_hadoop/nodeSwapDetails";
	}
	
	/**
	 * Hadoop configurations auto provision.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/configurations/autoProvision/{clusterId}", method = RequestMethod.GET)
	public String hadoopConfigurationsAutoProvision(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "configurationsAutoProvision");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/configurations_autoProvision";
	}
	
	/**
	 * Hadoop configurations parameters.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/configurations/parameters/{clusterId}", method = RequestMethod.GET)
	public String hadoopConfigurationsParameters(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "configurationsAutoProvision");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/hadoopParameters";
	}
	
	/**
	 * Hadoop configurations alerts.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/configurations/alerts/{clusterId}", method = RequestMethod.GET)
	public String hadoopConfigurationsAlerts(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "configurationsAlerts");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/configurations_alerts";
	}

	/**
	 * Hadoop add nodes.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/addNodes/{clusterId}", method = RequestMethod.GET)
	public String hadoopAddNodes(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "addNodes");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/hadoopAddNodes";
	}
	
	/**
	 * Hadoop audit trail.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/auditTrail/{clusterId}", method = RequestMethod.GET)
	public String hadoopAuditTrail(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "auditTrail");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/hadoopAuditTrail";
	}
	
	/**
	 * Hadoop audit trail detail.
	 *
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping(value = "/auditTrailDetail", method = RequestMethod.GET)
	public String hadoopAuditTrailDetail(ModelMap model, HttpServletRequest request) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "auditTrailDetail");
		Set<String> names = request.getParameterMap().keySet();
		for(String name : names) {
			model.addAttribute(name, request.getParameter(name));
		}
		String dateVal = request.getParameter("date");
		
		Date date = new Date(ParserUtil.getLongValue(dateVal, 0));
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		model.addAttribute("date", format.format(date));
		return "hadoop/monitoring_hadoop/auditTrailChild";
	}
	
	/**
	 * Hadoop events.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/events/{clusterId}", method = RequestMethod.GET)
	public String hadoopEvents(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "events");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/hadoopEvents";
	}

	/**
	 * Hadoop logs.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/logs/{clusterId}", method = RequestMethod.GET)
	public String hadoopLogs(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "logs");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/hadoopLogs";
	}
	
	/**
	 * Hadoop node drill down.
	 *
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping(value = "/nodeDrillDown", method = RequestMethod.GET)
	public String hadoopNodeDrillDown(ModelMap model, HttpServletRequest request) {
		String clusterId = request.getParameter("clusterId");
		String nodeId = request.getParameter("nodeId");
		String nodeIp = request.getParameter("nodeIp");
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "nodeDrillDown");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("nodeId", nodeId);
		model.addAttribute("nodeIp", nodeIp);
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/hadoopNodeDrillDown";
	}
	
	/**
	 * Hadoop add node setup details.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/hAddNodeSetupDetail", method = RequestMethod.GET)
	public String hadoopAddNodeSetupDetails(ModelMap model) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "addNodeSetUPDetail");
		return "hadoop/monitoring_hadoop/hAddNodeSetupDetail";
	}
	
	/**
	 * Hadoop component details view.
	 *
	 * @param model the model
	 * @param componentName the component name
	 * @return the string
	 */
	@RequestMapping(value = "/componentDetailsView/{componentName}", method = RequestMethod.GET)
	public String hadoopComponentDetailsView(ModelMap model,@PathVariable("componentName") String componentName) {
		logger.info("Inside hadoopCommands hadoop view");
		model.addAttribute("title", "nodeDrillDown");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("componentName",componentName);
		return "hadoop/monitoring_hadoop/componentDetailsView";
	}
	
	/**
	 * Node add progress.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/addNodesProgress/{clusterId}", method = RequestMethod.GET)
	public String nodeAddProgress(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside nodeAddProgress view");
		model.addAttribute("title", "nodeAddProgress");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/addNodesProgress";
	}
	
	/**
	 * Nodes detail.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/nodesDetail", method = RequestMethod.GET)
	public String nodesDetail(ModelMap model) {
		logger.info("Inside Nodes Details ");
		model.addAttribute("title", "Nodes");
		return "hadoop/monitoring_hadoop/nodesDetail";
	}
	
	/**
	 * Jobscheduler.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/jobscheduler/{clusterId}", method = RequestMethod.GET)
	public String jobscheduler(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside Nodes Details ");
		model.addAttribute("title", "Nodes");
		model.addAttribute("clusterId", clusterId);
		return "hadoop/monitoring_hadoop/jobSchedulers";
	}
	
	/**
	 * Capacity queue child.
	 *
	 * @param model the model
	 * @param newQueueId the new queue id
	 * @return the string
	 */
	@RequestMapping(value = "/capacityQueueChild/{newQueueId}", method = RequestMethod.GET)
	public String capacityQueueChild(ModelMap model,@PathVariable("newQueueId") String newQueueId) {
		logger.info("Inside Nodes Details ");
		model.addAttribute("title", "Nodes");
		model.addAttribute("newQueueId",newQueueId);
		return "hadoop/monitoring_hadoop/capacityQueueChild";
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
