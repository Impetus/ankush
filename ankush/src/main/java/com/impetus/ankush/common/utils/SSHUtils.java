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
import java.util.Set;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.AuthConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.Untar;
import com.impetus.ankush.common.scripting.impl.Unzip;
import com.impetus.ankush.common.scripting.impl.Wget;

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
	 * Setup paswwordless ssh.
	 * 
	 * @param source
	 *            the source
	 * @param destinations
	 *            the destinations
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param privateKey
	 *            the private key
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public static boolean setupPaswwordlessSSH(NodeConf source,
			Set<NodeConf> destinations, String username, String password,
			String privateKey) throws Exception {

		SSHExec connection = null;
		try {
			if (!destinations.contains(source)) {
				destinations.add(source);
			}

			for (NodeConf node : destinations) {
				boolean status = SSHUtils.generateRSAKeyFiles(node.getPublicIp(),
						username, password, privateKey);
				if (!status) {
					return false;
				}
			}

			// connect with from/master node
			connection = SSHUtils.connectToNode(source.getPublicIp(),
					username, password, privateKey);

			String passwordOrKeyForSSH = password;
			boolean authUsingPassword = true;
			if (privateKey != null && !privateKey.isEmpty()) {
				authUsingPassword = false;
				passwordOrKeyForSSH = privateKey;
			}

			String pubKey = SSHUtils.getFileContents("~/.ssh/id_rsa.pub",
					source.getPublicIp(), username, passwordOrKeyForSSH,
					authUsingPassword);

			if(pubKey == null) {
				logger.error(source, "Error : Unable to read ~/.ssh/id_rsa.pub file.");
				return false;
			}
			// add host entry in know host.
			for (NodeConf destination : destinations) {
				String cmdAddToKnowHost = "ssh -o ConnectTimeout=15 -o ConnectionAttempts=5 -o StrictHostKeyChecking=no "
						+ username
						+ "@"
						+ HostOperation.getAnkushHostName(destination
								.getPrivateIp()) + " ls";

				CustomTask task = new ExecCommand(cmdAddToKnowHost);
				connection.exec(task);

				SSHExec conn = SSHUtils.connectToNode(destination.getPublicIp(),
						username, password, privateKey);
				
				task = new ExecCommand("echo \"" + pubKey
						+ "\" >> ~/.ssh/authorized_keys",
						"chmod 0600 ~/.ssh/authorized_keys");

				if (conn.exec(task).rc != 0) {
					logger.error(destination, "Error : Unable to update authorized keys.");
					conn.disconnect();
					connection.disconnect();
					return false;
				} else {
					conn.disconnect();
				}
			}
		} catch (Exception e) {
			logger.error("Error while Passwordless SSH setup, please view ankush server logs for more information.");
			logger.debug(e.getMessage());
			return false;
		} finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}

	/**
	 * Connect to node.
	 * 
	 * @param host
	 *            the host
	 * @param authConf
	 *            the auth conf
	 * @return SSHExec Connection
	 */
	public static SSHExec connectToNode(String host, AuthConf authConf) {
		SSHExec connection;
		if (authConf.isAuthTypePassword()) {
			logger.debug("Type " + authConf.getType());
			connection = connectToNode(host, authConf.getUsername(),
					authConf.getPassword(), null);
		} else {
			logger.debug("Type " + authConf.getType());
			connection = connectToNode(host, authConf.getUsername(), null,
					authConf.getPassword());
		}
		return connection;
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
	 * Gets the and extract component.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @param componentName
	 *            the component name
	 * @return the and extract component
	 */
	public static boolean getAndExtractComponent(SSHExec connection,
			GenericConfiguration conf, String componentName) {
		logger.setLoggerConfig(conf);
		String componentPath = new String();
		String componentExtension = null;

		try {
			// upload from server to master node
			if (conf.getServerTarballLocation() != null
					&& !conf.getServerTarballLocation().isEmpty()) {

				logger.info("Uploading tarball from server to master node...");

				componentExtension = FileNameUtils.getFileExtension(conf
						.getServerTarballLocation());

				componentPath = conf.getInstallationPath()
						+ FileNameUtils
								.getName(conf.getServerTarballLocation());

				connection.uploadSingleDataToServer(
						conf.getServerTarballLocation(), componentPath);
				logger.info("Uploading tarball from server to master node is done...");
			}

			// tarball exists locally
			if (conf.getLocalBinaryFile() != null
					&& !conf.getLocalBinaryFile().isEmpty()) {

				logger.debug("getting local tarball path...");

				componentExtension = FileNameUtils.getFileExtension(conf
						.getLocalBinaryFile());
				componentPath = conf.getLocalBinaryFile();
			}

			// download through wget
			if (conf.getTarballUrl() != null && !conf.getTarballUrl().isEmpty()) {

				logger.debug("Downloading tarball from the given Url...");

				componentExtension = FileNameUtils.getFileExtension(conf
						.getTarballUrl());

				componentPath = conf.getInstallationPath()
						+ FileNameUtils.getName(conf.getTarballUrl());

				// Wget tarball
				AnkushTask wgetTask = new Wget(conf.getTarballUrl(),
						componentPath);
				connection.exec(wgetTask);
			}

			CustomTask task = null;

			if (FileNameUtils.isTarGz(componentExtension)) {
				// untar binary
				task = new Untar(componentPath, conf.getInstallationPath());
			}

			if (FileNameUtils.isZip(componentExtension)) {
				// unzip binary
				task = new Unzip(componentPath, conf.getInstallationPath());
			}

			if (FileNameUtils.isUnsupported(componentExtension)) {
				logger.error("Invalid component file ");
				return false;
			}

			Result res = connection.exec(task);
			if (!res.isSuccess) {
				logger.debug("Could not get/extract tarball ...");
			} else {
				logger.debug("Downloading tarball is completed...");
			}
			return res.isSuccess;

		} catch (TaskExecFailException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
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
		String osName = null;
		/* Making connection to the node. */
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

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
			System.out.println("output: " + output);
			if(output == null) {
				output = connection.getError();
			}
			if (output != null) {
				String strJavaVersion = output.substring(0, output.indexOf("\n"));
				String javaVersion = strJavaVersion.substring(strJavaVersion.indexOf("\""), strJavaVersion.length()-1);
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
		map.put(Constant.Keys.STATUS, status);
		map.put(Constant.Keys.OUTPUT, output);
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
			AnkushTask task = new AppendFile(className,
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
