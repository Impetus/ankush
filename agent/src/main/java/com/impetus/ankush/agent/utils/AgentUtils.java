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
package com.impetus.ankush.agent.utils;

import java.text.DecimalFormat;

/**
 * @author hokam
 * 
 */
public class AgentUtils {

	/**
	 * Convert bytes in human readable format(means add KB, MB or GB according
	 * to the provided input.
	 * 
	 * @param bytes
	 *            the bytes
	 * @return String
	 */
	public static String convertBytes(long bytes) {
		String val = "0";
		if (bytes == 0) {
			return val;
		}

		long KILOBYTE = 1024L;
		long MEGABYTE = 1024L * 1024L;
		long GIGABYTE = 1024L * 1024L * 1024L;
		DecimalFormat df = new DecimalFormat("0.00");

		if (bytes / KILOBYTE >= 0) {
			val = df.format(((bytes * 100.00 / KILOBYTE)) / 100.00) + "KB";
		}
		if (bytes / MEGABYTE > 0) {
			val = df.format(((bytes * 100.00 / MEGABYTE)) / 100.00) + "MB";
		}
		if (bytes / GIGABYTE > 0) {
			val = df.format(((bytes * 100.00 / GIGABYTE)) / 100.00) + "GB";
		}
		return val;
	}

	/**
	 * Gets the boolean value.
	 * 
	 * @param value
	 *            the key
	 * @return Boolean value for key
	 */
	public static boolean getBooleanValue(String value) {
		return Boolean.parseBoolean(value);
	}

	/**
	 * Gets the int value.
	 * 
	 * @param value
	 *            the key
	 * @return Integer value for key
	 */
	public static int getIntValue(String value) {
		if (value == null) {
			return 0;
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * Gets the int value.
	 * 
	 * @param value
	 *            the key
	 * @return Integer value for key
	 */
	public static Long getLongValue(String value) {
		if (value == null) {
			return 0l;
		}
		return Long.parseLong(value);
	}
	
	/**
	 * Gets the int value.
	 * 
	 * @param value
	 *            the key
	 * @return Integer value for key
	 */
	public static Double getDoubleValue(String value) {
		if (value == null) {
			return 0d;
		}
		return Double.parseDouble(value);
	}
}
