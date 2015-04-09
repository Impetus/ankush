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
package com.impetus.ankush.common.agent;

import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * @author root
 * 
 */
public class GenericServiceMonitor implements ServiceMonitorable {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(GenericServiceMonitor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.ServiceMonitorable#manageService(
	 * com.impetus.ankush.common.framework.config.ClusterConf,
	 * net.neoremind.sshxcute.core.SSHExec, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {

		boolean result = true;
		try {

			if (action.equals(Constant.ServiceAction.START)) {
				if (processName.equals(Constant.RoleProcessName
						.getProcessName(Constant.Role.AGENT))) {
					result = this.startagent(connection, conf);
				} else if (processName.equals(Constant.RoleProcessName
						.getProcessName(Constant.Role.GMETAD))) {
					result = this.startgmetad(connection, conf);
				} else if (processName.equals(Constant.RoleProcessName
						.getProcessName(Constant.Role.GMOND))) {
					result = this.startgmond(connection, conf);
				}
			} else {
				if (processName.equals(Constant.RoleProcessName
						.getProcessName(Constant.Role.AGENT))) {
					result = this.stopagent(connection, conf);
				} else if (processName.equals(Constant.RoleProcessName
						.getProcessName(Constant.Role.GMETAD))) {
					result = this.stopgmetad(connection, conf);
				} else if (processName.equals(Constant.RoleProcessName
						.getProcessName(Constant.Role.GMOND))) {
					result = this.stopgmond(connection, conf);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return result;
	}

	public boolean startagent(SSHExec connection, ClusterConf conf) {
		String startAgent = "sh " + Constant.Agent.AGENT_START_SCRIPT;
		CustomTask task = new ExecCommand(startAgent);
		startAgent = task.getCommand();
		return SSHUtils.action(connection, startAgent);
	}

	public boolean stopagent(SSHExec connection, ClusterConf conf) {
		String stopAgent = "sh " + Constant.Agent.AGENT_STOP_SCRIPT;
		CustomTask task = new ExecCommand(stopAgent);
		stopAgent = task.getCommand();
		return SSHUtils.action(connection, stopAgent);
	}

	public boolean stopgmetad(SSHExec connection, ClusterConf conf) {
		String stopGmetadCmd = "killall -9 gmetad";
		return SSHUtils.action(connection, stopGmetadCmd);
	}

	public boolean stopgmond(SSHExec connection, ClusterConf conf) {
		String stopGmondCmd = "killall -9 gmond";
		return SSHUtils.action(connection, stopGmondCmd);
	}

	public boolean startgmetad(SSHExec connection, ClusterConf conf) {
		// getting config reader object.
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

		// Getting gmetad configuration path.
		String gmetadConfPath = CommonUtil.getUserHome(conf.getUsername())
				+ ankushConf.getStringValue("gmetad.conf");
		// start gmetad command.
		String startGmetadCmd = "gmetad --conf=" + gmetadConfPath;
		return SSHUtils.action(connection, startGmetadCmd);
	}

	public boolean startgmond(SSHExec connection, ClusterConf conf) {
		// getting config reader object.
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

		// Getting gmond configuration path.
		String gmondConfPath = CommonUtil.getUserHome(conf.getUsername())
				+ ankushConf.getStringValue("gmond.conf");
		// start gmond command
		String startGmondCmd = "gmond --conf=" + gmondConfPath;
		return SSHUtils.action(connection, startGmondCmd);
	}

}
