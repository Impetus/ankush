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

/**
 * MailMsg is the class used to hold the object representation of mail message
 * that will be sent as mail.
 *
 * @author Vinay Kher
 */
public class MailMsg {
	/**
	 * Stores value for the from field of mail.
	 */
	private String from;

	/**
	 * Stores value for the to field of mail. Multiple recipients are separated
	 * by symbol semicolon (;).
	 */
	private String to;

	/**
	 * Stores value for the cc field of mail. Multiple recipients are separated
	 * by symbol semicolon (;).
	 */
	private String cc;

	/**
	 * Stores value for the bcc field of mail. Multiple recipients are separated
	 * by symbol semicolon (;).
	 */
	private String bcc;

	/**
	 * Stores the subject of mail.
	 */
	private String subject;

	/**
	 * Stores the content type for mail.
	 */
	private String contentType;

	/**
	 * Stores the actual message content of mail.
	 */
	private String message;

	/**
	 * Gets the from.
	 *
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 *
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * Gets the cc.
	 *
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * Sets the cc.
	 *
	 * @param cc the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * Gets the bcc.
	 *
	 * @return the bcc
	 */
	public String getBcc() {
		return bcc;
	}

	/**
	 * Sets the bcc.
	 *
	 * @param bcc the bcc to set
	 */
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the content type.
	 *
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 *
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
