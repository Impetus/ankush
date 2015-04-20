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
package com.impetus.ankush2.hadoop.monitor.commandsmanager;

import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.hadoop.config.ComponentConfigContext;
import com.impetus.ankush2.hadoop.deployer.servicemanager.HadoopBundleServiceManager;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopCommandsManager.
 *
 * @author Akhil
 */
public abstract class HadoopCommandsManager extends ComponentConfigContext {
	
	/**
	 * Instantiates a new hadoop commands manager.
	 *
	 * @param clusterConfig the cluster config
	 * @param hadoopConfig the hadoop config
	 * @param classObj the class obj
	 */
	public HadoopCommandsManager(ClusterConfig clusterConfig, ComponentConfig hadoopConfig, Class classObj) {
		super(clusterConfig, hadoopConfig, HadoopBundleServiceManager.class);
//		this.clusterConfig = clusterConfig;
//		this.hadoopConfig = hadoopConfig;
//		this.LOG = new AnkushLogger(classObj);
//		this.LOG.setCluster(clusterConfig);
	}
	
	public HadoopCommandsManager() {
		
	}
	
	/**
	 * Format name node.
	 *
	 * @return true, if successful
	 */
	public abstract boolean formatNameNode();
	
	/**
	 * Submit application.
	 *
	 * @return true, if successful
	 */
	public abstract boolean submitApplication();

}
