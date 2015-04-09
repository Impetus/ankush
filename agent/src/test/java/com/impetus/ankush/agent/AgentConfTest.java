///*******************************************************************************
// * ===========================================================
// * Ankush : Big Data Cluster Management Solution
// * ===========================================================
// * 
// * (C) Copyright 2014, by Impetus Technologies
// * 
// * This is free software; you can redistribute it and/or modify it under
// * the terms of the GNU Lesser General Public License (LGPL v3) as
// * published by the Free Software Foundation;
// * 
// * This software is distributed in the hope that it will be useful, but
// * WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// * See the GNU Lesser General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public License 
// * along with this software; if not, write to the Free Software Foundation, 
// * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
// ******************************************************************************/
//package com.impetus.ankush.agent;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * The class <code>AgentConfTest</code> contains tests for the class
// * <code>{@link AgentConf}</code>.
// * 
// * @author hokam
// */
//public class AgentConfTest {
//
//	private AgentConf conf = null;
//	private Properties props = null;
//
//	/**
//	 * Run the AgentConf() constructor test.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testAgentConf_1() throws Exception {
//		AgentConf result = new AgentConf();
//		assertNotNull(result);
//	}
//
//	/**
//	 * Perform pre-test initialization.
//	 * 
//	 * @throws Exception
//	 *             if the initialization fails for some reason
//	 */
//	@Before
//	public void setUp() throws Exception {
//		conf = new AgentConf();
//		File file = new File("src/test/resources/agent.properties");
//		props = conf.load(file.getAbsolutePath());
//		conf.setProperties(props);
//	}
//
//	/**
//	 * test for get log upload file url.
//	 */
//	@Test
//	public void testGetLogUploadFileUrl() {
//		String url = conf.getLogUploadFileUrl();
//
//		assertEquals("http://localhost:8080/ankush/uploadFile?category=log",
//				url);
//	}
//
//	/**
//	 * test for get hadoop jars path.
//	 */
//	@Test
//	public void testGetHadoopJarsPath() {
//		String path = conf.getHadoopJarsPath();
//
//		assertEquals(".ankush/agent/jars/compdep", path);
//	}
//
//	/**
//	 * test for get hadoop jars path.
//	 */
////	@Test
////	public void testGetPath() {
////		List<String> jars = conf.getJarPath();
////
////		assertNotNull(jars);
////		assertEquals(".ankush/agent/jars", jars.get(0));
////	}
//
//	/**
//	 * Test for gets the properties.
//	 */
//	@Test
//	public void testGetProperties() {
//		Properties props = conf.getProperties();
//
//		assertNotNull(props);
//		assertEquals(this.props, props);
//	}
//
//	/**
//	 * Test for gets the node info send url.
//	 */
//	@Test
//	public void testGetNodeInfoSendUrl() {
//		String url = conf.getURL(Constant.PROP_NAME_URL_COMMON);
//		assertNotNull(url);
//		assertEquals("http://localhost:8080/ankush/monitor/node/1/info", url);
//	}
//
//	/**
//	 * Test for gets the node info send url.
//	 */
//	@Test
//	public void testGetServiceSendUrl() {
//		String url = conf.getURL(Constant.PROP_NAME_SERVICE_URL_LAST);
//		assertNotNull(url);
//		assertEquals("http://localhost:8080/ankush/monitor/node/1/status", url);
//	}
//
//	/**
//	 * Test for gets the node info send url.
//	 */
//	@Test
//	public void testGetJobInfoSendUrl() {
//		String url = conf.getURL(Constant.PROP_NAME_JOB_URL_STATUS);
//		assertNotNull(url);
//		assertEquals("http://localhost:8080/ankush/monitor/node/1/job", url);
//	}
//
//	/**
//	 * Test Method getTopologyUrl.
//	 */
//	@Test
//	public void getTopologyUrl() {
//		String url = conf.getURL(Constant.PROP_NAME_MONITORING_URL);
//		assertNotNull(url);
//		assertEquals("http://localhost:8080/ankush/monitor/node/1/monitoring",
//				url);
//	}
//
//	/**
//	 * Test get node info top process count.
//	 */
//	@Test
//	public void testGetNodeInfoTopProcessCount() {
//		int count = 5;
//		assertEquals(conf.getIntValue(Constant.PROP_NAME_TOP_PROCESS_COUNT),
//				count);
//	}
//
//	/**
//	 * Test get node info send time.
//	 */
//	@Test
//	public void testGetNodeInfoSendTime() {
//		int time = 30;
//		assertEquals(conf.getIntValue(Constant.PROP_NAME_COMMON_UPDATE_TIME),
//				time);
//	}
//
//	/**
//	 * Test to Gets the host name.
//	 */
//	@Test
//	public void getHostName() {
//		String hostname = conf.getStringValue(Constant.PROP_HOSTNAME);
//		assertNotNull(hostname);
//		assertEquals("localhost", hostname);
//	}
//
//	/**
//	 * Test property values.
//	 */
//	@Test
//	public void testGetPropertyValues() {
//		assertEquals(conf.getProperties().getProperty("NODE_ID"), "1");
//		assertEquals(conf.getProperties().getProperty("URL_PART"),
//				"/ankush/monitor/node/");
//		assertEquals(conf.getProperties().getProperty("HOST"), "localhost");
//		assertEquals(conf.getProperties().getProperty("PORT"), "8080");
//		assertEquals(conf.getProperties().getProperty("URL_MEMORY"), "memory");
//	}
//
//	/**
//	 * Test add property.
//	 * 
//	 */
//	@Test
//	public void testAddProperty() throws Exception {
//		conf.getProperties().setProperty("TEST", "DONE");
//		conf.save();
//		assertEquals(conf.getProperties().getProperty("TEST"), "DONE");
//	}
//
//	/**
//	 * Test remove property.
//	 */
//	@Test
//	public void testRemoveProperty() throws Exception {
//		conf.getProperties().remove("TEST");
//		conf.save();
//		assertNull(conf.getProperties().getProperty("TEST"));
//	}
//
//	/**
//	 * Test to get Process list.
//	 */
//	@Test
//	public void testGetJPSProcessList() {
//		List<String> processs = new ArrayList<String>();
//		processs.add("jps");
//		processs.add("abcd");
//		assertEquals(processs, conf.getJPSServices());
//	}
//
//	/**
//	 * Test to get Ganglia process list.
//	 */
//	@Test
//	public void testGetGangliaProcessList() {
//		List<String> processs = new ArrayList<String>();
//		processs.add("gmond");
//		processs.add("gmetad");
//		assertEquals(processs, conf.getGangliaServices());
//	}
//
//	/**
//	 * Perform post-test clean-up.
//	 * 
//	 * @throws Exception
//	 *             if the clean-up fails for some reason
//	 */
//	@After
//	public void tearDown() throws Exception {
//	}
//}
