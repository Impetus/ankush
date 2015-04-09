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
package com.impetus.ankush2.ganglia;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.NodeConfig;

public class GangliaUtils {

	public static boolean isGmetad(Map<String, Set<String>> roles) {
		if (!roles.containsKey(Constant.Component.Name.GANGLIA)) {
			return false;
		}
		return roles.get(Constant.Component.Name.GANGLIA).contains(
				Constant.Role.GMETAD);
	}

	public static String getAuthentication(ClusterConfig clusterConfig) {
		return clusterConfig.getAuthConf().isUsingPassword() ? clusterConfig
				.getAuthConf().getPassword() : clusterConfig.getAuthConf()
				.getPrivateKey();
	}

	public static Map<String, Set<String>> getNodeRoles(
			ClusterConfig clusterConfig, String host) throws AnkushException {
		if (!clusterConfig.getNodes().containsKey(host)) {
			throw new AnkushException(
					"Cluster nodes does not contain mentioned host: " + host);
		}
		return clusterConfig.getNodes().get(host).getRoles();
	}

	public static String getGangliaMaster(Collection<NodeConfig> nodes)
			throws AnkushException {
		for (NodeConfig node : nodes) {
			if (!node.getRoles().containsKey(Constant.Component.Name.GANGLIA)) {
				throw new AnkushException("Could not get node role for "
						+ Constant.Component.Name.GANGLIA + "for "
						+ node.getHost());
			}
			if (isGmetad(node.getRoles())) {
				return node.getHost();
			}
		}
		throw new AnkushException("Could not get Ganglia Master node.");
	}
}
