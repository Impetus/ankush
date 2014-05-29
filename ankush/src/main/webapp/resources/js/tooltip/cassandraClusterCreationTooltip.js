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

com.impetus.ankush.tooltip.cassandraClusterCreation={

				vendor : "Cassandra Vendors",
				version : "Cassandra Versions",
				downloadPath : "Cassandra bundle location on the Web / Remote Server",
				localPath : "Directory location of Cassandra bundle",
				installationPath : "Directory location to deploy Cassandra",
				partitioner: "Partitioner determines the distribution of rows (by key) across nodes",
				snitch: "Snitch determines how nodes are grouped together within the overall network topology",
				vnodes:	"Virtual Nodes token count assigned randomly to a node in the ring",
				rpcPort: "The port reserved for client connections and Thrift service",
				storagePort: "The port reserved for inter-node communication",
				dataDirectory: "The directory location of column family data (SSTables)",
				logDirectory: "The directory location of logs",
				savedCacheDirectory: "The directory location of column family key and row caches",
				commitLogDirectory: "The directory location of the commit logs"
};
