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

/**
 * The Interface Constants.
 * 
 * @author Hokam Chauhan
 */
public interface Constant {
	
//	String ANKUSH="Ankush";
	
	String AGENT="Agent";

	/** The prop name node id. */
	String PROP_NAME_NODE_ID = "NODE_ID";

	/** The prop name url part. */
	String PROP_NAME_URL_PART = "URL_PART";

	/** The prop name url part. */
	String PROP_NAME_CLUSTER_MONITORING_URL = "CLUSTER_MONITORING_URL";

	/** The prop name host. */
	String PROP_SERVER_IP = "SERVER_IP";

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
	String PROP_NAMENODE_RPC_PORT = "NAMENODE_RPC_PORT";
	
	String PROP_JOBTRACKER_RPC_PORT = "JOBTRACKER_RPC_PORT";

	/** The prop dfs port. */
	String PROP_DFS_PORT = "DFS_PORT";

	String PROP_JOBTRACKER_HOST = "JOBTRACKER_HOST";
	
	/** The prop namenode host. */
	String PROP_NAMENODE_HOST = "NAMENODE_HOST";

	/** The prop namenode host. */
	String PROP_RESOURCEMANAGER_HOST = "RESOURCEMANAGER_HOST";

	/** The prop jarpath. */
	String PROP_JARPATH = "JARPATH";

	/** The prop seednode. */
	String PROP_SEEDNODE = "SEEDNODE";

	/** The prop name upload file url. */
	String PROP_NAME_UPLOAD_FILE_URL = "UPLOAD_FILE_URL";

	/** The prop name topology url. */
	String PROP_NAME_MONITORING_URL = "URL_MONITORING";

	/** The host ip. */
	String HOST_PUBLIC_IP = "HOST_PUBLIC_IP";
	
	/** The host ip. */
	String HOST_IP = "HOST_IP";

	/** The host ip. */
	String HOST_PRIVATE_IP = "HOST_PRIVATE_IP";

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
	
	/** The agent install dir. */
	String AGENT_INSTALL_DIR = "agent.install.dir";
	/** rrd dir group **/
	String RRD_GROUP = "RRD_GROUP";

	String PROP_HADOOP_REST_PORT = "HADOOP_REST_PORT";

	String PROP_NAMENODE_HTTP_PORT = "NAMENODE_HTTP_PORT";

	String PROP_SERVICE_CONF_DIR = "SERVICE_CONF_DIR";

	String PROP_HA_SERVICE_FILE_RELATIVE_PATH = "HA_SERVICE_FILE_RELATIVE_PATH";

	String PROP_NAME_URL_SERVICE_PART_1 = "SERVICE_MANAGE_PART_1";

	String PROP_NAME_CLUSTER_ID = "CLUSTER_ID";

	String PROP_NAME_URL_SERVICE_PART_2 = "SERVICE_MANAGE_PART_2";

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
}
