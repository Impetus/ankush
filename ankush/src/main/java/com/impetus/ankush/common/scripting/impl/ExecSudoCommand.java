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

import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * The Class ExecSudoCommand.
 * 
 * @author mayur
 */
public class ExecSudoCommand extends AnkushTask {

	/** The commands. */
	private String[] commands = null;

	/** The password. */
	private String password = null;

	/** The flagUseSudoOption. */
	private boolean flagUseSudoOption = true;

	/** The flagRunInBackGround. */
	private boolean flagRunInBackGround = false;

	/**
	 * Instantiates a new exec sudo command.
	 * 
	 * @param password
	 *            the password
	 * @param commands
	 *            the commands
	 * @param
	 */
	public ExecSudoCommand(String password, boolean flagUseSudoOption,
			boolean flagRunInBackGround, String... commands) {
		this.password = password;
		this.commands = commands;
		this.flagUseSudoOption = flagUseSudoOption;
		this.flagRunInBackGround = flagRunInBackGround;
	}

	/**
	 * Instantiates a new exec sudo command.
	 * 
	 * @param password
	 *            the password
	 * @param commands
	 *            the commands
	 */
	public ExecSudoCommand(String password, String... commands) {
		this.password = password;
		this.commands = commands;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		String cmdList = new String();
		for (final String command : this.commands) {
			if (this.flagUseSudoOption) {
				if(this.password != null) {
					cmdList = cmdList + "echo \"" + this.password + "\" | sudo -S "
							+ command;	
				} else {
					cmdList = "sudo -S " + command;	
				}
			} else {
				cmdList = cmdList + command;
			}
			if (this.flagRunInBackGround) {
				cmdList += " &";
			}
			cmdList += CustomTask.DELIMETER;
		}
		cmdList = (cmdList.length() == 0 ? "" : cmdList.substring(0,
				cmdList.length() - 1));
		return cmdList;
	}

}
