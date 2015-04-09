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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.impetus.ankush.common.constant.Constant;

/**
 * The Class ClusterConf.
 * 
 * @author hokam
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class ClusterConf extends ExtendedConfiguration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The errors. */
	protected Map<String, String> errors = new HashMap<String, String>();

	/** The current user. */
	private String currentUser;

	/** The cluster name. */
	protected String clusterName;

	/** The environment. */
	protected String environment = Constant.Cluster.Environment.IN_PREMISE;

	/** The ganglia master. */
	protected NodeConf gangliaMaster;
	
	/** The ip pattern. */
	protected String ipPattern;

	/** The pattern file. */
	protected String patternFile;

	/** The technology. */
	protected String technology;

	/** The java conf. */
	private JavaConf javaConf;
	
	/** The common components. */
	private Map<String, GenericConfiguration> commonComponents;
	
	private boolean registerableCluster = false; 
	
	/**
	 * Gets the cluster components.
	 *
	 * @return the clusterComponents
	 */
	@JsonIgnore
	public abstract Map<String, GenericConfiguration> getClusterComponents();

	/**
	 * Gets the errors.
	 * 
	 * @return the errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 * 
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	/**
	 * Gets the node count.
	 * 
	 * @return the node count
	 */
	@JsonIgnore
	public abstract int getNodeCount();

	/**
	 * Gets the node confs.
	 * 
	 * @return the node confs
	 */
	@JsonIgnore
	public abstract List<NodeConf> getNodeConfs();

	/**
	 * Sets the current user.
	 * 
	 * @param currentUser
	 *            the currentUser to set
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Gets the current user.
	 * 
	 * @return the currentUser
	 */
	@JsonIgnore
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * Gets the cluster name.
	 * 
	 * @return the clusterName
	 */
	public String getClusterName() {
		return clusterName;
	}

	/**
	 * Sets the cluster name.
	 * 
	 * @param clusterName
	 *            the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * Adds the error.
	 * 
	 * @param key
	 *            the key
	 * @param error
	 *            the error to set
	 */
	public void addError(String key, String error) {
		this.errors.put(key, error);
	}

	/**
	 * Gets the environment.
	 * 
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * Sets the environment.
	 * 
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * Checks if is state error.
	 * 
	 * @return true, if is state error
	 */
	@JsonIgnore
	public boolean isStateError() {
		return this.getState().equals(Constant.Cluster.State.ERROR);
	}

	/**
	 * Checks if is local.
	 * 
	 * @return true if Local cluster else false
	 */
	@JsonIgnore
	public boolean isLocal() {
		return this.getEnvironment().equals(
				Constant.Cluster.Environment.IN_PREMISE);
	}

	/**
	 * Gets the ganglia master.
	 * 
	 * @return the gangliaMaster
	 */
	@JsonIgnore
	public NodeConf getGangliaMaster() {
		return gangliaMaster;
	}

	/**
	 * Sets the ganglia master.
	 * 
	 * @param gangliaMaster
	 *            the gangliaMaster to set
	 */
	public void setGangliaMaster(NodeConf gangliaMaster) {
		this.gangliaMaster = gangliaMaster;
	}

	/**
	 * Gets the ip pattern.
	 * 
	 * @return the ipPattern
	 */
	public String getIpPattern() {
		return ipPattern;
	}

	/**
	 * Sets the ip pattern.
	 * 
	 * @param ipPattern
	 *            the ipPattern to set
	 */
	public void setIpPattern(String ipPattern) {
		this.ipPattern = ipPattern;
	}

	/**
	 * Gets the pattern file.
	 * 
	 * @return the patternFile
	 */
	public String getPatternFile() {
		return patternFile;
	}

	/**
	 * Sets the pattern file.
	 * 
	 * @param patternFile
	 *            the patternFile to set
	 */
	public void setPatternFile(String patternFile) {
		this.patternFile = patternFile;
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

	/**
	 * Gets the technology.
	 * 
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 * 
	 * @param technology
	 *            the technology to set
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Sets the java conf.
	 * 
	 * @param javaConf
	 *            the javaConf to set
	 */
	public void setJavaConf(JavaConf javaConf) {
		this.javaConf = javaConf;
	}

	/**
	 * Gets the java conf.
	 * 
	 * @return the javaConf
	 */
	public JavaConf getJavaConf() {
		return javaConf;
	}
	
	/**
	 * Gets the common components.
	 *
	 * @return the commonComponents
	 */
	@JsonIgnore
	public Map<String, GenericConfiguration> getCommonComponents() {
		return commonComponents;
	}

	/**
	 * Sets the common components.
	 *
	 * @param commonComponents the commonComponents to set
	 */
	@JsonIgnore
	public void setCommonComponents(
			Map<String, GenericConfiguration> commonComponents) {
		this.commonComponents = commonComponents;
	}

	/**
	 * @return the registerableCluster
	 */
	@JsonIgnore
	public boolean isRegisterableCluster() {
		return registerableCluster;
	}

	/**
	 * @param registerableCluster the registerableCluster to set
	 */
	public void setRegisterableCluster(boolean registerableCluster) {
		this.registerableCluster = registerableCluster;
	}
}
