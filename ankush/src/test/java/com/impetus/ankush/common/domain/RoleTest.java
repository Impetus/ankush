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
 * The class <code>RoleTest</code> contains tests for the class
 * <code>{@link Role}</code>.
 */
public class RoleTest {
	private Role fixture;

	/**
	 * Run the Role() constructor test.
	 */
	@Test
	public void testRole_1() {

		Role result = new Role();

		assertNotNull(result);
		assertEquals("<null>", result.toString());
		assertEquals(null, result.getName());
		assertEquals(null, result.getId());
		assertEquals(null, result.getAuthority());
		assertEquals(null, result.getDescription());
	}

	/**
	 * Run the Role(String) constructor test.
	 */
	@Test
	public void testRole_2() {
		String name = "role";

		Role result = new Role(name);

		assertNotNull(result);
		assertEquals("role", result.toString());
		assertEquals("role", result.getName());
		assertEquals(null, result.getId());
		assertEquals("role", result.getAuthority());
		assertEquals(null, result.getDescription());
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		Role o = new Role("name");
		o.setDescription("");
		o.setId(new Long(1L));

		boolean result = fixture.equals(o);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_2() {
		Object o = new Object();

		boolean result = fixture.equals(o);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_3() {
		Object o = new Role("name");

		boolean result = fixture.equals(o);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_4() {
		boolean result = fixture.equals(fixture);

		assertEquals(true, result);
	}

	/**
	 * Run the String getAuthority() method test.
	 */
	@Test
	public void testGetAuthority_1() {
		String result = fixture.getAuthority();

		assertEquals("name", result);
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
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Role("name");
		fixture.setDescription("description");
		fixture.setId(new Long(1L));
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
