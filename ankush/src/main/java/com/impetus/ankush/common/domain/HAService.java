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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

/**
 * @author hokam
 * 
 */
@Entity
@Table(name = "haservice")
public class HAService extends BaseObject {

	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;
	// The cluster id.
	private Long clusterId;
	// Component name
	private String component;
	// service name
	private String service;
	// Delay Interval
	private Integer delayInterval;
	// Try count.
	private Integer tryCount;

	/**
	 * Empty Constructor
	 */
	public HAService() {
		super();
	}

	/**
	 * @param clusterId
	 * @param component
	 * @param service
	 * @param delayInterval
	 * @param tryCount
	 */
	public HAService(Long clusterId, String component, String service,
			Integer delayInterval, Integer tryCount) {
		super();
		this.clusterId = clusterId;
		this.component = component;
		this.service = service;
		this.delayInterval = delayInterval;
		this.tryCount = tryCount;
	}

	/**
	 * @param service
	 */
	public HAService(String component, String service) {
		super();
		this.component = component;
		this.service = service;
	}

	public HAService(String service) {
		super();
		this.service = service;
	}
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	 * @param clusterId
	 *            the clusterId to set
	 */
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	/**
	 * @return the clusterId
	 */
	@Index(name = "haservice_cluster_index")
	public Long getClusterId() {
		return clusterId;
	}

	/**
	 * @return the component
	 */
	@Index(name = "haservice_component_index")
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
	@Index(name = "haservice_service_index")
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
	 * @return the delayInterval
	 */
	public Integer getDelayInterval() {
		return delayInterval;
	}

	/**
	 * @param delayInterval
	 *            the delayInterval to set
	 */
	public void setDelayInterval(Integer delay) {
		this.delayInterval = delay;
	}

	/**
	 * @return the tryCount
	 */
	public Integer getTryCount() {
		return tryCount;
	}

	/**
	 * @param tryCount
	 *            the tryCount to set
	 */
	public void setTryCount(Integer tryCount) {
		this.tryCount = tryCount;
	}

	@Transient
	public boolean isEnabled() {
		return this.delayInterval != null && this.tryCount != null;
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
				+ ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((service == null) ? 0 : service.hashCode());
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
		if (!(obj instanceof HAService)) {
			return false;
		}
		HAService other = (HAService) obj;
		if (component == null) {
			if (other.component != null) {
				return false;
			}
		} else if (!component.equals(other.component)) {
			return false;
		}
		if (service == null) {
			if (other.service != null) {
				return false;
			}
		} else if (!service.equals(other.service)) {
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
		return "HAService [clusterId=" + clusterId + ", component=" + component
				+ ", service=" + service + ", delayInterval=" + delayInterval
				+ ", tryCount=" + tryCount + "]";
	}

}
