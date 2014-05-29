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
package com.impetus.ankush.common.dependency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class DependencyConfTest.
 */
public class DependencyConfTest {

	// config
	private DependencyConf conf = null;

	@Before
	public void setUp() {
		conf = new DependencyConf();
		conf.setInstallJava(true);
		conf.setJavaBinFileName("jdk160.bin");
		conf.setComponents(new HashMap());
		conf.setNodes(new HashSet());
	}

	/**
	 * Test dependency conf.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testDependencyConf() throws Exception {
		// check for not null.
		assertNotNull(conf);
		// check for jdk bin file name.
		assertEquals("jdk160.bin", conf.getJavaBinFileName());
		// check for component name.
		assertEquals(new HashMap(), conf.getComponents());
		// check for installation true/false.
		assertNotNull(conf.getInstallJava());
		assertEquals(true, conf.getInstallJava());

		// check for nodes.
		assertNotNull(conf.getNodes());
		assertEquals(0, conf.getNodes().size());
	}
}
