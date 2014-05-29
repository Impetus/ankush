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
package com.impetus.ankush.hadoop.config;

import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.utils.FileNameUtils;

/**
 * The Class HadoopEcoConfiguration.
 *
 * @author mayur
 */
public class HadoopEcoConfiguration extends GenericConfiguration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The hadoop version. */
	private String hadoopVersion = new String();

	/** The hadoop home. */
	private String hadoopHome = new String();

	/** The hadoop conf. */
	private String hadoopConf = new String();

	/**
	 * Instantiates a new hadoop eco configuration.
	 */
	public HadoopEcoConfiguration() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the hadoop version.
	 *
	 * @return the hadoopVersion
	 */
	public String getHadoopVersion() {
		return hadoopVersion;
	}

	/**
	 * Sets the hadoop version.
	 *
	 * @param hadoopVersion the hadoopVersion to set
	 */
	public void setHadoopVersion(String hadoopVersion) {
		this.hadoopVersion = hadoopVersion;
	}

	/**
	 * Gets the hadoop home.
	 *
	 * @return the hadoopHome
	 */
	public String getHadoopHome() {
		return hadoopHome;
	}

	/**
	 * Sets the hadoop home.
	 *
	 * @param hadoopHome the hadoopHome to set
	 */
	public void setHadoopHome(String hadoopHome) {
		this.hadoopHome = FileNameUtils.convertToValidPath(hadoopHome);
	}

	/**
	 * Gets the hadoop conf.
	 *
	 * @return the hadoopConf
	 */
	public String getHadoopConf() {
		return hadoopConf;
	}

	/**
	 * Sets the hadoop conf.
	 *
	 * @param hadoopConf the hadoopConf to set
	 */
	public void setHadoopConf(String hadoopConf) {
		this.hadoopConf = FileNameUtils.convertToValidPath(hadoopConf);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HadoopEcoConfiguration [hadoopVersion=" + hadoopVersion
				+ ", hadoopHome=" + hadoopHome + ", hadoopConf=" + hadoopConf;
	}

}
