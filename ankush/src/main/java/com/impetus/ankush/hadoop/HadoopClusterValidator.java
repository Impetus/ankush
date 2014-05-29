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

import java.util.List;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.validator.ValidationResult;
import com.impetus.ankush.common.utils.validator.ValidationUtility;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;

/**
 * The Class HadoopClusterValidator.
 * 
 * @author hokam chauhan
 */
public class HadoopClusterValidator {

	/* Logger object */
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(HadoopClusterValidator.class);

	/** The conf. */
	private HadoopClusterConf conf = null;

	/**
	 * Validate.
	 * 
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	public boolean validate(HadoopClusterConf conf) {

		logger.setCluster(conf);
		logger.info("Validating...");

		try {
			this.conf = conf;
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

		return !conf.isStatusError();
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
		public ComponentValidator(String componentName, HadoopClusterConf conf,
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
				for (NodeConf node : compConf.getCompNodes()) {

					SSHExec connection = SSHUtils.connectToNode(
							node.getPublicIp(), conf.getAuthConf());

					if (connection != null) {
						validatingPathPermissions(componentName, connection,
								compConf, node);

						validatingComponentPaths(connection, componentName,
								compConf, node);

						if ((componentName
								.equals(Constant.Component.Name.HADOOP))
								|| (componentName
										.equals(Constant.Component.Name.HADOOP2))) {
							validateJavaHome(connection, node);
						}
						connection.disconnect();
					} else {
						conf.setState(Constant.Cluster.State.ERROR);
						logger.error("Authenication failed.");
						node.setNodeState("error");
						node.addError("authentication", "Authentication failed");
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				if (semaphore != null) {
					semaphore.release();
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
		logger.info("Validating Nodes...");
		this.conf = (HadoopClusterConf) clusterConf;
		try {
			Semaphore semaphore = new Semaphore(nodeConfs.size());
			for (NodeConf node : nodeConfs) {
				semaphore.acquire();
				NodeValidator task = new NodeValidator(semaphore, node,
						this.conf.getComponents().get(
								Constant.Component.Name.HADOOP));
				AppStoreWrapper.getExecutor().execute(task);
			}
			semaphore.acquire(nodeConfs.size());
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return !conf.isStatusError();
	}

	/**
	 * The Class NodeValidator.
	 */
	class NodeValidator extends Thread {

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
				GenericConfiguration compConf) {
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
				logger.info("Validating nodes...");
				String componentName = Constant.Component.Name.HADOOP;

				if (conf.getComponents().get(componentName) == null) {
					componentName = Constant.Component.Name.HADOOP2;
				}

				connection = SSHUtils.connectToNode(node.getPublicIp(),
						conf.getAuthConf());

				if (connection != null) {

					validatingPathPermissions(componentName, connection,
							compConf, node);

					validatingComponentPaths(connection, componentName,
							compConf, node);

					validateJavaHome(connection, node);

				} else {
					conf.setState(Constant.Cluster.State.ERROR);
					logger.error("Authenication failed.");
					this.node.setNodeState("error");
					this.node.addError("authentication",
							"Authentication failed.");
				}

				if (conf.getState().equals(Constant.Cluster.State.ERROR)) {
					logger.error("Nodes validation failed.");
				} else {
					logger.info("Nodes validated successfully.");
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
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

	/**
	 * Validate java home.
	 * 
	 * @param connection
	 *            the connection
	 * @param node
	 *            the node
	 */
	private void validateJavaHome(SSHExec connection, NodeConf node) {

		ValidationResult status = null;
		// validating Java_Home/Java bundle path
		if (!conf.getJavaConf().isInstall()) {

			logger.info(node.getPublicIp(), "Validating java home path...");
			String javaBinPath = conf.getJavaConf().getJavaBinPath();

			status = ValidationUtility.isFileExists(connection, javaBinPath);

			if (!status.isStatus()) {
				conf.setState("error");
				node.setStatus(false);
				node.setNodeState("error");
				logger.error(node, "Validating java home path failed...");
				node.addError("javaHome", status.getMessage());
			}
		}
	}

	/**
	 * Validating component paths.
	 * 
	 * @param connection
	 *            the connection
	 * @param componentName
	 *            the component name
	 * @param compInfo
	 *            the comp info
	 * @param node
	 *            the node
	 */
	private void validatingComponentPaths(SSHExec connection,
			String componentName, GenericConfiguration compInfo, NodeConf node) {

		ValidationResult status = null;
		// Validating the bundle path for the component.

		if (compInfo.getLocalBinaryFile() != null
				&& !compInfo.getLocalBinaryFile().equals("")) {

			logger.info(node.getPublicIp(), "Validating " + componentName
					+ " local bundle path...");
			status = ValidationUtility.isFileExists(connection,
					compInfo.getLocalBinaryFile());

			if (!status.isStatus()) {
				conf.setState("error");
				node.setStatus(false);
				node.setNodeState("error");
				logger.error(node, "Validating " + componentName
						+ " local bundle path failed...");
				node.addError(componentName + "_localBinaryFile",
						status.getMessage());
			}

		}

		// Validating the tarball url for the component.
		if (compInfo.getTarballUrl() != null
				&& !compInfo.getTarballUrl().equals("")) {

			logger.info(node.getPublicIp(), "Validating " + componentName
					+ " download url...");
			status = ValidationUtility.validateDownloadUrl(connection,
					compInfo.getTarballUrl());

			if (!status.isStatus()) {
				conf.setState("error");
				node.setNodeState("error");
				node.setStatus(false);
				logger.error(node, "Validating " + componentName
						+ " download url failed...");
				node.addError(componentName + "_tarballUrl",
						status.getMessage());
			}
		}
	}

	/**
	 * Validating path permissions.
	 * 
	 * @param componentName
	 *            the component name
	 * @param connection
	 *            the connection
	 * @param compInfo
	 *            the comp info
	 * @param node
	 *            the node
	 */
	private void validatingPathPermissions(String componentName,
			SSHExec connection, GenericConfiguration compInfo, NodeConf node) {

		logger.info(node.getPublicIp(), "Validating " + componentName
				+ " installation path...");
		ValidationResult status = null;
		// Validating the installation path for the component.
		status = ValidationUtility.validatePathPermissions(connection,
				compInfo.getInstallationPath(), true);

		if (!status.isStatus()) {
			conf.setState("error");
			node.setNodeState("error");
			node.setStatus(false);
			node.addError(componentName + "_installationPath",
					status.getMessage());
			logger.error(node, "Validating " + componentName
					+ " installation path failed...");
		}
	}
}
