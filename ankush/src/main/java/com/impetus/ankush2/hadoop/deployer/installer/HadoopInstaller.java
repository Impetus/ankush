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
package com.impetus.ankush2.hadoop.deployer.installer;

import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.config.ComponentConfigContext;

/**
 * @author Akhil
 *
 */
public abstract class HadoopInstaller extends ComponentConfigContext {
	
	/**
	 * @param clusterConfig
	 */
	public HadoopInstaller(ClusterConfig clusterConfig, ComponentConfig hadoopConfig, Class classObj) {
		super(clusterConfig, hadoopConfig, classObj);
//		super();
//		this.clusterConfig = clusterConfig;
//		this.hadoopConfig = hadoopConfig;
//		this.LOG = new AnkushLogger(classObj);
//		this.LOG.setCluster(clusterConfig);
	}
	
	public HadoopInstaller() {
		super();
	}

	public abstract boolean createNode(NodeConfig nodeConfig);
	
	public abstract boolean removeNode(NodeConfig nodeConfig);
	
	public abstract boolean addNode(NodeConfig nodeConfig);
	
	
// 	public boolean syncHadoopConfFolder(NodeConfig nodeConfig, ComponentConfig hadoopConf) {
//		SSHExec connection = null;
//		try {
//			
//			LOG.info("Sync hadoop configuration folder from NameNode", hadoopConf.getName(), nodeConfig.getHost());
//			
//			if (conf instanceof Hadoop2Conf) {
//				Hadoop2Conf hadoop2Conf = (Hadoop2Conf) conf;
//				if (hadoop2Conf.isHaEnabled()) {
//					if (hadoop2Conf.getActiveNamenode() != null) {
//						nodePublicIp = hadoop2Conf.getActiveNamenode()
//								.getPublicIp();
//					} else if (hadoop2Conf.getStandByNamenode() != null) {
//						nodePublicIp = hadoop2Conf.getStandByNamenode()
//								.getPublicIp();
//					} else {
//						// LOG.error(nodeConf,
//						// "Could not sync hadoop configuration directory.");
//						// LOG.debug("Both Active & StandBy NameNodes are down.");
//						return false;
//					}
//				} else {
//					nodePublicIp = hadoop2Conf.getNamenode().getPublicIp();
//				}
//			}
//			// connect to cluster NameNode using Public IP
//			connection = SSHUtils.connectToNode(nodePublicIp,
//					conf.getUsername(), conf.getPassword(),
//					conf.getPrivateKey());
//
//			// Use Private IP to sync folder
//			AnkushTask syncConfFolder = new SyncFolder(nodeConf.getPrivateIp(),
//					conf.getHadoopConfDir());
//			Result res = connection.exec(syncConfFolder);
//			if (res.rc != 0) {
//				// LOG.error(nodeConf,
//				// "Could not sync hadoop configuration directory.");
//				return false;
//			}
//
//		} catch (Exception e) {
//			// LOG.error(nodeConf,
//			// "Could not sync hadoop configuration directory.");
//			// LOG.debug(e.getMessage());
//			return false;
//		} finally {
//			// Disconnect from node/machine
//			if (connection != null) {
//				connection.disconnect();
//			}
//		}
//		return true;
//	}
	
}
