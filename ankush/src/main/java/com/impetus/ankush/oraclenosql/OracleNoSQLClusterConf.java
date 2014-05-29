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
package com.impetus.ankush.oraclenosql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class OracleNoSQLClusterConf.
 */
public class OracleNoSQLClusterConf extends ClusterConf {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The oracle no sql conf. */
	private OracleNoSQLConf oracleNoSQLConf;

	/** The newnodes. */
	private List<OracleNoSQLNodeConf> newNodes;

	/**
	 * Gets the oracle no sql conf.
	 *
	 * @return the oracleNoSQLConf
	 */
	public OracleNoSQLConf getOracleNoSQLConf() {
		return oracleNoSQLConf;
	}

	/**
	 * Sets the oracle no sql conf.
	 *
	 * @param oracleNoSQLConf the oracleNoSQLConf to set
	 */
	public void setOracleNoSQLConf(OracleNoSQLConf oracleNoSQLConf) {
		this.oracleNoSQLConf = oracleNoSQLConf;
	}

	/**
	 * Gets the new nodes.
	 *
	 * @return the newnodes
	 */
	public List<OracleNoSQLNodeConf> getNewNodes() {
		return newNodes;
	}

	/**
	 * Sets the new nodes.
	 *
	 * @param newnodes the newnodes to set
	 */
	public void setNewNodes(List<OracleNoSQLNodeConf> newnodes) {
		this.newNodes = newnodes;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.ClusterConf#getNodeCount()
	 */
	@Override
	public int getNodeCount() {
		return (oracleNoSQLConf.getNodes() == null ? 0 : oracleNoSQLConf
				.getNodes().size());
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.ClusterConf#getNodeConfs()
	 */
	@Override
	public List<NodeConf> getNodeConfs() {
		return new ArrayList<NodeConf>(oracleNoSQLConf.getNodes());
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.ClusterConf#getClusterComponents()
	 */
	@Override
	public Map<String, GenericConfiguration> getClusterComponents() {
		Map<String, GenericConfiguration> components = new HashMap<String, GenericConfiguration>();
		components.put(Constant.Technology.ORACLE_NOSQL, getOracleNoSQLConf());
		return components;
	}
}
