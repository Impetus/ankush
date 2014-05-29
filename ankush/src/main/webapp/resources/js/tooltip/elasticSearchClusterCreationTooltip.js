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

com.impetus.ankush.tooltip.elasticSearchClusterCreation={

				vendor : "ElasticSearch Vendors",
				version : "ElasticSearch Versions",
				downloadPath : "ElasticSearch bundle location on the Web / Remote Server",
				localPath : "Directory location of ElasticSearch bundle",
				installationPath : "Directory location to deploy ElasticSearch",
				actionAutoCreateIndex: "If this value is set to true,it creates an index if it has not been created before",
				bootstrapMlockall: "Set to true to instruct the operating system to never swap the ElasticSearch process(LINUX only)",
				dataPath: "Path to directory where to store index data allocated for this node.",
				heapSize: "It set the java heap size in the $ES_HEAP_SIZE environment variable." +
						" The rule of thumb is that the ElasticSearch heap should have around 50% of the available memory on the machine." +
						" ElasticSearch uses system caches heavily, so you should leave enough memory for them.",
				maxLocalStorageNodes: "It is the number of concurrent ES nodes that can run on the same machine.",
				shardIndex: "Set the number of shards (splits) of an index.",
				replicaIndex: "Set the number of replicas (additional copies) of an index.",
				httpPort: "Set a custom port to listen for HTTP traffic.",
				transportTcpPort:""
};
