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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

import com.impetus.ankush.common.constant.Constant;

/**
 * It is used for storing the events of the cluster.
 * 
 * @author hokam
 * 
 */
@Entity
@Table(name = "event")
public class Event extends BaseObject {

	/** The Constant CLUSTER_ID. */
	private static final String EVENT_ID = "eventId";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The cluster id. */
	private Long clusterId;

	/** The name. */
	private String name;

	/** The type. */
	private String type;

	/** The severity. */
	private String severity;

	/** The host. */
	private String host;

	/** The description. */
	private String description;

	/** The current value. */
	private String currentValue;

	/** The sub type. */
	private String subType;

	/** The grouping type. */
	private String groupingType;

	/** The date. */
	private Date date;

	private List<EventHistory> history;

	/**
	 * Gets the event histories.
	 * 
	 * @return the tiles
	 */
	@OneToMany(mappedBy = EVENT_ID, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnore
	public List<EventHistory> getHistory() {
		return history;
	}

	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(List<EventHistory> history) {
		this.history = history;
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
	 * Sets the cluster id.
	 * 
	 * @param clusterId
	 *            the clusterId to set
	 */
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
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
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Index(name="event_name_index")
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
		if (this.name != null) {
			if (this.name.startsWith("rg")) {
				setSubType("Rep Node");
			} else if (this.name.startsWith("sn")) {
				setSubType("Storage Node");
			} else {
				setSubType(name);
			}
			setGroupingType(getSubType());
		}
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
	 * Gets the severity.
	 * 
	 * @return the severity
	 */
	@Index(name="event_severity_index")
	public String getSeverity() {
		return severity;
	}

	/**
	 * Sets the severity.
	 * 
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	@Index(name="event_host_index")
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

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	@Lob
	public String getDescription() {
		return description;
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
	 * Gets the current value.
	 * 
	 * @return the currentValue
	 */
	public String getCurrentValue() {
		if (this.type != null && this.type.equals(Constant.Alerts.Type.USAGE)
				&& currentValue != null && !currentValue.contains("%")) {
			return currentValue + " %";
		}
		return currentValue;
	}

	/**
	 * Sets the current value.
	 * 
	 * @param currentValue
	 *            the currentValue to set
	 */
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * Sets the sub type.
	 * 
	 * @param subType
	 *            the subType to set
	 */
	public void setSubType(String subType) {
		if (subType != null) {
			this.subType = subType.replaceAll(" Service", "").replaceAll(
					"Usage", "");
		}
	}

	/**
	 * Gets the sub type.
	 * 
	 * @return the subType
	 */
	@Index(name="event_subtype_index")
	public String getSubType() {
		return subType;
	}

	/**
	 * @param groupingType
	 *            the groupingType to set
	 */
	public void setGroupingType(String groupingType) {
		if (groupingType != null) {
			this.groupingType = groupingType;
		}
	}

	/**
	 * @return the groupingType
	 */
	public String getGroupingType() {
		return groupingType;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the subject.
	 * 
	 * @return the subject
	 */
	@JsonIgnore
	@Transient
	public String getSubject() {
		StringBuilder sub = new StringBuilder();
		if (this.severity.equals(Constant.Alerts.Severity.NORMAL)) {
			sub.append(this.name).append(" At ").append(this.host);
			sub.append(" is back to normal");
		} else {
			sub.append("Ankush ").append(this.severity).append(" : ");
			sub.append(this.host).append(" " + this.name);
		}
		return sub.toString();
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
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((severity == null) ? 0 : severity.hashCode());
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Event other = (Event) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (host == null) {
			if (other.host != null) {
				return false;
			}
		} else if (!host.equals(other.host)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (severity == null) {
			if (other.severity != null) {
				return false;
			}
		} else if (!severity.equals(other.severity)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
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
		return "Event [id=" + id + ", name=" + name + ", type=" + type
				+ ", severity=" + severity + ", host=" + host
				+ ", description=" + description + ", date=" + date
				+ ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getType()=" + getType() + ", getSeverity()="
				+ getSeverity() + ", getHost()=" + getHost()
				+ ", getDescription()=" + getDescription() + ", getDate()="
				+ getDate() + ", hashCode()=" + hashCode() + "]";
	}
}
