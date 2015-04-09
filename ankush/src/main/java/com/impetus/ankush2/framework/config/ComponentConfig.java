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

import java.util.List;
import java.util.Map;

import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush2.constant.Constant;

public class ComponentConfig implements Configuration {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ComponentConfig [confState=" + confState + ", vendor=" + vendor
				+ ", version=" + version + ", source=" + source
				+ ", sourceType=" + sourceType + ", installPath=" + installPath
				+ ", register=" + register + ", homeDir=" + homeDir
				+ ", state=" + state + ", nodes=" + nodes + ", advanceConf="
				+ advanceConf + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum SourceType {
		UPLOAD, DOWNLOAD, LOCAL, PACKAGE
	}
	
	String confState;
	String vendor;
	String version;
	String source;
	SourceType sourceType;
	String installPath;
	boolean register;
	String homeDir;
	Constant.Component.State state;
	Map<String, Map<String, Object>> nodes;
	Map<String, Object> advanceConf;

	/**
	 * @return the confState
	 */
	public String getConfState() {
		return confState;
	}

	/**
	 * @param confState the confState to set
	 */
	public void setConfState(String confState) {
		this.confState = confState;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the sourceType
	 */
	public SourceType getSourceType() {
		return sourceType;
	}

	/**
	 * @param sourceType
	 *            the sourceType to set
	 */
	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * @return the installPath
	 */
	public String getInstallPath() {
		return installPath;
	}

	/**
	 * @param installPath
	 *            the installPath to set
	 */
	public void setInstallPath(String installPath) {
		this.installPath = FileNameUtils.convertToValidPath(installPath);
	}

	/**
	 * @return the register
	 */
	public boolean isRegister() {
		return register;
	}

	/**
	 * @param register
	 *            the register to set
	 */
	public void setRegister(boolean register) {
		this.register = register;
	}

	/**
	 * @return the homeDir
	 */
	public String getHomeDir() {
		return homeDir;
	}

	/**
	 * @param homeDir
	 *            the homeDir to set
	 */
	public void setHomeDir(String homeDir) {
		this.homeDir = FileNameUtils.convertToValidPath(homeDir);
	}

	/**
	 * @return the state
	 */
	public Constant.Component.State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(Constant.Component.State state) {
		this.state = state;
	}

	/**
	 * @return the nodes
	 */
	public Map<String, Map<String, Object>> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            the nodes to set
	 */
	public void setNodes(Map<String, Map<String, Object>> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the advanceConf
	 */
	public Map<String, Object> getAdvanceConf() {
		return advanceConf;
	}
	
	public Object getAdvanceConfProperty(String key) {
		return this.advanceConf.get(key);
	}
	
	public Object addAdvanceConfProperty(String key, Object value) {
		return this.advanceConf.put(key, value);
	}
	
	public Integer getAdvanceConfIntegerProperty(String key) {
		return (Integer)this.advanceConf.get(key);
	}
	public String getAdvanceConfStringProperty(String key) {
		return (String)this.advanceConf.get(key);
	}
	public Boolean getAdvanceConfBooleanProperty(String key) {
		return (Boolean)this.advanceConf.get(key);
	}
	public List getAdvanceConfListProperty(String key) {
		return (List)this.advanceConf.get(key);
	}

	/**
	 * @param advanceConf
	 *            the advanceConf to set
	 */
	public void setAdvanceConf(Map<String, Object> advanceConf) {
		this.advanceConf = advanceConf;
	}

}
