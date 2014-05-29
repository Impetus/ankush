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
package com.impetus.ankush.storm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.JavaConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.zookeeper.ZookeeperConf;

/**
 * The class <code>StormClusterConfTest</code> contains tests for the class
 * <code>{@link StormClusterConf}</code>.
 * 
 * @author hokam
 */
public class StormClusterConfTest {

	// field cluster conf.
	private StormClusterConf conf = null;
	private JavaConf jConf = new JavaConf();
	private StormConf sConf = new StormConf();
	private ZookeeperConf zConf = new ZookeeperConf();

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		conf = new StormClusterConf();
		Boolean install = true;
		String javaBundle = "/home/test/jdk.bin";
		String javaHomePath = "/opt/jdk";

		jConf.setInstall(install);
		jConf.setJavaBundle(javaBundle);
		jConf.setJavaHomePath(javaHomePath);

		sConf = new StormConf();
		ArrayList<NodeConf> nodes = new ArrayList<NodeConf>();
		nodes.add(new NodeConf("192.168.1.2", "192.168.1.2"));
		nodes.add(new NodeConf("192.168.1.3", "192.168.1.3"));
		ArrayList<String> zkNodes = new ArrayList<String>();
		zkNodes.add("192.168.1.1");
		sConf.setSupervisors(nodes);
		sConf.setZkNodes(zkNodes);
		sConf.setLocalDir("/home/test/storm/local_dir");
		sConf.setNimbus(new NodeConf("192.168.1.1", "192.168.1.1"));
		sConf.setUiPort(9090);
		sConf.setSlotsPorts(new ArrayList());

		ArrayList<NodeConf> znodes = new ArrayList<NodeConf>();
		znodes.add(sConf.getNimbus());
		zConf.setClientPort(1);
		zConf.setSyncLimit(1);
		zConf.setNodes(znodes);
		zConf.setDataDirectory("/tmp/data_dir");
		zConf.setTickTime(1);
		zConf.setInitLimit(1);
		zConf.setTickTime(1);

		conf.setJavaConf(jConf);
		conf.setStorm(sConf);
		conf.setZookeeper(zConf);
	}

	/**
	 * Run the StormClusterConf() constructor test.
	 * 
	 */
	@Test
	public void testStormClusterConf() throws Exception {
		assertNotNull(conf);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEquals_1() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper(zConf);
		fixture.setJavaConf(jConf);
		fixture.setStorm(sConf);
		StormClusterConf obj = new StormClusterConf();
		obj.setZookeeper(new ZookeeperConf());
		obj.setJavaConf(new JavaConf());
		obj.setStorm(new StormConf());

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testEquals_2() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper((ZookeeperConf)null);
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm(new StormConf());
		Object obj = null;

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testEquals_3() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm(new StormConf());
		Object obj = new Object();

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testEquals_4() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm(new StormConf());
		StormClusterConf obj = new StormClusterConf();
		obj.setStorm(new StormConf());

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testEquals_5() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper((ZookeeperConf)null);
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm((StormConf)null);
		StormClusterConf obj = new StormClusterConf();
		obj.setZookeeper(new ZookeeperConf());
		obj.setStorm(new StormConf());

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testEquals_6() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm(new StormConf());
		StormClusterConf obj = new StormClusterConf();
		obj.setZookeeper((ZookeeperConf)null);
		obj.setStorm((StormConf)null);

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEquals_8() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		boolean result = fixture.equals(fixture);
		assertEquals(true, result);
	}

	/**
	 * Run the Map<String, GenericConfiguration> getComponents() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testGetComponents_2() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper(zConf);
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm(sConf);

		Map<String, GenericConfiguration> result = fixture.getComponents();

		// add additional test code here
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey("Zookeeper"));
		assertTrue(result.containsKey("Storm"));
	}

	/**
	 * Run the Map<String, GenericConfiguration> getComponents() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testGetComponents_3() throws Exception {
		Map<String, GenericConfiguration> result = conf.getComponents();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey("Zookeeper"));
		assertTrue(result.containsKey("Storm"));
	}

	/**
	 * Run the JavaConf getJavaConf() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testGetJavaConf_1() throws Exception {
		JavaConf result = conf.getJavaConf();

		// add additional test code here
		assertNotNull(result);
		assertEquals(true, result.isInstall());
		assertEquals("/home/test/jdk.bin", result.getJavaBundle());
		assertEquals("/opt/jdk/", result.getJavaHomePath());
		assertEquals("/opt/jdk/bin/java", result.getJavaBinPath());
	}

	/**
	 * Run the List<NodeConf> getNodeConfs() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testGetNodeConfs() throws Exception {
		List<NodeConf> result = conf.getNodeConfs();

		// add additional test code here
		assertNotNull(result);
		assertEquals(3, result.size());
	}

	/**
	 * Run the int getNodeCount() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testGetNodeCount_1() throws Exception {
		int result = conf.getNodeCount();

		assertEquals(3, result);
	}

	/**
	 * Run the StormConf getStorm() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testGetStorm_1() throws Exception {
		StormConf result = conf.getStorm();

		assertNotNull(result);
	}

	/**
	 * Run the ZookeeperConf getZookeeper() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testGetZookeeper_1() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm(new StormConf());

		ZookeeperConf result = fixture.getZookeeper();

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the int hashCode() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testHashCode_1() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm((StormConf) null);

		int result = fixture.hashCode();

		assertNotNull(result);
	}

	/**
	 * Run the int hashCode() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testHashCode_2() throws Exception {
		StormClusterConf fixture = new StormClusterConf();
		fixture.setZookeeper((ZookeeperConf) null);
		fixture.setJavaConf(new JavaConf());
		fixture.setStorm(new StormConf());

		int result = fixture.hashCode();

		assertNotNull(result);
	}

	/**
	 * Run the String toString() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testToString_1() throws Exception {
		String result = conf.toString();

		assertNotNull(result);
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 * 
	 */
	@After
	public void tearDown() throws Exception {
		conf = null;
		zConf = null;
		sConf = null;
		jConf = null;
	}
}
