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
package com.impetus.ankush2.framework.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBClusterManager;
import com.impetus.ankush2.db.DBNodeManager;
import com.impetus.ankush2.db.DBOperationManager;
import com.impetus.ankush2.framework.config.AlertsConf;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;

public class DatabaseUtils {
	private static AnkushLogger logger = new AnkushLogger(DatabaseUtils.class);

	public void addClusterOperation(ClusterConfig clusterConf,
			Constant.Cluster.Operation operation) {
		// Can be used during deploy operation
		addClusterOperation(clusterConf, operation, clusterConf.getCreatedBy());
	}

	public void addClusterOperation(ClusterConfig clusterConf,
			Constant.Cluster.Operation operation, String loggedInUser) {
		Operation dbOperation = new Operation();
		dbOperation.setClusterId(clusterConf.getClusterId());
		dbOperation.setOperationId(clusterConf.getOperationId());
		dbOperation.setOpName(operation.toString());
		dbOperation.setStartedAt(new Date());
		dbOperation.setStartedBy(loggedInUser);
		dbOperation.setStatus(Constant.Operation.Status.INPROGRESS.toString());
		if (new DBOperationManager().saveOperation(dbOperation) != null) {
			updateOperationProgress(clusterConf);
		}
	}

	public void updateClusterOperation(ClusterConfig clusterConf) {
		Operation operation = new DBOperationManager()
				.getOperation(clusterConf);
		if (operation == null) {
			logger.error("Could not find operation.");
			return;
		}
		operation.setCompletedAt(new Date());
		if (clusterConf.getState() != Constant.Cluster.State.ERROR) {
			operation.setStatus(Constant.Operation.Status.COMPLETED.toString());
		} else {
			operation.setStatus(Constant.Operation.Status.ERROR.toString());
			if (clusterConf.getProgress() != null) {
				clusterConf.getProgress().setState(
						Constant.Operation.Status.ERROR);
			}
		}
		updateOperationProgress(clusterConf);
		new DBOperationManager().saveOperation(operation);
	}

	// Update operation data
	public void updateOperationData(ClusterConfig clusterConf, String key,
			Object value) {
		if (value == null) {
			return;
		}
		Operation operation = new DBOperationManager()
				.getOperation(clusterConf);
		if (operation == null) {
			logger.error("Could not find operation.");
			return;
		}
		HashMap<String, Object> opData = operation.getData();
		if (opData == null) {
			opData = new HashMap<String, Object>();
		}
		opData.put(key, value);
		operation.setData(opData);
		new DBOperationManager().saveOperation(operation);
	}

	// Update operation progress
	public void updateOperationProgress(ClusterConfig clusterConf) {
		updateOperationData(clusterConf, "progress", clusterConf.getProgress());
	}

	// Save cluster details into database.
	public String saveCluster(ClusterConfig clusterConf) {
		logger.info("Saving cluster configuration into database.");

		// Create and save new cluster
		Cluster cluster = new DBClusterManager().getCluster(clusterConf
				.getName());
		if (cluster == null) {
			cluster = new Cluster();
			cluster.setName(clusterConf.getName());
			cluster.setCreatedAt(new Date());
			cluster.setUser(clusterConf.getCreatedBy());
			cluster.setAlertConf(new AlertsConf());
			cluster.setTechnology(clusterConf.getTechnology());
			// setting current milliseconds as Id of the cluster
			cluster.setId(System.currentTimeMillis());
		}
		cluster.setState(clusterConf.getState().toString());
		cluster.setClusterConf(clusterConf);
		cluster.setAgentVersion(AppStoreWrapper.getAgentBuildVersion());
		// save to database
		cluster = new DBClusterManager().saveCluster(cluster);
		if (cluster == null) {
			return "Could not save cluster details.";
		}

		clusterConf.setClusterId(cluster.getId());

		// Create and save nodes
		return saveNodes(clusterConf, clusterConf.getNodes().values());
	}

	// update nodes into database
	public String saveNodes(ClusterConfig clusterConf,
			Collection<NodeConfig> nodeConfList) {
		// agent build version

		String agentBuildVersion = AppStoreWrapper.getAgentBuildVersion();
		for (NodeConfig nodeConf : nodeConfList) {
			Node node = new DBNodeManager().getNode(nodeConf.getHost());
			if (node == null) {
				node = new Node();
				node.setCreatedAt(new Date());
				node.setRackInfo(nodeConf.getRack());
				node.setClusterId(clusterConf.getClusterId());
				node.setPrivateIp(nodeConf.getPublicHost());
				node.setPublicIp(nodeConf.getHost());
				// setting current milliseconds as Id of the node
				node.setId(System.currentTimeMillis());
			}
			if (nodeConf.getState() == null) {
				node.setState(Constant.Node.State.DEPLOYING.toString());
				nodeConf.setState(Constant.Node.State.DEPLOYING);
			} else {
				node.setState(nodeConf.getState().toString());
			}
			node.setNodeConfig(nodeConf);
			node.setAgentVersion(agentBuildVersion);
			node = new DBNodeManager().saveNode(node);
			if (node == null) {
				return "Could not save node details.";
			}
			nodeConf.setId(node.getId());
		}
		return null;
	}

	public Set<String> validateCluster(ClusterConfig clusterConf,
			boolean isCreated) {
		return validateCluster(clusterConf, clusterConf, isCreated);
	}

	// Check cluster name and hosts into database
	public Set<String> validateCluster(ClusterConfig clusterConf,
			ClusterConfig newClusterConf, boolean isCreated) {
		logger.info("Cluster level validation.");
		Set<String> errors = new LinkedHashSet<String>();

		// Check for cluster name
		if (!isCreated
				&& new DBClusterManager().getCluster(clusterConf.getName()) != null) {
			errors.add("Cluster '" + clusterConf.getName()
					+ "'  already exist.");
			return errors;
		}

		// Check hosts
		for (String host : newClusterConf.getNodes().keySet()) {
			Node node = new DBNodeManager().getNode(host);
			// continue if node does not exist
			if (node == null) {
				continue;
			}
			// continue if cluster is created and node is with in the cluster
			if (isCreated
					&& node.getClusterId().equals(clusterConf.getClusterId())) {
				continue;
			}
			errors.add("Node '" + host + "' is already in use.");
		}

		return errors;
	}

	public void removeNodes(Collection<String> nodeList) {
		for (String host : nodeList) {
			new DBNodeManager().remove(host);
		}
	}
}
