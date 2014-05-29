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

import java.util.ArrayList;
import java.util.List;

import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * The Class PrependFile.
 *
 * @author mayur
 */
public class PrependFile extends AnkushTask {

	/** The file. */
	private String file = null;
	
	/** The lines. */
	private List<String> lines = new ArrayList<String>();
	
	/** The password. */
	private String password = null;

	/**
	 * Instantiates a new prepend file.
	 *
	 * @param content the content
	 * @param file the file
	 */
	public PrependFile(String content, String file) {
		this.lines.add(content);
		this.file = file;
	}

	/**
	 * Instantiates a new prepend file.
	 *
	 * @param contents the contents
	 * @param file the file
	 */
	public PrependFile(List<String> contents, String file) {
		this.lines = contents;
		this.file = file;
	}

	/**
	 * Instantiates a new prepend file.
	 *
	 * @param contents the contents
	 * @param file the file
	 * @param password the password
	 */
	public PrependFile(List<String> contents, String file, String password) {
		this.lines = contents;
		this.file = file;
		this.password = password;
		if (this.password == null) {
			this.password = "";
		}
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		StringBuilder sb = new StringBuilder();
		String echoPass = "";
		if (password != null) {
			echoPass = "echo '" + password + "' | sudo -S ";
		}
		for (String line : this.lines) {
			sb.append(echoPass);
			sb.append("sed -i 1i\"" + line + "\" " + this.file);
			sb.append(";");
		}
		return sb.toString();
	}

}
