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

import java.util.LinkedList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * The Class AlertsConfTest.
 */
public class AlertsConfTest {

	/**
	 * Test alerts conf_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testAlertsConf_1() throws Exception {
		AlertsConf result = new AlertsConf();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Test get mailing list_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetMailingList_1()
		throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(new LinkedList());
		fixture.setRefreshInterval(new Integer(1));

		String result = fixture.getMailingList();

		assertEquals("", result);
		
		fixture.setMailingList(null);		

		assertEquals(null, fixture.getMailingList());
		
	}

	/**
	 * Test get refresh interval_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetRefreshInterval_1() throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(new LinkedList());
		fixture.setRefreshInterval(new Integer(1));

		Integer result = fixture.getRefreshInterval();

		// add additional test code here
		assertNotNull(result);
		assertEquals("1", result.toString());
		assertEquals((byte) 1, result.byteValue());
		assertEquals((short) 1, result.shortValue());
		assertEquals(1, result.intValue());
		assertEquals(1L, result.longValue());
		assertEquals(1.0f, result.floatValue(), 1.0f);
		assertEquals(1.0, result.doubleValue(), 1.0);
	}

	/**
	 * Test get thresholds_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testGetThresholds_1() throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(null);
		fixture.setRefreshInterval(new Integer(1));

		List<ThresholdConf> result = fixture.getThresholds();

		// add additional test code here
		assertNotNull(result);
		assertEquals(2, result.size());
	}

	/**
	 * Test is inform all admins_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testIsInformAllAdmins_1() throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(new LinkedList());
		fixture.setRefreshInterval(new Integer(1));

		boolean result = fixture.isInformAllAdmins();

		// add additional test code here
		assertEquals(true, result);
	}

	/**
	 * Test set inform all admins_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetInformAllAdmins_1() throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(new LinkedList());
		fixture.setRefreshInterval(new Integer(1));
		boolean informAllAdmins = true;

		fixture.setInformAllAdmins(informAllAdmins);

		// add additional test code here
	}

	/**
	 * Test set mailing list_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetMailingList_1() throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(new LinkedList());
		fixture.setRefreshInterval(new Integer(1));
		String mailingList = "";

		fixture.setMailingList(mailingList);

		// add additional test code here
	}

	/**
	 * Test set refresh interval_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetRefreshInterval_1() throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(new LinkedList());
		fixture.setRefreshInterval(new Integer(1));
		Integer refreshInterval = new Integer(1);

		fixture.setRefreshInterval(refreshInterval);

		// add additional test code here
	}

	/**
	 * Test set thresholds_1.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testSetThresholds_1() throws Exception {
		AlertsConf fixture = new AlertsConf();
		fixture.setMailingList("");
		fixture.setInformAllAdmins(true);
		fixture.setThresholds(new LinkedList());
		fixture.setRefreshInterval(new Integer(1));
		List<ThresholdConf> thresholds = new LinkedList();

		fixture.setThresholds(thresholds);

		// add additional test code here
	}
}
