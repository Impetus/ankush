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
package com.impetus.ankush2.zookeeper;

import com.impetus.ankush.common.utils.validator.DirectoryValidator;
import com.impetus.ankush.common.utils.validator.PathExistenceValidator;
import com.impetus.ankush.common.utils.validator.SourceValidator;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

public class ZookeeperValidator {

	private final AnkushLogger logger = new AnkushLogger(
			ZookeeperValidator.class);

	private ClusterConfig clusterConfig;

	private NodeConfig nodeConfig;

	private ComponentConfig compConfig;

	private String compName;

	ZookeeperValidator(ClusterConfig clusterConfig, NodeConfig nodeConfig,
			String compName) {
		logger.setCluster(clusterConfig);
		this.clusterConfig = clusterConfig;
		this.compName = compName;
		this.compConfig = clusterConfig.getComponents().get(compName);
		this.nodeConfig = nodeConfig;
	}

	boolean isError = false;

	public boolean validate() {
		try {
			if (!compConfig.isRegister()) {
				// validate component Source(Local/Download url) existence
				validateSourcePath();

				// validate installation path and write permissions.Create
				// directory
				// if not exist
				validatePathAndPermission();
			} else {

				// validate componentHomePath existence only in Level3
				if (AnkushUtils.isManagedByAnkush(compConfig)) {
					validatePathExistence();
				}

			}
		} catch (Exception e) {
			logger.error("Exception in validation of Zookeeper."
					+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
					compName, this.nodeConfig.getHost(), e);
		}
		return !isError;
	}

	private void validateSourcePath() {
		logger.info("Validating Component Source for " + compName + " ...");
		SourceValidator validator = new SourceValidator(compName, nodeConfig,
				compConfig);
		if (!validator.validate()) {
			addAndLogError(validator.getErrMsg());
		}
	}

	// validate path and path permission
	private void validatePathAndPermission() {
		logger.info(
				"Validating path permission-" + compConfig.getInstallPath(),
				compName, this.nodeConfig.getHost());
		DirectoryValidator directoryValidator = new DirectoryValidator(
				nodeConfig.getConnection(), compConfig.getInstallPath(), true);
		if (!directoryValidator.validate()) {
			addAndLogError(directoryValidator.getErrMsg());
		}
	}

	private void validatePathExistence() {
		logger.info("Validating path existence-" + compConfig.getHomeDir(),
				compName, this.nodeConfig.getHost());
		PathExistenceValidator validator = new PathExistenceValidator(
				nodeConfig.getConnection(), compConfig.getHomeDir());
		if (!validator.validate()) {
			addAndLogError(validator.getErrMsg());
		} else {
			logger.info("Validated path existence-" + compConfig.getHomeDir(),
					compName, this.nodeConfig.getHost());
		}
	}

	private void addAndLogError(String errMsg) {
		isError = true;
		clusterConfig.addError(nodeConfig.getHost(), compName, errMsg);
		logger.error(errMsg, compName, nodeConfig.getHost());
	}

}
