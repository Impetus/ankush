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

import org.json.simple.JSONObject;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class Hadoop2Monitor.
 * 
 * @author Akhil
 */
public class Hadoop2Monitor extends HadoopMonitor {

	/** The Constant RELATIVE_URL_RM_REST_API. */
	public static final String RELATIVE_URL_RM_REST_API = "/ws/v1/cluster/";

	/** The Constant APPPROXY_REST_API_TEXT_APPID. */
	public static final String APPPROXY_REST_API_TEXT_APPID = "<application_id>";

	/** The Constant RELATIVE_URL_JHS_REST_API. */
	public static final String RELATIVE_URL_JHS_REST_API = "/ws/v1/history/mapreduce/jobs/";

	/** The Constant YARN_REST_API_APP_ATTEMPTS. */
	public static final String YARN_REST_API_APP_ATTEMPTS = "appattempts";

	/** The Constant RELATIVE_URL_APPPROXY_REST_API. */
	public static final String RELATIVE_URL_APPPROXY_REST_API = "/proxy/<application_id>/ws/v1/mapreduce/jobs/";

	/** The Constant YARN_REST_API_APPS. */
	public static final String YARN_REST_API_APPS = "apps";

	public static final String RM_API_CLUSTER_METRICS = "metrics";

	/**
	 * Instantiates a new hadoop2 monitor.
	 */
	public Hadoop2Monitor() {
		super();
	}

	/**
	 * Instantiates a new hadoop2 monitor.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param hadoopConfig
	 *            the hadoop config
	 */
	public Hadoop2Monitor(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, Hadoop2Monitor.class);
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

			boolean haEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

			if (haEnabled) {
				roleNodesMap.put(HadoopConstants.Roles.NAMENODE,
						HadoopUtils.getHaNameNodeHosts(compConfig));
			} else {
				roleNodesMap.put(HadoopConstants.Roles.NAMENODE,
						Collections.singleton(HadoopUtils
								.getNameNodeHost(this.compConfig)));
				if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
					roleNodesMap
							.put(HadoopConstants.Roles.NAMENODE,
									Collections.singleton(HadoopUtils
											.getSecondaryNameNodeHost(this.compConfig)));
				}
			}

			roleNodesMap.put(HadoopConstants.Roles.RESOURCEMANAGER, Collections
					.singleton(HadoopUtils
							.getResourceManagerHost(this.compConfig)));

			roleNodesMap.put(HadoopConstants.Roles.DATANODE,
					HadoopUtils.getSlaveHosts(this.compConfig));

			roleNodesMap.put(HadoopConstants.Roles.NODEMANAGER,
					HadoopUtils.getSlaveHosts(this.compConfig));

			boolean webAppProxyServerEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.WEBAPPPROXYSERVER_ENABLED);

			if (webAppProxyServerEnabled) {
				roleNodesMap.put(HadoopConstants.Roles.WEBAPPPROXYSERVER,
						Collections.singleton(HadoopUtils
								.getWebAppProxyServerHost(this.compConfig)));
			}

			boolean jobHistoryServerEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.JOBHISTORYSERVER_ENABLED);

			if (jobHistoryServerEnabled) {
				roleNodesMap.put(HadoopConstants.Roles.JOBHISTORYSERVER,
						Collections.singleton(HadoopUtils
								.getJobHistoryServerHost(this.compConfig)));
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
			if (role.equals(HadoopConstants.Roles.JOBHISTORYSERVER)) {
				role = "HistoryServer";
			} else if (role.equals(HadoopConstants.Roles.WEBAPPPROXYSERVER)) {
				role = "ProxyServer";
			}
			return super.getLogFilesList(role, host);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, host, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * Gets the application details.
	 * 
	 * @param appId
	 *            the app id
	 * @return the application details
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public Map<String, Object> getApplicationDetails(String appId)
			throws AnkushException {
		String errMsg = "Could not fetch application details from RM REST API";
		try {
			String relativeUrl = Hadoop2Monitor.YARN_REST_API_APPS + "/"
					+ appId;
			String rmHttpPort = HadoopUtils
					.getResourceManagerHttpPort(this.compConfig);
			String rmHttpHost = HadoopUtils
					.getResourceManagerHost(this.compConfig);

			JSONObject json = HadoopUtils.getJsonFromRmRestApi(rmHttpHost,
					rmHttpPort, relativeUrl);

			if (json == null || json.isEmpty()) {
				throw new AnkushException(errMsg);
			}

			JSONObject applicationDetails = (JSONObject) json
					.get(HadoopConstants.YARN.APP);

			relativeUrl += "/" + Hadoop2Monitor.YARN_REST_API_APP_ATTEMPTS;

			JSONObject appAttempts = (JSONObject) (HadoopUtils
					.getJsonFromRmRestApi(rmHttpHost, rmHttpPort, relativeUrl)
					.get(HadoopConstants.YARN.APPATTEMPTS));
			List<JSONObject> appAttemptsList = (List<JSONObject>) appAttempts
					.get(HadoopConstants.YARN.APPATTEMPT);

			// Hadoop2Application hadoopApp = JsonMapperUtil
			// .objectFromString(applicationDetails.toJSONString(),
			// Hadoop2Application.class);

			Map<String, Object> applicationDetailsMap = new HashMap<String, Object>();
			applicationDetailsMap.put(HadoopConstants.YARN.JSON_APPDETAILS,
					applicationDetails);
			applicationDetailsMap.put(HadoopConstants.YARN.JSON_APPATTEMPTS,
					appAttemptsList);
			return applicationDetailsMap;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}

	}

	/**
	 * Gets the application list.
	 * 
	 * @return the application list
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public Map<String, Object> getApplicationList() throws AnkushException {
		String errMsg = "Could not fetch application list from RM REST API";
		
		String relativeUrl = Hadoop2Monitor.YARN_REST_API_APPS;
		String rmHttpPort = HadoopUtils
				.getResourceManagerHttpPort(this.compConfig);
		String rmHttpHost = HadoopUtils.getResourceManagerHost(this.compConfig);

		JSONObject json = HadoopUtils.getJsonFromRmRestApi(rmHttpHost,
				rmHttpPort, relativeUrl);
		if (json == null) {
			throw new AnkushException(errMsg);
		}
		Map<String, Object> applicationListMap = new HashMap<String, Object>();
		JSONObject apps = (JSONObject) json.get(HadoopConstants.YARN.APPS);
		if (apps == null) {
			throw new AnkushException("No applications found: Total number of applications is ZERO");
		}

		List<JSONObject> applicationListJson = (List<JSONObject>) apps
				.get(HadoopConstants.YARN.APP);
		if (applicationListJson == null) {
			throw new AnkushException("No applications found: Total number of applications is ZERO");
		}
		
		applicationListMap.put(HadoopConstants.YARN.JSON_HADOOP2APPLIST,
				applicationListJson);
		return applicationListMap;
	}

	@Override
	public LinkedHashMap<String, String> getNodesSummary()
			throws AnkushException {
		String errMsg = "Could not get Nodes summary.";
		try {
			LinkedHashMap<String, String> nodesSummaryMap = new LinkedHashMap<String, String>();

			boolean haEnabled = (Boolean) this.compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.HA_ENABLED);

			if (haEnabled) {
				String namenodeId1 = HadoopUtils.getHaNameNodeId1(compConfig);

				String namenodeId2 = HadoopUtils.getHaNameNodeId2(compConfig);

				String activeNameNode = HadoopUtils
						.getActiveNameNodeHost(compConfig);
				if (activeNameNode == null)
					activeNameNode = HadoopMonitor.STRING_EMPTY_VALUE;

				String standByNameNode = HadoopUtils
						.getStandByNameNodeHost(compConfig);
				if (standByNameNode == null)
					standByNameNode = HadoopMonitor.STRING_EMPTY_VALUE;

				nodesSummaryMap.put(
						HadoopConstants.Hadoop.Keys.NodesSummary.NAMENODE_ID1,
						activeNameNode + "(" + namenodeId1 + ")");
				
				nodesSummaryMap.put(
						HadoopConstants.Hadoop.Keys.NodesSummary.NAMENODE_ID2,
						standByNameNode + "(" + namenodeId2 + ")");

			} else {
				nodesSummaryMap.put(HadoopConstants.Roles.NAMENODE,
						HadoopUtils.getNameNodeHost(this.compConfig));

				if (HadoopUtils.getSecondaryNameNodeHost(this.compConfig) != null) {
					nodesSummaryMap.put(
							HadoopConstants.Roles.SECONDARYNAMENODE,
							HadoopUtils
									.getSecondaryNameNodeHost(this.compConfig));
				} else {
					nodesSummaryMap.put(
							HadoopConstants.Roles.SECONDARYNAMENODE,
							HadoopMonitor.STRING_EMPTY_VALUE);
				}
			}

			nodesSummaryMap.put(HadoopConstants.Roles.RESOURCEMANAGER,
					HadoopUtils.getResourceManagerHost(this.compConfig));

			nodesSummaryMap
					.put(HadoopConstants.Hadoop.Keys.NodesSummary.COUNT_LIVE_DATANODES,
							this.getLiveDataNodesCount());

			nodesSummaryMap
					.put(HadoopConstants.Hadoop.Keys.NodesSummary.COUNT_ACTIVE_NODEMANAGERS,
							this.getActiveMapRedNodesCount());

			return nodesSummaryMap;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	@Override
	public String getActiveMapRedNodesCount() throws AnkushException {
		String errMsg = "Could not get YARN active NodeManagers count.";
		try {

			JSONObject clusterMetrics = this.getYarnClusterMetrics();

			if (clusterMetrics == null || clusterMetrics.isEmpty()) {
				throw new AnkushException(errMsg);
			}

			return String.valueOf((Number) clusterMetrics
					.get(HadoopConstants.YARN.ClusterMetrics.NODES_ACTIVE));
		} catch (AnkushException e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, e.getMessage(),
					Constant.Component.Name.HADOOP, e);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not fetch Active NodeManager count.",
					Constant.Component.Name.HADOOP, e);
		}
		return HadoopMonitor.STRING_EMPTY_VALUE;
	}

	@Override
	public LinkedHashMap<String, String> getMapReduceMetrics()
			throws AnkushException {
		String errMsg = "Could not get YARN summary.";
		LinkedHashMap<String, String> yarnSummaryMap = new LinkedHashMap<String, String>();
		try {
			JSONObject clusterMetrics = this.getYarnClusterMetrics();

			List<String> yarnKeys = new ArrayList<String>();
			yarnKeys.add(HadoopConstants.YARN.ClusterMetrics.APPS_RUNNING);
			yarnKeys.add(HadoopConstants.YARN.ClusterMetrics.APPS_COMPLETED);
			if (clusterMetrics != null) {
				for (String yarnkey : yarnKeys) {
					String key = HadoopConstants.YARN.RmApiDisplayName
							.getKeyDisplayName(yarnkey);
					String value = String.valueOf(((Number) clusterMetrics
							.get(yarnkey)));
					yarnSummaryMap.put(key, value);
				}
			}
			yarnKeys.clear();
			yarnKeys.add(HadoopConstants.YARN.ClusterMetrics.MEMORY_AVAILABLE);
			yarnKeys.add(HadoopConstants.YARN.ClusterMetrics.MEMORY_TOTAL);

			if (clusterMetrics != null) {
				for (String yarnkey : yarnKeys) {
					String key = HadoopConstants.YARN.RmApiDisplayName
							.getKeyDisplayName(yarnkey);
					String value = HadoopUtils.convertMbIntoGb(Long
							.parseLong(String.valueOf(((Number) clusterMetrics
									.get(yarnkey)))));
					yarnSummaryMap.put(key, value);
				}
			}

			String rmUiUrl = "http://"
					+ HadoopUtils.getResourceManagerHost(this.compConfig) + ":"
					+ HadoopUtils.getResourceManagerHttpPort(this.compConfig)
					+ "/";

			String rmUiKey = HadoopConstants.YARN.RmApiDisplayName
					.getKeyDisplayName(HadoopConstants.YARN.ClusterMetrics.RMUI);

			yarnSummaryMap.put(rmUiKey, rmUiUrl);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
		return yarnSummaryMap;
	}

	/**
	 * @return
	 * @throws AnkushException
	 */
	private JSONObject getYarnClusterMetrics() throws AnkushException {
		String rmHttpPort = HadoopUtils
				.getResourceManagerHttpPort(this.compConfig);
		String rmHttpHost = HadoopUtils.getResourceManagerHost(this.compConfig);
		String relativeUrl = Hadoop2Monitor.RM_API_CLUSTER_METRICS;

		LOG.info("Connecting to " + rmHttpHost + ":" + rmHttpPort
				+ " to fetch YARN summary.", Constant.Component.Name.HADOOP,
				rmHttpHost);

		return (JSONObject) HadoopUtils.getJsonFromRmRestApi(rmHttpHost,
				rmHttpPort, relativeUrl).get(
				HadoopConstants.YARN.CLUSTER_METRICS);
	}

	@Override
	public LinkedHashMap<String, String> getMapReduceProcessSummary()
			throws AnkushException {
		String errMsg = "Could not get ResourceManager process summary.";
		try {
//			processSummary.put(HadoopConstants.Roles.NAMENODE,
//					this.getNameNodeProcessSummary());
			String resourceManagerHost = HadoopUtils
					.getResourceManagerHost(this.compConfig);
			String resourceManagerHttpPort = HadoopUtils
					.getResourceManagerHttpPort(this.compConfig);
			return getProcessSummaryFromJmx(resourceManagerHost,
					resourceManagerHttpPort);
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
		} else if(process.equals(HadoopConstants.Roles.RESOURCEMANAGER)) {
			return this.getMapReduceProcessSummary();
		} else {
			throw new AnkushException("Could not get process summary for " + process + ".");
		}
	}
	
}
