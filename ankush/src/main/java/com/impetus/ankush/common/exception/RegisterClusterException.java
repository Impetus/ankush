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
package com.impetus.ankush.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akhil
 *
 */
public class RegisterClusterException extends AnkushException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private static final String GENERIC_ERR_MSG = "Excpetion while registering an existing cluster.";
	
	List<String> errorMessages;
	
	/**
	 * @return the errorMessages
	 */
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	public RegisterClusterException() {
		super(RegisterClusterException.GENERIC_ERR_MSG);
	}
	
	public RegisterClusterException(String errMsg) {
		super(RegisterClusterException.GENERIC_ERR_MSG);
		this.errorMessages = new ArrayList<String>(); 
		this.errorMessages.add(errMsg);
	}
	
	public RegisterClusterException(List<String> errorMessages) {
		super(RegisterClusterException.GENERIC_ERR_MSG);
		this.errorMessages = errorMessages;
	}
	
	public void addErrorMessage(String message) {
		if(this.errorMessages == null) {
			this.errorMessages = new ArrayList<String>();	
		}
		this.errorMessages.add(message);
	}
}
