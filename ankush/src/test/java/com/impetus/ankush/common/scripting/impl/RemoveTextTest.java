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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class RemoveTextTest.
 */
public class RemoveTextTest {

	/**
	 * Test remove text_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testRemoveText_1() throws Exception {
		String pattern = "somepattern";
		String filePath = "/tmp/test.txt";

		RemoveText result = new RemoveText(pattern, filePath);

		assertNotNull(result);
		assertEquals("sudo -S sed -i '/somepattern/d' /tmp/test.txt;",
				result.getCommand());
		assertEquals("Ankush Task Info...", result.getInfo());
	}

	/**
	 * Test remove text_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testRemoveText_2() throws Exception {
		ArrayList<String> contents = new ArrayList();
		contents.add("somepattern");
		String file = "/tmp/test.txt";

		RemoveText result = new RemoveText(contents, file);

		assertNotNull(result);
		assertEquals("sudo -S sed -i '/somepattern/d' /tmp/test.txt;",
				result.getCommand());
		assertEquals("Ankush Task Info...", result.getInfo());
	}

	/**
	 * Test remove text_3.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testRemoveText_3() throws Exception {
		List<String> contents = new LinkedList();
		contents.add("somepattern");
		String file = "/tmp/test.txt";
		String password = "password";

		RemoveText result = new RemoveText(contents, file, password);

		assertNotNull(result);
		assertEquals(
				"echo 'password' | sudo -S sed -i '/somepattern/d' /tmp/test.txt;",
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
		contents.add("somepattern");
		String file = "/tmp/test.txt";
		String password = "password";

		RemoveText fixture = new RemoveText(contents, file, password);

		String result = fixture.getCommand();

		assertEquals(
				"echo 'password' | sudo -S sed -i '/somepattern/d' /tmp/test.txt;",
				result);
	}

	/**
	 * Test get command_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCommand_2() throws Exception {
		List<String> contents = new LinkedList();
		contents.add("somepattern");
		String file = "/tmp/test.txt";

		RemoveText fixture = new RemoveText(contents, file, null);

		String result = fixture.getCommand();

		assertEquals("sudo -S sed -i '/somepattern/d' /tmp/test.txt;", result);
	}

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
	}
}
