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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * The Class ZipFiles.
 */
public class ZipFiles {

	/**
	 * Zip file.
	 *
	 * @param filePath the file path
	 * @return the string
	 */
	public String zipFile(String filePath) {
		try {
			/* Create Output Stream that will have final zip files */
			OutputStream zipOutput = new FileOutputStream(new File(filePath
					+ ".zip"));
			/*
			 * Create Archive Output Stream that attaches File Output Stream / and
			 * specifies type of compression
			 */
			ArchiveOutputStream logicalZip = new ArchiveStreamFactory()
					.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutput);
			/* Create Archieve entry - write header information */
			logicalZip.putArchiveEntry(new ZipArchiveEntry(FilenameUtils.getName(filePath)));
			/* Copy input file */
			IOUtils.copy(new FileInputStream(new File(filePath)), logicalZip);
			/* Close Archieve entry, write trailer information */
			logicalZip.closeArchiveEntry();

			/* Finish addition of entries to the file */
			logicalZip.finish();
			/* Close output stream, our files are zipped */
			zipOutput.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		return filePath + ".zip";
	}
}
