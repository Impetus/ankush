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
package com.impetus.ankush2.agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.AbstractDeployer;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

public class PostProcessorDeployer extends AbstractDeployer {

	/** GangliaDeployer logger */
	private AnkushLogger logger = new AnkushLogger(PostProcessorDeployer.class);
	
	private ClusterConfig clusterConfig;
	// private ComponentConfig compConfig;

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush2.framework.AbstractDeployer#getComponentName()
	 */
	@Override
	public String getComponentName() {
		return Constant.Component.Name.POSTPROCESSOR;
	}
	
	private String errMsg(String type) {
		return "Could not " + type + " " + getComponentName()
				+ ". Please view server logs for more details.";
	}

	@Override
	public boolean deploy(ClusterConfig conf) {
		try {
			// set cluster
			this.setClusterAndLogger(conf);
			// deploying and starting ganglia on all nodes
			return deployNodes(conf);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), getComponentName(), e);
			clusterConfig.addError(getComponentName(), e.getMessage());
		} catch (Exception e) {
			logger.error(errMsg("deploy"), getComponentName(), e);
			clusterConfig.addError(getComponentName(), errMsg("deploy"));
		}
		return false;
	}

	@Override
	public boolean undeploy(ClusterConfig conf) {
		String message = "Stopping Host PostProcessor Configuration";
		try {
			// set cluster
			this.setClusterAndLogger(conf);
			// logging startup message.
			logger.info(message);
			// Node Deployment process ...
			final Semaphore semaphore = new Semaphore(conf.getNodes().size());
			for (final NodeConfig nodeConfig : conf.getNodes().values()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							nodeConfig.setStatus(stopNode(nodeConfig));
						} catch (AnkushException e) {
							logger.error(e.getMessage(), getComponentName(), e);
							clusterConfig.addError(getComponentName(),
									e.getMessage());
						} catch (Exception e) {
							logger.error(errMsg("delete"), getComponentName(),
									e);
							clusterConfig.addError(getComponentName(),
									errMsg("delete"));
						} finally {
							if (semaphore != null) {
								semaphore.release();
							}
						}
					}
				});
			}
			semaphore.acquire(conf.getNodes().size());
			return AnkushUtils.getStatus(conf.getNodes());
		} catch (AnkushException e) {
			logger.error(e.getMessage(), getComponentName(), e);
			clusterConfig.addError(getComponentName(), e.getMessage());
		} catch (Exception e) {
			logger.error(message + " failed.", getComponentName(), e);
			clusterConfig.addError(getComponentName(), message + " failed.");
		}
		return false;
	}

	@Override
	public boolean addNode(ClusterConfig conf, ClusterConfig newConf) {
		try {
			// set cluster
			this.setClusterAndLogger(conf);
			// deploying and starting ganglia on all nodes
			return deployNodes(newConf);
		} catch (AnkushException e) {
			logger.error(e.getMessage(), getComponentName(), e);
			conf.addError(getComponentName(), e.getMessage());
			;
		} catch (Exception e) {
			logger.error(errMsg("deploy"), getComponentName(), e);
			conf.addError(getComponentName(), errMsg("deploy"));
		}
		return false;
	}

	@Override
	public boolean removeNode(final ClusterConfig conf, Collection<String> nodes) {
		String message = "Stopping Host PostProcessor Configuration";
		try {
			// set cluster
			this.setClusterAndLogger(conf);
			// logging startup message.
			logger.info(message);
			// Node Deployment process ...
			final Semaphore semaphore = new Semaphore(nodes.size());
			for (final String host : nodes) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						try {
							NodeConfig nodeConfig = conf.getNodes().get(host);
							nodeConfig.setStatus(stopNode(nodeConfig));
							if (semaphore != null) {
								semaphore.release();
							}
						} catch (AnkushException e) {
							logger.error(e.getMessage(), getComponentName(), e);
							clusterConfig.addError(getComponentName(),
									e.getMessage());
						} catch (Exception e) {
							logger.error(errMsg("delete"), getComponentName(),
									e);
							clusterConfig.addError(getComponentName(),
									errMsg("delete"));
						}
					}
				});
			}
			semaphore.acquire(nodes.size());
			// returning true in all cases
			return true;
		} catch (AnkushException e) {
			logger.error(e.getMessage(), getComponentName(), e);
			clusterConfig.addError(getComponentName(), e.getMessage());
		} catch (Exception e) {
			logger.error(message + " failed.", getComponentName(), e);
			clusterConfig.addError(getComponentName(), message + " failed.");
		}
		return false;
	}

	private void setClusterAndLogger(ClusterConfig clusterConfig)
			throws AnkushException {
		if (clusterConfig == null) {
			throw new AnkushException("Could not find clusterConfig");
		}
		this.clusterConfig = clusterConfig;
		// setting logger config.
		logger.setCluster(clusterConfig);
	}

	private void restartAgent(NodeConfig nodeConfig) throws AnkushException {
		try {
			new AgentServiceManager().stopAgent(clusterConfig, nodeConfig);
		} catch (AnkushException e) {
			// Do nothing
		}	
		try {
			new AgentServiceManager().startAgent(clusterConfig, nodeConfig);
		} catch (AnkushException e) {
			// Do nothing
		}	
	}

	private boolean deployNodes(final ClusterConfig clusterConfig)
			throws AnkushException {
		// logging startup message.
		String message = "Starting Host PostProcessor Configuration";
		try {
			logger.info(message, getComponentName());
			// Node Deployment process ...
			final Semaphore semaphore = new Semaphore(clusterConfig.getNodes()
					.size());
			for (final NodeConfig nodeConfig : clusterConfig.getNodes()
					.values()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean nodestatus = true;
						try {
//							if (createNode(nodeConfig)) {
//								nodestatus = startNode(nodeConfig);
//							}
							restartAgent(nodeConfig);
						} catch (AnkushException e) {
							logger.error(e.getMessage(), getComponentName(),
									nodeConfig.getHost(), e);
							clusterConfig.addError(nodeConfig.getHost(),
									getComponentName(), e.getMessage());
						}
						nodeConfig.setStatus(nodestatus);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(clusterConfig.getNodes().size());
			// return AnkushUtils.getStatus(clusterConfig.getNodes());
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), getComponentName(), e);
			return true;
		}
	}

	/**
	 * installing and configuring
	 * 
	 * @param host
	 *            {@link String}
	 * @return <code>true</code> , if successful
	 */
	private boolean createNode(NodeConfig nodeConfig) throws AnkushException {
		String message = "Starting Host PostProcessor Configuration";
		final String host = nodeConfig.getHost();
		logger.info(message, getComponentName(), host);
		try {
			SSHExec connection = nodeConfig.getConnection();
			if (connection == null) {
				throw new AnkushException("Could not get connection.");
			}
			return addTaskableClassesToAgent(connection, host);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not deploy Agent.");
		}
	}

	private boolean addTaskableClassesToAgent(SSHExec connection, String host)
			throws AnkushException {
		try {
			List<String> classes = new ArrayList<String>();
			// adding dir usage monitor.
			classes.add(Constant.Agent.TaskableClass.DIR_USAGE_MONITOR);
			// Add Service Monitor taskable
			classes.add(Constant.Agent.TaskableClass.SERVICE_MONITOR);

			// adding classes in agent taskables.
			return AgentUtils.addTaskables(connection, classes);
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not add taskable classes to Agent");
		}
	}

	private boolean startNode(NodeConfig nodeConfig) throws AnkushException {
//		final String host = nodeConfig.getHost();
//		try {
//			SSHExec connection = nodeConfig.getConnection();
//			if (connection == null) {
//				throw new AnkushException("Could not get connection.");
//			}
//			return new AgentServiceManager().startJmxTrans(clusterConfig,
//					connection, host);
//		} catch (AnkushException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new AnkushException("Could not start Agent");
//		}
		return true;
	}

	private boolean stopNode(NodeConfig nodeConfig) throws AnkushException {
//		try {
//			final String host = nodeConfig.getHost();
//			SSHExec connection = nodeConfig.getConnection();
//			if (connection == null) {
//				throw new AnkushException("Could not get connection.");
//			}
//			return new AgentServiceManager().stopJmxTrans(clusterConfig,
//					connection, host);
//			
//		} catch (AnkushException e) {
//			throw e;
//		}
		return true;
	}

}
