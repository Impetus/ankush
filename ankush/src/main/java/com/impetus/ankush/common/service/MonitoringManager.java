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
package com.impetus.ankush.common.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.TechnologyData;
import com.impetus.ankush.common.framework.config.MonitoringInfo;
import com.impetus.ankush.common.framework.config.NodeCpuInfo;
import com.impetus.ankush.common.framework.config.NodeDiskInfo;
import com.impetus.ankush.common.framework.config.NodeMemoryInfo;
import com.impetus.ankush.common.framework.config.NodeOSInfo;
import com.impetus.ankush.common.framework.config.NodeProcessInfo;
import com.impetus.ankush.common.framework.config.NodeSwapInfo;
import com.impetus.ankush.common.framework.config.NodeUpTimeInfo;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBEventManager;
import com.impetus.ankush2.db.DBServiceManager;
import com.impetus.ankush2.framework.manager.ClusterManager;

/**
 * The Class MonitoringManager.
 * 
 * @author hokam
 */
public class MonitoringManager {

	/** The Constant NODE_ID. */
	private static final String NODE_ID = "nodeId";

	// monitoring manager.
	/** The monitoring manager. */
	private GenericManager<NodeMonitoring, Long> monitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	// Node manager.
	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/**
	 * Ankush logger.
	 */
	private AnkushLogger LOG = new AnkushLogger(MonitoringManager.class);

	/**
	 * Save node monitoring info.
	 * 
	 * @param nodeId
	 *            the node id
	 * @param infoMap
	 *            the info map
	 */
	public boolean saveNodeMonitoringInfo(Long nodeId, Map infoMap) {
		boolean status = true;
		try {
			// getting the node object.
			Node node = nodeManager.get(nodeId);
			// get cluster from nodedId
			GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
					.getManager(Constant.Manager.CLUSTER, Cluster.class);

			// Return if not is not in deployed state
			if (clusterManager
					.get(node.getClusterId())
					.getState()
					.equalsIgnoreCase(
							Constant.Cluster.State.REMOVING.toString())
					|| !node.getState().equalsIgnoreCase(
							Constant.Node.State.DEPLOYED.toString())) {
				LOG.error("Cann't save node monitoring data as Cluster or node State is not Deployed.");
				status = false;
			} else {

				// Get the db node monitoring info
				NodeMonitoring nodeMonitoring = monitoringManager
						.getByPropertyValueGuarded(NODE_ID, nodeId);

				// if null create the new node monitoring obj.
				if (nodeMonitoring == null) {
					// Set initial node monitoring data
					nodeMonitoring = new NodeMonitoring();
					nodeMonitoring.setNodeId(nodeId);
				}

				// Create monitoring info object using map.
				MonitoringInfo monitoringInfo = getMonitoringInfo(infoMap);
				// Set monitoring info in node monitoring info.
				nodeMonitoring.setMonitoringInfo(monitoringInfo);
				// Setting update time in node monitoring info.
				nodeMonitoring.setUpdateTime(new Date());

				// Saving node monitoring info in db.
				nodeMonitoring = monitoringManager.save(nodeMonitoring);

				new DBEventManager().checkAlertsForUsage(node.getPublicIp(),
						node.getClusterId(), nodeMonitoring);
			}

		} catch (Exception e) {
			// Setting log error.
			LOG.error("Unable to save node monitoring info against nodeId : "
					+ nodeId, e);
			status = false;
		}
		return status;
	}

	/**
	 * Method to get node monitoring data.
	 * 
	 * @param nodeId
	 *            the node id
	 * @return the monitoring data
	 */
	public NodeMonitoring getMonitoringData(Long nodeId) {
		try {
			// Get the db node monitoring info
			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(NODE_ID, nodeId);
			return nodeMonitoring;
		} catch (Exception e) {
			// Setting log error.
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Method to get node monitoring data.
	 * 
	 * @param nodeId
	 *            the node id
	 * @return the monitoring data
	 */
	public TechnologyData getTechnologyData(String publicIp, String technology) {
		try {
			// Get the db node monitoring info
			NodeMonitoring nodeMonitoring = getMonitoringData(publicIp);
			return nodeMonitoring.getTechnologyData().get(technology);
		} catch (Exception e) {
			// Setting log error.
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Method to get node monitoring data.
	 * 
	 * @param publicIp
	 *            the public ip
	 * @return the monitoring data
	 */
	public NodeMonitoring getMonitoringData(String publicIp) {
		try {
			// Get the db node monitoring info
			Map<String, Object> propertyValueMap = new HashMap<String, Object>();
			propertyValueMap.put("publicIp", publicIp);
			List<NodeMonitoring> result = monitoringManager.getAllByNamedQuery(
					"getNodeMonitoring", propertyValueMap);
			if (result != null && !result.isEmpty()) {
				return result.get(0);
			}
		} catch (Exception e) {
			// Setting log error.
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Save Technology Service Status.
	 * 
	 * @param nodeId
	 * @param agentServiceStatus
	 */
	public boolean saveTecnologyServiceStatus(Long nodeId,
			HashMap<String, Map<String, Boolean>> agentServiceStatus) {
		boolean status = true;
		try {

			// getting the node object.
			Node node = nodeManager.get(nodeId);

			// get cluster from nodedId
			GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
					.getManager(Constant.Manager.CLUSTER, Cluster.class);

			// Return if not is not in deployed state
			if (clusterManager
					.get(node.getClusterId())
					.getState()
					.equalsIgnoreCase(
							Constant.Cluster.State.REMOVING.toString())
					|| !node.getState().equalsIgnoreCase(
							Constant.Node.State.DEPLOYED.toString())) {
				LOG.error("Cann't save node service status as Cluster or node State is not Deployed.");
				status = false;
			} else {

				// Get the db node monitoring info
				NodeMonitoring nodeMonitoring = monitoringManager
						.getByPropertyValueGuarded(NODE_ID, nodeId);

				// if null create the new node monitoring obj.
				if (nodeMonitoring == null) {
					nodeMonitoring = new NodeMonitoring();
					// Set initial node monitoring data
					nodeMonitoring.setNodeId(nodeId);
					// set service status.
					nodeMonitoring
							.setTechnologyServiceStatus(new HashMap<String, Map<String, Boolean>>());
				}

				// Update service into database if it is not empty
				if (!agentServiceStatus.isEmpty()) {

					Map<String, Map<String, Boolean>> serviceStatus = nodeMonitoring
							.getTechnologyServiceStatus();

					if (serviceStatus == null) {
						serviceStatus = new HashMap<String, Map<String, Boolean>>();
					}

					for (String key : agentServiceStatus.keySet()) {
						if (serviceStatus.containsKey(key)) {
							serviceStatus.get(key).putAll(
									agentServiceStatus.get(key));
						} else {
							serviceStatus.put(key, agentServiceStatus.get(key));
						}
					}
					// set service status.
					nodeMonitoring
							.setTechnologyServiceStatus((HashMap<String, Map<String, Boolean>>) serviceStatus);

					// Set service status into service table
					DBServiceManager.getManager().setServicesStatus(
							node.getClusterId(), node.getPublicIp(),
							agentServiceStatus);
				}

				// Set monitoring info in node monitoring info.
				nodeMonitoring.setUpdateTime(new Date());

				// Saving node monitoring info in database.
				nodeMonitoring = monitoringManager.save(nodeMonitoring);

				DBEventManager eventManager = new DBEventManager();
				eventManager.checkAlertsForService(node.getPublicIp(),
						node.getClusterId(), agentServiceStatus);
			}
		} catch (Exception e) {
			// Setting log error.
			LOG.error("Unable to save node service status against nodeId : "
					+ nodeId, e);
			status = false;
		}
		return status;
	}

	/**
	 * Save jobs info.
	 * 
	 * @param nodeId
	 *            the node id
	 * @param technologyData
	 *            the technology data
	 */
	public void saveMonitoringData(Long nodeId, TechnologyData technologyData) {
		try {
			// Get the db node monitoring info
			NodeMonitoring nodeMonitoring = monitoringManager
					.getByPropertyValueGuarded(NODE_ID, nodeId);

			// if null create the new node monitoring obj.
			if (nodeMonitoring == null) {
				nodeMonitoring = new NodeMonitoring();
			}

			Map<String, TechnologyData> map;
			try {
				map = nodeMonitoring.getTechnologyData();
			} catch (Exception e) {
				map = null;
			}

			if (map == null) {
				map = new HashMap<String, TechnologyData>();
			}

			TechnologyData oldTechnologyData = map.get(technologyData
					.getTechnologyName());

			MonitoringListener saveListener = technologyData
					.getMonitoringListener();

			if (saveListener != null) {
				saveListener.preSave(oldTechnologyData, technologyData);
			}

			map.put(technologyData.getTechnologyName(), technologyData);
			nodeMonitoring
					.setTechnologyData((HashMap<String, TechnologyData>) map);

			// Set monitoring info in node monitoring info.
			nodeMonitoring.setUpdateTime(new Date());
			// setting node id in node monitoring info.
			nodeMonitoring.setNodeId(nodeId);
			// Saving node monitoring info in db.
			nodeMonitoring = monitoringManager.save(nodeMonitoring);
		} catch (Exception e) {
			// Setting log error.
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Gets the monitoring info.
	 * 
	 * @param info
	 *            the info
	 * @return the monitoring info
	 * @throws Exception
	 *             the exception
	 */
	private MonitoringInfo getMonitoringInfo(Map info) throws Exception {

		// Creating CPU info list.
		List<NodeCpuInfo> cpuInfos = getNodeInfoList(info,
				Constant.Node.Info.CPU, NodeCpuInfo.class);

		// Creating node memory info list.
		List<NodeMemoryInfo> memoryInfos = getNodeInfoList(info,
				Constant.Node.Info.MEMORY, NodeMemoryInfo.class);

		// Create disk memory info list.
		List<NodeDiskInfo> diskInfos = getNodeInfoList(info,
				Constant.Node.Info.DISK, NodeDiskInfo.class);

		// Creating node up time info list.
		List<NodeUpTimeInfo> uptimeInfos = getNodeInfoList(info,
				Constant.Node.Info.UPTIME, NodeUpTimeInfo.class);

		// Creating node OS info list.
		List<NodeOSInfo> osInfos = getNodeInfoList(info, Constant.Node.Info.OS,
				NodeOSInfo.class);

		// Creating node swap info list.
		List<NodeSwapInfo> swapInfos = getNodeInfoList(info,
				Constant.Node.Info.SWAP, NodeSwapInfo.class);

		// Creating node process list of top high memory usage.
		List<NodeProcessInfo> processMemory = getNodeInfoList(info,
				Constant.Node.Info.PROCESS_MEMORY, NodeProcessInfo.class);

		// Creating node process list of top high cpu usage.
		List<NodeProcessInfo> processCPU = getNodeInfoList(info,
				Constant.Node.Info.PROCESS_CPU, NodeProcessInfo.class);

		// Create monitoring info object.
		MonitoringInfo monitoringInfo = new MonitoringInfo();

		// set cpu info.
		monitoringInfo.setCpuInfos(cpuInfos);
		// set memory info.
		monitoringInfo.setMemoryInfos(memoryInfos);
		// set disk info.
		monitoringInfo.setDiskInfos(diskInfos);
		// set uptime info.
		monitoringInfo.setUptimeInfos(uptimeInfos);
		// set os info.
		monitoringInfo.setOsInfos(osInfos);
		// set swap info.
		monitoringInfo.setSwapInfos(swapInfos);
		// set high Memory using processes.
		monitoringInfo.setProcessMemory(processMemory);
		// set high CPU using processes.
		monitoringInfo.setProcessCPU(processCPU);

		return monitoringInfo;
	}

	/**
	 * Gets the node info list.
	 * 
	 * @param <S>
	 *            the generic type
	 * @param monitoringInfo
	 *            the monitoring info
	 * @param key
	 *            the key
	 * @param targetClass
	 *            the target class
	 * @return the node info list
	 * @throws Exception
	 *             the exception
	 */
	private <S> List<S> getNodeInfoList(Map monitoringInfo, String key,
			Class<S> targetClass) throws Exception {

		// checking if map contains the key.
		if (!monitoringInfo.containsKey(key)) {
			return null;
		}
		// Getting list of maps from the info map using the key.
		List<Map<String, Object>> infoList = (List<Map<String, Object>>) monitoringInfo
				.get(key);

		return getObjectList(targetClass, infoList);
	}

	/**
	 * Gets the object list.
	 * 
	 * @param <S>
	 *            the generic type
	 * @param targetClass
	 *            the target class
	 * @param infoList
	 *            the info list
	 * @return the object list
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
	private <S> List<S> getObjectList(Class<S> targetClass,
			List<Map<String, Object>> infoList) throws InstantiationException,
			IllegalAccessException, InvocationTargetException {
		// Creating the resultant list object.
		List<S> result = new ArrayList<S>(infoList.size());

		// populating values in the list object from map.
		for (Map<String, Object> info : infoList) {
			// creating target class object.
			S status = targetClass.newInstance();
			// populating object with map values.
			BeanUtils.populate(status, info);
			// adding object in result list.
			result.add(status);
		}
		return result;
	}

	/**
	 * Save.
	 * 
	 * @param nodeMonitoring
	 *            the node monitoring
	 */
	public void save(NodeMonitoring nodeMonitoring) {
		try {
			monitoringManager.save(nodeMonitoring);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
