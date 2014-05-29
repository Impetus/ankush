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
package com.impetus.ankush.elasticsearch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.HostOperation;

/**
 * The Class ElasticSearchConf.
 * 
 * @author mayur
 */
public class ElasticSearchConf extends GenericConfiguration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The data path. */
	private String dataPath = null;

	/** The action auto create index. */
	private boolean actionAutoCreateIndex = false;

	/** The bootstrap mlockall. */
	private boolean bootstrapMlockall = true;

	/** The heap size. */
	private String heapSize = "4g";

	/** The node max local storage nodes. */
	private int nodeMaxLocalStorageNodes = 1;

	/** The index number of shards. */
	private int indexNumberOfShards = 5;

	/** The index number of replicas. */
	private int indexNumberOfReplicas = 1;

	/** The http port. */
	private int httpPort = 9200;

	/** The transport http port. */
	private int transportTcpPort = 9300;

	/** The rackEnabled. */
	private boolean rackEnabled;

	/** The nodes. */
	private List<NodeConf> nodes = null;

	/**
	 * The Interface ESConfParams.
	 */
	public static interface ESConfParams {

		/** The Constant CLUSTER_NAME. */
		final static String CLUSTER_NAME = "cluster.name";

		/** The Constant PATH_DATA. */
		final static String PATH_DATA = "path.data";

		/** The Constant NODE_NAME. */
		final static String NODE_NAME = "node.name";

		/** The Constant NODE_TAG. */
		final static String NODE_TAG = "node.tag";

		/** The Constant NODE_RACK. */
		final static String NODE_RACK = "node.rack";

		/** The Constant ACTION_AUTO_CREATE_INDEX. */
		final static String ACTION_AUTO_CREATE_INDEX = "action.auto_create_index";

		/** The Constant BOOTSTRAP_MLOCKALL. */
		final static String BOOTSTRAP_MLOCKALL = "bootstrap.mlockall";

		/** The Constant NODE_MAX_LOCAL_STORAGE_NODES. */
		final static String NODE_MAX_LOCAL_STORAGE_NODES = "node.max_local_storage_nodes";

		/** The Constant INDEX_NUMBER_OF_SHARDS. */
		final static String INDEX_NUMBER_OF_SHARDS = "index.number_of_shards";

		/** The Constant INDEX_NUMBER_OF_REPLICAS. */
		final static String INDEX_NUMBER_OF_REPLICAS = "index.number_of_replicas";

		/** The Constant HTTP_PORT. */
		final static String HTTP_PORT = "http.port";

		/** The Constant TRANSPORT_TCP_PORT. */
		final static String TRANSPORT_TCP_PORT = "transport.tcp.port";
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
	 * Checks if is action auto create index.
	 * 
	 * @return the actionAutoCreateIndex
	 */
	public boolean isActionAutoCreateIndex() {
		return actionAutoCreateIndex;
	}

	/**
	 * Sets the action auto create index.
	 * 
	 * @param actionAutoCreateIndex
	 *            the actionAutoCreateIndex to set
	 */
	public void setActionAutoCreateIndex(boolean actionAutoCreateIndex) {
		this.actionAutoCreateIndex = actionAutoCreateIndex;
	}

	/**
	 * Checks if is bootstrap mlockall.
	 * 
	 * @return the bootstrapMlockall
	 */
	public boolean isBootstrapMlockall() {
		return bootstrapMlockall;
	}

	/**
	 * Sets the bootstrap mlockall.
	 * 
	 * @param bootstrapMlockall
	 *            the bootstrapMlockall to set
	 */
	public void setBootstrapMlockall(boolean bootstrapMlockall) {
		this.bootstrapMlockall = bootstrapMlockall;
	}

	/**
	 * Gets the heap size.
	 * 
	 * @return the heapSize
	 */
	public String getHeapSize() {
		return heapSize;
	}

	/**
	 * Sets the heap size.
	 * 
	 * @param heapSize
	 *            the heapSize to set
	 */
	public void setHeapSize(String heapSize) {
		this.heapSize = heapSize;
	}

	/**
	 * Gets the node max local storage nodes.
	 * 
	 * @return the nodeMaxLocalStorageNodes
	 */
	public int getNodeMaxLocalStorageNodes() {
		return nodeMaxLocalStorageNodes;
	}

	/**
	 * Sets the node max local storage nodes.
	 * 
	 * @param nodeMaxLocalStorageNodes
	 *            the nodeMaxLocalStorageNodes to set
	 */
	public void setNodeMaxLocalStorageNodes(int nodeMaxLocalStorageNodes) {
		this.nodeMaxLocalStorageNodes = nodeMaxLocalStorageNodes;
	}

	/**
	 * Gets the index number of shards.
	 * 
	 * @return the indexNumberOfShards
	 */
	public int getIndexNumberOfShards() {
		return indexNumberOfShards;
	}

	/**
	 * Sets the index number of shards.
	 * 
	 * @param indexNumberOfShards
	 *            the indexNumberOfShards to set
	 */
	public void setIndexNumberOfShards(int indexNumberOfShards) {
		this.indexNumberOfShards = indexNumberOfShards;
	}

	/**
	 * Gets the index number of replicas.
	 * 
	 * @return the indexNumberOfReplicas
	 */
	public int getIndexNumberOfReplicas() {
		return indexNumberOfReplicas;
	}

	/**
	 * Sets the index number of replicas.
	 * 
	 * @param indexNumberOfReplicas
	 *            the indexNumberOfReplicas to set
	 */
	public void setIndexNumberOfReplicas(int indexNumberOfReplicas) {
		this.indexNumberOfReplicas = indexNumberOfReplicas;
	}

	/**
	 * Gets the http port.
	 * 
	 * @return the httpPort
	 */
	public int getHttpPort() {
		return httpPort;
	}

	/**
	 * Sets the http port.
	 * 
	 * @param httpPort
	 *            the httpPort to set
	 */
	public void setHttpPort(int httpPort) {
		this.httpPort = httpPort;
	}

	/**
	 * @return the transportTcpPort
	 */
	public int getTransportTcpPort() {
		return transportTcpPort;
	}

	/**
	 * @param transportTcpPort
	 *            the transportTcpPort to set
	 */
	public void setTransportTcpPort(int transportTcpPort) {
		this.transportTcpPort = transportTcpPort;
	}

	/**
	 * Checks if is rack enabled.
	 * 
	 * @return the rackEnabled
	 */
	public boolean isRackEnabled() {
		return rackEnabled;
	}

	/**
	 * Sets the rack enabled.
	 * 
	 * @param rackEnabled
	 *            the rackEnabled to set
	 */
	public void setRackEnabled(boolean rackEnabled) {
		this.rackEnabled = rackEnabled;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public List<NodeConf> getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<NodeConf> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the yaml contents.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param esConf
	 *            the es conf
	 * @return yaml file contents
	 */
	@JsonIgnore
	public String getYamlContents(NodeConf nodeConf, ElasticSearchConf esConf) {
		Yaml yaml = new Yaml();
		Map yamlContents = getConfigurationMap(nodeConf, esConf);
		return yaml.dumpAsMap(yamlContents);
	}

	/**
	 * Gets the configuration map.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param esConf
	 *            the es conf
	 * @return the configuration map
	 */
	@JsonIgnore
	public Map<String, Object> getConfigurationMap(NodeConf nodeConf,
			ElasticSearchConf esConf) {
		Map<String, Object> yamlContents = new HashMap<String, Object>();
		yamlContents.put(ElasticSearchConf.ESConfParams.CLUSTER_NAME,
				this.getClusterName());
		yamlContents.put(ElasticSearchConf.ESConfParams.PATH_DATA,
				this.getDataPath());
		yamlContents.put(ElasticSearchConf.ESConfParams.NODE_NAME,
				HostOperation.getAnkushHostName(nodeConf.getPublicIp()));
		yamlContents.put(ElasticSearchConf.ESConfParams.NODE_TAG,
				HostOperation.getAnkushHostName(nodeConf.getPublicIp()));
		if (esConf.isRackEnabled()) {
			yamlContents.put(ElasticSearchConf.ESConfParams.NODE_RACK,
					nodeConf.getRack());
		}
		yamlContents.put(
				ElasticSearchConf.ESConfParams.ACTION_AUTO_CREATE_INDEX,
				this.isActionAutoCreateIndex());
		yamlContents.put(ElasticSearchConf.ESConfParams.BOOTSTRAP_MLOCKALL,
				this.isBootstrapMlockall());
		yamlContents.put(
				ElasticSearchConf.ESConfParams.NODE_MAX_LOCAL_STORAGE_NODES,
				this.getNodeMaxLocalStorageNodes());
		yamlContents.put(ElasticSearchConf.ESConfParams.INDEX_NUMBER_OF_SHARDS,
				this.getIndexNumberOfShards());
		yamlContents.put(
				ElasticSearchConf.ESConfParams.INDEX_NUMBER_OF_REPLICAS,
				this.getIndexNumberOfReplicas());
		yamlContents.put(ElasticSearchConf.ESConfParams.HTTP_PORT,
				this.getHttpPort());
		yamlContents.put(ElasticSearchConf.ESConfParams.TRANSPORT_TCP_PORT,
				this.getTransportTcpPort());
		return yamlContents;
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
		return new HashSet<NodeConf>(nodes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.GenericConfiguration#addNewNodes
	 * ()
	 */
	@Override
	public void addNewNodes() {
		this.getNodes().addAll(this.getNewNodes());
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
		result = prime * result + (actionAutoCreateIndex ? 1231 : 1237);
		result = prime * result + (bootstrapMlockall ? 1231 : 1237);
		result = prime * result
				+ ((dataPath == null) ? 0 : dataPath.hashCode());
		result = prime * result
				+ ((heapSize == null) ? 0 : heapSize.hashCode());
		result = prime * result + httpPort;
		result = prime * result + indexNumberOfReplicas;
		result = prime * result + indexNumberOfShards;
		result = prime * result + nodeMaxLocalStorageNodes;
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		result = prime * result + (rackEnabled ? 1231 : 1237);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElasticSearchConf other = (ElasticSearchConf) obj;
		if (actionAutoCreateIndex != other.actionAutoCreateIndex)
			return false;
		if (bootstrapMlockall != other.bootstrapMlockall)
			return false;
		if (dataPath == null) {
			if (other.dataPath != null)
				return false;
		} else if (!dataPath.equals(other.dataPath))
			return false;
		if (heapSize == null) {
			if (other.heapSize != null)
				return false;
		} else if (!heapSize.equals(other.heapSize))
			return false;
		if (httpPort != other.httpPort)
			return false;
		if (indexNumberOfReplicas != other.indexNumberOfReplicas)
			return false;
		if (indexNumberOfShards != other.indexNumberOfShards)
			return false;
		if (nodeMaxLocalStorageNodes != other.nodeMaxLocalStorageNodes)
			return false;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		if (rackEnabled != other.rackEnabled)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.GenericConfiguration#toString
	 * ()
	 */
	@Override
	public String toString() {
		return "ElasticSearchConf [dataPath=" + dataPath
				+ ", actionAutoCreateIndex=" + actionAutoCreateIndex
				+ ", bootstrapMlockall=" + bootstrapMlockall + ", heapSize="
				+ heapSize + ", nodeMaxLocalStorageNodes="
				+ nodeMaxLocalStorageNodes + ", index_number_of_shards="
				+ indexNumberOfShards + ", index_number_of_replicas="
				+ indexNumberOfReplicas + ", httpPort=" + httpPort
				+ ", transportTcpPort=" + transportTcpPort + ", nodes=" + nodes
				+ ", getDataPath()=" + getDataPath()
				+ ", isActionAutoCreateIndex()=" + isActionAutoCreateIndex()
				+ ", isBootstrapMlockall()=" + isBootstrapMlockall()
				+ ", getHeapSize()=" + getHeapSize()
				+ ", getNodeMaxLocalStorageNodes()="
				+ getNodeMaxLocalStorageNodes()
				+ ", getIndex_number_of_shards()=" + getIndexNumberOfShards()
				+ ", getIndex_number_of_replicas()="
				+ getIndexNumberOfReplicas() + ", getHttpPort()="
				+ getHttpPort() + ", getTransportTcpPort()="
				+ getTransportTcpPort() + ", getNodes()=" + getNodes()
				+ ", getUsername()=" + getUsername() + ", getPassword()="
				+ getPassword() + ", getComponentVersion()="
				+ getComponentVersion() + ", getInstallationPath()="
				+ getInstallationPath() + ", getLocalBinaryFile()="
				+ getLocalBinaryFile() + ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", getPrivateKey()=" + getPrivateKey()
				+ ", getComponentHome()=" + getComponentHome()
				+ ", getClass()=" + getClass();
	}
}
