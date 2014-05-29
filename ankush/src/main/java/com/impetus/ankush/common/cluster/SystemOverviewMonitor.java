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
package com.impetus.ankush.common.cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;

/**
 * It is used to provide the system overview of ankush.
 * 
 * @author hokam
 * 
 */
public class SystemOverviewMonitor {

	/** Generic cluster master. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/**
	 * Gets the systerm overview.
	 * 
	 * @return the systerm overview
	 */
	public List<TileInfo> getSystermOverview() {
		// list of tiles.
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		// add all cluster tiles.
		tiles.addAll(getClusterTypeTiles());

		return tiles;
	}

	/**
	 * Gets the cluster type tiles.
	 * 
	 * @return the cluster type tiles
	 */
	private List<TileInfo> getClusterTypeTiles() {
		List<TileInfo> tileInfos = new ArrayList<TileInfo>();

		// iterating over the technology.
		for (String technology : Arrays.asList(Constant.Technology.HADOOP,
				Constant.Technology.ORACLE_NOSQL, Constant.Technology.STORM,
				Constant.Technology.CASSANDRA, Constant.Technology.KAFKA,
				Constant.Technology.ELASTICSEARCH)) {
			// iterating over the environment.
			for (String environment : Arrays
					.asList(Constant.Cluster.Environment.IN_PREMISE)) {

				for (String state : Arrays.asList(
						Constant.Cluster.State.DEPLOYING,
						Constant.Cluster.State.ERROR,
						Constant.Cluster.State.REMOVING)) {

					// line 2 message.
					String message = technology + "/" + environment;
					if (technology.equals(Constant.Technology.ORACLE_NOSQL)) {
						message = "Oracle Store";
					}

					if (!state.equals(Constant.Cluster.State.ERROR)) {
						message = message + " " + state;
					}

					Map<String, Object> propsMap = new HashMap<String, Object>();
					propsMap.put("technology", technology);
					propsMap.put("environment", environment);
					propsMap.put("state", state);

					// Method to get given technology type cluster count.
					Integer count = clusterManager
							.getAllByPropertyValueCount(propsMap);

					TileInfo tileInfo = null;
					if (count != 0) {
						// Create tile info object.
						tileInfo = new TileInfo();
						tileInfo.setLine1(count.toString());
						tileInfo.setLine2(message);
						tileInfo.setStatus(Constant.Tile.Status.NORMAL);

						if (state.equals(Constant.Cluster.State.ERROR)) {
							tileInfo.setLine3("In " + state);
							tileInfo.setStatus(Constant.Tile.Status.CRITICAL);
						}

						tileInfo.setUrl(null);
						tileInfo.setData(null);
						tileInfos.add(tileInfo);
					}
				}
				// creating disjunc map.
				List<Map<String, Object>> disMap = new ArrayList<Map<String, Object>>();
				// iterating over the running cluster states.
				for (String state : Arrays.asList(
						Constant.Cluster.State.DEPLOYED,
						Constant.Cluster.State.ADDING_NODES,
						Constant.Cluster.State.REMOVING_NODES,
						Constant.Cluster.State.REBALANCE,
						Constant.Cluster.State.REDISTRIBUTE,
						Constant.Cluster.State.MIGRATE_NODE,
						Constant.Cluster.State.CHANGE_REP_FACTOR)) {

					// conjunction map
					Map<String, Object> propsMap = new HashMap<String, Object>();
					propsMap.put("technology", technology);
					propsMap.put("environment", environment);
					propsMap.put("state", state);
					disMap.add(propsMap);
				}

				// Method to get given technology type cluster count.
				Integer count = clusterManager
						.getAllByDisjunctionveNormalQueryCount(disMap);

				// line2 message.
				String message = technology + "/" + environment + " running";
				if (technology.equals(Constant.Technology.ORACLE_NOSQL)) {
					message = "Oracle Store running";
				}
				TileInfo tileInfo = null;
				if (count != 0) {
					// Create tile info object.
					tileInfo = new TileInfo();
					tileInfo.setLine1(count.toString());
					tileInfo.setLine2(message);
					tileInfo.setStatus(Constant.Status.NORMAL);
					tileInfo.setUrl(null);
					tileInfo.setData(null);
					tileInfos.add(tileInfo);
				}
			}
		}
		TileManager manager = new TileManager();

		// add Service/CPU/Memory/Agent Down tiles.
		tileInfos.addAll((manager.getSystemEventTiles()));

		return tileInfos;
	}
}
