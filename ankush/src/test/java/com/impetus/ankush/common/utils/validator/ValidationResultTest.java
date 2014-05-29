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
package com.impetus.ankush.common.utils.validator;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class ValidationResultTest.
 */
public class ValidationResultTest {

	/**
	 * Test default validation result.
	 */
	@Test
	public void testDefaultValidationResult() {
		ValidationResult fixture = new ValidationResult();

		assertNotNull(fixture);
		assertFalse(fixture.isStatus());
		assertNull(fixture.getMessage());
	}

	/**
	 * Test validation result.
	 */
	@Test
	public void testValidationResult() throws Exception {
		ValidationResult fixture = new ValidationResult();
		fixture.setStatus(true);
		fixture.setMessage("message");

		assertNotNull(fixture);
		assertTrue(fixture.isStatus());
		assertEquals("message", fixture.getMessage());
	}
}
