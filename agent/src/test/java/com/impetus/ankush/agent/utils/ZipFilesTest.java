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

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>ZipFilesTest</code> contains tests for the class
 * <code>{@link ZipFiles}</code>.
 * 
 * @author hokam
 */
public class ZipFilesTest {
	/**
	 * An instance of the class being tested.
	 * 
	 * @see ZipFiles
	 */
	private ZipFiles fixture;

	/**
	 * Return an instance of the class being tested.
	 * 
	 * @return an instance of the class being tested
	 * 
	 * @see ZipFiles
	 */
	public ZipFiles getFixture() throws Exception {
		if (fixture == null) {
			fixture = new ZipFiles();
		}
		return fixture;
	}

	/**
	 * Run the String zipFile(String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testZipFile_fixture_1() throws Exception {
		ZipFiles fixture2 = getFixture();
		String filePath = "test.log";
		File file = new File(filePath);
		file.createNewFile();

		String result = fixture2.zipFile(filePath);
		file.delete();
		file = new File(result);
		file.delete();
		

		assertEquals("test.log.zip", result);
	}

	/**
	 * Run the String zipFile(String) method test.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testZipFile_fixture_2() throws Exception {
		ZipFiles fixture2 = getFixture();
		String filePath = "0123456789";

		String result = fixture2.zipFile(filePath);
		File file = new File(filePath + ".zip");
		file.delete();

		assertEquals(null, result);
	}

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
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 */
	@After
	public void tearDown() throws Exception {
	}
}
