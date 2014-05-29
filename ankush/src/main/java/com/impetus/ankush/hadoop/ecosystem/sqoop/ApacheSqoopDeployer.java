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
package com.impetus.ankush.hadoop.ecosystem.sqoop;

import java.util.List;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddToPathVariable;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.SetEnvVariable;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/*
 * Class implementing the deployment of Apache Sqoop on a cluster node
 * 
 *  @author Jayati
 */

/**
 * The Class ApacheSqoopDeployer.
 */
public class ApacheSqoopDeployer implements Deployable {

	/** The Constant LOG. */
	public static final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.SQOOP, ApacheSqoopDeployer.class);

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		SqoopConf conf = (SqoopConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Deploying Sqoop";
		final String node = conf.getMasterNode().getPublicIp();
		LOG.info(infoMsg);
		LOG.info(node, infoMsg);

		// Deploy component
		boolean status = this.deploy(conf);

		// Log & return result
		LOG.log(node, infoMsg, status);
		return status;
	}

	/**
	 * Deploy.
	 *
	 * @param sqoopConf the sqoop conf
	 * @return true, if successful
	 */
	private boolean deploy(SqoopConf sqoopConf) {
		SSHExec connection = null;
		try {
			LOG.debug("Connecting to the node ...............");
			// connect to remote node
			final NodeConf node = sqoopConf.getMasterNode();
			connection = SSHUtils.connectToNode(node.getPublicIp(),
					sqoopConf.getUsername(), sqoopConf.getPassword(),
					sqoopConf.getPrivateKey());

			Result res = null;
			// if connected
			if (connection != null) {

				LOG.debug("Create directory - "
						+ sqoopConf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						sqoopConf.getInstallationPath());
				connection.exec(mkInstallationPath);

				LOG.debug("Get and untar the file");
				// get and untar the file
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, sqoopConf, "sqoop");

				if(!isSuccessfull) {
					LOG.error(node, "Could not extract bundle file ");
					return false;
				}
				
				// if component exists at destination
				if (isSuccessfull) {

					LOG.debug("Setting SQOOP_HOME variable");
					// setting SQOOP_HOME in environment
					String envFile = "/etc/environment";
					String componentHome = sqoopConf.getInstallationPath()
							+ "sqoop-" + sqoopConf.getComponentVersion();
					// setting componentHome in bean
					sqoopConf.setComponentHome(componentHome);

					AnkushTask setSqoopHome = new SetEnvVariable(
							sqoopConf.getPassword(), "SQOOP_HOME",
							componentHome, envFile);
					res = connection.exec(setSqoopHome);

					if (!res.isSuccess) {
						LOG.error(node, "Could not set SQOOP_HOME "
								+ setSqoopHome);
						return false;
					}

					// setting HBASE_HOME in environment (if specified)
					if (sqoopConf.getHbaseHome() != null
							&& !sqoopConf.getHbaseHome().isEmpty()) {
						LOG.debug("Setting HBASE_HOME variable");
						AnkushTask setHbaseHome = new SetEnvVariable(
								sqoopConf.getPassword(), "HBASE_HOME",
								sqoopConf.getHbaseHome(), envFile);
						connection.exec(setHbaseHome);
					}

					// setting HIVE_HOME in environment (if specified)
					if (sqoopConf.getHiveHome() != null
							&& !sqoopConf.getHiveHome().isEmpty()) {
						LOG.debug("Setting HIVE_HOME variable");
						AnkushTask setHiveHome = new SetEnvVariable(
								sqoopConf.getPassword(), "HIVE_HOME",
								sqoopConf.getHiveHome(), envFile);
						connection.exec(setHiveHome);
					}

					LOG.debug("Setting SQOOP_HOME/bin in $PATH");
					// setting SQOOP_HOME/bin in PATH
					AnkushTask updatePath = new AddToPathVariable(componentHome
							+ "/bin", envFile, sqoopConf.getPassword());

					res = connection.exec(updatePath);
					if (!res.isSuccess) {
						LOG.error(node,
								"Could not set SQOOP_HOME/bin in $PATH ");
						return false;
					}

					LOG.debug("Deployment process completed ");
					return res.isSuccess;

				}
			} else {
				LOG.error(node, "Could not connect to node - " + node);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			// disconnect from the node/machine
			connection.disconnect();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		SqoopConf conf = (SqoopConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Undeploying Sqoop";
		final String node = conf.getMasterNode().getPublicIp();
		LOG.info(infoMsg);
		LOG.info(node, infoMsg);

		// Deploy component
		boolean status = this.undeploy(conf);

		// Log & return result
		LOG.log(node, infoMsg, status);
		return status;
	}

	/**
	 * Undeploy.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean undeploy(SqoopConf conf) {
		LOG.debug("Undeploying Apache Sqoop using configuration : " + conf);

		final String publicIp = conf.getMasterNode().getPublicIp();

		SSHExec connection = null;
		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// remove Sqoop folder
				AnkushTask removeComponent = new Remove(
						conf.getInstallationPath() + "sqoop-"
								+ conf.getComponentVersion());
				connection.exec(removeComponent);

				// remove SQOOP_HOME variable and SQOOP_HOME/bin from the $PATH
				return true;
			}
		} catch (Exception e) {
			LOG.error(conf.getMasterNode(), e.getMessage(), e);
			return false;
		} finally {
			connection.disconnect();
		}
		return false;

	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#startServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stopServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		return status(nodeList);
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
