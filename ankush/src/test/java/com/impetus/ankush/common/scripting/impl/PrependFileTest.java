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

import java.util.LinkedList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class PrependFileTest.
 */
public class PrependFileTest {

	/**
	 * Test prepend file_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testPrependFile_1() throws Exception {
		String content = "somelines";
		String file = "test.txt";

		PrependFile result = new PrependFile(content, file);

		assertNotNull(result);
		assertEquals("sed -i 1i\"somelines\" test.txt;", result.getCommand());
		assertEquals("Ankush Task Info...", result.getInfo());
	}

	/**
	 * Test prepend file_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testPrependFile_2() throws Exception {
		List<String> contents = new LinkedList();
		contents.add("line1");
		String file = "test.txt";

		PrependFile result = new PrependFile(contents, file);

		assertNotNull(result);
		assertEquals("sed -i 1i\"line1\" test.txt;", result.getCommand());
		assertEquals("Ankush Task Info...", result.getInfo());
	}

	/**
	 * Test prepend file_3.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testPrependFile_3() throws Exception {
		List<String> contents = new LinkedList();
		contents.add("line1");
		String file = "test.txt";
		String password = "password";

		PrependFile result = new PrependFile(contents, file, password);

		assertNotNull(result);
		assertEquals("echo 'password' | sudo -S sed -i 1i\"line1\" test.txt;",
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
		List<String> contents = new LinkedList();
		contents.add("line1");
		String file = "test.txt";
		String password = "password";
		PrependFile fixture = new PrependFile(contents, file, password);

		String result = fixture.getCommand();

		assertEquals("echo 'password' | sudo -S sed -i 1i\"line1\" test.txt;", result);
	}
}
