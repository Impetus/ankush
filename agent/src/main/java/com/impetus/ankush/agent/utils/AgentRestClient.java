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
package com.impetus.ankush.agent.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * The Class AgentRestClient.
 * 
 * @author Hokam Chauhan
 */
public class AgentRestClient {

	/** The log. */
	private static final AgentLogger LOGGER = new AgentLogger(
			AgentRestClient.class);

	private static final String APPLICATION_JSON = "application/json";

	/** The mapper. */
	private ObjectMapper mapper = new ObjectMapper();

	public AgentRestClient() {
		mapper.setVisibilityChecker(mapper.getVisibilityChecker()
				.withFieldVisibility(Visibility.ANY));
	}

	/**
	 * Method getJsonInfoString.
	 * 
	 * @param info
	 *            Object
	 * @return String The Json String of Object
	 */
	private String getJsonInfoString(Object info) {
		String jsonObject = null;
		try {
			jsonObject = mapper.writeValueAsString(info);
		} catch (JsonGenerationException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			LOGGER.error(e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return jsonObject;
	}

	/**
	 * Method sendObject.
	 * 
	 * @param object
	 *            Object
	 * @param urlPath
	 *            String
	 * @return String The Json Output Response of the POST Request.
	 */
	public String sendData(Object object, String urlPath) {

		String json = getJsonInfoString(object);
		return postRequest(urlPath, json);

	}

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
	private String sendRequest(String urlPath, String input, String method,
			String accept, String contentType) {
		HttpURLConnection conn = null;
		OutputStream os = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			LOGGER.info("URL Path : " + urlPath);
			URL url = new URL(urlPath);

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

			LOGGER.info("Response Code :" + conn.getResponseCode());

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			isr = new InputStreamReader(conn.getInputStream());
			br = new BufferedReader(isr);

			String buffer = "";
			StringBuilder output = new StringBuilder();
			while ((buffer = br.readLine()) != null) {
				output.append(buffer);
			}
			return output.toString();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}

				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				LOGGER.error(
						"Unable to close buffer stream while sending request",
						e);
			}
			if (conn != null) {
				conn.disconnect();
			}

			if (os != null) {
				IOUtils.closeQuietly(os);
			}
		}
	}

	/**
	 * Method to send get request.
	 * 
	 * @param url
	 * @return
	 */
	public String getRequest(String url) {
		return sendRequest(url, null, "GET", APPLICATION_JSON, APPLICATION_JSON);
	}

	/**
	 * Method to send post request.
	 * 
	 * @param url
	 * @param data
	 * @return
	 */
	public String postRequest(String url, String data) {
		return sendRequest(url, data, "POST", APPLICATION_JSON,
				APPLICATION_JSON);
	}

}
