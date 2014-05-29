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
package com.impetus.ankush.common.utils;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * The Class XmlUtilTest.
 */
public class XmlUtilTest {

	/**
	 * Test xml util_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testXmlUtil_1() throws Exception {
		XmlUtil result = new XmlUtil();
		assertNotNull(result);
	}

	/**
	 * Gets the app config.
	 *
	 * @param type the type
	 * @return the app config
	 */
	private static String getAppConfig(String type) {
		// getting configuration file
		Resource resource = new ClassPathResource("/ankush-" + type
				+ "-config.xml");

		try {
			return resource.getFile().getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
