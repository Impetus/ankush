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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Index;

/**
 * It is used for storing the events of the cluster.
 * 
 * @author hokam
 * 
 */
@Entity
@Table(name = "event")
public class Event extends BaseObject {

	public enum Severity {
		NORMAL, WARNING, CRITICAL
	}

	public enum Type {
		SERVICE, USAGE
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Long id;

	/** The cluster id. */
	private Long clusterId;

	/** The host. */
	private String host;

	/** The type. */
	private Type type;

	/** The severity. */
	private Severity severity;

	private String category;

	private String thresholdValue;

	/** The name. */
	private String name;

	/** The current value. */
	private String value;

	/** The date. */
	private Date date;

	private List<EventHistory> history;

	/**
	 * Gets the event histories.
	 * 
	 * @return the tiles
	 */
//	@OneToMany(mappedBy = "eventId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "eventId", cascade = CascadeType.ALL)
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
	@Index(name = "event_cluster_index")
	public Long getClusterId() {
		return clusterId;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Index(name = "event_name_index")
	public String getName() {
		return name;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	@Index(name = "event_type_index")
	public Type getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Gets the severity.
	 * 
	 * @return the severity
	 */
	@Index(name = "event_severity_index")
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * Sets the severity.
	 * 
	 * @param severity
	 *            the severity to set
	 */
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	/**
	 * Gets the host.
	 * 
	 * @return the host
	 */
	@Index(name = "event_host_index")
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
	 * @return the category
	 */
	@Index(name = "event_category_index")
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the thresholdValue
	 */
	public String getThresholdValue() {
		return thresholdValue;
	}

	/**
	 * @param thresholdValue
	 *            the thresholdValue to set
	 */
	public void setThresholdValue(String thresholdValue) {
		this.thresholdValue = thresholdValue;
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
		if (this.severity.equals(Severity.NORMAL)) {
			sub.append(this.name).append("(").append(this.category)
					.append(") At ").append(this.host)
					.append(" is back to normal");
		} else {
			sub.append("Ankush ").append(this.severity).append(" : ");
			sub.append(this.host).append(" " + this.name).append("(")
					.append(this.category).append(")");
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
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((clusterId == null) ? 0 : clusterId.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((severity == null) ? 0 : severity.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		if (!(obj instanceof Event)) {
			return false;
		}
		Event other = (Event) obj;
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		if (clusterId == null) {
			if (other.clusterId != null) {
				return false;
			}
		} else if (!clusterId.equals(other.clusterId)) {
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
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event [id=" + id + ", clusterId=" + clusterId + ", host="
				+ host + ", type=" + type + ", severity=" + severity
				+ ", category=" + category + ", thresholdValue="
				+ thresholdValue + ", name=" + name + ", value=" + value
				+ ", date=" + date + ", history=" + history + "]";
	}
	
}
