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
package com.impetus.ankush2.hadoop.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.lang.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.monitor.AbstractMonitor;
import com.impetus.ankush2.hadoop.utils.ConfigParameterUtils;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.hadoop.utils.Parameter;
import com.impetus.ankush2.utils.AnkushUtils;
import com.impetus.ankush2.utils.SSHUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopComponentMonitor.
 * 
 * @author Akhil
 */
public class HadoopComponentMonitor extends AbstractMonitor {

	/** The hadoop config. */
	private ComponentConfig hadoopConfig;

	/**
	 * Hadoopversion.
	 */
	private void hadoopversion() {
		String errMsg = "Exception: Unable to process request to get Hadoop version";
		try {
			this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
			result.put(HadoopConstants.HADOOP_VERSION,
					this.hadoopConfig.getVersion());
			result.put(
					HadoopConstants.AdvanceConfKeys.IS_HADOOP2,
					this.hadoopConfig
							.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.IS_HADOOP2));
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	private void registerlevel() {
		String errMsg = "Unable to process request to get Hadoop register level";
		try {
			this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
			if (hadoopConfig.isRegister()) {
				result.put(
						HadoopConstants.AdvanceConfKeys.REGISTER_LEVEL,
						this.hadoopConfig
								.getAdvanceConfStringProperty(HadoopConstants.AdvanceConfKeys.REGISTER_LEVEL));
			} else {
				result.put(HadoopConstants.AdvanceConfKeys.REGISTER_LEVEL,
						HadoopConstants.RegisterLevel.LEVEL3);
			}
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	private void allowservicemanagement() {
		String errMsg = "Could not manage Hadoop services.";
		try {
			this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
			if (HadoopUtils.isManagedByAnkush(hadoopConfig)) {
				result.put(HadoopConstants.ALLOW_SERVICE_MANAGEMENT_KEY, false);
				addAndLogError(errMsg + " "
						+ Constant.Registration.ErrorMsg.NOT_MANAGED_MODE);
				return;
			} else {
				result.put(HadoopConstants.ALLOW_SERVICE_MANAGEMENT_KEY, true);
			}
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Mapreducesummary.
	 */
	private void mapreducesummary() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Unable to process request to get MapReduce summary.";
		try {
			HadoopMonitor hadoopMonitor = HadoopUtils.getMonitorInstance(
					this.clusterConf, this.hadoopConfig);
			result.put(HadoopConstants.Hadoop.Keys.MONITORING_TABLE_DATA_KEY,
					hadoopMonitor.getMapReduceMetrics());
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Validatejobsubmission.
	 */
	private void validatejobsubmission() {
		String errMsg = "";
		try {
			this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);

			if (!HadoopUtils.isMonitoredByAnkush(this.hadoopConfig)) {
				result.put(HadoopConstants.ALLOW_JOB_SUBMISSION_KEY, false);
				addAndLogError(errMsg + " "
						+ Constant.Registration.ErrorMsg.BASIC_REGISTRATION);
				return;
			}

			boolean isHadoop2 = (boolean) this.hadoopConfig
					.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.IS_HADOOP2);
			String host = "";
			String serviceName = "";
			StringBuffer jobSubmitCommand = new StringBuffer();

			if (isHadoop2) {
				serviceName = HadoopConstants.Roles.RESOURCEMANAGER;
				jobSubmitCommand.append(HadoopConstants.Command.YARN + " ");
				host = HadoopUtils.getResourceManagerHost(this.hadoopConfig);
				errMsg = "Could not submit Application: ResourceManager is down.";
			} else {
				serviceName = HadoopConstants.Roles.JOBTRACKER;
				jobSubmitCommand.append(HadoopConstants.Command.HADOOP + " ");
				host = HadoopUtils.getJobTrackerHost(this.hadoopConfig);
				errMsg = "Could not submit Job: JobTracker is down.";
			}
			boolean status = AnkushUtils.getServiceStatus(host, serviceName,
					Constant.Component.Name.HADOOP);
			result.put(HadoopConstants.ALLOW_JOB_SUBMISSION_KEY, status);
			if (!status) {
				throw new AnkushException(errMsg);
			}
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Hdfssummary.
	 */
	private void hdfssummary() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Unable to process request to get HDFS summary.";
		try {
			HadoopMonitor hadoopMonitor = HadoopUtils.getMonitorInstance(
					this.clusterConf, this.hadoopConfig);
			result.put(HadoopConstants.Hadoop.Keys.MONITORING_TABLE_DATA_KEY,
					hadoopMonitor.getHdfsMetrics());
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Processsummary.
	 */
	private void processsummary() {
		String errMsg = "Unable to get process summary.";

		String process = (String) parameterMap
				.get(HadoopConstants.Hadoop.Keys.QUERY_PARAMETER_PROCESS);
		if (process == null) {
			addAndLogError(errMsg + " Please specify process name.");
			return;
		}

		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);

		try {
			HadoopMonitor hadoopMonitor = HadoopUtils.getMonitorInstance(
					this.clusterConf, this.hadoopConfig);
			// LinkedHashMap<String, Object> processSummaryMap = new
			// LinkedHashMap<String, Object>();
			// processSummaryMap.put(HadoopConstants.Roles.NAMENODE,
			// hadoopMonitor.getNameNodeProcessSummary());

			// if(this.hadoopConfig.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.IS_HADOOP2))
			// {
			// processSummaryMap.put(HadoopConstants.Roles.RESOURCEMANAGER,
			// hadoopMonitor.getMapReduceProcessSummary());
			// } else {
			// processSummaryMap.put(HadoopConstants.Roles.JOBTRACKER,
			// hadoopMonitor.getMapReduceProcessSummary());
			// }
			// result.put(HadoopConstants.Hadoop.Keys.MONITORING_TABLE_DATA_KEY,
			// processSummaryMap);

			// result.putAll(processSummaryMap);

			// result.put(HadoopConstants.Hadoop.Keys.MONITORING_TABLE_DATA_KEY,
			// hadoopMonitor.getProcessSummary(process));
			//
			result.put(HadoopConstants.Hadoop.Keys.MONITORING_TABLE_DATA_KEY,
					hadoopMonitor.getProcessSummary(process));

		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Nodessummary.
	 */
	private void nodessummary() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Exception: Unable to process request to get nodes summary.";
		try {
			HadoopMonitor hadoopMonitor = HadoopUtils.getMonitorInstance(
					this.clusterConf, this.hadoopConfig);
			result.put(HadoopConstants.Hadoop.Keys.MONITORING_TABLE_DATA_KEY,
					hadoopMonitor.getNodesSummary());
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Submitjob.
	 */
	private void submitjob() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		try {
			HadoopMonitor hadoopMonitor = HadoopUtils.getMonitorInstance(
					this.clusterConf, this.hadoopConfig);
			List<String> jobArguments = (List<String>) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.JOBARGS);
			List<Map<String, String>> hadoopParams = (List<Map<String, String>>) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.HADOOPPARAMS);

			String uploadedJarPath = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.JARPATH);
			String jobName = (String) parameterMap
					.get(com.impetus.ankush2.constant.Constant.Keys.JOB);
			String jobType = com.impetus.ankush2.constant.Constant.Keys.JAR;
			String jarPath = null;

			boolean isHadoop2 = (boolean) this.hadoopConfig
					.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.IS_HADOOP2);

			String host = "";
			String successMsg = "";
			StringBuffer jobSubmitCommand = new StringBuffer();
			if (isHadoop2) {
				jobSubmitCommand.append(
						HadoopUtils.getHadoopBinDir(hadoopConfig)).append(
						HadoopConstants.Command.YARN + " ");
				host = HadoopUtils.getResourceManagerHost(this.hadoopConfig);
				successMsg = "Application submitted successfully.";
			} else {
				jobSubmitCommand.append(
						HadoopUtils.getHadoopBinDir(hadoopConfig)).append(
						HadoopConstants.Command.HADOOP + " ");
				host = HadoopUtils.getJobTrackerHost(this.hadoopConfig);
				successMsg = "Job submitted successfully.";
			}

			String userHome = CommonUtil.getUserHome(clusterConf.getAuthConf()
					.getUsername());

			String destJobFilePath = FileUtils
					.getSeparatorTerminatedPathEntry(userHome)
					+ ".ankush/jobs/hadoop/";

			final SSHExec connection = com.impetus.ankush2.utils.SSHUtils
					.connectToNode(host, clusterConf.getAuthConf());
			boolean isSuccessful = false;
			try {
				File f = new File(uploadedJarPath);
				if (!f.exists()) {
					throw new AnkushException("Could not find jar file.. !");
				} else {
					String fileName = FileUtils.getNamePart(uploadedJarPath);
					jarPath = destJobFilePath + fileName;

					if (connection != null) {
						// Create Directory on master node
						AnkushTask createDir = new MakeDirectory(
								destJobFilePath);
						Result res = connection.exec(createDir);
						if (!res.isSuccess) {
							throw new AnkushException(
									"Could not create job directory on node..! ");
						} else {
							// Uploading hob file to master node
							try {
								connection.uploadSingleDataToServer(
										uploadedJarPath, jarPath);
								isSuccessful = true;
							} catch (Exception e1) {
								throw new AnkushException(
										"Could not upload job file to node.. !");
							}
						}
					} else {
						throw new AnkushException(
								"Could not establish connection to node.. !");
					}
				}
			} catch (Exception e) {
				if (connection != null) {
					connection.disconnect();
				}
				throw new AnkushException("Could not submit job.. !");

			} finally {
				// Disconnect the connection
				// if (connection != null) {
				// connection.disconnect();
				// }
			}

			if (isSuccessful) {
				// Job Submission Logic
				jobSubmitCommand.append(jobType).append(" ");
				jobSubmitCommand.append(jarPath).append(" ");
				jobSubmitCommand.append(jobName).append(" ");

				if (hadoopParams != null) {
					for (Map<String, String> params : hadoopParams) {
						Iterator iter = params.keySet().iterator();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							String val = params.get(key);
							jobSubmitCommand.append(key).append(" ")
									.append(val).append(" ");
						}
					}
				}
				if (jobArguments != null) {
					for (String args : jobArguments) {
						jobSubmitCommand.append(args).append(" ");
					}
				}
				final String commad = jobSubmitCommand.toString();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						submitJob(connection, commad);
					}
				});
			}
			result.put("message", successMsg);
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(
					"Could not process request to submit application.", e);
		}
	}

	/**
	 * Submit job.
	 * 
	 * @param connection
	 *            the connection
	 * @param command
	 *            the command
	 */
	private void submitJob(SSHExec connection, String command) {
		try {
			logger.debug("Submitting Job : " + command);
			CustomTask jobTask = new ExecCommand(command);
			connection.exec(jobTask);
		} catch (TaskExecFailException e) {
			logger.error("Could not submit Job.", e);
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Techlogs.
	 */
	private void techlogs() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Unable to process request to fetch log files.";
		if (!HadoopUtils.isMonitoredByAnkush(this.hadoopConfig)) {
			addAndLogError(errMsg + " "
					+ Constant.Registration.ErrorMsg.BASIC_REGISTRATION);
			return;
		}
		try {
			HadoopMonitor hadoopMonitor = HadoopUtils.getMonitorInstance(
					this.clusterConf, this.hadoopConfig);
			result.putAll(hadoopMonitor.getRoleNodesMap());
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Files.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void files() throws Exception {

		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Unable to process request to fetch log files.";
		if (!HadoopUtils.isMonitoredByAnkush(this.hadoopConfig)) {
			addAndLogError(errMsg + " "
					+ Constant.Registration.ErrorMsg.BASIC_REGISTRATION);
			return;
		}
		try {
			String host = (String) parameterMap.get(Constant.Keys.HOST);
			String role = (String) parameterMap.get(Constant.Keys.TYPE);
			if (host == null || role == null) {
				addAndLogError("Host or Service Type is missing.");
				return;
			}

			HadoopMonitor hadoopMonitor = HadoopUtils.getMonitorInstance(
					this.clusterConf, this.hadoopConfig);
			result.put(Constant.Keys.FILES,
					hadoopMonitor.getLogFilesList(role, host));
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Application list.
	 */
	private void applist() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Could not process request to fetch application list.";
		try {
			Hadoop2Monitor hadoop2Monitor = new Hadoop2Monitor(
					this.clusterConf, this.hadoopConfig);
			result.putAll(hadoop2Monitor.getApplicationList());
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Editparams.
	 */
	private void editparams() {

		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Unable to process request to edit Hadoop configuration files.";

		if (!HadoopUtils.isManagedByAnkush(this.hadoopConfig)) {
			this.addAndLogError(errMsg + " "
					+ Constant.Registration.ErrorMsg.NOT_MANAGED_MODE);
			return;
		}

		try {
			this.clusterConf.incrementOperation();
			boolean isAgentDown = AnkushUtils.isAnyAgentDown(this.hadoopConfig
					.getNodes().keySet());
			if (isAgentDown) {
				throw new AnkushException(
						"Could not process edit parameters request: AnkushAgent is down on few nodes.");
			}

			final Map<String, Object> confParams = (Map<String, Object>) parameterMap
					.get("params");

			final String loggedUser = (String) parameterMap.get("loggedUser");

			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					final Semaphore semaphore = new Semaphore(hadoopConfig
							.getNodes().size());
					try {
						// connect with all the component nodes
						AnkushUtils.connectNodesString(clusterConf,
								hadoopConfig.getNodes().keySet());

						for (final String host : hadoopConfig.getNodes()
								.keySet()) {

							semaphore.acquire();
							AppStoreWrapper.getExecutor().execute(
									new Runnable() {
										@Override
										public void run() {
											try {
												for (Entry entry : confParams
														.entrySet()) {

													// get fileName
													String fileName = (String) entry
															.getKey();
													// get config params list
													List<Map> params = (List<Map>) entry
															.getValue();

													for (Map param : params) {
														final Parameter parameter = JsonMapperUtil
																.objectFromMap(
																		param,
																		Parameter.class);

														String status = parameter
																.getStatus();

														Result res = null;

														ConfigurationManager confManager = new ConfigurationManager();

														// get component
														// homepath
														String confDir = HadoopUtils
																.getHadoopConfDir(hadoopConfig);

														// get server.properties
														// file path
														String propertyFilePath = confDir
																+ fileName;

														// if connection is
														// established.

														switch (Constant.ParameterActionType.valueOf(status
																.toUpperCase())) {
														case ADD:
															if (addParam(
																	clusterConf
																			.getNodes()
																			.get(host),
																	Constant.Component.Name.HADOOP,
																	parameter
																			.getName(),
																	parameter
																			.getValue(),
																	propertyFilePath,
																	Constant.File_Extension.XML)) {
																confManager
																		.saveConfiguration(
																				clusterConf
																						.getClusterId(),
																				loggedUser,
																				fileName,
																				host,
																				parameter
																						.getName(),
																				parameter
																						.getValue());
															}
															break;
														case EDIT:
															if (editParam(
																	clusterConf
																			.getNodes()
																			.get(host),
																	Constant.Component.Name.HADOOP,
																	parameter
																			.getName(),
																	parameter
																			.getValue(),
																	propertyFilePath,
																	Constant.File_Extension.XML)) {
																confManager
																		.saveConfiguration(
																				clusterConf
																						.getClusterId(),
																				loggedUser,
																				fileName,
																				host,
																				parameter
																						.getName(),
																				parameter
																						.getValue());
															}
															break;
														case DELETE:
															if (deleteParam(
																	clusterConf
																			.getNodes()
																			.get(host),
																	Constant.Component.Name.HADOOP,
																	parameter
																			.getName(),
																	propertyFilePath,
																	Constant.File_Extension.XML)) {
																confManager
																		.removeOldConfiguration(
																				clusterConf
																						.getClusterId(),
																				host,
																				fileName,
																				parameter
																						.getName());
															}
															break;
														}
													}
												}
											} catch (Exception e) {
												// To be Handled : Exception for
												// Edit Parameter call
											} finally {
												if (semaphore != null) {
													semaphore.release();
												}
											}
										}
									});
						}
						semaphore.acquire(hadoopConfig.getNodes().size());
						// disconnect with all the component nodes
						AnkushUtils.disconnectCompNodes(clusterConf,
								hadoopConfig.getNodes().keySet());
					} catch (Exception e) {
						// To be Handled : Exception for Edit Parameter call
					}
				}

			});
			result.put("message",
					"Parameters update request placed successfully.");
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Params.
	 */
	private void params() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Unable to process request to read Hadoop configuration files.";
		if (!HadoopUtils.isMonitoredByAnkush(this.hadoopConfig)) {
			addAndLogError(errMsg + " "
					+ Constant.Registration.ErrorMsg.BASIC_REGISTRATION);
			return;
		}
		try {
			final String nameNodeIp = HadoopUtils
					.getNameNodeHost(this.hadoopConfig);

			final String componentConfPath = HadoopUtils
					.getHadoopConfDir(this.hadoopConfig);

			List<String> confFiles = SSHUtils.listDirectory(nameNodeIp,
					componentConfPath, clusterConf.getAuthConf());

			Map resultInfo = new HashMap();

			// Code is currently not optimized.
			// Need to run the following logic (to get contents of each file)
			// using FutureTask for each file

			for (String fileName : confFiles) {
				if (fileName.endsWith(HadoopConstants.FileName.EXTENSION_XML)
						&& (!fileName.equals("fair-scheduler.xml"))) {
					String filePath = FileUtils
							.getSeparatorTerminatedPathEntry(componentConfPath)
							+ fileName;

					String content = SSHUtils.getFileContents(filePath,
							nameNodeIp, clusterConf.getAuthConf());

					List<Parameter> fileParams = new ArrayList<Parameter>();
					if (content != null && content.length() > 0) {
						fileParams = ConfigParameterUtils
								.loadXMLParameters(content);
					}
					Map<String, List<Object>> map = new HashMap<String, List<Object>>();

					// Converting Properties into Map.
					for (Parameter parameter : fileParams) {
						List<Object> list = new ArrayList<Object>();
						list.add(parameter.getValue());
						list.add(this.isParameterEditable(parameter.getName()));
						map.put(parameter.getName(), list);
					}
					resultInfo.put(fileName, map);
				}
			}
			result.put("params", resultInfo);
		} catch (Exception e) {
			addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Checks if is parameter editable.
	 * 
	 * @param parameterName
	 *            the parameter name
	 * @return true, if is parameter editable
	 */
	private boolean isParameterEditable(String parameterName) {
		try {
			boolean result = true;

			String predefinedNonEditableParameters = AppStoreWrapper
					.getAnkushConfReader().getStringValue(
							"hadoop.non.editable.parameters");

			if (predefinedNonEditableParameters != null) {
				List<String> nonEditableParametersList = new ArrayList<String>(
						Arrays.asList(predefinedNonEditableParameters
								.split(",")));
				
				if (nonEditableParametersList.contains(parameterName)) {
					return false;
				}
			}
			return result;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * Appdetails.
	 */
	private void appdetails() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Could not process request to fetch application details.";
		try {
			String appId = (String) parameterMap
					.get(HadoopConstants.YARN.APPID);
			if (appId == null) {
				addAndLogError(errMsg + " Invalid application id.");
				return;
			}
			Hadoop2Monitor hadoop2Monitor = new Hadoop2Monitor(
					this.clusterConf, this.hadoopConfig);
			result.putAll(hadoop2Monitor.getApplicationDetails(appId));
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Hadoopjobs.
	 */
	private void hadoopjobs() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Could not process request to fetch jobs list.";
		try {
			Hadoop1Monitor hadoop1Monitor = new Hadoop1Monitor(
					this.clusterConf, this.hadoopConfig);
			result.putAll(hadoop1Monitor.getJobList());
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Jobdetails.
	 */
	private void jobdetails() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Could not process request to fetch job details.";
		try {
			String jobId = (String) parameterMap
					.get(HadoopConstants.Hadoop.Keys.JOB_ID);
			if (jobId == null) {
				addAndLogError(errMsg + " Invalid Job Id.");
				return;
			}
			Hadoop1Monitor hadoop1Monitor = new Hadoop1Monitor(
					this.clusterConf, this.hadoopConfig);
			result.putAll(hadoop1Monitor.getJobDetails(jobId));
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	/**
	 * Jobmetrics.
	 */
	private void jobmetrics() {
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		String errMsg = "Could not process request to fetch jobs metrics.";
		try {
			Hadoop1Monitor hadoop1Monitor = new Hadoop1Monitor(
					this.clusterConf, this.hadoopConfig);
			result.putAll(hadoop1Monitor.getJobMetrics());
		} catch (AnkushException e) {
			this.addErrorAndLogException(e.getMessage(), e);
		} catch (Exception e) {
			this.addErrorAndLogException(errMsg, e);
		}
	}

	@Override
	public boolean canNodesBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes, String componentName) {
		this.clusterConf = clusterConfig;
		this.hadoopConfig = HadoopUtils.getHadoopConfig(this.clusterConf);
		boolean result = true;
		for (String host : nodes) {
			StringBuilder errorMsg = new StringBuilder(
					"Could not delete node - "
							+ host
							+ ": Operation not permitted for nodes with role(s) - ");
			Set<String> roles = new HashSet<String>();

			if (HadoopUtils.isNameNode(hadoopConfig, host)) {
				roles.add(HadoopConstants.Roles.NAMENODE);
			}
			if (HadoopUtils.getSecondaryNameNodeHost(hadoopConfig) != null) {
				if (HadoopUtils.isSecondaryNameNode(hadoopConfig, host)) {
					roles.add(HadoopConstants.Roles.SECONDARYNAMENODE);
				}
			}
			if (HadoopUtils.isHadoop2Config(hadoopConfig)) {
				if (HadoopUtils.isHdfsHaEnabled(hadoopConfig)) {
					if (HadoopUtils.isJournalNode(hadoopConfig, host)) {
						roles.add(HadoopConstants.Roles.JOURNALNODE);
					}
				}
				if (HadoopUtils.isResourceManagerNode(hadoopConfig, host)) {
					roles.add(HadoopConstants.Roles.RESOURCEMANAGER);
				}
				if (hadoopConfig
						.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.WEBAPPPROXYSERVER_ENABLED)) {
					if (HadoopUtils.isWebAppProxyServerNode(hadoopConfig, host)) {
						roles.add(HadoopConstants.Roles.WEBAPPPROXYSERVER);
					}
				}
				if (hadoopConfig
						.getAdvanceConfBooleanProperty(HadoopConstants.AdvanceConfKeys.JOBHISTORYSERVER_ENABLED)) {
					if (HadoopUtils.isJobHistoryServerNode(hadoopConfig, host)) {
						roles.add(HadoopConstants.Roles.JOBHISTORYSERVER);
					}
				}
			} else {
				if (HadoopUtils.isJobTrackerNode(hadoopConfig, host)) {
					roles.add(HadoopConstants.Roles.JOBTRACKER);
				}
			}
			if (roles.size() > 0) {
				errorMsg.append(StringUtils.join(roles, ", "));
				this.errors.add(errorMsg.toString());
				result = false;
			}
		}
		return result;
	}

}
