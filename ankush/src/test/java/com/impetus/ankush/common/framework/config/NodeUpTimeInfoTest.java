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

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class NodeUpTimeInfoTest.
 */
public class NodeUpTimeInfoTest {

	private NodeUpTimeInfo fixture;

	@Before
	public void setUp() {
		fixture = new NodeUpTimeInfo();
		fixture.setLoadAverage1(new Double(1.0));
		fixture.setUpTime(new Double(1.0));
		fixture.setCpuUsage(new Double(1.0));
		fixture.setLoggedUsers("root,test");
		fixture.setLoadAverage2(new Double(1.0));
		fixture.setLoadAverage3(new Double(1.0));
	}

	/**
	 * Test node up time info_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNodeUpTimeInfo() throws Exception {
		NodeUpTimeInfo result = new NodeUpTimeInfo();
		assertNotNull(result);
	}

	/**
	 * Test get cpu usage_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCpuUsage() throws Exception {
		Double result = fixture.getCpuUsage();

		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals(1.0, result.doubleValue(), 1.0);
	}

	/**
	 * Test get load average1_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetLoadAverage1() throws Exception {
		Double result = fixture.getLoadAverage1();

		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals(1.0, result.doubleValue(), 1.0);
	}

	/**
	 * Test get load average2_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetLoadAverage2() throws Exception {
		Double result = fixture.getLoadAverage2();
		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals(1.0, result.doubleValue(), 1.0);
	}

	/**
	 * Test get load average3_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetLoadAverage3() throws Exception {
		Double result = fixture.getLoadAverage3();

		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals(1.0, result.doubleValue(), 1.0);
	}

	/**
	 * Test get logged users_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetLoggedUsers_1() throws Exception {
		String result = fixture.getLoggedUsers();
		assertEquals("root,test", result);
	}

	/**
	 * Test get up time_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetUpTime_1() throws Exception {
		Double result = fixture.getUpTime();

		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals(1.0, result.doubleValue(), 1.0);
	}
}
