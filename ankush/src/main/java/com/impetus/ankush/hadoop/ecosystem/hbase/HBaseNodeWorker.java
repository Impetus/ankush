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
package com.impetus.ankush.hadoop.ecosystem.hbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang.StringUtils;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.config.ConfigurationReader;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddToPathVariable;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.ExecSudoCommand;
import com.impetus.ankush.common.scripting.impl.PrependFile;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.scripting.impl.SyncFolder;

import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.FileNameUtils;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.HadoopUtils;

/**
 * The Class HBaseNodeWorker.
 *
 * @author root
 */
public class HBaseNodeWorker implements Runnable {
	
	/** The log. */
	private static final AnkushLogger LOG = new AnkushLogger(
			Constant.Component.Name.HBASE, ApacheHBaseDeployer.class);

	// Configuration manager to save the property file change records.
	/** The conf manager. */
	private ConfigurationManager confManager = new ConfigurationManager();
		
	private boolean isAddNodeOperation = false;
	
	/** The conf. */
	private HBaseConf conf = null;
	
	/** The node conf. */
	private NodeConf nodeConf = null;

	/** The semaphore. */
	private Semaphore semaphore = null;
	
	/**
	 * Instantiates a new h base node worker.
	 *
	 * @param semaphore the semaphore
	 * @param config the config
	 * @param nodeConf the node conf
	 * @param nodeId the node id
	 */
	public HBaseNodeWorker(Semaphore semaphore, HBaseConf config,
			NodeConf nodeConf, boolean isAddNodeOperation) {
		this.semaphore = semaphore;
		this.conf = config;
		this.nodeConf = nodeConf;
		this.isAddNodeOperation = isAddNodeOperation;
		LOG.setLoggerConfig(this.conf);
	}
	
	/**
	 * Configure hadoop metrics.
	 *
	 * @param connection the connection
	 * @return true, if successful
	 */
	private boolean configureHadoopMetrics(SSHExec connection) {
		Result res = null;
		boolean statusFlag = true;
		String errorLog = "Could not edit hadoop-metrics.properties file"; 
		try 
		{
			/** The ankushConf Reader. */
			ConfigurationReader ankushConf = AppStoreWrapper.getAnkushConfReader();
			
			String hbaseClass = "hbase.class=org.apache.hadoop.metrics.ganglia.GangliaContext";
			String hbasePeriod = "hbase.period=10";
			String hbaseServers = "hbase.servers=" + conf.getClusterConf().getGangliaMaster().getPrivateIp() + ":" + ankushConf.getIntValue("ganglia.port");

			// add the "HBase Class" property to Hadoop-metrics.properties
			AnkushTask appendHbaseClass = new AppendFile(hbaseClass, this.conf.getHBaseConfDir() + "hadoop-metrics.properties");
			// add the "HBase Period" property to Hadoop-metrics.properties
			AnkushTask appendHbasePeriod = new AppendFile(hbasePeriod, this.conf.getHBaseConfDir() + "hadoop-metrics.properties");
			// add the "HBase Servers" property to Hadoop-metrics.properties
			AnkushTask appendHbaseServers = new AppendFile(hbaseServers, this.conf.getHBaseConfDir() + "hadoop-metrics.properties");
			
			res = connection.exec(appendHbaseClass);
			if (!res.isSuccess) {
				LOG.error(this.nodeConf, errorLog);
				statusFlag = false;
				return false;
			}
			res = connection.exec(appendHbasePeriod);
			if (!res.isSuccess) {
				LOG.error(this.nodeConf, errorLog);
				statusFlag = false;
				return false;
			}
			res = connection.exec(appendHbaseServers);
			if (!res.isSuccess) {
				LOG.error(this.nodeConf, errorLog);
				statusFlag = false;
				return false;
			}
		}
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
			if (connection != null) {
				connection.disconnect();
			}
			return false;
		} finally {
			this.nodeConf.setStatus(statusFlag);
			LOG.debug("Hbase hadoop metrics properties configuration over ... ");
		}
		return statusFlag;
	}
	
	/**
	 * Configure hbase site xml file.
	 *
	 * @param connection the connection
	 * @return true, if successful
	 */
	private boolean configureHbaseSiteXmlFile(SSHExec connection) {
		boolean statusFlag = true;
		try 
		{
			String hbaseSiteXmlPath = this.conf.getComponentHome() 
									+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR 
									+ Constant.Hadoop.FileName.XML_HBASE_SITE;
			
			String hbaseMasterValue = conf.getHbaseMasterNode().getPrivateIp() + ":" + conf.getHbaseMasterWebPort();
			String hbaseRootDirValue = conf.getHdfsPathForHbase();
			
			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put("hbase.master", hbaseMasterValue);
			paramList.put("hbase.rootdir", hbaseRootDirValue);
			paramList.put("hbase.cluster.distributed", "true");	
			paramList.put("hbase.zookeeper.quorum", conf.getZkQuoromPropertyValue());
			paramList.put("hbase.zookeeper.property.clientPort", conf.getZookeeperPort());
			
			for(String advConfPropName : conf.getAdvancedConf().keySet()) {
				String advConfPropValue = (String) conf.getAdvancedConf().get(advConfPropName);
				paramList.put(advConfPropName, advConfPropValue);
			}
			
			for(String propertyName : paramList.keySet()) {
				statusFlag = AgentUtils.addPropertyToFile(conf, connection, nodeConf, hbaseSiteXmlPath, propertyName, paramList.get(propertyName));
				if (!statusFlag) {
					return false;
				}	
			}
		}
		catch (Exception e) {
			LOG.debug(e.getMessage());
			if (connection != null) {
				connection.disconnect();
			}
			return false;
		} finally {
			LOG.debug(Constant.Hadoop.FileName.XML_HBASE_SITE + " file configuration over ... ");
		}
		return statusFlag;
	}
	
	private boolean addHadoopConfToClassPath(SSHExec connection, HBaseConf conf) {
		Result res = null;
		boolean statusFlag = true;
		try {
			String exportCmdHBaseClassPath = "export HBASE_CLASSPATH=";
			String newTextHBaseClassPath = exportCmdHBaseClassPath + conf.getHadoopConf();

			String targetText_HBaseClasspath = "# " + exportCmdHBaseClassPath;
					
			LOG.debug("Updating HBase classpath  in HBase env file...");
			String file = this.conf.getComponentHome() 
							+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR
							+ Constant.Hadoop.FileName.SCRIPT_ENV_HBASE;
			
			AnkushTask hbaseConfig = new ReplaceText(targetText_HBaseClasspath, 
										newTextHBaseClassPath, file, 
										false, conf.getPassword()); 
			
			res = connection.exec(hbaseConfig);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not set HBASE_CLASSPATH in " + Constant.Hadoop.FileName.SCRIPT_ENV_HBASE + " file.");
				return false;
			}
			// saving hbase-env.sh file change
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(),
					Constant.Hadoop.FileName.SCRIPT_ENV_HBASE,
					nodeConf.getPublicIp(), "HBASE_CLASSPATH", conf.getHadoopConf());
		} catch (Exception e) {
			LOG.debug(e.getMessage());
			if (connection != null) {
				connection.disconnect();
			}
			return false;
		} finally {
			this.nodeConf.setStatus(statusFlag);
		}
		return statusFlag;
		
	}
	
	/**
	 * Sync hadoop conf files.
	 *
	 * @param connection the connection
	 * @return true, if successful
	 */
	private boolean linkHadoopConfFiles(SSHExec connection) {
		Result res = null;
		boolean statusFlag = true;
		try {
			String hadoopHdfsSiteXmlPath = conf.getHadoopConf() + Constant.Hadoop.FileName.XML_HDFS_SITE ; 
			String hadoopCoreSiteXmlPath = conf.getHadoopConf() + Constant.Hadoop.FileName.XML_CORE_SITE ;
			
			String hbaseHdfsLinkPath = conf.getComponentHome() 
									+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR 
									+ Constant.Hadoop.FileName.XML_HDFS_SITE;
			String hbaseCoreLinkPath = conf.getComponentHome() 
									+ ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR 
									+ Constant.Hadoop.FileName.XML_CORE_SITE;
			
			Map<String, String> createFileLinks = new LinkedHashMap<String, String>();
			createFileLinks.put(hadoopHdfsSiteXmlPath, hbaseHdfsLinkPath);
			createFileLinks.put(hadoopCoreSiteXmlPath, hbaseCoreLinkPath);
			
			for(String sourceFile : createFileLinks.keySet()) {
				AnkushTask createLink = new RunInBackground("ln -s " + sourceFile + " " + createFileLinks.get(sourceFile));
				res = connection.exec(createLink);
				if(res.rc != 0) {
					LOG.error("Could not create soft link for " + FileNameUtils.getName(sourceFile) + " file");
					return false;
				}
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage());
			if (connection != null) {
				connection.disconnect();
			}
			return false;
		} finally {
			this.nodeConf.setStatus(statusFlag);
		}
		return statusFlag;
	}
	
	/**
	 * Configure hbase conf folder.
	 *
	 * @param connection the connection
	 * @return true, if successful
	 */
	private boolean configureHbaseConfFolder(SSHExec connection) {
		Result res = null;
		boolean statusFlag = true;
		
		try {
			// truncate region servers file 
			String filePathRegionServers = this.conf.getHBaseConfDir() 
										+ ApacheHBaseDeployer.HBASE_FILE_REGIONSERVERS;
			AnkushTask clearRegionServersFile  = new ClearFile(filePathRegionServers);
			res = connection.exec(clearRegionServersFile);
			if (!res.isSuccess) {
				LOG.error(this.nodeConf,
						"Could not clear region servers file");
				statusFlag = false;
				return false;
			}
			
			// add region servers IP Addresses to the /conf/regionservers file 
			ArrayList<String> regionServersIpList = new ArrayList<String>();
			for (NodeConf nodeConf : conf.getHbaseRegionServerNodes()) {
				regionServersIpList.add(nodeConf.getPrivateIp());
			}
			
			String rsFileContent = StringUtils.join(regionServersIpList, Constant.LINE_SEPERATOR);
			
			AnkushTask appendToRegionServers = new AppendFile(rsFileContent, filePathRegionServers);
			res = connection.exec(appendToRegionServers);
			
			if (!res.isSuccess) {
				LOG.error(this.nodeConf,
						"Could not edit region servers file");
				statusFlag = false;
				return false;
			}
			
			// write to the /conf/hbase-env.sh file 
			String javaHome = conf.getUniversalClusterJavaHome();
			
			if (javaHome == null || javaHome.isEmpty()) {
				String username = conf.getUsername();
				String password = conf.getPassword();
				boolean authUsingPassword = conf.getClusterConf().isAuthTypePassword();
				if (!authUsingPassword) {
					password = conf.getPrivateKey();
				}
				javaHome = SSHUtils.getJavaHome(nodeConf.getPublicIp(),
						username, password, authUsingPassword);
			}
			
			String hbaseEvnContent = "export JAVA_HOME=" + javaHome + "\n" + "export HBASE_MANAGES_ZK=" + conf.isHbaseManagesZK();
			AnkushTask appendToHBaseEnvFile = new AppendFile(hbaseEvnContent, this.conf.getComponentHome() + "/conf/hbase-env.sh");
			res = connection.exec(appendToHBaseEnvFile);
			if (!res.isSuccess) {
				LOG.error(this.nodeConf, "Could not edit hbase-env.sh file");
				return false;
			}
			
			statusFlag = this.configureHbaseSiteXmlFile(connection);
			if (!statusFlag) {
				LOG.error(this.nodeConf, "Could not edit hbase-site.xml file");
				return false;
			}
			statusFlag = this.configureHadoopMetrics(connection);
			if (!statusFlag) {
				LOG.error(this.nodeConf, "Could not edit hadoop-metrics.properties file");
				return false;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			if (connection != null) {
				connection.disconnect();
			}
			return false;
		} finally {
			this.nodeConf.setStatus(statusFlag);
			LOG.debug("Hbase conf folder configuration over ... ");
		}
		return statusFlag;
	}
	
	/**
	 * Configure hbase node.
	 *
	 * @param connection the connection
	 * @return true, if successful
	 */
	private boolean configureHbaseNode(SSHExec connection) {
		Result res = null;
		boolean statusFlag = true;

		try {
				LOG.debug("Create directory - "
						+ this.conf.getInstallationPath());
				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						this.conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(this.nodeConf,
							"Could not create installation directory");
					statusFlag = false;
					return false;
				} else {
					LOG.debug("Get and extract tarball");
					// get and extract tarball
					boolean isSuccessfull = SSHUtils.getAndExtractComponent(
							connection, this.conf, "hbase");

					if (!isSuccessfull) {
						LOG.error(this.nodeConf,
								"Could not extract bundle file ");
						statusFlag = false;
						return false;
					}

					// if component exists at destination
					if (isSuccessfull) {
						// setting HBASE_HOME in environment 
						LOG.debug("Setting HBASE_HOME variable");
						ExecSudoCommand setHbaseHome = new ExecSudoCommand(conf.getPassword(), "sed -i '$ a\\" 
														+  "export HBASE_HOME=" + conf.getComponentHome() + "' " + Constant.ETCENV_FILE);

						// setting HBASE_HOME/bin in PATH
						LOG.debug("Setting HBASE_HOME/bin in $PATH");
						AddToPathVariable updatePath = new AddToPathVariable(conf.getComponentHome() + "/bin", Constant.ETCENV_FILE, 
								conf.getPassword());
						AnkushTask recompileEnvFile = new RunInBackground("source " + Constant.ETCENV_FILE);
						
						res = connection.exec(setHbaseHome);
						if(!res.isSuccess) {
							LOG.error(this.nodeConf,
									"Could not edit " + Constant.ETCENV_FILE + " file");
							statusFlag = false;
							return false;
						}
						res = connection.exec(updatePath);
						if(!res.isSuccess) {
							LOG.error(this.nodeConf,
									"Could not edit " + Constant.ETCENV_FILE + " file");
							statusFlag = false;
							return false;
						}
						res = connection.exec(recompileEnvFile);
						if(!res.isSuccess) {
							LOG.error(this.nodeConf,
									"Could not edit " + Constant.ETCENV_FILE + " file");
							statusFlag = false;
							return false;
						}
						
						LOG.debug("Configuring Hbase conf folder");
						if(this.isAddNodeOperation) {
							if(!HBaseNodeWorker.syncHBaseConfFolder(nodeConf, conf)) {
								return false;
							}
						} else {
							statusFlag = this.configureHbaseConfFolder(connection);
							if(!statusFlag) {
								LOG.error(this.nodeConf,
										"Could not configure Hbase Configuration folder");
								return false;
							}
							
							statusFlag = this.addHadoopConfToClassPath(connection, conf);
							if(!statusFlag) {
								return false;
							}	
						}
						LOG.info("Sync hadoop configuration files to HBase conf folder");
						statusFlag = this.linkHadoopConfFiles(connection);
						if(!statusFlag) {
							LOG.error(this.nodeConf,
									"Could not link hadoop configuration files to HBase");
							return false;
						}
					}
				}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			if (connection != null) {
				connection.disconnect();
			}
			statusFlag = false;
			return false;
		} finally {
			LOG.debug("Hbase Node configuration over ... ");
			this.nodeConf.setStatus(statusFlag);
		}
		return  statusFlag;
	}
	
	/**
	 * Starts the update process for Hadoop Configuration to Existing Nodes
	 * during Add / Remove Nodes operation.
	 * 
	 * @param hbaseConf
	 *            the hadoop Conf
	 * @param action
	 *            the Constant.SyncConfigurationAction (ADD / REMOVE)
	 * @return true, if successful
	 */
	public static boolean updateConfToNodes(final HBaseConf hbaseConf,
			final Constant.SyncConfigurationAction action) {
		Set<NodeConf> nodeConfs = hbaseConf.getExpectedRSAfterAddOrRemove();
		
		final Semaphore semaphorePostOperation = new Semaphore(
				nodeConfs.size());
		boolean status = true;
		try {
			for (final NodeConf nodeConf : nodeConfs) {
				semaphorePostOperation.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean nodeStatus = HBaseNodeWorker.updateConfToNode(
								nodeConf, hbaseConf);
						nodeConf.setStatus(nodeStatus);
						if (semaphorePostOperation != null) {
							semaphorePostOperation.release();
						}
					}
				});
			}
			semaphorePostOperation.acquire(nodeConfs.size());
			status = HadoopUtils.status(nodeConfs);
			if(!status) {
				LOG.error("Could not update HBase configuration to nodes during " + action.toString() + " nodes operation.");
			}
		} catch (Exception e) {
			LOG.error("Could not update HBase configuration to nodes during " + action.toString() + " nodes operation.", e);
			return false;
		}
		return status;
	}
	
	/**
	 * Updates Hadoop Configuration To Existing Nodes during Add / Remove Nodes
	 * operation.
	 * 
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	private static boolean updateConfToNode(NodeConf nodeConf,
			HBaseConf conf) {
		boolean status = false;
		SSHExec connection = null;
		String publicIp = nodeConf.getPublicIp();

		try {
			// connect to remote node
			LOG.debug("Connecting with node : " + publicIp);
			LOG.info("Update HBase configuration files");
			connection = SSHUtils.connectToNode(publicIp, conf.getUsername(),
					conf.getPassword(), conf.getPrivateKey());

			// if connected
			if (connection != null) {
				status = HBaseNodeWorker.updateRegionServersFile(connection, nodeConf, conf);
				if (!status) {
					nodeConf.setStatus(status);
					return false;
				}
			}
		} catch (Exception e) {
			nodeConf.setStatus(false);
			LOG.error(nodeConf, "Could not update HBase configuration files.");
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		nodeConf.setStatus(status);
		return status;
	}
	
	/**
	 * Updates Slaves files to Existing Nodes during Add / Remove Nodes
	 * operation.
	 * 
	 * @param connection
	 *            the node connection
	 * @param nodeConf
	 *            the node conf
	 * @param conf
	 *            the conf
	 * @return true, if successful
	 */
	public static boolean updateRegionServersFile(SSHExec connection, NodeConf nodeConf,
			HBaseConf conf) {
		LOG.info("Update HBase " + ApacheHBaseDeployer.HBASE_FILE_REGIONSERVERS + " file");
		// configuring slaves file
		Result res = null;
		try {
			List<String> regionServerIpList = new ArrayList<String>();
			for (NodeConf sNode : conf.getExpectedRSAfterAddOrRemove()) {
				regionServerIpList.add(sNode.getPrivateIp());
			}

			String regionServerFile = FileUtils.getSeparatorTerminatedPathEntry(conf.getComponentHome()) +
								ApacheHBaseDeployer.REL_PATH_HBASE_CONF_DIR
								+ ApacheHBaseDeployer.HBASE_FILE_REGIONSERVERS;
			
			AnkushTask clearFile = new ClearFile(regionServerFile);
			res = connection.exec(clearFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not clear " + ApacheHBaseDeployer.HBASE_FILE_REGIONSERVERS + " file contents.");
				return false;
			}
			String regionServerFileContent = StringUtils.join(regionServerIpList,
					Constant.LINE_SEPERATOR);
			AnkushTask appendRegionServerFile = new AppendFile(regionServerFileContent,
					regionServerFile);
			res = connection.exec(appendRegionServerFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not add nodes information to " + ApacheHBaseDeployer.HBASE_FILE_REGIONSERVERS + " file");
				return false;
			}

//			// Saving slaveFile property.
//			confManager.saveConfiguration(conf.getClusterDbId(),
//					conf.getCurrentUser(), Constant.Hadoop.FileName.SLAVES,
//					nodeConf.getPublicIp(), "IpValues", slavesFileContent);

			return true;
		} catch (Exception e) {
			LOG.error(nodeConf.getPublicIp(), "Could not update slaves file.");
			return false;
		}
	}
	
	/**
	 * Update conf after add remove nodes.
	 * 
	 * @param conf
	 *            the conf
	 */
	public static void updateConfObjectAfterAddRemoveNodes(HBaseConf conf) {
		conf.setHbaseRegionServerNodes(conf.getExpectedRSAfterAddOrRemove());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		final String publicIp = this.nodeConf.getPublicIp();
		LOG.debug("HBase Worker thread deploying on node - " + publicIp
				+ "...");
		boolean statusFlag = true;
		
		SSHExec connection = null;
		try {
			LOG.debug("Connecting with node");
			// connect to remote node
			connection = SSHUtils.connectToNode(publicIp,
					this.conf.getUsername(), this.conf.getPassword(),
					this.conf.getPrivateKey());

			// if connected
			if (connection != null) {
				statusFlag = this.configureHbaseNode(connection);
				this.nodeConf.setStatus(statusFlag);	
			} else {
				LOG.error(nodeConf, "Authentication failed");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			// disconncet to node/machine
			if (semaphore != null) {
				semaphore.release();
			}
			if (connection != null) {
				connection.disconnect();
			}
			this.nodeConf.setStatus(statusFlag);
			LOG.debug("Hbase worker thread execution over ... ");
		}
	}
	
	public static boolean syncHBaseConfFolder(NodeConf nodeConf, HBaseConf conf) {
		SSHExec connection = null;
		try {
			LOG.info(nodeConf.getPublicIp(), "Sync HBase configuration folder.");
			
			String nodePublicIp = conf.getHbaseMasterNode().getPublicIp();
			// connect to HBase Master Node using Public IP
			connection = SSHUtils.connectToNode(nodePublicIp, conf.getUsername(), 
							conf.getPassword(), conf.getPrivateKey());

			List<String> excludeFileList = new ArrayList<String>();
			excludeFileList.add(Constant.Hadoop.FileName.XML_HDFS_SITE);
			excludeFileList.add(Constant.Hadoop.FileName.XML_CORE_SITE);
			
			// Use Private IP to sync folder
			AnkushTask syncConfFolder = new SyncFolder(nodeConf.getPrivateIp(), conf.getHBaseConfDir(), excludeFileList);
			Result res = connection.exec(syncConfFolder);
			if(!res.isSuccess) {
				LOG.error(nodeConf, "Could not sync HBase configuration directory.");
				return false;
			}
			
		} catch (Exception e) {
			LOG.error(nodeConf, "Could not sync HBase configuration directory.");
			LOG.debug(e.getMessage());
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}
	
}
