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
package com.impetus.ankush.common.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>ComponentServiceTest</code> contains tests for the class
 * <code>{@link ComponentService}</code>.
 * 
 * @author hokam
 */
public class ComponentServiceTest {
	/**
	 * Run the ComponentService() constructor test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testComponentServiceAssignment() throws Exception {

		ComponentService result = new ComponentService();

		// add additional test code here
		assertNotNull(result);
		assertEquals(
				"ComponentService [name=null, type=null, role=null, params=null]",
				result.toString());
		assertEquals(null, result.getName());
		assertEquals(null, result.getType());
		assertEquals(null, result.getParams());
	}

	/**
	 * Run the ComponentService(String,String) constructor test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testComponentServiceAssignment1() throws Exception {
		String name = "NameNode";
		String type = "jps";

		ComponentService result = new ComponentService(name, type);

		// add additional test code here
		assertNotNull(result);
		assertEquals(
				"ComponentService [name=NameNode, type=jps, role=null, params=null]",
				result.toString());
		assertEquals("NameNode", result.getName());
		assertEquals("jps", result.getType());
		assertEquals(null, result.getParams());
	}

	/**
	 * Run the ComponentService(String,String,List<Parameter>) constructor test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testComponentServiceAssignment2() throws Exception {
		String name = "NameNode";
		String type = "jps";
		List<Parameter> params = new ArrayList();

		ComponentService result = new ComponentService(name, type, params);

		// add additional test code here
		assertNotNull(result);
		assertEquals(
				"ComponentService [name=NameNode, type=jps, role=null, params=[]]",
				result.toString());
		assertEquals("NameNode", result.getName());
		assertEquals("jps", result.getType());
		// add additional test code here
		assertEquals(0, result.getParams().size());
	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		// add additional set up code here
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
		// Add additional tear down code here
	}
}
