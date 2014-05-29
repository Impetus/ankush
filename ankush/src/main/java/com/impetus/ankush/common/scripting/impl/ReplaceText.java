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

import org.apache.commons.lang3.StringUtils;

import com.impetus.ankush.common.scripting.AnkushTask;

// TODO: Auto-generated Javadoc
/**
 * The Class ReplaceText.
 * 
 * @author Akhil
 */
public class ReplaceText extends AnkushTask {

	/** The file path. */
	private String filePath = null;

	/** The Target Text. */
	private String targetText = "";

	/** The Replacement Text. */
	private String replacementText = "";

	/** The password. */
	private String password = null;

	/** The flag for creating BackUp File. */
	private boolean createBackUpFile = true;

	/**
	 * Instantiates a new ReplaceText object.
	 * 
	 * @param targetText
	 *            the targetText
	 * @param replacementText
	 *            the replacementText
	 * @param filePath
	 *            the file path
	 * @param createBackUpFile
	 *            the createBackUpFile
	 * @param password
	 *            the password
	 */
	public ReplaceText(String targetText, String replacementText,
			String filePath, boolean createBackUpFile, String password) {
		this.targetText = StringUtils.replace(targetText, "/", "\\/");
		this.replacementText = StringUtils.replace(replacementText, "/", "\\/");
		this.filePath = filePath;
		this.createBackUpFile = createBackUpFile;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {

		final StringBuilder sb = new StringBuilder();
		
		String prependSudo = "";
		if (this.password != null) {
			prependSudo = "echo '" + this.password + "' | sudo -S ";
		} else {
			prependSudo = "sudo -S ";
		}
		sb.append(prependSudo);
		if (this.createBackUpFile) {
			sb.append("sed -i.bak s/'" + this.targetText + "'/'"
					+ this.replacementText + "'/g " + this.filePath);
		} else {
			sb.append("sed -i s/'" + this.targetText + "'/'"
					+ this.replacementText + "'/g " + this.filePath);
		}
		sb.append(";");
		return sb.toString();
	}
}
