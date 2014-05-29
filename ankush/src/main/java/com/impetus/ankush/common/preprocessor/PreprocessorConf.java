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
package com.impetus.ankush.common.preprocessor;

import java.util.HashSet;
import java.util.Set;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * @author Akhil
 *
 */
public class PreprocessorConf extends GenericConfiguration {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The /etc/hosts file. */
	public static final String FILE_ETC_HOST = "/etc/hosts";
	
	/** The node confs. */
	private Set<NodeConf> clusterNodeConfs = new HashSet<NodeConf>();
	
	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public Set<NodeConf> getClusterNodeConfs() {
		return clusterNodeConfs;
	}

	/**
	 * Sets the clusterNodeConfs.
	 * 
	 * @param clusterNodeConfs
	 *            the nodes to set
	 */
	public void setClusterNodeConfs(Set<NodeConf> clusterNodeConfs) {
		this.clusterNodeConfs = clusterNodeConfs;
	}
}
