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
package com.impetus.ankush2.hadoop.monitor.commandsmanager;

import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;

/**
 * @author Akhil
 * 
 */
public class Hadoop1CommandsManager extends HadoopCommandsManager {

	public Hadoop1CommandsManager(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, Hadoop1CommandsManager.class);
	}
	
	public Hadoop1CommandsManager() {
		super();
	}

	@Override
	public boolean formatNameNode() {
		String namenodeHost = HadoopUtils.getNameNodeHost(this.compConfig);
		try {
			LOG.info("Formatting Hadoop NameNode",
					Constant.Component.Name.HADOOP, namenodeHost);

			String formatCommand = "yes | " +  HadoopUtils.getHadoopBinDir(compConfig)
					+ HadoopConstants.Command.HADOOP + " "
					+ HadoopConstants.Command.NAMENODE_FORMAT;

			// format namenode
			CustomTask formatNamenode = new ExecCommand(formatCommand);
			if (!this.clusterConfig.getNodes().get(namenodeHost)
					.getConnection().exec(formatNamenode).isSuccess) {
				HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
						"Could not format Hadoop NameNode",
						Constant.Component.Name.HADOOP, namenodeHost);
				return false;
			}
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not format Hadoop NameNode",
					Constant.Component.Name.HADOOP, namenodeHost, e);
			return false;
		}
		return true;
	}

	@Override
	public boolean submitApplication() {
		// TODO Auto-generated method stub
		return false;
	}

}
