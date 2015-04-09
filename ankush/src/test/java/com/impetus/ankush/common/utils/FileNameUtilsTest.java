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

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class FileNameUtilsTest.
 */
public class FileNameUtilsTest {
	
	/**
	 * Test file name utils_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testFileNameUtils_1()
		throws Exception {
		FileNameUtils result = new FileNameUtils();
		assertNotNull(result);
	}

	/**
	 * Test convert to valid path_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testConvertToValidPath_1()
		throws Exception {
		String path = "/";

		String result = FileNameUtils.convertToValidPath(path);

		assertEquals("/", result);
	}

	
	/**
	 * Test get extracted directory name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetExtractedDirectoryName_1()
		throws Exception {
		String archiveFile = "";

		String result = FileNameUtils.getExtractedDirectoryName(archiveFile);

		assertEquals(null, result);
	}

	/**
	 * Test get file extension_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetFileExtension_1()
		throws Exception {
		String fileName = ".zip";

		String result = FileNameUtils.getFileExtension(fileName);

		assertEquals(".zip", result);
	}

	/**
	 * Test get file type_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetFileType_1()
		throws Exception {
		String fileName = ".zip";

		FileNameUtils.ONSFileType result = FileNameUtils.getFileType(fileName);

		assertNotNull(result);
		assertEquals("ZIP", result.name());
		assertEquals("ZIP", result.toString());
		assertEquals(1, result.ordinal());
	}

	/**
	 * Test get installer file path_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetInstallerFilePath_1()
		throws Exception {
		String osName = "ubuntu";
		String componentName = "gmetad";

		String result = FileNameUtils.getInstallerFilePath(osName, componentName);

		assertNotNull(result);
	}

	/**
	 * Test get name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetName_1()
		throws Exception {
		String filename = "/home/abc.txt";

		String result = FileNameUtils.getName(filename);

		assertEquals("abc.txt", result);
	}

	/**
	 * Test get parent folder name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetParentFolderName_1()
		throws Exception {
		String folderPath = "/home/impetus/";

		String result = FileNameUtils.getParentFolderName(folderPath);
		assertNotNull(result);
	}

	/**
	 * Test get path from archive_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetPathFromArchive_1()
		throws Exception {
		String archiveFile = "";
		String charSequence = "";

		String result = FileNameUtils.getPathFromArchive(archiveFile, charSequence);

		assertEquals(null, result);
	}

	/**
	 * Test index of last separator_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIndexOfLastSeparator_1()
		throws Exception {
		String filename = "/home/";

		int result = FileNameUtils.indexOfLastSeparator(filename);

		assertEquals(5, result);
	}

	/**
	 * Test is tar gz_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIsTarGz_1()
		throws Exception {
		String componentExtension = ".tar.gz";

		boolean result = FileNameUtils.isTarGz(componentExtension);

		assertEquals(true, result);
	}

	/**
	 * Test is unsupported_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIsUnsupported_1()
		throws Exception {
		String componentExtension = "unsupported";

		boolean result = FileNameUtils.isUnsupported(componentExtension);

		assertEquals(true, result);
	}

	/**
	 * Test is zip_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testIsZip_1()
		throws Exception {
		String componentExtension = ".zip";

		boolean result = FileNameUtils.isZip(componentExtension);

		assertEquals(true, result);
	}

}
