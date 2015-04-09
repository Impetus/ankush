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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import com.impetus.ankush.common.exception.AnkushException;

import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.hadoop.deployer.configurator.Hadoop1Configurator;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.utils.AnkushUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Hadoop1Monitor.
 * 
 * @author Akhil
 */
public class Hadoop1Monitor extends HadoopMonitor {

	/** The Constant JMX_BEAN_NAME_JOBTRACKER_INFO. */
	public static final String JMX_BEAN_NAME_JOBTRACKER_INFO = "Hadoop:service=JobTracker,name=JobTrackerInfo";

	/** The Constant JMX_BEAN_NAME_METRICS_TASKTRACKER. */
	public static final String JMX_BEAN_NAME_METRICS_TASKTRACKER = "Hadoop:service=TaskTracker,name=TaskTrackerMetrics";

	/** The Constant JMX_BEAN_NAME_METRICS_JOBTRACKER. */
	public static final String JMX_BEAN_NAME_METRICS_JOBTRACKER = "Hadoop:service=JobTracker,name=JobTrackerMetrics";

	/**
	 * Instantiates a new hadoop1 monitor.
	 */
	public Hadoop1Monitor() {
		super();
	}

	/**
	 * Instantiates a new hadoop1 monitor.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param hadoopConfig
	 *            the hadoop config
	 */
	public Hadoop1Monitor(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, Hadoop1Monitor.class);
	}

	/**
	 * Gets the job metrics.
	 * 
	 * @return the job metrics
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public Map<String, Object> getJobMetrics() throws AnkushException {
		String errMsg = "Could not fetch Hadoop job metrics.";
		Map<String, Object> jobMetricsMap = new HashMap<String, Object>();
		try {
			String host = HadoopUtils.getJobTrackerHost(this.compConfig);
			if (!AnkushUtils.getServiceStatus(host,
					HadoopConstants.Roles.JOBTRACKER,
					Constant.Component.Name.HADOOP)) {
				throw new AnkushException(
						"Could not fetch Hadoop job metrics: JobTracker service on "
								+ host + " host is down.");
			}

			JobStatusProvider jobStatusProvider = new JobStatusProvider(
					clusterConfig, compConfig);
			jobMetricsMap
					.put(HadoopConstants.Hadoop.Keys.MONITORING_JOBS_METRICS_DATA_KEY,
							jobStatusProvider.getJobMetrics());

		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
		return jobMetricsMap;
	}

	/**
	 * Gets the job list.
	 * 
	 * @return the job list
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public Map<String, Object> getJobList() throws AnkushException {
		String errMsg = "Could not fetch Hadoop job list.";
		Map<String, Object> jobListMap = new HashMap<String, Object>();
		try {
			String host = HadoopUtils.getJobTrackerHost(this.compConfig);
//			if (!AnkushUtils.getServiceStatus(host,
//					HadoopConstants.Roles.JOBTRACKER,
//					Constant.Component.Name.HADOOP)) {
//				throw new AnkushException(
//						"Could not fetch Hadoop job list: JobTracker service on "
//								+ host + " host is down.");
//			}
			JobStatusProvider jobStatusProvider = new JobStatusProvider(
					clusterConfig, compConfig);
			jobListMap.put(
					HadoopConstants.Hadoop.Keys.MONITORING_JOBS_DATA_KEY,
					jobStatusProvider.listAllJobs());

		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}

		// String nodeIp = HadoopUtils.getJobTrackerHost(this.compConfig);
		//
		// try {
		// /** The node manager. */
		// GenericManager<Node, Long> nodeManager = AppStoreWrapper
		// .getManager(Constant.Manager.NODE, Node.class);
		//
		// GenericManager<NodeMonitoring, Long> monitoringManager =
		// AppStoreWrapper
		// .getManager(Constant.Manager.MONITORING, NodeMonitoring.class);
		//
		// // Get the db node info using public IP
		// Node hadoopNode = nodeManager.getByPropertyValueGuarded("publicIp",
		// nodeIp);
		// if (hadoopNode != null) {
		// Long nodeId = hadoopNode.getId();
		//
		// // Get the db node monitoring info
		// NodeMonitoring nodeMonitoring = monitoringManager
		// .getByPropertyValueGuarded(com.impetus.ankush2.constant.Constant.Keys.NODEID,
		// nodeId);
		// if (nodeMonitoring != null) {
		// String tileStatus = Constant.Tile.Status.NORMAL;
		// List<TileInfo> jobTiles = new ArrayList<TileInfo>();
		// TileInfo ti = null;
		// Map<String, Boolean> serviceStatusMap = nodeMonitoring
		// .getServiceStatus(Constant.Component.Name.HADOOP);
		//
		// if (nodeMonitoring.isAgentDown()) {
		// throw new AnkushException("Ankush Agent is down on " + nodeIp
		// + " . Please resolve related issues.");
		// } else {
		// boolean status = false;
		// if (serviceStatusMap
		// .containsKey(HadoopConstants.Roles.JOBTRACKER)) {
		// status = serviceStatusMap
		// .get(HadoopConstants.Roles.JOBTRACKER);
		// }
		// if (status) {
		// HadoopMonitoringData technologyData = (HadoopMonitoringData)
		// nodeMonitoring
		// .getTechnologyData().get(
		// Constant.Component.Name.HADOOP);
		//
		// ArrayList<HadoopJob> jobInfos = technologyData
		// .getJobInfos();
		//
		// for (HadoopJob job : jobInfos) {
		// job.setCounters(null);
		// job.setMapReport(null);
		// job.setSetupReport(null);
		// job.setReduceReport(null);
		// job.setCleanupReport(null);
		// }
		//
		// jobListMap.put(com.impetus.ankush2.constant.Constant.Keys.TILES,
		// technologyData.getJobTiles());
		// jobListMap.put(com.impetus.ankush2.constant.Constant.Keys.JOBS,
		// jobInfos);
		// } else {
		// tileStatus = Constant.Tile.Status.ERROR;
		// // namenode down tile.
		// ti = new TileInfo(com.impetus.ankush2.constant.Constant.Keys.DOWN,
		// HadoopConstants.Roles.JOBTRACKER,
		// "Could not fetch job data.", null,
		// tileStatus, null);
		// jobTiles.add(ti);
		// jobListMap.put(com.impetus.ankush2.constant.Constant.Keys.TILES,
		// jobTiles);
		// }
		// }
		// } else {
		// throw new AnkushException("Ankush Agent is down on " + nodeIp
		// + " . Please resolve related issues.");
		// }
		// }
		return jobListMap;
		// } catch (AnkushException e) {
		// throw e;
		// } catch (Exception e) {
		// HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
		// Constant.Component.Name.HADOOP, nodeIp, e);
		// throw new AnkushException(errMsg);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getRoleNodesMap()
	 */
	@Override
	public Map<String, Set<String>> getRoleNodesMap() throws AnkushException {
		Map<String, Set<String>> roleNodesMap = new LinkedHashMap<String, Set<String>>();
		try {
			roleNodesMap.put(HadoopConstants.Roles.NAMENODE, Collections
					.singleton(HadoopUtils.getNameNodeHost(this.compConfig)));
			roleNodesMap.put(HadoopConstants.Roles.JOBTRACKER, Collections
					.singleton(HadoopUtils.getJobTrackerHost(this.compConfig)));
			roleNodesMap.put(HadoopConstants.Roles.DATANODE,
					HadoopUtils.getSlaveHosts(this.compConfig));
			roleNodesMap.put(HadoopConstants.Roles.TASKTRACKER,
					HadoopUtils.getSlaveHosts(this.compConfig));

			if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
				roleNodesMap.put(HadoopConstants.Roles.NAMENODE, Collections
						.singleton(HadoopUtils
								.getSecondaryNameNodeHost(this.compConfig)));
			}
		} catch (Exception e) {
			String errMsg = "Could not fetch roles for Hadoop nodes.";
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
		return roleNodesMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getLogFilesList(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> getLogFilesList(String role, String host)
			throws AnkushException {
		String errMsg = "Could not fetch log files for " + role + ".";
		try {
			LogViewHandler logHandler = new LogViewHandler(host,
					this.clusterConfig.getAuthConf());

			String logsDirectory = this.compConfig.getHomeDir()
					+ Hadoop1Configurator.RELPATH_LOGS_DIR;

			// get the list of all log files for a particular role
			List<String> logFilesList = logHandler.getLogFilesList(
					logsDirectory, role);

			if (logFilesList.isEmpty()) {
				throw new AnkushException(errMsg += " " + logsDirectory
						+ " does not contain logs for " + role + ".");
			}

			Map<String, String> logFilesMap = new HashMap<String, String>();
			for (String logFile : logFilesList) {
				logFilesMap.put(logFile, logsDirectory + logFile);
			}
			return logFilesMap;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, host, e);
			throw new AnkushException(errMsg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getMapReduceSummary()
	 */
	@Override
	public LinkedHashMap<String, String> getMapReduceMetrics()
			throws AnkushException {
		String errMsg = "Could not get MapReduce summary.";
		try {
			LinkedHashMap<String, String> mapReduceSummaryMap = new LinkedHashMap<String, String>();

			String beanName = Hadoop1Monitor.JMX_BEAN_NAME_METRICS_JOBTRACKER;
			String jobTrackerHost = HadoopUtils
					.getJobTrackerHost(this.compConfig);
			String httpPort = HadoopUtils
					.getJobTrackerHttpPort(this.compConfig);
			String jobTrackerUiUrl = "http://" + jobTrackerHost + ":"
					+ httpPort + "/";

			Map<String, Object> jobTrackerMetricsBeanObject = HadoopUtils
					.getJmxBeanUsingCallable(jobTrackerHost, httpPort, beanName);

			if (jobTrackerMetricsBeanObject != null) {
				mapReduceSummaryMap = this
						.getMapRedClusterInfoFromBean(jobTrackerMetricsBeanObject);
				String jobTrackerUiKey = HadoopConstants.Hadoop.Keys.JobTrackerJmxKeyDisplayName
						.getKeyDisplayName(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.JOBTRACKERUI);
				mapReduceSummaryMap.put(jobTrackerUiKey, jobTrackerUiUrl);
			} else {
				throw new AnkushException("Could not connect to url-"
						+ jobTrackerUiUrl);
			}

			return mapReduceSummaryMap;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * Gets the map red cluster info from bean.
	 * 
	 * @param jobtrackerMetricsBeanObject
	 *            the jobtracker metrics bean object
	 * @return the map red cluster info from bean
	 */
	private static LinkedHashMap<String, String> getMapRedClusterInfoFromBean(
			Map<String, Object> jobtrackerMetricsBeanObject) {
		LinkedHashMap<String, String> mapredInfo = new LinkedHashMap<String, String>();

		List<String> mapredKeys = new ArrayList<String>();
		mapredKeys
				.add(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.JOBS_RUNNING);
		mapredKeys
				.add(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.JOBS_COMPLETED);
		// mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.JOBS_SUBMITTED);
		mapredKeys
				.add(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_MAP);
		mapredKeys
				.add(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_REDUCE);
		// mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_TOTAL);

		if (jobtrackerMetricsBeanObject != null) {
			for (String mapredkey : mapredKeys) {
				String key = HadoopConstants.Hadoop.Keys.JobTrackerJmxKeyDisplayName
						.getKeyDisplayName(mapredkey);
				String value = String
						.valueOf(((Number) jobtrackerMetricsBeanObject
								.get(mapredkey)));
				mapredInfo.put(key, value);
			}
		}
		return mapredInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getNodesSummary()
	 */
	@Override
	public LinkedHashMap<String, String> getNodesSummary()
			throws AnkushException {
		String errMsg = "Could not get Nodes summary.";
		try {
			LinkedHashMap<String, String> nodesSummaryMap = new LinkedHashMap<String, String>();

			nodesSummaryMap.put(HadoopConstants.Roles.NAMENODE,
					HadoopUtils.getNameNodeHost(this.compConfig));

			if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
				nodesSummaryMap.put(HadoopConstants.Roles.SECONDARYNAMENODE,
						HadoopUtils.getSecondaryNameNodeHost(this.compConfig));
			} else {
				nodesSummaryMap.put(HadoopConstants.Roles.SECONDARYNAMENODE,
						HadoopMonitor.STRING_EMPTY_VALUE);
			}

			nodesSummaryMap.put(HadoopConstants.Roles.JOBTRACKER,
					HadoopUtils.getJobTrackerHost(this.compConfig));

			nodesSummaryMap
					.put(HadoopConstants.Hadoop.Keys.NodesSummary.COUNT_LIVE_DATANODES,
							this.getLiveDataNodesCount());

			nodesSummaryMap
					.put(HadoopConstants.Hadoop.Keys.NodesSummary.COUNT_ACTIVE_TASKTRACKERS,
							this.getActiveMapRedNodesCount());

			return nodesSummaryMap;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getActiveMapRedNodesCount
	 * ()
	 */
	@Override
	public String getActiveMapRedNodesCount() {
		try {
			String jobtrackerHost = HadoopUtils
					.getJobTrackerHost(this.compConfig);
			String jobtrackerHttpPort = HadoopUtils
					.getJobTrackerHttpPort(this.compConfig);
			String beanName = Hadoop1Monitor.JMX_BEAN_NAME_JOBTRACKER_INFO;

			Map<String, Object> beanObject = HadoopUtils
					.getJmxBeanUsingCallable(jobtrackerHost,
							jobtrackerHttpPort, beanName);

			if (beanObject != null) {
				String strActiveNodesInfo = String.valueOf(beanObject
						.get("AliveNodesInfoJson"));
				JSONArray jsonArrayActiveNodeInfo = JsonMapperUtil
						.objectFromString(strActiveNodesInfo, JSONArray.class);
				if (jsonArrayActiveNodeInfo != null) {
					return String.valueOf(jsonArrayActiveNodeInfo.size());
				} else {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not get Active TaskTracker count for Host-"
									+ jobtrackerHost + ", Port-"
									+ jobtrackerHttpPort + ".",
							Constant.Component.Name.HADOOP, jobtrackerHost);
				}
			}
		} catch (AnkushException e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, e.getMessage(),
					Constant.Component.Name.HADOOP, e);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not get Active TaskTracker count.",
					Constant.Component.Name.HADOOP, e);
		}
		return HadoopMonitor.STRING_EMPTY_VALUE;
	}

	@Override
	public LinkedHashMap<String, String> getMapReduceProcessSummary()
			throws AnkushException {
		String errMsg = "Could not get JobTracker process summary.";
		try {
			String jobtrackerHost = HadoopUtils
					.getJobTrackerHost(this.compConfig);
			String jobtrackerHttpPort = HadoopUtils
					.getJobTrackerHttpPort(this.compConfig);
			return getProcessSummaryFromJmx(jobtrackerHost, jobtrackerHttpPort);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	@Override
	public LinkedHashMap<String, String> getProcessSummary(String process)
			throws AnkushException {
		if(process.equals(HadoopConstants.Roles.NAMENODE)) {
			return this.getNameNodeProcessSummary();
		} else if(process.equals(HadoopConstants.Roles.JOBTRACKER)) {
			return this.getMapReduceProcessSummary();
		} else {
			throw new AnkushException("Could not get process summary for " + process + ".");
		}
	}

	public LinkedHashMap<String, Object> getJobDetails(String jobId) throws AnkushException,Exception {
		String errMsg = "Could not fetch Hadoop job details for " + jobId + ".";
		LinkedHashMap<String, Object> jobListMap = new LinkedHashMap<String, Object>();
		try {
			String host = HadoopUtils.getJobTrackerHost(this.compConfig);
			if (!AnkushUtils.getServiceStatus(host,
					HadoopConstants.Roles.JOBTRACKER,
					Constant.Component.Name.HADOOP)) {
				throw new AnkushException(
						"Could not fetch Hadoop job details: JobTracker service on "
								+ host + " host is down.");
			}
			JobStatusProvider jobStatusProvider = new JobStatusProvider(
					clusterConfig, compConfig);
			jobListMap.put(
					HadoopConstants.Hadoop.Keys.MONITORING_JOBS_DATA_KEY,
					jobStatusProvider.getJobStatus(jobId));

		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
		return jobListMap;
	}
}
