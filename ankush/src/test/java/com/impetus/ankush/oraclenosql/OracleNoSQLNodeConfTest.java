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
package com.impetus.ankush.oraclenosql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * The Class OracleNoSQLNodeConfTest.
 */
public class OracleNoSQLNodeConfTest {

	/**
	 * Test default oracle no sql node conf.
	 */
	@Test
	public void testDefaultOracleNoSQLNodeConf() {

		OracleNoSQLNodeConf result = new OracleNoSQLNodeConf();

		// add additional test code here
		assertNotNull(result);
		assertEquals("StorageNode", result.getType());
		assertEquals(null, result.getConnection());
		assertEquals(false, result.isConnected());
		assertEquals(Boolean.FALSE, result.isAdmin());
		assertEquals(null, result.getRegistryPort());
		assertEquals(null, result.getAdminPort());
		assertEquals(new Integer(1), result.getCapacity());
		assertEquals(new Integer(0), result.getCpuNum());
		assertEquals(new Long(0L), result.getMemoryMb());
		assertEquals(null, result.getHaPortRangeStart());
		assertEquals(null, result.getHaPortRangeEnd());
		assertEquals(new Integer(0), result.getSnId());
		assertEquals(null, result.getMessage());
		assertEquals(null, result.getId());
		assertEquals(Boolean.FALSE, result.getStatus());
		assertEquals(null, result.getOs());
		assertEquals("depoying", result.getNodeState());
		assertEquals(null, result.getPublicIp());
		assertEquals(null, result.getPrivateIp());
	}

	/**
	 * Test oracle no sql node conf.
	 */
	@Test
	public void testOracleNoSQLNodeConf() {

		OracleNoSQLNodeConf result = new OracleNoSQLNodeConf();
		String type = "TestType";
		Boolean admin = Boolean.TRUE;
		Integer adminPort = Integer.valueOf(6001);
		Integer registryPort = Integer.valueOf(6000);
		Integer snId = Integer.valueOf(2);
		Integer haPortRangeStart = Integer.valueOf(6010);
		Integer haPortRangeEnd = Integer.valueOf(6020);
		Long memoryMb = Long.valueOf(1024);
		Integer capacity = Integer.valueOf(2);
		Integer cpuNum = Integer.valueOf(3);

		result.setAdmin(admin);
		result.setAdminPort(adminPort);
		result.setRegistryPort(registryPort);
		result.setSnId(snId);
		result.setHaPortRangeStart(haPortRangeStart);
		result.setHaPortRangeEnd(haPortRangeEnd);
		result.setMemoryMb(memoryMb);
		result.setCapacity(capacity);
		result.setCpuNum(cpuNum);

		// add additional test code here
		assertNotNull(result);
		assertEquals(true, result.isAdmin());
		assertEquals("Admin/StorageNode", result.getType());
		result.setType(type);
		assertEquals(type, result.getType());
		assertEquals(null, result.getConnection());
		assertEquals(false, result.isConnected());
		assertEquals(admin, result.isAdmin());
		assertEquals(registryPort, result.getRegistryPort());
		assertEquals(adminPort, result.getAdminPort());
		assertEquals(capacity, result.getCapacity());
		assertEquals(cpuNum, result.getCpuNum());
		assertEquals(memoryMb, result.getMemoryMb());
		assertEquals(haPortRangeStart, result.getHaPortRangeStart());
		assertEquals(haPortRangeEnd, result.getHaPortRangeEnd());
		assertEquals(snId, result.getSnId());

		// False admin
		result.setAdmin(false);
		assertEquals(false, result.isAdmin());
		assertEquals(type, result.getType());
	}

	/**
	 * Test finalize.
	 * 
	 * @throws Throwable
	 *             the throwable
	 */
	@Test
	public void testFinalize() throws Throwable {
		OracleNoSQLNodeConf fixture = new OracleNoSQLNodeConf();

		fixture.finalize();

		// add additional test code here
	}
}
