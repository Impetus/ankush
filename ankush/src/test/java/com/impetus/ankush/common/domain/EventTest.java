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

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>EventTest</code> contains tests for the class
 * <code>{@link Event}</code>.
 */
public class EventTest {
	private Event fixture;

	/**
	 * Run the Event() constructor test.
	 */
	@Test
	public void testEvent_1() {
		Event result = new Event();
		assertNotNull(result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		Event obj = new Event();
		obj.setHost("");
		obj.setType("");
		obj.setCurrentValue("");
		obj.setSeverity("");
		obj.setName("");
		obj.setDate(new Date());
		obj.setClusterId(new Long(1L));
		obj.setDescription("");
		obj.setId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
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
		Event obj = new Event();
		obj.setDate(fixture.getDate());

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		Event obj = new Event();
		obj.setDate(fixture.getDate());
		obj.setDescription("description");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		Event obj = new Event();
		obj.setHost("host");
		obj.setDate(fixture.getDate());
		obj.setDescription("description");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_7() {
		Event obj = new Event();
		obj.setHost("host");
		obj.setName("name");
		obj.setDate(fixture.getDate());
		obj.setDescription("description");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_8() {

		Event obj = new Event();
		obj.setHost("host");
		obj.setType("type");
		obj.setCurrentValue("value");
		obj.setSeverity("severity");
		obj.setName("name");
		obj.setDate(new Date(1372406912786L));
		obj.setClusterId(new Long(1L));
		obj.setDescription("description");
		obj.setId(new Long(1L));
		obj.setSubType("subtype");

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_9() {
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
	 * Run the String getCurrentValue() method test.
	 */
	@Test
	public void testGetCurrentValue_1() {
		String result = fixture.getCurrentValue();

		assertEquals("value", result);
	}

	/**
	 * Run the String getCurrentValue() method test.
	 */
	@Test
	public void testGetCurrentValue_2() {
		fixture.setCurrentValue("%");

		String result = fixture.getCurrentValue();

		assertEquals("%", result);
	}

	/**
	 * Run the String getCurrentValue() method test.
	 */
	@Test
	public void testGetCurrentValue_3() {
		fixture.setType("Usage");
		fixture.setCurrentValue("%");

		String result = fixture.getCurrentValue();

		assertEquals("%", result);
	}

	/**
	 * Run the String getCurrentValue() method test.
	 */
	@Test
	public void testGetCurrentValue_4() {
		fixture.setType("Usage");
		fixture.setCurrentValue("value");
		String result = fixture.getCurrentValue();

		assertEquals("value %", result);
	}

	/**
	 * Run the Date getDate() method test.
	 */
	@Test
	public void testGetDate_1() {
		Date result = fixture.getDate();

		assertNotNull(result);
		assertEquals(new Date(1372406912786L).toString(), result.toString());
	}

	/**
	 * Run the String getDescription() method test.
	 */
	@Test
	public void testGetDescription_1() {
		String result = fixture.getDescription();

		assertEquals("description", result);
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
	 * Run the String getName() method test.
	 */
	@Test
	public void testGetName_1() {
		String result = fixture.getName();

		assertEquals("name", result);
	}

	/**
	 * Run the String getSeverity() method test.
	 */
	@Test
	public void testGetSeverity_1() {
		String result = fixture.getSeverity();

		assertEquals("severity", result);
	}

	/**
	 * Run the String getSubType() method test.
	 */
	@Test
	public void testGetSubType_1() {
		String result = fixture.getSubType();

		assertEquals("subtype", result);
	}

	/**
	 * Run the String getSubject() method test.
	 */
	@Test
	public void testGetSubject_1() {
		fixture.setSeverity("Error");

		String result = fixture.getSubject();

		assertEquals("Ankush Error : host name", result);
	}

	/**
	 * Run the String getSubject() method test.
	 */
	@Test
	public void testGetSubject_2() {
		fixture.setSeverity("Normal");

		String result = fixture.getSubject();

		assertEquals("name At host is back to normal", result);
	}

	/**
	 * Run the String getType() method test.
	 */
	@Test
	public void testGetType_1() {
		String result = fixture.getType();

		assertEquals("type", result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Event();
		fixture.setHost("host");
		fixture.setType("type");
		fixture.setCurrentValue("value");
		fixture.setSeverity("severity");
		fixture.setName("name");
		fixture.setDate(new Date(1372406912786L));
		fixture.setClusterId(new Long(1L));
		fixture.setDescription("description");
		fixture.setId(new Long(1L));
		fixture.setSubType("subtype");
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
