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
package com.impetus.ankush.common.framework;

import java.lang.reflect.Method;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.ServiceConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * @author hokam
 * 
 */
public abstract class ServiceMonitor {

	protected Cluster cluster = null;
	protected ServiceConf serviceConf = null;
	private static final AnkushLogger LOG = new AnkushLogger(
			ServiceMonitor.class);

	public boolean startagent(SSHExec connection, ClusterConf conf) {
		String startAgent = "sh " + Constant.Agent.AGENT_START_SCRIPT;
		return SSHUtils.action(connection, startAgent);
	}

	public boolean stopagent(SSHExec connection, ClusterConf conf) {
		String stopAgent = "sh " + Constant.Agent.AGENT_STOP_SCRIPT;
		return SSHUtils.action(connection, stopAgent);
	}

	public void action(String action, SSHExec connection, String service,
			ClusterConf conf) {
		try {
			Method method;
			String methodName = action + service.toLowerCase();
			try {
				method = this.getClass().getMethod(methodName, SSHExec.class,
						ClusterConf.class);
			} catch (NoSuchMethodException e) {
				method = ServiceMonitor.class.getMethod(methodName,
						SSHExec.class, ClusterConf.class);
			}
			method.invoke(this, connection, conf);
		} catch (SecurityException e) {
			LOG.error(e.getMessage());
		} catch (NoSuchMethodException e) {
			LOG.error(e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	public boolean stopgmetad(SSHExec connection, ClusterConf conf) {
		String stopGmetadCmd = "killall -9 gmetad";
		return SSHUtils.action(conf.getPassword(), connection, stopGmetadCmd);
	}

	public boolean stopgmond(SSHExec connection, ClusterConf conf) {
		String stopGmondCmd = "killall -9 gmond";
		return SSHUtils.action(conf.getPassword(), connection, stopGmondCmd);
	}

	public boolean startgmetad(SSHExec connection, ClusterConf conf) {
		String startGmetadCmd = "gmetad --conf=.ankush/monitoring/conf/gmetad.conf";
		return SSHUtils.action(conf.getPassword(), connection, startGmetadCmd);
	}

	public boolean startgmond(SSHExec connection, ClusterConf conf) {
		String startGmondCmd = "gmond --conf=.ankush/monitoring/conf/gmond.conf";
		return SSHUtils.action(conf.getPassword(), connection, startGmondCmd);
	}

	public void managerService(Cluster cluster, ServiceConf conf) {

		this.cluster = cluster;
		this.serviceConf = conf;
		AppStoreWrapper.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				manageServices();
			}
		});
	}

	private void manageServices() {
		ClusterConf conf = cluster.getClusterConf();

		SSHExec connection = null;
		try {
			LOG.info("connecting with " + serviceConf.getIp());

			// connect to remote node
			connection = SSHUtils.connectToNode(serviceConf.getIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			for (String service : serviceConf.getServices()) {
				action(serviceConf.getAction(), connection, service, conf);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	// public void startquorumpeermain(SSHExec connection, ClusterConf conf)
	// throws TaskExecFailException {
	// monitorZookeeper(connection, conf);
	// }
	//
	// public void stopquorumpeermain(SSHExec connection, ClusterConf conf)
	// throws TaskExecFailException {
	// monitorZookeeper(connection, conf);
	// }
	//
	// private void monitorZookeeper(SSHExec connection, ClusterConf conf)
	// throws TaskExecFailException {
	// ZookeeperConf zConf = (ZookeeperConf) conf.getClusterComponents().get(
	// Constant.Component.Name.ZOOKEEPER);
	// String componentHome = zConf.getInstallationPath() + "zookeeper-"
	// + zConf.getComponentVersion();
	// String command = componentHome + "/bin/zkServer.sh "
	// + serviceConf.getAction();
	// AnkushTask task = new RunInBackground(command);
	// Result res = connection.exec(task);
	// if (!res.isSuccess) {
	// LOG.error(serviceConf.getIp(),
	// "Could not " + serviceConf.getAction()
	// + " quorumpeermain service");
	// }
	// }
}
