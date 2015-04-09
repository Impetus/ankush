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
import javax.servlet.http.HttpSession;

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
	@RequestMapping(value = "/{clusterName}/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String common(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			HttpServletRequest request) {
		ArrayList<String> jsFiles = new ArrayList<String>();
		logger.info("Inside common monitoring view");
		jsFiles.removeAll(jsFiles);
		jsFiles.add("commonMonitoring");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("page", "monitoringPage");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "commonMonitoringPages/monitoringPage";
	}

	/** Audit trail page. */
	@RequestMapping(value = "/{clusterName}/auditTrails/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String auditTrail(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside Audit Trail view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/auditTrails");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "commonMonitoringPages/auditTrails";
	}
	@RequestMapping(value = "/{clusterName}/deploymentLogs/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String deploymentLogs(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside deploymentLogs view");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.common");
		model.addAttribute("jsFiles", jsFiles);
		return "common/nodeStatus";
	}
	/** Audit trail child page. */
	@RequestMapping(value = "/auditTrailDetails", method = RequestMethod.GET)
	public String auditTrailDetail(ModelMap model) {
		logger.info("Inside Audit Trails Details view");
		return "commonMonitoringPages/auditTrailDetails";
	}

	/** Events page. */
	@RequestMapping(value = "/{clusterName}/events/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String events(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside Events view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/events");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "commonMonitoringPages/events";
	}
	/** Operations page. */
	@RequestMapping(value = "/{clusterName}/operations/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String operations(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside operations view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/operations");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("page", "operations");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "commonMonitoringPages/operations";
	}
	/** Operations page. */
	@RequestMapping(value = "/{clusterName}/operations/{operationId}/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String operationLogs(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("operationId") String operationId) {
		logger.info("Inside operations view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.common");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("page", "operations");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("operationId", operationId);
		return "commonMonitoringPages/operationProgress";
	}
/** parameter page. */
	@RequestMapping(value = "/{clusterName}/{hybridTechnology}/parameters/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String parametersHybrid(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("hybridTechnology") String hybridTechnology) {
		logger.info("Inside Events view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/configurationParam");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("hybridTechnology", hybridTechnology);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("page", "hybridParameters");
		return "commonMonitoringPages/parameters";
	}


	/** Logs page. */
	@RequestMapping(value = "/{clusterName}/{hybridTechnology}/logs/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String logsHybrid(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("hybridTechnology") String hybridTechnology) {
		logger.info("Inside Logs page view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/logs");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("hybridTechnology", hybridTechnology);
		model.addAttribute("page", "hybridLogs");
		return "commonMonitoringPages/logs";
	}

	@RequestMapping(value = "/logAnalysis", method = RequestMethod.GET)
	public String logAnalysis(ModelMap model) {
		logger.info("Inside Log Analysis view");
		return "commonMonitoringPages/logAnalysis";
	}

	/** Nodes page. */
	@RequestMapping(value = "/{clusterName}/nodes/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String nodes(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside Nodes view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/nodes");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("page", "nodeList");
		return "commonMonitoringPages/nodes";
	}

	/** Nodes child page or heatmap child page. */
	@RequestMapping(value = "/{clusterName}/nodeDetails/C-D/{clusterId}/{clusterTechnology}/{hostName:.+}", method = RequestMethod.GET)
	public String nodeDrillDown(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("hostName") String hostName) {
		logger.info("Inside nodeDrillDown view");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("hostName", hostName);
		model.addAttribute("page", "nodeDrillDown");
		return "commonMonitoringPages/nodeDrillDown";
	}

	/** Configuration page. */
	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	public String configuration(ModelMap model) {
		logger.info("Inside configuration view");
		return "commonMonitoringPages/configuration";
	}

	/** Alert page. */
	@RequestMapping(value = "/{clusterName}/alertsAndHighAvailability/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String alert(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside alert view");
		ArrayList<String> jsFiles = new ArrayList<String>();
		jsFiles.add("commonMonitoring/alerts");
		jsFiles.add("ankush.validation");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("clusterName", clusterName);
		return "commonMonitoringPages/alert";
	}

	/** Utilization Trend graphs details. */
	@RequestMapping(value = "/{clusterName}/utilizationTrend/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String utilizationTrend(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside Utilization Trend Detailed Graph view");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("clusterName", clusterName);
		return "commonMonitoringPages/clusterUtilization";
	}

	@RequestMapping(value = "/{clusterName}/nodeDetails/nodeUtilizationTrend/C-D/{clusterId}/{clusterTechnology}/{hostName:.+}", method = RequestMethod.GET)
	public String nodeUtilizationTrend(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("hostName") String hostName){
		logger.info("Inside Node Utilization Trend Detailed Graph view");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("hostName", hostName);
		model.addAttribute("page", "nodeUtilizationTrend");
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
	@RequestMapping(value = "/retrievedNodeDetail/{nodeIp:.+}", method = RequestMethod.GET)
	public String nodeDetail(ModelMap model,
			@PathVariable("nodeIp") String nodeIp) {
		logger.info("Inside node details view");
		model.addAttribute("title", "nodeDetail");
		model.addAttribute("nodeIp", nodeIp);
		return "hybridCluster/hybridClusterCreation/retrievedNodeDetail";
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
