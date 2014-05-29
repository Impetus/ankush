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
package com.impetus.ankush.common.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>LogTest</code> contains tests for the class
 * <code>{@link Log}</code>.
 */
public class LogTest {
	private static final long DATE = 1372406912786L;
	private Log fixture;

	/**
	 * Run the Log() constructor test.
	 */
	@Test
	public void testLog_1() {
		Log result = new Log();
		assertNotNull(result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		Log obj = new Log();
		obj.setType("type");
		obj.setId(new Long(1L));
		obj.setClusterId(new Long(1L));
		obj.setHost("host");

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_2() {
		Object obj = null;

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_3() {
		Object obj = new Object();

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_4() {
		Log obj = new Log();
		obj.setClusterId(new Long(1L));

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		Log obj = new Log();
		obj.setClusterId(new Long(1L));
		obj.setHost("host");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		Log obj = new Log();
		obj.setId(new Long(1L));
		obj.setClusterId(new Long(1L));
		obj.setHost("host");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_7() {
		boolean result = fixture.equals(fixture);

		assertEquals(true, result);
	}

	/**
	 * Run the Long getClusterId() method test.
	 */
	@Test
	public void testGetClusterId_1() {
		Long result = fixture.getClusterId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the Date getCreatedAt() method test.
	 */
	@Test
	public void testGetCreatedAt_1() {
		Date result = fixture.getCreatedAt();

		assertNotNull(result);
		assertEquals(new Date(DATE).toString(), result.toString());
	}

	/**
	 * Run the String getHost() method test.
	 */
	@Test
	public void testGetHost_1() {
		String result = fixture.getHost();

		assertEquals("host", result);
	}

	/**
	 * Run the Long getId() method test.
	 */
	@Test
	public void testGetId_1() {
		Long result = fixture.getId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the String getLongMessage() method test.
	 */
	@Test
	public void testGetLongMessage_1() {
		String result = fixture.getLongMessage();

		assertEquals(
				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z").format(new Date(
						DATE)) + " [host] :message", result);
	}

	/**
	 * Run the String getLongMessage() method test.
	 */
	@Test
	public void testGetLongMessage_2() {
		fixture.setHost((String) null);

		String result = fixture.getLongMessage();

		assertEquals(
				new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z").format(new Date(
						DATE)) + ":message", result);
	}

	/**
	 * Run the String getMessage() method test.
	 */
	@Test
	public void testGetMessage_1() {
		String result = fixture.getMessage();

		assertEquals("message", result);
	}

	/**
	 * Run the String getNodeLastLogQuery(long,long) method test.
	 */
	@Test
	public void testGetNodeLastLogQuery_1() {
		long clusterId = 1L;
		long operationID = 1L;

		String result = Log.getNodeLastLogQuery(clusterId, operationID);

		assertEquals(
				"select * from log l inner join( select host, max(id) id from log where clusterid=1 and operationid=1 and host is not null group by host  ) ss on l.id = ss.id",
				result);
	}

	/**
	 * Run the Long getOperationId() method test.
	 */
	@Test
	public void testGetOperationId_1() {
		Long result = fixture.getOperationId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the String getType() method test.
	 */
	@Test
	public void testGetType_1() {
		String result = fixture.getType();

		assertEquals("type", result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Log();
		fixture.setType("type");
		fixture.setMessage("message");
		fixture.setOperationId(new Long(1L));
		fixture.setId(new Long(1L));
		fixture.setClusterId(new Long(1L));
		fixture.setHost("host");
		fixture.setCreatedAt(new Date(DATE));
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
