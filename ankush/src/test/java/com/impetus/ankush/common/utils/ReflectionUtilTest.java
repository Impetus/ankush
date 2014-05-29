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
package com.impetus.ankush.common.utils;

import org.junit.*;
import static org.junit.Assert.*;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.Clusterable;
import com.impetus.ankush.common.framework.Deployable;

/**
 * The Class ReflectionUtilTest.
 */
public class ReflectionUtilTest {

	/**
	 * Test reflection util_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testReflectionUtil_1() throws Exception {
		ReflectionUtil result = new ReflectionUtil();
		assertNotNull(result);
	}

	/**
	 * Test get clusterable object_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetClusterableObject_1() throws Exception {
		String className = "com.impetus.ankush.hadoop.HadoopCluster";

		Clusterable result = ReflectionUtil.getClusterableObject(className);

		assertNotNull(result);
	}

	/**
	 * Test get deployable object_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetDeployableObject_1() throws Exception {
		String className = "com.impetus.ankush.hadoop.ecosystem.pig.ApachePig9Deployer";

		Deployable result = ReflectionUtil.getDeployableObject(className);

		assertNotNull(result);
	}

	/**
	 * Test get monitorable object_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetMonitorableObject_1() throws Exception {
		String className = "com.impetus.ankush.hadoop.HadoopClusterMonitor";

		AbstractMonitor result = ReflectionUtil.getMonitorableObject(className);

		assertNotNull(result);
	}

}
