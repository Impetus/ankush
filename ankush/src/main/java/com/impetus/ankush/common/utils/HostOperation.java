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

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;
import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.hadoop.GenericRackAwareness;

/**
 * The Class HostOperation.
 */
public class HostOperation {

	/** The log. */
	private static AnkushLogger logger = new AnkushLogger(HostOperation.class);

	/** The Constant INDEX_AUTHENTICATED. */
	private static final int INDEX_AUTHENTICATED = 3;

	/** The Constant INDEX_OS_NAME. */
	private static final int INDEX_OS_NAME = 4;

	/** The avaiable nodes. */
	private Set<String> avaiableNodes;
	// node pattend for getting nodes.
	/** The node pattern. */
	private String nodePattern;
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
	private GenericRackAwareness objRackAwareness;

	/** The formator. */
	private DecimalFormat formator = new DecimalFormat("#.##");

	/**
	 * Detect nodes.
	 * 
	 * @param parameters
	 *            the parameters
	 * @return the map
	 */

	public static String getAnkushHostName(String privateIp) {
		String hostName = "ankush-";
		hostName += privateIp.replace('.', '-');
		return hostName;
	}

	public static String getMachineHostName(NodeConf source, String username,
			String password, String privateKey) {

		SSHExec connection = null;
		try {
			connection = SSHUtils.connectToNode(source.getPublicIp(), username,
					password, privateKey);
			if (connection == null) {
				return "";
			}
			CustomTask task = new ExecCommand(
					Constant.SystemCommands.GETHOSTNAME);
			Result res = connection.exec(task);
			if (res.isSuccess) {
				if (!res.sysout.isEmpty()) {
					return res.sysout.trim().replaceAll("\n", "");
				}
			}
		} catch (Exception e) {
			return "";
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return "";
	}

	public Map<String, Object> detectNodes(Map parameters) {

		logger.info(parameters.toString());
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

			if (cluster.getState().equals(Constant.Cluster.State.ERROR)) {
				this.avaiableNodes.removeAll(getClusterNodes(clusterId));
			} else {
				ClusterConf clusterConf = cluster.getClusterConf();
				// set username
				parameters.put("userName", clusterConf.getUsername());
				String pass = clusterConf.getPassword();
				if (pass != null && !pass.isEmpty()) {
					parameters.put("password", pass);
					parameters.put("authTypePassword", true);
				} else {
					parameters.put("password", clusterConf.getPrivateKey());
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
				nodePattern = "";
				for (String line : lines) {
					nodePattern += line.trim() + " ";
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		if (isRackEnabled) {
			this.filePathRackMap = (String) parameters.get("filePathRackMap");
		} else {
			this.filePathRackMap = null;
		}
		result.putAll(retrieveNodes());
		return result;
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
		// create result hash map.
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<String> error = null;
		// creating rack awareness reference.
		final List<Object> resultList = new ArrayList<Object>();
		// creating nmap util object.
		NmapUtil nmap = new NmapUtil(nodePattern);
		if (filePathRackMap != null) {
			try {
				// Initialising the rack awareness object.
				objRackAwareness = new GenericRackAwareness(
						this.filePathRackMap);

			} catch (Exception e) {
				// putting nodes.
				resultMap.put("nodes", resultList);
				// putting error.
				error = new ArrayList<String>();
				error.add("Unable to get node / rack mapping.");
				resultMap.put("error", error);
				return resultMap;
			}
		}

		// getting hash map of ip as key and status as value.
		final Map<String, Boolean> nodeStatusMap = nmap.getNodeListWithStatus();
		try {
			final Semaphore semaphore = new Semaphore(nodeStatusMap.size());
			// iterating over the nodes using threading.
			for (final String ip : nodeStatusMap.keySet()) {
				semaphore.acquire();
				// starting the thread.
				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						try {
							// getting reachability.
							boolean isRechable = nodeStatusMap.get(ip);
							// getting availability.
							boolean isAvailable = !avaiableNodes.contains(ip);
							// creating list object.
							List<Object> listObject = new ArrayList<Object>();
							// adding items in list.
							listObject.add(ip);
							listObject.add(isAvailable);
							listObject.add(isRechable);
							listObject.add(false);
							listObject.add("");

							// Adding Rack Information to the Node Retrieve
							// Event

							if (filePathRackMap != null) {
								// getting rackinfo.
								listObject.add(objRackAwareness
										.getDatacenter(ip));
								listObject.add(objRackAwareness.getRack(ip));
							} else {
								listObject.add("");
								listObject.add("");
							}

							// if reachable and available then adding the
							// authenticity and os name.
							if (isRechable && isAvailable) {
								// getting the os name.
								String osName = SSHUtils.getOS(ip, userName,
										password, isAuthTypePass);

								// if os name is not null then set auth and os
								// name.
								if (osName != null) {
									listObject.set(INDEX_AUTHENTICATED, true);
									listObject.set(INDEX_OS_NAME, osName);
								}
								// adding the cpu cores
								listObject.add(getCpuCores(ip));

								// adding the disk count
								listObject.add(getDiskCount(ip));

								// adding the disk size
								listObject.add(getDiskSize(ip));

								// adding the ram size
								listObject.add(getTotalMemory(ip));

							} else {
								listObject.add("");
								listObject.add("");
								listObject.add("");
								listObject.add("");
							}
							System.out.println("listObject: "
									+ listObject.size());
							// adding the node list in head list.
							resultList.add(listObject);
						} catch (Exception e) {
							logger.error(e.getMessage());
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			// waiting for all node threads to complete.
			semaphore.acquire(nodeStatusMap.size());
		} catch (Exception e) {
			logger.error(e.getMessage());
			error = new ArrayList<String>();
			error.add(e.getLocalizedMessage());
		}
		// if result list is empty and error is null setting generic message.
		if (resultList.isEmpty() && error == null) {
			error = new ArrayList<String>();
			error.add("Unable to get node list. Please provide valid ip pattern.");
		}
		// putting nodes.
		resultMap.put("nodes", resultList);
		// putting error.
		resultMap.put("error", error);
		return resultMap;
	}

	/**
	 * Method to get total memory.
	 * 
	 * @param hostname
	 * @return
	 */
	private String getTotalMemory(String hostname) {
		SSHConnection ssc = new SSHConnection(hostname, userName, password,
				isAuthTypePass);
		Double memory = new Double(0);
		if (ssc.isConnected()) {
			ssc.exec("cat /proc/meminfo | grep MemTotal | awk '{print $2}'");

			if (ssc.getExitStatus() == 0) {
				memory = Double.parseDouble(ssc.getOutput());
				memory = memory / 1024 / 1024;
				System.out.println("memory  " + formator.format(memory));
				return formator.format(memory);
			} else {
				logger.error(ssc.getError());
			}
		}
		return "";
	}

	/**
	 * Method to get disk size.
	 * 
	 * @param hostname
	 * @return
	 */
	private String getDiskSize(String hostname) {
		SSHConnection ssc = new SSHConnection(hostname, userName, password,
				isAuthTypePass);

		if (ssc.isConnected()) {
			ssc.exec("df -k --total | grep total | awk '{print $2}'");

			if (ssc.getExitStatus() == 0) {
				Double size = new Double(ssc.getOutput());
				size = size / 1024 / 1024;
				System.out.println(" disk size  " + formator.format(size));
				return formator.format(size);
			} else {
				logger.error("Failed to get disk size : " + ssc.getError());
			}
		}
		return "";
	}

	/**
	 * Method to get disk count.
	 * 
	 * @param hostname
	 * @return
	 */
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

	/**
	 * Method to get cpu cores.
	 * 
	 * @param hostname
	 * @return
	 */
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

	/**
	 * Method to set system host names for nodes.
	 * 
	 * @param nodeConfs
	 * @param conf
	 * @return
	 */
	public static boolean setSysHostNameForNodes(List<NodeConf> nodeConfs,
			ClusterConf conf) {
		try {
			for (NodeConf nodeConf : nodeConfs) {
				String hostName = HostOperation.getMachineHostName(nodeConf,
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				nodeConf.setSystemHostName(hostName);
			}
		} catch (Exception e) {
			return true;
		}
		return true;
	}
}
