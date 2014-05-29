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
package com.impetus.ankush.common.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.HadoopClusterMonitor;

/**
 * It is used for giving the JSON for cluster monitoring.
 * 
 * @author hokam
 * 
 */
public class ClusterMonitor {

	/**
	 * Cluster Manager.
	 */
	static private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/* Logger object */
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(ClusterMonitor.class);

	// Errors List collection.
	/** The errors. */
	private List<String> errors = new ArrayList<String>();

	// Returning result map.
	/** The result. */
	private Map result = new HashMap();

	/**
	 * Return result.
	 *
	 * @return The result object.
	 */
	private Map returnResult() {
		if (this.errors.isEmpty()) {
			this.result.put("status", true);
		} else {
			this.result.put("status", false);
			this.result.put("error", this.errors);
		}
		return this.result;
	}

	/**
	 * Gets the map.
	 *
	 * @param clusterId the cluster id
	 * @param action the action
	 * @param parameterMap the parameter map
	 * @return the map
	 */
	public Map getMap(Long clusterId, String action, Map parameterMap) {
		try {
			// Getting cluster object from database.
			Cluster dbCluster = clusterManager.get(clusterId);
			if (dbCluster == null) {
				throw new Exception("Could not find cluster.");
			}

			// Getting monitorable object using the technology name.
			AbstractMonitor monitor = ObjectFactory
					.getMonitorableInstanceById(dbCluster.getTechnology());

			// Getting details map.
			result.putAll(monitor.monitor(dbCluster, action,
					getParameterMap(parameterMap)));
			return result;
		} catch (Exception e) {
			errors.add(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return returnResult();
	}

	/**
	 * Gets the map post.
	 *
	 * @param clusterId the cluster id
	 * @param action the action
	 * @param parameterMap the parameter map
	 * @return the map post
	 */
	public Map getMapPost(Long clusterId, String action, Map parameterMap) {
		try {
			// Getting cluster object from database.
			Cluster dbCluster = clusterManager.get(clusterId);
			if (dbCluster == null) {
				throw new Exception("Could not find cluster.");
			}

			logger.debug("Technology : " + dbCluster.getTechnology());
			// Getting monitorable object using the technology name.
			AbstractMonitor monitor = ObjectFactory
					.getMonitorableInstanceById(dbCluster.getTechnology());

			Map<String, Object> params = new HashMap<String, Object>();
			Set<String> keys = new HashSet<String>(parameterMap.keySet());
			for (String key : keys) {
				Object val = parameterMap.get(key);
				if (val != null) {
					params.put(key, val);
				}
			}

			// Getting details map.
			result.putAll(monitor.monitor(dbCluster, action, params));

			return result;
		} catch (Exception e) {
			errors.add(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return returnResult();
	}

	/**
	 * Method to fetch parameters from HTTPRequestParameterMap.
	 *
	 * @param parameterMap the parameter map
	 * @param name the name
	 * @return the parameter
	 */
	private String getParameter(Map parameterMap, final String name) {
		String[] strings = (String[]) parameterMap.get(name);
		if (strings != null) {
			return strings[0];
		}
		return null;
	}

	/**
	 * Retriev Key:Value map.
	 *
	 * @param parameterMap the parameter map
	 * @return the parameter map
	 */
	private Map getParameterMap(Map parameterMap) {
		Map<String, String> params = new HashMap<String, String>();
		Set<String> keys = new HashSet<String>(parameterMap.keySet());
		for (String key : keys) {
			params.put(key, getParameter(parameterMap, key));
		}
		return params;
	}
}
