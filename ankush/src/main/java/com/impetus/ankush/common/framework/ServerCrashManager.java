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
package com.impetus.ankush.common.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * @author hokam
 * 
 */
public class ServerCrashManager {

	// Ankush logger.
	private AnkushLogger log = new AnkushLogger(ServerCrashManager.class);

	// cluster manager.
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	// node manager.
	private static GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	// node manager.
	private static GenericManager<Operation, Long> operationManager = AppStoreWrapper
			.getManager(
					com.impetus.ankush2.constant.Constant.Manager.OPERATION,
					Operation.class);

	/**
	 * Method to handle deploying and removing state clusters.
	 */
	public void handleDeployingRemovingClusters() {

		// Creating property map for deploying state.
		Map<String, Object> deployingStateMap = new HashMap<String, Object>();
		deployingStateMap.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
				com.impetus.ankush2.constant.Constant.Cluster.State.DEPLOYING
						.toString());
		// Creating property map for removing state.
		Map<String, Object> removingStateMap = new HashMap<String, Object>();
		removingStateMap.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
				com.impetus.ankush2.constant.Constant.Cluster.State.REMOVING
						.toString());

		// Map<String, Object> registeringStateMap = new HashMap<String,
		// Object>();
		// registeringStateMap.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
		// com.impetus.ankush2.constant.Constant.Cluster.State.REGISTERING);

		// making list of maps
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		maps.add(deployingStateMap);
		maps.add(removingStateMap);
		// maps.add(registeringStateMap);

		// list of deploying + removing state clusters.
		List<Cluster> clusters = clusterManager
				.getAllByDisjunctionveNormalQuery(maps);

		// iterating over the all deploying/removing state clusters.
		for (Cluster cluster : clusters) {
			// getting clusterable object.
			try {
				// setting state as crashed.
				cluster.setState(Constant.Cluster.State.SERVER_CRASHED
						.toString());
				// getting cluster conf.
				ClusterConfig conf = cluster.getClusterConfig();
				// setting id of cluster inside conf.
				conf.setClusterId(cluster.getId());
				// setting state as error.
				conf.setState(com.impetus.ankush2.constant.Constant.Cluster.State.ERROR);
				// adding error message.
				conf.addError("Deploy", "Server crashed unexpectedly.");
				// saving cluster conf.
				cluster.setClusterConf(conf);
				// saving cluster.
				clusterManager.save(cluster);
			} catch (Exception e) {
				log.error(e.getMessage());
				try {
					// setting server crashed as state.
					cluster.setState(Constant.Cluster.State.SERVER_CRASHED
							.toString());
					// saving in db.
					clusterManager.save(cluster);
				} catch (Exception subExe) {
					log.error(subExe.getMessage());
				}
			}

		}
	}

	/**
	 * Method to handle deploying, removing and adding state nodes.
	 */
	public void handleDeployingRemovingNodes() {

		// Creating property map for deploying state.
		Map<String, Object> deployingStateMap = new HashMap<String, Object>();
		deployingStateMap.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
				com.impetus.ankush2.constant.Constant.Node.State.DEPLOYING
						.toString());
		// Creating property map for removing state.
		Map<String, Object> removingStateMap = new HashMap<String, Object>();
		removingStateMap.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
				com.impetus.ankush2.constant.Constant.Node.State.REMOVING
						.toString());

		Map<String, Object> addingStateMap = new HashMap<String, Object>();
		addingStateMap.put(com.impetus.ankush2.constant.Constant.Keys.STATE,
				com.impetus.ankush2.constant.Constant.Node.State.ADDING
						.toString());

		// making list of maps
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		maps.add(deployingStateMap);
		maps.add(removingStateMap);
		maps.add(addingStateMap);

		try {
			// list of deploying + removing state nodes.
			List<Node> nodes = nodeManager
					.getAllByDisjunctionveNormalQuery(maps);
			// setting node state as Server_Crashed
			for (Node node : nodes) {
				node.setState(Constant.Node.State.SERVER_CRASHED.toString());
				nodeManager.save(node);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Function that changed the state of all inprogress operations to
	 * ServerCrashed
	 */
	public void handleInProgressOperations() {
		try {
			// Creating property map for deploying state.
			Map<String, Object> inProgressStatusMap = new HashMap<String, Object>();
			inProgressStatusMap
					.put(com.impetus.ankush2.constant.Constant.Keys.STATUS,
							com.impetus.ankush2.constant.Constant.Operation.Status.INPROGRESS
									.toString());

			// making list of maps
			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
			maps.add(inProgressStatusMap);

			List<Operation> operations = operationManager
					.getAllByPropertyValue(inProgressStatusMap);

			for (Operation operation : operations) {
				try {
					operation
							.setStatus(com.impetus.ankush2.constant.Constant.Operation.Status.ERROR
									.toString());
					operation.setCompletedAt(new Date());
					operation.getData().put("Error",
							"Server crashed unexpectedly.");
					operationManager.save(operation);
				} catch (Exception e) {
					log.error(e.getMessage());
					try {
						operation
								.setStatus(com.impetus.ankush2.constant.Constant.Operation.Status.ERROR
										.toString());
						operationManager.save(operation);
					} catch (Exception subExe) {
						log.error(subExe.getMessage());
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}
}
