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
package com.impetus.ankush.common.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedSet;

import com.impetus.ankush.common.alerts.AlertsConf;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;

/**
 * The class <code>ClusterTest</code> contains tests for the class
 * <code>{@link Cluster}</code>.
 */
public class ClusterTest {
	private static final long DATE = 1372406912786L;
	private Cluster fixture;

	/**
	 * Run the Cluster() constructor test.
	 */
	@Test
	public void testCluster_1() {
		Cluster result = new Cluster();
		assertNotNull(result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_1() {
		Cluster obj = new Cluster();
		obj.setState("error");
		obj.setId(new Long(1L));
		obj.setName("cluster");
		obj.setEnvironment("cloud");
		obj.setConfigurations(new ManagedList<Configuration>());
		obj.setLogs(new ManagedList<Log>());
		obj.setTechnology("hadoop");
		obj.setTiles(new ManagedList<Tile>());
		obj.setCreatedAt(new Date(DATE));
		obj.setNodes(new ManagedSet<Node>());
		obj.setUser("ankush");
		obj.setEvents(new ManagedList<Event>());

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_2() {
		Object obj = null;

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_3() {
		Object obj = new Object();

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_4() {
		Cluster obj = new Cluster();
		obj.setName("cluster");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_5() {
		Cluster obj = new Cluster();
		obj.setName("cluster");
		obj.setTechnology("hadoop");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_6() {
		Cluster obj = new Cluster();
		obj.setName("cluster");
		obj.setTechnology("hadoop");
		obj.setUser("ankush");

		boolean result = fixture.equals(obj);

		assertEquals(true, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_7() {
		Cluster obj = new Cluster();
		obj.setName("cluster");
		obj.setTechnology("hadoop");
		obj.setUser("");

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_8() {
		Cluster obj = new Cluster();
		obj.setName("");
		obj.setTechnology("");
		obj.setUser((String) null);

		boolean result = fixture.equals(obj);

		assertEquals(false, result);
	}

	/**
	 * Run the boolean equals(Object) method test.
	 */
	@Test
	public void testEquals_9() {
		boolean result = fixture.equals(fixture);

		assertEquals(true, result);
	}

	/**
	 * Run the AlertsConf getAlertsConf() method test.
	 */
	@Test
	public void testGetAlertsConf_1() {
		AlertsConf result = fixture.getAlertsConf();

		assertEquals(null, result);
	}

	/**
	 * Run the AlertsConf getAlertsConf() method test.
	 */
	@Test
	public void testGetAlertsConf_2() {
		AlertsConf alertsConf = new AlertsConf();
		String mailingList = "mailing list";
		alertsConf.setMailingList(mailingList);
		fixture.setAlertConf(alertsConf);

		AlertsConf result = fixture.getAlertsConf();

		assertNotNull(result);
		assertEquals(mailingList, result.getMailingList());
	}

	/**
	 * Run the ClusterConf getClusterConf() method test.
	 */
	@Test
	public void testGetClusterConf_1() {

		ClusterConf result = fixture.getClusterConf();

		assertEquals(null, result);
	}

	/**
	 * Run the ClusterConf getClusterConf() method test.
	 */
	@Test
	public void testGetClusterConf_2() {
		ClusterConf clusterConf = new HadoopClusterConf();
		fixture.setClusterConf(clusterConf);

		ClusterConf result = fixture.getClusterConf();

		assertEquals(clusterConf, result);
	}

	/**
	 * Run the Set<Configuration> getConfigurations() method test.
	 */
	@Test
	public void testGetConfigurations_1() {

		List<Configuration> result = fixture.getConfigurations();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Date getCreatedAt() method test.
	 */
	@Test
	public void testGetCreatedAt_1() {
		Date result = fixture.getCreatedAt();

		assertNotNull(result);
		assertEquals(new Date(1372406912786L).toString(), result.toString());
	}

	/**
	 * Run the String getEnvironment() method test.
	 */
	@Test
	public void testGetEnvironment_1() {

		String result = fixture.getEnvironment();

		assertEquals("cloud", result);
	}

	/**
	 * Run the Set<Event> getEvents() method test.
	 */
	@Test
	public void testGetEvents_1() {

		List<Event> result = fixture.getEvents();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the Long getId() method test.
	 */
	@Test
	public void testGetId_1() {

		Long result = fixture.getId();

		assertNotNull(result);
		assertEquals(1L, result.longValue());
	}

	/**
	 * Run the Set<Log> getLogs() method test.
	 */
	@Test
	public void testGetLogs_1() {

		List<Log> result = fixture.getLogs();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the String getName() method test.
	 */
	@Test
	public void testGetName_1() {

		String result = fixture.getName();

		assertEquals("cluster", result);
	}

	/**
	 * Run the Set<Node> getNodes() method test.
	 */
	@Test
	public void testGetNodes_1() {

		Set<Node> result = fixture.getNodes();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the String getState() method test.
	 */
	@Test
	public void testGetState_1() {

		String result = fixture.getState();

		assertEquals("error", result);
	}

	/**
	 * Run the String getTechnology() method test.
	 */
	@Test
	public void testGetTechnology_1() {

		String result = fixture.getTechnology();

		assertEquals("hadoop", result);
	}

	/**
	 * Run the Set<Tile> getTiles() method test.
	 */
	@Test
	public void testGetTiles_1() {

		List<Tile> result = fixture.getTiles();

		assertNotNull(result);
		assertEquals(0, result.size());
	}

	/**
	 * Run the String getUser() method test.
	 */
	@Test
	public void testGetUser_1() {

		String result = fixture.getUser();

		assertEquals("ankush", result);
	}

	/**
	 * Perform pre-test initialization.
	 */
	@Before
	public void setUp() {
		fixture = new Cluster();
		fixture.setConfigurations(new ManagedList<Configuration>());
		fixture.setState("error");
		fixture.setLogs(new ManagedList<Log>());
		fixture.setName("cluster");
		fixture.setTiles(new ManagedList<Tile>());
		fixture.setCreatedAt(new Date(DATE));
		fixture.setNodes(new ManagedSet<Node>());
		fixture.setTechnology("hadoop");
		fixture.setEvents(new ManagedList<Event>());
		fixture.setId(new Long(1L));
		fixture.setEnvironment("cloud");
		fixture.setUser("ankush");
	}

	/**
	 * Perform post-test clean-up.
	 */
	@After
	public void tearDown() {
		fixture = null;
	}
}
