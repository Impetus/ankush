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
package com.impetus.ankush2.hadoop.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Akhil
 *
 */
public class CmpConfigMapping {
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CmpConfigMappingXml [installationType=" + installationType
				+ ", vendor=" + vendor + ", version=" + version + ", classMap="
				+ classMap + "]";
	}

	/**
	 * @param installationType
	 * @param vendor
	 * @param version
	 * @param classMap
	 */
	public CmpConfigMapping(String installationType, String vendor,
			String version, Map<String, String> classMap) { 
		this.installationType = installationType;
		this.vendor = vendor;
		this.version = version;
		this.classMap = classMap;
	}

	/**
	 * @param vendor
	 * @param version
	 * @param classMap
	 */
	public CmpConfigMapping(String vendor, String version,
			Map<String, String> classMap) {
		this.vendor = vendor;
		this.version = version;
		this.classMap = classMap;
	}

	String installationType;
	
	String vendor;
	
	String version;
	
	Map<String, String> classMap = new HashMap<String, String>();

	/**
	 * @return the installationType
	 */
	public String getInstallationType() {
		return installationType;
	}

	/**
	 * @param installationType the installationType to set
	 */
	public void setInstallationType(String installationType) {
		this.installationType = installationType;
	}

	/**
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
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
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the classMap
	 */
	public Map<String, String> getClassMap() {
		return classMap;
	}

	/**
	 * @param classMap the classMap to set
	 */
	public void setClassMap(Map<String, String> classMap) {
		this.classMap = classMap;
	}
	
	public void putClassInMap(String classType, String className) {
		this.classMap.put(classType, className);
	}
	
	public String getClassName(String classType) {
		return this.classMap.get(classType);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((installationType == null) ? 0 : installationType.hashCode());
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CmpConfigMapping other = (CmpConfigMapping) obj;
		if (installationType == null) {
			if (other.installationType != null)
				return false;
		} else if (!installationType.equals(other.installationType))
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
}
