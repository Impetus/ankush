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
package com.impetus.ankush.hadoop.ecosystem.hive;

import java.util.Collections;
import java.util.Set;

import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.hadoop.config.HadoopEcoConfiguration;

/**
 * The Class HiveConf.
 *
 * @author mayur
 */
public class HiveConf extends HadoopEcoConfiguration {

	// Hive specific configuration goes here ...

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** The node conf. */
	private NodeConf nodeConf = null;

	/**
	 * Instantiates a new hive conf.
	 */
	public HiveConf() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the node.
	 *
	 * @return the node
	 */
	public NodeConf getNode() {
		return nodeConf;
	}

	/**
	 * Sets the node.
	 *
	 * @param nodeConf the node to set
	 */
	public void setNode(NodeConf nodeConf) {
		this.nodeConf = nodeConf;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#getComponentHome()
	 */
	public String getComponentHome() {
		return this.getInstallationPath() + "hive-"
				+ this.getComponentVersion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HiveConf [node=" + nodeConf + ", getNode()=" + getNode()
				+ ", getHadoopVersion()=" + getHadoopVersion()
				+ ", getHadoopHome()=" + getHadoopHome() + ", getHadoopConf()="
				+ getHadoopConf() + ", toString()=" + super.toString()
				+ ", getUsername()=" + getUsername() + ", getPassword()="
				+ getPassword() + ", getComponentVersion()="
				+ getComponentVersion() + ", getInstallationPath()="
				+ getInstallationPath() + ", getLocalBinaryFile()="
				+ getLocalBinaryFile() + ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#getCompNodes()
	 */
	public Set<NodeConf> getCompNodes() {
		return Collections.singleton(nodeConf);
	}

}
