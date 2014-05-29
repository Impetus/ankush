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
package com.impetus.ankush.storm;

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
import com.impetus.ankush.common.zookeeper.ZookeeperConf;

/**
 * The Class StormCluster.
 * 
 * @author hokam
 */
public class StormCluster implements Clusterable {

	/** Generic cluster master. */
	static private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** Generic node master. */
	static private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(StormCluster.class);

	/** The error. */
	private String error = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#validate(com.impetus.
	 * ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean validate(ClusterConf clusterConf) {
		// Create validator object
		StormClusterValidator validator = new StormClusterValidator();
		// type cast storm conf object.
		StormClusterConf conf = (StormClusterConf) clusterConf;
		// validating cluster.
		return validator.validate(conf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#validate(com.impetus.
	 * ankush.common.framework.config.ClusterConf, java.util.List)
	 */
	@Override
	public boolean validate(ClusterConf clusterConf, List<NodeConf> nodeConfs) {
		// create validator object.
		StormClusterValidator validator = new StormClusterValidator();
		// validating nodes.
		return validator.validate(nodeConfs, clusterConf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#saveClusterDetails(com
	 * .impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean saveClusterDetails(ClusterConf clusterConf) {
		try {
			AlertsConf alertsConf = new AlertsConf();
			// Create and save new cluster
			Cluster cluster = new Cluster();
			cluster.setName(clusterConf.getClusterName());
			cluster.setTechnology(clusterConf.getTechnology());
			cluster.setState(clusterConf.getState());
			cluster.setEnvironment(clusterConf.getEnvironment());
			cluster.setClusterConf(clusterConf);
			cluster.setCreatedAt(new Date());
			cluster.setUser(clusterConf.getCurrentUser());
			cluster.setAlertConf(alertsConf);
			// save to database
			cluster = clusterManager.save(cluster);

			// Set cluster and operation id
			clusterConf.setClusterId(cluster.getId());
			clusterConf
					.setOperationId(logger.getNewOperationId(cluster.getId()));
			logger.setClusterId(clusterConf.getClusterId());
			logger.setOperationId(clusterConf.getOperationId());
			logger.setClusterName(cluster.getName());

			// Create and save nodes
			saveOrUpdateNodeDetails(clusterConf.getNodeConfs(), clusterConf);
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
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	private boolean saveOrUpdateNodeDetails(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		try {
			// storm cluster conf //
			StormClusterConf conf = (StormClusterConf) clusterConf;
			// Create and save nodes
			for (NodeConf nodeConf : nodeConfs) {
				Node node = nodeManager.getByPropertyValueGuarded(
						Constant.Keys.PUBLICIP, nodeConf.getPublicIp());

				// if null create empty object.
				if (node == null) {
					node = new Node();
					node.setCreatedAt(new Date());
				}
				nodeConf.setType(conf.getNodeType(nodeConf));
				// setting node state as deploying if node state is null else
				// same.
				if (nodeConf.getNodeState() == null) {
					node.setState(Constant.Node.State.DEPLOYING);
				} else {
					node.setState(nodeConf.getNodeState());
				}
				node.setClusterId(clusterConf.getClusterId());
				node.setPrivateIp(nodeConf.getPrivateIp());
				node.setPublicIp(nodeConf.getPublicIp());
				node.setNodeConf(nodeConf);
				node = nodeManager.save(node);
				nodeConf.setId(node.getId());
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#preDeploy(com.impetus
	 * .ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preDeploy(ClusterConf clusterConf) {
		// Do pre deploy operations.
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#createConfigs(com.impetus
	 * .ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public Map<String, Configuration> createConfigs(ClusterConf newClusterConf,
			ClusterConf clusterConf) {
		Map<String, Configuration> components = new HashMap<String, Configuration>();
		try {
			logger.info("Creating configuration for component deployments...");
			StormClusterConf conf = (StormClusterConf) clusterConf;
			/** get state **/
			String state = conf.getState();

			if (state.equals(Constant.Cluster.State.ADDING_NODES)
					|| state.equals(Constant.Cluster.State.REMOVING_NODES)) {
				
				// add zookeeper component only in removing_nodes state
				if (state.equals(
						Constant.Cluster.State.REMOVING_NODES)) {
					ZookeeperConf zConf = conf.getZookeeper();
					List<NodeConf> zookeeperNodes = new ArrayList<NodeConf>();
					for (NodeConf nodeConf : this.getNodes(newClusterConf)) {
						if (zConf.getNodes().contains(nodeConf)) {
							zookeeperNodes.add(nodeConf);
						}
					}
					//add zookeeper component onlyif at least one node has zookeeper deployed on itself.
					if(!zookeeperNodes.isEmpty()){
						zConf.setNewNodes(zookeeperNodes);
						components.put(Constant.Component.Name.ZOOKEEPER, zConf);
					}
				}
				
				// currently adding only storm component conf for adding nodes.
				StormConf sConf = conf.getStorm();
				sConf.setNewNodes(this.getNodes(newClusterConf));
				components.put(Constant.Component.Name.STORM, sConf);
			} else {
				// create configuration.
				components = new HashMap<String, Configuration>(
						conf.getComponents());
			}
			conf.setGangliaMaster(conf.getStorm().getNimbus());

			// setting dependency config.
			setDependencyConf(clusterConf, components);

			for (Configuration component : components.values()) {
				GenericConfiguration genericComp = (GenericConfiguration) component;
				genericComp.setClusterConf(conf);
			}

			return components;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return components;

	}

	/**
	 * Sets the dependency conf.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param components
	 *            the components
	 */
	private void setDependencyConf(ClusterConf clusterConf,
			Map<String, Configuration> components) {
		JavaConf javaConf = ((StormClusterConf) clusterConf).getJavaConf();
		boolean installJava = javaConf.isInstall();
		String javaBinFileName = javaConf.getJavaBundle();
		DependencyConf dependencyConf = ComponentConfigurator
				.getDependencyConf(installJava, javaBinFileName, clusterConf,
						new HashMap(components));
		components.put(Constant.Component.Name.DEPENDENCY, dependencyConf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#postDeploy(com.impetus
	 * .ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean postDeploy(ClusterConf clusterConf) {
		logger.info("Performing the post processing tasks...");

		try {
			if (clusterConf.getState().equalsIgnoreCase(
					Constant.Cluster.State.ERROR)) {
				logger.error("Cluster deployment failed.");
			} else {
				logger.info("Cluster deployment done.");
			}

			// Updating the database status
			updateCluster(clusterConf);

			// update nodes details.
			saveOrUpdateNodeDetails(clusterConf.getNodeConfs(), clusterConf);
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#preUndeploy(com.impetus
	 * .ankush.common.domain.Cluster)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#postUndeploy(com.impetus
	 * .ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public void postUndeploy(ClusterConf clusterConf) {
		// Do post un-deploy operations.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.framework.Clusterable#getError()
	 */
	@Override
	public String getError() {
		// return error.
		return error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#preAddNodes(java.util
	 * .List, com.impetus.ankush.common.framework.config.ClusterConf)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#postAddNodes(java.util
	 * .List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean postAddNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		// Cast to storm cluster conf.
		StormClusterConf conf = (StormClusterConf) clusterConf;
		List<NodeConf> newNodes = new ArrayList<NodeConf>();
		boolean returnFlag = false;
		// iterating over the nodes.
		for (NodeConf nodeConf : nodeConfs) {
			// getting db node object.
			Node node = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodeConf.getPublicIp());

			if (conf.getState().equals(Constant.Cluster.State.ERROR)) {
				nodeManager.remove(node.getId());

				// node addition is failed,so returnFlag is set to true.
				returnFlag = true;
			} else {
				// set nodeState as deployed.
				nodeConf.setNodeState(Constant.Node.State.DEPLOYED);
				conf.getStorm().getSupervisors().add(nodeConf);
			}
			newNodes.add(nodeConf);
		}
		// setting state.
		conf.setState(Constant.Cluster.State.DEPLOYED);
		// setting new nodes.
		conf.setNewNodes(newNodes);
		// updating cluster details.
		updateCluster(conf);

		// if node addition is failed,return
		if (returnFlag) {
			return true;
		}
		return saveOrUpdateNodeDetails(newNodes, conf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#postRemoveNodes(java.
	 * util.List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean postRemoveNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		// remove all the removing state nodes from db.
		try {
			StormConf conf = (StormConf) clusterConf.getClusterComponents()
					.get(Constant.Component.Name.STORM);
			ZookeeperConf zkConf = (ZookeeperConf) clusterConf
					.getClusterComponents().get(
							Constant.Component.Name.ZOOKEEPER);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("clusterId", clusterConf.getClusterId());
			map.put("state", Constant.Node.State.REMOVING);
			List<Node> nodes = nodeManager.getAllByPropertyValue(map);
			for (Node node : nodes) {
				NodeConf nodeConf = node.getNodeConf();
				nodeManager.remove(node.getId());
				conf.getSupervisors().remove(nodeConf);
				conf.getNewNodes().remove(nodeConf);
				
				//remove node from zkNodes of Storm to update cluster tile for zookeeper nodes
				conf.getZkNodes().remove(nodeConf.getPublicIp());
				
				//remove node from zookeeper newNodes
				zkConf.getNewNodes().remove(nodeConf);
				
				//remove node from zookeeper nodes
				zkConf.getNodes().remove(nodeConf);
			}
		} catch (Exception e) {
			error = e.getMessage();
			return false;
		}
		clusterConf.setState(Constant.Cluster.State.DEPLOYED);
		return updateClusterDetails(clusterConf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#getClusterConf(com.impetus
	 * .ankush.common.domain.Cluster)
	 */
	@Override
	public ClusterConf getClusterConf(Cluster dbCluster) {
		// getting cluster conf.
		return dbCluster.getClusterConf();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#getNodes(com.impetus.
	 * ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public List<NodeConf> getNodes(ClusterConf clusterConf) {
		// getting cluster nodes.
		StormClusterConf conf = (StormClusterConf) clusterConf;
		return conf.getNewNodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#saveNodeDetails(java.
	 * util.List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean saveNodeDetails(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		// setting new nodes in db;
		StormClusterConf conf = (StormClusterConf) clusterConf;
		// save new nodes in db.
		saveOrUpdateNodeDetails(nodeConfs, conf);

		// setting new nodes.
		conf.getStorm().setNewNodes(nodeConfs);

		// update cluster details.
		return updateCluster(conf);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#updateClusterDetails(
	 * com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean updateClusterDetails(ClusterConf clusterConf) {
		try {
			// Get saved cluster object and change its status/state
			logger.debug("Updating cluster and nodes information...");
			Cluster cluster = clusterManager.get(clusterConf.getClusterId());
			cluster.setState(clusterConf.getState());
			cluster.setClusterConf(clusterConf);
			// save to database
			cluster = clusterManager.save(cluster);

			// Create and save nodes
			saveOrUpdateNodeDetails(clusterConf.getNodeConfs(), clusterConf);
			return true;
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * Update cluster details.
	 * 
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean updateCluster(ClusterConf conf) {
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
			cluster = clusterManager.save(cluster);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#preRemoveNodes(java.util
	 * .List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preRemoveNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		List<NodeConf> nodeList = new ArrayList<NodeConf>();
		nodeList.addAll(nodeConfs);
		List<String> nodes = new ArrayList<String>();
		for (NodeConf node : nodeConfs) {
			nodes.add(node.getPublicIp());
		}
		nodeConfs.clear();
		for (NodeConf node : clusterConf.getNodeConfs()) {
			if (nodes.contains(node.getPublicIp())) {
				// check node can be deleted or not
				int zkNodesCountToBeDeleted = getZookeeperNodesCountToBeDeleted(nodeList, clusterConf);
				boolean canNodeBeDeleted = canDeleteNode(node, clusterConf,zkNodesCountToBeDeleted);
				logger.error("Can Node -" + node.getPublicIp() + "Be Deleted :"
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
	 * method to calculate zookeeper node from incoming nodes for deletion
	 * 
	 * @param nodeConfs
	 * @param clusterConf
	 * @return
	 */
	private int getZookeeperNodesCountToBeDeleted(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		ZookeeperConf conf = (ZookeeperConf) clusterConf.getClusterComponents()
				.get(Constant.Component.Name.ZOOKEEPER);
		int zkNodesCountToBeDeleted = 0;
		List<String> zkIPList = new ArrayList<String>();
		for (NodeConf nc : conf.getNodes()) {
			zkIPList.add(nc.getPublicIp());
		}
		for (NodeConf nodeConf : nodeConfs) {
			if (zkIPList.contains(nodeConf.getPublicIp())) {
				zkNodesCountToBeDeleted++;
			}
		}
		return zkNodesCountToBeDeleted;
	}

	/**
	 * this method checks whether node can be deleted or not.
	 */
	private boolean canDeleteNode(NodeConf nodeConf, ClusterConf clusterConf,int zkNodesCountToBeDeleted) {
		StormConf conf = (StormConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.STORM);
		// if node is a nimbus node,it cann't be deleted.
		if (isNimbusNode(nodeConf, clusterConf)) {
			this.error = "Could not delete the node:" + nodeConf.getPublicIp()
					+ ",As it is nimbus node.";
			logger.error("Could not delete the node:" + nodeConf.getPublicIp()
					+ ",As it is nimbus node.");
			return false;
		}
		if (!checkForZookeeperNodes(nodeConf, clusterConf,zkNodesCountToBeDeleted)) {
			this.error = "Could not delete node:" + nodeConf.getPublicIp()
					+ ",As there must be at least one zookeeper node.";
			logger.error("Could not delete node:" + nodeConf.getPublicIp()
					+ ",As there must be at least one zookeeper node.");
			return false;
		}
		return true;
	}
	
	/**
	 * checks node is nimbusnode or not
	 * @param nodeConf
	 * @param clusterConf
	 * @return
	 */
	private boolean isNimbusNode(NodeConf nodeConf, ClusterConf clusterConf) {
		StormConf conf = (StormConf) clusterConf.getClusterComponents().get(
				Constant.Component.Name.STORM);
		if (conf.getNimbus().getPublicIp().equals(nodeConf.getPublicIp())) {
			return true;
		}
		return false;
	}

	/**
	 * checks node is zookeeper node or not
	 * @param nodeConf
	 * @param clusterConf
	 * @return
	 */
	private boolean checkForZookeeperNodes(NodeConf nodeConf,
			ClusterConf clusterConf,int zkNodesCountToBeDeleted) {
		ZookeeperConf conf = (ZookeeperConf) clusterConf.getClusterComponents()
				.get(Constant.Component.Name.ZOOKEEPER);
		int totalZookeeperNodes = conf.getNodes().size();
		if (totalZookeeperNodes > zkNodesCountToBeDeleted) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Configuration> createConfigs(ClusterConf clusterConf) {
		return createConfigs(null, clusterConf);
	}
}
