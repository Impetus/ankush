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
package com.impetus.ankush.core.hadoop.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.hadoop.config.Parameter;

/**
 * The class <code>ParameterTest</code> contains tests for the class
 * <code>{@link Parameter}</code>.
 * 
 * @author hokam
 */
public class ParameterTest {

	private Parameter parameter = null;

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		parameter = new Parameter();
		parameter.setName("name");
		parameter.setValue("value");
		parameter.setDefaultValue("defaultvalue");
		parameter.setFinal(false);
		parameter.setStatus("add");
		parameter.setDescription("property description");
	}

	/**
	 * Run the Parameter() constructor test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParameter() throws Exception {

		Parameter result = parameter;

		assertNotNull(result);
		assertEquals("name", result.getName());
		assertEquals("value", result.getValue());
		assertEquals("defaultvalue", result.getDefaultValue());
		assertEquals(Boolean.FALSE, result.isFinal());
		assertEquals("add", result.getStatus());
		assertEquals("property description", result.getDescription());
	}

	/**
	 * Run the Parameter(String,Object) constructor test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParameter_1() throws Exception {
		String name = "";
		String value = new String();

		Parameter result = new Parameter(name, value);

		assertNotNull(result);
		assertEquals("", result.getName());
		assertEquals(new String(), result.getValue());
		assertEquals(null, result.getDefaultValue());
		assertEquals(null, result.isFinal());
		assertEquals("none", result.getStatus());
		assertEquals(null, result.getDescription());
	}

	/**
	 * Run the Parameter(String,Object) constructor test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testParameter_2() throws Exception {
		String name = "";
		Object value = null;

		Parameter result = new Parameter(name, value);

		assertNotNull(result);
		assertEquals("", result.getName());
		assertEquals(null, result.getValue());
		assertEquals(null, result.getDefaultValue());
		assertEquals(null, result.isFinal());
		assertEquals("none", result.getStatus());
		assertEquals(null, result.getDescription());
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEquals() throws Exception {
		assertEquals(false, parameter.equals(null));
		assertEquals(false, parameter.equals(new Object()));
		assertEquals(true, parameter.equals(parameter));
		Parameter fixture = new Parameter("name", "value", "defaultvalue",
				"property description", new Boolean(true));
		assertEquals(true, parameter.equals(fixture));
	}

	/**
	 * Run the String getDefaultValue() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDefaultValue() throws Exception {
		String result = parameter.getDefaultValue();
		assertEquals("defaultvalue", result);
	}

	/**
	 * Run the String getDescription() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDescription() throws Exception {
		String result = parameter.getDescription();

		assertEquals("property description", result);
	}

	/**
	 * Run the String getName() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetName() throws Exception {
		String result = parameter.getName();

		assertEquals("name", result);
	}

	/**
	 * Run the String getStatus() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetStatus() throws Exception {
		String result = parameter.getStatus();

		assertEquals("add", result);
	}

	/**
	 * Run the String getValue() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetValue() throws Exception {
		String result = parameter.getValue();

		assertEquals("value", result);
	}

	/**
	 * Run the Boolean isFinal() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIsFinal_1() throws Exception {
		Boolean result = parameter.isFinal();

		assertNotNull(result);
		assertEquals("false", result.toString());
		assertEquals(false, result.booleanValue());
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
		parameter = null;
	}
}
