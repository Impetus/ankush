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
@RequestMapping("/kafkaMonitoring")
public class AnkushKafkaMonitoringController  extends AbstractController {
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushHadoopClusterMonitoring.class);
	/** Constructor. */
	public AnkushKafkaMonitoringController() {
		// TODO Auto-generated constructor stub
	}
	/** this will show topics detail table in topic drill down. */
	@RequestMapping(value = "/topicDrillDown", method = RequestMethod.GET)
	public String topicDrillDown(ModelMap model,@RequestParam("topicIndex") int topicIndex) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside common monitoring view");
		jsFiles.add("kafka/kafkaMonitoring");
		model.addAttribute("topicIndex", topicIndex);
		return "kafka/kafkaMonitoring/topicDrillDown";
	}
	/** this will show topics detail table in topic drill down. */
	@RequestMapping(value = "/brokerDrillDown", method = RequestMethod.GET)
	public String brokerDrillDown(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside common monitoring view");
		jsFiles.add("kafka/kafkaMonitoring");
		return "kafka/kafkaMonitoring/brokerDrillDown";
	}
	/** this will show topics detail table in topic drill down. */
	@RequestMapping(value = "/topicMoreDetailDrillDown", method = RequestMethod.GET)
	public String topicMoreDetailDrillDown(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside common monitoring view");
		jsFiles.add("kafka/kafkaMonitoring");
		return "kafka/kafkaMonitoring/topicMoreDetailDrillDown";
	}
	/** this will show topics detail table in topic drill down. */
	@RequestMapping(value = "/brokerBoxDetail", method = RequestMethod.GET)
	public String brokerBoxDetail(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside common monitoring view");
		jsFiles.add("kafka/kafkaMonitoring");
		return "kafka/kafkaMonitoring/brokerBoxDetail";
	}
	/** this will show topics detail table in topic drill down. */
	@RequestMapping(value = "/logBoxDetail", method = RequestMethod.GET)
	public String logBoxDetail(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside common monitoring view");
		jsFiles.add("kafka/kafkaMonitoring");
		return "kafka/kafkaMonitoring/logBoxDetail";
	}
	@RequestMapping(value = "/topicCreation", method = RequestMethod.GET)
	public String topicCreation(ModelMap model) {
		ArrayList<String>jsFiles = new ArrayList<String>();
		logger.info("Inside topic creation view");
		jsFiles.add("kafka/kafkaMonitoring");
		return "kafka/kafkaMonitoring/topicCreation";
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
