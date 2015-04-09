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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.HAService;
import com.impetus.ankush.common.domain.Service;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.utils.ObjectFactory;

public class DBHAServiceManager {
	private static AnkushLogger logger = new AnkushLogger(
			DBHAServiceManager.class);

	/** HA Service Database Manager *. */
	private GenericManager<HAService, String> hADBManager = AppStoreWrapper
			.getManager(Constant.Manager.HAService, HAService.class,
					String.class);

	public Set<HAService> getHAServices(Cluster dbCluster) {

		// Create blank services set
		Set<HAService> services = new HashSet<HAService>();

		// Get HAServices from database.
		List<HAService> dbServices = hADBManager.getAllByPropertyValue(
				com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
				dbCluster.getId());

		if (dbServices != null) {
			for (HAService service : dbServices) {
				services.add(service);
			}
		}

		// Get list of possible HAServices
		ClusterConfig clusterConf = dbCluster.getClusterConfig();
		for (String componentName : clusterConf.getComponents().keySet()) {

			try {
				Serviceable serviceable = ObjectFactory
						.getServiceObject(componentName);

				if (serviceable != null) {
					Set<String> serviceList = serviceable
							.getServiceList(clusterConf);
					if (serviceList != null && !serviceList.isEmpty()) {
						for (String service : serviceList) {
							services.add(new HAService(componentName, service));
						}
					}
				}
			} catch (Exception e) {
				logger.error("Could not get HA service list for "
						+ componentName + ".", e);
			}

		}
		return services;
	}

	/**
	 * Return HAService based if it is configured and not present in ignored
	 * list
	 * 
	 * @param clusterId
	 * @param component
	 * @param node
	 * @param service
	 * @return
	 */
	public HAService getHAService(Long clusterId, String node,
			String component, String service) {
		HAService haService = null;

		// Check into service table
		Service dbService = DBServiceManager.getManager().getService(clusterId,
				node, component, service);
		if (dbService != null && dbService.getHa() && !dbService.getStop()) {

			Map<String, Object> propMap = new HashMap<String, Object>();
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
					clusterId);
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.COMPONENT,
					component);
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.SERVICE,
					service);

			try {
				haService = hADBManager.getByPropertyValue(propMap);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return haService;
	}

	public Map updateServices(Long clusterId, List services) {
		Map result = new HashMap();

		try {

			if (new DBClusterManager().getCluster(clusterId) == null) {
				throw new AnkushException("Invalid cluster id.");
			}

			// Getting List of HA services from
			List<HAService> haServices = JsonMapperUtil.getListObject(services,
					HAService.class);

			remove(clusterId);
			for (HAService service : haServices) {
				service.setClusterId(clusterId);
				hADBManager.save(service);
			}

			// Set flag into Service table
			DBServiceManager.getManager().setHAService(clusterId, haServices);

			result.put("status", true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.put("status", false);
			result.put("error", e.getMessage());
		}
		return result;
	}

	public void remove(Long clusterId) {
		try {
			hADBManager.deleteAllByPropertyValue(
					com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
					clusterId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
