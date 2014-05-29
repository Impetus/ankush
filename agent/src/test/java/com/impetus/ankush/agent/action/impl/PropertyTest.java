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
package com.impetus.ankush.agent.action.impl;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class PropertyTest.
 */
public class PropertyTest {

	/**
	 * Test get description_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetDescription_1() throws Exception {
		Property fixture = new Property();
		fixture.setDescription("");
		fixture.setName("");
		fixture.setValue("");

		String result = fixture.getDescription();

		assertEquals("", result);
	}

	/**
	 * Test get name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetName_1() throws Exception {
		Property fixture = new Property();
		fixture.setDescription("");
		fixture.setName("");
		fixture.setValue("");

		String result = fixture.getName();

		assertEquals("", result);
	}

	/**
	 * Test get value_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetValue_1() throws Exception {
		Property fixture = new Property();
		fixture.setDescription("");
		fixture.setName("");
		fixture.setValue("");

		String result = fixture.getValue();

		assertEquals("", result);
	}

	/**
	 * Test set description_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetDescription_1() throws Exception {
		Property fixture = new Property();
		fixture.setDescription("");
		fixture.setName("");
		fixture.setValue("");
		String value = "";

		fixture.setDescription(value);

	}

	/**
	 * Test set name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetName_1() throws Exception {
		Property fixture = new Property();
		fixture.setDescription("");
		fixture.setName("");
		fixture.setValue("");
		String value = "";

		fixture.setName(value);

	}

	/**
	 * Test set value_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetValue_1() throws Exception {
		Property fixture = new Property();
		fixture.setDescription("");
		fixture.setName("");
		fixture.setValue("");
		String value = "";

		fixture.setValue(value);

	}

}
