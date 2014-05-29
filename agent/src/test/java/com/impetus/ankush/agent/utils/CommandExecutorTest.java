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
package com.impetus.ankush.agent.utils;

import java.io.IOException;

import org.junit.*;
import org.junit.rules.ExpectedException;

import com.impetus.ankush.agent.utils.CommandExecutor;
import com.impetus.ankush.agent.utils.Result;

import static org.junit.Assert.*;

/**
 * The class <code>CommandExecutorTest</code> contains tests for the class
 * <code>{@link CommandExecutor}</code>.
 * 
 * @author Hokam Chauhan
 */
public class CommandExecutorTest {

	/** The exception. */
	@Rule
	public ExpectedException exception = ExpectedException.none();

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Run the CommandExecutor() constructor test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCommandExecutorObjectNotNull() throws Exception {
		CommandExecutor result = new CommandExecutor();
		assertNotNull(result);
	}
	
	/**
	 * Run the Result executeCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExecute_JPS_Command() throws Exception {
		String command = "jps";

		Result result = CommandExecutor.executeCommand(command);

		assertEquals(0, result.getExitVal());
		assertNotNull(result);
	}

	/**
	 * Run the Result executeCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExecute_LS_Command() throws Exception {
		String command = "ls";

		Result result = CommandExecutor.executeCommand(command);

		assertEquals(0, result.getExitVal());
		assertNotNull(result);
	}

	/**
	 * Run the Result executeCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExecute_ABCD_Command() throws Exception {
		String command = "abcd";

		exception.expect(IOException.class);
		Result result = CommandExecutor.executeCommand(command);

		assertEquals(0, result.getExitVal());
		assertNotNull(result);
	}

	/**
	 * Run the Result executeCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExecute_MkTestDir_Command() throws Exception {
		String command = "mkdir test";

		Result result = CommandExecutor.executeCommand(command);

		assertEquals(0, result.getExitVal());
		assertNotNull(result);
	}

	/**
	 * Run the Result executeCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExecute_RmTestDir_Command() throws Exception {
		String command = "rm -r test";

		Result result = CommandExecutor.executeCommand(command);

		assertEquals(0, result.getExitVal());
		assertNotNull(result);
	}
	
	/**
	 * Run the Result executeCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExecute_Empty_Command() throws Exception {
		String command = "";

		exception.expect(java.lang.IllegalArgumentException.class);
		Result result = CommandExecutor.executeCommand(command);

		assertEquals(1, result.getExitVal());
		assertNotNull(result);
	}
	
	/**
	 * Run the Result executeCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testExecute_Null_Command() throws Exception {
		String command = null;

		exception.expect(NullPointerException.class);
		Result result = CommandExecutor.executeCommand(command);

		assertEquals(1, result.getExitVal());
		assertNotNull(result);
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
	}
}
