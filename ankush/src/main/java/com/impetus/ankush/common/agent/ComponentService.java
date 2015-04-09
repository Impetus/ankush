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
package com.impetus.ankush.common.agent;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author hokam
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ComponentService {
	@XmlElement(name = "name")
	private String name;

	@XmlElement(name = "type")
	private String type;

	@XmlElement(name = "role")
	private String role;

	@XmlElement(name = "param")
	private List<Parameter> params;

	/**
	 * @param name
	 * @param type
	 * @param role
	 * @param params
	 */
	public ComponentService(String name, String role, String type,
			List<Parameter> params) {
		super();
		this.name = name;
		this.type = type;
		this.role = role;
		this.params = params;
	}

	/**
	 * @param name
	 * @param type
	 * @param role
	 */
	public ComponentService(String name, String role, String type) {
		super();
		this.name = name;
		this.type = type;
		this.role = role;
	}

	/**
	 * 
	 */
	public ComponentService() {
		super();
	}

	/**
	 * @param name
	 * @param type
	 */
	public ComponentService(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	/**
	 * @param name
	 * @param type
	 * @param params
	 */
	public ComponentService(String name, String type, List<Parameter> params) {
		super();
		this.name = name;
		this.type = type;
		this.params = params;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the params
	 */
	public List<Parameter> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<Parameter> params) {
		this.params = params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComponentService [name=" + name + ", type=" + type + ", role="
				+ role + ", params=" + params + "]";
	}
}
