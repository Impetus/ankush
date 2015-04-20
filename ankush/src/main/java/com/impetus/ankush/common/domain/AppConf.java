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

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class AppConf.
 * 
 * @author bgunjan
 */
@Entity
@Table(name = "appconf")
public class AppConf extends BaseObject {

	/** The logger. */
	static private AnkushLogger logger = new AnkushLogger(AppConf.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The conf key. */
	private String confKey;

	/** The conf value. */
	private String confValue;

	/**
	 * Gets the conf key.
	 * 
	 * @return the confKey
	 */
	@Id
	public String getConfKey() {
		return confKey;
	}

	/**
	 * Sets the conf key.
	 * 
	 * @param confKey
	 *            the confKey to set
	 */
	public void setConfKey(String confKey) {
		this.confKey = confKey;
	}

	/**
	 * Gets the conf value.
	 * 
	 * @return the confValue
	 */
	@Lob
	public String getConfValue() {
		return confValue;
	}

	/**
	 * Sets the conf value.
	 * 
	 * @param confValue
	 *            the confValue to set
	 */
	public void setConfValue(String confValue) {
		this.confValue = confValue;
	}

	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	@Transient
	@JsonIgnore
	public Object getObject() {
		Object object = null;
		ObjectMapper mapper = new ObjectMapper();
		String acv = this.confValue;
		try {
			object = mapper.readValue(acv,
					new TypeReference<Map<String, Object>>() {
					});
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return object;
	}

	/**
	 * Sets the object.
	 * 
	 * @param object
	 *            the object to set
	 */
	@Transient
	public void setObject(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String acv = null;
		try {
			acv = mapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		this.setConfValue(acv);
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
		result = prime * result + ((confKey == null) ? 0 : confKey.hashCode());
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
		if (!(obj instanceof AppConf)) {
			return false;
		}
		AppConf other = (AppConf) obj;
		if (confKey == null) {
			if (other.confKey != null) {
				return false;
			}
		} else if (!confKey.equals(other.confKey)) {
			return false;
		}
		return true;
	}
}
