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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ClusterValidator;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class ClusterValidator.
 * 
 * @author hokam chauhan
 */
public class StormClusterValidator extends ClusterValidator {

	private static final String ERROR = "error";
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(StormClusterValidator.class);

	private Map<String, Boolean> statusMap = new HashMap<String, Boolean>();

	/**
	 * Validate.
	 * 
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	public boolean validate(StormClusterConf conf) {

		logger.setCluster(conf);
		logger.info("Validating...");
		try {
			this.conf = conf;
			this.javaConf = conf.getJavaConf();
			Semaphore semaphore = new Semaphore(conf.getComponents().size());
			for (String componentName : conf.getComponents().keySet()) {
				semaphore.acquire();
				ComponentValidator task = new ComponentValidator(componentName,
						conf, semaphore, conf.getComponents()
								.get(componentName));
				AppStoreWrapper.getExecutor().execute(task);
			}
			semaphore.acquire(conf.getComponents().size());
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return !conf.isStateError();
	}

	/**
	 * The Class ComponentValidator.
	 */
	class ComponentValidator extends Thread {

		/** The semaphore. */
		private Semaphore semaphore;

		/** The comp conf. */
		private GenericConfiguration compConf;

		/** The component name. */
		private String componentName;

		/**
		 * Instantiates a new component validator.
		 * 
		 * @param componentName
		 *            the component name
		 * @param conf
		 *            the conf
		 * @param semaphore
		 *            the semaphore
		 * @param compConf
		 *            the comp conf
		 */
		public ComponentValidator(String componentName, StormClusterConf conf,
				Semaphore semaphore, GenericConfiguration compConf) {
			super();
			this.componentName = componentName;
			this.semaphore = semaphore;
			this.compConf = compConf;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			try {
				Semaphore semaphore = new Semaphore(compConf.getCompNodes()
						.size());
				for (NodeConf node : compConf.getCompNodes()) {
					semaphore.acquire();
					NodeValidator task = new NodeValidator(semaphore, node,
							compConf, componentName);
					AppStoreWrapper.getExecutor().execute(task);
				}
				semaphore.acquire(compConf.getCompNodes().size());
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (this.semaphore != null) {
					this.semaphore.release();
				}
			}
		}

	}

	/**
	 * Validate.
	 * 
	 * @param nodeConfs
	 *            the node confs
	 * @param clusterConf
	 *            the cluster conf
	 * @return true, if successful
	 */
	public boolean validate(List<NodeConf> nodeConfs, ClusterConf clusterConf) {

		logger.setCluster(clusterConf);
		StormClusterConf conf = (StormClusterConf) clusterConf;
		this.conf = conf;
		this.javaConf = conf.getJavaConf();
		String componentName = Constant.Component.Name.STORM;
		try {
			Semaphore semaphore = new Semaphore(nodeConfs.size());
			for (NodeConf node : nodeConfs) {
				semaphore.acquire();
				NodeValidator task = new NodeValidator(semaphore, node, conf
						.getComponents().get(componentName), componentName);
				AppStoreWrapper.getExecutor().execute(task);
			}
			semaphore.acquire(nodeConfs.size());
			if (this.conf.getState().equals(Constant.Cluster.State.ERROR)) {
				logger.error("Nodes validation failed.");
			} else {
				logger.info("Nodes validated successfully.");
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return !conf.isStateError();
	}

	/**
	 * The Class NodeValidator.
	 */
	class NodeValidator extends Thread {

		private String componentName;

		/** The semaphore. */
		private Semaphore semaphore;

		/** The node. */
		private NodeConf node;

		/** The comp conf. */
		private GenericConfiguration compConf;

		/**
		 * Instantiates a new node validator.
		 * 
		 * @param semaphore
		 *            the semaphore
		 * @param node
		 *            the node
		 * @param compConf
		 *            the comp conf
		 */
		public NodeValidator(Semaphore semaphore, NodeConf node,
				GenericConfiguration compConf, String componentName) {
			this.componentName = componentName;
			this.semaphore = semaphore;
			this.node = node;
			this.compConf = compConf;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			SSHExec connection = null;
			try {
				connection = SSHUtils.connectToNode(node.getPublicIp(),
						compConf.getUsername(), compConf.getPassword(),
						compConf.getPrivateKey());

				if (connection != null) {

					validatingPathPermissions(componentName, connection,
							compConf, node);

					validatingComponentPaths(connection, componentName,
							compConf, node);

					if (!statusMap.containsKey(node.getPublicIp())) {
						statusMap.put(node.getPublicIp(), true);
						validateJavaHome(connection, node);
					}

				} else {
					conf.setState(Constant.Cluster.State.ERROR);
					logger.error("Authenication failed.");
					this.node.setNodeState(ERROR);
					this.node.addError("authentication",
							"Authentication failed");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (semaphore != null) {
					semaphore.release();
				}
				if (connection != null) {
					connection.disconnect();
				}
			}
		}
	}
}
