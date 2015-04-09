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

import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import com.impetus.ankush.common.service.AsyncExecutorService;

/**
 * The Class AsyncExecutorServiceImpl.
 */
@Service
public class AsyncExecutorServiceImpl extends ThreadPoolExecutor implements
		AsyncExecutorService {

	/** The Constant MIN_THREAD_POOL_SIZE. */
	private static final int MIN_THREAD_POOL_SIZE = 0;

	/** The Constant MAX_THREAD_POOL_SIZE. */
	private static final int MAX_THREAD_POOL_SIZE = 100;

	/** The Constant KEEP_ALIVE_TIME. */
	private static final long KEEP_ALIVE_TIME = 60L;

	/** The log. */
	private final Logger log = LoggerFactory
			.getLogger(AsyncExecutorServiceImpl.class);

	/** The task scheduler. */
	private TaskScheduler taskScheduler;

	/**
	 * Instantiates a new async executor service impl.
	 */
	public AsyncExecutorServiceImpl() {
		super(MIN_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE, KEEP_ALIVE_TIME,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
				new CustomizableThreadFactory());
		ThreadFactory factory = this.getThreadFactory();
		if (factory instanceof CustomizableThreadFactory) {
			CustomizableThreadFactory customizableThreadFactory = (CustomizableThreadFactory) factory;
			customizableThreadFactory
					.setThreadNamePrefix("AnkushProgressAwareThread_");
			customizableThreadFactory.setDaemon(true);
		}
	}

	/**
	 * Sets the task scheduler.
	 * 
	 * @param taskScheduler
	 *            the new task scheduler
	 */
	@Autowired
	public void setTaskScheduler(
			@Qualifier("pooledScheduler") TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.AsyncExecutorService#schedule(java.
	 * lang.Runnable, java.util.Date)
	 */
	@Override
	public ScheduledFuture schedule(final Runnable runnable, final Date start) {
		return taskScheduler.schedule(runnable, start);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.util.concurrent.ThreadPoolExecutor#afterExecute(java.lang.Runnable,
	 * java.lang.Throwable)
	 */
	@Override
	protected void afterExecute(Runnable runnable, Throwable t) {
		if (t != null) {
			log.error(t.getMessage(), t);
		}
		super.afterExecute(runnable, t);
	}

	@Override
	public ScheduledFuture scheduleWithFixedDelay(Runnable runnable,
			long seconds) {
		return taskScheduler.scheduleWithFixedDelay(runnable, seconds * 1000);
	}
}
