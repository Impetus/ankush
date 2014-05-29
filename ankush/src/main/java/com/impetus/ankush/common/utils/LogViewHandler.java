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

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.io.FilenameUtils;

import com.impetus.ankush.common.constant.Constant;

/**
 * The Class LogViewHandler.
 * 
 * @author hokam chauhan
 */
public class LogViewHandler {

	/** The username. */
	private String username = new String();

	/** The auth info. */
	private String authInfo = new String();

	/** The hostname. */
	private String hostname = new String();

	/** The password. */
	private String password = new String();

	/** The private key. */
	private String privateKey = new String();

	/** The auth using password. */
	private boolean authUsingPassword = false;

	/**
	 * Instantiates a new log view handler.
	 * 
	 * @param hostname
	 *            Ip Address of Host
	 * @param username
	 *            User name for the host
	 * @param password
	 *            the password
	 * @param privateKey
	 *            the private key
	 */
	public LogViewHandler(String hostname, String username, String password,
			String privateKey) {
		super();
		this.username = username;
		if (password != null && !password.isEmpty()) {
			this.authInfo = password;
			this.authUsingPassword = true;
			this.password = password;
		} else {
			this.authInfo = privateKey;
			this.authUsingPassword = false;
			this.privateKey = privateKey;
		}
		this.hostname = hostname;
	}

	/** The Constant READ_COUNT_KEY. */
	private static final String READ_COUNT_KEY = "readCount";

	/** The Constant CONTENT_KEY. */
	private static final String CONTENT_KEY = "content";

	/** The Constant LS_COMMAND. */
	private static final String LS_COMMAND = "ls ";

	/**
	 * Method to get the list of type files.
	 * 
	 * @param fileNames
	 *            the file names
	 * @param type
	 *            the type
	 * @return the type file names
	 */
	public List<String> getTypeFileNames(List<String> fileNames, String type) {
		// list of type files.
		List<String> typeFiles = new ArrayList<String>();

		// iterate over the file names.
		for (String fileName : fileNames) {
			// split the string using - character.
			if (fileName.contains("-" + type.toLowerCase() + "-")) {
				typeFiles.add(fileName);
			}
		}
		return typeFiles;
	}

	/**
	 * Method to Get the list of path found in remote directory.
	 * 
	 * @param directoryPath
	 *            the directory path
	 * @param type
	 *            the type
	 * @return The list of paths for files and directories.
	 * @throws Exception
	 *             the exception
	 */
	public List<String> listTypeLogDirectory(String directoryPath, String type)
			throws Exception {

		// Create ls command
		String command = "cd \"" + directoryPath + "\"; " + LS_COMMAND
				+ "*-" + type.toLowerCase() + "-*.log*";

		// Executing command and getting the command output
		String commandOutput = SSHUtils.getCommandOutput(command, hostname,
				username, authInfo, authUsingPassword);

		// Create list object
		List<String> paths = new ArrayList<String>();

		// if command output not null.
		if (commandOutput != null) {
			// Splitting the output by '\n'
			String[] fileNameList = commandOutput.split(Constant.LINE_SEPERATOR);
			// adding all filenames to list.
			paths.addAll(Arrays.asList(fileNameList));
		} else {
			// throwing the exception
			throw new Exception("Unable to get the log files.");
		}
		return paths;
	}

	/**
	 * Method to Get the list of path found in remote directory.
	 * 
	 * @param directoryPath
	 *            the directory path
	 * @return The list of paths for files and directories.
	 * @throws Exception
	 *             the exception
	 */
	public List<String> listLogDirectory(String directoryPath) throws Exception {

		// Create ls command
		String lsCommand = "cd \"" + directoryPath + "\"; " + LS_COMMAND
				+ " *.log*";

		/* Create SSH connection */
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

		// if not connected.
		if (!connection.isConnected()) {
			throw new Exception("Unable to connect to node.");
		}

		// if executing command successfully.
		if (connection.exec(lsCommand)) {
			String commandOutput = connection.getOutput();

			// Iterating over the filename list.
			return new ArrayList<String>(Arrays.asList(commandOutput
					.split(Constant.LINE_SEPERATOR)));
		} else {
			new Exception(connection.getError());
		}
		return new ArrayList<String>();
	}

	/**
	 * Get the Content of the File.
	 * 
	 * @param filePath
	 *            Path of the file on remote machine.
	 * @param readCount
	 *            Character count to start reading next string.
	 * @param bytesCount
	 *            the bytes count
	 * @return the file content
	 * @throws Exception
	 *             the exception
	 */
	public Map<String, String> getFileContent(String filePath, int readCount,
			int bytesCount) throws Exception {

		// Create Map.
		Map<String, String> resultMap = new HashMap<String, String>();

		String output = null;
		/* Create SSH connection */
		SSHConnection connection = new SSHConnection(hostname, username,
				authInfo, authUsingPassword);

		/* Execute agent log command. */
		String readLogCmd = "java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar"
				+ " com.impetus.ankush.agent.action.ActionHandler log "
				+ filePath + " " + readCount + " " + bytesCount;

		if (!connection.isConnected()) {
			throw new Exception("Unable to connect to node.");
		}

		if (connection.exec(readLogCmd)) {

			// Getting the output.
			output = connection.getOutput();

			// If command execution is failed.
			if (connection.getExitStatus() != 0 || output == null) {
				throw new Exception(
						"Unable to get log content due to unavailabilty of ankush agent on node.");
			}
			if (!output.equals("0")) {
				readCount = Integer.parseInt(output.substring(0,
						output.indexOf('\n')));
				output = output.substring(output.indexOf('\n'));
			} else {
				readCount = 0;
				output = "";
			}

		}
		// Setting the content in map.
		resultMap.put(CONTENT_KEY, output);

		// Setting the output size in map.
		resultMap.put(READ_COUNT_KEY, readCount + "");

		// Returning the result map.
		return resultMap;
	}

	/**
	 * Method to Get the path of log file for Downloading.
	 * 
	 * @param clusterName
	 *            Name of the cluster.
	 * @param filePath
	 *            Path of the file on remote machine.
	 * @return Path of the log file for Download.
	 * @throws Exception
	 *             the exception
	 */
	public String downloadFile(String clusterName, String filePath)
			throws Exception {

		// Getting the log file content with total read count value.
		SSHExec conn = SSHUtils.connectToNode(hostname, username, authInfo,
				privateKey);

		String command = "java -cp $HOME/.ankush/agent/libs/*:$HOME/.ankush/agent/libs/agent-0.1.jar"
				+ " com.impetus.ankush.agent.action.ActionHandler upload "
				+ filePath;
		ExecCommand task = new ExecCommand(command);
		if (conn != null) {
			Result rs = conn.exec(task);
			if (rs.rc != 0) {
				conn.disconnect();
				throw new Exception(
						"Unable to download log file due unavailability of ankush agent on node.");
			} else {
				conn.disconnect();
				// Getting the file name for file path.
				String fileName = FilenameUtils.getName(filePath);

				// Getting the cluster folder path.
				String downloadPath = "/public/clusters/logs/" + fileName
						+ ".zip";

				// return the down-load path
				return downloadPath;
			}
		}
		// return the down-load path
		throw new Exception("Unable to connect to node.");
	}
}
