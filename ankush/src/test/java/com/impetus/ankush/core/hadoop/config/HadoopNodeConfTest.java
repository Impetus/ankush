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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.hadoop.config.HadoopNodeConf;

/**
 * The class <code>HadoopNodeConfTest</code> contains tests for the class
 * <code>{@link HadoopNodeConf}</code>.
 * 
 * @author hokam
 */
public class HadoopNodeConfTest {

	// field node
	private HadoopNodeConf fixture = null;

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		fixture = new HadoopNodeConf();
		fixture.setDataNode(new Boolean(true));
		fixture.setNameNode(new Boolean(true));
		fixture.setSecondaryNameNode(new Boolean(true));
		fixture.setRack("rack");
	}

	/**
	 * Run the HadoopNodeConf() constructor test.
	 */
	@Test
	public void testHadoopNodeConf() throws Exception {
		assertNotNull(fixture);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEquals() throws Exception {
		assertEquals(false, fixture.equals(new Object()));
		assertEquals(true, fixture.equals(fixture));
		fixture.setPublicIp("publicIp");
		fixture.setPrivateIp("privateIp");
		assertEquals(true, fixture.equals(fixture));
	}

	/**
	 * Run the Boolean getDataNode() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetDataNode() throws Exception {

		Boolean result = fixture.getDataNode();

		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the Boolean getNameNode() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetNameNode() throws Exception {
		Boolean result = fixture.getNameNode();
		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the String getRackInfo() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetRackInfo_1() throws Exception {
		String result = fixture.getRack();
		assertEquals("rack", result);
	}

	/**
	 * Run the Boolean getSecondaryNameNode() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 26/6/13 11:43 AM
	 */
	@Test
	public void testGetSecondaryNameNode() throws Exception {
		Boolean result = fixture.getSecondaryNameNode();
		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Run the int hashCode() method test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 26/6/13 11:43 AM
	 */
	@Test
	public void testHashCode_1() throws Exception {
		HadoopNodeConf fixture = new HadoopNodeConf();
		fixture.setPublicIp("1");
		fixture.setPrivateIp("1");
		fixture.setDataNode((Boolean) null);
		fixture.setNameNode((Boolean) null);
		fixture.setSecondaryNameNode(new Boolean(true));
		fixture.setRack("");

		int result = fixture.hashCode();
		assertEquals(2529, result);
	}

	/**
	 * Run the int hashCode() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testHashCode_2() throws Exception {
		HadoopNodeConf fixture = new HadoopNodeConf();
		fixture.setPublicIp("2");
		fixture.setPrivateIp("2");
		fixture.setDataNode(new Boolean(true));
		fixture.setNameNode(new Boolean(true));
		fixture.setSecondaryNameNode((Boolean) null);
		fixture.setRack((String) null);

		int result = fixture.hashCode();
		assertEquals(2561, result);
	}

	/**
	 * Run the String toString() method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testToString_1() throws Exception {
		HadoopNodeConf fixture = new HadoopNodeConf();
		fixture.setDataNode(new Boolean(true));
		fixture.setNameNode(new Boolean(true));
		fixture.setSecondaryNameNode(new Boolean(true));
		fixture.setRack("rack");

		String result = fixture.toString();

		assertEquals(
				"HadoopNodeConf [nameNode=true, dataNode=true, secondaryNameNode=true]",
				result);
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
		fixture = null;
	}
}
