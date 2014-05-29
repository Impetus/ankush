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
 * The Class RemoveText.
 * 
 * @author mayur
 */
public class RemoveText extends AnkushTask {

	/** The file. */
	private String file = null;

	/** The lines. */
	private List<String> lines = new ArrayList<String>();

	/** The password. */
	private String password = null;

	/**
	 * Instantiates a new removes the text.
	 * 
	 * @param pattern
	 *            the pattern
	 * @param filePath
	 *            the file path
	 */
	public RemoveText(String pattern, String filePath) {
		this.lines.add(pattern);
		this.file = filePath;
	}

	/**
	 * Instantiates a new removes the text.
	 * 
	 * @param pattern
	 *            the pattern
	 * @param filePath
	 *            the file path
	 */
	public RemoveText(String pattern, String filePath, String password) {
		this.lines.add(pattern);
		this.file = filePath;
		this.password = password;
	}

	/**
	 * Instantiates a new removes the text.
	 * 
	 * @param contents
	 *            the contents
	 * @param file
	 *            the file
	 */
	public RemoveText(ArrayList<String> contents, String file) {
		this.lines = contents;
		this.file = file;
	}

	/**
	 * Instantiates a new removes the text.
	 * 
	 * @param contents
	 *            the contents
	 * @param file
	 *            the file
	 * @param password
	 *            the password
	 */
	public RemoveText(List<String> contents, String file, String password) {
		this.lines = contents;
		this.file = file;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		StringBuilder sb = new StringBuilder();
		
		for (String line : this.lines) {
			String prependSudo = "";
			if (password != null) {
				prependSudo = "echo '" + password + "' | sudo -S ";
			} else {
				prependSudo = "sudo -S ";
			}
			sb.append(prependSudo);
			
			sb.append("sed -i '/"
					+ line.replace("/", "\\/").replace("*", "\\*") + "/d' "
					+ this.file);
			sb.append(";");
		}
		return sb.toString();
	}

}
