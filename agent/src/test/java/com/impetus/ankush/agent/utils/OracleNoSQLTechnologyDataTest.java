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
package com.impetus.ankush.agent.utils;

import org.junit.*;

import com.impetus.ankush.agent.oracle.OracleNoSQLTechnologyData;

import static org.junit.Assert.*;

/**
 * The class <code>OracleNoSQLTechnologyDataTest</code> contains tests for the
 * class <code>{@link OracleNoSQLTechnologyData}</code>.
 */
public class OracleNoSQLTechnologyDataTest {
	/**
	 * Run the OracleNoSQLTechnologyData(String,int) constructor test.
	 */
	@Test
	public void testOracleNoSQLTechnologyData_1() {
		String hostname = "";
		int registryPort = 1;

		OracleNoSQLTechnologyData result = new OracleNoSQLTechnologyData(
				hostname, registryPort);

		assertNotNull(result);
	}

	/**
	 * Run the OracleNoSQLTechnologyData(String,int) constructor test.
	 */
	@Test
	public void testOracleNoSQLTechnologyData_2() {
		String hostname = "localhost";
		int registryPort = 5000;

		OracleNoSQLTechnologyData result = new OracleNoSQLTechnologyData(
				hostname, registryPort);

		assertNotNull(result);
	}

}
