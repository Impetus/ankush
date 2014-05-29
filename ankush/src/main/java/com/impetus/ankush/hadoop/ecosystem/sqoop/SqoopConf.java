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
package com.impetus.ankush.hadoop.ecosystem.sqoop;

import java.util.Collections;
import java.util.Set;

import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.hadoop.config.HadoopEcoConfiguration;

/*
 * Bean Class containing Apache Sqoop deployment parameters
 * 
 *  @author Jayati
 */
/**
 * The Class SqoopConf.
 */
public class SqoopConf extends HadoopEcoConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new sqoop conf.
	 */
	public SqoopConf() {
		super();
	}

	/*
	 * IP Address of the node
	 */
	/** The master node. */
	private NodeConf masterNode = null;
	
	/*
	 * Value of HBASE_HOME to be set on the node
	 */
	/** The hbase home. */
	private String hbaseHome = null;
	
	/*
	 * Value of HIVE_HOME to be set on the node
	 */
	/** The hive home. */
	private String hiveHome = null;

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

	/**
	 * Gets the hbase home.
	 *
	 * @return the hbaseHome
	 */
	public String getHbaseHome() {
		return hbaseHome;
	}

	/**
	 * Sets the hbase home.
	 *
	 * @param hbaseHome the hbaseHome to set
	 */
	public void setHbaseHome(String hbaseHome) {
		this.hbaseHome = hbaseHome;
	}

	/**
	 * Gets the hive home.
	 *
	 * @return the hiveHome
	 */
	public String getHiveHome() {
		return hiveHome;
	}

	/**
	 * Sets the hive home.
	 *
	 * @param hiveHome the hiveHome to set
	 */
	public void setHiveHome(String hiveHome) {
		this.hiveHome = hiveHome;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SqoopConf [masterNode=" + masterNode + ", hbaseHome="
				+ hbaseHome + ", hiveHome=" + hiveHome + ", getMasterNode()="
				+ getMasterNode() + ", getHbaseHome()=" + getHbaseHome()
				+ ", getHiveHome()=" + getHiveHome() + ", getHadoopVersion()="
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
