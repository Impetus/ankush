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

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * MailManager is class used at high level to access mail sending related
 * functionality. It is used to send mail based on provided mailConf
 * 
 * @author Vinay Kher
 */

public class MailManager {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(MailManager.class);

	/** used to store mail configuration. */
	private MailConf mailConf;

	/** used to store mailClient associated with this manager instance which actually performs the task of sending mail. */
	private MailClient mailClient;

	/** used to store complete address of from field of mail using from which mail is sent. */
	private String from;

	/**
	 * Instantiates a new mail manager.
	 *
	 * @param mailConf the mail conf
	 */
	public MailManager(MailConf mailConf) {
		this.mailConf = mailConf;
		mailClient = new MailClient(mailConf);
		from = mailConf.getUserName();
		if (!(from.indexOf('@') >= 0)) {
			from += "@" + mailConf.getServer();
		}
	}

	/**
	 * This method is used for sending all auto generated system mails. The
	 * methods automatically adds footer to all outgoing mails
	 * 
	 * @param mailMsg
	 *            mail message for the outgoing mail
	 * @return returns whether mail was send successfully or not
	 */
	public boolean sendSystemMail(MailMsg mailMsg) {
		boolean status = false;
		try {
			mailMsg.setFrom(mailConf.getUserName());
			String message = mailMsg.getMessage() == null ? "" : mailMsg
					.getMessage();
			StringBuilder messageText = new StringBuilder(message);
			messageText.append("\n\n").append(Constant.Mail.FOOTER_MESSAGE);
			mailMsg.setMessage(messageText.toString());
			status = sendMail(mailMsg);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return status;
	}

	/**
	 * This method is responsible for sending mail. It offers high level mail
	 * sending functionality by hiding all complexities related with mailMsg &
	 * MailClient
	 * 
	 * @param mailMsg
	 *            mail message for the outgoing mail
	 * @return returns whether mail was send successfully or not
	 */
	public boolean sendMail(MailMsg mailMsg) {
		boolean status = false;
		try {
			status = mailClient.sendMail(mailMsg);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return status;
	}
}
