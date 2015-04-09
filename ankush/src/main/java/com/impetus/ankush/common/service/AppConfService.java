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
package com.impetus.ankush.common.service;

import java.util.Enumeration;
import java.util.Map;

import com.impetus.ankush.common.mail.MailConf;
import com.impetus.ankush.common.service.impl.AnkushApplicationConf;

/**
 * The Interface AppConfService.
 */
public interface AppConfService {

	/**
	 * Manage common configuration.
	 * 
	 * @param commonConf
	 *            the common conf
	 * @param unauthenticated
	 *            the unauthenticated
	 * @return the map
	 */
	Map manageCommonConfiguration(AnkushApplicationConf commonConf,
			boolean unauthenticated);

	/**
	 * Gets the common configuration.
	 * 
	 * @param unauthenticated
	 *            the unauthenticated
	 * @return the common configuration
	 */
	Map getCommonConfiguration(boolean unauthenticated);

	/**
	 * Gets the app conf.
	 * 
	 * @param key
	 *            the key
	 * @return the app conf
	 */
	Map getAppConf(String key);

	/**
	 * Sets the app conf.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	boolean setAppConf(String key, Object value);

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	String getState();

	/**
	 * Gets the app access public url.
	 * 
	 * @return the app access public url
	 */
	String getAppAccessPublicURL();

	/**
	 * Gets the app conf.
	 * 
	 * @param keys
	 *            the keys
	 * @return the app conf
	 */
	Map getAppConf(Enumeration keys);

	/**
	 * Gets the metadata.
	 * 
	 * @param file
	 *            the file
	 * @return the metadata
	 */
	Map getMetadata(String file);

	/**
	 * Method to set Host Address and Port.
	 */
	void setDefaultHostAddress();

	/**
	 * Test mail conf
	 */
	Map testMailConf(MailConf mailConf, String toUser);

	/**
	 * Update mail conf verification status
	 */
	Map updateMailConfVerificationStatus(String toUser);
}
