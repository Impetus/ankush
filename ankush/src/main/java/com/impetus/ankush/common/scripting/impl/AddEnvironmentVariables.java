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
package com.impetus.ankush.common.scripting.impl;

import java.util.LinkedHashMap;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.sun.mail.imap.AppendUID;

/**
 * @author Akhil
 * 
 */
public class AddEnvironmentVariables extends AnkushTask {

	LinkedHashMap<String, String> envPropertiesMap = new LinkedHashMap<String, String>();

	/** The file path. */
	private String filePath = Constant.ETCENV_FILE;

	private String startTag = Constant.TEXT_COMMENT_START;

	private String endTag = Constant.TEXT_COMMENT_END;

	/**
	 * @param envPropertiesMap
	 * @param filePath
	 * @param componentName
	 */
	public AddEnvironmentVariables(
			LinkedHashMap<String, String> envPropertiesMap, String filePath,
			String componentName) {
		this.envPropertiesMap = envPropertiesMap;
		this.filePath = filePath;
		this.startTag += componentName;
		this.endTag += componentName;
	}

	/**
	 * @param envPropertiesMap
	 * @param componentName
	 */
	public AddEnvironmentVariables(
			LinkedHashMap<String, String> envPropertiesMap, String componentName) {
		this.envPropertiesMap = envPropertiesMap;
		this.startTag += componentName;
		this.endTag += componentName;
	}

	@Override
	public String getCommand() {
		StringBuilder text = new StringBuilder(startTag);
		text.append("\\n");
		for (String variableKey : envPropertiesMap.keySet()) {
			text.append(variableKey).append("=")
					.append(envPropertiesMap.get(variableKey)).append("\\n");
		}
		text.append(endTag);

		AnkushTask appendUsingSed = new AppendFileUsingSed(text.toString(), this.filePath);
		return appendUsingSed.getCommand();
//		return "sed -i '/"
//				+ com.impetus.ankush2.constant.Constant.Strings.TEXT_COMMENT_END_ENV_SETTINGS
//				+ "/i \\" + text.toString() + "' " + filePath;
		// return "sed -i '$ a\'\""+ text.toString() + "\" " + filePath;
	}
}
