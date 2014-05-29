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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;
import com.impetus.ankush.common.framework.config.NodeConf;
import net.minidev.json.JSONArray;

/**
 * The class <code>OracleNoSQLConfTest</code> contains tests for the class
 * <code>{@link OracleNoSQLConf}</code>.
 */
public class OracleNoSQLConfTest {
	OracleNoSQLConf fixture = null;

	/**
	 * Run the OracleNoSQLConf() constructor test.
	 */
	@Test
	public void testOracleNoSQLConf_1() {

		OracleNoSQLConf result = new OracleNoSQLConf();

		assertNotNull(result);
		assertEquals("", result.getDataPath());
		assertEquals("", result.getBasePath());
		assertEquals(new Integer(5000), result.getRegistryPort());
		assertEquals(new Integer(5010), result.getHaPortRangeStart());
		assertEquals(new Integer(5020), result.getHaPortRangeEnd());
		assertEquals("", result.getTopologyName());
		assertEquals("", result.getDatacenterName());
		assertEquals(new Integer(3), result.getReplicationFactor());
		assertEquals(new Long(100L), result.getPartitionCount());
		assertEquals("", result.getNtpServer());
		assertEquals(null, result.getTileId());
		assertEquals(null, result.getPrivateKey());
		assertEquals(null, result.getOperationId());
		assertEquals(null, result.getUsername());
		assertEquals(null, result.getCurrentUser());
		assertEquals(null, result.getClusterConf());
		assertEquals(null, result.getInstallationPath());
		assertEquals(null, result.getComponentVendor());
		assertEquals(null, result.getComponentVersion());
		assertEquals(null, result.getLocalBinaryFile());
		assertEquals(null, result.getTarballUrl());
		assertEquals(null, result.getServerTarballLocation());
		assertEquals(null, result.getComponentHome());
		assertEquals(null, result.getClusterName());
		assertEquals(null, result.getClusterDbId());
		assertEquals(null, result.getCertification());
		assertEquals(null, result.getPassword());
	}

	/**
	 * Run the String getBasePath() method test.
	 */
	@Test
	public void testGetBasePath_1() {

		String result = fixture.getBasePath();

		assertEquals("basepath", result);
	}

	/**
	 * Run the Set<NodeConf> getCompNodes() method test.
	 */
	@Test
	public void testGetCompNodes_1() {

		Set<NodeConf> result = fixture.getCompNodes();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the String getDataPath() method test.
	 */
	@Test
	public void testGetDataPath_1() {

		String result = fixture.getDataPath();

		assertEquals("datapath", result);
	}

	/**
	 * Run the String getDatacenterName() method test.
	 */
	@Test
	public void testGetDatacenterName_1() {

		String result = fixture.getDatacenterName();

		assertEquals("datacenter", result);
	}

	/**
	 * Run the Integer getHaPortRangeEnd() method test.
	 */
	@Test
	public void testGetHaPortRangeEnd_1() {

		Integer result = fixture.getHaPortRangeEnd();

		assertNotNull(result);
		assertEquals(1, result.intValue());
	}

	/**
	 * Run the Integer getHaPortRangeStart() method test.
	 */
	@Test
	public void testGetHaPortRangeStart_1() {

		Integer result = fixture.getHaPortRangeStart();

		assertNotNull(result);
		assertEquals(1, result.intValue());
	}

	/**
	 * Run the List<OracleNoSQLNodeConf> getNodes() method test.
	 */
	@Test
	public void testGetNodes_1() {

		List<OracleNoSQLNodeConf> result = fixture.getNodes();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the String getNtpServer() method test.
	 */
	@Test
	public void testGetNtpServer_1() {

		String result = fixture.getNtpServer();

		assertEquals("ntpserver", result);
	}

	/**
	 * Run the Long getPartitionCount() method test.
	 */
	@Test
	public void testGetPartitionCount_1() {

		Long result = fixture.getPartitionCount();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the Integer getRegistryPort() method test.
	 */
	@Test
	public void testGetRegistryPort_1() {

		Integer result = fixture.getRegistryPort();

		assertNotNull(result);
		assertEquals(1, result.intValue());
	}

	/**
	 * Run the Integer getReplicationFactor() method test.
	 */
	@Test
	public void testGetReplicationFactor_1() {

		Integer result = fixture.getReplicationFactor();

		assertNotNull(result);
		assertEquals(1, result.intValue());
	}

	/**
	 * Run the Long getTileId() method test.
	 */
	@Test
	public void testGetTileId_1() {

		Long result = fixture.getTileId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the String getTopologyName() method test.
	 */
	@Test
	public void testGetTopologyName_1() {

		String result = fixture.getTopologyName();

		assertEquals("topology", result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new OracleNoSQLConf();
		fixture.setNtpServer("ntpserver");
		fixture.setReplicationFactor(new Integer(1));
		fixture.setTileId(new Long(1L));
		fixture.setTopologyName("topology");
		fixture.setPartitionCount(new Long(1L));
		fixture.setNodes(new ArrayList<OracleNoSQLNodeConf>());
		fixture.setDatacenterName("datacenter");
		fixture.setRegistryPort(new Integer(1));
		fixture.setHaPortRangeStart(new Integer(1));
		fixture.setDataPath("datapath");
		fixture.setHaPortRangeEnd(new Integer(1));
		fixture.setBasePath("basepath");
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
