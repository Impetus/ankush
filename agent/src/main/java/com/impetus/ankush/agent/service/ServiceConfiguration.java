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
package com.impetus.ankush.agent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hokam
 * 
 */
@XmlRootElement(name = "services")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceConfiguration {

	// component name.
	private String componentName;

	@XmlElement(name = "service")
	private List<ComponentService> services;

	/**
	 * @param componentName
	 *            the componentName to set
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * @return the services
	 */
	public List<ComponentService> getServices() {
		return services;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<ComponentService> services) {
		this.services = services;
	}

	/**
	 * Method to get component by service type.
	 * 
	 * @param type
	 * @return
	 */
	public Map<String, List<ComponentService>> getTypeServices() {
		// Local service empty object.
		Map<String, List<ComponentService>> typeServices = new HashMap<String, List<ComponentService>>();
		// Iterating over the list of services.
		for (ComponentService service : this.services) {
			// if service type is same as given type then add it to local
			// services object.
			List<ComponentService> services = typeServices.get(service
					.getType());
			if (services == null) {
				services = new ArrayList<ComponentService>();
			}
			services.add(service);
			typeServices.put(service.getType(), services);
		}
		// return type matched services.
		return typeServices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceConfiguration [services=" + services + "]";
	}
}
