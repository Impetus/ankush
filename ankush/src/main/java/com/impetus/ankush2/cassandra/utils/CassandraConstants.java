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
package com.impetus.ankush2.cassandra.utils;

public interface CassandraConstants {

	/** The CassandraDaemon. */
	String CASSANDRA_DAEMON = "CassandraDaemon";

	String CASSANDRA_MONITORABLE_CLASS_NAME = "com.impetus.ankush.agent.cassandra.CassandraServiceStatusMonitor";

	// String CASSANDRA_SERVICE_START_COMMAND = "bin/cassandra";

	String CLUSTER_SUMMARY = "clusterSummary";

	String ORG_APACHE_CASSANDRA = "org.apache.cassandra.";

	String ACTIONS = "actions";

	enum Cassandra_Services {
		CassandraDaemon, CassandraSeed, CassandraNonSeed;
	}

	interface NodeProperties {
		String V_NODE_COUNT = "vNodeCount";

		String CASSANDRA_SEED = "cassandraSeed";
	}

	interface ClusterProperties {

		/**
		 * This key is used to set cluster_name property in cassandra.conf file.
		 * During registration , we read same property from cassandra.conf file
		 * and set it in advanceConf to use it later during add nodes case
		 **/
		String CLUSTER_NAME = "clusterName";

		String LOG_DIR = "logDir";

		String PARTITIONER = "partitioner";

		String RPC_PORT = "rpcPort";

		String STORAGE_PORT = "storagePort";

		String DATA_DIR = "dataDir";

		String SAVED_CACHES_DIR = "savedCachesDir";

		String COMMIT_LOG_DIR = "commitlogDir";

		String SNITCH = "snitch";

		String JMX_PORT = "jmxPort";

		String SEED_NODE_SET = "seedNodeSet";

		String BIN_DIR = "binDir";

		String CONF_DIR = "confDir";
	}

	/**
	 * The interface Node_Type.
	 */
	interface Node_Type {

		/** The Cassandra Seed role. */
		String CASSANDRA_SEED = "CassandraSeed";

		/** The Cassandra Non Seed role. */
		String CASSANDRA_NON_SEED = "CassandraNonSeed";

	}

	/**
	 * The interface Cassandra_vendors.
	 */
	interface Cassandra_vendors {

		/** The Constant CASSANDRA_VENDOR_DSC. */
		String CASSANDRA_VENDOR_DSC = "dsc";

		/** The Constant CASSANDRA_VENDOR_APACHE. */
		String CASSANDRA_VENDOR_APACHE = "apache";

	}

	/**
	 * The interface Configuration_Properties.
	 */
	interface Configuration_Properties {

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

		String KEYSPACE = "keyspace";

		String COLUMN_FAMILY = "columnfamily";
	}

	/**
	 * The interface Cassandra.
	 */
	interface Cassandra_Configuration_Files {

		/** The cassandra.yaml. */
		String CASSANDRA_YAML = "cassandra.yaml";

		/** The cassandra-topology.properties. */
		String CASSANDRA_TOPOLOGY_PROPERTIES = "cassandra-topology.properties";

		/** The Constant CASSANDRA-CASSANDRA_RACK_DC_PROPERTIES. */
		String CASSANDRA_RACK_DC_PROPERTIES = "cassandra-rackdc.properties";

		/** The log4j-server.properties. */
		String CASSANDRA_LOG4J_SERVER_PROPERTIES = "log4j-server.properties";

		/** The log4j-server.properties. */
		String CASSANDRA_LOGBACK_XML = "logback.xml";

		/** The Constant CASSANDRA_CONF_ENV. */
		String CASSANDRA_ENV_SH = "cassandra-env.sh";

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

		/** The LoadString. */
		String CASSANDRA_JMX_ATTRIBUTE_LOADSTRING = "LoadString";

		/** The HostIdMap. */
		String CASSANDRA_JMX_ATTRIBUTE_HOSTID = "HostIdMap";

		/** The UnreachableNodes. */
		String CASSANDRA_JMX_ATTRIBUTE_UNREACHABLE_NODES = "UnreachableNodes";

		/** The SimpleStates. */
		String CASSANDRA_JMX_ATTRIBUTE_SIMPLESTATES = "SimpleStates";

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

		/** The FailureDetector. */
		String CASSANDRA_JMX_OBJECT_FAILUREDETECTOR = "FailureDetector";

		String CASSANDRA_JMX_ATTRIBUTE_HOSTIDMAP = "HostIdMap";

		String CASSANDRA_JMX_OBJECT_ENDPOINT_SNITCH_INFO = "EndpointSnitchInfo";

		String CASSANDRA_JMX_ATTRIBUTE_LIVE_NODES = "LiveNodes";

	}

	interface Cassandra_Actions {

		/** The Nodetool. */
		// String CASSANDRA_NODETOOL = "nodetool";

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

	interface JMX_Operations {
		String CASSANDRA_JMX_ATTRIBUTE_GETRACK = "getRack";

		String CASSANDRA_JMX_ATTRIBUTE_GETDATACENTER = "getDatacenter";

		String CASSANDRA_JMX_ATTRIBUTE_ENDPOINTSTATE = "getEndpointState";

		/** The getTokens. */
		String CASSANDRA_JMX_ATTRIBUTE_TOKENS = "getTokens";
	}

	interface Cassandra_executables {
		String CASSANDRA_DAEMON_START = "cassandra";

		String NODETOOL = "nodetool";

	}
}
