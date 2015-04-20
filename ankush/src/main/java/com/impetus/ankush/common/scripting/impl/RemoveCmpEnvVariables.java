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

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * @author Akhil
 * 
 */
public class RemoveCmpEnvVariables extends AnkushTask {

	private String componentName;

	/** The file path. */
	private String filePath;

	/**
	 * Instantiates a new remove env variable.
	 * 
	 * @param password
	 *            the password
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @param filePath
	 *            the file path
	 */
	public RemoveCmpEnvVariables(String componentName, String filePath) {
		this.componentName = componentName;
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		String startPattern = Constant.Strings.TEXT_COMMENT_START
				+ this.componentName;
		String endPattern = Constant.Strings.TEXT_COMMENT_END
				+ this.componentName;
		AnkushTask task = new RemoveTextBetweenPatterns(startPattern,
				endPattern, filePath);
		return task.getCommand();
	}
}
