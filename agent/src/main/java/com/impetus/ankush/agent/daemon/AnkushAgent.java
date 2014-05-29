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
package com.impetus.ankush.agent.daemon;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.impetus.ankush.agent.action.ActionFactory;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;

/**
 * @author Hokam Chauhan
 */
public final class AnkushAgent {

	/** The log. */
	private static AgentLogger LOGGER = new AgentLogger(AnkushAgent.class);

	private static final int TASK_SEARCH_SLEEP_TIME = 1000;
	private static Map<String, Taskable> objMap = new HashMap<String, Taskable>();

	// private Constructor.
	private AnkushAgent() {
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

		// taskable file name.
		String file = System.getProperty("user.home")
				+ "/.ankush/agent/conf/taskable.conf";

		// iterate always
		while (true) {
			try {
				// reading the class name lines from the file
				List<String> classNames = FileUtils.readLines(new File(file));
				// iterate over the class names to start the newly added task.
				for (String className : classNames) {
					// if an empty string from the file then continue the loop.
					if (className.isEmpty()) {
						continue;
					}
					// if not started.
					if (!objMap.containsKey(className)) {
						// create taskable object
						LOGGER.info("Creating " + className + " object.");

						Taskable taskable = ActionFactory
								.getTaskableObject(className);
						objMap.put(className, taskable);
						// call start on object ...
						taskable.start();
					}
				}

				// iterating over the existing tasks to stop if it is removed
				// from the file.
				Set<String> existingClassNames = new HashSet<String>(
						objMap.keySet());
				for (String className : existingClassNames) {
					// if not started.
					if (!classNames.contains(className)) {
						// create taskable object

						LOGGER.info("Removing " + className + " object.");

						Taskable taskable = objMap.get(className);
						objMap.remove(className);
						// call stop on object ...
						taskable.stop();
					}
				}
				Thread.sleep(TASK_SEARCH_SLEEP_TIME);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}
}
