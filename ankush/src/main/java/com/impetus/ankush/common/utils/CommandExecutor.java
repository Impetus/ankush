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

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * Command Executor executes commands.
 * 
 * @author nikunj
 * 
 */
public class CommandExecutor {
	
	/**
	 * Exec.
	 *
	 * @param command the command
	 * @param out the out
	 * @param err the err
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static int exec(String command, OutputStream out, OutputStream err)
			throws IOException, InterruptedException {
		CommandLine cmdLine = CommandLine.parse(command);
		Executor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(out, err, null));
		return executor.execute(cmdLine);
	}

	/**
	 * Exec.
	 *
	 * @param command the command
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static int exec(String command) throws IOException,
			InterruptedException {
		return exec(command, null, null);
	}
}
