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
package com.impetus.ankush.common.dependency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * It is the configuration class, used the deploy the dependency on nodes.
 * 
 * @author hokam chauhan
 * 
 */
public class DependencyConf extends GenericConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The node confs. */
	private Set<NodeConf> nodeConfs = new HashSet<NodeConf>();

	private Map<String, Configuration> components = new HashMap<String, Configuration>();

	/** The install java. */
	private Boolean installJava = false;

	/** The java bin file name. */
	private String javaBinFileName = null;

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 */
	public Set<NodeConf> getNodes() {
		return nodeConfs;
	}

	/**
	 * Sets the nodes.
	 * 
	 * @param nodeConfs
	 *            the nodes to set
	 */
	public void setNodes(Set<NodeConf> nodeConfs) {
		this.nodeConfs = nodeConfs;
	}

	/**
	 * Gets the install java.
	 * 
	 * @return the installJava
	 */
	public Boolean getInstallJava() {
		return installJava;
	}

	/**
	 * Sets the install java.
	 * 
	 * @param installJava
	 *            the installJava to set
	 */
	public void setInstallJava(Boolean installJava) {
		this.installJava = installJava;
	}

	/**
	 * Gets the java bin file name.
	 * 
	 * @return the javaBinFileName
	 */
	public String getJavaBinFileName() {
		return javaBinFileName;
	}

	/**
	 * Sets the java bin file name.
	 * 
	 * @param javaBinFileName
	 *            the javaBinFileName to set
	 */
	public void setJavaBinFileName(String javaBinFileName) {
		this.javaBinFileName = javaBinFileName;
	}

	/**
	 * @param componentsMap the componentsMap to set
	 */
	public void setComponents(Map<String, Configuration> componentsMap) {
		this.components = componentsMap;
	}

	/**
	 * @return the componentsMap
	 */
	public Map<String, Configuration> getComponents() {
		return components;
	}
}
