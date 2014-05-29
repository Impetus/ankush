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
package com.impetus.ankush.common.scripting;

import org.junit.*;
import static org.junit.Assert.*;
import com.impetus.ankush.common.scripting.impl.ClearFile;

/**
 * The Class AnkushTaskTest.
 */
public class AnkushTaskTest {
	
	/**
	 * Test check exit code_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckExitCode_1()
		throws Exception {
		AnkushTask fixture = new ClearFile("/tmp/tmp.txt");
		int exitCode = 0;

		Boolean result = fixture.checkExitCode(exitCode);

		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Test check std out_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCheckStdOut_1()
		throws Exception {
		AnkushTask fixture = new ClearFile("/tmp/tmp.txt");
		String stdout = "some output";

		Boolean result = fixture.checkStdOut(stdout);

		assertNotNull(result);
		assertEquals("true", result.toString());
		assertEquals(true, result.booleanValue());
	}

	/**
	 * Test get info_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetInfo_1()
		throws Exception {
		AnkushTask fixture = new ClearFile("/tmp/tmp.txt");

		String result = fixture.getInfo();

		assertEquals("Ankush Task Info...", result);
	}
}
