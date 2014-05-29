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
package com.impetus.ankush.common.custom;

import java.util.List;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.FilenameUtils;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * @author mayur
 * 
 */
public class CustomDeployer implements Deployable {

	/** The log. */
	private final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.CUSTOMDEPLOYER, CustomDeployer.class);

	@Override
	public boolean deploy(Configuration config) {
		CustomDeployerConf conf = (CustomDeployerConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Deploying Custom cluster");
		LOG.debug("Deploying Custom cluster with configuration - " + conf);

		List<NodeConf> nodeList = conf.getNodes();
		boolean status = createCustomNodes(conf, nodeList);
		LOG.log("Custom deployment", status);
		return status;
	}
	
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		CustomDeployerConf conf = (CustomDeployerConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Adding nodes of Custom cluster");
		LOG.debug("Adding Custom cluster nodes with configuration - " + conf);

		boolean status = createCustomNodes(conf, nodeList);
		LOG.log("Custom deployer add nodes", status);
		return status;
	}

	private boolean createCustomNodes(final CustomDeployerConf conf,
			List<NodeConf> nodes) {
		final Semaphore semaphore = new Semaphore(nodes.size());
		boolean status = false;
		try {
			// iterate over node list
			for (final NodeConf nodeConf : nodes) {

				semaphore.acquire();
				// create worker node
				Runnable worker = new Runnable() {
					public void run() {
						final String publicIp = nodeConf.getPublicIp();
						LOG.debug("CustomDeployer Worker thread deploying on node - "
								+ publicIp + "...");

						SSHExec connection = null;
						Result res = null;
						boolean statusFlag = true;

						try {
							LOG.debug("Connecting with node");
							// connect to remote node
							connection = SSHUtils.connectToNode(publicIp,
									conf.getUsername(), conf.getPassword(),
									conf.getPrivateKey());

							// if connected
							if (connection != null) {
								// remote file name
								String remoteFileName = "/tmp/"
										+ FilenameUtils.getName(conf
												.getScriptFilePath());
								// upload script
								connection.uploadSingleDataToServer(
										conf.getScriptFilePath(),
										remoteFileName);
								// execute script
								CustomTask runScript = new ExecCommand("sh "
										+ remoteFileName + " "
										+ conf.getScriptArgs());
								res = connection.exec(runScript);
								if (res.rc == 0) {
									LOG.info(nodeConf.getPublicIp(),
											"Custom deployer executed successfully...");
									statusFlag = true;
								} else {
									statusFlag = false;
									LOG.error(nodeConf,
											"Custom script failed...");
								}
							} else {
								statusFlag = false;
								LOG.error(nodeConf, "Authentication failed");
							}
						} catch (Exception e) {
							statusFlag = false;
							LOG.error(e.getMessage(), e);
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
						nodeConf.setStatus(statusFlag);
					}
				};

				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodes.size());
			status = status(nodes);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		return true;
	}

	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		return true;
	}

	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		return true;
	}

	@Override
	public boolean undeploy(Configuration config) {
		return true;
	}

	@Override
	public boolean start(Configuration config) {
		return true;
	}

	@Override
	public boolean stop(Configuration config) {
		return true;
	}

	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            List<NodeConf>
	 * @return boolean
	 */
	private boolean status(List<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = nodeConf.getStatus() && status;
		}
		return status;
	}

}
