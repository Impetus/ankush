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

/**
 * The Class Result.
 * 
 * @author Hokam Chauhan
 */
public class Result {

	/** The command. */
	private String command = "";

	/** The output. */
	private String output = "";

	/** The error. */
	private String error = "";

	/** The exit val. */
	private int exitVal = 1;

	/**
	 * Gets the command.
	 * 
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Sets the command.
	 * 
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Gets the output.
	 * 
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Sets the output.
	 * 
	 * @param output
	 *            the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * Gets the error.
	 * 
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets the error.
	 * 
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Gets the exit val.
	 * 
	 * @return the exitVal
	 */
	public int getExitVal() {
		return exitVal;
	}

	/**
	 * Sets the exit val.
	 * 
	 * @param exitVal
	 *            the exitVal to set
	 */
	public void setExitVal(int exitVal) {
		this.exitVal = exitVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Result [command=" + command + ", output=" + output + ", error="
				+ error + ", exitVal=" + exitVal + "]";
	}

}
