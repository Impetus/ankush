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
 * The Class NodeCpuInfoTest.
 */
public class NodeCpuInfoTest {

	// cpu info
	private NodeCpuInfo cpu = null;

	@Before
	public void setUp() {
		cpu = new NodeCpuInfo();
		cpu.setVendor("Acer");
		cpu.setClock(new Integer(2048));
		cpu.setCores(new Integer(2));
		cpu.setCacheSize(new Long(1024));

	}

	@After
	public void tearDown() {

	}

	/**
	 * Test node cpu info_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNodeCpuInfo() throws Exception {
		assertNotNull(cpu);
	}

	/**
	 * Test get cache size_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCacheSize_1() throws Exception {
		Long result = cpu.getCacheSize();

		assertNotNull(result);
		assertEquals("1024", result.toString());
		assertEquals(new Long(1024), result);
	}

	/**
	 * Test get clock_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetClock_1() throws Exception {
		Integer result = cpu.getClock();

		assertNotNull(result);
		assertEquals("2048", result.toString());
		assertEquals(new Integer(2048), result);
	}

	/**
	 * Test get cores_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCores_1() throws Exception {
		Integer result = cpu.getCores();

		assertNotNull(result);
		assertEquals("2", result.toString());
		assertEquals(new Integer(2), result);
	}

	/**
	 * Test get vendor_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetVendor_1() throws Exception {
		String result = cpu.getVendor();

		assertNotNull(result);
		assertEquals("Acer", result.toString());
	}
}
