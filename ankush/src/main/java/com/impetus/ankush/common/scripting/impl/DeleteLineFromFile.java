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

import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * The Class DeleteLineFromFile.
 */
public class DeleteLineFromFile extends AnkushTask {
	
	/** The delete pattern. */
	private String deletePattern;
	
	/** The file path. */
	private String filePath;
	/** The password. */
	private String password = null;

	/** The flag for creating BackUp File. */
	private boolean createBackUpFile = true;

	/**
	 * Instantiates a new delete line from file.
	 *
	 * @param deletePattern the delete pattern
	 * @param filePath the file path
	 * @param password the password
	 * @param createBackUpFile the create back up file
	 */
	public DeleteLineFromFile(String deletePattern, String filePath,
			String password, boolean createBackUpFile) {
		super();
		this.deletePattern = deletePattern;
		this.filePath = filePath;
		this.password = password;
		this.createBackUpFile = createBackUpFile;
	}

	/**
	 * Instantiates a new delete line from file.
	 *
	 * @param deletePattern the delete pattern
	 * @param filePath the file path
	 */
	public DeleteLineFromFile(String deletePattern, String filePath) {
		this(deletePattern, filePath, null, false);
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		StringBuilder sb = new StringBuilder();
		String echoPass = "";
		if (this.password != null) {
			echoPass = "echo '" + this.password + "' | sudo -S ";
		}
		sb.append(echoPass);

		if (this.createBackUpFile) {
			sb.append("sed -i.bak /'" + this.deletePattern + "'/d "
					+ this.filePath);
		} else {
			sb.append("sed -i '/" + this.deletePattern + "/'d " + this.filePath);
		}

		sb.append(";");
		return sb.toString();
	}

}
