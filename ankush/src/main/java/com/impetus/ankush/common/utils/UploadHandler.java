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
/**
 * 
 */
package com.impetus.ankush.common.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.impetus.ankush.AppStore;
import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.exception.ControllerException;

/**
 * The Class UploadHandler.
 * 
 * @author hokam chauhan
 */
public class UploadHandler {

	/** The Constant UPLOAD_PATH. */
	private static final String UPLOAD_PATH;

	/** The Constant REPO_PATH. */
	private static final String REPO_PATH;
	
	/** The Constant LICENSE_PATH. */
	private static final String LICENSE_PATH;
	
	private static String LICENSE = "license"; 

	/** The category. */
	private String category = new String();

	static {
		String userHome = System.getProperty("user.home");
		ConfigurationReader reader = AppStoreWrapper.getAnkushConfReader();
		UPLOAD_PATH = userHome + reader.getStringValue("uploadpath");

		REPO_PATH = userHome + reader.getStringValue("repo");
		
		LICENSE_PATH = userHome + reader.getStringValue(LICENSE);
	}

	/** The multipart file. */
	private MultipartFile multipartFile;

	/**
	 * Instantiates a new upload handler.
	 */
	public UploadHandler() {
	}

	/**
	 * Instantiates a new upload handler.
	 * 
	 * @param multipartFile
	 *            the multipart file
	 */
	public UploadHandler(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	/**
	 * Instantiates a new upload handler.
	 * 
	 * @param multipartFile
	 *            the multipart file
	 * @param category
	 *            the category
	 */
	public UploadHandler(MultipartFile multipartFile, String category) {
		this.multipartFile = multipartFile;
		this.category = category;
	}

	/**
	 * Gets the hash.
	 * 
	 * @return the hash
	 */
	private String getHash() {
		return RandomStringUtils.randomAlphanumeric(20)
				+ System.currentTimeMillis();
	}

	/**
	 * Gets the upload folder path.
	 * 
	 * @return the uploadFolderPath
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String getUploadFolderPath() throws IOException {

		String path = null;
		// equals the category provide.
		if (category.equalsIgnoreCase("bundle")) {
			path = REPO_PATH;
		} else if (category.equals("log")) {
			// Getting application real path.
			String appRealPath = AppStoreWrapper.getAppPath();

			// Getting the application public/clusters folder.
			String clustersFolderPath = FileNameUtils
					.convertToValidPath(appRealPath) + "resources/clusters/";

			// Getting the cluster folder path.
			path = clustersFolderPath + "/logs/";
		} else if(category.equals(LICENSE)){
			path = LICENSE_PATH;
		}else {
			// other paths.
			path = UPLOAD_PATH + getHash() + File.separator;
		}
		// Create file.
		File file = new File(path);
		FileUtils.forceMkdir(file);
		return path;
	}

	/**
	 * Upload file.
	 * 
	 * @return the string
	 */
	public String uploadFile() {

		if (multipartFile == null) {
			throw new ControllerException(HttpStatus.BAD_REQUEST,
					HttpStatus.BAD_REQUEST.toString(),
					"No payload with name \"file\" found in the request ");
		}

		// file path to upload.
		String realFilePath = null;
		try {
			// getting original file name.
			String originalFileName = multipartFile.getOriginalFilename();

			// making real file path.
			realFilePath = this.getUploadFolderPath() + originalFileName;

			// destination file.
			File dest = new File(realFilePath);

			multipartFile.transferTo(dest);
		} catch (IllegalStateException e) {
			throw new ControllerException(HttpStatus.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
		} catch (IOException e) {
			throw new ControllerException(HttpStatus.INTERNAL_SERVER_ERROR,
					HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage());
		}
		return realFilePath;
	}

	/**
	 * Gets the available file list.
	 * 
	 * @param parameters
	 *            the parameters
	 * @return The List of file names for the found files inside the Category
	 *         folder.
	 */
	public Map getAvailableFileList(Map<String, String> parameters) {

		Map result = new HashMap<String, Object>();

		/* Getting the Category value from the Parameters map */
		String category = parameters.get("category");

		/* Getting the Pattern value from the Parameters map */
		final String pattern = parameters.get("pattern");

		/* Getting the Path of folder for the category */
		String folderPath = (String) AppStore.getObject(category.toLowerCase());

		List<String> fileList = new ArrayList<String>();

		if (folderPath != null) {
			result.put("path", folderPath);
			File repoDirs = new File(folderPath);
			FileFilter fileFilter = new FileFilter() {

				@Override
				public boolean accept(File file) {

					String regex = pattern;
					if (regex == null) {
						return true;
					} else if (regex.startsWith("*")) {
						return false;
					}
					return file.getName().matches(regex);
				}
			};

			for (File file : repoDirs.listFiles(fileFilter)) {
				fileList.add(file.getName());
			}
			result.put("files", fileList);
		}
		return result;
	}
}
