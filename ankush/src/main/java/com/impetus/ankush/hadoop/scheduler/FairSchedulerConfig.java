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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.OutputKeys;

import com.impetus.ankush.hadoop.config.Parameter;
import com.jamesmurty.utils.XMLBuilder;

/**
 * The Class FairSchedulerConfig.
 *
 * @author mayur
 */
@XmlRootElement(name = "allocations")
public class FairSchedulerConfig extends SchedulerConfig {
	
	/**
	 * The Class DefaultValues.
	 *
	 * @author mayur
	 */
	public class DefaultValues {
		
		/** The pool max jobs default. */
		private int poolMaxJobsDefault = 20;
		
		/** The user max jobs default. */
		private int userMaxJobsDefault = 10;
		
		/** The default min share preemption timeout. */
		private int defaultMinSharePreemptionTimeout = 600;
		
		/** The fair share preemption timeout. */
		private int fairSharePreemptionTimeout = 600;
		
		/** The default pool scheduling mode. */
		private String defaultPoolSchedulingMode = "fair";
		
		/**
		 * Gets the pool max jobs default.
		 *
		 * @return the poolMaxJobsDefault
		 */
		public int getPoolMaxJobsDefault() {
			return poolMaxJobsDefault;
		}
		
		/**
		 * Sets the pool max jobs default.
		 *
		 * @param poolMaxJobsDefault the poolMaxJobsDefault to set
		 */
		public void setPoolMaxJobsDefault(int poolMaxJobsDefault) {
			this.poolMaxJobsDefault = poolMaxJobsDefault;
		}
		
		/**
		 * Gets the user max jobs default.
		 *
		 * @return the userMaxJobsDefault
		 */
		public int getUserMaxJobsDefault() {
			return userMaxJobsDefault;
		}
		
		/**
		 * Sets the user max jobs default.
		 *
		 * @param userMaxJobsDefault the userMaxJobsDefault to set
		 */
		public void setUserMaxJobsDefault(int userMaxJobsDefault) {
			this.userMaxJobsDefault = userMaxJobsDefault;
		}
		
		/**
		 * Gets the default min share preemption timeout.
		 *
		 * @return the defaultMinSharePreemptionTimeout
		 */
		public int getDefaultMinSharePreemptionTimeout() {
			return defaultMinSharePreemptionTimeout;
		}
		
		/**
		 * Sets the default min share preemption timeout.
		 *
		 * @param defaultMinSharePreemptionTimeout the defaultMinSharePreemptionTimeout to set
		 */
		public void setDefaultMinSharePreemptionTimeout(
				int defaultMinSharePreemptionTimeout) {
			this.defaultMinSharePreemptionTimeout = defaultMinSharePreemptionTimeout;
		}
		
		/**
		 * Gets the fair share preemption timeout.
		 *
		 * @return the fairSharePreemptionTimeout
		 */
		public int getFairSharePreemptionTimeout() {
			return fairSharePreemptionTimeout;
		}
		
		/**
		 * Sets the fair share preemption timeout.
		 *
		 * @param fairSharePreemptionTimeout the fairSharePreemptionTimeout to set
		 */
		public void setFairSharePreemptionTimeout(int fairSharePreemptionTimeout) {
			this.fairSharePreemptionTimeout = fairSharePreemptionTimeout;
		}
		
		/**
		 * Gets the default pool scheduling mode.
		 *
		 * @return the defaultPoolSchedulingMode
		 */
		public String getDefaultPoolSchedulingMode() {
			return defaultPoolSchedulingMode;
		}
		
		/**
		 * Sets the default pool scheduling mode.
		 *
		 * @param defaultPoolSchedulingMode the defaultPoolSchedulingMode to set
		 */
		public void setDefaultPoolSchedulingMode(String defaultPoolSchedulingMode) {
			this.defaultPoolSchedulingMode = defaultPoolSchedulingMode;
		}		
	}

	/**
	 * The Class CommonValues.
	 *
	 * @author mayur
	 */
	public class CommonValues {
		
		/** The pool. */
		private String pool;
		
		/** The poolname property. */
		private String poolnameProperty = "user.name";
		
		/** The preemption. */
		private boolean preemption = false;
		
		/** The allocation file. */
		private String allocationFile = "";
		
		/** The size based weight. */
		private boolean sizeBasedWeight = false;
		
		/** The preemption only log. */
		private boolean preemptionOnlyLog = false;
		
		/** The update interval. */
		private int updateInterval = 500;
		
		/** The preemption interval. */
		private int preemptionInterval = 15000;
		
		/** The weight adjuster class. */
		private String weightAdjusterClass;
		
		/** The load manager class. */
		private String loadManagerClass;
		
		/** The task selector class. */
		private String taskSelectorClass;
		
		/**
		 * Gets the pool.
		 *
		 * @return the pool
		 */
		public String getPool() {
			return pool;
		}
		
		/**
		 * Sets the pool.
		 *
		 * @param pool the pool to set
		 */
		public void setPool(String pool) {
			this.pool = pool;
		}
		
		/**
		 * Gets the poolname property.
		 *
		 * @return the poolnameProperty
		 */
		public String getPoolnameProperty() {
			return poolnameProperty;
		}
		
		/**
		 * Sets the poolname property.
		 *
		 * @param poolnameProperty the poolnameProperty to set
		 */
		public void setPoolnameProperty(String poolnameProperty) {
			this.poolnameProperty = poolnameProperty;
		}
		
		/**
		 * Checks if is preemption.
		 *
		 * @return the preemption
		 */
		public boolean isPreemption() {
			return preemption;
		}
		
		/**
		 * Sets the preemption.
		 *
		 * @param preemption the preemption to set
		 */
		public void setPreemption(boolean preemption) {
			this.preemption = preemption;
		}
		
		/**
		 * Gets the allocation file.
		 *
		 * @return the allocationFile
		 */
		public String getAllocationFile() {
			return allocationFile;
		}
		
		/**
		 * Sets the allocation file.
		 *
		 * @param allocationFile the allocationFile to set
		 */
		public void setAllocationFile(String allocationFile) {
			this.allocationFile = allocationFile;
		}
		
		/**
		 * Checks if is size based weight.
		 *
		 * @return the sizeBasedWeight
		 */
		public boolean isSizeBasedWeight() {
			return sizeBasedWeight;
		}
		
		/**
		 * Sets the size based weight.
		 *
		 * @param sizeBasedWeight the sizeBasedWeight to set
		 */
		public void setSizeBasedWeight(boolean sizeBasedWeight) {
			this.sizeBasedWeight = sizeBasedWeight;
		}
		
		/**
		 * Checks if is preemption only log.
		 *
		 * @return the preemptionOnlyLog
		 */
		public boolean isPreemptionOnlyLog() {
			return preemptionOnlyLog;
		}
		
		/**
		 * Sets the preemption only log.
		 *
		 * @param preemptionOnlyLog the preemptionOnlyLog to set
		 */
		public void setPreemptionOnlyLog(boolean preemptionOnlyLog) {
			this.preemptionOnlyLog = preemptionOnlyLog;
		}
		
		/**
		 * Gets the update interval.
		 *
		 * @return the updateInterval
		 */
		public int getUpdateInterval() {
			return updateInterval;
		}
		
		/**
		 * Sets the update interval.
		 *
		 * @param updateInterval the updateInterval to set
		 */
		public void setUpdateInterval(int updateInterval) {
			this.updateInterval = updateInterval;
		}
		
		/**
		 * Gets the preemption interval.
		 *
		 * @return the preemptionInterval
		 */
		public int getPreemptionInterval() {
			return preemptionInterval;
		}
		
		/**
		 * Sets the preemption interval.
		 *
		 * @param preemptionInterval the preemptionInterval to set
		 */
		public void setPreemptionInterval(int preemptionInterval) {
			this.preemptionInterval = preemptionInterval;
		}
		
		/**
		 * Gets the weight adjuster class.
		 *
		 * @return the weightAdjusterClass
		 */
		public String getWeightAdjusterClass() {
			return weightAdjusterClass;
		}
		
		/**
		 * Sets the weight adjuster class.
		 *
		 * @param weightAdjusterClass the weightAdjusterClass to set
		 */
		public void setWeightAdjusterClass(String weightAdjusterClass) {
			this.weightAdjusterClass = weightAdjusterClass;
		}
		
		/**
		 * Gets the load manager class.
		 *
		 * @return the loadManagerClass
		 */
		public String getLoadManagerClass() {
			return loadManagerClass;
		}
		
		/**
		 * Sets the load manager class.
		 *
		 * @param loadManagerClass the loadManagerClass to set
		 */
		public void setLoadManagerClass(String loadManagerClass) {
			this.loadManagerClass = loadManagerClass;
		}
		
		/**
		 * Gets the task selector class.
		 *
		 * @return the taskSelectorClass
		 */
		public String getTaskSelectorClass() {
			return taskSelectorClass;
		}
		
		/**
		 * Sets the task selector class.
		 *
		 * @param taskSelectorClass the taskSelectorClass to set
		 */
		public void setTaskSelectorClass(String taskSelectorClass) {
			this.taskSelectorClass = taskSelectorClass;
		}	
		
	}

	/** The defaults. */
	private DefaultValues defaults = new DefaultValues();
	
	/** The commons. */
	private CommonValues commons = new CommonValues();
	
	/** The pools. */
	private List<Pool> pools;
	
	/** The users. */
	private List<User> users;

	/**
	 * Sets the defaults.
	 *
	 * @param defaults the defaults to set
	 */
	public void setDefaults(DefaultValues defaults) {
		this.defaults = defaults;
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
	 * Sets the commons.
	 *
	 * @param commons the commons to set
	 */
	public void setCommons(CommonValues commons) {
		this.commons = commons;
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
	 * Gets the users.
	 *
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * Sets the users.
	 *
	 * @param users the new users
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * Gets the pools.
	 *
	 * @return the pools
	 */
	public List<Pool> getPools() {
		return pools;
	}

	/**
	 * Sets the pools.
	 *
	 * @param pools the new pools
	 */
	public void setPools(List<Pool> pools) {
		this.pools = pools;
	}

	/**
	 * Instantiates a new fair scheduler config.
	 */
	public FairSchedulerConfig() {
		super("org.apache.hadoop.mapred.FairScheduler");
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#toParameters()
	 */
	public List<Parameter> toParameters() {
		List<Parameter> parameters = new ArrayList<Parameter>(Arrays.asList(
				new Parameter("mapred.jobtracker.taskScheduler", this
						.getSchedulerClassName()),
				new Parameter("mapred.fairscheduler.allocation.file",
						this.commons.allocationFile),
				new Parameter("mapred.fairscheduler.preemption", Boolean
						.toString(this.commons.preemption))));
		parameters.addAll(Arrays.asList(
				new Parameter("mapred.fairscheduler.sizebasedweight", Boolean
						.toString(this.commons.sizeBasedWeight)),
				new Parameter("mapred.fairscheduler.preemption.only.log",
						Boolean.toString(this.commons.preemptionOnlyLog)),
				new Parameter("mapred.fairscheduler.update.interval", Integer
						.toString(this.commons.updateInterval)),
				new Parameter("mapred.fairscheduler.preemption.interval",
						Integer.valueOf(this.commons.preemptionInterval))));

		if(this.commons.pool.isEmpty()) {
			parameters.add(new Parameter("mapred.fairscheduler.poolnameproperty",
					this.commons.poolnameProperty));
		} else {
			parameters.add(new Parameter("mapred.fairscheduler.pool", this.commons.pool));
		}
		
		if (this.commons.loadManagerClass != null) {
			parameters.add(new Parameter("mapred.fairscheduler.loadmanager",
					this.commons.loadManagerClass));
		}

		if (this.commons.taskSelectorClass != null) {
			parameters.add(new Parameter("mapred.fairscheduler.taskselector",
					this.commons.taskSelectorClass));
		}

		return parameters;
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#allParameters()
	 */
	@Override
	public List<Parameter> allParameters() {
		return Arrays
				.asList(new Parameter("mapred.jobtracker.taskScheduler", null),
						new Parameter("mapred.fairscheduler.allocation.file",
								null),
						new Parameter("mapred.fairscheduler.preemption", null),
						new Parameter("mapred.fairscheduler.pool", null),
						new Parameter("mapred.fairscheduler.poolnameproperty",
								null),
						new Parameter("mapred.fairscheduler.sizebasedweight",
								null),
						new Parameter(
								"mapred.fairscheduler.preemption.only.log",
								null),
						new Parameter("mapred.fairscheduler.update.interval",
								null),
						new Parameter(
								"mapred.fairscheduler.preemption.interval",
								null),
						new Parameter("mapred.fairscheduler.weightadjuster",
								null),
						new Parameter("mapred.newjobweightbooster.factor", null),
						new Parameter("mapred.newjobweightbooster.duration",
								null),
						new Parameter("mapred.fairscheduler.loadmanager", null),
						new Parameter("mapred.fairscheduler.taskselector", null));
	}

	/* (non-Javadoc)
	 * @see com.impetus.ankush.hadoop.scheduler.SchedulerConfig#toXML()
	 */
	public String toXML() throws Exception {
		XMLBuilder builder = XMLBuilder.create("allocations");

		for (Pool pool : this.getPools()) {
			builder.e("pool").a("name", pool.name).e("minMaps")
					.t(Integer.toString(pool.minMaps)).up().e("minReduces")
					.t(Integer.toString(pool.minReduces)).up().e("maxMaps")
					.t(Integer.toString(pool.maxMaps)).up().e("maxReduces")
					.t(Integer.toString(pool.maxReduces)).up()
					.e("schedulingMode").t(pool.schedulingMode).up()
					.e("maxRunningJobs")
					.t(Integer.toString(pool.maxRunningJobs)).up()
					.e("minSharePreemptionTimeout")
					.t(Integer.toString(pool.minSharePreemptionTimeout)).up()
					.e("weight").t(Double.toString(pool.weight));
		}

		for (User user : this.getUsers()) {
			builder.e("user").a("name", user.name).e("maxRunningJobs")
					.t(Integer.toString(user.maxRunningJobs));
		}

		builder.e("poolMaxJobsDefault")
				.t(Integer.toString(this.defaults.poolMaxJobsDefault))
				.up()
				.e("userMaxJobsDefault")
				.t(Integer.toString(this.defaults.userMaxJobsDefault))
				.up()
				.e("defaultMinSharePreemptionTimeout")
				.t(Integer
						.toString(this.defaults.defaultMinSharePreemptionTimeout))
				.up().e("fairSharePreemptionTimeout")
				.t(Integer.toString(this.defaults.fairSharePreemptionTimeout))
				.up().e("defaultPoolSchedulingMode")
				.t(this.defaults.defaultPoolSchedulingMode).up();

		StringWriter writer = new StringWriter();
		Properties properties = new Properties();
		properties.put(OutputKeys.INDENT, "yes");
		builder.toWriter(writer, properties);
		return writer.toString().replaceAll("\\\"", "\\\\\"");
	}

	/**
	 * The Class Pool.
	 *
	 * @author mayur
	 */
	public static class Pool {
		
		/** The name. */
		private String name;
		
		/** The min maps. */
		private int minMaps = 0;
		
		/** The min reduces. */
		private int minReduces = 0;
		
		/** The max maps. */
		private int maxMaps;
		
		/** The max reduces. */
		private int maxReduces;
		
		/** The scheduling mode. */
		private String schedulingMode = "fair";
		
		/** The max running jobs. */
		private int maxRunningJobs = 5;
		
		/** The min share preemption timeout. */
		private int minSharePreemptionTimeout = 300;
		
		/** The weight. */
		private double weight = 1.0;
		
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
		 * Gets the min maps.
		 *
		 * @return the minMaps
		 */
		public int getMinMaps() {
			return minMaps;
		}
		
		/**
		 * Sets the min maps.
		 *
		 * @param minMaps the minMaps to set
		 */
		public void setMinMaps(int minMaps) {
			this.minMaps = minMaps;
		}
		
		/**
		 * Gets the min reduces.
		 *
		 * @return the minReduces
		 */
		public int getMinReduces() {
			return minReduces;
		}
		
		/**
		 * Sets the min reduces.
		 *
		 * @param minReduces the minReduces to set
		 */
		public void setMinReduces(int minReduces) {
			this.minReduces = minReduces;
		}
		
		/**
		 * Gets the max maps.
		 *
		 * @return the maxMaps
		 */
		public int getMaxMaps() {
			return maxMaps;
		}
		
		/**
		 * Sets the max maps.
		 *
		 * @param maxMaps the maxMaps to set
		 */
		public void setMaxMaps(int maxMaps) {
			this.maxMaps = maxMaps;
		}
		
		/**
		 * Gets the max reduces.
		 *
		 * @return the maxReduces
		 */
		public int getMaxReduces() {
			return maxReduces;
		}
		
		/**
		 * Sets the max reduces.
		 *
		 * @param maxReduces the maxReduces to set
		 */
		public void setMaxReduces(int maxReduces) {
			this.maxReduces = maxReduces;
		}
		
		/**
		 * Gets the scheduling mode.
		 *
		 * @return the schedulingMode
		 */
		public String getSchedulingMode() {
			return schedulingMode;
		}
		
		/**
		 * Sets the scheduling mode.
		 *
		 * @param schedulingMode the schedulingMode to set
		 */
		public void setSchedulingMode(String schedulingMode) {
			this.schedulingMode = schedulingMode;
		}
		
		/**
		 * Gets the max running jobs.
		 *
		 * @return the maxRunningJobs
		 */
		public int getMaxRunningJobs() {
			return maxRunningJobs;
		}
		
		/**
		 * Sets the max running jobs.
		 *
		 * @param maxRunningJobs the maxRunningJobs to set
		 */
		public void setMaxRunningJobs(int maxRunningJobs) {
			this.maxRunningJobs = maxRunningJobs;
		}
		
		/**
		 * Gets the min share preemption timeout.
		 *
		 * @return the minSharePreemptionTimeout
		 */
		public int getMinSharePreemptionTimeout() {
			return minSharePreemptionTimeout;
		}
		
		/**
		 * Sets the min share preemption timeout.
		 *
		 * @param minSharePreemptionTimeout the minSharePreemptionTimeout to set
		 */
		public void setMinSharePreemptionTimeout(int minSharePreemptionTimeout) {
			this.minSharePreemptionTimeout = minSharePreemptionTimeout;
		}
		
		/**
		 * Gets the weight.
		 *
		 * @return the weight
		 */
		public double getWeight() {
			return weight;
		}
		
		/**
		 * Sets the weight.
		 *
		 * @param weight the weight to set
		 */
		public void setWeight(double weight) {
			this.weight = weight;
		}		
	}

	/**
	 * The Class User.
	 *
	 * @author mayur
	 */
	public static class User {
		
		/** The name. */
		private String name;
		
		/** The max running jobs. */
		private int maxRunningJobs;
		
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
		 * Gets the max running jobs.
		 *
		 * @return the maxRunningJobs
		 */
		public int getMaxRunningJobs() {
			return maxRunningJobs;
		}
		
		/**
		 * Sets the max running jobs.
		 *
		 * @param maxRunningJobs the maxRunningJobs to set
		 */
		public void setMaxRunningJobs(int maxRunningJobs) {
			this.maxRunningJobs = maxRunningJobs;
		}		
	}
}
