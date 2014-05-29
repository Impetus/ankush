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
package com.impetus.ankush.hadoop.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.impetus.ankush.hadoop.config.Parameter;

/**
 * The Class CapacitySchedulerConfig.
 *
 * @author mayur
 */
@XmlRootElement(name = "configuration")
public class CapacitySchedulerConfig extends SchedulerConfig {

	/** The Constant MAPRED_CAPACITY_SCHEDULER. */
	public static final String MAPRED_CAPACITY_SCHEDULER = "mapred.capacity-scheduler.";
	
	/** The Constant MAPRED_CAPACITY_SCHEDULER_QUEUE. */
	public static final String MAPRED_CAPACITY_SCHEDULER_QUEUE = "mapred.capacity-scheduler.queue.";
	
	/** The queues. */
	private List<Queue> queues = new ArrayList<CapacitySchedulerConfig.Queue>();
	
	/** The commons. */
	private CommonValues commons = new CommonValues();
	
	/** The defaults. */
	private DefaultValues defaults = new DefaultValues();

	/**
	 * The Class Queue.
	 *
	 * @author mayur
	 */
	public static class Queue {

		/** The name. */
		private String name;
		
		/** The capacity. */
		private int capacity = -1;
		
		/** The max capacity. */
		private int maxCapacity = -1;
		
		/** The min user limit. */
		private int minUserLimit = 100; // percentage
		
		/** The user limit. */
		private int userLimit = 1;
		
		/** The priority support. */
		private boolean prioritySupport;
		
		/** The max active tasks. */
		private int maxActiveTasks = 20; // percentage
		
		/** The max active tasks per user. */
		private int maxActiveTasksPerUser = 10;
		
		/** The job count. */
		private int jobCount = 10;

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the name.
		 *
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Gets the capacity.
		 *
		 * @return the capacity
		 */
		public int getCapacity() {
			return capacity;
		}

		/**
		 * Sets the capacity.
		 *
		 * @param capacity the capacity to set
		 */
		public void setCapacity(int capacity) {
			this.capacity = capacity;
		}

		/**
		 * Gets the max capacity.
		 *
		 * @return the maxCapacity
		 */
		public int getMaxCapacity() {
			return maxCapacity;
		}

		/**
		 * Sets the max capacity.
		 *
		 * @param maxCapacity the maxCapacity to set
		 */
		public void setMaxCapacity(int maxCapacity) {
			this.maxCapacity = maxCapacity;
		}

		/**
		 * Gets the min user limit.
		 *
		 * @return the minUserLimit
		 */
		public int getMinUserLimit() {
			return minUserLimit;
		}

		/**
		 * Sets the min user limit.
		 *
		 * @param minUserLimit the minUserLimit to set
		 */
		public void setMinUserLimit(int minUserLimit) {
			this.minUserLimit = minUserLimit;
		}

		/**
		 * Gets the user limit.
		 *
		 * @return the userLimit
		 */
		public int getUserLimit() {
			return userLimit;
		}

		/**
		 * Sets the user limit.
		 *
		 * @param userLimit the userLimit to set
		 */
		public void setUserLimit(int userLimit) {
			this.userLimit = userLimit;
		}

		/**
		 * Checks if is priority support.
		 *
		 * @return the prioritySupport
		 */
		public boolean isPrioritySupport() {
			return prioritySupport;
		}

		/**
		 * Sets the priority support.
		 *
		 * @param prioritySupport the prioritySupport to set
		 */
		public void setPrioritySupport(boolean prioritySupport) {
			this.prioritySupport = prioritySupport;
		}

		/**
		 * Gets the max active tasks.
		 *
		 * @return the maxActiveTasks
		 */
		public int getMaxActiveTasks() {
			return maxActiveTasks;
		}

		/**
		 * Sets the max active tasks.
		 *
		 * @param maxActiveTasks the maxActiveTasks to set
		 */
		public void setMaxActiveTasks(int maxActiveTasks) {
			this.maxActiveTasks = maxActiveTasks;
		}

		/**
		 * Gets the max active tasks per user.
		 *
		 * @return the maxActiveTasksPerUser
		 */
		public int getMaxActiveTasksPerUser() {
			return maxActiveTasksPerUser;
		}

		/**
		 * Sets the max active tasks per user.
		 *
		 * @param maxActiveTasksPerUser the maxActiveTasksPerUser to set
		 */
		public void setMaxActiveTasksPerUser(int maxActiveTasksPerUser) {
			this.maxActiveTasksPerUser = maxActiveTasksPerUser;
		}

		/**
		 * Gets the job count.
		 *
		 * @return the jobCount
		 */
		public int getJobCount() {
			return jobCount;
		}

		/**
		 * Sets the job count.
		 *
		 * @param jobCount the jobCount to set
		 */
		public void setJobCount(int jobCount) {
			this.jobCount = jobCount;
		}
	};

	/**
	 * The Class DefaultValues.
	 *
	 * @author mayur
	 */
	public class DefaultValues {

		/** The max system jobs. */
		private int maxSystemJobs = 4;
		
		/** The priority support. */
		private boolean prioritySupport = false;
		
		/** The min user limit. */
		private int minUserLimit = 100; // in percentage
		
		/** The max active tasks per queue. */
		private int maxActiveTasksPerQueue = 5;
		
		/** The max active tasks per user. */
		private int maxActiveTasksPerUser = 20;
		
		/** The job count. */
		private int jobCount = 10;

		/**
		 * Gets the max system jobs.
		 *
		 * @return the maxSystemJobs
		 */
		public int getMaxSystemJobs() {
			return maxSystemJobs;
		}

		/**
		 * Sets the max system jobs.
		 *
		 * @param maxSystemJobs the maxSystemJobs to set
		 */
		public void setMaxSystemJobs(int maxSystemJobs) {
			this.maxSystemJobs = maxSystemJobs;
		}

		/**
		 * Checks if is priority support.
		 *
		 * @return the prioritySupport
		 */
		public boolean isPrioritySupport() {
			return prioritySupport;
		}

		/**
		 * Sets the priority support.
		 *
		 * @param prioritySupport the prioritySupport to set
		 */
		public void setPrioritySupport(boolean prioritySupport) {
			this.prioritySupport = prioritySupport;
		}

		/**
		 * Gets the min user limit.
		 *
		 * @return the minUserLimit
		 */
		public int getMinUserLimit() {
			return minUserLimit;
		}

		/**
		 * Sets the min user limit.
		 *
		 * @param minUserLimit the minUserLimit to set
		 */
		public void setMinUserLimit(int minUserLimit) {
			this.minUserLimit = minUserLimit;
		}

		/**
		 * Gets the max active tasks per queue.
		 *
		 * @return the maxActiveTasksPerQueue
		 */
		public int getMaxActiveTasksPerQueue() {
			return maxActiveTasksPerQueue;
		}

		/**
		 * Sets the max active tasks per queue.
		 *
		 * @param maxActiveTasksPerQueue the maxActiveTasksPerQueue to set
		 */
		public void setMaxActiveTasksPerQueue(int maxActiveTasksPerQueue) {
			this.maxActiveTasksPerQueue = maxActiveTasksPerQueue;
		}

		/**
		 * Gets the max active tasks per user.
		 *
		 * @return the maxActiveTasksPerUser
		 */
		public int getMaxActiveTasksPerUser() {
			return maxActiveTasksPerUser;
		}

		/**
		 * Sets the max active tasks per user.
		 *
		 * @param maxActiveTasksPerUser the maxActiveTasksPerUser to set
		 */
		public void setMaxActiveTasksPerUser(int maxActiveTasksPerUser) {
			this.maxActiveTasksPerUser = maxActiveTasksPerUser;
		}

		/**
		 * Gets the job count.
		 *
		 * @return the jobCount
		 */
		public int getJobCount() {
			return jobCount;
		}

		/**
		 * Sets the job count.
		 *
		 * @param jobCount the jobCount to set
		 */
		public void setJobCount(int jobCount) {
			this.jobCount = jobCount;
		}
	}

	/**
	 * The Class CommonValues.
	 *
	 * @author mayur
	 */
	public class CommonValues {

		/** The poll interval. */
		private int pollInterval = 5000;
		
		/** The worker threads. */
		private int workerThreads = 5;
		
		/** The access control list. */
		private boolean accessControlList = false;

		/**
		 * Gets the poll interval.
		 *
		 * @return the pollInterval
		 */
		public int getPollInterval() {
			return pollInterval;
		}

		/**
		 * Sets the poll interval.
		 *
		 * @param pollInterval the pollInterval to set
		 */
		public void setPollInterval(int pollInterval) {
			this.pollInterval = pollInterval;
		}

		/**
		 * Gets the worker threads.
		 *
		 * @return the workerThreads
		 */
		public int getWorkerThreads() {
			return workerThreads;
		}

		/**
		 * Sets the worker threads.
		 *
		 * @param workerThreads the workerThreads to set
		 */
		public void setWorkerThreads(int workerThreads) {
			this.workerThreads = workerThreads;
		}

		/**
		 * Checks if is access control list.
		 *
		 * @return the accessControlList
		 */
		public boolean isAccessControlList() {
			return accessControlList;
		}

		/**
		 * Sets the access control list.
		 *
		 * @param accessControlList the accessControlList to set
		 */
		public void setAccessControlList(boolean accessControlList) {
			this.accessControlList = accessControlList;
		}
	}

	/**
	 * Gets the commons.
	 *
	 * @return the commons
	 */
	public CommonValues getCommons() {
		return commons;
	}

	/**
	 * Sets the commons.
	 *
	 * @param commons the commons to set
	 */
	public void setCommons(CommonValues commons) {
		this.commons = commons;
	}

	/**
	 * Gets the defaults.
	 *
	 * @return the defaults
	 */
	public DefaultValues getDefaults() {
		return defaults;
	}

	/**
	 * Sets the defaults.
	 *
	 * @param defaults the defaults to set
	 */
	public void setDefaults(DefaultValues defaults) {
		this.defaults = defaults;
	}

	/**
	 * Gets the queues.
	 *
	 * @return the queues
	 */
	public List<Queue> getQueues() {
		return queues;
	}

	/**
	 * Sets the queues.
	 *
	 * @param queues the new queues
	 */
	public void setQueues(List<Queue> queues) {
		this.queues = queues;
	}

	/**
	 * Instantiates a new capacity scheduler config.
	 */
	public CapacitySchedulerConfig() {
		super("org.apache.hadoop.mapred.CapacityTaskScheduler");
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#toParameters()
	 */
	public List<Parameter> toParameters() {
		return Arrays.asList(new Parameter("mapred.jobtracker.taskScheduler",
				this.getSchedulerClassName()), new Parameter(
				"mapred.queue.names", getQueueNames()), new Parameter(
				"mapred.acls.enabled", this.commons.accessControlList));
	}

	/**
	 * Gets the queue names.
	 *
	 * @return the queue names
	 */
	private String getQueueNames() {
		List<String> queueNames = new ArrayList<String>(this.getQueues().size());
		for (Queue queue : this.getQueues()) {
			queueNames.add(queue.name);
		}
		return StringUtils.join(queueNames, ',');
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#toXML()
	 */
	public String toXML() throws Exception {
		return parametersToString(getSchedulerParameters());
	}

	/**
	 * Gets the scheduler parameters.
	 *
	 * @return the scheduler parameters
	 */
	@JsonIgnore
	public List<Parameter> getSchedulerParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>(Arrays.asList(
				new Parameter(MAPRED_CAPACITY_SCHEDULER
						+ "default-supports-priority",
						this.defaults.prioritySupport), new Parameter(
						MAPRED_CAPACITY_SCHEDULER
								+ "default-minimum-user-limit-percent",
						this.defaults.minUserLimit), new Parameter(
						MAPRED_CAPACITY_SCHEDULER
								+ "default-maximum-active-tasks-per-user",
						this.defaults.maxActiveTasksPerUser), new Parameter(
						MAPRED_CAPACITY_SCHEDULER
								+ "default-maximum-active-tasks-per-queue",
						this.defaults.maxActiveTasksPerQueue), new Parameter(
						MAPRED_CAPACITY_SCHEDULER
								+ "default-init-accept-jobs-factor",
						this.defaults.jobCount), new Parameter(
						MAPRED_CAPACITY_SCHEDULER + "init-poll-interval",
						this.commons.pollInterval), new Parameter(
						MAPRED_CAPACITY_SCHEDULER + "maximum-system-jobs",
						this.defaults.maxSystemJobs), new Parameter(
						MAPRED_CAPACITY_SCHEDULER + "init-worker-threads",
						this.commons.workerThreads)));
		for (Queue queue : this.getQueues()) {
			parameters
					.addAll(Arrays
							.asList(new Parameter(
									MAPRED_CAPACITY_SCHEDULER_QUEUE
											+ queue.name + ".capacity",
									queue.capacity),
									new Parameter(
											MAPRED_CAPACITY_SCHEDULER_QUEUE
													+ queue.name
													+ ".maximum-capacity",
											queue.maxCapacity),
									new Parameter(
											MAPRED_CAPACITY_SCHEDULER_QUEUE
													+ queue.name
													+ ".minimum-user-limit-percent",
											queue.minUserLimit),
									new Parameter(
											MAPRED_CAPACITY_SCHEDULER_QUEUE
													+ queue.name
													+ ".user-limit-factor",
											queue.userLimit),
									new Parameter(
											MAPRED_CAPACITY_SCHEDULER_QUEUE
													+ queue.name
													+ ".supports-priority",
											queue.prioritySupport),
									new Parameter(
											MAPRED_CAPACITY_SCHEDULER_QUEUE
													+ queue.name
													+ ".maximum-initialized-active-tasks",
											queue.maxActiveTasks),
									new Parameter(
											MAPRED_CAPACITY_SCHEDULER_QUEUE
													+ queue.name
													+ ".maximum-initialized-active-tasks-per-user",
											queue.maxActiveTasksPerUser),
									new Parameter(
											MAPRED_CAPACITY_SCHEDULER_QUEUE
													+ queue.name
													+ ".init-accept-jobs-factor",
											queue.jobCount)));
		}
		return parameters;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#allParameters()
	 */
	@Override
	public List<Parameter> allParameters() {
		return Arrays.asList(new Parameter("mapred.jobtracker.taskScheduler",
				null), new Parameter("mapred.queue.names", null),
				new Parameter("mapred.acls.enabled", null));
	}
}
