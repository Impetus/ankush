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
package com.impetus.ankush.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hokam
 * 
 */
public class HttpResult {
	private String output;
	private Boolean status;
	private List<String> errors = new ArrayList<String>();

	/**
	 * @param output
	 * @param status
	 * @param errors
	 */
	public HttpResult(String output, Boolean status, List<String> errors) {
		super();
		this.output = output;
		this.status = status;
		this.errors = errors;
	}

	/**
	 * @param output
	 * @param status
	 * @param error
	 */
	public HttpResult(String output, Boolean status, String error) {
		super();
		this.output = output;
		this.status = status;
		this.errors.add(error);
	}

	public HttpResult() {
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.errors.add(error);
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
}
