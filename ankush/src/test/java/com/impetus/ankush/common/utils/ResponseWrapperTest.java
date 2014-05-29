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
package com.impetus.ankush.common.utils;

import java.util.LinkedList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;
import com.impetus.ankush.common.framework.config.ErrorInfo;

/**
 * The Class ResponseWrapperTest.
 */
public class ResponseWrapperTest {

	/**
	 * Test response wrapper_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testResponseWrapper_1() throws Exception {

		ResponseWrapper result = new ResponseWrapper();

		assertNotNull(result);
		assertEquals(null, result.getStatus());
		assertEquals(null, result.getOutput());
		assertEquals(null, result.getDescription());
		assertEquals(null, result.getErrors());
	}

	/**
	 * Test response wrapper_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testResponseWrapper_2() throws Exception {
		String status = "status";
		String description = "desc";
		List<ErrorInfo> errors = new LinkedList();
		ResponseWrapper result = new ResponseWrapper(null, status, description,
				errors);

		assertNotNull(result);
		assertEquals(status, result.getStatus());
		assertEquals(null, result.getOutput());
		assertEquals(description, result.getDescription());
		assertNotNull(result.getErrors());
	}

	/**
	 * Test response wrapper_10.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testResponseWrapper_10() throws Exception {
		String output = "test";
		String status = "status";
		String description = "desc";
		List<ErrorInfo> errors = new LinkedList();
		ResponseWrapper result = new ResponseWrapper();
		result.setDescription(description);
		result.setErrors(errors);
		result.setOutput(output);
		result.setStatus(status);

		assertNotNull(result);
		assertEquals(status, result.getStatus());
		assertEquals(output, result.getOutput());
		assertEquals(description, result.getDescription());
		assertNotNull(result.getErrors());
		assertEquals(errors, result.getErrors());
	}

}
