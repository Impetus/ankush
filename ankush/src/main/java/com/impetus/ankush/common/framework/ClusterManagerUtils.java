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
package com.impetus.ankush.common.framework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentConf;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.DeployableConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.framework.config.RegisterClusterConf;
import com.impetus.ankush.common.ganglia.GangliaConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.validator.ValidationUtility;
import com.impetus.ankush2.constant.Constant.Component;

/**
 * @author hokam
 * 
 */
public class ClusterManagerUtils {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			ClusterManagerUtils.class);

	/**
	 * Method to set node state as provided.
	 * 
	 * @param state
	 * @param clusterConf
	 */
	public static void setNodesStateAs(String state, List<NodeConf> nodes) {
		// iterating over the nodes to set the state.
		for (NodeConf node : nodes) {
			// updating state.
			node.setNodeState(state);
		}
	}

	/**
	 * Method to add nodes to component.
	 * 
	 * @param deployableConf
	 */
	public static boolean addNodesToComponent(DeployableConf deployableConf) {
		GenericConfiguration configuration = (GenericConfiguration) deployableConf
				.getConfiguration();
		// Getting deployer object.
		Deployable deployer = deployableConf.getDeployer();

		// cluster conf
		ClusterConf clusterConf = configuration.getClusterConf();

		// setting state as Adding node.
		configuration.setState(Constant.Component.DeploymentState.ADDING_NODE);

		// updating the component state.
		// addUpdateComponent(clusterConf.getClusterId(),
		// deployableConf.getComponentName(), configuration);

		// call add node method on component
		boolean status = deployer.addNodes(configuration.getNewNodes(),
				configuration);

		if (status) {
			// call start method on component
			status = deployer.startServices(configuration.getNewNodes(),
					configuration);
			// start services failed
			if (!status) {
				// setting cluster conf state as error.
				clusterConf.setState(Constant.Cluster.State.ERROR);
				// setting component state as failed.
				configuration
						.setState(Constant.Component.DeploymentState.ADD_NODE_FAILED);
				status = false;
			} else {
				// setting component state as completed.
				configuration
						.setState(Constant.Component.DeploymentState.COMPLETED);
			}
		} else {
			// component deployment failed
			clusterConf.setState(Constant.Cluster.State.ERROR);
			// setting component state as failed.
			configuration
					.setState(Constant.Component.DeploymentState.ADD_NODE_FAILED);
			status = false;
		}

		// updating the component state.
		/*
		 * addUpdateComponent(clusterConf.getClusterId(),
		 * deployableConf.getComponentName(), configuration);
		 */
		return status;
	}

	/**
	 * Method to remove nodes from the component.
	 * 
	 * @param deployableConf
	 */
	public static void removeNodesFromComponent(DeployableConf deployableConf) {
		// Getting deployer from deployable conf.
		Deployable deployer = deployableConf.getDeployer();

		// configuration class.
		GenericConfiguration configuration = (GenericConfiguration) deployableConf
				.getConfiguration();

		// call stop services on nodes
		deployer.stopServices(configuration.getNewNodes(), configuration);

		// call remove node method on component
		deployer.removeNodes(configuration.getNewNodes(), configuration);
	}

	/**
	 * Stop and undeploy the component.
	 * 
	 * @param deployableConf
	 */
	public static void undeployComponent(DeployableConf deployableConf) {
		// Getting deployer from deployable conf.
		Deployable deployer = deployableConf.getDeployer();
		// configuration class.
		GenericConfiguration configuration = (GenericConfiguration) deployableConf
				.getConfiguration();

		// call stop
		deployer.stop(deployableConf.getConfiguration());
		// call undeploy
		deployer.undeploy(deployableConf.getConfiguration());
	}

	/**
	 * Deploy and Start the component.
	 * 
	 * @param deployableConf
	 * @return
	 */
	public static boolean deployComponent(DeployableConf deployableConf) {
		// deployer object.
		Deployable deployer = deployableConf.getDeployer();
		// generic configuration object.
		GenericConfiguration configuration = (GenericConfiguration) deployableConf
				.getConfiguration();

		// cluster conf
		ClusterConf clusterConf = configuration.getClusterConf();

		// if current state is in progress then remove and stop component.
		if (configuration.isInProgress()) {
			ClusterManagerUtils.undeployComponent(deployableConf);
		} else if (configuration.isCompleted()) {
			return true;
		}

		// setting inprogress state.
		configuration.setState(Constant.Component.DeploymentState.INPROGRESS);

		// addUpdateComponent(clusterConf.getClusterId(),
		// deployableConf.getComponentName(), configuration);

		// call deploy method on component

		boolean isDeployed = true;

		if (configuration.isDeployComponentFlag()) {
			isDeployed = deployer.deploy(configuration);
		} else {
			isDeployed = deployer.registerComponent(configuration);
		}

		// if (isDeployed) {
		// isDeployed = deployer.registerComponent(configuration);
		// }

		boolean status = true;

		if (isDeployed) {

			if (!configuration.isDeployComponentFlag()) {
				configuration
						.setState(Constant.Component.DeploymentState.COMPLETED);
			} else {
				// call start method on component
				boolean isStarted = deployer.start(configuration);
				// start serivces failed
				if (!isStarted) {
					clusterConf.setState(Constant.Cluster.State.ERROR);
					// setting failed state.
					configuration
							.setState(Constant.Component.DeploymentState.FAILED);
					status = false;
				} else {
					// setting completed state.
					configuration
							.setState(Constant.Component.DeploymentState.COMPLETED);
				}
			}
		} else {
			// component deployment failed
			clusterConf.setState(Constant.Cluster.State.ERROR);
			// setting failed state.
			configuration.setState(Constant.Component.DeploymentState.FAILED);
			status = false;
		}
		// addUpdateComponent(clusterConf.getClusterId(),
		// deployableConf.getComponentName(), configuration);
		return status;
	}

	/**
	 * Method to get Agent, Ganglia, Preprocessor and PostProcessor component
	 * map.
	 * 
	 * @param clusterConf
	 * @return
	 */
	public static Map<String, GenericConfiguration> getCommonComponents(
			ClusterConf clusterConf) {
		// return components.
		return getCommonComponents(clusterConf, null);
	}

	public static boolean updateGangliaNodesToClusterConf(
			ClusterConf clusterConf) {
		try {
			GangliaConf gConf = (GangliaConf) clusterConf
					.getClusterComponents()
					.get(Component.Name.GANGLIA);
			if (gConf == null) {
				return false;
			}
			gConf.setGmondNodes(new HashSet<NodeConf>(clusterConf
					.getNodeConfs()));
			clusterConf.setGangliaMaster(gConf.getGmetadNode());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Method to get Agent, Ganglia, Preprocessor and PostProcessor component
	 * map.
	 * 
	 * @param clusterConf
	 * @return
	 * @throws Exception
	 */
	public static Map<String, GenericConfiguration> getCommonComponents(
			RegisterClusterConf regClusterConf, ClusterConf clusterConf) {
		try {
			// Components object.
			Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();

			// Getting agent conf.
			AgentConf agentConf = ComponentConfigurator
					.getAgentConf(clusterConf);
			// Getting GangliaConf
			// GangliaConf gangliaConf = ComponentConfigurator
			// .getGangliaConf(clusterConf);

			// Adding Agent Configuration
			components.put(Component.Name.AGENT, agentConf);
			// Adding Ganglia Configuration.
			// components.put(Constant.Component.Name.GANGLIA, gangliaConf);
			// Adding Preprocessor Configuration.

			return components;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Method to get Agent, Ganglia, Preprocessor and PostProcessor component
	 * map.
	 * 
	 * @param clusterConf
	 * @return
	 */
	public static Map<String, GenericConfiguration> getCommonComponents(
			ClusterConf clusterConf, List<NodeConf> newNodes) {
		// Components object.
		Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();
		// Getting agent conf.
		AgentConf agentConf = ComponentConfigurator.getAgentConf(clusterConf);
		// Getting ganglia conf.
		GangliaConf gangliaConf = ComponentConfigurator
				.getGangliaConf(clusterConf);

		// if it called for Add Nodes operation then add the new Nodes in common
		// components.
		if (newNodes != null) {
			agentConf.setState(Constant.Component.DeploymentState.COMPLETED);
			agentConf.setNewNodes(newNodes);
			gangliaConf.setState(Constant.Component.DeploymentState.COMPLETED);
			gangliaConf.setNewNodes(newNodes);
		}
		// Adding Agent Configuration
		components.put(Component.Name.AGENT, agentConf);
		// Adding Ganglia Configuration.
		components.put(Component.Name.GANGLIA, gangliaConf);
		// return components.
		return components;
	}

	/**
	 * Validate node.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param nodeIp
	 *            the node ip
	 * @param componentSet
	 *            the component set
	 * @return true, if successful
	 */
	public static boolean validateNode(ClusterConf clusterConf, String nodeIp,
			Set<String> componentSet) {

		SSHExec connection = null;
		boolean returnStatus = true;
		logger.setCluster(clusterConf);

		try {
			// connect to node
			connection = SSHUtils.connectToNode(nodeIp,
					clusterConf.getUsername(), clusterConf.getPassword(),
					clusterConf.getPrivateKey());
			if (connection != null) {
				logger.info(nodeIp, "Validating java...");
				// get javaBinPath
				String javaBinPath = clusterConf.getJavaConf().getJavaBinPath();
				// validate java on node
				returnStatus = ValidationUtility.isFileExists(connection,
						javaBinPath).isStatus();
				if (!returnStatus) {
					logger.error(nodeIp, "Couldn't validate java on this node.");
				}

				// validate for all other components on node
				for (String component : componentSet) {

					GenericConfiguration componnetConfig = clusterConf
							.getClusterComponents().get(component);

					// create Deployer obj
					Deployable deployer = ObjectFactory
							.getInstanceById(component);

					// perform component specific validation
					if (!deployer.validateComponent(nodeIp, connection,
							componnetConfig)) {
						returnStatus = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnStatus = false;
		} finally {
			// disconncet to node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return returnStatus;
	}

	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            List<NodeConf>
	 * @return boolean
	 */
	public static boolean status(List<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = nodeConf.getStatus() && status;
		}
		return status;
	}

	/**
	 * Unregister component.
	 * 
	 * @param deployableConf
	 *            the deployable conf
	 */
	public static void unregisterComponent(DeployableConf deployableConf) {
		// Getting deployer from deployable conf.
		Deployable deployer = deployableConf.getDeployer();
		// configuration class.
		GenericConfiguration configuration = (GenericConfiguration) deployableConf
				.getConfiguration();
		// call unregister
		deployer.unregisterComponent(deployableConf.getConfiguration());

	}

	public static boolean updateClusterConfInDB(ClusterConf conf) {

		/** Generic cluster master. */
		GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
				.getManager(Constant.Manager.CLUSTER, Cluster.class);

		// Create and save new cluster
		Cluster cluster = null;

		try {
			cluster = clusterManager.get(conf.getClusterId());
		} catch (Exception e) {
			return false;
		}
		if (cluster == null) {
			return false;
		}

		cluster.setState(conf.getState());
		cluster.setClusterConf(conf);

		try {
			cluster = clusterManager.save(cluster);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Gets the nodes technology map.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return the nodes technology map
	 */
	public static Map<String, Set<String>> getNodesComponentMap(
			ClusterConf clusterConf) {

		Map<String, Set<String>> nodeComponentMap = new HashMap<String, Set<String>>();

		// iterate over the components of the cluster
		for (String componentName : clusterConf.getClusterComponents().keySet()) {
			// get component Conf
			GenericConfiguration componentConfig = clusterConf
					.getClusterComponents().get(componentName);
			Set<NodeConf> compNodes = componentConfig.getCompNodes();
			for (NodeConf nc : compNodes) {
				if (!nodeComponentMap.containsKey(nc.getPublicIp())) {
					nodeComponentMap.put(nc.getPublicIp(),
							new HashSet<String>());
				}
				nodeComponentMap.get(nc.getPublicIp()).add(componentName);
			}
		}
		return nodeComponentMap;
	}

}
