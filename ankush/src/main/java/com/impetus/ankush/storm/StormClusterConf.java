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
package com.impetus.ankush.storm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.util.StringUtils;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.zookeeper.ZookeeperConf;

/**
 * The Class StormClusterConf.
 * 
 * @author Hokam
 */
public class StormClusterConf extends ClusterConf {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The storm. */
	private StormConf storm = new StormConf();

	/** The zookeeper. */
	private ZookeeperConf zookeeper = new ZookeeperConf();

	/** New nodes **/
	private List<NodeConf> newNodes = new ArrayList<NodeConf>();

	/**
	 * Gets the storm.
	 * 
	 * @return the storm
	 */
	public StormConf getStorm() {
		return storm;
	}

	/**
	 * Sets the storm.
	 * 
	 * @param storm
	 *            the storm to set
	 */
	public void setStorm(StormConf storm) {
		this.storm = storm;
	}

	/**
	 * Gets the zookeeper.
	 * 
	 * @return the zookeeper
	 */
	public ZookeeperConf getZookeeper() {
		return zookeeper;
	}

	/**
	 * Sets the zookeeper.
	 * 
	 * @param zookeeper
	 *            the zookeeper to set
	 */
	public void setZookeeper(ZookeeperConf zookeeper) {
		this.zookeeper = zookeeper;
	}

	/**
	 * Gets the components.
	 * 
	 * @return the components
	 */
	@JsonIgnore
	public Map<String, GenericConfiguration> getComponents() {
		Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();
		ArrayList<NodeConf> nodeConfs = new ArrayList<NodeConf>();
		for (NodeConf nodeConf : getNodeConfs()) {
			if (zookeeper.getNodes().contains(nodeConf)) {
				nodeConfs.add(nodeConf);
			}
		}
		zookeeper.setNodes(nodeConfs);
		components.put(Constant.Component.Name.ZOOKEEPER, zookeeper);
		components.put(Constant.Component.Name.STORM, storm);
		return components;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.ClusterConf#getNodeCount()
	 */
	@Override
	public int getNodeCount() {
		return getNodeConfs().size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.ClusterConf#getNodeConfs()
	 */
	@Override
	public List<NodeConf> getNodeConfs() {
		List<NodeConf> nodeConfs = new ArrayList<NodeConf>();
		nodeConfs.add(storm.getNimbus());
		nodeConfs.addAll(storm.getSupervisors());
		nodeConfs.addAll(zookeeper.getNodes());

		// iterating over the nodes to finalize the type of node.
		for (NodeConf nodeConf : nodeConfs) {
			String type = nodeConf.getType();
			if (type != null) {
				// taking the node roles.
				Set<String> roles = new HashSet<String>(Arrays.asList(type
						.split("/")));

				// if the node is part of other component than taking the other
				// type too.
				for (NodeConf node : nodeConfs) {
					if (nodeConf.equals(node)) {
						roles.addAll(Arrays.asList(node.getType().split("/")));
					}
				}
				// setting the new type to node.
				nodeConf.setType(StringUtils.collectionToDelimitedString(roles,
						"/"));
			}
		}
		// returning the list of unique nodes.
		return new ArrayList<NodeConf>(new HashSet<NodeConf>(nodeConfs));
	}

	/**
	 * @param newNodes
	 *            the newNodes to set
	 */
	public void setNewNodes(List<NodeConf> newNodes) {
		this.newNodes = newNodes;
	}

	/**
	 * @return the newNodes
	 */
	public List<NodeConf> getNewNodes() {
		return newNodes;
	}

	@JsonIgnore
	public String getNodeType(NodeConf nodeConf) {
		String type = "";
		if (this.getStorm().getNimbus().equals(nodeConf)) {
			type = "Nimbus/";
		}
		if (this.getStorm().getSupervisors().contains(nodeConf)) {
			type += "Supervisor/";
		}
		if (this.getZookeeper().getNodes().contains(nodeConf)) {
			type += "Zookeeper/";
		}
		if (type.isEmpty()) {
			return "Supervisor";
		}
		return type.substring(0, type.length() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StormClusterConf [storm=" + storm + ", zookeeper=" + zookeeper
				+ ", errors=" + errors + ", clusterName=" + clusterName
				+ ", environment=" + environment + ", gangliaMaster="
				+ gangliaMaster + ", ipPattern=" + ipPattern + ", patternFile="
				+ patternFile + ", getErrors()=" + getErrors()
				+ ", getCurrentUser()=" + getCurrentUser()
				+ ", getClusterName()=" + getClusterName()
				+ ", getEnvironment()=" + getEnvironment() + ", isLocal()="
				+ isLocal() + ", getGangliaMaster()=" + getGangliaMaster()
				+ ", getIpPattern()=" + getIpPattern() + ", getPatternFile()="
				+ getPatternFile() + ", isAuthTypePassword()="
				+ isAuthTypePassword() + ", getUsername()=" + getUsername()
				+ ", getPassword()=" + getPassword() + ", getPrivateKey()="
				+ getPrivateKey() + ", getClusterId()=" + getClusterId()
				+ ", getOperationId()=" + getOperationId() + ", getState()="
				+ getState() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
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
		result = prime * result + ((storm == null) ? 0 : storm.hashCode());
		result = prime * result
				+ ((zookeeper == null) ? 0 : zookeeper.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StormClusterConf other = (StormClusterConf) obj;
		if (storm == null) {
			if (other.storm != null) {
				return false;
			}
		} else if (!storm.equals(other.storm)) {
			return false;
		}
		if (zookeeper == null) {
			if (other.zookeeper != null) {
				return false;
			}
		} else if (!zookeeper.equals(other.zookeeper)) {
			return false;
		}
		return true;
	}

	@Override
	public Map<String, GenericConfiguration> getClusterComponents() {
		return getComponents();
	}
}
