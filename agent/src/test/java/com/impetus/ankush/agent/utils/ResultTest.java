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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.*;

import com.impetus.ankush.agent.utils.Result;

/**
 * The class <code>ResultTest</code> contains tests for the class
 * <code>{@link Result}</code>.
 * 
 * @author Hokam Chauhan
 */
public class ResultTest {

	/**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 */
	@Before
	public void setUp() throws Exception {
		// add additional set up code here
	}

	/**
	 * Run the Result() constructor test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testResult() throws Exception {
		Result result = new Result();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Run the String getCommand() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetCommandValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("jps command not found");
		fixture.setOutput("");
		fixture.setCommand("jps");
		fixture.setExitVal(1);

		String result = fixture.getCommand();

		// add additional test code here
		assertEquals("jps", result);
	}

	/**
	 * Run the String getError() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetErrorValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("jps command not found");
		fixture.setOutput("");
		fixture.setCommand("jps");
		fixture.setExitVal(1);

		String result = fixture.getError();

		// add additional test code here
		assertEquals("jps command not found", result);
	}

	/**
	 * Run the int getExitVal() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetExitValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("");
		fixture.setOutput("");
		fixture.setCommand("");
		fixture.setExitVal(1);

		int result = fixture.getExitVal();

		// add additional test code here
		assertEquals(1, result);
	}

	/**
	 * Run the String getOutput() method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetOutputValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("");
		fixture.setOutput("123 AB \n145 BCDD");
		fixture.setCommand("");
		fixture.setExitVal(1);

		String result = fixture.getOutput();

		// add additional test code here
		assertEquals("123 AB \n145 BCDD", result);
	}

	/**
	 * Run the void setCommand(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetCommandValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("");
		fixture.setOutput("");
		fixture.setCommand("");
		fixture.setExitVal(1);
		String command = "ls";

		fixture.setCommand(command);

		assertEquals(command, fixture.getCommand());
	}

	/**
	 * Run the void setError(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetErrorValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("");
		fixture.setOutput("");
		fixture.setCommand("ls");
		fixture.setExitVal(1);
		String error = "ls : command not found";

		fixture.setError(error);

		assertEquals(error, fixture.getError());
	}

	/**
	 * Run the void setExitVal(int) method test.
	 *
	 * @throws Exception the exception
	 * @generatedBy CodePro at 12/2/13 11:59 AM
	 */
	@Test
	public void testSetExitValValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("");
		fixture.setOutput("");
		fixture.setCommand("");
		fixture.setExitVal(1);
		int exitVal = 0;

		fixture.setExitVal(exitVal);

		assertEquals(exitVal, fixture.getExitVal());
	}

	/**
	 * Run the void setOutput(String) method test.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetOutputValue() throws Exception {
		Result fixture = new Result();
		fixture.setError("");
		fixture.setOutput("");
		fixture.setCommand("ls");
		fixture.setExitVal(1);
		String output = "abcd.xml";

		fixture.setOutput(output);

		assertEquals(output, fixture.getOutput());
	}

	/**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
		// Add additional tear down code here
	}
}
