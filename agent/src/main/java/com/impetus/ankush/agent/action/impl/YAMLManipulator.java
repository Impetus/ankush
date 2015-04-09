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
package com.impetus.ankush.agent.action.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.agent.action.Manipulatable;

public class YAMLManipulator implements Manipulatable {

	/**
	 * Delete conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @return true, if successful
	 */
	public boolean deleteConfValue(String file, String objPropertyName) {

		String propertyName = (String) objPropertyName;
		Yaml yaml = new Yaml();
		try {
			InputStream fis = new FileInputStream(file);
			Object javaObject = yaml.load(fis);
			Map<String, Object> map = (Map<String, Object>) javaObject;
			if (map.containsKey(propertyName)) {
				map.remove(propertyName);
			}
			fis.close();
			// save to file
			File confFile = new File(file);
			if (!confFile.exists()) {
				System.err.println("File " + file + " does not exists.");
			}
			String dumped = yaml.dumpAsMap(map);
			FileUtils.writeStringToFile(confFile, dumped, false);

			return true;

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	/**
	 * Edits the conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @param newPropertyValue
	 *            the new property value
	 * @return true, if successful
	 */
	public boolean editConfValue(String file, String propertyName,
			String newPropertyValue) {

		boolean isInteger = checkNumber((String) newPropertyValue);
		boolean isBoolean = false;

		if (String.valueOf(newPropertyValue).equalsIgnoreCase("true")
				|| String.valueOf(newPropertyValue).equalsIgnoreCase("false")) {
			isBoolean = true;
		}

		List<Object> newPropertyValueList = new ArrayList<Object>();
		if (String.valueOf(newPropertyValue).contains(",")) {
			List<Object> list = new ArrayList<Object>(Arrays.asList(String
					.valueOf(newPropertyValue).split(",")));
			for (Object val : list) {
				if (isInteger) {
					newPropertyValueList.add(Integer.valueOf((String) val));
				} else if (isBoolean) {
					newPropertyValueList.add(Boolean.valueOf((String) val));
				} else {
					newPropertyValueList.add((String) val);
				}
			}
		} else {
			if (isInteger) {
				newPropertyValueList.add(Integer
						.valueOf((String) newPropertyValue));
			} else if (isBoolean) {
				newPropertyValueList.add(Boolean
						.valueOf((String) newPropertyValue));
			} else {
				newPropertyValueList.add((String) newPropertyValue);
			}
		}

		Yaml yaml = new Yaml();
		try {
			InputStream fis = new FileInputStream(file);
			Object javaObject = yaml.load(fis);
			Map<Object, Object> map = (Map<Object, Object>) javaObject;

			Object propertyValue = new Object();

			if (newPropertyValueList.size() > 1) {
				propertyValue = (List<Object>) newPropertyValueList;
			} else {
				propertyValue = newPropertyValueList.get(0);
			}

			map.put(propertyName, propertyValue);
			fis.close();
			// save to file
			File confFile = new File(file);
			if (!confFile.exists()) {
				System.err.println("File " + file + " does not exists.");
			}

			String dumped = yaml.dumpAsMap(map);
			FileUtils.writeStringToFile(confFile, dumped, false);

			return true;

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	private boolean checkNumber(String propertyValue) {
		try {
			double d;
			if (propertyValue.contains(",")) {
				String[] splittedArray = propertyValue.split(",");
				for (int i = 0; i < splittedArray.length; i++) {
					d = Double.parseDouble(splittedArray[i]);
				}
			} else {
				d = Double.parseDouble(propertyValue);
			}
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * Write conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @param propertyValue
	 *            the property value
	 * @return true, if successful
	 */
	public boolean writeConfValue(String file, String propertyName,
			String propertyValue) {

		return editConfValue(file, propertyName, propertyValue);

	}

	/**
	 * Read conf value.
	 * 
	 * @param file
	 *            the file
	 * @param propertyName
	 *            the property name
	 * @return the string
	 */
	public String readConfValue(String file, String objPropertyName) {

		String propertyName = (String) objPropertyName;

		Yaml yaml = new Yaml();
		try {
			InputStream fis = new FileInputStream(file);
			Object javaObject = yaml.load(fis);
			Map<String, Object> map = (Map<String, Object>) javaObject;

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().equals(propertyName)) {
					return (String) entry.getValue();
				}
			}
			fis.close();

		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

}
