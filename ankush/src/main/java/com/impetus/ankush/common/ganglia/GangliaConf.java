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
package com.impetus.ankush.common.ganglia;

import java.util.HashSet;
import java.util.Set;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
/**
 * It is a configuration class, for deployment of ganglia on nodes.
 * 
 * @author Hokam Chauhan
 * 
 */
public class GangliaConf extends GenericConfiguration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The rrd directory. */
	public static final String RRD_DIRECTORY = "/var/lib/ganglia/rrds/";
	
	/** The dwoo directory. */
	public static final String DWOO_DIRECTORY = "/var/lib/ganglia/dwoo/";

	/** The port. */
	private Integer port;
	
	/** The polling interval. */
	private Integer pollingInterval;
	
	/** The grid name. */
	private String gridName;
	
	/** The gmetad node. */
	private NodeConf gmetadNode = null;
	
	/** The gmond nodes. */
	private Set<NodeConf> gmondNodes = new HashSet<NodeConf>();
	
	/** The server conf folder. */
	private String serverConfFolder = null;
	
	/** The rrd file path. */
	private String rrdFilePath = RRD_DIRECTORY;
	
	/** The dwoo file path. */
	private String dwooFilePath = DWOO_DIRECTORY;

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 *
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * Gets the polling interval.
	 *
	 * @return the pollingInterval
	 */
	public Integer getPollingInterval() {
		return pollingInterval;
	}

	/**
	 * Sets the polling interval.
	 *
	 * @param pollingInterval the pollingInterval to set
	 */
	public void setPollingInterval(Integer pollingInterval) {
		this.pollingInterval = pollingInterval;
	}

	/**
	 * Gets the grid name.
	 *
	 * @return the gridName
	 */
	public String getGridName() {
		return gridName;
	}

	/**
	 * Sets the grid name.
	 *
	 * @param gridName the gridName to set
	 */
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	/**
	 * Gets the gmetad node.
	 *
	 * @return the gmetadNode
	 */
	public NodeConf getGmetadNode() {
		return gmetadNode;
	}

	/**
	 * Sets the gmetad node.
	 *
	 * @param gmetadNode the gmetadNode to set
	 */
	public void setGmetadNode(NodeConf gmetadNode) {
		this.gmetadNode = gmetadNode;
		/*String nodeType = gmetadNode.getType();
		System.out.println("nodeIp-"+ gmetadNode.getPublicIp() +"nodeType in setGmetadNode-"+nodeType);
		if(nodeType != null) {
			if(nodeType.isEmpty()) {
				gmetadNode.setType(Constant.Role.GMETAD);
			} else {
				gmetadNode.setType(nodeType + "/" + Constant.Role.GMETAD);
			}	
		}*/
	}

	/**
	 * Gets the gmond nodes.
	 *
	 * @return the gmondNodes
	 */
	public Set<NodeConf> getGmondNodes() {
		return gmondNodes;
	}

	/**
	 * Sets the gmond nodes.
	 *
	 * @param gmondNodes the gmondNodes to set
	 */
	public void setGmondNodes(Set<NodeConf> gmondNodes) {
		this.gmondNodes = gmondNodes;
	}

	/**
	 * Sets the server conf folder.
	 *
	 * @param serverConfFolder the serverConfFolder to set
	 */
	public void setServerConfFolder(String serverConfFolder) {
		this.serverConfFolder = serverConfFolder;
	}

	/**
	 * Gets the server conf folder.
	 *
	 * @return the serverConfFolder
	 */
	public String getServerConfFolder() {
		return serverConfFolder;
	}

	/**
	 * Sets the rrd file path.
	 *
	 * @param rrdFilePath the rrdFilePath to set
	 */
	public void setRrdFilePath(String rrdFilePath) {
		this.rrdFilePath = rrdFilePath;
	}

	/**
	 * Gets the rrd file path.
	 *
	 * @return the rrdFilePath
	 */
	public String getRrdFilePath() {
		return rrdFilePath;
	}

	/**
	 * Sets the dwoo file path.
	 *
	 * @param dwooFilePath the dwooFilePath to set
	 */
	public void setDwooFilePath(String dwooFilePath) {
		this.dwooFilePath = dwooFilePath;
	}

	/**
	 * Gets the dwoo file path.
	 *
	 * @return the dwooFilePath
	 */
	public String getDwooFilePath() {
		return dwooFilePath;
	}

	/**
	 * Return True if the passed node is the gmetad node else false.
	 *
	 * @param nodeConf the node conf
	 * @return true, if is gmetad node
	 */
	public boolean isGmetadNode(NodeConf nodeConf) {

		// Returning false if any object in comparison is null
		if (nodeConf == null || this.gmetadNode == null) {
			return false;
		}
		// Returning the equals status of node with gmetad node.
		return nodeConf.equals(this.gmetadNode);
	}
}
