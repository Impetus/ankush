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
package com.impetus.ankush.hadoop.ecosystem.mahout;

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
 * Class implementing the deployment of Apache Mahout on a cluster node
 * 
 *  @author Jayati
 */

/**
 * The Class ApacheMahoutDeployer.
 */
public class ApacheMahoutDeployer implements Deployable {

	/** The Constant MAHOUT_DISTRIBUTION. */
	private static final String MAHOUT_DISTRIBUTION = "mahout-distribution-";
	
	/** The Constant LOG. */
	public static final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.MAHOUT, ApacheMahoutDeployer.class);

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		MahoutConf conf = (MahoutConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Deploying Mahout";
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
	 * @param mahoutConf the mahout conf
	 * @return true, if successful
	 */
	private boolean deploy(MahoutConf mahoutConf) {
		SSHExec connection = null;
		final NodeConf node = mahoutConf.getMasterNode();
		final String publicIp = node.getPublicIp();
		Result res = null;

		try {
			LOG.debug("Connecting to the node...");
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp,
					mahoutConf.getUsername(), mahoutConf.getPassword(),
					mahoutConf.getPrivateKey());

			// if connected
			if (connection != null) {

				LOG.debug("Create directory - "
						+ mahoutConf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						mahoutConf.getInstallationPath());
				connection.exec(mkInstallationPath);

				LOG.debug("Get and unzip the file");
				// get and unzip the file
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, mahoutConf, "mahout-distribution");

				if(!isSuccessfull) {
					LOG.error(node, "Could not extract bundle file ");
					return false;
				}
				// setting mahout home to configuration
				String componentHome = mahoutConf.getInstallationPath()
						+ MAHOUT_DISTRIBUTION
						+ mahoutConf.getComponentVersion();
				mahoutConf.setComponentHome(componentHome);

				// if component exists at destination
				if (isSuccessfull) {

					LOG.debug("Setting MAHOUT_HOME variable");
					// setting MAHOUT_HOME in environment

					String envFile = "/etc/environment";

					AnkushTask setMahoutHome = new SetEnvVariable(
							mahoutConf.getPassword(), "MAHOUT_HOME",
							mahoutConf.getInstallationPath()
									+ MAHOUT_DISTRIBUTION
									+ mahoutConf.getComponentVersion(), envFile);
					res = connection.exec(setMahoutHome);

					if (!res.isSuccess) {
						LOG.error(node, "Could not set MAHOUT_HOME "
								+ setMahoutHome);
						return false;
					}

					LOG.debug("Setting MAHOUT_HOME/bin in $PATH");
					// setting MAHOUT_HOME/bin in PATH
					AnkushTask updatePath = new AddToPathVariable(
							mahoutConf.getInstallationPath()
									+ MAHOUT_DISTRIBUTION
									+ mahoutConf.getComponentVersion() + "/bin",
							envFile, mahoutConf.getPassword());
					res = connection.exec(updatePath);

					if (!res.isSuccess) {
						LOG.error(node,
								"Could not set MAHOUT_HOME/bin in $PATH");
						return false;
					}

					LOG.debug("Deployment process completed ");

					return res.isSuccess;
				}
			} else {
				LOG.error(node, "Could not connect to node - " + node);
			}
		} catch (Exception e) {
			LOG.error(node, e.getMessage(), e);
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
		MahoutConf conf = (MahoutConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Undeploying Mahout";
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
	private boolean undeploy(MahoutConf conf) {
		LOG.debug("Undeploying Apache Mahout using configuration : " + conf);
		SSHExec connection = null;
		final String publicIp = conf.getMasterNode().getPublicIp();

		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// remove mahout folder
				AnkushTask removeComponent = new Remove(
						conf.getInstallationPath() + MAHOUT_DISTRIBUTION
								+ conf.getComponentVersion());
				connection.exec(removeComponent);

				// remove MAHOUT_HOME variable and MAHOUT_HOME/bin from the
				// $PATH
				return true;
			}
		} catch (Exception e) {
			LOG.error(publicIp, e.getMessage(), e);
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
		// TODO Auto-generated method stub
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stopServices(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
		return status(nodeList);
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		// TODO Auto-generated method stub
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
