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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.impetus.ankush2.constant.Constant.Strings;

/**
 * The Interface Constant.
 * 
 * @author nikunj
 */
public interface Constant {

	/** The Constant DOTS. */
	String DOTS = "...";

	/** The Constant .bashrc file. */
	String BASHRC_FILE = "~/.bashrc";

	/** The Constant /etc/environment file. */
	String ETCENV_FILE = "/etc/environment";

	String TEXT_COMMENT_START = "# ankush_start_settings_";

	String TEXT_COMMENT_END = "# ankush_end_settings_";

	String TEXT_COMMENT_START_ENV_SETTINGS = "# ANKUSH ENVIRONMENT SECTION START, DO NOT EDIT OR DELETE";

	String TEXT_COMMENT_END_ENV_SETTINGS = "# ANKUSH ENVIRONMENT SECTION END";

	/** The root user. */
	String ROOT_USER = "root";

	/** The validating. */
	String VALIDATING = "Validating ";

	/** The str space. */
	String STR_SPACE = " ";

	/**
	 * API to get ganglia metrics display name.
	 * 
	 * @author hokam
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
		 * @param name
		 *            the name
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
		/** dir usage monitor for ganglia rrd creation. **/
		String DIR_USAGE_MONITOR = "com.impetus.ankush.agent.utils.DirectoryUsageMonitor";

		/** The Hadoop NameNode Role Update class for Hadoop 2 with HA enabled. */
		String Hadoop_HA_Update_NN_Role = "com.impetus.ankush.agent.hadoop.UpdateNameNodeRole";

		/** The Hadoop Job Monitoring class for Hadoop 1. */
		String Hadoop_JOB_STATUS_MONITOR = "com.impetus.ankush.agent.utils.JobStatusMonitor";

		String SERVICE_MONITOR = "com.impetus.ankush.agent.service.ServiceMonitor";
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
	 * The interface FileTypeExtension.
	 */
	interface FileTypeExtension {

		/** The config. */
		String CONFIG = ".config";

		/** The xml. */
		String XML = ".xml";

		/** The properties. */
		String PROPERTIES = ".properties";

		/** The yaml. */
		String YAML = ".yaml";
	}

	/**
	 * The Constant Interface for Ganglia.
	 */
	interface Ganglia {
		/**
		 * Process Constant interface.
		 */
		interface PROCESS {
			// Ganglia Meta daemon
			/** The gmetad. */
			String GMETAD = "gmetad";
			// Ganglia monitor daemon.
			/** The gmond. */
			String GMOND = "gmond";
		}
	}

	/**
	 * The Interface Agent.
	 */
	interface Agent {
		/**
		 * Action handler and action names.
		 * 
		 * @author hokam
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

		/** The agent down message. */
		String AGENT_DOWN_MESSAGE = "Ankush Agent is down on some nodes. Please resolve related issue.";

		/** The Constant AGENT_PROPERTY_FILE_PATH. */
		String AGENT_PROPERTY_FILE_PATH = ".ankush/agent/conf/agent.properties";

		/** The Constant AGENT_TASKABLE_FILE_PATH. */
		String AGENT_TASKABLE_FILE_PATH = ".ankush/agent/conf/taskable.conf";

		/** The agent start script. */
		String AGENT_START_SCRIPT = "$HOME/.ankush/agent/bin/start-agent.sh";

		/** The agent stop script. */
		String AGENT_STOP_SCRIPT = "$HOME/.ankush/agent/bin/stop-agent.sh";

		/** The agent service conf folder. */
		String AGENT_SERVICE_CONF_FOLDER = "$HOME/.ankush/agent/conf/services/";

		/** The agent conf path. */
		String AGENT_CONF_PATH = "$HOME/.ankush/agent/conf/";

		/** The agent haservice conf path. */
		String AGENT_HASERVICE_CONF_PATH = "$HOME/.ankush/agent/conf/HAService.xml";
	}

	/** The thread pool size. */
	int THREAD_POOL_SIZE = Integer.MAX_VALUE;

	/** The colon. */
	String COLON = ":";

	/** The slash n. */
	String SLASH_N = "\n";

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

			/** The change password. */
			String CHANGE_PASSWORD = "changepassword";

		}
	}

	/**
	 * The Interface Technology.
	 */
	interface Technology {

		/** The hadoop. */
		String HADOOP = "Hadoop";

		/** The hadoop2. */
		String HADOOP2 = "Hadoop2";

		/** The cassandra. */
		String CASSANDRA = "Cassandra";

		/** The zookeeper. */
		String ZOOKEEPER = "Zookeeper";

		/** The Hybrid. */
		String HYBRID = "Hybrid";

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
			String ERROR = com.impetus.ankush2.constant.Constant.Keys.ERROR;

			/** The warning. */
			String WARNING = "warning";

			/** The down. */
			String DOWN = "Down";
		}

		/** The parameter config service. */
		String PARAMETER_CONFIG_SERVICE = "parameterConfigService";

		/** The parameter config service. */
		String CASSANDRA_PARAMETER_CONFIG_SERVICE = "cassandraParameterConfigService";

		/** App Conf Service *. */
		String APPCONF = "appConfService";
	}

	/**
	 * The Interface Cluster.
	 */
	interface Cluster {

		/**
		 * The Interface State.
		 */
		interface State {

			/** Deployment states. */
			String MAINTENANCE = "Maintenance";

			/** Deployment states. */
			String DEPLOYING = "deploying";

			/** The registering. */
			String REGISTERING = "registering";

			/** The deployed. */
			String DEPLOYED = "deployed";

			/** The error. */
			String ERROR = com.impetus.ankush2.constant.Constant.Keys.ERROR;

			/** The error. */
			String SERVER_CRASHED = "SERVER_CRASHED";

			/** The down. */
			String DOWN = "down";

			/** The warning. */
			String WARNING = "warning";

			/** The running. */
			String RUNNING = "running";

			/** The critical. */
			String CRITICAL = "critical";

			/** Deleting state. */
			String REMOVING = "removing";

			/** Adding Nodes state. */
			String ADDING_NODES = "Adding Nodes";

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

			/** The agent. */
			String AGENT = "Agent";

			/** The quorumpeermain. */
			String QUORUMPEERMAIN = "QuorumPeerMain";

			/** The CassandraDaemon. */
			String CASSANDRA = "CassandraDaemon";

			/** RABBITMQ Process Name. */
			String RABBITMQ = "beam";
		}

		/**
		 * The Interface DeploymentState.
		 */
		interface DeploymentState {

			/** The adding node. */
			String ADDING_NODE = "AddingNode";

			/** The removing node. */
			String REMOVING_NODE = "RemovingNode";

			/** The add node failed. */
			String ADD_NODE_FAILED = "AddNodeFailed";

			/** The notstarted. */
			String NOTSTARTED = "NotStarted";

			/** The failed. */
			String FAILED = "Failed";

			/** The completed. */
			String COMPLETED = "Completed";

			/** The inprogress. */
			String INPROGRESS = "InProgress";
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

			/** The validation. */
			String VALIDATION = "validating";

			/** The validation failed. */
			String VALIDATION_FAILED = "validationFailed";

			/** The deploying. */
			String DEPLOYING = "deploying";

			/** The adding. */
			String ADDING = "adding";

			/** The adding error. */
			String ADDING_ERROR = "addingError";

			/** The deployed. */
			String DEPLOYED = "deployed";

			/** The deployed failed. */
			String DEPLOYED_FAILED = "deploymentFailed";

			/** The error. */
			String ERROR = com.impetus.ankush2.constant.Constant.Keys.ERROR;

			/** The removing. */
			String REMOVING = "removing";

			/** The running. */
			String RUNNING = "running";

			/** The server crashed. */
			String SERVER_CRASHED = "SERVER_CRASHED";
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

	/**
	 * The Class ComponentName.
	 */
	class ComponentName {

		/** The service name map. */
		static Map<String, String> componentRoleMap = new HashMap<String, String>();

		static {
			componentRoleMap
					.put(com.impetus.ankush2.constant.Constant.Component.Name.GANGLIA,
							Constant.Role.GMETAD + Strings.COMMA
									+ Constant.Role.GMOND);

			componentRoleMap
					.put(com.impetus.ankush2.constant.Constant.Component.Name.CASSANDRA,
							Constant.Role.CASSANDRA_SEED + Strings.COMMA
									+ Constant.Role.CASSANDRA_NON_SEED);

			componentRoleMap
					.put(com.impetus.ankush2.constant.Constant.Component.Name.ZOOKEEPER,
							Constant.Role.ZOOKEEPER);


		}

		/**
		 * Gets the component role map.
		 * 
		 * @param component
		 *            the component
		 * @return the component role map
		 */
		public static String getComponentRoleMap(String component) {
			return componentRoleMap.get(component);
		}

		/**
		 * Gets the component roles.
		 * 
		 * @param component
		 *            the component
		 * @return the component roles.
		 */
		public static Set<String> getComponentRoles(String component) {
			// Get component role string from map.
			String rolesString = Constant.ComponentName
					.getComponentRoleMap(component);
			// List of components.
			Set<String> roles = new HashSet<String>();
			// If Component role string not null.
			if (rolesString != null) {
				// add in roles.
				roles = new HashSet<String>(Arrays.asList(rolesString
						.split(Strings.COMMA)));
			}
			// return roles
			return roles;
		}

		/**
		 * Gets the component name.
		 * 
		 * @param role
		 *            the role
		 * @return the component name
		 */
		public static String getComponentName(String role) {
			for (String component : componentRoleMap.keySet()) {
				String componentRoleString = Constant.ComponentName
						.getComponentRoleMap(component);
				if (componentRoleString != null) {
					List<String> componentRoleList = Arrays
							.asList(componentRoleString.split(Strings.COMMA));
					if (componentRoleList.contains(role)) {
						return component;
					}
				}
			}
			return null;
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

	/**
	 * Implementation for getting the services of a given role.
	 * 
	 * @author hokam
	 * 
	 */
	class RoleServices {

		/** The services. */
		static Map<String, String> SERVICES = new HashMap<String, String>();

		static {
			Map<String, String> services = new HashMap<String, String>();

			// Common Roles
			services.put(Role.NAMENODE, "NameNode");
			services.put(Role.DATANODE, "DataNode");
			services.put(Role.SECONDARYNAMENODE, "SecondaryNameNode");

			// Roles for Hadoop 0.x & 1.x
			services.put(Role.JOBTRACKER, "JobTracker");
			services.put(Role.TASKTRACKER, "TaskTracker");

			// Roles for Hadoop 2.x
			services.put(Role.RESOURCEMANAGER, "ResourceManager");
			services.put(Role.NODEMANAGER, "NodeManager");
			services.put(Role.JOURNALNODE, "JournalNode");
			services.put(Role.JOBHISTORYSERVER, "JobHistoryServer");
			services.put(Role.DFSZKFAILOVERCONTROLLER, "zkfc");

			services.put(Role.SEED_NODE, "CassandraDaemon");
			services.put(Role.NON_SEED_NODE, "CassandraDaemon");
			services.put(Role.ZOOKEEPER, "QuorumPeerMain");

			services.put(Role.AGENT, "Agent");

			SERVICES = services;
		}

		/**
		 * To get the comma separated services for the given role.
		 * 
		 * @param role
		 *            the role
		 * @return comma separated services.
		 */
		public static String getServices(String role) {
			return SERVICES.get(role);
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

			roleProcessMap.put(Role.SEED_NODE, "CassandraDaemon");
			roleProcessMap.put(Role.NON_SEED_NODE, "CassandraDaemon");
			roleProcessMap.put(Role.ZOOKEEPER, "QuorumPeerMain");

			roleProcessMap.put(Role.AGENT, "Agent"); // To be changed for
														// AnkushAgent as it
														// is hard-coded for
														// all technologies
			roleProcessMap.put(Role.GMETAD, "gmetad");
			roleProcessMap.put(Role.GMOND, "gmond");

			roleProcessMap.put(Role.RABBIT_MQ,
					Constant.Component.ProcessName.RABBITMQ);
			

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
	 * The Interface Role.
	 */
	interface Role {

		/** Roles Separator *. */
		String SEPARATOR = "/";

		/** The agent. */
		String AGENT = "Agent";

		/** The gmetad. */
		String GMETAD = "GangliaMaster";

		/** The gmond. */
		String GMOND = "gmond";

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

		/** The zookeeper. */
		String ZOOKEEPER = "Zookeeper";

		/** The Cassandra Non Seed role. */
		String CASSANDRA_NON_SEED = "CassandraNonSeed";

		/** The Cassandra Seed role. */
		String CASSANDRA_SEED = "CassandraSeed";

		/** The NonSeedNode role. */
		String NON_SEED_NODE = "CassandraNonSeed";

		/** The SeedNode role. */
		String SEED_NODE = "CassandraSeed";

		/** The Rabbit MQ role. */
		String RABBIT_MQ = "RabbitMQ";		
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

			/** The LIN e1. */
			String LINE1 = "line1";

			/** The LIN e2. */
			String LINE2 = "line2";

			/** The LIN e3. */
			String LINE3 = "line3";

			/** The tile lines. */
			String TILE_LINES = "tileLines";

			/** The Header. */
			String Header = "header";

		}

		/**
		 * The Interface Url.
		 */
		interface Url {

			/** The Node List. */
			String NODE_LIST = "Node List";
		}
	}

	/**
	 * The interface Cassandra.
	 */
	interface Cassandra {

		/** The Snitch. */
		String SNITCH = "Snitch";

		/** The Partitioner. */
		String PARTITIONER = "Partitioner";

		/** The SimpleStrategy. */
		String SIMPLE_STRATEGY = "SimpleStrategy";

		/** The SimpleStrategy. */
		String NETWORK_TOPOLOGY_STRATEGY = "NetworkTopologyStrategy";

		/** The SimpleSnitch. */
		String SIMPLE_SNITCH = "SimpleSnitch";

		/** The PropertyFileSnitch. */
		String PROPERTY_FILE_SNITCH = "PropertyFileSnitch";

		/** The RackInferringSnitch. */
		String RACK_INFERRING_SNITCH = "RackInferringSnitch";

		/** The GossipingPropertyFileSnitch. */
		String GOSSIPING_PROPERTY_FILE_SNITCH = "GossipingPropertyFileSnitch";

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

		/** The LoadMap. */
		String CASSANDRA_JMX_ATTRIBUTE_LOADMAP = "LoadMap";

		/** The HostIdMap. */
		String CASSANDRA_JMX_ATTRIBUTE_HOSTID = "HostIdMap";

		/** The UnreachableNodes. */
		String CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES = "UnreachableNodes";

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

		/** The Stop Cassandra Daemon. */
		String CASSANDRA_NODE_STOP_DAEMON = "Stop Daemon";

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

		/** The Disable Backup. */
		String CASSANDRA_NODE_DISABLEBACKUP = "Disable Backup";

		/** The Enable Backup. */
		String CASSANDRA_NODE_ENABLEBACKUP = "Enable Backup";

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

		/** The Enable Autocompaction. */
		String CASSANDRA_ENABLE_AUTOCOMPACTION = "Enable Autocompaction";

		/** The Disable Autocompaction. */
		String CASSANDRA_DISABLE_AUTOCOMPACTION = "Disable Autocompaction";

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

		/** The oozie process name *. */
		String OOZIE = "Bootstrap";

		/** The oozie process name *. */
		String ZOOKEEPER = "QuorumPeerMain";

		/** The cassandra. */
		String CASSANDRA = "CassandraDaemon";


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

		/**
		 * The Interface Level.
		 */
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


	/**
	 * The Interface operation.
	 */
	interface Operation {

		/**
		 * The Interface Keys.
		 */
		interface Keys {

			/** The operation name. */
			String NAME = "name";

			/** The status. */
			String STATUS = "status";

			/** The operation status. */
			String OPERATION_STATUS = "operationStatus";
		}

		/**
		 * The Interface Status.
		 */
		interface Status {
			/** The in progress. */
			String IN_PROGRESS = "InProgress";

			/** The failed. */
			String FAILED = "Failed";

			/** The completed. */
			String COMPLETED = "Completed";

		}

		/**
		 * The Interface Name.
		 */
		interface Name {

			/** The start cluster. */
			String START_CLUSTER = "startCluster";

			/** The stop cluster. */
			String STOP_CLUSTER = "stopCluster";

			/** The restart cluster. */
			String RESTART_CLUSTER = "restartCluster";
		}

		class OperationName {

			static Map<String, String> opDisplayNameMap = new HashMap<String, String>();

			static {
				opDisplayNameMap.put(Constant.Operation.Name.START_CLUSTER,
						"Start Cluster");
				opDisplayNameMap.put(Constant.Operation.Name.STOP_CLUSTER,
						"Stop Cluster");
				opDisplayNameMap.put(Constant.Operation.Name.RESTART_CLUSTER,
						"Restart Cluster");
			}

			public static String getOperationDisplayName(String opName) {
				return opDisplayNameMap.get(opName);
			}
		}
	}
}
