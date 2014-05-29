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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;

/**
 * The Class JobStatusProviderTest.
 *
 * @author bgunjan
 */
public class JobStatusProviderTest {
	
	/** The provider. */
	JobStatusProvider provider;
	
	/** The job client. */
	JobClient jobClient;
	
	/** The host. */
	String host;
	
	/** The job tracker port. */
	int jobTrackerPort;
	
	/** The conf. */
	Configuration conf;
	
	/** The agent conf. */
	AgentConf agentConf;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp() {
		provider = EasyMock.createMock(JobStatusProvider.class);
		host = "192.168.41.109";
		jobTrackerPort = 9001;
		conf = new Configuration();
		jobClient = EasyMock.createMock(JobClient.class);
		agentConf = EasyMock.createMock(AgentConf.class);
	}
	
	/**
	 * Gets the job client.
	 *
	 * @return the job client
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void getJobClient() throws IOException {
		Object jobClient = null;
		jobClient = EasyMock.createMock(JobClient.class);
//		jobClient = new JobClient(new InetSocketAddress(host, jobTrackerPort), conf);
		assertNotNull(jobClient);
	}
	
	/**
	 * Gets the job metrics.
	 *
	 * @return the job metrics
	 */
	@Test
	public void getJobMetrics() {
		Map<String,Object> jobMetrics = new HashMap<String,Object>();
		EasyMock.expect(provider.getJobMetrics()).andReturn(jobMetrics);
	}
	
	/**
	 * Gets the job metrics test.
	 *
	 * @return the job metrics test
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Test
	public void getJobMetricsTest() throws IOException {
		Map<String, Object> hadoopJobMetrics = new HashMap<String, Object>();
		
		if (jobClient != null) {
			EasyMock.expect(jobClient.getSystemDir()).andReturn(null);
			EasyMock.expect(jobClient.getClusterStatus()).andReturn(null);
			ClusterStatus clusterStatus = EasyMock.createMock(ClusterStatus.class);
			EasyMock.expect(clusterStatus.getJobTrackerState()).andReturn(null);
			EasyMock.expect(clusterStatus.getMapTasks()).andReturn(0);
			EasyMock.expect(clusterStatus.getMaxMapTasks()).andReturn(0);
			EasyMock.expect(clusterStatus.getMaxReduceTasks()).andReturn(0);
			EasyMock.expect(clusterStatus.getReduceTasks()).andReturn(0);
			EasyMock.expect(clusterStatus.getTaskTrackers()).andReturn(0);
			EasyMock.expect(clusterStatus.getBlacklistedTrackers()).andReturn(0);
			EasyMock.expect(clusterStatus.getMaxMemory()).andReturn(0L);
			EasyMock.expect(clusterStatus.getUsedMemory()).andReturn(0L);
			EasyMock.expect(clusterStatus.getTTExpiryInterval()).andReturn(0L);
			EasyMock.expect(jobClient.getDefaultMaps()).andReturn(0);
			EasyMock.expect(jobClient.getDefaultReduces()).andReturn(0);
			EasyMock.expect(jobClient.getAllJobs()).andReturn(null);
		} 
		assertNotNull(hadoopJobMetrics);
	}
	
	/**
	 * Gets the hadoop home.
	 *
	 * @return the hadoop home
	 */
	@Test
	public void getHadoopHome() {
		EasyMock.expect(agentConf.getStringValue(Constant.PROP_NAME_HADOOP_HOME)).andReturn(null);
	}
	
	/**
	 * Gets the job tracker port.
	 *
	 * @return the job tracker port
	 */
	@Test
	public void getJobTrackerPort() {
		// Getting DFS Port value from AgentConf
		Integer jobTrackerPort = agentConf.getIntValue(Constant.PROP_NAMENODE_PORT);
		Integer jtPortExpected = new Integer(0);
		assertEquals(jobTrackerPort, jtPortExpected);
	}
}
