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
package com.impetus.ankush.common.utils.validator;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.impl.FileExists;
import com.impetus.ankush.common.scripting.impl.UrlExists;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JmxUtil;

/**
 * This utility class pefrorms the following validations: 1. Path existence
 * validation. 2. Path Permission validation.
 * 
 * @author hokam
 */
public class ValidationUtility {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			ValidationUtility.class);

	/** The VALIDATING string. */
	private static final String VALIDATING = "Validating ";

	/** The Constant ERROR. */
	private static final String ERROR = "error";

	/**
	 * Method isFileExists.
	 * 
	 * @param connection
	 *            SSHExec
	 * @param filePath
	 *            String
	 * @return Map
	 */
	public static ValidationResult isFileExists(SSHExec connection,
			String filePath) {
		String errMsg = null;
		boolean status = false;

		CustomTask command = new FileExists(filePath);
		try {
			Result result = connection.exec(command);
			if (result.rc == 0) {
				status = true;
			} else {
				errMsg = "Could not find file " + filePath
						+ ". Please specify the correct path.";
			}
		} catch (TaskExecFailException e) {
			errMsg = e.getMessage();
		} catch (Exception e) {
			errMsg = e.getMessage();
		}
		return getResultMap(errMsg, status);
	}

	/**
	 * Method isValidPermissions.
	 * 
	 * @param connection
	 *            SSHExec
	 * @param directory
	 *            String
	 * @param ignoreIfExist
	 *            boolean
	 * @return Map
	 */
	public static ValidationResult validatePathPermissions(SSHExec connection,
			String directory, boolean ignoreIfExist) {
		String errMsg = null;
		boolean status = false;
		try {

			if (directory != null && (!directory.isEmpty())) {
				// Check if directory already exist
				Result result = connection.exec(new ExecCommand("[ -a \""
						+ directory + "\" ]"));
				System.out.println("result.isSuccess=" + result.isSuccess
						+ "& result.rc=" + result.rc);
				if (result.isSuccess) {
					// Return false if ignoreIfExist flag is not set.
					if (!ignoreIfExist) {
						errMsg = directory
								+ " already exist. Please remove it manually.";
						throw new Exception(errMsg);
					}

					// Check for write permission on existing directory.
					result = connection.exec(new ExecCommand("[ -w \""
							+ directory + "\" ]"));
					if (!result.isSuccess) {
						errMsg = "No write permission on " + directory
								+ ". Please check it manually.";
					} else {
						status = true;
					}
				} else {
					// Create directory
					result = connection.exec(new ExecCommand("mkdir -p \""
							+ directory + "\"", "rm -r \"" + directory + "\""));
					if (!result.isSuccess) {
						errMsg = "No write permission to create " + directory
								+ ". Please check it manually.";
					} else {
						status = true;
					}
				}
			}

		} catch (TaskExecFailException e) {
			errMsg = e.getMessage();
		} catch (Exception e) {
			errMsg = e.getMessage();
		}
		return getResultMap(errMsg, status);
	}

	/**
	 * Gets the result map.
	 * 
	 * @param errMsg
	 *            the err msg
	 * @param status
	 *            the status
	 * @return Map
	 */
	private static ValidationResult getResultMap(String errMsg, boolean status) {
		ValidationResult result = new ValidationResult();
		result.setStatus(status);
		result.setMessage(errMsg);
		return result;
	}

	/**
	 * Method isValidPort.
	 * 
	 * @param connection
	 *            the connection
	 * @param url
	 *            the url
	 * @return Map
	 */
	public static ValidationResult validateDownloadUrl(SSHExec connection,
			String url) {
		String errMsg = null;
		boolean status = false;
		CustomTask task = new UrlExists(url);
		try {
			Result rs = connection.exec(task);
			if (rs.rc == 0) {
				status = true;
			} else {
				errMsg = "Unable to access the download URL " + url;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return getResultMap(errMsg, status);
	}

	/**
	 * Validate directory existence.
	 * 
	 * @param connection
	 *            the connection
	 * @param directoryPath
	 *            the directory path
	 * @return the validation result
	 */
	public static ValidationResult validatePathExistence(SSHExec connection,
			String directoryPath) {
		String errMsg = null;
		boolean status = true;
		try {

			// Check if directory already exist
			Result result = connection.exec(new ExecCommand("[ -a \""
					+ directoryPath + "\" ]"));
			// if not success set error message
			if (!result.isSuccess) {
				status = false;
				errMsg = directoryPath + " doesn't exist.";
			}
		} catch (Exception e) {
			errMsg = e.getMessage();
		}
		return getResultMap(errMsg, status);
	}

	/**
	 * Validate directory existence.
	 * 
	 * @param connection
	 *            the connection
	 * @param path
	 *            the directory path
	 * @return the validation result
	 */
	public static ValidationResult validatePathPermissions(SSHExec connection,
			String path) {
		ValidationResult status = null;
		// Validating the installation path for the component.
		status = ValidationUtility.validatePathPermissions(connection, path,
				true);
		return status;
	}

	/**
	 * Validate permissions on given path.
	 * 
	 * @param componentName
	 * @param connection
	 * @param compInfo
	 * @param node
	 */
	public static boolean validatePathPermissions(SSHExec connection,
			String componentName, GenericConfiguration compInfo, NodeConf node) {
		// Setting logger config
		logger.setLoggerConfig(compInfo);
		logger.info(node.getPublicIp(), VALIDATING + componentName
				+ " installation path...");
		// validation result.
		ValidationResult status = null;
		// Validating the installation path for the component.
		status = ValidationUtility.validatePathPermissions(connection,
				compInfo.getInstallationPath(), true);

		node.setStatus(status.isStatus());
		if (!status.isStatus()) {
			// conf.setState(ERROR);
			node.setNodeState(ERROR);
			// node.setStatus(false);
			node.addError(componentName + "_installationPath",
					status.getMessage());
			logger.error(node, VALIDATING + componentName
					+ " installation path failed...");
		}
		return node.getStatus();
	}

	/**
	 * 
	 * @param connection
	 * @param componentName
	 * @param compInfo
	 * @param node
	 */
	public static boolean validatingComponentPaths(SSHExec connection,
			String componentName, GenericConfiguration compInfo, NodeConf node) {
		logger.setLoggerConfig(compInfo);
		logger.setCluster(compInfo.getClusterConf());
		ValidationResult status = null;
		// Validating the bundle path for the component.
		if (compInfo.getLocalBinaryFile() != null
				&& !compInfo.getLocalBinaryFile().equals("")) {

			logger.info(node.getPublicIp(), VALIDATING + componentName
					+ " local bundle path...");
			status = ValidationUtility.isFileExists(connection,
					compInfo.getLocalBinaryFile());
			node.setStatus(status.isStatus());
			if (!status.isStatus()) {
				compInfo.getClusterConf().setState(ERROR);
				// node.setStatus(false);
				node.setNodeState(ERROR);
				logger.error(node, VALIDATING + componentName
						+ " local bundle path failed...");
				node.addError(componentName + "_localBinaryFile",
						status.getMessage());
			}

		}

		// Validating the tarball url for the component.
		if (compInfo.getTarballUrl() != null
				&& !compInfo.getTarballUrl().equals("")) {

			logger.info(node.getPublicIp(), VALIDATING + componentName
					+ " download url...");
			status = ValidationUtility.validateDownloadUrl(connection,
					compInfo.getTarballUrl());
			node.setStatus(status.isStatus());
			if (!status.isStatus()) {
				// conf.setState(ERROR);
				node.setNodeState(ERROR);
				// node.setStatus(false);
				logger.error(node, VALIDATING + componentName
						+ " download url failed...");
				node.addError(componentName + "_tarballUrl",
						status.getMessage());
			}
		}
		return node.getStatus();
	}

	/**
	 * Method to validate jmx port.
	 * 
	 * @param componentName
	 * @param jmxPort
	 * @param nodeIp
	 * @param config
	 * @return
	 */
	public static boolean validateJMXPort(String componentName, String jmxPort,
			String nodeIp, GenericConfiguration config) {
		// status.
		boolean status = false;
		// return status.
		logger.setLoggerConfig(config);
		// validating jmx port
		logger.info(nodeIp, Constant.VALIDATING + componentName
				+ " jmx port...");
		try {
			// Creating jmx util object
			JmxUtil jmxUtil = new JmxUtil(nodeIp, Integer.parseInt(jmxPort));
			// creating jmx bean object.
			MBeanServerConnection jmxConnection = jmxUtil.connect();
			// if connected.
			if (jmxConnection != null) {
				status = true;
				logger.info(nodeIp, Constant.VALIDATING + componentName
						+ " jmx port passed.");
			} else {
				logger.error(nodeIp, "Couldn't connect with the JMX PORT of "
						+ componentName);
			}
		} catch (AnkushException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}
}
