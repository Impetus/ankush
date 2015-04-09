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
package com.impetus.ankush2.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.neoremind.sshxcute.core.ConnBean;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Untar;
import com.impetus.ankush.common.scripting.impl.Unzip;
import com.impetus.ankush.common.scripting.impl.Wget;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.SSHConnection;
import com.impetus.ankush2.agent.AgentConstant;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.AuthConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.ComponentConfig.SourceType;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.ganglia.GangliaConstants;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.logger.AnkushLogger;

public class SSHUtils {
	private static AnkushLogger logger = new AnkushLogger(SSHUtils.class);

	public static SSHExec connectToNode(String host, AuthConfig authConfig) {
		if (authConfig.isUsingPassword()) {
			return connectToNode(host, authConfig.getUsername(),
					authConfig.getPassword(), null);
		} else {
			return connectToNode(host, authConfig.getUsername(), null,
					authConfig.getPrivateKey());
		}
	}

	public static SSHExec connectToNode(String node, String username,
			String password, String privateKey) {
		try {
			// create connection bean
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

	public static boolean getAndExtractComponent(SSHExec connection,
			ComponentConfig conf, String componentName) throws Exception {
		String bundlePath = new String();
		String componentExtension = null;

		try {
			AnkushTask makeHomeDir = new MakeDirectory(conf.getHomeDir());
			if (!connection.exec(makeHomeDir).isSuccess) {
				throw new AnkushException(
						"Could not create home directory for " + componentName
								+ " using command - "
								+ makeHomeDir.getCommand());
			}

			// upload from server to master node
			if (conf.getSourceType().equals(SourceType.UPLOAD)
					&& !conf.getSource().isEmpty()) {

				componentExtension = FileNameUtils.getFileExtension(conf
						.getSource());

				bundlePath = conf.getInstallPath()
						+ FileNameUtils.getName(conf.getSource());
				// Uploading tarball from server to master node
				connection.uploadSingleDataToServer(conf.getSource(),
						bundlePath);
			}

			// tarball exists locally
			if (conf.getSourceType().equals(SourceType.LOCAL)
					&& !conf.getSource().isEmpty()) {

				// getting local tarball path
				componentExtension = FileNameUtils.getFileExtension(conf
						.getSource());
				bundlePath = conf.getSource();
			}

			// download through wget
			if (conf.getSourceType().equals(SourceType.DOWNLOAD)
					&& !conf.getSource().isEmpty()) {

				componentExtension = FileNameUtils.getFileExtension(conf
						.getSource());

				bundlePath = conf.getInstallPath()
						+ FileNameUtils.getName(conf.getSource());

				// Wget tarball
				AnkushTask wgetTask = new Wget(conf.getSource(), bundlePath);
				connection.exec(wgetTask);
			}

			CustomTask task = null;

			if (FileNameUtils.isTarGz(componentExtension)) {
				// untar binary
				task = new Untar(bundlePath, conf.getHomeDir());
			}

			if (FileNameUtils.isZip(componentExtension)) {
				// unzip binary
				task = new Unzip(bundlePath, conf.getHomeDir());
			}

			if (FileNameUtils.isUnsupported(componentExtension)) {
				// Invalid component file
				return false;
			}

			Result res = connection.exec(task);
			if (!res.isSuccess) {
				throw new AnkushException("Could not extract " + componentName
						+ " using command - " + task.getCommand());
			}
			return res.isSuccess;

		} catch (AnkushException e) {
			throw e;
		} catch (TaskExecFailException e) {
			throw new Exception(e.getMessage(), e);
		} catch (Exception e) {
			throw new Exception(e.getMessage(), e);
		}
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

	public static String getFileContents(String filePath, NodeConfig host) {
		String output = null;
		try {
			if (host.getConnection() == null) {
				logger.error("Invalid connection for host - " + host.getHost());
				return null;
			}

			CustomTask task = new ExecCommand("cat " + filePath);
			Result res = host.getConnection().exec(task);
			/* Executing the command. */
			if (res.rc != 0) {
				logger.error("Unable to read file " + filePath + " for host - "
						+ host.getHost());
				return null;
			} else {
				output = res.sysout;
			}
			return output;
		} catch (Exception e) {
			logger.error("Unable to read file " + filePath + " for host - "
					+ host.getHost());
			return null;
		}
	}

	public static String getFileContents(String filePath, SSHExec connection) {
		String output = null;
		try {
			if (connection == null) {
				logger.error("Invalid connection");
				return null;
			}

			CustomTask task = new ExecCommand("cat " + filePath);
			Result res = connection.exec(task);
			/* Executing the command. */
			if (res.rc != 0) {
				logger.error("Unable to read file " + filePath + ".");
				return null;
			} else {
				output = res.sysout;
			}
			return output;
		} catch (Exception e) {
			logger.error("Unable to read file " + filePath + ".");
			return null;
		}
	}

	public static String getCommandOutput(String command, String hostname,
			AuthConfig authConfig) throws Exception {
		String output = null;

		/* Making connection to the node. */
		SSHConnection connection = new SSHConnection(hostname,
				authConfig.getUsername(), authConfig.getPassword(),
				authConfig.getPrivateKey());

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

	public static List<String> listDirectory(String hostname,
			String directoryPath, AuthConfig authConfig) throws Exception {

		// Create ls command
		String lsCommand = "ls " + directoryPath;

		// Executing command and getting the command output
		String commandOutput = SSHUtils.getCommandOutput(lsCommand, hostname,
				authConfig);

		// Create list object
		List<String> paths = new ArrayList<String>();

		// if command output not null.
		if (commandOutput != null) {
			// Splitting the output by '\n'
			String[] fileNameList = commandOutput
					.split(Constant.Strings.LINE_SEPERATOR);

			// Iterating over the filename list.
			paths.addAll(Arrays.asList(fileNameList));
		} else {
			// throwing the exception
			throw new Exception("Unable to connect to node.");
		}
		return paths;
	}

	public static String getFileContents(String filePath, String hostname,
			AuthConfig authConf) throws Exception {
		String output = null;

		// Making the SSH Connection.
		SSHConnection connection = new SSHConnection(hostname,
				authConf.getUsername(), authConf.getPassword(),
				authConf.isUsingPassword());

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

	public static boolean generateRSAKeyFiles(NodeConfig host) {
		try {
			CustomTask task = new ExecCommand(
					HadoopConstants.Command.GENERATE_RSA_KEYS);
			if (host.getConnection().exec(task).rc != 0) {
				logger.error("Unable to generate RSA Keys for host - "
						+ host.getHost());
				return false;
			}
		} catch (Exception e) {
			logger.error("Unable to generate RSA Keys for host - "
					+ host.getHost());
			return false;
		}
		return true;
	}

	public static String getOS(String hostname, String username,
			String authInfo, boolean authUsingPassword) {
		SSHExec connection = null;
		try {
			/* Making connection to the node. */
			if (authUsingPassword) {
				connection = connectToNode(hostname, username, authInfo, null);
			} else {
				connection = connectToNode(hostname, username, null, authInfo);
			}
			if (connection == null) {
				throw new AnkushException(
						"Could not create connection with node to get OS details");
			}
			return getOS(connection, hostname);
		} catch (AnkushException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error("Could not gte OS details.");
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

	/**
	 * Method to get OS Name
	 * 
	 * @param connection
	 *            {@link SSHExec}
	 * @return {@link String}
	 */
	public static String getOS(SSHExec connection, String host)
			throws AnkushException {
		Result rs = null;
		try {
			String command = "cat /etc/*-release";
			CustomTask task = new ExecCommand(command);
			/* Executing the command. */
			rs = connection.exec(task);
			if (rs.rc != 0) {
				throw new AnkushException("Could not execute " + command
						+ " on node " + host + " to find OS.");
			}
			String output = rs.sysout.toLowerCase();
			if (output != null) {
				Pattern pattern = Pattern.compile("NAME=\"(.*?)\"");
				Matcher matcher = pattern.matcher(output);
				if (matcher.find()) {
					return getOSName(matcher.group(1));
				} else {
					return getOSName(output);
				}
			} else {
				throw new AnkushException("Could not get OS name on node : "
						+ host);
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while getting OS detail on node : "
							+ host + "." + GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	private static String getOSName(String output) throws AnkushException {
		if (output.contains("ubuntu")) {
			return "Ubuntu";
		}
		if (output.contains("centos")) {
			return "CentOS";
		}
		if (output.contains("red hat enterprise linux")) {
			return "RHEL";
		}
		if (output.contains("opensuse")) {
			return "openSUSE";
		}
		if (output.contains("fedora")) {
			return "Fedora";
		}
		throw new AnkushException("Could not get OS Name");
	}

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

	public static Map getCommandOutputAndError(String command, String hostName,
			AuthConfig authInfo) {
		Map map = new HashMap();
		String output = null;
		boolean status = false;

		/* Making connection to the node. */
		SSHConnection connection = new SSHConnection(hostName,
				authInfo.getUsername(), authInfo.getPassword(),
				authInfo.isUsingPassword());

		/* Executing the command. */
		if (!connection.exec(command) || connection.getExitStatus() != 0) {
			/* Getting the error from connection object. */
			output = connection.getError();
		} else {
			/* Getting the output from connection object. */
			output = connection.getOutput();
			status = true;
		}
		/* Returning the output of command. */
		map.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, status);
		map.put(com.impetus.ankush2.constant.Constant.Keys.OUTPUT, output);
		return map;
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
	public static boolean addInAgentTaskableFile(SSHExec connection, String className,
			String agentHomeDir) {
		try {
			// JPS service status monitor class name.
			className = Constant.Strings.LINE_SEPERATOR + className;

			// add task information in taskable conf.
			AnkushTask task = new AppendFileUsingEcho(className, agentHomeDir
					+ AgentConstant.Relative_Path.TASKABLE_FILE);
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
