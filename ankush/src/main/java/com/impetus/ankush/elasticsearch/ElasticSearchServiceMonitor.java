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
package com.impetus.ankush.elasticsearch;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ServiceMonitor;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * The Class ElasticSearchServiceMonitor.
 */
public class ElasticSearchServiceMonitor implements ServiceMonitorable {

	/** The logger. */
	AnkushLogger logger = new AnkushLogger(ElasticSearchServiceMonitor.class);

	/**
	 * Startelasticsearch.
	 *
	 * @param connection the connection
	 * @param clusterConf the cluster conf
	 * @return true, if successful
	 */
	public boolean startelasticsearch(SSHExec connection, ClusterConf clusterConf) {
		try {
			ElasticSearchConf esConf = (ElasticSearchConf) clusterConf
					.getClusterComponents().get(Constant.Component.Name.ELASTICSEARCH);
			String compHome = esConf.getComponentHome();
			//start Elastic Search Command
			String startESCommand = 
					compHome + ElasticSearchDeployer.ES_BIN;
			//jps to node and check if ElasticSearch is already running
			startESCommand = "(jps | grep "+ Constant.Process.ELASTICSEARCH + " || " + startESCommand + ")";
			
			CustomTask startES = new RunInBackground(startESCommand);
			Result rs = connection.exec(startES);
			if (rs.rc != 0) {
				logger.error("Could not start " + Constant.Process.ELASTICSEARCH + " service.");
				return false;
			}	
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Stopelasticsearch.
	 *
	 * @param connection the connection
	 * @param clusterConf the cluster conf
	 * @return true, if successful
	 */
	public boolean stopelasticsearch(SSHExec connection, ClusterConf clusterConf) {
		try {
			AnkushTask stopES = new KillJPSProcessByName(Constant.Process.ELASTICSEARCH);
			Result rs = connection.exec(stopES);
			if (rs.rc != 0) {
				logger.error("Could not stop " + Constant.Process.ELASTICSEARCH + " service.");
				return false;
			}	
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.ServiceMonitorable#manageService(com.impetus.ankush.common.framework.config.ClusterConf, net.neoremind.sshxcute.core.SSHExec, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {
		boolean result = true;
		try {
			
			if(action.equals(Constant.ServiceAction.START)) {
				result = this.startelasticsearch(connection, conf);
			} else {
				result = this.stopelasticsearch(connection, conf);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return result;
	}

}
