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

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.hadoop.deployer.configurator.Hadoop1Configurator;
import com.impetus.ankush2.hadoop.deployer.servicemanager.HdpRoleLogsDirPath;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;

/**
 * @author Akhil
 * 
 */
public class Hadoop2HdpMonitor extends HadoopMonitor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getRoleNodesMap()
	 */
	@Override
	public Map<String, Set<String>> getRoleNodesMap() throws AnkushException {
		return new Hadoop2Monitor(clusterConfig, compConfig).getRoleNodesMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getNodesSummary()
	 */
	@Override
	public LinkedHashMap<String, String> getNodesSummary()
			throws AnkushException {
		return new Hadoop2Monitor(clusterConfig, compConfig).getNodesSummary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getMapReduceMetrics()
	 */
	@Override
	public LinkedHashMap<String, String> getMapReduceMetrics()
			throws AnkushException {
		return new Hadoop2Monitor(clusterConfig, compConfig)
				.getMapReduceMetrics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getMapReduceProcessSummary
	 * ()
	 */
	@Override
	public LinkedHashMap<String, String> getMapReduceProcessSummary()
			throws AnkushException {
		return new Hadoop2Monitor(clusterConfig, compConfig)
				.getMapReduceProcessSummary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getActiveMapRedNodesCount
	 * ()
	 */
	@Override
	public String getActiveMapRedNodesCount() throws AnkushException {
		return new Hadoop2Monitor(clusterConfig, compConfig)
				.getActiveMapRedNodesCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.hadoop.monitor.HadoopMonitor#getProcessSummary(java
	 * .lang.String)
	 */
	@Override
	public LinkedHashMap<String, String> getProcessSummary(String process)
			throws AnkushException {
		return new Hadoop2Monitor(clusterConfig, compConfig)
				.getProcessSummary(process);
	}

	@Override
	public Map<String, String> getLogFilesList(String role, String host)
			throws AnkushException {
		String errMsg = "Could not fetch log files for " + role + ".";
		try {
			LogViewHandler logHandler = new LogViewHandler(host,
					this.clusterConfig.getAuthConf());

			String logsDirectory = HdpRoleLogsDirPath.getRoleLogDirPath(role);

			// get the list of all .log files
			Map<String, String> files = logHandler.getLogFilesMap(
					logsDirectory, role);
			// List<String> files = listLogDirectory(logDirectory);
			if (files == null || files.size() == 0 || files.isEmpty()) {
				throw new AnkushException(errMsg += " " + logsDirectory
						+ " does not contain logs for " + role + ".");
			}
			return files;
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig, errMsg,
					Constant.Component.Name.HADOOP, host, e);
			throw new AnkushException(errMsg);
		}
	}
}
