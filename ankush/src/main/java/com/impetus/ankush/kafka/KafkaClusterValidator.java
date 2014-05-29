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
package com.impetus.ankush.kafka;

import java.util.HashMap;
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

/**
 * The class KafkaClusterValidator
 * 
 * @author monika
 * 
 */
public class KafkaClusterValidator {

	/** The conf */
	private KafkaClusterConf conf = null;
	/** The errorMessages */
	private HashMap<String, String> errorMessages = new HashMap<String, String>();

	/** The VALIDATING string. */
	private static final String VALIDATING = "Validating ";
	
	/** The ERROR string. */
	private static final String ERROR = "error";

	/** The logger */
	AnkushLogger logger = new AnkushLogger(KafkaClusterValidator.class);

	/**
	 * method to validate kafkaClusterConfiguration
	 * 
	 * @param conf
	 * @return
	 */
	public boolean validate(KafkaClusterConf conf) {

		logger.setCluster(conf);
		logger.info("Validating...");
		try {
			this.conf = conf;
			final Semaphore semaphore = new Semaphore(conf.getComponents()
					.size());
			/* Validate nodes */
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
		public ComponentValidator(String componentName, KafkaClusterConf conf,
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
			SSHExec connection = null;
			try {
				for (NodeConf node : compConf.getCompNodes()) {
					connection = SSHUtils.connectToNode(node.getPublicIp(),
							conf.getUsername(), conf.getPassword(),
							conf.getPrivateKey());

					if (connection != null) {
						validatingPathPermissions(componentName, connection,
								compConf, node);
						validatingComponentPaths(connection, componentName,
								compConf, node);

						validatingJava(connection, node);
						connection.disconnect();
					} else {
						conf.setState(Constant.Cluster.State.ERROR);
						logger.error("Authenication failed.");
						node.setNodeState(ERROR);
						node.addError("authentication", "Authentication failed");
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				if (semaphore != null) {
					semaphore.release();
				}
			}
		}

	}

	/*
	 * Validate.
	 * 
	 * @param nodeConfs the node confs
	 * 
	 * @param clusterConf the cluster conf
	 * 
	 * @return true, if successful
	 */
	public boolean validate(List<NodeConf> nodeConfs, ClusterConf clusterConf) {

		logger.setCluster(clusterConf);
		logger.info("Validating Nodes...");
		this.conf = (KafkaClusterConf) clusterConf;
		try {
			Semaphore semaphore = new Semaphore(nodeConfs.size());
			for (NodeConf node : nodeConfs) {
				semaphore.acquire();
				NodeValidator task = new NodeValidator(semaphore, node,
						this.conf.getComponents().get(
								Constant.Component.Name.KAFKA));
				AppStoreWrapper.getExecutor().execute(task);
			}
			semaphore.acquire(nodeConfs.size());
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return !this.conf.isStateError();
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
				String componentName = Constant.Component.Name.KAFKA;

				connection = SSHUtils.connectToNode(node.getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());

				if (connection != null) {

					validatingPathPermissions(componentName, connection,
							compConf, node);

					validatingComponentPaths(connection, componentName,
							compConf, node);
					validatingJava(connection, node);

				} else {
					conf.setState(Constant.Cluster.State.ERROR);
					logger.error("Authenication failed.");
					this.node.setNodeState(ERROR);
					this.node.addError("authentication",
							"Authentication failed");
				}

				if (conf.getState().equals(Constant.Cluster.State.ERROR)) {
					logger.error("Nodes validation failed.");
				} else {
					logger.info("Nodes validated successfully.");
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				// disconncet to node/machine
				if (connection != null) {
					connection.disconnect();
				}
				
				if (semaphore != null) {
					semaphore.release();
				}
			}
		}
	}

	/**
	 * @return the errorMessages
	 */
	public HashMap<String, String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * Method to validate java
	 * 
	 * @param connection
	 */
	private void validatingJava(SSHExec connection, NodeConf node) {
		ValidationResult status = null;
		// validating Java_Home/Java bundle path
		if (!this.conf.getJavaConf().isInstall()) {

			logger.info(node.getPublicIp(), "Validating java home path...");
			String javaBinPath = conf.getJavaConf().getJavaBinPath();

			status = ValidationUtility.isFileExists(connection, javaBinPath);

			if (!status.isStatus()) {
				conf.setState("error");
				node.setNodeState(ERROR);
				node.setStatus(false);
				logger.error(node, "Validating java home path failed...");
				node.addError(node.getPublicIp(), status.getMessage());
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

			logger.info(node.getPublicIp(), VALIDATING + componentName
					+ " local bundle path...");
			status = ValidationUtility.isFileExists(connection,
					compInfo.getLocalBinaryFile());

			if (!status.isStatus()) {
				conf.setState(ERROR);
				node.setStatus(false);
				node.setNodeState(ERROR);
				logger.error(node, VALIDATING + componentName
						+ " local bundle path failed...");
				node.addError(componentName + "_localBinaryFile",
						status.getMessage());
			}

		}

		// Validating the tarball url for the component.
		if (compInfo.getTarballUrl() != null
				&& !compInfo.getTarballUrl().equals("")) {

			logger.info(node.getPublicIp(), VALIDATING + componentName
					+ " download url...");
			status = ValidationUtility.validateDownloadUrl(connection,
					compInfo.getTarballUrl());

			if (!status.isStatus()) {
				conf.setState(ERROR);
				node.setNodeState(ERROR);
				node.setStatus(false);
				logger.error(node, VALIDATING + componentName
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

		logger.info(node.getPublicIp(), VALIDATING + componentName
				+ " installation path...");
		ValidationResult status = null;
		// Validating the installation path for the component.
		status = ValidationUtility.validatePathPermissions(connection,
				compInfo.getInstallationPath(), true);

		if (!status.isStatus()) {
			conf.setState(ERROR);
			node.setNodeState(ERROR);
			node.setStatus(false);
			node.addError(componentName + "_installationPath",
					status.getMessage());
			logger.error(node, VALIDATING + componentName
					+ " installation path failed...");
		}
	}

}
