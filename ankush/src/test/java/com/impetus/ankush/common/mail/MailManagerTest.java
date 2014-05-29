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

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import com.impetus.ankush.common.mail.MailConf;
import com.impetus.ankush.common.mail.MailManager;
import com.impetus.ankush.common.mail.MailMsg;

/**
 * The Class MailManagerTest.
 */
public class MailManagerTest {
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

	private MailManager getMailManager() {
		MailManager mailManager = new MailManager(getMailConf());
		return mailManager;
	}

	/**
	 * Test mail manager_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testMailManager_1() throws Exception {
		MailManager result = getMailManager();
		assertNotNull(result);
	}

	/**
	 * Test mail manager_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testMailManager_2() throws Exception {
		MailConf mailConf = getMailConf();
		mailConf.setUserName("ankushAdmin");
		MailManager result = new MailManager(mailConf);
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
		MailManager fixture = getMailManager();
		MailMsg mailMsg = getMailMsg();
		boolean result = fixture.sendMail(mailMsg);

		System.out.println(result);
		assertEquals(true, result);
	}

	/**
	 * Test send mail_2.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSendMail_2() throws Exception {
		MailConf mailConf = getMailConf();
		mailConf.setSecured(true);
		MailManager fixture = new MailManager(mailConf);
		MailMsg mailMsg = getMailMsg();
		boolean result = fixture.sendMail(mailMsg);
		assertEquals(false, result);
	}
	
	/**
	 * Test send system mail_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSendSystemMail_1() throws Exception {
		MailManager fixture = getMailManager();
		MailMsg mailMsg = getMailMsg();
		boolean result = fixture.sendSystemMail(mailMsg);
		assertEquals(true, result);
	}
}
