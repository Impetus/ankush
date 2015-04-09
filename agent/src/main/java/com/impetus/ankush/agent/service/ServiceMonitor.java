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
package com.impetus.ankush.agent.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;

import com.impetus.ankush.agent.AgentConf;
import com.impetus.ankush.agent.Constant;
import com.impetus.ankush.agent.action.ActionFactory;
import com.impetus.ankush.agent.action.Taskable;
import com.impetus.ankush.agent.service.provider.ServiceProvider;
import com.impetus.ankush.agent.utils.AgentLogger;
import com.impetus.ankush.agent.utils.AgentRestClient;

/**
 * @author hokam
 * 
 */
public class ServiceMonitor extends Taskable {
	/** The log object. */
	private static final AgentLogger LOGGER = new AgentLogger(
			ServiceMonitor.class);

	/** The agent conf object. */
	private AgentConf conf;

	/** The rest client. */
	private AgentRestClient restClient;

	// private Map<String, Map<String, Boolean>> oldServiceStatus = null;

	/**
	 * Service Monitor Constructor
	 */
	public ServiceMonitor() {
		conf = new AgentConf();
		restClient = new AgentRestClient();
	}

	private synchronized Map<String, Map<String, Boolean>> processServiceStatus(
			Map<String, Map<String, Boolean>> techServiceStatus) {
		Map<String, Map<String, Boolean>> serviceStatus = new HashMap<String, Map<String, Boolean>>();

		// Send all service status if oldServiceStatus is
		// null
		// if (oldServiceStatus == null || oldServiceStatus.isEmpty()) {
		// serviceStatus = techServiceStatus;
		// } else {
		// Iterate on technology
		for (String technology : techServiceStatus.keySet()) {
			// Fetch technology status maps
			Map<String, Boolean> services = techServiceStatus.get(technology);
			// Map<String, Boolean> oldServices = oldServiceStatus
			// .get(technology);
			//
			// if (oldServices == null || oldServices.isEmpty()) {
			//
			// // Add technology if not exist
			// serviceStatus.put(technology, services);
			// continue;
			// }

			// Iterate on service
			for (String service : services.keySet()) {

				// It service is not found or changes
				// if (!oldServices.containsKey(service)
				// || oldServices.get(service) != services
				// .get(service)) {
				// Add technology map if not exist
				if (!serviceStatus.containsKey(technology)) {
					serviceStatus.put(technology,
							new HashMap<String, Boolean>());
				}
				// Add service status
				serviceStatus.get(technology).put(service,
						services.get(service));
			}
		}

		// }
		// }
		// oldServiceStatus = techServiceStatus;

		return serviceStatus;
	}

	// private synchronized void clearServiceStatus() {
	// oldServiceStatus = null;
	// }

	@Override
	public void start() {
		LOGGER.info("ServiceMonitor:start");
		try {
			// Getting service status send url.
			final String url = conf.getURL(Constant.PROP_NAME_SERVICE_URL_LAST);
			// Getting service status send period.
			long period = conf
					.getIntValue(Constant.PROP_NAME_SERVICE_STATUS_UPDATE_TIME);
			// creating a runnable object.
			Runnable serviceStatusThread = new Runnable() {

				@Override
				public void run() {
					try {

						Map<String, Map<String, Boolean>> techServiceStatus = collectServiceStatus();

						LOGGER.info("Service Data Sending " + new Date());
						String output = restClient.sendData(
								processServiceStatus(collectServiceStatus()),
								url);
						// Clear oldServiceStatus if server is down.
//						if (output == null || output.isEmpty()) {
//							clearServiceStatus();
//						}

					} catch (Exception e) {
						LOGGER.error(e.getMessage(), e);
					}
				}
			};
			// scheduling the runnable for periodical execution.
			service.scheduleAtFixedRate(serviceStatusThread, 0, period,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * Method to collect service status by technology
	 * 
	 * @return
	 */
	private Map<String, Map<String, Boolean>> collectServiceStatus() {
		// Service Conf directory
		String serviceConfDir = conf
				.getStringValue(Constant.PROP_SERVICE_CONF_DIR);
		// ServiceConf directory file object.
		File serviceConfigDir = new File(serviceConfDir);
		LOGGER.info("ServiceConfDirectory:" + serviceConfDir);

		// Getting list of files whose name ends with XML extension in agent
		// services folder.
		File[] files = serviceConfigDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				// file name ends with XML extension.
				return name.endsWith(Constant.File_Extension.XML);
			}
		});

		Map<String, Map<String, Boolean>> serviceStatus = new HashMap<String, Map<String, Boolean>>();
		// Add agent status
		Map<String, Boolean> agentStatus = new HashMap<String, Boolean>();
		agentStatus.put(Constant.AGENT, true);
		serviceStatus.put(Constant.AGENT, agentStatus);

		// iterate over the files.
		for (File file : files) {
			LOGGER.info("Service File:" + file.getName());
			String componentName = FilenameUtils.getBaseName(file.getName());
			try {
				// java XML context object.
				JAXBContext jc = JAXBContext
						.newInstance(ServiceConfiguration.class);
				// Input stream reader.
				InputStream inputStream = new FileInputStream(file);
				// Buffered reader
				BufferedReader br = new BufferedReader(new InputStreamReader(
						inputStream));
				// Creating un marshaller
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				// Getting component services
				ServiceConfiguration services = (ServiceConfiguration) unmarshaller
						.unmarshal(br);
				// Adding component service status against component.
				serviceStatus.put(componentName,
						getServiceStatus(services.getTypeServices()));
				LOGGER.info("Service Status Object:" + serviceStatus);
			} catch (FileNotFoundException e) {
				LOGGER.error(e.getMessage(), e);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return serviceStatus;
	}

	public static void main(String[] args) {
		ServiceMonitor sm = new ServiceMonitor();
		ComponentService cs = new ComponentService("abcd", "abcdtype");
		List<Parameter> ps = new ArrayList<Parameter>();
		ps.add(new Parameter());

		sm.collectServiceStatus();
	}

	/**
	 * Getting service status
	 * 
	 * @param typeServices
	 * @return
	 */
	public Map<String, Boolean> getServiceStatus(
			Map<String, List<ComponentService>> typeServices) {
		// Empty service oldServiceStatus.
		Map<String, Boolean> serviceStatus = new HashMap<String, Boolean>();
		// Iterating on the type service oldServiceStatus.
		for (String type : typeServices.keySet()) {
			// Service provider.
			ServiceProvider sp = ActionFactory.getServiceProviderObject(type);
			// Service status.
			serviceStatus.putAll(sp.getServiceStatus(typeServices.get(type)));
		}
		// returning service status.
		return serviceStatus;
	}
}
