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
package com.impetus.ankush2.cassandra.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.service.MonitoringListener;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush2.cassandra.utils.CassandraConstants;
import com.impetus.ankush2.cassandra.utils.CassandraUtils;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.logger.AnkushLogger;

public class CassandraJMXData implements TechnologyData {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The logger. */
	private static AnkushLogger LOG = new AnkushLogger(CassandraJMXData.class);

	/** The datacenters. */
	private List<Datacenter> datacenters = new ArrayList<Datacenter>();

	public CassandraJMXData() {
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

	public static CassandraJMXData getTechnologyData(
			ClusterConfig clusterConfig, boolean ignoreStatus) {

		if (clusterConfig == null) {
			return null;
		}
		try {
			NodeMonitoring nodeMonitoring;
			CassandraJMXData techData = null;
			for (String host : clusterConfig.getNodes().keySet()) {
				if (CassandraUtils.isSeedNode(clusterConfig.getNodes()
						.get(host).getRoles())) {
					nodeMonitoring = new MonitoringManager()
							.getMonitoringData(host);
					if (nodeMonitoring != null
							&& nodeMonitoring.getTechnologyData() != null) {

						if (!ignoreStatus) {
							// Cassandra service status
							Map<String, Boolean> serviceStatus = nodeMonitoring
									.getServiceStatus(Constant.Component.Name.CASSANDRA);

							if (serviceStatus
									.containsKey(CassandraConstants.Node_Type.CASSANDRA_SEED)
									&& serviceStatus
											.get(CassandraConstants.Node_Type.CASSANDRA_SEED)) {
								techData = (CassandraJMXData) nodeMonitoring
										.getTechnologyData()
										.get(Constant.Component.Name.CASSANDRA);
								if (!techData.getDatacenters().isEmpty()) {
									return techData;
								}
							}
						} else {
							techData = (CassandraJMXData) nodeMonitoring
									.getTechnologyData().get(
											Constant.Component.Name.CASSANDRA);
							if (!techData.getDatacenters().isEmpty()) {
								return techData;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			if (!ignoreStatus) {
				LOG.error(
						"Could not get Cassandra technology data.",
						com.impetus.ankush2.constant.Constant.Component.Name.CASSANDRA,
						e);
			}
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
	public static CassandraJMXData getTechnologyData(ClusterConfig clusterConfig) {

		return getTechnologyData(clusterConfig, false);
	}

	@Override
	public String getTechnologyName() {
		return Constant.Component.Name.CASSANDRA;
	}

	@Override
	public MonitoringListener getMonitoringListener() {
		// TODO Auto-generated method stub
		return null;
	}
}
