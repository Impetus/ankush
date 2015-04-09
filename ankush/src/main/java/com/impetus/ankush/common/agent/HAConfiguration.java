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
import javax.xml.bind.annotation.XmlRootElement;

import com.impetus.ankush.common.domain.HAService;

/**
 * @author hokam
 * 
 */
@XmlRootElement(name = "services")
@XmlAccessorType(XmlAccessType.FIELD)
public class HAConfiguration {

	@XmlElement(name = "service")
	private List<HAService> services;

	/**
	 * 
	 */
	public HAConfiguration() {
		super();
	}

	/**
	 * @param services
	 */
	public HAConfiguration(List<HAService> services) {
		super();
		this.services = services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<HAService> services) {
		this.services = services;
	}

	/**
	 * @return the services
	 */
	public List<HAService> getServices() {
		return services;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HAConfiguration [services=" + services + "]";
	}
}
