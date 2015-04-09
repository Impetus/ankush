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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.ServiceConf;
import com.impetus.ankush.common.scripting.impl.AgentAction;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * @author hokam
 * 
 */
public class ServiceManager {

	/**
	 * Cluster Manager.
	 */
	static private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	/* Logger object */
	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(ServiceManager.class);

	// Errors List collection.
	/** The errors. */
	private List<String> errors = new ArrayList<String>();

	// Returning result map.
	/** The result. */
	private Map result = new HashMap();

	/**
	 * Return result.
	 * 
	 * @return The result object.
	 */
	private Map returnResult() {
		if (this.errors.isEmpty()) {
			this.result.put("status", true);
		} else {
			this.result.put("status", false);
			this.result.put("error", this.errors);
		}
		return this.result;
	}

	public Map manageClusterService(Long clusterId, Map parameterMap)
			throws Exception {
		// Getting cluster object from database.
		Cluster cluster = clusterManager.get(clusterId);

		// To be implemented

		// if null then set error and return.
		if (cluster == null) {
			errors.add("Could not find cluster.");
			return returnResult();
		}
		return returnResult();
	}

	/**
	 * Method to perform the start/stop action on services
	 * 
	 * @param clusterId
	 * @param serviceConf
	 * @return
	 * @throws Exception
	 */
	public Map manage(Long clusterId, ServiceConf serviceConf) throws Exception {

		// Getting cluster object from database.
		Cluster cluster = clusterManager.get(clusterId);

		// if null then set error and return.
		if (cluster == null) {
			errors.add("Could not find cluster.");
			return returnResult();
		}

		// Iterating over the services to perform the action.
		for (String serviceName : serviceConf.getServices()) {
			// service error message.
			String errorMsg = "Unable to " + serviceConf.getAction() + " "
					+ serviceName
					+ " service, please view server logs for more detatils.";
			// service monitor object.
			ServiceMonitorable serviceMonitor = null;

			serviceMonitor = ObjectFactory
						.getServiceMonitorableInstanceByServiceName(serviceName);


			// if it is null then add error else perform the action
			if (serviceMonitor == null) {
				this.errors.add(errorMsg);
				logger.error("Unable to map class service monitor class for "
						+ serviceName + ".");
			} else {
				// Cluster conf
				ClusterConf clusterConf = cluster.getClusterConf();

				// SSH conncetion
				SSHExec connection = null;
				try {
					logger.info("connecting with " + serviceConf.getIp());

					// connect to remote node
					connection = SSHUtils.connectToNode(serviceConf.getIp(),
							clusterConf.getUsername(),
							clusterConf.getPassword(),
							clusterConf.getPrivateKey());

					// Getting process name.
					String processName = Constant.RoleProcessName
							.getProcessName(serviceName);
					if (processName == null) {
						processName = serviceName;
					}

					// force stop flag.
					String forceStopFlag = "false";
					if (serviceConf.getAction().equals(
							Constant.ServiceAction.STOP)) {
						forceStopFlag = "true";
					}
					// update forceStop flag when user manually start/stop the
					// service.
					AgentAction setForceStopFlag = new AgentAction(
							Constant.Agent.Action.Handler.HACONFIG,
							Constant.Agent.Action.ADDFORCESTOP, serviceName,
							Constant.Agent.AGENT_HASERVICE_CONF_PATH,
							forceStopFlag);

					// setting force stop.
					connection.exec(setForceStopFlag);

					// service start/stop operation.
					boolean requestStatus = serviceMonitor.manageService(
							clusterConf, connection, processName,
							serviceConf.getAction());

					// if failed to perform action.
					if (!requestStatus) {
						this.errors.add(errorMsg);
						logger.error("Unable to process service monitor request for "
								+ serviceName + ".");
					}

				} catch (Exception e) {
					this.errors.add(errorMsg);
					logger.error(e.getMessage());
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}
		return returnResult();
	}
}
