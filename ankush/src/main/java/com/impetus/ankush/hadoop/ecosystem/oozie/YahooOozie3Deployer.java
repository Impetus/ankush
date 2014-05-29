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
package com.impetus.ankush.hadoop.ecosystem.oozie;

import java.util.List;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.AddToPathVariable;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.EcosystemServiceUtil;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;

/**
 * The Class YahooOozie3Deployer.
 *
 * @author mayur
 */
public class YahooOozie3Deployer implements Deployable {

	/** The Constant LOG. */
	public static final AnkushLogger LOG = new AnkushLogger(Constant.Component.Name.OOZIE,
			YahooOozie3Deployer.class);

	/** The oozie services name. */
	private static String oozieServicesName = new String("oozie.services");

	/** The oozie services value. */
	private static StringBuffer oozieServicesValue = new StringBuffer()
			.append("org.apache.oozie.service.SchedulerService,")
			.append("org.apache.oozie.service.InstrumentationService,")
			.append("org.apache.oozie.service.CallableQueueService,")
			.append("org.apache.oozie.service.UUIDService,")
			.append("org.apache.oozie.service.ELService,")
			.append("org.apache.oozie.service.AuthorizationService,")
			.append("org.apache.oozie.service.HadoopAccessorService,")
			.append("org.apache.oozie.service.MemoryLocksService,")
			.append("org.apache.oozie.service.DagXLogInfoService,")
			.append("org.apache.oozie.service.SchemaService,")
			.append("org.apache.oozie.service.LiteWorkflowAppService,")
			.append("org.apache.oozie.service.JPAService,")
			.append("org.apache.oozie.service.StoreService,")
			.append("org.apache.oozie.service.CoordinatorStoreService,")
			.append("org.apache.oozie.service.SLAStoreService,")
			.append("org.apache.oozie.service.DBLiteWorkflowStoreService,")
			.append("org.apache.oozie.service.CallbackService,")
			.append("org.apache.oozie.service.ActionService,")
			.append("org.apache.oozie.service.ActionCheckerService,")
			.append("org.apache.oozie.service.RecoveryService,")
			.append("org.apache.oozie.service.PurgeService,")
			.append("org.apache.oozie.service.CoordinatorEngineService,")
			.append("org.apache.oozie.service.BundleEngineService,")
			.append("org.apache.oozie.service.DagEngineService,")
			.append("org.apache.oozie.service.CoordMaterializeTriggerService,")
			.append("org.apache.oozie.service.StatusTransitService,");

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		OozieConf conf = (OozieConf) config;		
		LOG.setLoggerConfig(conf);
		// Logging information 
		final String infoMsg = "Deploying Oozie";		
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
	private boolean deploy(OozieConf conf) {
		LOG.debug("Deploying Oozie using configuration : " + conf);

		SSHExec connection = null;
		Result res = null;
		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(conf.getNode().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {

				LOG.debug("Create directory - " + conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);
				if (!res.isSuccess) {
					LOG.error(conf.getNode(), "Could not create installation directory " + conf.getInstallationPath());
					return false;
				}

				LOG.debug("Get and extract tarball");
				// get and extract tarball
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(
						connection, conf, "oozie");
				
				if(!isSuccessfull) {
					LOG.error(conf.getNode(), "Could not extract bundle file ");
					return false;
				}
				// if component exists at destination
				if (isSuccessfull) {
					String componentHome = FileUtils.getSeparatorTerminatedPathEntry(conf.getInstallationPath())
							+ "oozie-" + conf.getComponentVersion();
					// setting componentHome in bean
					conf.setComponentHome(componentHome);

					// Execute commands
//					CustomTask createGroupAndUser = new ExecSudoCommand(
//							conf.getPassword(), "groupadd oozie",
//							"useradd -g oozie oozie");

					// execute chmod command
//					CustomTask changeOwnership = new ExecSudoCommand(
//							conf.getPassword(), "chown -R oozie:oozie "
//									+ componentHome);
					
					
					// version specific handling
					// for version 3.0.0 - BundlePauseStartService
					if (conf.getComponentVersion().equals("3.0.0")) {
						oozieServicesValue
								.append("org.apache.oozie.service.BundlePauseStartService");
					}
					// for version 3.0.2 - PauseTransitService
					if (conf.getComponentVersion().equals("3.0.2")) {
						oozieServicesValue
								.append("org.apache.oozie.service.PauseTransitService");
					}

					String logDir = FileUtils.getSeparatorTerminatedPathEntry(conf.getComponentHome()) + "logs/";
					AnkushTask mkLogDir = new MakeDirectory(logDir);
					res = connection.exec(mkLogDir);
					if (!res.isSuccess) {
						LOG.error(conf.getNode(), "Could not create log directory " + logDir);
						return false;
					}
					
					// configuring oozie-site.xml
					AnkushTask configureOozie = new AddConfProperty(
							oozieServicesName, oozieServicesValue.toString(),
							componentHome + "/conf/oozie-site.xml",Constant.File_Extension.XML);

					// configuring hdfs-site.xml
					AnkushTask configureHdfsHosts = new AddConfProperty(
							"hadoop.proxyuser." + conf.getUsername() + ".hosts", "*",
							conf.getHadoopConf() + "hdfs-site.xml",Constant.File_Extension.XML);

					AnkushTask configureHdfsGroups = new AddConfProperty(
							"hadoop.proxyuser." + conf.getUsername() + ".groups", "*",
							conf.getHadoopConf() + "hdfs-site.xml",Constant.File_Extension.XML);

					// building oozie war
					String oozieSteupCommand = componentHome
							+ "/bin/oozie-setup.sh -hadoop "
							+ conf.getHadoopVersion() + " "
							+ conf.getHadoopHome();
					CustomTask setupOozie = new ExecSudoCommand(
							conf.getPassword(), oozieSteupCommand);

					// create oozie group and user
//					Result result = connection.exec(createGroupAndUser);
//					if (!result.isSuccess) {
//						if (!result.error_msg.contains("already exists")) {
//							// if oozie user/group already exists then ignore
//							// this error
//							LOG.error(conf.getNode(), "Not able to create Oozie user and group");
//							return false;
//						}
//					}
					
					LOG.debug("Setting OOZIE_HOME/bin in $PATH");
					// setting OOZIE_HOME/bin in PATH
					String envFile = "/etc/environment";
					AnkushTask updatePath = new AddToPathVariable(componentHome
							+ "/bin", envFile, conf.getPassword());

					
					HadoopConf hConf = (HadoopConf) conf.getClusterConf().getClusterComponents().get(Constant.Component.Name.HADOOP);
					if(hConf == null) {
						hConf = (HadoopConf) conf.getClusterConf().getClusterComponents().get(Constant.Component.Name.HADOOP2);
					}
					String oozieEnvFile = componentHome + "/conf/oozie-env.sh";
					
					String javaHome = hConf.getJavaHome(conf.getNode().getPublicIp()) ;
					String appendJavaHomeTxt = "export JAVA_HOME=" + javaHome;
					AnkushTask updateJavaHome = new AppendFile(appendJavaHomeTxt, oozieEnvFile, conf.getPassword()); 
					res = connection.exec(updateJavaHome);
					if (!res.isSuccess) {
						LOG.error(conf.getNode(), "Could not set JAVA_HOME in oozie-env.sh file.");
						return false;
					}
					
					// execute steps in chain...
					if (connection.exec(configureOozie).isSuccess
							&& connection.exec(configureHdfsHosts).isSuccess
							&& connection.exec(configureHdfsGroups).isSuccess
							&& connection.exec(setupOozie).isSuccess
//							&& connection.exec(changeOwnership).isSuccess
							&& connection.exec(updatePath).isSuccess) {
						return true;
					} else {
						LOG.error(conf.getNode(), "Could not complete Oozie installation.");
						return false;
					}
				}
			} else {
				LOG.error(conf.getNode(), "Could not connect to node.");
			}
		} catch (Exception e) {
			LOG.error(conf.getNode(), e.getMessage(), e);
			return false;
		} finally {
			// disconncet to node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		OozieConf conf = (OozieConf) config;		
		LOG.setLoggerConfig(conf);
		// Logging information 
		final String infoMsg = "Undeploying Oozie";		
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
	private boolean undeploy(OozieConf conf) {
		LOG.debug("Undeploying Yahoo Oozie using configuration : " + conf);

		SSHExec connection = null;
		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(conf.getNode().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				// remove component folder
				AnkushTask removeComponent = new Remove(conf.getComponentHome());
				AnkushTask remove = new ExecSudoCommand(conf.getPassword(),
						removeComponent.getCommand());
				connection.exec(remove);

				return true;
			}
		} catch (Exception e) {
			LOG.error(conf.getNode(), e.getMessage(), e);
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
		return executeAction(config, "start");
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		return executeAction(config, "stop");
	}

	/**
	 * Execute action.
	 *
	 * @param config the config
	 * @param action the action
	 * @return true, if successful
	 */
	private boolean executeAction(Configuration config, String action) {
		OozieConf conf = (OozieConf) config;
		LOG.setLoggerConfig(conf);
		final String publicIp = conf.getNode().getPublicIp();
		
		LOG.debug("Performing  [" + action
				+ "]action Oozie using configuration : " + conf);
		
		String message = action + "ing Oozie server...";
		
		LOG.info(publicIp, message);
				
		SSHExec connection = null;
		try {
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp,
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			if (connection != null) {
				boolean result = EcosystemServiceUtil.manageOozie3Server(connection, conf, action);
				if(!result) {
					LOG.error(conf.getNode(), "Could not start Oozie server.");
				} else {
					LOG.info(conf.getNode().getPublicIp() , message + " done...");
				}
				return result;
				
			} else {
				LOG.error(conf.getNode(), "Could not connect to the node.");
				return false;
			}
		} catch (Exception e) {
			LOG.error(conf.getNode(), e.getMessage(), e);
			return false;
		} finally {
			// disconncet to node/machine
			connection.disconnect();
		}
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
