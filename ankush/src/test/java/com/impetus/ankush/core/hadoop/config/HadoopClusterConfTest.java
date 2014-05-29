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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.AuthConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.JavaConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;

/**
 * The class <code>HadoopClusterConfTest</code> contains tests for the class
 * <code>{@link HadoopClusterConf}</code>.
 * 
 * @author hokam
 */
public class HadoopClusterConfTest {

	private JavaConf jConf = null;
	private HadoopClusterConf conf = null;
	private List<HadoopNodeConf> nodes = null;
	List<HadoopNodeConf> newNodes = null;
	private AuthConf aConf = null;
	HadoopNodeConf namenode = new HadoopNodeConf();

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		conf = new HadoopClusterConf();
		Boolean install = true;
		String javaBundle = "/home/test/jdk.bin";
		String javaHomePath = "/opt/jdk";

		jConf = new JavaConf();
		jConf.setInstall(install);
		jConf.setJavaBundle(javaBundle);
		jConf.setJavaHomePath(javaHomePath);

		aConf = new AuthConf();
		aConf.setUsername("username");
		aConf.setPassword("password");
		aConf.setType("password");

		namenode.setDataNode(false);
		namenode.setNameNode(true);
		namenode.setSecondaryNameNode(false);
		HadoopNodeConf hNode2 = new HadoopNodeConf();
		hNode2.setDataNode(true);
		hNode2.setNameNode(false);
		hNode2.setSecondaryNameNode(false);
		HadoopNodeConf hNode3 = new HadoopNodeConf();
		hNode3.setDataNode(true);
		hNode3.setNameNode(false);
		hNode3.setSecondaryNameNode(false);
		HadoopNodeConf hNode4 = new HadoopNodeConf();
		hNode4.setDataNode(true);
		hNode4.setNameNode(false);
		hNode4.setSecondaryNameNode(true);

		nodes = new ArrayList<HadoopNodeConf>();
		nodes.add(namenode);
		nodes.add(hNode2);
		nodes.add(hNode3);
		nodes.add(hNode4);

		newNodes = new ArrayList<HadoopNodeConf>();
		newNodes.add(new HadoopNodeConf());
		newNodes.add(new HadoopNodeConf());

		conf.setClusterName("clusterName");
		conf.setTechnology("Hadoop");
		conf.setEnvironment(Constant.Cluster.Environment.IN_PREMISE);
		conf.setDescription("cluster description");
		conf.setNodes(nodes);
		conf.setNewNodes(newNodes);
		conf.setComponents(new LinkedHashMap());
		conf.setJavaConf(jConf);
		conf.setAuthConf(aConf);
	}

	/**
	 * Run the HadoopClusterConf() constructor test.
	 * 
	 */
	@Test
	public void testHadoopClusterConf() throws Exception {
		HadoopClusterConf result = new HadoopClusterConf();
		assertNotNull(result);
	}

	/**
	 * Run the AuthConf getAuthConf() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAuthConf() throws Exception {
		AuthConf result = conf.getAuthConf();

		assertNotNull(result);
		assertEquals("password", result.getType());
		assertEquals("password", result.getPassword());
		assertEquals("username", result.getUsername());
		assertEquals(true, result.isAuthTypePassword());
		result.setType("sharedKey");
		assertEquals(false, result.isAuthTypePassword());
		result.setType(null);
		assertEquals(false, result.isAuthTypePassword());
	}

	/**
	 * Run the JavaConf getJavaConf() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetJavaConf() throws Exception {
		JavaConf result = conf.getJavaConf();

		assertNotNull(result);
		assertEquals(true, result.isInstall());
		assertEquals("/home/test/jdk.bin", result.getJavaBundle());
		assertEquals("/opt/jdk/", result.getJavaHomePath());
		assertEquals("/opt/jdk/bin/java", result.getJavaBinPath());
	}

	/**
	 * Run the LinkedHashMap<String, GenericConfiguration> getComponents()
	 * method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetComponents() throws Exception {
		LinkedHashMap<String, GenericConfiguration> result = conf
				.getComponents();

		// add additional test code here
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the List<NodeConf> getDataNodes() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDataNodes() throws Exception {
		List<NodeConf> result = conf.getDataNodes();

		assertNotNull(result);
		assertEquals(3, result.size());
	}

	/**
	 * Run the String getDescription() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDescription() throws Exception {
		String result = conf.getDescription();

		assertEquals("cluster description", result);
	}

	/**
	 * Run the NodeConf getNameNode() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNameNode_1() throws Exception {
		NodeConf result = conf.getNameNode();

		assertNotNull(result);
		assertEquals(namenode, result);
	}

	/**
	 * Run the List<HadoopNodeConf> getNewnodes() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNewnodes() throws Exception {
		List<HadoopNodeConf> result = conf.getNewNodes();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(newNodes, result);
	}

	/**
	 * Run the List<NodeConf> getNodeConfs() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNodeConfs() throws Exception {
		List<NodeConf> result = conf.getNodeConfs();

		assertNotNull(result);
		assertEquals(4, result.size());
	}

	/**
	 * Run the int getNodeCount() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNodeCount_1() throws Exception {
		int result = conf.getNodeCount();
		assertEquals(4, result);
	}

	/**
	 * Run the int getNodeCount() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNodeCount_2() throws Exception {
		int result = conf.getNodeCount();

		assertEquals(4, result);
	}

	/**
	 * Run the int getNodeCount() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNodeCount_3() throws Exception {
		int result = conf.getNodeCount();

		assertEquals(4, result);
	}

	/**
	 * Run the int getNodeCount() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNodeCount_4() throws Exception {
		conf.setEnvironment(Constant.Cluster.Environment.IN_PREMISE);
		conf.setNodes(null);
		int result = conf.getNodeCount();

		assertEquals(0, result);
	}

	/**
	 * Run the List<HadoopNodeConf> getNodes() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNodes() throws Exception {
		List<HadoopNodeConf> result = conf.getNodes();

		assertNotNull(result);
		assertEquals(4, result.size());
		assertEquals(nodes, result);
	}

	/**
	 * Run the List<NodeConf> getSecondaryNameNodes() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSecondaryNameNodes() throws Exception {
		List<NodeConf> result = conf.getSecondaryNameNodes();

		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Run the boolean isStatusError() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIsStatusError() throws Exception {
		conf.setState(Constant.Cluster.State.ERROR);
		boolean result = conf.isStatusError();
		assertTrue(result);
		assertEquals(Boolean.TRUE, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEquals() throws Exception {
		HadoopClusterConf fixture = new HadoopClusterConf();
		fixture.setDescription("Cluster Description");
		fixture.setNodes(new ArrayList());
		fixture.setNewNodes(new ArrayList());
		AuthConf authConf = new AuthConf();
		authConf.setUsername("username");
		authConf.setPassword("password");
		fixture.setAuthConf(authConf);
		fixture.setJavaConf(new JavaConf());
		fixture.setComponents(new LinkedHashMap());
		HadoopClusterConf obj = new HadoopClusterConf();
		AuthConf authConf1 = new AuthConf();
		authConf1.setUsername("username");
		authConf1.setPassword("password");
		obj.setAuthConf(authConf1);

		assertEquals(true, fixture.equals(obj));
		assertEquals(false, conf.equals(new Object()));
		assertEquals(true, conf.equals(conf));
		assertEquals(false, conf.equals(fixture));
		assertEquals(false, fixture.equals(conf));
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
		// Add additional tear down code here
	}
}
