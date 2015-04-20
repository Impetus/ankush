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
package com.impetus.ankush2.framework.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.domain.Operation;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush2.agent.AgentConstant;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBOperationManager;

public class ClusterConfig implements Configuration {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Long clusterId;
	String name;
	String createdBy;
	String hosts;
	String hostFile;
	String technology;
	Long operationId;
	Constant.Cluster.State state;
	AuthConfig authConf;
	ComponentConfig javaConf;
	ProgressConfig progress;
	Map<String, NodeConfig> nodes;
	Constant.Cluster.InstallationType installationType;
	Map<String, ComponentConfig> components;
	Map<String, Object> data = new HashMap<String, Object>();
	Map<String, Set<String>> errors = new HashMap<String, Set<String>>(); // Map
																			// Component
																			// Specific
																			// Errors
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClusterConfig [clusterId=" + clusterId + ", name=" + name
				+ ", createdBy=" + createdBy + ", hosts=" + hosts
				+ ", hostFile=" + hostFile + ", technology=" + technology
				+ ", operationId=" + operationId + ", state=" + state
				+ ", authConf=" + authConf + ", javaConf=" + javaConf
				+ ", progress=" + progress + ", nodes=" + nodes
				+ ", components=" + components + ", data=" + data + ", errors="
				+ errors + "]";
	}

	public void incrementOperation() throws AnkushException {
		if (allowNewOperation()) {
			if (operationId == null || operationId < 1) {
				operationId = 1L;
			} else {
				operationId = new DBOperationManager()
						.getNewOperationId(clusterId);
			}
			resetErrorObjects();
		}
	}

	private boolean allowNewOperation() throws AnkushException {
		try {
			List<Operation> operations = new DBOperationManager()
					.getOperations(this.clusterId, null, null,
							Constant.Operation.Status.INPROGRESS.toString());
			if (operations.size() == 0) {
				return true;
			} else if (operations.size() == 1) {
				throw new AnkushException(
						operations.get(0).getOpName()
								+ " operation is already in progress, so cannot start any other operation.");
			} else {
				throw new AnkushException(
						"Invalid cluster state as there are more than one operation in progress, so could not start new operation");
			}
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"Could not identify cluster state to start new operation.",
					e);
		}
	}

	private void resetErrorObjects() {
		for (NodeConfig nodeConfig : this.nodes.values()) {
			nodeConfig.setErrors(new LinkedHashMap<String, Set<String>>());
		}
		errors = new HashMap<String, Set<String>>();
	}

	public Long getClusterId() {
		return clusterId;
	}

	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public String getHostFile() {
		return hostFile;
	}

	public void setHostFile(String hostFile) {
		this.hostFile = hostFile;
	}

	/**
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * @param technology
	 *            the technology to set
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Long getOperationId() {
		return operationId;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	public Constant.Cluster.State getState() {
		return state;
	}

	public void setState(Constant.Cluster.State state) {
		this.state = state;
	}

	public AuthConfig getAuthConf() {
		return authConf;
	}

	public void setAuthConf(AuthConfig authConf) {
		this.authConf = authConf;
	}

	public ComponentConfig getJavaConf() {
		return javaConf;
	}

	public void setJavaConf(ComponentConfig javaConf) {
		this.javaConf = javaConf;
	}

	/**
	 * @return the progress
	 */
	@JsonIgnore
	@Transient
	public ProgressConfig getProgress() {
		return progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(ProgressConfig progress) {
		this.progress = progress;
	}

	public Map<String, NodeConfig> getNodes() {
		return nodes;
	}

	public void setNodes(Map<String, NodeConfig> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the installationType
	 */
	public Constant.Cluster.InstallationType getInstallationType() {
		return installationType;
	}

	/**
	 * @param installationType
	 *            the installationType to set
	 */
	public void setInstallationType(
			Constant.Cluster.InstallationType installationType) {
		this.installationType = installationType;
	}

	@JsonIgnore
	public String getAgentInstallDir() {
		return FileNameUtils.convertToValidPath(components
				.get(Constant.Component.Name.AGENT).installPath);

	}

	@JsonIgnore
	public String getAgentHomeDir() {
		return FileNameUtils.convertToValidPath(components
				.get(Constant.Component.Name.AGENT).installPath)
				+ AgentConstant.Relative_Path.AGENT_HOME_DIR;
	}

	public Map<String, ComponentConfig> getComponents() {
		return components;
	}

	public void setComponents(Map<String, ComponentConfig> components) {
		this.components = components;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Set<String>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, Set<String>> errors) {
		this.errors = errors;
	}

	public void addError(String key, String error) {
		if (!this.errors.containsKey(key)) {
			this.errors.put(key, new LinkedHashSet<String>());
		}
		this.errors.get(key).add(error);
	}

	public void addError(String key, Set<String> errors) {
		if (!this.errors.containsKey(key)) {
			this.errors.put(key, new LinkedHashSet<String>());
		}
		this.errors.get(key).addAll(errors);
	}

	public void addError(String host, String key, String error) {
		NodeConfig nodeConf = getNodes().get(host);
		if (nodeConf != null) {
			nodeConf.addError(key, error);
		}
	}

	public void addError(String host, String key, Set<String> errors) {
		NodeConfig nodeConf = getNodes().get(host);
		if (nodeConf != null) {
			nodeConf.addError(key, errors);
		}
	}

}
