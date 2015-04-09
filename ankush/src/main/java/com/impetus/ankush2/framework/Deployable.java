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

import java.util.Collection;
import java.util.Set;

import com.impetus.ankush2.framework.config.ClusterConfig;

public interface Deployable {

	String getComponentName();

	void setComponentName(String componentName);

	// Create configuration from registration details
	boolean createConfig(ClusterConfig conf);

	// Validate configuration
	boolean validate(ClusterConfig conf);

	// Validate new nodes configuration
	boolean validateNodes(ClusterConfig conf, ClusterConfig newConf);

	// Deploy cluster
	boolean deploy(ClusterConfig conf);

	// Register cluster
	boolean register(ClusterConfig conf);

	// Undeploy cluster
	boolean undeploy(ClusterConfig conf);

	// Only unregister
	boolean unregister(ClusterConfig conf);

	// Start cluster
	boolean start(ClusterConfig conf);

	// Stop cluster
	boolean stop(ClusterConfig conf);

	// Start selected nodes
	boolean start(ClusterConfig conf, Collection<String> nodes);

	// Stop selected nodes
	boolean stop(ClusterConfig conf, Collection<String> nodes);

	// Add node
	boolean addNode(ClusterConfig conf, ClusterConfig newConf);

	// Remove selected nodes
	boolean removeNode(ClusterConfig conf, Collection<String> nodes);

	// If error in add operation
	boolean removeNode(ClusterConfig conf, ClusterConfig newConf);

	boolean canNodeBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes);

	Set<String> getNodesForDependenciesDeployment(ClusterConfig clusterConfig);

	// public boolean validateClusterJsonForNodes(ClusterConfig conf);
}
