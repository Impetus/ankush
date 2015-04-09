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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.impetus.ankush.common.utils.JsonMapperUtil;

/**
 * @author Akhil
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class RegisterClusterConf implements Configuration {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** component json map **/
	private LinkedHashMap<String, HashMap> components = new LinkedHashMap<String, HashMap>();

	/** The cluster name. */
	protected String clusterName;

	/** The technology. */
	protected String technology;

	/** The username. */
	private String username;

	/** The private key. */
	private String privateKey;

	/** The password. */
	private String password;

	private String javaHomePath;

	private String environment;

	/**
	 * @return the components
	 */
	public LinkedHashMap<String, HashMap> getComponents() {
		return components;
	}

	/**
	 * @param technologies
	 *            the technologies to set
	 */
	public void setComponents(LinkedHashMap<String, HashMap> components) {
		this.components = components;
	}

	/**
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}

	/**
	 * @param clusterName
	 *            the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * @param technology
	 *            the technology to set
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the privateKey
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey
	 *            the privateKey to set
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the javaHomePath
	 */
	public String getJavaHomePath() {
		return javaHomePath;
	}

	/**
	 * @param javaHomePath
	 *            the javaHomePath to set
	 */
	public void setJavaHomePath(String javaHomePath) {
		this.javaHomePath = javaHomePath;
	}

	/**
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * Checks if is auth type password.
	 * 
	 * @return true, if is auth type password
	 */
	@JsonIgnore
	public boolean isAuthTypePassword() {
		String password = getPassword();
		if (password != null && !password.isEmpty()) {
			return true;
		}
		return false;
	}

}
