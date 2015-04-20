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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import net.neoremind.sshxcute.core.SSHExec;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.db.DBServiceManager;
import com.impetus.ankush2.framework.monitor.MonitorUtils;

public class NodeConfig implements Configuration {

	private static final long serialVersionUID = 1L;

	String host;
	String publicHost;
	Boolean monitor;
	Long id;
	String datacenter;
	String rack;
	String os;
	boolean status;
	Constant.Node.State state;
	Map<String, Set<String>> errors = new HashMap<String, Set<String>>(); // <comp,list
																			// of
																			// errors>
	Map<String, Set<String>> roles = new HashMap<String, Set<String>>();
	Map<String, Object> params;
	SSHExec connection;

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the publicHost
	 */
	public String getPublicHost() {
		return publicHost;
	}

	/**
	 * @param publicHost
	 *            the publicHost to set
	 */
	public void setPublicHost(String publicHost) {
		this.publicHost = publicHost;
	}

	/**
	 * @return the monitor
	 */
	public Boolean getMonitor() {
		return monitor;
	}

	/**
	 * @param monitor
	 *            the monitor to set
	 */
	public void setMonitor(Boolean monitor) {
		this.monitor = monitor;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the datacenter
	 */
	public String getDatacenter() {
		return datacenter;
	}

	/**
	 * @param datacenter
	 *            the datacenter to set
	 */
	public void setDatacenter(String datacenter) {
		this.datacenter = datacenter;
	}

	/**
	 * @return the rack
	 */
	public String getRack() {
		return rack;
	}

	/**
	 * @param rack
	 *            the rack to set
	 */
	public void setRack(String rack) {
		this.rack = rack;
	}

	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * @param os
	 *            the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		if (this.params == null) {
			this.params = new HashMap<String, Object>();
		}
		this.params.put("message", message);
	}

	/**
	 * @return the status
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the state
	 */
	public Constant.Node.State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(Constant.Node.State state) {
		this.state = state;
	}

	/**
	 * @return the errors
	 */
	public Map<String, Set<String>> getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(Map<String, Set<String>> errors) {
		this.errors = errors;
	}

	/**
	 * @return the roles
	 */
	public Map<String, Set<String>> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Map<String, Set<String>> roles) {
		this.roles = roles;
	}

	/**
	 * @return the params
	 */
	@Transient
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams() {
		this.params = new HashMap<String, Object>();
		NodeMonitoring nodeMonitor = new MonitoringManager()
				.getMonitoringData(id);
		Map health = null;
		if (nodeMonitor != null) {
			health = MonitorUtils.getNodeUsageMap(nodeMonitor);
			// this.params
			// .put("health", MonitorUtils.getNodeUsageMap(nodeMonitor));
		}
		this.params.put("health", health);
		this.params.put("serviceStatus", DBServiceManager.getManager()
				.getServices(null, publicHost, null, null, null, null, null));

	}

	/**
	 * @return the connection
	 */
	@JsonIgnore
	@Transient
	public SSHExec getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(SSHExec connection) {
		this.connection = connection;
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

	public void addRole(String technology, String role) {
		if (!this.roles.containsKey(technology)) {
			this.roles.put(technology, new LinkedHashSet<String>());
		}
		this.roles.get(technology).add(role);
	}

	public void addRole(String technology, Set<String> roles) {
		if (!this.roles.containsKey(technology)) {
			this.roles.put(technology, new LinkedHashSet<String>());
		}
		this.roles.get(technology).addAll(roles);
	}

	public Set<String> removeRole(String technology) {
		return this.roles.remove(technology);
	}

	public void removeRole(String technology, String role) {
		if (this.roles.containsKey(technology)) {
			this.roles.get(technology).remove(role);
		}
	}
}
