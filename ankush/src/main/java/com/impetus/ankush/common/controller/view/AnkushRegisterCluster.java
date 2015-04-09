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

@Controller
@RequestMapping("/register-cluster")
public class AnkushRegisterCluster extends AbstractController {
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushRegisterCluster.class);

	private ArrayList<String> cssFiles;
	private ArrayList<String> jsFiles;

	public AnkushRegisterCluster() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();
	}

	@RequestMapping(value = "/hadoopConfig/{technology}", method = RequestMethod.GET)
	public String hadoopConfig(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside hadoopConfig view");
		model.addAttribute("title", "hadoopConfig");
		model.addAttribute("technology", technology);
		return "registerCluster/hadoopConfig";
	}

	@RequestMapping(value = "/hadoopNodes/{technology}", method = RequestMethod.GET)
	public String hadoopNodes(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside hadoopNodes view");
		model.addAttribute("title", "hadoopNodes");
		model.addAttribute("technology", technology);
		return "hybridCluster/hybridClusterCreation/hadoopNodes";
	}

	@RequestMapping(value = "/zookeeperConfig/{technology}", method = RequestMethod.GET)
	public String zookeeperConfig(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside zookeeperConfig view");
		model.addAttribute("technology", technology);
		model.addAttribute("title", "zookeeperConfig");
		return "registerCluster/zookeeperConfig";
	}

	@RequestMapping(value = "/zookeeperNodes/{technology}", method = RequestMethod.GET)
	public String zookeeperNodes(ModelMap model,
			@PathVariable("technology") String technology) {
		logger.info("Inside zookeeperNodes view");
		model.addAttribute("title", "zookeeperNodes");
		model.addAttribute("technology", technology);
		return "hybridCluster/hybridClusterCreation/zookeeperNodes";
	}

	@RequestMapping(value = "/gangliaConfig/{technology}", method = RequestMethod.GET)
	public String gangliaConfig(ModelMap model) {
		logger.info("Inside gangliaConfig view");
		model.addAttribute("title", "gangliaConfig");
		return "registerCluster/gangliaConfig";
	}

	@RequestMapping(value = "/gangliaNodes", method = RequestMethod.GET)
	public String gangliaNodes(ModelMap model) {
		logger.info("Inside gangliaNodes view");
		model.addAttribute("title", "gangliaNodes");
		return "registerCluster/gangliaNodes";
	}

	@RequestMapping(value = "/cassandraConfig", method = RequestMethod.GET)
	public String cassandraConfig(ModelMap model,
			@RequestParam("technology") String technology) {
		logger.info("Inside cassandraConfig view");
		model.addAttribute("title", "cassandraConfig");
		model.addAttribute("technology", technology);
		return "registerCluster/cassandraConfig";
	}

	@RequestMapping(value = "/cassandraNodes", method = RequestMethod.GET)
	public String cassandraNodes(ModelMap model,
			@RequestParam("technology") String technology) {
		logger.info("Inside cassandraNodes view");
		model.addAttribute("title", "cassandraNodes");
		model.addAttribute("technology", technology);
		return "registerCluster/cassandraNodes";
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
