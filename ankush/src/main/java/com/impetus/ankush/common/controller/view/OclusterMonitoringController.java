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

/**
 * The Class OclusterMonitoringController.
 */
@Controller
@RequestMapping("/oClusterMonitoring")
public class OclusterMonitoringController extends AbstractController{
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(OclusterMonitoringController.class);
	
	/** The js files. */
	private ArrayList<String> jsFiles;
	
	/** The css files. */
	private ArrayList<String> cssFiles;
	
	/**
	 * Instantiates a new ocluster monitoring controller.
	 */
	public OclusterMonitoringController(){
		jsFiles = new ArrayList<String>();
		jsFiles.add("oracle/oClusterMonitoring");
		jsFiles.add("ankush.validation");
		jsFiles.add("ankush.common");
		jsFiles.add("oracle/oracleSetupDetail");
		cssFiles = new ArrayList<String>();
		cssFiles.add("ankush.oClusterParam");
	}
	
	/**
	 * O deployed cluster.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @param clusterName the cluster name
	 * @return the string
	 */
	@RequestMapping(value = "/oDeployedCluster", method = RequestMethod.GET)
	public String oDeployedCluster(ModelMap model,@RequestParam("clusterId") Long clusterId,@RequestParam("clusterName") String clusterName) {
		logger.info("Inside oDeployedCluster view");
		model.addAttribute("title", "oDeployedCluster");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		return "oClusterMonitoring/oClusterMonitoringHome";
	}
	
	/**
	 * Shard drill down.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/shardDrillDown", method = RequestMethod.GET)
	public String shardDrillDown(ModelMap model) {
		logger.info("Inside shardDrillDown view");
		model.addAttribute("title", "shardDrillDown");
		return "oClusterMonitoring/shardDrillDown";
	}
	
	/**
	 * Gets the rep node child page.
	 *
	 * @param repGpId the rep gp id
	 * @param repNodeId the rep node id
	 * @param model the model
	 * @return the rep node child page
	 */
	@RequestMapping(value="/repNodeChild/{repGpId}/{repNodeId}",method=RequestMethod.GET)
	public String getRepNodeChildPage(@PathVariable("repGpId") String repGpId,@PathVariable("repNodeId") String repNodeId,ModelMap model){
		model.addAttribute("repGpId", repGpId);
		model.addAttribute("repNodeId", repNodeId);
		return "oClusterMonitoring/repNodeChild";
	}
	
	/**
	 * Gets the rep node child parameter page.
	 *
	 * @param repGpId the rep gp id
	 * @param repNodeId the rep node id
	 * @param model the model
	 * @return the rep node child parameter page
	 */
	@RequestMapping(value="/repNodeChildParameter/{repGpId}/{repNodeId}",method=RequestMethod.GET)
	public String getRepNodeChildParameterPage(@PathVariable("repGpId") String repGpId,@PathVariable("repNodeId") String repNodeId,ModelMap model){
		model.addAttribute("repGpId", repGpId);
		model.addAttribute("repNodeId", repNodeId);
		return "oClusterMonitoring/repNodeChildParameter";
	}
	
	/**
	 * Gets the storage node child page.
	 *
	 * @param model the model
	 * @param storageNodeId the storage node id
	 * @param storageNodeIP the storage node ip
	 * @return the storage node child page
	 */
	@RequestMapping(value="/storageNodeChild",method=RequestMethod.POST)
	public String getStorageNodeChildPage(ModelMap model,@RequestParam("storageNodeId") String storageNodeId,@RequestParam("storageNodeIP") String storageNodeIP){
		model.addAttribute("storageNodeId", storageNodeId);
		model.addAttribute("storageNodeIP", storageNodeIP);
		return "oClusterMonitoring/storageNodeChild";
	}
	
	/**
	 * Gets the storage node child parameter page.
	 *
	 * @param storageNodeId the storage node id
	 * @param model the model
	 * @return the storage node child parameter page
	 */
	@RequestMapping(value="/storageNodeChildParameter/{storageNodeId}",method=RequestMethod.GET)
	public String getStorageNodeChildParameterPage(@PathVariable("storageNodeId") String storageNodeId,ModelMap model){
		model.addAttribute("storageNodeId", storageNodeId);
		return "oClusterMonitoring/storageNodeChildParameter";
	}
	
	/**
	 * Gets the increase rep factor log.
	 *
	 * @param model the model
	 * @return the increase rep factor log
	 */
	@RequestMapping(value="/increaseRepFactorLog",method=RequestMethod.GET)
	public String getIncreaseRepFactorLog(ModelMap model){
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "oClusterMonitoring/increaseRepFactorLog";
	}
	
	/**
	 * Adds the node child.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/addNodeChild", method = RequestMethod.GET)
	public String addNodeChild(ModelMap model) {
		logger.info("Inside addNodeChild view");
		model.addAttribute("title", "addNodeChild");
		return "oClusterMonitoring/addNodeChild";
	}
	
	/**
	 * Node add progress.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/nodeAddProgress", method = RequestMethod.GET)
	public String nodeAddProgress(ModelMap model) {
		logger.info("Inside nodeAddProgress view");
		model.addAttribute("title", "nodeAddProgress");
		return "oClusterMonitoring/nodeAddProgress";
	}
	
	/**
	 * Adds the node child progress.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/addNodeChildProgress", method = RequestMethod.GET)
	public String addNodeChildProgress(ModelMap model) {
		logger.info("Inside addNodeChildProgress view");
		model.addAttribute("title", "addNodeChildProgress");
		return "oClusterMonitoring/addNodeChildProgress";
	}
	
	
	/**
	 * Common action log page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/commonActionLogPage", method = RequestMethod.GET)
	public String commonActionLogPage(ModelMap model) {
		logger.info("Inside commonActionLogPage view");
		model.addAttribute("title", "commonActionLogPage");
		return "oClusterMonitoring/commonActionLogPage";
	}
	
	/**
	 * Gets the rebalance.
	 *
	 * @return the rebalance
	 */
	@RequestMapping(value="/rebalanceLogPage",method= RequestMethod.GET)
	public String getRebalance(){
		return "oClusterMonitoring/rebalanceLogPage";
	}
	
	/**
	 * Redistribute.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/redistributeLogPage", method = RequestMethod.GET)
	public String redistribute(ModelMap model) {
		logger.info("Inside redistribute view");
		model.addAttribute("title", "redistribute");
		return "oClusterMonitoring/redistributeLogPage";
	}
	
	/**
	 * Verify log page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/verifyLogPage", method = RequestMethod.GET)
	public String verifyLogPage(ModelMap model) {
		logger.info("Inside verifyLogPage view");
		model.addAttribute("title", "verifyLogPage");
		return "oClusterMonitoring/verifyLogPage";
	}
	
	/**
	 * Manage config.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/manageConfig", method = RequestMethod.GET)
	public String manageConfig(ModelMap model) {
		logger.info("Inside manageConfig view");
		model.addAttribute("title", "manageConfig");
		return "oClusterMonitoring/manageConfig";
	}
	@RequestMapping(value = "/storeConf/{clusterId}", method = RequestMethod.GET)
	public String clusterCreate(ModelMap model,@PathVariable("clusterId") String clusterId) {
	logger.info("Inside cluster create view");
	model.addAttribute("title", "setupDetail");
	model.addAttribute("cssFiles", cssFiles);
	model.addAttribute("clusterId", clusterId);
	return "oClusterCreation/oClusterCreate";
}
	/**
	 * O monitor add node.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/oMonitorAddNode", method = RequestMethod.GET)
	public String oMonitorAddNode(ModelMap model) {
		logger.info("Inside oMonitorAddNode view");
		model.addAttribute("title", "oMonitorAddNode");
		return "oClusterMonitoring/oClusterAddNode";
	}
	
	/**
	 * Store config.
	 *
	 * @param model the model
	 * @return the string
	 */
/*	@RequestMapping(value = "/storeConfig", method = RequestMethod.GET)
	public String storeConfig(ModelMap model) {
		logger.info("Inside storeConfig view");
		model.addAttribute("title", "storeConfig");
		return "oClusterMonitoring/storeConfig";
	} */
	@RequestMapping(value = "/setupDetail/{clusterId}", method = RequestMethod.GET)
	public String storeConfig(ModelMap model,@PathVariable("clusterId") String clusterId) {
	logger.info("Inside cluster create view");
	model.addAttribute("title", "setupDetail");
	model.addAttribute("cssFiles", cssFiles);
	model.addAttribute("clusterId", clusterId);
	return "oClusterCreation/oClusterCreate";
}
	/**
	 * Admin param.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/adminParam", method = RequestMethod.GET)
	public String adminParam(ModelMap model) {
		logger.info("Inside adminParam view");
		model.addAttribute("title", "adminParam");
		return "oClusterMonitoring/adminParam";
	}
	
	/**
	 * Policy param.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/policyParam", method = RequestMethod.GET)
	public String policyParam(ModelMap model) {
		logger.info("Inside policyParam view");
		model.addAttribute("title", "policyParam");
		return "oClusterMonitoring/policyParam";
	}
	
	/**
	 * Rep node param.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/repNodeParam", method = RequestMethod.GET)
	public String repNodeParam(ModelMap model) {
		logger.info("Inside repNodeParam view");
		model.addAttribute("title", "repNodeParam");
		return "oClusterMonitoring/repNodeParam";
	}
	
	/**
	 * Alert page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/alertPage", method = RequestMethod.GET)
	public String alertPage(ModelMap model) {
		logger.info("Inside alertPage view");
		model.addAttribute("title", "alertPage");
		return "oClusterMonitoring/alertPage";
	}
	
	/**
	 * Plan history.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/planHistory", method = RequestMethod.GET)
	public String planHistory(ModelMap model) {
		logger.info("Inside planHistory view");
		model.addAttribute("title", "planHistory");
		return "oClusterMonitoring/planHistory";
	}
	
	/**
	 * Plan history child.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/planHistoryChild", method = RequestMethod.GET)
	public String planHistoryChild(ModelMap model) {
		logger.info("Inside planHistoryChild view");
		model.addAttribute("title", "planHistoryChild");
		return "oClusterMonitoring/planHistoryChild";
	}
	
	/**
	 * Event page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/eventPage", method = RequestMethod.GET)
	public String eventPage(ModelMap model) {
		logger.info("Inside eventPage view");
		model.addAttribute("title", "eventPage");
		return "oClusterMonitoring/eventPage";
	}
	
	/**
	 * Event child.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/eventChildPage", method = RequestMethod.GET)
	public String eventChild(ModelMap model) {
		logger.info("Inside eventChildPage view");
		model.addAttribute("title", "eventChild");
		return "oClusterMonitoring/eventChildPage";
	}
	
	/**
	 * Store event page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/storeEventPage", method = RequestMethod.GET)
	public String storeEventPage(ModelMap model) {
		logger.info("Inside storeEventPage view");
		model.addAttribute("title", "storeEventPage");
		return "oClusterMonitoring/storeEventPage";
	}
	
	/**
	 * Audit taril.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/auditTaril", method = RequestMethod.GET)
	public String auditTaril(ModelMap model) {
		logger.info("Inside auditTaril view");
		model.addAttribute("title", "auditTaril");
		return "oClusterMonitoring/auditTrail";
	}
	
	/**
	 * Audit taril child.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/auditTaril/auditTarilChild", method = RequestMethod.GET)
	public String auditTarilChild(ModelMap model) {
		logger.info("Inside auditTaril view");
		model.addAttribute("title", "auditTarilChild");
		return "oClusterMonitoring/auditTrailChild";
	}
	
	/**
	 * Logs page.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/logPage", method = RequestMethod.GET)
	public String logsPage(ModelMap model) {
		logger.info("Inside logPage view");
		model.addAttribute("title", "logsPage");
		return "oClusterMonitoring/logPage";
	}
	@RequestMapping(value = "/utilizationTrend", method = RequestMethod.GET)
	public String utilizationGraph(ModelMap model) {
		logger.info("Inside utilization view");
		model.addAttribute("title", "utilization trend");
		return "oClusterMonitoring/utilizationTrend";
	}
	/**Node Utilization Trend graphs details. */
	@RequestMapping(value = "/nodeUtilizationTrend", method = RequestMethod.GET)
	public String nodeUtilizationTrend(ModelMap model,@RequestParam("clusterTechnology") String clusterTechnology) {
		logger.info("Inside Node Utilization Trend Detailed Graph view");
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "commonMonitoringPages/nodeUtilizationTrends";
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
