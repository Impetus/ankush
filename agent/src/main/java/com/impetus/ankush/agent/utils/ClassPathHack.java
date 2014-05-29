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
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * The Class ClassPathHack.
 * 
 * @author Hokam
 */
public class ClassPathHack {

	/** The Constant parameters. */
	private static final Class[] parameters = new Class[] { URL.class };

	/**
	 * Adds the file.
	 * 
	 * @param s
	 *            the s
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addFile(String s) throws IOException {
		File f = new File(s);
		addFile(f);
	}

	/**
	 * Add the list of files.
	 * 
	 * @param s
	 *            the s
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addFiles(List<String> files) throws IOException {
		for (String jar : files) {
			try {
				addFile(jar);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Adds the file.
	 * 
	 * @param f
	 *            the f
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addFile(File f) throws IOException {
		// f.toURL is deprecated
		addURL(f.toURL());
	}

	/**
	 * Adds the files.
	 * 
	 * @param jarDir
	 *            the jar dir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addJarFiles(String jarDir) throws IOException {
		File fileDirs = new File(jarDir);
		for (File file : fileDirs.listFiles()) {
			if (file.getName().endsWith(".jar")) {
				addFile(file);
			}
		}
	}

	/**
	 * Adds the files.
	 * 
	 * @param jarDir
	 *            the jar dir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addFiles(String jarDir) throws IOException {
		File fileDirs = new File(jarDir);
		for (File file : fileDirs.listFiles()) {
			addFile(file);
		}
	}

	/**
	 * Adds the url.
	 * 
	 * @param u
	 *            the u
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static void addURL(URL u) throws IOException {
		URLClassLoader sysloader = (URLClassLoader) ClassLoader
				.getSystemClassLoader();
		Class sysclass = URLClassLoader.class;

		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (Exception t) {
			t.printStackTrace();
			throw new IOException(
					"Error, could not add URL to system classloader");
		}

	}
}
