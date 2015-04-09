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
package com.impetus.ankush2.ganglia;

import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.validator.DirectoryValidator;
import com.impetus.ankush.common.utils.validator.PortValidator;
import com.impetus.ankush.common.utils.validator.ValidationResult;
import com.impetus.ankush.common.utils.validator.ValidationUtility;
import com.impetus.ankush.common.utils.validator.Validator;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;

public class GangliaValidator {

	private AnkushLogger logger;
	private ClusterConfig clusterConf;
	private NodeConfig nodeConf;
	private ComponentConfig compConf;
	private boolean isError = false;

	public GangliaValidator(ClusterConfig clusterConf, NodeConfig nodeConf)
			throws AnkushException {
		try {
			this.clusterConf = clusterConf;
			this.nodeConf = nodeConf;
			this.logger = new AnkushLogger(this.getClass(), this.clusterConf);
			this.compConf = clusterConf.getComponents().get(
					Constant.Component.Name.GANGLIA);
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while creating Ganglia Validator class object. "
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	public boolean validate() {
		try {
			logger.info("Validating nodes for "
					+ Constant.Component.Name.GANGLIA + " deployment...",
					Constant.Component.Name.GANGLIA, nodeConf.getHost());
			// validating node connection
			if (!validateConnection()) {
				return false;
			}

			// validating port
			validatePort();
			// validating paths
			validatePath();
			if (isError) {
				clusterConf.addError(Constant.Component.Name.GANGLIA,
						"Validating node " + nodeConf.getHost() + " for "
								+ Constant.Component.Name.GANGLIA
								+ " deployment failed.");
			}

			return !isError;
		} catch (Exception e) {
			return addError("There is some exception while validating nodes for "
					+ Constant.Component.Name.GANGLIA + " deployment.");
		}
	}

	/** Validation connection with node */
	private boolean validateConnection() throws AnkushException {
		try {
			logger.debug("Validating connection.", nodeConf.getHost());
			if (nodeConf.getConnection().exec(new ExecCommand("ls")).rc == 0) {
				return true;
			}
		} catch (Exception e) {
			return addError("Authentication failed on node : "
					+ nodeConf.getHost());
		}
		return false;
	}

	/**
	 * Validate path.
	 */
	private void validatePath() {
		try {
			logger.info("Validating rrdpath used for storing rrd files for "
					+ Constant.Component.Name.GANGLIA,
					Constant.Component.Name.GANGLIA, nodeConf.getHost());
			StringBuilder errMsg = new StringBuilder("");
			Validator rrdPathValidator = new DirectoryValidator(
					nodeConf.getConnection(),
					FileNameUtils.convertToValidPath(compConf
							.getAdvanceConfStringProperty(GangliaConstants.ClusterProperties.RRD_FILE_PATH))
							+ compConf
									.getAdvanceConf()
									.get(GangliaConstants.ClusterProperties.GANGLIA_CLUSTER_NAME),
					false);
			if (!rrdPathValidator.validate()) {
				errMsg.append(rrdPathValidator.getErrMsg());
			}

			ValidationResult confPathStatus = ValidationUtility
					.isFileExists(
							nodeConf.getConnection(),
							compConf.getAdvanceConfStringProperty(GangliaConstants.ClusterProperties.GMOND_CONF_PATH));
			if (confPathStatus.isStatus()) {
				errMsg.append("\n")
						.append("gmond configuration file already exists. Please remove it manually.");
			}
			confPathStatus = ValidationUtility
					.isFileExists(
							nodeConf.getConnection(),
							compConf.getAdvanceConfStringProperty(GangliaConstants.ClusterProperties.GMETAD_CONF_PATH));
			if (confPathStatus.isStatus()) {
				errMsg.append("\n")
						.append("gmetad configuration file already exists. Please remove it manually.");
			}

			if (errMsg.length() > 0) {
				addError(errMsg.toString());
			}
		} catch (Exception e) {
			addError("There is some exception while validating paths used by "
					+ Constant.Component.Name.GANGLIA + ". "
					+ GangliaConstants.EXCEPTION_STRING);
		}
	}

	/** Validating ganglia port */
	private void validatePort() {
		try {
			logger.info("Validating " + Constant.Component.Name.GANGLIA
					+ " port", Constant.Component.Name.GANGLIA,
					nodeConf.getHost());
			PortValidator validator = new PortValidator(
					nodeConf.getHost(),
					compConf.getAdvanceConf()
							.get(GangliaConstants.ClusterProperties.GANGLIA_PORT)
							.toString());
			if (!validator.validate()) {
				addError(validator.getErrMsg());
			}
			logger.info("Port Validation done.",
					Constant.Component.Name.GANGLIA, nodeConf.getHost());
		} catch (Exception e) {
			addError("Exception while validating Ganglia port availability. "
					+ GangliaConstants.EXCEPTION_STRING);
		}
	}

	/**
	 * Adding error to clusterConf and logger
	 * 
	 * @param error
	 *            {@link String}
	 */
	private boolean addError(String error) {
		isError = true;
		clusterConf.addError(nodeConf.getHost(),
				Constant.Component.Name.GANGLIA, error);
		logger.error(error, Constant.Component.Name.GANGLIA, nodeConf.getHost());
		return false;
	}
}
