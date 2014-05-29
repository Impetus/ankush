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
package com.impetus.ankush.cassandra;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CassandranodeConfTest {
	private CassandraNodeConf cassandraNodeConfObj;
	
	@Before
	public void setUp() throws Exception {
		boolean seedNode = true;
		cassandraNodeConfObj = new CassandraNodeConf();
		cassandraNodeConfObj.isSeedNode(seedNode);
	}

	@After
	public void tearDown() throws Exception {
		cassandraNodeConfObj = null;
	}

	@Test
	public void test() {
		assertNotNull(cassandraNodeConfObj);
	}
	
	@Test
	public void testPositiveGetSeedNode() {
		boolean val = cassandraNodeConfObj.isSeedNode();
		assertNotNull(val);
		assertSame(true, cassandraNodeConfObj.isSeedNode());
	}

	@Test
	public void testNegativeGetSeedNode() {
		boolean val = cassandraNodeConfObj.isSeedNode();
		assertNotNull(val);
		assertNotSame(false, cassandraNodeConfObj.isSeedNode());
	}

	@Test
	public void testPositiveSetSeedNode() {
		cassandraNodeConfObj.isSeedNode(false);
		assertSame(false,
				cassandraNodeConfObj.isSeedNode());
		assertNotNull(cassandraNodeConfObj.isSeedNode());
	}

	@Test
	public void testNegativeSetSeedNode() {
		cassandraNodeConfObj.isSeedNode(false);
		assertNotSame(true,
				cassandraNodeConfObj.isSeedNode());
		assertNotNull(cassandraNodeConfObj.isSeedNode());
	}
	
}
