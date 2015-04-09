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
package com.impetus.ankush.common.scripting.impl;

import java.util.ArrayList;
import java.util.List;

import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.utils.FileUtils;

/**
 * @author Akhil
 *
 */
public class SyncFolder extends AnkushTask {
	
	/** The destinationIp. */
	private String destinationIp;
	
	/** The folderPath. */
	private String folderPath;
	
	private List<String> excludeFileList; 
	
	public SyncFolder(String destinationIp, String folderPath){
		this.destinationIp = destinationIp;
		this.folderPath = FileUtils.getSeparatorTerminatedPathEntry(folderPath);
		this.excludeFileList = new ArrayList<String>();
	} 
	
	public SyncFolder(String destinationIp, String folderPath, List<String> excludeFileList){
		this.destinationIp = destinationIp;
		this.folderPath = FileUtils.getSeparatorTerminatedPathEntry(folderPath);
		this.excludeFileList = excludeFileList;
	} 
	
	/* (non-Javadoc)
	 * @see net.neoremind.sshxcute.task.CustomTask#getCommand()
	 */
	@Override
	public String getCommand() {
		if((this.excludeFileList != null) && (this.excludeFileList.size() > 0)) {
			StringBuilder command = new StringBuilder("cd " + this.folderPath + ";scp -r $(ls " + this.folderPath + " | grep -v"); 
			for(String excludeFile : this.excludeFileList) {
				command.append(" -e " + excludeFile);
			}
			command.append(") " + this.destinationIp + ":" + this.folderPath);
			return command.toString();
		} else {
			return ("scp -r " + this.folderPath + "* " + this.destinationIp + ":" + this.folderPath);	
		}
	}

}
