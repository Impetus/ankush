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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.util.SerializationUtils;

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;

/**
 * Template domain class for storing template information with technology, user
 * and technology data in database.
 * 
 * @author hokam
 * 
 */
@Entity
@Table(name = "template")
public class Template extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String user;

	private String technology;

	private String name;

	private byte[] dataBytes;

	private Date updateDate;

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	/**
	 * @return the user
	 */
	@Column(name = "username")
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * @param technology
	 *            the technology to set
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dataBytes
	 */
	@JsonIgnore
	@Lob
	@Column(length = Integer.MAX_VALUE - 1)
	public byte[] getDataBytes() {
		return dataBytes;
	}

	/**
	 * @param dataBytes
	 *            the dataBytes to set
	 */
	public void setDataBytes(byte[] dataBytes) {
		this.dataBytes = dataBytes;
	}

	/**
	 * Method to get data as Linked Hash Map.
	 * 
	 * @return {@link ClusterConf}
	 */
	@Transient
	public ClusterConfig getData() {
		if (getDataBytes() == null) {
			return null;
		}
		try {
			return (ClusterConfig) SerializationUtils.deserialize(getDataBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to set the ClusterConf as bytes in db.
	 * 
	 */
	public void setData(ClusterConfig data) {
		if (data != null) {
			data.setState(Constant.Cluster.State.ERROR);
			setDataBytes(SerializationUtils.serialize(data));
		}
	}
	
	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
		if (!(obj instanceof Template)) {
			return false;
		}
		Template other = (Template) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((technology == null) ? 0 : technology.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

}
