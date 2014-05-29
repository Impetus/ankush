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
package com.impetus.ankush.common.framework;

import java.io.File;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.JavaConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.validator.ValidationResult;
import com.impetus.ankush.common.utils.validator.ValidationUtility;

/**
 * The Class ClusterValidator.
 * 
 * @author hokam chauhan
 */
public class ClusterValidator {

	protected static final String ERROR = "error";

	protected static final String VALIDATING = "Validating ";

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(ClusterValidator.class);

	/** The conf. **/
	protected ClusterConf conf = null;
	/** java conf **/
	protected JavaConf javaConf = null;

	/**
	 * 
	 */
	public ClusterValidator() {

	}

	/**
	 * 
	 * @param conf
	 * @param javaConf
	 */
	public ClusterValidator(ClusterConf conf, JavaConf javaConf) {
		this.conf = conf;
		this.javaConf = javaConf;
	}

	/**
	 * Validate java home.
	 * 
	 * @param connection
	 *            the connection
	 * @param node
	 *            the node
	 */
	public void validateJavaHome(SSHExec connection, NodeConf node) {

		logger.setCluster(conf);
		ValidationResult status = null;

		// validating Java_Home/Java bundle path
		if (!javaConf.isInstall()) {

			logger.info(node.getPublicIp(), "Validating Java home path...");

			String javaBinPath = javaConf.getJavaBinPath();
			status = ValidationUtility.isFileExists(connection, javaBinPath);

			if (!status.isStatus()) {
				conf.setState(ERROR);
				node.setStatus(false);
				node.setNodeState(ERROR);
				logger.error(node, "Validating Java home path failed...");
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
	public void validatingComponentPaths(SSHExec connection,
			String componentName, GenericConfiguration compInfo, NodeConf node) {
		logger.setCluster(conf);
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

		// Validating the server tarball URL for the component.
		if (compInfo.getServerTarballLocation() != null
				&& !compInfo.getServerTarballLocation().equals("")) {

			logger.info(node.getPublicIp(), VALIDATING + componentName
					+ " server tarball location...");
			File file = new File(compInfo.getServerTarballLocation());

			status = new ValidationResult();
			status.setMessage("Could not find file "
					+ compInfo.getServerTarballLocation()
					+ ". Please specify the correct path.");
			status.setStatus(file.exists());

			if (!status.isStatus()) {
				conf.setState(ERROR);
				node.setNodeState(ERROR);
				node.setStatus(false);
				logger.error(node, VALIDATING + componentName
						+ " server tarball location failed...");
				node.addError(componentName + "_serverTarballLocation",
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
	public void validatingPathPermissions(String componentName,
			SSHExec connection, GenericConfiguration compInfo, NodeConf node) {
		logger.setCluster(conf);
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
