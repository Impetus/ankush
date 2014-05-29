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
 * The Class UrlExists.
 *
 * @author hokam
 */
public class UrlExists extends AnkushTask {

	/** The url. */
	private String url = null;
	
	/**
	 * Instantiates a new url exists.
	 *
	 * @param url the url
	 */
	public UrlExists(String url) {
		this.url = url;
	}
	
	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		int retryCount = 5;
		int timeOut = 3;
		StringBuilder cmnd = new StringBuilder();
		cmnd.append("wget");
		cmnd.append(" --no-check-certificate");
		cmnd.append(" -t ").append(retryCount);
		cmnd.append(" -q");
		cmnd.append(" --timeout=").append(timeOut);
		cmnd.append(" --spider ").append(url);
		return cmnd.toString();
	}

}
