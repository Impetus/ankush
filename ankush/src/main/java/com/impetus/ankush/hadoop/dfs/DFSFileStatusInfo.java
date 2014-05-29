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

import org.apache.hadoop.fs.permission.FsPermission;

/**
 * The Class DFSFileStatusInfo.
 *
 * @author bgunjan
 */
public class DFSFileStatusInfo {
	
	/** The name. */
	private String name;
	
	/** The complete path. */
	private String completePath;
	
	/** The file type. */
	private String fileType;
	
	/** The access time. */
	private String accessTime;
	
	/** The modification time. */
	private String modificationTime;
	
	/** The group name. */
	private String groupName;
	
	/** The owner. */
	private String owner;
	
	/** The block size. */
	private String blockSize;
	
	/** The file length. */
	private String fileLength;
	
	/** The permission. */
	private FsPermission permission;
	
	/** The replication. */
	private short replication;
	
	/** The directory count. */
	private long directoryCount;
	
	/** The file count. */
	private long fileCount;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the complete path.
	 *
	 * @return the completePath
	 */
	public String getCompletePath() {
		return completePath;
	}
	
	/**
	 * Sets the complete path.
	 *
	 * @param completePath the completePath to set
	 */
	public void setCompletePath(String completePath) {
		this.completePath = completePath;
	}
	
	/**
	 * Gets the file type.
	 *
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}
	
	/**
	 * Sets the file type.
	 *
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	/**
	 * Gets the access time.
	 *
	 * @return the accessTime
	 */
	public String getAccessTime() {
		return accessTime;
	}
	
	/**
	 * Sets the access time.
	 *
	 * @param accessTime the accessTime to set
	 */
	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}
	
	/**
	 * Gets the modification time.
	 *
	 * @return the modificationTime
	 */
	public String getModificationTime() {
		return modificationTime;
	}
	
	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}
	
	/**
	 * Gets the group name.
	 *
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	
	/**
	 * Sets the group name.
	 *
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner.
	 *
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**
	 * Gets the block size.
	 *
	 * @return the blockSize
	 */
	public String getBlockSize() {
		return blockSize;
	}
	
	/**
	 * Sets the block size.
	 *
	 * @param blockSize the blockSize to set
	 */
	public void setBlockSize(String blockSize) {
		this.blockSize = blockSize;
	}
	
	/**
	 * Gets the permission.
	 *
	 * @return the permission
	 */
	public FsPermission getPermission() {
		return permission;
	}
	
	/**
	 * Sets the permission.
	 *
	 * @param permission the permission to set
	 */
	public void setPermission(FsPermission permission) {
		this.permission = permission;
	}
	
	/**
	 * Gets the replication.
	 *
	 * @return the replication
	 */
	public short getReplication() {
		return replication;
	}
	
	/**
	 * Sets the replication.
	 *
	 * @param replication the replication to set
	 */
	public void setReplication(short replication) {
		this.replication = replication;
	}
	
	/**
	 * Gets the directory count.
	 *
	 * @return the directoryCount
	 */
	public long getDirectoryCount() {
		return directoryCount;
	}
	
	/**
	 * Sets the directory count.
	 *
	 * @param directoryCount the directoryCount to set
	 */
	public void setDirectoryCount(long directoryCount) {
		this.directoryCount = directoryCount;
	}
	
	/**
	 * Gets the file count.
	 *
	 * @return the fileCount
	 */
	public long getFileCount() {
		return fileCount;
	}
	
	/**
	 * Sets the file count.
	 *
	 * @param fileCount the fileCount to set
	 */
	public void setFileCount(long fileCount) {
		this.fileCount = fileCount;
	}

	/**
	 * Gets the file length.
	 *
	 * @return the fileLength
	 */
	public String getFileLength() {
		return fileLength;
	}
	
	/**
	 * Sets the file length.
	 *
	 * @param fileLength the fileLength to set
	 */
	public void setFileLength(String fileLength) {
		this.fileLength = fileLength;
	}
	
	
}
