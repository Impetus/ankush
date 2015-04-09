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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * The Class Log.
 * 
 * @author nikunj
 */
@Entity
@Table(name = "log")
		@NamedQuery(name = "getLastOperationId", query = "select l from Log l where l.id=(select max(id) from Log where clusterId=:clusterId)")

public class Log extends BaseObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The date format. */
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss z");

	/** The id. */
	private Long id;

	/** The cluster id. */
	private Long clusterId;

	/** The operation id. */
	private Long operationId;

	/** The host. */
	private String host;

	private String componentName;

	/** The type. */
	private String type;

	/** The message. */
	private String message;

	/** The long message. */
	private String longMessage;

	/** The created at. */
	private Date createdAt;

	/**
	 * Gets the node last log query.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param operationID
	 *            the operation id
	 * @return the node last log query
	 */
	public static String getNodeLastLogQuery(long clusterId, long operationID) {
		StringBuilder query = new StringBuilder();
		query.append("select * from log l ")
				.append("inner join( ")
				.append("select host, max(id) id from log where clusterid=")
				.append(clusterId)
				.append(" and operationid=")
				.append(operationID)
				.append(" and host is not null group by host  ) ss on l.id = ss.id");
		return query.toString();
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
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
	@JsonIgnore
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
	 * Gets the operation id.
	 * 
	 * @return the operationId
	 */
	@JsonIgnore
	public Long getOperationId() {
		return operationId;
	}

	/**
	 * Sets the operation id.
	 * 
	 * @param operationId
	 *            the operationId to set
	 */
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	@JsonIgnore
	@Index(name = "log_host_index")
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host.
	 * 
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	@Lob
	@JsonIgnore
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the created at.
	 * 
	 * @return the createdAt
	 */
	@JsonIgnore
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
	 * Gets the long message.
	 * 
	 * @return the longMessage
	 */
	@Transient
	public String getLongMessage() {
		if (this.getHost() == null) {
			longMessage = dateFormat.format(this.getCreatedAt()) + ":"
					+ this.getMessage();
		} else {
			longMessage = dateFormat.format(this.getCreatedAt()) + " ["
					+ this.getHost() + "] :" + this.getMessage();
		}
		return longMessage;
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
				+ ((componentName == null) ? 0 : componentName.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Log other = (Log) obj;
		if (clusterId == null) {
			if (other.clusterId != null)
				return false;
		} else if (!clusterId.equals(other.clusterId))
			return false;
		if (componentName == null) {
			if (other.componentName != null)
				return false;
		} else if (!componentName.equals(other.componentName))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Log [clusterId=" + clusterId + ", operationId=" + operationId
				+ ", host=" + host + ", componentName=" + componentName
				+ ", type=" + type + ", message=" + message + ", createdAt="
				+ createdAt + "]";
	}
}
