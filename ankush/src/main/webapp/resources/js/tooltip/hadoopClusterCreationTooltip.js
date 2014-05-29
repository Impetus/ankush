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

com.impetus.ankush.tooltip.hadoopClusterCreation={

				vendor : "Hadoop Vendors. Ecosystem Components might vary across Vendors",
				version : "Hadoop Versions. Ecosystem Components might vary across Vendors",
				downloadPath : "Hadoop bundle location on the Web / Remote Server",
				localPath : "Directory location of Hadoop bundle",
				installationPath : "Directory location to deploy Hadoop",
				
				
				dfsReplicationFactor : "Replication Factor for each block; set to 3 by default.",
				dfsNameDir : "Directory location where name table(fsimage) is stored. Give a comma separated list to replicate the name table across all directories for  redundancy",
				dfsDataDir : "Directory location where DataNode stores its blocks. Give a comma separated list to store data in all the named directories",
				
				hadoopTmpDir : "A shared directory for temporary files.",
				mapRedTmpDir : "A local directory for MapRead temporary directory.",
				
				s3AccessKey : "ID of the account to access S3.",
				s3SecretKey : "Authentication key of the account to access S3.",
				s3nAccessKey : "ID of the account to access S3n.",
				s3nSecretKey : "Authentication key of the account to access S3n."
};
