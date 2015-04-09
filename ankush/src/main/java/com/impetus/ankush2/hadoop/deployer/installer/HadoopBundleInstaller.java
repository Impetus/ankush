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
package com.impetus.ankush2.hadoop.deployer.installer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.RemoveCmpEnvVariables;
import com.impetus.ankush.common.scripting.impl.SyncFolder;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.utils.SSHUtils;
import com.impetus.ankush2.constant.Constant;

/**
 * @author Akhil
 * 
 */
public class HadoopBundleInstaller extends HadoopInstaller {

	/**
	 * 
	 */
	public HadoopBundleInstaller() {
		super();
	}

	public HadoopBundleInstaller(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, HadoopBundleInstaller.class);
	}

	@Override
	public boolean createNode(NodeConfig nodeConfig) {
		if (!this.createHadoopDirectories(nodeConfig)) {
			return false;
		}
		try {
			LOG.info("Get and extract Hadoop tarball",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			if (!SSHUtils.getAndExtractComponent(nodeConfig.getConnection(),
					this.compConfig, Constant.Component.Name.HADOOP)) {
				HadoopUtils.addAndLogError(LOG, clusterConfig,
						"Could not extract tarball for Hadoop at location "
								+ this.compConfig.getInstallPath(),
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
		} catch (AnkushException e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig, e.getMessage(),
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not extract tarball for Hadoop at location "
							+ this.compConfig.getInstallPath(),
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean removeNode(NodeConfig nodeConfig) {
		try {
			LOG.info("Removing Hadoop directories from node",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			// In-case of register, do not delete Hadoop Directories 
			if (!this.compConfig.isRegister()) {
				
				Set<String> dirList = new HashSet<String>();

				dirList.add(this.compConfig.getHomeDir());

				dirList.add(this.compConfig
						.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.TMP_DIR_HADOOP));

				if (nodeConfig.getRoles().get(Constant.Component.Name.HADOOP)
						.contains(HadoopConstants.Roles.NAMENODE)) {
					dirList.add(this.compConfig
							.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.DFS_NAME_DIR));
				}

				if (nodeConfig.getRoles().get(Constant.Component.Name.HADOOP)
						.contains(HadoopConstants.Roles.DATANODE)) {
					dirList.add(this.compConfig
							.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.DFS_DATA_DIR));
				}

				if (nodeConfig.getRoles().containsKey(
						HadoopConstants.Roles.JOURNALNODE)) {
					dirList.add(this.compConfig
							.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.HA_JOURNALNODE_EDITS_DIR));
				}

				for (String dirName : dirList) {
					LOG.info("Removing directory - " + dirName,
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
					AnkushTask removeDir = new Remove(dirName);
					if (!nodeConfig.getConnection().exec(removeDir).isSuccess) {
						HadoopUtils.addAndLogError(
								LOG,
								clusterConfig,
								"Could not remove directory " + dirName
										+ " using command - "
										+ removeDir.getCommand(),
								Constant.Component.Name.HADOOP,
								nodeConfig.getHost());
						// Do not return in-case of error, Execute Remove
						// command
						// for all directories
						// return false;
					}
				}
				AnkushTask removeEnvVariables = new RemoveCmpEnvVariables(
						Constant.Component.Name.HADOOP,
						Constant.LinuxEnvFiles.BASHRC);
				if (!nodeConfig.getConnection().exec(removeEnvVariables).isSuccess) {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not remove environment variables from "
									+ Constant.LinuxEnvFiles.BASHRC
									+ " file using command - "
									+ removeEnvVariables.getCommand(),
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
				}
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not remove Hadoop directories.",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			// Do not return false in-case of error
		}
		return true;
	}

	private boolean createHadoopDirectories(NodeConfig nodeConfig) {

		try {
			Result res = null;

			LOG.info("Create Hadoop installation directory - "
					+ this.compConfig.getInstallPath(),
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			// make installation directory if not exists
			AnkushTask mkInstallationPath = new MakeDirectory(
					this.compConfig.getInstallPath());
			res = nodeConfig.getConnection().exec(mkInstallationPath);

			if (!res.isSuccess) {
				HadoopUtils.addAndLogError(LOG, clusterConfig,
						"Could not create installation directory using command - "
								+ mkInstallationPath.getCommand(),
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}

			List<String> hadoopTmpDirs = new ArrayList<String>();
			hadoopTmpDirs
					.add((String) this.compConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.TMP_DIR_HADOOP));
			hadoopTmpDirs
					.add((String) this.compConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.TMP_DIR_MAPRED));

			// removing old existing Hadoop directories.
			List<String> hadoopDirs = new ArrayList<String>();
			hadoopDirs
					.add((String) this.compConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_NAME_DIR));
			hadoopDirs
					.add((String) this.compConfig
							.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DFS_DATA_DIR));
			hadoopDirs.addAll(hadoopTmpDirs);

			for (String dirPath : hadoopDirs) {
				AnkushTask removeDir = new Remove(dirPath);
				res = nodeConfig.getConnection().exec(removeDir);
				if (!res.isSuccess) {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not remove directory using command - "
									+ removeDir.getCommand(),
							Constant.Component.Name.HADOOP,
							nodeConfig.getHost());
				}
			}

			// Create Hadoop Tmp directories.
			// for (String dirPath : hadoopTmpDirs) {
			// AnkushTask mkHadoopTmpDir = new MakeDirectory(dirPath);
			// res = nodeConfig.getConnection().exec(mkHadoopTmpDir);
			// if (!res.isSuccess) {
			// HadoopUtils.addAndLogError(LOG, clusterConfig,
			// "Could not create temporary directory: " + dirPath,
			// Constant.Component.Name.HADOOP,
			// nodeConfig.getHost());
			// return false;
			// }
			// }
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not configure Hadoop directories",
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean addNode(NodeConfig nodeConfig) {
		SSHExec namenodeConnection = null;
		try {
			LOG.info(
					"Copying HADOOP_HOME from NameNode to "
							+ nodeConfig.getHost(),
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			AnkushTask ankushTask = new MakeDirectory(
					this.compConfig.getHomeDir());
			if (nodeConfig.getConnection().exec(ankushTask).rc != 0) {
				HadoopUtils.addAndLogError(LOG, clusterConfig,
						"Could not create HADOOP_HOME directory - "
								+ this.compConfig.getHomeDir(),
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}

			LOG.info(
					"Connecting to NameNode - "
							+ HadoopUtils.getNameNodeHost(this.compConfig),
					Constant.Component.Name.HADOOP,
					HadoopUtils.getNameNodeHost(this.compConfig));

			namenodeConnection = SSHUtils.connectToNode(
					HadoopUtils.getNameNodeHost(this.compConfig),
					clusterConfig.getAuthConf());

			ankushTask = new SyncFolder(nodeConfig.getHost(),
					this.compConfig.getHomeDir());

			if (namenodeConnection.exec(ankushTask).rc != 0) {
				HadoopUtils.addAndLogError(LOG, clusterConfig,
						"Could not copy HADOOP_HOME directory from NameNode- "
								+ this.compConfig.getHomeDir(),
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not copy HADOOP_HOME directory from NameNode- "
							+ this.compConfig.getHomeDir(),
					Constant.Component.Name.HADOOP, nodeConfig.getHost(), e);
			return false;
		} finally {
			if (namenodeConnection != null) {
				namenodeConnection.disconnect();
			}
		}
		return true;
	}
}
