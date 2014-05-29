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
import org.junit.Ignore;
import org.junit.Test;

import com.impetus.ankush.common.framework.config.NodeConf;

public class KafkaConfTest {
	
	KafkaConf conf = null;
	@Before
	public void setUp() {
		conf = new KafkaConf();
		ArrayList<NodeConf> nodes = new ArrayList<NodeConf>();
		HashMap zkNodesPort = new HashMap<String,Object>();
		nodes.add(new NodeConf("192.168.1.2", "192.168.1.2"));
		nodes.add(new NodeConf("192.168.1.3", "192.168.1.3"));
		ArrayList<String> zkNodes = new ArrayList<String>();
		zkNodes.add("192.168.1.1");
		zkNodesPort.put("zkNodes", zkNodes);
		conf.setPort(9092);
		conf.setNodes(nodes);
		conf.setZkNodesPort(zkNodesPort);
		conf.setLogDir("/home/test/kafka/log_dir");
	}

	@After
	public void tearDown() {
		conf = null;
	}
	
	/**
	 * Test kafka conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Ignore
	public void testKafkaConf() throws Exception {

		// object checking.
		assertNotNull(conf);

		// checking components.
		assertNotNull(conf.getCompNodes());
		// checking component size.
		assertEquals(2, conf.getCompNodes().size());

		//checking port.
		assertEquals(9092, conf.getPort());

		// checking log dir.
		assertEquals("/home/test/kafka/log_dir", conf.getLogDir());


		// checking zk nodes.
		HashMap<String,Object> zkNodesPort = conf.getZkNodesPort();
		List<String> zkNodes = (List<String>)zkNodesPort.get("zkNodes");

		assertNotNull(zkNodes);
		assertEquals(1, zkNodes.size());
	}
	
	/**
	 * Run the String toString() method test.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void testToString_1() throws Exception {
		KafkaConf fixture = new KafkaConf();
		fixture.setNodes(new ArrayList());
		fixture.setLogDir("");
		fixture.setZkNodesPort(new HashMap());
		fixture.setPort(9092);

		String result = fixture.toString();

		// add additional test code here
		assertNotNull(result);
	}

}
