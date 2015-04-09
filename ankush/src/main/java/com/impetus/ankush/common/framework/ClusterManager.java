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
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.DeployableConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * The Class ClusterManager.
 * 
 * @author nikunj
 */
public class ClusterManager {

	/** The Constant COULD_NOT_GET_NODE_LIST. */
	private static final String COULD_NOT_GET_NODE_LIST = "Could not get Node list.";

	/** The Constant COULD_NOT_GET_CLUSTER_CONFIGURATION. */
	private static final String COULD_NOT_GET_CLUSTER_CONFIGURATION = "Could not get cluster configuration.";

	/** New Cluster conf object. **/
	private ClusterConf newClusterConf;

	/**
	 * Adds the nodes.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param clusterConf
	 *            the cluster conf
	 * @return the map
	 */
	public Map<String, Object> addNodes(Long clusterId, ClusterConf clusterConf) {

		try {
			this.newClusterConf = clusterConf;
			// Getting cluster object from database.
			Cluster dbCluster = clusterManager.get(clusterId);

			if (!dbCluster.getState().equals(Constant.Cluster.State.DEPLOYED)) {
				addError("Cluster is in " + dbCluster.getState()
						+ " state, could not add node.");
				return returnResult();
			}

			// Getting clusteable object using the technology name.
			cluster = ObjectFactory.getClusterableInstanceById(dbCluster
					.getTechnology());

			// Get cluster configuration
			this.clusterConf = cluster.getClusterConf(dbCluster);
			if (this.clusterConf == null) {
				addError(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				logger.error(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				return returnResult();
			}

			// setting add node state
			this.clusterConf.setState(Constant.Cluster.State.ADDING_NODES);
			this.clusterConf.setIpPattern(clusterConf.getIpPattern());
			this.clusterConf.setPatternFile(clusterConf.getPatternFile());

			// getting node list to add.
			this.nodeConfs = cluster.getNodes(clusterConf);
			if (this.nodeConfs == null || this.nodeConfs.size() == 0) {
				addError(COULD_NOT_GET_NODE_LIST);
				logger.error(COULD_NOT_GET_NODE_LIST);
				return returnResult();
			}

			// performing pre add nodes opertation.
			if (!cluster.preAddNodes(this.nodeConfs, this.clusterConf)) {
				// adding error if failed.
				addError(cluster.getError());
				logger.error("Pre-processing task is failed."
						+ cluster.getError());
				return returnResult();
			}

			// HostOperation.setSysHostNameForNodes(this.nodeConfs,
			// this.clusterConf);

			// Save cluster detail
			if (!cluster.saveNodeDetails(this.nodeConfs, this.clusterConf)) {
				// adding error if failed.
				addError(cluster.getError());
				logger.error("Saving cluster failed." + cluster.getError());
				return returnResult();
			}

			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					addNodes();
				}
			});

		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return returnResult();
	}

	/**
	 * Creates the.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param clusterConf
	 *            the cluster conf
	 * @return the map
	 */
	public Map<String, Object> deployPatch(long clusterId, String componentId,
			Map map) {

		// serverPatchTarballLocation
		String serverPatchTarballLocation = (String) map
				.get("serverPatchTarballLocation");
		// Getting cluster object from database.
		Cluster dbCluster = clusterManager.get(clusterId);

		if (!dbCluster.getState().equals(Constant.Cluster.State.DEPLOYED)) {
			addError("Cluster is in " + dbCluster.getState()
					+ " state, could not apply patch.");
			return returnResult();
		}

		// Set variables
		this.clusterConf = dbCluster.getClusterConf();

		// create Deployer obj
		Deployable deployer = ObjectFactory.getInstanceById(componentId);

		GenericConfiguration genericConfiguration = clusterConf
				.getClusterComponents().get(componentId);
		genericConfiguration.setServerPatchTarballLocation(AppStoreWrapper
				.getServerPatchesRepoPath() + serverPatchTarballLocation);
		// // create DeployableConf(Deployer, Conf) bean class object
		DeployableConf deployableConf = new DeployableConf(deployer,
				clusterConf.getClusterComponents().get(componentId),
				componentId);
		deployer.deployPatch(clusterConf.getClusterComponents()
				.get(componentId));

		return new HashMap();
	}

	/**
	 * Delete.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the map
	 */
	public void delete(final long clusterId) {

		AppStoreWrapper.getExecutor().execute(new Runnable() {

			@Override
			public void run() {
				prepareUndeployableQueue(clusterId);

				undeploy();
				// remove cluster record.
				clusterManager.remove(clusterId);
				// remove the audit trail records.
				new ConfigurationManager().removeAuditTrail(clusterId);
			}
		});

	}

	/**
	 * Return result.
	 * 
	 * @return the map
	 */
	private Map<String, Object> returnResult() {
		if (errors.isEmpty()) {
			result.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, true);
		} else {
			result.put(com.impetus.ankush2.constant.Constant.Keys.STATUS, false);
			result.put(com.impetus.ankush2.constant.Constant.Keys.ERROR, errors);
		}
		return result;
	}

	/**
	 * Adds the error.
	 * 
	 * @param error
	 *            the error
	 */
	private void addError(String error) {
		this.errors.add(error);
	}

	/**
	 * Undeploy.
	 */
	private void undeploy() {

		// iterate over deployable priority queue
		while (unDeployableQueue.size() > 0) {
			// pop component from stack and stop and undeploy it.
			ClusterManagerUtils.undeployComponent(unDeployableQueue.remove());
		}
	}

	/**
	 * Prepare undeployable queue.
	 * 
	 * @param clusterId
	 *            the cluster id
	 */
	public void prepareUndeployableQueue(Long clusterId) {
		System.out.println("this.clusterConf : " + this.clusterConf);
		try {
			// If cluster state is error then no need to undeply the components.
			if (!this.clusterConf.getState().equals(
					Constant.Cluster.State.ERROR)) {

				// Creating undeploy configs.
				Map<String, Configuration> componentConfig = cluster
						.createConfigs(this.clusterConf);

				// setting agent , ganglia and preprocessor confs.

				// If CLuster is Registerable, then add CommonComponents using
				// ClusterConf object,
				// else recreate the common components
				if (clusterConf.isRegisterableCluster()) {
					componentConfig.putAll(clusterConf.getCommonComponents());
				} else {
					componentConfig.putAll(ClusterManagerUtils
							.getCommonComponents(clusterConf));
				}

				// iterate over component list
				for (String componentId : componentConfig.keySet()) {
					// create Deployer obj
					Deployable deployer = ObjectFactory
							.getInstanceById(componentId);
					// create DeployableConf(Deployer, Conf) bean class object
					DeployableConf deployableConf = new DeployableConf(
							deployer, componentConfig.get(componentId),
							componentId);
					// insert DeployableConf in PriorityQueue
					unDeployableQueue.add(deployableConf);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * Adds the nodes.
	 */
	private void addNodes() {
		// Creating add node configs.
		Map<String, Configuration> componentConfig = cluster.createConfigs(
				this.newClusterConf, this.clusterConf);

		// Adding Agent, Ganglia, PreProcessor and PostProcessor
		// Components.
		componentConfig.putAll(ClusterManagerUtils.getCommonComponents(
				clusterConf, this.nodeConfs));

		// iterate over component list
		for (String componentId : componentConfig.keySet()) {
			// create Deployer obj
			Deployable deployer = ObjectFactory.getInstanceById(componentId);
			// create DeployableConf(Deployer, Conf) bean class object
			DeployableConf deployableConf = new DeployableConf(deployer,
					componentConfig.get(componentId), componentId);
			// insert DeployableConf in PriorityQueue
			deployableQueue.add(deployableConf);
		}

		// Step3 . Validate the cluster inputs
		if (!cluster.validate(this.clusterConf, this.nodeConfs)) {
			// It should return the error code with below object.
			clusterConf.setState(Constant.Cluster.State.ERROR);
			cluster.postAddNodes(nodeConfs, clusterConf);
			return;
		}

		// stack for tracking inprogress components
		Stack<DeployableConf> deployedStack = new Stack<DeployableConf>();
		// deployemnt status flag

		// iterate over deployable priority queue
		while (deployableQueue.size() > 0) {
			// pick component object from priority queue
			DeployableConf deployableConf = deployableQueue.remove();
			// push component into inprogress stack
			deployedStack.push(deployableConf);

			// Adding and starting the services on nodes.
			boolean status = ClusterManagerUtils
					.addNodesToComponent(deployableConf);

			// if node addition fails then break the loop.
			if (!status) {
				break;
			}
		}

		/* Rollback whole cluster if any component deployment failed */
		if (clusterConf.getState().equals(Constant.Cluster.State.ERROR)) {
			while (deployedStack.size() > 0) {
				// pop component from stack
				DeployableConf deployableConf = deployedStack.pop();
				// remove the node deployment from the node.
				ClusterManagerUtils.removeNodesFromComponent(deployableConf);
			}

			// setting node state as error for failures.
			for (NodeConf nodeConf : this.nodeConfs) {
				nodeConf.setNodeState(Constant.Node.State.ERROR);
			}
		}
		cluster.postAddNodes(this.nodeConfs, this.clusterConf);
	}

	/** Logger object. */
	private AnkushLogger logger = new AnkushLogger(ClusterManager.class);

	/** Generic cluster master. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	// Priority Queue for components
	/** The deployable queue. */
	private PriorityQueue<DeployableConf> deployableQueue = new PriorityQueue<DeployableConf>(
			10, new ComponentComparator());

	// Priority Queue for components
	/** The un deployable queue. */
	private PriorityQueue<DeployableConf> unDeployableQueue = new PriorityQueue<DeployableConf>(
			10, new ComponentComparator(true));

	/** Cluster result. */
	Map<String, Object> result = new HashMap<String, Object>();

	/** Error list. */
	private List<String> errors = new ArrayList<String>();

	/** The node confs. */
	private List<NodeConf> nodeConfs = new ArrayList<NodeConf>();

	/** Clusterable object. */
	private Clusterable cluster = null;

	/** Cluster conf object. */
	private ClusterConf clusterConf = null;

}
