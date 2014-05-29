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
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import com.impetus.ankush.agent.action.Manipulatable;

/**
 * The Class XMLManipulator.
 *
 * @author Mayur
 */
public class XMLManipulator implements Manipulatable{

	/**
	 * Delete conf value.
	 *
	 * @param file the file
	 * @param propertyName the property name
	 * @return true, if successful
	 */
	@Override
	public boolean deleteConfValue(String file, String propertyName) {
		try {
			// create jaxb unmarshaller
			JAXBContext jaxbContext = JAXBContext
					.newInstance("com.impetus.ankush.agent.action.impl");
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			// read conf file
			File confFile = new File(file);
			Configuration configuration = (Configuration) unmarshaller
					.unmarshal(confFile);

			boolean status = false;
			// iterate and search for propertyName and delete
			List<Property> propertyList = configuration.getProperty();
			for (Iterator<Property> iterator = propertyList.iterator(); iterator
					.hasNext();) {
				Property property = iterator.next();
				if (property.getName().equals(propertyName)) {
					// delete
					status = configuration.getProperty().remove(property);
					break;
				}
			}
			if (status) {
				// update file
				Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(configuration, confFile);
				return true;
			} else {
				return false;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Edits the conf value.
	 *
	 * @param file the file
	 * @param propertyName the property name
	 * @param newPropertyValue the new property value
	 * @return true, if successful
	 */
	@Override
	public boolean editConfValue(String file, String propertyName,
			String newPropertyValue) {
		try {
			// read conf file
			File confFile = new File(file);

			if (!confFile.exists()) {
				System.err.println("File does not exist.");
				return false;
			}
			// create jaxb unmarshaller
			JAXBContext jaxbContext = JAXBContext
					.newInstance("com.impetus.ankush.agent.action.impl");
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			Configuration configuration = (Configuration) unmarshaller
					.unmarshal(confFile);

			boolean status = false;
			// iterate and search for propertyName and delete
			List<Property> propertyList = configuration.getProperty();
			for (Iterator<Property> iterator = propertyList.iterator(); iterator
					.hasNext();) {
				Property property = iterator.next();
				if (property.getName().equals(propertyName)) {
					// delete
					status = configuration.getProperty().remove(property);
					break;
				}
			}
			if (status) {
				return marshallObject(propertyName, newPropertyValue,
						jaxbContext, confFile, configuration);
			} else {
				return false;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Marshall object.
	 *
	 * @param propertyName the property name
	 * @param newPropertyValue the new property value
	 * @param jaxbContext the jaxb context
	 * @param confFile the conf file
	 * @param configuration the configuration
	 * @return true, if successful
	 * @throws JAXBException the jAXB exception
	 * @throws PropertyException the property exception
	 */
	private static boolean marshallObject(String propertyName,
			String newPropertyValue, JAXBContext jaxbContext, File confFile,
			Configuration configuration) throws JAXBException,
			PropertyException {
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		// create Property Object
		Property property = new Property();
		property.setName(propertyName);
		property.setValue(newPropertyValue);
		// add to parent
		configuration.getProperty().add(property);
		// update file
		marshaller.marshal(configuration, confFile);
		return true;
	}

	/**
	 * Write conf value.
	 *
	 * @param file the file
	 * @param propertyName the property name
	 * @param propertyValue the property value
	 * @return true, if successful
	 */
	@Override
	public boolean writeConfValue(String file, String propertyName,
			String propertyValue) {
		try {

			// save to file
			File confFile = new File(file);

			if (!confFile.exists()) {
				System.err.println("File does not exist.");
				return false;
			}
			// create jaxb unmarshaller
			JAXBContext jaxbContext = JAXBContext
					.newInstance("com.impetus.ankush.agent.action.impl");

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Configuration configuration = (Configuration) unmarshaller
					.unmarshal(confFile);

			return marshallObject(propertyName, propertyValue, jaxbContext,
					confFile, configuration);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Read conf value.
	 *
	 * @param file the file
	 * @param propertyName the property name
	 * @return the string
	 */
	@Override
	public String readConfValue(String file, String propertyName) {
		try {

			// save to file
			File confFile = new File(file);

			if (!confFile.exists()) {
				System.err.println("File does not exist.");
				return null;
			}

			// create jaxb unmarshaller
			JAXBContext jaxbContext = JAXBContext
					.newInstance("com.impetus.ankush.agent.action.impl");
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			Configuration configuration = (Configuration) unmarshaller
					.unmarshal(confFile);

			// iterate and search for propertyName
			List<Property> propertyList = configuration.getProperty();
			for (Iterator<Property> iterator = propertyList.iterator(); iterator
					.hasNext();) {
				Property property = iterator.next();
				if (property.getName().equals(propertyName)) {
					return property.getValue();
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
