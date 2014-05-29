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
 * The Class AddConfProperty.
 *
 * @author mayur
 */
public class AddConfProperty extends AnkushTask {

	/** The name. */
	private String name = null;

	/** The value. */
	private String value = null;

	/** The xml path. */
	private String filePath = null;
	
	/** The typeOfFile. */
	private String typeOfFile = null;

	/**
	 * Instantiates a new adds the conf property.
	 *
	 * @param name the name
	 * @param value the value
	 * @param filePath the file path
	 */
	public AddConfProperty(String name, String value, String filePath,String typeOfFile) {
		this.name = name;
		this.value = value;
		this.filePath = filePath;
		this.typeOfFile = typeOfFile;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {

		String startCommand = "java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar"
				+ " com.impetus.ankush.agent.action.ActionHandler "+ Constant.File_Extension.CONFIG + " " +this.typeOfFile + " ";

		StringBuilder sb = new StringBuilder(startCommand);
		sb.append("add ");
		sb.append("\"" + name + "\"");
		sb.append(" ");
		sb.append("\"" + value + "\"");
		sb.append(" ");
		sb.append(filePath);

		return sb.toString();
	}
	
}
