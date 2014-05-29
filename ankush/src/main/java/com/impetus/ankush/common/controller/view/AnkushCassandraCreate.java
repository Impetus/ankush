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
@RequestMapping("/cassandra-cluster")
public class AnkushCassandraCreate extends AbstractController {
	private static final Logger logger = LoggerFactory
	.getLogger(AnkushCassandraCreate.class);
	
	private ArrayList<String> cssFiles;
	private ArrayList<String> jsFiles;
	public AnkushCassandraCreate() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();	
		jsFiles = new ArrayList<String>();
		jsFiles.add("cassandraClusterCreation");
		jsFiles.add("cassandra/cassandraSetupDetail");
		jsFiles.add("ankush.validation");
		jsFiles.add("ankush.common");
		jsFiles.add("ErrorMessage");
		jsFiles.add("tooltip/cassandraClusterCreationTooltip");
		jsFiles.add("tooltip/commonClusterCreationTooltip");
		
	}
	/**
	 * home view to render by returning its name.
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String cassandraCreate(ModelMap model) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "Home");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		
		//set path for the jsp page
		return "cassandra/cassandraClusterCreation/cassandraClusterCreate";
	}
	
	/*Setup detail view to renderby returning its name*/
	@RequestMapping(value = "/setupDetail/{clusterId}", method = RequestMethod.GET)
	public String setupDetail(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside setupDetail view");
		model.addAttribute("title", "setupDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("clusterId", clusterId);
		return "cassandra/cassandraClusterCreation/cassandraClusterCreate";
	} 
	
	/*Retrieved node detail view to renderby returning its name*/
	@RequestMapping(value = "/retrievedNodeDetail", method = RequestMethod.GET)
	public String cassandraNodeDetail(ModelMap model) {
		logger.info("Inside node details view");
		model.addAttribute("title", "oNodeDetail");
		return "cassandra/cassandraClusterCreation/retrievedNodeDetail";
	}
	@RequestMapping(value = "/clusterConf/{clusterId}", method = RequestMethod.GET)
	public String clusterConf(ModelMap model,@PathVariable("clusterId") String clusterId) {
		logger.info("Inside  clusterConf view");
		model.addAttribute("title", "clusterConf");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("clusterId", clusterId);
		return "cassandra/cassandraClusterCreation/cassandraClusterCreate";
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
