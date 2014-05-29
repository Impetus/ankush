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
package com.impetus.ankush.agent.utils;

import java.io.Serializable;
import java.util.Map;

/**
 * The Class TileInfo.
 * 
 * @author hokam
 */
public class TileInfo {

	/** The line1. */
	private String line1;

	/** The line2. */
	private String line2;

	/** The line3. */
	private String line3;

	/** The data. */
	private Map<String, Object> data;

	/** The status. */
	private String status;

	/** The url. */
	private String url;

	/**
	 * Gets the line1.
	 * 
	 * @return the line1
	 */
	public String getLine1() {
		return line1;
	}

	/**
	 * Sets the line1.
	 * 
	 * @param line1
	 *            the line1 to set
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}

	/**
	 * Gets the line2.
	 * 
	 * @return the line2
	 */
	public String getLine2() {
		return line2;
	}

	/**
	 * Sets the line2.
	 * 
	 * @param line2
	 *            the line2 to set
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}

	/**
	 * Gets the line3.
	 * 
	 * @return the line3
	 */
	public String getLine3() {
		return line3;
	}

	/**
	 * Sets the line3.
	 * 
	 * @param line3
	 *            the line3 to set
	 */
	public void setLine3(String line3) {
		this.line3 = line3;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the graphData
	 */
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
