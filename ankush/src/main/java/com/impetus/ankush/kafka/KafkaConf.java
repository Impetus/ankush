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
package com.impetus.ankush.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class KafkaConf.
 * 
 * @author mayur
 */
public class KafkaConf extends GenericConfiguration {

	private static final long serialVersionUID = 1L;

	/** The node conf. */
	private List<NodeConf> nodes = new ArrayList<NodeConf>();

	/** The zk nodes. */
	private HashMap<String, Object> zkNodesPort = new HashMap<String, Object>();

	/** The logDir. */
	private String logDir = "/tmp/kafka-logs";

	/** The port. */
	private int port = 9092;

	/** The replication_factor. */
	private int replicationFactor = -1;

	/** The lastNodeId */
	private int lastNodeId = 0;

	/** The JMX Port. */
	private String jmxPort = "9999";

	/** The num of network threads. */
	private int numOfNetworkThreads = 3;

	/** The num of IO Threads. */
	private int numOfIOThreads = 8;

	/** The queued Max Requests. */
	private int queuedMaxRequests = 500;

	/** The num of Partitions. */
	private int numPartitions = 1;

	/** The log Retentiiton Hours. */
	private int logRetentionHours = 168;

	/** The log Retentition Bytes. */
	private long logRetentitionBytes = 1073741824;

	/** The log cleanup Interval Minutes */
	private int logCleanupIntervalMins = 10;

	/** The log flush Interval Messages. */
	private int logFlushIntervalMessage = 10000;

	/** The log Flush Schedular Interval MilliSeconds */
	private int logFlushSchedularIntervalMs = 3000;

	/** The log flush Interval MillSeconds. */
	private int logFlushIntervalMs = 3000;

	/** The controlled Shutdown. */
	private boolean controlledShutdownEnable = false;

	/** The controlled Shutdown MaxRetries. */
	private int controlledShutdownMaxRetries = 3;
	
	/** The brokerIdIPMap. */
	private Map<String, String> brokerIdIPMap = new HashMap<String, String>();

	/**
	 * @return the lastNodeId
	 */
	public int getLastNodeId() {
		return lastNodeId;
	}

	/**
	 * @return the logDir
	 */
	public String getLogDir() {
		return logDir;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodeList
	 */
	public List<NodeConf> getNodes() {
		return nodes;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the replicationFactor
	 */
	public int getReplicationFactor() {
		return replicationFactor;
	}

	/**
	 * @param replicationFactor
	 *            the replicationFactor to set
	 */
	public void setReplicationFactor(int replicationFactor) {
		this.replicationFactor = replicationFactor;
	}

	/**
	 * @return the zkNodes
	 */
	public HashMap<String, Object> getZkNodesPort() {
		return zkNodesPort;
	}

	/**
	 * @param lastNodeId
	 *            the lastNodeId to set
	 */
	public void setLastNodeId(int lastNodeId) {
		this.lastNodeId = lastNodeId;
	}

	/**
	 * @param logDir
	 *            the logDir to set
	 */
	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodeList
	 *            the nodeList to set
	 */
	public void setNodes(List<NodeConf> nodeList) {
		nodes = nodeList;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param zkNodes
	 *            the zkNodes to set
	 */
	public void setZkNodesPort(HashMap<String, Object> zkNodesPort) {
		this.zkNodesPort = zkNodesPort;
	}

	@Override
	public Set<NodeConf> getCompNodes() {
		return new HashSet<NodeConf>(nodes);
	}

	/**
	 * @return the jmxPort
	 */
	public String getJmxPort() {
		return jmxPort;
	}

	/**
	 * @param jmxPort
	 *            the jmxPort to set
	 */
	public void setJmxPort(String jmxPort) {
		this.jmxPort = jmxPort;
	}

	/**
	 * @return the numOfNetworkThreads
	 */
	public int getNumOfNetworkThreads() {
		return numOfNetworkThreads;
	}

	/**
	 * @param numOfNetworkThreads
	 *            the numOfNetworkThreads to set
	 */
	public void setNumOfNetworkThreads(int numOfNetworkThreads) {
		this.numOfNetworkThreads = numOfNetworkThreads;
	}

	/**
	 * @return the numOfIOThreads
	 */
	public int getNumOfIOThreads() {
		return numOfIOThreads;
	}

	/**
	 * @param numOfIOThreads
	 *            the numOfIOThreads to set
	 */
	public void setNumOfIOThreads(int numOfIOThreads) {
		this.numOfIOThreads = numOfIOThreads;
	}

	/**
	 * @return the queuedMaxRequests
	 */
	public int getQueuedMaxRequests() {
		return queuedMaxRequests;
	}

	/**
	 * @param queuedMaxRequests
	 *            the queuedMaxRequests to set
	 */
	public void setQueuedMaxRequests(int queuedMaxRequests) {
		this.queuedMaxRequests = queuedMaxRequests;
	}

	/**
	 * @return the numPartitions
	 */
	public int getNumPartitions() {
		return numPartitions;
	}

	/**
	 * @param numPartitions
	 *            the numPartitions to set
	 */
	public void setNumPartitions(int numPartitions) {
		this.numPartitions = numPartitions;
	}

	/**
	 * @return the logRetentionHours
	 */
	public int getLogRetentionHours() {
		return logRetentionHours;
	}

	/**
	 * @param logRetentionHours
	 *            the logRetentionHours to set
	 */
	public void setLogRetentionHours(int logRetentionHours) {
		this.logRetentionHours = logRetentionHours;
	}

	/**
	 * @return the logRetentitionBytes
	 */
	public long getLogRetentitionBytes() {
		return logRetentitionBytes;
	}

	/**
	 * @param logRetentitionBytes
	 *            the logRetentitionBytes to set
	 */
	public void setLogRetentitionBytes(long logRetentitionBytes) {
		this.logRetentitionBytes = logRetentitionBytes;
	}

	/**
	 * @return the logCleanupIintervalMins
	 */
	public int getLogCleanupIntervalMins() {
		return logCleanupIntervalMins;
	}

	/**
	 * @param logCleanupIntervalMins
	 *            the logCleanupIintervalMins to set
	 */
	public void setLogCleanupIntervalMins(int logCleanupIntervalMins) {
		this.logCleanupIntervalMins = logCleanupIntervalMins;
	}

	/**
	 * @return the logFlushIntervalMessage
	 */
	public int getLogFlushIntervalMessage() {
		return logFlushIntervalMessage;
	}

	/**
	 * @param logFlushIntervalMessage
	 *            the logFlushIntervalMessage to set
	 */
	public void setLogFlushIntervalMessage(int logFlushIntervalMessage) {
		this.logFlushIntervalMessage = logFlushIntervalMessage;
	}

	/**
	 * @return the logFlushSchedularIntervalMs
	 */
	public int getLogFlushSchedularIntervalMs() {
		return logFlushSchedularIntervalMs;
	}

	/**
	 * @param logFlushSchedularIntervalMs
	 *            the logFlushSchedularIntervalMs to set
	 */
	public void setLogFlushSchedularIntervalMs(int logFlushSchedularIntervalMs) {
		this.logFlushSchedularIntervalMs = logFlushSchedularIntervalMs;
	}

	/**
	 * @return the logFlushIntervalMs
	 */
	public int getLogFlushIntervalMs() {
		return logFlushIntervalMs;
	}

	/**
	 * @param logFlushIntervalMs
	 *            the logFlushIntervalMs to set
	 */
	public void setLogFlushIntervalMs(int logFlushIntervalMs) {
		this.logFlushIntervalMs = logFlushIntervalMs;
	}

	/**
	 * @return the controlledShutdownEnable
	 */
	public boolean getControlledShutdownEnable() {
		return controlledShutdownEnable;
	}

	/**
	 * @param controlledShutdownEnable
	 *            the controlledShutdownEnable to set
	 */
	public void setControlledShutdownEnable(boolean controlledShutdownEnable) {
		this.controlledShutdownEnable = controlledShutdownEnable;
	}

	/**
	 * @return the controlledShutdownMaxRetries
	 */
	public int getControlledShutdownMaxRetries() {
		return controlledShutdownMaxRetries;
	}

	/**
	 * @param controlledShutdownMaxRetries
	 *            the controlledShutdownMaxRetries to set
	 */
	public void setControlledShutdownMaxRetries(int controlledShutdownMaxRetries) {
		this.controlledShutdownMaxRetries = controlledShutdownMaxRetries;
	}

	/**
	 * @return the brokerIdIPMap
	 */
	public Map<String, String> getBrokerIdIPMap() {
		return brokerIdIPMap;
	}

	/**
	 * @param brokerIdIPMap
	 *            the brokerIdIPMap to set
	 */
	public void setBrokerIdIPMap(Map<String, String> brokerIdIPMap) {
		this.brokerIdIPMap = brokerIdIPMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KafkaConf [nodeList=" + nodes + ", zkNodesPort=" + zkNodesPort
				+ ", logDir=" + logDir + ", port=" + port + ", jmxPort="
				+ jmxPort + ",numOfNetworkThreads=" + numOfNetworkThreads
				+ ", numOfIOThreads=" + numOfIOThreads + ", queuedMaxRequests="
				+ queuedMaxRequests + ", numPartitions=" + numPartitions
				+ ", logRetentionHours=" + logRetentionHours
				+ ", logRetentitionBytes=" + logRetentitionBytes
				+ ", logCleanupIntervalMins=" + logCleanupIntervalMins
				+ ",logFlushIntervalMessage=" + logFlushIntervalMessage
				+ ",logFlushSchedularIntervalMs=" + logFlushSchedularIntervalMs
				+ ", logFlushIntervalMs=" + logFlushIntervalMs
				+ ", controlledShutdownEnable=" + controlledShutdownEnable
				+ ",controlledShutdownMaxRetries="
				+ controlledShutdownMaxRetries + ",getNodeList()=" + getNodes()
				+ ",getZkNodesPort()=" + getZkNodesPort() + ",getLogDir()="
				+ getLogDir() + "getPort()=" + getPort() + "getJmxPort()="
				+ getJmxPort() + "getNumOfNetworkThreads()="
				+ getNumOfNetworkThreads() + "getNumOfIOThreads()="
				+ getNumOfIOThreads() + "getQueuedMaxRequests()="
				+ getQueuedMaxRequests() + "getNumPartitions()="
				+ getNumPartitions() + "getLogRetentionHours()="
				+ getLogRetentionHours() + "getLogRetentitionBytes()="
				+ getLogRetentitionBytes() + "getLogCleanupIntervalMins()="
				+ getLogCleanupIntervalMins()
				+ "getLogFlushIntervalMessage()="
				+ getLogFlushIntervalMessage()
				+ "getLogFlushSchedularIntervalMs()="
				+ getLogFlushSchedularIntervalMs() + "getLogFlushIntervalMs()="
				+ getLogFlushIntervalMs() + "getControlledShutdownEnable()="
				+ getControlledShutdownEnable()
				+ "getControlledShutdownMaxRetries()="
				+ getControlledShutdownMaxRetries() + ", getUsername()="
				+ getUsername() + ", getPassword()=" + getPassword()
				+ ", getComponentVersion()=" + getComponentVersion()
				+ ", getInstallationPath()=" + getInstallationPath()
				+ ", getLocalBinaryFile()=" + getLocalBinaryFile()
				+ ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", getPrivateKey()=" + getPrivateKey()
				+ ", getComponentHome()=" + getComponentHome()
				+ ", toString()=" + super.toString() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + "]";
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#addNewNodes()
	 */
	@Override
	public void addNewNodes() {
		this.getNodes().addAll(this.getNewNodes());
	}
}
