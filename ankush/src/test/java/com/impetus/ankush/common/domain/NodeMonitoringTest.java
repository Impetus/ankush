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
package com.impetus.ankush.common.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.framework.config.MonitoringInfo;

/**
 * The class <code>NodeMonitoringTest</code> contains tests for the class
 * <code>{@link NodeMonitoring}</code>.
 */
public class NodeMonitoringTest {
	private static final long DATE = 1372406902786L;
	private NodeMonitoring fixture;

	/**
	 * Run the NodeMonitoring() constructor test.
	 */
	@Test
	public void testNodeMonitoring_1() {
		NodeMonitoring result = new NodeMonitoring();
		assertNotNull(result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		NodeMonitoring obj = new NodeMonitoring();
		obj.setNodeId(new Long(1L));
		obj.setUpdateTime(new Date(DATE));
		obj.setId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_2() {
		Object obj = null;

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_3() {
		Object obj = new Object();

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_4() {
		Object obj = new NodeMonitoring();

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		NodeMonitoring obj = new NodeMonitoring();
		obj.setNodeId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		NodeMonitoring obj = new NodeMonitoring();
		obj.setNodeId(new Long(1L));
		obj.setUpdateTime(new Date(DATE));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_7() {
		NodeMonitoring obj = new NodeMonitoring();
		obj.setNodeId(new Long(1L));
		obj.setUpdateTime((Date) null);

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_8() {
		NodeMonitoring obj = new NodeMonitoring();
		obj.setNodeId(new Long(1L));
		obj.setUpdateTime(new Date(DATE));
		obj.setMonitoringInfo(new MonitoringInfo());
		obj.setServiceStatus(new HashMap<String, Boolean>());
		obj.setTechnologyData(new HashMap<String, TechnologyData>());

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_9() {
		boolean result = fixture.equals(fixture);

		assertEquals(true, result);
	}

	/**
	 * Run the Long getId() method test.
	 */
	@Test
	public void testGetId_1() {
		Long result = fixture.getId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the MonitoringInfo getMonitoringInfo() method test.
	 */
	@Test
	public void testGetMonitoringInfo_1() {
		MonitoringInfo result = fixture.getMonitoringInfo();
		assertNotNull(result);
	}

	/**
	 * Run the Long getNodeId() method test.
	 */
	@Test
	public void testGetNodeId_1() {
		Long result = fixture.getNodeId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus() method test.
	 */
	@Test
	public void testGetServiceStatus_1() {
		Map<String, Boolean> result = fixture.getServiceStatus();

		assertNotNull(result);
	}

	/**
	 * Run the TechnologyData getTechnologyData() method test.
	 */
	@Test
	public void testGetTechnologyData_1() {
		Map result = fixture.getTechnologyData();

		assertNotNull(result);
	}

	/**
	 * Run the Date getUpdateTime() method test.
	 */
	@Test
	public void testGetUpdateTime_1() {

		Date result = fixture.getUpdateTime();

		assertNotNull(result);
		assertEquals(DATE, result.getTime());
	}

	/**
	 * Run the boolean isAgentDown() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 28/6/13 1:37 PM
	 */
	@Test
	public void testIsAgentDown_1() {
		NodeMonitoring fixture = new NodeMonitoring();
		fixture.setNodeId(new Long(1L));
		fixture.setUpdateTime(new Date());
		fixture.setId(new Long(1L));

		boolean result = fixture.isAgentDown();

		assertEquals(false, result);
	}

	/**
	 * Run the boolean isAgentDown() method test.
	 */
	@Test
	public void testIsAgentDown_2() {
		boolean result = fixture.isAgentDown();

		assertEquals(true, result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new NodeMonitoring();
		fixture.setNodeId(new Long(1L));
		fixture.setUpdateTime(new Date(DATE));
		fixture.setId(new Long(1L));
		fixture.setMonitoringInfo(new MonitoringInfo());
		fixture.setServiceStatus(new HashMap<String, Boolean>());
		fixture.setTechnologyData(new HashMap<String, TechnologyData>());
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
