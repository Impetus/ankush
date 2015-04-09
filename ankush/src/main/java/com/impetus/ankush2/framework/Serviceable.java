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
package com.impetus.ankush2.framework;

import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush2.framework.config.ClusterConfig;

public interface Serviceable {

	String getComponentName();

	void setComponentName(String componentName);

	/**
	 * Used to start services on a node . Used during startServices call.
	 * 
	 * @param clusterConfig
	 *            {@link ClusterConfig}
	 * @param host
	 *            (connection object to a node) {@link String}
	 * @param services
	 *            set of services to start {@link Set}
	 */
	boolean startServices(ClusterConfig clusterConfig, String host,
			Set<String> services);

	/**
	 * Used to stop services on a node . Used during stopServices call.
	 * 
	 * @param clusterConfig
	 *            {@link ClusterConfig}
	 * @param connection
	 *            (connection object to a node) {@link SSHExec}
	 * @param services
	 *            set of services to start {@link Set}
	 */
	boolean stopServices(ClusterConfig clusterConfig, String host,
			Set<String> services);

	/**
	 * Used to start services on a node . Used during startNode call.
	 * 
	 * @param clusterConfig
	 *            {@link ClusterConfig}
	 * @param connection
	 *            (connection object to a node) {@link SSHExec}
	 * @param services
	 *            set of services to start {@link Set}
	 */
	boolean startNode(ClusterConfig clusterConfig, String host);

	/**
	 * Used to stop services on a node . Used during stopNode call.
	 * 
	 * @param clusterConfig
	 *            {@link ClusterConfig}
	 * @param connection
	 *            (connection object to a node) {@link SSHExec}
	 * @param services
	 *            set of services to start {@link Set}
	 */
	boolean stopNode(ClusterConfig clusterConfig, String host);

	/**
	 * Used during start component as well as start Cluster call.
	 * 
	 * @param clusterConfig
	 *            {@link ClusterConfig}
	 */
	boolean start(ClusterConfig clusterConfig);

	/**
	 * Used during stop component as well as stop Cluster call.
	 * 
	 * @param clusterConfig
	 *            {@link ClusterConfig}
	 */
	boolean stop(ClusterConfig clusterConfig);

	/**
	 * 
	 * @param clusterConfig
	 * @param role
	 * @return
	 */
	boolean startRole(ClusterConfig clusterConfig, String role);

	/**
	 * 
	 * @param clusterConfig
	 * @param role
	 * @return
	 */
	boolean stopRole(ClusterConfig clusterConfig, String role);

	Set<String> getServiceList(ClusterConfig clusterConfig);
	
	String getLogDirPath(ClusterConfig clusterConfig, String host, String role);
	
	String getLogFilesRegex(ClusterConfig clusterConfig, String host, String role, Map<String, Object> parameters);
}
