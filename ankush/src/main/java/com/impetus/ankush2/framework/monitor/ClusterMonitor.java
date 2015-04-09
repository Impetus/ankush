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
package com.impetus.ankush2.framework.monitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBClusterManager;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.monitor.AbstractMonitor;
import com.impetus.ankush2.framework.utils.ObjectFactory;

public class ClusterMonitor extends AbstractMonitor {

	public Map getMap(Long clusterId, String action, Map parameterMap) {
		try {
			AbstractMonitor monitor = null;
			// Getting cluster object from database.
			Cluster dbCluster = new DBClusterManager().getCluster(clusterId);
			if (dbCluster == null) {
				throw new Exception("Could not find cluster.");
			}

			Map<String, String> params = getParameterMap(parameterMap);

			if (params.containsKey(Constant.Keys.COMPONENT)) {
				monitor = ObjectFactory.getMonitorObject((String) (params
						.get(Constant.Keys.COMPONENT)));
				if (monitor == null) {
					throw new Exception("Could not create monitor object.");
				}
			} else {
				monitor = this;
			}
			monitor.monitor(dbCluster, action, params);
			return monitor.returnResult();
		} catch (Exception e) {
			errors.add(e.getMessage());
			logger.error(e.getMessage(), e);
			return returnResult();
		}
	}

	/**
	 * Retriev Key:Value map.
	 * 
	 * @param parameterMap
	 *            the parameter map
	 * @return the parameter map
	 */
	private Map<String, String> getParameterMap(Map parameterMap) {
		Map<String, String> params = new HashMap<String, String>();
		Set<String> keys = new HashSet<String>(parameterMap.keySet());
		String[] values = null;
		for (String key : keys) {
			values = (String[]) parameterMap.get(key);
			if (values != null) {
				params.put(key, values[0]);
			} else {
				params.put(key, null);
			}
		}
		return params;
	}

	public Map getMapPost(Long clusterId, String action, Map parameterMap) {
		try {
			AbstractMonitor monitor = null;
			// Getting cluster object from database.
			Cluster dbCluster = new DBClusterManager().getCluster(clusterId);
			if (dbCluster == null) {
				throw new Exception("Could not find cluster.");
			}

			Map<String, Object> params = getParameterMapPost(parameterMap);

			if (params.containsKey(Constant.Keys.COMPONENT)) {
				monitor = ObjectFactory.getMonitorObject((String) (params
						.get(Constant.Keys.COMPONENT)));

				if (monitor == null) {
					throw new Exception("Could not create monitor object.");
				}
			} else {
				monitor = this;
			}
			monitor.monitor(dbCluster, action, params);
			return monitor.returnResult();
		} catch (Exception e) {
			errors.add(e.getMessage());
			logger.error(e.getMessage(), e);
			return returnResult();
		}
	}

	private Map<String, Object> getParameterMapPost(Map parameterMap) {
		Map<String, Object> params = new HashMap<String, Object>();
		Set<String> keys = new HashSet<String>(parameterMap.keySet());
		for (String key : keys) {
			Object val = (Object) parameterMap.get(key);
			if (val != null) {
				params.put(key, val);
			} else {
				params.put(key, null);
			}
		}
		return params;
	}

	@Override
	public boolean canNodesBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes, String componentName) {
		return false;
	}

	public List<String> canNodesBeDeleted(ClusterConfig clusterConfig,
			Map<String, Set<String>> compNodeMap) {
		try {
			boolean deletionPossible = true;
			for (String component : compNodeMap.keySet()) {
				AbstractMonitor monitor = ObjectFactory
						.getMonitorObject(component);
				if (monitor != null) {
					deletionPossible = monitor.canNodesBeDeleted(clusterConfig,
							compNodeMap.get(component), component) && deletionPossible;
					this.errors.addAll(monitor.errors);
				}
			}
			if (!deletionPossible && this.errors.isEmpty()) {
				addAndLogError("Could not delete nodes.");
			}
		} catch (Exception e) {
			addAndLogError("Exception while getting status for nodes deletion.");
		}
		return this.errors;
	}

}
