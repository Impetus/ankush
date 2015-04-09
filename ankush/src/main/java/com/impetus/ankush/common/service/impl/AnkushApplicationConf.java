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
/**
 * 
 */
package com.impetus.ankush.common.service.impl;

import java.util.List;

import com.impetus.ankush.common.domain.User;

/**
 * The Class AnkushApplicationConf.
 *
 * @author bgunjan
 */
public class AnkushApplicationConf {

	/** The email. */
	private Object email;
	
	/** The server ip. */
	private Object serverIP;
	
	/** The users. */
	private List<User> users;
	
	/** The logged user. */
	private User loggedUser;
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public Object getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the email to set
	 */
	public void setEmail(Object email) {
		this.email = email;
	}
	
	/**
	 * Gets the server ip.
	 *
	 * @return the serverIP
	 */
	public Object getServerIP() {
		return serverIP;
	}
	
	/**
	 * Sets the server ip.
	 *
	 * @param serverIP the serverIP to set
	 */
	public void setServerIP(Object serverIP) {
		this.serverIP = serverIP;
	}
	
	/**
	 * Gets the users.
	 *
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}
	
	/**
	 * Sets the users.
	 *
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	/**
	 * Gets the logged user.
	 *
	 * @return the loggedUser
	 */
	public User getLoggedUser() {
		return loggedUser;
	}
	
	/**
	 * Sets the logged user.
	 *
	 * @param loggedUser the loggedUser to set
	 */
	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}
	
}
