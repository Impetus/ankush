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
package com.impetus.ankush2.hadoop.utils;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The Class Parameter.
 * 
 * @author Hokam Chauhan
 */
public class Parameter {

	/** The name. */
	private String name;

	/** The value. */
	private String value;

	/** The default value. */
	private String defaultValue;

	/** The description. */
	private String description;

	/** The is final. */
	private Boolean isFinal;

	/** The status. */
	private String status = "none";

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Parameter [name=" + name + ", value=" + value
				+ ", defaultValue=" + defaultValue + ", description="
				+ description + ", isFinal=" + isFinal + ", status=" + status
				+ "]";
	}

	/**
	 * Instantiates a new parameter.
	 */
	public Parameter() {

	}

	/**
	 * Instantiates a new parameter.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @param description
	 *            the description
	 * @param isFinal
	 *            the is final
	 */
	public Parameter(String name, String value, String defaultValue,
			String description, Boolean isFinal) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.defaultValue = defaultValue;
		this.isFinal = isFinal;
	}

	/**
	 * Instantiates a new parameter.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public Parameter(String name, Object value) {
		this.name = name;
		this.value = value == null ? null : value.toString();
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
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the default value.
	 * 
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the default value.
	 * 
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
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
	 * Checks if is final.
	 * 
	 * @return the isFinal
	 */
	@JsonIgnore
	public Boolean isFinal() {
		return isFinal;
	}

	/**
	 * Sets the final.
	 * 
	 * @param isFinal
	 *            the isFinal to set
	 */
	public void setFinal(Boolean isFinal) {
		this.isFinal = isFinal;
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
		Parameter other = (Parameter) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
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

}
