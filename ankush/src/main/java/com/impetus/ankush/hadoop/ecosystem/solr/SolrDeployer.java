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
package com.impetus.ankush.hadoop.ecosystem.solr;

import java.util.List;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillProcessOnPort;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.EcosystemServiceUtil;

/**
 * The Class SolrDeployer.
 *
 * @author mayur
 */
public class SolrDeployer implements Deployable {

	/** The Constant CONNECTING_WITH_NODE. */
	private static final String CONNECTING_WITH_NODE = "Connecting with node";
	
	/** The Constant LOG. */
	public final static AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.SOLR, SolrDeployer.class);

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		SolrConf conf = (SolrConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Deploying Solr";
		final String node = conf.getNode().getPublicIp();
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
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean deploy(SolrConf conf) {
		LOG.debug("Deploying Solr using configuration : " + conf);

		SSHExec connection = null;
		Result res = null;
		
		final NodeConf node = conf.getNode();
		final String publicIp = node.getPublicIp();

		try {
			LOG.debug(CONNECTING_WITH_NODE);
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// setting solr home to configuration
				String componentHome = conf.getInstallationPath()
						+ "apache-solr-" + conf.getComponentVersion();
				conf.setComponentHome(componentHome);

				LOG.debug("Create directory - " + conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);
				if (!res.isSuccess) {
					LOG.error(node,
							"Could not create installation directory" + mkInstallationPath);
					return false;
				}

				LOG.debug("Get and extract zip");
				// get and extract tarball
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, conf, "solr");

				// if component exists at destination
				if (isSuccessfull) {
					LOG.debug("Solr extracted in destination folder ");
					return true;
				} else {
					LOG.error(node, "Could not extract bundle file");
					return false;
				}
			} else {
				LOG.error(node,
						"Could not connect to node - " + node);
			}
		} catch (Exception e) {
			LOG.error(node, e.getMessage(), e);
			return false;
		} finally {
			// disconncet to node/machine
			connection.disconnect();
		}
		return false;

	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		SolrConf conf = (SolrConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Undeploying Solr";
		final String node = conf.getNode().getPublicIp();
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
	private boolean undeploy(SolrConf conf) {
		LOG.debug("Undeploying Apache Solr using configuration : " + conf);

		SSHExec connection = null;
		try {
			LOG.debug(CONNECTING_WITH_NODE);
			// connect to remote node
			connection = SSHUtils.connectToNode(conf.getNode().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// remove component folder
				AnkushTask removeComponent = new Remove(
						conf.getInstallationPath() + "apache-solr-"
								+ conf.getComponentVersion());
				connection.exec(removeComponent);

				return true;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
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
		SolrConf conf = (SolrConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Starting Solr";
		final String node = conf.getNode().getPublicIp();
		LOG.info(infoMsg);
		LOG.info(node, infoMsg);

		// Deploy component
		boolean status = this.start(conf);

		// Log & return result
		LOG.log(node, infoMsg, status);
		return status;
	}

	/**
	 * Start.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean start(SolrConf conf) {
		LOG.debug("Deploying Solr using configuration : " + conf);

		SSHExec connection = null;
		final NodeConf node = conf.getNode();
		final String publicIp = node.getPublicIp();

		try {
			LOG.debug(CONNECTING_WITH_NODE);
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {
				return EcosystemServiceUtil.manageSolr(connection, conf, Constant.Hadoop.Command.START);
			} else {
				LOG.error(node,
						"Could not connect to node - " + node);
			}
		} catch (Exception e) {
			LOG.error(node, e.getMessage(), e);
			return false;
		} finally {
			// disconncet to node/machine
			connection.disconnect();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		SolrConf conf = (SolrConf) config;
		LOG.debug("Deploying Solr using configuration : " + conf);
		LOG.setLoggerConfig(conf);
		SSHExec connection = null;
		
		try {
			LOG.debug(CONNECTING_WITH_NODE);
			// connect to remote node
			connection = SSHUtils.connectToNode(conf.getNode().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				return EcosystemServiceUtil.manageSolr(connection, conf, Constant.Hadoop.Command.STOP);
			} else {
				LOG.debug("Could not connect to node - " + conf.getNode());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			// disconncet to node/machine
			connection.disconnect();
		}
		return false;
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
		boolean status = false;
		for (NodeConf nodeConf : keySet) {
			status = nodeConf.getStatus() || status;
		}
		return status;
	}

}
