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

import javax.xml.bind.JAXBElement;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class ObjectFactoryTest.
 */
public class ObjectFactoryTest {
	
	/**
	 * Test object factory_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testObjectFactory_1()
		throws Exception {

		ObjectFactory result = new ObjectFactory();

		assertNotNull(result);
	}

	/**
	 * Test create configuration_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateConfiguration_1()
		throws Exception {
		ObjectFactory fixture = new ObjectFactory();

		Configuration result = fixture.createConfiguration();

		assertNotNull(result);
	}

	/**
	 * Test create description_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateDescription_1()
		throws Exception {
		ObjectFactory fixture = new ObjectFactory();
		String value = "";

		JAXBElement<String> result = fixture.createDescription(value);

		assertNotNull(result);
		assertEquals("", result.getValue());
		assertEquals(false, result.isNil());
		assertEquals(true, result.isGlobalScope());
		assertEquals(false, result.isTypeSubstituted());
	}

	/**
	 * Test create name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateName_1()
		throws Exception {
		ObjectFactory fixture = new ObjectFactory();
		String value = "";

		JAXBElement<String> result = fixture.createName(value);

		assertNotNull(result);
		assertEquals("", result.getValue());
		assertEquals(false, result.isNil());
		assertEquals(true, result.isGlobalScope());
		assertEquals(false, result.isTypeSubstituted());
	}

	/**
	 * Test create property_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateProperty_1()
		throws Exception {
		ObjectFactory fixture = new ObjectFactory();

		Property result = fixture.createProperty();

		assertNotNull(result);
		assertEquals("Property [name=null, value=null, description=null, getName()=null, getValue()=null, getDescription()=null]", result.toString());
		assertEquals(null, result.getName());
		assertEquals(null, result.getValue());
		assertEquals(null, result.getDescription());
	}

	/**
	 * Test create value_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testCreateValue_1()
		throws Exception {
		ObjectFactory fixture = new ObjectFactory();
		String value = "";

		JAXBElement<String> result = fixture.createValue(value);

		assertNotNull(result);
		assertEquals("", result.getValue());
		assertEquals(false, result.isNil());
		assertEquals(true, result.isGlobalScope());
		assertEquals(false, result.isTypeSubstituted());
	}

}
