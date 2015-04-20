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
@RequestMapping("/hybrid-cluster")
public class AnkushHybridClusterCreation extends AbstractController {
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushHybridClusterCreation.class);

	private ArrayList<String> cssFiles;
	private ArrayList<String> jsFiles;

	public AnkushHybridClusterCreation() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.common");
		jsFiles.add("ankush.validation");
		jsFiles.add("register/registerCluster");
		jsFiles.add("tooltip/commonClusterCreationTooltip");
		jsFiles.add("hybrid/hybrid_Zookeeper");
		jsFiles.add("hybrid/hybrid_Cassandra");
		jsFiles.add("hybrid/hybrid_Hadoop");
		jsFiles.add("hybrid/hybrid_Ganglia");
		jsFiles.add("register/register_Zookeeper");
		jsFiles.add("register/register_Hadoop");
		jsFiles.add("register/register_Ganglia");
	}

	@RequestMapping(value = "/clusterCreation/C-D/", method = RequestMethod.GET)
	public String clusterCreate(ModelMap model) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "hybridCluster/hybridClusterCreation/hybridClusterCreate";
	}

	@RequestMapping(value = "/clusterCreation/C-D/{templateName}", method = RequestMethod.GET)
	public String templateLoad(ModelMap model,
			@PathVariable("templateName") String templateName) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "templateLoad");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("templateName", templateName);
		return "hybridCluster/hybridClusterCreation/hybridClusterCreate";
	}

	@RequestMapping(value = "/clusterCreation/C-D/{clusterTechnology}/{templateName}", method = RequestMethod.GET)
	public String templateLoadTechnology(ModelMap model,
			@PathVariable("clusterTechnology") String clusterTechnology,
			@PathVariable("templateName") String templateName) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "templateLoad");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterTechnology", clusterTechnology);
		model.addAttribute("templateName", templateName);
		return "hybridCluster/hybridClusterCreation/hybridClusterCreate";
	}

	@RequestMapping(value = "/clusterCreate/C-D/{clusterTechnology}", method = RequestMethod.GET)
	public String clusterCreate(ModelMap model,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "templateLoad");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "hybridCluster/hybridClusterCreation/hybridClusterCreate";
	}

	@RequestMapping(value = "/home/{clusterId}", method = RequestMethod.GET)
	public String clusterError(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		return "hybridCluster/hybridClusterCreation/hybridClusterCreate";
	}

	@RequestMapping(value = "/zookeeperConfig/{technology}", method = RequestMethod.GET)
	public String zookeeperConfig(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside zookeeperConfig view");
		model.addAttribute("title", "zookeeperConfig");
		model.addAttribute("technology", technology);
		return "hybridCluster/hybridClusterCreation/zookeeperConfig";
	}

	@RequestMapping(value = "/cassandraConfig/{technology}", method = RequestMethod.GET)
	public String cassandraConfig(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside cassandraConfig view");
		model.addAttribute("title", "cassandraConfig");
		return "hybridCluster/hybridClusterCreation/cassandraConfig";
	}

	@RequestMapping(value = "/hadoopConfig/{technology}", method = RequestMethod.GET)
	public String hadoopConfig(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside hadoopConfig view");
		model.addAttribute("title", "hadoopConfig");
		model.addAttribute("technology", technology);
		return "hybridCluster/hybridClusterCreation/hadoopConfig";
	}

	@RequestMapping(value = "/gangliaConfig/{technology}", method = RequestMethod.GET)
	public String gangliaConfig(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside gangliaConfig view");
		model.addAttribute("title", "gangliaConfig");
		model.addAttribute("technology", technology);
		return "hybridCluster/hybridClusterCreation/gangliaConfig";
	}

	@RequestMapping(value = "/cassandraNodes/{technology}", method = RequestMethod.GET)
	public String cassandraNodes(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside cassandraNodes view");
		model.addAttribute("title", "cassandraNodes");
		return "hybridCluster/hybridClusterCreation/cassandraNodes";
	}

	@RequestMapping(value = "/zookeeperNodes/{technology}", method = RequestMethod.GET)
	public String zookeeperNodes(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside zookeeperNodes view");
		model.addAttribute("title", "zookeeperNodes");
		model.addAttribute("technology", technology);
		return "hybridCluster/hybridClusterCreation/zookeeperNodes";
	}

	@RequestMapping(value = "/hadoopNodes/{technology}", method = RequestMethod.GET)
	public String hadoopNodes(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside hadoopNodes view");
		model.addAttribute("title", "hadoopNodes");
		model.addAttribute("technology", technology);
		return "hybridCluster/hybridClusterCreation/hadoopNodes";
	}

	@RequestMapping(value = "/gangliaNodes/{technology}", method = RequestMethod.GET)
	public String gangliaNodes(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside gangliaNodes view");
		model.addAttribute("technology", technology);
		model.addAttribute("title", "gangliaNodes");
		return "registerCluster/gangliaNodes";
	}

	@RequestMapping(value = "/{clusterName}/setupDetail/C-D/{clusterId}/{clusterTechnology}", method = RequestMethod.GET)
	public String setupDetail(ModelMap model,
			@PathVariable("clusterId") String clusterId,
			@PathVariable("clusterName") String clusterName,
			@PathVariable("clusterTechnology") String clusterTechnology) {
		logger.info("Inside setupDetail view");
		model.addAttribute("title", "setupDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterName", clusterName);
		model.addAttribute("clusterTechnology", clusterTechnology);
		return "hybridCluster/hybridClusterCreation/hybridClusterCreate";
	}

	@RequestMapping(value = "/retrievedNodeDetail/{nodeIp:.+}", method = RequestMethod.GET)
	public String nodeDetail(ModelMap model,
			@PathVariable("nodeIp") String nodeIp) {
		logger.info("Inside node details view");
		model.addAttribute("title", "nodeDetail");
		model.addAttribute("nodeIp", nodeIp);
		return "hybridCluster/hybridClusterCreation/retrievedNodeDetail";
	}

	@RequestMapping(value = "/nodeMapNodeDetail", method = RequestMethod.GET)
	public String nodeMapNodeDetail(ModelMap model) {
		logger.info("Inside node details view");
		model.addAttribute("title", "nodeDetail");
		return "hybridCluster/hybridClusterCreation/nodeMapNodeDetail";
	}

	@RequestMapping(value = "/clusterConf/{clusterId}", method = RequestMethod.GET)
	public String clusterConf(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside  clusterConf view");
		model.addAttribute("title", "clusterConf");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		return "hybridCluster/hybridClusterCreation/hybridClusterCreate";
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}