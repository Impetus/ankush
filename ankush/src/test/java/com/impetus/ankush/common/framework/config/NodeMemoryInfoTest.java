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
 * The Class NodeMemoryInfoTest.
 */
public class NodeMemoryInfoTest {
	
	NodeMemoryInfo fixture = null;
	@Before
	public void setUp() {
		fixture = new NodeMemoryInfo();
		fixture.setUsed(new Long(1L));
		fixture.setUsedPercentage(new Double(1.0));
		fixture.setTotal(new Long(1L));
		fixture.setFree(new Long(1L));
		fixture.setFreePercentage(new Double(1.0));
	}
	/**
	 * Test node memory info_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testNodeMemoryInfo_1()
		throws Exception {
		NodeMemoryInfo result = new NodeMemoryInfo();
		assertNotNull(result);
	}

	/**
	 * Test get free_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetFree_1()
		throws Exception {
		Long result = fixture.getFree();

		assertNotNull(result);
		assertEquals("1", result.toString());
		assertEquals(1L, result.longValue());
	}

	/**
	 * Test get free percentage_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetFreePercentage_1()
		throws Exception {
		Double result = fixture.getFreePercentage();

		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals(1L, result.longValue());
	}

	/**
	 * Test get total_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetTotal_1()
		throws Exception {
		Long result = fixture.getTotal();

		assertNotNull(result);
		assertEquals("1", result.toString());
		assertEquals(1L, result.longValue());
	}

	/**
	 * Test get used_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetUsed_1()
		throws Exception {
		Long result = fixture.getUsed();

		assertNotNull(result);
		assertEquals("1", result.toString());
		assertEquals(1L, result.longValue());
	}

	/**
	 * Test get used percentage_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetUsedPercentage_1()
		throws Exception {
		Double result = fixture.getUsedPercentage();

		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals(1L, result.longValue());
	}
}
