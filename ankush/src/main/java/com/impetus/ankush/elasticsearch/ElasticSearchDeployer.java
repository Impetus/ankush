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
package com.impetus.ankush.elasticsearch;

import java.util.List;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.DeleteLineFromFile;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class ElasticSearchDeployer.
 *
 * @author mayur
 */
public class ElasticSearchDeployer implements Deployable {

	/** The log. */
	private final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.ELASTICSEARCH, ElasticSearchDeployer.class);

	/** The Constant ES_BIN. */
	public static final String ES_BIN = "bin/elasticsearch";

	/** The Constant ES_HEAP_SIZE. */
	public static final String ES_HEAP_SIZE = "ES_HEAP_SIZE";

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Deploying ElasticSearch cluster");
		LOG.debug("Deploying ElasticSearch cluster with configuration - "
				+ conf);

		List<NodeConf> nodeList = conf.getNodes();
		boolean status = createESNodes(conf, nodeList);
		LOG.log("ElasticSearch deployment", status);
		return status;
	}

	/**
	 * Creates the es nodes.
	 *
	 * @param conf the conf
	 * @param nodeList the node list
	 * @return true, if successful
	 */
	private boolean createESNodes(ElasticSearchConf conf,
			List<NodeConf> nodeList) {
		Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			// iterate over node list
			for (NodeConf nodeConf : nodeList) {

				semaphore.acquire();
				// create worker node
				ElasticSearchWorker worker = new ElasticSearchWorker(semaphore,
						conf, nodeConf);
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		final ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Undeploying ElasticSearch cluster");
		LOG.debug("Undeploying ElasticSearch cluster with configuration - "
				+ conf);

		List<NodeConf> nodeList = conf.getNodes();
		boolean status = removeESOnNodes(conf, nodeList);
		LOG.log("ElasticSearch shutdown ", status);

		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		final ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Starting ElasticSearch cluster");
		LOG.debug("Starting ElasticSearch cluster with configuration - " + conf);

		List<NodeConf> nodeList = conf.getNodes();
		boolean status = startESOnNodes(conf, nodeList);
		LOG.log("ElasticSearch startup ", status);

		return status;
	}

	/**
	 * Start es on nodes.
	 *
	 * @param conf the conf
	 * @param nodeList the node list
	 * @return true, if successful
	 */
	private boolean startESOnNodes(final ElasticSearchConf conf,
			List<NodeConf> nodeList) {
		LOG.setLoggerConfig(conf);
		final Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			// iterate over node list
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				Runnable worker = new Runnable() {

					@Override
					public void run() {
						final String publicIp = nodeConf.getPublicIp();
						LOG.debug("ElasticSearch Worker thread starting on node - "
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
								// ES Start command
								CustomTask startES = new ExecCommand(
										conf.getComponentHome()
												+ ElasticSearchDeployer.ES_BIN);
								LOG.info(publicIp, "Starting ElasticSearch...");
								res = connection.exec(startES);
								if (res.rc != 0) {
									LOG.error(nodeConf,res.error_msg);
									LOG.error(nodeConf,
											"Could not start elasticsearch on "
													+ nodeConf.getPublicIp());
									statusFlag = false;
								} else {
									LOG.info(publicIp,
											"Starting ElasticSearch done.");
								}
							} else {
								statusFlag = false;
								LOG.error(nodeConf, "Authentication failed");
							}
						} catch (Exception e) {
							statusFlag = false;
							LOG.error(e.getMessage(), e);
						} finally {
							nodeConf.setStatus(statusFlag);
							if (semaphore != null) {
								semaphore.release();
							}
							if (connection != null) {
								connection.disconnect();
							}
						}
					}
				};
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Stop es on nodes.
	 *
	 * @param conf the conf
	 * @param nodeList the node list
	 * @return true, if successful
	 */
	private boolean stopESOnNodes(final ElasticSearchConf conf,
			List<NodeConf> nodeList) {
		final Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			// iterate over node list
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				Runnable worker = new Runnable() {

					@Override
					public void run() {
						final String publicIp = nodeConf.getPublicIp();
						LOG.info("ElasticSearch stopping on node - "
								+ publicIp + "...");
						LOG.debug("ElasticSearch Worker thread stopping on node - "
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
								// ES Stop command
								AnkushTask stopES = new KillJPSProcessByName(
										"ElasticSearch");
								res = connection.exec(stopES);
								if (res.rc != 0) {
									LOG.error(nodeConf,
											"Could not stop elasticsearch on "
													+ nodeConf.getPublicIp());
									statusFlag = false;
								}
							} else {
								statusFlag = false;
								LOG.error(nodeConf, "Authentication failed");
							}
						} catch (Exception e) {
							statusFlag = false;
							LOG.error(e.getMessage(), e);
						} finally {
							nodeConf.setStatus(statusFlag);
							if (semaphore != null) {
								semaphore.release();
							}
							if (connection != null) {
								connection.disconnect();
							}
						}
					}
				};
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Removes the es on nodes.
	 *
	 * @param conf the conf
	 * @param nodeList the node list
	 * @return true, if successful
	 */
	private boolean removeESOnNodes(final ElasticSearchConf conf,
			List<NodeConf> nodeList) {
		final Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			// iterate over node list
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				Runnable worker = new Runnable() {

					@Override
					public void run() {
						final String publicIp = nodeConf.getPublicIp();
						LOG.debug("ElasticSearch Worker thread removing on node - "
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
								// ES Stop command
								AnkushTask removeES = new Remove(
										conf.getComponentHome());
								LOG.info(publicIp,
										"Removing ElasticSearch from component homePath...");
								res = connection.exec(removeES);
								AnkushTask removeData = new Remove(
										conf.getDataPath());
								AnkushTask deleteHeapSizeEntry = new DeleteLineFromFile(
										"ES_HEAP_SIZE", Constant.ETCENV_FILE,
										conf.getPassword(), false);
								if (res.rc != 0) {
									LOG.error(nodeConf,
											"Could not remove elasticsearch on "
													+ nodeConf.getPublicIp());
									statusFlag = false;
								} else {
									LOG.info(publicIp,
											"Removing ElasticSearch from component homePath done.");
									LOG.info(publicIp,
											"Removing ElasticSearch data from dataPath...");
									res = connection.exec(removeData);
									if (res.rc != 0) {
										LOG.error(
												nodeConf,
												"Could not remove data on "
														+ nodeConf
																.getPublicIp());
										statusFlag = false;
									} else {
										LOG.info(publicIp,
												"Removing ElasticSearch data from dataPath done.");
										res = connection
												.exec(deleteHeapSizeEntry);
										if (res.rc != 0) {
											LOG.info(publicIp,
													"Removing ES_HEAP_SIZE entry from Environment Variables...");
											LOG.error(
													nodeConf,
													"Could not remove "
															+ ElasticSearchDeployer.ES_HEAP_SIZE
															+ " from "
															+ Constant.ETCENV_FILE);
											statusFlag = false;
										} else {
											LOG.info(publicIp,
													"Removing ES_HEAP_SIZE entry from Environment Variables done.");
										}
									}
								}
							} else {
								statusFlag = false;
								LOG.error(nodeConf, "Authentication failed");
							}
						} catch (Exception e) {
							statusFlag = false;
							LOG.error(e.getMessage(), e);
						} finally {
							nodeConf.setStatus(statusFlag);
							if (semaphore != null) {
								semaphore.release();
							}
							if (connection != null) {
								connection.disconnect();
							}
						}
					}
				};
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		final ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Stopping ElasticSearch cluster");
		LOG.debug("Stopping ElasticSearch cluster with configuration - " + conf);

		List<NodeConf> nodeList = conf.getNodes();
		boolean status = stopESOnNodes(conf, nodeList);
		LOG.log("ElasticSearch shutdown ", status);

		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Adding nodes...");
		boolean status = false;
		status = createESNodes(conf, nodeList);
		LOG.log("Adding nodes", status);
		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Removing nodes...");
		boolean status = false;
		status = removeESOnNodes(conf, nodeList);
		LOG.log("Removing nodes", status);
		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#startServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Starting ElasticSearch...");
		boolean status = startESOnNodes(conf, nodeList);
		LOG.log("Starting ElasticSearch", status);
		return status;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stopServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		ElasticSearchConf conf = (ElasticSearchConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Stopping ElasticSearch...");
		boolean status = stopESOnNodes(conf, nodeList);
		LOG.log("Stopping ElasticSearch", status);
		return status;
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
