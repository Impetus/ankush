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
package com.impetus.ankush.hadoop.config;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class HadoopNodeConf.
 * 
 * @author hokam
 */
public class HadoopNodeConf extends NodeConf {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name node. */
	private Boolean nameNode;

	/** The data node. */
	private Boolean dataNode;

	/** The secondary name node. */
	private Boolean secondaryNameNode;

	@JsonIgnore
	private Boolean standByNameNode = false;
	
	/**
	 * Instantiates a new node conf.
	 */
	public HadoopNodeConf() {
		super();
	}

	/**
	 * Instantiates a new node conf.
	 * 
	 * @param publicIp
	 *            the public ip
	 * @param privateIp
	 *            the private ip
	 */
	public HadoopNodeConf(String publicIp, String privateIp) {
		super(publicIp, privateIp);
	}
	
	/**
	 * Gets the name node.
	 * 
	 * @return the nameNode
	 */
	public Boolean getNameNode() {
		return nameNode;
	}

	/**
	 * Sets the name node.
	 * 
	 * @param nameNode
	 *            the nameNode to set
	 */
	public void setNameNode(Boolean nameNode) {
		this.nameNode = nameNode;
	}

	/**
	 * Gets the data node.
	 * 
	 * @return the dataNode
	 */
	public Boolean getDataNode() {
		return dataNode;
	}

	/**
	 * Sets the data node.
	 * 
	 * @param dataNode
	 *            the dataNode to set
	 */
	public void setDataNode(Boolean dataNode) {
		this.dataNode = dataNode;
	}

	/**
	 * Gets the secondary name node.
	 * 
	 * @return the secondaryNameNode
	 */
	public Boolean getSecondaryNameNode() {
		return secondaryNameNode;
	}

	/**
	 * Sets the secondary name node.
	 * 
	 * @param secondaryNameNode
	 *            the secondaryNameNode to set
	 */
	public void setSecondaryNameNode(Boolean secondaryNameNode) {
		this.secondaryNameNode = secondaryNameNode;
	}

	/**
	 * @return the standByNameNode
	 */
	public Boolean getStandByNameNode() {
		return standByNameNode;
	}

	/**
	 * @param standByNameNode the standByNameNode to set
	 */
	public void setStandByNameNode(Boolean standByNameNode) {
		this.standByNameNode = standByNameNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//		if (getClass() != obj.getClass()) {
//			return false;
//		}
		if (!super.equals(obj)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HadoopNodeConf [nameNode=" + nameNode + ", dataNode="
				+ dataNode + ", secondaryNameNode=" + secondaryNameNode
				+ "]";
	}
}
