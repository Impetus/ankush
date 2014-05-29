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
package com.impetus.ankush.oraclenosql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.alerts.AlertsConf;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.dependency.DependencyConf;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.framework.Clusterable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.validator.NTPServerValidator;
import com.impetus.ankush.common.utils.validator.Validator;

/**
 * Implementation for OracleNoSQL cluster management methods.
 * 
 * @author nikunj
 * 
 */
public class OracleNoSQLCluster implements Clusterable {
	/** The cluster manager to interact with cluster table. */
	private static GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** The node manager to interact with node table. */
	private static GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(OracleNoSQLCluster.class);

	/** The error message. */
	private String errorMessage = null;

	/**
	 * Validate cluster configuration.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean validate(ClusterConf clusterConf) {
		OracleNoSQLClusterConf config = (OracleNoSQLClusterConf) clusterConf;

		logger.info("Validating...");
		boolean status = true;

		// Validate NTP server
		Validator ntpValidator = new NTPServerValidator(config
				.getOracleNoSQLConf().getNtpServer());
		if (!ntpValidator.validate()) {
			status = false;
			logger.error("NTP server validation failed.");
			config.addError("ntpServer", "Invalid NTP server.");
		}

		// Validate nodes
		return status && this.validate(clusterConf, config.getNodeConfs());
	}

	/**
	 * Save cluster and node configuration into database.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean saveClusterDetails(ClusterConf clusterConf) {

		// Create and save cluster
		Cluster cluster = new Cluster();
		cluster.setName(clusterConf.getClusterName());
		cluster.setTechnology(clusterConf.getTechnology());
		cluster.setEnvironment(clusterConf.getEnvironment());
		cluster.setState(clusterConf.getState());
		cluster.setClusterConf(clusterConf);
		cluster.setCreatedAt(new Date());
		cluster.setUser(clusterConf.getCurrentUser());
		cluster.setAlertConf(new AlertsConf());
		try {
			cluster = clusterManager.save(cluster);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			return false;
		}

		// Set cluster and operation id
		clusterConf.setClusterId(cluster.getId());
		clusterConf.setOperationId(logger.getNewOperationId(cluster.getId()));
		logger.setCluster(clusterConf);

		// Create and save nodes
		return updateNodeTable(clusterConf.getNodeConfs(), clusterConf, false);
	}

	/**
	 * Save or update node details.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @param remove
	 *            the remove
	 * @return true, if successful
	 */
	private boolean updateNodeTable(List<NodeConf> nodeConfs,
			ClusterConf clusterConf, boolean remove) {
		try {
			// Create and save nodes
			for (NodeConf nodeConf : nodeConfs) {
				if (remove) {
					// Remove node
					nodeManager.deleteAllByPropertyValue(
							Constant.Keys.PUBLICIP, nodeConf.getPublicIp());
					continue;
				}
				Node node = nodeManager.getByPropertyValueGuarded(
						Constant.Keys.PUBLICIP, nodeConf.getPublicIp());

				// Add Node
				if (node == null) {
					node = new Node();
					node.setCreatedAt(new Date());
					node.setState(clusterConf.getState());
					node.setPublicIp(nodeConf.getPublicIp());
					node.setPrivateIp(nodeConf.getPrivateIp());
					node.setClusterId(clusterConf.getClusterId());
				}

				// Update node
				node.setState(nodeConf.getNodeState());
				node.setNodeConf(nodeConf);

				// Save node
				node = nodeManager.save(node);
				nodeConf.setId(node.getId());
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * Deployment pre-processing tasks.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean preDeploy(ClusterConf clusterConf) {
		return true;
	}

	/**
	 * Create component configurations.
	 * 
	 * @param newClusterConf
	 *            the new cluster conf
	 * @param clusterConf
	 *            the cluster conf
	 * @return the map
	 */
	@Override
	public Map<String, Configuration> createConfigs(ClusterConf newClusterConf,
			ClusterConf clusterConf) {
		return setComponents((OracleNoSQLClusterConf) newClusterConf,
				(OracleNoSQLClusterConf) clusterConf);
	}

	/**
	 * Deployment post processing tasks.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean postDeploy(ClusterConf clusterConf) {
		if (clusterConf.getState().equals(Constant.Cluster.State.ERROR)) {
			logger.error("Could not deploy the cluster.");
		} else {
			logger.info("Cluster deployed.");
		}
		// Update cluster details.
		return updateClusterDetails(clusterConf);
	}

	/**
	 * Undeployment pre-processing tasks.
	 * 
	 * @param dbCluster
	 *            the db cluster
	 * @return the cluster conf
	 */
	@Override
	public ClusterConf preUndeploy(Cluster dbCluster) {
		return dbCluster.getClusterConf();
	}

	/**
	 * Undeployment post processing tasks.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 */
	@Override
	public void postUndeploy(ClusterConf clusterConf) {
		// TODO Auto-generated method stub
	}

	/**
	 * Get error message.
	 * 
	 * @return the error
	 */
	@Override
	public String getError() {
		return errorMessage;
	}

	/**
	 * Check error status by checking status of each nodes.
	 * 
	 * @param nodeConfs
	 *            list of node configuration
	 * @return true, if is error
	 */
	private boolean isError(List<NodeConf> nodeConfs) {
		for (NodeConf nodeConf : nodeConfs) {
			if (!nodeConf.getStatus()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Set components configuration.
	 * 
	 * @param newClusterConf
	 *            the new cluster conf
	 * @param conf
	 *            Cluster configuration
	 * @return the map
	 */
	private Map<String, Configuration> setComponents(
			OracleNoSQLClusterConf newClusterConf, OracleNoSQLClusterConf conf) {
		// Set cluster configuration if not set
		conf.getOracleNoSQLConf().setClusterConf(conf);

		Map<String, Configuration> components = new HashMap<String, Configuration>();

		// create Ganglia conf
		if (conf.getGangliaMaster() == null) {
			for (OracleNoSQLNodeConf nodeConf : conf.getOracleNoSQLConf()
					.getNodes()) {
				if (nodeConf.isAdmin()) {
					conf.setGangliaMaster(nodeConf);
					break;
				}
			}
		}

		// Add Oracle component
		OracleNoSQLConf nosqlConf = conf.getOracleNoSQLConf();

		// if state is adding/removing nodes then put the new nodes in conf.
		if (newClusterConf != null) {
			nosqlConf.setNewNodes(this.getNodes(newClusterConf));
		}

		components.put(Constant.Technology.ORACLE_NOSQL, nosqlConf);

		// create Dependency conf
		Set<NodeConf> nodeConfs = new HashSet<NodeConf>(conf.getNodeConfs());
		DependencyConf dependencyConf = new DependencyConf();
		dependencyConf.setClusterConf(conf);
		dependencyConf.setComponents(new HashMap(components));
		dependencyConf.setNodes(nodeConfs);
		components.put(Constant.Component.Name.DEPENDENCY, dependencyConf);

		return components;
	}

	/**
	 * Add node pre-processing task.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean preAddNodes(List<NodeConf> nodeConfs, ClusterConf clusterConf) {
		clusterConf.setOperationId(logger.getNewOperationId(clusterConf
				.getClusterId()));
		for (NodeConf nodeConf : nodeConfs) {
			// Check if node is in use
			try {
				if (nodeManager.getByPropertyValueGuarded("publicIp",
						nodeConf.getPublicIp()) != null) {
					if (errorMessage == null) {
						errorMessage = "Node '" + nodeConf.getPublicIp()
								+ "' is already in use.";
					} else {
						errorMessage += " Node '" + nodeConf.getPublicIp()
								+ "' is already in use.";
					}
				}
			} catch (Exception e) {
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage == null) {
			return true;
		}
		return false;
	}

	/**
	 * Remove node pre-processing tasks.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean preRemoveNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		return true;
	}

	/**
	 * Remove node post processing tasks.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean postRemoveNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		OracleNoSQLClusterConf conf = (OracleNoSQLClusterConf) clusterConf;

		// Remove node form cluster conf
		for (NodeConf nodeConf : nodeConfs) {
			conf.getOracleNoSQLConf().getNodes().remove(nodeConf);
		}
		conf.setState(Constant.Cluster.State.DEPLOYED);

		// Update cluster table
		return updateClusterTable(conf, nodeConfs, true);
	}

	/**
	 * Add node post processing tasks.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean postAddNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {

		OracleNoSQLClusterConf conf = (OracleNoSQLClusterConf) clusterConf;
		boolean isError = false;

		// Set error flag
		if (clusterConf.getState().equals(Constant.Cluster.State.ERROR)) {
			isError = true;
			logger.error("Could not add nodes.");
		} else {
			logger.info("Nodes added.");
		}

		// Update nodeConf
		Map<String, String> nodeMessages = new OracleNoSQLMonitor()
				.getDbNodeMessages(conf.getClusterId());
		List<OracleNoSQLNodeConf> newNodes = new ArrayList<OracleNoSQLNodeConf>();

		for (NodeConf nodeConf : nodeConfs) {
			if (isError) {
				nodeConf.setNodeState(Constant.Node.State.ERROR);
			} else {
				nodeConf.setNodeState(Constant.Node.State.DEPLOYED);
			}
			nodeConf.setMessage(nodeMessages.get(nodeConf.getPublicIp()));
			conf.getOracleNoSQLConf().getNodes()
					.add((OracleNoSQLNodeConf) nodeConf);
			newNodes.add((OracleNoSQLNodeConf) nodeConf);
		}

		conf.setState(Constant.Cluster.State.DEPLOYED);
		// Set New node conf
		conf.setNewNodes(newNodes);

		// Update cluster table
		updateClusterTable(conf, nodeConfs, isError);

		return true;
	}

	/**
	 * Update cluster details.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConfs
	 *            the node confs
	 * @param removeNode
	 *            the remove node
	 * @return true, if successful
	 */
	private boolean updateClusterTable(OracleNoSQLClusterConf conf,
			List<NodeConf> nodeConfs, boolean removeNode) {
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
		// Set cluster conf reference
		conf.getOracleNoSQLConf().setClusterConf(conf);

		cluster.setState(conf.getState());
		cluster.setClusterConf(conf);

		try {
			cluster = clusterManager.save(cluster);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return updateNodeTable(nodeConfs, conf, removeNode);
	}

	/**
	 * Save node configuration.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean saveNodeDetails(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		// Set cluster and operation id
		logger.setCluster(clusterConf);

		OracleNoSQLClusterConf conf = (OracleNoSQLClusterConf) clusterConf;
		for (NodeConf nodeConf : nodeConfs) {
			nodeConf.setNodeState(Constant.Node.State.ADDING);
		}
		conf.getOracleNoSQLConf().setNewNodes(nodeConfs);

		// Save into database
		if (!updateClusterTable(conf, nodeConfs, false)) {
			return false;
		}

		return true;
	}

	/**
	 * Update cluster configuration.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean updateClusterDetails(ClusterConf clusterConf) {

		// Update node configuration
		for (NodeConf nodeConf : clusterConf.getNodeConfs()) {
			nodeConf.setNodeState(clusterConf.getState());
		}

		// Update cluster table
		return updateClusterTable((OracleNoSQLClusterConf) clusterConf,
				clusterConf.getNodeConfs(), false);
	}

	/**
	 * Get cluster configuration.
	 * 
	 * @param dbCluster
	 *            the db cluster
	 * @return the cluster conf
	 */
	@Override
	public ClusterConf getClusterConf(Cluster dbCluster) {
		return preUndeploy(dbCluster);
	}

	/**
	 * Get list of nodes.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return the nodes
	 */
	@Override
	public List<NodeConf> getNodes(ClusterConf clusterConf) {
		return new ArrayList<NodeConf>(
				((OracleNoSQLClusterConf) clusterConf).getNewNodes());
	}

	/**
	 * Validate node configuration.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param nodeConfs
	 *            the node confs
	 * @return true, if successful
	 */
	@Override
	public boolean validate(ClusterConf clusterConf, List<NodeConf> nodeConfs) {
		final OracleNoSQLConf config = ((OracleNoSQLClusterConf) clusterConf)
				.getOracleNoSQLConf();

		logger.info("Validating...");
		boolean status = true;

		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		try {
			for (final NodeConf nc : nodeConfs) {
				final OracleNoSQLNodeConf nodeConf = (OracleNoSQLNodeConf) nc;
				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						// Validate nodes
						OracleNoSQLNodeValidator validator = new OracleNoSQLNodeValidator();
						if (validator.validate(config, nodeConf)) {
							nodeConf.setStatus(true);
						} else {
							nodeConf.setErrors((HashMap<String, String>) validator
									.getErrorMessages());
							nodeConf.setStatus(false);
						}
						if (semaphore != null) {
							semaphore.release();
						}
					}

				});
			}
			semaphore.acquire(nodeConfs.size());
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

		return (status && isError(nodeConfs));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#createConfigs(com.impetus
	 * .ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public Map<String, Configuration> createConfigs(ClusterConf clusterConf) {
		return createConfigs(null, clusterConf);
	}
}
