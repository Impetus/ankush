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

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.utils.AnkushLogger;

/**
 * ScheduledActionsExecutionService is the class which is used to schedule all
 * such actions that needs to be executed periodically after some fixed time
 * interval.
 */

@Service
@EnableScheduling
public class ScheduledActionsExecutionService {

	/** The logger. */
	private AnkushLogger logger = new AnkushLogger(
			ScheduledActionsExecutionService.class);

	/**
	 * Check down agents.
	 */
	@Scheduled(fixedDelay = 100001)
	public void checkDownAgents() {
		try {
			EventManager manager = new EventManager();
			manager.processAgentDownAlerts();
		} catch (Exception e) {
			logger.error("Unable to initialize the manager " + e.getMessage());
		}
	}
}
