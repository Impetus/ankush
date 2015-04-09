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
package com.impetus.ankush.common.zookeeper;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush2.constant.Constant.Component;

/**
 * @author Akhil
 *
 */
public class ZookeeperServiceMonitor implements ServiceMonitorable {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(ZookeeperServiceMonitor.class);
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.ServiceMonitorable#manageService(com.impetus.ankush.common.framework.config.ClusterConf, net.neoremind.sshxcute.core.SSHExec, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {
		return this.monitorZookeeper(connection, conf, processName, action);
	}

	private boolean monitorZookeeper(SSHExec connection, ClusterConf conf, String processName, String action) {
		try {
			ZookeeperConf zConf = (ZookeeperConf) conf.getClusterComponents().get(
					Component.Name.ZOOKEEPER);
			String componentHome = FileUtils.getSeparatorTerminatedPathEntry(zConf.getComponentHome());
			String command = componentHome + "bin/zkServer.sh "
					+ action;
			AnkushTask task = new RunInBackground(command);
			Result res = connection.exec(task);
			if (!res.isSuccess) {
				logger.error("Could not " + action + " " + processName + " service");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
}
