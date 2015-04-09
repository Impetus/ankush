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
package com.impetus.ankush.agent.sigar;

import java.util.concurrent.TimeUnit;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;

/**
 * @author Hokam Chauhan
 * 
 */
public class SigarInfoMonitor extends Taskable {

	private static final AgentLogger LOGGER = new AgentLogger(
			SigarInfoMonitor.class);
	private AgentConf agentConf;
	private AgentRestClient client;

	/**
	 * Constructor for SigarInfoMonitor.
	 * 
	 * @param conf
	 *            AgentConf
	 * @param client
	 *            AgentRestClient
	 */
	public SigarInfoMonitor() {
		this.agentConf = new AgentConf();
		this.client = new AgentRestClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.impetus.ankush.agent.action.Taskable#start()
	 */
	public void start() {
		LOGGER.info("SigarInfoMonitor start");
		try {
			symmetricMonitoringInfo();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Symmetric monitoring info.
	 */
	public void symmetricMonitoringInfo() {

		final SigarNodeInfoProvider provider = new SigarNodeInfoProvider();

		int commonInfoSendTime = agentConf
				.getIntValue(Constant.PROP_NAME_COMMON_UPDATE_TIME);
		final String commonInfoSendUrl = agentConf
				.getURL(Constant.PROP_NAME_URL_COMMON);

		Runnable info = new Runnable() {
			@Override
			public void run() {
				client.sendData(provider.getNodeInfo(agentConf
						.getIntValue(Constant.PROP_NAME_TOP_PROCESS_COUNT)),
						commonInfoSendUrl);

			}
		};
		service.scheduleAtFixedRate(info, 0, commonInfoSendTime,
				TimeUnit.SECONDS);
	}
}
