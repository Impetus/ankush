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
package com.impetus.ankush.common.scripting.impl;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class AddToPathVariableTest.
 */
public class AddToPathVariableTest {

	/**
	 * Test add to path variable_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testAddToPathVariable_1() throws Exception {
		String path = "somepath";
		String filePath = "/etc/environment";
		String password = "password";

		AddToPathVariable result = new AddToPathVariable(path, filePath,
				password);

		assertNotNull(result);
		assertEquals(
				"source /etc/environment; echo \"password\" | sudo -S sh -c \"echo 'export PATH=somepath:$PATH' >> /etc/environment\"",
				result.getCommand());
		assertEquals("Ankush Task Info...", result.getInfo());
	}

	/**
	 * Test get command_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCommand_1() throws Exception {
		AddToPathVariable fixture = new AddToPathVariable("somepath",
				"/etc/environment", "password");

		String result = fixture.getCommand();

		assertEquals(
				"source /etc/environment; echo \"password\" | sudo -S sh -c \"echo 'export PATH=somepath:$PATH' >> /etc/environment\"",
				result);
	}
}
