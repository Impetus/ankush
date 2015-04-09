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
package com.impetus.ankush.common.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.springframework.util.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommandExecutor;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.validator.PortValidator;
import com.impetus.ankush2.agent.AgentDeployer;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;

/**
 * @author hokam
 * 
 */
public class ClusterPreValidator {
	private static final String CONNECTED = "connected";
	private static final String JPS_PROCESS_LIST = "jpsProcessList";
	private static final String REQUIRE_TTY_DISABLED = "requireTTYDisabled";
	private static final String WGET_EXISTS = "wgetExists";
	private static final String TAR_EXISTS = "tarExists";
	// private static final String UNZIP_EXISTS = "unzipExists";
	// private static final String ZIP_EXISTS = "zipExists";
	private static final String JPS_EXISTS = "jpsExists";
	private static final String NO_LOOPBACK = "noLoopback";
	private static final String IS_SUDO_USER = "isSudoUser";
	private static final String IS_DISK_FREE = "isDiskFree";
	private static final String PORT_AVAILABILITY = "portAvailability";
	private static final String FIREWALL_DISABLED = "firewallDisabled";
	private static final String AGENT_METADATA_NOT_EXISTS = "agentMetadataNotExists";
	private static final String AGENT_NOT_RUNNING = "agentNotRunning";
	private static final String CONNECTION_STATUS = "Connection Status";
	private static final String WARNING = "Warning";
	private static final String OK = "Ok";
	private static final String CRITICAL = "Critical";
	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			ClusterPreValidator.class);

	public Map validate(final Map params) {
		final LinkedHashMap result = new LinkedHashMap();

		if (notContainsKey(params, "nodePorts", result)) {
			return result;
		}
		if (params.containsKey(Constant.Keys.CLUSTERID)) {
			// Get cluster manager
			GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
					.getManager(Constant.Manager.CLUSTER, Cluster.class);

			// get cluster id string
			String clusterIdStr = (String) params.get(Constant.Keys.CLUSTERID);
			// convert cluster id string into long value.
			Long clusterId = ParserUtil.getLongValue(clusterIdStr, 0);
			// Get the cluster object from database.
			Cluster cluster = clusterManager.get(clusterId);

			ClusterConfig clusterConfig = cluster.getClusterConfig();
			// set username
			params.put(Constant.Keys.USERNAME, clusterConfig.getAuthConf()
					.getUsername());
			String pass = clusterConfig.getAuthConf().getPassword();
			if (pass != null && !pass.isEmpty()) {
				params.put(Constant.Keys.PASSWORD, pass);
			} else {
				params.put(Constant.Keys.PRIVATEKEY, clusterConfig
						.getAuthConf().getPrivateKey());
			}
			params.put(Constant.Agent.Key.AGENT_INSTALL_DIR,
					clusterConfig.getAgentInstallDir());
		} else {
			if (notContainsKey(params, Constant.Keys.USERNAME, result)) {
				return result;
			}
			if (notContainsKey(params, Constant.Keys.PASSWORD, result)) {
				return result;
			}
			if (notContainsKey(params, Constant.Keys.PRIVATEKEY, result)) {
				return result;
			}
		}
		final String username = (String) params.get(Constant.Keys.USERNAME);
		final String password = (String) params.get(Constant.Keys.PASSWORD);
		final String privateKey = (String) params.get(Constant.Keys.PRIVATEKEY);

		final Map nodePorts = (Map) params.get("nodePorts");
		Set<String> nodes = nodePorts.keySet();

		final boolean authUsingPassword = (password != null && !password
				.isEmpty());
		final String authInfo = authUsingPassword ? password : privateKey;

		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			for (final String hostname : nodes) {
				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {

					@Override
					public void run() {
						SSHExec conn = null;
						LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
						try {
							conn = SSHUtils.connectToNode(hostname, username,
									password, privateKey);
							map = getValidationMap(params, username, password,
									nodePorts, authUsingPassword, authInfo,
									hostname, conn);
						} catch (Exception e) {
							map.put("error", new Status("Error",
									"Unable to perform validations", CRITICAL));
							logger.error(e.getMessage(), e);

						} finally {
							// setting overall status.
							map.put("status",
									getOverAllStatus((Collection) map.values()));
							// putting map against node.
							result.put(hostname, map);
							if (semaphore != null) {
								semaphore.release();
							}
							if (conn != null) {
								conn.disconnect();
							}
						}
					}

				});
			}
			semaphore.acquire(nodes.size());
			result.put("status", true);
		} catch (Exception e) {
			String message = e.getMessage();
			if (message == null) {
				message = "Unable to validate nodes";
			}
			result.put("error", Collections.singletonList(message));
			result.put("status", false);
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * Method to get over all status of the node validations.
	 * 
	 * @param collection
	 * @return
	 */
	private Status getOverAllStatus(Collection<Status> collection) {
		List<String> statusValues = new ArrayList<String>();
		String overallStatus = OK;
		for (Status status : collection) {
			statusValues.add(status.getStatus());
		}
		if (statusValues.contains(CRITICAL)) {
			overallStatus = CRITICAL;
		} else if (statusValues.contains(WARNING)) {
			overallStatus = WARNING;
		}

		return new Status("Overall Status", "", overallStatus);
	}

	/**
	 * Method to check process lists.
	 * 
	 * @param username
	 * @param hostname
	 * @param authUsingPassword
	 * @param authInfo
	 * @return
	 */
	private Status checkProcessList(String username, String hostname,
			boolean authUsingPassword, String authInfo) {
		String processList = "";
		String message = null;
		try {
			processList = SSHUtils.getCommandOutput("jps", hostname, username,
					authInfo, authUsingPassword);
		} catch (Exception e) {
			message = e.getMessage();
			logger.error(message, e);
		}

		List<String> processes = null;
		if ((processList == null) || processList.isEmpty()) {
			processes = new ArrayList<String>();
		} else {
			processes = new ArrayList<String>(Arrays.asList(processList
					.split("\n")));
		}

		List<String> processLists = new ArrayList<String>();
		for (String process : processes) {
			if (!(process.contains("Jps") || process
					.contains(AgentDeployer.ANKUSH_SERVER_PROCSS_NAME))) {
				String[] processArray = process.split("\\ ");
				if (processArray.length == 2) {
					processLists.add(process.split("\\ ")[1]);
				}
			}
		}

		if (processLists.isEmpty()) {
			return new Status("JPS process list", OK);
		} else {
			message = StringUtils
					.collectionToCommaDelimitedString(processLists)
					+ " already running";
			return new Status("JPS process list", message, WARNING);
		}
	}

	/**
	 * Method to check command existence.
	 * 
	 * @param conn
	 * @param command
	 * @return
	 */
	private Status checkCommandExistence(SSHExec conn, String command) {
		boolean taskStatus = SSHUtils
				.getCommandStatus(conn, "which " + command);
		String label = command.substring(0, 1).toUpperCase()
				+ command.substring(1) + " existence";
		if (taskStatus) {
			return new Status(label, OK);
		} else {
			return new Status(label, command + " command not found", CRITICAL);
		}
	}

	/**
	 * Method to check port availability
	 * 
	 * @param nodePorts
	 * @param hostname
	 * @return
	 */
	private Status checkPortAvailability(final Map nodePorts,
			final String hostname) {
		String message = new String();

		for (Object port : (List) nodePorts.get(hostname)) {
			PortValidator validator = new PortValidator(hostname,
					port.toString());
			boolean taskStatus = validator.validate();
			if (!taskStatus) {
				message = message + port.toString() + ", ";
			}
		}
		if (message.isEmpty()) {
			return new Status("Port availability", OK);
		} else {
			return new Status("Port availability", message.substring(0,
					message.length() - 2)
					+ " already in use/blocked", CRITICAL);
		}
	}

	/**
	 * Method to check key existence
	 * 
	 * @param params
	 * @param key
	 * @param result
	 * @return
	 */
	private boolean notContainsKey(Map params, String key, Map result) {
		if (params.containsKey(key)) {
			return false;
		} else {
			result.put("error", "Missing " + key);
			return true;
		}
	}

	/**
	 * Method to check .ankush/agent folder existence.
	 * 
	 * @param conn
	 * @return
	 */
	private Status checkDotAnkushAgentFolder(SSHExec conn) {
		// checking .ankush folder existance.
		boolean taskStatus = !SSHUtils.getCommandStatus(conn,
				"ls .ankush/agent");
		if (taskStatus) {
			return new Status("AnkushAgent directory existence", OK);
		} else {
			return new Status("AnkushAgent directory existence",
					"AnkushAgent found installed on this node", WARNING);
		}

	}

	/**
	 * Check Agent process status.l
	 * 
	 * @param conn
	 * @return
	 */
	private Status checkAgentProcessStatus(SSHExec conn) {
		String message = "AnkushAgent found running on this node";
		boolean taskStatus = !SSHUtils.getCommandStatus(conn,
				"jps | grep AnkushAgent");
		if (taskStatus) {
			return new Status("AnkushAgent service running", OK);
		} else {
			return new Status("AnkushAgent service running", message, WARNING);
		}
	}

	/**
	 * Check etc hosts.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            the auth info
	 * @param authUsingPassword
	 *            the auth using password
	 * @return true, if successful
	 */
	public Status checkLoopbackAddress(SSHExec conn, String hostname) {
		Result res = null;
		// requires tty check by executing a sudo command
		boolean status = false;
		String message = "Invalid /etc/hosts configuration, please remove the loopback IP (127.0.x.x) mapping with "
				+ hostname + ".";
		CustomTask checkLoopBackTask = new ExecCommand("egrep  '^127.0.*"
				+ hostname + "|^::1*" + hostname + "' "
				+ Constant.LinuxEnvFiles.ETC_HOSTS);

		try {
			if (conn != null) {
				status = (conn.exec(checkLoopBackTask).rc != 0);
			}
		} catch (TaskExecFailException e) {
			logger.error(e.getMessage(), e);
			message = e.getMessage();
			if (message == null) {
				message = "Failed to validate loopback address";
			}
		}
		if (status) {
			return new Status("Loopback address", OK);
		} else {
			return new Status("Loopback address", message, CRITICAL);
		}
	}

	/**
	 * Check requires tty.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param privateKey
	 *            the private key
	 * @return true, if successful
	 */
	public Status checkRequiresTTY(SSHExec conn, String password) {
		Result res = null;
		// requires tty check by executing a sudo command
		boolean status = false;
		String message = "Requiretty is enabled. Unable to get tty session on machine.";
		CustomTask ttyTask = new ExecSudoCommand(password,
				"grep 'requiretty' /etc/sudoers");
		try {
			if (conn != null) {
				res = conn.exec(ttyTask);
			} else {
				status = false;
			}
		} catch (Exception e) {
			message = e.getMessage();
			if (message == null) {
				message = "Failed to validate requires tty";
			}
			logger.error(e.getMessage(), e);
		}
		// error msg contains tty to run
		boolean isTtyEnabled = res.error_msg.contains("tty to run");

		status = !isTtyEnabled || res.error_msg.isEmpty();

		if (status) {
			return new Status("Require TTY", OK);
		} else {
			return new Status("Require TTY", message, WARNING);
		}
	}

	/**
	 * Check sudoers.
	 * 
	 * @param hostname
	 *            the hostname
	 * @param username
	 *            the username
	 * @param authInfo
	 *            the auth info
	 * @param authUsingPassword
	 *            the auth using password
	 * @return true, if successful
	 */
	public Status checkSudoers(SSHExec conn, String password) {
		// requires tty check by executing a sudo command
		CustomTask ttyTask = new ExecSudoCommand(password, "ls");
		boolean status = false;
		String message = "The user is not having admin credentials";
		// if connected.
		if (conn != null) {
			logger.debug(CONNECTED);
			Result rs;
			try {
				rs = conn.exec(ttyTask);
				status = (rs.rc == 0);
			} catch (Exception e) {
				message = e.getMessage();
				if (message == null) {
					message = "Failed to validate sudo user";
				}
				status = false;
			}
		}

		if (status) {
			return new Status("Sudo user", OK);
		} else {
			return new Status("Sudo user", message, WARNING);
		}
	}

	public Status checkFreeDisk(SSHExec conn, String password, String agentHome) {
		String errMsg = "Could not check free disk availability @ " + agentHome
				+ ".";
		String diskSpaceKey = "AnkushAgent Install Dir Disk Space";
		Long availability;
		boolean status = true;
		try {
			// task to check free disk apace
			CustomTask freeDisk = new ExecCommand("df " + agentHome);
			Result result = conn.exec(freeDisk);
			// If filesystem name is long, then the command will output details
			// in more than one line, so splitting the output on the basis of
			// new line so as to remove the first line containing column labels
			List<Object> details = new ArrayList(Arrays.asList(result.sysout
					.split("\n")));
			// Removing the column labels from command output
			details.remove(0);
			// joining elements in lists with tab
			String filesystemDetail = org.apache.commons.lang3.StringUtils
					.join(details, "\t");
			// Considering that a directory is mounted only on a single
			// filesystem, so throwing exception if the size of the list created
			// using space as token is other than 6
			if (new ArrayList(Arrays.asList(filesystemDetail.trim().split(
					"\\s+"))).size() != 6) {
				throw new AnkushException(errMsg);
			}
			// Getting the disk availability details
			availability = Long.valueOf(new ArrayList(Arrays
					.asList(filesystemDetail.split("\\s+"))).get(3).toString());
			// if disk space available is less than 50 MB, then setting status
			// as critical
			if (availability < 51200L) {
				status = false;
			}
		} catch (Exception e) {
			return new Status(diskSpaceKey, errMsg, CRITICAL);
		}
		if (status) {
			return new Status(diskSpaceKey, "Sufficent disk space available @ "
					+ agentHome + ". Space Availability : "
					+ (availability / 1024) + "  MB", OK);
		} else {
			return new Status(diskSpaceKey,
					"Insufficent disk space available @ " + agentHome
							+ ". Space Availability : " + (availability / 1024)
							+ " MB", CRITICAL);
		}
	}

	/**
	 * Check iptables.
	 * 
	 * @param hostname
	 *            the hostname
	 * @return true, if successful
	 */
	// public Status checkIptables(String hostname) {
	// Status value = null;
	// boolean status = false;
	// String message = "Firewall is not disabled";
	// String command = "nmap -P0 " + hostname;
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	//
	// try {
	// CommandExecutor.exec(command, baos, null);
	// String result = baos.toString();
	// value = new Status("Firewall", OK);
	// status = result.contains("closed") && result.contains("open")
	// && !result.contains("filtered ports");
	// } catch (IOException e) {
	// message = e.getMessage();
	// logger.error(e.getMessage(), e);
	// } catch (Exception e) {
	// message = e.getMessage();
	// logger.error(e.getMessage(), e);
	// }
	// if (message == null) {
	// message = "Failed to validate firewall";
	// }
	//
	// if (status) {
	// value = new Status("Firewall", OK);
	// } else {
	// value = new Status("Firewall", message, CRITICAL);
	// }
	// return value;
	// }

	/**
	 * Check iptables.
	 * 
	 * @param hostname
	 *            the hostname
	 * @return true, if successful
	 */
	public Status checkIptables(String hostname) {
		Status value = null;
		String message = "Firewall is not disabled";
		Socket socket = null;
		// Random port to validate
		Integer port = 4511;
		try {
			// Creating a socket condition
			socket = new Socket(hostname, port);
			// if socket connection created, then firewall is disabled
			value = new Status("Firewall", OK);
		} catch (Exception e) {
			// if exception type is NoRouteToHostException , then firewall is
			// enabled , else if exception type is ConnectException, then
			// firewall is disabled but no process is running on the port , else
			// unable to validate firewall
			if (e instanceof java.net.NoRouteToHostException) {
				value = new Status("Firewall", message, CRITICAL);
			} else if (e instanceof java.net.ConnectException) {
				value = new Status("Firewall", OK);
			} else {
				value = new Status("Firewall", "Failed to validate firewall",
						CRITICAL);
			}
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		return value;
	}

	private LinkedHashMap<String, Object> getValidationMap(Map params,
			String username, String password, Map nodePorts,
			boolean authUsingPassword, String authInfo, String hostname,
			SSHExec conn) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		if (conn != null) {
			// connection status.
			map.put(CONNECTED, new Status(CONNECTION_STATUS,
					"Connection established to node", OK));
			// Getting Ankush agent process status.
			logger.debug("getting AnkushAgent process status");
			map.put(AGENT_NOT_RUNNING, checkAgentProcessStatus(conn));

			logger.debug("Checking .ankush folder existance");
			// checking .ankush folder existance.
			map.put(AGENT_METADATA_NOT_EXISTS, checkDotAnkushAgentFolder(conn));

			// cheking ip tables.
			logger.debug("checking ip tables." + params);
			// taskStatus = checkIptables(hostname);
			map.put(FIREWALL_DISABLED, checkIptables(hostname));

			// checking port availability.
			map.put(PORT_AVAILABILITY,
					checkPortAvailability(nodePorts, hostname));

			// checking require tty.
			Status requiretty = checkRequiresTTY(conn, password);
			logger.debug("checking require tty." + params);
			map.put(REQUIRE_TTY_DISABLED, requiretty);

			Status isSudoUser = checkSudoers(conn, password);

			if (requiretty.getStatus().equals(WARNING)) {
				isSudoUser.setMessage(requiretty.getMessage());
			}
			// checking free disk space.
			logger.debug("checking free disk space." + params);
			Status isDiskFree = checkFreeDisk(conn, password,
					String.valueOf(params
							.get(Constant.Agent.Key.AGENT_INSTALL_DIR)));
			map.put(IS_DISK_FREE, isDiskFree);

			// checking sudoers.
			logger.debug("checking sudoers." + params);
			map.put(IS_SUDO_USER, isSudoUser);

			// checking etc hosts.
			logger.debug("checking etc hosts." + params);
			map.put(NO_LOOPBACK, checkLoopbackAddress(conn, hostname));

			// checking wget.
			logger.debug("checking checking wget." + params);
			map.put(WGET_EXISTS, checkCommandExistence(conn, "wget"));

			// checking tar.
			logger.debug("checking checking tar." + params);
			map.put(TAR_EXISTS, checkCommandExistence(conn, "tar"));

			// checking unzip.
			// logger.debug("checking checking unzip." + params);
			// map.put(UNZIP_EXISTS, checkCommandExistence(conn, "unzip"));

			// checking zip.
			// logger.debug("checking checking zip." + params);
			// map.put(ZIP_EXISTS, checkCommandExistence(conn, "zip"));

			// checking jps.
			logger.debug("checking checking jps." + params);
			map.put(JPS_EXISTS, checkCommandExistence(conn, "jps"));

			// getting running jps process list.
			logger.debug("getting jps process list");
			map.put(JPS_PROCESS_LIST,
					checkProcessList(username, hostname, authUsingPassword,
							authInfo));
		} else {
			map.put(CONNECTED, new Status(CONNECTION_STATUS,
					"Failed to connect to node", CRITICAL));
		}
		return map;
	}

	class Status {
		private String label;
		private String message = "No Conflict Detected";
		private String status;

		public Status(String label, String message, String status) {
			super();
			this.label = label;
			this.message = message;
			this.status = status;
		}

		public Status(String label, String status) {
			super();
			this.label = label;
			this.status = status;
		}

		/**
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * @param label
		 *            the label to set
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message
		 *            the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status
		 *            the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}
	}

}
