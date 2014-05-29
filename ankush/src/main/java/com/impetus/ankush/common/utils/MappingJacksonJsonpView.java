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
package com.impetus.ankush.common.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

/**
 * The Class MappingJacksonJsonpView.
 */
public class MappingJacksonJsonpView extends MappingJacksonJsonView {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MappingJacksonJsonpView.class);

	/**
	 * Default content type. Overridable as bean property.
	 */
	public static final String DEFAULT_CONTENT_TYPE = "application/javascript";

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#getContentType()
	 */
	@Override
	public String getContentType() {
		return DEFAULT_CONTENT_TYPE;
	}

	/**
	 * Prepares the view given the specified model, merging it with static
	 * attributes and a RequestContext attribute, if necessary. Delegates to
	 * renderMergedOutputModel for the actual rendering.
	 *
	 * @param model the model
	 * @param request the request
	 * @param response the response
	 * @throws Exception the exception
	 * @see #renderMergedOutputModel
	 */
	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		LOGGER.info("MappingJacksonJsonpView::render - Requesting Method...." + request.getMethod().toUpperCase());
		if ("GET".equals(request.getMethod().toUpperCase())) {
			@SuppressWarnings("unchecked")
			Map<String, String[]> params = request.getParameterMap();

			if (params.containsKey("callback")) {
				LOGGER.info("MappingJacksonJsonpView::render - callback method request....");
				response.getOutputStream().write(
						new String(params.get("callback")[0] + "(").getBytes());
				super.render(model, request, response);
				response.getOutputStream().write(");".getBytes());
				response.setContentType("application/javascript");
			}
			else {
				super.render(model, request, response);
			}
		}
		else {
			super.render(model, request, response);
		}
	}
}
