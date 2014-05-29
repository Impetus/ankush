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

import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * The Class Unzip.
 *
 * @author mayur
 */
public class Unzip extends AnkushTask {

	/** The zip file path. */
	private String zipFilePath = null;
	
	/** The destination. */
	private String destination = null;

	/**
	 * Instantiates a new unzip.
	 *
	 * @param zipFilePath the zip file path
	 * @param destination the destination
	 */
	public Unzip(String zipFilePath, String destination) {
		this.zipFilePath = zipFilePath;
		this.destination = destination;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		return "unzip -q -o " + this.zipFilePath + " -d " + this.destination;
	}

}
