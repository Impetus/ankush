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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.*;
import static org.junit.Assert.*;
import com.impetus.ankush.cassandra.CassandraClusterConf;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;

/**
 * The Class GenericConfigurationTest.
 */
public class GenericConfigurationTest {

	/**
	 * Test generic configuration_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGenericConfiguration_1() throws Exception {

		GenericConfiguration result = new GenericConfiguration();

		assertNotNull(result);
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
	 * Test get advanced conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetAdvancedConf_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		Map<String, Object> result = fixture.getAdvancedConf();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * @return
	 */
	private GenericConfiguration getGenericConfiguration() {
		
		ClusterConf clusterConf = new HadoopClusterConf();
		clusterConf.setPassword("password");
		clusterConf.setUsername("username");
		clusterConf.setCurrentUser("username");
		clusterConf.setClusterId(new Long(1L));
		clusterConf.setClusterName("cluster");
		clusterConf.setOperationId(new Long(1L));
		clusterConf.setPrivateKey("privateKey");
		
		GenericConfiguration fixture = new GenericConfiguration();
		fixture.setClusterDbId(new Long(1L));
		fixture.setClusterName("cluster");
		fixture.setUsername("username");
		fixture.setOperationId(new Long(1L));
		fixture.setCurrentUser("username");
		fixture.setAdvancedConf(new HashMap());
		fixture.setPrivateKey("privateKey");
		fixture.setTarballUrl("http://downloadurl.com/");
		fixture.setComponentVersion("1.0");
		fixture.setComponentHome("/home/");
		fixture.setServerTarballLocation("/home/repo/");
		fixture.setPassword("password");
		fixture.setComponentVendor("apache");
		fixture.setClusterConf(clusterConf);
		fixture.setLocalBinaryFile("/home/local/");
		fixture.setCertification("xyz");
	
		return fixture;
	}

	/**
	 * Test get certification_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCertification_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getCertification();

		assertEquals("xyz", result);
	}

	/**
	 * Test get cluster db id_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetClusterDbId_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		Long result = fixture.getClusterDbId();

		assertNotNull(result);
		assertEquals("1", result.toString());
	}

	/**
	 * Test get cluster name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetClusterName_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getClusterName();

		assertEquals("cluster", result);
	}

	/**
	 * Test get comp nodes_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCompNodes_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		Set<NodeConf> result = fixture.getCompNodes();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Test get component home_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetComponentHome_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getComponentHome();

		assertEquals("/home/", result);
	}

	/**
	 * Test get component vendor_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetComponentVendor_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getComponentVendor();

		assertEquals("apache", result);
	}

	/**
	 * Test get component version_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetComponentVersion_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getComponentVersion();

		assertEquals("1.0", result);
	}

	/**
	 * Test get current user_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCurrentUser_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getCurrentUser();

		assertEquals("username", result);
	}

	/**
	 * Test get installation path_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetInstallationPath_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getInstallationPath();

		assertEquals(null, result);
	}

	/**
	 * Test get local binary file_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetLocalBinaryFile_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getLocalBinaryFile();

		assertEquals("/home/local/", result);
	}

	/**
	 * Test get operation id_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetOperationId_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		Long result = fixture.getOperationId();

		assertNotNull(result);
		assertEquals("1", result.toString());
	}

	/**
	 * Test get password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPassword_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getPassword();

		assertEquals("password", result);
	}

	/**
	 * Test get private key_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPrivateKey_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getPrivateKey();

		assertEquals("privateKey", result);
	}

	/**
	 * Test get server tarball location_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetServerTarballLocation_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getServerTarballLocation();

		assertEquals("/home/repo/", result);
	}

	/**
	 * Test get tarball url_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetTarballUrl_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getTarballUrl();

		assertEquals("http://downloadurl.com/", result);
	}

	/**
	 * Test get username_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetUsername_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();

		String result = fixture.getUsername();

		assertEquals("username", result);
	}

	/**
	 * Test set advanced conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetAdvancedConf_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		Map<String, Object> advancedConf = new HashMap();
		advancedConf.put("property", "value");
		fixture.setAdvancedConf(advancedConf);
		assertEquals(1, fixture.getAdvancedConf().size());
	}

	/**
	 * Test set certification_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetCertification_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String certification = "xyz";

		fixture.setCertification(certification);
		assertEquals(certification, fixture.getCertification());
	}

	/**
	 * Test set cluster conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetClusterConf_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		ClusterConf clusterConf = new CassandraClusterConf();

		fixture.setClusterConf(clusterConf);
		assertNotNull(fixture.getClusterConf());
	}

	/**
	 * Test set cluster db id_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetClusterDbId_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		Long clusterDbId = new Long(1L);

		fixture.setClusterDbId(clusterDbId);
		assertEquals(clusterDbId, fixture.getClusterDbId());
	}

	/**
	 * Test set cluster name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetClusterName_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String clusterName = "cluster";

		fixture.setClusterName(clusterName);
		assertEquals(clusterName, fixture.getClusterName());
	}

	/**
	 * Test set component home_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetComponentHome_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String componentHome = "/home/";

		fixture.setComponentHome(componentHome);
		assertEquals(componentHome, fixture.getComponentHome());
	}

	/**
	 * Test set component vendor_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetComponentVendor_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String componentVendor = "twitter";

		fixture.setComponentVendor(componentVendor);

		assertEquals(componentVendor, fixture.getComponentVendor());
	}

	/**
	 * Test set component version_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetComponentVersion_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String componentVersion = "1.0";

		fixture.setComponentVersion(componentVersion);
		assertEquals(componentVersion, fixture.getComponentVersion());
	}

	/**
	 * Test set current user_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetCurrentUser_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String currentUser = "username";

		fixture.setCurrentUser(currentUser);
		assertEquals(currentUser, fixture.getCurrentUser());
	}

	/**
	 * Test set installation path_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetInstallationPath_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String installationPath = "/home/components/";

		fixture.setInstallationPath(installationPath);
		assertEquals(installationPath, fixture.getInstallationPath());
	}

	/**
	 * Test set local binary file_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetLocalBinaryFile_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String localBinaryFile = "/home/local/";

		fixture.setLocalBinaryFile(localBinaryFile);
		assertEquals(localBinaryFile, fixture.getLocalBinaryFile());
	}

	/**
	 * Test set operation id_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetOperationId_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		Long operationId = new Long(1L);

		fixture.setOperationId(operationId);
		assertEquals(operationId, fixture.getOperationId());
	}

	/**
	 * Test set password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetPassword_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String password = "password";

		fixture.setPassword(password);
		assertEquals(password, fixture.getPassword());
	}

	/**
	 * Test set private key_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetPrivateKey_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String privateKey = "privateKey";

		fixture.setPrivateKey(privateKey);
		assertEquals(privateKey, fixture.getPrivateKey());
	}

	/**
	 * Test set server tarball location_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetServerTarballLocation_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String serverTarballLocation = "/home/repo/";

		fixture.setServerTarballLocation(serverTarballLocation);
		assertEquals(serverTarballLocation, fixture.getServerTarballLocation());
	}

	/**
	 * Test set tarball url_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetTarballUrl_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String tarballUrl = "http://download.com/";

		fixture.setTarballUrl(tarballUrl);
		assertEquals(tarballUrl, fixture.getTarballUrl());
	}

	/**
	 * Test set username_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetUsername_1() throws Exception {
		GenericConfiguration fixture = getGenericConfiguration();
		String username = "username";

		fixture.setUsername(username);
		assertEquals(username, fixture.getUsername());
	}

}
