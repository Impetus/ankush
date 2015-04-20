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

import java.util.HashMap;
import java.util.Map;

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
			WAITING, INPROGRESS, COMPLETED, ERROR, FAILED
		}
	}

	interface Cluster {
		enum State {
			DEPLOYING, DEPLOYED, ERROR, REMOVING, MAINTENANCE, ADD_NODE, REMOVE_NODE, SERVER_CRASHED, CRITICAL, WARNING
		}

		enum Operation {
			DEPLOY, REMOVE, START_CLUSTER, STOP_CLUSTER, START_COMPONENT, STOP_COMPONENT, START_ROLE, STOP_ROLE, START_NODE, STOP_NODE, START_SERVICES, STOP_SERVICES, ADD_NODE, REMOVE_NODE
		}

		enum InstallationType {
			SUDO
		}
	}

	interface Service {

		/** App Conf Service *. */
		String APPCONF = "appConfService";
	}

	interface JsonKeys {

		interface Logs {

			String ROLES = "roles";

			String NODES = "nodes";
		}
	}

	interface Node {

		enum State {
			DEPLOYING, DEPLOYED, ERROR, ADDING, REMOVING, SERVER_CRASHED
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

			/** The process memory. */
			String PROCESS_MEMORY = "processMemory";

			/** The process cpu. */
			String PROCESS_CPU = "processCPU";
		}
	}

	enum ServiceAction {
		START, STOP
	}

	/**
	 * The Interface Alerts.
	 */
	interface Alerts {

		/**
		 * The Interface Severity.
		 */
		interface Severity {

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
		 * The Interface Metric.
		 */
		interface Metric {

			/** The cpu. */
			String CPU = "CPU";

			/** The memory. */
			String MEMORY = "Memory";
		}
	}

	interface Component {

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

			/** The Cassandra. */
			String CASSANDRA = "Cassandra";
		}
		
		enum State {
			DEPLOYING, DEPLOYED, ERROR, REMOVING, MAINTENANCE, ADD_NODE, REMOVE_NODE, MIGRATE, UPGRADING
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

		/** The str comma. */
		String COMMA = ",";

		/** The str colon. */
		String COLON = ":";

		/** The Constant /etc/environment file. */
		String ETCENV_FILE = "/etc/environment";

		String TEXT_COMMENT_START = "# start_settings_";

		String TEXT_COMMENT_END = "# end_settings_";

		String TEXT_COMMENT_START_ENV_SETTINGS = "# ANKUSH ENVIRONMENT SECTION START, DO NOT EDIT OR DELETE";

		String TEXT_COMMENT_END_ENV_SETTINGS = "# ANKUSH ENVIRONMENT SECTION END";

		interface ExceptionsMessage {

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
		 * Service Type.
		 */
		interface ServiceType {
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

		interface Key {
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

			/** The node id. */
			String NODE_ID = "NODE_ID";

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

		String REGISTER_LEVEL = "registerLevel";

		/** Conf Keys *. */
		String CONFKEY = "confKey";

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

		/** The datacenters. */
		String DATACENTERS = "datacenters";

		/** The nodedata. */
		String NODEDATA = "nodeData";

		/** The totalrack. */
		String TOTALRACKS = "totalRacks";

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

		/** The publicip. */
		String PUBLICIP = "publicIp";

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

		/** The components. */
		String COMPONENTS = "components";

		/** The password. */
		String PASSWORD = "password";

		/** The username. */
		String USERNAME = "username";

		/** The privatekey. */
		String PRIVATEKEY = "privateKey";

		/** The action. */
		String ACTION = "action";

		/** The services. */
		String SERVICES = "services";

		/** The type. */
		String TYPE = "type";

		/** The files. */
		String FILES = "files";

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

		/** The file pattern. */
		String PATTERN = "pattern";

		/** The component. */
		String COMPONENT = "component";

		/** The port. */
		String PORT = "port";

		/** The all. */
		String ALL = "ALL";

		/** The severity *. */
		String SEVERITY = "severity";

		/** Name *. */
		String NAME = "name";

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

		String SERVICE = "service";
		String STOP = "stop";
		String HA = "ha";

		String KEY_INITIAL_DELAY_AGENT_CHECK = "agent.initialdelay";

		String AGENT_DOWN_INTERVAL = "agent.down.interval";

		String LOG_FILE_PATH = "logFilePath";
	}

	interface Role {

		String ZOOKEEPER = "Zookeeper";

		String GMETAD = "GangliaMaster";

		String GMOND = "gmond";

		String CASSANDRA_SEED = "CassandraSeed";

		String CASSANDRA_NON_SEED = "CassandraNonSeed";

		String AGENT = "Agent";

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

	interface ServiceType {

		String PS = "ps";

		String JPS = "jps";
	}

	interface Process {

		String CASSANDRA = "CassandraDaemon";

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

		/** The monitoring. */
		String MONITORING = "nodeMonitoringManager";

		/** The node. */
		String NODE = "nodeManager";

		/** The cluster. */
		String CLUSTER = "clusterManager";

		/** The service. */
		String SERVICE = "serviceManager";

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

	public enum RegisterLevel {
		LEVEL1, LEVEL2, LEVEL3
	}

	interface Graph {

		/**
		 * Graph Type Enum.
		 */
		public enum Type {

			/** CPU, memory, network and load type *. */
			cpu,
			/** The network. */
			network,
			/** The memory. */
			memory,
			/** The load. */
			load,
			/** The invalid. */
			invalid;

			/**
			 * From string.
			 * 
			 * @param string
			 *            the string
			 * @return the operation
			 */
			public static Type fromString(String string) {
				try {
					return Type.valueOf(string);
				} catch (Exception ex) {
					return invalid;
				}
			}
		}

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
	 * The Interface Mail.
	 */
	interface Mail {

		/** The footer message. */
		String FOOTER_MESSAGE = "Note: This is a system generated email. Please do not reply to this message. For further assistance contact your Ankush administrator.";
	}

	/**
	 * The Interface Server.
	 */
	interface Server {

		/** The config. */
		String CONFIG = "appConfig";
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

			roleProcessMap.put(Role.CASSANDRA_SEED, "CassandraDaemon");
			roleProcessMap.put(Role.CASSANDRA_NON_SEED, "CassandraDaemon");
			roleProcessMap.put(Role.ZOOKEEPER, "QuorumPeerMain");

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
	 * The Interface App.
	 */
	interface App {

		/**
		 * The Interface State.
		 */
		interface State {

			/** The configure. */
			String CONFIGURE = "configure";

			/** The change password. */
			String CHANGE_PASSWORD = "changepassword";

		}
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
}
