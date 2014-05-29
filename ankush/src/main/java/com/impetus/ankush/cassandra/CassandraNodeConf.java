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
package com.impetus.ankush.cassandra;

import java.util.ArrayList;
import java.util.List;

import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class CassandraNodeConf.
 * 
 * @author saurabh
 */
public class CassandraNodeConf extends NodeConf {

	/** The Constant strDefaultRack. */
	public final static String strDefaultRack = "DC1:r1";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The seed node. */
	private Boolean seedNode;

	/** The rack info. */
	private String rackInfo = new String(CassandraNodeConf.strDefaultRack);

	/** The rackName. */
	private String rackName = new String();

	/** The tokenValue. */
	private String tokenValue = new String();

	private List<String> tokens = new ArrayList<String>();

	/** The dataCenterName. */
	private String dataCenterName = new String();

	/** The nodeStatus. */
	private String nodeStatus = new String();

	/** The clusterOwnership. */
	private String clusterOwnership = new String();

	/** The nodeLoad. */
	private String nodeLoad = new String();

	/** The vNodeCount. */
	private int vNodeCount = 256;

	/**
	 * Gets the seed node.
	 * 
	 * @return the seedNode
	 */
	public Boolean isSeedNode() {
		return this.seedNode;
	}

	/**
	 * Sets the seed node.
	 * 
	 * @param seedNode
	 *            the seedNode to set
	 */
	public void isSeedNode(Boolean seedNode) {
		this.seedNode = seedNode;
	}

	/**
	 * @return the rackInfo
	 */
	public String getRackInfo() {
		return rackInfo;
	}

	/**
	 * @param rackInfo
	 *            the rackInfo to set
	 */
	public void setRackInfo(String rackInfo) {
		this.rackInfo = rackInfo;
	}

	/**
	 * @return the tokenValue
	 */
	public String getTokenValue() {
		return tokenValue;
	}

	/**
	 * @param tokenValue
	 *            the tokenValue to set
	 */
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	/**
	 * @return the dataCenterName
	 */
	public String getDataCenterName() {
		return dataCenterName;
	}

	/**
	 * @param dataCenterName
	 *            the dataCenterName to set
	 */
	public void setDataCenterName(String dataCenterName) {
		this.dataCenterName = dataCenterName;
	}

	/**
	 * @return the rackName
	 */
	public String getRackName() {
		return rackName;
	}

	/**
	 * @param rackName
	 *            the rackName to set
	 */
	public void setRackName(String rackName) {
		this.rackName = rackName;
	}

	/**
	 * @return the nodeStatus
	 */
	public String getNodeStatus() {
		return nodeStatus;
	}

	/**
	 * @param nodeStatus
	 *            the nodeStatus to set
	 */
	public void setNodeStatus(String nodeStatus) {
		this.nodeStatus = nodeStatus;
	}

	/**
	 * @return the clusterOwnership
	 */
	public String getClusterOwnership() {
		return clusterOwnership;
	}

	/**
	 * @param clusterOwnership
	 *            the clusterOwnership to set
	 */
	public void setClusterOwnership(String clusterOwnership) {
		this.clusterOwnership = clusterOwnership;
	}

	/**
	 * @return the nodeLoad
	 */
	public String getNodeLoad() {
		return nodeLoad;
	}

	/**
	 * @param nodeLoad
	 *            the nodeLoad to set
	 */
	public void setNodeLoad(String nodeLoad) {
		this.nodeLoad = nodeLoad;
	}

	/**
	 * @return the tokens
	 */
	public List<String> getTokens() {
		return tokens;
	}

	/**
	 * @param tokens
	 *            the tokens to set
	 */
	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}

	/**
	 * @return the vNodeCount
	 */
	public int getvNodeCount() {
		return vNodeCount;
	}

	/**
	 * @param vNodeCount
	 *            the vNodeCount to set
	 */
	public void setvNodeCount(int vNodeCount) {
		this.vNodeCount = vNodeCount;
	}

}
