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
 * The Class WgetTest.
 */
public class WgetTest {

	/**
	 * Test wget_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testWget_1() throws Exception {
		String resourceUrl = "http://downloadurl.com/test.zip";
		String destinationFile = "test.zip";

		Wget result = new Wget(resourceUrl, destinationFile);

		assertNotNull(result);
		assertEquals("wget http://downloadurl.com/test.zip -O test.zip",
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
		Wget fixture = new Wget("http://downloadurl.com/test.zip", "test.zip");

		String result = fixture.getCommand();

		assertEquals("wget http://downloadurl.com/test.zip -O test.zip", result);
	}
}
