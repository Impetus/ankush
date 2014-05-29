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

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class ErrorInfoTest.
 */
public class ErrorInfoTest {

	/**
	 * Test get error desc_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetErrorDesc_1() throws Exception {
		ErrorInfo fixture = getErrorInfo();

		String result = fixture.getErrorDesc();

		assertEquals("some desc", result);
	}

	/**
	 * @return
	 */
	private ErrorInfo getErrorInfo() {
		ErrorInfo fixture = new ErrorInfo();
		fixture.setErrorId("1");
		fixture.setErrorDesc("some desc");
		return fixture;
	}

	/**
	 * Test get error id_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetErrorId_1() throws Exception {
		ErrorInfo fixture = getErrorInfo();

		String result = fixture.getErrorId();

		assertEquals("1", result);
	}

	/**
	 * Test set error desc_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetErrorDesc_1() throws Exception {
		ErrorInfo fixture = getErrorInfo();
		String errorDesc = "error desc";

		fixture.setErrorDesc(errorDesc);
		assertEquals(errorDesc, fixture.getErrorDesc());
	}

	/**
	 * Test set error id_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetErrorId_1() throws Exception {
		ErrorInfo fixture = getErrorInfo();
		String errorId = "2";

		fixture.setErrorId(errorId);
		assertEquals(errorId, fixture.getErrorId());
	}
}
