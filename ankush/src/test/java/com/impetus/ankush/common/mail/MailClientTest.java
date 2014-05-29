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
 * The Class MailClientTest.
 */
public class MailClientTest {

	private MailMsg getMailMsg() {
		MailMsg mailMsg = new MailMsg();
		mailMsg.setTo("ankushadmin@impetus.in");
		mailMsg.setSubject("Test Mail from MailManagerTest");
		mailMsg.setMessage("New Test message");
		mailMsg.setFrom("ankushadmin@impetus.in");
		return mailMsg;
	}

	private MailConf getMailConf() {
		final String server = "192.168.161.49";
		final String userName = "ankushadmin@impetus.in";
		final String password = "ankush2012";
		final int port = 25;
		MailConf mailConf = new MailConf();
		mailConf.setServer(server);
		mailConf.setUserName(userName);
		mailConf.setPassword(password);
		mailConf.setPort(port);
		return mailConf;
	}

	/**
	 * Test mail client_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testMailClient_1() throws Exception {
		MailClient result = new MailClient(getMailConf());
		assertNotNull(result);
	}

	/**
	 * Test mail client_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testMailClient_2() throws Exception {
		MailConf mailConf = getMailConf();
		mailConf.setSecured(true);
		MailClient result = new MailClient(mailConf);
		assertNotNull(result);
	}
	
	/**
	 * Test send mail_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSendMail_1() throws Exception {
		MailClient fixture = new MailClient(getMailConf());
		boolean result = fixture.sendMail(getMailMsg());
		assertTrue(result);
	}
	
	/**
	 * Test send mail_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSendMail_2() throws Exception {
		MailClient fixture = new MailClient(getMailConf());
		MailMsg mailMsg = getMailMsg();
		mailMsg.setFrom(null);
		boolean result = fixture.sendMail(mailMsg);
		assertTrue(result);
	}
	
	/**
	 * Test send mail_3.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSendMail_3() throws Exception {
		MailConf mailConf = getMailConf();
		mailConf.setSecured(true);
		MailClient fixture = new MailClient(mailConf);
		boolean result = fixture.sendMail(getMailMsg());
		assertFalse(result);
	}
}
