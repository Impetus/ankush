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

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.validation.constraints.AssertFalse;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.mchange.util.AssertException;

/**
 * The Class ConfigurationReaderTest.
 */
public class ConfigurationReaderTest {

	/**
	 * Test configuration reader_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testConfigurationReader_1() throws Exception {
		Resource rs = new ClassPathResource("/ankush_constants.xml");
		String filePath = rs.getFile().getAbsolutePath();

		ConfigurationReader result = new ConfigurationReader(filePath);

		assertNotNull(result);
	}

	/**
	 * Test get boolean value_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetBooleanValue_1() throws Exception {
		Resource rs = new ClassPathResource("/ankush_constants.xml");
		String filePath = rs.getFile().getAbsolutePath();
		
		ConfigurationReader fixture = new ConfigurationReader(filePath);
		String key = "ankush.connectiontimeout";
		
		assertEquals(true, fixture.getBooleanValue("distributed"));
		assertEquals(5000L, fixture.getLongValue(key));
		assertEquals(5000, fixture.getFloatValue(key), 0.1);
		assertEquals(5000, fixture.getDoubleValue(key), 0.1);
		assertEquals(5000, fixture.getIntValue(key));
		assertEquals("5000", fixture.getStringValue(key));
	}

}
