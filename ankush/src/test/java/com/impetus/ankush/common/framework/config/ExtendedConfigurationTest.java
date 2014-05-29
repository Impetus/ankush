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
package com.impetus.ankush.common.framework.config;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class ExtendedConfigurationTest.
 */
public class ExtendedConfigurationTest {

	/**
	 * Test default extended configuration.
	 */
	@Test
	public void testDefaultExtendedConfiguration() throws Exception {

		ExtendedConfiguration result = new ExtendedConfiguration();

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getState());
		assertEquals(null, result.getPassword());
		assertEquals(null, result.getPrivateKey());
		assertEquals(null, result.getOperationId());
		assertEquals(null, result.getUsername());
		assertEquals(null, result.getClusterId());
	}

	/**
	 * Test extended configuration.
	 */
	@Test
	public void testExtendedConfiguration() {
		String username = "user";
		String privateKey = "path to private key";
		String password = "password";
		Long clusterId = Long.valueOf(2);
		Long operationId = Long.valueOf(3);
		String state = "Error";

		ExtendedConfiguration result = new ExtendedConfiguration();
		result.setClusterId(clusterId);
		result.setOperationId(operationId);
		result.setPassword(password);
		result.setPrivateKey(privateKey);
		result.setState(state);
		result.setUsername(username);

		// add additional test code here
		assertNotNull(result);
		assertEquals(state, result.getState());
		assertEquals(password, result.getPassword());
		assertEquals(privateKey, result.getPrivateKey());
		assertEquals(operationId, result.getOperationId());
		assertEquals(username, result.getUsername());
		assertEquals(clusterId, result.getClusterId());
	}
}
