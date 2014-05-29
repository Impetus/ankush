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
/**
 * 
 */
package com.impetus.ankush.hadoop.dfs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class DFSDataNodesInfoTest.
 *
 * @author bgunjan
 */
public class DFSDataNodesInfoTest {

	/** The dfs file status info. */
	DFSFileStatusInfo dfsFileStatusInfo;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void testSetUp() throws Exception {
		dfsFileStatusInfo = new DFSFileStatusInfo();
	}
	
	/**
	 * Test dfs file status info test.
	 */
	@Test
	public void testDFSFileStatusInfoTest() {
		assertNotNull(dfsFileStatusInfo);
	}

	
	/**
	 * Test get name.
	 */
	@Test
	public void testGetName() {
		String name = "datanode";
		dfsFileStatusInfo.setName(name);
		String val = dfsFileStatusInfo.getName();
		assertNotNull(val);
		assertSame(name, dfsFileStatusInfo.getName());
	}
	
	/**
	 * Test set name.
	 */
	@Test
	public void testSetName() {
		String name = "datanode";
		dfsFileStatusInfo.setName("datanode1");

		assertNotNull(dfsFileStatusInfo.getName());
		assertNotSame(name, dfsFileStatusInfo.getName());
	}
	
	/**
	 * Test get complete path.
	 */
	@Test
	public void testGetCompletePath() {
		String completePath = "/home/test/";
		dfsFileStatusInfo.setCompletePath(completePath);
		String val = dfsFileStatusInfo.getCompletePath();
		assertNotNull(val);
		assertSame(completePath, dfsFileStatusInfo.getCompletePath());
	}
	
	/**
	 * Test set complete path.
	 */
	@Test
	public void testSetCompletePath() {
		String completePath = "/home/test/";
		dfsFileStatusInfo.setCompletePath("/home/test1/");

		assertNotNull(dfsFileStatusInfo.getCompletePath());
		assertNotSame(completePath, dfsFileStatusInfo.getCompletePath());
	}
	
	/**
	 * Test get file type.
	 */
	@Test
	public void testGetFileType() {
		String fileType = "file";
		dfsFileStatusInfo.setFileType(fileType);
		String val = dfsFileStatusInfo.getFileType();
		assertNotNull(val);
		assertSame(fileType, dfsFileStatusInfo.getFileType());
	}
	
	
	/**
	 * Test set file type.
	 */
	@Test
	public void testSetFileType() {
		String fileType = "file";
		dfsFileStatusInfo.setFileType("dir");

		assertNotNull(dfsFileStatusInfo.getFileType());
		assertNotSame(fileType, dfsFileStatusInfo.getFileType());
	}
	
	/**
	 * Test get access time.
	 */
	@Test
	public void testGetAccessTime() {
		String accessTime = "Thu Jan 01 05:30:00 IST 1970";
		dfsFileStatusInfo.setAccessTime(accessTime);
		String val = dfsFileStatusInfo.getAccessTime();
		assertNotNull(val);
		assertSame(accessTime, dfsFileStatusInfo.getAccessTime());
	}
	
	/**
	 * Test set access time.
	 */
	@Test
	public void testSetAccessTime() {
		String accessTime = "Thu Jan 01 05:30:00 IST 1970";
		dfsFileStatusInfo.setAccessTime("Thu Jun 05 05:30:00 IST 1970");

		assertNotNull(dfsFileStatusInfo.getAccessTime());
		assertNotSame(accessTime, dfsFileStatusInfo.getAccessTime());
	}
	
	/**
	 * Test get modification time.
	 */
	@Test
	public void testGetModificationTime() {
		String modificationTime = "Tue Mar 12 20:01:37 IST 2013";
		dfsFileStatusInfo.setModificationTime(modificationTime);
		String val = dfsFileStatusInfo.getModificationTime();
		assertNotNull(val);
		assertSame(modificationTime, dfsFileStatusInfo.getModificationTime());
	}
	
	/**
	 * Test set modification time.
	 */
	@Test
	public void testSetModificationTime() {
		String modificationTime = "Tue Mar 12 20:01:37 IST 2013";
		dfsFileStatusInfo.setModificationTime("Tue Mar 23 20:01:37 IST 2013");

		assertNotNull(dfsFileStatusInfo.getModificationTime());
		assertNotSame(modificationTime, dfsFileStatusInfo.getModificationTime());
	}
	
	/**
	 * Test get group name.
	 */
	@Test
	public void testGetGroupName() {
		String groupName = "supergroup";
		dfsFileStatusInfo.setGroupName(groupName);
		String val = dfsFileStatusInfo.getGroupName();
		assertNotNull(val);
		assertSame(groupName, dfsFileStatusInfo.getGroupName());
	}
	
	/**
	 * Test set group name.
	 */
	@Test
	public void testSetGroupName() {
		String groupName = "supergroup";
		dfsFileStatusInfo.setGroupName("testgroup");

		assertNotNull(dfsFileStatusInfo.getGroupName());
		assertNotSame(groupName, dfsFileStatusInfo.getGroupName());
	}
	
	/**
	 * Test get owner.
	 */
	@Test
	public void testGetOwner() {
		String owner = "root";
		dfsFileStatusInfo.setOwner(owner);
		String val = dfsFileStatusInfo.getOwner();
		assertNotNull(val);
		assertSame(owner, dfsFileStatusInfo.getOwner());
	}
	
	/**
	 * Test set owner.
	 */
	@Test
	public void testSetOwner() {
		String owner = "root";
		dfsFileStatusInfo.setOwner("test");

		assertNotNull(dfsFileStatusInfo.getOwner());
		assertNotSame(owner, dfsFileStatusInfo.getOwner());
	}
	
	/**
	 * Test get block size.
	 */
	@Test
	public void testGetBlockSize() {
		String blockSize = "64MB";
		dfsFileStatusInfo.setBlockSize(blockSize);
		String val = dfsFileStatusInfo.getBlockSize();
		assertNotNull(val);
		assertSame(blockSize, dfsFileStatusInfo.getBlockSize());
	}
	
	/**
	 * Test set block size.
	 */
	@Test
	public void testSetBlockSize() {
		String blockSize = "64MB";
		dfsFileStatusInfo.setBlockSize("0");

		assertNotNull(dfsFileStatusInfo.getBlockSize());
		assertNotSame(blockSize, dfsFileStatusInfo.getBlockSize());
	}
	
	/**
	 * Test get directory count.
	 */
	@Test
	public void testGetDirectoryCount() {
		long directoryCount = 4;
		dfsFileStatusInfo.setDirectoryCount(directoryCount);
		long val = dfsFileStatusInfo.getDirectoryCount();
		assertNotNull(val);
		assertSame(directoryCount, dfsFileStatusInfo.getDirectoryCount());
	}
	
	/**
	 * Test set directory count.
	 */
	@Test
	public void testSetDirectoryCount() {
		long directoryCount = 4;
		dfsFileStatusInfo.setDirectoryCount(0);

		assertNotNull(dfsFileStatusInfo.getDirectoryCount());
		assertNotSame(directoryCount, dfsFileStatusInfo.getDirectoryCount());
	}
	
	/**
	 * Test get file count.
	 */
	@Test
	public void testGetFileCount() {
		long fileCount = 4;
		dfsFileStatusInfo.setFileCount(fileCount);
		long val = dfsFileStatusInfo.getFileCount();
		assertNotNull(val);
		assertSame(fileCount, dfsFileStatusInfo.getFileCount());
	}
	
	/**
	 * Test set file count.
	 */
	@Test
	public void testSetFileCount() {
		long fileCount = 4;
		dfsFileStatusInfo.setFileCount(0);

		assertNotNull(dfsFileStatusInfo.getFileCount());
		assertNotSame(fileCount, dfsFileStatusInfo.getFileCount());
	}
}
