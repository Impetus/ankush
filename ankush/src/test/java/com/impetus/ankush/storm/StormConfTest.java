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

import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class StormConfTest.
 */
public class StormConfTest {

	// conf field
	StormConf conf = null;

	@Before
	public void setUp() {
		conf = new StormConf();
		ArrayList<NodeConf> nodes = new ArrayList<NodeConf>();
		nodes.add(new NodeConf("192.168.1.2", "192.168.1.2"));
		nodes.add(new NodeConf("192.168.1.3", "192.168.1.3"));
		ArrayList<String> zkNodes = new ArrayList<String>();
		zkNodes.add("192.168.1.1");
		conf.setSupervisors(nodes);
		conf.setZkNodes(zkNodes);
		conf.setLocalDir("/home/test/storm/local_dir");
		conf.setNimbus(new NodeConf("192.168.1.1", "192.168.1.1"));
		conf.setUiPort(9090);
		conf.setSlotsPorts(new ArrayList());
	}

	@After
	public void tearDown() {
		conf = null;
	}

	/**
	 * Run the String toString() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testToString_1() throws Exception {
		StormConf fixture = new StormConf();
		fixture.setSupervisors(new ArrayList());
		fixture.setSlotsPorts(new ArrayList());
		fixture.setLocalDir("");
		fixture.setNimbus(new NodeConf());
		fixture.setUiPort(1);
		fixture.setZkNodes(new ArrayList());

		String result = fixture.toString();

		// add additional test code here
		assertNotNull(result);
	}
}
