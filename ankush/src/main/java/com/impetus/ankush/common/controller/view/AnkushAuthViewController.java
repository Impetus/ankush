/*******************************************************************************
 * Copyright 2014 Impetus Infotech.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ******************************************************************************/
package com.impetus.ankush.common.controller.view;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
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
	@RequestMapping(value = "/user1/forget_password", method = RequestMethod.GET)
	public String resetPassword(
			@RequestParam(value = "error", required = false) boolean error,
			ModelMap model) {
		return "user/forget-password";
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
	@RequestMapping(value = "/auth/config", method = RequestMethod.GET)
	public String getConfigPage(
			@RequestParam(value = "error", required = false) boolean error,
			ModelMap model, HttpServletRequest request) {
		logger.info("Received Request to show COnfiguration page");
		model.addAttribute("url", "/app/conf");
		return "/afterLoginConfigPage";
	}

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
