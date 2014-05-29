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
package com.impetus.ankush.cassandra;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;

public class CassandraServiceMonitor implements ServiceMonitorable {
	
	/** The Constant STR_SLASH. */
	private static final String STR_SLASH = "/";
	
	/** The Constant COMPONENT_FOLDER_BIN. */
	private static final String COMPONENT_FOLDER_BIN = "bin/";

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			CassandraServiceMonitor.class);

	public boolean manageCassandra(SSHExec connection, ClusterConf clusterConf, 
				String processName, String action) {
		try {
			CassandraConf cassandraConf = (CassandraConf) clusterConf.getClusterComponents()
					.get(Constant.Component.Name.CASSANDRA);
			
			String vendor = null;
			if(cassandraConf.getComponentVendor().equalsIgnoreCase("Datastax")){
				vendor = "dsc";
			}else{
				vendor = "apache";
			}
			String componentHome = cassandraConf.getInstallationPath() + vendor + "-cassandra-"
					+ cassandraConf.getComponentVersion(); 
			StringBuffer command = new StringBuffer();
			AnkushTask manageServices;
			if(action.equalsIgnoreCase(Constant.ServiceAction.START)){
				command.append(componentHome).append(STR_SLASH + COMPONENT_FOLDER_BIN)
				.append("cassandra");
				manageServices = new RunInBackground(
						command.toString());
			}else{
				manageServices = new KillJPSProcessByName(Constant.Component.ProcessName.CASSANDRA);
			}

			Result res = connection.exec(manageServices);
			if (res.rc != 0) {
				logger.error("Could not " + action + " "
						+ processName + " process");
				return false;
			}	
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {
		return this.manageCassandra(connection, conf, processName, action);
	}

}
