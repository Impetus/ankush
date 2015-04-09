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
package com.impetus.ankush2.hadoop.deployer.servicemanager;

import java.util.HashMap;
import java.util.Map;

import com.impetus.ankush2.hadoop.utils.HadoopConstants;

/**
 * @author Akhil
 *
 */
// Needs to be changed into a simple logic of Key value pair
public class HdpRolePidFilePath {
	/** The dir pid hdfs. */
	static String DIR_PID_HDFS = "/var/run/hadoop-hdfs/";

	/** The prefix pid hdfs. */
	static String PREFIX_PID_HDFS = "hadoop-hdfs-";

	/** The dir pid mapreduce. */
	static String DIR_PID_MAPREDUCE = "/var/run/hadoop-mapreduce/";

	/** The prefix pid mapreduce. */
	static String PREFIX_PID_MAPREDUCE = "mapreduce-mapreduce-";

	/** The DI r_ pi d_ m rv1. */
	static String DIR_PID_MRv1 = "/var/run/hadoop-0.20-mapreduce/";

	/** The prefix pid mrv1. */
	static String PREFIX_PID_MRv1 = "hadoop-hadoop-";

	/** The dir pid yarn. */
	static String DIR_PID_YARN = "/var/run/hadoop-yarn/";

	/** The prefix pid yarn. */
	static String PREFIX_PID_YARN = "yarn-yarn-";

	/** The pid extn. */
	static String PID_EXTN = ".pid";

	/** The process pid file map. */
	static Map<String, String> ROLE_PID_FILE_PATH_MAP = new HashMap<String, String>();

	static {
		Map<String, String> roleFilePathMap = new HashMap<String, String>();

		roleFilePathMap.put(HadoopConstants.Roles.NAMENODE,
				HdpRolePidFilePath.DIR_PID_HDFS + PREFIX_PID_HDFS
						+ HadoopConstants.Roles.NAMENODE.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(HadoopConstants.Roles.DATANODE,
				HdpRolePidFilePath.DIR_PID_HDFS + PREFIX_PID_HDFS
						+ HadoopConstants.Roles.DATANODE.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(
				HadoopConstants.Roles.SECONDARYNAMENODE,
				HdpRolePidFilePath.DIR_PID_HDFS
						+ PREFIX_PID_HDFS
						+ HadoopConstants.Roles.SECONDARYNAMENODE
								.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(
				HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
				HdpRolePidFilePath.DIR_PID_HDFS
						+ PREFIX_PID_HDFS
						+ HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER
								.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(
				HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER,
				HdpRolePidFilePath.DIR_PID_HDFS
						+ PREFIX_PID_HDFS
						+ HadoopConstants.Roles.DFSZKFAILOVERCONTROLLER
								.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(HadoopConstants.Roles.JOBTRACKER,
				HdpRolePidFilePath.DIR_PID_MRv1 + PREFIX_PID_MRv1
						+ HadoopConstants.Roles.JOBTRACKER.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(HadoopConstants.Roles.TASKTRACKER,
				HdpRolePidFilePath.DIR_PID_MRv1 + PREFIX_PID_MRv1
						+ HadoopConstants.Roles.TASKTRACKER.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(HadoopConstants.Roles.JOBHISTORYSERVER,
				HdpRolePidFilePath.DIR_PID_MAPREDUCE + PREFIX_PID_MAPREDUCE
						+ "historyserver" + HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(
				HadoopConstants.Roles.RESOURCEMANAGER,
				HdpRolePidFilePath.DIR_PID_YARN
						+ PREFIX_PID_YARN
						+ HadoopConstants.Roles.RESOURCEMANAGER
								.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(HadoopConstants.Roles.NODEMANAGER,
				HdpRolePidFilePath.DIR_PID_YARN + PREFIX_PID_YARN
						+ HadoopConstants.Roles.NODEMANAGER.toLowerCase()
						+ HdpRolePidFilePath.PID_EXTN);

		roleFilePathMap.put(HadoopConstants.Roles.WEBAPPPROXYSERVER,
				HdpRolePidFilePath.DIR_PID_YARN + PREFIX_PID_YARN
						+ "proxyserver" + HdpRolePidFilePath.PID_EXTN);

		ROLE_PID_FILE_PATH_MAP = roleFilePathMap;
	}

	/**
	 * Gets the process pid file path.
	 * 
	 * @param roleName
	 *            the role name
	 * @return the process pid file path
	 */
	public static String getRolePidFilePath(String roleName) {
		return ROLE_PID_FILE_PATH_MAP.get(roleName);
	}
}
