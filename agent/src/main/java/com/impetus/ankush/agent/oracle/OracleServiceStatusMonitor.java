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
package com.impetus.ankush.agent.oracle;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import oracle.kv.impl.topo.RepNodeId;
import oracle.kv.impl.topo.ResourceId;
import oracle.kv.impl.topo.StorageNodeId;
import oracle.kv.impl.topo.Topology;
import oracle.kv.impl.util.ConfigurableService.ServiceStatus;
import oracle.kv.util.Ping;

import org.codehaus.jackson.map.ObjectMapper;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;
import com.impetus.ankush.agent.utils.ClassPathHack;
import com.impetus.ankush.agent.utils.ServiceStatusProvider;

/**
 * The Class OracleServiceStatusMonitor.
 * 
 * @author Hokam Chauhan
 */
public class OracleServiceStatusMonitor extends Taskable {
	private AgentLogger LOGGER = new AgentLogger(
			OracleServiceStatusMonitor.class);
	/** The conf. */
	private AgentConf conf;

	/** The client. */
	private AgentRestClient client = new AgentRestClient();

	/**
	 * Constructor for ServiceStatusMonitor.
	 * 
	 */
	public OracleServiceStatusMonitor() {
		this.conf = new AgentConf();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.agent.action.Taskable#start()
	 */
	public void start() {
		LOGGER.info("ServiceStatusMonitor start");
		try {
			for (String jar : conf.getJarPath()) {
				try {
					ClassPathHack.addFile(jar);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			updateServiceStatusInfo();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Update service status info.
	 */
	private void updateServiceStatusInfo() {

		final String url = conf.getURL(Constant.PROP_NAME_SERVICE_URL_LAST);
		long period = conf
				.getIntValue(Constant.PROP_NAME_SERVICE_STATUS_UPDATE_TIME);

		final String techDataUrl = conf
				.getURL(Constant.PROP_NAME_MONITORING_URL);
		final String host = conf.getStringValue(Constant.PROP_HOSTNAME);
		final Integer port = conf.getIntValue(Constant.PROP_REGISTRY_PORT);
		final Integer snId = conf.getIntValue(Constant.PROP_SNID);
		final Boolean isAdmin = conf.getBooleanValue(Constant.PROP_ADMIN);
		final ServiceStatusProvider gangliaProvider = new ServiceStatusProvider();

		Runnable serviceStatusThread = new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, Boolean> serviceStatus = ping(host, port, snId);
					serviceStatus
							.putAll(gangliaProvider
									.getGangliaServiceStatus(conf
											.getGangliaServices()));
					LOGGER.info(" service status map " + serviceStatus);
					client.sendData(serviceStatus, url);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		};

		service.scheduleAtFixedRate(serviceStatusThread, 0, period,
				TimeUnit.SECONDS);
		if (isAdmin) {
			Runnable topologyThread = new Runnable() {

				@Override
				public void run() {
					try {
						OracleNoSQLTechnologyData data = new OracleNoSQLTechnologyData(
								host, port);
						Map map = new ObjectMapper().convertValue(data,
								HashMap.class);
						map.put("@class",
								"com.impetus.ankush.oraclenosql.OracleNoSQLTechnologyData");
						client.sendData(map, techDataUrl);
					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}

				}
			};

			service.scheduleAtFixedRate(topologyThread, 0, period,
					TimeUnit.SECONDS);
		}
	}

	/**
	 * Ping.
	 * 
	 * @param host
	 *            the host
	 * @param port
	 *            the port
	 * @param snId
	 *            the sn id
	 * @return the map
	 */
	private Map<String, Boolean> ping(String host, int port, int snId) {
		Map<String, Boolean> serviceState = new HashMap<String, Boolean>();
		try {

			// Check registry
			Registry registry = LocateRegistry.getRegistry(host, port);
			// Unused variable used to check service status.
			String[] list = registry.list();
			serviceState.put("sn" + snId, true);

			// Get Topology
			Topology topology = Ping.getTopology(host, port);

			// Service state map
			Map<ResourceId, ServiceStatus> statusMap = Ping
					.getTopologyStatus(topology);

			for (ResourceId rId : statusMap.keySet()) {
				if (rId.getType().isStorageNode()) {
					if (snId == ((StorageNodeId) rId).getStorageNodeId()) {
						serviceState.put(rId.toString(), statusMap.get(rId)
								.isAlive());
					}
				} else if (rId.getType().isRepNode()) {
					if (snId == ((RepNodeId) rId).getComponent(topology)
							.getStorageNodeId().getStorageNodeId()) {
						serviceState.put(rId.toString(), statusMap.get(rId)
								.isAlive());
					}
				}
			}
		} catch (Exception e) {
			if (!serviceState.containsKey("sn" + snId)) {
				serviceState.put("sn" + snId, false);
			}
			LOGGER.error(e.getMessage(), e);
		}

		return serviceState;
	}
}
