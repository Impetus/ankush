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
 * The Class SetEnvVariableTest.
 */
public class SetEnvVariableTest {

	/**
	 * Test set env variable_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetEnvVariable_1() throws Exception {
		String password = "password";
		String name = "TEST_HOME";
		String value = "test";
		String filePath = "/etc/environment";

		SetEnvVariable result = new SetEnvVariable(password, name, value,
				filePath);

		assertNotNull(result);
		assertEquals(
				"echo \"password\" | sudo -S sh -c \"echo 'export TEST_HOME=test' >> /etc/environment\"",
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
		String password = "password";
		String name = "TEST_HOME";
		String value = "test";
		String filePath = "/etc/environment";

		SetEnvVariable fixture = new SetEnvVariable(password, name, value,
				filePath);

		String result = fixture.getCommand();

		assertEquals(
				"echo \"password\" | sudo -S sh -c \"echo 'export TEST_HOME=test' >> /etc/environment\"",
				result);
	}
}
