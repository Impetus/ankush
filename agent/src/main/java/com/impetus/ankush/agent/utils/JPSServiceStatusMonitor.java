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
package com.impetus.ankush.agent.utils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;

/**
 * The Class JPSServiceStatusMonitor.
 * 
 * @author Hokam Chauhan
 */
public class JPSServiceStatusMonitor extends Taskable {

	/** The log. */
	private AgentLogger LOGGER = new AgentLogger(JPSServiceStatusMonitor.class);
	/** The conf. */
	private AgentConf conf;

	/** The rest client. */
	private AgentRestClient restClient;

	/**
	 * Constructor for JPSServiceStatusMonitor.
	 * 
	 */
	public JPSServiceStatusMonitor() {
		this.conf = new AgentConf();
		this.restClient = new AgentRestClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.agent.action.Taskable#start()
	 */
	public void start() {
		LOGGER.info("JPSServiceStatusMonitor start");
		try {
			// calling updated service status info.
			sendServiceStatus();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Method to run a scheduler for periodic execution of service sender.
	 */
	private void sendServiceStatus() {
		// creating provider object.
		final JPSServiceStatusProvider provider = new JPSServiceStatusProvider();
		// gettins service status send url.
		final String url = conf.getURL(Constant.PROP_NAME_SERVICE_URL_LAST);
		// gettins service status send period.
		long period = conf
				.getIntValue(Constant.PROP_NAME_SERVICE_STATUS_UPDATE_TIME);

		// creating a runnable object.
		Runnable serviceStatusThread = new Runnable() {

			@Override
			public void run() {
				try {
					// getting service status.
					Map<String, Boolean> serviceStatus = provider
							.getServiceStatus(conf.getJPSServices());
					// getting ganglia service status.
					serviceStatus.putAll(provider.getGangliaServiceStatus(conf
							.getGangliaServices()));

					serviceStatus.putAll(provider.getProcessPortStatus((conf
							.getProcessPortList())));

					// sending service status.
					restClient.sendData(serviceStatus, url);
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}

			}
		};
		// scheduling the runnable for periodical execution.
		service.scheduleAtFixedRate(serviceStatusThread, 0, period,
				TimeUnit.SECONDS);
	}
}
