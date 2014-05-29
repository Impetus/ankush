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
package com.impetus.ankush.common.framework.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.impetus.ankush.cassandra.CassandraClusterConf;

/**
 * The Class ClusterConfTest.
 */
public class ClusterConfTest {

	/**
	 * @return
	 */
	private CassandraClusterConf getClusterConf() {
		CassandraClusterConf fixture = new CassandraClusterConf();
		fixture.errors = new HashMap();
		fixture.gangliaMaster = new NodeConf();
		fixture.technology = "cassandra";
		fixture.ipPattern = "192.168.0.0";
		fixture.clusterName = "name";
		fixture.patternFile = "pattern.txt";
		fixture.environment = "cloud";
		return fixture;
	}

	/**
	 * Test add error_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testAddError_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		String key = "error";
		String error = "some error string";

		fixture.addError(key, error);
		assertNotNull(fixture.getErrors());
	}

	/**
	 * Test get cluster name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetClusterName_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		String result = fixture.getClusterName();

		assertEquals("name", result);
	}

	/**
	 * Test get current user_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCurrentUser_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		String result = fixture.getCurrentUser();

		assertEquals(null, result);
	}

	/**
	 * Test get environment_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetEnvironment_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		String result = fixture.getEnvironment();

		assertEquals("cloud", result);
	}

	/**
	 * Test get errors_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetErrors_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		Map<String, String> result = fixture.getErrors();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Test get ganglia master_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetGangliaMaster_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		NodeConf result = fixture.getGangliaMaster();

		assertEquals(new NodeConf(), result);
	}

	/**
	 * Test get ip pattern_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetIpPattern_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		String result = fixture.getIpPattern();

		assertEquals("192.168.0.0", result);
	}

	/**
	 * Test get pattern file_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPatternFile_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		String result = fixture.getPatternFile();

		assertEquals("pattern.txt", result);
	}

	/**
	 * Test get technology_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetTechnology_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		String result = fixture.getTechnology();

		assertEquals("cassandra", result);
	}

	/**
	 * Test is auth type password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsAuthTypePassword_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();

		boolean result = fixture.isAuthTypePassword();

		assertEquals(false, result);
	}

	/**
	 * Test is auth type password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsAuthTypePassword_2() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		fixture.setPassword("password");

		boolean result = fixture.isAuthTypePassword();

		assertEquals(true, result);
	}

	/**
	 * Test is local_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsLocal_1() throws Exception {
		CassandraClusterConf fixture = new CassandraClusterConf();
		fixture.environment = "In Premise";

		boolean result = fixture.isLocal();

		assertEquals(true, result);
	}

	/**
	 * Test is state error_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsStateError_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		fixture.setState("error");
		boolean result = fixture.isStateError();

		assertTrue(result);
	}

	/**
	 * Test set cluster name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetClusterName_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		String clusterName = "name";

		fixture.setClusterName(clusterName);
		assertEquals("name", fixture.getClusterName());
	}

	/**
	 * Test set current user_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetCurrentUser_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		String currentUser = "user";

		fixture.setCurrentUser(currentUser);
		assertEquals(currentUser, fixture.getCurrentUser());
	}

	/**
	 * Test set environment_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetEnvironment_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		String environment = "Cloud";

		fixture.setEnvironment(environment);
		assertEquals(environment, fixture.getEnvironment());
	}

	/**
	 * Test set errors_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetErrors_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		HashMap<String, String> errors = new HashMap();
		errors.put("node", "nmap does't exists");
		fixture.setErrors(errors);
		assertEquals(1, fixture.getErrors().size());
	}

	/**
	 * Test set ganglia master_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetGangliaMaster_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		fixture.setGangliaMaster(new NodeConf());
		assertEquals(new NodeConf(), fixture.getGangliaMaster());
	}

	/**
	 * Test set ip pattern_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetIpPattern_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		String ipPattern = "192.168.0.0";

		fixture.setIpPattern(ipPattern);
		assertEquals(ipPattern, fixture.getIpPattern());
	}

	/**
	 * Test set pattern file_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetPatternFile_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		String patternFile = "pattern.txt";

		fixture.setPatternFile(patternFile);
		assertEquals(patternFile, fixture.getPatternFile());
	}

	/**
	 * Test set technology_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetTechnology_1() throws Exception {
		CassandraClusterConf fixture = getClusterConf();
		String technology = "cassandra";

		fixture.setTechnology(technology);
		assertEquals(technology, fixture.getTechnology());
	}

}
