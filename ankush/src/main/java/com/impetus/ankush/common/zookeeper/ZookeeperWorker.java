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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.agent.ComponentService;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JmxMonitoringUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush2.constant.Constant.Agent;
import com.impetus.ankush2.constant.Constant.Component;

/**
 * The Class ZookeeperWorker.
 * 
 * @author mayur
 */
public class ZookeeperWorker implements Runnable {

	/** The log. */
	private static AnkushLogger LOG = new AnkushLogger(
			Component.Name.ZOOKEEPER, ZookeeperWorker.class);

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

	private static boolean configureJMXMonitoring(SSHExec connection,
			ZookeeperConf conf, NodeConf nodeConf) {

		LOG.debug("Configuring JMX Monitoring for Zookeeper started ... ");
		boolean status = true;
		String componentHome = conf.getComponentHome();
		String zkServer_FilePath = componentHome
				+ ZookeeperWorker.relPath_ZkServerScript;
//		status = JmxMonitoringUtil.configureJmxPort(
//				Constant.Component.ProcessName.QUORUMPEERMAIN, connection,
//				zkServer_FilePath, conf.getJmxPort(), conf.getPassword());
		if (!status) {
			LOG.error(nodeConf, "Could not update " + zkServer_FilePath
					+ " file.");
			LOG.error(nodeConf,
					"Couldn't configure JMX monitoring for Zookeeper...");
			return false;
		}

		// status = JmxMonitoringUtil.copyJmxTransJson(connection,
		// conf.getUsername(), conf.getPassword(),
		// Constant.Component.Name.ZOOKEEPER,
		// Constant.Component.ProcessName.QUORUMPEERMAIN,
		// conf.getJmxPort(), nodeConf.getPrivateIp());
		// if (!status) {
		// LOG.error(nodeConf,
		// "Could not copy JmxTrans JSON file for Zookeeper.");
		// return false;
		// }
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
					} else {
						// make data directory
						AnkushTask makeDataDir = new MakeDirectory(
								this.conf.getDataDirectory());
						// create zoo.cfg file
						String componentHome = this.conf.getInstallationPath()
								+ "zookeeper-"
								+ this.conf.getComponentVersion();
						// setting componentHome in bean
						conf.setComponentHome(componentHome);

						AnkushTask createZooCfg = new AppendFileUsingEcho(
								this.conf.getZooConfContents(), componentHome
										+ "/conf/zoo.cfg");
						// create myid file
						AnkushTask createMyId = new AppendFileUsingEcho(
								"" + this.nodeId, this.conf.getDataDirectory()
										+ "myid");

						LOG.info(publicIp,
								"Creating zookeeper's data directory...");
						if (connection.exec(makeDataDir).isSuccess) {
							LOG.info(publicIp, "Creating zoo.cfg file...");
							if (connection.exec(createZooCfg).isSuccess) {
								LOG.info(publicIp, "Creating myid file...");
								if (connection.exec(createMyId).isSuccess) {
									LOG.info(publicIp,
											"Configuring JMX monitoring for Zookeeper... ");
									if (ZookeeperWorker.configureJMXMonitoring(
											connection, conf, nodeConf)) {
										statusFlag = ZookeeperWorker.configure(
												connection, conf, nodeConf);
									}else{
										statusFlag = false;
									}
								} else {
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
					}
					LOG.info(publicIp,
							"Zookeeper worker thread execution over ... ");
				}
			} else {
				LOG.error(nodeConf, "Authentication failed");
				statusFlag = false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			this.nodeConf.setStatus(false);
		} finally {
			this.nodeConf.setStatus(statusFlag);
			// disconncet to node/machine
			if (semaphore != null) {
				semaphore.release();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Configure.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return the boolean
	 */
	public static boolean register(ZookeeperConf conf, SSHExec connection,
			NodeConf nodeConf) {
		LOG.setLoggerConfig(conf);
		boolean statusFlag = false;
		try {
			LOG.info(nodeConf.getPublicIp(), "Connecting with node");
			// if connected
			if (connection != null) {
				statusFlag = configure(connection, conf, nodeConf);
			} else {
				LOG.error("Could not connect to node " + nodeConf);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return statusFlag;
	}

	/**
	 * Configure.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	private static boolean configure(SSHExec connection, ZookeeperConf conf,
			NodeConf nodeConf) {
		LOG.setLoggerConfig(conf);
		boolean statusFlag = false;
		LOG.info(nodeConf.getPublicIp(),
				"Creating zookeeper service configuration in agent...");
		// Create ZOOKEEPER Service XML configuration in agent.
		// Component service list
		List<ComponentService> services = new ArrayList<ComponentService>();
		// adding ZOOKEEPER service entry.
		services.add(new ComponentService(
				Constant.Component.ProcessName.QUORUMPEERMAIN,
				Constant.Role.ZOOKEEPER, Agent.ServiceType.JPS));
		// Creating ZOOKEEPER service XML.
		statusFlag = AgentUtils.createServiceXML(connection, services,
				Component.Name.ZOOKEEPER);
		// if failed
		if (!statusFlag) {
			LOG.error(nodeConf,
					"Could not create zookeeper service configuration in agent.");
			return statusFlag;
		}
		LOG.info(nodeConf.getPublicIp(),
				"Copying JmxTrans JSON file to Zookeeper...");
		// Configuring JMX Monitoring using JMX-Trans
		statusFlag = JmxMonitoringUtil.copyJmxTransJson(connection,
				conf.getUsername(), conf.getPassword(),
				Component.Name.ZOOKEEPER,
				Constant.Component.ProcessName.QUORUMPEERMAIN,
				conf.getJmxPort(), nodeConf.getPrivateIp());
		if (!statusFlag) {
			LOG.error(nodeConf,
					"Could not copy JmxTrans JSON file for Zookeeper.");
			return statusFlag;
		}
		LOG.debug("Configuring JMX Monitoring for Zookeeper over ... ");
		return true;
	}
}
