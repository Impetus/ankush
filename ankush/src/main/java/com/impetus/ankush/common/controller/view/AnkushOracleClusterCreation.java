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

/**
 * The Class AnkushOracleClusterCreation.
 */
@Controller
@RequestMapping("/oracle-cluster")
public class AnkushOracleClusterCreation extends AbstractController {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushOracleClusterCreation.class);

	/** The css files. */
	private ArrayList<String> cssFiles;
	
	/** The js files. */
	private ArrayList<String> jsFiles;

	/**
	 * Instantiates a new ankush oracle cluster creation.
	 */
	public AnkushOracleClusterCreation() {
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();
		jsFiles.add("ankush.validation");
		jsFiles.add("ankush.common");
		jsFiles.add("oracle/oClusterSetup");
		jsFiles.add("ErrorMessage");
		jsFiles.add("oracle/oracleSetupDetail");
	}

	/**
	 * home view to render by returning its name.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String oClusterCreate(ModelMap model) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "oClusterCreation/oClusterCreate";
	}

	/**
	 * O cluster create.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	@RequestMapping(value = "/home/{clusterId}", method = RequestMethod.GET)
	public String oClusterCreate(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside cluster create view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		return "oClusterCreation/oClusterCreate";
	}

	/**
	 * O node detail.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/oNodeDetail", method = RequestMethod.GET)
	public String oNodeDetail(ModelMap model) {
		logger.info("Inside node details view");
		model.addAttribute("title", "oNodeDetail");
		return "oClusterCreation/oNodeDetail";
	}

	/**
	 * O node detail error.
	 *
	 * @param model the model
	 * @return the string
	 */
	/*@RequestMapping(value = "/oNodeDetailError", method = RequestMethod.GET)
	public String oNodeDetailError(ModelMap model) {
		logger.info("Inside node oNodeDetailError view");
		model.addAttribute("title", "oNodeDetailError");
		return "oClusterCreation/oNodeDetailError";
	}*/

	/**
	 * O deployment progress.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	/*@RequestMapping(value = "/oDeploymentProgress/{clusterId}", method = RequestMethod.GET)
	public String oDeploymentProgress(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside oDeploymentProgress view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("clusterId", clusterId);
		return "oClusterCreation/oDeploymentProgress";
	}*/

	/**
	 * O setup details.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	/*@RequestMapping(value = "/oSetupDetails/{clusterId}", method = RequestMethod.GET)
	public String oSetupDetails(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside oSetupDetails view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("clusterId", clusterId);
		return "oClusterCreation/oSetupDetails";
	}
*/
	@RequestMapping(value = "/setupDetail/{clusterId}", method = RequestMethod.GET)
	public String clusterCreate(ModelMap model,@PathVariable("clusterId") String clusterId) {
	logger.info("Inside cluster create view");
	model.addAttribute("title", "setupDetail");
	model.addAttribute("cssFiles", cssFiles);
	model.addAttribute("clusterId", clusterId);
	return "oClusterCreation/oClusterCreate";
}
	/**
	 * O node setup detail.
	 *
	 * @param model the model
	 * @param clusterId the cluster id
	 * @return the string
	 */
	/*@RequestMapping(value = "/oNodeSetupDetail/{clusterId}/details", method = RequestMethod.GET)
	public String oNodeSetupDetail(ModelMap model,
			@PathVariable("clusterId") String clusterId) {
		logger.info("Inside oNodeSetupDetail view");
		model.addAttribute("title", "oNodeDetail");
		model.addAttribute("clusterId", clusterId);
		return "oClusterCreation/oNodeSetupDetail";
	}*/

	/**
	 * O node status.
	 *
	 * @param model the model
	 * @return the string
	 */
/*	@RequestMapping(value = "/oNodeStatus", method = RequestMethod.GET)
	public String oNodeStatus(ModelMap model) {
		logger.info("Inside oNodeStatus view");
		model.addAttribute("title", "oNodeDetail");
		return "oClusterCreation/oNodeStatus";
	}*/

	/**
	 * O cluster create error.
	 *
	 * @param model the model
	 * @return the string
	 */
/*	@RequestMapping(value = "/oClusterCreateError", method = RequestMethod.GET)
	public String oClusterCreateError(ModelMap model) {
		logger.info("Inside oClusterCreateError view");
		model.addAttribute("title", "oNodeDetail");
		return "oClusterCreation/oClusterCreateError";
	}
*/
	/**
	 * O cluster deploy.
	 *
	 * @param model the model
	 * @return the string
	 */
/*	@RequestMapping(value = "/oClusterDeploy", method = RequestMethod.GET)
	public String oClusterDeploy(ModelMap model) {
		logger.info("Inside oDeploymentProgress view");
		model.addAttribute("title", "oNodeDetail");
		return "oClusterCreation/oDeploymentProgress";
	}*/

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return null;
	}
}
