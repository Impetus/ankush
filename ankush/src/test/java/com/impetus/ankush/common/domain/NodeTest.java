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
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.ManagedList;

import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The class <code>NodeTest</code> contains tests for the class
 * <code>{@link Node}</code>.
 */
public class NodeTest {
	private static final long DATE = 1372406912786L;
	private Node fixture;

	/**
	 * Run the Node() constructor test.
	 */
	@Test
	public void testNode_1() {
		Node result = new Node();
		assertNotNull(result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		Node obj = new Node();
		obj.setId(new Long(1L));
		obj.setCreatedAt(new Date());
		obj.setState("state");
		obj.setPublicIp("public");
		obj.setMonitors(new ManagedList<NodeMonitoring>());
		obj.setClusterId(new Long(1L));
		obj.setPrivateIp("private");
		obj.setRackInfo("");

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
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
		Node obj = new Node();
		obj.setPrivateIp("private");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		Node obj = new Node();
		obj.setPublicIp("");
		obj.setPrivateIp("");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		Node obj = new Node();
		obj.setPublicIp((String) null);
		obj.setPrivateIp("");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_7() {
		Node obj = new Node();
		obj.setPublicIp("public");
		obj.setPrivateIp("private");
		obj.setClusterId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_8() {
		boolean result = fixture.equals(fixture);

		assertEquals(true, result);
	}

	/**
	 * Run the Long getClusterId() method test.
	 */
	@Test
	public void testGetClusterId_1() {
		Long result = fixture.getClusterId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the Date getCreatedAt() method test.
	 */
	@Test
	public void testGetCreatedAt_1() {
		Date result = fixture.getCreatedAt();

		assertNotNull(result);
		assertEquals(DATE, result.getTime());
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
	 * Run the Set<NodeMonitoring> getMonitors() method test.
	 */
	@Test
	public void testGetMonitors_1() {
		List<NodeMonitoring> result = fixture.getMonitors();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the NodeConf getNodeConf() method test.
	 */
	@Test
	public void testGetNodeConf_1() {
		NodeConf result = fixture.getNodeConf();

		assertNotNull(result);
		assertEquals("private", result.getPrivateIp());

	}

	/**
	 * Run the String getPrivateIp() method test.
	 */
	@Test
	public void testGetPrivateIp_1() {
		String result = fixture.getPrivateIp();

		assertEquals("private", result);
	}

	/**
	 * Run the String getPublicIp() method test.
	 */
	@Test
	public void testGetPublicIp_1() {
		String result = fixture.getPublicIp();

		assertEquals("public", result);
	}

	/**
	 * Run the String getRackInfo() method test.
	 */
	@Test
	public void testGetRackInfo_1() {
		String result = fixture.getRackInfo();

		assertEquals("rack", result);
	}

	/**
	 * Run the String getState() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 28/6/13 1:37 PM
	 */
	@Test
	public void testGetState_1() {
		String result = fixture.getState();

		assertEquals("state", result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Node();
		fixture.setId(new Long(1L));
		fixture.setCreatedAt(new Date(DATE));
		fixture.setState("state");
		fixture.setPublicIp("public");
		fixture.setMonitors(new ManagedList<NodeMonitoring>());
		fixture.setClusterId(new Long(1L));
		fixture.setPrivateIp("private");
		fixture.setRackInfo("rack");
		NodeConf nodeConf = new NodeConf();
		nodeConf.setPrivateIp("private");
		fixture.setNodeConf(nodeConf);
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
