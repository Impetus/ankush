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
package com.impetus.ankush.kafka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.zookeeper.ZookeeperConf;

/**
 * The class KafkaClusterConf.
 * 
 * @author monika
 * 
 */
public class KafkaClusterConf extends ClusterConf {

	private static final long serialVersionUID = 1L;

	/** The isStatusError. */
	private boolean isStatusError;
	/** The kafka. */
	private KafkaConf kafka = new KafkaConf();

	/** The zookeeper. */
	private ZookeeperConf zookeeper = new ZookeeperConf();

	/** The newNodesList */
	private List<NodeConf> newNodes = new ArrayList<NodeConf>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	/**
	 * @return the kafka
	 */
	public KafkaConf getKafka() {
		return kafka;
	}

	/**
	 * @return the newNodes
	 */
	public List<NodeConf> getNewNodes() {
		return newNodes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.kafka.KafkaConf#getNodes()
	 */
	@Override
	public List<NodeConf> getNodeConfs() {
		return kafka.getNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.KafkaClusterConf#getNodeCount()
	 */
	@Override
	public int getNodeCount() {
		return getNodeConfs().size();
	}

	/**
	 * @return the zookeeper
	 */
	public ZookeeperConf getZookeeper() {
		return zookeeper;
	}

	/**
	 * @return the isStatusError
	 */
	public boolean isStatusError() {
		return getState().equals(Constant.Cluster.State.ERROR);
	}

	/**
	 * @param kafka
	 *            the kafka to set
	 */
	public void setKafka(KafkaConf kafka) {
		this.kafka = kafka;
	}

	/**
	 * @param newNodes
	 *            the newNodes to set
	 */
	public void setNewNodes(List<NodeConf> newNodes) {
		this.newNodes = newNodes;
	}

	/**
	 * @param isStatusError
	 *            the isStatusError to set
	 */
	public void setStatusError(boolean isStatusError) {
		this.isStatusError = isStatusError;
	}

	/**
	 * @param zookeeper
	 *            the zookeeper to set
	 */
	public void setZookeeper(ZookeeperConf zookeeper) {
		this.zookeeper = zookeeper;
	}

	@Override
	public Map<String, GenericConfiguration> getClusterComponents() {
		return getComponents();
	}

	/**
	 * Gets the components.
	 * 
	 * @return the components
	 */
	@JsonIgnore
	public Map<String, GenericConfiguration> getComponents() {
		final Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();
		final ArrayList<NodeConf> nodeConfs = new ArrayList<NodeConf>();
		for (final NodeConf nodeConf : getNodeConfs()) {
			if (zookeeper.getNodes().contains(nodeConf)) {
				nodeConfs.add(nodeConf);
			}
		}
		zookeeper.setNodes(nodeConfs);
		components.put(Constant.Component.Name.ZOOKEEPER, zookeeper);
		components.put(Constant.Component.Name.KAFKA, kafka);
		return components;
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
		final KafkaClusterConf other = (KafkaClusterConf) obj;
		if (kafka == null) {
			if (other.kafka != null) {
				return false;
			}
		} else if (!kafka.equals(other.kafka)) {
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
	public String toString() {
		return "KafkaClusterConf[kafka=" + kafka + ", zookeeper=" + zookeeper
				+ ", javaConf=" + javaConf + ", isStatusError=" + isStatusError
				+ ", errors=" + errors + ", clusterName=" + clusterName
				+ ", environment=" + environment + ", gangliaMaster="
				+ gangliaMaster + ", ipPattern=" + ipPattern + ", patternFile="
				+ patternFile + ", newNodes= " + newNodes + ", getNewNodes()="
				+ getNewNodes() + ", getErrors()=" + getErrors()
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
		result = (prime * result) + ((kafka == null) ? 0 : kafka.hashCode());
		result = (prime * result)
				+ ((zookeeper == null) ? 0 : zookeeper.hashCode());
		return result;
	}

}
