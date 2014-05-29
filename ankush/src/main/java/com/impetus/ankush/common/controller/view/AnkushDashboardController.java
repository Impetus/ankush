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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.impetus.ankush.common.domain.User;

/**
 * The Class AnkushDashboardController.
 */
@Controller
@RequestMapping("/dashboardnew")
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
		jsFiles.add("ankush.techlist");
		jsFiles.add("ankush.dashboard");
		jsFiles.add("commonMonitoring");
		jsFiles.add("ankush.password");
	}
	
	/**
	 * home view to render by returning its name.
	 *
	 * @param model the model
	 * @return the string
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(ModelMap model) {
		
		logger.info("User name " + getUserName());
		logger.info("Inside dashboard home view");
		model.addAttribute("title", "dashboard");
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "dashboard/home";
	}
	
	/**
	 * Gets the default page.
	 *
	 * @param model the model
	 * @return the default page
	 */
	@RequestMapping(value="/default",method= RequestMethod.GET)
	public String getDefaultPage(ModelMap model){
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "systemOverview";
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
	@RequestMapping(value="/getUpdatePassword",method= RequestMethod.GET)
	public String getUpdatePassword(ModelMap model){
		model.addAttribute("cssFiles", cssFiles);
		model.addAttribute("jsFiles", jsFiles);
		return "changePassword";
	}
	
	@RequestMapping(value = "/listTemplate", method = RequestMethod.GET)
	public String templateList(ModelMap model) {
		logger.info("Inside template management view");
		model.addAttribute("title", "Template Management");
		return "templateManage";
	}
	
	
}
