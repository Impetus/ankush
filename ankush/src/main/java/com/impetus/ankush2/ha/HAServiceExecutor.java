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
package com.impetus.ankush2.ha;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.HAService;
import com.impetus.ankush2.db.DBClusterManager;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.utils.ObjectFactory;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

public class HAServiceExecutor implements Runnable {
	private Long clusterId;
	private String node;
	private String component;
	private String service;
	private int tryCount;

	private static final String SEPERATOR = "_";

	private static AnkushLogger logger = new AnkushLogger(
			HAServiceExecutor.class);
	private static Map<String, ScheduledFuture> futureMap = new HashMap<String, ScheduledFuture>();

	private HAServiceExecutor(HAService haService, String host) {
		clusterId = haService.getClusterId();
		node = host;
		component = haService.getComponent();
		service = haService.getService();
		tryCount = haService.getTryCount();

		futureMap.put(getId(haService, host), AppStoreWrapper.getExecutor()
				.scheduleWithFixedDelay(this, haService.getDelayInterval()));

		logger.info(getId(haService, host) + " has been added.");

	}

	public void run() {

		try {
			ClusterConfig clusterConfig = new DBClusterManager().getCluster(
					clusterId).getClusterConfig();

			// Create servicebale object
			Serviceable serviceable = ObjectFactory.getServiceObject(component);

			// Connect to node
			AnkushUtils.connectNodesString(clusterConfig, Arrays.asList(node));

			// Start service
			serviceable.startServices(clusterConfig, node, new HashSet<String>(
					Arrays.asList(service)));

			// Disconnect node
			SSHExec connection = clusterConfig.getNodes().get(node)
					.getConnection();
			if (connection != null) {
				connection.disconnect();
				clusterConfig.getNodes().get(node).setConnection(null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// Reduce try count
		tryCount--;
		logger.info(getId(clusterId, node, component, service) + " " + tryCount
				+ " try remains.");
		// Stop scheduler if trial count reach to zero
		if (tryCount == 0) {
			removeHAService(clusterId, node, component, service);
		}
	}

	public static String getId(HAService haService, String host) {
		return getId(haService.getClusterId(), host, haService.getComponent(),
				haService.getService());
	}

	public static String getId(Long clusterId, String host, String component,
			String service) {
		return new StringBuilder().append(clusterId).append(SEPERATOR)
				.append(host).append(SEPERATOR).append(component)
				.append(SEPERATOR).append(service).toString();
	}

	public static void addHAService(HAService haService, String host) {
		synchronized (futureMap) {
			String id = getId(haService, host);
			if (!futureMap.containsKey(id)) {
				new HAServiceExecutor(haService, host);
			}
		}
	}

	public static void removeHAService(Long clusterId, String host,
			String component, String service) {
		synchronized (futureMap) {
			String id = getId(clusterId, host, component, service);
			if (futureMap.containsKey(id)) {
				futureMap.remove(id).cancel(true);
				logger.info(id + " has been removed.");
			}
		}
	}
}
