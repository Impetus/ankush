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
package com.impetus.ankush2.hadoop.monitor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;

/**
 * The Class HadoopDFSManager.
 * 
 * @author Akhil
 */
public class HadoopDFSManager {

	/**
	 * Convert bytes.
	 * 
	 * @param bytes
	 *            the bytes
	 * @return String
	 */
	public static String convertBytes(long bytes) {
		String convertedVal = "0";
		if (bytes == 0) {
			return convertedVal;
		}

		final double HUNDRED = 100.0;
		final long KILOBYTE = 1024L;
		final long MEGABYTE = 1024L * 1024L;
		final long GIGABYTE = 1024L * 1024L * 1024L;
		DecimalFormat df = new DecimalFormat("0.00");

		if (bytes / GIGABYTE > 0) {
			convertedVal = df.format(((bytes * HUNDRED / GIGABYTE)) / HUNDRED)
					+ "GB";
		} else if (bytes / MEGABYTE > 0) {
			convertedVal = df.format(((bytes * HUNDRED / MEGABYTE)) / HUNDRED)
					+ "MB";
		} else if (bytes / KILOBYTE >= 0) {
			convertedVal = df.format(((bytes * HUNDRED / KILOBYTE)) / HUNDRED)
					+ "KB";
		}
		return convertedVal;
	}

	/**
	 * Convert to percentage.
	 * 
	 * @param value
	 *            the value
	 * @return the string
	 */
	private static String convertToPercentage(double value) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		return (df.format(value) + "%");
	}

    public static LinkedHashMap<String, String> getDfsUsageData(
			Map<String, Object> nameNodeInfoBeanObject) throws AnkushException {
		try {
			LinkedHashMap<String, String> dfsUsageData = new LinkedHashMap<String, String>();
			String key = HadoopConstants.Hadoop.Keys.NameNodeJmxKeyDisplayName
					.getKeyDisplayName(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.USED);
			String valueBytes = HadoopDFSManager
					.convertBytes(((Number) nameNodeInfoBeanObject
							.get(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.USED))
							.longValue());
			String valuePercent = HadoopDFSManager
					.convertToPercentage(((Number) nameNodeInfoBeanObject
							.get(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.PERCENTUSED))
							.doubleValue());

			String value = valueBytes
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + valuePercent;
			dfsUsageData.put(key, value);

			key = HadoopConstants.Hadoop.Keys.NameNodeJmxKeyDisplayName
					.getKeyDisplayName(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.FREE);
			valueBytes = HadoopDFSManager
					.convertBytes(((Number) nameNodeInfoBeanObject
							.get(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.FREE))
							.longValue());
			valuePercent = HadoopDFSManager
					.convertToPercentage(((Number) nameNodeInfoBeanObject
							.get(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.PERCENTREMAINING))
							.doubleValue());

			value = valueBytes + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ valuePercent;
			dfsUsageData.put(key, value);

			List<String> dfsUsageKeys = new ArrayList<String>();
			dfsUsageKeys
					.add(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.NONDFSUSEDSPACE);
			dfsUsageKeys.add(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.TOTAL);

			if (nameNodeInfoBeanObject != null) {
				for (String dfsUsagekey : dfsUsageKeys) {
					key = HadoopConstants.Hadoop.Keys.NameNodeJmxKeyDisplayName
							.getKeyDisplayName(dfsUsagekey);
					value = HadoopDFSManager
							.convertBytes(((Number) nameNodeInfoBeanObject
									.get(dfsUsagekey)).longValue());
					dfsUsageData.put(key, value);
				}
			}
			return dfsUsageData;
		} catch (Exception e){
			throw new AnkushException("Could not get HDFS summary from JMX data.");
		}
		
	}
}
