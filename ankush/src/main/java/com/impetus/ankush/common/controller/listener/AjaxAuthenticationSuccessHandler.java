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
package com.impetus.ankush.common.controller.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.service.UserManager;

/**
 * The Class AjaxAuthenticationSuccessHandler.
 */
public class AjaxAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {

	/** The user manager. */
	private UserManager userManager;

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
	 * Sets the user manager.
	 * 
	 * @param userManager
	 *            the new user manager
	 */
	@Autowired
	public void setUserManager(@Qualifier("userManager") UserManager userManager) {
		this.userManager = userManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 * SavedRequestAwareAuthenticationSuccessHandler
	 * #onAuthenticationSuccess(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse,
	 * org.springframework.security.core.Authentication)
	 */
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			String state = null;
			String userName = authentication.getName();
			User usr = userManager.getByPropertyValue("username", userName);
			if (usr != null) {
				Object obj = usr.getForcePasswordChange();
				if ((obj != null) && (obj instanceof Boolean)) {
					boolean b = (Boolean)obj;
					if (b)
						state = Constant.App.State.CHANGE_PASSWORD;
				}
			}
				
			if (state == null)
				state = appConfService.getState();

			response.setContentType("application/json");
			String responseJSON = "{\"success\":true";
			if (state != null) {
				responseJSON += ",\"target\":\"" + state + "\"";
			}
			responseJSON += "}";
			response.getWriter().print(responseJSON);
			response.getWriter().flush();
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
}
