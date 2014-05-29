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
package com.impetus.ankush.common.alerts;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TemporalType;

import com.impetus.ankush.AppStoreWrapper;
import com.impetus.ankush.common.constant.Constant;
import com.impetus.ankush.common.domain.Cluster;
import com.impetus.ankush.common.domain.Event;
import com.impetus.ankush.common.domain.EventHistory;
import com.impetus.ankush.common.domain.Node;
import com.impetus.ankush.common.domain.NodeMonitoring;
import com.impetus.ankush.common.domain.Role;
import com.impetus.ankush.common.domain.User;
import com.impetus.ankush.common.mail.MailManager;
import com.impetus.ankush.common.mail.MailMsg;
import com.impetus.ankush.common.service.GenericManager;
import com.impetus.ankush.common.service.MonitoringManager;
import com.impetus.ankush.common.service.UserManager;
import com.impetus.ankush.common.utils.AnkushLogger;
import com.impetus.ankush.common.utils.HibernateUtils;

/**
 * It generates the alerts and events for the node monitoring.
 * 
 * @author hokam
 * 
 */
public class EventManager {

	/** The Constant SLASH_N. */
	private static final String SLASH_N = "\n";

	/** The Constant COLON. */
	private static final String COLON = " : ";

	// cluster manager.
	/** The cluster manager. */
	private GenericManager<Cluster, Long> clusterManager = AppStoreWrapper
			.getManager(Constant.Manager.CLUSTER, Cluster.class);

	// Node manager.
	/** The node manager. */
	private GenericManager<Node, Long> nodeManager = AppStoreWrapper
			.getManager(Constant.Manager.NODE, Node.class);

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
	private AnkushLogger LOG = new AnkushLogger(EventManager.class);

	// formating the usage value to 0 decimal number
	/** The f. */
	private DecimalFormat f = new DecimalFormat("##");

	/**
	 * Save event.
	 * 
	 * @param event
	 *            the event
	 * @return the event
	 */
	public Event saveEvent(Event event) {
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

	/**
	 * Method to get the given severity events.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the cluster events
	 */
	public int getEventsCountBySeverity(Cluster dbCluster, String severity) {
		// Get the cluster events.
		List<Event> events = getEvents(dbCluster);

		int severityCount = 0;
		// iterating over the events to find the given severity count.
		for (Event event : events) {
			// if given severity is same
			if (event.getSeverity().equals(severity)) {
				severityCount++;
			}
		}
		// returning event count.
		return severityCount;
	}

	/**
	 * Method to get the cluster events.
	 * 
	 * @param dbCluster
	 * @return
	 */
	public List<Event> getEvents(Cluster dbCluster) {
		// property map for given critical
		// Getting events by cluster id.
		List<Event> events = new ArrayList<Event>();
		// iterating over the nodes.
		for (Node node : dbCluster.getNodes()) {
			events.addAll(getHostEvents(dbCluster.getId(), node.getPublicIp()));
		}
		return events;
	}

	/**
	 * Method to get the cluster events.
	 * 
	 * @param dbCluster
	 * @return
	 */
	public List<Event> getEventsByType(Cluster dbCluster, String type) {
		// property map for given critical
		// Getting events by cluster id.
		List<Event> events = new ArrayList<Event>();
		// iterating over the nodes.
		for (Node node : dbCluster.getNodes()) {
			events.addAll(getHostEventsByType(dbCluster.getId(),
					node.getPublicIp(), type));
		}

		return events;
	}

	/**
	 * Method to get Host critical and warning events.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param host
	 *            the host
	 * @return the node events
	 */
	public List<Event> getHostEvents(Object clusterId, String host) {
		// Getting critical events.
		List<Event> events = getEventsBySeverity(clusterId,
				Constant.Alerts.Severity.CRITICAL, host);
		// Getting warning events.
		events.addAll(getEventsBySeverity(clusterId,
				Constant.Alerts.Severity.WARNING, host));

		for (Event event : events) {
			if (event.getSubType().equals(Constant.Role.AGENT)) {
				return Collections.singletonList(event);
			}
		}
		return events;
	}

	/**
	 * Method to get Host critical and warning events.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @param host
	 *            the host
	 * @return the node events
	 */
	public List<Event> getHostEventsByType(Object clusterId, String host,
			String type) {
		// Getting critical events.
		List<Event> events = getEventsBySeverityAndType(clusterId,
				Constant.Alerts.Severity.CRITICAL, host, type);
		// Getting warning events.
		events.addAll(getEventsBySeverityAndType(clusterId,
				Constant.Alerts.Severity.WARNING, host, type));

		for (Event event : events) {
			if (event.getSubType().equals(Constant.Role.AGENT)) {
				return Collections.singletonList(event);
			}
		}
		return events;
	}

	/**
	 * Method to get the given severity events.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the cluster events
	 */
	public List<Event> getEventsBySeverity(Object clusterId, String severity,
			String host) {
		// property map for given critical
		Map<String, Object> propMap = new HashMap<String, Object>();
		propMap.put(Constant.Keys.SEVERITY, severity);
		propMap.put(Constant.Keys.CLUSTERID, clusterId);
		propMap.put(Constant.Keys.HOST, host);

		// retrieving nodes using disjunction query.
		List<Event> events = eventManager.getAllByPropertyValue(propMap);

		// returning events.
		return events;
	}

	/**
	 * Method to get the given severity events.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the cluster events
	 */
	public List<Event> getEventsBySeverityAndType(Object clusterId,
			String severity, String host, String type) {
		// property map for given critical
		Map<String, Object> propMap = new HashMap<String, Object>();
		propMap.put(Constant.Keys.SEVERITY, severity);
		propMap.put(Constant.Keys.CLUSTERID, clusterId);
		propMap.put(Constant.Keys.HOST, host);
		propMap.put(Constant.Keys.TYPE, type);

		// retrieving nodes using disjunction query.
		List<Event> events = eventManager.getAllByPropertyValue(propMap);

		// returning events.
		return events;
	}

	/**
	 * Method to get the cluster event history.
	 * 
	 * @param clusterId
	 *            the cluster id
	 * @return the all events
	 */
	public List<Event> getEvents(Long clusterId) {
		// getting the events history of the cluster.
		List<EventHistory> eventsHistory = eventHistoryManager
				.getAllByPropertyValue(Constant.Keys.CLUSTERID, clusterId,
						Constant.Keys.ID);
		// list of events.
		List<Event> events = new ArrayList<Event>();
		// adding the history events in list.
		for (EventHistory eventHistory : eventsHistory) {
			events.add(eventHistory.getEvent());
		}
		// returning events.
		return events;
	}

	/**
	 * method to get event.
	 * 
	 * @param host
	 *            the host
	 * @param eventName
	 *            the event name
	 * @return the event
	 */
	public Event getEvent(String host, String eventName) {
		// property map for query.
		Map<String, Object> propMap = new HashMap<String, Object>();
		propMap.put(Constant.Keys.HOST, host);
		propMap.put(Constant.Keys.NAME, eventName);

		// Getting last event.
		List<Event> events = eventManager.getAllByPropertyValue(propMap);

		// Returning first element
		if (events != null && !events.isEmpty()) {
			return events.get(0);
		}
		return null;
	}

	/**
	 * Method to generate event and alerts for monitoring.
	 * 
	 * @param nodeMonitoring
	 *            the node monitoring
	 */
	public void checkAlertsForMonitoring(final NodeMonitoring nodeMonitoring) {
		try {
			AppStoreWrapper.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					Long nodeId = nodeMonitoring.getNodeId();
					Node node = nodeManager.get(nodeId);

					Long clusterId = node.getClusterId();
					Cluster cluster = clusterManager.get(clusterId);

					if (!isDeployedOrAdded(node, cluster)) {
						return;
					}

					checkAlertsForCpu(node, cluster, nodeMonitoring);

					checkAlertsForMemory(node, cluster, nodeMonitoring);

					checkAlertsForService(node, cluster, nodeMonitoring);
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
	private boolean isDeployedOrAdded(Node node, Cluster cluster) {
		if (!node.getState().equalsIgnoreCase(Constant.Node.State.DEPLOYED)
				|| cluster.getState().equals(Constant.Cluster.State.ERROR)
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
	 * @param nodeMonitoring
	 *            the node monitoring
	 */
	private void checkAlertsForService(Node node, Cluster cluster,
			NodeMonitoring nodeMonitoring) {
		if (nodeMonitoring.getServiceStatus() != null) {

			// Get the service status map.
			Map<String, Boolean> serviceStatus = nodeMonitoring
					.getServiceStatus();

			// iterating over the map of services.
			for (String service : serviceStatus.keySet()) {
				if (service.isEmpty()) {
					continue;
				}
				Boolean status = serviceStatus.get(service);
				processServiceAlert(node, cluster, status, service);
			}
		}
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
	private void processServiceAlert(Node node, Cluster cluster,
			Boolean status, String service) {

		// Alert type.
		String type = Constant.Alerts.Type.SERVICE;
		String eventName = service + " " + type;

		// calculating current severity.
		String currentSeverity = status ? Constant.Alerts.Severity.NORMAL
				: Constant.Alerts.Severity.CRITICAL;

		// current value of the service.
		String currentValue = status ? "Up" : "Down";

		// process alerts.
		processAlert(node, cluster, currentValue, type, null, eventName,
				currentSeverity, null);
	}

	/**
	 * Process alert.
	 * 
	 * @param node
	 *            the node
	 * @param cluster
	 *            the cluster
	 * @param currentValue
	 *            the current value
	 * @param type
	 *            the type
	 * @param subType
	 *            the sub type
	 * @param eventName
	 *            the event name
	 * @param currentSeverity
	 *            the current severity
	 */
	private void processAlert(Node node, Cluster cluster, String currentValue,
			String type, String subType, String eventName,
			String currentSeverity, String groupingType) {

		// last event object.
		Event lastEvent = getEvent(node.getPublicIp(), eventName);

		// if the last and current severity is not equals then generate the
		// event and alert.
		if ((lastEvent == null && !currentSeverity
				.equalsIgnoreCase(Constant.Alerts.Severity.NORMAL))
				|| (lastEvent != null && !currentSeverity
						.equalsIgnoreCase(lastEvent.getSeverity()))) {
			// create event object.
			Event event = populateEvent(node, cluster, currentValue, type,
					subType, eventName, currentSeverity, groupingType,
					lastEvent);
			event = this.saveEvent(event);
			// drop a mail.
			sendMail(event, getEmailList(cluster));
			return;
		}

		// if the Alerts threshold value changed then event sub type
		// should also be changed in database.
		if (lastEvent != null) {
			// if old and new sub type is different then
			if (subType != null && !lastEvent.getSubType().equals(subType)) {
				// get the event
				Event event = populateEvent(node, cluster, currentValue, type,
						subType, eventName, currentSeverity, groupingType,
						lastEvent);
				// save the event.
				eventManager.save(event);
			}
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
	private Event populateEvent(Node node, Cluster cluster,
			String currentValue, String type, String subType, String eventName,
			String currentSeverity, String groupingType, Event lastEvent) {

		Event event = new Event();
		if (lastEvent != null) {
			event.setId(lastEvent.getId());
		}
		event.setClusterId(cluster.getId());
		event.setName(eventName);
		event.setSubType(subType);
		event.setGroupingType(groupingType);
		event.setType(type);
		event.setSeverity(currentSeverity);
		event.setHost(node.getPublicIp());
		event.setCurrentValue(currentValue);
		event.setDate(new Date());
		event.setDescription(getDescription(event, cluster.getName()));
		return event;
	}

	/**
	 * To generate events and alerts for cpu.
	 * 
	 * @param node
	 *            the node
	 * @param cluster
	 *            the cluster
	 * @param nodeMonitoring
	 *            the node monitoring
	 */
	private void checkAlertsForCpu(Node node, Cluster cluster,
			NodeMonitoring nodeMonitoring) {
		if (nodeMonitoring.getMonitoringInfo() != null
				&& nodeMonitoring.getMonitoringInfo().getUptimeInfos() != null) {

			// getting current cpu usage value.
			Double cpuUsage = nodeMonitoring.getMonitoringInfo()
					.getUptimeInfos().get(0).getCpuUsage();

			// process the usage alerts.
			processUsageAlert(node, cluster, cpuUsage,
					Constant.Alerts.Metric.CPU);
		}

	}

	/**
	 * To generate events and alerts for Memory.
	 * 
	 * @param node
	 *            the node
	 * @param cluster
	 *            the cluster
	 * @param nodeMonitoring
	 *            the node monitoring
	 */
	private void checkAlertsForMemory(Node node, Cluster cluster,
			NodeMonitoring nodeMonitoring) {
		if (nodeMonitoring.getMonitoringInfo() != null
				&& nodeMonitoring.getMonitoringInfo().getMemoryInfos() != null) {

			// getting current memory usage value.
			Double memoryUsage = nodeMonitoring.getMonitoringInfo()
					.getMemoryInfos().get(0).getUsedPercentage();

			// process the memory usage alerts.
			processUsageAlert(node, cluster, memoryUsage,
					Constant.Alerts.Metric.MEMORY);
		}

	}

	/**
	 * Process usage alert.
	 * 
	 * @param node
	 *            the node
	 * @param cluster
	 *            the cluster
	 * @param usageValue
	 *            the usage value
	 * @param metricName
	 *            the metric name
	 */
	private void processUsageAlert(Node node, Cluster cluster,
			Double usageValue, String metricName) {

		// Alert type.
		String type = Constant.Alerts.Type.USAGE;
		// event name.
		String eventName = metricName + " " + type;

		// Getting threshold values for the metric.
		ThresholdConf threshold = getThresholdConf(cluster, metricName);

		// Getting current severity.
		String currentSeverity = getCurrentSeverity(usageValue, threshold);

		String subType = null;
		// setting sub type for the critical severity.
		if (currentSeverity.equals(Constant.Alerts.Severity.CRITICAL)) {
			subType = f.format(threshold.getAlertLevel()) + "% above";
		} else if (currentSeverity.equals(Constant.Alerts.Severity.WARNING)) {
			// setting sub type for the warning alerts.
			subType = f.format(threshold.getWarningLevel()) + "% - "
					+ f.format(threshold.getAlertLevel()) + "% ";
		}

		// current usage value.
		String currentValue = f.format(usageValue).toString();

		// processing alerts.
		processAlert(node, cluster, currentValue, type, subType, eventName,
				currentSeverity, eventName);
	}

	/**
	 * To send e-mail.
	 * 
	 * @param event
	 *            the event
	 * @param to
	 *            the to
	 */
	private void sendMail(Event event, String to) {
		LOG.debug("Sending a mail");
		// creating mail message object.
		MailMsg message = new MailMsg();
		message.setTo(to);
		message.setSubject(event.getSubject());
		message.setMessage(event.getDescription());
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
		string.append(event.getName()).append(COLON)
				.append(event.getCurrentValue()).append(SLASH_N);
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
	private String getCurrentSeverity(Double currentValue,
			ThresholdConf threshold) {

		Double alertLevel = null;
		Double warnLevel = null;

		// if threshold is not null set alertlevel and warnlevel.
		if (threshold != null) {
			alertLevel = threshold.getAlertLevel();
			warnLevel = threshold.getWarningLevel();
		}

		// returning current severity as normal for all null values.
		if ((threshold == null) || (alertLevel == null) || (warnLevel == null)) {
			return Constant.Alerts.Severity.NORMAL;
		}

		// if current valueis > alertlevel then return severity as critical.
		if (currentValue >= alertLevel) {
			return Constant.Alerts.Severity.CRITICAL;
		}

		// if current valueis > warnLevel then return severity as warning else
		// return normal.
		if (currentValue >= warnLevel) {
			return Constant.Alerts.Severity.WARNING;
		} else {
			return Constant.Alerts.Severity.NORMAL;
		}
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
			Role role = roleManager.getByPropertyValue("name",
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
		// list of nodes.
		List<Node> nodes = nodeManager.getAllByPropertyValue(
				Constant.Keys.STATE, Constant.Node.State.DEPLOYED);

		// iterate over the down nodes.
		MonitoringManager monitoringManager = new MonitoringManager();
		for (Node node : nodes) {
			NodeMonitoring nodeMonitoring = monitoringManager
					.getMonitoringData(node.getId());

			if (nodeMonitoring == null) {
				// setting empty object.
				nodeMonitoring = new NodeMonitoring();
				// setting node id.
				nodeMonitoring.setNodeId(node.getId());
				// setting node monitoring info as null.
				nodeMonitoring.setMonitoringInfo(null);
				// setting empty service status.
				nodeMonitoring.setServiceStatus(new HashMap<String, Boolean>());
				// setting update time
				nodeMonitoring.setUpdateTime(new Date(
						new Date().getTime() - 100011));
				// calling check alerts for down agents.
				checkAlertsForMonitoring(nodeMonitoring);
			} else if (nodeMonitoring.isAgentDown()) {
				// setting node monitoring info as null.
				nodeMonitoring.setMonitoringInfo(null);
				// setting service agent down
				nodeMonitoring.setServiceStatus(new HashMap<String, Boolean>());
				// calling check alerts for down agents.
				checkAlertsForMonitoring(nodeMonitoring);
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
			checkAlertsForMonitoring(nodeMonitoring);
		}
	}

	/**
	 * Delete events.
	 * 
	 * @param host
	 *            the host
	 * @param subTypes
	 *            the sub types
	 */
	public void deleteEvents(String host, List<String> subTypes) {
		// Property map with host and subtype event fetching
		Map<String, Object> propertyValueMap = new HashMap<String, Object>();
		propertyValueMap.put(Constant.Keys.HOST, host);

		// iterating over the sub types.
		for (String subType : subTypes) {
			// setting sub type
			propertyValueMap.put("subType", subType);
			// deleting events.
			eventManager.deleteAllByPropertyValue(propertyValueMap);
		}
	}

	/**
	 * Method to check the agent down alerts.
	 * 
	 * @param cluster
	 * @return
	 */
	public boolean isAnyAgentDown(Cluster cluster) {
		try {
			// property map to check the agent down alerts.
			Map<String, Object> propertyValueMap = new HashMap<String, Object>();
			propertyValueMap.put(Constant.Keys.CLUSTERID, cluster.getId());
			propertyValueMap.put(Constant.Keys.NAME, "Agent Service");
			propertyValueMap.put(Constant.Keys.SEVERITY,
					Constant.Alerts.Severity.CRITICAL);

			// fetching events.
			List<Event> events = eventManager
					.getAllByPropertyValue(propertyValueMap);
			if (events != null && !events.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
}
