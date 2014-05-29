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
package com.impetus.ankush.hadoop.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.AuthConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.HadoopCommandBuilder;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.service.ClusterCommandExecutionService;

/**
 * The Class ClusterCommandExecutionServiceImpl.
 */
@Service("clusterCommandExecutionService")
public class ClusterCommandExecutionServiceImpl implements
		ClusterCommandExecutionService {
	
	/** The log. */
	private static Logger log = LoggerFactory
			.getLogger(ClusterCommandExecutionServiceImpl.class);

	/** The command builder. */
	private HadoopCommandBuilder commandBuilder = new HadoopCommandBuilder();
	
	/** The newcluster manager. */
	private GenericManager<Cluster, Long> newclusterManager;

	/**
	 * Sets the cluseter manager.
	 *
	 * @param cluseterManager the cluseter manager
	 */
	@Autowired
	public void setCluseterManager(
			@Qualifier(Constant.Manager.CLUSTER) GenericManager<Cluster, Long> cluseterManager) {
		this.newclusterManager = cluseterManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.impl.ClusterCommandExecutionService
	 * #executeHadoopCommand(java.lang.Long, java.lang.String, java.util.Map)
	 */
	@Override
	public Map executeHadoopCommand(Long clusterId, String command,
			Map<String, String> arguments) {
		String commandString = null;
		Map map = new HashMap();

		try {
			if (command.equals("archive")) {
				commandString = commandBuilder.createArchiveCmd(
						arguments.get("archiveName"),
						arguments.get("parentDirectory"),
						arguments.get("sourceDirectory"),
						arguments.get("destinationDirectory"));
			} else if (command.equals("distcp")) {
				commandString = commandBuilder.createDistcpCmd(
						arguments.get("sourceURL"),
						arguments.get("destinatinURL"),
						arguments.get("options"));
			} else if (command.equals("balancer")) {
				commandString = commandBuilder.createBalancerCmd(arguments
						.get("threshold"));
			} else if (command.equals("fsck")) {
				commandString = commandBuilder.createFsckCmd(
						arguments.get("path"), arguments.get("genericOptions"),
						arguments.get("otherOptions"));
			}

			if (commandString == null) {
				throw new RuntimeException(command
						+ " is not a supported hadoop command");
			}

			Cluster cluster = newclusterManager.get(clusterId);

			this.hadoopClusterConf = ((HadoopClusterConf) cluster
					.getClusterConf());

			HadoopConf hadoopConf = (HadoopConf) hadoopClusterConf
					.getComponents().get(Constant.Component.Name.HADOOP);
			String basePath = FileUtils
					.getSeparatorTerminatedPathEntry(hadoopConf
							.getComponentHome())
					+ "bin/hadoop ";
			commandString = basePath + commandString;
			final String hadoopCommand = commandString;

			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					executeCommand(hadoopCommand);
				}
			});
			map.put("status", "OK");
		} catch (Exception e) {
			log.error(e.getMessage());
			map.put("status", "ERROR");
		}

		return map;
	}

	/** The hadoop cluster conf. */
	private HadoopClusterConf hadoopClusterConf = new HadoopClusterConf();

	/**
	 * Execute command.
	 *
	 * @param commandString the command string
	 */
	private void executeCommand(String commandString) {
		AuthConf authConf = hadoopClusterConf.getAuthConf();
		String masterIp = hadoopClusterConf.getNameNode().getPublicIp();
		String username = authConf.getUsername();
		String password = authConf.getPassword();
		String privateKey = "";
		if (!authConf.isAuthTypePassword()) {
			privateKey = authConf.getPassword();
		}
		SSHExec connection = null;
		Result res = null;
		try {
			// connect to remote node
			connection = SSHUtils.connectToNode(masterIp, username, password,
					privateKey);

			// if connected
			if (connection != null) {
				log.debug("Going to execute command : " + commandString);

				CustomTask executeHadoopCmnd = new ExecCommand(commandString);
				res = connection.exec(executeHadoopCmnd);
				if (!res.isSuccess) {
					log.error("Unable to execute Hadoop command..");
				}

			}
		} catch (TaskExecFailException e) {
			log.error(e.getMessage());
		} finally {
			// Disconnect Connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
