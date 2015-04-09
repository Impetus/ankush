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
package com.impetus.ankush2.agent;

public class AnkushConstant {

	interface Agent {
		interface Keys {
			String AGENT_TAR = "scripts/agent/agent.tar.gz";
			String AGENT_DAEMON_CLASS = "agent.daemon.class";
			String AGENT_DIR = "agent.dir";
		}
		interface Action {

			/** The add. */
			String ADD = "add";

			/** The addforcestop. */
			String ADDFORCESTOP = "addForceStop";

			/** The delete. */
			String DELETE = "delete";

			/** The edit. */
			String EDIT = "edit";

			/**
			 * Action Handler names.
			 * 
			 * @author hokam
			 */
			interface Handler {

				/** The haconfig. */
				String HACONFIG = "haconfig";
			}
		}

		/**
		 * Parameters for service xml configuration.
		 * 
		 */
		interface ServiceParams {
			// pid file param
			/** The pidfile. */
			String PIDFILE = "pidFile";
			// port
			/** The port. */
			String PORT = "port";
		}

		/** The agent down message. */
		String AGENT_DOWN_MESSAGE = "Ankush Agent is down on some nodes. Please resolve related issue.";

//		/** The Constant AGENT_PROPERTY_FILE_PATH. */
//		String AGENT_PROPERTY_FILE_PATH = ".ankush/agent/conf/agent.properties";
//
//		/** The Constant AGENT_TASKABLE_FILE_PATH. */
//		String AGENT_TASKABLE_FILE_PATH = ".ankush/agent/conf/taskable.conf";
//
//		/** The agent start script. */
//		String AGENT_START_SCRIPT = "$HOME/.ankush/agent/bin/start-agent.sh";
//
//		/** The agent stop script. */
//		String AGENT_STOP_SCRIPT = "$HOME/.ankush/agent/bin/stop-agent.sh";
//
//		/** The agent service conf folder. */
//		String AGENT_SERVICE_CONF_FOLDER = "$HOME/.ankush/agent/conf/services/";
//
//		/** The agent conf path. */
//		String AGENT_CONF_PATH = "$HOME/.ankush/agent/conf/";
//
//		/** The agent haservice conf path. */
//		String AGENT_HASERVICE_CONF_PATH = "$HOME/.ankush/agent/conf/HAService.xml";

		/**
		 * Service Type.
		 */
		interface ServiceType {
			// Service PS
			/** The ps. */
			String PS = "ps";
			// Service type jps
			/** The jps. */
			String JPS = "jps";
			// service type port.
			/** The port. */
			String PORT = "port";
			// service type pid.
			/** The pid. */
			String PID = "pid";
		}
	}

}
