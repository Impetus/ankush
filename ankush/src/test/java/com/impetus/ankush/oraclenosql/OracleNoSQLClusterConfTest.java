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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The class <code>OracleNoSQLClusterConfTest</code> contains tests for the
 * class <code>{@link OracleNoSQLClusterConf}</code>.
 */
public class OracleNoSQLClusterConfTest {
	/**
	 * Run the OracleNoSQLClusterConf() constructor test.
	 */
	@Test
	public void testOracleNoSQLClusterConf_1() {
		OracleNoSQLClusterConf result = new OracleNoSQLClusterConf();
		assertNotNull(result);
	}

	/**
	 * Run the List<OracleNoSQLNodeConf> getNewNodes() method test.
	 */
	@Test
	public void testGetNewNodes_1() {
		OracleNoSQLClusterConf fixture = new OracleNoSQLClusterConf();
		fixture.setOracleNoSQLConf(new OracleNoSQLConf());
		fixture.setNewNodes(new ArrayList<OracleNoSQLNodeConf>());

		List<OracleNoSQLNodeConf> result = fixture.getNewNodes();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<NodeConf> getNodeConfs() method test.
	 */
	@Test
	public void testGetNodeConfs_1() {
		OracleNoSQLClusterConf fixture = new OracleNoSQLClusterConf();
		fixture.setOracleNoSQLConf(new OracleNoSQLConf());
		fixture.setNewNodes(new ArrayList<OracleNoSQLNodeConf>());

		List<NodeConf> result = fixture.getNodeConfs();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the int getNodeCount() method test.
	 */
	@Test
	public void testGetNodeCount_1() {
		OracleNoSQLClusterConf fixture = new OracleNoSQLClusterConf();
		fixture.setOracleNoSQLConf(new OracleNoSQLConf());
		fixture.setNewNodes(new ArrayList<OracleNoSQLNodeConf>());

		int result = fixture.getNodeCount();

		assertEquals(0, result);
	}

	/**
	 * Run the OracleNoSQLConf getOracleNoSQLConf() method test.
	 */
	@Test
	public void testGetOracleNoSQLConf_1() {
		OracleNoSQLClusterConf fixture = new OracleNoSQLClusterConf();
		fixture.setOracleNoSQLConf(new OracleNoSQLConf());
		fixture.setNewNodes(new ArrayList<OracleNoSQLNodeConf>());

		OracleNoSQLConf result = fixture.getOracleNoSQLConf();

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
}
