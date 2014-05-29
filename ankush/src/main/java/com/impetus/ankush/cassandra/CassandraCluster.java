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
package com.impetus.ankush.cassandra;

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
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.dependency.DependencyConf;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.Clusterable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.oraclenosql.OracleNoSQLMonitor;

/**
 * The Class CassandraCluster.
 */
public class CassandraCluster implements Clusterable {

	/** Generic cluster master. */
	static private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** Generic node master. */
	static private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#validate(com.impetus.
	 * ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean validate(ClusterConf clusterConf) {

		// Casting ClusterConf to CassandraClusterConf
		CassandraClusterConf conf = (CassandraClusterConf) clusterConf;
		return validate(clusterConf, conf.getNodeConfs());
	}

	private ClusterConf clusterConf = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#validate(com.impetus.
	 * ankush.common.framework.config.ClusterConf, java.util.List)
	 */
	@Override
	public boolean validate(ClusterConf conf, List<NodeConf> nodeConfs) {
		logger.setCluster(conf);
		logger.info("Validating...");
		try {
			this.clusterConf = conf;
			final Semaphore semaphore = new Semaphore(nodeConfs.size());

			/* Validate nodes */
			for (final NodeConf nodeConf : nodeConfs) {
				final CassandraNodeConf nc = (CassandraNodeConf) nodeConf;
				// establishing connection with node
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {

						// Create validator object
						CassandraClusterValidator validator = new CassandraClusterValidator();
						if (validator.validate(clusterConf, nc)) {
							nodeConf.setStatus(true);
						} else {
							logger.debug("validation failed");
							nodeConf.setErrors(validator.getErrorMessages());
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
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return this.isError(nodeConfs);
	}

	/**
	 * Check error status
	 * 
	 * @param nodeConfs
	 * @return
	 */
	private boolean isError(List<NodeConf> nodeConfs) {
		for (final NodeConf nodeConf : nodeConfs) {
			if (!nodeConf.getStatus()) {
				return false;
			}
		}
		return true;
	}

	/* Logger object */
	/** The logger. */
	private final AnkushLogger logger = new AnkushLogger(CassandraCluster.class);

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

	private boolean saveOrUpdateNodeDetails(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		return saveOrUpdateNodeDetails(nodeConfs, clusterConf, false);
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
			ClusterConf clusterConf, boolean remove) {
		try {
			// cassandra cluster conf //
			CassandraClusterConf conf = (CassandraClusterConf) clusterConf;
			// Create and save nodes
			for (NodeConf cassandraNode : nodeConfs) {
				if (remove) {
					// Remove node
					nodeManager
							.deleteAllByPropertyValue(Constant.Keys.PUBLICIP,
									cassandraNode.getPublicIp());
					continue;
				}
				CassandraNodeConf cNode = (CassandraNodeConf) cassandraNode;
				Node node = nodeManager.getByPropertyValueGuarded(
						Constant.Keys.PUBLICIP, cassandraNode.getPublicIp());

				// if null create empty object.
				if (node == null) {
					node = new Node();
					node.setCreatedAt(new Date());
					node.setState(Constant.Node.State.DEPLOYING);
					node.setClusterId(conf.getClusterId());
					node.setPrivateIp(cassandraNode.getPrivateIp());
					node.setPublicIp(cassandraNode.getPublicIp());
					node.setRackInfo(cNode.getRackInfo());
					if (cNode.isSeedNode()) {
						cassandraNode.setType(Constant.Role.CASSANDRA_SEED);
					} else {
						cassandraNode.setType(Constant.Role.CASSANDRA_NON_SEED);
					}
				} else {
					node.setState(cassandraNode.getNodeState());
				}

				// Setting node details
				node.setNodeConf(cassandraNode);
				node = nodeManager.save(node);

				// Setting Node Id
				cassandraNode.setId(node.getId());
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
		// Casting ClusterConf to CassandraConf
		return setComponents((CassandraClusterConf) newClusterConf,
				(CassandraClusterConf) clusterConf);
	}

	private Map<String, Configuration> setComponents(
			CassandraClusterConf newClusterConf, CassandraClusterConf conf) {

		CassandraConf cassandraConf = conf.getCassandra();
		cassandraConf.setClusterConf(conf);
		cassandraConf.setRackEnabled(conf.isRackEnabled());

		// set ganglia master.
		if (conf.getGangliaMaster() == null) {
			for (CassandraNodeConf nodeConf : cassandraConf.getNodes()) {
				if (nodeConf.isSeedNode()) {
					conf.setGangliaMaster(nodeConf);
					break;
				}
			}
		}

		if (newClusterConf != null) {
			cassandraConf.setNewNodes(getNodes(newClusterConf));
		}

		// Adding cassandra component
		Map<String, Configuration> components = new HashMap<String, Configuration>();
		components.put(Constant.Technology.CASSANDRA, cassandraConf);

		// create and add dependency component
		final DependencyConf dependencyConf = new DependencyConf();
		dependencyConf.setInstallJava(conf.getJavaConf().isInstall());
		dependencyConf.setJavaBinFileName(conf.getJavaConf().getJavaBundle());
		dependencyConf.setClusterConf(conf);
		dependencyConf.setComponents(new HashMap(components));
		dependencyConf.setNodes(new HashSet<NodeConf>(conf.getNodeConfs()));

		components.put(Constant.Component.Name.DEPENDENCY, dependencyConf);

		return components;
	}

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
	 * com.impetus.ankush.common.framework.Clusterable#postUndeploy(com.impetus
	 * .ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public void postUndeploy(ClusterConf clusterConf) {
		// Do post undeploy operations
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
	 * com.impetus.ankush.common.framework.Clusterable#preAddNodes(java.util
	 * .List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preAddNodes(List<NodeConf> nodeConfs, ClusterConf clusterConf) {
		clusterConf.setOperationId(logger.getNewOperationId(clusterConf
				.getClusterId()));

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

		boolean isError = false;
		if (clusterConf.getState().equals(Constant.Cluster.State.ERROR)) {
			isError = true;
			logger.error("Could not add nodes.");
		} else {
			logger.info("Nodes added.");
		}

		// Cast to cassandra cluster conf.
		CassandraClusterConf conf = (CassandraClusterConf) clusterConf;

		// final List<NodeConf> newNodes = new ArrayList<NodeConf>();
		final List<CassandraNodeConf> newNodes = new ArrayList<CassandraNodeConf>();

		Map<String, String> nodeMessages = new OracleNoSQLMonitor()
				.getDbNodeMessages(conf.getClusterId());
		for (final NodeConf nodeConf : nodeConfs) {
			nodeConf.setMessage(nodeMessages.get(nodeConf.getPublicIp()));

			CassandraNodeConf cassandraNode = (CassandraNodeConf) nodeConf;
			newNodes.add(cassandraNode);

			if (isError) {
				nodeConf.setNodeState(Constant.Node.State.ERROR);
			} else {
				nodeConf.setNodeState(Constant.Node.State.DEPLOYED);
				conf.getCassandra().getCompNodes().add(nodeConf);
				conf.getCassandra().getNodes().add(cassandraNode);
			}
		}

		// Update database
		conf.setState(Constant.Cluster.State.DEPLOYED);
		conf.setNewNodes(newNodes);
		updateClusterDetails(conf);
		return saveOrUpdateNodeDetails(nodeConfs, conf, isError);
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
		// getting database cassandra configuration
		CassandraClusterConf conf = (CassandraClusterConf) clusterConf;
		// list containing node details saved in database
		Set<CassandraNodeConf> newNodeConfs = new HashSet<CassandraNodeConf>();
		Set<String> nodeIpList = new HashSet<String>();

		// Removing node from database
		for (NodeConf nodeConf : nodeConfs) {
			nodeIpList.add(nodeConf.getPublicIp());
			try {
				nodeManager.deleteAllByPropertyValue(Constant.Keys.PUBLICIP,
						nodeConf.getPublicIp());
			} catch (Exception e) {
				logger.error(
						"ERROR : Could not remove entry from database.. !", e);
			}
		}

		// Remove nodes from cassandra configuration
		for (CassandraNodeConf nodeConf : conf.getCassandra().getNodes()) {
			if (!nodeIpList.contains(nodeConf.getPublicIp())) {
				newNodeConfs.add(nodeConf);
			}
		}

		conf.getCassandra().setNodes(newNodeConfs);
		conf.getCassandra().setNewNodes(null);
		conf.setState(Constant.Cluster.State.DEPLOYED);
		return updateClusterDetails(conf);
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
				((CassandraClusterConf) clusterConf).getNewNodes());
	}

	@Override
	public boolean saveNodeDetails(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		// setting new nodes in db
		CassandraClusterConf cassandraClusterConf = (CassandraClusterConf) clusterConf;

		// setting new nodes.
		cassandraClusterConf.getCassandra().setNewNodes(nodeConfs);

		if (updateCluster(clusterConf)) {
			// save new nodes in db.
			return saveOrUpdateNodeDetails(nodeConfs, clusterConf);
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
			if (cluster == null) {
				logger.error("Failed to fetch cluster from database.");
				return false;
			}
			cluster.setState(conf.getState());
			cluster.setClusterConf(conf);
			cluster = clusterManager.save(cluster);
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
	 * com.impetus.ankush.common.framework.Clusterable#preRemoveNodes(java.util
	 * .List, com.impetus.ankush.common.framework.config.ClusterConf)
	 */
	@Override
	public boolean preRemoveNodes(List<NodeConf> nodeConfs,
			ClusterConf clusterConf) {
		clusterConf.setOperationId(logger.getNewOperationId(clusterConf
				.getClusterId()));
		// Cast to cassandra cluster conf where clusterConf is taken from the
		// database.
		CassandraClusterConf cassandraConf = (CassandraClusterConf) clusterConf;
		int seedNodeCount = 0;
		boolean isUp = false;
		Set<String> nodeIpList = new HashSet<String>();

		// Check if it is ganglia master or not.
		for (NodeConf nodeConf : nodeConfs) {
			if (nodeConf.getPublicIp().equals(
					clusterConf.getGangliaMaster().getPublicIp())) {
				error = nodeConf.getPublicIp()
						+ " is ganglia master node. It cannot be deleted";
				return false;
			}
			nodeIpList.add(nodeConf.getPublicIp());
		}

		// Check for seed node
		for (CassandraNodeConf nodeConf : cassandraConf.getCassandra()
				.getNodes()) {
			if (nodeIpList.contains(nodeConf.getPublicIp())) {
				nodeConf.setNodeState(Constant.Node.State.REMOVING);
			} else {
				NodeMonitoring monitoring = new MonitoringManager()
						.getMonitoringData(nodeConf.getPublicIp());
				if (monitoring != null) {
					Map<String, Boolean> serviceStatus = monitoring
							.getServiceStatus();
					if (serviceStatus
							.containsKey(Constant.Component.ProcessName.CASSANDRA)
							&& serviceStatus
									.get(Constant.Component.ProcessName.CASSANDRA)) {
						isUp = true;
					}
				}

				if (nodeConf.isSeedNode()) {
					seedNodeCount++;
				}
			}
		}

		if (seedNodeCount == 0) {
			error = "Cannot delete all seednodes. There must be atleast 1 seednode in the cluster";
			return false;
		}

		if (!isUp) {
			error = "Could not found any up node to delete the nodes.";
			return false;
		}

		// Update database
		if (updateCluster(clusterConf)) {
			return saveOrUpdateNodeDetails(clusterConf.getNodeConfs(),
					clusterConf);
		}
		return false;
	}

	/**
	 * Update cluster details.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	@Override
	public boolean updateClusterDetails(ClusterConf clusterConf) {
		try {
			// Get saved cluster object and change its status/state
			logger.info("Updating cluster and nodes information...");
			// Update node configuration
			for (NodeConf nodeConf : clusterConf.getNodeConfs()) {
				nodeConf.setNodeState(clusterConf.getState());
			}

			updateCluster(clusterConf);

			saveOrUpdateNodeDetails(clusterConf.getNodeConfs(), clusterConf);
			return true;
		} catch (final Exception e) {
			this.error = e.getMessage();
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Clusterable#preUndeploy(com.impetus
	 * .ankush.common.domain.Cluster)
	 */
	@Override
	public ClusterConf preUndeploy(Cluster dbCluster) {
		// getting cluster conf obj.
		return dbCluster.getClusterConf();
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

	@Override
	public Map<String, Configuration> createConfigs(ClusterConf clusterConf) {
		return createConfigs(null, clusterConf);
	}

}
