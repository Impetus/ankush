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
package com.impetus.ankush2.cassandra.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush2.cassandra.monitor.CassandraNode;
import com.impetus.ankush2.cassandra.monitor.Datacenter;
import com.impetus.ankush2.cassandra.monitor.Rack;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;

public class CassandraUtils {

	public static boolean isSeedNode(Map<String, Set<String>> roles) {
		if (!roles.containsKey(Constant.Component.Name.CASSANDRA)) {
			return false;
		}
		return roles.get(Constant.Component.Name.CASSANDRA).contains(
				CassandraConstants.Node_Type.CASSANDRA_SEED);
	}

	public static String getNodeRole(Map<String, Set<String>> roles) {

		String role = CassandraConstants.Node_Type.CASSANDRA_NON_SEED;
		if (isSeedNode(roles)) {
			role = CassandraConstants.Node_Type.CASSANDRA_SEED;
		}
		return role;
	}

	public static Object getAdvanceConf(ComponentConfig compConf, String key) {
		return (Object) compConf.getAdvanceConf().get(key);
	}

	public static String getTruncatedPath(String path) {
		while (path.endsWith("/")) {
			path = path.substring(0, (path.length() - 1));
		}
		return path;
	}

	public static Set<String> getSeedNodeSet(ClusterConfig clusterConfig,
			ComponentConfig componentConfig) {
		Set<String> seedNodeSet = new HashSet<String>();
		Map<String, Set<String>> roles;
		for (String host : componentConfig.getNodes().keySet()) {
			roles = clusterConfig.getNodes().get(host).getRoles();
			if (roles.containsKey(Constant.Component.Name.CASSANDRA)
					&& roles.get(Constant.Component.Name.CASSANDRA).contains(
							CassandraConstants.Node_Type.CASSANDRA_SEED)) {
				seedNodeSet.add(host);
			}
		}
		return seedNodeSet;
	}

	public static Integer getUpNodeCount(List<Datacenter> dcList) {
		Integer upNodeCount = 0;
		for (Datacenter dc : dcList) {
			for (Rack rack : dc.getRacks()) {
				for (CassandraNode node : rack.getNodes()) {
					if (node.getStatus() != null
							&& node.getStatus().equals("UP")) {
						upNodeCount++;
					}

				}
			}
		}
		return upNodeCount;
	}

	public static Set<String> getCassandraActiveNodes(Set<String> nodes) {
		Set<String> activeNodes = new HashSet<String>();
		for (String host : nodes) {
			NodeMonitoring mNode = new MonitoringManager()
					.getMonitoringData(host);
			if (mNode != null) {
				// if service contains the role and it is up
				if (isActive(host)) {
					activeNodes.add(host);
				}
			}
		}
		return activeNodes;
	}

	public static boolean isActive(String host) {
		NodeMonitoring mNode = new MonitoringManager().getMonitoringData(host);
		if (mNode != null) {
			// Cassandra Technology services.
			Map<String, Boolean> serviceMap = mNode
					.getServiceStatus(Constant.Component.Name.CASSANDRA);
			if (serviceMap == null) {
				return false;
			}

			// if service contains the role and it is up
			if (serviceMap.containsKey(Constant.Role.CASSANDRA_NON_SEED)
					&& serviceMap.get(Constant.Role.CASSANDRA_NON_SEED)) {
				return true;
			}
			// if service contains the role and it is up
			if (serviceMap.containsKey(Constant.Role.CASSANDRA_SEED)
					&& serviceMap.get(Constant.Role.CASSANDRA_SEED)) {
				return true;
			}
		}
		return false;
	}

}
