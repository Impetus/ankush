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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.utils.FileNameUtils;

/**
 * The Class GenericConfiguration.
 * 
 * @author mayur
 */
public class GenericConfiguration implements Configuration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The username. */
	private String username;

	/** The password. */
	private String password;

	/** The private key. */
	private String privateKey;

	/** The component vendor. */
	private String componentVendor;

	/** The component version. */
	private String componentVersion;

	/** The installation path. */
	private String installationPath;

	/** The local binary file. */
	private String localBinaryFile;

	/** The tarball url. */
	private String tarballUrl;

	/** The server tarball location. */
	private String serverTarballLocation;

	/** The component home. */
	private String componentHome;

	/** The advanced conf. */
	private Map<String, Object> advancedConf = new HashMap<String, Object>();

	/** The cluster name. */
	private String clusterName;

	/** The cluster db id. */
	private Long clusterDbId;

	/** The operation id. */
	private Long operationId;

	/** The current user. */
	private String currentUser;

	/** The certification. */
	private String certification;

	/** The cluster conf. */
	private ClusterConf clusterConf;

	/** The comp nodes. */
	private Set<NodeConf> compNodes = new HashSet<NodeConf>();

	/** New nodes */
	private List<NodeConf> newNodes = new ArrayList<NodeConf>();

	/** Configuration State - Default/-/Custom **/
	private String confState = new String();

	/**
	 * Gets the comp nodes.
	 * 
	 * @return the comp nodes
	 */
	public Set<NodeConf> getCompNodes() {
		return compNodes;
	}

	/**
	 * Instantiates a new generic configuration.
	 */
	public GenericConfiguration() {
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	@JsonIgnore
	public String getUsername() {
		if (clusterConf == null) {
			return username;
		}
		return clusterConf.getUsername();
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
	@JsonIgnore
	public String getPassword() {
		if (clusterConf == null) {
			return password;
		}
		return clusterConf.getPassword();
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
	 * Sets the component vendor.
	 * 
	 * @param componentVendor
	 *            the componentVendor to set
	 */
	public void setComponentVendor(String componentVendor) {
		this.componentVendor = componentVendor;
	}

	/**
	 * Gets the component vendor.
	 * 
	 * @return the componentVendor
	 */
	public String getComponentVendor() {
		return componentVendor;
	}

	/**
	 * Gets the component version.
	 * 
	 * @return the componentVersion
	 */
	public String getComponentVersion() {
		return componentVersion;
	}

	/**
	 * Sets the component version.
	 * 
	 * @param componentVersion
	 *            the componentVersion to set
	 */
	public void setComponentVersion(String componentVersion) {
		this.componentVersion = componentVersion;
	}

	/**
	 * Gets the installation path.
	 * 
	 * @return the installationPath
	 */
	public String getInstallationPath() {
		return installationPath;
	}

	/**
	 * Sets the installation path.
	 * 
	 * @param installationPath
	 *            the installationPath to set
	 */
	public void setInstallationPath(String installationPath) {
		this.installationPath = FileNameUtils
				.convertToValidPath(installationPath);
	}

	/**
	 * Gets the local binary file.
	 * 
	 * @return the localBinaryFile
	 */
	public String getLocalBinaryFile() {
		return localBinaryFile;
	}

	/**
	 * Sets the local binary file.
	 * 
	 * @param localBinaryFile
	 *            the localBinaryFile to set
	 */
	public void setLocalBinaryFile(String localBinaryFile) {
		this.localBinaryFile = localBinaryFile;
	}

	/**
	 * Gets the tarball url.
	 * 
	 * @return the tarballUrl
	 */
	public String getTarballUrl() {
		return tarballUrl;
	}

	/**
	 * Sets the tarball url.
	 * 
	 * @param tarballUrl
	 *            the tarballUrl to set
	 */
	public void setTarballUrl(String tarballUrl) {
		this.tarballUrl = tarballUrl;
	}

	/**
	 * Gets the server tarball location.
	 * 
	 * @return the serverTarballLocation
	 */
	public String getServerTarballLocation() {
		return serverTarballLocation;
	}

	/**
	 * Sets the server tarball location.
	 * 
	 * @param serverTarballLocation
	 *            the serverTarballLocation to set
	 */
	public void setServerTarballLocation(String serverTarballLocation) {
		this.serverTarballLocation = serverTarballLocation;
	}

	/**
	 * Gets the private key.
	 * 
	 * @return the privateKey
	 */
	@JsonIgnore
	public String getPrivateKey() {
		if (clusterConf == null) {
			return privateKey;
		}
		return clusterConf.getPrivateKey();
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
	 * Gets the component home.
	 * 
	 * @return the componentHome
	 */
	public String getComponentHome() {
		return componentHome;
	}

	/**
	 * Sets the component home.
	 * 
	 * @param componentHome
	 *            the componentHome to set
	 */
	public void setComponentHome(String componentHome) {
		this.componentHome = FileNameUtils.convertToValidPath(componentHome);
	}

	/**
	 * Sets the advanced conf.
	 * 
	 * @param advancedConf
	 *            the advanced conf
	 */
	public void setAdvancedConf(Map<String, Object> advancedConf) {
		this.advancedConf = advancedConf;
	}

	/**
	 * Gets the advanced conf.
	 * 
	 * @return the advanceConf
	 */
	public Map<String, Object> getAdvancedConf() {
		return advancedConf;
	}

	/**
	 * Gets the cluster name.
	 * 
	 * @return the clusterName
	 */
	@JsonIgnore
	public String getClusterName() {
		if (clusterConf == null) {
			return clusterName;
		}
		return clusterConf.getClusterName();
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
	 * Sets the cluster db id.
	 * 
	 * @param clusterDbId
	 *            the clusterDbId to set
	 */
	public void setClusterDbId(Long clusterDbId) {
		this.clusterDbId = clusterDbId;
	}

	/**
	 * Gets the cluster db id.
	 * 
	 * @return the clusterDbId
	 */
	@JsonIgnore
	public Long getClusterDbId() {
		if (clusterConf == null) {
			return clusterDbId;
		}
		return clusterConf.getClusterId();
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
	 * Gets the operation id.
	 * 
	 * @return the operationId
	 */
	@JsonIgnore
	public Long getOperationId() {
		if (clusterConf == null) {
			return operationId;
		}
		return clusterConf.getOperationId();
	}

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
		if (clusterConf == null) {
			return currentUser;
		}
		return clusterConf.getCurrentUser();
	}

	/**
	 * Gets the cluster conf.
	 * 
	 * @return the clusterConf
	 */
	@JsonIgnore
	public ClusterConf getClusterConf() {
		return clusterConf;
	}

	/**
	 * Sets the cluster conf.
	 * 
	 * @param clusterConf
	 *            the clusterConf to set
	 */
	public void setClusterConf(ClusterConf clusterConf) {
		this.clusterConf = clusterConf;
	}

	/**
	 * Sets the certification.
	 * 
	 * @param certification
	 *            the certification to set
	 */
	public void setCertification(String certification) {
		this.certification = certification;
	}

	/**
	 * Gets the certification.
	 * 
	 * @return the certification
	 */
	public String getCertification() {
		return certification;
	}

	/**
	 * @param newNodes
	 *            the newNodes to set
	 */
	public void setNewNodes(List<NodeConf> newNodes) {
		this.newNodes = newNodes;
	}

	/**
	 * @return the newNodes
	 */
	public List<NodeConf> getNewNodes() {
		return newNodes;
	}


	/**
	 * @param confState
	 *            the confState to set
	 */
	public void setConfState(String confState) {
		this.confState = confState;
	}

	/**
	 * @return the confState
	 */
	public String getConfState() {
		return confState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenericConfiguration [username=" + username + ", password="
				+ password + ", privateKey=" + privateKey
				+ ", componentVendor=" + componentVendor
				+ ", componentVersion=" + componentVersion
				+ ", installationPath=" + installationPath
				+ ", localBinaryFile=" + localBinaryFile + ", tarballUrl="
				+ tarballUrl + ", serverTarballLocation="
				+ serverTarballLocation + ", componentHome=" + componentHome
				+ ", advancedConf=" + advancedConf + ", clusterName="
				+ clusterName + ", getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword()
				+ ", getComponentVendor()=" + getComponentVendor()
				+ ", getComponentVersion()=" + getComponentVersion()
				+ ", getInstallationPath()=" + getInstallationPath()
				+ ", getLocalBinaryFile()=" + getLocalBinaryFile()
				+ ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", getPrivateKey()=" + getPrivateKey()
				+ ", getComponentHome()=" + getComponentHome()
				+ ", getAdvancedConf()=" + getAdvancedConf()
				+ ", getClusterId()=" + getClusterName() + "]";
	}

	/**
	 * Method to add new nodes in original nodes of object.
	 */
	public void addNewNodes() {
		// empty method for all components to implement.
	}
}
