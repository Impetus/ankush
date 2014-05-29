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
package com.impetus.ankush.hadoop.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.neoremind.sshxcute.core.Result;
import net.neoremind.sshxcute.core.SSHExec;
import net.neoremind.sshxcute.task.CustomTask;
import net.neoremind.sshxcute.task.impl.ExecCommand;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.impetus.ankush.common.alerts.EventManager;
import com.impetus.ankush.common.config.XMLManipulator;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.framework.config.ClusterConf;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.FileUtils;
import com.impetus.ankush.common.utils.JsonMapperUtil;
import com.impetus.ankush.common.utils.SSHUtils;
import com.impetus.ankush.hadoop.ecosystem.hadoop2.Hadoop2Conf;
import com.impetus.ankush.hadoop.config.Parameter;
import com.impetus.ankush.hadoop.ecosystem.hadoop1.HadoopConf;
import com.impetus.ankush.hadoop.scheduler.CapacitySchedulerConfig;
import com.impetus.ankush.hadoop.scheduler.CapacitySchedulerConfig.Queue;
import com.impetus.ankush.hadoop.scheduler.DefaultSchedulerConfig;
import com.impetus.ankush.hadoop.scheduler.FairSchedulerConfig;
import com.impetus.ankush.hadoop.scheduler.FairSchedulerConfig.Pool;
import com.impetus.ankush.hadoop.scheduler.FairSchedulerConfig.User;
import com.impetus.ankush.hadoop.scheduler.SchedulerConfig;
import com.impetus.ankush.hadoop.service.SchedulerConfigService;

/**
 * The Class SchedulerConfigServiceImpl.
 */
@Service
public class SchedulerConfigServiceImpl implements SchedulerConfigService {

	/** The Constant ERROR. */
	private static final String ERROR = Constant.Keys.ERROR;

	/** The Constant STATUS. */
	private static final String STATUS = Constant.Keys.STATUS;

	/** The cluster manager. */
	private GenericManager<Cluster, Long> clusterManager;

	/** The log. */
	private final AnkushLogger LOG = new AnkushLogger(
			SchedulerConfigServiceImpl.class);

	/**
	 * Sets the cluster manager.
	 *
	 * @param clusterManager the cluster manager
	 */
	@Autowired
	public void setClusterManager(
			@Qualifier(Constant.Manager.CLUSTER) GenericManager<Cluster, Long> clusterManager) {
		this.clusterManager = clusterManager;
	}

	/** The result. */
	private Map result = new HashMap();

	/** The errors. */
	private List<String> errors = new ArrayList<String>();

	/**
	 * Adds the error.
	 *
	 * @param error the error
	 */
	private void addError(String error) {
		this.errors.add(error);
	}

	/**
	 * Return result.
	 *
	 * @return the map
	 */
	private Map<String, Object> returnResult() {
		if (errors.isEmpty()) {
			result.put(STATUS, true);
		} else {
			result.put(STATUS, false);
			result.put(ERROR, errors);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.impl.SchedulerConfigService#saveConfig
	 * (java.lang.Long, com.impetus.ankush.hadoop.scheduler.SchedulerConfig)
	 */
	@Override
	public Map saveConfig(Long clusterId, SchedulerConfig config)
			throws Exception {
		result.clear();
		errors.clear();
		Cluster cluster = clusterManager.get(clusterId);

		// cluster conf object.
		ClusterConf conf = cluster.getClusterConf();

		// hadoop conf object.		
		HadoopConf hConf = (HadoopConf) conf.getClusterComponents().get(Constant.Component.Name.HADOOP);
		if(hConf == null) {
			hConf = (Hadoop2Conf) conf.getClusterComponents().get(Constant.Component.Name.HADOOP2);
		}

		boolean isAnyAgentDown = new EventManager().isAnyAgentDown(cluster);
		
		if (isAnyAgentDown) {
			// if any node is down then send the error message.
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
			return returnResult();
		}

		// list of parameters to delete.
		List<Parameter> parameterToDelete = new ArrayList<Parameter>();
		parameterToDelete.addAll(new CapacitySchedulerConfig().allParameters());
		parameterToDelete.addAll(new FairSchedulerConfig().allParameters());

		// delete previous state
		ParameterConfigurator configurator = new ParameterConfigurator();
		String mapredFilePath = hConf.getComponentHome()
				+ "conf/mapred-site.xml";

		// removing the parameter from mapred file.
		configurator.removeParameters(cluster, parameterToDelete,
				mapredFilePath);
		if (config != null) {
			// adding parameters in mapred-site.xml file.
			configurator.addParameters(cluster, config.toParameters(),
					mapredFilePath);

			String filePath = hConf.getComponentHome()
					+ "conf/capacity-scheduler.xml";
			if (config instanceof CapacitySchedulerConfig) {
				configurator
						.writeXMLToConfig(cluster, filePath, config.toXML());
			} else if (config instanceof FairSchedulerConfig) {
				FairSchedulerConfig failFairSchedulerConfig = (FairSchedulerConfig) config;
				String allocationFile = failFairSchedulerConfig.getCommons()
						.getAllocationFile();
				if (allocationFile != null && !allocationFile.isEmpty()) {
					filePath = allocationFile;
				} else {
					filePath = hConf.getComponentHome()
							+ "conf/fair-scheduler.xml";
				}
				configurator
						.writeXMLToConfig(cluster, filePath, config.toXML());
			}
			restartMapredService(conf, hConf.getNamenode().getPublicIp(), hConf.getComponentHome());
		}
		return returnResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.impetus.ankush.common.service.impl.SchedulerConfigService#getConfig
	 * (java.lang.Long)
	 */
	@Override
	public Map getConfig(Long clusterId) throws Exception {

		result.clear();
		errors.clear();
		Cluster cluster = clusterManager.get(clusterId);
		
		boolean isAnyAgentDown = new EventManager().isAnyAgentDown(cluster);
		if (isAnyAgentDown) {
			// if any node is down then send the error message.
			addError(Constant.Agent.AGENT_DOWN_MESSAGE);
		}

		String schedulerClass = null;
		try {
			Map<String, String> mapredXml = getRemoteParamater(cluster,
					"mapred-site.xml");

			SchedulerConfig config = null;
			if (mapredXml.get("mapred.jobtracker.taskScheduler") == null) {
				config = new DefaultSchedulerConfig();
				config.setSchedulerClassName("default");
				schedulerClass = config.getSchedulerClassName();
			} else if (mapredXml.get("mapred.jobtracker.taskScheduler").equals(
					"org.apache.hadoop.mapred.FairScheduler")) {
				schedulerClass = "org.apache.hadoop.mapred.FairScheduler";
				config = getFairScheduler(cluster, mapredXml);
			} else if (mapredXml.get("mapred.jobtracker.taskScheduler").equals(
					"org.apache.hadoop.mapred.CapacityTaskScheduler")) {
				schedulerClass = "org.apache.hadoop.mapred.CapacityTaskScheduler";
				config = getCapacityScheduler(cluster, mapredXml);
			}
			result.putAll(JsonMapperUtil.mapFromObject(config));
		} catch (Exception e) {
			String error = e.getMessage();
			if (error == null && e.getCause() != null) {
				error = e.getCause().getMessage();
			}
			if (error == null) {
				error = "Unable to get scheduler configuration.";
			}
			result.put("schedulerClassName", schedulerClass);
			addError(error);
		}
		return returnResult();
	}

	/**
	 * Gets the fair scheduler.
	 *
	 * @param cluster the cluster
	 * @param mapredXml the mapred xml
	 * @return the fair scheduler
	 * @throws Exception the exception
	 */
	private SchedulerConfig getFairScheduler(Cluster cluster,
			Map<String, String> mapredXml) throws Exception {
		FairSchedulerConfig config = new FairSchedulerConfig();
		String fileName = mapredXml.get("mapred.fairscheduler.allocation.file");

		if (fileName == null) {
			throw new RuntimeException("Allocation file not found for fair scheduler");
		} else {
			config.getCommons().setAllocationFile(fileName);
		}

		if (mapredXml.containsKey("mapred.fairscheduler.preemption")) {
			config.getCommons().setPreemption(
					Boolean.parseBoolean(mapredXml
							.get("mapred.fairscheduler.preemption")));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.pool")) {
			config.getCommons().setPool(
					mapredXml.get("mapred.fairscheduler.pool"));
		}
		if (mapredXml.containsKey("mapred.fairscheduler.poolnameproperty")) {
			config.getCommons().setPoolnameProperty(
					mapredXml.get("mapred.fairscheduler.poolnameproperty"));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.sizebasedweight")) {
			config.getCommons().setSizeBasedWeight(
					Boolean.parseBoolean(mapredXml
							.get("mapred.fairscheduler.sizebasedweight")));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.preemption.only.log")) {
			config.getCommons().setPreemptionOnlyLog(
					Boolean.parseBoolean(mapredXml
							.get("mapred.fairscheduler.preemption.only.log")));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.update.interval")) {
			config.getCommons().setUpdateInterval(
					Integer.parseInt(mapredXml
							.get("mapred.fairscheduler.update.interval")));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.preemption.interval")) {
			config.getCommons().setPreemptionInterval(
					Integer.parseInt(mapredXml
							.get("mapred.fairscheduler.preemption.interval")));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.weightadjuster")) {
			config.getCommons().setWeightAdjusterClass(
					mapredXml.get("mapred.fairscheduler.weightadjuster"));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.loadmanager")) {
			config.getCommons().setLoadManagerClass(
					mapredXml.get("mapred.fairscheduler.loadmanager"));
		}

		if (mapredXml.containsKey("mapred.fairscheduler.taskselector")) {
			config.getCommons().setTaskSelectorClass(
					mapredXml.get("mapred.fairscheduler.taskselector"));
		}

		File tempFile = File
				.createTempFile("Cluster" + cluster.getId(), ".xml");
		ClusterConf conf = cluster.getClusterConf();
		HadoopConf hConf = (HadoopConf) conf.getClusterComponents().get(Constant.Component.Name.HADOOP);
		
		String filePath = fileName;
		String password = conf.getPassword();
		if(!conf.isAuthTypePassword()) {
			password = conf.getPrivateKey();
		}
		FileUtils.writeLines(tempFile.getAbsolutePath(), SSHUtils
				.getFileContents(filePath, hConf.getNamenode().getPublicIp(),
						conf.getUsername(), password, conf.isAuthTypePassword()));

		SAXBuilder builder = new SAXBuilder();
		org.jdom.Document doc = builder.build(tempFile);
		Element element = doc.getRootElement();

		String s;

		s = get(element, "poolMaxJobsDefault");
		if (s != null) {
			config.getDefaults().setPoolMaxJobsDefault(Integer.parseInt(s));
		}

		s = get(element, "userMaxJobsDefault");
		if (s != null) {
			config.getDefaults().setUserMaxJobsDefault(Integer.parseInt(s));
		}

		s = get(element, "defaultMinSharePreemptionTimeout");
		if (s != null) {
			config.getDefaults().setDefaultMinSharePreemptionTimeout(
					Integer.parseInt(s));
		}

		s = get(element, "fairSharePreemptionTimeout");
		if (s != null) {
			config.getDefaults().setFairSharePreemptionTimeout(
					Integer.parseInt(s));
		}

		s = get(element, "defaultPoolSchedulingMode");
		if (s != null) {
			config.getDefaults().setDefaultPoolSchedulingMode(s);
		}

		config.setUsers(new ArrayList<FairSchedulerConfig.User>());
		config.setPools(new ArrayList<FairSchedulerConfig.Pool>());

		List pls = element.getChildren("pool");
		for (int i = 0; i < pls.size(); i++) {
			Pool p = new Pool();

			Element pl = (Element) pls.get(i);

			p.setName(pl.getAttributeValue("name"));

			s = get(pl, "minMaps");
			if (s != null) {
				p.setMinMaps(Integer.parseInt(s));
			}

			s = get(pl, "minReduces");
			if (s != null) {
				p.setMinReduces(Integer.parseInt(s));
			}

			s = get(pl, "maxMaps");
			if (s != null) {
				p.setMaxMaps(Integer.parseInt(s));
			}

			s = get(pl, "maxReduces");
			if (s != null) {
				p.setMaxReduces(Integer.parseInt(s));
			}

			s = get(pl, "schedulingMode");
			if (s != null) {
				p.setSchedulingMode(s);
			}

			s = get(pl, "maxRunningJobs");
			if (s != null) {
				p.setMaxRunningJobs(Integer.parseInt(s));
			}

			s = get(pl, "minSharePreemptionTimeout");
			if (s != null) {
				p.setMinSharePreemptionTimeout(Integer.parseInt(s));
			}

			s = get(pl, "weight");
			if (s != null) {
				p.setWeight(Double.parseDouble(s));
			}

			config.getPools().add(p);
		}

		List users = element.getChildren("user");
		for (int i = 0; i < users.size(); i++) {
			User u = new User();

			Element ul = (Element) users.get(i);

			u.setName(ul.getAttributeValue("name"));

			s = get(ul, "maxRunningJobs");
			if (s != null) {
				u.setMaxRunningJobs(Integer.parseInt(s));
			}

			config.getUsers().add(u);
		}

		tempFile.delete();

		return config;

	}

	/**
	 * Gets the.
	 *
	 * @param element the element
	 * @param string the string
	 * @return the string
	 * @throws Exception the exception
	 */
	private String get(Element element, String string) throws Exception {
		try {
			List child = element.getChildren(string);
			if (child.size() == 0) {
				throw new Exception("Unable to get value of " + string
						+ " from configuration file.");
			}
			return ((Element) child.get(0)).getValue();
		} catch (IndexOutOfBoundsException e) {
			throw new Exception("Unable to get value of " + string
					+ " from configuration file.");
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Gets the capacity scheduler.
	 *
	 * @param cluster the cluster
	 * @param mapred the mapred
	 * @return the capacity scheduler
	 * @throws Exception the exception
	 */
	private SchedulerConfig getCapacityScheduler(Cluster cluster,
			Map<String, String> mapred) throws Exception {
		CapacitySchedulerConfig c = new CapacitySchedulerConfig();

		String[] queueNames = mapred.get("mapred.queue.names").split(",");
		List<Queue> queues = new ArrayList<Queue>(queueNames.length);
		for (String queueName : queueNames) {
			Queue queue = new Queue();
			queue.setName(queueName);
			queues.add(queue);
		}

		Map<String, String> xml = getRemoteParamater(cluster,
				"capacity-scheduler.xml");

		if (xml.containsKey("mapred.capacity-scheduler.default-supports-priority")) {
			c.getDefaults()
					.setPrioritySupport(
							Boolean.parseBoolean(xml
									.get("mapred.capacity-scheduler.default-supports-priority")));
		}
		if (xml.containsKey("mapred.capacity-scheduler.default-minimum-user-limit-percent")) {
			c.getDefaults()
					.setMinUserLimit(
							Integer.parseInt(xml
									.get("mapred.capacity-scheduler.default-minimum-user-limit-percent")));
		}
		if (xml.containsKey("mapred.capacity-scheduler.init-poll-interval")) {
			c.getCommons()
					.setPollInterval(
							Integer.parseInt(xml
									.get("mapred.capacity-scheduler.init-poll-interval")));
		}
		if (xml.containsKey("mapred.capacity-scheduler.init-worker-threads")) {
			c.getCommons()
					.setWorkerThreads(
							Integer.parseInt(xml
									.get("mapred.capacity-scheduler.init-worker-threads")));
		}
		if (xml.containsKey("mapred.capacity-scheduler.maximum-system-jobs")) {
			c.getDefaults()
					.setMaxSystemJobs(
							Integer.parseInt(xml
									.get("mapred.capacity-scheduler.maximum-system-jobs")));
		}
		if (xml.containsKey("mapred.capacity-scheduler.default-init-accept-jobs-factor")) {
			c.getDefaults()
					.setJobCount(
							Integer.parseInt(xml
									.get("mapred.capacity-scheduler.default-init-accept-jobs-factor")));
		}
		if (xml.containsKey("mapred.capacity-scheduler.default-maximum-active-tasks-per-queue")) {
			c.getDefaults()
					.setMaxActiveTasksPerQueue(
							Integer.parseInt(xml
									.get("mapred.capacity-scheduler.default-maximum-active-tasks-per-queue")));
		}
		if (xml.containsKey("mapred.capacity-scheduler.default-maximum-active-tasks-per-user")) {
			c.getDefaults()
					.setMaxActiveTasksPerUser(
							Integer.parseInt(xml
									.get("mapred.capacity-scheduler.default-maximum-active-tasks-per-user")));
		}

		for (Queue queue : queues) {
			String queueName = queue.getName();
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".capacity")) {
				queue.setCapacity(Integer.parseInt(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName + ".capacity")));
			}
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".maximum-capacity")) {
				queue.setMaxCapacity(Integer.parseInt(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName + ".maximum-capacity")));
			}
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".supports-priority")) {
				queue.setPrioritySupport(Boolean.parseBoolean(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName + ".supports-priority")));
			}
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".minimum-user-limit-percent")) {
				queue.setMinUserLimit(Integer.parseInt(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName + ".minimum-user-limit-percent")));
			}
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".user-limit-factor")) {
				queue.setUserLimit(Integer.parseInt(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName + ".user-limit-factor")));
			}
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".maximum-initialized-active-tasks")) {
				queue.setMaxActiveTasks(Integer.parseInt(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName
								+ ".maximum-initialized-active-tasks")));
			}
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".maximum-initialized-active-tasks-per-user")) {
				queue.setMaxActiveTasksPerUser(Integer.parseInt(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName
								+ ".maximum-initialized-active-tasks-per-user")));
			}
			if (xml.containsKey(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
					+ queueName + ".init-accept-jobs-factor")) {
				queue.setJobCount(Integer.parseInt(xml
						.get(CapacitySchedulerConfig.MAPRED_CAPACITY_SCHEDULER_QUEUE
								+ queueName + ".init-accept-jobs-factor")));
			}
		}

		if (mapred.containsKey("mapred.acls.enabled")) {
			c.getCommons().setAccessControlList(
					Boolean.parseBoolean(xml.get("mapred.acls.enabled")));
		}

		c.setQueues(queues);
		return c;

	}

	/**
	 * Gets the remote paramater.
	 *
	 * @param cluster the cluster
	 * @param fileName the file name
	 * @return the remote paramater
	 * @throws Exception the exception
	 */
	private Map<String, String> getRemoteParamater(Cluster cluster,
			String fileName) throws Exception {

		File tempFile = File
				.createTempFile("Cluster" + cluster.getId(), ".xml");
		ClusterConf conf = cluster.getClusterConf();
		
		HadoopConf hConf = (HadoopConf) conf.getClusterComponents().get(Constant.Component.Name.HADOOP);
		
		String filePath = hConf.getComponentHome() + "conf/" + fileName;

		String password = conf.getPassword();
		boolean isAuthTypePassword = conf.isAuthTypePassword();
		if(!isAuthTypePassword) {
			password = conf.getPrivateKey();
		}
		
		String fileContent = SSHUtils.getFileContents(filePath, hConf.getNamenode().getPublicIp(), 
							conf.getUsername(), password, isAuthTypePassword);

		FileUtils.writeLines(tempFile.getAbsolutePath(), fileContent);
		Map<String, String> res = null;
		try {
			res = XMLManipulator.getProperties(tempFile.getAbsolutePath());
		} catch (Exception e) {
			LOG.error("", e);
		}

		if (res == null || res.isEmpty()) {
			throw new Exception("Xml configuration is invalid, Please correct it manully.");
		}
		tempFile.delete();
		return res;
	}

	/**
	 * Restart mapred service.
	 *
	 * @param authConf the auth conf
	 * @param host the host
	 * @param componentHome the component home
	 */
	private void restartMapredService(ClusterConf conf, String host,
			String componentHome) {
		SSHExec connection = null;
		// restarting the task tracker and job tracker process.
		try {
			connection = SSHUtils.connectToNode(host, conf.getUsername(), conf.getPassword(), conf.getPrivateKey()); 
			if (connection != null) {
				// start mapred command
				String startmapred = componentHome + "bin/start-mapred.sh";
				// stop mapred command
				String stopmapred = componentHome + "bin/stop-mapred.sh";

				CustomTask task = new ExecCommand(stopmapred);
				// executing stop.
				Result rs = connection.exec(task);
				// if success
				if (rs.rc == 0) {
					// starting jobtracker and tasktrackers.
					task = new ExecCommand(startmapred);
					rs = connection.exec(task);
					if (rs.rc == 0) {
						result.put(STATUS, "Configuration saved.");
					} else {
						result.put(STATUS, "Saving configuration failed.");
					}
				}
			}
		} catch (Exception e) {
			LOG.error("", e);
		}
	}
}
