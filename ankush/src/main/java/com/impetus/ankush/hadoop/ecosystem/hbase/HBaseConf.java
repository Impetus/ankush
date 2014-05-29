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
package com.impetus.ankush.hadoop.ecosystem.hbase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.hadoop.config.HadoopEcoConfiguration;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;

/*
 * Bean Class containing Apache HBase deployment parameters
 * 
 *  @author Jayati
 */
/**
 * The Class HBaseConf.
 */
public class HBaseConf extends HadoopEcoConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Cluster Id
	 */
	/** The cluster id. */
	private int clusterId = 0;

	/*
	 * HDFS path for storing hbase metadata and data
	 */
	/** The hdfs path for hbase. */
	private String hdfsPathForHbase = new String();
		
	/*
	 * Master Node in the HBase Cluster 
	 */
	/** The hbase master node. */
	private NodeConf hbaseMasterNode = null;
	
	/*
	 * List RegionServers/Slaves in the HBase Cluster
	 */
	/** The hbase region server nodes. */
	private Set<NodeConf> hbaseRegionServerNodes = new HashSet<NodeConf>();
	
	/*
	 * Port on which HBase Master would run
	 */
	/** The hbase master web port. */
	private String hbaseMasterWebPort = ApacheHBaseDeployer.DEFAULT_HBASE_MASTER_HTTP_PORT;
	
	/*
	 * Check if hbase manages zookeeper or an external zookeeper ensemble is to be used
	 */
	/** The hbase manages zk. */
	private boolean hbaseManagesZK = true;
	
	/*
	 * JAVA_HOME to be set on all machines of the cluster
	 */
	/** The universal cluster java home. */
	private String universalClusterJavaHome = new String();
	
	/** The zk quorom property value. */
	private String zkQuoromPropertyValue = new String();
	
	private Set<NodeConf> expectedRSAfterAddOrRemove = new HashSet<NodeConf>();
	
	/*
	 * Port on which the external zookeeper cluster is running
	 */
	/** The zookeeper port. */
	private String zookeeperPort = new String();
	
	/**
	 * Gets the cluster id.
	 *
	 * @return the clusterId
	 */
	public int getClusterId() {
		return clusterId;
	}


	/**
	 * Sets the cluster id.
	 *
	 * @param clusterId the clusterId to set
	 */
	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	/**
	 * Gets the hdfs path for hbase.
	 *
	 * @return the hdfsPathForHbase
	 */
	public String getHdfsPathForHbase() {
		return hdfsPathForHbase;
	}


	/**
	 * Sets the hdfs path for hbase.
	 *
	 * @param hdfsPathForHbase the hdfsPathForHbase to set
	 */
	public void setHdfsPathForHbase(String hdfsPathForHbase) {
		this.hdfsPathForHbase = hdfsPathForHbase;
	}


	/**
	 * Gets the hbase master node.
	 *
	 * @return the hbaseMasterNode
	 */
	public NodeConf getHbaseMasterNode() {
		return hbaseMasterNode;
	}


	/**
	 * Sets the hbase master node.
	 *
	 * @param hbaseMasterNode the hbaseMasterNode to set
	 */
	public void setHbaseMasterNode(NodeConf hbaseMasterNode) {
		this.hbaseMasterNode = hbaseMasterNode;
	}

	/**
	 * Gets the hbase master Web port.
	 *
	 * @return the hbaseMasterWebPort
	 */
	public String getHbaseMasterWebPort() {
		return hbaseMasterWebPort;
	}


	/**
	 * Sets the hbase master port.
	 *
	 * @param hbaseMasterWebPort the hbaseMasterWebPort to set
	 */
	public void setHbaseMasterWebPort(String hbaseMasterWebPort) {
		this.hbaseMasterWebPort = hbaseMasterWebPort;
	}


	/**
	 * Checks if is hbase manages zk.
	 *
	 * @return the hbaseManagesZK
	 */
	public boolean isHbaseManagesZK() {
		return hbaseManagesZK;
	}


	/**
	 * Sets the hbase manages zk.
	 *
	 * @param hbaseManagesZK the hbaseManagesZK to set
	 */
	public void setHbaseManagesZK(boolean hbaseManagesZK) {
		this.hbaseManagesZK = hbaseManagesZK;
	}


	/**
	 * Gets the universal cluster java home.
	 *
	 * @return the universalClusterJavaHome
	 */
	public String getUniversalClusterJavaHome() {
		return universalClusterJavaHome;
	}


	/**
	 * Sets the universal cluster java home.
	 *
	 * @param universalClusterJavaHome the universalClusterJavaHome to set
	 */
	public void setUniversalClusterJavaHome(String universalClusterJavaHome) {
		this.universalClusterJavaHome = universalClusterJavaHome;
	}

	/**
	 * Gets the hbase region server nodes.
	 *
	 * @return the hbaseRegionServerNodes
	 */
	public Set<NodeConf> getHbaseRegionServerNodes() {
		return hbaseRegionServerNodes;
	}


	/**
	 * Sets the hbase region server nodes.
	 *
	 * @param hbaseRegionServerNodes the hbaseRegionServerNodes to set
	 */
	public void setHbaseRegionServerNodes(Set<NodeConf> hbaseRegionServerNodes) {
		this.hbaseRegionServerNodes = hbaseRegionServerNodes;
	}

	/**
	 * Gets the zookeeper port.
	 *
	 * @return the ZookeeperPort
	 */
	public String getZookeeperPort() {
		return zookeeperPort;
	}


	/**
	 * Sets the external zookeeper port.
	 *
	 * @param ZookeeperPort the ZookeeperPort to set
	 */
	public void setZookeeperPort(String zookeeperPort) {
		this.zookeeperPort = zookeeperPort;
	}


	/**
	 * Gets the zk quorom property value.
	 *
	 * @return the zk quorom property value
	 */
	public String getZkQuoromPropertyValue() {
		return zkQuoromPropertyValue;
	}


	/**
	 * Sets the zk quorom property value.
	 *
	 * @param zkQuoromPropertyValue the new zk quorom property value
	 */
	public void setZkQuoromPropertyValue(String zkQuoromPropertyValue) {
		this.zkQuoromPropertyValue = zkQuoromPropertyValue;
	}

	/**
	 * @return the expectedRSAfterAddOrRemove
	 */
	public Set<NodeConf> getExpectedRSAfterAddOrRemove() {
		return expectedRSAfterAddOrRemove;
	}


	/**
	 * @param expectedRSAfterAddOrRemove the expectedRSAfterAddOrRemove to set
	 */
	public void setExpectedRSAfterAddOrRemove(
			Set<NodeConf> expectedRSAfterAddOrRemove) {
		this.expectedRSAfterAddOrRemove = expectedRSAfterAddOrRemove;
	}


	/**
	 * Instantiates a new h base conf.
	 */
	public HBaseConf() {
		super();
	}

//	@Override
//	public void addNewNodes() {
//		// Assuming that the NewNodes list will have HadoopNodeConf objects
//		// No validations for the type caste operation
//		this.hbaseRegionServerNodes.addAll(this.getNewNodes());
//	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HBaseConf [clusterId=" + clusterId + ", externalZookeeperPort="
				+ zookeeperPort + ", hbaseManagesZK=" + hbaseManagesZK
				+ ", hbaseMasterNode=" + hbaseMasterNode + ", hbaseMasterPort="
				+ hbaseMasterWebPort + ", hbaseRegionServerNodes="
				+ hbaseRegionServerNodes + ", hdfsPathForHbase="
				+ hdfsPathForHbase + ", universalClusterJavaHome=" + universalClusterJavaHome
				+ ", getClusterId()="
				+ getClusterId() + ", getZookeeperPort()="
				+ getZookeeperPort() + ", getHbaseMasterNode()="
				+ getHbaseMasterNode() + ", getHbaseMasterWebPort()="
				+ getHbaseMasterWebPort() + ", getHbaseRegionServerNodes()="
				+ getHbaseRegionServerNodes() + ", getHdfsPathForHbase()="
				+ getHdfsPathForHbase() + ", getUniversalClusterJavaHome()="
				+ getUniversalClusterJavaHome() + ", isHbaseManagesZK()="
				+ isHbaseManagesZK() + ", getAdvancedConf()="
				+ getAdvancedConf() + ", getHadoopConf()=" + getHadoopConf()
				+ ", getHadoopHome()=" + getHadoopHome()
				+ ", getHadoopVersion()=" + getHadoopVersion()
				+ ", toString()=" + super.toString()
				+ ", getComponentVersion()=" + getComponentVersion()
				+ ", getInstallationPath()=" + getInstallationPath()
				+ ", getLocalBinaryFile()=" + getLocalBinaryFile()
				+ ", getPassword()=" + getPassword() + ", getPrivateKey()="
				+ getPrivateKey() + ", getServerTarballLocation()="
				+ getServerTarballLocation() + ", getTarballUrl()="
				+ getTarballUrl() + ", getUsername()=" + getUsername()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#getCompNodes()
	 */
	public Set<NodeConf> getCompNodes() {
		Set<NodeConf> nodeConfs = new HashSet<NodeConf>();
		nodeConfs.add(hbaseMasterNode);
		nodeConfs.addAll(hbaseRegionServerNodes);
		return nodeConfs;
	}

	public String getHBaseConfDir() {
		return FileUtils.getSeparatorTerminatedPathEntry(this.getComponentHome()) + ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR;
	}
}
