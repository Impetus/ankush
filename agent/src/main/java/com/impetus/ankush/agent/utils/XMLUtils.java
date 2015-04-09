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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

/**
 * XML Utility class for getting an Object from the XML file configuration and
 * to write the Object as XML configuration.
 * 
 * @author hokam
 * 
 */
public class XMLUtils {

	/**
	 * Method to get an XML Object for the given class type from the provided
	 * XML configuration file .
	 * 
	 * @param <S>
	 *            The class name for which XML object needs to be returned.
	 * @param filePath
	 *            File Path of XML file.
	 * @param className
	 *            Class name of returning object.
	 * @return The Object of provided class
	 * @throws Exception
	 */
	public static <S> S getXMLObject(String filePath, Class<S> className)
			throws Exception {
		// java XML context object.
		JAXBContext jc = JAXBContext.newInstance(className);
		// file.
		File file = new File(filePath);
		if (file.exists()) {
			// if file exists read the file content.
			String fileContent = FileUtils.readFileToString(file);
			// if not empty then unmarshal the object.
			if (!fileContent.isEmpty()) {
				// Creating unmarshaller
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				// Getting component services
				S object = (S) unmarshaller.unmarshal(file);
				// returning object.
				return object;
			}
		}
		return null;
	}

	/**
	 * Method to write an XML object in the given filePath.
	 * 
	 * @param <S>
	 *            The type of object that needs to written in XML file.
	 * @param filePath
	 *            Path of file where xml object gets written
	 * @param object
	 *            The Object that needs to be written in XML file.
	 * @throws Exception
	 */
	public static <S> void writeXMLObject(String filePath, S object)
			throws Exception {
		// java XML context object.
		JAXBContext jc = JAXBContext.newInstance(object.getClass());
		// file.
		File file = new File(filePath);
		// Creating marshaller
		Marshaller marshaller = jc.createMarshaller();
		// Setting output format
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// Marshalling object.
		marshaller.marshal(object, file);
	}
}
