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
import com.impetus.ankush.common.postprocessor.PostProcessorConf;
import com.impetus.ankush.common.preprocessor.PreprocessorConf;
import com.impetus.ankush.common.utils.CommonUtil;

/**
 * It is a component configurator class used for configuring the ganglia and
 * agent configuration.
 * 
 * @author Hokam Chauhan
 * 
 */
public class ComponentConfigurator {

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

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
		Set<NodeConf> gmondNodes = new HashSet<NodeConf>(
				clusterConf.getNodeConfs());
		// getting config reader object.
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();
		// gettign resource path.
		String resourceBasePath = AppStoreWrapper.getResourcePath();

		// making installation path using username.
		String installationPath = CommonUtil.getUserHome(clusterConf
				.getUsername()) + ankushConf.getStringValue("ganglia.dir");

		// creating ganglia conf object.
		GangliaConf conf = new GangliaConf();

		// setting ganglia conf fields.
		conf.setClusterConf(clusterConf);
		conf.setInstallationPath(installationPath);
		conf.setComponentVersion("3.1.7");
		conf.setGridName("Ankush");
		conf.setDwooFilePath("/var/lib/ganglia/dwoo/");
		conf.setRrdFilePath("/var/lib/ganglia/rrds/");
		conf.setServerConfFolder(resourceBasePath + "config/ganglia/");
		conf.setPollingInterval(15);
		conf.setPort(ankushConf.getIntValue("ganglia.port"));
		conf.setGmetadNode(clusterConf.getGangliaMaster());
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

		// setting the fields of agent conf.
		conf.setClusterConf(clusterConf);
		conf.setInstallationPath(installationPath);
		conf.setTechnologyName(clusterConf.getTechnology());

		String basePath = AppStoreWrapper.getResourcePath();
		String localAgentBasePath = basePath + "scripts/agent/";

		String localAgentJarsPath = localAgentBasePath + "jars/agent.zip";

		conf.setLocalJarsPath(localAgentJarsPath);
		conf.setAgentDaemonClass(ankushConf
				.getStringValue("agent.daemon.class"));
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

	/**
	 * Gets the dependency conf.
	 * 
	 * @param installJava
	 *            the install java
	 * @param javaBinFileName
	 *            the java bin file name
	 * @param clusterConf
	 *            the cluster conf
	 * @return the dependency conf
	 */
	public static PreprocessorConf getPreprocessorConf(ClusterConf clusterConf) {

		Set<NodeConf> nodeConfs = new HashSet<NodeConf>(
				clusterConf.getNodeConfs());
		PreprocessorConf conf = new PreprocessorConf();
		conf.setClusterConf(clusterConf);
		conf.setClusterNodeConfs(nodeConfs);
		return conf;
	}

	/**
	 * Gets the dependency conf.
	 * 
	 * @param installJava
	 *            the install java
	 * @param javaBinFileName
	 *            the java bin file name
	 * @param clusterConf
	 *            the cluster conf
	 * @return the dependency conf
	 */
	public static PostProcessorConf getPostProcessorConf(ClusterConf clusterConf) {

		Set<NodeConf> nodeConfs = new HashSet<NodeConf>(
				clusterConf.getNodeConfs());
		/** The ankushConf Reader. */
		ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();
		String jmxInstallPath = CommonUtil.getUserHome(clusterConf
				.getUsername())
				+ ankushConf.getStringValue("agent.dir")
				+ ankushConf
						.getStringValue("jmxtrans.installation.relative.path");
		String jmxScriptFilePath = jmxInstallPath
				+ ankushConf.getStringValue("jmx.script.file.name");
		PostProcessorConf conf = new PostProcessorConf();
		conf.setClusterConf(clusterConf);
		conf.setClusterNodeConfs(nodeConfs);
		conf.setGangliaMasterIp(clusterConf.getGangliaMaster().getPrivateIp());
		conf.setJmxTransScriptFilePath(jmxScriptFilePath);
		return conf;
	}
}
