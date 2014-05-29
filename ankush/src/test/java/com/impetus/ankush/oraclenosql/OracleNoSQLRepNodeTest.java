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
 * The Class OracleNoSQLRepNodeTest.
 */
public class OracleNoSQLRepNodeTest {

	/**
	 * Test default oracle no sql rep node.
	 */
	@Test
	public void testDefaultOracleNoSQLRepNode() {
		OracleNoSQLRepNode result = new OracleNoSQLRepNode();
		assertNotNull(result);
	}

	/**
	 * Test oracle no sql rep node.
	 */
	@Test
	public void testOracleNoSQLRepNode() {
		int rgId = 2;
		String hostname = "hostname";
		int nodeNum = 3;
		int snId = 1;
		int port = 5000;
		int active = 2;
		boolean master = true;
		float avg = 3.4F;
		int percent95 = 2;
		long throughput = 5L;

		OracleNoSQLRepNode result = new OracleNoSQLRepNode();
		result.setActive(active);
		result.setHostname(hostname);
		result.setMaster(master);
		result.setNodeNum(nodeNum);
		result.setPort(port);
		result.setRgId(rgId);
		result.setSnId(snId);
		
		assertNotNull(result);
		assertEquals(active, result.getActive());
		assertEquals(hostname, result.getHostname());
		assertEquals(master, result.isMaster());
		assertEquals(nodeNum, result.getNodeNum());
		assertEquals(port, result.getPort());
		assertEquals(rgId, result.getRgId());
		assertEquals(snId, result.getSnId());		
	}
}
