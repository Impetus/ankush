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

import java.util.HashSet;

import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;

/**
 * @author Akhil
 * 
 */
public class CmpConfigMappingSet extends HashSet<CmpConfigMapping> {

	private CmpConfigMapping get(String installationType, String vendor,
			String version) {
		for (Object configXmlObject : this.toArray()) {
			CmpConfigMapping configXml = (CmpConfigMapping) configXmlObject;
			if (configXml.getInstallationType().equalsIgnoreCase(
					installationType)
					&& configXml.getVendor().equalsIgnoreCase(vendor)
					&& version.startsWith(configXml.getVersion())) {
				return configXml;
			}
		}
		return null;
	}

	private CmpConfigMapping get(String installationType, String vendor) {
		for (Object configXmlObject : this.toArray()) {
			CmpConfigMapping configXml = (CmpConfigMapping) configXmlObject;
			if (configXml.getInstallationType().equalsIgnoreCase(
					installationType)
					&& configXml.getVendor().equalsIgnoreCase(vendor)) {
				return configXml;
			}
		}
		return null;
	}

	public CmpConfigMapping get(ComponentConfig compConfig,
			boolean skipVersion) {
		Object installationType = compConfig
				.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.INSTALLATION_TYPE);
		if (installationType == null) {
			installationType = new String("Bundle");
		}
		if(skipVersion) {
			return this.get((String) installationType, compConfig.getVendor());
		} else {
			return this.get((String) installationType, compConfig.getVendor(),
					compConfig.getVersion());	
		}
		
	}
}
