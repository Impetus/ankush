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
package com.impetus.ankush.common.framework.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * The Class MonitoringInfoTest.
 */
public class MonitoringInfoTest {

	/**
	 * Test default monitoring info.
	 */
	@Test
	public void testDefaultMonitoringInfo() {
		MonitoringInfo result = new MonitoringInfo();

		assertNotNull(result);
		assertEquals(null, result.getCpuInfos());
		assertEquals(null, result.getDiskInfos());
		assertEquals(null, result.getMemoryInfos());
		assertEquals(null, result.getOsInfos());
		assertEquals(null, result.getSwapInfos());
		assertEquals(null, result.getUptimeInfos());
	}

	/**
	 * Test monitoring info.
	 */
	@Test
	public void testMonitoringInfo() {
		List<NodeCpuInfo> cpuInfos = new LinkedList();
		List<NodeMemoryInfo> memoryInfos = new LinkedList();
		List<NodeDiskInfo> diskInfos = new LinkedList();
		List<NodeUpTimeInfo> uptimeInfos = new LinkedList();
		List<NodeOSInfo> osInfos = new LinkedList();
		List<NodeSwapInfo> swapInfos = new LinkedList();

		MonitoringInfo result = new MonitoringInfo();
		result.setCpuInfos(cpuInfos);
		result.setDiskInfos(diskInfos);
		result.setMemoryInfos(memoryInfos);
		result.setOsInfos(osInfos);
		result.setSwapInfos(swapInfos);
		result.setUptimeInfos(uptimeInfos);

		assertNotNull(result);
		assertEquals(cpuInfos, result.getCpuInfos());
		assertEquals(diskInfos, result.getDiskInfos());
		assertEquals(memoryInfos, result.getMemoryInfos());
		assertEquals(osInfos, result.getOsInfos());
		assertEquals(swapInfos, result.getSwapInfos());
		assertEquals(uptimeInfos, result.getUptimeInfos());
	}

}
