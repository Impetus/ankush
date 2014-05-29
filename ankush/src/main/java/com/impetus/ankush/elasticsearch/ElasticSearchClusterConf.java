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
package com.impetus.ankush.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class ElasticSearchClusterConf.
 */
public class ElasticSearchClusterConf extends ClusterConf {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The es conf. */
	ElasticSearchConf esConf = new ElasticSearchConf();

	/** The is status error. */
	private boolean isStatusError;

	/** The new nodes. */
	private List<NodeConf> newNodes = new ArrayList<NodeConf>();

	/** The rackEnabled. */
	private boolean rackEnabled;

	/** The rack file name. */
	private String rackFileName;

	/**
	 * Gets the es conf.
	 * 
	 * @return the esConf
	 */
	public ElasticSearchConf getEsConf() {
		return esConf;
	}

	/**
	 * Sets the es conf.
	 * 
	 * @param esConf
	 *            the esConf to set
	 */
	public void setEsConf(ElasticSearchConf esConf) {
		this.esConf = esConf;
	}

	/**
	 * Checks if is status error.
	 * 
	 * @return the isStatusError
	 */
	public boolean isStatusError() {
		return getState().equals(Constant.Cluster.State.ERROR);
	}

	/**
	 * Sets the status error.
	 * 
	 * @param isStatusError
	 *            the isStatusError to set
	 */
	public void setStatusError(boolean isStatusError) {
		this.isStatusError = isStatusError;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.ClusterConf#getClusterComponents
	 * ()
	 */
	@Override
	public Map<String, GenericConfiguration> getClusterComponents() {
		return getComponents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.ClusterConf#getNodeCount()
	 */
	@Override
	public int getNodeCount() {
		return getNodeConfs().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.ClusterConf#getNodeConfs()
	 */
	@Override
	public List<NodeConf> getNodeConfs() {
		return esConf.getNodes();
	}

	/**
	 * Gets the new nodes.
	 * 
	 * @return the newNodes
	 */
	public List<NodeConf> getNewNodes() {
		return newNodes;
	}

	/**
	 * Sets the new nodes.
	 * 
	 * @param newNodes
	 *            the newNodes to set
	 */
	public void setNewNodes(List<NodeConf> newNodes) {
		this.newNodes = newNodes;
	}

	/**
	 * Checks if is rack enabled.
	 * 
	 * @return the rackEnabled
	 */
	public boolean isRackEnabled() {
		return rackEnabled;
	}

	/**
	 * Sets the rack enabled.
	 * 
	 * @param rackEnabled
	 *            the rackEnabled to set
	 */
	public void setRackEnabled(boolean rackEnabled) {
		this.rackEnabled = rackEnabled;
	}

	/**
	 * Gets the rack file name.
	 * 
	 * @return the rack file name
	 */
	public String getRackFileName() {
		return rackFileName;
	}

	/**
	 * Sets the rack file name.
	 * 
	 * @param rackFileName
	 *            the new rack file name
	 */
	public void setRackFileName(String rackFileName) {
		this.rackFileName = rackFileName;
	}

	/**
	 * Gets the components.
	 * 
	 * @return the components
	 */
	@JsonIgnore
	public Map<String, GenericConfiguration> getComponents() {
		final Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();
		components.put(Constant.Component.Name.ELASTICSEARCH, esConf);
		return components;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ElasticSearchClusterConf[esConf=" + esConf + ",javaConf="
				+ javaConf + ", isStatusError=" + isStatusError + ", errors="
				+ errors + ", clusterName=" + clusterName + ", environment="
				+ environment + ", gangliaMaster=" + gangliaMaster
				+ ", ipPattern=" + ipPattern + ", patternFile=" + patternFile
				+ ", newNodes= " + newNodes + ", getEsConf()=" + getEsConf()
				+ ", getNewNodes()=" + getNewNodes() + ", getErrors()="
				+ getErrors() + ", getCurrentUser()=" + getCurrentUser()
				+ ", getClusterName()=" + getClusterName()
				+ ", getEnvironment()=" + getEnvironment() + ", isLocal()="
				+ isLocal() + ", getGangliaMaster()=" + getGangliaMaster()
				+ ", getIpPattern()=" + getIpPattern() + ", getPatternFile()="
				+ getPatternFile() + ", isAuthTypePassword()="
				+ isAuthTypePassword() + ", getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword() + ", getPrivateKey()="
				+ getPrivateKey() + ", getClusterId()=" + getClusterId()
				+ ", getOperationId()=" + getOperationId() + ", getState()="
				+ getState() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((esConf == null) ? 0 : esConf.hashCode());
		result = prime * result + (isStatusError ? 1231 : 1237);
		result = prime * result
				+ ((javaConf == null) ? 0 : javaConf.hashCode());
		result = prime * result
				+ ((newNodes == null) ? 0 : newNodes.hashCode());
		result = prime * result + (rackEnabled ? 1231 : 1237);
		result = prime * result
				+ ((rackFileName == null) ? 0 : rackFileName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElasticSearchClusterConf other = (ElasticSearchClusterConf) obj;
		if (esConf == null) {
			if (other.esConf != null)
				return false;
		} else if (!esConf.equals(other.esConf))
			return false;
		if (isStatusError != other.isStatusError)
			return false;
		if (javaConf == null) {
			if (other.javaConf != null)
				return false;
		} else if (!javaConf.equals(other.javaConf))
			return false;
		if (newNodes == null) {
			if (other.newNodes != null)
				return false;
		} else if (!newNodes.equals(other.newNodes))
			return false;
		if (rackEnabled != other.rackEnabled)
			return false;
		if (rackFileName == null) {
			if (other.rackFileName != null)
				return false;
		} else if (!rackFileName.equals(other.rackFileName))
			return false;
		return true;
	}
}
