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
 * The Class RunInBackgroundTest.
 */
public class RunInBackgroundTest {
	
	/**
	 * Test run in background_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testRunInBackground_1()
		throws Exception {
		String command = "ls";

		RunInBackground result = new RunInBackground(command);

		assertNotNull(result);
		assertEquals("ls </dev/null >nohup.out 2>&1 &", result.getCommand());
		assertEquals("Ankush Task Info...", result.getInfo());
	}

	/**
	 * Test run in background_2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testRunInBackground_2()
		throws Exception {
		String command = "ls";
		String filePath = "file";

		RunInBackground result = new RunInBackground(command, filePath);

		assertNotNull(result);
		assertEquals("ls </dev/null >file 2>&1 &", result.getCommand());
		assertEquals("Ankush Task Info...", result.getInfo());
	}

	/**
	 * Test get command_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCommand_1()
		throws Exception {
		RunInBackground fixture = new RunInBackground("ls", "file");

		String result = fixture.getCommand();

		assertEquals("ls </dev/null >file 2>&1 &", result);
	}

	/**
	 * Test get command_2.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCommand_2()
		throws Exception {
		RunInBackground fixture = new RunInBackground("ls", (String) null);

		String result = fixture.getCommand();

		assertEquals("ls </dev/null >nohup.out 2>&1 &", result);
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp()
		throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown()
		throws Exception {
	}
}
