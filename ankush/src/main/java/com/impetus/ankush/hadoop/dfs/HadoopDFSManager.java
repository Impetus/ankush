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
package com.impetus.ankush.hadoop.dfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.sf.json.JSONArray;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.AuthConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.ReadConfProperty;
import com.impetus.ankush.common.tiles.TileInfo;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.AnkushRestClient;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.CallableJmxBeanData;
import com.impetus.ankush.hadoop.HadoopTileViewHandler;
import com.impetus.ankush.hadoop.HadoopUtils;
import com.impetus.ankush.hadoop.config.HadoopClusterConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.Hadoop1Deployer;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Deployer;
import com.impetus.ankush.hadoop.job.Hadoop2RunningJob;
import com.impetus.ankush.hadoop.job.HadoopJobsManager;

// TODO: Auto-generated Javadoc
/**
 * The Class HadoopDFSManager.
 * 
 * @author bgunjan
 */
public class HadoopDFSManager {

	/** The LOGGER. */
	private static final AnkushLogger LOGGER = new AnkushLogger(
			HadoopDFSManager.class);
	
	/** The Constant DFS_CONTENT_TYPE_DIR. */
	private static final String DFS_CONTENT_TYPE_DIR = "Dir";

	/** The Constant DFS_CONTENT_TYPE_FILE. */
	private static final String DFS_CONTENT_TYPE_FILE = "File";
	
	/** The Constant JMX_DATA_KEY_BEANS. */
	private static final String JMX_DATA_KEY_BEANS = "beans";
	
	/** The Constant JMX_DATA_KEY_BEAN_NAME. */
	private static final String JMX_DATA_KEY_BEAN_NAME = "name";
	
	/** The Constant JMX_BEAN_NAME_NAMENODEINFO. */
	public static final String JMX_BEAN_NAME_NAMENODEINFO = "Hadoop:service=NameNode,name=NameNodeInfo";
	
	/** The Constant JMX_BEAN_NAME_NAMENODEINFO for CDHv3. */
	public static final String JMX_BEAN_NAME_NAMENODEINFO_CHD3 = "hadoop:service=NameNode,name=NameNodeInfo";
	
	/** The Constant JMX_BEAN_NAME_JAVA_RUNTIME. */
	private static final String JMX_BEAN_NAME_JAVA_RUNTIME = "java.lang:type=Runtime";
	
	/**
	 * Read dfs file content.
	 * 
	 * @param path
	 *            the path
	 * @param fileSystem
	 *            Reads dfs file content for any given file
	 * @return String
	 */
	public String readDFSFileContent(Path path, FileSystem fileSystem) {
		String fileContent = "";
		BufferedReader bufferedReader = null;
		// Open the DFS file for reading
		try {
			// Construct the BufferedReader object
			bufferedReader = new BufferedReader(new InputStreamReader(
					fileSystem.open(path)));
			String line;
			line = bufferedReader.readLine();
			while (line != null) {
				// Process the file content
				fileContent += (line + System.getProperty("line.separator")
						.toString());
				line = bufferedReader.readLine();
			}
		} catch (Exception e) {
		} finally {
			try {
				// Close the BufferedReader
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}
		return fileContent;
	}

	/**
	 * Dfs file dir structure.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param dfsPath
	 *            the dfs path
	 * @return map
	 */
	public Map<String, Object> dfsFileDirStructure(Cluster cluster,
			String dfsPath) {
		Map<String, Object> dfsFileStatusInfo = new HashMap<String, Object>();
		Configuration conf = new Configuration();
		Map<String, Object> pathDetail = new HashMap<String, Object>();

		try {
			// Getting HadoopClusterConf Object from given cluster
			HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) cluster
					.getClusterConf();
			// Getting Namenode public IP
			String host = hadoopClusterConf.getNameNode().getPublicIp();
			// Getting Hadoop DFS port value
			int port = getDFSPort(cluster);

			// Construct a DFS URI for given host and port
			String dfsURI = HadoopUtils.HADOOP_URI_PREFIX + host + ":" + port;

			// Construct a Hadoop FileSystem for given hdfs uri and
			// configuration
			FileSystem dfs = FileSystem.get(new URI(dfsURI), conf);

			// Construct a path from given path String
			Path path = new Path(dfsPath);

			// Check whether named path is a regular file.
			boolean isFile = dfs.isFile(path);

			// Fetching given path information
			try {
				String homeDirectory = dfs.getHomeDirectory().toString();
				String parentDirectory = null;

				if (path.getParent() != null) {
					parentDirectory = path.getParent().toString();
				}

				pathDetail.put("HomeDirectory", homeDirectory);
				pathDetail.put("ParentDirectory", parentDirectory);
				pathDetail.put("Depth", path.depth());
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}

			// Fetching file / directory information
			Object pathContent = null;
			if (!isFile) {
				pathDetail.putAll(getPathContent(dfs, path));
				pathContent = getDFSHierarchy(dfs, path);
			} else {
				pathContent = readDFSFileContent(path, dfs);
			}

			dfsFileStatusInfo.put("PathDetail", pathDetail);
			dfsFileStatusInfo.put("PathHierarchy", pathContent);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage());
		}
		return dfsFileStatusInfo;
	}

	/**
	 * Gets the dFS hierarchy.
	 * 
	 * @param dfs
	 *            the dfs
	 * @param path
	 *            the path
	 * @return map
	 */
	private Map<String, List<DFSFileStatusInfo>> getDFSHierarchy(
			FileSystem dfs, Path path) {
		Map<String, List<DFSFileStatusInfo>> fileDirTree = new HashMap<String, List<DFSFileStatusInfo>>();
		List<DFSFileStatusInfo> fileList = new ArrayList<DFSFileStatusInfo>();
		List<DFSFileStatusInfo> dirList = new ArrayList<DFSFileStatusInfo>();

		try {
			// List the status of the files/directories in the given path
			FileStatus[] dfsFileStatus = dfs.listStatus(path);
			if (dfsFileStatus != null) {
				for (int fsCnt = 0; fsCnt < dfsFileStatus.length; fsCnt++) {
					// Constructing object of bean DFSFileStatusInfo
					DFSFileStatusInfo fsStatus = new DFSFileStatusInfo();

					// Getting file status object that represents the path
					FileStatus pathFileStatus = dfsFileStatus[fsCnt];

					// Checking for content type
					String type = DFS_CONTENT_TYPE_DIR;
					boolean fsDir = pathFileStatus.isDir();
					if (!fsDir) {
						type = DFS_CONTENT_TYPE_FILE;
					}

					Path fsPath = pathFileStatus.getPath();
					// Get the access time of the file.
					Date accessTime = new Date(pathFileStatus.getAccessTime());
					// Get the group associated with the file.
					String groupName = pathFileStatus.getGroup();
					// Get the modification time of the file.
					Date modificationTime = new Date(
							pathFileStatus.getModificationTime());
					// Get the owner of the file.
					String owner = pathFileStatus.getOwner();
					// Get FsPermission associated with the file.
					FsPermission fsp = pathFileStatus.getPermission();

					long directoryCount = 0;
					long fileCount = 0;
					try {
						// Return the ContentSummary of a given Path
						ContentSummary content = dfs.getContentSummary(fsPath);
						// Fetching up the total directories count
						directoryCount = content.getDirectoryCount();
						// Fetching up the total files count
						fileCount = content.getFileCount();

					} catch (IOException e1) {
						LOGGER.error(e1.getMessage());
					}

					// File related parameters
					if (type.equals(DFS_CONTENT_TYPE_FILE)) {
						// Get the block size of the file.
						String blockSize = convertBytes(pathFileStatus
								.getBlockSize());
						// Get the length of this file, in blocks
						String fileLength = convertBytes(pathFileStatus
								.getLen());
						fsStatus.setBlockSize(blockSize);
						fsStatus.setFileLength(fileLength);
					}

					// Populating the File Status bean
					fsStatus.setAccessTime(accessTime.toString());
					fsStatus.setModificationTime(modificationTime.toString());
					fsStatus.setCompletePath(fsPath.toString());
					fsStatus.setName(fsPath.getName());
					fsStatus.setDirectoryCount(directoryCount);
					fsStatus.setFileCount(fileCount);
					fsStatus.setOwner(owner);
					fsStatus.setGroupName(groupName);
					fsStatus.setPermission(fsp);
					if (type.equals(DFS_CONTENT_TYPE_FILE)) {
						fileList.add(fsStatus);
					} else {
						dirList.add(fsStatus);
					}
				}
				fileDirTree.put(DFS_CONTENT_TYPE_FILE, fileList);
				fileDirTree.put(DFS_CONTENT_TYPE_DIR, dirList);
			}

		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		return fileDirTree;
	}

	/**
	 * Gets the path content.
	 * 
	 * @param dfs
	 *            the dfs
	 * @param dfsPath
	 *            the dfs path
	 * @return map
	 */
	private Map<String, Object> getPathContent(FileSystem dfs, Path dfsPath) {

		Map<String, Object> pathDetail = new HashMap<String, Object>();

		try {
			// Getting File Status of given path
			FileStatus fileStatus = dfs.getFileStatus(dfsPath);
			// Get the owner of the file.
			pathDetail.put("Owner", fileStatus.getOwner());
			// Get the group associated with the file.
			pathDetail.put("Group", fileStatus.getGroup());
			// Get FsPermission associated with the file.
			pathDetail.put("Permission", fileStatus.getPermission());
			// Get the modification time of the file.
			Date modificationTime = new Date(fileStatus.getModificationTime());
			pathDetail.put("ModificationTime", modificationTime);
			// Get the access time of the file.
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		return pathDetail;
	}

	/**
	 * Gets the dFS port.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return Integer
	 */
	private int getDFSPort(Cluster cluster) {
		int dfsPort = Integer.parseInt(Hadoop1Deployer.DEFAULT_PORT_RPC_NAMENODE);
		// Getting HadoopClusterConf Object from given cluster
		HadoopClusterConf hadoopClusterConf = (HadoopClusterConf) cluster
				.getClusterConf();
		// Getting AuthConf Object from Cluster configuraion
		AuthConf authConf = hadoopClusterConf.getAuthConf();
		String host = hadoopClusterConf.getNameNode().getPublicIp();
		HadoopConf hadoopConf = (HadoopConf) hadoopClusterConf.getComponents()
				.get(Constant.Component.Name.HADOOP);
		if(hadoopConf == null) {
			hadoopConf = (HadoopConf) hadoopClusterConf.getComponents()
					.get(Constant.Component.Name.HADOOP2);
			dfsPort = Integer.parseInt(Hadoop2Deployer.DEFAULT_PORT_RPC_NAMENODE);
		}
		String hadoopConfPath = com.impetus.ankush.common.utils.FileUtils
				.getSeparatorTerminatedPathEntry(hadoopConf.getHadoopConfDir());
		try {
			String dfsPortVal = null;
			String privateKey = null;
			String authenticationInfo = authConf.getPassword();
			if (!authConf.isAuthTypePassword()) {
				privateKey = authConf.getPassword();
				authenticationInfo = null;
			}
			// Establishing connection with master node
			SSHExec hadoopMasterConnection = SSHUtils.connectToNode(host,
					authConf.getUsername(), authenticationInfo, privateKey);

			// if connected
			if (hadoopMasterConnection != null) {
				// Creating Task for fetching configuration parameter value
				AnkushTask rdp = new ReadConfProperty("fs.default.name",
						hadoopConfPath + "core-site.xml",Constant.File_Extension.XML);
				// Executing task for fetching configuration parameter value
				Result zkValues = hadoopMasterConnection.exec(rdp);
				String portVal = zkValues.sysout;
				// Getting port value for result
				if (portVal != null && portVal.trim().length() > 0) {
					dfsPortVal = portVal.trim();
				}
			}
			// String manipulation to extract port value from complete result
			// string
			if (dfsPortVal != null) {
				dfsPortVal = dfsPortVal.split(":")[2];
				dfsPort = Integer.parseInt(dfsPortVal);
			}

		} catch (Exception e) {
			LOGGER.error("EXCEPTION : " + this.getClass().getSimpleName()
					+ " :  - possible reason : " + e.getMessage());
		}
		return dfsPort;
	}

	/**
	 * Convert bytes.
	 * 
	 * @param bytes
	 *            the bytes
	 * @return String
	 */
	private static String convertBytes(long bytes) {
		String convertedVal = "0";
		if (bytes == 0) {
			return convertedVal;
		}

		final double HUNDRED = 100.0;
		final long KILOBYTE = 1024L;
		final long MEGABYTE = 1024L * 1024L;
		final long GIGABYTE = 1024L * 1024L * 1024L;
		DecimalFormat df = new DecimalFormat("0.00");

		if (bytes / GIGABYTE > 0) {
			convertedVal = df.format(((bytes * HUNDRED / GIGABYTE)) / HUNDRED)
					+ "GB";
		} else if (bytes / MEGABYTE > 0) {
			convertedVal = df.format(((bytes * HUNDRED / MEGABYTE)) / HUNDRED)
					+ "MB";
		} else if (bytes / KILOBYTE >= 0) {
			convertedVal = df.format(((bytes * HUNDRED / KILOBYTE)) / HUNDRED)
					+ "KB";
		} 
		return convertedVal;
	}
	
	/**
	 * Gets the dfs jmx bean data.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @param beanName the bean name
	 * @return the dfs jmx bean data
	 */
	private static Map<String, Object> getDfsJmxBeanData(String nodeIp, int clientPort, String beanName) {
		Map<String, Object> beanObject = new HashMap<String, Object>();
		String urlDfsJmxBean = "";
		if(beanName != null && (!beanName.isEmpty())) {
			urlDfsJmxBean = getJmxBeanUrl(nodeIp, clientPort, beanName);
		} else {
			// Return Error : Invalid Bean Name
		}
		
		JSONObject jmxDataJson =  getBeanJsonForDfsJmxData(urlDfsJmxBean);
		if(jmxDataJson != null) {
			List<JSONObject> beanObjList = (List<JSONObject>) jmxDataJson.get(HadoopDFSManager.JMX_DATA_KEY_BEANS);
			for(JSONObject beanObj : beanObjList) {
				if(beanObj.get(HadoopDFSManager.JMX_DATA_KEY_BEAN_NAME).equals(beanName)) {
					beanObject = (Map<String, Object>) beanObj;
					break;
				}	
			}
		}
		return beanObject;
	}
	
	/**
	 * Gets the jmx bean url.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @param beanName the bean name
	 * @return the jmx bean url
	 */
	private static String getJmxBeanUrl(String nodeIp, int clientPort, String beanName) {
			return ("http://" + nodeIp + ":" + clientPort + "/jmx?qry=" + beanName);	
	}

	/**
	 * Gets the bean json for dfs jmx data.
	 *
	 * @param urlDfsJmxBean the url dfs jmx bean
	 * @return the bean json for dfs jmx data
	 */
	private static JSONObject getBeanJsonForDfsJmxData(String urlDfsJmxBean) {
		String errMsg = "Exception: Unalble to get DFS data using url : " + urlDfsJmxBean;
		JSONObject json = null;
		try {
			AnkushRestClient restClient = new AnkushRestClient();
			String data = restClient.getRequest(urlDfsJmxBean);
			if(data == null) {
				LOGGER.error(errMsg);	
			} else {
				json = (JSONObject) new JSONParser().parse(data);
			}
		} catch (Exception e) {
			LOGGER.error(errMsg);
			LOGGER.error(e.getMessage());
		}
		return json;
	}
	
	/**
	 * Gets the dfs cluster data map.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @param isHadoop2 the is hadoop2
	 * @return the dfs cluster data map
	 */
	public static Map<String, String> getDfsClusterDataMap(String nodeIp, int clientPort, boolean isHadoop2, boolean isCdh3)
	{
		Map<String, String> dfsClusterDataMap = null;
		dfsClusterDataMap = HadoopDFSManager.getDfsClusterCommonData(nodeIp, clientPort, isCdh3);
		return dfsClusterDataMap;
	}
	
	/**
	 * Gets the name node start time as map.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @return the name node start time as map
	 */
	private static Map<String, String> getNameNodeStartTimeAsMap(String nodeIp, int clientPort) {
		String startTime = HadoopDFSManager.getNameNodeStartTime(nodeIp, clientPort);
		Map<String, String> startTimeMap = new HashMap<String, String>();
		startTimeMap.put(Constant.Hadoop.Keys.NameNodeJmxKeyDisplayName.getKeyDisplayName(Constant.Hadoop.Keys.NameNodeJmxInfo.STARTTIME), startTime);
		return startTimeMap;
	}
	
	/**
	 * Gets the name node start time.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @return the name node start time
	 */
	private static String getNameNodeStartTime(String nodeIp, int clientPort) {
		Map<String, Object> beanJavaRuntime = new HashMap<String, Object>();
		String beanName =  HadoopDFSManager.JMX_BEAN_NAME_JAVA_RUNTIME;
		beanJavaRuntime = HadoopDFSManager.getDfsJmxBeanData(nodeIp, clientPort, beanName);
		long startTime = ((Number)beanJavaRuntime.get(Constant.Hadoop.Keys.NameNodeJmxInfo.STARTTIME)).longValue();
		return HadoopUtils.getGmtFromTimeInMillis(startTime);
	}
	
	/**
	 * Gets the dfs cluster common data.
	 *
	 * @param nodeIp the node ip
	 * @param clientPort the client port
	 * @return the dfs cluster common data
	 */
	private static Map<String, String> getDfsClusterCommonData(String nodeIp, int clientPort, boolean isCdh3)
	{
		Map<String, String> dfsCommonData = null;
		String beanName = HadoopDFSManager.JMX_BEAN_NAME_NAMENODEINFO;
		if(isCdh3) {
			beanName = HadoopDFSManager.JMX_BEAN_NAME_NAMENODEINFO_CHD3;
		}
		
		Map<String, Object> nameNodeInfoBeanObject = HadoopDFSManager.getDfsJmxBeanData(nodeIp, clientPort, beanName);
		if(nameNodeInfoBeanObject != null) {
			dfsCommonData = HadoopDFSManager.getDfsUsageData(nameNodeInfoBeanObject);
		}
		if(dfsCommonData != null) {
			dfsCommonData.putAll(HadoopDFSManager.getNameNodeStartTimeAsMap(nodeIp, clientPort));	
		}
		return dfsCommonData;
	}
	
	public static String getLiveDataNodesCount(HadoopConf hConf) {
		String nodeIp = hConf.getNamenode().getPublicIp();
		int clientPort = Integer.parseInt(Hadoop1Deployer.DEFAULT_PORT_HTTP_NAMENODE);
		String valLiveDataNodesCount = null;
		String errMsgLiveDataNodesCount = "Could not fetch Live DataNodes count."; 
		
		if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.AGENT)) {
			valLiveDataNodesCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
			LOGGER.error("Agent Down on " + nodeIp + ": " + errMsgLiveDataNodesCount);
		} else if(!HadoopUtils.getServiceStatusForNode(nodeIp, Constant.Role.NAMENODE)) {
			valLiveDataNodesCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
			LOGGER.error(Constant.Role.NAMENODE + " Down on " + nodeIp + ": " + errMsgLiveDataNodesCount);
		} else {
			valLiveDataNodesCount = HadoopDFSManager.getLiveDataNodesCount(nodeIp, clientPort, hConf.isCdh3());
			if(valLiveDataNodesCount == null) {
				valLiveDataNodesCount = HadoopTileViewHandler.STRING_EMPTY_VALUE;			
				LOGGER.error("Error: " + errMsgLiveDataNodesCount);	
			}
		}
		return valLiveDataNodesCount;
	}
	
	private static String getLiveDataNodesCount(String nodeIp, int clientPort, boolean isCdh3)
	{
		String beanName = HadoopDFSManager.JMX_BEAN_NAME_NAMENODEINFO;
		if(isCdh3) {
			beanName = HadoopDFSManager.JMX_BEAN_NAME_NAMENODEINFO_CHD3;
		}
		try {
			long waitTime = AppStoreWrapper.getAnkushConfReader().getLongValue(
					"hadoop.jmxmonitoring.wait.time");
			CallableJmxBeanData callableJmxBeanData = new CallableJmxBeanData(nodeIp, clientPort, beanName);
			FutureTask<Map<String, Object>> futureTaskJmxBeanData = new FutureTask<Map<String, Object>>(callableJmxBeanData);
			
			AppStoreWrapper.getExecutor().execute(futureTaskJmxBeanData);
		    
		    Map<String, Object> namenodeInfoBeanObject = futureTaskJmxBeanData.get(waitTime, TimeUnit.MILLISECONDS);
		    
			if(namenodeInfoBeanObject != null) {
				String strActiveNodesInfo = String.valueOf(namenodeInfoBeanObject.get("LiveNodes"));
				JSONObject jsonArrayActiveNodeInfo = JsonMapperUtil.objectFromString(strActiveNodesInfo, JSONObject.class);
				if(jsonArrayActiveNodeInfo != null) {
					return String.valueOf(jsonArrayActiveNodeInfo.size());	
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception: " + e.getMessage());
		}
		return null;
	}
	
	/**
	 * Convert to percentage.
	 *
	 * @param value the value
	 * @return the string
	 */
	private static String convertToPercentage(double value) {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		return (df.format(value) + "%");
	}
	
	/**
	 * Gets the dfs usage data in percent.
	 *
	 * @param nameNodeInfoBeanObject the name node info bean object
	 * @return the dfs usage data in percent
	 */
	private static Map<String, String> getDfsUsageDataInPercent(Map<String, Object> nameNodeInfoBeanObject) {
		Map<String, String> dfsUsageData = new HashMap<String, String>();
		
		List<String> dfsUsageKeys = new ArrayList<String>();
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.PERCENTUSED);
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.PERCENTREMAINING);
		
		if(nameNodeInfoBeanObject != null) {
			for(String dfsUsagekey : dfsUsageKeys) {
				String key = Constant.Hadoop.Keys.NameNodeJmxKeyDisplayName.getKeyDisplayName(dfsUsagekey);
				String value = HadoopDFSManager.convertToPercentage(((Number)nameNodeInfoBeanObject.get(dfsUsagekey)).doubleValue());
				dfsUsageData.put(key, value);	
			}
		}
		return dfsUsageData;
	}
	
	private static Map<String, String> getDfsUsageData(Map<String, Object> nameNodeInfoBeanObject) {
		Map<String, String> dfsUsageData = new LinkedHashMap<String, String>();
		String key = Constant.Hadoop.Keys.NameNodeJmxKeyDisplayName
				.getKeyDisplayName(Constant.Hadoop.Keys.NameNodeJmxInfo.USED);
		String valueBytes = HadoopDFSManager.convertBytes
				(((Number)nameNodeInfoBeanObject.get
					(Constant.Hadoop.Keys.NameNodeJmxInfo.USED)).longValue());
		String valuePercent = HadoopDFSManager.convertToPercentage
		(((Number)nameNodeInfoBeanObject.get
			(Constant.Hadoop.Keys.NameNodeJmxInfo.PERCENTUSED)).doubleValue());

		String value = valueBytes + "  " + valuePercent;
		dfsUsageData.put(key, value);
		
		key = Constant.Hadoop.Keys.NameNodeJmxKeyDisplayName
						.getKeyDisplayName(Constant.Hadoop.Keys.NameNodeJmxInfo.FREE);
		valueBytes = HadoopDFSManager.convertBytes
						(((Number)nameNodeInfoBeanObject.get
							(Constant.Hadoop.Keys.NameNodeJmxInfo.FREE)).longValue());
		valuePercent = HadoopDFSManager.convertToPercentage
				(((Number)nameNodeInfoBeanObject.get
					(Constant.Hadoop.Keys.NameNodeJmxInfo.PERCENTREMAINING)).doubleValue());
		
		value = valueBytes + "  " + valuePercent; 
		dfsUsageData.put(key, value);
		
		List<String> dfsUsageKeys = new ArrayList<String>();
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.NONDFSUSEDSPACE);
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.TOTAL);
		
		if(nameNodeInfoBeanObject != null) {
			for(String dfsUsagekey : dfsUsageKeys) {
				key = Constant.Hadoop.Keys.NameNodeJmxKeyDisplayName.getKeyDisplayName(dfsUsagekey);
				value = HadoopDFSManager.convertBytes(((Number)nameNodeInfoBeanObject.get(dfsUsagekey)).longValue());
				dfsUsageData.put(key, value);
			}
		}
		return dfsUsageData;
	}
	
	/**
	 * Gets the dfs usage data in bytes.
	 *
	 * @param nameNodeInfoBeanObject the name node info bean object
	 * @return the dfs usage data in bytes
	 */
	private static Map<String, String> getDfsUsageDataInBytes(Map<String, Object> nameNodeInfoBeanObject) {
		Map<String, String> dfsUsageData = new HashMap<String, String>();
		
		List<String> dfsUsageKeys = new ArrayList<String>();
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.TOTAL);
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.NONDFSUSEDSPACE);
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.FREE);
		dfsUsageKeys.add(Constant.Hadoop.Keys.NameNodeJmxInfo.USED);
		
		if(nameNodeInfoBeanObject != null) {
			for(String dfsUsagekey : dfsUsageKeys) {
				String key = Constant.Hadoop.Keys.NameNodeJmxKeyDisplayName.getKeyDisplayName(dfsUsagekey);
				String value = HadoopDFSManager.convertBytes(((Number)nameNodeInfoBeanObject.get(dfsUsagekey)).longValue());
				dfsUsageData.put(key, value);
			}
		}
		
		return dfsUsageData;
	}
}
