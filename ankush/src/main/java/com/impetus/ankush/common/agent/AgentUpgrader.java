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
package com.impetus.ankush.common.agent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.framework.ComponentConfigurator;
import com.impetus.ankush.common.framework.ComponentUpgrader;
import com.impetus.ankush.common.framework.ObjectFactory;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.Copy;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.Unzip;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush2.constant.Constant.Component;

/**
 * API to upgrade agent on clusters
 * 
 * @author hokam
 * 
 */
public class AgentUpgrader {

	private static final String MESSAGE_UPGRADING_AGENT = "Upgrading agent";

	/** The LOGGER. */
	private AnkushLogger LOGGER = new AnkushLogger(Component.Name.AGENT,
			AgentUpgrader.class);

	/** The cluster manager. */
	protected GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/** The node manager. */
	protected GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	// ankush home on node.
	public static final String NODE_ANKUSH_HOME = "$HOME/.ankush/";

	// current agent version
	String agentBuildVersion = new String();

	/**
	 * Agent Upgrader
	 */
	public AgentUpgrader() {
		// Agent build version.
		this.agentBuildVersion = AppStoreWrapper.getAgentBuildVersion();
	}

	/**
	 * Method to upgrade agent asynchronously.
	 */
	public void asyncUpgradeAgent() {
		// Calling executor to upgrade agent.
		AppStoreWrapper.getExecutor().execute(new Runnable() {

			@Override
			public void run() {
				// call upgrade agent.
				upgradeAgent();
			}
		});
	}

	/**
	 * Update agent implementation.
	 */
	public void upgradeAgent() {
//		// list of clusters/
//		List<Cluster> clusters = clusterManager.getAllByPropertyValue(
//				Constant.Keys.STATE, Constant.Cluster.State.DEPLOYED);
//		
//		// iterate over the clusters
//		for (Cluster cluster : clusters) {
//			try {
//				// if cluster agent version and agent cluster version is
//				// different
//				if (!agentBuildVersion.equals(cluster.getAgentVersion())) {
//					// Cluster conf Object.
//					ClusterConf clusterConf = cluster.getClusterConf();
//					// set new operation id.
//					clusterConf.setOperationId(LOGGER.getNewOperationId(cluster
//							.getId()));
//					// Set ClusterConf in logger.
//					LOGGER.setCluster(clusterConf);
//					// log message.
//					LOGGER.info(MESSAGE_UPGRADING_AGENT + "...");
//
//					// set cluster state as upgrading agent
//					cluster.setState(Constant.Cluster.State.MAINTENANCE);
//					clusterManager.save(cluster);
//					// cluster nodes.
//					Set<Node> nodes = cluster.getNodes();
//
//					// iterate over the nodes to upgrade.
//					for (Node node : nodes) {
//						NodeConf nodeConf = node.getNodeConf();
//						nodeConf.setStatus(true);
//						node.setNodeConf(nodeConf);
//						nodeManager.save(node);
//						// node upgrader task.
//						NodeUpgrader nodeUpgrader = new NodeUpgrader(node,
//								cluster);
//						// executing task.
//						nodeUpgrader.upgrade();
//					}
//					// Getting overall status.
//					boolean status = status(nodes);
//
//					// if success
//					if (status) {
//						// set agent build version.
//						cluster.setAgentVersion(agentBuildVersion);
//						// set cluster state as deployed
//						cluster.setState(Constant.Cluster.State.DEPLOYED);
//					}
//
//					LOGGER.log(MESSAGE_UPGRADING_AGENT, status);
//					clusterManager.save(cluster);
//				}
//			} catch (Exception e) {
//				LOGGER.error(e.getMessage(), e);
//			}
//		}
	}

	/**
	 * Status.
	 * 
	 * @param keySet
	 *            Set<NodeConf>
	 * @return boolean
	 * @author hokam chauhan Method status.
	 */
	private boolean status(Set<Node> nodes) {
		boolean status = true;
		for (Node node : nodes) {
			status = status && node.getNodeConf().getStatus();
		}
		return status;
	}

	/**
	 * Node Upgrader thread
	 */
	class NodeUpgrader {

		private static final String NEW_AGENT_FOLDER = NODE_ANKUSH_HOME
				+ "/.newagent/";
		private static final String AGENT_BACKUP_FOLDER = NODE_ANKUSH_HOME
				+ "/agentBackup";
		private static final String NEW_AGENT_ZIP = NODE_ANKUSH_HOME
				+ "agent.zip";

		private Node node;
		private Cluster cluster;
		private ClusterConf clusterConf;
		private NodeConf nodeConf;

		/**
		 * @param node
		 * @param cluster
		 * @param clusterConf
		 */
		public NodeUpgrader(Node node, Cluster cluster) {
			super();
			this.node = node;
			this.nodeConf = node.getNodeConf();
			nodeConf.setErrors(new HashMap<String, String>());
			this.cluster = cluster;
			this.clusterConf = cluster.getClusterConf();
		}

		/**
		 * Method to Roll back agent folder
		 * 
		 * @param connection
		 * @param conf
		 * @throws Exception
		 */
		private void rollBackAgent(SSHExec connection, AgentConf conf)
				throws Exception {
			// remove updated agent folder.
			Remove remove = new Remove(conf.getInstallationPath());
			// execute task
			connection.exec(remove);

			// backup old existing agent folder.
			Copy backUpAgent = new Copy(AGENT_BACKUP_FOLDER,
					conf.getInstallationPath(), true);

			// execute task.
			connection.exec(backUpAgent);
		}

		/**
		 * Gets the components.
		 * 
		 * @return the components
		 */
		private Map<String, GenericConfiguration> getComponentsConfiguration(
				NodeConf nodeConf) {
			// Empty component hash map.
			Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();
			// cluster components
			Map<String, GenericConfiguration> clusterComponents = cluster
					.getClusterConf().getClusterComponents();
			// iterating over the cluster components.
			for (String componentName : clusterComponents.keySet()) {
				// Get component configuration.
				GenericConfiguration compConf = clusterComponents
						.get(componentName);
				// if node is part of this component then set it in its
				// component.
				if (compConf.getCompNodes().contains(nodeConf)) {
					components.put(componentName, compConf);
				}
			}
			// GangliaConf object.
			GenericConfiguration gangliaConf = null;
			// Getting GangliaConf from common components.
			if (cluster.getClusterConf().getCommonComponents() != null) {
				gangliaConf = cluster.getClusterConf().getCommonComponents()
						.get(Component.Name.GANGLIA);
			}

			// if GangliaConf is not available in common components then taking
			// it from the ComponentConfigurator.
			if (gangliaConf == null) {
				gangliaConf = ComponentConfigurator.getDefaultGangliaConf(
						cluster.getName(), cluster.getClusterConf()
								.getUsername());
			}
			// setting GangliaConf
			components.put(Component.Name.GANGLIA, gangliaConf);
			// return components.
			return components;
		}

		/**
		 * Method to Upgrade Agent on node.
		 */
		public void upgrade() {
			String publicIp = nodeConf.getPublicIp();
			LOGGER.info(publicIp, MESSAGE_UPGRADING_AGENT + "...");
			boolean status = false;
			// connection
			SSHExec connection = null;
			try {
				// Agent version.
				String installedAgentVersion = node.getAgentVersion();

				// if cluster agent version and agent cluster version is
				// different
				if (!agentBuildVersion.equals(installedAgentVersion)) {

					// connecting to node.
					connection = SSHUtils.connectToNode(publicIp,
							clusterConf.getUsername(),
							clusterConf.getPassword(),
							clusterConf.getPrivateKey());

					// if connection not null.
					if (connection != null) {
						// AgentConf
						AgentConf conf = ComponentConfigurator
								.getAgentConf(clusterConf);

						LOGGER.info(publicIp,
								"Stopping agent and doing backup of agent...");
						/* Creating the Kill Process Command for AnkushAgent */
						CustomTask killProcess = new ExecCommand("sh "
								+ Constant.Agent.AGENT_STOP_SCRIPT);

						// backup old existing agent folder.
						Copy backUpAgent = new Copy(conf.getInstallationPath(),
								AGENT_BACKUP_FOLDER, true);

						// stop agent
						connection.exec(killProcess);

						// Backup command.
						Result result = connection.exec(backUpAgent);
						status = result.isSuccess;
						LOGGER.log(publicIp,
								"Stopping agent and doing backup of agent",
								status);

						// if stopped and backup done
						if (status) {
							// upgrade agent script path.
							String upgradeScriptDirectory = NODE_ANKUSH_HOME
									+ "upgrade/";
							status = connection.exec(new MakeDirectory(
									upgradeScriptDirectory)).rc == 0;

							if (status) {
								LOGGER.info(publicIp, "Copying agent bundle...");

								/* Uploading the jar files to node */
								connection.uploadSingleDataToServer(
										conf.getServerTarballLocation(),
										NODE_ANKUSH_HOME);

								// new agent path
								String newAgentPath = NEW_AGENT_FOLDER;
								// unzip and remove task creation.
								AnkushTask unzipTask = new Unzip(NEW_AGENT_ZIP,
										newAgentPath);

								// executing unzip and task creation task.
								result = connection.exec(unzipTask);
								status = result.isSuccess;

								if (status) {
									// Agent Upgrade Script Path.
									String updateScriptPath = AppStoreWrapper
											.getResourcePath();
									// agent update script path.
									updateScriptPath = updateScriptPath
											+ "scripts/agent/upgrade.sh";

									// Getting the dependency file name for
									// the fetched OS name
									File file = new File(updateScriptPath);

									// if file exists.
									if (file.exists()) {
										// node update script path.
										String nodeUpdateScriptPath = upgradeScriptDirectory
												+ FilenameUtils
														.getName(updateScriptPath);

										LOGGER.info(publicIp,
												"Copying upgrade script...");
										// upload file to node.
										connection.uploadSingleDataToServer(
												updateScriptPath,
												nodeUpdateScriptPath);

										// List of upgrade commands
										List<String> upgradeCommands = FileUtils
												.readLines(new File(
														updateScriptPath));

										// Iterating over the commands.
										for (String command : upgradeCommands) {
											// executing script.
											result = connection
													.exec(new ExecCommand(
															command));

											if (result.rc != 0) {
												status = false;
												nodeConf.addError(
														"EXECUTE_SCRIPT",
														result.error_msg);
												break;
											}
										}

										LOGGER.log(publicIp,
												"Executing upgrade script",
												status);
									}

									// Get the components
									Map<String, GenericConfiguration> components = getComponentsConfiguration(node
											.getNodeConf());

									// Iterate over the components to
									// create the component shell script
									// for agent upgrade and execute it.
									for (String component : components.keySet()) {
										try {
											// Getting deployer object.
											ComponentUpgrader upgrader = ObjectFactory
													.getComponentUpgrader(component);

											// if UPGRADER is not null.
											if (upgrader != null) {
												LOGGER.info(
														publicIp,
														"Copying and executing "
																+ component
																+ " upgrade script...");
												// Getting list of
												// Commands
												// which is required for
												// upgrading the agent
												List<String> commands = upgrader
														.getUpgradeCommands(
																node.getPublicIp(),
																components
																		.get(component));
												// if commands are not null.
												if (commands != null) {
													// convert into
													// lines.
													String scriptContent = StringUtils
															.join(commands,
																	"\n");

													// component script
													// path
													String compScriptPath = upgradeScriptDirectory
															+ component + ".sh";
													// append task.
													AppendFileUsingEcho appendTask = new AppendFileUsingEcho(
															scriptContent,
															compScriptPath);
													// execute task
													status = status
															& connection
																	.exec(appendTask).isSuccess;

													// Iterating over the
													// commands.
													for (String command : commands) {
														// executing script.
														Result rs = connection
																.exec(new ExecCommand(
																		command));

														if (rs.rc != 0) {
															status = false;
															nodeConf.addError(
																	"EXECUTE_"
																			+ component
																			+ "_SCRIPT",
																	result.error_msg);
															break;
														}
													}
													LOGGER.log(
															publicIp,
															"Copying and executing "
																	+ component
																	+ " upgrade script",
															status);
												}
											}
										} catch (Exception e) {
											status = false;
											nodeConf.addError("EXCEPTION1",
													e.getMessage());
											LOGGER.error(publicIp,
													e.getMessage(), e);
										}
									}
								} else {
									nodeConf.addError("UNZIP_AGENT",
											result.error_msg);
								}
							}
							if (!status) {
								LOGGER.info(publicIp, "Doing rollback...");
								// roll back the agent with backup
								rollBackAgent(connection, conf);
							} else {
								LOGGER.info(publicIp,
										"Removing agent upgrade script folder...");
								// remove agent upgrade script folder.
								Remove remove = new Remove(
										upgradeScriptDirectory);
								connection.exec(remove);
							}

							LOGGER.info(publicIp,
									"Removing agent backup folder...");
							// remove agent backup folder.
							Remove removeAgentBackupFolder = new Remove(
									AGENT_BACKUP_FOLDER);

							connection.exec(removeAgentBackupFolder);
						} else {
							nodeConf.addError("AGENT_STOP", result.error_msg);
						}
						LOGGER.info(publicIp, "Starting agent...");

						// start agent
						String startAgent = "sh "
								+ Constant.Agent.AGENT_START_SCRIPT;
						// create task
						CustomTask task = new ExecCommand(startAgent);
						// execute start command
						result = connection.exec(task);
						// if not started
						if (!result.isSuccess) {
							status = false;
							nodeConf.addError("AGENT_START", result.error_msg);
							LOGGER.error(publicIp, "Could not start the agent.");
						} else {
							status = true;
						}
					} else {
						nodeConf.addError("CONNECTION",
								"Unable to make connection with node.");
						status = false;
					}
				} else {
					status = true;
				}
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				nodeConf.addError("EXCEPTION2", e.getMessage());
				status = false;
			} finally {
				// if connection is not null.
				if (connection != null) {
					connection.disconnect();
				}
			}

			// update the agent version
			if (status) {
				node.setAgentVersion(agentBuildVersion);
			} else {
				// Add agent upgrade error message.
				nodeConf.addError(
						Component.Name.AGENT,
						"Please run the upgrade scripts available under "
								+ CommonUtil.getUserHome(cluster
										.getClusterConf().getUsername())
								+ ".ankush/upgrade/ directory to upgrade the agent manually.");
			}
			// set status.
			nodeConf.setStatus(status);
			// set NodeConf in node
			node.setNodeConf(nodeConf);
			// save Node in database
			nodeManager.save(node);
			// log message
			LOGGER.log(publicIp, MESSAGE_UPGRADING_AGENT, status);
		}
	}
}
