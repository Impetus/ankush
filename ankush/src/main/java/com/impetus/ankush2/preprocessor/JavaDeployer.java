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
package com.impetus.ankush2.preprocessor;

import java.util.LinkedHashMap;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddEnvironmentVariables;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.SourceFile;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.FileNameUtils.ONSFileType;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.AuthConfig;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class JavaDeployer.
 */
public class JavaDeployer {

	/** The logger. */
	AnkushLogger logger = new AnkushLogger(JavaDeployer.class);

	/** The cluster config. */
	private ClusterConfig clusterConfig;

	/** The repo path. */
	private static final String repoPath = AppStoreWrapper.getServerRepoPath();

	/** The ankush common. */
	private static final String ankushCommon = ".ankush/common/";

	/**
	 * Instantiates a new java deployer.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 */
	public JavaDeployer(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
		logger.setCluster(clusterConfig);
	}

	/**
	 * Install java.
	 * 
	 * @param host
	 *            the host
	 * @return true, if successful
	 */
	public boolean installJava(String host) {
		// Location of java binary at Ankush Server
		final String javaBundleSource = repoPath
				+ clusterConfig.getJavaConf().getSource();
		String destinationFolderName = FileNameUtils
				.getExtractedDirectoryName(javaBundleSource);

		// Java Installtion path - AgentInstallDir/.ankush/common
		String installationPath = clusterConfig.getAgentInstallDir()
				+ ankushCommon;

		// destination file
		final String jdkbinFile = installationPath
				+ clusterConfig.getJavaConf().getSource();

		// JAVA_HOME
		final String newJavaHomePath = installationPath + destinationFolderName;
		// $JAVA_HOME/bin in $PATH
		final String newJavaBinInPath = newJavaHomePath + "bin";

		return installJava(clusterConfig.getNodes().get(host),
				clusterConfig.getAuthConf(), installationPath,
				javaBundleSource, jdkbinFile, newJavaHomePath, newJavaBinInPath);

	}

	/**
	 * Install java.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @param authConf
	 *            the auth conf
	 * @param installationPath
	 *            the installation path
	 * @param javaBundleSource
	 *            the java bundle source
	 * @param jdkbinFile
	 *            the jdkbin
	 * @param newJavaHomePath
	 *            the new java home path
	 * @param newJavaBinInPath
	 *            the new java bin in path
	 * @return true, if successful
	 */
	private boolean installJava(NodeConfig nodeConfig, AuthConfig authConf,
			final String installationPath, final String javaBundleSource,
			final String jdkbinFile, final String newJavaHomePath,
			final String newJavaBinInPath) {

		ONSFileType fileType = FileNameUtils.getFileType(javaBundleSource);

		boolean status = true;
		try {
			logger.info("Installing java...",
					Constant.Component.Name.PREPROCESSOR, nodeConfig.getHost());

			Result res = null;
			SSHExec connection = nodeConfig.getConnection();
			// if connected
			if (connection == null) {
				throw new AnkushException(
						Constant.Strings.ExceptionsMessage.CONNECTION_NULL_STRING);
			}
			logger.info("Create directory - " + installationPath,
					Constant.Component.Name.PREPROCESSOR, nodeConfig.getHost());

			// make installation directory if not exists
			AnkushTask mkInstallationPath = new MakeDirectory(installationPath);
			if (connection.exec(mkInstallationPath).rc != 0) {
				throw new AnkushException(
						"Could not create installation directory "
								+ installationPath);

			}
			logger.info("Uploading java bundle to node...",
					Constant.Component.Name.PREPROCESSOR, nodeConfig.getHost());
			// upload java binary
			connection.uploadSingleDataToServer(javaBundleSource,
					installationPath);
			logger.info("Unpacking java bundle...",
					Constant.Component.Name.PREPROCESSOR, nodeConfig.getHost());
			// execute command
			if (fileType == ONSFileType.BIN) {
				CustomTask cmd = new ExecCommand("cd " + installationPath,
						"echo /r/n/r/n | sh " + jdkbinFile);
				res = connection.exec(cmd);
			} else if (fileType == ONSFileType.TAR_GZ
					|| fileType == ONSFileType.GZ) {
				CustomTask cmd = new ExecCommand("cd " + installationPath,
						"tar -xzvf " + jdkbinFile);
				res = connection.exec(cmd);
			} else if (fileType == ONSFileType.ZIP) {
				CustomTask cmd = new ExecCommand("cd " + installationPath,
						"unzip " + jdkbinFile);
				res = connection.exec(cmd);
			}

			if (res.rc != 0) {
				throw new AnkushException(
						"Could not extract/unpack java bundle : ");
			}

			CustomTask getPathVar = new ExecCommand("echo $PATH");
			res = connection.exec(getPathVar);
			// if java not already set in $PATH
			if (res.rc == 0
					&& !res.sysout.trim().toString().contains(newJavaBinInPath)) {
				LinkedHashMap<String, String> envPropertiesMap = new LinkedHashMap<String, String>();
				logger.info("Setting Java in Path variable...",
						Constant.Component.Name.PREPROCESSOR,
						nodeConfig.getHost());
				StringBuilder pathVar = new StringBuilder(res.sysout.trim())
						.append(":").append(newJavaBinInPath);
				envPropertiesMap.put("PATH", pathVar.toString());

				envPropertiesMap.put("JAVA_HOME", newJavaHomePath);
				AnkushTask addEnvVar = new AddEnvironmentVariables(
						envPropertiesMap, Constant.LinuxEnvFiles.BASHRC,
						Constant.Component.Name.PREPROCESSOR);
				if (connection.exec(addEnvVar).rc != 0) {
					throw new AnkushException(
							"Could not add environment variables for JAVA in "
									+ Constant.LinuxEnvFiles.BASHRC + " file");
				}
				// source .bashrc file
				AnkushTask sourceBashFile = new SourceFile(
						Constant.LinuxEnvFiles.BASHRC);
				if (connection.exec(sourceBashFile).rc != 0) {
					throw new AnkushException(
							"Could not source .bashrc file after setting JAVA in $PATH and export $JAVA_HOME");
				}
				// set homeDir in javaConf
				clusterConfig.getJavaConf().setHomeDir(newJavaHomePath);
				logger.info("Java installation is over.",
						Constant.Component.Name.PREPROCESSOR,
						nodeConfig.getHost());
			} else {
				logger.warn(
						"JAVA is already set in PATH variable.Skipping setting new JAVA.",
						Constant.Component.Name.PREPROCESSOR,
						nodeConfig.getHost());

			}
		} catch (AnkushException e) {
			logger.error(e.getMessage(), Constant.Component.Name.PREPROCESSOR,
					nodeConfig.getHost(), e);
			status = false;
		} catch (Exception e) {
			logger.error("There is some exception in installing JAVA."
					+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
					Constant.Component.Name.PREPROCESSOR, nodeConfig.getHost(),
					e);
			status = false;
		}
		nodeConfig.setStatus(status);
		return status;
	}
}
