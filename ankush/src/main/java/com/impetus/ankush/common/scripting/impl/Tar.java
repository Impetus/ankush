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
 * The Class Tar.
 *
 * @author Vinay
 */
public class Tar extends AnkushTask {
	
	/** The tar file path. */
	private String tarFilePath = null;
	
	/** The destination. */
	private String sourceFolder = null;
	
	private String baseFolder = null;
	
	
	/**
	 * Instantiates a new tar.
	 *
	 * @param tarFilePath the tar file path
	 * @param sourceFolder
	 */
	public Tar(String tarFilePath, String sourceFolder, String baseFolder) {
		this.tarFilePath = tarFilePath;
		this.sourceFolder = sourceFolder;
		this.baseFolder = baseFolder;
	}

	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		return "tar -C " + this.baseFolder + " -cf " + this.tarFilePath + " " + this.sourceFolder;
	}
}
