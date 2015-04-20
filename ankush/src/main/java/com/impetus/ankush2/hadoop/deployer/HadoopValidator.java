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
package com.impetus.ankush2.hadoop.deployer;

import java.util.HashSet;
import java.util.Set;

import com.impetus.ankush.common.utils.validator.DirectoryValidator;
import com.impetus.ankush.common.utils.validator.PathExistenceValidator;
import com.impetus.ankush.common.utils.validator.SourceValidator;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * @author Akhil
 * 
 */
public class HadoopValidator {
	
	private final AnkushLogger logger = new AnkushLogger(HadoopValidator.class);

	private ClusterConfig clusterConfig;

	private NodeConfig nodeConfig;

	private ComponentConfig hadoopConfig;

	HadoopValidator(ClusterConfig clusterConfig, NodeConfig nodeConfig) {
		logger.setCluster(clusterConfig);
		this.clusterConfig = clusterConfig;
		this.hadoopConfig = clusterConfig.getComponents().get(
				Constant.Component.Name.HADOOP);
		this.nodeConfig = nodeConfig;
	}

	public boolean validate() {
		boolean status = true;
		try {
			if (!hadoopConfig.isRegister()) {
				// validate component Source(Local/Download url) existence
				status = validateSourcePath();
				// validate installation path and write permissions.Create
				// directory
				// if not exist
				Set<String> dirList = new HashSet<String>();
				Set<String> nodeRoles =  nodeConfig.getRoles().get(Constant.Component.Name.HADOOP);
				
				dirList.add(hadoopConfig.getInstallPath());
				dirList.add(hadoopConfig.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.TMP_DIR_HADOOP));
				
				if(nodeRoles.contains(HadoopConstants.Roles.DATANODE)) {
					dirList.add(hadoopConfig.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.DFS_DATA_DIR));
				}
				
				if(nodeRoles.contains(HadoopConstants.Roles.NAMENODE)) {
					dirList.add(hadoopConfig.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.DFS_NAME_DIR));
				}
				
				if(nodeRoles.contains(HadoopConstants.Roles.JOURNALNODE)) {
					dirList.add(hadoopConfig.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.HA_JOURNALNODE_EDITS_DIR));
				}
				
				for(String dirPath : dirList) {
					status = validatePathPermissions(dirPath, true) && status;
				}
			} else {
				// validate componentHomePath existence
				status = validatePathExistence(hadoopConfig.getHomeDir());
			}
			nodeConfig.setStatus(status);
		} catch (Exception e) {
			addAndLogError("Could not validate Hadoop component");
			nodeConfig.setStatus(status);
			return false;
		}
		return status;
	}

	private boolean validateSourcePath() {
		logger.info("Validating component source for ...");
		SourceValidator validator = new SourceValidator(
				Constant.Component.Name.HADOOP, nodeConfig, hadoopConfig);
		if (!validator.validate()) {
			addAndLogError(validator.getErrMsg());
			return false;
		} else {
			logger.info(
					"Validated source for Hadoop done",
					Constant.Component.Name.HADOOP,
					this.nodeConfig.getHost());
		}
		return true;
	}

	private boolean validatePathExistence(String path) {
		logger.info("Validating path existence - " + path,
				Constant.Component.Name.HADOOP,
				this.nodeConfig.getHost());
		PathExistenceValidator validator = new PathExistenceValidator(
				nodeConfig.getConnection(), path);
		if (!validator.validate()) {
			addAndLogError(validator.getErrMsg());
			return false;
		} else {
			logger.info(
					"Validated path existence - " + path,
					Constant.Component.Name.HADOOP,
					this.nodeConfig.getHost());
		}
		return true;
	}
	
	// validate path and path permission
	private boolean validatePathPermissions(String path, boolean ignoreIfExists) {
		logger.info(
				"Validating path permissions - " + path,
				Constant.Component.Name.HADOOP, this.nodeConfig.getHost());
		
		DirectoryValidator directoryValidator = new DirectoryValidator(
				nodeConfig.getConnection(), path, ignoreIfExists);
		
		if (!directoryValidator.validate()) {
			addAndLogError(directoryValidator.getErrMsg());
			return false;
		}
		return true;
	}
	
	private void addAndLogError(String errMsg) {
		HadoopUtils.addAndLogError(logger, clusterConfig,
				errMsg, Constant.Component.Name.HADOOP,
				this.nodeConfig.getHost());		
	}
}
