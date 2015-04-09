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
package com.impetus.ankush2.constant;

public interface Constant {

	interface AppStore {
		String COMPONENT_MAP = "componentConfiguration";

		interface CompConfigXmlMapping {

			String KEY_ANKUSH_CONSTANT_PROPERTY = "config.classes.supported.components";
			
			String KEY_APP_STORE_OBJECT = "cmpConfig";
			
			String HADOOP = "hadoopConfigXmlMapping";
		}
		
		interface ComponentConf {
			interface Key {
				String NAME = "name";
				String PRIORITY = "priority";
				String DEPLOYER = "deployer";
				String COMPONENT = "component";
				String MONITOR = "monitor";
				String SERVICE = "service";
			}
		}
	}

	interface Operation {
		enum Status {
			WAITING, INPROGRESS, COMPLETED, ERROR
		}
	}

	interface Cluster {
		enum State {
			DEPLOYING, DEPLOYED, ERROR, REMOVING, MAINTENANCE, ADD_NODE, REMOVE_NODE, SERVER_CRASHED, DOWN, CRITICAL, WARNING, REBALANCE, REDISTRIBUTE, CHANGE_REP_FACTOR
		}

		enum Operation {
			DEPLOY, REMOVE, START_CLUSTER, STOP_CLUSTER, RESTART_CLUSTER, START_COMPONENT, STOP_COMPONENT, START_ROLE, STOP_ROLE, START_NODE, STOP_NODE, START_SERVICES, STOP_SERVICES, ADD_NODE, REMOVE_NODE, MANAGE_SERVICE
		}

		enum InstallationType {
			SUDO, NONSUDO, CUSTOM
		}
	}

	interface Service {
		interface State {

			/** The hide. */
			String HIDE = "hide";

			/** The running. */
			String RUNNING = "running";

			/** The error. */
			String ERROR = com.impetus.ankush2.constant.Constant.Keys.ERROR;

			/** The warning. */
			String WARNING = "warning";

			/** The down. */
			String DOWN = "Down";
		}
	}

	interface JsonKeys {

		interface Logs {

			String COMPONENTS = "components";

			String ROLES = "roles";

			String NODES = "nodes";
		}
	}

	interface Node {

		enum State {
			DEPLOYING, DEPLOYED, ERROR, REMOMVING, MAINTENANCE, ADDING, REMOVING, UPGRADING, SERVER_CRASHED
		}

		interface ComponentConfigKeys {

			String ROLES = "roles";
		}

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
	}

	enum ServiceAction {
		START, STOP, RESTART
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
		int DEFAULT_REFRESH_INTERVAL = 30;

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

	interface Component {

		interface AdvanceConfKeys {

			/** The home dir. */
			String HOME_DIR = "homeDir";

			String CONF_DIR = "confDir";

			String LOGS_DIR = "logDir";

			String binDir = "binDir";
		}

		enum State {
			DEPLOYING, DEPLOYED, ERROR, REMOVING, MAINTENANCE, ADD_NODE, REMOVE_NODE, MIGRATE, UPGRADING
		}

		/**
		 * The Interface Name.
		 */
		interface Name {

			/** The agent. */
			String AGENT = "Agent";

			/** The hadoop. */
			String HADOOP = "Hadoop";

			/** The zookeeper. */
			String ZOOKEEPER = "Zookeeper";

			/** The ganglia. */
			String GANGLIA = "Ganglia";

			/** The dependency. */
			String DEPENDENCY = "Dependency";

			/** The Preprocessor. */
			String PREPROCESSOR = "Preprocessor";

			/** The Preprocessor. */
			String POSTPROCESSOR = "Postprocessor";

			/** The Custom Deployer. */
			String CUSTOMDEPLOYER = "CustomDeployer";

			/** The Cassandra. */
			String CASSANDRA = "Cassandra";

			String JAVA = "Java";
			
			String RABBITMQ = "RabbitMq";
			
		}

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

	interface Strings {

		/** The Constant LINE_SEPERATOR. */
		String LINE_SEPERATOR = System.getProperty("line.separator");

		/** The str space. */
		String SPACE = " ";

		/** The str forward slash. */
		String FORWARD_SLASH = "/";

		/** The str comma. */
		String COMMA = ",";

		/** The str left parenthesis. */
		String LEFT_PARENTHESIS = "(";

		/** The str right parenthesis. */
		String RIGHT_PARENTHESIS = ")";

		/** The str colon. */
		String COLON = ":";

		/** The str dot. */
		String DOT = ".";

		/** The Constant DOTS. */
		String DOTS = "...";

		/** The Constant .bashrc file. */
		String BASHRC_FILE = "~/.bashrc";

		/** The Constant /etc/environment file. */
		String ETCENV_FILE = "/etc/environment";

		String TEXT_COMMENT_START = "# start_settings_";

		String TEXT_COMMENT_END = "# end_settings_";

		String TEXT_COMMENT_START_ENV_SETTINGS = "# ANKUSH ENVIRONMENT SECTION START, DO NOT EDIT OR DELETE";

		String TEXT_COMMENT_END_ENV_SETTINGS = "# ANKUSH ENVIRONMENT SECTION END";

		String TEXT_COMMENT_ENV_SETTINGS = TEXT_COMMENT_START_ENV_SETTINGS
				+ "\\n\\n" + TEXT_COMMENT_END_ENV_SETTINGS;

		/** The Constant URI File Prefix for HDFS . */
		String URI_FILE_PREFIX = "file://";

		/** The root user. */
		String ROOT_USER = "root";

		/** The validating. */
		String VALIDATING = "Validating ";

		interface ExceptionsMessage {

			/** The general exception string. */
			String PROCESS_REQUEST = "Exception: Unable to process request.";

			String INVALID_CLUSTER_CONFIG_MSG = "Invalid ClusterConfig";

			String INVALID_COMPONENT_CONFIG_MSG = "Invalid ComponentConfig";

			String CONNECTION_NULL_STRING = "Could not connect to node.";

			String GENERIC_EXCEPTION_MSG = "There is some Exception. Please view server logs for further information.";

			String VIEW_SERVER_LOGS = " Please view server logs.";
		}

	}

	interface LinuxEnvFiles {

		/** The Constant .bashrc file. */
		String BASHRC = "~/.bashrc";

		/** The Constant /etc/environment file. */
		String ETC_ENVIRONMENT = "/etc/environment";

		String ETC_HOSTS = "/etc/hosts";
	}

	interface Response {
		interface Header {
			String STATUS = "status";
			String ERROR = "error";
		}
	}

	interface Agent {

		/**
		 * Action handler and action names.
		 */
		interface Action {

			/** The add. */
			String ADD = "add";

			/** The addforcestop. */
			String ADDFORCESTOP = "addForceStop";

			/** The delete. */
			String DELETE = "delete";

			/** The edit. */
			String EDIT = "edit";

			/**
			 * Action Handler names.
			 * 
			 * @author hokam
			 */
			interface Handler {

				/** The haconfig. */
				String HACONFIG = "haconfig";
			}
		}

		/**
		 * Service Type.
		 */
		interface ServiceType {
			// Service PS
			/** The ps. */
			String PS = "ps";
			// Service type jps
			/** The jps. */
			String JPS = "jps";
			// service type port.
			/** The port. */
			String PORT = "port";
			// service type pid.
			/** The pid. */
			String PID = "pid";
		}

		/**
		 * Parameters for service xml configuration.
		 * 
		 */
		interface ServiceParams {
			// pid file param
			/** The pidfile. */
			String PIDFILE = "pidFile";
			// port
			/** The port. */
			String PORT = "port";
		}

		/**
		 * The Interface TaskableClass.
		 */
		interface TaskableClass {
			/** dir usage monitor for ganglia rrd creation. **/
			String DIR_USAGE_MONITOR = "com.impetus.ankush.agent.utils.DirectoryUsageMonitor";

			String SERVICE_MONITOR = "com.impetus.ankush.agent.service.ServiceMonitor";
		}

		/** The agent down message. */
		String AGENT_DOWN_MESSAGE = "Ankush Agent is down on some nodes. Please resolve related issue.";
		String AGENT_TASKABLE_FILE_PATH = ".ankush/agent/conf/taskable.conf";
		String AGENT_PROPERTY_FILE_PATH = ".ankush/agent/conf/agent.properties";
		String AGENT_SERVICE_CONF_FOLDER = "$HOME/.ankush/agent/conf/services/";
		String AGENT_SERVICE_JMXTRANS = "$HOME/.ankush/agent/jmxtrans/";
		String AGENT_HASERVICE_CONF_PATH = "$HOME/.ankush/agent/conf/HAService.xml";
		String AGENT_START_SCRIPT = "$HOME/.ankush/agent/bin/start-agent.sh";
		String AGENT_STOP_SCRIPT = "$HOME/.ankush/agent/bin/stop-agent.sh";

		interface Key {
			String INSTALL_DIR_PROPERTY_KEY = "agent.install.dir";
			String AGENT_INSTALL_DIR = "agentInstallDir";
		}

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

			/** The cluster id. */
			String CLUSTER_ID = "CLUSTER_ID";

			/** The service conf dir. */
			String SERVICE_CONF_DIR = "SERVICE_CONF_DIR";

			/** Server IP *. */
			String SERVER_IP = "SERVER_IP";

			/** public ip *. */
			String HOST_PUBLIC_IP = "HOST_PUBLIC_IP";

			/** private IP *. */
			String HOST_PRIVATE_IP = "HOST_PRIVATE_IP";
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
		 * The Interface Url.
		 */
		interface Url {

			/** The Node List. */
			String NODE_LIST = "Node List";
		}
	}

	interface Registration {

		interface ErrorMsg {

			String BASIC_REGISTRATION = "Operation not allowed for Basic registration mode.";

			String MONITOR_ONLY_MODE = "Operation not allowed for MonitorOnly registration mode.";

			String NOT_MANAGED_MODE = "Operation allowed only for Managed registration mode.";
		}

	}

	/**
	 * The Interface Keys.
	 */
	interface Keys {

		interface Logs {

		}

		String REGISTER_LEVEL = "registerLevel";

		interface ComponentNodeProperties {
			String ROLES = "roles";
		}

		/**
		 * The Interface RegisterCluster.
		 */
		interface RegisterCluster {

			/** The component version. */
			String COMPONENT_VERSION = "componentVersion";

			/** The deploy component flag. */
			String DEPLOY_COMPONENT_FLAG = "deployComponentFlag";

			/** The component vendor. */
			String COMPONENT_VENDOR = "componentVendor";

			String DEPLOYMENT_TYPE = "deploymentType";

			String DEPLOYMENT_TYPE_BUNDLE = "Bundle";

			String DEPLOYMENT_TYPE_PACKAGE_MANAGER = "PackageManager";

			/** The registerable cluster. */
			String REGISTERABLE_CLUSTER = "registerableCluster";

			/** The cluster registration failed. */
			String CLUSTER_REGISTRATION_FAILED = "Cluster registration failed.";

			/**
			 * The Interface Ganglia.
			 */
			interface Ganglia {

				/** The grid name. */
				String GRID_NAME = "gridName";

				/** The cluster name. */
				String CLUSTER_NAME = "clusterName";

				/** The gmond conf path. */
				String GMOND_CONF_PATH = "gmondConfPath";

				/** The gmetad conf path. */
				String GMETAD_CONF_PATH = "gmetadConfPath";

				/** The polling interval. */
				String POLLING_INTERVAL = "pollingInterval";

				/** The port. */
				String PORT = "port";

				/** The rrd dir path. */
				String RRD_FILE_PATH = "rrdFilePath";

				/** The master node. */
				String MASTER_NODE = "masterNode";

			}
		}

		/** Conf Keys *. */
		String CONFKEY = "confKey";

		/** GRAPH VIEWS *. */
		String GRAPHVIEWS = "graphviews";

		/** Node graphs *. */
		String NODEGRAPHS = "nodegraphs";

		/** default graphs *. */
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
		String TOTALRACKS = "totalRacks";

		/** The totalDatacenter. */
		String TOTALDATACENTER = "totalDatacenter";

		/** The nodeid. */
		String NODEID = "nodeId";

		/** The totalnode. */
		String TOTALNODES = "totalNodes";

		/** The events. */
		String EVENTS = "events";

		/** The events. */
		String OPERATIONS = "operations";

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

		String CATEGORY = "category";

		/** The host. */
		String HOST = "host";

		/** The node. */
		String NODE = "node";

		/** The params. */
		String PARAMS = "params";

		/** The opName. */
		String OPERATION_NAME = "opName";

		/** The opCount. */
		String OPERATION_COUNT = "opCount";

		/** The operationid. */
		String OPERATIONID = "operationId";

		/** The logs. */
		String LOGS = "logs";

		/** The state. */
		String STATE = "state";

		/** The operationState. */
		String OPERATION_STATE = "operationState";

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

		/** The action. */
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

		/** The clusterNodes. */
		String CLUSTERNODES = "ClusterNodes";

		/** The jps process list. */
		String JPS_PROCESS_LIST = "JPS_PROCESS_LIST";

		/** The process port map. */
		String PROCESS_PORT_MAP = "PROCESS_PORT_MAP";

		/** The technology. */
		String TECHNOLOGY = "technology";

		/** The component. */
		String COMPONENT = "component";

		String COMPONENT_NAME = "componentName";

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

		/** The severity *. */
		String SEVERITY = "severity";

		/** Name *. */
		String NAME = "name";

		/** key *. */
		String KEY = "key";

		/** The subtype. */
		String SUBTYPE = "subType";

		/** The count. */
		String COUNT = "count";

		/** The up. */
		String UP = "Up";

		/** The source. */
		String SOURCE = "source";

		/** The propertyname. */
		String PROPERTYNAME = "propertyName";

		/** The date. */
		String DATE = "date";

		/** The revisionid. */
		String REVISIONID = "revisionId";

		/** The timestamp. */
		String TIMESTAMP = "timestamp";

		/** The serverip. */
		Object SERVERIP = "serverIP";

		/** The ankush. */
		String ANKUSH = "Ankush";

		/** The done. */
		String DONE = "Done";

		/** The inprogress. */
		String INPROGRESS = "In-Progress";

		/** The failed. */
		String FAILED = "Failed";

		String DOWN_SERVICES = "downServices";

		String GANGLIA_MASTER = "gangliaMaster";

		String GROUPING_TYPE = "groupingType";

		String SERVICE = "service";
		String STOP = "stop";
		String HA = "ha";

		String LOGVIEWER = "logviewer";

		/** The no parameter found string. */
		String NO_PARAMETER_FOUND_STRING = "No parameters found in"
				+ Strings.SPACE;

		/** The components status. */
		String COMPONENTS_STATUS = "componentsStatus";

		String KEY_INITIAL_DELAY_AGENT_CHECK = "agent.initialdelay";

		String AGENT_DOWN_INTERVAL = "agent.down.interval";

		/** The general exception string. */
		String GENERAL_EXCEPTION_STRING = "Exception: Unable to process request.";

		String HOME_DIR = "componentHome";

		String LOG_FILE_PATH = "logFilePath";
	}

	interface Role {

		String ZOOKEEPER = "Zookeeper";

		String GMETAD = "GangliaMaster";

		String GMOND = "gmond";

		String CASSANDRA_SEED = "CassandraSeed";

		String CASSANDRA_NON_SEED = "CassandraNonSeed";

		String AGENT = "Agent";


	}

	interface ServiceType {

		String PS = "ps";

		String JPS = "jps";

		String PORT = "port";

		String PID = "pid";
	}

	interface Process {

		String CASSANDRA = "CassandraDaemon";

		String APACHECTL = "apachectl";

		String QUORUMPEERMAIN = "QuorumPeerMain";

		String GMETAD = "gmetad";

		String GMOND = "gmond";

	}

	/**
	 * The Interface SystemCommands.
	 */
	interface SystemCommands {

		/** The gethostname. */
		String GETHOSTNAME = "hostname";
	}

	/**
	 * The Interface Manager.
	 */
	interface Manager {

		/** The user. */
		String USER = "userManager";

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

		/** App Cofnf manager *. */
		String APPCONF = "appConfManager";

		/** The tile. */
		String TILE = "tileManager";

		// HA service Database manager
		/** The HA service. */
		String HAService = "haServiceManager";

		/** The operation. */
		String OPERATION = "operationManager";
	}

	enum ParameterActionType {
		/** The none status. */
		NONE,

		/** The add status. */
		ADD,

		EDIT,

		DELETE
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

	public enum RegisterLevel {
		LEVEL1, LEVEL2, LEVEL3
	}
}
