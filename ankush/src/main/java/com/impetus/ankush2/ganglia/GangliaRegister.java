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
package com.impetus.ankush2.ganglia;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.logger.AnkushLogger;
import com.impetus.ankush2.utils.AnkushUtils;
import com.impetus.ankush2.utils.SSHUtils;

public class GangliaRegister {

	private AnkushLogger logger = new AnkushLogger(GangliaRegister.class);;
	private ClusterConfig clusterConf;
	private ComponentConfig compConfig;
	private Map<String, Object> advanceConf;
	private SSHExec connection;
	private String gmetadHost;
	private String gmetadConfPath;

	public GangliaRegister(ClusterConfig clusterConf) throws AnkushException {
		try {
			this.clusterConf = clusterConf;
			this.logger = new AnkushLogger(this.getClass(), this.clusterConf);
			this.compConfig = clusterConf.getComponents().get(
					Constant.Component.Name.GANGLIA);
			this.advanceConf = this.compConfig.getAdvanceConf();
			setConnection();
		} catch (Exception e) {
			throw new AnkushException("There is some exception while getting "
					+ Constant.Component.Name.GANGLIA
					+ " cluster details for registration. "
					+ GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	/**
	 * Method to set connection object of gmetad host
	 */
	private void setConnection() throws AnkushException {

		try {
			gmetadHost = (String) this.advanceConf
					.get(GangliaConstants.ClusterProperties.GMETAD_HOST);
			logger.info("Getting node connection.",
					Constant.Component.Name.GANGLIA, gmetadHost);
			if (gmetadHost == null || gmetadHost.isEmpty()) {
				throw new AnkushException(
						"Ganglia master hostname provided from UI is either null or empty.");
			}
			this.connection = SSHUtils.connectToNode(gmetadHost,
					clusterConf.getAuthConf());
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException("Could not get create connection with "
					+ gmetadHost, e);
		}
	}

	public boolean createConfig() throws AnkushException {

		try {
			logger.info("Getting " + Constant.Component.Name.GANGLIA
					+ " cluster configuration for registration.",
					Constant.Component.Name.GANGLIA);
			gmetadConfPath = (String) advanceConf
					.get(GangliaConstants.ClusterProperties.GMETAD_CONF_PATH);
			if (gmetadConfPath == null || gmetadConfPath.isEmpty()) {
				throw new AnkushException(
						"GMetad configuration path from where to read "
								+ Constant.Component.Name.GANGLIA
								+ " cluster configuration for registration is either null or empty.");
			}
			setClusterName();
			setRRDPath();
			setGridName();
			addNodesToClusternodes();
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while getting details for registering "
							+ Constant.Component.Name.GANGLIA + " cluster. "
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
		return true;
	}

	private void setClusterName() throws AnkushException {
		try {
			logger.info("Getting Ganglia cluster name details.",
					Constant.Component.Name.GANGLIA, gmetadHost);
			String clusterNameCmd = "cat " + gmetadConfPath
					+ " | grep ^data_source | awk -F'\"' '{ print $2 }'";
			CustomTask task = new ExecCommand(clusterNameCmd);
			Result res = connection.exec(task);
			if (res.rc != 0) {
				throw new AnkushException(
						"Could not identify Ganglia cluster name from "
								+ gmetadConfPath + " file.");
			}
			task = new ExecCommand(clusterNameCmd + " | wc -l | grep '1'");
			if (connection.exec(task).rc != 0) {
				throw new AnkushException(
						"Ganglia cluster registration with multiple data sources not supported.");
			}
			this.advanceConf.put(
					GangliaConstants.ClusterProperties.GANGLIA_CLUSTER_NAME,
					res.sysout.trim());
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while reading Ganglia cluster name from "
							+ gmetadConfPath + " file. "
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	private void setRRDPath() throws AnkushException {
		try {
			logger.info("Getting RRD path directory location.",
					Constant.Component.Name.GANGLIA, gmetadHost);
			String rrdPathCmd = "cat " + gmetadConfPath
					+ " | grep ^rrd_rootdir | awk -F'\"' '{ print $2 }'";
			CustomTask task = new ExecCommand(rrdPathCmd);
			Result res = connection.exec(task);
			if (res.rc != 0) {
				throw new AnkushException("Could not identify RRD path from "
						+ gmetadConfPath + " file.");
			}
			task = new ExecCommand(rrdPathCmd + " | wc -l | grep '1'");
			if (connection.exec(task).rc != 0) {
				throw new AnkushException(
						"Multiple rrd path location identified from "
								+ gmetadConfPath
								+ " file, so could not register "
								+ Constant.Component.Name.GANGLIA);
			}
			this.advanceConf.put(
					GangliaConstants.ClusterProperties.RRD_FILE_PATH,
					res.sysout.trim());
		} catch (AnkushException e) {
			throw e;
		} catch (Exception e) {
			throw new AnkushException(
					"There is some exception while reading RRD path location from "
							+ gmetadConfPath + " file. "
							+ GangliaConstants.EXCEPTION_STRING, e);
		}
	}

	/**
	 * Setting gridname in advance conf, not a mandatory requirement so no
	 * exception is thrown.
	 * 
	 * @param gmetadConfPath
	 * 
	 */
	private void setGridName() {
		try {
			String rrdPathCmd = "cat " + gmetadConfPath
					+ " | grep ^gridname | awk -F'\"' '{ print $2 }'";
			CustomTask task = new ExecCommand(rrdPathCmd);
			Result res = connection.exec(task);
			if (res.rc == 0) {
				task = new ExecCommand(rrdPathCmd + " | wc -l | grep '1'");
				if (connection.exec(task).rc == 0) {
					this.advanceConf.put(
							GangliaConstants.ClusterProperties.GRID_NAME,
							res.sysout.trim());
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Adding nodes to cluster and component nodes. Need to improve the logic
	 */
	private void addNodesToClusternodes() throws AnkushException {
		try {
			Set<String> roles = null;
			Set<String> gmondNodes = new HashSet<String>();
			for (String host : clusterConf.getNodes().keySet()) {
				roles = new HashSet<String>();
				roles.add(GangliaConstants.Ganglia_Services.gmond.toString());
				if (host.equals(gmetadHost)) {
					roles.add(GangliaConstants.Ganglia_Services.GangliaMaster
							.toString());
				}
				// skip Registration in Level1
				if (!AnkushUtils.isMonitoredByAnkush(compConfig)) {
					AnkushUtils.addNodeToComponentConfig(clusterConf,
							Constant.Component.Name.GANGLIA, host,
							new HashMap());

				} else {
					AnkushUtils.addNodeToClusterAndComponent(clusterConf, host,
							roles, Constant.Component.Name.GANGLIA);
				}
				gmondNodes.add(host);
			}
			// Adding gmond node set to Ganglia advance conf
			advanceConf.put(GangliaConstants.ClusterProperties.GMOND_SET,
					gmondNodes);
			// checking if gmetad host is not a part of cluster nodes and if not
			// , adding gmetad node to cluster nodes with role as Ganglia Master
			if (!clusterConf.getNodes().containsKey(gmetadHost)) {
				roles = new HashSet<String>(Arrays.asList(
						GangliaConstants.Ganglia_Services.GangliaMaster
								.toString(),
						GangliaConstants.Ganglia_Services.gmond.toString()));
				// skip Registration in Level1
				if (!AnkushUtils.isMonitoredByAnkush(compConfig)) {
					AnkushUtils.addNodeToComponentConfig(clusterConf,
							Constant.Component.Name.GANGLIA, gmetadHost,
							new HashMap());

				} else {
					AnkushUtils.addNodeToClusterAndComponent(clusterConf,
							gmetadHost, roles, Constant.Component.Name.GANGLIA);
				}
			}
		} catch (Exception e) {
			throw new AnkushException("Could not add "
					+ Constant.Component.Name.GANGLIA
					+ " nodes to cluster nodes during registration.", e);
		}

	}

	@Override
	protected void finalize() throws Throwable {
		try {
			if (this.connection != null) {
				connection.disconnect();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.finalize();
	}
}
