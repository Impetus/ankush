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

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.scripting.impl.FileExists;
import com.impetus.ankush.common.scripting.impl.UrlExists;
import com.impetus.ankush.common.utils.AnkushLogger;

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
			
			// Check if directory already exist
			Result result = connection.exec(new ExecCommand("[ -a \""
					+ directory + "\" ]"));

			if (result.isSuccess) {
				// Return false if ignoreIfExist flag is not set.
				if (!ignoreIfExist) {
					errMsg = directory
							+ " already exist. Please remove it manually.";
					throw new Exception(errMsg);
				}

				// Check for write permission on existing directory.
				result = connection.exec(new ExecCommand("[ -w \"" + directory
						+ "\" ]"));
				if (!result.isSuccess) {
					errMsg = "No write permission on " + directory
							+ ". Please check it manually.";
				}else{
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
	 * @param errMsg the err msg
	 * @param status the status
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
	 * @param connection the connection
	 * @param url the url
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

}
