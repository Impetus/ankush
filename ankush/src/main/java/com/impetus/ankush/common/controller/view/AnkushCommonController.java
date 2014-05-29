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
	 * @param model
	 * @param clusterId
	 * @param clusterTechnology
	 * @return
	 */
	@RequestMapping(value = "/deploymentProgress/{clusterId}", method = RequestMethod.GET)
	public String deploymentProgress(ModelMap model,@PathVariable("clusterId") String clusterId,@RequestParam("clusterTechnology") String clusterTechnology) {
		logger.info("Inside deploymentProgress view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("clusterTechnology", clusterTechnology);
		System.out.println(clusterTechnology);
		jsFiles.removeAll(jsFiles);
		jsFiles.add("ankush.common");
		jsFiles.add("ErrorMessage");
		jsFiles.add("ankush.validation");
		jsFiles.add("tooltip/commonClusterCreationTooltip");
		if(clusterTechnology.equalsIgnoreCase("oracle nosql database")){
			jsFiles.add("oracle/oClusterSetup");
			jsFiles.add("oracle/oracleSetupDetail");
		}else if(clusterTechnology.equalsIgnoreCase("storm")){
			jsFiles.add("stormClusterCreation");
			jsFiles.add("storm/stormSetupDetail");
		}else if(clusterTechnology.equalsIgnoreCase("cassandra")){
			jsFiles.add("cassandraClusterCreation");
			jsFiles.add("cassandra/cassandraSetupDetail");
		}else if(clusterTechnology.equalsIgnoreCase("kafka")){
			jsFiles.add("kafka/kafkaClusterCreation");
			jsFiles.add("kafka/kafkaSetupDetail");
		}else if(clusterTechnology.equalsIgnoreCase("elasticSearch")){
			jsFiles.add("ElasticSearch/elasticSearchClusterCreation");
			jsFiles.add("ElasticSearch/elasticSearchSetupDetail");
		}
		
		else if(clusterTechnology.equalsIgnoreCase(Constant.Technology.HADOOP)
				|| clusterTechnology.equalsIgnoreCase(Constant.Component.Name.HADOOP2)) {
			jsFiles.add("hadoop/ankush.hadoop-cluster");
			jsFiles.add("ankush.validation");
		}
		else if(clusterTechnology.equalsIgnoreCase("hybrid")) {
			jsFiles.add("hybrid/hybridClusterCreation");
			jsFiles.add("hybrid/hybridSetupDetail");
			jsFiles.add("hybrid/hybrid_Zookeeper");
			jsFiles.add("hybrid/hybrid_Kafka");
			jsFiles.add("hybrid/hybrid_Storm");
			jsFiles.add("hybrid/hybrid_Oracle");
		//	jsFiles.add("hybrid/hybrid_Vajra");
			jsFiles.add("hybrid/hybrid_Bda");
			jsFiles.add("hybrid/hybrid_ElasticSearch");
			jsFiles.add("hybrid/hybrid_Cassandra");
			jsFiles.add("hybrid/hybrid_Greenplum");
			jsFiles.add("hybrid/hybrid_RabbitMQ");
			jsFiles.add("hybrid/hybrid_Hadoop");
			jsFiles.add("hybrid/hybrid_Hbase");
		}
		model.addAttribute("jsFiles", jsFiles);
		return "common/deploymentLogs";
	}
	/**
	 * return node status table view
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/nodeStatus", method = RequestMethod.GET)
	public String nodeStatus(ModelMap model) {
		logger.info("Inside nodeStatus view");
		model.addAttribute("title", "oNodeDetail");
		return "common/nodeStatus";
	}
	/**
	 * return node level logs view
	 * @param model
	 * @param clusterId
	 * @return
	 */
	@RequestMapping(value = "/nodeStatusNode/{clusterId}/details", method = RequestMethod.GET)
	public String nodeStatusNode(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside nodeStatusNode view");
		model.addAttribute("title", "nodeStatusNode");
		model.addAttribute("clusterId", clusterId);
		return "common/nodeDeploymentLogs";
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
