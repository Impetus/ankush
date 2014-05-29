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
package com.impetus.ankush.agent.storm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.thrift7.TException;
import org.codehaus.jackson.map.ObjectMapper;

import backtype.storm.Config;
import backtype.storm.generated.BoltStats;
import backtype.storm.generated.ClusterSummary;
import backtype.storm.generated.ErrorInfo;
import backtype.storm.generated.ExecutorSpecificStats;
import backtype.storm.generated.ExecutorStats;
import backtype.storm.generated.ExecutorSummary;
import backtype.storm.generated.GlobalStreamId;
import backtype.storm.generated.Nimbus.Client;
import backtype.storm.generated.NotAliveException;
import backtype.storm.generated.SpoutStats;
import backtype.storm.generated.StormTopology;
import backtype.storm.generated.SupervisorSummary;
import backtype.storm.generated.TopologyInfo;
import backtype.storm.generated.TopologySummary;
import backtype.storm.utils.NimbusClient;
import backtype.storm.utils.Utils;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;
import com.impetus.ankush.agent.utils.ClassPathHack;

/**
 * StormTopologyMontior class for monitoring storm topology summary, cluster
 * summary. supervisor summary and nimbus configuration.
 * 
 * @author hokam
 * 
 */
public class StormTopologyMonitor extends Taskable {

	/** The conf. */
	private AgentConf conf = new AgentConf();

	/** The client. */
	private AgentRestClient client = new AgentRestClient();

	private AgentLogger LOG = new AgentLogger(StormTopologyMonitor.class);

	@Override
	public void start() {
		// storm monitoring url.
		final String url = conf.getURL(Constant.PROP_NAME_MONITORING_URL);
		// time period for sending topology data.
		long period = conf
				.getIntValue(Constant.PROP_NAME_STORM_TOPOLOGY_PERIOD);
		// host ip
		final String nimbusIp = conf.getProperties().getProperty(
				Constant.HOST_IP);

		try {
			// loading the jars available inside the lib directory
			String stormLib = conf.getStringValue(Constant.STORM_LIB);
			ClassPathHack.addJarFiles(stormLib);

			// loading the jars available inside the storm home directory
			String stormHome = conf.getStringValue(Constant.STORM_HOME);
			ClassPathHack.addJarFiles(stormHome);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return;
		}

		// creating scheduler thread for sending topology indormation.
		Runnable topologyThread = new Runnable() {

			@Override
			public void run() {
				try {
					// getting storm monitoring details using the nimbus IP.
					StormMonitoringConf conf = getStormMonitoringDetails(nimbusIp);
					// populating the storm monitoring conf object.
					conf.calculateMetrics();
					// converting to the map object.
					Map map = new ObjectMapper().convertValue(conf,
							HashMap.class);
					// putting the class name in @class property for object
					// creation at the ankush side.
					map.put("@class",
							"com.impetus.ankush.storm.StormMonitoringData");
					// sending the data.
					client.sendData(map, url);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
				}

			}
		};
		// scheduling the topology thread for periodic execution.
		service.scheduleAtFixedRate(topologyThread, 0, period, TimeUnit.SECONDS);
	}

	/**
	 * @throws TException
	 * @throws NotAliveException
	 */
	private static StormMonitoringConf getStormMonitoringDetails(String nimbusIp)
			throws Exception {
		// create storm monitoring conf object
		StormMonitoringConf conf = new StormMonitoringConf();
		Map clientConf = Utils.readStormConfig();
		clientConf.put(Config.NIMBUS_HOST, nimbusIp);
		clientConf.put(Config.NIMBUS_THRIFT_PORT, 6627);
		NimbusClient nc = NimbusClient.getConfiguredClient(clientConf);

		Client client = nc.getClient();
		String nimbusConf = client.getNimbusConf();
		conf.setNimbusConf(nimbusConf);

		ClusterSummary clusterSummary = client.getClusterInfo();
		int nimbusUptime = clusterSummary.get_nimbus_uptime_secs();
		int supervisorsSize = clusterSummary.get_supervisors_size();
		int topologiesSize = clusterSummary.get_topologies_size();

		// setting cluster level details...
		conf.setNimbusUpTime(nimbusUptime);
		conf.setSupervisorSize(supervisorsSize);
		conf.setTopologySize(topologiesSize);

		List<Supervisor> supervisors = new ArrayList<Supervisor>();
		for (Iterator<SupervisorSummary> iterator = clusterSummary
				.get_supervisors_iterator(); iterator.hasNext();) {
			SupervisorSummary supervisorSumm = iterator.next();

			String supervisorHost = supervisorSumm.get_host();
			int numUsedWorkers = supervisorSumm.get_num_used_workers();
			int numOfWorkers = supervisorSumm.get_num_workers();
			int supervisorUptimeSecs = supervisorSumm.get_uptime_secs();

			// setting supervisors...
			Supervisor supervisor = new Supervisor();
			supervisor.setHost(supervisorHost);
			supervisor.setNumberOfWorkers(numOfWorkers);
			supervisor.setNumberOfUsedWorkers(numUsedWorkers);
			supervisor.setUptime(supervisorUptimeSecs);

			supervisors.add(supervisor);

			// update cluster level configs...
			conf.setUsedSlots(conf.getUsedSlots() + numUsedWorkers);
			conf.setTotalSlots(conf.getTotalSlots() + numOfWorkers);
		}
		// setting list of supervisors...
		conf.setSupervisors(supervisors);
		// collection of topologies...
		List<Topology> topologies = new ArrayList<Topology>();

		for (Iterator<TopologySummary> iterator = clusterSummary
				.get_topologies_iterator(); iterator.hasNext();) {
			TopologySummary topology = iterator.next();

			String topologyId = topology.get_id();
			String topologyName = topology.get_name();
			int numOfTasks = topology.get_num_tasks();
			int numOfWorkers = topology.get_num_workers();
			String topologyStatus = topology.get_status();
			int topologyUptimeSecs = topology.get_uptime_secs();
			int numOfExecutors = topology.get_num_executors();

			// setting topology details...
			Topology topo = new Topology();
			topo.setTopolgoyId(topologyId);
			topo.setName(topologyName);
			topo.setNumberOfTasks(numOfTasks);
			topo.setNumberOfWorkers(numOfWorkers);
			topo.setStatus(topologyStatus);
			topo.setUptime(topologyUptimeSecs);
			topo.setNumberOfExecutors(numOfExecutors);

			// setting cluster level configs...
			conf.setExecutors(conf.getExecutors() + numOfExecutors);
			conf.setTasks(conf.getTasks() + numOfTasks);

			TopologyInfo topologyInfo = client.getTopologyInfo(topologyId);
			Map<String, List<ErrorInfo>> errors = topologyInfo.get_errors();
			Map<String, List> rawTopologyInfo = new HashMap<String, List>();

			// for new versions (0.8 and above)
			for (Iterator<ExecutorSummary> iterator2 = topologyInfo
					.get_executors_iterator(); iterator2.hasNext();) {
				ExecutorSummary executorSummary = iterator2.next();

				String componentId = executorSummary.get_component_id();
				if (componentId.equals("__acker")) {
					continue;
				}
				Map map = new HashMap();
				ExecutorStats exeStats = executorSummary.get_stats();

				Map<String, Map<String, Long>> emitted = exeStats.get_emitted();
				Map<String, Map<String, Long>> transferred = exeStats
						.get_transferred();
				ExecutorSpecificStats executorSpecificStats = exeStats
						.get_specific();

				map.put("emitted", emitted.get(":all-time").get("default"));

				map.put("transferred",
						transferred.get(":all-time").get("default"));

				// for bolts...
				Map g = null;
				if (executorSpecificStats.is_set_bolt()) {
					map.put("type", "bolt");

					BoltStats boltStats = executorSpecificStats.get_bolt();
					map.put("processLatency", 0);
					map.put("executeLatency", 0);
					map.put("acked", 0);
					map.put("failed", 0);
					map.put("parallelism", 0);
					g = boltStats.get_process_ms_avg().get(":all-time");
					if (g.size() > 0) {
						map.put("processLatency", g.values().iterator().next());
					}

					Map<String, Map<GlobalStreamId, Double>> executeLatency = boltStats
							.get_execute_ms_avg();
					g = executeLatency.get(":all-time");
					if (g.size() > 0) {
						map.put("executeLatency", g.values().iterator().next());
					}

					Map<String, Map<GlobalStreamId, Long>> ackedMap = boltStats
							.get_acked();
					g = ackedMap.get(":all-time");
					if (g.size() > 0) {
						map.put("acked", g.values().iterator().next());
					}

					Map<String, Map<GlobalStreamId, Long>> failedMap = boltStats
							.get_failed();
					g = failedMap.get(":all-time");
					if (g.size() > 0) {
						map.put("failed", g.values().iterator().next());
					}

					Map<String, Map<GlobalStreamId, Long>> executed = boltStats
							.get_executed();
					g = executed.get(":all-time");
					if (g.size() > 0) {
						map.put("executed", g.values().iterator().next());
					}

					StormTopology stormTopology = client
							.getTopology(topologyId);
					int parallelism = stormTopology.get_bolts()
							.get(executorSummary.get_component_id())
							.get_common().get_parallelism_hint();
					map.put("parallelism", parallelism);

				}

				// for spouts...
				if (executorSpecificStats.is_set_spout()) {
					map.put("type", "spout");

					map.put("completeLatency", 0);
					map.put("acked", 0);
					map.put("failed", 0);
					map.put("parallelism", 0);

					SpoutStats spoutStats = executorSpecificStats.get_spout();
					g = spoutStats.get_acked().get(":all-time");
					if (g.size() > 0) {
						map.put("acked", g.values().iterator().next());
					}
					g = spoutStats.get_failed().get(":all-time");
					if (g.size() > 0) {
						map.put("failed", g.values().iterator().next());
					}
					g = spoutStats.get_failed().get(":all-time");
					if (g.size() > 0) {
						map.put("completeLatency", g.values().iterator().next());
					}

					StormTopology stormTopology = client
							.getTopology(topologyId);
					int parallelism = stormTopology.get_spouts()
							.get(executorSummary.get_component_id())
							.get_common().get_parallelism_hint();
					map.put("parallelism", parallelism);
				}
				List list = rawTopologyInfo.get(componentId);
				if (list == null) {
					list = new ArrayList();
				}
				map.put("lastError", null);
				List<ErrorInfo> compErrors = errors.get(componentId);
				if (compErrors.size() > 0) {
					map.put("lastError", compErrors.get(compErrors.size() - 1)
							.get_error());
				}
				list.add(map);
				rawTopologyInfo.put(componentId, list);
			}
			// setting raw topology data...
			topo.setRawTopologyData(rawTopologyInfo);
			// setting topology in topologies list
			topologies.add(topo);
		}
		// setting topologies in config
		conf.setTopologies(topologies);
		return conf;
	}
}
