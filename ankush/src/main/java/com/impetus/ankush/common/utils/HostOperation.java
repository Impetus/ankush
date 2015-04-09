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
package com.impetus.ankush.common.utils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.AuthConfig;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.utils.SSHUtils;

/**
 * The Class HostOperation.
 */
public class HostOperation {

	/** The log. */
	private AnkushLogger logger = new AnkushLogger(HostOperation.class);

	/** The Constant INDEX_AUTHENTICATED. */
	private static final int INDEX_AUTHENTICATED = 3;

	/** The Constant INDEX_OS_NAME. */
	private static final int INDEX_OS_NAME = 4;

	/** The avaiable nodes. */
	private Set<String> avaiableNodes;
	// node pattend for getting nodes.
	/** The node pattern. */
	private String nodePattern;

	private Set<String> hostsSet;

	private Map<String, Boolean> hostValidationMap;

	// username
	/** The user name. */
	private String userName;
	// password
	/** The password. */
	private String password;

	// is auth type is password or not.
	/** The is auth type pass. */
	private Boolean isAuthTypePass;
	// file path of rack map.
	/** The file path rack map. */
	private String filePathRackMap;
	// generic rack awareness.

	/** The formator. */
	private DecimalFormat formator = new DecimalFormat("#.##");

	private static String Invalid_Host_Pattern_String = "Invalid host pattern, unable to get host list.";

	private static String Invalid_Host = "Host is invalid.";

	private static String Valid_Host = "Host is valid";

	private static String Unavailable_Host = "Host is already in use.";

	private static String UnreachableHost = "Host is not reachable.";

	private static String Unauthenticated_Host = "Host is unauthenticated.";

	/**
	 * Detect nodes.
	 * 
	 * @param parameters
	 *            the parameters
	 * @return the map
	 */

	public static String getOsForNode(String host, AuthConfig authConfig) {
		return "";
	}

	// public static boolean setSysHostNameForNodes(List<NodeConf> nodeConfs,
	// ClusterConf conf) {
	// try {
	// for (NodeConf nodeConf : nodeConfs) {
	// String hostName = HostOperation.getMachineHostName(nodeConf,
	// conf.getUsername(), conf.getPassword(),
	// conf.getPrivateKey());
	// nodeConf.setSystemHostName(hostName);
	// }
	// } catch (Exception e) {
	// return true;
	// }
	// return true;
	// }

	// public static String getMachineHostName(NodeConf source, String username,
	// String password, String privateKey) {
	//
	// SSHExec connection = null;
	// try {
	// connection = SSHUtils.connectToNode(source.getPublicIp(), username,
	// password, privateKey);
	// if (connection == null) {
	// return "";
	// }
	// CustomTask task = new ExecCommand(
	// Constant.SystemCommands.GETHOSTNAME);
	// Result res = connection.exec(task);
	// if (res.isSuccess) {
	// if (!res.sysout.isEmpty()) {
	// return res.sysout.trim().replaceAll("\n", "");
	// }
	// }
	// } catch (Exception e) {
	// return "";
	// } finally {
	// if (connection != null) {
	// connection.disconnect();
	// }
	// }
	// return "";
	// }

	public Map<String, Object> detectNodes(Map parameters) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			this.avaiableNodes = getDatabaseNodes();

			if (parameters.containsKey("clusterId")) {
				// Get cluster manager
				GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
						.getManager(Constant.Manager.CLUSTER, Cluster.class);

				// get cluster id string
				String clusterIdStr = (String) parameters.get("clusterId");
				// convert cluster id string into long value.
				Long clusterId = ParserUtil.getLongValue(clusterIdStr, 0);
				// Get the cluster object from database.
				Cluster cluster = clusterManager.get(clusterId);
				if ((cluster.getState()
						.equals(Constant.Cluster.State.ERROR
								.toString()))
						|| (cluster.getState()
								.equals(Constant.Cluster.State.SERVER_CRASHED.toString()))) {
					this.avaiableNodes.removeAll(getClusterNodes(clusterId));
				} else {
					ClusterConfig clusterConf = cluster.getClusterConfig();
					// set username
					parameters.put("userName", clusterConf.getAuthConf()
							.getUsername());
					String pass = clusterConf.getAuthConf().getPassword();
					if (pass != null && !pass.isEmpty()) {
						parameters.put("password", pass);
						parameters.put("authTypePassword", true);
					} else {
						parameters.put("password", clusterConf.getAuthConf()
								.getPrivateKey());
						parameters.put("authTypePassword", false);
					}
				}
			}

			this.nodePattern = (String) parameters.get("nodePattern");
			this.userName = (String) parameters.get("userName");
			this.password = (String) parameters.get("password");
			Boolean isFile = (Boolean) parameters.get("isFileUploaded");
			isAuthTypePass = (Boolean) parameters.get("authTypePassword");
			if (isAuthTypePass == null) {
				isAuthTypePass = true;
			}
			Boolean isRackEnabled = (Boolean) parameters.get("isRackEnabled");
			if (isRackEnabled == null) {
				isRackEnabled = false;
			}

			if (isFile) {
				try {
					List<String> lines = FileUtils.loadLines(nodePattern);
					this.nodePattern = "";
					for (String line : lines) {
						this.nodePattern += line.trim()
								+ com.impetus.ankush2.constant.Constant.Strings.COMMA;
					}
				} catch (IOException e) {
					List<String> error = new ArrayList<String>();
					error.add(e.getMessage() != null ? e.getMessage()
							+ HostOperation.Invalid_Host_Pattern_String
							: HostOperation.Invalid_Host_Pattern_String);
					result.put("error", error);
					// logger.error(Constant.Keys.GENERAL_EXCEPTION_STRING, e);
				}
			}
			// split the nodePattern on Comma
			String[] splittedNodeArray = this.nodePattern
					.split(com.impetus.ankush2.constant.Constant.Strings.COMMA);

			this.hostsSet = new HashSet<String>();
			this.hostValidationMap = new HashMap<String, Boolean>();
			this.nodePattern = "";
			// trim each token of nodePattern and put in the hostList
			for (int i = 0; i < splittedNodeArray.length; i++) {
				String host = splittedNodeArray[i].trim();
				// validate host and put in hostvalidationMap
				Boolean isHostValid = validateHost(host);
				this.hostValidationMap.put(host, isHostValid);
				// append the host to node pattern only if host is valid
				if (isHostValid) {
					this.hostsSet.add(host);
					this.nodePattern += host
							+ com.impetus.ankush2.constant.Constant.Strings.SPACE;
				}
			}
			if (isRackEnabled) {
				this.filePathRackMap = (String) parameters
						.get("filePathRackMap");
			} else {
				this.filePathRackMap = null;
			}
			result.putAll(retrieveNodes());
			return result;
		} catch (Exception e) {
			List<String> error = new ArrayList<String>();
			error.add(e.getMessage() != null ? e.getMessage()
					+ HostOperation.Invalid_Host_Pattern_String
					: HostOperation.Invalid_Host_Pattern_String);
			result.put("error", error);
			// logger.error(Constant.Keys.GENERAL_EXCEPTION_STRING, e);
		}
		return result;

	}

	/**
	 * Validate host.
	 * 
	 * @param host
	 *            the host
	 * @return the boolean
	 */
	private Boolean validateHost(String host) {
		String ValidHostnameRegex = "^[a-zA-Z]$|^[a-zA-Z][a-zA-Z0-9\\-\\.]*[a-zA-Z0-9]$";
		// Create a Pattern object
		Pattern pattern = Pattern.compile(ValidHostnameRegex);

		// Now create matcher object.
		java.util.regex.Matcher m = pattern.matcher(host);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the database nodes.
	 * 
	 * @return the database nodes
	 */
	private Set<String> getDatabaseNodes() {
		GenericManager<Node, Long> nodeManager = AppStoreWrapper.getManager(
				Constant.Manager.NODE, Node.class);
		Set<String> totalNodes = new HashSet<String>();
		List<Node> nodes = nodeManager.getAll();
		for (Node node : nodes) {
			totalNodes.add(node.getPrivateIp());
		}
		return totalNodes;
	}

	/**
	 * Gets the database nodes.
	 * 
	 * @return the database nodes
	 */
	private Set<String> getClusterNodes(Long clusterId) {
		GenericManager<Node, Long> nodeManager = AppStoreWrapper.getManager(
				Constant.Manager.NODE, Node.class);
		Set<String> totalNodes = new HashSet<String>();
		List<Node> nodes = nodeManager.getAllByPropertyValue("clusterId",
				clusterId);
		for (Node node : nodes) {
			totalNodes.add(node.getPrivateIp());
		}
		return totalNodes;
	}

	/**
	 * Method to retrieve the nodes with reachability, availability,
	 * authenticity, os name and rack id.
	 * 
	 * @return the map
	 */
	private Map<String, Object> retrieveNodes() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// create result hash map.
		List<String> error = null;
		// creating rack awareness reference.
		final List<Object> resultList = new ArrayList<Object>();
		// creating nmap util object.
		NmapUtil nmap = new NmapUtil(nodePattern.replace(
				com.impetus.ankush2.constant.Constant.Strings.COMMA,
				com.impetus.ankush2.constant.Constant.Strings.SPACE));

		try {
			// getting hash map of ip as key and status as value.
			final Map<String, Boolean> nodeStatusMap = nmap.getNodeListWithStatus();
			System.out.println("nodeStatusMap :" + nodeStatusMap);
			if (nodeStatusMap.size() == 0) {
				error = new ArrayList<String>();
				error.add(HostOperation.Invalid_Host_Pattern_String);
				resultMap.put("error", error);
			}
			final Semaphore semaphore = new Semaphore(this.hostsSet.size());
			// iterating over the nodes using threading.
			for (final String host : this.hostsSet) {
				semaphore.acquire();
				// starting the thread.
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						try {
							// creating list object.
							List<Object> listObject = new ArrayList<Object>();
							String statusMessage = "";
							boolean isSudoers = false;
							listObject.add(host);
							// getting availability.
							boolean isAvailable = !avaiableNodes.contains(host);
							boolean isRechable = false;
							// adding items in list.
							listObject.add(isAvailable);

							if (nodeStatusMap.keySet().contains(host)) {
								// getting reachability.
								isRechable = nodeStatusMap.get(host);
							}
							listObject.add(isRechable);
							listObject.add(false);
							listObject.add("");
							listObject.add("");
							listObject.add("");
							
							// if reachable and valid host{validity is checked
							// via hostName regex earlier} then adding the
							// authenticity and os name.
							if (isRechable && hostValidationMap.get(host)) {
								// getting the os name.
								String osName = SSHUtils.getOS(host, userName,
										password, isAuthTypePass);

								// if os name is not null then set auth and os
								// name.
								if (osName != null && !osName.isEmpty()) {
									listObject.set(INDEX_AUTHENTICATED, true);
									listObject.set(INDEX_OS_NAME, osName);
									// if host is valid,reachable and
									// authenticated
									statusMessage = HostOperation.Valid_Host;
								} else {
									// if host is valid,reachable but
									// unauthenticated
									statusMessage = HostOperation.Unauthenticated_Host;
								}
								// adding the cpu cores
								listObject.add(getCpuCores(host));

								// adding the disk count
								listObject.add(getDiskCount(host));

								// adding the disk size
								listObject.add(getDiskSize(host));

								// adding the ram size
								listObject.add(getTotalMemory(host));
								isSudoers = checkSudoers(host);

							} else {
								listObject.add("");
								listObject.add("");
								listObject.add("");
								listObject.add("");
							}
							if (hostValidationMap.get(host) && !isAvailable) {
								// if host is valid,reachable but already in use
								statusMessage = HostOperation.Unavailable_Host;
							} else if (hostValidationMap.get(host)
									&& !isRechable) {
								// if host is valid,but not reachable
								statusMessage = HostOperation.UnreachableHost;
							} else if (!hostValidationMap.get(host)) {
								// host is invalid as per regex of hostName
								statusMessage = HostOperation.Invalid_Host;
							}
							listObject.add(statusMessage);
							listObject.add(isSudoers);
							// adding the node list in head list.
							resultList.add(listObject);
						} catch (Exception e) {
							// logger.error(
							// Constant.Keys.GENERAL_EXCEPTION_STRING, e);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			// waiting for all node threads to complete.
			semaphore.acquire(hostsSet.size());
		} catch (Exception e) {
			// logger.error(Constant.Keys.GENERAL_EXCEPTION_STRING, e);
			error = new ArrayList<String>();
			error.add(e.getMessage() != null ? e.getMessage()
					+ Invalid_Host_Pattern_String : Invalid_Host_Pattern_String);
		}
		// if result list is empty and error is null setting generic message.
		if (resultList.isEmpty() && error == null) {
			error = new ArrayList<String>();
			error.add(Invalid_Host_Pattern_String);
		}
		String invalidHostString = "";
		for (String key : hostValidationMap.keySet()) {
			if (!this.hostsSet.contains(key)) {
				invalidHostString += (key
						+ com.impetus.ankush2.constant.Constant.Strings.COMMA + com.impetus.ankush2.constant.Constant.Strings.SPACE);
			}
		}
		if (!invalidHostString.isEmpty() && invalidHostString.length() > 0) {
			invalidHostString = invalidHostString.substring(0,
					invalidHostString.length() - 2);
			error = new ArrayList<String>();
			error.add("Invalid host(s):" + invalidHostString);
		}
		// putting nodes.
		resultMap.put("nodes", resultList);
		// putting error.
		resultMap.put("error", error);
		return resultMap;
	}

	protected boolean checkSudoers(String host) {

		Result res = null;
		// requires tty check by executing a sudo command
		boolean status = false;
		// String message =
		// "Requiretty is enabled. Unable to get tty session on machine.";
		// CustomTask ttyTask = new ExecSudoCommand(password,
		// "grep 'requiretty' /etc/sudoers");
		try {
			SSHExec conn = null;
			if (isAuthTypePass) {
				conn = SSHUtils.connectToNode(host, userName, password, null);
			} else {
				conn = SSHUtils.connectToNode(host, userName, null, password);
			}
			// if (conn != null) {
			// res = conn.exec(ttyTask);
			// if(res.rc == 0){
			// //check sudo command
			// }
			// }
			CustomTask ttyTask = new ExecSudoCommand(password, "ls");
			// if connected.
			if (conn != null) {
				Result rs;
				rs = conn.exec(ttyTask);
				status = (rs.rc == 0);

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	private String getTotalMemory(String hostname) {
		SSHConnection ssc = new SSHConnection(hostname, userName, password,
				isAuthTypePass);
		Double memory = new Double(0);
		if (ssc.isConnected()) {
			ssc.exec("cat /proc/meminfo | grep MemTotal | awk '{print $2}'");

			if (ssc.getExitStatus() == 0) {
				memory = Double.parseDouble(ssc.getOutput());
				memory = memory / 1024 / 1024;
				return formator.format(memory);
			} else {
				logger.error(ssc.getError());
			}
		}
		return "";
	}

	private String getDiskSize(String hostname) {
		SSHConnection ssc = new SSHConnection(hostname, userName, password,
				isAuthTypePass);

		if (ssc.isConnected()) {
			ssc.exec("df -k --total | grep total | awk '{print $2}'");

			if (ssc.getExitStatus() == 0) {
				Double size = new Double(ssc.getOutput());
				size = size / 1024 / 1024;
				return formator.format(size);
			} else {
				logger.error("Failed to get disk size : " + ssc.getError());
			}
		}
		return "";
	}

	private String getDiskCount(String hostname) {
		SSHConnection ssc = new SSHConnection(hostname, userName, password,
				isAuthTypePass);
		String diskCount = "";
		if (ssc.isConnected()) {
			if (isAuthTypePass) {
				ssc.exec("echo " + password
						+ " | sudo -S fdisk -l | grep 'Device Boot'");
			} else {
				ssc.exec("echo '' | sudo -S fdisk -l | grep 'Device Boot'");
			}

			if (ssc.getExitStatus() == 0) {
				String arr[] = ssc.getOutput().split("\n");
				diskCount = diskCount + arr.length;
			} else {
				logger.error("Failed to get disk count : " + ssc.getError());
			}
		}
		return diskCount;
	}

	private String getCpuCores(String hostname) {
		SSHConnection ssc = new SSHConnection(hostname, userName, password,
				isAuthTypePass);
		String cores = "";
		if (ssc.isConnected()) {
			ssc.exec("nproc");

			if (ssc.getExitStatus() == 0) {
				cores = ssc.getOutput();
			} else {
				logger.error("Failed to get CPU cores : " + ssc.getError());
			}
		}
		return cores;
	}
}
