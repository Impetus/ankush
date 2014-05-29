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
package com.impetus.ankush.hadoop.ecosystem.hadoop2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopConf.
 *
 * @author Akhil
 */
public class Hadoop2Conf extends HadoopConf {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The resource manager node. */
	private NodeConf resourceManagerNode;
	
	/** The mapred framework. */
	private String mapredFramework = "yarn";
	
	/** The scheduler class. */
	private String schedulerClass = "org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler";
	
	private boolean startJobHistoryServer;
	
	/** The job history server node. */
	private NodeConf jobHistoryServerNode;
	
	/** The ha enabled. */
	private boolean haEnabled = false;
	
	/** The Web Applications Proxy enabled. */
	private boolean webProxyEnabled = false;
	
	private NodeConf webAppProxyNode;
	
	private String webAppProxyPort = new String();
	
	/** The nameservice id. */
	private String nameserviceId = new String();
	
	/** The active namenode. */
	private NodeConf activeNamenode;
	
	/** The stand by namenode. */
	private NodeConf standByNamenode;
	
	private List<String> haNameNodesPublicIp = new ArrayList<String>();
	
	/** The name node id1. */
	private String nameNodeId1 = new String("nn1");
	
	/** The name node id2. */
	private String nameNodeId2 = new String("nn2");
	
	/** The journal nodes. */
	private List<NodeConf> journalNodes;
	
	/** The journal node edits dir. */
	private String journalNodeEditsDir = new String();
	
	/** The automatic failover enabled. */
	private boolean automaticFailoverEnabled = false; 
	
	/** The zk quorum nodes. */
	private List<NodeConf> zkQuorumNodes;
	
	/** The zk client port. */
	private String zkClientPort = new String();
	
	/** The ha zk qourum value. */
	private String haZkQourumValue = new String();
	
	/**
	 * Gets the resource manager node.
	 *
	 * @return the resourceManagerNode
	 */
	public NodeConf getResourceManagerNode() {
		return resourceManagerNode;
	}
	
	/**
	 * Sets the resource manager node.
	 *
	 * @param resourceManagerNode the resourceManagerNode to set
	 */
	public void setResourceManagerNode(NodeConf resourceManagerNode) {
		this.resourceManagerNode = resourceManagerNode;
	}
	
	/**
	 * Gets the mapred framework.
	 *
	 * @return the mapredFramework
	 */
	public String getMapredFramework() {
		return mapredFramework;
	}
	
	/**
	 * Sets the mapred framework.
	 *
	 * @param mapredFramework the mapredFramework to set
	 */
	public void setMapredFramework(String mapredFramework) {
		this.mapredFramework = mapredFramework;
	}
	
	/**
	 * Gets the scheduler class.
	 *
	 * @return the schedulerClass
	 */
	public String getSchedulerClass() {
		return schedulerClass;
	}
	
	/**
	 * Sets the scheduler class.
	 *
	 * @param schedulerClass the schedulerClass to set
	 */
	public void setSchedulerClass(String schedulerClass) {
		this.schedulerClass = schedulerClass;
	}
	
	/**
	 * @return the startJobHistoryServer
	 */
	public boolean isStartJobHistoryServer() {
		return startJobHistoryServer;
	}

	/**
	 * @param startJobHistoryServer the startJobHistoryServer to set
	 */
	public void setStartJobHistoryServer(boolean startJobHistoryServer) {
		this.startJobHistoryServer = startJobHistoryServer;
	}

	/**
	 * @return the webProxyEnabled
	 */
	public boolean isWebProxyEnabled() {
		return webProxyEnabled;
	}

	/**
	 * @param webProxyEnabled the webProxyEnabled to set
	 */
	public void setWebProxyEnabled(boolean webProxyEnabled) {
		this.webProxyEnabled = webProxyEnabled;
	}

	/**
	 * @return the webAppProxyNode
	 */
	public NodeConf getWebAppProxyNode() {
		return webAppProxyNode;
	}

	/**
	 * @param webAppProxyNode the webAppProxyNode to set
	 */
	public void setWebAppProxyNode(NodeConf webAppProxyNode) {
		this.webAppProxyNode = webAppProxyNode;
	}

	/**
	 * @return the webAppProxyPort
	 */
	public String getWebAppProxyPort() {
		return webAppProxyPort;
	}

	/**
	 * @param webAppProxyPort the webAppProxyPort to set
	 */
	public void setWebAppProxyPort(String webAppProxyPort) {
		this.webAppProxyPort = webAppProxyPort;
	}

	/**
	 * Gets the job history server node.
	 *
	 * @return the jobHistoryServerNode
	 */
	public NodeConf getJobHistoryServerNode() {
		return jobHistoryServerNode;
	}
	
	/**
	 * Sets the job history server node.
	 *
	 * @param jobHistoryServerNode the jobHistoryServerNode to set
	 */
	public void setJobHistoryServerNode(NodeConf jobHistoryServerNode) {
		this.jobHistoryServerNode = jobHistoryServerNode;
	}
	
	/**
	 * Checks if is ha enabled.
	 *
	 * @return the haEnabled
	 */
	public boolean isHaEnabled() {
		return haEnabled;
	}
	
	/**
	 * Sets the ha enabled.
	 *
	 * @param haEnabled the new ha enabled
	 */
	public void setHaEnabled(boolean haEnabled) {
		this.haEnabled = haEnabled;
	}
	
	/**
	 * Gets the nameservice id.
	 *
	 * @return the nameserviceId
	 */
	public String getNameserviceId() {
		return nameserviceId;
	}
	
	/**
	 * Sets the nameservice id.
	 *
	 * @param nameserviceId the nameserviceId to set
	 */
	public void setNameserviceId(String nameserviceId) {
		this.nameserviceId = nameserviceId;
	}
	
	/**
	 * Gets the active namenode.
	 *
	 * @return the activeNamenode
	 */
	public NodeConf getActiveNamenode() {
		return activeNamenode;
	}
	
	/**
	 * Sets the active namenode.
	 *
	 * @param activeNamenode the activeNamenode to set
	 */
	public void setActiveNamenode(NodeConf activeNamenode) {
		this.activeNamenode = activeNamenode;
	}
	
	/**
	 * Gets the stand by namenode.
	 *
	 * @return the standByNamenode
	 */
	public NodeConf getStandByNamenode() {
		return standByNamenode;
	}
	
	/**
	 * Sets the stand by namenode.
	 *
	 * @param standByNamenode the standByNamenode to set
	 */
	public void setStandByNamenode(NodeConf standByNamenode) {
		this.standByNamenode = standByNamenode;
	}
	
	/**
	 * @return the haNameNodesPublicIp
	 */
	@JsonIgnore
	public List<String> getHaNameNodesPublicIp() {
		return haNameNodesPublicIp;
	}
	

	/**
	 * @param haNameNodesPublicIp the haNameNodesPublicIp to set
	 */
	@JsonIgnore
	public void setHaNameNodesPublicIp(List<String> haNameNodesPublicIp) {
		this.haNameNodesPublicIp = haNameNodesPublicIp;
	}

	/**
	 * Gets the name node id1.
	 *
	 * @return the namenode1Id
	 */
	public String getNameNodeId1() {
		return nameNodeId1;
	}
	
	/**
	 * Sets the name node id1.
	 *
	 * @param nameNodeId1 the new name node id1
	 */
	public void setNameNodeId1(String nameNodeId1) {
		this.nameNodeId1 = nameNodeId1;
	}
	
	/**
	 * Gets the name node id2.
	 *
	 * @return the namenodeId2
	 */
	public String getNameNodeId2() {
		return nameNodeId2;
	}
	
	/**
	 * Sets the name node id2.
	 *
	 * @param nameNodeId2 the new name node id2
	 */
	public void setNameNodeId2(String nameNodeId2) {
		this.nameNodeId2 = nameNodeId2;
	}
	
	/**
	 * Gets the journal nodes.
	 *
	 * @return the journalNodes
	 */
	public List<NodeConf> getJournalNodes() {
		return journalNodes;
	}
	
	/**
	 * Sets the journal nodes.
	 *
	 * @param journalNodes the journalNodes to set
	 */
	public void setJournalNodes(List<NodeConf> journalNodes) {
		this.journalNodes = journalNodes;
	}
	
	/**
	 * Gets the journal node edits dir.
	 *
	 * @return the journalNodeEditsDir
	 */
	public String getJournalNodeEditsDir() {
		return journalNodeEditsDir;
	}
	
	/**
	 * Sets the journal node edits dir.
	 *
	 * @param journalNodeEditsDir the journalNodeEditsDir to set
	 */
	public void setJournalNodeEditsDir(String journalNodeEditsDir) {
		this.journalNodeEditsDir = journalNodeEditsDir;
	}
	
	/**
	 * Checks if is automatic failover enabled.
	 *
	 * @return the isAutomaticFailoverEnabled
	 */
	public boolean isAutomaticFailoverEnabled() {
		return automaticFailoverEnabled;
	}
	
	/**
	 * Sets the automatic failover enabled.
	 *
	 * @param automaticFailoverEnabled the new automatic failover enabled
	 */
	public void setAutomaticFailoverEnabled(boolean automaticFailoverEnabled) {
		this.automaticFailoverEnabled = automaticFailoverEnabled;
	}
	
	/**
	 * Gets the zk quorum nodes.
	 *
	 * @return the zkQuorumNodes
	 */
	@JsonIgnore
	public List<NodeConf> getZkQuorumNodes() {
		return zkQuorumNodes;
	}
	
	/**
	 * Sets the zk quorum nodes.
	 *
	 * @param zkQuorumNodes the zkQuorumNodes to set
	 */
	@JsonIgnore
	public void setZkQuorumNodes(List<NodeConf> zkQuorumNodes) {
		this.zkQuorumNodes = zkQuorumNodes;
	}
	
	/**
	 * Gets the zk client port.
	 *
	 * @return the zkClientPort
	 */
	public String getZkClientPort() {
		return zkClientPort;
	}
	
	/**
	 * Sets the zk client port.
	 *
	 * @param zkClientPort the zkClientPort to set
	 */
	public void setZkClientPort(String zkClientPort) {
		this.zkClientPort = zkClientPort;
	}
	
	/**
	 * Gets the ha zk qourum value.
	 *
	 * @return the haZkQourumValue
	 */
	@JsonIgnore
	public String getHaZkQourumValue() {
		return haZkQourumValue;
	}
	
	/**
	 * Sets the ha zk qourum value.
	 *
	 * @param haZkQourumValue the haZkQourumValue to set
	 */
	public void setHaZkQourumValue(String haZkQourumValue) {
		this.haZkQourumValue = haZkQourumValue;
	}
	
	@JsonIgnore
	public boolean isResourceManagerNode(NodeConf nodeConf) {
		return this.resourceManagerNode.getPrivateIp().equals(nodeConf.getPrivateIp());
	}
	
	/**
	 * Gets the bashrc contents.
	 *
	 * @return the bashrc contents
	 */
	@JsonIgnore
	public String getBashrcContents() {
		String hadoopHome = this.getComponentHome();
		StringBuilder sb = new StringBuilder();
		sb.append("export " + HadoopUtils.KEY_HADOOP_HOME + "=").append(hadoopHome).append(Constant.LINE_SEPERATOR);
		sb.append("export HADOOP_PREFIX=").append(hadoopHome).append(Constant.LINE_SEPERATOR);
		sb.append("export HADOOP_MAPRED_HOME=").append(hadoopHome).append(Constant.LINE_SEPERATOR);
		sb.append("export HADOOP_COMMON_HOME=").append(hadoopHome).append(Constant.LINE_SEPERATOR);
		sb.append("export HADOOP_HDFS_HOME=").append(hadoopHome).append(Constant.LINE_SEPERATOR);
		sb.append("export YARN_HOME=").append(hadoopHome).append(Constant.LINE_SEPERATOR);
		sb.append("export HADOOP_CONF_DIR=").append(hadoopHome)
				.append(Hadoop2Deployer.RELPATH_CONF_DIR + Constant.LINE_SEPERATOR);
		sb.append("export YARN_CONF_DIR=").append(hadoopHome)
				.append(Hadoop2Deployer.RELPATH_CONF_DIR + Constant.LINE_SEPERATOR);

		return sb.toString();
	}

	/**
	 * Gets the node list for ssh.
	 *
	 * @return the node list for ssh
	 */
	@JsonIgnore
	public Set<NodeConf> getNodeListForSsh(String role) {
		Set<NodeConf> nodeListForSshSetup = new HashSet<NodeConf>();
		nodeListForSshSetup.addAll(this.getSlaves());
		
		if(role.equals(Constant.Role.NAMENODE)) {
			nodeListForSshSetup.add(this.getNamenode());	
			if(this.isHaEnabled()) {
				nodeListForSshSetup.add(this.getStandByNamenode());
				nodeListForSshSetup.addAll(this.getJournalNodes());
				if(this.isAutomaticFailoverEnabled()) {
					nodeListForSshSetup.addAll(this.getZkQuorumNodes());	
				}
			} else if(this.getSecondaryNamenode() != null) {
				nodeListForSshSetup.add(this.getSecondaryNamenode());
			}
		} else if(role.equals(Constant.Role.RESOURCEMANAGER)) {
			nodeListForSshSetup.add(this.getResourceManagerNode());
			if(this.startJobHistoryServer) {
				nodeListForSshSetup.add(this.getJobHistoryServerNode());
			}	
		}
		return nodeListForSshSetup;
	}
	
	/**
	 * Checks if is namenode.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @return true, if is namenode
	 */
	@Override
	public boolean isNamenode(NodeConf nodeConf) {
		if(this.isHaEnabled()) {
			if(this.getHaNameNodesPublicIp().contains(nodeConf.getPublicIp())) {
				return true;	
			}
		} else {
			return this.getNamenode().equals(nodeConf);
		}
		return false;
	}
	
	@Override
	public String getHdfsUri() {
		String hdfsUri = "";
		if(this.isHaEnabled()) {
			hdfsUri = HadoopUtils.HADOOP_URI_PREFIX + this.getNameserviceId();
		} else {
			hdfsUri = HadoopUtils.HADOOP_URI_PREFIX + this.getNamenode().getPrivateIp() 
					 + ":" + Hadoop2Deployer.DEFAULT_PORT_RPC_NAMENODE;
		}
		hdfsUri += "/";
		return hdfsUri;
	}
}
