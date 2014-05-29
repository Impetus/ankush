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

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>XMLManipulatorTest</code> contains tests for the class
 * <code>{@link XMLManipulator}</code>.
 * 
 * @author hokam
 */
public class XMLManipulatorTest {

	private String filePath = null;
	private String props = null;

	private XMLManipulator xmlManipulator = new XMLManipulator();
	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		File file = new File("src/test/resources/ankush_constants.xml");
		filePath = file.getAbsolutePath();
		props = FileUtils.readFileToString(file);
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
		FileUtils.write(new File(filePath), props);
	}

	/**
	 * Run the boolean deleteConfValue(String,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteConfValue_1() throws Exception {
		String propertyName = "abcd";

		boolean result = xmlManipulator.deleteConfValue(filePath, propertyName);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean deleteConfValue(String,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteConfValue_2() throws Exception {
		String propertyName = "name1";

		boolean result = xmlManipulator.deleteConfValue(filePath, propertyName);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean deleteConfValue(String,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteConfValue_3() throws Exception {
		String propertyName = "";
		String file = "abcde.xml";
		boolean result = xmlManipulator.deleteConfValue(file, propertyName);
		FileUtils.deleteQuietly(new File(file));
		assertEquals(false, result);
	}

	/**
	 * Run the boolean editConfValue(String,String,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEditConfValue_1() throws Exception {
		String propertyName = "name2";
		String newPropertyValue = "value6";

		boolean result = xmlManipulator.editConfValue(filePath, propertyName,
				newPropertyValue);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean editConfValue(String,String,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEditConfValue_2() throws Exception {
		String propertyName = "0123456789";
		String newPropertyValue = "0123456789";

		boolean result = xmlManipulator.editConfValue(filePath, propertyName,
				newPropertyValue);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean editConfValue(String,String,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEditConfValue_3() throws Exception {
		String filePath = "test.xml";
		String propertyName = "test";
		String newPropertyValue = "0123456789";

		boolean result = xmlManipulator.editConfValue(filePath, propertyName,
				newPropertyValue);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the String readConfValue(String,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadConfValue_1() throws Exception {
		String propertyName = "test";
		String filePath = "test.xml";
		String result = xmlManipulator.readConfValue(filePath, propertyName);
		assertEquals(null, result);
	}

	/**
	 * Run the String readConfValue(String,String) method test.
	 * 
	 * @throws Exception
	 * 
	 * 
	 */
	@Test
	public void testReadConfValue_2() throws Exception {
		String propertyName = "0123456789";
		String result = xmlManipulator.readConfValue(filePath, propertyName);
		assertEquals(null, result);
	}

	/**
	 * Run the String readConfValue(String,String) method test.
	 * 
	 * @throws Exception
	 * 
	 * 
	 */
	@Test
	public void testReadConfValue_3() throws Exception {
		String propertyName = "distributed";
		String result = xmlManipulator.readConfValue(filePath, propertyName);
		assertEquals("true", result);
	}

	/**
	 * Run the boolean writeConfValue(String,String,String) method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testWriteConfValue_1() throws Exception {
		String propertyName = "a";
		String propertyValue = "b";
		String filePath = "abcdef.xml";

		boolean result = xmlManipulator.writeConfValue(filePath, propertyName,
				propertyValue);
		assertEquals(false, result);
	}

	/**
	 * Run the boolean writeConfValue(String,String,String) method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testWriteConfValue_2() throws Exception {
		String propertyName = "0123456789";
		String propertyValue = "0123456789";

		boolean result = xmlManipulator.writeConfValue(filePath, propertyName,
				propertyValue);

		assertEquals(true, result);
	}
}
