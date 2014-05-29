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

/**
 * The Class JavaValidator.
 *
 * @author nikunj
 */
public class JavaValidator implements Validator {
	
	/** The err msg. */
	private String errMsg;
	
	/** The connection. */
	private SSHExec connection;
	
	/**
	 * Instantiates a new java validator.
	 *
	 * @param connection the connection
	 */
	public JavaValidator(SSHExec connection) {
		this.connection = connection;
		this.errMsg = null;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.utils.validator.Validator#validate()
	 */
	@Override
	public boolean validate() {
		CustomTask command = new ExecCommand("which java");
		try {
			Result result = connection.exec(command);
			if(result.isSuccess){
				return true;
			}
			command = new ExecCommand("echo Could not find Java in $PATH. Please install it manually.");
			result = connection.exec(command);
			errMsg = result.sysout;
		} catch (TaskExecFailException e) {
			errMsg = e.getMessage();
		} catch (Exception e){
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
