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
package com.impetus.ankush.hadoop.ecosystem.hive;

import java.util.List;

import org.apache.commons.io.FilenameUtils;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddToPathVariable;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Move;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.SetEnvVariable;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.common.utils.XmlUtil;
import com.impetus.ankush.hadoop.HadoopUtils;

/**
 * The Class ApacheHiveDeployer.
 *
 * @author mayur
 */
public class ApacheHiveDeployer implements Deployable {

	/** The log. */
	private final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.HIVE, ApacheHiveDeployer.class);

	// Configuration manager to save the property file change records.
	/** The conf manager. */
	private ConfigurationManager confManager = new ConfigurationManager();
		
	private final static String FILENAME_HIVE_ENV = "hive-env.sh";
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		HiveConf conf = (HiveConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Deploying Hive";
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
	private boolean deploy(HiveConf conf) {
		LOG.debug("Deploying Apache Hive using configuration : " + conf);

		SSHExec connection = null;
		Result res = null;
		NodeConf node = conf.getNode();
		final String publicIp = node.getPublicIp();

		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {

				LOG.debug("Create directory - " + conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(
							node,
							"Could not create installation directory "
									+ conf.getInstallationPath());
					return false;
				}

				LOG.debug("Get and extract tarball");
				// get and extract tarball
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, conf, "hive");

				if(!isSuccessfull) {
					LOG.error(node, "Could not extract bundle file ");
					return false;
				}
				
				// if component exists at destination
				if (isSuccessfull) {
					String componentHome = conf.getInstallationPath() + "hive-"
							+ conf.getComponentVersion();
					// setting componentHome in bean
					conf.setComponentHome(componentHome);

					LOG.debug("Setting HIVE_HOME variable");
					// setting HIVE_HOME in environment
										AnkushTask setHiveHome = new SetEnvVariable(
							conf.getPassword(), "HIVE_HOME", componentHome,
							Constant.ETCENV_FILE);
					res = connection.exec(setHiveHome);

					if (!res.isSuccess) {
						LOG.error(node, "Could not set HIVE_HOME "
								+ componentHome);
						return false;
					}
					
					confManager.saveConfiguration(conf.getClusterDbId(),
							conf.getCurrentUser(), Constant.ETCENV_FILE, node.getPublicIp(),
							"HIVE_HOME", conf.getComponentHome());
					

					LOG.debug("Setting HIVE_HOME/bin in $PATH");
					// setting HIVE_HOME/bin in PATH
					AnkushTask updatePath = new AddToPathVariable(componentHome
							+ "/bin", Constant.ETCENV_FILE, conf.getPassword());
					res = connection.exec(updatePath);
					if (!res.isSuccess) {
						LOG.error(node, "Could not set " + componentHome
								+ "/bin in PATH");
						return false;
					}

					LOG.debug("Setting HADOOP_HOME in hive-env.sh");
					String hadoopHomeEnv = "HADOOP_HOME=" + conf.getHadoopHome();
					String fileHiveEnv = conf.getComponentHome() + "/conf/" + FILENAME_HIVE_ENV;
					
					AnkushTask moveFile = new Move(fileHiveEnv + ".template", fileHiveEnv);
					res = connection.exec(moveFile);
					if (!res.isSuccess) {
						LOG.error(node, "Could not create " + fileHiveEnv + " file.");
						return false;
					}
					
					// setting HADOOP_HOME in hive-env.sh
					AnkushTask updateHiveEnv = new AppendFile(hadoopHomeEnv, fileHiveEnv);
					res = connection.exec(updateHiveEnv);
					if (!res.isSuccess) {
						LOG.error(node, "Could not set HADOOP_HOME in " + FILENAME_HIVE_ENV + " file.");
						return false;
					}
					confManager.saveConfiguration(conf.getClusterDbId(),
							conf.getCurrentUser(), FilenameUtils.getName(fileHiveEnv), node.getPublicIp(),
							"HADOOP_HOME", conf.getHadoopHome());
					
					LOG.debug("Create hive-site.xml");
					// hive-site.xml doesn't exists... creating it...
					String filePath = componentHome + "/conf/hive-site.xml";
					boolean status;
					status = XmlUtil
							.createConfiguratonXml(filePath, connection);

					LOG.debug("Configuring advanced settings");
					// advanced configuration handling
					if (status) {
						Thread.sleep(2000); // Wait 2 second for File Creation on Disk 
						for(String propertyName : conf.getAdvancedConf().keySet()) {
							Object propValue = conf.getAdvancedConf().get(propertyName);
							status = AgentUtils.addPropertyToFile(conf, connection, node, filePath, propertyName, String.valueOf(propValue));
							if (!status) {
								return false;
							}	
						}
					} else {
						LOG.error(node,
								"Could not create hive-site.xml file");
						return false;
					}

					LOG.debug("Creating required folder in hdfs");
					// creating required folders in hdfs
					String commonCmd = conf.getHadoopHome() + "bin/hadoop fs ";
					String makeTmp = commonCmd + "-mkdir -p /tmp";
					String makeWarehouse = commonCmd
							+ "-mkdir -p /user/hive/warehouse";
					String chmodTmp = commonCmd + "-chmod g+w  /tmp";
					String chmodWarehouse = commonCmd
							+ "-chmod g+w  /user/hive/warehouse";
					CustomTask hdfsCmds = new ExecCommand(makeTmp,
							makeWarehouse, chmodTmp, chmodWarehouse);
					Result result = connection.exec(hdfsCmds);
					if(!result.isSuccess) {
						LOG.error(node, "Could not deploy hive on node.");
						LOG.debug("Hive deployment failed.");
						return false;
					} else {
						LOG.debug("Hive deployment completed.");
						return true;	
					}	
				}
			}
			else {
				LOG.error(node, "Could not connect to node.");
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
		HiveConf conf = (HiveConf) config;
		LOG.setLoggerConfig(conf);
		// Logging information
		final String infoMsg = "Undeploying Hive";
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
	private boolean undeploy(HiveConf conf) {
		LOG.debug("Undeploying Apache Hive using configuration : " + conf);

		SSHExec connection = null;
		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(conf.getNode().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// remove hive folder
				AnkushTask removeComponent = new Remove(
						conf.getInstallationPath() + "hive-"
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
