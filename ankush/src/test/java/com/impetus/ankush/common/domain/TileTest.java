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
package com.impetus.ankush.common.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.common.tiles.TileInfo;

/**
 * The class <code>TileTest</code> contains tests for the class
 * <code>{@link Tile}</code>.
 */
public class TileTest {
	private Tile fixture;

	/**
	 * Run the Tile() constructor test.
	 */
	@Test
	public void testTile_1() {
		Tile result = new Tile();
		assertNotNull(result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		Tile obj = new Tile();
		obj.setClusterId(new Long(1L));
		obj.setDestroy(new Boolean(true));
		obj.setMinorKey("key");
		obj.setId(new Long(1L));
		TileInfo tileInfo = new TileInfo();
		tileInfo.setLine1("line1");
		obj.setTileInfoObj(tileInfo);
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(Integer.valueOf(1));
		obj.setDataObj(list);

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_2() {
		Object obj = null;

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_3() {
		Object obj = new Object();

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_4() {
		Tile obj = new Tile();
		obj.setClusterId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		Tile obj = new Tile();
		obj.setClusterId(new Long(1L));
		obj.setId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		Tile obj = new Tile();
		obj.setClusterId(new Long(1L));
		obj.setMinorKey("key");
		obj.setId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_7() {
		boolean result = fixture.equals(fixture);

		assertEquals(true, result);
	}

	/**
	 * Run the Long getClusterId() method test.
	 */
	@Test
	public void testGetClusterId_1() {
		Long result = fixture.getClusterId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the Object getDataObj() method test.
	 */
	@Test
	public void testGetDataObj_1() {
		ArrayList<Integer> result = (ArrayList<Integer>) fixture.getDataObj();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1, result.get(0).intValue());
	}

	/**
	 * Run the Boolean getDestroy() method test.
	 */
	@Test
	public void testGetDestroy_1() {
		Boolean result = fixture.getDestroy();

		assertNotNull(result);
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the Long getId() method test.
	 */
	@Test
	public void testGetId_1() {
		Long result = fixture.getId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the String getMinorKey() method test.
	 */
	@Test
	public void testGetMinorKey_1() {
		String result = fixture.getMinorKey();

		assertEquals("key", result);
	}

	/**
	 * Run the TileInfo getTileInfoObj() method test.
	 */
	@Test
	public void testGetTileInfoObj_1() {

		TileInfo result = fixture.getTileInfoObj();

		assertNotNull(result);
		assertEquals("line1", result.getLine1());

	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Tile();
		fixture.setClusterId(new Long(1L));
		fixture.setDestroy(new Boolean(true));
		fixture.setMinorKey("key");
		fixture.setId(new Long(1L));
		TileInfo tileInfo = new TileInfo();
		tileInfo.setLine1("line1");
		fixture.setTileInfoObj(tileInfo);
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(Integer.valueOf(1));
		fixture.setDataObj(list);
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
