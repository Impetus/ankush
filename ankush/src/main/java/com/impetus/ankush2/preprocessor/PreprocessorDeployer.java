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
package com.impetus.ankush2.preprocessor;

import java.util.concurrent.Semaphore;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

/**
 * The Class PreprocessorDeployer.
 */
public class PreprocessorDeployer extends AbstractDeployer {

	/** The logger. */
	AnkushLogger logger = new AnkushLogger(PreprocessorDeployer.class);

	/**
	 * Sets the cluster and logger.
	 * 
	 * @param clusterConfig
	 *            the new cluster and logger
	 */
	private void setClusterAndLogger(ClusterConfig clusterConfig) {
		// this.clusterConfig = clusterConfig;
		logger.setCluster(clusterConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush2.framework.AbstractDeployer#deploy(com.impetus.ankush2
	 * .framework.config.ClusterConfig)
	 */
	@Override
	public boolean deploy(ClusterConfig clusterConfig) {
		this.setClusterAndLogger(clusterConfig);
		boolean status = true;
		try {
			// install java on behalf of register flag
			if (!clusterConfig.getJavaConf().isRegister()) {
				final Semaphore semaphore = new Semaphore(clusterConfig
						.getNodes().size());
				for (final NodeConfig nodeConfig : clusterConfig.getNodes()
						.values()) {
					// acuiring the semaphore
					semaphore.acquire();
					final JavaDeployer javaDeployer = new JavaDeployer(
							clusterConfig);
					// starting a thread to start pre-processing on node.
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							try {

								javaDeployer.installJava(nodeConfig.getHost());
							} catch (Exception e) {
								logger.error(
										"Exception in installing Java."
												+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
										Constant.Component.Name.PREPROCESSOR,
										nodeConfig.getHost());

							} finally {
								if (semaphore != null) {
									semaphore.release();
								}
							}
						}
					});
				}
				// waiting for all semaphores to finish the installation.
				semaphore.acquire(clusterConfig.getNodes().size());
				return AnkushUtils.getStatus(clusterConfig.getNodes());

			}
		} catch (Exception e) {
			logger.error("There is some exception in PreprocessorDeployer."
					+ Constant.Strings.ExceptionsMessage.VIEW_SERVER_LOGS,
					Constant.Component.Name.PREPROCESSOR);

		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.framework.AbstractDeployer#getComponentName()
	 */
	@Override
	public String getComponentName() {
		return Constant.Component.Name.PREPROCESSOR;
	}
}
