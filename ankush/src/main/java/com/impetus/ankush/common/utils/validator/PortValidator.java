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
package com.impetus.ankush.common.utils.validator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.impetus.ankush.common.utils.CommandExecutor;

/**
 * The Class PortValidator.
 *
 * @author nikunj
 */
public class PortValidator implements Validator{
	
	/** The host. */
	private String host;
	
	/** The ports. */
	private String ports;
	
	/** The err msg. */
	private String errMsg;

	/**
	 * Gets the err msg.
	 *
	 * @return the errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * Instantiates a new port validator.
	 *
	 * @param host the host
	 * @param ports the ports
	 */
	public PortValidator(String host, String ports) {
		this.host = host;
		this.ports = ports;
		this.errMsg = null;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.utils.validator.Validator#validate()
	 */
	public boolean validate() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String command = "nmap -p " + ports + " " + host;
		try {
			CommandExecutor.exec(command, baos, null);
			String result = baos.toString();
			if (result.contains("filtered")) {
				errMsg = ports
						+ " is/are blocked. Please check firewall settings";
			} else if (result.contains("open")) {
				errMsg = ports + " is in use. Please select other port(s).";
			} else {
				return true;
			}
		} catch (IOException e) {
			errMsg = e.getMessage();
		} catch (InterruptedException e) {
			errMsg = e.getMessage();
		} catch (Exception e){
			errMsg = e.getMessage();
		}
		return false;
	}
}
