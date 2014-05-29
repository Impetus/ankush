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

import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.common.utils.FileNameUtils;

/**
 * The Class JavaConf.
 * 
 * @author hokam
 */
public class JavaConf implements Configuration {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The install. */
	private Boolean install;

	/** The java bundle. */
	private String javaBundle;

	/** The java home path. */
	private String javaHomePath;

	/**
	 * Checks if is install.
	 * 
	 * @return the install
	 */
	public Boolean isInstall() {
		return install;
	}

	/**
	 * Sets the install.
	 * 
	 * @param install
	 *            the install to set
	 */
	public void setInstall(Boolean install) {
		this.install = install;
	}

	/**
	 * Gets the java bundle.
	 * 
	 * @return the javaBundle
	 */
	public String getJavaBundle() {
		return javaBundle;
	}

	/**
	 * Sets the java bundle.
	 * 
	 * @param javaBundle
	 *            the javaBundle to set
	 */
	public void setJavaBundle(String javaBundle) {
		this.javaBundle = javaBundle;
	}

	/**
	 * Gets the java home path.
	 * 
	 * @return the javaHomePath
	 */
	public String getJavaHomePath() {
		return javaHomePath;
	}

	/**
	 * Sets the java home path.
	 * 
	 * @param javaHomePath
	 *            the javaHomePath to set
	 */
	public void setJavaHomePath(String javaHomePath) {
		if (javaHomePath != null && !javaHomePath.isEmpty()) {
			javaHomePath = FileNameUtils.convertToValidPath(javaHomePath);
		}
		this.javaHomePath = javaHomePath;
	}

	/**
	 * Gets the java bin path.
	 * 
	 * @return the java Binary file path.
	 */
	@JsonIgnore
	public String getJavaBinPath() {
		if (javaHomePath != null) {
			return getJavaHomePath() + "bin/java";
		} else {
			return null;
		}
	}
}
