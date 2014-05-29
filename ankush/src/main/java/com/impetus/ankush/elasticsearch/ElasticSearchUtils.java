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
package com.impetus.ankush.elasticsearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.MonitoringManager;

/**
 * The Class ElasticSearchUtils.
 */
public class ElasticSearchUtils {

	
	/**
	 * Ge firstt live node.
	 *
	 * @param nodes the nodes
	 * @return the map
	 */
	public static Map<String, Object> geFirsttLiveNode(List<NodeConf> nodes) {
		System.out.println("ElasticSearchClusterUtils...");
		MonitoringManager monitoringManager = new MonitoringManager();
		Map<String, Object> outputMap = new HashMap<String, Object>();
		String errorString = "This operation cann't be performed As Not able to get ElasticSearch service status on any node." +
				"Please look that AnkushAgent is running properly.";
		boolean status = false;
		//iterate through all the nodes of ES
		for (NodeConf node : nodes) {
			//get monitoringData
			NodeMonitoring nodeMonitoring = monitoringManager
					.getMonitoringData(node.getPublicIp());
			if (nodeMonitoring != null) {
				Map<String, Boolean> serviceStatusMap = nodeMonitoring
						.getServiceStatus();
				if (serviceStatusMap != null
						&& (serviceStatusMap.get(Constant.Role.AGENT))) {
					if (serviceStatusMap.get(Constant.Process.ELASTICSEARCH)) {
						outputMap.put(Constant.Keys.OUTPUT, node);
						status = true;
						break;
					}else{
						errorString = "This operation cann't be performed As ElasticSearch is Down on all the nodes";
						outputMap.put(Constant.Keys.OUTPUT, null);
					}
				}
			}
		}
		outputMap.put(Constant.Keys.STATUS, status);
		outputMap.put(Constant.Keys.ERROR, errorString);
		return outputMap;
	}

}
