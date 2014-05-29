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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.mapred.Counters.Group;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobQueueInfo;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.JobTracker.State;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TIPStatus;
import org.apache.hadoop.mapred.TaskID;
import org.apache.hadoop.mapred.TaskReport;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Manipulatable;
import com.impetus.ankush.agent.action.impl.ManipulatorFactory;

/**
 * The Class JobStatusProvider.
 * 
 * @author bgunjan
 */
public class JobStatusProvider {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(JobStatusProvider.class);
	/** The Constant JOB_STATE_PREP. */
	public static final int JOB_STATE_PREP = 4; // 4

	/** The Constant JOB_STATE_RUNNING. */
	public static final int JOB_STATE_RUNNING = 1;

	/** The Constant JOB_STATE_SUCCEEDED. */
	public static final int JOB_STATE_SUCCEEDED = 2;

	/** The Constant JOB_STATE_FAILED. */
	public static final int JOB_STATE_FAILED = 3;

	/** The Constant JOB_STATE_KILLED. */
	public static final int JOB_STATE_KILLED = 5;

	/** The conf. */
	private AgentConf conf = new AgentConf();

	/**
	 * Gets the job metrics.
	 * 
	 * @return List
	 */
	public Map<String, Object> getJobMetrics() {
		// job tracker port.
		int port = conf.getIntValue(Constant.PROP_NAMENODE_PORT);

		// job tracker host
		String host = conf.getStringValue(Constant.PROP_NAMENODE_HOST);
		// Getting jobClient Value
		JobClient jobClient = getJobClient(host, port);
		// Checking for !null
		if (jobClient != null) {
			// Creating empty map for storing job metrics information
			Map<String, Object> jobMetrics = new HashMap<String, Object>();
			return getJobMetrics(jobClient);
		}
		return Collections.EMPTY_MAP;
	}

	/**
	 * Method getJobStatus.
	 * 
	 * @return Map<Object,Object> The Map for Job Status Value.
	 */
	public List<Map<String, Object>> getJobStatus() {
		// Creating an empty list of map for storing job Status information
		try {
			// job tracker port.
			int port = conf.getIntValue(Constant.PROP_NAMENODE_PORT);
			// job tracker host
			String host = conf.getStringValue(Constant.PROP_NAMENODE_HOST);
			// return all jobs.
			return listAllJobs(getJobClient(host, port));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * List all jobs.
	 * 
	 * @param jobClient
	 *            the job client
	 * @return the list
	 * @throws InterruptedException
	 */
	private List<Map<String, Object>> listAllJobs(JobClient jobClient)
			throws InterruptedException {
		// Creating an empty list of map for storing job Status information
		List<Map<String, Object>> jobReports = new ArrayList<Map<String, Object>>();
		try {
			// Checking for jobClient null
			if (jobClient != null) {
				// Get the jobs that are submitted.
				JobStatus[] jobStatus = jobClient.getAllJobs();
				// Iterating over the list of all submitted jobs
				for (JobStatus jobSts : jobStatus) {
					// Creating an empty map for storing job information
					Map<String, Object> jobReport = new HashMap<String, Object>();
					// Returns the jobid of the Job
					org.apache.hadoop.mapred.JobID jobId = jobSts.getJobID();
					// Get an RunningJob object to track an ongoing Map-Reduce
					// job.
					RunningJob job = jobClient.getJob(jobId);
					String jobName = "";
					if (job != null) {
						// Get the name of the job.
						jobName = job.getJobName();
					}
					// Percentage of progress in maps
					float mapProgress = jobSts.mapProgress() * 100;
					// Percentage of progress in reduce
					float reduceProgress = jobSts.reduceProgress() * 100;

					int mapTotal = 0;
					int reduceTotal = 0;
					int mapComp = 0;
					int reduceComp = 0;

					// Count for Map and Reduce Complete
					try {
						// Get the information of the current state of the map
						// tasks of a job
						TaskReport[] mapTaskReports = jobClient
								.getMapTaskReports(jobId);
						// Get the total map
						mapTotal = mapTaskReports.length;
						// Iterating over the map tasks
						for (TaskReport taskReport : mapTaskReports) {
							// The current state of a map TaskInProgress as seen
							// by the JobTracker.
							TIPStatus currentStatus = taskReport
									.getCurrentStatus();
							if (currentStatus == TIPStatus.COMPLETE) {
								mapComp++;
							}
						}

						// Get the information of the current state of the
						// reduce tasks of a job.
						TaskReport[] reduceTaskReport = jobClient
								.getReduceTaskReports(jobId);
						// Get the total reduce
						reduceTotal = reduceTaskReport.length;
						// Iterating over the reduce tasks
						for (TaskReport taskReport : reduceTaskReport) {
							// The current state of a reduce TaskInProgress as
							// seen by the JobTracker.
							TIPStatus currentStatus = taskReport
									.getCurrentStatus();
							if (currentStatus == TIPStatus.COMPLETE) {
								reduceComp++;
							}
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
					// Percentage of progress in setup
					float setupProgress = jobSts.setupProgress() * 100;
					// The progress made on cleanup
					float cleanupProgress = jobSts.cleanupProgress() * 100;
					// gets any available info on the reason of failure of the
					// job..Returns the diagnostic information on why a job
					// might have failed.
					String failureInfo = jobSts.getFailureInfo();

					// Putting Job Sttaus information in map
					jobReport.put("jobId", jobId.toString());
					jobReport.put("jobName", jobName);
					jobReport.put("jobPriority", jobSts.getJobPriority()
							.toString());
					jobReport.put("jobStartTime", jobSts.getStartTime());

					jobReport.put("userName", jobSts.getUsername());
					jobReport.put("jobComplete", jobSts.isJobComplete());

					jobReport.put("mapProgress", mapProgress);
					jobReport.put("reduceProgress", reduceProgress);

					jobReport.put("mapTotal", mapTotal);
					jobReport.put("reduceTotal", reduceTotal);
					jobReport.put("mapCompleted", mapComp);
					jobReport.put("reduceCompleted", reduceComp);

					jobReport.put("setupProgress", setupProgress);
					jobReport.put("cleanupProgress", cleanupProgress);

					jobReport.put("schedulingInfo", jobSts.getSchedulingInfo());
					jobReport.put("jobState",
							JobStatus.getJobRunState(jobSts.getRunState()));
					jobReport.put("failureInfo", failureInfo);
					jobReport.put("jobFile", job.getJobFile());
					jobReport.put("trackingURL", job.getTrackingURL());
					Counters counters = job.getCounters();
					List counterList = new ArrayList();
					for (Group group : counters) {
						Map<String, Object> counterMap = new HashMap<String, Object>();
						counterMap.put("name", group.getDisplayName());
						List subCounters = new ArrayList();
						for (Counter counter : group) {
							Map subCounter = new HashMap();
							subCounter.put("name", counter.getDisplayName());
							subCounter.put("value", counter.getCounter());
							subCounters.add(subCounter);
						}
						counterMap.put("subCounters", subCounters);
						counterList.add(counterMap);
					}
					jobReport.put("counters", counterList);
					jobReport.put("mapReport",
							getTaskReport(jobClient.getMapTaskReports(jobId)));
					jobReport
							.put("reduceReport", getTaskReport(jobClient
									.getReduceTaskReports(jobId)));
					jobReport.put("cleanupReport", getTaskReport(jobClient
							.getCleanupTaskReports(jobId)));
					jobReport
							.put("setupReport", getTaskReport(jobClient
									.getSetupTaskReports(jobId)));

					// Adding map in list
					jobReports.add(jobReport);
				}
			} else {
				LOGGER.error("Unable to get JobClient...");
			}

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return jobReports;
	}

	/**
	 * Gets the job metrics.
	 * 
	 * @param jobClient
	 *            the job client
	 * @return Map
	 */
	private Map<String, Object> getJobMetrics(JobClient jobClient) {
		// Creating an empty map for storing Hadoop Job Metrics information
		Map<String, Object> hadoopJobMetrics = new HashMap<String, Object>();
		try {
			// Checking for null jobClient
			if (jobClient != null) {
				LOGGER.info("Fetching Hadoop Metrics Information.." + jobClient);
				// Get status information about the Map-Reduce cluster.
				ClusterStatus clusterStatus = jobClient.getClusterStatus();
				// Get the current state of the JobTracker,
				State jobTrackerState = clusterStatus.getJobTrackerState();
				// Get the number of currently running map tasks in the cluster.
				int mapTasks = clusterStatus.getMapTasks();
				// Get the maximum capacity for running map tasks in the
				// cluster.
				int maxMapTasks = clusterStatus.getMaxMapTasks();
				// Get the maximum capacity for running reduce tasks in the
				// cluster.
				int maxReduceTasks = clusterStatus.getMaxReduceTasks();
				// Get the number of currently running reduce tasks in the
				// cluster.
				int reduceTasks = clusterStatus.getReduceTasks();
				// Get the number of active task trackers in the cluster.
				int taskTrackers = clusterStatus.getTaskTrackers();
				// Get the number of blacklisted task trackers in the cluster.
				int blackListedTrackers = clusterStatus
						.getBlacklistedTrackers();
				// Get the maximum configured heap memory that can be used by
				// the JobTracker
				long maxMemory = clusterStatus.getMaxMemory();
				// Get the total heap memory used by the JobTracker
				long usedMemory = clusterStatus.getUsedMemory();
				// Get the tasktracker expiry interval for the cluster
				long ttExpiryInterval = clusterStatus.getTTExpiryInterval();

				int defaultMaps = 0;
				int defaultReduces = 0;
				try {
					defaultMaps = jobClient.getDefaultMaps();
					defaultReduces = jobClient.getDefaultReduces();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Putting Hadoop Metrics information in a map
				hadoopJobMetrics.put("systemDirectoryName", null); // ("systemDirectoryName",
																	// systemDir);
				hadoopJobMetrics.put("defaultMaps", defaultMaps + "");
				hadoopJobMetrics.put("defaultReduces", defaultReduces + "");
				hadoopJobMetrics.put("jobTrackerState", jobTrackerState + "");
				hadoopJobMetrics.put("mapTasks", mapTasks + "");
				hadoopJobMetrics.put("maxMapTasksCapacity", maxMapTasks + "");
				hadoopJobMetrics.put("reduceTasks", reduceTasks + "");
				hadoopJobMetrics.put("maxReduceTasksCapacity", maxReduceTasks
						+ "");
				hadoopJobMetrics.put("taskTrackers", taskTrackers + "");
				hadoopJobMetrics.put("blackListedTrackers", blackListedTrackers
						+ "");
				float byteToMB = 1048576;
				hadoopJobMetrics.put("maxMemory", (maxMemory / byteToMB) + "");
				hadoopJobMetrics
						.put("usedMemory", (usedMemory / byteToMB) + "");
				hadoopJobMetrics.put("taskTrackerExpiryInterval",
						ttExpiryInterval + "");

				hadoopJobMetrics.put("schedulerType", getSchedulerType());

				try {
					// Return an array of queue information objects about all
					// the Job Queues configured.
					JobQueueInfo[] jobQueueInfoArr = jobClient.getQueues();
					int queuedJobClount = 0;
					hadoopJobMetrics
							.put("queuedJobCount", queuedJobClount + "");
					List<String[]> jobQueueInfoLst = new ArrayList<String[]>();
					/*
					 * for(JobQueueInfo queue: queues) { String queueName =
					 * queue.getQueueName(); String state =
					 * queue.getQueueState(); String schedulingInformation =
					 * queue.getSchedulingInfo(); if(schedulingInformation ==
					 * null || schedulingInformation.trim().equals("")) {
					 * schedulingInformation = "NA"; }
					 */
					// Iterating over the queue info
					for (JobQueueInfo jobQueueInfo : jobQueueInfoArr) {
						String[] queueInfoArr = new String[2];
						// Get the queue name from JobQueueInfo
						String queueName = jobQueueInfo.getQueueName();
						// Gets the scheduling information associated to
						// particular job queue.
						String schedulingName = jobQueueInfo
								.getSchedulingInfo();
						queueInfoArr[0] = queueName;
						queueInfoArr[1] = schedulingName;

						jobQueueInfoLst.add(queueInfoArr);
					}
					hadoopJobMetrics.put("queueInfo", jobQueueInfoLst);

				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
				int totalJobSubmission = 0;
				// Get the jobs that are submitted.
				JobStatus[] jobStatus = jobClient.getAllJobs();
				if (jobStatus != null) {
					totalJobSubmission = jobClient.getAllJobs().length;
				}

				List<Map<String, Object>> allJobsList = listAllJobs(jobClient);
				int totalJobRunning = getRunningJobList(allJobsList).size();
				int completedJobs = getCompletedJobs(allJobsList).size();

				hadoopJobMetrics.put("totalJobSubmission", totalJobSubmission
						+ "");
				hadoopJobMetrics.put("totalJobRunning", totalJobRunning + "");
				hadoopJobMetrics.put("totalJobsCompleted", completedJobs + "");
			} else {
				System.err
						.println("Unable to getch Hadoop Metrics as Job Client is null..");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return hadoopJobMetrics;
	}

	/**
	 * Gets the scheduler type.
	 * 
	 * @return String
	 */
	private String getSchedulerType() {
		String schedulerType = "default";
		String hadoopConfPath = conf
				.getStringValue(Constant.PROP_NAME_HADOOP_HOME) + "/conf/";
		// Reading conf parameter value from Hadoop conf file
		Manipulatable manipulator = ManipulatorFactory
				.getInstanceById(Constant.File_Extension.XML);
		String value = manipulator.readConfValue(hadoopConfPath
				+ "mapred-site.xml", "mapred.jobtracker.taskScheduler");
		// Checo=king for not null
		if (value != null) {
			if (value.contains("FairScheduler")) {
				schedulerType = "Fair";
			}
			if (value.contains("CapacityTaskScheduler")) {
				schedulerType = "Capacity";
			}
		}
		// returns the scheduler type
		return schedulerType;
	}

	/**
	 * Gets the running job list.
	 * 
	 * @param allJobsList
	 *            the all jobs list
	 * @return List
	 */
	private List<Map<String, Object>> getRunningJobList(
			List<Map<String, Object>> allJobsList) {
		return getJobList("RUNNING", allJobsList);
	}

	/**
	 * Gets the completed jobs.
	 * 
	 * @param allJobsList
	 *            the all jobs list
	 * @return the completed jobs
	 */
	private List<Map<String, Object>> getCompletedJobs(
			List<Map<String, Object>> allJobsList) {
		return getJobList("SUCCEEDED", allJobsList);
	}

	/**
	 * Getting the job list via state.
	 * 
	 * @param state
	 * @param allJobsList
	 * @return
	 */
	private List<Map<String, Object>> getJobList(String state,
			List<Map<String, Object>> allJobsList) {
		// Creating an empty list of map for storing completed job information
		List<Map<String, Object>> completedJobList = new ArrayList<Map<String, Object>>();
		// Iterating over all jobs list that are submitted to the cluster
		for (Map<String, Object> jAct : allJobsList) {
			// Extracting job state
			String jobState = (String) jAct.get("jobState");
			if (jobState.equals(state)) {
				completedJobList.add(jAct);
			}
		}
		return completedJobList;
	}

	/**
	 * Gets the job client.
	 * 
	 * @return JobClient object
	 */
	public JobClient getJobClient(String host, int port) {
		// JobClient is the primary interface for the user-job to interact with
		// the JobTracker.
		JobClient jobClient = null;
		if (host == null) {
			host = "localhost";
		}
		LOGGER.info("Requesting job Client..");
		try {
			// Getting the NameNode IP from AgentConf
			host = conf.getStringValue(Constant.PROP_NAMENODE_HOST);
			// Provides access to configuration parameters.
			Configuration conf = new Configuration();
			LOGGER.info("JobClient : " + host + " & port : " + port);
			// Build a job client, connect to the indicated job tracker.
			jobClient = new JobClient(new InetSocketAddress(host, port),
					new JobConf(conf));
			// Set the configuration to be used by this object.
			jobClient.setConf(conf);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return jobClient;
	}

	/**
	 * Gets the task report.
	 * 
	 * @param taskReports
	 *            the task reports
	 * @return the task report
	 */
	private Map<String, Object> getTaskReport(TaskReport[] taskReports) {
		Map<String, Object> taskReportsInfo = new HashMap<String, Object>();
		try {
			LOGGER.info("Total Task : " + taskReports.length);
			List<Map> taskLists = new ArrayList<Map>();
			// A report on the state of a task.
			if (taskReports != null) {
				int completeTask = 0;
				int failedTask = 0;
				int killedTask = 0;
				int runningTask = 0;
				int pendingTask = 0;
				Map<String, Object[]> diagInfo = new HashMap<String, Object[]>();
				// Iterating over the task reports
				for (TaskReport mtr : taskReports) {
					// Creating an empty map for storing task details
					Map<String, Object> taskReport = new HashMap<String, Object>();
					// The current status of the task
					TIPStatus currentStatus = mtr.getCurrentStatus();
					// Checking for task's current status COMPLETE
					if (currentStatus == TIPStatus.COMPLETE) {
						completeTask++;
					}
					// Checking for task's current status KILLED
					if (currentStatus == TIPStatus.KILLED) {
						killedTask++;
					}
					// Checking for task's current status RUNNING
					if (currentStatus == TIPStatus.RUNNING) {
						runningTask++;
					}
					// Checking for task's current status PENDING
					if (currentStatus == TIPStatus.PENDING) {
						pendingTask++;
					}
					// The id of the task.
					TaskID taskId = mtr.getTaskID();
					float progress = mtr.getProgress();
					// The most recent state
					String state = mtr.getState();

					// Putting value in a map
					taskReport.put("taskId", taskId.toString());
					taskReport.put("successfulTaskAttemp", mtr
							.getSuccessfulTaskAttempt().toString());
					taskReport.put("startTime", mtr.getStartTime());
					taskReport.put("finishTime", mtr.getFinishTime());
					taskReport.put("progress", progress * 100);
					taskReport.put("state", state);
					taskReport.put("currentStatus", currentStatus);
					Counters counters = mtr.getCounters();
					List countersList = new ArrayList();
					for (Group group : counters) {
						Map<String, Object> counterMap = new HashMap<String, Object>();
						counterMap.put("name", group.getDisplayName());
						List subCounters = new ArrayList();
						for (Counter counter : group) {
							Map subCounter = new HashMap();
							subCounter.put("name", counter.getDisplayName());
							subCounter.put("value", counter.getCounter());
							subCounters.add(subCounter);
						}
						counterMap.put("subCounters", subCounters);
						countersList.add(counterMap);
					}
					taskReport.put("counters", countersList);
					taskLists.add(taskReport);
					// A list of error messages.
					String[] diagnostics = mtr.getDiagnostics();
					if (diagnostics != null) {
						int count = 0;
						// Iterating over the list of error messages
						for (String di : diagnostics) {
							Object[] diagStatus = new Object[2];
							diagStatus[0] = taskId;
							diagStatus[1] = di;
							diagInfo.put(taskId + "_" + count, diagStatus);
							count++;
						}
					}
				}
				// Putting value in a map
				taskReportsInfo.put("completedTask", completeTask);
				taskReportsInfo.put("pendingTask", pendingTask);
				taskReportsInfo.put("killedTask", killedTask);
				taskReportsInfo.put("runningTask", runningTask);
				taskReportsInfo.put("failedTask", failedTask);
				taskReportsInfo.put("failedOrKilledTask", failedTask);
				taskReportsInfo.put("diagInfo", diagInfo);
				taskReportsInfo.put("tasks", taskLists);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return taskReportsInfo;
	}
}
