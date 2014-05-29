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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>JPSServiceStatusProviderTest</code> contains tests for the
 * class <code>{@link JPSServiceStatusProvider}</code>.
 * 
 * @author hokam
 */
public class JPSServiceStatusProviderTest {
	/**
	 * An instance of the class being tested.
	 * 
	 * @see JPSServiceStatusProvider
	 */
	private JPSServiceStatusProvider fixture;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * 
	 * @see JPSServiceStatusProvider
	 */
	public JPSServiceStatusProvider getFixture() throws Exception {
		if (fixture == null) {
			fixture = new JPSServiceStatusProvider();
		}
		return fixture;
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_1() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		ArrayList<String> services = new ArrayList<String>();
		services.add("");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		// add additional test code here
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(Boolean.FALSE, result.get(""));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_2() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		ArrayList<String> services = new ArrayList<String>();
		services.add("");
		services.add("0123456789");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(Boolean.FALSE, result.get(""));
		assertEquals(Boolean.FALSE, result.get("0123456789"));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_3() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		ArrayList<String> services = new ArrayList<String>();
		services.add("0123456789");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(Boolean.FALSE, result.get("0123456789"));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_4() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		LinkedList<String> services = new LinkedList<String>();
		services.add("");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(Boolean.FALSE, result.get(""));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_5() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		LinkedList<String> services = new LinkedList<String>();
		services.add("");
		services.add("0123456789");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(Boolean.FALSE, result.get(""));
		assertEquals(Boolean.FALSE, result.get("0123456789"));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_6() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		LinkedList<String> services = new LinkedList<String>();
		services.add("0123456789");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(Boolean.FALSE, result.get("0123456789"));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_7() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		Vector<String> services = new Vector<String>();
		services.add("");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(Boolean.FALSE, result.get(""));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_8() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		Vector<String> services = new Vector<String>();
		services.add("");
		services.add("0123456789");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(Boolean.FALSE, result.get(""));
		assertEquals(Boolean.FALSE, result.get("0123456789"));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_9() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		Vector<String> services = new Vector<String>();
		services.add("0123456789");

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(Boolean.FALSE, result.get("0123456789"));
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_10() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		List<String> services = new ArrayList<String>();

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_11() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		List<String> services = new LinkedList<String>();

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Map<String, Boolean> getServiceStatus(List<String>) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetServiceStatus_fixture_12() throws Exception {
		JPSServiceStatusProvider fixture2 = getFixture();
		List<String> services = new Vector<String>();

		Map<String, Boolean> result = fixture2.getServiceStatus(services);

		assertNotNull(result);
		assertEquals(0, result.size());
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
}
