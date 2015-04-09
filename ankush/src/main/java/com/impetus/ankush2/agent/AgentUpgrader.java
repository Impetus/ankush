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
package com.impetus.ankush2.agent;

import java.util.List;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.logger.AnkushLogger;

public class AgentUpgrader {

	protected AnkushLogger LOGGER = new AnkushLogger(AgentUpgrader.class);

	/** The cluster manager. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);
	/** The node manager. */
	protected GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	protected static final String MESSAGE_UPGRADING_AGENT = "Upgrading Agent";

	protected static final String COULD_NOT_UPGRADE_AGENT = "Could not update Agent";

	// ankush home on node.
	public static final String NODE_ANKUSH_HOME = "$HOME/.ankush/";

	// current agent version
	String agentBuildVersion = new String();

	/**
	 * Agent Upgrader
	 */
	public AgentUpgrader() {
		// Agent build version.
		this.agentBuildVersion = AppStoreWrapper.getAgentBuildVersion();
	}

	/**
	 * Method to upgrade agent asynchronously.
	 */
	public void asyncUpgradeAgent() {
		// Calling executor to upgrade agent.
		AppStoreWrapper.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				// call upgrade agent.
				upgradeAgent();
			}
		});
	}

	/**
	 * Update agent implementation.
	 */
	public void upgradeAgent() {
		// getting a list of clusters that are in deployed state
		List<Cluster> clusters = clusterManager
				.getAllByPropertyValue(Constant.Keys.STATE,
						Constant.Cluster.State.DEPLOYED.toString());
		clusters.addAll(clusterManager.getAllByPropertyValue(
				Constant.Keys.STATE,
				Constant.Cluster.State.MAINTENANCE.toString()));

		if (clusters == null) {
			return;
		}
		// iterate over the clusters
		for (Cluster cluster : clusters) {
			try {
				// if cluster agent version and agent cluster version is
				// different
				if (!(agentBuildVersion.equals(cluster.getAgentVersion()))
						|| ((AgentUtils.nodeCountForAgentUpgrade(cluster) > 0))) {

					// Cluster configuration Object
					ClusterConfig clusterConfig = cluster.getClusterConfig();
					if (clusterConfig == null) {
						throw new AnkushException(
								"Could not get cluster configuration.");
					}
					// Incrementing operation id
					clusterConfig.incrementOperation();
					// setting cluster config to save logs in database
					LOGGER.setCluster(clusterConfig);
					// log message.
					LOGGER.info(MESSAGE_UPGRADING_AGENT);
					// Setting cluster state to maintenence
					cluster.setState(Constant.Cluster.State.MAINTENANCE
							.toString());
					cluster.setClusterConf(clusterConfig);
					clusterManager.save(cluster);

					// cluster nodes.
					Set<Node> nodes = cluster.getNodes();
					if (nodes == null) {
						cluster.setState(Constant.Cluster.State.DEPLOYED
								.toString());
						clusterManager.save(cluster);
						throw new AnkushException(
								"Could not get Cluster nodes.");
					}
					// iterate over the nodes to upgrade.
					for (Node node : nodes) {
						new AgentNodeUpgrader(node, clusterConfig)
								.upgradeAgent();
					}
					if (status(nodes)) {
						// set agent build version.
						LOGGER.info(MESSAGE_UPGRADING_AGENT + " done.");
						cluster.setAgentVersion(agentBuildVersion);
					}
					cluster.setState(Constant.Cluster.State.DEPLOYED.toString());
					clusterManager.save(cluster);
				}
			} catch (AnkushException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (Exception e) {
				LOGGER.error(COULD_NOT_UPGRADE_AGENT, e);
			} finally {
				cluster.setState(Constant.Cluster.State.DEPLOYED.toString());
				clusterManager.save(cluster);
			}

		}
	}

	private boolean status(Set<Node> nodes) {
		boolean status = true;
		for (Node node : nodes) {
			status = status && node.getNodeConfig().getStatus();
		}
		return status;
	}
}
