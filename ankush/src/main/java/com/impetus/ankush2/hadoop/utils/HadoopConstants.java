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
package com.impetus.ankush2.hadoop.utils;

import java.util.HashMap;
import java.util.Map;

import com.impetus.ankush2.constant.Constant;

// TODO: Auto-generated Javadoc
/**
 * The Interface HadoopConstants.
 * 
 * @author Akhil
 */
public interface HadoopConstants {

	/** The Constant URI File Prefix for HDFS . */
	String URI_FILE_PREFIX = "file://";

	String HADOOP_VERSION = "hadoopVersion";

	String ALLOW_JOB_SUBMISSION_KEY = "AllowJobSubmission";

	String ALLOW_SERVICE_MANAGEMENT_KEY = "AllowServiceManagement";

	enum RegisterLevel {
		LEVEL1, LEVEL2, LEVEL3
	}

	interface TaskableClass {

		/** The Hadoop Job Monitoring class for Hadoop 1. */
		String JOB_STATUS_MONITOR = "com.impetus.ankush.agent.hadoop.JobStatusMonitor";

		/**
		 * The Hadoop NameNode Role Update class for Hadoop 2 with HA enabled.
		 */
		String HA_UPDATE_NAMENODE_ROLE = "com.impetus.ankush.agent.hadoop.UpdateNameNodeRole";

	}

	/**
	 * The Interface AdvanceConfKeys.
	 */
	interface AdvanceConfKeys {

		/**
		 * The Interface RpcPort.
		 */
		interface RpcPort {

			/** The namenode. */
			String NAMENODE = "rpcPortNameNode";

			/** The jobtracker. */
			String JOBTRACKER = "rpcPortJobTracker";

			/** The resourcemanager. */
			String RESOURCEMANAGER = "rpcPortResourceManager";
		}

		/**
		 * The Interface HttpPort.
		 */
		interface HttpPort {

			String WEBAPP_PROXY_SERVER = "rpcPortWebAppProxyServer";

			/** The namenode. */
			String NAMENODE = "httpPortNameNode";

			/** The jobtracker. */
			String JOBTRACKER = "httpPortJobTracker";

			/** The resourcemanager. */
			String RESOURCEMANAGER = "httpPortResourceManager";
		}

		String IS_HADOOP2 = "isHadoop2";

		/** The namenode. */
		String NAMENODE = "namenode";

		/** The slaves. */
		String SLAVES = "slaves";

		/** The jobtracker. */
		String JOBTRACKER = "jobTracker";

		/** The secondary namenode. */
		String SECONDARY_NAMENODE = "secondaryNamenode";

		/** The resource manager. */
		String RESOURCE_MANAGER = "resourceManager";

		/** The web app proxy server. */
		String WEB_APP_PROXY_SERVER = "webAppProxyServer";

		/** The job history server. */
		String JOB_HISTORY_SERVER = "jobHistoryServer";

		/** The ha journalnodes. */
		String HA_JOURNALNODES = "journalNodes";

		/** The installation type. */
		String INSTALLATION_TYPE = "installationType";

		/** The dfs replication factor. */
		String DFS_REPLICATION_FACTOR = "dfsReplicationFactor";

		/** The dfs data dir. */
		String DFS_DATA_DIR = "dfsDataDir";

		/** The dfs name dir. */
		String DFS_NAME_DIR = "dfsNameDir";

		/** The tmp dir hadoop. */
		String TMP_DIR_HADOOP = "hadoopTmpDir";

		/** The tmp dir mapred. */
		String TMP_DIR_MAPRED = "mapRedTmpDir";

		/** The dir conf. */
		String DIR_CONF = "confDir";

		/** The dir bin. */
		String DIR_BIN = "binDir";

		/** The dir script. */
		String DIR_SCRIPT = "scriptDir";

		/** The ha enabled. */
		String HA_ENABLED = "haEnabled";

		/** The ha automatic failover enabled. */
		String HA_AUTOMATIC_FAILOVER_ENABLED = "automaticFailoverEnabled";

		/** The ha zk ensembleid. */
		String HA_ZK_ENSEMBLEID = "ensembleId";

		/** The jobhistoryserver enabled. */
		String JOBHISTORYSERVER_ENABLED = "jobHistoryServerEnabled";

		/** The webappproxyserver enabled. */
		String WEBAPPPROXYSERVER_ENABLED = "webAppProxyServerEnabled";

		/** The ha nameserviceid. */
		String HA_NAMESERVICEID = "nameserviceId";

		/** The H a_ namenodei d1. */
		String HA_NAMENODEID1 = "nameNodeId1";

		/** The H a_ namenodei d2. */
		String HA_NAMENODEID2 = "nameNodeId2";

		/** The ha active namenode. */
		String HA_ACTIVE_NAMENODE = "activeNamenode";

		String HA_NAMENODE_HOSTS = "haNameNodeHosts";

		/** The ha standby namenode. */
		String HA_STANDBY_NAMENODE = "standByNamenode";

		/** The ha journalnode edits dir. */
		String HA_JOURNALNODE_EDITS_DIR = "journalNodeEditsDir";

		/** The mapreduce framework. */
		String MAPREDUCE_FRAMEWORK = "mapreduceFramework";

		String REGISTER_LEVEL = "registerLevel";
	}

	/**
	 * The Interface ConfigXmlKeys.
	 */
	interface ConfigXmlKeys {

		/**
		 * The Interface ClassType.
		 */
		interface ClassType {

			/** The installer. */
			String INSTALLER = "installer";

			/** The configurator. */
			String CONFIGURATOR = "configurator";

			/** The service manager. */
			String SERVICE_MANAGER = "servicemanager";

			/** The commands manager. */
			String COMMANDS_MANAGER = "commandsmanager";

			/** The monitor. */
			String MONITOR = "monitor";
		}

		/**
		 * The Interface Attributes.
		 */
		interface Attributes {

			/** The installation type. */
			String INSTALLATION_TYPE = "installationType";
			/** The vendor. */
			String VENDOR = "vendor";

			/** The version. */
			String VERSION = "version";
		}

		/** The class. */
		String CLASS = "class";

	}

	/**
	 * The Interface MapReduceFramework.
	 */
	interface MapReduceFramework {

		/** The yarn. */
		String YARN = "yarn";

		/** The classic. */
		String CLASSIC = "classic";

		/** The local. */
		String LOCAL = "local";
	}

	/**
	 * The Enum SyncConfigurationAction.
	 */
	enum SyncConfigurationAction {

		/** The add. */
		ADD,

		/** The remove. */
		REMOVE
	};

	/**
	 * The Interface RegisterHadoop.
	 */
	interface RegisterHadoop {

		/**
		 * The Interface JsonKeys.
		 */
		interface JsonKeys {

			/** The hadoop node conf namenode. */
			String HADOOP_NODE_CONF_NAMENODE = "namenode";

			/** The hadoop node conf rmnode. */
			String HADOOP_NODE_CONF_RMNODE = "resourceManagerNode";

			/** The hadoop node conf slaves. */
			String HADOOP_NODE_CONF_SLAVES = "slaves";

			/** The hadoop start job history server. */
			String HADOOP_START_JOB_HISTORY_SERVER = "startJobHistoryServer";

			/** The hadoop node conf secondary namenode. */
			String HADOOP_NODE_CONF_SECONDARY_NAMENODE = "secondaryNamenode";

			/** The http port namenode. */
			String HTTP_PORT_NAMENODE = "httpPortNameNode";

			/** The component home. */
			String COMPONENT_HOME = "componentHome";

			/** The version. */
			String VERSION = "version";

			/** The vendor. */
			String VENDOR = "vendor";

		}
	}

	/**
	 * The Interface Hadoop.
	 */
	interface Hadoop {

		/**
		 * The Interface PropertyName.
		 */
		interface PropertyName {

			/**
			 * The Interface SiteXmlHdfs.
			 */
			interface SiteXmlHdfs {

				/** The dfs replication. */
				String DFS_REPLICATION = "dfs.replication";

				/** The dfs name dir. */
				String DFS_NAME_DIR = "dfs.name.dir";

				/** The dfs data dir. */
				String DFS_DATA_DIR = "dfs.data.dir";

			}
		}

		/**
		 * The Interface Keys.
		 */
		interface Keys {

			interface NodesSummary {

				String COUNT_ACTIVE_TASKTRACKERS = "Active TaskTrackers";

				String COUNT_ACTIVE_NODEMANAGERS = "Active NodeManagers";

				String COUNT_LIVE_DATANODES = "Live DataNodes";

				String NAMENODE_ACTIVE = "Active NameNode";

				String NAMENODE_STANDBY = "StandBy NameNode";

				String NAMENODE_ID1 = "NameNode ID 1";

				String NAMENODE_ID2 = "NameNode ID 2";
			}

			String JOB_ID = "jobid";

			String QUERY_PARAMETER_PROCESS = "process";

			/** The monitoring table data key. */
			String MONITORING_TABLE_DATA_KEY = "data";

			String MONITORING_JOBS_DATA_KEY = "jobs";

			String MONITORING_JOBS_METRICS_DATA_KEY = "metrics";

			/** The jmx data key beans. */
			String JMX_DATA_KEY_BEANS = "beans";

			/** The input arg hadoop log dir. */
			String INPUT_ARG_HADOOP_LOG_DIR = "hadoop.log.dir";

			/** The input arg hadoop home dir. */
			String INPUT_ARG_HADOOP_HOME_DIR = "hadoop.home.dir";

			/** The jmx data key bean name. */
			String JMX_DATA_KEY_BEAN_NAME = "name";

			/** The jmx data key heap memory usage. */
			String JMX_DATA_KEY_HEAP_MEMORY_USAGE = "HeapMemoryUsage";

			String JMX_DATA_KEY_MEMORY_USAGE_INIT = "init";

			String JMX_DATA_KEY_MEMORY_USAGE_MAX = "max";

			String JMX_DATA_KEY_MEMORY_USAGE_USED = "used";

			String JMX_DATA_KEY_MEMORY_USAGE_COMMITTED = "committed";

			/** The jmx data key non heap memory usage. */
			String JMX_DATA_KEY_NON_HEAP_MEMORY_USAGE = "NonHeapMemoryUsage";

			/** The jmx java runtime input arguments key. */
			String JMX_JAVA_RUNTIME_INPUT_ARGUMENTS_KEY = "InputArguments";

			/** The display name heap memory used. */
			String DISPLAY_NAME_HEAP_MEMORY_USED = "Heap Memory Used";

			/** The display name non heap memory used. */
			String DISPLAY_NAME_NON_HEAP_MEMORY_USED = "NonHeap Memory Used";

			/** The display name heap memory used. */
			String DISPLAY_NAME_HEAP_MEMORY_COMMITTED = "Heap Memory Committed";

			/** The display name non heap memory used. */
			String DISPLAY_NAME_NON_HEAP_MEMORY_COMMITTED = "NonHeap Memory Committed";

			String DISPLAY_NAME_PROCESS_STARTED = "Started";

			/** The display name thread count. */
			String DISPLAY_NAME_THREAD_COUNT = "Thread Count";

			/** The jmx data key thread count. */
			String JMX_DATA_KEY_THREAD_COUNT = "ThreadCount";

			/**
			 * The Interface NameNodeJmxInfo.
			 */
			interface NameNodeJmxInfo {

				/** The total. */
				String TOTAL = "Total";

				/** The used. */
				String USED = "Used";

				/** The nondfsusedspace. */
				String NONDFSUSEDSPACE = "NonDfsUsedSpace";

				/** The free. */
				String FREE = "Free";

				/** The percentused. */
				String PERCENTUSED = "PercentUsed";

				/** The percentremaining. */
				String PERCENTREMAINING = "PercentRemaining";

				/** The totalfiles. */
				String TOTALFILES = "TotalFiles";

				/** The totalblocks. */
				String TOTALBLOCKS = "TotalBlocks";

				/** The starttime. */
				String STARTTIME = "StartTime";

				/** The namenodeui. */
				String NAMENODEUI = "NameNode UI";
			}

			/**
			 * The Interface JobTrackerJmxInfo.
			 */
			interface JobTrackerJmxInfo {

				/** The slots total map. */
				String SLOTS_TOTAL_MAP = "map_slots";

				/** The slots total reduce. */
				String SLOTS_TOTAL_REDUCE = "reduce_slots";

				/** The jobs running. */
				String JOBS_RUNNING = "jobs_running";

				/** The jobs submitted. */
				String JOBS_SUBMITTED = "jobs_submitted";

				/** The jobs completed. */
				String JOBS_COMPLETED = "jobs_completed";

				/** The trackers total. */
				String TRACKERS_TOTAL = "trackers";

				/** The trackers blacklisted. */
				String TRACKERS_BLACKLISTED = "trackers_blacklisted";

				/** The trackers decommissioned. */
				String TRACKERS_DECOMMISSIONED = "trackers_decommissioned";

				/** The starttime. */
				String STARTTIME = "StartTime";

				/** The jobtrackerui. */
				String JOBTRACKERUI = "JobTracker UI";
			}

			/**
			 * The Interface DataNodeJmxInfo.
			 */
			interface DataNodeJmxInfo {

				/** The remaining. */
				String REMAINING = "Remaining";

				/** The capacity. */
				String CAPACITY = "Capacity";

				/** The dfsused. */
				String DFSUSED = "DfsUsed";
			}

			/**
			 * The Interface TaskTrackerJmxInfo.
			 */
			interface TaskTrackerJmxInfo {

				/** The maps running. */
				String MAPS_RUNNING = "maps_running";

				/** The reduces running. */
				String REDUCES_RUNNING = "reduces_running";

				/** The tasks completed. */
				String TASKS_COMPLETED = "tasks_completed";

				/** The map task slots. */
				String MAP_TASK_SLOTS = "mapTaskSlots";

				/** The reduce task slots. */
				String REDUCE_TASK_SLOTS = "reduceTaskSlots";
			}

			/**
			 * The Interface ResourceManagerJmxInfo.
			 */
			interface ResourceManagerJmxInfo {

				/** The total. */
				String TOTAL = "Total";

				/** The used. */
				String USED = "Used";

				/** The nondfsusedspace. */
				String NONDFSUSEDSPACE = "NonDfsUsedSpace";

				/** The free. */
				String FREE = "Free";

				/** The percentused. */
				String PERCENTUSED = "PercentUsed";

				/** The percentremaining. */
				String PERCENTREMAINING = "PercentRemaining";

				/** The totalfiles. */
				String TOTALFILES = "TotalFiles";

				/** The totalblocks. */
				String TOTALBLOCKS = "TotalBlocks";

				/** The starttime. */
				String STARTTIME = "StartTime";
			}

			/**
			 * Implementation to get the display name for a given key.
			 * 
			 * @author Akhil
			 * 
			 */
			class JobTrackerJmxKeyDisplayName {

				/** The MapRed Key-Display map. */
				static Map<String, String> KEY_DISPLAY_NAME_MAP = new HashMap<String, String>();

				static {
					Map<String, String> keyDisplayNameMap = new HashMap<String, String>();
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_MAP,
									"Total Map Slots");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_REDUCE,
									"Total Reduce Slots");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.JOBS_RUNNING,
									"Running Jobs");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.JOBS_SUBMITTED,
									"Submiited Jobs");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.JOBS_COMPLETED,
									"Completed Jobs");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_TOTAL,
									"Total TaskTrackers");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_BLACKLISTED,
									"Blacklisted TaskTrackers");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_DECOMMISSIONED,
									"Decommissioned TaskTrackers");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.STARTTIME,
									"JobTracker Started");

					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.JobTrackerJmxInfo.JOBTRACKERUI,
									"JobTracker UI");

					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

				/**
				 * Gets the key display name.
				 * 
				 * @param key
				 *            the key
				 * @return the key display name
				 */
				public static String getKeyDisplayName(String key) {
					return KEY_DISPLAY_NAME_MAP.get(key);
				}
			}

			/**
			 * Implementation to get the display name for a given key.
			 * 
			 * @author Akhil
			 * 
			 */
			class TaskTrackerJmxKeyDisplayName {

				/** The DFS Key-Display map. */
				static Map<String, String> KEY_DISPLAY_NAME_MAP = new HashMap<String, String>();

				static {
					Map<String, String> keyDisplayNameMap = new HashMap<String, String>();
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.TaskTrackerJmxInfo.MAPS_RUNNING,
									"Maps Running");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.TaskTrackerJmxInfo.REDUCES_RUNNING,
									"Reduces Running");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.TaskTrackerJmxInfo.REDUCE_TASK_SLOTS,
									"Reduce Task Slots");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.TaskTrackerJmxInfo.MAP_TASK_SLOTS,
									"Map Task Slots");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.TaskTrackerJmxInfo.TASKS_COMPLETED,
									"Tasks Completed");
					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

				/**
				 * Gets the key display name.
				 * 
				 * @param key
				 *            the key
				 * @return the key display name
				 */
				public static String getKeyDisplayName(String key) {
					return KEY_DISPLAY_NAME_MAP.get(key);
				}
			}

			/**
			 * Implementation to get the display name for a given key.
			 * 
			 * @author Akhil
			 * 
			 */
			class NameNodeJmxKeyDisplayName {

				/** The DFS Key-Display map. */
				static Map<String, String> KEY_DISPLAY_NAME_MAP = new HashMap<String, String>();

				static {
					Map<String, String> keyDisplayNameMap = new HashMap<String, String>();
					keyDisplayNameMap.put(
							HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.TOTAL,
							"Configured Capacity");
					keyDisplayNameMap.put(
							HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.USED,
							"DFS Used");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.NONDFSUSEDSPACE,
									"Non-Dfs Used");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.TOTALFILES,
									"Total Files");
					keyDisplayNameMap.put(
							HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.FREE,
							"DFS Remaining");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.PERCENTUSED,
									"DFS Used %");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.PERCENTREMAINING,
									"DFS Remaining %");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.TOTALBLOCKS,
									"Total Blocks");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.STARTTIME,
									"NameNode Started");

					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.NameNodeJmxInfo.NAMENODEUI,
									"NameNode UI");

					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

				/**
				 * Gets the key display name.
				 * 
				 * @param key
				 *            the key
				 * @return the key display name
				 */
				public static String getKeyDisplayName(String key) {
					return KEY_DISPLAY_NAME_MAP.get(key);
				}
			}

			/**
			 * Implementation to get the display name for a given key.
			 * 
			 * @author Akhil
			 * 
			 */
			class DataNodeJmxKeyDisplayName {

				/** The DFS Key-Display map. */
				static Map<String, String> KEY_DISPLAY_NAME_MAP = new HashMap<String, String>();

				static {
					Map<String, String> keyDisplayNameMap = new HashMap<String, String>();
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.DataNodeJmxInfo.CAPACITY,
									"Configured Capacity");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.DataNodeJmxInfo.DFSUSED,
									"DFS Used");
					keyDisplayNameMap
							.put(HadoopConstants.Hadoop.Keys.DataNodeJmxInfo.REMAINING,
									"Remaining Capacity");
					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

				/**
				 * Gets the key display name.
				 * 
				 * @param key
				 *            the key
				 * @return the key display name
				 */
				public static String getKeyDisplayName(String key) {
					return KEY_DISPLAY_NAME_MAP.get(key);
				}
			}

			/**
			 * The Interface YARN.
			 */
			interface YARN {

				/** The app id. */
				String APPID = "appid";

				/** The appattempts. */
				String APPATTEMPTS = "appAttempts";

				/** The appattempt. */
				String APPATTEMPT = "appAttempt";

				/** The Hadoop 2 App List. */
				String JSON_HADOOP2APPLIST = "hadoop2applist";

				/** The apps. */
				String APPS = "apps";

				/** The app. */
				String APP = "app";

				/** The job. */
				String JOB = "job";

				/** The Hadoop 2 App Details. */
				String JSON_APPDETAILS = "appdetails";

				/** The Hadoop 2 App Details. */
				String JSON_APPATTEMPTS = "appattempts";

				/** The Hadoop 2 job info. */
				String JSON_JOBINFO = "jobinfo";

				/** The Hadoop 2 Application Type. */
				String APPLICATIONTYPE = "applicationType";

				/** The Hadoop 2 Application Type MAPREDUCE. */

				String APPTYPE_MAPRED = "MAPREDUCE";

				/** The Hadoop 2 Application tracking ui Application Master. */
				String APPLICATION_TRACKINGUI_APPLICATIONMASTER = "ApplicationMaster";

				/** The Hadoop 2 Application tracking ui History. */
				String APPLICATION_TRACKINGUI_HISTORY = "History";

				/** The Hadoop 2 Application Type YARN. */
				String APPTYPE_YARN = "YARN";

				/** The Hadoop 2 Application Tracking UI. */
				String TRACKINGUI = "trackingUI";

				/** The Hadoop 2 Tracking UI History. */
				String TRACKINGUI_HISTORY = "History";

				/** The Hadoop 2 Tracking UI Application Master. */
				String TRACKINGUI_APPMASTER = "ApplicationMaster";

			}
		}

		/** The nnrole info key. */
		String NNROLE_INFO_KEY = "nnrole";

		/**
		 * The Enum NNROLE_VALUES.
		 */
		static enum NNROLE_VALUES {

			/** The active. */
			ACTIVE,

			/** The standby. */
			STANDBY,

			/** The null. */
			NULL
		}

		/**
		 * The Enum ProcessList.
		 */
		static enum ProcessList {

			/** The namenode. */
			NAMENODE,
			/** The datanode. */
			DATANODE,
			/** The jobtracker. */
			JOBTRACKER,
			/** The tasktracker. */
			TASKTRACKER,
			/** The secondarynamenode. */
			SECONDARYNAMENODE,
			/** The resourcemanager. */
			RESOURCEMANAGER,
			/** The nodemanager. */
			NODEMANAGER,
			/** The journalnode. */
			JOURNALNODE,
			/** The webappproxyserver. */
			WEBAPPPROXYSERVER,
			/** The jobhistoryserver. */
			JOBHISTORYSERVER,
			/** The dfszkfailovercontroller. */
			DFSZKFAILOVERCONTROLLER,
			/** The hmaster. */
			HMASTER,
			/** The hregionserver. */
			HREGIONSERVER,
			/** The quorumpeermain. */
			QUORUMPEERMAIN,
			/** The jar. */
			JAR,
			/** The bootstrap. */
			BOOTSTRAP,
			/** The gmond. */
			GMOND,
			/** The gmetad. */
			GMETAD,
			/** The agent. */
			AGENT;
		}
	}

	/**
	 * The Interface Roles.
	 */
	interface Roles {

		/** The namenode. */
		String NAMENODE = "NameNode";

		/** The resourcemanager. */
		String RESOURCEMANAGER = "ResourceManager";

		/** The webappproxyserver. */
		String WEBAPPPROXYSERVER = "WebAppProxyServer";

		/** The nodemanager. */
		String NODEMANAGER = "NodeManager";

		/** The datanode. */
		String DATANODE = "DataNode";

		/** The tasktracker. */
		String TASKTRACKER = "TaskTracker";

		/** The jobtracker. */
		String JOBTRACKER = "JobTracker";

		/** The secondarynamenode. */
		String SECONDARYNAMENODE = "SecondaryNameNode";

		/** The jobhistoryserver. */
		String JOBHISTORYSERVER = "JobHistoryServer";

		/** The journalnode. */
		String JOURNALNODE = "JournalNode";

		/** The dfszkfailovercontroller. */
		String DFSZKFAILOVERCONTROLLER = "zkfc";
	}

	/**
	 * The Class JavaProcessScriptFile.
	 */
	class JavaProcessScriptFile {

		/** The javaprocessscriptfile. */
		static Map<String, String> JAVAPROCESSSCRIPTFILE = new HashMap<String, String>();

		static {
			Map<String, String> procesFileMap = new HashMap<String, String>();
			procesFileMap.put(Constant.Role.NAMENODE.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_HADOOP);
			procesFileMap.put(Constant.Role.DATANODE.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_HADOOP);
			procesFileMap.put(Constant.Role.JOBTRACKER.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_HADOOP);
			procesFileMap.put(Constant.Role.TASKTRACKER.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_HADOOP);
			procesFileMap.put(Constant.Role.RESOURCEMANAGER.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_YARN);
			procesFileMap.put(Constant.Role.WEBAPPPROXYSERVER.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_YARN);

			procesFileMap.put(Constant.Role.NODEMANAGER.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_YARN);
			procesFileMap.put(Constant.Role.SECONDARYNAMENODE.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_HADOOP);
			procesFileMap
					.put(Constant.Role.JOBHISTORYSERVER.toLowerCase(),
							HadoopConstants.FileName.ScriptFile.DAEMON_FILE_MRJOBHISTORY);
			procesFileMap.put(
					Constant.Role.DFSZKFAILOVERCONTROLLER.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_HADOOP);
			procesFileMap.put(Constant.Role.JOURNALNODE.toLowerCase(),
					HadoopConstants.FileName.ScriptFile.DAEMON_FILE_HADOOP);

			JAVAPROCESSSCRIPTFILE = procesFileMap;
		}

		/**
		 * To get the comma separated services for the given role.
		 * 
		 * @param processName
		 *            the process name
		 * @return Daemon Script Name for Service Action.
		 */
		public static String getProcessDaemonFile(String processName) {
			return JAVAPROCESSSCRIPTFILE.get(processName.toLowerCase());
		}
	}

	/**
	 * The Class RoleRpmServiceCommand.
	 */
	class RoleRpmServiceCommand {

		/** The cmd service. */
		static String CMD_SERVICE = "service";

		/** The cmd sudo. */
		static String CMD_SUDO = "sudo";

		/** The cmd start. */
		static String CMD_START = "start";

		/** The dir path etc initd. */
		static String DIR_PATH_ETC_INITD = "/etc/init.d/";

		/** The prefix services hdfs. */
		static String PREFIX_SERVICES_HDFS = "hadoop-hdfs-";

		/** The prefix services yarn. */
		static String PREFIX_SERVICES_YARN = "hadoop-yarn-";

		/** The prefix services mapreduce. */
		static String PREFIX_SERVICES_MAPREDUCE = "hadoop-mapreduce-";

		/** The PREFI x_ service s_ m rv1. */
		static String PREFIX_SERVICES_MRv1 = "hadoop-0.20-mapreduce-";

		/** The process pid file map. */
		static Map<String, String> ROLE_RPM_SERVICE_CMD_MAP = new HashMap<String, String>();

		static {
			Map<String, String> roleRpmServiceCmdMap = new HashMap<String, String>();

			roleRpmServiceCmdMap
					.put(Constant.Role.NAMENODE, PREFIX_SERVICES_HDFS
							+ Constant.Role.NAMENODE.toLowerCase());

			roleRpmServiceCmdMap
					.put(Constant.Role.DATANODE, PREFIX_SERVICES_HDFS
							+ Constant.Role.DATANODE.toLowerCase());

			roleRpmServiceCmdMap.put(
					Constant.Role.SECONDARYNAMENODE,
					PREFIX_SERVICES_HDFS
							+ Constant.Role.SECONDARYNAMENODE.toLowerCase());

			roleRpmServiceCmdMap.put(
					Constant.Role.DFSZKFAILOVERCONTROLLER,
					PREFIX_SERVICES_HDFS
							+ Constant.Role.DFSZKFAILOVERCONTROLLER
									.toLowerCase());

			roleRpmServiceCmdMap.put(
					Constant.Role.JOURNALNODE,
					PREFIX_SERVICES_HDFS
							+ Constant.Role.JOURNALNODE.toLowerCase());

			roleRpmServiceCmdMap.put(
					Constant.Role.JOBTRACKER,
					PREFIX_SERVICES_MRv1
							+ Constant.Role.JOBTRACKER.toLowerCase());

			roleRpmServiceCmdMap.put(
					Constant.Role.TASKTRACKER,
					PREFIX_SERVICES_MRv1
							+ Constant.Role.TASKTRACKER.toLowerCase());

			roleRpmServiceCmdMap.put(Constant.Role.JOBHISTORYSERVER,
					PREFIX_SERVICES_MAPREDUCE + "historyserver");

			roleRpmServiceCmdMap.put(
					Constant.Role.RESOURCEMANAGER,
					PREFIX_SERVICES_YARN
							+ Constant.Role.RESOURCEMANAGER.toLowerCase());

			roleRpmServiceCmdMap.put(
					Constant.Role.NODEMANAGER,
					PREFIX_SERVICES_YARN
							+ Constant.Role.NODEMANAGER.toLowerCase());

			roleRpmServiceCmdMap.put(Constant.Role.WEBAPPPROXYSERVER,
					PREFIX_SERVICES_YARN + "proxyserver");

			ROLE_RPM_SERVICE_CMD_MAP = roleRpmServiceCmdMap;
		}

		/**
		 * Gets the role rpm service cmd.
		 * 
		 * @param roleName
		 *            the role name
		 * @return the role rpm service cmd
		 */
		public static String getRoleRpmServiceCmd(String roleName) {
			// return RoleRpmServiceCommand.CMD_SUDO + " "
			// + RoleRpmServiceCommand.DIR_PATH_ETC_INITD + " "

			return RoleRpmServiceCommand.CMD_SERVICE + " "
					+ ROLE_RPM_SERVICE_CMD_MAP.get(roleName) + " ";
		}
	}

	/**
	 * The Class RoleLogsDirPathMap.
	 */
	class RoleLogsDirPathMap {

		/** The dir path hadoop. */
		static String DIR_PATH_HADOOP = "/var/log/";

		/** The relpath dir hdfs. */
		static String RELPATH_DIR_HDFS = "hadoop-hdfs";

		/** The relpath dir mapreduce. */
		static String RELPATH_DIR_MAPREDUCE = "hadoop-mapreduce";

		/** The RELPAT h_ di r_ m rv1. */
		static String RELPATH_DIR_MRv1 = "hadoop-0.20-mapreduce";

		/** The relpath dir yarn. */
		static String RELPATH_DIR_YARN = "hadoop-yarn";

		/** The process pid file map. */
		static Map<String, String> ROLE_LOG_DIR_PATH_MAP = new HashMap<String, String>();

		static {
			Map<String, String> roleLogDirMap = new HashMap<String, String>();

			roleLogDirMap.put(Constant.Role.NAMENODE, DIR_PATH_HADOOP
					+ RELPATH_DIR_HDFS + "/");

			roleLogDirMap.put(Constant.Role.DATANODE, DIR_PATH_HADOOP
					+ RELPATH_DIR_HDFS + "/");

			roleLogDirMap.put(Constant.Role.SECONDARYNAMENODE, DIR_PATH_HADOOP
					+ RELPATH_DIR_HDFS + "/");

			roleLogDirMap.put(Constant.Role.DFSZKFAILOVERCONTROLLER,
					DIR_PATH_HADOOP + RELPATH_DIR_HDFS + "/");

			roleLogDirMap.put(Constant.Role.JOURNALNODE, DIR_PATH_HADOOP
					+ RELPATH_DIR_HDFS + "/");

			roleLogDirMap.put(Constant.Role.JOBTRACKER, DIR_PATH_HADOOP
					+ RELPATH_DIR_MRv1 + "/");

			roleLogDirMap.put(Constant.Role.TASKTRACKER, DIR_PATH_HADOOP
					+ RELPATH_DIR_MRv1 + "/");

			roleLogDirMap.put(Constant.Role.JOBHISTORYSERVER, DIR_PATH_HADOOP
					+ RELPATH_DIR_MAPREDUCE + "/");

			roleLogDirMap.put(Constant.Role.RESOURCEMANAGER, DIR_PATH_HADOOP
					+ RELPATH_DIR_YARN + "/");

			roleLogDirMap.put(Constant.Role.NODEMANAGER, DIR_PATH_HADOOP
					+ RELPATH_DIR_YARN + "/");

			roleLogDirMap.put(Constant.Role.WEBAPPPROXYSERVER, DIR_PATH_HADOOP
					+ RELPATH_DIR_YARN + "/");

			ROLE_LOG_DIR_PATH_MAP = roleLogDirMap;
		}

		/**
		 * Gets the role log dir path.
		 * 
		 * @param roleName
		 *            the role name
		 * @return the role log dir path
		 */
		public static String getRoleLogDirPath(String roleName) {
			return ROLE_LOG_DIR_PATH_MAP.get(roleName);
		}
	}

	/**
	 * The Interface YARN.
	 */
	interface YARN {

		/**
		 * Implementation to get the display name for a given key.
		 * 
		 * @author Akhil
		 * 
		 */
		class RmApiDisplayName {

			/** The RM API Key-Display map. */
			static Map<String, String> KEY_DISPLAY_NAME_MAP = new HashMap<String, String>();

			static {
				Map<String, String> keyDisplayNameMap = new HashMap<String, String>();
				keyDisplayNameMap.put(
						HadoopConstants.YARN.ClusterMetrics.APPS_RUNNING,
						"Running Applications");
				keyDisplayNameMap.put(
						HadoopConstants.YARN.ClusterMetrics.APPS_COMPLETED,
						"Completed Applications");
				keyDisplayNameMap.put(
						HadoopConstants.YARN.ClusterMetrics.MEMORY_AVAILABLE,
						"Available Memory");
				keyDisplayNameMap.put(
						HadoopConstants.YARN.ClusterMetrics.MEMORY_TOTAL,
						"Total Memory");
				keyDisplayNameMap.put(
						HadoopConstants.YARN.ClusterMetrics.STARTTIME,
						"Resource Manager Started");

				keyDisplayNameMap.put(HadoopConstants.YARN.ClusterMetrics.RMUI,
						"Resource Manager UI");

				KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;

			}

			/**
			 * Gets the key display name.
			 * 
			 * @param key
			 *            the key
			 * @return the key display name
			 */
			public static String getKeyDisplayName(String key) {
				return KEY_DISPLAY_NAME_MAP.get(key);
			}
		}

		/**
		 * The Interface ClusterMetrics.
		 */
		interface ClusterMetrics {

			/** The apps completed. */
			String APPS_COMPLETED = "appsCompleted";

			/** The apps running. */
			String APPS_RUNNING = "appsRunning";

			/** The memory total. */
			String MEMORY_TOTAL = "totalMB";

			/** The memory available. */
			String MEMORY_AVAILABLE = "availableMB";

			/** The nodes active. */
			String NODES_ACTIVE = "activeNodes";

			/** The starttime. */
			String STARTTIME = "StartTime";

			/** The rmui. */
			String RMUI = "Resource Manager UI";
		}

		/** The app id. */
		String APPID = "appid";

		/** The appattempts. */
		String APPATTEMPTS = "appAttempts";

		/** The appattempt. */
		String APPATTEMPT = "appAttempt";

		/** The Hadoop 2 App List. */
		String JSON_HADOOP2APPLIST = "hadoop2applist";

		/** The cluster metrics. */
		String CLUSTER_METRICS = "clusterMetrics";

		/** The apps. */
		String APPS = "apps";

		/** The app. */
		String APP = "app";

		/** The job. */
		String JOB = "job";

		/** The Hadoop 2 App Details. */
		String JSON_APPDETAILS = "appdetails";

		/** The Hadoop 2 App Details. */
		String JSON_APPATTEMPTS = "appattempts";

		/** The Hadoop 2 job info. */
		String JSON_JOBINFO = "jobinfo";

		/** The Hadoop 2 Application Type. */
		String APPLICATIONTYPE = "applicationType";

		/** The Hadoop 2 Application Type MAPREDUCE. */
		String APPTYPE_MAPRED = "MAPREDUCE";

		/** The Hadoop 2 Application tracking ui Application Master. */
		String APPLICATION_TRACKINGUI_APPLICATIONMASTER = "ApplicationMaster";

		/** The Hadoop 2 Application tracking ui History. */
		String APPLICATION_TRACKINGUI_HISTORY = "History";

		/** The Hadoop 2 Application Type YARN. */
		String APPTYPE_YARN = "YARN";

		/** The Hadoop 2 Application Tracking UI. */
		String TRACKINGUI = "trackingUI";

		/** The Hadoop 2 Tracking UI History. */
		String TRACKINGUI_HISTORY = "History";

		/** The Hadoop 2 Tracking UI Application Master. */
		String TRACKINGUI_APPMASTER = "ApplicationMaster";

	}

	/**
	 * The Interface Command.
	 */
	interface Command {

		String GENERATE_RSA_KEYS = "[[ -f ~/.ssh/id_rsa ]] || ssh-keygen -q -t rsa -P '' -f ~/.ssh/id_rsa;";

		/**
		 * The Interface Action.
		 */
		interface Action {

			/** The start. */
			String START = "start";

			/** The stop. */
			String STOP = "stop";

		}

		/** The hadoop. */
		String HADOOP = "hadoop";

		/** The hdfs. */
		String HDFS = "hdfs";

		/** The job. */
		String JOB = "job";

		/** The application. */
		String APPLICATION = "application";

		/** The yarn. */
		String YARN = "yarn";

		/** The datanode. */
		String DAEMON_NODEMANAGER = "nodemanager";

		/** The datanode. */
		String DAEMON_DATANODE = "datanode";

		/** The tasktracker. */
		String DAEMON_TASKTRACKER = "tasktracker";

		/** The secondary namenode. */
		String DAEMON_SECONDARY_NAMENODE = "secondarynamenode";

		/** The namenode -format. */
		String NAMENODE_FORMAT = "namenode -format";

		/** The transition to active. */
		String TRANSITION_TO_ACTIVE = "haadmin -transitionToActive";

		/** The namenode copy metadata. */
		String NAMENODE_COPY_METADATA = "namenode -bootstrapStandby";

		/** The initialiaze ha in zookeeper. */
		String INITIALIAZE_HA_IN_ZOOKEEPER = "zkfc -formatZK";
	}

	/**
	 * The Interface FileName.
	 */
	interface FileName {

		String EXTENSION_XML = ".xml";

		/**
		 * The Interface ConfigurationFile.
		 */
		interface ConfigurationFile {
			/** The core-site.xml. */
			String XML_CORE_SITE = "core-site.xml";

			/** The core-site.xml. */
			String XML_YARN_SITE = "yarn-site.xml";

			/** The mapred-site.xml. */
			String XML_MAPRED_SITE = "mapred-site.xml";

			/** The hdfs-site.xml. */
			String XML_HDFS_SITE = "hdfs-site.xml";

			/** The hdfs-site.xml. */
			String XML_HADOOP_POLICY = "hadoop-policy.xml";

			/** The xml capacity scheduler. */
			String XML_CAPACITY_SCHEDULER = "capacity-scheduler.xml";

			/** The slaves. */
			String SLAVES = "slaves";

			/** The masters. */
			String MASTERS = "masters";

			/** The hadoop-env.sh. */
			String ENV_HADOOP = "hadoop-env.sh";

			/** The yarn-env.sh. */
			String ENV_YARN = "yarn-env.sh";

			/** The mapred-env.sh. */
			String ENV_MAPRED = "mapred-env.sh";

			/** The Constant HADOOP_MAPRED_STOP. */
			String METRICS_PROPERTIES_FILE = "hadoop-metrics.properties";

			/** The Constant hadoop-metrics2.properties. */
			String METRICS2_PROPERTIES_FILE = "hadoop-metrics2.properties";
		}

		/**
		 * The Interface ScriptFile.
		 */
		interface ScriptFile {

			/** The hadoop-daemon.sh. */
			String DAEMON_FILE_HADOOP = "hadoop-daemon.sh";

			/** The hadoop-daemons.sh. */
			String DAEMONS_FILE_HADOOP = "hadoop-daemons.sh";

			/** The yarn-daemons.sh. */
			String DAEMONS_FILE_YARN = "yarn-daemons.sh";

			/** The hadoop-daemon.sh. */
			String DAEMON_FILE_MRJOBHISTORY = "mr-jobhistory-daemon.sh";

			/** The hadoop-daemon.sh. */
			String DAEMON_FILE_YARN = "yarn-daemon.sh";

			/** The start-dfs.sh. */
			String DFS_START = "start-dfs.sh";

			/** The stop-dfs.sh. */
			String DFS_STOP = "stop-dfs.sh";

			/** The start-yarn.sh. */
			String YARN_START = "start-yarn.sh";

			/** The stop-yarn.sh. */
			String YARN_STOP = "stop-yarn.sh";

			/** The Constant start-mapred.sh. */
			String MAPRED_START = "start-mapred.sh";

			/** The Constant stop-mapred.sh. */
			String MAPRED_STOP = "stop-mapred.sh";
		}
	}

	/**
	 * The Interface Vendors.
	 */
	interface Vendors {

		/** The cloudera. */
		String CLOUDERA = "Cloudera";

		/** The apache. */
		String APACHE = "Apache";
	}

	interface JmxBeanKeys {

		interface DfsData {

			String VERSION = "Version";

			interface Nodes {

				String DEAD = "DeadNodes";

				String LIVE = "LiveNodes";

				String DECOMMISION = "DecomNodes";
			}

		}

		interface MapReduceData {

		}

		interface YarnData {

		}

	}
}
