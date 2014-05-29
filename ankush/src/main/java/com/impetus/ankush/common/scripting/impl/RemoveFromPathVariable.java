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
public class RemoveFromPathVariable extends AnkushTask {

	/** The path. */
	private String path = null;
	
	/** The file path. */
	private String filePath = null;
	
	/** The password. */
	private String password = null;

	/**
	 * Instantiates a new adds the to path variable.
	 *
	 * @param path the path
	 * @param filePath the file path
	 * @param password the password
	 */
	public RemoveFromPathVariable(String path, String filePath, String password) {
		this.path = path;	
		this.filePath = filePath;
		this.password = password;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		String removeText = "export PATH=" + this.path + ":$PATH";
		AnkushTask task = new RemoveText(removeText, this.filePath, this.password);
		return task.getCommand();
	}

}
