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
package com.impetus.ankush2.zookeeper;

/**
 * The Interface ZookeeperConstant.
 * 
 */
public interface ZookeeperConstant {

	/**
	 * The Interface Keys.
	 */
	public interface Keys {

		String ERROR_Zookeeper_CONF_NOT_FOUND = "Zookeeper configuration is not properly send.Please provide valid configuration and nodes.";

		/** The node id. */
		String NODE_ID = "nodeId";

		/** The last node id. */
		String LAST_NODE_ID = "lastNodeId";

		/** The data directory. */
		String DATA_DIRECTORY = "dataDirectory";

		/** The client port. */
		String CLIENT_PORT = "clientPort";

		/** The zk nodes. */
		String ZK_NODES = "zkNodes";

		/** The init limit. */
		String INIT_LIMIT = "initLimit";

		/** The tick time. */
		String TICK_TIME = "tickTime";

		/** The sync limit. */
		String SYNC_LIMIT = "syncLimit";

		/** The rel path_ zk server script. */
		String relPath_ZkServerScript = "bin/zkServer.sh ";

		/** The jmx port. */
		String JMX_PORT = "jmxPort";

		/** The ensembleid. */
		String ENSEMBLEID = "ensembleId";
		
		String DEFAULT_ENSEMBLEID = "_default";

		/** The zookeeper command execution log path. */
		String ZOOKEEPER_COMMAND_EXECUTION_LOG_PATH = "zooCommandExecutionLog";

		String ZOOKEEPER_STRING = "Zookeeper_";

		String STANDALONESERVER_PORT_1 = "StandaloneServer_port-1";

		interface Advance_Conf_Keys {
			String HOST = "host";
		}
	}

	interface Monitor_Keys {
		/** The Constant COMMAND_MNTR. */
		String COMMAND_MNTR = "mntr";

		/** The Constant COMMAND_SRVR. */
		String COMMAND_SRVR = "srvr";

		/** The Constant COMMAND_CONF. */
		String COMMAND_CONF = "conf";

		/** The Constant SERVER_ID. */
		String SERVER_ID = "serverId";

		/** The Constant SERVER_TYPE. */
		String SERVER_TYPE = "serverType";

		/** The Constant ZOOKEEPER_NODE_INFO. */
		String ZOOKEEPER_NODE_INFO = "zookeeperNodeInfo";

		/** The Constant CASSANDRA_FOLDER_CONF. */
		String ZOOKEEPER_FOLDER_CONF = "conf/";

		/** The Constant CASSANDRA_FILE_YAML. */
		String ZOOKEEPER_FILE_CFG = "zoo.cfg";

		/** The Constant ZNODE_DATA. */
		String ZNODE_DATA = "znodeData";
	}

	/**
	 * The Interface Action.
	 */
	public interface Action {

		/** The start. */
		String START = "start";

		/** The stop. */
		String STOP = "stop";
	}

}
