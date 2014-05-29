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

import java.util.HashMap;
import java.util.Map;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.validator.DirectoryValidator;
import com.impetus.ankush.common.utils.validator.JavaValidator;
import com.impetus.ankush.common.utils.validator.PortValidator;
import com.impetus.ankush.common.utils.validator.Validator;

/**
 * Validate Oracle Node configuration.
 * 
 * @author nikunj
 */
public class OracleNoSQLNodeValidator {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			OracleNoSQLNodeValidator.class);

	/** The error messages. */
	private Map<String, String> errorMessages = new HashMap<String, String>();

	/**
	 * Class constructor.
	 */
	public OracleNoSQLNodeValidator() {
	}

	/**
	 * Gets the error messages.
	 * 
	 * @return the errorMessages
	 */
	public Map<String, String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * Validate node configuration.
	 * 
	 * @param config
	 *            the config
	 * @param nodeConf
	 *            Node configuration
	 * @return true, if successful
	 */
	public boolean validate(OracleNoSQLConf config, OracleNoSQLNodeConf nodeConf) {

		// Set logger
		logger.setClusterId(config.getClusterConf().getClusterId());
		logger.setOperationId(config.getClusterConf().getOperationId());
		logger.setHost(nodeConf.getPublicIp());
		logger.setClusterName(config.getClusterName());

		logger.info("Validating node...");

		// Create remote connection
		SSHExec connection = SSHUtils
				.connectToNode(nodeConf.getPublicIp(), config.getClusterConf()
						.getUsername(), config.getClusterConf().getPassword(),
						config.getClusterConf().getPrivateKey());
		if (connection == null) {
			this.errorMessages.put("authentication", "Authentication failed.");
			logger.error("Authenication failed.");
			return false;
		}

		validatePath(config, connection, nodeConf);

		validateRegistryPort(nodeConf);

		validateHaPortRange(nodeConf);

		validateAdminPort(nodeConf);

		validateJava(connection);

		if (this.errorMessages.isEmpty()) {
			logger.info("Node validated.");
			return true;
		} else {
			logger.error("Validating node failed.");
		}

		return false;
	}

	/**
	 * Validate path.
	 *
	 * @param config the config
	 * @param connection the connection
	 * @param nodeConf the node conf
	 */
	private void validatePath(OracleNoSQLConf config, SSHExec connection,
			OracleNoSQLNodeConf nodeConf) {
		logger.info("Validating path...");
		Validator installPathValidator = new DirectoryValidator(connection,
				config.getInstallationPath(), true);
		Validator dataPathValidator = new DirectoryValidator(connection,
				config.getDataPath(), false);

		StringBuilder errMsg = new StringBuilder("");

		if (!installPathValidator.validate()) {
			errMsg.append(installPathValidator.getErrMsg());
		}
		if (!dataPathValidator.validate()) {
			errMsg.append(dataPathValidator.getErrMsg());
		}

		// Get list of storage dirs
		if (nodeConf.getStorageDirs() != null
				&& !nodeConf.getStorageDirs().isEmpty()) {
			String[] storageDirs = nodeConf.getStorageDirs().split(",");
			for (String storageDir : storageDirs) {
				dataPathValidator = new DirectoryValidator(connection,
						storageDir, false);
				if (!dataPathValidator.validate()) {
					errMsg.append(dataPathValidator.getErrMsg());
				}
			}
		}

		if (errMsg.length() > 0) {
			this.errorMessages.put("path", errMsg.toString());
			logger.error("Path validation failed.");
		}
	}

	/**
	 * Validate registry port.
	 * 
	 * @param nodeConf
	 *            the node conf
	 */
	private void validateRegistryPort(OracleNoSQLNodeConf nodeConf) {
		logger.info("Validating RegistryPort...");
		Validator portValidator = new PortValidator(nodeConf.getPublicIp(),
				nodeConf.getRegistryPort().toString());
		if (!portValidator.validate()) {
			this.errorMessages.put("registryPort", portValidator.getErrMsg());
			logger.error("Validating RegistryPort failed.");
		}
	}

	/**
	 * Validate port range.
	 * 
	 * @param nodeConf
	 *            the node conf
	 */
	private void validateHaPortRange(OracleNoSQLNodeConf nodeConf) {
		Validator portValidator;
		logger.info("Validating HaPortRange...");
		portValidator = new PortValidator(nodeConf.getPublicIp(),
				nodeConf.getHaPortRangeStart() + "-"
						+ nodeConf.getHaPortRangeEnd());
		if (!portValidator.validate()) {
			this.errorMessages.put("haPortRange", portValidator.getErrMsg());
			logger.error("Validating haPortRange failed.");
		}
	}

	/**
	 * Validate admin port.
	 * 
	 * @param nodeConf
	 *            the node conf
	 */
	private void validateAdminPort(OracleNoSQLNodeConf nodeConf) {
		Validator portValidator;
		if (nodeConf.isAdmin()) {
			logger.info("Validating AdminPort...");
			portValidator = new PortValidator(nodeConf.getPublicIp(), nodeConf
					.getAdminPort().toString());
			if (!portValidator.validate()) {
				this.errorMessages.put("adminPort", portValidator.getErrMsg());
				logger.error("Validating adminPort failed.");
			}
		}
	}

	/**
	 * Validate java.
	 * 
	 * @param connection
	 *            the connection
	 */
	private void validateJava(SSHExec connection) {
		logger.info("Validating Java...");
		Validator javaValidator = new JavaValidator(connection);
		if (!javaValidator.validate()) {
			this.errorMessages.put("java", javaValidator.getErrMsg());
			logger.error("Validating Java failed.");
		}
	}

}
