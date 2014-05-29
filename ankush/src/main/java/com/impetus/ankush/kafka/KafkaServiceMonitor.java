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
package com.impetus.ankush.kafka;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.exception.TaskExecFailException;

import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.framework.ServiceMonitorable;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.scripting.AnkushTask;
import com.impetus.ankush.common.scripting.impl.KillJPSProcessByName;
import com.impetus.ankush.common.scripting.impl.RunInBackground;
import com.impetus.ankush.common.utils.AnkushLogger;

public class KafkaServiceMonitor implements ServiceMonitorable {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(KafkaServiceMonitor.class);

	/** The kafka-start-script */
	private static final String relPath_KAFKA_SERVER_START_SCRIPT = "/bin/kafka-server-start.sh"
			+ Constant.STR_SPACE;

	/** The kafka server-file-path */
	private static final String KAFKA_SERVER_FILE_PATH = "/config/server.properties";

	/** The Constant KAFKA. */
	private static final String KAFKA = "kafka_";

	/* (non-Javadoc)
	 * @see com.impetus.ankush.common.framework.ServiceMonitorable#manageService(com.impetus.ankush.common.framework.config.ClusterConf, net.neoremind.sshxcute.core.SSHExec, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean manageService(ClusterConf conf, SSHExec connection,
			String processName, String action) {
		boolean result = true;
		try {

			if (action.equals(Constant.ServiceAction.START)) {
				result = this.startkafka(connection, conf);
			} else {
				result = this.stopkafka(connection, conf);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return result;
	}

	/**
	 * This method start kafka service
	 * 
	 * @param connection
	 * @param clusterConf
	 * @throws TaskExecFailException
	 */
	public boolean startkafka(SSHExec connection, ClusterConf clusterConf) {
		try {
			KafkaConf conf = (KafkaConf) clusterConf.getClusterComponents()
					.get(Constant.Technology.KAFKA);
			String kafkaHome = conf.getInstallationPath() + KAFKA
					+ conf.getComponentVersion();
			// command to run Kafka service.
			String cmd = kafkaHome + relPath_KAFKA_SERVER_START_SCRIPT
					+ kafkaHome + KAFKA_SERVER_FILE_PATH;

			// jps to node and check if Kafka service is already running.
			cmd = "(jps | grep " + Constant.Process.KAFKA + " || " + cmd + ")";

			AnkushTask kafkaStart = new RunInBackground(cmd);

			Result res = connection.exec(kafkaStart);
			if (!res.isSuccess) {
				logger.error("Could not start Kafka service");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * This method stop kafka service
	 * 
	 * @param connection
	 * @param clusterConf
	 * @throws TaskExecFailException
	 */
	public boolean stopkafka(SSHExec connection, ClusterConf clusterConf) {
		try {
			AnkushTask killKafka = new KillJPSProcessByName(
					Constant.Process.KAFKA);
			Result res = connection.exec(killKafka);
			if (!res.isSuccess) {
				logger.error("Could not stop Kafka service");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
}
