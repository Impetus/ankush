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
package com.impetus.ankush.common.framework.config;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The Class ExtendedConfiguration.
 * 
 * @author nikunj
 */
public class ExtendedConfiguration implements Configuration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The username. */
	private String username;

	/** The private key. */
	private String privateKey;

	/** The password. */
	private String password;
	/* Component Name */
	/** The cluster id. */
	private Long clusterId = null;

	/** The operation id. */
	private Long operationId = null;

	/** The state. */
	private String state;

	/**
	 * Instantiates a new extended configuration.
	 */
	public ExtendedConfiguration() {
		super();
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the private key.
	 * 
	 * @return the privateKey
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * Sets the private key.
	 * 
	 * @param privateKey
	 *            the privateKey to set
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * Gets the cluster id.
	 * 
	 * @return the clusterId
	 */
	@JsonIgnore
	public Long getClusterId() {
		return clusterId;
	}

	/**
	 * Sets the cluster id.
	 * 
	 * @param clusterId
	 *            the clusterId to set
	 */
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	/**
	 * Gets the operation id.
	 * 
	 * @return the operationId
	 */
	@JsonIgnore
	public Long getOperationId() {
		return operationId;
	}

	/**
	 * Sets the operation id.
	 * 
	 * @param operationId
	 *            the operationId to set
	 */
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

}
