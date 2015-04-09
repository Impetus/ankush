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
package com.impetus.ankush.common.tiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event;
import com.impetus.ankush.common.domain.Tile;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush2.db.DBEventManager;

/**
 * The Class TileManager.
 * 
 * @author nikunj
 */
public class TileManager {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(TileManager.class);

	/** Generic Tile master. */
	private static GenericManager<Tile, Long> tileManager = AppStoreWrapper
			.getManager(Constant.Manager.TILE, Tile.class);

	/** Generic Tile master. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/**
	 * Save tile.
	 * 
	 * @param tile
	 *            the tile
	 * @return the tile
	 */
	public Tile saveTile(Tile tile) {
		if (tile != null) {
			try {
				return tileManager.save(tile);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Save tile.
	 * 
	 * @param tileInfo
	 *            TileInfo
	 * @param clusterId
	 *            Cluster Id
	 * @param key
	 *            Secondary Key
	 * @param data
	 *            Data
	 * @return the tile
	 */
	public Tile saveTile(TileInfo tileInfo, Long clusterId, String key,
			Serializable data) {
		if (tileInfo != null) {
			try {
				Tile tile = new Tile();
				tile.setClusterId(clusterId);
				tile.setMinorKey(key);
				tile.setTileInfoObj(tileInfo);
				tile.setDataObj(data);
				return saveTile(tile);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Save Generic tile.
	 * 
	 * @param tileInfo
	 *            the tile info
	 * @return the tile
	 */
	public Tile saveTile(TileInfo tileInfo) {
		return saveTile(tileInfo, null, null, null);
	}

	/**
	 * Save Cluster tile.
	 * 
	 * @param tileInfo
	 *            the tile info
	 * @param clusterId
	 *            the cluster id
	 * @return the tile
	 */
	public Tile saveTile(TileInfo tileInfo, Long clusterId) {
		return saveTile(tileInfo, clusterId, null, null);
	}

	/**
	 * Save cluster tile with data.
	 * 
	 * @param tileInfo
	 *            the tile info
	 * @param clusterId
	 *            the cluster id
	 * @param data
	 *            the data
	 * @return the tile
	 */
	public Tile saveTile(TileInfo tileInfo, Long clusterId, Serializable data) {
		return saveTile(tileInfo, clusterId, null, data);
	}

	/**
	 * Remove tile.
	 * 
	 * @param tileId
	 *            the tile id
	 */
	public void removeTile(Long tileId) {
		try {
			tileManager.remove(tileId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Get single tile.
	 * 
	 * @param tileId
	 *            the tile id
	 * @return the tile
	 */
	public Tile getTile(Long tileId) {
		Tile tile = null;

		try {
			tile = tileManager.get(tileId);
			if (tile.getDestroy()) {
				removeTile(tile.getId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return tile;
	}

	/**
	 * Read cluster tiles.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the list
	 */
	public List<Tile> readClusterTile(Long clusterId) {
		List<Tile> tiles = null;

		try {
			tiles = tileManager.getAllByPropertyValue(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
					clusterId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		for (Tile tile : tiles) {
			if (tile.getDestroy()) {
				removeTile(tile.getId());
			}
		}

		if (tiles.isEmpty()) {
			return null;
		}
		return tiles;
	}

	/**
	 * Read cluster tiles.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param key
	 *            the key
	 * @return the list
	 */
	public List<Tile> readClusterTile(Long clusterId, String key) {
		Map<String, Object> propertyValueMap = new HashMap<String, Object>();
		propertyValueMap.put(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID, clusterId);
		propertyValueMap.put(com.impetus.ankush2.constant.Constant.Keys.KEY, key);

		List<Tile> tiles = null;

		try {
			tiles = tileManager.getAllByPropertyValue(propertyValueMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		for (Tile tile : tiles) {
			if (tile.getDestroy()) {
				removeTile(tile.getId());
			}
		}

		if (tiles.isEmpty()) {
			return null;
		}
		return tiles;
	}

	/**
	 * Method to get the cluster evnets.
	 * 
	 * @return the map
	 * @author hokam
	 */
	public List<TileInfo> getClusterEventTypeTiles(Cluster dbCluster,
			Event.Type type) {
		// event manager object.
		DBEventManager eventManager = new DBEventManager();
		// Getting events by cluster id.
		List<Event> events = eventManager.getEvents(dbCluster.getId(), null,
				type, null, null, null);

		// Grouped Event Map
		Map<String, Map> groupedEventMap = new HashMap<String, Map>();

		// iterating over the events.
		for (Event event : events) {
			// group+severity key
			String groupSeverity = event.getCategory() + "-"
					+ event.getSeverity();
			// old grouping type + severity map
			Map oldTypeMap = (Map) groupedEventMap.get(groupSeverity);
			// Getting new grouping event type
			Map newTypeMap = getGroupedEventMap(event, oldTypeMap);
			// putting against group severity.
			groupedEventMap.put(groupSeverity, newTypeMap);
		}

		// Getting grouped event tiles.
		return getGroupedTiles(dbCluster, groupedEventMap, false);
	}

	/**
	 * Get node event tiles.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param host
	 *            the host
	 * @return the node event tiles
	 */
	public List<TileInfo> getNodeEventTiles(Long clusterId, String host) {
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		TileInfo tileInfo = null;
		DBEventManager eManager = new DBEventManager();
		List<Event> events = eManager.getAlerts(clusterId, host, null,
				null, null);
		for (Event event : events) {
			tileInfo = new TileInfo();
			tileInfo.setLine1(event.getValue());

			tileInfo.setLine2(event.getName());
			String role = Constant.RoleProcessName.getRoleName(event
					.getCategory());
			if ((role != null) && (!role.isEmpty())) {
				tileInfo.setLine2(role);
			}

			tileInfo.setLine3(event.getHost());
			tileInfo.setStatus(event.getSeverity().toString());
			tiles.add(tileInfo);
		}
		return tiles;
	}

	/**
	 * Get all event tiles.
	 * 
	 * @return the system event tiles
	 */
	public List<TileInfo> getSystemEventTiles() {
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		List<Cluster> clusters = clusterManager.getAll();
		for (Cluster cluster : clusters) {
			tiles.addAll(getGroupedClusterTiles(cluster, true));
		}
		return tiles;
	}

	/**
	 * Method to get the cluster evnets.
	 * 
	 * @return the map
	 * @author hokam
	 */
	public List<TileInfo> getGroupedClusterTiles(Cluster dbCluster) {
		return getGroupedClusterTiles(dbCluster, false);
	}

	/**
	 * Method to get the cluster evnets.
	 * 
	 * @return the map
	 * @author hokam
	 */
	public List<TileInfo> getGroupedClusterTiles(Cluster dbCluster,
			boolean addClusterName) {
		// event manager object.
		DBEventManager eventManager = new DBEventManager();
		// Getting events by cluster id.
		List<Event> events = eventManager.getEvents(dbCluster.getId(), null,
				null, null, null, null);		

		// Grouped Event Map
		Map<String, Map> groupedEventMap = new HashMap<String, Map>();

		// iterating over the events.
		for (Event event : events) {
			// group+severity key
			String groupSeverity = event.getCategory() + "-"
					+ event.getSeverity();
			// old grouping type + severity map
			Map oldTypeMap = (Map) groupedEventMap.get(groupSeverity);
			// Getting new grouping event type
			Map newTypeMap = getGroupedEventMap(event, oldTypeMap);
			// putting against group severity.
			groupedEventMap.put(groupSeverity, newTypeMap);
		}

		// Getting grouped event tiles.
		return getGroupedTiles(dbCluster, groupedEventMap, addClusterName);
	}

	private List<TileInfo> getGroupedTiles(Cluster dbCluster,
			Map<String, Map> groupedEventMap, boolean addClusterName) {
		// List of tiles
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		// iterating over the map key set for creating tiles.
		for (Object key : groupedEventMap.keySet()) {
			// Getting grouped event map for the grouping+severity key
			Map eventMap = ((Map) groupedEventMap.get(key));
			// Get sub type.
			String subType = (String) eventMap.get(com.impetus.ankush2.constant.Constant.Keys.CATEGORY);
			// get severity
			String severity = (String) eventMap.get(com.impetus.ankush2.constant.Constant.Keys.SEVERITY);
			// event name.
			String groupingType = (String) eventMap
					.get(com.impetus.ankush2.constant.Constant.Keys.GROUPING_TYPE);
			// event count.
			Integer count = (Integer) eventMap.get(com.impetus.ankush2.constant.Constant.Keys.COUNT);
			// event type.
			String type = (String) eventMap.get(com.impetus.ankush2.constant.Constant.Keys.TYPE);

			// tile object.
			TileInfo tile = new TileInfo();
			// setting sub type.
			tile.setLine1(subType);

			// if it is usage type then replacing usage by utilisation.
			if (type.equals(Event.Type.USAGE.toString())) {
				tile.setLine2(groupingType + " Utilization");
			} else {
				// else setting line 2 as down for service events.
				tile.setLine2(com.impetus.ankush2.constant.Constant.Keys.DOWN);
				// Getting process role name.
				String role = Constant.RoleProcessName.getRoleName(subType);

				// if role is not null and not empty
				if ((role != null) && (!role.isEmpty())) {
					tile.setLine1(role);
				}
			}
			// setting line 3 as nodes count
			String line3 = count
					+ CommonUtil.singlePlural(count, " Node ", " Nodes ");
			if (addClusterName) {

				tile.setLine3(line3 + " | " + dbCluster.getName());
				// setting url as null
				tile.setUrl(null);
			} else {
				tile.setLine3(line3);
				// setting url
				tile.setUrl("Node List");
			}

			// setting status as severity
			tile.setStatus(severity);
			// preparing cluster data.
			Map data = new HashMap();
			// setting cluster id.
			data.put(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID, dbCluster.getId());
			// setting type as technology in data
			data.put(com.impetus.ankush2.constant.Constant.Keys.TYPE, dbCluster.getTechnology());
			// setting data in tile
			tile.setData(data);
			// adding tile in tiles list.
			tiles.add(tile);
		}
		return tiles;
	}

	private Map getGroupedEventMap(Event event, Map oldTypeMap) {
		// new grouping type + severity map
		Map newTypeMap = new HashMap();
		// setting severity
		newTypeMap.put(com.impetus.ankush2.constant.Constant.Keys.SEVERITY, event.getSeverity());
		// setting name
		newTypeMap.put(com.impetus.ankush2.constant.Constant.Keys.NAME, event.getName());
		// setting subtype
		newTypeMap.put(com.impetus.ankush2.constant.Constant.Keys.CATEGORY, event.getCategory());
		// setting type
		newTypeMap.put(com.impetus.ankush2.constant.Constant.Keys.TYPE, event.getType());

		// if it is null.
		if (oldTypeMap == null) {
			// set count to 1
			newTypeMap.put(com.impetus.ankush2.constant.Constant.Keys.COUNT, 1);
		} else {
			// removing the count from old grouping+severity map for
			// comparison
			Integer count = (Integer) oldTypeMap.remove(com.impetus.ankush2.constant.Constant.Keys.COUNT);
			// comparing old and new map.
			if (oldTypeMap.equals(newTypeMap)) {
				// increasing old count by 1
				newTypeMap.put(com.impetus.ankush2.constant.Constant.Keys.COUNT, ++count);
			} else {
				// setting the count to 1
				newTypeMap.put(com.impetus.ankush2.constant.Constant.Keys.COUNT, 1);
			}
		}
		return newTypeMap;
	}
}
