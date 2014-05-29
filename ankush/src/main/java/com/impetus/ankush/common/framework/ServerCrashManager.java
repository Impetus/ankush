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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;

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

	/**
	 * Method to handle deploying and removing state clusters.
	 */
	public void handleDeployingRemovingClusters() {

		// Creating property map for deploying state.
		Map<String, Object> deployingStateMap = new HashMap<String, Object>();
		deployingStateMap.put(Constant.Keys.STATE,
				Constant.Cluster.State.DEPLOYING);
		// Creating property map for removing state.
		Map<String, Object> removingStateMap = new HashMap<String, Object>();
		removingStateMap.put(Constant.Keys.STATE,
				Constant.Cluster.State.REMOVING);

		// making list of maps
		List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		maps.add(deployingStateMap);
		maps.add(removingStateMap);

		// list of deploying + removing state clusters.
		List<Cluster> clusters = clusterManager
				.getAllByDisjunctionveNormalQuery(maps);

		// iterating over the all deploying/removing state clusters.
		for (Cluster cluster : clusters) {
			// getting clusterable object.
			try {
				// setting state as crashed.
				cluster.setState(Constant.Cluster.State.SERVER_CRASHED);
				// getting cluster conf.
				ClusterConf conf = cluster.getClusterConf();
				// setting id of cluster inside conf.
				conf.setClusterId(cluster.getId());
				// setting state as error.
				conf.setState(Constant.Cluster.State.SERVER_CRASHED);
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
					cluster.setState(Constant.Cluster.State.SERVER_CRASHED);
					// saving in db.
					clusterManager.save(cluster);
				} catch (Exception subExe) {
					log.error(subExe.getMessage());
				}
			}

		}
	}
}
