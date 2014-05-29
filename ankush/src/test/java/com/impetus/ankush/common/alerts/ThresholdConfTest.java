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
package com.impetus.ankush.common.alerts;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class ThresholdConfTest.
 */
public class ThresholdConfTest {
	
	/**
	 * Test threshold conf_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testThresholdConf_1()
		throws Exception {
		ThresholdConf result = new ThresholdConf();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Test get alert level_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetAlertLevel_1()
		throws Exception {
		ThresholdConf fixture = new ThresholdConf();
		fixture.setMetricName("");
		fixture.setWarningLevel(new Double(1.0));
		fixture.setAlertLevel(new Double(1.0));

		Double result = fixture.getAlertLevel();

		// add additional test code here
		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals((byte) 1, result.byteValue());
		assertEquals((short) 1, result.shortValue());
		assertEquals(1, result.intValue());
		assertEquals(1L, result.longValue());
		assertEquals(1.0f, result.floatValue(), 1.0f);
		assertEquals(1.0, result.doubleValue(), 1.0);
		assertEquals(false, result.isNaN());
		assertEquals(false, result.isInfinite());
	}

	/**
	 * Test get metric name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetMetricName_1()
		throws Exception {
		ThresholdConf fixture = new ThresholdConf();
		fixture.setMetricName("");
		fixture.setWarningLevel(new Double(1.0));
		fixture.setAlertLevel(new Double(1.0));

		String result = fixture.getMetricName();

		// add additional test code here
		assertEquals("", result);
	}

	/**
	 * Test get warning level_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetWarningLevel_1()
		throws Exception {
		ThresholdConf fixture = new ThresholdConf();
		fixture.setMetricName("");
		fixture.setWarningLevel(new Double(1.0));
		fixture.setAlertLevel(new Double(1.0));

		Double result = fixture.getWarningLevel();

		// add additional test code here
		assertNotNull(result);
		assertEquals("1.0", result.toString());
		assertEquals((byte) 1, result.byteValue());
		assertEquals((short) 1, result.shortValue());
		assertEquals(1, result.intValue());
		assertEquals(1L, result.longValue());
		assertEquals(1.0f, result.floatValue(), 1.0f);
		assertEquals(1.0, result.doubleValue(), 1.0);
		assertEquals(false, result.isNaN());
		assertEquals(false, result.isInfinite());
	}

	/**
	 * Test set alert level_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetAlertLevel_1()
		throws Exception {
		ThresholdConf fixture = new ThresholdConf();
		fixture.setMetricName("");
		fixture.setWarningLevel(new Double(1.0));
		fixture.setAlertLevel(new Double(1.0));
		Double alertLevel = new Double(1.0);

		fixture.setAlertLevel(alertLevel);

		// add additional test code here
	}

	/**
	 * Test set metric name_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetMetricName_1()
		throws Exception {
		ThresholdConf fixture = new ThresholdConf();
		fixture.setMetricName("");
		fixture.setWarningLevel(new Double(1.0));
		fixture.setAlertLevel(new Double(1.0));
		String metricName = "";

		fixture.setMetricName(metricName);

		// add additional test code here
	}

	/**
	 * Test set warning level_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testSetWarningLevel_1()
		throws Exception {
		ThresholdConf fixture = new ThresholdConf();
		fixture.setMetricName("");
		fixture.setWarningLevel(new Double(1.0));
		fixture.setAlertLevel(new Double(1.0));
		Double warningLevel = new Double(1.0);

		fixture.setWarningLevel(warningLevel);

		// add additional test code here
	}

	/**
	 * Test to string_1.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testToString_1()
		throws Exception {
		ThresholdConf fixture = new ThresholdConf();
		fixture.setMetricName("");
		fixture.setWarningLevel(new Double(1.0));
		fixture.setAlertLevel(new Double(1.0));

		String result = fixture.toString();

		// add additional test code here
		assertEquals("ThresholdConf [metricName=, warningLevel=1.0, alertLevel=1.0, getMetricName()=, getWarningLevel()=1.0, getAlertLevel()=1.0]", result);
	}
}
