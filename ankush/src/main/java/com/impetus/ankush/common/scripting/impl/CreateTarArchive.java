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

import java.util.List;

import com.impetus.ankush.common.scripting.AnkushTask;

/**
 * @author Akhil
 *
 */
public class CreateTarArchive extends AnkushTask {
	
	/**
	 * @param outputTarFilePath
	 * @param sourceList
	 */
	public CreateTarArchive(String baseDir, String outputTarFilePath, List<String> sourceList) {
		this.outputTarFilePath = outputTarFilePath;
		this.sourceList = sourceList;
		this.baseDir = baseDir;
	}
	private String baseDir;
	
	private String outputTarFilePath;
	
	private List<String> sourceList;

	@Override
	public String getCommand() {
		if(this.sourceList == null || this.sourceList.isEmpty()) {
			return null;
		}
		// "cd " + this.baseDir + ";
		StringBuilder command = new StringBuilder("tar -C " + this.baseDir +" -czf \"");
		command.append(outputTarFilePath).append("\"");
		for(String sourcePath : sourceList) {
			command.append(" \"").append(sourcePath).append("\"");	
		}
		return command.toString();
	}
}
