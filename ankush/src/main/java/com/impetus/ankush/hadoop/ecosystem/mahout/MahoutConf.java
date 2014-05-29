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
package com.impetus.ankush.hadoop.ecosystem.mahout;

import java.util.Collections;
import java.util.Set;

import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.hadoop.config.HadoopEcoConfiguration;

/*
 * Bean Class containing Apache Mahout deployment parameters
 * 
 *  @author Jayati
 */
/**
 * The Class MahoutConf.
 */
public class MahoutConf extends HadoopEcoConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * IP Address of the node
	 */
	/** The master node. */
	private NodeConf masterNode = null;

	/**
	 * Instantiates a new mahout conf.
	 */
	public MahoutConf() {
		super();
	}

	/**
	 * Gets the master node.
	 *
	 * @return the masterNode
	 */
	public NodeConf getMasterNode() {
		return masterNode;
	}

	/**
	 * Sets the master node.
	 *
	 * @param masterNode the masterNode to set
	 */
	public void setMasterNode(NodeConf masterNode) {
		this.masterNode = masterNode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MahoutConf [masterNode=" + masterNode + ", getMasterNode()="
				+ getMasterNode() + ", getHadoopVersion()="
				+ getHadoopVersion() + ", getHadoopHome()=" + getHadoopHome()
				+ ", getHadoopConf()=" + getHadoopConf() + ", toString()="
				+ super.toString() + ", getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword()
				+ ", getComponentVersion()=" + getComponentVersion()
				+ ", getInstallationPath()=" + getInstallationPath()
				+ ", getLocalBinaryFile()=" + getLocalBinaryFile()
				+ ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#getCompNodes()
	 */
	public Set<NodeConf> getCompNodes() {
		return Collections.singleton(masterNode);
	}
	
}
