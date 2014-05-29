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
import net.neoremind.sshxcute.task.impl.ExecCommand;

/**
 * The Class DirectoryValidator.
 *
 * @author nikunj
 */
public class DirectoryValidator implements Validator {
	
	/** The connection. */
	private SSHExec connection;
	
	/** The err msg. */
	private String errMsg;
	
	/** The directory. */
	private String directory;
	
	/** The ignore if exist. */
	private boolean ignoreIfExist;

	/**
	 * Instantiates a new directory validator.
	 *
	 * @param connection The SSH connection object
	 * @param directory The name of directory which needs to be check on remote
	 * machine.
	 * @param ignoreIfExist Flag to ignore directory if exist.
	 */
	public DirectoryValidator(SSHExec connection, String directory,
			boolean ignoreIfExist) {
		super();
		this.connection = connection;
		this.directory = directory;
		this.ignoreIfExist = ignoreIfExist;
		this.errMsg = null;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.utils.validator.Validator#validate()
	 */
	@Override
	public boolean validate() {
		try {
			// Check if directory already exist
			Result result = connection.exec(new ExecCommand("[ -a \""
					+ directory + "\" ]"));
			if (result.isSuccess) {
				// Return false if ignoreIfExist flag is not set.
				if (!ignoreIfExist) {
					errMsg = directory
							+ " already exist. Please remove it manually.";
					return false;
				}

				// Check for write permission on existing directory.
				result = connection.exec(new ExecCommand("[ -w \"" + directory
						+ "\" ]"));
				if (!result.isSuccess) {
					errMsg = "No write permission on " + directory
							+ ". Please check it manually.";
					return false;
				}
				return true;
			}

			// Create directory
			result = connection.exec(new ExecCommand("mkdir -p \"" + directory
					+ "\"", "rm -r \"" + directory + "\""));
			if (result.isSuccess) {
				return true;
			}
			errMsg = "No write permission to create " + directory
					+ ". Please check it manually.";
		} catch (TaskExecFailException e) {
			errMsg = e.getMessage();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.utils.validator.Validator#getErrMsg()
	 */
	@Override
	public String getErrMsg() {
		return errMsg;
	}

}
