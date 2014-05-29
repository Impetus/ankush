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
package com.impetus.ankush.hadoop.ecosystem.hadoop2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.apache.commons.io.FilenameUtils;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.constant.Constant.JavaProcessScriptFile;
import com.impetus.ankush.common.framework.Deployable;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AppendFile;
import com.impetus.ankush.common.scripting.impl.ClearFile;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.scripting.impl.Move;
import com.impetus.ankush.common.scripting.impl.Remove;
import com.impetus.ankush.common.scripting.impl.RemoveText;
import com.impetus.ankush.common.scripting.impl.ReplaceText;
import com.impetus.ankush.common.scripting.impl.SetEnvVariable;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.HadoopRackAwareness;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;

// TODO: Auto-generated Javadoc
/**
 * The Class Hadoop2Deployer.
 * 
 * @author Akhil
 */
public class Hadoop2Deployer implements Deployable {
	
	/** The Constant RELATIVE_URL_RM_REST_API. */
	public static final String RELATIVE_URL_RM_REST_API = "/ws/v1/cluster/";
	
	/** The Constant RELATIVE_URL_APPPROXY_REST_API. */
	public static final String RELATIVE_URL_APPPROXY_REST_API = "/proxy/<application_id>/ws/v1/mapreduce/jobs/";
	
	/** The Constant APPPROXY_REST_API_TEXT_APPID. */
	public static final String APPPROXY_REST_API_TEXT_APPID = "<application_id>";
	
	/** The Constant RELATIVE_URL_JHS_REST_API. */
	public static final String RELATIVE_URL_JHS_REST_API = "/ws/v1/history/mapreduce/jobs/";
	
	/** The Constant YARN_REST_API_APPS. */
	public static final String YARN_REST_API_APPS = "apps";
	
	/** The Constant YARN_REST_API_APP_ATTEMPTS. */
	public static final String YARN_REST_API_APP_ATTEMPTS = "appattempts";
	
	/** The Constant RELPATH_CONF_DIR. */
	public static final String RELPATH_CONF_DIR = "etc/hadoop/";
	
	/** The Constant RELPATH_SCRIPT_DIR. */
	public static final String RELPATH_SCRIPT_DIR = "sbin/";
	
	/** The Constant RELPATH_COMMMAND_DIR. */
	public static final String RELPATH_COMMMAND_DIR = "bin/";
	
	/** The Constant DEFAULT_PORT_RESOURCEMANAGER. */
	private static final String DEFAULT_PORT_RESOURCEMANAGER = "8032";
	
	/** The Constant DEFAULT_PORT_HTTP_RESOURCEMANAGER. */
	public static final String DEFAULT_PORT_HTTP_RESOURCEMANAGER = "8088";
	
	/** The Constant DEFAULT_PORT_HTTP_NODEMANAGER. */
	public static final String DEFAULT_PORT_HTTP_NODEMANAGER = "8042";
	
	/** The Constant DEFAULT_PORT_HTTP_JOBHISTORYSERVER. */
	private static final String DEFAULT_PORT_HTTP_JOBHISTORYSERVER = "19888";
	
	/** The Constant DEFAULT_PORT_REST_JOBHISTORYSERVER. */
	public static final String DEFAULT_PORT_REST_JOBHISTORYSERVER = "10020";
	
	/** The Constant DEFAULT_PORT_SECNAMENODE. */
	private static final String DEFAULT_PORT_SECNAMENODE = "50090";
	
	/** The Constant DEFAULT_PORT_RESOURCE_TRACKER. */
	private static final String DEFAULT_PORT_RESOURCE_TRACKER = "8031";
	
	/** The Constant DEFAULT_PORT_SCHEDULER_ADDRESS. */
	private static final String DEFAULT_PORT_SCHEDULER_ADDRESS = "8030";
	
	/** The Constant DEFAULT_PORT_RPC_NAMENODE. */
	public static final String DEFAULT_PORT_RPC_NAMENODE = "8020";
	
	/** The Constant DEFAULT_PORT_JOURNAL_NODE. */
	private static final String DEFAULT_PORT_JOURNAL_NODE = "8485";
	
	/** The Constant STR_VERSION. */
	private static final String STR_VERSION = "$VERSION";
	
	/** The Constant STR_SLASH. */
	private static final String STR_SLASH = "/";
	
	/** The Constant CONNECTING_WITH_NODE. */
	private static final String CONNECTING_WITH_NODE = "Connecting with node : ";
	
	// Configuration manager to save the property file change records.
		/** The conf manager. */
	private ConfigurationManager confManager = new ConfigurationManager();
	
	
	/** The log. */
	private AnkushLogger LOG = new AnkushLogger(Constant.Component.Name.HADOOP2,
			Hadoop2Deployer.class);
	
	/**
	 * Gets the comp nodes.
	 *
	 * @param conf the conf
	 * @return the comp nodes
	 */
	private static Set<NodeConf> getCompNodes(Hadoop2Conf conf) {
		Set<NodeConf> nodeConfs = new HashSet<NodeConf>();
		nodeConfs.add(conf.getNamenode());
		if(conf.getSecondaryNamenode() != null) {
			if(!nodeConfs.contains(conf.getSecondaryNamenode())) {
				nodeConfs.add(conf.getSecondaryNamenode());	
			}	
		}
		for(NodeConf slave : conf.getSlaves()) {
			if(!nodeConfs.contains(slave)) {
				nodeConfs.add(slave);	
			}	
		}
		return nodeConfs;
	}
	
	//private boolean setupPasswordSSH
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#deploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean deploy(Configuration config) {
		final Hadoop2Conf conf = (Hadoop2Conf) config;
		LOG.setLoggerConfig(conf);
		
		LOG.info("Deploying Apache Hadoop...");

		Set<NodeConf> nodeConfs = new HashSet<NodeConf>();
		nodeConfs = Hadoop2Deployer.getCompNodes(conf);
		
		LOG.info("Setting Passwordless SSH from Namenode across all slave nodes...");
		boolean passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, conf.getNamenode(), conf.getNodeListForSsh(Constant.Role.NAMENODE));
		if(conf.isHaEnabled()) {
			LOG.info("Setting Passwordless SSH from StandBy Namenode across all slave nodes...");
			passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, conf.getStandByNamenode(), conf.getNodeListForSsh(Constant.Role.NAMENODE));
		}
		if(!conf.getResourceManagerNode().equals(conf.getNamenode())) {
			if(conf.isHaEnabled()) {
				if(!conf.getResourceManagerNode().equals(conf.getStandByNamenode())) {
					// PasswordLess SSH from ResourceManager to all nodes
					LOG.info("Setting Passwordless SSH from Resource Manager across all slave nodes...");
					passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, conf.getResourceManagerNode(), 
										 conf.getNodeListForSsh(Constant.Role.RESOURCEMANAGER));		
				}	
			}
			else {
				// PasswordLess SSH from ResourceManager to all nodes
				LOG.info("Setting Passwordless SSH from Resource Manager across all slave nodes...");
				passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, conf.getResourceManagerNode(), 
									 conf.getNodeListForSsh(Constant.Role.RESOURCEMANAGER));
			}			
		}
		if (!passwordlessStatus) {
			LOG.error("Could not set Passwordless SSH...");
			return false;
		}
		
		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeConfs) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = createNode(nodeConf, conf, false);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeConfs.size());
			status = status(nodeConfs);
			if(status) {
				
				if(conf.isHaEnabled()) {
					status = this.startJournalNodes(conf);
					if (!status) {
						LOG.error("Could not start Journal Nodes for the cluster");
						return false;
					}
				}
				
				SSHExec connection = SSHUtils.connectToNode(conf.getNamenode().getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				status = this.formatNamenode(connection, conf);
				if (!status) {
					LOG.error("Could not format NameNode");
					return false;
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

	/**
	 * Start journal nodes.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean startJournalNodes(final Hadoop2Conf conf) {
		return this.startServiceOnNodes(conf, conf.getJournalNodes(), Constant.Role.JOURNALNODE);
	}
	
	/**
	 * Start service on nodes.
	 *
	 * @param conf the conf
	 * @param nodeList the node list
	 * @param serviceName the service name
	 * @return true, if successful
	 */
	private boolean startServiceOnNodes(final Hadoop2Conf conf, final List<NodeConf> nodeList, final String serviceName) {
		final Semaphore semaphore = new Semaphore(nodeList.size());
		boolean status = false;
		try {
			for (final NodeConf nodeConf : nodeList) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = startServiceOnNode(nodeConf, conf, serviceName);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeList.size());
			status = status(new HashSet(nodeList));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}
	
	
	/**
	 * Start service on node.
	 *
	 * @param nodeConf the node conf
	 * @param conf the conf
	 * @param serviceName the service name
	 * @return true, if successful
	 */
	private boolean startServiceOnNode(NodeConf nodeConf, Hadoop2Conf conf, String serviceName) {
		SSHExec connection = null;
		Result res = null;
		try {
			LOG.debug(CONNECTING_WITH_NODE + nodeConf.getPublicIp());
			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				String daemonFileRelPath = Hadoop2Deployer.RELPATH_SCRIPT_DIR;
				daemonFileRelPath += JavaProcessScriptFile.getProcessDaemonFile(serviceName);
				String componentHome = conf.getComponentHome();
						
				StringBuffer command = new StringBuffer();
				command.append(componentHome).append(daemonFileRelPath).append(Constant.STR_SPACE);
				command.append(Constant.Hadoop.Command.START).append(Constant.STR_SPACE)
						.append(serviceName.toLowerCase());

				CustomTask manageServices = new ExecCommand(command.toString());

				res = connection.exec(manageServices);
				if (res.rc != 0) {
					LOG.error(nodeConf, "Could not start " + serviceName.toLowerCase() + " process");
					nodeConf.setStatus(false);
					return false;
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			// disconncet from node/machine
			if(connection != null) {
				connection.disconnect();	
			}
		}
		return true;
	}
	
	/**
	 * Format namenode.
	 *
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean formatNamenode(SSHExec connection, Hadoop2Conf conf) {
		try {
			Result res = null;
			LOG.info("Formatting Hadoop namenode..");

			// format namenode
			CustomTask formatNamenode = new ExecCommand("yes | " 
							+ conf.getComponentHome()
							+ Hadoop2Deployer.RELPATH_COMMMAND_DIR
							+ Constant.Hadoop.Command.HDFS
							+ Constant.STR_SPACE
							+ Constant.Hadoop.Command.NAMENODE_FORMAT);
			res = connection.exec(formatNamenode);
			if(!res.isSuccess) {
				LOG.error(conf.getNamenode(), "Could not format hadoop namenode.");
				return false;	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}
	
	/**
	 * Copy metadata content.
	 *
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean copyMetadataContent(SSHExec connection, Hadoop2Conf conf) {
		try {
			Result res = null;
			LOG.info("Copy NameNode metadata directories to unformatted NameNode...");

			// Copy NameNode metadata directories to unformatted NameNode
			CustomTask setupStandByNamenode = new ExecCommand(
							conf.getComponentHome()
							+ Hadoop2Deployer.RELPATH_COMMMAND_DIR
							+ Constant.Hadoop.Command.HDFS
							+ Constant.STR_SPACE
							+ Constant.Hadoop.Command.NAMENODE_COPY_METADATA);
			res = connection.exec(setupStandByNamenode);
			if(!res.isSuccess) {
				LOG.error(conf.getStandByNamenode(), "Could not copy metadata NameNode to StandBy NameNode.");
				conf.getStandByNamenode().setStatus(false);
				return false;	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}
	
	/**
	 * Configure hdfs site xml.
	 *
	 * @param nodeConf the node conf
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean configureHdfsSiteXml(NodeConf nodeConf, SSHExec connection, Hadoop2Conf conf) {
		Result res = null;
		boolean status = true;
		try 
		{
			
			String nameDir = conf.getDfsNameDir();
			String dataDir = conf.getDfsDataDir();

			AnkushTask removeHadoopDirs = new Remove(nameDir);
			res = connection.exec(removeHadoopDirs);
			if (!res.isSuccess) {
				LOG.error("Could not remove " + nameDir + " directory" );
				return false;
			}
			
			removeHadoopDirs = new Remove(dataDir);
			res = connection.exec(removeHadoopDirs);
			if (!res.isSuccess) {
				LOG.error("Could not remove " + dataDir + " directory" );
				return false;
			}
			
			// configuring hdfs-site.xml file in $HADOOP_CONF_DIR
			String hdfssite = conf.getComponentHome() + Hadoop2Deployer.RELPATH_CONF_DIR +  Constant.Hadoop.FileName.XML_HDFS_SITE;
			String hdfsNameDirValue = Constant.URI_FILE_PREFIX + nameDir;
			String hdfsDataDirValue = Constant.URI_FILE_PREFIX + dataDir;
			
			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put("dfs.replication", 
					conf.getDfsReplicationFactor() + "");
			paramList.put("dfs.permissions.enabled", 
					"false");
			paramList.put("dfs.namenode.name.dir", 
					hdfsNameDirValue);
			paramList.put("dfs.datanode.data.dir", 
					hdfsDataDirValue);
			String valAddrSNNIp = HadoopUtils.DEFAULT_NODE_IP;
			if(conf.getSecondaryNamenode() != null) {
				valAddrSNNIp = conf.getSecondaryNamenode().getPrivateIp();
			}
			paramList.put("dfs.namenode.secondary.http-address", 
					valAddrSNNIp + ":" + Hadoop2Deployer.DEFAULT_PORT_SECNAMENODE);
			
			if(conf.isHaEnabled()) {
				paramList.put("dfs.nameservices", conf.getNameserviceId());
				paramList.put("dfs.ha.namenodes." + conf.getNameserviceId(), conf.getNameNodeId1() + "," + conf.getNameNodeId2());
				paramList.put("dfs.namenode.rpc-address." + conf.getNameserviceId() + "." + conf.getNameNodeId1(),
						conf.getActiveNamenode().getPrivateIp() + ":" + Hadoop2Deployer.DEFAULT_PORT_RPC_NAMENODE);
				paramList.put("dfs.namenode.rpc-address." + conf.getNameserviceId() + "." + conf.getNameNodeId2(),
						conf.getStandByNamenode().getPrivateIp() + ":" + Hadoop2Deployer.DEFAULT_PORT_RPC_NAMENODE);
				paramList.put("dfs.namenode.http-address." + conf.getNameserviceId() + "." + conf.getNameNodeId1(),
						conf.getActiveNamenode().getPrivateIp() + ":" + Hadoop1Deployer.DEFAULT_PORT_HTTP_NAMENODE);
				paramList.put("dfs.namenode.http-address." + conf.getNameserviceId() + "." + conf.getNameNodeId2(),
						conf.getStandByNamenode().getPrivateIp() + ":" + Hadoop1Deployer.DEFAULT_PORT_HTTP_NAMENODE);
				
				StringBuilder journalNodeList = new StringBuilder("qjournal://");
				for(NodeConf journalNode : conf.getJournalNodes()) {
					journalNodeList.append(journalNode.getPrivateIp() + ":" + DEFAULT_PORT_JOURNAL_NODE);
					journalNodeList.append(";");
				}
				String valJournalNodeProp = journalNodeList.toString().substring(0, journalNodeList.length() -1) + "/" + conf.getNameserviceId();
				paramList.put("dfs.namenode.shared.edits.dir", valJournalNodeProp);
				paramList.put("dfs.journalnode.edits.dir", conf.getJournalNodeEditsDir());
				paramList.put("dfs.client.failover.proxy.provider." + conf.getNameserviceId(), "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
				paramList.put("dfs.ha.fencing.methods", "shell(/bin/true)");
				if(conf.isAutomaticFailoverEnabled()) {
					paramList.put("dfs.ha.automatic-failover.enabled." + conf.getNameserviceId(), "true");
					paramList.put("ha.zookeeper.quorum", conf.getHaZkQourumValue());
				}
				else {
					paramList.put("dfs.ha.automatic-failover.enabled." + conf.getNameserviceId(), "false");
				}				
			}
			
			for(String propertyName : paramList.keySet()) {
					status = AgentUtils.addPropertyToFile(conf, connection, nodeConf, hdfssite, propertyName, paramList.get(propertyName));
					if (!status) {
						return false;
				}	
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} 
		return true;
	}
	
	/**
	 * Configure mapred site xml.
	 *
	 * @param nodeConf the node conf
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean configureMapredSiteXml(NodeConf nodeConf, SSHExec connection, Hadoop2Conf conf) {
		boolean status = true;
		try 
		{
			// configuring mapred-site.xml file in $HADOOP_CONF_DIR
			String mapredsite = conf.getComponentHome() + Hadoop2Deployer.RELPATH_CONF_DIR +  Constant.Hadoop.FileName.XML_MAPRED_SITE;
			
			String mapredsiteTemplate = conf.getComponentHome() + Hadoop2Deployer.RELPATH_CONF_DIR +  "mapred-site.xml.template";
			
			AnkushTask moveFile = new Move(mapredsiteTemplate, mapredsite);
			Result res = connection.exec(moveFile); 
			if(res.rc != 0 ) {
				LOG.error(nodeConf, "Could not configure " + Constant.Hadoop.FileName.XML_MAPRED_SITE + " file.");
				return false;	
			}
			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put("mapreduce.framework.name", 
					conf.getMapredFramework());
			paramList.put("mapreduce.cluster.temp.dir", 
					Constant.URI_FILE_PREFIX + conf.getMapRedTmpDir());
			if(conf.isStartJobHistoryServer()) {
				paramList.put("mapreduce.jobhistory.address", 
					conf.getJobHistoryServerNode().getPrivateIp() + ":" + Hadoop2Deployer.DEFAULT_PORT_REST_JOBHISTORYSERVER);
			}
			for(String propertyName : paramList.keySet()) {
				status = AgentUtils.addPropertyToFile(conf, connection, nodeConf, mapredsite, propertyName, paramList.get(propertyName));
				if (!status) {
					return false;
				}	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} 
		return true;
	}
	
	/**
	 * Configure yarn site xml.
	 *
	 * @param nodeConf the node conf
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean configureYarnSiteXml(NodeConf nodeConf, SSHExec connection, Hadoop2Conf conf) {
		boolean status = true;
		try 
		{
			// configuring yarn-site.xml file in $HADOOP_CONF_DIR
			String yarnsite = conf.getComponentHome() + Hadoop2Deployer.RELPATH_CONF_DIR +  Constant.Hadoop.FileName.XML_YARN_SITE;
			String valAuxServices = AppStoreWrapper.getAnkushConfReader().
									getStringValue("hadoop2.propval.auxservices." + conf.getComponentVersion());
			String keyMRShuffleClass = AppStoreWrapper.getAnkushConfReader().
										getStringValue("hadoop2.propname.mrshuffleclass." + conf.getComponentVersion());
			
			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put("yarn.nodemanager.aux-services", valAuxServices);
			paramList.put(keyMRShuffleClass, "org.apache.hadoop.mapred.ShuffleHandler");
			paramList.put("yarn.resourcemanager.address", 
					conf.getResourceManagerNode().getPrivateIp() + ":" + Hadoop2Deployer.DEFAULT_PORT_RESOURCEMANAGER);
			paramList.put("yarn.resourcemanager.resource-tracker.address", 
					conf.getResourceManagerNode().getPrivateIp() + ":" + Hadoop2Deployer.DEFAULT_PORT_RESOURCE_TRACKER);
			paramList.put("yarn.resourcemanager.scheduler.address", 
					conf.getResourceManagerNode().getPrivateIp() + ":" + Hadoop2Deployer.DEFAULT_PORT_SCHEDULER_ADDRESS);
			
			if(conf.isWebProxyEnabled()) {
				paramList.put("yarn.web-proxy.address", 
						conf.getWebAppProxyNode().getPrivateIp() + ":" + conf.getWebAppProxyPort());
			}

			for(String propertyName : paramList.keySet()) {
				status = AgentUtils.addPropertyToFile(conf, connection, nodeConf, yarnsite, propertyName, paramList.get(propertyName));
				if (!status) {
					return false;
				}	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} 
		return true;
	}
	
	/**
	 * Configure core site xml.
	 *
	 * @param nodeConf the node conf
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean configureCoreSiteXml(NodeConf nodeConf, SSHExec connection, Hadoop2Conf conf) {
		boolean status = true;
		try 
		{
			// configuring core-site.xml file in $HADOOP_CONF_DIR
			String coresite = conf.getComponentHome() + Hadoop2Deployer.RELPATH_CONF_DIR +  Constant.Hadoop.FileName.XML_CORE_SITE ;
			String valFsDefaultFS = HadoopUtils.HADOOP_URI_PREFIX + conf.getNamenode().getPrivateIp() + ":" + Hadoop2Deployer.DEFAULT_PORT_RPC_NAMENODE;
			if(conf.isHaEnabled()) {
				valFsDefaultFS = HadoopUtils.HADOOP_URI_PREFIX + conf.getNameserviceId();
			}
			Map<String, String> paramList = new HashMap<String, String>();
			paramList.put("fs.defaultFS", valFsDefaultFS);
			paramList.put("hadoop.tmp.dir", 
					Constant.URI_FILE_PREFIX + conf.getHadoopTmpDir());
			
			for(String propertyName : paramList.keySet()) {
				status = AgentUtils.addPropertyToFile(conf, connection, nodeConf, coresite, propertyName, paramList.get(propertyName));
				if (!status) {
					return false;
				}	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} 
		return true;
	}
	
	/**
	 * Configure site xml files.
	 *
	 * @param nodeConf the node conf
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean configureSiteXmlFiles(NodeConf nodeConf, SSHExec connection, Hadoop2Conf conf) {
		boolean statusFlag = true;
		LOG.info(nodeConf.getPublicIp(), "Configuring site.xml files...");
		try 
		{
			statusFlag = this.configureCoreSiteXml(nodeConf, connection, conf);
			if(!statusFlag){
				return false;
			}
			statusFlag = this.configureHdfsSiteXml(nodeConf, connection, conf);
			if(!statusFlag){
				return false;
			}
			statusFlag = this.configureMapredSiteXml(nodeConf, connection, conf);
			if(!statusFlag){
				return false;
			}
			statusFlag = this.configureYarnSiteXml(nodeConf, connection, conf);
			if(!statusFlag){
				return false;
			}
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} 
		return true;
	}
	
	/**
	 * Update env variables.
	 *
	 * @param nodeConf the node conf
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean updateEnvVariables(NodeConf nodeConf, SSHExec connection, Hadoop2Conf conf) {
		Result res = null;
		LOG.info(nodeConf.getPublicIp(), "Configuring environment variables...");
		try 
		{
			String componentHome = conf.getComponentHome();
			String envVariables = conf.getBashrcContents();
			// set environment variables in .bashrc
			
			AnkushTask exportHadoopHome = new SetEnvVariable(
					conf.getPassword(), HadoopUtils.KEY_HADOOP_HOME,
					conf.getComponentHome(), Constant.ETCENV_FILE);

			res = connection.exec(exportHadoopHome);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not set " + HadoopUtils.KEY_HADOOP_HOME + " in " + Constant.ETCENV_FILE + "file.");
				return false;
			}
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(), Constant.ETCENV_FILE, nodeConf.getPublicIp(),
					HadoopUtils.KEY_HADOOP_HOME, conf.getComponentHome());
			
			exportHadoopHome = new SetEnvVariable(
					conf.getPassword(), HadoopUtils.KEY_HADOOP_CONF_DIR,
					conf.getHadoopConfDir(), Constant.ETCENV_FILE);

			res = connection.exec(exportHadoopHome);
			if (!res.isSuccess) {
				LOG.error(nodeConf,
						"Could not set " + HadoopUtils.KEY_HADOOP_CONF_DIR+ " in " + Constant.ETCENV_FILE + "file.");
				return false;
			}
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(), Constant.ETCENV_FILE, nodeConf.getPublicIp(),
					HadoopUtils.KEY_HADOOP_CONF_DIR, conf.getHadoopConfDir());
			
			List<String> envFileList = new ArrayList<String>();
			envFileList.add(Constant.BASHRC_FILE);
			envFileList.add(conf.getHadoopConfDir() + Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP);
			envFileList.add(conf.getHadoopConfDir() + Constant.Hadoop.FileName.SCRIPT_ENV_YARN);
			envFileList.add(conf.getHadoopConfDir() + Constant.Hadoop.FileName.SCRIPT_ENV_MAPRED);
			
			for(String fileName : envFileList) {
				AnkushTask updateEnvFile = new AppendFile(envVariables, fileName);
				res = connection.exec(updateEnvFile);
				if (!res.isSuccess) {
					LOG.error(nodeConf, "Could not set Environment variables in " + fileName + " file.");
					return false;
				}
//				confManager.saveConfiguration(conf.getClusterDbId(),
//				conf.getCurrentUser(), FilenameUtils.getName(fileName), nodeConf.getPublicIp(),
//				"Environment Variables", envVariables);
			}
			
			// set JAVA_HOME in hadop-env.sh
			String replacementTextJavaHome = AppStoreWrapper.getAnkushConfReader().getStringValue("hadoop2.javahome.hadoopenv.replacementtext"); 
			String javaHome = conf.getJavaHome(nodeConf.getPublicIp());
			
			String password = conf.getPassword();
			
			boolean authUsingPassword = conf.getClusterConf().isAuthTypePassword();
			if(!authUsingPassword) {
				password = conf.getPrivateKey();
			}
			
			if (javaHome == null || javaHome.isEmpty()) {
				javaHome = SSHUtils.getJavaHome(nodeConf.getPublicIp(), conf.getUsername(),
						password, authUsingPassword);
			}
			replacementTextJavaHome = replacementTextJavaHome.replaceAll
					(AppStoreWrapper.getAnkushConfReader().getStringValue("hadoop2.template.javahome.value"), javaHome);

			String targetTextJavaHome = AppStoreWrapper.getAnkushConfReader().getStringValue("hadoop2.javahome.hadoopenv.targettext");
			
			String filePathHadoopEnvScript = componentHome + Hadoop2Deployer.RELPATH_CONF_DIR + Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP;
			
			AnkushTask updateJavaHome = new ReplaceText(targetTextJavaHome, replacementTextJavaHome, filePathHadoopEnvScript, false, conf.getPassword()); 
			res = connection.exec(updateJavaHome);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not set JAVA_HOME in " + Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP + " file.");
				return false;
			}
			confManager.saveConfiguration(conf.getClusterDbId(),
					conf.getCurrentUser(), Constant.Hadoop.FileName.SCRIPT_ENV_HADOOP, nodeConf.getPublicIp(),
					"JAVA_HOME", javaHome);
			
			// set JAVA_HOME in yarn-env.sh
			targetTextJavaHome = AppStoreWrapper.getAnkushConfReader().getStringValue("hadoop2.javahome.yarnenv.targettext");
			String yarnEnvFile = componentHome + Hadoop2Deployer.RELPATH_CONF_DIR + Constant.Hadoop.FileName.SCRIPT_ENV_YARN ;
			updateJavaHome = new ReplaceText(targetTextJavaHome, replacementTextJavaHome, yarnEnvFile, false, conf.getPassword()); 
			res = connection.exec(updateJavaHome);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not set JAVA_HOME in " + Constant.Hadoop.FileName.SCRIPT_ENV_YARN + " file.");
				return false;
			}
			confManager.saveConfiguration(conf.getClusterDbId(), conf.getCurrentUser(), 
					Constant.Hadoop.FileName.SCRIPT_ENV_YARN, nodeConf.getPublicIp(),
					"JAVA_HOME", javaHome);
			
			
			// set JAVA_HOME in mapred-env.sh
			String mapredEnvFile = componentHome + Hadoop2Deployer.RELPATH_CONF_DIR + Constant.Hadoop.FileName.SCRIPT_ENV_MAPRED;
			updateJavaHome = new ReplaceText(targetTextJavaHome, replacementTextJavaHome, mapredEnvFile, false, conf.getPassword()); 
			res = connection.exec(updateJavaHome);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not set JAVA_HOME in " + Constant.Hadoop.FileName.SCRIPT_ENV_MAPRED + " file.");
				return false;
			}
			confManager.saveConfiguration(conf.getClusterDbId(), conf.getCurrentUser(), 
					Constant.Hadoop.FileName.SCRIPT_ENV_MAPRED, nodeConf.getPublicIp(),
					"JAVA_HOME", javaHome);
			
			return true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * Configure slaves files.
	 *
	 * @param nodeConf the node conf
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean configureSlavesFiles(NodeConf nodeConf, SSHExec connection, Hadoop2Conf conf){
		Result res = null;
		try 
		{
			LOG.info(nodeConf.getPublicIp(), "Configuring Slaves file...");
			// configuring slaves file
			StringBuilder slaveList = new StringBuilder();

			for (NodeConf sNode : conf.getSlaves()) {
				slaveList.append(sNode.getPrivateIp()).append(
						Constant.LINE_SEPERATOR);
			}
			// Slaves File Location
			String slaves = conf.getComponentHome() + Hadoop2Deployer.RELPATH_CONF_DIR + Constant.Hadoop.FileName.SLAVES;
			
			AnkushTask clearFile = new ClearFile(slaves);
			AnkushTask slaveFile = new AppendFile(slaveList.toString(), slaves);

			res = connection.exec(clearFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not clear " + slaves + " file.");
				return false;
			}
			res = connection.exec(slaveFile);
			if (!res.isSuccess) {
				LOG.error(nodeConf, "Could not configure " + slaves + " file.");
				return false;
			}
//			confManager.saveConfiguration(conf.getClusterDbId(), conf.getCurrentUser(), 
//					Constant.Hadoop.FileName.SLAVES, nodeConf.getPublicIp(),
//					"IpValues", slaveList.toString());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} 
		return true;
	}

	/**
	 * Creates the node.
	 *
	 * @param nodeConf the node conf
	 * @param conf the conf
	 * @param isAddNodeOperation the is add node operation
	 * @return true, if successful
	 */
	private boolean createNode(NodeConf nodeConf, Hadoop2Conf conf, boolean isAddNodeOperation) {
		//boolean isAddNodeOperation
		SSHExec connection = null;
		Result res = null;
		boolean statusFlag = true;
		try {
			LOG.debug(CONNECTING_WITH_NODE + nodeConf.getPublicIp());
			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				
				String componentInstallationPath = conf.getInstallationPath();
				if (componentInstallationPath.contains(STR_VERSION)) {
					componentInstallationPath = FilenameUtils
							.getFullPath(componentInstallationPath);
				}
				if (componentInstallationPath.contains(STR_VERSION)) {
					componentInstallationPath = componentInstallationPath
							.replace(STR_SLASH + STR_VERSION, "");
				}

				conf.setInstallationPath(componentInstallationPath);

				LOG.debug("Create directory - " + conf.getInstallationPath());

				// make installation directory if not exists
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(nodeConf,
							"Could not create installation directory"
									+ mkInstallationPath);
					return false;
				}

				// get and extract tarball
				LOG.debug("Get and extract tarball");
				boolean isSuccessfull = SSHUtils.getAndExtractComponent(connection, conf, Constant.Component.Name.HADOOP2);

				// if component exists at destination
				if (isSuccessfull) {
					String componentHome = conf.getInstallationPath()
							+ "hadoop-" + conf.getComponentVersion();
					// setting componentHome in bean
					conf.setComponentHome(componentHome);

					statusFlag = this.updateEnvVariables(nodeConf, connection, conf);
					if(!statusFlag) {
						return false;
					}
					
					// make hadoop tmp direcotry
					AnkushTask mkHadoopTmp = new MakeDirectory(conf.getHadoopTmpDir());
					res = connection.exec(mkHadoopTmp);
					if (!res.isSuccess) {
						LOG.debug("Could not create temporary directory");
						return false;
					}

					if(isAddNodeOperation) {
						if(!HadoopUtils.syncHadoopConfFolder(nodeConf, conf)) {
							return false;
						}
					} else {
						statusFlag = this.configureSiteXmlFiles(nodeConf, connection, conf);
						if(!statusFlag) {
							return false;
						}
						statusFlag = this.configureSlavesFiles(nodeConf, connection, conf);
						if(!statusFlag) {
							return false;
						}
						// Configuring Rack Awareness
						if(conf.isRackEnabled()) {
							boolean isRackSetupSuccess = HadoopUtils.setupRackAwareness(connection, nodeConf, conf);
							if (!isRackSetupSuccess) {
								LOG.error(nodeConf, "Could not setup rack awareness for hadoop");
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
					
					
					// Add processes name of nodes in agent.properties file.
					statusFlag = HadoopUtils.addHadoopAgentInfo(nodeConf, conf, connection);
					if(!statusFlag) {
						LOG.error(nodeConf,"Error in adding Hadoop agent informations..");
						return false;
					}
					
					if(conf.isHaEnabled()) {
						if (conf.isNamenode(nodeConf)) {
							statusFlag = HadoopUtils.addClassEntry(connection, conf, 
									Constant.TaskableClass.Hadoop_HA_Update_NN_Role);
							if(!statusFlag) {
								LOG.error(nodeConf, "Could not add class entry for " +  Constant.TaskableClass.Hadoop_HA_Update_NN_Role);
								return false;
							}	
						}
					}
				} else {
					LOG.error(nodeConf, "Could not extract tarball");
					return false;
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			// disconncet from node/machine
			if(connection != null) {
				connection.disconnect();	
			}
		}
		return true;
	}

	//private boolean 
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.framework.Deployable#undeploy(com.impetus.ankush
	 * .common.framework.config.Configuration)
	 */
	@Override
	public boolean undeploy(Configuration config) {
		
		final Hadoop2Conf conf = (Hadoop2Conf) config;
		LOG.setLoggerConfig(conf);
		
		LOG.info("Undeploying Apache Hadoop...");

		Set<NodeConf> nodeConfs = new HashSet<NodeConf>();
		nodeConfs = Hadoop2Deployer.getCompNodes(conf);

		List<NodeConf> slavesList = new ArrayList<NodeConf>();
		slavesList.addAll(conf.getSlaves());
		
		if(conf.getSecondaryNamenode() != null) {
			if(!slavesList.contains(conf.getSecondaryNamenode())) {
				slavesList.add(conf.getSecondaryNamenode());	
			}	
		}
		
		final Semaphore semaphore = new Semaphore(nodeConfs.size());
		try {
			for (final NodeConf nodeConf : nodeConfs) {
				semaphore.acquire();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						boolean status = removeNode(nodeConf, conf);
						nodeConf.setStatus(status);
						if (semaphore != null) {
							semaphore.release();
						}
					}
				});
			}
			semaphore.acquire(nodeConfs.size());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return true;
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
	private boolean removeNode(NodeConf nodeConf, Hadoop2Conf conf) {
		SSHExec connection = null;
		try {
			LOG.debug(CONNECTING_WITH_NODE + nodeConf.getPublicIp());
			// connect to remote node
			connection = SSHUtils.connectToNode(nodeConf.getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				
				List<String> dirList = new ArrayList<String>();
				dirList.add(conf.getHadoopTmpDir());
				dirList.add(conf.getMapRedTmpDir());
				dirList.add(conf.getDfsNameDir());
				dirList.add(conf.getDfsDataDir());
				dirList.add(conf.getComponentHome());
				
				// Remove Journal Node Dir if HA is enabled
				if(conf.isHaEnabled()) {
					dirList.add(conf.getJournalNodeEditsDir());
				}	
				for(String dirName : dirList) {
					// remove directory
					AnkushTask removeDir = new Remove(dirName);
					connection.exec(removeDir);			
				}
				
				// Remove Contents from /etc/environment & .bashrc files
				List<String> fileContents = Arrays.asList(conf.getBashrcContents().split(Constant.LINE_SEPERATOR));
				RemoveText prepFile = new RemoveText(fileContents, Constant.BASHRC_FILE, conf.getPassword());
				connection.exec(prepFile);
				return true;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			// disconncet from node/machine
			if(connection != null) {
				connection.disconnect();	
			}
		}
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
		Hadoop2Conf conf = (Hadoop2Conf) config;
		LOG.info("Starting Hadoop services...");
		boolean status = true;
		
		if(conf.isHaEnabled()) {
			// Start a HA Cluster
			status = this.startHACluster(conf);
		}
		else {
			// Start a Non-HA Cluster
			status = this.startNonHACluster(conf);
		}
		
		return status;
	}
	
	/**
	 * Start all data nodes from name node.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean startAllDataNodesFromNameNode(final Hadoop2Conf conf) {
		SSHExec connection = null;
		try {
			Result res = null;
			connection = SSHUtils.connectToNode(conf.getNamenode().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// Start DataNodes
			CustomTask startDataNodes = new ExecCommand(
							conf.getComponentHome()
							+ Hadoop2Deployer.RELPATH_SCRIPT_DIR
							+ Constant.Hadoop.FileName.SCRIPT_DAEMONS_FILE_HADOOP
							+ Constant.STR_SPACE
							+ Constant.Hadoop.Command.START
							+ Constant.STR_SPACE
							+ Constant.Hadoop.Command.DAEMON_DATANODE);
			res = connection.exec(startDataNodes);
			if(!res.isSuccess) {
				LOG.error(conf.getNamenode(), "Could not start datanodes...");
				conf.getNamenode().setStatus(false);
				return false;	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
		return true;
	}
	
	/**
	 * Start zkfc service.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean startZKFCService(final Hadoop2Conf conf) {
		List<NodeConf> nameNodeList = new ArrayList<NodeConf>();
		nameNodeList.add(conf.getNamenode());
		nameNodeList.add(conf.getStandByNamenode());
		return this.startServiceOnNodes(conf, nameNodeList, Constant.Role.DFSZKFAILOVERCONTROLLER);
	}
	
	/**
	 * Start ha cluster.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean startHACluster(Hadoop2Conf conf) {
		
		boolean status = true;
		SSHExec connection = null;
		try {
			LOG.info("Starting Active Namenode...");
			status = this.startServiceOnNode(conf.getActiveNamenode(), conf, Constant.Role.NAMENODE);
			connection = SSHUtils.connectToNode(conf.getStandByNamenode().getPublicIp(),
					conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			status = this.copyMetadataContent(connection, conf);
			if(!status) {
				return false;
			}
			
			LOG.info("Starting StandBy Namenode...");
			status = this.startServiceOnNode(conf.getStandByNamenode(), conf, Constant.Role.NAMENODE);
			if(!status) {
				return false;
			}
			if(conf.isAutomaticFailoverEnabled()) {
				LOG.info("Initializing HA state in ZooKeeper...");
				connection = SSHUtils.connectToNode(conf.getActiveNamenode().getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				
				status = this.initializeHAInZooKeeper(connection, conf);
				if(!status) {
					return false;
				}	
				LOG.info("Starting DFSZKFailoverController on Namenodes...");
				status = this.startZKFCService(conf);
				if(!status) {
					return false;
				}
			}
			else {
				LOG.info("Starting state transition from StandBy to Active NameNode...");
				connection = SSHUtils.connectToNode(conf.getActiveNamenode().getPublicIp(),
						conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				
				CustomTask nameNodeTransition = new ExecCommand(conf.getComponentHome()
						+ Hadoop2Deployer.RELPATH_COMMMAND_DIR
						+ Constant.Hadoop.Command.HDFS
						+ Constant.STR_SPACE
						+ Constant.Hadoop.Command.TRANSITION_TO_ACTIVE
						+ Constant.STR_SPACE
						+ conf.getNameNodeId1());
					Result res = connection.exec(nameNodeTransition);
					if(!res.isSuccess) {
						LOG.error(conf.getNamenode(), "Could not complete state transition from StandBy to Active NameNode.");
						return false;	
					}
			}
			
			LOG.info("Starting DataNodes...");
			status = this.startAllDataNodesFromNameNode(conf);
			if(!status) {
				return false;
			}
			return this.startYarnServices(conf);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 * Initialize ha in zoo keeper.
	 *
	 * @param connection the connection
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean initializeHAInZooKeeper(SSHExec connection, Hadoop2Conf conf) {
		try {
			Result res = null;
			// Initializing HA state In ZooKeeper
			CustomTask initializeHAInZooKeeper = new ExecCommand(
							conf.getComponentHome()
							+ Hadoop2Deployer.RELPATH_COMMMAND_DIR
							+ Constant.Hadoop.Command.HDFS
							+ Constant.STR_SPACE
							+ Constant.Hadoop.Command.INITIALIAZE_HA_IN_ZOOKEEPER);
			res = connection.exec(initializeHAInZooKeeper);
			if(!res.isSuccess) {
				LOG.error(conf.getActiveNamenode(), "Could not initialize HA state in ZooKeeper.");
				conf.getActiveNamenode().setStatus(false);
				return false;	
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		}
		finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
		return true;
		
	}
	
	/**
	 * Start yarn services.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean startYarnServices(Hadoop2Conf conf) {
		boolean status = true;
		SSHExec connection = null;
		Result res = null;
		try {
			String componentHome = conf.getComponentHome();
			String startYarnCmd = componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR + Constant.Hadoop.FileName.SCRIPT_YARN_START;
			CustomTask startYarn = new ExecCommand(startYarnCmd);	
			connection = SSHUtils.connectToNode(conf.getResourceManagerNode()
					.getPublicIp(), conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			res = connection.exec(startYarn);
			if (res.rc != 0) {
				LOG.error("Could not start YARN processes.");
				return false;
			}
			
			if(conf.isStartJobHistoryServer()) {
				
				String startJHSCmd = componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR
									+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_MRJOBHISTORY
									+ " " + Constant.Hadoop.Command.START 
									+ " " + Constant.RoleCommandName.getCommandName(Constant.Role.JOBHISTORYSERVER);  
				CustomTask startJHS = new ExecCommand(startJHSCmd);
				if(connection != null) {
					connection.disconnect();
				}
				connection = SSHUtils.connectToNode(conf.getJobHistoryServerNode()
						.getPublicIp(), conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				res = connection.exec(startJHS);
				if (res.rc != 0) {
					LOG.error("Could not start Job History Server.");
					return false;
				}
			}
			
			if(conf.isWebProxyEnabled()) {
				
				String startJHSCmd = componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR
									+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_YARN
									+ " " + Constant.Hadoop.Command.START 
									+ " " + Constant.RoleCommandName.getCommandName(Constant.Role.WEBAPPPROXYSERVER);  
				CustomTask startWebAppProxy = new ExecCommand(startJHSCmd);
				if(connection != null) {
					connection.disconnect();
				}
				connection = SSHUtils.connectToNode(conf.getWebAppProxyNode()
						.getPublicIp(), conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				res = connection.exec(startWebAppProxy);
				if (res.rc != 0) {
					LOG.error("Could not start Web Application Proxy Server.");
					return false;
				}
			}
			
			// sbin/yarn-daemon.sh start proxyserver
			
			
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
		return status;
	}
	
	/**
	 * Start non ha cluster.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean startNonHACluster(Hadoop2Conf conf) {
		
		String componentHome = conf.getComponentHome();
		// commands to execute
		CustomTask starthdfs = new ExecCommand(componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR + Constant.Hadoop.FileName.SCRIPT_DFS_START); 
		
		Result res = null;
		// connect to namenode and execute commands
		
		SSHExec connection = null;

		try {
			connection = SSHUtils.connectToNode(conf.getNamenode()
					.getPublicIp(), conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			
			// if connected
			if (connection != null) {
				res = connection.exec(starthdfs);
				if (res.rc != 0) {
					LOG.error("Could not start HDFS processes");
					return false;
				}
				return this.startYarnServices(conf);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return false;
		} finally {
			// disconncet from node/machine
			if(connection != null) {
				connection.disconnect();	
			}
		}
		return true;
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
		Hadoop2Conf conf = (Hadoop2Conf) config;
		LOG.debug("Stopping Apache Hadoop 2 using configuration : " + conf);
		NodeConf namenode = conf.getNamenode();
		return stopCluster(conf, namenode);
	}

	
	/**
	 * Stop zkfc processes.
	 *
	 * @param conf the conf
	 * @return true, if successful
	 */
	private boolean stopZkfcProcesses(Hadoop2Conf conf) {
		Result res = null;
		SSHExec connection = null;

		// connect to ha Active & StandBy NameNodes and execute command to stop ZkFC process
		try {
			String componentHome = conf.getComponentHome();
			String serviceName = Constant.Role.DFSZKFAILOVERCONTROLLER;
			String daemonFileRelPath = Hadoop2Deployer.RELPATH_SCRIPT_DIR;
			daemonFileRelPath += JavaProcessScriptFile.getProcessDaemonFile(serviceName);
					
			StringBuffer commandStopZkfc = new StringBuffer();
			commandStopZkfc.append(componentHome).append(daemonFileRelPath).append(Constant.STR_SPACE);
			commandStopZkfc.append(Constant.Hadoop.Command.STOP).append(Constant.STR_SPACE)
					.append(serviceName.toLowerCase());
			
			CustomTask stopZkfc = new ExecCommand(commandStopZkfc.toString());
			
			for(String nameNodeIp : conf.getHaNameNodesPublicIp()) {
				LOG.debug(CONNECTING_WITH_NODE + nameNodeIp);
				// connect to remote node
				connection = SSHUtils.connectToNode(nameNodeIp, conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				if(connection == null) {
					LOG.error("Could not stop zkfc process on Node - " + nameNodeIp + " : Unable to connect to node");
					continue;
				}
				connection.exec(stopZkfc);
				if(connection != null) {
					connection.disconnect();
				}	
			}
			return true;	
		}catch(Exception e) {
			LOG.error(e.getMessage(), e);
			return true;	
		}
		finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
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
	private boolean stopCluster(Hadoop2Conf conf, NodeConf nodeConf) {
		
		LOG.info("Stopping Hadoop 2 services...");

		// commands to execute
		String componentHome = conf.getComponentHome();
		
		CustomTask stophdfs = new ExecCommand(componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR + Constant.Hadoop.FileName.SCRIPT_DFS_STOP);
		CustomTask stopYarn = new ExecCommand(componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR + Constant.Hadoop.FileName.SCRIPT_YARN_STOP);
				
		Result res = null;
		SSHExec connection = null;

		// connect to namenode and execute commands
		try {
			
			if(conf.isStartJobHistoryServer()) {
				LOG.debug(CONNECTING_WITH_NODE
						+ conf.getJobHistoryServerNode().getPublicIp());
				
				connection = SSHUtils.connectToNode(conf.getJobHistoryServerNode()
						.getPublicIp(), conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				CustomTask stopJHS = new ExecCommand(componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR
						+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_MRJOBHISTORY
						+ " " + Constant.Hadoop.Command.STOP
						+ " " + Constant.RoleCommandName.getCommandName(Constant.Role.JOBHISTORYSERVER));
				res = connection.exec(stopJHS);
				if (res.rc != 0) {
					LOG.error("Could not stop Job History Server process");
				}
				if(connection != null) {
					connection.disconnect();	
				}
			}
			if(conf.isWebProxyEnabled()) {
				LOG.debug(CONNECTING_WITH_NODE
						+ conf.getWebAppProxyNode().getPublicIp());
				
				connection = SSHUtils.connectToNode(conf.getWebAppProxyNode()
						.getPublicIp(), conf.getUsername(), conf.getPassword(),
						conf.getPrivateKey());
				CustomTask stopWebAppProxy = new ExecCommand(componentHome + Hadoop2Deployer.RELPATH_SCRIPT_DIR
						+ Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_YARN
						+ " " + Constant.Hadoop.Command.STOP
						+ " " + Constant.RoleCommandName.getCommandName(Constant.Role.WEBAPPPROXYSERVER));
				res = connection.exec(stopWebAppProxy);
				if (res.rc != 0) {
					LOG.error("Could not stop Web Application Proxy Server.");
				}
				if(connection != null) {
					connection.disconnect();	
				}
			}
			
			connection = SSHUtils.connectToNode(conf.getResourceManagerNode()
					.getPublicIp(), conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());
			res = connection.exec(stopYarn);
			if (res.rc != 0) {
				LOG.error("Could not stop Yarn process");
			}
			if(connection != null) {
				connection.disconnect();	
			}
			
			LOG.debug(CONNECTING_WITH_NODE
					+ conf.getNamenode().getPublicIp());
			// connect to remote node
			connection = SSHUtils.connectToNode(conf.getNamenode()
					.getPublicIp(), conf.getUsername(), conf.getPassword(),
					conf.getPrivateKey());

			// if connected
			if (connection != null) {
				if(conf.isHaEnabled()) {
					if(conf.isAutomaticFailoverEnabled()) {
						this.stopZkfcProcesses(conf);	
					}
				}	
				res = connection.exec(stophdfs);
				if (res.rc != 0) {
					LOG.error("Could not stop HDFS processes");
				}
				return true;
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return true;
		} finally {
			// disconncet from node/machine
			if(connection != null) {
				connection.disconnect();	
			}
		}
		return true;
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
		Result res = null;
		SSHExec connection = null;

		// connect to nodes and execute commands
		try {
			Hadoop2Conf conf = (Hadoop2Conf) config;
			String componentHome = conf.getComponentHome();
			
			for (NodeConf nodeConf : nodeList) {
				LOG.setLoggerConfig(conf);
				HadoopNodeConf hadoopNodeConf = (HadoopNodeConf) nodeConf;

				List<String> serviceList = new ArrayList<String>();
				if(hadoopNodeConf.getDataNode()) {
					serviceList.add(Constant.Hadoop.Command.DAEMON_DATANODE);
					serviceList.add(Constant.Hadoop.Command.DAEMON_NODEMANAGER);	
				}
				if(hadoopNodeConf
						.getSecondaryNameNode()) {
					serviceList.add(Constant.Hadoop.Command.DAEMON_SECONDARY_NAMENODE);	
				}
				
				String nodeIp = nodeConf.getPublicIp();
				// connect to remote node
				connection = SSHUtils.connectToNode(nodeIp, conf.getUsername(),
						conf.getPassword(), conf.getPrivateKey());

				// if connected
				if (connection != null) {
					
					for(String serviceName : serviceList) {
						
						StringBuffer daemonFileRelPath = new StringBuffer(componentHome);
						
						daemonFileRelPath.append(Hadoop2Deployer.RELPATH_SCRIPT_DIR);
						daemonFileRelPath.append(JavaProcessScriptFile.getProcessDaemonFile(serviceName));

						String startCommand = daemonFileRelPath + " " 
						      + Constant.Hadoop.Command.START + " " 
						      + serviceName;		
						
						CustomTask startService = new ExecCommand(startCommand);
						res = connection.exec(startService);
						if (res.rc != 0) {
							LOG.error(nodeConf, "Could not start " + serviceName + " service");
							return false;
						}
					}
				} else {
					LOG.error(nodeConf, "Unable to establish connection ! ");
					return false;
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
		return status(new HashSet(nodeList));
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
		Result res = null;
		SSHExec connection = null;

		// connect to nodes and execute commands
		try {
			Hadoop2Conf conf = (Hadoop2Conf) config;
			String componentHome = conf.getComponentHome();
			
			for (NodeConf nodeConf : nodeList) {
				LOG.setLoggerConfig(conf);
				HadoopNodeConf hadoopNodeConf = (HadoopNodeConf) nodeConf;

				List<String> serviceList = new ArrayList<String>();
				if(hadoopNodeConf.getDataNode()) {
					serviceList.add(Constant.Hadoop.Command.DAEMON_DATANODE);
					serviceList.add(Constant.Hadoop.Command.DAEMON_NODEMANAGER);	
				}
				if(hadoopNodeConf.getSecondaryNameNode()) {
					serviceList.add(Constant.Hadoop.Command.DAEMON_SECONDARY_NAMENODE);	
				}
				
				String nodeIp = nodeConf.getPublicIp();
				// connect to remote node
				connection = SSHUtils.connectToNode(nodeIp, conf.getUsername(),
						conf.getPassword(), conf.getPrivateKey());

				// if connected
				if (connection != null) {
					
					for(String serviceName : serviceList) {
						
						StringBuffer daemonFileRelPath = new StringBuffer(componentHome);
						
						daemonFileRelPath.append(Hadoop2Deployer.RELPATH_SCRIPT_DIR);
						daemonFileRelPath.append(JavaProcessScriptFile.getProcessDaemonFile(serviceName));

						String startCommand = daemonFileRelPath + " " 
						      + Constant.Hadoop.Command.STOP + " " 
						      + serviceName;		
						
						CustomTask startService = new ExecCommand(startCommand);
						res = connection.exec(startService);
						if (res.rc != 0) {
							LOG.error(nodeConf, "Could not stop " + serviceName + " service");
							return false;
						}
					}
				} else {
					LOG.error(nodeConf, "Unable to establish connection ! ");
					return false;
				}
			}
		} catch (Exception e) {
			LOG.error("Error in stopping hadoop services : " + e.getMessage());
			return false;
		} finally {
			// Disconnect from node/machine
			if (connection != null) {
				connection.disconnect();
			}
		}
		return status(new HashSet(nodeList));
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
		final Hadoop2Conf conf = (Hadoop2Conf) config;

		final String infoMsg = "Adding Nodes..";
		final String node = conf.getNamenode().getPublicIp();
		LOG.setLoggerConfig(conf);
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
		
		LOG.info("Setting Passwordless SSH from NameNode across newly added nodes.");
		boolean passwordlessStatus = true;
		if(conf.isHaEnabled()) {
			LOG.info("Setting Passwordless SSH from StandBy Namenode across all slave nodes...");
			for(String nameNodeIp : conf.getHaNameNodesPublicIp()) {
				for(NodeConf tmpNodeConf : conf.getCompNodes()) {
					if(tmpNodeConf.getPublicIp().equals(nameNodeIp)) {
						passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, tmpNodeConf, new HashSet<NodeConf>(nodeList));
						if (!passwordlessStatus) {
							LOG.error("Could not set Passwordless SSH from " + nameNodeIp + " node...");
							return false;
						}
					}
				}
			}
			
		} else {
			passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, conf.getNamenode(), new HashSet<NodeConf>(nodeList));
			if (!passwordlessStatus) {
				LOG.error("Could not set Passwordless SSH from " + conf.getNamenode().getPublicIp() + " node...");
				return false;
			}
		}
		
		if(!conf.getResourceManagerNode().equals(conf.getNamenode())) {
			if(conf.isHaEnabled()) {
				if(!conf.getHaNameNodesPublicIp().contains(conf.getResourceManagerNode().getPublicIp())) {
					LOG.info("Setting Passwordless SSH from Resource Manager across newly added nodes.");
					passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, conf.getResourceManagerNode(), 
							new HashSet<NodeConf>(nodeList));
					if (!passwordlessStatus) {
						LOG.error("Could not set Passwordless SSH from " + conf.getResourceManagerNode().getPublicIp() + " node...");
						return false;
					}
				}
			}
			else {
				// PasswordLess SSH from ResourceManager to all nodes
				LOG.info("Setting Passwordless SSH from Resource Manager across newly added nodes.");
				passwordlessStatus = HadoopUtils.setupPasswordlessSSH(conf, conf.getResourceManagerNode(), 
						new HashSet<NodeConf>(nodeList));
				if (!passwordlessStatus) {
					LOG.error("Could not set Passwordless SSH from " + conf.getResourceManagerNode().getPublicIp() + " node...");
					return false;
				}
			}			
		}
		
		if (!passwordlessStatus) {
			LOG.error("Could not set Passwordless SSH...");
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
			
			status = HadoopUtils.updateConfToNodes(conf, Constant.SyncConfigurationAction.ADD);
			if(!status) {
				LOG.error("Error in adding nodes..");
				return false;
			}
			HadoopUtils.updateConfObjectAfterAddRemoveNodes(conf);
			status = status(new HashSet<NodeConf>(nodeList));
		} catch (Exception e) {
			LOG.error("Error in adding nodes..", e);
			status = false;
		}
		
		LOG.log(node, infoMsg, status);
		return status;
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
				AnkushTask mkInstallationPath = new MakeDirectory(
						conf.getInstallationPath());
				res = connection.exec(mkInstallationPath);

				if (!res.isSuccess) {
					LOG.error(nodeConf,
							"Could not create installation directory");
					return false;
				}

				status = createNode(nodeConf, (Hadoop2Conf)conf, true);
				
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
	 * com.impetus.ankush.common.framework.Deployable#removeNodes(java.util.
	 * List, com.impetus.ankush.common.framework.config.Configuration)
	 */
	@Override
	public boolean removeNodes(List<NodeConf> nodeList, Configuration config) {
		final Hadoop2Conf hadoopConf = (Hadoop2Conf) config;
		LOG.setLoggerConfig(hadoopConf);
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
			status = status(new HashSet<NodeConf>(nodeList));
		} catch (Exception e) {
			LOG.error("Error in removing nodes..", e);
			status = false;
		}
		return status;
	}

	/**
	 * Method status.
	 * 
	 * @param keySet
	 *            List<NodeConf>
	 * @return boolean
	 */
	private boolean status(Set<NodeConf> keySet) {
		boolean status = true;
		for (NodeConf nodeConf : keySet) {
			status = nodeConf.getStatus() && status;
		}
		return status;
	}

}
