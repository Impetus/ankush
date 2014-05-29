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
package com.impetus.ankush.cassandra;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.impetus.ankush.common.framework.config.JavaConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;

/**
 * The Class CassandraConfTest.
 */
public class CassandraConfTest {
	private CassandraConf cassandraConfObj;
	private JavaConf javaConf;
	private CassandraNodeConf cassandraNodeConf;
//	private List<CassandraNodeConf> nodes = null;
	private Set<CassandraNodeConf> nodes = null;
	List<CassandraNodeConf> newNodes = null;


	@Before
	public void setUp() {
		cassandraConfObj = new CassandraConf();
		CassandraNodeConf node1 = new  CassandraNodeConf();
		node1.setOs("Ubuntu");
		node1.setPrivateIp("192.168.145.54");
		node1.setPublicIp("192.168.145.54");
		node1.isSeedNode(true);
//		nodes = new ArrayList<CassandraNodeConf>();
		nodes = new HashSet<CassandraNodeConf>();
		nodes.add(node1);
		
		newNodes = new ArrayList<CassandraNodeConf>();
		newNodes.add(new CassandraNodeConf());
		newNodes.add(new CassandraNodeConf());
		
		String partitioner = "RandomPartitioner";
		String rpcPort = "9160";
		String storagePort = "7000";
		cassandraConfObj.setNodes(nodes);
		cassandraConfObj.setPartitioner(partitioner);
		cassandraConfObj.setRpcPort(rpcPort);
		cassandraConfObj.setStoragePort(storagePort);
	}

	@After
	public void tearDown() {
		// making cassandraConfObj null.
		cassandraConfObj = null;
	}

	@Test
	public void testCassandraConfTest() {
		assertNotNull(cassandraConfObj);
	}

	@Test
	public void testPositiveGetInstallationPath() {
		String installationPath = "/home/cassandra/";
		cassandraConfObj.setInstallationPath(installationPath);
		String val = cassandraConfObj.getInstallationPath();
		assertNotNull(val);
		assertSame(installationPath, cassandraConfObj.getInstallationPath());
	}

	@Test
	public void testNegativeGetInstallationPath() {
		String installationPath = "/home/cassandra";
		cassandraConfObj.setInstallationPath(installationPath);
		String val = cassandraConfObj.getInstallationPath();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getInstallationPath());
	}

	@Test
	public void testPositiveSetInstallationPath() {
		String cassandraInstallationPath = "/home/test1/cassandra/";
		cassandraConfObj.setInstallationPath(cassandraInstallationPath);
		assertSame("/home/test1/cassandra/",
				cassandraConfObj.getInstallationPath());
		assertNotNull(cassandraConfObj.getInstallationPath());
	}

	@Test
	public void testNegativeSetInstallationPath() {
		String cassandraInstallationPath = "/home/test1/cassandra";
		cassandraConfObj.setInstallationPath(cassandraInstallationPath);
		assertNotSame("/home/test/cassandra",
				cassandraConfObj.getInstallationPath());
		assertNotNull(cassandraConfObj.getInstallationPath());
	}

	@Test
	public void testPositiveGetDatacenterCount() {
		Integer datacenterCount = 1;
		cassandraConfObj.setDatacenterCount(datacenterCount);
		Integer val = cassandraConfObj.getDatacenterCount();
		assertNotNull(val);
		assertSame(datacenterCount, cassandraConfObj.getDatacenterCount());
	}

	@Test
	public void testNegativeGetDatacenterCount() {
		Integer datacenterCount = 1;
		cassandraConfObj.setDatacenterCount(datacenterCount);
		Integer val = cassandraConfObj.getDatacenterCount();
		assertNotNull(val);
		assertNotSame(2, cassandraConfObj.getDatacenterCount());
	}

	@Test
	public void testPositiveSetDatacenterCount() {
		Integer datacenterCount = 1;
		cassandraConfObj.setDatacenterCount(datacenterCount);
		assertSame(1, cassandraConfObj.getDatacenterCount());
		assertNotNull(cassandraConfObj.getDatacenterCount());
	}

	@Test
	public void testNegativeSetDatacenterCount() {
		Integer datacenterCount = 1;
		cassandraConfObj.setDatacenterCount(datacenterCount);
		assertNotSame(2, cassandraConfObj.getDatacenterCount());
		assertNotNull(cassandraConfObj.getDatacenterCount());
	}

	@Test
	public void testPositiveGetComponentHome() {
		String componentHome = "/home/cassandra/";
		cassandraConfObj.setComponentHome(componentHome);
		String val = cassandraConfObj.getComponentHome();
		assertNotNull(val);
		assertSame(componentHome, cassandraConfObj.getComponentHome());
	}

	@Test
	public void testNegativeGetComponentHome() {
		String componentHome = "testCassandra";
		cassandraConfObj.setComponentHome(componentHome);
		String val = cassandraConfObj.getComponentHome();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getComponentHome());
	}

	@Test
	public void testPositiveSetComponentHome() {
		String componentHome = "testCassandra/";
		cassandraConfObj.setComponentHome(componentHome);
		assertSame(componentHome, cassandraConfObj.getComponentHome());
		assertNotNull(cassandraConfObj.getComponentHome());
	}

	@Test
	public void testNegativeSetComponentHome() {
		String componentHome = "testCassandra";
		cassandraConfObj.setComponentHome(componentHome);
		assertNotSame("/home", cassandraConfObj.getComponentHome());
		assertNotNull(cassandraConfObj.getComponentHome());
	}
	
	@Test
	public void testPositiveGetComponentVersion() {
		String componentVersion = "/home/cassandra";
		cassandraConfObj.setComponentVersion(componentVersion);
		String val = cassandraConfObj.getComponentVersion();
		assertNotNull(val);
		assertSame(componentVersion, cassandraConfObj.getComponentVersion());
	}

	@Test
	public void testNegativeGetComponentVersion() {
		String componentVersion = "testCassandra";
		cassandraConfObj.setComponentVersion(componentVersion);
		String val = cassandraConfObj.getComponentVersion();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getComponentVersion());
	}

	@Test
	public void testPositiveSetComponentVersion() {
		String componentVersion = "testCassandra";
		cassandraConfObj.setComponentVersion(componentVersion);
		assertSame(componentVersion, cassandraConfObj.getComponentVersion());
		assertNotNull(cassandraConfObj.getComponentVersion());
	}

	@Test
	public void testNegativeSetComponentVersion() {
		String componentVersion = "testCassandra";
		cassandraConfObj.setComponentVersion(componentVersion);
		assertNotSame("/home", cassandraConfObj.getComponentVersion());
		assertNotNull(cassandraConfObj.getComponentVersion());
	}
	
	@Ignore
	@Test
	public void testPositiveGetNodes() throws Exception {
		CassandraNodeConf node2 = new  CassandraNodeConf();
		node2.setOs("Ubuntu");
		node2.setPrivateIp("192.168.145.54");
		node2.setPrivateIp("192.168.145.54");
		node2.isSeedNode(true);
		List<CassandraNodeConf> node = new ArrayList<CassandraNodeConf>();
		node.add(node2);
		assertSame(node, cassandraConfObj.getNodes());
	}
	
	@Test
	public void testNegativeGetNodes() throws Exception {
		CassandraNodeConf node3 = new  CassandraNodeConf();
		node3.setOs("");
		node3.setPrivateIp("");
		node3.setPrivateIp("");
		node3.isSeedNode(true);
		List<CassandraNodeConf> node = new ArrayList<CassandraNodeConf>();
		node.add(node3);
		assertNotSame(node, cassandraConfObj.getNodes());
	}
	
	@Test
	public void testPositiveGetServerTarballLocation() {
		String serverTarballLocation = "/home/cassandra";
		cassandraConfObj.setServerTarballLocation(serverTarballLocation);
		String val = cassandraConfObj.getServerTarballLocation();
		assertNotNull(val);
		assertSame(serverTarballLocation, cassandraConfObj.getServerTarballLocation());
	}

	@Test
	public void testNegativeGetServerTarballLocation() {
		String serverTarballLocation = "/home/cassandra";
		cassandraConfObj.setServerTarballLocation(serverTarballLocation);
		String val = cassandraConfObj.getServerTarballLocation();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getServerTarballLocation());
	}

	@Test
	public void testPositiveSetServerTarballLocation() {
		String serverTarballLocation = "/home/cassandra";
		cassandraConfObj.setServerTarballLocation(serverTarballLocation);
		assertSame(serverTarballLocation, cassandraConfObj.getServerTarballLocation());
		assertNotNull(cassandraConfObj.getServerTarballLocation());
	}

	@Test
	public void testNegativeSetServerTarballLocation() {
		String serverTarballLocation = "/home/cassandra";
		cassandraConfObj.setServerTarballLocation(serverTarballLocation);
		assertNotSame("/home", cassandraConfObj.getServerTarballLocation());
		assertNotNull(cassandraConfObj.getServerTarballLocation());
	}
	
	@Test
	public void testPositiveGetLocalBinaryFile() {
		String localBinaryFile = "/home/cassandra";
		cassandraConfObj.setLocalBinaryFile(localBinaryFile);
		String val = cassandraConfObj.getLocalBinaryFile();
		assertNotNull(val);
		assertSame(localBinaryFile, cassandraConfObj.getLocalBinaryFile());
	}

	@Test
	public void testNegativeGetLocalBinaryFile() {
		String localBinaryFile = "/home/cassandra";
		cassandraConfObj.setLocalBinaryFile(localBinaryFile);
		String val = cassandraConfObj.getLocalBinaryFile();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getLocalBinaryFile());
	}

	@Test
	public void testPositiveSetLocalBinaryFile() {
		String localBinaryFile = "/home/cassandra";
		cassandraConfObj.setLocalBinaryFile(localBinaryFile);
		assertSame(localBinaryFile, cassandraConfObj.getLocalBinaryFile());
		assertNotNull(cassandraConfObj.getLocalBinaryFile());
	}

	@Test
	public void testNegativeSetLocalBinaryFile() {
		String localBinaryFile = "/home/cassandra";
		cassandraConfObj.setLocalBinaryFile(localBinaryFile);
		assertNotSame("/home", cassandraConfObj.getLocalBinaryFile());
		assertNotNull(cassandraConfObj.getLocalBinaryFile());
	}
	
	@Test
	public void testPositiveGetTarballUrl() {
		String tarballUrl = "/home/cassandra";
		cassandraConfObj.setTarballUrl(tarballUrl);
		String val = cassandraConfObj.getTarballUrl();
		assertNotNull(val);
		assertSame(tarballUrl, cassandraConfObj.getTarballUrl());
	}

	@Test
	public void testNegativeGetTarballUrl() {
		String tarballUrl = "/home/cassandra";
		cassandraConfObj.setTarballUrl(tarballUrl);
		String val = cassandraConfObj.getTarballUrl();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getTarballUrl());
	}

	@Test
	public void testPositiveSetTarballUrl() {
		String tarballUrl = "/home/cassandra";
		cassandraConfObj.setTarballUrl(tarballUrl);
		assertSame(tarballUrl, cassandraConfObj.getTarballUrl());
		assertNotNull(cassandraConfObj.getTarballUrl());
	}

	@Test
	public void testNegativeSetTarballUrl() {
		String tarballUrl = "/home/cassandra";
		cassandraConfObj.setTarballUrl(tarballUrl);
		assertNotSame("/home", cassandraConfObj.getTarballUrl());
		assertNotNull(cassandraConfObj.getTarballUrl());
	}
	
	@Test
	public void testPositiveGetPartitioner() {
		String partitioner = "/home/cassandra";
		cassandraConfObj.setPartitioner(partitioner);
		String val = cassandraConfObj.getPartitioner();
		assertNotNull(val);
		assertSame(partitioner, cassandraConfObj.getPartitioner());
	}

	@Test
	public void testNegativeGetPartitioner() {
		String partitioner = "/home/cassandra";
		cassandraConfObj.setPartitioner(partitioner);
		String val = cassandraConfObj.getPartitioner();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getPartitioner());
	}

	@Test
	public void testPositiveSetPartitioner() {
		String partitioner = "/home/cassandra";
		cassandraConfObj.setPartitioner(partitioner);
		assertSame(partitioner, cassandraConfObj.getPartitioner());
		assertNotNull(cassandraConfObj.getPartitioner());
	}

	@Test
	public void testNegativeSetPartitioner() {
		String partitioner = "/home/cassandra";
		cassandraConfObj.setPartitioner(partitioner);
		assertNotSame("/home", cassandraConfObj.getPartitioner());
		assertNotNull(cassandraConfObj.getPartitioner());
	}
	
	@Test
	public void testPositiveGetRpcPort() {
		String rpcPort = "/home/cassandra";
		cassandraConfObj.setRpcPort(rpcPort);
		String val = cassandraConfObj.getRpcPort();
		assertNotNull(val);
		assertSame(rpcPort, cassandraConfObj.getRpcPort());
	}

	@Test
	public void testNegativeGetRpcPort() {
		String rpcPort = "/home/cassandra";
		cassandraConfObj.setRpcPort(rpcPort);
		String val = cassandraConfObj.getRpcPort();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getRpcPort());
	}

	@Test
	public void testPositiveSetRpcPort() {
		String rpcPort = "/home/cassandra";
		cassandraConfObj.setRpcPort(rpcPort);
		assertSame(rpcPort, cassandraConfObj.getRpcPort());
		assertNotNull(cassandraConfObj.getRpcPort());
	}

	@Test
	public void testNegativeSetRpcPort() {
		String rpcPort = "/home/cassandra";
		cassandraConfObj.setRpcPort(rpcPort);
		assertNotSame("/home", cassandraConfObj.getRpcPort());
		assertNotNull(cassandraConfObj.getRpcPort());
	}
	
	@Test
	public void testPositiveGetStoragePort() {
		String storagePort = "/home/cassandra";
		cassandraConfObj.setStoragePort(storagePort);
		String val = cassandraConfObj.getStoragePort();
		assertNotNull(val);
		assertSame(storagePort, cassandraConfObj.getStoragePort());
	}

	@Test
	public void testNegativeGetStoragePort() {
		String storagePort = "/home/cassandra";
		cassandraConfObj.setStoragePort(storagePort);
		String val = cassandraConfObj.getStoragePort();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getStoragePort());
	}

	@Test
	public void testPositiveSetStoragePort() {
		String storagePort = "/home/cassandra";
		cassandraConfObj.setStoragePort(storagePort);
		assertSame(storagePort, cassandraConfObj.getStoragePort());
		assertNotNull(cassandraConfObj.getStoragePort());
	}

	@Test
	public void testNegativeSetStoragePort() {
		String storagePort = "/home/cassandra";
		cassandraConfObj.setStoragePort(storagePort);
		assertNotSame("/home", cassandraConfObj.getStoragePort());
		assertNotNull(cassandraConfObj.getStoragePort());
	}
	
	@Test
	public void testPositiveGetVendor() {
		String vendor = "/home/cassandra";
		cassandraConfObj.setComponentVendor(vendor);
		String val = cassandraConfObj.getComponentVendor();
		assertNotNull(val);
		assertSame(vendor, cassandraConfObj.getComponentVendor());
	}

	@Test
	public void testNegativeGetVendor() {
		String vendor = "/home/cassandra";
		cassandraConfObj.setComponentVendor(vendor);
		String val = cassandraConfObj.getComponentVendor();
		assertNotNull(val);
		assertNotSame("/home", cassandraConfObj.getComponentVendor());
	}

	@Test
	public void testPositiveSetVendor() {
		String vendor = "/home/cassandra";
		cassandraConfObj.setComponentVendor(vendor);
		assertSame(vendor, cassandraConfObj.getComponentVendor());
		assertNotNull(cassandraConfObj.getComponentVendor());
	}

	@Test
	public void testNegativeSetVendor() {
		String vendor = "/home/cassandra";
		cassandraConfObj.setComponentVendor(vendor);
		assertNotSame("/home", cassandraConfObj.getComponentVendor());
		assertNotNull(cassandraConfObj.getComponentVendor());
	}
	
//	@Ignore
//	@Test
//	public void testPositiveGetConfigurationMap() {
//		Map<String, Object> yamlContents = new HashMap<String, Object>();
//		yamlContents.put("cassandra.partitioner", "RandomPartitioner");
//		yamlContents.put("cassandra.rpcPort", "9160");
//		yamlContents.put("cassandra.storagePort", "7000");
//		assertSame(yamlContents, cassandraConfObj.getConfigurationMap());
//	}
//
//	@Test
//	public void testNegativeGetConfigurationMap() {
//		Map<String, Object> yamlContents = new HashMap<String, Object>();
//		yamlContents.put("cassandra.partitioner", "");
//		yamlContents.put("cassandra.rpcPort", "");
//		yamlContents.put("cassandra.storagePort", "");
//		assertNotSame(yamlContents, cassandraConfObj.getConfigurationMap());
//	}

}
