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
package com.impetus.ankush.hadoop;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.JavaProcessScriptFile;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Deployer;
import com.impetus.ankush.hadoop.ecosystem.hbase.HBaseConf;
import com.impetus.ankush.hadoop.ecosystem.oozie.OozieConf;
import com.impetus.ankush.hadoop.ecosystem.solr.SolrConf;

/**
 * The Class HadoopServiceMonitor.
 * 
 * @author Akhil
 */
public class HadoopServiceMonitor implements ServiceMonitorable {

	/** The logger. */
	private static AnkushLogger logger = new AnkushLogger(
			HadoopServiceMonitor.class);

	 /** The Constant STR_SPACE. */
	private static final String STR_SPACE = " ";

	public boolean manageHadoop(SSHExec connection, ClusterConf clusterConf, String processName,String action) {
		try {
			HadoopConf hadoopConf = null;
			String daemonFileRelPath = "";
			if(clusterConf.getClusterComponents()
					.get(Constant.Component.Name.HADOOP) == null) {
				hadoopConf = (HadoopConf) clusterConf.getClusterComponents()
						.get(Constant.Component.Name.HADOOP2);
				
				daemonFileRelPath = Hadoop2Deployer.RELPATH_SCRIPT_DIR;
			}
			else {
				hadoopConf = (HadoopConf) clusterConf.getClusterComponents()
						.get(Constant.Component.Name.HADOOP);
				daemonFileRelPath = Hadoop1Deployer.COMPONENT_FOLDER_BIN;
			}
			
			String serviceRole = Constant.RoleProcessName.getRoleName(processName);
			if(serviceRole.isEmpty()) {
				logger.error("Could not " + action + " " + processName + ".");
				return false;
			}
			
			daemonFileRelPath += JavaProcessScriptFile.getProcessDaemonFile(serviceRole);
			String componentHome = hadoopConf.getComponentHome();
					
			StringBuffer command = new StringBuffer();
			
			command.append(componentHome).append(daemonFileRelPath).append(STR_SPACE);
			command.append(action).append(STR_SPACE)
			.append(Constant.RoleCommandName.getCommandName(serviceRole));
//					Constant.RoleServices.getServices(service).toLowerCase());
					
			//.append( service.toLowerCase());

			logger.info("Performing Action: " + action + " " + processName + ".");
			logger.info("Using Command: " + command);
			
			CustomTask manageServices = new RunInBackground(command.toString());
			
			//CustomTask manageServices = new ExecCommand(command.toString());

			Result res = connection.exec(manageServices);
			if (res.rc != 0) {
				logger.error("Could not " + action + " " + processName + ".");
			}
		} catch(Exception e) {
			logger.error("Could not " + action + " " + processName + ".");
			return false;
		}
		return true;
	}

	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {
		boolean result = true;
		try {
			switch(Constant.Hadoop.ProcessList.valueOf(processName.toUpperCase())) {
				case HMASTER:
				case HREGIONSERVER:
					HBaseConf hbaseConf = (HBaseConf) conf.getClusterComponents().get(Constant.Component.Name.HBASE);
					result = EcosystemServiceUtil.manageHBaseServices(connection, hbaseConf, processName, action);
					break;
				case BOOTSTRAP:
					OozieConf oozieConf = (OozieConf) conf.getClusterComponents().get(Constant.Component.Name.OOZIE); 
					result = EcosystemServiceUtil.manageOozie3Server(connection, oozieConf, action);
					break;
				case JAR:
					SolrConf solrConf = (SolrConf) conf.getClusterComponents().get(Constant.Component.Name.SOLR);
					result = EcosystemServiceUtil.manageSolr(connection, solrConf, action);
					break;
				default:
					result = this.manageHadoop(connection, conf, processName, action);
					break;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return result;
	}
}
