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
package com.impetus.ankush.hadoop.job;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class HadoopMetricsInfo.
 *
 * @author bgunjan
 */
public class HadoopMetricsInfoTest  {
	
	/** The hadoop job. */
	HadoopMetricsInfo hadoopMetricsInfo;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		hadoopMetricsInfo = new HadoopMetricsInfo();
	}
	
	/**
	 * Test hadoop metrics info test.
	 */
	@Test
	public void testHadoopMetricsInfoTest() {
		assertNotNull(hadoopMetricsInfo);
	}
	
	/**
	 * Test get system directory name.
	 */
	@Test
	public void testGetSystemDirectoryName() {
		String systemDirectoryName = "/home/test";
		hadoopMetricsInfo.setSystemDirectoryName(systemDirectoryName);
		String val = hadoopMetricsInfo.getSystemDirectoryName();
		assertNotNull(val);
		assertSame(systemDirectoryName, hadoopMetricsInfo.getSystemDirectoryName());
	}
	
	/**
	 * Test set system directory name.
	 */
	@Test
	public void testSetSystemDirectoryName() {
		hadoopMetricsInfo.setSystemDirectoryName("/home/test");
		String systemDirectoryName = "/home/test1";

		hadoopMetricsInfo.setSystemDirectoryName(systemDirectoryName);

		assertNotSame("/home/test", hadoopMetricsInfo.getSystemDirectoryName());
		assertNotNull(hadoopMetricsInfo.getSystemDirectoryName());
	}
	
	/**
	 * Test get default maps.
	 */
	@Test
	public void testGetDefaultMaps() {
		String defaultMaps = "2";
		hadoopMetricsInfo.setDefaultMaps(defaultMaps);
		String val = hadoopMetricsInfo.getDefaultMaps();
		assertNotNull(val);
		assertSame(defaultMaps, hadoopMetricsInfo.getDefaultMaps());
	}
	
	/**
	 * Test set default maps.
	 */
	@Test
	public void testSetDefaultMaps() {
		hadoopMetricsInfo.setDefaultMaps("3");
		String defaultMaps = "2";

		hadoopMetricsInfo.setDefaultMaps(defaultMaps);

		assertNotSame("3", hadoopMetricsInfo.getDefaultMaps());
		assertNotNull(hadoopMetricsInfo.getDefaultMaps());
	}
	
	/**
	 * Test get default reduces.
	 */
	@Test
	public void testGetDefaultReduces() {
		String defaultReducers = "2";
		hadoopMetricsInfo.setDefaultReduces(defaultReducers);
		String val = hadoopMetricsInfo.getDefaultReduces();
		assertNotNull(val);
		assertSame(defaultReducers, hadoopMetricsInfo.getDefaultReduces());
	}
	
	/**
	 * Test set default reduces.
	 */
	@Test
	public void testSetDefaultReduces() {
		hadoopMetricsInfo.setDefaultReduces("3");
		String defaultReducers = "2";

		hadoopMetricsInfo.setDefaultReduces(defaultReducers);

		assertNotSame("3", hadoopMetricsInfo.getDefaultReduces());
		assertNotNull(hadoopMetricsInfo.getDefaultReduces());
	}
	
	/**
	 * Test get job tracker state.
	 */
	@Test
	public void testGetJobTrackerState() {
		String jobTrackerState = "RUNNING";
		hadoopMetricsInfo.setJobTrackerState(jobTrackerState);
		String val = hadoopMetricsInfo.getJobTrackerState();
		assertNotNull(val);
		assertSame(jobTrackerState, hadoopMetricsInfo.getJobTrackerState());
	}
	
	/**
	 * Test set job tracker state.
	 */
	@Test
	public void testSetJobTrackerState() {
		String jobTrackerState = "RUNNING";
		hadoopMetricsInfo.setJobTrackerState("INITIALIZING");

		assertNotSame(jobTrackerState, hadoopMetricsInfo.getJobTrackerState());
		assertNotNull(hadoopMetricsInfo.getJobTrackerState());
	}
	
	/**
	 * Test get map tasks.
	 */
	@Test
	public void testGetMapTasks() {
		String mapTask = "2";
		hadoopMetricsInfo.setMapTasks(mapTask);
		String val = hadoopMetricsInfo.getMapTasks();
		assertNotNull(val);
		assertSame(mapTask, hadoopMetricsInfo.getMapTasks());
	}
	
	/**
	 * Test set map tasks.
	 */
	@Test
	public void testSetMapTasks() {
		String mapTask = "2";
		hadoopMetricsInfo.setMapTasks("1");

		assertNotNull(hadoopMetricsInfo.getMapTasks());
		assertNotSame(mapTask, hadoopMetricsInfo.getMapTasks());
	}
	
	/**
	 * Test get reduce tasks.
	 */
	@Test
	public void testGetReduceTasks() {
		String reduceTask = "2";
		hadoopMetricsInfo.setReduceTasks(reduceTask);
		String val = hadoopMetricsInfo.getReduceTasks();
		assertNotNull(val);
		assertSame(reduceTask, hadoopMetricsInfo.getReduceTasks());
	}
	
	/**
	 * Test set reduce tasks.
	 */
	@Test
	public void testSetReduceTasks() {
		String reduceTask = "2";
		hadoopMetricsInfo.setReduceTasks("1");

		assertNotNull(hadoopMetricsInfo.getReduceTasks());
		assertNotSame(reduceTask, hadoopMetricsInfo.getReduceTasks());
	}
	
	/**
	 * Test get max map tasks capacity.
	 */
	@Test
	public void testGetMaxMapTasksCapacity() {
		String mapMapCapacity = "4";
		hadoopMetricsInfo.setMaxMapTasksCapacity(mapMapCapacity);
		String val = hadoopMetricsInfo.getMaxMapTasksCapacity();
		assertNotNull(val);
		assertSame(mapMapCapacity, hadoopMetricsInfo.getMaxMapTasksCapacity());
	}
	
	/**
	 * Test set max map tasks capacity.
	 */
	@Test
	public void testSetMaxMapTasksCapacity() {
		String maxMapTasksCapacity = "4";
		hadoopMetricsInfo.setMaxMapTasksCapacity("1");

		assertNotNull(hadoopMetricsInfo.getMaxMapTasksCapacity());
		assertNotSame(maxMapTasksCapacity, hadoopMetricsInfo.getMaxMapTasksCapacity());
	}
	
	/**
	 * Test get max reduce tasks capacity.
	 */
	@Test
	public void testGetMaxReduceTasksCapacity() {
		String maxReduceTasksCapacity = "4";
		hadoopMetricsInfo.setMaxReduceTasksCapacity(maxReduceTasksCapacity);
		String val = hadoopMetricsInfo.getMaxReduceTasksCapacity();
		assertNotNull(val);
		assertSame(maxReduceTasksCapacity, hadoopMetricsInfo.getMaxReduceTasksCapacity());
	}
	
	/**
	 * Test set max reduce tasks capacity.
	 */
	@Test
	public void testSetMaxReduceTasksCapacity() {
		String maxReduceTasksCapacity = "4";
		hadoopMetricsInfo.setMaxReduceTasksCapacity("1");

		assertNotNull(hadoopMetricsInfo.getMaxReduceTasksCapacity());
		assertNotSame(maxReduceTasksCapacity, hadoopMetricsInfo.getMaxReduceTasksCapacity());
	}
	
	/**
	 * Test get task trackers.
	 */
	@Test
	public void testGetTaskTrackers() {
		String taskTrackers = "4";
		hadoopMetricsInfo.setTaskTrackers(taskTrackers);
		String val = hadoopMetricsInfo.getTaskTrackers();
		assertNotNull(val);
		assertSame(taskTrackers, hadoopMetricsInfo.getTaskTrackers());
	}
	
	/**
	 * Test set task trackers.
	 */
	@Test
	public void testSetTaskTrackers() {
		String taskTrackers = "4";
		hadoopMetricsInfo.setTaskTrackers("1");

		assertNotNull(hadoopMetricsInfo.getTaskTrackers());
		assertNotSame(taskTrackers, hadoopMetricsInfo.getTaskTrackers());
	}
	
	/**
	 * Test get black listed trackers.
	 */
	@Test
	public void testGetBlackListedTrackers() {
		String blackListedTrackers = "0";
		hadoopMetricsInfo.setBlackListedTrackers(blackListedTrackers);
		String val = hadoopMetricsInfo.getBlackListedTrackers();
		assertNotNull(val);
		assertSame(blackListedTrackers, hadoopMetricsInfo.getBlackListedTrackers());
	}
	
	/**
	 * Test set black listed trackers.
	 */
	@Test
	public void testSetBlackListedTrackers() {
		String blackListedTrackers = "0";
		hadoopMetricsInfo.setBlackListedTrackers("1");

		assertNotNull(hadoopMetricsInfo.getBlackListedTrackers());
		assertNotSame(blackListedTrackers, hadoopMetricsInfo.getBlackListedTrackers());
	}
	
	/**
	 * Test get max memory.
	 */
	@Test
	public void testGetMaxMemory() {
		String maxMemory = "1024";
		hadoopMetricsInfo.setMaxMemory(maxMemory);
		String val = hadoopMetricsInfo.getMaxMemory();
		assertNotNull(val);
		assertSame(maxMemory, hadoopMetricsInfo.getMaxMemory());
	}
	
	/**
	 * Test set max memory.
	 */
	@Test
	public void testSetMaxMemory() {
		String maxMemory = "1024";
		hadoopMetricsInfo.setMaxMemory("512");

		assertNotNull(hadoopMetricsInfo.getMaxMemory());
		assertNotSame(maxMemory, hadoopMetricsInfo.getMaxMemory());
	}
	
	/**
	 * Test get used memory.
	 */
	@Test
	public void testGetUsedMemory() {
		String usedMemory = "889";
		hadoopMetricsInfo.setUsedMemory(usedMemory);
		String val = hadoopMetricsInfo.getUsedMemory();
		assertNotNull(val);
		assertSame(usedMemory, hadoopMetricsInfo.getUsedMemory());
	}
	
	/**
	 * Test set used memory.
	 */
	@Test
	public void testSetUsedMemory() {
		String usedMemory = "889";
		hadoopMetricsInfo.setUsedMemory("512");

		assertNotNull(hadoopMetricsInfo.getUsedMemory());
		assertNotSame(usedMemory, hadoopMetricsInfo.getUsedMemory());
	}
	
	/**
	 * Test get task tracker expiry interval.
	 */
	@Test
	public void testGetTaskTrackerExpiryInterval() {
		String taskTrackerExpiryInterval = "2";
		hadoopMetricsInfo.setTaskTrackerExpiryInterval(taskTrackerExpiryInterval);
		String val = hadoopMetricsInfo.getTaskTrackerExpiryInterval();
		assertNotNull(val);
		assertSame(taskTrackerExpiryInterval, hadoopMetricsInfo.getTaskTrackerExpiryInterval());
	}
	
	/**
	 * Test set task tracker expiry interval.
	 */
	@Test
	public void testSetTaskTrackerExpiryInterval() {
		String taskTrackerExpiryInterval = "2";
		hadoopMetricsInfo.setTaskTrackerExpiryInterval("1");

		assertNotNull(hadoopMetricsInfo.getTaskTrackerExpiryInterval());
		assertNotSame(taskTrackerExpiryInterval, hadoopMetricsInfo.getTaskTrackerExpiryInterval());
	}
	
	/**
	 * Test get queue info.
	 */
	@Test
	public void testGetQueueInfo() {
		List<String[]> queueInfo = new ArrayList<String[]>();
		hadoopMetricsInfo.setQueueInfo(queueInfo);
		List<String[]> val = hadoopMetricsInfo.getQueueInfo();
		assertNotNull(val);
		assertSame(queueInfo, hadoopMetricsInfo.getQueueInfo());
	}
	
	/**
	 * Test set queue info.
	 */
	@Test
	public void testSetQueueInfo() {
		List<String[]> queueInfo = new ArrayList<String[]>();
		hadoopMetricsInfo.setQueueInfo(queueInfo);

		assertNotNull(hadoopMetricsInfo.getQueueInfo());
		assertSame(queueInfo, hadoopMetricsInfo.getQueueInfo());
	}
	
	/**
	 * Test get total job submission.
	 */
	@Test
	public void testGetTotalJobSubmission() {
		String totalJobSubmission = "2";
		hadoopMetricsInfo.setTotalJobSubmission(totalJobSubmission);
		String val = hadoopMetricsInfo.getTotalJobSubmission();
		assertNotNull(val);
		assertSame(totalJobSubmission, hadoopMetricsInfo.getTotalJobSubmission());
	}
	
	/**
	 * Test set total job submission.
	 */
	@Test
	public void testSetTotalJobSubmission() {
		String totalJobSubmission = "2";
		hadoopMetricsInfo.setTotalJobSubmission("1");

		assertNotNull(hadoopMetricsInfo.getTotalJobSubmission());
		assertNotSame(totalJobSubmission, hadoopMetricsInfo.getTotalJobSubmission());
	}
	
	/**
	 * Test get total job running.
	 */
	@Test
	public void testGetTotalJobRunning() {
		String totalJobRunning = "2";
		hadoopMetricsInfo.setTotalJobRunning(totalJobRunning);
		String val = hadoopMetricsInfo.getTotalJobRunning();
		assertNotNull(val);
		assertSame(totalJobRunning, hadoopMetricsInfo.getTotalJobRunning());
	}
	
	/**
	 * Test set total job running.
	 */
	@Test
	public void testSetTotalJobRunning() {
		String totalJobRunning = "2";
		hadoopMetricsInfo.setTotalJobRunning("1");

		assertNotNull(hadoopMetricsInfo.getTotalJobRunning());
		assertNotSame(totalJobRunning, hadoopMetricsInfo.getTotalJobRunning());
	}
	
	/**
	 * Test get total jobs completed.
	 */
	@Test
	public void testGetTotalJobsCompleted() {
		String totalJobsCompleted = "2";
		hadoopMetricsInfo.setTotalJobsCompleted(totalJobsCompleted);
		String val = hadoopMetricsInfo.getTotalJobsCompleted();
		assertNotNull(val);
		assertSame(totalJobsCompleted, hadoopMetricsInfo.getTotalJobsCompleted());
	}
	
	/**
	 * Test set total jobs completed.
	 */
	@Test
	public void testSetTotalJobsCompleted() {
		String totalJobsCompleted = "2";
		hadoopMetricsInfo.setTotalJobsCompleted("1");

		assertNotNull(hadoopMetricsInfo.getTotalJobsCompleted());
		assertNotSame(totalJobsCompleted, hadoopMetricsInfo.getTotalJobsCompleted());
	}
	
	/**
	 * Test get queued job count.
	 */
	@Test
	public void testGetQueuedJobCount() {
		String queuedJobCount = "4";
		hadoopMetricsInfo.setQueuedJobCount(queuedJobCount);
		String val = hadoopMetricsInfo.getQueuedJobCount();
		assertNotNull(val);
		assertSame(queuedJobCount, hadoopMetricsInfo.getQueuedJobCount());
	}
	
	/**
	 * Test set queued job count.
	 */
	@Test
	public void testSetQueuedJobCount() {
		String queuedJobCount = "4";
		hadoopMetricsInfo.setQueuedJobCount("3");

		assertNotNull(hadoopMetricsInfo.getQueuedJobCount());
		assertNotSame(queuedJobCount, hadoopMetricsInfo.getQueuedJobCount());
	}
	
	/**
	 * Test get avarage task per node.
	 */
	@Test
	public void testGetAvarageTaskPerNode() {
		String avarageTaskPerNode = "6";
		hadoopMetricsInfo.setAvarageTaskPerNode(avarageTaskPerNode);
		String val = hadoopMetricsInfo.getAvarageTaskPerNode();
		assertNotNull(val);
		assertSame(avarageTaskPerNode, hadoopMetricsInfo.getAvarageTaskPerNode());
	}
	
	/**
	 * Test set avarage task per node.
	 */
	@Test
	public void testSetAvarageTaskPerNode() {
		String avarageTaskPerNode = "5";
		hadoopMetricsInfo.setAvarageTaskPerNode("3");

		assertNotNull(hadoopMetricsInfo.getAvarageTaskPerNode());
		assertNotSame(avarageTaskPerNode, hadoopMetricsInfo.getAvarageTaskPerNode());
	}
	
	/**
	 * Test get scheduler type.
	 */
	@Test
	public void testGetSchedulerType() {
		String schedulerType = "default";
		hadoopMetricsInfo.setSchedulerType(schedulerType);
		String val = hadoopMetricsInfo.getSchedulerType();
		assertNotNull(val);
		assertSame(schedulerType, hadoopMetricsInfo.getSchedulerType());
	}
	
	/**
	 * Sets the scheduler type.
	 *
	 */
	@Test
	public void testSetSchedulerType() {
		String schedulerType = "default";
		hadoopMetricsInfo.setSchedulerType("capacity");

		assertNotNull(hadoopMetricsInfo.getSchedulerType());
		assertNotSame(schedulerType, hadoopMetricsInfo.getSchedulerType());
	}
}
