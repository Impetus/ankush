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

import com.impetus.ankush2.constant.Constant;

/**
 * @author Akhil
 * 
 */
public class HdpRoleLogsDirPath {

	/** The dir path hadoop. */
	static String DIR_PATH_HADOOP = "/var/log/";

	/** The relpath dir hdfs. */
	static String RELPATH_DIR_HDFS = "hadoop-hdfs";

	/** The relpath dir mapreduce. */
	static String RELPATH_DIR_MAPREDUCE = "hadoop-mapreduce";

	/** The RELPAT h_ di r_ m rv1. */
	static String RELPATH_DIR_MRv1 = "hadoop-0.20-mapreduce";

	/** The relpath dir yarn. */
	static String RELPATH_DIR_YARN = "hadoop-yarn";

	/** The process pid file map. */
	static Map<String, String> ROLE_LOG_DIR_PATH_MAP = new HashMap<String, String>();

	static {
		Map<String, String> roleLogDirMap = new HashMap<String, String>();

		roleLogDirMap.put(Constant.Role.NAMENODE, DIR_PATH_HADOOP
				+ RELPATH_DIR_HDFS + "/");

		roleLogDirMap.put(Constant.Role.DATANODE, DIR_PATH_HADOOP
				+ RELPATH_DIR_HDFS + "/");

		roleLogDirMap.put(Constant.Role.SECONDARYNAMENODE, DIR_PATH_HADOOP
				+ RELPATH_DIR_HDFS + "/");

		roleLogDirMap.put(Constant.Role.DFSZKFAILOVERCONTROLLER,
				DIR_PATH_HADOOP + RELPATH_DIR_HDFS + "/");

		roleLogDirMap.put(Constant.Role.JOURNALNODE, DIR_PATH_HADOOP
				+ RELPATH_DIR_HDFS + "/");

		roleLogDirMap.put(Constant.Role.JOBTRACKER, DIR_PATH_HADOOP
				+ RELPATH_DIR_MRv1 + "/");

		roleLogDirMap.put(Constant.Role.TASKTRACKER, DIR_PATH_HADOOP
				+ RELPATH_DIR_MRv1 + "/");

		roleLogDirMap.put(Constant.Role.JOBHISTORYSERVER, DIR_PATH_HADOOP
				+ RELPATH_DIR_MAPREDUCE + "/");

		roleLogDirMap.put(Constant.Role.RESOURCEMANAGER, DIR_PATH_HADOOP
				+ RELPATH_DIR_YARN + "/");

		roleLogDirMap.put(Constant.Role.NODEMANAGER, DIR_PATH_HADOOP
				+ RELPATH_DIR_YARN + "/");

		roleLogDirMap.put(Constant.Role.WEBAPPPROXYSERVER, DIR_PATH_HADOOP
				+ RELPATH_DIR_YARN + "/");

		ROLE_LOG_DIR_PATH_MAP = roleLogDirMap;
	}

	/**
	 * Gets the role log dir path.
	 * 
	 * @param roleName
	 *            the role name
	 * @return the role log dir path
	 */
	public static String getRoleLogDirPath(String roleName) {
		return ROLE_LOG_DIR_PATH_MAP.get(roleName);
	}
}
