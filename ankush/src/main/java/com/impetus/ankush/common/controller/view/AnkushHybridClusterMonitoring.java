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



@Controller
@RequestMapping("/hybrid-monitoring")
public class AnkushHybridClusterMonitoring extends AbstractController {
	private static final Logger logger = LoggerFactory
	.getLogger(AnkushHybridClusterMonitoring.class);
	
	private ArrayList<String> cssFiles;
	private ArrayList<String> jsFiles;
	public AnkushHybridClusterMonitoring() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();	
		jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.common");
		jsFiles.add("hybrid/hybridSetupDetail");
		jsFiles.add("hybrid/hybridMonitoring/hybridMonitoring");
	}
	


	

	@RequestMapping(value = "/{clusterName}/{hybridTechnology}/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String commonMonitoring(@PathVariable("clusterId") String clusterId,@PathVariable("clusterName") String clusterName,@PathVariable("clusterTechnology") String clusterTechnology,@PathVariable("hybridTechnology") String hybridTechnology,ModelMap model) {
		logger.info("Inside Node Utilization Trend Detailed Graph view");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("hybridTechnology", hybridTechnology);
		model.addAttribute("page", "technologyMonitoring");
		String returnjsp = "hybridCluster/hybridMonitoring/monitoring"+hybridTechnology;
		if(hybridTechnology.contains("Zookeeper")){
			returnjsp = "hybridCluster/hybridMonitoring/monitoringZookeeper";
		}
		return returnjsp;
	}
	@RequestMapping(value = "/{clusterName}/addNode/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String hybridAddNode(ModelMap model,@PathVariable("clusterId") String clusterId,@PathVariable("clusterName") String clusterName,@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside Hybrid Add node view");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("clusterName", clusterName);
		return "hybridCluster/hybridMonitoring/hybridAddNode";
	}
	@RequestMapping(value = "/hybridAddNode/{technology}", method = RequestMethod.GET)
	public String hybridAddNode(@PathVariable("technology") String technology,ModelMap model) {
		logger.info("Inside cluster create view");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "hybridCluster/hybridMonitoring/addNode/"+technology;
	}
	@RequestMapping(value = "/{clusterName}/{hybridTechnology}/runCommand/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String runCommand(ModelMap model,@PathVariable("clusterId") String clusterId,@PathVariable("clusterName") String clusterName,@PathVariable("hybridTechnology") String hybridTechnology,@PathVariable("clusterTechnology") String clusterTechnology) {
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("hybridTechnology", hybridTechnology);
		model.addAttribute("page", "zookeeperRunCommand");
		return "hybridCluster/hybridMonitoring/runCommandZookeeper";
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

