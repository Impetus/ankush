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
package com.impetus.ankush.common.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.SerializationUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

import com.impetus.ankush2.framework.config.NodeConfig;

/**
 * The Class Node.
 * 
 * @author nikunj
 */
@Entity
@Table(name = "node")
public class Node extends BaseObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The cluster id. */
	private Long clusterId;

	/** The public ip. */
	private String publicIp;

	/** The private ip. */
	private String privateIp;

	/** The conf bytes. */
	private byte[] confBytes;

	/** The rack info. */
	private String rackInfo;

	/** The created at. */
	private Date createdAt;

	/** The state. */
	private String state;

	/** The monitors. */
	private List<NodeMonitoring> monitors;

	/** The Configuration **/
	private List<Configuration> configuration;

	/** the events **/
	private List<Event> events;

	/** the ervicess **/
	private List<Service> services;

	/** Agent version **/
	private String agentVersion;

	/**
	 * @param events
	 *            the events to set
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	/**
	 * @return the events
	 */
	/**
	 * @return the configuration
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumns({
			@JoinColumn(name = "clusterId", referencedColumnName = "clusterId"),
			@JoinColumn(name = "host", referencedColumnName = "publicIp") })
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * @param events
	 *            the events to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * @return the events
	 */
	/**
	 * @return the configuration
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumns({ @JoinColumn(name = "node", referencedColumnName = "publicIp") })
	public List<Service> getServices() {
		return services;
	}

	/**
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration(List<Configuration> configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return the configuration
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumns({
			@JoinColumn(name = "clusterId", referencedColumnName = "clusterId"),
			@JoinColumn(name = "host", referencedColumnName = "publicIp") })
	public List<Configuration> getConfiguration() {
		return configuration;
	}

	/**
	 * Gets the monitors.
	 * 
	 * @return the monitors
	 */
	@OneToMany(mappedBy = "nodeId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<NodeMonitoring> getMonitors() {
		return monitors;
	}

	/**
	 * Sets the monitors.
	 * 
	 * @param monitors
	 *            the monitors to set
	 */
	public void setMonitors(List<NodeMonitoring> monitors) {
		this.monitors = monitors;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the cluster id.
	 * 
	 * @return the clusterId
	 */
	public Long getClusterId() {
		return clusterId;
	}

	/**
	 * Sets the cluster id.
	 * 
	 * @param clusterId
	 *            the clusterId to set
	 */
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	/**
	 * Gets the public ip.
	 * 
	 * @return the publicIp
	 */
	@Index(name = "node_publicip_index")
	public String getPublicIp() {
		return publicIp;
	}

	/**
	 * Sets the public ip.
	 * 
	 * @param publicIp
	 *            the publicIp to set
	 */
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	/**
	 * Gets the private ip.
	 * 
	 * @return the privateIp
	 */
	public String getPrivateIp() {
		return privateIp;
	}

	/**
	 * Sets the private ip.
	 * 
	 * @param privateIp
	 *            the privateIp to set
	 */
	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	/**
	 * Sets the conf bytes.
	 * 
	 * @param confBytes
	 *            the confBytes to set
	 */
	private void setConfBytes(byte[] confBytes) {
		this.confBytes = confBytes;
	}

	/**
	 * Gets the conf bytes.
	 * 
	 * @return the confBytes
	 */
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	private byte[] getConfBytes() {
		return confBytes;
	}

	/**
	 * Gets the rack info.
	 * 
	 * @return the rackInfo
	 */
	public String getRackInfo() {
		return rackInfo;
	}

	/**
	 * Sets the rack info.
	 * 
	 * @param rackInfo
	 *            the rackInfo to set
	 */
	public void setRackInfo(String rackInfo) {
		this.rackInfo = rackInfo;
	}

	/**
	 * Gets the created at.
	 * 
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the created at.
	 * 
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the node config obj.
	 * 
	 * @return the object
	 */
	@Transient
	@JsonIgnore
	public NodeConfig getNodeConfig() {
		if (getConfBytes() == null) {
			return null;
		}
		return (NodeConfig) SerializationUtils.deserialize(getConfBytes());
	}

	public void setNodeConfig(NodeConfig nodeConf) {
		this.setConfBytes(SerializationUtils.serialize(nodeConf));
	}

	/**
	 * Gets the state.
	 * 
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param agentVersion
	 *            the agentVersion to set
	 */
	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}

	/**
	 * @return the agentVersion
	 */
	public String getAgentVersion() {
		return agentVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Node [id=" + id + ", clusterId=" + clusterId + ", publicIp="
				+ publicIp + ", privateIp=" + privateIp + ", rackConf="
				+ rackInfo + ", createdAt=" + createdAt + ", state=" + state
				+ "]";
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
		result = prime * result
				+ ((clusterId == null) ? 0 : clusterId.hashCode());
		result = prime * result
				+ ((privateIp == null) ? 0 : privateIp.hashCode());
		result = prime * result
				+ ((publicIp == null) ? 0 : publicIp.hashCode());
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
		if (!(obj instanceof Node)) {
			return false;
		}
		Node other = (Node) obj;
		if (clusterId == null) {
			if (other.clusterId != null) {
				return false;
			}
		} else if (!clusterId.equals(other.clusterId)) {
			return false;
		}
		if (privateIp == null) {
			if (other.privateIp != null) {
				return false;
			}
		} else if (!privateIp.equals(other.privateIp)) {
			return false;
		}
		if (publicIp == null) {
			if (other.publicIp != null) {
				return false;
			}
		} else if (!publicIp.equals(other.publicIp)) {
			return false;
		}
		return true;
	}
}
