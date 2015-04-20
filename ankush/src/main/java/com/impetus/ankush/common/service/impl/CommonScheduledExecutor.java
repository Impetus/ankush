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
package com.impetus.ankush.common.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.impetus.ankush.AppStore;
import com.impetus.ankush2.db.DBEventManager;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * CommonScheduledExecutor is the class which is used to schedule all such
 * actions that needs to be executed periodically after some fixed time
 * interval.
 */

@Service
public class CommonScheduledExecutor {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			CommonScheduledExecutor.class);

	/**
	 * Check down agents.
	 */
	@Scheduled(fixedDelay = 30000)
	public void checkDownAgents() {
		try {
			Integer initialDelay = (Integer) AppStore
					.removeObject(com.impetus.ankush2.constant.Constant.Keys.KEY_INITIAL_DELAY_AGENT_CHECK);

			// if initial delay is not null then sleep
			if (initialDelay != null) {
				Thread.sleep(initialDelay);
			}
			logger.debug("Started check down agents.");			
			// Calling process agent down alerts of event manager
			new DBEventManager().processAgentDownAlerts();
		} catch (Exception e) {
			// if failed to
			logger.error("Unable to initialize the manager " + e.getMessage(),
					e);
		}
	}
}
