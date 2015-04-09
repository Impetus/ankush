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
package com.impetus.ankush.common.agent;

import java.util.ArrayList;
import java.util.List;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * It is a configuration class, for the deployment of agent on nodes.
 * 
 * @author hokam chauhan
 * 
 */
public class AgentConf extends GenericConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The technology name. */
	private String technologyName = null;

	/** The nodes. */
	private List<NodeConf> nodes = new ArrayList<NodeConf>();

	/** The agent daemon class. */
	private String agentDaemonClass = null;

	/**
	 * Sets the technology name.
	 * 
	 * @param technologyName
	 *            the technologyName to set
	 */
	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}

	/**
	 * Gets the technology name.
	 * 
	 * @return the technologyName
	 */
	public String getTechnologyName() {
		return technologyName;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodes
	 *            the new nodes
	 */
	public void setNodes(List<NodeConf> nodes) {
		this.nodes = nodes;
	}

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodeMap
	 */
	public List<NodeConf> getNodes() {
		return nodes;
	}

	/**
	 * Sets the agent daemon class.
	 * 
	 * @param agentDaemonClass
	 *            the agentDaemonClass to set
	 */
	public void setAgentDaemonClass(String agentDaemonClass) {
		this.agentDaemonClass = agentDaemonClass;
	}

	/**
	 * Gets the agent daemon class.
	 * 
	 * @return the agentDaemonClass
	 */
	public String getAgentDaemonClass() {
		return agentDaemonClass;
	}
}
