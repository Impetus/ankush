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
package com.impetus.ankush.agent;

import java.util.Arrays;
import java.util.List;

/**
 * The Interface Constants.
 * 
 * @author Hokam Chauhan
 */
public interface Constant {

	/** The prop name node id. */
	String PROP_NAME_NODE_ID = "NODE_ID";

	/** The prop name url part. */
	String PROP_NAME_URL_PART = "URL_PART";

	/** The prop name host. */
	String PROP_NAME_HOST = "HOST";

	/** The prop name port. */
	String PROP_NAME_PORT = "PORT";

	/** The prop name url common. */
	String PROP_NAME_URL_COMMON = "URL_COMMON";

	/** The prop name common update time. */
	String PROP_NAME_COMMON_UPDATE_TIME = "COMMON_UPDATE_TIME";

	/** The prop name service url part. */
	String PROP_NAME_SERVICE_URL_PART = "SERVICE_URL_PART";

	/** The prop name service url last. */
	String PROP_NAME_SERVICE_URL_LAST = "SERVICE_URL_LAST";

	/** The prop name hadoop conf dir. */
	String PROP_NAME_HADOOP_CONF_DIR = "HADOOP_CONF_DIR";

	/** The prop name hadoop pid dir. */
	String PROP_NAME_HADOOP_PID_DIR = "HADOOP_PID_DIR";

	/** The prop name hadoop log dir. */
	String PROP_NAME_HADOOP_LOG_DIR = "HADOOP_LOG_DIR";

	/** The prop name hadoop home. */
	String PROP_NAME_HADOOP_HOME = "HADOOP_HOME";

	/** The prop name hadoop jar path. */
	String PROP_NAME_HADOOP_JAR_PATH = "HADOOP_JAR_PATH";

	/** The prop name service status update time. */
	String PROP_NAME_SERVICE_STATUS_UPDATE_TIME = "SERVICE_STATUS_UPDATE_TIME";

	/** The prop name cluster technology name. */
	String PROP_NAME_CLUSTER_TECHNOLOGY_NAME = "CLUSTER_TECHNOLOGY_NAME";

	/** The prop name top process count. */
	String PROP_NAME_TOP_PROCESS_COUNT = "TOP_PROCESS_COUNT";

	/** The prop name jps process list. */
	String PROP_NAME_JPS_PROCESS_LIST = "JPS_PROCESS_LIST";

	/** The prop name ganglia process list. */
	String PROP_NAME_GANGLIA_PROCESS_LIST = "GANGLIA_PROCESS_LIST";

	/** The prop name process port list. */
	String PROP_NAME_PROCESS_PORT_MAP = "PROCESS_PORT_MAP";
	
	/** The technology hadoop. */
	String TECHNOLOGY_HADOOP = "Hadoop";

	/** The technology oracle nosql. */
	String TECHNOLOGY_ORACLE_NOSQL = "Oracle NoSQL";

	/** The technology linux. */
	String TECHNOLOGY_LINUX = "Linux";

	/** The technology storm. */
	String TECHNOLOGY_STORM = "Storm";

	/** The technology cassandra. */
	String TECHNOLOGY_CASSANDRA = "Cassandra";

	/** The technology hadoop vendor. */
	String TECHNOLOGY_HADOOP_VENDOR = "HADOOP_VENDOR";

	/** The prop name job status update time. */
	String PROP_NAME_HADOOP_DATA_UPDATE_TIME = "HADOOP_DATA_UPDATE_TIME";

	/** The prop name job url part. */
	String PROP_NAME_JOB_URL_PART = "JOB_URL_PART";

	/** The prop name job url status. */
	String PROP_NAME_JOB_URL_STATUS = "URL_JOB_STATUS";
	
	String PROP_NAME_NAMENODE_ROLE = "URL_NAMENODE_ROLE";
	

	/** The prop namenode port. */
	String PROP_NAMENODE_PORT = "NAMENODE_PORT";

	/** The prop dfs port. */
	String PROP_DFS_PORT = "DFS_PORT";

	/** The prop namenode host. */
	String PROP_NAMENODE_HOST = "NAMENODE_IP";

	/** The prop namenode host. */
	String PROP_RESOURCEMANAGER_IP = "RESOURCEMANAGER_IP";

	/** The prop hostname. */
	String PROP_HOSTNAME = "HOSTNAME";

	/** The prop registry port. */
	String PROP_REGISTRY_PORT = "REGISTRY_PORT";

	/** The prop jarpath. */
	String PROP_JARPATH = "JARPATH";

	/** The prop snid. */
	String PROP_SNID = "SNID";

	/** The prop admin. */
	String PROP_ADMIN = "ADMIN";

	/** The prop seednode. */
	String PROP_SEEDNODE = "SEEDNODE";

	/** The kv jar path. */
	String KV_JAR_PATH = "KVJAR";

	/** The prop name upload file url. */
	String PROP_NAME_UPLOAD_FILE_URL = "UPLOAD_FILE_URL";

	/** The prop name topology url. */
	String PROP_NAME_MONITORING_URL = "URL_MONITORING";

	/** The host ip. */
	String HOST_IP = "HOST_IP";

	/** The cassandra home. */
	String CASSANDRA_HOME = "CASSANDRA_HOME";
	
	/** The node ip list. */
	String NODE_IP_LIST = "NODE_IP_LIST";

	/** The jmx port. */
	String JMX_PORT = "JMX_PORT";

	/** The rpc_port. */
	String RPC_PORT = "RPC_PORT";

	/** The prop name storm topology period. */
	String PROP_NAME_STORM_TOPOLOGY_PERIOD = "STORM_TOPOLOGY_PERIOD";

	/** The storm home. */
	String STORM_HOME = "STORM_HOME";

	/** The storm jar. */
	String STORM_JAR = "STORM_JAR";

	/** The storm lib. */
	String STORM_LIB = "STORM_LIB";

	/** The storm lib. */
	String DATA_DIR_LIST = "DATA_DIR_LIST";

	/** The prop name service status update time. */
	String DIR_USAGE_RRD_UPDATE_TIME = "DIR_USAGE_RRD_UPDATE_TIME";

	/** ganglia gmond conf relative path **/
	String GANGLIA_CONF_PATH = "GANGLIA_CONF_PATH";

	/** user home key **/
	String USER_HOME = "user.home";

	/** rrd dir group **/
	String RRD_GROUP = "RRD_GROUP";

	String PROP_HADOOP_REST_PORT = "HADOOP_REST_PORT";

	String PROP_NAMENODE_HTTP_PORT = "NAMENODE_HTTP_PORT";

	/**
	 * The splitter interface.
	 */
	interface Splitter {
		// comma splitter.
		String COMMA = ",";
		// dot spliter.
		String DOT = ".";
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
	
	interface ElasticSearch{
		/**
		 * The Interface Cluster_Health.
		 */
		interface ClusterHealth_Keys{
			
			/** The active primary shards. */
			String ACTIVE_PRIMARY_SHARDS = "active_primary_shards";
			
			String ACTIVE_SHARDS = "active_shards";
			
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
		 * The Interface Cluster_Health.
		 */
		interface ClusterHealth_Table_Keys{
			
			/** The active primary shards. */
			String PRIMARY_SHARDS = "Primary Shards";
			
			/** The initializing shards. */
			String INITIALIZING_SHARDS = "Initializing Shards";
			
			/** The relocating shards. */
			String RELOCATING_SHARDS = "Relocating Shards";
			
			/** The unassigned shards. */
			String UNASSIGNED_SHARDS = "Unassigned Shards";
			
			/** The number of nodes. */
			String NODES = "Nodes";
			
			/** The number of data nodes. */
			String DATA_NODES = "Data Nodes";
			
			/** The status. */
			String STATUS = "Status";
			
			/** The timed out. */
			String ACTIVE_SHARDS = "Active Shards";
		}
		
		interface Document_Keys {
			String NUM_DOCS = "num_docs"; 
			String MAX_DOCS = "max_doc"; 
			String DELETED_DOCS = "deleted_docs"; 
			String PRIMARY_SIZE = "primary_size";
			String SIZE = "size";
		}
		/**
		 * The Interface Documents.
		 */
		interface Documents_Table_Keys{
			
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
		
		interface Operations_Keys{
			String TOTAL = "total";
			String TOTAL_TIME = "total_time"; 
		}
		
		/**
		 * The Interface Operations.
		 */
		interface Operations_Table_Keys {
			
			/** The refresh total. */
			String REFRESH_TOTAL = "Refresh Total";
			
			/** The refresh time. */
			String REFRESH_TIME = "Refresh Time";
			
			/** The flush total. */
			String FLUSH_TOTAL = "Flush Total";
			
			/** The flush time. */
			String FLUSH_TIME = "Flush Time";
		}
		
		interface MergeActivity_Keys{
			
			String TOTAL = "total";
			String TOTAL_TIME = "total_time";
			String TOTAL_DOCS = "total_docs";
			String TOTAL_SIZE = "total_size";
//			"total","total_time","total_docs","total_size"
		}
		
		/**
		 * The Interface Merge_Activity.
		 */
		interface MergeActivity_Table_Keys {
			
			/** The merge total. */
			String MERGE_TOTAL = "Merge Total";
			
			/** The merge total time. */
			String MERGE_TOTAL_TIME = "Merge Total Time";
			
			/** The merge total docs. */
			String MERGE_TOTAL_DOCS = "Merge Total Docs";
			
			/** The merge total size. */
			String MERGE_TOTAL_SIZE = "Merge Total Size";
		}
		
		interface SearchTotal{
			String QUERY_TOTAL = "query_total";
			String QUERY_TIME = "query_time";
			String FETCH_TOTAL = "fetch_total";
			String FETCH_TIME = "fetch_time";
//			"query_total","query_time","fetch_total","fetch_time"
		}
		
		/**
		 * The Interface Search_Total.
		 */
		interface SearchTotal_Table_Keys {
			
			/** The query total. */
			String QUERY_TOTAL = "Query Total";
			
			/** The query time. */
			String QUERY_TIME = "Query Time";
			
			/** The fetch total. */
			String FETCH_TOTAL = "Fetch Total";
			
			/** The fetch time. */
			String FETCH_TIME = "Fetch Time";
		}
		
		interface IndexingTotal { 
			String INDEX_TOTAL = "index_total"; 
			String INDEX_TIME = "index_time"; 
			String DELETE_TOTAL = "delete_total";
			String DELETE_TIME = "delete_time";
//			"index_total","index_time","delete_total","delete_time"
		}
		
		/**
		 * The Interface Indexing_Total.
		 */
		interface IndexingTotal_Table_Keys {
			
			/** The index total. */
			String INDEX_TOTAL = "Index Total";
			
			/** The index time. */
			String INDEX_TIME = "Index Time";
			
			/** The delete total. */
			String DELETE_TOTAL = "Delete Total";
			
			/** The delete time. */
			String DELETE_TIME = "Delete Time";
		}
		
		interface GetTotal_Keys {
			String TOTAL = "total";
			String GET_TIME = "get_time"; 
			String EXISTS_TOTAL = "exists_total"; 
			String EXISTS_TIME = "exists_time";
			String MISSING_TOTAL = "missing_total";
			String MISSING_TIME = "missing_time";
//			"total","get_time","exists_total","exists_time","missing_total","missing_time"
		}
		
		/**
		 * The Interface Get_Total.
		 */
		interface GetTotal_Table_Keys {
			
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
		
		interface Shard_Keys{
			String ROUTING = "routing"; 
			String SHARD = "shard";
			String STATE = "state";
			String PRIMARY = "primary";
			String DOCS = "docs";
			String NUM_DOCS = "num_docs";
			String INDEX = "index";
			String SIZE = "size";
			String NODE = "node";
//			"routing","shard","state","primary","docs","num_docs","index","size","node"
		}
		
		interface Shard_Table_Keys{
			
			String SHARD = "Shard"; 
			String STATE = "State"; 
			String PRIMARY = "Primary?"; 
			String NO_DOCS = "# Docs";
			String SIZE = "Size";
			String NODE = "Node";
			
//			"Shard","State","Primary?","# Docs","Size","Node"
			
		}
		
	}
}
