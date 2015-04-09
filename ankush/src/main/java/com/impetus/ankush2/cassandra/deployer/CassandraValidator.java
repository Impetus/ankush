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
package com.impetus.ankush2.cassandra.deployer;

import java.util.HashMap;
import java.util.Map;

import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.validator.DirectoryValidator;
import com.impetus.ankush.common.utils.validator.JavaValidator;
import com.impetus.ankush.common.utils.validator.ValidationResult;
import com.impetus.ankush.common.utils.validator.ValidationUtility;
import com.impetus.ankush.common.utils.validator.Validator;
import com.impetus.ankush2.cassandra.utils.CassandraConstants;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.ComponentConfig.SourceType;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.SSHUtils;

public class CassandraValidator {

	private AnkushLogger logger;
	private ClusterConfig clusterConfig;
	private NodeConfig nodeConfig;
	private ComponentConfig componentConfig;
	private boolean isError = false;

	public CassandraValidator(ClusterConfig clusterConfig, NodeConfig nodeConfig) {
		this.clusterConfig = clusterConfig;
		this.nodeConfig = nodeConfig;
		this.logger = new AnkushLogger(this.getClass(), this.clusterConfig);
		this.componentConfig = clusterConfig.getComponents().get(
				Constant.Component.Name.CASSANDRA);
	}

	public boolean validate() {
		try {
			logger.info("Validating " + Constant.Component.Name.CASSANDRA
					+ "...", Constant.Component.Name.CASSANDRA,
					nodeConfig.getHost());
			// validating node connection
			if (!validateConnection()) {
				return false;
			}

			// Validating whether bundle used for cluster deployment is of same
			// vendor as specified
			validateVendor();

			// validation paths
			validatePath();
			// Validating local bundle path and download url path
			validatingBundlePath();
			validateJava();
			validateJavaVersion();

		} catch (AnkushException e) {
			addError(e.getMessage(), e);
		} catch (Exception e) {
			addError("Could not validate " + Constant.Component.Name.CASSANDRA,
					e);
		}
		return !isError;
	}

	/** Validation connection with node */
	private boolean validateConnection() throws AnkushException {
		try {
			logger.debug("Validating connection.", nodeConfig.getHost());
			return (nodeConfig.getConnection().exec(new ExecCommand("ls")).rc == 0);
		} catch (TaskExecFailException e) {
			throw new AnkushException(
					"Could not execute task for validating node connection.");
		} catch (Exception e) {
			throw new AnkushException(
					"Could not validating connection on node : "
							+ nodeConfig.getHost());
		}
	}

	/**
	 * Validate path.
	 * 
	 * @param connection
	 *            the connection
	 */
	private void validateVendor() throws AnkushException {
		try {
			String vendor = null;
			if (componentConfig.getVendor().equalsIgnoreCase("Datastax")) {
				vendor = CassandraConstants.Cassandra_vendors.CASSANDRA_VENDOR_DSC;
			} else if (componentConfig.getVendor().equalsIgnoreCase("Apache")) {
				vendor = CassandraConstants.Cassandra_vendors.CASSANDRA_VENDOR_APACHE;
			} else {
				throw new AnkushException(componentConfig.getVendor()
						+ " vendor not supported.");
			}
			logger.info("Validating " + Constant.Component.Name.CASSANDRA
					+ " vendor...", Constant.Component.Name.CASSANDRA,
					nodeConfig.getHost());
			String errMsg = "The package provided for installation is of different vendor as specified";
			String[] fileParts = componentConfig.getSource().split("/");
			String[] packageParts = fileParts[fileParts.length - 1].split("-");
			if (!packageParts[0].equals(vendor)) {
				throw new AnkushException(errMsg);
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not validate "
					+ Constant.Component.Name.CASSANDRA + " vendor.");
		}
	}

	/**
	 * Validate path.
	 */
	private void validatePath() throws AnkushException {
		try {
			logger.info("Validating " + Constant.Component.Name.CASSANDRA
					+ " directories path.", nodeConfig.getHost());
			Validator pathValidator;
			StringBuilder errMsg = new StringBuilder("");
			for (Map.Entry<String, Boolean> directory : getDirectories()
					.entrySet()) {
				pathValidator = new DirectoryValidator(
						nodeConfig.getConnection(), directory.getKey(),
						directory.getValue());
				if (!pathValidator.validate()) {
					errMsg.append(pathValidator.getErrMsg());
				}
			}
			if (errMsg.length() > 0) {
				throw new AnkushException(errMsg.toString());
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not validate "
					+ Constant.Component.Name.CASSANDRA + " directories path.");
		}
	}

	private Map<String, Boolean> getDirectories() throws AnkushException {
		try {
			Map<String, Boolean> directoriesMap = new HashMap<String, Boolean>();
			directoriesMap.put(componentConfig.getInstallPath(), true);
			directoriesMap
					.put(componentConfig
							.getAdvanceConfStringProperty(CassandraConstants.ClusterProperties.DATA_DIR),
							false);
			directoriesMap
					.put(componentConfig
							.getAdvanceConfStringProperty(CassandraConstants.ClusterProperties.LOG_DIR),
							false);
			directoriesMap
					.put(componentConfig
							.getAdvanceConfStringProperty(CassandraConstants.ClusterProperties.SAVED_CACHES_DIR),
							false);
			directoriesMap
					.put(componentConfig
							.getAdvanceConfStringProperty(CassandraConstants.ClusterProperties.COMMIT_LOG_DIR),
							false);
			return directoriesMap;
		} catch (Exception e) {
			throw new AnkushException("Could not get "
					+ Constant.Component.Name.CASSANDRA
					+ " directories path to validate.");
		}
	}

	/** Validating local bundle path and download url path **/
	private void validatingBundlePath() throws AnkushException {
		try {
			ValidationResult status = null;
			// Validating the bundle path for the component.
			logger.info("Validating "
					+ Constant.Component.Name.CASSANDRA
					+ " "
					+ String.valueOf(componentConfig.getSourceType())
							.toLowerCase() + " path...",
					Constant.Component.Name.CASSANDRA, nodeConfig.getHost());
			if ((componentConfig.getSourceType().equals(SourceType.LOCAL) && !componentConfig
					.getSource().isEmpty())) {
				status = ValidationUtility
						.isFileExists(nodeConfig.getConnection(),
								componentConfig.getSource());
			} else if ((componentConfig.getSourceType().equals(
					SourceType.DOWNLOAD) && !componentConfig.getSource()
					.isEmpty())) {
				status = ValidationUtility
						.validateDownloadUrl(nodeConfig.getConnection(),
								componentConfig.getSource());
			} else {
				throw new AnkushException(
						"Either "
								+ Constant.Component.Name.CASSANDRA
								+ " bundle source type is invalid or source value is empty.");
			}
			if (!status.isStatus()) {
				throw new AnkushException("Validating "
						+ Constant.Component.Name.CASSANDRA
						+ String.valueOf(componentConfig.getSourceType())
								.toLowerCase() + " path failed...");
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not validate "
					+ Constant.Component.Name.CASSANDRA + " bundle path.");
		}
	}

	/**
	 * Validating Java version for Cassandra 2.x
	 */
	private void validateJavaVersion() throws AnkushException {
		try {
			// Checking whether the major revision number of the Cassandra
			// package
			// version is 2
			if (componentConfig.getVersion().split("\\.")[0].equals("2")) {
				// If Java is already installed, then checking its version
				if (clusterConfig.getJavaConf().isRegister()) {
					String username = clusterConfig.getAuthConf().getUsername();
					String password = clusterConfig.getAuthConf()
							.isUsingPassword() ? clusterConfig.getAuthConf()
							.getPassword() : clusterConfig.getAuthConf()
							.getPrivateKey();
					// Getting Java version
					String javaVersion = SSHUtils.getJavaVersion(
							nodeConfig.getHost(), username, password,
							clusterConfig.getAuthConf().isUsingPassword());
					// Checking whether the minor revision number of Java
					// version is
					// 7 as Cassandra 2.x.x requires Java 1.7.x
					if (!javaVersion.split("\\.")[1].equals("7")) {
						throw new AnkushException(
								"Java version should be 1.7.x for "
										+ Constant.Component.Name.CASSANDRA
										+ componentConfig.getVersion() + " ...");
					}
				}
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"Could not validate java version for Cassandra");
		}
	}

	/**
	 * Validate java.
	 */
	private void validateJava() throws AnkushException {
		try {
			logger.debug("Validating java.", nodeConfig.getHost());
			Validator validator = new JavaValidator(nodeConfig.getConnection());
			if (!validator.validate()) {
				throw new AnkushException(validator.getErrMsg());
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not validate Java on "
					+ nodeConfig.getHost());
		}
	}

	/** adding error to clusterConf and logger */
	private boolean addError(String error, Throwable t) {
		isError = true;
		clusterConfig.addError(nodeConfig.getHost(),
				Constant.Component.Name.CASSANDRA, error);
		logger.error(error, Constant.Component.Name.CASSANDRA,
				nodeConfig.getHost(), t);
		return false;
	}

}
