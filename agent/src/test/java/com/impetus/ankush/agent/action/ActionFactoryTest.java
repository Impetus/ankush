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
//package com.impetus.ankush.agent.action;
//
//import java.io.File;
//import java.util.Properties;
//
//import org.junit.*;
//
//import com.impetus.ankush.agent.AgentConf;
//
//import static org.junit.Assert.*;
//
///**
// * The class <code>ActionFactoryTest</code> contains tests for the class
// * <code>{@link ActionFactory}</code>.
// * 
// * @author hokam
// */
//public class ActionFactoryTest {
//
//	private AgentConf conf = null;
//
//	/**
//	 * Run the Actionable getInstanceById(String) method test.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testGetInstanceById_1() throws Exception {
//		String id = "log";
//
//		Actionable result = ActionFactory.getInstanceById(id);
//		assertNotNull(result);
//	}
//
//	/**
//	 * Run the Actionable getInstanceById(String) method test.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testGetInstanceById_2() throws Exception {
//		String id = "testsett";
//		Actionable result = ActionFactory.getInstanceById(id);
//		assertEquals(null, result);
//	}
//
//	/**
//	 * Run the Taskable getTaskableObject(String) method test.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testGetNullTaskableObject() throws Exception {
//		String className = "asdfasdfasd";
//
//		Taskable result = ActionFactory.getTaskableObject(className);
//
//		assertEquals(null, result);
//	}
//
//	/**
//	 * Run the Taskable getTaskableObject(String) method test.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	public void testGetServiceMonitorTaskable() throws Exception {
//		String className = "com.impetus.ankush.agent.service.ServiceMonitor";
//
//		Taskable result = ActionFactory.getTaskableObject(className);
//
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
//		Properties props = conf.load(file.getAbsolutePath());
//		conf.setProperties(props);
//		conf.save();
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
//		conf = null;
//	}
//}
