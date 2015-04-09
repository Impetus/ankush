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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.impetus.ankush.common.exception.AnkushException;

/**
 * The Class NmapUtilTest.
 */
public class NmapUtilTest {

	/**
	 * Test nmap util_1.
	 */
	@Test
	public void testNmapUtil_1() {
		NmapUtil fixture = new NmapUtil(null);
		Map<String, Boolean> result = null;
		try {
			result = fixture.getNodeListWithStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(fixture);
		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Test nmap util_2.
	 */
	@Test
	public void testNmapUtil_2() {
		String nodePattern = "127.0.0.1";
		NmapUtil fixture = new NmapUtil(nodePattern);
		Map<String, Boolean> result = null;
		try {
			result = fixture.getNodeListWithStatus();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(fixture);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.get(result.keySet().iterator().next()));
	}

}
