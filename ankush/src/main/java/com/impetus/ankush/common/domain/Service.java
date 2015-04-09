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
package com.impetus.ankush.common.domain;

import java.util.Arrays;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.SerializationUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

import com.impetus.ankush2.constant.Constant;

/**
 * @author nikunj
 * 
 */
@Entity
@Table(name = "service")
public class Service extends BaseObject {

	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;
	// The cluster id.
	private Long clusterId;
	// Node
	private String node;
	// Component name
	private String component;
	// service name
	private String service;
	// HA enable flag
	private Boolean ha;
	// Forcefully stop flag
	private Boolean stop;
	// Service status
	private Boolean status;
	// Service type
	private Constant.RegisterLevel registrationType;

	// Data
	private byte[] dataBytes;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the clusterId
	 */
	@JsonIgnore
	@Index(name = "service_cluster_index")
	public Long getClusterId() {
		return clusterId;
	}

	/**
	 * @param clusterId
	 *            the clusterId to set
	 */
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	/**
	 * @return the node
	 */
	@Index(name = "service_node_index")
	public String getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}

	/**
	 * @return the component
	 */
	@Index(name = "service_component_index")
	public String getComponent() {
		return component;
	}

	/**
	 * @param component
	 *            the component to set
	 */
	public void setComponent(String component) {
		this.component = component;
	}

	/**
	 * @return the service
	 */
	@Index(name = "service_service_index")
	public String getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the ha
	 */
	public Boolean getHa() {
		return ha;
	}

	/**
	 * @param ha
	 *            the ha to set
	 */
	public void setHa(Boolean ha) {
		this.ha = ha;
	}

	/**
	 * @return the stop
	 */
	public Boolean getStop() {
		return stop;
	}

	/**
	 * @param stop
	 *            the stop to set
	 */
	public void setStop(Boolean stop) {
		this.stop = stop;
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * Sets the conf bytes.
	 * 
	 * @param confBytes
	 *            the confBytes to set
	 */
	private void setDataBytes(byte[] dataBytes) {
		this.dataBytes = dataBytes;
	}

	/**
	 * Gets the conf bytes.
	 * 
	 * @return the confBytes
	 */
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	private byte[] getDataBytes() {
		return dataBytes;
	}

	@Transient
	public HashMap<String, Object> getData() {
		if (getDataBytes() == null) {
			return null;
		}
		return (HashMap<String, Object>) SerializationUtils
				.deserialize(getDataBytes());
	}

	public void setData(HashMap<String, Object> data) {
		setDataBytes(SerializationUtils.serialize(data));
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
				+ ((component == null) ? 0 : component.hashCode());
		result = prime * result + Arrays.hashCode(dataBytes);
		result = prime * result + (ha ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		result = prime * result + (stop ? 1231 : 1237);
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
		if (!(obj instanceof Service)) {
			return false;
		}
		Service other = (Service) obj;
		if (clusterId == null) {
			if (other.clusterId != null) {
				return false;
			}
		} else if (!clusterId.equals(other.clusterId)) {
			return false;
		}
		if (component == null) {
			if (other.component != null) {
				return false;
			}
		} else if (!component.equals(other.component)) {
			return false;
		}
		if (!Arrays.equals(dataBytes, other.dataBytes)) {
			return false;
		}
		if (ha != other.ha) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (node == null) {
			if (other.node != null) {
				return false;
			}
		} else if (!node.equals(other.node)) {
			return false;
		}
		if (service == null) {
			if (other.service != null) {
				return false;
			}
		} else if (!service.equals(other.service)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (stop != other.stop) {
			return false;
		}
		return true;
	}

	/**
	 * @param registrationType
	 *            the registrationType to set
	 */
	public void setRegistrationType(Constant.RegisterLevel registrationType) {
		this.registrationType = registrationType;
	}

	/**
	 * @return the registrationType
	 */
	public Constant.RegisterLevel getRegistrationType() {
		return registrationType;
	}

}
