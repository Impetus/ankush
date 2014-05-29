/*******************************************************************************
*===========================================================
*Ankush : Big Data Cluster Management Solution
*===========================================================
*
*(C) Copyright 2014, by Impetus Technologies
*
*This is free software; you can redistribute it and/or modify it under
*the terms of the GNU Lesser General Public License (LGPL v3) as
*published by the Free Software Foundation;
*
*This software is distributed in the hope that it will be useful, but
*WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
*See the GNU Lesser General Public License for more details.
*
*You should have received a copy of the GNU Lesser General Public License 
*along with this software; if not, write to the Free Software Foundation, 
*Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/

com.impetus.ankush.tooltip.hbaseClusterCreation={
		 		vendor : "HBase Vendors",
				version : "HBase Versions",
				downloadPath : "HBase bundle location on the Web / Remote Server",
				localPath : "Directory location of HBase bundle",
				installationPath : "Directory location to deploy HBase",
				
				fileSize : "Maximum HStoreFile size; default is 10G",
				compactionThreshold : "Minimum number of StoreFiles per Store for a compaction to occur",

				cacheSize : "Percentage of maximum heap allocated to block cache; default is 25%. Set to 0 to disable but it is not recommended",
				caching :" Number of rows fetched when calling next on a scanner if it is not served from (local, client) memory",
				zookeeperTimeout : "ZooKeeper session timeout",
			 	multiplier : "Useful in preventing runaway memstore during spikes in update traffic. Block updates if memstore has hbase.hregion.block.memstore time hbase.hregion.flush.size bytes.",
			 	majorCompaction : "The time (in miliseconds) between major compactions of all HStoreFiles in a region.",
			 	maxSize : "The maximum combined value in a multi-level block index beyond which the block is written out and a new block is started.",
			 	flushSize : "The Memstore size (in Bytes) beyond which it is flushed to disk.",
			 	handlerCount : "Count of RPC Listener instances spun up on RegionServers."
};
