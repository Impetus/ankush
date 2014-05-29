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
package com.impetus.ankush.common.exception;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.http.HttpStatus;

/**
 * The Class ControllerException.
 */
@JsonAutoDetect(value=JsonMethod.NONE)
public class ControllerException extends RuntimeException {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 919572865155030066L;
	
	/** The status. */
	private final HttpStatus status;
	
	/** The error id. */
	private final String errorId;

	/**
	 * Instantiates a new controller exception.
	 *
	 * @param status the status
	 * @param errorId the error id
	 * @param message the message
	 */
	public ControllerException(HttpStatus status, String errorId, String message) {
		super(message);
		this.status = status;
		this.errorId = errorId;
	}

	/**
	 * Instantiates a new controller exception.
	 *
	 * @param status the status
	 * @param errorId the error id
	 * @param message the message
	 * @param cause the cause
	 */
	public ControllerException(HttpStatus status, String errorId, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
		this.errorId = errorId;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	@JsonProperty
	public HttpStatus getStatus() {
		return status;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@JsonProperty
	@Override
	public String getMessage() {
		return super.getMessage();
	}

	/**
	 * Gets the error id.
	 *
	 * @return the error id
	 */
	@JsonProperty
	public String getErrorId() {
		return errorId;
	}
}
