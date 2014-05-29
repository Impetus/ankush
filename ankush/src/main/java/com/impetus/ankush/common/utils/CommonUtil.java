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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Random;

/**
 * The Class CommonUtil.
 */
public class CommonUtil {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(CommonUtil.class);

	/**
	 * Method to get random request id.
	 * 
	 * @param rndVal
	 *            the rnd val
	 * @return the random req id
	 */
	public static String getRandomReqId(int rndVal) {
		return System.currentTimeMillis() + "_" + rndVal;
	}

	/**
	 * Method to get random request id.
	 * 
	 * @return the random req id
	 */
	public static String getRandomReqId() {
		return getRandomReqId(getIntRandomReqId());
	}

	/**
	 * Method to get integer random id.
	 * 
	 * @return the int random req id
	 */
	public static int getIntRandomReqId() {
		Random random = new Random();
		int n = random.nextInt();
		if (n < 0) {
			n = -n;
		}
		return n;
	}

	/**
	 * Method to get int value from string.
	 * 
	 * @param intStr
	 *            the int str
	 * @param defaultVal
	 *            the default val
	 * @return the int
	 */
	public static int getInt(String intStr, int defaultVal) {
		int paramVal = 0;
		try {
			if ((intStr != null) && (!intStr.equals(""))) {
				paramVal = Integer.parseInt(intStr);
			} else {
				paramVal = defaultVal;
			}
		} catch (NumberFormatException e) {
			paramVal = defaultVal;
		}
		return paramVal;
	}

	/**
	 * Method to load properties from input stream.
	 * 
	 * @param inStream
	 *            the in stream
	 * @return the properties
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Properties loadProperties(InputStream inStream)
			throws IOException {
		Properties properties = null;
		try {
			Properties props = new Properties();
			props.load(inStream);
			properties = props;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			inStream.close();
		}
		return properties;
	}

	/**
	 * Method to return singular or plural by the count value.
	 * 
	 * @param count
	 * @param singular
	 * @param plural
	 * @return
	 */
	public static String singlePlural(int count, String singular, String plural) {
		return count == 1 ? singular : plural;
	}

	/**
	 * Method to load properties.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the properties
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Properties loadProperties(String fileName)
			throws FileNotFoundException, IOException {
		Properties properties = null;
		InputStream inStream = new FileInputStream(fileName);
		properties = loadProperties(inStream);
		return properties;
	}

	/**
	 * Method to store properties.
	 * 
	 * @param fileName
	 *            the file name
	 * @param propName
	 *            the prop name
	 * @param propValue
	 *            the prop value
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void storeProperties(String fileName, String propName,
			String propValue) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.setProperty(propName, propValue);
		storeProperties(fileName, properties);
	}

	/**
	 * Method to store properties.
	 * 
	 * @param fileName
	 *            the file name
	 * @param properties
	 *            the properties
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void storeProperties(String fileName, Properties properties)
			throws FileNotFoundException, IOException {
		OutputStream outputStream = new FileOutputStream(fileName);
		storeProperties(outputStream, properties);
		outputStream.close();
	}

	/**
	 * Method to store properties.
	 * 
	 * @param outputStream
	 *            the output stream
	 * @param properties
	 *            the properties
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void storeProperties(OutputStream outputStream,
			Properties properties) throws IOException {
		properties.store(outputStream, "" + System.currentTimeMillis());
	}

	/** The Constant USER_NAME_ROOT. */
	private static final String USER_NAME_ROOT = "root";

	/**
	 * Gets the user home.
	 * 
	 * @param userName
	 *            the user name
	 * @return standard linux home directory for user
	 */
	public static String getUserHome(String userName) {
		StringBuffer homePath = new StringBuffer(File.separator);
		if ((userName != null) && (!userName.equals(USER_NAME_ROOT))) {
			homePath.append("home").append(File.separator);
		}
		homePath.append(userName).append(File.separator);
		return homePath.toString();
	}

	/**
	 * Method to get my ip address.
	 * 
	 * @return the my ip address
	 * @throws UnknownHostException
	 *             the unknown host exception
	 */
	public static String getMyIPAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
}
