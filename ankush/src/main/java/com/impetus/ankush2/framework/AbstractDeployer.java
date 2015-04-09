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

public abstract class AbstractDeployer implements Deployable {
	protected String componentName;

	public boolean validateNodes(ClusterConfig conf, ClusterConfig newConf) {
		return true;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public boolean start(ClusterConfig conf, Collection<String> nodes) {
		return true;
	}

	public boolean stop(ClusterConfig conf, Collection<String> nodes) {
		return true;
	}

	public String getComponentName() {
		return componentName;
	}

	public boolean createConfig(ClusterConfig conf) {
		return true;
	}

	public boolean validate(ClusterConfig conf) {
		return true;
	}

	public boolean deploy(ClusterConfig conf) {
		return true;
	}

	public boolean register(ClusterConfig conf) {
		return true;
	}

	public boolean undeploy(ClusterConfig conf) {
		return true;
	}

	public boolean unregister(ClusterConfig conf) {
		return true;
	}

	public boolean start(ClusterConfig conf) {
		return true;
	}

	public boolean stop(ClusterConfig conf) {
		return true;
	}

	public boolean addNode(ClusterConfig conf, ClusterConfig newConf) {
		return true;
	}

	public boolean removeNode(ClusterConfig conf, Collection<String> nodes) {
		return true;
	}

	public boolean removeNode(ClusterConfig conf, ClusterConfig newConf) {
		return true;
	}

	public boolean canNodeBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes) {
		return true;
	}

	public Set<String> getNodesForDependenciesDeployment(
			ClusterConfig clusterConfig) {
		try {
			if (clusterConfig.getComponents().containsKey(getComponentName())
					&& clusterConfig.getComponents().get(getComponentName())
							.getNodes() != null) {
				return clusterConfig.getComponents().get(getComponentName())
						.getNodes().keySet();
			}
		} catch (Exception e) {

		}
		return null;
	}

	// public boolean validateClusterJsonForNodes(ClusterConfig clusterConfig) {
	//
	// for (String component : clusterConfig.getComponents().keySet()) {
	// try {
	// if (component.equals(Constant.Component.Name.GANGLIA)
	// || component.equals(Constant.Component.Name.HADOOP)
	// || component.equals(Constant.Component.Name.AGENT)) {
	// continue;
	// }
	// if (clusterConfig.getComponents().get(component).getNodes() != null
	// && clusterConfig.getComponents().get(component)
	// .getNodes().size() > 0) {
	// for (String host : clusterConfig.getComponents()
	// .get(component).getNodes().keySet()) {
	// System.out.println("component : " + component);
	// if (!(clusterConfig.getNodes().get(host).getRoles()
	// .keySet().contains(component) && clusterConfig
	// .getNodes().get(host).getRoles().get(component)
	// .size() > 0)) {
	// clusterConfig.addError(component,
	// "Invalid node mapping : Node- " + host
	// + " for Component- " + component);
	// }
	// }
	//
	// } else {
	// clusterConfig.addError(component,
	// "No nodes given for the componnet- "
	// + componentName);
	// }
	//
	// } catch (Exception e) {
	// clusterConfig
	// .addError(
	// component,
	// "Exception in validating cluster Json.Please send proper Json to create cluster.");
	// }
	// }
	// return !(clusterConfig.getErrors().size() > 0);
	// }
}
