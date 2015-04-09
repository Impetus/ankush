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
package com.impetus.ankush2.zookeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.constant.Constant.ServiceAction;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;

public class ZookeeperUtils {

	public static Map<String, Object> getZookeeperNodesAndPort(
			ClusterConfig clusterConfig, String ensembleId)
			throws AnkushException {

		Map<String, Object> zookeeperNodesPortMap = new HashMap<String, Object>();
		ensembleId = "Zookeeper_" + ensembleId;
		try {
			ComponentConfig zkConf = clusterConfig.getComponents().get(
					ensembleId);
			List<String> zookeeperNodes = new ArrayList<String>();
			for (String host : zkConf.getNodes().keySet()) {
				zookeeperNodes.add(host);
			}
			zookeeperNodesPortMap.put(ZookeeperConstant.Keys.ZK_NODES,
					zookeeperNodes);
			zookeeperNodesPortMap.put(
					ZookeeperConstant.Keys.CLIENT_PORT,
					zkConf.getAdvanceConf().get(
							ZookeeperConstant.Keys.CLIENT_PORT));
			System.out.println("zookeeperNodesPortMap : "
					+ zookeeperNodesPortMap);
		} catch (Exception e) {
			throw new AnkushException(
					"Unable to get Zookeeper component from the Cluster for ensembleId :"
							+ ensembleId);
		}
		return zookeeperNodesPortMap;
	}

	public static String getZookeeperConnectionString(
			ClusterConfig clusterConfig, String ensembleId)
			throws AnkushException {
		String zkConnectionStr = "";
		ensembleId = "Zookeeper_" + ensembleId;
		try {
			ComponentConfig zkConf = clusterConfig.getComponents().get(
					ensembleId);
			for (String host : zkConf.getNodes().keySet()) {
				zkConnectionStr += host
						+ Constant.Strings.COLON
						+ zkConf.getAdvanceConf().get(
								ZookeeperConstant.Keys.CLIENT_PORT)
						+ Constant.Strings.COMMA;
			}
		} catch (Exception e) {
			throw new AnkushException(
					"Unable to get Zookeeper component from the Cluster for ensembleId :"
							+ ensembleId);
		}
		if (zkConnectionStr != "") {
			return zkConnectionStr.substring(0, zkConnectionStr.length() - 1);
		} else {
			throw new AnkushException(
					"Unable to get Zookeeper connection string from ensembleId :"
							+ ensembleId);
		}
	}

	public static String errMessage(ServiceAction action, String componentName,
			String host) {
		if (host != null) {
			return "Couldn't " + action + " " + componentName + "  on node : "
					+ host;
		} else {
			return "Couldn't " + action + " " + componentName
					+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS;
		}

	}

}
