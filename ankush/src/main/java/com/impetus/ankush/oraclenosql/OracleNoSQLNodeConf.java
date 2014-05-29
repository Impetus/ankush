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
package com.impetus.ankush.oraclenosql;

import net.neoremind.sshxcute.core.SSHExec;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * Oracle NoSQL Node configuration.
 * 
 * @author nikunj
 */
public class OracleNoSQLNodeConf extends NodeConf {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant DEFAULT_TYPE. */
	private static final String DEFAULT_TYPE = "StorageNode";

	/** The Constant STATE_DEPLOYING. */
	private static final String STATE_DEPLOYING = "depoying";

	/** The Constant STATE_STARTED. */
	public static final String STATE_STARTED = "started";

	/** The Constant STATE_DEPLOYED. */
	public static final String STATE_DEPLOYED = "deployed";

	/** The Constant STATE_ERROR. */
	public static final String STATE_ERROR = "error";

	/** The Constant DEFAULT_CAPACITY. */
	private static final int DEFAULT_CAPACITY = 1;

	/** The Constant DEFAULT_CPU_NUM. */
	private static final int DEFAULT_CPU_NUM = 0;

	/** The Constant DEFAULT_SN_ID. */
	private static final int DEFAULT_SN_ID = 0;

	/** The Constant DEFAULT_MEMORY. */
	private static final long DEFAULT_MEMORY = 0;

	/** The admin. */
	private Boolean admin;

	/** The admin port. */
	private Integer adminPort;

	/** The registry port. */
	private Integer registryPort;

	/** The sn id. */
	private Integer snId;

	/** The ha port range start. */
	private Integer haPortRangeStart;

	/** The ha port range end. */
	private Integer haPortRangeEnd;

	/** The memory mb. */
	private Long memoryMb;

	/** The capacity. */
	private Integer capacity;

	/** The cpu num. */
	private Integer cpuNum;

	/** The storage dirs. */
	private String storageDirs;

	/** The connection. */
	private transient SSHExec connection = null;

	/**
	 * Constructor to set default value.
	 */
	public OracleNoSQLNodeConf() {
		super();
		admin = Boolean.FALSE;
		snId = Integer.valueOf(DEFAULT_SN_ID);
		memoryMb = Long.valueOf(DEFAULT_MEMORY);
		capacity = Integer.valueOf(DEFAULT_CAPACITY);
		cpuNum = Integer.valueOf(DEFAULT_CPU_NUM);
		storageDirs = null;
		setType(DEFAULT_TYPE);
		setNodeState(STATE_DEPLOYING);
	}

	/**
	 * Gets the ha port range start.
	 * 
	 * @return the haPortRangeStart
	 */
	public Integer getHaPortRangeStart() {
		return haPortRangeStart;
	}

	/**
	 * Sets the ha port range start.
	 * 
	 * @param haPortRangeStart
	 *            the haPortRangeStart to set
	 */
	public void setHaPortRangeStart(Integer haPortRangeStart) {
		this.haPortRangeStart = haPortRangeStart;
	}

	/**
	 * Gets the ha port range end.
	 * 
	 * @return the haPortRangeEnd
	 */
	public Integer getHaPortRangeEnd() {
		return haPortRangeEnd;
	}

	/**
	 * Sets the ha port range end.
	 * 
	 * @param haPortRangeEnd
	 *            the haPortRangeEnd to set
	 */
	public void setHaPortRangeEnd(Integer haPortRangeEnd) {
		this.haPortRangeEnd = haPortRangeEnd;
	}

	/**
	 * Checks if is admin.
	 * 
	 * @return the admin
	 */
	public Boolean isAdmin() {
		return admin;
	}

	/**
	 * Sets the admin.
	 * 
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(Boolean admin) {
		this.admin = admin;
		if (this.admin) {
			setType("Admin/StorageNode");
		}
	}

	/**
	 * Gets the admin port.
	 * 
	 * @return the adminPort
	 */
	public Integer getAdminPort() {
		return adminPort;
	}

	/**
	 * Sets the admin port.
	 * 
	 * @param adminPort
	 *            the adminPort to set
	 */
	public void setAdminPort(Integer adminPort) {
		this.adminPort = adminPort;
	}

	/**
	 * Gets the registry port.
	 * 
	 * @return the registryPort
	 */
	public Integer getRegistryPort() {
		return registryPort;
	}

	/**
	 * Sets the registry port.
	 * 
	 * @param registryPort
	 *            the registryPort to set
	 */
	public void setRegistryPort(Integer registryPort) {
		this.registryPort = registryPort;
	}

	/**
	 * Gets the sn id.
	 * 
	 * @return the snId
	 */
	public Integer getSnId() {
		return snId;
	}

	/**
	 * Sets the sn id.
	 * 
	 * @param snId
	 *            the snId to set
	 */
	public void setSnId(Integer snId) {
		this.snId = snId;
	}

	/**
	 * Gets the memory mb.
	 * 
	 * @return the memoryMb
	 */
	public Long getMemoryMb() {
		return memoryMb;
	}

	/**
	 * Sets the memory mb.
	 * 
	 * @param memoryMb
	 *            the memoryMb to set
	 */
	public void setMemoryMb(Long memoryMb) {
		this.memoryMb = memoryMb;
	}

	/**
	 * Gets the capacity.
	 * 
	 * @return the capacity
	 */
	public Integer getCapacity() {
		return capacity;
	}

	/**
	 * Sets the capacity.
	 * 
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	/**
	 * Gets the cpu num.
	 * 
	 * @return the cpuNum
	 */
	public Integer getCpuNum() {
		return cpuNum;
	}

	/**
	 * Sets the cpu num.
	 * 
	 * @param cpuNum
	 *            the cpuNum to set
	 */
	public void setCpuNum(Integer cpuNum) {
		this.cpuNum = cpuNum;
	}

	/**
	 * Gets the storage dirs.
	 * 
	 * @return the storageDirs
	 */
	public String getStorageDirs() {
		return storageDirs;
	}

	/**
	 * Sets the storage dirs.
	 * 
	 * @param storageDirs
	 *            the storageDirs to set
	 */
	public void setStorageDirs(String storageDirs) {
		this.storageDirs = storageDirs;
	}

	/**
	 * Create remote connection.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 */
	public void connect(ClusterConf clusterConf) {
		if (clusterConf != null && connection == null) {
			connection = SSHUtils.connectToNode(getPublicIp(),
					clusterConf.getUsername(), clusterConf.getPassword(),
					clusterConf.getPrivateKey());
		}
	}

	/**
	 * Check connection status.
	 * 
	 * @return true, if is connected
	 */
	@JsonIgnore
	public boolean isConnected() {
		return connection != null;
	}

	/**
	 * Gets the connection.
	 * 
	 * @return the connection
	 */
	@JsonIgnore
	public SSHExec getConnection() {
		return connection;
	}

	/**
	 * Destructor.
	 * 
	 * @throws Throwable
	 *             the throwable
	 */
	@Override
	protected void finalize() throws Throwable {
		if (connection != null) {
			connection.disconnect();
		}
		super.finalize();
	}
}
