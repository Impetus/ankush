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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class FileNameUtils.
 * 
 * @author mayur
 */
public class FileNameUtils {

	private static final String TAR_GZ = ".tar.gz";

	private static final String COMPONENT_ENV_FILE_PREFIX = "ankush_";
	
	private static final String COMPONENT_ENV_FILE_DIR = "/etc/profile.d/";
	
	private static final String TGZ = ".tgz";

	private static final String ZIP = ".zip";

	/** The log. */
	private static final AnkushLogger LOG = new AnkushLogger(
			FileNameUtils.class);

	/**
	 * Gets the file extension.
	 * 
	 * @param fileName
	 *            the file name
	 * @return extension of file
	 */
	public static String getFileExtension(String fileName) {
		if (fileName != null) {
			if (fileName.endsWith(ZIP)) {
				return ZIP;
			}

			if (fileName.endsWith(TAR_GZ)) {
				return TAR_GZ;
			}
			if (fileName.endsWith(TGZ)) {
				return TGZ;
			}
		}
		return "unsupported";
	}

	/**
	 * Checks if is tar gz.
	 * 
	 * @param componentExtension
	 *            the component extension
	 * @return true, if is tar gz
	 */
	public static boolean isTarGz(String componentExtension) {
		return componentExtension.equals(new String(TAR_GZ))
				|| componentExtension.equals(new String(TGZ));
	}

	/**
	 * Checks if is zip.
	 * 
	 * @param componentExtension
	 *            the component extension
	 * @return true, if is zip
	 */
	public static boolean isZip(String componentExtension) {
		return componentExtension.equals(new String(ZIP));
	}

	/**
	 * Checks if is unsupported.
	 * 
	 * @param componentExtension
	 *            the component extension
	 * @return true, if is unsupported
	 */
	public static boolean isUnsupported(String componentExtension) {
		return componentExtension.equals(new String("unsupported"));
	}

	/**
	 * Convert to valid path.
	 * 
	 * @param path
	 *            the path
	 * @return the string
	 */
	public static String convertToValidPath(String path) {
		if(path == null) {
			return path;
		}
		if (path.endsWith("/")) {
			return path;
		} else {
			return path + "/";
		}
	}

	/**
	 * The Unix separator character.
	 */
	private static final char UNIX_SEPARATOR = '/';

	/**
	 * The Windows separator character.
	 */
	private static final char WINDOWS_SEPARATOR = '\\';

	/**
	 * Gets the name.
	 * 
	 * @param filename
	 *            the filename
	 * @return the name
	 */
	public static String getName(String filename) {
		if (filename == null) {
			return null;
		}
		int index = indexOfLastSeparator(filename);
		return filename.substring(index + 1);
	}

	/**
	 * Index of last separator.
	 * 
	 * @param filename
	 *            the filename
	 * @return the int
	 */
	public static int indexOfLastSeparator(String filename) {
		if (filename == null) {
			return -1;
		}
		int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
		int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
		return Math.max(lastUnixPos, lastWindowsPos);
	}

	/**
	 * Gets the dependency file name.
	 * 
	 * @param osName
	 *            the os name
	 * @param componentName
	 *            the component name
	 * @return the dependency file name
	 * @author hokam chauhan
	 */
	public static String getDependencyFileName(String osName,
			String componentName) {

		if (osName == null || componentName == null) {
			return null;
		}

		String resourcePath = AppStoreWrapper.getResourcePath();
		return resourcePath + "dependencies/" + componentName.toLowerCase()
				+ "/" + osName.toLowerCase();
	}

	/**
	 * Gets the installer file path.
	 * 
	 * @param osName
	 *            the os name
	 * @param componentName
	 *            the component name
	 * @return the installer file path
	 * @author hokam chauhan
	 */
	public static String getInstallerFilePath(String osName,
			String componentName) {

		if (osName == null || componentName == null) {
			return null;
		}

		String resourcePath = AppStoreWrapper.getResourcePath();
		return resourcePath + "installer/" + componentName.toLowerCase() + "/"
				+ osName.toLowerCase();
	}

	/**
	 * Gets the sciprt path.
	 * 
	 * @param type
	 *            the type
	 * @return the sciprt path
	 */
	public static String getSciprtPath(String technology, String fileName) {
		if (technology == null || fileName == null) {
			return null;
		}

		String resourcePath = AppStoreWrapper.getResourcePath();
		// getting configuration file
		return resourcePath + "/scripts/" + technology.toLowerCase() + "/"
				+ fileName + ".sh";
	}

	/**
	 * Method to get the parent folder path.
	 * 
	 * @param folderPath
	 *            the folder path
	 * @return The parent folder path.
	 */
	public static String getParentFolderName(String folderPath) {

		/* Checking folderPath end with file separator. */
		if (folderPath.endsWith(File.separator)) {
			folderPath = folderPath.substring(0, folderPath.length() - 1);
		}
		// Getting the last index of file separator in folderPath string.
		int end = folderPath.lastIndexOf(File.separator);

		// Getting the second last index of file separator in folderPath string.
		int start = folderPath.lastIndexOf(File.separator, end - 1);

		// Getting teh parent folder name.
		String parentFolderName = folderPath.substring(start + 1, end);

		return parentFolderName;
	}

	/**
	 * The Enum ONSFileType.
	 */
	public enum ONSFileType {

		/** The invalid. */
		INVALID,
		/** The zip. */
		ZIP,
		/** The tar gz. */
		TAR_GZ,
		/** The bin. */
		BIN,
		/** The rpm. */
		RPM,
		/** The gz. */
		GZ

	};

	/**
	 * Gets the file type.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the file type
	 */
	public static ONSFileType getFileType(String fileName) {
		if (fileName != null) {
			if (fileName.endsWith(ZIP)) {
				return ONSFileType.ZIP;
			}

			if (fileName.endsWith(TAR_GZ)) {
				return ONSFileType.TAR_GZ;
			}

			if (fileName.endsWith(".bin")) {
				return ONSFileType.BIN;
			}

			if (fileName.endsWith(".gz")) {
				return ONSFileType.GZ;
			}
		}
		return ONSFileType.INVALID;

	}

	/**
	 * Gets the path from archive.
	 * 
	 * @param archiveFile
	 *            the archive file
	 * @param charSequence
	 *            the char sequence
	 * @return the path from archive
	 */
	public static String getPathFromArchive(String archiveFile,
			String charSequence) {
		String path = null;
		ONSFileType fileType = getFileType(archiveFile);

		if (fileType == ONSFileType.TAR_GZ) {
			try {
				GZIPInputStream gzipInputStream = null;
				gzipInputStream = new GZIPInputStream(new FileInputStream(
						archiveFile));
				TarArchiveInputStream tarInput = new TarArchiveInputStream(
						gzipInputStream);

				TarArchiveEntry entry;
				while (null != (entry = tarInput.getNextTarEntry())) {
					if (entry.getName().contains(charSequence)) {
						path = entry.getName();
						break;
					}
				}

			} catch (FileNotFoundException e) {
				LOG.error(e.getMessage(), e);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		} else if (fileType == ONSFileType.ZIP) {
			ZipFile zf;
			try {
				zf = new ZipFile(archiveFile);
				Enumeration<? extends ZipEntry> entries = zf.entries();
				String fileName;
				while (entries.hasMoreElements()) {
					fileName = entries.nextElement().getName();
					if (fileName.contains(charSequence)) {
						path = fileName;
						break;
					}
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return path;
	}

	/**
	 * Gets the extracted directory name.
	 * 
	 * @param archiveFile
	 *            the archive file
	 * @return Directory name after extraction of archiveFile
	 */
	public static String getExtractedDirectoryName(String archiveFile) {
		String path = null;
		ONSFileType fileType = getFileType(archiveFile);

		if (fileType == ONSFileType.TAR_GZ || fileType == ONSFileType.GZ) {
			try {
				GZIPInputStream gzipInputStream = null;
				gzipInputStream = new GZIPInputStream(new FileInputStream(
						archiveFile));
				TarArchiveInputStream tarInput = new TarArchiveInputStream(
						gzipInputStream);

				return tarInput.getNextTarEntry().getName();

			} catch (FileNotFoundException e) {
				LOG.error(e.getMessage(), e);
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		} else if (fileType == ONSFileType.ZIP || fileType == ONSFileType.BIN) {
			ZipFile zf;
			try {
				zf = new ZipFile(archiveFile);
				Enumeration<? extends ZipEntry> entries = zf.entries();

				return entries.nextElement().getName();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		return path;
	}
	
	public static String getComponentEnvFilePath(String componentName) {
		if(componentName != null && (!componentName.isEmpty())) {
			return FileNameUtils.COMPONENT_ENV_FILE_DIR + FileNameUtils.COMPONENT_ENV_FILE_PREFIX + componentName + ".sh" ;	
		}
		return null;
	}
}
