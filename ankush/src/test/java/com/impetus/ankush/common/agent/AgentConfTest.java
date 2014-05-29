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
package com.impetus.ankush.common.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class AgentConfTest.
 */
public class AgentConfTest {

	AgentConf conf = null;

	@Before
	public void setUp() {
		// creating a agent conf object.
		conf = new AgentConf();
		conf.setAgentDaemonClass("AnkushAgent");
		conf.setTechnologyName("Hadoop");
		conf.setNodes(new LinkedList());
		conf.setLocalJarsPath("/tmp/jars/");
	}

	@After
	public void tearDown() {
		// making conf null.
		conf = null;
	}

	/**
	 * Test agent conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testAgentConf() throws Exception {

		assertNotNull(conf);
		assertEquals("AnkushAgent", conf.getAgentDaemonClass());
		assertNotNull(conf.getNodes());
		assertEquals(0, conf.getNodes().size());
		assertEquals("Hadoop", conf.getTechnologyName());
		assertEquals("/tmp/jars/", conf.getLocalJarsPath());
	}
}
