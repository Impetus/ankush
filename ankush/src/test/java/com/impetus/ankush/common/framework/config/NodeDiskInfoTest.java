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
 * The Class NodeDiskInfoTest.
 */
public class NodeDiskInfoTest {
	
	private NodeDiskInfo fixture = null;
	/**
	 * @return
	 */
	@Before
	public void setUp() {
		fixture = new NodeDiskInfo();
		fixture.setFreeMemory(new Long(300L));
		fixture.setUsedMemory(new Long(724L));
		fixture.setAvailableMemory(new Long(123L));
		fixture.setFileSystemType("NTFS");
		fixture.setTotalMemory(new Long(1024L));
		fixture.setDeviceName("D");
		fixture.setDirName("D:/");
	}
	/**
	 * Test node disk info_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testNodeDiskInfo_1()
		throws Exception {
		NodeDiskInfo result = new NodeDiskInfo();
		assertNotNull(result);
	}

	/**
	 * Test get available memory_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetAvailableMemory()
		throws Exception {
		Long result = fixture.getAvailableMemory();

		assertNotNull(result);
		assertEquals("123", result.toString());
		assertEquals(new Long(123), result);
	}

	/**
	 * Test get device name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetDeviceName()
		throws Exception {
		String result = fixture.getDeviceName();

		assertEquals("D", result);
	}

	/**
	 * Test get dir name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetDirName()
		throws Exception {
		String result = fixture.getDirName();

		assertEquals("D:/", result);
	}

	/**
	 * Test get file system type_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetFileSystemType()
		throws Exception {
		String result = fixture.getFileSystemType();

		assertEquals("NTFS", result);
	}

	/**
	 * Test get free memory_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetFreeMemory()
		throws Exception {
		Long result = fixture.getFreeMemory();

		assertNotNull(result);
		assertEquals("300", result.toString());
		assertEquals(new Long(300), result);
	}

	/**
	 * Test get total memory_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetTotalMemory()
		throws Exception {
		Long result = fixture.getTotalMemory();

		assertNotNull(result);
		assertEquals("1024", result.toString());
		assertEquals(new Long(1024), result);
	}

	/**
	 * Test get used memory_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetUsedMemory()
		throws Exception {
		Long result = fixture.getUsedMemory();

		assertNotNull(result);
		assertEquals("724", result.toString());
		assertEquals(new Long(724), result);
	}
}
