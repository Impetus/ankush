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
package com.impetus.ankush.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.impetus.ankush.AppStoreWrapper;

/**
 * The Class SSHConnection.
 */
public class SSHConnection {

	/** Error Msg for connection problem */
	private static final String CONNECTION_PROBLEM_ERR_MSG = "Could not get connection. ";

	/** Error Msg for Input Stream processing problem */
	private static final String STREAM_PROCESSING_ERR_MSG = "Unable to process InputStream. ";

	/** Error Msg for Command execution problem */
	private static final String CMD_EXECUTION_FAILURE_ERR_MSG = "Could not excute command. ";

	/** The logger. */
	private static final AnkushLogger logger = new AnkushLogger(SSHUtils.class);

	/** The connection. */
	private Connection connection = null;

	/** The error. */
	private String error = null;

	/** The output. */
	private String output = null;

	/** The exit status. */
	private int exitStatus;

	/**
	 * Instantiates a new sSH connection.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param privateKey
	 *            the private key
	 */
	public SSHConnection(String hostname, String username, String password,
			String privateKey) {
		exitStatus = -1;
		int timeout = AppStoreWrapper.getAnkushConfReader().getIntValue(
				"ankush.connectiontimeout");
		connection = new Connection(hostname);
		try {
			connection.connect(null, timeout, timeout);
			if (password == null || password.isEmpty()) {
				if (!connection.authenticateWithPublicKey(username, new File(
						privateKey), null)) {
					connection.close();
					connection = null;
				}
			} else {
				if (!connection.authenticateWithPassword(username, password)) {
					connection.close();
					connection = null;
				}
			}
		} catch (IOException e) {
			logger.error(CONNECTION_PROBLEM_ERR_MSG + e.getMessage(),e);
			closeConnection();
		} catch (Exception e) {
			logger.error(CONNECTION_PROBLEM_ERR_MSG + e.getMessage(),e);
			closeConnection();

		} 
	}

	/**
	 * Instantiates a new sSH connection.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            the auth info
	 * @param authUsingPassword
	 *            the auth using password
	 */
	public SSHConnection(String hostname, String username, String authInfo,
			boolean authUsingPassword) {
		exitStatus = -1;
		int timeout = AppStoreWrapper.getAnkushConfReader().getIntValue(
				"ankush.connectiontimeout");
		// int timeout = 3000;
		connection = new Connection(hostname);
		try {
			connection.connect(null, timeout, timeout);
			if (authUsingPassword) {
				if (!connection.authenticateWithPassword(username, authInfo)) {
					connection.close();
					connection = null;
				}
			} else {
				if (!connection.authenticateWithPublicKey(username, new File(
						authInfo), null)) {
					connection.close();
					connection = null;
				}
			}
		} catch (IOException e) {
			logger.error(CONNECTION_PROBLEM_ERR_MSG + e.getMessage(),e);
			closeConnection();
		} catch (Exception e) {
			logger.error(CONNECTION_PROBLEM_ERR_MSG + e.getMessage(),e);
			closeConnection();
		}
	}

	/**
	 * Close connection.
	 */
	private void closeConnection() {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	/**
	 * Process input stream.
	 * 
	 * @param input
	 *            the input
	 * @return the string
	 */
	private String processInputStream(InputStream input) {
		if (input == null) {
			return null;
		}

		String line = null;
		StringBuilder builder = new StringBuilder();
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(
				new StreamGobbler(input)));
		try {
			line = stdoutReader.readLine();
			if (line != null) {
				builder.append(line);
			}
			while ((line = stdoutReader.readLine()) != null) {
				builder.append("\n").append(line);
			}
		} catch (IOException e) {
			logger.error(STREAM_PROCESSING_ERR_MSG + e.getMessage());
		} catch (Exception e) {
			logger.error(STREAM_PROCESSING_ERR_MSG + e.getMessage());
		}
		if (builder.toString().equals("")) {
			return null;
		}
		return builder.toString();
	}

	/**
	 * Exec.
	 * 
	 * @param command
	 *            the command
	 * @return true, if successful
	 */
	public boolean exec(String command) {
		if (connection == null) {
			logger.error("Connection is empty.");
			return false;
		}

		Session session = null;

		try {
			session = connection.openSession();
			logger.debug("Going to execute command " + command);
			session.execCommand(command);

			Integer exitStatus = session.getExitStatus();
			while (exitStatus == null) {
				exitStatus = session.getExitStatus();
				// Waiting for the complete execution of the command.
			}

			if (session.getStdout() != null) {
				output = processInputStream(session.getStdout());
			}

			if (session.getStderr() != null) {
				error = processInputStream(session.getStderr());
			}
			this.exitStatus = session.getExitStatus();

			if (this.exitStatus == 0) {
				logger.debug("Execute successfully for command: " + command);
				// logger.debug("Command Output: " + output);
			} else {
				logger.debug("Execution failed for command: " + command);
				// logger.debug("Command Output: " + output);
				logger.debug("Command Error: " + error);
			}
		} catch (IOException e) {
			logger.error(CMD_EXECUTION_FAILURE_ERR_MSG + e.getMessage());
		} catch (Exception e) {
			logger.error(CMD_EXECUTION_FAILURE_ERR_MSG + e.getMessage());
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			closeConnection();
		}
		return exitStatus == 0;
	}

	/**
	 * Gets the error.
	 * 
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Gets the output.
	 * 
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Gets the exit status.
	 * 
	 * @return the exitStatus
	 */
	public int getExitStatus() {
		return exitStatus;
	}

	/**
	 * Checks if is connected.
	 * 
	 * @return the connection status
	 */
	public boolean isConnected() {
		return (connection != null);
	}
}
