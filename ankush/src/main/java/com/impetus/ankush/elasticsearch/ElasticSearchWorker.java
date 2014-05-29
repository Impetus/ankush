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
package com.impetus.ankush.elasticsearch;

import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.common.agent.AgentDeployer;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.SetEnvVariable;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class ElasticSearchWorker.
 * 
 * @author mayur
 * 
 */
public class ElasticSearchWorker implements Runnable {

	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.ELASTICSEARCH, ElasticSearchWorker.class);

	/** The conf. */
	private ElasticSearchConf conf = null;

	/** The node conf. */
	private NodeConf nodeConf = null;

	/** The semaphore. */
	private Semaphore semaphore = null;

	/** The conf manager. */
	private static ConfigurationManager confManager = new ConfigurationManager();

	/** The Constant ELASTICSEARCH_YAML_PATH. */
	public static final String ELASTICSEARCH_YAML_PATH = "/config/elasticsearch.yml";

	/**
	 * Instantiates a new elasticsearch worker.
	 * 
	 * @param semaphore
	 *            the semaphore
	 * @param config
	 *            the config
	 * @param nodeConf
	 *            the node conf
	 */
	public ElasticSearchWorker(Semaphore semaphore, ElasticSearchConf config,
			NodeConf nodeConf) {
		this.semaphore = semaphore;
		this.conf = config;
		this.nodeConf = nodeConf;
	}

	@Override
	public void run() {
		LOG.setLoggerConfig(conf);
		final String publicIp = this.nodeConf.getPublicIp();
		LOG.debug("ElasticSearch Worker thread deploying on node - " + publicIp
				+ "...");

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
				LOG.debug("Create directory - "
						+ this.conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						this.conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(this.nodeConf,
							"Could not create installation directory");
					statusFlag = false;
				} else {
					LOG.debug("Get and extract tarball");
					// get and extract tarball
					boolean isSuccessfull = SSHUtils.getAndExtractComponent(
							connection, this.conf, "elasticsearch");

					// if component exists at destination
					if (isSuccessfull) {
						// setting componentHome in bean
						String componentHome = this.conf.getInstallationPath()
								+ "elasticsearch-"
								+ this.conf.getComponentVersion();
						conf.setComponentHome(componentHome);

						// make data directory
						AnkushTask makeDataPathDir = new MakeDirectory(
								this.conf.getDataPath());
						// configure elasticsearch.yaml
						AnkushTask esYamlConfig = new AppendFile(
								conf.getYamlContents(this.nodeConf, this.conf),
								conf.getComponentHome()
										+ ELASTICSEARCH_YAML_PATH);
						// set ES_HEAP_SIZE
						AnkushTask setHeapSize = new SetEnvVariable(
								conf.getPassword(),
								ElasticSearchDeployer.ES_HEAP_SIZE,
								conf.getHeapSize(), Constant.ETCENV_FILE);

						Result result = null;
						LOG.info(publicIp, "Creating data path directory...");
						result = connection.exec(makeDataPathDir);
						if (result.rc != 0) {
							LOG.error(this.nodeConf,
									"Could not create data path directory");
							statusFlag = false;
						} else {
							LOG.info(publicIp,
									"Creating data path directory done.");
							LOG.info(publicIp,
									"Configuring elasticsearch.yml file...");
							result = connection.exec(esYamlConfig);
							if (result.rc != 0) {
								LOG.error(this.nodeConf,
										"Could not edit elasticsearch.yml");
								statusFlag = false;
							} else {
								LOG.info(publicIp,
										"Configuring elasticsearch.yml file done.");
								LOG.info(publicIp,
										"Adding ES_HEAP_SIZE to environment variable...");
								result = connection.exec(setHeapSize);
								if (result.rc != 0) {
									LOG.error(
											this.nodeConf,
											"Could not set "
													+ ElasticSearchDeployer.ES_HEAP_SIZE
													+ " environment variable");
									statusFlag = false;
								} else {
									LOG.info(publicIp,
											"Adding ES_HEAP_SIZE to environment variable done.");
								}
							}
							LOG.debug("ElasticSearch worker thread execution over ... ");
						}
					} else {
						LOG.error(this.nodeConf,
								"Could not extract bundle file ");
						statusFlag = false;
					}
				}
			} else {
				statusFlag = false;
				LOG.error(nodeConf, "Authentication failed");
			}
			if (statusFlag) {
				if (this.nodeConf.getPublicIp().equals(
						this.conf.getClusterConf().getGangliaMaster()
								.getPublicIp())) {
					updateAgentPropFile(connection, conf);
					SSHUtils.addAgentConfig(connection,
							Constant.TaskableClass.ElasticSearch_MONITOR);
				}
				// add audit configurations.
				confManager.saveConfiguration(conf.getClusterDbId(),
						conf.getCurrentUser(), "elasticsearch.yml", publicIp,
						conf.getConfigurationMap(this.nodeConf, this.conf));
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
	 * Update agent prop file.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	public static boolean updateAgentPropFile(SSHExec connection,
			ElasticSearchConf conf) {
		StringBuffer agentConfProp = new StringBuffer();
		int updateClusterDataTime = 30;
		String agentFile = Constant.Agent.AGENT_PROPERTY_FILE_PATH;
		agentConfProp.append("HTTP_PORT=").append(conf.getHttpPort())
				.append(Constant.LINE_SEPERATOR);
		agentConfProp.append("UPDATE_CLUSTER_DATA_TIME=")
				.append(updateClusterDataTime).append(Constant.LINE_SEPERATOR);
		CustomTask task = new AppendFile(agentConfProp.toString(), agentFile);
		try {
			connection.exec(task);
		} catch (TaskExecFailException e) {
			return false;
		}
		return true;
	}

}
