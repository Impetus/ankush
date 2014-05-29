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
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * The Class XMLManipulator.
 *
 * @author Mayur
 */
public class XMLManipulator {

	/** The Constant CONFIG_PACKAGE. */
	private static final String CONFIG_PACKAGE = "com.impetus.ankush.common.config";

	/** The log. */
	private static AnkushLogger log = new AnkushLogger(XMLManipulator.class);

	/**
	 * Gets the properties.
	 *
	 * @param filePath the file path
	 * @return the properties
	 */
	public static Map<String, String> getProperties(String filePath) {
		Map<String, String> params = new HashMap<String, String>();
		try {
			// create jaxb unmarshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(CONFIG_PACKAGE);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			// read conf file
			File confFile = new File(filePath);
			Configuration configuration = (Configuration) unmarshaller
					.unmarshal(confFile);

			// iterate and search for propertyName and delete

			List<Property> propertyList = configuration.getProperty();

			for (Property prop : propertyList) {
				params.put(prop.getName(), prop.getValue());
			}
		} catch (JAXBException e) {
			log.error(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return params;
	}
}
