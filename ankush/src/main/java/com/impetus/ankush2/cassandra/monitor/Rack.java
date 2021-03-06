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
package com.impetus.ankush2.cassandra.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rackName = new String();

	private List<CassandraNode> nodes = new ArrayList<CassandraNode>();

	/**
	 * @return the nodes
	 */
	public List<CassandraNode> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(List<CassandraNode> nodes) {
		this.nodes = nodes;
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

}
