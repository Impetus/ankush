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
package com.impetus.ankush.agent.utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;

/**
 * Class to create RRD files for directory usage monitoringss.
 * 
 * @author hokam
 * 
 */
public class DirectoryUsageMonitor extends Taskable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(DirectoryUsageMonitor.class);
	/** Agent config object. **/
	private AgentConf conf = null;

	/**
	 * Constructor fine assigning agent conf.
	 */
	public DirectoryUsageMonitor() {
		// assigning agent conf.
		conf = new AgentConf();
	}

	@Override
	public void start() {
		try {
			// calling method for rrd file creation.
			createDirUsageRRD();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Method to create directory usage rrd files for configured dir lists.
	 */
	private void createDirUsageRRD() {
		// getting execution period.
		long period = conf.getIntValue(Constant.DIR_USAGE_RRD_UPDATE_TIME);
		// ganglia gmond conf path.
		final String confPath = System.getProperty(Constant.USER_HOME)
				+ conf.getStringValue(Constant.GANGLIA_CONF_PATH);

		final String graphGroup = conf.getStringValue(Constant.RRD_GROUP);

		Runnable task = new Runnable() {
			@Override
			public void run() {
				List<String> dirs = conf.getDataList(Constant.DATA_DIR_LIST,
						Constant.Splitter.COMMA);
				// calling method to execute command.
				executeRRGGenerationTask(graphGroup, confPath, dirs);
			}
		};
		// scheduling the runnable for periodical execution.
		service.scheduleAtFixedRate(task, 0, period, TimeUnit.SECONDS);
	}

	/**
	 * Method to generate command for rrd file creation.
	 * 
	 * @param confPath
	 * @param dirs
	 * @return
	 */
	private void executeRRGGenerationTask(String graphGroup, String confPath,
			List<String> dirs) {
		// iterating over the list of directories.
		for (String dirName : dirs) {
			// file object.
			File directory = new File(dirName);
			// dir size value.
			Double value = 0d;
			// if exists.
			if (directory.exists()) {
				// getting dir size value.
				value = (double) FileUtils.sizeOf(directory);
			}
			// command to create rrd directory.
			String command = "gmetric --conf " + confPath + " --name "
					+ graphGroup + dirName.replace("/", "_") + " --value "
					+ Math.round(value) + " --type double --units bytes";
			try {
				// executing command.
				CommandExecutor.executeCommand(command);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
