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
package com.impetus.ankush.agent.action.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.impetus.ankush.agent.action.Actionable;

/**
 * The Class LogHandler.
 *
 * @author nikunj
 */
public class LogHandler implements Actionable {

	/* (non-Javadoc)
	 * @see com.impetus.ankush.agent.action.Actionable#execute(java.util.List)
	 */
	@Override
	public boolean execute(List<String> argsList) {
		if (argsList.size() < 1) {
			System.out
					.println("Usage: java -jar agent.jar log <filePath> [start]");
			System.exit(1);
		}
		long skip = 0;
		long bytesCount = 0;
		String filePath = argsList.get(0);
		if (argsList.size() > 1) {
			skip = Long.parseLong(argsList.get(1));
		}

		if (argsList.size() > 2) {
			bytesCount = Long.parseLong(argsList.get(2));
		}

		if (!new File(filePath).isFile()) {
			System.err.println("File does not exist, Please provide valid path.");
			System.exit(1);
		}

		try {
			readFileAsString(filePath, skip, bytesCount);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		System.exit(0);
		return true;
	}

	/**
	 * Read file as string.
	 *
	 * @param filePath the file path
	 * @param skip the skip
	 * @param bytesCount the bytes count
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void readFileAsString(String filePath, long skip, long bytesCount)
			throws java.io.IOException {
		StringBuilder fileData = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		long actualSkip = reader.skip(skip);
		if (skip > actualSkip) {
			System.err.println("Invallid skip count");
			reader.close();
			System.exit(1);
		}
		char[] buf = new char[1024];
		int numRead = 0;
		int count = 0;
		while ((numRead = reader.read(buf)) != -1) {
			
			if (bytesCount > 0 && count+numRead >= bytesCount) {
				numRead = (int) bytesCount - count;				
			}
			count += numRead;
			
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
			if (bytesCount != 0 && count >= bytesCount) {
				break;
			}
		}
		skip += count;
		reader.close();
		System.out.println(skip);
		if (!fileData.toString().isEmpty()) {
			System.out.println(fileData.toString());
		}

	}
}
