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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.client.FourLetterWordMain;

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.monitor.AbstractMonitor;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * The Class ZookeeperClusterMonitor.
 */
public class ZookeeperMonitor extends AbstractMonitor {

	/** The logger. */
	AnkushLogger logger = new AnkushLogger(ZookeeperMonitor.class);

	/** The comp conf. */
	ComponentConfig compConf;

	/** The ensemble id. */
	private String ensembleId;

	/**
	 * Sets the ensemble id.
	 * 
	 * @return true, if successful
	 */
	private boolean setEnsembleId() {
		String ensembleId = (String) parameterMap.get(Constant.Keys.COMPONENT);
		if (ensembleId == null || ensembleId == "") {
			errors.add("Please give valid ensembleId.");
			return false;
		}
		this.ensembleId = ensembleId;
		this.compConf = this.clusterConf.getComponents().get(ensembleId);
		if (this.compConf == null) {
			errors.add("No Zookeeper exist with the ensembleId : "
					+ this.ensembleId);
			return false;
		}
		return true;

	}

	/**
	 * Rest API for the node list table on Zookeeper dashboard.
	 * 
	 * Zookeepernodes.
	 */
	private void zookeepernodes() {

		try {
			if (!this.setEnsembleId()) {
				return;
			}
			int clientPort = compConf
					.getAdvanceConfIntegerProperty(ZookeeperConstant.Keys.CLIENT_PORT);
			List<Map> zookeeperNodeInfo = new ArrayList<Map>();

			for (String host : compConf.getNodes().keySet()) {
				String nodeType = execFourLetterCmd(host,
						ZookeeperConstant.Monitor_Keys.COMMAND_SRVR, clientPort);
				String serverId = execFourLetterCmd(host,
						ZookeeperConstant.Monitor_Keys.COMMAND_CONF, clientPort);
				Map<String, Object> zookeeperNodeData = new HashMap<String, Object>();
				zookeeperNodeData.put(Constant.Keys.NODEIP, host);
				zookeeperNodeData.put(ZookeeperConstant.Monitor_Keys.SERVER_ID,
						serverId);
				zookeeperNodeData.put(
						ZookeeperConstant.Monitor_Keys.SERVER_TYPE, nodeType);
				zookeeperNodeInfo.add(zookeeperNodeData);
			}

			result.put(ZookeeperConstant.Monitor_Keys.ZOOKEEPER_NODE_INFO,
					zookeeperNodeInfo);
			return;
		} catch (Exception e) {
			addErrorAndLogException("Couldn't get zookeeper nodes", e);
		}
	}

	/**
	 * Rest API for the commands list on Zookeeper dashboard Commandlist.
	 */
	private void commandlist() {
		if (!this.setEnsembleId()) {
			return;
		}
		try {
			List<String> commandList = new ArrayList<String>();
			commandList.add("conf");
			commandList.add("cons");
			commandList.add("crst");
			commandList.add("dump");
			commandList.add("envi");
			commandList.add("ruok");
			commandList.add("srst");
			commandList.add("srvr");
			commandList.add("stat");
			commandList.add("wchs");
			commandList.add("wchc");
			commandList.add("wchp");

			List<String> versionList = new ArrayList<String>(
					Arrays.asList(compConf.getVersion().split("\\.")));
			if (versionList.get(1).equalsIgnoreCase("4")) {
				commandList.add("mntr");
			}
			result.put(Constant.Keys.COMMAND, commandList);
		} catch (Exception e) {
			addErrorAndLogException("Error while getting command list: ", e);
		}
	}

	/**
	 * Rest API to run fourLetterCommand Run four letter command.
	 */
	private void runFourLetterCommand() {
		if (!this.setEnsembleId()) {
			return;
		}
		// Getting the ip address from parameter map.
		String host = (String) parameterMap.get(Constant.Keys.HOST);

		// Getting the ip address from parameter map.
		String command = (String) parameterMap.get(Constant.Keys.COMMAND);

		int clientPort = compConf
				.getAdvanceConfIntegerProperty(ZookeeperConstant.Keys.CLIENT_PORT);

		try {
			logger.info("Zookeeper 4 Letter Command Execution ...");
			String commandOutput = FourLetterWordMain.send4LetterWord(host,
					clientPort, command);

			result.put(Constant.Keys.OUT, commandOutput);

		} catch (Exception e) {
			addErrorAndLogException(
					"Error while executing Zookeeper 4 Letter Command: ", e);
		}
	}

	/**
	 * Exec four letter cmd.
	 * 
	 * @param host
	 *            the host
	 * @param command
	 *            the command
	 * @param clientPort
	 *            the client port
	 * @return the string
	 */
	private String execFourLetterCmd(String host, String command, int clientPort) {
		try {
			String commandOutput = FourLetterWordMain.send4LetterWord(host,
					clientPort, command);

			List<String> sysoutList = new ArrayList<String>(
					Arrays.asList(commandOutput.split("\n")));
			String data = new String();

			String escapeCharacter;
			String parameterName;

			if (command
					.equalsIgnoreCase(ZookeeperConstant.Monitor_Keys.COMMAND_MNTR)) {
				escapeCharacter = "\t";
				parameterName = "zk_server_state";
			} else if (command
					.equalsIgnoreCase(ZookeeperConstant.Monitor_Keys.COMMAND_SRVR)) {
				escapeCharacter = ":";
				parameterName = "Mode";
			} else if (command
					.equalsIgnoreCase(ZookeeperConstant.Monitor_Keys.COMMAND_CONF)) {
				escapeCharacter = "=";
				parameterName = "serverId";
			} else {
				escapeCharacter = " ";
				parameterName = " ";
			}

			for (String outData : sysoutList) {

				if (outData.contains(escapeCharacter)) {
					if (outData.split(escapeCharacter)[0].trim()
							.equalsIgnoreCase(parameterName)) {
						data = outData.split(escapeCharacter)[1].trim();
						logger.info("data: " + data);
					}
				}
			}

			return data;
		} catch (Exception e) {
			// addErrorAndLogException("Couldn't execute command-"+command, e);
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	@Override
	public boolean canNodesBeDeleted(ClusterConfig clusterConfig,
			Collection<String> nodes, String componentName) {

		boolean status = true;
		for (String host : nodes) {
			StringBuilder errorMsg = new StringBuilder(
					"Could not delete node - "
							+ host
							+ ": Operation not permitted for nodes with role(s) - "
							+ Constant.Role.ZOOKEEPER);
			if (clusterConfig.getNodes().get(host).getRoles()
					.get(componentName).contains(Constant.Role.ZOOKEEPER)) {
				status = false;
				addAndLogError(errorMsg.toString());
			}
		}
		return status;
	}

}
