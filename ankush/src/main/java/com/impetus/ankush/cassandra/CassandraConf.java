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
package com.impetus.ankush.cassandra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.FileNameUtils;

/**
 * The Class CassandraConf.
 */
public class CassandraConf extends GenericConfiguration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The nodes. */
	private Set<CassandraNodeConf> nodes;

	/** The datacenter count. */
	private int datacenterCount;

	/** The partitioner. */
	private String partitioner = null;

	/** The rpc port. */
	private String rpcPort = null;

	/** The storage port. */
	private String storagePort = null;

	/** The dataDir */
	private String dataDir = null;

	/** The savedCacheDir */
	private String savedCachesDir = null;

	/** The commitLogDir */
	private String commitlogDir = null;

	/** The logDir */
	private String logDir = null;

	/** The snitch. */
	private String snitch = null;

	/** The rackEnabled. */
	private boolean rackEnabled;

	/** The vNodeEnabled. */
	private boolean vNodeEnabled = true;

	/** The JMX Port. */
	private String jmxPort = "7199";

	/**
	 * Instantiates a new Cassandra the conf.
	 * 
	 */
	public CassandraConf() {
		this.partitioner = new String(
				"org.apache.cassandra.dht.RandomPartitioner");
		this.rpcPort = new String("9160");
		this.storagePort = new String("7000");
		this.dataDir = new String("/home/cassandraDataDir");
		this.logDir = new String("/home/cassandralogDir");
		this.snitch = new String("SimpleSnitch");
		this.vNodeEnabled = true;
		this.savedCachesDir = new String("/home/cassandraSavedCachesDir");
		this.commitlogDir = new String("/home/cassandraCommitlogDir");
	}

	public Set<NodeConf> getSeedNodes() {
		Set<NodeConf> nodes = new HashSet<NodeConf>();
		for (CassandraNodeConf node : getNodes()) {
			if (node.isSeedNode()) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public Set<NodeConf> getNonSeedNodes() {
		Set<NodeConf> nodes = new HashSet<NodeConf>();
		for (CassandraNodeConf node : getNodes()) {
			if (!node.isSeedNode()) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * Gets the datacenter count.
	 * 
	 * @return the datacenterCount
	 */
	public int getDatacenterCount() {
		return this.datacenterCount;
	}

	/**
	 * Sets the datacenter count.
	 * 
	 * @param datacenterCount
	 *            the datacenterCount to set
	 */
	public void setDatacenterCount(int datacenterCount) {
		this.datacenterCount = datacenterCount;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public Set<CassandraNodeConf> getNodes() {
		return this.nodes;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(Set<CassandraNodeConf> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the partitioner.
	 * 
	 * @return the partitioner
	 */
	public String getPartitioner() {
		return this.partitioner;
	}

	/**
	 * Sets the partitioner.
	 * 
	 * @param partitioner
	 *            the partitioner to set
	 */
	public void setPartitioner(String partitioner) {
		this.partitioner = partitioner;
	}

	/**
	 * Gets the rpc port.
	 * 
	 * @return the rpcPort
	 */
	public String getRpcPort() {
		return this.rpcPort;
	}

	/**
	 * Sets the rpc port.
	 * 
	 * @param rpcPort
	 *            the rpcPort to set
	 */
	public void setRpcPort(String rpcPort) {
		this.rpcPort = rpcPort;
	}

	/**
	 * Gets the storage port.
	 * 
	 * @return the storagePort
	 */
	public String getStoragePort() {
		return this.storagePort;
	}

	/**
	 * Sets the storage port.
	 * 
	 * @param storagePort
	 *            the storagePort to set
	 */
	public void setStoragePort(String storagePort) {
		this.storagePort = storagePort;
	}

	/**
	 * @return the dataDir
	 */
	public String getDataDir() {
		return FileNameUtils.convertToValidPath(this.dataDir);
	}

	/**
	 * @param dataDir
	 *            the dataDir to set
	 */
	public void setDataDir(String dataDir) {
		this.dataDir = FileNameUtils.convertToValidPath(dataDir);
	}

	/**
	 * @return the logDir
	 */
	public String getLogDir() {
		return FileNameUtils.convertToValidPath(this.logDir);
	}

	/**
	 * @param logDir
	 *            the logDir to set
	 */
	public void setLogDir(String logDir) {
		this.logDir = FileNameUtils.convertToValidPath(logDir);
	}

	/**
	 * @return the snitch
	 */
	public String getSnitch() {
		return snitch;
	}

	/**
	 * @param snitch
	 *            the snitch to set
	 */
	public void setSnitch(String snitch) {
		this.snitch = snitch;
	}

	/**
	 * @return the rackEnabled
	 */
	public boolean isRackEnabled() {
		return rackEnabled;
	}

	/**
	 * @param rackEnabled
	 *            the rackEnabled to set
	 */
	public void setRackEnabled(boolean rackEnabled) {
		this.rackEnabled = rackEnabled;
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

	@Override
	public Set<NodeConf> getCompNodes() {
		return new HashSet<NodeConf>(nodes);
	}

	/**
	 * @return the vNodeEnabled
	 */
	public boolean isvNodeEnabled() {
		return vNodeEnabled;
	}

	/**
	 * @param vNodeEnabled
	 *            the vNodeEnabled to set
	 */
	public void setvNodeEnabled(boolean vNodeEnabled) {
		this.vNodeEnabled = vNodeEnabled;
	}

	/**
	 * @return the savedCachesDir
	 */
	public String getSavedCachesDir() {
		return savedCachesDir;
	}

	/**
	 * @param savedCachesDir
	 *            the savedCachesDir to set
	 */
	public void setSavedCachesDir(String savedCachesDir) {
		this.savedCachesDir = savedCachesDir;
	}

	/**
	 * @return the commitlogDir
	 */
	public String getCommitlogDir() {
		return commitlogDir;
	}

	/**
	 * @param commitlogDir
	 *            the commitlogDir to set
	 */
	public void setCommitlogDir(String commitlogDir) {
		this.commitlogDir = commitlogDir;
	}

	/**
	 * Gets the yaml contents.
	 * 
	 * @return yaml file contents
	 */
	@JsonIgnore
	public String getYamlContents(CassandraNodeConf cNodeConf) {
		Yaml yaml = new Yaml();
		Map yamlContents = getConfigurationMap(cNodeConf);

		return yaml.dumpAsMap(yamlContents);
	}

	/**
	 * Gets the configuration map.
	 * 
	 * @return the configuration map
	 */
	@JsonIgnore
	public Map<String, Object> getConfigurationMap(CassandraNodeConf cNodeConf) {
		Map<String, Object> yamlContents = new HashMap<String, Object>();
		yamlContents.put("cluster_name", this.getClusterName());
		yamlContents.put("partitioner", this.partitioner);
		yamlContents.put("rpc_port", this.rpcPort);
		yamlContents.put("storage_port", this.storagePort);
		yamlContents.put("listen_address", cNodeConf.getPublicIp());
		yamlContents.put("rpc_address", cNodeConf.getPublicIp());
		yamlContents.put("endpoint_snitch", this.snitch);
		yamlContents.put("saved_caches_directory", this.savedCachesDir);
		yamlContents.put("commitlog_directory", this.commitlogDir);
		return yamlContents;
	}

	public void setCassandraNewNodes(List<CassandraNodeConf> newNodes) {
		setNewNodes(new ArrayList<NodeConf>(newNodes));
	}

	// Modified by Akhil
	// Special Case for Handling Cassandra Process Name in the Service List
	// NEEDS to be changed when we upgrade AGENT to handle jps process status as
	// Technology - Process Map
	@JsonIgnore
	public static boolean isSeedNode(CassandraConf conf, NodeConf nodeConf) {
		for (CassandraNodeConf cassandraNode : conf.nodes) {
			if (cassandraNode.getPublicIp().equals(nodeConf.getPublicIp())) {
				if (cassandraNode.isSeedNode()) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public void addNewNodes() {
		// Assuming that the NewNodes list will have HadoopNodeConf objects
		// No validations for the type caste operation
		for (NodeConf nodeConf : this.getNewNodes()) {
			CassandraNodeConf cNodeConf = (CassandraNodeConf) nodeConf;
			this.nodes.add(cNodeConf);
		}

	}
}
