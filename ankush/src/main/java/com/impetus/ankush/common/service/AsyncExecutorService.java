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
package com.impetus.ankush.common.service;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * The Interface AsyncExecutorService.
 */
public interface AsyncExecutorService {

	/**
	 * Schedule.
	 * 
	 * @param runnable
	 *            the runnable
	 * @param start
	 *            the start
	 * @return the scheduled future
	 */
	ScheduledFuture schedule(Runnable runnable, Date start);

	/**
	 * 
	 * @param runnable
	 * @param seconds
	 * @return
	 */
	ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long seconds);

	/**
	 * Execute.
	 * 
	 * @param task
	 *            the task
	 */
	void execute(Runnable task);

}
