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
 * The Class NodeSwapInfoTest.
 */
public class NodeSwapInfoTest {

	private NodeSwapInfo fixture = null;

	@Before
	public void setUp() {
		fixture = new NodeSwapInfo();
		fixture.setPageIn(1L);
		fixture.setFreeSystemSwap(1L);
		fixture.setUsedSystemSwap(1L);
		fixture.setTotalSystemSwap(1L);
		fixture.setPageOut(1L);
	}

	/**
	 * Test node swap info_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNodeSwapInfo_1() throws Exception {
		NodeSwapInfo result = new NodeSwapInfo();
		assertNotNull(result);
	}

	/**
	 * Test get free system swap_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetFreeSystemSwap_1() throws Exception {
		long result = fixture.getFreeSystemSwap();
		assertEquals(1L, result);
	}

	/**
	 * Test get page in_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPageIn_1() throws Exception {
		long result = fixture.getPageIn();
		assertEquals(1L, result);
	}

	/**
	 * Test get page out_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPageOut_1() throws Exception {
		long result = fixture.getPageOut();
		assertEquals(1L, result);
	}

	/**
	 * Test get total system swap_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetTotalSystemSwap_1() throws Exception {
		long result = fixture.getTotalSystemSwap();
		assertEquals(1L, result);
	}

	/**
	 * Test get used system swap_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetUsedSystemSwap_1() throws Exception {
		long result = fixture.getUsedSystemSwap();
		assertEquals(1L, result);
	}
}
