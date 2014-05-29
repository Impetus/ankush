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
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.HadoopClusterMonitor;

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

	public Map manage(Long clusterId, ServiceConf serviceConf) throws Exception {

		// Getting cluster object from database.
		Cluster cluster = clusterManager.get(clusterId);
		if (cluster == null) {
			errors.add("Could not find cluster.");
		}
		
		for(String serviceName : serviceConf.getServices()) {
			String errorMsg = "Unable to " + serviceConf.getAction() + " " + serviceName + " service, please view server logs for more detatils.";
			ServiceMonitorable serviceMonitor = null;
			if(serviceName.startsWith("rg") || serviceName.startsWith("sn")) {
				serviceMonitor = ObjectFactory.getServiceMonitorableInstanceByServiceName(Constant.Technology.ORACLE_NOSQL);
			} else {
				serviceMonitor = ObjectFactory.getServiceMonitorableInstanceByServiceName(serviceName);	
			}
			
			if(serviceMonitor == null) {
				this.errors.add(errorMsg);
				logger.error("Unable to map class service monitor class for " + serviceName + ".");
			} else {
				ClusterConf clusterConf = cluster.getClusterConf();
				
				SSHExec connection = null;
				try {
					logger.info("connecting with " + serviceConf.getIp());

					// connect to remote node
					connection = SSHUtils.connectToNode(serviceConf.getIp(),
							clusterConf.getUsername(), clusterConf.getPassword(),
							clusterConf.getPrivateKey());

					String processName = Constant.RoleProcessName.getProcessName(serviceName);
					if(processName == null) {
						processName = serviceName;
					}
					
					boolean requestStatus = serviceMonitor.manageService(clusterConf, connection, processName, serviceConf.getAction()); 
					
					if(!requestStatus) {
						this.errors.add(errorMsg);
						logger.error("Unable to process service monitor request for " + serviceName + ".");
					}

				} catch (Exception e) {
					this.errors.add(errorMsg);
					logger.error(e.getMessage());
				} finally {
					if(connection != null) {
						connection.disconnect();
					}
				}
			}
		}
		return returnResult();
	}
}
