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

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>AppConfTest</code> contains tests for the class
 * <code>{@link AppConf}</code>.
 */
public class AppConfTest {
	private AppConf fixture;

	/**
	 * Run the AppConf() constructor test.
	 */
	@Test
	public void testAppConf_1() {
		AppConf result = new AppConf();
		assertNotNull(result);
	}

	/**
	 * Run the String getConfKey() method test.
	 */
	@Test
	public void testGetConfKey_1() {

		String result = fixture.getConfKey();

		assertEquals("key", result);
	}

	/**
	 * Run the String getConfValue() method test.
	 */
	@Test
	public void testGetConfValue_1() {

		String result = fixture.getConfValue();

		assertEquals("value", result);
	}

	/**
	 * Run the Object getObject() method test.
	 */
	@Test
	public void testGetObject_1() {
		Map<String, String> object = new HashMap<String, String>();
		object.put("foo", "bar");
		fixture.setObject(object);

		Map<String, String> result = (Map<String, String>) fixture.getObject();

		assertNotNull(result);
		assertEquals("bar", result.get("foo"));
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new AppConf();
		fixture.setConfKey("key");
		fixture.setConfValue("value");
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;

	}
}
