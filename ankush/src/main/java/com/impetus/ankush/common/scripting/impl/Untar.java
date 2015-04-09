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
 * The Class Untar.
 *
 * @author mayur
 */
public class Untar extends AnkushTask {
	
	/** The tar file path. */
	private String tarFilePath = null;
	
	/** The destination. */
	private String destination = null;
	
	private boolean appendStripComponentsFlag = true;
	
	/**
	 * Instantiates a new untar.
	 *
	 * @param tarFilePath the tar file path
	 * @param destination the destination
	 */
	public Untar(String tarFilePath, String destination) {
		this.tarFilePath = tarFilePath;
		this.destination = destination;
	}
	
	public Untar(String tarFilePath, String location, boolean appendStripComponentsFlag) {
		this.tarFilePath = tarFilePath;
		this.destination = location;
		this.appendStripComponentsFlag = appendStripComponentsFlag;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		if(this.appendStripComponentsFlag) {
			return "tar -C " + this.destination + " -xf " + this.tarFilePath + " --strip-components=1";
		} else {
			return "tar -C " + this.destination + " -xf " + this.tarFilePath;	
		}
	}

}
