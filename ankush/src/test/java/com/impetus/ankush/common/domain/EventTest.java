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

import com.impetus.ankush.common.domain.Event.Severity;

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
		obj.setType(Event.Type.SERVICE);
		obj.setValue("");
		obj.setSeverity(Event.Severity.NORMAL);
		obj.setName("");
		obj.setDate(new Date());
		obj.setClusterId(new Long(1L));
		obj.setCategory("");
		
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
		obj.setType(Event.Type.SERVICE);
		obj.setValue("value");
		obj.setSeverity(Event.Severity.NORMAL);
		obj.setName("name");
		obj.setDate(new Date(1372406912786L));
		obj.setClusterId(new Long(1L));		
		obj.setId(new Long(1L));
		obj.setCategory("subtype");

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
		String result = fixture.getValue();

		assertEquals("value", result);
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
		 Severity result = fixture.getSeverity();

		assertEquals(Event.Severity.NORMAL, result);
	}

	/**
	 * Run the String getSubType() method test.
	 */
	@Test
	public void testGetSubType_1() {
		String result = fixture.getCategory();

		assertEquals("subtype", result);
	}

	/**
	 * Run the String getSubject() method test.
	 */
	@Test
	public void testGetSubject_1() {
		fixture.setSeverity(Event.Severity.CRITICAL);

		String result = fixture.getSubject();

		assertEquals("Ankush CRITICAL : host name(subtype)", result);
	}

	/**
	 * Run the String getSubject() method test.
	 */
	@Test
	public void testGetSubject_2() {
		fixture.setSeverity(Event.Severity.NORMAL);

		String result = fixture.getSubject();

		assertEquals("name(subtype) At host is back to normal", result);
	}

	/**
	 * Run the String getType() method test.
	 */
	@Test
	public void testGetType_1() {
		com.impetus.ankush.common.domain.Event.Type result = fixture.getType();

		assertEquals(Event.Type.SERVICE, result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Event();
		fixture.setHost("host");
		fixture.setType(Event.Type.SERVICE);
		fixture.setValue("value");
		fixture.setSeverity(Event.Severity.NORMAL);
		fixture.setName("name");
		fixture.setDate(new Date(1372406912786L));
		fixture.setClusterId(new Long(1L));		
		fixture.setId(new Long(1L));
		fixture.setCategory("subtype");
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
