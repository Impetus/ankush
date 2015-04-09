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
package com.impetus.ankush.common.zookeeper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ComponentJsonMapper;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.framework.config.RegisterClusterConf;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush.common.utils.JsonMapperUtil;

/**
 * The Class ZookeeperConf.
 * 
 * @author mayur
 */
public class ZookeeperConf extends GenericConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The Constant defaultZkClientPort. */
	public static final String defaultZkClientPort = "2182";

	/** The node confs. */
	private List<NodeConf> nodeConfs = new ArrayList<NodeConf>();

	/** The data directory. */
	private String dataDirectory;

	/** The sync limit. */
	private int syncLimit;

	/** The init limit. */
	private int initLimit;

	/** The client port. */
	private int clientPort;

	/** The tick time. */
	private int tickTime;

	/** The JMX Port. */
	private String jmxPort = "12345";

	/**
	 * Default Constructor.
	 */
	public ZookeeperConf() {
		super();
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<NodeConf> getNodes() {
		return nodeConfs;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodeConfs
	 *            the nodes to set
	 */
	public void setNodes(List<NodeConf> nodeConfs) {
		this.nodeConfs = nodeConfs;
	}

	/**
	 * Gets the data directory.
	 * 
	 * @return the dataDirectory
	 */
	public String getDataDirectory() {
		return dataDirectory;
	}

	/**
	 * Sets the data directory.
	 * 
	 * @param dataDirectory
	 *            the dataDirectory to set
	 */
	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	/**
	 * Gets the sync limit.
	 * 
	 * @return the syncLimit
	 */
	public int getSyncLimit() {
		return syncLimit;
	}

	/**
	 * Sets the sync limit.
	 * 
	 * @param syncLimit
	 *            the syncLimit to set
	 */
	public void setSyncLimit(int syncLimit) {
		this.syncLimit = syncLimit;
	}

	/**
	 * Gets the inits the limit.
	 * 
	 * @return the initLimit
	 */
	public int getInitLimit() {
		return initLimit;
	}

	/**
	 * Sets the inits the limit.
	 * 
	 * @param initLimit
	 *            the initLimit to set
	 */
	public void setInitLimit(int initLimit) {
		this.initLimit = initLimit;
	}

	/**
	 * Gets the client port.
	 * 
	 * @return the clientPort
	 */
	public int getClientPort() {
		return clientPort;
	}

	/**
	 * Sets the client port.
	 * 
	 * @param clientPort
	 *            the clientPort to set
	 */
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	/**
	 * Gets the tick time.
	 * 
	 * @return the tickTime
	 */
	public int getTickTime() {
		return tickTime;
	}

	/**
	 * Sets the tick time.
	 * 
	 * @param tickTime
	 *            the tickTime to set
	 */
	public void setTickTime(int tickTime) {
		this.tickTime = tickTime;
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
	 * Gets the zoo conf contents.
	 * 
	 * @return zoo.cfg file contents
	 */
	@JsonIgnore
	public String getZooConfContents() {

		// Building first part of zoo.cfg file
		StringBuilder partOneBuilder = new StringBuilder();
		String dataDir = this.dataDirectory;
		if((dataDir != null) && (dataDir.endsWith("/"))){
			dataDir = dataDir.substring(0, dataDir.length()-1);
		}
		partOneBuilder.append("dataDir=").append(dataDir)
				.append("\n").append("clientPort=").append(this.clientPort)
				.append("\n").append("tickTime=").append(this.tickTime)
				.append("\n").append("initLimit=").append(this.initLimit)
				.append("\n").append("syncLimit=").append(this.syncLimit)
				.append("\n");

		// Building second part of zoo.cfg file
		int i = 1;
		StringBuilder partTwoBuilder = new StringBuilder();
		for (Iterator<NodeConf> iterator = this.nodeConfs.iterator(); iterator
				.hasNext();) {

			String hostName = "";
			if (this.nodeConfs.get(i - 1) != null)
//				hostName = HostOperation.getAnkushHostName(this.nodeConfs.get(
//						i - 1).getPrivateIp());
			hostName = this.nodeConfs.get(i - 1).getPrivateIp();
			partTwoBuilder.append("server.").append(i).append("=")
					.append(hostName).append(":2888:3888").append("\n");
			++i;
			iterator.next();
		}

		// merging both buffers
		StringBuilder zooConfContent = partOneBuilder.append(partTwoBuilder);

		return zooConfContent.toString();
	}

	/**
	 * Gets the hosts file contents.
	 * 
	 * @return /etc/hosts file contents
	 */
	@JsonIgnore
	public String getHostsFileContents() {
		int i = 1;
		StringBuilder hostsFileBuilder = new StringBuilder();
		for (Iterator<NodeConf> iterator = this.nodeConfs.iterator(); iterator
				.hasNext();) {
			// for cloud and non-cloud deployments
			String nodeIP = iterator.next().getPrivateIp();

			hostsFileBuilder.append(nodeIP).append("\t").append("zoo")
					.append(i).append("\n");
			++i;
		}
		return hostsFileBuilder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ZookeeperConf [nodes=" + nodeConfs + ", dataDirectory="
				+ dataDirectory + ", syncLimit=" + syncLimit + ", initLimit="
				+ initLimit + ", clientPort=" + clientPort + ", tickTime="
				+ tickTime + ", getNodes()=" + getNodes()
				+ ", getDataDirectory()=" + getDataDirectory()
				+ ", getSyncLimit()=" + getSyncLimit() + ", getInitLimit()="
				+ getInitLimit() + ", getClientPort()=" + getClientPort()
				+ ", getTickTime()=" + getTickTime()
				+ ", getZooConfContents()=" + getZooConfContents()
				+ ", getHostsFileContents()=" + getHostsFileContents()
				+ ", getUsername()=" + getUsername() + ", getPassword()="
				+ getPassword() + ", getComponentVersion()="
				+ getComponentVersion() + ", getInstallationPath()="
				+ getInstallationPath() + ", getLocalBinaryFile()="
				+ getLocalBinaryFile() + ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", toString()=" + super.toString() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + "]";
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
		return new HashSet<NodeConf>(this.getNodes());
	}
	
	@Override
	public void addNewNodes() {
		this.getNodes().addAll(this.getNewNodes());
	}

	
}
