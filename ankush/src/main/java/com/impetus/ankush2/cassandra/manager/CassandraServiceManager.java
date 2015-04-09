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
package com.impetus.ankush2.cassandra.manager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush2.cassandra.utils.CassandraConstants;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.Serviceable;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;

public class CassandraServiceManager implements Serviceable {

	private static final String COULD_NOT_GET_CONNECTION = "Could not get connection.";
	private static final String SERVER_LOG_MESSAGE = "Please view server logs for more details.";

	/** The compConfig */
	private ComponentConfig componentConfig;

	private static AnkushLogger logger = new AnkushLogger(
			CassandraServiceManager.class);

	String componentName;

	@Override
	public String getComponentName() {
		return this.componentName;
		// return Constant.Component.Name.CASSANDRA;
	}

	@Override
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public Set<String> getServiceList(ClusterConfig clusterConfig) {
		return new HashSet<String>(
				Arrays.asList(CassandraConstants.Cassandra_Services.CassandraDaemon
						.toString()));
	}

	@Override
	public boolean startServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		logger.setCluster(clusterConfig);
		// getting connection object
		SSHExec connection = clusterConfig.getNodes().get(host).getConnection();
		if (connection == null) {
			logger.error(COULD_NOT_GET_CONNECTION, getComponentName(), host);
			return false;
		}
		return manageService(clusterConfig, connection, services,
				Constant.ServiceAction.START);

	}

	@Override
	public boolean stopServices(ClusterConfig clusterConfig, String host,
			Set<String> services) {
		logger.setCluster(clusterConfig);
		// getting connection object
		SSHExec connection = clusterConfig.getNodes().get(host).getConnection();
		if (connection == null) {
			logger.error(COULD_NOT_GET_CONNECTION, getComponentName(), host);
			return false;
		}
		return manageService(clusterConfig, connection, services,
				Constant.ServiceAction.STOP);
	}

	public boolean manageService(ClusterConfig clusterConfig,
			SSHExec connection, Set<String> services,
			Constant.ServiceAction action) {
		try {
			for (String service : services) {
				switch (CassandraConstants.Cassandra_Services.valueOf(service)) {
				case CassandraDaemon:
				case CassandraSeed:
				case CassandraNonSeed:
					return manageCassandraDaemon(clusterConfig, connection,
							action, CassandraConstants.CASSANDRA_DAEMON);
				}
			}
		} catch (Exception e) {
			logger.error("Could not find service to manage.");
		}
		return false;
	}

	public boolean manageCassandraDaemon(ClusterConfig clusterConfig,
			SSHExec connection, Constant.ServiceAction action, String service) {
		String errMsg = "Could not " + action.toString().toLowerCase() + " "
				+ service;
		logger.info(action.toString().toLowerCase() + " " + getComponentName()
				+ " services", getComponentName());
		ComponentConfig compConfig = clusterConfig.getComponents().get(
				Constant.Component.Name.CASSANDRA);
		AnkushTask manageServices;
		if (action.equals(Constant.ServiceAction.START)) {
			String command = compConfig
					.getAdvanceConfStringProperty(CassandraConstants.ClusterProperties.BIN_DIR)
					+ CassandraConstants.Cassandra_executables.CASSANDRA_DAEMON_START;
			manageServices = new RunInBackground(command);
		} else {
			manageServices = new KillJPSProcessByName(service);
		}
		try {
			if (connection.exec(manageServices).rc != 0) {
				logger.error(errMsg, getComponentName());
				return false;
			} else {
				logger.info(service + " " + action.toString().toLowerCase()
						+ "ed successfully.", getComponentName());
			}
		} catch (TaskExecFailException e) {
			logger.error(errMsg, getComponentName());
			return false;
		}
		return true;
	}

	@Override
	public boolean start(final ClusterConfig clusterConfig) {
		return manageClusterServices(clusterConfig,
				Constant.ServiceAction.START);
	}

	@Override
	public boolean stop(final ClusterConfig clusterConfig) {
		return manageClusterServices(clusterConfig, Constant.ServiceAction.STOP);
	}

	private boolean manageClusterServices(final ClusterConfig clusterConfig,
			final Constant.ServiceAction action) {
		logger.setCluster(clusterConfig);
		if (clusterConfig == null) {
			logger.error("Could not get clusterConfig.", getComponentName());
			return false;
		}
		// getting component config
		this.componentConfig = clusterConfig.getComponents().get(
				Constant.Component.Name.CASSANDRA);
		// Creating semaphores
		final Semaphore semaphore = new Semaphore(componentConfig.getNodes()
				.size());
		try {
			// starting service on each node in cluster
			for (final String host : componentConfig.getNodes().keySet()) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {

						switch (action) {
						case START:
							clusterConfig.getNodes().get(host)
									.setStatus(startNode(clusterConfig, host));
							break;
						case STOP:
							clusterConfig.getNodes().get(host)
									.setStatus(stopNode(clusterConfig, host));
							break;
						}
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(componentConfig.getNodes().size());
		} catch (Exception e) {
			logger.error("Could not " + action + " " + getComponentName()
					+ " service.", getComponentName(), e);
			return false;
		}
		return AnkushUtils.getStatus(clusterConfig.getNodes());
	}

	@Override
	public boolean startNode(ClusterConfig clusterConfig, String host) {
		try {
			logger.setCluster(clusterConfig);

			logger.info("Starting " + getComponentName() + "...",
					getComponentName(), host);
			if (clusterConfig == null || host == null || host.isEmpty()) {
				logger.error(
						"Either clusterConfig or host is empty or undefined.",
						getComponentName(), host);
				return false;
			}
			// get connection object
			SSHExec connection = clusterConfig.getNodes().get(host)
					.getConnection();
			if (connection == null) {
				logger.error(COULD_NOT_GET_CONNECTION, getComponentName(), host);
				return false;
			}
			// getting services list
			logger.info(
					"Getting " + getComponentName() + " services on nodes.",
					getComponentName(), host);
			Set<String> services = clusterConfig.getNodes().get(host)
					.getRoles().get(Constant.Component.Name.CASSANDRA);

			return manageService(clusterConfig, connection, services,
					Constant.ServiceAction.START);
		} catch (Exception e) {
			logger.error("Could not start " + getComponentName() + ". "
					+ SERVER_LOG_MESSAGE);
			return false;
		}
	}

	@Override
	public boolean stopNode(ClusterConfig clusterConfig, String host) {
		try {
			logger.setCluster(clusterConfig);
			if (clusterConfig == null || host == null || host.isEmpty()) {
				logger.error(
						"Either clusterConfig or host is empty or undefined.",
						getComponentName(), host);
				return false;
			}
			// get connection object
			SSHExec connection = clusterConfig.getNodes().get(host)
					.getConnection();
			if (connection == null) {
				logger.error(COULD_NOT_GET_CONNECTION, getComponentName(), host);
				return false;
			}
			// getting services list
			Set<String> services = clusterConfig.getNodes().get(host)
					.getRoles().get(Constant.Component.Name.CASSANDRA);

			return manageService(clusterConfig, connection, services,
					Constant.ServiceAction.STOP);
		} catch (Exception e) {
			logger.error("Could not stop " + getComponentName() + ". "
					+ SERVER_LOG_MESSAGE);
			return false;
		}
	}

	@Override
	public boolean startRole(ClusterConfig clusterConfig, String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopRole(ClusterConfig clusterConfig, String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLogDirPath(ClusterConfig clusterConfig, String host,
			String role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogFilesRegex(ClusterConfig clusterConfig,
			String host, String role, Map<String, Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}
}
