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
package com.impetus.ankush2.hadoop.deployer.configurator;

import java.util.Map;
import java.util.Set;

import net.neoremind.sshxcute.core.Result;

import org.apache.commons.lang.StringUtils;

import com.impetus.ankush.common.exception.AnkushException;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFileUsingEcho;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;
import com.impetus.ankush2.framework.config.ComponentConfig;
import com.impetus.ankush2.framework.config.NodeConfig;
import com.impetus.ankush2.hadoop.config.ComponentConfigContext;
import com.impetus.ankush2.hadoop.utils.HadoopConstants;
import com.impetus.ankush2.hadoop.utils.HadoopUtils;
import com.impetus.ankush2.utils.AnkushUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopConfigurator.
 * 
 * @author Akhil
 */
public abstract class HadoopConfigurator extends ComponentConfigContext {

	/**
	 * Instantiates a new hadoop configurator.
	 * 
	 * @param clusterConfig
	 *            the cluster config
	 * @param hadoopConfig
	 *            the hadoop config
	 * @param classObj
	 *            the class obj
	 */
	public HadoopConfigurator(ClusterConfig clusterConfig,
			ComponentConfig hadoopConfig, Class classObj) {
		super(clusterConfig, hadoopConfig, classObj);
		// this.clusterConfig = clusterConfig;
		// this.hadoopConfig = hadoopConfig;
		// this.LOG = new AnkushLogger(classObj);
		// this.LOG.setCluster(clusterConfig);
	}

	public HadoopConfigurator() {
		super();
	}

	/** The Constant HADOOP_URI_PREFIX. */
	public static final String HADOOP_URI_PREFIX = "hdfs://";

	/** The Constant DEFAULT_PORT_HTTP_DATANODE. */
	public static final String DEFAULT_PORT_HTTP_DATANODE = "50075";

	/** The Constant DEFAULT_PORT_HTTP_NAMENODE. */
	public static final String DEFAULT_PORT_HTTP_NAMENODE = "50070";

	/** The Constant DEFAULT_PORT_HTTP_SECNAMENODE. */
	public static final String DEFAULT_PORT_HTTP_SECNAMENODE = "50090";

	/**
	 * Configure node.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	public abstract boolean configureNode(NodeConfig nodeConfig);

	public abstract boolean setupPasswordlessSSH(Set<String> nodes);

	public abstract boolean updateAdvConfSetNodes();

	public abstract String getAgentProperties(NodeConfig nodeConfig)
			throws AnkushException;

	public boolean generateRsaKeysForHadoopNodes(Set<String> nodes) {
		try {
			for (String host : nodes) {
				if (!com.impetus.ankush2.utils.SSHUtils
						.generateRSAKeyFiles(this.clusterConfig.getNodes().get(
								host))) {
					HadoopUtils.addAndLogError(LOG, clusterConfig,
							"Could not create ssh keys file using command - " + HadoopConstants.Command.GENERATE_RSA_KEYS,
							Constant.Component.Name.HADOOP, host);
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(LOG, clusterConfig,
					"Could not create ssh keys",
					Constant.Component.Name.HADOOP, e);
			return false;
		}
	}

	/**
	 * Initialize and start cluster.
	 * 
	 * @return true, if successful
	 */
	public abstract boolean initializeAndStartCluster();

	public abstract boolean manageComponent(String action);

	/**
	 * Configure environment variables.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	public abstract boolean configureEnvironmentVariables(NodeConfig nodeConfig, boolean isAddNodeOperation);

	/**
	 * Configure hdfs site xml.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected abstract boolean configureHdfsSiteXml(NodeConfig nodeConfig);

	/**
	 * Configure core site xml.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected abstract boolean configureCoreSiteXml(NodeConfig nodeConfig);

	/**
	 * Configure mapred site xml.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected abstract boolean configureMapredSiteXml(NodeConfig nodeConfig);

	/**
	 * Configure ganglia metrics.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected abstract boolean configureGangliaMetrics(NodeConfig nodeConfig);

	/**
	 * Sets the hadoop conf dir.
	 */
	public abstract void setHadoopConfDir();

	public abstract void setHadoopScriptDir();

	public abstract void setHadoopBinDir();

	public abstract void setRpcPorts();
	
	public abstract Set<String> getHAServicesList();
	
	protected abstract void addNodeRolesToMap(NodeRolesMap nodeRolesMap);

	/**
	 * Configure slaves file.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	public boolean configureSlavesFile(NodeConfig nodeConfig, Set<String> slaves) {
		// configuring slaves file
		Result res = null;
		try {
			LOG.info("Updating Hadoop slaves file",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());

			String slavesFile = (String) compConfig
					.getAdvanceConfProperty(HadoopConstants.AdvanceConfKeys.DIR_CONF) // getConfDir()
					+ HadoopConstants.FileName.ConfigurationFile.SLAVES;// "slaves";
			AnkushTask clearFile = new ClearFile(slavesFile);
			res = nodeConfig.getConnection().exec(clearFile);
			if (!res.isSuccess) {
				HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
						"Could not clear slaves file contents",
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
			String slavesFileContent = StringUtils.join(slaves,
					Constant.Strings.LINE_SEPERATOR);

			AnkushTask appendSlaveFile = new AppendFileUsingEcho(
					slavesFileContent, slavesFile);
			res = nodeConfig.getConnection().exec(appendSlaveFile);
			if (!res.isSuccess) {
				HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
						"Could not update slaves file",
						Constant.Component.Name.HADOOP, nodeConfig.getHost());
				return false;
			}
			ConfigurationManager confManager = new ConfigurationManager();
			// Saving slaveFile property.
			confManager.saveConfiguration(this.clusterConfig.getClusterId(),
					this.clusterConfig.getCreatedBy(),
					HadoopConstants.FileName.ConfigurationFile.SLAVES,
					nodeConfig.getHost(), "Slaves List", slavesFileContent);

			return true;
		} catch (Exception e) {
			HadoopUtils.addAndLogError(this.LOG, this.clusterConfig,
					"Could not update slaves file.",
					Constant.Component.Name.HADOOP, nodeConfig.getHost());
			return false;
		}
	}

	/**
	 * Configure rack awareness.
	 * 
	 * @param nodeConfig
	 *            the node config
	 * @return true, if successful
	 */
	protected boolean configureRackAwareness(NodeConfig nodeConfig) {
		return true;
	}

	public boolean saveNodesWhileRegitration() throws Exception {
		NodeRolesMap nodeRolesMap = new NodeRolesMap();
		this.addNodeRolesToMap(nodeRolesMap);
		for (Map.Entry<String, Set<String>> entry : nodeRolesMap.entrySet()) {
			AnkushUtils.addNodeToClusterAndComponent(this.clusterConfig,
					entry.getKey(), entry.getValue(),
					Constant.Component.Name.HADOOP);
		}
		System.out.println("this.compConfig.getNodes() : " + this.compConfig.getNodes());
		return true;
	}

	
}
