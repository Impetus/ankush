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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.impetus.ankush.common.service.AppConfService;
import com.impetus.ankush.common.utils.ResponseWrapper;
import com.impetus.ankush.common.utils.UploadHandler;

/**
 * The Class FileUploadController.
 * 
 * @author mayur
 */
@Controller
@RequestMapping("/")
public class FileUploadController extends BaseController {

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
	 * Upload.
	 * 
	 * @param category
	 *            the category
	 * @param multipartRequest
	 *            the multipart request
	 * @param p
	 *            the p
	 * @return the response entity
	 */
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseWrapper<String>> upload(
			@RequestParam(defaultValue = "") String category,
			MultipartHttpServletRequest multipartRequest, Principal p) {

		MultipartFile multipartFile = (MultipartFile) multipartRequest
				.getFile("file");

		UploadHandler uploadHandler = new UploadHandler(multipartFile, category);
		return wrapResponse(uploadHandler.uploadFile(), HttpStatus.CREATED,
				HttpStatus.CREATED.toString(), "file uploaded successfully");
	}


	/**
	 * Gets the available file list.
	 * 
	 * @param parameters
	 *            the parameters
	 * @return the available file list
	 */
	@RequestMapping(value = "list/files", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<ResponseWrapper<Map>> getAvailableFileList(
			@RequestBody Map<String, String> parameters) {
		UploadHandler handler = new UploadHandler();
		return wrapResponse(handler.getAvailableFileList(parameters),
				HttpStatus.OK, HttpStatus.OK.toString(),
				"list of available files.");
	}
}
