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
package com.impetus.ankush.common.framework.config;

import java.util.HashMap;
import org.junit.*;

import com.impetus.ankush.common.constant.Constant;

import static org.junit.Assert.*;

/**
 * The Class NodeConfTest.
 */
public class NodeConfTest {

	// node conf
	private NodeConf node;

	@Before
	public void setUp() {
		node = new NodeConf("192.168.1.1", "192.168.1.1");
		node.setPublicIp("192.168.1.1");
		node.setPrivateIp("192.168.1.1");
		node.setId(1L);
		node.setMessage("Testing");
		node.setNodeState(Constant.Node.State.ADDING);
		node.setOs("Ubuntu");
		node.setType("Nimbus");
		node.setStatus(Boolean.FALSE);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put("Agent", "installation failed");
		node.setErrors(errors);
	}

	@After
	public void tearDown() {

	}

	/**
	 * Test node conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNodeConf_1() throws Exception {

		// add additional test code here
		assertNotNull(node);
		assertEquals("Testing", node.getMessage());
		assertEquals(new Long(1), node.getId());
		assertEquals("Nimbus", node.getType());
		assertEquals(Boolean.FALSE, node.getStatus());
		assertEquals("Ubuntu", node.getOs());
		assertEquals(Constant.Node.State.ERROR, node.getNodeState());
		assertEquals("192.168.1.1", node.getPublicIp());
		assertEquals("192.168.1.1", node.getPrivateIp());
	}

	/**
	 * Test node conf_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testNodeConf_2() throws Exception {
		NodeConf node = new NodeConf();

		// add additional test code here
		assertNotNull(node);
		assertEquals(null, node.getMessage());
		assertEquals(null, node.getId());
		assertEquals(null, node.getType());
		assertEquals(Boolean.FALSE, node.getStatus());
		node.setStatus(Boolean.TRUE);
		assertEquals(Boolean.TRUE, node.getStatus());
		assertEquals(null, node.getOs());
		assertEquals("deployed", node.getNodeState());
		assertEquals(null, node.getPublicIp());
		assertEquals(null, node.getPrivateIp());
	}

	/**
	 * Test add error_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testAddError() throws Exception {
		String key = "Test";
		String error = "executing test failed.";

		node.addError(key, error);

		assertNotNull(node.getErrors());
		assertEquals(true, node.getErrors().containsKey(key));
		assertEquals(error, node.getErrors().get(key));
	}

	/**
	 * Test equals_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testEquals() throws Exception {

		NodeConf nodeconf = new NodeConf();
		assertEquals(true, node.equals(node));
		assertEquals(false, node.equals(new NodeConf()));
		assertEquals(false, node.equals(new NodeConf(null, "192.168.1.1")));
		assertEquals(false, node.equals(new NodeConf("192.168.1.1", null)));
		assertEquals(false, node.equals(new Object()));
		assertEquals(false, node.equals(null));
		assertEquals(false,
				nodeconf.equals(new NodeConf("192.168.1.1", "192.168.1.1")));
		nodeconf = new NodeConf(null, "192.168.1.1");
		assertEquals(false,
				nodeconf.equals(new NodeConf("192.168.1.1", "192.168.1.1")));
		assertEquals(true,
				node.equals(new NodeConf("192.168.1.1", "192.168.1.1")));

	}

	/**
	 * Test get errors_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetErrors() throws Exception {
		HashMap<String, String> errors = node.getErrors();

		assertNotNull(errors);
		assertEquals(1, errors.size());
	}

	/**
	 * Test hash code_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testHashCode() throws Exception {
		NodeConf fixture = new NodeConf(null, null);

		// add additional test code here
		assertEquals(961, fixture.hashCode());

	}
}
