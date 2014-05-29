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
package com.impetus.ankush.storm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.yaml.snakeyaml.Yaml;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class StormConf.
 * 
 * @author mayur
 */
public class StormConf extends GenericConfiguration {

	/** The Jmx Port for Supervisor. */
	private String jmxPort_Supervisor = "12346";
	
	/** The Jmx Port for Nimbus. */
	private String jmxPort_Nimbus = "12347";
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The nimbus. */
	private NodeConf nimbus = null;

	/** The supervisors. */
	private List<NodeConf> supervisors = new ArrayList<NodeConf>();

	/** The zk nodes. */
	private List<String> zkNodes = new ArrayList<String>();

	/** The slots ports. */
	private List<Integer> slotsPorts = new ArrayList<Integer>(Arrays.asList(
			Integer.valueOf(6700), Integer.valueOf(6701),
			Integer.valueOf(6702), Integer.valueOf(6703)));

	/** The local dir. */
	private String localDir = null;

	/** The ui port. */
	private int uiPort = 9090;
	
	private int zookeeperPort = 2181;

	/**
	 * Gets the nimbus.
	 * 
	 * @return the nimbus
	 */
	public NodeConf getNimbus() {
		return nimbus;
	}

	/**
	 * Sets the nimbus.
	 * 
	 * @param nimbus
	 *            the nimbus to set
	 */
	public void setNimbus(NodeConf nimbus) {
		this.nimbus = nimbus;
	}

	/**
	 * Gets the supervisors.
	 * 
	 * @return the supervisors
	 */
	public List<NodeConf> getSupervisors() {
		return supervisors;
	}

	/**
	 * Sets the supervisors.
	 * 
	 * @param supervisors
	 *            the supervisors to set
	 */
	public void setSupervisors(ArrayList<NodeConf> supervisors) {
		this.supervisors = supervisors;
	}

	/**
	 * Gets the zk nodes.
	 * 
	 * @return the zkNodes
	 */
	public List<String> getZkNodes() {
		return zkNodes;
	}

	/**
	 * Sets the zk nodes.
	 * 
	 * @param zkNodes
	 *            the zkNodes to set
	 */
	public void setZkNodes(ArrayList<String> zkNodes) {
		this.zkNodes = zkNodes;
	}

	/**
	 * Gets the slots ports.
	 * 
	 * @return the slotsPorts
	 */
	public List<Integer> getSlotsPorts() {
		return slotsPorts;
	}

	/**
	 * Sets the slots ports.
	 * 
	 * @param slotsPorts
	 *            the slotsPorts to set
	 */
	public void setSlotsPorts(ArrayList<Integer> slotsPorts) {
		this.slotsPorts = slotsPorts;
	}

	/**
	 * Gets the local dir.
	 * 
	 * @return the localDir
	 */
	public String getLocalDir() {
		return localDir;
	}

	/**
	 * Sets the local dir.
	 * 
	 * @param localDir
	 *            the localDir to set
	 */
	public void setLocalDir(String localDir) {
		this.localDir = localDir;
	}

	/**
	 * Gets the ui port.
	 * 
	 * @return the uiPort
	 */
	public int getUiPort() {
		return uiPort;
	}

	/**
	 * Sets the ui port.
	 * 
	 * @param uiPort
	 *            the uiPort to set
	 */
	public void setUiPort(int uiPort) {
		this.uiPort = uiPort;
	}

	/**
	 * @param zookeeperPort the zookeeperPort to set
	 */
	public void setZookeeperPort(int zookeeperPort) {
		this.zookeeperPort = zookeeperPort;
	}

	/**
	 * @return the zookeeperPort
	 */
	public int getZookeeperPort() {
		return zookeeperPort;
	}

	/**
	 * @return the jmxPort_Supervisor
	 */
	public String getJmxPort_Supervisor() {
		return jmxPort_Supervisor;
	}

	/**
	 * @param jmxPort_Supervisor the jmxPort_Supervisor to set
	 */
	public void setJmxPort_Supervisor(String jmxPort_Supervisor) {
		this.jmxPort_Supervisor = jmxPort_Supervisor;
	}

	/**
	 * @return the jmxPort_Nimbus
	 */
	public String getJmxPort_Nimbus() {
		return jmxPort_Nimbus;
	}

	/**
	 * @param jmxPort_Nimbus the jmxPort_Nimbus to set
	 */
	public void setJmxPort_Nimbus(String jmxPort_Nimbus) {
		this.jmxPort_Nimbus = jmxPort_Nimbus;
	}

	/**
	 * Gets the yaml contents.
	 * 
	 * @return yaml file contents
	 */
	@JsonIgnore
	public String getYamlContents(NodeConf nodeConf) {
		Yaml yaml = new Yaml();
		Map yamlContents = getConfigurationMap(nodeConf);
		
		return yaml.dumpAsMap(yamlContents);
	}

	/**
	 * Gets the configuration map.
	 * 
	 * @return the configuration map
	 */
	@JsonIgnore
	public Map<String, Object> getConfigurationMap(NodeConf nodeConf) {
		Map<String, Object> yamlContents = new HashMap<String, Object>();
		yamlContents.put("storm.local.dir", this.localDir);
		yamlContents.put("nimbus.host", this.nimbus.getPrivateIp());
		yamlContents.put("storm.zookeeper.servers", this.zkNodes);
		yamlContents.put("supervisor.slots.ports", this.slotsPorts);
		yamlContents.put("ui.port", this.uiPort);
		yamlContents.put("storm.zookeeper.port", this.zookeeperPort);
		yamlContents.put("supervisor.childopts", AppStoreWrapper.getAnkushConfReader().getStringValue("jmx.yaml.childopts.supervisor"));
		if(nodeConf.getPrivateIp().equals(this.getNimbus().getPrivateIp()) ) {
			yamlContents.put("nimbus.childopts", AppStoreWrapper.getAnkushConfReader().getStringValue("jmx.yaml.childopts.nimbus"));
		}
		return yamlContents;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StormConf [nimbus=" + nimbus + ", supervisors=" + supervisors
				+ ", zkNodes=" + zkNodes + ", slotsPorts=" + slotsPorts
				+ ", localDir=" + localDir + ", uiPort=" + uiPort
				+ ", getNimbus()=" + getNimbus() + ", getSupervisors()="
				+ getSupervisors() + ", getZkNodes()=" + getZkNodes()
				+ ", getSlotsPorts()=" + getSlotsPorts() + ", getLocalDir()="
				+ getLocalDir() + ", getUiPort()=" + getUiPort()
				+ ", getUsername()=" + getUsername() + ", getPassword()="
				+ getPassword() + ", getComponentVersion()="
				+ getComponentVersion() + ", getInstallationPath()="
				+ getInstallationPath() + ", getLocalBinaryFile()="
				+ getLocalBinaryFile() + ", getTarballUrl()=" + getTarballUrl()
				+ ", getServerTarballLocation()=" + getServerTarballLocation()
				+ ", getPrivateKey()=" + getPrivateKey()
				+ ", getComponentHome()=" + getComponentHome()
				+ ", getClass()=" + getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.config.GenericConfiguration#getCompNodes
	 * ()
	 */
	@Override
	public Set<NodeConf> getCompNodes() {
		Set<NodeConf> nodes = new HashSet<NodeConf>();
		nodes.add(nimbus);
		nodes.addAll(supervisors);
		return nodes;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.config.GenericConfiguration#addNewNodes()
	 */
	@Override
	public void addNewNodes() {
		this.getSupervisors().addAll(this.getNewNodes());
	}
}
