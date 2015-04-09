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
package com.impetus.ankush.common.framework.config;

import com.impetus.ankush.common.framework.Deployable;

/**
 * The Class DeployableConf.
 */
public class DeployableConf {

	/** The deployer. */
	private Deployable deployer;

	/** The configuration. */
	private Configuration configuration;

	/** Component Name **/
	private String componentName;

	/**
	 * Gets the deployer.
	 * 
	 * @return the deployer
	 */
	public Deployable getDeployer() {
		return deployer;
	}

	/**
	 * Gets the configuration.
	 * 
	 * @return the configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * Instantiates a new deployable conf.
	 * 
	 * @param deployer
	 *            the deployer
	 * @param configuration
	 *            the configuration
	 */
	public DeployableConf(Deployable deployer, Configuration configuration) {
		super();
		this.deployer = deployer;
		this.configuration = configuration;
	}

	/**
	 * Instantiates a new deployable conf.
	 * 
	 * @param deployer
	 *            the deployer
	 * @param configuration
	 *            the configuration
	 */
	public DeployableConf(Deployable deployer, Configuration configuration,
			String componentName) {
		super();
		this.deployer = deployer;
		this.configuration = configuration;
		this.componentName = componentName;
	}
	
//	public DeployableConf(Deployable deployer, Configuration configuration,
//			String componentName, boolean deployComponent) {
//		super();
//		this.deployer = deployer;
//		this.configuration = configuration;
//		this.componentName = componentName;
//		this.deployComponent = deployComponent;
//	}
}
