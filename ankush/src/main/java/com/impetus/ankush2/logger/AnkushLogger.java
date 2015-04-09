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
package com.impetus.ankush2.logger;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush2.framework.config.ClusterConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class AnkushLogger.
 */
public class AnkushLogger {

	/**
	 * The Enum LogLevel.
	 */
	public enum LogLevel {

		/** The debug. */
		DEBUG,
		/** The info. */
		INFO,
		/** The error. */
		ERROR,
		/** The warn. */
		WARN
	}

	/** The logger. */
	private org.apache.log4j.Logger logger;

	/** The cluster id. */
	private Long clusterId;

	/** The operation id. */
	private Long operationId;

	/** The cluster name. */
	private String clusterName;

	/** The record log. */
	private boolean recordLog = false;

	/**
	 * Instantiates a new ankush logger.
	 */
	public AnkushLogger() {
	}

	/**
	 * Instantiates a new ankush logger.
	 * 
	 * @param classObj
	 *            the class obj
	 */
	public AnkushLogger(Class classObj) {
		this.logger = Logger.getLogger(classObj);
	}

	// Call only if save logs into database
	/**
	 * Instantiates a new ankush logger.
	 * 
	 * @param classObj
	 *            the class obj
	 * @param clusterConf
	 *            the cluster conf
	 */
	public AnkushLogger(Class classObj, ClusterConfig clusterConf) {
		this(classObj);
		setCluster(clusterConf);
	}

	// Add cluster name with logs
	/**
	 * Sets the cluster name.
	 * 
	 * @param clusterName
	 *            the new cluster name
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	// Call only if save logs into database
	/**
	 * Sets the cluster.
	 * 
	 * @param clusterConf
	 *            the new cluster
	 */
	public void setCluster(ClusterConfig clusterConf) {
		setClusterName(clusterConf.getName());
		this.clusterId = clusterConf.getClusterId();
		this.operationId = clusterConf.getOperationId();

		// Disable database logs if ids are missing.
		if (operationId == null || clusterId == null || operationId == 0
				|| clusterId == 0) {
			recordLog = false;
		} else {
			recordLog = true;
		}
	}

	/**
	 * Db message.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 * @param level
	 *            the level
	 */
	private void dbMessage(String message, String componentName, String host,
			LogLevel level) {
		// save message into database.
		if (recordLog) {
			if (message == null) {
				message = Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG;
			}
			Log log = new Log();
			log.setClusterId(clusterId);
			log.setOperationId(operationId);
			log.setMessage(message);
			log.setType(level.toString());
			log.setComponentName(componentName);
			log.setHost(host);

			log.setCreatedAt(new Date());
			try {
				AppStoreWrapper.getLogManager().save(log);
			} catch (Exception e) {
				this.logger.error(e.getMessage(), e);
			}

		}
	}

	/**
	 * Process message.
	 * 
	 * @param message
	 *            the message
	 * @param host
	 *            the host
	 * @return the string
	 */
	private String processMessage(String message, String host) {
		if (message == null) {
			message = Constant.Strings.ExceptionsMessage.GENERIC_EXCEPTION_MSG;
		}
		if (host != null) {
			message = host + " : " + message;
		}

		if (clusterName != null) {
			return "[" + clusterName + "] " + message;
		}
		return message;
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 */
	public void error(String message) {
		error(message, null, null, null);
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the Exception object
	 */
	public void error(String message, Throwable t) {
		error(message, null, null, t);
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 */
	public void error(String message, String componentName) {
		error(message, componentName, null, null);
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param t
	 *            the Exception object
	 */
	public void error(String message, String componentName, Throwable t) {
		error(message, componentName, null, t);
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 */
	public void error(String message, String componentName, String host) {
		error(message, componentName, host, null);
	}

	/**
	 * Error.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 * @param t
	 *            the Exception object
	 */
	public void error(String message, String componentName, String host,
			Throwable t) {
		logger.error(processMessage(message, host), t);
		dbMessage(message, componentName, host, LogLevel.ERROR);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 */
	public void warn(String message) {
		warn(message, null, null, null);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the Exception object
	 */
	public void warn(String message, Throwable t) {
		warn(message, null, null, t);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 */
	public void warn(String message, String componentName) {
		warn(message, componentName, null, null);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param t
	 *            the Exception object
	 */
	public void warn(String message, String componentName, Throwable t) {
		warn(message, componentName, null, t);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 */
	public void warn(String message, String componentName, String host) {
		warn(message, componentName, host, null);
	}

	/**
	 * Warn.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 * @param t
	 *            the Exception object
	 */
	public void warn(String message, String componentName, String host,
			Throwable t) {
		logger.warn(processMessage(message, host), t);
		dbMessage(message, componentName, host, LogLevel.WARN);
	}

	/**
	 * Info.
	 * 
	 * @param message
	 *            the message
	 */
	public void info(String message) {
		info(message, null, null);
	}

	/**
	 * Info.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 */
	public void info(String message, String componentName) {
		info(message, componentName, null);
	}

	/**
	 * Info.
	 * 
	 * @param message
	 *            the message
	 * @param componentName
	 *            the component name
	 * @param host
	 *            the host
	 */
	public void info(String message, String componentName, String host) {
		logger.info(processMessage(message, host));
		dbMessage(message, componentName, host, LogLevel.INFO);
	}

	/**
	 * Debug.
	 * 
	 * @param message
	 *            the message
	 */
	public void debug(String message) {
		debug(message, null);
	}

	/**
	 * Debug.
	 * 
	 * @param message
	 *            the message
	 * @param host
	 *            the host
	 */
	public void debug(String message, String host) {
		logger.debug(processMessage(message, host));
	}
}
