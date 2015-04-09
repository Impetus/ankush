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
 * The Class ValidatePortUsage.
 */
public class ValidatePortUsage extends AnkushTask {

	/** The port. */
	private int port;

	/**
	 * Instantiates a new check port open.
	 *
	 * @param port the port
	 */
	public ValidatePortUsage(int port) {
		super();
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		return "/sbin/fuser -n tcp " + port;
	}
}
