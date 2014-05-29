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
package com.impetus.ankush.common.constant;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Interface Constant.
 * 
 * @author nikunj
 */
public interface Constant {

	/** The Constant LINE_SEPERATOR. */
	String LINE_SEPERATOR = System.getProperty("line.separator");

	/** The Constant DOTS. */
	String DOTS = "...";

	/** The Constant .bashrc file. */
	String BASHRC_FILE = "~/.bashrc";

	/** The Constant /etc/environment file. */
	String ETCENV_FILE = "/etc/environment";

	/** The Constant URI File Prefix for HDFS . */
	String URI_FILE_PREFIX = "file://";

	/** The root user. */
	String ROOT_USER = "root";

	/** The str space. */
	String STR_SPACE = " ";

	/**
	 * API to get ganglia metrics display name
	 * 
	 * @author hokam
	 * 
	 */
	class GangliaMetricsName {

		/** The service name map. */
		static Map<String, String> metricsNameMap = new HashMap<String, String>();

		static {
			// cpu metrics display name.
			metricsNameMap.put("cpu_idle", "CPU Idle");
			metricsNameMap.put("cpu_wio", "CPU wio");
			metricsNameMap.put("cpu_system", "CPU System");
			metricsNameMap.put("cpu_nice", "CPU Nice");
			metricsNameMap.put("cpu_user", "CPU User");

			// disk metrics display name.
			metricsNameMap.put("disk_free", "Disk Space Available");
			metricsNameMap.put("disk_total", "Total Disk Space");

			// memory metrics display name.
			metricsNameMap.put("mem_total", "Total Memory");
			metricsNameMap.put("mem_buffers", "Memory Buffers");
			metricsNameMap.put("mem_shared", "Shared Memory");
			metricsNameMap.put("mem_free", "Free Memory");
			metricsNameMap.put("mem_cached", "Cached Memory");

			// load metrics display name.
			metricsNameMap.put("load_one", "One Minute Load Average");
			metricsNameMap.put("load_five", "Five Minute Load Average");
			metricsNameMap.put("load_fifteen", "Fifteen Minute Load Average");

			// process metrics display name
			metricsNameMap.put("proc_total", "Total Processes");
			metricsNameMap.put("proc_run", "Total Running Processes");

			// network metrics display name.
			metricsNameMap.put("bytes_in", "Bytes Received");
			metricsNameMap.put("bytes_out", "Bytes Sent");
			metricsNameMap.put("pkts_in", "Packets Received");
			metricsNameMap.put("pkts_out", "Packets Sent");

			// process metrics display name
			metricsNameMap.put("swap_total", "Total Swap Memory");
			metricsNameMap.put("swap_free", "Free Swap Memory");

		}

		/**
		 * Gets the service name map.
		 * 
		 * @param technology
		 *            the technology
		 * @return the service name map
		 */
		public static String getMetricsDisplayName(String name) {
			return metricsNameMap.get(name);
		}

	}

	/**
	 * The Interface TaskableClass.
	 */
	interface TaskableClass {
		/** jps service status monitor. **/
		String JPS_MONITOR = "com.impetus.ankush.agent.utils.JPSServiceStatusMonitor";
		/** dir usage monitor for ganglia rrd creation. **/
		String DIR_USAGE_MONITOR = "com.impetus.ankush.agent.utils.DirectoryUsageMonitor";

		/** oracle no sql service and shard monitoring class. **/
		String ORACLE_MONITOR = "com.impetus.ankush.agent.oracle.OracleServiceStatusMonitor";

		/** The storm monitor. */
		String STORM_MONITOR = "com.impetus.ankush.agent.storm.StormTopologyMonitor";

		/** The Elastic search_ monitor. */
		String ElasticSearch_MONITOR = "com.impetus.ankush.agent.elasticsearch.ElasticSearchMonitor";

		/** The Hadoop NameNode Role Update class for Hadoop 2 with HA enabled. */
		String Hadoop_HA_Update_NN_Role = "com.impetus.ankush.agent.hadoop.UpdateNameNodeRole";

		/** The Hadoop Job Monitoring class for Hadoop 1. */
		String Hadoop_JOB_STATUS_MONITOR = "com.impetus.ankush.agent.utils.JobStatusMonitor";

	}

	/**
	 * The Enum JmxTransServiceAction.
	 */
	enum JmxTransServiceAction {

		/** The start. */
		START,

		/** The stop. */
		STOP,

		/** The restart. */
		RESTART,

		/** The status. */
		STATUS,
	};

	/**
	 * The Enum SyncConfigurationAction.
	 */
	enum SyncConfigurationAction {

		/** The add. */
		ADD,

		/** The remove. */
		REMOVE
	};

	/** The tmp private key file. */
	String TMP_PRIVATE_KEY_FILE = "/tmp/sshKeyFile";

	/**
	 * The Interface SystemCommands.
	 */
	interface SystemCommands {

		/** The gethostname. */
		String GETHOSTNAME = "hostname";

		/** The sshcopyid. */
		String SSHCOPYID = "ssh-copy-id";

		/** The sshpasswithpassword. */
		String SSHPASSWITHPASSWORD = "sshpass -p";

		/** The sshpasswithkeyfile. */
		String SSHPASSWITHKEYFILE = "sshpass -f";
	}

	/**
	 * The Interface ServiceAction.
	 */
	interface ServiceAction {

		/** The start. */
		String START = "start";

		/** The stop. */
		String STOP = "stop";
	}

	// constants for server.
	/**
	 * The Interface Graph.
	 */
	interface Graph {

		/** The rrd bash path. */
		String RRD_BASH_PATH = "/var/lib/ganglia/rrds/";

		/**
		 * The Enum StartTime.
		 */
		public enum StartTime {

			/** The lasthour. */
			lasthour,
			/** The lastday. */
			lastday,
			/** The lastweek. */
			lastweek,
			/** The lastmonth. */
			lastmonth,
			/** The lastyear. */
			lastyear
		};
	}

	/**
	 * The Interface Server.
	 */
	interface Server {

		/** The config. */
		String CONFIG = "appConfig";
	}

	/**
	 * The interface File_Extension.
	 */
	interface File_Extension {

		/** The config. */
		String CONFIG = "config";

		/** The xml. */
		String XML = "xml";

		/** The properties. */
		String PROPERTIES = "properties";

		/** The yaml. */
		String YAML = "yaml";
	}

	/**
	 * The Interface Keys.
	 */
	interface Keys {

		/** Conf Keys **/
		String CONFKEY = "confKey";

		/** GRAPH VIEWS **/
		String GRAPHVIEWS = "graphviews";

		/** Node graphs **/
		String NODEGRAPHS = "nodegraphs";

		/** default graphs **/
		String DEFAULTGRAPHS = "defaultgraphs";

		/** The id. */
		String ID = "id";

		/** The rackid. */
		String RACKID = "rackId";

		/** The nodeip. */
		String NODEIP = "nodeIp";

		/** The value. */
		String VALUE = "value";

		/** The status. */
		String STATUS = "status";

		/** The rackinfo. */
		String RACKINFO = "rackInfo";

		/** The datacenterInfo. */
		String DATACENTERINFO = "datacenterInfo";

		/** The datacenters. */
		String DATACENTERS = "datacenters";

		/** The nodedata. */
		String NODEDATA = "nodeData";

		/** The totalrack. */
		String TOTALRACK = "totalRack";

		/** The totalDatacenter. */
		String TOTALDATACENTER = "totalDatacenter";

		/** The nodeid. */
		String NODEID = "nodeId";

		/** The totalnode. */
		String TOTALNODE = "totalnode";

		/** The events. */
		String EVENTS = "events";

		/** The error. */
		String ERROR = "error";

		/** The ip. */
		String IP = "ip";

		/** The command. */
		String COMMAND = "command";

		/** The cfName. */
		String CFNAME = "cfName";

		/** The ksName. */
		String KSNAME = "ksName";

		/** The jobs. */
		String JOBS = "jobs";

		/** The job id. */
		String JOBID = "jobid";

		/** The publicip. */
		String PUBLICIP = "publicIp";

		/** The privateip. */
		String PRIVATEIP = "privateIp";

		/** The lastlog. */
		String LASTLOG = "lastlog";

		/** The clusterid. */
		String CLUSTERID = "clusterId";

		/** The host. */
		String HOST = "host";

		/** The operationid. */
		String OPERATIONID = "operationId";

		/** The logs. */
		String LOGS = "logs";

		/** The state. */
		String STATE = "state";

		/** The starttime. */
		String STARTTIME = "startTime";

		/** The message. */
		String MESSAGE = "message";

		/** The out. */
		String OUT = "out";

		/** The output. */
		String OUTPUT = "output";

		/** The nodes. */
		String NODES = "nodes";

		/** The cluster name. */
		String CLUSTER_NAME = "clusterName";

		/** The ecosystem. */
		String ECOSYSTEM = "ecosystem";

		/** The components. */
		String COMPONENTS = "components";

		/** The password. */
		String PASSWORD = "password";

		/** The username. */
		String USERNAME = "username";

		/** The errors. */
		String ERRORS = "errors";

		/** The privatekey. */
		String PRIVATEKEY = "privateKey";

		/** The agent. */
		String AGENT = "Agent";

		/** The agent. */
		String ACTION = "action";

		/** The services. */
		String SERVICES = "services";

		/** The servicestatus. */
		String SERVICESTATUS = "serviceStatus";

		/** The serviceerror. */
		String SERVICEERROR = "serviceError";

		/** The tiles. */
		String TILES = "tiles";

		/** The cloudnodemetadatainfo. */
		String CLOUDNODEMETADATAINFO = "cloudNodeMetaDataInfo";

		/** The namenode. */
		String NAMENODE = "NameNode";

		/** The datanodes. */
		String DATANODES = "DataNodes";

		/** The datanodes. */
		String DATANODE = "DataNode";

		/** The secondarynamenode. */
		String SECONDARYNAMENODE = "Secondary NameNode";

		/** The type. */
		String TYPE = "type";

		/** The files. */
		String FILES = "files";

		/** The filename. */
		String FILENAME = "fileName";

		/** The readcount. */
		String READCOUNT = "readCount";

		/** The bytescount. */
		String BYTESCOUNT = "bytesCount";

		/** The downloadpath. */
		String DOWNLOADPATH = "downloadPath";

		/** The down. */
		String DOWN = "Down";

		/** The jobargs. */
		String JOBARGS = "jobArgs";

		/** The hadoopparams. */
		String HADOOPPARAMS = "hadoopParams";

		/** The job. */
		String JOB = "job";

		/** The jar. */
		String JAR = "jar";

		/** The jarpath. */
		String JARPATH = "jarPath";

		/** The pattern file. */
		String PATTERN_FILE = "patternFile";

		/** The file pattern. */
		String PATTERN = "pattern";

		/** The ip pattern. */
		String IP_PATTERN = "ipPattern";

		/** The nodestate. */
		String NODESTATE = "nodeState";

		/** The seedNodes. */
		String SEEDNODES = "SeedNodes";

		/** The nonseedNodes. */
		String NONSEEDNODES = "NonSeedNodes";

		/** The clusterNodes. */
		String CLUSTERNODES = "ClusterNodes";

		/** The jps process list. */
		String JPS_PROCESS_LIST = "JPS_PROCESS_LIST";

		/** The process port map. */
		String PROCESS_PORT_MAP = "PROCESS_PORT_MAP";

		/** The technology. */
		String TECHNOLOGY = "technology";

		/** The topology. */
		String TOPOLOGY = "topology";

		/** The topologyargs. */
		String TOPOLOGYARGS = "topologyArgs";

		/** The datacenterName. */
		String DATACENTERNAME = "dataCenterName";

		/** The nodeStatus. */
		String NODESTATUS = "nodeStatus";

		/** The rackName. */
		String RACKNAME = "rackName";

		/** The load. */
		String LOAD = "Load";

		/** The ownership. */
		String OWNERSHIP = "ownership";

		/** The ThriftServer. */
		String RPCSERVER = "RPCServerRunning";

		/** The NativeTransportRunning. */
		String NATIVETRANSPORT = "NativeTransportRunning";

		/** The HintedHandoffEnabled. */
		String HINTEDHANDOFF = "HintedHandoffEnabled";

		/** The hostId. */
		String HOSTID = "Host Id";

		/** The clusterOwnership. */
		String CLUSTEROWNERSHIP = "clusterOwnership";

		/** The nodeLoad. */
		String NODELOAD = "nodeLoad";

		/** The tokenValue. */
		String TOKENVALUE = "tokenValue";

		/** The tokens. */
		String TOKENS = "tokens";

		/** The tokens. */
		String TOKENCOUNT = "Token Count";

		/** The vNodeCount. */
		String V_NODE_COUNT = "vNodeCount";

		/** The KeyCaches. */
		String KEYCACHES = "Key Caches";

		/** The RowCaches. */
		String ROWCACHES = "Row Caches";

		/** The RowCaches. */
		String GENERATIONNUMBER = "Generation Number";

		/** The dbSchema. */
		String DBSCHEMA = "dbSchema";

		/** The replicationData. */
		String REPLICATIONDATA = "replicationData";

		/** The replicationFactor. */
		String REPLICATIONFACTOR = "replicationFactor";

		/** The replicationStrategy. */
		String REPLICATIONSTRATEGY = "replicationStrategy";

		/** The columnFamilyCount. */
		String COLUMN_FAMILY_COUNT = "columnFamilyCount";

		/** The Snitch. */
		String SNITCH = "Snitch";

		/** The Partitioner. */
		String PARTITIONER = "Partitioner";

		/** The keySpace. */
		String COLUMN_METADATA = "columnMetadata";

		/** The keySpace. */
		String COLUMN_FAMILY_GENERAL_PROPERTIES = "general";

		/** The keySpace. */
		String COLUMN_FAMILY_PERFORMANCE_TUNING_PROPERTIES = "performanceTuning";

		/** The keySpace. */
		String COLUMN_FAMILY_COMPACTION_PROPERTIES = "compaction";

		/** The keySpace. */
		String COLUMN_FAMILY_COMPRESSION_PROPERTIES = "compression";

		/** The comment. */
		String COLUMN_FAMILY_COMMENT = "comment";

		/** The comment. */
		String COLUMN_TYPE = "column_type";

		/** The comment. */
		String COMPARATOR_TYPE = "comparator_type";

		/** The comment. */
		String SUBCOMPARATOR_TYPE = "subcomparator_type";

		/** The comment. */
		String KEY_VALIDATION_CLASS = "key_validation_class";

		/** The comment. */
		String DEFAULT_VALIDATION_CLASS = "default_validation_class";

		/** The comment. */
		String GC_GRACE_SECONDS = "gc_grace_seconds";

		/** The comment. */
		String CACHING = "caching";

		/** The comment. */
		String READ_REPAIR_CHANCE = "read_repair_chance";

		/** The comment. */
		String DCLOCAL_READ_REPAIR_CHANCE = "dclocal_read_repair_chance";

		/** The comment. */
		String REPLICATE_ON_WRITE = "replicate_on_write";

		/** The comment. */
		String BLOOM_FILTER_FP_CHANCE = "bloom_filter_fp_chance";

		/** The comment. */
		String COMPACTION_STRATEGY = "compaction_strategy";

		/** The comment. */
		String MAX_COMPACTION_THRESHOLD = "max_compaction_threshold";

		/** The comment. */
		String MIN_COMPACTION_THRESHOLD = "min_compaction_threshold";

		/** The comment. */
		String COLUMN_NAME = "name";

		/** The comment. */
		String COLUMN_VALIDATION_CLASS = "validationClass";

		/** The response code. */
		String RESPONSE_CODE = "responseCode";

		/** The port. */
		String PORT = "port";

		/** The all. */
		String ALL = "ALL";

		/** The list. */
		String LIST = "list";

		/** The ui. */
		String UI = "ui";

		/** The worker. */
		String WORKER = "worker";

		/** The severity **/
		String SEVERITY = "severity";

		/** Name **/
		String NAME = "name";

		/** key **/
		String KEY = "key";

		String SUBTYPE = "subType";

		String COUNT = "count";

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
							Constant.Keys.YARN.ClusterMetrics.APPS_RUNNING,
							"Running Applications");
					keyDisplayNameMap.put(
							Constant.Keys.YARN.ClusterMetrics.APPS_COMPLETED,
							"Completed Applications");
					keyDisplayNameMap.put(
							Constant.Keys.YARN.ClusterMetrics.MEMORY_AVAILABLE,
							"Available Memory");
					keyDisplayNameMap.put(
							Constant.Keys.YARN.ClusterMetrics.MEMORY_TOTAL,
							"Total Memory");
					keyDisplayNameMap.put(
							Constant.Keys.YARN.ClusterMetrics.STARTTIME,
							"Resource Manager Started");
					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

				public static String getKeyDisplayName(String key) {
					return KEY_DISPLAY_NAME_MAP.get(key);
				}
			}

			interface ClusterMetrics {
				String APPS_COMPLETED = "appsCompleted";
				String APPS_RUNNING = "appsRunning";
				String MEMORY_TOTAL = "totalMB";
				String MEMORY_AVAILABLE = "availableMB";
				String NODES_ACTIVE = "activeNodes";
				String STARTTIME = "StartTime";
			}

			/** The app id. */
			String APPID = "appid";

			/** The appattempts. */
			String APPATTEMPTS = "appAttempts";

			/** The appattempt. */
			String APPATTEMPT = "appAttempt";

			/** The Hadoop 2 App List. */
			String JSON_HADOOP2APPLIST = "hadoop2applist";

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
	}

	/**
	 * The Interface Agent.
	 */
	interface Agent {

		/** The agent down message. */
		String AGENT_DOWN_MESSAGE = "Ankush Agent is down on some nodes. Please resolve related issue.";

		/** The Constant AGENT_PROPERTY_FILE_PATH. */
		String AGENT_PROPERTY_FILE_PATH = ".ankush/agent/conf/agent.properties";

		/** The Constant AGENT_TASKABLE_FILE_PATH. */
		String AGENT_TASKABLE_FILE_PATH = ".ankush/agent/conf/taskable.conf";
		
		String AGENT_START_SCRIPT = "$HOME/.ankush/agent/bin/start-agent.sh";
		
		String AGENT_STOP_SCRIPT = "$HOME/.ankush/agent/bin/stop-agent.sh";
	}

	/**
	 * The Interface Properties.
	 */
	interface Properties {

		/**
		 * The Interface Agent.
		 */
		interface Agent {

			/** The symmetric update flag. */
			String SYMMETRIC_UPDATE_FLAG = "SYMMETRIC_UPDATE_FLAG";

			/** The cluster technology name. */
			String CLUSTER_TECHNOLOGY_NAME = "CLUSTER_TECHNOLOGY_NAME";

			/** The default server port. */
			String DEFAULT_SERVER_PORT = "8080";

			/** The node id. */
			String NODE_ID = "NODE_ID";

			/** The host. */
			String HOST = "HOST";

			/** The port. */
			String PORT = "PORT";

			String CLUSTER_ID = "CLUSTER_ID";
		}
	}

	/** The thread pool size. */
	int THREAD_POOL_SIZE = Integer.MAX_VALUE;

	/**
	 * The Interface App.
	 */
	interface App {

		/**
		 * The Interface State.
		 */
		interface State {

			/** The login. */
			String LOGIN = "login";

			/** The configure. */
			String CONFIGURE = "configure";
		}
	}

	/**
	 * The Interface Technology.
	 */
	interface Technology {

		/** The oracle nosql. */
		String ORACLE_NOSQL = "Oracle NoSQL Database";

		/** The hadoop. */
		String HADOOP = "Hadoop";

		/** The hadoop2. */
		String HADOOP2 = "Hadoop2";

		/** The cassandra. */
		String CASSANDRA = "Cassandra";

		/** The kafka. */
		String KAFKA = "Kafka";

		/** The storm. */
		String STORM = "Storm";

		/** The zookeeper. */
		String ZOOKEEPER = "Zookeeper";

		/** The elasticsearch. */
		String ELASTICSEARCH = "ElasticSearch";

	}

	/**
	 * The Interface Service.
	 */
	interface Service {

		/**
		 * The Interface State.
		 */
		interface State {

			/** The hide. */
			String HIDE = "hide";

			/** The running. */
			String RUNNING = "running";

			/** The error. */
			String ERROR = Keys.ERROR;

			/** The warning. */
			String WARNING = "warning";

			/** The down. */
			String DOWN = "Down";
		}

		/** The parameter config service. */
		String PARAMETER_CONFIG_SERVICE = "parameterConfigService";

		/** The parameter config service. */
		String CASSANDRA_PARAMETER_CONFIG_SERVICE = "cassandraParameterConfigService";

		/** The jcloud. */
		String JCLOUD = "jCloudActionsProvider";

		/** App Conf Service **/
		String APPCONF = "appConfService";
	}

	/**
	 * The Interface Cluster.
	 */
	interface Cluster {

		interface DeploymentTask {

			String SAVEDCLUSTER = "SavedCluster";

			String CLOUDLAUNCHED = "CloudLaunched";

			String PREDEPLOY = "PreDeploy";

			String CONFIGSADDED = "ConfigsAdded";

			String UPDATEDCLUSTER = "UpdatedCluster";

			String VALIDATEDCLUSTER = "ValidatedCluster";

			String DEPLOYEDCOMPONENTS = "DeployedComponents";

			String POSTDEPLOYDONE = "PostDeployeDone";

			String ROLLBACKDONE = "RollbackDone";
		}

		/**
		 * The Interface State.
		 */
		interface State {

			/** Deployment states. */
			String DEPLOYING = "deploying";

			/** The deployed. */
			String DEPLOYED = "deployed";

			/** The error. */
			String ERROR = Keys.ERROR;

			/** The down. */
			String DOWN = "down";

			/** The warning. */
			String WARNING = "warning";

			/** The critical. */
			String CRITICAL = "critical";

			/** Deleting state. */
			String REMOVING = "removing";

			/** Adding Nodes state. */
			String ADDING_NODES = "Adding Nodes";

			/** Removing Nodes state. */
			String REMOVING_NODES = "Removing Nodes";

			/** Oracle NoSQL cluster state. */
			String CHANGE_REP_FACTOR = "Change Replication Factor";

			/** The rebalance. */
			String REBALANCE = "Rebalancing";

			/** The redistribute. */
			String REDISTRIBUTE = "Redistributing";

			/** The migrate node. */
			String MIGRATE_NODE = "Migrating Node";

			String SERVER_CRASHED = "serverCrashed";
		}

		/**
		 * The Interface Environment.
		 */
		interface Environment {

			/** The in premise. */
			String IN_PREMISE = "In Premise";
		}
	}

	/**
	 * The Interface Component.
	 */
	interface Component {

		/**
		 * The Interface Process Name.
		 */
		interface ProcessName {
			/** The supervisor. */
			String SUPERVISOR = "supervisor";

			/** The nimbus. */
			String NIMBUS = "nimbus";

			/** The quorumpeermain. */
			String QUORUMPEERMAIN = "quorumpeermain";

			/** The kafka. */
			String KAFKA = "kafka";

			/** The CassandraDaemon. */
			String CASSANDRA = "CassandraDaemon";

			/** SA Process Names. */
			// String SA_WEB_SERVER_BOOTSTRAP = "BootStrap";
			String SA_WEB_SERVER_BOOTSTRAP = "SAWeb";
			String VAJRA_QUERY_SERVER = "VajraQueryServer";
			String KAFKA_CONSUMER_SERVICE = "KafkaConsumerService";
			String INDEXING_KAFKA_CONSUMER_SERVICE = "IndexingKafkaConsumerService";
			String VAJRA_AGENT = "VajraAgent";
			String ROUTER = "CamelKafkaRouter";

			/** MySQL Process Name. */
			String MYSQL = "mysqld";
		}

		interface DeploymentState {

			/** The NOTSTARTED. */
			String NOTSTARTED = "NotStarted";

			/** The INPROGRESS. */
			String INPROGRESS = "InProgress";

			/** The COMPLETED. */
			String COMPLETED = "Completed";

			/** The failed. */
			String FAILED = "Failed";
		}

		/**
		 * The Interface Name.
		 */
		interface Name {

			/** The hadoop. */
			String HADOOP = "Hadoop";

			/** The hadoop2. */
			String HADOOP2 = "Hadoop2";

			/** The hbase. */
			String HBASE = "Hbase";

			/** The zookeeper. */
			String ZOOKEEPER = "Zookeeper";

			/** The hive. */
			String HIVE = "Hive";

			/** The mahout. */
			String MAHOUT = "Mahout";

			/** The sqoop. */
			String SQOOP = "Sqoop";

			/** The pig. */
			String PIG = "Pig";

			/** The flume. */
			String FLUME = "Flume";

			/** The solr. */
			String SOLR = "Solr";

			/** The oozie. */
			String OOZIE = "Oozie";

			/** The oracle nosql. */
			String ORACLE_NOSQL = Constant.Technology.ORACLE_NOSQL;

			/** The agent. */
			String AGENT = "Agent";

			/** The ganglia. */
			String GANGLIA = "Ganglia";

			/** The dependency. */
			String DEPENDENCY = "Dependency";

			/** The Preprocessor. */
			String PREPROCESSOR = "Preprocessor";

			/** The Preprocessor. */
			String POSTPROCESSOR = "Postprocessor";

			/** The storm. */
			String STORM = "Storm";

			/** The Kafka. */
			String KAFKA = "Kafka";

			/** The ElasticSearch. */
			String ELASTICSEARCH = "ElasticSearch";

			/** The Custom Deployer. */
			String CUSTOMDEPLOYER = "CustomDeployer";

			/** The Cassandra. */
			String CASSANDRA = "Cassandra";

		}
	}

	/**
	 * The Interface Node.
	 */
	interface Node {

		/**
		 * The Interface Info.
		 */
		interface Info {

			/** The cpu. */
			String CPU = "cpu";

			/** The memory. */
			String MEMORY = "memory";

			/** The disk. */
			String DISK = "disk";

			/** The swap. */
			String SWAP = "swap";

			/** The os. */
			String OS = "os";

			/** The uptime. */
			String UPTIME = "uptime";

			/** The hadoop metrics. */
			String HADOOP_METRICS = "hmetrics";

			/** The process memory. */
			String PROCESS_MEMORY = "processMemory";

			/** The process cpu. */
			String PROCESS_CPU = "processCPU";
		}

		/**
		 * The Interface State.
		 */
		interface State {
			/** The deploying. */
			String DEPLOYING = "deploying";

			/** The adding. */
			String ADDING = "adding";

			/** The deployed. */
			String DEPLOYED = "deployed";

			/** The error. */
			String ERROR = Keys.ERROR;

			/** The removing. */
			String REMOVING = "removing";
		}
	}

	/**
	 * The Interface Manager.
	 */
	interface Manager {

		/** The monitoring. */
		String MONITORING = "nodeMonitoringManager";

		/** The node. */
		String NODE = "nodeManager";

		/** The cluster. */
		String CLUSTER = "clusterManager";

		/** The log. */
		String LOG = "logManager";

		/** The configuration. */
		String CONFIGURATION = "configurationManager";

		/** The event. */
		String EVENT = "eventManager";

		/** The event history. */
		String EVENTHISTORY = "eventHistoryManager";

		/** The role. */
		String ROLE = "roleManager";

		/** The template *. */
		String TEMPLATE = "templateManager";

		/** The template *. */
		String DEPLOYMENTSTATE = "deploymentStateManager";

		// app conf manager
		String APPCONF = "appConfManager";

		// tile manager
		String TILE = "tileManager";
	}

	/**
	 * The Class ServiceName.
	 */
	class ServiceName {

		/** The service name map. */
		static Map<String, String> serviceNameMap = new HashMap<String, String>();

		static {
			serviceNameMap.put(Constant.Technology.STORM,
					"nimbus,supervisor");
			serviceNameMap.put(Constant.Technology.KAFKA, "Kafka");
			serviceNameMap.put(Constant.Technology.CASSANDRA,
					"CassandraDaemon");
			serviceNameMap
					.put(Constant.Technology.HADOOP,
							"NameNode,SecondaryNameNode,TaskTracker,DataNode,HMaster,HRegionServer,JobTracker");
			serviceNameMap.put(Constant.Technology.ELASTICSEARCH,
					"ElasticSearch");
			serviceNameMap.put(Constant.Technology.ZOOKEEPER,
					"QuorumPeerMain");
		}


		/**
		 * Gets the service name map.
		 * 
		 * @param technology
		 *            the technology
		 * @return the service name map
		 */
		public static String getServiceNameMap(String technology) {
			System.out.println("serviceNameMap=" + serviceNameMap);
			return serviceNameMap.get(technology);
		}

	}

	/**
	 * The Interface Alerts.
	 */
	interface Alerts {

		/**
		 * The Interface Severity.
		 */
		interface Severity {

			/** The normal. */
			String NORMAL = "Normal";

			/** The critical. */
			String CRITICAL = "Critical";

			/** The warning. */
			String WARNING = "Warning";

			/** The unavailable. */
			String UNAVAILABLE = "Unavailable";
		}

		/** The default alert level. */
		double DEFAULT_ALERT_LEVEL = 80d;

		/** The default warning level. */
		double DEFAULT_WARNING_LEVEL = 60d;

		/** The default refresh interval. */
		int DEFAULT_REFRESH_INTERVAL = 60;

		/**
		 * The Interface Type.
		 */
		interface Type {

			/** The usage. */
			String USAGE = "Usage";

			/** The service. */
			String SERVICE = "Service";
		}

		/**
		 * The Interface Metric.
		 */
		interface Metric {

			/** The cpu. */
			String CPU = "CPU";

			/** The memory. */
			String MEMORY = "Memory";
		}

		/**
		 * The Interface Usage.
		 */
		interface Usage {

			/** The memory. */
			String MEMORY = "Memory Usage";

			/** The cpu. */
			String CPU = "CPU Usage";
		}
	}

	/**
	 * Implementation to get the Java Process Name for a given role.
	 * 
	 * @author Akhil
	 * 
	 */
	class RoleCommandName {

		/** The role command map. */
		static Map<String, String> ROLE_COMMAND_MAP = new HashMap<String, String>();

		static {
			Map<String, String> roleCommandMap = new HashMap<String, String>();

			// Common Roles
			roleCommandMap.put(Role.NAMENODE, "namenode");
			roleCommandMap.put(Role.DATANODE, "datanode");
			roleCommandMap.put(Role.SECONDARYNAMENODE, "secondarynamenode");

			// Roles for Hadoop 0.x & 1.x
			roleCommandMap.put(Role.JOBTRACKER, "jobtracker");
			roleCommandMap.put(Role.TASKTRACKER, "tasktracker");

			// Roles for Hadoop 2.x
			roleCommandMap.put(Role.RESOURCEMANAGER, "resourcemanager");
			roleCommandMap.put(Role.WEBAPPPROXYSERVER, "proxyserver");

			roleCommandMap.put(Role.NODEMANAGER, "nodemanager");
			roleCommandMap.put(Role.JOURNALNODE, "journalnode");
			roleCommandMap.put(Role.JOBHISTORYSERVER, "historyserver");
			roleCommandMap.put(Role.DFSZKFAILOVERCONTROLLER, "zkfc");

			// Roles for HBase
			roleCommandMap.put(Role.HBASEMASTER, "master");
			roleCommandMap.put(Role.HBASEREGIONSERVER, "regionserver");

			// Roles for Hive
			// roleCommandMap.put(Role.HIVESERVER, "hiveserver");
			ROLE_COMMAND_MAP = roleCommandMap;
		}

		/**
		 * To get the comma separated services for the given role.
		 * 
		 * @param role
		 *            the role
		 * @return comma separated services.
		 */
		public static String getCommandName(String role) {
			return ROLE_COMMAND_MAP.get(role);
		}
	}

	/**
	 * Implementation for getting the services of a given role.
	 * 
	 * @author Akhil
	 * 
	 */
	class RoleProcessName {

		/** The role java process map. */
		static Map<String, String> ROLE_PROCESS_MAP = new HashMap<String, String>();

		static {
			Map<String, String> roleProcessMap = new HashMap<String, String>();
			// Common Roles
			roleProcessMap.put(Role.NAMENODE, "NameNode");
			roleProcessMap.put(Role.DATANODE, "DataNode");
			roleProcessMap.put(Role.SECONDARYNAMENODE, "SecondaryNameNode");

			// Roles for Hadoop 0.x & 1.x
			roleProcessMap.put(Role.JOBTRACKER, "JobTracker");
			roleProcessMap.put(Role.TASKTRACKER, "TaskTracker");

			// Roles for Hadoop 2.x
			roleProcessMap.put(Role.RESOURCEMANAGER, "ResourceManager");
			roleProcessMap.put(Role.WEBAPPPROXYSERVER, "WebAppProxyServer");
			roleProcessMap.put(Role.NODEMANAGER, "NodeManager");
			roleProcessMap.put(Role.JOURNALNODE, "JournalNode");
			roleProcessMap.put(Role.JOBHISTORYSERVER, "JobHistoryServer");
			roleProcessMap.put(Role.DFSZKFAILOVERCONTROLLER,
					"DFSZKFailoverController");

			// Roles for HBase
			roleProcessMap.put(Role.HBASEMASTER, "HMaster");
			roleProcessMap.put(Role.HBASEREGIONSERVER, "HRegionServer");

			// Roles for Hive
			roleProcessMap.put(Role.HIVESERVER, "HiveServer");

			roleProcessMap.put(Role.NIMBUS, "nimbus");
			roleProcessMap.put(Role.SUPERVISOR, "supervisor");
			roleProcessMap.put(Role.KAFKA, "Kafka");
			roleProcessMap.put(Role.CASSANDRA_SEED, "CassandraDaemon");
			roleProcessMap.put(Role.CASSANDRA_NON_SEED, "CassandraDaemon");
			roleProcessMap.put(Role.ELASTICSEARCH, Role.ELASTICSEARCH);
			roleProcessMap.put(Role.ZOOKEEPER, "QuorumPeerMain");
			roleProcessMap.put(Role.OOZIESERVER, "Bootstrap");
			roleProcessMap.put(Role.SOLRSERVICE, "jar");

			roleProcessMap.put(Role.AGENT, "Agent"); // To be changed for
														// AnkushAgent as it
														// is hard-coded for
														// all technologies
			roleProcessMap.put(Role.GMETAD, "gmetad");
			roleProcessMap.put(Role.GMOND, "gmond");

			ROLE_PROCESS_MAP = roleProcessMap;
		}

		/**
		 * To get the comma separated services for the given role.
		 * 
		 * @param role
		 *            the role
		 * @return comma separated services.
		 */
		public static String getProcessName(String role) {
			return ROLE_PROCESS_MAP.get(role);
		}

		/**
		 * Gets the role name.
		 * 
		 * @param javaProcess
		 *            the java process
		 * @return the role name
		 */
		public static String getRoleName(String javaProcess) {
			for (String role : ROLE_PROCESS_MAP.keySet()) {
				String process = ROLE_PROCESS_MAP.get(role);
				if (process.equalsIgnoreCase(javaProcess)) {
					return role;
				}
			}
			return "";
		}
	}

	/**
	 * The Class JavaProcessScriptFile.
	 */
	class JavaProcessScriptFile {

		/** The javaprocessscriptfile. */
		static Map<String, String> JAVAPROCESSSCRIPTFILE = new HashMap<String, String>();

		static {
			Map<String, String> procesFileMap = new HashMap<String, String>();
			procesFileMap.put(Role.NAMENODE.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP);
			procesFileMap.put(Role.DATANODE.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP);
			procesFileMap.put(Role.JOBTRACKER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP);
			procesFileMap.put(Role.TASKTRACKER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP);
			procesFileMap.put(Role.RESOURCEMANAGER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_YARN);
			procesFileMap.put(Role.WEBAPPPROXYSERVER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_YARN);

			procesFileMap.put(Role.NODEMANAGER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_YARN);
			procesFileMap.put(Role.SECONDARYNAMENODE.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP);
			procesFileMap.put(Role.JOBHISTORYSERVER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_MRJOBHISTORY);
			procesFileMap.put(Role.DFSZKFAILOVERCONTROLLER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP);
			procesFileMap.put(Role.JOURNALNODE.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HADOOP);
			procesFileMap.put(Role.HBASEMASTER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HBASE);
			procesFileMap.put(Role.HBASEREGIONSERVER.toLowerCase(),
					Constant.Hadoop.FileName.SCRIPT_DAEMON_FILE_HBASE);
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
	 * The Interface Role.
	 */
	interface Role {

		/** The agent. */
		String AGENT = "Agent";

		/** The gmetad. */
		String GMETAD = "GangliaMaster";

		/** The gmond. */
		String GMOND = "gmond";

		/** The adminnode. */
		String ADMINNODE = "AdminNode";

		/** The storagenode. */
		String STORAGENODE = "StorageNode";

		/** The repnode. */
		String REPNODE = "RepNode";

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

		/** The hbasemaster. */
		String HBASEMASTER = "HBaseMaster";

		/** The hbaseregionserver. */
		String HBASEREGIONSERVER = "HBaseRegionServer";

		/** The hiveserver. */
		String HIVESERVER = "HiveServer";

		/** The dfszkfailovercontroller. */
		String DFSZKFAILOVERCONTROLLER = "zkfc";

		/** The oozieserver. */
		String OOZIESERVER = "OozieServer";

		/** The solrservice. */
		String SOLRSERVICE = "SolrService";

		/** The nimbus. */
		String NIMBUS = "Nimbus";

		/** The supervisor. */
		String SUPERVISOR = "Supervisor";

		/** The zookeeper. */
		String ZOOKEEPER = "Zookeeper";

		/** The kafka. */
		String KAFKA = "Kafka";

		/** The elastic search node role. */
		String ELASTICSEARCH = "ElasticSearch";

		/** The Cassandra Non Seed role. */
		String CASSANDRA_NON_SEED = "CassandraNonSeed";

		/** The Cassandra Seed role. */
		String CASSANDRA_SEED = "CassandraSeed";

		/**
		 * The Interface Regex.
		 */
		interface Regex {

			/** The adminnode. */
			String ADMINNODE = "admin";

			/** The storagenode. */
			String STORAGENODE = "sn";

			/** The repnode. */
			String REPNODE = "rg";
		}
	}

	/**
	 * The Interface Tile.
	 */
	interface Tile {

		/**
		 * The Interface Status.
		 */
		interface Status {

			/** The normal. */
			String NORMAL = "Normal";

			/** The warning. */
			String WARNING = "Warning";

			/** The critical. */
			String CRITICAL = "Critical";

			/** The error. */
			String ERROR = "Error";
		}

		/**
		 * The Interface Type.
		 */
		interface Type {

			/** The small text. */
			String SMALL_TEXT = "small_text";

			/** The big text. */
			String BIG_TEXT = "big_text";
		}

		/**
		 * The Interface Data.
		 */
		interface Data {

			/** The LINE1. */
			String LINE1 = "line1";

			/** The LINE2. */
			String LINE2 = "line2";

			/** The LINE3. */
			String LINE3 = "line3";

			/** The tile lines. */
			String TILE_LINES = "tileLines";

			/** The Header. */
			String Header = "header";

		}

		interface Url {

			/** The Node List. */
			String NODE_LIST = "Node List";
		}
	}

	/**
	 * The Interface Hadoop.
	 */
	interface Hadoop {

		/**
		 * The Interface Keys.
		 */
		interface Keys {

			String MONITORING_TABLE_DATA_KEY = "data";

			String JMX_DATA_KEY_BEANS = "beans";

			String JMX_DATA_KEY_BEAN_NAME = "name";

			String DISPLAY_NAME_ACTIVE_TASKTRACKERS_COUNT = "Active TaskTrackers";

			String DISPLAY_NAME_ACTIVE_NODEMANAGERS_COUNT = "Active NodeManagers";

			String DISPLAY_NAME_LIVE_DATANODES_COUNT = "Live DataNodes";

			/**
			 * The Interface NameNodeJmxInfo.
			 */
			interface NameNodeJmxInfo {
				String TOTAL = "Total";
				String USED = "Used";
				String NONDFSUSEDSPACE = "NonDfsUsedSpace";
				String FREE = "Free";
				String PERCENTUSED = "PercentUsed";
				String PERCENTREMAINING = "PercentRemaining";
				String TOTALFILES = "TotalFiles";
				String TOTALBLOCKS = "TotalBlocks";
				String STARTTIME = "StartTime";
			}

			interface JobTrackerJmxInfo {
				String SLOTS_TOTAL_MAP = "map_slots";
				String SLOTS_TOTAL_REDUCE = "reduce_slots";
				String JOBS_RUNNING = "jobs_running";
				String JOBS_SUBMITTED = "jobs_submitted";
				String JOBS_COMPLETED = "jobs_completed";
				String TRACKERS_TOTAL = "trackers";
				String TRACKERS_BLACKLISTED = "trackers_blacklisted";
				String TRACKERS_DECOMMISSIONED = "trackers_decommissioned";
				String STARTTIME = "StartTime";
			}

			interface DataNodeJmxInfo {
				String REMAINING = "Remaining";
				String CAPACITY = "Capacity";
				String DFSUSED = "DfsUsed";
			}

			interface TaskTrackerJmxInfo {
				String MAPS_RUNNING = "maps_running";
				String REDUCES_RUNNING = "reduces_running";
				String TASKS_COMPLETED = "tasks_completed";
				String MAP_TASK_SLOTS = "mapTaskSlots";
				String REDUCE_TASK_SLOTS = "reduceTaskSlots";
			}

			/**
			 * The Interface ResourceManagerJmxInfo.
			 */
			interface ResourceManagerJmxInfo {
				String TOTAL = "Total";
				String USED = "Used";
				String NONDFSUSEDSPACE = "NonDfsUsedSpace";
				String FREE = "Free";
				String PERCENTUSED = "PercentUsed";
				String PERCENTREMAINING = "PercentRemaining";
				String TOTALFILES = "TotalFiles";
				String TOTALBLOCKS = "TotalBlocks";
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
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_MAP,
									"Total Map Slots");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.SLOTS_TOTAL_REDUCE,
									"Total Reduce Slots");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.JOBS_RUNNING,
									"Running Jobs");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.JOBS_SUBMITTED,
									"Submiited Jobs");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.JOBS_COMPLETED,
									"Completed Jobs");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_TOTAL,
									"Total TaskTrackers");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_BLACKLISTED,
									"Blacklisted TaskTrackers");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.JobTrackerJmxInfo.TRACKERS_DECOMMISSIONED,
									"Decommissioned TaskTrackers");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.JobTrackerJmxInfo.STARTTIME,
							"JobTracker Started");

					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

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
							.put(Constant.Hadoop.Keys.TaskTrackerJmxInfo.MAPS_RUNNING,
									"Maps Running");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.TaskTrackerJmxInfo.REDUCES_RUNNING,
									"Reduces Running");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.TaskTrackerJmxInfo.REDUCE_TASK_SLOTS,
									"Reduce Task Slots");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.TaskTrackerJmxInfo.MAP_TASK_SLOTS,
									"Map Task Slots");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.TaskTrackerJmxInfo.TASKS_COMPLETED,
									"Tasks Completed");
					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

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
							Constant.Hadoop.Keys.NameNodeJmxInfo.TOTAL,
							"Configured Capacity");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.NameNodeJmxInfo.USED,
							"DFS Used");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.NameNodeJmxInfo.NONDFSUSEDSPACE,
									"Non-Dfs Used");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.NameNodeJmxInfo.TOTALFILES,
							"Total Files");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.NameNodeJmxInfo.FREE,
							"DFS Remaining");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.NameNodeJmxInfo.PERCENTUSED,
							"DFS Used %");
					keyDisplayNameMap
							.put(Constant.Hadoop.Keys.NameNodeJmxInfo.PERCENTREMAINING,
									"DFS Remaining %");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.NameNodeJmxInfo.TOTALBLOCKS,
							"Total Blocks");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.NameNodeJmxInfo.STARTTIME,
							"NameNode Started");

					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

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
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.DataNodeJmxInfo.CAPACITY,
							"Configured Capacity");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.DataNodeJmxInfo.DFSUSED,
							"DFS Used");
					keyDisplayNameMap.put(
							Constant.Hadoop.Keys.DataNodeJmxInfo.REMAINING,
							"Remaining Capacity");
					KEY_DISPLAY_NAME_MAP = keyDisplayNameMap;
				}

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

		/**
		 * The Interface Vendors.
		 */
		interface Vendors {

			/** The cloudera. */
			String CLOUDERA = "Cloudera";

			/** The apache. */
			String APACHE = "Apache";
		}

		/**
		 * The Interface FileName.
		 */
		interface FileName {

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
			String SCRIPT_ENV_HADOOP = "hadoop-env.sh";

			/** The yarn-env.sh. */
			String SCRIPT_ENV_YARN = "yarn-env.sh";

			/** The mapred-env.sh. */
			String SCRIPT_ENV_MAPRED = "mapred-env.sh";

			/** The hbase-env.sh. */
			String SCRIPT_ENV_HBASE = "hbase-env.sh";

			/** The script hbase start. */
			String SCRIPT_HBASE_START = "start-hbase.sh";

			/** The script hbase stop. */
			String SCRIPT_HBASE_STOP = "stop-hbase.sh";

			/** The hadoop-daemon.sh. */
			String SCRIPT_DAEMON_FILE_HBASE = "hbase-daemon.sh";

			String XML_HBASE_SITE = "hbase-site.xml";

			/** The hadoop-daemons.sh. */
			String SCRIPT_DAEMONS_FILE_HBASE = "hbase-daemons.sh";

			/** The hadoop-daemon.sh. */
			String SCRIPT_DAEMON_FILE_HADOOP = "hadoop-daemon.sh";

			/** The hadoop-daemons.sh. */
			String SCRIPT_DAEMONS_FILE_HADOOP = "hadoop-daemons.sh";

			/** The hadoop-daemon.sh. */
			String SCRIPT_DAEMON_FILE_MRJOBHISTORY = "mr-jobhistory-daemon.sh";

			/** The hadoop-daemon.sh. */
			String SCRIPT_DAEMON_FILE_YARN = "yarn-daemon.sh";

			/** The start-dfs.sh. */
			String SCRIPT_DFS_START = "start-dfs.sh";

			/** The stop-dfs.sh. */
			String SCRIPT_DFS_STOP = "stop-dfs.sh";

			/** The start-yarn.sh. */
			String SCRIPT_YARN_START = "start-yarn.sh";

			/** The stop-yarn.sh. */
			String SCRIPT_YARN_STOP = "stop-yarn.sh";

			/** The Constant start-mapred.sh. */
			String SCRIPT_MAPRED_START = "start-mapred.sh";

			/** The Constant stop-mapred.sh. */
			String SCRIPT_MAPRED_STOP = "stop-mapred.sh";

			/** The Constant HADOOP_MAPRED_STOP. */
			String METRICS_PROPERTIES_FILE = "hadoop-metrics.properties";

			/** The Constant hadoop-metrics2.properties. */
			String METRICS2_PROPERTIES_FILE = "hadoop-metrics2.properties";
		}

		/**
		 * The Interface Command.
		 */
		interface Command {

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

			/** The Constant COMMAND_START. */
			String START = "start";

			/** The stop. */
			String STOP = "stop";

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
	}

	/**
	 * The interface Cassandra.
	 */
	interface Cassandra {

		/** The SimpleStrategy. */
		String SIMPLE_STRATEGY = "SimpleStrategy";

		/** The SimpleStrategy. */
		String NETWORK_TOPOLOGY_STRATEGY = "NetworkTopologyStrategy";

		/** The JmxPort. */
		Integer JMX_PORT = 7199;
	}

	/**
	 * The interface Cassandra.
	 */
	interface Cassandra_JMX_Attributes {

		/** The Keyspaces. */
		String CASSANDRA_JMX_ATTRIBUTE_KEYSPACES = "Keyspaces";

		/** The Caches. */
		String CASSANDRA_JMX_OBJECT_CACHES = "Caches";

		/** The KeyCacheCapacityInBytes. */
		String CASSANDRA_JMX_ATTRIBUTE_KCC = "KeyCacheCapacityInBytes";

		/** The KeyCacheEntries. */
		String CASSANDRA_JMX_ATTRIBUTE_KCE = "KeyCacheEntries";

		/** The KeyCacheHits. */
		String CASSANDRA_JMX_ATTRIBUTE_KCH = "KeyCacheHits";

		/** The KeyCacheRecentHitRate. */
		String CASSANDRA_JMX_ATTRIBUTE_KCRHR = "KeyCacheRecentHitRate";

		/** The KeyCacheRequests. */
		String CASSANDRA_JMX_ATTRIBUTE_KCR = "KeyCacheRequests";

		/** The KeyCacheSavePeriodInSeconds. */
		String CASSANDRA_JMX_ATTRIBUTE_KCSPIS = "KeyCacheSavePeriodInSeconds";

		/** The KeyCacheSize. */
		String CASSANDRA_JMX_ATTRIBUTE_KCS = "KeyCacheSize";

		/** The RowCacheCapacityInBytes. */
		String CASSANDRA_JMX_ATTRIBUTE_RCC = "RowCacheCapacityInBytes";

		/** The RowCacheEntries. */
		String CASSANDRA_JMX_ATTRIBUTE_RCE = "RowCacheEntries";

		/** The RowCacheHits. */
		String CASSANDRA_JMX_ATTRIBUTE_RCH = "RowCacheHits";

		/** The RowCacheRecentHitRate. */
		String CASSANDRA_JMX_ATTRIBUTE_RCRHR = "RowCacheRecentHitRate";

		/** The RowCacheRequests. */
		String CASSANDRA_JMX_ATTRIBUTE_RCR = "RowCacheRequests";

		/** The RowCacheSavePeriodInSeconds. */
		String CASSANDRA_JMX_ATTRIBUTE_RCSPIS = "RowCacheSavePeriodInSeconds";

		/** The StorageService. */
		String CASSANDRA_JMX_OBJECT_STORAGESERVICE = "StorageService";

		/** The Ownership. */
		String CASSANDRA_JMX_ATTRIBUTE_OWNERSHIP = "Ownership";

		/** The HostIdMap. */
		String CASSANDRA_JMX_ATTRIBUTE_HOSTID = "HostIdMap";

		/** The UnreachableNodes. */
		String CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES = "UnreachableNodes";

		/** The LoadMap. */
		String CASSANDRA_JMX_ATTRIBUTE_LOADMAP = "LoadMap";

		/** The SimpleStates. */
		String CASSANDRA_JMX_ATTRIBUTE_SIMPLESTATES = "SimpleStates";

		/** The getTokens. */
		String CASSANDRA_JMX_ATTRIBUTE_TOKENS = "getTokens";

		/** The LiveSSTableCount. */
		String CASSANDRA_JMX_ATTRIBUTE_LIVESSTABLECOUNT = "LiveSSTableCount";

		/** The PendingTasks. */
		String CASSANDRA_JMX_ATTRIBUTE_PENDINGTASKS = "PendingTasks";

		/** The TotalReadLatencyMicros. */
		String CASSANDRA_JMX_ATTRIBUTE_READLATENCY = "TotalReadLatencyMicros";

		/** The TotalWriteLatencyMicros. */
		String CASSANDRA_JMX_ATTRIBUTE_WRITELATENCY = "TotalWriteLatencyMicros";

		/** The BloomFilterDiskSpaceUsed. */
		String CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERDISKSPACEUSED = "BloomFilterDiskSpaceUsed";

		/** The BloomFilterFalsePositives. */
		String CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERFALSEPOSITIVES = "BloomFilterFalsePositives";

		/** The BloomFilterFalseRatio. */
		String CASSANDRA_JMX_ATTRIBUTE_BLOOMFILTERFALSERATIO = "BloomFilterFalseRatio";

		/** The CompactionStrategyClass. */
		String CASSANDRA_JMX_ATTRIBUTE_COMPACTIONSTRATEGYCLASS = "CompactionStrategyClass";

		/** The CompressionRatio. */
		String CASSANDRA_JMX_ATTRIBUTE_COMPRESSIONRATIO = "CompressionRatio";

		/** The CompressionParameters. */
		String CASSANDRA_JMX_ATTRIBUTE_COMPRESSIONPARAMETERS = "CompressionParameters";

		/** The DroppableTombstoneRatio. */
		String CASSANDRA_JMX_ATTRIBUTE_DROPPABLETOMBSTONERATIO = "DroppableTombstoneRatio";

		/** The LiveDiskSpaceUsed. */
		String CASSANDRA_JMX_ATTRIBUTE_LIVEDISKSPACEUSED = "LiveDiskSpaceUsed";

		/** The MaxRowSize. */
		String CASSANDRA_JMX_ATTRIBUTE_MAXROWSIZE = "MaxRowSize";

		/** The MeanRowSize. */
		String CASSANDRA_JMX_ATTRIBUTE_MEANROWSIZE = "MeanRowSize";

		/** The MaximumCompactionThreshold. */
		String CASSANDRA_JMX_ATTRIBUTE_MAXCOMPACTIONTHRESHOLD = "MaximumCompactionThreshold";

		/** The MinimumCompactionThreshold. */
		String CASSANDRA_JMX_ATTRIBUTE_MINCOMPACTIONTHRESHOLD = "MinimumCompactionThreshold";

		/** The MemtableColumnsCount. */
		String CASSANDRA_JMX_ATTRIBUTE_MEMTABLECOLUMNSCOUNT = "MemtableColumnsCount";

		/** The MemtableDataSize. */
		String CASSANDRA_JMX_ATTRIBUTE_MEMTABLEDATASIZE = "MemtableDataSize";

		/** The MemtableSwitchCount. */
		String CASSANDRA_JMX_ATTRIBUTE_MEMTABLESWITCHCOUNT = "MemtableSwitchCount";

		/** The MinRowSize. */
		String CASSANDRA_JMX_ATTRIBUTE_MINROWSIZE = "MinRowSize";

		/** The ReadCount. */
		String CASSANDRA_JMX_ATTRIBUTE_READCOUNT = "ReadCount";

		/** The RecentBloomFilterFalsePositives. */
		String CASSANDRA_JMX_ATTRIBUTE_RECENTBLOOMFILTERFALSEPOSITIVES = "RecentBloomFilterFalsePositives";

		/** The RecentBloomFilterFalseRatio. */
		String CASSANDRA_JMX_ATTRIBUTE_RECENTBLOOMFILTERFALSERATIOS = "RecentBloomFilterFalseRatio";

		/** The RecentReadLatencyMicros. */
		String CASSANDRA_JMX_ATTRIBUTE_RECENTREADLATENCYMICROS = "RecentReadLatencyMicros";

		/** The RecentWriteLatencyMicros. */
		String CASSANDRA_JMX_ATTRIBUTE_RECENTWRITELATENCYMICROS = "RecentWriteLatencyMicros";

		/** The TotalDiskSpaceUsed. */
		String CASSANDRA_JMX_ATTRIBUTE_TOTALDISKSPACEUSED = "TotalDiskSpaceUsed";

		/** The TotalReadLatencyMicros. */
		String CASSANDRA_JMX_ATTRIBUTE_TOTALREADLATENCYMICROS = "TotalReadLatencyMicros";

		/** The TotalWriteLatencyMicros. */
		String CASSANDRA_JMX_ATTRIBUTE_TOTALWRITELATENCYMICROS = "TotalWriteLatencyMicros";

		/** The UnleveledSSTables. */
		String CASSANDRA_JMX_ATTRIBUTE_UNLEVELEDSSTABLES = "UnleveledSSTables";

		/** The WriteCount. */
		String CASSANDRA_JMX_ATTRIBUTE_WRITECOUNT = "WriteCount";

		/** The RowCacheSize. */
		String CASSANDRA_JMX_ATTRIBUTE_RCS = "RowCacheSize";

		/** The getEndpointState. */
		String CASSANDRA_JMX_ATTRIBUTE_ENDPOINTSTATE = "getEndpointState";

		/** The FailureDetector. */
		String CASSANDRA_JMX_OBJECT_FAILUREDETECTOR = "FailureDetector";

	}

	/**
	 * The interface Cassandra.
	 */
	interface Cassandra_Actions {

		/** The Nodetool. */
		String CASSANDRA_NODETOOL = "nodetool";

		/** The Cleanup. */
		String CASSANDRA_CLEANUP = "Cleanup";

		/** The Compact. */
		String CASSANDRA_COMPACT = "Compact";

		/** The Flush. */
		String CASSANDRA_FLUSH = "Flush";

		/** The Invalidate Keycache. */
		String CASSANDRA_INVALIDATE_KEY_CACHE = "Invalidate Keycache";

		/** The Invalidate Rowcache. */
		String CASSANDRA_INVALIDATE_ROW_CACHE = "Invalidate Rowcache";

		/** The Reset Local Schema. */
		String CASSANDRA_RESET_LOCAL_SCHEMA = "Reset Local Schema";

		/** The Pause Handoff. */
		String CASSANDRA_PAUSE_HANDOFF = "Pause Handoff";

		/** The Resume Handoff. */
		String CASSANDRA_RESUME_HANDOFF = "Resume Handoff";

		/** The Scrub. */
		String CASSANDRA_SCRUB = "Scrub";

		/** The Repair. */
		String CASSANDRA_REPAIR = "Repair";

		/** The Refresh. */
		String CASSANDRA_REFRESH = "Refresh";

		/** The Decommission. */
		String CASSANDRA_NODE_DECOMMISSION = "Decommission";

		/** The Drain. */
		String CASSANDRA_NODE_DRAIN = "Drain";

		/** The Disable Binary. */
		String CASSANDRA_DISABLE_NATIVE_TRANSPORT = "Disable Binary";

		/** The Enable Binary. */
		String CASSANDRA_ENABLE_NATIVE_TRANSPORT = "Enable Binary";

		/** The Disable Handoff. */
		String CASSANDRA_DISABLE_HANDOFF = "Disable Handoff";

		/** The Enable Handoff. */
		String CASSANDRA_ENABLE_HANDOFF = "Enable Handoff";

		/** The Disable Gossip. */
		String CASSANDRA_DISABLE_GOSSIP = "Disable Gossip";

		/** The Enable Gossip. */
		String CASSANDRA_ENABLE_GOSSIP = "Enable Gossip";

		/** The Disable Thrift. */
		String CASSANDRA_DISABLE_THRIFT = "Disable Thrift";

		/** The Enable Thrift. */
		String CASSANDRA_ENABLE_THRIFT = "Enable Thrift";

	}

	/**
	 * The interface Cassandra.
	 */
	interface Cassandra_Configuration_Files {

		/** The cassandra.yaml. */
		String CASSANDRA_FILE_YAML = "cassandra.yaml";

		/** The cassandra-topology.properties. */
		String CASSANDRA_FILE_TOPOLOGY = "cassandra-topology.properties";

		/** The log4j-server.properties. */
		String CASSANDRA_FILE_LOG4JSERVER = "log4j-server.properties";

	}

	/**
	 * The Interface Status.
	 */
	interface Status {

		/** The failed. */
		String FAILED = "DeploymentFailed";

		/** The deployed. */
		String DEPLOYED = "Deployed";

		/** The deploying. */
		String DEPLOYING = "Deploying";

		/** The running. */
		String RUNNING = "Running";

		/** The stopped. */
		String STOPPED = "Stopped";

		/** The ok. */
		String OK = "Ok";

		/** The normal. */
		String NORMAL = "Normal";
	}

	/**
	 * The Interface Mail.
	 */
	interface Mail {

		/** The footer message. */
		String FOOTER_MESSAGE = "Note: This is a system generated email. Please do not reply to this message. For further assistance contact your Ankush administrator.";
	}

	/**
	 * The Interface User.
	 */
	interface User {

		/**
		 * The Interface Role.
		 */
		interface Role {

			/** The role super user. */
			String ROLE_SUPER_USER = "ROLE_SUPER_USER";
		}
	}

	/**
	 * The interface Kafka_Properties.
	 */
	interface Kafka_Properties {

		/** The brokerid. */
		String BROKER_ID = "broker.id";

		/** The enable.zookeeper. */
		// String Enable_Zookeeper = "enable.zookeeper";

		/** The ZKCONNECT. */
		String Zookeeper_Connect = "zookeeper.connect";

		/** The port. */
		String PORT = "port";

		/** The log_dir. */
		String LOG_DIR = "log.dirs";

		/** The default_replication_factor. */
		String DEFAULT_REPLICATION_FACTOR = "default.replication.factor";

		/** The num network threads. */
		String NUM_NETWORK_THREADS = "num.network.threads";

		/** The num io threads. */
		String NUM_IO_THREADS = "num.io.threads";

		/** The queued max requests. */
		String QUEUED_MAX_REQUESTS = "queued.max.requests";

		/** The num partitions. */
		String NUM_PARTITIONS = "num.partitions";

		/** The log retentition hours. */
		String LOG_RETENTITION_HOURS = "log.retention.hours";

		/** The log retentition bytes. */
		String LOG_RETENTITION_BYTES = "log.retention.bytes";

		/** The log cleanup interval mins. */
		String LOG_CLEANUP_INTERVAL_MINS = "log.cleanup.interval.mins";

		/** The log flush interval message. */
		String LOG_FLUSH_INTERVAL_MESSAGE = "log.flush.interval.messages";

		/** The log flush schedular interval ms. */
		String LOG_FLUSH_SCHEDULAR_INTERVAL_MS = "log.flush.scheduler.interval.ms";

		/** The log flush interval ms. */
		String LOG_FLUSH_INTERVAL_MS = "log.flush.interval.ms";

		/** The controlled shutdown enable. */
		String CONTROLLED_SHUTDOWN_ENABLE = "controlled.shutdown.enable";

		/** The controlled shutdown max retries. */
		String CONTROLLED_SHUTDOWN_MAX_RETRIES = "controlled.shutdown.max.retries";

		/** The state change logger. */
		String STATE_CHANGE_LOGGER = "log4j.logger.state.change.logger";

		/** The logger kafka controller. */
		String LOGGER_KAFKA_CONTROLLER = "log4j.logger.kafka.controller";

		/** The kafka request logger. */
		String KAFKA_REQUEST_LOGGER = "log4j.logger.kafka.request.logger";

		/** The logger network requestchannel. */
		String LOGGER_NETWORK_REQUESTCHANNEL = "log4j.logger.kafka.network.RequestChannel$";
	}

	/**
	 * The interface Kafka_Keys.
	 */
	interface Kafka_Keys {

		/** The topic. */
		String TOPIC = "topic:";

		/** The partition. */
		String PARTITION = "partition:";

		/** The leader. */
		String LEADER = "leader:";

		/** The replicas. */
		String REPLICAS = "replicas:";

		/** The isr. */
		String ISR = "isr:";

		/** The partition id. */
		String PARTITION_ID = "partitionId";

		/** The leader id. */
		String LEADER_ID = "leaderId";

		/** The replicasid. */
		String REPLICASID = "replicasId";

		/** The isrid. */
		String ISRID = "isrId";

		/** The topic name. */
		String TOPIC_NAME = "topicName";

		/** The zk nodes. */
		String ZK_NODES = "zkNodes";

		/** The zk port. */
		String ZK_PORT = "zkPort";

		/** The request logger. */
		String REQUEST_LOGGER = "requestLogger";

		/** The logger kafka controller. */
		String LOGGER_KAFKA_CONTROLLER = "loggerKafkaController";

		/** The state change logger. */
		String STATE_CHANGE_LOGGER = "stateChangeLogger";

		/** The LOGGE r_ network_ requestchannel. */
		String LOGGER_Network_REQUESTCHANNEL = "loggerRequestChannel";

		/** The log level. */
		String LOG_LEVEL = "logLevel";
	}

	/**
	 * The interface Zookeeper_Keys.
	 */
	interface Zookeeper_Keys {

		/** The ZK nodes. */
		String ZKNodes = "zkNodes";

		/** The zkport. */
		String ZKPORT = "zkPort";
	}

	/**
	 * The Interface SSHUtils_Keys.
	 */
	interface SSHUtils_Keys {

		/** The output. */
		String OUTPUT = "output";

		/** The status. */
		String STATUS = "status";
	}

	/**
	 * The interface Parameter_Status.
	 * 
	 */
	interface Parameter_Status {
		/** The none status. */
		String NONE = "none";

		/** The add status. */
		String ADD = "add";

		/** The edit status. */
		String EDIT = "edit";

		/** The delete status. */
		String DELETE = "delete";
	}

	/**
	 * The Interface Process.
	 */
	interface Process {

		/** The kafka. */
		String KAFKA = "Kafka";

		/** The elasticsearch. */
		String ELASTICSEARCH = "ElasticSearch";
	}

	/**
	 * The Interface OracleNoSQL.
	 */
	interface OracleNoSQL {

		/**
		 * The Interface StorageNode.
		 */
		interface StorageNode {

			/** The editable parameters. */
			List<String> EDITABLE_PARAMETERS = Arrays.asList("haPortRange",
					"servicePortRange", "capacity", "numCPUs", "memoryMB",
					"mgmtClass", "mgmtPollPort", "mgmtTrapHost",
					"mgmtTrapPort", "serviceLogFileCount",
					"serviceLogFileLimit", "rnHeapPercent");
		}

		/**
		 * The Interface RepNode.
		 */
		interface RepNode {

			/** The editable parameters. */
			List<String> EDITABLE_PARAMETERS = Arrays.asList(
					"configProperties", "cacheSize", "javaMiscParams",
					"loggingConfigProps", "cacheMode", "rnMountPoint",
					"rnCachePercent", "maxTrackedLatency", "statsInterval",
					"collectEnvStats", "throughputFloor");
		}

		/**
		 * The Interface Admin.
		 */
		interface Admin {

			/** The editable parameters. */
			List<String> EDITABLE_PARAMETERS = Arrays.asList(
					"adminLogFileCount", "adminLogFileLimit", "eventExpiryAge",
					"configProperties", "javaMiscParams", "loggingConfigProps",
					"collectorPollPeriod");
		}

		/**
		 * The Interface Policy.
		 */
		interface Policy {

			/** The editable parameters. */
			List<String> EDITABLE_PARAMETERS = Arrays.asList(
					"adminLogFileCount", "adminLogFileLimit", "cacheSize",
					"collectEnvStats", "collectorPollPeriod",
					"configProperties", "eventExpiryAge", "javaMiscParams",
					"latencyCeiling", "loggingConfigProps",
					"maxTrackedLatency", "rnCachePercent", "rnHeapPercent",
					"serviceLogFileCount", "serviceLogFileLimit",
					"statsInterval", "throughputFloor");
		}

		/**
		 * The Interface AllRepNodes.
		 */
		interface AllRepNodes {

			/** The editable parameters. */
			List<String> EDITABLE_PARAMETERS = Arrays.asList("cacheSize",
					"collectEnvStats", "configProperties", "javaMiscParams",
					"loggingConfigProps", "maxTrackedLatency", "statsInterval");
		}

	}

	/**
	 * The Interface ElasticSearch.
	 */
	interface ElasticSearch {

		/**
		 * The Interface Keys.
		 */
		interface Keys {

			/** The tiles. */
			String TILES = "tiles";

			/** The shards. */
			String SHARDS = "shards";

			/** The aliases. */
			String ALIASES = "aliases";

			/** The index. */
			String INDEX = "index";

			/** The alias. */
			String ALIAS = "alias";

			/** The setting. */
			String SETTING = "settings";

			/** The number of shards. */
			String NUMBER_OF_SHARDS = "number_of_shards";

			/** The number of replicas. */
			String NUMBER_OF_REPLICAS = "number_of_replicas";

			/** The flush. */
			String FLUSH = "flush";

			/** The clear cache. */
			String CLEAR_CACHE = "clearcache";

			/** The optmize. */
			String OPTMIZE = "optimize";

			/** The refresh. */
			String REFRESH = "refresh";

			/** The close. */
			String CLOSE = "close";

			/** The delete. */
			String DELETE = "delete";

			/** The open. */
			String OPEN = "open";

			/**
			 * The Interface JVM.
			 */
			interface JVM {

				/** The heap used. */
				String HEAP_USED = "Heap Used";

				/** The heap committed. */
				String HEAP_COMMITTED = "Heap Committed";

				/** The non heap used. */
				String NON_HEAP_USED = "Non Heap Used";

				/** The non heap committed. */
				String NON_HEAP_COMMITTED = "Non Heap Committed";

				/** The thread count peak. */
				String THREAD_COUNT_PEAK = "Thread Count/Peak";

				/** The gc count. */
				String GC_COUNT = "GC Count";

				/** The java version. */
				String JAVA_VERSION = "Java Version";

				/** The jvm vendor. */
				String JVM_VENDOR = "JVM Vendor";

				/** The jvm. */
				String JVM = "JVM";
			}

			/**
			 * The Interface Indices.
			 */
			interface Indices {

				/** The documents. */
				String DOCUMENTS = "Documents";

				/** The documents deleted. */
				String DOCUMENTS_DELETED = "Documents Deleted";

				/** The store size. */
				String STORE_SIZE = "Store Size";

				/** The index req total. */
				String INDEX_REQ_TOTAL = "Index Req Total";

				/** The delete req total. */
				String DELETE_REQ_TOTAL = "Delete Req Total";

				/** The get req total. */
				String GET_REQ_TOTAL = "Get Req Total";

				/** The get exists total. */
				String GET_EXISTS_TOTAL = "Get(Exists) Total";

				/** The get missing total. */
				String GET_MISSING_TOTAL = "Get(Missing) Total";

				/** The quert total. */
				String QUERT_TOTAL = "Query Total";

				/** The fetch total. */
				String FETCH_TOTAL = "Fetch Total";
			}

			/**
			 * The Interface OS.
			 */
			interface OS {

				/** The total memory. */
				String TOTAL_MEMORY = "Total Memory";

				/** The total swap. */
				String TOTAL_SWAP = "Total Swap";

				/** The memory used free. */
				String MEMORY_USED_FREE = "Memory(Used/Free)";

				/** The swap used free. */
				String SWAP_USED_FREE = "Swap(Used/Free)";

				/** The cpu user sys. */
				String CPU_USER_SYS = "CPU User/Sys";

				/** The cpu idle. */
				String CPU_IDLE = "CPU Idle";

				/** The cpu vendor. */
				String CPU_VENDOR = "CPU Vendor";

				/** The cpu model. */
				String CPU_MODEL = "CPU Model";

				/** The total cores. */
				String TOTAL_CORES = "Total Cores";
			}

			/**
			 * The Interface Process.
			 */
			interface Process {

				/** The open file descriptors. */
				String OPEN_FILE_DESCRIPTORS = "Open File Descriptors";

				/** The resident memory. */
				String RESIDENT_MEMORY = "Resident Memory";

				/** The shared memory. */
				String SHARED_MEMORY = "Shared Memory";

				/** The total virtual memory. */
				String TOTAL_VIRTUAL_MEMORY = "Total Virtual Memory";
			}

			/**
			 * The Interface Thread_Pool.
			 */
			interface Thread_Pool {

				/** The index. */
				String INDEX = "Index (Queue/Peak/Active)";

				/** The get. */
				String GET = "Get (Queue/Peak/Active)";

				/** The serch. */
				String SERCH = "Search (Queue/Peak/Active)";

				/** The bulk. */
				String BULK = "Bulk (Queue/Peak/Active)";

				/** The refresh. */
				String REFRESH = "Refresh (Queue/Peak/Active)";

				/** The flush. */
				String FLUSH = "Flush (Queue/Peak/Active)";

				/** The merge. */
				String MERGE = "Merge (Queue/Peak/Active)";

				/** The management. */
				String MANAGEMENT = "Management (Queue/Peak/Active)";
			}

			/**
			 * The Interface Network.
			 */
			interface Network {

				/** The http address. */
				String HTTP_ADDRESS = "HTTP Address";

				/** The http bound address. */
				String HTTP_BOUND_ADDRESS = "HTTP Bound Address";

				/** The http publish address. */
				String HTTP_PUBLISH_ADDRESS = "HTTP Publish Address";

				/** The transport address. */
				String TRANSPORT_ADDRESS = "Transport Address";

				/** The transport bound address. */
				String TRANSPORT_BOUND_ADDRESS = "Transport Bound Address";

				/** The transport publish address. */
				String TRANSPORT_PUBLISH_ADDRESS = "Transport Publish Address";
			}

			/**
			 * The Interface File_System.
			 */
			interface File_System {

				/** The path. */
				String PATH = "Path";

				/** The mount. */
				String MOUNT = "Mount";

				/** The device. */
				String DEVICE = "Device";

				/** The total space. */
				String TOTAL_SPACE = "Total Space";

				/** The free space. */
				String FREE_SPACE = "Free Space";

				/** The disk reads. */
				String DISK_READS = "Disk Reads";

				/** The disk writes. */
				String DISK_WRITES = "Disk Writes";

				/** The read size. */
				String READ_SIZE = "Read Size";

				/** The write size. */
				String WRITE_SIZE = "Write Size";
			}

			/**
			 * The Interface Documents.
			 */
			interface Documents {

				/** The docs. */
				String DOCS = "Documents";

				/** The max docs. */
				String MAX_DOCS = "Max Documents";

				/** The deleted docs. */
				String DELETED_DOCS = "Deleted Documents";

				/** The primary size. */
				String PRIMARY_SIZE = "Primary Size";

				/** The total size. */
				String TOTAL_SIZE = "Total Size";
			}

			/**
			 * The Interface Operations.
			 */
			interface Operations {

				/** The refresh total. */
				String REFRESH_TOTAL = "Refresh Total";

				/** The refresh time. */
				String REFRESH_TIME = "Refresh Time";

				/** The flush total. */
				String FLUSH_TOTAL = "Flush Total";

				/** The flush time. */
				String FLUSH_TIME = "Flush Time";
			}

			/**
			 * The Interface Merge_Activity.
			 */
			interface Merge_Activity {

				/** The merge total. */
				String MERGE_TOTAL = "Merge Total";

				/** The merge total time. */
				String MERGE_TOTAL_TIME = "Merge Total Time";

				/** The merge total docs. */
				String MERGE_TOTAL_DOCS = "Merge Total Docs";

				/** The merge total size. */
				String MERGE_TOTAL_SIZE = "Merge Total Size";
			}

			/**
			 * The Interface Search_Total.
			 */
			interface Search_Total {

				/** The query total. */
				String QUERY_TOTAL = "Query Total";

				/** The query time. */
				String QUERY_TIME = "Query Time";

				/** The fetch total. */
				String FETCH_TOTAL = "Fetch Total";

				/** The fetch time. */
				String FETCH_TIME = "Fetch Time";
			}

			/**
			 * The Interface Indexing_Total.
			 */
			interface Indexing_Total {

				/** The index total. */
				String INDEX_TOTAL = "Index Total";

				/** The index time. */
				String INDEX_TIME = "Index Time";

				/** The delete total. */
				String DELETE_TOTAL = "Delete Total";

				/** The delete time. */
				String DELETE_TIME = "Delete Time";
			}

			/**
			 * The Interface Get_Total.
			 */
			interface Get_Total {

				/** The get total. */
				String GET_TOTAL = "Get Total";

				/** The get time. */
				String GET_TIME = "Get Time";

				/** The exists total. */
				String EXISTS_TOTAL = "Exists Total";

				/** The exists time. */
				String EXISTS_TIME = "Exists Time";

				/** The missing total. */
				String MISSING_TOTAL = "Missing Total";

				/** The missing time. */
				String MISSING_TIME = "Missing Time";
			}

		}

		/**
		 * The Interface Cluster_Health.
		 */
		interface Cluster_Health {

			/** The active primary shards. */
			String ACTIVE_PRIMARY_SHARDS = "active_primary_shards";

			/** The initializing shards. */
			String INITIALIZING_SHARDS = "initializing_shards";

			/** The relocating shards. */
			String RELOCATING_SHARDS = "relocating_shards";

			/** The unassigned shards. */
			String UNASSIGNED_SHARDS = "unassigned_shards";

			/** The number of nodes. */
			String NUMBER_OF_NODES = "number_of_nodes";

			/** The number of data nodes. */
			String NUMBER_OF_DATA_NODES = "number_of_data_nodes";

			/** The status. */
			String STATUS = "status";

			/** The timed out. */
			String TIMED_OUT = "timed_out";
		}

		/**
		 * The Interface Cluster_State.
		 */
		interface Cluster_State {

			/** The master node. */
			String MASTER_NODE = "master_node";

			/** The nodes. */
			String NODES = "nodes";
		}

		/**
		 * The Interface Index_Administration_Keys.
		 */
		interface Index_Administration_Keys {

			/** The flush. */
			String FLUSH = "_flush";

			/** The cache clear. */
			String CACHE_CLEAR = "_cache/clear";

			/** The optimize. */
			String OPTIMIZE = "_optimize";

			/** The refresh. */
			String REFRESH = "_refresh";

			/** The close. */
			String CLOSE = "_close";

			/** The open. */
			String OPEN = "_open";
		}
	}

	/**
	 * The Interface Severity.
	 */
	interface Severity {

		/** The normal. */
		String NORMAL = "Normal";

		/** The critical. */
		String CRITICAL = "Critical";
	}

	/**
	 * The Interface Method_Type.
	 */
	interface Method_Type {

		/** The get. */
		String GET = "GET";

		/** The post. */
		String POST = "POST";

		/** The delete. */
		String DELETE = "DELETE";
	}

	/**
	 * The Interface Log.
	 */
	interface Log {

		interface Level {
			/** The all. */
			String ALL = "ALL";

			/** The debug. */
			String DEBUG = "DEBUG";

			/** The error. */
			String ERROR = "ERROR";

			/** The fatal. */
			String FATAL = "FATAL";

			/** The info. */
			String INFO = "INFO";

			/** The warn. */
			String WARN = "WARN";

			/** The off. */
			String OFF = "OFF";

			/** The trace. */
			String TRACE = "TRACE";
		}
	}
}
