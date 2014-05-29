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

/**
 * This class implements the functionality of the Parsing the boolean, long and
 * int values from the String values.
 * 
 * @author hokam
 * 
 */
public class ParserUtil {

	/**
	 * Method to get the long value from the string value.
	 *
	 * @param numStr the num str
	 * @param defaultVal the default val
	 * @return the long value
	 */
	public static long getLongValue(String numStr, long defaultVal) {
		long paramVal = 0;
		try {
			if ((numStr != null) && (!numStr.equals(""))) {
				paramVal = Long.parseLong(numStr);
			}
			else {
				paramVal = defaultVal;
			}
		} catch (NumberFormatException e) {
			paramVal = defaultVal;
		}
		return paramVal;
	}

	/**
	 * Method to get the int value from the string value.
	 *
	 * @param intStr the int str
	 * @param defaultVal the default val
	 * @return the int value
	 */
	public static int getIntValue(String intStr, int defaultVal) {
		int paramVal = 0;
		try {
			if ((intStr != null) && (!intStr.equals(""))) {
				paramVal = Integer.parseInt(intStr);
			}
			else {
				paramVal = defaultVal;
			}
		} catch (NumberFormatException e) {
			paramVal = defaultVal;
		}
		return paramVal;
	}
}
