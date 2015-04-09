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
package com.impetus.ankush.common.framework.config;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * The Class NodeConf.
 * 
 * @author mayur
 */
public class NodeConf implements Configuration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The public ip. */
	private String publicIp;

	/** The private ip. */
	private String privateIp;

	/** The os. */
	protected String os;

	/** The type. */
	private String type;

	/** The status. */
	private Boolean status = Boolean.FALSE;

	/** The id. */
	private Long id;

	/** The message. */
	private String message;

	/** The state. */
	private String state;

	/** The datacenter. */
	private String datacenter;

	/** The rack. */
	private String rack;

	private String systemHostName = new String("");

	/** The errors. */
	private HashMap<String, String> errors = new HashMap<String, String>();

	private Map conf;

	/**
	 * Instantiates a new node conf.
	 */
	public NodeConf() {
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
	public NodeConf(String publicIp, String privateIp) {
		super();
		this.publicIp = publicIp;
		this.privateIp = privateIp;
	}

	
	public NodeConf(NodeConf nodeConf) {
		super();
		this.id = nodeConf.id;
		this.os = nodeConf.os;
		this.publicIp = nodeConf.publicIp;
		this.privateIp = nodeConf.privateIp;
		this.type = nodeConf.type;
		this.systemHostName = nodeConf.systemHostName;
		this.datacenter = nodeConf.datacenter;
		this.rack = nodeConf.rack;
		this.status = nodeConf.status;
		this.state = nodeConf.state;
		this.message = nodeConf.message;
		this.errors = nodeConf.errors;
		this.conf = nodeConf.conf;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	@JsonIgnore
	public Long getId() {
		return id;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message
	 *            the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the public ip.
	 * 
	 * @return the publicIp
	 */
	public String getPublicIp() {
		return publicIp;
	}

	/**
	 * Sets the public ip.
	 * 
	 * @param publicIp
	 *            the publicIp to set
	 */
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	/**
	 * Gets the private ip.
	 * 
	 * @return the privateIp
	 */
	public String getPrivateIp() {
		return privateIp;
	}

	/**
	 * Sets the private ip.
	 * 
	 * @param privateIp
	 *            the privateIp to set
	 */
	public void setPrivateIp(String privateIp) {
		this.privateIp = privateIp;
	}

	/**
	 * Gets the status.
	 * 
	 * @return the status
	 */
	@JsonIgnore
	public Boolean getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
		if (this.status) {
			state = "deployed";
		} else {
			state = "error";
		}
	}

	/**
	 * Gets the node state.
	 * 
	 * @return the state
	 */
	public String getNodeState() {
		return state;
	}

	/**
	 * Sets the node state.
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setNodeState(String state) {
		this.state = state;
	}

	/**
	 * Gets the os.
	 * 
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Sets the os.
	 * 
	 * @param os
	 *            the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * To get the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Node [publicIp=" + publicIp + ", privateIp=" + privateIp
				+ ", status=" + status + ", getPublicIp()=" + getPublicIp()
				+ ", getPrivateIp()=" + getPrivateIp() + ", getStatus()="
				+ getStatus() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((privateIp == null) ? 0 : privateIp.hashCode());
		result = prime * result
				+ ((publicIp == null) ? 0 : publicIp.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof NodeConf)) {
			return false;
		}
		NodeConf other = (NodeConf) obj;
		if (privateIp == null) {
			if (other.privateIp != null) {
				return false;
			}
		} else if (!privateIp.equals(other.privateIp)) {
			return false;
		}
		if (publicIp == null) {
			if (other.publicIp != null) {
				return false;
			}
		} else if (!publicIp.equals(other.publicIp)) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the errors.
	 * 
	 * @return the error
	 */
	public HashMap<String, String> getErrors() {
		return errors;
	}

	/**
	 * Adds the error.
	 * 
	 * @param key
	 *            the key
	 * @param error
	 *            the error to set
	 */
	public void addError(String key, String error) {
		this.errors.put(key, error);
	}

	/**
	 * Sets the errors.
	 * 
	 * @param errors
	 *            the errors
	 */
	public void setErrors(HashMap<String, String> errors) {
		this.errors = errors;
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
	 * @return the sysHostName
	 */
	public String getSystemHostName() {
		return systemHostName;
	}

	/**
	 * @param sysHostName
	 *            the sysHostName to set
	 */
	public void setSystemHostName(String systemHostName) {
		this.systemHostName = systemHostName;
	}

	/**
	 * @return the conf
	 */
	public Map getConf() {
		return conf;
	}

	/**
	 * @param conf
	 *            the conf to set
	 */
	public void setConf(Map conf) {
		this.conf = conf;
	}

	/**
	 * Method to check whether the role exists in node or not.
	 * 
	 * @param role
	 * @return
	 */
	@JsonIgnore
	public boolean isRoleExists(String role) {
		// if node type is not null.
		if (this.getType() != null) {
			// apply contains check
			return this.type.contains(role);
		}
		// return false.
		return false;
	}
}
