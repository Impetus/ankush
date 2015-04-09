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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Handles requests for the application home page.
 */
@Controller
public class AnkushAuthViewController extends AbstractController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(AnkushAuthViewController.class);
	private ArrayList<String> jsFiles;

	/**
	 * Simply selects the home view to render by returning its name.
	 * 
	 * @param locale
	 *            the locale
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! the client locale is " + locale.toString());
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);
		String formattedDate = dateFormat.format(date);
		model.addAttribute("serverTime", formattedDate);
		return "redirect:/auth/login";
	}

	/**
	 * Reset password.
	 * 
	 * @param error
	 *            the error
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/auth/forget_password", method = RequestMethod.GET)
	public String resetPassword(
			ModelMap model) {
		model.addAttribute("page", "forget password");
		return "/forgetPassword";
	}

	/**
	 * Handles and retrieves the login JSP page.
	 * 
	 * @param error
	 *            the error
	 * @param model
	 *            the model
	 * @param request
	 *            the request
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/auth/login", method = RequestMethod.GET)
	public String getLoginPage(
			@RequestParam(value = "error", required = false) boolean error,
			ModelMap model, HttpServletRequest request) {
		logger.info("Received request to show login page");
		model.addAttribute("page", "login");
		return "/home";
	}

	/**
	 * Gets the config page.
	 * 
	 * @param error
	 *            the error
	 * @param model
	 *            the model
	 * @param request
	 *            the request
	 * @return the config page
	 */
	

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
		return null;
	}
}
