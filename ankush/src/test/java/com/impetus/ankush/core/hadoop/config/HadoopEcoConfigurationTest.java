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

import org.junit.*;

import com.impetus.ankush.hadoop.config.HadoopEcoConfiguration;

import static org.junit.Assert.*;

/**
 * The Class HadoopEcoConfigurationTest.
 */
public class HadoopEcoConfigurationTest {

	/**
	 * Test hadoop eco configuration_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testHadoopEcoConfiguration_1() throws Exception {

		HadoopEcoConfiguration result = new HadoopEcoConfiguration();

		assertNotNull(result);
		assertEquals("", result.getHadoopVersion());
		assertEquals("", result.getHadoopHome());
		assertEquals("", result.getHadoopConf());
		assertEquals(null, result.getPassword());
		assertEquals(null, result.getPrivateKey());
		assertEquals(null, result.getOperationId());
		assertEquals(null, result.getUsername());
		assertEquals(null, result.getCurrentUser());
		assertEquals(null, result.getClusterConf());
		assertEquals(null, result.getClusterName());
		assertEquals(null, result.getClusterDbId());
		assertEquals(null, result.getComponentVendor());
		assertEquals(null, result.getComponentVersion());
		assertEquals(null, result.getInstallationPath());
		assertEquals(null, result.getLocalBinaryFile());
		assertEquals(null, result.getTarballUrl());
		assertEquals(null, result.getServerTarballLocation());
		assertEquals(null, result.getComponentHome());
		assertEquals(null, result.getCertification());
	}

	/**
	 * Test get hadoop conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetHadoopConf_1() throws Exception {
		HadoopEcoConfiguration fixture = getHadoopEcoConfiguration();

		String result = fixture.getHadoopConf();

		assertEquals("/home/hadoop/conf/", result);
	}

	/**
	 * Test get hadoop home_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetHadoopHome_1() throws Exception {
		HadoopEcoConfiguration fixture = getHadoopEcoConfiguration();

		String result = fixture.getHadoopHome();

		assertEquals("/home/hadoop/", result);
	}

	/**
	 * Test get hadoop version_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetHadoopVersion_1() throws Exception {
		HadoopEcoConfiguration fixture = getHadoopEcoConfiguration();

		String result = fixture.getHadoopVersion();

		assertEquals("1.0.4", result);
	}

	/**
	 * Test set hadoop conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetHadoopConf_1() throws Exception {
		HadoopEcoConfiguration fixture = getHadoopEcoConfiguration();
		String hadoopConf = "/home/hadoop/conf/";

		fixture.setHadoopConf(hadoopConf);
		assertEquals("/home/hadoop/conf/", fixture.getHadoopConf());
	}

	/**
	 * Test set hadoop home_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetHadoopHome_1() throws Exception {
		HadoopEcoConfiguration fixture = getHadoopEcoConfiguration();
		String hadoopHome = "/home/hadoop/";

		fixture.setHadoopHome(hadoopHome);
		assertEquals("/home/hadoop/", fixture.getHadoopHome());
	}

	/**
	 * Test set hadoop version_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetHadoopVersion_1() throws Exception {
		HadoopEcoConfiguration fixture = getHadoopEcoConfiguration();
		String hadoopVersion = "1.0.4";

		fixture.setHadoopVersion(hadoopVersion);
		assertEquals("1.0.4", fixture.getHadoopVersion());
	}

	/**
	 * @return
	 */
	private HadoopEcoConfiguration getHadoopEcoConfiguration() {
		HadoopEcoConfiguration fixture = new HadoopEcoConfiguration();
		fixture.setHadoopVersion("1.0.4");
		fixture.setHadoopConf("/home/hadoop/conf/");
		fixture.setHadoopHome("/home/hadoop/");
		return fixture;
	}
}
