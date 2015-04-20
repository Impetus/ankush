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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.SerializationUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush2.framework.config.AlertsConf;
import com.impetus.ankush2.framework.config.ClusterConfig;

/**
 * The Class Cluster.
 * 
 * @author nikunj
 */
@Entity
@Table(name = "cluster")
public class Cluster extends BaseObject {

	/** The Constant CLUSTER_ID. */
	private static final String CLUSTER_ID = "clusterId";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The name. */
	private String name;

	/** The state. */
	private String state;

	/** The environment. */
	private String environment;

	/** The created at. */
	private Date createdAt;

	/** The an conf bytes. */
	private byte[] anConfBytes;

	/** The conf bytes. */
	private byte[] confBytes;

	/** The technology. */
	private String technology;

	/** The user. */
	private String user;

	/** The nodes. */
	private Set<Node> nodes;

	/** The logs. */
	private List<Log> logs;

	/** The events. */
	private List<Event> events;

	/** The configurations. */
	private List<Configuration> configurations;

	/** The operations. */
	private List<Operation> operations;

	/** The HAServices. */
	private List<HAService> haServices;

	/** The Services. */
	private List<Service> services;

	/** Agent version **/
	private String agentVersion;

	/**
	 * @return the haServices
	 */
	@OneToMany(mappedBy = CLUSTER_ID, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<Service> getServices() {
		return services;
	}

	/**
	 * @param haServices
	 *            the haServices to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * @return the haServices
	 */
	@OneToMany(mappedBy = CLUSTER_ID, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<HAService> getHaServices() {
		return haServices;
	}

	/**
	 * @param haServices
	 *            the haServices to set
	 */
	public void setHaServices(List<HAService> haServices) {
		this.haServices = haServices;
	}

	/**
	 * @return the operations
	 */
	@OneToMany(mappedBy = CLUSTER_ID, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<Operation> getOperations() {
		return operations;
	}

	/**
	 * @param operations
	 *            the operations to set
	 */
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	/**
	 * Gets the configurations.
	 * 
	 * @return the configurations
	 */
	@OneToMany(mappedBy = CLUSTER_ID, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<Configuration> getConfigurations() {
		return configurations;
	}

	/**
	 * Sets the configurations.
	 * 
	 * @param configurations
	 *            the configurations to set
	 */
	public void setConfigurations(List<Configuration> configurations) {
		this.configurations = configurations;
	}

	/**
	 * Gets the events.
	 * 
	 * @return the events
	 */
	@OneToMany(mappedBy = CLUSTER_ID, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * Sets the events.
	 * 
	 * @param events
	 *            the events to set
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	/**
	 * Gets the logs.
	 * 
	 * @return the logs
	 */
	@OneToMany(mappedBy = CLUSTER_ID, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<Log> getLogs() {
		return logs;
	}

	/**
	 * Sets the logs.
	 * 
	 * @param logs
	 *            the logs to set
	 */
	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	@OneToMany(mappedBy = CLUSTER_ID, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public Set<Node> getNodes() {
		return nodes;
	}

	/**
	 * Method to get sorted nodes by public ip address.
	 * 
	 * @return
	 */
	// @Transient
	// public List<Node> getSortedNodesByIp() {
	// // nodes list getting from nodes set.
	// List<Node> sortedNodes = new ArrayList<Node>(nodes);
	// // sorting nodes.
	// Collections.sort(sortedNodes, new Comparator<Node>() {
	// /**
	// * Comparing public ip.
	// *
	// * @param o1
	// * @param o2
	// * @return
	// */
	// @Override
	// public int compare(Node o1, Node o2) {
	// return CommonUtil.toNumeric(o1.getPublicIp()).compareTo(
	// CommonUtil.toNumeric(o2.getPublicIp()));
	// }
	// });
	// return sortedNodes;
	// }

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Sets the environment.
	 * 
	 * @param environment
	 *            the environment to set
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * Gets the environment.
	 * 
	 * @return the environment
	 */
	public String getEnvironment() {
		return environment;
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
	 * Sets the an conf bytes.
	 * 
	 * @param anConfBytes
	 *            the new an conf bytes
	 * @return the alertNotificationConf
	 */
	/**
	 * @param anConfBytes
	 *            the anConfBytes to set
	 */
	private void setAnConfBytes(byte[] anConfBytes) {
		this.anConfBytes = anConfBytes;
	}

	/**
	 * Gets the an conf bytes.
	 * 
	 * @return the anConfBytes
	 */
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	private byte[] getAnConfBytes() {
		return anConfBytes;
	}

	/**
	 * Gets the alerts conf.
	 * 
	 * @return the object
	 */
	@Transient
	@JsonIgnore
	public AlertsConf getAlertsConf() {
		if (getAnConfBytes() == null) {
			return null;
		}
		return (AlertsConf) SerializationUtils.deserialize(getAnConfBytes());
	}

	/**
	 * Sets the alert conf.
	 * 
	 * @param object
	 *            the object to set
	 */
	@Transient
	public void setAlertConf(AlertsConf object) {
		this.setAnConfBytes(SerializationUtils.serialize(object));
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
	 * Gets the technology.
	 * 
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 * 
	 * @param technology
	 *            the technology to set
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	@Column(name = "username")
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	@Transient
	public ClusterConfig getClusterConfig() {
		if (getConfBytes() == null) {
			return null;
		}
		return (ClusterConfig) SerializationUtils.deserialize(getConfBytes());
	}

	@Transient
	public void setClusterConf(ClusterConfig clusterConf) {
		setConfBytes(SerializationUtils.serialize(clusterConf));
	}

	/**
	 * @return the agentVersion
	 */
	public String getAgentVersion() {
		return agentVersion;
	}

	/**
	 * @param agentVersion
	 *            the agentVersion to set
	 */
	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
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
		if (!(obj instanceof Cluster)) {
			return false;
		}
		Cluster other = (Cluster) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (technology == null) {
			if (other.technology != null) {
				return false;
			}
		} else if (!technology.equals(other.technology)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((technology == null) ? 0 : technology.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Cluster [id=" + id + ", name=" + name + ", state=" + state
				+ ", environment=" + environment + ", createdAt=" + createdAt
				+ ", anConfBytes=" + Arrays.toString(anConfBytes)
				+ ", confBytes=" + Arrays.toString(confBytes) + ", technology="
				+ technology + ", user=" + user + ", nodes=" + nodes
				+ ", getNodes()=" + getNodes() + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getState()=" + getState()
				+ ", getEnvironment()=" + getEnvironment()
				+ ", getCreatedAt()=" + getCreatedAt() + ", getAnConfBytes()="
				+ Arrays.toString(getAnConfBytes()) + ", getAlertsConf()="
				+ getAlertsConf() + ", getConfBytes()="
				+ Arrays.toString(getConfBytes()) + ", getTechnology()="
				+ getTechnology() + ", getUser()=" + getUser()
				+ ", getClusterConfig()=" + getClusterConfig() + ", hashCode()="
				+ hashCode() + "]";
	}

}
