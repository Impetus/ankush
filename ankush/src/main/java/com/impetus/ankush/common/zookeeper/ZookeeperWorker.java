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
package com.impetus.ankush.common.zookeeper;

import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class ZookeeperWorker.
 * 
 * @author mayur
 */
public class ZookeeperWorker implements Runnable {

	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.ZOOKEEPER, ZookeeperWorker.class);

	// Configuration manager to save the property file change records.
	/** The conf manager. */
	private ConfigurationManager confManager = new ConfigurationManager();
	
	private static final String FILE_NAME_ZOO_CFG = "zoo.cfg";
	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	private static final String relPath_ZkServerScript = "bin/zkServer.sh";

	/** The conf. */
	private ZookeeperConf conf = null;

	/** The node conf. */
	private NodeConf nodeConf = null;

	/** The semaphore. */
	private Semaphore semaphore = null;

	/** The node id. */
	private int nodeId;

	/**
	 * Instantiates a new zookeeper worker.
	 * 
	 * @param semaphore
	 *            the semaphore
	 * @param config
	 *            the config
	 * @param nodeConf
	 *            the node conf
	 * @param nodeId
	 *            the node id
	 */
	public ZookeeperWorker(Semaphore semaphore, ZookeeperConf config,
			NodeConf nodeConf, int nodeId) {
		this.semaphore = semaphore;
		this.conf = config;
		this.nodeConf = nodeConf;
		this.nodeId = nodeId;
	}

	private boolean configureJMXMonitoring(SSHExec connection) {
		LOG.debug("Configuring JMX Monitoring for Zookeeper started ... ");
		boolean status = true;
		String componentHome = conf.getComponentHome();
		String zkServer_FilePath = componentHome
				+ ZookeeperWorker.relPath_ZkServerScript;
		status = JmxMonitoringUtil.configureJmxPort(
				Constant.Component.ProcessName.QUORUMPEERMAIN, connection,
				zkServer_FilePath, conf.getJmxPort(), conf.getPassword());
		if (!status) {
			LOG.error(this.nodeConf, "Could not update " + zkServer_FilePath
					+ " file.");
			return false;
		}

		status = JmxMonitoringUtil.copyJmxTransJson(connection,
				conf.getUsername(), conf.getPassword(),
				Constant.Component.Name.ZOOKEEPER,
				Constant.Component.ProcessName.QUORUMPEERMAIN,
				conf.getJmxPort(), nodeConf.getPrivateIp());
		if (!status) {
			LOG.error(this.nodeConf,
					"Could not copy JmxTrans JSON file for Zookeeper.");
			return false;
		}
		LOG.debug("Configuring JMX Monitoring for Zookeeper over ... ");
		return true;
	}

	@Override
	public void run() {
		LOG.setLoggerConfig(conf);
		final String publicIp = this.nodeConf.getPublicIp();
		LOG.info(publicIp, "Zookeeper Worker thread deploying on node - "
				+ publicIp + "...");

		SSHExec connection = null;
		Result res = null;
		boolean statusFlag = true;

		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp,
					this.conf.getUsername(), this.conf.getPassword(),
					this.conf.getPrivateKey());

			// if connected
			if (connection != null) {
				LOG.info(publicIp,
						"Create directory - " + this.conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						this.conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(this.nodeConf,
							"Could not create installation directory");
					statusFlag = false;
				} else {
					LOG.info(publicIp, "Get and extract tarball");
					// get and extract tarball
					boolean isSuccessfull = SSHUtils.getAndExtractComponent(
							connection, this.conf, "zookeeper");

					if (!isSuccessfull) {
						LOG.error(this.nodeConf,
								"Could not extract bundle file ");
						statusFlag = false;
					}else{
						// make data directory
						AnkushTask makeDataDir = new MakeDirectory(
								this.conf.getDataDirectory());
						// create zoo.cfg file
						String componentHome = this.conf.getInstallationPath()
								+ "zookeeper-"
								+ this.conf.getComponentVersion();
						// setting componentHome in bean
						conf.setComponentHome(componentHome);

						AnkushTask createZooCfg = new AppendFile(
								this.conf.getZooConfContents(), componentHome
										+ "/conf/" + ZookeeperWorker.FILE_NAME_ZOO_CFG);
						// create myid file
						AnkushTask createMyId = new AppendFile(
								"" + this.nodeId, this.conf.getDataDirectory()
										+ "myid");
	
						LOG.info(publicIp,
								"Creating zookeeper's data directory...");
						if (connection.exec(makeDataDir).isSuccess) {
							LOG.info(publicIp, "Creating zoo.cfg file...");
							
							if (connection.exec(createZooCfg).isSuccess) {
								
								confManager.saveConfiguration(this.conf.getClusterDbId(),
										this.conf.getCurrentUser(), ZookeeperWorker.FILE_NAME_ZOO_CFG, 
										this.nodeConf.getPublicIp(), this.conf.getZooConfContentsAsMap());
								
								LOG.info(publicIp, "Creating myid file...");
								if (!connection.exec(createMyId).isSuccess) {
									statusFlag = false;
									LOG.error(this.nodeConf,
											"Couldn't create myid file");
								}
							} else {
								statusFlag = false;
								LOG.error(this.nodeConf,
										"Couldn't create zoo.cfg file");
							}
						} else {
							statusFlag = false;
							LOG.error(this.nodeConf,
									"Couldn't create zookeeper's data directory");
						}
	
						// Configuring JMX Monitoring using JMX-Trans
						statusFlag = this.configureJMXMonitoring(connection);
					}
					this.nodeConf.setStatus(statusFlag);
					LOG.info(publicIp,
							"Zookeeper worker thread execution over ... ");
				}
			} else {
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			// disconncet to node/machine
			if (semaphore != null) {
				semaphore.release();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

}
