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

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopJobTest.
 *
 * @author bgunjan
 */
public class HadoopJobTest {
	
	/** The hadoop job. */
	HadoopJob hadoopJob;
	
	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		hadoopJob = new HadoopJob();
	}
	
	/**
	 * Test hadoop job test.
	 */
	@Test
	public void testHadoopJobTest() {
		assertNotNull(hadoopJob);
	}

	/**
	 * Test get failure info.
	 */
	@Test
	public void testGetFailureInfo() {
		String failureInfo = "failureInfo";
		hadoopJob.setFailureInfo(failureInfo);
		String val = hadoopJob.getFailureInfo();
		assertNotNull(val);
		assertSame(failureInfo, hadoopJob.getFailureInfo());
	}

	/**
	 * Test set failure info.
	 */
	@Test
	public void testSetFailureInfo() {
		hadoopJob.setFailureInfo("failureInfo");
		String failureInfo = "failureInfo";

		hadoopJob.setFailureInfo(failureInfo);

		assertNotSame("NA", hadoopJob.getFailureInfo());
		assertNotNull(hadoopJob.getFailureInfo());
	}

	/**
	 * Test get job state.
	 */
	@Test
	public void testGetJobState() {

		String jobState = "RUNNING";
		hadoopJob.setJobState(jobState);
		String val = hadoopJob.getJobState();
		assertNotNull(val);
		assertSame(jobState, hadoopJob.getJobState());
	
	}

	/**
	 * Test set job state.
	 */
	@Test
	public void testSetJobState() {
		hadoopJob.setJobState("RUNNING");
		String jobState = "PREP";

		hadoopJob.setJobState(jobState);

		assertNotSame("RUNNING", hadoopJob.getJobState());
		assertNotNull(hadoopJob.getJobState());
	}

	/**
	 * Test get job id.
	 */
	public void testGetJobId() {

		String jobId = "job_20131191311_0001";
		hadoopJob.setJobId(jobId);
		String val = hadoopJob.getJobId();
		assertNotNull(val);
		assertSame(jobId, hadoopJob.getJobId());
	
	}

	/**
	 * Test set job id.
	 */
	@Test
	public void testSetJobId() {
		hadoopJob.setJobId("job_20131191311_0001");
		String jobId = "job_20131191311_0002";

		hadoopJob.setJobId(jobId);

		assertNotSame("job_20131191311_0001", hadoopJob.getJobId());
		assertNotNull(hadoopJob.getJobId());
	}

	/**
	 * Test get job name.
	 */
	public void testGetJobName() {

		String jobName = "PiEstimator";
		hadoopJob.setJobName(jobName);
		String val = hadoopJob.getJobName();
		assertNotNull(val);
		assertSame(jobName, hadoopJob.getJobName());
	
	}

	/**
	 * Test set job name.
	 */
	@Test
	public void testSetJobName() {
		hadoopJob.setJobName("PiEstimator");
		String jobId = "WordCount";

		hadoopJob.setJobName(jobId);

		assertNotNull(hadoopJob.getJobName());
		assertNotSame("PiEstimator", hadoopJob.getJobName());
	}

	/**
	 * Test get user name.
	 */
	@Test
	public void testGetUserName() {

		String userName = "impetus";
		hadoopJob.setUserName(userName);
		String val = hadoopJob.getUserName();
		assertNotNull(val);
		assertSame(userName, hadoopJob.getUserName());
	
	}

	/**
	 * Test set user name.
	 */
	@Test
	public void testSetUserName() {
		hadoopJob.setUserName("impetus");
		String userName = "ankush";

		hadoopJob.setUserName(userName);

		assertNotNull(hadoopJob.getUserName());
		assertNotSame("impetus", hadoopJob.getUserName());
	}

	/**
	 * Test get job priority.
	 */
	@Test
	public void testGetJobPriority() {

		String jobPriority = "HIGH";
		hadoopJob.setJobPriority(jobPriority);
		String val = hadoopJob.getJobPriority();
		assertNotNull(val);
		assertSame(jobPriority, hadoopJob.getJobPriority());
	
	}

	/**
	 * Test set job priority.
	 */
	@Test
	public void testSetJobPriority() {
		hadoopJob.setJobPriority("HIGH");
		String jobPriority = "LOW";

		hadoopJob.setJobPriority(jobPriority);

		assertNotNull(hadoopJob.getJobPriority());
		assertNotSame("HIGH", hadoopJob.getJobPriority());
	}

	/**
	 * Test get scheduling info.
	 */
	@Test
	public void testGetSchedulingInfo() {

		String schedulingInfo = "NA";
		hadoopJob.setSchedulingInfo(schedulingInfo);
		String val = hadoopJob.getSchedulingInfo();
		assertNotNull(val);
		assertSame(schedulingInfo, hadoopJob.getSchedulingInfo());
	
	}

	/**
	 * Test set scheduling info.
	 */
	@Test
	public void testSetSchedulingInfo() {
		hadoopJob.setSchedulingInfo("NA");
		String schedulingInfo = "test";

		hadoopJob.setSchedulingInfo(schedulingInfo);

		assertNotNull(hadoopJob.getSchedulingInfo());
		assertNotSame("NA", hadoopJob.getSchedulingInfo());
	}

	/**
	 * Test get job start time.
	 */
	@Test
	public void testGetJobStartTime() {

		Date jobStartTime = new Date();
		hadoopJob.setJobStartTime(jobStartTime);
		Date date = hadoopJob.getJobStartTime();
		assertNotNull(date);
		assertSame(jobStartTime, hadoopJob.getJobStartTime());
	
	}

	/**
	 * Test set job start time.
	 */
	@Test
	public void testSetJobStartTime() {
		Date jobStartTime = new Date();

		hadoopJob.setJobStartTime(jobStartTime);
		assertNotNull(hadoopJob.getJobStartTime());
	}

	/**
	 * Test is job complete.
	 */
	@Test
	public void testIsJobComplete() {

		boolean jobComplete = false;
		hadoopJob.setJobComplete(jobComplete);
		boolean val = hadoopJob.isJobComplete();
		assertNotNull(val);
		assertSame(jobComplete, hadoopJob.isJobComplete());
	
	}

	/**
	 * Test set job complete.
	 */
	@Test
	public void testSetJobComplete() {
		hadoopJob.setJobComplete(true);
		boolean jobComplete = false;

		hadoopJob.setJobComplete(jobComplete);

		assertNotNull(hadoopJob.isJobComplete());
		assertNotSame(true, hadoopJob.isJobComplete());
	}

	/**
	 * Test get map progress.
	 */
	@Test
	public void testGetMapProgress() {

		float mapProgress = 1.0f;
		hadoopJob.setMapProgress(mapProgress);
		float val = hadoopJob.getMapProgress();
		assertNotNull(val);
		assertNotSame(0.0f, hadoopJob.getMapProgress());
	
	}

	/**
	 * Test set map progress.
	 */
	@Test
	public void testSetMapProgress() {
		hadoopJob.setMapProgress(1.1f);
		float mapProgress = 0.0f;

		hadoopJob.setMapProgress(mapProgress);

		assertNotNull(hadoopJob.getMapProgress());
		assertNotSame(1.1f, hadoopJob.getMapProgress());
	}

	/**
	 * Test get map total.
	 */
	@Test
	public void testGetMapTotal() {

		int mapTotal = 0;
		hadoopJob.setMapTotal(mapTotal);
		int val = hadoopJob.getMapTotal();
		assertNotNull(val);
		assertSame(mapTotal, hadoopJob.getMapTotal());
	
	}

	/**
	 * Test set map total.
	 */
	@Test
	public void testSetMapTotal() {
		hadoopJob.setMapTotal(1);
		int mapTotal = 0;

		hadoopJob.setMapTotal(mapTotal);

		assertNotNull(hadoopJob.getMapTotal());
		assertNotSame(1, hadoopJob.getMapTotal());
	}

	/**
	 * Test get map completed.
	 */
	@Test
	public void testGetMapCompleted() {

		int mapCompleted = 0;
		hadoopJob.setMapCompleted(mapCompleted);
		int val = hadoopJob.getMapCompleted();
		assertNotNull(val);
		assertSame(mapCompleted, hadoopJob.getMapCompleted());
	
	}

	/**
	 * Test set map completed.
	 */
	@Test
	public void testSetMapCompleted() {
		hadoopJob.setMapCompleted(1);
		int mapCompleted = 0;

		hadoopJob.setMapCompleted(mapCompleted);

		assertNotNull(hadoopJob.getMapCompleted());
		assertNotSame(1, hadoopJob.getMapCompleted());
	}

	/**
	 * Test get reduce progress.
	 */
	@Test
	public void testGetReduceProgress() {

		float reduceProgress = 0.0f;
		hadoopJob.setReduceProgress(reduceProgress);
		float val = hadoopJob.getReduceProgress();
		assertNotNull(val);
		assertNotSame(reduceProgress, hadoopJob.getReduceProgress());
	
	}

	/**
	 * Test set reduce progress.
	 */
	@Test
	public void testSetReduceProgress() {
		hadoopJob.setReduceProgress(1.1f);
		float reduceProgress = 0.0f;

		hadoopJob.setReduceProgress(reduceProgress);

		assertNotNull(hadoopJob.getReduceProgress());
		assertNotSame(1.1f, hadoopJob.getReduceProgress());
	}

	/**
	 * Test get reduce total.
	 */
	@Test
	public void testGetReduceTotal() {

		int reduceTotal = 0;
		hadoopJob.setReduceTotal(reduceTotal);
		int val = hadoopJob.getReduceTotal();
		assertNotNull(val);
		assertSame(reduceTotal, hadoopJob.getReduceTotal());
	
	}

	/**
	 * Test set reduce total.
	 */
	@Test
	public void testSetReduceTotal() {
		hadoopJob.setReduceTotal(1);
		int reduceTotal = 0;

		hadoopJob.setReduceTotal(reduceTotal);

		assertNotNull(hadoopJob.getReduceTotal());
		assertNotSame(1, hadoopJob.getReduceTotal());
	}

	/**
	 * Test get reduce completed.
	 */
	@Test
	public void testGetReduceCompleted() {

		int reduceCompleted = 0;
		hadoopJob.setReduceCompleted(reduceCompleted);
		int val = hadoopJob.getReduceCompleted();
		assertNotNull(val);
		assertSame(reduceCompleted, hadoopJob.getReduceCompleted());
	
	}

	/**
	 * Test set reduce completed.
	 */
	public void testSetReduceCompleted() {
		hadoopJob.setReduceCompleted(1);
		int reduceCompleted = 0;

		hadoopJob.setReduceCompleted(reduceCompleted);

		assertNotNull(hadoopJob.getReduceCompleted());
		assertNotSame(1, hadoopJob.getReduceCompleted());
	}

	/**
	 * Test gettest setup progress.
	 */
	@Test
	public void testGetSetupProgress() {

		float setupProgress = 0.0f;
		hadoopJob.setSetupProgress(setupProgress);
		float val = hadoopJob.getSetupProgress();
		assertNotNull(val);
		assertNotSame(setupProgress, hadoopJob.getSetupProgress());
	
	}

	/**
	 * Test setup progress.
	 */
	@Test
	public void testSetupProgress() {
		hadoopJob.setSetupProgress(1.1f);
		float setupProgress = 0.0f;

		hadoopJob.setSetupProgress(setupProgress);

		assertNotNull(hadoopJob.getSetupProgress());
		assertNotSame(1.1f, hadoopJob.getSetupProgress());
	}

	/**
	 * Test get cleanup progress.
	 */
	@Test
	public void testGetCleanupProgress() {

		float cleanupProgress = 0.0f;
		hadoopJob.setCleanupProgress(cleanupProgress);
		float val = hadoopJob.getCleanupProgress();
		assertNotNull(val);
		assertNotSame(cleanupProgress, hadoopJob.getCleanupProgress());
	
	}

	/**
	 * Test set cleanup progress.
	 */
	@Test
	public void testSetCleanupProgress() {
		hadoopJob.setCleanupProgress(1.1f);
		float cleanupProgress = 0.0f;

		hadoopJob.setCleanupProgress(cleanupProgress);

		assertNotNull(hadoopJob.getCleanupProgress());
		assertNotSame(1.1f, hadoopJob.getCleanupProgress());
	}
}
