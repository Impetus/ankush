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
package com.impetus.ankush.hadoop;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Semaphore;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.agent.AgentUtils;
import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.framework.AbstractMonitor;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;
import com.impetus.ankush.common.ganglia.Graph;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.AddConfProperty;
import com.impetus.ankush.common.scripting.impl.DeleteConfProperty;
import com.impetus.ankush.common.scripting.impl.EditConfProperty;
import com.impetus.ankush.common.scripting.impl.MakeDirectory;
import com.impetus.ankush.common.service.ConfigurationManager;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.tiles.TileManager;
import com.impetus.ankush.common.tiles.TileViewHandler;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.AnkushRestClient;
import com.impetus.ankush.common.utils.CommonUtil;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.LogViewHandler;
import com.impetus.ankush.common.utils.ParserUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.config.HadoopNodeConf;
import com.impetus.ankush.hadoop.config.Parameter;
import com.impetus.ankush.hadoop.dfs.DFSReport;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Application;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Deployer;
import com.impetus.ankush.hadoop.job.Hadoop2HistoryJob;
import com.impetus.ankush.hadoop.job.Hadoop2Job;
import com.impetus.ankush.hadoop.job.Hadoop2RunningJob;
import com.impetus.ankush.hadoop.job.HadoopJob;
import com.impetus.ankush.hadoop.service.ParameterConfigService;
import com.impetus.ankush.hadoop.service.impl.ParameterConfigServiceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopClusterMonitor.
 * 
 * @author Akhil
 */
public class HadoopClusterMonitor extends AbstractMonitor {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(HadoopClusterMonitor.class);
	
	/** The monitoring manager. */
	private GenericManager<NodeMonitoring, Long> monitoringManager = AppStoreWrapper
			.getManager(Constant.Manager.MONITORING, NodeMonitoring.class);

	/** The Constant EXTENSION_XML. */
	private static final String EXTENSION_XML = ".xml";
	
	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

	/** The parameter config service. */
	private ParameterConfigService parameterConfigService = AppStoreWrapper
			.getService(Constant.Service.PARAMETER_CONFIG_SERVICE,
					ParameterConfigService.class);

	/** The Constant AppMonitoringErrMsg. */
	private static final String AppMonitoringErrMsg = "Exception: Unable to get application data.. !"; 
	
	/**
	 * Nodeprogress.
	 * 
	 * @return the map
	 * @throws Exception
	 *             the exception
	 */
	private Map addingnodes() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		HadoopClusterConf hadoopClusterConf = ((HadoopClusterConf) dbCluster.getClusterConf());

		List<HadoopNodeConf> nodes = hadoopClusterConf.getNewNodes();
		if (nodes == null) {
			return map;
		}

		setNodes(nodes);
		map.put(Constant.Keys.IP_PATTERN, hadoopClusterConf.getIpPattern());
		map.put(Constant.Keys.PATTERN_FILE, hadoopClusterConf.getPatternFile());

		String eventState = hadoopClusterConf.getState();
		for (HadoopNodeConf node : nodes) {
			if (node.getNodeState().equals(Constant.Keys.ERROR)) {
				eventState = node.getNodeState();
				break;
			}
		}
		map.put(Constant.Keys.STATE, eventState);

		// in case of error and deployed cluster state removing new nodes from
		// the db.
		if (!eventState.equals(Constant.Cluster.State.ADDING_NODES)) {
			hadoopClusterConf.setNewNodes(null);
			dbCluster.setClusterConf(hadoopClusterConf);
			clusterManager.save(dbCluster);
		}
		result.putAll(map);
		return map;
	}

	/**
	 * Sets the add node log msg.
	 * 
	 * @param hNodes
	 *            the h nodes
	 * @throws Exception
	 *             the exception
	 */
	private void setNodes(List<HadoopNodeConf> hNodes) throws Exception {

		long operationId = logger.getNewOperationId(dbCluster.getId()) - 1;
		List<Log> logs = logManager.getAllByNativeQuery(Log
				.getNodeLastLogQuery(dbCluster.getId(), operationId));

		Map<String, String> hostMsgMap = new HashMap<String, String>();
		for (Log log : logs) {
			hostMsgMap.put(log.getHost(), log.getMessage());
		}

		List<Map> nodesMap = new ArrayList<Map>();
		for (HadoopNodeConf node : hNodes) {
			if (dbCluster.getState().equals(Constant.Cluster.State.DEPLOYED)
					&& node.getStatus()) {
				node.setMessage("Deployment Completed.");
			} else {
				node.setMessage(hostMsgMap.get(node.getPublicIp()));
			}
			LinkedHashMap nodeMap = JsonMapperUtil.objectFromObject(node,
					LinkedHashMap.class);
			nodeMap.remove("nameNode");
			nodeMap.remove("dataNode");
			nodeMap.remove("secondaryNameNode");
			nodesMap.add(nodeMap);
		}
		result.putAll(Collections.singletonMap(Constant.Keys.NODES, nodesMap));
		result.put(Constant.Keys.STATE, dbCluster.getState());
	}

	/**
	 * Instantiates a new hadoop cluster monitor.
	 */
	public HadoopClusterMonitor() {
		logger.removeAppender();
	}

	/**
	 * Details.
	 */
	private void details() {
		try {
			// cluster conf.
			ClusterConf clusterConf = dbCluster.getClusterConf();

			// setting errors.
			Map<String, String> errors = clusterConf.getErrors();
			for (NodeConf nodeConf : clusterConf.getNodeConfs()) {
				if (nodeConf.getErrors().size() > 0) {
					errors.put(
							nodeConf.getPublicIp(),
							"Deployment failed on node : "
									+ nodeConf.getPublicIp());
				}
			}
			clusterConf.setErrors(errors);
			// putting cluster setup details in map.
			result.putAll(JsonMapperUtil.mapFromObject(clusterConf));

			// set nodes
			nodelist();
		} catch (Exception e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Sets the nodes log msg.
	 *
	 * @throws Exception the exception
	 */
	private void nodelist() throws Exception {
		HadoopClusterConf clusterConf = (HadoopClusterConf) dbCluster.getClusterConf();
		setNodes(clusterConf.getNodes());
	}

	/**
	 * Tilecluster.
	 * 
	 * @return the map
	 */
	private void clustertiles() {
		Map<String, Object> map = new HashMap<String, Object>();
		TileViewHandler tileViewHandler = new HadoopTileViewHandler();

		// getting cluster detail tiles.
		List<TileInfo> tiles = tileViewHandler.getClusterDetailsTiles(
				dbCluster, parameterMap);

		// createing tile manager object.
		TileManager tileManager = new TileManager();

		// adding event tiles.
		tiles.addAll(tileManager.getGroupedClusterTiles(dbCluster));

		map.put(Constant.Keys.TILES, tiles);
		result.putAll(map);
	}

	/**
	 * Gets the hadoop conf object.
	 *
	 * @return the hadoop conf object
	 */
	private HadoopConf getHadoopConfObject() {
		// Making hadoop conf object.
		ClusterConf clusterConf = (ClusterConf) dbCluster.getClusterConf();
		// Making the hadoop conf object.
		HadoopConf hadoopConf = null;

		if (clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP) != null) {
			hadoopConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP);
		} else {
			hadoopConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
		}
		return hadoopConf;
	}
	
	/**
	 * Checks if is hadoop2 instance.
	 *
	 * @param hConf the h conf
	 * @return true, if is hadoop2 instance
	 */
	private boolean isHadoop2Instance(HadoopConf hConf) {
		return (hConf instanceof Hadoop2Conf);
	}
	
	/**
	 * Hdfs monitoring usage.
	 */
	private void hdfsMonitoringUsage() {
		String errMsgDfsData = "Could not get dfs data";
		try {
			HadoopConf hConf = this.getHadoopConfObject();
			String nameNodeIp = hConf.getNamenode().getPublicIp();
			String role = Constant.Role.NAMENODE;
			int clientPort = Integer.parseInt(Hadoop1Deployer.DEFAULT_PORT_HTTP_NAMENODE);
			boolean isHadoop2 = this.isHadoop2Instance(hConf);
			
			if(isHadoop2) {
				if(((Hadoop2Conf) hConf).isHaEnabled()) {
					if(((Hadoop2Conf) hConf).getActiveNamenode() != null) {
						nameNodeIp = ((Hadoop2Conf) hConf).getActiveNamenode().getPublicIp();
					}
				}
			}
			if(!HadoopUtils.getServiceStatusForNode(nameNodeIp, Constant.Role.AGENT)) {
				addAndLogError("Agent Down on " + nameNodeIp + ": " + errMsgDfsData);
				return;
			}
			if(!HadoopUtils.getServiceStatusForNode(nameNodeIp, role)) {
				addAndLogError(role + " Down on " + nameNodeIp + ": " + errMsgDfsData);
				return;
			}
			Map<String, String> mapData = new LinkedHashMap<String, String>();
			HadoopTileViewHandler tileViewHandler = new HadoopTileViewHandler();
			mapData = tileViewHandler.getHdfsClusterUsageData(nameNodeIp, clientPort, isHadoop2, hConf.isCdh3());
			if(mapData != null) {
				result.put(Constant.Hadoop.Keys.MONITORING_TABLE_DATA_KEY, mapData);
			} else {
				addAndLogError("DFS Monitoring: " + errMsgDfsData);
			}
		} catch (Exception e) {
			addAndLogError("DFS Monitoring: " + errMsgDfsData);
		}
	}
	
	/**
	 * Hadoop nodes summary.
	 */
	private void hadoopNodesSummary() {
		try 
		{
			HadoopConf hConf = this.getHadoopConfObject();
			boolean isHadoop2 = this.isHadoop2Instance(hConf);
			Map<String, String> mapData = HadoopTileViewHandler.getHadoopNodesSummary(hConf, isHadoop2);;
			result.put(Constant.Hadoop.Keys.MONITORING_TABLE_DATA_KEY, mapData);
		} catch (Exception e) {
			addAndLogError("Hadoop Monitoring: Could not get nodes summary");
		}
	}
	
	/**
	 * Mapred monitoring jobs.
	 */
	private void mapredMonitoringJobs() {
		String errMsgMapredData = "Could not get Job data";
		try {
			HadoopConf hConf = this.getHadoopConfObject();
			boolean isHadoop2 = this.isHadoop2Instance(hConf);
			String nodeIp = hConf.getNamenode().getPublicIp();
			String role = Constant.Role.JOBTRACKER;
			int clientPort = Integer.parseInt(Hadoop1Deployer.DEFAULT_PORT_HTTP_JOBTRACKER);
			
			if(isHadoop2) {
				nodeIp = ((Hadoop2Conf) hConf).getResourceManagerNode().getPublicIp();
				role = Constant.Role.RESOURCEMANAGER;
				errMsgMapredData = "Could not get Application data";
				clientPort = Integer.parseInt(Hadoop2Deployer.DEFAULT_PORT_HTTP_RESOURCEMANAGER);
			}
			
			if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.AGENT)) {
				addAndLogError("Agent Down on " + nodeIp + ": " + errMsgMapredData);
				return;
			}
			
			if(!HadoopUtils.getServiceStatusForNode(nodeIp, role)) {
				addAndLogError(role + " Down on " + nodeIp + ": " + errMsgMapredData);
				return;
			}
			
			Map<String, String> mapData = new LinkedHashMap<String, String>();
			HadoopTileViewHandler tileViewHandler = new HadoopTileViewHandler();
			mapData = tileViewHandler.getMapredJobsData(nodeIp, clientPort, isHadoop2);
			
			if(mapData != null) {
				result.put(Constant.Hadoop.Keys.MONITORING_TABLE_DATA_KEY, mapData);	
			} else {
				addAndLogError("Mapreduce Monitoring: " + errMsgMapredData);
			}
			
		} catch (Exception e) {
			addAndLogError("Mapreduce Monitoring: " + errMsgMapredData);
		}
	}
	
	/**
	 * Tilenode.
	 * 
	 * @return the map
	 */
	public void nodetiles() {
		Map<String, Object> map = new HashMap<String, Object>();
		TileViewHandler tileViewHandler = new HadoopTileViewHandler();
		List<TileInfo> tileInfo = tileViewHandler.getNodeDetailsTiles(dbCluster, parameterMap);
		map.put(Constant.Keys.TILES, tileInfo);
		result.putAll(map);
	}
	
	/**
	 * Clusterdetails.
	 * 
	 * @return the map
	 */
	private Map clusterdetails() {
		Map map = getClusterDetails(dbCluster);
		result.putAll(map);
		return map;
	}

	/**
	 * Ecosystem.
	 * 
	 * @return the map
	 */
	private Map ecosystem() {
		Map map = getEcosystemDetails(dbCluster);
		result.putAll(map);
		return map;
	}

	/**
	 * Gets the ecosystem details.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return the ecosystem details
	 */
	private Map getEcosystemDetails(Cluster cluster) {
		Map map = new HashMap();
		HadoopClusterConf hadoopClusterConf = ((HadoopClusterConf) cluster
				.getClusterConf());
		map.put(Constant.Keys.CLUSTER_NAME, cluster.getName());
		map.put(Constant.Keys.ECOSYSTEM, hadoopClusterConf.getComponents());
		return map;
	}

	/**
	 * Gets the cluster details.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return the cluster details
	 */
	private Map getClusterDetails(Cluster cluster) {
		Map map = new HashMap();
		try {
			ClusterConf clusterConf = cluster.getClusterConf();
			map.putAll((Map) JsonMapperUtil.mapFromObject(clusterConf));
			map.remove(Constant.Keys.NODES);
			map.remove(Constant.Keys.COMPONENTS);
			map.remove(Constant.Keys.LOGS);
			map.remove(Constant.Keys.PASSWORD);
			map.remove(Constant.Keys.USERNAME);
			map.remove(Constant.Keys.ERRORS);
			map.remove(Constant.Keys.PRIVATEKEY);
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}

	/**
	 * To get cluster nodes.
	 * 
	 * @return the map
	 */
	private Map nodes() {
		try {
			dbCluster.getNodes();
			HadoopClusterConf conf = (HadoopClusterConf) dbCluster
					.getClusterConf();

			List<Map> nodesMap = new ArrayList<Map>();
			for (HadoopNodeConf node : conf.getNodes()) {
				Map nodeMap = JsonMapperUtil.mapFromObject(node);
				// creating monitoring manager object
				MonitoringManager manager = new MonitoringManager();
				// getting node monitoring data
				NodeMonitoring nodeMonitoring = manager.getMonitoringData(node
						.getId());

				// putting services status in map.
				boolean serviceStatus = true;
				if (nodeMonitoring != null
						&& nodeMonitoring.getServiceStatus() != null) {
					nodeMap.put(Constant.Keys.SERVICES,
							nodeMonitoring.getServiceStatus());
					for (Boolean status : nodeMonitoring.getServiceStatus()
							.values()) {
						serviceStatus = serviceStatus && status;
					}
					nodeMap.put(Constant.Keys.SERVICESTATUS, serviceStatus);
				} else {
					nodeMap.put(Constant.Keys.SERVICES, null);
					nodeMap.put(Constant.Keys.SERVICEERROR,
							"Unable to get services status");
					nodeMap.put(Constant.Keys.SERVICESTATUS, false);
				}
				nodeMap.putAll(getNodeUsageMap(nodeMonitoring));
				nodeMap.put(Constant.Keys.ID, node.getId());
				nodeMap.remove(Constant.Keys.MESSAGE);
				nodeMap.remove(Constant.Keys.CLOUDNODEMETADATAINFO);
				nodeMap.remove(Constant.Keys.ERRORS);
				nodesMap.add(nodeMap);
			}
			result.put(
					Constant.Keys.TILES,
					getNodesTile((HadoopClusterConf) dbCluster.getClusterConf()));
			result.putAll(Collections.singletonMap(Constant.Keys.NODES,
					nodesMap));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
		return returnResult();
	}

	/**
	 * Method to get nodes tile.
	 * 
	 * @param conf
	 *            the conf
	 * @return the nodes tile
	 */
	private List<TileInfo> getNodesTile(HadoopClusterConf conf) {
		Integer datanodes = conf.getDataNodes().size();
		Integer secnodes = conf.getSecondaryNameNodes().size();
		String namenode = conf.getNameNode().getPublicIp();

		// create namenode tile
		TileInfo namenodeTile = new TileInfo();
		namenodeTile.setLine1(namenode);
		namenodeTile.setLine2(Constant.Keys.NAMENODE);
		namenodeTile.setStatus(Constant.Tile.Status.NORMAL);

		// create datanode tile
		TileInfo datanodeTile = new TileInfo();
		datanodeTile.setLine1(datanodes.toString());
		datanodeTile.setLine2(Constant.Keys.DATANODES);
		datanodeTile.setStatus(Constant.Tile.Status.NORMAL);

		// create secondary name node tile
		TileInfo secondarynodeTile = new TileInfo();
		secondarynodeTile.setLine1(secnodes.toString());
		secondarynodeTile.setLine2(Constant.Keys.SECONDARYNAMENODE);
		secondarynodeTile.setStatus(Constant.Tile.Status.NORMAL);

		// list object.
		List<TileInfo> tiles = new ArrayList<TileInfo>();
		tiles.add(secondarynodeTile);
		tiles.add(datanodeTile);
		tiles.add(namenodeTile);

		return tiles;
	}

	/**
	 * Method to get the list of files and directories.
	 * 
	 * @return The Map of files and directories against the ip address.
	 */
	private void techlogs() {
		String errMsg = "Exception Occured: Unable to process request for log files.";
		try {
			// Getting hadoop cluster conf.
			HadoopConf hadoopConf = this.getHadoopConfObject();
			boolean isHadoop2 = this.isHadoop2Instance(hadoopConf);
			
			// Name Node ip list.
			List<String> nameNode = new ArrayList<String>();
			nameNode.add(hadoopConf.getNamenode().getPublicIp());
			// data node ip list.
			Set<String> dataNodes = new HashSet<String>();
			// filling data nodes.
			for (NodeConf nodeConf : hadoopConf.getSlaves()) {
				dataNodes.add(nodeConf.getPublicIp());
			}
			result.put(Constant.Role.NAMENODE, nameNode);
			result.put(Constant.Role.DATANODE, dataNodes);
			
			if(hadoopConf.getSecondaryNamenode() != null) {
				List<String> secNameNode = new ArrayList<String>();
				secNameNode.add(hadoopConf.getSecondaryNamenode().getPublicIp());
				result.put(Constant.Role.SECONDARYNAMENODE, secNameNode);
			}
					
			if (!isHadoop2) {
				result.put(Constant.Role.JOBTRACKER, nameNode);
				result.put(Constant.Role.TASKTRACKER, dataNodes);
			} else {
				Hadoop2Conf hadoop2Conf = (Hadoop2Conf) hadoopConf; 
				if (hadoop2Conf.isHaEnabled()) {
					// Remove the NameNode & SecondaryNameNode if HA is Enabled
					result.remove(Constant.Role.NAMENODE);	
					result.remove(Constant.Role.SECONDARYNAMENODE);
					
					// Add both of the HA NameNodes to the result object 
					nameNode.clear();
					nameNode.addAll(hadoop2Conf.getHaNameNodesPublicIp());
					result.put(Constant.Role.NAMENODE, nameNode);
				}
				result.put(Constant.Role.RESOURCEMANAGER, Collections
						.singletonList(hadoop2Conf.getResourceManagerNode()
								.getPublicIp()));
				result.put(Constant.Role.NODEMANAGER, dataNodes);
			}
		} catch (Exception e) {
			addAndLogError(errMsg, e);
		}
	}

	/**
	 * Method to get the node type files.
	 * 
	 * @return The Map of files and directories against the ip address.
	 */
	private Map files() {
		String errMsg = "Exception: Unable to process request for log files.";
		try {
			HadoopConf hadoopConf = this.getHadoopConfObject();
			String nodeIp = (String) parameterMap.get(Constant.Keys.IP);
			String type = (String) parameterMap.get(Constant.Keys.TYPE);

			// Adding HTML break tag
			// To be changed when Error Div on UI will support \n character
			errMsg +=  "<br>NodeIP-" + nodeIp + "<br>Type-" + type;
			
			LogViewHandler logHandler = new LogViewHandler(nodeIp,
					hadoopConf.getUsername(), hadoopConf.getPassword(),
					hadoopConf.getPrivateKey());
			
			// Making the log directory.
			String logDirectory = hadoopConf.getComponentHome() + Constant.Keys.LOGS;
			
			// Name Node ip list.
			List<String> files = logHandler.listTypeLogDirectory(logDirectory, type);
			// puting the files in list.
			result.put(Constant.Keys.FILES, files);
		} catch (Exception e) {
			addAndLogError(errMsg, e);
		}
		return returnResult();
	}
	
	/**
	 * Method to view the content of the file.
	 * 
	 * @return The content of file.
	 */
	private Map view() {
		
		String errMsg = "Exception: Unable to process request to view log file.";
		try {
		// Making hadoop conf object.
		HadoopConf hadoopConf = this.getHadoopConfObject();
		
		// Getting the ip address from parameter map.
		String ip = (String) parameterMap.get(Constant.Keys.IP);
		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);
		// Getting the readCount value
		int readCount = ParserUtil.getIntValue((String) parameterMap.get(Constant.Keys.READCOUNT), 0);
		int bytesCount = ParserUtil.getIntValue((String) parameterMap.get(Constant.Keys.BYTESCOUNT), 0);
		
		// Adding HTML break tag
		// To be changed when Error Div on UI will support \n character
		errMsg +=  "<br>NodeIP-" + ip + "<br>Type-" + fileName;
		
		// Create log view handler object.
		LogViewHandler logHandler = new LogViewHandler(ip,
				hadoopConf.getUsername(), hadoopConf.getPassword(),
				hadoopConf.getPrivateKey());

		String filePath = hadoopConf.getComponentHome() + Constant.Keys.LOGS
				+ "/" + fileName;

		// putting the file content with total read characters.
		Map<String, String> content;
		
			content = logHandler
					.getFileContent(filePath, readCount, bytesCount);
			result.putAll(content);
		} catch (Exception e) {
			addAndLogError(errMsg, e);
		}

		return returnResult();
	}

	/**
	 * Method to get download url of the file.
	 * 
	 * @return Map of the download url.
	 */
	private Map download() {
		
		String errMsg = "Exception: Unable to process request to download log file.";

		// Making hadoop conf object.
		ClusterConf clusterConf = (ClusterConf) dbCluster.getClusterConf();
		HadoopConf hadoopConf = this.getHadoopConfObject();
		
		// Getting the ip address from parameter map.
		String ip = (String) parameterMap.get(Constant.Keys.IP);
		// Getting the filename from parameter map.
		String fileName = (String) parameterMap.get(Constant.Keys.FILENAME);
		
		// Adding HTML break tag
		// To be changed when Error Div on UI will support \n character
		errMsg +=  "<br>NodeIP-" + ip + "<br>Type-" + fileName;

		// Create the lod View Handler object.
		LogViewHandler logHandler = new LogViewHandler(ip,
				hadoopConf.getUsername(), hadoopConf.getPassword(),
				hadoopConf.getPrivateKey());

		// Create the file path.
		String filePath = hadoopConf.getComponentHome() + Constant.Keys.LOGS
				+ "/" + fileName;

		// setting download path.
		String downloadPath;
		try {
			downloadPath = logHandler.downloadFile(clusterConf.getClusterName(), filePath);
			result.put(Constant.Keys.DOWNLOADPATH, downloadPath);
		} catch (Exception e) {
			addAndLogError(errMsg, e);
		}

		return returnResult();
	}

	// START -- Hadoop JOB Methods

	/** The Constant STR_SPACE. */
	private static final String STR_SPACE = " ";

	/**
	 * Jobtrackerstatus.
	 * 
	 * @return the map
	 */
	private Map jobtrackerstatus() {
		// hadoop cluster info
		ClusterConf clusterConf = dbCluster.getClusterConf();
		boolean isHadoop2 = false;
		HadoopConf hadoopConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP);
		
		if(hadoopConf == null) {
			hadoopConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
			isHadoop2 = true;
		}
		
		String nodeIp = hadoopConf.getNamenode().getPublicIp();
		
		String jsonMessage = "AllowJobSubmission";
		String roleToCheck = Constant.Role.JOBTRACKER;
		if (isHadoop2) {
			roleToCheck = Constant.Role.RESOURCEMANAGER;
			Hadoop2Conf hadoop2Conf = (Hadoop2Conf) hadoopConf;
			nodeIp = hadoop2Conf.getResourceManagerNode().getPublicIp();
		}
		boolean status = false;
		try {
			// Get the db node info using public IP
			Node hadoopNode = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodeIp);
			if (hadoopNode != null) {

				Long nodeId = hadoopNode.getId();
				// Get the db node monitoring info
				NodeMonitoring nodeMonitoring = monitoringManager
						.getByPropertyValueGuarded(Constant.Keys.NODEID, nodeId);
				if (nodeMonitoring != null) {
					Map<String, Boolean> serviceStatusMap = nodeMonitoring
							.getServiceStatus();

					status = serviceStatusMap.get(roleToCheck);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		result.put(jsonMessage, status);
		if (!status) {
			addError("Job can not be submitted at the moment. Please resolve related errors.");
		}
		return returnResult();
	}

	/**
	 * Method to get the DFS information.
	 */
	private void dfsinfo() {
		try {
			// getting hadoop cluster conf.
			ClusterConf clusterConf = dbCluster.getClusterConf();
			
			boolean isHadoop2 = false;
			HadoopConf hadoopConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP);
			
			if(hadoopConf == null) {
				hadoopConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
				isHadoop2 = true;
			}
			
			// getting name node public ip.
			String nodeIp = hadoopConf.getNamenode().getPublicIp();
			
			// hadoop monitoring data.
			DFSReport dfsReport = new DFSReport();
			
			// getting dfs report.
			if (isHadoop2) {
//				Hadoop2MonitoringData h2MonitoringData = (Hadoop2MonitoringData) new MonitoringManager()
//						.getTechnologyData(nodeIp, Constant.Technology.HADOOP);
//				dfsReport = h2MonitoringData.getDfsReport();
			} else {
				HadoopMonitoringData hMonitoringData = (HadoopMonitoringData) new MonitoringManager()
						.getTechnologyData(nodeIp, Constant.Technology.HADOOP);
				dfsReport = hMonitoringData.getDfsInfos();
			}

			// converting object to map.
			Map dfsReportMap = (Map) JsonMapperUtil.mapFromObject(dfsReport);
			// removing all node info from the map.
			dfsReportMap.remove("allNodeInfo");
			// puting dfsinfo in map.
			result.put("dfsInfo", dfsReportMap);
		} catch (Exception e) {
			addError(e.getMessage());
		}
	}
	
	/**
	 * Appjobinfo.
	 */
	private void appjobinfo() {
		Map map = new HashMap();
		ClusterConf clusterConf = dbCluster.getClusterConf();
		
		Hadoop2Conf hConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
		if(hConf == null) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Unable to fetch Hadoop 2 details.. !");
			return;
		}
		try {
			String appId = (String) parameterMap.get(Constant.Keys.YARN.APPID);
			if(appId == null) {
				addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Invalid request: Please select a valid application id. !");
				return;
			}
			String relativeUrl = Hadoop2Deployer.YARN_REST_API_APPS + "/" + appId;
			
			JSONObject json = this.getJsonObjectFromRmApi(hConf, relativeUrl);
			JSONObject applicationDetails = (JSONObject) json.get(Constant.Keys.YARN.APP);
			Hadoop2Application hadoopApp = JsonMapperUtil.objectFromString(applicationDetails.toJSONString(), Hadoop2Application.class);
			
			if(hadoopApp.isMapredType()) {
				Hadoop2Job jobInfo = this.getJobInfoForApp(hConf, hadoopApp);
				map.put(Constant.Keys.YARN.JSON_JOBINFO, jobInfo);		
			} else {
				addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Invalid request: Please select a mapreduce application for job details. !");
				return;
			}
			result.putAll(map);
		} catch (Exception e) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, e);
		}
	}
	
	/**
	 * Appdetails.
	 */
	private void appdetails() {
		Map map = new HashMap();
		ClusterConf clusterConf = dbCluster.getClusterConf();
		
		Hadoop2Conf hConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
		if(hConf == null) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Unable to fetch Hadoop 2 details.. !");
			return;
		}
		try {
			String appId = (String) parameterMap.get(Constant.Keys.YARN.APPID);
			if(appId == null) {
				addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Invalid request: Please select a valid application id. !");
				return;
			}
			String relativeUrl = Hadoop2Deployer.YARN_REST_API_APPS + "/" + appId;
			
			JSONObject json = this.getJsonObjectFromRmApi(hConf, relativeUrl);
			JSONObject applicationDetails = (JSONObject) json.get(Constant.Keys.YARN.APP);
			Hadoop2Application hadoopApp = JsonMapperUtil.objectFromString(applicationDetails.toJSONString(), Hadoop2Application.class);
			map.put(Constant.Keys.YARN.JSON_APPDETAILS, hadoopApp);
			
			relativeUrl += "/" + Hadoop2Deployer.YARN_REST_API_APP_ATTEMPTS;
			json = this.getJsonObjectFromRmApi(hConf, relativeUrl);
			JSONObject appAttempts = (JSONObject) json.get(Constant.Keys.YARN.APPATTEMPTS);
			List<JSONObject> appAttemptsList = (List<JSONObject>) appAttempts.get(Constant.Keys.YARN.APPATTEMPT);
			map.put(Constant.Keys.YARN.JSON_APPATTEMPTS, appAttemptsList);
			result.putAll(map);
		} catch (Exception e) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, e);
		}
	}
	
	/**
	 * Gets the job info for app.
	 *
	 * @param hConf the h conf
	 * @param hadoopApp the hadoop app
	 * @return the job info for app
	 */
	private Hadoop2Job getJobInfoForApp(Hadoop2Conf hConf, Hadoop2Application hadoopApp) {
		Hadoop2Job hadoop2Job = null;
		JSONObject json = new JSONObject();
		
		if(hadoopApp.getTrackingUI() != null) {
			String trackingUI = hadoopApp.getTrackingUI();
			String jobId = HadoopUtils.getJobIdFromAppId(hadoopApp.getId());
			
			if(trackingUI.equals(Constant.Keys.YARN.TRACKINGUI_APPMASTER)) {
				json = this.getJsonObjectFromAppProxyApi(hConf, hadoopApp.getId(), jobId);
				if(json != null) {
					json = (JSONObject) json.get(Constant.Keys.YARN.JOB);
					hadoop2Job = JsonMapperUtil.objectFromString(json.toJSONString(), Hadoop2RunningJob.class);
				}
			} else if(trackingUI.equals(Constant.Keys.YARN.TRACKINGUI_HISTORY)) {
				
				json = this.getJsonObjectFromJHSApi(hConf, jobId);
				if(json != null) {
					json = (JSONObject) json.get(Constant.Keys.YARN.JOB);
					hadoop2Job = JsonMapperUtil.objectFromString(json.toJSONString(), Hadoop2HistoryJob.class);
				}
			}
		}
		return hadoop2Job;
	}
	
	/**
	 * Gets the json object from rm api.
	 *
	 * @param hConf the h conf
	 * @param relativeUrl the relative url
	 * @return the json object from rm api
	 */
	private JSONObject getJsonObjectFromRmApi(Hadoop2Conf hConf, String relativeUrl) {

		JSONObject json = null;
		try {
			String resourceManagerIp = hConf.getResourceManagerNode().getPublicIp();
			String restPort = Hadoop2Deployer.DEFAULT_PORT_HTTP_RESOURCEMANAGER;
		
			String url = "http://" + resourceManagerIp + ":" + restPort
				+ Hadoop2Deployer.RELATIVE_URL_RM_REST_API + relativeUrl;
		
			if(!HadoopUtils.getServiceStatusForNode(resourceManagerIp, Constant.Role.RESOURCEMANAGER)) {
				String errMsg = Constant.STR_SPACE + Constant.Role.RESOURCEMANAGER + " on node " + resourceManagerIp + " is down"; 
				addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg + errMsg, errMsg);
				return null;
			}
			// Same logic is currently used for Cluster Monitoring Data..
			// Need to move the code to HadoopUtils OR HadoopJobsManager
			AnkushRestClient restClient = new AnkushRestClient();
			String data = restClient.getRequest(url);
			if(data == null) {
				addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Unable to get data from RM rest client, url:" + url);
				return null;
			} else {
				json = (JSONObject) new JSONParser().parse(data);	
			}
			return json;
		} catch (Exception e) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, e);
			return null;
		}
	}
	
	/**
	 * Gets the json object from app proxy api.
	 *
	 * @param hConf the h conf
	 * @param appId the app id
	 * @param relativeUrl the relative url
	 * @return the json object from app proxy api
	 */
	private JSONObject getJsonObjectFromAppProxyApi(Hadoop2Conf hConf, String appId, String relativeUrl) {
		JSONObject json = null;
		try {
		String webAppProxyIp = hConf.getResourceManagerNode().getPublicIp();
		String restPort = Hadoop2Deployer.DEFAULT_PORT_HTTP_RESOURCEMANAGER;
		String serviceToCheck = Constant.Role.RESOURCEMANAGER;
		
		if(hConf.isWebProxyEnabled()) {
			webAppProxyIp = hConf.getWebAppProxyNode().getPublicIp();
			restPort = hConf.getWebAppProxyPort();
			serviceToCheck = Constant.Role.WEBAPPPROXYSERVER;
		}
		
		if(!HadoopUtils.getServiceStatusForNode(webAppProxyIp, serviceToCheck)) {
			String errMsg = Constant.Role.WEBAPPPROXYSERVER + " on node " + webAppProxyIp + " is down"; 
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg + errMsg, errMsg);
			return null;
		}
		
		String url = "http://" + webAppProxyIp + ":" + restPort
				+ Hadoop2Deployer.RELATIVE_URL_APPPROXY_REST_API.replace(Hadoop2Deployer.APPPROXY_REST_API_TEXT_APPID , appId)
				+ relativeUrl;
		
			AnkushRestClient restClient = new AnkushRestClient();
			String data = restClient.getRequest(url);
			if(data == null) {
				addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Unable to get data from RM rest client, url:" + url);
				return null;	
			} else {
				json = (JSONObject) new JSONParser().parse(data);	
			}
			return json;
		} catch (Exception e) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, e);
			return null;
		}
		
	}
	
	/**
	 * Gets the json object from jhs api.
	 *
	 * @param hConf the h conf
	 * @param relativeUrl the relative url
	 * @return the json object from jhs api
	 */
	private JSONObject getJsonObjectFromJHSApi(Hadoop2Conf hConf, String relativeUrl) {
		JSONObject json = null;
		try {
		String jobHistoryServerIp = hConf.getJobHistoryServerNode().getPublicIp();
		String restPort = Hadoop2Deployer.DEFAULT_PORT_REST_JOBHISTORYSERVER;
		
		if(!HadoopUtils.getServiceStatusForNode(jobHistoryServerIp, Constant.Role.JOBHISTORYSERVER)) {
			String errMsg = Constant.Role.JOBHISTORYSERVER + " on node " + jobHistoryServerIp + " is down"; 
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg + errMsg, errMsg);
			return null;
		}
		
		String url = "http://" + jobHistoryServerIp + ":" + restPort
				+ Hadoop2Deployer.RELATIVE_URL_JHS_REST_API + relativeUrl;
		
			AnkushRestClient restClient = new AnkushRestClient();
			String data = restClient.getRequest(url);
			if(data == null) {
				addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Unable to get data from Job History Server rest client, url:" + url);
				return null;
			} else {
				json = (JSONObject) new JSONParser().parse(data);	
			}
			return json;
		} catch (Exception e) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, e);
			return null;
		}
	}
	
	/**
	 * Hadoop2applist.
	 */
	private void hadoop2applist() {
		Map map = new HashMap();
		ClusterConf clusterConf = dbCluster.getClusterConf();
		
		Hadoop2Conf hConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
		if(hConf == null) {
			addAndLogError(HadoopClusterMonitor.AppMonitoringErrMsg, "Unable to fetch Hadoop 2 details.. !");
			return;
		}
		
		String relativeUrl = Hadoop2Deployer.YARN_REST_API_APPS;
		JSONObject json = this.getJsonObjectFromRmApi(hConf, relativeUrl);
		if (json == null || json.isEmpty()) {
			return;
		}
		JSONObject apps = (JSONObject) json.get(Constant.Keys.YARN.APPS);
		if (apps == null || apps.isEmpty()) {
			return;
		} 
		List<JSONObject> applicationListJson = (List<JSONObject>) apps.get(Constant.Keys.YARN.APP);
		List<Hadoop2Application> applicationList = new ArrayList<Hadoop2Application>(); 
		for(JSONObject obj : applicationListJson) {
			Hadoop2Application hadoopApp = JsonMapperUtil.objectFromString(obj.toJSONString(), Hadoop2Application.class);
			if(hadoopApp != null) {
				applicationList.add(hadoopApp);
			}
		}
		if (applicationListJson == null || applicationListJson.isEmpty()) {
			return;
		}
		map.put(Constant.Keys.YARN.JSON_HADOOP2APPLIST, applicationList);
		result.putAll(map);
	}
	
	/**
	 * Hadoopjobs.
	 * 
	 * @return the map
	 */
	private Map hadoopjobs() {
		Map map = new HashMap();
		String nodeIp = "";
		ClusterConf clusterConf = ((ClusterConf) dbCluster
				.getClusterConf());
		
		HadoopConf hConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP);
		nodeIp = hConf.getNamenode().getPublicIp();
		
		try {
			// Get the db node info using public IP
			Node hadoopNode = nodeManager.getByPropertyValueGuarded("publicIp",
					nodeIp);
			if (hadoopNode != null) {
				Long nodeId = hadoopNode.getId();

				// Get the db node monitoring info
				NodeMonitoring nodeMonitoring = monitoringManager
						.getByPropertyValueGuarded(Constant.Keys.NODEID, nodeId);
				if (nodeMonitoring != null) {
					String tileStatus = Constant.Tile.Status.NORMAL;
					List<TileInfo> jobTiles = new ArrayList<TileInfo>();
					TileInfo ti = null;
					Map<String, Boolean> serviceStatusMap = nodeMonitoring
							.getServiceStatus();
					if (nodeMonitoring.isAgentDown()) {
						addError("Ankush Agent is down on " + nodeIp
								+ " . Please resolve related issues.");
					} else {
						boolean status = false;
						if (serviceStatusMap.containsKey(Constant.Role.JOBTRACKER)) {
							status = serviceStatusMap
									.get(Constant.Role.JOBTRACKER);
						}
						if (status) {
							HadoopMonitoringData technologyData = (HadoopMonitoringData) nodeMonitoring
									.getTechnologyData().get(Constant.Technology.HADOOP);

							ArrayList<HadoopJob> jobInfos = technologyData
									.getJobInfos();

							for (HadoopJob job : jobInfos) {
								job.setCounters(null);
								job.setMapReport(null);
								job.setSetupReport(null);
								job.setReduceReport(null);
								job.setCleanupReport(null);
							}

							map.put(Constant.Keys.TILES,
									technologyData.getJOBTiles());
							map.put(Constant.Keys.JOBS, jobInfos);
						} else {
							tileStatus = Constant.Tile.Status.ERROR;
							// namenode down tile.
							ti = new TileInfo(Constant.Keys.DOWN,
									Constant.Role.JOBTRACKER,
									"Could not fetch job data.", null,
									tileStatus, null);
							jobTiles.add(ti);
							map.put(Constant.Keys.TILES, jobTiles);
						}
					}
				} else {
					addError("Ankush Agent is down on " + nodeIp
							+ " . Please resolve related issues.");
				}
			}
		} catch (Exception e) {
			addError("Exception: Could not retrieve job list.. !");
			logger.error("", e);
		}
		result.putAll(map);
		return map;
	}
	
	/**
	 * Jobdetails.
	 *
	 * @throws Exception the exception
	 */
	private void jobdetails() throws Exception {
		/** checking job id in input parameters **/
		if (!parameterMap.containsKey(Constant.Keys.JOBID)) {
			addError("Job id is missing, please provide the job id.");
			return;
		}

		/** jobid **/
		String jobId = (String) parameterMap.get(Constant.Keys.JOBID);

		/** hadooop conf object **/
		HadoopConf hConf = (HadoopConf) dbCluster.getClusterConf()
				.getClusterComponents().get(Constant.Component.Name.HADOOP);

		/** Node monitoring object **/
		NodeMonitoring nodeMonitoring = new MonitoringManager()
				.getMonitoringData(hConf.getNamenode().getId());

		/** hadoop monitoring data **/
		HadoopMonitoringData hMonitoringData = (HadoopMonitoringData) nodeMonitoring
				.getTechnologyData().get(Constant.Component.Name.HADOOP);

		/** job list **/
		List<HadoopJob> jobs = hMonitoringData.getJobInfos();

		// list of tiles.
		List<TileInfo> tiles = new ArrayList<TileInfo>();

		/** iterating over the job list to find the job **/
		for (HadoopJob job : jobs) {
			if (job.getJobId().equals(jobId)) {
				result.putAll(JsonMapperUtil.mapFromObject(job));

				Date date = new Date();
				Date startdate = job.getJobStartTime();
				Date age = new Date(startdate.getTime() - date.getTime());
				// status
				tiles.add(new TileInfo(job.getJobState(), "Age : ", null, null,
						Constant.Tile.Status.NORMAL, ""));

				// mapper
				tiles.add(new TileInfo(job.getMapCompleted() + "/"
						+ job.getMapTotal(), "Mappers", null, null,
						Constant.Tile.Status.NORMAL, ""));

				// reducer
				tiles.add(new TileInfo(job.getReduceCompleted() + "/"
						+ job.getReduceTotal(), "Reducers", null, null,
						Constant.Tile.Status.NORMAL, ""));

				// counters.
				tiles.add(new TileInfo(job.getCounters().size() + "",
						"Counters", null, null, Constant.Tile.Status.NORMAL, ""));
				result.put("tiles", tiles);
				break;
			}
		}

	}

	/**
	 * Submitjob.
	 * 
	 * @return the map
	 */
	private Map submitjob() {
		Map map = new HashMap();
		StringBuffer jobSubmitCommand = new StringBuffer();
		
		ClusterConf clusterConf = ((ClusterConf) dbCluster
				.getClusterConf());
		HadoopConf hadoopConf = (HadoopConf) clusterConf.getClusterComponents()
				.get(Constant.Component.Name.HADOOP);
		
		if (hadoopConf == null) {
			hadoopConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
			jobSubmitCommand.append(hadoopConf.getComponentHome()
					+ Hadoop2Deployer.RELPATH_COMMMAND_DIR
					+ Constant.Hadoop.Command.YARN + STR_SPACE);
		} else {
			jobSubmitCommand.append(hadoopConf.getComponentHome()
					+ Hadoop1Deployer.COMPONENT_FOLDER_BIN
					+ Constant.Hadoop.Command.HADOOP + STR_SPACE);
		}

		String hadoopBinPath = FileUtils
				.getSeparatorTerminatedPathEntry(hadoopConf.getComponentHome())
				+ Hadoop1Deployer.COMPONENT_FOLDER_BIN;

		List<String> jobArguments = (List<String>) parameterMap
				.get(Constant.Keys.JOBARGS);
		List<Map<String, String>> hadoopParams = (List<Map<String, String>>) parameterMap
				.get(Constant.Keys.HADOOPPARAMS);

		String uploadedJarPath = (String) parameterMap
				.get(Constant.Keys.JARPATH);
		String jobName = (String) parameterMap.get(Constant.Keys.JOB);
		String jobType = Constant.Keys.JAR;
		String jarPath = null;

		
		String hostname = hadoopConf.getNamenode().getPublicIp();
		String username = clusterConf.getUsername();
		String password = clusterConf.getPassword();
		String privateKey = clusterConf.getPrivateKey();
		
		String userHome = CommonUtil.getUserHome(clusterConf
				.getUsername());
		String destJobFilePath = FileUtils
				.getSeparatorTerminatedPathEntry(userHome)
				+ ".ankush/jobs/hadoop/";

		SSHExec connection = null;
		boolean isSuccessful = false;
		try {
			File f = new File(uploadedJarPath);
			if (!f.exists()) {
				addError("Could not find jar file.. !");
			} else {
				String fileName = FileUtils.getNamePart(uploadedJarPath);
				jarPath = destJobFilePath + fileName;

				connection = SSHUtils.connectToNode(hostname, username, password, privateKey);
				if (connection != null) {
					// Create Directory on master node
					AnkushTask createDir = new MakeDirectory(destJobFilePath);
					Result res = connection.exec(createDir);
					if (!res.isSuccess) {
						addError("Could not create job directory on node..! ");
					} else {
						// Uploading hob file to master node
						try {
							connection.uploadSingleDataToServer(
									uploadedJarPath, jarPath);
							isSuccessful = true;
						} catch (Exception e1) {
							addError("Could not upload job file to node.. !");
						}
					}
				} else {
					addError("Could not establish connection to node.. !");
				}
			}
		} catch (Exception e) {
			addError("Could not submit job.. !");
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}

		if (isSuccessful) {
			// Job Submission Logic
			try {
				jobSubmitCommand.append(jobType).append(STR_SPACE);
				jobSubmitCommand.append(jarPath).append(STR_SPACE);
				jobSubmitCommand.append(jobName).append(STR_SPACE);

				if (hadoopParams != null) {
					for (Map<String, String> params : hadoopParams) {
						Iterator iter = params.keySet().iterator();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							String val = params.get(key);
							jobSubmitCommand.append(key).append(STR_SPACE)
									.append(val).append(STR_SPACE);
						}
					}
				}
				if (jobArguments != null) {
					for (String args : jobArguments) {
						jobSubmitCommand.append(args).append(STR_SPACE);
					}
				}
				final String commad = jobSubmitCommand.toString();
				AppStoreWrapper.getExecutor().execute(new Runnable() {
					@Override
					public void run() {
						submitJob(dbCluster, commad);
					}
				});

			} catch (Exception e) {
				addError("Job Submission failed " + e.getMessage());
				logger.error("", e);
			}
		}
		result.putAll(map);
		return map;
	}

	/**
	 * Killjob.
	 * 
	 * @return the map
	 */
	private Map killjob() {
		Map map = new HashMap();
		final List<String> jobs = (List<String>) parameterMap
				.get(Constant.Keys.JOBS);

		try {
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					killJob(dbCluster, jobs);
				}
			});
		} catch (Exception e) {
			addError("Could not kill Job.. !");
			logger.error("", e);
		}
		result.putAll(map);
		return map;
	}

	/**
	 * Updatejobpriority.
	 * 
	 * @return the map
	 */
	private Map updatejobpriority() {
		Map map = new HashMap();
		final Map<String, String> jobs = parameterMap;
		try {
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					updateJobPriority(dbCluster, jobs);
				}
			});
		} catch (Exception e) {
			addError("Could not update job priority.. !");
			logger.error("", e);
		}

		result.putAll(map);
		return map;
	}

	/**
	 * Submit job.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param command
	 *            the command
	 */
	private void submitJob(Cluster cluster, String command) {
		SSHExec connection = null;
		Result res = null;
		ClusterConf clusterConf = cluster.getClusterConf();
		HadoopConf hadoopConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP);

		if (hadoopConf == null) {
			hadoopConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
		}
		String hadoopBinPath = FileUtils
				.getSeparatorTerminatedPathEntry(hadoopConf.getComponentHome())
				+ Hadoop1Deployer.COMPONENT_FOLDER_BIN;

		String hostname = hadoopConf.getNamenode().getPublicIp();
		String username = clusterConf.getUsername();
		String password = clusterConf.getPassword();
		String privateKey = clusterConf.getPrivateKey();
		
		try {
			connection = SSHUtils.connectToNode(hostname, username, password,
					privateKey);
			if (connection != null) {
				logger.debug("Submitting Job : " + command);
				CustomTask jobTask = new ExecCommand(command);
				res = connection.exec(jobTask);
				if (!res.isSuccess) {
					logger.error("Could not submit Job..");
				} else {
					logger.info("Job Submitted Successfully ..");
				}
			} else {
				addError("Could not establish connection.. !");
			}
		} catch (TaskExecFailException e) {
			logger.error("", e);
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Kill job.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param jobs
	 *            the jobs
	 */
	private void killJob(Cluster cluster, List<String> jobs) {
		ClusterConf clusterConf = ((ClusterConf) cluster
				.getClusterConf());

		HadoopConf hadoopConf = (HadoopConf) clusterConf.getClusterComponents()
				.get(Constant.Component.Name.HADOOP);

		StringBuffer killJobCommand = new StringBuffer();
		if (hadoopConf == null) {
			hadoopConf = (Hadoop2Conf) clusterConf.getClusterComponents().get(
					Constant.Component.Name.HADOOP2);
			killJobCommand.append(hadoopConf.getComponentHome()
					+ Hadoop2Deployer.RELPATH_COMMMAND_DIR
					+ Constant.Hadoop.Command.YARN + STR_SPACE
					+ Constant.Hadoop.Command.APPLICATION + STR_SPACE);
		} else {
			killJobCommand.append(hadoopConf.getComponentHome()
					+ Hadoop1Deployer.COMPONENT_FOLDER_BIN
					+ Constant.Hadoop.Command.HADOOP + STR_SPACE
					+ Constant.Hadoop.Command.JOB + STR_SPACE);

		}

		String hostname = hadoopConf.getNamenode().getPublicIp();
		String username = clusterConf.getUsername();
		String password = clusterConf.getPassword();
		String privateKey = clusterConf.getPrivateKey();
		
		SSHExec connection = null;
		try {
			connection = SSHUtils.connectToNode(hostname, username, password,
					privateKey);
			if (connection != null) {

				for (final String jobId : jobs) {
					killJobCommand.append("-kill ").append(jobId);

					CustomTask jobTask = new ExecCommand(
							killJobCommand.toString());
					Result res = connection.exec(jobTask);
					if (!res.isSuccess) {
						logger.error("Could not kill job " + jobId);
					}

				}
			} else {
				addError("Could not establish connection !!");
			}
		} catch (Exception e) {
			addError("Could not kill job....!! " + e.getMessage());
			logger.error("", e);
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	/**
	 * Update job priority.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param jobInfo
	 *            the job info
	 */
	private void updateJobPriority(Cluster cluster, Map<String, String> jobInfo) {
		ClusterConf clusterConf = ((ClusterConf) cluster
				.getClusterConf());
		HadoopConf hadoopConf = this.getHadoopConfObject();
				
		String hadoopBinPath = FileUtils
				.getSeparatorTerminatedPathEntry(hadoopConf.getComponentHome())
				+ Hadoop1Deployer.COMPONENT_FOLDER_BIN;
		
		String hostname = hadoopConf.getNamenode().getPublicIp();
		String username = clusterConf.getUsername();
		String password = clusterConf.getPassword();
		String privateKey = clusterConf.getPassword();
		
		SSHExec connection = null;
		try {
			connection = SSHUtils.connectToNode(hostname, username, password,
					privateKey);
			if (connection != null) {
				logger.debug("jobInfo : " + jobInfo);
				Iterator iter = jobInfo.keySet().iterator();
				while (iter.hasNext()) {
					final String jobId = (String) iter.next();
					String newPriority = jobInfo.get(jobId);

					StringBuffer priorityUpdateCommand = new StringBuffer();
					priorityUpdateCommand.append(hadoopBinPath)
							.append("hadoop job ").append("-set-priority ")
							.append(jobId).append(STR_SPACE)
							.append(newPriority);

					CustomTask jobTask = new ExecCommand(
							priorityUpdateCommand.toString());
					Result res = connection.exec(jobTask);

					if (!res.isSuccess) {
						logger.error("Could not update priority for job "
								+ jobId);
					}
				}
			} else {
				addError("Could not establish connection..!");
			}
		} catch (Exception e) {
			addError("Could not update job priority..!");
			logger.error("Could not update job priority..", e);
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	// END -- Hadoop JOB Methods

	/**
	 * Safemodeleave.
	 */
	private void safemodeleave() {
		ClusterConf clusterConf = dbCluster.getClusterConf();
		HadoopConf hadoopConf = (HadoopConf) clusterConf.getClusterComponents().get(Constant.Component.Name.HADOOP);
		String hadoopBinPath = FileUtils
				.getSeparatorTerminatedPathEntry(hadoopConf.getComponentHome())
				+ "bin/";

		String hostname = hadoopConf.getNamenode().getPublicIp();
		String username = clusterConf.getUsername();
		String password = clusterConf.getPassword();
		String privateKey = clusterConf.getPassword();
		
		SSHExec connection = null;
		try {
			// establishing connection with NameNode
			connection = SSHUtils.connectToNode(hostname, username, password,
					privateKey);
			if (connection != null) {
				StringBuffer safemodeLeaveCmnd = new StringBuffer();
				safemodeLeaveCmnd.append(hadoopBinPath).append(
						"hadoop dfsadmin -safemode leave");

				CustomTask safemodeTask = new ExecCommand(
						safemodeLeaveCmnd.toString());
				// executing safemode leave command
				Result res = connection.exec(safemodeTask);

				if (!res.isSuccess) {
					logger.error("Could not execute safemode leave..!");
				}
			}
		} catch (Exception e) {
			addError("Could not execute safemode leave..!");
			logger.error("Could not execute safemode leave..!", e);
		} finally {
			// Disconnect the connection
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	// START -- Hadoop Configuration Parameter methods
	
	/**
	 * Listconfparam.
	 * 
	 * @return the map
	 */
	private void listconfparam() {
		Map resultInfo = new HashMap();
		boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
		if (isAgentDown) {
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return;
		}
		resultInfo = parameterConfigService.getComponentConfigFiles(
					dbCluster, parameterMap);
		result.putAll(resultInfo);
		return;
	}

	/**
	 * Listconffileparam.
	 * 
	 * @return the map
	 */
	private void listconffileparam() {
		Map<String, List<Parameter>> resultInfo = new HashMap<String, List<Parameter>>();
		boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
		if (isAgentDown) {
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return;
		}
		resultInfo = parameterConfigService.getCompConfFileParams(
					dbCluster, parameterMap);
		result.putAll(resultInfo);
		return;
	}

	/**
	 * Editconfparams.
	 * 
	 * @return the map
	 */
	private void editconfparams() {
		Map map = new HashMap();
		String errMsg = "Exception: Unable to update configuration file parameters.";
		try {
			boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
			if (isAgentDown) {
				addError(Constant.Agent.AGENT_DOWN_MESSAGE);
				return;
			}
			parameterConfigService.updateConfigFileParam(dbCluster, parameterMap);
		} catch (Exception e) {
			addAndLogError(errMsg, e);
		}
		result.putAll(map);
		return;
	}
	
	/**
	 * Params.
	 *
	 */
	private void params(){
		try {
			
			final HadoopConf hConf = this.getHadoopConfObject();
			final String nameNodeIp = hConf.getNamenode().getPublicIp();
			ClusterConf clusterConf = hConf.getClusterConf();
			
		if(!HadoopUtils.getServiceStatusForNode(nameNodeIp, Constant.Role.AGENT)) {
			addError(AgentUtils.getAgentDownMessage(nameNodeIp));
			return;
		}

		final String username = clusterConf.getUsername();
		String password = clusterConf.getPassword();
		boolean authUsingPassword = clusterConf.isAuthTypePassword();
		if(!authUsingPassword) {
			password = clusterConf.getPrivateKey();
		}
		
		final String componentConfPath = hConf.getHadoopConfDir();
		List<String> confFiles = SSHUtils.listDirectory(nameNodeIp, username, password, authUsingPassword, componentConfPath);
		
		final Semaphore semaphore = new Semaphore(confFiles.size());
		final boolean authUsingPasswordFinal = authUsingPassword; 
		final String passwordFinal = password;
		
		Map resultInfo = new HashMap();

		// Code is currently not optimized.
		// Need to run the following logic (to get contents of each file) using FutureTask for each file
		
		for (String fileName : confFiles) {
			if (fileName.endsWith(HadoopClusterMonitor.EXTENSION_XML)
					&& (!fileName.equals("fair-scheduler.xml"))) {
				String filePath = FileUtils
						.getSeparatorTerminatedPathEntry(componentConfPath)
						+ fileName;
			
				String content = SSHUtils.getFileContents(filePath, nameNodeIp,
						hConf.getUsername(), passwordFinal, hConf.getClusterConf().isAuthTypePassword());
				List<Parameter> fileParams = new ArrayList<Parameter>();
				if (content != null && content.length() > 0) {
					fileParams = ParameterConfigServiceImpl.loadXMLParameters(content);
				}
				Map<String, List<Object>> map = new HashMap<String, List<Object>>();
				
				// Converting Properties into Map.
				for (Parameter parameter : fileParams) {
					List<Object> list = new ArrayList<Object>();
					list.add(parameter.getValue());
					list.add(this.isParameterEditable(parameter.getName()));
					map.put(parameter.getName(), list);
				}
				resultInfo.put(fileName, map);		
			}
		}
		
		result.put("params", resultInfo);
		} catch (Exception e) {
			addAndLogError("Could not get parameter list from Hadoop NameNode.", e);
		}
	}

	/**
	 * Editparams.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	private void editparams() throws Exception {

		boolean isAgentDown = new EventManager().isAnyAgentDown(dbCluster);
		if (isAgentDown) {
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return;
		}
		
		HadoopConf conf = this.getHadoopConfObject();

		Map<String, Object> confParams = (Map<String, Object>) parameterMap.get("params");
		
		String loggedUser = (String) parameterMap.get("loggedUser");
		
		// iterate over confParams Map.
		for (Entry entry : confParams.entrySet()) {

			// get fileName
			String fileName = (String) entry.getKey();

			// get config params list
			List<Map> params = (List<Map>) entry.getValue();

			// iterate on each param
			for (Map param : params) {
				Parameter parameter = JsonMapperUtil.objectFromMap(param, Parameter.class);

				String status = parameter.getStatus();
				// If no status Set for parameter
				if (status.equals(Constant.Parameter_Status.NONE)) {
					continue;
				}
				if (status.equals(Constant.Parameter_Status.ADD)) {
					addConfigFileParam(conf, parameter, fileName, loggedUser);
				}
				if (status.equals(Constant.Parameter_Status.EDIT)) {
					editConfigFileParam(conf, parameter, fileName, loggedUser);
				}
				if (status.equals(Constant.Parameter_Status.DELETE)) {
					deleteConfigFileParam(conf, parameter, fileName, loggedUser);
				}
			}
		}
	}
	
	/**
	 * add parameter to configuration file.
	 *
	 * @param conf the conf
	 * @param parameter the parameter
	 * @param fileName the file name
	 * @param loggedUser the logged user
	 */
	private void addConfigFileParam(final GenericConfiguration conf,final Parameter parameter,
			final String fileName, final String loggedUser) {
		Set<NodeConf> nodes = conf.getCompNodes();
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String propertyValue = parameter.getValue();
							
							// get component Conf Dir
							String componentConfPath = ((HadoopConf)conf).getHadoopConfDir();

							String filePath = componentConfPath + fileName;

							String username = conf.getUsername();
							String password = conf.getPassword();
							String privateKey = conf.getPrivateKey();

							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask add = new AddConfProperty(propertyName, propertyValue,
											filePath, Constant.File_Extension.XML);
									res = connection.exec(add);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												conf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, propertyValue);
									}
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}
	}

	/**
	 * edit the Configuration parameters.
	 *
	 * @param conf the conf
	 * @param parameter the parameter
	 * @param fileName the file name
	 * @param loggedUser the logged user
	 */
	private void editConfigFileParam(final GenericConfiguration conf, final Parameter parameter,
			final String fileName, final String loggedUser) {

		Set<NodeConf> nodes = conf.getCompNodes();
		
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							ConfigurationManager confManager = new ConfigurationManager();
							String propertyName = parameter.getName();
							String newValue = parameter.getValue();

							// get component Conf Dir
							String componentConfPath = ((HadoopConf)conf).getHadoopConfDir();

							String filePath = componentConfPath + fileName;

							String username = conf.getUsername();
							String password = conf.getPassword();
							String privateKey = conf.getPrivateKey();
							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask update = new EditConfProperty(
											propertyName, newValue,
											filePath,
											Constant.File_Extension.XML);
									res = connection.exec(update);

									if (res.isSuccess) {
										// Configuration manager to save the
										// property file change records.
										confManager.saveConfiguration(
												conf.getClusterDbId(),
												loggedUser, fileName, hostName,
												propertyName, newValue);
									}
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
							}
							if (semaphore != null) {
								semaphore.release();
							}

						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}

	}

	/**
	 * delete the configuration parameters.
	 *
	 * @param conf the conf
	 * @param parameter the parameter
	 * @param fileName the file name
	 * @param loggedUser the logged user
	 */
	private void deleteConfigFileParam(final GenericConfiguration conf, final Parameter parameter,
			final String fileName, final String loggedUser) {
		Set<NodeConf> nodes = conf.getCompNodes();
		
		try {
			final Semaphore semaphore = new Semaphore(nodes.size());
			try {
				// iterate over all the nodes.
				for (final NodeConf node : nodes) {
					semaphore.acquire();
					AppStoreWrapper.getExecutor().execute(new Runnable() {
						@Override
						public void run() {
							Result res = null;

							String propertyName = parameter.getName();

							ConfigurationManager confManager = new ConfigurationManager();
							
							// get component Conf Dir
							String componentConfPath = ((HadoopConf)conf).getHadoopConfDir();

							String filePath = componentConfPath + fileName;

							String username = conf.getUsername();
							String password = conf.getPassword();
							String privateKey = conf.getPrivateKey();
							SSHExec connection = null;
							String hostName = node.getPublicIp();
							try {
								// connect to node/machine
								connection = SSHUtils.connectToNode(hostName,
										username, password, privateKey);
								// if connection is established.
								if (connection != null) {
									AnkushTask update = new DeleteConfProperty(
											propertyName, filePath,
											Constant.File_Extension.XML);
									res = connection.exec(update);
									if (res.isSuccess) {
										confManager.removeOldConfiguration(
												conf.getClusterDbId(),
												hostName, fileName, propertyName);
									}
									
								} else {
									logger.error("Could not connect to node..."
											+ node.getPublicIp());
								}
							} catch (Exception e) {
								logger.error("error:" + e.getMessage());
							} finally {
								// Disconnecting the connection
								if (connection != null) {
									connection.disconnect();
								}
								if (semaphore != null) {
									semaphore.release();
								}
							}
						}
					});
				}
				semaphore.acquire(nodes.size());
			} catch (Exception e) {
				logger.error("Error in updating config file params..."
						+ e.getMessage());
			}
		} catch (Exception e) {
			logger.error("Could not update conf file parameters..."
					+ e.getMessage());
		}

	}
	
	/**
	 * Checks if is parameter editable.
	 *
	 * @param parameterName the parameter name
	 * @return true, if is parameter editable
	 */
	private boolean isParameterEditable(String parameterName) {
		try {
			boolean result = true;
			
			String predefinedNonEditableParameters = AppStoreWrapper.getAnkushConfReader().getStringValue(
					"hadoop.non.editable.parameters");
			
				if(predefinedNonEditableParameters != null) {
				List<String> nonEditableParametersList = Arrays.asList(predefinedNonEditableParameters.split(","));
				if(nonEditableParametersList.contains(parameterName)) {
					return false;		
				}
			}
			return result;
		} catch (Exception e) {
			return true;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.AbstractMonitor#categories()
	 */
	public Map categories() throws Exception {
		String errMsg = "Exception: Unable to process request for Utilization Graphs.";
		Map categoryMap = (Map) super.categories().get("categories");
		try {
			String nodeIp = (String) parameterMap.get(Constant.Keys.IP);

			Node node = nodeManager.getByPropertyValueGuarded(
					Constant.Keys.PUBLICIP, nodeIp);
			HadoopNodeConf nodeConf = (HadoopNodeConf) node.getNodeConf();

			Map<String, String> categories = new HashMap<String, String>();

			// node types with / deliminator.
			String type = nodeConf.getType();

			if (type.contains(Constant.Role.NAMENODE)) {
				categories.put("dfs.namenode", "dfs\\.namenode.*\\.rrd");
				categories.put("dfs.FSNamesystem", "dfs\\.FSNamesystem.*\\.rrd");
				categories.put("mapred.Queue", "mapred\\.Queue.*\\.rrd");
				categories.put("mapred.jobtracker", "mapred\\.jobtracker.*\\.rrd");
				categories.put("rpcdetailed.rpcdetailed",
						"rpcdetailed\\.rpcdetailed.*\\.rrd");
				categories.put("default.shuffleInput",
						"default\\.shuffleInput.*\\.rrd");
			}

			if (type.contains(Constant.Role.DATANODE)) {
				categories.put("dfs.datanode", "dfs\\.datanode.*\\.rrd");
				categories.put("mapred.shuffleOutput",
						"mapred\\.shuffleOutput.*\\.rrd");
				categories
					.put("mapred.tasktracker", "mapred\\.tasktracker.*\\.rrd");
			}

			categories.put("rpc.rpc", "rpc\\.rpc.*\\.rrd");
			categories.put("ugi.ugi", "ugi\\.ugi.*\\.rrd");
			categories.put("jvm.metrics", "jvm\\.metrics.*\\.rrd");
			categories.put("metricssystem.MetricsSystem",
				"metricssystem\\.MetricsSystem.*\\.rrd");

		
			// creating node graph object.
			Graph graph = new Graph(this.clusterConfig.getGangliaMaster()
					.getPublicIp(), this.clusterConfig.getUsername(),
					this.clusterConfig.getPassword(),
					this.clusterConfig.getPrivateKey(),
					this.clusterConfig.getClusterName());

			List<String> files = graph.getAllFiles(nodeIp);

			for (String category : categories.keySet()) {
				Map map = new HashMap();
				List<String> matchingFiles = graph.getMatchingFiles(
						categories.get(category), files);

				map.put("count", matchingFiles.size());
				map.put("legends", matchingFiles);
				map.put("pattern", categories.get(category));
				map.put("ip", nodeIp);
				if (matchingFiles != null && !matchingFiles.isEmpty()) {
					categoryMap.put(category, map);
				}
			}
		} catch (Exception e) {
			addAndLogError(errMsg, e);
		}
		result.put("categories", categoryMap);
		return result;
	}
}
