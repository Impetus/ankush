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

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush2.constant.Constant;

public class HostOperation {

	public static String getMachineHostName(String host, String username,
			String password, String privateKey) {

		SSHExec connection = null;
		try {
			connection = SSHUtils.connectToNode(host, username, password,
					privateKey);
			if (connection == null) {
				return "";
			}
			CustomTask task = new ExecCommand(
					Constant.SystemCommands.GETHOSTNAME);
			Result res = connection.exec(task);
			if (res.isSuccess) {
				if (!res.sysout.isEmpty()) {
					return res.sysout.trim().replaceAll("\n", "");
				}
			}
		} catch (Exception e) {
			return "";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return "";
	}

}
