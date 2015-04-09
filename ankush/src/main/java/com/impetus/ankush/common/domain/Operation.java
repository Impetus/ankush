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

import java.util.Date;
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

/**
 * The Class Operation.
 */
@Entity
@Table(name = "operation")
public class Operation extends BaseObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The name. */
	private String opName;
	private String startedBy;

	/** The started at. */
	private Date startedAt;

	/** The completed at. */
	private Date completedAt;

	/** The state. */
	private String status;

	/** The cluster id. */
	private Long clusterId;

	private Long operationId;

	private byte[] dataBytes;

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	 * Gets the op name.
	 * 
	 * @return the opName
	 */
	public String getOpName() {
		return opName;
	}

	/**
	 * Sets the op name.
	 * 
	 * @param opName
	 *            the opName to set
	 */
	public void setOpName(String opName) {
		this.opName = opName;
	}

	/**
	 * Gets the started at.
	 * 
	 * @return the startedAt
	 */
	public Date getStartedAt() {
		return startedAt;
	}

	/**
	 * Sets the started at.
	 * 
	 * @param startedAt
	 *            the startedAt to set
	 */
	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	/**
	 * Gets the completed at.
	 * 
	 * @return the completedAt
	 */
	public Date getCompletedAt() {
		return completedAt;
	}

	/**
	 * Sets the completed at.
	 * 
	 * @param completedAt
	 *            the completedAt to set
	 */
	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the operationId
	 */
	public Long getOperationId() {
		return operationId;
	}

	/**
	 * @param operationId
	 *            the operationId to set
	 */
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	/**
	 * @return the startedBy
	 */
	public String getStartedBy() {
		return startedBy;
	}

	/**
	 * @param startedBy
	 *            the startedBy to set
	 */
	public void setStartedBy(String startedBy) {
		this.startedBy = startedBy;
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

	/**
	 * Gets the cluster conf.
	 * 
	 * @return the cluster conf
	 */
	@Transient
	public HashMap<String, Object> getData() {
		if (getDataBytes() == null) {
			return null;
		}
		return (HashMap<String, Object>) SerializationUtils
				.deserialize(getDataBytes());
	}

	@Transient
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
				+ ((completedAt == null) ? 0 : completedAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((opName == null) ? 0 : opName.hashCode());
		result = prime * result
				+ ((startedAt == null) ? 0 : startedAt.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		Operation other = (Operation) obj;
		if (clusterId == null) {
			if (other.clusterId != null)
				return false;
		} else if (!clusterId.equals(other.clusterId))
			return false;
		if (completedAt == null) {
			if (other.completedAt != null)
				return false;
		} else if (!completedAt.equals(other.completedAt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (opName == null) {
			if (other.opName != null)
				return false;
		} else if (!opName.equals(other.opName))
			return false;
		if (startedAt == null) {
			if (other.startedAt != null)
				return false;
		} else if (!startedAt.equals(other.startedAt))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
}
