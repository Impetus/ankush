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

package com.impetus.ankush.hadoop.ecosystem.hadoop1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.Copy;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.PrependFile;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.RemoveText;
import com.impetus.ankush.common.scripting.impl.SetEnvVariable;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.HadoopRackAwareness;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.config.Parameter;

// TODO: Auto-generated Javadoc
/**
 * The Class Hadoop1Deployer.
 * 
 * @author Akhil
 */
public class Hadoop1Deployer implements Deployable {

	/** The ankushConf Reader. */
	ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();

	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(Constant.Component.Name.HADOOP,
			Hadoop1Deployer.class);

	/** The Constant COMPONENT_HADOOP. */
	private static final String COMPONENT_HADOOP = "hadoop";

	/** The Constant COMPONENT_FOLDER_BIN. */
	public static final String COMPONENT_FOLDER_BIN = "bin/";

	public static final String RELPATH_COMPONENT_FOLDER_CAPACITYJAR = "contrib/capacity-scheduler/";
	
	/** The Constant COMPONENT_FOLDER_CONF. */
	public static final String RELPATH_COMPONENT_FOLDER_CONF = "conf/";

	/** The Constant STR_VERSION. */
	private static final String STR_VERSION = "$VERSION";

	/** The Constant STR_SLASH. */
	private static final String STR_SLASH = "/";

	/** The Constant DEFAULT_PORT_HTTP_NAMENODE. */
	public static final String DEFAULT_PORT_HTTP_NAMENODE = "50070";
	
	/** The Constant DEFAULT_PORT_HTTP_JOBTRACKER. */
	public static final String DEFAULT_PORT_HTTP_JOBTRACKER = "50030";
	
	/** The Constant DEFAULT_PORT_HTTP_TASKTRACKER. */
	public static final String DEFAULT_PORT_HTTP_TASKTRACKER = "50060";
	
	/** The Constant DEFAULT_PORT_HTTP_DATANODE. */
	public static final String DEFAULT_PORT_HTTP_DATANODE = "50075";

	/** The Constant DEFAULT_PORT_HTTP_SECNAMENODE. */
	public static final String DEFAULT_PORT_HTTP_SECNAMENODE = "50090";

	/** The Constant DEFAULT_PORT_RPC_NAMENODE. */
	public static final String DEFAULT_PORT_RPC_NAMENODE = "9000";
	
	/** The Constant HADOOP_JAR_PATH_LOCATION. */
	private static final String HADOOP_JAR_PATH_LOCATION = ".ankush/agent/libs/compdep/";

	// Configuration manager to save the property file change records.
	/** The conf manager. */
	private ConfigurationManager confManager = new ConfigurationManager();

	/** The Constant hadoop string. */
	private static final String STR_HADOOP = "hadoop-";

	/** The add nodes. */
	private boolean addNodes = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		final HadoopConf conf = (HadoopConf) config;

		final String infoMsg = "Deploying Hadoop";
		final String node = conf.getNamenode().getPublicIp();

		LOG.setLoggerConfig(conf);

		LOG.info(infoMsg);

		// Step1: Setup passwordlessSSH
		LOG.info("Setting up Passwordless SSH from Hadoop Namenode across all slave nodes...");

		NodeConf namenode = conf.getNamenode();
		boolean passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, namenode, new HashSet<NodeConf>(conf.getCompNodes()));
		if (!passwordlessStatus) {
			LOG.error(node, "Could not set Passwordless SSH in cluster");
			return false;
		}

		HashSet<NodeConf> nodeConfs = new HashSet<NodeConf>();
		nodeConfs.add(conf.getNamenode());
		for (NodeConf nodeTmp : conf.getSlaves()) {
			if (HadoopUtils.validateNodeAdditionToSet(nodeConfs, nodeTmp)) {
				nodeConfs.add(nodeTmp);
			}
		}
		if (HadoopUtils.validateNodeAdditionToSet(nodeConfs,
				conf.getSecondaryNamenode())) {
			nodeConfs.add(conf.getSecondaryNamenode());
		}

		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeConfs) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = createNode(nodeConf, conf);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeConfs.size());
			status = HadoopUtils.status(nodeConfs);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		LOG.log(node, infoMsg, status);
		return status;
	}

	private String getValidInstallationPath(String installationPath) {
		if (installationPath.contains(STR_VERSION)) {
			installationPath = FilenameUtils
					.getFullPath(installationPath);
		}
		if (installationPath.contains(STR_VERSION)) {
			installationPath = installationPath
					.replace(STR_SLASH + STR_VERSION, "");
		}
		return installationPath;
	}
	
	private String getComponentHomeValue(HadoopConf conf) {
		String compHome = com.impetus.ankush.common.utils.FileUtils
		.getSeparatorTerminatedPathEntry(conf.getInstallationPath())
		+ STR_HADOOP + conf.getComponentVersion();
		return FileUtils.getSeparatorTerminatedPathEntry(compHome);
	}
	/**
	 * Creates the node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean createNode(NodeConf nodeConf, HadoopConf conf) {
		SSHExec connection = null;

		String username = conf.getUsername();
		String publicIp = nodeConf.getPublicIp();
		
		LOG.info(publicIp, "Installing Hadoop...");
		try {
			LOG.debug("Connecting with node..");

			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp, username,
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {
				LOG.info(publicIp, "Deploying Hadoop on " + publicIp + " ...");

				// Add HadoopConf Info in agent.properties file.
				if (!HadoopUtils.addHadoopAgentInfo(nodeConf, conf, connection)) {
					return false;
				}

				if(!this.configureHadoopDirs(nodeConf, conf, connection)) {
					return false;
				}
				
				// get and extract tarball
				LOG.debug("Get and extract tarball");
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(connection, conf, COMPONENT_HADOOP);

				// if Extraction is successful
				if (isSuccessfull) {
					LOG.info(publicIp, "Configuring Hadoop parameters...");

					if(!this.configureEnvVariables(nodeConf, conf, connection)) {
						return false;
					}
					
					if(!this.configureHadoopConfFiles(nodeConf, conf, connection)) {
						return false;
					}
					
					boolean status = true;

					// Configuring Rack Awareness
					if (conf.isRackEnabled()) {
						boolean isRackSetupSuccess = HadoopUtils
								.setupRackAwareness(connection, nodeConf, conf);
						if (!isRackSetupSuccess) {
							LOG.error(nodeConf,
									"Could not setup rack awareness for hadoop");
							return false;
						}
					}
					
					status = this.updateMasterSlavesConfFile(connection, nodeConf, conf);
					if (!status) {
						nodeConf.setStatus(false);
						return false;
					}
					
					// if node is namenode
					if (conf.isNamenode(nodeConf)) {
						try {
							// Copying Hadoop Jar to Agent

							copyHadoopJars(connection, conf);

							// Adding Job Monitor entry in agent taskable conf
							// file
							HadoopUtils
									.addClassEntry(connection, conf, Constant.TaskableClass.Hadoop_JOB_STATUS_MONITOR);
							
							LOG.info(publicIp, "Formatting Hadoop namenode..");

							String formatCommand = "yes | " + conf.getComponentHome()
									+ STR_SLASH + COMPONENT_FOLDER_BIN
									+ "hadoop "
									+ Constant.Hadoop.Command.NAMENODE_FORMAT;

							// format namenode
							CustomTask formatNamenode = new ExecCommand(
									formatCommand);
							Result res = connection.exec(formatNamenode);
							if (!res.isSuccess) {
								LOG.error(nodeConf,
										"Could not format NameNode...");
								return false;
							}
						} catch (Exception e) {
							LOG.error(nodeConf,
									"Failed to copy hadoop jars in agent..");
						}
					}
	
					LOG.info(publicIp, "Installing Hadoop Done..");
					return true;
				} else {
					nodeConf.setStatus(false);
					LOG.error(nodeConf, "Could not extract tarball");
					return false;
				}
			} else {
				nodeConf.setStatus(false);
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			nodeConf.setStatus(false);
			LOG.error(publicIp, "Error in deploying hadoop..");
			LOG.debug(e.getMessage());
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	/**
	 * Updates Masters files to Existing Nodes during Add / Remove Nodes
	 * operation.
	 * 
	 * @param connection
	 *            the node connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 * @throws TaskExecFailException
	 *             the task exec fail exception
	 */
	private boolean updateMastersFile(SSHExec connection, NodeConf nodeConf,
			HadoopConf conf) throws TaskExecFailException {
		LOG.debug("Updating Hadoop conf/masters file");
		// configuring masters file
		Result res = null;
		try {
			List<String> masterList = new ArrayList<String>();
			for (NodeConf snnNode : conf.getExpectedNodesAfterAddOrRemove()) {
				HadoopNodeConf hNode = (HadoopNodeConf) snnNode;
				if (hNode.getSecondaryNameNode()) {
					masterList.add(snnNode.getPrivateIp());
				}
			}
			String mastersFile = conf.getHadoopConfDir()
					+ Constant.Hadoop.FileName.MASTERS;// "masters";
			AnkushTask clearFile = new ClearFile(mastersFile);
			res = connection.exec(clearFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not clear master file contents");
				return false;
			}
			String mastersFileContent = StringUtils.join(masterList,
					Constant.LINE_SEPERATOR);
			AnkushTask appendMasterFile = new AppendFile(mastersFileContent,
					mastersFile);
			res = connection.exec(appendMasterFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not add nodes information to masters file");
				return false;
			}
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(), Constant.Hadoop.FileName.MASTERS,
					nodeConf.getPublicIp(), "ipValues", mastersFileContent);

			return true;
		} catch (Exception e) {
			LOG.error(nodeConf.getPublicIp(), "Could not update slaves file..");
			return false;
		}

	}
	
	/**
	 * Updates Masters / Slaves files to Existing Nodes during Add / Remove
	 * Nodes operation.
	 * 
	 * @param connection
	 *            the node connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean updateMasterSlavesConfFile(SSHExec connection,
			NodeConf nodeConf, HadoopConf conf) {
		boolean status = true;
		try {
			LOG.debug("Update Hadoop masters file.");
			status = updateMastersFile(connection, nodeConf, conf);
			if (!status) {
				return false;
			}
			LOG.debug("Update Hadoop slaves file.");
			status = HadoopUtils.updateSlavesFile(connection, nodeConf, conf);
			nodeConf.setStatus(status);
			return status;
		} catch (Exception e) {
			nodeConf.setStatus(false);
			return false;
		}
	}
	
	/**
	 * Copy hadoop jars.
	 * 
	 * @param connection
	 *            the connection
	 * @param conf
	 *            the conf
	 */
	private void copyHadoopJars(SSHExec connection, HadoopConf conf) {
		String hadoopHome = conf.getComponentHome();
		String userName = conf.getUsername();
		NodeConf nodeConf = conf.getNamenode();
		hadoopHome = FileUtils.getSeparatorTerminatedPathEntry(hadoopHome);
		String source = hadoopHome + "*.jar";
		String userHome = CommonUtil.getUserHome(userName);
		String destination = FileUtils
				.getSeparatorTerminatedPathEntry(userHome)
				+ HADOOP_JAR_PATH_LOCATION;
		Result res = null;
		try {
			LOG.debug("Copying Hadoop Jars in Agent..");
			// if connection established
			if (connection != null) {
				// Create folder if not exist
				AnkushTask createJarFolder = new MakeDirectory(destination);
				res = connection.exec(createJarFolder);
				if (!res.isSuccess) {
					LOG.error(nodeConf,
							"Could not create Jar folder in agent path");
				}

				AnkushTask copyFiles = new Copy(source, destination);
				res = connection.exec(copyFiles);
				if (!res.isSuccess) {
					LOG.error(nodeConf, "Unable to copy hadoop jar files");
				}

				source = hadoopHome + "lib/*.jar";
				copyFiles = new Copy(source, destination);
				res = connection.exec(copyFiles);
				if (!res.isSuccess) {
					LOG.error(nodeConf, "Unable to copy hadoop lib jar files");
				}
			}
		} catch (Exception e) {
		}
	}
	
	private boolean configureHadoopConfFiles(NodeConf nodeConf, HadoopConf conf, SSHExec connection) {
		try {
			if (addNodes) {
				if(!HadoopUtils.syncHadoopConfFolder(nodeConf, conf)) {
					return false;
				}
			} else {
				if(!this.configureHadoopEnvFile(nodeConf, conf, connection)) {
					return false;
				}

				// map of files with list of parameters.				
				Map<String, List<Parameter>> fileConfs = null;	
				// taking the default values from hadoop conf for first time.
				fileConfs = HadoopUtils.getHadoopParameterConfs(nodeConf, conf);
				if (fileConfs != null && !fileConfs.isEmpty()) {
					if (!HadoopUtils.addConfParameters(nodeConf,connection, conf, fileConfs,Constant.File_Extension.XML)) {
						LOG.error(nodeConf, "Could not configure hadoop configuration files.");
						nodeConf.setStatus(false);
						return false;
					}
				}
				
				// Integrating Ganglia metrics info
				try {
					HadoopUtils.setHadoopMetrics(connection, nodeConf, conf);
				} catch (Exception e) {
					// Add info to logger but do not stop the deployment process
					LOG.info(nodeConf.getPublicIp(),
							"Could not configure ganglia with Hadoop.");
				}
				
			}
			return true;
		} catch (Exception e) {
			LOG.error(nodeConf, "Could not configure hadoop configuration files.");
			LOG.debug(e.getMessage());
			return false;
		}
	}
	
	private boolean configureHadoopEnvFile(NodeConf nodeConf, HadoopConf conf, SSHExec connection) {
		try {
			Result res = null;
			
			// set JAVA_HOME in hadop-config.sh
			String javaHome = conf.getJavaHome(nodeConf.getPublicIp());
			String password = conf.getPassword();
			if(!conf.getClusterConf().isAuthTypePassword()) {
				password = conf.getPrivateKey();
			}
			
			if (javaHome == null || javaHome.isEmpty()) {
				javaHome = SSHUtils.getJavaHome(nodeConf.getPublicIp(), conf.getUsername(),
						password, conf.getClusterConf().isAuthTypePassword());
			}
			
			String exportJavaHome = "export JAVA_HOME=" + javaHome;

			LOG.debug("Updating Java Home in Hadoop env file");
			String file = conf.getHadoopConfDir()
					+ Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP;
			AnkushTask hadoopConfig = new PrependFile(exportJavaHome,
					file);
			res = connection.exec(hadoopConfig);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not set JAVA_HOME in hadoop-env.sh");
				return false;
			}
			// saving hadoop-env.sh file change
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(),
					Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP,
					nodeConf.getPublicIp(), "JAVA_HOME", javaHome);
			
			if (conf.getComponentVendor().equalsIgnoreCase(Constant.Hadoop.Vendors.CLOUDERA)) {
				String hadoopEnvFilePath = conf.getComponentHome()
						+ Hadoop1Deployer.RELPATH_COMPONENT_FOLDER_CONF
						+ Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP;
				String valClassPath = "${HADOOP_CLASSPATH}:"
						+ conf.getComponentHome()
						+ Hadoop1Deployer.RELPATH_COMPONENT_FOLDER_CAPACITYJAR
						+ "*";
				AnkushTask updateHadoopClassPath = new SetEnvVariable(
						conf.getPassword(), "HADOOP_CLASSPATH",
						valClassPath, hadoopEnvFilePath);
				res = connection.exec(updateHadoopClassPath);
				if (!res.isSuccess) {
					LOG.error(nodeConf,
							"Could not set HADOOP_CLASSPATH in "
									+ hadoopEnvFilePath + " file.");
					return false;
				}
				confManager.saveConfiguration(conf.getClusterDbId(),
						conf.getCurrentUser(),
						Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP,
						nodeConf.getPublicIp(), "HADOOP_CLASSPATH", valClassPath);
				
			}
			return true;
		} catch (Exception e) {
			LOG.error(nodeConf, "Could not configure hadoop environment file.");
			LOG.debug(e.getMessage());
			return false;
		}
	}
	
	private boolean configureEnvVariables(NodeConf nodeConf, HadoopConf conf, SSHExec connection) {
		try {
			Result res = null;
			LOG.debug("Setting Hadoop Home in Environment variables");
			
			// set environment variables in .bashrc
			String envContents = this.getBashrcContents(conf.getComponentHome());
			AnkushTask appendEnvFile = new AppendFile(envContents, Constant.BASHRC_FILE, conf.getPassword());
			res = connection.exec(appendEnvFile);
			if (!res.isSuccess) {
				LOG.error("Could not set environment variables in " + Constant.BASHRC_FILE + " file.");
				return false;
			}
			
			appendEnvFile = new SetEnvVariable(conf.getPassword(), HadoopUtils.KEY_HADOOP_HOME, conf.getComponentHome(), Constant.ETCENV_FILE);
			res = connection.exec(appendEnvFile);
			if (!res.isSuccess) {
				LOG.error("Could not set environment variable " + HadoopUtils.KEY_HADOOP_HOME + " in " + Constant.ETCENV_FILE + " file.");
				return false;
			}
			appendEnvFile = new SetEnvVariable(conf.getPassword(), HadoopUtils.KEY_HADOOP_CONF_DIR, conf.getHadoopConfDir(), Constant.ETCENV_FILE);
			res = connection.exec(appendEnvFile);
			if (!res.isSuccess) {
				LOG.error("Could not set environment variable " + HadoopUtils.KEY_HADOOP_CONF_DIR + " in " + Constant.ETCENV_FILE + " file.");
				return false;
			}
			return true;
		} catch (Exception e) {
			LOG.error(nodeConf, "Could not configure hadoop environment variables.");
			return false;
		}
	}
	
	private boolean configureHadoopDirs(NodeConf nodeConf, HadoopConf conf, SSHExec connection) {
		
		try{
			Result res = null;
			conf.setInstallationPath(this.getValidInstallationPath(conf.getInstallationPath()));
			
			String componentHome = this.getComponentHomeValue(conf);
			// setting componentHome in HadoopConf object
			conf.setComponentHome(componentHome);
			
			String componentConfPath = componentHome + Hadoop1Deployer.RELPATH_COMPONENT_FOLDER_CONF;
			// setting HadoopConfDir in HadoopConf object
			conf.setHadoopConfDir(componentConfPath);
			
			LOG.debug("Create directory - " + conf.getInstallationPath());

			// make installation directory if not exists
			AnkushTask mkInstallationPath = new MakeDirectory(conf.getInstallationPath());
			res = connection.exec(mkInstallationPath);

			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not create installation directory: " + mkInstallationPath);
				return false;
			}
			
			List<String> hadoopTmpDirs = new ArrayList<String>();
			hadoopTmpDirs.add(conf.getHadoopTmpDir());
			hadoopTmpDirs.add(conf.getMapRedTmpDir());
			
			// removing old existing Hadoop directories.
			List<String> hadoopDirs = new ArrayList<String>();
			hadoopDirs.add(conf.getDfsNameDir());
			hadoopDirs.add(conf.getDfsDataDir());
			hadoopDirs.addAll(hadoopTmpDirs);
			
			for(String dirPath : hadoopDirs) {
				AnkushTask removeDir = new Remove(dirPath);
				res = connection.exec(removeDir);
				if (!res.isSuccess) {
					LOG.error(nodeConf,
							"Could not remove directory: " + removeDir);
					return false;
				}
			}
								
			// Create Hadoop Tmp directories.
			for(String dirPath : hadoopTmpDirs) {
				AnkushTask mkHadoopTmpDir = new MakeDirectory(dirPath);
				res = connection.exec(mkHadoopTmpDir);
				if (!res.isSuccess) {
					LOG.error(nodeConf, "Could not create temporary directory: " + dirPath);
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			LOG.error(nodeConf, "Could not configure Hadoop directories.");
			return false;
		}
	}
	
	/**
	 * Gets the bashrc contents.
	 * 
	 * @param hadoopHome
	 *            the hadoop home
	 * @return the bashrc contents
	 */

	private String getBashrcContents(String hadoopHome) {
		StringBuilder sb = new StringBuilder();
		sb.append("export " + HadoopUtils.KEY_HADOOP_HOME + "=").append(hadoopHome)
				.append(";");
		sb.append("export HADOOP_CONF_DIR=").append(hadoopHome)
				.append(STR_SLASH).append(RELPATH_COMPONENT_FOLDER_CONF)
				.append(";");

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		final HadoopConf conf = (HadoopConf) config;
		LOG.setLoggerConfig(conf);
		LOG.info("Undeploying Apache Hadoop...");

		ArrayList<NodeConf> nodeConfs = new ArrayList(conf.getCompNodes());
		
		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeConfs) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean nodeStatus = removeNode(nodeConf, conf);
						nodeConf.setStatus(nodeStatus);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeConfs.size());
			status = status(nodeConfs);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * Removes the node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean removeNode(NodeConf nodeConf, HadoopConf conf) {
		LOG.setLoggerConfig(conf);
		
		SSHExec connection = null;
		String publicIp = nodeConf.getPublicIp();

		try {
			// connect to remote node
			LOG.debug("Connecting with node : " + publicIp);
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {

				List<String> dirList = new ArrayList<String>();
				dirList.add(conf.getDfsNameDir());
				dirList.add(conf.getDfsDataDir());
				dirList.add(conf.getHadoopTmpDir());
				dirList.add(conf.getComponentHome());
				
				for(String dirName : dirList) {
					AnkushTask removeDir = new Remove(dirName);
					connection.exec(removeDir);	
				}

				// Remove Contents from /etc/environment & .bashrc files
				List<String> fileContents = Arrays
						.asList(this.getBashrcContents(conf.getComponentHome())
								.split("\n"));
				RemoveText prepFile = new RemoveText(fileContents,
						Constant.BASHRC_FILE, conf.getPassword());
				connection.exec(prepFile);

			}
		} catch (Exception e) {
			LOG.error("Error while Hadoop remove node...", e);
			LOG.debug("Error while removing node:" + e.getMessage());
			return true;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		nodeConf.setStatus(true);

		LOG.info(publicIp, "Hadoop Remove node Successfully Done..");
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#start(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean start(Configuration config) {
		HadoopConf conf = (HadoopConf) config;
		LOG.setLoggerConfig(conf);

		// commands to execute
		String componentHome = conf.getInstallationPath() + STR_HADOOP
				+ conf.getComponentVersion();

		String startNodeCommand = componentHome + STR_SLASH
				+ COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DFS_START;
		CustomTask startDFS = new ExecCommand(startNodeCommand);
		startNodeCommand = componentHome + STR_SLASH + COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_MAPRED_START;
		CustomTask startMapred = new ExecCommand(startNodeCommand);
		Result res = null;
		SSHExec connection = null;
		NodeConf nodeConf = conf.getNamenode();
		// connect to namendoe and execute commands
		try {
			LOG.debug("Connecting with node : " + nodeConf.getPublicIp());

			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				LOG.info(nodeConf.getPublicIp(), "Starting Hadoop Services..");

				res = connection.exec(startDFS);
				if (res.rc != 0) {
					LOG.error(nodeConf, "Could not start DFS services..");
					return false;
				}

				res = connection.exec(startMapred);
				if (res.rc != 0) {
					LOG.error(nodeConf, "Could not start Mapred services..");
					return false;
				}
				return true;
			}
		} catch (Exception e) {
			LOG.error(nodeConf,
					"Error in starting hadoop services : " + e.getMessage());
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stop(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean stop(Configuration config) {
		HadoopConf conf = (HadoopConf) config;
		LOG.setLoggerConfig(conf);
		NodeConf namenode = conf.getNamenode();
		return stopCluster(conf, namenode);
	}

	/**
	 * Stop cluster.
	 * 
	 * @param conf
	 *            the conf
	 * @param nodeConf
	 *            the node conf
	 * @return true, if successful
	 */
	private boolean stopCluster(HadoopConf conf, NodeConf nodeConf) {
		// commands to execute
		String componentHome = conf.getComponentHome();
		String stopNodeCommand = componentHome + STR_SLASH
				+ COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DFS_STOP;
		CustomTask stopDFS = new ExecCommand(stopNodeCommand);
		stopNodeCommand = componentHome + STR_SLASH + COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_MAPRED_STOP;
		CustomTask stopMapred = new ExecCommand(stopNodeCommand);

		SSHExec connection = null;

		// connect to namendoe and execute commands
		try {
			// connect to remote node
			LOG.debug("Connecting with node : " + nodeConf.getPublicIp());
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			// if connected
			if (connection != null) {
				LOG.info(nodeConf.getPublicIp(), "Stopping Hadoop Services..");

				connection.exec(stopDFS);
				connection.exec(stopMapred);
			}
		} catch (Exception e) {
			LOG.error(nodeConf,
					"Error in stopping Hadoop services : " + e.getMessage());
			return true;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}

	/**
	 * Adds the node.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private boolean addNode(NodeConf nodeConf, HadoopConf conf) {
		LOG.setLoggerConfig(conf);

		boolean status = false;
		SSHExec connection = null;
		Result res = null;
		try {
			// connect to remote node
			LOG.debug("Connecting with node : " + nodeConf.getPublicIp());
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {

				// make installation directory if not exists
				LOG.debug("Create directory - " + conf.getInstallationPath());
				AnkushTask mkInstallationPath = new MakeDirectory(conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(nodeConf,
							"Could not create installation directory");
					return false;
				}

				status = createNode(nodeConf, conf);

				if (!status) {
					LOG.error(nodeConf, "Failed to setup Hadoop Bundle on "
							+ nodeConf.getPublicIp());
				}

				// Setting node status
				nodeConf.setStatus(status);
			}
		} catch (Exception e) {
			LOG.error("Failed to setup Hadoop Bundle on "
					+ nodeConf.getPublicIp() + " & Reason is : "
					+ e.getMessage());
			nodeConf.setStatus(false);
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#addNodes(java.util.List,
	 * com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean addNodes(List<NodeConf> nodeList, Configuration config) {
		final HadoopConf conf = (HadoopConf) config;
		addNodes = true;
		final String infoMsg = "Adding Nodes..";
		final String node = conf.getNamenode().getPublicIp();
		LOG.info(infoMsg);
		LOG.info(node, infoMsg);

		Set<NodeConf> expectedNodeList =  new HashSet<NodeConf>();
		expectedNodeList.addAll(new HashSet<NodeConf>(nodeList));
		expectedNodeList.addAll(conf.getCompNodes());
		conf.setExpectedNodesAfterAddOrRemove(expectedNodeList);
		String rackFileContent = "";
		if (conf.isRackEnabled()) {
			rackFileContent = HadoopRackAwareness.createRackFileContents(expectedNodeList);
		}
		conf.setRackFileContent(rackFileContent);
		
		boolean setupPasswordlessSSH = HadoopUtils.setupPasswordlessSSH(conf, conf.getNamenode(), new HashSet<NodeConf>(nodeList));
		if (!setupPasswordlessSSH) {
			LOG.error("Could not set Passwordless SSH in cluster..");
			return false;
		}

		// Node Deployment process ...
		final Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean nodeStatus = addNode(nodeConf, conf);
						nodeConf.setStatus(nodeStatus);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());
			status = status(nodeList);
			if(status) {
				status = HadoopUtils.updateConfToNodes(conf, Constant.SyncConfigurationAction.ADD);	
			}
			
			if (!status) {
				conf.setExpectedNodesAfterAddOrRemove(new HashSet<NodeConf>(conf.getCompNodes()));
				LOG.error("Could not add nodes for Hadoop.");
				return false;
			}
			status = status(nodeList);
			if(status) {
				HadoopUtils.updateConfObjectAfterAddRemoveNodes(conf);
			}
			
			if (!status) {
				conf.setExpectedNodesAfterAddOrRemove(new HashSet<NodeConf>(conf.getCompNodes()));
				LOG.error("Could not add nodes for Hadoop.");
				return false;
			}
		} catch (Exception e) {
			conf.setExpectedNodesAfterAddOrRemove(new HashSet<NodeConf>(conf.getCompNodes()));
			LOG.error("Could not add nodes for Hadoop."); 
			LOG.debug("Could not add nodes for Hadoop.");
			LOG.debug(e.getMessage());
			status = false;
		}

		LOG.log(node, infoMsg, status);
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.
	 * List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		final HadoopConf hadoopConf = (HadoopConf) config;

		final Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean nodeStatus = removeNode(nodeConf, hadoopConf);
						nodeConf.setStatus(nodeStatus);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());

			HadoopUtils.updateConfToNodes(hadoopConf,
					Constant.SyncConfigurationAction.REMOVE);
			HadoopUtils.updateConfObjectAfterAddRemoveNodes(hadoopConf);
			status = status(nodeList);
		} catch (Exception e) {
			LOG.error("Error in removing nodes..", e);
			status = false;
		}
		return status;
	}

	/**
	 * Sets the jobtracker recovery.
	 * 
	 * @param connection
	 *            the connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @throws Exception
	 *             the exception
	 */
	private void setJobtrackerRecovery(SSHExec connection, NodeConf nodeConf,
			HadoopConf conf) throws Exception {
		String mapredsite = conf.getHadoopConfDir()
				+ Constant.Hadoop.FileName.XML_MAPRED_SITE;
		boolean jobTrackerRecovery = conf.isJobTrackerRecovery();
		if (jobTrackerRecovery) {
			AnkushTask marredJobTracker = new AddConfProperty(
					"mapred.jobtracker.restart.recover", "true", mapredsite,
					Constant.File_Extension.XML);
			Result res = connection.exec(marredJobTracker);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not edit mapred-site.xml");
			}

			// Saving mapred.restart.recover property.
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(),
					Constant.Hadoop.FileName.XML_MAPRED_SITE,
					nodeConf.getPublicIp(),
					"mapred.jobtracker.restart.recover", "true");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#startServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean startServices(List<NodeConf> nodeList, Configuration config) {
		HadoopConf conf = (HadoopConf) config;
		String componentHome = conf.getComponentHome();

		CustomTask startDatanodes = new ExecCommand(componentHome + STR_SLASH
				+ COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP + " "
				+ Constant.Hadoop.Command.START + " "
				+ Constant.Hadoop.Command.DAEMON_DATANODE);
		CustomTask startTasktrackers = new ExecCommand(componentHome
				+ STR_SLASH + COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP + " "
				+ Constant.Hadoop.Command.START + " "
				+ Constant.Hadoop.Command.DAEMON_TASKTRACKER);
		CustomTask startSecondarynamenodes = new ExecCommand(componentHome
				+ STR_SLASH + COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP + " "
				+ Constant.Hadoop.Command.START + " "
				+ Constant.Hadoop.Command.DAEMON_SECONDARY_NAMENODE);

		Result res = null;
		SSHExec connection = null;

		String namenodeIP = conf.getNamenode().getPublicIp();
		LOG.info(namenodeIP, "Connecting with node....");

		// connect to nodes and execute commands
		try {

			for (NodeConf nodeConf : nodeList) {
				LOG.setLoggerConfig(conf);
				HadoopNodeConf hadoopNodeConf = (HadoopNodeConf) nodeConf;
				boolean isDatanode = hadoopNodeConf.getDataNode();
				boolean isSecondaryNameNode = hadoopNodeConf
						.getSecondaryNameNode();

				String nodeIp = nodeConf.getPublicIp();
				// connect to remote node
				connection = SSHUtils.connectToNode(nodeIp, conf.getUsername(),
						conf.getPassword(), conf.getPrivateKey());

				// if connected
				if (connection != null) {
					if (isDatanode) {
						res = connection.exec(startDatanodes);
						if (res.rc != 0) {
							LOG.error(nodeConf,
									"Could not start datanode service on "
											+ nodeIp);
							return false;
						}

						res = connection.exec(startTasktrackers);
						if (res.rc != 0) {
							LOG.error(nodeConf,
									"Could not start tasktracker service on "
											+ nodeIp);
							return false;
						}
					}
					if (isSecondaryNameNode) {
						res = connection.exec(startSecondarynamenodes);
						if (res.rc != 0) {
							LOG.error(nodeConf,
									"Could not start secondarynamenode service on "
											+ nodeIp);
							return false;
						}
					}
				} else {
					LOG.error(nodeConf, "Unable to establish connection ! ");
				}
			}
		} catch (Exception e) {
			LOG.error("Error in starting hadoop services : " + e.getMessage());
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		LOG.info(namenodeIP, "Starting node services activity completed..");
		return status(nodeList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#stopServices(java.util
	 * .List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean stopServices(List<NodeConf> nodeList, Configuration config) {
		HadoopConf conf = (HadoopConf) config;
		String componentHome = conf.getInstallationPath() + STR_HADOOP
				+ conf.getComponentVersion();

		CustomTask stopDatanodes = new ExecCommand(componentHome + STR_SLASH
				+ COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP + " "
				+ Constant.Hadoop.Command.STOP + " "
				+ Constant.Hadoop.Command.DAEMON_DATANODE);

		CustomTask stopTasktrackers = new ExecCommand(componentHome + STR_SLASH
				+ COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP + " "
				+ Constant.Hadoop.Command.STOP + " "
				+ Constant.Hadoop.Command.DAEMON_TASKTRACKER);

		CustomTask stopSecondarynamenodes = new ExecCommand(componentHome
				+ STR_SLASH + COMPONENT_FOLDER_BIN
				+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP + " "
				+ Constant.Hadoop.Command.STOP + " "
				+ Constant.Hadoop.Command.DAEMON_SECONDARY_NAMENODE);

		Result res = null;
		SSHExec connection = null;
		LOG.setLoggerConfig(conf);
		String namenodeIP = conf.getNamenode().getPublicIp();

		// connect to nodes and execute commands
		try {
			for (NodeConf nodeConf : nodeList) {

				HadoopNodeConf hadoopNodeConf = (HadoopNodeConf) nodeConf;
				boolean isDatanode = hadoopNodeConf.getDataNode();
				boolean isSecondaryNameNode = hadoopNodeConf
						.getSecondaryNameNode();

				String nodeIp = nodeConf.getPublicIp();
				// connect to remote node
				LOG.info(nodeIp, "Connecting with node....");
				connection = SSHUtils.connectToNode(nodeIp, conf.getUsername(),
						conf.getPassword(), conf.getPrivateKey());

				// if connected
				if (connection != null) {
					if (isDatanode) {
						res = connection.exec(stopDatanodes);
						if (res.rc != 0) {
							LOG.error(nodeConf,
									"Could not stop datanodes process on "
											+ nodeIp);
							return false;
						}

						res = connection.exec(stopTasktrackers);
						if (res.rc != 0) {
							LOG.error(nodeConf,
									"Could not stop tasktracker process on "
											+ nodeIp);
							return false;
						}
					}
					if (isSecondaryNameNode) {
						res = connection.exec(stopSecondarynamenodes);
						if (res.rc != 0) {
							LOG.error(nodeConf,
									"Could not stop secondarynamenode process on "
											+ nodeIp);
							return false;
						}
					}
				} else {
					LOG.error("Unable to establish connection ! ");
				}
			}
		} catch (Exception e) {
			LOG.error(namenodeIP,
					"Error in stopping hadoop services : " + e.getMessage());
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		LOG.info(namenodeIP, "Stopping node service activity completed..");

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