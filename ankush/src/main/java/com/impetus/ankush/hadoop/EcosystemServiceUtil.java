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
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.KillProcessOnPort;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.hadoop.ecosystem.hbase.HBaseConf;
import com.impetus.ankush.hadoop.ecosystem.oozie.OozieConf;
import com.impetus.ankush.hadoop.ecosystem.solr.SolrConf;

/**
 * The Class EcosystemServiceUtil.
 *
 * @author Akhil
 */
public class EcosystemServiceUtil {

	/** The log. */
	private static AnkushLogger LOG = new AnkushLogger(Constant.Component.Name.HADOOP,
			EcosystemServiceUtil.class);
	
	/**
	 * Manage hbase services.
	 *
	 * @param connection the connection
	 * @param conf the conf
	 * @param service the service
	 * @param action the action
	 * @return true, if successful
	 */
	public static boolean manageHBaseServices(SSHExec connection, HBaseConf conf, String service, String action) {
		String role = Constant.RoleProcessName.getRoleName(service);
		if(role == null) {
			return false;
		}
		try {
			String commandNameForRole = Constant.RoleCommandName.getCommandName(role);
			if(commandNameForRole == null) {
				return false;
			}
			
			String hbaseCommand = conf.getComponentHome() + "/bin/" 
								  + Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HBASE
								  + Constant.STR_SPACE + action
								  + Constant.STR_SPACE + commandNameForRole;
			
			CustomTask hbaseAction = new RunInBackground(hbaseCommand);
			Result result = connection.exec(hbaseAction);
			
			if(result.rc == 0) {
				return true;
			} else {
				LOG.error("Could not " + action + role + ".");
				return false;
			}	
		} catch(Exception e) {
			LOG.error("Could not " + action + role + ".");
			return false;
		}
	}
	
	/**
	 * Manage oozie3 server.
	 *
	 * @param connection the connection
	 * @param conf the conf
	 * @param action the action
	 * @return true, if successful
	 */
	public static boolean manageOozie3Server(SSHExec connection, OozieConf conf, String action) {
		try {
			String oozieCommand = conf.getComponentHome() + "/bin/oozie-"
					+ action + ".sh";
			CustomTask oozieAction = new ExecSudoCommand(conf.getPassword(), false, true, oozieCommand);
			Result result = connection.exec(oozieAction);
			
			if(result.rc == 0) {
				return true;
			} else {
				LOG.error(conf.getNode(), "Could not " + action + " Oozie.");
				return false;
			}	
		} catch(Exception e) {
			LOG.error(conf.getNode(), "Could not " + action + " Oozie.");
			return false;
		}
	}
	
	/**
	 * Manage solr.
	 *
	 * @param connection the connection
	 * @param conf the conf
	 * @param action the action
	 * @return true, if successful
	 */
	public static boolean manageSolr(SSHExec connection, SolrConf conf, String action) {
		try {
			if(action.equals(Constant.Hadoop.Command.START)){
				String cdCmd = "cd " + conf.getComponentHome() + "/example/";
				
				// start solr server
				String solrStart = "java -jar start.jar </dev/null >nohup.out 2>&1 &";
				
				if(connection.exec(new ExecSudoCommand(conf.getPassword(), false, false, cdCmd, solrStart)).isSuccess) {
					return true;
				} else {
					LOG.error(conf.getNode(), "Could not " + action + " Solr service.");
					return false;
				}
			} else {
				// stop solr server
				AnkushTask stopSolr = new KillProcessOnPort("" + conf.getServicePort());
				if(connection.exec(stopSolr).isSuccess) {
					return true;
				} else {
					LOG.error(conf.getNode(), "Could not " + action + " Solr service.");
					return false;
				}
			}
		} catch(Exception e) {
			LOG.error(conf.getNode(), "Could not " + action + " Solr.");
			return false;
		}
	}
	
	
}
