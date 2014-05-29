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
package com.impetus.ankush.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringListener;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.TileInfo;

public class CassandraJMXData implements TechnologyData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The tiles. */
	private List<TileInfo> tiles = new ArrayList<TileInfo>();

	/** The datacenters. */
	private List<Datacenter> datacenters = new ArrayList<Datacenter>();

	public CassandraJMXData() {
	}

	/**
	 * Gets the tiles.
	 * 
	 * @return the tiles
	 */
	public List<TileInfo> getTiles() {
		return tiles;
	}

	/**
	 * Sets the tiles.
	 * 
	 * @param tiles
	 *            the tiles to set
	 */
	public void setTiles(List<TileInfo> tiles) {
		this.tiles = tiles;
	}

	/**
	 * Gets the datacenters.
	 * 
	 * @return the datacenters
	 */
	public List<Datacenter> getDatacenters() {
		return datacenters;
	}

	/**
	 * Sets the datacenters.
	 * 
	 * @param datacenters
	 *            the datacenters to set
	 */
	public void setDatacenters(List<Datacenter> datacenters) {
		this.datacenters = datacenters;
	}

	public static CassandraJMXData getTechnologyData(CassandraConf conf,
			boolean ignoreStatus) {

		if (conf == null) {
			return null;
		}
		try {
			NodeMonitoring nodeMonitoring;
			CassandraJMXData techData = null;
			for (CassandraNodeConf nodeConf : conf.getNodes()) {
				if (nodeConf.isSeedNode()) {
					nodeMonitoring = new MonitoringManager()
							.getMonitoringData(nodeConf.getPublicIp());

					if (nodeMonitoring != null
							&& nodeMonitoring.getTechnologyData() != null) {

						if (!ignoreStatus) {
							// Check if service is running or not
							Map<String, Boolean> serviceStatus = nodeMonitoring
									.getServiceStatus();
							if (serviceStatus
									.containsKey(Constant.Component.ProcessName.CASSANDRA)
									&& serviceStatus
											.get(Constant.Component.ProcessName.CASSANDRA)) {
								techData = (CassandraJMXData) nodeMonitoring
										.getTechnologyData().get(
												Constant.Technology.CASSANDRA);
								if (!techData.getDatacenters().isEmpty()) {
									return techData;
								}
							}
						} else {
							techData = (CassandraJMXData) nodeMonitoring
									.getTechnologyData().get(
											Constant.Technology.CASSANDRA);
							if (!techData.getDatacenters().isEmpty()) {
								return techData;
							}
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * Get cluster specific technology data.
	 * 
	 * @param conf
	 *            the conf
	 * @return the technology data
	 */
	public static CassandraJMXData getTechnologyData(CassandraConf conf) {

		return getTechnologyData(conf, false);
	}

	@Override
	public String getTechnologyName() {
		return Constant.Technology.CASSANDRA;
	}

	@Override
	public MonitoringListener getMonitoringListener() {
		// TODO Auto-generated method stub
		return null;
	}
}
