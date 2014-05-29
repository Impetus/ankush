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
package com.impetus.ankush.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Class FileUtils.
 *
 * @author Vinay Kher
 */
public class FileUtils {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(FileUtils.class);

	/** The Constant STR_SLASH. */
	private static final String STR_SLASH = "/";
	
	/** The Constant LINE_SEPARATOR. */
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * Gets the separator terminated path entry.
	 *
	 * @param path the path
	 * @param separator the separator
	 * @return the separator terminated path entry
	 */
	private static String getSeparatorTerminatedPathEntry(String path,
			String separator) {
		if ((path != null) && (!path.endsWith(separator))) {
			path += separator;
		}
		return path;
	}

	/**
	 * Gets the separator terminated path entry.
	 *
	 * @param path the path
	 * @return the separator terminated path entry
	 */
	public static String getSeparatorTerminatedPathEntry(String path) {
		return getSeparatorTerminatedPathEntry(path, STR_SLASH);
	}

	/**
	 * Load lines.
	 *
	 * @param fileName the file name
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> loadLines(String fileName) throws IOException {
		List<String> lineList = new ArrayList<String>();
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		do {
			line = br.readLine();
			if (line != null) {
				lineList.add(line);
			}
		} while (line != null);

		br.close();
		fr.close();
		return lineList;
	}

	/**
	 * Gets the random file name.
	 *
	 * @return the random file name
	 */
	public static String getRandomFileName() {
		String fileName = null;
		Random random = new Random();
		int n = random.nextInt();
		if (n < 0) {
			n = -n;
		}
		fileName = "" + n;
		return fileName;
	}

	/**
	 * Ensure folder.
	 *
	 * @param folderPath the folder path
	 * @return true, if successful
	 */
	public static boolean ensureFolder(String folderPath) {
		boolean ensured = false;
		File file = new File(folderPath);
		if (!file.exists()) {
			ensured = file.mkdirs();
		}
		else {
			ensured = file.isDirectory();
		}
		return ensured;
	}

	/**
	 * Checks if is file exists.
	 *
	 * @param filePath the file path
	 * @param dataSizeChk the data size chk
	 * @return true, if is file exists
	 */
	public static boolean isFileExists(String filePath, boolean dataSizeChk) {
		return FileUtils.isFileExists(filePath, dataSizeChk, 1);
	}

	/**
	 * Checks if is file exists.
	 *
	 * @param filePath the file path
	 * @param dataSizeChk the data size chk
	 * @param size the size
	 * @return true, if is file exists
	 */
	private static boolean isFileExists(String filePath, boolean dataSizeChk,
			long size) {
		boolean exists = false;
		if ((filePath != null) && (filePath.length() != 0)) {
			File f = new File(filePath);
			if (f.exists() && f.isFile()) {
				if (dataSizeChk) {
					if (f.length() >= size) {
						exists = true;
					}
					else {
						exists = false;
					}
				} else {
					exists = true;
				}
			}
		}
		return exists;
	}

	/**
	 * Gets the name part.
	 *
	 * @param path the path
	 * @return the name part
	 */
	public static String getNamePart(String path) {
		if (path.endsWith(File.separator)) {
			path = path.substring(0, path.length() - 1);
		}
		int start = path.lastIndexOf(File.separator);
		return path.substring(start + 1);
	}

	/**
	 * Write lines.
	 *
	 * @param completeFilePath the complete file path
	 * @param lineList the line list
	 */
	public static void writeLines(String completeFilePath, List<String> lineList) {
		if (completeFilePath.endsWith(STR_SLASH)) {
			completeFilePath = completeFilePath.substring(0,
					completeFilePath.lastIndexOf(STR_SLASH));
		}

		String fileName = completeFilePath.substring(
				completeFilePath.lastIndexOf(STR_SLASH) + 1,
				completeFilePath.length());
		String filePath = completeFilePath.substring(0,
				completeFilePath.lastIndexOf(STR_SLASH) + 1);

		File parentFolder = new File(filePath);

		if (!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		try {
			FileWriter scriptFile = new FileWriter(filePath + fileName);
			BufferedWriter out = new BufferedWriter(scriptFile);
			for (int count = 0; count < lineList.size(); ++count) {
				String data = lineList.get(count);
				out.write(data);
				out.write(LINE_SEPARATOR);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Write lines.
	 *
	 * @param completeFilePath the complete file path
	 * @param data the data
	 */
	public static void writeLines(String completeFilePath, String data) {
		List lst = new ArrayList<String>();
		lst.add(data);
		writeLines(completeFilePath, lst);
	}
}
