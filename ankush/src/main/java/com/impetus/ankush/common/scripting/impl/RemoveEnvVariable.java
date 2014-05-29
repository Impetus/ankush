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

import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * @author Akhil
 *
 */
public class RemoveEnvVariable extends AnkushTask {

	/** The password. */
	private String password = null;
	
	/** The name. */
	private String name = null;

	/** The value. */
	private String value = null;
	
	/** The file path. */
	private String filePath = null;

	/**
	 * Instantiates a new remove env variable.
	 *
	 * @param password the password
	 * @param name the name
	 * @param value the value
	 * @param filePath the file path
	 */
	public RemoveEnvVariable( String password, String name, String value, String filePath) {
		this.password = password;
		this.name = name;
		this.value = value;
		this.filePath = filePath;
	}
	
	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		String removeText = "export " + this.name + "=" + this.value;
		AnkushTask task = new RemoveText(removeText, this.filePath, this.password);
		return task.getCommand();
	}

}
