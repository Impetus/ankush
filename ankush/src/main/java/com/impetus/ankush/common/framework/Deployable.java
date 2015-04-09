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
package com.impetus.ankush.common.framework;

import java.util.List;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

// TODO: Auto-generated Javadoc
/**
 * The Interface Deployable.
 * 
 * @author mayur
 */
public interface Deployable {

	/**
	 * DeployPatch.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean deployPatch(Configuration config);
	
	/**
	 * Deploy.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean deploy(Configuration config);

	/**
	 * Undeploy.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean undeploy(Configuration config);

	/**
	 * Register component.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean registerComponent(Configuration config);

	/**
	 * Unregister component.
	 * 
	 * @param nodeIp
	 *            TODO
	 * @param connection
	 *            TODO
	 * @param config
	 *            the config
	 * 
	 * @return true, if successful
	 */
	boolean unregisterComponent(Configuration config);

	/**
	 * Start.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean start(Configuration config);

	/**
	 * Stop.
	 * 
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean stop(Configuration config);

	/**
	 * Adds the nodes.
	 * 
	 * @param nodeList
	 *            the node list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean addNodes(List<NodeConf> nodeList, Configuration config);

	/**
	 * Removes the nodes.
	 * 
	 * @param nodeList
	 *            the node list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean removeNodes(List<NodeConf> nodeList, Configuration config);

	/**
	 * Start services.
	 * 
	 * @param nodeList
	 *            the node list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean startServices(List<NodeConf> nodeList, Configuration config);

	/**
	 * Stop services.
	 * 
	 * @param nodeList
	 *            the node list
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean stopServices(List<NodeConf> nodeList, Configuration config);

	/**
	 * Validate component.
	 * 
	 * @param nodeIp
	 *            the node ip
	 * @param connection
	 *            the connection
	 * @param config
	 *            the config
	 * @return true, if successful
	 */
	boolean validateComponent(String nodeIp, SSHExec connection,
			GenericConfiguration config);
}
