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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>ServiceStatusProviderTest</code> contains tests for the class
 * <code>{@link ServiceStatusProvider}</code>.
 * 
 * @author hokam
 */
public class ServiceStatusProviderTest {

	ServiceStatusProvider fixture = null;

	/**
	 * Run the Map<Object, Object> getGangliaServiceStatus(List<String>) method
	 * test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetGangliaServiceStatus_1() throws Exception {
		List<String> services = new LinkedList();
		services.add("gmond");
		services.add("gmetad");

		Map<String, Boolean> result = fixture.getGangliaServiceStatus(services);

		// add additional test code here
		assertNotNull(result);
		assertEquals(2, result.size());
	}

	/**
	 * Run the Map<Object, Object> getGangliaServiceStatus(List<String>) method
	 * test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetGangliaServiceStatus_2() throws Exception {
		List<String> services = new LinkedList();
		services.add("lkeowadd");
		Map<String, Boolean> result = fixture.getGangliaServiceStatus(services);

		// add additional test code here
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		fixture = new ServiceStatusProvider();
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
		fixture = null;
	}
}
