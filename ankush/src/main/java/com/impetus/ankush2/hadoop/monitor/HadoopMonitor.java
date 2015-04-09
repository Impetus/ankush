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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.hadoop.config.ComponentConfigContext;
import com.impetus.ankush2.hadoop.deployer.configurator.Hadoop1Configurator;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopConstants.JmxBeanKeys.DfsData.Nodes;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.hadoop.utils.ProcessMemoryType;
import com.impetus.ankush2.hadoop.utils.ProcessMemoryUsageType;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopMonitor.
 * 
 * @author Akhil
 */
public abstract class HadoopMonitor extends ComponentConfigContext {

	public static final String STRING_EMPTY_VALUE = "--";

	/** The Constant JMX_BEAN_NAME_JAVA_MEMORY. */
	public static final String JMX_BEAN_NAME_JAVA_MEMORY = "java.lang:type=Memory";

	/** The Constant JMX_BEAN_NAME_NAMENODEINFO. */
	public static final String JMX_BEAN_NAME_NAMENODE_INFO = "Hadoop:service=NameNode,name=NameNodeInfo";

	/** The Constant JMX_BEAN_NAME_JAVA_RUNTIME. */
	public static final String JMX_BEAN_NAME_JAVA_RUNTIME = "java.lang:type=Runtime";

	/** The Constant JMX_BEAN_NAME_JAVA_THREADING. */
	public static final String JMX_BEAN_NAME_JAVA_THREADING = "java.lang:type=Threading";

	/**
	 * Instantiates a new hadoop configurator.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param hadoopConfig
	 *            the hadoop config
	 * @param classObj
	 *            the class obj
	 */
	public HadoopMonitor(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig, Class classObj) {
		super(clusterConfig, hadoopConfig, classObj);
	}

	/**
	 * Instantiates a new hadoop monitor.
	 */
	public HadoopMonitor() {
		super();
	}

	/**
	 * Gets the role nodes map.
	 * 
	 * @return the role nodes map
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public abstract Map<String, Set<String>> getRoleNodesMap()
			throws AnkushException;

	public abstract LinkedHashMap<String, String> getNodesSummary()
			throws AnkushException;

	public abstract LinkedHashMap<String, String> getMapReduceMetrics()
			throws AnkushException;

	public abstract LinkedHashMap<String, String> getMapReduceProcessSummary()
			throws AnkushException;

	public LinkedHashMap<String, String> getHdfsMetrics()
			throws AnkushException {
		String errMsg = "Could not get HDFS summary.";
		LinkedHashMap<String, String> hdfsSummary = new LinkedHashMap<String, String>();

		try {
			String nameNodeHost = HadoopUtils.getNameNodeHost(this.compConfig);
			String nameNodeHttpPort = HadoopUtils
					.getNameNodeHttpPort(compConfig);
			String beanName = HadoopMonitor.JMX_BEAN_NAME_NAMENODE_INFO;
			LOG.info("Connecting to " + nameNodeHost + ":" + nameNodeHttpPort
					+ " to get HDFS summary.", Constant.Component.Name.HADOOP,
					nameNodeHost);

			Map<String, Object> beanObject = HadoopUtils
					.getJmxBeanUsingCallable(nameNodeHost, nameNodeHttpPort,
							beanName);
			errMsg = "Could not get HDFS summary from host-" + nameNodeHost
					+ ", port-" + nameNodeHttpPort + ".";
			if (beanObject != null) {
				hdfsSummary = HadoopDFSManager.getDfsUsageData(beanObject);
				String nameNodeUiUrl = "http://" + nameNodeHost + ":"
						+ nameNodeHttpPort + "/";
				hdfsSummary
						.put(HadoopConstants.Hadoop.Keys.NameNodeJmxKeyDisplayName
								.getKeyDisplayName(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.NAMENODEUI),
								nameNodeUiUrl);
			}
			return hdfsSummary;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	public LinkedHashMap<String, String> getProcessSummaryFromJmx(String host,
			String port) throws AnkushException {
		String errMsg = "Could not get process summary for host- " + host
				+ ", port- " + port + ".";
		LinkedHashMap<String, String> processSummary = new LinkedHashMap<String, String>();
		LOG.info("Connecting to " + host + ":" + port
				+ " to get process summary.", Constant.Component.Name.HADOOP,
				host);
		try {
			Map<String, Object> beanObject = HadoopUtils
					.getJmxBeanUsingCallable(host, port,
							HadoopMonitor.JMX_BEAN_NAME_JAVA_MEMORY);
			if (beanObject != null) {
				processSummary
						.put(HadoopConstants.Hadoop.Keys.DISPLAY_NAME_HEAP_MEMORY_USED,
								HadoopUtils.getMemoryInfoFromJmxBean(
										beanObject, ProcessMemoryType.HEAP,
										ProcessMemoryUsageType.USED));
				processSummary
						.put(HadoopConstants.Hadoop.Keys.DISPLAY_NAME_HEAP_MEMORY_COMMITTED,
								HadoopUtils.getMemoryInfoFromJmxBean(
										beanObject, ProcessMemoryType.HEAP,
										ProcessMemoryUsageType.COMMITTED));
				processSummary
						.put(HadoopConstants.Hadoop.Keys.DISPLAY_NAME_NON_HEAP_MEMORY_USED,
								HadoopUtils.getMemoryInfoFromJmxBean(
										beanObject, ProcessMemoryType.NONHEAP,
										ProcessMemoryUsageType.USED));
				processSummary
						.put(HadoopConstants.Hadoop.Keys.DISPLAY_NAME_NON_HEAP_MEMORY_COMMITTED,
								HadoopUtils.getMemoryInfoFromJmxBean(
										beanObject, ProcessMemoryType.NONHEAP,
										ProcessMemoryUsageType.COMMITTED));
			} else {
				throw new AnkushException(errMsg);
			}
			//
			// processSummary.put(
			// HadoopConstants.Hadoop.Keys.DISPLAY_NAME_THREAD_COUNT,
			// HadoopUtils.getProcessThreadCount(host, port));

			processSummary.put(
					HadoopConstants.Hadoop.Keys.DISPLAY_NAME_PROCESS_STARTED,
					HadoopUtils.getProcessStartTime(host, port));

			return processSummary;

		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	public LinkedHashMap<String, String> getNameNodeProcessSummary()
			throws AnkushException {
		String errMsg = "Could not get NameNode process summary.";
		try {
			String nameNodeHost = HadoopUtils.getNameNodeHost(this.compConfig);
			String nameNodeHttpPort = HadoopUtils
					.getNameNodeHttpPort(compConfig);
			return getProcessSummaryFromJmx(nameNodeHost, nameNodeHttpPort);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, e);
			throw new AnkushException(errMsg);
		}
	}

	/**
	 * Gets the log files list.
	 * 
	 * @param role
	 *            the role
	 * @param host
	 *            the host
	 * @return the log files list
	 * @throws AnkushException
	 *             the ankush exception
	 */
	public Map<String, String> getLogFilesList(String role, String host)
			throws AnkushException {
		String errMsg = "Could not fetch log files for " + role + ".";
		try {
			LogViewHandler logHandler = new LogViewHandler(host,
					this.clusterConfig.getAuthConf());

			String logsDirectory = this.compConfig.getHomeDir()
					+ Hadoop1Configurator.RELPATH_LOGS_DIR;

			// get the list of all .log files
			Map<String, String> files = logHandler.getLogFilesMap(
					logsDirectory, role);
			// List<String> files = listLogDirectory(logDirectory);
			if (files == null || files.size() == 0 || files.isEmpty()) {
				throw new AnkushException(errMsg += " " + logsDirectory
						+ " does not contain logs for " + role + ".");
			}

			// get the list of all log files for a particular role
			// List<String> logFilesList = logHandler.getLogFilesList(
			// logsDirectory, role);
			//
			// if (logFilesList.isEmpty()) {
			// throw new AnkushException(errMsg += " " + logsDirectory
			// + " does not contain logs for " + role + ".");
			// }
			//
			// Map<String, String> logFilesMap = new HashMap<String, String>();
			// for (String logFile : logFilesList) {
			// logFilesMap.put(logFile, logsDirectory + logFile);
			// }
			return files;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, host, e);
			throw new AnkushException(errMsg);
		}
	}

	protected String getLiveDataNodesCount() {
		try {
			String nameNodeHost = HadoopUtils.getNameNodeHost(this.compConfig);
			String nameNodeHttpPort = HadoopUtils
					.getNameNodeHttpPort(compConfig);
			String beanName = HadoopMonitor.JMX_BEAN_NAME_NAMENODE_INFO;
			LOG.info("Connecting to " + nameNodeHost + ":" + nameNodeHttpPort
					+ " to fetch Live DataNode count.",
					Constant.Component.Name.HADOOP, nameNodeHost);

			Map<String, Object> beanObject = HadoopUtils
					.getJmxBeanUsingCallable(nameNodeHost, nameNodeHttpPort,
							beanName);
			String errMsg = "Could not get Live DataNode count for Host-"
					+ nameNodeHost + ", Port-" + nameNodeHttpPort + ".";
			if (beanObject != null) {
				String strActiveNodesInfo = String.valueOf(beanObject
						.get(Nodes.LIVE));
				JSONObject jsonArrayActiveNodeInfo = JsonMapperUtil
						.objectFromString(strActiveNodesInfo, JSONObject.class);
				if (jsonArrayActiveNodeInfo != null) {
					return String.valueOf(jsonArrayActiveNodeInfo.size());
				} else {
					throw new AnkushException(errMsg);
				}
			} else {
				throw new AnkushException(errMsg);
			}
		} catch (AnkushException e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, e.getMessage(),
					Constant.Component.Name.HADOOP, e);
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not fetch Live DataNode count.",
					Constant.Component.Name.HADOOP, e);
		}
		return HadoopMonitor.STRING_EMPTY_VALUE;
	}

	public abstract String getActiveMapRedNodesCount() throws AnkushException;

	public abstract LinkedHashMap<String, String> getProcessSummary(String process)
			throws AnkushException;
}
