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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.HAService;
import com.impetus.ankush.common.domain.Service;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush2.constant.Constant.RegisterLevel;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;

public class DBServiceManager {
	/** The log. */
	private static AnkushLogger logger = new AnkushLogger(
			DBServiceManager.class);

	private GenericManager<Service, Long> serviceManager = AppStoreWrapper
			.getManager(Constant.Manager.SERVICE, Service.class);

	private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	private static DBServiceManager dbServiceManager = null;

	private DBServiceManager() {

	}

	public static DBServiceManager getManager() {
		if (dbServiceManager == null) {
			dbServiceManager = new DBServiceManager();
		}
		return dbServiceManager;
	}

	// Create property map
	private Map<String, Object> getPropertyMap(Long clusterId, String node,
			String component, String service, Boolean ha, Boolean stop,
			Boolean status) {
		Map<String, Object> propMap = new HashMap<String, Object>();
		if (clusterId != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
					clusterId);
		}

		if (node != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.NODE, node);
		}

		if (component != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.COMPONENT,
					component);
		}

		if (service != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.SERVICE,
					service);
		}

		if (ha != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.HA, ha);
		}

		if (stop != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.STOP, stop);
		}

		if (status != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.STATUS,
					status);
		}

		return propMap;
	}

	/**
	 * Get selected service record
	 * 
	 * @param clusterId
	 * @param node
	 * @param component
	 * @param service
	 * @return
	 */
	public Service getService(Long clusterId, String node, String component,
			String service) {
		try {
			return serviceManager.getByPropertyValueGuarded(getPropertyMap(
					clusterId, node, component, service, null, null, null));
		} catch (Exception e) {
			logger.error("Could not set stop flag.", e);
		}

		return null;
	}

	public boolean isAgentDown(String node) {
		Service service = getService(null, node,
				com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
				com.impetus.ankush2.constant.Constant.Role.AGENT);
		if (service == null) {
			return true;
		}
		return !service.getStatus();
	}

	/**
	 * Create new Service object
	 * 
	 * @param clusterId
	 * @param node
	 * @param component
	 * @param service
	 * @return
	 */
	private Service getNewService(Long clusterId, String node,
			String component, String service, RegisterLevel registrationType) {
		Service dbService = new Service();
		dbService.setClusterId(clusterId);
		dbService.setNode(node);
		dbService.setComponent(component);
		dbService.setService(service);
		// Set HA service status
		dbService.setHa(new DBHAServiceManager().getHAService(clusterId, node,
				component, service) != null);
		// setting default service type as MANAGED
		dbService.setRegistrationType(registrationType);
		dbService.setStatus(false);
		dbService.setStop(false);
		return dbService;
	}

	/**
	 * Add record for each service in service table
	 * 
	 * @param clusterId
	 * @param nodeList
	 */
	public void addClusterServices(ClusterConfig clusterConfig,
			Collection<NodeConfig> nodeList) {
		RegisterLevel registrationType;
		// Iterate nodes
		for (NodeConfig nodeConf : nodeList) {
			try {
				// Create record for agent
				Service dbService = getNewService(
						clusterConfig.getClusterId(),
						nodeConf.getPublicHost(),
						com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
						com.impetus.ankush2.constant.Constant.Role.AGENT,
						RegisterLevel.LEVEL3);
				serviceManager.save(dbService);

				// Iterate components
				for (String component : nodeConf.getRoles().keySet()) {
					if (clusterConfig.getComponents().get(component)
							.isRegister()) {
						registrationType = (RegisterLevel) clusterConfig
								.getComponents()
								.get(component)
								.getAdvanceConf()
								.get(com.impetus.ankush2.constant.Constant.Keys.REGISTER_LEVEL);
					} else {
						registrationType = RegisterLevel.LEVEL3;
					}
					// Iterate services
					for (String service : nodeConf.getRoles().get(component)) {
						String serviceName = Constant.RoleProcessName
								.getProcessName(service);
						if (serviceName == null)
							continue;
						// Create new service record
						dbService = getNewService(clusterConfig.getClusterId(),
								nodeConf.getPublicHost(), component, service,
								registrationType);
						serviceManager.save(dbService);
					}
				}
			} catch (Exception e) {
				logger.error("Could not create service record.", e);
			}

		}
	}

	/**
	 * Set HA service flag
	 * 
	 * @param clusterId
	 * @param services
	 */
	public synchronized void setHAService(Long clusterId,
			List<HAService> services) {
		// Set false to all
		setHAService(clusterId, null, null, null, null, null, false);

		// Set true to selected services
		for (HAService service : services) {
			setHAService(clusterId, null, service.getComponent(),
					service.getService(), null, null, true);
		}
	}

	/**
	 * Set service level force stop flag
	 * 
	 * @param clusterId
	 * @param host
	 * @param services
	 * @param flag
	 */
	public synchronized void setServicesForceStop(Long clusterId, String host,
			Map<String, Set<String>> services, boolean flag) {
		// Create property map
		for (String component : services.keySet()) {
			for (String service : new HashSet<String>(services.get(component))) {
				setForceStop(clusterId, host, component, service, null, null,
						flag);

			}
		}
	}

	/**
	 * Set setvice status
	 * 
	 * @param clusterId
	 * @param host
	 * @param serviceStatus
	 */
	public synchronized void setServicesStatus(Long clusterId, String host,
			Map<String, Map<String, Boolean>> serviceStatus) {

		for (String component : serviceStatus.keySet()) {
			for (String service : serviceStatus.get(component).keySet()) {
				try {
					Service dbService = getService(clusterId, host, component,
							service);
					if (dbService == null) {
						dbService = getNewService(clusterId, host, component,
								service, getRegisterLevel(clusterId, component));
					}
					dbService.setStatus(serviceStatus.get(component).get(
							service));
					serviceManager.save(dbService);

				} catch (AnkushException e) {
					logger.error(e.getMessage(), e);
				} catch (Exception e) {
					logger.error("Could not set ha flag.", e);
				}

			}
		}
	}

	private RegisterLevel getRegisterLevel(Long clusterId, String component)
			throws AnkushException {

		try {
			Cluster cluster = clusterManager.get(clusterId);
			if (cluster == null) {
				throw new AnkushException(
						"Could not get cluster object against cluster id "
								+ clusterId);
			}
			ClusterConfig clusterConfig = cluster.getClusterConfig();
			if (clusterConfig.getComponents() == null
					|| !clusterConfig.getComponents().containsKey(component)) {
				throw new AnkushException(
						"Cluster " + clusterConfig.getName() != null ? "with cluster name "
								+ clusterConfig.getName()
								: "" + " does not contain " + component);
			}
			ComponentConfig componentConfig = clusterConfig.getComponents()
					.get(component);
			if (componentConfig == null) {
				throw new AnkushException(
						"Component configuration object is null.");
			}
			if (componentConfig.isRegister()
					&& componentConfig
							.getAdvanceConfStringProperty(com.impetus.ankush2.constant.Constant.Keys.REGISTER_LEVEL) != null) {
				return com.impetus.ankush2.constant.Constant.RegisterLevel
						.valueOf(componentConfig
								.getAdvanceConfStringProperty(com.impetus.ankush2.constant.Constant.Keys.REGISTER_LEVEL));
			} else {
				return com.impetus.ankush2.constant.Constant.RegisterLevel.LEVEL3;
			}

		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"Exception while getting new service Register level value.",
					e);
		}
	}

	/**
	 * Get list of services based on property map
	 * 
	 * @param propMap
	 * @return
	 */
	public List<Service> getServices(Long clusterId, String host,
			String component, String service, Boolean ha, Boolean stop,
			Boolean status) {
		try {
			return serviceManager.getAllByPropertyValue(
					getPropertyMap(clusterId, host, component, service, ha,
							stop, status),
					com.impetus.ankush2.constant.Constant.Keys.COMPONENT,
					com.impetus.ankush2.constant.Constant.Keys.SERVICE);
		} catch (Exception e) {
			logger.error("Could not set stop flag.", e);
		}
		return new ArrayList<Service>();
	}

	/**
	 * Set force stop flag
	 * 
	 * @param propMap
	 * @param flag
	 */
	public synchronized void setForceStop(Long clusterId, String node,
			String component, String service, Boolean ha, Boolean status,
			boolean flag) {
		try {
			for (Service dbService : getServices(clusterId, node, component,
					service, ha, null, status)) {
				dbService.setStop(flag);
				serviceManager.save(dbService);
			}
		} catch (Exception e) {
			logger.error("Could not set stop flag.", e);
		}
	}

	/**
	 * Set HA service flag
	 * 
	 * @param propMap
	 * @param flag
	 */
	private void setHAService(Long clusterId, String node, String component,
			String service, Boolean stop, Boolean status, boolean flag) {
		try {
			for (Service dbService : getServices(clusterId, node, component,
					service, null, stop, status)) {
				dbService.setHa(flag);
				serviceManager.save(dbService);
			}
		} catch (Exception e) {
			logger.error("Could not set ha flag.", e);
		}
	}

	public void setStatus(Long clusterId, String node, String component,
			String service, Boolean stop, Boolean ha, Boolean status) {
		try {
			List<Service> services = getServices(clusterId, node, component,
					service, ha, stop, null);
			for (Service dbService : services) {
				dbService.setStatus(status);
				serviceManager.save(dbService);
			}
		} catch (Exception e) {
			logger.error("Could not set ha flag.", e);
		}
	}
}
