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
package com.impetus.ankush.common.zookeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class ZookeeperConfTest.
 */
public class ZookeeperConfTest {
	
	/**
	 * Test zookeeper conf_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testZookeeperConf_1()
		throws Exception {

		ZookeeperConf result = new ZookeeperConf();

		assertNotNull(result);
		assertEquals(null, result.getDataDirectory());
		assertEquals(0, result.getSyncLimit());
		assertEquals(0, result.getInitLimit());
		assertEquals(0, result.getClientPort());
		assertEquals(0, result.getTickTime());
		assertEquals("initLimit=0\nclientPort=0\ntickTime=0\nsyncLimit=0\ndataDir=null\n", result.getZooConfContents());
		assertEquals("", result.getHostsFileContents());
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
	 * Test set client port_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetClientPort_1()
		throws Exception {
		ZookeeperConf fixture = new ZookeeperConf();
		fixture.setClientPort(1);
		fixture.setSyncLimit(1);
		fixture.setNodes(new LinkedList());
		fixture.setDataDirectory("");
		fixture.setTickTime(1);
		fixture.setInitLimit(1);
		int clientPort = 1;

		fixture.setClientPort(clientPort);
		assertEquals(clientPort, fixture.getClientPort());
	}

	/**
	 * Test set data directory_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetDataDirectory_1()
		throws Exception {
		ZookeeperConf fixture = new ZookeeperConf();
		fixture.setClientPort(1);
		fixture.setSyncLimit(1);
		fixture.setNodes(new LinkedList());
		fixture.setDataDirectory("");
		fixture.setTickTime(1);
		fixture.setInitLimit(1);
		String dataDirectory = "/tmp";

		fixture.setDataDirectory(dataDirectory);
		assertEquals(dataDirectory, fixture.getDataDirectory());
		
	}

	/**
	 * Test set init limit_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetInitLimit_1()
		throws Exception {
		ZookeeperConf fixture = new ZookeeperConf();
		fixture.setClientPort(1);
		fixture.setSyncLimit(1);
		fixture.setNodes(new LinkedList());
		fixture.setDataDirectory("");
		fixture.setTickTime(1);
		fixture.setInitLimit(1);
		int initLimit = 1;

		fixture.setInitLimit(initLimit);
		assertEquals(initLimit, fixture.getInitLimit());
	}

	/**
	 * Test set nodes_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetNodes_1()
		throws Exception {
		ZookeeperConf fixture = new ZookeeperConf();
		fixture.setClientPort(1);
		fixture.setSyncLimit(1);
		fixture.setNodes(new LinkedList());
		fixture.setDataDirectory("");
		fixture.setTickTime(1);
		fixture.setInitLimit(1);
		List<NodeConf> nodeConfs = new LinkedList();
		nodeConfs.add(new NodeConf("192.168.1.1", "192.168.1.1"));
		fixture.setNodes(nodeConfs);
		
		assertEquals(nodeConfs, fixture.getNodes());
	}

	/**
	 * Test set sync limit_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetSyncLimit_1()
		throws Exception {
		ZookeeperConf fixture = new ZookeeperConf();
		fixture.setClientPort(1);
		fixture.setSyncLimit(1);
		fixture.setNodes(new LinkedList());
		fixture.setDataDirectory("");
		fixture.setTickTime(1);
		fixture.setInitLimit(1);
		int syncLimit = 1;

		fixture.setSyncLimit(syncLimit);
		assertEquals(syncLimit, fixture.getSyncLimit());
	}

	/**
	 * Test set tick time_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetTickTime_1()
		throws Exception {
		ZookeeperConf fixture = new ZookeeperConf();
		fixture.setClientPort(1);
		fixture.setSyncLimit(1);
		fixture.setNodes(new LinkedList());
		fixture.setDataDirectory("");
		fixture.setTickTime(1);
		fixture.setInitLimit(1);
		int tickTime = 1;

		fixture.setTickTime(tickTime);
		assertEquals(tickTime, fixture.getTickTime());
	}
}
