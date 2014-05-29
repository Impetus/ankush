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
package com.impetus.ankush.agent.action.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.action.Actionable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.ZipFiles;

/**
 * The Class UploadHandler.
 */
public class UploadHandler implements Actionable {
	/** The log. */
	private static AgentLogger LOGGER = new AgentLogger(UploadHandler.class);

	/**
	 * A generic method to execute any type of Http Request and constructs a
	 * response object.
	 * 
	 * @param requestBase
	 *            the request that needs to be exeuted
	 * @return server response as <code>String</code>
	 */
	private static String executeRequest(HttpRequestBase requestBase) {
		String responseString = "";

		InputStream responseStream = null;
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(requestBase);
			if (response != null) {
				HttpEntity responseEntity = response.getEntity();

				if (responseEntity != null) {
					responseStream = responseEntity.getContent();
					if (responseStream != null) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(responseStream));
						String responseLine = br.readLine();
						String tempResponseString = "";
						while (responseLine != null) {
							tempResponseString = tempResponseString
									+ responseLine
									+ System.getProperty("line.separator");
							responseLine = br.readLine();
						}
						br.close();
						if (tempResponseString.length() > 0) {
							responseString = tempResponseString;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			if (responseStream != null) {
				try {
					responseStream.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		client.getConnectionManager().shutdown();

		return responseString;
	}

	/**
	 * Method that builds the multi-part form data request.
	 * 
	 * @param urlString
	 *            the urlString to which the file needs to be uploaded
	 * @param file
	 *            the actual file instance that needs to be uploaded
	 * @param fileName
	 *            name of the file, just to show how to add the usual form
	 *            parameters
	 * @param fileDescription
	 *            some description for the file, just to show how to add the
	 *            usual form parameters
	 * @return server response as <code>String</code>
	 */
	public String executeMultiPartRequest(String urlString, File file,
			String fileName, String fileDescription) {

		HttpPost postRequest = new HttpPost(urlString);
		try {

			MultipartEntity multiPartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			// The usual form parameters can be added this way
			multiPartEntity.addPart("fileDescription", new StringBody(
					fileDescription != null ? fileDescription : ""));
			multiPartEntity.addPart("fileName", new StringBody(
					fileName != null ? fileName : file.getName()));

			/*
			 * Need to construct a FileBody with the file that needs to be
			 * attached and specify the mime type of the file. Add the fileBody
			 * to the request as an another part. This part will be considered
			 * as file part and the rest of them as usual form-data parts
			 */
			FileBody fileBody = new FileBody(file, "application/octect-stream");
			multiPartEntity.addPart("file", fileBody);

			postRequest.setEntity(multiPartEntity);

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			return null;
		}

		return executeRequest(postRequest);
	}

	/**
	 * Upload file.
	 * 
	 * @param filePath
	 *            the file path
	 * @param url
	 *            the url
	 * @return true, if successful
	 */
	public boolean uploadFile(String filePath, String url) {
		ZipFiles zipFile = new ZipFiles();
		filePath = zipFile.zipFile(filePath);
		if (filePath != null) {
			File file = new File(filePath);

			String response = executeMultiPartRequest(url, file,
					file.getName(), "File Upload");
			LOGGER.info("Response : " + response);

			FileUtils.deleteQuietly(file);
			if (response == null) {
				return false;
			}

			return response.contains("file uploaded successfully");
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.agent.action.Actionable#execute(java.util.List)
	 */
	@Override
	public boolean execute(List<String> argsList) {
		if (argsList.size() < 1) {
			System.out.println("Usage: java -jar agent.jar upload <filePath>");
			System.exit(1);
		}
		String filePath = argsList.get(0);

		// if file path null.
		if (filePath == null) {
			System.err.println("Invalid file path, Please provide valid path.");
			System.exit(1);
		}
		File file = new File(filePath);

		if (!file.isFile() || !file.exists()) {
			System.err
					.println("File does not exist, Please provide valid path.");
			System.exit(1);
		}

		AgentConf conf = new AgentConf();
		String fileUploadUrl = conf.getLogUploadFileUrl();

		try {
			boolean status = uploadFile(filePath, fileUploadUrl);
			if (!status) {
				throw new Exception("File Upload failed.");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		System.out.println("File " + filePath + " uploaded to server ");
		System.exit(0);
		return true;
	}
}
