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
package com.impetus.ankush.agent.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

/**
 * The Class CommandExecutor.
 * 
 * @author Hokam Chauhan
 */
public class CommandExecutor {

	/** The Constant SLEEP_TIME. */
	private static final int SLEEP_TIME = 5000;

	/**
	 * Method to execute command arrays.
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Result executeCommand(String... command) throws IOException,
			InterruptedException {
		Result rs = new Result();
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(command);
		StreamWrapper error = new StreamWrapper(proc.getErrorStream());
		StreamWrapper output = new StreamWrapper(proc.getInputStream());

		error.start();
		output.start();
		error.join(SLEEP_TIME);
		output.join(SLEEP_TIME);
		proc.waitFor();
		rs.setExitVal(proc.exitValue());
		rs.setOutput(output.getMessage());
		rs.setError(error.getMessage());
		proc.destroy();
		return rs;
	}

	/**
	 * Method executeCommand.
	 * 
	 * @param command
	 *            String
	 * @return Result
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public static Result executeCommand(String command) throws IOException,
			InterruptedException {
		Result rs = new Result();
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(command);
		StreamWrapper error = new StreamWrapper(proc.getErrorStream());
		StreamWrapper output = new StreamWrapper(proc.getInputStream());

		error.start();
		output.start();
		error.join(SLEEP_TIME);
		output.join(SLEEP_TIME);
		proc.waitFor();
		rs.setExitVal(proc.exitValue());
		rs.setOutput(output.getMessage());
		rs.setError(error.getMessage());
		proc.destroy();
		return rs;
	}

	/**
	 * The Class StreamWrapper.
	 */
	private static class StreamWrapper extends Thread {

		/** The is. */
		private InputStream is = null;

		/** The message. */
		private String message = null;

		/**
		 * Method getMessage.
		 * 
		 * @return String
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Constructor for StreamWrapper.
		 * 
		 * @param is
		 *            InputStream
		 */
		StreamWrapper(InputStream is) {
			this.is = is;
		}

		/**
		 * Method run.
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(is));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = br.readLine()) != null) {
					buffer.append(line).append("\n");
				}
				message = buffer.toString();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (Exception ioe) {
				ioe.printStackTrace();
			} finally {
				if (br != null) {
					IOUtils.closeQuietly(br);
				}
				if (is != null) {
					IOUtils.closeQuietly(is);
				}
			}
		}
	}
}
