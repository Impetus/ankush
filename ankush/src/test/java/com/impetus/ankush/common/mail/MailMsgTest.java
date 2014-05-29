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
 * The Class MailMsgTest.
 */
public class MailMsgTest {

	private static final String TEST_VALUE = "test-value";
	private static final String FROM = "from_test@abc.com";
	private static final String TO = "to_test@def.com";
	private static final String CC = "cc_test@ghi.com";
	private static final String BCC = "bcc_test@jkl.com";
	private static final String SUBJECT = "Subject Test";
	private static final String MESSAGE = "Test Msg";
	private static final String CONTENT_TYPE = "text/plain";

	private MailMsg getMailMsg() {
		MailMsg mailMsg = new MailMsg();
		mailMsg.setFrom(FROM);
		mailMsg.setTo(TO);
		mailMsg.setCc(CC);
		mailMsg.setBcc(BCC);
		mailMsg.setSubject(SUBJECT);
		mailMsg.setMessage(MESSAGE);
		mailMsg.setContentType(CONTENT_TYPE);
		return mailMsg;
	}

	/**
	 * Test get bcc_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetBcc_1() throws Exception {
		MailMsg fixture = getMailMsg();
		assertEquals(BCC, fixture.getBcc());
	}

	/**
	 * Test get cc_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetCc_1() throws Exception {
		MailMsg fixture = getMailMsg();
		assertEquals(CC, fixture.getCc());
	}

	/**
	 * Test get content type_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetContentType_1() throws Exception {
		MailMsg fixture = getMailMsg();
		assertEquals(CONTENT_TYPE, fixture.getContentType());
	}

	/**
	 * Test get from_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetFrom_1() throws Exception {
		MailMsg fixture = getMailMsg();
		assertEquals(FROM, fixture.getFrom());
	}

	/**
	 * Test get message_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetMessage_1() throws Exception {
		MailMsg fixture = getMailMsg();
		assertEquals(MESSAGE, fixture.getMessage());
	}

	/**
	 * Test get subject_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetSubject_1() throws Exception {
		MailMsg fixture = getMailMsg();
		assertEquals(SUBJECT, fixture.getSubject());
	}

	/**
	 * Test get to_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetTo_1() throws Exception {
		MailMsg fixture = getMailMsg();
		assertEquals(TO, fixture.getTo());
	}

	/**
	 * Test set bcc_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetBcc_1() throws Exception {
		MailMsg fixture = getMailMsg();
		fixture.setBcc(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getBcc());
	}

	/**
	 * Test set cc_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetCc_1() throws Exception {
		MailMsg fixture = getMailMsg();
		fixture.setCc(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getCc());
	}

	/**
	 * Test set content type_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetContentType_1() throws Exception {
		MailMsg fixture = getMailMsg();
		fixture.setContentType(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getContentType());
	}

	/**
	 * Test set from_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetFrom_1() throws Exception {
		MailMsg fixture = getMailMsg();
		fixture.setFrom(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getFrom());
	}

	/**
	 * Test set message_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetMessage_1() throws Exception {
		MailMsg fixture = getMailMsg();
		fixture.setMessage(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getMessage());
	}

	/**
	 * Test set subject_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetSubject_1() throws Exception {
		MailMsg fixture = getMailMsg();
		fixture.setSubject(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getSubject());
	}

	/**
	 * Test set to_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetTo_1() throws Exception {
		MailMsg fixture = getMailMsg();
		fixture.setTo(TEST_VALUE);
		assertEquals(TEST_VALUE, fixture.getTo());
	}
}
