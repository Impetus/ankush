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

import javax.persistence.EntityNotFoundException;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentConf;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.DeployableConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.ganglia.GangliaConf;
import com.impetus.ankush.common.postprocessor.PostProcessorConf;
import com.impetus.ankush.common.preprocessor.PreprocessorConf;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.HostOperation;
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
	
	private ClusterConf newClusterConf;

	public Map<String, Object> redeploy(final Long clusterId,
			final ClusterConf clusterConf) {
		try {
			// Getting cluster object from database.
			Cluster dbCluster = clusterManager.get(clusterId);
			String state = dbCluster.getState();

			if (!state.equals(Constant.Cluster.State.DEPLOYED)
					&& !state.equals(Constant.Cluster.State.ERROR)
					&& !state.equals(Constant.Cluster.State.SERVER_CRASHED)) {
				addError("Cluster is in " + state
						+ " state, could not delete cluster.");
				return returnResult();
			}

			dbCluster.setState(Constant.Cluster.State.REMOVING);
			this.clusterConf = dbCluster.getClusterConf();
			this.clusterConf.setState(dbCluster.getState());
			this.clusterConf.setOperationId(logger.getNewOperationId(dbCluster
					.getId()));
			dbCluster.setClusterConf(this.clusterConf);
			dbCluster = clusterManager.save(dbCluster);

			// Getting clusteable object using the technology name.
			cluster = ObjectFactory.getClusterableInstanceById(dbCluster
					.getTechnology());

			// Get cluster configuration
			this.clusterConf = cluster.preUndeploy(dbCluster);

			if (this.clusterConf == null) {
				addError(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				logger.error(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				return returnResult();
			}
			this.clusterConf.setState(state);

			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					undeploy();
					// remove cluster record.
					clusterManager.remove(clusterId);
					// create cluster.
					create(cluster, clusterConf);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnResult();
	}

	/**
	 * Removes the nodes.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param clusterConf
	 *            the cluster conf
	 * @return the map
	 */
	public Map<String, Object> removeNodes(Long clusterId,
			ClusterConf clusterConf) {
		try {
			this.newClusterConf = clusterConf;
			// Getting cluster object from database.
			Cluster dbCluster = clusterManager.get(clusterId);

			if (!dbCluster.getState().equals(Constant.Cluster.State.DEPLOYED)) {
				addError("Cluster is in " + dbCluster.getState()
						+ " state, could not remove node.");
				return returnResult();
			}

			// Getting clusterable object using the technology name.
			cluster = ObjectFactory.getClusterableInstanceById(dbCluster
					.getTechnology());

			// Get cluster configuration
			this.clusterConf = cluster.getClusterConf(dbCluster);
			if (this.clusterConf == null) {
				addError(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				logger.error(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				return returnResult();
			}

			// getting node list to remove.
			this.nodeConfs = cluster.getNodes(clusterConf);
			if (this.nodeConfs == null) {
				addError(COULD_NOT_GET_NODE_LIST);
				logger.error(COULD_NOT_GET_NODE_LIST);
				return returnResult();
			}
			// setting cluster state as removing nodes
			this.clusterConf.setState(Constant.Cluster.State.REMOVING_NODES);
			
			// performing pre remove nodes opertation.
			if (!cluster.preRemoveNodes(this.nodeConfs, this.clusterConf)) {
				// adding error if failed.
				addError(cluster.getError());
				logger.error("Pre-processing task is failed."
						+ cluster.getError());
				return returnResult();
			}

			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					removeNodes();
				}
			});
		} catch (Exception e) {
			addError("Error in removing nodes, " + e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return returnResult();
	}

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

			HostOperation.setSysHostNameForNodes(this.nodeConfs,
					this.clusterConf);

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
	public Map<String, Object> create(Clusterable cluster,
			ClusterConf clusterConf) {
		
		// Set variables
		this.cluster = cluster;
		this.clusterConf = clusterConf;
		this.clusterConf.setState(Constant.Cluster.State.DEPLOYING);

		// Call validate cluster method.
		if (!validateCluster(clusterConf)) {
			logger.error("Cluster validation failed.");
			return returnResult();
		}
		
		//It is needed to be done after validateCluster,So moved below that check.
		HostOperation.setSysHostNameForNodes(clusterConf.getNodeConfs(),
						clusterConf);

		// Save cluster detail
		if (!cluster.saveClusterDetails(clusterConf)) {
			addError(cluster.getError());
			logger.error("Saving cluster failed." + cluster.getError());
			return returnResult();
		}
		logger.setCluster(clusterConf);

		AppStoreWrapper.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				// deploy the cluster
				try {
					deploy();
				} catch (Exception e) {
					addError(e.getMessage());
					logger.error("", e);
				}
			}
		});
		return returnResult();
	}

	/**
	 * Delete.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the map
	 */
	public Map<String, Object> delete(final long clusterId) {
		try {
			// Getting cluster object from database.
			Cluster dbCluster = clusterManager.get(clusterId);
			String state = dbCluster.getState();

			if (!state.equals(Constant.Cluster.State.DEPLOYED)
					&& !state.equals(Constant.Cluster.State.ERROR)
					&& !state.equals(Constant.Cluster.State.SERVER_CRASHED)) {
				addError("Cluster is in " + state
						+ " state, could not delete cluster.");
				return returnResult();
			}

			dbCluster.setState(Constant.Cluster.State.REMOVING);
			dbCluster.getClusterConf().setState(dbCluster.getState());
			dbCluster.getClusterConf().setOperationId(
					logger.getNewOperationId(dbCluster.getId()));
			dbCluster = clusterManager.save(dbCluster);

			// Getting clusteable object using the technology name.
			cluster = ObjectFactory.getClusterableInstanceById(dbCluster
					.getTechnology());

			// Get cluster configuration
			clusterConf = cluster.preUndeploy(dbCluster);

			if (clusterConf == null) {
				addError(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				logger.error(COULD_NOT_GET_CLUSTER_CONFIGURATION);
				return returnResult();
			}
			clusterConf.setState(state);

			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					undeploy();
					// remove cluster record.
					clusterManager.remove(clusterId);
					// remove the audit trail records.
					new ConfigurationManager().removeAuditTrail(clusterId);
				}
			});

		} catch (EntityNotFoundException e) {
			addError("Cluster is already removed from ankush.");
			logger.error("Cluster is already removed from ankush.", e);
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return returnResult();
	}

	/**
	 * Return result.
	 * 
	 * @return the map
	 */
	private Map<String, Object> returnResult() {
		if (errors.isEmpty()) {
			result.put("status", true);
		} else {
			result.put("status", false);
			result.put("error", errors);
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
	 * Deploy.
	 */
	private void deploy() {
		try {
			// Call pre-deploy steps
			if (!cluster.preDeploy(clusterConf)) {
				addError(cluster.getError());
				logger.error("Pre-processing task is failed."
						+ cluster.getError());
				clusterConf.setState(Constant.Cluster.State.ERROR);
				cluster.postDeploy(clusterConf);
				return;
			}

			Map<String, Configuration> componentConfig = cluster
					.createConfigs(clusterConf);

			// setting agent , ganglia and preprocessor confs.
			componentConfig.put(Constant.Component.Name.AGENT,
					ComponentConfigurator.getAgentConf(clusterConf));
			componentConfig.put(Constant.Component.Name.GANGLIA,
					ComponentConfigurator.getGangliaConf(clusterConf));
			componentConfig.put(Constant.Component.Name.PREPROCESSOR,
					ComponentConfigurator.getPreprocessorConf(clusterConf));
			componentConfig.put(Constant.Component.Name.POSTPROCESSOR,
					ComponentConfigurator.getPostProcessorConf(clusterConf));

			// iterate over component list
			for (String componentId : componentConfig.keySet()) {
				// create Deployer obj
				Deployable deployer = ObjectFactory
						.getInstanceById(componentId);
				// create DeployableConf(Deployer, Conf) bean class object
				DeployableConf deployableConf = new DeployableConf(deployer,
						componentConfig.get(componentId));
				// insert DeployableConf in PriorityQueue
				deployableQueue.add(deployableConf);
			}

			// Updating cluster, components and nodes information in database.
			if (!cluster.updateClusterDetails(clusterConf)) {
				clusterConf.setState(Constant.Cluster.State.ERROR);
				cluster.postDeploy(clusterConf);
				return;
			}

			// Step3 . Validate the cluster inputs
			if (!cluster.validate(clusterConf)) {
				// It should return the error code with below object.
				clusterConf.setState(Constant.Cluster.State.ERROR);
				cluster.postDeploy(clusterConf);
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
				Deployable deployer = deployableConf.getDeployer();
				// call deploy method on component
				boolean isDeployed = deployer.deploy(deployableConf
						.getConfiguration());

				if (isDeployed) {
					// call start method on component
					boolean isStarted = deployer.start(deployableConf
							.getConfiguration());
					// start serivces failed
					if (!isStarted) {
						clusterConf.setState(Constant.Cluster.State.ERROR);
						break;
					}
				} else {
					// component deployment failed
					clusterConf.setState(Constant.Cluster.State.ERROR);
					break;
				}
				// replace by logger
				logger.debug(" deployableConf.configuration === "
						+ deployableConf.getConfiguration());
			}

			/* Rollback whole cluster if any component deployment failed */
			if (clusterConf.getState().equals(Constant.Cluster.State.ERROR)) {
				while (deployedStack.size() > 0) {
					// pop component from stack
					DeployableConf deployableConf = deployedStack.pop();
					Deployable deployer = deployableConf.getDeployer();
					// call stop
					deployer.stop(deployableConf.getConfiguration());
					// call undeploy
					deployer.undeploy(deployableConf.getConfiguration());
				}
			} else {
				clusterConf.setState(Constant.Cluster.State.DEPLOYED);
			}

			cluster.postDeploy(clusterConf);

		} catch (Exception e) {
			addError(cluster.getError());
			logger.error("Exception while deployment, please view logs for details.");
			clusterConf.setState(Constant.Cluster.State.ERROR);
			cluster.postDeploy(clusterConf);
			return;
		}
	}

	/**
	 * Undeploy.
	 */
	private void undeploy() {

		try {
			// If cluster state is error then no need to undeply the components.
			if (!this.clusterConf.getState().equals(
					Constant.Cluster.State.ERROR)) {

				// Creating undeploy configs.
				Map<String, Configuration> componentConfig = cluster
						.createConfigs(this.clusterConf);

				// setting agent , ganglia and preprocessor confs.
				componentConfig.put(Constant.Component.Name.AGENT,
						ComponentConfigurator.getAgentConf(clusterConf));
				componentConfig.put(Constant.Component.Name.GANGLIA,
						ComponentConfigurator.getGangliaConf(clusterConf));
				componentConfig.put(Constant.Component.Name.PREPROCESSOR,
						ComponentConfigurator.getPreprocessorConf(clusterConf));
				componentConfig
						.put(Constant.Component.Name.POSTPROCESSOR,
								ComponentConfigurator
										.getPostProcessorConf(clusterConf));

				// iterate over component list
				for (String componentId : componentConfig.keySet()) {
					// create Deployer obj
					Deployable deployer = ObjectFactory
							.getInstanceById(componentId);
					// create DeployableConf(Deployer, Conf) bean class object
					DeployableConf deployableConf = new DeployableConf(
							deployer, componentConfig.get(componentId));
					// insert DeployableConf in PriorityQueue
					unDeployableQueue.add(deployableConf);
				}

				// iterate over deployable priority queue
				while (unDeployableQueue.size() > 0) {
					// pick component object from priority queue
					DeployableConf deployableConf = unDeployableQueue.remove();
					// push component into inprogress stack
					Deployable deployer = deployableConf.getDeployer();
					// call deploy method on component
					deployer.stop(deployableConf.getConfiguration());
					deployer.undeploy(deployableConf.getConfiguration());
				}
			}
			cluster.postUndeploy(clusterConf);
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

		// setting agent configuration.
		AgentConf agentConf = ComponentConfigurator.getAgentConf(clusterConf);
		agentConf.setNewNodes(this.nodeConfs);

		// setting ganglia configuration.
		componentConfig.put(Constant.Component.Name.AGENT, agentConf);
		GangliaConf gangliaConf = ComponentConfigurator
				.getGangliaConf(clusterConf);
		gangliaConf.setNewNodes(this.nodeConfs);
		componentConfig.put(Constant.Component.Name.GANGLIA, gangliaConf);

		// setting preprocessor configuration.
		PreprocessorConf preprocessConf = ComponentConfigurator
				.getPreprocessorConf(clusterConf);
		preprocessConf.setNewNodes(this.nodeConfs);
		componentConfig.put(Constant.Component.Name.PREPROCESSOR,
				preprocessConf);

		PostProcessorConf postProcessorConf = ComponentConfigurator
				.getPostProcessorConf(clusterConf);
		postProcessorConf.setNewNodes(this.nodeConfs);
		componentConfig.put(Constant.Component.Name.POSTPROCESSOR,
				postProcessorConf);

		// iterate over component list
		for (String componentId : componentConfig.keySet()) {
			// create Deployer obj
			Deployable deployer = ObjectFactory.getInstanceById(componentId);
			// create DeployableConf(Deployer, Conf) bean class object
			DeployableConf deployableConf = new DeployableConf(deployer,
					componentConfig.get(componentId));
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
			GenericConfiguration configuration = (GenericConfiguration) deployableConf
					.getConfiguration();
			// push component into inprogress stack
			deployedStack.push(deployableConf);
			Deployable deployer = deployableConf.getDeployer();

			// call add node method on component
			boolean isAdded = deployer.addNodes(configuration.getNewNodes(),
					configuration);

			if (isAdded) {
				// call start method on component
				boolean isStarted = deployer.startServices(
						configuration.getNewNodes(), configuration);
				// start services failed
				if (!isStarted) {
					this.clusterConf.setState(Constant.Cluster.State.ERROR);
					break;
				}
			} else {
				// component deployment failed
				this.clusterConf.setState(Constant.Cluster.State.ERROR);
				break;
			}
			// replace by logger
			logger.debug(" deployableConf.configuration === "
					+ deployableConf.getConfiguration());
		}

		/* Rollback whole cluster if any component deployment failed */
		if (clusterConf.getState().equals(Constant.Cluster.State.ERROR)) {
			while (deployedStack.size() > 0) {
				// pop component from stack
				DeployableConf deployableConf = deployedStack.pop();
				Deployable deployer = deployableConf.getDeployer();
				GenericConfiguration configuration = (GenericConfiguration) deployableConf
						.getConfiguration();
				// call stop
				deployer.stopServices(configuration.getNewNodes(),
						configuration);
				// call remove
				deployer.removeNodes(configuration.getNewNodes(), configuration);
			}

			// setting node state as error for failures.
			for (NodeConf nodeConf : this.nodeConfs) {
				nodeConf.setNodeState(Constant.Node.State.ERROR);
			}
		}
		cluster.postAddNodes(this.nodeConfs, this.clusterConf);
	}

	/**
	 * Removes the nodes.
	 */
	private void removeNodes() {
		try {
			// If cluster state is error then no need to undeploy the
			// components.
			if (!this.clusterConf.getState().equals(
					Constant.Cluster.State.ERROR)) {
				this.clusterConf
						.setState(Constant.Cluster.State.REMOVING_NODES);
				// Creating undeploy configs.
				Map<String, Configuration> componentConfig = cluster
						.createConfigs(this.newClusterConf, this.clusterConf);

				// setting agent configuration.
				AgentConf agentConf = ComponentConfigurator
						.getAgentConf(clusterConf);
				agentConf.setNewNodes(this.nodeConfs);

				// setting ganglia configuration.
				componentConfig.put(Constant.Component.Name.AGENT, agentConf);
				GangliaConf gangliaConf = ComponentConfigurator
						.getGangliaConf(clusterConf);
				gangliaConf.setNewNodes(this.nodeConfs);

				// setting preprocessor configuration.
				componentConfig.put(Constant.Component.Name.GANGLIA,
						gangliaConf);
				PreprocessorConf preprocessConf = ComponentConfigurator
						.getPreprocessorConf(clusterConf);
				preprocessConf.setNewNodes(this.nodeConfs);
				componentConfig.put(Constant.Component.Name.PREPROCESSOR,
						preprocessConf);

				PostProcessorConf postProcessorConf = ComponentConfigurator
						.getPostProcessorConf(clusterConf);
				postProcessorConf.setNewNodes(this.nodeConfs);
				componentConfig.put(Constant.Component.Name.POSTPROCESSOR,
						postProcessorConf);

				// iterate over component list
				for (String componentId : componentConfig.keySet()) {
					// create Deployer obj
					Deployable deployer = ObjectFactory
							.getInstanceById(componentId);
					// create DeployableConf(Deployer, Conf) bean class object
					DeployableConf deployableConf = new DeployableConf(
							deployer, componentConfig.get(componentId));
					// insert DeployableConf in PriorityQueue
					unDeployableQueue.add(deployableConf);
				}

				// iterate over deployable priority queue
				while (unDeployableQueue.size() > 0) {
					// pick component object from priority queue
					DeployableConf deployableConf = unDeployableQueue.remove();
					// push component into inprogress stack
					Deployable deployer = deployableConf.getDeployer();

					GenericConfiguration configuration = (GenericConfiguration) deployableConf
							.getConfiguration();

					// call stop services on nodes
					deployer.stopServices(configuration.getNewNodes(),
							configuration);

					// call remove node method on component
					boolean isRemoved = deployer.removeNodes(
							configuration.getNewNodes(), configuration);

					if (!isRemoved) {
						// component deployment failed
						clusterConf.setState(Constant.Cluster.State.ERROR);
					}
				}
			}
			cluster.postRemoveNodes(nodeConfs, clusterConf);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Method to validate cluster name and ip address existance.
	 * 
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean validateCluster(ClusterConf conf) {
		String error = null;
		try {
			if (clusterManager.getByPropertyValueGuarded("name",
					conf.getClusterName()) != null) {
				error = "Cluster '" + conf.getClusterName()
						+ "'  is already exist.";
				addError(error);
				return false;
			}
		} catch (Exception e) {
			error = e.getMessage();
			if (error == null) {
				error = e.getCause().getMessage();
			}
			addError(error);
			return false;
		}

		for (NodeConf nodeConf : conf.getNodeConfs()) {
			// Check if node is in use
			try {
				if (nodeManager.getByPropertyValueGuarded("publicIp",
						nodeConf.getPublicIp()) != null) {
					if (error == null) {
						error = "Node '" + nodeConf.getPublicIp()
								+ "' is already in use.";
					} else {
						error += " Node '" + nodeConf.getPublicIp()
								+ "' is already in use.";
					}
				}
			} catch (Exception e) {
				error = e.getMessage();
			}
		}
		if (error != null) {
			addError(error);
			return false;
		}
		return true;
	}
	
	/** Logger object. */
	private AnkushLogger logger = new AnkushLogger(ClusterManager.class);

	/** Generic cluster master. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** Generic Node Manager. */
	private static GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

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
