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

import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;

/**
 * The Class JPSServiceStatusMonitorTest.
 * 
 * @author Hokam Chauhan
 */
public class JPSServiceStatusMonitorTest {

	/** The conf. */
	private AgentConf conf;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		conf = EasyMock.createMock(AgentConf.class);
	}

	/**
	 * Tear down.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for checking not null.
	 * 
	 */
	@Test
	public void testServiceStatusMonitorNotNull() {
		JPSServiceStatusMonitor monitor = new JPSServiceStatusMonitor();
		assertNotNull(monitor);
	}

	/**
	 * Test method for start.
	 */
	@Test
	public void testStart() {
		JPSServiceStatusMonitor monitor = new JPSServiceStatusMonitor();

		EasyMock.expect(conf.getURL(Constant.PROP_NAME_SERVICE_URL_LAST)).andReturn("service");
		EasyMock.expect(
				conf.getIntValue(Constant.PROP_NAME_SERVICE_STATUS_UPDATE_TIME))
				.andReturn(60);
		EasyMock.replay(conf);
		monitor.start();
	}

}
