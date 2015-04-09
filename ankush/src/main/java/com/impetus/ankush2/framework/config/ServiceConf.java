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
package com.impetus.ankush2.framework.config;

import java.util.Map;
import java.util.Set;

import com.impetus.ankush2.constant.Constant;

public class ServiceConf {
	private String host;
	private Map<String, Set<String>> services;
	private Constant.ServiceAction action;

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(Map<String, Set<String>> services) {
		this.services = services;
	}

	/**
	 * @return the services
	 */
	public Map<String, Set<String>> getServices() {
		return services;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(Constant.ServiceAction action) {
		this.action = action;
	}

	/**
	 * @return the action
	 */
	public Constant.ServiceAction getAction() {
		return action;
	}
}
