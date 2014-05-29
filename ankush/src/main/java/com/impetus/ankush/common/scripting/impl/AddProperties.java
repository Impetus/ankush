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
package com.impetus.ankush.common.scripting.impl;

import java.util.Iterator;
import java.util.Properties;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * The Class AppendFile.
 * 
 * @author vinay
 */
public class AddProperties extends AnkushTask {

	/** The properties. */
	Properties properties;

	/** The file path. */
	private String filePath = null;

	private String password = null;

	/**
	 * Instantiates a new add properties.
	 * 
	 * @param properties
	 *            the properties
	 * @param filePath
	 *            the file path
	 */
	public AddProperties(Properties properties, String filePath) {
		this(properties, filePath, null);
	}

	/**
	 * Instantiates a new add properties.
	 * 
	 * @param properties
	 *            the properties
	 * @param filePath
	 *            the file path
	 * @param password
	 *            the password
	 */
	public AddProperties(Properties properties, String filePath, String password) {
		this.properties = properties;
		this.filePath = filePath;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		String line = getPropertiesString();
		AppendFile appendFile = new AppendFile(line, filePath, password);
		return appendFile.getCommand();
	}

	private String getPropertiesString() {
		StringBuilder sb = new StringBuilder();
		Iterator itr = properties.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			String value = properties.getProperty(key);
			sb.append(key).append("=").append(value)
					.append(Constant.LINE_SEPERATOR);
		}
		return sb.toString();
	}
}
