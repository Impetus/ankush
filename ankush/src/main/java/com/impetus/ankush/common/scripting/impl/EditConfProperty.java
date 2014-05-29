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

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * The Class EditConfProperty.
 *
 * @author mayur
 */
public class EditConfProperty extends AnkushTask {

	/** The property name. */
	private String propertyName = null;

	/** The new value. */
	private String newValue = null;

	/** The xml path. */
	private String filePath = null;
	
	/** The typeOfFile. */
	private String typeOfFile = null;

	/**
	 * Instantiates a new edits the conf property.
	 *
	 * @param propertyName the property name
	 * @param newValue the new value
	 * @param filePath the file path
	 */
	public EditConfProperty(String propertyName, String newValue, String filePath,String typeOfFile) {
		this.propertyName = propertyName;
		this.newValue = newValue;
		this.filePath = filePath;
		this.typeOfFile = typeOfFile;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		String startCommand = "java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar"
				+ " com.impetus.ankush.agent.action.ActionHandler " +  Constant.File_Extension.CONFIG + " " + typeOfFile + " " ;

		StringBuilder sb = new StringBuilder(startCommand);
		sb.append("edit ");
		sb.append("\"" + propertyName + "\"");
		sb.append(" ");
		sb.append("\"" + newValue + "\"");
		sb.append(" ");
		sb.append(filePath);
		return sb.toString();
	}

}
