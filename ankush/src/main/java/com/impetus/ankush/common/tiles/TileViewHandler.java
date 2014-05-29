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

import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * The Class TileViewHandler.
 *
 * @author bgunjan
 */
public abstract class TileViewHandler {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(TileViewHandler.class);

	// Cluster Manager
	/** The cluster manager. */
	static protected GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	// monitoring manager.
	/** The monitoring manager. */
	protected GenericManager<NodeMonitoring, Long> monitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	// Node manager
	/** The node manager. */
	protected GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/**
	 * Gets the cluster details tiles.
	 *
	 * @param cluster the cluster
	 * @param parameter the parameter
	 * @return the cluster details tiles
	 */
	public abstract List<TileInfo> getClusterDetailsTiles(Cluster cluster,
			Map parameter);

	/**
	 * Gets the node details tiles.
	 *
	 * @param cluster the cluster
	 * @param parameter the parameter
	 * @return the node details tiles
	 */
	public abstract List<TileInfo> getNodeDetailsTiles(Cluster cluster,
			Map parameter);

	/**
	 * Gets the tile info.
	 *
	 * @param line1 the line1
	 * @param line2 the line2
	 * @param line3 the line3
	 * @param data the data
	 * @param status the status
	 * @param url TODO
	 * @return TileInfo
	 */
	public static TileInfo getTileInfo(String line1, String line2,
			String line3, Map<String, Object> data, String status, String url) {
		TileInfo tileInfo = new TileInfo();
		tileInfo.setLine1(line1);
		tileInfo.setLine2(line2);
		tileInfo.setLine3(line3);
		tileInfo.setData(data);
		tileInfo.setStatus(status);
		tileInfo.setUrl(url);

		return tileInfo;
	}
}
