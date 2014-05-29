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
 * The Class AuthConfTest.
 */
public class AuthConfTest {

	/**
	 * Test auth conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testAuthConf_1() throws Exception {
		AuthConf result = new AuthConf();
		assertNotNull(result);
	}

	/**
	 * Test get password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPassword_1() throws Exception {
		AuthConf fixture = getAuthConf();

		String result = fixture.getPassword();

		assertEquals("password", result);
	}

	/**
	 * @return
	 */
	private AuthConf getAuthConf() {
		AuthConf fixture = new AuthConf();
		fixture.setType("password");
		fixture.setPassword("password");
		fixture.setUsername("username");
		return fixture;
	}

	/**
	 * Test get type_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetType_1() throws Exception {
		AuthConf fixture = getAuthConf();

		String result = fixture.getType();

		assertEquals("password", result);
	}

	/**
	 * Test get username_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetUsername_1() throws Exception {
		AuthConf fixture = getAuthConf();

		String result = fixture.getUsername();

		assertEquals("username", result);
	}

	/**
	 * Test is auth type password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsAuthTypePassword_1() throws Exception {
		AuthConf fixture = new AuthConf();
		fixture.setType((String) null);
		fixture.setPassword("xyz");
		fixture.setUsername("user");

		boolean result = fixture.isAuthTypePassword();

		assertEquals(false, result);
	}

	/**
	 * Test is auth type password_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsAuthTypePassword_2() throws Exception {
		AuthConf fixture = new AuthConf();
		fixture.setType("password");
		fixture.setPassword("xyz");
		fixture.setUsername("user");

		boolean result = fixture.isAuthTypePassword();

		assertEquals(true, result);
	}

	/**
	 * Test set password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetPassword_1() throws Exception {
		AuthConf fixture = getAuthConf();
		String password = "password";

		fixture.setPassword(password);
		assertEquals(password, fixture.getPassword());
	}

	/**
	 * Test set type_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetType_1() throws Exception {
		AuthConf fixture = getAuthConf();
		String type = "password";

		fixture.setType(type);
		assertEquals(type, fixture.getType());
	}

	/**
	 * Test set username_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetUsername_1() throws Exception {
		AuthConf fixture = getAuthConf();
		String username = "user";

		fixture.setUsername(username);
		assertEquals(username, fixture.getUsername());
	}
}
