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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.impetus.ankush.common.domain.User;

/**
 * The Class AnkushDashboardController.
 */
@Controller
@RequestMapping("/dashboard")
public class AnkushDashboardController extends AbstractController {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
	.getLogger(AnkushDashboardController.class);
	
	/** The css files. */
	private ArrayList<String> cssFiles;
	
	/** The js files. */
	private ArrayList<String> jsFiles;
	
	/**
	 * Instantiates a new ankush dashboard controller.
	 */
	public AnkushDashboardController() {
		// TODO Auto-generated constructor stub
		cssFiles = new ArrayList<String>();
		jsFiles = new ArrayList<String>();
	}
	
	/**
	 * home view to render by returning its name.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String home(ModelMap model) {
		
		model.addAttribute("title", "dashboard");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("page", "Cluster Overview");
		return "dashboard/home";
	}
	@RequestMapping(value = "/clusterMaintenance", method = RequestMethod.GET)
	public String clusterMaintenance(ModelMap model) {	
		model.addAttribute("title", "clusterMaintenance");
		return "dashboard/clusterMaintenance";
	}
	@RequestMapping(value = "/nodeMaintenance", method = RequestMethod.GET)
	public String nodeMaintenance(ModelMap model,@RequestParam("clusterId") String clusterId,@RequestParam("host") String host) {	
		model.addAttribute("title", "nodeMaintenance");
		model.addAttribute("clusterId", clusterId);
		model.addAttribute("host", host);
		return "dashboard/nodeMaintenance";
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
	@RequestMapping(value="/changePassword/C-D/{decisionVar}",method= RequestMethod.GET)
	public String getUpdatePassword(ModelMap model,@PathVariable("decisionVar") String decisionVar){
		model.addAttribute("decisionVar", decisionVar);
		model.addAttribute("page", "changePassword");
		return "changePassword";
	}
	@RequestMapping(value = "/listTemplate", method = RequestMethod.GET)
	public String templateList(ModelMap model) {
		logger.info("Inside template management view");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("title", "Template Management");
		return "templateManage";
	}
	@RequestMapping(value = "/configuration/C-D/{decisionVar}", method = RequestMethod.GET)
	public String getConfigPage(
		ModelMap model, HttpServletRequest request,@PathVariable("decisionVar") String decisionVar) {
		logger.info("Received Request to show COnfiguration page");
		jsFiles = new ArrayList<String>();
		jsFiles.add("configurationNew");
		model.addAttribute("jsFiles", jsFiles);
		model.addAttribute("decisionVar", decisionVar);
		model.addAttribute("page", "configuration");
		return "/afterLoginConfigPage";
	}
	/**
	 * Gets the user name.
	 *
	 * @return the userName
	 */
	public String getUserName() {
		try {
			SecurityContextHolder
					.setContext(SecurityContextHolder.getContext());

			User user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			return user.getUsername();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "UNKNOWN";
	}
	
	/**
	 * Gets the update password.
	 *
	 * @param model the model
	 * @return the update password
	 */
	
	
	
}
