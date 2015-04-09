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
	public void testGetServices() throws Exception {
		// service conf object.
		ServiceConfiguration serviceConf = new ServiceConfiguration();

		// services object
		List<ComponentService> services = new ArrayList<ComponentService>();
		// adding Test service
		services.add(new ComponentService("Test", "test"));
		// setting services
		serviceConf.setServices(services);

		// result services.
		List<ComponentService> resultServices = serviceConf.getServices();

		// not null assertion.
		assertNotNull(resultServices);
		// service list size assertion.
		assertEquals(1, resultServices.size());
		// first service name assertion.
		assertEquals("Test", resultServices.get(0).getName());
		// first service type assertion.
		assertEquals("test", resultServices.get(0).getType());
	}

	/**
	 * Run the List<ComponentService> getServices() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceParameters() throws Exception {
		// service configuration object.
		ServiceConfiguration serviceConf = new ServiceConfiguration();
		// service list
		List<ComponentService> services = new ArrayList<ComponentService>();

		// parameter list.
		List<Parameter> params = new ArrayList<Parameter>();

		// adding a parameter
		params.add(new Parameter("port", "8080"));

		// creating a parameter
		Parameter param = new Parameter();
		param.setName("processName");
		param.setValue("test");

		// adding a process name parameter.
		params.add(param);

		// creating component service using setters
		ComponentService cs = new ComponentService();
		cs.setName("TestServiceName");
		cs.setType("TestType");
		cs.setParams(params);

		// adding component service via two args constructor
		services.add(new ComponentService("Test", "test"));
		// adding component service via three args constructor.
		services.add(new ComponentService("Test", "test", params));
		// addign componnet service object.
		services.add(cs);

		// setting services in service conf
		serviceConf.setServices(services);

		// result services.
		List<ComponentService> resultServices = serviceConf.getServices();

		// not null assertion.
		assertNotNull(resultServices);
		// service list size assertion.
		assertEquals(3, resultServices.size());
		// first service name assertion.
		assertEquals("Test", resultServices.get(0).getName());
		// first service type assertion.
		assertEquals("test", resultServices.get(0).getType());
		// second service parameters assertion.
		assertEquals(params, resultServices.get(1).getParams());
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
