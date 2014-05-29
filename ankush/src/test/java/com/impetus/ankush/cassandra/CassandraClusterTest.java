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
package com.impetus.ankush.cassandra;

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;

public class CassandraClusterTest {

	private CassandraCluster cassandraClusterObj;
	private CassandraCluster cassandraClusterObj1;
	private CassandraClusterConf cassandarconfObj;
	private CassandraClusterConf cassandarconfObj1;
	private NodeConf nodeConf;
	private CassandraNodeConf nodeConf1;
	private ArrayList<NodeConf> nodeList;
	private ArrayList<CassandraNodeConf> nodeList1;
//	private JCloudActionsProvider jCloudActionsProvider;
	private Cluster cluster;
	
	@Before
	public void setUp() throws Exception {
		String publicIp = "192.168.145.61";
		String privateIp = "192.168.145.61";
		nodeConf = new NodeConf(publicIp, privateIp);
		nodeConf.setStatus(false);
		nodeConf1 = new CassandraNodeConf();
		nodeConf1.setPublicIp(publicIp);
		nodeConf1.setPrivateIp(privateIp);
		nodeConf1.setStatus(false);
		nodeList = new ArrayList<NodeConf>();
		nodeList.add(nodeConf);
		nodeList1 = new ArrayList<CassandraNodeConf>();
		nodeList1.add(nodeConf1);
		cassandraClusterObj = new CassandraCluster();
		cassandarconfObj1 = new CassandraClusterConf();
		cassandraClusterObj1 = EasyMock.createMock(CassandraCluster.class);
		cassandarconfObj = EasyMock.createMock(CassandraClusterConf.class);
//		jCloudActionsProvider = EasyMock.createMock(JCloudActionsProvider.class);
		cluster = EasyMock.createMock(Cluster.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testPositiveValidate() {
		ClusterConf clusterConf = new CassandraClusterConf();
		EasyMock.expect(cassandarconfObj.getNodeConfs()).andReturn(nodeList);
		EasyMock.replay(cassandarconfObj);
		EasyMock.expect(cassandraClusterObj1.validate(clusterConf, nodeList)).andReturn(true);
		EasyMock.replay(cassandraClusterObj1);
		assertSame(true,cassandraClusterObj1.validate(clusterConf));
	}
	
	@Ignore
	@Test
	public void testPositiveUndeploy() {
		ClusterConf clusterConf = new CassandraClusterConf();
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("sahdhkj",true);
		EasyMock.replay(cassandarconfObj);
		cassandraClusterObj1.postUndeploy(clusterConf);
	}
	
	@Ignore
	@Test
	public void testPositiveGetNodes() {
		ClusterConf clusterConf = new CassandraClusterConf();
		EasyMock.replay(cassandarconfObj);
		assertSame(nodeList,cassandraClusterObj1.getNodes(clusterConf));
	}
	
	@Ignore
	@Test
	public void testPositiveGetClusterConf() {
		ClusterConf clusterConf = new CassandraClusterConf();
		EasyMock.expect(cluster.getClusterConf()).andReturn(clusterConf);
		EasyMock.replay(cluster);
		assertSame(clusterConf,cassandraClusterObj1.getClusterConf(cluster));
	}
	
}
