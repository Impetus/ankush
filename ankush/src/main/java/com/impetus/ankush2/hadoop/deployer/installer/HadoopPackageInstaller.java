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
package com.impetus.ankush2.hadoop.deployer.installer;

import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;

/**
 * @author Akhil
 *
 */
public class HadoopPackageInstaller extends HadoopInstaller {

	/**
	 * 
	 */
	public HadoopPackageInstaller() {
		super();
	}

	/**
	 * @param clusterConfig
	 * @param hadoopConfig
	 */
	public HadoopPackageInstaller(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig) {
		super(clusterConfig, hadoopConfig, HadoopPackageInstaller.class);
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush2.hadoop.deployer.HadoopInstaller#createNode(com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	public boolean createNode(NodeConfig nodeConfig) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush2.hadoop.deployer.HadoopInstaller#removeNode(com.impetus.ankush2.framework.config.NodeConfig)
	 */
	@Override
	public boolean removeNode(NodeConfig nodeConfig) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addNode(NodeConfig nodeConfig) {
		// TODO Auto-generated method stub
		return false;
	}

}
