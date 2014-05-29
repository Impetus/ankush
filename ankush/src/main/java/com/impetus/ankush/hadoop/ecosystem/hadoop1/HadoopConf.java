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

package com.impetus.ankush.hadoop.ecosystem.hadoop1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;

/**
 * The Class HadoopConf.
 */
public class HadoopConf extends GenericConfiguration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The hadoop conf dir. */
	private String hadoopConfDir = new String();

	/** The hadoop tmp dir. */
	private String hadoopTmpDir = new String();
	
	/** The mapred tmp dir. */
	private String mapRedTmpDir = new String();

	/** The dfs replication factor. */
	private int dfsReplicationFactor = 3;

	/** The flag for rack awareness. */
	private Boolean rackEnabled = false;
	
	/** The Uploaded File Name for rack awareness. */
	private String rackFileName = new String();
	
	/** The contents for topology.data file for rack awareness. */
	private String rackFileContent = new String();

	/** The dfs name dir. */
	private String dfsNameDir = new String();

	/** The dfs data dir. */
	private String dfsDataDir = new String();

	/** The includes3. */
	private boolean includes3 = false;

	/** The s3 access key. */
	private String s3AccessKey = new String();

	/** The s3 secret key. */
	private String s3SecretKey = new String();

	/** The includes3n. */
	private boolean includes3n = false;

	/** The s3n access key. */
	private String s3nAccessKey = new String();

	/** The s3n secret key. */
	private String s3nSecretKey = new String();

	/** The job tracker recovery. */
	private boolean jobTrackerRecovery = false;

	/** The namenode. */
	private HadoopNodeConf namenode = null;

	/** The secondary namenode. */
	private HadoopNodeConf secondaryNamenode = new HadoopNodeConf();

	/** The slaves. */
	private Set<HadoopNodeConf> slaves = new HashSet<HadoopNodeConf>();

	private Set<NodeConf> expectedNodesAfterAddOrRemove = new HashSet<NodeConf>();
	
	/** The java homes. */
	private HashMap<String, String> javaHomes = new HashMap<String, String>();

	/** The node confs. */
//	private Set<HadoopNodeConf> clusterNodeConfs = new HashSet<HadoopNodeConf>();
	
	/** The prop name topology script file. */
	private String propNameTopologyScriptFile = new String();
	
	/**
	 * Gets the hadoop conf dir.
	 * 
	 * @return the hadoopConfDir
	 */
	public String getHadoopConfDir() {
		return hadoopConfDir;
	}

	/**
	 * Sets the hadoop conf dir.
	 * 
	 * @param hadoopConfDir
	 *            the hadoopConfDir to set
	 */
	public void setHadoopConfDir(String hadoopConfDir) {
		this.hadoopConfDir = FileNameUtils.convertToValidPath(hadoopConfDir);
	}

	/**
	 * Gets the hadoop tmp dir.
	 * 
	 * @return the hadoopTmpDir
	 */
	public String getHadoopTmpDir() {
		return hadoopTmpDir;
	}

	/**
	 * Sets the hadoop tmp dir.
	 * 
	 * @param hadoopTmpDir
	 *            the hadoopTmpDir to set
	 */
	public void setHadoopTmpDir(String hadoopTmpDir) {
		this.hadoopTmpDir = FileNameUtils.convertToValidPath(hadoopTmpDir);
	}

	/**
	 * Gets the mapred tmp dir.
	 *
	 * @return the mapredTmpDir
	 */
	public String getMapRedTmpDir() {
		return mapRedTmpDir;
	}

	/**
	 * Sets the mapred tmp dir.
	 *
	 * @param mapredTmpDir the mapredTmpDir to set
	 */
	public void setMapRedTmpDir(String mapredTmpDir) {
		this.mapRedTmpDir = mapredTmpDir;
	}

	/**
	 * Gets the namenode.
	 * 
	 * @return the namenode
	 */
	public NodeConf getNamenode() {
		return namenode;
	}

	/**
	 * Sets the namenode.
	 * 
	 * @param namenode
	 *            the namenode to set
	 */
	public void setNamenode(HadoopNodeConf namenode) {
		this.namenode = namenode;
	}

	/**
	 * Checks if is namenode.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @return true, if is namenode
	 */
	public boolean isNamenode(NodeConf nodeConf) {
		return this.namenode.equals(nodeConf);
	}
	
	/**
	 * Gets the secondary namenode.
	 * 
	 * @return the secondaryNamenode
	 */
	public NodeConf getSecondaryNamenode() {
		return secondaryNamenode;
	}

	/**
	 * Sets the secondary namenode.
	 * 
	 * @param secondaryNamenode
	 *            the secondaryNamenode to set
	 */
	public void setSecondaryNamenode(HadoopNodeConf secondaryNamenode) {
		this.secondaryNamenode = secondaryNamenode;
	}

	/**
	 * Gets the slaves.
	 * 
	 * @return the slaves
	 */
	public Set<HadoopNodeConf> getSlaves() {
		return slaves;
	}

	/**
	 * Sets the slaves.
	 * 
	 * @param slaves
	 *            the slaves to set
	 */
	public void setSlaves(Set<HadoopNodeConf> slaves) {
		this.slaves = slaves;
	}
	
	/**
	 * Checks if is rack enabled.
	 *
	 * @return the rackEnabled
	 */
	public Boolean isRackEnabled() {
		return rackEnabled;
	}

	/**
	 * Sets the rack enabled.
	 *
	 * @param rackEnabled the rackEnabled to set
	 */
	public void setRackEnabled(Boolean rackEnabled) {
		this.rackEnabled = rackEnabled;
	}

	/**
	 * Gets the rack file name.
	 *
	 * @return the rackFileName
	 */
	public String getRackFileName() {
		return rackFileName;
	}

	/**
	 * Sets the rack file name.
	 *
	 * @param rackFileName the rackFileName to set
	 */
	public void setRackFileName(String rackFileName) {
		this.rackFileName = rackFileName;
	}

	/**
	 * Gets the rack file content.
	 *
	 * @return the rackFileContent
	 */
	public String getRackFileContent() {
		return rackFileContent;
	}

	/**
	 * Sets the rack file content.
	 *
	 * @param rackFileContent the rackFileContent to set
	 */
	public void setRackFileContent(String rackFileContent) {
		this.rackFileContent = rackFileContent;
	}


	/**
	 * Gets the dfs name dir.
	 * 
	 * @return the dfsNameDir
	 */
	public String getDfsNameDir() {
		return dfsNameDir;
	}

	/**
	 * Sets the dfs name dir.
	 * 
	 * @param dfsNameDir
	 *            the dfsNameDir to set
	 */
	public void setDfsNameDir(String dfsNameDir) {
		this.dfsNameDir = dfsNameDir;
	}

	/**
	 * Gets the dfs data dir.
	 * 
	 * @return the dfsDataDir
	 */
	public String getDfsDataDir() {
		return dfsDataDir;
	}

	/**
	 * Sets the dfs data dir.
	 * 
	 * @param dfsDataDir
	 *            the dfsDataDir to set
	 */
	public void setDfsDataDir(String dfsDataDir) {
		this.dfsDataDir = dfsDataDir;
	}

	/**
	 * Checks if is includes3.
	 * 
	 * @return the includes3
	 */
	public boolean isIncludes3() {
		return includes3;
	}

	/**
	 * Sets the includes3.
	 * 
	 * @param includes3
	 *            the includes3 to set
	 */
	public void setIncludes3(boolean includes3) {
		this.includes3 = includes3;
	}

	/**
	 * Gets the s3 access key.
	 * 
	 * @return the s3AccessKey
	 */
	public String getS3AccessKey() {
		return s3AccessKey;
	}

	/**
	 * Sets the s3 access key.
	 * 
	 * @param s3AccessKey
	 *            the s3AccessKey to set
	 */
	public void setS3AccessKey(String s3AccessKey) {
		this.s3AccessKey = s3AccessKey;
	}

	/**
	 * Gets the s3 secret key.
	 * 
	 * @return the s3SecretKey
	 */
	public String getS3SecretKey() {
		return s3SecretKey;
	}

	/**
	 * Sets the s3 secret key.
	 * 
	 * @param s3SecretKey
	 *            the s3SecretKey to set
	 */
	public void setS3SecretKey(String s3SecretKey) {
		this.s3SecretKey = s3SecretKey;
	}

	/**
	 * Checks if is includes3n.
	 * 
	 * @return the includes3n
	 */
	public boolean isIncludes3n() {
		return includes3n;
	}

	/**
	 * Sets the includes3n.
	 * 
	 * @param includes3n
	 *            the includes3n to set
	 */
	public void setIncludes3n(boolean includes3n) {
		this.includes3n = includes3n;
	}

	/**
	 * Gets the s3n access key.
	 * 
	 * @return the s3nAccessKey
	 */
	public String getS3nAccessKey() {
		return s3nAccessKey;
	}

	/**
	 * Sets the s3n access key.
	 * 
	 * @param s3nAccessKey
	 *            the s3nAccessKey to set
	 */
	public void setS3nAccessKey(String s3nAccessKey) {
		this.s3nAccessKey = s3nAccessKey;
	}

	/**
	 * Gets the s3n secret key.
	 * 
	 * @return the s3nSecretKey
	 */
	public String getS3nSecretKey() {
		return s3nSecretKey;
	}

	/**
	 * Sets the s3n secret key.
	 * 
	 * @param s3nSecretKey
	 *            the s3nSecretKey to set
	 */
	public void setS3nSecretKey(String s3nSecretKey) {
		this.s3nSecretKey = s3nSecretKey;
	}

	/**
	 * Gets the dfs replication factor.
	 * 
	 * @return the dfsReplicationFactor
	 */
	public int getDfsReplicationFactor() {
		return dfsReplicationFactor;
	}

	/**
	 * Sets the dfs replication factor.
	 * 
	 * @param dfsReplicationFactor
	 *            the dfsReplicationFactor to set
	 */
	public void setDfsReplicationFactor(int dfsReplicationFactor) {
		this.dfsReplicationFactor = dfsReplicationFactor;
	}

	/**
	 * Gets the java home.
	 * 
	 * @param hostname
	 *            the hostname
	 * @return the java home
	 */
	@JsonIgnore
	public String getJavaHome(String hostname) {
		return this.javaHomes.get(hostname);
	}

	/**
	 * Sets the java homes.
	 * 
	 * @param javaHomes
	 *            the java homes
	 */
	public void setJavaHomes(HashMap<String, String> javaHomes) {
		this.javaHomes = javaHomes;
	}

	/**
	 * Gets the cluster node confs.
	 *
	 * @return the clusterNodeConfs
	 */
	@JsonIgnore
	public Set<NodeConf> getExpectedNodesAfterAddOrRemove() {
		return this.expectedNodesAfterAddOrRemove;
	}

	/**
	 * @param expectedNodesAfterAddOrRemove the expectedNodesAfterAddOrRemove to set
	 */
	@JsonIgnore
	public void setExpectedNodesAfterAddOrRemove(
			Set<NodeConf> expectedNodesAfterAddOrRemove) {
		this.expectedNodesAfterAddOrRemove = expectedNodesAfterAddOrRemove;
	}

	/**
	 * Gets the prop name topology script file.
	 *
	 * @return the propNameTopologyScriptFile
	 */
	public String getPropNameTopologyScriptFile() {
		return propNameTopologyScriptFile;
	}

	/**
	 * Sets the prop name topology script file.
	 *
	 * @param propNameTopologyScriptFile the propNameTopologyScriptFile to set
	 */
	public void setPropNameTopologyScriptFile(String propNameTopologyScriptFile) {
		this.propNameTopologyScriptFile = propNameTopologyScriptFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#
	 * getComponentHome()
	 */
	public String getComponentHome() {
		return FileNameUtils.convertToValidPath(this.getInstallationPath()
				+ "hadoop-" + this.getComponentVersion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HadoopConf [hadoopConfDir=" + hadoopConfDir + ", hadoopTmpDir="
				+ hadoopTmpDir + ", dfsReplicationFactor="
				+ dfsReplicationFactor + ", dfsNameDir=" + dfsNameDir
				+ ", dfsDataDir=" + dfsDataDir + ", includes3=" + includes3
				+ ", s3AccessKey=" + s3AccessKey + ", s3SecretKey="
				+ s3SecretKey + ", includes3n=" + includes3n
				+ ", s3nAccessKey=" + s3nAccessKey + ", s3nSecretKey="
				+ s3nSecretKey + ", namenode=" + namenode
				+ ", secondaryNamenode=" + secondaryNamenode + ", slaves="
				+ slaves + ", javaHomes="
				+ javaHomes + ", getHadoopConfDir()=" + getHadoopConfDir()
				+ ", getHadoopTmpDir()=" + getHadoopTmpDir()
				+ ", getNamenode()=" + getNamenode()
				+ ", getSecondaryNamenode()=" + getSecondaryNamenode()
				+ ", getSlaves()=" + getSlaves()
				+ ", getDfsNameDir()=" + getDfsNameDir() + ", getDfsDataDir()="
				+ getDfsDataDir() + ", isIncludes3()=" + isIncludes3()
				+ ", getS3AccessKey()=" + getS3AccessKey()
				+ ", getS3SecretKey()=" + getS3SecretKey()
				+ ", isIncludes3n()=" + isIncludes3n() + ", getS3nAccessKey()="
				+ getS3nAccessKey() + ", getS3nSecretKey()="
				+ getS3nSecretKey() + ", getDfsReplicationFactor()="
				+ getDfsReplicationFactor() + ", getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword()
				+ ", getComponentVendor()=" + getComponentVendor()
				+ ", getComponentVersion()=" + getComponentVersion()
				+ ", getInstallationPath()=" + getInstallationPath()
				+ ", getLocalBinaryFile()=" + getLocalBinaryFile()
				+ ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", getPrivateKey()=" + getPrivateKey()
				+ ", getComponentHome()=" + getComponentHome()
				+ ", getAdvancedConf()=" + getAdvancedConf() + "]";
	}

	/**
	 * Checks if is job tracker recovery.
	 * 
	 * @return the jobTrackerRecovery
	 */
	public boolean isJobTrackerRecovery() {
		return jobTrackerRecovery;
	}

	/**
	 * Sets the job tracker recovery.
	 * 
	 * @param jobTrackerRecovery
	 *            the jobTrackerRecovery to set
	 */
	public void setJobTrackerRecovery(boolean jobTrackerRecovery) {
		this.jobTrackerRecovery = jobTrackerRecovery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.GenericConfiguration#getCompNodes
	 * ()
	 */
	@Override
	public Set<NodeConf> getCompNodes() {
		Set<NodeConf> nodeConfs = new HashSet<NodeConf>();
		nodeConfs.add(namenode);
		if(secondaryNamenode != null) {
			nodeConfs.add(secondaryNamenode);
		}
		nodeConfs.addAll(slaves);
		return nodeConfs;
	}
	
	public void setHadoopNewNodes(List<HadoopNodeConf> newNodes) {
		setNewNodes(new ArrayList<NodeConf>(newNodes));
	}
	
	@Override
	public void addNewNodes() {
		// Assuming that the NewNodes list will have HadoopNodeConf objects
		// No validations for the type caste operation
		for(NodeConf nodeConf : this.getNewNodes()) {
			HadoopNodeConf hNodeConf = (HadoopNodeConf) nodeConf;
			this.slaves.add(hNodeConf);	
		}
	}
	
	public String getHdfsUri() {
		String hdfsUri = HadoopUtils.HADOOP_URI_PREFIX + this.getNamenode().getPrivateIp() 
						 + ":" + Hadoop1Deployer.DEFAULT_PORT_RPC_NAMENODE + "/";
		
		return hdfsUri;
	}
	
	@JsonIgnore
	public boolean isCdh3() {
		if(this.getComponentVendor().equals(Constant.Hadoop.Vendors.CLOUDERA)) {
			if(this.getComponentVersion().equals(HadoopUtils.VERSION_CDH3)) {
				return true;
			}
		}
		return false;
	}
}
