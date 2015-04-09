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
package com.impetus.ankush2.hadoop.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;

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

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush2.common.scripting.impl.ReadConfProperty;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * @author Akhil
 * 
 */
public class JobStatusProvider {

	JobClient jobClient;

	String jobTrackerRpcPort;

	String jobTrackerHost;

	/**
	 * @param clusterConfig
	 * @param compConfig
	 */
	public JobStatusProvider(ClusterConfig clusterConfig,
			ComponentConfig compConfig) {
		super();
		this.clusterConfig = clusterConfig;
		this.compConfig = compConfig;

		// job tracker port.
		jobTrackerRpcPort = HadoopUtils.getJobTrackerRpcPort(this.compConfig);

		// job tracker host
		jobTrackerHost = HadoopUtils.getJobTrackerHost(this.compConfig);
		
		jobClient = getJobClient(jobTrackerHost, jobTrackerRpcPort);
	}

	/** The cluster config. */
	private ClusterConfig clusterConfig;

	/** The hadoop config. */
	private ComponentConfig compConfig;

	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(JobStatusProvider.class);
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

	/**
	 * Gets the job metrics.
	 * 
	 * @return List
	 */
	public Map<String, Object> getJobMetrics() throws AnkushException {
		String errMsg = "Unable to getch Hadoop Metrics, could not connect to Hadoop JobClient.";
		try {
			// Checking for !null
			if (jobClient != null) {
				// Creating an empty map for storing Hadoop Job Metrics information
				LinkedHashMap<String, Object> hadoopJobMetrics = new LinkedHashMap<String, Object>();
				try {
					// Checking for null jobClient
					if (jobClient != null) {
						LOG.info("Fetching Hadoop Metrics Information.." + jobClient);
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

						long ttExpiryInterval = clusterStatus.getTTExpiryInterval();

						int defaultMaps = 0;
						int defaultReduces = 0;
						try {
							defaultMaps = jobClient.getDefaultMaps();
							defaultReduces = jobClient.getDefaultReduces();
						} catch (Exception e) {
							
							//e.printStackTrace();
						}

						// Putting Hadoop Metrics information in a map
						hadoopJobMetrics.put("jobTrackerState",
								String.valueOf(jobTrackerState));
						hadoopJobMetrics
								.put("defaultMaps", String.valueOf(defaultMaps));
						hadoopJobMetrics.put("defaultReduces",
								String.valueOf(defaultReduces));
						hadoopJobMetrics.put("mapTasks", String.valueOf(mapTasks));
						hadoopJobMetrics
								.put("reduceTasks", String.valueOf(reduceTasks));
						hadoopJobMetrics.put("maxMapTasksCapacity",
								String.valueOf(maxMapTasks));
						hadoopJobMetrics.put("maxReduceTasksCapacity",
								String.valueOf(maxReduceTasks));
						hadoopJobMetrics.put("taskTrackers",
								String.valueOf(taskTrackers));
						hadoopJobMetrics.put("blackListedTrackers",
								String.valueOf(blackListedTrackers));

						hadoopJobMetrics.put("taskTrackerExpiryInterval",
								String.valueOf(ttExpiryInterval));

						hadoopJobMetrics.put("schedulerType", getSchedulerType());

						int totalJobSubmission = 0;
						// Get the jobs that are submitted.
						JobStatus[] jobStatus = jobClient.getAllJobs();
						if (jobStatus != null) {
							totalJobSubmission = jobClient.getAllJobs().length;
						}

						List<Map<String, Object>> allJobsList = listAllJobs();
						int totalJobRunning = getRunningJobList(allJobsList).size();
						int completedJobs = getCompletedJobs(allJobsList).size();

						hadoopJobMetrics.put("totalJobSubmission",
								String.valueOf(totalJobSubmission));
						hadoopJobMetrics.put("totalJobRunning",
								String.valueOf(totalJobRunning));
						hadoopJobMetrics.put("totalJobsCompleted",
								String.valueOf(completedJobs));
					} else {
						HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
								errMsg, Constant.Component.Name.HADOOP);
						throw new AnkushException(errMsg);
					}
				} catch (AnkushException e) {
					throw e;
				} catch (Exception e) {
					HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
							Constant.Component.Name.HADOOP, e);
					throw new AnkushException(errMsg);
				}
				return hadoopJobMetrics;
			} else {
				throw new AnkushException(errMsg);
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * Method getJobStatus.
	 * 
	 * @return Map<Object,Object> The Map for Job Status Value.
	 */
	public Map<String, Object> getJobStatus(String jobId)
			throws AnkushException {
		String errMsg = "Unable to getch Hadoop jobs list, could not connect to Hadoop JobClient.";
		// Creating an empty list of map for storing job Status information
		try {
			return getJobDetails(jobClient, jobId);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * List all jobs.
	 * 
	 * @param jobClient
	 *            the job client
	 * @return the list
	 * @throws InterruptedException
	 */
	public List<Map<String, Object>> listAllJobs()
			throws AnkushException, InterruptedException {
		// Creating an empty list of map for storing job Status information
		String errMsg = "Unable to getch Hadoop jobs list, could not connect to Hadoop JobClient.";
		List<Map<String, Object>> jobReports = new ArrayList<Map<String, Object>>();
		try {
			// Checking for jobClient null
			if (jobClient != null) {
				// Get the jobs that are submitted.
				JobStatus[] jobStatus = jobClient.getAllJobs();
				// Iterating over the list of all submitted jobs
				for (JobStatus jobSts : jobStatus) {
					jobReports.add(getJobReport(jobSts));
				}
			} else {
				HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
						errMsg, Constant.Component.Name.HADOOP);
				throw new AnkushException(errMsg);
			}

		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
		return jobReports;
	}

	/**
	 * @param jobClient
	 * @param jobSts
	 * @return
	 * @throws IOException
	 */
	private Map<String, Object> getJobReport(JobStatus jobSts) throws IOException {
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
			TaskReport[] mapTaskReports = jobClient.getMapTaskReports(jobId);
			// Get the total map
			mapTotal = mapTaskReports.length;
			// Iterating over the map tasks
			for (TaskReport taskReport : mapTaskReports) {
				// The current state of a map TaskInProgress as seen
				// by the JobTracker.
				TIPStatus currentStatus = taskReport.getCurrentStatus();
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
				TIPStatus currentStatus = taskReport.getCurrentStatus();
				if (currentStatus == TIPStatus.COMPLETE) {
					reduceComp++;
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
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
		jobReport.put("jobPriority", jobSts.getJobPriority().toString());
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

		jobReport.putAll(getDetailedJobReport(jobId));
		return jobReport;
	}

	/**
	 * @param jobClient
	 * @param jobReport
	 * @param jobId
	 * @param job
	 * @throws IOException
	 */
	private Map<String, Object> getDetailedJobReport(org.apache.hadoop.mapred.JobID jobId) throws IOException {
		Map<String, Object> jobDetailedReport = new HashMap<String, Object>();

		RunningJob job = jobClient.getJob(jobId);
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
		jobDetailedReport.put("counters", counterList);
		jobDetailedReport.put("mapReport",
				getTaskReport(jobClient.getMapTaskReports(jobId)));
		jobDetailedReport.put("reduceReport",
				getTaskReport(jobClient.getReduceTaskReports(jobId)));
		jobDetailedReport.put("cleanupReport",
				getTaskReport(jobClient.getCleanupTaskReports(jobId)));
		jobDetailedReport.put("setupReport",
				getTaskReport(jobClient.getSetupTaskReports(jobId)));
		return jobDetailedReport;
	}

	public Map<String, Object> getJobDetails(JobClient jobClient, String jobId)
			throws AnkushException {
		String errMsg = "Unable to getch Hadoop jobs details, could not connect to Hadoop JobClient.";
		try {
			if (jobClient != null) {
				// Get the jobs that are submitted.
				JobStatus[] jobStatus = jobClient.getAllJobs();
				for (JobStatus jobSts : jobStatus) {

				}
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
		return null;
	}

	/**
	 * Gets the scheduler type.
	 * 
	 * @return String
	 */
	private String getSchedulerType() {
		String schedulerType = "default";
		try {
			String hadoopConfPath = HadoopUtils
					.getHadoopConfDir(this.compConfig);
			String mapredFilePath = hadoopConfPath
					+ HadoopConstants.FileName.ConfigurationFile.XML_MAPRED_SITE;

			AnkushTask readXmlProperty = new ReadConfProperty(
					"mapred.jobtracker.taskScheduler", mapredFilePath,
					Constant.File_Extension.XML,
					this.clusterConfig.getAgentInstallDir());

			NodeConfig jobTrackerHost = this.clusterConfig.getNodes().get(
					HadoopUtils.getJobTrackerHost(compConfig));
			Result result = jobTrackerHost.getConnection()
					.exec(readXmlProperty);

			if (result.isSuccess) {
				// Checking for not null
				if (result.sysout != null) {
					if (result.sysout.contains("FairScheduler")) {
						schedulerType = "Fair";
					}
					if (result.sysout.contains("CapacityTaskScheduler")) {
						schedulerType = "Capacity";
					}
				}
			}
		} catch (Exception e) {
			HadoopUtils
					.addAndLogError(
							this.LOG,
							this.clusterConfig,
							"Could not get Scheduler type from "
									+ HadoopConstants.FileName.ConfigurationFile.XML_MAPRED_SITE
									+ " file.", Constant.Component.Name.HADOOP,
							e);
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
	private JobClient getJobClient(String host, String port) {
		// JobClient is the primary interface for the user-job to interact with
		// the JobTracker.
		JobClient jobClient = null;
		if (host == null) {
			host = "localhost";
		}
		LOG.info("Requesting job Client..");
		try {
			// Provides access to configuration parameters.
			Configuration conf = new Configuration();
			LOG.info("JobClient : " + host + " & port : " + port);
			// Build a job client, connect to the indicated job tracker.
			jobClient = new JobClient(new InetSocketAddress(host,
					Integer.parseInt(port)), new JobConf(conf));
			// Set the configuration to be used by this object.
			jobClient.setConf(conf);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
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
			LOG.info("Total Task : " + taskReports.length);
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
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not get task report",
					Constant.Component.Name.HADOOP, e);
		}
		return taskReportsInfo;
	}

}
