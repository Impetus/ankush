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
 * Oracle NoSQL configuration object
 */
package com.impetus.ankush.oraclenosql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * OracleNoSQL cluster configuration used to perform cluster level operations.
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLConf extends GenericConfiguration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant DEFAULT_REP_FACTOR. */
	private static final int DEFAULT_REP_FACTOR = 3;

	/** The Constant DEFAULT_REGISTRY_PORT. */
	private static final int DEFAULT_REGISTRY_PORT = 5000;

	/** The Constant DEFAULT_HA_PORT_RANGE_START. */
	private static final int DEFAULT_HA_PORT_RANGE_START = 5010;

	/** The Constant DEFAULT_HA_PORT_RANGE_END. */
	private static final int DEFAULT_HA_PORT_RANGE_END = 5020;

	/** The Constant DEFAULT_PARTITIONS_COUNT. */
	private static final long DEFAULT_PARTITIONS_COUNT = 100;

	/** The Constant DEFAULT_STRING. */
	private static final String DEFAULT_STRING = "";

	/** The topology name. */
	private String topologyName;

	/** The datacenter name. */
	private String datacenterName;

	/** The replication factor. */
	private Integer replicationFactor;

	/** The partition count. */
	private Long partitionCount;

	/** The ntp server. */
	private String ntpServer;

	/** The data path. */
	private String dataPath;

	/** The nodes. */
	private List<OracleNoSQLNodeConf> nodes;

	/** The base path. */
	private String basePath;

	/** The registry port. */
	private Integer registryPort;

	/** The ha port range start. */
	private Integer haPortRangeStart;

	/** The ha port range end. */
	private Integer haPortRangeEnd;

	/** The tile id. */
	private Long tileId;

	/** The datacenter list. */
	private List<OracleNoSQLDatacenter> datacenters;

	/**
	 * Constructor to set default values.
	 */
	public OracleNoSQLConf() {
		topologyName = DEFAULT_STRING;
		datacenterName = DEFAULT_STRING;
		replicationFactor = Integer.valueOf(DEFAULT_REP_FACTOR);
		partitionCount = Long.valueOf(DEFAULT_PARTITIONS_COUNT);
		ntpServer = DEFAULT_STRING;
		dataPath = DEFAULT_STRING;
		nodes = new ArrayList<OracleNoSQLNodeConf>();
		basePath = DEFAULT_STRING;
		registryPort = Integer.valueOf(DEFAULT_REGISTRY_PORT);
		haPortRangeStart = Integer.valueOf(DEFAULT_HA_PORT_RANGE_START);
		haPortRangeEnd = Integer.valueOf(DEFAULT_HA_PORT_RANGE_END);
		datacenters = null;
	}

	/**
	 * Adds the error.
	 * 
	 * @param key
	 *            the key
	 * @param error
	 *            the error to set
	 */
	public void addError(String key, String error) {
		this.getClusterConf().getErrors().put(key, error);
	}

	/**
	 * Gets the data path.
	 * 
	 * @return the dataPath
	 */
	public String getDataPath() {
		return dataPath;
	}

	/**
	 * Sets the data path.
	 * 
	 * @param dataPath
	 *            the dataPath to set
	 */
	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	/**
	 * Gets the kv store jar path.
	 * 
	 * @return the kvStoreJarPath
	 */
	@JsonIgnore
	public String getKvStoreJarPath() {
		return getInstallationPath() + "kv-" + getComponentVersion()
				+ "/lib/kvstore.jar";
	}

	/**
	 * Gets the registry port.
	 * 
	 * @return the registryPort
	 */
	public Integer getRegistryPort() {
		return registryPort;
	}

	/**
	 * Sets the registry port.
	 * 
	 * @param registryPort
	 *            the registryPort to set
	 */
	public void setRegistryPort(Integer registryPort) {
		this.registryPort = registryPort;
	}

	/**
	 * Gets the ha port range start.
	 * 
	 * @return the haPortRangeStart
	 */
	public Integer getHaPortRangeStart() {
		return haPortRangeStart;
	}

	/**
	 * Sets the ha port range start.
	 * 
	 * @param haPortRangeStart
	 *            the haPortRangeStart to set
	 */
	public void setHaPortRangeStart(Integer haPortRangeStart) {
		this.haPortRangeStart = haPortRangeStart;
	}

	/**
	 * Gets the ha port range end.
	 * 
	 * @return the haPortRangeEnd
	 */
	public Integer getHaPortRangeEnd() {
		return haPortRangeEnd;
	}

	/**
	 * Sets the ha port range end.
	 * 
	 * @param haPortRangeEnd
	 *            the haPortRangeEnd to set
	 */
	public void setHaPortRangeEnd(Integer haPortRangeEnd) {
		this.haPortRangeEnd = haPortRangeEnd;
	}

	/**
	 * Gets the topology name.
	 * 
	 * @return the topologyName
	 */
	public String getTopologyName() {
		return topologyName;
	}

	/**
	 * Sets the topology name.
	 * 
	 * @param topologyName
	 *            the topologyName to set
	 */
	public void setTopologyName(String topologyName) {
		this.topologyName = topologyName;
	}

	/**
	 * Gets the datacenter name.
	 * 
	 * @return the datacenterName
	 */
	public String getDatacenterName() {
		return datacenterName;
	}

	/**
	 * Sets the datacenter name.
	 * 
	 * @param datacenterName
	 *            the datacenterName to set
	 */
	public void setDatacenterName(String datacenterName) {
		this.datacenterName = datacenterName;
	}

	/**
	 * Gets the replication factor.
	 * 
	 * @return the replicationFactor
	 */
	public Integer getReplicationFactor() {
		return replicationFactor;
	}

	/**
	 * Sets the replication factor.
	 * 
	 * @param replicationFactor
	 *            the replicationFactor to set
	 */
	public void setReplicationFactor(Integer replicationFactor) {
		this.replicationFactor = replicationFactor;
	}

	/**
	 * Gets the partition count.
	 * 
	 * @return the partitionsCount
	 */
	public Long getPartitionCount() {
		return partitionCount;
	}

	/**
	 * Sets the partition count.
	 * 
	 * @param partitionCount
	 *            the partitionsCount to set
	 */
	public void setPartitionCount(Long partitionCount) {
		this.partitionCount = partitionCount;
	}

	/**
	 * Gets the ntp server.
	 * 
	 * @return the ntpServer
	 */
	public String getNtpServer() {
		return ntpServer;
	}

	/**
	 * Sets the ntp server.
	 * 
	 * @param ntpServer
	 *            the ntpServer to set
	 */
	public void setNtpServer(String ntpServer) {
		this.ntpServer = ntpServer;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<OracleNoSQLNodeConf> getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<OracleNoSQLNodeConf> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the base path.
	 * 
	 * @return the basePath
	 */
	public String getBasePath() {
		return basePath;
	}

	/**
	 * Sets the base path.
	 * 
	 * @param basePath
	 *            the basePath to set
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	/**
	 * Gets the tile id.
	 * 
	 * @return the tileId
	 */
	@JsonIgnore
	public Long getTileId() {
		return tileId;
	}

	/**
	 * Sets the tile id.
	 * 
	 * @param tileId
	 *            the tileId to set
	 */
	public void setTileId(Long tileId) {
		this.tileId = tileId;
	}

	/**
	 * Gets the comp nodes.
	 * 
	 * @return the comp nodes
	 */
	public Set<NodeConf> getCompNodes() {
		return new HashSet<NodeConf>(nodes);
	}

	/**
	 * Sets the oracle new nodes.
	 * 
	 * @param newNodes
	 *            the newNodes to set
	 */
	public void setOracleNewNodes(List<OracleNoSQLNodeConf> newNodes) {
		setNewNodes(new ArrayList<NodeConf>(newNodes));
	}

	/**
	 * Gets the datacenters.
	 *
	 * @return the datacenters
	 */
	public List<OracleNoSQLDatacenter> getDatacenters() {
		return datacenters;
	}

	/**
	 * Sets the datacenters.
	 *
	 * @param datacenters the datacenters to set
	 */
	public void setDatacenters(List<OracleNoSQLDatacenter> datacenters) {
		this.datacenters = datacenters;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#addNewNodes()
	 */
	@Override
	public void addNewNodes() {
		for (NodeConf node : this.getNewNodes()) {
			this.getNodes().add((OracleNoSQLNodeConf) node);
		}
	}
}
