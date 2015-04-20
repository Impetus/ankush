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
package com.impetus.ankush2.db;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.impetus.ankush.AppStore;
import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush2.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event;
import com.impetus.ankush.common.domain.Event.Severity;
import com.impetus.ankush.common.domain.Event.Type;
import com.impetus.ankush.common.domain.EventHistory;
import com.impetus.ankush.common.domain.HAService;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.mail.MailManager;
import com.impetus.ankush.common.mail.MailMsg;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush2.framework.config.AlertsConf;
import com.impetus.ankush2.framework.config.ThresholdConf;
import com.impetus.ankush2.ha.HAServiceExecutor;
import com.impetus.ankush2.logger.AnkushLogger;

/**
 * It generates the alerts and events for the node monitoring.
 * 
 * @author hokam
 * 
 */
public class DBEventManager {

	/** The Constant SLASH_N. */
	private static final String SLASH_N = "\n";

	/** The Constant COLON. */
	private static final String COLON = " : ";

	// Event manager.
	/** The event manager. */
	private GenericManager<Event, Long> eventManager = AppStoreWrapper
			.getManager(Constant.Manager.EVENT, Event.class);

	// Event manager.
	/** The event manager. */
	private GenericManager<EventHistory, Long> eventHistoryManager = AppStoreWrapper
			.getManager(Constant.Manager.EVENTHISTORY, EventHistory.class);

	// Event manager.
	/** The role manager. */
	private GenericManager<Role, Long> roleManager = AppStoreWrapper
			.getManager(Constant.Manager.ROLE, Role.class);

	// user manager
	/** The user manager. */
	private UserManager userManager = AppStoreWrapper.getService("userManager",
			UserManager.class);

	// Ankush logger.
	/** The log. */
	private static AnkushLogger LOG = new AnkushLogger(DBEventManager.class);

	// formating the usage value to 0 decimal number
	/** The f. */
	private static DecimalFormat f = new DecimalFormat("##");

	/**
	 * Save event.
	 * 
	 * @param event
	 *            the event
	 * @return the event
	 */
	private Event saveEvent(Event event) {
		// save event
		event = eventManager.save(event);
		// save event history.
		EventHistory eventHistory = new EventHistory();
		eventHistory.setEvent(event);
		eventHistory.setEventId(event.getId());
		eventHistory.setClusterId(event.getClusterId());
		eventHistoryManager.save(eventHistory);
		return event;
	}

	public Map<String, Object> getPropertyMap(Long clusterId, String node,
			Type type, String category, String name, Severity severity) {
		Map<String, Object> propMap = new HashMap<String, Object>();
		if (clusterId != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
					clusterId);
		}

		if (node != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.HOST, node);
		}

		if (type != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.TYPE, type);
		}

		if (category != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.CATEGORY,
					category);
		}

		if (name != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.NAME, name);
		}

		if (severity != null) {
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.SEVERITY,
					severity);
		}

		return propMap;
	}

	private List<Event> getEvents(Map<String, Object> propMap) {
		try {
			return eventManager.getAllByPropertyValue(propMap,
					com.impetus.ankush2.constant.Constant.Keys.CATEGORY,
					com.impetus.ankush2.constant.Constant.Keys.NAME,
					com.impetus.ankush2.constant.Constant.Keys.SEVERITY);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return new ArrayList<Event>();
	}

	public Event getEvent(Long clusterId, String node, Type type,
			String category, String name, Severity severity) {
		try {
			return eventManager.getByPropertyValueGuarded(getPropertyMap(
					clusterId, node, type, category, name, severity));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;

	}

	/**
	 * Method to get the cluster event history.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the all events
	 */
	public List<Event> getEventsSummary(Long clusterId, int start,
			int maxResults) {
		// getting the events history of the cluster.
		List<Event> events = eventManager.getAllByPropertyValue(
				com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
				clusterId, start, maxResults, "-date");
		// returning events.
		return events;
	}

	public List<Event> getEvents(Long clusterId, String node, Type type,
			String category, String name, Severity severity) {
		return getEvents(getPropertyMap(clusterId, node, type, category, name,
				severity));
	}

	public List<Event> getAlerts(Long clusterId, String node, Type type,
			String category, String name) {
		try {
			// creating disjunc map.
			List<Map<String, Object>> disMap = new ArrayList<Map<String, Object>>();
			disMap.add(getPropertyMap(clusterId, node, type, category, name,
					Severity.CRITICAL));
			disMap.add(getPropertyMap(clusterId, node, type, category, name,
					Severity.WARNING));
			return eventManager.getAllByDisjunctionveNormalQuery(disMap,
					com.impetus.ankush2.constant.Constant.Keys.CATEGORY,
					com.impetus.ankush2.constant.Constant.Keys.NAME,
					com.impetus.ankush2.constant.Constant.Keys.SEVERITY);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return new ArrayList<Event>();

	}

	public Map<Event.Severity, Integer> getEventsCountBySeverity(Long clusterId) {

		Map<Event.Severity, Integer> severityCount = new HashMap<Event.Severity, Integer>();
		Map<String, Object> propMap = new HashMap<String, Object>();

		try {
			// Get CRITICAL count
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID,
					clusterId);
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.SEVERITY,
					Event.Severity.CRITICAL);
			severityCount.put(Event.Severity.CRITICAL,
					eventManager.getAllByPropertyValueCount(propMap));

			// Get WARNING count
			propMap.put(com.impetus.ankush2.constant.Constant.Keys.SEVERITY,
					Event.Severity.WARNING);
			severityCount.put(Event.Severity.WARNING,
					eventManager.getAllByPropertyValueCount(propMap));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return severityCount;
	}

	/**
	 * Method to generate event and alerts for monitoring.
	 * 
	 * @param nodeMonitoring
	 *            the node monitoring
	 */
	public void checkAlertsForUsage(final String host, final Long clusterId,
			final NodeMonitoring nodeMonitoring) {
		try {
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {

					Cluster cluster = new DBClusterManager()
							.getCluster(clusterId);

					// Check for stable cluster.
					if (!isDeployedOrAdded(cluster)) {
						return;
					}

					// Process CPU usage
					if (nodeMonitoring != null
							&& nodeMonitoring.getMonitoringInfo() != null
							&& nodeMonitoring.getMonitoringInfo()
									.getUptimeInfos() != null) {

						// getting current cpu usage value.
						Double cpuUsage = nodeMonitoring.getMonitoringInfo()
								.getUptimeInfos().get(0).getCpuUsage();

						// process the usage alerts.
						processUsageAlert(host, cluster, cpuUsage,
								Constant.Alerts.Metric.CPU);
					}

					// Process Memory usage
					if (nodeMonitoring.getMonitoringInfo() != null
							&& nodeMonitoring.getMonitoringInfo()
									.getMemoryInfos() != null) {

						// getting current memory usage value.
						Double memoryUsage = nodeMonitoring.getMonitoringInfo()
								.getMemoryInfos().get(0).getUsedPercentage();

						// process the memory usage alerts.
						processUsageAlert(host, cluster, memoryUsage,
								Constant.Alerts.Metric.MEMORY);
					}

				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Process HAServices
	 * 
	 * Start HAServices if it is down. Stop schedule of HAService if it is up.
	 * 
	 * @param clusterId
	 * @param host
	 * @param component
	 * @param serviceStatus
	 */
	private void processHAServices(Long clusterId, String host,
			String component, Map<String, Boolean> serviceStatus) {

		for (String service : serviceStatus.keySet()) {
			if (serviceStatus.get(service)) {
				HAServiceExecutor.removeHAService(clusterId, host, component,
						service);
			} else {
				HAService haService = new DBHAServiceManager().getHAService(
						clusterId, host, component, service);
				if (haService != null) {
					HAServiceExecutor.addHAService(haService, host);
				}
			}
		}

	}

	public void checkAlertsForService(final String host, final Long clusterId,
			final HashMap<String, Map<String, Boolean>> agentServiceStatus) {
		try {
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {

					Cluster cluster = new DBClusterManager()
							.getCluster(clusterId);

					if (!isDeployedOrAdded(cluster)) {
						return;
					}

					// iterating over the map of services.
					for (String technology : agentServiceStatus.keySet()) {

						// Process HAServices
						processHAServices(clusterId, host, technology,
								agentServiceStatus.get(technology));

						// Process alerts
						processServiceAlert(host, cluster, technology,
								agentServiceStatus.get(technology));

					}
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	/**
	 * Method to check the either the cluster/node is deployed/added or not.
	 * 
	 * @param node
	 *            the node
	 * @param cluster
	 *            the cluster
	 * @return true, if is deployed or added
	 */
	private boolean isDeployedOrAdded(Cluster cluster) {
		if (cluster.getState().equals(Constant.Cluster.State.ERROR)
				|| cluster.getState().equals(Constant.Cluster.State.DEPLOYING)
				|| cluster.getState().equals(Constant.Cluster.State.REMOVING)) {
			return false;
		}
		return true;
	}

	/**
	 * To generate events and alerts for service.
	 * 
	 * @param node
	 *            the node
	 * @param cluster
	 *            the cluster
	 * @param status
	 *            the status
	 * @param service
	 *            the service
	 */
	private void processServiceAlert(String host, Cluster cluster,
			String category, Map<String, Boolean> serviceStatus) {

		for (String service : serviceStatus.keySet()) {

			// calculating current severity.

			Severity severity = serviceStatus.get(service) ? Event.Severity.NORMAL
					: Event.Severity.CRITICAL;

			// current value of the service.
			String value = serviceStatus.get(service) ? com.impetus.ankush2.constant.Constant.Keys.UP
					: com.impetus.ankush2.constant.Constant.Keys.DOWN;

			processAlert(cluster, host, Event.Type.SERVICE, category, service,
					severity, value, null);
		}
	}

	private void processAlert(Cluster dbCluster, String host, Event.Type type,
			String category, String name, Event.Severity severity,
			String value, String thresholdValue) {

		// last event object.
		Event lastEvent = getEvent(null, host, null, category, name, null);

		// process first non normal event and existing event with different
		// severity
		if ((lastEvent == null && !severity.equals(Event.Severity.NORMAL))
				|| (lastEvent != null && !lastEvent.getSeverity().equals(
						severity))) {
			// create event object.
			Event event = populateEvent(dbCluster.getId(), host, type,
					category, name, severity, value, thresholdValue, lastEvent);

			event = saveEvent(event);
			// drop a mail.
			sendMail(dbCluster, event);
			return;
		}
	}

	/**
	 * Method to save the event
	 * 
	 * @param node
	 * @param cluster
	 * @param currentValue
	 * @param type
	 * @param subType
	 * @param eventName
	 * @param currentSeverity
	 * @param groupingType
	 * @param lastEvent
	 * @return
	 */
	private Event populateEvent(Long clusterId, String host, Event.Type type,
			String category, String name, Event.Severity severity,
			String value, String thresholdValue, Event lastEvent) {

		Event event = new Event();
		if (lastEvent != null) {
			event.setId(lastEvent.getId());
		}
		event.setClusterId(clusterId);
		event.setHost(host);
		event.setType(type);
		event.setCategory(category);
		event.setName(name);
		event.setSeverity(severity);
		event.setValue(value);
		event.setThresholdValue(thresholdValue);
		event.setDate(new Date());
		return event;
	}

	/**
	 * Process usage alert.
	 * 
	 * @param node
	 *            the node
	 * @param cluster
	 *            the cluster
	 * @param value
	 *            the usage value
	 * @param name
	 *            the metric name
	 */
	private void processUsageAlert(String host, Cluster cluster, Double value,
			String name) {
		// Getting threshold values for the metric.
		ThresholdConf threshold = getThresholdConf(cluster, name);
		// Getting current severity.
		Event.Severity severity = getCurrentSeverity(value, threshold);

		String thresholdValue = getThresholdValue(threshold, severity);

		// current usage value.
		String strValue = f.format(value).toString();
		processAlert(cluster, host, Event.Type.USAGE,
				com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
				name, severity, strValue, thresholdValue);
	}

	public List getGroupbyCount(Long clusterId, String host, Event.Type type,
			String component, String name, Event.Severity severity) {

		try {
			StringBuilder sb = new StringBuilder();

			// Count select query
			sb.append("select e.type, e.category, e.name, e.severity, count(e) from Event e ");
			Map<String, Object> propMap = getPropertyMap(clusterId, host, type,
					component, name, severity);

			// Create where clause
			if (!propMap.isEmpty()) {
				StringBuilder where = new StringBuilder();
				for (String key : propMap.keySet()) {
					if (where.length() > 0) {
						where.append("and ");
					}
					where.append(key).append("=");
					if (key.equals(com.impetus.ankush2.constant.Constant.Keys.CLUSTERID)) {
						where.append(propMap.get(key)).append(" ");
					} else if (key
							.equals(com.impetus.ankush2.constant.Constant.Keys.TYPE)) {
						where.append(((Event.Type) propMap.get(key)).ordinal())
								.append(" ");
					} else if (key
							.equals(com.impetus.ankush2.constant.Constant.Keys.SEVERITY)) {
						where.append(
								((Event.Severity) propMap.get(key)).ordinal())
								.append(" ");
					} else {
						where.append("\"").append(propMap.get(key))
								.append("\" ");
					}
				}
				sb.append("where ").append(where);
			}

			// Add group by
			sb.append("group by e.type, e.category, e.name, e.severity");

			return eventManager.getCustomQuery(sb.toString());

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	private String getThresholdValue(ThresholdConf threshold, Severity severity) {
		if (threshold == null || severity.equals(Event.Severity.NORMAL)) {
			return null;
		}
		if (severity.equals(Event.Severity.CRITICAL)) {
			return threshold.getAlertLevel().toString();
		}
		return threshold.getWarningLevel().toString();
	}

	/**
	 * To send e-mail.
	 * 
	 * @param event
	 *            the event
	 * @param to
	 *            the to
	 */
	private void sendMail(Cluster dbCluster, Event event) {
		LOG.debug("Sending a mail");
		// creating mail message object.
		MailMsg message = new MailMsg();
		message.setTo(getEmailList(dbCluster));
		message.setSubject(event.getSubject());
		message.setMessage(getDescription(event, dbCluster.getName()));
		message.setContentType("text/plain");

		// Getting mail manager.
		MailManager mm = AppStoreWrapper.getMailManager();
		if (mm != null) {
			// Sending mail.
			mm.sendSystemMail(message);
		}

	}

	/**
	 * Method to get description of message.
	 * 
	 * @param event
	 *            the event
	 * @param clustername
	 *            the clustername
	 * @return the description
	 */
	private String getDescription(Event event, String clustername) {
		// creating the description message for the event.
		StringBuilder string = new StringBuilder();
		string.append("Cluster Name").append(COLON).append(clustername)
				.append(SLASH_N);
		string.append(event.getName()).append(COLON).append(event.getValue())
				.append(SLASH_N);
		string.append("Host").append(COLON).append(event.getHost())
				.append(SLASH_N);
		string.append("Created At").append(COLON)
				.append(event.getDate().toString()).append(" ").append(SLASH_N);
		return string.toString();
	}

	/**
	 * To get the current severity value.
	 * 
	 * @param currentValue
	 *            the current value
	 * @param threshold
	 *            the threshold
	 * @return the current severity
	 */
	private Event.Severity getCurrentSeverity(Double value,
			ThresholdConf threshold) {

		if (threshold != null && threshold.getAlertLevel() != null
				&& value >= threshold.getAlertLevel()) {
			return Event.Severity.CRITICAL;
		}

		if (threshold != null && threshold.getWarningLevel() != null
				&& value >= threshold.getWarningLevel()) {
			return Event.Severity.WARNING;
		}

		return Event.Severity.NORMAL;

	}

	/**
	 * Methdo to get the threshold values for the cluster of giver metric.
	 * 
	 * @param cluster
	 *            the cluster
	 * @param metricName
	 *            the metric name
	 * @return the threshold conf
	 */
	private ThresholdConf getThresholdConf(Cluster cluster, String metricName) {
		// Getting alerts conf.
		AlertsConf alertsConf = cluster.getAlertsConf();

		if (alertsConf == null) {
			return null;
		}
		// iterating over the all threshold values.
		for (ThresholdConf thresholdConf : alertsConf.getThresholds()) {
			// If metric name is same then return the threshold conf.
			if (thresholdConf.getMetricName().equalsIgnoreCase(metricName)) {
				return thresholdConf;
			}
		}
		return null;
	}

	/**
	 * Method to get email list of the cluster.
	 * 
	 * @param cluster
	 *            the cluster
	 * @return the email list
	 */
	private String getEmailList(Cluster cluster) {
		// Getting alerts conf.
		AlertsConf alertsConf = cluster.getAlertsConf();

		String emailList = "";
		if (alertsConf == null) {
			return null;
		}
		// getting administrator mailing list
		if (alertsConf.isInformAllAdmins()) {
			Role role = roleManager.getByPropertyValue(
					com.impetus.ankush2.constant.Constant.Keys.NAME,
					Constant.User.Role.ROLE_SUPER_USER);

			List<User> users = userManager.getUsersByRole(role);
			for (User user : users) {
				if (user.isEnabled()) {
					emailList += user.getEmail() + ";";
				}
			}
		}
		// getting configured mailing list
		if (alertsConf.getMailingList() != null) {
			emailList += alertsConf.getMailingList();
		}

		return emailList;
	}

	/**
	 * To process agent down alerts.
	 */
	public void processAgentDownAlerts() {

		List<Cluster> clusters = new DBClusterManager().getClusters();
		MonitoringManager monitoringManager = new MonitoringManager();
		for (Cluster cluster : clusters) {
			// Check for stable clusters only.
			if (!isDeployedOrAdded(cluster)) {
				continue;
			}

			for (Node node : cluster.getNodes()) {
				// Check agent down for deployed nodes only.
				if (!node.getState().equalsIgnoreCase(
						Constant.Node.State.DEPLOYED.toString())) {
					continue;
				}

				NodeMonitoring nodeMonitoring = monitoringManager
						.getMonitoringData(node.getId());
				if (nodeMonitoring == null) {
					// Create new object with default values
					nodeMonitoring = new NodeMonitoring();
					nodeMonitoring.setNodeId(node.getId());
					nodeMonitoring.setUpdateTime(new Date());
					nodeMonitoring
							.setTechnologyServiceStatus(new HashMap<String, Map<String, Boolean>>());
					monitoringManager.save(nodeMonitoring);
				} else {

					long dateDiff = new Date().getTime()
							- nodeMonitoring.getUpdateTime().getTime();
					if (!DBServiceManager.getManager().isAgentDown(
							node.getPublicIp())
							&& (dateDiff > (Integer) AppStore
									.getObject(com.impetus.ankush2.constant.Constant.Keys.AGENT_DOWN_INTERVAL))) {

						// Set Agent is down and update database.
						Map<String, Boolean> status = new HashMap<String, Boolean>();
						HashMap<String, Map<String, Boolean>> serviceMap = new HashMap<String, Map<String, Boolean>>();

						status.put(
								com.impetus.ankush2.constant.Constant.Role.AGENT,
								false);
						serviceMap
								.put(com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
										status);
						nodeMonitoring.setTechnologyServiceStatus(serviceMap);
						nodeMonitoring.setUpdateTime(new Date());
						monitoringManager.save(nodeMonitoring);

						// Save into service table
						DBServiceManager
								.getManager()
								.setStatus(
										cluster.getId(),
										node.getPublicIp(),
										com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
										com.impetus.ankush2.constant.Constant.Role.AGENT,
										null, null, false);

						// Process alert
						processAlert(
								cluster,
								node.getPublicIp(),
								Event.Type.SERVICE,
								com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
								com.impetus.ankush2.constant.Constant.Role.AGENT,
								Severity.CRITICAL,
								com.impetus.ankush2.constant.Constant.Keys.DOWN,
								null);

					}
				}
			}
		}
	}

	/**
	 * Update event sub types.
	 * 
	 * @param cluster
	 *            the cluster
	 */
	public void updateEventSubTypes(Cluster cluster) {
		// iterate over the nodes.
		MonitoringManager monitoringManager = new MonitoringManager();
		for (Node node : cluster.getNodes()) {
			// node monitoring object.
			NodeMonitoring nodeMonitoring = monitoringManager
					.getMonitoringData(node.getId());
			// check for node monitoring.
			checkAlertsForUsage(node.getPublicIp(), cluster.getId(),
					nodeMonitoring);
		}
	}

	/**
	 * Method to check the agent down alerts.
	 * 
	 * @param cluster
	 * @return
	 */
	public boolean isAnyAgentDown(Cluster cluster) {
		Event event = getEvent(cluster.getId(), null, Type.SERVICE,
				com.impetus.ankush2.constant.Constant.Component.Name.AGENT,
				com.impetus.ankush2.constant.Constant.Role.AGENT,
				Severity.CRITICAL);

		return event != null;

	}
}
