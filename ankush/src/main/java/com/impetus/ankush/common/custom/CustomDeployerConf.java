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
package com.impetus.ankush.common.custom;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * @author mayur
 * 
 */
public class CustomDeployerConf extends GenericConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String scriptFilePath = null;

	private String scriptArgs = null;

	private List<NodeConf> nodes = null;

	private int priority;
	/**
	 * @return the scriptFilePath
	 */
	public String getScriptFilePath() {
		return scriptFilePath;
	}

	/**
	 * @param scriptFilePath
	 *            the scriptFilePath to set
	 */
	public void setScriptFilePath(String scriptFilePath) {
		this.scriptFilePath = scriptFilePath;
	}

	/**
	 * @return the scriptArgs
	 */
	public String getScriptArgs() {
		return scriptArgs;
	}

	/**
	 * @param scriptArgs
	 *            the scriptArgs to set
	 */
	public void setScriptArgs(String scriptArgs) {
		this.scriptArgs = scriptArgs;
	}

	/**
	 * @return the nodes
	 */
	public List<NodeConf> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<NodeConf> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@Override
	public Set<NodeConf> getCompNodes() {
		return new HashSet<NodeConf>(nodes);
	}
}
