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
package com.impetus.ankush.common.utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.domain.Log;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.framework.config.GenericConfiguration;
import com.impetus.ankush.common.framework.config.NodeConf;

/**
 * The Class AnkushLogger.
 * 
 * @author nikunj
 */
public class AnkushLogger {

	private static final String FAILED = " failed.";
	private static final String DONE = " done.";
	private static final String MSG_TYPE_ERR = "error";
	private static final String MSG_TYPE_INFO = "info";

	/** The logger. */
	private org.apache.log4j.Logger logger;

	/** The cluster id. */
	private Long clusterId;

	/** The operation id. */
	private Long operationId;

	/** The host. */
	private String host;

	/** The component name. */
	private String componentName = null;
	
	/** The cluster name */
	private String clusterName = null;

	/**
	 * Instantiates a new ankush logger.
	 * 
	 * @param classObj the class obj
	 */
	public AnkushLogger(Class classObj) {
		this.logger = Logger.getLogger(classObj);
	}

	/**
	 * Instantiates a new ankush logger.
	 * 
	 * @param componentName the component name
	 * @param classObj the class obj
	 */
	public AnkushLogger(String componentName, Class classObj) {
		this.componentName = componentName;
		this.logger = Logger.getLogger(classObj);
	}
	
	/**
	 * @param clusterName the clusterName to set
	 */
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	/**
	 * Set cluster details.
	 * 
	 * @param clusterConf the new cluster
	 */
	public void setCluster(ClusterConf clusterConf) {		
		this.clusterId = clusterConf.getClusterId();
		this.operationId = clusterConf.getOperationId();
		setClusterName(clusterConf.getClusterName());
	}

	/**
	 * Get Next Operation ID.
	 * 
	 * @param clusterId the cluster id
	 * @return the new operation id
	 */
	public long getNewOperationId(long clusterId) {
		long newOperationId = 1;
		try {
			List<Log> logs = AppStoreWrapper.getLogManager()
					.getAllByNamedQuery(
							"getLastOperationId",
							Collections.singletonMap("clusterId",
									(Object) new Long(clusterId)));
			if (!logs.isEmpty()) {
				newOperationId = logs.get(0).getOperationId() + 1;
			}
		} catch (Exception e) {
		}
		return newOperationId;
	}

	
	/**
	 * Set configuration and LOG object.
	 * 
	 * @param conf the new logger config
	 */
	public void setLoggerConfig(GenericConfiguration conf) {
		this.setClusterId(conf.getClusterDbId());
		this.setOperationId(conf.getOperationId());
		this.setClusterName(conf.getClusterName());
	}

	/**
	 * Remove all appenders.
	 */
	public void removeAppender() {
		this.logger.removeAllAppenders();
	}

	/**
	 * Sets the cluster id.
	 * 
	 * @param clusterId the clsuterId to set
	 */
	public void setClusterId(Long clusterId) {
		this.clusterId = clusterId;
	}

	/**
	 * Sets the operation id.
	 * 
	 * @param operationId the operationId to set
	 */
	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}

	/**
	 * Sets the host.
	 * 
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Adds the log message.
	 * 
	 * @param host the host
	 * @param message the message
	 * @param type the type
	 */
	private void addLogMessage(String host, String message, String type) {
		if (this.clusterId != null && this.operationId != null) {
			Log log = new Log();
			log.setClusterId(this.clusterId);
			log.setOperationId(operationId);
			log.setMessage(message);
			log.setType(type);
			log.setHost(host);
			log.setCreatedAt(new Date());
			try {
				AppStoreWrapper.getLogManager().save(log);
			} catch (Exception e) {
				this.logger.error(e.getMessage(), e);
			}
		}
	}
	
	private void logInfo(String message) {
		if (clusterName != null) {
			logger.info("["+clusterName + "] " + message);
		} else {
			logger.info(message);
		}
	}

	private void logError(String message, Exception e) {
		if (clusterName != null) {
			logger.error("["+clusterName + "] "+ message, e);
		} else {
			logger.error(message, e);
		}
	}

	/**
	 * Info.
	 * 
	 * @param message the message
	 */
	public void info(String message) {
		logInfo(message);
		addLogMessage(this.host, message, MSG_TYPE_INFO);
	}
	
	/**
	 * Info.
	 * 
	 * @param host the host
	 * @param message the message
	 */
	public void info(String host, String message) {
		logInfo(host + " : " + message);
		addLogMessage(host, message, MSG_TYPE_INFO);
	}
	
	/**
	 * Debug.
	 * 
	 * @param message the message
	 */
	public void debug(String message) {
		if (clusterName != null) {
			logger.debug("["+clusterName + "] " + message);
		} else {
			logger.debug(message);
		}
	}

	/**
	 * Error.
	 * 
	 * @param message the message
	 */
	public void error(String message) {
		logError(message, null);
		addLogMessage(this.host, message, MSG_TYPE_ERR);		
	}

	/**
	 * Error.
	 * 
	 * @param message the message
	 * @param e the e
	 */
	public void error(String message, Exception e) {
		logError(message, e);
		addLogMessage(this.host, message, MSG_TYPE_ERR);
	}

	/**
	 * Error.
	 * 
	 * @param host the host
	 * @param message the message
	 */
	public void error(String host, String message) {
		error(host, message, null);		
	}

	/**
	 * Error.
	 * 
	 * @param host the host
	 * @param message the message
	 * @param e the e
	 */
	public void error(String host, String message, Exception e) {
		logError(host + " : " + message, e);
		addLogMessage(host, message, MSG_TYPE_ERR);
	}
	
	/**
	 * Error.
	 * 
	 * @param nodeConf the node conf
	 * @param message the message
	 */
	public void error(NodeConf nodeConf, String message) {
		error(nodeConf, message, null);		
	}

	/**
	 * Error.
	 * 
	 * @param nodeConf the node conf
	 * @param message the message
	 * @param e the e
	 */
	public void error(NodeConf nodeConf, String message, Exception e) {
		if (componentName != null) {
			nodeConf.addError(componentName, message);
		}
		error(nodeConf.getPublicIp(), message, e);		
	}
	/**
	 * Log.
	 * 
	 * @param message the message
	 * @param status the status
	 */
	public void log(String message, boolean status) {
		if (status) {
			info(message + DONE);
		} else {
			error(message + FAILED);
		}
	}

	/**
	 * Log.
	 * 
	 * @param host the host
	 * @param message the message
	 * @param status the status
	 */
	public void log(String host, String message, boolean status) {
		if (status) {
			info(host, message + DONE);
		} else {
			error(host, message + FAILED);
		}
	}

	/**
	 * Log.
	 * 
	 * @param nodeConf the node conf
	 * @param message the message
	 */
	public void log(NodeConf nodeConf, String message) {
		if (nodeConf.getStatus()) {
			info(nodeConf.getPublicIp(), message + DONE);
		} else {
			error(nodeConf, message + FAILED);
		}
	}
}
