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
package com.impetus.ankush.common.exception;

import org.junit.*;
import static org.junit.Assert.*;
import org.springframework.http.HttpStatus;

/**
 * The Class ControllerExceptionTest.
 */
public class ControllerExceptionTest {
	
	/**
	 * Test controller exception_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testControllerException_1()
		throws Exception {
		HttpStatus status = HttpStatus.ACCEPTED;
		String errorId = "1";
		String message = "message";

		ControllerException result = new ControllerException(status, errorId, message);

		assertNotNull(result);
		assertEquals("message", result.getMessage());
		assertEquals("1", result.getErrorId());
		assertEquals(null, result.getCause());
	}

	/**
	 * Test controller exception_2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testControllerException_2()
		throws Exception {
		HttpStatus status = HttpStatus.ACCEPTED;
		String errorId = "1";
		String message = "message";
		Throwable cause = new Throwable();

		ControllerException result = new ControllerException(status, errorId, message, cause);

		assertNotNull(result);
		assertEquals("message", result.getMessage());
		assertEquals("1", result.getErrorId());
	}

	/**
	 * Test get error id_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetErrorId_1()
		throws Exception {
		ControllerException fixture = new ControllerException(HttpStatus.ACCEPTED, "1", "message", new Throwable());

		String result = fixture.getErrorId();

		assertEquals("1", result);
	}

	/**
	 * Test get message_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetMessage_1()
		throws Exception {
		ControllerException fixture = new ControllerException(HttpStatus.ACCEPTED, "1", "message", new Throwable());

		String result = fixture.getMessage();

		assertEquals("message", result);
	}

	/**
	 * Test get status_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetStatus_1()
		throws Exception {
		ControllerException fixture = new ControllerException(HttpStatus.ACCEPTED, "1", "message", new Throwable());

		HttpStatus result = fixture.getStatus();

		assertNotNull(result);
		assertEquals(202, result.value());
		assertEquals("202", result.toString());
		assertEquals("Accepted", result.getReasonPhrase());
		assertEquals("ACCEPTED", result.name());
		assertEquals(6, result.ordinal());
	}

}
