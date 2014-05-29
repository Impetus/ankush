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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>AgentRestClientTest</code> contains tests for the class
 * <code>{@link AgentRestClient}</code>.
 * 
 * @author hokam
 */
public class AgentRestClientTest {
	/**
	 * An instance of the class being tested.
	 * 
	 * @see AgentRestClient
	 */
	private AgentRestClient fixture;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * 
	 * @see AgentRestClient
	 */
	public AgentRestClient getFixture() throws Exception {
		if (fixture == null) {
			fixture = new AgentRestClient();
		}
		return fixture;
	}

	/**
	 * Run the String sendData(Object,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSendData_fixture_1() throws Exception {
		AgentRestClient fixture2 = getFixture();
		Object object = new TileInfo();
		String urlPath = "https://localhost:8080/ankush";

		String result = fixture2.sendData(object, urlPath);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String sendData(Object,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSendData_fixture_2() throws Exception {
		AgentRestClient fixture2 = getFixture();
		Object object = new TileInfo();
		String urlPath = "http://google.com";

		String result = fixture2.sendData(object, urlPath);

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Run the String sendData(Object,String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSendData_fixture_3() throws Exception {
		AgentRestClient fixture2 = getFixture();
		Object object = null;
		String urlPath = "abcd";

		String result = fixture2.sendData(object, urlPath);

		assertEquals("", result);
	}

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
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Launch the test.
	 * 
	 * @param args
	 *            the command line arguments
	 * 
	 * @generatedBy CodePro at 26/6/13 7:54 PM
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(AgentRestClientTest.class);
	}
}
