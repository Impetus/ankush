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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class CommandExecutorTest.
 */
public class CommandExecutorTest {

	/**
	 * Test exec_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testExec_1() throws Exception {
		String command = "ls";

		int result = CommandExecutor.exec(command);

		assertEquals(0, result);
	}

	/**
	 * Test exec_3.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testExec_3() throws Exception {
		String command = "sl";

		int result = 0;
		try {
			result = CommandExecutor.exec(command);
		} catch (Exception e) {
			e.printStackTrace();
			result = 1;
		}

		assertNotSame(0, result);
	}

	/**
	 * Test exec_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testExec_2() throws Exception {
		String command = "ls";
		OutputStream out = new ByteArrayOutputStream();
		OutputStream err = new ByteArrayOutputStream();

		int result = CommandExecutor.exec(command, out, err);

		assertEquals(0, result);
		assertFalse(out.toString().isEmpty());
		assertTrue(err.toString().isEmpty());
	}

}
