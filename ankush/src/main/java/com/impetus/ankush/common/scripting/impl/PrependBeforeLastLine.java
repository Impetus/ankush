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
 * The Class PrependBeforeLastLine.
 * 
 * @author Monika
 */
public class PrependBeforeLastLine extends AnkushTask {

	/** The text. */
	private String text = null;

	/** The file path. */
	private String filePath = null;

	/**
	 * Instantiates a new prepend before last line.
	 * 
	 * @param text
	 *            the text
	 * @param filePath
	 *            the file path
	 */
	public PrependBeforeLastLine(String text, String filePath) {
		this.text = text;
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		final StringBuilder sb = new StringBuilder();
		sb.append("sed -i '$i" + this.text + "' " + this.filePath);
		sb.append(";");
		return sb.toString();
	}

}
