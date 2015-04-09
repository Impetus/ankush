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

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush2.constant.Constant.Component;

/**
 * @author anzar.abbas
 * 
 */
@Controller
@RequestMapping("/common-cluster")
public class AnkushCommonController extends AbstractController {
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushCommonController.class);

	private ArrayList<String> cssFiles;
	private ArrayList<String> jsFiles;

	public AnkushCommonController() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();

	}

	/**
	 * return deployment logs view of a cluster
	 * 
	 * @param model
	 * @param clusterId
	 * @param clusterTechnology
	 * @return
	 */
	@RequestMapping(value = "/{clusterName}/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String deploymentProgress(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside deploymentProgress view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		jsFiles.removeAll(jsFiles);
		jsFiles.add("ankush");
		jsFiles.add("ankush.common");
		jsFiles.add("ankush.constants");
		jsFiles.add("ErrorMessage");
		jsFiles.add("tooltip/commonClusterCreationTooltip");
		jsFiles.add("ankush.validation");
		model.addAttribute("jsFiles", jsFiles);
		return "common/deploymentLogs";
	}

	/**
	 * return node status table view
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{clusterName}/deploymentLogs/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String deploymentLogs(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside deploymentLogs view");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.common");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("title", "oNodeDetail");
		return "common/nodeStatus";
	}

	/**
	 * return node level logs view
	 * 
	 * @param model
	 * @param clusterId
	 * @return
	 */
	@RequestMapping(value = "/{clusterName}/nodeDeploymentLog/C-D/{clusterId}/{clusterTechnology}/{hostName:.+}", method = RequestMethod.GET)
	public String nodeStatusNode(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("hostName") String hostName) {
		logger.info("Inside nodeStatusNode view");
		jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.common");
		model.addAttribute("title", "nodeStatusNode");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("hostName", hostName);
		model.addAttribute("jsFiles", jsFiles);
		return "common/nodeDeploymentLogs";
	}

	@RequestMapping(value = "/{clusterName}/C-D/deleteCluster/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String deleteCluster(ModelMap model,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside deleteCluster view");
		model.addAttribute("clusterId", clusterId);
		jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.common");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("title", "deleteCluster");
		return "common/deleteClusterLog";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
