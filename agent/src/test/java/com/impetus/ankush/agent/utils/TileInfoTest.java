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
package com.impetus.ankush.agent.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>TileInfoTest</code> contains tests for the class
 * <code>{@link TileInfo}</code>.
 * 
 * @author hokam
 */
public class TileInfoTest {

	private TileInfo fixture = null;

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		fixture = new TileInfo();
		fixture.setLine3("line3");
		fixture.setUrl("url");
		fixture.setLine2("line2");
		fixture.setData(new HashMap());
		fixture.setStatus("status");
		fixture.setLine1("line1");
	}

	/**
	 * Run the Map<String, Object> getData() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetData_1() throws Exception {
		Map<String, Object> result = fixture.getData();
		assertNotNull(result);
		assertEquals(new HashMap(), result);
	}

	/**
	 * Run the String getLine1() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetLine1_1() throws Exception {
		String result = fixture.getLine1();
		assertNotNull(result);
		assertEquals("line1", result);
	}

	/**
	 * Run the String getLine2() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetLine2() throws Exception {
		String result = fixture.getLine2();
		assertNotNull(result);
		assertEquals("line2", result);
	}

	/**
	 * Run the String getLine3() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetLine3() throws Exception {
		String result = fixture.getLine3();
		assertNotNull(result);
		assertEquals("line3", result);
	}

	/**
	 * Run the String getStatus() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetStatus() throws Exception {
		String result = fixture.getStatus();

		assertNotNull(result);
		assertEquals("status", result);
	}

	/**
	 * Run the String getUrl() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetUrl() throws Exception {
		String result = fixture.getUrl();
		assertNotNull(result);
		assertEquals("url", result);
	}
}
