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
package com.impetus.ankush.common.ganglia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class GangliaConfTest.
 */
public class GangliaConfTest {

	// field ganglia conf.
	GangliaConf conf = null;

	@Before
	public void setUp() {
		conf = new GangliaConf();
		conf.setPollingInterval(new Integer(1));
		conf.setGmetadNode(new NodeConf("192.168.1.1", "192.168.1.1"));
		conf.setGridName("Ankush");
		conf.setGmondNodes(new HashSet());
		conf.setServerConfFolder("/config/ganglia/");
		conf.setDwooFilePath("/var/lib/ganglia/dwoo");
		conf.setRrdFilePath("/var/lib/ganglia/rrds");
		conf.setPort(new Integer(1));
	}

	@After
	public void tearDown() {
		// making conf null.
		conf = null;
	}

	/**
	 * Test ganglia conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Ignore
	public void testGetGangliaConf() throws Exception {

		assertNotNull(conf);
		// checking dwoo path.
		assertEquals("/var/lib/ganglia/dwoo", conf.getDwooFilePath());
		// checking rrds path.
		assertEquals("/var/lib/ganglia/rrds", conf.getRrdFilePath());
		// checking gmetad node.
		NodeConf objNode = new NodeConf("192.168.1.1", "192.168.1.1");
		String nodeType = objNode.getType();
		if(nodeType != null) {
			if(nodeType.isEmpty()) {
				objNode.setType(Constant.Role.GMETAD);
			} else {
				objNode.setType(nodeType + "/" + Constant.Role.GMETAD);
			}	
		}
		
		assertEquals(objNode, conf.getGmetadNode());
		// checking gmond nodes.
		assertNotNull(conf.getGmondNodes());
		// checking gmond nodes size.
		assertEquals(0, conf.getGmondNodes().size());
		// checking grid name.
		assertEquals("Ankush", conf.getGridName());
		// chekcing polling interval.
		assertEquals(new Integer(1), conf.getPollingInterval());
		// checking port.
		assertEquals(new Integer(1), conf.getPort());
		// checking server conf path.
		assertEquals("/config/ganglia/", conf.getServerConfFolder());
		// checking is gmetad node.
		assertEquals(true,
				conf.isGmetadNode(objNode));
		assertEquals(false, conf.isGmetadNode(null));
		// setting gmetad node.
		conf.setGmetadNode(null);
		assertEquals(false, conf.isGmetadNode(null));

	}
}
