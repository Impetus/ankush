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
package com.impetus.ankush2.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;

import org.zeroturnaround.zip.ZipUtil;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.CreateTarArchive;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.UnTarArchive;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.framework.utils.ObjectFactory;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * @author Akhil
 * 
 */
public class LogsManager {

	/**
	 * @param clusterConfig
	 * @param component
	 * @param parameterMap
	 */
	public LogsManager(ClusterConfig clusterConfig, String component,
			Map<String, Object> parameterMap) {
		this.clusterConfig = clusterConfig;
		this.parameterMap = parameterMap;
	}

	private void initializeDataMembers() throws AnkushException {
		try {
			component = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.COMPONENT);
			if (component == null || component.isEmpty()) {
				throw (new AnkushException(
						"Invalid Log request: Please specify a component."));
			}

			nodes = (ArrayList) parameterMap.get(Constant.JsonKeys.Logs.NODES);
			if (nodes == null || nodes.isEmpty()) {
				nodes = new ArrayList<String>(this.clusterConfig
						.getComponents().get(component).getNodes().keySet());
			}

			roles = (ArrayList) parameterMap.get(Constant.JsonKeys.Logs.ROLES);

			Serviceable serviceableObj = ObjectFactory
					.getServiceObject(component);

			if (roles == null || roles.isEmpty()) {
				roles = new ArrayList<String>(
						serviceableObj.getServiceList(this.clusterConfig));
			}
		} catch (Exception e) {
			throw (new AnkushException(
					"Invalid Log request: Could not read POST JSON data."));
		}
	}

	protected AnkushLogger LOG = new AnkushLogger(LogsManager.class);

	private ClusterConfig clusterConfig;

	private String component;

	private Map<String, Object> parameterMap;

	private ArrayList<String> nodes;

	private ArrayList<String> roles;

	public String downloadLogsOnServer() throws AnkushException {
		try {
			String clusterResourcesLogsDir = AppStoreWrapper
					.getClusterResourcesPath() + "logs/";

			String clusterLogsDirName = "Logs_" + this.clusterConfig.getName()
					+ "_" + System.currentTimeMillis();

			String clusterLogsArchiveName = clusterLogsDirName + ".zip";

			final String cmpLogsDirPathOnServer = clusterResourcesLogsDir
					+ clusterLogsDirName + "/" + component + "/";

			if (!FileUtils.ensureFolder(cmpLogsDirPathOnServer)) {
				throw (new AnkushException(
						"Could not create log directory for " + this.component
								+ " on server."));
			}

			final Semaphore semaphore = new Semaphore(nodes.size());

			try {
				for (final String host : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							NodeConfig nodeConfig = clusterConfig.getNodes()
									.get(host);

							SSHExec connection = SSHUtils.connectToNode(host,
									clusterConfig.getAuthConf());
							if (connection == null) {
								// TODO: handle Error
								LOG.error(
										"Could not fetch log files - Connection not initialized",
										component, host);
							}
							Serviceable serviceableObj = null;
							try {
								serviceableObj = ObjectFactory
										.getServiceObject(component);

								for (String role : roles) {
									if (nodeConfig.getRoles().get(component)
											.contains(role)) {

										String tmpLogsDirOnServer = cmpLogsDirPathOnServer
												+ "/" + role + "/" + host + "/";
										if (!FileUtils
												.ensureFolder(tmpLogsDirOnServer)) {
											// TODO: handle Error
											// Log error in operation table and
											// skip
											// this role
											continue;
										}

										String nodeLogsDirPath = FileUtils
												.getSeparatorTerminatedPathEntry(serviceableObj
														.getLogDirPath(
																clusterConfig,
																host, role));
										String logFilesRegex = serviceableObj
												.getLogFilesRegex(
														clusterConfig, host,
														role, null);
										String outputTarArchiveName = role
												+ "_"
												+ +System.currentTimeMillis()
												+ ".tar.gz";
										try {
											List<String> logsFilesList = AnkushUtils
													.listFilesInDir(connection,
															host,
															nodeLogsDirPath,
															logFilesRegex);

											AnkushTask ankushTask = new CreateTarArchive(
													nodeLogsDirPath,
													nodeLogsDirPath
															+ outputTarArchiveName,
													logsFilesList);
											if (connection.exec(ankushTask).rc != 0) {
												// TODO: handle Error
												// Log error in operation table
												// and
												// skip this
												// role
												continue;
											}
											connection
													.downloadFile(
															nodeLogsDirPath
																	+ outputTarArchiveName,
															tmpLogsDirOnServer
																	+ outputTarArchiveName);
											ankushTask = new Remove(
													nodeLogsDirPath
															+ outputTarArchiveName);
											connection.exec(ankushTask);
											System.out
													.println("tmpLogsDirOnServer + outputTarArchiveName : "
															+ tmpLogsDirOnServer
															+ outputTarArchiveName);
											ankushTask = new UnTarArchive(
													tmpLogsDirOnServer
															+ outputTarArchiveName,
													tmpLogsDirOnServer);
											System.out.println("ankushTask.getCommand() : "
													+ ankushTask.getCommand());
											Runtime.getRuntime()
													.exec(ankushTask
															.getCommand())
													.waitFor();
											ankushTask = new Remove(
													tmpLogsDirOnServer
															+ outputTarArchiveName);
											Runtime.getRuntime()
													.exec(ankushTask
															.getCommand())
													.waitFor();
										} catch (Exception e) {
											e.printStackTrace();
											// TODO: handle exception
											// Log error in operation table and
											// skip
											// this role
											continue;
										}
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								return;
							} finally {
								if (semaphore != null) {
									semaphore.release();
								}
								if (connection != null) {
									connection.disconnect();
								}
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {

			}

			ZipUtil.pack(
					new File(clusterResourcesLogsDir + clusterLogsDirName),
					new File(clusterResourcesLogsDir + clusterLogsArchiveName),
					true);

			org.apache.commons.io.FileUtils.deleteDirectory(new File(
					clusterResourcesLogsDir + clusterLogsDirName));

			// result.put(com.impetus.ankush2.constant.Constant.Keys.DOWNLOADPATH,
			// clusterResourcesLogsDir + clusterLogsArchiveName);
		} catch (Exception e) {
			// this.addAndLogError("Could not download logs for " + component +
			// ".");
			LOG.error(e.getMessage(), component, e);
		}
		return null;
	}

	// public List<String> listLogsFiles() {
	//
	// }

	// private Set<String> files;

}
