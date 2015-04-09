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
package com.impetus.ankush2.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.HostOperation;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBOperationManager;
import com.impetus.ankush2.framework.config.AuthConfig;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.framework.config.ProgressConfig;
import com.impetus.ankush2.framework.utils.DatabaseUtils;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class AnkushUtils.
 */
public class AnkushUtils {

	/** The log. */
	private static AnkushLogger LOG = new AnkushLogger(AnkushUtils.class);

	public static boolean getServiceStatus(String host, String serviceName,
			String componentName) throws AnkushException {
		try {
			MonitoringManager monitoringManager = new MonitoringManager();
			NodeMonitoring nodeMonitoring = monitoringManager
					.getMonitoringData(host);
			if (nodeMonitoring != null) {
				// Elastic search technology services
				Map<String, Boolean> serviceStatusMap = nodeMonitoring
						.getServiceStatus(componentName);
				if (serviceStatusMap != null) {
					if (serviceStatusMap.get(serviceName) != null
							&& serviceStatusMap.get(serviceName)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			String errMsg = "Could not get service status for Host-" + host
					+ ", Component-" + componentName + ", Service-"
					+ serviceName + ".";
			LOG.error(errMsg, componentName, e);
			throw new AnkushException(errMsg);
		}
		return false;
	}

	public static boolean isAnyAgentDown(Set<String> hostList) {
		try {
			for (String host : hostList) {
				if (!AnkushUtils.getServiceStatus(host, Constant.Role.AGENT,
						Constant.Component.Name.AGENT)) {
					LOG.error("Agent Down on host-" + host + ".",
							Constant.Component.Name.AGENT);
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			LOG.error("Could not check Agent service status.",
					Constant.Component.Name.AGENT);
			return true;
		}
	}

	/**
	 * Adds the node to cluster config.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param host
	 *            the host
	 * @param datacenter
	 *            the datacenter
	 * @param rack
	 *            the rack
	 * @return true, if successful
	 * @throws AnkushException
	 *             the ankush exception
	 * @throws Exception
	 *             the exception
	 */
	public static boolean addNodeToClusterConfig(ClusterConfig clusterConfig,
			String host, String datacenter, String rack)
			throws AnkushException, Exception {
		if (clusterConfig == null) {
			throw (new AnkushException(
					"Unable to add "
							+ host
							+ " to cluster: "
							+ Constant.Strings.ExceptionsMessage.INVALID_CLUSTER_CONFIG_MSG));
		}

		if (clusterConfig.getNodes() == null) {
			clusterConfig.setNodes(new HashMap<String, NodeConfig>());
		} else if (clusterConfig.getNodes().containsKey(host)) {
			return true;
		}
		NodeConfig nodeConfig = getNewNodeConfig(host, datacenter, rack,
				clusterConfig.getAuthConf());
		clusterConfig.getNodes().put(host, nodeConfig);
		return true;
	}

	public static void connectNodes(ClusterConfig clusterConfig,
			Collection<NodeConfig> nodeConfList) {
		for (NodeConfig nodeConf : nodeConfList) {
			if (nodeConf.getConnection() == null) {
				nodeConf.setConnection(SSHUtils.connectToNode(
						nodeConf.getHost(), clusterConfig.getAuthConf()));
			}
		}
	}

	public static void connectNodesString(ClusterConfig clusterConfig,
			Collection<String> hostList) {
		for (String host : hostList) {
			NodeConfig nodeConf = clusterConfig.getNodes().get(host);
			if (nodeConf.getConnection() == null) {
				nodeConf.setConnection(SSHUtils.connectToNode(
						nodeConf.getHost(), clusterConfig.getAuthConf()));
			}
		}
	}

	public static void disconnectNodesString(ClusterConfig clusterConfig,
			Collection<String> hostList) {
		for (String host : hostList) {
			NodeConfig nodeConf = clusterConfig.getNodes().get(host);
			SSHExec connection = nodeConf.getConnection();
			if (connection != null) {
				connection.disconnect();
			}
			nodeConf.setConnection(null);
		}
	}

	public static void disconnectCompNodes(ClusterConfig clusterConfig,
			Collection<String> hostList) {
		for (String host : hostList) {
			NodeConfig nodeConf = clusterConfig.getNodes().get(host);
			SSHExec connection = nodeConf.getConnection();
			if (connection != null) {
				connection.disconnect();
			}
			nodeConf.setConnection(null);
		}
	}

	public static void disconnectNodes(ClusterConfig clusterConfig,
			Collection<NodeConfig> nodeConfList) {
		for (NodeConfig nodeConf : nodeConfList) {
			SSHExec connection = nodeConf.getConnection();
			if (connection != null) {
				connection.disconnect();
			}
			nodeConf.setConnection(null);
		}
	}

	/**
	 * 
	 * @param host
	 * @param datacenter
	 * @param rack
	 * @param authConfig
	 * @return
	 */
	private static NodeConfig getNewNodeConfig(String host, String datacenter,
			String rack, AuthConfig authConfig) {
		NodeConfig nodeConfig = new NodeConfig();
		nodeConfig.setHost(host);
		nodeConfig.setPublicHost(host);
		nodeConfig.setDatacenter(datacenter);
		nodeConfig.setRack(rack);
		nodeConfig.setState(Constant.Node.State.DEPLOYING);
		nodeConfig.setMonitor(false);
		nodeConfig.setStatus(true);
		nodeConfig.setOs(HostOperation.getOsForNode(host, authConfig));
		return nodeConfig;
	}

	public static boolean getStatus(Map<String, NodeConfig> map) {
		boolean status = false;
		for (NodeConfig nodeConf : map.values()) {
			status = status || nodeConf.getStatus();
			// if (!nodeConf.getStatus()) {
			// return false;
			// }
		}
		return status;
	}

	public static boolean getStatus(ClusterConfig clusterConf,
			Collection<String> nodeList) {
		boolean status = false;
		for (String host : nodeList) {
			status = status || clusterConf.getNodes().get(host).getStatus();
			// if (!clusterConf.getNodes().get(host).getStatus())
			// return false;
		}
		return status;
	}

	public static void addNodes(ClusterConfig clusterConf,
			ClusterConfig newClusterConf) {
		// Add nodes into cluster conf
		clusterConf.getNodes().putAll(newClusterConf.getNodes());

		// Add nodes into component conf
		for (String component : newClusterConf.getComponents().keySet()) {
			if (component == Constant.Component.Name.AGENT) {
				continue;
			}
			clusterConf
					.getComponents()
					.get(component)
					.getNodes()
					.putAll(newClusterConf.getComponents().get(component)
							.getNodes());
		}
	}

	public static void removeNodes(ClusterConfig clusterConf, List<String> nodes) {
		// Remove node from cluster configuration
		clusterConf.getNodes().keySet().removeAll(nodes);
		// Remove node from database
		new DatabaseUtils().removeNodes(nodes);
	}

	/**
	 * Adds the node to cluster and component.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param host
	 *            the host
	 * @param nodeRoles
	 *            the node roles
	 * @param component
	 *            the component
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	public static boolean addNodeToClusterAndComponent(
			ClusterConfig clusterConfig, String host, Set<String> nodeRoles,
			String component) throws Exception {
		if (addNodeToClusterConfig(clusterConfig, component, host, nodeRoles,
				null, null)) {
			return addNodeToComponentConfig(clusterConfig, component, host,
					new HashMap<String, Object>());
		}
		return false;
	}

	/**
	 * @param clusterConfig
	 * @param component
	 * @param host
	 * @param nodeRoles
	 * @param datacenter
	 * @param rack
	 * @return
	 * @throws AnkushException
	 * @throws Exception
	 */
	public static boolean addNodeToClusterConfig(ClusterConfig clusterConfig,
			String component, String host, Set<String> nodeRoles,
			String datacenter, String rack) throws AnkushException, Exception {
		if (clusterConfig == null) {
			throw (new AnkushException(
					"Unable to add "
							+ host
							+ " to cluster: "
							+ Constant.Strings.ExceptionsMessage.INVALID_CLUSTER_CONFIG_MSG));
		}
		NodeConfig nodeConfig = null;
		if (clusterConfig.getNodes().containsKey(host)) {
			nodeConfig = clusterConfig.getNodes().get(host);
		}
		if (nodeConfig != null) {
			if (nodeConfig.getRoles().containsKey(component)) {
				(nodeConfig.getRoles().get(component)).addAll(nodeRoles);
			} else {
				nodeConfig.getRoles().put(component, nodeRoles);
			}
		} else {
			nodeConfig = getNewNodeConfig(host, component, nodeRoles,
					datacenter, rack, clusterConfig.getAuthConf());
			clusterConfig.getNodes().put(host, nodeConfig);
		}
		return true;
	}

	/**
	 * @param clusterConfig
	 * @param component
	 * @param host
	 * @param advanceConf
	 * @return
	 */
	public static boolean addNodeToComponentConfig(ClusterConfig clusterConfig,
			String component, String host, Map<String, Object> advanceConf) {
		if (clusterConfig.getComponents().containsKey(component)) {
			if (clusterConfig.getComponents().get(component).getNodes() == null) {
				clusterConfig.getComponents().get(component)
						.setNodes(new HashMap<String, Map<String, Object>>());
			}
			if (!clusterConfig.getComponents().get(component).getNodes()
					.containsKey(host)) {
				clusterConfig.getComponents().get(component).getNodes()
						.put(host, advanceConf);
			}
		}
		return true;
	}

	/**
	 * @param host
	 * @param component
	 * @param nodeRoles
	 * @param datacenter
	 * @param rack
	 * @param authConfig
	 * @return
	 */
	private static NodeConfig getNewNodeConfig(String host, String component,
			Set<String> nodeRoles, String datacenter, String rack,
			AuthConfig authConfig) {
		Map<String, Set<String>> compRoleMap = new HashMap<String, Set<String>>();
		compRoleMap.put(component, nodeRoles);
		NodeConfig nodeConfig = new NodeConfig();
		nodeConfig.setHost(host);
		nodeConfig.setPublicHost(host);
		nodeConfig.setDatacenter(datacenter);
		nodeConfig.setRack(rack);
		nodeConfig.setState(Constant.Node.State.DEPLOYING);
		nodeConfig.setRoles(compRoleMap);
		nodeConfig.setMonitor(false);
		nodeConfig.setStatus(true);
		nodeConfig.setOs(HostOperation.getOsForNode(host, authConfig));
		return nodeConfig;
	}

	public static boolean isMonitoredByAnkush(ComponentConfig compConfig) {
		if (compConfig.isRegister()) {
			Constant.RegisterLevel registerLevel = Constant.RegisterLevel
					.valueOf((String) compConfig.getAdvanceConf().get(
							Constant.Keys.REGISTER_LEVEL));
			if (registerLevel != null) {
				if (registerLevel.equals(Constant.RegisterLevel.LEVEL3)
						|| registerLevel.equals(Constant.RegisterLevel.LEVEL2)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isManagedByAnkush(ComponentConfig compConfig) {
		if (compConfig.isRegister()) {
			Constant.RegisterLevel registerLevel = Constant.RegisterLevel
					.valueOf((String) compConfig.getAdvanceConf().get(
							Constant.Keys.REGISTER_LEVEL));
			if (registerLevel != null) {
				if (registerLevel.equals(Constant.RegisterLevel.LEVEL3)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public static void addOperation(Constant.Cluster.Operation opName,
			ClusterConfig clusterConfig) {
		// Save operation details
		DatabaseUtils databaseUtils = new DatabaseUtils();
		databaseUtils.addClusterOperation(clusterConfig, opName);
	}

	public static void updateOperationStatus(ClusterConfig clusterConfig,
			String status) {
		Operation operation = new DBOperationManager()
				.getOperation(clusterConfig);
		if (operation == null) {
			LOG.error("Could not find operation.");
			return;
		}
		operation.setCompletedAt(new Date());
		operation.setStatus(status);
		new DBOperationManager().saveOperation(operation);
	}

	public static List<String> listFilesInDir(SSHExec connection, String host,
			String dirPath, String regex) throws Exception {
		dirPath = FileUtils.getSeparatorTerminatedPathEntry(dirPath);
		String command = "cd " + dirPath + ";ls -p " + regex + " | grep -v /";
		CustomTask listFiles = new ExecCommand(command);
		Result res = connection.exec(listFiles);
		if (res.rc != 0) {
			throw new AnkushException(
					"Could not get file list for directory - " + dirPath
							+ " on host - " + host);
		}
		List<String> logFilesList = new ArrayList<String>();
		logFilesList.addAll(Arrays.asList(res.sysout
				.split(Constant.Strings.LINE_SEPERATOR)));
		return logFilesList;
	}

}
