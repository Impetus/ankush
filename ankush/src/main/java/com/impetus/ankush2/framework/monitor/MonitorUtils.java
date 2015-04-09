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
package com.impetus.ankush2.framework.monitor;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.config.NodeDiskInfo;
import com.impetus.ankush.common.framework.config.NodeMemoryInfo;
import com.impetus.ankush2.logger.AnkushLogger;

public class MonitorUtils {
	private static DecimalFormat formator = new DecimalFormat(".##");
	private static AnkushLogger logger = new AnkushLogger(MonitorUtils.class);

	/**
	 * Gets the node usage map.
	 * 
	 * @param nodeMonitoring
	 *            the node monitoring
	 * @return the node usage map
	 */
	public static Map getNodeUsageMap(NodeMonitoring nodeMonitoring) {
		Map nodeMap = new HashMap();
		nodeMap.put("cpuUsage", "NA");
		nodeMap.put("usedMemory", "NA");
		nodeMap.put("totalMemory", "NA");
		nodeMap.put("freeMemory", "NA");
		nodeMap.put("totalDisk", "NA");
		nodeMap.put("usedDisk", "NA");
		nodeMap.put("freeDisk", "NA");
		nodeMap.put("cores", "NA");
		try {
			if (nodeMonitoring != null
					&& nodeMonitoring.getMonitoringInfo() != null) {
				// adding cpu usage.
				nodeMap.put("cpuUsage", nodeMonitoring.getMonitoringInfo()
						.getUptimeInfos().get(0).getCpuUsage());

				// adding memory usage.
				NodeMemoryInfo memInfo = nodeMonitoring.getMonitoringInfo()
						.getMemoryInfos().get(0);
				nodeMap.put("usedMemory", formator.format(memInfo
						.getActualUsed().doubleValue() / 1024 / 1024 / 1024));
				nodeMap.put("totalMemory", formator.format(memInfo.getTotal()
						.doubleValue() / 1024 / 1024 / 1024));
				nodeMap.put("freeMemory", formator.format(memInfo
						.getActualFree().doubleValue() / 1024 / 1024 / 1024));

				// calculating the total, used and free disk.
				double totalDisk = 0d;
				double usedDisk = 0d;
				double freeDisk = 0d;
				for (NodeDiskInfo diskInfo : nodeMonitoring.getMonitoringInfo()
						.getDiskInfos()) {
					totalDisk = totalDisk
							+ diskInfo.getTotalMemory().doubleValue();
					usedDisk = usedDisk
							+ diskInfo.getUsedMemory().doubleValue();
					freeDisk = freeDisk
							+ diskInfo.getFreeMemory().doubleValue();
				}
				nodeMap.put("totalDisk",
						formator.format(totalDisk / 1024 / 1024));
				nodeMap.put("usedDisk", formator.format(usedDisk / 1024 / 1024));
				nodeMap.put("freeDisk", formator.format(freeDisk / 1024 / 1024));

				// Getting cores.
				nodeMap.put("cores", nodeMonitoring.getMonitoringInfo()
						.getCpuInfos().get(0).getCores());
			}
		} catch (Exception e) {
			logger.error("Unable to get the usage values.", e);
		}
		return nodeMap;
	}
}
