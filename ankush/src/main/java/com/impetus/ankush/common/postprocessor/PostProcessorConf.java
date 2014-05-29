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
package com.impetus.ankush.common.postprocessor;

import java.util.HashSet;
import java.util.Set;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

// TODO: Auto-generated Javadoc
/**
 * The Class PostProcessorConf.
 * 
 * @author Akhil
 */

public class PostProcessorConf extends GenericConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The file path for Jmx Trans Script File. */
	private String jmxTransScriptFilePath = null;

	/** The node confs. */
	private Set<NodeConf> clusterNodeConfs = new HashSet<NodeConf>();

	/** The Ip Address for Ganglia Master. */
	private String gangliaMasterIp = null;
	
	/**
	 * Instantiates a new post processor conf.
	 */
	public PostProcessorConf() {
		super();
		this.jmxTransScriptFilePath = "";
		this.clusterNodeConfs = null;
	}

	/**
	 * Instantiates a new post processor conf.
	 * 
	 * @param jmxTransScriptFilePath
	 *            the jmx trans script file path
	 * @param clusterNodeConfs
	 *            the cluster node confs
	 */
	public PostProcessorConf(String jmxTransScriptFilePath,
			Set<NodeConf> clusterNodeConfs) {
		super();
		this.jmxTransScriptFilePath = jmxTransScriptFilePath;
		this.clusterNodeConfs = clusterNodeConfs;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public Set<NodeConf> getClusterNodeConfs() {
		return this.clusterNodeConfs;
	}

	/**
	 * Gets the jmx trans script file path.
	 * 
	 * @return the jmxTransScriptFilePath
	 */
	public String getJmxTransScriptFilePath() {
		return this.jmxTransScriptFilePath;
	}

	/**
	 * @return the gangliaMasterIp
	 */
	public String getGangliaMasterIp() {
		return gangliaMasterIp;
	}

	/**
	 * @param gangliaMasterIp the gangliaMasterIp to set
	 */
	public void setGangliaMasterIp(String gangliaMasterIp) {
		this.gangliaMasterIp = gangliaMasterIp;
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

	/**
	 * Sets the jmx trans script file path.
	 * 
	 * @param jmxTransScriptFilePath
	 *            the jmxTransScriptFilePath to set
	 */
	public void setJmxTransScriptFilePath(String jmxTransScriptFilePath) {
		this.jmxTransScriptFilePath = jmxTransScriptFilePath;
	}

}
