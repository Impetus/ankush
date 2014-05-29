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

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class OracleNoSQLStorageNodeTest.
 */
public class OracleNoSQLStorageNodeTest {

	/**
	 * Test default oracle no sql storage node.
	 */
	@Test
	public void testDefaultOracleNoSQLStorageNode() {
		OracleNoSQLStorageNode result = new OracleNoSQLStorageNode();
		assertNotNull(result);
	}

	/**
	 * Test oracle no sql storage node.
	 */
	@Test
	public void testOracleNoSQLStorageNode() {
		int snId = 2;
		int registryPort = 500;
		int adminPort = 10;
		String hostname = "localhost";
		int active = 1;
		int capacity = 3;
		int rnCount = 4;

		OracleNoSQLStorageNode result = new OracleNoSQLStorageNode();
		result.setActive(active);
		result.setAdminPort(adminPort);
		result.setCapacity(capacity);
		result.setHostname(hostname);
		result.setRegistryPort(registryPort);
		result.setRnCount(rnCount);
		result.setSnId(snId);

		assertNotNull(result);
		assertEquals(active, result.getActive());
		assertEquals(adminPort, result.getAdminPort());
		assertEquals(capacity, result.getCapacity());
		assertEquals(hostname, result.getHostname());
		assertEquals(registryPort, result.getRegistryPort());
		assertEquals(rnCount, result.getRnCount());
		assertEquals(snId, result.getSnId());
	}

}
