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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.validator.DirectoryValidator;
import com.impetus.ankush.common.utils.validator.ValidationResult;
import com.impetus.ankush.common.utils.validator.ValidationUtility;
import com.impetus.ankush.common.utils.validator.Validator;

/**
 * The Class CassandraClusterValidator.
 */
public class CassandraClusterValidator {

	private ClusterConf clusterConf = null;
	private CassandraNodeConf nodeConf = null;

	private String vendor = null;

	/** The error messages. */
	private final HashMap<String, String> errorMessages = new HashMap<String, String>();

	/* Logger object */
	/** The logger. */
	private final AnkushLogger logger = new AnkushLogger(
			CassandraClusterValidator.class);

	public boolean validate(ClusterConf clusterConf, CassandraNodeConf nodeConf) {
		logger.info("Validating...");
		this.clusterConf = clusterConf;
		this.nodeConf = nodeConf;

		// Set logger
		logger.setClusterId(this.clusterConf.getClusterId());
		logger.setOperationId(this.clusterConf.getOperationId());
		logger.setHost(this.nodeConf.getPublicIp());
		logger.setClusterName(this.clusterConf.getClusterName());
		CassandraClusterConf conf = (CassandraClusterConf) this.clusterConf;
		CassandraConf cassandraConf = conf.getCassandra();
		SSHExec connection = SSHUtils.connectToNode(
				nodeConf.getPublicIp(), clusterConf.getUsername(),
				clusterConf.getPassword(), clusterConf.getPrivateKey());

		if (connection == null) {
			errorMessages.put("authentication", "Authentication failed.");
			logger.error("Authenication failed.");
			return false;
		}

		// Validating whether package used for cluster deployment is of same vendor as specified
		validateVendor(connection,cassandraConf);

		// Validating installation path, data directory & log directory path
		validatePath(connection,cassandraConf);

		// Validating component path
		validatingComponentPaths(connection,conf,cassandraConf);

		// Validating java
		validateJavaHome(connection,conf);
		
		validateJavaVersion(conf, cassandraConf,nodeConf);

		if(connection != null) {
			connection.disconnect();
		}
		if (errorMessages.isEmpty()) {
			logger.info("Node validated.");
			return true;
		} else {
			for(Entry<String, String> entry : errorMessages.entrySet()){
				logger.error(nodeConf, entry.getKey() + ": "  +entry.getValue());
			}
			logger.error("Validating node failed.");
		}
		return false;
	}
	
	private void validateJavaVersion(CassandraClusterConf conf, CassandraConf cassandraConf, CassandraNodeConf nodeConf){
		try {
			List<String> cassandraVersionSeriesList = new ArrayList<String>(
					Arrays.asList(cassandraConf.getComponentVersion().split("\\.")));
			if(cassandraVersionSeriesList.get(0).equalsIgnoreCase("2")){
				if(!conf.getJavaConf().isInstall()){
					String username = conf.getUsername();
					String password = conf.getPassword();
					boolean authUsingPassword = true;
					if (conf.getPrivateKey() != null && !conf.getPrivateKey().isEmpty()) {
						password = conf.getPrivateKey();
						authUsingPassword = false;
					}
					String javaVersion = SSHUtils.getJavaVersion(nodeConf.getPublicIp(), username, password, authUsingPassword);
					List<String> lstVersionVal = new ArrayList<String>(Arrays.asList(javaVersion.split("\\.")));
					if(!lstVersionVal.get(1).equalsIgnoreCase("7")){
						errorMessages.put("java", "Java version should be 7 for Cassandra "+cassandraConf.getComponentVersion()+" ...");
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Validate path.
	 * 
	 * @param connection
	 *            the connection
	 */
	private void validateVendor(SSHExec connection, CassandraConf conf) {

		if(conf.getComponentVendor().equalsIgnoreCase("Datastax")){
			vendor = "dsc";
		}else{
			vendor = "apache";
		}
		// finding component home
		String componentHome = conf.getInstallationPath()
				+ vendor + "-cassandra-"
				+ conf.getComponentVersion();
		// setting component home
		conf.setComponentHome(componentHome);

		logger.info("Validating vendor...");
		String errMsg = null;
		String[] fileParts;
		String[] packageParts;
		if ((conf.getServerTarballLocation() != null)
				&& !conf.getServerTarballLocation().isEmpty()) {
			fileParts = conf.getServerTarballLocation().split("/");
			packageParts = fileParts[fileParts.length-1].split("-");
			if(!packageParts[0].equals(vendor)){
				errMsg = "The package provided for installation is of different vendor as specified";
			}
		}

		if ((conf.getLocalBinaryFile() != null)
				&& !conf.getLocalBinaryFile().isEmpty()){
			fileParts = conf.getLocalBinaryFile().split("/");
			packageParts = fileParts[fileParts.length-1].split("-");
			if(!packageParts[0].equals(vendor)){
				errMsg = "The package provided for installation is of different vendor as specified";
			}
		}
		if ((conf.getTarballUrl() != null)
				&& !conf.getTarballUrl().isEmpty()){
			fileParts = conf.getTarballUrl().split("/");
			packageParts = fileParts[fileParts.length-1].split("-");
			if(!packageParts[0].equals(vendor)){
				errMsg = "The package provided for installation is of different vendor as specified";
			}
		}

		if (errMsg != null) {
			errorMessages.put("Vendor", errMsg);
			logger.error("The package provided for installation is of different vendor as specified.");
		}
	}

	/**
	 * Validate path.
	 * 
	 * @param connection
	 *            the connection
	 */
	private void validatePath(SSHExec connection, CassandraConf conf) {
		logger.info("Validating path...");
		String errMsg = null;

		// validating installation path
		Validator installPathValidator = new DirectoryValidator(
				connection, conf.getInstallationPath(), true);
		if (!installPathValidator.validate()) {
			errMsg = installPathValidator.getErrMsg();
			errorMessages.put("path", errMsg);
			logger.error(errMsg);
		}

		// validating data directory path
		Validator installDataDirValidator = new DirectoryValidator(
				connection, conf.getDataDir(), false);
		if (!installDataDirValidator.validate()) {
			errMsg = installDataDirValidator.getErrMsg();
			errorMessages.put("path", errMsg);
			logger.error(errMsg);
		}

		// validating saved_caches directory path
		Validator installsavedCacheDirValidator = new DirectoryValidator(
				connection, conf.getSavedCachesDir(), false);
		if (!installsavedCacheDirValidator.validate()) {
			errMsg = installsavedCacheDirValidator.getErrMsg();
			errorMessages.put("path", errMsg);
			logger.error(errMsg);
		}

		// validating commitlog directory path
		Validator installCommitLogDirValidator = new DirectoryValidator(
				connection, conf.getCommitlogDir(), false);
		if (!installCommitLogDirValidator.validate()) {
			errMsg = installCommitLogDirValidator.getErrMsg();
			errorMessages.put("path", errMsg);
			logger.error(errMsg);
		}

		// validating log directory path
		Validator installLogDirValidator = new DirectoryValidator(
				connection, conf.getLogDir(), true);
		if (!installLogDirValidator.validate()) {
			errMsg = installLogDirValidator.getErrMsg();
			errorMessages.put("path", errMsg);
			logger.error(errMsg);
		}

	}

	/**
	 * Gets the error messages.
	 * 
	 * @return the errorMessages
	 */
	public HashMap<String, String> getErrorMessages() {
		return this.errorMessages;
	}

	/**
	 * Validate java home.
	 *
	 * @param connection the connection
	 * @param node the node
	 */
	private void validateJavaHome(SSHExec connection,CassandraClusterConf conf) {
		ValidationResult status = null;
		// validating Java_Home/Java bundle path
		if (!conf.getJavaConf().isInstall()) {

			logger.info(nodeConf.getPublicIp(), "Validating java home path...");
			String javaBinPath = conf.getJavaConf().getJavaBinPath();

			status = ValidationUtility.isFileExists(connection, javaBinPath);

			if (!status.isStatus()) {
				conf.setState("error");
				nodeConf.setStatus(false);
				nodeConf.setNodeState("error");
				logger.error(nodeConf, "Validating java home path failed...");
				nodeConf.addError("javaHome", status.getMessage());
				errorMessages.put("java", "Validating java home path failed...");
			}
		}
	}

	/**
	 * Validating component paths.
	 *
	 * @param connection the connection
	 * @param componentName the component name
	 * @param compInfo the comp info
	 * @param node the node
	 */
	private void validatingComponentPaths(SSHExec connection,CassandraClusterConf conf, CassandraConf cassandraConf) {
		ValidationResult status = null;

		// Validating the bundle path for the component.
		if (cassandraConf.getLocalBinaryFile() != null
				&& !cassandraConf.getLocalBinaryFile().equals("")) {

			logger.info(nodeConf.getPublicIp(), "Validating " + conf.getTechnology()
					+ " local bundle path...");
			status = ValidationUtility.isFileExists(connection,
					cassandraConf.getLocalBinaryFile());

			if (!status.isStatus()) {
				conf.setState("error");
				nodeConf.setStatus(false);
				nodeConf.setNodeState("error");
				logger.error(nodeConf, "Validating " + conf.getTechnology()
						+ " local bundle path failed...");
				nodeConf.addError(conf.getTechnology() + "_localBinaryFile",
						status.getMessage());
				errorMessages.put("component local path", "Validating  local path failed...");
			}
		}

		// Validating the tarball url for the component.
		if (cassandraConf.getTarballUrl() != null
				&& !cassandraConf.getTarballUrl().equals("")) {

			logger.info(nodeConf.getPublicIp(), "Validating  download url...");
			status = ValidationUtility.validateDownloadUrl(connection,
					cassandraConf.getTarballUrl());

			if (!status.isStatus()) {
				conf.setState("error");
				nodeConf.setNodeState("error");
				nodeConf.setStatus(false);
				logger.error(nodeConf, "Validating  download url failed...");
				nodeConf.addError(conf.getTechnology() + "_tarballUrl",
						status.getMessage());
				errorMessages.put("component download url", "Validating  download url failed...");
			}
		}
	}
}
