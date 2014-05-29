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
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationAttributes;

import com.impetus.ankush.common.tiles.TileInfo;

/**
 * The class <code>OracleNoSQLTechnologyDataTest</code> contains tests for the
 * class <code>{@link OracleNoSQLTechnologyData}</code>.
 */
public class OracleNoSQLTechnologyDataTest {

	/** The fixture. */
	OracleNoSQLTechnologyData fixture;

	/**
	 * Run the OracleNoSQLTechnologyData() constructor test.
	 */
	@Test
	public void testOracleNoSQLTechnologyData_1() {

		OracleNoSQLTechnologyData result = new OracleNoSQLTechnologyData();

		assertNotNull(result);
		assertEquals(0, result.getActive());
		assertEquals(false, result.isAdminUp());
	}

	/**
	 * Run the int getActive() method test.
	 */
	@Test
	public void testGetActive_1() {
		int result = fixture.getActive();
		assertEquals(1, result);
	}

	/**
	 * Run the List<OracleNoSQLRepNode> getRepNodeList() method test.
	 */
	@Test
	public void testGetRepNodeList_1() {
		List<OracleNoSQLRepNode> result = fixture.getRepNodeList();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<OracleNoSQLStorageNode> getStorageNodeList() method test.
	 */
	@Test
	public void testGetStorageNodeList_1() {

		List<OracleNoSQLDatacenter> result = fixture.getDatacenterList();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the OracleNoSQLTechnologyData getTechnologyData(Cluster) method test.
	 */
	@Test
	public void testGetTechnologyData_1() {
		OracleNoSQLTechnologyData result = OracleNoSQLTechnologyData
				.getTechnologyData(null);

		assertEquals(null, result);
	}

	/**
	 * Run the OracleNoSQLTechnologyData getTechnologyData(Cluster) method test.
	 */
	@Test
	public void testGetTechnologyData_2() {
		OracleNoSQLConf conf = new OracleNoSQLConf();
		conf.setNodes(new ArrayList<OracleNoSQLNodeConf>());

		OracleNoSQLTechnologyData result = OracleNoSQLTechnologyData
				.getTechnologyData(conf);

		assertEquals(null, result);
	}

	/**
	 * Run the List<TileInfo> getTiles() method test.
	 */
	@Test
	public void testGetTiles_1() {

		List<TileInfo> result = fixture.getTiles();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map getTopologyTree() method test.
	 */
	@Test
	public void testGetTopologyTree_1() {

		Map result = fixture.getTopologyTree();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the boolean isAdminUp() method test.
	 */
	@Test
	public void testIsAdminUp_1() {

		boolean result = fixture.isAdminUp();

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		fixture = new OracleNoSQLTechnologyData();
		fixture.setAdminUp(true);
		fixture.setRepNodeList(new ArrayList<OracleNoSQLRepNode>());
		fixture.setActive(1);
		fixture.setDatacenterList(new ArrayList<OracleNoSQLDatacenter>());
		fixture.setTiles(new ArrayList<TileInfo>());
		fixture.setTopologyTree(new AnnotationAttributes());
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
		// Add additional tear down code here
	}
}
