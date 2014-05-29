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

import java.util.List;

import com.impetus.ankush.common.framework.config.ErrorInfo;

/**
 * The Class ResponseWrapper.
 *
 * @param <T> the generic type
 * @author hokam
 */
public class ResponseWrapper<T> {
	
	/** The output. */
	private T output;
	
	/** The status. */
	private String status;
	
	/** The description. */
	private String description;
	
	/** The errors. */
	private List<ErrorInfo> errors;
	
	/**
	 * Instantiates a new response wrapper.
	 */
	public ResponseWrapper() {
	}
	
	/**
	 * Instantiates a new response wrapper.
	 *
	 * @param output the output
	 * @param status the status
	 * @param description the description
	 * @param errors the errors
	 */
	public ResponseWrapper(T output, String status, String description, List<ErrorInfo> errors) {
		this.output = output;
		this.status = status;
		this.description = description;
		this.errors = errors;
	}

	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	public T getOutput() {
		return output;
	}
	
	/**
	 * Sets the output.
	 *
	 * @param output the new output
	 */
	public void setOutput(T output) {
		this.output = output;
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
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public List<ErrorInfo> getErrors() {
		return errors;
	}
	
	/**
	 * Sets the errors.
	 *
	 * @param errors the new errors
	 */
	public void setErrors(List<ErrorInfo> errors) {
		this.errors = errors;
	}
	
	
}
