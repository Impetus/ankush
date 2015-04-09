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
import com.impetus.ankush2.constant.Constant.Strings;

/**
 * @author hokam
 * 
 */
public class AgentAction extends AnkushTask {

	/** The commands. */
	private String[] args = null;

	/**
	 * @param args
	 */
	public AgentAction(String... args) {
		super();
		this.args = args;
	}

	@Override
	public String getCommand() {
		// String command buffer
		StringBuffer command = new StringBuffer();
		// setting path to agent action.
		command.append("sh $HOME/.ankush/agent/bin/agent-action.sh ");
		// iterating over the args to pass it to script.
		for (String arg : args) {
			// setting argument and space in command.
			command.append(arg).append(Strings.SPACE);
		}
		// returning command.
		return command.toString();
	}
}
