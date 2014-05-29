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

com.impetus.ankush.tooltip.oracleClusterCreation={
			
			datacenter : "Datacenter is often used to represent group of nodes based on their physical location",
			topology : "Topology represents the configuration of the Store",
			replicationFactor : "Replication Factor is the number of nodes belonging to a Shard; default value is 3",
			partition : "Each Shard contains one or more partitions. As a rough rule of thumb, there should be at least 10 to 20 partitions per shard. The number of partitions CAN NOT be changed after the initial deployment",
			registryPort : "The TCP/IP port for each node on which Oracle NoSQL Database is contacted; default value is 5000",
			haPortRange : "Range of sequential free ports which nodes use to communicate amongst themselves. The count of ports should be AT LEAST equal to the count of Replication Nodes on Storage Node. The default port range value is 5010 and 5020", 
			installationPath :"Directory location for Oracle NoSQL Database package files aka KVHOME.", 
			dataPath : "Directory location for Oracle NoSQL Database data aka KVROOT.",
			dbPackage : "Set or upload the Oracle NoSQL DB Package version",
			ntpServer : "The NTP (Network Time Protocol) server is used to synchronize time across all nodes to ensure effective consistency policy during read operations.",
};
