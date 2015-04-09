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
package com.impetus.ankush.common.utils.validator;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.logger.AnkushLogger;

public class PathExistenceValidator implements Validator {

	private final AnkushLogger logger = new AnkushLogger(
			PathExistenceValidator.class);

	/** The connection. */
	private SSHExec connection;

	/** The err msg. */
	private String errMsg;

	/** The directory. */
	private String path;

	public PathExistenceValidator(SSHExec connection, String path) {
		this.connection = connection;
		this.path = path;
		this.errMsg = null;
	}

	@Override
	public boolean validate() {
		String errMsg = null;
		boolean status = true;
		try {
			if (connection == null) {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
			// Check if directory already exist
			Result result = connection.exec(new ExecCommand("[ -a \"" + path
					+ "\" ]"));
			// if not success set error message
			if (result.rc != 0 || !result.isSuccess) {
				errMsg = path + " doesn't exist.";
				return false;
			}

		} catch (AnkushException e) {
			errMsg = e.getMessage();
			logger.error(e.getMessage(), e);
			return false;
		} catch (Exception e) {
			errMsg = Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG;
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public String getErrMsg() {
		// TODO Auto-generated method stub
		return this.errMsg;
	}

}
