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
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

/**
 * The Class NTPServerValidator.
 *
 * @author nikunj
 */
public class NTPServerValidator implements Validator {
	
	/** The host. */
	private String host;
	
	/** The err msg. */
	private String errMsg;

	/** The Constant NTP_CLIENT_TIMEOUT. */
	private static final int NTP_CLIENT_TIMEOUT = 10000;

	/**
	 * Instantiates a new nTP server validator.
	 *
	 * @param host the host
	 */
	public NTPServerValidator(String host) {
		super();
		this.host = host;
		this.errMsg = null;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.utils.validator.Validator#validate()
	 */
	@Override
	public boolean validate() {
		NTPUDPClient client = new NTPUDPClient();
		client.setDefaultTimeout(NTP_CLIENT_TIMEOUT);
		try {
			TimeInfo timeInfo = client.getTime(InetAddress.getByName(host));
			timeInfo.computeDetails();
			return timeInfo.getOffset() != null;
		} catch (SocketException e) {
			errMsg = e.getMessage();
		} catch (UnknownHostException e) {
			errMsg = e.getMessage();
		} catch (IOException e) {
			errMsg = e.getMessage();
		} catch (Exception e) {
			errMsg = e.getMessage();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.utils.validator.Validator#getErrMsg()
	 */
	@Override
	public String getErrMsg() {
		return errMsg;
	}

}
