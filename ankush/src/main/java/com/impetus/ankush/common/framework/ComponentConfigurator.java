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
 */
package com.impetus.ankush.common.framework;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentConf;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.dependency.DependencyConf;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.ganglia.GangliaConf;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush2.constant.Constant.Component;

/**
 * It is a ComponentConfigurator class used for configuring the Ganglia, Agent,
 * Preprocessor and Post Processor component configuration.
 * 
 * @author Hokam Chauhan
 * 
 */
public class ComponentConfigurator {

	private static final String KEY_GANGLIA_PORT = "ganglia.port";
	private static final String KEY_GANGLIA_RRDS = "ganglia.rrds";

	private static final String KEY_GANGLIA_POLLING_INTERVAL = "ganglia.polling.interval";

	private static final String KEY_GMOND_CONF = "gmond.conf";

	private static final String KEY_GMETAD_CONF = "gmetad.conf";

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	/**
	 * Getting default Ganglia configuration.
	 * 
	 * @param clusterName
	 * @param username
	 * @return
	 */
	public static GangliaConf getDefaultGangliaConf(String clusterName,
			String username) {

		// getting config reader object.
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

		// GangliaConf.
		GangliaConf gConf = new GangliaConf();

		// Getting gmond configuration path.
		String gmondConfPath = CommonUtil.getUserHome(username)
				+ ankushConf.getStringValue(KEY_GMOND_CONF);

		// Getting gmetad configuration path.
		String gmetadConfPath = CommonUtil.getUserHome(username)
				+ ankushConf.getStringValue(KEY_GMETAD_CONF);

		// Getting ganglia rrds directory path
		String rrdDirectory = CommonUtil.getUserHome(username)
				+ ankushConf.getStringValue(KEY_GANGLIA_RRDS);

		// Setting Ganglia configuration parameters.
		gConf.setGangliaClusterName(clusterName);
		gConf.setGridName(com.impetus.ankush2.constant.Constant.Keys.ANKUSH);
		gConf.setRrdFilePath(rrdDirectory);
		gConf.setPollingInterval(ankushConf
				.getIntValue(KEY_GANGLIA_POLLING_INTERVAL));
		gConf.setPort(ankushConf.getIntValue(KEY_GANGLIA_PORT));
		gConf.setGmondConfPath(gmondConfPath);
		gConf.setGmetadConfPath(gmetadConfPath);
		return gConf;
	}

	/**
	 * Gets the ganglia conf.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @param gmetadNode
	 *            the gmetad node
	 * @return the Ganglia config object to install ganglia
	 */
	public static GangliaConf getGangliaConf(ClusterConf clusterConf) {

		// creating ganglia conf object.
		GangliaConf conf = ComponentConfigurator.getDefaultGangliaConf(
				clusterConf.getClusterName(), clusterConf.getUsername());

		// setting ganglia conf fields.
		conf.setClusterConf(clusterConf);
		conf.setGmetadNode(clusterConf.getGangliaMaster());
		// All Nodes.
		Set<NodeConf> gmondNodes = new HashSet<NodeConf>(
				clusterConf.getNodeConfs());

		conf.setGmondNodes(gmondNodes);
		return conf;
	}

	/**
	 * Gets the agent conf.
	 * 
	 * @param clusterConf
	 *            the cluster conf
	 * @return the agentConf
	 */
	public static AgentConf getAgentConf(ClusterConf clusterConf) {
		// getting config reader object.
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

		// getting installation path.
		String installationPath = CommonUtil.getUserHome(clusterConf
				.getUsername()) + ankushConf.getStringValue("agent.dir");

		// create agent conf obj.
		AgentConf conf = new AgentConf();

		// setting cluster conf.
		conf.setClusterConf(clusterConf);
		// setting installation path.
		conf.setInstallationPath(installationPath);

		// Setting technology na,e
		conf.setTechnologyName(clusterConf.getTechnology());

		// Resource base path.
		String basePath = AppStoreWrapper.getResourcePath();
		// Creating agent bundle path.
		String agentBundlePath = basePath + "scripts/agent/agent.zip";

		// Setting server tarball location.
		conf.setServerTarballLocation(agentBundlePath);
		// setting agent main class name.
		conf.setAgentDaemonClass(ankushConf
				.getStringValue("agent.daemon.class"));
		// setting nodes.
		conf.setNodes(clusterConf.getNodeConfs());
		return conf;
	}

	/**
	 * Gets the dependency conf.
	 * 
	 * @param installJava
	 *            the install java
	 * @param javaBundle
	 *            the java bundle path
	 * @param clusterConf
	 *            the cluster conf
	 * @return the dependency conf
	 */
	public static DependencyConf getDependencyConf(boolean installJava,
			String javaBundle, ClusterConf clusterConf,
			Map<String, Configuration> components) {

		Set<NodeConf> nodeConfs = new HashSet<NodeConf>(
				clusterConf.getNodeConfs());

		DependencyConf conf = new DependencyConf();
		conf.setInstallJava(installJava);
		conf.setJavaBinFileName(javaBundle);
		conf.setClusterConf(clusterConf);
		conf.setComponents(components);
		conf.setNodes(nodeConfs);

		return conf;
	}
}
