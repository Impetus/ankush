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

// TODO: Auto-generated Javadoc
/**
 * The Interface AgentConstant.
 */
public interface AgentConstant {

	/**
	 * The Interface Strings.
	 */
	interface Strings {
		/** The agent down message. */
		String AGENT_DOWN_MESSAGE = "Ankush Agent is down on some nodes. Please resolve related issue.";

		// String RELPATH_AGENT_CRON_CONF = "conf/ankush-agent-cron.conf";

		/** The etc path agent cron conf. */
		String ETC_PATH_AGENT_CRON_CONF = "/etc/cron.d/"
				+ AgentConstant.Strings.FILE_AGENT_CRON_CONF;

		/** The file agent cron conf. */
		String FILE_AGENT_CRON_CONF = "ankush-agent-cron.conf";

	}

	/**
	 * The Interface Relative_Path.
	 */
	interface Relative_Path {
		/** The Constant AGENT_PROPERTY_FILE_PATH. */
		String AGENT_CONF_FILE = "conf/agent.properties";

		/** The agent home dir. */
		String AGENT_HOME_DIR = ".ankush/agent/";

		/** The log4j.properties file for Agent. */
		String LOG4J_PROPERTIES_FILE = "conf/log4j.properties";
		/** The Constant AGENT_TASKABLE_FILE_PATH. */
		String TASKABLE_FILE = "conf/taskable.conf";
		/** The agent start script. */
		String START_SCRIPT = "bin/start-agent.sh";

		/** The action script. */
		String ACTION_SCRIPT = "bin/agent-action.sh";
		/** The agent stop script. */
		String STOP_SCRIPT = "bin/stop-agent.sh";
		/** The agent service conf folder. */
		String SERVICE_CONF_DIR = "conf/services/";
		/** The agent conf path. */
		String CONF_DIR = "conf/";
		/** The agent haservice conf path. */
		String HASERVICE_CONF_FILE = ".conf/HAService.xml";

		/** The cron conf file. */
		String CRON_CONF_FILE = "conf/"
				+ AgentConstant.Strings.FILE_AGENT_CRON_CONF;

		/** The jmxtrans. */
		String JMXTRANS = "jmxtrans/";
	}

	/**
	 * The Interface Key.
	 */
	interface Key {

		/** The install dir env variable key. */
		String INSTALL_DIR_ENV_VARIABLE_KEY = "AGENT_INSTALL_DIR";

		/** The java property agent install dir. */
		String JAVA_PROPERTY_AGENT_INSTALL_DIR = "agent.install.dir";
	}

	/**
	 * The Enum Agent_Services.
	 */
	enum Agent_Services {
		
		/** The Ankush agent. */
		AnkushAgent;
	}

}
