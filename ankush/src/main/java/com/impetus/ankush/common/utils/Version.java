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
 * The Class Version.
 *
 * @author impadmin
 */
public class Version {

	/** The major. */
	private int major;
	
	/** The minor. */
	private int minor;
	
	/** The tiny. */
	private int tiny;

	/**
	 * Instantiates a new version.
	 *
	 * @param version the version
	 */
	private Version(String version) {
		this.major = 0;
		this.minor = 0;
		this.tiny = 0;
		String[] array = version.split("\\.");
		try {
			if (array.length > 0) {
				this.major = Integer.parseInt(array[0]);
			}

			if (array.length > 1) {
				this.minor = Integer.parseInt(array[1]);
			}

			if (array.length > 2) {
				this.tiny = Integer.parseInt(array[2]);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Compare int.
	 *
	 * @param int1 the int1
	 * @param int2 the int2
	 * @return the int
	 */
	private int compareInt(int int1, int int2) {
		if (int1 > int2) {
			return 1;
		} else if (int1 < int2) {
			return -1;
		}
		return 0;
	}

	/**
	 * Compare.
	 *
	 * @param v the v
	 * @return the int
	 */
	private int compare(Version v) {
		int result = compareInt(this.major, v.major);
		if (result != 0) {
			return result;
		}

		result = compareInt(this.minor, v.minor);
		if (result != 0) {
			return result;
		}

		return compareInt(this.tiny, v.tiny);
	}

	/**
	 * Compare.
	 *
	 * @param version1 the version1
	 * @param version2 the version2
	 * @return the int
	 */
	public static int compare(String version1, String version2) {
		if (version1.equalsIgnoreCase(version2)) {
			return 0;
		}
		return new Version(version1).compare(new Version(version2));
	}
}
