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
package com.impetus.ankush.hadoop.ecosystem.flume;

import java.util.List;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddToPathVariable;
import com.impetus.ankush.common.scripting.impl.Copy;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;

/**
 * The Class ApacheFlumeNgDeployer.
 *
 * @author mayur
 */
public class ApacheFlumeNgDeployer implements Deployable {

	/** The Constant LOG. */
	public final static AnkushLogger LOG = new AnkushLogger(Constant.Component.Name.FLUME,
			ApacheFlumeNgDeployer.class);

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		FlumeConf conf = (FlumeConf) config;		
		LOG.setLoggerConfig(conf);
		// Logging information 
		final String infoMsg = "Deploying Flume";		
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
	private boolean deploy(FlumeConf conf) {		
		LOG.debug("Deploying Apache Flume using configuration : " + conf);
		final NodeConf node = conf.getNode();
		final String publicIp = node.getPublicIp();
		
		SSHExec connection = null;
		try {
			LOG.debug("Connecting with node");
			// connect to remote node			
			connection = SSHUtils.connectToNode(publicIp,
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			Result res = null;
			// if connected
			if (connection != null) {
				LOG.debug("Create directory - " + conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(node, "Could not create installation directory" + mkInstallationPath);
					return false;
				}

				LOG.debug("Get and extract tarball");
				// get and extract tarball
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, conf, "flume");


				if(!isSuccessfull) {
					LOG.error(node, "Could not extract bundle file ");
					return false;
				}
				
				// if component exists at destination
				if (isSuccessfull) {

					// business logic here ...
					LOG.debug("Setting flumeng/bin in $PATH");
					// setting HIVE_HOME/bin in PATH
					String componentHome = conf.getInstallationPath()
							+ "apache-flume-" + conf.getComponentVersion();
					// setting componentHome in bean
					conf.setComponentHome(componentHome);

					AnkushTask updatePath = new AddToPathVariable(componentHome
							+ "/bin", Constant.ETCENV_FILE, conf.getPassword());
					res = connection.exec(updatePath);

					if (res.isSuccess) {
						LOG.debug("coping conf/flume-env and conf/flume.conf");
						// cp flume.conf file
						AnkushTask copyFlumeConf = new Copy(componentHome
								+ "/conf/flume-conf.properties.template",
								componentHome + "/conf/flume.conf");
						// cp flume-env.sh file
						// conf/flume-env.sh.template conf/flume-env.sh
						AnkushTask copyFlumeEnv = new Copy(componentHome
								+ "/conf/flume-env.sh.template", componentHome
								+ "/conf/flume-env.sh");

						// not a must requirement to run flumeng, so ignoring
						// response code
						connection.exec(copyFlumeConf);
						connection.exec(copyFlumeEnv);

						return true;
					} else {
						LOG.error(node, "Couldn't set flumeng in PATH");
						return false;
					}
				}
			} else {
				LOG.error(node, "Could not connect to node - " + node);
			}
		} catch (Exception e) {
			LOG.error(publicIp, e.getMessage(), e);
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
		FlumeConf conf = (FlumeConf) config;		
		LOG.setLoggerConfig(conf);
		// Logging information 
		final String infoMsg = "Undeploying Flume";		
		final String node = conf.getNode().getPublicIp();		
		LOG.info(infoMsg);
		LOG.info(node, infoMsg);
		
		// Undeploy component
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
	private boolean undeploy(FlumeConf conf) {
		LOG.debug("Undeploying Apache Flume using configuration : " + conf);
		final NodeConf node = conf.getNode();
		final String publicIp = node.getPublicIp();

		SSHExec connection = null;
		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp,
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// remove Home folder
				AnkushTask ankushTask = new Remove(conf.getComponentHome());
				if(connection.exec(ankushTask).isSuccess) {
					LOG.error("Unable to remove FLUME_HOME directory from the node");
				}
				return true;
			}
		} catch (Exception e) {
			LOG.error(node, e.getMessage(), e);
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
