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
package com.impetus.ankush.agent.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>ServiceConfigurationTest</code> contains tests for the class
 * <code>{@link ServiceConfiguration}</code>.
 * 
 * @author hokam
 */
public class ServiceConfigurationTest {

	ServiceConfiguration fixture = new ServiceConfiguration();

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Run the List<ComponentService> getServices() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testServices() throws Exception {
		ServiceConfiguration fixture = new ServiceConfiguration();
		List<ComponentService> services = new ArrayList<ComponentService>();
		services.add(new ComponentService("Test", "test"));
		fixture.setServices(services);

		List<ComponentService> result = fixture.getServices();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Test", result.get(0).getName());
		assertEquals("test", result.get(0).getType());
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
	}
}
