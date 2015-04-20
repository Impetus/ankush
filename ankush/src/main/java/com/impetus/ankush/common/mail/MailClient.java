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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.impetus.ankush2.logger.AnkushLogger;

/**
 * MailClient is the class that is used to for sending mails. It uses java mail
 * API for sending mail.
 * 
 * @author Vinay Kher
 */

public class MailClient {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(MailClient.class);
	
	/** Stores the mail session associated with this MailClient that is based on specified mail configuration in constructor argument of MailClient. */
	private Session session;

	/** Stores the mail configuration associated with this MailClient. */
	private MailConf mailConf;

	/**
	 * creates MailClient object with given mail configuration.
	 *
	 * @param mailConf mailConfiguration that will be used by this mail client
	 */
	public MailClient(MailConf mailConf) {
		this.mailConf = mailConf;
		init();
	}

	/**
	 * Initializes various properties related to mailConfiguration based on the
	 * values specified in associated MailConf object.
	 */
	private void init() {
		String un = mailConf.getUserName();
		String pw = mailConf.getPassword();
		boolean anonymous = (un == null || un.equals("")) && (pw == null || pw.equals(""));
		
		boolean debug = false;
		
		Properties props = new Properties();
		props.put("mail.smtp.host", mailConf.getServer());
		props.put("mail.smtp.port", "" + mailConf.getPort());
		props.put("mail.debug", "false");
		if (!anonymous)
			props.put("mail.smtp.auth", "true");
//		else
//			props.put("mail.smtp.auth", "false");

		
		if (mailConf.isSecured()) {
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.socketFactory.port", "" + mailConf.getPort());
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
		}

		if (!anonymous) {
			session = Session.getInstance(props, new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailConf.getUserName(),
							mailConf.getPassword());
				}
			});
		} else {
			//session = Session.getInstance(props, null);
			session = Session.getInstance(props);
		}
		session.setDebug(debug);
	}

	/**
	 * Converts addresses in List<String> to InternetAddress array.
	 *
	 * @param addressLst a List<String> where each item in list corresponds to one mail
	 * address
	 * @return returns array of corresponding InternetAddress for the provided
	 * List<String> argument
	 */
	private InternetAddress[] getInternetAddress(List<String> addressLst) {
		int size = 0;
		if (addressLst != null) {
			size = addressLst.size();
		}
		InternetAddress[] address = new InternetAddress[size];
		for (int i = 0; i < size; i++) {
			try {
				address[i] = new InternetAddress(addressLst.get(i));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return address;
	}

	/**
	 * Converts semicolon delimited address values present in string to
	 * List<String>.
	 *
	 * @param recipients the recipients
	 * @return returns List<String> for the provided String input argument that
	 * may contains multiple addresses delimited by semicolon
	 */
	private List<String> getRecipients(String recipients) {
		List<String> recipientsList = new ArrayList<String>();
		if (recipients != null) {
			StringTokenizer st = new StringTokenizer(recipients, ";");
			while (st.hasMoreTokens()) {
				String address = st.nextToken().trim();
				if (address.length() > 0) {
					recipientsList.add(address);
				}
			}
		}
		return recipientsList;
	}

	/**
	 * This method actually send the mail.
	 *
	 * @param mailMsg the actual mail message content in form of MailMsg object that
	 * includes recipients list, subject & the message
	 * @return returns success status of sending mail
	 * @throws Exception the exception
	 */
	public boolean sendMail(MailMsg mailMsg) throws Exception {
		boolean mailSent = false;
		Message msg = new MimeMessage(session);

		String from = mailMsg.getFrom();

		if (from == null) {
			from = mailConf.getEmailAddress();
		}

		if ((from != null) && (!from.equals(""))) {
			InternetAddress addressFrom = new InternetAddress(from);
			msg.setFrom(addressFrom);
		}

		// convert delimited list to array for to, cc & bcc
		msg.setRecipients(Message.RecipientType.TO,
				getInternetAddress(getRecipients(mailMsg.getTo())));
		msg.setRecipients(Message.RecipientType.CC,
				getInternetAddress(getRecipients(mailMsg.getCc())));
		msg.setRecipients(Message.RecipientType.BCC,
				getInternetAddress(getRecipients(mailMsg.getBcc())));

		msg.setSubject(mailMsg.getSubject());

		String contentType = mailMsg.getContentType();
		if (contentType == null) {
			contentType = "text/plain";
		}
		msg.setContent(mailMsg.getMessage(), contentType);
		try {
			Transport.send(msg);
			mailSent = true;
		} catch (Exception e) {
			logger.error("Error in Sending mail @ Mail client : "
					+ e.getMessage());
			e.printStackTrace();
		}
		return mailSent;
	}
}
