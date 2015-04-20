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
package com.impetus.ankush.common.controller.rest;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.support.WebContentGenerator;

import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.exception.ControllerException;
import com.impetus.ankush.common.framework.config.ErrorInfo;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush2.constant.Constant;

/**
 * Implementation of <strong>SimpleFormController</strong> that contains
 * convenience methods for subclasses. For example, getting the current user and
 * saving messages/errors. This class is intended to be a base class for all
 * controllers.
 * 
 */
public class BaseController extends WebContentGenerator implements
		ServletContextAware {

	/** The log. */
	protected final transient Logger log = LoggerFactory.getLogger(getClass());

	/** The user manager. */
	protected UserManager userManager = null;

	/** The Constant ACCEPT_APPLICATION_JSON. */
	protected static final String ACCEPT_APPLICATION_JSON = "Accept=application/json";

	/**
	 * Sets the user manager.
	 *
	 * @param userManager the new user manager
	 */
	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * Gets the user manager.
	 *
	 * @return the user manager
	 */
	public UserManager getUserManager() {
		return this.userManager;
	}

	/**
	 * Convenience method to get the Configuration HashMap from the servlet
	 * context.
	 * 
	 * @return the user's populated form from the session
	 */
	public Map<String, Object> getConfiguration() {
		Map<String, Object> config = (Map<String, Object>) super
				.getServletContext().getAttribute(Constant.Server.CONFIG);

		// so unit tests don't puke when nothing's been set
		if (config == null) {
			return Collections.EMPTY_MAP;
		}

		return config;
	}

	/**
	 * Set up a custom property editor for converting form inputs to real
	 * objects.
	 *
	 * @param request the current request
	 * @param binder the data binder
	 */
	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Integer.class, null,
				new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(
				Long.class, null, true));
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy.MM.dd G 'at' HH:mm:ss z");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(
				dateFormat, true));
	}

	/**
	 * Gets the logged in user.
	 *
	 * @param principal the principal
	 * @return the logged in user
	 */
	public User getLoggedInUser(Principal principal) {
		User user = null;
		if (principal != null) {
			String userName = principal.getName();
			user = userManager.getUserByUsername(userName);
		}
		return user;
	}

	/**
	 * Exception handler.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(ControllerException.class)
	@ResponseBody
	public ResponseEntity<ControllerException> exceptionHandler(
			ControllerException e) {
		return new ResponseEntity<ControllerException>(e, e.getStatus());
	}

	/**
	 * Exception handler.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<String> exceptionHandler(
			HttpMessageNotReadableException e) {
		log.error("Invalid request body", e);
		return new ResponseEntity<String>(e.getMessage(),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Exception handler.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<String> exceptionHandler(Exception e) {
		log.error("Error occured while executing request", e);
		return new ResponseEntity<String>(e.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Wrap response.
	 *
	 * @param <T> the generic type
	 * @param output the output
	 * @param httpStatus the http status
	 * @param status the status
	 * @param description the description
	 * @param errors the errors
	 * @return the response entity
	 */
	protected <T> ResponseEntity<ResponseWrapper<T>> wrapResponse(T output,
			HttpStatus httpStatus, String status, String description,
			List<ErrorInfo> errors) {

		return new ResponseEntity<ResponseWrapper<T>>(new ResponseWrapper<T>(
				output, status, description, errors), httpStatus);
	}

	/**
	 * Wrap response.
	 *
	 * @param <T> the generic type
	 * @param output the output
	 * @param httpStatus the http status
	 * @param status the status
	 * @param description the description
	 * @param errors the errors
	 * @return the response entity
	 */
	protected <T> ResponseEntity<ResponseWrapper<T>> wrapResponse(T output,
			HttpStatus httpStatus, String status, String description,
			ErrorInfo... errors) {

		List<ErrorInfo> errorList = null;
		if (errors.length > 0) {
			errorList = Arrays.asList(errors);
		}

		return this.wrapResponse(output, httpStatus, status, description,
				errorList);
	}
}
