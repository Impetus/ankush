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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;

import com.impetus.ankush.common.constant.Constant;

/**
 * @author Akhil
 * 
 */
public class AnkushRestClient {

	private static final String APPLICATION_JSON = "application/json";

	/** The mapper. */
	private ObjectMapper mapper = new ObjectMapper();

	public AnkushRestClient() {
		mapper.setVisibilityChecker(mapper.getVisibilityChecker()
				.withFieldVisibility(Visibility.ANY));
	}

	private AnkushLogger logger = new AnkushLogger(AnkushRestClient.class);

	/**
	 * Method sendNodeInfo.
	 * 
	 * @param urlPath
	 *            String
	 * @param input
	 *            String
	 * @return String
	 * @throws IOException
	 */
	public HttpResult sendRequest(String urlPath, String input, String method,
			String accept, String contentType) {
		HttpURLConnection conn = null;
		String output = "";
		OutputStream os = null;

		// Http Response object.
		HttpResult result = new HttpResult();
		boolean status = true;

		try {
			URL url = new URL(urlPath);
			logger.info("Executing request : " + urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(method);
			conn.setRequestProperty("Accept", accept);
			conn.setRequestProperty("Content-type", contentType);
			if (input != null && !input.isEmpty()) {
				os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();
			}
			String buffer = "";
			BufferedReader br;
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				status = false;
				String error = "";
				// get ErrorStream
				br = new BufferedReader(new InputStreamReader(
						(conn.getErrorStream())));

				while ((buffer = br.readLine()) != null) {
					error += buffer;
				}
				result.setError(error);
			}
			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			while ((buffer = br.readLine()) != null) {
				output += buffer;
			}
		} catch (Exception e) {
			logger.debug(e.getMessage());
			logger.error("Exception in executing request : " + urlPath, e);
			result.setError(e.getLocalizedMessage());
			status = false;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
			if (os != null) {
				IOUtils.closeQuietly(os);
			}
		}
		result.setStatus(status);
		result.setOutput(output);

		return result;
	}

	/**
	 * Method to send get request.
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String getRequest(String url) {
		HttpResult output = sendRequest(url, null, Constant.Method_Type.GET,
				APPLICATION_JSON, APPLICATION_JSON);
		return output.getOutput();
	}

	/**
	 * Method to send get request.
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public HttpResult get(String url) {
		HttpResult output = sendRequest(url, null, Constant.Method_Type.GET,
				APPLICATION_JSON, APPLICATION_JSON);
		return output;
	}

	/**
	 * Post Method to send the data.
	 * 
	 * @param url
	 * @param postData
	 * @return
	 */
	public HttpResult postRequest(String url, String postData) {
		HttpResult output = sendRequest(url, postData,
				Constant.Method_Type.POST, APPLICATION_JSON, APPLICATION_JSON);
		return output;
	}
}
