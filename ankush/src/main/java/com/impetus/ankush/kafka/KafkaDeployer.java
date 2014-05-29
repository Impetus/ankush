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
package com.impetus.ankush.kafka;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class KafkaDeployer.
 * 
 * @author mayur
 */
public class KafkaDeployer implements Deployable {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(Constant.Component.Name.KAFKA,
											KafkaDeployer.class);

	/** The kafka-start-script*/
	private static final String relPath_KAFKA_SERVER_START_SCRIPT = "/bin/kafka-server-start.sh"
			+ Constant.STR_SPACE;
	
	/** The kafka server-file-path*/
	private static final String KAFKA_SERVER_FILE_PATH = "/config/server.properties";
	
	/** The Constant KAFKA. */
	private static final String KAFKA = "kafka_";

	/*
	 * Deploy the cluster
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		
		KafkaConf conf = (KafkaConf) config;

		logger.setLoggerConfig(conf);
		logger.debug("Deploying Kafka cluster with configuration : " + conf);
		logger.info("Deploying Kafka Cluster...");

		boolean status = false;
		int nodeId = 0;
		List<NodeConf> nodeList = conf.getNodes();
		Semaphore semaphore = new Semaphore(nodeList.size());
		try {
			// iterate over node list
			for (NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				++nodeId;
				// create worker node
				KafkaWorker worker = new KafkaWorker(semaphore,conf, nodeConf,nodeId);
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			
			status = status(nodeList);
			//if cluster successfully deployed,then setting lastNodeId in conf.
			if(status){
				conf.setLastNodeId(nodeId);
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.log("Deploying Kafka Cluster", status);

		return status;
	}
	
	/*
	 * Undeploy the cluster
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {

		final KafkaConf conf = (KafkaConf) config;
		logger.setLoggerConfig(conf);
		logger.debug("Undeploying Kafka cluster " + conf.toString());
		logger.info("Undeploying kafka cluster");

		boolean status = false;
		// remove kafka-Directory on all the node after getting the
		// component-home from installation path and version
		// All node-list
		List<NodeConf> nodeConfs = conf.getNodes();
		final Semaphore semaphore = new Semaphore(conf.getNodes().size());
		try {
			// Iterating on all nodes.
			for (Iterator<NodeConf> iterator = nodeConfs.iterator(); iterator
					.hasNext();) {
				final NodeConf nodeConf = iterator.next();
				semaphore.acquire();

				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean nodeStatus = KafkaWorker.removeNode(conf, nodeConf);
						nodeConf.setStatus(nodeStatus);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			// block all threads.
			semaphore.acquire(nodeConfs.size());
			status = status(nodeConfs);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.log("Undeploying Kafka Cluster", status);
		return status;
	}

	/*
	 * start the cluster after it is successfully deployed
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		KafkaConf conf = (KafkaConf) config;
		logger.debug("Starting Kafka cluster with configuration - " + conf.toString());
		logger.info("Starting Kafka cluster...");

		boolean statusFlag = false;
		List<NodeConf> nodeConfs = conf.getNodes();
		for (Iterator<NodeConf> iterator = nodeConfs.iterator(); iterator
				.hasNext();) {
			NodeConf nodeConf = iterator.next();
			SSHExec connection = null;
			try {
				logger.info(nodeConf.getPublicIp(),"Connecting with node...");
				// connect to remote node
				connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());

				// if connected
				if (connection != null) {
					String kafkaHome = conf.getInstallationPath() + KAFKA
							+ conf.getComponentVersion();
					String cmd = kafkaHome + relPath_KAFKA_SERVER_START_SCRIPT 
							+ kafkaHome + KAFKA_SERVER_FILE_PATH;
					AnkushTask runCmd = new RunInBackground(cmd);
					logger.info(nodeConf.getPublicIp()+" : Starting Kafka...");
					logger.debug("Executing command - " + cmd + " on node - "
							+ nodeConf.getPublicIp());
					Result result = connection.exec(runCmd);
					if(!result.isSuccess){
						logger.error("Could not start kafka server on node - " + nodeConf.getPublicIp());
					}
					statusFlag = statusFlag || result.isSuccess;
				}else {
					logger.error("Could not connect to node " + nodeConf.getPublicIp());
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();

				return false;
			} finally {
				// disconncet to node/machine
				if(connection != null){
					connection.disconnect();
				}
				
			}
		}
		logger.log("Starting kafka cluster" , statusFlag);
		return statusFlag;
	}

	/*
	 * Stop the cluster if there is any issue in cluster deployment.
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		KafkaConf conf = (KafkaConf) config;
		logger.setLoggerConfig(conf);
		logger.info("Stopping Kafka cluster...");
		logger.debug("Stopping Kafka cluster with configuration - " + conf);

		// kill Kafka Service on all nodes
		List<NodeConf> nodeConfs = conf.getNodes();
		for (Iterator<NodeConf> iterator = nodeConfs.iterator(); iterator
				.hasNext();) {
			NodeConf nodeConf = iterator.next();
			SSHExec connection = null;
			try {
				// connect to remote node
				connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());

				// if connected
				if (connection != null) {
					// kill Kafka jps process
					AnkushTask killKafka = new KillJPSProcessByName(Constant.Process.KAFKA);
					Result rs = connection.exec(killKafka);
					if(rs.isSuccess){
						logger.info("Stopping Kafka cluster done.");
					}
				} else {
					logger.debug("Could not connet to node " + nodeConf);
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
				return false;
			} finally {
				// disconncet to node/machine
				if(connection != null){
					connection.disconnect();
				}
			}
		}

		return true;
	}

	/*
	 * start Kafka Service on all the nodes which are added after cluster creation
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#startServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		KafkaConf conf = (KafkaConf) config;
		logger.setLoggerConfig(conf);
		SSHExec connection = null;
		for (NodeConf node : nodeList) {
			try {
				// connect to remote node
				connection = SSHUtils.connectToNode(node.getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				// if connected
				if (connection != null) {
					String kafkaHome = conf.getInstallationPath() + KAFKA
							+ conf.getComponentVersion();
					
					String cmd = kafkaHome + relPath_KAFKA_SERVER_START_SCRIPT 
							+ kafkaHome + KAFKA_SERVER_FILE_PATH;
					AnkushTask runKafka = new RunInBackground(cmd);
					logger.info(node.getPublicIp()+" : Starting Kafka...");
					logger.info("Execute command " + cmd + " on node "
							+ node.getPublicIp());
					Result rs = connection.exec(runKafka);
					node.setStatus(rs.isSuccess);
					if (!rs.isSuccess) {
						logger.error(node , "Could not start kafka service on node-"+node.getPublicIp());
						break;
					}
					logger.info(node.getPublicIp()+" : Starting Kafka done.");
				}else{
					logger.error(node, "Could not connect to node - " + node.getPublicIp());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			} finally {
				if(connection != null){
					connection.disconnect();
				}
			}
		}
		return status(nodeList);
	}

	/*
	 * stop Kafka Service on all the nodes which are added after cluster creation
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stopServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeConfs, Configuration config) {
		KafkaConf conf = (KafkaConf) config;
		logger.setLoggerConfig(conf);
		logger.info("Stopping Kafka...");
		SSHExec connection = null;
		for (NodeConf nodeConf : nodeConfs) {
				connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				// if connected
				if (connection != null) {
					AnkushTask kafkaStop = new KillJPSProcessByName(Constant.Process.KAFKA);
					try {
						logger.debug("Killing kafka process on node : "
								+ nodeConf.toString());
						logger.info(nodeConf.getPublicIp()+" : Stopping Kafka...");
						connection.exec(kafkaStop);
					} catch (TaskExecFailException e) {
						logger.debug(e.getMessage());
					}
					connection.disconnect();
					logger.info(nodeConf.getPublicIp()+" : Stopping Kafka done.");
				}else{
					nodeConf.setStatus(false);
					logger.error("Could not connect to node : "+nodeConf.getPublicIp());
				}
		}
		return status(nodeConfs);
	}
	
	/*
	 * deploy Kafka cluster on the new nodes which have come for addition in the cluster.
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List,
	 * com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		
		KafkaConf conf = (KafkaConf) config;
		logger.setLoggerConfig(conf);
		logger.info("Adding nodes...");
		boolean status = false;
		//get the recent lastNodeId set as brokerId
		int nodeId = conf.getLastNodeId();
		Semaphore semaphore = new Semaphore(nodeList.size());
		try {
			// iterate over node list
			for (NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				++nodeId;
				// create worker node
				KafkaWorker worker = new KafkaWorker(semaphore,conf, nodeConf,nodeId);
				AppStoreWrapper.getExecutor().execute(worker);
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
			//if nodes are successfully added,then setting lastNodeId in conf.
			if(status){
				conf.setLastNodeId(nodeId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.log("Adding nodes", status);
		return status;
	}

	/**
	 * undeploy the nodes 
	 * called from removeNodes method
	 * @param conf
	 * @param nodeConfs
	 * @return
	 * @throws InterruptedException
	 */
	private boolean undeployNodes(final KafkaConf conf, List<NodeConf> nodeConfs)
			throws InterruptedException {
		final Semaphore semaphore = new Semaphore(nodeConfs.size());

		for (final NodeConf node : nodeConfs) {
			semaphore.acquire();
			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					node.setStatus(KafkaWorker.removeNode(conf, node));
					if (semaphore != null) {
						semaphore.release();
					}
				}
			});
		}
		// block all threads.
		semaphore.acquire(nodeConfs.size());
		return status(nodeConfs);
	}

	/*
	 * remove the nodes after cluster creation.
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.
	 * List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		KafkaConf conf = (KafkaConf) config;
		logger.setLoggerConfig(conf);
		logger.info("Removing nodes...");
		boolean status = false;
		try {
			status = undeployNodes(conf, nodeList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.log("Removing nodes", status);
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
