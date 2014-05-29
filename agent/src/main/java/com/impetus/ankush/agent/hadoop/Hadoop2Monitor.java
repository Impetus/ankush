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
package com.impetus.ankush.agent.hadoop;

/**
 * 
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;
import com.impetus.ankush.agent.utils.AgentUtils;

/**
 * @author hokam
 * 
 */
public class Hadoop2Monitor extends Taskable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(Hadoop2Monitor.class);
	// agent conf.
	private AgentConf conf;
	// rest client.
	private AgentRestClient restClient;

	/**
	 * constructor.
	 */
	public Hadoop2Monitor() {
		conf = new AgentConf();
		restClient = new AgentRestClient();
	}

	@Override
	public void start() {
		LOGGER.info("Hadoop2Monitor Start");
		// namenode ip.
		final String nameNodeIp = conf
				.getStringValue(Constant.PROP_NAMENODE_HOST);

		// monitoring ankush url.
		final String url = conf.getURL(Constant.PROP_NAME_MONITORING_URL);

		// job status update time.
		long period = conf
				.getIntValue(Constant.PROP_NAME_HADOOP_DATA_UPDATE_TIME);

		final String nameNodeHttpPort = conf
				.getStringValue(Constant.PROP_NAMENODE_HTTP_PORT);

		Runnable monitorThread = new Runnable() {

			@Override
			public void run() {
				try {
					LOGGER.info("Monitor Thread");
					// getting json object.
					JSONObject objectData = getHadoopMonitoringData(nameNodeIp,
							nameNodeHttpPort);
					// sending the data.
					restClient.sendData(objectData, url);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		};

		service.scheduleAtFixedRate(monitorThread, 0, period, TimeUnit.SECONDS);
	}

	public JSONObject getHadoopMonitoringData(String namenodeIp,
			String dfsHealthUIPort) throws Exception {
		// blank json object.
		LOGGER.info("Hadoop Monitoring Data");
		JSONObject data = new JSONObject();
		// putting @class property.
		data.put("@class", "com.impetus.ankush.hadoop.Hadoop2MonitoringData");

		// Application Data is not saved in the DB, it is directly fetched by
		// the Ankush Server through Hadoop Yarn REST API's.

		// putting the hadoop metrics information.
		// data.put("clusterMetrics",
		// getClusterMetrics(resourceManagerIp, restPort));
		// // putting jobs information.
		// data.put("jobs", getJobs(resourceManagerIp, restPort));
		// // putting nodes information.
		// data.put("nodes", getNodes(resourceManagerIp, restPort));

		data.put("dfsReport", getDfsReport(namenodeIp, dfsHealthUIPort));
		return data;
	}

	private Object getDfsReport(String namenodeIp, String dfsHealthUIPort)
			throws Exception {
		// // parsing string data in json object.
		// parsing string data in json object.
		JSONObject jsonObject;
		JSONObject dfsReport = new JSONObject();
		try {
			AgentRestClient restClient = new AgentRestClient();
			// rest url for cluster metrics.
			String url = "http://" + namenodeIp + ":" + dfsHealthUIPort
					+ "/jmx?qry=Hadoop:service=NameNode,name=NameNodeInfo";
			// getting the metrics data from rest.
			String metricsData = restClient.getRequest(url);
			jsonObject = (JSONObject) new JSONParser().parse(metricsData);
			// getting metrics from it.
			List<JSONObject> beans = (List<JSONObject>) jsonObject.get("beans");
			JSONObject dfsinfo = beans.get(0);
			JSONObject liveNodes = (JSONObject) new JSONParser().parse(dfsinfo
					.get("LiveNodes").toString());
			JSONObject decomNodes = (JSONObject) new JSONParser().parse(dfsinfo
					.get("DecomNodes").toString());
			JSONObject deadNodes = (JSONObject) new JSONParser().parse(dfsinfo
					.get("DeadNodes").toString());

			long dfsTotal = AgentUtils.getLongValue(dfsinfo.get("Total")
					.toString());
			long dfsUsed = AgentUtils.getLongValue(dfsinfo.get("Used")
					.toString());
			long nonDFSUsed = AgentUtils.getLongValue(dfsinfo.get(
					"NonDfsUsedSpace").toString());
			String percentUsed = dfsinfo.get("PercentUsed").toString();
			String percentRemaining = dfsinfo.get("PercentRemaining")
					.toString();
			long dfsRemaining = AgentUtils.getLongValue(dfsinfo.get("Free")
					.toString());
			int numberOfMissingBlocks = AgentUtils.getIntValue(dfsinfo.get(
					"NumberOfMissingBlocks").toString());

			dfsReport.put("version", dfsinfo.get("Version"));
			dfsReport.put("inSafemode", dfsinfo.get("Safemode").equals("yes"));
			dfsReport.put("configuredCapacity",
					AgentUtils.convertBytes(dfsTotal));
			dfsReport.put("dfsUsed", AgentUtils.convertBytes(dfsUsed));
			dfsReport
					.put("dfsRemaining", AgentUtils.convertBytes(dfsRemaining));
			dfsReport.put("nonDFSUsed", AgentUtils.convertBytes(nonDFSUsed));
			dfsReport.put("dfsUsedPercent", percentUsed);
			dfsReport.put("dfsRemainingPercent", percentRemaining);
			dfsReport.put("liveNodesCount", liveNodes.size());
			dfsReport.put("deadNodesCount", deadNodes.size());
			dfsReport.put("decommissionNodes", decomNodes.size());
			dfsReport.put("missingBlockCount", numberOfMissingBlocks);

			dfsReport.put("liveNodeInfo", getNodes(liveNodes));
			dfsReport.put("deadNodeInfo", getNodes(deadNodes));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return dfsReport;
	}

	private static List<JSONObject> getNodes(JSONObject nodes) {
		List<JSONObject> updatedNodes = new ArrayList<JSONObject>();
		for (Object host : nodes.keySet()) {
			JSONObject node = (JSONObject) nodes.get(host);
			long dfsTotal = AgentUtils.getLongValue(node.get("capacity")
					.toString());
			long dfsUsed = AgentUtils.getLongValue(node.get("usedSpace")
					.toString());
			long nonDFSUsed = AgentUtils.getLongValue(node.get(
					"nonDfsUsedSpace").toString());
			String adminState = node.get("adminState").toString();
			long dfsRemaining = dfsTotal - dfsUsed - nonDFSUsed;
			Double remainingPercent = ((double) dfsRemaining / dfsTotal) * 100;
			Double usedPercent = ((double) dfsUsed / dfsTotal) * 100;
			int lastContact = AgentUtils.getIntValue(node.get("lastContact")
					.toString());
			JSONObject updatedNode = new JSONObject();
			updatedNode.put("configuredCapacity",
					AgentUtils.convertBytes(dfsTotal));
			updatedNode.put("dfsUsed", AgentUtils.convertBytes(dfsUsed));
			updatedNode.put("nonDfsUsed", AgentUtils.convertBytes(nonDFSUsed));
			updatedNode.put("dfsRemaining",
					AgentUtils.convertBytes(dfsRemaining));
			updatedNode.put("usedPercent", Double.valueOf(usedPercent));
			updatedNode.put("remainingPercent",
					Double.valueOf(remainingPercent));
			updatedNode.put("host", host);
			updatedNode.put("adminState", adminState);
			updatedNode.put("lastContact", lastContact);
			updatedNodes.add(updatedNode);
		}
		return updatedNodes;
	}

	private List<JSONObject> getJobs(String namenodeIp, String restPort)
			throws IOException, ParseException {
		// rest url for apps information.
		String url = "http://" + namenodeIp + ":" + restPort
				+ "/ws/v1/cluster/apps";
		// getting the jobs data from rest.
		String appsData = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject json = (JSONObject) new JSONParser().parse(appsData);
		// getting apps from it.
		JSONObject apps = (JSONObject) json.get("apps");

		if (apps == null || apps.isEmpty()) {
			return new ArrayList<JSONObject>();
		}
		// getting list of apps from it.
		List<JSONObject> jobs = (List<JSONObject>) apps.get("app");

		// iterating over the job list.
		for (JSONObject obj : jobs) {
			HashMap job = new HashMap();

			// renaming the properties for compatibility with the old classes.
			obj.put("jobId", obj.remove("id"));
			obj.put("jobState", obj.remove("state"));
			obj.put("jobName", obj.remove("name"));
			obj.put("userName", obj.remove("user"));
			obj.put("trackingURL", obj.remove("trackingUrl"));
			obj.put("jobStartTime", obj.remove("startedTime"));
		}
		// returning jobs.
		return jobs;
	}

	private JSONObject getClusterMetrics(String namenodeIp, String restPort)
			throws IOException, ParseException {
		LOGGER.info("Cluster Metrics");
		// rest url for cluster metrics.
		String url = "http://" + namenodeIp + ":" + restPort
				+ "/ws/v1/cluster/metrics";
		// getting the metrics data from rest.
		String metricsData = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject jsonObject = (JSONObject) new JSONParser()
				.parse(metricsData);
		// getting metrics from it.
		JSONObject metrics = (JSONObject) jsonObject.get("clusterMetrics");
		// returning metrics.
		return metrics;
	}

	private List<JSONObject> getNodes(String namenodeIp, String restPort)
			throws IOException, ParseException {
		LOGGER.info("Cluster Metrics");
		// rest url for cluster metrics.
		String url = "http://" + namenodeIp + ":" + restPort
				+ "/ws/v1/cluster/nodes";
		// getting the metrics data from rest.
		String metricsData = restClient.getRequest(url);
		// parsing string data in json object.
		JSONObject jsonObject = (JSONObject) new JSONParser()
				.parse(metricsData);
		// getting metrics from it.
		JSONObject nodesHash = (JSONObject) jsonObject.get("nodes");
		// node list
		List<JSONObject> nodes = (List<JSONObject>) nodesHash.get("node");
		// returning metrics.
		return nodes;
	}
}
