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
package com.impetus.ankush.common.framework.config;

/**
 * The Class ErrorInfo.
 * 
 * @author hokam
 */
public class ErrorInfo {

	/** The error id. */
	private String errorId;

	/** The error desc. */
	private String errorDesc;

	/**
	 * Gets the error id.
	 * 
	 * @return the error id
	 */
	public String getErrorId() {
		return errorId;
	}

	/**
	 * Sets the error id.
	 * 
	 * @param errorId
	 *            the new error id
	 */
	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	/**
	 * Gets the error desc.
	 * 
	 * @return the error desc
	 */
	public String getErrorDesc() {
		return errorDesc;
	}

	/**
	 * Sets the error desc.
	 * 
	 * @param errorDesc
	 *            the new error desc
	 */
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

}
