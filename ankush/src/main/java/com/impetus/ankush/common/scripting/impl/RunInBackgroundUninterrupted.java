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

public class RunInBackgroundUninterrupted extends AnkushTask{
	

	/** The command. */
	private String command = null;
	
	/** The file path. */
	private String filePath = null;
	
	private static String SPAC_STR=" ";

	/**
	 * Instantiates a new run in background.
	 *
	 * @param command the command
	 */
	public RunInBackgroundUninterrupted(String command) {
		this.command = command;
		this.filePath = null;
	}

	/**
	 * Instantiates a new run in background.
	 *
	 * @param command the command
	 * @param filePath the file path
	 */
	public RunInBackgroundUninterrupted(String command, String filePath) {
		this.command = command;
		this.filePath = filePath;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		if (this.filePath != null) {
			return "nohup" + SPAC_STR + command + ">" +filePath +  " 2>"+ filePath + SPAC_STR +"&";
		}
		return "nohup" + SPAC_STR + command + ">nohup.out 2>nohup.out &";
	}

}
