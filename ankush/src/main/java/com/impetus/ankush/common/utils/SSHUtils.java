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
package com.impetus.ankush.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class SSHUtils.
 * 
 * @author mayur
 */
public class SSHUtils {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(SSHUtils.class);

	/** The Constant LS_COMMAND. */
	private static final String LS_COMMAND = "ls ";

	/** The Constant LINE_SEPARATOR. */
	private static final String LINE_SEPARATOR = "\n";

	private static boolean generateRSAKeyFiles(String publicIp,
			String username, String password, String privateKey)
			throws TaskExecFailException {

		CustomTask task = new ExecCommand(
				"[[ -f ~/.ssh/id_rsa ]] || ssh-keygen -q -t rsa -P '' -f ~/.ssh/id_rsa; ");
		SSHExec connection = SSHUtils.connectToNode(publicIp, username,
				password, privateKey);
		if (connection.exec(task).rc != 0) {
			return false;
		}
		if (connection != null) {
			connection.disconnect();
		}
		return true;
	}

	/**
	 * Connect to node.
	 * 
	 * @param node
	 *            the node
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param privateKey
	 *            the private key
	 * @return the sSH exec
	 */
	public static SSHExec connectToNode(String node, String username,
			String password, String privateKey) {
		try {
			// create connection bean
			node = node.trim();

			if (node == null || node.isEmpty()) {
				return null;
			}

			if (username == null || username.isEmpty()) {
				return null;
			}

			ConnBean connectionBean = new ConnBean(node, username, password,
					privateKey);
			// singleton bug fix
			SSHExec connection = new SSHExec(connectionBean);
			// connect to node/machine
			Boolean isConnected = connection.connect();
			if (isConnected) {
				return connection;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Action.
	 * 
	 * @param passwword
	 *            the passwword
	 * @param connection
	 *            the connection
	 * @param commands
	 *            the commands
	 * @return true, if successful
	 * @author hokam chauhan
	 */
	public static boolean action(String passwword, SSHExec connection,
			String... commands) {
		CustomTask execTask = new ExecSudoCommand(passwword, commands);

		boolean done = false;
		try {
			if (connection != null) {
				Result rs = connection.exec(execTask);
				if (rs.rc == 0) {
					done = true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return done;
	}

	/**
	 * Action.
	 * 
	 * @param connection
	 *            the connection
	 * @param commands
	 *            the commands
	 * @return true, if successful
	 * @author hokam chauhan
	 */
	public static boolean action(SSHExec connection, String... commands) {
		CustomTask execTask = new ExecCommand(commands);
		boolean done = false;
		try {
			if (connection != null) {
				Result rs = connection.exec(execTask);
				if (rs.rc == 0) {
					done = true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return done;
	}

	/**
	 * Get the Content of the File.
	 * 
	 * @param filePath
	 *            the file path
	 * @param hostname
	 *            Ip Address of Host
	 * @param username
	 *            User name for the host
	 * @param authInfo
	 *            password/sharedkey
	 * @param authUsingPassword
	 *            Using password or shared key for connection
	 * @return The Content of the file.
	 * @throws Exception
	 *             the exception
	 * @author hokam chauhan
	 */
	public static String getFileContents(String filePath, String hostname,
			String username, String authInfo, boolean authUsingPassword)
			throws Exception {

		String output = null;

		// Making the SSH Connection.
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

		if (!connection.isConnected()) {
			throw new Exception("Unable to connect to node.");
		}

		/* Executing the command. */
		if (connection.exec("cat " + filePath)) {

			if (connection.getExitStatus() == 0) {
				// Getting the output of command.
				output = connection.getOutput();
			} else {
				String error = connection.getError();
				if (error.contains("No such file")) {
					throw new Exception(filePath + " does not exists");
				} else if (error.contains("Permission denied")) {
					throw new Exception("Invalid permissions on " + filePath);
				}
			}
		}
		return output;
	}

	public static String getFileContents(SSHExec connection, String filePath)
			throws Exception {
		String output = null;
		Result res = connection.exec(new ExecCommand("cat " + filePath));
		output = res.sysout;
		return output;
	}

	public static String getFileContents(String filePath, String hostname,
			String username, String password, String privateKeyFilePath)
			throws Exception {

		String output = null;

		// Making the SSH Connection.
		SSHConnection connection = new SSHConnection(hostname, username,
				password, privateKeyFilePath);

		if (!connection.isConnected()) {
			throw new Exception("Unable to connect to node.");
		}

		/* Executing the command. */
		if (connection.exec("cat " + filePath)) {

			if (connection.getExitStatus() == 0) {
				// Getting the output of command.
				output = connection.getOutput();
			} else {
				String error = connection.getError();
				if (error.contains("No such file")) {
					throw new Exception(filePath + " does not exists");
				} else if (error.contains("Permission denied")) {
					throw new Exception("Invalid permissions on " + filePath);
				}
			}
		}
		return output;
	}

	/**
	 * Find out OSName of remote machine.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            password/sharedkey
	 * @param authUsingPassword
	 *            the auth using password
	 * @return the os
	 */
	public static String getOS(String hostname, String username,
			String authInfo, boolean authUsingPassword) {
		String osName = "";
		SSHConnection connection = null;
		try {
			/* Making connection to the node. */
			connection = new SSHConnection(hostname, username, authInfo,
					authUsingPassword);

			// if connected.
			if (connection.isConnected()) {
				osName = "";

				/* Executing the command. */
				if (connection.exec("cat /etc/*-release")) {
					String output = connection.getOutput().toLowerCase();
					if (output != null) {
						if (output.contains("ubuntu")) {
							osName = "Ubuntu";
						}
						if (output.contains("centos")) {
							osName = "CentOS";
						}
						if (output.contains("opensuse")) {
							osName = "openSUSE";
						}
						if (output.contains("fedora")) {
							osName = "Fedora";
						}
						if (output.contains("red hat enterprise linux")) {
							osName = "RHEL";
						}
					}
				}

			}
		} catch (Exception e) {
			return osName;
		}
		return osName;

	}

	/**
	 * Get JavaHome path from remote machine.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            password/sharedkey
	 * @param authUsingPassword
	 *            the auth using password
	 * @return the java home
	 */
	public static String getJavaHome(String hostname, String username,
			String authInfo, boolean authUsingPassword) {

		/* Making connection to the node. */
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

		/* Executing the command. */
		if (connection.exec("readlink -f `which jps`")) {
			String output = connection.getOutput();
			if (output != null) {
				return output.replace("bin/jps", "");
			}
		}
		return null;
	}

	/**
	 * Get JavaHome path from remote machine.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            password/sharedkey
	 * @param authUsingPassword
	 *            the auth using password
	 * @return the java home
	 */
	public static String getJavaVersion(String hostname, String username,
			String authInfo, boolean authUsingPassword) {

		/* Making connection to the node. */
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

		/* Executing the command. */
		if (connection.exec("java -version")) {
			String output = connection.getOutput();
			if (output == null) {
				output = connection.getError();
			}
			if (output != null) {
				String strJavaVersion = output.substring(0,
						output.indexOf("\n"));
				String javaVersion = strJavaVersion.substring(
						strJavaVersion.indexOf("\""),
						strJavaVersion.length() - 1);
				return javaVersion;
			}
		}
		return null;
	}

	public static Map getCommandOutputAndError(String command, String hostname,
			String username, String authInfo, boolean authUsingPassword) {
		Map map = new HashMap();
		String output = null;
		boolean status = false;

		/* Making connection to the node. */
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

		/* Executing the command. */
		if (connection.exec(command)) {
			if (connection.getExitStatus() != 0) {
				output = connection.getError();
			} else {
				/* Getting the output from connection object. */
				output = connection.getOutput();
				status = true;
			}
		} else {
			// command is not executed successfully,getting the Error.
			output = connection.getError();
		}
		/* Returning the output of command. */
		map.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, status);
		map.put(com.impetus.ankush2.constant.Constant.Keys.OUTPUT, output);
		return map;
	}

	/**
	 * Execute the Command and Return the command output.
	 * 
	 * @param command
	 *            the command
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            the auth info
	 * @param authUsingPassword
	 *            the auth using password
	 * @return the command output
	 * @throws Exception
	 *             the exception
	 * @author Hokam Chauhan
	 */
	public static String getCommandOutput(String command, String hostname,
			String username, String authInfo, boolean authUsingPassword)
			throws Exception {
		String output = null;

		/* Making connection to the node. */
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

		/* Executing the command. */
		if (connection.exec(command)) {
			if (connection.getExitStatus() != 0) {
				output = connection.getError();
				throw new Exception(connection.getError());
			}
			/* Getting the output from connection object. */
			output = connection.getOutput();
		}
		/* Returning the output of command. */
		return output;
	}

	/**
	 * Check command.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param privateKey
	 *            the private key
	 * @param command
	 *            the command
	 * @return true, if successful
	 */
	public static boolean getCommandStatus(SSHExec connection, String command) {
		CustomTask task = new ExecCommand(command);
		try {
			if (connection == null) {
				return false;
			}
			Result result = connection.exec(task);
			if (result.rc == 0) {
				return true;
			}
		} catch (TaskExecFailException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * Method to Get the list of path found in remote directory.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            the auth info
	 * @param authUsingPassword
	 *            the auth using password
	 * @param directoryPath
	 *            the directory path
	 * @return The list of paths for files and directories.
	 * @throws Exception
	 *             the exception
	 */
	public static List<String> listDirectory(String hostname, String username,
			String authInfo, boolean authUsingPassword, String directoryPath)
			throws Exception {

		// Create ls command
		String lsCommand = LS_COMMAND + directoryPath;

		// Executing command and getting the command output
		String commandOutput = SSHUtils.getCommandOutput(lsCommand, hostname,
				username, authInfo, authUsingPassword);

		// Create list object
		List<String> paths = new ArrayList<String>();

		// if command output not null.
		if (commandOutput != null) {
			// Splitting the output by '\n'
			String[] fileNameList = commandOutput.split(LINE_SEPARATOR);

			// Iterating over the filename list.
			paths.addAll(Arrays.asList(fileNameList));
		} else {
			// throwing the exception
			throw new Exception("Unable to connect to node.");
		}
		return paths;
	}

	/**
	 * Method to execute list of tasks. If any one fails it return backs the
	 * result of failed task otherwise the last task result.
	 * 
	 * @param tasks
	 * @return
	 */

	public static Result executeTasks(List<AnkushTask> tasks, SSHExec connection) {
		Result result = null;
		try {
			// iterating over the tasks.
			for (AnkushTask task : tasks) {
				// executing command
				result = connection.exec(task);
				// if command execution failed break the for loop
				if (result.rc != 0) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Could not execute the tasks.", e);
		}
		// returning result.
		return result;
	}

	/**
	 * Adds the process info.
	 * 
	 */
	public static boolean addAgentConfig(SSHExec connection, String className) {
		try {
			// line seperator.
			String lineSeperator = "\n";

			// JPS service status monitor class name.
			className = lineSeperator + className;

			// add task information in taskable conf.
			AnkushTask task = new AppendFileUsingEcho(className,
					Constant.Agent.AGENT_TASKABLE_FILE_PATH);
			connection.exec(task);
			return true;
		} catch (TaskExecFailException e) {
			logger.error("Error in adding agent informations..", e);
		} catch (Exception e) {
			logger.error("Error in adding agent informations..", e);
		}
		return false;
	}
}
