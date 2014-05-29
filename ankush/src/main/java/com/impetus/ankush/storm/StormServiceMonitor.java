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
package com.impetus.ankush.storm;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * @author hokam
 * 
 */
public class StormServiceMonitor implements ServiceMonitorable {

	private AnkushLogger logger = new AnkushLogger(StormServiceMonitor.class);

	public boolean startnimbus(SSHExec connection, ClusterConf clusterConf) {
		try {
			StormConf conf = (StormConf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.STORM);
			String componentHome = conf.getInstallationPath() + "storm-"
					+ conf.getComponentVersion();

			String stormPath = componentHome + "/bin/storm ";
			String nimbusStartCmd = stormPath + "nimbus";
			AnkushTask nimbusStart = new RunInBackground(nimbusStartCmd);
			Result res = connection.exec(nimbusStart);
			if (!res.isSuccess) {
				logger.error("Could not start nimbus service");
				return false;

		}
		return true;			
		} catch (Exception e) {
			logger.error("Could not start supervisor service");
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean startsupervisor(SSHExec connection, ClusterConf clusterConf) {
		try {
			StormConf conf = (StormConf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.STORM);
			String componentHome = conf.getInstallationPath() + "storm-"
					+ conf.getComponentVersion();

			String stormPath = componentHome + "/bin/storm ";
			String supervisorStartCmd = stormPath + "supervisor";
			AnkushTask nimbusStart = new RunInBackground(supervisorStartCmd);
			Result res = connection.exec(nimbusStart);
			if (!res.isSuccess) {
				logger.error("Could not start supervisor service");
				return false;	
			}
			return true;			
		} catch (Exception e) {
			logger.error("Could not start supervisor service");
			logger.error(e.getMessage());
			return false;
		} 

	}

//	public void stopnimbus(SSHExec connection, ClusterConf clusterConf)
//			throws TaskExecFailException {
//		AnkushTask stopNimbus = new KillJPSProcessByName("nimbus");
//		Result res = connection.exec(stopNimbus);
//		if (!res.isSuccess) {
//			logger.error("Could not stop nimbus service");
//		}
//	}
//
//	public void stopsupervisor(SSHExec connection, ClusterConf clusterConf)
//			throws TaskExecFailException {
//		AnkushTask stopSupervisor = new KillJPSProcessByName("supervisor");
//		Result res = connection.exec(stopSupervisor);
//		if (!res.isSuccess) {
//			logger.error("Could not stop supervisor service");
//		}
//	}


	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {
		boolean result = true;
		try {
			
			if(action.equals(Constant.ServiceAction.START)) {
				if(processName.equals(Constant.RoleProcessName.getProcessName(Constant.Role.NIMBUS))) {
					result = this.startnimbus(connection, conf);
				}
				else if(processName.equals(Constant.RoleProcessName.getProcessName(Constant.Role.SUPERVISOR))) {
					result = this.startsupervisor(connection, conf);
				}
			} else {
				AnkushTask stopService = new KillJPSProcessByName(processName);
				Result res = connection.exec(stopService);
				if (!res.isSuccess) {
					logger.error("Could not stop " + processName + " service");
					result = false;
				}		
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}
}
