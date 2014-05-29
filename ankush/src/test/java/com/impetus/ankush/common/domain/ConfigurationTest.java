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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>ConfigurationTest</code> contains tests for the class
 * <code>{@link Configuration}</code>.
 */
public class ConfigurationTest {
	private Configuration fixture;

	/**
	 * Run the Configuration() constructor test.
	 */
	@Test
	public void testConfiguration_1() {
		Configuration result = new Configuration();
		assertNotNull(result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		Configuration obj = new Configuration();
		obj.setSource("source");
		obj.setId(new Long(1L));
		obj.setPropertyValue("value");
		obj.setPropertyName("key");
		obj.setUsername("user");
		obj.setHost("host");
		obj.setClusterId(new Long(1L));

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
		Configuration obj = new Configuration();
		obj.setClusterId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		Configuration obj = new Configuration();
		obj.setClusterId(new Long(1L));
		obj.setHost("host");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		Configuration obj = new Configuration();
		obj.setPropertyName("key");
		obj.setHost("host");
		obj.setClusterId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_7() {
		Configuration obj = new Configuration();
		obj.setPropertyName("key");
		obj.setHost("host");
		obj.setClusterId(new Long(1L));
		obj.setPropertyValue("value");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_8() {
		Configuration obj = new Configuration();
		obj.setSource("source");
		obj.setPropertyName("key");
		obj.setHost("host");
		obj.setClusterId(new Long(1L));
		obj.setPropertyValue("value");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_9() {
		Configuration obj = new Configuration();
		obj.setSource("source");
		obj.setPropertyName("key");
		obj.setHost("host");
		obj.setClusterId(new Long(1L));
		obj.setUsername("user");
		obj.setPropertyValue("value");

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_10() {
		Configuration obj = new Configuration();
		obj.setSource("source");
		obj.setPropertyName("key");
		obj.setHost("host");
		obj.setClusterId(new Long(1L));
		obj.setUsername("user");
		obj.setPropertyValue("value");

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_11() {
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
	 * Run the String getHost() method test.
	 */
	@Test
	public void testGetHost_1() {
		String result = fixture.getHost();

		assertEquals("host", result);
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
	 * Run the String getPropertyName() method test.
	 */
	@Test
	public void testGetPropertyName_1() {
		String result = fixture.getPropertyName();

		assertEquals("key", result);
	}

	/**
	 * Run the String getPropertyValue() method test.
	 */
	@Test
	public void testGetPropertyValue_1() {
		String result = fixture.getPropertyValue();

		assertEquals("value", result);
	}

	/**
	 * Run the String getSource() method test.
	 */
	@Test
	public void testGetSource_1() {
		String result = fixture.getSource();

		assertEquals("source", result);
	}

	/**
	 * Run the String getUsername() method test.
	 */
	@Test
	public void testGetUsername_1() {
		String result = fixture.getUsername();

		assertEquals("user", result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Configuration();
		fixture.setSource("source");
		fixture.setId(new Long(1L));
		fixture.setPropertyValue("value");
		fixture.setPropertyName("key");
		fixture.setUsername("user");
		fixture.setHost("host");
		fixture.setClusterId(new Long(1L));
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
