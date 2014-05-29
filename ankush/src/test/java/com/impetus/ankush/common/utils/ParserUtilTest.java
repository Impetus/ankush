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
package com.impetus.ankush.common.utils;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class ParserUtilTest.
 */
public class ParserUtilTest {

	/**
	 * Test get int value_1.
	 */
	@Test
	public void testGetIntValue_1() {
		String intStr = "10";
		int defaultVal = 1;

		int result = ParserUtil.getIntValue(intStr, defaultVal);

		assertEquals(10, result);
	}

	/**
	 * Test get long value_1.
	 */
	@Test
	public void testGetLongValue_1() {
		String numStr = "10";
		long defaultVal = 1L;

		long result = ParserUtil.getLongValue(numStr, defaultVal);

		assertEquals(10L, result);
	}

	/**
	 * Test get int value_1.
	 */
	@Test
	public void testGetIntValue_3() {
		String intStr = null;
		int defaultVal = 1;

		int result = ParserUtil.getIntValue(intStr, defaultVal);

		assertEquals(1, result);
	}

	/**
	 * Test get long value_1.
	 */
	@Test
	public void testGetLongValue_4() {
		String numStr = null;
		long defaultVal = 1L;

		long result = ParserUtil.getLongValue(numStr, defaultVal);

		assertEquals(1L, result);
	}

}
