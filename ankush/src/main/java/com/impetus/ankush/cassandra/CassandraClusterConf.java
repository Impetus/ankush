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
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class CassandraClusterConf.
 */
public class CassandraClusterConf extends ClusterConf {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cassandraConf. */
	private CassandraConf cassandra = new CassandraConf();
	/** The description. */
	private String description;
	
	/** The rackEnabled. */
	private boolean rackEnabled;
	
	/** The rackFileName. */
	private String rackFileName;
	
	/** New nodes **/
	private List<CassandraNodeConf> newNodes = new ArrayList<CassandraNodeConf>();

	/**
	 * @return the cassandraConf
	 */
	public CassandraConf getCassandra() {
		return cassandra;
	}

	/**
	 * @param cassandraConf
	 *            the cassandraConf to set
	 */
	public void setCassandra(CassandraConf cassandra) {
		this.cassandra = cassandra;
	}
	/**
	 * @return the isStatusError
	 */
	public boolean isStatusError() {
		return this.getState().equals(Constant.Cluster.State.ERROR);
	}

	/**
	 * Gets the node confs.
	 * 
	 * @return the nodes
	 */
	@Override
	public List<NodeConf> getNodeConfs() {
		return new ArrayList<NodeConf>(this.getCassandra().getNodes());
	}

	/**
	 * Gets the node count.
	 * 
	 * @return the size
	 */
	@Override
	public int getNodeCount() {
		if (this.getCassandra().getNodes() != null) {
			return this.getCassandra().getNodes().size();
		}
		return 0;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param newNodes
	 *            the newNodes to set
	 */
	public void setNewNodes(List<CassandraNodeConf> newNodes) {
		this.newNodes = newNodes;
	}

	/**
	 * @return the newNodes
	 */
	public List<CassandraNodeConf> getNewNodes() {
		return newNodes;
	}


	/**
	 * Method to get list of Cassandra seed nodes from cluster Conf.
	 * 
	 * @return the data nodes
	 */
	@JsonIgnore
	public List<NodeConf> getNonSeedNodes() {
		List<NodeConf> nodes = new ArrayList<NodeConf>();
		for (CassandraNodeConf node : this.getCassandra().getNodes()) {
			if (!node.isSeedNode()) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	@Override
	public Map<String, GenericConfiguration> getClusterComponents() {
		Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();
		components.put(Constant.Technology.CASSANDRA, getCassandra());
		return components;
	}

	/**
	 * @return the rackEnabled
	 */
	public boolean isRackEnabled() {
		return rackEnabled;
	}

	/**
	 * @param rackEnabled the rackEnabled to set
	 */
	public void setRackEnabled(boolean rackEnabled) {
		this.rackEnabled = rackEnabled;
	}

	/**
	 * @return the rackFileName
	 */
	public String getRackFileName() {
		return rackFileName;
	}

	/**
	 * @param rackFileName the rackFileName to set
	 */
	public void setRackFileName(String rackFileName) {
		this.rackFileName = rackFileName;
	}
}
