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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.SerializationUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.framework.config.MonitoringInfo;

/**
 * The Class NodeMonitoring.
 * 
 * @author hokam
 */
@Entity
@Table(name = "nodemonitoring")
@NamedQueries({
		@NamedQuery(name = "getClusterMonitoring", query = "SELECT n FROM NodeMonitoring n,Node nn, Cluster nc WHERE nc.id=:clusterId AND nc.id=nn.clusterId and nn.id=n.nodeId"),
		@NamedQuery(name = "getNodeMonitoring", query = "SELECT n FROM NodeMonitoring n,Node nn WHERE nn.publicIp=:publicIp AND nn.id=n.nodeId") })
public class NodeMonitoring extends BaseObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The node id. */
	private Long nodeId;

	/** The monitoring info bytes. */
	private byte[] monitoringInfoBytes;

	/** The service status bytes. */
	private byte[] serviceStatusBytes;

	/** The technology data bytes. */
	private byte[] technologyDataBytes;

	/** The update time. */
	private Date updateTime;

	/** Graph View data **/
	private byte[] graphView;

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
	 * Gets the node id.
	 * 
	 * @return the nodeId
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the node id.
	 * 
	 * @param nodeId
	 *            the nodeId to set
	 */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Sets the monitoring info bytes.
	 * 
	 * @param monitoringInfoBytes
	 *            the monitoringInfoBytes to set
	 */
	private void setMonitoringInfoBytes(byte[] monitoringInfoBytes) {
		this.monitoringInfoBytes = monitoringInfoBytes;
	}

	/**
	 * Gets the monitoring info bytes.
	 * 
	 * @return the monitoringInfoBytes
	 */
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	private byte[] getMonitoringInfoBytes() {
		return monitoringInfoBytes;
	}

	/**
	 * Gets the monitoring info.
	 * 
	 * @return the monitoringInfo
	 */
	@Transient
	public MonitoringInfo getMonitoringInfo() {
		if (getMonitoringInfoBytes() == null) {
			return null;
		}
		return (MonitoringInfo) SerializationUtils
				.deserialize(getMonitoringInfoBytes());
	}

	/**
	 * Sets the monitoring info.
	 * 
	 * @param monitoringInfo
	 *            the monitoringInfo to set
	 */
	public void setMonitoringInfo(MonitoringInfo monitoringInfo) {
		setMonitoringInfoBytes(SerializationUtils.serialize(monitoringInfo));
	}

	/**
	 * Sets the service status bytes.
	 * 
	 * @param serviceStatusBytes
	 *            the serviceStatusBytes to set
	 */
	private void setServiceStatusBytes(byte[] serviceStatusBytes) {
		this.serviceStatusBytes = serviceStatusBytes;
	}

	/**
	 * Gets the service status bytes.
	 * 
	 * @return the serviceStatusBytes
	 */
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	private byte[] getServiceStatusBytes() {
		return serviceStatusBytes;
	}

	/**
	 * Gets the service status.
	 * 
	 * @return the serviceStatus
	 */
	@Transient
	public Map<String, Boolean> getServiceStatus() {
		if (getServiceStatusBytes() == null) {
			return null;
		}
		if (isAgentDown()) {
			Map<String, Boolean> serviceMap = new HashMap<String, Boolean>();
			serviceMap.put("Agent", false);
			return serviceMap;
		}
		Map<String, Boolean> serviceMap = (HashMap<String, Boolean>) SerializationUtils
				.deserialize(getServiceStatusBytes());
		serviceMap.put("Agent", true);
		return serviceMap;
	}

	/**
	 * Sets the service status.
	 * 
	 * @param serviceStatus
	 *            the serviceStatus to set
	 */
	public void setServiceStatus(HashMap<String, Boolean> serviceStatus) {
		setServiceStatusBytes(SerializationUtils.serialize(serviceStatus));
	}

	/**
	 * Sets the technology data bytes.
	 * 
	 * @param technologyDataBytes
	 *            the technologyDataBytes to set
	 */
	private void setTechnologyDataBytes(byte[] technologyDataBytes) {
		this.technologyDataBytes = technologyDataBytes;
	}

	/**
	 * Gets the technology data bytes.
	 * 
	 * @return the technologyDataBytes
	 */
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	private byte[] getTechnologyDataBytes() {
		return technologyDataBytes;
	}

	/**
	 * Sets the technology data.
	 * 
	 * @param technologyData
	 *            the technologyData to set
	 */
	public void setTechnologyData(
			HashMap<String, TechnologyData> technologiesData) {
		setTechnologyDataBytes(SerializationUtils.serialize(technologiesData));
	}

	/**
	 * Gets the technology data.
	 * 
	 * @return the technologyData
	 */
	@Transient
	public Map<String, TechnologyData> getTechnologyData() {
		if (getTechnologyDataBytes() == null) {
			return null;
		}
		return (Map<String, TechnologyData>) SerializationUtils
				.deserialize(getTechnologyDataBytes());
	}

	/**
	 * Gets the update time.
	 * 
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * Sets the update time.
	 * 
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @param graphView
	 *            the graphView to set
	 */
	public void setGraphView(byte[] graphView) {
		this.graphView = graphView;
	}

	/**
	 * @return the graphView
	 */
	@JsonIgnore
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	public byte[] getGraphView() {
		return graphView;
	}

	/**
	 * Method to set graph view data.
	 * 
	 * @param graphViewData
	 */
	public void setGraphViewData(HashMap graphViewData) {
		// if graphViewData is not null.
		if (graphViewData != null) {
			this.graphView = SerializationUtils.serialize(graphViewData);
		}
	}

	/**
	 * Method to sset graph view data.
	 * 
	 * @return
	 */
	@Transient
	public HashMap getGraphViewData() {
		// if graphViewData is not null.
		if (this.graphView != null) {
			try {
				return (HashMap) SerializationUtils.deserialize(this
						.getGraphView());
			} catch (Exception e) {
				return new HashMap();
			}
		}
		return new HashMap();
	}

	/**
	 * Method to get agent status.
	 * 
	 * @return true, if is agent down
	 */
	@Transient
	public boolean isAgentDown() {
		Date currentTime = new Date();
		if (currentTime.getTime() - this.updateTime.getTime() > 100000) {
			return true;
		}
		return false;
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
		result = prime * result + Arrays.hashCode(monitoringInfoBytes);
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + Arrays.hashCode(serviceStatusBytes);
		result = prime * result + Arrays.hashCode(technologyDataBytes);
		result = prime * result
				+ ((updateTime == null) ? 0 : updateTime.hashCode());
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
		if (!(obj instanceof NodeMonitoring)) {
			return false;
		}
		NodeMonitoring other = (NodeMonitoring) obj;
		if (nodeId == null) {
			if (other.nodeId != null) {
				return false;
			}
		} else if (!nodeId.equals(other.nodeId)) {
			return false;
		}
		if (!Arrays.equals(monitoringInfoBytes, other.monitoringInfoBytes)) {
			return false;
		}
		if (!Arrays.equals(serviceStatusBytes, other.serviceStatusBytes)) {
			return false;
		}
		if (!Arrays.equals(technologyDataBytes, other.technologyDataBytes)) {
			return false;
		}
		if (updateTime == null) {
			if (other.updateTime != null) {
				return false;
			}
		} else if (!updateTime.equals(other.updateTime)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NodeMonitoring [id=" + id + ", nodeId=" + nodeId
				+ ", monitoringInfoBytes="
				+ Arrays.toString(monitoringInfoBytes)
				+ ", serviceStatusBytes=" + Arrays.toString(serviceStatusBytes)
				+ ", technologyDataBytes="
				+ Arrays.toString(technologyDataBytes) + ", updateTime="
				+ updateTime + ", getId()=" + getId() + ", getNodeId()="
				+ getNodeId() + ", getMonitoringInfoBytes()="
				+ Arrays.toString(getMonitoringInfoBytes())
				+ ", getMonitoringInfo()=" + getMonitoringInfo()
				+ ", getServiceStatusBytes()="
				+ Arrays.toString(getServiceStatusBytes())
				+ ", getServiceStatus()=" + getServiceStatus()
				+ ", getTechnologyDataBytes()="
				+ Arrays.toString(getTechnologyDataBytes())
				+ ", getTechnologyData()=" + getTechnologyData()
				+ ", getUpdateTime()=" + getUpdateTime() + ", hashCode()="
				+ hashCode() + "]";
	}
}
