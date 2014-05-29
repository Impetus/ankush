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
package com.impetus.ankush.storm;

import java.util.ArrayList;
import java.util.HashSet;
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
import com.impetus.ankush.common.scripting.impl.RunInBackgroundUninterrupted;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class StormDeployer.
 * 
 * @author mayur
 */
public class StormDeployer implements Deployable {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			Constant.Component.Name.STORM, StormDeployer.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		final StormConf conf = (StormConf) config;

		logger.setLoggerConfig(conf);
		logger.debug("Deploying Storm cluster with configuration : " + conf);
		logger.info("Deploying Storm Cluster...");

		boolean status = false;
		try {
			HashSet<NodeConf> nodeConfs = new HashSet<NodeConf>();
			nodeConfs.addAll(conf.getSupervisors());
			nodeConfs.add(conf.getNimbus());

			List<NodeConf> nodeConfsList = new ArrayList<NodeConf>(nodeConfs);
			status = deployNodes(conf, nodeConfsList, false);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.log("Deploying Storm Cluster", status);

		return status;
	}

	/**
	 * @param conf
	 * @param nodeConfs
	 * @throws InterruptedException
	 */
	private boolean deployNodes(final StormConf conf, List<NodeConf> nodeConfs,
			final boolean addingNodes) throws InterruptedException {
		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		for (final NodeConf node : nodeConfs) {
			semaphore.acquire();
			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					StormHelper stormHeloer = new StormHelper();
					node.setStatus(stormHeloer.createNode(conf, node,
							addingNodes));
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
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		final StormConf conf = (StormConf) config;

		logger.setLoggerConfig(conf);
		logger.debug("Undeploying Storm Cluster" + conf.toString());
		logger.info("Undeploying Storm Cluster");

		boolean status = false;
		try {
			List<NodeConf> nodeConfs = new ArrayList<NodeConf>();
			nodeConfs.addAll(conf.getSupervisors());
			nodeConfs.add(conf.getNimbus());
			status = undeployNodes(conf, nodeConfs);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		logger.log("Undeploying Storm Cluster", status);

		return status;
	}

	/**
	 * @param conf
	 * @param nodeConfs
	 * @return
	 * @throws InterruptedException
	 */
	private boolean undeployNodes(final StormConf conf, List<NodeConf> nodeConfs)
			throws InterruptedException {
		final Semaphore semaphore = new Semaphore(nodeConfs.size());

		for (final NodeConf node : nodeConfs) {
			semaphore.acquire();
			AppStoreWrapper.getExecutor().execute(new Runnable() {

				@Override
				public void run() {
					StormHelper stormHeloer = new StormHelper();
					node.setStatus(stormHeloer.removeNode(conf, node));
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
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		StormConf conf = (StormConf) config;
		logger.debug("Starting Storm Cluster:" + conf.toString());
		logger.info("Starting Storm Cluster");

		String componentHome = conf.getInstallationPath() + "storm-"
				+ conf.getComponentVersion();

		String stormPath = componentHome + "/bin/storm ";
		String nimbusStartCmd = stormPath + "nimbus";
		String nimbusStartUICmd = stormPath + "ui";

		// connect to nimbus and start nimbus process/service
		SSHExec connection = null;

		Result res = null;
		try {
			logger.debug("Connecting with node");
			// step 1 : connect to nimbus node
			connection = SSHUtils.connectToNode(conf.getNimbus().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			// if connected
			if (connection != null) {
				AnkushTask nimbusStart = new RunInBackgroundUninterrupted(
						nimbusStartCmd);
				res = connection.exec(nimbusStart);
				if (!res.isSuccess) {
					logger.error(conf.getNimbus().getPublicIp(),
							"Could not start nimbus service");
					return false;
				}
			} else {
				logger.error("Could not connect to node " + conf.getNimbus());
			}

			if (!startSupervisors(conf.getSupervisors(), conf)) {
				logger.log("Starting Storm Cluster", false);
				return false;
			}

			// step 3 : start UI service on nimbus node
			AnkushTask nimbusUIStart = new RunInBackgroundUninterrupted(
					nimbusStartUICmd);
			if (connection != null) {
				res = connection.exec(nimbusUIStart);
				if (!res.isSuccess) {
					logger.error(conf.getNimbus().getPublicIp(),
							"Could not start UI service on nimbus");
				}

				logger.log("Starting Storm Cluster", res.isSuccess);
				return res.isSuccess;
			} else {
				logger.debug("Could not connect to nimbus");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			connection.disconnect();
		}
		logger.log("Starting Storm Cluster", false);
		return false;
	}

	/**
	 * @param conf
	 * @param supervisorStartCmd
	 * @throws TaskExecFailException
	 */
	private boolean startSupervisors(List<NodeConf> nodeConfs, StormConf conf)
			throws TaskExecFailException {
		SSHExec connection;
		String componentHome = conf.getInstallationPath() + "storm-"
				+ conf.getComponentVersion();
		String stormPath = componentHome + "/bin/storm ";
		String supervisorStartCmd = stormPath + "supervisor";

		// connect to supervisors and start service
		for (NodeConf nodeConf : nodeConfs) {
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			// if connected
			if (connection != null) {
				AnkushTask supervisorStart = new RunInBackgroundUninterrupted(
						supervisorStartCmd);
				Result res = connection.exec(supervisorStart);

				nodeConf.setStatus(res.isSuccess);
				if (!res.isSuccess) {
					logger.error(nodeConf,
							"Could not start supervisor service on node ");
					break;
				}
				connection.disconnect();
			} else {
				logger.error(nodeConf, "Could not connect to node ");
			}
		}
		return status(nodeConfs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		StormConf conf = (StormConf) config;
		logger.debug("Stopping Storm Cluster:" + conf.toString());

		// step 1 : kill services on nimbus node
		SSHExec connection = null;
		try {
			logger.debug("Connecting with nimbus node");
			// connect to remote node
			connection = SSHUtils.connectToNode(conf.getNimbus().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				AnkushTask stopNimbus = new KillJPSProcessByName("nimbus");
				AnkushTask stopUI = new KillJPSProcessByName("core");

				connection.exec(stopUI);
				connection.exec(stopNimbus);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		} finally {
			// disconncet to node/machine
			connection.disconnect();
		}

		// step 2: Kill services on supervisor nodes
		return stopSupervisors(conf.getSupervisors(), conf);
	}

	/**
	 * @param conf
	 */
	private boolean stopSupervisors(List<NodeConf> nodeConfs, StormConf conf) {
		SSHExec connection = null;
		for (NodeConf nodeConf : nodeConfs) {
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			// if connected
			if (connection != null) {
				AnkushTask supervisorStop = new KillJPSProcessByName(
						"supervisor");
				AnkushTask killAllWorkers = new KillJPSProcessByName("worker");

				try {
					logger.debug("Killing supervisor process on node : "
							+ nodeConf.toString());
					connection.exec(supervisorStop);
					connection.exec(killAllWorkers);
				} catch (TaskExecFailException e) {
					logger.debug(e.getMessage());
				}
				connection.disconnect();
			}
		}
		return status(nodeConfs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#startServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		StormConf conf = (StormConf) config;
		logger.setLoggerConfig(conf);
		logger.info("Adding nodes...");
		boolean status = false;
		try {
			status = startSupervisors(nodeList, conf);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.log("Adding nodes", status);
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stopServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		StormConf conf = (StormConf) config;
		logger.setLoggerConfig(conf);
		logger.info("Removing nodes...");
		try {
			stopSupervisors(nodeList, conf);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.log("Removing nodes", true);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List,
	 * com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		StormConf conf = (StormConf) config;
		logger.setLoggerConfig(conf);
		logger.info("Adding nodes...");
		boolean status = false;
		try {
			status = deployNodes(conf, nodeList, true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.log("Adding nodes", status);
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.
	 * List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		StormConf conf = (StormConf) config;
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
