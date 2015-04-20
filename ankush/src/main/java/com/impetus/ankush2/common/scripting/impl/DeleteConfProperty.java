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
package com.impetus.ankush2.common.scripting.impl;

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush2.agent.AgentUtils;

/**
 * The Class DeleteConfProperty.
 * 
 * @author mayur
 */
public class DeleteConfProperty extends AnkushTask {

	/** The property name. */
	private String propertyName = null;

	/** The xml path. */
	private String filePath = null;

	/** The typeOfFile. */
	private String typeOfFile = null;

	private String agentInstallDir;

	/**
	 * Instantiates a new delete conf property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @param filePath
	 *            the file path
	 */
	public DeleteConfProperty(String propertyName, String filePath,
			String typeOfFile) {
		this.propertyName = propertyName;
		this.filePath = filePath;
		this.typeOfFile = typeOfFile;
	}

	public DeleteConfProperty(String propertyName, String filePath,
			String typeOfFile, String agentInstallDir) {
		this.propertyName = propertyName;
		this.filePath = filePath;
		this.typeOfFile = typeOfFile;
		this.agentInstallDir = agentInstallDir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	// @Override
	// public String getCommand() {
	// String deletePropCommand =
	// "java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar"
	// + " com.impetus.ankush.agent.action.ActionHandler " +
	// Constant.File_Extension.CONFIG + " " + typeOfFile + " ";
	//
	// StringBuilder sb = new StringBuilder(deletePropCommand);
	// sb.append("delete ");
	// sb.append(propertyName);
	// sb.append(" ");
	// sb.append(filePath);
	// return sb.toString();
	// }

	@Override
	public String getCommand() {
		StringBuilder sb = new StringBuilder(
				AgentUtils.getActionHandlerCommand(agentInstallDir));
		sb.append(Constant.File_Extension.CONFIG).append(" ")
				.append(this.typeOfFile).append(" ").append("delete ")
				.append(propertyName).append(" ").append(filePath);
		return sb.toString();
	}

}
