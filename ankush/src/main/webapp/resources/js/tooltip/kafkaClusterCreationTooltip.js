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

com.impetus.ankush.tooltip.kafkaClusterCreation = {
		vendor : "Kafka Vendors",
		version : "Kafka Versions",
		downloadPath : "Kafka bundle location on the Web / Remote Server",
		localPath : "Directory location of Kafka bundle",
		installationPath : "Directory location to deploy Kafka",
		replicationFactor : "The default replication factor for automatically created topics",
		port : "The port reserved for client connections",
		// jmxPort:
		logDirectory : "A comma-separated list of one or more directories in which Kafka data is stored",
						
		partitions : "The default number of partitions per topic",
		networkThreads : "The count of network threads used for handling network requests" ,
		ioThreads : "The count of I/O threads used for executing requests. At the minimum, the thread count should be equal to number of disks" ,
					
		queuedMaxRequests : "The number of requests that can be queued up for processing by the I/O threads.",
		logRetentionHours : "The number of hours to keep a log segment before it is deleted, i.e. the default data retention window for all topics." +
							"Also note that if both this and log.retention.bytes are set Kafka delete a segment when either limit is " +
							"exceeded.",
		logRetentionBytes : "The amount of data to retain in the log for each topic-partitions." +
							"Note that this is the limit per-partition so multiply by the number of partitions to get the total data" +
							"retained for the topic.Also note that if both this and log.retention.hours are set Kafka delete a segment when either limit is exceeded.",
		logCleanupIntervalMins : "The frequency in minutes that the log cleaner checks whether any log segment is eligible for " +
								  "deletion to meet the retention policies.",
		logFlushIntervalMessage : "The number of messages written to a log partition before we force an fsync on the log." +
								"Setting this higher will improve performance a lot but will increase the window of data " +
								"at risk in the event of a crash.If both this setting and log.flush.interval.ms are used " +
								"the log will be flushed when either criteria is met.",
		logFlushSchedularInterval : "The frequency in ms that the log flusher checks whether any log is eligible to be flushed to disk.",
		logFlushInterval : "The maximum time between fsync calls on the log.if used in conjuction with log.flush.interval.messages " +
							"the log will be flushed when either criteria is met.",
		controlledShutdownEnable : "Enable controlled shutdown of the broker. If enabled, the broker will move all leaders on it" +
									" to some other brokers before shutting itself down. This reduces the unavailability window " +
									"during shutdown.",
		controlledShutdownMaxRetries : "Number of retries to complete the controlled shutdown successfully before executing " +
										"an unclean shutdown.",
		logLevel : "This is the log level that will be set for all types of log in log4j.properties file." +
					"In future it can be changed from Parameter page of cluster-monitorig."
};
