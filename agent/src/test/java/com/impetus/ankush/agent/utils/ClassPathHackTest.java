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

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>ClassPathHackTest</code> contains tests for the class
 * <code>{@link ClassPathHack}</code>.
 * 
 * @author hokam
 */
public class ClassPathHackTest {

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
	 * Run the ClassPathHack() constructor test.
	 */
	@Test
	public void testClassPathHack_1() throws Exception {
		ClassPathHack result = new ClassPathHack();
		assertNotNull(result);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_3() throws Exception {
		File f = File.createTempFile("0123456789", "0123456789");

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_4() throws Exception {
		File f = File.createTempFile("0123456789", "0123456789", (File) null);

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_5() throws Exception {
		File f = File.createTempFile("An��t-1.0.txt", "An��t-1.0.txt",
				(File) null);

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_6() throws Exception {
		File f = new File("");

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_7() throws Exception {
		File f = new File("", "");

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_8() throws Exception {
		File f = new File("0123456789", "0123456789");

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_9() throws Exception {
		File f = new File((File) null, "");

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(File) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_10() throws Exception {
		File f = new File((File) null, "0123456789");

		ClassPathHack.addFile(f);
	}

	/**
	 * Run the void addFile(String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_11() throws Exception {
		String s = "";

		ClassPathHack.addFile(s);
	}

	/**
	 * Run the void addFile(String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFile_12() throws Exception {
		String s = "0123456789";

		ClassPathHack.addFile(s);
	}

	/**
	 * Run the void addFiles(String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddFiles_1() throws Exception {
		String jarDir = System.getProperty("user.home");

		ClassPathHack.addFiles(jarDir);
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
