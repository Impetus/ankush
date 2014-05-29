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
package com.impetus.ankush.hadoop;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

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
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.config.HadoopEcoSystemConfigurator;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;

/**
 * The Class HadoopCluster.
 * 
 * @author mayur
 */
public class HadoopCluster implements Clusterable {

	/** Generic cluster master. */
	private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** Generic node master. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/* Logger object */
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(HadoopCluster.class);

	// Error List.
	/** The error. */
	private String error = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#saveClusterDetails(com
	 * .impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean saveClusterDetails(ClusterConf clusterConf) {

		logger.info("Saving cluster and node informations...");

		// Casting Clusterconf to HadoopClusterConf
		HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) clusterConf;
		hadoopClusterConf.setTechnology(Constant.Technology.HADOOP);

		return saveClusterConfig(hadoopClusterConf);
	}

	/**
	 * Save cluster config.
	 * 
	 * @param hadoopClusterConf
	 *            the hadoop cluster conf
	 * @return true, if successful
	 */
	private boolean saveClusterConfig(HadoopClusterConf hadoopClusterConf) {
		try {
			AlertsConf alertsConf = new AlertsConf();
			// Create and save new cluster
			Cluster cluster = new Cluster();
			cluster.setName(hadoopClusterConf.getClusterName());
			cluster.setTechnology(Constant.Technology.HADOOP);
			cluster.setState(hadoopClusterConf.getState());
			cluster.setEnvironment(hadoopClusterConf.getEnvironment());
			cluster.setClusterConf(hadoopClusterConf);
			cluster.setCreatedAt(new Date());
			cluster.setUser(hadoopClusterConf.getCurrentUser());
			cluster.setAlertConf(alertsConf);
			// save to database
			cluster = clusterManager.save(cluster);

			// Set cluster and operation id
			hadoopClusterConf.setClusterId(cluster.getId());
			hadoopClusterConf.setOperationId(logger.getNewOperationId(cluster
					.getId()));
			logger.setClusterId(hadoopClusterConf.getClusterId());
			logger.setOperationId(hadoopClusterConf.getOperationId());
			logger.setClusterName(cluster.getName());

			// Create and save nodes
			List<NodeConf> nodeConfs = new ArrayList<NodeConf>(
					hadoopClusterConf.getNodes());

			saveOrUpdateNodeDetails(nodeConfs, hadoopClusterConf);
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
	 * com.impetus.ankush.common.framework.Clusterable#preDeploy(com.impetus
	 * .ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preDeploy(ClusterConf clusterConf) {
		// Perform pre-deploy operations
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
		// try {
		logger.info("Creating configuration for component deployments...");
		HadoopClusterConf conf = (HadoopClusterConf) clusterConf;

		if (clusterConf.getState().equals(Constant.Cluster.State.DEPLOYING)) {
			// create ecosystem configurations
			HadoopEcoSystemConfigurator configurator = new HadoopEcoSystemConfigurator();
			components = configurator.getEcoSystemConfigurationMap(conf);
		} else if (clusterConf.getState().equals(
				Constant.Cluster.State.ADDING_NODES)) {
			// currently adding only hadoop component conf for adding nodes.
			HadoopConf hConf = (HadoopConf) conf.getComponents().get(
					Constant.Component.Name.HADOOP);
			String componentName = Constant.Component.Name.HADOOP;
			if (hConf == null) {
				hConf = (Hadoop2Conf) conf.getComponents().get(
						Constant.Component.Name.HADOOP2);
				componentName = Constant.Component.Name.HADOOP2;

			}
			hConf.setNewNodes(this.getNodes(newClusterConf));
			components.put(componentName, hConf);
		} else if (clusterConf.getState().equals(
				Constant.Cluster.State.REMOVING_NODES)) {
			// currently adding only hadoop component conf for removing
			// nodes.
			List<String> nodes = new ArrayList<String>();
			List<NodeConf> nodeConfs = this.getNodes(newClusterConf);
			for (NodeConf node : nodeConfs) {
				nodes.add(node.getPublicIp());
			}
			nodeConfs.clear();
			for (NodeConf node : clusterConf.getNodeConfs()) {
				if (nodes.contains(node.getPublicIp())) {
					node.setNodeState(Constant.Node.State.REMOVING);
					nodeConfs.add(node);
				}
			}
			// currently adding only hadoop component conf for adding nodes.
			HadoopConf hConf = (HadoopConf) conf.getComponents().get(
					Constant.Component.Name.HADOOP);
			String componentName = Constant.Component.Name.HADOOP;
			if (hConf == null) {
				hConf = (Hadoop2Conf) conf.getComponents().get(
						Constant.Component.Name.HADOOP2);
				componentName = Constant.Component.Name.HADOOP2;
			}
			hConf.setNewNodes(nodeConfs);
			components.put(componentName, hConf);
		} else {
			components = new HashMap<String, Configuration>(
					conf.getComponents());
		}
		conf.setGangliaMaster(conf.getNameNode());

		// create Dependency conf
		createDependencyConf(conf, components);

		for (Configuration component : components.values()) {
			GenericConfiguration genericComp = (GenericConfiguration) component;
			genericComp.setClusterConf(conf);
		}

		return components;
		// } catch (Exception e) {
		// logger.error(e.getMessage(), e);
		//
		// }
	}

	/**
	 * Creates the dependency conf.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param components
	 *            the components
	 */
	private void createDependencyConf(HadoopClusterConf clusterConf,
			Map<String, Configuration> components) {

		boolean installJava = clusterConf.getJavaConf().isInstall();
		String javaBundle = clusterConf.getJavaConf().getJavaBundle();
		DependencyConf dependencyConf = ComponentConfigurator
				.getDependencyConf(installJava, javaBundle, clusterConf,
						new HashMap<String, Configuration>(components));
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

		HadoopClusterConf hadoopClusterConf;
		try {
			hadoopClusterConf = (HadoopClusterConf) clusterConf;

			if (clusterConf.getState().equalsIgnoreCase(
					Constant.Cluster.State.ERROR)) {
				logger.error("Cluster deployment failed.");
			} else {
				logger.info("Cluster deployment done.");
			}

			// Updating the database status
			updateClusterDetails(hadoopClusterConf);

			// update nodes details.
			List<NodeConf> nodeConfs = new ArrayList<NodeConf>(
					hadoopClusterConf.getNodes());
			saveOrUpdateNodeDetails(nodeConfs, hadoopClusterConf);
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	/**
	 * Update cluster details.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	public boolean updateClusterDetails(ClusterConf clusterConf) {
		try {
			// Get saved cluster object and change its status/state
			logger.info("Updating cluster and nodes information...");
			HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) clusterConf;

			Cluster cluster = clusterManager.get(hadoopClusterConf
					.getClusterId());
			cluster.setState(hadoopClusterConf.getState());
			cluster.setClusterConf(hadoopClusterConf);
			// save to database
			cluster = clusterManager.save(cluster);

			// Create and save nodes
			List<NodeConf> nodeConfs = new ArrayList<NodeConf>(
					hadoopClusterConf.getNodes());

			saveOrUpdateNodeDetails(nodeConfs, hadoopClusterConf);
			return true;
		} catch (Exception e) {
			error = e.getMessage();
			logger.error(e.getMessage(), e);
		}
		return false;
	}

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
		HadoopClusterValidator validator = new HadoopClusterValidator();
		HadoopClusterConf conf = (HadoopClusterConf) clusterConf;
		return validator.validate(conf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#preUndeploy(com.impetus
	 * .ankush.common.domain.Cluster)
	 */
	@Override
	public ClusterConf preUndeploy(Cluster dbCluster)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException, InvocationTargetException, Exception {
		HadoopClusterConf clusterConf = (HadoopClusterConf) dbCluster
				.getClusterConf();
		clusterConf.setOperationId(logger.getNewOperationId(dbCluster.getId()));
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
		// Do post un-deploy operation
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.common.framework.Clusterable#getError()
	 */
	@Override
	public String getError() {
		return error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.cluster.Clusterable#preAddNodes(java.util.List,
	 * com.impetus.ankush.dto.ClusterConf)
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
			// HadoopClusterConf conf = (HadoopClusterConf) clusterConf;
			// HadoopConf hadoopConf = (HadoopConf) conf.getComponents().get(
			// Constant.Component.Name.HADOOP);
			// if(hadoopConf == null) {
			// hadoopConf = (Hadoop2Conf) conf.getComponents().get(
			// Constant.Component.Name.HADOOP2);
			// }
			//
			// Set<NodeConf> expectedNodeList = new HashSet<NodeConf>();
			// expectedNodeList.addAll(nodeConfs);
			// expectedNodeList.addAll(conf.getNodes());
			// hadoopConf.setExpectedNodesAfterAddOrRemove(expectedNodeList);
			// String rackFileContent = "";
			// if (hadoopConf.isRackEnabled()) {
			// rackFileContent =
			// HadoopRackAwareness.createRackFileContents(expectedNodeList);
			// }
			// hadoopConf.setRackFileContent(rackFileContent);
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
		// Cast to hadoop cluster conf.
		HadoopClusterConf conf = (HadoopClusterConf) clusterConf;
		Node node = null;
		List<HadoopNodeConf> newNodes = new ArrayList<HadoopNodeConf>();
		List<NodeConf> newNodeConfs = new ArrayList<NodeConf>();
		for (NodeConf nodeConf : nodeConfs) {
			node = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodeConf.getPublicIp());

			if (conf.getState().equals(Constant.Cluster.State.ERROR)) {
				nodeManager.remove(node.getId());
			} else {
				nodeConf.setNodeState(Constant.Node.State.DEPLOYED);
				newNodeConfs.add(nodeConf);
				conf.getNodes().add((HadoopNodeConf) nodeConf);
			}

			HadoopNodeConf hNode = (HadoopNodeConf) nodeConf;
			newNodes.add(hNode);
		}
		conf.setState(Constant.Cluster.State.DEPLOYED);
		conf.setNewNodes(newNodes);
		updateClusterDetails(conf);
		boolean status = saveOrUpdateNodeDetails(newNodeConfs, conf);
		return status;
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

		Node node = null;

		// Cast to hadoop cluster conf.
		HadoopClusterConf conf = (HadoopClusterConf) clusterConf;
		List<HadoopNodeConf> dbNodes = conf.getNodes();
		List<HadoopNodeConf> clusterConfNodes = dbNodes;

		// Removing nodes from Database
		for (NodeConf nodeConf : nodeConfs) {
			try {
				// Removing node
				node = nodeManager.getByPropertyValueGuarded(
						Constant.Keys.PUBLICIP, nodeConf.getPublicIp());
				nodeManager.remove(node.getId());
			} catch (Exception e) {
				logger.error(
						"ERROR : Could not remove entry from database.. !", e);
			}

			try {
				// Removing nodes from Hadoop Cluster Configuration
				String nodeIp = nodeConf.getPublicIp();
				for (HadoopNodeConf hnc : dbNodes) {
					String dbNodeIp = hnc.getPublicIp();
					if (dbNodeIp.equals(nodeIp)) {
						logger.debug("Deleting from Cluster Nodes : "
								+ dbNodeIp);
						clusterConfNodes.remove(hnc);
						break;
					}
				}
			} catch (Exception e) {
				logger.error("ERROR : Could not update hadoopClusterConf.. !",
						e);
			}
		}
		conf.setNodes(clusterConfNodes);
		conf.setState(Constant.Cluster.State.DEPLOYED);
		updateClusterDetails(conf);
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
		// Cast to hadoop cluster conf.
		HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) clusterConf;
		boolean isHadoop2 = false;
		// Set Cluster Node Confs for Remove Nodes Operation
		HadoopConf hadoopConf = (HadoopConf) hadoopClusterConf.getComponents()
				.get(Constant.Component.Name.HADOOP);
		if (hadoopConf == null) {
			hadoopConf = (Hadoop2Conf) hadoopClusterConf.getComponents().get(
					Constant.Component.Name.HADOOP2);
			isHadoop2 = true;
		}

		if (!validateNodesDelete(nodeConfs, hadoopConf, isHadoop2)) {
			return false;
		}

		List<HadoopNodeConf> dbNodes = hadoopClusterConf.getNodes();
		// Map<String, NodeConf> objNodeConfs = new HashMap<String, NodeConf>();

		HashMap<String, NodeConf> mapNodeConf = new HashMap<String, NodeConf>();
		// Removing nodes from Database

		List<String> nodes = new ArrayList<String>();
		for (NodeConf node : nodeConfs) {
			nodes.add(node.getPublicIp());
		}
		nodeConfs.clear();
		for (NodeConf node : clusterConf.getNodeConfs()) {
			if (nodes.contains(node.getPublicIp())) {
				node.setNodeState(Constant.Node.State.REMOVING);
				nodeConfs.add(node);
			}
		}

		for (NodeConf nodeConf : nodeConfs) {
			// Removing nodes from Hadoop Cluster Configuration
			String nodeIp = nodeConf.getPublicIp();
			for (HadoopNodeConf hnc : dbNodes) {
				String dbNodeIp = hnc.getPublicIp();
				if (dbNodeIp.equals(nodeIp)) {
					logger.debug("Going to set state REMOVING for node "
							+ nodeIp);
					hnc.setNodeState(Constant.Node.State.REMOVING);
					break;
				}
			}
		}
		for (HadoopNodeConf hnc : dbNodes) {
			if (!hnc.getNodeState().equals(Constant.Node.State.REMOVING)) {
				mapNodeConf.put(hnc.getPublicIp(), hnc);
			}
		}

		String rackFileContent = "";
		Set<NodeConf> expectedNodeList = new HashSet<NodeConf>(
				mapNodeConf.values());
		hadoopConf.setExpectedNodesAfterAddOrRemove(expectedNodeList);
		if (hadoopConf.isRackEnabled()) {
			rackFileContent = HadoopRackAwareness
					.createRackFileContents(expectedNodeList);
		}
		hadoopConf.setRackFileContent(rackFileContent);

		// Save & Update Cluster configuration
		updateClusterDetails(hadoopClusterConf);
		return saveOrUpdateNodeDetails(nodeConfs, hadoopClusterConf);
	}

	private String getStringForInvalidNodeDelete(
			Map<String, Set<String>> nodeDeleteErrorList) {
		String result = new String(
				"Delete operation not supported for node(s) configured as:");
		for (String nodeIp : nodeDeleteErrorList.keySet()) {
			// <BR> tag to be changed as per UI changes
			String nodeRoleMap = "<br>  "
					+ nodeIp
					+ " - "
					+ StringUtils.collectionToDelimitedString(
							nodeDeleteErrorList.get(nodeIp), ",");
			result += nodeRoleMap;
		}
		return result;
	}

	private boolean validateNodesDelete(List<NodeConf> nodeConfs,
			HadoopConf hadoopConf, boolean isHadoop2) {

		Map<String, Set<String>> nodeDeleteErrorList = new HashMap<String, Set<String>>();

		Hadoop2Conf hadoop2Conf = null;
		if (isHadoop2) {
			hadoop2Conf = (Hadoop2Conf) hadoopConf;
		}

		for (NodeConf nodeConf : nodeConfs) {
			Set<String> daemonList = new HashSet<String>();
			boolean allowDelete = true;

			if (hadoopConf.getNamenode() != null) {
				if (HadoopUtils.areNodesEqual(hadoopConf.getNamenode(),
						nodeConf)) {
					daemonList.add(Constant.Role.NAMENODE);
					allowDelete = false;
				}
			}

			if (hadoopConf.getSecondaryNamenode() != null) {
				if (HadoopUtils.areNodesEqual(
						hadoopConf.getSecondaryNamenode(), nodeConf)) {
					daemonList.add(Constant.Role.SECONDARYNAMENODE);
					allowDelete = false;
				}
			}

			if (isHadoop2) {

				if (hadoop2Conf.isHaEnabled()) {

					if (hadoop2Conf.getHaNameNodesPublicIp().contains(
							nodeConf.getPublicIp())) {
						daemonList.add(Constant.Role.NAMENODE);
						allowDelete = false;
					}

					for (NodeConf journalNode : hadoop2Conf.getJournalNodes()) {
						if (HadoopUtils.areNodesEqual(journalNode, nodeConf)) {
							daemonList.add(Constant.Role.JOURNALNODE);
							allowDelete = false;
						}
					}

					for (NodeConf zkNode : hadoop2Conf.getZkQuorumNodes()) {
						if (HadoopUtils.areNodesEqual(zkNode, nodeConf)) {
							daemonList.add(Constant.Role.ZOOKEEPER);
							allowDelete = false;
						}
					}
				}
				if (hadoop2Conf.isStartJobHistoryServer()) {
					if (HadoopUtils.areNodesEqual(
							hadoop2Conf.getJobHistoryServerNode(), nodeConf)) {
						daemonList.add(Constant.Role.JOBHISTORYSERVER);
						allowDelete = false;
					}
				}
				if (HadoopUtils.areNodesEqual(
						hadoop2Conf.getResourceManagerNode(), nodeConf)) {
					daemonList.add(Constant.Role.RESOURCEMANAGER);
					allowDelete = false;
				}
				if (hadoop2Conf.isWebProxyEnabled()) {
					if (HadoopUtils.areNodesEqual(
							hadoop2Conf.getWebAppProxyNode(), nodeConf)) {
						daemonList.add(Constant.Role.WEBAPPPROXYSERVER);
						allowDelete = false;
					}
				}
			}
			if (!allowDelete) {
				nodeDeleteErrorList.put(nodeConf.getPublicIp(), daemonList);
			}

		}
		if (nodeDeleteErrorList.size() > 0) {
			this.error = getStringForInvalidNodeDelete(nodeDeleteErrorList);
			return false;
		}
		return true;
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
		// Cast to hadoop cluster conf.
		HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) clusterConf;
		List<HadoopNodeConf> newNodes = new ArrayList<HadoopNodeConf>();
		for (NodeConf nodeConf : nodeConfs) {
			HadoopNodeConf hNode = (HadoopNodeConf) nodeConf;
			nodeConf.setNodeState(Constant.Node.State.ADDING);
			newNodes.add(hNode);
		}
		hadoopClusterConf.setNewNodes(newNodes);
		updateClusterDetails(hadoopClusterConf);
		return saveOrUpdateNodeDetails(nodeConfs, hadoopClusterConf);
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
			HadoopClusterConf clusterConf) {
		try {
			// Create and save nodes
			for (NodeConf hadoopNode : nodeConfs) {
				Node node = nodeManager.getByPropertyValueGuarded(
						Constant.Keys.PUBLICIP, hadoopNode.getPublicIp());

				// if null create empty object.
				if (node == null) {
					node = new Node();
					node.setCreatedAt(new Date());
				}

				// setting node state as deploying if node state is null else
				// same.
				if (hadoopNode.getNodeState() == null) {
					node.setState(Constant.Node.State.DEPLOYING);
				} else {
					node.setState(hadoopNode.getNodeState());
				}
				String strRackInfo = hadoopNode.getRack();
				node.setClusterId(clusterConf.getClusterId());
				node.setPrivateIp(hadoopNode.getPrivateIp());
				node.setPublicIp(hadoopNode.getPublicIp());
				node.setRackInfo(strRackInfo);
				node.setNodeConf(hadoopNode);
				node = nodeManager.save(node);
				hadoopNode.setId(node.getId());
			}
			return true;
		} catch (Exception e) {
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
	private boolean updateClusterDetails(HadoopClusterConf conf) {
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
	 * com.impetus.ankush.common.framework.Clusterable#getClusterConf(com.impetus
	 * .ankush.common.domain.Cluster)
	 */
	@Override
	public ClusterConf getClusterConf(Cluster dbCluster) {
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
		return new ArrayList<NodeConf>(
				((HadoopClusterConf) clusterConf).getNewNodes());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.cluster.Clusterable#validate(com.impetus.ankush
	 * .dto.ClusterConf, java.util.List)
	 */
	@Override
	public boolean validate(ClusterConf clusterConf, List<NodeConf> nodeConfs) {
		logger.info("Validating...");
		HadoopClusterValidator validator = new HadoopClusterValidator();
		return validator.validate(nodeConfs, clusterConf);
	}

	@Override
	public Map<String, Configuration> createConfigs(ClusterConf clusterConf) {
		return createConfigs(null, clusterConf);
	}

	public void updateNameNodeRole(Long nodeId,
			HashMap<String, String> roleInfoMap) {
		Node dbNode = nodeManager.get(nodeId);
		String nodePublicIp = dbNode.getPublicIp();

		try {
			logger.info("updateNameNodeRole: nodePublicIp - " + nodePublicIp
					+ "\tRole - "
					+ roleInfoMap.get(Constant.Hadoop.NNROLE_INFO_KEY));

			Cluster dbCluster = clusterManager.get(dbNode.getClusterId());
			ClusterConf clusterConf = dbCluster.getClusterConf();

			if (clusterConf.getState().equals(Constant.Cluster.State.DEPLOYING)
					|| clusterConf.getState().equals(
							Constant.Cluster.State.REMOVING)) {
				return;
			}

			Hadoop2Conf hadoop2Conf = (Hadoop2Conf) clusterConf
					.getClusterComponents()
					.get(Constant.Component.Name.HADOOP2);
			if (hadoop2Conf == null) {
				logger.error("Invalid cluster configuration, unable to map Hadoop 2 object");
				return;
			}

			String nnRole = roleInfoMap.get(Constant.Hadoop.NNROLE_INFO_KEY);
			NodeConf nameNode = null;
			switch (Constant.Hadoop.NNROLE_VALUES.valueOf(nnRole)) {
			case ACTIVE:
				nameNode = hadoop2Conf.getActiveNamenode();
				if (nameNode != null) {
					if (!nodePublicIp.equals(nameNode.getPublicIp())) {
						hadoop2Conf.setActiveNamenode(dbNode.getNodeConf());
						logger.info("ACTIVE NAMENODE UPDATED : "
								+ hadoop2Conf.getActiveNamenode().getPublicIp());
					}
				} else {
					hadoop2Conf.setActiveNamenode(dbNode.getNodeConf());
					logger.info("ACTIVE NAMENODE UPDATED : "
							+ hadoop2Conf.getActiveNamenode().getPublicIp());
				}

				if (hadoop2Conf.getActiveNamenode().equals(
						hadoop2Conf.getStandByNamenode())) {
					hadoop2Conf.setStandByNamenode(null);
					logger.info("Same Role for Node " + nodePublicIp
							+ " : STANDBY NAMENODE set to NULL");
				}
				break;
			case STANDBY:
				nameNode = hadoop2Conf.getStandByNamenode();
				if (nameNode != null) {
					if (!nodePublicIp.equals(nameNode.getPublicIp())) {
						hadoop2Conf.setStandByNamenode(dbNode.getNodeConf());
						logger.info("STANDBY NAMENODE UPDATED : "
								+ hadoop2Conf.getStandByNamenode()
										.getPublicIp());
					}
				} else {
					hadoop2Conf.setStandByNamenode(dbNode.getNodeConf());
					logger.info("STANDBY NAMENODE UPDATED : "
							+ hadoop2Conf.getStandByNamenode().getPublicIp());
				}

				if (hadoop2Conf.getActiveNamenode().equals(
						hadoop2Conf.getStandByNamenode())) {
					hadoop2Conf.setActiveNamenode(null);
					logger.info("Same Role for Node " + nodePublicIp
							+ " : ACTIVE NAMENODE set to NULL");
				}

				break;
			case NULL:
				nameNode = hadoop2Conf.getActiveNamenode();
				if (nameNode != null) {
					if (nodePublicIp.equals(nameNode.getPublicIp())) {
						hadoop2Conf.setActiveNamenode(null);
						logger.info("ACTIVE NAMENODE set to NULL");
						break;
					}
				}

				nameNode = hadoop2Conf.getStandByNamenode();
				if (nameNode != null) {
					if (nodePublicIp.equals(nameNode.getPublicIp())) {
						hadoop2Conf.setStandByNamenode(null);
						logger.info("STANDBY NAMENODE set to NULL");
						break;
					}
				}
			}
			dbCluster.setClusterConf(clusterConf);
			clusterManager.save(dbCluster);

			// Logging NameNode Role Information
			if (hadoop2Conf.getActiveNamenode() != null) {
				logger.info("ACTIVE NAMENODE : "
						+ hadoop2Conf.getActiveNamenode().getPublicIp());
			} else {
				logger.info("ACTIVE NAMENODE : NULL");
			}
			if (hadoop2Conf.getStandByNamenode() != null) {
				logger.info("STANDBY NAMENODE : "
						+ hadoop2Conf.getStandByNamenode().getPublicIp());
			} else {
				logger.info("STANDBY NAMENODE : NULL");
			}

		} catch (Exception e) {
			logger.error("Unable to update the NameNode Role for "
					+ nodePublicIp + " node & role "
					+ roleInfoMap.get(Constant.Hadoop.NNROLE_INFO_KEY) + " .");

			logger.error(e.getMessage());
		}
	}
}
