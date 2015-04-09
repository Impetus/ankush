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
package com.impetus.ankush2.db;

import java.util.ArrayList;
import java.util.List;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;

public class DBClusterManager {
	private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	private static AnkushLogger logger = new AnkushLogger(
			DBClusterManager.class);

	public Cluster getCluster(String clusterName) {
		try {
			return clusterManager
					.getByPropertyValueGuarded("name", clusterName);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public Cluster getCluster(Long clusterId) {
		try {
			return clusterManager.get(clusterId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public List<Cluster> getClusters() {
		try {
			return clusterManager.getAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ArrayList<Cluster>();
	}

	public Cluster saveCluster(Cluster cluster) {
		try {
			return clusterManager.save(cluster);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public void remove(Long clusterId) {
		try {
			// remove the audit trail records.
			new ConfigurationManager().removeAuditTrail(clusterId);
			
			// Remove cluster.
			clusterManager.remove(clusterId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
