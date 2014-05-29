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
package com.impetus.ankush.hadoop.job;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.AnkushRestClient;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.hadoop.CallableJmxBeanData;
import com.impetus.ankush.hadoop.HadoopTileViewHandler;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Deployer;

/**
 * @author Akhil
 *
 */
public class HadoopJobsManager {

	/** The LOGGER. */
	private static final AnkushLogger LOG = new AnkushLogger(HadoopJobsManager.class);
	
	private static final String RM_API_CLUSTER_METRICS = "metrics";
	
	public static final String JMX_BEAN_NAME_METRICS_JOBTRACKER = "Hadoop:service=JobTracker,name=JobTrackerMetrics";
	
	public static final String JMX_BEAN_NAME_METRICS_RESOURCEMANAGER = "Hadoop:service=,name=JobTrackerMetrics";
	
	public static final String JMX_BEAN_NAME_METRICS_TASKTRACKER = "Hadoop:service=TaskTracker,name=TaskTrackerMetrics";
	
	public static final String JMX_BEAN_NAME_JOBTRACKER_INFO = "Hadoop:service=JobTracker,name=JobTrackerInfo";
	
	public static Map<String, String> getMapRedClusterData(String nodeIp, int clientPort) throws Exception
	{
		String beanName = HadoopJobsManager.JMX_BEAN_NAME_METRICS_JOBTRACKER;
		Map<String, String> mapredData = null;
		
		Map<String, Object> jobTrackerMetricsBeanObject = HadoopUtils.getJmxBeanData(nodeIp, clientPort, beanName);
		if(jobTrackerMetricsBeanObject != null) {
			mapredData = HadoopJobsManager.getMapRedClusterInfoFromBean(jobTrackerMetricsBeanObject);
		} else {
			return null;
		}
		String startTimeValue = HadoopUtils.getProcessStartTime(nodeIp, clientPort);
		String startTimeKey = Constant.Hadoop.Keys.JobTrackerJmxKeyDisplayName.getKeyDisplayName(Constant.Hadoop.Keys.JobTrackerJmxInfo.STARTTIME);
		mapredData.put(startTimeKey, startTimeValue);
		return mapredData;
	}
	
	public static String getActiveTaskTrackersCount(HadoopConf hConf) {
		String nodeIp = hConf.getNamenode().getPublicIp();
		int clientPort = Integer.parseInt(Hadoop1Deployer.DEFAULT_PORT_HTTP_JOBTRACKER);
		String valActiveTaskTrackerCount = null;
		String errMsgActiveTaskTracker = "Could not fetch active TaskTrackers count."; 
		
		if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.AGENT)) {
			valActiveTaskTrackerCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
			LOG.error("Agent Down on " + nodeIp + ": " + errMsgActiveTaskTracker);
		} else if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.JOBTRACKER)) {
			valActiveTaskTrackerCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
			LOG.error(Constant.Role.JOBTRACKER + " Down on " + nodeIp + ": " + errMsgActiveTaskTracker);
		} else {
			valActiveTaskTrackerCount = HadoopJobsManager.getActiveTaskTrackersCount(nodeIp, clientPort);
			if(valActiveTaskTrackerCount == null) {
				valActiveTaskTrackerCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
				LOG.error("Error: " + errMsgActiveTaskTracker);	
			}
		}
		return valActiveTaskTrackerCount;
	}
	
	public static String getActiveNodeManagersCount(Hadoop2Conf h2Conf) {
		String nodeIp = h2Conf.getResourceManagerNode().getPublicIp();
		int clientPort = Integer.parseInt(Hadoop2Deployer.DEFAULT_PORT_HTTP_RESOURCEMANAGER);
		
		String valActiveNodeManagersCount = null;
		String errMsgActiveNodeManagers = "Could not fetch active NodeManagers count"; 
		
		if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.AGENT)) {
			valActiveNodeManagersCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
			LOG.error("Agent Down on " + nodeIp + ": " + errMsgActiveNodeManagers);
		} else if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.RESOURCEMANAGER)) {
			valActiveNodeManagersCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
			LOG.error(Constant.Role.RESOURCEMANAGER + " Down on " + nodeIp + ": " + errMsgActiveNodeManagers);
		} else {
			valActiveNodeManagersCount = HadoopJobsManager.getActiveNodeManagersCount(nodeIp, clientPort);
			if(valActiveNodeManagersCount == null) {
				valActiveNodeManagersCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
				LOG.error("Error: " + errMsgActiveNodeManagers);	
			}
		}
		return valActiveNodeManagersCount;
	}
	
	private static String getActiveNodeManagersCount(String nodeIp, int clientPort)
	{
		// To be changed
		try {
				JSONObject clusterMetrics = HadoopJobsManager.getRMClusterMetricsJson(nodeIp, clientPort);
				if(clusterMetrics == null) {
					LOG.error("Error: Unable to get RM Cluster Metrics object.");
					return null;
				}
				String strActiveNodesInfo = String.valueOf(((Number)clusterMetrics.get(Constant.Keys.YARN.ClusterMetrics.NODES_ACTIVE)));
				return strActiveNodesInfo;
		} catch (Exception e) {
			LOG.error("Exception: " + e.getMessage());
		}
		return null;
	}
	
	private static String getActiveTaskTrackersCount(String nodeIp, int clientPort)
	{
		try {
			String beanName = HadoopJobsManager.JMX_BEAN_NAME_JOBTRACKER_INFO;
			long waitTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
					"hadoop.jmxmonitoring.wait.time");
			CallableJmxBeanData callableJmxBeanData = new CallableJmxBeanData(nodeIp, clientPort, beanName);
			FutureTask<Map<String, Object>> futureTaskJmxBeanData = new FutureTask<Map<String, Object>>(callableJmxBeanData);
			
			AppStoreWrapper.getExecutor().execute(futureTaskJmxBeanData);
			
		    Map<String, Object> jobTrackerMetricsBeanObject = futureTaskJmxBeanData.get(waitTime, TimeUnit.MILLISECONDS);
		    
		    if(jobTrackerMetricsBeanObject != null) {
		    	String strActiveNodesInfo = String.valueOf(jobTrackerMetricsBeanObject.get("AliveNodesInfoJson"));
				JSONArray jsonArrayActiveNodeInfo = JsonMapperUtil.objectFromString(strActiveNodesInfo, JSONArray.class);
				if(jsonArrayActiveNodeInfo != null) {
					return String.valueOf(jsonArrayActiveNodeInfo.size());
				}	
		    }
		} catch (Exception e) {
			LOG.error("Exception: " + e.getMessage());
		}
		return null;
	}
	
	public static Map<String, String> getMapRedNodeData(String nodeIp, int clientPort) throws Exception
	{
		Map<String, String> mapredData = new HashMap<String, String>();
		
		String beanName = HadoopJobsManager.JMX_BEAN_NAME_METRICS_TASKTRACKER;
		Map<String, Object> taskTrackerMetricsBeanObject = HadoopUtils.getJmxBeanData(nodeIp, clientPort, beanName);
		if(taskTrackerMetricsBeanObject != null) {
			mapredData.putAll(HadoopJobsManager.getMapRedNodeInfoFromBean(taskTrackerMetricsBeanObject));
		}
		String startTimeValue = HadoopUtils.getProcessStartTime(nodeIp, clientPort);
		String startTimeKey = HadoopUtils.getDisplayNameForStartTime(Constant.Role.TASKTRACKER);
		mapredData.put(startTimeKey, startTimeValue);

		return mapredData;
	}
	
	public static Map<String, String> getYarnNodeData(String nodeIp, int clientPort) throws Exception
	{
		Map<String, String> mapredData = new HashMap<String, String>();
		
		String beanName = HadoopJobsManager.JMX_BEAN_NAME_METRICS_JOBTRACKER;
		Map<String, Object> jobTrackerMetricsBeanObject = HadoopUtils.getJmxBeanData(nodeIp, clientPort, beanName);
		if(jobTrackerMetricsBeanObject != null) {
			//mapredData.putAll(HadoopJobsManager.getMapRedClusterInfoFromBean(jobTrackerMetricsBeanObject));
		}
		String startTimeValue = HadoopUtils.getProcessStartTime(nodeIp, clientPort);
		String startTimeKey = Constant.Hadoop.Keys.JobTrackerJmxKeyDisplayName.getKeyDisplayName(Constant.Hadoop.Keys.JobTrackerJmxInfo.STARTTIME);
		mapredData.put(startTimeKey, startTimeValue);

		return mapredData;
	}
	
	private static Map<String, String> getMapRedClusterInfoFromBean(Map<String, Object> jobtrackerMetricsBeanObject) {
		Map<String, String> mapredInfo = new LinkedHashMap<String, String>();
		
		List<String> mapredKeys = new ArrayList<String>();
		mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.JOBS_RUNNING);
		mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.JOBS_COMPLETED);
		//mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.JOBS_SUBMITTED);
		mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_MAP);
		mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_REDUCE);
		//mapredKeys.add(Constant.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_TOTAL);
		
		if(jobtrackerMetricsBeanObject != null) {
			for(String mapredkey : mapredKeys) {
				String key = Constant.Hadoop.Keys.JobTrackerJmxKeyDisplayName.getKeyDisplayName(mapredkey);
				String value = String.valueOf(((Number)jobtrackerMetricsBeanObject.get(mapredkey)));
				mapredInfo.put(key, value);
			}
		}
		return mapredInfo;
	}
	
	private static Map<String, String> getMapRedNodeInfoFromBean(Map<String, Object> tasktrackerMetricsBeanObject) {
		Map<String, String> mapredInfo = new HashMap<String, String>();
		
		List<String> mapredKeys = new ArrayList<String>();
		mapredKeys.add(Constant.Hadoop.Keys.TaskTrackerJmxInfo.MAP_TASK_SLOTS);
		mapredKeys.add(Constant.Hadoop.Keys.TaskTrackerJmxInfo.REDUCE_TASK_SLOTS);
		mapredKeys.add(Constant.Hadoop.Keys.TaskTrackerJmxInfo.MAPS_RUNNING);
		mapredKeys.add(Constant.Hadoop.Keys.TaskTrackerJmxInfo.REDUCES_RUNNING);
		mapredKeys.add(Constant.Hadoop.Keys.TaskTrackerJmxInfo.TASKS_COMPLETED);

		if(tasktrackerMetricsBeanObject != null) {
			for(String mapredkey : mapredKeys) {
				String key = Constant.Hadoop.Keys.TaskTrackerJmxKeyDisplayName.getKeyDisplayName(mapredkey);
				String value = String.valueOf(((Number)tasktrackerMetricsBeanObject.get(mapredkey)));
				mapredInfo.put(key, value);
			}
		}
		return mapredInfo;
	}
	
	public static JSONObject getRMClusterMetricsJson(String nodeIp, int clientPort) {
		JSONObject clusterMetrics = null;
		String url = "http://" + nodeIp + ":" + clientPort
				+ Hadoop2Deployer.RELATIVE_URL_RM_REST_API + HadoopJobsManager.RM_API_CLUSTER_METRICS;
		
		try {
			JSONObject json = null;
			AnkushRestClient restClient = new AnkushRestClient();
			
			String data = restClient.getRequest(url);
			if(data == null) {
				return null;
			} else {
				json = JsonMapperUtil.objectFromString(data, JSONObject.class);
				if (json == null || json.isEmpty()) {
					return null;
				}
			}
			clusterMetrics = (JSONObject) json.get(Constant.Keys.YARN.CLUSTER_METRICS);
			if (clusterMetrics == null || clusterMetrics.isEmpty()) {
				return null;
			}	
		}
		catch (Exception e) {
			LOG.error(e.getMessage());
			return null;
		}
		return clusterMetrics;
	}
	
	public static Map<String, String> getYarnClusterData(String nodeIp, int clientPort) throws Exception
	{
		Map<String, String> yarnData = new LinkedHashMap<String, String>();
		
		JSONObject clusterMetrics = HadoopJobsManager.getRMClusterMetricsJson(nodeIp, clientPort);
		if(clusterMetrics == null) {
			LOG.error("Error: Unable to get RM Cluster Metrics object.");
		}
		
		List<String> yarnKeys = new ArrayList<String>();
		yarnKeys.add(Constant.Keys.YARN.ClusterMetrics.APPS_RUNNING);
		yarnKeys.add(Constant.Keys.YARN.ClusterMetrics.APPS_COMPLETED);
		if(clusterMetrics != null) {
			for(String yarnkey : yarnKeys) {
				String key = Constant.Keys.YARN.RmApiDisplayName.getKeyDisplayName(yarnkey);
				String value = String.valueOf(((Number)clusterMetrics.get(yarnkey)));
				yarnData.put(key, value);
			}
		}
		yarnKeys.clear();
		yarnKeys.add(Constant.Keys.YARN.ClusterMetrics.MEMORY_AVAILABLE);
		yarnKeys.add(Constant.Keys.YARN.ClusterMetrics.MEMORY_TOTAL);
		
		if(clusterMetrics != null) {
			for(String yarnkey : yarnKeys) {
				String key = Constant.Keys.YARN.RmApiDisplayName.getKeyDisplayName(yarnkey);
				String value = HadoopJobsManager.convertMbIntoGb
								(Long.parseLong(String.valueOf(
								  ((Number)clusterMetrics.get(yarnkey)))));
				yarnData.put(key, value);
			}
		}
		String startTimeValue = HadoopUtils.getProcessStartTime(nodeIp, clientPort);
		String startTimeKey = Constant.Keys.YARN.RmApiDisplayName.getKeyDisplayName(Constant.Keys.YARN.ClusterMetrics.STARTTIME);
		yarnData.put(startTimeKey, startTimeValue);
		return yarnData;
	}
	
	private static String convertMbIntoGb(long megaBytes){
		String convertedVal = "0";
		if (megaBytes == 0) {
			return convertedVal;
		}

		final double HUNDRED = 100.0;
		final long DIVEDEBY = 1024L;
		
		DecimalFormat df = new DecimalFormat("0.00");

		if (megaBytes / DIVEDEBY > 0) {
			return (df.format(((megaBytes * HUNDRED / DIVEDEBY)) / HUNDRED) + "GB");
		} else {
			return (String.valueOf(megaBytes) + "MB");
		}
	}
	
}
