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
package com.impetus.ankush.common.mail;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class MailConfTest.
 */
public class MailConfTest {

	private static final String TEST_VALUE = "test-value";
	private static final String SERVER = "192.168.161.49";
	private static final String USERNAME = "ankush@ankush.in";
	private static final String PASSWORD = "ankushpw";
	private static final int PORT = 25;
	private static final boolean SECURED = true;

	private MailConf getMailConf() {
		MailConf mailConf = new MailConf();
		mailConf.setServer(SERVER);
		mailConf.setUserName(USERNAME);
		mailConf.setPassword(PASSWORD);
		mailConf.setPort(PORT);
		mailConf.setSecured(SECURED);
		return mailConf;
	}

	/**
	 * Test get password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPassword_1() throws Exception {
		MailConf fixture = getMailConf();
		assertEquals(PASSWORD, fixture.getPassword());
	}

	/**
	 * Test get port_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetPort_1() throws Exception {
		MailConf fixture = getMailConf();
		assertEquals(PORT, fixture.getPort());
	}

	/**
	 * Test get server_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetServer_1() throws Exception {
		MailConf fixture = getMailConf();
		assertEquals(SERVER, fixture.getServer());
	}

	/**
	 * Test get user name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetUserName_1() throws Exception {
		MailConf fixture = getMailConf();
		assertEquals(USERNAME, fixture.getUserName());
	}

	/**
	 * Test is secured_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsSecured_1() throws Exception {
		MailConf fixture = getMailConf();
		assertEquals(SECURED, fixture.isSecured());
	}

	/**
	 * Test set password_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetPassword_1() throws Exception {
		MailConf fixture = getMailConf();
		fixture.setPassword(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getPassword());
	}

	/**
	 * Test set port_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetPort_1() throws Exception {
		MailConf fixture = getMailConf();
		int newPort = PORT * 10;
		fixture.setPort(newPort);
		assertEquals(newPort, fixture.getPort());
	}

	/**
	 * Test set secured_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetSecured_1() throws Exception {
		MailConf fixture = getMailConf();
		boolean secured = !SECURED;
		fixture.setSecured(secured);
		assertEquals(secured, fixture.isSecured());
	}

	/**
	 * Test set server_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetServer_1() throws Exception {
		MailConf fixture = getMailConf();
		fixture.setServer(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getServer());
	}

	/**
	 * Test set user name_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetUserName_1() throws Exception {
		MailConf fixture = getMailConf();
		fixture.setUserName(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getUserName());
	}
}
