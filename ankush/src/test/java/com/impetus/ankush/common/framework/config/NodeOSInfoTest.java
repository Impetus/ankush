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
 * The Class NodeOSInfoTest.
 */
public class NodeOSInfoTest {

	private NodeOSInfo fixture = null;

	@Before
	public void setUp() {
		fixture = new NodeOSInfo();
		fixture.setDataModel("64");
		fixture.setArch("x86_64");
		fixture.setDescription("CentOS 6.2");
		fixture.setMachineName("x86_64");
		fixture.setVendor("CentOS");
	}

	/**
	 * Test node os info_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNodeOSInfo_1() throws Exception {
		NodeOSInfo result = new NodeOSInfo();
		assertNotNull(result);
	}

	/**
	 * Test get arch_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetArch_1() throws Exception {
		String result = fixture.getArch();
		assertEquals("x86_64", result);
	}

	/**
	 * Test get data model_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetDataModel_1() throws Exception {
		String result = fixture.getDataModel();
		assertEquals("64", result);
	}

	/**
	 * Test get description_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetDescription_1() throws Exception {
		String result = fixture.getDescription();
		assertEquals("CentOS 6.2", result);
	}

	/**
	 * Test get machine name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetMachineName_1() throws Exception {
		String result = fixture.getMachineName();
		assertEquals("x86_64", result);
	}

	/**
	 * Test get vendor_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetVendor_1() throws Exception {
		String result = fixture.getVendor();
		assertEquals("CentOS", result);
	}
}
