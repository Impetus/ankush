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
package com.impetus.ankush.common.config;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class ConfigurationReader.
 *
 * @author Mayur Maps Hadoop style key-value XML file to HashMap object
 */
public class ConfigurationReader {

	/** The log. */
	private static AnkushLogger log = new AnkushLogger(
			ConfigurationReader.class);

	/** HashMap to store key value pairs. */
	private Map<String, String> keyValueConf = null;

	/** Fully qualified package name of generated conf classes. */
	private String packageName = new String("com.impetus.ankush.common.config");

	/**
	 * Instantiates a new configuration reader.
	 *
	 * @param filePath the file path
	 */
	public ConfigurationReader(String filePath) {
		this.keyValueConf = this.readKeyValueConfiguration(this.packageName,
				filePath);
	}

	/**
	 * Read key value configuration.
	 *
	 * @param packageName the package name
	 * @param filePath the file path
	 * @return HashMap of hadoop style key-value configuration file
	 */
	private Map<String, String> readKeyValueConfiguration(String packageName,
			String filePath) {
		try {
			// create jaxb unmarshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(packageName);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			// read conf file
			File confFile = new File(filePath);
			Configuration configuration = (Configuration) unmarshaller
					.unmarshal(confFile);

			// iterate and populate hashmap
			Map<String, String> configurationMap = new HashMap<String, String>();
			List<Property> propertyList = configuration.getProperty();
			for (Iterator<Property> iterator = propertyList.iterator(); iterator
					.hasNext();) {
				Property property = iterator.next();
				configurationMap.put(property.getName(), property.getValue());
			}

			// return populated hashmap
			return configurationMap;
		} catch (JAXBException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Gets the string value.
	 *
	 * @param key the key
	 * @return String value for key
	 */
	public String getStringValue(String key) {
		return this.keyValueConf.get(key);
	}

	/**
	 * Gets the boolean value.
	 *
	 * @param key the key
	 * @return Boolean value for key
	 */
	public boolean getBooleanValue(String key) {
	 	return Boolean.parseBoolean(this.keyValueConf.get(key));
	}

	/**
	 * Gets the int value.
	 *
	 * @param key the key
	 * @return Integer value for key
	 */
	public int getIntValue(String key) {
		return Integer.parseInt(this.keyValueConf.get(key));
	}

	/**
	 * Gets the long value.
	 *
	 * @param key the key
	 * @return Long value for key
	 */
	public long getLongValue(String key) {
		return Long.parseLong(this.keyValueConf.get(key));
	}

	/**
	 * Gets the float value.
	 *
	 * @param key the key
	 * @return Float value for key
	 */
	public float getFloatValue(String key) {
		return Float.parseFloat(this.keyValueConf.get(key));
	}

	/**
	 * Gets the double value.
	 *
	 * @param key the key
	 * @return Double value for key
	 */
	public double getDoubleValue(String key) {
		return Double.parseDouble(this.keyValueConf.get(key));
	}

}
