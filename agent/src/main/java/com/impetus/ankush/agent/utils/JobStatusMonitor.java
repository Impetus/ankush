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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;

/**
 * @author bgunjan
 * 
 */
public class JobStatusMonitor extends Taskable {

	private AgentConf conf;
	private AgentRestClient client;

	/**
	 * Constructor for JobStatusMonitor.
	 * 
	 * @param conf
	 *            AgentConf
	 * @param client
	 *            AgentRestClient
	 */
	public JobStatusMonitor() {
		this.client = new AgentRestClient();
		this.conf = new AgentConf();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.agent.action.Taskable#start()
	 */
	public void start() {

		try {
			String hadoopJarPath = conf.getHadoopJarsPath();
			if (hadoopJarPath == null) {
				hadoopJarPath = System.getProperty("user.home")
						+ "/.ankush/agent/jars/compdep/";
			}
			ClassPathHack.addFiles(hadoopJarPath);
			updateHadoopJobStatusInfo();
		} catch (Exception e) {
			System.err.println("Exception in JobStatusMonitor start() : "
					+ e.getMessage());
		}
	}

	private void updateHadoopJobStatusInfo() {

		final JobStatusProvider statusProvider = new JobStatusProvider();
		
		final String url = conf.getURL(Constant.PROP_NAME_MONITORING_URL);
		long period = conf
				.getIntValue(Constant.PROP_NAME_HADOOP_DATA_UPDATE_TIME);

		Runnable jobStatusThread = new Runnable() {

			@Override
			public void run() {
				List<Map<String, Object>> jobStatus = statusProvider
						.getJobStatus();
				Map<String, Object> hadoopMetrics = statusProvider
						.getJobMetrics();
				Map<String, Object> hadoopStatus = new HashMap<String, Object>();
				hadoopStatus.put("jobInfos", jobStatus);
				hadoopStatus.put("hadoopMetricsInfo", hadoopMetrics);
				hadoopStatus.put("@class", "com.impetus.ankush.hadoop.HadoopMonitoringData");
				client.sendData(hadoopStatus, url);
			}
		};

		service.scheduleAtFixedRate(jobStatusThread, 0, period,
				TimeUnit.SECONDS);
	}
}
