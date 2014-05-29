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
package com.impetus.ankush.hadoop.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.AuthConf;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class HadoopClusterConf.
 * 
 * @author hokam chauhan
 */
public class HadoopClusterConf extends ClusterConf {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The description. */
	private String description;

	/** The auth conf. */
	private AuthConf authConf;

	/** The nodes. */
	private List<HadoopNodeConf> nodes;

	/** The newnodes. */
	private List<HadoopNodeConf> newNodes;

	/** The components. */
	private LinkedHashMap<String, GenericConfiguration> components;

	private boolean rackEnabled;

	private String rackFileName = new String();

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the auth conf.
	 * 
	 * @return the authConf
	 */
	public AuthConf getAuthConf() {
		return authConf;
	}

	/**
	 * Sets the auth conf.
	 * 
	 * @param authConf
	 *            the authConf to set
	 */
	public void setAuthConf(AuthConf authConf) {
		this.setUsername(authConf.getUsername());
		if (authConf.isAuthTypePassword()) {
			this.setPassword(authConf.getPassword());
		} else {
			this.setPrivateKey(authConf.getPassword());
		}
		this.authConf = authConf;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<HadoopNodeConf> getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<HadoopNodeConf> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the components.
	 * 
	 * @return the components
	 */
	public LinkedHashMap<String, GenericConfiguration> getComponents() {
		return components;
	}

	/**
	 * Sets the components.
	 * 
	 * @param components
	 *            the components to set
	 */
	public void setComponents(
			LinkedHashMap<String, GenericConfiguration> components) {
		this.components = components;
	}

	/**
	 * Gets whether the rack is enabled or not
	 * 
	 * @return the rackEnabled
	 */
	public boolean isRackEnabled() {
		return rackEnabled;
	}

	/**
	 * Sets the property, whether the rack is enabled or not
	 * 
	 * @param rackEnabled
	 *            the rackEnabled to set
	 */
	public void setRackEnabled(boolean rackEnabled) {
		this.rackEnabled = rackEnabled;
	}

	/**
	 * @return the rackFileName
	 */
	public String getRackFileName() {
		return rackFileName;
	}

	/**
	 * @param rackFileName
	 *            the rackFileName to set
	 */
	public void setRackFileName(String rackFileName) {
		this.rackFileName = rackFileName;
	}

	/**
	 * Checks if is status error.
	 * 
	 * @return true, if is status error
	 */
	@JsonIgnore
	public boolean isStatusError() {
		return this.getState().equals("error");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.ClusterConf#getNodeCount()
	 */
	@Override
	public int getNodeCount() {
		if (this.nodes != null) {
			return this.nodes.size();
		}
		return 0;
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
		result = prime * result
				+ ((authConf == null) ? 0 : authConf.hashCode());
		result = prime * result
				+ ((clusterName == null) ? 0 : clusterName.hashCode());
		result = prime * result
				+ ((components == null) ? 0 : components.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((environment == null) ? 0 : environment.hashCode());
		result = prime * result
				+ ((javaConf == null) ? 0 : javaConf.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		result = prime * result
				+ ((technology == null) ? 0 : technology.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HadoopClusterConf [clusterName=" + clusterName
				+ ", description=" + description + ", technology=" + technology
				+ ", environment=" + environment + ", javaConf=" + javaConf
				+ ", authConf=" + authConf + ", nodes=" + nodes
				+ ", components=" + components + ", getClusterName()="
				+ getClusterName() + ", getDescription()=" + getDescription()
				+ ", getTechnology()=" + getTechnology() + ", isRackEnabled()="
				+ isRackEnabled() + ", getEnvironment()=" + getEnvironment()
				+ ", getJavaConf()=" + getJavaConf() + ", getAuthConf()="
				+ getAuthConf() + ", getNodes()=" + getNodes()
				+ ", getComponents()=" + getComponents() + ", getErrors()="
				+ getErrors() + ", hashCode()=" + hashCode() + ", getClass()="
				+ getClass() + ", toString()=" + super.toString() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		HadoopClusterConf other = (HadoopClusterConf) obj;
		if (clusterName == null) {
			if (other.clusterName != null) {
				return false;
			}
		} else if (!clusterName.equals(other.clusterName)) {
			return false;
		}
		if (environment == null) {
			if (other.environment != null) {
				return false;
			}
		} else if (!environment.equals(other.environment)) {
			return false;
		}
		if (technology == null) {
			if (other.technology != null) {
				return false;
			}
		} else if (!technology.equals(other.technology)) {
			return false;
		}
		if (!rackEnabled == (other.rackEnabled)) {
			return false;
		}
		return true;
	}

	/**
	 * Method to get Hadoop Name node from cluster Conf.
	 * 
	 * @return the name node
	 */
	@JsonIgnore
	public NodeConf getNameNode() {
		for (HadoopNodeConf node : this.getNodes()) {
			if (node.getNameNode()) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Method to get list of Hadoop data nodes from cluster Conf.
	 * 
	 * @return the data nodes
	 */
	@JsonIgnore
	public List<NodeConf> getDataNodes() {
		List<NodeConf> nodes = new ArrayList<NodeConf>();
		for (HadoopNodeConf node : this.getNodes()) {
			if (node.getDataNode()) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * Method to get list of Hadoop Secondary Name nodes from cluster Conf.
	 * 
	 * @return the secondary name nodes
	 */
	@JsonIgnore
	public List<NodeConf> getSecondaryNameNodes() {

		List<NodeConf> nodes = new ArrayList<NodeConf>();
		for (HadoopNodeConf node : this.getNodes()) {
			if (node.getSecondaryNameNode()) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * Gets the newnodes.
	 * 
	 * @return the newnodes
	 */
	public List<HadoopNodeConf> getNewNodes() {

		return newNodes;
	}

	/**
	 * Sets the newnodes.
	 * 
	 * @param newnodes
	 *            the newnodes to set
	 */
	public void setNewNodes(List<HadoopNodeConf> newnodes) {
		this.newNodes = newnodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.ClusterConf#getNodeConfs()
	 */
	@Override
	public List<NodeConf> getNodeConfs() {
		return new ArrayList<NodeConf>(nodes);
	}

	@Override
	public Map<String, GenericConfiguration> getClusterComponents() {
		return getComponents();
	}

	/**
	 * Method to check whether the cluster is hadoop2 cluster or not.
	 * 
	 * @return
	 */
	public boolean isHadoop2() {
		System.out.println(getClusterComponents().keySet());
		return getClusterComponents().keySet().contains(
				Constant.Component.Name.HADOOP2);
	}
}
