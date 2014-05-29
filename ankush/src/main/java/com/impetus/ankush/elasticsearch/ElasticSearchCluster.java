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
package com.impetus.ankush.elasticsearch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.AlertsConf;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.dependency.DependencyConf;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.framework.Clusterable;
import com.impetus.ankush.common.framework.ComponentConfigurator;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.JavaConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * The Class ElasticSearchCluster.
 */
public class ElasticSearchCluster implements Clusterable{
	
	/** The logger. */
	AnkushLogger logger = new AnkushLogger(ElasticSearchCluster.class);
	
	/** The cluster manager. */
	static private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);
	
	/** The node manager. */
	static private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);
	
	/** The error. */
	private String error = null;
	

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#validate(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean validate(ClusterConf clusterConf) {
		ElasticSearchValidator validator = new ElasticSearchValidator();
		return validator.validate((ElasticSearchClusterConf)clusterConf);
		
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#validate(com.impetus.ankush.common.framework.config.ClusterConf, java.util.List)
	 */
	@Override
	public boolean validate(ClusterConf clusterConf, List<NodeConf> nodeConfs) {
		ElasticSearchValidator validator = new ElasticSearchValidator(); 
		return validator.validate(nodeConfs,clusterConf);

	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#saveClusterDetails(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean saveClusterDetails(ClusterConf clusterConf) {
		logger.info("Saving ElasticSearch cluster and node informations...");

		// Casting ClusterConf to ElasticSearchClusterConf
		ElasticSearchClusterConf esClusterConf = (ElasticSearchClusterConf) clusterConf;

		// Setting technology to ElasticSearch
		esClusterConf.setTechnology(Constant.Technology.ELASTICSEARCH);

		return saveClusterConfig(esClusterConf);
	}
	
	/**
	 * Save cluster config.
	 *
	 * @param esClusterConf the es cluster conf
	 * @return true, if successful
	 */
	private boolean saveClusterConfig(ElasticSearchClusterConf esClusterConf) {
		try {
			AlertsConf alertsConf = new AlertsConf();
			// Create and save new cluster
			Cluster cluster = new Cluster();
			cluster.setName(esClusterConf.getClusterName());
			cluster.setTechnology(esClusterConf.getTechnology());
			cluster.setState(esClusterConf.getState());
			cluster.setEnvironment(esClusterConf.getEnvironment());
			cluster.setClusterConf(esClusterConf);
			cluster.setCreatedAt(new Date());
			cluster.setUser(esClusterConf.getCurrentUser());
			cluster.setAlertConf(alertsConf);

			// save to database
			cluster = clusterManager.save(cluster);

			// Set cluster and operation id
			esClusterConf.setClusterId(cluster.getId());
			esClusterConf.setOperationId(logger.getNewOperationId(cluster
					.getId()));
			logger.setClusterId(esClusterConf.getClusterId());
			logger.setOperationId(esClusterConf.getOperationId());
			logger.setClusterName(cluster.getName());

			// Create and save nodes
			List<NodeConf> nodeConfs = new ArrayList<NodeConf>(
					esClusterConf.getNodeConfs());

			saveOrUpdateNodeDetails(nodeConfs, esClusterConf);
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
	
	/**
	 * Save or update node details.
	 *
	 * @param nodeConfs the node confs
	 * @param clusterConf the cluster conf
	 * @return true, if successful
	 */
	private boolean saveOrUpdateNodeDetails(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		try {
			// Create and save nodes
			for (NodeConf esNode : nodeConfs) {
				Node node = nodeManager.getByPropertyValueGuarded("publicIp",
						esNode.getPublicIp());

				// if null create empty object.
				if (node == null) {
					node = new Node();
					node.setCreatedAt(new Date());
				}

				// setting node state as deploying if node state is null else
				// same.
				if (esNode.getNodeState() == null) {
					node.setState(Constant.Node.State.DEPLOYING);
				} else {
					node.setState(esNode.getNodeState());
				}
				String type = Constant.Technology.ELASTICSEARCH;
				esNode.setType(type);
				// Setting node details
				node.setClusterId(clusterConf.getClusterId());
				node.setPrivateIp(esNode.getPrivateIp());
				node.setPublicIp(esNode.getPublicIp());
				node.setNodeConf(esNode);
				node = nodeManager.save(node);

				// Setting Node Id
				esNode.setId(node.getId());
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#preDeploy(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preDeploy(ClusterConf clusterConf) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#createConfigs(com.impetus.ankush.common.framework.config.ClusterConf, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public Map<String, Configuration> createConfigs(ClusterConf newClusterConf,
			ClusterConf clusterConf) {
		Map<String, Configuration> components = new HashMap<String, Configuration>();
		try {
			logger.info("Creating configuration for component deployments...");
			ElasticSearchClusterConf esClusterConf = (ElasticSearchClusterConf) clusterConf;
			//adding elasticSearch component conf
			ElasticSearchConf esConf = esClusterConf.getEsConf();
			if (esClusterConf.isRackEnabled()) {
				esConf.setRackEnabled(true);
			}
			if (clusterConf.getState().equals(
					Constant.Cluster.State.ADDING_NODES)
					|| clusterConf.getState().equals(
							Constant.Cluster.State.REMOVING_NODES)) {

				esConf.setNewNodes(this.getNodes(newClusterConf));
				logger.info("Adding new nodes to configuration...");
				components.put(Constant.Component.Name.ELASTICSEARCH, esConf);
			} else {
				// create configuration.
				components = new HashMap<String, Configuration>(
						esClusterConf.getComponents());
			}
			// set the first node as ganglia master node.
			clusterConf.setGangliaMaster(esClusterConf.getNodeConfs().get(0));
			
			// setting agent, dependency and ganglia conf
			setDependencyConf(clusterConf, components);
			for (Configuration component : components.values()) {
				GenericConfiguration genericComp = (GenericConfiguration) component;
				genericComp.setClusterConf(esClusterConf);
			}
			logger.info("Created configuration for component deployments...");
			return components;
		} catch (Exception e) {

		}
		// TODO Auto-generated method stub
		return components;
	}
	
	/**
	 * Sets the dependency conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param components the components
	 */
	private void setDependencyConf(ClusterConf clusterConf,
			Map<String, Configuration> components) {

		ElasticSearchClusterConf esClusterConf = (ElasticSearchClusterConf) clusterConf;
		JavaConf javaConf = esClusterConf.getJavaConf();
		DependencyConf dependencyConf = ComponentConfigurator
				.getDependencyConf(javaConf.isInstall(), javaConf
						.getJavaBundle(), clusterConf, new HashMap(components));

		// Adding dependencies to component map
		components.put(Constant.Component.Name.DEPENDENCY, dependencyConf);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#createConfigs(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public Map<String, Configuration> createConfigs(ClusterConf clusterConf) {
		return createConfigs(null, clusterConf);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#postDeploy(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean postDeploy(ClusterConf clusterConf) {
		logger.info("Performing the post processing tasks...");

		try {

			// logging cluster state
			if (clusterConf.getState().equalsIgnoreCase(
					Constant.Cluster.State.ERROR)) {
				logger.error("Cluster deployment failed.");
			} else {
				logger.info("Cluster deployment done.");
			}

			// Updating the database status
			updateClusterDetails(clusterConf);

			// getting cluster nodes
			List<NodeConf> nodeConfs = new ArrayList<NodeConf>(
					clusterConf.getNodeConfs());

			// updating node details
			saveOrUpdateNodeDetails(nodeConfs, clusterConf);
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#preUndeploy(com.impetus.ankush.common.domain.Cluster)
	 */
	@Override
	public ClusterConf preUndeploy(Cluster dbCluster) throws Exception {
		// getting cluster conf obj.
		ClusterConf clusterConf = dbCluster.getClusterConf();
		// setting new operation id .
		clusterConf.setOperationId(logger.getNewOperationId(dbCluster.getId()));
		// setting state as removing.
		clusterConf.setState(Constant.Cluster.State.REMOVING);
		return clusterConf;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#postUndeploy(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public void postUndeploy(ClusterConf clusterConf) {
		// Do post undeploy operations
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#getError()
	 */
	@Override
	public String getError() {
		return error;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#preAddNodes(java.util.List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preAddNodes(List<NodeConf> nodeConfs, ClusterConf clusterConf) {
		try {
			clusterConf.setOperationId(logger.getNewOperationId(clusterConf
					.getClusterId()));
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
			return false;
		}

		for (NodeConf nodeConf : nodeConfs) {
			// Check if node is in use
			try {
				if (nodeManager.getByPropertyValueGuarded(
						Constant.Keys.PUBLICIP, nodeConf.getPublicIp()) != null) {
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

		if (error == null) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#postAddNodes(java.util.List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean postAddNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		
		ElasticSearchClusterConf esClusterconf = (ElasticSearchClusterConf)clusterConf;
		List<NodeConf> newNodes = new ArrayList<NodeConf>();
		boolean returnFlag = false;

		// iterating over the nodes.
		for (NodeConf nodeConf : nodeConfs) {
			// getting db node object.
			Node node = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodeConf.getPublicIp());

			// if cluser deployment is not successful(clusterState=ERROR) on
			// this node
			if (esClusterconf.getState().equals(Constant.Cluster.State.ERROR)) {

				logger.info(node.getPublicIp(), "Removing node from DB");

				// node addition is failed,so returnFlag is set to true.
				returnFlag = true;

				// remove this node from DB.
				nodeManager.remove(node.getId());
			} else {
				// set nodeState as deployed.
				nodeConf.setNodeState(Constant.Node.State.DEPLOYED);
				esClusterconf.getNodeConfs().add(nodeConf);
			}
			// adding each node to newNodes.
			newNodes.add(nodeConf);
		}
		// setting state.
		esClusterconf.setState(Constant.Cluster.State.DEPLOYED);

		// setting new nodes.
		esClusterconf.setNewNodes(newNodes);

		// updating cluster details.
		updateCluster(esClusterconf);

		// if node addition is failed,return
		if (returnFlag) {
			return returnFlag;
		}
		// else save the newNodes to DB.
		return saveOrUpdateNodeDetails(newNodes, esClusterconf);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#postRemoveNodes(java.util.List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean postRemoveNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		// remove all the removing state nodes from db.
		try {
			ElasticSearchConf conf = (ElasticSearchConf) clusterConf.getClusterComponents()
					.get(Constant.Component.Name.ELASTICSEARCH);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("clusterId", clusterConf.getClusterId());
			map.put("state", Constant.Node.State.REMOVING);
			List<Node> nodes = nodeManager.getAllByPropertyValue(map);
			for (Node node : nodes) {
				NodeConf nodeConf = node.getNodeConf();
				nodeManager.remove(node.getId());
				conf.getNodes().remove(nodeConf);
				conf.getNewNodes().remove(nodeConf);
			}
		} catch (Exception e) {
			error = e.getMessage();
			return false;
		}
		clusterConf.setState(Constant.Cluster.State.DEPLOYED);
		return updateClusterDetails(clusterConf);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#getClusterConf(com.impetus.ankush.common.domain.Cluster)
	 */
	@Override
	public ClusterConf getClusterConf(Cluster dbCluster) {
		return dbCluster.getClusterConf();
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#getNodes(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public List<NodeConf> getNodes(ClusterConf clusterConf) {
		ElasticSearchClusterConf esClusterConf = (ElasticSearchClusterConf)clusterConf;
		return esClusterConf.getNewNodes();
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#saveNodeDetails(java.util.List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean saveNodeDetails(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		
		ElasticSearchClusterConf esClusterConf = (ElasticSearchClusterConf)clusterConf;
		// save new nodes in db.
		saveOrUpdateNodeDetails(nodeConfs, clusterConf);
		
		// setting new nodes.
		esClusterConf.getEsConf().setNewNodes(nodeConfs);
		
		return updateCluster(clusterConf);
		
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#updateClusterDetails(com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean updateClusterDetails(ClusterConf clusterConf) {
		try {
			// Get saved cluster object and change its status/state
			logger.debug("Updating cluster and nodes information...");

			// Getting cluster object
			Cluster cluster = clusterManager.get(clusterConf.getClusterId());

			// Setting cluster state & configuration as of ElasticSearch state &
			// configuration
			cluster.setState(clusterConf.getState());
			cluster.setClusterConf(clusterConf);

			// save to database
			cluster = clusterManager.save(cluster);

			saveOrUpdateNodeDetails(clusterConf.getNodeConfs(), clusterConf);
			logger.debug("Updated cluster and nodes information...");
			return true;
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	/**
	 * Update cluster.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean updateCluster(ClusterConf conf) {
		logger.info("updating clusterConf...");
		// Create and save new cluster
		Cluster cluster = null;

		try {
			cluster = clusterManager.get(conf.getClusterId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (cluster == null) {
			logger.error("Failed to fetch cluster from database.");
			return false;
		}
		cluster.setState(conf.getState());
		cluster.setClusterConf(conf);

		try {
			logger.info("Saving updated clusterConf to cluster...");
			cluster = clusterManager.save(cluster);
			logger.info("Saved updated clusterConf to cluster...");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Clusterable#preRemoveNodes(java.util.List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preRemoveNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		List<String> nodes = new ArrayList<String>();
		for (NodeConf node : nodeConfs) {
			nodes.add(node.getPublicIp());
		}
		nodeConfs.clear();
		for (NodeConf node : clusterConf.getNodeConfs()) {
			if (nodes.contains(node.getPublicIp())) {
				// check node can be deleted or not
				boolean canNodeBeDeleted = canDeleteNode(node, clusterConf);
				logger.debug("Can Node -" + node.getPublicIp() + "Be Deleted :"
						+ canNodeBeDeleted);
				if (!canNodeBeDeleted) {
					return canNodeBeDeleted;
				}
				node.setNodeState(Constant.Node.State.REMOVING);
				nodeConfs.add(node);
			}
		}
		// update cluster details.
		updateCluster(clusterConf);

		// update nodes
		return saveOrUpdateNodeDetails(clusterConf.getNodeConfs(), clusterConf);
	}
	
	/**
	 * Can delete node.
	 *
	 * @param nodeConf the node conf
	 * @param clusterConf the cluster conf
	 * @return true, if successful
	 */
	private boolean canDeleteNode(NodeConf nodeConf, ClusterConf clusterConf) {
		if (isGmetadNode(nodeConf, clusterConf)) {
			this.error = "Could not delete the node:" + nodeConf.getPublicIp()
					+ ",As it is ganglia master node.";
			logger.error("Could not delete the node:" + nodeConf.getPublicIp()
					+ ",As it is ganglia master node.");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if is gmetad node.
	 *
	 * @param nodeConf the node conf
	 * @param clusterConf the cluster conf
	 * @return true, if is gmetad node
	 */
	private boolean isGmetadNode(NodeConf nodeConf, ClusterConf clusterConf) {
		if (clusterConf.getGangliaMaster().equals(nodeConf)) {
			return true;
		}
		return false;
	}
}
