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
 * @author Akhil
 * 
 */
public class AppendFileUsingSed extends AnkushTask {

	/** The line. */
	private String line = null;

	/** The file path. */
	private String filePath = null;
	private String password = null;

	/**
	 * Instantiates a new append file.
	 * 
	 * @param line
	 *            the line
	 * @param filePath
	 *            the file path
	 */
	public AppendFileUsingSed(String line, String filePath) {
		this.line = line;
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		// Need to handle scenario for empty file
		return "sed -i '$ a\'\""+ this.line.replace("\"", "\\\"") + "\" " + this.filePath;
//		return "sed -i \"$ a" + this.line.replace("\"", "\\\"") + "\" "
//				+ this.filePath;
	}
}
