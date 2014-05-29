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
package com.impetus.ankush.hadoop.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.config.Configuration;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.zookeeper.ZookeeperConf;
import com.impetus.ankush.hadoop.HadoopRackAwareness;
import com.impetus.ankush.hadoop.ecosystem.flume.FlumeConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Deployer;
import com.impetus.ankush.hadoop.ecosystem.hbase.ApacheHBaseDeployer;
import com.impetus.ankush.hadoop.ecosystem.hbase.HBaseConf;
import com.impetus.ankush.hadoop.ecosystem.hive.HiveConf;
import com.impetus.ankush.hadoop.ecosystem.mahout.MahoutConf;
import com.impetus.ankush.hadoop.ecosystem.oozie.OozieConf;
import com.impetus.ankush.hadoop.ecosystem.pig.PigConf;
import com.impetus.ankush.hadoop.ecosystem.solr.SolrConf;
import com.impetus.ankush.hadoop.ecosystem.sqoop.SqoopConf;

/**
 * This class is to prepare the Hadoop EcoSystem component configuration.
 * 
 * @author hokam chauhan.
 */
public class HadoopEcoSystemConfigurator {

	/**
	 * Ankush logger.
	 */
	private AnkushLogger LOG = new AnkushLogger(HadoopEcoSystemConfigurator.class);

	/**
	 * The Enum Component.
	 */
	private enum Component {
		
		/** The Hadoop2. */
		Hadoop2,
		/** The Hadoop. */
		Hadoop, 
		/** The Hive. */
		Hive, 
		/** The Hbase. */
		Hbase, 
		/** The Oozie. */
		Oozie, 
		/** The Zookeeper. */
		Zookeeper, 
		/** The Flume. */
		Flume, 
		/** The Mahout. */
		Mahout, 
		/** The Solr. */
		Solr, 
		/** The Pig. */
		Pig, 
		/** The Sqoop. */
		Sqoop, 
		/** The Invalid. */
		Invalid;
		
		/**
		 * From string.
		 *
		 * @param str the str
		 * @return the component
		 */
		public static Component fromString(String str) {
			try {
				return valueOf(str);
			} catch (Exception ex) {
				return Invalid;
			}
		}
	};

	/**
	 * Gets the hadoop conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the hadoop conf
	 */
	private HadoopConf getHadoopConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		
		HadoopConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), HadoopConf.class);
		conf.setClusterConf(clusterConf);
		Map<String, Object> advancedConf = conf.getAdvancedConf();
		conf.setNamenode(getNameNode(clusterConf));
		conf.setSlaves(getDataNodes(clusterConf));
		conf.setSecondaryNamenode(getSecondaryNameNodes(clusterConf));
		conf.setMapRedTmpDir((String) advancedConf.get("mapRedTmpDir"));
		conf.setHadoopTmpDir((String) advancedConf.get("hadoopTmpDir"));
		conf.setDfsDataDir((String) advancedConf.get("dataDir"));
		conf.setDfsNameDir((String) advancedConf.get("nameNodePath"));
		conf.setDfsReplicationFactor(ParserUtil.getIntValue((String) advancedConf.get("replicationFactorDFS"), 3));
		conf.setIncludes3((Boolean) advancedConf.get("includes3"));
		conf.setIncludes3n((Boolean) advancedConf.get("includes3n"));
		conf.setS3AccessKey((String) advancedConf.get("s3AccessKey"));
		conf.setS3SecretKey((String) advancedConf.get("s3SecretKey"));
		conf.setS3nAccessKey((String) advancedConf.get("s3nAccessKey"));
		conf.setS3nSecretKey((String) advancedConf.get("s3nSecretKey"));
		
		HashMap<String, String> javaHomes = new HashMap<String, String>();
		Set<NodeConf> objNodeConfs = new HashSet<NodeConf>();
		for (NodeConf hadoopNodeConf : clusterConf.getNodes()) {
			HadoopNodeConf tmpNodeConf = (HadoopNodeConf) hadoopNodeConf;
			objNodeConfs.add(tmpNodeConf);
			javaHomes.put(hadoopNodeConf.getPublicIp(), clusterConf
					.getJavaConf().getJavaHomePath());
		}
		conf.setJavaHomes(javaHomes);
		conf.setExpectedNodesAfterAddOrRemove(objNodeConfs);
		conf.setRackEnabled(clusterConf.isRackEnabled());
		conf.setRackFileName(clusterConf.getRackFileName());
		conf.setHadoopConfDir(conf.getComponentHome() + Hadoop1Deployer.RELPATH_COMPONENT_FOLDER_CONF);
		conf.setPropNameTopologyScriptFile(AppStoreWrapper.getAnkushConfReader().getStringValue("hadoop.conf.propname.topology.script"));
		
		String rackFileContent = "";
		if(clusterConf.isRackEnabled()) {
			rackFileContent = HadoopRackAwareness.createRackFileContents(new HashSet<NodeConf>(clusterConf.getNodes()));
			conf.setRackFileContent(rackFileContent);
		}
		clusterConf.getComponents().put(Constant.Component.Name.HADOOP, conf);
		return conf;
	}

	/**
	 * Gets the hadoop2 conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the hadoop2 conf
	 */
	private Hadoop2Conf getHadoop2Conf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		Hadoop2Conf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), Hadoop2Conf.class);

		conf.setClusterConf(clusterConf);
		Map<String, Object> advancedConf = conf.getAdvancedConf();
		conf.setNamenode(getNameNode(clusterConf));
		conf.setSlaves(getDataNodes(clusterConf));
		conf.setSecondaryNamenode(getSecondaryNameNodes(clusterConf));

		conf.setHadoopTmpDir((String) advancedConf.get("hadoopTmpDir"));
		conf.setMapRedTmpDir((String) advancedConf.get("mapRedTmpDir"));
		conf.setDfsDataDir((String) advancedConf.get("dataDir"));
		conf.setDfsNameDir((String) advancedConf.get("nameNodePath"));
		conf.setDfsReplicationFactor(ParserUtil.getIntValue((String) advancedConf.get("replicationFactorDFS"), 3));
		conf.setIncludes3((Boolean) advancedConf.get("includes3"));
		conf.setIncludes3n((Boolean) advancedConf.get("includes3n"));
		conf.setS3AccessKey((String) advancedConf.get("s3AccessKey"));
		conf.setS3SecretKey((String) advancedConf.get("s3SecretKey"));
		conf.setS3nAccessKey((String) advancedConf.get("s3nAccessKey"));
		conf.setS3nSecretKey((String) advancedConf.get("s3nSecretKey"));
		
		HashMap<String, String> javaHomes = new HashMap<String, String>();
		Set<NodeConf> objNodeConfs = new HashSet<NodeConf>();
		for (NodeConf hadoopNodeConf : clusterConf.getNodes()) {
			HadoopNodeConf tmpNodeConf = (HadoopNodeConf) hadoopNodeConf;
			objNodeConfs.add(tmpNodeConf);
			javaHomes.put(hadoopNodeConf.getPublicIp(), clusterConf
					.getJavaConf().getJavaHomePath());
		}
		conf.setJavaHomes(javaHomes);
		conf.setExpectedNodesAfterAddOrRemove(objNodeConfs);
		conf.setRackEnabled(clusterConf.isRackEnabled());
		conf.setRackFileName(clusterConf.getRackFileName());
		conf.setPropNameTopologyScriptFile(AppStoreWrapper.getAnkushConfReader().getStringValue("hadoop.conf.propname.topology.script"));
		
		String rackFileContent = "";
		if(clusterConf.isRackEnabled()) {
			rackFileContent = HadoopRackAwareness.createRackFileContents(new HashSet<NodeConf>(clusterConf.getNodes()));
			conf.setRackFileContent(rackFileContent);
		}
		conf.setHadoopConfDir(conf.getComponentHome() + Hadoop2Deployer.RELPATH_CONF_DIR);
		conf.setResourceManagerNode(conf.getNamenode());
		conf.setPropNameTopologyScriptFile(AppStoreWrapper.getAnkushConfReader().getStringValue("hadoop2.conf.propname.topology.script"));

		conf.setResourceManagerNode(this.getNodeConfFromAdvanceConf("resourceManagerNode", advancedConf, clusterConf.getNodes()));
		
		conf.setStartJobHistoryServer((Boolean) advancedConf.get("startJobHistoryServer"));
		if(conf.isStartJobHistoryServer()) {
			conf.setJobHistoryServerNode(this.getNodeConfFromAdvanceConf("jobHistoryServer", advancedConf, clusterConf.getNodes()));
			
			NodeConf hNode = conf.getJobHistoryServerNode();
			String nodeType = hNode.getType();
			if(nodeType.isEmpty()) {
				hNode.setType(Constant.Role.JOBHISTORYSERVER);
			} else {
				hNode.setType(nodeType + "/" + Constant.Role.JOBHISTORYSERVER);
			}
		}
		
		conf.setWebProxyEnabled((Boolean) advancedConf.get("isWebProxyEnabled"));
		if(conf.isWebProxyEnabled()) {
			conf.setWebAppProxyNode(this.getNodeConfFromAdvanceConf("webAppProxyNode", advancedConf, clusterConf.getNodes()));
			conf.setWebAppProxyPort((String) advancedConf.get("webAppProxyPort"));
			NodeConf hNode = conf.getWebAppProxyNode();
			String nodeType = hNode.getType();
			if(nodeType.isEmpty()) {
				hNode.setType(Constant.Role.WEBAPPPROXYSERVER);
			} else {
				hNode.setType(nodeType + "/" + Constant.Role.WEBAPPPROXYSERVER);
			}
		}
		
		// Field Mapping for Hadoop HA
		conf.setHaEnabled((Boolean) advancedConf.get("isHAEnabled"));
		if(conf.isHaEnabled()) {
			this.mapHAFieldsToConf(clusterConf, conf, advancedConf);
		}
		else {
			this.setHAEmptyValues(conf);
		}
		
//		conf.setMapredFramework((String) advancedConf.get("mapredFramework"));
//		conf.setSchedulerClass((String) advancedConf.get("schedulerClass"));

		clusterConf.getComponents().put(Constant.Component.Name.HADOOP2, conf);
		return conf;
	}
	
	private void setHAEmptyValues(Hadoop2Conf conf) {
		conf.setNameserviceId("");
		conf.setActiveNamenode(new NodeConf());
		conf.setStandByNamenode(new NodeConf());
		conf.setNameNodeId1("");
		conf.setNameNodeId2("");
		conf.setJournalNodes(null);
		conf.setJournalNodeEditsDir("");
		conf.setAutomaticFailoverEnabled(false);
		conf.setZkQuorumNodes(new ArrayList<NodeConf>());
		conf.setZkClientPort("");
		conf.setHaZkQourumValue("");
	}
	
	private void mapHAFieldsToConf(HadoopClusterConf clusterConf, Hadoop2Conf conf, Map<String, Object> advancedConf) {
		conf.setNameserviceId((String) advancedConf.get("nameserviceId"));
		conf.setActiveNamenode(this.getNodeConfFromAdvanceConf("activeNamenode", advancedConf, clusterConf.getNodes()));
		conf.setStandByNamenode(this.getNodeConfFromAdvanceConf("standByNamenode", advancedConf, clusterConf.getNodes()));
		
		conf.getHaNameNodesPublicIp().clear();
		conf.getHaNameNodesPublicIp().add(conf.getActiveNamenode().getPublicIp());
		conf.getHaNameNodesPublicIp().add(conf.getStandByNamenode().getPublicIp());
		
		conf.setNameNodeId1((String) advancedConf.get("nameNodeId1"));
		conf.setNameNodeId2((String) advancedConf.get("nameNodeId2"));
		conf.setJournalNodes(this.getNodeConfListFromAdvanceConf("journalNodes", advancedConf, clusterConf.getNodes()));
		conf.setJournalNodeEditsDir((String) advancedConf.get("journalNodeEditsDir"));
		conf.setAutomaticFailoverEnabled((Boolean) advancedConf.get("automaticFailoverEnabled"));
		
		if(conf.isAutomaticFailoverEnabled()) {
			conf.setZkQuorumNodes(this.getNodeConfListFromAdvanceConf("zkNodes", advancedConf, clusterConf.getNodes()));
			conf.setZkClientPort((String) advancedConf.get("zkClientPort"));
			
			StringBuilder haZkQourumValue = new StringBuilder();
			for(NodeConf node : conf.getZkQuorumNodes()) {
				haZkQourumValue.append(node.getPrivateIp()).append(":").append(conf.getZkClientPort()).append(","); 
			}
			conf.setHaZkQourumValue(haZkQourumValue.substring(0, haZkQourumValue.length()-1));
		}
		else {
			conf.setZkQuorumNodes(new ArrayList<NodeConf>());
			conf.setZkClientPort("");
			conf.setHaZkQourumValue("");
		}
	}
	
	private List<NodeConf> getNodeConfListFromAdvanceConf(String nodeType, Map<String, Object> advancedConf, List<HadoopNodeConf> clusterNodes) {
		if(advancedConf.get(nodeType) == null) {
			return null;	
		}
		List<NodeConf> nodeList = new ArrayList<NodeConf>();
		Map<String, String> ipMap = (Map<String, String>) advancedConf.remove(nodeType);
		for (String publicIp : ipMap.keySet()) {
			for (HadoopNodeConf hNode : clusterNodes) {
				if (publicIp.equals(hNode.getPublicIp())) {
					nodeList.add(hNode);
					break;
				}
			}
		}
		return nodeList;
	}
	
	private NodeConf getNodeConfFromAdvanceConf(String nodeType, Map<String, Object> advancedConf, List<HadoopNodeConf> clusterNodes) {
		if(advancedConf.get(nodeType) == null) {
			return null;	
		}
		String nodeIp = (String) advancedConf.get(nodeType);
		for (HadoopNodeConf hNode : clusterNodes) {
			if (nodeIp.equals(hNode.getPublicIp())) {
				return hNode;
			}
		}
		return null;
	}

	/**
	 * Gets the hbase conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the hbase conf
	 */
	private HBaseConf getHbaseConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		  try {
			  HBaseConf conf = JsonMapperUtil.objectFromString(JsonMapperUtil.jsonFromObject(genericConf), HBaseConf.class);
				conf.setClusterConf(clusterConf);
				
				String hdfsUri = "";
				String hadoopComponentName = Constant.Component.Name.HADOOP;
				if(clusterConf.getComponents().containsKey(Constant.Component.Name.HADOOP)) {
					HadoopConf hConf = (HadoopConf) clusterConf.getComponents().get(Constant.Component.Name.HADOOP);
					hdfsUri = hConf.getHdfsUri();
				} else if(clusterConf.getComponents().containsKey(Constant.Component.Name.HADOOP2)) {
					hadoopComponentName = Constant.Component.Name.HADOOP2;
					Hadoop2Conf hConf = (Hadoop2Conf) clusterConf.getComponents().get(hadoopComponentName);
					hdfsUri = hConf.getHdfsUri();
				}
				
				String hbaseRootDir = hdfsUri + ApacheHBaseDeployer.REL_HDFS_PATH_FOR_HBASE;
				
				GenericConfiguration hadoopConf = clusterConf.getComponents().get(hadoopComponentName);
				Map<String, Object> advancedConf = conf.getAdvancedConf();
				Set<NodeConf> regionNodes = new HashSet<NodeConf>();
				Map<String, String> ipMap = (Map<String, String>) advancedConf.remove("regionServers");
				
				for (String publicIp : ipMap.keySet()) {
					for (HadoopNodeConf hNode : clusterConf.getNodes()) {
						if (publicIp.equals(hNode.getPublicIp())) {
							// Add Node type for HBase Region Servers 
							String nodeType = hNode.getType();
							if(nodeType.isEmpty()) {
								hNode.setType(Constant.Role.HBASEREGIONSERVER);
							} else {
								hNode.setType(nodeType + "/" + Constant.Role.HBASEREGIONSERVER);
							}
							regionNodes.add(hNode);
							break;
						}
					}
				}

				NodeConf hbaseMaster = null;
				if (clusterConf.isLocal()) {
					// For local environment the server ip will be fetched from the
					// advanced conf map.
					String hbaseMasterIp = (String) advancedConf.remove("hbaseMaster");
					for (HadoopNodeConf hNode : clusterConf.getNodes()) {
						if (hbaseMasterIp.equals(hNode.getPublicIp())) {
							hbaseMaster = hNode;
							break;
						}
					}
					conf.setHbaseMasterNode(hbaseMaster);
				} else {
					/* For cloud case Taking the cluster namenode as HBase Master Node. */
					hbaseMaster = ((HadoopConf) hadoopConf).getNamenode();
				}
				// Add Node type for HBase Region Servers 
				String nodeType = hbaseMaster.getType();
				if(nodeType.isEmpty()) {
					hbaseMaster.setType(Constant.Role.HBASEMASTER);
				} else {
					hbaseMaster.setType(nodeType + "/" + Constant.Role.HBASEMASTER);
				}
				
				conf.setUniversalClusterJavaHome(((HadoopConf)hadoopConf).getJavaHome(hbaseMaster.getPublicIp()));
				conf.setHbaseMasterNode(hbaseMaster);
				conf.setHbaseRegionServerNodes(regionNodes);
				conf.setHadoopHome(((HadoopConf) hadoopConf).getComponentHome());
				conf.setHadoopConf(((HadoopConf) hadoopConf).getHadoopConfDir());
				conf.setHadoopVersion(((HadoopConf) hadoopConf).getComponentVersion());
				
				conf.setHdfsPathForHbase(hbaseRootDir);		
				conf.setZookeeperPort(ZookeeperConf.defaultZkClientPort);
				
				String zkQuoromPropertyValue = new String();
				boolean hbaseManagesZK = false;
				
				GenericConfiguration zookeeperConf = clusterConf.getComponents().get(
						Constant.Component.Name.ZOOKEEPER);
				
				if (zookeeperConf != null) {
					for (NodeConf nodeConf : zookeeperConf.getCompNodes()) {
						if (zkQuoromPropertyValue.length() != 0) {
							zkQuoromPropertyValue += ",";
						}
						zkQuoromPropertyValue += nodeConf.getPrivateIp();
					}
					conf.setZookeeperPort(((ZookeeperConf) zookeeperConf).getClientPort() + "");
					
				} else {
					zkQuoromPropertyValue = conf.getHbaseMasterNode().getPrivateIp();
					hbaseManagesZK = true;
				}
				
				conf.setHbaseManagesZK(hbaseManagesZK);
				conf.setZkQuoromPropertyValue(zkQuoromPropertyValue);
				clusterConf.getComponents().put(Constant.Component.Name.HBASE, conf);
				return conf;
		  } catch (Exception e) {
			  LOG.error("Exception: Unable to map HBase json for deployment.");
			  return null;
		}
	}

	/**
	 * Gets the zookeeper conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the zookeeper conf
	 */
	private ZookeeperConf getZookeeperConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		ZookeeperConf conf = JsonMapperUtil
				.objectFromString(JsonMapperUtil.jsonFromObject(genericConf),
						ZookeeperConf.class);
		conf.setClusterConf(clusterConf);
		Map<String, Object> advancedConf = conf.getAdvancedConf();

		ArrayList<NodeConf> nodeConfs = new ArrayList<NodeConf>();
		Map<String, String> ipMap = (Map<String, String>) advancedConf
				.remove("nodes");

		for (String publicIp : ipMap.keySet()) {
			for (HadoopNodeConf hNode : clusterConf.getNodes()) {
				if (publicIp.equals(hNode.getPublicIp())) {
					nodeConfs.add(hNode);
					String nodeType = hNode.getType();
					if(nodeType.isEmpty()) {
						hNode.setType(Constant.Role.ZOOKEEPER);
					} else {
						hNode.setType(nodeType + "/" + Constant.Role.ZOOKEEPER);
					}
					break;
				}
			}
		}

		conf.setNodes(nodeConfs);
		conf.setDataDirectory((String) advancedConf.get("dataDir"));
		conf.setClientPort(ParserUtil.getIntValue(
				(String) advancedConf.get("clientPort"), 2182));

		conf.setInitLimit(ParserUtil.getIntValue(
				(String) advancedConf.get("initLimit"), 5));
		conf.setSyncLimit(ParserUtil.getIntValue(
				(String) advancedConf.get("syncLimit"), 2));
		conf.setTickTime(ParserUtil.getIntValue(
				(String) advancedConf.get("tickTime"), 2000));
		clusterConf.getComponents()
				.put(Constant.Component.Name.ZOOKEEPER, conf);
		return conf;
	}

	/**
	 * Gets the hive conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the hive conf
	 */
	private HiveConf getHiveConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		HiveConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), HiveConf.class);
		Map<String, Object> advancedConf = conf.getAdvancedConf();

		String hadoopComponentName = Constant.Component.Name.HADOOP;
		if(clusterConf.getComponents().get(Constant.Component.Name.HADOOP) == null) {
			hadoopComponentName = Constant.Component.Name.HADOOP2;
		}
		
		GenericConfiguration hadoopConf = clusterConf.getComponents().get(
				hadoopComponentName);
		conf.setNode(((HadoopConf) hadoopConf).getNamenode());
		conf.setClusterConf(clusterConf);

		conf.setHadoopHome(((HadoopConf) hadoopConf).getComponentHome());
		conf.setHadoopConf(((HadoopConf) hadoopConf).getHadoopConfDir());
		String hadoopVersion = ((HadoopConf) hadoopConf).getComponentVersion();
		conf.setHadoopVersion(hadoopVersion);
		if (clusterConf.isLocal()) {
			/*
			 * For local environment the server ip will be fetched from the
			 * advanced conf map.
			 */
			String hiveServerIP = (String) advancedConf.remove("hiveServer");

			for (HadoopNodeConf hNode : clusterConf.getNodes()) {
				if (hiveServerIP.equals(hNode.getPublicIp())) {
					conf.setNode(hNode);
				}
			}

		}

		clusterConf.getComponents().put(Constant.Component.Name.HIVE, conf);
		return conf;
	}

	/**
	 * Gets the mahout conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the mahout conf
	 */
	private MahoutConf getMahoutConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		MahoutConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), MahoutConf.class);
		String hadoopComponentName = Constant.Component.Name.HADOOP;
		
		if(clusterConf.getComponents().get(Constant.Component.Name.HADOOP) == null) {
			hadoopComponentName = Constant.Component.Name.HADOOP2;
		}
		
		GenericConfiguration hadoopConf = clusterConf.getComponents().get(
				hadoopComponentName);
		conf.setMasterNode(((HadoopConf) hadoopConf).getNamenode());
		conf.setClusterConf(clusterConf);
		conf.setHadoopHome(((HadoopConf) hadoopConf).getComponentHome());
		conf.setHadoopConf(((HadoopConf) hadoopConf).getHadoopConfDir());
		String hadoopVersion = ((HadoopConf) hadoopConf).getComponentVersion();
		conf.setHadoopVersion(hadoopVersion);
		clusterConf.getComponents().put(Constant.Component.Name.MAHOUT, conf);
		return conf;
	}

	/**
	 * Gets the sqoop conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the sqoop conf
	 */
	private SqoopConf getSqoopConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		SqoopConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), SqoopConf.class);
		String hadoopComponentName = Constant.Component.Name.HADOOP;
		if(clusterConf.getComponents().get(Constant.Component.Name.HADOOP) == null) {
			hadoopComponentName = Constant.Component.Name.HADOOP2;
		}
		
		GenericConfiguration hadoopConf = clusterConf.getComponents().get(
				hadoopComponentName);
		conf.setMasterNode(((HadoopConf) hadoopConf).getNamenode());
		conf.setClusterConf(clusterConf);
		conf.setHadoopHome(((HadoopConf) hadoopConf).getComponentHome());
		conf.setHadoopConf(((HadoopConf) hadoopConf).getHadoopConfDir());
		String hadoopVersion = ((HadoopConf) hadoopConf).getComponentVersion();
		conf.setHadoopVersion(hadoopVersion);
		clusterConf.getComponents().put(Constant.Component.Name.SQOOP, conf);
		return conf;
	}

	/**
	 * Gets the pig conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the pig conf
	 */
	private PigConf getPigConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		PigConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), PigConf.class);
		String hadoopComponentName = Constant.Component.Name.HADOOP;
		if(clusterConf.getComponents().get(Constant.Component.Name.HADOOP) == null) {
			hadoopComponentName = Constant.Component.Name.HADOOP2;
		}
		
		GenericConfiguration hadoopConf = clusterConf.getComponents().get(
				hadoopComponentName);
		conf.setNode(((HadoopConf) hadoopConf).getNamenode());
		conf.setClusterConf(clusterConf);
		conf.setHadoopHome(((HadoopConf) hadoopConf).getComponentHome());
		conf.setHadoopConf(((HadoopConf) hadoopConf).getHadoopConfDir());
		String hadoopVersion = ((HadoopConf) hadoopConf).getComponentVersion();
		conf.setHadoopVersion(hadoopVersion);
		clusterConf.getComponents().put(Constant.Component.Name.PIG, conf);
		return conf;
	}

	/**
	 * Gets the flume conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the flume conf
	 */
	private FlumeConf getFlumeConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		FlumeConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), FlumeConf.class);
		String hadoopComponentName = Constant.Component.Name.HADOOP;
		if(clusterConf.getComponents().get(Constant.Component.Name.HADOOP) == null) {
			hadoopComponentName = Constant.Component.Name.HADOOP2;
		}
		
		GenericConfiguration hadoopConf = clusterConf.getComponents().get(
				hadoopComponentName);
		conf.setNode(((HadoopConf) hadoopConf).getNamenode());
		conf.setClusterConf(clusterConf);
		clusterConf.getComponents().put(Constant.Component.Name.FLUME, conf);
		return conf;
	}

	/**
	 * Gets the solr conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the solr conf
	 */
	private SolrConf getSolrConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {
		SolrConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), SolrConf.class);

		String hadoopComponentName = Constant.Component.Name.HADOOP;
		if(clusterConf.getComponents().get(Constant.Component.Name.HADOOP) == null) {
			hadoopComponentName = Constant.Component.Name.HADOOP2;
		}
		
		GenericConfiguration hadoopConf = clusterConf.getComponents().get(
				hadoopComponentName);
		
		// Add Node type for Solr Service to Hadoop Namenode 
		NodeConf namenode = ((HadoopConf) hadoopConf).getNamenode();
		String nodeType = namenode.getType();
		if(nodeType.isEmpty()) {
			namenode.setType(Constant.Role.SOLRSERVICE);
		} else {
			namenode.setType(nodeType + "/" + Constant.Role.SOLRSERVICE);
		}
		conf.setNode(namenode);
				
		conf.setClusterConf(clusterConf);
		clusterConf.getComponents().put(Constant.Component.Name.SOLR, conf);
		return conf;
	}

	/**
	 * Gets the oozie conf.
	 *
	 * @param clusterConf the cluster conf
	 * @param genericConf the generic conf
	 * @return the oozie conf
	 */
	private OozieConf getOozieConf(HadoopClusterConf clusterConf,
			Configuration genericConf) {

		OozieConf conf = JsonMapperUtil.objectFromString(
				JsonMapperUtil.jsonFromObject(genericConf), OozieConf.class);

		conf.setClusterConf(clusterConf);
		String hadoopComponentName = Constant.Component.Name.HADOOP;
		if(clusterConf.getComponents().get(Constant.Component.Name.HADOOP) == null) {
			hadoopComponentName = Constant.Component.Name.HADOOP2;
		}
		
		GenericConfiguration hadoopConf = clusterConf.getComponents().get(
				hadoopComponentName);

		// Add Node type for Oozie Server to Hadoop Namenode 
		NodeConf namenode = ((HadoopConf) hadoopConf).getNamenode();
		String nodeType = namenode.getType();
		if(nodeType.isEmpty()) {
			namenode.setType(Constant.Role.OOZIESERVER);
		} else {
			namenode.setType(nodeType + "/" + Constant.Role.OOZIESERVER);
		}
		conf.setNode(namenode);
		
		
		
		conf.setHadoopHome(((HadoopConf) hadoopConf).getComponentHome());
		conf.setHadoopConf(((HadoopConf) hadoopConf).getHadoopConfDir());

		String version = ((HadoopConf) hadoopConf).getComponentVersion();

		if (version.startsWith("1.")) {
			version = "0.20.200";
		}
		if (version.startsWith("0.20.2")) {
			version = "0.20.2";
		}

		conf.setHadoopVersion(version);
		clusterConf.getComponents().put(Constant.Component.Name.OOZIE, conf);
		return conf;
	}

	/**
	 * Method to get Hadoop Name node from cluster Conf.
	 *
	 * @param clusterConf the cluster conf
	 * @return the name node
	 */
	public HadoopNodeConf getNameNode(HadoopClusterConf clusterConf) {
		for (HadoopNodeConf node : clusterConf.getNodes()) {
			if (node.getNameNode()) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Method to get list of Hadoop data nodes from cluster Conf.
	 *
	 * @param clusterConf the cluster conf
	 * @return the data nodes
	 */
	public Set<HadoopNodeConf> getDataNodes(HadoopClusterConf clusterConf) {
		Set<HadoopNodeConf> nodes = new HashSet<HadoopNodeConf>();
		for (HadoopNodeConf node : clusterConf.getNodes()) {
			if (node.getDataNode()) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	/**
	 * Method to get list of Hadoop Secondary Name nodes from cluster Conf.
	 *
	 * @param clusterConf the cluster conf
	 * @return the secondary name nodes
	 */
	public HadoopNodeConf getSecondaryNameNodes(
			HadoopClusterConf clusterConf) {

		for (HadoopNodeConf node : clusterConf.getNodes()) {
			if (node.getSecondaryNameNode()) {
				return node;
			}
		}
		return null;
	}

	/**
	 * Method to get EcoSystem Component Name, Component Configuration Map.
	 *
	 * @param clusterConf the cluster conf
	 * @return the eco system configuration map
	 */
	public Map<String, Configuration> getEcoSystemConfigurationMap(
			HadoopClusterConf clusterConf) {

		/* Created the Empty Hash Map to return */
		Map<String, Configuration> componentMap = new HashMap<String, Configuration>();

		/* Populating the HashMap with the Component configurations. */
		for (String componentName : clusterConf.getComponents().keySet()) {
			componentMap.put(componentName,
					getComponentConf(componentName, clusterConf));
		}
		LOG.debug("Component Map " + componentMap);
		return componentMap;
	}

	/**
	 * Gets the component conf.
	 *
	 * @param componentName the component name
	 * @param clusterConf the cluster conf
	 * @return the component conf
	 */
	private Configuration getComponentConf(String componentName,
			HadoopClusterConf clusterConf) {

		/* Getting the component configuration */
		Configuration genericConf = clusterConf.getComponents().get(
				componentName);
		LOG.debug("Component name " + componentName);
		/* Getting the component name integer enum value. */
		switch (Component.fromString(componentName)) {

		case Hadoop2:
			return getHadoop2Conf(clusterConf, genericConf);
		case Hadoop:
			return getHadoopConf(clusterConf, genericConf);
		case Hbase:
			return getHbaseConf(clusterConf, genericConf);
		case Zookeeper:
			return getZookeeperConf(clusterConf, genericConf);
		case Hive:
			return getHiveConf(clusterConf, genericConf);
		case Oozie:
			return getOozieConf(clusterConf, genericConf);
		case Mahout:
			return getMahoutConf(clusterConf, genericConf);
		case Pig:
			return getPigConf(clusterConf, genericConf);
		case Flume:
			return getFlumeConf(clusterConf, genericConf);
		case Solr:
			return getSolrConf(clusterConf, genericConf);
		case Sqoop:
			return getSqoopConf(clusterConf, genericConf);
		default:
			return null;
		}
	}
}
