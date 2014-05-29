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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.exception.ControllerException;
import com.impetus.ankush.common.exception.UserExistsException;
import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.utils.ResponseWrapper;

/**
 * Controller for User related actions.
 * <p/>
 */
@Controller
@RequestMapping("/")
public class UserController extends BaseController {

	/** The app conf service. */
	private AppConfService appConfService;

	/**
	 * Sets the config service.
	 * 
	 * @param appConfService
	 *            the new config service
	 */
	@Autowired
	public void setConfigService(
			@Qualifier("appConfService") AppConfService appConfService) {
		this.appConfService = appConfService;
	}
	
	/**
	 * Gets the user id.
	 *
	 * @param request the request
	 * @return the user id
	 */
	@RequestMapping(value = { "user/userid" }, method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map<String, Object>>> getUserId(
			HttpServletRequest request) {
		String userName = null;
		Principal p = request.getUserPrincipal();
		if (p != null) {
			userName = p.getName();
		}
		if (userName != null) {
			Map<String, Object> userInfo = new HashMap<String, Object>();
			String state = appConfService.getState();
			userInfo.put("target", state);
			userInfo.put("username", userName);
			return wrapResponse(userInfo, HttpStatus.OK,
					HttpStatus.OK.toString(), "User ID");
		}
		throw new ControllerException(HttpStatus.BAD_REQUEST,
				HttpStatus.BAD_REQUEST.toString(),
				"Unable to retrieve user name");
	}

	/**
	 * Gets the user by attribute.
	 *
	 * @param parameters the parameters
	 * @param paramName the param name
	 * @param attributeName the attribute name
	 * @return the user by attribute
	 */
	private User getUserByAttribute(Map<String, Object> parameters,
			String paramName, String attributeName) {
		User user = null;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		String paramVal = (String) parameters.get(paramName);
		if ((paramVal != null) && paramVal.length() > 0) {
			queryMap.put(attributeName, paramVal);
			try {
				user = userManager.getByPropertyValue(queryMap);
			} catch (EmptyResultDataAccessException e) {
			}
		}
		return user;
	}

	/**
	 * Forgot password.
	 *
	 * @param parameters the parameters
	 * @return the response entity
	 */
	@RequestMapping(value = { "user/forgotpassword" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> forgotPassword(
			@RequestBody Map<String, Object> parameters) {
		User user = getUserByAttribute(parameters, "username", "username");
		if (user != null) {
			userManager.forgotPassword(user);
			return wrapResponse(null, HttpStatus.OK, HttpStatus.OK.toString(),
					"New password mail sent");
		}
		throw new ControllerException(HttpStatus.BAD_REQUEST,
				HttpStatus.BAD_REQUEST.toString(),
				"No account found with this User ID");
	}

	/**
	 * Forgot user id.
	 *
	 * @param parameters the parameters
	 * @return the response entity
	 */
	@RequestMapping(value = { "user/forgotuserid" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> forgotUserId(
			@RequestBody Map<String, Object> parameters) {
		User user = getUserByAttribute(parameters, "mail", "email");
		if (user != null) {
			userManager.forgotUserId(user);
			return wrapResponse(null, HttpStatus.OK, HttpStatus.OK.toString(),
					"Userid information mail sent");
		}
		throw new ControllerException(HttpStatus.BAD_REQUEST,
				HttpStatus.BAD_REQUEST.toString(),
				"No account found with this Email address");
	}

	/**
	 * Change password.
	 *
	 * @param parameters the parameters
	 * @param request the request
	 * @return the response entity
	 */
	@RequestMapping(value = { "user/changepassword" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Object>> changePassword(
			@RequestBody Map<String, Object> parameters,
			HttpServletRequest request) {
		String msg = null;
		String userName = request.getUserPrincipal().getName();
		String currentPassword = (String) parameters.get("password");
		String newPassword = (String) parameters.get("newpassword");
		try {
			userManager.changePassword(userName, currentPassword, newPassword);
			return wrapResponse(null, HttpStatus.OK, HttpStatus.OK.toString(),
					"User password changed successfully");
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return wrapResponse(null, HttpStatus.BAD_REQUEST,
				HttpStatus.BAD_REQUEST.toString(), msg);
	}

	/**
	 * Chek user existence.
	 *
	 * @param user the user
	 * @return the response entity
	 * @throws UserExistsException the user exists exception
	 */
	@RequestMapping(value = { "admin/validate" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Boolean>> chekUserExistence(
			@RequestBody User user) throws UserExistsException {
		boolean userExist = false;
		userExist = userManager.checkUserExistance(user);
		return wrapResponse(userExist, HttpStatus.OK, HttpStatus.OK.toString(),
				"User Existance Check");
	}
}
