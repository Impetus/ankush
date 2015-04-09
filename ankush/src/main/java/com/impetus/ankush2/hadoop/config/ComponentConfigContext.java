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
package com.impetus.ankush2.hadoop.config;

import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * @author Akhil
 *
 */
public class ComponentConfigContext {
	
	public ComponentConfigContext() {
		
	}
	
	public ComponentConfigContext(ClusterConfig clusterConfig,
			ComponentConfig compConfig, Class classObj) {
		this.clusterConfig = clusterConfig;
		this.compConfig = compConfig;
		this.LOG = new AnkushLogger(classObj);
		this.LOG.setCluster(clusterConfig);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComponentConfigContext [clusterConfig=" + clusterConfig
				+ ", compConfig=" + compConfig + "]";
	}

	/** The log. */
	protected AnkushLogger LOG;

	/** The cluster config. */
	protected ClusterConfig clusterConfig;

	/** The hadoop config. */
	protected ComponentConfig compConfig;
	
	/**
	 * @return the lOG
	 */
	public AnkushLogger getLOG() {
		return LOG;
	}

	/**
	 * @param lOG the lOG to set
	 */
	public void setLOG(AnkushLogger lOG) {
		LOG = lOG;
	}
	
	public void setLOG(Class classObj) {
		this.LOG = new AnkushLogger(classObj, clusterConfig);
	}

	/**
	 * @return the clusterConfig
	 */
	public ClusterConfig getClusterConfig() {
		return clusterConfig;
	}

	/**
	 * @param clusterConfig the clusterConfig to set
	 */
	public void setClusterConfig(ClusterConfig clusterConfig) {
		this.clusterConfig = clusterConfig;
	}

	/**
	 * @return the hadoopConfig
	 */
	public ComponentConfig getComponentConfig() {
		return compConfig;
	}

	/**
	 * @param hadoopConfig the hadoopConfig to set
	 */
	public void setComponentConfig(ComponentConfig compConfig) {
		this.compConfig = compConfig;
	}
}
