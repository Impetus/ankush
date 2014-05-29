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
package com.impetus.ankush.kafka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
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
 * The Class KafkaClusterConfTest.
 */
public class KafkaClusterConfTest {
	
	/** The conf. */
	private KafkaClusterConf conf = null;
	
	/** The z conf. */
	private ZookeeperConf zConf = new ZookeeperConf();
	
	/** The j conf. */
	private JavaConf jConf = new JavaConf();
	
	/** The k conf. */
	private KafkaConf kConf = new KafkaConf();
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		conf = new KafkaClusterConf();
		Boolean install = true;
		String javaBundle = "/home/test/jdk.bin";
		String javaHomePath = "/opt/jdk";
		HashMap<String,Object> zkNodesPort = new HashMap<String,Object>();

		jConf.setInstall(install);
		jConf.setJavaBundle(javaBundle);
		jConf.setJavaHomePath(javaHomePath);

		kConf = new KafkaConf();
		ArrayList<NodeConf> nodes = new ArrayList<NodeConf>();
		nodes.add(new NodeConf("192.168.1.2", "192.168.1.2"));
		nodes.add(new NodeConf("192.168.1.3", "192.168.1.3"));
		ArrayList<String> zkNodes = new ArrayList<String>();
		zkNodes.add("192.168.1.1");
		zkNodesPort.put("zkNodes", zkNodes);
		zkNodesPort.put("port","9092");
		kConf.setZkNodesPort(zkNodesPort);
		kConf.setLogDir("/home/test/Kafka/local_dir");
		kConf.setNodes(nodes);
		kConf.setPort(9092);

		ArrayList<NodeConf> znodes = new ArrayList<NodeConf>();
		znodes.add(kConf.getNodes().get(0));
		zConf.setClientPort(1);
		zConf.setSyncLimit(1);
		zConf.setNodes(znodes);
		zConf.setDataDirectory("/tmp/data_dir");
		zConf.setTickTime(1);
		zConf.setInitLimit(1);
		zConf.setTickTime(1);

		conf.setJavaConf(jConf);
		conf.setKafka(kConf);
		conf.setZookeeper(zConf);
	}

	/**
	 * Run the KafkaClusterConf() constructor test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testKafkaClusterConf() throws Exception {
		assertNotNull(conf);
	}
	
	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEquals_1() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper(zConf);
		fixture.setJavaConf(jConf);
		fixture.setKafka(kConf);
		KafkaClusterConf obj = new KafkaClusterConf();
		obj.setZookeeper(new ZookeeperConf());
		obj.setJavaConf(new JavaConf());
		obj.setKafka(new KafkaConf());

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEquals_2() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper((ZookeeperConf)null);
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka(new KafkaConf());
		Object obj = null;

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEquals_3() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka(new KafkaConf());
		Object obj = new Object();

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEquals_4() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka(new KafkaConf());
		KafkaClusterConf obj = new KafkaClusterConf();
		obj.setKafka(new KafkaConf());

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEquals_5() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper((ZookeeperConf)null);
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka((KafkaConf)null);
		KafkaClusterConf obj = new KafkaClusterConf();
		obj.setZookeeper(new ZookeeperConf());
		obj.setKafka(new KafkaConf());

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEquals_6() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka(new KafkaConf());
		KafkaClusterConf obj = new KafkaClusterConf();
		obj.setZookeeper((ZookeeperConf)null);
		obj.setKafka((KafkaConf)null);

		boolean result = fixture.equals(obj);

		// add additional test code here
		assertEquals(false, result);
	}
	
	/**
	 * Run the boolean equals(Object) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testEquals_8() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		boolean result = fixture.equals(fixture);
		assertEquals(true, result);
	}
	
	/**
	 * Run the Map<String, GenericConfiguration> getComponents() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetComponents_2() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper(zConf);
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka(kConf);

		Map<String, GenericConfiguration> result = fixture.getComponents();

		// add additional test code here
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey("Zookeeper"));
		assertTrue(result.containsKey("Kafka"));
	}
	
	/**
	 * Run the Map<String, GenericConfiguration> getComponents() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetComponents_3() throws Exception {
		Map<String, GenericConfiguration> result = conf.getComponents();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey("Zookeeper"));
		assertTrue(result.containsKey("Kafka"));
	}
	
	/**
	 * Run the JavaConf getJavaConf() method test.
	 *
	 * @throws Exception the exception
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
	 * @throws Exception the exception
	 */
	@Test
	public void testGetNodeConfs() throws Exception {
		List<NodeConf> result = conf.getNodeConfs();

		// add additional test code here
		assertNotNull(result);
		assertEquals(2, result.size());
	}
	
	/**
	 * Run the int getNodeCount() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetNodeCount_1() throws Exception {
		int result = conf.getNodeCount();

		assertEquals(2, result);
	}
	
	/**
	 * Run the KafkaConf getKafka() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetKafka_1() throws Exception {
		KafkaConf result = conf.getKafka();

		assertNotNull(result);
	}
	
	/**
	 * Run the ZookeeperConf getZookeeper() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetZookeeper_1() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka(new KafkaConf());

		ZookeeperConf result = fixture.getZookeeper();

		// add additional test code here
		assertNotNull(result);
	}
	
	/**
	 * Run the int hashCode() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHashCode_1() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper(new ZookeeperConf());
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka((KafkaConf) null);

		int result = fixture.hashCode();

		assertNotNull(result);
	}
	
	/**
	 * Run the int hashCode() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testHashCode_2() throws Exception {
		KafkaClusterConf fixture = new KafkaClusterConf();
		fixture.setZookeeper((ZookeeperConf) null);
		fixture.setJavaConf(new JavaConf());
		fixture.setKafka(new KafkaConf());

		int result = fixture.hashCode();

		assertNotNull(result);
	}
	
	/**
	 * Run the String toString() method test.
	 *
	 * @throws Exception the exception
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
		kConf = null;
		jConf = null;
	}


}
