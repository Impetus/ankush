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

import java.io.IOException;
import java.net.Socket;

import com.impetus.ankush.common.exception.AnkushException;

/**
 * The Class PortValidator.
 * 
 * @author nikunj
 */
public class PortValidator implements Validator {

	/** The host. */
	private String host;

	/** The ports. */
	private String ports;

	/** The err msg. */
	private String errMsg;

	/**
	 * The minimum number of server port number.
	 */
	public static final int MIN_PORT_NUMBER = 1;

	/**
	 * The maximum number of server port number.
	 */
	public static final int MAX_PORT_NUMBER = 49151;

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
	 * @param host
	 *            the host
	 * @param ports
	 *            the ports
	 */
	public PortValidator(String host, String ports) {
		this.host = host;
		this.ports = ports;
		this.errMsg = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.utils.validator.Validator#validate()
	 */
	public boolean validate() {
		Socket socket = null;
		try {
			Integer port = Integer.valueOf(ports);
			if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
				throw new AnkushException("Invalid port: " + ports);
			}
			socket = new Socket(host, port);
			errMsg = "Port " + port + "is already in use. Please check.";
			// if socket connection created, then port is not free
			return false;
		} catch (AnkushException e) {
			errMsg = e.getMessage();
			return false;
		} catch (Exception e) {
			errMsg = e.getMessage();
			return true;
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
